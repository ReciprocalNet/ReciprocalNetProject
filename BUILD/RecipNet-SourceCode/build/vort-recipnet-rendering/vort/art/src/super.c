#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"
#include "poly.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

extern double	power();

/*
 * ...CAME ACROSS A SCENE WHERE THE SUPERI() FUNCTION GENERATES A
 * FP ERROR: INVALID
 * THIS IS NOT CAUSED BY THE FUNCTION, IT'S THE OPTIMIZER OF MICROSOFT
 * C 5.1 THAT GENERATES THIS. IN CASE IT HAPPENS YOU MUST DISABLE THE
 * OPTIMIZATION WITH -OD.
 *
 *			ALBERTO
 */

/*
 * max allowed value for diff (in rescaled space)
 */
#define MAXDIFF 2.0

float suptolerance;

/*
 * bounding box for all superquadrics
 */
static bbox sqbox = {
	{ 1.0, 1.0, 1.0 },
	{ -1.0, -1.0, -1.0 }
};

/*
 * superi
 *
 *	returns the first hit of the ray r on the superquadric o, unless the
 * superquadric is in a csg model in which case a list of intersection points
 * for the ray r is returned.
 */
hlist *
superi(r, o, last)
	ray		*r;
	object		*o;
	hlist		**last;
{
	hlist		*hitlist, *hp;
	float		t1, t2;
	ray		nr;
	superquadric	*sprq;
	int		ord;
	float		oldt, diff, mdiff;
	float		val, oldval;

	transray(o, nr, *r);

	if (!inbbox(&nr, &sqbox, &t1, &t2))
		return((hlist *)NULL);

	hitlist = (hlist *)NULL;

	sprq = o->obj.spq;

	ord = sprq->ord;

	/*
	 * the dprod gives an estimate of the superquadric scaling, assumed
	 * that the nr.dir before the transformation to object space is
	 * normalized. (this assumption is true)
	 */
	mdiff = MAXDIFF/sqrt(dprod(nr.dir, nr.dir));

	if (fabs(t1) < mdiff) {  /* compute val only if we are near the surface */

		val = power(fabs(nr.org.x), ord) + power(fabs(nr.org.y), ord)
			+ power(fabs(nr.org.z), ord) - 1.0;

		if (val <= tolerance)	/* we are on the surface */
			oldt = t1 + tolerance;
		else
			oldt = t1 - tolerance;	/* we are heading towards it */
		}
	else
		oldt = t1 - tolerance;	/* we are far (and thus heading towards it) */

	val = power(fabs(nr.org.x + nr.dir.x * oldt), ord)
		+ power(fabs(nr.org.y + nr.dir.y * oldt), ord)
		+ power(fabs(nr.org.z + nr.dir.z * oldt), ord)
		- 1.0;

	/* now adjust the tolerance */
	suptolerance = t1 * TOLERANCE / 50.0;	 /* yes, but why 50? */

	if (suptolerance < TOLERANCE)	 /* make sure it's not too small */
		suptolerance = TOLERANCE;

	while (val > suptolerance) {

		oldval = val;

		val = power(fabs(nr.org.x + nr.dir.x * t1), ord)
			+ power(fabs(nr.org.y + nr.dir.y * t1), ord)
			+ power(fabs(nr.org.z + nr.dir.z * t1), ord)
			- 1.0;

		if (val >= oldval) {
			t1 = 0.0;
			break;
		}

		diff = (val * (t1 - oldt)) / (val - oldval);

		if (fabs(diff) > mdiff ) {
			t1 = 0.0;
			break;
		}

		oldt = t1;

		t1 -= diff;
	}

	if (o->incsg && t2 > tolerance) {
		oldt = t2 + tolerance;

		val = power(fabs(nr.org.x + nr.dir.x * oldt), ord)
			+ power(fabs(nr.org.y + nr.dir.y * oldt), ord)
			+ power(fabs(nr.org.z + nr.dir.z * oldt), ord)
			- 1.0;

		while (val > suptolerance) {
			oldval = val;
			val = power(fabs(nr.org.x + nr.dir.x * t2), ord)
				+ power(fabs(nr.org.y + nr.dir.y * t2), ord)
				+ power(fabs(nr.org.z + nr.dir.z * t2), ord)
				- 1.0;

			if (val >= oldval) {
				t2 = 0.0;
				break;
			}

			diff = (val * (t2 - oldt)) / (val - oldval);

			if (fabs(diff) > mdiff ) {
				t2 = 0.0;
				break;
			}

			oldt = t2;

			t2 -= diff;
		}

		if (t2 > tolerance) {
			fetch(hitlist);
			hitlist->obj = o;
			hitlist->t = t2;
			hitlist->nxt = (hlist *)NULL;
			*last = hitlist;
		}
	}

	if (t1 > tolerance) {
		fetch(hp);
		hp->obj = o;
		hp->t = t1;
		hp->nxt = hitlist;
		hitlist = hp;
		if (hp->nxt == (hlist *)NULL)
			*last = hp;
	}

	return(hitlist);
}

