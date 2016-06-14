#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <math.h>

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/Xatom.h>

#include "vort.h"
#include "status.h"

#define	CMAPSIZE	256
#define EV_MASK         KeyPressMask|ButtonReleaseMask|ExposureMask|ButtonPressMask

XWindowAttributes	wa;
static char		title[BUFSIZ], *myname;
static Display		*display;
static Visual		*visual;
static int		theScreen, depth;
static Colormap		cmap;
static Window		winder, rootw, vrootw;
static GC		theGC;
static Pixmap		pm;
static XImage		*ximage;
static unsigned char	carray[CMAPSIZE];
static unsigned int	w, h;
static int		forcecmap;
static int		verbose;
static int		truecolor;
static int		half = 0;

static void	(*copyfun)(const void *src, void *dest, size_t n);

/*
 * xbcopy
 *
 *	copy bytes from from to to.
 */
static void xbcopy(from, to, size)
	const unsigned char	*from;
	unsigned char		*to;
	size_t			size;
{
	while (size--) {
		*to++ = carray[*from++];
	}
}

/*
 * program to read in a color-mapped image and display it on X11, greyscale
 * is used if the image is rgb.
 */
main(ac, av)
	int	ac;
	char	**av;
{
	char		*p, *pp, *mem, *data;
	register int	i, j, ox;
	unsigned int	w2, bytes_per_line;
	int		x, y, val, upsidedown, installmap, wreal, mag = 1;
	long		delay, idelay, timeout;
	unsigned char	v, *red, *green, *blue, *line, *l;
	image		*im;
	XEvent		event;
	char		c, *geomstring, *displaystring;
	float		fmag = 1.0;
	XVisualInfo	*vis_info, rvis_info;
	long		vis_info_mask;
	int		nv;


        if ((myname = strrchr(av[0],'/')) == NULL)
                myname = av[0];
        else
                *myname++;

	if (ac < 2) {
		fprintf(stderr, "%s: usage %s [-f] [-geom geometry] [-display displayname] [-s<n>] -i file [file ...].\n", myname, myname);
		fprintf(stderr, "	-f means force creation of a new colourmap.\n");
		fprintf(stderr, "	-v means be verbose about some things.\n");
		fprintf(stderr, "	-geom (or -g or -geometry) means specify X geometry.\n");
		fprintf(stderr, "	-display means specify X display.\n");
		fprintf(stderr, "	-i means Install a colourmap (for use without a window manager).\n");
		fprintf(stderr, "	-s<n> means sleep this many seconds between images\n");
		fprintf(stderr, "	Pressing the 's' or 'p' key will pause the sequence\n	Pressing any key will then continue it.\n	Press 'q' or ESC to quit the whole sequence\n");
		exit(ERROR);
	}

	geomstring = displaystring = (char *)NULL;
;
        installmap = idelay = verbose = forcecmap = upsidedown = 0;
        for (i = 1; i < ac && *av[i] == '-'; i++) {
                if (strncmp(av[i], "-m", 2) == 0) {             /* magnify */
                        if (sscanf(av[i] , "-m%f", &fmag) != 1) {
                                fprintf(stderr, "%s: bad mag factor\n", myname);
                                exit(1);
                        }
			if (fmag > 0.5) {
				half = 0;
				mag = fmag;
			} else {
				half = 1;
				mag = 1;
			}

			if (verbose)
				fprintf(stderr, "Magnify by %d.\n", mag);

                        if (mag < 1 || mag > 255) {
                                fprintf(stderr, "%s: bad mag factor\n", myname);
                                exit(ERROR);
                        }
                } else if (strncmp(av[i], "-u", 2) == 0) {              /* show
image upside down */
                        upsidedown = 1;
			if (verbose)
				fprintf(stderr, "Display upsidedown.\n");
                } else if (strcmp(av[i], "-g") == 0 ||
			   strcmp(av[i], "-geom") == 0 ||
			   strcmp(av[i], "-geometry") == 0) {
				geomstring = av[++i];

			if (verbose)
				fprintf(stderr, "X geometry (from command line): %s.\n", geomstring);

                } else if (strcmp(av[i], "-display") == 0) {
			displaystring = av[++i];

			if (verbose)
				fprintf(stderr, "Display on: %s.\n", displaystring);
                } else if (strcmp(av[i], "-f") == 0) {
			forcecmap = 1;
			if (verbose)
				fprintf(stderr, "Force new colourmap.\n");
                } else if (strcmp(av[i], "-v") == 0) {
			verbose = 1;
                } else if (strcmp(av[i], "-i") == 0) {
			installmap = 1;
                } else if (strncmp(av[i], "-s", 2) == 0) {
			sscanf(av[i], "-s%d", &idelay);
		} else {
			fprintf(stderr, "Unknown option: '%s'.\n", av[i]);
		}
        }


	/*
	 * Big buffer for reading....
	 */
	for (; i < ac; i++) {
		imagebufsize(3 * 4096);
		if ((im = openimage(av[i], "r")) == (image *)NULL) {
			fprintf(stderr, "disp: can't open file %s.\n", av[i]);
			exit(ERROR);
		}

		strcpy(title, myname);
		strcat(title, ": ");
		if (titlelength(im) != 0)
			strcat(title, imagetitle(im));
		else
			strcpy(title, av[i]);


		if (!display) {
			if (!displaystring)
				displaystring = XDisplayName((char *)NULL);

			if ((display = XOpenDisplay(displaystring)) == NULL) {
				fprintf(stderr, "Can't open display %s.\n", displaystring);
				exit(ERROR);
			}

			/*
			 * See if there is anything in .Xdefaults for us...
			 */
			if (!geomstring)
				geomstring = XGetDefault(display, "vort", "Geometry");

			if (geomstring && verbose)
				fprintf(stderr, "X geometry (from .Xdefaults): %s.\n", geomstring);

			/*
			 * Try for a TrueColor 24 bit visual.
			 */
			truecolor = 3;
			rvis_info.class = TrueColor;  
			rvis_info.depth = 24;  
			vis_info_mask = VisualClassMask | VisualDepthMask;
			vis_info = XGetVisualInfo(display, vis_info_mask, &rvis_info, &nv);

		        if (vis_info) {
				/* Got a 24 bit TrueColor one.... */
				if (verbose)
					fprintf(stderr, "Using 24 bit TrueColor visual type.\n");

			} else {
				rvis_info.class = PseudoColor;  
				rvis_info.depth = 8;  
				vis_info = XGetVisualInfo(display, vis_info_mask, &rvis_info, &nv);
				if (!vis_info) {
					fprintf(stderr, "No 24bit TrueColor nor 8bit PseudoColor visual available\n");
					exit(1);
				}

				if (verbose)
					fprintf(stderr, "Using 8 bit PseudoColor visual type.\n");
				truecolor = 1;
			}

			visual = vis_info[0].visual;
			depth = vis_info[0].depth;
			theScreen = vis_info[0].screen;
			rootw = RootWindow(display, theScreen);
			if (truecolor == 3)
				cmap = XCreateColormap(display, rootw, visual, AllocNone);
		}
			
        wreal = imagewidth(im);
        w = wreal * fmag;
        h = fmag * imageheight(im);

	/*
	 * Make it all 32 bit aligned
        w2 = w;
        if ((i = w % 4) != 0)
                w2 = (w / 4 + 1) * 4;

	 */
	if (truecolor == 3)
		bytes_per_line = w * 4;
	else
		bytes_per_line = w;
	
	if (truecolor == 1)
		setcolormap(im);

	makewindow(geomstring);

	/*
	 * Get mem for data in image
	 */
	if ((mem = data = (char *)malloc((unsigned)(h * bytes_per_line))) == NULL) {
		fprintf(stderr, "mem = NULL\n");
		exit(ERROR);
	}

	
	ximage = XCreateImage(display,
			visual,
			depth,
			ZPixmap,
			0,
			data,
			w, (unsigned int)h,
			32, bytes_per_line
		);

	if (ximage == (XImage *)NULL) {
		fprintf(stderr, "Unable to create ximage\n");
		exit(ERROR);
	}

	y = h - 1;
	x = 0;

	p = mem;

	red = (u_char *)malloc(w);
	green = (u_char *)malloc(w);
	blue = (u_char *)malloc(w);
	line = (u_char *)malloc(w);

	if (truecolor == 3) {
		while (y >= 0) {
			readrgbline(im, red, green, blue);
			for (x = 0; x < w; x++) {
				*p++ = 0;
				*p++ = blue[x];
				*p++ = green[x];
				*p++ = red[x];
			}
			y--;
		}
	} else {
		if (colormapped(im)) {
			while (y >= 0) {
				readmappedline(im, line);
				if (mag == 1) {
					(*copyfun)(line, mem, w);
					mem += bytes_per_line;
					y--;
				} else {
					for (j = 0; j < mag; j++) {
						pp = p;
						for (x = 0; x < wreal; x++)
							for (i = 0; i < mag; i++)
								*pp++ = line[x];

                                                p += bytes_per_line;
					}
					y -= mag;
				}
			}
		} else {
			fprintf(stderr, "Can't display full colour (24bit) image on this screen\n - converting to grayscale for displaying...\n");
			fprintf(stderr, "Use 'median' to convert 24bit image into 8bit image.\n");
				while (y >= 0) {
					readrgbline(im, red, green, blue);

					for (x = 0; x < w; x++) 
						line[x] = red[x] * 0.3 + green[x] * 0.59 + blue[x] * 0.11;
					if (mag == 1) {
						bcopy(line, mem, w);
						mem += bytes_per_line;
						y--;
					} else {
						for (j = 0; j < mag; j++) {
							pp = p;
							for (x = 0; x < wreal; x++)
								for (i = 0; i < mag; i++)
									*pp++ = line[x];

								p += bytes_per_line;
							}
							y -= mag;
						}
					}
				}
			}

			XMapWindow(display, winder);
			if (installmap) {	/* When not under a window manager */
				XInstallColormap(display, cmap);
			}

			/*
			 * Wait for Exposure event.
			 */
			do {
				XNextEvent(display, &event);
			} while (event.type != Expose);
		 
			/*
			 * Copy it to the screen
			 */

			XGetWindowAttributes(display, winder, &wa);

			XPutImage(display, 
				winder,
				theGC, 
				ximage,
				0, 0, 
				0, 0,
				w, h
			);


			/*XSetInputFocus(display, winder, RevertToParent, CurrentTime);*/
			XFlush(display);

		if (idelay > 0) {
			timeout = delay = idelay;
			delay += time(0L);
		} else
			timeout = 0L;

		do {
			XCheckWindowEvent(display, winder, Expose|KeyPressMask|ButtonPressMask, &event);
			if (event.type == Expose) {
				/*
				 * Copy it to the screen
				 */

				XPutImage(display, 
					winder,
					theGC, 
					ximage,
					0, 0, 
					0, 0,
					w, h
				);

				XFlush(display);
			}

			if (idelay > 0)
				timeout = delay - time(0L);

		} while (timeout >= 0L && event.type != KeyPress && event.type != ButtonPress);
		if (XLookupString((XKeyEvent *)&event, &c, 1, NULL, NULL) > 0)
			if (c == 'p' || c == 's') {
				do {
					XNextEvent(display, &event);
				} while (event.type != KeyPress && event.type != ButtonPress);
			}
				
		XUnmapWindow(display, winder);
		if (truecolor == 1)
			XFreeColormap(display, cmap);
		XDestroyImage(ximage);

		if (XLookupString((XKeyEvent *)&event, &c, 1, NULL, NULL) > 0)
			if (c == 'q' || c == 27)
				break;
	}

	XCloseDisplay(display);

	if (c == 'q' || c == 27)
		exit(2);

	exit(0);
}


