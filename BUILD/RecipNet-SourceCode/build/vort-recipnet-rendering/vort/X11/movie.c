#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include <X11/Xlib.h>
#include <X11/Xutil.h>

#include <poll.h>

#include "vort.h"
#include "status.h"

int
usleep(usec)
unsigned long usec;
{
    struct timeval timeout;
    timeout.tv_usec = usec % (unsigned long) 1000000;
    timeout.tv_sec = usec / (unsigned long) 1000000;

    select(0, NULL, NULL, NULL, &timeout);

    return 0;
}



static int              Sleep;
static int              forcecmap;



#define	CMAPSIZE	250
#define	W_MIN_WIDTH	286
#define EV_MASK         KeyPressMask|ButtonReleaseMask|ExposureMask|ButtonPressMask
#define MAX(a, b)       ((a) > (b) ? (a) : (b))


static char		title[BUFSIZ], *myname, **titles;
static Display		*display;
static int		theScreen, depth;
static Visual		*visual;
static Colormap		cmap;
static Window		winder, rootw;
static GC		theGC;
static Pixmap		pm, *ppm;
static XImage		*xi, **xip;
static int		f, fnum;
static int		font_w, font_h, one = 1, color, dir, cur_frame = -1;
static unsigned short	oldw, oldh, do_titles, tlen, upsidedown, usepixmaps;
static unsigned long	white, black;
static unsigned int	w, h, window_width, window_height;

static int		stopped, xf, lx1, lx2, px1, px2, rx1, rx2, sx1, sx2, qx1, qx2, by;
unsigned char		carray[CMAPSIZE];

static int		sync;
static void		(*copyfun)(const void *src, void *dest, size_t n);

/*
 * xbcopy
 *
 *	copy bytes from from to to, taking into account the colourmap.
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
	register int	i;
	float		val;
	image		*im;
	XEvent		event;
	XVisualInfo	visualinfo;
	char		*geomstring, *displaystring;

        if ((myname = strrchr(av[0],'/')) == NULL)
                myname = av[0];
        else
                *myname++;

	if (ac < 2) {
                fprintf(stderr, "usage: %s [-p] [-t] [-f] [-u] [-d secs] [-s] [-geom geometry] [-display displayname] files\n", myname);
                fprintf(stderr, "	[-p] means use Pixmaps not XImages.\n");
                fprintf(stderr, "	[-t] means show titles (if any).\n");
                fprintf(stderr, "	[-u] means upsidedown.\n");
                fprintf(stderr, "	[-f] means force new colourmap creation.\n");
                fprintf(stderr, "	[-d secs] means delay in seconds.\n");
                fprintf(stderr, "	[-s] means syncronize display.\n");
                fprintf(stderr, "	[-geom] (or -g or -geometry) means the standard X geometry string.\n");
                fprintf(stderr, "	[-display] means the X display string.\n");
                exit(ERROR);
        }

	tlen = 0;
        do_titles = 0;
        upsidedown = 0;
	usepixmaps = 0;
	forcecmap = 0;
	val = 0.0;
        Sleep = 0;
	sync = 0;
	geomstring = displaystring = (char *)NULL;

        for (f = 1; f < ac && *av[f] == '-'; f++) {
                if (strcmp(av[f], "-t") == 0) {
                        do_titles = 1;
                } else if (strcmp(av[f], "-u") == 0) {
                        upsidedown = 1;
                } else if (strcmp(av[f], "-p") == 0) {
			usepixmaps = 1;
                } else if (strcmp(av[f], "-f") == 0) {
			forcecmap = 1;
		} else if (strcmp(av[f], "-d") == 0) {
                        val = atof(av[++f]);
                        Sleep = val * 1000000;
                        printf("Delay %d usecs between frames.\n",Sleep);
                } else if (strcmp(av[f], "-s") == 0) {
                        sync = 1;
                } else if (strcmp(av[f], "-geom") == 0 || 
                           strcmp(av[f], "-geometry") == 0 ||
                           strcmp(av[f], "-g") == 0) {
				geomstring = av[++f];
			
                } else if (strcmp(av[f], "-display") == 0) {
				displaystring = av[++f];
                }
        }

	strcpy(title, myname);
	strcat(title, ": ");

	if (!displaystring)
		displaystring = XDisplayName((char *)NULL);

	if ((display = XOpenDisplay(displaystring)) == NULL) {
		fprintf(stderr, "Can't open display %s.\n", displaystring);
		exit(ERROR);
	}

	/*
	 * See if there is something in .Xdefaults for us...
	 */
	if (!geomstring)
		geomstring = XGetDefault(display, "vort", "Geometry");


	theScreen = DefaultScreen(display);

	if ((depth = XDisplayPlanes(display, theScreen)) < 8) {
		fprintf(stderr, "You need a 256 (or more) colour display device\n");
		exit(ERROR);
	}

	i = XMatchVisualInfo(display, theScreen, depth, PseudoColor, &visualinfo);
	if (!i) {
		fprintf(stderr, "Need a PseudoColor visual type.\n");
		exit(ERROR);
	}

	visual = visualinfo.visual;

	rootw = RootWindow(display, theScreen);
	theGC = DefaultGC(display, theScreen);

	if (usepixmaps)
		ppm = (Pixmap *)malloc((ac - f) * sizeof(Pixmap));
	else
		xip = (XImage **)malloc((ac - f) * sizeof(XImage *));

        if (do_titles)
                titles = (char **)malloc(sizeof(char *) * (ac - f));

	read_files(ac, av);

	makewindow(geomstring);

	XMapWindow(display, winder);


        /*
         * Wait for Exposure event.
         */
        do {
                XNextEvent(display, &event);
        } while (event.type != Expose);


	makebuttons();
	stopped = 1;

	XSetInputFocus(display, winder, RevertToParent, CurrentTime);
        if (stopped) {
                cur_frame = 0;
                show_frame();
		if(sync) XSync(display,0);
        }


	dir = 1;
	while (1) {
                if (!stopped) {
                        show_frame();
                        if(Sleep > 0) {
                                XSync(display, 0);
                                usleep(Sleep);
                        } else
				if(sync) XSync(display, 0);
                } else
			XSync(display, 0);

		if (QLength(display) > 0) {
			XCheckWindowEvent(display, winder, EV_MASK, &event);
			check_events(&event);
		}
	}
}

