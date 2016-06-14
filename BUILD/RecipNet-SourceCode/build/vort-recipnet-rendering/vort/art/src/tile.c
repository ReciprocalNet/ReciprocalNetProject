#include <stdio.h>
#include <math.h>
#include <fcntl.h>
#include "art.h"
#include "gram.h"
#include "macro.h"

extern attr	*astackp;

extern symbol	*findsym(), *insertsym();

static symbol	*tiles = NULL;

/*
 * tileinit
 *
 *	initialise the function pointers and fields for a tile pattern. 
 */
void
tileinit(name, map, pixw, pixh)
	char		*name;
	char		**map;
	unsigned short	*pixw, *pixh;
{
	register	int	i;
	int		nlines;
	image	*im;
	symbol	*sym;
	char	 *red, *green, *blue, buf[BUFSIZ], *cp;

	if ((sym = findsym(tiles, name)) != (symbol *)NULL) {
		*pixw = sym->u.tile.width;
		*pixh = sym->u.tile.height;
		*map = sym->u.tile.pic;
		return;
	}

	if ((im = openimage(name, "r")) == (image *)NULL) {
		sprintf(buf, "art: error opening tile file %s.\n", name);
		fatal(buf);
	}

	sym = insertsym(&tiles, name);

	*pixw = sym->u.tile.width = imagewidth(im);
	nlines = *pixh = sym->u.tile.height = imageheight(im);

	red = (char *)smalloc(imagewidth(im));
	green = (char *)smalloc(imagewidth(im));
	blue = (char *)smalloc(imagewidth(im));

	cp = *map = sym->u.tile.pic = (char *)scalloc(imagewidth(im), (unsigned long)imageheight(im) * 3);

	while (readrgbline(im, red, green, blue) && nlines-- != 0)
		for (i = 0; i < imagewidth(im); i++) {
			*cp++ = red[i];
			*cp++ = green[i];
			*cp++ = blue[i];
		}

	closeimage(im);
	free(red);
	free(green);
	free(blue);
}

