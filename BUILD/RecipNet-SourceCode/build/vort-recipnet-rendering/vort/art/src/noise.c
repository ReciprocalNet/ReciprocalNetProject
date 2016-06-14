/*
 * This is it!. Ken Perlin's noise function... straight from him
 * (Well, SIGGRAPH '92 tutorial 23  course notes)
 */


#include <stdio.h>
#include <math.h>
#include "art.h"
#include "macro.h"

#define B	512
#define NUMPTS  B

extern  float randtable[];
float   *rp = randtable, *erp = &randtable[NUMPTS];     /* One more! */
#define randinx()       ((rp == erp) ? *(rp = randtable) : *rp++)

static  int     noise_inited = 0;


static	int	*p;
static	vector	*g;

#define setup(i, b0, b1, r0, r1)\
	t = i + 10000.0;\
	if (t < 0.0) t += 10000.0;\
	b0 = (int)t & (B - 1);\
	b1 = (b0 + 1) & (B - 1);\
	r0 = t - (int)t;\
	r1 = r0 -1.0;

float
noise(vec)
	vector	*vec;
{
	int	bx0, bx1, by0, by1, bz0, bz1, b00, b10, b01, b11;
	real	rx0, rx1, ry0, ry1, rz0, rz1, sx, sy, sz, a, b, c, d, t, u, v;
	vector	*q;
	register	i, j;


	setup(vec->x, bx0, bx1, rx0, rx1);
	setup(vec->y, by0, by1, ry0, ry1);
	setup(vec->z, bz0, bz1, rz0, rz1);

	i = p[bx0];
	j = p[bx1];

	b00 = p[i + by0];
	b10 = p[j + by0];
	b01 = p[i + by1];
	b11 = p[j + by1];

#define	at(rx, ry, rz)	(rx * q->x + ry * q->y + rz * q->z)
#define	s_curve(t)	(t * t * (3.0 - 2.0 * t))
#define	lerp(t, a, b)	(a + t * (b - a))

	sx = s_curve(rx0);
	sy = s_curve(ry0);
	sz = s_curve(rz0);

	q = &g[b00 + bz0]; u = at(rx0, ry0, rz0);
	q = &g[b10 + bz0]; v = at(rx1, ry0, rz0);
	a = lerp(sx, u, v);

	q = &g[b01 + bz0]; u = at(rx0, ry1, rz0);
	q = &g[b11 + bz0]; v = at(rx1, ry1, rz0);
	b = lerp(sx, u, v);

	c = lerp(sy, a, b);		/* interpolate in y at lo x */

	q = &g[b00 + bz1]; u = at(rx0, ry0, rz1);
	q = &g[b10 + bz1]; v = at(rx1, ry0, rz1);
	a = lerp(sx, u, v);

	q = &g[b01 + bz1]; u = at(rx0, ry1, rz1);
	q = &g[b11 + bz1]; v = at(rx1, ry1, rz1);
	b = lerp(sx, u, v);

	d = lerp(sy, a, b);		/* interpolate in y at hi x */

	return( 1.5 * lerp(sz, c, d));	/* interpolate in z */

}

/*
 * init_noise
 *
 */
init_noise()
{
	vector	v;
	int	i, n = 0, bonk;
	real	a;

	if (noise_inited) 
		return;
	
	g = (vector *)smalloc((NUMPTS + NUMPTS + 2) * sizeof(vector));
	p = (int *)smalloc((NUMPTS + NUMPTS + 2) * sizeof(int));

	i = 0;
	while (n < NUMPTS) {
		v.x = 2.0 * randinx() - 1.0;
		v.y = 2.0 * randinx() - 1.0;
		v.z = 2.0 * randinx() - 1.0;

		if ((a = dprod(v, v)) <= 1.0) {
			/*
			 * Normalise (the x, y, z compenents of) v...
			 */
			a = sqrt((double)a);
			if (a > 0.0) {
				v.x /= a;
				v.y /= a;
				v.z /= a;
			}
			g[n].x = v.x;
			g[n].y = v.y;
			g[n].z = v.z;
			n++;
		}
	}


	/*
	 * Set a random permutation of the first NUMPTS integers
	 */
	for (n = 0; n < NUMPTS; n++)
		p[n] = n;

	for (n = 0; n < NUMPTS; n++) {
		i = (int)(randtable[n] * (NUMPTS - 1));
		bonk = p[n];
		p[n] = p[i];
		p[i] = bonk;
	}

	/* Extend g and p arrays to allow faster indexing */
	for (i = 0; i < B + 2; i++) {
		p[B + i] = p[i];
		g[B + i].x = g[i].x;
		g[B + i].y = g[i].y;
		g[B + i].z = g[i].z;
	}

	noise_inited = 1;
}

#define OFFSET1	1000.12324
#define OFFSET2	2000.45631
#define OFFSET3	4000.98763

/*
 * Vnoise
 *
 *      A vector valued noise function.
 *      (Again, from the '89 paper pp 259)
 */
void
Vnoise(p, v)
        vector  *p, *v;
{
	vector s;

	s.x = p->x - OFFSET1;
	s.y = p->y - OFFSET1;
	s.z = p->z - OFFSET1;

	v->x = noise(&s);

	s.x = p->x + OFFSET1;
	s.y = p->y - OFFSET1;
	s.z = p->z + OFFSET3;

	v->y = noise(&s);

	s.x = p->x + OFFSET2;
	s.y = p->y + OFFSET2;
	s.z = p->z + OFFSET2;

	v->z = noise(&s);
}
