/*
 * Reciprocal Net project
 * @(#)dartio.c
 * 
 * 10-Oct-2000: jobollin wrote first draft
 * 06-Nov-2002: ekoperda incorporated file into recipnet source tree
 *
 * Routines for doing i/o across network connections.  (In fact, these
 * routines will work equally well with any file descriptors, but they
 * are designed for file descriptors associated with connected sockets.)
 *
 * Three types of data transfer are supported: short (as in short int),
 * block, and file.  For each type there are complementary send and recv
 * (receive) functions.  Every attempt is made to catch error conditions,
 * but they are just passed back to the calling function in the form of a
 * negative return value.
 */

#define INDARTIO
#include "dartio.h"
#undef INDARTIO

int recd_Z;

ssize_t recvall (int fd, char *buf, ssize_t len) {
    ssize_t n2r = len, nr;
    char *p = buf;
#ifdef DEBUG3R
    fprintf(stderr, "  recvall %u bytes...", len);
#endif
    while (n2r > 0) {
        if ((nr = read(fd, p, n2r)) < 0) {
	    return -1;
        }
        n2r -= nr; 
        p += nr;
#ifdef DEBUG4R
        if (nr > 0) {
            fprintf(stderr, " %u", n2r);
        }
#endif
    }
    return len;
}

ssize_t recvblock(int fd, char **bufp) {
    unsigned short int bs;
    char *p;

    if (recvshort(fd, &bs) != SHINT_SZ) {
        return -1;
    }
    if (bs > MAX_BLOCK_SZ) {
        return -1;
    }
#ifdef DEBUG2R
    fprintf(stderr, "  recv %hu-byte block...", bs);
#endif
    /* TODO: it might be nice to avoid some of these reallocs... */
    if ((p = realloc(*bufp, bs)) == NULL) {
        return ((bs == 0) ? 0 : -1);
    }
    return recvall (fd, *bufp = p, bs);
}

ssize_t recvfile(int fdin, int fdout) {
    char c;

    if (read(fdin, &c, 1) != 1) {
        return -1;
    }
    if (c == 'F') {
#ifdef DEBUG1R
        fprintf(stderr, "Receiving file");
#endif
        recd_Z = DART_FALSE;
        return recvfile_common(fdin, fdout);
    } else if (c == 'Z') {
#ifdef DEBUG1R
        fprintf(stderr, "Receiving compressed file");
#endif
        recd_Z = DART_TRUE;
        return recvfileZ(fdin, fdout);
    }
    else return -1;
}

ssize_t recvfile_common(int fdin, int fdout) {
    ssize_t total = 0, len;
    char *buf = NULL;

    while (DART_TRUE) {
        len = recvblock(fdin, &buf);
        if (len < 0) {
	    return len;
	}
        if (len == 0) {
	    break;
	}
        total += len;
#ifdef DEBUG1R
        fprintf(stderr, "...%lu", total);
#endif
        if (total > MAX_FILE_SZ) {
	    return -1;
	}
        if (sendall(fdout, buf, len) != len) {
	    return -1;
	}
    }
#ifdef DEBUG1R
    fprintf(stderr, "...complete\n");
#endif
    return total;
}

#define OBUF_SZ (2 * DEF_BLOCK_SZ)
#define DARTBZ_CLEANUP do { free(inbuf); \
                            free(outbuf); \
                            BZ2LIB(bzDecompressEnd)(&bzs); } while(DART_FALSE)
#define RESET_OBUF \
    do { bzs.next_out = outbuf; bzs.avail_out = OBUF_SZ; } while (DART_FALSE)

