#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * ringi
 *
 *	returns an intersection point for the ray and ring o
 */
hlist *
ringi(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist	*hitlist, *hp;
	ray	nr;
	real	x, y, prod, t;
	ring	*rng;

	transray(o, nr, *r);

	rng = o->obj.rng;

	if (nr.dir.z == 0.0)
		return((hlist *)NULL);

	t = -nr.org.z / nr.dir.z;

	if (t < tolerance)
		return((hlist *)NULL);

	x = t * nr.dir.x + nr.org.x;
	y = t * nr.dir.y + nr.org.y;

	prod = x * x + y * y;
	if (prod > 1.0)
		return((hlist *)NULL);
	
	if (prod < rng->intradsqu)
		return((hlist *)NULL);

	fetch(hitlist);
	hitlist->t = t;
	hitlist->obj = o;
	hitlist->nxt = (hlist *)NULL;
	*last = hitlist;

	if (o->incsg) {
		fetch(hp);
		hp->t = t;
		hp->obj = o;
		hp->nxt = hitlist;
		return(hp);
	}

	return(hitlist);
}

/*
 * ringn
 *
 *	returns the normal to the ring o
 */
void
ringn(n, l, o)
	register vector	*n;
	vector	*l;
	object	*o;
{
	n->x = 0.0;
	n->y = 0.0;
	n->z = 1.0;
}

/*
 * ringc
 *
 *	return the color of the ring o at the point l
 *
 */
void
ringc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	int	w, h, indx;
	float	u, v;
	vector	loc;
	tiletxt	*tp;

	totexture(txt, loc, *l);

	loc.x /= o->td->nscales.x; 
	loc.y /= o->td->nscales.y;

	v = (1.0 - sqrt((double)(loc.x * loc.x + loc.y * loc.y))) / (1.0 - o->obj.rng->intrad);

	if (loc.x != 0.0 && loc.y != 0.0)
		normalise(loc);

	if (fabs(loc.x) > 1.0)
		loc.x = (loc.x < 0.0) ? -1.0 : 1.0;
	
	if (loc.y < 0.0)
		u = 1.0 - acos(loc.x) / (2 * M_PI);
	else
		u = acos(loc.x) / (2 * M_PI);

	tp = txt->u.t;

	w = v * tp->scalew;
	h = u * tp->scaleh;

	indx = (w % tp->pixw + (h % tp->pixh) * tp->pixw) * 3;

	pcol->r = (unsigned char)tp->map[indx] / 255.0;
	pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
	pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
}

/*
 * ringinit
 *
 *	initialise the function pointers and fields for a sphere object,
 * returning its pointer.
 */
void
ringinit(o, d)
	object	*o;
	details *d;
{
	ring	*rng;
	details	*ld;
	vector	cent, radii1, radii2;
	int	first;

	rng = o->obj.rng = (ring *)smalloc(sizeof(ring));

	cent.x = cent.y = cent.z = 0.0;
	first = 1;
	radii1.x = radii1.y = 1.0;
	radii2.x = 0.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			cent = d->u.v;
			break;
		case RADIUS:
			if (first) {
				radii2.x = radii2.y = d->u.f;
				radii1.x = radii1.y = d->u.f;
				first = 0;
			} else
				radii1.x = radii1.y = d->u.f;
			break;
		case RADII:
			if (first) {
				radii1.x = radii2.x = d->u.v.x;
				radii1.y = radii2.y = d->u.v.y;
				first = 0;
			} else {
				radii1.x = d->u.v.x;
				radii1.y = d->u.v.y;
			}
			break;
		default:
			warning("art: illegal field in ring ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	obj_scale(radii1.x, radii1.y, 1.0);

	obj_translate(cent.x, cent.y, cent.z);

	if (radii2.x != radii1.x) {
		rng->intrad = radii2.x / radii1.x;
		rng->intradsqu = (radii2.x * radii2.x) / (radii1.x * radii1.x);
	} else 
		rng->intrad = rng->intradsqu = 0.0;

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, -TOLERANCE, 1.0, 1.0, TOLERANCE);

	setattributes(o);

}

/*
 * ringtabinit
 *
 *	set the table of function pointers for the ring.
 */
ringtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[RING] = ringn;
	intersects[RING] = ringi;
	tilefuns[RING] = ringc;
	checkbbox[RING] = TRUE;
	selfshadowing[RING] = FALSE;
}
