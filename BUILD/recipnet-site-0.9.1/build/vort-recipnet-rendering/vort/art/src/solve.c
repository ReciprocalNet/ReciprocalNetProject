#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"
#include "poly.h"

extern attr	*astackp;
extern hlist	*fhlist;
extern vector	org;

/*
 * power
 *
 *	raise x to the integer power e
 */
double 
power(x, e)
	double	x;
	register int	e;
{
	register double	a, b;

	b = x;

	switch (e) {
	case 0:
		return(1.0);
	case 1:
		return(b);
	case 2:
		return(b * b);
	case 3:
		return(b * b * b);
	case 4:
		b *= b;
		return(b * b);
	case 5:
		b *= b;
		return(b * b * x);
	case 6:
		b *= b;
		return(b * b * b);
	default:
		e -= 6;
		a = b * b;
		a = a * a * a;
		for (;;) {
			if (e & 1) {
				a *= b;
				if ((e /= 2) == 0)
					return(a);
			} else 
				e /= 2;
			b *= b;
		}
	}

	/* NOTREACHED */
}

/*
 * evalpoly
 *
 *	evaluate polynomial defined in coef
 */
double
evalpoly(ord, coef, x)
	int	ord;
	register double	*coef, x;
{
	register double	*fp, f;

	fp = &coef[ord];
	f = *fp;

	for (fp--; fp >= coef; fp--)
		f = x * f + *fp;

	return(f);
}

/*
 * evalat0
 *
 *	calculate the sign changes in the sturm sequence in smat at 0
 */
int
evalat0(np, sseq)
	register int	np;
	register spoly	*sseq;
{
	register int	at0;
	register spoly	*s, *ls;

	at0 = 0;

	ls = sseq;
	for (s = sseq + 1; s <= sseq + np; s++) {
		if (ls->coef[0] * s->coef[0] < 0 || (ls->coef[0] == 0.0 && s->coef[0] != 0.0))
				at0++;
		ls = s;
	}

	return(at0);
}

/*
 * evalata
 *
 *	calculate the sign changes in the sturm sequence in smat at min value a
 */
int
evalata(np, sseq, a)
	register int	np;
	register spoly	*sseq;
	register double	a;
{
	register int	ata;
	register double	f, lf;
	register spoly	*s;

	ata = 0;

	lf = evalpoly(sseq[0].ord, sseq[0].coef, a);

	for (s = sseq + 1; s <= sseq + np; s++) {
		f = evalpoly(s->ord, s->coef, a);
		if (lf * f < 0 || lf == 0.0)
				ata++;
		lf = f;
	}

	return(ata);
}

/*
 * evalatb
 *
 *	calculate the sign changes in the sturm sequence in smat at max value b
 */
int
evalatb(np, sseq, b)
	int		np;
	register spoly	*sseq;
	register double	b;
{
	int		atb;
	register double	f, lf;
	register spoly	*s, *ls;

	atb = 0;

	if (b == 0.0) {
		ls = sseq;
		for (s = sseq + 1; s <= sseq + np; s++) {
			if (ls->coef[ls->ord] * s->coef[s->ord] < 0 || (ls->coef[ls->ord] == 0.0 && s->coef[s->ord] != 0.0))
				atb++;
			ls = s;
		}
	} else {
		lf = evalpoly(sseq[0].ord, sseq[0].coef, b);
		for (s = sseq + 1; s <= sseq + np; s++) {
			f = evalpoly(s->ord, s->coef, b);
			if (lf * f < 0 || lf == 0.0)
					atb++;
			lf = f;
		}
	}

	return(atb);
}

/*
 * modrf
 *
 *	uses the modified regula-falsi method to evaluate the root in interval
 * [a,b] of the polynomial described in coef. The root is returned is returned
 * in *val. The routine returns zero if it can't converge.
 */
int
modrf(ord, coef, a, b, val)
	int	ord;
	double	*coef;
	register double	a, b, *val;
{
	register int	its;
	register double	fa, fb, x, fx, lfx;
	register double	*fp, *scoef, *ecoef;
	char		buf[MESLEN];

	scoef = coef;
	ecoef = &coef[ord];

	fb = fa = *ecoef;
	for (fp = ecoef - 1; fp >= scoef; fp--) {
		fa = a * fa + *fp;
		fb = b * fb + *fp;
	}

	/*
	 * if there is no sign difference the method won't work
	 */
	if (fa * fb > 0.0)
		return(0);

	if (fabs(fa) < RELERROR) {
		*val = a;
		return(1);
	}

	if (fabs(fb) < RELERROR) {
		*val = b;
		return(1);
	}

	lfx = fa;

	for (its = 0; its < MAXIT; its++) {

		x = (fb * a - fa * b) / (fb - fa);

		fx = *ecoef;
		for (fp = ecoef - 1; fp >= scoef; fp--)
			fx = x * fx + *fp;

		if (fabs(x) > RELERROR) {
			if (fabs(fx / x) < RELERROR) {
				*val = x;
				return(1);
			}
		} else if (fabs(fx) < RELERROR) {
			*val = x;
			return(1);
		}

		if ((fa * fx) < 0) {
			b = x;
			fb = fx;
			if ((lfx * fx) > 0)
				fa /= 2;
		} else {
			a = x;
			fa = fx;
			if ((lfx * fx) > 0)
				fb /= 2;
		}

		lfx = fx;
	}

	if (fabs(fx) > ALG_TOLERANCE) {
		sprintf(buf, "modrf overflow %f %f %f\n", a, b, fx);
		message(buf);
	}

	*val = x;

	return(1);
}

