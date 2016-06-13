#include <unistd.h>
#include "readall.h"

/*
 * readall
 *
 * read exactly count characters from file descriptor fd.  if the return value
 * is different from the requested size then an error is indicated; the exact
 * error may be determined by checking errno.
 */
ssize_t readall (int fd, char *buf, size_t count) {
    size_t n2r, nr = 0;
    for (n2r = count; n2r > 0; n2r -= nr) {
        if ( (nr = read(fd, buf, n2r)) < 0 ) break;
        else buf += nr;
    }
    return count - n2r;
}

