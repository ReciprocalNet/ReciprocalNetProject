#include <stdio.h>
#include <sys/types.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "vort.h"

/*
 * copyheader
 *
 *	copy all the header details for b into a, including the title, etc..
 */
void copyheader(a, b)
	image	*a, *b;
{
	int	i;

	a->magic = b->magic;
	a->pixw = b->pixw;
	a->pixh = b->pixh;
	a->pixd = b->pixd;
	a->tlength = b->tlength;
	a->background = b->background;
	a->date = b->date;

	if (imagefragment(b)) {
		imagefragment(a) = 1;
		imagexaddr(a) = imagexaddr(b);
		imageyaddr(a) = imageyaddr(b);
		imageorigwidth(a) = imageorigwidth(b);
		imageorigheight(a) = imageorigheight(b);
	}

	if (titlelength(a) != 0) {
		imagetitle(a) = (char *)malloc(titlelength(a));
		if (imagetitle(a) == (char *)NULL) 
			vorterror("copyheader: malloc returns NULL.\n");
		strcpy(imagetitle(a), imagetitle(b));
	} else
		imagetitle(b) = "";

	if (colormapped(a)) {
		cmapsize(a) = cmapsize(b);

		a->red = (unsigned char *)malloc(cmapsize(a));
		if (a->red == (unsigned char *)NULL)
			vorterror("copyheader: malloc returns NULL.\n");
		for (i = 0; i < (int)cmapsize(a); i++)
			a->red[i] = b->red[i];

		a->green = (unsigned char *)malloc(cmapsize(a));
		if (a->green == (unsigned char *)NULL)
			vorterror("copyheader: malloc returns NULL.\n");
		for (i = 0; i < (int)cmapsize(a); i++)
			a->green[i] = b->green[i];

		a->blue = (unsigned char *)malloc(cmapsize(a));
		if (a->blue == (unsigned char *)NULL)
			vorterror("copyheader: malloc returns NULL.\n");
		for (i = 0; i < (int)cmapsize(a); i++)
			a->blue[i] = b->blue[i];
	}
}

/*
 * setcmap
 *
 *	set the colour map of the image
 */
void setcmap(im, numc, red, green, blue)
	image		*im;
	int		numc;
	unsigned char	*red, *green, *blue;
{
	int		i;

	cmapsize(im) = numc;

	im->red = (unsigned char *)malloc(cmapsize(im));
	if (im->red == (unsigned char *)NULL)
		vorterror("setcmap: malloc returns NULL.\n");
	for (i = 0; i < numc; i++)
		im->red[i] = red[i];

	im->green = (unsigned char *)malloc(cmapsize(im));
	if (im->green == (unsigned char *)NULL)
		vorterror("setcmap: malloc returns NULL.\n");
	for (i = 0; i < numc; i++)
		im->green[i] = green[i];

	im->blue = (unsigned char *)malloc(cmapsize(im));
	if (im->blue == (unsigned char *)NULL)
		vorterror("setcmap: malloc returns NULL.\n");
	for (i = 0; i < numc; i++)
		im->blue[i] = blue[i];
}

/*
 * writeheader
 *
 *	write out the header details for an image e.g. title, colormap, etc...
 * - should be done before anything else is written to the file.
 */
int writeheader(im)
	image	*im;
{
	long	count, extra;
	int	entries;
	char	c;

	lseek(im->fd, 0L, SEEK_SET);

	count = write(im->fd, VERSION_STR, VSTR_SIZE);

	/*
	 * write out address of directory
	 */
	count += writelongval(im->fd, (unsigned long)(VSTR_SIZE + 5), 4);

	/*
	 * write out type
	 */
	c = V_DIRECTORY;
	count += write(im->fd, &c, 1);

	entries = 1;

	extra = 0;

	if (titlelength(im) != 0)
		entries++;

	if (colormapped(im))
		entries++;

	/*
	 * write out size
	 */
	count += writelongval(im->fd, (unsigned long)(entries * 6), 1);

	if (titlelength(im) != 0) {
		c = D_OBJECT;
		count += write(im->fd, &c, 1);
		count += writelongval(im->fd, (unsigned long)(count + 5 + (entries - 1) * 6), 4);

		lseek(im->fd, (unsigned long)(count + (entries - 1) * 6), SEEK_SET);

		extra = writetextobj(im);

		lseek(im->fd, count, SEEK_SET);
	}
	
	if (colormapped(im)) {
		c = D_OBJECT;
		count += write(im->fd, &c, 1);
		count += writelongval(im->fd, (unsigned long)(count + 11 + extra), 4);

		im->cmaddr = count + 6 + extra;

		lseek(im->fd, (unsigned long)(count + 6 + extra), SEEK_SET);

		extra += writecolormapobj(im);

		lseek(im->fd, count, SEEK_SET);
	}

	c = D_OBJECT;
	count += write(im->fd, &c, 1);
	count += writelongval(im->fd, (unsigned long)(count + extra + 5), 4);

	lseek(im->fd, count + extra, SEEK_SET);

	count += writeimageobj(im);

	im->imaddr = count + extra;

	lseek(im->fd, im->imaddr, SEEK_SET);	/* shift to start of image */

	return count+extra;
}
