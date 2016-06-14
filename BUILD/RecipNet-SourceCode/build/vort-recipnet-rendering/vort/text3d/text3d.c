#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "text3d.h"

int	linecount = 1;
int	verbose = 1;

static	float	xc = -100.0e24;
static	float	yc = -100.0e24;
static	float	zc = -100.0e24;
static	float	rc = -100.0e24;

attribs	attr;

#define NOT_THE_SAME_AS_THE_LAST (\
(ABS(xc - xp) > 1.0e-3) || \
(ABS(yc - yp) > 1.0e-3) || \
(ABS(zc - zp) > 1.0e-3) || \
(ABS(rc - attr.radius) > 1.0e-3))


#define	XCOORD(x)	((x) - 'R')
#define	YCOORD(y)	('R' - (y))

static	float	xcurrent = 0.0, ycurrent = 0.0, zcurrent = 0.0;
static	float	SCSIZEX = 1.0, SCSIZEY = 1.0;
static	int	Loaded = 0;
static	short	nchars;


static	char	errmsg1[120] = "font: unable to open ";

static	struct	{
	char	*p;	/* All the vectors in the font */
	char	**ind;	/* Pointers to where the chars start in p */
	int	as;	/* Max ascender of a character in this font */
	int	dec;	/* Max decender of a character in this font */
	int	mw;	/* Max width of a character in this font */
} ftab;

float	strlength();
void	bdraw();
void	sdraw();
float	getfontheight();

extern char	*getenv();
static int hershfont(char *fontname);

/*
 * font
 * 	loads in a font.
 */
void
font(name)
	char	*name;
{
	if (!hershfont(name)) {
		strcat(errmsg1, "fontfile ");
		strcat(errmsg1, name);
		yyerror(errmsg1);
	}
}

/*
 * numchars
 *
 *	return the number of characters in the currently loaded hershey font.
 *	(The 128 is the number of chars in a hardware font)
 */
int
numchars()
{
	return((int)nchars);
}

/*
 * hershfont
 *
 * Load in a hershey font. First try the font library, if that fails try
 * the current directory, otherwise return 0.
 */
static int
hershfont(fontname)
	char	*fontname;
{
	int	fd, i, j;
	short	nvects, n;
	char	path[120], *flib;
	
	if ((flib = getenv("VFONTLIB")) == (char *)NULL) {
		strcpy(path, FONTLIB);
		strcat(path, fontname);
	} else {
		strcpy(path, flib);
#ifdef PC
		strcat(path, "\\");
#else
		strcat(path, "/");
#endif
		strcat(path, fontname);
	}

#ifdef PC
	if ((fd = open(path, O_RDONLY | O_BINARY)) < 0) 
		if ((fd = open(fontname, O_RDONLY | O_BINARY)) < 0) 
#else
	if ((fd = open(path, O_RDONLY)) < 0) 
		if ((fd = open(fontname, O_RDONLY)) < 0) 
#endif
			return (0);

	/*
	 * In case you're wondering, the reason I don't just read in
	 * a structure here is that some machines do this funny
	 * alignment thing and that stuffs up the structures sometimes.
	 */
	if (read(fd, &nchars, sizeof(nchars)) != sizeof(nchars))
		return (0);

	if (read(fd, &nvects, sizeof(nvects)) != sizeof(nvects))
		return(0);

	if (read(fd, &n, sizeof(n)) != sizeof(n))
		return(0);

	ftab.as = (int)n;

	if (read(fd, &n, sizeof(n)) != sizeof(n))
		return(0);

	ftab.dec = (int)n;

	if (read(fd, &n, sizeof(n)) != sizeof(n))
		return(0);

	ftab.mw = (int)n;

	/*
	 *  Allocate space for it all....
	 */
	if (Loaded) {
		if (ftab.ind[0])
			free(ftab.ind[0]);
		if (ftab.ind)
			free(ftab.ind);
		Loaded = 0;
	}

	ftab.ind = (char **)malloc(sizeof(char *)*(nchars + 1));

	ftab.p = (char *)malloc((unsigned)(2 * nvects));

	/*
	 *  As we read in each character, figure out what ind should be
	 */

	for (i = 0; i < nchars; i++) {
		if ((j = read(fd, &n , sizeof(n))) != sizeof(n))
			return(0);

		if ((j = read(fd, ftab.p, (unsigned)n)) != (unsigned)n)
			return(0);

		ftab.ind[i] = ftab.p;
		ftab.p += n;
	}
	ftab.ind[nchars] = ftab.p;	/* To Terminate the last one */

	close(fd);
	Loaded = 1;
	return(1);
}

