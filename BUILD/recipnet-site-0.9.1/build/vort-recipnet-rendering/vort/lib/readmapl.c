#include <stdio.h>
#include "vort.h"

/*
 * readmappeda
 *
 *	reads in a scanline for a colour mapped image setting the alpha
 * channel as it goes (if required).
 */
static int readmappeda(in, line, alpha)
	image		*in;
	unsigned char	*line;
	unsigned char	*alpha;
{
	register int	x, i, j;
	unsigned char	run;

	switch (imagetype(in)) {
	case PIX_CMAP:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			line[x] = i;
		}
		if (alpha != (unsigned char *)NULL)
			for (x = 0; x != (int)imagewidth(in); x++)
				alpha[x] = 0xff;
		break;
	case PIX_RLECMAP:
		x = 0;
		while (x < (int)imagewidth(in)) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			run = i;
			if (run & 0x80) {
				while ((run & 0x7f) && x < (int)imagewidth(in)) {
					i = readbyte(in);
					line[x] = i;
					run--;
					x++;
				}
			} else {
				i = readbyte(in);
				line[x] = i;
				x++;
				while (run && x < (int)imagewidth(in)) {
					line[x] = i;
					run--;
					x++;
				}
			}
		}
		if (alpha != (unsigned char *)NULL)
			for (x = 0; x != (int)imagewidth(in); x++)
				alpha[x] = 0xff;
		break;
	case PIX_ACMAP:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			line[x] = i;
			if ((i = readbyte(in)) == EOF)
				return(0);
			if (alpha != (unsigned char *)NULL)
				alpha[x] = i;
		}
		break;
	case PIX_RLEACMAP:
		x = 0;
		while (x < (int)imagewidth(in)) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			run = i;
			if (run & 0x80) {
				while ((run & 0x7f) && x < (int)imagewidth(in)) {
					i = readbyte(in);
					line[x] = i;
					i = readbyte(in);
					if (alpha != (unsigned char *)NULL)
						alpha[x] = i;
					run--;
					x++;
				}
			} else {
				i = readbyte(in);
				j = readbyte(in);
				line[x] = i;
				if (alpha != (unsigned char *)NULL)
					alpha[x] = j;
				x++;
				while (run && x < (int)imagewidth(in)) {
					line[x] = i;
					run--;
					if (alpha != (unsigned char *)NULL)
						alpha[x] = j;
					x++;
				}
			}
		}
		break;
	default:
		vortwarning("readmappeda: bad file format.\n");
		return(0);
	}

	return(1);
}

/*
 * readmappedline
 *
 *	reads in a scanline for a colour mapped image.
 */
int readmappedline(in, line)
	image		*in;
	unsigned char	*line;
{
	return(readmappeda(in, line, (unsigned char *)NULL));
}

/*
 * readmappedaline
 *
 *	reads in a scanline for a colour mapped image plus the alpha
 * information.
 */
int readmappedaline(in, line, alpha)
	image		*in;
	unsigned char	*line;
	unsigned char	*alpha;
{
	return(readmappeda(in, line, alpha));
}
