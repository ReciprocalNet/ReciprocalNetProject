#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "vort.h"
#include "status.h"

#define	MAX_CMAP_SIZE	256

#define	COLOR_DEPTH	8
#define	MAX_COLOR	256

char	*usage = "usage: impaste file1 x y file2 x y w h file3\n";

/*
 * join two image files together making a third image.
 */
main(ac, av)
	int ac;
	char **av;
{
	int		i;
	image		*im1, *im2, *im3;
	char		*infile, *outfile;
	unsigned int	w, h;
	int		num_colours;
	int		locx, locy, fromx, fromy, fromw, fromh;
	unsigned char	*red, *green, *blue, *alpha, **pix, *line;

	if (ac < 10) {
		fprintf(stderr, "%s", usage);
		exit(-1);
	}

	if ((im1 = openimage(av[1], "r")) == (image *)NULL) {
		fprintf(stderr, "impaste: can't open %s.\n", av[1]);
		exit(1);
	}

	locx = atoi(av[2]);
	locy = atoi(av[3]);

	if ((im2 = openimage(av[4], "r")) == (image *)NULL) {
		fprintf(stderr, "impaste: can't open %s.\n", av[4]);
		exit(1);
	}

	fromx = atoi(av[5]);
	fromy = atoi(av[6]);
	fromw = atoi(av[7]);
	fromh = atoi(av[8]);

	if ((im3 = openimage(av[9], "w")) == (image *)NULL) {
		fprintf(stderr, "impaste: can't open %s.\n", av[9]);
		exit(1);
	}

	copyheader(im3, im1);

	if (alphachannel(im1))
		imagetype(im3) = PIX_RLEA;
	else
		imagetype(im3) = PIX_RLE;

	writeheader(im3);

	pix = (unsigned char **)malloc(imageheight(im1) * sizeof(char *));
	for (i = 0; i != imageheight(im1); i++)
		pix[i] = (unsigned char *)malloc(imagewidth(im1) * 4);

	red = (unsigned char *)malloc(imagewidth(im1));
	green = (unsigned char *)malloc(imagewidth(im1));
	blue = (unsigned char *)malloc(imagewidth(im1));
	alpha = (unsigned char *)malloc(imagewidth(im1));

	h = 0;
	while (readrgbaline(im1, red, green, blue, alpha)) {
		for (i = 0; i < imagewidth(im1); i++) {
			pix[h][i * 4] = red[i];
			pix[h][i * 4 + 1] = green[i];
			pix[h][i * 4 + 2] = blue[i];
			pix[h][i * 4 + 3] = alpha[i];
		}
		h++;
	}

	red = (unsigned char *)malloc(imagewidth(im2));
	green = (unsigned char *)malloc(imagewidth(im2));
	blue = (unsigned char *)malloc(imagewidth(im2));
	alpha = (unsigned char *)malloc(imagewidth(im1));

	h = 0;

	if (fromy + fromh > imageheight(im1)) {
		fprintf(stderr, "impaste: segment to be copied too high.\n");
		exit(1);
	}

	if (fromx + fromw > imagewidth(im1)) {
		fprintf(stderr, "impaste: segment to be copied too wide.\n");
		exit(1);
	}

	while (readrgbaline(im2, red, green, blue, alpha)) {
		if (h >= fromy && h < (fromy + fromh)) {
			for (i = fromx; i < fromx + fromw; i++) {
				pix[locy + (h - fromy)][(locx + i - fromx) * 4] = red[i];
				pix[locy + (h - fromy)][(locx + i - fromx) * 4 + 1] = green[i];
				pix[locy + (h - fromy)][(locx + i - fromx) * 4 + 2] = blue[i];
				pix[locy + (h - fromy)][(locx + i - fromx) * 4 + 3] = alpha[i];
			}
		}
		h++;
	}

	red = (unsigned char *)malloc(imagewidth(im3));
	green = (unsigned char *)malloc(imagewidth(im3));
	blue = (unsigned char *)malloc(imagewidth(im3));
	alpha = (unsigned char *)malloc(imagewidth(im3));

	h = 0;
	for (h = 0; h != imageheight(im3); h++) {
		for (i = 0; i < imagewidth(im3); i++) {
			red[i] = pix[h][i * 4];
			green[i] = pix[h][i * 4 + 1];
			blue[i] = pix[h][i * 4 + 2];
			alpha[i] = pix[h][i * 4 + 3];
		}
		if (alphachannel(im3))
			writergbaline(im3, red, green, blue, alpha);
		else
			writergbline(im3, red, green, blue);
	}

	closeimage(im1);
	closeimage(im2);
	closeimage(im3);

	exit(ALLOK);
}