/*
 * drawchar
 *
 * Display a character from the currently loaded font.
 */
void
drawchar(c)
	int	c;
{
	char	*p, *e;
	int	Move, i, x, y, xt, yt;
	float	xp, yp, tmp, xsave, ysave;
	float	tcos, tsin;

	if (!Loaded)
		yyerror("drawchar: no font loaded");

	if (c < 32 || c > 127) 
		printf("composite { /* %d */\n", c);
	else
		printf("composite { /* %c */\n", c);

	if (attr.scal_set)
		printf("\tscale (%f, %f, %f)\n", attr.scal.x, attr.scal.y, attr.scal.z);

	if (attr.rot_set)
		printf("\trotate (%f, %c)\n", attr.rot.ang, attr.rot.ax);

	if (attr.mat_set)
		printf("\tmaterial %.5f, %.5f, %.5f, %d\n", attr.mat.ri, attr.mat.kd, attr.mat.ks, attr.mat.ksexp);

	if (attr.amb_set)
		printf("\tambient %g, %g, %g\n", attr.amb.r, attr.amb.g, attr.amb.b);

	if (attr.col_set)
		printf("\tcolor %g, %g, %g\n", attr.col.r, attr.col.g, attr.col.b);

	if (attr.ref_set)
		printf("\treflectance %.5f\n", attr.refl);

	if (attr.tran_set)
		printf("\ttransparency %.5f\n", attr.trans);

	tcos = attr.textcos;
	tsin = attr.textsin;

	if ((i = c - 32) < 0)
		i = 0;
	if (i >= nchars)
		i = nchars - 1;

	xsave = xcurrent;
	ysave = ycurrent;

	Move = 1;
	xt = attr.centered ? 0 : (attr.fixedwidth ? -ftab.mw / 2 : XCOORD(ftab.ind[i][0]));
	yt = attr.centered ? 0 : ftab.dec;

	e = ftab.ind[i+1];
	p = ftab.ind[i] + 2;
	while(p < e) {
		x = XCOORD((int)(*p++));
		y = YCOORD((int)(*p++));
		if (x != -50) {			/* means move */
			xp = (float)(x - xt)*SCSIZEX;
			yp = (float)(y - yt)*SCSIZEY;
			tmp = xp;
			xp = tcos*tmp - tsin*yp + xsave;
			yp = tsin*tmp + tcos*yp + ysave;
			if (Move) {
				Move = 0;
				move(xp, yp, zcurrent);
			} else  {
			        (*attr.drawfun)(xp, yp, zcurrent);
			}
		} else
			Move = 1;
	}
	/*
	 * Move to right hand of character.
	 */
	
	tmp = attr.fixedwidth ? (float)ftab.mw : (float)(ftab.ind[i][1] - ftab.ind[i][0]);
	tmp *= SCSIZEX;
	xsave += tcos*tmp;
	ysave += tsin*tmp;
	move(xsave, ysave, zcurrent);

	printf("\t}\n");
}

/*
 * textsize
 *
 * set software character scaling values 
 *
 * Note: Only changes software char size. Should be called
 * after a font has been loaded.
 *
 */