int
trydefaultcmap(im)
	image	*im;
{
	int	i, stat, num;
	XColor	tmp;

	/*
	 * First try to Allocate out of the default map...
	 */
	cmap = DefaultColormap(display, theScreen);

	num = 0;
	if (colormapped(im)) {
		for (i = 0; i < cmapsize(im); i++) {
#ifdef DEBUG
			fprintf(stderr, "Trying %d  ", i);
#endif
			tmp.pixel = (unsigned long)i;
			tmp.red = (unsigned short)(im->red[i] << 8);
			tmp.green = (unsigned short)(im->green[i] << 8);
			tmp.blue = (unsigned short)(im->blue[i] << 8);
			tmp.flags = DoRed | DoGreen | DoBlue;
			/* Should use XAllocColorcells... */
			if (XAllocColor(display, cmap, &tmp) == 0) {
#ifdef DEBUG
				fprintf(stderr, "XAllocColor failed - default map not used.\n");
#endif
				if (verbose) {
					fprintf(stderr, "XAllocColor failed after %d colours allocated.\n", num);
					fprintf(stderr, "Using own colour map.\n");
				}

				return(0);
			}
#ifdef DEBUG
			fprintf(stderr, "Got %d\n", tmp.pixel);
#endif
			carray[i] = tmp.pixel;
			num++;

		}
	} else {
		for (i = 0; i < CMAPSIZE; i++) {
			tmp.pixel = (unsigned long)i;
			tmp.red = tmp.green = tmp.blue = (unsigned short)(i << 8);
			tmp.flags = DoRed | DoGreen | DoBlue;

			/* Should use XAllocColorcells... */
			if ((stat = XAllocColor(display, cmap, &tmp)) == 0) {
#ifdef DEBUG
				fprintf(stderr, "XAllocColor failed - default map not used.\n");
#endif
				if (verbose) {
					fprintf(stderr, "XAllocColor failed after %d colours allocated.\n", num);
					fprintf(stderr, "Using own colour map.\n");
				}
				return(0);
			}
			carray[i] = tmp.pixel;
			num++;
		}
	}

	if (num > 255)
		return(0);

	if (verbose)
		fprintf(stderr, "%d colours allocated from default colourmap.\n", num);

	copyfun = xbcopy;

	return(1);

}

