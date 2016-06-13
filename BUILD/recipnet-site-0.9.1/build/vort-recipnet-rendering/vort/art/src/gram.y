/*
 * parser for the input language to art
 */

%{
#include <stdio.h>
#include <math.h>
#include "art.h"
#include "macro.h"

extern object	*objectinit(),
		*compinit(),
		*csginit(),
		*getcsgobj(),
		*getcsgexp(),
		*treeinit(),
		*gridinit();

extern symbol	*lookup();

extern light	*lightinit();
extern texture	*textureinit();
extern wlist	*waveinit();
extern cmap	*mapinit();

extern attr	*astackp;
extern mats	*mstackp;

extern object	**oblistsp;
extern object	*subobjs;
extern light	*lights;

extern vector	org, up;

extern matrix	trans;
extern float	focallength;

extern char	*title;
extern int	maxhitlevel, raysperpix, pixelgrid;
extern long	filetype;

extern colour	backcol;

extern float	fogfactor, rfactor;
extern colour	hazecolour;

extern float	huge_dist;

extern float	sourceradius;
extern float	falloff;
extern float	ri;

extern float	screenx, screeny;
extern int	maxtreedepth;
extern int	perspective;

extern int	frameno;
extern int	gridsize[];
extern int	usegrid;

extern int	adaptive;
extern real	aathreshold;

extern float		eval_fexpr();
extern int		eval_iexpr();
extern expression	*get_expr(), *get_varexpr();

#ifndef M_PI
#define M_PI	3.14159265358979323846
#endif

static int	objdefined = FALSE;
static int	fieldtype;

extern objst	*ostackp;

%}

%union{
	object		*y_obj;
	vector		*y_pnt;
	details		*y_det;
	char		*y_str;
	eqn		*y_eqn;
	term		*y_trm;
	expression	*y_exp;
	symbol		*y_sym;
	csgnode		*y_csg;
	float		y_flt;
	int		y_int;
};

%token		CSG
%token		COMPOSITE

%token <y_sym>	OBJECT_TYPE

%token <y_flt>	FLOAT
%token <y_int>	INTEGER FILETYPE OPTION
%token <y_str>	NAME

%type  <y_int>	points
%type  <y_exp>	expr 
%type  <y_trm>	termlist term
%type  <y_csg>	csgexpr
%type  <y_det>	csgbody object definition vlist vbody vitem
%type  <y_det>	body bodyitem texture_ops textitem variation
%type  <y_det>	transform stlist compbody wbody witem mbody mitem

%token LBRACE RBRACE LP RP RADIUS RADII COLOUR CENTER VERTEX COMMA PCENT
%token MATERIAL REFI MINUS AMBIENT INTENSITY LOCATION NAME DOLS
%token EQUATION OFFFILE BASE TOP CONST COEFFS SCALE ROTATE TRANSLATE
%token TITLE REFLECTANCE DOT ON OFF LOOKAT FIELDOFVIEW TRANSPARENCY
%token RAYSPERPIXEL BACKGROUND SIZE MAXHITLEVEL OUTPUT FILETYPE ORDER
%token ABSORPTION VREF1 VREF2 NUMRAYS OBJECT TEXTURE DIRECTION ANGLE UP
%token TWENTYFIVEBIT RANGE MAP BLENDCOLOR SCALEFACTORS VORTFILE HAZECOLOUR
%token FOGFACTOR RFACTOR FALLOFF QUOTE REPEAT SHADOWS COLOURFILE VNORMALFILE
%token SCALEFACTOR SOURCE AMPLITUDE WAVELENGTH PHASE TURBULENCE SQUEEZE
%token DAMPING SOURCERADIUS NORMAL COMPLEXVERTEX SCREENSIZE MAXTREEDEPTH
%token BLEND COLOURMAP MAPVALUES PIXELGRID RI COLOURBLEND NORMALFILE
%token BEAMDISTRIBUTION INSIDEANGLE PROJECTION PERSPECTIVE ORTHOGRAPHIC
%token STRIP VCOLOURFILE TRANSFORM MINDIST CLIPVOLUME HEIGHTFIELD FRAMENO
%token BLOCKSIZE GAPCOLOUR GAPSIZE COLOURFIELD ALPHA GEOMX GEOMY GEOMZ
%token ANTIALIASING ADAPTIVE BRUTE THRESHOLD METABALL BASIS MAXSUBLEVEL
%token SUBDIVISION GRID KDTREE

%left  PLUS MINUS
%left  MULT DIV
%left  POWER
%left  UMINUS

%right  EQUALS

%%

input	: /* NULL */
	| input hitem
	| input object
	  {
		object	*obj, *head;

		if ((head = objectinit($2->u.obj.sym, $2->u.obj.det)) != (object *)NULL) {
			for (obj = head; obj->nxt != (object *)NULL; obj = obj->nxt)	
				;

			obj->nxt = *oblistsp;
			*oblistsp = head;
		}

		objdefined = TRUE;

		free($2);
	  }
	| input statement
	| input definition
	  {
		objdefined = TRUE;

		free($2);
	  }
	;

