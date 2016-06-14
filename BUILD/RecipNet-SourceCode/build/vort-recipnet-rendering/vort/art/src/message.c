#include <stdio.h>
#include <stdlib.h>
#include "status.h"

extern int	linecount;
extern FILE	*logfile;
extern char	currentfile[];
extern char	*tmpname;

/*
 * yyerror
 *
 *	error routine for yacc, assumed fatal
 */
yyerror(s)
	char	*s;
{
	fprintf(stderr, "art: %s line %d - %s\n", currentfile, linecount, s);
	fprintf(logfile, "art: %s line %d - %s\n", currentfile, linecount, s);

	if (tmpname != (char *)NULL)
		unlink(tmpname);

	exit(ERROR);
}

/*
 * printprogress
 *
 *	prints th escanline we are up to on the screen.
 */
printprogress(scanline)
	int	scanline;
{
	fprintf(stderr, "\r(%03d)", scanline);
}

/*
 * message
 *
 *	log a message.
 */
message(s)
	char	*s;
{
	fprintf(logfile, s);
	fflush(logfile);
}

/*
 * warning
 *
 *	print and log a warning.
 */
warning(s)
	char	*s;
{
	fprintf(stderr, s);

	if (logfile != stdout)
		fprintf(logfile, s);
}

/*
 * fatal
 *
 *	print and log a fatal message and exit
 */
fatal(s)
	char	*s;
{
	fprintf(stderr, s);

	if (logfile != stdout)
		fprintf(logfile, s);

	exit(ERROR);
}

