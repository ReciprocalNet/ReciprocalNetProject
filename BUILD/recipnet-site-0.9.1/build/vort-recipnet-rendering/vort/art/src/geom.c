#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern attr	*astackp;
extern mats	*mstackp;
extern real	tolerance;

extern hlist	*fhlist;

extern matrix	trans;

extern symbol	*findsym(), *insertsym();

/*
 * geometry file list
 */
static symbol	*geoms = (symbol *)NULL;
static symbol	*colours = (symbol *)NULL;
static symbol	*normals = (symbol *)NULL;
static symbol	*vcolours = (symbol *)NULL;
static symbol	*vnormals = (symbol *)NULL;

/*
 * ray details
 */
static vector	dir, org;
static real	orgu, orgv, diru, dirv;

/*
 * ray box dimensions
 */
static float	minu, maxu, minv, maxv;

/*
 * normals for the polygons
 */
static vector	*fnorms;

/*
 * polygon object
 */
static object	*obj;

/*
 * hitlist ptr for this object
 */
static hlist	*hitlist;

/*
 * options
 */
static int	backfacing;

extern int	shadow_rays;

/*
 * addhit
 *
 *	add a hit to the hitlist
 */
static
addhit(t, indx, last)
	register float	t;
	register int	indx;
	register hlist	**last;
{
	register hlist	*lp, *tp, *hp;

	lp = (hlist *)NULL;
	for (tp = hitlist; tp != (hlist *)NULL; tp = tp->nxt) {
		if (tp->t > t)
			break;
		lp = tp;
	}
	fetch(hp);
	hp->t = t;
	hp->obj = obj;
	hp->type = indx;
	if (hitlist == (hlist *)NULL) {
		hitlist = hp;
		hitlist->nxt = (hlist *)NULL;
		*last = hitlist;
	} else if (lp == (hlist *)NULL) {
		hp->nxt = hitlist;
		hitlist = hp;
	} else {
		hp->nxt = lp->nxt;
		lp->nxt = hp;
		if (lp == *last)
			*last = hp;
	}
}

/*
 * inttris
 *
 *	generate a list of hits for the triangles from startface to endface
 * according to the environment set up in geomi. The method used
 * here is what is commonly known as the infinite half-ray approach.
 */
inttris(base, us, vs, midface, midv, startface, endface, last)
	int		base;
	register float	*us, *vs;
	facet		*midface;
	float		midv;
	facet		*startface, *endface;
	hlist		**last;
{
	vector		n;
	register int	crosses;
	register vertno	*cp;
	register float	t, pu, pv, m;
	register facet *face, *start, *end, *mid;

	/*
	 * do a quick search for a likely candidate
	 */
	start = startface;
	end = endface;
	while (start->maxv < minv) {
		mid = start + (end - start) / 2;
		if (start == mid)
			break;
		if (mid->maxv < minv)
			start = mid;
		else
			end = mid;
	}

	/*
	 * scan back
	 */
	if (midv < maxv) {
		end = endface;
		while (end->minv > maxv && end > start)
			end--;
	} else {
		end = midface;
		while (end->minv > maxv && end > start)
			end--;
	}

	for (face = start; face <= end; face++) {
		
		if (face->maxu < minu || face->minu > maxu)
			continue;

		if (face->maxv < minv || face->minv > maxv)
			continue;

		n = fnorms[face->index];

		t = dprod(n, dir);

		if (t == 0.0 || (t > 0.0 && backfacing))
			continue;

		t = -(dprod(n, org) + face->cnst) / t;

		if (t < TOLERANCE)
			continue;

		pu = orgu + t * diru;

		if (face->maxu < pu || face->minu > pu)
			continue;

		pv = orgv + t * dirv;

		if (face->maxv < pv || face->minv > pv)
			continue;

		crosses = 0;

		cp = face->vertnos;

		if ((vs[cp[0]] - pv) * (vs[cp[1]] - pv) < 0.0) {
			if (us[cp[0]] < pu) {
				if (us[cp[1]] < pu)
					crosses++;
				else {
					m = (us[cp[1]] - us[cp[0]]) / (vs[cp[1]] - vs[cp[0]]);
					if (((us[cp[1]] - pu) - m * (vs[cp[1]] - pv)) < 0.0)
						crosses++;
				}
			} else if (us[cp[1]] < pu) {
				m = (us[cp[1]] - us[cp[0]]) / (vs[cp[1]] - vs[cp[0]]);
				if (((us[cp[1]] - pu) - m * (vs[cp[1]] - pv)) < 0.0)
					crosses++;
			}
		}

		if ((vs[cp[1]] - pv) * (vs[cp[2]] - pv) < 0.0) {
			if (us[cp[1]] < pu) {
				if (us[cp[2]] < pu)
					crosses++;
				else {
					m = (us[cp[2]] - us[cp[1]]) / (vs[cp[2]] - vs[cp[1]]);
					if (((us[cp[2]] - pu) - m * (vs[cp[2]] - pv)) < 0.0)
						crosses++;
				}
			} else if (us[cp[2]] < pu) {
				m = (us[cp[2]] - us[cp[1]]) / (vs[cp[2]] - vs[cp[1]]);
				if (((us[cp[2]] - pu) - m * (vs[cp[2]] - pv)) < 0.0)
					crosses++;
			}
		}

		if ((vs[cp[2]] - pv) * (vs[cp[0]] - pv) < 0.0) {
			if (us[cp[2]] < pu) {
				if (us[cp[0]] < pu)
					crosses++;
				else {
					m = (us[cp[0]] - us[cp[2]]) / (vs[cp[0]] - vs[cp[2]]);
					if (((us[cp[0]] - pu) - m * (vs[cp[0]] - pv)) < 0.0)
						crosses++;
				}
			} else if (us[cp[0]] < pu) {
				m = (us[cp[0]] - us[cp[2]]) / (vs[cp[0]] - vs[cp[2]]);
				if (((us[cp[0]] - pu) - m * (vs[cp[0]] - pv)) < 0.0)
					crosses++;
			}
		}

		if (crosses & 1)
			addhit(t, face - startface + base, last);
	}
}

/*
 * intpolys
 *
 *	generate a list of hits for the polygons from startface to endface
 * according to the environment set up in geomi. The method used
 * here is what is commonly known as the infinite half-ray approach.
 */