hitem	: TITLE NAME
	  {
		title = $2;
	  }
	| FIELDOFVIEW expr
	  {
		float	val;

		val = eval_fexpr($2);

		if (val == 0.0 || val == 360.0)
			fatal("art: idiotic angle in field of view.\n");

		focallength = focallength / tan(M_PI / 360.0 * val);
	  }
	| SCREENSIZE expr COMMA expr
	  {
		screenx = eval_fexpr($2) / 2.0;
		screeny = eval_fexpr($4) / 2.0;
	  }
	| MAXTREEDEPTH expr
	  {
		maxtreedepth = eval_iexpr($2);
	  }
	| RAYSPERPIXEL expr
	  {
		raysperpix = eval_iexpr($2);
		pixelgrid = (raysperpix > 1);
	  }
	| HAZECOLOUR expr COMMA expr COMMA expr
	  {
		hazecolour.r = eval_fexpr($2);
		hazecolour.g = eval_fexpr($4);
		hazecolour.b = eval_fexpr($6);
	  }
	| FOGFACTOR expr
	  {
		fogfactor = eval_fexpr($2);
	  }
	| RFACTOR expr
	  {
		rfactor = eval_fexpr($2);
	  }
	| SOURCERADIUS expr
	  {
		sourceradius = eval_fexpr($2);
	  }
	| PIXELGRID ON
	  {
		pixelgrid = TRUE;
	  }
	| TWENTYFIVEBIT ON
	  {
		twentyfivebit(TRUE);
	  }
	| TWENTYFIVEBIT OFF
	  {
		twentyfivebit(FALSE);
	  }
	| ANTIALIASING ADAPTIVE 
	  {
		adaptive = TRUE;
	  }
	| ADAPTIVE THRESHOLD expr
	  {
		aathreshold = eval_fexpr($3);
	  }
	| ANTIALIASING BRUTE
	  {
		adaptive = FALSE;
	  }
	| UP LP expr COMMA expr COMMA expr RP
	  {
		up.x = eval_fexpr($3);
		up.y = eval_fexpr($5);
		up.z = eval_fexpr($7);
	  }
	| LOOKAT LP expr COMMA expr COMMA expr COMMA expr COMMA expr COMMA expr COMMA expr RP
	  {
		vector	t, u, s, eye;
		matrix	m, tmp;
		double	val, vy, vz, sinval, cosval;

		/*
		 * apply the twist
		 */
		val = eval_fexpr($15) * M_PI / 180.0;
		sinval = sin(val);
		cosval = cos(val);
		mident4(m);
		m[0][0] = cosval;
		m[0][1] = -sinval;
		m[1][0] = sinval;
		m[1][1] = cosval;
		mcpy4(tmp, trans);
		mmult4(trans, tmp, m);

		/*
		 * calculate the lookat
		 */
		eye.x = eval_fexpr($3);
		eye.y = eval_fexpr($5);
		eye.z = eval_fexpr($7);

		t.x = eye.x - eval_fexpr($9);
		t.y = eye.y - eval_fexpr($11);
		t.z = eye.z - eval_fexpr($13);

		u.x = up.x;
		u.y = up.y;
		u.z = up.z;

		normalise(t);

		normalise(u);

		vz = dprod(t, u);

		if (fabs(vz) >= 1.0)
			fatal("art: up vector and direction of view are the same.\n");

		vy = sqrt(1.0 - vz * vz);

		u.x = (u.x - vz * t.x) / vy;
		u.y = (u.y - vz * t.y) / vy;
		u.z = (u.z - vz * t.z) / vy;

		xprod(s, u, t);

		mident4(m);

		m[0][0] = s.x;
		m[0][1] = s.y;
		m[0][2] = s.z;

		m[1][0] = u.x;
		m[1][1] = u.y;
		m[1][2] = u.z;

		m[2][0] = t.x;
		m[2][1] = t.y;
		m[2][2] = t.z;

		mcpy4(tmp, trans);
		mmult4(trans, tmp, m);

		mident4(m);
		m[3][0] = eye.x;
		m[3][1] = eye.y;
		m[3][2] = eye.z;
		mcpy4(tmp, trans);
		mmult4(trans, tmp, m);
					/* set up initial trans data */
		mstackp->d.omused = TRUE;
		calctransforms(mstackp);
	  }
	| BACKGROUND expr COMMA expr COMMA expr
	  {
		backcol.r = eval_fexpr($2);
		backcol.g = eval_fexpr($4);
		backcol.b = eval_fexpr($6);
	  }
	| MAXHITLEVEL expr
	  {
		maxhitlevel = eval_iexpr($2);
	  }
	| OUTPUT FILETYPE
	  {
		filetype = $2;
	  }
	| FALLOFF expr
	  {
		falloff = eval_fexpr($2);
	  }
	| PROJECTION PERSPECTIVE
	  {
		perspective = TRUE;
	  }
	| PROJECTION ORTHOGRAPHIC
	  {
		perspective = FALSE;
	  }
	;

object	: OBJECT_TYPE LBRACE body RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = OBJECT;
		$$->u.obj.sym = $1;
		$$->u.obj.det = $3;
		$$->nxt = (details *)NULL;
	  }
	| OBJECT_TYPE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = OBJECT;
		$$->u.obj.sym = $1;
		$$->u.obj.det = (details *)NULL;
		$$->nxt = (details *)NULL;
	  }
	| COMPOSITE LBRACE compbody RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COMP_OBJ;
		$$->u.obj.sym = (symbol *)NULL;
		$$->u.obj.det = $3;
		$$->nxt = (details *)NULL;
	  }
	| CSG LBRACE 
	  {
		if (ostackp->nxt == (objst *)NULL) {
			ostackp->nxt = (objst *)smalloc(sizeof(objst));
			ostackp->nxt->lst = ostackp;
			ostackp->nxt->nxt = (objst *)NULL;
		}

		ostackp = ostackp->nxt;
		ostackp->sym = (symbol *)NULL;
	  }
	  csgbody csgexpr RBRACE
	  {
		details	*d;
		symbol	*s;

		ostackp = ostackp->lst;

		d = (details *)smalloc(sizeof(details));
		d->type = $4->type;
		d->u.csgobj.tree = $5;
		d->u.csgobj.det = $4;
		d->nxt = (details *)NULL;

		s = (symbol *)smalloc(sizeof(symbol));
		s->type = CSG_OBJ;
		s->u.det = d;

		$$ = (details *)smalloc(sizeof(details));
		$$->type = OBJECT;
		$$->u.obj.sym = s;
		$$->u.obj.det = (details *)NULL;
		$$->nxt = (details *)NULL;
	  }
	;

