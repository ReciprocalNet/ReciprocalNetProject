#include <stdio.h>
#include <stdlib.h>
#include "vort.h"

#define	STRSIZE		40		/* length of postscript string - must be even */

/*
 * program to read a pix image and convert it to greyscale postscript
 */
main(ac, av)
	int	ac;
	char	**av;
{
	FILE		*out;
	register int	i, x;
	unsigned short	h, w;
	float		scale, xoff, yoff;
	int		val, count, outfound, landscape;
	unsigned char	*red, *green, *blue;
	char		*outfile, *infile;
	image		*im;

	if (ac < 3) {
		fprintf(stderr, "vort2ps: usage vort2ps [-sn -l -a3] file1 file2.\n");
		exit(1);
	}

	landscape = outfound = 0;
	scale = 1.0;
	xoff = 300;
	yoff = 400;

	outfile = infile = (char *)NULL;

	while (--ac >= 1) {
		switch (av[ac][0]) {
		case '-':
			if (av[ac][1] == 's')
				scale = atof(&av[ac][2]);
			else if (av[ac][1] == 'l')
				landscape = 1;
			else if (av[ac][1] == 'a') {
				if (av[ac][2] == '3') {
					xoff *= 1.414;
					yoff *= 1.414;
				}
			} else {
				if (outfound)
					infile = "-";
				else {
					outfile = "-";
					outfound = 1;
				}
			}
			break;
		default:
			if (outfound)
				infile = av[ac];
			else {
				outfile = av[ac];
				outfound = 1;
			}
		}
	}

	if (infile == (char *)NULL || outfile == (char *)NULL) {
		fprintf(stderr, "vort2ps: usage vort2ps [-sn] file1 file2.\n");
		exit(1);
	}

	if ((im = openimage(infile, "r")) == (image *)NULL) {
		fprintf(stderr, "vort2ps: can't open file %s.\n", infile);
		exit(1);
	}

	if (strcmp(outfile, "-") != 0) {
		if ((out = fopen(outfile, "w")) == (FILE *)NULL) {
			fprintf(stderr, "vort2ps: can't open file %s.\n", outfile);
			exit(1);
		}
	} else 
		out = stdout;

	w = imagewidth(im);
	h = imageheight(im);

	x = 0;
	val = -1;

	count = 1;

	red = (unsigned char *)malloc(w);
	green = (unsigned char *)malloc(w);
	blue = (unsigned char *)malloc(w);

	fprintf(out, "%%! pixel file image: %s\n", imagetitle(im));
	if (landscape) {
		fprintf(out, "%%%%BoundingBox: %d %d %d %d\n",
			(int)(yoff - scale * w / 300.0 * 72.0),
			(int)(-xoff - scale * h / 300.0 * 72),
			(int)(yoff - scale * w / 300.0 * 72.0 + scale * 2 * w / 300.0 * 72.0),
			(int)(-xoff - scale * h / 300.0 * 72.0 + scale * 2 * h / 300.0 * 72.0));
	} else {
		fprintf(out, "%%%%BoundingBox: %d %d %d %d\n",
			(int)(xoff - scale * w / 300.0 * 72.0),
			(int)(yoff - scale * h / 300.0 * 72),
			(int)(xoff - scale * w / 300.0 * 72.0 + scale * 2 * w / 300.0 * 72.0),
			(int)(yoff - scale * h / 300.0 * 72.0 + scale * 2 * h / 300.0 * 72.0));
	}

	fprintf(out, "\ngsave\n\n");
	fprintf(out, "/picstr %d string def\n", STRSIZE);

	if (landscape) {
		fprintf(out, "90 rotate\n");
		fprintf(out, "%f %f translate\n", yoff - scale * w / 300.0 * 72.0, -xoff - scale * h / 300.0 * 72);
		fprintf(out, "%f %f scale\n", scale * 2 * w / 300.0 * 72, scale * 2 * h / 300.0 * 72);
		fprintf(out, "%d %d 8 [ %d 0 0 -%d 0 %d ]\n", w, h, w, h, h);
	} else {
		fprintf(out, "%f %f translate\n", xoff - scale * w / 300.0 * 72, yoff - scale * h / 300.0 * 72.0);
		fprintf(out, "%f %f scale\n", scale * 2 * w / 300.0 * 72, scale * 2 * h / 300.0 * 72);
		fprintf(out, "%d %d 8 [ %d 0 0 -%d 0 %d ]\n", w, h, w, h, h);
	}

	fprintf(out, "{ currentfile picstr readhexstring pop }\n");
	fprintf(out, "image\n");

	while (readrgbline(im, red, green, blue)) {

		for (x = 0; x < w; x++) {
			val = red[x] * 0.3 + green[x] * 0.59 + blue[x] * 0.11 + 0.5;
			fprintf(out, "%02x", val);
			if ((count++) % (STRSIZE / 2) == 0)
				fputc('\n', out);
		}
	}

				/* pad out last line for readhexstring */
	if ((count - 1) % STRSIZE != 0)
		for (i = (count - 1) % STRSIZE; i != STRSIZE; i++)
			fprintf(out, "00");

	fprintf(out, "\nshowpage\n");
	fprintf(out, "grestore\n");

	exit(0);
}
