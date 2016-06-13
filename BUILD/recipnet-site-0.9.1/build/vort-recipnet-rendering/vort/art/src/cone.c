#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

#define dist_gt(x, y, d) ((x) * (x) + (y) * (y) > (d))



/*
 * conei
 *
 *	returns a list of intersection points for the ray r and cone o.
 *
 */
hlist *
conei(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist	*hitlist, *hp;
	real	a, b, c, d, t, t1, t2;
	real	tipval, ax1, ax2, x, y, z;
	int	face, entry, exit;
	ray	nr;

	transray(o, nr, *r);

	a = nr.dir.x * nr.dir.x + nr.dir.y * nr.dir.y - nr.dir.z * nr.dir.z;
	b = nr.org.x * nr.dir.x + nr.org.y * nr.dir.y - nr.dir.z * nr.org.z;
	c = nr.org.x * nr.org.x + nr.org.y * nr.org.y - nr.org.z * nr.org.z;

	d = b * b - a * c;

	if (d < 0.0)
		return((hlist *)NULL);
	
	tipval = o->obj.cne_tipval;

	hitlist = (hlist *)NULL;

	if (fabs(a) == 0.0) {
		t1 = -c / b;
		entry = SIDE;

		z = nr.org.z + t1 * nr.dir.z;

		if (z <= 1.0 && z >= tipval) {
			t2 = (1.0 - nr.org.z) / nr.dir.z;
			exit = PZFACE;
		} else {
			t1 = (tipval - nr.org.z) / nr.dir.z;
			t2 = (1.0 - nr.org.z) / nr.dir.z;

			if (t1 < tolerance && t2 < tolerance)
				return((hlist *)NULL);

			x = t2 * nr.dir.x + nr.org.x;
			y = t2 * nr.dir.y + nr.org.y;
			if (dist_gt(x, y, 1.0))
				return((hlist *)NULL);

			entry = NZFACE;
			exit = PZFACE;
		}
	} else {
		d = sqrt((double)d);

		entry = exit = SIDE;

		t1 = (-b - d) / a;
		t2 = (-b + d) / a;

		ax1 = t1 * nr.dir.z + nr.org.z;
		ax2 = t2 * nr.dir.z + nr.org.z;

		/*
		 * both hits outside range of z in -1.0 to 1.0
		 */
		if (fabs(ax1) >= 1.0 && fabs(ax2) >= 1.0)
			return((hlist *)NULL);

		/*
		 * both hits in range of z < 0.0
		 */
		if (ax1 <= 0.0 && ax2 <= 0.0)
			return((hlist *)NULL);

		if (ax1 < tipval && ax2 < tipval) {
			/*
			 * we pass through truncated section
			 */
			if (ax1 > 0.0 && ax2 > 0.0)
				return((hlist *)NULL);

			/*
			 * we must pass through top and base faces
			 * of truncated cone.
			 */
			entry = PZFACE;
			t1 = (1.0 - nr.org.z) / nr.dir.z;
			exit = NZFACE;
			t2 = (tipval - nr.org.z) / nr.dir.z;

		} else if (ax1 >= 1.0) {
			/*
			 * must hit base of cone.
			 */
			entry = PZFACE;
			t1 = (1.0 - nr.org.z) / nr.dir.z;

						/* do we hit top? */
			if (ax2 <= tipval) {
				/*
				 * check that we have really hit base
				 */
				x = t1 * nr.dir.x + nr.org.x;
				y = t1 * nr.dir.y + nr.org.y;
				if (dist_gt(x, y, 1.0))
					return((hlist *)NULL);

				exit = NZFACE;
				t2 = (tipval - nr.org.z) / nr.dir.z;
			}

		} else if (ax1 <= tipval) {
			/*
			 * must hit top of cone
			 */
			entry = NZFACE;
			t1 = (tipval - nr.org.z) / nr.dir.z;

			x = t1 * nr.dir.x + nr.org.x;
			y = t1 * nr.dir.y + nr.org.y;
			if (dist_gt(x, y, tipval * tipval)) {

				if (ax2 >= 1.0)
					return((hlist *)NULL);

				entry = PZFACE;
				t1 = (1.0 - nr.org.z) / nr.dir.z;

			} else if (ax2 >= 1.0) {
				exit = PZFACE;
				t2 = (1.0 - nr.org.z) / nr.dir.z;
			}
		} else {
			/*
			 * must have hit in the middle, check that
			 * the ax2 value is in range.
			 */
			if (ax2 >= 1.0) {
				exit = PZFACE;
				t2 = (1.0 - nr.org.z) / nr.dir.z;
			} else if (ax2 <= tipval) {
				exit = NZFACE;
				t2 = (tipval - nr.org.z) / nr.dir.z;

				x = t2 * nr.dir.x + nr.org.x;
				y = t2 * nr.dir.y + nr.org.y;
				if (dist_gt(x, y, tipval * tipval)) {
									/* must hit base */
					exit = PZFACE;
					t2 = (1.0 - nr.org.z) / nr.dir.z;
				}
			}
		}
	}

	if (t1 > t2) {
		t = t1;
		t1 = t2;
		t2 = t;
		face = entry;
		entry = exit;
		exit = face;
	}

	if (t1 >= tolerance) {
		fetch(hp);
		hp->type = entry;
		hitlist = hp;
		hp->t = t1;
		hp->obj = o;
		if (o->incsg) {
			fetch(hp);
			hitlist->nxt = hp;
			hp->type = exit;
			hp->t = t2;
			hp->obj = o;
		} 
		hp->nxt = (hlist *)NULL;
	} else if (t2 >= tolerance) {
		fetch(hp);
		hitlist = hp;
		hp->type = exit;
		hp->t = t2;
		hp->obj = o;
		hp->nxt = (hlist *)NULL;
	}

	*last = hp;

	return(hitlist);
}

