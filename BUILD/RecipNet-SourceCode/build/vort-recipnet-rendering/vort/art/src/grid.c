#include <stdio.h>
#include <math.h>
#include "art.h"
#include "macro.h"

extern float	tolerance;

/*
 * strictly speaking this should be calculated
 * according to the size of the grid, later...
 */
#define	SMALL	0.00001
				

extern int	debug;

extern hlist	*(*intersects[])();
extern int	checkbbox[];

extern hlist	*fhlist;

extern int	gridsize[];

/*
 * checkvoxel
 *
 *	check the objects in a object list, returning a hit if the
 * object is intersected by the ray and the hit lies in this voxel.
 */
hlist *
checkvoxel(r, pmin, pmax, voxbb, oblist)
	ray	*r;
	float	**pmin, **pmax;
	bbox	*voxbb;
	olist	*oblist;
{
	olist	*ol;
	object	*o;
	float	val;
	int	bboxcheck;
	surface	*sp;
	vector	minv, maxv;
	hlist	*end, *hit, *hp, *lp, *prevhits;

	prevhits = (hlist *)NULL;

	minv.x = *pmin[X];
	minv.y = *pmin[Y];
	minv.z = *pmin[Z];

	maxv.x = *pmax[X];
	maxv.y = *pmax[Y];
	maxv.z = *pmax[Z];

	for (ol = oblist; ol != (olist *)NULL; ol = ol->nxt) {
		o = ol->obj;

		if (o->lastray.raynumber == r->raynumber) {
			if (o->lastray.t != 0.0) {
				fetch(hit);
				hit->t = o->lastray.t;
				hit->type = o->lastray.type;
				if (o->type == CSG_ADD || o->type == CSG_INT ||
				    o->type == CSG_SUB)
					hit->obj = o->obj.csgt->hitobj;
				else
					hit->obj = o;
				hit->nxt = (hlist *)NULL;
			} else
				continue;		/* go to next object */

		} else {

			if (o->bb.max[X] < minv.x || o->bb.min[X] > maxv.x
			  || o->bb.max[Y] < minv.y || o->bb.min[Y] > maxv.y
			  || o->bb.max[Z] < minv.z || o->bb.min[Z] > maxv.z)
				continue;

			o->lastray.raynumber = r->raynumber;
			o->lastray.t = 0.0;

			if (r->type == SHADOW_RAY && !o->s->shadows)
				continue;

			if (checkbbox[o->type] && missedbbox(r, &o->bb))
				continue;

			if ((hit = intersects[o->type](r, o, &end)) == (hlist *)NULL)
				continue;		/* go to next object */

			o->lastray.t = hit->t;
			o->lastray.type = hit->type;
		}

		/*
		 * check that we need to look further...
		 */
		if (r->type == SHADOW_RAY && hit->t < r->maxt) {
			if (o->txtlist == (texture *)NULL) {
				sp = o->s;
				if (sp->trans.r == 0.0 && sp->trans.g == 0.0 && sp->trans.b == 0.0) {
					for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
						lp = hp->nxt;
						release(hp);
					}
					return(hit);
				}
			}
		}

		if (prevhits == (hlist *)NULL) {
			prevhits = hit;
		} else {
			if (prevhits->t > hit->t) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}

				prevhits = hit;
			} else {
				for (hp = hit; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}

			}
		}
	}

	if (prevhits != (hlist *)NULL) {

		val = r->org.x + r->dir.x * prevhits->t;
		if (r->dir.x < 0.0) {
			if (val < voxbb->min[X]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > voxbb->max[X]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}

		val = r->org.y + r->dir.y * prevhits->t;
		if (r->dir.y < 0.0) {
			if (val < voxbb->min[Y]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > voxbb->max[Y]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}

		val = r->org.z + r->dir.z * prevhits->t;
		if (r->dir.z < 0.0) {
			if (val < voxbb->min[Z]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		} else {
			if (val > voxbb->max[Z]) {
				for (hp = prevhits; hp != (hlist *)NULL; hp = lp) {
					lp = hp->nxt;
					release(hp);
				}
				return((hlist *)NULL);
			}
		}
	}

	return(prevhits);
}

/*
 * gridtrace
 *
 *	intersect a ray with the uniform grid.
 */
hlist *
gridtrace(r, gr)
	ray	*r;
	grid	*gr;
{
	vector	pnt, npnt;
	int	i, j, k;
	int	istep, jstep, kstep;
	int	iend, jend, kend;
	hlist	*hit;
	voxel	*vox;
	bbox	voxbb;
	int	li, lj, lk;
	vector	rmin, rmax, loc;
	vector	xskip, yskip, zskip;
	float	t1, t2, xgap, ygap, zgap, delta;
	float	xdist, ydist, zdist;
	vector	nxpnt, nypnt, nzpnt;
	float	*pmin[3], *pmax[3];

	if (inbbox(r, &gr->bb, &t1, &t2)) {
		if (t2 <= 0.0) {	/* we are in the box */
			t2 = t1;
			t1 = 0.0;
			pnt.x = r->org.x;
			pnt.y = r->org.y;
			pnt.z = r->org.z;
		} else {
			pnt.x = r->org.x + t1 * r->dir.x;
			pnt.y = r->org.y + t1 * r->dir.y;
			pnt.z = r->org.z + t1 * r->dir.z;
		}
	} else 
		return((hlist *)NULL);

	i = (pnt.x - gr->bb.min[X]) / gr->deltax;
	j = (pnt.y - gr->bb.min[Y]) / gr->deltay;
	k = (pnt.z - gr->bb.min[Z]) / gr->deltaz;

	/*
	 * check that we aren't on the outside edge...
	 */
	if (i == gr->xdiv)
		i = gr->xdiv - 1;

	if (j == gr->ydiv)
		j = gr->ydiv - 1;

	if (k == gr->zdiv)
		k = gr->zdiv - 1;

	/* 
	 * calculate the increments to get along voxels.
	 */
	voxbb.min[X] = gr->bb.min[X] + i * gr->deltax;
	voxbb.max[X] = voxbb.min[X] + gr->deltax;
	voxbb.min[Y] = gr->bb.min[Y] + j * gr->deltay;
	voxbb.max[Y] = voxbb.min[Y] + gr->deltay;
	voxbb.min[Z] = gr->bb.min[Z] + k * gr->deltaz;
	voxbb.max[Z] = voxbb.min[Z] + gr->deltaz;

	if (r->dir.x < -SMALL) {
		istep = -1;
		iend = -1;
		xgap = gr->deltax / -r->dir.x;
		xdist = (voxbb.min[X] - pnt.x) / r->dir.x;
		pmin[X] = &npnt.x;
		pmax[X] = &pnt.x;
	} else if (r->dir.x > SMALL) {
		istep = 1;
		iend = gr->xdiv;
		xgap = gr->deltax / r->dir.x;
		xdist = (voxbb.max[X] - pnt.x) / r->dir.x;
		pmin[X] = &pnt.x;
		pmax[X] = &npnt.x;
	} else {
		istep = 0;
		iend = i;
		xgap = HUGE_DIST;
		xdist = HUGE_DIST;
		pmin[X] = &pnt.x;
		pmax[X] = &npnt.x;
	}

	if (r->dir.y < -SMALL) {
		jstep = -1;
		jend = -1;
		ygap = gr->deltay / -r->dir.y;
		ydist = (voxbb.min[Y] - pnt.y) / r->dir.y;
		pmin[Y] = &npnt.y;
		pmax[Y] = &pnt.y;
	} else if (r->dir.y > SMALL) {
		jstep = 1;
		jend = gr->ydiv;
		ygap = gr->deltay / r->dir.y;
		ydist = (voxbb.max[Y] - pnt.y) / r->dir.y;
		pmin[Y] = &pnt.y;
		pmax[Y] = &npnt.y;
	} else {
		jstep = 0;
		jend = j;
		ygap = HUGE_DIST;
		ydist = HUGE_DIST;
		pmin[Y] = &pnt.y;
		pmax[Y] = &npnt.y;
	}

	if (r->dir.z < -SMALL) {
		kstep = -1;
		kend = -1;
		zgap = gr->deltaz / -r->dir.z;
		zdist = (voxbb.min[Z] - pnt.z) / r->dir.z;
		pmin[Z] = &npnt.z;
		pmax[Z] = &pnt.z;
	} else if (r->dir.z > SMALL) {
		kstep = 1;
		kend = gr->zdiv;
		zgap = gr->deltaz / r->dir.z;
		zdist = (voxbb.max[Z] - pnt.z) / r->dir.z;
		pmin[Z] = &pnt.z;
		pmax[Z] = &npnt.z;
	} else {
		kstep = 0;
		kend = k;
		zgap = HUGE_DIST;
		zdist = HUGE_DIST;
		pmin[Z] = &pnt.z;
		pmax[Z] = &npnt.z;
	}

	/*
	 * no point in going past light source
	 */
	if (r->type == SHADOW_RAY) {
		loc.x = r->org.x + r->maxt * r->dir.x;
		li = (loc.x - gr->bb.min[X]) / gr->deltax;
		if (li > 1 && li < gr->xdiv - 1)
			iend = li + istep;

		loc.y = r->org.y + r->maxt * r->dir.y;
		lj = (loc.y - gr->bb.min[Y]) / gr->deltay;
		if (lj > 1 && lj < gr->ydiv - 1)
			jend = lj + jstep;

		loc.z = r->org.z + r->maxt * r->dir.z;
		lk = (loc.z - gr->bb.min[Z]) / gr->deltaz;
		if (lk > 1 && lk < gr->zdiv - 1)
			kend = lk + kstep;
	}

	xskip.x = xgap * r->dir.x;
	xskip.y = xgap * r->dir.y;
	xskip.z = xgap * r->dir.z;

	yskip.x = ygap * r->dir.x;
	yskip.y = ygap * r->dir.y;
	yskip.z = ygap * r->dir.z;

	zskip.x = zgap * r->dir.x;
	zskip.y = zgap * r->dir.y;
	zskip.z = zgap * r->dir.z;

	nxpnt.x = pnt.x + xdist * r->dir.x;
	nxpnt.y = pnt.y + xdist * r->dir.y;
	nxpnt.z = pnt.z + xdist * r->dir.z;

	nypnt.x = pnt.x + ydist * r->dir.x;
	nypnt.y = pnt.y + ydist * r->dir.y;
	nypnt.z = pnt.z + ydist * r->dir.z;

	nzpnt.x = pnt.x + zdist * r->dir.x;
	nzpnt.y = pnt.y + zdist * r->dir.y;
	nzpnt.z = pnt.z + zdist * r->dir.z;

	/*
	 * step through the grid until we arrive at an object
	 */
	for (;;) {

		vox = gr->voxs[i][j][k];

		if (xdist < ydist)
			if (xdist < zdist) {
				npnt = nxpnt;
				if (vox != (voxel *)NULL && (hit = checkvoxel(r, pmin, pmax, &vox->bb, vox->oblist)) != (hlist *)NULL) {
					return(hit);
				}
				i += istep;
							/* out of grid */
				if (i == iend)
					return((hlist *)NULL);

				xdist += xgap;
				pnt = nxpnt;
				vadd(nxpnt, pnt, xskip);
			} else {
				npnt = nzpnt;
				if (vox != (voxel *)NULL && (hit = checkvoxel(r, pmin, pmax, &vox->bb, vox->oblist)) != (hlist *)NULL) {
					return(hit);
				}
				k += kstep;
							/* out of grid */
				if (k == kend)
					return((hlist *)NULL);

				zdist += zgap;
				pnt = nzpnt;
				vadd(nzpnt, pnt, zskip);
			}
		else
			if (ydist < zdist) {
				npnt = nypnt;
				if (vox != (voxel *)NULL && (hit = checkvoxel(r, pmin, pmax, &vox->bb, vox->oblist)) != (hlist *)NULL) {
					return(hit);
				}
				j += jstep;
							/* out of grid */
				if (j == jend)
					return((hlist *)NULL);

				ydist += ygap;
				pnt = nypnt;
				vadd(nypnt, pnt, yskip);
			} else {
				npnt = nzpnt;
				if (vox != (voxel *)NULL && (hit = checkvoxel(r, pmin, pmax, &vox->bb, vox->oblist)) != (hlist *)NULL) {
					return(hit);
				}
				k += kstep;
							/* out of grid */
				if (k == kend)
					return((hlist *)NULL);

				zdist += zgap;
				pnt = nzpnt;
				vadd(nzpnt, pnt, zskip);
			}
	}
}

/*
 * gridi
 * 
 *	intersect a uniform grid.
 *
 */
hlist *
gridi(r, o, last)
	ray	*r;
	object	*o;
	hlist   **last;
{
	hlist	*hit;

	hit = gridtrace(r, o->obj.grd);

	return(hit);
}

/*
 * gridinit
 *
 *	throw the list of objects into a uniform grid.
 */
object *
gridinit(objs)
	object	*objs;
{
	object		*obj, *o;
	olist		*ol;
	grid		*gr;
	voxel		*vox;
	int		i, j, k;
	int		mini, maxi, minj, maxj, mink, maxk;
	static surface	gridsurface;

	obj = (object *)smalloc(sizeof(object));

	obj->type = UGRID;
	obj->obj.grd = gr = (grid *)smalloc(sizeof(grid));

	/*
	 * set up a surface so shadow optimisation and stuff
	 * doesn't get confused.
	 */
	gridsurface.shadows = TRUE;
	gridsurface.trans.r = 0.0;
	gridsurface.trans.g = 0.0;
	gridsurface.trans.b = 0.0;

	obj->s = &gridsurface;

	/*
	 * set the grid's bounding box.
	 */
	obj->bb.min[X] = objs->bb.min[X];
	obj->bb.max[X] = objs->bb.max[X];

	obj->bb.min[Y] = objs->bb.min[Y];
	obj->bb.max[Y] = objs->bb.max[Y];

	obj->bb.min[Z] = objs->bb.min[Z];
	obj->bb.max[Z] = objs->bb.max[Z];

	for (o = objs; o != (object *)NULL; o = o->nxt)
		adjustbbox(&obj->bb, o);

	gr->bb = obj->bb;

	gr->deltax = (gr->bb.max[X] - gr->bb.min[X]) / gridsize[X];
	gr->deltay = (gr->bb.max[Y] - gr->bb.min[Y]) / gridsize[Y];
	gr->deltaz = (gr->bb.max[Z] - gr->bb.min[Z]) / gridsize[Z];

	/*
	 * allocate the space for the grid.
	 */
	gr->voxs = (voxel ****)smalloc(sizeof(voxel ***) * gridsize[X]);
	for (i = 0; i != gridsize[X]; i++) {
		gr->voxs[i] = (voxel ***)smalloc(sizeof(voxel **) * gridsize[Y]);
		for (j = 0; j != gridsize[Y]; j++) {
			gr->voxs[i][j] = (voxel **)smalloc(sizeof(voxel *) * gridsize[Z]);
			for (k = 0; k != gridsize[Z]; k++)
				gr->voxs[i][j][k] = (voxel *)NULL;
		}
	}

	gr->xdiv = gridsize[X];
	gr->ydiv = gridsize[Y];
	gr->zdiv = gridsize[Z];

	/*
	 * insert the objects into the grid.
	 */
	for (o = objs; o != (object *)NULL; o = o->nxt) {
		mini = (o->bb.min[X] - gr->bb.min[X]) / gr->deltax;
		maxi = (o->bb.max[X] - gr->bb.min[X]) / gr->deltax;
		if (maxi == gr->xdiv)
			maxi = gr->xdiv - 1;
		minj = (o->bb.min[Y] - gr->bb.min[Y]) / gr->deltay;
		maxj = (o->bb.max[Y] - gr->bb.min[Y]) / gr->deltay;
		if (maxj == gr->ydiv)
			maxj = gr->ydiv - 1;
		mink = (o->bb.min[Z] - gr->bb.min[Z]) / gr->deltaz;
		maxk = (o->bb.max[Z] - gr->bb.min[Z]) / gr->deltaz;
		if (maxk == gr->zdiv)
			maxk = gr->zdiv - 1;
		for (i = mini; i <= maxi; i++)
			for (j = minj; j <= maxj; j++)
				for (k = mink; k <= maxk; k++) {
					if (gr->voxs[i][j][k] == (voxel *)NULL) {
						vox = gr->voxs[i][j][k] = (voxel *)smalloc(sizeof(voxel));
						vox->oblist = (olist *)NULL;
						vox->bb.min[X] = gr->bb.min[X] + i * gr->deltax;
						vox->bb.max[X] = vox->bb.min[X] + gr->deltax;
						vox->bb.min[Y] = gr->bb.min[Y] + j * gr->deltay;
						vox->bb.max[Y] = vox->bb.min[Y] + gr->deltay;
						vox->bb.min[Z] = gr->bb.min[Z] + k * gr->deltaz;
						vox->bb.max[Z] = vox->bb.min[Z] + gr->deltaz;

					}
					ol = (olist *)smalloc(sizeof(olist));
					ol->obj = o;
					ol->nxt = gr->voxs[i][j][k]->oblist;
					gr->voxs[i][j][k]->oblist = ol;
				}

	}

	return(obj);
}

/*
 * gridtabinit
 *
 *	set the table of function pointers for a grid.
 */
gridtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	intersects[UGRID] = gridi;
	normals[UGRID] = (void (*)())NULL;
	tilefuns[UGRID] = (void (*)())NULL;
	checkbbox[UGRID] = TRUE;
	selfshadowing[UGRID] = TRUE;
}
