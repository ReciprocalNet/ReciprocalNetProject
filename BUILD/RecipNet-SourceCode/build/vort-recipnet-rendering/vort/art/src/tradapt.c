/*
 * tradapt.c
 *
 *	Simple adaptive-sampling rendering for art.
 *	A pixel is supersampled if the first sample on it
 *	has a contrast with any orthogonal neighbor
 *	that exceeds aathreshold.
 */

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "art.h"
#include "objs.h"
#include "macro.h"

extern hlist *trace ();
extern void shade ();

extern object *subobjs;
extern float *randp, *erandp;
extern float randtable[];
extern short raynumber;	/* initial raynumber */

extern real aathreshold;	/* Max contrast before we jitterbug. */
extern int  chatty;

static int extrarays,	/* if we jitter, do this many */
  	   totrays;     /* so this many in all */
static real scale;	/* Cached 1/xscale or 1/yscale */
static real xoffset,
  	    yoffset;     /* xstart - xsize/2,  (and y) */

/*
 * Arrays for scanlines.
 * We compare each new pixel with 2 neighbors and
 * supersample pairs when there's need.  So we
 * keep 2 scanlines and write a line after the next
 * has been scanned.
 *
 * To keep messy offsets out of the array indexes,
 * scanlines will point to element [1] of a width+2 array,
 * so the outer pixel can be indexed -1.
 */
typedef unsigned char int8;

static real *red, *green, *blue, *alpha,
            *old_red, *old_green, *old_blue, *old_alpha;
static int8 *supered,
 	    *old_supered; /* flags... has pt been supersampled? */

static int8 *outred, *outgreen, *outblue, *outalpha;    /* for image file */

static void 
init_rasters (xsize)
	int xsize;
{
	/*
	 * The ++ give us a [-1] element in the array.
	 * If we want to free the arrays after, have to decrement!
	 */
	red 	= (real *) smalloc((xsize + 2) * sizeof (real)); red++;
	green 	= (real *) smalloc((xsize + 2) * sizeof (real)); green++;
	blue 	= (real *) smalloc((xsize + 2) * sizeof (real)); blue++;
	alpha 	= (real *) smalloc((xsize + 2) * sizeof (real)); alpha++;
	old_red   = (real *) smalloc((xsize + 2) * sizeof (real)); old_red++;
	old_green = (real *) smalloc((xsize + 2) * sizeof (real)); old_green++;
	old_blue  = (real *) smalloc((xsize + 2) * sizeof (real)); old_blue++;
	old_alpha = (real *) smalloc((xsize + 2) * sizeof (real)); old_alpha++;

	supered =     (int8 *) smalloc(sizeof (int8) * (xsize + 2)); supered++;
	old_supered = (int8 *) smalloc(sizeof (int8) * (xsize + 2)); supered++;

	outred   = (int8 *) smalloc (sizeof (int8) * (xsize));
	outgreen = (int8 *) smalloc (sizeof (int8) * (xsize));
	outblue  = (int8 *) smalloc (sizeof (int8) * (xsize));
	outalpha = (int8 *) smalloc (sizeof (int8) * (xsize));
}

/*
 * swaplines - exchange pointers so the current line
 * becomes the previous line and the previous line
 * gets reused for the upcoming line.
 */
static int 
swaplines ()
{
#define	SWAP(A,B)   tmp = A; A = B; B = tmp
	void *tmp;

	SWAP (old_red, red);
	SWAP (old_green, green);
	SWAP (old_blue, blue);
	SWAP (old_alpha, alpha);
	SWAP (old_supered, supered);
}

/*
 * superneeded ?
 *
 *	True if the colors are far enough apart to need supersampling.
 *	aathreshold should range from 0 to 3.
 *	If 0, everything gets supersampled; if 3, nothing does.
 *	fabs() may be costly on most machines, so a macro might be better.
 */
static int 
superneeded(r1, g1, b1, a1, r2, g2, b2, a2)
	real	r1, g1, b1, a1, r2, g2, b2, a2;
{
	return (fabs(r2 - r1) + fabs(g2 - g1) + fabs(b2 - b1) + fabs(a2 - a1)) > aathreshold;
}

/*
 * sample_once
 *
 *	Generate a sample value for a given ray.
 */
static void 
sample_once(truex, truey, r, g, b, a)
	real truex, truey, *r, *g, *b, *a;
{
	pixel sample;

	gensample((float) truex, (float) truey, &sample);
	*r = sample.r;
	*g = sample.g;
	*b = sample.b;
	*a = sample.a;
}

/*
 * supersample
 *
 * 	Do extrarays more samples
 * 	around the given point, averaging the result.
 * 	pr,pg,pb, pa are <-> params; they hold the first sample
 * 	at entry and the averaged sample at exit.
 */
