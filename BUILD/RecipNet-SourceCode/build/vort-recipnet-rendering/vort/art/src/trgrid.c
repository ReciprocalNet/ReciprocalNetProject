#include <stdio.h>
#include <math.h>

#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern int	chatty;
extern short	raynumber;
extern object	*subobjs;
extern float	*randp, *erandp, randtable[];

/*
 * x, y offsets, scale, and sampling
 */
static int	xoff, yoff;
static float	xscale, yscale;
static int	ysperpix, xsperpix;

/*
 * anti aliasing stuff.
 */
static int	extrarays,      	/* spare rays for jittering */
		totrays;        	/* total number of rays per pixel */

static pixel	*topline;       /* scanline of sample grid at top of current scanline */

static pixel	others[2];      /* bottom 2 samples of pixel grid */

static float	averagefactor;	/* number to divide final samples through by */

/*
 * dotrace
 *
 *	traces the neccessary number of rays and calculates r,
 * g, b, and alpha info averaging the samples at the end.
 */
static
dotrace(x, y, r, g, b, a)
	int		x, y;
	unsigned char	*r, *g, *b, *a;
{
	int	i;
	int	jitter = TRUE;
	pixel	sample;
	float	red, green, blue, alpha;
	float	k1, k2;
	object	*o;
	sampleMask	*raySampleMask;

	red = green = blue = alpha = 0.0;

	gensample((x - xoff + 1) / xscale, (y - yoff) / yscale, &others[1]);

	red = (topline[x].r + topline[x + 1].r + others[0].r + others[1].r);
	green = (topline[x].g + topline[x + 1].g + others[0].g + others[1].g);
	blue = (topline[x].b + topline[x + 1].b + others[0].b + others[1].b);
	alpha = (topline[x].a + topline[x + 1].a + others[0].a + others[1].a);

	topline[x] = others[0];
	others[0] = others[1];

	raySampleMask = getSquareSampleMask(extrarays + 1);

	for (i = 0; i != extrarays; i++) {

		k1 = x - xoff + 0.5 + raySampleMask->points[i].u;
		k2 = y - yoff + 0.5 + raySampleMask->points[i].v;

		if (jitter) {
			k1 += raySampleMask->magnitude
					* (1.0 - randnum() * 2);
			k2 += raySampleMask->magnitude
					* (1.0 - randnum() * 2);
		}

		gensample(k1 / xscale, k2 / yscale, &sample);

		red += sample.r;
		green += sample.g;
		blue += sample.b;
		alpha += sample.a;
	}

	if (alpha != 0.0) {
		*r = red * averagefactor + randnum();
		*g = green * averagefactor + randnum();
		*b = blue * averagefactor + randnum();
		*a = alpha * averagefactor;
		if (*a == 0)			/* check for numerical probs */
			*a = 255;
	} else {
		*r = red * averagefactor;
		*g = green * averagefactor;
		*b = blue * averagefactor;
		*a = alpha * averagefactor;
	}

	if (raynumber < 0) {
		for (o = subobjs; o != (object *)NULL; o = o->nxt)
			o->lastray.raynumber = 0;
		raynumber = 1;
	}
}

/*
 * dotrace_grid
 *
 *	trace the image space using a pixel grid and super sampling
 * to help antialias the ray trace.
 */
dotrace_grid(im, raysperpix, xsize, ysize, xstart, xend, ystart, yend)
	image	*im;
	int	raysperpix, xsize, ysize, xstart, xend, ystart, yend;
{
	unsigned char	*red, *green, *blue, *alpha;
	float		k1, k2;
	int		i, x, y;

	xoff = xsize / 2;
	yoff = ysize / 2;

	if (xsize > ysize)
		xscale = yscale = ysize / 2;
	else
		xscale = yscale = xsize / 2;

	red = (unsigned char *)scalloc(xsize, 1);
	green = (unsigned char *)scalloc(xsize, 1);
	blue = (unsigned char *)scalloc(xsize, 1);
	alpha = (unsigned char *)scalloc(xsize, 1);

	extrarays = raysperpix - 1;
	totrays = extrarays + 4;

	averagefactor = 255.0 / totrays;

	topline = (pixel *)scalloc(sizeof(pixel), xend + 2);

	k2 = (ystart + 1 - yoff) / yscale;

	for (i = xstart; i <= xend + 1; i++)
		gensample((i - xoff) / xscale, k2, &topline[i]);

	k1 = (xstart - xoff) / xscale;

	for (y = ystart; y >= yend; y--) {

		if (chatty)
			printprogress(ysize - y - 1);

		gensample(k1, (y - yoff) / yscale, &others[0]);

		for (x = xstart; x <= xend; x++)
			dotrace(x, y, &red[x - xstart], &green[x - xstart], &blue[x - xstart], &alpha[x - xstart]);

		if (alphachannel(im))
			writergbaline(im, red, green, blue, alpha);
		else
			writergbline(im, red, green, blue);
	}
}