/*
 * conen
 *
 *	returns the normal to the cone o
 */
void
conen(n, l, o, type)
	register vector		*n;
	register vector		*l;
	object			*o;
	int			type;
{
	if (type == SIDE) {
		toobject(o, *n, *l);

		n->z = -n->z;
		
	} else if (type == PZFACE) {
		n->x = 0.0;
		n->y = 0.0;
		n->z = 1.0;
	} else {
		n->x = 0.0;
		n->y = 0.0;
		n->z = -1.0;
	}
}

/*
 * conec
 *
 *	set the colour for the cone o
 */
void
conec(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	pixel   *pcol;
	int     type;
{
	vector  tmp;
	float	u, v, tipval;
	int     w, h, indx;
	tiletxt	*tp;

	tipval = o->obj.cne_tipval;

	if (type == SIDE) {
		totexture(txt, tmp, *l);

		tmp.x /= o->td->nscales.x; 
		tmp.y /= o->td->nscales.y;
		tmp.z /= o->td->nscales.z;
						 
		if (fabs(tmp.x) > 1.0)
			tmp.x = (tmp.x < 0.0) ? -1.0 : 1.0;

		v = 1.0 - (fabs(tmp.z) - tipval) / (1.0 - tipval);
		if (tmp.z != 0.0) {
			u = tmp.x / tmp.z;
			if (fabs(u) > 1.0)
				u = (u < 0.0) ? -1.0 : 1.0;

			u = acos(u) / (2.0 * M_PI);
		} else
			u = 1.0;

		tp = txt->u.t;

		w = v * tp->scalew;
		h = ((tmp.y < 0.0) ? 1.0 - u : u) * tp->scaleh;

		if (tmp.z < 0.0)
			w = tp->pixw - 1 - (w % tp->pixw);
		else
			w = w % tp->pixw;

		indx = (w + (h % tp->pixh) * tp->pixw) * 3;

		pcol->r = (unsigned char)tp->map[indx] / 255.0;
		pcol->g = (unsigned char)tp->map[indx + 1] / 255.0;
		pcol->b = (unsigned char)tp->map[indx + 2] / 255.0;
	}
}

/*
 * coneinit
 *
 *	initialise the function pointers and fields for a cone object,
 * returning its pointer.
 */
void
coneinit(o, d)
	object	*o;
	details *d;
{
	int		first, faces;
	vector		base, tip, basescale, tipscale, tmp;
	details		*ld;
	float		grad;

	faces = 0;
	first = 1;

	base.x = base.y = base.z = 0.0;
	tip.x = tip.y = tip.z = 1.0;

	basescale.x = basescale.y = 1.0;
	tipscale.x = tipscale.y = 0.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case VERTEX:
			tip = d->u.v;
			break;
		case CENTER:
			if (faces == 0)
				base = d->u.v;
			else
				tip = d->u.v;
			faces++;
			break;
		case RADIUS:
			if (first) {
				basescale.x = basescale.y = d->u.f;
				first = 0;
			} else
				tipscale.x = tipscale.y = d->u.f;
			break;
		case RADII:
			 if (first) {
				basescale.x = d->u.v.x;
				basescale.y = d->u.v.y;
				first = 0;
			} else {
				tipscale.x = d->u.v.x;
				tipscale.y = d->u.v.y;
			}
			break;
		default:
			warning("art: illegal field in cone ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

			/* this axis is done in obj_transform */
	basescale.z = tipscale.z = 1.0;

	if (faces == 2) {
		if (tipscale.x > basescale.x) {
			tmp = tip;
			tip = base;
			base = tmp;
			tmp = tipscale;
			tipscale = basescale;
			basescale = tmp;
		}
		o->obj.cne_tipval = tipscale.x / basescale.x;
		grad = (tip.x - base.x) / (basescale.x - tipscale.x);
		tip.x += grad * tipscale.x;
		grad = (tip.y - base.y) / (basescale.x - tipscale.x);
		tip.y += grad * tipscale.x;
		grad = (tip.z - base.z) / (basescale.x - tipscale.x);
		tip.z += grad * tipscale.x;
	} else 
		o->obj.cne_tipval = 0.0;

	tmp.x = (tip.x + base.x) / 2.0;
	tmp.y = (tip.y + base.y) / 2.0;
	tmp.z = (tip.z + base.z) / 2.0;

	obj_scale(basescale.x, basescale.y, basescale.z);

	obj_transform(tip, base);

	calctransforms(mstackp);

	makebbox(o, -1.0, -1.0, o->obj.cne_tipval, 1.0, 1.0, 1.0);

	setattributes(o);
}

/*
 * conetabinit
 *
 *	set the table of function pointers for the cone.
 */
conetabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[CONE] = conen;
	intersects[CONE] = conei;
	tilefuns[CONE] = conec;
	checkbbox[CONE] = TRUE;
	selfshadowing[CONE] = FALSE;
}
