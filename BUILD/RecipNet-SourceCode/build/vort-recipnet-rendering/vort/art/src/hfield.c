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
extern hlist	*streei();

extern int	maxtreedepth;

/*
 * structure for communicating convex weights, pointer, and
 * the distance to the triangle we hit.
 */
struct {
	float		t;
	float		w1, w2;
	htriangle	*obj;
} hitdata;

/*
 * origin and direction for current ray
 */
static float	orgs[DIMS], dirs[DIMS];

/*
 * z values, size, and various other values for the heightfield we
 * are intersecting
 */
static int	xfieldsize;
static int	yfieldsize;
static int	fieldsize;
static float	*zvals;
static trilist	*tris;
static int	reset;
static int	freecount;
static nodelist *treenodes, *freenodes;
static vector	*norms;

/*
 * maketriangle
 *
 *	make a trianglular polygon from the vertices a, b, and c.
 */
static void
maketriangle(tri, a, b, c)
	htriangle	*tri;
	vector		*a, *b, *c;
{
	vector		v, v1, v2, vt[3];
	int		i;
	char		buf[MESLEN];
	matrix		m;
	float		area, maxx, maxy, maxz, minx, miny, minz;

	vt[0].x = a->x; vt[0].y = a->y; vt[0].z = a->z;
	vt[1].x = b->x; vt[1].y = b->y; vt[1].z = b->z;
	vt[2].x = c->x; vt[2].y = c->y; vt[2].z = c->z;

	vsub(v1, vt[1], vt[0]);
	vsub(v2, vt[2], vt[1]);

	xprod(tri->n, v1, v2);

	if (fabs(tri->n.x) > fabs(tri->n.y))
		if (fabs(tri->n.x) > fabs(tri->n.z))
			tri->axis = X;
		else
			tri->axis = Z;
	else
		if (fabs(tri->n.y) > fabs(tri->n.z))
			tri->axis = Y;
		else
			tri->axis = Z;

	if (tri->n.x == 0.0 && tri->n.y == 0.0 && tri->n.z == 0.0) {
		sprintf(buf, "invalid polygon in heightfield\n");
		fatal(buf);
		return;
	}

	/*
	 * as tri->n isn't normalised the magnitude of the
	 * normal in the part of 3 Space we ignore represents
	 * the area of the projected triangle. Dividing through
	 * by the component takes care of sign trouble we could
	 * have in the intersection test so we don't worry about
	 * taking the magnitude...
	 */
	switch (tri->axis) {
	case X:
		area = tri->n.x;
		m[0][0] = (vt[0].y - vt[2].y) / area;
		m[1][0] = (vt[1].y - vt[0].y) / area;

		m[0][1] = (vt[0].z - vt[2].z) / area;
		m[1][1] = (vt[1].z - vt[0].z) / area;

		m[0][2] = vt[2].y * m[0][1] - vt[2].z * m[0][0];
		m[1][2] = vt[0].y * m[1][1] - vt[0].z * m[1][0];
		break;
	case Y:
		area = tri->n.y;
		m[0][0] = (vt[2].x - vt[0].x) / area;
		m[1][0] = (vt[0].x - vt[1].x) / area;

		m[0][1] = (vt[2].z - vt[0].z) / area;
		m[1][1] = (vt[0].z - vt[1].z) / area;

		m[0][2] = vt[2].x * m[0][1] - vt[2].z * m[0][0];
		m[1][2] = vt[0].x * m[1][1] - vt[0].z * m[1][0];
		break;
	case Z:
		area = tri->n.z;
		m[0][0] = (vt[0].x - vt[2].x) / area;
		m[1][0] = (vt[1].x - vt[0].x) / area;

		m[0][1] = (vt[0].y - vt[2].y) / area;
		m[1][1] = (vt[1].y - vt[0].y) / area;

		m[0][2] = vt[2].x * m[0][1] - vt[2].y * m[0][0];
		m[1][2] = vt[0].x * m[1][1] - vt[0].y * m[1][0];
		break;
	default:
		fatal("art: bad axis in maketriangle.\n");
	}

	normalise(tri->n);

	tri->cnst = -dprod(tri->n, vt[0]);

	cp2x3(tri->mat, m);

	minx = maxx = vt[0].x;
	miny = maxy = vt[0].y;
	minz = maxz = vt[0].z;
	for (i = 1; i != 3; i++) {
		minmax(minx, maxx, vt[i].x);
		minmax(miny, maxy, vt[i].y);
		minmax(minz, maxz, vt[i].z);
	}

	tri->bb.min[X] = minx - tolerance;
	tri->bb.min[Y] = miny - tolerance;
	tri->bb.min[Z] = minz - tolerance;

	tri->bb.max[X] = maxx + tolerance;
	tri->bb.max[Y] = maxy + tolerance;
	tri->bb.max[Z] = maxz + tolerance;
}

