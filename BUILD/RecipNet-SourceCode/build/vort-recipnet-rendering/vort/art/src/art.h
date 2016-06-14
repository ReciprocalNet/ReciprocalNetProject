extern char	*smalloc();
extern char	*scalloc();

#ifdef DEBUG
#define scalloc(a, b)	scalloc(a, b, __FILE__, __LINE__)
#define smalloc(a)	smalloc(a, __FILE__, __LINE__)
#endif

#include <unistd.h>
#include <string.h>
#define bcopy(s1, s2, l)        memcpy(s2, s1, l)
#define bzero(s, l)     memset(s, 0, l)
#define rindex    strrchr

/*
 * include file for pixel data
 */
#include "vort.h"
#include "objs.h"
#include "status.h"

/*
 * types and defs for the ray tracer
 */

#define	TRUE	1
#define	FALSE	0

#define	MESLEN	100

/*
 * tolerance value used for tolerance calculation also serves
 * as minimum distance we must be from the last surface before
 * we register another hit.
 */
#define	TOLERANCE	0.0001

/*
 * basic raddi for bounding spheres used for calculating object
 * tolerance values
 */
#define	BS_SQRT2	1.414213563
#define	BS_SQRT0PNT5	0.707106782
#define	BS_SQRT1PNT25	1.118033990

/*
 * where we put infinity
 */
#define	HUGE_DIST	1.0e24

typedef float	matrix[4][4];
typedef float	mat3x3[3][3];
typedef float	mat2x3[2][3];

typedef struct v {
	float	x, y, z;
} vector;

typedef struct uvv {
	float	u, v;
} uvvec;

typedef struct col {
	float	r, g, b;
} colour;

typedef struct pix {
	float	r, g, b, a;
} pixel;

typedef struct td {
	vector		trans,
			nscales;
	mat3x3		mat;
} transdata;

typedef struct w {
	vector  center;
	float   amp;
	float   len;
	float   phase;
	float   damp;
	struct w        *nxt;
} wlist;

/*
 * sampling masks.
 */
typedef struct {
	int      size;
	float    magnitude;
	uvvec    *points;
} sampleMask;

/*
 * typedefs for texture, bumps, and tile patterns
 */
typedef struct {
	float		blend;
	int		octaves;	/* range for noise function */
	colour		blendcolour;
	float		var[3];
	unsigned short	pixw, pixh;
	char		*map;
} proctxt;

typedef struct {
	float		blend;
	float		scalew, scaleh;
	char		*map;
	unsigned short	pixw, pixh;
} tiletxt;

typedef struct {
	char    flags;
	float   blockwidth;
	float   blockheight;
	float   blockdepth;
	float   gapwidth;
	float	mixval;
	colour  gapcolour;
	colour  blendcolour;
} blocktxt;

typedef struct t {
	float		(*txtfun)();	/* texturing function */
	union {
		wlist		*w;		/* List of wave sources */
		proctxt		*p;		/* procedural texture stuff */
		tiletxt		*t;		/* tile texture stuff */
		blocktxt	*b;		/* Block texture */
	} u;
	int		modulate;
	transdata	*td;
	struct t	*nxt;
} texture;
	
/*
 * general structure for a surface
 */
typedef struct surf {
	colour	c, a;			/* surface and ambient light colour */
	colour	trans,			/* transparency */
		refl;			/* reflection */
	float	alpha;			/* alpha value */
	float	kd, ks;			/* diffuse, specular */
	char	shadows;		/* shadows on or off */
	short	ksexp;			/* specular exponent */
	float	ri,			/* refractive index */
		falloff;		/* falloff in the media */
} surface;

/*
 * attribute stack structure
 */
typedef struct at {
	struct {			/* stack details */
		int		options;	/* shading options */
		int		shadows;	/* shadows on or off */
		texture		*txtlist;	/* texturing info */
		surface		*s;		/* surface properties */
		int		slevel;		/* level of most recent suface data */
	} d;
	struct at	*lst, *nxt;	/* pointer to elements above & below */
} attr;

/*
 * matrix stack structure
 */
