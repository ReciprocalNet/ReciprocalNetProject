#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * the matrices to assist in calculating the convex hull for
 * the first half and second half of a cubic curve.
 */
matrix  half1 = {
	{ 1 / 8.0, 0.0, 0.0, 0.0 },
	{ 0.0, 1 / 4.0, 0.0, 0.0 },
	{ 0.0, 0.0, 1 / 2.0, 0.0 },
	{ 0.0, 0.0, 0.0, 1.0 }
};

matrix  half2 = {
	{ 1 / 8.0, 0.0, 0.0, 0.0 },
	{ 3 / 8.0, 1 / 4.0, 0.0, 0.0 },
	{ 3 / 8.0, 1 / 2.0, 1 / 2.0, 0.0 },
	{ 1 / 8.0, 1 / 4.0, 1 / 2.0, 1.0 }
};

/*
 * basis matrices...
 */
matrix  bezier = {
	{-1.0,  3.0,    -3.0,   1.0},
	{3.0,   -6.0,   3.0,    0.0},
	{-3.0,  3.0,    0.0,    0.0},
	{1.0,   0.0,    0.0,    0.0}
};

matrix  cardinal = {			 /* Or catmull-rom */
	{-0.5,  1.5,    -1.5,   0.5},
	{1.0,   -2.5,   2.0,    -0.5},
	{-0.5,  0.0,    0.5,    0.0},
	{0.0,   1.0,    0.0,    0.0}
};

matrix  bspline = {
	{-1.0 / 6.0,    3.0 / 6.0,      -3.0 / 6.0,     1.0 / 6.0},
	{3.0 / 6.0,     -6.0 / 6.0,     3.0 / 6.0,      0.0},
	{-3.0 / 6.0,    0.0,            3.0 / 6.0,      0.0},
	{1.0 / 6.0,     4.0 / 6.0,      1.0 / 6.0,      0.0}
};



/*
 * eval_fdu
 *
 *	evaluate the dot product of the partial derivative of the patch function
 * in v and the ray at the point on the surface (u, v).
 */
float
eval_fdu(u, v, M, H)
	float	u, v;
	matrix	M, H;
{
	float	U[4], V[4], T1[4], T2[4];
	int	i, j;

	U[3] = 0;
	U[2] = 1;
	U[1] = 2 * u;
	U[0] = 3 * u * u;

	/*
	 *	U' * M
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += U[j] * M[i][j];
	}

	/*
	 *	U' * M * H
	 */
	for (i = 0; i < 4; i++) {
		T2[i] = 0;
		for (j = 0; j < 4; j++)
			T2[i] += T1[j] * H[i][j];
	}

	/*
	 * 	U' * M * H * transpose(M)
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += T2[j] * M[j][i];
	}

	/*
	 * Now return U' * M * H * transpose(M) * V
	 */
	return(v * (v * (v * T1[0] + T1[1]) + T1[2]) + T1[3]);
}

/*
 * eval_fdv
 *
 *	evaluate the dot product of the partial derivative of the patch function
 * in v and the ray at the point on the surface (u, v).
 */
float
eval_fdv(u, v, M, H)
	float	u, v;
	matrix	M, H;
{
	float	U[4], V[4], T1[4], T2[4];
	int	i, j;

	U[3] = 1;
	U[2] = u;
	U[1] = u * u;
	U[0] = U[1] * u;

	/*
	 *	U * M
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += U[j] * M[i][j];
	}

	/*
	 *	U * M * H
	 */
	for (i = 0; i < 4; i++) {
		T2[i] = 0;
		for (j = 0; j < 4; j++)
			T2[i] += T1[j] * H[i][j];
	}

	/*
	 * 	U * M * H * transpose(M)
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += T2[j] * M[j][i];
	}

	/*
	 * Now return U * M * H * transpose(M) * V'
	 */
	return(v * (3.0 * v * T1[0] + 2.0 * T1[1]) + T1[2]);
}

/*
 * eval_f
 *
 *	evaluate the dot product of the patch and the ray at the
 * point on the surface (u, v).
 */
