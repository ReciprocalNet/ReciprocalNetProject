/*
 * Translate a VORT image (colour mapped thank you! - use median/mulmcut) to
 * Microsoft/ZSoft PCX format.
 *
 * PCX format: 8 bits per pixel, 1 image plane, colour mapped, RLE.
 * 
 * Should be extended to handle less than 256 entry colour maps.
 */

#include <stdio.h>
#include <stdlib.h>
#include "vort.h"

#define WRITE_BINARY_MODE	"w"

#define USAGE	"vort2pcx: usage: vort2pcx vortfile pcxfile\n"

typedef unsigned int	UINT;
typedef unsigned char	UCHAR;

/* this structure needs to be byte aligned! */
typedef struct {
	UCHAR	r;
	UCHAR	g;
	UCHAR	b;
	} CMAP;

/* this structure needs to be byte aligned! */
typedef	struct {
	UCHAR	manuf;		/* don't ask */
	UCHAR	ver;		/* PCX format version */
	UCHAR	rle_flag;	/* 1 = run-length encoded */
	UCHAR	bits_per_pixel;	/* number of bits per pixel */
	UINT	X1;		/* upper LH pixel co-ord */
	UINT	Y1;		/* upper LH pixel co-ord */
	UINT	X2;		/* lower RH pixel co-ord */
	UINT	Y2;		/* lower RH pixel co-ord */
	UINT	Hres;		/* horizontal resolution of device */
	UINT	Vres;		/* vertical resolution of device */
	CMAP	cmap_16[16];	/* first 16 entries of colour map - MUST be 48 bytes */
	UCHAR	Vmode;		/*???*/
	UCHAR	nplanes;	/* number of image planes */
	UINT	bytes_per_line;	/* number of bytes per scan line */
	UCHAR	filler[60];	/* pad out to 128 bytes */
	} PCX_HDR;


main(ac, av)
	int	ac;
	char	**av;
{
	PCX_HDR		pcx_hdr;
	FILE		*out;
	int		outfound,
			chatty = 0;
	register int	i, x, n, run;
	unsigned short	h, w;
	unsigned char	*s;
	char		*outfile, *infile;
	image		*im;

	if (ac < 3) {
		fprintf(stderr, USAGE);
		exit(1);
	}

	outfound = 0;

	outfile = infile = (char *)NULL;

	while (--ac >= 1) {
		switch (av[ac][0]) {
		case '-':
			if (tolower(av[ac][1]) == 'v') chatty = 1;
			else 
				if (outfound)
					infile = "-";
				else {
					outfile = "-";
					outfound = 1;
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
		fprintf(stderr, USAGE);
		exit(1);
	}

	if ((im = openimage(infile, "r")) == (image *)NULL) {
		fprintf(stderr, "vort2pcx: can't open file %s\n", infile);
		exit(1);
	}

	if (!colormapped(im)) {
		fprintf(stderr, "vort2pcx: %s must be a colour mapped image!\n", infile);
		exit(1);
	}

	if (strcmp(outfile, "-") != 0) {
		if ((out = fopen(outfile, WRITE_BINARY_MODE)) == (FILE *)NULL) {
			fprintf(stderr, "vort2pcx: can't open file %s\n", outfile);
			exit(1);
		}
	} else 
		out = stdout;

	w = imagewidth(im);
	h = imageheight(im);

	if (!(s = (unsigned char *)malloc(w))) {
		fprintf(stderr, "vort2pcx: not enough memory for scanline!\n");
		closeimage(im);
		fclose(out);
		exit(1);
		}

	/* write PCX header */
	pcx_hdr.manuf = 10;
	pcx_hdr.ver = 5;
	pcx_hdr.rle_flag = 1;
	pcx_hdr.bits_per_pixel = 8;
	pcx_hdr.X1 = 0;
	pcx_hdr.Y1 = 0;
	pcx_hdr.X2 = w - 1;
	pcx_hdr.Y2 = h - 1;
	pcx_hdr.Hres = w;
	pcx_hdr.Vres = h;
	for (i = 0; i < 16; i++) {
		pcx_hdr.cmap_16[i].r = redmap(im)[i];
		pcx_hdr.cmap_16[i].g = greenmap(im)[i];
		pcx_hdr.cmap_16[i].b = bluemap(im)[i];
		}
	pcx_hdr.Vmode = 0;
	pcx_hdr.nplanes = 1;
	pcx_hdr.bytes_per_line = w;
	for (i = 0; i < sizeof(pcx_hdr.filler); i++) pcx_hdr.filler[i] = 0;
	if (fwrite(&pcx_hdr, sizeof(pcx_hdr), 1, out) != 1) {
		fprintf(stderr, "vort2pcx: can't write PCX header!\n");
		fclose(out);
		exit(1);
		}

	n = 0;

	while (readmappedline(im, s)) {
		/* write a line (run-length encoded) to output */
		if (chatty) fprintf(stderr, "%4.4d ---> %4.4d\r", n, h);
		x = 0;
		while (x < w) {
			run = 1;
			while ((x+run < w) && s[x] == s[x+run] && run < 0x3F) run++;
			if (run > 1 || s[x] > 0xBF) fprintf(out, "%c%c", 0xC0 + run, s[x]);
			else fprintf(out, "%c%c", 0xC1, s[x]);
			x += run;
			}
		n++;
		}

	if (chatty) fprintf(stderr, "\n");

	/* output some value for some reason - Why? - Because it's there! */
	fprintf(out, "%c", 0x0c);

	/* output the colour map */
	for (i = 0; i < 256; i++) {
		fprintf(out, "%c%c%c", redmap(im)[i], greenmap(im)[i], bluemap(im)[i]);
		}

	fclose(out);
	closeimage(im);
	exit(0);
}

