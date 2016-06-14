/*
   dart.c

   A program implementing the remote art protocol.

   Dart is part of the public domain VORT package by Eric Chidna

   This version of dart was derived from the version of VORT 2.3.3 with the
   implementation of many extensions and some improvements, by
   John C. Bollinger of the Indiana University Molecular structure center.

   Last modification 11/2000.
*/
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <netdb.h>
#include "art.h"
#include "readall.h"

#define DEF_HOSTSFILE "/usr/local/lib/vort/hosts"
#define MAX_NAME_LEN 128
#define MAXHOSTS 64
#define FRAGDIMENSION 50
#define DEFAULT_BUF_SIZE 1024
#define DART_PORT 1992
#define DART_TRUE (1 == 1)
#define DART_FALSE (1 == 0)

struct dart_host {
    int    serial;
    int    fd;
    int    x, y;
    int    xdiff, ydiff;
    int    busy;
    struct dart_host *prev, *next;
} ;

static size_t checkfilename (const char *fname);
static void memerr (const char *pname);
static void quithost (int hfd);
static int sendshort (int sock, unsigned short i);
static void usage (const char *pname);

/*
 * checkfilename
 *
 * locate unacceptable characters in file names
 *
 * acceptable characters are digits, upper- and lowercase letters, and ./_-~
 */
