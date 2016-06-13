%{
#include <string.h>
#include <math.h>
#include "text3d.h"
#include "gram.h"

extern int	linecount;
extern double	atof();

%}

%%

output		return(OUTPUT);

drawstr		return(DRAWSTR);

colour		return(COLOUR);

color		return(COLOUR);

textsize	return(TEXTSIZE);

ambient		return(AMBIENT);

reflectance	return(REFLECTANCE);

transparency	return(TRANSPARENCY);

material	return(MATERIAL);

textradius	return(TEXTRADIUS);

fixedwidth	return(FIXEDWIDTH);

centertext	return(CENTERTEXT);

centretext	return(CENTERTEXT);

textang		return(TEXTANG);

font		return(FONT);

drawchar	return(DRAWCHAR);

boxfit		return(BOXFIT);

boxtext		return(BOXTEXT);

move		return(MOVE);

rmove		return(RMOVE);

scale		return(SCALE);

translate	return(TRANSLATE);

rotate		return(ROTATE);

fixedwidth	return(FIXEDWIDTH);

centertext	return(CENTERTEXT);

true		return(TRUE);

false		return(FALSE);

cyl_sph		return(CYL_SPH);

box_cyl		return(BOX_CYL);

[0-9]*	{
			yylval.y_int = atoi(yytext);
			return(INTEGER);
		}

[0-9]*\.[0-9]*	{
			yylval.y_flt = atof(yytext);
			return(FLOAT);
		}

copy		{
			int	braces = 1, c;
			while ((c = input()) != '{')
				if (c == '\n')
					linecount++;

			while (braces) {
				c = input();
				if (c == '\n')
					linecount++;

				if (c == '\\') {
					c = input();
					putchar(c);
					if (c == '\n')
						linecount++;
				} else {
					
					if (c == '{')
						braces++;
					if (c == '}')
						braces--;

					if (c == EOF) {
						fprintf(stderr, "Unterminated copy\n");
						exit(1);
					}

					if (braces)
						putchar(c);
				}
			}
			while ((c = input()) == '\n' || c == ' ' || c == '\t' )
				if (c == '\n')
					linecount++;

			unput(c);
			putchar('\n');
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

"\'"		{
			int	c1, c2;

			c1 = input();

			if (c1 == '\\')
				c1 = input();

			if (c1 == '\n' || c1 == EOF)
				yyerror("syntax error");

			while((c2 = input()) != '\'')
				if (c2 == '\n' || c2 == EOF)
					yyerror("syntax error");
					
		 	
			yylval.y_int = c1;
			return(INTEGER);
		}

"\""		{
			char	buf[BUFSIZ], *p;
			
			for (p = buf; (*p = input()) != '"'; p++) {
				if (*p == '\n' || *p == EOF) {
					sprintf(buf, "syntax error\n", linecount);
					yyerror(buf);
				}
					

				if (*p == '\\') 
					*p = input();
			}

			*p = 0;

			yylval.y_str = (char *)malloc(strlen(buf) + 1);
			strcpy(yylval.y_str, buf);

			return(STRING);
		}

"{"		{
			return(LBRACE);
		}

"}"		{
			return(RBRACE);
		}

"\n"		{
			linecount++;
		}

[ \t]		{
			;
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
				fprintf(stderr, "text3d: unterminated comment - started line %d.\n", comline);
				exit(1);
			}
		}
