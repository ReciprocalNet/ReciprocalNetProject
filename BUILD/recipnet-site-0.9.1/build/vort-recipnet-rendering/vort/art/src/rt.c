#include <stdio.h>
#include <math.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern light	*lights;
extern hlist	*fhlist;
extern int	maxhitlevel;

extern pixel	backcol;
extern colour	ambient;

extern hlist	*(*intersects[NUM_OBJS])();

/*
 * trace
 *
 *	returns a list of hits on the first object hit by r
 */
hlist *
trace(r, oblist)
	register ray	*r;
	object		*oblist;
{
	register object	*o;
	register hlist	*hit, *prevhits, *p, *np;
	hlist		*end;

	prevhits = (hlist *)NULL;

	for (o = oblist; o != (object *)NULL; o = o->nxt) {
		if ((hit = intersects[o->type](r, o, &end)) != (hlist *)NULL) {
			if (prevhits == (hlist *)NULL)
				prevhits = hit;
			else {
				if (prevhits->t > hit->t) {
					for (p = prevhits; p != (hlist *)NULL; p = np) {
						np = p->nxt;
						release(p)
					}
					prevhits = hit;
				} else {
					for (p = hit; p != (hlist *)NULL; p = np) {
						np = p->nxt;
						release(p)
					}
				}
			}
		}
	}

	return(prevhits);
}

