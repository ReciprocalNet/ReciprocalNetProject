#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>
#include <sys/wait.h>
#include <errno.h>
#include <sys/times.h>
#include <sys/resource.h>

#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"
#include "random.h"
#include "readall.h"

extern hlist	*trace();
extern void	shade();

static u_int32_t dotrace1(int x, int y);
static u_int32_t dotrace2(int x, int y);
static void traceblock(u_int32_t *mp, int xstart, int xend, int ystart, int yend);

extern object	*treeinit();

FILE	*logfile;

int	linecount;		/* line counter for parser */

object	*oblists[OBJSTACKSIZE], **oblistsp = oblists;	/* object list stack */
object  *oblist;		/* object list */
object	*subobjs;		/* objects that have their raynumber checked */
object  *treeobjs, *otherobjs;	/* bsp-able and non-bsp-able objects */

light	*lights;		/* lights list */

objst	*ostackp;		/* object def stack */

attr	*astackp;		/* attribute stack */

mats	*mstackp;		/* transformation stack */

char	*title;			/* title of picture */

char	currentfile[MESLEN];	/* name of current "input" file */

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
 * huge distance
 */
float	huge_dist = HUGE_DIST;

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
 * temporary file pointer
 */
char	*tmpname;

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

/*
 * x, y offsets, scale, and sampleing
 */
static int	xoff, yoff;
static float	xscale, yscale;
static int	ysperpix, xsperpix;

/*
 * frame id extension and frame number
 */
char	*frameid = "000";
int	frameno = 0;

/*
 * default name
 */
char	*defname = "pic.pix";

int shadow_rays	= FALSE;		/* are rays for shadow testing? */
 
/*
 * antialiasing stuff
 */
int		pixelgrid = FALSE;	/* pixel grid on or off */
static int	extrarays,      	/* spare rays for jittering */
        totrays;        	/* total number of rays per pixel */

static pixel	*topline;       /* scanline of sample grid at top of current scanline */

static pixel	others[2];      /* bottom 2 samples of pixel grid */

static float	averagefactor;	/* number to divide final samples through by */
                  
int		adaptive;
float		aathreshold;

/*
 * statistics stuff
 */
#ifdef STATS
static int	totnodes;
static int	totleafs;
static int	neverentered;
static int	maxfinaldepth;
static int	maxinleaf;
static int	mininleaf;
static int	emptynodes;
static int	numobjptrs;
#endif

#ifdef TIME_STATS
static float    prev_time;
static time_t  start_time, rprev_time, tstart_time;
static struct tms       buffer;
#endif

static int	totobjs;

extern FILE	*yyin;

/*
 * debugging flag.
 */
int	debug = FALSE;

/*
 * gridstuff
 */
int		usegrid = FALSE;			/* default to kdtree */
int		gridsize[DIMS] = { 20, 20, 20 };	/* dimensions of grid */

/*
 * usage
 *
 *	the usage message - in this case a do nothing operation.
 */
usage()
{
}

/*
 * readshort
 *
 *      read a short value from a socket.
 */
static int readshort(sock)
    int	sock;
{
    unsigned short  val;
    int		count;

    count = 0;

    while ((count += read(sock, (char *)&val + count, sizeof(unsigned short) - count)) != sizeof(unsigned short) && count >= 0)
        ;
        
    return((int)ntohs(val));
}

/*
 * main driver
 */
