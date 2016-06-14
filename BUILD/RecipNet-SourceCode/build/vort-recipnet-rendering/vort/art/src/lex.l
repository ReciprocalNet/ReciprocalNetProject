%{
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "art.h"
#include "objs.h"
#include "gram.h"

extern int	linecount;

extern char	currentfile[];

extern symbol	*lookup();

%}

%p 5000
%a 3000
%n 1000
%e 1500

%%

kdtree		return(KDTREE);

grid		return(GRID);

subdivision	return(SUBDIVISION);

maxsublevel	return(MAXSUBLEVEL);

basis		return(BASIS);

metaball	return(METABALL);

threshold	return(THRESHOLD);

gapcolou?r	return(GAPCOLOUR);

gapsize		return(GAPSIZE);

maxtreedepth	return(MAXTREEDEPTH);

screensize	return(SCREENSIZE);

source		return(SOURCE);

amplitude	return(AMPLITUDE);

wavelength	return(WAVELENGTH);

damping		return(DAMPING);

phase		return(PHASE);

alpha		return(ALPHA);

scalefactor	return(SCALEFACTOR);

mixval		return(SCALEFACTOR);

sourceradius	return(SOURCERADIUS);

turbulence	return(TURBULENCE);

squeeze		return(SQUEEZE);

geofile		return(OFFFILE);

offfile		return(OFFFILE);

colou?rfield	return(COLOURFIELD);

heightfield	return(HEIGHTFIELD);

projection	return(PROJECTION);

orthographic	return(ORTHOGRAPHIC);

perspective	return(PERSPECTIVE);

colou?rfile	return(COLOURFILE);

normalfile	return(NORMALFILE);

vnormalfile	return(VNORMALFILE);

vcolou?rfile	return(VCOLOURFILE);

vortfile	return(VORTFILE);

antialiasing	return(ANTIALIASING);

adaptive	return(ADAPTIVE);

brute		return(BRUTE);

top		return(TOP);

base		return(BASE);

constant	return(CONST);

clipvolume	return(CLIPVOLUME);

coefficients	return(COEFFS);

order		return(ORDER);

csg		return(CSG);

composite	return(COMPOSITE);

transform	return(TRANSFORM);

frameno		return(FRAMENO);

phongshading	{
			yylval.y_int = PHONGSHADING;
			return(OPTION);
		}

backfacing	{
			yylval.y_int = BACKFACING;
			return(OPTION);
		}

on		return(ON);

off		return(OFF);

material	return(MATERIAL);

center		return(CENTER);

centre		return(CENTER);

colou?r		return(COLOUR);

ambient		return(AMBIENT);

reflectance	return(REFLECTANCE);

absorption	return(ABSORPTION);

transparency	return(TRANSPARENCY);

hazecolou?r	return(HAZECOLOUR);

fogfactor	return(FOGFACTOR);

rfactor		return(RFACTOR);

falloff		return(FALLOFF);

ri		return(RI);

repeat		return(REPEAT);

radius		return(RADIUS);

radii		return(RADII);

strip		return(STRIP);

direction	return(DIRECTION);

angle		return(ANGLE);

insideangle	return(INSIDEANGLE);

beamdistribution	return(BEAMDISTRIBUTION);

mindist		return(MINDIST);

numrays		return(NUMRAYS);

geomx		return(GEOMX);

geomy		return(GEOMY);

geomz		return(GEOMZ);

vertex		return(VERTEX);

location	return(LOCATION);

texture		return(TEXTURE);

map		return(MAP);

colou?rmap	return(COLOURMAP);

range		return(RANGE);

blend		return(BLEND);

blendcolou?r	return(BLENDCOLOR);

scalefactors	return(SCALEFACTORS);

blocksize	return(BLOCKSIZE);

bricksize	return(BLOCKSIZE);

size		return(SIZE);

equation	return(EQUATION);

translate	return(TRANSLATE);

