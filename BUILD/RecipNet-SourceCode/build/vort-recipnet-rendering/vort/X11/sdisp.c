#include <stdio.h>
#include <signal.h>
#include <math.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>

#include <string.h>

#include "vort.h"
#include "status.h"

#define	CMAPSIZE	256
#define EV_MASK         KeyPressMask|ButtonReleaseMask|ExposureMask|ButtonPressMask

static char     title[BUFSIZ], *myname;
static Display *display;
static Visual  *visual;
static int      theScreen, depth;
static Colormap cmap;
static Window   winder, rootw;
static GC       theGC;
static Pixmap   pm;
static XImage  *ximage;
static unsigned char carray[CMAPSIZE];
static unsigned int w, h;
static int      forcecmap;
static int      verbose;
static int      truecolor;
static int      half = 0;

static int      (*copyfun) ();

typedef struct l {
	char           *fname;
	int             sleep_time;
	int             use_cmap;
	struct l       *nxt;
}               List;

/*
 * xbcopy
 * 
 * copy bytes from from to to.
 */
xbcopy(from, to, size)
	unsigned char  *from, *to;
	int             size;
{
	while (size--)
		*to++ = carray[*from++];

}

char            buf[BUFSIZ], blark[BUFSIZ];
static char    *kp, *cp;

char           *
getword(buf)
	char            buf[];
{
	int             i = 0;

	for (; *kp == ' ' || *kp == '\t'; kp++);

	for (; *kp != ' ' && *kp != '\n' && *kp != '\t'; kp++)
		blark[i++] = *kp;

	blark[i] = 0;

	return (&blark[0]);
}

#ifdef SYSV
/*
 * bcopy
 * 
 * copy bytes from from to to.
 */
bcopy(from, to, size)
	unsigned char  *from, *to;
	int             size;
{
	while (size--)
		*to++ = *from++;

}

int
usleep(usec)
	unsigned long   usec;
{
	poll((struct pollfd *) 0, (size_t) 0, usec / 1000);
	return 0;
}

#else
extern int      bcopy();
#endif

int             timedout;

void
catchtimeout(sig)
	int             sig;
{
	timedout = 1;
}

/*
 * program to read in a color-mapped image and display it on X11, greyscale
 * is used if the image is rgb.
 */
