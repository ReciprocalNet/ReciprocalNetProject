#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/times.h>
#include <netinet/in.h>
#include <netdb.h>
#include <syslog.h>

#define	ART	"/usr/local/bin/art"

/*
 * readshort
 *
 *	read a short value from a socket.
 */
int
readshort(sock)
	int	sock;
{
	unsigned short	val;

	read(sock, &val, sizeof(unsigned short));
	return((int)ntohs(val));
}

/*
 * main driver to read in scene files and then raytrace them.
 */
main(ac, av)
	int	ac;
	char	**av;
{
	FILE			*logf;
	struct sockaddr_in	caller;
	char			c, name[200], path[200], *argv[10], *envv[10];
	char			xbuf[100], ybuf[100];
	int			len, screenx, screeny, pid, status, error;
	unsigned char		*map;

	logf = fopen("/tmp/in.netartd", "w");

	nice(10);

	c = ' ';
	read(0, &c, 1);		/* grab header character */

	error = 0;

	while (!error && c != 'Q') {

		read(0, &c, 1);		/* do we preprocess? */
		/*
		 * read in working directory for art
		 */
		len = readshort(0);

		read(0, path, len);
		path[len] = 0;

		fprintf(logf, "path %s\n", path);
		fflush(logf);

		if (chdir(path) < 0)
			error = 1;

		/*
		 * read in name of scene file
		 */
		len = readshort(0);

		read(0, name, len);
		name[len] = 0;

		screenx = readshort(0);
		screeny = readshort(0);

		sprintf(xbuf, "%d", screenx);
		sprintf(ybuf, "%d", screeny);

		fprintf(logf, "name %s - %d %d\n", name, screenx, screeny);
		fflush(logf);

		if (!error) {
			if ((pid = fork()) != 0) {
				wait(&status);
				error = (status != 0);
			} else {
				/*
				 * stop junk from art going through the sockets
				 */
				close(0);
				close(1);
				close(2);

				argv[0] = "art";
				argv[1] = name;
				argv[2] = xbuf;
				argv[3] = ybuf;

				if (c == 'Y')
					argv[4] = (char *)NULL;
				else {
					argv[4] = "-n";
					argv[5] = (char *)NULL;
				}

				envv[0] = (char *)NULL;

				execve(ART, argv, envv);

				/*
				 * shouldn't get here but if we do...
				 */
				exit(1);
			}
		}

		if (!error) {
			write(1, "D", 1);
			read(0, &c, 1);
			fprintf(logf, "%c\n", c);
			fflush(logf);
		} else
			write(1, "E", 1);
	}

	fclose(logf);

	exit(0);
}
