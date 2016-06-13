#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "vort.h"

/*
 * vortinfo
 *
 *	provides some details as to what a pix file contains
 */
main(ac, av)
	int	ac;
	char	**av;
{
	image		*im;
	int		w, h;

	if ((im = openimage(av[1], "r")) == (image *)NULL) {
		fprintf(stderr, "vortinfo: can't open image file.\n");
		exit(1);
	}

	w = imagewidth(im);
	h = imageheight(im);

	if (imagefragment(im))
		printf("image fragment - ");
	else
		printf("image - ");
	
	switch (imagetype(im)) {
	case PIX_RGB:
		printf("rgb %d by %d.\n", w, h);
		break;
	case PIX_RLE:
		printf("runlength encoded rgb %d by %d.\n", w, h);
		break;
	case PIX_RGBA:
		printf("rgb-alpha %d by %d.\n", w, h);
		break;
	case PIX_RLEA:
		printf("rgb-alpha runlength encoded %d by %d.\n", w, h);
		break;
	case PIX_CMAP:
		printf("rgb colour-mapped %d by %d.\n", w, h);
		break;
	case PIX_RLECMAP:
		printf("runlength encoded rgb colour-mapped %d by %d.\n", w, h);
		break;
	case PIX_ACMAP:
		printf("rgb colour-mapped %d by %d with alpha.\n", w, h);
		break;
	case PIX_RLEACMAP:
		printf("runlength encoded rgb colour-mapped %d by %d with alpha.\n", w, h);
		break;
	default:
		fprintf(stderr, "vortinfo: not a vort image.\n");
		exit(1);
	}

	if (imagefragment(im)) {
		printf("x address %d.\n", imagexaddr(im));
		printf("y address %d.\n", imageyaddr(im));
		printf("width of original image %d.\n", imageorigwidth(im));
		printf("height of original image %d.\n", imageorigheight(im));
	}

	printf("creation date: %s", ctime(&imagedate(im)));

	if (imagetype(im) == PIX_CMAP)
		printf("colourmap size %d.\n", cmapsize(im));
	else if (imagetype(im) == PIX_RLECMAP) 
		printf("colourmap size %d.\n", cmapsize(im));

	if (*imagetitle(im) != 0)
		printf("imagetitle: %s\n", imagetitle(im));

	exit(0);
}