/*
 * maketriangles
 *
 *	find the space for four triangles and set them up using maketriangle.
 * We have a cache of MAXTRIS triangles set here and it is used on a
 * cyclic basis.
 */
maketriangles(hfnode, a, b, c, d, ind)
	hnode	*hfnode;
	vector	*a, *b, *c, *d;
	int	ind;
{
	trilist	*tri;
	vector	e;

	tri = tris;
	tris = tri->nxt;

	if (tri->parent != (hnode *)NULL
	   && tri->parent->type == HLEAF
	   && tri->parent->u.triangles == tri->tris)
		tri->parent->u.triangles = (htriangle *)NULL;

	e.x = (a->x + b->x + c->x + d->x) / 4.0;
	e.y = (a->y + b->y + c->y + d->y) / 4.0;
	e.z = (a->z + b->z + c->z + d->z) / 4.0;

	maketriangle(&tri->tris[0], a, b, &e);
	maketriangle(&tri->tris[1], b, c, &e);
	maketriangle(&tri->tris[2], c, d, &e);
	maketriangle(&tri->tris[3], d, a, &e);

	if (norms != (vector *)NULL) {
		tri->tris[0].norms[0] = &norms[ind];
		tri->tris[0].norms[1] = &norms[ind + 1];
		tri->tris[0].norms[2] = &tri->cnorm;

		tri->tris[1].norms[0] = &norms[ind + 1];
		tri->tris[1].norms[1] = &norms[ind + xfieldsize + 1];
		tri->tris[1].norms[2] = &tri->cnorm;

		tri->tris[2].norms[0] = &norms[ind + xfieldsize + 1];
		tri->tris[2].norms[1] = &norms[ind + xfieldsize];
		tri->tris[2].norms[2] = &tri->cnorm;

		tri->tris[3].norms[0] = &norms[ind + xfieldsize];
		tri->tris[3].norms[1] = &norms[ind];
		tri->tris[3].norms[2] = &tri->cnorm;

		tri->cnorm.x = norms[ind].x + norms[ind + 1].x + norms[ind + xfieldsize].x + norms[ind + xfieldsize + 1].x;
		tri->cnorm.y = norms[ind].y + norms[ind + 1].y + norms[ind + xfieldsize].y + norms[ind + xfieldsize + 1].y;
		tri->cnorm.z = norms[ind].z + norms[ind + 1].z + norms[ind + xfieldsize].z + norms[ind + xfieldsize + 1].z;
		normalise(tri->cnorm);
	}

	hfnode->type = HLEAF;
	hfnode->u.triangles = tri->tris;
	tri->parent = hfnode;
}

/*
 * inttri
 *
 *	intersect a triangle, we calculate the barycentric coordinates using
 * the coefficients in the matrix we pre calculated in the initialisation
 * routine. This owes a bit to Didier Badouel from his article in Graphics 
 * Gems.
 */
