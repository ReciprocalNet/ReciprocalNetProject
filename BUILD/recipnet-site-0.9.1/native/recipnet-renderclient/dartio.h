/*
 * Reciprocal Net project
 * @(#)dartio.h
 * 
 * 10-Oct-2000: jobollin wrote first draft
 * 06-Nov-2002: ekoperda incorporated file into recipnet source tree
 *
 * Header file for dartio.c
 */
#ifndef _DARTIO_H
#define _DARTIO_H

/* standard headers required */
#include <stdlib.h>
#include <unistd.h>
#define BZ_NO_STDIO
#include <bzlib.h>
#undef BZ_NO_STDIO

/*
 * default to using a fairly small compression block.  The hope is that the
 * whole block can be kept in cache for fast operation.  Allowable values
 * are 1 through 9, signifying the number of hundreds of kilobytes.
 */
#ifndef DARTBZ_BLOCK
#define DARTBZ_BLOCK 2
#endif

#ifdef DEBUG4R
#define DEBUG3R
#endif

#ifdef DEBUG3R
#define DEBUG2R
#endif

#ifdef DEBUG2R
#define DEBUG1R
#endif

#ifdef DEBUG4W
#define DEBUG3W
#endif

#ifdef DEBUG3W
#define DEBUG2W
#endif

#ifdef DEBUG2W
#define DEBUG1W
#endif

#if defined (DEBUG1R) || defined (DEBUG1W)
#include <stdio.h>
#endif

#ifndef VERBOSITY
#ifdef DEBUGBITS
#define VERBOSITY 2
#else
#define VERBOSITY 0
#endif
#endif

/* local definitions */
#define DART_TRUE (1 == 1)
#define DART_FALSE (1 == 0)
#define DEF_BLOCK_SZ 8096
#define MAX_BLOCK_SZ 65536
#define MAX_FILE_SZ 4194304
#define SOCK_BUF_SZ (MAX_BLOCK_SZ * 4)
#define DART_REQ_PORT 1994
#define SHINT_SZ sizeof(unsigned short int)
#define LINT_SZ sizeof(unsigned long int)

#ifndef INDARTIO
extern int recd_Z;
#endif

/* function prototypes */
ssize_t recvall(int fd, char *buf, ssize_t len);
ssize_t recvblock(int fd, char **block);
ssize_t recvfile(int fdin, int fdout);
ssize_t recvfile_common(int fdin, int fdout);
ssize_t recvfileZ(int fdin, int fdout);
ssize_t recvlong(int fd, unsigned long int *l);
ssize_t recvshort(int fd, unsigned short int *s);
ssize_t sendall(int fd, const char *buf, ssize_t len);
ssize_t sendblock(int fd, const char *block, ssize_t len);
ssize_t sendfile(int fdin, int fdout);
ssize_t sendfile_common(int fdin, int fdout, char *buf);
ssize_t sendfileZ(int fdin, int fdout);
ssize_t sendlong(int fd, const unsigned long int l);
ssize_t sendshort(int fd, const unsigned short int s);

/*
   versions of libbz2 from 1.0.0 up prepend "BZ2_" to all the API function
   names.  If you use one of those then define the symbol PREPEND_BZ2 to
   the preprocessor.  If using library version 0.9.5 or below, then don't.
*/
#ifdef BZ2V1
#define BZ2LIB(function) BZ2_ ## function
#define TOTAL(dir) total_ ## dir ## _lo32
#else
#define BZ2LIB(function) function
#define TOTAL(dir) total_ ## dir
#endif

#endif /* DARTIO */

