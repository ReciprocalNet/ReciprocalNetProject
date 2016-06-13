#include <stdio.h>
#include "vort.h"

/*
 * writergba
 *
 *	writes out the rgba scanline described by red, green, and blue into out.
 */
static int writergba(out, red, green, blue, alpha)
	image		*out;
	unsigned char	*red, *green, *blue, *alpha;
{
	register int	x, i;
	unsigned char	r, g, b, a, count, run, rbuf[127], gbuf[127], bbuf[127], abuf[127];

	switch (imagetype(out)) {
	case PIX_RGB:
		for (x = 0; x < (int)imagewidth(out); x++) {
			if (writebyte(out, red[x]) == EOF)
				return(0);
			writebyte(out, green[x]);
			writebyte(out, blue[x]);
		}
		break;
	case PIX_RLE:
		r = red[0];
		g = green[0];
		b = blue[0];
		count = 0;
		run = 0;
		for (x = 1; x < (int)imagewidth(out); x++) {
			if (red[x] != r || green[x] != g || blue[x] != b) {
				if (run != 0) {
					if (writebyte(out, run) == EOF)
						return(0);
					writebyte(out, r);
					writebyte(out, g);
					writebyte(out, b);
					run = 0;
				} else {
					if (count == 127) {
						if (writebyte(out, 255) == EOF)
							return(0);
						for (i = 0; i != 127; i++) {
							writebyte(out, rbuf[i]);
							writebyte(out, gbuf[i]);
							writebyte(out, bbuf[i]);
						}
						count = 0;
					}
					rbuf[count] = r;
					gbuf[count] = g;
					bbuf[count] = b;
					count++;
				}
			} else if (run == 127) {
				if (writebyte(out, 127) == EOF)
					return(0);
				writebyte(out, r);
				writebyte(out, g);
				writebyte(out, b);
				run = 0;
			} else {
				if (count != 0) {
					if (writebyte(out, count | 0x80) == EOF)
						return(0);
					for (i = 0; i != count; i++) {
						writebyte(out, rbuf[i]);
						writebyte(out, gbuf[i]);
						writebyte(out, bbuf[i]);
					}
					count = 0;
				}
				run++;
			}
			r = red[x];
			g = green[x];
			b = blue[x];
		}
		if (count != 0) {
			if (writebyte(out, count | 0x80) == EOF)
				return(0);
			for (i = 0; i != count; i++) {
				writebyte(out, rbuf[i]);
				writebyte(out, gbuf[i]);
				writebyte(out, bbuf[i]);
			}
		} 
		if (writebyte(out, run) == EOF)
			return(0);
		writebyte(out, r);
		writebyte(out, g);
		writebyte(out, b);
		break;
	case PIX_RGBA:
		for (x = 0; x < (int)imagewidth(out); x++) {
			if (writebyte(out, red[x]) == EOF)
				return(0);
			writebyte(out, green[x]);
			writebyte(out, blue[x]);
			if (alpha != (unsigned char *)NULL)
				writebyte(out, alpha[x]);
			else
				writebyte(out, 0xff);
		}
		break;
	case PIX_RLEA:
		r = red[0];
		g = green[0];
		b = blue[0];
		if (alpha != (unsigned char *)NULL)
			a = alpha[0];
		else
			a = 0xff;
		count = 0;
		run = 0;
		for (x = 1; x < (int)imagewidth(out); x++) {
			if (red[x] != r || green[x] != g || blue[x] != b || (alpha != (unsigned char *)NULL && alpha[x] != a)) {
				if (run != 0) {
					if (writebyte(out, run) == EOF)
						return(0);
					writebyte(out, r);
					writebyte(out, g);
					writebyte(out, b);
					writebyte(out, a);
					run = 0;
				} else {
					if (count == 127) {
						if (writebyte(out, 255) == EOF)
							return(0);
						for (i = 0; i != 127; i++) {
							writebyte(out, rbuf[i]);
							writebyte(out, gbuf[i]);
							writebyte(out, bbuf[i]);
							writebyte(out, abuf[i]);
						}
						count = 0;
					}
					rbuf[count] = r;
					gbuf[count] = g;
					bbuf[count] = b;
					abuf[count] = a;
					count++;
				}
			} else if (run == 127) {
				if (writebyte(out, 127) == EOF)
					return(0);
				writebyte(out, r);
				writebyte(out, g);
				writebyte(out, b);
				writebyte(out, a);
				run = 0;
			} else {
				if (count != 0) {
					if (writebyte(out, count | 0x80) == EOF)
						return(0);
					for (i = 0; i != count; i++) {
						writebyte(out, rbuf[i]);
						writebyte(out, gbuf[i]);
						writebyte(out, bbuf[i]);
						writebyte(out, abuf[i]);
					}
					count = 0;
				}
				run++;
			}
			r = red[x];
			g = green[x];
			b = blue[x];
			if (alpha != (unsigned char *)NULL)
				a = alpha[x];
			else
				a = 0xff;
		}
		if (count != 0) {
			if (writebyte(out, count | 0x80) == EOF)
				return(0);
			for (i = 0; i != count; i++) {
				writebyte(out, rbuf[i]);
				writebyte(out, gbuf[i]);
				writebyte(out, bbuf[i]);
				writebyte(out, abuf[i]);
			}
		} 
		if (writebyte(out, run) == EOF)
			return(0);
		writebyte(out, r);
		writebyte(out, g);
		writebyte(out, b);
		writebyte(out, a);
		break;
	default:
		vortwarning("writergba: bad output file format.\n");
		return(0);
	}

	return(1);
}

/*
 * writergbline
 *
 *	write out the red, green, and blue values to out.
 */
int writergbline(out, red, green, blue)
	image		*out;
	unsigned char	*red, *green, *blue;
{
	return(writergba(out, red, green, blue, (unsigned char *)NULL));
}

/*
 * writergbaline
 *
 *	write out the red, green, blue and alpha values to out.
 */
int writergbaline(out, red, green, blue, alpha)
	image		*out;
	unsigned char	*red, *green, *blue, *alpha;
{
	return(writergba(out, red, green, blue, alpha));
}
