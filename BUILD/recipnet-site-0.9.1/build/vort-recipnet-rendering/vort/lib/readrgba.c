#include <stdio.h>
#include "vort.h"

/*
 * readrgba
 *
 *	reads in a scanline from file in and puts the scanline into red, green,
 * blue, and, if required, alpha.
 */
static int readrgba(in, red, green, blue, alpha)
	image		*in;
	unsigned char	*red, *green, *blue, *alpha;
{
	register int	x, i;
	unsigned char	r, g, b, a, run;

	switch (imagetype(in)) {
	case PIX_CMAP:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			red[x] = in->red[i];
			green[x] = in->green[i];
			blue[x] = in->blue[i];
			if (alpha != (unsigned char *)NULL)
				alpha[x] = 255;
		}
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
					red[x] = in->red[i];
					green[x] = in->green[i];
					blue[x] = in->blue[i];
					if (alpha != (unsigned char *)NULL)
						alpha[x] = 255;
					run--;
					x++;
				}
			} else {
				i = readbyte(in);
				r = red[x] = in->red[i];
				g = green[x] = in->green[i];
				b = blue[x] = in->blue[i];
				if (alpha != (unsigned char *)NULL)
					alpha[x] = 255;
				x++;
				while (run && x < (int)imagewidth(in)) {
					red[x] = r;
					green[x] = g;
					blue[x] = b;
					if (alpha != (unsigned char *)NULL)
						alpha[x] = 255;
					run--;
					x++;
				}
			}
		}
		break;
	case PIX_ACMAP:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			red[x] = in->red[i];
			green[x] = in->green[i];
			blue[x] = in->blue[i];
			if (alpha != (unsigned char *)NULL)
				alpha[x] = readbyte(in);
			else
				readbyte(in);
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
					red[x] = in->red[i];
					green[x] = in->green[i];
					blue[x] = in->blue[i];
					if (alpha != (unsigned char *)NULL)
						alpha[x] = readbyte(in);
					else
						readbyte(in);
					run--;
					x++;
				}
			} else {
				i = readbyte(in);
				r = red[x] = in->red[i];
				g = green[x] = in->green[i];
				b = blue[x] = in->blue[i];
				if (alpha != (unsigned char *)NULL)
					a = alpha[x] = readbyte(in);
				else
					readbyte(in);
				x++;
				while (run && x < (int)imagewidth(in)) {
					red[x] = r;
					green[x] = g;
					blue[x] = b;
					if (alpha != (unsigned char *)NULL)
						alpha[x] = a;
					run--;
					x++;
				}
			}
		}
		break;
	case PIX_RGB:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			red[x] = i;
			green[x] = readbyte(in);
			blue[x] = readbyte(in);
			if (alpha != (unsigned char *)NULL)
				alpha[x] = 255;
		}
		break;
	case PIX_RGBA:
		for (x = 0; x < (int)imagewidth(in); x++) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			red[x] = i;
			green[x] = readbyte(in);
			blue[x] = readbyte(in);
			if (alpha != (unsigned char *)NULL)
				alpha[x] = readbyte(in);
			else
				readbyte(in);
		}
		break;
	case PIX_RLE:
		x = 0;
		while (x < (int)imagewidth(in)) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			run = i;
			if (run & 0x80) {
				while ((run & 0x7f) && x < (int)imagewidth(in)) {
					red[x] = readbyte(in);
					green[x] = readbyte(in);
					blue[x] = readbyte(in);
					if (alpha != (unsigned char *)NULL)
						alpha[x] = 255;
					run--;
					x++;
				}
			} else {
				r = red[x] = readbyte(in);
				g = green[x] = readbyte(in);
				b = blue[x] = readbyte(in);
				if (alpha != (unsigned char *)NULL)
					a = alpha[x] = 255;
				x++;
				while (run && x < (int)imagewidth(in)) {
					red[x] = r;
					green[x] = g;
					blue[x] = b;
					if (alpha != (unsigned char *)NULL)
						alpha[x] = 255;
					run--;
					x++;
				}
			}
		}
		break;
	case PIX_RLEA:
		x = 0;
		while (x < (int)imagewidth(in)) {
			if ((i = readbyte(in)) == EOF)
				return(0);
			run = i;
			if (run & 0x80) {
				while ((run & 0x7f) && x < (int)imagewidth(in)) {
					red[x] = readbyte(in);
					green[x] = readbyte(in);
					blue[x] = readbyte(in);
					if (alpha != (unsigned char *)NULL)
						alpha[x] = readbyte(in);
					else
						readbyte(in);
					run--;
					x++;
				}
			} else {
				r = red[x] = readbyte(in);
				g = green[x] = readbyte(in);
				b = blue[x] = readbyte(in);
				if (alpha != (unsigned char *)NULL)
					a = alpha[x] = readbyte(in);
				else
					readbyte(in);
				x++;
				while (run && x < (int)imagewidth(in)) {
					red[x] = r;
					green[x] = g;
					blue[x] = b;
					if (alpha != (unsigned char *)NULL)
						alpha[x] = a;
					run--;
					x++;
				}
			}
		}
		break;
	default:
		vortwarning("readrgba: bad file format.\n");
		return(0);
	}

	return(1);
}

/*
 * readrgbline
 *
 *	read in a scanline from file in setting red, green, blue, and
 * ignoring any alpha information that's present.
 */
int readrgbline(in, red, green, blue)
	image	*in;
	unsigned char	*red, *green, *blue;
{
	return(readrgba(in, red, green, blue, (unsigned char *)NULL));
}

/*
 * readrgbaline
 *
 *	read in a scanline from file in setting red, green, blue, and
 * alpha.
 */
int readrgbaline(in, red, green, blue, alpha)
	image	*in;
	unsigned char	*red, *green, *blue, *alpha;
{
	return(readrgba(in, red, green, blue, alpha));
}