typedef struct mt {
	struct {			/* stack details */
		matrix		ray2obj,	/* ray to object transformation */
				obj2ray;	/* object to ray transformation */
		matrix		om;		/* current object matrix */
		char		omused;		/* was om used at this level? */
		matrix		vm;		/* current viewing matrix */
		char		vmused;		/* was vm used at this level? */
		float		maxscale;	/* maximum scale applied to an object */
		vector		nscales;	/* scaling factors for normals */
		transdata	*td;		/* transformation data */
		int		tdlevel;	/* level of most recent trans data */
	} d;
	struct mt	*lst, *nxt;	/* pointer to elements above & below */
} mats;

/*
 * object def stack structure.
 */
typedef struct ot {
	struct sy	*sym;
	struct ot	*lst, *nxt;
} objst;

/*
 * shader stack structure
 */
typedef struct st {
	float   ri;
	float   falloff;
} shadedata;

/*
 * nodes for storing hit lists
 */

#define	ENTRY	1
#define	EXIT	2

typedef struct hl {
	float		t;
	struct o	*obj;
	int		type;
	struct hl	*nxt;
} hlist;

/*
 * defs for lights
 */
#define	POINT		0
#define	DIRECTIONAL	1
#define	DISTANT		2

typedef struct l {
	int		type;
	vector		org;
	vector		dir;
	colour		c;
	float		rad;
	float		cosedge;
	float		cosin;
	float		beamdist;
	int		rays;
	int		shadows;
	struct o        **lasthits;
	struct l	*nxt;
} light;

/*
 * the ray structure
 */
#define	PRIMARY_RAY		0
#define	SHADOW_RAY		1
#define	REFLECTION_RAY		2
#define	TRANSPARENCY_RAY	3

typedef struct r {
	int		type;
	short		raynumber;
	float		maxt;
	float		ri, fallof;
	vector		org, dir;
	struct o	*orgobj;
	int		orgtype;
} ray;

/*
 * major axis flags
 */
#define	XAXIS	0
#define	YAXIS	1
#define	ZAXIS	2

#define	DIMS	3

/*
 * hit types
 */

#define	NXFACE	0		/* face faces negative axis */
#define	NYFACE	1
#define	NZFACE	2

#define	PXFACE	3		/* face faces positive axis */
#define	PYFACE	4
#define	PZFACE	5

#define	SIDE	6		/* side of quadric */

/*
 * bounding shapes details
 */

typedef struct b {
	float	max[DIMS];
	float	min[DIMS];
} bbox;

/*
 * object model definitions
 */

typedef struct s {
	float	radsqu;
	vector	orig;
} sphere;

typedef struct rg {
	vector	n;
	float	intrad, intradsqu;
} ring;

typedef struct to {
	float	cnst;
} torus;

typedef struct sq {
	int	ord;
} superquadric;

/*
 * typedefs for patches.
 */

#define	PX	0
#define	PY	1
#define	PZ	2
#define	PLEAF	3

typedef struct sn {
	int	type;
	bbox	bb;
	union {
		struct {
			float		maxu, minu;
			float		maxv, minv;
			float		midu, midv;
		} leaf;
		struct {
			float		splitval;
			struct sn	*left, *right;
		} branch;
	} u;
} subnode;

typedef struct pt {
	float	u, v;
	matrix	geomx, geomy, geomz;
	matrix	ogeomx, ogeomy, ogeomz;
	matrix	basis;
	subnode	*tree;
} patch;

/*
 * typedefs for height fields, these use a quad tree algorithm
 */
#define	HBRANCH	0
#define	HLEAF	1

typedef struct hvt {
	char		axis;		/* axis code */
	bbox		bb;		/* bounding box */
	vector		n;		/* surface normal */
	float		cnst;		/* constant factor */
	mat2x3		mat;		/* 2x3 matrix for triangles */
	vector		*norms[3];	/* normals - if phong shaded */
} htriangle;

typedef struct hfn {
	char	type, used;
	float	minz, maxz;
	union {
		htriangle	*triangles;
		struct hfn	*subtree;
	} u;
} hnode;

/*
 * type for triangle cache - used on a circular basis
 */
#define MAXTRIS		2500

typedef struct htl {
	hnode		*parent;
	vector		cnorm;
	htriangle	tris[4];
	struct htl	*nxt;
} trilist;

/*
 * type for hnode cache - allocated on a least recently used.
 *
 *	the values for reset cuttoff should be greater than the minimum
 * recursion level the intersection routine is likely to go to (roughly
 * this value is 2 * n where 2**n is the size of the heightfield).
 *
 *	Note: the code assumes that when COLLECT_CUTOFF is reached there
 * will be enough non-recently used nodes in the list for the free node
 * count to get above RESET_CUTOFF.
 */
