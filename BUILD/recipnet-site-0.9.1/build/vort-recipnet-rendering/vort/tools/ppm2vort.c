/*
 * ppm2vort.c - read a portable pixmap and produce a VORT file
 * 
 * Compile with: (In the tools directory)
 *
 *		cc -o ppmtovort ppmtovort.c -I../lib ../lib/libvort.a
 */

#include <stdio.h>
#include <stdlib.h>
#include "vort.h"

#define ASCII	111
#define RAW	222

main(argc, argv)
	int             argc;
	char           *argv[];
{

	FILE		*fp;
	unsigned char	*r;
	unsigned char	*g;
	unsigned char	*b;
	image		*im;
	int		i, type, w, h, maxval;

	/*
	 * Read from file or stdin...
	 */
	if (argc == 1)
		fp = stdin;
	else if ((fp = fopen(argv[1], "r")) == NULL) {
		fprintf(stderr, "Couldn't open %s for reading.\n", argv[1]);
		exit(1);
	}

	type = readppmheader(fp, &w, &h, &maxval);


	if ((r = (unsigned char *)malloc((unsigned)w)) == NULL) {
		fprintf(stderr, "Out of mem.\n");
		exit(1);
	}
	if ((g = (unsigned char *)malloc((unsigned)w)) == NULL) {
		fprintf(stderr, "Out of mem.\n");
		exit(1);
	}
	if ((b = (unsigned char *)malloc((unsigned)w)) == NULL) {
		fprintf(stderr, "Out of mem.\n");
		exit(1);
	}

	/*
	 * Write to stdout....
	 */
	if ((im = openimage("-", "w")) == (image *)NULL) {
		fprintf(stderr, "Can't open '-' for output!\n");
		exit(1);
	}

	imageheight(im) = h;
	imagewidth(im) = w;

	imagetitle(im) = "";
	titlelength(im) = 0;
	imagedate(im) = time(0L);

	/*
	 * We are going to write a 24bit VORT file anyway....
	 */

	imagetype(im) = PIX_RLE;
	imagedepth(im) = 24;
	writeheader(im);

	fprintf(stderr, "Writing 24 bit PIX_RLE VORT file....\n");

	for (i = 0; i < h; i++) {
		readppmline(fp, type, w, r, g, b);
		writergbline(im, r, g, b);
	}

	closeimage(im);
	fclose(fp);

	exit(0);
}

/*
 * Skips blanks and comments etc etc
 */
skipblanks(fp)
	FILE	*fp;
{
	int	c;

	while((c = fgetc(fp)) == ' ' || c == '\t' || c == '\n')
				/* NOTHING */;

	if (c == EOF) {
		fprintf(stderr, "EOF found in ppmfile!\n");
		exit(1);
	}

	if (c == '#') {
		while((c = fgetc(fp)) != '\n')
			/* NOTHING */;
	} else {
		ungetc(c, fp);
	}
}

int
readppmheader(fp, w, h, maxval)
	FILE	*fp;
	int	*w, *h, *maxval;
{
	int	type, c;

	skipblanks(fp);

	if ((c = fgetc(fp)) == 'P')
		c = fgetc(fp);
	else {
		fprintf(stderr, "Bad header in ppm file.\n");
		exit(1);
	}

	if (c == '3')
		type = ASCII;
	else if (c == '6')
		type = RAW;
	else {
		fprintf(stderr, "Unknown ppm type 'P%c'.\n", c);
		exit(1);
	}

	skipblanks(fp);

	if (fscanf(fp, "%d", w) != 1) {
		fprintf(stderr, "Error reading width.\n");
		exit(1);
	}

	skipblanks(fp);

	if (fscanf(fp, "%d", h) != 1) {
		fprintf(stderr, "Error reading height.\n");
		exit(1);
	}
	
	skipblanks(fp);

	if (fscanf(fp, "%d", maxval) != 1) {
		fprintf(stderr, "Error reading maxval.\n");
		exit(1);
	}

	if (*maxval > 255) {
		fprintf(stderr, "can't handle maxval > 255.\n");
		exit(1);
	}

	skipblanks(fp);

	return(type);
}

/*
 * Read the ppm data into r, g and b arrays for one line.
 */
readppmline(fp, type, w, r, g, b)
	FILE	*fp;
	int	type;
	int	w;
	unsigned char	*r, *g, *b;
{
	int	c, i, rdata, gdata, bdata;

	switch(type) {
	case ASCII:
		for (i = 0; i < w; i++) {
			if (fscanf(fp, "%d %d %d", &rdata, &gdata, &bdata) != 3) {
				fprintf(stderr, "Error reading ASCII integer data from ppm file.\n");
				exit(1);
			}

			r[i] = (unsigned char)rdata;
			g[i] = (unsigned char)gdata;
			b[i] = (unsigned char)bdata;
		}
		break;
	case RAW:
		for (i = 0; i < w; i++) {
			if ((c = fgetc(fp)) == EOF) {
				fprintf(stderr, "EOF read in raw ppm file.\n");
				exit(1);
			}
			r[i] = (unsigned char)c;

			if ((c = fgetc(fp)) == EOF) {
				fprintf(stderr, "EOF read in raw ppm file.\n");
				exit(1);
			}
			g[i] = (unsigned char)c;

			if ((c = fgetc(fp)) == EOF) {
				fprintf(stderr, "EOF read in raw ppm file.\n");
				exit(1);
			}
			b[i] = (unsigned char)c;
		}
	}
	}
