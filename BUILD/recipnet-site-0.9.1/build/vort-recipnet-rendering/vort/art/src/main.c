#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <signal.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/file.h>
#include <unistd.h>
#include <sys/times.h>

#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern hlist	*trace();
extern void	shade();
extern time_t	time();

extern object	*treeinit();

FILE	*logfile;

/*
 * statistics stuff
 */
#ifdef STATS
static int	totnodes;
static int	totleafs;
static int	neverentered;
static int	maxfinaldepth;
static int	maxinleaf;
static int	mininleaf;
static int	emptynodes;
static int	numobjptrs;
#endif

#ifdef TIME_STATS
static float    prev_time;
static time_t  start_time, rprev_time, tstart_time;
static struct tms       buffer;
#endif

extern int	chatty;

/*
 * object list - defined in render.c
 */
extern object	*oblist, *treeobjs;

/*
 * frame id extension and frame number
 */
char	*frameid = "000";
int	frameno = 0;

/*
 * default name
 */
char	*defname = "pic.pix";

/*
 * temporary file pointer
 */
char	*tmpname;


static int	totobjs;

/*
 * debugging flag.
 */
int	debug = FALSE;

/*
 * usage
 *
 *	print out usage details for art.
 */
usage(s)
	char    *s;
{
	if (s != (char *)NULL)
		warning(s);

	fatal("usage: art [-v] file.scn x_res y_res [-x x1 x2] [-y y1 y2] [-f frameid] [-o outputname] [-n] [-D var=expr]\n");
}

/*
 * main driver
 */