#define MAXNODES	5000

#define	RESET_CUTOFF	500	/* stage at which we clear the used field */
#define COLLECT_CUTOFF	300	/* stage at which we collect unused nodes */

typedef struct hftl {
	hnode		*parent;
	hnode		subtree[2];
	struct hftl	*nxt;
} nodelist;

typedef struct bhfn {
	int		xsize;
	int		ysize;
	float		*zvals;
	hnode		*hfnode;
	float		*colours;
	float		w1, w2;
	htriangle	*obj;
	vector		*norms;
	trilist		*tris;
	int		reset;
	int		freecount;
	nodelist	*treenodes, *freenodes;
} heightfield;

/*
 * typedefs for polygons and polygon models
 */
#define	MAXVERTS	256		/* maximum number of vertices */

#define	BACKFACING	0x1		/* backfacing bit */
#define	PHONGSHADING	0x2		/* phong shading bit */

typedef struct pgy {
	unsigned char	axis,		/* major axis of normal */
			backfacing;	/* backfacing? */
	unsigned short	nsides;		/* number of sides */
	float		cnst;		/* const for the polygon's plane */
	vector		n, on;		/* normal - ray/object space */
	union {
		mat3x3	*mat;		/* 3x3 matrix for triangles */
		uvvec	*verts;		/* vertices in u, v space */
	} u;
	vector		*norms;		/* vertex normals */
	vector		*colours;	/* vertex colours */
} polygon;

/*
 * types for organisation of polygons in geometry
 */
typedef unsigned short  vertno;

typedef struct fct {
	unsigned char   nsides;		/* number of sides */
	unsigned short	index;		/* generic index */
	vector		n;		/* unnormalised normal */
	float		cnst;		/* const for the faces plane */
	vertno		*vertnos;	/* vertex numbers */
	float		minu, maxu,	/* min max in u value */
			minv, maxv;	/* min max in v value */
} facet;

typedef struct gty {
	bbox            bb;             /* bounding box */
	int             options;        /* shading options */
	vector          *pnorms;        /* normals at each polygon */
	vector          *vnorms;        /* normals at each vertex */
	vertno		**vnormtable;	/* table of indexes into norms */
	vector          *vcolours;      /* colours at each vertex */
	float		*transp;	/* transparency at each vertex */
	vertno		**vcolourtable;	/* table of indexes into colours */
	vector		*colours;	/* colour array */
	unsigned short	*colourtable;	/* indexes into colour array */
	float           *xs, *ys, *zs;  /* x, y, and z values for each vertex */
	facet		*faces;         /* polygon array */
	int             ntris[DIMS];    /* number of triangles in each axis */
	int             npolys[DIMS];   /* number of polygons in each axis */
	facet		*midts[DIMS];   /* middle of triangle array */
	float		midtval[DIMS];  /* smallest minv in top half of tris */
	facet		*midps[DIMS];   /* middle of polygon array */
	float           midpval[DIMS];  /* smallest minv in top half of polys */
} geometry;
			 
/*
 * algebraic equation term def and node structure
 */

typedef struct trm {
	double		coef;
	int		type;
	int		xp, yp, zp;
	double		*xcoefs, *ycoefs, *zcoefs;
	struct trm	*nxt;
} term;

typedef struct pwl {
	int		pw;
	double		*coefs;
	struct pwl	*nxt;
} pwlist;

typedef struct eq {
	int		order;
	int		maxxp, maxyp, maxzp;
	struct o	*obj;
	pwlist		*xpws, *ypws, *zpws;
	term		*trmlist,
			*dxlist, *dylist, *dzlist;	/* terms for the partial derivatives */
} eqn;


/*
 * csg node structure and defs
 */
#define	UNION		1
#define	INTERSECT	2
#define	SUBTRACT	3

#define ADDED		1
#define SUBTRACTED	2	/* need to flip normal in this case */

typedef struct cs {
	int		rightb, leftb;
	struct o	*hitobj, *left, *right;
} csg;

/*
 * the composite object
 */
typedef struct co {
	struct o	*oblist;
} composite;

/*
 * blobbies
 */
typedef struct mbl {
	vector		v;
	float		r, s;
	int		inside;
	int		inside1, inside2;
	struct mbl	*nxt;
} mball;

typedef struct blb {
	float		thresh;
	mball		*mballs;
} blobby;

