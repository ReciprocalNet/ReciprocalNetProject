#include <stdio.h>
#include <math.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern hlist	*trace();
extern hlist	*fhlist;

extern int	perspective;
extern float	focallength;
extern float	ri;
extern short	raynumber;
extern object	*oblist;
extern float	screenx, screeny;
extern colour	backcol;

/*
 * gensample
 *
 *	generate a sample value for a given ray
 */
gensample(k1, k2, sample)
	float	k1, k2;
	pixel	*sample;
{
	ray	ir;
	hlist	*hit, *lp, *p;
	float	fact;

	if (perspective) {
		ir.org.x = ir.dir.x = k1;
		ir.org.y = ir.dir.y = k2;
		
		ir.org.z = 0.0;
		ir.dir.z = focallength;
		normalise(ir.dir);
	} else {
		ir.org.x = k1;
		ir.org.y = k2;
		ir.org.z = 0.0;

		ir.dir.x = 0.0;
		ir.dir.y = 0.0;
		ir.dir.z = -1.0;
	}

	ir.org.x *= screenx;
	ir.org.y *= screeny;

	ir.ri = ri;
	ir.type = PRIMARY_RAY;
	ir.raynumber = raynumber++;

	ir.orgobj = (object *)NULL;

	if ((hit = trace(&ir, oblist)) != (hlist *)NULL) {
		shade(sample, &ir, hit, 0);
		sample->a = 1.0;
	} else {
		sample->r = backcol.r;
		sample->g = backcol.g;
		sample->b = backcol.b;
		sample->a = 0.0;
	}

	for (lp = hit; lp != (hlist *)NULL; lp = p) {
		p = lp->nxt;
		release(lp);
	}
}

