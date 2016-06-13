#include <math.h>
#include <stdio.h>
#include "art.h"
#include "macro.h"

/*
 * adjustbbox
 *
 *	adjust the bounding box for a tree for the object o
 */
adjustbbox(bb, o)
	bbox	*bb;
	object	*o;
{
	if (o->bb.min[X] < bb->min[X])
		bb->min[X] = o->bb.min[X];
	if (o->bb.max[X] > bb->max[X])
		bb->max[X] = o->bb.max[X];

	if (o->bb.min[Y] < bb->min[Y])
		bb->min[Y] = o->bb.min[Y];
	if (o->bb.max[Y] > bb->max[Y])
		bb->max[Y] = o->bb.max[Y];

	if (o->bb.min[Z] < bb->min[Z])
		bb->min[Z] = o->bb.min[Z];
	if (o->bb.max[Z] > bb->max[Z])
		bb->max[Z] = o->bb.max[Z];
}

/*
 * missedbbox
 *
 *	checks a ray against a bounding box returning FALSE if it intersects
 * TRUE otherwise. 
 */
int
missedbbox(r, bb)
	ray	*r;
	bbox	*bb;
{
	real	d, o, t, t1, t2, b1, b2;

	if (r->org.x >= bb->min[X] && r->org.x <= bb->max[X]
	   && r->org.y >= bb->min[Y] && r->org.y <= bb->max[Y]
	   && r->org.z >= bb->min[Z] && r->org.z <= bb->max[Z])
		return(FALSE);

	t1 = 0.0;
	t2 = HUGE_DIST;

	d = r->dir.x;
	o = r->org.x;
	if (d != 0.0) {

		if (d < 0.0) {
			b2 = bb->min[X] - o;
			if (b2 > 0.0)
				return(TRUE);
			b1 = bb->max[X] - o;
		} else {
			b2 = bb->max[X] - o;
			if (b2 < 0.0)
				return(TRUE);
			b1 = bb->min[X] - o;
		}
			
		t2 = b2 / d;
		t1 = b1 / d;
		if (t1 < 0.0)
			t1 = 0.0;
	} else if (o < bb->min[X] || o > bb->max[X])
		return(TRUE);

	d = r->dir.y;
	o = r->org.y;
	if (d != 0.0) {

		if (d < 0.0) {
			b2 = bb->min[Y] - o;
			if (b2 > 0.0)
				return(TRUE);
			b1 = bb->max[Y] - o;
		} else {
			b2 = bb->max[Y] - o;
			if (b2 < 0.0)
				return(TRUE);
			b1 = bb->min[Y] - o;
		}
			
		if ((t = b2 / d) < t1)
			return(TRUE);

		if (t < t2)
			t2 = t;

		if ((t = b1 / d) > t2)
			return(TRUE);

		if (t > t1)
			t1 = t;
	} else if (o < bb->min[Y] || o > bb->max[Y])
		return(TRUE);

	d = r->dir.z;
	o = r->org.z;
	if (d != 0.0) {
		if (d < 0.0) {
			b2 = bb->min[Z] - o;
			if (b2 > 0.0)
				return(TRUE);
			b1 = bb->max[Z] - o;
		} else {
			b2 = bb->max[Z] - o;
			if (b2 < 0.0)
				return(TRUE);
			b1 = bb->min[Z] - o;
		}

		if (b2 / d < t1)
			return(TRUE);

		if (b1 / d > t2)
			return(TRUE);
	} else if (o < bb->min[Z] || o > bb->max[Z])
		return(TRUE);

	return(FALSE);
}

/*
 * inbbox
 *
 *	checks a ray against a bounding box returning TRUE if it intersects
 * FALSE otherwise. *pt1 and *pt2 are returned with the entry and exit
 * distances of the ray. If the ray is in the box *pt1 represents the
 * distance to the box in front of the observer, *pt2 the distance behind.
 */
int
inbbox(r, bb, pt1, pt2)
	ray	*r;
	bbox	*bb;
	float	*pt1, *pt2;
{
	float	d, o, t, t1, t2, b1, b2;

	t1 = 0.0;
	t2 = HUGE_DIST;

	d = r->dir.x;
	o = r->org.x;
	if (d != 0.0) {

		if (d < 0.0) {
			b2 = bb->min[X] - o;
			if (b2 > 0.0)
				return(FALSE);
			b1 = bb->max[X] - o;
		} else {
			b2 = bb->max[X] - o;
			if (b2 < 0.0)
				return(FALSE);
			b1 = bb->min[X] - o;
		}
			
		t2 = b2 / d;
		t1 = b1 / d;
		if (t1 < 0.0)
			t1 = 0.0;
	} else if (o < bb->min[X] || o > bb->max[X])
		return(FALSE);

	d = r->dir.y;
	o = r->org.y;
	if (d != 0.0) {

		if (d < 0.0) {
			b2 = bb->min[Y] - o;
			if (b2 > 0.0)
				return(FALSE);
			b1 = bb->max[Y] - o;
		} else {
			b2 = bb->max[Y] - o;
			if (b2 < 0.0)
				return(FALSE);
			b1 = bb->min[Y] - o;
		}
			
		if ((t = b2 / d) < t1)
			return(FALSE);

		if (t < t2)
			t2 = t;

		if ((t = b1 / d) > t2)
			return(FALSE);

		if (t > t1)
			t1 = t;
	} else if (o < bb->min[Y] || o > bb->max[Y])
		return(FALSE);

	d = r->dir.z;
	o = r->org.z;
	if (d != 0.0) {
		if (d < 0.0) {
			b2 = bb->min[Z] - o;
			if (b2 > 0.0)
				return(FALSE);
			b1 = bb->max[Z] - o;
		} else {
			b2 = bb->max[Z] - o;
			if (b2 < 0.0)
				return(FALSE);
			b1 = bb->min[Z] - o;
		}

		if ((t = (b2 / d)) < t1)
			return(FALSE);
		
		if (t < t2)
			t2 = t;

		if ((t = (b1 / d)) > t2)
			return(FALSE);

		if (t > t1)
			t1 = t;
	} else if (o < bb->min[Z] || o > bb->max[Z])
		return(FALSE);

	*pt1 = t1;
	*pt2 = t2;

	return(TRUE);
}