intpolys(base, us, vs, midface, midv, startface, endface, last)
	int		base;
	register float	*us, *vs;
	facet		*midface;
	float		midv;
	facet		*startface, *endface;
	hlist		**last;
{
	vector		n;
	register int	crosses;
	register vertno	*cp, *np;
	register float	t, pu, pv, m;
	register facet *face, *start, *end, *mid;

	/*
	 * do a quick search for a likely candidate
	 */
	start = startface;
	end = endface;
	while (start->maxv < minv) {
		mid = start + (end - start) / 2;
		if (start == mid)
			break;
		if (mid->maxv < minv)
			start = mid;
		else
			end = mid;
	}

	/*
	 * scan back
	 */
	if (midv < maxv) {
		end = endface;
		while (end->minv > maxv && end > start)
			end--;
	} else {
		end = midface;
		while (end->minv > maxv && end > start)
			end--;
	}

	for (face = start; face <= end; face++) {
		
		if (face->maxu < minu || face->minu > maxu)
			continue;

		if (face->maxv < minv || face->minv > maxv)
			continue;

		n = fnorms[face->index];

		t = dprod(n, dir);

		if (t == 0.0 || (t > 0.0 && backfacing))
			continue;

		t = -(dprod(n, org) + face->cnst) / t;

		if (t < TOLERANCE)
			continue;

		pu = orgu + t * diru;

		if (face->maxu < pu || face->minu > pu)
			continue;

		pv = orgv + t * dirv;

		if (face->maxv < pv || face->minv > pv)
			continue;

		crosses = 0;

		cp = face->vertnos;
		np = &face->vertnos[face->nsides - 1];

		do {

			/*
			 * If we are both above, or both below the intersection
			 * point, go onto the next one.
			 */

			if (vs[*cp] < pv) {
				if (vs[*np] < pv) {
					cp = np--;
					continue;
				}
			} else {
				if (vs[*np] >= pv) {
					cp = np--;
					continue;
				}
			}

			/*
			 * Find out if we have crossed in the negative.
			 */
			if (us[*cp] < pu) {
				if (us[*np] < pu) {	/* crossed on left */
					crosses++;
					cp = np--;
					continue;
				}
			} else if (us[*np] >= pu) {		/* both to right */
				cp = np--;
				continue;
			}

			/* 
			 * We calculate the u at the intersection of the
			 * half-ray and the edge and test it against pu. 
			 */

			m = (us[*np] - us[*cp]) / (vs[*np] - vs[*cp]);
			if (((us[*np] - pu) - m * (vs[*np] - pv)) < 0.0)
				crosses++;

			cp = np--;

		} while (cp != face->vertnos);

		if (crosses & 1)
			addhit(t, face - startface + base, last);
	}
}

/*
 * geomi
 *
 *	returns a list of hits for the ray with a polygon model defined
 * from a geometry file.
 */
hlist *
geomi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	register int		base;
	register geometry	*geom;
	register facet		*start;
	float			t1, t2;
	register float		*us, *vs;
	register float		minx, maxx, miny, maxy, minz, maxz;
	hlist			*hp;
	ray			nr;

	transray(o, nr, *r);

	if (!inbbox(&nr, &o->obj.geo->bb, &t1, &t2))
		return((hlist *)NULL);

	org.x = nr.org.x;
	org.y = nr.org.y;
	org.z = nr.org.z;

	dir.x = nr.dir.x;
	dir.y = nr.dir.y;
	dir.z = nr.dir.z;

	if (dir.x < 0.0) {
		maxx = org.x + t1 * dir.x;
		minx = org.x + t2 * dir.x;
	} else {
		maxx = org.x + t2 * dir.x;
		minx = org.x + t1 * dir.x;
	}

	if (dir.y < 0.0) {
		maxy = org.y + t1 * dir.y;
		miny = org.y + t2 * dir.y;
	} else {
		maxy = org.y + t2 * dir.y;
		miny = org.y + t1 * dir.y;
	}

	if (dir.z < 0.0) {
		maxz = org.z + t1 * dir.z;
		minz = org.z + t2 * dir.z;
	} else {
		maxz = org.z + t2 * dir.z;
		minz = org.z + t1 * dir.z;
	}
	
	hitlist = (hlist *)NULL;

	obj = o;

	geom = o->obj.geo;

	backfacing = geom->options & BACKFACING;

	fnorms = geom->pnorms;

	start = geom->faces;
	base = 0;

	if (geom->npolys[XAXIS] != 0) {
		minu = miny; maxu = maxy;
		minv = minz; maxv = maxz;
		orgu = org.y; diru = dir.y;
		orgv = org.z; dirv = dir.z;
		us = geom->ys; vs = geom->zs;

		if (geom->ntris[XAXIS] != 0)
			inttris(base, us, vs, geom->midts[XAXIS],
				geom->midtval[XAXIS], start,
				start + geom->ntris[XAXIS] - 1, last);

		if (geom->ntris[XAXIS] != geom->npolys[XAXIS])
			intpolys(base + geom->ntris[XAXIS], us, vs, geom->midps[XAXIS],
				geom->midpval[XAXIS], start + geom->ntris[XAXIS],
				start + geom->npolys[XAXIS] - 1, last);

		start += geom->npolys[XAXIS];
		base += geom->npolys[XAXIS];
	}

	if (geom->npolys[YAXIS] != 0) {
		minu = minx; maxu = maxx;
		minv = minz; maxv = maxz;
		orgu = org.x; diru = dir.x;
		orgv = org.z; dirv = dir.z;
		us = geom->xs; vs = geom->zs;

		if (geom->ntris[YAXIS] != 0)
			inttris(base, us, vs, geom->midts[YAXIS],
				geom->midtval[YAXIS], start,
				start + geom->ntris[YAXIS] - 1, last);

		if (geom->ntris[YAXIS] != geom->npolys[YAXIS])
			intpolys(base + geom->ntris[YAXIS], us, vs, geom->midps[YAXIS],
				geom->midpval[YAXIS], start + geom->ntris[YAXIS],
				start + geom->npolys[YAXIS] - 1, last);

		start += geom->npolys[YAXIS];
		base += geom->npolys[YAXIS];
	}

	if (geom->npolys[ZAXIS] != 0) {
		minu = minx; maxu = maxx;
		minv = miny; maxv = maxy;
		orgu = org.x; diru = dir.x;
		orgv = org.y; dirv = dir.y;
		us = geom->xs; vs = geom->ys;

		if (geom->ntris[ZAXIS] != 0)
			inttris(base, us, vs, geom->midts[ZAXIS],
				geom->midtval[ZAXIS], start,
				start + geom->ntris[ZAXIS] - 1, last);

		if (geom->ntris[ZAXIS] != geom->npolys[ZAXIS])
			intpolys(base + geom->ntris[ZAXIS], us, vs, geom->midps[ZAXIS],
				geom->midpval[ZAXIS], start + geom->ntris[ZAXIS],
				start + geom->npolys[ZAXIS] - 1, last);
		}

	if (hitlist != (hlist *)NULL && o == r->orgobj && hitlist->type == r->orgtype) {
		hp = hitlist->nxt;
		release(hitlist);
		return(hp);
	}

	return(hitlist);
}

/*
 * interp3
 *
 *	calculates the interpolated normal for the triangle described
 * by the normals norms. In this case we have to use area (barycentric)
 * coordinates. P is the (x, y, z) point we are on.
 */