compbody: /* NULL */
	  {
		$$ = (details *)NULL;
	  }
	| compbody transform
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	| compbody bodyitem
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	| compbody object
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	;

statement: NAME EQUALS expr
	  {
		defvar($1, $3);
	  }
	| transform
	  {
		switch ($1->type) {
		case TRANSLATE:
			translate($1->u.v.x, $1->u.v.y, $1->u.v.z);
			break;
		case SCALE:
			scale($1->u.v.x, $1->u.v.y, $1->u.v.z);
			break;
		case ROTATE:
			rotate($1->u.rot.ang, $1->u.rot.axis);
			break;
		case TRANSFORM:
			transform(*$1->u.trans.m);
			free($1->u.trans.m);
			break;
		default:
			fatal("art: bad transform type in switch.\n");
		}

		free($1);
	  }
	| bodyitem
	  {
		surface	*s;

			/*
			 * don't want to change someone else's
			 * material properties
			 */
		if (objdefined) {
			s = (surface *)smalloc(sizeof(surface));
			*s = *astackp->d.s;
			astackp->d.s = s;
			objdefined = FALSE;
		} else
			s = astackp->d.s;

		switch ($1->type) {
		case COLOUR:
			s->c.r = $1->u.c.r;
			s->c.g = $1->u.c.g;
			s->c.b = $1->u.c.b;
			break;
		case AMBIENT:
			s->a.r = $1->u.c.r;
			s->a.g = $1->u.c.g;
			s->a.b = $1->u.c.b;
			break;
		case TEXTURE:
			$1->u.txt->nxt = astackp->d.txtlist;
			astackp->d.txtlist = $1->u.txt;
			break;
		case MATERIAL:
			s->ri = $1->u.mat.ri;
			s->kd = $1->u.mat.kd;
			s->ks = $1->u.mat.ks;
			s->ksexp = $1->u.mat.ksexp;
			if (s->ri == 0.0)
				s->ri = 1.0;
			break;
		case REFLECTANCE:
			s->refl = $1->u.c;
			break;
		case TRANSPARENCY:
			s->trans = $1->u.c;
			break;
		case ABSORPTION:
			s->falloff = $1->u.f;
			break;
		case ON:
			astackp->d.options |= $1->u.i;
			break;
		case OFF:
			astackp->d.options &= ~$1->u.i;
			break;
		case SHADOWS:
			astackp->d.s->shadows = $1->u.i;
			break;
		default:
			fatal("art: bad statement type in switch.\n");
		}

		free($1);
	  }
	| SUBDIVISION GRID
	  { 
		oblistsp++;
		*oblistsp = (object *)NULL;
	  }
	  LBRACE input RBRACE
	  {
		object	*divobjs, *otherobjs, *nexto, *o, *obj;

		divobjs = otherobjs = (object *)NULL;

		for (o = *oblistsp; o != (object *)NULL; o = nexto) {
			nexto = o->nxt;
			o->lastray.raynumber = 0;
						/* algebraic surface or contains one */
			if (o->type == ALGEBRAIC || o->bb.max[X] == huge_dist) {
				o->nxt = otherobjs;
				otherobjs = o;
			} else if (o->type == NULL_OBJ)
				free(o);
			else {
				o->nxt = divobjs;
				divobjs = o;
			}
		}

		oblistsp--;

		obj = gridinit(divobjs);
		obj->nxt = otherobjs;

		/*
		 * add our new composite to the object stack
		 */
		if (*oblistsp != (object *)NULL) {
			for (o = *oblistsp; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = obj;
		} else {
			*oblistsp = obj;
		}

		/*
		 * add the interior objects to the general object list.
		 */
		if (subobjs != (object *)NULL) {
			for (o = subobjs; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = divobjs;
		} else {
			subobjs = divobjs;
		}
	  }
	| SUBDIVISION GRID expr COMMA expr COMMA expr
	  {
		oblistsp++;
		*oblistsp = (object *)NULL;
	  }
	  LBRACE input RBRACE
	  {
		object	*divobjs, *otherobjs, *nexto, *o, *obj;

		divobjs = otherobjs = (object *)NULL;

		for (o = *oblistsp; o != (object *)NULL; o = nexto) {
			nexto = o->nxt;
			o->lastray.raynumber = 0;
						/* algebraic surface or contains one */
			if (o->type == ALGEBRAIC || o->bb.max[X] == huge_dist) {
				o->nxt = otherobjs;
				otherobjs = o;
			} else if (o->type == NULL_OBJ)
				free(o);
			else {
				o->nxt = divobjs;
				divobjs = o;
			}
		}

		oblistsp--;

		obj = gridinit(divobjs);
		obj->nxt = otherobjs;

		/*
		 * add our new composite to the object stack
		 */
		if (*oblistsp != (object *)NULL) {
			for (o = *oblistsp; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = obj;
		} else {
			*oblistsp = obj;
		}

		/*
		 * add the interior objects to the general object list.
		 */
		if (subobjs != (object *)NULL) {
			for (o = subobjs; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = divobjs;
		} else {
			subobjs = divobjs;
		}
	  }
	| SUBDIVISION KDTREE 
	  {
		oblistsp++;
		*oblistsp = (object *)NULL;
	  }
	  LBRACE input RBRACE
	  {
		object	*divobjs, *otherobjs, *nexto, *o, *obj;

		divobjs = otherobjs = (object *)NULL;

		for (o = *oblistsp; o != (object *)NULL; o = nexto) {
			nexto = o->nxt;
			o->lastray.raynumber = 0;
						/* algebraic surface or contains one */
			if (o->type == ALGEBRAIC || o->bb.max[X] == huge_dist) {
				o->nxt = otherobjs;
				otherobjs = o;
			} else if (o->type == NULL_OBJ)
				free(o);
			else {
				o->nxt = divobjs;
				divobjs = o;
			}
		}

		oblistsp--;

		obj = treeinit(divobjs);
		obj->nxt = otherobjs;

		/*
		 * add our new composite to the object stack
		 */
		if (*oblistsp != (object *)NULL) {
			for (o = *oblistsp; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = obj;
		} else {
			*oblistsp = obj;
		}

		/*
		 * add the interior objects to the general object list.
		 */
		if (subobjs != (object *)NULL) {
			for (o = subobjs; o->nxt != (object *)NULL; o = o->nxt)	
				;
			o->nxt = divobjs;
		} else {
			subobjs = divobjs;
		}
	  }
	| REPEAT expr LBRACE stlist RBRACE
	  {
		dorepeat($2, $4);
	  }
	;

stlist	: /* NULL */
	  {
		$$ = (details *)NULL;
	  }
	| stlist transform
	  {
		$2->nxt = $1;
		$$ = $2;
	  }
	| stlist OBJECT_TYPE
	  {
		$$ = (details *)smalloc(sizeof(details));

		$$->type = OBJECT;
		$$->u.obj.sym = $2;
		$$->u.obj.det = (details *)NULL;
		$$->nxt = $1;
	  }
	| stlist REPEAT expr LBRACE stlist RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));

		$$->type = REPEAT;
		$$->u.rpt.expr = $3;
		$$->u.rpt.stmt = $5;
		$$->nxt = $1;
	  }
	;
		

body	: /* NULL */
	  {
		$$ = (details *)NULL;
	  }
	| body transform
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	| body bodyitem
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	;

bodyitem: CENTER LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = CENTER;
		$$->u.v.x = eval_fexpr($3);
		$$->u.v.y = eval_fexpr($5);
		$$->u.v.z = eval_fexpr($7);
		$$->nxt = (details *)NULL;
	  }
	| THRESHOLD expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = THRESHOLD;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| METABALL LP expr COMMA expr COMMA expr RP COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = METABALL;
		$$->u.mb = (mball *)smalloc(sizeof(mball));
		$$->u.mb->v.x = eval_fexpr($3);
		$$->u.mb->v.y = eval_fexpr($5);
		$$->u.mb->v.z = eval_fexpr($7);
		$$->u.mb->r = eval_fexpr($10);
		$$->u.mb->s = eval_fexpr($12);
		$$->nxt = (details *)NULL;
	  }
	| ORDER expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = ORDER;
		$$->u.i = eval_iexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| RADIUS expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = RADIUS;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| RADII expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = RADII;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->nxt = (details *)NULL;
	  }
	| RADII expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = RADII;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| VERTEX vbody
	  {
		if ($2->nxt == (details *)NULL) {
			$2->type = VERTEX;
			$$ = $2;
		} else {
			$$ = (details *)smalloc(sizeof(details));
			$$->type = COMPLEXVERTEX;
			$$->u.det = $2;
			$$->nxt = (details *)NULL;
		}
	  }
	| EQUATION DOLS termlist EQUALS expr DOLS
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = EQUATION;
		$$->u.t = (term *)smalloc(sizeof(term));
		$$->u.t->coef = -eval_fexpr($5);
		$$->u.t->xp = 0;
		$$->u.t->yp = 0;
		$$->u.t->zp = 0;
		$$->u.t->nxt = $3;
		$$->nxt = (details *)NULL;
	  }
	| CLIPVOLUME OBJECT_TYPE LBRACE body RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = OBJECT;
		$$->u.obj.sym = $2;
		$$->u.obj.det = $4;
		$$->nxt = (details *)NULL;
	  }
	| COEFFS expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COEFFS;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| CONST expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = CONST;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| TOP expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TOP;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| BASE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BASE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| HEIGHTFIELD NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = HEIGHTFIELD;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| COLOURFIELD NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COLOURFIELD;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| OFFFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = OFFFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| STRIP LBRACE vlist RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = STRIP;
		$$->u.det = $3;
		$$->nxt = (details *)NULL;
	  }
	| VCOLOURFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = VCOLOURFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| COLOURFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COLOURFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| VNORMALFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = VNORMALFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| NORMALFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = NORMALFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| MATERIAL expr COMMA expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = MATERIAL;
		$$->u.mat.ri = eval_fexpr($2);
		$$->u.mat.kd = eval_fexpr($4);
		$$->u.mat.ks = eval_fexpr($6);
		$$->u.mat.ksexp = eval_iexpr($8);
		$$->nxt = (details *)NULL;
	  }
	| SHADOWS ON
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SHADOWS;
		$$->u.i = TRUE;
		$$->nxt = (details *)NULL;

		astackp->d.shadows = TRUE;
	  }
	| SHADOWS OFF
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SHADOWS;
		$$->u.i = FALSE;
		$$->nxt = (details *)NULL;

		astackp->d.shadows = FALSE;
	  }
	| COLOUR  { fieldtype = COLOUR; } variation
	  {
		$$ = $3;
	  }
	| AMBIENT { fieldtype = AMBIENT; } variation
	  {
		$$ = $3;
	  }
	| REFLECTANCE  { fieldtype = REFLECTANCE; } variation
	  {
		$$ = $3;
	  }
	| TRANSPARENCY { fieldtype = TRANSPARENCY; } variation
	  {
		$$ = $3;
	  }
	| ALPHA { fieldtype = ALPHA; } variation
	  {
		$$ = $3;
	  }
	| RI { fieldtype = RI; } variation
	  {
		$$ = $3;
	  }
	| ABSORPTION { fieldtype = ABSORPTION; } variation
	  {
		$$ = $3;
	  }
	| OPTION ON
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = ON;
		$$->u.i = $1;
		$$->nxt = (details *)NULL;
	  }
	| OPTION OFF
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = OFF;
		$$->u.i = $1;
		$$->nxt = (details *)NULL;
	  }
	| TEXTURE NAME LBRACE texture_ops RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TEXTURE;
		$$->u.txt = textureinit($2, $4, COLOUR);
		$$->nxt = (details *)NULL;
	  }
	| TEXTURE NAME textitem 
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TEXTURE;
		$$->u.txt = textureinit($2, $3, COLOUR);
		$$->nxt = (details *)NULL;
	  }
	| NAME NAME SIZE expr COMMA expr	/* backward compatability... */
	  {
		details	*d1, *d2;

		d1 = (details *)smalloc(sizeof(details));
		d1->type = VORTFILE;
		d1->u.s = $2;

		d2 = (details *)smalloc(sizeof(details));
		d2->type = SIZE;
		d2->u.v.x = eval_fexpr($4);
		d2->u.v.y = eval_fexpr($6);

		d1->nxt = d2;
		d2->nxt = (details *)NULL;

		$$ = (details *)smalloc(sizeof(details));
		$$->type = TEXTURE;
		$$->u.txt = textureinit("tile", d1, COLOUR);
		$$->nxt = (details *)NULL;
	  }
	| LOCATION LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = CENTER;
		$$->u.v.x = eval_fexpr($3);
		$$->u.v.y = eval_fexpr($5);
		$$->u.v.z = eval_fexpr($7);
		$$->nxt = (details *)NULL;
	  }
	| NUMRAYS expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = NUMRAYS;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| INSIDEANGLE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = INSIDEANGLE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| BEAMDISTRIBUTION expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BEAMDISTRIBUTION;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| MINDIST expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = MINDIST;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| ANGLE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = ANGLE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| DIRECTION LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = DIRECTION;
		$$->u.v.x = eval_fexpr($3);
		$$->u.v.y = eval_fexpr($5);
		$$->u.v.z = eval_fexpr($7);
		$$->nxt = (details *)NULL;
	  }
	| BASIS NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BASIS;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| MAXSUBLEVEL expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = MAXSUBLEVEL;
		$$->u.i = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| points LBRACE expr COMMA expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr COMMA expr RBRACE
	  {
		matrix	m;

		$$ = (details *)smalloc(sizeof(details));
		$$->type = $1;

		mident4(m);

		m[0][0] = eval_fexpr($3);
		m[0][1] = eval_fexpr($5);
		m[0][2] = eval_fexpr($7);
		m[0][3] = eval_fexpr($9);

		m[1][0] = eval_fexpr($11);
		m[1][1] = eval_fexpr($13);
		m[1][2] = eval_fexpr($15);
		m[1][3] = eval_fexpr($17);

		m[2][0] = eval_fexpr($19);
		m[2][1] = eval_fexpr($21);
		m[2][2] = eval_fexpr($23);
		m[2][3] = eval_fexpr($25);

		m[3][0] = eval_fexpr($27);
		m[3][1] = eval_fexpr($29);
		m[3][2] = eval_fexpr($31);
		m[3][3] = eval_fexpr($33);

		$$->u.trans.m = (matrix *)smalloc(sizeof(matrix));

		mcpy4(*$$->u.trans.m, m);

		$$->nxt = (details *)NULL;
	  }
	;

