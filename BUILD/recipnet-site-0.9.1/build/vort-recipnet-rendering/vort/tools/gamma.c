#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "vort.h"


/*
 * a program to do simple gamma correction on images
 *
 *	usage: gamma [-gn] file1 file2
 *
 *		where n is some floating point number (default 2.0)
 */

#define	DEFGAMMA	2.0

#define	RANGE		256

char	*usage = "gamma: Usage gamma [-gn] file1 file2\n";

/*
 * gammacorrect
 *
 *	adjust the pixel value
 *
 */
gammacorrect(table, w, red, green, blue)
	unsigned char	*table;
	unsigned short	w;
	unsigned char	*red, *green, *blue;
{
	int		i;

	for (i = 0; i < w; i++) {
		red[i] = table[red[i]];
		green[i] = table[green[i]];
		blue[i] = table[blue[i]];
	}
}

/*
 * main driver
 */
main(ac, av)
	int	ac;
	char	**av;
{
	char		*infile,
			*outfile;
	register int    i;
	unsigned short  h, w;
	int		gotinfile;
	unsigned char	*red, *green, *blue, *alpha, table[RANGE];
	double		gamma;
	image		*in, *out;

	gamma = DEFGAMMA;
	gotinfile = 0;

	while (ac > 1) 
		if (*(*++av) == '-')
			switch(*(*av + 1)) {
			case 'g':
				gamma = atof((*av + 2));
				ac--;
				break;
			case 0:			/* single - read stdin */
				if (gotinfile)
					outfile = *av;
				else {
					infile = *av;
					gotinfile = 1;
				}
				ac--;
				break;
			default:
				fprintf(stderr, "%s", usage);
				exit(1);
			}
		else {  
			if (gotinfile)
				outfile = *av;
			else {
				infile = *av;
				gotinfile = 1;
			}
			ac--;
		}

	if ((in = openimage(infile, "r")) == (image *)NULL) {
		fprintf(stderr, "gamma: can't open input file.\n");
		exit(1);
	}

	if ((out = openimage(outfile, "w")) == (image *)NULL) {
		fprintf(stderr, "gamma: can't open output file.\n");
		exit(1);
	}

	copyheader(out, in);

	w = imagewidth(in);
	h = imageheight(in);

	table[0] = 0;
					/* create correction table */
	for (i = 1; i != RANGE; i++)
		table[i] = exp(log((double)i / 255.0) / gamma) * 255 + 0.5;

	writeheader(out);

	if (!colormapped(in)) {
		red = (unsigned char *)malloc(w);
		green = (unsigned char *)malloc(w);
		blue = (unsigned char *)malloc(w);

		if (alphachannel(in)) {
			alpha = (unsigned char *)malloc(w);
			while (readrgbaline(in, red, green, blue, alpha)) {
				gammacorrect(table, w, red, green, blue);
				writergbline(out, red, green, blue);
			}
		} else
			while (readrgbline(in, red, green, blue)) {
				gammacorrect(table, w, red, green, blue);
				writergbline(out, red, green, blue);
			}
	} else {
		gammacorrect(table, cmapsize(out), redmap(out), greenmap(out), bluemap(out));
		if (alphachannel(in))
			for (i = 0; i != w * h * 2; i++)
				writebyte(out, readbyte(in));
		else
			for (i = 0; i != w * h; i++)
				writebyte(out, readbyte(in));
	}

	closeimage(in);
	closeimage(out);

	exit(0);
}