main(int ac, char *av[]) {
    int		len, indx, preprocess, debug, tempfd;
    char	c, *p, *name, buf[BUFSIZ], *path, *scenename;
    FILE	*infile, *logf;
    u_int32_t	*map;
    int		i, xsize, ysize, xstart, xend, ystart, yend;
    int		xfrag, yfrag, fragsize, thissize;
    object	*o, *nexto;
    expression	*e;

    /* stdout and stderr streams must be redirected so as to not send garbage
       back to the server.  Filehandle 1 is still used to send data back,
       however, so we don't want to close it. */
    freopen("/dev/null", "a", stderr);
    stdout = stderr;
#ifdef TIME_STATS
    times(&buffer);
    start_time = buffer.tms_utime + buffer.tms_stime;
#endif
    debug = (1==0);
    if (ac > 1) {
        if (strcmp(av[1], "-debug") == 0) debug = (1==1);
    }
    buf[BUFSIZ - 1] = '\0';

    /* Attempt to improve the group's priority, but ignore any error */
    setpriority(PRIO_PGRP, 0, getpriority(PRIO_PROCESS, 0) - 1);

    /* open the log file */
    logf = fopen("/tmp/dartlog", "w");
    logfile = logf;

    /*
     * read in working directory for in.artd
     */
    len = readshort(0);

    path = (char *) smalloc(len + 1);
    readall(0, path, len);
    path[len] = 0;

    chdir(path);
    snprintf(buf, BUFSIZ - 1, "%s\n", path);
    free(path);
    message(buf);

    /*
     * read in name of scene file
     */
    len = readshort(0);

    scenename = (char *) smalloc(len+1);
    readall(0, scenename, len);
    scenename[len] = '\0';
    snprintf(buf, BUFSIZ - 1, "%s\n", scenename);
    message(buf);

    /*
     * get size of total image in pixels.
     */
    xsize = readshort(0);
    ysize = readshort(0);

    /*
     * get flags
     */
    preprocess = readshort(0);

    snprintf(buf, BUFSIZ - 1, "image size %d %d\n", xsize, ysize);
    message(buf);
    fflush(logf);

    xstart = 0;
    xend = xsize - 1;

    ystart = ysize - 1;
    yend = 0;

#ifndef OLDFOV
    if (xsize > ysize)
        focallength = -(float)xsize / (float)ysize;
    else
        focallength = -(float)ysize / (float)xsize;
#endif

                	/* check for other options */
    sourceradius = 100.0;

    mstackp = (mats *)smalloc(sizeof(mats));
    mident4(mstackp->d.om);
    mident4(mstackp->d.vm);
    mident4(mstackp->d.obj2ray);
    mident4(mstackp->d.ray2obj);
    mstackp->d.maxscale = 1.0;
    mstackp->d.nscales.x = mstackp->d.nscales.y = mstackp->d.nscales.z = 1.0;

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

    if (preprocess) {
        if (debug) {
            snprintf(buf, BUFSIZ - 1, "preprocessing scene file... ");
            message(buf);
        }
        name = (char *) smalloc(strlen(scenename) + 12);
        sprintf(name, "/tmp/%sXXXXXX", scenename);

        if ((tempfd = mkstemp(name)) < 0) {
            snprintf(buf, BUFSIZ - 1, "in.artd: unable to open temporary file %s.\n", name);
            fatal(buf);
	} else if ((infile = fdopen(tempfd, "w")) == NULL) {
		perror("in.artd");
		fatal("");
        }

        prepro(scenename, infile);

        fclose(infile);
        tmpname = name;
        if (debug) {
            snprintf(buf, BUFSIZ - 1, "done.\n");
            message(buf);
        }
    } else {
        if (debug) {
            snprintf(buf, BUFSIZ - 1, "no preprocessing.\n");
            message(buf);
        }
        name = strdup(scenename);
        if (name == NULL) {
            snprintf(buf, BUFSIZ - 1, "in.artd: memory allocation failure.\n");
            fatal(buf);
        }
        tmpname = NULL;
    }

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

    if (debug) {
        snprintf(buf, BUFSIZ - 1, "processing scene file %s\n", name);
        message(buf);
    }
    if ((yyin = fopen(name, "r")) != (FILE *)NULL) {
        fflush(logf);
        if (preprocess)
            unlink(name);

        yyparse();
        fflush(logf);

        fclose(yyin);
    } else {
        snprintf(buf, BUFSIZ - 1, "in.artd: unable to open file %s.\n", name);
        fatal(buf);
    }

    if (debug) {
        snprintf(buf, BUFSIZ - 1, "initializing data objects\n", name);
        message(buf);
    }
    
#ifdef TIME_STATS
    printtime("Preprocessing time		:");
#endif

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

        if (o->type == ALGEBRAIC) {
            o->nxt = otherobjs;
            otherobjs = o;
            totobjs++;
        } else if (o->type == NULL_OBJ) 
            free(o);
        else {
            o->nxt = treeobjs;
            treeobjs = o;
            totobjs++;
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

    xoff = xsize / 2;
    yoff = ysize / 2;

    if (xsize > ysize)
        xscale = yscale = ysize / 2;
    else
        xscale = yscale = xsize / 2;


    if (oblist != (object *)NULL)
        snprintf(buf, BUFSIZ - 1, "world bounding box: %+11.4f %+11.4f %+11.4f\n                    %+11.4f %+11.4f %+11.4f\n",
        oblist->obj.tree->bb.min[X], oblist->obj.tree->bb.min[Y], oblist->obj.tree->bb.min[Z],
        oblist->obj.tree->bb.max[X] , oblist->obj.tree->bb.max[Y] , oblist->obj.tree->bb.max[Z]);

    message(buf);


    xfrag = readshort(0);
    yfrag = readshort(0);
    fragsize = xfrag * yfrag * sizeof(u_int32_t);

    fprintf(logf, "frag size %d %d\n", xfrag, yfrag);
    fflush(logf);

#ifdef TIME_STATS
    times(&buffer);
    tstart_time = buffer.tms_utime + buffer.tms_stime;
#endif

    topline = (pixel *)scalloc(sizeof(pixel), xsize + 3);

    if (pixelgrid) {
        extrarays = raysperpix - 1;
        totrays = extrarays + 4;
        averagefactor = 255.0 / totrays;
    }

    map = (u_int32_t *)smalloc(fragsize);

        /* receive instructions from the remote master */
    read(0, &c, 1);
    while (c != 'Q') {
        /* receive fragment definition */
        xstart = readshort(0);
        xend = readshort(0);
        ystart = ysize - readshort(0);
        yend = ysize - readshort(0);

        /* log it */
        fprintf(logf, "%c - %d %d %d %d\n", c, xstart, xend, ystart, yend);
        fflush(logf);

	/* Has the master gone psycho and sent us too big a fragment request? */
        thissize = (xend-xstart+1) * (ystart-yend+1) * sizeof(u_int32_t);
	if ( thissize > fragsize || thissize < 0 ) {
            if (debug) {
                fprintf(logf, "  -- invalid fragment size --\n");
                fflush(logf);
            }
            break;
        }

        traceblock(map, xstart, xend, ystart, yend);

        /* return the fragment to the master and get next instruction */
        if (debug) {
            fprintf(logf, "sending %d-byte fragment data\n", fragsize);
            fflush(logf);
        }
        write(1, "D", 1);
        write(1, map, thissize);
        read(0, &c, 1);
    }

#ifdef TIME_STATS
    printtime("Total time taken		:");
#endif

#ifdef STATS
    snprintf(buf, BUFSIZ - 1, "Total number of objects		: %d\n", totobjs);
    message(buf);

    if (treeobjs != (object *)NULL) {
        mininleaf = totobjs;

        getstats(oblist->obj.tree);

        snprintf(buf, BUFSIZ - 1, "Total number of tree nodes	: %d\n", totnodes);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Number of leaf nodes not entered: %d\n", neverentered);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Number of leaf nodes entered	: %d\n", totleafs);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Number of object pointers	: %d\n", numobjptrs);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Min number of objects in a leaf	: %d\n", mininleaf);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Max number of objects in a leaf	: %d\n", maxinleaf);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Max depth of a leaf node	: %d\n", maxfinaldepth);
        message(buf);
        snprintf(buf, BUFSIZ - 1, "Number of empty nodes		: %d\n", emptynodes);
        message(buf);
    }
