#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern hlist	*fhlist;

extern hlist	*(*intersects[])();

extern attr	*astackp;
extern mats	*mstackp;

extern object	*objectinit();

/*
 * csgaddi
 *
 *	a list of points hit by r in the csg tree formed by a union
 * operation, NULL otherwise
 *
 */
hlist *
csgaddi(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	csg		*tree;
	hlist		tmp, *lend, *rend;
	register int	leftb, rightb;
	register hlist	*lls, *rls, *next, *p, *lp;


	tree = o->obj.csgt;
	tree->hitobj = (object *)NULL;

	if (!missedbbox(r, &tree->left->bb))
		lls = intersects[tree->left->type](r, tree->left, &lend);
	else
		lls = (hlist *)NULL;

	if (!missedbbox(r, &tree->right->bb))
		rls = intersects[tree->right->type](r, tree->right, &rend);
	else
		rls = (hlist *)NULL;
	
	if (lls == rls)
		return((hlist *)NULL);

	leftb = tree->leftb;
	rightb = tree->rightb;

	for (p = lls; p != (hlist *)NULL; p = p->nxt)
		leftb = !leftb;

	for (p = rls; p != (hlist *)NULL; p = p->nxt)
		rightb = !rightb;

	next = &tmp;
	while (lls != (hlist *)NULL && rls != (hlist *)NULL) {
		if (lls->t < rls->t) {
			if (rightb) {
				next->nxt = lls;
				next = next->nxt;
				lls = lls->nxt;
			} else {
				p = lls;
				lls = lls->nxt;
				release(p);
			}
			leftb = !leftb;
		} else {
			if (leftb) {
				next->nxt = rls;
				next = next->nxt;
				rls = rls->nxt;
			} else {
				p = rls;
				rls = rls->nxt;
				release(p);
			}
			rightb = !rightb;
		}
	}

	if (lls != (hlist *)NULL && rightb) {
		next->nxt = lls;
		*last = lend;
		for (lp = rls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	} else if (rls != (hlist *)NULL && leftb) {
		next->nxt = rls;
		*last = rend;
		for (lp = lls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	} else {
		next->nxt = (hlist *)NULL;
		*last = next;
		for (lp = rls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
		for (lp = lls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	}

	if (tmp.nxt != (hlist *)NULL)
		tree->hitobj = tmp.nxt->obj;

	return(tmp.nxt);
}

/*
 * csgsubinti
 *
 *	a list of points hit by r in the csg tree resulting form a subtract
 * or an intersection operation, NULL otherwise
 *
 */
hlist *
csgsubinti(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	csg		*tree;
	hlist		tmp, *lend, *rend;
	register int	leftb, rightb;
	register hlist	*lls, *rls, *next, *p, *lp;

	tree = o->obj.csgt;
	tree->hitobj = (object *)NULL;

	if (!missedbbox(r, &tree->left->bb))
		lls = intersects[tree->left->type](r, tree->left, &lend);
	else
		return((hlist *)NULL);

	if (lls == (hlist *)NULL)
		return((hlist *)NULL);

	if (!missedbbox(r, &tree->right->bb))
		rls = intersects[tree->right->type](r, tree->right, &rend);
	else
		rls = (hlist *)NULL;
	
	leftb = tree->leftb;
	rightb = tree->rightb;

	for (p = lls; p != (hlist *)NULL; p = p->nxt)
		leftb = !leftb;

	for (p = rls; p != (hlist *)NULL; p = p->nxt) {
		if (o->type == CSG_SUB)
			p->obj->incsg = SUBTRACTED;
		rightb = !rightb;
	}

	next = &tmp;
	while (lls != (hlist *)NULL && rls != (hlist *)NULL) {
		if (lls->t < rls->t) {
			if (rightb) {
				next->nxt = lls;
				next = next->nxt;
				lls = lls->nxt;
			} else {
				p = lls;
				lls = lls->nxt;
				release(p);
			}
			leftb = !leftb;
		} else {
			if (leftb) {
				next->nxt = rls;
				next = next->nxt;
				rls = rls->nxt;
			} else {
				p = rls;
				rls = rls->nxt;
				release(p);
			}
			rightb = !rightb;
		}
	}

	if (lls != (hlist *)NULL && rightb) {
		next->nxt = lls;
		*last = lend;
		for (lp = rls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	} else if (rls != (hlist *)NULL && leftb) {
		next->nxt = rls;
		*last = rend;
		for (lp = lls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	} else {
		next->nxt = (hlist *)NULL;
		*last = next;
		for (lp = rls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
		for (lp = lls; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	}

	if (tmp.nxt != (hlist *)NULL)
		tree->hitobj = tmp.nxt->obj;

	return(tmp.nxt);
}

/*
 * csgn
 *
 *	flag an error - this shouldn't happen
 */
void
csgn(n, l, o, type)
	vector	*n, *l;
	object	*o;
	int	type;
{
	object		*hitobj;
	extern void	(*normal[])();

	fatal("art: csgn called!\n");
}

/*
 * csgc
 *
 *	return the tiling for the part of the csg object we hit
 */
void
csgc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	object		*hitobj;
	extern void	(*tilefun[])();

	hitobj = o->obj.csgt->hitobj;

	tilefun[hitobj->type](o, txt, l, n, pcol, type);
}

/*
 * getcsgobj
 *
 *	instantiate an object, mark it as being in csg and return it
 */
object *
getcsgobj(sym, d)
	symbol	*sym;
	details	*d;
{
	object	*obj;

	obj = objectinit(sym, d);

	return(obj);
}

/*
 * makecsgtree
 *
 *	set up a csg object from its tree and details list.
 */
object *
makecsgtree(tree)
	csgnode	*tree;
{
	register object	*o;
	register csg	*c;

	switch (tree->type) {
	case CSG_SUB:
		o = (object *)smalloc(sizeof(object));
		c = o->obj.csgt = (csg *)smalloc(sizeof(csg));
		o->type = CSG_SUB;
		o->s = astackp->d.s;
		c->leftb = FALSE;
		c->rightb = TRUE;
		c->left = makecsgtree(tree->u.branch.left);
		c->right = makecsgtree(tree->u.branch.right);
		c->left->incsg = ADDED;
		c->right->incsg = SUBTRACTED;

					/* take bounding box of left */
		o->bb = c->left->bb;
		break;
	case CSG_ADD:
		o = (object *)smalloc(sizeof(object));
		c = o->obj.csgt = (csg *)smalloc(sizeof(csg));
		o->type = CSG_ADD;
		o->s = astackp->d.s;
		c->leftb = TRUE;
		c->rightb = TRUE;
		c->left = makecsgtree(tree->u.branch.left);
		c->right = makecsgtree(tree->u.branch.right);
		c->left->incsg = ADDED;
		c->right->incsg = ADDED;

		if (c->left->bb.max[X] > c->right->bb.max[X])
			o->bb.max[X] = c->left->bb.max[X];
		else
			o->bb.max[X] = c->right->bb.max[X];
		if (c->left->bb.max[Y] > c->right->bb.max[Y])
			o->bb.max[Y] = c->left->bb.max[Y];
		else
			o->bb.max[Y] = c->right->bb.max[Y];
		if (c->left->bb.max[Z] > c->right->bb.max[Z])
			o->bb.max[Z] = c->left->bb.max[Z];
		else
			o->bb.max[Z] = c->right->bb.max[Z];

		if (c->left->bb.min[X] < c->right->bb.min[X])
			o->bb.min[X] = c->left->bb.min[X];
		else
			o->bb.min[X] = c->right->bb.min[X];
		if (c->left->bb.min[Y] < c->right->bb.min[Y])
			o->bb.min[Y] = c->left->bb.min[Y];
		else
			o->bb.min[Y] = c->right->bb.min[Y];
		if (c->left->bb.min[Z] < c->right->bb.min[Z])
			o->bb.min[Z] = c->left->bb.min[Z];
		else
			o->bb.min[Z] = c->right->bb.min[Z];
		break;
	case CSG_INT:
		o = (object *)smalloc(sizeof(object));
		c = o->obj.csgt = (csg *)smalloc(sizeof(csg));
		o->type = CSG_INT;
		o->s = astackp->d.s;
		c->leftb = FALSE;
		c->rightb = FALSE;
		c->left = makecsgtree(tree->u.branch.left);
		c->right = makecsgtree(tree->u.branch.right);
		c->left->incsg = ADDED;
		c->right->incsg = ADDED;

							/* take overlap */
		if (c->left->bb.max[X] < c->right->bb.max[X])
			o->bb.max[X] = c->left->bb.max[X];
		else
			o->bb.max[X] = c->right->bb.max[X];
		if (c->left->bb.max[Y] < c->right->bb.max[Y])
			o->bb.max[Y] = c->left->bb.max[Y];
		else
			o->bb.max[Y] = c->right->bb.max[Y];
		if (c->left->bb.max[Z] < c->right->bb.max[Z])
			o->bb.max[Z] = c->left->bb.max[Z];
		else
			o->bb.max[Z] = c->right->bb.max[Z];

		if (c->left->bb.min[X] > c->right->bb.min[X])
			o->bb.min[X] = c->left->bb.min[X];
		else
			o->bb.min[X] = c->right->bb.min[X];
		if (c->left->bb.min[Y] > c->right->bb.min[Y])
			o->bb.min[Y] = c->left->bb.min[Y];
		else
			o->bb.min[Y] = c->right->bb.min[Y];
		if (c->left->bb.min[Z] > c->right->bb.min[Z])
			o->bb.min[Z] = c->left->bb.min[Z];
		else
			o->bb.min[Z] = c->right->bb.min[Z];
		break;
	case OBJECT:
		o = getcsgobj(tree->u.sym, (details *)NULL);
		break;
	default:
		fatal("art: illegal type in csgtree.\n");

	}

	o->nxt = (object *)NULL;

	return(o);
}
	
/*
 * csginit
 *
 *	initialise a csg object
 *
 */
object *
csginit(sym, d)
	symbol	*sym;
	details	*d;
{
	details *dl, *nxtdl, *argdt, *otherdt, *nd;
	object	*o;
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

	mstackp = mstackp->nxt;
	mstackp->d = mstackp->lst->d;
	mident4(mstackp->d.vm);

	s = *astackp->d.s; 

					/* copy sym list */
	otherdt = (details *)NULL;
	for (dl = sym->u.det->u.csgobj.det; dl != (details *)NULL; dl = dl->nxt) {
		nd = (details *)smalloc(sizeof(details));
		copydetails(nd, dl);
		if (otherdt == (details *)NULL) 
			nxtdl = otherdt = nd;
		else {   
			nxtdl->nxt = nd;
			nxtdl = nd; 
		}    
	}

	if (d == (details *)NULL) {
		argdt = otherdt;
		otherdt = (details *)NULL;
	} else
		argdt = d;

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
		case CSG_OBJ:
		case OBJECT:
			break;
		default:
			warning("art: bad detail in csg ignored.\n");
		}

		nxtdl = dl->nxt;
		if (nxtdl == (details *)NULL) {
			nxtdl = otherdt;
			otherdt = (details *)NULL;
		}

		free(dl);
	}

	if (sset) {
		astackp->d.s = (surface *)smalloc(sizeof(surface));
		*astackp->d.s = s;
	}

	o = makecsgtree(sym->u.det->u.csgobj.tree);

	astackp = astackp->lst;
	mstackp = mstackp->lst;

	return(o);
}

/*
 * csgtabinit
 *
 *	set the table of function pointers for a sphere object.
 */
csgtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[CSG_ADD] = csgn;
	normals[CSG_SUB] = csgn;
	normals[CSG_INT] = csgn;

	intersects[CSG_ADD] = csgaddi;
	intersects[CSG_SUB] = csgsubinti;
	intersects[CSG_INT] = csgsubinti;

	tilefuns[CSG_ADD] = csgc;
	tilefuns[CSG_SUB] = csgc;
	tilefuns[CSG_INT] = csgc;

	checkbbox[CSG_ADD] = TRUE;
	checkbbox[CSG_SUB] = TRUE;
	checkbbox[CSG_INT] = TRUE;

	selfshadowing[CSG_ADD] = TRUE;
	selfshadowing[CSG_SUB] = TRUE;
	selfshadowing[CSG_INT] = TRUE;
}
