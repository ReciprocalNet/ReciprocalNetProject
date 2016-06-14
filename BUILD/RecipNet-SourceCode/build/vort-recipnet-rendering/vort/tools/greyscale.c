#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "vort.h"
#include "status.h"

#define	MAX_CMAP_SIZE	256

#define	COLOR_DEPTH	8
#define	MAX_COLOR	256

char	*usage = "usage: greyscale [-c n] input output\n";

/*
 * produce a colours mapped grey scale image of the input file
 */
main(argc, argv)
	int argc;
	char **argv;
{
	int		i;
	image		*in, *out;
	char		*infile, *outfile;
	unsigned short  w;
	int		num_colours;
	int		gotinfile;
	unsigned char	*red, *green, *blue, *outline;
	unsigned char	r[MAX_CMAP_SIZE], g[MAX_CMAP_SIZE], b[MAX_CMAP_SIZE];

	num_colours = MAX_CMAP_SIZE;
	gotinfile = 0;

	if (argc < 3) {
		fprintf(stderr, "%s", usage);
		exit(-1);
	}

	while (argc > 1) 
	    if (*(*++argv) == '-')
		switch(*(*argv + 1)) {
		   case 'c':
			if (*(*argv + 1) != 0)
				num_colours = atoi(*argv + 1);
			else {
				if (argv[1])
					num_colours = atoi(*++argv);
				else {
					fprintf(stderr, "%s", usage);
					exit(-1);
				}
				argc--;
			}
			argc--;
			break;
		   case 0:			/* single - (stdin or stdout) */
			if (gotinfile)
				outfile = *argv;
			else {
				infile = *argv;
				gotinfile = 1;
			}
			argc--;
			break;
		   default:
			fprintf(stderr, "%s", usage);
			exit(-1);
		 }
	else {  
		if (gotinfile)
			outfile = *argv;
		else {
			infile = *argv;
			gotinfile = 1;
		}
		argc--;
	}

	if ((in = openimage(infile, "r")) == (image *)NULL) {
		fprintf(stderr, "greyscale: can't open file %s.\n", infile);
		exit(1);
	}

	if ((out = openimage(outfile, "w")) == (image *)NULL) {
		fprintf(stderr, "greyscale: can't open file %s.\n", outfile);
		exit(1);
	}

	copyheader(out, in);

	imagedepth(out) = 8;

	if (rlecoded(in))
		imagetype(out) = PIX_RLECMAP;
	else
		imagetype(out) = PIX_CMAP;

	for (i = 0; i < num_colours; i++)
		r[i] = g[i] = b[i] = i * (num_colours - 1) / 255.0;

	setcmap(out, num_colours, r, g, b);

	writeheader(out);

	w = imagewidth(in);

	red = (unsigned char *)malloc(w);
	green = (unsigned char *)malloc(w);
	blue = (unsigned char *)malloc(w);

	outline = (unsigned char *)malloc(w);

	while (readrgbline(in, red, green, blue)) {
		for (i = 0; i < w; i++)
			outline[i] = red[i] * 0.3 + green[i] * 0.59 + blue[i] * 0.11;
		writemappedline(out, outline);
	}

	exit(ALLOK);
}
