#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"
#include "poly.h"

/*
 * based on, "ray tracing implicit surfaces resulting from the summation
 * of bounded volume polynomial functions", By David Tonnesen, Tech Report
 * no 89003, Rensselear Design Research Center, Rensselaer Polytechnic
 * Institute, Troy, Nw York, 12180.
 */
extern mats	*mstackp;
extern hlist	*fhlist;
extern real	tolerance;

/*
 * hit type structure and defs...
 */
#define	OENTRY		1		/* hit outter sphere */
#define	IENTRY		2		/* hit inner sphere */
#define	IEXIT		4		/* leaving inner sphere */
#define	OEXIT		8		/* leaving outer sphere */

#define	EXITMASK	12		/* bit mask for exits */

typedef struct hnd {
	float		t;
	mball		*mb;
	int		type;
	int		inside;
	float		coeff[5];
	struct hnd	*nxt;
} hitnode;

hitnode	*fhnlist = (hitnode *)NULL;

/*
 * macros to handle allocation of hitnode entries
 */
#define unget(p)	{\
	p->nxt = fhnlist;\
	fhnlist = p;\
}

#define	get(p)	{\
	if (fhnlist != (hitnode *)NULL) {\
		p = fhnlist;\
		fhnlist = fhnlist->nxt;\
	} else\
		p = (hitnode *)smalloc(sizeof(hitnode));\
}

/*
 * function macros....
 */
#define	subfunc(a, b) {\
		a[4] -= b[4]; \
		a[3] -= b[3]; \
		a[2] -= b[2]; \
		a[1] -= b[1]; \
		a[0] -= b[0]; \
}

#define	addfunc(a, b) {\
		a[4] += b[4]; \
		a[3] += b[3]; \
		a[2] += b[2]; \
		a[1] += b[1]; \
		a[0] += b[0]; \
}

/*
 * inserthit
 *
 *	insert hit into the list pointed to by hits, if *hits is
 * not NULL we check to see if we can start the insert from where
 * we are now.
 */
inserthit(hits, hit)
	hitnode **hits, *hit;
{
	hitnode		*hp, *lsthp;
	static hitnode	*end, *lst;

	if (*hits == (hitnode *)NULL) {
		*hits = hit;
		end = lst = hit;
	} else {
		end->nxt = hit;
		if (lst->t < hit->t)
			for (hp = lst; hp->t < hit->t; hp = hp->nxt)
				lsthp = hp;
		else
			for (hp = *hits; hp->t < hit->t; hp = hp->nxt)
				lsthp = hp;

		if (lsthp == end) {
			end = hit;
		} else if (hp == *hits) {
			hit->nxt = hp;
			*hits = hit;
		} else {
			hit->nxt = hp;
			lsthp->nxt = hit;
		}
		lst = hit;
		end->nxt = (hitnode *)NULL;
	}
}

/*
 * calcfunction
 *
 *	calculate the polynomial for this metaball.
 */
calcfunction(coeff, r, mb, rdirsqu)
	float	*coeff;
	ray	*r;
	mball	*mb;
	float	rdirsqu;
{
	vector	v;
	float	rsqu;
	float	a, b, c;

	/*
	 * precalculation. stage 1
	 */
	vsub(v, r->org, mb->v);
	rsqu = mb->r * mb->r;

	/*
	 * precalculation. stage 2
	 */
	a = rdirsqu / rsqu;
	b = 2 * dprod(v, r->dir) / rsqu;
	c = dprod(v, v) / rsqu - 1.0; 	/* c - 1.0 */

	/*
	 * coefficients:
	 *
	 *	a^2.t^4 + 2a.b.t^3 + (2a.c - 2a + b^2)t^2 +
	 * (2b.c - 2b)t + (c^2 - 2c + 1) = 0
	 */

	coeff[4] = mb->s * a * a;
	coeff[3] = mb->s * 2 * a * b;
	coeff[2] = mb->s * (b * b + 2 * a * c);
	coeff[1] = mb->s * 2 * b * c;
	coeff[0] = mb->s * c * c;
}