main(ac, av)
	int             ac;
	char          **av;
{
	char           *p, *pp, *mem, *data;
	register int    i, j, ox;
	unsigned int    w2, bytes_per_line;
	int             x, y, val, upsidedown, installmap, wreal, mag = 1;
	long            delay, idelay, timeout;
	unsigned char   v, *red, *green, *blue, *line, *l;
	image          *im;
	XEvent          event;
	XVisualInfo     visualinfo;
	char            c, *geomstring, *displaystring, *infile, *name;
	float           fmag = 1.0;
	List           *list, *prev, *next, *fl;
	FILE           *fp;
	int             sleep_time, old_w = -1, old_h = -1;

	if ((myname = strrchr(av[0], '/')) == NULL)
		myname = av[0];
	else
		*myname++;

	if (ac < 2) {
		fprintf(stderr, "%s: usage %s -f scriptfile [-display displayname] [-geom Xgeometry]\n", myname, myname);
		fprintf(stderr, "	-f ... specify the script file\n");
		fprintf(stderr, "	-display means specify X display.\n");
		fprintf(stderr, "	-geom means specify X geometry.\n");
		exit(ERROR);
	}
	infile = geomstring = displaystring = (char *) NULL;
	;
	installmap = idelay = verbose = forcecmap = upsidedown = 0;
	for (i = 1; i < ac && *av[i] == '-'; i++) {
		if (strncmp(av[i], "-m", 2) == 0) {	/* magnify */
			if (sscanf(av[i], "-m%f", &fmag) != 1) {
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
		} else if (strncmp(av[i], "-u", 2) == 0) {	/* show image upside
								 * down */
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
		} else if (strcmp(av[i], "-c") == 0) {
			forcecmap = 1;
			if (verbose)
				fprintf(stderr, "Force new colourmap.\n");
		} else if (strcmp(av[i], "-v") == 0) {
			verbose = 1;
		} else if (strcmp(av[i], "-f") == 0) {
			infile = av[++i];
		} else if (strcmp(av[i], "-i") == 0) {
			installmap = 1;
		} else if (strncmp(av[i], "-s", 2) == 0) {
			sscanf(av[i], "-s%d", &idelay);
		} else {
			fprintf(stderr, "Unknown option: '%s'.\n", av[i]);
		}
	}

	imagebufsize(1024 * 100);

	list = (List *) NULL;

	if (!infile) {
		fprintf(stderr, "no input script file....");
		exit(1);
	}
	if ((fp = fopen(infile, "r")) == NULL) {
		fprintf(stderr, "Can't open script file '%s'.\n", infile);
		exit(0);
	}
	while (fgets(buf, BUFSIZ, fp)) {
		sleep_time = 0;
		kp = buf;

		fl = (List *) malloc(sizeof(List));
		name = getword(buf);
		fl->fname = (char *) malloc(strlen(name) + 1);
		strcpy(fl->fname, name);

		name = getword(buf);
		if (name) {
			if (!strncmp(name, "pause", 5))
				sleep_time = -1;
			else if (sscanf(name, "%d", &sleep_time) != 1) {
				fprintf(stderr, "Bad line in file: %s %s\n", name, buf);
				exit(1);
			}
		}
		fl->sleep_time = sleep_time;

		name = getword(buf);

		if (name && !strcmp(name, "usecmap"))
			fl->use_cmap = 1;
		else
			fl->use_cmap = forcecmap;

		fl->nxt = list;
		list = fl;
	}

	/*
	 * Reverse the list...
	 */

	prev = next = (List *) NULL;

	for (fl = list; fl; fl = next) {
		next = fl->nxt;
		fl->nxt = prev;
		prev = fl;
	}
	list = prev;

	imagebufsize(5 * 4096);

	mem = data = NULL;
	ximage = NULL;
	cmap = NULL;

	while (1) {
		for (fl = list; fl != (List *) NULL; fl = fl->nxt) {
			if ((im = openimage(fl->fname, "r")) == (image *) NULL) {
				fprintf(stderr, "%s: can't open file '%s'.\n", myname, fl->fname);
				exit(ERROR);
			}
			signal(SIGALRM, catchtimeout);
			strcpy(title, fl->fname);
			/* n = strlen(name) + titlelength(im); */
			if (titlelength(im) != 0) {
				strcat(title, ": ");
				strcat(title, imagetitle(im));
			}
			if (fl->sleep_time < 0) {
				strcat(title, " (PAUSED)");
			}
			if (!display) {
				if (!displaystring)
					displaystring = XDisplayName((char *) NULL);

				if ((display = XOpenDisplay(displaystring)) == NULL) {
					fprintf(stderr, "Can't open display %s.\n", displaystring);
					exit(ERROR);
				}
				/*
				 * See if there is anything in .Xdefaults for
				 * us...
				 */
				if (!geomstring)
					geomstring = XGetDefault(display, "vort", "Geometry");

				if (geomstring && verbose)
					fprintf(stderr, "X geometry (from .Xdefaults): %s.\n", geomstring);

				theScreen = DefaultScreen(display);
				if ((depth = XDisplayPlanes(display, theScreen)) < 8) {
					fprintf(stderr, "You need a 256 (or more) colour display device\n");
					exit(1);
				}
				/*
				 * Try for a TrueColor visual if we are 24
				 * bits.
				 */
				truecolor = 1;
				if (depth >= 24) {
					int             tv, pv;
					tv = XMatchVisualInfo(display, theScreen, depth, TrueColor, &visualinfo);
					if (!tv) {
						pv = XMatchVisualInfo(display, theScreen, depth, PseudoColor, &visualinfo);
						if (!pv) {
							fprintf(stderr, "Need a TrueColor or PseudoColor visual type.\n");
							exit(ERROR);
						}
					}
					truecolor = 3;
					if (verbose)
						fprintf(stderr, "Using %d bit %s visual type.\n", depth, tv ? "TrueColor" : "PseudoColor");
				} else {
					int             i;

					i = XMatchVisualInfo(display, theScreen, depth, PseudoColor, &visualinfo);
					if (!i) {
						fprintf(stderr, "Need a PseudoColor visual type.\n");
						exit(ERROR);
					}
					if (verbose)
						fprintf(stderr, "Using %d bit PseudoColor visual type.\n", depth);
				}

				visual = visualinfo.visual;

				rootw = RootWindow(display, theScreen);
				theGC = DefaultGC(display, theScreen);
			}

			wreal = imagewidth(im);
			w = wreal * fmag;
			h = fmag * imageheight(im);

			if (w != old_w || h != old_h) {

				old_w = w;
				old_h = h;


				if (ximage) {
					XDestroyImage(ximage);
					XUnmapWindow(display, winder);
					if (truecolor == 1) {
						XFreeColormap(display, cmap);
						cmap = NULL;
					}

					if (!colormapped(im) || truecolor == 3) {
						free(red);
						free(green);
						free(blue);
					} else
						free(line);
				}

				if (!colormapped(im) || truecolor == 3) {
					red = (u_char *) malloc(w);
					green = (u_char *) malloc(w);
					blue = (u_char *) malloc(w);
				} else
					line = (u_char *) malloc(w);

				if (truecolor == 1) {
					forcecmap = 1;
					setcolormap(im);
				}
				makewindow(geomstring);
				XMapWindow(display, winder);
				/*
				 * Wait for Exposure event.
				 */
				do {
					XNextEvent(display, &event);
				} while (event.type != Expose);

				if (truecolor == 3)
					bytes_per_line = w * 4;
				else
					bytes_per_line = w;

				/*
				 * Get mem for data in image
				 */
				if ((mem = data = (char *) malloc((unsigned) (h * bytes_per_line))) == NULL) {
					fprintf(stderr, "mem = NULL\n");
					exit(ERROR);
				}

				ximage = XCreateImage(display,
						      visual,
						      depth,
						      ZPixmap,
						      0,
						      data,
						      w, (unsigned int) h,
						      32, bytes_per_line
					);

				if (ximage == (XImage *) NULL) {
					fprintf(stderr, "Unable to create ximage\n");
					exit(ERROR);
				}

			} else {
				XStoreName(display, winder, title);

				if (truecolor == 1) {
					forcecmap = 1;
					setcolormap(im);
				}
			}

			y = h - 1;
			x = 0;

			p = mem;

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
							(*copyfun) (line, p, w);
							p += bytes_per_line;
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

			if (installmap) {	/* When not under a window
						 * manager */
				XInstallColormap(display, cmap);
			}


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


			/*
			 * XSetInputFocus(display, winder, RevertToParent,
			 * CurrentTime);
			 */
			XFlush(display);

			timedout = 0;
			alarm(0);
			if (fl->sleep_time > 0)
				alarm(fl->sleep_time);

			if (fl->sleep_time != 0) {
				do {
					XCheckWindowEvent(display, winder, Expose | KeyPressMask | ButtonPressMask, &event);
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
				} while (!timedout && event.type != KeyPress && event.type != ButtonPress);
				if (!timedout) {
					if (XLookupString((XKeyEvent *) & event, &c, 1, NULL, NULL) > 0)
						if (c == 'p' || c == 's') {
							alarm(0);
							do {
								XNextEvent(display, &event);
							} while (event.type != KeyPress && event.type != ButtonPress);
						}
					if (XLookupString((XKeyEvent *) & event, &c, 1, NULL, NULL) > 0)
						if (c == 'q' || c == 27)
							exit(0);
				}
			}
			alarm(0);
		}
	}

	XCloseDisplay(display);

	if (c == 'q' || c == 27)
		exit(2);

	exit(0);
}


int
trydefaultcmap(im)
	image          *im;
{
	int             i, stat, num;
	XColor          tmp;

	/*
	 * First try to Allocate out of the default map...
	 */
	if (!cmap)
		cmap = DefaultColormap(display, theScreen);

	num = 0;
	if (colormapped(im)) {
		for (i = 0; i < cmapsize(im); i++) {
#ifdef DEBUG
			fprintf(stderr, "Trying %d  ", i);
#endif
			tmp.pixel = (unsigned long) i;
			tmp.red = (unsigned short) (im->red[i] << 8);
			tmp.green = (unsigned short) (im->green[i] << 8);
			tmp.blue = (unsigned short) (im->blue[i] << 8);
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
				cmap = NULL;
				return (0);
			}
#ifdef DEBUG
			fprintf(stderr, "Got %d\n", tmp.pixel);
#endif
			carray[i] = tmp.pixel;
			num++;

		}
	} else {
		for (i = 0; i < CMAPSIZE; i++) {
			tmp.pixel = (unsigned long) i;
			tmp.red = tmp.green = tmp.blue = (unsigned short) (i << 8);
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
				cmap = NULL;
				return (0);
			}
			carray[i] = tmp.pixel;
			num++;
		}
	}

	if (num > 255) {
		cmap = NULL;
		return (0);
	}

	if (verbose)
		fprintf(stderr, "%d colours allocated from default colourmap.\n", num);

	copyfun = xbcopy;

	return (1);

}

