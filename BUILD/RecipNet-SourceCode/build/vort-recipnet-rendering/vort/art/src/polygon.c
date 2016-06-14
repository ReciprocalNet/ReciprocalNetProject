#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern attr	*astackp;
extern real	tolerance;

extern hlist	*fhlist;
extern int 	linecount;

/*
 * trianglei
 *
 *	intersect a triangle, we calculate the barycentric coordinates using
 * the coefficients in the matrix we pre calculated in the initialisation
 * routine. This owes a bit to Didier Badouel from his article in Graphics 
 * Gems.
 */
hlist *
trianglei(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	polygon	*p;
	mat3x3	*m;
	hlist	*hp, *hitlist;
	real	t, pu, pv, a1, a2;

	p = o->obj.ply;

	t = dprod(p->n, r->dir);

	if (t == 0.0 || (t > 0.0 && p->backfacing))
		return((hlist *)NULL);

	t = -(dprod(p->n, r->org) + p->cnst) / t;

	if (t < tolerance)
		return((hlist *)NULL);

	switch (p->axis) {
	case X:
		pu = r->org.y + t * r->dir.y;
		if (pu < o->bb.min[Y] || pu > o->bb.max[Y])
			return((hlist *)NULL);
		pv = r->org.z + t * r->dir.z;
		if (pv < o->bb.min[Z] || pv > o->bb.max[Z])
			return((hlist *)NULL);
		break;
	case Y:
		pu = r->org.x + t * r->dir.x;
		if (pu < o->bb.min[X] || pu > o->bb.max[X])
			return((hlist *)NULL);
		pv = r->org.z + t * r->dir.z;
		if (pv < o->bb.min[Z] || pv > o->bb.max[Z])
			return((hlist *)NULL);
		break;
	case Z:
		pu = r->org.x + t * r->dir.x;
		if (pu < o->bb.min[X] || pu > o->bb.max[X])
			return((hlist *)NULL);
		pv = r->org.y + t * r->dir.y;
		if (pv < o->bb.min[Y] || pv > o->bb.max[Y])
			return((hlist *)NULL);
		break;
	default:
		fatal("art: bad axis in inttri.\n");
	}

	m = p->u.mat;

	a1 = pv * (*m)[0][0] - pu * (*m)[0][1] + (*m)[0][2];
	if (a1 < 0.0 || a1 > 1.0)
		return((hlist *)NULL);

	a2 = pv * (*m)[1][0] - pu * (*m)[1][1] + (*m)[1][2];
	if (a2 < 0.0 || a1 + a2 > 1.0)
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
		hp->nxt = (hlist *)NULL;
		hitlist->nxt = hp;
		*last = hp;
	}

	return(hitlist);
}

/*
 * polyi
 *
 *	intersect a polygon. We use a do ... while here as sometimes
 * on segmented machines the original for loop construct wouldn't work.
 */
hlist *
polyi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	polygon	*p;
	int	crosses;
	real	t, m, pu, pv;
	uvvec	*cp, *np;
	hlist	*hp, *hitlist;

	p = o->obj.ply;

	t = dprod(p->n, r->dir);

	if (t == 0.0 || (t > 0.0 && p->backfacing))
		return((hlist *)NULL);

	t = -(dprod(p->n, r->org) + p->cnst) / t;

	if (t < tolerance)
		return((hlist *)NULL);

	switch (p->axis) {
	case X:
		pu = r->org.y + t * r->dir.y;
		if (pu < o->bb.min[Y] || pu > o->bb.max[Y])
			return((hlist *)NULL);
		pv = r->org.z + t * r->dir.z;
		if (pv < o->bb.min[Z] || pv > o->bb.max[Z])
			return((hlist *)NULL);
		break;
	case Y:
		pu = r->org.x + t * r->dir.x;
		if (pu < o->bb.min[X] || pu > o->bb.max[X])
			return((hlist *)NULL);
		pv = r->org.z + t * r->dir.z;
		if (pv < o->bb.min[Z] || pv > o->bb.max[Z])
			return((hlist *)NULL);
		break;
	case Z:
		pu = r->org.x + t * r->dir.x;
		if (pu < o->bb.min[X] || pu > o->bb.max[X])
			return((hlist *)NULL);
		pv = r->org.y + t * r->dir.y;
		if (pv < o->bb.min[Y] || pv > o->bb.max[Y])
			return((hlist *)NULL);
		break;
	default:
		fatal("art: bad axis in polyi.\n");
	}

	crosses = 0;

	cp = p->u.verts;
	np = &p->u.verts[p->nsides - 1];

	do {
		/*
		 * If we are both above, or both below the intersection
		 * point, go onto the next one.
		 */

		if (cp->v < pv) {
			if (np->v < pv) {
				cp = np--;
				continue;
			}
		} else {
			if (np->v >= pv) {
				cp = np--;
				continue;
			}
		}

		/*
		 * Find out if we have crossed in the negative.
		 */
		if (cp->u < pu) {
			if (np->u < pu) {	/* crossed on left */
				crosses++;
				cp = np--;
				continue;
			}
		} else if (np->u >= pu) {	/* both to right */
			cp = np--;
			continue;
		}

		/* 
		 * We calculate the u at the intersection of the
		 * half-ray and the edge and test it against pu. 
		 */

		m = (np->u - cp->u) / (np->v - cp->v);
		if (((np->u - pu) - m * (np->v - pv)) < 0.0)
			crosses++;

		cp = np--;

	} while (cp != p->u.verts);

	if (!(crosses & 1))
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
		hp->nxt = (hlist *)NULL;
		hitlist->nxt = hp;
		*last = hp;
	}

	return(hitlist);
}