points	: GEOMX
	  {
		$$ = GEOMX;
	  }
	| GEOMY
	  {
		$$ = GEOMY;
	  }
	| GEOMZ
	  {
		$$ = GEOMZ;
	  }
	;

variation: expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = fieldtype;
		$$->u.c.r = eval_fexpr($1);
		$$->u.c.g = eval_fexpr($3);
		$$->u.c.b = eval_fexpr($5);
		$$->nxt = (details *)NULL;
	  }
	| expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = fieldtype;
		$$->u.c.r = $$->u.c.g = $$->u.c.b = eval_fexpr($1);
		$$->nxt = (details *)NULL;
	  }
	| TEXTURE NAME LBRACE texture_ops RBRACE
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TEXTURE;
		$$->u.txt = textureinit($2, $4, fieldtype);
		$$->nxt = (details *)NULL;
	  }
	| TEXTURE NAME textitem
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TEXTURE;
		$$->u.txt = textureinit($2, $3, fieldtype);
		$$->nxt = (details *)NULL;
	  }
	;

vlist	: vbody
	  {
		$$ = (details *)smalloc(sizeof(details));
		if ($1->nxt == (details *)NULL)
			$$->type = VERTEX;
		else
			$$->type = COMPLEXVERTEX;
		$$->u.det = $1;
		$$->nxt = (details *)NULL;
	  }
	| vlist vbody
	  {
		$$ = (details *)smalloc(sizeof(details));
		if ($2->nxt == (details *)NULL)
			$$->type = VERTEX;
		else
			$$->type = COMPLEXVERTEX;
		$$->u.det = $2;
		$$->nxt = $1;
	  }
	;