static size_t checkfilename (const char *fname) {
    size_t i = strspn(fname, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./_-~");
    if (i > MAX_NAME_LEN) i = MAX_NAME_LEN;
    return i;
}

/*
 * memerr
 *
 * print a diagnostic and exit
 */
static void memerr (const char *pname) {
    fprintf(stderr, "%s: memory allocation failure; exiting...\n");
    exit(-2);
}

/*
 * quithost
 *
 * safely break the connection to a remote host
 */
static void quithost (int hfd) {
    int flags;

    /* ignore any SIGPIPE received during execution of this routine */
    signal(SIGPIPE, SIG_IGN);

    /* send a quit message to the remote host with a non-blocking write, and
       close the file descriptor */
    if ( (flags = fcntl(hfd, F_GETFL)) != -1 ) {
        if ( fcntl(hfd, F_SETFL, flags | O_NONBLOCK) == 0 ) {
            write(hfd, "Q", 1);
            close(hfd);
        }
    }

    /* restore previous SIGPIPE handling */
    signal(SIGPIPE, SIG_DFL);
}

/*
 * sendshort
 *
 *    send a short down a socket
 */
static int sendshort (int sock, unsigned short i) {
    unsigned short    val;

    val = htons(i);
    return write(sock, &val, sizeof(unsigned short));
}

/*
 * usage
 *
 * output a usage message and exit
 */
static void usage (const char *pname) {
    fprintf(stderr, "usage: %s [-g] [-h <hostfile>] [-m <shmid>] [-n] [-o <outfile>] [-p <path>] [-r <port>] [-v] <scenefile> <xpixels> <ypixels>\n", pname);
    exit(-1);
}

/*
 * main driver for distributed dart.
 */
int main (int argc, char *argv[]) {
    unsigned short	preprocess, remoteport, screenx, screeny, verbose;
    unsigned short	debug;
    int			fileno, i, j, nc, nhosts, opt, pending, pfds[2], shmid;
    int			x, xfragsize, xinc, y, yfragsize;
    char		c, *endp, *hostfilename, *map, name[100], *outfilename;
    char		*p, *p2, *path, *progname, *scenefilename, *shmp;
    long		l;
    off_t		imagesize;
    struct sockaddr_in	server;
    struct hostent	*hp;
    FILE		*hostfile, *scenefile;
    fd_set		readfds;
    struct stat		statbuf;
    image		*im;
    struct dart_host	*dh, *dh2, *first_dead, *first_host, *first_idle;
    struct dart_host	hosts[MAXHOSTS], *last_dead, *last_host, *last_idle;

    /*
       process program options and arguments
    */
    progname = strrchr(argv[0], '/');
    if (progname == NULL) progname = argv[0];
    else progname++;

    /* defaults */
    hostfilename = NULL;
    outfilename = NULL;
    path = NULL;
    preprocess = DART_TRUE;
    remoteport = DART_PORT;
    verbose = DART_FALSE;
    debug = DART_FALSE;
    xfragsize = yfragsize = FRAGDIMENSION;
    shmid = -1;

    /* loop through options */
    while ( (opt = getopt(argc, argv, "gh:m:no:p:r:v")) != -1 ) {
        switch (opt) {
            case 'h':    /* host file name */
                if ( *(optarg + checkfilename(optarg)) != '\0' ) {
                    fprintf(stderr,
                            "%s: invalid character(s) in host file name\n",
                            progname);
                    exit(-10);
                }
                hostfilename = strdup(optarg);
                if (hostfilename == NULL) memerr(progname);
                break;
            case 'm':    /* write image to a shared memory segment */
                l = strtol(optarg, &endp, 0);
                if (*endp != '\0' || l < 0) {
                    fprintf(stderr, "%s: shared memory id (%s)\n", progname,
                            optarg);
                    exit(-14);
                } else shmid = l;
                break;
            case 'n':    /* turn off preprocessor */
                preprocess = DART_FALSE;
                break;
            case 'o':    /* specify output file name */
                i = strlen(optarg);
                if ( i != checkfilename(optarg) ) {
                    fprintf(stderr,
                            "%s: invalid character(s) in output file name\n",
                            progname);
                    exit(-8);
                } else if ( (i < 4) ? DART_TRUE : (strcmp(optarg+i-4, ".pix")
                            != 0) ) {
                    outfilename = (char *) malloc(i + 5);
                    if (outfilename == NULL) memerr(progname);
                    strcat(strcpy(outfilename, optarg), ".pix");
                } else {
                    outfilename = strdup(optarg);
                    if (outfilename == NULL) memerr(progname);
                }
                break;
            case 'p':    /* specify path for remote hosts to use */
                if ( *(optarg + checkfilename(optarg)) != '\0' ) {
                    fprintf(stderr, "%s: invalid character(s) in remote path\n",
                            progname);
                    exit(-9);
                }
                path = strdup(optarg);
                if (path == NULL) memerr(progname);
                i = strlen(path) - 1;
                if ( i > 0 && path[i] == '/') path[i] = '\0';
                break;
            case 'r':    /* the remote port to connect to */
                l = strtol(optarg, &endp, 0);
                if (*endp != '\0' || l < 1025 || l > 65535) {
                    fprintf(stderr, "%s: invalid remote port (%s)\n", progname,
                            optarg);
                    exit(-7);
                } else remoteport = l;
                break;
            case 'g':
                debug = DART_TRUE;
                /* no break -- debug mode implies verbose mode */
            case 'v':    /* turn on verbose mode */
                verbose = DART_TRUE;
                break;
            default:     /* anything else is an error */
                usage(progname);
        }
    }

    /* non-option arguments */
    if (argc - optind < 2) usage(progname);
    scenefilename = strdup(argv[optind]);
    if (scenefilename == NULL) memerr(progname);
    screenx = atoi(argv[optind+1]);
    screeny = atoi(argv[optind+2]);
    if (hostfilename == NULL) {
        hostfilename = strdup(DEF_HOSTSFILE);
        if (hostfilename == NULL) memerr(progname);
    }

#define VORT_HDR_SZ 62
    if (shmid >= 0) {
        struct shmid_ds shmdata;
        if ( shmctl(shmid, IPC_STAT, &shmdata) != 0 ) shmp = NULL;
        else if ( shmdata.shm_segsz >= screenx * screeny * 4 + VORT_HDR_SZ ) {
            shmp = NULL;
            errno = ENOMEM;
        } else if ( (shmp = (char *) shmat(shmid, NULL, 0)) == (char *) -1 )
            shmp = NULL;
        else if (outfilename != NULL) strcpy(outfilename, "-");
        else {
            outfilename = strdup("-");
            if ( outfilename == NULL ) memerr(progname);
        }
        if (shmp == NULL) {
            perror(progname);
            exit(-15);
        }
        if ( pipe(pfds) != 0 ) {
            perror(progname);
            exit(-16);
        }
    }
#undef VORT_HDR_SZ

    if (verbose) {
        fprintf(stderr, "%s execution begins...\n", progname);
        fprintf(stderr, "  scene file: %s  size: %d x %d\n", scenefilename,
                screenx, screeny);
        if (shmid >= 0) fprintf(stderr, "  using shared memory segment %d\n",
            shmid);
    }

    /*
      check scene file unless a remote path was specified
    */
    if ( path == NULL ) {
        if ( (scenefile = fopen(scenefilename, "r")) == NULL ) {
            perror(progname);
            fprintf(stderr, "%s: cannot open scene file %s\n", progname,
                    scenefilename);
            exit(-3);
        }
/*
        if ( fstat(fileno, &statbuf) != 0 ) {
            perror(progname);
            exit(-12);
        }
        close(fileno);
        if ( ! S_ISREG(statbuf.st_mode) ) {
            fprintf(stderr, "%s: %s is not a regular file\n", progname,
                    scenefilename);
            exit(-13);
        }
*/
        fclose(scenefile);
    }

    /*
      open hosts file
    */
    if ( (hostfile = fopen(hostfilename, "r")) == (FILE *)NULL ) {
        fprintf(stderr, "%s: cannot open host file %s\n", progname, hostfilename);
        exit(-4);
    }

    /*
      determine the path to send to the remote artds
    */
    p = strrchr(scenefilename, '/');
    if ( path != NULL ) {    /* a remote path has been specified */
        if ( p != NULL ) {
            p++;
            memmove(scenefilename, p, strlen(p) + 1);
        }
    } else if ( *scenefilename == '/' ) {    /* absolute scene file name */
        if ( p == scenefilename ) {
            path = strdup("/");
        } else {
            *p = '\0';
            path = strdup(scenefilename);
        }
        if ( path == NULL ) memerr(progname);
        p++;
        memmove(scenefilename, p, strlen(p) + 1);
    } else {
        path = (char *) malloc(DEFAULT_BUF_SIZE * sizeof(char));
        if ( path == NULL ) memerr(progname);
        getcwd(path, DEFAULT_BUF_SIZE - 1);
        if ( p != NULL ) {
            *p = '\0';
            p++;
            if ( realloc(path, 
                         (strlen(path)+strlen(scenefilename)+2)*sizeof(char))
                 == NULL)
                memerr(progname);
            if (strlen(path) > 1) strcat(path, "/");
            strcat(path, scenefilename);
            p++;
            memmove(scenefilename, p, strlen(p) + 1);
        }
    }
    if (verbose) fprintf(stderr, "  using '%s' as the scene file path\n", path);

    if (verbose) fprintf(stderr, "  sanity checks complete (you're sane)\n");

    /*
      read entries from the host file and connect to artd servers on the
      specified systems
    */
    if (debug) fprintf(stderr, "  reading host info from %s:\n", hostfilename);
    FD_ZERO(&readfds);
    nhosts = 0;
    last_host = NULL;
    while ((nhosts < MAXHOSTS) && (fscanf(hostfile, "%99s %d", name, &i) == 2)){

        if (debug) fprintf(stderr, "    %s (%d jobs)\n", name, i);

        /* lookup host name/IP */
        if ((hp = gethostbyname(name)) == (struct hostent *)NULL) {
            printf("dart: can't find host %s\n", name);
            continue;
        }

        /* set up remote host parameters */
        memset(&server, 0, sizeof(server));
        memcpy(&server.sin_addr, hp->h_addr, hp->h_length);
        server.sin_family = hp->h_addrtype;
        server.sin_port = htons(remoteport);

        /* make the specified number of connections for this host */
        for ( ; i > 0; i--) {
            hosts[nhosts].fd = socket(hp->h_addrtype, SOCK_STREAM, 0);
            if ( connect(hosts[nhosts].fd, (struct sockaddr *)&server,
                         sizeof(server)) == 0 ) {
                hosts[nhosts].serial = nhosts;
                hosts[nhosts].busy = DART_FALSE;
                sendshort(hosts[nhosts].fd, strlen(path));
                write(hosts[nhosts].fd, path, strlen(path));
                sendshort(hosts[nhosts].fd, strlen(scenefilename));
                write(hosts[nhosts].fd, scenefilename, strlen(scenefilename));
                sendshort(hosts[nhosts].fd, screenx);
                sendshort(hosts[nhosts].fd, screeny);
                sendshort(hosts[nhosts].fd, preprocess);
                sendshort(hosts[nhosts].fd, xfragsize);
                sendshort(hosts[nhosts].fd, yfragsize);
                FD_SET(hosts[nhosts].fd, &readfds);
                hosts[nhosts].next = NULL;
                hosts[nhosts].prev = last_host;
                last_host = hosts + nhosts;
                if ( hosts[nhosts].prev != NULL )
                    hosts[nhosts].prev->next = last_host;
                nhosts++;
            } else {
                if (verbose) perror(progname);
                close(hosts[nhosts].fd);                          
                break;
            }
        }
    }

    fclose(hostfile);

    if (nhosts == 0) {
        fprintf(stderr, "%s: can't connect to any hosts\n", progname);
        exit(-5);
    }
    first_host = hosts;
    first_idle = last_idle = first_dead = last_dead = NULL;

    if (verbose) fprintf(stderr, "  %d remote host connections established\n",
                         nhosts);

    /*
      Open output file and write header.
    */
    if (outfilename == NULL) {
        outfilename = strdup(scenefilename);
        if (outfilename == NULL) memerr(progname);
        p = strrchr(outfilename, '.');
        if ( (p == NULL) ? DART_TRUE : strcmp(p, ".scn") ) {
            j = strlen(outfilename);
            if ( realloc(outfilename, j + 5) == NULL ) memerr(progname);
            p = outfilename + j;
        }
        strcpy(p, ".pix");
    }

    if ((im = openimage(outfilename, "w")) == NULL) {
        fprintf(stderr, "%s: can't open output file %s.\n", progname,
                outfilename);
        exit(-6);
    /*
      if writing to shared memory then outfilename should have been dummied to
      "-", causing openimage to assign im->fd as 1 instead of opening any
      actual file.  In this case, pfds should also have been initialized with
      pipe(); we use this as a hook into the image file to copy the output into
      the shared memory segment.  In any case, we set im->fd to the writing
      end of the pipe.
    */
    } else if ( shmid >= 0 ) im->fd = pfds[1];

    imagetype(im) = PIX_RGBA;
    imagewidth(im) = screenx;
    imageheight(im) = screeny;
    imagedate(im) = time(0);
    imagedepth(im) = 24;
    titlelength(im) = 0;

    writeheader(im);
    
    /*
      If outputting to shared memory then capture the image header, and
      initialize map in the process.
    */
    if (shmid >= 0) {
        map = shmp;
        do {
            nc = read(pfds[0], map, 128);
            if (nc < 0) {
                perror(progname);
                exit(-18);
            } else map += nc;
        } while ( nc != 0 );
        close(pfds[0]);
    }

    if (verbose) fprintf(stderr, "  image header written\n");

    /*
      If outputting to a file then set up a memory buffer for all the pixel data
    */
    if (shmid < 0) {
        imagesize = screenx * screeny * 4;
        map = (char *) malloc(imagesize);
        if (map == NULL) memerr(progname);
    }

    /*
      macros for managing the various host queues (normal, idle, and dead) 
    */
#define RMHOST(ih,f,l) do { struct dart_host *h = ih; \
    if (h->next != NULL) h->next->prev = h->prev; else l = h->prev; \
    if (h->prev != NULL) h->prev->next = h->next; else f = h->next; } \
                      while (DART_FALSE)
#define APHOST(h,f,l) do { h->prev = l; h->next = NULL; \
                           if ( l == NULL ) f = l = h; else l = (l->next = h);}\
                      while (DART_FALSE)
