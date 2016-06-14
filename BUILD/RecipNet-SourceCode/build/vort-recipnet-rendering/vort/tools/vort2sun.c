#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <pixrect/pixrect_hs.h>
#include "vort.h"

#define	CMAPSIZE	256
#define DEPTH		8
#define TRUE		1
#define FALSE		0

extern	char	*rindex();


/*
 * Program to read in a color-mapped image and output a Sun rasterfile on stdout
 * Adapted by Ole Holm Nielsen (ohnielse@ltf.dth.dk) 31-May-1990.
 * Further modifications by Ole Holm Nielsen 10-May-1991.
 */
main(ac, av)
	int	ac;
	char	**av;
{
	char			*myname;
	register int		i, j, x, y;
	unsigned int		h, w, wreal, w2;
	int			line_bytes, mag = 1;
	u_char			*line, *red, *green, *blue;
	u_char			*l, *p, *pp;
	register Pixrect	*pr;
	struct mpr_data		*data;	/* Pixrect data */
	image			*im;
	int			upsidedown = 0;
	colormap_t		cm;	/* temp colormap info */

	if ((myname = rindex(av[0],'/')) == NULL)
		myname = av[0];
	else
		*myname++;

	if (ac < 2 || ac > 4) {
		fprintf(stderr,
			"%s: usage %s [-m<n>] [-u] file.\n", myname, myname);
		exit(1);
	}

	for (i = 1; i < ac && *av[i] == '-'; i++) {
		if (strncmp(av[i], "-m", 2) == 0) {		/* magnify */
			if (sscanf(av[i] , "-m%d", &mag) != 1) {
				fprintf(stderr, "%s: bad mag factor\n", myname);
				exit(1);
			}

			if (mag < 1 || mag > 5) {
				fprintf(stderr, "%s: bad mag factor\n", myname);
				exit(1);
			}
		} else if (strncmp(av[i], "-u", 2) == 0) {
			/* show image upside down */
			upsidedown = 1;
		}
	} 

	if ((im = openimage(av[i], "r")) == (image *)NULL) {
		fprintf(stderr, "%s: can't open file %s.\n", myname, av[i]);
		exit(1);
	}

	wreal = imagewidth(im);
	w = wreal * mag;
	h = mag * imageheight(im);

	if ((pr = mem_create(w, h, DEPTH)) == (Pixrect *)NULL) {
		perror ("Couldn't allocate Pixrect");
		exit(1);
	}

	data = mpr_d(pr);			/* Nice for debugging */
	p = (unsigned char *)data->md_image;	/* Start of Pixrect */
	line_bytes = data->md_linebytes;	/* MUST use this value */

	if (upsidedown) {
		p += line_bytes * (h - 1);	/* Last line in Pixrect */
		line_bytes = - line_bytes;	/* Invert */
	}

	y = h - 1;

	w2 = w + (w % 4);
	line = (u_char *)malloc(w2);
	l = line;

	if (! colormapped(im)) {
		x = w2 < CMAPSIZE ? CMAPSIZE : w2;
		red = (u_char *)malloc(x);
		green = (u_char *)malloc(x);
		blue = (u_char *)malloc(x);
	}

	while (y >= 0) {
		if (colormapped(im))
			readmappedline(im, line);
		else {
			readrgbline(im, red, green, blue);
			for (x = 0; x < w; x++) 
				line[x] = red[x] * 0.3 + green[x] * 0.59
					+ blue[x] * 0.11;
		}
		if (mag == 1) {
			bcopy(l, p, w);
			p += line_bytes;
			y--;
		} else {
			pp = p;
			for (x = 0; x < wreal; x++) 	/* Magnify */
				for (i = 0; i < mag; i++)
					*pp++ = l[x];
			pp = p;
			p += line_bytes;
			for (j = 1; j < mag; j++) {	/* Copy mag-1 times */
				bcopy(pp, p, w);
				p += line_bytes;
			}
			y -= mag;
		}
	}

	/* Define colormap */

	if (colormapped(im)) {
		cm.type = RMT_EQUAL_RGB;
		cm.length = cmapsize(im);
		cm.map [0] = redmap(im);
		cm.map [1] = greenmap(im);
		cm.map [2] = bluemap(im);
	} else {
		cm.type = RMT_EQUAL_RGB;
		cm.length = CMAPSIZE;
		cm.map [0] = red;
		cm.map [1] = green;
		cm.map [2] = blue;
		/* Define gray-scale colormap */
		for (i = 0; i < CMAPSIZE; i++)
			red[i] = green[i] = blue[i] = i;
	}

	/* Dump the rasterfile */

	pr_dump (pr, stdout, &cm, RT_BYTE_ENCODED, FALSE);

	return(0);
}

