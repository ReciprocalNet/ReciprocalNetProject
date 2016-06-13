/*
 * vorttoppm.c - read a VORT raster file and produce a portable pixmap *
 * This is public domain.
 */

#include <stdio.h>
#include <stdlib.h>
#include "vort.h"

main(argc, argv)
	int	argc;
	char	*argv[];
{
	int		x, y, w, h;
	unsigned char	*line, *red, *green, *blue;
	image		*im;
	char		*fname;
	char		buf[128];

	if (argc == 1)
		fname = "-";
	else
		fname = argv[1];

	if ((im = openimage(fname, "r")) == (image *)NULL) {
		fprintf(stderr, "Unable to open VORT file");
		exit(1);
	}

	w = imagewidth(im);
	h = imageheight(im);

	if (!colormapped(im)) {
		if ((red = (unsigned char *)malloc((unsigned)w)) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(1);
		}
		if ((green = (unsigned char *)malloc((unsigned)w)) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(1);
		}
		if ((blue = (unsigned char *)malloc((unsigned)w)) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(1);
		}
	} else
		if ((line = (unsigned char *)malloc((unsigned)w)) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(1);
		}


	/*
	 * The header...
	 */
	printf("P6\n%d %d\n255\n", w, h);

	for (y = 0; y < h; y++) {
		if (colormapped(im)) {
			readmappedline(im, line);
			for (x = 0; x < w; x++) {
				putchar(redmap(im)[line[x]]);
				putchar(greenmap(im)[line[x]]);
				putchar(bluemap(im)[line[x]]);
			}
		} else {
			readrgbline(im, red, green, blue);
			for (x = 0; x < w; x++) {
				putchar(red[x]);
				putchar(green[x]);
				putchar(blue[x]);
			}
		}
	}

	exit(0);
}