int
trydefaultcmap(im)
	image	*im;
{
	int	i, stat;
	XColor	tmp;

	/*
	 * First try to Allocate out of the default map...
	 */
	cmap = DefaultColormap(display, theScreen);

	if (colormapped(im)) {
		color = 1;
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
			if ((stat = XAllocColor(display, cmap, &tmp)) == 0) {
#ifdef DEBUG
				fprintf(stderr, "XAllocColor failed - default colourmap not used.\n");
#endif
				return(0);
			}
#ifdef DEBUG
			fprintf(stderr, "Got %d\n", tmp.pixel);
#endif
			carray[i] = tmp.pixel;

		}
	} else {
		for (i = 0; i < CMAPSIZE; i++) {
			tmp.pixel = (unsigned long)i;
			tmp.red = tmp.green = tmp.blue = (unsigned short)(i << 8);
			tmp.flags = DoRed | DoGreen | DoBlue;

			/* Should use XAllocColorcells... */
			if (XAllocColor(display, cmap, &tmp) == 0) {
#ifdef DEBUG
				fprintf(stderr, "XAllocColor failed - default map not used.\n");
#endif
				return(0);
			}
			carray[i] = tmp.pixel;
		}
	}

	black = BlackPixel(display, theScreen);
	white = WhitePixel(display, theScreen);

	copyfun = xbcopy;

	return(1);

}

#define SQR(a)	(float)(a) * (float)(a)