/*
 * sbisect
 *
 *	uses a bisection based on the sturm sequence for the polynomial
 * described in sseq to isolate the lowest positve root of the polynomial
 * in the bounds min and max. The root is returned in *roots.
 */
sbisect(np, sseq, min, max, atmin, atmax, roots)
	int		np;
	register spoly	*sseq;
	register double	min, max;
	register int	atmin, atmax;
	register double	*roots;
{
	register double	mid;
	register int	n1, its, atmid;
	char		buf[MESLEN];

	for (its = 0; its < MAXIT; its++) {

		mid = (min + max) / 2;

		atmid = numchanges(np, sseq, mid);

		n1 = atmin - atmid;

		if (n1 == 1) {
			/*
			 * first try a less expensive technique.
			 */
			if (modrf(sseq->ord, sseq->coef, min, mid, &roots[0]))
				return;

			/*
			 * if we get here we have to evaluate the root the hard
			 * way by using the Sturm sequence. It also means the
			 * root is repeated an even number of times.
			 */
			for (its = 0; its < MAXIT; its++) {
				mid = (min + max) / 2;

				atmid = numchanges(np, sseq, mid);

				if (fabs(mid) > RELERROR) {
					if (fabs((max - min) / mid) < RELERROR) {
						roots[0] = mid;
						return;
					}
				} else if (fabs(max - min) < RELERROR) {
					roots[0] = mid;
					return;
				}

				if ((atmin - atmid) == 0)
					min = mid;
				else
					max = mid;
			}

			if (its == MAXIT && (max - min) > ALG_TOLERANCE) {
				sprintf(buf, "sbisect: overflow min %f max %f diff %e\n", min, max, max - min);
				message(buf);
			}

			roots[0] = mid;

			return;
		} else if (n1 == 0)
			min = mid;
		else
			max = mid;

	}

	roots[0] = min;		/* multiple roots close together */

	if (its == MAXIT && (max - min) > ALG_TOLERANCE) {
		sprintf(buf, "sbisect: overflow min %f max %f diff %e\n", min, max, max - min);
		message(buf);
	}
}

/*
 * csgsbisect
 *
 *	uses a bisection based on the sturm sequence for the polynomial
 * described in sseq to find the positive roots in the bounds min to max,
 * the roots are returned in the roots array in order of magnitude.
 */
csgsbisect(np, sseq, min, max, atmin, atmax, roots, repeatcount)
	int		np;
	spoly	*sseq;
	double	min, max;
	int	atmin, atmax;
	double	*roots;
	int	*repeatcount;
{
	double	mid;
	char	buf[MESLEN];
	int	n1, n2, its, atmid, nroot;

	repeatcount[0] = atmin - atmax;		/* assume the worst */

	if ((nroot = atmin - atmax) == 1) {

		/*
		 * first try a less expensive technique.
		 */
		if (modrf(sseq->ord, sseq->coef, min, max, &roots[0])) {
			repeatcount[0] = 1;
			return;
		}

		/*
		 * if we get here we have to evaluate the root the hard
		 * way by using the Sturm sequence. It also means the
		 * root is repeated an even number of times.
		 */
		for (its = 0; its < MAXIT; its++) {
			mid = (min + max) / 2;

			atmid = numchanges(np, sseq, mid);

			if (fabs(mid) > RELERROR) {
				if (fabs((max - min) / mid) < RELERROR)
					break;
			} else if (fabs(max - min) < RELERROR)
				break;

			if ((atmin - atmid) == 0)
				min = mid;
			else
				max = mid;
		}

		if (its == MAXIT) {
			sprintf(buf, "csgsbisect: overflow min %f max %f diff %e\n", min, max, max - min);
			message(buf);
		}

		roots[0] = mid;
		repeatcount[0] = 2;

		return;
	}

	/*
	 * more than one root in the interval, we have to bisect...
	 */
	for (its = 0; its < MAXIT; its++) {

		mid = (min + max) / 2;

		atmid = numchanges(np, sseq, mid);

		n1 = atmin - atmid;
		n2 = atmid - atmax;

		if (n1 != 0 && n2 != 0) {
			csgsbisect(np, sseq, min, mid, atmin, atmid, roots, repeatcount);
			csgsbisect(np, sseq, mid, max, atmid, atmax, &roots[n1], &repeatcount[n1]);
			break;
		}

		if (n1 == 0)
			min = mid;
		else
			max = mid;
	}

	if (its == MAXIT) {
		sprintf(buf, "csgsbisect: roots too close together\n");
		message(buf);
		sprintf(buf, "csgsbisect: overflow min %f max %f diff %e nroot %d n1 %d n2 %d\n", min, max, max - min, nroot, n1, n2);
		message(buf);
		roots[0] = mid;
	}
}