vbody	: vitem
	  {
		$$ = $1;
	  }
	| vbody COMMA vitem
	  {
		if ($3->nxt != (details *)NULL)
			$3->nxt->nxt = $1;
		else
			$3->nxt = $1;
		$$ = $3;
	  }
	;

vitem	: LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = VERTEX;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COLOUR;
		$$->u.v.x = eval_fexpr($1);
		$$->u.v.y = eval_fexpr($3);
		$$->u.v.z = eval_fexpr($5);
		$$->nxt = (details *)NULL;
	  }
	| expr COMMA expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = COLOUR;
		$$->u.v.x = eval_fexpr($1);
		$$->u.v.y = eval_fexpr($3);
		$$->u.v.z = eval_fexpr($5);
		$$->nxt = (details *)smalloc(sizeof(details));
		$$->nxt->type = TRANSPARENCY;
		$$->nxt->u.c.r = $$->nxt->u.c.g = $$->nxt->u.c.b = eval_fexpr($7);
		$$->nxt->nxt = (details *)NULL;
	  }
	;

transform: ROTATE LP expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = ROTATE;
		$$->u.rot.ang = eval_fexpr($3);
		$$->u.rot.axis = eval_iexpr($5);
		$$->nxt = (details *)NULL;
	  }
	| TRANSLATE LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TRANSLATE;
		$$->u.v.x = eval_fexpr($3);
		$$->u.v.y = eval_fexpr($5);
		$$->u.v.z = eval_fexpr($7);
		$$->nxt = (details *)NULL;
	  }
	| SCALE LP expr COMMA expr COMMA expr RP
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SCALE;
		$$->u.v.x = eval_fexpr($3);
		$$->u.v.y = eval_fexpr($5);
		$$->u.v.z = eval_fexpr($7);
		$$->nxt = (details *)NULL;
	  }
	| TRANSFORM LBRACE expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr COMMA
		expr COMMA expr COMMA expr RBRACE
	  {
		matrix	m;

		$$ = (details *)smalloc(sizeof(details));
		$$->type = TRANSFORM;

		mident4(m);

		m[0][0] = eval_fexpr($3);
		m[0][1] = eval_fexpr($5);
		m[0][2] = eval_fexpr($7);

		m[1][0] = eval_fexpr($9);
		m[1][1] = eval_fexpr($11);
		m[1][2] = eval_fexpr($13);

		m[2][0] = eval_fexpr($15);
		m[2][1] = eval_fexpr($17);
		m[2][2] = eval_fexpr($19);

		m[3][0] = eval_fexpr($21);
		m[3][1] = eval_fexpr($23);
		m[3][2] = eval_fexpr($25);

		$$->u.trans.m = (matrix *)smalloc(sizeof(matrix));

		minv4(*$$->u.trans.m, m);

		$$->nxt = (details *)NULL;
	  }
	;

