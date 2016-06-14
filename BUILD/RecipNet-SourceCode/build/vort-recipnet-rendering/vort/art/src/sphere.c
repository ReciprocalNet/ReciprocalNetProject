#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * spherei
 *
 *	returns a list of intersection points for the ray r and sphere o.
 */
hlist *
spherei(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	vector	c;
	sphere	*sp;
	hlist	*hitlist, *hp;
	real	a, b, d;

	sp = o->obj.sph;

	vsub(c, r->org, sp->orig);

	b = -dprod(c, r->dir);
	a = (dprod(c, c) - sp->radsqu);

	if ((d = b * b - a) < 0.0 || (b < 0.0 && a > 0.0))
		return((hlist *)NULL);

	d = sqrt((double)d);

	fetch(hp);
	hitlist = hp;
	hp->t = b - d;

	if (hp->t > tolerance) {
		hitlist->obj = o;
		if (o->incsg) {
			fetch(hp);
			hitlist->nxt = hp;
			hp->t = b + d;
		}
	} else { 
		hp->t = b + d;
		if (hp->t < tolerance) {
			release(hp);
			return((hlist *)NULL);
		}
	}

	hp->obj = o;
	hp->nxt = (hlist *)NULL;
	*last = hp;

	return(hitlist);
}

/*
 * spheren
 *
 *	returns the normal to the sphere s
 */
void
spheren(n, l, o)
	register vector	*n;
	register vector	*l;
	register object	*o;
{
	toobject(o, *n, *l);
}

/*
 * spherec
 *
 *	return the color of a sphere o at a the intersection point l.
 *
 */
void
spherec(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	pixel	*pcol;
	int	type;
{
	vector	loc;
	float	u, v;
	int	w, h, indx;
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
 * sphereinit
 *
 *	initialise the function pointers and fields for a sphere object,
 * returning its pointer.
 */
void
sphereinit(o, d)
	object	*o;
	details *d;
{
	details	*ld;
	vector	cent;
	float	radius;

	cent.x = cent.y = cent.z = 0.0;		/* default sphere */
	radius = 1.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			cent = d->u.v;
			break;
		case RADIUS:
			if ((radius = d->u.f) <= 0.0)
				fatal("art: sphere radius must be > 0.\n");
			break;
		default:
			warning("art: illegal field in sphere ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (radius != 1.0)
		obj_scale(radius, radius, radius);

	if (cent.x != 0.0 || cent.y != 0.0 || cent.z != 0.0)
		obj_translate(cent.x, cent.y, cent.z);

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0);

	setattributes(o);

	o->obj.sph = (sphere *)smalloc(sizeof(sphere));

	cent.x = cent.y = cent.z = 0.0;
	vmmult(o->obj.sph->orig, cent, mstackp->d.obj2ray);

	o->obj.sph->radsqu = mstackp->d.maxscale * mstackp->d.maxscale;
}

/*
 * spheretabinit
 *
 *	set the table of function pointers for the sphere.
 */
spheretabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[SPHERE] = spheren;
	intersects[SPHERE] = spherei;
	tilefuns[SPHERE] = spherec;
	checkbbox[SPHERE] = FALSE;
	selfshadowing[SPHERE] = FALSE;
}
