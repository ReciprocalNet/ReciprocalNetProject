#include <stdio.h>
#include "art.h"
#include "gram.h"
#include "objs.h"
#include "macro.h"

extern attr	*astackp;
extern mats	*mstackp;

extern matrix	trans;

extern float	eval_fexpr();
extern object	*compinit(), *csginit();

extern objst	*ostackp;

extern void	sphereinit(),
		ellipsinit(),
		boxinit(),
		polygoninit(),
		torusinit(),
		ringinit(),
		alginit(),
		superinit(),
		coneinit(),
		blobinit(),
		geominit();

/*
 * defobj
 *
 *	define an object
 */
defobj(s, type, d)
	char	*s;
	int	type;
	details	*d;
{
	symbol	*sym;

	sym = insertsym(&ostackp->sym, s);

	sym->type = type;
	sym->u.det = d;
}

/*
 * lookup
 *
 *	check to see if string represents a predefined object type
 */
symbol *
lookup(string)
	char	*string;
{
	symbol	*sym;
	objst	*symptr;

	symptr = ostackp;

	while ((sym = findsym(symptr->sym, string)) == (symbol *)NULL)
		if ((symptr = symptr->lst) == (objst *)NULL)
			return((symbol *)NULL);

	return(sym);
}

/*
 * objectinit
 *
 *	return an initialised object 
 */
object *
objectinit(sym, d)
	symbol	*sym;
	details	*d;
{
	int	type;
	object	*o;
	details	*nd1, *nd2, *nd3;
	details	*nd, *dl, *nxtd, *otherdt, *argdt, *nxtdl;
	surface	s;
	int	sset;
	char	buf[BUFSIZ];

					/* might have a composite */
	if (sym == (symbol *)NULL || sym->type == COMP_OBJ)
		return(compinit(sym, d));

	if (sym->type == CSG_OBJ)
		return(csginit(sym, d));

	if (astackp->nxt == (attr *)NULL) {
		astackp->nxt = (attr *)smalloc(sizeof(attr));
		astackp->nxt->lst = astackp;
		astackp->nxt->nxt = (attr *)NULL;
	}

	astackp = astackp->nxt;
	astackp->d = astackp->lst->d;

	if (mstackp->nxt == (mats *)NULL) {
		mstackp->nxt = (mats *)smalloc(sizeof(mats));
		mstackp->nxt->lst = mstackp;
		mstackp->nxt->nxt = (mats *)NULL;
	}

	mstackp = mstackp->nxt;
	mstackp->d = mstackp->lst->d;
	mident4(mstackp->d.vm);

	astackp->d.txtlist = (texture *)NULL;

	s = *astackp->d.s;

	o = (object *)smalloc(sizeof(object));

	o->type = type = sym->type;
	o->incsg = FALSE;

	o->nxt = (object *)NULL;

	nxtd = otherdt = (details *)NULL;

	if (sym->u.det != (details *)NULL) {
		for (dl = sym->u.det; dl != (details *)NULL; dl = dl->nxt) {
			nd = (details *)smalloc(sizeof(details));
			copydetails(nd, dl);
			if (otherdt == (details *)NULL)
				nxtd = otherdt = nd;
			else {
				nxtd->nxt = nd;
				nxtd = nd;
			}
		}
	}

	if (d == (details *)NULL) {
		argdt = otherdt;
		otherdt = (details *)NULL;
	} else
		argdt = d;

	nd = (details *)NULL;

	sset = FALSE;

	for (dl = argdt; dl != (details *)NULL; dl = nxtd) {

		if ((nxtd = dl->nxt) == (details *)NULL) {
			nxtd = otherdt;
			otherdt = (details *)NULL;
		}

		switch (dl->type) {
		case COLOUR:
			s.c.r = dl->u.c.r;
			s.c.g = dl->u.c.g;
			s.c.b = dl->u.c.b;
			sset = TRUE;
			free(dl);
			break;
		case AMBIENT:
			s.a.r = dl->u.c.r;
			s.a.g = dl->u.c.g;
			s.a.b = dl->u.c.b;
			sset = TRUE;
			free(dl);
			break;
		case TEXTURE:
			dl->u.txt->nxt = astackp->d.txtlist;
			astackp->d.txtlist = dl->u.txt;
			free(dl);
			break;
		case MATERIAL:
			s.ri = dl->u.mat.ri;
			s.kd = dl->u.mat.kd;
			s.ks = dl->u.mat.ks;
			s.ksexp = dl->u.mat.ksexp;
			sset = TRUE;
			free(dl);
			break;
		case REFLECTANCE:
			s.refl = dl->u.c;
			sset = TRUE;
			free(dl);
			break;
		case TRANSPARENCY:
			s.trans = dl->u.c;
			sset = TRUE;
			free(dl);
			break;
		case ABSORPTION:
			s.falloff = dl->u.c.r;
			sset = TRUE;
			free(dl);
			break;
		case ALPHA:
			s.alpha = dl->u.c.r;
			sset = TRUE;
			free(dl);
			break;
		case SHADOWS:
			s.shadows = dl->u.i;
			sset = TRUE;
			free(dl);
			break;
		case TRANSLATE:
			translate(dl->u.v.x, dl->u.v.y, dl->u.v.z);
			free(dl);
			break;
		case SCALE:
			scale(dl->u.v.x, dl->u.v.y, dl->u.v.z);
			free(dl);
			break;
		case ROTATE:
			rotate(dl->u.rot.ang, dl->u.rot.axis);
			free(dl);
			break;
		case TRANSFORM:
			transform(*dl->u.trans.m);
			free(dl->u.trans.m);
			free(dl);
			break;
		case ON:
			astackp->d.options |= dl->u.i;
			free(dl);
			break;
		case OFF:
			astackp->d.options &= ~dl->u.i;
			free(dl);
			break;
		default:
			dl->nxt = nd;
			nd = dl;
		}
	}

	if (sset) {
		astackp->d.s = (surface *)smalloc(sizeof(surface));
		*astackp->d.s = s;
	}

	if ((astackp->d.s->trans.r != 0.0 || astackp->d.s->trans.g != 0.0 ||
	   astackp->d.s->trans.b != 0.0) && astackp->d.s->ri == 0.0)
		astackp->d.s->ri = 1.0;

	switch (type) {
	case SPHERE:
		if (mstackp->d.nscales.x == mstackp->d.nscales.y
		   && mstackp->d.nscales.y == mstackp->d.nscales.z)
			sphereinit(o, nd);
		else {
			o->type = ELLIPSOID;
			ellipsinit(o, nd);
		}
		break;
	case BOX:
		boxinit(o, nd);
		break;
	case RING:
		ringinit(o, nd);
		break;
	case POLYGON:
		polygoninit(o, nd);
		break;
	case GEOMETRY:
		geometryinit(o, nd);
		break;
	case SUPERQUADRIC:
		superinit(o, nd);
		break;
	case ALGEBRAIC:
		alginit(o, nd);
		break;
	case CONE:
		coneinit(o, nd);
		break;
	case ELLIPSOID:
		ellipsinit(o, nd);
		break;
	case CYLINDER:
		cylinit(o, nd);
		break;
	case TORUS:
		torusinit(o, nd);
		break;
	case PATCH:
		patchinit(o, nd);
		break;
	case BLOBBY:
		blobinit(o, nd);
		break;
	case LIGHT:
		o->type = NULL_OBJ;
		lightinit(nd);
		break;
	default:
		sprintf(buf, "art: unkown object type %d in objectinit.\n", type);
		fatal(buf);
	}

	astackp = astackp->lst;
	mstackp = mstackp->lst;

	return(o);
}
