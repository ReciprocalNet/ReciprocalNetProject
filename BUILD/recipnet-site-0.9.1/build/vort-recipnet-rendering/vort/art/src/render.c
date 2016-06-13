/*
 *  render.c
 *
 *	do the rendering stuff, hopefully most of what is in here
 * is machine independent. Thanks to Brad Fowlow (brad@vedge.com)
 * for doing the adaptive antialiasing.
 */

#include <stdio.h>
#include <string.h>
#include <math.h>

#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"
#include "random.h"

extern hlist	*trace();
extern void	shade();

extern object	*treeinit();
extern object	*gridinit();

int	linecount;		/* line counter for parser */

object	*oblists[OBJSTACKSIZE], **oblistsp = oblists;	/* object list stack */
object	*oblist;			/* object list */
object	*treeobjs, *otherobjs;	/* bsp-able and non-bsp-able objects */

object	*subobjs;		/* objects that have their raynumber checked */

light	*lights;		/* lights list */

objst	*ostackp;		/* object def stack */

attr	*astackp;		/* attribute stack */

mats	*mstackp;		/* transformation stack */

char	*title;			/* title of picture */

char	currentfile[MESLEN];	/* name of current "input" file */

float	huge_dist;		/* max dist we can travel */

int	perspective = TRUE;	/* projection - perspective or orthographic */
int	raysperpix = 1;		/* number of rays per pixel */
int	maxhitlevel = 6;	/* max number of rays traced in reflection */
long	filetype = PIX_RLE;	/* output file format */

short	raynumber = 1;		/* initial raynumber */

float	screenx = 1.0,		/* half screen width */
	screeny = 1.0;		/* half screen height */

real	tolerance;		/* tolerance value */

int	maxtreedepth = 35;	/* max depth for kd tree */

/*
 * ambient colour and background pixel stuff
 */
colour	ambient;
colour	backcol = { 0.0, 0.0, 0.0 };

/*
 * light falloff
 */
float	falloff = 0.0;

/*
 * global refractive index
 */
float	ri = 1.0;

/*
 * pointer to the free list for hit structures
 */
hlist	*fhlist;

/*
 * details for the primary rays
 */
vector	org;

vector	up = { 0.0, 1.0, 0.0 };

float	focallength = -1.0;

matrix	trans = {
	1.0, 0.0, 0.0, 0.0,
	0.0, 1.0, 0.0, 0.0,
	0.0, 0.0, 1.0, 0.0,
	0.0, 0.0, 0.0, 1.0
};

/*
 * shading stack
 */
shadedata	*sstack, *sstackp;

/*
 * haze stuff
 */
float	fogfactor = 0.0,		/* the fog factor */
	rfactor = 0.03;			/* the mysterious "r" factor */


colour	hazecolour = { 1.0, 1.0, 1.0 };	/* the haze colour */

float	sourceradius;		/* For ripple texture */

/*
 * random number pointers
 */
float	*randp = randtable,
	*erandp = &randtable[sizeof(randtable) / sizeof(float)];

/*
 * function pointer tables for intersections, normals, etc...
 */
hlist	*(*intersects[NUM_OBJS])();
void	(*normal[NUM_OBJS])();
void	(*tilefun[NUM_OBJS])();

int	checkbbox[NUM_OBJS];	/* should we check bounding box of this type */
int	selfshadowing[NUM_OBJS];	/* is this object self shadowing? */

int	shadow_rays = FALSE;	/* are rays for shadow testing? */
 
/*
 * antialiasing stuff
 */
int		pixelgrid = FALSE;	/* pixel grid on or off */
int		adaptive = FALSE;	/* adaptive antialiasing on or off */

float		aathreshold = 0.03;	/* Max contrast before we jitterbug. */

/* 
 * verbose mode stuff.
 */
int		chatty = FALSE;

/*
 * gridstuff
 */
int		gridsize[DIMS] = { 20, 20, 20 };	/* dimensions of grid */
			      
/*
 * render
 *
 *	so much for the start up, now set up the stack, parse the file
 * and do the ray tracing.
 */
