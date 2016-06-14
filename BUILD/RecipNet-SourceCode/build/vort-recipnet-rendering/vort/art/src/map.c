#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

extern attr	*astackp;
extern hlist	*fhlist;

/*
 * spheremap
 *
 *	return the (u, v) values for the inverse mapping of a canonical
 * sphere given the point on the spheres surface.
 *
 */
void
spheremap(l, u, v)
	vector	*l;
	float	*u, *v;
{
	double	t;
	double	phi, theta;
	vector	loc;

	t = -l->z;

	if (fabs(t) > 1.0)		/* precision check */
		t = (t < 0.0) ? -1.0 : 1.0;

	phi = acos(t);

	*v = phi / M_PI;

	if (*v != 0.0 && *v != 1.0) {
		loc.x = l->x;
		loc.y = l->y;
		loc.z = 0.0;

		normalise(loc);

		if (fabs(loc.x) > 1.0)		/* precision check */
			loc.x = (loc.x < 0.0) ? -1.0 : 1.0;

		theta = acos(loc.x) / (2 * M_PI);

		if (loc.y > 0.0)
			*u = theta;
		else
			*u = 1 - theta;
	} else
		*u = 0.0;
}
