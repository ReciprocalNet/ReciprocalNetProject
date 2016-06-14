#include <stdio.h>
#include <sys/types.h>
#include <sys/file.h>

#include "vort.h"

/*
 * imagepos
 *
 *	position the image file at pixel x, y. (at the moment this just
 * goes to (0, 0)).
 */
int
imagepos(im, x, y)
	image	*im;
	int	x, y;
{
	im->ebufp = im->ibufp = im->ibuf + im->ibufsize;

	return(lseek(im->fd, (long)im->imaddr, SEEK_SET));
}