static int
inttri(r, tri)
	ray		*r;
	htriangle	*tri;
{
	mat2x3		*m;
	hlist		*hp, *hitlist;
	float		t, pu, pv, a1, a2;

	t = dprod(tri->n, r->dir);

	if (t == 0.0)
		return(FALSE);

	t = -(dprod(tri->n, r->org) + tri->cnst) / t;

	if (t < tolerance)
		return(FALSE);

	switch (tri->axis) {
	case X:
		pu = r->org.y + t * r->dir.y;
		if (pu < tri->bb.min[Y] || pu > tri->bb.max[Y])
			return(FALSE);
		pv = r->org.z + t * r->dir.z;
		if (pv < tri->bb.min[Z] || pv > tri->bb.max[Z])
			return(FALSE);
		break;
	case Y:
		pu = r->org.x + t * r->dir.x;
		if (pu < tri->bb.min[X] || pu > tri->bb.max[X])
			return(FALSE);
		pv = r->org.z + t * r->dir.z;
		if (pv < tri->bb.min[Z] || pv > tri->bb.max[Z])
			return(FALSE);
		break;
	case Z:
		pu = r->org.x + t * r->dir.x;
		if (pu < tri->bb.min[X] || pu > tri->bb.max[X])
			return(FALSE);
		pv = r->org.y + t * r->dir.y;
		if (pv < tri->bb.min[Y] || pv > tri->bb.max[Y])
			return(FALSE);
		break;
	default:
		fatal("art: bad axis in inttri.\n");
	}

	a1 = pv * tri->mat[0][0] - pu * tri->mat[0][1] + tri->mat[0][2];
	if (a1 < 0.0 || a1 > 1.0)
		return(FALSE);

	a2 = pv * tri->mat[1][0] - pu * tri->mat[1][1] + tri->mat[1][2];
	if (a2 < 0.0 || a1 + a2 > 1.0)
		return(FALSE);

	hitdata.t = t;
	hitdata.w1 = a1;
	hitdata.w2 = a2;
	hitdata.obj = tri;

	return(TRUE);
}

/*
 * checkleaf
 *
 *	check the objects in a leaf - returning a legal hit if there is
 * one, note in this case we (that of height fields), we don't have to
 * worry about over lapping objects, we just grab the closest one we hit
 * in the first leaf node we score a hit in.
 */
static hlist *
checkleaf(r, hfnode, bb, ind)
	ray	*r;
	hnode	*hfnode;
	bbox	*bb;
	int	ind;
{
	htriangle	*tri;
	float	val, t;
	hlist	*hit, *prevhits;
	vector	a, b, c, d;

	if (hfnode->u.triangles == (htriangle *)NULL) {
		a.x = bb->min[X]; a.y = bb->min[Y]; a.z = zvals[ind];
		b.x = bb->max[X]; b.y = bb->min[Y]; b.z = zvals[ind + 1];
		c.x = bb->max[X]; c.y = bb->max[Y]; c.z = zvals[ind + xfieldsize + 1];
		d.x = bb->min[X]; d.y = bb->max[Y]; d.z = zvals[ind + xfieldsize];
		maketriangles(hfnode, &a, &b, &c, &d, ind);
	}

	tri = hfnode->u.triangles;

	if (inttri(r, tri)) {
		fetch(hit);
		hit->t = hitdata.t;
		hit->type = ind * 4;
		hit->nxt = (hlist *)NULL;
	} else if (inttri(r, tri + 1)) {
		fetch(hit);
		hit->t = hitdata.t;
		hit->type = ind * 4 + 1;
		hit->nxt = (hlist *)NULL;
	} else if (inttri(r, tri + 2)) {
		fetch(hit);
		hit->t = hitdata.t;
		hit->type = ind * 4 + 2;
		hit->nxt = (hlist *)NULL;
	} else if (inttri(r, tri + 3)) {
		fetch(hit);
		hit->t = hitdata.t;
		hit->type = ind * 4 + 3;
		hit->nxt = (hlist *)NULL;
	} else
		hit = (hlist *)NULL;

	return(hit);
}

/*
 * split
 *
 *	split a heightfield node into two branches, this is done
 * along either x or y considering the value of xsplit
 */
