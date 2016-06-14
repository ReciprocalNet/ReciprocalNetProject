#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"
#include "poly.h"

extern hlist	*(*intersects[NUM_OBJS])();

#define	_X	1
#define	_Y	2
#define	_Z	4

extern attr	*astackp;
extern mats	*mstackp;
extern hlist	*fhlist;
extern vector	org;

extern object	*objectinit();

extern double	power(), evalpoly(), modrf();

extern int	buildsturm(), evalat0(), evalata(), evalatb();

static double fact[] = {
	 1.0,		1.0,		2.0,
	 6.0,		24.0,		120.0,
	 720.0,		5040.0,		40320.0,
	 362880.0,	3628800.0,	39916800.0
};

/*
 * algi
 *
 *	returns a the first hit for the ray r and the algebraic surface o.
 */
hlist *
algi(r, o, last)
	register ray	*r;
	register object	*o;
	hlist		**last;
{
	hlist		*hp, *lp, *hitlist, *end;
	int		i, j, its, nroots, np, atmin, atinf;
	int		numroots, rptcount[MAX_ORDER];
	eqn		*e;
	double 		roots[MAX_ORDER], t, min, max, lcarry, *cp;
	ray		nr;
	spoly		sseq[MAX_ORDER];

	if (r->type == SHADOW_RAY && !o->s->shadows)
		return((hlist *)NULL);

	e = o->obj.alg;

	min = max = 0.0;

	if (e->obj != (object *)NULL) {		/* we have a clipping volume */
		hitlist = intersects[e->obj->type](r, e->obj, &end);
		if (hitlist == (hlist *)NULL)
			return((hlist *)NULL);
		
		if (end == hitlist) {
			min = ALG_TOLERANCE;
			max = hitlist->t;
		} else {
			min = hitlist->t;
			max = end->t;
		}

		for (hp = hitlist; hp != (hlist *)NULL; hp = lp) {
			lp = hp->nxt;
			release(hp);
		}
	}

	transray(o, nr, *r);

	for (i = 0; i <= e->order; i++)
		sseq[0].coef[i] = 0.0;

	buildp(e, &nr, sseq[0].coef);

	if (fabs(sseq[0].coef[0]) <= ALG_TOLERANCE) {
		cp = &sseq[0].coef[e->order];
		lcarry = *cp;
		for (cp--; cp >= sseq[0].coef; cp--) {
			lcarry = *cp + lcarry * ALG_TOLERANCE;
			if (lcarry < 0.0)
				break;
		}

		if (lcarry >= 0.0)
			return((hlist *)NULL);
	}

	np = buildsturm(e->order, sseq);

	if (min == 0.0)
		atmin = evalat0(np, sseq);
	else
		atmin = evalata(np, sseq, min);

	atinf = evalatb(np, sseq, max);

	nroots = atmin - atinf;

	if (nroots == 0) 
		return((hlist *)NULL);

	if (e->obj == (object *)NULL) {		/* min and max need to be set */
		atmin = evalata(np, sseq, ALG_TOLERANCE);

		nroots = atmin - atinf;

		if (nroots == 0)
			return((hlist *)NULL);

		/*
		 * find an upper bound
		 */
		min = t = ALG_TOLERANCE;
		max = min + 2.0;
		for (its = 0; its < MAXP2; its++) {
			atinf = evalatb(np, sseq, max);

			numroots = atmin - atinf;

			if (o->incsg == FALSE) {
				if (numroots > 0)
					break;
			} else {
				if (numroots == nroots)
					break;
			}

			if (numroots == 0)
				min = t;

			t = max;
			max *= 2.0;
		}
	}

	if (nroots == 1 || o->incsg == FALSE) {
		sbisect(np, sseq, min, max, atmin, atinf, roots);

		fetch(hitlist);
		hitlist->t = roots[0];
		hitlist->obj = o;
		hitlist->nxt = (hlist *)NULL;
		*last = hitlist;
	} else {
		for (i = 1; i <= e->order; i++)
			rptcount[i] = 0;

		csgsbisect(np, sseq, min, max, atmin, atinf, roots, rptcount);

		fetch(hitlist);
		lp = hp = hitlist;
		for (i = 0; i < nroots; i++) {
			for (j = 0; j < rptcount[i]; j++) {
				hp->t = roots[i];
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
 * eval
 * 
 *	evaluate the polynomial defined by trmlist for x y and z
 */
static double
eval(trmlist, xpow, ypow, zpow)
	term		*trmlist;
	register double	*xpow, *ypow, *zpow;
{
	register double	val;
	register term	*t;

	val = 0;
	for (t = trmlist; t != (term *)NULL; t = t->nxt)
		val += t->coef * xpow[t->xp] * ypow[t->yp] * zpow[t->zp];
	
	return(val);
}

/*
 * algn
 *
 *	returns the normal vector to a point on an algebraic surface
 */
void
algn(n, l, o)
	register vector	*n, *l;
	register object	*o;
{
	register double	x, y, z;
	register int	i;
	double		xpow[MAX_ORDER], ypow[MAX_ORDER], zpow[MAX_ORDER];
	register eqn	*e;
	vector		tmp;

	e = o->obj.alg;

	toobject(o, tmp, *l);

	xpow[0] = ypow[0] = zpow[0] = 1.0;
	xpow[1] = x = tmp.x;
	ypow[1] = y = tmp.y;
	zpow[1] = z = tmp.z;
	for (i = 2; i <= e->maxxp; i++)
		xpow[i] = xpow[i - 1] * x;
	for (i = 2; i <= e->maxyp; i++)
		ypow[i] = ypow[i - 1] * y;
	for (i = 2; i <= e->maxzp; i++)
		zpow[i] = zpow[i - 1] * z;

	n->x = eval(e->dxlist, xpow, ypow, zpow);
	n->y = eval(e->dylist, xpow, ypow, zpow);
	n->z = eval(e->dzlist, xpow, ypow, zpow);

	if ((n->x == 0.0) && (n->y == 0.0) && (n->z == 0.0))
	{
		n->z = -1.0;
	}
}

/*
 * buildp
 *
 *    Build up the polynomial in t
 * 
 *    Remember 	x = x->a + x->b * t
 *      	y = y->a + y->b * t
 *	    	z = z->a + z->b * t
 *
 *    and a general term is of the form 
 *     
 *         term = coef * (x^l) * (y^m) * (z^n)
 *
 *	The reader will bare in mind that speed is important here.
 */
int
buildp(e, r,  coef)
	eqn	*e; 
	ray	*r;
	double	*coef;
{
	register int 	i, j, l, m, n;
	register double	t1, t2, ecoef, *cf, *lf, *mf, *nf;
	register pwlist	*pw;
	register term	*et;

	for (pw = e->xpws; pw != (pwlist *)NULL; pw = pw->nxt)
		bp(pw->pw, r->org.x, r->dir.x, pw->coefs);

	for (pw = e->ypws; pw != (pwlist *)NULL; pw = pw->nxt)
		bp(pw->pw, r->org.y, r->dir.y, pw->coefs);

	for (pw = e->zpws; pw != (pwlist *)NULL; pw = pw->nxt)
		bp(pw->pw, r->org.z, r->dir.z, pw->coefs);

	for (et = e->trmlist; et != (term *)NULL; et = et->nxt) {
		ecoef = et->coef;
		switch (et->type) {
		case 0:
			coef[0] += ecoef;
			break;
		case _X:
			l = et->xp;
			lf = et->xcoefs + l;
			cf = coef + l;
			for (i = l; i >= 0; i--)
				*cf-- += *lf-- * ecoef;
			break;
		case _Y:
			l = et->yp;
			lf = et->ycoefs + l;
			cf = coef + l;
			for (i = l; i >= 0; i--)
				*cf-- += *lf-- * ecoef;
			break;
		case _Z:
			l = et->zp;
			lf = et->zcoefs + l;
			cf = coef + l;
			for (i = l; i >= 0; i--)
				*cf-- += *lf-- * ecoef;
			break;
		case _X | _Y:
			m = et->xp;
			n = et->yp;
			mf = et->xcoefs + m;
			lf = et->ycoefs + n;
			cf = coef + m;
			while (m >= 0) {
				t1 = ecoef * *mf--;
				nf = lf;
				cf += n;
				for (i = n; i >= 0; i--)
					*cf-- += *nf-- * t1;
				m--;
			}
			break;
		case _Y | _Z:
			m = et->yp;
			n = et->zp;
			mf = et->ycoefs + m;
			lf = et->zcoefs + n;
			cf = coef + m;
			while (m >= 0) {
				t1 = ecoef * *mf--;
				nf = lf;
				cf += n;
				for (i = n; i >= 0; i--)
					*cf-- += *nf-- * t1;
				m--;
			}
			break;
		case _X | _Z:
			m = et->xp;
			n = et->zp;
			mf = et->xcoefs + m;
			lf = et->zcoefs + n;
			cf = coef + m;
			while (m >= 0) {
				t1 = ecoef * *mf--;
				nf = lf;
				cf += n;
				for (i = n; i >= 0; i--)
					*cf-- += *nf-- * t1;
				m--;
			}
			break;
		case _X | _Y | _Z:
			l = et->xp;
			m = et->yp;
			n = et->zp;
			lf = et->xcoefs + l;
			mf = et->ycoefs - 1;
			nf = et->zcoefs - 1;
			cf = coef + l;
			while (l >= 0) {
				t1 = ecoef * *lf--;
				mf += m + 1;
				cf += m;
				for (i = m; i >= 0; i--) {
					t2 = t1 * *mf--;
					cf += n;
					nf += n + 1;
					for (j = n; j >= 0; j--)
						*cf-- += *nf-- * t2;
				}
				l--;
			}
			break;
		default:
			fatal("art: bad type in buildp.\n");
		}
	}
}

/*
 * bp
 *
 *	constructs the coefficients for a polynomial of order ord in
 * 	variables org and dir
 */
bp(ord, org, dir, coef)
	register int	ord;
	register double	org, dir;
	register double	*coef;
{
	register double	*start, *end;
	register double	cof, delta;

	start = coef;
	end = coef + ord;

	if (org != 0) {
		switch (ord) {
		case 1:
			coef[0] = org;
			coef[1] = dir;
			break;
		case 2:
			coef[0] = org * org;
			coef[1] = 2.0 * org * dir;
			coef[2] = dir * dir;
			break;
		default:
			*coef = power(org, ord);
			delta = dir / org;
			cof = *coef * delta * fact[ord];
			while (++coef <= end) {
				*coef = cof / (fact[end - coef] * fact[coef - start]);
				cof = cof * delta;
			}
		}
	} else {
		while (coef < end)
			*coef++ = 0;
		*coef = power(dir, ord);
	}
}

/*
 * alginit
 *
 *	initialise the function pointers and fields for a algebraic object,
 * 	returning its pointer.
 */
void
alginit(o, d)
	object	*o;
	details *d;
{
	eqn	*alg;
	int	i;
	char	xpws[MAX_ORDER], ypws[MAX_ORDER], zpws[MAX_ORDER], buf[BUFSIZ];
	double	*xcoefs[MAX_ORDER], *ycoefs[MAX_ORDER], *zcoefs[MAX_ORDER];
	pwlist	*p;
	details	*ld;
	term	*t, *dxt, *dyt, *dzt, *nt;

	calctransforms(mstackp);

	setattributes(o);

	o->bb.min[X] = -HUGE_DIST;
	o->bb.min[Y] = -HUGE_DIST;
	o->bb.min[Z] = -HUGE_DIST;

	o->bb.max[X] = HUGE_DIST;
	o->bb.max[Y] = HUGE_DIST;
	o->bb.max[Z] = HUGE_DIST;

	alg = o->obj.alg = (eqn *)smalloc(sizeof(eqn));
	alg->obj = (object *)NULL;

	while (d != (details *)NULL) {
		switch (d->type) {
		case OBJECT:
			alg->obj = objectinit(d->u.obj.sym, d->u.obj.det);
			alg->obj->incsg = TRUE;
			break;
		case EQUATION:
			alg->trmlist = d->u.t;
			break;
		default:
			warning("art: illegal field in algebraic ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	alg->order = 0;
	alg->maxxp = 0;
	alg->maxyp = 0;
	alg->maxzp = 0;
	alg->dxlist = (term *)NULL;
	alg->dylist = (term *)NULL;
	alg->dzlist = (term *)NULL;
	alg->xpws = (pwlist *)NULL;
	alg->ypws = (pwlist *)NULL;
	alg->zpws = (pwlist *)NULL;

	for (t = alg->trmlist; t != (term *)NULL; t = t->nxt) {
		for (nt = t->nxt; nt != (term *)NULL; nt = nt->nxt) {
			if (nt->xp == t->xp && nt->yp == t->yp && nt->zp == t->zp) {
				t->coef += nt->coef;
				nt->coef = 0.0;
			}
		}
	}

	nt = (term *)NULL;

	for (t = alg->trmlist; t != (term *)NULL; t = t->nxt) {
		if (t->coef == 0.0) {
			if (nt != (term *)NULL)
				nt->nxt = t->nxt;
			else
				alg->trmlist = t->nxt;
			free(t);
		} else
			nt = t;
	}

	for (i = 0; i < MAX_ORDER; i++) {
		xpws[i] = FALSE;
		ypws[i] = FALSE;
		zpws[i] = FALSE;
	}

	for (t = alg->trmlist; t != (term *)NULL; t = t->nxt) {
		if (alg->maxxp < t->xp)
			alg->maxxp = t->xp;
		if (alg->maxyp < t->yp)
			alg->maxyp = t->yp;
		if (alg->maxzp < t->zp)
			alg->maxzp = t->zp;
		if (alg->order < (t->xp + t->yp +t->zp))
			alg->order = t->xp + t->yp + t->zp;
		t->type = 0;
		if (t->xp != 0) {
			t->type |= _X;
			dxt = (term *)smalloc(sizeof(term));
			*dxt = *t;
			dxt->xp = t->xp - 1;
			dxt->coef *= t->xp;
			dxt->nxt = alg->dxlist;
			alg->dxlist = dxt;
			if (!xpws[t->xp - 1]) {
				p = (pwlist *)smalloc(sizeof(pwlist));
				p->pw = t->xp;
				xcoefs[t->xp - 1] = p->coefs = (double *)scalloc(sizeof(double), (p->pw + 1));
				p->nxt = alg->xpws;
				alg->xpws = p;
				xpws[t->xp - 1] = TRUE;
			}
			t->xcoefs = xcoefs[t->xp - 1];
		}
		if (t->yp != 0) {
			t->type |= _Y;
			dyt = (term *)smalloc(sizeof(term));
			*dyt = *t;
			dyt->yp = t->yp - 1;
			dyt->coef *= t->yp;
			dyt->nxt = alg->dylist;
			alg->dylist = dyt;
			if (!ypws[t->yp - 1]) {
				p = (pwlist *)smalloc(sizeof(pwlist));
				p->pw = t->yp;
				ycoefs[t->yp - 1] = p->coefs = (double *)scalloc(sizeof(double), (p->pw + 1));
				p->nxt = alg->ypws;
				alg->ypws = p;
				ypws[t->yp - 1] = TRUE;
			}
			t->ycoefs = ycoefs[t->yp - 1];
		}
		if (t->zp != 0) {
			t->type |= _Z;
			dzt = (term *)smalloc(sizeof(term));
			*dzt = *t;
			dzt->zp = t->zp - 1;
			dzt->coef *= t->zp;
			dzt->nxt = alg->dzlist;
			alg->dzlist = dzt;
			if (!zpws[t->zp - 1]) {
				p = (pwlist *)smalloc(sizeof(pwlist));
				p->pw = t->zp;
				zcoefs[t->zp - 1] = p->coefs = (double *)scalloc(sizeof(double), (p->pw + 1));
				p->nxt = alg->zpws;
				alg->zpws = p;
				zpws[t->zp - 1] = TRUE;
			}
			t->zcoefs = zcoefs[t->zp - 1];
		}
	}

	for (t = alg->trmlist; t != (term *)NULL; t = t->nxt) {
		sprintf(buf, "%f %d %d %d\n", t->coef, t->xp, t->yp, t->zp);
		message(buf);
	}
}

/*
 * algtabinit
 *
 *	set the table of function pointers for the algebraic surface.
 */
algtabinit(intersects, normals, tilefuns, checkbbox, selfshadowing)
	hlist	*(*intersects[])();
	void	(*normals[])();
	void	(*tilefuns[])();
	int	checkbbox[];
	int	selfshadowing[];
{
	normals[ALGEBRAIC] = algn;
	intersects[ALGEBRAIC] = algi;
	tilefuns[ALGEBRAIC] = (void (*)())NULL;
	checkbbox[ALGEBRAIC] = FALSE;
	selfshadowing[ALGEBRAIC] = TRUE;
}