ssize_t recvfileZ(int fdin, int fdout) {
    bz_stream bzs;
    int last_result, last_avail, endfl;
    char *outbuf, *inbuf = NULL;

    /* initialize */
    bzs.bzalloc = NULL; bzs.bzfree = NULL; bzs.opaque = NULL;
    if (BZ2LIB(bzDecompressInit)(&bzs, VERBOSITY, 0) != BZ_OK) {
        return -1;
    }
    if ((outbuf = malloc(OBUF_SZ)) == NULL) {
        BZ2LIB(bzDecompressEnd)(&bzs);
        return -1;
    }
    RESET_OBUF;
    endfl = DART_FALSE;

    /* process stream */
    do {
        bzs.avail_in = recvblock(fdin, &inbuf); /* receive a net block */
        bzs.next_in = inbuf;
        if (((long) (bzs.avail_in)) <= 0) {
            DARTBZ_CLEANUP;
            return -1;
        }
        do {  /* feed the block to the decompression stream */
            last_result = BZ2LIB(bzDecompress)(&bzs);
            last_avail = bzs.avail_out;
            if (last_result == BZ_STREAM_END) {
	        endfl = DART_TRUE;
	    } else if ( last_result != BZ_OK ) {
                DARTBZ_CLEANUP;
                return -1;
            }
            if ( (bzs.avail_out <= (OBUF_SZ / 2)) || endfl ) { 
                /* drain the output buffer */
                int t = 2 * DEF_BLOCK_SZ - bzs.avail_out;
                if (sendall(fdout, outbuf, t) != t) {
                    DARTBZ_CLEANUP;
                    return -1;
                }
                RESET_OBUF;
            }
	    /* 
	     * TODO: the loop control expression below leaves open the
	     *       possibility that bzDecompress will be called with no input
	     *       and when no output is waiting in the library.  Hopefully
	     *       that is not a problem.
	     */
        } while (((bzs.avail_in > 0) || (last_avail == 0)) && (!endfl));
#ifdef DEBUG1R
        fprintf(stderr, "...%u", bzs.TOTAL(in));
#endif
        if ( (bzs.TOTAL(in) > MAX_FILE_SZ) && !endfl ) {
            DARTBZ_CLEANUP;
            return -1;
        }
    } while (!endfl);

    /*
     * TODO: we should have consumed all of the last input block; if I can
     *       think of a good way to implement a check for that then I will put
     *       it in here 
     */
    /* There should be a final empty block */
    if (recvblock(fdin, &inbuf) != 0) {
        DARTBZ_CLEANUP;
        return -1;
    }

    /* all seems ok; clean up and exit */
    free (outbuf);
    BZ2LIB(bzDecompressEnd)(&bzs);

    return bzs.TOTAL(in);
}
#undef DARTBZ_CLEANUP
#undef RESET_OBUF
#undef OBUF_SZ

ssize_t recvlong(int fd, unsigned long int *l) {
    ssize_t sz;

    sz = read(fd, l, LINT_SZ);
#ifdef DEBUGBITS
    fprintf(stderr, "\nRecieved %ld bytes in recvlong\n", sz);
#endif
    *l = ntohl(*l);
    return sz;
}

ssize_t recvshort(int fd, unsigned short int *s) {
    ssize_t sz;

    sz = read(fd, s, SHINT_SZ);
#ifdef DEBUGBITS
    fprintf(stderr, "\nRecieved %ld bytes in recvshort\n", sz);
#endif
    *s = ntohs(*s);
    return sz;
}

ssize_t sendall (int fd, const char *buf, ssize_t len) {
    ssize_t n2w = len, nw;
    const char *p = buf;
#ifdef DEBUG3W
    fprintf(stderr, "  sendall %ld bytes...", len);
#endif
    while (n2w > 0) {
        if ((nw = write(fd, p, n2w)) < 0) {
	    return -1;
	}
        n2w-=nw; p+=nw;
#ifdef DEBUG4W
        fprintf(stderr, " %ld", n2w);
#endif
    }
    return len;
}

ssize_t sendblock(int fd, const char *buf, ssize_t len) {

#ifdef DEBUG2W
    fprintf(stderr, "  send %hu-byte block...", (unsigned short int) len);
#endif
    if (sendshort(fd, (unsigned short int) len) != SHINT_SZ) {
        return -1;
    }
    return sendall(fd, buf, len);
}

/*
   sendfile
   reads a file from file descriptor fdin, breaking it into blocks, and
   writes a series of <blocksize><block> records to file descriptor fdout.
   This is a pretty dumb protocol, depending on the underlying layer to
   handle all the details.  DO NOT USE THIS OVER A UDP CONNECTION!

   The file is preceded by an 'F' character; end-of-file is signified by
   a zero-sized block record (i.e. just a 0).
*/
ssize_t sendfile(int fdin, int fdout) {
    char *buf;

    if ((buf = (char *) malloc(DEF_BLOCK_SZ)) == NULL) {
        return -1;
    }
#ifdef DEBUG1W
    fprintf(stderr, "Sending file");
#endif
    *buf = 'F';
    return sendfile_common(fdin, fdout, buf);
}

ssize_t sendfile_common(int fdin, int fdout, char *buf) {
    ssize_t sz, total;

    if (write(fdout, buf, 1) != 1) {
        return -1;
    }
    total = 0;
    while (DART_TRUE) {
        sz = read(fdin, buf, DEF_BLOCK_SZ);
        if (sz < 0) {
	    return sz;
	}
        if (sz == 0) {
	    break;
	}
        if (sendblock(fdout, buf, sz) != sz) {
	    return -1;
	}
        total += sz;
#ifdef DEBUG1W
        fprintf(stderr, "...%ld", total);
#endif
    }
    if (sendshort(fdout, 0) != SHINT_SZ) {
        return -1;
    }
#ifdef DEBUG1W
    fprintf(stderr, "...complete\n");
#endif
    free(buf);
    return total;
}