static void
split(hfnode, xstart, xend, ystart, yend, xsplit)
	hnode	*hfnode;
	int	xstart, xend, ystart, yend;
	int	xsplit;
{
	float		z, minz1, minz2, maxz1, maxz2;
	static int	count;
	hnode		*subtree;
	nodelist	*nds, *nxtnds, *usednodes;
	int		mid, ind, i, j;

	subtree = freenodes->subtree;
	if (freenodes->parent != (hnode *)NULL
	   && freenodes->parent->type == HBRANCH
	   && freenodes->parent->u.subtree == freenodes->subtree)
		freenodes->parent->u.subtree = (hnode *)NULL;
	
	freenodes->parent = hfnode;

	if ((nds = freenodes) == (nodelist *)NULL)
		fatal("ran out of nodes in heightfield - increase MAXNODES");

	freenodes = nds->nxt;

	nds->nxt = treenodes;
	treenodes = nds;

	freecount--;

	if (freecount == RESET_CUTOFF)
		reset = TRUE;

	if (freecount == COLLECT_CUTOFF) {
		/*
		 * garbage collect...
		 */
		usednodes = (nodelist *)NULL;

		for (nds = treenodes; nds != (nodelist *)NULL; nds = nxtnds) {
			nxtnds = nds->nxt;
			if (!nds->parent->used) {
				nds->parent->u.subtree = (hnode *)NULL;
				nds->nxt = freenodes;
				freenodes = nds;
				freecount++;
			} else {
				nds->nxt = usednodes;
				usednodes = nds;
			}
		}

		treenodes = usednodes;
	}

	if (xsplit) {
		mid = xstart + (xend - xstart) / 2;

		ind = ystart * xfieldsize + xstart;
		minz1 = maxz1 = zvals[ind];

		ind = ystart * xfieldsize + mid;
		minz2 = maxz2 = zvals[ind];

		for (j = ystart; j <= yend; j++) {
			ind = j * xfieldsize;

			for (i = xstart; i <= mid; i++) {
				z = zvals[ind + i];
				if (z > maxz1)
					maxz1 = z;
				else if (z < minz1)
					minz1 = z;
			}

			for (i = mid; i <= xend; i++) {
				z = zvals[ind + i];
				if (z > maxz2)
					maxz2 = z;
				else if (z < minz2)
					minz2 = z;
			}
		}
	} else {
		mid = ystart + (yend - ystart) / 2;

		ind = ystart * xfieldsize + xstart;
		minz1 = maxz1 = zvals[ind];

		ind = mid * xfieldsize + xstart;
		minz2 = maxz2 = zvals[ind];

		for (j = ystart; j <= mid; j++) {
			ind = j * xfieldsize;

			for (i = xstart; i <= xend; i++) {
				z = zvals[ind + i];
				if (z > maxz1)
					maxz1 = z;
				else if (z < minz1)
					minz1 = z;
			}
		}

		for (j = mid; j <= yend; j++) {
			ind = j * xfieldsize;

			for (i = xstart; i <= xend; i++) {
				z = zvals[ind + i];
				if (z > maxz2)
					maxz2 = z;
				else if (z < minz2)
					minz2 = z;
			}
		}
	}

	subtree->minz = minz1;
	subtree->maxz = maxz1;
	subtree->u.subtree = (hnode *)NULL;

	(subtree + 1)->minz = minz2;
	(subtree + 1)->maxz = maxz2;
	(subtree + 1)->u.subtree = (hnode *)NULL;

	hfnode->type = HBRANCH;
	hfnode->u.subtree = subtree;
}

/*
 * inthfield
 *
 *	intersect the ray with the height field, we do this by breaking
 * the field up ala a quadtree, and then test the ray against the triangles
 * making up a leaf node.
 */