void
textsize(width, height)
	float	width, height;
{
	float	a;

	if (!Loaded)
		yyerror("textsize: no font loaded");

	a = (float)MAX(ftab.mw, (ftab.as - ftab.dec));
	SCSIZEX = width / a;
	SCSIZEY = height / a;
}

/* 
 * getfontheight
 *
 * Return the maximum Height of the current font
 */
float 
getfontheight()
{
	return((float)(SCSIZEY * MAX(ftab.mw, (ftab.as - ftab.dec))));
}

/*
 * drawhstr
 *
 * Display the text string using the currently loaded Hershey font
 */
static void
drawhstr(string)
	char	*string;
{
	char	c;

	int oldCentered = attr.centered;
	attr.centered = 0;

	/*
	 * Now display each character
	 *
	 */
	while (c = *string++)
		drawchar(c);
	
	attr.centered = oldCentered;
}

/*
 * drawstr
 *
 * Draw a string from the current pen position.
 *
 */
void
drawstr(string)
	char 	*string;
{
	float	width, height, cx, cy;

	if (attr.centered) {
		height = getfontheight() / 2.0;
		width = strlength(string) / 2.0;

		cx = xcurrent + height * attr.textsin - width * attr.textcos;
		cy = ycurrent - height * attr.textcos - width * attr.textsin;
		move(cx, cy, zcurrent);
	}

	printf("/* %s */\n", string);
	drawhstr(string);
}

/*
 * istrlength
 *
 * Find out the length of a string in raw "Hershey coordinates".
 */
static	int
istrlength(s)
	char	*s;
{
	char	c;
	int	i, len = 0;
	
	if (attr.fixedwidth) 
		return((float)(strlen(s) * ftab.mw));
	else {
		while (c = *s++) {
			if ((i = (int)c - 32) < 0 || i >= nchars)
				i = nchars - 1;

			len += (ftab.ind[i][1] - ftab.ind[i][0]);
		}
		return (len);
	}
}

/*
 * strlength
 *
 * Find out the length (in world coords) of a string.
 *
 */
float
strlength(s)
	char	*s;
{
	return((float)(istrlength(s) * SCSIZEX));
}

/*
 * boxtext
 *
 * Draw text so it fits in a "box" - note only works with hershey text
 */
void
boxtext(x, y, l, h, s)
	float	x, y, l, h;
	char	*s;
{
	float	oscsizex, oscsizey;

	oscsizex = SCSIZEX;
	oscsizey = SCSIZEY;
	/*
	 * set width so string length is the same a "l" 
	 */
	SCSIZEX = l / (float)istrlength(s);

	/* 
	 * set character height so it's the same as "h" 
	 */
	SCSIZEY = h / (float)(ftab.as - ftab.dec);
	move(x, y, zcurrent);
	drawstr(s);

	SCSIZEX = oscsizex;
	SCSIZEY = oscsizey;
}

/*
 * boxfit
 *
 * Set up the scales etc for text so that a string of "nchars" characters
 * of the maximum width in the font fits in a box.
 */
void
boxfit(l, h, nchars)
	float	l, h;
	int	nchars;
{
	SCSIZEX = l / (float)(nchars * ftab.mw);
	SCSIZEY = h / (float)(ftab.as - ftab.dec);
}

FILE	*fp;

