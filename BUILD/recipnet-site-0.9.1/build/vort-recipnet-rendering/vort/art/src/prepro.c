#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <fcntl.h>
#include <string.h>
#include <sys/types.h>

extern char	*frameid;

/*
 * prepro
 *
 *	open and read file1 and write it to the file pointed to by f2
 * doing what preprocessing is necessary along the way.
 */
prepro(file1, f2)
	char	*file1;
	FILE	*f2;
{
	FILE	*f1;
	int	c, lastc, incomment, linecount, i;
	char	buf[BUFSIZ];

	if ((f1 = fopen(file1, "r")) == NULL) {
		sprintf(buf, "art: unable to open file %s.\n", file1);
		fatal(buf);
	}

	fprintf(f2, "# 1 \"%s\"\n", file1);

	incomment = 0;
	linecount = 1;
	lastc = 0;

	c = fgetc(f1);
	while (!feof(f1)) {
		if (c == '*' && lastc == '/')
			incomment++;
		if (c == '/' && lastc == '*')
			incomment--;
		else if (c == '\n')
			linecount++;

		if (incomment)
			fputc(c, f2);
		else {
			if (c == 'i')
				if ((c = fgetc(f1)) == 'n')
					if (fscanf(f1, "clude %s", buf) == 1) {
						i = strlen(buf) - 8;
						if (i > 0 && strcmp(&buf[i], ".frameid") == 0)
							strcpy(&buf[i + 1], frameid);
						prepro(buf, f2);
						fprintf(f2, "# %d \"%s\"\n", linecount, file1);
					} else {
						fputc('i', f2);
						fputc(c, f2);
					}
				else {
					fputc('i', f2);
					fputc(c, f2);
				}
			else
				fputc(c, f2);
		}

		lastc = c;
		c = fgetc(f1);
	}

	fclose(f1);
}
