#include <stdio.h>
#include <stdlib.h>

/*
 * smalloc
 *
 *	allocates memory - checking for null
 */
#ifdef DEBUG
#undef smalloc
char *
smalloc(size, file, line)
	unsigned 	size;
	char		*file;
	int		line;
#else
char *
smalloc(size)
	unsigned 	size;
#endif
{
	char	*p, *p2, buf[BUFSIZ];

	if ((p = malloc(size)) == (char *)NULL) {
		sprintf(buf, "smalloc: request for %d bytes from malloc returns NULL.\n", size);
#ifdef DEBUG
		fprintf(stderr, "%s: %d, size=%ld\n", file, line, (long)size);
#endif
		fatal(buf);
	}

	/*
	for (p2 = p; p2 != p + size; p2++)
		*p2 = 0xff;
	*/

	return(p);
}

/*
 * scalloc
 *
 *	allocates memory - checking for null - this was needed for the PC.
 */
char *
#ifdef DEBUG
#undef scalloc
scalloc(size, n, file, line)
	unsigned 	size;
	unsigned	n;
	char		*file;
	int		line;
#else
scalloc(size, n)
	unsigned 	size;
	unsigned	n;
#endif
{
	char	*p, *p2, buf[BUFSIZ];


	if ((p = malloc((unsigned) size * n)) == (char *)NULL) {
		sprintf(buf, "scalloc: request for %ld bytes from malloc returns NULL.\n", (long)size * (long)n);
#ifdef DEBUG
		fprintf(stderr, "%s: %d, size=%d, n = %d\n", file, line, size, n);
#endif
		fatal(buf);
	}

	/*
	for (p2 = p; p2 != p + size * n; p2++)
		*p2 = 0xff;
	*/

	return(p);
}