static void
interp3(axis, fn, u, v, us, vs, norms, n)
	int		axis;
	vector		*fn;
	register float	u, v;
	register float	*us, *vs;
	register vector	**norms, *n;
{
	mat3x3		mat;
	register float	a1, a2, a3, area;

	if (axis == Y) {
		mat[0][0] = (us[2] - us[0]);
		mat[1][0] = (us[0] - us[1]);
		mat[2][0] = (us[1] - us[2]);

		mat[0][1] = (vs[2] - vs[0]);
		mat[1][1] = (vs[0] - vs[1]);
		mat[2][1] = (vs[1] - vs[2]);
	} else {
		mat[0][0] = (us[0] - us[2]);
		mat[1][0] = (us[1] - us[0]);
		mat[2][0] = (us[2] - us[1]);

		mat[0][1] = (vs[0] - vs[2]);
		mat[1][1] = (vs[1] - vs[0]);
		mat[2][1] = (vs[2] - vs[1]);
	}

	mat[0][2] = us[2] * mat[0][1] - vs[2] * mat[0][0];
	mat[1][2] = us[0] * mat[1][1] - vs[0] * mat[1][0];
	mat[2][2] = us[1] * mat[2][1] - vs[1] * mat[2][0];

	a1 = (v * mat[0][0] - u * mat[0][1] + mat[0][2]);

	a2 = (v * mat[1][0] - u * mat[1][1] + mat[1][2]);

	a3 = (v * mat[2][0] - u * mat[2][1] + mat[2][2]);

	area = a1 + a2 + a3;

	n->x = (a1 * norms[1]->x + a2 * norms[2]->x + a3 * norms[0]->x) / area;
	n->y = (a1 * norms[1]->y + a2 * norms[2]->y + a3 * norms[0]->y) / area;
	n->z = (a1 * norms[1]->z + a2 * norms[2]->z + a3 * norms[0]->z) / area;
}

/*
 * interp4
 *
 *	calculate a normal from the four normals provided by spliting
 * the four sided polygon pg into triangles, finding out which triangle
 * the point hit is in and using interp3 to calculate the normal at the
 * point hit. Note: this routine assumes the centroid splits the
 * 4 sided polygon into equal areas.
 */
static void
interp4(f, axis, u, v, us, vs, norms, cn, n)
	facet		*f;
	int		axis;
	register float	u, v;
	register float	*us, *vs;
	register vector	**norms, *cn, *n;
{
	vector		*tns[12];
	float		tus[12], tvs[12];
	register int	i, j, nj, crosses;
	register float	cu, cv, m;

	/*
	 * calculate the centroid
	 */
	cu = (us[0] + us[1] + us[2] + us[3]) / 4;
	cv = (vs[0] + vs[1] + vs[2] + vs[3]) / 4;

	/*
	 * split the mother into triangles 
	 */
	tus[0] = us[0]; tvs[0] = vs[0]; tns[0] = norms[0];
	tus[1] = us[1]; tvs[1] = vs[1]; tns[1] = norms[1];

	tus[3] = us[1]; tvs[3] = vs[1]; tns[3] = norms[1];
	tus[4] = us[2]; tvs[4] = vs[2]; tns[4] = norms[2];

	tus[6] = us[2]; tvs[6] = vs[2]; tns[6] = norms[2];
	tus[7] = us[3]; tvs[7] = vs[3]; tns[7] = norms[3];

	tus[9] = us[3]; tvs[9] = vs[3]; tns[9] = norms[3];
	tus[10] = us[0]; tvs[10] = vs[0]; tns[10] = norms[0];

	tus[2] = tus[5] = tus[8] = tus[11] = cu;
	tvs[2] = tvs[5] = tvs[8] = tvs[11] = cv;
	tns[2] = tns[5] = tns[8] = tns[11] = cn;

	/*
	 * find which part we have hit
	 */
	for (i = 0; i != 12; i += 3) {
		crosses = 0;
		for (j = i; j != i + 3; j++) {

			nj = (j == i + 2) ? i : j + 1;

			if (tvs[j] < v) {
				if (tvs[nj] < v)
					continue;
			} else {
				if (tvs[nj] > v)
					continue;
			}

			if (tus[j] < u) {
				if (tus[nj] < u) {	/* crossed on left */
					crosses++;
					continue;
				}
			} else if (tus[nj] > u)		/* both to right */
				continue;

			if (tvs[nj] == tvs[j])
				continue;

			m = (tus[nj] - tus[j]) / (tvs[nj] - tvs[j]);
			if (((tus[nj] - u) - m * (tvs[nj] - v)) < 0.0)
				crosses++;
		}

		if (crosses & 1)
			break;
	}

	if (i == 12)
		*n = *cn;
	else
		interp3(axis, &f->n, u, v, &tus[i], &tvs[i], &tns[i], n);
}

/*
 * interptrans
 *
 *	returns the interpolated transparency to a polygon in the geometry o.
 */
void
interptrans(o, indx, l, c)
	object	*o;
	int	indx;
	vector	*l, *c;
{
	facet		*f;
	geometry	*gm;
	vector		*cols[4], tcols[4], cn, loc;
	float		us[4], vs[4], u, v;
	int		i, axis, nsides;
	vertno		*verts;

	gm = o->obj.geo;

	f = &gm->faces[indx];

	if (f->nsides > 4) {
		c->x = o->s->trans.r;
		c->y = o->s->trans.g;
		c->z = o->s->trans.b;
		return;
	}

	nsides = f->nsides;
	verts = f->vertnos;

	toobject(o, loc, *l);

	/*
	 * set the u, v values
	 */
	if (indx < gm->npolys[XAXIS]) {		/* x facing */
		u = loc.y; v = loc.z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->ys[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = X;
	} else if (indx < gm->npolys[YAXIS] + gm->npolys[XAXIS]) {	/* y facing */
		u = loc.x; v = loc.z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = Y;
	} else {				/* z facing */
		u = loc.x; v = loc.y;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->ys[verts[i]];
		}
		axis = Z;
	}

	for (i = 0; i != nsides; i++) {
		tcols[i].x = tcols[i].y = tcols[i].z = gm->transp[f->vertnos[i]];
		cols[i] = &tcols[i];
	}

	if (nsides == 3)
		interp3(axis, &f->n, u, v, us, vs, cols, c);
	else {
		c->x = o->s->trans.r;
		c->y = o->s->trans.g;
		c->z = o->s->trans.b;
		cn.x = (cols[0]->x + cols[1]->x + cols[2]->x + cols[3]->x) / 4;
		cn.y = (cols[0]->y + cols[1]->y + cols[2]->y + cols[3]->y) / 4;
		cn.z = (cols[0]->z + cols[1]->z + cols[2]->z + cols[3]->z) / 4;
		interp4(f, axis, u, v, us, vs, cols, &cn, c);
	}
}

/*
 * interpcol
 *
 *	returns the interpolated colour to a polygon in the geometry o.
 */