useourowncmap(im)
	image	*im;
{
	int		i, min, max, val;
	unsigned int	size;
	XColor		*cols;


	cmap = XCreateColormap(display, rootw, visual, AllocAll);

	if (colormapped(im)) {
		size = cmapsize(im);
		if ((cols = (XColor *)malloc(size * sizeof(XColor))) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(ERROR);
		}
		for (i = 0; i < cmapsize(im); i++) {
			cols[i].pixel = (unsigned long)i;
			cols[i].red = (unsigned short)(im->red[i] << 8);
			cols[i].green = (unsigned short)(im->green[i] << 8);
			cols[i].blue = (unsigned short)(im->blue[i] << 8);
			cols[i].flags = DoRed | DoGreen | DoBlue;
		}
	} else {
		size = CMAPSIZE;
		if ((cols = (XColor *)malloc(size * sizeof(XColor))) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(ERROR);
		}
		for (i = 0; i < size; i++) {
			cols[i].pixel = (unsigned long)i;
			cols[i].red = cols[i].green = cols[i].blue = (unsigned short)(i << 8);
			cols[i].flags = DoRed | DoGreen | DoBlue;
		}
	}

	copyfun = bcopy;
	XStoreColors(display, cmap, cols, size);
	free(cols);
}

setcolormap(im)
	image	*im;
{
	if (forcecmap)
		useourowncmap(im);
	else if (!trydefaultcmap(im))
		useourowncmap(im);
}

