#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include "art.h"

#define	MAXHOSTS	100
#define	HOSTSFILE	"/usr/local/lib/vort/hosts"

/*
 * sendshort
 *
 *	send a short down a sockect
 */
sendshort(sock, i)
	int	sock, i;
{
	unsigned short	val;

	val = htons(i);
	write(sock, &val, sizeof(unsigned short));
}

/*
 * main driver for distributed arts.
 */
main(ac, av)
	int	ac;
	char	*av[];
{
	FILE			*infile;
	int			sock, socks[MAXHOSTS], pending, ret;
	struct sockaddr_in	server;
	struct servent		*sp;
	struct hostent		*hp, *gethostbyname();
	unsigned short		len, screenx, screeny;
	int			nhosts, i, j, x, y, xfragsize, yfragsize;
	int			compress, preprocess;
	char			name[200], path[200], buf[1024];
	char			*names[MAXHOSTS];
	char			*hosts[MAXHOSTS], c;
	fd_set			readfds, writefds, exceptfds;

	if (ac < 4 && ac != 2 && ac != 3) {
		fprintf(stderr, "nart: usage nart [file.scn xpixels ypixels] | [ infile ] [-Z]\n");
		exit(1);
	}

	getcwd(path, sizeof(path) - 1);

	if (ac == 4 || ac == 5) {
		sp = getservbyname("netartd", "tcp");
		hp = gethostbyname("localhost");

		if (sp == (struct servent *)NULL) {
			fprintf(stderr, "nart: can't find service\n");
			exit(1);
		}

		if (hp == (struct hostent *)NULL) {
			fprintf(stderr, "nart: can't find localhost\n");
			exit(1);
		}

		bzero(&server, sizeof(server));

		bcopy((char *)hp->h_addr, (char *)&server.sin_addr, hp->h_length);

		server.sin_family = hp->h_addrtype;
		server.sin_port = sp->s_port;

		if ((sock = socket(hp->h_addrtype, SOCK_STREAM, 0)) < 0) {
			fprintf(stderr, "nart: can't create socket\n");
			exit(1);
		}

		if (connect(sock, (struct sockaddr *)&server, sizeof(server)) < 0) {
			perror("nart: can't connect to netart daemon.\n");
			exit(1);
		}

		write(sock, "N", 1);

		if (ac == 5 && strcmp(av[2], "-n") == 0)
			write(sock, "N", 1);
		else
			write(sock, "Y", 1);

		sendshort(sock, strlen(path));

		write(sock, path, strlen(path));

		sendshort(sock, strlen(av[1]));

		write(sock, av[1], strlen(av[1]));

		screenx = atoi(av[2]);
		screeny = atoi(av[3]);

		sendshort(sock, screenx);
		sendshort(sock, screeny);

		printf("scene %s - %d %d\n", av[1], screenx, screeny);

		read(sock, &c, 1);

		write(sock, "Q", 1);	/* terminate session */

		close(sock);

	} else {
		compress = (ac == 3 && (av[2][1] == 'Z' || av[2][2] == 'Z'));

		preprocess = !(ac == 3 && (av[2][1] == 'n' || av[2][2] == 'n'));


		if ((infile = fopen(HOSTSFILE, "r")) == (FILE *)NULL) {
			fprintf(stderr, "nart: can't open hosts file %s\n", HOSTSFILE);
			close(sock);
			exit(1);
		}

		sp = getservbyname("netartd", "tcp");

		if (sp == (struct servent *)NULL) {
			fprintf(stderr, "nart: can't find service\n");
			exit(1);
		}

		nhosts = 0;

		while (fscanf(infile, "%s ", name) == 1) {

			bzero(&server, sizeof(server));

			if ((hp = gethostbyname(name)) == (struct hostent *)NULL) {
				printf("nart: can't find host %s\n", name);
				continue;
			}

			bcopy((char *)hp->h_addr, (char *)&server.sin_addr, hp->h_length);

			server.sin_family = hp->h_addrtype;
			server.sin_port = sp->s_port;

			socks[nhosts] = socket(hp->h_addrtype, SOCK_STREAM, 0);

			names[nhosts] = (char *)NULL;
			if (socks[nhosts] > 0 && !(connect(socks[nhosts], (struct sockaddr *)&server, sizeof(server)) < 0))
				nhosts++;
			else
				close(socks[nhosts]);
		}

		fclose(infile);

		if (nhosts == 0) {
			fprintf(stderr, "nart: can't connect to any hosts\n");
			exit(1);
		}

		if ((infile = fopen(av[1], "r")) == (FILE *)NULL) {
			fprintf(stderr, "nart: can't open file %s\n", av[1]);
			for (i = 0; i != nhosts; i++)
				close(socks[i]);
			exit(1);
		}

		FD_ZERO(&readfds);
		for (i = 0; i != nhosts; i++)
			if (socks[i] > 0)
				FD_SET(socks[i], &readfds);

		pending = 1;
		while (pending) {
			for (i = 0; i != nhosts; i++)
				if (socks[i] > 0 && FD_ISSET(socks[i], &readfds)) {
					ret = fscanf(infile, "%s %hd %hd ", name, &screenx, &screeny);
					if (ret == EOF) {
						for (j = i; j != nhosts; j++) {
							write(socks[j], "Q", 1);
							close(socks[j]);
							socks[j] = -1;
						}
						break;
					}

					if (ret != 3) {
						fprintf(stderr, "nart: can't open file %s\n", av[2]);
						for (i = 0; i != nhosts; i++)
							if (socks[i] > 0) 
								close(socks[i]);
						exit(1);
					}

					write(socks[i], "N", 1);

					if (preprocess)
						write(socks[i], "Y", 1);
					else
						write(socks[i], "N", 1);

					sendshort(socks[i], strlen(path));

					write(socks[i], path, strlen(path));

					sendshort(socks[i], strlen(name));

					write(socks[i], name, strlen(name));

					sendshort(socks[i], screenx);
					sendshort(socks[i], screeny);

					printf("scene file %s - %d %d\n", name, screenx, screeny);
					if (compress && names[i] != (char *)NULL) {
						sprintf(buf, "compress -f %s", names[i]);
						system(buf);
					}

					names[i] = (char *)malloc(strlen(name) + 1);
					strcpy(names[i], name);
					strcpy(rindex(names[i], '.'), ".pix");
				}

			FD_ZERO(&readfds);
			pending = 0;
			for (i = 0; i != nhosts; i++)
				if (socks[i] > 0) {
					pending = 1;
					FD_SET(socks[i], &readfds);
				}

			if (pending)
				if ((i = select((int)ulimit(4, 0L), &readfds, (fd_set *)NULL, (fd_set *)NULL, (struct timeval *)NULL)) < 0)
					perror("whoops");

			for (i = 0; i != nhosts; i++)
				if (socks[i] > 0 && FD_ISSET(socks[i], &readfds))  {
					if (read(socks[i], &c, 1) != 1)
						socks[i] = -1;
					if (socks[i] > 0 && c != 'D')
						FD_CLR(socks[i], &readfds);
				}
		}

		fclose(infile);
	}
}
