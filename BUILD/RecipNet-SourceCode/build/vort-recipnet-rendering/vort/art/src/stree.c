#include <stdio.h>
#include <math.h>
#include "art.h"
#include "objs.h"
#include "gram.h"
#include "macro.h"

extern object	*oblist;
extern light	*lights;
extern hlist	*fhlist;
extern int	maxhitlevel;

extern int	maxtreedepth;

extern hlist	*(*intersects[])();
extern int	checkbbox[];

extern pixel	backcol;
extern colour	ambient;

extern double	power();

#define	U	0
#define	V	1

extern object	*oblist;
extern light	*lights;
extern hlist	*fhlist;
extern int	maxhitlevel;

extern pixel	backcol;
extern colour	ambient;

extern double	power();

extern real	tolerance;

/*
 * origin and direction for current ray
 */
float	orgs[DIMS], dirs[DIMS];

/*
 * shrinkbbox
 *
 *	reduce the size of the bounding box for the leaf (if possible).
 */
shrinkbbox(leaf)
	stree	*leaf;
{
	register object	*o;
	register olist	*ol;
	register float	minx, miny, minz;
	register float	maxx, maxy, maxz;

	if (leaf->u.leaf.oblist == (olist *)NULL)
		return;

	o = leaf->u.leaf.oblist->obj;

	minx = o->bb.min[X];
	miny = o->bb.min[Y];
	minz = o->bb.min[Z];

	maxx = o->bb.max[X];
	maxy = o->bb.max[Y];
	maxz = o->bb.max[Z];

	for (ol = leaf->u.leaf.oblist->nxt; ol != (olist *)NULL; ol = ol->nxt) {
		o = ol->obj;

		if (o->bb.min[X] < minx)
			minx = o->bb.min[X];
		if (o->bb.max[X] > maxx)
			maxx = o->bb.max[X];

		if (o->bb.min[Y] < miny)
			miny = o->bb.min[Y];
		if (o->bb.max[Y] > maxy)
			maxy = o->bb.max[Y];

		if (o->bb.min[Z] < minz)
			minz = o->bb.min[Z];
		if (o->bb.max[Z] > maxz)
			maxz = o->bb.max[Z];
	}

	if (minx > leaf->bb.min[X])
		leaf->bb.min[X] = minx;
	if (miny > leaf->bb.min[Y])
		leaf->bb.min[Y] = miny;
	if (minz > leaf->bb.min[Z])
		leaf->bb.min[Z] = minz;

	if (maxx < leaf->bb.max[X])
		leaf->bb.max[X] = maxx;
	if (maxy < leaf->bb.max[Y])
		leaf->bb.max[Y] = maxy;
	if (maxz < leaf->bb.max[Z])
		leaf->bb.max[Z] = maxz;
}

/*
 * splitleaf
 *
 *	split a leaf along the middle of an axis.
 */
