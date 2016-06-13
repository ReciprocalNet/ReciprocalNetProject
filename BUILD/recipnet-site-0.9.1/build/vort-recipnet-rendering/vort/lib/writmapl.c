#include <stdio.h>
#include "vort.h"

/*
 * writemappeda
 *
 *	writes a scanline of colour map indexes from line to file out.
 */
static int writemappeda(out, line, alpha)
	image		*out;
	unsigned char	*line, *alpha;
{
	register int	x;
	unsigned char	indx, jndx, i, run, count, lbuf[127], abuf[127];

	switch (imagetype(out)) {
	case PIX_CMAP:
		for (x = 0; x < (int)imagewidth(out); x++)
			if (writebyte(out, line[x]) == EOF)
				return(0);
		break;
	case PIX_RLECMAP:
		indx = line[0];
		count = 0;
		run = 0;
		for (x = 1; x < (int)imagewidth(out); x++) {
			if (indx != line[x]) {
				if (run != 0) {
					if (writebyte(out, run) == EOF)
						return(0);
					writebyte(out, indx);
					run = 0;
				} else {
					if (count == 127) {
						if (writebyte(out, 255) == EOF)
							return(0);
						for (i = 0; i != 127; i++)
							writebyte(out, lbuf[i]);
						count = 0;
					}
					lbuf[count] = indx;
					count++;
				}
			} else if (run == 127) {
				if (writebyte(out, 127) == EOF)
					return(0);
				writebyte(out, indx);
				run = 0;
			} else {
				if (count != 0) {
					if (writebyte(out, count | 0x80) == EOF)
						return(0);
					for (i = 0; i != count; i++)
						writebyte(out, lbuf[i]);
					count = 0;
				}
				run++;
			}
			indx = line[x];
		}
		if (count != 0) {
			if (writebyte(out, count | 0x80) == EOF)
				return(0);
			for (i = 0; i != count; i++)
				writebyte(out, lbuf[i]);
		}

		if (writebyte(out, run) == EOF)
			return(0);
		writebyte(out, indx);
		break;
	case PIX_ACMAP:
		for (x = 0; x < (int)imagewidth(out); x++) {
			if (writebyte(out, line[x]) == EOF)
				return(0);
			if (alpha == (unsigned char *)NULL) {
				if (writebyte(out, alpha[x]) == EOF)
					return(0);
			} else {
				if (writebyte(out, 0xff) == EOF)
					return(0);
			}
		}
		break;
	case PIX_RLEACMAP:
		indx = line[0];
		if (alpha != (unsigned char *)NULL)
			jndx = alpha[0];
		else
			jndx = 0xff;
		count = 0;
		run = 0;
		for (x = 1; x < (int)imagewidth(out); x++) {
			if (indx != line[x] || (alpha != (unsigned char *)NULL && jndx != alpha[x])) {
				if (run != 0) {
					if (writebyte(out, run) == EOF)
						return(0);
					writebyte(out, indx);
					writebyte(out, jndx);
					run = 0;
				} else {
					if (count == 127) {
						if (writebyte(out, 255) == EOF)
							return(0);
						for (i = 0; i != 127; i++) {
							writebyte(out, lbuf[i]);
							writebyte(out, abuf[i]);
						}
						count = 0;
					}
					lbuf[count] = indx;
					abuf[count] = jndx;
					count++;
				}
			} else if (run == 127) {
				if (writebyte(out, 127) == EOF)
					return(0);
				writebyte(out, indx);
				writebyte(out, jndx);
				run = 0;
			} else {
				if (count != 0) {
					if (writebyte(out, count | 0x80) == EOF)
						return(0);
					for (i = 0; i != count; i++) {
						writebyte(out, lbuf[i]);
						writebyte(out, abuf[i]);
					}
					count = 0;
				}
				run++;
			}
			indx = line[x];
			if (alpha != (unsigned char *)NULL)
				jndx = alpha[x];
		}
		if (count != 0) {
			if (writebyte(out, count | 0x80) == EOF)
				return(0);
			for (i = 0; i != count; i++) {
				writebyte(out, lbuf[i]);
				writebyte(out, abuf[i]);
			}
		}

		if (writebyte(out, run) == EOF)
			return(0);
		writebyte(out, indx);
		writebyte(out, jndx);
		break;
	default:
		vortwarning("writemappeda: bad file format.\n");
		return(0);
	}

	return(1);
}

/*
 * writemappedline
 *
 *	writes a scanline of colour map indexes from line to file out.
 */
int writemappedline(out, line)
	image		*out;
	unsigned char	*line;
{
	return(writemappeda(out, line, (unsigned char *)NULL));
}

/*
 * writemappedaline
 *
 *	writes a scanline of colour map indexes and it associated alpha
 * information to file out.
 */
int writemappedaline(out, line, alpha)
	image		*out;
	unsigned char	*line, *alpha;
{
	return(writemappeda(out, line, alpha));
}