hlist *
inthfield(r, hfnode, xstart, xend, ystart, yend)
	ray	*r;
	hnode	*hfnode;
	int	xstart, xend, ystart, yend;
{
	bbox	bb;
	hlist	*hit;
	int	mid;

	hit = (hlist *)NULL;

	bb.min[X] = ((float)xstart / (float)(fieldsize - 1));
	bb.max[X] = ((float)xend / (float)(fieldsize - 1));
	bb.min[Y] = ((float)ystart / (float)(fieldsize - 1));
	bb.max[Y] = ((float)yend / (float)(fieldsize - 1));
	bb.min[Z] = hfnode->minz;
	bb.max[Z] = hfnode->maxz;

	hfnode->used = TRUE;

	if (!missedbbox(r, &bb)) {
		
		if (xend - xstart == 1 && yend - ystart == 1) {
			hit = checkleaf(r, hfnode, &bb, ystart * xfieldsize + xstart);
		} else if ((xend - xstart) > (yend - ystart)) {

			if (hfnode->u.subtree == (hnode *)NULL)
				split(hfnode, xstart, xend, ystart, yend, TRUE);

			/*
			 * check subtrees
			 */
			mid = (xstart + xend) / 2;
			if (r->org.x <= (bb.min[X] + bb.max[X]) / 2.0) {
				hit = inthfield(r, hfnode->u.subtree, xstart, mid, ystart, yend);
				if (hit == (hlist *)NULL && r->dir.x >= 0.0)
					hit = inthfield(r, hfnode->u.subtree + 1, mid, xend, ystart, yend);
			} else {
				hit = inthfield(r, hfnode->u.subtree + 1, mid, xend, ystart, yend);
				if (hit == (hlist *)NULL && r->dir.x <= 0.0)
					hit = inthfield(r, hfnode->u.subtree, xstart, mid, ystart, yend);
			}
		} else  {

			if (hfnode->u.subtree == (hnode *)NULL)
				split(hfnode, xstart, xend, ystart, yend, FALSE);

			/*
			 * check subtrees
			 */
			mid = (ystart + yend) / 2;
			if (r->org.y <= (bb.min[Y] + bb.max[Y]) / 2.0) {
				hit = inthfield(r, hfnode->u.subtree, xstart, xend, ystart, mid);
				if (hit == (hlist *)NULL && r->dir.y >= 0.0) 
					hit = inthfield(r, hfnode->u.subtree + 1, xstart, xend, mid, yend);
			} else {
				hit = inthfield(r, hfnode->u.subtree + 1, xstart, xend, mid, yend);
				if (hit == (hlist *)NULL && r->dir.y <= 0.0) 
					hit = inthfield(r, hfnode->u.subtree, xstart, xend, ystart, mid);
			}
		}
	}

	return(hit);
}

/*
 * hfieldi
 *
 *	return the intersections between the ray r and heightfield in o.
 */
hlist	*
hfieldi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	ray		nr;
	hlist		*hit;
	heightfield	*hfield;
	nodelist	*nd;

	transray(o, nr, *r);

	hfield = o->obj.hfield;

	xfieldsize = hfield->xsize;
	yfieldsize = hfield->ysize;

	if (xfieldsize > yfieldsize)
		fieldsize = xfieldsize;
	else
		fieldsize = yfieldsize;

	zvals = hfield->zvals;

	tris = hfield->tris;
	treenodes = hfield->treenodes;
	freenodes = hfield->freenodes;
	reset = hfield->reset;
	freecount = hfield->freecount;
	norms = hfield->norms;

	if (reset) {
		for (nd = treenodes; nd != (nodelist *)NULL; nd = nd->nxt)
			nd->parent->used = FALSE;

		reset = hfield->reset = FALSE;
	}

	hit = inthfield(&nr, hfield->hfnode, 0, xfieldsize - 1, 0, yfieldsize - 1);

	hfield->tris = tris;
	hfield->treenodes = treenodes;
	hfield->freenodes = freenodes;
	hfield->reset = reset;
	hfield->freecount = freecount;

	if (hit != (hlist *)NULL) {
		hfield->obj = hitdata.obj;
		hfield->w1 = hitdata.w1;
		hfield->w2 = hitdata.w2;
		hit->obj = o;
	}

	return(hit);
}

/*
 * calcnorm
 *
 *	calculate the normal at the triangle defined by the
 * indexes a, b, and c.
 */
calcnorm(n, a, b, c, z, xsize, ysize)
	vector	*n;
	int	a, b, c;
	float	*z;
	int	xsize, ysize;
{
	vector	v1, v2, v3, va, vb;
	float	fact;

	if (xsize > ysize)
		fact = xsize - 1;
	else
		fact = ysize - 1;

	v1.x = ((float)(a % xsize) / fact);
	v1.y = ((float)(a / ysize) / fact);
	v1.z = z[a];

	v2.x = ((float)(b % xsize) / fact);
	v2.y = ((float)(b / ysize) / fact);
	v2.z = z[b];

	v3.x = ((float)(c % xsize) / fact);
	v3.y = ((float)(c / ysize) / fact);
	v3.z = z[c];

	vsub(va, v2, v1);
	vsub(vb, v3, v2);

	xprod(*n, va, vb);

	normalise(*n);
}

/*
 * getcol
 *
 *	get the colour of a point in a heightfield cell.
 */
