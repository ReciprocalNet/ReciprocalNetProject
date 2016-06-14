#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * cyli
 *
 *	returns a list of intersection points for the ray r and cylinder o.
 *
 */
hlist *
cyli(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist		*hitlist, *hp;
	real		ax1, ax2;
	int		entry, exit;
	ray		nr;
	real		a, b, c, d, t, t1, t2;

	transray(o, nr, *r);

	a = nr.dir.x * nr.dir.x + nr.dir.y * nr.dir.y;
	b = -(nr.org.x * nr.dir.x + nr.org.y * nr.dir.y);
	c = nr.org.x * nr.org.x + nr.org.y * nr.org.y - 1.0;

	d = b * b - a * c;

	hitlist = (hlist *)NULL;

	if (d < 0.0 || a == 0.0) {

		if (c > 0.0 || nr.dir.z == 0.0)
			return((hlist *)NULL);

		t1 = -nr.org.z / nr.dir.z;
		t2 = (1.0 - nr.org.z) / nr.dir.z;

		if (t1 < tolerance && t2 < tolerance)
			return((hlist *)NULL);

		if (t1 > t2) {
			t = t1;
			t1 = t2;
			t2 = t;
			entry = PZFACE;
			exit = NZFACE;
		} else {
			entry = NZFACE;
			exit = PZFACE;
		}
	} else {
		entry = exit = SIDE;

		d = sqrt((double)d);

		t1 = (b - d) / a;
		t2 = (b + d) / a;

		ax1 = t1 * nr.dir.z + nr.org.z;
		ax2 = t2 * nr.dir.z + nr.org.z;

		if (ax1 < 0.0 && ax2 < 0.0)
			return((hlist *)NULL);

		if (ax1 > 1.0 && ax2 > 1.0)
			return((hlist *)NULL);

		if (ax1 > 1.0) {
			entry = PZFACE;
			t1 = (1.0 - nr.org.z) / nr.dir.z;
		} else if (ax1 < 0.0) {
			entry = NZFACE;
			t1 = -nr.org.z / nr.dir.z;
		} 

		if (ax2 > 1.0) {
			exit = PZFACE;
			t2 = (1.0 - nr.org.z) / nr.dir.z;
		} else if (ax2 < 0.0) {
			exit = NZFACE;
			t2 = -nr.org.z / nr.dir.z;
		}
	}

	if (t1 >= tolerance) {
		fetch(hp);
		hp->type = entry;
		hitlist = hp;
		hp->t = t1;
		hp->obj = o;
		if (o->incsg) {
			fetch(hp);
			hitlist->nxt = hp;
			hp->type = exit;
			hp->t = t2;
			hp->obj = o;
		} 
		hp->nxt = (hlist *)NULL;
	} else if (t2 >= tolerance) {
		fetch(hp);
		hitlist = hp;
		hp->type = exit;
		hp->t = t2;
		hp->obj = o;
		hp->nxt = (hlist *)NULL;
	}

	*last = hp;

	return(hitlist);
}

/*
 * cyln
 *
 *	returns the normal to the cylinder o
 */
void
cyln(n, l, o, type)
	register vector		*n;
	register vector		*l;
	object			*o;
	int			type;
{
	if (type == SIDE) {
		toobject(o, *n, *l);

		n->z = 0.0;

	} else if (type == PZFACE) {
		n->x = 0.0;
		n->y = 0.0;
		n->z = 1.0;
	} else {
		n->x = 0.0;
		n->y = 0.0;
		n->z = -1.0;
	}
}

/*
 * cylc
 *
 *	return the colour of the cylinder o at the intersection point l
 */
void
cylc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	vector	tmp;
	float	u;
	int	w, h, indx;
	tiletxt	*tp;

	if (type == SIDE) {
		totexture(txt, tmp, *l);

		tmp.x /= o->td->nscales.x; 
		tmp.y /= o->td->nscales.y;
		tmp.z /= o->td->nscales.z;
				 
		if (fabs(tmp.x) > 1.0)
			tmp.x = (tmp.x < 0.0) ? -1.0 : 1.0;

		u = acos(tmp.x) / (2.0 * M_PI);

		tp = txt->u.t;

		w = fabs(tmp.z) * tp->scalew;
		h = ((tmp.y < 0.0) ? 1.0 - u : u) * tp->scaleh;

		if (tmp.z < 0.0)
			w = tp->pixw - 1 - (w % tp->pixw);
		else
			w = w % tp->pixw;

		indx = (w + (h % tp->pixh) * tp->pixw) * 3;

		pcol->r = (unsigned char)tp->map[indx] / 255.0;
		pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
		pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
	}
}

/*
 * cylinit
 *
 *	initialise the function pointers and fields of a cylinder object
 */
cylinit(o, d)
	object	*o;
	details *d;
{
	int	first;
	vector	c1, c2, rads;
	details	*ld;

	first = 1;

	rads.x = rads.y = 1.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			if (first) {
				c1 = d->u.v;
				first = 0;
			} else {
				c2 = d->u.v;
			}
			break;
		case RADIUS:
			if ((rads.x = rads.y = d->u.f) <= 0.0)
				fatal("art: cylinder radius must be > 0.0.\n");
			break;
		case RADII:
			rads.x = d->u.v.x;
			rads.y = d->u.v.y;
			if (rads.x <= 0.0 || rads.y <= 0.0)
				fatal("art: cylinder radii must be > 0.0.\n");
			break;
		default:
			warning("art: illegal field in cylinder ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (rads.x != 1.0 || rads.y != 1.0)
		obj_scale(rads.x, rads.y, 1.0);

	if (c1.x == 0.0 && c1.y == 0.0 && c1.z == 0.0) {
		if (c2.x != 0.0 || c2.y != 0.0 || c2.z != 1.0)
			obj_transform(c1, c2);
	} else if (c2.x == 0.0 && c2.y == 0.0 && c2.z == 0.0) {
		if (c1.x != 0.0 || c1.y != 0.0 || c1.z != 1.0)
			obj_transform(c1, c2);
	} else
		obj_transform(c1, c2);

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, 0.0, 1.0, 1.0, 1.0);

	setattributes(o);
}

/*
 * cyltabinit
 *
 *	set the table of function pointers for the cylinder.
 */
cyltabinit(intersects, normals, tilefun, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefun[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[CYLINDER] = cyln;
	intersects[CYLINDER] = cyli;
	tilefun[CYLINDER] = cylc;
	checkbbox[CYLINDER] = TRUE;
	selfshadowing[CYLINDER] = FALSE;
}
