/*
 * Convert TARGA format image to pixel format image.
 *
 * Usage:
 *		 targ2pix targafile rgbfile [options]
 *
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "vort.h"

#define	TRUE	1
#define	FALSE	!TRUE

typedef	struct {
	char		pad1[12];
	unsigned short	pw;
	unsigned short	ph;
	char		bits_per_pixel;
	char		pad2[1];
} TGA_HEADER;

#define TGA_HEADER_SIZE	(12 + 2 + 2 + 1 + 1)

int
main(argc, argv)
	int	argc;
	char	*argv[];
{
	FILE		*fin;
	image		*iout;
	TGA_HEADER	tga_header;
	unsigned char	*tga_line;
	char		*infn = NULL, *outfn = NULL;
	char		*r, *g, *b;
	unsigned	t1, t2, c;
	int		i, j,
			bppixel,
			nlines,
			chatty = FALSE,
			shift = TRUE,
			rle_rgb = FALSE,
			flip = FALSE;

	if (argc < 3)
		usage();

	for (i = 1; i < argc; i++) {
		if (argv[i][0] == '-' || argv[i][0] == '/') {
			if (strlen(argv[i]) != 2) usage();
			switch (argv[i][1]) {
			case 'c' :
				rle_rgb = TRUE;
				break;
			case 'f' :
				flip = TRUE;
				break;
			case 's' :
				shift = 0;
				break;
			case 'v' :
				chatty = TRUE;
				break;
			default :
				usage();
			}
		} else if (infn == NULL)
			infn = argv[i];
		else
			outfn = argv[i];
	}

	/* open TARGA file */
	if ((fin = fopen(infn, "r")) == NULL) {
		fprintf(stderr, "Can't read TARGA file: %s\n", infn);
		exit(1);
	}

	if (fread(tga_header.pad1, 12, 1, fin) != 1) {
		fprintf(stderr, "Can't read TARGA header: %s\n", infn);
		exit(1);
	}
	if (fread(&tga_header.pw, sizeof(unsigned short), 1, fin) != 1) {
		fprintf(stderr, "Can't read TARGA header: %s\n", infn);
		exit(1);
	}

	c = tga_header.pw & 0x00ff;
	tga_header.pw = tga_header.pw >> 8;
	tga_header.pw |= c << 8;

	if (fread(&tga_header.ph, sizeof(unsigned short), 1, fin) != 1) {
		fprintf(stderr, "Can't read TARGA header: %s\n", infn);
		exit(1);
	}

	c = tga_header.ph & 0x00ff;
	tga_header.ph = tga_header.ph >> 8;
	tga_header.ph |= c << 8;

	if (fread(&tga_header.bits_per_pixel, 1, 1, fin) != 1) {
		fprintf(stderr, "Can't read TARGA header: %s\n", infn);
		exit(1);
	}
	if (fread(tga_header.pad2, 1, 1, fin) != 1) {
		fprintf(stderr, "Can't read TARGA header: %s\n", infn);
		exit(1);
	}

	bppixel = tga_header.bits_per_pixel;
	if (bppixel != 16 && bppixel != 32) {
		fprintf(stderr, "TGA header error; invalid bits per pixel: %d\n", bppixel);
		exit(1);
	}
	/* convert bits per pixel to bytes per pixel */
	bppixel /= 8;

	if ((iout = openimage(outfn, "w")) == NULL) {
		fprintf(stderr, "Can't create RGB file: %s\n", outfn);
		exit(1);
	}

	imagetype(iout) = (rle_rgb ? PIX_RLE : PIX_RGB);

	iout->pixw = tga_header.pw;
	iout->pixh = nlines = tga_header.ph;
	iout->tlength = 0;
	iout->date = 0;
	writeheader(iout);

	if ((tga_line = (unsigned char *)malloc(bppixel * tga_header.pw * sizeof(unsigned short))) == NULL) {
		fprintf(stderr, "Not enough space for TARGA scanline: %d\n", tga_header.pw * 2);
		exit(1);
	}
	if ((r = (char *)malloc(tga_header.pw)) == NULL) {
		fprintf(stderr, "Not enough space for r scanline: %d\n", tga_header.pw);
		exit(1);
	}
	if ((g = (char *)malloc(tga_header.pw)) == NULL) {
		fprintf(stderr, "Not enough space for g scanline: %d\n", tga_header.pw);
		exit(1);
	}
	if ((b = (char *)malloc(tga_header.pw)) == NULL) {
		fprintf(stderr, "Not enough space for b scanline: %d\n", tga_header.pw);
		exit(1);
	}

	while ((!flip && !feof(fin) && nlines) || (flip && nlines)) {
		if (flip) {
			/* we are reading image backwards - seek to start of line to read */
			fseek(fin, (long)(TGA_HEADER_SIZE + tga_header.pw * (long)(nlines-1) * sizeof(unsigned short)), 0);
			}
		if (fread(tga_line, 1, bppixel * tga_header.pw, fin) != bppixel * tga_header.pw) {
			fprintf(stderr, "targ2pix: Can't read file: %s, too short.\n", infn);
			exit(1);
		}

		for (i = j = 0; i < tga_header.pw; i++, j += bppixel) {
			switch (bppixel) {

			case 2:
				t1 = tga_line[j];	/* gggbbbbb */
				t2 = tga_line[j+1];	/* ?rrrrrgg */
				if (shift) {
					r[i] = (char) (t2 & 0x7c) << 1;
					g[i] = (char) ((t2 & 0x03) << 6) | ((t1 & 0xe0) >> 2);
					b[i] = (char) (t1 & 0x1f) << 3;
				} else {
					r[i] = (char) (t2 & 0x7c) >> 2;
					g[i] = (char) ((t2 & 0x03) << 3) | ((t1 & 0xe0) >> 5);
					b[i] = (char) (t1 & 0x1f);
				}
				break;

			case 4:
				r[i] = (char) tga_line[j];
				g[i] = (char) tga_line[j+1];
				b[i] = (char) tga_line[j+2];
				/* fourth byte is ignored */
				break;

			default :
				break;
			}
		}

		writergbline(iout, r, g, b);
		nlines--;
		if (chatty)
			fprintf(stderr, "%5.5d <-- %5.5d\r", tga_header.ph, nlines);
	}
	if (chatty)
		fprintf(stderr, "\n");
	if (nlines > 0) {
		fprintf(stderr, "File %s too short by %d scanlines", infn, nlines);
		exit(1);
	}
	fclose(fin);

	closeimage(iout);
}


int	
usage()
{
	fprintf(stderr, "convert TARGA format image to RGB format\n\n");
	fprintf(stderr, "usage:  targ2pix targafile rgbfile [-v]\n\n");
	fprintf(stderr, "            -c       RGB file is run-length encoded\n");
	fprintf(stderr, "            -f       flip image (read from bottom to top)\n");
	fprintf(stderr, "            -s       turn off shifting (for 16 bit TARGA files)\n");
	fprintf(stderr, "            -v       verbose\n");
	exit(1);
}
