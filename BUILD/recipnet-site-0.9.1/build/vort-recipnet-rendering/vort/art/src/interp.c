#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern attr	*astackp;
extern hlist	*fhlist;

/*
 * interp3
 *
 *	calculates the interpolated normal for the triangle described
 * by the normals norms. In this case we have to use area (barycentric)
 * coordinates. u and v represent the point we are on and m is the
 * precalculated solution matrix to give us the area coordinates.
 */
interp3(u, v, m, norms, n)
	register float	u, v;
	register mat3x3	*m;
	register vector	*norms, *n;
{
	register float	a1, a2, a3;

	a1 = v * (*m)[0][0] - u * (*m)[0][1] + (*m)[0][2];

	a2 = v * (*m)[1][0] - u * (*m)[1][1] + (*m)[1][2];

	a3 = v * (*m)[2][0] - u * (*m)[2][1] + (*m)[2][2];

	n->x = a1 * norms[1].x + a2 * norms[2].x + a3 * norms[0].x;
	n->y = a1 * norms[1].y + a2 * norms[2].y + a3 * norms[0].y;
	n->z = a1 * norms[1].z + a2 * norms[2].z + a3 * norms[0].z;
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
interp4(p, pn, pnorms, u, v, n)
	polygon	*p;
	vector	*pn, *pnorms;
	float	u, v;
	vector	*n;
{
	register int	i, j, nj, crosses;
	register float	a1, a2, a3;
	register float	m;
	mat3x3		mat;
	float		area;
	uvvec		c, *pnts, tpnts[12];
	vector		norms[3];

	pnts = p->u.verts;

	/*
	 * calculate the centroid
	 */
	
	c.u = (pnts[0].u + pnts[1].u + pnts[2].u + pnts[3].u) / 4;
	c.v = (pnts[0].v + pnts[1].v + pnts[2].v + pnts[3].v) / 4;

	/*
	 * split the mother into triangles 
	 */
	tpnts[0] = pnts[0]; tpnts[1] = pnts[1];

	tpnts[3] = pnts[1]; tpnts[4] = pnts[2];

	tpnts[6] = pnts[2]; tpnts[7] = pnts[3];

	tpnts[9] = pnts[3]; tpnts[10] = pnts[0];

	tpnts[2] = c; tpnts[5] = c; tpnts[8] = c; tpnts[11] = c;

	/*
	 * find which part we have hit
	 */
	for (i = 0; i != 12; i += 3) {
		crosses = 0;
		for (j = i; j != i + 3; j++) {

			nj = (j == i + 2) ? i : j + 1;

			if (tpnts[j].v < v) {
				if (tpnts[nj].v < v)
					continue;
			} else {
				if (tpnts[nj].v > v)
					continue;
			}

			if (tpnts[j].u < u) {
				if (tpnts[nj].u < u) {   /* crossed on left */
					crosses++;
					continue;
				}
			} else if (tpnts[nj].u > u)         /* both to right */
				continue;

			if (tpnts[nj].v == tpnts[j].v)
				continue;

			m = (tpnts[nj].u - tpnts[j].u) / (tpnts[nj].v - tpnts[j].v);
			if (((tpnts[nj].u - u) - m * (tpnts[nj].v - v)) < 0.0)
				crosses++;
		}

		if (crosses & 1)
			break;
	}

	if (i == 12) {
		*n = *pn;
	} else {
		if (p->axis == Y) {
			area = p->n.y;
			mat[0][0] = (tpnts[i + 2].u - tpnts[i + 0].u) / area;
			mat[1][0] = (tpnts[i + 0].u - tpnts[i + 1].u) / area;
			mat[2][0] = (tpnts[i + 1].u - tpnts[i + 2].u) / area;

			mat[0][1] = (tpnts[i + 2].v - tpnts[i + 0].v) / area;
			mat[1][1] = (tpnts[i + 0].v - tpnts[i + 1].v) / area;
			mat[2][1] = (tpnts[i + 1].v - tpnts[i + 2].v) / area;
		} else {
			if (p->axis == X)
				area = p->n.x;
			else
				area = p->n.z;

			mat[0][0] = (tpnts[i + 0].u - tpnts[i + 2].u) / area;
			mat[1][0] = (tpnts[i + 1].u - tpnts[i + 0].u) / area;
			mat[2][0] = (tpnts[i + 2].u - tpnts[i + 1].u) / area;

			mat[0][1] = (tpnts[i + 0].v - tpnts[i + 2].v) / area;
			mat[1][1] = (tpnts[i + 1].v - tpnts[i + 0].v) / area;
			mat[2][1] = (tpnts[i + 2].v - tpnts[i + 1].v) / area;
		}

		mat[0][2] = tpnts[i + 2].u * mat[0][1] - tpnts[i + 2].v * mat[0][0];
		mat[1][2] = tpnts[i + 0].u * mat[1][1] - tpnts[i + 0].v * mat[1][0];
		mat[2][2] = tpnts[i + 1].u * mat[2][1] - tpnts[i + 1].v * mat[2][0];

		a1 = v * mat[0][0] - u * mat[0][1] + mat[0][2];

		a2 = v * mat[1][0] - u * mat[1][1] + mat[1][2];

		a3 = v * mat[2][0] - u * mat[2][1] + mat[2][2];

		switch (i) {
		case 0:
			norms[0] = pnorms[0];
			norms[1] = pnorms[1];
			break;
		case 3:
			norms[0] = pnorms[1];
			norms[1] = pnorms[2];
			break;
		case 6:
			norms[0] = pnorms[2];
			norms[1] = pnorms[3];
			break;
		case 9:
			norms[0] = pnorms[3];
			norms[1] = pnorms[0];
			break;
		}

		n->x = a1 * norms[1].x + a2 * pn->x + a3 * norms[0].x;
		n->y = a1 * norms[1].y + a2 * pn->y + a3 * norms[0].y;
		n->z = a1 * norms[1].z + a2 * pn->z + a3 * norms[0].z;
	}
}

/*
 * interp
 *
 *	returns the interpolated normal to a polygon pg.
 */
void
interp(p, pn, pnorms, l, n)
	polygon	*p;
	vector	*pn, *pnorms, *l, *n;
{
	float	u, v;

	switch (p->axis) {
	case X:
		u = l->y;
		v = l->z;
		break;
	case Y:
		u = l->x;
		v = l->z;
		break;
	case Z:
		u = l->x;
		v = l->y;
		break;
	default:
		fatal("art: bad axis in interp.\n");
	}

	if (p->nsides == 3)
		interp3(u, v, p->u.mat, pnorms, n);
	else if (p->nsides == 4)
		interp4(p, pn, pnorms, u, v, n);
	else {
		n->x = pn->x;
		n->y = pn->y;
		n->z = pn->z;
	}
}
