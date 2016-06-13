#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "vort.h"

/*
 * program to convert a pix file into straight rgb and an ascii
 * header. The ascii header ends in .hdr, and the raster file
 * in .rgb.
 */
main(ac, av)
	int	ac;
	char	**av;
{
	int	i;
	image	*im;
	char	*p, *red, *green, *blue;
	FILE	*out;

	if (ac != 2) {
		fprintf(stderr, "pix2simp: usage pix2simp pixfile.\n");
		exit(1);
	}

	imagebufsize(8192);
	if ((im = openimage(av[1], "r")) == (image *)NULL) {
		fprintf(stderr, "pix2simp: can't open image file.\n");
		exit(1);
	}

	if ((p = strrchr(av[1], '.')) == (char *)NULL) {
		fprintf(stderr, "pix2simp: image file name must end in .pix.\n");
		exit(1);
	}

	/*
	 * do the rgb
	 */
	strcpy(p, ".rgb");

	if ((out = fopen(av[1], "w")) == (FILE *)NULL) {
		fprintf(stderr, "pix2simp: can't open .rgb file.\n");
		exit(1);
	}

	red = malloc(imagewidth(im));
	green = malloc(imagewidth(im));
	blue = malloc(imagewidth(im));

	while (readrgbline(im, red, green, blue)) {
		for (i = 0; i < imagewidth(im); i++) {
			fwrite(&red[i], 1, 1, out);
			fwrite(&green[i], 1, 1, out);
			fwrite(&blue[i], 1, 1, out);
		}
	}

	fclose(out);

	strcpy(p, ".hdr");

	if ((out = fopen(av[1], "w")) == (FILE *)NULL) {
		fprintf(stderr, "pix2simp: can't open .hdr file.\n");
		exit(1);
	}

	fprintf(out, "Imagetitle:	%s\n", imagetitle(im));
	fprintf(out, "Creation date:	%s", ctime(&imagedate(im)));
	fprintf(out, "Dimensions:	width %d height %d.\n", imagewidth(im), imageheight(im));

	fclose(out);

	closeimage(im);
}

