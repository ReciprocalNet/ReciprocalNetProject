/*
 * Reciprocal Net project
 *
 * ortwrap.c
 * 
 *    ca. 1996: jobollin wrote first draft
 * 01-Nov-2002: ekoperda incorporated file into recipnet source tree
 * 14-Nov-2006: jobollin included stdlib.h to get a declaration of exit()
 *
 * This is a simple command-line wrapper for the ORTEP software.  When linked 
 * with ORTEP, this program will interpret command-line options from the user 
 * and invoke ORTEP's Fortran subroutines appropriately.
 */
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

void userr(char *prog) {
     fprintf(stderr, "Usage: %s [-ps|-hp] [-o=(p|P|1|l|L|2)] [-l=(float)]"
                     " [filename]\n", prog);
     exit(1);
}

int main (int argc, char *argv[]) {
    long val=2, val2=0, iorien=0, ipght=0;
    int counter;
    char nofile[1] = "\0";
    char *filename = nofile, *prog = strrchr(argv[0], '/');
    float tempf = 0.0;

    if (prog) {
        prog ++;
    } else {
        prog = argv[0];
    }
    for (counter = 1; counter < argc; counter++) {
        if (!strncasecmp(argv[counter], "-ps", 3)) {
            /* do nothing */
        } else if (!strncasecmp(argv[counter], "-hp", 3)) {
            val = 3;
        } else if (!strncasecmp(argv[counter], "-o=", 3)) {
            switch (argv[counter][3]) {
              case 'p':
              case 'P':
              case '1':
                iorien=1;
                break;
              case 'l':
              case 'L':
              case '2':
                iorien=2;
		break;
	    }
	} else if (!strncasecmp(argv[counter], "-l=", 3)) {
            sscanf(argv[counter] + 3, "%f", &tempf);
            ipght = tempf * 1000;
	} else if (argv[counter][0] == '-') {
	    userr(prog);
	} else {
            filename = argv[counter];
            val2 = strlen(filename);
	}
    }
    ortepmain_(filename, &val2, &val, &iorien, &ipght);
    exit(0);
}
