#include <stdio.h>
#include <math.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern attr	*astackp;
extern mats	*mstackp;

extern matrix	trans;
extern vector	org;
extern int	linecount;

/*
 * utility routines for adjusting transformations and other
 * object attributes.
 */

/*
 * rotate
 *
 * 	adjust the transformation matrix for a rotate - done in reverse
 * as it is done to the ray.
 */
rotate(ang, axis)
	float	ang;
	char	axis;
{
	matrix	mat, tmp;
	float	sinval, cosval;

	mident4(mat);
	sinval = sin(-ang * M_PI / 180.0);
	cosval = cos(-ang * M_PI / 180.0);

	switch (axis) {
	case 'x':
		mat[1][1] = cosval;
		mat[1][2] = sinval;
		mat[2][2] = cosval;
		mat[2][1] = -sinval;
		break;
	case 'y':
		mat[0][0] = cosval;
		mat[0][2] = -sinval;
		mat[2][0] = sinval;
		mat[2][2] = cosval;
		break;
	case 'z':
		mat[0][0] = cosval;
		mat[0][1] = sinval;
		mat[1][0] = -sinval;
		mat[1][1] = cosval;
		break;
	default:
		fatal("art: bad axis name in rotate.\n");
	}

	mcpy4(tmp, mstackp->d.vm);
	mmult4(mstackp->d.vm, mat, tmp);

	mstackp->d.vmused = TRUE;
}

/*
 * translate
 *
 *	translate - negative as we do this to the ray.
 */
translate(x, y, z)
	float	x, y, z;
{
	matrix	mat, tmp;

	mident4(mat);

	mat[3][0] = -x;
	mat[3][1] = -y;
	mat[3][2] = -z;

	mcpy4(tmp, mstackp->d.vm);
	mmult4(mstackp->d.vm, mat, tmp);

	mstackp->d.vmused = TRUE;
}

/*
 * obj_translate
 *
 *	object translate - used to go from a canonical form to world 
 * form.
 */
obj_translate(x, y, z)
	float	x, y, z;
{
	matrix	mat, tmp;

	mident4(mat);

	mat[3][0] = -x;
	mat[3][1] = -y;
	mat[3][2] = -z;

	mcpy4(tmp, mstackp->d.om);
	mmult4(mstackp->d.om, mat, tmp);

	mstackp->d.omused = TRUE;
}

/*
 * scale
 *
 *	scale routine - done in reverse as we do this to the ray.
 */
scale(x, y, z)
	float	x, y, z;
{
	matrix	mat, tmp;

	mident4(mat);

	mat[0][0] /= x;
	mat[1][1] /= y;
	mat[2][2] /= z;

	mcpy4(tmp, mstackp->d.vm);
	mmult4(mstackp->d.vm, mat, tmp);

	mstackp->d.nscales.x *= fabs(x);
	mstackp->d.nscales.y *= fabs(y);
	mstackp->d.nscales.z *= fabs(z);

	if (fabs(x) > fabs(y))
		if (fabs(x) > fabs(z))
			mstackp->d.maxscale *= fabs(x);
		else
			mstackp->d.maxscale *= fabs(z);
	else
		if (fabs(y) > fabs(z))
			mstackp->d.maxscale *= fabs(y);
		else
			mstackp->d.maxscale *= fabs(z);

	mstackp->d.vmused = TRUE;
}

/*
 * obj_scale
 *
 *	scale routine - done in reverse as we do this to the ray.
 */
obj_scale(x, y, z)
	float	x, y, z;
{
	matrix	mat, tmp;

	mident4(mat);

	mat[0][0] /= x;
	mat[1][1] /= y;
	mat[2][2] /= z;

	mcpy4(tmp, mstackp->d.om);
	mmult4(mstackp->d.om, mat, tmp);

	mstackp->d.nscales.x *= fabs(x);
	mstackp->d.nscales.y *= fabs(y);
	mstackp->d.nscales.z *= fabs(z);

	if (fabs(x) > fabs(y))
		if (fabs(x) > fabs(z))
			mstackp->d.maxscale *= fabs(x);
		else
			mstackp->d.maxscale *= fabs(z);
	else
		if (fabs(y) > fabs(z))
			mstackp->d.maxscale *= fabs(y);
		else
			mstackp->d.maxscale *= fabs(z);

	mstackp->d.omused = TRUE;
}

/*
 * transform
 *
 *	apply the transformation matrix m to the matrix stack.
 */