static void 
supersample(x, y, pr, pg, pb, pa)
	real x, y, *pr, *pg, *pb, *pa;
{
	real truexbase = x + xoffset;	/* Omit .5; randnum puts it back. */
	real trueybase = y + yoffset;

	register real lred = *pr;
	register real lgreen = *pg;
	register real lblue = *pb;
	register real lalpha = *pa;
	int i, jitter = TRUE;
	register object *o;
	float		k1, k2;
	sampleMask	*raySampleMask = getSquareSampleMask(extrarays + 1);

	for (i = 0; i < extrarays; i++)
	{
		k1 = truexbase + raySampleMask->points[i].u;
		k2 = trueybase + raySampleMask->points[i].v;

		if (jitter) {
			k1 += raySampleMask->magnitude
					* (1.0 - randnum() * 2);
			k2 += raySampleMask->magnitude
					* (1.0 - randnum() * 2);
		}

		sample_once ((k1 * scale),
			     (k2 * scale),
			     pr, pg, pb, pa);
		lred += *pr;
		lgreen += *pg;
		lblue += *pb;
		lalpha += *pa;
	}

	*pr = lred / totrays;
	*pg = lgreen / totrays;
	*pb = lblue / totrays;
	*pa = lalpha / totrays;

	if (raynumber < 0)
	{
		for (o = subobjs; o != (object *) NULL; o = o->nxt)
			o->lastray.raynumber = 0;
		raynumber = 1;
	}
}

/*
 * trace_point
 *
 *	Trace the given pixel and supersample as needed:
 * 	if the basic sample for this and its upper or left neighbor
 *	differ too much,  supersample both this and neighbor.
 */
static void 
trace_point(x, y)
	int	x, y;
{
	real truex = (x + xoffset + 0.5) * scale;
	real truey = (y + yoffset + 0.5) * scale;

	sample_once (truex, truey, &red[x], &green[x], &blue[x], &alpha[x]);

	if (superneeded (red[x], green[x], blue[x], alpha[x],
			 old_red[x], old_green[x], old_blue[x], old_alpha[x]))
	{
		if (!old_supered[x])
		{
			supersample ((real)x, (real)y + 1, 
				     &old_red[x], &old_green[x], &old_blue[x], 
				     &old_alpha[x]);
			old_supered[x] = 1;
		}

		/*
		 * Don't bother supersampling the offscreen line at the
		 * bottom.
		 */
		if (y >= 0)
		{
			supersample ((real)x, (real)y, &red[x], &green[x], &blue[x],
				     &alpha[x]);
			supered[x] = 1;
		}
	}

	if (superneeded (red[x], green[x], blue[x], alpha[x],
			 red[x-1], green[x-1], blue[x-1], alpha[x - 1]))
	{
		if (!supered[x-1] && y >= 0)
		{
			supersample ((real)x-1, (real)y, &red[x-1], &green[x-1], &blue[x-1],
				     &alpha[x-1]);
			supered[x-1] = 1;
		}
		if (!supered[x] && y >= 0)
		{
			supersample ((real)x, (real)y, &red[x], &green[x], &blue[x], 
				     &alpha[x]);
			supered[x] = 1;
		}
	}
}

/* dotrace_adaptive
 *
 * 	Trace the image doing antialiasing where needed.
 *	To do this, we keep the current and previous scan lines in arrays.
 *	We do some juggling here so the loops run in fragment coordinates
 *	rather than image coordinates, so the array indexes are -1..fragsize
 *	including the extra pixels at either end.
 */
dotrace_adaptive (im, nrays, ixsize, iysize, ixstart, ixend, iystart, iyend)
	image *im;
	int nrays;
	int ixsize,
	  iysize,
	  ixstart,
	  ixend,
	  iystart,
	  iyend;
{
	register int x, y;
	real truex, truey;
	int fragxsize = ixend - ixstart + 1;
	int fragysize = iystart - iyend + 1;

	if (ixsize > iysize)
		scale = 2.0 / iysize;  /* I.e. 1/(size/2) */
	else
		scale = 2.0 / ixsize;

	extrarays = nrays - 1;
	totrays = nrays;
	init_rasters (fragxsize);

	/* Offsets for points (fragment coordinates to centered world) */

	xoffset = ((real) ixstart) - ixsize / 2.0;
	yoffset = ((real) iyend) - iysize / 2.0;

	/* Load 'first previous' y line with samples. */

	truey = (fragysize + yoffset + 0.5) * scale;
	for (x = -1; x <= fragxsize; x++)
	{
		truex = (x + xoffset + 0.5) * scale;
		sample_once (truex, truey,
			     &old_red[x],
			     &old_green[x],
			     &old_blue[x],
			     &old_alpha[x]);
		old_supered[x] = 1;    /* Lie so we don't supersample
				        * offscreen */
	}

	truex = (xoffset - 0.5) * scale;	/* For x=-1 initial pixel
						 * in each line. */

	for (y = fragysize - 1; y >= -1; y--)
	{
		if (y > -1 && chatty)
			printprogress (iysize - y - 1);

		/* Sample the offscreen first pixel in the line. */

		sample_once (truex,
			     (y + yoffset + 0.5) * scale,
			     &red[-1], &green[-1], &blue[-1], &alpha[-1]);

		supered[-1] = 1;       /* Lie so we don't supersample
				        * offscreen */

		/*
		 * Scan the line.
		 */
		for (x = 0; x <= fragxsize; x++)
		{
			supered[x] = 0;
			trace_point (x, y);
		}

		/*
		 * Now we can write out the -previous- line, which we may
		 * have supersampled doing this line.
		 */
		if (y < fragysize - 1)
		{
			for (x = 0; x < fragxsize; x++)
			{
				outred[x] = (old_red[x] * 255.0);
				outgreen[x] = (old_green[x] * 255.0);
				outblue[x] = (old_blue[x] * 255.0);
				outalpha[x] = (old_alpha[x] * 255.0);
			}
			if (alphachannel (im))
				writergbaline (im, outred, outgreen, outblue, 
						outalpha);
			else
				writergbline (im, outred, outgreen, outblue);
		}
		swaplines ();
	}
}