main(ac, av)
	int	ac;
	char	*av[];
{
	int		indx, standardin, preprocess, nameset, tempfd;
	char		*p, name[100], fname[100], buf[100];
	FILE		*infile;
	int		i, fragment, xsize, ysize, xstart, xend, ystart, yend;
	image		*im;
	object		*o;
#ifdef LOSINGTRACK
	char		hostname[65], pathname[513];
#endif

	logfile = stdout;

	if (ac < 4)
		usage((char *)NULL);

#ifdef TIME_STATS
	times(&buffer);
	start_time = buffer.tms_utime + buffer.tms_stime;
#endif

	if (strcmp(av[1], "-v") == 0) {
		indx = 2;
		chatty = TRUE;
	} else {
		chatty = FALSE;
		indx = 1;
	}

	xsize = atoi(av[indx + 1]);
	ysize = atoi(av[indx + 2]);

	xstart = 0;
	xend = xsize - 1;

	ystart = ysize - 1;
	yend = 0;

	preprocess = TRUE;
	nameset = FALSE;
	standardin = FALSE;
	fragment = FALSE;
					/* check for other options */
	for (i = indx + 3; i < ac; i++) {
		if (strcmp(av[i], "-y") == 0) {
			if (av[i + 1] == (char *)NULL)
				usage("art: -y requires two numbers.\n");
			if (av[i + 2] == (char *)NULL)
				usage("art: -y requires two numbers.\n");
			ystart = ysize - atoi(av[i + 1]) - 1;
			yend = ysize - atoi(av[i + 2]) - 1;
			fragment = TRUE;
			i += 2;
		} else if (strcmp(av[i], "-x") == 0) {
			if (av[i + 1] == (char *)NULL)
				usage("art: -x requires two numbers.\n");
			if (av[i + 2] == (char *)NULL)
				usage("art: -x requires two numbers.\n");
			xstart = atoi(av[i + 1]);
			xend = atoi(av[i + 2]);
			fragment = TRUE;
			i += 2;
		} else if (strcmp(av[i], "-D") == 0) {
			if (av[i + 1] == (char *)NULL)
				usage("art: -D requires an assignment string.\n");
			doassignment(av[i + 1]);
			i += 1;
		} else if (strcmp(av[i], "-f") == 0) {
			if (av[i + 1] == (char *)NULL)
				usage("art: -f requires frame identifier.\n");
			frameid = av[i + 1];
			if ('0' <= frameid[0] && '9' >= frameid[0])
				frameno = atoi(frameid);
			i += 1;
		} else if (strcmp(av[i], "-o") == 0) {
			if (av[i + 1] == (char *)NULL)
				usage("art: -o requires name.\n");
			else {
				defname = av[i + 1];
				nameset = TRUE;
			}
			i += 1;
		} else if (strcmp(av[i], "-n") == 0) {
			preprocess = FALSE;
		} else {
			sprintf(buf, "art: unknown option %s.\n", av[i]);
			usage(buf);
		}
	}

	if (strcmp(av[indx], "-") == 0)
		standardin = TRUE;
		
	if (standardin || nameset) {
		strcpy(name, defname);
	} else {
		strcpy(name, av[indx]);
	}
	p = rindex(name, '.');
	if (p == (char *)NULL) {
                p = name + strlen(name);
		strcat(name, ".log");
	} else
		strcpy(p, ".log");
	if ((logfile = fopen(name, "w")) == NULL) {
		sprintf(buf, "art: unable to open %s for writing.\n", name);
		fatal(buf);
	}

#ifdef LOSINGTRACK
	gethostname(hostname, 64);
	getcwd(pathname, 512);
	fprintf(logfile, "Host: %s	Directory: %s\n", hostname, pathname);
	fflush(logfile);
#endif

	if (!standardin) {
		if (preprocess) {
			strcpy(fname, name);
			strcpy(rindex(fname, '.'), "XXXXXX");
			if ((tempfd = mkstemp(fname)) < 0) {
				sprintf(buf, "art: unable to open temporary file %s.\n", fname);
				fatal(buf);
			} else if ((infile = fdopen(tempfd, "w")) == NULL) {
				perror("art");
				fatal("");
                        }

			prepro(av[indx], infile);

			fclose(infile);
		} else
			strcpy(fname, av[indx]);
	} else
		infile = stdin;

	if (!standardin) {
		if ((infile = freopen(fname, "r", stdin)) == NULL) {
			sprintf(buf, "art: unable to open file %s.\n", av[indx]);
			fatal(buf);
		}
	}

	if (preprocess)
		unlink(fname);

	if (preprocess)
		tmpname = fname;
	else
		tmpname = (char *)NULL;

	strcpy(p, PIX_SUFFIX);

	imagebufsize(512);
	if ((im = openimage(name, "w")) == (image *)NULL) {
		sprintf(buf, "art: unable to open %s for writing\n", name);
		fatal(buf);
	}

	imagewidth(im) = xend - xstart + 1;
	imageheight(im) = ystart - yend + 1;

	imagedepth(im) = 24;

	imagedate(im) = time((time_t *)NULL);

	if (fragment) {
		imagefragment(im) = TRUE;
		imagexaddr(im) = xstart;
		imageyaddr(im) = ysize - 1 - ystart;
		imageorigwidth(im) = xsize;
		imageorigheight(im) = ysize;
	}

	render(infile, im, xsize, ysize, xstart, xend, ystart, yend);

#ifdef TIME_STATS
	printtime("Total time taken             :");
#endif

#ifdef STATS
	totobjs = 0;
	for (o = oblist; o != (object *)NULL; o = o->nxt)
		totobjs++;

	sprintf(buf, "Total number of objects           : %d\n", totobjs);
	message(buf);

	if (treeobjs != (object *)NULL && oblist->type == STREE) {
		mininleaf = totobjs;

		sprintf(buf, "Total number of tree nodes	: %d\n", totnodes);
		message(buf);
		sprintf(buf, "Number of leaf nodes not entered: %d\n", neverentered);
		message(buf);
		sprintf(buf, "Number of leaf nodes entered	: %d\n", totleafs);
		message(buf);
		sprintf(buf, "Number of object pointers	: %d\n", numobjptrs);
		message(buf);
		sprintf(buf, "Min number of objects in a leaf	: %d\n", mininleaf);
		message(buf);
		sprintf(buf, "Max number of objects in a leaf	: %d\n", maxinleaf);
		message(buf);
		sprintf(buf, "Max depth of a leaf node	: %d\n", maxfinaldepth);
		message(buf);
		sprintf(buf, "Number of empty nodes		: %d\n", emptynodes);
		message(buf);
	}
#endif

	fclose(logfile);

	exit(ALLOK);
}

#ifdef STATS
/*
 * getstats
 *
 *	calculate the statistics for the kd-tree pointed to by 
 * root.
 */
getstats(root)
	stree	*root;
{
	olist	*obs;
	int	count;

	totnodes++;

	if (root->type == SPLITABLELEAF)
		neverentered++;
	else if (root->type == LEAF) {
		if (maxfinaldepth < root->u.leaf.depth)
			maxfinaldepth = root->u.leaf.depth;
		if (root->u.leaf.oblist == (olist *)NULL)
			emptynodes++;
		else {
			count = 0;
			for (obs = root->u.leaf.oblist; obs != (olist *)NULL; obs = obs->nxt)
				count++;
			numobjptrs += count;
			if (mininleaf > count)
				mininleaf = count;
			if (maxinleaf < count)
				maxinleaf = count;
			totleafs++;
		}
	} else {
		getstats(root->u.branch.left);
		getstats(root->u.branch.right);
	}
}
#endif

#ifdef TIME_STATS
/*
 * printtime
 *
 *      print out how much time has been taken up.
 */
printtime(s)
	char    *s;
{
	float           diff;
	time_t          now;
	char            buf[BUFSIZ];

	times(&buffer);

	diff = buffer.tms_utime + buffer.tms_stime - start_time;

	sprintf(buf, "%s %.2f seconds.\n", s, diff / 60.0);       
	message(buf);
}

#endif