transform(m)
	matrix	m;
{
	matrix	tmp;
	float	x, y, z;

	/*
	 * multiply stack
	 */
	mcpy4(tmp, mstackp->d.vm);
	mmult4(mstackp->d.vm, m, tmp);

	/*
	 * calculate the scaling vector
	 */
	x = sqrt(m[0][0] * m[0][0] + m[0][1] * m[0][1] + m[0][2] * m[0][2]);
	y = sqrt(m[1][0] * m[1][0] + m[1][1] * m[1][1] + m[1][2] * m[1][2]);
	z = sqrt(m[2][0] * m[2][0] + m[2][1] * m[2][1] + m[2][2] * m[2][2]);

	mstackp->d.nscales.x /= x;
	mstackp->d.nscales.y /= y;
	mstackp->d.nscales.z /= z;

	if (x > y)
		if (x > z)
			mstackp->d.maxscale /= x;
		else
			mstackp->d.maxscale /= z;
	else
		if (y > z)
			mstackp->d.maxscale /= y;
		else
			mstackp->d.maxscale /= z;
	
	mstackp->d.vmused = TRUE;
}

/*
 * obj_transform
 *
 *	set up the appropriate scaling, rotations, and translations for
 * a cylinder, cone, or general quadric. Radii, etc, should be taken
 * into account before this routine is called.
 */
obj_transform(cent1, cent2)
	vector	cent1, cent2;
{
	float	sinval, cosval, d1, d2, d3;
	matrix	m, tmp;

	d1 = cent2.x - cent1.x;
	d2 = cent2.y - cent1.y;
	d3 = cent2.z - cent1.z;

	/*
	 * fix the quadric's height 
	 */
	obj_scale(1.0, 1.0, sqrt(d1 * d1 + d2 * d2 + d3 * d3));

	/*
	 * calculate the rotations to align the ray
	 * with the quadric's axis 
	 */

	/*
	 * twist around y to align with positive in z
	 * (the transformations below align us in negative z)
	 */
	mident4(m);
	m[0][0] = -1.0;
	m[2][2] = -1.0;
	mcpy4(tmp, mstackp->d.om);
	mmult4(mstackp->d.om, m, tmp);

	if (d2 != 0.0) {        /* rotate about x */
		sinval = -d2 / sqrt(d1 * d1 + d2 * d2 + d3 * d3);

		if (fabs(sinval) > 1.0)
			sinval = (sinval > 0.0) ? 1.0 : -1.0;

		cosval = sqrt(1.0 - sinval * sinval);

		mident4(m);
		m[1][1] = cosval;
		m[1][2] = sinval;
		m[2][2] = cosval;
		m[2][1] = -sinval;
		mcpy4(tmp, mstackp->d.om);
		mmult4(mstackp->d.om, m, tmp);
	}

	if (d1 != 0.0 || d3 != 0.0) {	/* rotate about y */
		sinval = d1 / sqrt(d1 * d1 + d3 * d3);

		cosval = -d3 / sqrt(d1 * d1 + d3 * d3);

		if (fabs(sinval) > 1.0)
			sinval = (sinval > 0.0) ? 1.0 : -1.0;

		if (fabs(cosval) > 1.0)
			cosval = (cosval > 0.0) ? 1.0 : -1.0;

		mident4(m);
		m[0][0] = cosval;
		m[2][0] = sinval;
		m[0][2] = -sinval;
		m[2][2] = cosval;
		mcpy4(tmp, mstackp->d.om);
		mmult4(mstackp->d.om, m, tmp);
	}

	/*
	 * translate it
	 */
	mident4(m);
	m[3][0] = -cent1.x;
	m[3][1] = -cent1.y;
	m[3][2] = -cent1.z;
	mcpy4(tmp, mstackp->d.om);
	mmult4(mstackp->d.om, m, tmp);

	mstackp->d.omused = TRUE;
}

/*
 * generatePertubationVector
 *
 *	generates a perturbation vector using the values u and v
 * for the vector ax. 
 */
void
generatePerturbationVector(ax, u, v, res)
	vector	ax;
	float	u;
	float	v;
	vector	*res;
{
	float		a, b, c, fact;

	a = ax.x;
	b = ax.y;
	c = ax.z;

	/*
	 * bump mapped polygons start out life with (0, 0, 1) as their
	 * normal, as this happens a lot it's worth checking for!
	 */
	if (a == 0.0 && b == 0.0)
	{
		fact = fabs(c);
	}
	else
	{
		fact = sqrt(b * b + c * c);
	}

	/*
	 * rotate the vector (u, v, 0) so that it aligns with
	 * ax
	 */
	if (fact != 0.0)
	{
		res->x = fact * u;
		res->y = (-a * b * u + c * v) / fact;
		res->z = (-a * c * u + b * v) / fact;
	}
	else
	{
		res->x = 0.0;
		res->y = u;
		res->z = -v;
	}
}

/*
 * setattributes
 *
 *	set the attributes and transformations for an object.
 */
setattributes(o)
	object	*o;
{
	matrix		txtmat, tmp;
	texture		*tl, *ntl, *nxttl;
	transdata	*txttd;
	attr		*sp;
	mats		*mp;