void
interpcol(o, indx, l, c)
	object	*o;
	int	indx;
	vector	*l, *c;
{
	facet		*f;
	geometry	*gm;
	vector		*cols[4], cn, loc;
	float		us[4], vs[4], u, v;
	int		i, axis, nsides;
	vertno		*verts;

	gm = o->obj.geo;

	f = &gm->faces[indx];

	if (f->nsides > 4) {
		c->x = o->s->c.r;
		c->y = o->s->c.g;
		c->z = o->s->c.b;
		return;
	}

	nsides = f->nsides;
	verts = f->vertnos;

	toobject(o, loc, *l);

	/*
	 * set the u, v values
	 */
	if (indx < gm->npolys[XAXIS]) {		/* x facing */
		u = loc.y; v = loc.z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->ys[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = X;
	} else if (indx < gm->npolys[YAXIS] + gm->npolys[XAXIS]) {	/* y facing */
		u = loc.x; v = loc.z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = Y;
	} else {				/* z facing */
		u = loc.x; v = loc.y;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->ys[verts[i]];
		}
		axis = Z;
	}

	for (i = 0; i != nsides; i++)
		cols[i] = &gm->vcolours[gm->vcolourtable[f->index][i]];

	if (nsides == 3)
		interp3(axis, &f->n, u, v, us, vs, cols, c);
	else {
		c->x = o->s->c.r;
		c->y = o->s->c.g;
		c->z = o->s->c.b;
		cn.x = (cols[0]->x + cols[1]->x + cols[2]->x + cols[3]->x) / 4;
		cn.y = (cols[0]->y + cols[1]->y + cols[2]->y + cols[3]->y) / 4;
		cn.z = (cols[0]->z + cols[1]->z + cols[2]->z + cols[3]->z) / 4;
		interp4(f, axis, u, v, us, vs, cols, &cn, c);
	}
}

/*
 * interp
 *
 *	returns the interpolated normal to a polygon pg.
 */
static void
interp(gm, indx, l, n)
	geometry	*gm;
	int		indx;
	vector		*l, *n;
{
	facet	*f;
	vector	*norms[4], cn;
	float	us[4], vs[4], u, v;
	int	i, axis, nsides;
	vertno	*verts;

	f = &gm->faces[indx];

	if (f->nsides > 4) {
		*n = gm->pnorms[f->index];
		return;
	}

	nsides = f->nsides;
	verts = f->vertnos;

	/*
	 * set the u, v values
	 */
	if (indx < gm->npolys[XAXIS]) {		/* x facing */
		u = l->y; v = l->z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->ys[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = X;
	} else if (indx < gm->npolys[YAXIS] + gm->npolys[XAXIS]) {	/* y facing */
		u = l->x; v = l->z;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->zs[verts[i]];
		}
		axis = Y;
	} else {				/* z facing */
		u = l->x; v = l->y;
		for (i = 0; i != nsides; i++) {
			us[i] = gm->xs[verts[i]];
			vs[i] = gm->ys[verts[i]];
		}
		axis = Z;
	}

	if (gm->vnormtable == (vertno **)NULL)
		for (i = 0; i != nsides; i++)
			norms[i] = &gm->vnorms[verts[i]];
	else
		for (i = 0; i != nsides; i++) 
			norms[i] = &gm->vnorms[gm->vnormtable[f->index][i]];

	if (nsides == 3)
		interp3(axis, &f->n, u, v, us, vs, norms, n);
	else {
		cn.x = (norms[0]->x + norms[1]->x + norms[2]->x + norms[3]->x) / 4;
		cn.y = (norms[0]->y + norms[1]->y + norms[2]->y + norms[3]->y) / 4;
		cn.z = (norms[0]->z + norms[1]->z + norms[2]->z + norms[3]->z) / 4;
		interp4(f, axis, u, v, us, vs, norms, &cn, n);
	}
}

/*
 * geomn
 *
 *	returns the normal to a polygon with index number indx
 */
void
geomn(n, l, o, indx)
	vector	*n, *l;
	object	*o;
	int	indx;
{
	facet		*face;
	geometry	*geom;
	vector		loc;

	geom = o->obj.geo;

	face = &geom->faces[indx];
	fnorms = geom->pnorms;

	if (geom->options & PHONGSHADING) {
		toobject(o, loc, *l);
		interp(geom, indx, &loc, n);
	} else {
		*n = geom->pnorms[face->index];
	}
}

/*
 * calc2dbbox
 *
 *	 calculate a 2d bounding box for a facet
 *
 */
calc2dbbox(pg, type, xs, ys, zs, nsides)
	register facet	*pg;
	unsigned char	type;
	float		*xs, *ys, *zs;
	int		nsides;
{
	int		j;
	register float	*us, *vs;

	switch (type) {
	case X:
		us = ys;
		vs = zs;
		break;
	case Y:
		us = xs;
		vs = zs;
		break;
	case Z:
		us = xs;
		vs = ys;
		break;
	default:
		fatal("calc2dbbox: bad type in switch\n");
	}
		
	pg->minu = pg->maxu = us[pg->vertnos[0]];
	pg->minv = pg->maxv = vs[pg->vertnos[0]];
	for (j = 1; j != nsides; j++) {
		minmax(pg->minu, pg->maxu, us[pg->vertnos[j]]);
		minmax(pg->minv, pg->maxv, vs[pg->vertnos[j]]);
	}
}

/*
 * sortpolys
 *
 *	sort a list of polygons according to their maxv values bubbling
 * all the triangles down to the start.
 */
sortpolys(polys, npolys, ntris)
	facet	**polys;
	int	npolys, ntris;
{
	int	i, swapped;
	facet	*face, *next, **start, **end;

	/*
	 * put the triangles at the start.
	 */
	start = &polys[0];
	end = &polys[npolys - 1];
	while (end > start) {
		while ((*start)->nsides == 3 && start != end)
			start++;
		while ((*end)->nsides != 3 && end > start)
			end--;
		face = *start;
		*start = *end;
		*end = face;
	}

	/*
	 * sort triangles by maxv
	 */
	swapped = TRUE;
	while (swapped) {
		swapped = FALSE;
		for (i = 0; i < ntris - 1; i++) {
			face = polys[i];
			next = polys[i + 1];
			if (face->maxv > next->maxv) {
				polys[i + 1] = face;
				polys[i] = next;
				swapped = TRUE;
			}
		}
	}

	/*
	 * sort others by maxv
	 */
	swapped = TRUE;
	while (swapped) {
		swapped = FALSE;
		for (i = ntris; i < npolys - 1; i++) {
			face = polys[i];
			next = polys[i + 1];
			if (face->maxv > next->maxv) {
				polys[i + 1] = face;
				polys[i] = next;
				swapped = TRUE;
			}
		}
	}
}

/*
 * readgeometry
 *
 *	reads in and sets up a polygon model from a geometry file, care
 * is taken to make sure the same file is not read in twice.
 */
