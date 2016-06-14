
%{
#include <stdio.h>
#include <math.h>
#include "text3d.h"

extern	void	bdraw();
extern	void	sdraw();

extern	attribs	attr;

extern	int	verbose;

%}

%union{
	float		y_flt;
	int		y_int;
	char		*y_str;
};

%token <y_flt>	FLOAT
%token <y_int>	INTEGER OPTION
%token <y_str>	STRING

%token DRAWSTR TEXTSIZE COLOUR AMBIENT REFLECTANCE TRANSPARENCY
%token MATERIAL TEXTRADIUS FIXEDWIDTH CENTERTEXT TEXTANG FONT CHAR
%token DRAWCHAR BOXFIT BOXTEXT MOVE RMOVE SCALE TRANSLATE ROTATE OUTPUT 
%token BOX_CYL CYL_SPH
%token LP RP LBRACE RBRACE COMMA ON OFF TRUE FALSE OPTION PCENT

%type  <y_flt>  expr

%left  PLUS MINUS
%left  MULT DIV
%left  POWER
%left  UMINUS

%right  EQUALS

%%

input	: /* NULL */
	| input item
	;

item	: DRAWSTR LP STRING RP
	  {
		drawstr($3);
		if (verbose)
			fprintf(stderr, "drawstr(\"%s\")\n", $3);
	  }
	| OUTPUT LP BOX_CYL RP
	  {
		attr.drawfun = bdraw;
		if (verbose)
			fprintf(stderr, "output(box_cyl)\n");
	  }
	| OUTPUT LP CYL_SPH RP
	  {
		attr.drawfun = sdraw;
		if (verbose)
			fprintf(stderr, "output(cyl_sph)\n");
	  }
	| TEXTSIZE LP expr COMMA expr RP
	  {
		textsize($3, $5);
		if (verbose)
			fprintf(stderr, "textsize(%g, %g)\n", $3, $5);
	  }
	| COLOUR LP expr COMMA expr COMMA expr RP
	  {
		attr.col_set = 1;
		attr.col.r = $3;
		attr.col.g = $5;
		attr.col.b = $7;
		if (verbose)
			fprintf(stderr, "colour(%g, %g, %g)\n", $3, $5, $7);
	  }
	| AMBIENT LP expr COMMA expr COMMA expr RP
	  {
		attr.amb_set = 1;
		attr.amb.r = $3;
		attr.amb.g = $5;
		attr.amb.b = $7;
		if (verbose)
			fprintf(stderr, "ambient(%g, %g, %g)\n", $3, $5, $7);
	  }
	| REFLECTANCE LP expr RP
	  {
		attr.ref_set = 1;
		attr.refl = $3;
		if (verbose)
			fprintf(stderr, "reflectance(%g)\n", $3);
	  }
	| TRANSPARENCY LP expr RP
	  {
		attr.trans_set = 1;
		attr.trans = $3;
		if (verbose)
			fprintf(stderr, "transparency(%g)\n", $3);
	  }
	| MATERIAL LP expr COMMA expr COMMA expr COMMA expr RP
	  {
		attr.mat_set = 1;
		attr.mat.ri = $3;
		attr.mat.kd = $5;
		attr.mat.ks = $7;
		attr.mat.ksexp = $9;
		if (verbose)
			fprintf(stderr, "material(%g, %g, %g, %g)\n", $3, $5, $7, $9);
	  }
	|  TEXTRADIUS LP expr RP
	  {
		attr.radius = $3;
		if (verbose)
			fprintf(stderr, "textradius(%g)\n", $3);
	  }
	|  CENTERTEXT LP TRUE RP
	  {
		attr.centered = 1;
		if (verbose)
			fprintf(stderr, "centretext(true)\n");
	  }
	|  CENTERTEXT LP FALSE RP
	  {
		attr.centered = 0;
		if (verbose)
			fprintf(stderr, "centretext(false)\n");
	  }
	|  FIXEDWIDTH LP TRUE RP
	  {
		attr.fixedwidth = 1;
		if (verbose)
			fprintf(stderr, "fixedwidth(true)\n");
	  }
	|  FIXEDWIDTH LP FALSE RP
	  {
		attr.fixedwidth = 0;
		if (verbose)
			fprintf(stderr, "fixedwidth(false)\n");
	  }
	|  TEXTANG LP expr RP
	  {
		double	ang = $3;
	
		attr.textcos = cos(ang * D2R);
		attr.textsin = sin(ang * D2R);
		if (verbose)
			fprintf(stderr, "textang(%g)\n", $3);
	  }
	|  FONT LP STRING RP
	  {
		font($3);
		if (verbose)
			fprintf(stderr, "font(\"%s\")\n", $3);
	  }
	|  DRAWCHAR LP expr RP
	  {
		int	ch = (int)$3;

		drawchar(ch);

		if (verbose)
			if (ch < 32 || ch > 127)
				fprintf(stderr, "drawchar(%d)\n", ch);
			else
				fprintf(stderr, "drawchar('%c')\n", ch);

	  }
	|  BOXFIT LP expr COMMA expr COMMA expr RP
	  {
		int	n = (int)$7;

		boxfit($3, $5, $7);
		if (verbose)
			fprintf(stderr, "boxfit(%g, %g, %d)\n", $3, $5, n);
	  }
	|  BOXTEXT LP expr COMMA expr COMMA expr COMMA expr COMMA STRING RP
	  {
		boxtext($3, $5, $7, $9, $11);
		if (verbose)
			fprintf(stderr, "boxtext(%g, %g, %g, %g, %s)\n", $3, $5, $7, $9, $11);
	  }
	|  MOVE LP expr COMMA expr COMMA expr RP
	  {
		move($3, $5, $7);
		if (verbose)
			fprintf(stderr, "move(%g, %g, %g)\n", $3, $5, $7);
	  }
	|  RMOVE LP expr COMMA expr COMMA expr RP
	  {
		rmove($3, $5, $7);
		if (verbose)
			fprintf(stderr, "rmove(%g, %g, %g)\n", $3, $5, $7);
	  }
	|  SCALE LP expr COMMA expr COMMA expr RP
	  {
		attr.scal_set = 1;
		attr.scal.x = $3;
		attr.scal.y = $5;
		attr.scal.z = $7;
		if (verbose)
			fprintf(stderr, "scale(%g, %g, %g)\n", $3, $5, $7);
	  }
	|  TRANSLATE LP expr COMMA expr COMMA expr RP
	  {
		attr.trans_set = 1;
		attr.tran.x = $3;
		attr.tran.y = $5;
		attr.tran.z = $7;
		if (verbose)
			fprintf(stderr, "translate(%g, %g, %g)\n", $3, $5, $7);
	  }
	|  ROTATE LP expr COMMA expr RP
	  {
		attr.rot_set = 1;
		attr.rot.ang = $3;
		attr.rot.ax = $5;
		if (verbose)
			fprintf(stderr, "rotate(%g, %c)\n", attr.rot.ang, attr.rot.ax);
	  }
	;

expr	: FLOAT
	  {
		$$ = $1;
	  }
	| INTEGER
	  {
		$$ = $1;
	  }
	| expr PLUS expr
	  {
		$$ = $1 + $3;
	  }
	| expr MINUS expr
	  {
		$$ = $1 - $3;
	  }
	| expr MULT expr
	  {
		$$ = $1 * $3;
	  }
	| expr DIV expr
	  {
		$$ = $1 / $3;
	  }
	| MINUS expr %prec UMINUS
	  {
		$$ = -$2;
	  }
	| LP expr RP
	  {
		$$ = $2;
	  }
	;