texture_ops:	/* NULL */
	  {
		$$ = (details *)NULL;
	  }
	| texture_ops textitem
	  {
		$2->nxt = $1;
		$$ = $2;
	  }
	| texture_ops transform
	  {
		$2->nxt = $1;
		$$ = $2;
	  }
	;

textitem: MAP NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = MAP;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| COLOURMAP LBRACE mbody RBRACE
	 {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = MAPVALUES;
                $$->u.cm = mapinit($3);
                $$->nxt = (details *)NULL;
          }
	| BLEND expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BLEND;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| RANGE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = RANGE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| BLENDCOLOR expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BLENDCOLOR;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| SIZE expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SIZE;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->nxt = (details *)NULL;
	  }
	| SCALEFACTORS expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SCALEFACTORS;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| SCALEFACTOR expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SCALEFACTOR;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| VORTFILE NAME
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = VORTFILE;
		$$->u.s = $2;
		$$->nxt = (details *)NULL;
	  }
	| TURBULENCE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = TURBULENCE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| SQUEEZE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = SQUEEZE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| SOURCE LBRACE wbody RBRACE
	  {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = SOURCE;
                $$->u.w = waveinit($3);
                $$->nxt = (details *)NULL;
	  }
	| GAPSIZE expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = GAPSIZE;
		$$->u.f = eval_fexpr($2);
		$$->nxt = (details *)NULL;
	  }
	| GAPCOLOUR expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = GAPCOLOUR;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	| BLOCKSIZE expr COMMA expr COMMA expr
	  {
		$$ = (details *)smalloc(sizeof(details));
		$$->type = BLOCKSIZE;
		$$->u.v.x = eval_fexpr($2);
		$$->u.v.y = eval_fexpr($4);
		$$->u.v.z = eval_fexpr($6);
		$$->nxt = (details *)NULL;
	  }
	;

