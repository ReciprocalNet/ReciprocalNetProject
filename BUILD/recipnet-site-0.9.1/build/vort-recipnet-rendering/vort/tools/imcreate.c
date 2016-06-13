#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "vort.h"
#include "status.h"

#define	MAX_CMAP_SIZE	256

#define	COLOR_DEPTH	8
#define	MAX_COLOR	256

char	*usage = "usage: imcreate file w h\n";

/*
 * create a blank image file of width w and height h.
 */
main(ac, av)
	int ac;
	char **av;
{
	int		i;
	image		*im;
	char		*infile, *outfile;
	unsigned short  w, h;
	int		num_colours;
	int		locx, locy, fromx, fromy, fromw, fromh;
	unsigned char	*red, *green, *blue, **pic;

	if (ac < 4) {
		fprintf(stderr, "%s", usage);
		exit(-1);
	}

	w = atoi(av[2]);
	h = atoi(av[3]);

	if ((im = openimage(av[1], "w")) == (image *)NULL) {
		fprintf(stderr, "imcreate: unable to open %s for writing\n", av[1]);
		exit(1);
	}

	imagetype(im) = PIX_RLE;

	imagewidth(im) = w;
	imageheight(im) = h;

	imagedate(im) = time(0);

	titlelength(im) = 0;

	writeheader(im);

	red = (unsigned char *)malloc(w);
	green = (unsigned char *)malloc(w);
	blue = (unsigned char *)malloc(w);

	for (i = 0; i < imagewidth(im); i++) {
		red[i] = 0;
		green[i] = 0;
		blue[i] = 0;
	}

	for (h = 0; h != imageheight(im); h++)
		writergbline(im, red, green, blue);

	closeimage(im);

	exit(ALLOK);
}