/*
 * polyn
 *
 *	returns the normal to a polygon.
 */
void
polyn(n, l, o, indx)
	vector	*n, *l;
	object	*o;
	int	indx;
{
	polygon	*poly;
	vector	col;

	poly = o->obj.ply;

	if (poly->norms != (vector *)NULL) {
		interp(poly, &poly->on, poly->norms, l, n);
		normalise(*n);
	} else {
		n->x = poly->on.x;
		n->y = poly->on.y;
		n->z = poly->on.z;
	}
}

/*
 * polyc
 *
 *	returns the colour of polygon o at point l
 */
void
polyc(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	int		indx, w, h;
	vector		loc;
	transdata	*td;
	tiletxt		*tp;

	totexture(txt, loc, *l);

	td = o->td;

	loc.x /= td->nscales.x;
	loc.y /= td->nscales.y;
	
	tp = txt->u.t;

	w = fabs(loc.x) * tp->scalew;
	h = fabs(loc.y) * tp->scaleh;

	if (loc.x > 0.0)
		w = w % tp->pixw;
	else
		w = tp->pixw - 1 - (w % tp->pixw);

	if (loc.y > 0.0)
		h = h % tp->pixh;
	else
		h = tp->pixh - 1 - (h % tp->pixh);

	indx = (w + h * tp->pixw) * 3;

	pcol->r = (unsigned char)tp->map[indx] / 255.0;
	pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
	pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
}

/*
 * polygoninit
 *
 *	initialise the function pointers and fields for a polygon
 */
