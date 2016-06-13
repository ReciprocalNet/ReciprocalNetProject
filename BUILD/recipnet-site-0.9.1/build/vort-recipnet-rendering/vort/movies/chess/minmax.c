
#include <stdio.h>

#define MAX(x, y)	((x) > (y) ? (x) : (y))
#define MIN(x, y)	((x) < (y) ? (x) : (y))

/*
 * simple program to print out min and max coord vals of a polygon file
 */
main(ac, av)
	int	ac;
	char	**av;
{
	float		xmax, xmin, ymax, ymin, zmax, zmin;
	float		x, y, z;
	int		i, j, k, verts, connections, edges;
	FILE		*infile;
	char		name[80];

	xmax = ymax = zmax = -1.0e37;
	xmin = ymin = zmin = 1.0e37;

	infile = fopen(av[1], "r");

	while ((i = fgetc(infile)) != '\n' && i != EOF)	/* skip title */
			;
	fscanf(infile, "%d %d %d\n", &verts, &connections, &edges);

	for (i = 0; i < verts; i++) {
		fscanf(infile, "%f %f %f\n", &x, &y, &z);
		xmax = MAX(xmax, x);
		ymax = MAX(ymax, y);
		zmax = MAX(zmax, z);
		xmin = MIN(xmin, x);
		ymin = MIN(ymin, y);
		zmin = MIN(zmin, z);
	}

	fclose(infile);

	printf("%s: X(%.3f, %.3f), Y(%.3f, %.3f), Z(%.3f, %.3f)\n", av[1], xmin, xmax, ymin, ymax, zmin, zmax);
}