useourowncmap(im)
	image	*im;
{
	XColor		*cols;
	int		i;
	unsigned int	size;
	float		min, max, val;

	max = -1.0e+30;
	min =  1.0e+30;

	cmap = XCreateColormap(display, rootw, visual, AllocAll);

	if (colormapped(im)) {
		size = cmapsize(im);
		if ((cols = (XColor *)malloc(size * sizeof(XColor))) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(ERROR);
		}
		color = 1;
		for (i = 0; i < size; i++) {
			cols[i].pixel = (unsigned long)i;
			cols[i].red = (unsigned short)(im->red[i] << 8);
			cols[i].green = (unsigned short)(im->green[i] << 8);
			cols[i].blue = (unsigned short)(im->blue[i] << 8);
			cols[i].flags = DoRed | DoGreen | DoBlue;
			val = SQR(cols[i].red) + SQR(cols[i].green) + SQR(cols[i].blue);
			if (val < min) {
				black = (unsigned long)i;
				min = val;
			}
			if (val > max) {
				white = (unsigned long)i;
				max = val;
			}
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
		black = 0L;
		white = (unsigned long)(size - 1);
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
	unsigned int		wattrmask;
	XSetWindowAttributes	wattr;
	XSizeHints		sizehints;
	XWMHints		wmhints;
	unsigned int		bw = 3;
	XGCValues		xgcvals;
	XFontStruct		*font;
	unsigned int		mask, wid, hi;
	int			xpos, ypos;


	if ((font = XLoadQueryFont(display, "fixed")) == (XFontStruct *)NULL) {		
		fprintf(stderr, "Couldn't get 'fixed' font.\n");
		exit(ERROR);
	}

        font_h = font->max_bounds.ascent + font->max_bounds.descent;
        font_w = font->max_bounds.width;

        xgcvals.font = XLoadFont(display, "fixed");
        XChangeGC(display, theGC, GCFont, &xgcvals);

	mask = 0;
	sizehints.flags = 0;
	sizehints.width = (w < W_MIN_WIDTH ? W_MIN_WIDTH : w);
	sizehints.height = h + font_h + 8 + (do_titles ? (8 + font_h) : 0);
	sizehints.x = DisplayWidth(display, theScreen) / 2 - sizehints.width / 2;
	sizehints.y = DisplayHeight(display, theScreen) / 2 - sizehints.height / 2;

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

/*
	fprintf(stderr, "%d %d, %d %d\n", sizehints.x, sizehints.y, sizehints.width, sizehints.height);
*/

	/* so we can get at the quit button.... */
	sizehints.flags |= PMinSize;
	sizehints.min_width = W_MIN_WIDTH;
	sizehints.min_height = 20;

        wattr.background_pixel = black;
        wattr.border_pixel = white;
	wattr.colormap = cmap;
	wattrmask = CWBackPixel | CWBorderPixel | CWColormap;


	winder = XCreateWindow(display,
			rootw,
			sizehints.x, sizehints.y,
			sizehints.width, sizehints.height,
			bw,
			depth,
			CopyFromParent,
			visual,
			wattrmask,
			&wattr
		);

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
        XSetIconName(display, winder, "VORT");

#ifdef PRE_R4
	XInstallColormap(display, cmap);
#endif

        XSelectInput(display, winder, EV_MASK);
}

makebuttons()
{
	by = font_h + 4;
	lx1 = 4;
	lx2 = lx1 + font_w * 4 + 4;
	drawbut(lx1, lx2, " << ", 0);

	px1 = lx2 + 4;
	px2 = px1 + font_w * 6 + 4;
	drawbut(px1, px2, " Play ", 0);

	rx1 = px2 + 4;
	rx2 = rx1 + font_w * 4 + 4;
	drawbut(rx1, rx2, " >> ", 0);

	sx1 = rx2 + 4;
	sx2 = sx1 + font_w * 6 + 4;
	drawbut(sx1, sx2, " Stop ", 0);

	xf = font_w * 12;
	XSetForeground(display, theGC, black);
	XSetBackground(display, theGC, white);
	XFillRectangle(display, winder, theGC, sx2 + 8, 5, xf , by);
	XSetForeground(display, theGC, white);
	XDrawString(display, winder, theGC, sx2 + 12, font_h + 3, "Frame: ", 7);
	xf = sx2 + 12 + 8 * font_w;

	qx1 = xf + 3 * font_w + 12;
	qx2 = qx1 + font_w * 6 + 4;
	drawbut(qx1, qx2, " QUIT ", 0);
}

drawbut(x1, x2,  text, invert)
	int	x1, x2, invert;
	char	*text;
{
	int		xc, yc;
	unsigned long	fore, back;

	if (invert) {
		fore = black;
		back = white;
	} else {
		fore = white;
		back = black;
	}

	XSetForeground(display, theGC, back);
	XSetBackground(display, theGC, fore);
	XFillRectangle(display, winder, theGC, x1, 4, (unsigned)(x2 - x1),(unsigned)by);
	XSetForeground(display, theGC, fore);
	XSetBackground(display, theGC, back);
	XDrawRectangle(display, winder, theGC, x1, 4, (unsigned)(x2 - x1),(unsigned)by);
	xc = (x2 + x1) / 2 - font_w * strlen(text) / 2; 
	yc = font_h + 3;
	XDrawString(display, winder, theGC, xc, yc, text, strlen(text));
}

#define IN_RECT(b, x1, x2)	((b)->x > x1 && (b)->x < x2 && (b)->y > 4 && (b)->y < by + 4)
#define LEFT(b)	IN_RECT((b), lx1, lx2)
#define PLAY(b)	IN_RECT((b), px1, px2)
#define RIGHT(b)	IN_RECT((b), rx1, rx2)
#define STOP(b)	IN_RECT((b), sx1, sx2)
#define QUIT(b)	IN_RECT((b), qx1, qx2)

check_events(event)
	XEvent	*event;
{
	int	i;
	XButtonEvent  	*button_event;

	button_event = (XButtonEvent *)event;
	if (event->type == ButtonRelease) {
		if (button_event->button == Button1) {
			if (LEFT(button_event)) {
				dir = -1;
				drawbut(lx1, lx2, " << ", 0);
				if (stopped)
					show_frame();

			} else if (PLAY(button_event)) {
				stopped = 0;
				drawbut(px1, px2, " Play ", 0);
			} else if (RIGHT(button_event)) {
				dir = 1;
				if (stopped)
					show_frame();
				drawbut(rx1, rx2, " >> ", 0);
			} else if (STOP(button_event)) {
				stopped = 1;
				drawbut(sx1, sx2, " Stop ", 0);
			} else if (QUIT(button_event)) {
				drawbut(qx1, qx2, " QUIT ", 0);
				XUnmapWindow(display, winder);
				XFreeColormap(display, cmap);
				for (i = 0; i < fnum; i++)
					if (usepixmaps)
						XFreePixmap(display, ppm[i]);
					else
						XDestroyImage(xip[i]);

				XCloseDisplay(display);
				exit(ALLOK);
			}

		}

	} else if (event->type == ButtonPress) {
		if (LEFT(button_event)) 
			drawbut(lx1, lx2, " << ", 1);
		else if (PLAY(button_event))
			drawbut(px1, px2, " Play ", 1);
		else if (RIGHT(button_event))
			drawbut(rx1, rx2, " >> ", 1);
		else if (STOP(button_event))
			drawbut(sx1, sx2, " Stop ", 1);
		else if (QUIT(button_event))
			drawbut(qx1, qx2, " QUIT ", 1);

	} else if (event->type == Expose) {
		drawbut(lx1, lx2, " << ", 0);
		drawbut(px1, px2, " Play ", 0);
		drawbut(rx1, rx2, " >> ", 0);
		drawbut(sx1, sx2, " Stop ", 0);
		drawbut(qx1, qx2, " QUIT ", 0);
		xf = font_w * 12;
		XSetForeground(display, theGC, black);
		XSetBackground(display, theGC, white);
		XFillRectangle(display, winder, theGC, sx2 + 8, 5, xf , by);
		XSetForeground(display, theGC, white);
		XDrawString(display, winder, theGC, sx2 + 12, font_h + 3, "Frame: ", 7);
		xf = sx2 + 12 + 8 * font_w;

		if (stopped) {
			cur_frame--;
			show_frame();
		}
	}
}

show_frame()
{
	char	buf[12];
	int	n;

	cur_frame += dir;
	if (cur_frame >= fnum)
		cur_frame = 0;
	else if (cur_frame < 0)
		cur_frame = fnum - 1;

        /*
         * Copy it to the screen
         */

	if (usepixmaps) {
		XCopyArea(display, 
			ppm[cur_frame],
			winder,
			theGC,
			0, 0, 
			(unsigned int)oldw, (unsigned int)oldh,
			0, font_h + 12
		);
	} else {
		XPutImage(display, 
			winder,
			theGC, 
			xip[cur_frame],
			0, 0, 
			0, font_h + 12,
			oldw, oldh
		);
	}

	/*
	 * update frame number display...
	 */
	sprintf(buf, "%d", cur_frame);
	n = strlen(buf);
	XSetForeground(display, theGC, black);
	XFillRectangle(display, winder, theGC, xf, 5, 3 * font_w, by);
	XSetForeground(display, theGC, white);
	XDrawString(display, winder, theGC, xf, font_h + 3, buf, n);

	/*
	 * update the title (if any)
	 */
	if (do_titles) {
		n = strlen(titles[cur_frame]);
		XSetForeground(display, theGC, black);
		XFillRectangle(display, winder, theGC, 0, h + font_h + 12, W_MIN_WIDTH, by);
		XSetForeground(display, theGC, white);
		XDrawString(display, winder, theGC, 7, h + 2 * font_h + 12, titles[cur_frame], n);
	}

	
}

read_files(ac, av)
	int	ac;
	char	**av;
{
	u_char		*line, *red, *green, *blue;
	unsigned int	w2, bytes_per_line;
	int		x, y, len;
	char		*mem, *data;
	image		*im;

	imagebufsize(3 * 4096);


	for (fnum = f; fnum < ac; fnum++) {

		fprintf(stderr, "Reading file %s\n", av[fnum]);

		if ((im = openimage(av[fnum], "r")) == (image *)NULL) {
			fprintf(stderr, "disp: can't open file %s.\n", av[fnum]);
			exit(ERROR);
		}

		w = imagewidth(im);
		h = imageheight(im);
		/*
		 * Make it all 32 bit aligned
		 */
		w2 = w;
		if ((x = w % 4) != 0)
			w2 = (w / 4 + x) * 4;

		bytes_per_line = w2;

		if (one) {
			oldw = w;
			oldh = h;
			one = 0;
			setcolormap(im);
			line = (u_char *)malloc(w2);
			if (!colormapped(im)) {
				red = (u_char *)malloc(w);
				green = (u_char *)malloc(w);
				blue = (u_char *)malloc(w);
			}
			if (usepixmaps) {
				/*
				 * Get mem for data in image
				 */
				if ((mem = data = (char *)malloc((unsigned)(w2 * h))) == NULL) {
					fprintf(stderr, "mem = NULL\n");
					exit(ERROR);
				}
				xi = XCreateImage(display,
						visual,
						depth,
						ZPixmap,
						0,
						data,
						w2, (unsigned int)h,
						8, 0
					);

				if (xi == (XImage *)NULL) {
					fprintf(stderr, "Unable to create ximage\n");
					exit(ERROR);
				}
			}

		} else if (oldw != w || oldh != h) {
			fprintf(stderr, "All images must be the same size you dork!\n");
			exit(ERROR);
		}

		len = titlelength(im);
                tlen = MAX(tlen, len);

                if (do_titles) {
                        if (len != 0) {
                                titles[fnum - f] = (char *)malloc((unsigned)len);
                                strcpy(titles[fnum - f], imagetitle(im));
                        } else {
                                titles[fnum - f] = (char *)malloc(1);
                                titles[fnum - f][0] = '\0';
                        }
                }


		
		if (!usepixmaps) {
			/*
			 * Allocate the XImage for this frame 
			 */

			/*
			 * Get mem for data in image
			 */
			if ((mem = data = (char *)malloc((unsigned)(w2 * h))) == NULL) {
				fprintf(stderr, "mem = NULL\n");
				exit(ERROR);
			}
			xi = xip[fnum - f] =  XCreateImage(display,
					visual,
					depth,
					ZPixmap,
					0,
					data,
					w2, (unsigned int)h,
					8, 0
				);

			if (xi == (XImage *)NULL) {
				fprintf(stderr, "Unable to create ximage\n");
				exit(ERROR);
			}
		} else {
			pm = ppm[fnum -f] = XCreatePixmap(display,
					rootw,
					w2, (unsigned int)h,
					(unsigned int)8
				);
			if (pm == 0) {
				fprintf(stderr, "Unable to create Pixmap\n");
				exit(ERROR);
			}
			mem = data;
		}

		if (upsidedown)
			mem += (h - 1) * bytes_per_line;

		y = h - 1;

		if (colormapped(im)) {
			while (readmappedline(im, line)) {
				(*copyfun)(line, mem, (int)w);
				if (upsidedown)
					mem -= bytes_per_line;
				else
					mem += bytes_per_line;
			}
		} else {
			while (readrgbline(im, red, green, blue)) {
				for (x = 0; x < w; x++)
					line[x] = red[x] * 0.3 + green[x] * 0.59 + blue[x] * 0.11;
				(*copyfun)(line, mem, (int)w);
				if (upsidedown)
					mem -= bytes_per_line;
				else
					mem += bytes_per_line;
			}
		}
		closeimage(im);
		if (usepixmaps) {
			XPutImage(display, 
				pm,
				theGC, 
				xi,
				0, 0, 
				0, 0,
				w2, h
			);
		}
	}

	fnum -= f;
	fprintf(stderr, "Finished reading %d files\n", fnum);

	tlen--;
        if (tlen <= 0)
                do_titles = 0;
}