#define IBUF_SZ DEF_BLOCK_SZ
#define OBUF_SZ (2 * DEF_BLOCK_SZ)
#define DARTBZ_CLEANUP do { \
        free(inbuf); free(outbuf); BZ2LIB(bzCompressEnd)(&bzs); \
    } while(DART_FALSE)
#define RESET_OBUF \
    do { bzs.next_out = outbuf; bzs.avail_out = OBUF_SZ; } while (DART_FALSE)
#define RESET_IBUF bzs.next_in = inbuf

ssize_t sendfileZ(int fdin, int fdout) {
    char *inbuf, *outbuf;
    int action, result, outlen;
    bz_stream bzs;

    /* initialize */
    if ((inbuf = (char *) malloc(IBUF_SZ)) == NULL) {
        return -1;
    }
    if ((outbuf = (char *) malloc(OBUF_SZ)) == NULL) {
        free(inbuf);
        return -1;
    }
    bzs.bzalloc = NULL; bzs.bzfree = NULL; bzs.opaque = NULL;
    if (BZ2LIB(bzCompressInit)(&bzs, DARTBZ_BLOCK, VERBOSITY, 0) != BZ_OK) {
        free(inbuf);
        free(outbuf);
        return -1;
    }
#ifdef DEBUG1W
    fprintf (stderr, "Sending compressed file");
#endif
    *outbuf = 'Z';
    if (write (fdout, outbuf, 1) != 1) {
        DARTBZ_CLEANUP;
        return -1;
    }
    RESET_OBUF;
    action = BZ_RUN;

    /* process */
    do {
        RESET_IBUF;
        bzs.avail_in = read(fdin, bzs.next_in, IBUF_SZ);
#ifdef DEBUG2W
        fprintf(stderr,"...%ld", bzs.avail_in);
#endif
        if (bzs.avail_in < 0) {
            DARTBZ_CLEANUP;
            return -1;
        } else if (bzs.avail_in == 0) action = BZ_FINISH;
        do {
            result = BZ2LIB(bzCompress)(&bzs, action);
            if ((result != ((action == BZ_RUN) ? BZ_RUN_OK : BZ_FINISH_OK))
                 && (result != BZ_STREAM_END) ) {
                DARTBZ_CLEANUP;
                return -1;
            }
            outlen = OBUF_SZ - bzs.avail_out;
            if ((outlen > (OBUF_SZ / 2)) || (result == BZ_STREAM_END)) {
                if (sendblock(fdout, outbuf, outlen) != outlen) {
                    DARTBZ_CLEANUP;
                    return -1;
                }
                RESET_OBUF;
#ifdef DEBUG1W
                fprintf(stderr, "...%ld", bzs.TOTAL(out));
#endif
            }
        } while (((action == BZ_RUN) && (bzs.avail_in > 0)) ||
                  ((action == BZ_FINISH) && (result != BZ_STREAM_END)) );
    } while (result != BZ_STREAM_END);
    if (sendshort(fdout, 0) != SHINT_SZ) {
        DARTBZ_CLEANUP;
        return -1;
    }
#ifdef DEBUG1W
    fprintf (stderr, "...complete\n");
#endif
    BZ2LIB(bzCompressEnd)(&bzs);
    free(outbuf);
    free(inbuf);
    return bzs.TOTAL(out);
}
#undef RESET_OBUF
#undef DARTBZ_CLEANUP
#undef OBUF_SZ
#undef IBUF_SZ

ssize_t sendlong(int fd, const unsigned long int l) {
    unsigned long int ll;
    ssize_t sz;

    ll = htonl(l);
    sz = write(fd, &ll, LINT_SZ);
#ifdef DEBUGBITS
    fprintf(stderr, "\nSent %ld of %d bytes in sendlong\n", sz, LINT_SZ);
#endif
    return sz;
}

ssize_t sendshort(int fd, const unsigned short int s) {
    unsigned short int ls;
    ssize_t sz;

    ls = htons(s);
    sz = write(fd, &ls, SHINT_SZ);
#ifdef DEBUGBITS
    fprintf(stderr, "\nSent %ld bytes in sendshort\n", sz);
#endif
    return sz;
}

