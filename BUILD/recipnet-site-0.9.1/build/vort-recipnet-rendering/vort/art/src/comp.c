#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern hlist	*fhlist;

extern hlist	*(*intersects[])();

extern object	*objectinit(), *csginit();

extern attr	*astackp;
extern mats	*mstackp;

/*
 * compinit
 *
 *	initialise a composite object
 *
 */
object *
compinit(sym, d)
	symbol	*sym;
	details	*d;
{
	details *dl, *nxtdl, *argdt, *otherdt, *nd;
	details *ndl, *newdl, *dtail, *dhead, *tail;
	object	*o, *objlist, *head;
	surface	s;
	int	sset;

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

	calctransforms(mstackp);

	mstackp = mstackp->nxt;
	mstackp->d = mstackp->lst->d;
	mident4(mstackp->d.vm);

	s = *astackp->d.s; 
					/* copy sym list */
	otherdt = (details *)NULL;
	if (sym != (symbol *)NULL) {
		for (dl = sym->u.det; dl != (details *)NULL; dl = dl->nxt) {
			nd = (details *)smalloc(sizeof(details));
			copydetails(nd, dl);
			if (otherdt == (details *)NULL)
				tail = otherdt = nd;
			else {
				tail->nxt = nd;
				tail = nd;
			}
		}
	}

	objlist = (object *)NULL;

	if (d == (details *)NULL) {
		argdt = otherdt;
		otherdt = (details *)NULL;
	} else {
		argdt = d;
	}

	sset = FALSE;

	for (dl = argdt; dl != (details *)NULL; dl = nxtdl) {
		switch (dl->type) {
		case COLOUR:
			s.c.r = dl->u.c.r;
			s.c.g = dl->u.c.g;
			s.c.b = dl->u.c.b;
			sset = TRUE;
			break;
		case AMBIENT:
			s.a.r = dl->u.c.r;
			s.a.g = dl->u.c.g;
			s.a.b = dl->u.c.b;
			sset = TRUE;
			break;
		case TEXTURE:
			dl->u.txt->nxt = astackp->d.txtlist;
			astackp->d.txtlist = dl->u.txt;
			break;
		case MATERIAL:
			s.ri = dl->u.mat.ri;
			s.kd = dl->u.mat.kd;
			s.ks = dl->u.mat.ks;
			s.ksexp = dl->u.mat.ksexp;
			sset = TRUE;
			break;
		case REFLECTANCE:
			s.refl = dl->u.c;
			sset = TRUE;
			break;
		case TRANSPARENCY:
			s.trans = dl->u.c;
			sset = TRUE;
			break;
		case ABSORPTION:
			s.falloff = dl->u.c.r;
			sset = TRUE;
			break;
		case ALPHA:
			s.alpha = dl->u.c.r;
			sset = TRUE;
			break;
		case SHADOWS:
			s.shadows = dl->u.i;
			sset = TRUE;
			break;
		case TRANSLATE:
			translate(dl->u.v.x, dl->u.v.y, dl->u.v.z);
			break;
		case SCALE:
			scale(dl->u.v.x, dl->u.v.y, dl->u.v.z);
			break;
		case ROTATE:
			rotate(dl->u.rot.ang, dl->u.rot.axis);
			break;
		case TRANSFORM:
			transform(*dl->u.trans.m);
			free(dl->u.trans.m);
			break;
		case ON:
			astackp->d.options |= dl->u.i;
			break;
		case OFF:
			astackp->d.options &= ~dl->u.i;
			break;
		case OBJECT:
			if (sset) {
				astackp->d.s = (surface *)smalloc(sizeof(surface));
				*astackp->d.s = s;
				sset = FALSE;
			}

			if ((head = objectinit(dl->u.obj.sym, dl->u.obj.det)) != (object *)NULL) {
				for (o = head; o->nxt != (object *)NULL; o = o->nxt)
					;
				o->nxt = objlist;
				objlist = head;
			}
			break;
		case COMP_OBJ:
			if (sset) {
				astackp->d.s = (surface *)smalloc(sizeof(surface));
				*astackp->d.s = s;
				sset = FALSE;
			}

			if ((head = compinit((symbol *)NULL, dl->u.obj.det)) != (object *)NULL) {
				for (o = head; o->nxt != (object *)NULL; o = o->nxt)
					;
				o->nxt = objlist;
				objlist = head;
			}
			break;
		case CSG_OBJ:
			if (sset) {
				astackp->d.s = (surface *)smalloc(sizeof(surface));
				*astackp->d.s = s;
				sset = FALSE;
			}
			o = csginit(dl->u.obj.sym, (details *)NULL);
			o->nxt = objlist;
			objlist = o;
			break;
		default:
			warning("art: bad detail in composite ignored.\n");
		}

		nxtdl = dl->nxt;
		if (nxtdl == (details *)NULL) {
			nxtdl = otherdt;
			otherdt = (details *)NULL;
		}

		free(dl);
	}

	astackp = astackp->lst;
	mstackp = mstackp->lst;

	return(objlist);
}