float
eval_f(u, v, M, H)
	float	u, v;
	matrix	M, H;
{
	float 	U[4], V[4], T1[4], T2[4];
	int	i, j;

	U[3] = 1;
	U[2] = u;
	U[1] = u * u;
	U[0] = U[1] * u;

	/*
	 *	U * M
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += U[j] * M[i][j];
	}

	/*
	 *	U * M * H
	 */
	for (i = 0; i < 4; i++) {
		T2[i] = 0;
		for (j = 0; j < 4; j++)
			T2[i] += T1[j] * H[i][j];
	}

	/*
	 * 	U * M * H * transpose(M)
	 */
	for (i = 0; i < 4; i++) {
		T1[i] = 0;
		for (j = 0; j < 4; j++)
			T1[i] += T2[j] * M[j][i];
	}

	/*
	 * Now return U * M * H * transpose(M) * V
	 */
	return(v * (v * (v * T1[0] + T1[1]) + T1[2]) + T1[3]);
}

/*
 * these are common to the top intersection routine and the
 * final one. They only need to be calculated once per ray.
 */
static matrix	H1, H2;
static vector	P1, P2;
static float	D1, D2;

/*
 * intersect
 *
 *	calculate the rays intersection with the patch.
 */
hlist *
intersect(r, o, u, v, last)
	ray	*r;
	object	*o;
	float	u, v;
	hlist	**last;
{
	vector		c;
	int		i, j;
	register hlist	*hitlist, *hp;
	register float	a, b, d;
	float		E1, E2;
	matrix		M;
	patch		*pch;
	float		x, y, err, lasterr;
	float		E1du, E1dv, E2du, E2dv;

	pch = o->obj.pch;

	for (i = 0; i != 50; i++) {
		E1 = eval_f(u, v, pch->basis, H1) - D1;
		E2 = eval_f(u, v, pch->basis, H2) - D2;

		if ((err = fabs(E1) + fabs(E2)) < 0.01)
			break;
		
		if (i > 5 && err > lasterr)
			break;

		E1du = eval_fdu(u, v, pch->basis, H1);
		E1dv = eval_fdv(u, v, pch->basis, H1);

		E2du = eval_fdu(u, v, pch->basis, H2);
		E2dv = eval_fdv(u, v, pch->basis, H2);
		
		u += (E2 * E1dv - E1 * E2dv) / (E1du * E2dv - E1dv * E2du);
		v += (E2 * E1du - E1 * E2du) / (E1dv * E2du - E1du * E2dv);

		lasterr = err;
	}

	if (err < 0.01) {
		if (u < 0.0 || u > 1.0)
			return((hlist *)NULL);

		if (v < 0.0 || v > 1.0)
			return((hlist *)NULL);

		fetch(hitlist);

		hitlist->obj = o;

		pch->u = u;
		pch->v = v;

		x = fabs(r->dir.x);
		y = fabs(r->dir.y);
		if (x > y)
			if (x > fabs(r->dir.z))
				hitlist->t = (eval_f(u, v, pch->basis, pch->geomx) - r->org.x) / r->dir.x;
			else
				hitlist->t = (eval_f(u, v, pch->basis, pch->geomz) - r->org.z) / r->dir.z;
		else
			if (y > fabs(r->dir.z))
				hitlist->t = (eval_f(u, v, pch->basis, pch->geomy) - r->org.y) / r->dir.y;
			else
				hitlist->t = (eval_f(u, v, pch->basis, pch->geomz) - r->org.z) / r->dir.z;
		
		if (hitlist->t < tolerance) {
			release(hitlist);
			return((hlist *)NULL);
		}

		hitlist->nxt = (hlist *)NULL;
		*last = hitlist;

		return(hitlist);
	}

	return((hlist *)NULL);
}

/*
 * findnode
 *
 *	find the subdivision branch node closest to the patch, that
 * the ray intersects. We use the u and v values for this node to give
 * a starting value for the root solver.
 */
