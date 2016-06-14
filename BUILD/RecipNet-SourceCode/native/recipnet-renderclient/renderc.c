/*
 * Reciprocal Net project
 * @(#)renderc.c
 * 
 * 10-Oct-2000: jobollin wrote first draft
 * 06-Nov-2002: ekoperda incorporated file into recipnet source tree
 *
 * This command-line program is a network client for submitting rendering
 * jobs to a dart server and receiving back the rendered image.  The protocol
 * is very simple and sits on top of TCP.  By default, this program reads a 
 * scene file from stdin and writes the output file to stdout, but filenames
 * may be specified by using the -i and -o command-line options.  A parameter
 * specifying the number of rendering servers to use is exchanged between
 * renderc and darter, and may be passed to renderc on the command line, but is
 * presently ignored by darter.  Likewise, the -v verbose mode flag is 
 * presently ignored.
 *
 * The -z command-line option tells this program to compress the input file
 * using the bzip2 library before sending it across the network; in this case 
 * the server also will compress the returned image file.  This incurs a 
 * significant overhead and normally is useful only if there is a severe
 * bandwidth limitation between client and server, and if the scene file and/or
 * the image is large.
 *
 * TODO:
 *   1. implement verbose mode
 *   2. improve security by linking in and using libwrap
 */

#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netdb.h>
#include "dartio.h"

#define MAX_N_SERVERS 255L

long getrngint(const char *data, long llim, long ulim, const char *pname,
               const char *estr);
void memerr(const char *pname);
void syserr(const char *pname);
void usage(const char *pname);

long getrngint(const char *data, long llim, long ulim, const char *pname,
               const char *estr) {
    long l;
    const char *endp;

    endp = data;
    l = strtol(data, (char **) &endp, 0);
    if (*endp != '\0') {
        fprintf(stderr, "%s: invalid integer '%s'\n", pname, data);
        exit(-1);
    }
    if (l < llim || l > ulim) {
        fprintf(stderr, "%s: %s\n", pname, estr);
        exit(-1);
    }
    return l;
}

void memerr(const char *pname) {
    fprintf(stderr, "%s: memory allocation error\n", pname);
    exit (-1);
}

void syserr(const char *pname) {
    perror(pname);
    exit (-1);
}

void usage(const char *pname) {
    fprintf(stderr, "usage: %s [-i <infile>] [-n <nserv>] [-o <outfile>]"
                    " [-v] [-z] <xdim> <ydim> <server>\n", pname);
    exit (-1);
}

int main(int argc, char *argv[]) {
    ssize_t filelen;
    unsigned short xdim, ydim;
    unsigned char ns;
    int sock, i, ind, fdin, fdout, opt, verbose, compress;
    char *progname;
    struct hostent *hep;
    struct sockaddr_in addr;
    long resp_len;

    /*
     * determine by what name we were called
     */
    if ((progname = strrchr(argv[0], '/')) == NULL) {
        progname = argv[0];
    } else {
        progname++;
    }

    /*
     * process arguments
     */
    fdin = STDIN_FILENO;
    fdout = STDOUT_FILENO;
    verbose = DART_FALSE;
    compress = DART_FALSE;
    while ((opt = getopt(argc, argv, "i:n:o:vz")) != -1) {
        switch (opt) {
            case 'i':
  	        if ((fdin = open(optarg, O_RDONLY)) < 0) {
                    syserr(progname);
                }
                break;
            case 'n':
                ns = (unsigned char) getrngint(optarg, 0L, MAX_N_SERVERS,
                    progname, "invalid number of servers");
                break;
            case 'o':
                if ((fdout = open(optarg, O_WRONLY | O_CREAT | O_TRUNC, 
		        S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP)) < 0) {
                    syserr(progname);
		}
                break;
            case 'v':
                verbose = DART_TRUE;
                break;
            case 'z':
                compress = DART_TRUE;
                break;
            default:
                usage(progname);
        }
    }
    ind = optind;
    if (argc - ind < 3) {
        usage(progname);
    }
    xdim = (unsigned short) getrngint(argv[ind++], 1L, 65535L, progname, 
        "first dimension invalid");
    ydim = (unsigned short) getrngint(argv[ind++], 1L, 65535L, progname, 
        "second dimension invalid");
    if ((hep = gethostbyname(argv[ind++])) == NULL) {
        fprintf(stderr, "%s: cannot resolve host '%s'\n", progname, argv[ind]);
        exit(-1);
    }

    /*
     * Connect to server
     */
    addr.sin_family = AF_INET;
    addr.sin_port = htons(DART_REQ_PORT);
    memcpy(&(addr.sin_addr.s_addr), hep->h_addr_list[0],
           sizeof(addr.sin_addr.s_addr));
    if ((sock = socket(PF_INET, SOCK_STREAM, 0)) < 0) {
        syserr(progname);
    }
    i = SOCK_BUF_SZ;
    if (setsockopt(sock, SOL_SOCKET, SO_SNDBUF, &i, (socklen_t) sizeof(i)) 
	    != 0) {
        syserr(progname);
    }
    if (setsockopt(sock, SOL_SOCKET, SO_RCVBUF, &i, (socklen_t) sizeof(i)) 
	    != 0) {
        syserr(progname);
    }
    if (connect(sock, (struct sockaddr *) &addr, sizeof(addr)) != 0) {
        syserr(progname);
    }
    
    /*
     * Send request to server
     */
    if ((filelen = (compress ? sendfileZ(fdin, sock) : sendfile(fdin, sock)))
            < 0) {
        syserr(progname);
    }
    fprintf (stdout, "Input file transferred to server (%ld bytes)\n", 
            filelen);
    if (sendshort(sock, xdim) != sizeof(unsigned short)) {
        fprintf(stderr, "%s: communications error 1\n", progname);
        exit(-1);
    }
    if (sendshort(sock, ydim) != sizeof(unsigned short)) {
        fprintf(stderr, "%s: communications error 2\n", progname);
        exit(-1);
    }
    if (write(sock, &ns, 1) != 1) {
        fprintf(stderr, "%s: communications error 3\n", progname);
        exit(-1);
    }

    /*
     * get and check initial server response
     */
    {
        unsigned short resp_x, resp_y;
        unsigned char ok = 1;

        if (recvlong(sock, &resp_len) != LINT_SZ) {
	    ok = 0;
	} else if (recvshort(sock, &resp_x) != SHINT_SZ) {
            ok = 0;
	}
        else if (recvshort(sock, &resp_y) != SHINT_SZ) {
            ok = 0;
	}
        if (ok == 1) {
            if (filelen != resp_len || xdim != resp_x || ydim != resp_y) {
                fprintf(stderr, "%s: verification error\n", progname);
                ok = 0;
            }
        } else {
            fprintf(stderr, "%s: communications error 4\n", progname);
	}
        write(sock, &ok, 1);
        if (ok != 1) {
            exit(-1);
	}
    }
    fprintf(stderr, "%s: server acknowledges request; now wait...\n", 
            progname);

    /*
     * Wait for and receive the server's response
     */
    if ((filelen = recvfile(sock, fdout)) < 0) {
        syserr(progname);
    }
    if (recvlong(sock, &resp_len) < 0) {
        syserr(progname);
    }
    if (resp_len == filelen) {
        fprintf(stderr, "rendering complete.\n");
    } else {
        fprintf(stderr, "%s: transmission error -- check output\n", progname);
        exit(1);
    }

    /*
     * Tidy up and exit
     */
    close (fdout);
    close (sock);
    close (fdin);
    exit(0);
}