	o->s = astackp->d.s;

	o->td = mstackp->d.td;

	/*
	 * put textures into correct order and set some parameters
	 */
	o->txtlist = (texture *)NULL;

	mident4(tmp);

	for (mp = mstackp, sp = astackp; sp != (attr *)NULL; sp = sp->lst, mp = mp->lst) {

		if (sp != astackp && sp->d.txtlist != (texture *)NULL)
			calctransforms(mp);

		for (tl = sp->d.txtlist; tl != (texture *)NULL; tl = nxttl) {

			nxttl = tl->nxt;

			ntl = (texture *)smalloc(sizeof(texture));
			*ntl = *tl;
			ntl->nxt = o->txtlist;
			o->txtlist = ntl;

			if (ntl->td == (transdata *)NULL)
				ntl->td = mp->d.td;
			else {
				txttd = ntl->td;

				cp3x3(tmp, txttd->mat);    /* include object transformation */
				tmp[3][0] = txttd->trans.x;
				tmp[3][1] = txttd->trans.y;
				tmp[3][2] = txttd->trans.z;

				txttd = (transdata *)smalloc(sizeof(transdata));

				mmult4(txtmat, mp->d.ray2obj, tmp);
				txttd->nscales = mp->d.nscales;

				cp3x3(txttd->mat, txtmat);
				txttd->trans.x = txtmat[3][0];
				txttd->trans.y = txtmat[3][1];
				txttd->trans.z = txtmat[3][2];

				ntl->td = txttd;
			}
		}
	}
}

/*
 * copydetails
 *
 *	copy the details from one detail structure to another, 
 * making sure sub fields are handled as well.
 */
copydetails(d1, d2)
	details	*d1, *d2;
{
	details	*nd1, *nd2, *nd3, *dl, *nd, *tail;

	*d1 = *d2;

	if (d2->type == COMPLEXVERTEX) {
		nd1 = (details *)smalloc(sizeof(details));
		*nd1 = *d2->u.det;
		if (nd1->nxt != (details *)NULL) {
			nd2 = (details *)smalloc(sizeof(details));
			*nd2 = *nd1->nxt;
			nd1->nxt = nd2;
			if (nd2->nxt != (details *)NULL) {
				nd3 = (details *)smalloc(sizeof(details));
				*nd3 = *nd2->nxt;
				nd2->nxt = nd3;
			}
		}
		d1->u.det = nd1;
	} else if (d2->type == TRANSFORM) {
		d1->u.trans.m = (matrix *)smalloc(sizeof(matrix));              
		mcpy4(*d1->u.trans.m, *d2->u.trans.m);
	} else if (d2->type == COMP_OBJ || d2->type == OBJECT) {
		tail = (details *)NULL;
		for (dl = d2->u.obj.det; dl != (details *)NULL; dl = dl->nxt) {
			nd = (details *)smalloc(sizeof(details));
			copydetails(nd, dl);
			if (tail == (details *)NULL) 
				tail = d1->u.obj.det = nd; 
			else {
				tail->nxt = nd; 
				tail = nd;
			}
		}
	}
}

/*
 * calctransforms
 *
 *	calculate the current ray2obj and obj2ray transformation
 * matrices.
 */
calctransforms(msp)
	mats	*msp;
{
	matrix		tmp, tmp1, tmp2;
	mats		*mp;
	transdata	*td;

	if (msp->d.omused) {
		if (msp->lst != (mats *)NULL) {
			mp = msp;
			mcpy4(tmp1, mp->d.vm);
			while (mp->lst != (mats *)NULL) {
				mmult4(tmp2, mp->lst->d.vm, tmp1);
				mcpy4(tmp1, tmp2);
				mp = mp->lst;
			}
		} else
			mcpy4(tmp1, msp->d.vm);
		mmult4(tmp2, tmp1, msp->d.om);
		mmult4(msp->d.ray2obj, trans, tmp2);
		minv4(msp->d.obj2ray, msp->d.ray2obj);
	} else if (msp->d.vmused) {
		if (msp->lst != (mats *)NULL) {
			mp = msp;
			mcpy4(tmp1, mp->d.vm);
			while (mp->lst != (mats *)NULL) {
				mmult4(tmp2, mp->lst->d.vm, tmp1);
				mcpy4(tmp1, tmp2);
				mp = mp->lst;
			}
		} else
			mcpy4(tmp1, msp->d.vm);
		mmult4(msp->d.ray2obj, trans, tmp1);
		minv4(msp->d.obj2ray, msp->d.ray2obj);
	} else
		return;		/* nothing to calculate */

	msp->d.td = td = (transdata *)smalloc(sizeof(transdata));

	cp3x3(td->mat, msp->d.ray2obj);

	td->trans.x = msp->d.ray2obj[3][0];
	td->trans.y = msp->d.ray2obj[3][1];
	td->trans.z = msp->d.ray2obj[3][2];

	td->nscales = msp->d.nscales;

	msp->d.omused = msp->d.vmused = FALSE;
}

