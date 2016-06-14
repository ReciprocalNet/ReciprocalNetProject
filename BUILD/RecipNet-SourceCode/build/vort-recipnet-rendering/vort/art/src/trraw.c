
#include <stdio.h>
#include <math.h>

#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern int	chatty;
extern short	raynumber;
extern object	*subobjs;

/*
 * x, y offsets, scale.
 */
static int	xoff, yoff;
static float	xscale, yscale;

static unsigned char	*red, *green, *blue, *alpha;

extern int	debug;


/*
 * dotrace_raw
 *
 *	simple sample the image space, one ray per pixel.
 */
dotrace_raw(im, xsize, ysize, xstart, xend, ystart, yend)
	image	*im;
	int	xsize, ysize;
	int	xstart, xend, ystart, yend;
{
	pixel		pix;
	int		x, y;
	object		*o;

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

	for (y = ystart; y >= yend; y--) {

		if (chatty)
			printprogress(ysize - y - 1);

		for (x = xstart; x <= xend; x++) {

			gensample((x - xoff + 0.5) / xscale, (y - yoff + 0.5) / yscale, &pix);

			if (raynumber < 0) {
				for (o = subobjs; o != (object *)NULL; o = o->nxt)
					o->lastray.raynumber = 0;
				raynumber = 1;
			}

			red[x - xstart] = pix.r * 255.0;
			green[x - xstart] = pix.g * 255.0;
			blue[x - xstart] = pix.b * 255.0;
			alpha[x - xstart] = pix.a * 255.0;
		}

		if (alphachannel(im))
			writergbaline(im, red, green, blue, alpha);
		else
			writergbline(im, red, green, blue);
	}
}