getcol(o, index, l, rvcol)
	object		*o;
	int		index;
	vector		*l, *rvcol;
{
	int		ind, x, y;
	float		*cols, *c0, *c1, c2[3], *cextra, avec[3];
	float		w1, w2, w3;
	heightfield	*hfield;
	int		fieldsize, offset;

	hfield = o->obj.hfield;

	cols = hfield->colours;

	ind = index / 4;
	offset = index % 4;

	switch (offset) {
	case 0:
		c0 = &cols[ind * 3];
		c1 = &cols[(ind + 1) * 3];
		break;
	case 1:
		c0 = &cols[(ind + 1) * 3];
		c1 = &cols[(ind + hfield->xsize + 1) * 3];
		break;
	case 2:
		c0 = &cols[(ind + hfield->xsize + 1) * 3];
		c1 = &cols[(ind + hfield->xsize) * 3];
		break;
	case 3:
		c0 = &cols[(ind + hfield->xsize) * 3];
		c1 = &cols[ind * 3];
		break;
	default:
		fatal("getcol: bad value in switch.\n");
	}
		
	c2[X] = (cols[ind * 3] + cols[(ind + hfield->xsize) * 3]
		+ cols[(ind + 1) * 3] + cols[(ind + hfield->xsize + 1) * 3]) / 4.0;

	c2[Y] = (cols[ind * 3 + 1] + cols[(ind + hfield->xsize) * 3 + 1]
		+ cols[(ind + 1) * 3 + 1] + cols[(ind + hfield->xsize + 1) * 3 + 1]) / 4.0;

	c2[Z] = (cols[ind * 3 + 2] + cols[(ind + hfield->xsize) * 3 + 2]
		+ cols[(ind + 1) * 3 + 2] + cols[(ind + hfield->xsize + 1) * 3 + 2]) / 4.0;

	w1 = hfield->w1;
	w2 = hfield->w2;
	w3 = 1.0 - (w1 + w2);

	rvcol->x = w1 * c1[X] + w2 * c2[X] + w3 * c0[X];
	rvcol->y = w1 * c1[Y] + w2 * c2[Y] + w3 * c0[Y];
	rvcol->z = w1 * c1[Z] + w2 * c2[Z] + w3 * c0[Z];
}

/*
 * readcolourfield
 *
 *	read in the height field data, setting up a sub tree.
 */
readcolourfield(name, cfield)
	char	*name;
	float	**cfield;
{
	FILE	*fp;
	int	xsize, ysize;
	float	*cols;
	char	buf[MESLEN];

	if ((fp = fopen(name, "r")) == NULL) {
		sprintf(buf, "can't open colourfield data file %s.\n", name);
		fatal(buf);
	}

	if (fscanf(fp, "COLORFIELD %d %d ", &xsize, &ysize) == 0) {
		fread(&xsize, sizeof(xsize), 1, fp);
		ysize = xsize;
	}

	*cfield = (float *)scalloc(sizeof(float), xsize * ysize * 3);

	fread(*cfield, sizeof(float), xsize * ysize * 3, fp);
}

/*
 * readheightfield
 *
 *	read in the height field data, setting up a sub tree.
 */
