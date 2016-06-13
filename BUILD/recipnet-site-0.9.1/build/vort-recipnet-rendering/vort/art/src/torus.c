#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"
#include "poly.h"

extern attr	*astackp;
extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

extern double	modrf();


/*
 * torusi
 *
 *	returns the closest hit for the ray r and the torus to.
 * the equation used is:
 *	(x^2 + y^2 + z^2 + 1.0 - r^2)^2 - 4(x^2 + y^2) = 0.0.
 * where r is between 0.0 and 1.0.
 */
hlist *
torusi(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist		*hitlist, *hp, *lp;
	int		np, numroots, nroots, atmin, atinf, rptcount[5];
	spoly		sseq[5];
	double		x2[3], y2[3], z2[3], roots[4];
	ray		nr;
	bbox		bb;
	float		baset, pt1, pt2;
	register torus	*to;
	register int	its, i, j;
	register float	t, min, max;

	bb.min[X] = bb.min[Y] = bb.min[Z] = -2.0;
	bb.max[X] = bb.max[Y] = bb.max[Z] = 2.0;

	transray(o, nr, *r);

	if (!inbbox(&nr, &bb, &pt1, &pt2))
		return((hlist *)NULL);

	hitlist = (hlist *)NULL;

	if (pt1 > 2.0 + tolerance) {
		baset = pt1 - tolerance;
		nr.org.x += baset * nr.dir.x;
		nr.org.y += baset * nr.dir.y;
		nr.org.z += baset * nr.dir.z;
	} else
		baset = 0.0;

	to = o->obj.trs;

	x2[0] = nr.org.x * nr.org.x;
	x2[1] = 2.0 * nr.org.x * nr.dir.x;
	x2[2] = nr.dir.x * nr.dir.x;

	y2[0] = nr.org.y * nr.org.y;
	y2[1] = 2.0 * nr.org.y * nr.dir.y;
	y2[2] = nr.dir.y * nr.dir.y;

	z2[0] = nr.org.z * nr.org.z;
	z2[1] = 2.0 * nr.org.z * nr.dir.z;
	z2[2] = nr.dir.z * nr.dir.z;

	sseq[0].coef[4] = sseq[0].coef[3] = 0.0;
	sseq[0].coef[2] = -4.0 * (x2[2] + y2[2]);
	sseq[0].coef[1] = -4.0 * (x2[1] + y2[1]);
	sseq[0].coef[0] = -4.0 * (x2[0] + y2[0]) + to->cnst * to->cnst;

	for (i = 0; i <= 2; i++) {
		for (j = 0; j <= 2; j++) 
			sseq[0].coef[i + j] += x2[i] * x2[j] + y2[i] * y2[j] + z2[i] * z2[j] + 2 * (x2[i] * y2[j] + y2[i] * z2[j] + x2[i] * z2[j]);
		sseq[0].coef[i] += 2 * to->cnst * (x2[i] + y2[i] + z2[i]);
	}

	np = buildsturm(4, sseq);

	atmin = evalat0(np, sseq);
	atinf = evalatb(np, sseq, 0.0);

	nroots = atmin - atinf;

	if (nroots == 0) 
		return((hlist *)NULL);

	atmin = evalata(np, sseq, tolerance);

	nroots = atmin - atinf;

	if (nroots == 0)
		return((hlist *)NULL);

	min = t = tolerance;
	max = min + 2.0;
	for (its = 0; its < MAXP2; its++) {
		atinf = evalatb(np, sseq, max);

		numroots = atmin - atinf;

		if (o->incsg == FALSE) {
			if (numroots > 1)
				break;
		} else if (numroots == nroots)
			break;

		t = max;
		max *= 2.0;
	}

	if (nroots == 1 || o->incsg == FALSE) {
		sbisect(np, sseq, min, max, atmin, atinf, roots);

		fetch(hitlist);
		hitlist->t = roots[0] + baset;
		hitlist->obj = o;
		hitlist->nxt = (hlist *)NULL;
		*last = hitlist;
	} else {
		rptcount[1] = rptcount[2] = rptcount[3] = 0;

		csgsbisect(np, sseq, min, max, atmin, atinf, roots, rptcount);

		fetch(hitlist);
		lp = hp = hitlist;
		for (i = 0; i < nroots; i++) {
			for (j = 0; j < rptcount[i]; j++) {
				hp->t = roots[i] + baset;
				hp->obj = o;
				fetch(hp->nxt);
				lp = hp;
				hp = hp->nxt;
			}
		}
		release(hp);
		lp->nxt = (hlist *)NULL;
		*last = lp;
	}

	return(hitlist);
}