rotate		return(ROTATE);

scale		return(SCALE);

lookat		return(LOOKAT);

up		return(UP);

fieldofview	return(FIELDOFVIEW);

raysperpixel	return(RAYSPERPIXEL);

pixelgrid	return(PIXELGRID);

shadows		return(SHADOWS);

title		return(TITLE);

background	return(BACKGROUND);

maxhitlevel	return(MAXHITLEVEL);

output		return(OUTPUT);

twentyfivebit	return(TWENTYFIVEBIT);

rgb		{
			yylval.y_int = PIX_RGB;
			return(FILETYPE);
		}

rle		{
			yylval.y_int = PIX_RLE;
			return(FILETYPE);
		}

rgba		{
			yylval.y_int = PIX_RGBA;
			return(FILETYPE);
		}

rlea		{
			yylval.y_int = PIX_RLEA;
			return(FILETYPE);
		}

[a-zA-Z_][/a-zA-Z_.0-9]*	{
			if ((yylval.y_sym = lookup(yytext)) != (symbol *)NULL)
				return(OBJECT_TYPE);
			else {
				yylval.y_str = (char *)smalloc(strlen(yytext) + 1);
				strcpy(yylval.y_str, yytext);

				return(NAME);
			}
		}

[0-9]*	{
			yylval.y_int = atoi(yytext);
			return(INTEGER);
		}

[0-9]*\.[0-9]*	{
			yylval.y_flt = atof(yytext);
			return(FLOAT);
		}

[0-9]*\.[0-9]*[eE][-+]*[0-9]*	{
			yylval.y_flt = atof(yytext);
			return(FLOAT);
		}

"+"		return(PLUS);

"-"		return(MINUS);

"/"		return(DIV);

"*"		return(MULT);

"%"		return(PCENT);

"^"		return(POWER);

","		return(COMMA);

"("		return(LP);

")"		return(RP);

"="		return(EQUALS);

"$$"		return(DOLS);

"\""		{
			char	buf[BUFSIZ], *p;
			
			for (p = buf; (*p = input()) != '"'; p++)
				if (*p == '\n' || *p == EOF)
					yyerror("syntax error");

			*p = 0;

			yylval.y_str = (char *)smalloc(strlen(buf) + 1);
			strcpy(yylval.y_str, buf);

			return(NAME);
		}

"{"		{
			return(LBRACE);
		}

"}"		{
			return(RBRACE);
		}

"'"		return(QUOTE);

"\n"		{
			linecount++;
		}

[ \t]		{
			;
		}

"#"		{
			int	c, i, j;
			char	buf[BUFSIZ], *p;

			i = 0;
			while ((c = input()) >= 0 && c != '\n')
				buf[i++] = (char)c;
			buf[i] = 0;

			if (sscanf(buf, "%d", &linecount) != 1)
				linecount++;
			else {
				for (p = buf; p != &buf[i]; p++)
					if (*p == '"')
						break;

				if (p == &buf[i])
					linecount++;
				else {
					j = 0;
					p++;
					while (*p != '"' && p != &buf[i])
						currentfile[j++] = *p++;
					currentfile[j] = 0;
				}
			}
		}

"/*"		{
			int	c1, c2;
			int	comline, incomment = 1;
			char	buf[BUFSIZ];

			comline = linecount;

			do {
				while ((c1 = input()) != '*' && c1 != '/' && c1 != EOF)
					if (c1 == '\n')
						linecount++;

				c2 = input();
				if (c2 == '\n')
					linecount++;

				if (c1 == '*' && c2 == '/')
					incomment--;

				if (c1 == '/' && c2 == '*') {
					incomment++;
					comline = linecount;
				}

			} while (incomment && c2 != EOF);

			if (c1 == EOF || c2 == EOF) {
				sprintf(buf, "art: unterminated comment - started line %d.\n", comline);
				fatal(buf);
			}
		}