/*
 * supern
 *
 *	returns the normal vector to a point on a superquadric surface
 */
void
supern(n, l, o)
	vector *n, *l;
	object *o;
{
	int	ord;
	vector	loc;

	ord = o->obj.spq->ord;

	toobject(o, loc, *l);

	n->x = (loc.x > 0) ? power(loc.x, ord - 1) : -power(-loc.x, ord - 1);
	n->y = (loc.y > 0) ? power(loc.y, ord - 1) : -power(-loc.y, ord - 1);
	n->z = (loc.z > 0) ? power(loc.z, ord - 1) : -power(-loc.z, ord - 1);
}

/*
 * superc
 *
 *	returns the colour of a superquadric at the position loc.
 */
void
superc(o, txt, l, n, pcol, type)
	object	*o;
	texture *txt;
	vector	*l, *n;
	pixel	*pcol;
	int	type;
{
	float	u, v;
	int	w, h, indx;
	vector	loc;
	tiletxt *tp;

	totexture(txt, loc, *l);

	loc.x /= o->td->nscales.x;
	loc.y /= o->td->nscales.y;
	loc.z /= o->td->nscales.z;

	normalise(loc);

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
 * superinit
 *
 *	initialise the function pointers and fields for a superquadric object,
 *	returning its pointer.
 */
void
superinit(o, d)
	object	*o;
	details *d;
{
	superquadric	*sprq;
	int		first;
	vector		topcnr, botcnr, cent;
	float		xlen, ylen, zlen;
	details 	*ld;

	sprq = o->obj.spq = (superquadric *)smalloc(sizeof(superquadric));

	first = 1;

	botcnr.x = botcnr.y = botcnr.z = -1.0;
	topcnr.x = topcnr.y = topcnr.z = 1.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case VERTEX:
			if (first) {
				botcnr = d->u.v;
				first = 0;
			} else
				topcnr = d->u.v;
			break;
		case ORDER:
			sprq->ord = d->u.i;
			break;
		default:
			warning("art: illegal field in superquadric ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	xlen = (topcnr.x - botcnr.x) / 2;
	ylen = (topcnr.y - botcnr.y) / 2;
	zlen = (topcnr.z - botcnr.z) / 2;

	cent.x = (topcnr.x + botcnr.x) / 2.0;
	cent.y = (topcnr.y + botcnr.y) / 2.0;
	cent.z = (topcnr.z + botcnr.z) / 2.0;

	obj_scale(fabs(xlen), fabs(ylen), fabs(zlen));

	obj_translate(cent.x, cent.y, cent.z);

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0);

	setattributes(o);
}

/*
 * supertabinit
 *
 *	set the table of function pointers for the superquadric surface.
 */
supertabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[SUPERQUADRIC] = supern;
	intersects[SUPERQUADRIC] = superi;
	tilefuns[SUPERQUADRIC] = superc;
	checkbbox[SUPERQUADRIC] = TRUE;
	selfshadowing[SUPERQUADRIC] = FALSE;
}