/*
 * makebbox
 *
 *	make a bounding box for the object o.
 *
 */
makebbox(o, x1, y1, z1, x2, y2, z2)
	object	*o;
	float	x1, y1, z1, x2, y2, z2;
{
	vector	c, v;

	v.x = x1; v.y = y1; v.z = z1;

	vmmult(c, v, mstackp->d.obj2ray);

	o->bb.min[X] = o->bb.max[X] = c.x;
	o->bb.min[Y] = o->bb.max[Y] = c.y;
	o->bb.min[Z] = o->bb.max[Z] = c.z;

	v.x = x2; v.y = y2; v.z = z2;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x1; v.y = y2; v.z = z2;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x2; v.y = y1; v.z = z2;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x2; v.y = y2; v.z = z1;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x1; v.y = y1; v.z = z2;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x1; v.y = y2; v.z = z1;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	v.x = x2; v.y = y1; v.z = z1;

	vmmult(c, v, mstackp->d.obj2ray);

	minmax(o->bb.min[X], o->bb.max[X], c.x);
	minmax(o->bb.min[Y], o->bb.max[Y], c.y);
	minmax(o->bb.min[Z], o->bb.max[Z], c.z);

	o->bb.min[X] -= TOLERANCE;
	o->bb.min[Y] -= TOLERANCE;
	o->bb.min[Z] -= TOLERANCE;

	o->bb.max[X] += TOLERANCE;
	o->bb.max[Y] += TOLERANCE;
	o->bb.max[Z] += TOLERANCE;
}

/*
 * readascmap
 *
 *	read in an ascii texture/bump map.
 */
readascmap(txt, name)
	proctxt	*txt;
	char	*name;
{
	FILE	*f;
	int	c, noheader, x, y, tot;
	float	val1, val2, val3;
	char	buf[BUFSIZ];

	if ((f = fopen(name, "r")) == (FILE *)NULL) {
		sprintf(buf, "art: can't open ascii map file %s.\n", name);
		fatal(buf);
	}
	
	noheader = TRUE;

	while (noheader && (c = getc(f)) != EOF) {
		if (c == '#')		/* comment */
			while ((c = getc(f)) != '\n' && c != EOF)
				;
		else {
			ungetc(c, f);
			if (fscanf(f, "%d %d ", &x, &y) != 2) {
				sprintf(buf, "art: improper header in ascii map file %s.\n", name);
				fatal(buf);
			}
			noheader = FALSE;
		}
	}

	txt->pixw = x;
	txt->pixh = y;
	txt->map = (char *)scalloc(x, y * 3);

	if (noheader) {
		sprintf(buf, "art: no header in ascii map file %s.\n", name);
		fatal(buf);
	}

	tot = 0;
	while (tot < x * y && (c = getc(f)) != EOF) {
		if (c == '#')		/* comment */
			while ((c = getc(f)) != '\n' && c != EOF)
				;
		else {
			ungetc(c, f);
			if (fscanf(f, "%f %f %f ", &val1, &val2, &val3) != 3) {
				sprintf(buf, "art: improper data in ascii map file %s.\n", name);
				fatal(buf);
			}
			txt->map[tot * 3] = val1 * 255.0;
			txt->map[tot * 3 + 1] = val2 * 255.0;
			txt->map[tot * 3 + 2] = val3 * 255.0;
			tot++;
		}
	}

	if (tot != x * y) {
		sprintf(buf, "art: ascii map file %s too short.\n", name);
		warning(buf);
	}

	fclose(f);
}

/*
 * mapinit
 *
 *	Form a small color map from a detail list.
 *
 */
cmap *
mapinit(d)
	details	*d;
{
	cmap	*m;
	details	*md;
	char	buf[BUFSIZ];
	int	num;

	/*
	 * Find out how many of them there are....
	 */
	for (num = 0, md = d; md != (details *)NULL; md = md->nxt, num++)
		;

	/*
	 * Allocate the bastard....
	 */
	m = (cmap *)smalloc(sizeof(cmap));
	m->n = num;
	m->m = (char *)scalloc(num, 3);

	/*
	 * List is backwards....
	 */
	num--;

	while (d != (details *)NULL) {
		switch (d->type) {
		case MAPVALUES:
                        m->m[num * 3] = d->u.v.x * 255.0;
                        m->m[num * 3 + 1] = d->u.v.y * 255.0;
                        m->m[num * 3 + 2] = d->u.v.z * 255.0;
                        num--;
			break;

		default:
			sprintf(buf, "art: illegal field in map value ignored (line %d).\n", linecount);
			warning(buf);
		}
		md = d;
		d = d->nxt;
		free(md);
	}

	return(m);
}
