#include <stdio.h>
#include <stdlib.h>
#include "status.h"

/*
 * vorterror
 *
 * prints out an error message and then exits.
 *
 */
void vorterror(s)
	char	*s;
{
	fprintf(stderr, s);
	exit(ERROR);
}

/*
 * vortwarning
 *
 * prints out a non-fatal error message.
 *
 */
void vortwarning(s)
	char	*s;
{
	fprintf(stderr, s);
}