mbody	: mitem
	{
		$$ = $1;
	}
	| mbody COMMA mitem
	{
		$3->nxt = $1;
		$$ = $3;
	}
	;

mitem   : expr COMMA expr COMMA expr
          {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = MAPVALUES;
                $$->u.v.x = eval_fexpr($1);
                $$->u.v.y = eval_fexpr($3);
                $$->u.v.z = eval_fexpr($5);
                $$->nxt = (details *)NULL;
	  }
	;
wbody   : /* NULL */
          {
                $$ = (details *)NULL;
          }
        | wbody witem
          {
                $2->nxt = $1;
                $$ = $2;
          }
        ;

witem   : CENTER LP expr COMMA expr COMMA expr RP
          {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = CENTER;
                $$->u.v.x = eval_fexpr($3);
                $$->u.v.y = eval_fexpr($5);
                $$->u.v.z = eval_fexpr($7);
                $$->nxt = (details *)NULL;
	  }
	| WAVELENGTH expr
	  {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = WAVELENGTH;
                $$->u.f = eval_fexpr($2);
                $$->nxt = (details *)NULL;
	  }
	| AMPLITUDE expr
	  {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = AMPLITUDE;
                $$->u.f = eval_fexpr($2);
                $$->nxt = (details *)NULL;
	  }
	| PHASE expr
	  {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = PHASE;
                $$->u.f = eval_fexpr($2);
                $$->nxt = (details *)NULL;
	  }
	| DAMPING expr
	  {
                $$ = (details *)smalloc(sizeof(details));
                $$->type = DAMPING;
                $$->u.f = eval_fexpr($2);
                $$->nxt = (details *)NULL;
	  }
	;


csgbody	: /* NULL */
	  {
		$$ = (details *)NULL;
	  }
	| csgbody bodyitem
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	| csgbody transform
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	| csgbody definition
	  {
		details	*d;

		if ($1 != (details *)NULL) {
			for (d = $1; d->nxt != (details *)NULL; d = d->nxt)
				;
			d->nxt = $2;
			$$ = $1;
		} else
			$$ = $2;
	  }
	;

definition: OBJECT_TYPE NAME LBRACE body RBRACE
	  {
		defobj($2, $1->type, $4);

		$$ = (details *)smalloc(sizeof(details));

		$$->type = OBJECT;
		$$->u.obj.sym = lookup($2);
		$$->u.obj.det = (details *)NULL;
		$$->nxt = (details *)NULL;
	  }
	| CSG NAME LBRACE
	  {
		if (ostackp->nxt == (objst *)NULL) {
			ostackp->nxt = (objst *)smalloc(sizeof(objst));
			ostackp->nxt->lst = ostackp;
			ostackp->nxt->nxt = (objst *)NULL;
		}

		ostackp = ostackp->nxt;
		ostackp->sym = (symbol *)NULL;
	  }
	  csgbody csgexpr RBRACE
	  {
		details	*d;

		ostackp = ostackp->lst;

		d = (details *)smalloc(sizeof(details));

		d->type = $6->type;
		d->u.csgobj.tree = $6;
		d->u.csgobj.det = $5;
		d->nxt = (details *)NULL;

		defobj($2, CSG_OBJ, d);

		$$ = (details *)smalloc(sizeof(details));

		$$->type = OBJECT;
		$$->u.obj.sym = lookup($2);
		$$->u.obj.det = (details *)NULL;
		$$->nxt = (details *)NULL;
	  }
	| COMPOSITE NAME LBRACE compbody RBRACE
	  {
		defobj($2, COMP_OBJ, $4);

		$$ = (details *)smalloc(sizeof(details));

		$$->type = OBJECT;
		$$->u.obj.sym = lookup($2);
		$$->u.obj.det = (details *)NULL;
		$$->nxt = (details *)NULL;
	  }
	;


csgexpr	: OBJECT_TYPE
	  {
		$$ = (csgnode *)smalloc(sizeof(csgnode));
		$$->type = OBJECT;
		$$->u.sym = $1;
	  }
	| csgexpr PLUS csgexpr
	  {
		$$ = (csgnode *)smalloc(sizeof(csgnode));
		$$->type = CSG_ADD;
		$$->u.branch.left = $1;
		$$->u.branch.right = $3;
	  }
	| csgexpr MULT csgexpr
	  {
		$$ = (csgnode *)smalloc(sizeof(csgnode));
		$$->type = CSG_INT;
		$$->u.branch.left = $1;
		$$->u.branch.right = $3;
	  }
	| csgexpr MINUS csgexpr
	  {
		$$ = (csgnode *)smalloc(sizeof(csgnode));
		$$->type = CSG_SUB;
		$$->u.branch.left = $1;
		$$->u.branch.right = $3;
	  }
	| LP csgexpr RP
	  {
		$$ = $2;
	  }
	| NAME
	  {
		char	buf[BUFSIZ];

		sprintf(buf, "art: object %s not defined.\n", $1);
		fatal(buf);
	  }
	;