/*
 * expression defs
 */
#define	EXP_FLOAT	1
#define	EXP_INT		2
#define	EXP_VAR		3
#define	EXP_ADD		4
#define	EXP_SUB		5
#define	EXP_MUL		6
#define	EXP_DIV		7
#define	EXP_UMINUS	8

typedef struct ex {
	int	type;
	union {
		float		f;
		int		i;
		char		*s;
	} u;
} expression;

/*
 * csg tree def
 */

typedef struct cn {
	int	type;
	union {
		struct {
			struct cn	*left, *right;
		} branch;
		struct sy	*sym;
	} u;
} csgnode;

/*
 * user colourmap for textures
 */
typedef struct m {
	char	*m;
	int	n;
} cmap;

#define OBJSTACKSIZE	20		/* max level of subdivision types */

/*
 * details to go in objects
 */
typedef struct det {
	int		type;
	union {
		vector		v;
		colour		c;
		float		f;
		int		i;
		term		*t;
		char		*s;
		texture		*txt;
		struct sy	*sym;
		wlist		*w;
		cmap		*cm;
		struct det	*det;
		struct {
			float	ri, kd, ks;
			int	ksexp;
		} mat;
		mball		*mb;
		struct {
			float	ang;
			int	axis;
		} rot;
		struct {
			struct sy	*sym;
			struct det	*det;
		} obj;
		struct {
			csgnode		*tree;
			struct det	*det;
		} csgobj;
		struct {
			expression	*expr;
			struct det	*stmt;
		} rpt;
		struct {
			matrix		*m;
		} trans;
	} u;
	struct det	*nxt;
} details;

/*
 * symbol table structure
 */
typedef struct sy {
	char	*name;
	char	type;
	union {
		float		f;
		int		i;
		details		*det;
		struct {
			geometry	*geom;
			vector		*pnorms;
			vector		*vnorms;
		} geo;
		struct {
			vector	*norms;
			vertno	**ntable;
		} norm;
		struct {
			vector		*colours;
			unsigned short	*colourtable;
		} cols;
		struct {
			unsigned short	width, height;
			char		*pic;
		} tile;
	} u;
	struct sy	*left, *right;
} symbol;

/*
 * tree structure
 */
#define	X	0
#define	Y	1
#define	Z	2

#define DIMS	3

#define	LEAF	4
#define	SPLITABLELEAF	(0x08 | LEAF)

#define	SPLITCUTOFF1	40 /* below this value we use a more conservative splitting strategy */
#define	SPLITCUTOFF2	3

#define	splittable(a)	((a)->type & 0x08)

typedef struct ol {
	struct o	*obj;
	struct ol	*nxt;
} olist;

typedef struct kdt {
	unsigned char	type;
	float		splitval;
	bbox		bb;
	union {
		struct {
			int		depth;
			struct ol	*oblist;
		} leaf;
		struct {
			struct kdt	*left, *right;
		} branch;
	} u;
} stree;

/*
 * uniform grid
 */
typedef struct vx {
	bbox		bb;
	struct ol	*oblist;
} voxel;

typedef struct gd {
	bbox	bb;
	int	xdiv, ydiv, zdiv;
	float	deltax, deltay, deltaz;
	voxel	****voxs;
} grid;

/*
 * ray mailbox data
 */
typedef struct rd {
	float	t;
	int	type;
	short	raynumber;
} raydata;

/*
 * general object structure and defs
 */
typedef struct o {
	unsigned char	type,
			incsg;
	bbox		bb;
	surface		*s;
	transdata	*td;
	texture		*txtlist;
	raydata		lastray;
	union {
		float		cne_tipval;
		int		spq_order;
		csg		*csgt;
		eqn		*alg;
		ring		*rng;
		torus		*trs;
		heightfield	*hfield;
		stree		*tree;
		grid		*grd;
		sphere		*sph;
		polygon		*ply;
		geometry	*geo;
		superquadric	*spq;
		composite	*cmp;
		blobby		*blb;
		patch		*pch;
	} obj;
	struct o	*nxt;
} object;

#ifndef M_PI
#define M_PI    3.14159265358979323846
#endif


extern float	eval_fexpr();
extern int	eval_iexpr();

extern sampleMask *getSquareSampleMask();
extern sampleMask *getCircularSampleMask();

extern symbol	*insertsym();
extern symbol	*findsym();


typedef float real;

