#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern attr	*astackp;
extern mats	*mstackp;

extern matrix	trans;

extern int	maxhitlevel;

extern light	*lights;

/*
 * lightinit
 *
 *	initialise the function pointers and fields for a light object,
 * returning a pointer to it.
 */
lightinit(d)
	details *d;
{
	light	*l;
	details	*ld;
	matrix	tmp;
	int	i;

		/* calculate current transformation matrix */
	calctransforms(mstackp);

	l = (light *)smalloc(sizeof(light));

	l->rad = 0.0;
	
	l->cosedge = l->cosin = 2.0;

	l->beamdist = 0.0;

	l->rays = 1;

	l->type = POINT;

	l->c.r = astackp->d.s->c.r;
	l->c.g = astackp->d.s->c.g;
	l->c.b = astackp->d.s->c.b;

	l->shadows = astackp->d.s->shadows;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
				/* transform light into ray space */

			vmmult(l->org, d->u.v, mstackp->d.obj2ray);
			break;
		case DIRECTION:

			smult(d->u.v, -1.0);
			normalise(d->u.v);
			v3x3mult(l->dir, d->u.v, mstackp->d.obj2ray)
			l->type = DIRECTIONAL;
			break;
		case RADIUS:
			l->rad = d->u.f;
			break;
		case ANGLE:
			l->cosedge = cos((double)d->u.f / 180.0 * M_PI);
			break;
		case INSIDEANGLE:
			l->cosin = cos((double)d->u.f / 180.0 * M_PI);
			break;
		case BEAMDISTRIBUTION:
			l->beamdist = d->u.f;
			break;
		case NUMRAYS:
			l->rays = d->u.f;
			break;
		default:
			warning("art: illegal field in light ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (l->type == DIRECTIONAL && l->cosedge == 2.0)
		l->type = DISTANT;

	l->c.r /= l->rays; 
	l->c.g /= l->rays;
	l->c.b /= l->rays;

	l->lasthits = (object **)scalloc(sizeof(object *), maxhitlevel + 1);

	for (i = 0; i <= maxhitlevel; i++)
		l->lasthits[i] = (object *)NULL;

	/*
	 * add to the list...
	 */
	l->nxt = lights;
	lights = l;
}