hlist *
findnode(r, root, o, last)
	ray	*r;
	subnode	*root;
	object	*o;
	hlist	**last;
{
	hlist	*hit;
	float	org, dir;

	hit = (hlist *)NULL;

	if (root->type == PLEAF)
		return(intersect(r, o,
			(root->u.leaf.minu + root->u.leaf.maxu) / 2.0,
			(root->u.leaf.minv + root->u.leaf.maxv) / 2.0,
			last));
	else {
		if (root->type == PX) {
			org = r->org.x;
			dir = r->dir.x;
		} else if (root->type == PY) {
			org = r->org.y;
			dir = r->dir.y;
		} else if (root->type == PZ) {
			org = r->org.z;
			dir = r->dir.z;
		}

		if (org <= root->u.branch.splitval) {
			if (!missedbbox(r, &root->u.branch.left->bb))
				hit = findnode(r, root->u.branch.left, o, last);
			if (dir >= 0.0 && hit == (hlist *)NULL && !missedbbox(r, &root->u.branch.right->bb))
				return(findnode(r, root->u.branch.right, o, last));
		} else {
			if (!missedbbox(r, &root->u.branch.right->bb))
				hit = findnode(r, root->u.branch.right, o, last);
			if ((dir <= 0.0 || org < root->u.branch.left->bb.max[root->type]) && hit == (hlist *)NULL && !missedbbox(r, &root->u.branch.left->bb))
				return(findnode(r, root->u.branch.left, o, last));
		}
	}

	return(hit);
}

/*
 * patchi
 *
 *	returns a list of intersection points for the ray r and patch o.
 */
hlist *
patchi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	subnode	*sn;
	patch	*pch;
	vector	P;
	int	i, j;

	pch = o->obj.pch;

	/*
	 * calculate two planes:
	 * P1.(x, y, z) = D1 and P2.(x, y, z) = D2
	 *
	 *	the line along which this intersect represents the ray.
	 */
	xprod(P1, r->org, r->dir);
	normalise(P1);

	D1 = dprod(P1, r->org);

	xprod(P2, P1, r->dir);
	normalise(P2);

	D2 = dprod(P2, r->org);

	/*
	 * Calculate the coefficients matrices. These are made up of
	 * the dot products of the control points and P1 and P2.
	 */
	
	for (i = 0; i != 4; i++)
		for (j = 0; j != 4; j++) {
			P.x = pch->geomx[i][j];
			P.y = pch->geomy[i][j];
			P.z = pch->geomz[i][j];
			H1[i][j] = dprod(P, P1);
			H2[i][j] = dprod(P, P2);
		}
	
	return(findnode(r, o->obj.pch->tree, o, last));
}

/*
 * patchn
 *
 *	returns the normal to the patch s
 */
void
patchn(n, l, o)
	register vector	*n;
	register vector	*l;
	register object	*o;
{
	vector	v1, v2;
	float	u, v;
	patch	*pch;

	pch = o->obj.pch;

	u = pch->u;
	v = pch->v;

	v1.x = eval_fdu(u, v, pch->basis, pch->ogeomx);
	v1.y = eval_fdu(u, v, pch->basis, pch->ogeomy);
	v1.z = eval_fdu(u, v, pch->basis, pch->ogeomz);

	v2.x = eval_fdv(u, v, pch->basis, pch->ogeomx);
	v2.y = eval_fdv(u, v, pch->basis, pch->ogeomy);
	v2.z = eval_fdv(u, v, pch->basis, pch->ogeomz);

	xprod(*n, v1, v2);
}

/*
 * patchc
 *
 *	return the color of a patch o at a the intersection point l.
 *
 */