splitleaf(axis, leaf, val)
	int	axis;
	stree	*leaf;
	float	val;
{
	register int	ax, count;
	register object	*o;
	register stree	*left, *right;
	register olist	*objs, *bothlist, *nxtobj, *tmpobj;

	if (leaf->type != SPLITABLELEAF)
		return;

	left = (stree *)smalloc(sizeof(stree));
	right = (stree *)smalloc(sizeof(stree));

	bothlist = left->u.leaf.oblist = right->u.leaf.oblist = (olist *)NULL;

	ax = axis;
	 
	for (objs = leaf->u.leaf.oblist; objs != (olist *)NULL; objs = nxtobj) {
		nxtobj = objs->nxt;
		o = objs->obj;

		if (o->bb.max[ax] < val) {		/* to the left */
			objs->nxt = left->u.leaf.oblist;
			left->u.leaf.oblist = objs;
		} else if (o->bb.min[ax] > val) {	/* to the right */
			objs->nxt = right->u.leaf.oblist;
			right->u.leaf.oblist = objs;
		} else {				/* in both (sigh) */
			objs->nxt = bothlist;
			bothlist = objs;
		}
	}

	if (bothlist != (olist *)NULL) {
		if (right->u.leaf.oblist == (olist *)NULL) {
			o = left->u.leaf.oblist->obj;

			val = o->bb.max[ax];

			objs = left->u.leaf.oblist->nxt;

			while (objs != (olist *)NULL) {
				o = objs->obj;

				if (o->bb.max[ax] > val)
					val = o->bb.max[ax];

				objs = objs->nxt;
			}

			for (objs = bothlist; objs != (olist *)NULL; objs = nxtobj) {
				nxtobj = objs->nxt;

				o = objs->obj;

				if (o->bb.min[ax] < val) {
					tmpobj = (olist *)smalloc(sizeof(olist));

					tmpobj->obj = objs->obj;

					tmpobj->nxt = left->u.leaf.oblist;
					left->u.leaf.oblist = tmpobj;
				}

				objs->nxt = right->u.leaf.oblist;
				right->u.leaf.oblist = objs;
			}
		} else if (left->u.leaf.oblist == (olist *)NULL) {
			o = right->u.leaf.oblist->obj;

			val = o->bb.min[axis];

			objs = right->u.leaf.oblist->nxt;

			while (objs != (olist *)NULL) {
				o = objs->obj;

				if (o->bb.min[ax] < val)
					val = o->bb.min[ax];

				objs = objs->nxt;
			}

			for (objs = bothlist; objs != (olist *)NULL; objs = nxtobj) {
				nxtobj = objs->nxt;

				o = objs->obj;

				if (o->bb.max[ax] > val) {
					tmpobj = (olist *)smalloc(sizeof(olist));

					tmpobj->obj = objs->obj;

					tmpobj->nxt = right->u.leaf.oblist;
					right->u.leaf.oblist = tmpobj;
				}

				objs->nxt = left->u.leaf.oblist;
				left->u.leaf.oblist = objs;
			}
		} else
			for (objs = bothlist; objs != (olist *)NULL; objs = nxtobj) {
				nxtobj = objs->nxt;

				tmpobj = (olist *)smalloc(sizeof(olist));

				tmpobj->obj = objs->obj;

				tmpobj->nxt = left->u.leaf.oblist;
				left->u.leaf.oblist = tmpobj;

				objs->nxt = right->u.leaf.oblist;
				right->u.leaf.oblist = objs;
			}
	}

	left->bb = right->bb = leaf->bb;
	left->bb.max[ax] = val;
	right->bb.min[ax] = val;
				  
	shrinkbbox(left);
	shrinkbbox(right);

	left->u.leaf.depth = right->u.leaf.depth = leaf->u.leaf.depth + 1;

	left->type = right->type = SPLITABLELEAF;

	leaf->type = axis;
	leaf->splitval = val;
	leaf->u.branch.left = left;
	leaf->u.branch.right = right;
}

/*
 * split
 *
 *	split a splittable leaf into two nodes.
 */