readheightfield(name, hfield)
	char		*name;
	heightfield	**hfield;
{
	polygon		*pg;
	vector		v, v1, v2, *norms, **normtab;
	vector		xv, yv, zv;
	float		vx, vz, radius, area;
	char		buf[MESLEN];
	details		*ld, *d1, *ld1, *headd;
	surface		*news;
	hnode		*hfnode;
	nodelist	*nodes;
	trilist		*tris;
	int		xsize, ysize, i, j, ind, count, phong;
	float		*x, *y, *z, minz, maxz;
	FILE		*fp;
	vector		n;

	if ((fp = fopen(name, "r")) == NULL) {
		sprintf(buf, "can't open heightfield data file %s.\n", name);
		fatal(buf);
	}

	if (fscanf(fp, "HEIGHTFIELD %d %d ", &xsize, &ysize) == 0) {
		fread(&xsize, sizeof(xsize), 1, fp);
		ysize = xsize;
	}

	z = (float *)scalloc(sizeof(float), xsize * ysize);

	fread(z, sizeof(float), xsize * ysize, fp);

	minz = maxz = z[0];
	for (i = 1; i != xsize * ysize; i++)
		if (z[i] < minz)
			minz = z[i];
		else if (z[i] > maxz)
			maxz = z[i];

	*hfield = (heightfield *)smalloc(sizeof(heightfield));

	(*hfield)->zvals = z;
	(*hfield)->xsize = xsize;
	(*hfield)->ysize = ysize;

	(*hfield)->hfnode = hfnode = (hnode *)smalloc(sizeof(hnode));
	(*hfield)->freenodes = nodes = (nodelist *)smalloc(sizeof(nodelist) * MAXNODES);
	(*hfield)->treenodes = (nodelist *)NULL;
	(*hfield)->tris = tris = (trilist *)smalloc(sizeof(trilist) * MAXTRIS);

	for (i = 0; i != MAXNODES; i++) {
		nodes[i].subtree[0].type = HBRANCH;
		nodes[i].subtree[0].used = FALSE;
		nodes[i].subtree[0].u.subtree = (hnode *)NULL;

		nodes[i].subtree[1].type = HBRANCH;
		nodes[i].subtree[1].used = FALSE;
		nodes[i].subtree[1].u.subtree = (hnode *)NULL;

		nodes[i].parent = (hnode *)NULL;
		nodes[i].nxt = &nodes[i + 1];
	}

	nodes[MAXNODES - 1].nxt = (nodelist *)NULL;

	(*hfield)->freecount = MAXNODES;

	for (i = 0; i != MAXTRIS; i++) {
		tris[i].parent = (hnode *)NULL;
		tris[i].nxt = &tris[i + 1];
	}

	tris[MAXTRIS - 1].nxt = tris;

	hfnode->minz = minz;
	hfnode->maxz = maxz;
	hfnode->u.subtree = (hnode *)NULL;

	if (astackp->d.options & PHONGSHADING) {
		(*hfield)->norms = norms = (vector *)scalloc(sizeof(vector), xsize * ysize);

		for (i = 0; i < ysize - 1; i++)
			for (j = 0; j < xsize - 1 ; j++) {
				ind = i * xsize + j;
			 
				calcnorm(&n, ind, ind + 1, ind + xsize + 1, z, xsize, ysize);

				vadd(norms[ind], norms[ind], n);
				vadd(norms[ind + 1], norms[ind + 1], n);
				vadd(norms[ind + xsize + 1], norms[ind + xsize + 1], n);
				calcnorm(&n, ind, ind + xsize + 1, ind + xsize, z, xsize, ysize);

				vadd(norms[ind], norms[ind], n);
				vadd(norms[ind + xsize + 1], norms[ind + xsize + 1], n);
				vadd(norms[ind + xsize], norms[ind + xsize], n);
			}

		for (i = 0; i < xsize * ysize; i++)
			normalise(norms[i]);
	} else
		(*hfield)->norms = (vector *)NULL;
}

/*
 * hfieldn
 *
 *	return the normal for a height field.
 */
void
hfieldn(n, l, o, type)
	vector	*n, *l;
	object	*o;
	int	type;
{
	htriangle	*tri;
	float		w1, w2, w3;
	vector		**ns;

	tri = o->obj.hfield->obj;

	if (norms != (vector *)NULL) {
		w1 = o->obj.hfield->w1;
		w2 = o->obj.hfield->w2;
		w3 = 1.0 - (w1 + w2);

		ns = tri->norms;

		n->x = w1 * ns[1]->x + w2 * ns[2]->x + w3 * ns[0]->x;
		n->y = w1 * ns[1]->y + w2 * ns[2]->y + w3 * ns[0]->y;
		n->z = w1 * ns[1]->z + w2 * ns[2]->z + w3 * ns[0]->z;
	} else
		*n = tri->n;
}

/*
 * hfieldc
 *
 *	calculate the colour at a point on a height field. This uses
 * a linear map (ie we drop z and use x and y to index the image).
 */
void
hfieldc(o, txt, l, n, pcol, type)
	object		*o;
	texture		*txt;
	vector		*l, *n;
	pixel		*pcol;
	int		type;
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
 * hfieldtabinit
 *
 *	set the table of function pointers for a heightfield.
 */
hfieldtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[HFIELD] = hfieldn;
	intersects[HFIELD] = hfieldi;
	tilefuns[HFIELD] = hfieldc;
	checkbbox[HFIELD] = TRUE;
	selfshadowing[HFIELD] = TRUE;
}