void
polygoninit(o, d)
	object	*o;
	details *d;
{
	polygon		*pg;
	int		axis, i, count, ccount, ncount, dcount;
	matrix		m, tmp;
	vector		v, v1, v2, ovt[MAXVERTS], vt[MAXVERTS], norms[4];
	vector		xv, yv, zv;
	float		maxx, maxy, maxz, minx, miny, minz;
	float		vx, vz, radius, area, tolerance;
	vector		colours[4];
	char		buf[MESLEN];
	details		*ld, *d1, *ld1, *headd;
	surface		*news;

	calctransforms(mstackp);

	/*
	 * count vertices and reverse vertex list.
	 */
	count = 0;
	headd = (details *)NULL;

	while (d != (details *)NULL) {
		ld = d->nxt;

		d->nxt = headd;
		headd = d;

		count++;
		d = ld;
	}

	d = headd;

	pg = o->obj.ply = (polygon *)smalloc(sizeof(polygon));

	count = 0;
	ncount = 0;
	ccount = 0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case VERTEX:
					/* transform vertex into ray space */
			ovt[count] = d->u.v;
			vmmult(vt[count], d->u.v, mstackp->d.obj2ray);
			count++;
			break;
		case COMPLEXVERTEX:
			if (ccount == 4 || ncount == 4) {
				if (count == 4)
					warning("art: can't interpolate across a polygon with more than 4 vertices.\n");
				if (ccount != 0)
					ccount = 1;
				ncount = 1;
			}

			dcount = 0;
			for (d1 = d->u.det; d1 != (details *)NULL; d1 = d1->nxt)
				dcount++;
				
			for (d1 = d->u.det; d1 != (details *)NULL; d1 = ld1) {
				if (d1->type == VERTEX) {
					if (dcount > 1) {	/* actual vertex will be last in list */
						norms[ncount] = d1->u.v;
						normalise(norms[ncount]);
						ncount++;
					} else {
						ovt[count] = d1->u.v;
						vmmult(vt[count], d1->u.v, mstackp->d.obj2ray);
					}
				} else {
					colours[ccount] = d1->u.v;
					ccount++;
				}
				dcount--;
				ld1 = d1->nxt;
				free(d1);
			}

			count++;
			break;
		default:
			warning("art: illegal field in polygon ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	pg->nsides = count;

	pg->backfacing = astackp->d.options & BACKFACING;

	vsub(v1, vt[1], vt[0]);
	vsub(v2, vt[2], vt[1]);

	xprod(pg->n, v1, v2);

	pg->cnst = -dprod(pg->n, vt[0]);

	if (fabs(pg->n.x) > fabs(pg->n.y))
		if (fabs(pg->n.x) > fabs(pg->n.z))
			pg->axis = X;
		else
			pg->axis = Z;
	else
		if (fabs(pg->n.y) > fabs(pg->n.z))
			pg->axis = Y;
		else
			pg->axis = Z;

	vsub(v1, ovt[1], ovt[0]);
	vsub(v2, ovt[2], ovt[1]);

	xprod(pg->on, v1, v2);

	if (pg->on.x == 0.0 && pg->on.y == 0.0 && pg->on.z == 0.0) {
		sprintf(buf, "invalid polygon in file before line %d\n", linecount);
		warning(buf);
		o->type = NULL_OBJ;

		return;
	} else
		normalise(pg->on);

	if (ncount != 0 && count <= 4) {
		pg->norms = (vector *)scalloc(sizeof(vector), (unsigned long)ncount);
		pg->norms[0] = norms[0];
		pg->norms[1] = norms[1];
		pg->norms[2] = norms[2];
		if (ncount == 4) {
			pg->norms[3] = norms[3];
		}
	} else {
		pg->norms = (vector *)NULL;
	}

	if (ccount != 0 && count <= 4) {
		pg->colours = (vector *)scalloc(sizeof(vector), (unsigned long)ccount);
		pg->colours[0] = colours[0];
		pg->colours[1] = colours[1];
		pg->colours[2] = colours[2];
		if (ccount == 4) {
			pg->colours[3] = colours[3];
		}
	} else {
		pg->colours = (vector *)NULL;
	}

	if (count > 3) {
		pg->u.verts = (uvvec *)scalloc(sizeof(uvvec), (unsigned long)count);

		switch (pg->axis) {
		case X:
			for (i = 0; i != count; i++) {
				pg->u.verts[i].u = vt[i].y;
				pg->u.verts[i].v = vt[i].z;
			}
			break;
		case Y:
			for (i = 0; i != count; i++) {
				pg->u.verts[i].u = vt[i].x;
				pg->u.verts[i].v = vt[i].z;
			}
			break;
		case Z:
			for (i = 0; i != count; i++) {
				pg->u.verts[i].u = vt[i].x;
				pg->u.verts[i].v = vt[i].y;
			}
			break;
		default:
			fatal("art: bad axis in polygoninit.\n");
		}
	} else {
		/*
		 * as pg->n isn't normalised the magnitude of the
		 * normal in the part of 3 Space we ignore represents
		 * the area of the projected triangle. Dividing through
		 * by the component takes care of sign trouble we could
		 * have in the intersection test so we don't worry about
		 * taking the magnitude...
		 */
		switch (pg->axis) {
		case X:
			area = pg->n.x;
			m[0][0] = (vt[0].y - vt[2].y) / area;
			m[1][0] = (vt[1].y - vt[0].y) / area;
			m[2][0] = (vt[2].y - vt[1].y) / area;

			m[0][1] = (vt[0].z - vt[2].z) / area;
			m[1][1] = (vt[1].z - vt[0].z) / area;
			m[2][1] = (vt[2].z - vt[1].z) / area;

			m[0][2] = vt[2].y * m[0][1] - vt[2].z * m[0][0];
			m[1][2] = vt[0].y * m[1][1] - vt[0].z * m[1][0];
			m[2][2] = vt[1].y * m[2][1] - vt[1].z * m[2][0];
			break;
		case Y:
			area = pg->n.y;
			m[0][0] = (vt[2].x - vt[0].x) / area;
			m[1][0] = (vt[0].x - vt[1].x) / area;
			m[2][0] = (vt[1].x - vt[2].x) / area;

			m[0][1] = (vt[2].z - vt[0].z) / area;
			m[1][1] = (vt[0].z - vt[1].z) / area;
			m[2][1] = (vt[1].z - vt[2].z) / area;

			m[0][2] = vt[2].x * m[0][1] - vt[2].z * m[0][0];
			m[1][2] = vt[0].x * m[1][1] - vt[0].z * m[1][0];
			m[2][2] = vt[1].x * m[2][1] - vt[1].z * m[2][0];
			break;
		case Z:
			area = pg->n.z;
			m[0][0] = (vt[0].x - vt[2].x) / area;
			m[1][0] = (vt[1].x - vt[0].x) / area;
			m[2][0] = (vt[2].x - vt[1].x) / area;

			m[0][1] = (vt[0].y - vt[2].y) / area;
			m[1][1] = (vt[1].y - vt[0].y) / area;
			m[2][1] = (vt[2].y - vt[1].y) / area;

			m[0][2] = vt[2].x * m[0][1] - vt[2].y * m[0][0];
			m[1][2] = vt[0].x * m[1][1] - vt[0].y * m[1][0];
			m[2][2] = vt[1].x * m[2][1] - vt[1].y * m[2][0];
			break;
		default:
			fatal("art: bad axis in polygoninit.\n");
		}

		pg->u.mat = (mat3x3 *)smalloc(sizeof(mat3x3));

		cp3x3((*pg->u.mat), m);

					/* flag special case */
		o->type = TRIANGLE;
	}

				/* transform to "canonical" form */
	if (astackp->d.txtlist != (texture *)NULL) {

		vsub(xv, ovt[1], ovt[0]);
		vsub(v1, ovt[2], ovt[1]);

		xprod(zv, xv, v1);

		normalise(xv);

		normalise(zv);

		xprod(yv, xv, zv);

		mident4(m);

		m[0][0] = xv.x;
		m[1][0] = xv.y;
		m[2][0] = xv.z;

		m[0][1] = yv.x;
		m[1][1] = yv.y;
		m[2][1] = yv.z;

		m[0][2] = zv.x;
		m[1][2] = zv.y;
		m[2][2] = zv.z;

		v3x3mult(v, ovt[0], m);
		minx = maxx = v.x;
		miny = maxy = v.y;
		for (i = 1; i != count; i++) {
			v3x3mult(v, ovt[i], m);
			minmax(minx, maxx, v.x);
			minmax(miny, maxy, v.y);
		}

		obj_scale(maxx - minx, maxy - miny, 1.0);

		mcpy4(tmp, mstackp->d.om);
		mmult4(mstackp->d.om, m, tmp);

		obj_translate(ovt[0].x, ovt[0].y, ovt[0].z);

		pg->on.x = 0.0;
		pg->on.y = 0.0;
		pg->on.z = 1.0;

		minx = maxx = vt[0].x;
		miny = maxy = vt[0].y;
		minz = maxz = vt[0].z;
		for (i = 1; i != count; i++) {
			minmax(minx, maxx, vt[i].x);
			minmax(miny, maxy, vt[i].y);
			minmax(minz, maxz, vt[i].z);
		}

		radius = 1.0;

	} else {

		minx = maxx = vt[0].x;
		miny = maxy = vt[0].y;
		minz = maxz = vt[0].z;
		for (i = 1; i != count; i++) {
			minmax(minx, maxx, vt[i].x);
			minmax(miny, maxy, vt[i].y);
			minmax(minz, maxz, vt[i].z);
		}

		radius = sqrt(sqr(maxx - minx) + sqr(maxy - miny) + sqr(maxz - minz));
	}

	tolerance = radius * TOLERANCE / 2.0;

	o->bb.min[X] = minx - tolerance;
	o->bb.min[Y] = miny - tolerance;
	o->bb.min[Z] = minz - tolerance;

	o->bb.max[X] = maxx + tolerance;
	o->bb.max[Y] = maxy + tolerance;
	o->bb.max[Z] = maxz + tolerance;

	calctransforms(mstackp);

	setattributes(o);

	/*
	 * if this has happened, we have a polygon with more
	 * than foursides and colour interpolation expected,
	 * we set the colour of the polygon according to the first
	 * vertex.
	 */
	if (ccount != 0 && count >= 4) {
		news = (surface *)smalloc(sizeof(surface));
		*news = *o->s;
		o->s = news;
		o->s->c.r = colours[0].x;
		o->s->c.g = colours[0].y;
		o->s->c.b = colours[0].z;
	}
}

/*
 * polytabinit
 *
 *	set the table of function pointers for the polygon.
 */
polytabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[POLYGON] = polyn;
	intersects[POLYGON] = polyi;
	tilefuns[POLYGON] = polyc;
	checkbbox[POLYGON] = FALSE;
	selfshadowing[POLYGON] = FALSE;

	normals[TRIANGLE] = polyn;
	intersects[TRIANGLE] = trianglei;
	tilefuns[TRIANGLE] = polyc;
	checkbbox[TRIANGLE] = FALSE;
	selfshadowing[TRIANGLE] = FALSE;
}