termlist: term
	  {
		$$ = $1;
	  }
	| termlist termlist %prec MULT
	  {
		term	*t, *p, *np, *prod;

		prod = (term *)NULL;
		for (p = $1; p != (term *)NULL; p = p->nxt) {
			for (np = $2; np != (term *)NULL; np = np->nxt) {
				t = (term *)smalloc(sizeof(term));
				*t = *np;
				t->coef *= p->coef;
				t->xp += p->xp;
				t->yp += p->yp;
				t->zp += p->zp;
				t->nxt = prod;
				prod = t;
			}
		}

		for (t = $1; t != (term *)NULL; t = np) {
			np = t->nxt;
			free(t);
		}

		for (t = $2; t != (term *)NULL; t = np) {
			np = t->nxt;
			free(t);
		}

		$$ = prod;
	  }
	| LP termlist RP 
	  {
		$$ = $2;
	  }
	| LP termlist RP POWER LBRACE expr RBRACE
	  {
		term	*t, *p, *np, *prod, *nprod;
		int	i, power;

		prod = $2;
		power = eval_iexpr($6);

		for (i = 1; i != power; i++) {		
			nprod = (term *)NULL;
			for (p = $2; p != (term *)NULL; p = p->nxt) {
				for (np = prod; np != (term *)NULL; np = np->nxt) {
					t = (term *)smalloc(sizeof(term));
					*t = *np;
					t->coef *= p->coef;
					t->xp += p->xp;
					t->yp += p->yp;
					t->zp += p->zp;
					t->nxt = nprod;
					nprod = t;
				}
			}
			if (prod != $2)
				for (t = prod; t != (term *)NULL; t = np) {
					np = t->nxt;
					free(t);
				}
			prod = nprod;
		}

		for (t = $2; t != (term *)NULL; t = np) {
			np = t->nxt;
			free(t);
		}

		$$ = prod;
	  }
	| termlist PLUS termlist
	  {
		term	*p;

		for (p = $3; p->nxt != (term *)NULL; p = p->nxt)
			;
		$$ = $3;
		p->nxt = $1;
	  }
	| termlist MINUS termlist
	  {
		term	*p, *lp;

		for (p = $3; p != (term *)NULL; p = p->nxt) {
			p->coef *= -1.0;
			lp = p;
		}

		$$ = $3;
		lp->nxt = $1;
	  }
	;

term	: NAME
	  {
		char	*p;

		$$ = (term *)smalloc(sizeof(term));
		$$->coef = 1;
		$$->xp = 0;
		$$->yp = 0;
		$$->zp = 0;
		$$->nxt = (term *)NULL;

		for (p = $1; *p != 0; p++)
			switch (*p) {
			case 'x':
				$$->xp += 1;
				break;
			case 'y':
				$$->yp += 1;
				break;
			case 'z':
				$$->zp += 1;
				break;
			default:
				fatal("art: illegal name in equation.\n");
			}
	  }
	| FLOAT
	  {
		$$ = (term *)smalloc(sizeof(term));
		$$->coef = $1;
		$$->xp = 0;
		$$->yp = 0;
		$$->zp = 0;
		$$->nxt = (term *)NULL;
	  }
	| INTEGER
	  {
		$$ = (term *)smalloc(sizeof(term));
		$$->coef = $1;
		$$->xp = 0;
		$$->yp = 0;
		$$->zp = 0;
		$$->nxt = (term *)NULL;
	  }
	| NAME POWER LBRACE expr RBRACE
	  {
		char	*p;
		int	power;

		$$ = (term *)smalloc(sizeof(term));
		$$->coef = 1;
		$$->xp = 0;
		$$->yp = 0;
		$$->zp = 0;
		$$->nxt = (term *)NULL;

		power = eval_iexpr($4);

		for (p = $1; *p != 0; p++)
			switch (*p) {
			case 'x':
				$$->xp += 1;
				break;
			case 'y':
				$$->yp += 1;
				break;
			case 'z':
				$$->zp += 1;
				break;
			default:
				fatal("art: illegal name in equation.\n");
			}

		p--;

		switch (*p) {
		case 'x':
			$$->xp += power - 1;
			break;
		case 'y':
			$$->yp += power - 1;
			break;
		case 'z':
			$$->zp += power - 1;
			break;
		default:
			fatal("art: illegal name in equation.\n");
		}
	  }
	;

expr	: FLOAT
	  {
		$$ = (expression *)smalloc(sizeof(expression));
		$$->type = EXP_FLOAT;
		$$->u.f = $1;
	  }
	| INTEGER
	  {
		$$ = (expression *)smalloc(sizeof(expression));
		$$->type = EXP_INT;
		$$->u.i = $1;
	  }
	| FRAMENO
	  {
		$$ = (expression *)smalloc(sizeof(expression));
		$$->type = EXP_INT;
		$$->u.i = frameno;
	  }
	| QUOTE NAME QUOTE
	  {
		$$ = (expression *)smalloc(sizeof(expression));
		$$->type = EXP_INT;
		$$->u.i = *$2;
		free($2);
	  }
	| NAME
	  {
		$$ = get_varexpr($1);
	  }
	| expr PLUS expr
	  {
		$$ = get_expr(EXP_ADD, $1, $3);
	  }
	| expr MINUS expr
	  {
		$$ = get_expr(EXP_SUB, $1, $3);
	  }
	| expr MULT expr
	  {
		$$ = get_expr(EXP_MUL, $1, $3);
	  }
	| expr DIV expr
	  {
		$$ = get_expr(EXP_DIV, $1, $3);
	  }
	| MINUS expr %prec UMINUS
	  {
		$$ = get_expr(EXP_UMINUS, $2, (expression *)NULL);
	  }
	;
