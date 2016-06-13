#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * ellipsi
 *
 *	returns a list of intersection points for the ray r and ellipsoid o.
 *
 */
hlist *
ellipsi(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist			*hitlist, *hp;
	register float		a, b, c, d, t1, t2;
	ray			nr;

	transray(o, nr, *r);

	a = dprod(nr.dir, nr.dir);
	b = -dprod(nr.dir, nr.org);
	c = dprod(nr.org, nr.org) - 1.0;

	d = b * b - a * c;

	if (d < 0.0)
		return((hlist *)NULL);
	
	hitlist = (hlist *)NULL;

	d = sqrt((double)d);

	t1 = (b - d) / a;
	t2 = (b + d) / a;

	if (t1 >= tolerance) {
		fetch(hp);
		hitlist = hp;
		hp->t = t1;
		hp->obj = o;
		if (o->incsg) {
			fetch(hp);
			hitlist->nxt = hp;
			hp->t = t2;
			hp->obj = o;
		} 
		hp->nxt = (hlist *)NULL;
	} else if (t2 >= tolerance) {
		fetch(hp);
		hitlist = hp;
		hp->t = t2;
		hp->obj = o;
		hp->nxt = (hlist *)NULL;
	}

	*last = hp;

	return(hitlist);
}

/*
 * ellipsn
 *
 *	returns the normal to the ellipsoid o
 */
void
ellipsn(n, l, o)
	register vector		*n;
	register vector		*l;
	object			*o;
{
	toobject(o, *n, *l);
}

/*
 * ellipsc
 *
 *	return the color of an ellipse o at a the intersection point l.
 *
 */
void
ellipsc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	vector  loc;
	float   u, v;
	int     w, h, indx;
	tiletxt	*tp;

	totexture(txt, loc, *l);

	loc.x /= o->td->nscales.x; 
	loc.y /= o->td->nscales.y;
	loc.z /= o->td->nscales.z;
				 
	spheremap(&loc, &u, &v);

	tp = txt->u.t;

	w = u * tp->scalew;
	h = v * tp->scaleh;

	indx = (w % tp->pixw + (h % tp->pixh) * tp->pixw) * 3;

	pcol->r = (unsigned char)tp->map[indx] / 255.0;
	pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
	pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
}

/*
 * ellipsinit
 *
 *	initialise the function pointers and fields of an ellipsoid
 *
 */
void
ellipsinit(o, d)
	object	*o;
	details *d;
{
	details		*ld;
	vector		cent, radii;

	cent.x = cent.y = cent.z = 0.0;
	radii.x = radii.y = radii.z = 1.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			cent = d->u.v;
			break;
		case RADII:
			radii = d->u.v;
			break;
		case RADIUS:
			radii.x = radii.y = radii.z = d->u.f;
			break;
		default:
			warning("art: illegal field in ellipsoid ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (radii.x != 1.0 || radii.y != 1.0 || radii.z != 1.0)
		obj_scale(radii.x, radii.y, radii.z);

	if (cent.x != 0.0 || cent.y != 0.0 || cent.z != 0.0)
		obj_translate(cent.x, cent.y, cent.z);

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0);

	setattributes(o);
}

/*
 * ellipstabinit
 *
 *	set the table of function pointers for the ellipse.
 */
ellipstabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[ELLIPSOID] = ellipsn;
	intersects[ELLIPSOID] = ellipsi;
	tilefuns[ELLIPSOID] = ellipsc;
	checkbbox[ELLIPSOID] = TRUE;
	selfshadowing[ELLIPSOID] = FALSE;
}