readgeometry(name, geom, pnorms, vnorms)
	char		*name;
	geometry	**geom;
	vector		**pnorms, **vnorms;
{
        float		minx, maxx, miny, maxy, minz, maxz;
	float		*xs, *ys, *zs, midminv;
	int		nverts, connections, npnts;
	int		i, j, numedges, phong, delcount;
	int		*ntris, *npolys, cvertno, base;
	vertno		*vertnums, *pntno;
	FILE		*pfile;
	vector		v, v1, v2, norm, *norms, *nrms;
	facet		*face, **polys[DIMS], *faces;
	unsigned char	*axis;
	char		buf[BUFSIZ];
	symbol		*sym;
	geometry	*gm;

	if ((sym = findsym(geoms, name)) != (symbol *)NULL) {
		*geom = sym->u.geo.geom;
		*vnorms = sym->u.geo.vnorms;
		*pnorms = sym->u.geo.pnorms;
		return;
	}

	if ((pfile = fopen(name, "r")) == NULL) {
		sprintf(buf, "art: cannot open offfile %s.\n", name);
		fatal(buf);
	}

	if (fscanf(pfile, "%d %d %d\n", &nverts, &connections, &numedges) != 3) {
		sprintf(buf, "art: can't find header info in offfile %s.\n", name);
		fatal(buf);
	}

	sym = insertsym(&geoms, name);

	*geom = sym->u.geo.geom = gm = (geometry *)smalloc(sizeof(geometry));

	gm->colours = (vector *)NULL;
	gm->colourtable = (unsigned short *)NULL;

	gm->vcolours = (vector *)NULL;
	gm->vcolourtable = (vertno **)NULL;

	gm->vnorms = (vector *)NULL;
	gm->vnormtable = (vertno **)NULL;

	gm->transp = (float *)NULL;

	gm->options = astackp->d.options;
	gm->xs = xs = (float *)scalloc(sizeof(float), nverts);
	gm->ys = ys = (float *)scalloc(sizeof(float), nverts);
	gm->zs = zs = (float *)scalloc(sizeof(float), nverts);
	vertnums = (vertno *)scalloc(sizeof(vertno), numedges);
	gm->faces = (facet *)NULL;

	ntris = gm->ntris;
	npolys = gm->npolys;

	*pnorms = nrms = (vector *)scalloc(sizeof(vector), connections);

	phong = (gm->options & PHONGSHADING);

	if (phong)
		*vnorms = norms = (vector *)scalloc(sizeof(vector), nverts);
	else
		*vnorms = (vector *)NULL;

	sym->u.geo.vnorms = *vnorms;
	sym->u.geo.pnorms = *pnorms;

	fscanf(pfile, "%f %f %f\n", &v1.x, &v1.y, &v1.z);

	minx = maxx = v1.x;
	miny = maxy = v1.y;
	minz = maxz = v1.z;
	xs[0] = v1.x; ys[0] = v1.y; zs[0] = v1.z;

	for (i = 1; i != nverts; i++) {
		fscanf(pfile, "%f %f %f\n", &v1.x, &v1.y, &v1.z);
		minmax(minx, maxx, v1.x);
		minmax(miny, maxy, v1.y);
		minmax(minz, maxz, v1.z);
		xs[i] = v1.x; ys[i] = v1.y; zs[i] = v1.z;
	}

	/*
	 * set up the bounding box 
	 */
	gm->bb.max[X] = maxx; gm->bb.min[X] = minx;
	gm->bb.max[Y] = maxy; gm->bb.min[Y] = miny;
	gm->bb.max[Z] = maxz; gm->bb.min[Z] = minz;

	/*
	 * set organisation
	 */
	faces = (facet *)scalloc(sizeof(facet), connections);
	axis = (unsigned char *)scalloc(1, connections);

	if (phong)
		for (i = 0; i != nverts; i++)
			norms[i].x = norms[i].y = norms[i].z = 0.0;

	delcount = cvertno = 0;
	npolys[XAXIS] = npolys[YAXIS] = npolys[ZAXIS] = 0;
	ntris[XAXIS] = ntris[YAXIS] = ntris[ZAXIS] = 0;
	for (i = 0; i != connections; i++) {
		fscanf(pfile, "%d", &npnts);
		face = &faces[i];

		face->index = i + delcount;

		face->vertnos = pntno = &vertnums[cvertno];
		fscanf(pfile, "%hd", &pntno[0]);
		pntno[0] -= 1;
		
		for (j = 1; j != npnts; j++) {
			fscanf(pfile, "%hd", &pntno[j]);
			pntno[j] -= 1;
		}

		v1.x = xs[pntno[1]] - xs[pntno[0]];
		v1.y = ys[pntno[1]] - ys[pntno[0]];
		v1.z = zs[pntno[1]] - zs[pntno[0]];

		v2.x = xs[pntno[2]] - xs[pntno[1]];
		v2.y = ys[pntno[2]] - ys[pntno[1]];
		v2.z = zs[pntno[2]] - zs[pntno[1]];

		xprod(norm, v1, v2);

		face->n = norm;

		/*
		 * reverse it as the default normal is backwards.
		 */
		smult(norm, -1.0);

		if (norm.x != 0.0 || norm.y != 0.0 || norm.z != 0.0) {
			normalise(norm);

			if (phong) {
				for (j = 0; j != npnts; j++)
					vadd(norms[pntno[j]], norms[pntno[j]], norm);
			}

			if (fabs(norm.x) > fabs(norm.y))
				if (fabs(norm.x) > fabs(norm.z))
					axis[i] = XAXIS;
				else
					axis[i] = ZAXIS;
			else
				if (fabs(norm.y) > fabs(norm.z))
					axis[i] = YAXIS;
				else
					axis[i] = ZAXIS;

			npolys[axis[i]]++;
			if (npnts == 3)
				ntris[axis[i]]++;

			calc2dbbox(face, axis[i], xs, ys, zs, npnts);

			nrms[i + delcount] = norm;

			v.x = xs[pntno[0]];
			v.y = ys[pntno[0]];
			v.z = zs[pntno[0]];

			face->cnst = -dprod(norm, v);

			face->nsides = npnts;
		} else {
			connections--;		/* a dud facet */
			i--;
			delcount++;
			sprintf(buf, "polygon %d in geometry file %s not a polygon!\n", i + 2, name);
			message(buf);
		}

		cvertno += npnts;
	}

	if (phong)
		for (i = 0; i != nverts; i++) {
			if (norms[i].x == 0.0 && norms[i].y == 0.0 && norms[i].z == 0.0) {
				sprintf(buf, "vertex %d unused in geometry file %s\n", i + 1, name);
				message(buf);
			} else 
				normalise(norms[i]);
		}

	polys[XAXIS] = (facet **)scalloc(sizeof(facet *), npolys[XAXIS]);
	polys[YAXIS] = (facet **)scalloc(sizeof(facet *), npolys[YAXIS]);
	polys[ZAXIS] = (facet **)scalloc(sizeof(facet *), npolys[ZAXIS]);

	npolys[XAXIS] = npolys[YAXIS] = npolys[ZAXIS] = 0;

	for (i = 0; i < connections; i++)
		polys[axis[i]][npolys[axis[i]]++] = faces + i;

	gm->faces = (facet *)scalloc(sizeof(facet), connections);

	base = 0;
	for (i = 0; i != DIMS; i++) {
		if (npolys[i] != 0) {
			sortpolys(polys[i], npolys[i], ntris[i]);
			for (j = 0; j != npolys[i]; j++)
				gm->faces[j + base] = *polys[i][j];

			gm->midts[i] = &gm->faces[base + ntris[i] / 2];
			midminv = gm->midts[i]->minv;
			for (j = ntris[i] / 2; j != ntris[i]; j++)
				if (midminv > polys[i][j]->minv)
					midminv = polys[i][j]->minv;
			gm->midtval[i] = midminv;

			if (ntris[i] != npolys[i]) {
				gm->midps[i] = &gm->faces[base + ntris[i] + (npolys[i] - ntris[i]) / 2];
				midminv = gm->midps[i]->minv;
				for (j = ntris[i] + (npolys[i] - ntris[i]) / 2; j != npolys[i]; j++)
					if (midminv > polys[i][j]->minv)
						midminv = polys[i][j]->minv;
				gm->midpval[i] = midminv;
			}

			base += npolys[i];
		}
	}

	if (cvertno != numedges) {
		sprintf(buf, "number of edges should be %d not %d in file %s\n", cvertno, numedges, name);
		warning(buf);
	}

	free(axis);
	free(faces);

	free(polys[XAXIS]);
	free(polys[YAXIS]);
	free(polys[ZAXIS]);

	fclose(pfile);
}