useourowncmap(im)
	image          *im;
{
	int             i, min, max, val;
	unsigned int    size;
	XColor         *cols;


	if (!cmap)
		cmap = XCreateColormap(display, rootw, visual, AllocAll);

	if (colormapped(im)) {
		size = cmapsize(im);
		if ((cols = (XColor *) malloc(size * sizeof(XColor))) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(ERROR);
		}
		for (i = 0; i < cmapsize(im); i++) {
			cols[i].pixel = (unsigned long) i;
			cols[i].red = (unsigned short) (im->red[i] << 8);
			cols[i].green = (unsigned short) (im->green[i] << 8);
			cols[i].blue = (unsigned short) (im->blue[i] << 8);
			cols[i].flags = DoRed | DoGreen | DoBlue;
		}
	} else {
		size = CMAPSIZE;
		if ((cols = (XColor *) malloc(size * sizeof(XColor))) == NULL) {
			fprintf(stderr, "Out of mem.\n");
			exit(ERROR);
		}
		for (i = 0; i < size; i++) {
			cols[i].pixel = (unsigned long) i;
			cols[i].red = cols[i].green = cols[i].blue = (unsigned short) (i << 8);
			cols[i].flags = DoRed | DoGreen | DoBlue;
		}
	}

	copyfun = bcopy;
	XStoreColors(display, cmap, cols, size);
	free(cols);
}