#define INHOST(h,f,l) do { h->prev = NULL; h->next = f; \
                           if ( f == NULL ) f = l = h; else f = (f->prev = h);}\
                      while (DART_FALSE)
#define WAKE(h) do { RMHOST(h, first_idle, last_idle); \
                     INHOST(h, first_host, last_host); } while (DART_FALSE)
#define HANGUP(h) do { quithost(h->fd); RMHOST(h, first_host, last_host); } \
                  while(DART_FALSE)
#define ZOMBIE(h) do { HANGUP(h); APHOST(h, first_dead, last_dead); } \
                  while (DART_FALSE)

    /*
      Now send out the fragments and receive back the results
    */
    for ( x = y = 0; first_host != NULL; ) {
        /* scan the list of active hosts */
        for (dh = first_host; dh != NULL; dh = dh2) {

            /* copy the current value of dh's next pointer to dh2 */
            dh2 = dh->next;

            /* for those that have completed their latest assignment... */
            if ( ! dh->busy ) {

                /* apply line wrapping */
                if (x >= screenx) {
                    x = 0;
                    y += yfragsize;
                }

                /* have we assigned all fragments? */
                if (y >= screeny) {
                    /* did any fragments not complete?  */
                    if (first_dead != NULL) {
                        /* assign an uncompleted fragment to the host */
                        dh->x = first_dead->x;
                        dh->y = first_dead->y;
                        dh->xdiff = first_dead->xdiff;
                        dh->ydiff = first_dead->ydiff;
                        dh->busy = DART_TRUE;
                        if (debug) 
                            fprintf(stderr, "  dead host %d's fragment reassigned to host %d\n",
                                    first_dead->serial, dh->serial);
                        /* remove the first dead host from the list */
                        RMHOST(first_dead, first_dead, last_dead);
                    }

                    /* if all fragments are complete so far, idle the host */
                    if (! dh->busy) {
                        RMHOST(dh, first_host, last_host);
                        APHOST(dh, first_idle, last_idle);
                        if (debug)
                            fprintf(stderr, "  no more work for host %d\n", 
                                    dh->serial);
                        continue;
                    }
                } else {
                    /* assign a new fragment to the host */
                    dh->ydiff = ((y + yfragsize >= screeny) ? (screeny - y) :
                        yfragsize);
                    dh->xdiff = ((x + xfragsize >= screenx) ? (screenx - x) :
                        xfragsize);
                    dh->x = x;
                    dh->y = y;
                    dh->busy = DART_TRUE;
                    if (debug) 
                        fprintf(stderr, "  host %d assigned %d %d %d %d\n", 
                                dh->serial, x, x + dh->xdiff - 1, screeny - y,
                                screeny - y - dh->ydiff + 1);
                    x += xfragsize;
                }

                /* send the fragment data to the host */
                write(dh->fd, "F", 1);
                sendshort(dh->fd, dh->x);
                sendshort(dh->fd, dh->x + dh->xdiff - 1);
                sendshort(dh->fd, dh->y);
                sendshort(dh->fd, dh->y + dh->ydiff - 1);
                if (debug) 
                    fprintf(stderr, "  fragment sent to host %d\n", dh->serial);
            }
        }

        if (first_host == NULL) break;
        /* load readfds with the fds of the remaining active hosts */
        FD_ZERO(&readfds);
        for (dh = first_host; dh != NULL; dh = dh->next)
            FD_SET(dh->fd, &readfds);

        if ( select(FD_SETSIZE, &readfds, NULL, NULL, NULL) < 0 )
            perror("whoops");

        for (dh = first_host; dh != NULL; dh = dh2) {
            /* copy dh's current next pointer to dh2 */
            dh2 = dh->next;

            /* get the responses from all hosts that responded */
            if ( FD_ISSET(dh->fd, &readfds) )  {

                if (debug) 
                    fprintf(stderr, "  response from host %d ", dh->serial);

                /* has the remote host hung up? */
                if ( read(dh->fd, &c, 1) != 1 ) {
                    if (debug) fprintf(stderr, "is a hangup.\n");
                    ZOMBIE(dh);
                    if (debug) fprintf(stderr, "    host %d has bad juju\n",
                                       dh->serial);
                    continue;
                }
                /* does the host send a valid response code? */
                if ( c != 'D' ){
                    if (debug) fprintf(stderr,
#ifndef STRICT
                        "is garbage ('%c' = %x);  ignoring it.\n", c, (int) c);
                    FD_CLR(dh->fd, &readfds);
#else
                        "is garbage ('%c' = %x);  hanging up.\n", c, (int) c);
                    ZOMBIE(dh);
#endif
                    continue;
                }

                /*
                  receive the fragment data from the remote host.  if we get
                  fewer characters than the fragment size then assume the remote
                  machine has had a fault; it is zombified.
                */
                p = map + (dh->y * screenx + dh->x) * 4;
                nc = dh->xdiff * 4;
                for (j = 0; j < dh->ydiff; j++) {
                    if ( readall(dh->fd, p, nc) != nc ) {
                        ZOMBIE(dh);
                        if (debug) fprintf(stderr, "is garbled; hanging up.\n");
                        continue;
                    }
                    p += screenx * 4;
                }

                /* fragment received; this host is no longer busy */
                dh->busy = DART_FALSE;
                if (debug) fprintf(stderr, "is good.\n");
            }
        }
        /* wake up as many idle hosts as we have zombie hosts, or as many as
           there are, whichever is fewer */
        for (dh = first_dead; dh != NULL && first_idle != NULL; dh = dh->next) {
            if (debug)
                fprintf(stderr, "  waking host %d\n", first_idle->serial);
            WAKE(first_idle);
        }
    }

    /* Close connections to the remaining hosts (which should all be idle) */
    for (dh = first_idle; dh != NULL; dh = dh->next) HANGUP(dh);

    /*
      Rendering is complete.  Write out the image and finish up.
    */
    if (verbose) fprintf(stderr, "  rendering complete, writing image...\n");
    if ( shmid < 0 ) {
        for (p=map; imagesize > 0; imagesize -= i) {
            i = write(im->fd, p, imagesize);
            if (i < 0) {
                perror(progname);
                break;
            }
            p += i;
        }
    }

    close(im->fd);

    if (verbose) fprintf(stderr, "All done.\n\n");

    if (shmid >= 0) shmdt(shmp);
    free(map);
    free(path);
    free(outfilename);
    free(hostfilename);
    free(scenefilename);
    exit(0);
}