/*
 * modp
 *
 *	calculates the modulus of u(x) / v(x) leaving it in r, it
 * returns 0 if r(x) is a constant.
 *
 *	note: this function assumes the leading coefficient of v is 1 or -1
 */
static int
modp(u, v, r)
	spoly	*u, *v, *r;
{
	int	k, j;
	double	*nr, *end, *uc;

	nr = r->coef;
	end = &u->coef[u->ord];

	uc = u->coef;
	while (uc <= end)
		*nr++ = *uc++;

	if (v->coef[v->ord] < 0.0) {

		for (k = u->ord - v->ord - 1; k >= 0; k -= 2)
			r->coef[k] = -r->coef[k];

		for (k = u->ord - v->ord; k >= 0; k--)
			for (j = v->ord + k - 1; j >= k; j--)
				r->coef[j] = -r->coef[j] - r->coef[v->ord + k] * v->coef[j - k];
	} else {
		for (k = u->ord - v->ord; k >= 0; k--)
			for (j = v->ord + k - 1; j >= k; j--)
			r->coef[j] -= r->coef[v->ord + k] * v->coef[j - k];
	}

#ifdef WORRY_ABOUT_REPEATED_ROOTS
	k = v->ord - 1;
	while (k >= 0 && fabs(r->coef[k]) < FUDGE) {
		r->coef[k] = 0.0;
		k--;
	}
#else
	k = v->ord - 1;
	while (k >= 0 && r->coef[k] == 0.0)
		k--;
#endif

	r->ord = (k < 0) ? 0 : k;

	return(r->ord);
}

/*
 * buildsturm
 *
 *	build up a sturm sequence for a polynomial in smat, returning
 * the number of polynomials in the sequence
 */
int
buildsturm(ord, sseq)
	int	ord;
	spoly	*sseq;
{
	int	i;
	double	f, *fp, *fc;
	spoly	*sp;

	/*
	 * guard against degenerate cases
	 */
	while (ord > 0 && sseq[0].coef[ord] == 0.0)
		ord--;

	if (ord < 1) {
		sseq[0].ord = 0;
		return(0);
	}


	sseq[0].ord = ord;
	sseq[1].ord = ord - 1;

	/*
	 * calculate the derivative and normalise the leading
	 * coefficient.
	 */
	f = fabs(sseq[0].coef[ord] * ord);
	fp = sseq[1].coef;
	fc = sseq[0].coef + 1;
	for (i = 1; i <= ord; i++)
		*fp++ = *fc++ * i / f;

	/*
	 * construct the rest of the Sturm sequence
	 */
	for (sp = sseq + 2; modp(sp - 2, sp - 1, sp); sp++) {

		/*
		 * reverse the sign and normalise
		 */
		f = -fabs(sp->coef[sp->ord]);
		for (fp = &sp->coef[sp->ord]; fp >= sp->coef; fp--)
			*fp /= f;
	}

	sp->coef[0] = -sp->coef[0];	/* reverse the sign */

	return(sp - sseq);
}

/*
 * numroots
 *
 *	return the number of distinct real roots of the polynomial
 * described in sseq.
 */
int
numroots(np, sseq, atneg, atpos)
	int	np;
	spoly	*sseq;
	int		*atneg, *atpos;
{
	int	atposinf, atneginf;
	spoly	*s;
	double	f, lf;

	atposinf = atneginf = 0;

	/*
	 * changes at positve infinity
	 */
	lf = sseq[0].coef[sseq[0].ord];

	for (s = sseq + 1; s <= sseq + np; s++) {
		f = s->coef[s->ord];
		if (lf == 0.0 || lf * f < 0)
			atposinf++;
		lf = f;
	}

	/*
	 * changes at negative infinity
	 */
	if (sseq[0].ord & 1)
		lf = -sseq[0].coef[sseq[0].ord];
	else
		lf = sseq[0].coef[sseq[0].ord];

	for (s = sseq + 1; s <= sseq + np; s++) {
		if (s->ord & 1)
			f = -s->coef[s->ord];
		else
			f = s->coef[s->ord];
		if (lf == 0.0 || lf * f < 0)
			atneginf++;
		lf = f;
	}

	*atneg = atneginf;
	*atpos = atposinf;

	return(atneginf - atposinf);
}

/*
 * numchanges
 *
 *	return the number of sign changes in the Sturm sequence in
 * sseq at the value a.
 */
int
numchanges(np, sseq, a)
	int	np;
	spoly	*sseq;
	double	a;
{
	int	changes;
	double	f, lf;
	spoly	*s;

	changes = 0;

	lf = evalpoly(sseq[0].ord, sseq[0].coef, a);

	for (s = sseq + 1; s <= sseq + np; s++) {
		f = evalpoly(s->ord, s->coef, a);
		if (lf == 0.0 || lf * f < 0)
			changes++;
		lf = f;
	}

	return(changes);
}