setcolormap(im)
	image          *im;
{
	if (forcecmap)
		useourowncmap(im);
	else if (!trydefaultcmap(im))
		useourowncmap(im);
}

makewindow(geomstring)
	char           *geomstring;
{
	unsigned int    wattrmask;
	XSetWindowAttributes wattr;
	XSizeHints      sizehints;
	XWMHints        wmhints;
	unsigned int    bw, mask, wid, hi, xpos, ypos;

	bw = 3;
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
			sizehints.x = DisplayWidth(display, theScreen) - 2 * bw - sizehints.width + xpos;

		if (mask & YNegative)
			sizehints.y = DisplayHeight(display, theScreen) - 2 * bw - sizehints.height + ypos;

	} else
		sizehints.flags = PPosition | PSize;

	if (sizehints.width < w)
		fprintf(stderr, "Specified window width (%d) is less than image width (%d).\n", sizehints.width, w);

	if (sizehints.height < h)
		fprintf(stderr, "Specified window height (%d) is less than image height (%d).\n", sizehints.height, h);


	wattr.background_pixel = BlackPixel(display, theScreen);
	wattr.border_pixel = WhitePixel(display, theScreen);
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

	if (truecolor == 1)
		XSetWindowColormap(display, winder, cmap);

	XSetStandardProperties(display,
			       winder,
			       title,
			       myname,
			       None,
			       (char **) NULL, 0,
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