render(infile, im, xsize, ysize, xstart, xend, ystart, yend)
	FILE	*infile;
	image	*im;
	int	xsize, ysize;
	int	xstart, xend, ystart, yend;
{
	expression	*e;
	object		*o, *nexto;
	unsigned char	*red, *green, *blue, *alpha;
	float		k1, k2;
	int		i, x, y;
	char		buf[BUFSIZ];

	/*
	 * set field of view.
	 */
#ifndef OLDFOV
	if (xsize > ysize)
		focallength = -(float)xsize / (float)ysize;
	else
		focallength = -(float)ysize / (float)xsize;
#endif

	/*
	 * take care of those precision problems.
	 */
	huge_dist = HUGE_DIST;

	/*
	 * initialise matrix, attribute and texture stacks
	 */
	sourceradius = 100.0;

	mstackp = (mats *)smalloc(sizeof(mats));
	mident4(mstackp->d.om);
	mident4(mstackp->d.vm);
	mident4(mstackp->d.obj2ray);
	mident4(mstackp->d.ray2obj);
	mstackp->d.maxscale = 1.0;
	mstackp->d.nscales.x = mstackp->d.nscales.y = mstackp->d.nscales.z = 1.0;
	mstackp->d.vmused = TRUE;
	mstackp->d.omused = TRUE;

	mstackp->lst = mstackp->nxt = (mats *)NULL;

	astackp = (attr *)smalloc(sizeof(attr));

	astackp->d.s = (surface *)smalloc(sizeof(surface));
	astackp->d.s->falloff = 0.0;
	astackp->d.s->alpha = 1.0;
	astackp->d.s->trans.r = astackp->d.s->trans.g = 0.0;
	astackp->d.s->trans.b = 0.0;
	astackp->d.s->refl.r = astackp->d.s->refl.g = 0.0;
	astackp->d.s->refl.b = 0.0;
	astackp->d.s->c.r = astackp->d.s->c.g = astackp->d.s->c.b = 1.0;
	astackp->d.s->a.r = astackp->d.s->a.g = astackp->d.s->a.b = 0.1;
	astackp->d.s->ri = astackp->d.s->kd = 1.0;
	astackp->d.s->ks = 0.0;
	astackp->d.s->shadows = TRUE;
	astackp->d.txtlist = (texture *)NULL;
	astackp->d.shadows = TRUE;
	astackp->d.slevel = 0;
	astackp->d.options = 0;

	astackp->lst = astackp->nxt = (attr *)NULL;

	ostackp = (objst *)smalloc(sizeof(objst));
	ostackp->sym = (symbol *)NULL;

	ostackp->lst = ostackp->nxt = (objst *)NULL;

	linecount = 1;

	/*
	 * initialise variable symbol table
	 */
	e = (expression *)smalloc(sizeof(expression));
	e->type = EXP_INT;
	e->u.i = 'x';
	defvar("x", e);

	e = (expression *)smalloc(sizeof(expression));
	e->type = EXP_INT;
	e->u.i = 'y';
	defvar("y", e);

	e = (expression *)smalloc(sizeof(expression));
	e->type = EXP_INT;
	e->u.i = 'z';
	defvar("z", e);

	/*
	 * initialise object symbol table
	 */
	defobj("geometry", GEOMETRY, (details *)NULL);
	defobj("sphere", SPHERE, (details *)NULL);
	defobj("ellipsoid", ELLIPSOID, (details *)NULL);
	defobj("box", BOX, (details *)NULL);
	defobj("torus", TORUS, (details *)NULL);
	defobj("algebraic", ALGEBRAIC, (details *)NULL);
	defobj("cylinder", CYLINDER, (details *)NULL);
	defobj("cone", CONE, (details *)NULL);
	defobj("superquadric", SUPERQUADRIC, (details *)NULL);
	defobj("ring", RING, (details *)NULL);
	defobj("disk", RING, (details *)NULL);
	defobj("light", LIGHT, (details *)NULL);
	defobj("polygon", POLYGON, (details *)NULL);
	defobj("patch", PATCH, (details *)NULL);
	defobj("blobby", BLOBBY, (details *)NULL);

	yyparse();		/* read in model and set oblist */

	fclose(infile);		/* get rid of temp file (unlinked above) */

#ifdef TIME_STATS
	printtime("Preprocessing time		:");
#endif
	/*
	 * set final details in image pointer and write header out
	 */
	imagetype(im) = filetype;

	if (title == (char *)NULL)
		titlelength(im) = 0;
	else {
		titlelength(im) = strlen(title) + 1;
		imagetitle(im) = title;
	}

	imagebackgnd(im).r = backcol.r * 255.0;
	imagebackgnd(im).g = backcol.g * 255.0;
	imagebackgnd(im).b = backcol.b * 255.0;

	writeheader(im);

	/*
	 * initialise shade stack
	 */
	sstack = (shadedata *)scalloc(sizeof(shadedata), maxhitlevel);

	sstack[0].ri = ri;
	sstack[0].falloff = falloff;

	sstackp = sstack;

	treeobjs = otherobjs = (object *)NULL;

	oblist = *oblistsp;

	for (o = oblist; o != (object *)NULL; o = nexto) {
		nexto = o->nxt;
		o->lastray.raynumber = 0;
					/* algebraic surface or contains one */
		if (o->type == NULL_OBJ) 
			free(o);
		else if (o->type == ALGEBRAIC || o->bb.max[X] == huge_dist) {
			o->nxt = otherobjs;
			otherobjs = o;
		} else {
			o->nxt = treeobjs;
			treeobjs = o;
		}
	}

	if (treeobjs != (object *)NULL && treeobjs->nxt != (object *)NULL) {
		oblist = treeinit(treeobjs);

		/*
		 * add the interior objects to the general object list.
		 */
		if (subobjs != (object *)NULL) {
			for (o = subobjs; o->nxt != (object *)NULL; o = o->nxt)
				;

			o->nxt = treeobjs;
		} else {
			subobjs = treeobjs;
		}

		tolerance = sqrt(sqr(oblist->bb.max[X] - oblist->bb.min[X]) + sqr(oblist->bb.max[Y] - oblist->bb.min[Y]) + sqr(oblist->bb.max[Z] - oblist->bb.min[Z])) / 2.0 * TOLERANCE;

		for (o = treeobjs; o != (object *)NULL; o = o->nxt) {
			o->bb.min[X] -= tolerance;
			o->bb.min[Y] -= tolerance;
			o->bb.min[Z] -= tolerance;

			o->bb.max[X] += tolerance;
			o->bb.max[Y] += tolerance;
			o->bb.max[Z] += tolerance;
		}

		oblist->obj.tree->bb.min[X] -= tolerance;
		oblist->obj.tree->bb.min[Y] -= tolerance;
		oblist->obj.tree->bb.min[Z] -= tolerance;

		oblist->obj.tree->bb.max[X] += tolerance;
		oblist->obj.tree->bb.max[Y] += tolerance;
		oblist->obj.tree->bb.max[Z] += tolerance;

		oblist->nxt = otherobjs;
	} else if (treeobjs != (object *)NULL) {
		oblist = treeobjs;
		tolerance = sqrt(sqr(oblist->bb.max[X] - oblist->bb.min[X]) + sqr(oblist->bb.max[Y] - oblist->bb.min[Y]) + sqr(oblist->bb.max[Z] - oblist->bb.min[Z])) / 2.0 * TOLERANCE;
		oblist->nxt = otherobjs;
	} else {
		oblist = otherobjs;
		tolerance = TOLERANCE;
	}

	/*
	 * set up intersection and normal function pointers
	 */
	spheretabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	boxtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	torustabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	cyltabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	conetabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	ellipstabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	geomtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	polytabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	supertabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	ringtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	csgtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	algtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	hfieldtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	treetabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	gridtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	patchtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);
	blobtabinit(intersects, normal, tilefun, checkbbox, selfshadowing);

	if (oblist != (object *)NULL) {
		sprintf(buf, "world bounding box: %+11.4f %+11.4f %+11.4f\n                    %+11.4f %+11.4f %+11.4f\n",
		oblist->bb.min[X], oblist->bb.min[Y], oblist->bb.min[Z],
		oblist->bb.max[X] , oblist->bb.max[Y] , oblist->bb.max[Z]);
	}

#ifndef MACINTOSH
	if (chatty)
		printf(buf);
#endif
	message(buf);

	if (adaptive)
		dotrace_adaptive(im, raysperpix, xsize, ysize, xstart, xend, ystart, yend);
	else if (pixelgrid)
		dotrace_grid(im, raysperpix, xsize, ysize, xstart, xend, ystart, yend);
	else
		dotrace_raw(im, xsize, ysize, xstart, xend, ystart, yend);

	closeimage(im);
}