/*
 * blobi
 *
 *	returns a list of intersection points for the ray r and blob o.
 */
hlist *
blobi(r, o, last)
	ray	*r;
	object	*o;
	hlist	**last;
{
	vector	c;
	blobby	*bl;
	mball	*mb;
	hlist	*hitlist, *hp;
	hitnode	*hit, *hits, *nxthit;
	spoly	sseq[5];
	real	a, b, d;
	real	c1, c2, c3, endt, startt, t;
	int	count, i, atinf, atmin, onsurf, np, nroots;
	double	roots[5], rdirsqu;
	ray	nr;

	transray(o, nr, *r);

	bl = o->obj.blb;

	hitlist = (hlist *)NULL;
	hits = (hitnode *)NULL;
	count = 0;

	for (mb = o->obj.blb->mballs; mb != (mball *)NULL; mb = mb->nxt) {

		/*
		 * get interval for ri = 0 to R
		 */
		vsub(c, nr.org, mb->v);

		b = -dprod(c, nr.dir);
		a = (dprod(c, c) - mb->r * mb->r);

		if ((d = b * b - a) < 0.0 || (b < 0.0 && a > 0.0))
			continue;

		d = sqrt((double)d);

		if ((b - d) > tolerance) {
			get(hit);
			hit->t = b - d;
			hit->mb = mb;
			hit->type = OENTRY;
			hit->inside = FALSE;
			hit->nxt = (hitnode *)NULL;

			inserthit(&hits, hit);

			get(hit);
			hit->t = b + d;
			hit->mb = mb;
			hit->type = OEXIT;
			hit->inside = FALSE;
			hit->nxt = (hitnode *)NULL;

			inserthit(&hits, hit);
		} else if (b + d > tolerance) { 
			get(hit);
			hit->t = b + d;
			hit->mb = mb;
			hit->type = OEXIT;
			hit->inside = TRUE;
			hit->nxt = (hitnode *)NULL;
			inserthit(&hits, hit);
		}

	}

	if (hits != (hitnode *)NULL) {

		/*
		 * do some useful precalculation...
		 */
		rdirsqu = dprod(nr.dir, nr.dir);

		sseq[0].coef[4] = sseq[0].coef[3] = sseq[0].coef[2] = 
		sseq[0].coef[1] = 0.0;
		sseq[0].coef[0] = -o->obj.blb->thresh;

		/*
		 * add the density functions for any blobbys we are inside
		 */
		onsurf = FALSE;

		for (hit = hits; hit != (hitnode *)NULL; hit = hit->nxt) {
			calcfunction(hit->coeff, &nr, hit->mb, rdirsqu);
			hit->mb->inside = FALSE;
			if (hit->inside) {
				onsurf = TRUE;
				addfunc(sseq[0].coef, hit->coeff);
			}
		}

		endt = tolerance;

		for (hit = hits; hit != (hitnode *)NULL; hit = hit->nxt) {
			startt = endt;
			endt = hit->t;

			if (onsurf || hit != hits) {
				np = buildsturm(4, sseq);

				atmin = evalata(np, sseq, startt);
				atinf = evalatb(np, sseq, endt);

				nroots = atmin - atinf;

				if (nroots == 0) {
					if (hit->type == OEXIT) {
						hit->mb->inside = FALSE; /* we left it */
						subfunc(sseq[0].coef, hit->coeff);
					} else {
						hit->mb->inside = TRUE; /* perhaps */
						addfunc(sseq[0].coef, hit->coeff);
					}
					continue;
				}

				sbisect(np, sseq, startt, endt, atmin, atinf, roots);

				fetch(hitlist);
				hitlist->t = roots[0];
				hitlist->obj = o;
				hitlist->nxt = (hlist *)NULL;
				*last = hitlist;

				break;
			} else {
				hit->mb->inside = TRUE; /* perhaps */
				addfunc(sseq[0].coef, hit->coeff);
			}
		}
	}

	for (hit = hits; hit != (hitnode *)NULL; hit = nxthit) {
		nxthit = hit->nxt;
		unget(hit);
	}

	return(hitlist);
}