#endif
    fprintf (logf, "task complete.\n");
    fflush (logf);
    free(name);
    free(scenename);
    exit(ALLOK);
}

/*
 * dotrace1
 *
 *	Generate a straight no-nonsense sampling for the image; if you
 * like weird filters and postprocessing this is the one for you!
 */
static u_int32_t dotrace1(x, y)
    int		x, y;
{
    u_int32_t	rgba;
    pixel	pix;
    object	*o;

    gensample((x - xoff + 0.5) / xscale, (y - yoff + 0.5) / yscale, &pix);

    if (raynumber < 0) {
        for (o = subobjs; o != (object *)NULL; o = o->nxt)
            o->lastray.raynumber = 0;
        raynumber = 1;
    }

    rgba =   (((u_int32_t) (pix.r * 255.0)) << 24)
           | (((u_int32_t) (pix.g * 255.0)) << 16)
           | (((u_int32_t) (pix.b * 255.0)) <<  8)
           |  ((u_int32_t) (pix.a * 255.0));
    return htonl(rgba);
}

/*
 * dotrace2
 *
 *	traces the neccessary number of rays and calculates r,
 * g, b, and alpha info averaging the samples at the end.
 */
static u_int32_t dotrace2(x, y)
    int		x, y;
{
    u_int32_t	rgba;
    int	i;
    pixel	sample;
    float	red, green, blue, alpha;
    object	*o;

    red = green = blue = alpha = 0.0;

    gensample((x + 1 - xoff) / xscale, (y - yoff) / yscale, &others[1]);

    red = (topline[x].r + topline[x + 1].r + others[0].r + others[1].r);
    green = (topline[x].g + topline[x + 1].g + others[0].g + others[1].g);
    blue = (topline[x].b + topline[x + 1].b + others[0].b + others[1].b);
    alpha = (topline[x].a + topline[x + 1].a + others[0].a + others[1].a);

    topline[x] = others[0];
    others[0] = others[1];

    if (extrarays) {
        gensample((x - xoff + 0.5) / xscale, (y - yoff + 0.5) / yscale, &sample);
        red += sample.r;
        green += sample.g;
        blue += sample.b;
        alpha += sample.a;

        for (i = 1; i != extrarays; i++) {
            gensample((x - xoff + randnum()) / xscale, 
                      (y - yoff + randnum()) / yscale,
                      &sample);
            red += sample.r;
            green += sample.g;
            blue += sample.b;
            alpha += sample.a;
        }
    }

    if (alpha != 0.0) {
        rgba =  (((u_int32_t) (red * averagefactor + randnum())) << 24)
             |(((u_int32_t) (green * averagefactor + randnum())) << 16)
             |(((u_int32_t) (blue * averagefactor + randnum()))  <<  8)
             | ((u_int32_t) (alpha * averagefactor));
    } else {
        rgba = (((u_int32_t) (red * averagefactor))   << 24)
             | (((u_int32_t) (green * averagefactor)) << 16)
             | (((u_int32_t) (blue * averagefactor))  <<  8)
             |  ((u_int32_t) (alpha * averagefactor));
    }

    if (raynumber < 0) {
        for (o = subobjs; o != (object *)NULL; o = o->nxt)
            o->lastray.raynumber = 0;
        raynumber = 1;
    }
    return htonl(rgba);
}