makewindow(geomstring)
	char	*geomstring;
{
	unsigned long		wattrmask;
	XSetWindowAttributes	wattr;
	XSizeHints		sizehints;
	XWMHints		wmhints;
	unsigned int		bw, mask, wid, hi;
	int			xpos, ypos;

	bw = 2;
	mask = 0;
	sizehints.flags = 0;
	sizehints.x = DisplayWidth(display, theScreen) / 2 - w / 2;
	sizehints.y = DisplayHeight(display, theScreen) / 2 - h / 2;
	sizehints.width = w;
	sizehints.height = h;

	if (geomstring) {
		mask = XParseGeometry(geomstring, &xpos, &ypos, &wid, &hi);

		if (mask & XValue) {
			sizehints.flags |= USPosition;
			sizehints.x = xpos;
		}

		if (mask & YValue) {
			sizehints.flags |= USPosition;
			sizehints.y = ypos;
		}

		if (mask & WidthValue) {
			sizehints.flags |= USSize;
			sizehints.width = wid;
		}

		if (mask & HeightValue) {
			sizehints.flags |= USSize;
			sizehints.height = hi;
		}

		if (mask & XNegative)
			 sizehints.x = DisplayWidth(display, theScreen) - 2*bw - sizehints.width + xpos;

		if (mask & YNegative)
			sizehints.y = DisplayHeight(display, theScreen) - 2*bw - sizehints.height + ypos;

	} else
		sizehints.flags = PPosition | PSize;

	if (sizehints.width < w)
		fprintf(stderr, "Specified window width (%d) is less than image width (%d).\n", sizehints.width, w);

	if (sizehints.height < h)
		fprintf(stderr, "Specified window height (%d) is less than image height (%d).\n", sizehints.height, h);


        wattr.background_pixel = BlackPixel(display, theScreen);
        wattr.border_pixel = WhitePixel(display, theScreen);
	wattrmask = CWBackPixel | CWBorderPixel;
	wattr.colormap = cmap;
	wattrmask |= CWColormap;

	winder = XCreateWindow(display,
			rootw,
			sizehints.x, sizehints.y,
			sizehints.width, sizehints.height,
			2,
			depth,
			InputOutput,
			visual,
			wattrmask,
			&wattr
		);

	theGC = XCreateGC(display, winder, 0L, NULL);

	if (truecolor == 1)
		XSetWindowColormap(display, winder, cmap);

	XSetStandardProperties(display,
			winder,
			title, 
			myname,
			None,
			(char **)NULL, 0,
			&sizehints
	);

        wmhints.initial_state = NormalState;
        wmhints.input = True;
        wmhints.flags = StateHint | InputHint;
        XSetWMHints(display, winder, &wmhints);

	XSetForeground(display, theGC, WhitePixel(display, theScreen));
	XSetBackground(display, theGC, BlackPixel(display, theScreen));

        XSelectInput(display, winder, EV_MASK);
}