void
patchc(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	pixel	*pcol;
	int	type;
{
	int	w, h, indx;
	tiletxt	*tp;

	tp = txt->u.t;

	w = o->obj.pch->u * tp->scalew;
	h = o->obj.pch->v * tp->scaleh;

	indx = (w % tp->pixw + (h % tp->pixh) * tp->pixw) * 3;

	pcol->r = (unsigned char)tp->map[indx] / 255.0;
	pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
	pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
}

/*
 * getnodebbox
 *
 *	calculate the bounding box for the set of control
 * points described in geomx, geomy, and geomz.
 */
getnodebbox(bb, geomx, geomy, geomz)
	bbox	*bb;
	matrix	geomx, geomy, geomz;
{
	int	i, j;
	float	fact;

	bb->max[X] = bb->min[X] = geomx[0][0];
	bb->max[Y] = bb->min[Y] = geomy[0][0];
	bb->max[Z] = bb->min[Z] = geomz[0][0];

	for (i = 0; i != 4; i++)
		for (j = 0; j != 4; j++) {
			if (bb->max[X] < geomx[i][j])
				bb->max[X] = geomx[i][j];
			else if (bb->min[X] > geomx[i][j])
				bb->min[X] = geomx[i][j];

			if (bb->max[Y] < geomy[i][j])
				bb->max[Y] = geomy[i][j];
			else if (bb->min[Y] > geomy[i][j])
				bb->min[Y] = geomy[i][j];

			if (bb->max[Z] < geomz[i][j])
				bb->max[Z] = geomz[i][j];
			else if (bb->min[Z] > geomz[i][j])
				bb->min[Z] = geomz[i][j];
		}
}

/*
 * subdivide
 *
 *	subdivide the patch area in root to calculate new geometry
 * points to give the bounding boxes for the two smaller patches.
 * The sudivision keeps going until the level of a leaf node equals
 * maxsublevel.
 */
subdivide(root, geomx, geomy, geomz, basis, invbasis, minu, maxu, minv, maxv, maxsublevel, level)
	subnode	*root;
	matrix	geomx, geomy, geomz;
	matrix	basis, invbasis;
	float	minu, maxu, minv, maxv;
	int	maxsublevel, level;
{
	subnode	*left, *right;
	matrix	ngeomx, ngeomy, ngeomz;
	matrix	tmp1, tmp2;
	float	xgap, ygap, zgap;

	if (level == maxsublevel)
		return;			/* finished */

	if (level & 1) {		/* split in v */

		root->u.branch.left = left = (subnode *)smalloc(sizeof(subnode));

		mmult4(tmp1, invbasis, half1);
		mmult4(tmp2, tmp1, basis);

		mmult4(ngeomx, tmp2, geomx);
		mmult4(ngeomy, tmp2, geomy);
		mmult4(ngeomz, tmp2, geomz);

		getnodebbox(&left->bb, ngeomx, ngeomy, ngeomz);

		left->type = PLEAF;
		left->u.leaf.minu = minu;
		left->u.leaf.maxu = maxu;
		left->u.leaf.minv = minv;
		left->u.leaf.maxv = (minv + maxv) / 2.0;

		subdivide(left,
			ngeomx, ngeomy, ngeomz,
			basis, invbasis,
			minu, maxu,
			minv, (maxv + minv) / 2.0,
			maxsublevel, level + 1);

		root->u.branch.right = right = (subnode *)smalloc(sizeof(subnode));

		mmult4(tmp1, invbasis, half2);
		mmult4(tmp2, tmp1, basis);

		mmult4(ngeomx, tmp2, geomx);
		mmult4(ngeomy, tmp2, geomy);
		mmult4(ngeomz, tmp2, geomz);

		getnodebbox(&right->bb, ngeomx, ngeomy, ngeomz);

		right->type = PLEAF;
		right->u.leaf.minu = minu;
		right->u.leaf.maxu = maxu;
		right->u.leaf.minv = (minv + maxv) / 2.0;
		right->u.leaf.maxv = maxv;

		subdivide(right,
			ngeomx, ngeomy, ngeomz,
			basis, invbasis,
			minu, maxu,
			(maxv + minv) / 2.0, maxv,
			maxsublevel, level + 1);

	} else {		/* split in u */
		root->u.branch.left = left = (subnode *)smalloc(sizeof(subnode));

		mmult4(tmp1, invbasis, half1);
		mmult4(tmp2, tmp1, basis);

		transpose(tmp1, tmp2);

		mmult4(ngeomx, geomx, tmp1);
		mmult4(ngeomy, geomy, tmp1);
		mmult4(ngeomz, geomz, tmp1);

		getnodebbox(&left->bb, ngeomx, ngeomy, ngeomz);

		left->type = PLEAF;
		left->u.leaf.minu = minu;
		left->u.leaf.maxu = (minu + maxu) / 2.0;
		left->u.leaf.minv = minv;
		left->u.leaf.maxv = maxv;

		subdivide(left,
			ngeomx, ngeomy, ngeomz,
			basis, invbasis,
			minu, (maxu + minu) / 2.0,
			minv, maxv,
			maxsublevel, level + 1);


		root->u.branch.right = right = (subnode *)smalloc(sizeof(subnode));

		mmult4(tmp1, invbasis, half2);
		mmult4(tmp2, tmp1, basis);

		transpose(tmp1, tmp2);

		mmult4(ngeomx, geomx, tmp1);
		mmult4(ngeomy, geomy, tmp1);
		mmult4(ngeomz, geomz, tmp1);

		getnodebbox(&right->bb, ngeomx, ngeomy, ngeomz);

		right->type = PLEAF;
		right->u.leaf.minu = (minu + maxu) / 2.0;
		right->u.leaf.maxu = maxu;
		right->u.leaf.minv = minv;
		right->u.leaf.maxv = maxv;

		subdivide(right,
			ngeomx, ngeomy, ngeomz,
			basis, invbasis,
			(maxu + minu) / 2.0, maxu,
			minv, maxv,
			maxsublevel, level + 1);
	}

	/*
	 * recalculate bounding box for using the bounding boxes of
	 * the left and right nodes.
	 */
	root->bb = left->bb;

	if (root->bb.min[X] > right->bb.min[X])
		root->bb.min[X] = right->bb.min[X];
	if (root->bb.max[X] < right->bb.max[X])
		root->bb.max[X] = right->bb.max[X];

	if (root->bb.min[Y] > right->bb.min[Y])
		root->bb.min[Y] = right->bb.min[Y];
	if (root->bb.max[Y] < right->bb.max[Y])
		root->bb.max[Y] = right->bb.max[Y];

	if (root->bb.min[Z] > right->bb.min[Z])
		root->bb.min[Z] = right->bb.min[Z];
	if (root->bb.max[Z] < right->bb.max[Z])
		root->bb.max[Z] = right->bb.max[Z];

	/*
	 * find a splitting plane.
	 */
	xgap = fabs(left->bb.min[X] - right->bb.min[X]);
	ygap = fabs(left->bb.min[Y] - right->bb.min[Y]);
	zgap = fabs(left->bb.min[Z] - right->bb.min[Z]);

	if (xgap > ygap)
		if (xgap > zgap)
			root->type = PX;
		else
			root->type = PZ;
	else
		if (ygap > zgap)
			root->type = PY;
		else
			root->type = PX;

	switch (root->type) {
	case PX:
		if (left->bb.min[X] > right->bb.min[X]) {
			root->u.branch.right = left;
			root->u.branch.left = right;
		}
		root->u.branch.splitval = root->u.branch.right->bb.min[X];
		break;
	case PY:
		if (left->bb.min[Y] > right->bb.min[Y]) {
			root->u.branch.right = left;
			root->u.branch.left = right;
		}
		root->u.branch.splitval = root->u.branch.right->bb.min[Y];
		break;
	case PZ:
		if (left->bb.min[Z] > right->bb.min[Z]) {
			root->u.branch.right = left;
			root->u.branch.left = right;
		}
		root->u.branch.splitval = root->u.branch.right->bb.min[Z];
		break;
	default:
		fatal("subdivide: unknown type in switch.\n");
	}
}

/*
 * patchinit
 *
 *	initialise the function pointers and fields for a patch object,
 * returning its pointer.
 */
void
patchinit(o, d)
	object	*o;
	details *d;
{
	details	*ld;
	vector	v1, v2;
	patch	*ptch;
	subnode	*root;
	float	radius, minv, maxv;
	matrix	invbasis;
	matrix	*m;
	int	i, j, maxsublevel;

	o->obj.pch = ptch = (patch *)smalloc(sizeof(patch));

	mcpy4(ptch->basis, bezier);

	maxsublevel = 6;

	while (d != (details *)NULL) {
		switch (d->type) {
		case GEOMX:
			m = d->u.trans.m;
			maxv = minv = (*m)[0][0];
			for (i = 0; i != 4; i++)
				for (j = 0; j != 4; j++) {
					ptch->ogeomx[i][j] = (*m)[i][j];
					ptch->geomx[i][j] = (*m)[i][j];
					if (maxv < ptch->geomx[i][j])
						maxv = ptch->geomx[i][j];
					else if (minv > ptch->geomx[i][j])
						minv = ptch->geomx[i][j];
				}
			
			o->bb.max[X] = maxv;
			o->bb.min[X] = minv;

			free(m);
			break;
		case GEOMY:
			m = d->u.trans.m;
			maxv = minv = (*m)[0][0];
			for (i = 0; i != 4; i++)
				for (j = 0; j != 4; j++) {
					ptch->ogeomy[i][j] = (*m)[i][j];
					ptch->geomy[i][j] = (*m)[i][j];
					if (maxv < ptch->geomy[i][j])
						maxv = ptch->geomy[i][j];
					else if (minv > ptch->geomy[i][j])
						minv = ptch->geomy[i][j];
				}
			
			o->bb.max[Y] = maxv;
			o->bb.min[Y] = minv;

			free(m);
			break;
		case GEOMZ:
			m = d->u.trans.m;
			maxv = minv = (*m)[0][0];
			for (i = 0; i != 4; i++)
				for (j = 0; j != 4; j++) {
					ptch->ogeomz[i][j] = (*m)[i][j];
					ptch->geomz[i][j] = (*m)[i][j];
					if (maxv < ptch->geomz[i][j])
						maxv = ptch->geomz[i][j];
					else if (minv > ptch->geomz[i][j])
						minv = ptch->geomz[i][j];
				}
			
			o->bb.max[Z] = maxv;
			o->bb.min[Z] = minv;

			free(m);
			break;
		case BASIS:
			if (strcmp(d->u.s, "bezier") == 0)
				mcpy4(ptch->basis, bezier);
			else if (strcmp(d->u.s, "cardinal") == 0)
				mcpy4(ptch->basis, cardinal);
			else if (strcmp(d->u.s, "bspline") == 0)
				mcpy4(ptch->basis, bspline);
			else
				fatal("art: unknown basis in patch.\n");
			break;
		case MAXSUBLEVEL:
			maxsublevel = d->u.i;
			break;
		default:
			warning("art: illegal field in patch ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	/*
	 * transform patch into ray space and calculate 
	 */
	calctransforms(mstackp);

	for (i = 0; i != 4; i++)
		for (j = 0; j != 4; j++) {
			v1.x = ptch->geomx[i][j];
			v1.y = ptch->geomy[i][j];
			v1.z = ptch->geomz[i][j];
			vmmult(v2, v1, mstackp->d.obj2ray);
			ptch->geomx[i][j] = v2.x;
			ptch->geomy[i][j] = v2.y;
			ptch->geomz[i][j] = v2.z;
		}

	/*
	 * set up the root node for the patch subdivision tree and
	 * calculate the bounding boxes for a few subdivisions of
	 * the patch.
	 */

	ptch->tree = root = (subnode *)smalloc(sizeof(subnode));

	root->type = PLEAF;
	root->bb = o->bb;
	root->u.leaf.minu = 0.0;
	root->u.leaf.maxu = 1.0;
	root->u.leaf.minv = 0.0;
	root->u.leaf.maxv = 1.0;

	minv4(invbasis, ptch->basis);
	for (i = 0; i != 4; i++)
		for (j = 0; j != 4; j++)
			if (invbasis[i][j] < 0.0001)
				invbasis[i][j] = 0;

	/*
	 * subdivide and get the bounding box...
	 */
	subdivide(root, ptch->geomx, ptch->geomy, ptch->geomz, ptch->basis, invbasis, 0.0, 1.0, 0.0, 1.0, maxsublevel, 0);

	o->bb = root->bb;

	calctransforms(mstackp);

	setattributes(o);
}

/*
 * patchtabinit
 *
 *	set the table of function pointers for the patch.
 */
patchtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[PATCH] = patchn;
	intersects[PATCH] = patchi;
	tilefuns[PATCH] = patchc;
	checkbbox[PATCH] = TRUE;
	selfshadowing[PATCH] = FALSE;
}