/*
 * readcolours
 *
 *	read in the colour information for a geometry file
 */
readcolours(name, col, colindxs)
	char		*name;
	vector		**col;
	unsigned short	**colindxs;
{
	FILE		*cfile;
	int		numcols, numindexes, i;
	vector		*cl;
	unsigned short	*inds;
	char		buf[BUFSIZ];
	symbol		*sym;

	if ((sym = findsym(colours, name)) != (symbol *)NULL) {
		*col = sym->u.cols.colours;
		*colindxs = sym->u.cols.colourtable;
		return;
	}

	if ((cfile = fopen(name, "r")) == NULL) {
		sprintf(buf, "art: cannot open colourfile %s.\n", name);
		fatal(buf);
	}

	fscanf(cfile, "%d %d ", &numcols, &numindexes);

	*col = cl = (vector *)scalloc(sizeof(vector), numcols);

	*colindxs = inds = (unsigned short *)scalloc(sizeof(unsigned short), numindexes);

	sym = insertsym(&colours, name);

	sym->u.cols.colours = cl;
	sym->u.cols.colourtable = inds;

	for (i = 0; i != numcols; i++)
		fscanf(cfile, "%f %f %f ", &cl[i].x, &cl[i].y, &cl[i].z);

	for (i = 0; i != numindexes; i++) {
		fscanf(cfile, "%hd ", &inds[i]);
		inds[i] -= 1;
	}

	fclose(cfile);
}

/* 
 * readnormals
 *
 *	read in the polygon normal information for a geometry file
 */
readnormals(name, norms)
	char	*name;
	vector	**norms;
{
	FILE		*nfile;
	int		nnorms, i;
	char		buf[BUFSIZ];
	symbol		*sym;
	vector		*nrms;

	if ((sym = findsym(normals, name)) != (symbol *)NULL) {
		*norms = sym->u.norm.norms;
		return;
	}

	if ((nfile = fopen(name, "r")) == NULL) {
		sprintf(buf, "art: cannot open normalfile %s.\n", name);
		fatal(buf);
	}

	if (fscanf(nfile, "%d ", &nnorms) != 1) {
		sprintf(buf, "art: bad header in normalfile %s.\n", name);
		fatal(buf);
	}

	*norms = nrms = (vector *)scalloc(sizeof(vector), nnorms);

	sym = insertsym(&normals, name);

	sym->u.norm.norms = nrms;

	for (i = 0; i != nnorms; i++)
		fscanf(nfile, "%f %f %f ", &nrms[i].x, &nrms[i].y, &nrms[i].z);

	fclose(nfile);
}


/* 
 * readvcolours
 *
 *	read in the vertex normal information for a geometry file
 */
readvcolours(name, cols, coltable)
	char	*name;
	vector	**cols;
	vertno	***coltable;
{
	FILE		*nfile;
	int		ncols, npolys, nedges, i, j, npoints, cvertno;
	char		buf[BUFSIZ];
	symbol		*sym;
	vector		*cls;
	vertno		**ctbl, *verts, *pntno;

	if ((sym = findsym(vcolours, name)) != (symbol *)NULL) {
		*cols = sym->u.norm.norms;
		*coltable = sym->u.norm.ntable;
		return;
	}

	if ((nfile = fopen(name, "r")) == NULL) {
		sprintf(buf, "art: cannot open vcolourfile %s.\n", name);
		fatal(buf);
	}

	if (fscanf(nfile, "%d %d %d ", &ncols, &npolys, &nedges) != 3) {
		sprintf(buf, "art: bad header in vcolourfile %s.\n", name);
		fatal(buf);
	}

	*cols = cls = (vector *)scalloc(sizeof(vector), ncols);

	*coltable = ctbl = (vertno **)scalloc(sizeof(vertno *), npolys);

	sym = insertsym(&vcolours, name);

	sym->u.norm.norms = cls;
	sym->u.norm.ntable = ctbl;

	for (i = 0; i != ncols; i++)
		fscanf(nfile, "%f %f %f ", &cls[i].x, &cls[i].y, &cls[i].z);

	verts = (vertno *)scalloc(sizeof(vertno), nedges);

	cvertno = 0;

	for (i = 0; i != npolys; i++) {
		fscanf(nfile, "%d", &npoints);

		ctbl[i] = pntno = &verts[cvertno];

		for (j = 0; j != npoints; j++)
			fscanf(nfile, "%hd", &pntno[j]);

		for (j = 0; j != npoints; j++)
			pntno[j] -= 1;

		cvertno += npoints;
	}

	fclose(nfile);
}

/* 
 * readvnormals
 *
 *	read in the vertex normal information for a geometry file
 */
readvnormals(name, norms, ntable)
	char	*name;
	vector	**norms;
	vertno	***ntable;
{
	FILE		*nfile;
	int		nnorms, npolys, nedges, i, j, npoints, cvertno;
	char		buf[BUFSIZ];
	symbol		*sym;
	vector		*nrms;
	vertno		**ntbl, *verts, *pntno;

	if ((sym = findsym(vnormals, name)) != (symbol *)NULL) {
		*norms = sym->u.norm.norms;
		*ntable = sym->u.norm.ntable;
		return;
	}

	if ((nfile = fopen(name, "r")) == NULL) {
		sprintf(buf, "art: cannot open vnormalfile %s.\n", name);
		fatal(buf);
	}

	if (fscanf(nfile, "%d %d %d ", &nnorms, &npolys, &nedges) != 3) {
		sprintf(buf, "art: bad header in vnormalfile %s.\n", name);
		fatal(buf);
	}

	*norms = nrms = (vector *)scalloc(sizeof(vector), nnorms);

	*ntable = ntbl = (vertno **)scalloc(sizeof(vertno *), npolys);

	sym = insertsym(&vnormals, name);

	sym->u.norm.norms = nrms;
	sym->u.norm.ntable = ntbl;

	for (i = 0; i != nnorms; i++)
		fscanf(nfile, "%f %f %f ", &nrms[i].x, &nrms[i].y, &nrms[i].z);

	verts = (vertno *)scalloc(sizeof(vertno), nedges);

	cvertno = 0;

	for (i = 0; i != npolys; i++) {
		fscanf(nfile, "%d", &npoints);

		ntbl[i] = pntno = &verts[cvertno];

		for (j = 0; j != npoints; j++)
			fscanf(nfile, "%hd", &pntno[j]);

		for (j = 0; j != npoints; j++)
			pntno[j] -= 1;

		cvertno += npoints;
	}

	fclose(nfile);
}