main(argc, argv)
	int	argc;
	char	**argv;
{
	if (argc == 2) {
#ifdef PC
               if ((fp = freopen(argv[1], "rt", stdin)) == NULL) {
#else
               if ((fp = freopen(argv[1], "r", stdin)) == NULL) {
#endif
			fprintf(stderr, "Can't open: %s\n", argv[1]);
			exit(1);
		}
	} else {
		fprintf(stderr, "usage: %s infile\n", argv[0]);
		exit(1);
	}

	attr.drawfun = bdraw;
	attr.textcos = 1.0;
	attr.textsin = 0.0;

	yyparse();

	exit(0);
}

rmove(xr, yr, zr)
	float	xr, yr, zr;
{
	move(xcurrent + xr, ycurrent + yr, zcurrent +zr);
}

move(xp, yp, zp)
	float	xp, yp, zp;
{	
	xcurrent = (fabs(xp) < 1.0e-6 ? 0.0 : xp);
	ycurrent = (fabs(yp) < 1.0e-6 ? 0.0 : yp);
	zcurrent = (fabs(zp) < 1.0e-6 ? 0.0 : zp);

	/* Force sphere or box output */
	xc = yc = zc = rc = 1.0e38;
}

void
bdraw(xp, yp, zp)
	float	xp, yp, zp;
{
	float	d, t;
	xp = (fabs(xp) < 1.0e-6 ? 0.0 : xp);
	yp = (fabs(yp) < 1.0e-6 ? 0.0 : yp);
	zp = (fabs(zp) < 1.0e-6 ? 0.0 : zp);

	if (NOT_THE_SAME_AS_THE_LAST) {
		printf("\tcylinder {\n");
		printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xcurrent, ycurrent, zcurrent);
		printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xcurrent, ycurrent, zcurrent + 1.0);
		printf("\t\tradius %.5f\n", attr.radius);
		printf("\t}\n\n");

		xc = xp;
		yc = yp;
		zc = zp;
		rc = attr.radius;
	}

	d = 0.5 * sqrt(SQR(xp - xcurrent) + SQR(yp - ycurrent));
	t = atan2((double)(yp - ycurrent), (double)(xp - xcurrent));
	t = 90 + t * 180.0 / M_PI;

	printf("\tbox {\n");
	printf("\t\tscale(%f, %f, 1)\n", attr.radius, d);
	printf("\t\trotate(%f, z)\n", t);
	printf("\t\ttranslate(%.5f, %.5f, %.5f)\n", 0.5 * (xcurrent + xp) , 0.5 * (ycurrent + yp), zcurrent);
	printf("\t\tvertex(-1, -1, 0)\n");
	printf("\t\tvertex( 1,  1, 1)\n");
	printf("\t}\n\n");


	printf("\tcylinder {\n");
	printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xp, yp, zp);
	printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xp, yp, zp + 1.0);
	printf("\t\tradius %.5f\n", attr.radius);
	printf("\t}\n\n");

	xc = xp;
	yc = yp;
	zc = zp;
	rc = attr.radius;

	xcurrent = xp;
	ycurrent = yp;
	zcurrent = zp;
}

void
sdraw(xp, yp, zp)
	float	xp, yp, zp;
{
	xp = (fabs(xp) < 1.0e-6 ? 0.0 : xp);
	yp = (fabs(yp) < 1.0e-6 ? 0.0 : yp);
	zp = (fabs(zp) < 1.0e-6 ? 0.0 : zp);

	if (NOT_THE_SAME_AS_THE_LAST) {
		printf("\tsphere {\n");
		printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xcurrent, ycurrent, zcurrent);
		printf("\t\tradius %.5f\n", attr.radius);
		printf("\t}\n\n");

		xc = xp;
		yc = yp;
		zc = zp;
		rc = attr.radius;
	}

	printf("\tcylinder {\n");
	printf("\t\tcenter(%.5f, %.5f, %.5f)\n", xcurrent, ycurrent, zcurrent);
	printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xp, yp, zp);
	printf("\t\tradius %.5f\n", attr.radius);
	printf("\t}\n\n");

	printf("\t\tsphere {\n");
	printf("\t\tcenter (%.5f, %.5f, %.5f)\n", xp, yp, zp);
	printf("\t\tradius %.5f\n", attr.radius);
	printf("\t}\n\n");


	xc = xp;
	yc = yp;
	zc = zp;
	rc = attr.radius;

	xcurrent = xp;
	ycurrent = yp;
	zcurrent = zp;
}

yyerror(s)
	char	*s;
{
	fprintf(stderr, "%s in line %d\n", s, linecount);
	exit(1);
}

#ifdef PC
yywrap()
{
	return(1);
}
#endif
