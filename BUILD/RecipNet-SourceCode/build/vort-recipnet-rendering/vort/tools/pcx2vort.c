/*
 * Translate a PCX format image to VORT colour mapped RLE image.
 *
 * PCX format: 8 bits per pixel, 1 image plane, colour mapped, RLE.
 * 
 */

#include <stdio.h>
#include <time.h>

#include <stdlib.h>
#include "vort.h"

#define READ_BINARY_MODE	"r"

#define USAGE	"pcx2vort: usage: pcx2vort pcxfile vortfile\n"

typedef unsigned short	UINT;
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
	UCHAR	Vmode;		/* ???*/
	UCHAR	nplanes;	/* number of image planes */
	UINT	bytes_per_line;	/* number of bytes per scan line */
	UCHAR	filler[60];	/* pad out to 128 bytes */
	} PCX_HDR;


main(ac, av)
	int	ac;
	char	**av;
{
	PCX_HDR		pcx_hdr;
	FILE		*in;
	int		outfound,
			chatty = 0;
	register int	i, x, n, run;
	unsigned short	h, w;
	unsigned char	*s;
	char		*outfile, *infile;
	int		palette_size = 256;
	image		*out;

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

	if (strcmp(infile, "-") != 0) {
		if ((in = fopen(infile, READ_BINARY_MODE)) == (FILE *)NULL) {
			fprintf(stderr, "pcx2vort: can't open file %s\n", infile);
			exit(1);
		}
	} else 
		in = stdin;

	if (fread(&pcx_hdr, sizeof(pcx_hdr), 1, in) != 1) {
		fprintf(stderr, "pcx2vort: can't read PCX header\n");
		exit(1);
	}
	/* should really validate header here to make sure it is 256 RLE colour mapped ! */
	w = pcx_hdr.X2 - pcx_hdr.X1 + 1;
	h = pcx_hdr.Y2 - pcx_hdr.Y1 + 1;

	if (chatty)
		printf("%dx%d\n", w, h);

	if (!(s = (unsigned char *)malloc(w))) {
		fprintf(stderr, "pcx2vort: not enough memory for scanline!\n");
		fclose(in);
		exit(1);
		}

	if ((out = openimage(outfile, "w")) == (image *)NULL) {
		fprintf(stderr, "pcx2vort: can't open image output file %s\n", outfile);
		exit(1);
	}

	imagewidth(out) = w;
	imageheight(out) = h;
	imagetype(out) = PIX_RLECMAP;
	imagedepth(out) = 8;
	cmapsize(out) = palette_size;
	imagedate(out) = time((time_t *)NULL);
	imagetitle(out) = NULL;
	titlelength(out) = 0;

	/* load colour map info */
	redmap(out) = (unsigned char *)malloc(palette_size);
	greenmap(out) = (unsigned char *)malloc(palette_size);
	bluemap(out) = (unsigned char *)malloc(palette_size);
	if (redmap(out) == NULL || greenmap(out) == NULL || bluemap(out) == NULL) {
		fail("pcx2vort: can't allocate colour map memory\n");
		}
	extract_pcx_colour_map(in, palette_size, redmap(out), greenmap(out), bluemap(out));

	writeheader(out);

	/* convert each scan line */
	for (n = 0; n < h; n++) {
		read_pcx_rle_line(in, s, w);
		writemappedline(out, s);
		if (chatty) fprintf(stderr, "\r%d ---> %d", n+1, h);
		}
	if (chatty) fprintf(stderr, "\n");

	fclose(in);
	closeimage(out);
	exit(0);
}



int	extract_pcx_colour_map(f, size, r, g, b)
FILE	*f;
int	size;
char	*r, *g, *b;
{
	long	posn;
	int	i;
	CMAP	rgb;

	/* save current position in file */
	posn = ftell(f);

	/* position to presumed start of PCX colour map */
	fseek(f, -3L*size, 2);

	/* read colour map */
	for (i = 0; i < size; i++) {
		fread(&rgb, sizeof(rgb), 1, f);
		r[i] = rgb.r;
		g[i] = rgb.g;
		b[i] = rgb.b;
		}

	/* reposition to where we started */
	fseek(f, posn, 0);
}



int	read_pcx_rle_line(f, s, n)
FILE	*f;
char	*s;
int	n;
{
	int	i, j, cnt;
	int	c;

	for (i = 0; i < n; ) {
		if ((c = getc(f)) == EOF) {
			if (feof(f)) return;
			}
		if ((c & 0xc0) == 0xc0) {
			cnt = c & 0x3f;
			if ((c = getc(f)) == EOF) {
				if (feof(f)) return;
				}
			if (i + cnt > n) cnt = n - i;
			for (j = 0; j < cnt; j++) s[i+j] = c;
			i += cnt;
			}
		else {
			s[i++] = c;
			}
		}
}


int		fail(msg)
const char *msg;
{
	fprintf(stderr, "%s", msg);
	exit(1);
}