/*
 * dostrip
 *
 *	does a triangular strip
 */
dostrip(d, geom, pnorms, vnorms, vcols, vcoltable)
	details		*d;
	geometry	**geom;
	vector		**pnorms, **vnorms;
	vector		**vcols;
	vertno		***vcoltable;
{
	int		count;
	vector		*cols;
	details		*nd, *nnd, *dets;
        float		minx, maxx, miny, maxy, minz, maxz;
	float		*xs, *ys, *zs, *transp, midminv;
	int		nverts, connections;
	int		i, j, numedges, phong, delcount;
	int		*ntris, *npolys, cvertno, base;
	vertno		*vertnums, *pntno, **vcoltbl;
	vector		v, v1, v2, norm, *norms, *nrms;
	facet		*face, **polys[DIMS], *faces;
	int		dcount, detcount;
	int		normsfound, colsfound, transfound;
	unsigned char	*axis;
	char		buf[BUFSIZ];
	geometry	*gm;

	/*
	 * count vertices and reverse vertex list.
	 */
	count = 0;
	dets = (details *)NULL;

	while (d != (details *)NULL) {
		nd = d->nxt;

		d->nxt = dets;
		dets = d;

		count++;
		d = nd;
	}

	d = dets;

	/*
	 * count number of details per vertex
	 */
	detcount = 0;
	for (nd = d->u.det; nd != (details *)NULL; nd = nd->nxt)
		detcount++;

	normsfound = colsfound = transfound = FALSE;

	dcount = detcount;
	for (nd = d->u.det; nd != (details *)NULL; nd = nd->nxt) {
		if (nd->type == VERTEX && dcount > 1)
			normsfound = TRUE;
		else if (nd->type == COLOUR)
			colsfound = TRUE;
		else if (nd->type == TRANSPARENCY)
			transfound = TRUE;
		dcount--;
	}

	*vcols = cols = (vector *)scalloc(count, sizeof(colour));

	nverts = count;
	connections = count - 2;
	numedges = (count - 2) * 3;

	*geom = gm = (geometry *)smalloc(sizeof(geometry));

	gm->colours = (vector *)NULL;
	gm->colourtable = (unsigned short *)NULL;

	gm->vcolours = (vector *)NULL;
	gm->vcolourtable = (vertno **)NULL;

	gm->vnorms = (vector *)NULL;
	gm->vnormtable = (vertno **)NULL;

	if (transfound)
		gm->transp = transp = (float *)scalloc(count, sizeof(float));
	else
		gm->transp = transp = (float *)NULL;

	gm->options = astackp->d.options;
	gm->xs = xs = (float *)scalloc(sizeof(float), nverts);
	gm->ys = ys = (float *)scalloc(sizeof(float), nverts);
	gm->zs = zs = (float *)scalloc(sizeof(float), nverts);
	vertnums = (vertno *)scalloc(sizeof(vertno), numedges);

	*vcoltable = vcoltbl = (vertno **)scalloc(sizeof(vertno *), connections);

	gm->faces = (facet *)NULL;

	ntris = gm->ntris;
	npolys = gm->npolys;

	*pnorms = nrms = (vector *)scalloc(sizeof(vector), connections);

	phong = (gm->options & PHONGSHADING);

	if (phong || normsfound) {
		*vnorms = norms = (vector *)scalloc(sizeof(vector), nverts);
		for (i = 0; i != nverts; i++)
			norms[i].x = norms[i].y = norms[i].z = 0.0;
	} else
		*vnorms = (vector *)NULL;

	count = 0;
	while (d != (details *)NULL) {
		switch (d->type) {
		case COMPLEXVERTEX:
			dcount = detcount;
			for (nd = d->u.det; nd != (details *)NULL; nd = nnd) {

				if (nd->type == VERTEX) {
					if (dcount > 1) {
						norms[count].x = nd->u.v.x;
						norms[count].y = nd->u.v.y;
						norms[count].z = nd->u.v.z;
					} else {
						xs[count] = nd->u.v.x;
						ys[count] = nd->u.v.y;
						zs[count] = nd->u.v.z;
					}
				} else if (nd->type == COLOUR) {
					cols[count].x = nd->u.c.r;
					cols[count].y = nd->u.c.g;
					cols[count].z = nd->u.c.b;
				} else if (nd->type == TRANSPARENCY)
					transp[count] = nd->u.c.r;
				dcount--;
				nnd = nd->nxt;
				free(nd);
			}
			break;
		case VERTEX:
			nd = d->u.det;

			xs[count] = nd->u.v.x;
			ys[count] = nd->u.v.y;
			zs[count] = nd->u.v.z;

			cols[count].x = astackp->d.s->c.r;
			cols[count].y = astackp->d.s->c.g;
			cols[count].z = astackp->d.s->c.b;

			free(nd);
			break;
		default:
			fatal("art: unknown construct in strip.\n");
		}
		count++;
		nd = d->nxt;
		free(d);
		d = nd;
	}

	minx = maxx = xs[0];
	miny = maxy = ys[0];
	minz = maxz = zs[0];

	for (i = 1; i != nverts; i++) {
		minmax(minx, maxx, xs[i]);
		minmax(miny, maxy, ys[i]);
		minmax(minz, maxz, zs[i]);
	}

	/*
	 * set up the bounding box 
	 */
	gm->bb.max[X] = maxx; gm->bb.min[X] = minx;
	gm->bb.max[Y] = maxy; gm->bb.min[Y] = miny;
	gm->bb.max[Z] = maxz; gm->bb.min[Z] = minz;

	/*
	 * set organisation
	 */
	faces = (facet *)scalloc(sizeof(facet), connections);
	axis = (unsigned char *)scalloc(1, connections);

	delcount = cvertno = 0;
	ntris[XAXIS] = ntris[YAXIS] = ntris[ZAXIS] = 0;
	count = connections;

	for (i = 0; i != connections; i++) {
		face = &faces[i];

		face->index = i + delcount;

		face->vertnos = vcoltbl[face->index] = &vertnums[cvertno];
		
		pntno = face->vertnos;

		if (i & 1) {
			pntno[2] = cvertno / 3;
			pntno[1] = cvertno / 3 + 1;
			pntno[0] = cvertno / 3 + 2;
		} else {
			pntno[0] = cvertno / 3;
			pntno[1] = cvertno / 3 + 1;
			pntno[2] = cvertno / 3 + 2;
		}

		count--;

		v1.x = xs[pntno[1]] - xs[pntno[0]];
		v1.y = ys[pntno[1]] - ys[pntno[0]];
		v1.z = zs[pntno[1]] - zs[pntno[0]];

		v2.x = xs[pntno[2]] - xs[pntno[1]];
		v2.y = ys[pntno[2]] - ys[pntno[1]];
		v2.z = zs[pntno[2]] - zs[pntno[1]];

		xprod(norm, v1, v2);

		face->n = norm;

		if (norm.x != 0.0 || norm.y != 0.0 || norm.z != 0.0) {
			normalise(norm);

			if (phong && !normsfound) {
				vadd(norms[pntno[0]], norms[pntno[0]], norm);
				vadd(norms[pntno[1]], norms[pntno[1]], norm);
				vadd(norms[pntno[2]], norms[pntno[2]], norm);
			}

			if (fabs(norm.x) > fabs(norm.y))
				if (fabs(norm.x) > fabs(norm.z))
					axis[i] = XAXIS;
				else
					axis[i] = ZAXIS;
			else
				if (fabs(norm.y) > fabs(norm.z))
					axis[i] = YAXIS;
				else
					axis[i] = ZAXIS;

			ntris[axis[i]]++;

			calc2dbbox(face, axis[i], xs, ys, zs, 3);

			nrms[i + delcount] = norm;

			v.x = xs[pntno[0]];
			v.y = ys[pntno[0]];
			v.z = zs[pntno[0]];

			face->cnst = -dprod(norm, v);

			face->nsides = 3;
		} else {
			connections--;		/* a dud facet */
			i--;
			delcount++;
			sprintf(buf, "polygon %d in triangular strip is not a polygon!\n", i + 2);
			message(buf);
		}

		cvertno += 3;
	}

	if (phong)
		for (i = 0; i != nverts; i++) {
			if (norms[i].x == 0.0 && norms[i].y == 0.0 && norms[i].z == 0.0) {
				sprintf(buf, "vertex %d with dud triangle in strip.\n", i);
				message(buf);
			} else 
				normalise(norms[i]);
		}

	polys[XAXIS] = polys[YAXIS] = polys[ZAXIS] = (facet **)NULL;

	if (ntris[XAXIS] != 0)
		polys[XAXIS] = (facet **)scalloc(sizeof(facet *), ntris[XAXIS]);
	if (ntris[YAXIS] != 0)
		polys[YAXIS] = (facet **)scalloc(sizeof(facet *), ntris[YAXIS]);
	if (ntris[ZAXIS] != 0)
		polys[ZAXIS] = (facet **)scalloc(sizeof(facet *), ntris[ZAXIS]);

	npolys[XAXIS] = npolys[YAXIS] = npolys[ZAXIS] = 0;

	for (i = 0; i < connections; i++)
		polys[axis[i]][npolys[axis[i]]++] = faces + i;

	gm->faces = (facet *)scalloc(sizeof(facet), connections);

	base = 0;
	for (i = 0; i != DIMS; i++) {
		if (npolys[i] != 0) {
			sortpolys(polys[i], npolys[i], ntris[i]);
			for (j = 0; j != npolys[i]; j++)
				gm->faces[j + base] = *polys[i][j];

			gm->midts[i] = &gm->faces[base + ntris[i] / 2];
			midminv = gm->midts[i]->minv;
			for (j = ntris[i] / 2; j != ntris[i]; j++)
				if (midminv > polys[i][j]->minv)
					midminv = polys[i][j]->minv;
			gm->midtval[i] = midminv;

			base += npolys[i];
		}
	}

	free(axis);
	free(faces);

	free(polys[XAXIS]);
	free(polys[YAXIS]);
	free(polys[ZAXIS]);
}

