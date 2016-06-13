#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * boxi
 *
 *	returns a list of intersection points for the ray r and box o.
 */
hlist *
boxi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	hlist	*hitlist, *hp;
	int	entry, exit, ent, ext;
	ray	nr;
	real	dir, org, t, t1, t2, b1, b2;

	transray(o, nr, *r);

	t1 = 0.0;
	t2 = HUGE_DIST;

	dir = nr.dir.x;
	org = nr.org.x;
	if (dir != 0.0) {

		if (dir < 0.0) {
			if (org < 0.0)
				return((hlist *)NULL);
			b2 = -org;
			b1 = 1.0 - org;
			entry = PXFACE;
			exit = NXFACE;
		} else {
			if (org > 1.0)
				return((hlist *)NULL);
			b2 = 1.0 - org;
			b1 = -org;
			exit = NXFACE;
			entry = PXFACE;
		}
			
		t2 = b2 / dir;
		t1 = b1 / dir;
		if (t1 < 0.0)
			t1 = 0.0;
	} else if (org < 0.0 || org > 1.0)
		return((hlist *)NULL);

	dir = nr.dir.y;
	org = nr.org.y;
	if (dir != 0.0) {

		if (dir < 0.0) {
			if (org < 0.0)
				return((hlist *)NULL);
			b2 = -org;
			b1 = 1.0 - org;
			ent = PYFACE; 
			ext = NYFACE;
		} else {
			if (org > 1.0)
				return((hlist *)NULL);
			b2 = 1.0 - org;
			b1 = -org;
			ent = NYFACE; 
			ext = PYFACE;
		}
			
		if ((t = b2 / dir) < t1)
			return((hlist *)NULL);

		if (t < t2) {
			t2 = t;
			exit = ext;
		}

		if ((t = b1 / dir) > t2)
			return((hlist *)NULL);

		if (t > t1) {
			t1 = t;
			entry = ent;
		}
	} else if (org < 0.0 || org > 1.0)
		return((hlist *)NULL);

	dir = nr.dir.z;
	org = nr.org.z;
	if (dir != 0.0) {
		if (dir < 0.0) {
			if (org < 0.0)
				return((hlist *)NULL);
			b2 = -org;
			b1 = 1.0 - org;
			ent = PZFACE; 
			ext = NZFACE;
		} else {
			if (org > 1.0)
				return((hlist *)NULL);
			b2 = 1.0 - org;
			b1 = -org;
			ent = NZFACE; 
			ext = PZFACE;
		}
			
		if ((t = b2 / dir) < t1)
			return((hlist *)NULL);

		if (t < t2) {
			t2 = t;
			exit = ext;
		}

		if ((t = b1 / dir) > t2)
			return((hlist *)NULL);

		if (t > t1) {
			t1 = t;
			entry = ent;
		}
	} else if (org < 0.0 || org > 1.0)
		return((hlist *)NULL);

	hitlist = (hlist *)NULL;

	if (t1 > tolerance) {
		fetch(hp);
		hitlist = hp;
		hitlist->obj = o;
		hitlist->type = entry;
		hitlist->t = t1;
		if (o->incsg) {
			fetch(hp);
			hitlist->nxt = hp;
			hp->obj = o;
			hp->type = exit;
			hp->t = t2;
		}
		hp->nxt = (hlist *)NULL;
	} else if (t2 > tolerance) {
		fetch(hp);
		hitlist = hp;
		hitlist->obj = o;
		hitlist->type = exit;
		hitlist->t = t2;
		hp->nxt = (hlist *)NULL;
	}

	*last = hp;

	return(hitlist);
}

/*
 * boxn
 *
 *	returns the normal to the box b
 */
/*ARGSUSED*/
void
boxn(n, l, o, type)
	register vector	*n;
	vector		*l;
	object		*o;
	int		type;
{
	n->x = 0.0;
	n->y = 0.0;
	n->z = 0.0;
	
	switch (type) {
	case NXFACE:
		n->x = -1.0;
		break;
	case NYFACE:
		n->y = -1.0;
		break;
	case NZFACE:
		n->z = -1.0;
		break;
	case PXFACE:
		n->x = 1.0;
		break;
	case PYFACE:
		n->y = 1.0;
		break;
	case PZFACE:
		n->z = 1.0;
		break;
	default:
		fatal("art: illegal face type in boxn.\n");
	}
}

/*
 * boxc
 *
 *	stub in case tiling appears
 */
void
boxc(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	pixel	*pcol;
	int	type;
{
}

/*
 * boxinit
 *
 *	initialise the function pointers and extra field for a box object
 */
void
boxinit(o, d)
	object	*o;
	details *d;
{
	int	first;
	vector	c1, c2, max, min;
	details	*ld;

	c1.x = c1.y = c1.z = 0.0;		/* default box */
	c2.x = c2.y = c2.z = 1.0;

	first = 1;

	while (d != (details *)NULL) {
		switch (d->type) {
		case VERTEX:
			if (first) {
				c1 = d->u.v;
				first = 0;
			} else 
				c2 = d->u.v;
			break;
		default:
			warning("art: illegal field in box ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (c1.x > c2.x) {
		max.x = c1.x;
		min.x = c2.x;
	} else {
		max.x = c2.x;
		min.x = c1.x;
	}

	if (c1.y > c2.y) {
		max.y = c1.y;
		min.y = c2.y;
	} else {
		max.y = c2.y;
		min.y = c1.y;
	}

	if (c1.z > c2.z) {
		max.z = c1.z;
		min.z = c2.z;
	} else {
		max.z = c2.z;
		min.z = c1.z;
	}

	obj_scale(max.x - min.x, max.y - min.y, max.z - min.z);

	obj_translate(min.x, min.y, min.z);

	calctransforms(mstackp);

	makebbox(o, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

	setattributes(o);
}

/*
 * boxtabinit
 *
 *	set the table of function pointers for the box.
 */
boxtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[BOX] = boxn;
	intersects[BOX] = boxi;
	tilefuns[BOX] = boxc;
	checkbbox[BOX] = TRUE;
	selfshadowing[BOX] = FALSE;
}