void
split(root)
	stree	*root;
{
	int		left[DIMS], right[DIMS], extras[DIMS], tot[DIMS];
	float		vals[DIMS];
	register olist	*ol;
	register object	*o;
	register int	total;

	if (root->u.leaf.oblist == (olist *)NULL || root->u.leaf.oblist->nxt == (olist *)NULL || root->u.leaf.depth == maxtreedepth) {
		root->type = LEAF;
		return;
	}

	vals[X] = (root->bb.max[X] + root->bb.min[X]) / 2;
	vals[Y] = (root->bb.max[Y] + root->bb.min[Y]) / 2;
	vals[Z] = (root->bb.max[Z] + root->bb.min[Z]) / 2;

	left[X] = right[X] = extras[X] = 0;
	left[Y] = right[Y] = extras[Y] = 0;
	left[Z] = right[Z] = extras[Z] = 0;

	for (ol = root->u.leaf.oblist; ol != (olist *)NULL; ol = ol->nxt) {
		o = ol->obj;

		if (o->bb.max[X] < vals[X])		/* to the left */
			left[X]++;
		else if (o->bb.min[X] > vals[X])	/* to the right */
			right[X]++;
		else
			extras[X]++;

		if (o->bb.max[Y] < vals[Y])		/* to the left */
			left[Y]++;
		else if (o->bb.min[Y] > vals[Y])	/* to the right */
			right[Y]++;
		else
			extras[Y]++;

		if (o->bb.max[Z] < vals[Z])		/* to the left */
			left[Z]++;
		else if (o->bb.min[Z] > vals[Z])	/* to the right */
			right[Z]++;
		else
			extras[Z]++;
	}

	/*
	 * we don't want data set to expand when we get to SPLITCUTOFF2
	 * and below...
	 */
	if ((total = left[X] + right[X] + extras[X]) > SPLITCUTOFF2) {
		if (total < SPLITCUTOFF1)
			total += total / 3;
		else
			total = (total - 1) * 2;
	}
	
	tot[X] = left[X] + right[X] + 2 * extras[X];
	tot[Y] = left[Y] + right[Y] + 2 * extras[Y];
	tot[Z] = left[Z] + right[Z] + 2 * extras[Z];

	if (tot[X] < tot[Y]) {
		if (tot[X] < tot[Z]) {
			if (tot[X] <= total)
				splitleaf(X, root, vals[X]);
			else
				root->type = LEAF;
		} else if (tot[X] == tot[Z]) {
			if (extras[X] < extras[Z]) {
				if (tot[X] <= total)
					splitleaf(X, root, vals[X]);
				else
					root->type = LEAF;
			} else {
				if (tot[Z] <= total)
					splitleaf(Z, root, vals[Z]);
				else
					root->type = LEAF;
			}
		} else {
			if (tot[Z] <= total)
				splitleaf(Z, root, vals[Z]);
			else
				root->type = LEAF;
		}
	} else {
		if (tot[Y] < tot[Z]) {
			if (tot[Y] == tot[X]) {
				if (extras[X] < extras[Y]) {
					if (tot[X] <= total)
						splitleaf(X, root, vals[X]);
					else
						root->type = LEAF;
				} else {
					if (tot[Y] <= total)
						splitleaf(Y, root, vals[Y]);
					else
						root->type = LEAF;
				}
			} else
				if (tot[Y] <= total)
					splitleaf(Y, root, vals[Y]);
				else
					root->type = LEAF;
		} else if (tot[Y] == tot[Z]) {
			if (extras[Y] < extras[Z]) {
				if (tot[Y] <= total)
					splitleaf(Y, root, vals[Y]);
				else
					root->type = LEAF;
			} else {
				if (tot[Z] <= total)
					splitleaf(Z, root, vals[Z]);
				else
					root->type = LEAF;
			}
		} else {
			if (tot[Z] <= total)
				splitleaf(Z, root, vals[Z]);
			else
				root->type = LEAF;
		}
	}
}

/*
 * checkleaf
 *
 *	check the objects in a leaf - returning a legal hit if there is
 * one
 */