/*
 * geometryinit
 *
 *	sets up a geometry object, these can be off files, or heightfields.
 */
void
geometryinit(o, d)
	object	*o;
	details	*d;
{
	details		*ld;
	vector          *cols;
	unsigned short  *colindexes;
	vector		c1, c2, cent, *pnorms1, *pnorms2, *vnorms1, *vnorms2;
	vector		*vcols;
	vertno		**vnormtab, **vcoltab;
	geometry	*geom;
	heightfield	*hfield;
	float		*cfield;
	int		npolys, i;
	facet		*f;

	vcols = (vector *)NULL;
	vnorms1 = vnorms2 = (vector *)NULL;
	pnorms1 = pnorms2 = (vector *)NULL;
	vcoltab = vnormtab = (vertno **)NULL;

	cols = (vector *)NULL;
	colindexes = (unsigned short *)NULL;

	geom = (geometry *)NULL;
	hfield = (heightfield *)NULL;
	cfield = (float *)NULL;

	while (d != (details *)NULL) {
		switch (d->type) {
		case OFFFILE:
			readgeometry(d->u.s, &geom, &pnorms1, &vnorms1);
			break;
		case COLOURFILE:
			readcolours(d->u.s, &cols, &colindexes);
			break;
		case NORMALFILE:
			readnormals(d->u.s, &pnorms2);
			break;
		case VCOLOURFILE:
			readvcolours(d->u.s, &vcols, &vcoltab);
			break;
		case VNORMALFILE:
			readvnormals(d->u.s, &vnorms2, &vnormtab);
			break;
		case HEIGHTFIELD:
			readheightfield(d->u.s, &hfield);
			break;
		case COLOURFIELD:
			readcolourfield(d->u.s, &cfield);
			break;
		case STRIP:
			dostrip(d->u.det, &geom, &pnorms1, &vnorms1, &vcols, &vcoltab);
			break;
		default:
			warning("art: illegal field in geometry ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	calctransforms(mstackp);

	if (geom != (geometry *)NULL) {

		o->obj.geo = geom;

		geom->colours = cols;
		geom->colourtable = colindexes;

		geom->vcolours = vcols;
		geom->vcolourtable = vcoltab;

		if (pnorms2 != (vector *)NULL)
			geom->pnorms = pnorms2;
		else
			geom->pnorms = pnorms1;
		
		if (vnorms2 != (vector *)NULL) {
			geom->vnorms = vnorms2;
			geom->vnormtable = vnormtab;
		} else {
			geom->vnormtable = (vertno **)NULL;
			geom->vnorms = vnorms1;
		}

		c1.x = o->obj.geo->bb.min[X];
		c1.y = o->obj.geo->bb.min[Y];
		c1.z = o->obj.geo->bb.min[Z];
		c2.x = o->obj.geo->bb.max[X];
		c2.y = o->obj.geo->bb.max[Y];
		c2.z = o->obj.geo->bb.max[Z];
		cent.x = (c1.x + c2.x) / 2.0;
		cent.y = (c1.y + c2.y) / 2.0;
		cent.z = (c1.z + c2.z) / 2.0;

		makebbox(o, c1.x, c1.y, c1.z, c2.x, c2.y, c2.z);
	} else if (hfield != (heightfield *)NULL) {
		o->type = HFIELD;
		o->obj.hfield = hfield;
		o->obj.hfield->colours = cfield;

		makebbox(o, 0.0, 0.0, hfield->hfnode->minz, 1.0, 1.0, hfield->hfnode->maxz);
	} else {
		warning("art: no geometry data given in geometry.\n");
		o->type = NULL_OBJ;
	}

	setattributes(o);
}

/*
 * geomtabinit
 *
 *	set the table of function pointers for the geometry.
 */
geomtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[GEOMETRY] = geomn;
	intersects[GEOMETRY] = geomi;
	tilefuns[GEOMETRY] = (void (*)())NULL;
	checkbbox[GEOMETRY] = TRUE;
	selfshadowing[GEOMETRY] = TRUE;
}
