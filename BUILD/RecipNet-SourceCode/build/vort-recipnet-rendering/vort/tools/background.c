/*
 * background.c
 */
#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <math.h>
#include "vort.h"
#include "status.h"


/*
 * smalloc
 *
 *      allocate bytes for a scanline
 */
unsigned char *
smalloc(i)
	int     i;
{
	unsigned char	*p;

	if ((p = (unsigned char *)calloc(i, 1)) == (unsigned char *)NULL) {
		fprintf(stderr, "background: smalloc: no space for scan line.\n");
		exit(3);
	}

	return(p);
}

/*
 * produce a copy of the input file with a different background colour.
 */
main(ac, av)
	int	ac;
	char	*av[];
{
	image		*in, *out, *backgnd;
	int		i, width, height;
	float		rb, gb, bb, fact, val;
	unsigned char	*red, *green, *blue, *alpha;
	unsigned char	*redback, *greenback, *blueback, *alphaback;

	if (ac != 6 && ac != 4) {
		fprintf(stderr, "usage: background r g b file1 file2\n");
		exit(1);
	}

	if (ac == 6) {
		if ((in = openimage(av[4], "r")) == (image *)NULL) {
			fprintf(stderr, "background: can't open input file %s.\n", av[4]);
			exit(1);
		}

		if ((out = openimage(av[5], "w")) == (image *)NULL) {
			fprintf(stderr, "background: can't open output file %s.\n", av[5]);
			exit(1);
		}

		rb = atof(av[1]) * 255.0;
		gb = atof(av[2]) * 255.0;
		bb = atof(av[3]) * 255.0;
	} else {
		if ((backgnd = openimage(av[1], "r")) == (image *)NULL) {
			fprintf(stderr, "background: can't open input file %s.\n", av[1]);
			exit(1);
		}

		if ((in = openimage(av[2], "r")) == (image *)NULL) {
			fprintf(stderr, "background: can't open input file %s.\n", av[2]);
			exit(1);
		}

		if ((out = openimage(av[3], "w")) == (image *)NULL) {
			fprintf(stderr, "background: can't open output file %s.\n", av[3]);
			exit(1);
		}

		redback = (unsigned char *)smalloc(imagewidth(in));
		greenback = (unsigned char *)smalloc(imagewidth(in));
		blueback = (unsigned char *)smalloc(imagewidth(in));
		alphaback = (unsigned char *)smalloc(imagewidth(in));
	}

	red = (unsigned char *)smalloc(imagewidth(in));
	green = (unsigned char *)smalloc(imagewidth(in));
	blue = (unsigned char *)smalloc(imagewidth(in));
	alpha = (unsigned char *)smalloc(imagewidth(in));

	copyheader(out, in);

	if (ac == 6) {		/* set to new colour */
		imagebackgnd(out).r = rb;
		imagebackgnd(out).g = gb;
		imagebackgnd(out).b = bb;
	} else {		/* set to background of background image */
		imagebackgnd(out).r = imagebackgnd(backgnd).r;
		imagebackgnd(out).g = imagebackgnd(backgnd).g;
		imagebackgnd(out).b = imagebackgnd(backgnd).b;
	}

	writeheader(out);

	if (alphachannel(in)) {
		if (ac == 6) {		/* blending in another background colour */
			while (readrgbaline(in, red, green, blue, alpha)) {
				for (i = 0; i != imagewidth(in); i++)
					if (alpha[i] != 255) {
						fact = (255.0 - alpha[i]) / 255.0;
						red[i] += fact * (rb - imagebackgnd(in).r);
						green[i] += fact * (gb - imagebackgnd(in).g);
						blue[i] += fact * (bb - imagebackgnd(in).b);
					}
				writergbaline(out, red, green, blue, alpha);
			}
		} else {		/* compositing over another image */
			while (readrgbaline(in, red, green, blue, alpha)) {
				readrgbaline(backgnd, redback, greenback, blueback, alphaback);
				for (i = 0; i != imagewidth(in); i++)
					if (alpha[i] != 255) {
						fact = (255.0 - alpha[i]) / 255.0;
						val = red[i] + ((float)redback[i] - imagebackgnd(in).r) * fact;
						red[i] = (val > 255.0) ? 255 : val;
						val = green[i] + ((float)greenback[i] - imagebackgnd(in).g) * fact;
						green[i] = (val > 255.0) ? 255 : val;
						val = blue[i] + ((float)blueback[i] - imagebackgnd(in).b) * fact;
						blue[i] = (val > 255.0) ? 255 : val;
						val = alpha[i] + (float)alphaback[i] * fact;
						alpha[i] = (val > 255.0) ? 255 : val;
					}
				writergbaline(out, red, green, blue, alpha);
			}

			closeimage(backgnd);
		}
	}

	closeimage(in);
	closeimage(out);
}