hlist *
checkleaf(r, leaf)
	ray	*r;
	stree	*leaf;
{
	olist	*ol;
	object	*o;
	float	val;
	surface	*sp;
	hlist	*end, *hit, *hp, *lp, *prevhits;

	prevhits = (hlist *)NULL;

	for (ol = leaf->u.leaf.oblist; ol != (olist *)NULL; ol = ol->nxt) {
		o = ol->obj;

		if (o->lastray.raynumber == r->raynumber) {
			if (o->lastray.t != 0.0) {
				fetch(hit);
				hit->t = o->lastray.t;
				hit->type = o->lastray.type;
				if (o->type == CSG_ADD || o->type == CSG_INT || o->type == CSG_SUB)
					hit->obj = o->obj.csgt->hitobj;
				else
					hit->obj = o;
				hit->nxt = (hlist *)NULL;
			} else
				continue;		/* go to next object */

		} else {

			o->lastray.raynumber = r->raynumber;
			o->lastray.t = 0.0;

			if (r->type == SHADOW_RAY && !o->s->shadows)
				continue;

			if (checkbbox[o->type] && missedbbox(r, &o->bb))
				continue;

			if ((hit = intersects[o->type](r, o, &end)) == (hlist *)NULL)
				continue;		/* go to next object */

			o->lastray.t = hit->t;
			o->lastray.type = hit->type;
		}

		/*
		 * check that we need to look further...
		 */
		if (r->type == SHADOW_RAY && hit->t < r->maxt) {
			if (o->txtlist == (texture *)NULL) {
				sp = o->s;
				if (sp->trans.r == 0.0 && sp->trans.g == 0.0 && sp->trans.b == 0.0) {
					for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
						lp = hp->nxt;
						release(hp);
					}
					return(hit);
				}
			}
		}

		if (prevhits == (hlist *)NULL) {
			prevhits = hit;
		} else {
			if (prevhits->t > hit->t) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}

				prevhits = hit;
			} else {
				for (hp = hit; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}

			}
		}
	}

	if (prevhits != (hlist *)NULL) {

		val = orgs[X] + dirs[X] * prevhits->t;
		if (dirs[X] < 0.0) {
			if (val < leaf->bb.min[X]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > leaf->bb.max[X]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}

		val = orgs[Y] + dirs[Y] * prevhits->t;
		if (dirs[Y] < 0.0) {
			if (val < leaf->bb.min[Y]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > leaf->bb.max[Y]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}

		val = orgs[Z] + dirs[Z] * prevhits->t;
		if (dirs[Z] < 0.0) {
			if (val < leaf->bb.min[Z]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > leaf->bb.max[Z]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}
	}

	return(prevhits);
}

/*
 * treetrace
 *
 *	returns the first hit on the kd tree pointed to by root.
 */
hlist *
treetrace(r, root)
	ray	*r;
	stree	*root;
{
	hlist	*hit;
	stree	*nxtbranch;
	int	p, u, v;
	float	t1, t2, othert1, othert2, org, dir;

	hit = (hlist *)NULL;

	if (root->type == LEAF)
		return(checkleaf(r, root));
	else if (root->type == SPLITABLELEAF) {
		split(root);
		hit = treetrace(r, root);
	} else {
		p = root->type;
		org = orgs[p];
		dir = dirs[p];

		if (org < root->splitval) {
			if (!missedbbox(r, &root->u.branch.left->bb))
				hit = treetrace(r, root->u.branch.left);
			else
				hit = (hlist *)NULL;

			if (hit != (hlist *)NULL) {
				return(hit);
			} else if (dir <= 0.0)
				return((hlist *)NULL);

			nxtbranch = root->u.branch.right;
			t1 = (nxtbranch->bb.min[p] - org) / dir;
			t2 = (nxtbranch->bb.max[p] - org) / dir;

		} else {
			if (!missedbbox(r, &root->u.branch.right->bb))
				hit = treetrace(r, root->u.branch.right);
			else
				hit = (hlist *)NULL;

			if (hit != (hlist *)NULL) {
				return(hit);
			} else if (dir >= 0.0)
				return((hlist *)NULL);

			nxtbranch = root->u.branch.left;
			t1 = (nxtbranch->bb.max[p] - org) / dir;
			t2 = (nxtbranch->bb.min[p] - org) / dir;
		} 

		if (p == X) {
			u = Y;
			v = Z;
		} else if (p == Y) {
			u = Z;
			v = X;
		} else {
			u = X;
			v = Y;
		}

		org = orgs[u];
		dir = dirs[u];

                if (dir != 0.0) {
                        othert1 = (nxtbranch->bb.min[u] - org) / dir;
                        othert2 = (nxtbranch->bb.max[u] - org) / dir;
                        if (othert1 > othert2) {
                                if (othert1 < t1 || othert2 > t2)
                                        return((hlist *)NULL);
				if (othert2 > t1)
					t1 = othert2;
				if (othert1 < t2)
					t2 = othert1;
                        } else {
                                if (othert2 < t1 || othert1 > t2)
                                        return((hlist *)NULL);
				if (othert1 > t1)
					t1 = othert1;
				if (othert2 < t2)
					t2 = othert2;
                        }
                } else {
                        if (nxtbranch->bb.min[u] > org)
                                return((hlist *)NULL);
                        if (nxtbranch->bb.max[u] < org)
                                return((hlist *)NULL);
                }

		org = orgs[v];
		dir = dirs[v];

                if (dir != 0.0) {
                        othert1 = (nxtbranch->bb.min[v] - org) / dir;
                        othert2 = (nxtbranch->bb.max[v] - org) / dir;
                        if (othert1 > othert2) {
                                if (othert1 < t1 || othert2 > t2)
                                        return((hlist *)NULL);
                        } else {
                                if (othert2 < t1 || othert1 > t2)
                                        return((hlist *)NULL);
                        }
                } else {
                        if (nxtbranch->bb.min[v] > org)
                                return((hlist *)NULL);
                        if (nxtbranch->bb.max[v] < org)
                                return((hlist *)NULL);
                }

		hit = treetrace(r, nxtbranch);
	}

	return(hit);
}

/*
 * streei
 *
 *	return the intersections between the ray r and the kd tree in o.
 */
hlist	*
streei(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	hlist	*hit;
	float	oldorgs[3], olddirs[3];

	oldorgs[X] = orgs[X];
	oldorgs[Y] = orgs[Y];
	oldorgs[Z] = orgs[Z];

	olddirs[X] = dirs[X];
	olddirs[Y] = dirs[Y];
	olddirs[Z] = dirs[Z];

	orgs[X] = r->org.x;
	orgs[Y] = r->org.y;
	orgs[Z] = r->org.z;

	dirs[X] = r->dir.x;
	dirs[Y] = r->dir.y;
	dirs[Z] = r->dir.z;

	hit = treetrace(r, o->obj.tree);

	orgs[X] = oldorgs[X];
	orgs[Y] = oldorgs[Y];
	orgs[Z] = oldorgs[Z];

	dirs[X] = olddirs[X];
	dirs[Y] = olddirs[Y];
	dirs[Z] = olddirs[Z];

	return(hit);
}

/*
 * treeinit
 *
 *	set up a kd tree
 */
object *
treeinit(obs)
	object	*obs;
{
	object		*obj, *o;
	olist		*ol;
	stree		*tree;
	static surface  treesurface;
	 
	obj = (object *)smalloc(sizeof(object));

	obj->type = STREE;
	obj->obj.tree = tree = (stree *)smalloc(sizeof(stree));

	/*  
	 * set up a surface so shadow optimisation and stuff 
	 * doesn't get confused. 
	 */ 
	treesurface.shadows = TRUE; 
	treesurface.trans.r = 0.0; 
	treesurface.trans.g = 0.0;
	treesurface.trans.b = 0.0;

	obj->s = &treesurface; 

	/*
	 * set up the tree.
	 */
	tree->type = SPLITABLELEAF;

	tree->u.leaf.oblist = (olist *)NULL;
	tree->u.leaf.depth = 0;

	tree->bb.min[X] = obs->bb.min[X];
	tree->bb.max[X] = obs->bb.max[X];

	tree->bb.min[Y] = obs->bb.min[Y];
	tree->bb.max[Y] = obs->bb.max[Y];

	tree->bb.min[Z] = obs->bb.min[Z];
	tree->bb.max[Z] = obs->bb.max[Z];

	for (o = obs; o != (object *)NULL; o = o->nxt) {
		ol = (olist *)smalloc(sizeof(olist));
		ol->nxt = tree->u.leaf.oblist;
		ol->obj = o;
		adjustbbox(&tree->bb, o);
		tree->u.leaf.oblist = ol;
	}

	obj->bb = tree->bb;

	return(obj);
}

/*
 * treetabinit
 *
 *	set the table of function pointers for the tree.
 */
treetabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[STREE] = (void (*)())NULL;
	intersects[STREE] = streei;
	tilefuns[STREE] = (void (*)())NULL;
	checkbbox[STREE] = TRUE;
	selfshadowing[STREE] = TRUE;
}