/*
 * torusn
 *
 *	returns the normal to the torus to
 */
void
torusn(n, l, o)
	register vector	*n;
	register vector	*l;
	register object	*o;
{
	register float	fact;
	register torus	*to;
	vector		loc;

	to = o->obj.trs;

	toobject(o, loc, *l);

	fact = loc.x * loc.x + loc.y * loc.y + loc.z * loc.z + to->cnst;

	n->x = loc.x * (fact - 2.0);
	n->y = loc.y * (fact - 2.0);
	n->z = loc.z * fact;
}

/*
 * torusc
 *
 *	returns the colour of the torus o at the point l
 */
void
torusc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	int	w, h, indx;
	float	u, v, fact;
	vector	loc, tmp;
	tiletxt	*tp;

	totexture(txt, loc, *l);

	loc.x /= o->td->nscales.x; 
	loc.y /= o->td->nscales.y;
	loc.z /= o->td->nscales.z;
					 
	tmp = loc;

	tmp.z = 0.0;
	normalise(tmp);

	if (fabs(tmp.x) > 1.0)
		tmp.x = (tmp.x < 0.0) ? -1.0 : 1.0;
	
	if (tmp.y < 0.0)
		u = 1.0 - acos(tmp.x) / (2 * M_PI);
	else
		u = acos(tmp.x) / (2 * M_PI);

	loc.x = loc.x * tmp.x + loc.y * tmp.y;	/* align n->x with x */

	fact = loc.x * loc.x + loc.z * loc.z + o->obj.trs->cnst;

	loc.x = loc.x * (fact - 2.0);
	loc.y = 0.0;
	loc.z = loc.z * fact;

	normalise(loc);

	if (fabs(loc.x) > 1.0)
		loc.x = (loc.x < 0.0) ? -1.0 : 1.0;

	if (loc.z < 0.0)
		v = 1.0 - acos(loc.x) / (2 * M_PI);
	else
		v = acos(loc.x) / (2 * M_PI);

	tp = txt->u.t;

	w = u * tp->scalew;
	h = v * tp->scaleh;

	indx = (w % tp->pixw + (h % tp->pixh) * tp->pixw) * 3;

	pcol->r = (unsigned char)tp->map[indx] / 255.0;
	pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
	pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
}

/*
 * torusinit
 *
 *	initialise the function pointers and fields of a torus object
 *
 */
void
torusinit(o, d)
	object	*o;
	details *d;
{
	torus	*to;
	float	torad, ringrad, radius;
	vector	cent;
	details	*ld;
	int	first;

	to = o->obj.trs = (torus *)smalloc(sizeof(torus));
	cent.x = cent.y = cent.z = 0.0;

	first = 1;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			cent = d->u.v;
			break;
		case RADII:
			torad = d->u.v.x;
			ringrad = d->u.v.y;
			break;
		case RADIUS:
			if (first) {
				ringrad = d->u.f;
				first = 0;
			} else
				torad = d->u.f;
			break;
		default:
			warning("art: illegal field in torus ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	obj_scale(torad, torad, torad);

	obj_translate(cent.x, cent.y, cent.z);

	to->cnst = 1.0 - sqr(ringrad / torad);

	radius = 1.0 + ringrad / torad;

	calctransforms(mstackp);

	makebbox(o, -radius, -radius, -radius, radius, radius, radius);

	setattributes(o);
}

/*
 * torustabinit
 *
 *	set the table of function pointers for the torus.
 */
torustabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[TORUS] = torusn;
	intersects[TORUS] = torusi;
	tilefuns[TORUS] = torusc;
	checkbbox[TORUS] = TRUE;
	selfshadowing[TORUS] = TRUE;
}