static void traceblock(u_int32_t *mp, int xstart, int xend, int ystart, int yend) {
    int x, y;
    float k1, k2;

    if (pixelgrid) {
        k2 = (ystart + 1 - yoff) / yscale;
        for (x = xstart; x <= xend + 1; x++)
            gensample((x - xoff) / xscale, k2, &topline[x]);
        k1 = (xstart - xoff) / xscale;
        for (y = ystart; y >= yend; y--) {
            gensample(k1, (y - yoff) / yscale, &others[0]);
            for (x = xstart; x <= xend; x++) {
                *mp = dotrace2(x, y);
                mp++;
            }
            topline[xend + 1] = others[1];
        }
    } else {
        for (y = ystart; y >= yend; y--) {
            for (x = xstart; x <= xend; x++) {
                *mp = dotrace1(x, y);
                mp++;
            }
        }
    }
}

#ifdef STATS
/*
 * getstats
 *
 *	calculate the statistics for the kd-tree pointed to by 
 * root.
 */
getstats(root)
    stree	*root;
{
    olist	*obs;
    int	count;

    totnodes++;

    if (root->type == SPLITABLELEAF)
        neverentered++;
    else if (root->type == LEAF) {
        if (maxfinaldepth < root->u.leaf.depth)
            maxfinaldepth = root->u.leaf.depth;
        if (root->u.leaf.oblist == (olist *)NULL)
            emptynodes++;
        else {
            count = 0;
            for (obs = root->u.leaf.oblist; obs != (olist *)NULL; obs = obs->nxt)
                count++;
            numobjptrs += count;
            if (mininleaf > count)
                mininleaf = count;
            if (maxinleaf < count)
                maxinleaf = count;
            totleafs++;
        }
    } else {
        getstats(root->u.branch.left);
        getstats(root->u.branch.right);
    }
}
#endif

#ifdef TIME_STATS
/*
 * printtime
 *
 *      print out how much time has been taken up.
 */
printtime(s)
    char    *s;
{
    float           diff;
    time_t          now;
    char            buf[BUFSIZ];

    times(&buffer);

    diff = buffer.tms_utime + buffer.tms_stime - start_time;

    buf[BUFSIZ - 1] = '\0';
    snprintf(buf, BUFSIZ - 1, "%s %.2f seconds.\n", s, diff / 60.0);       
    message(buf);
}

#endif

