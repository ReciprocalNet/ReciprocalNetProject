#include <stdio.h>
#include <math.h>
#include "random.h"

#define	NUMFRAMES	36

FILE	*fp;

int	numsph, ran = 0, numpts = (sizeof(randtable) / sizeof(float));
float	*rx, *ry, *rz, *rr, *rred, *rgreen, *rblue;

main(argc, argv)
	int	argc;
	char	**argv;
{
	int	i, j;
	char	buf[80];

	if (argc == 2)
		numsph = atoi(argv[1]);
	else
		numsph = 29;

	if  ((rx = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((ry = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((rz = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((rr = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((rred = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((rgreen = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	if  ((rblue = (float *)malloc(numsph * sizeof(float))) == NULL) {
		fprintf(stderr, "Not enough mem\n");
		exit(1);
	}

	makespheres();

	for (i = 1; i <= NUMFRAMES; i++) {
		sprintf(buf, "s%02d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
			exit(1);
		}

		fprintf(fp, "lookat(0.0, 0.0, 20.0, 0.0, 0.0, 0.0, 0.0)\n");
		fprintf(fp, "raysperpixel 4\n");
		fprintf(fp, "background 0.1,0.1,0.2\n");
		fprintf(fp, "\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	location (0.0, 0.0, 20.0)\n");
		fprintf(fp, "}\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	location (-5.0, 5.0, 6.0)\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");

		fprintf(fp, "polygon {\n");
		fprintf(fp, "	material 0.0, 0.65, 0.35, 20.0\n");
		fprintf(fp, "	reflectance 0.65\n");
		fprintf(fp, "	tile tile.pix size 0.25, 0.25\n");
		fprintf(fp, "	vertex(12.0, -12.5, 12.0)\n");
		fprintf(fp, "	vertex(-12.0, -12.5, 12.0)\n");
		fprintf(fp, "	vertex(-12.0, -12.5, -12.0)\n");
		fprintf(fp, "	vertex(12.0, -12.5, -12.0)\n");
		fprintf(fp, "}\n");

		fprintf(fp, "composite {\n");
#ifdef ROTATE
		fprintf(fp, "rotate (%f, x)\n", 10.0 * i);
		fprintf(fp, "rotate (%f, y)\n", 10.0 * i);
		fprintf(fp, "rotate (%f, z)\n", 10.0 * i);
#endif 
		fprintf(fp, "\tambient 0.3, 0.3, 0.3\n");
		fprintf(fp, "\tmaterial 0.0, 0.25, 0.75, 10.0\n");

		movespheres(i);

		fprintf(fp, "}\n");

		fclose(fp);
	}
}

/*
 * Initially make the spheres (ie. their location, radius and color)
 */
makespheres()
{
	int	i;

	for (i = 0; i < numsph; i++) {
		rx[i] = -10 + 20 * randtable[ran++ % numpts];
		ry[i] = -10 + 20 * randtable[ran++ % numpts];
		rz[i] = -10 + 20 * randtable[ran++ % numpts];
		rr[i] = 0.5 + randtable[ran++ % numpts];
		rred[i] = randtable[ran++ % numpts];
		rgreen[i] = randtable[ran++ % numpts];
		rblue[i] = randtable[ran++ % numpts];
	}

}
	
/*
 * move the spheres according to the frame number i
 * then print them out.
 */
movespheres(i)
	int	i;
{
	int	j;
	float	val = 3.1416 * i / 18.0;

	val = 1.5 * sin(val);

	for (j = 1; j < numsph; j++) {
		fprintf(fp, "\tsphere {\n\t\tcenter (%.3f, %.3f, %.3f)\n",
			rx[j] + val, ry[j] + val, rz[j] + val);
		fprintf(fp, "\t\tradius %f\n\t\tcolor %.2f, %.2f, %.2f\n\t}\n",
			rr[j], rred[j], rgreen[j], rblue[j]);
	}
}