/*
 * blobn
 *
 *	returns the normal to the blob s
 */
void
blobn(n, l, o)
	register vector	*n;
	register vector	*l;
	register object	*o;
{
	vector	loc, v;
	float	rsqu;
	mball	*mb;

	toobject(o, loc, *l);

	n->x = n->y = n->z = 0.0;

	for (mb = o->obj.blb->mballs; mb != (mball *)NULL; mb = mb->nxt) {

		if (mb->inside) {
			rsqu = mb->r * mb->r;
			vsub(v, loc, mb->v);

			n->x += mb->s * ((2 * (dprod(v, v))) / (rsqu * rsqu) -
				2 / rsqu) * 2 * v.x;
			n->y += mb->s * ((2 * (dprod(v, v))) / (rsqu * rsqu) -
				2 / rsqu) * 2 * v.y;
			n->z += mb->s * ((2 * (dprod(v, v))) / (rsqu * rsqu) -
				2 / rsqu) * 2 * v.z;
		}
	}
}

/*
 * blobc
 *
 *	return the color of a blob o at a the intersection point l.
 *
 */
void
blobc(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	pixel	*pcol;
	int	type;
{
}

/*
 * blobinit
 *
 *	initialise the function pointers and fields for a blob object,
 * returning its pointer.
 */
void
blobinit(o, d)
	object	*o;
	details *d;
{
	details	*ld;
	blobby	*b;
	mball	*mb;
	vector	cent;
	float	radius;

	b = o->obj.blb = (blobby *)smalloc(sizeof(blobby));
	b->mballs = (mball *)NULL;
	b->thresh = 0.5;

	while (d != (details *)NULL) {
		switch (d->type) {
		case METABALL:
			mb = d->u.mb;
			mb->nxt = b->mballs;
			b->mballs = mb;
			break;
		case THRESHOLD:
			b->thresh = d->u.f;
			break;
		default:
			warning("art: illegal field in blob ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	calctransforms(mstackp);

	if (b->mballs == (mball *)NULL)
		fatal("blobbyinit: no metaballs in blobby!\n");

	/*
	 * calculate the bounding box
	 */
	o->bb.max[X] = b->mballs->v.x + mb->r;
	o->bb.min[X] = b->mballs->v.x - mb->r;
	o->bb.max[Y] = b->mballs->v.y + mb->r;
	o->bb.min[Y] = b->mballs->v.y - mb->r;
	o->bb.max[Z] = b->mballs->v.z + mb->r;
	o->bb.min[Z] = b->mballs->v.z - mb->r;
	for (mb = b->mballs->nxt; mb != (mball *)NULL; mb = mb->nxt) {
		if (mb->v.x + mb->r > o->bb.max[X])
			o->bb.max[X] = mb->v.x + mb->r;
		if (mb->v.x - mb->r < o->bb.min[X])
			o->bb.min[X] = mb->v.x - mb->r;
		if (mb->v.y + mb->r > o->bb.max[Y])
			o->bb.max[Y] = mb->v.y + mb->r;
		if (mb->v.y - mb->r < o->bb.min[Y])
			o->bb.min[Y] = mb->v.y - mb->r;
		if (mb->v.z + mb->r > o->bb.max[Z])
			o->bb.max[Z] = mb->v.z + mb->r;
		if (mb->v.z - mb->r < o->bb.min[Z])
			o->bb.min[Z] = mb->v.z - mb->r;
	}

	makebbox(o, o->bb.min[X], o->bb.min[Y], o->bb.min[Z], o->bb.max[X], o->bb.max[Y], o->bb.max[Z]);

	setattributes(o);
}

/*
 * blobtabinit
 *
 *	set the table of function pointers for the sphere.
 */
blobtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[BLOBBY] = blobn;
	intersects[BLOBBY] = blobi;
	tilefuns[BLOBBY] = (void (*)())NULL;
	checkbbox[BLOBBY] = FALSE;
	selfshadowing[BLOBBY] = TRUE;
}
