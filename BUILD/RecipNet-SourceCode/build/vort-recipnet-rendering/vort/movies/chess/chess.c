#include <stdio.h>
#include <math.h>

/*
 * Produces 36 frames of some chess pieces moving a round a chess board
 * with a bouncing ball on it.
 */

/*
 * How much the ball translates over 36 frames.
 */
float	T[] = { 0.000, 0.085, 0.340, 0.765, 1.360, 2.125, 3.060, 4.165,
		5.440, 6.885, 8.500, 8.542, 8.583, 8.625, 8.667, 8.708,
		8.750, 8.792, 8.833, 8.875, 8.917, 8.958, 9.000, 9.042,
		9.083, 8.500, 8.415, 8.160, 7.735, 7.140, 6.375, 5.440,
		4.335, 3.060, 1.615, 0.000
	};

/*
 * How much the ball scales over 36 frames.
 */
float S[36][3] = {
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.042, 0.958, 1.042},
	{1.083, 0.917, 1.083},
	{1.125, 0.875, 1.125},
	{1.167, 0.833, 1.167},
	{1.208, 0.792, 1.208},
	{1.250, 0.750, 1.250},
	{1.250, 0.750, 1.250},
	{1.208, 0.792, 1.208},
	{1.167, 0.833, 1.167},
	{1.125, 0.875, 1.125},
	{1.083, 0.917, 1.083},
	{1.042, 0.958, 1.042},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0},
	{1.0, 1.0, 1.0}
};


FILE	*fp;

main()
{
	float	yd, yr;
	int	i;
	char	buf[80];

	for (i = 1; i <= 36; i++) {
		sprintf(buf, "chess%02d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
			exit(1);
		}

		yd = 10.0 * (i - 1);
		yr = yd * 0.0174533;

		fprintf(fp, "lookat(0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 0.0)\n");
		fprintf(fp, "raysperpixel 1\n");
		fprintf(fp, "background 0.1,0.1,0.2\n");
		fprintf(fp, "\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	location (0.0, 1.0, 8.0)\n");
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
		fprintf(fp, "	vertex(7.0, -4.5, 7.0)\n");
		fprintf(fp, "	vertex(-7.0, -4.5, 7.0)\n");
		fprintf(fp, "	vertex(-7.0, -4.5, -7.0)\n");
		fprintf(fp, "	vertex(7.0, -4.5, -7.0)\n");
		fprintf(fp, "}\n");

		fprintf(fp, "polygon {\n");
		fprintf(fp, "	material 0.0, 0.05, 0.9, 20.0\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	reflectance 0.99\n");
		fprintf(fp, "	vertex(-9.0, -4.5, -10.0)\n");
		fprintf(fp, "	vertex(-9.0, 15.0, -10.0)\n");
		fprintf(fp, "	vertex(9.0, 15.0, -10.0)\n");
		fprintf(fp, "	vertex(9.0, -4.5, -10.0)\n");
		fprintf(fp, "}\n");

		fprintf(fp, "polygon {\n");
		fprintf(fp, "	material 0.0, 0.05, 0.9, 20.0\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	reflectance 0.99\n");
		fprintf(fp, "	vertex(10.0, -4.5, -9.0)\n");
		fprintf(fp, "	vertex(10.0, 15.0, -9.0)\n");
		fprintf(fp, "	vertex(10.0, 15.0, 9.0)\n");
		fprintf(fp, "	vertex(10.0, -4.5, 9.0)\n");
		fprintf(fp, "}\n");
		
		fprintf(fp, "\n");
		fprintf(fp, "sphere {\n");
		fprintf(fp, "	material 0.0, 0.3, 0.7, 20.0\n");
		fprintf(fp, "	colour 0.6, 0.7, 0.7\n");
		fprintf(fp, "	reflectance 0.85\n");
		fprintf(fp, "	transparency 0.4\n");
		fprintf(fp, "	radius 1.0\n");
		fprintf(fp, "	center (-3.0, 5.0, 3.0)\n");
		fprintf(fp, "	translate(0.0, %f, 0.0)\n", -T[i-1]);
		fprintf(fp, "        scale(%f, %f, %f)\n", S[i-1][0], S[i-1][1], S[i-1][2]);

		fprintf(fp, "}\n");

		fprintf(fp, "geometry {\n");
		fprintf(fp, "	phongshading on\n");
		fprintf(fp, "        colour 0.284, 0.309, 0.184\n");
		fprintf(fp, "        material 0.0, 0.25, 0.5, 6.0\n");
		fprintf(fp, "        backfacing on\n");
		fprintf(fp, "	scale(0.0015, 0.0015, 0.0015)\n");
		fprintf(fp, "        translate(%f, -4.5, %f)\n", 2.0 * cos(yr), 2.0 * sin(yr));

		fprintf(fp, "        offfile pawn20.geo\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "geometry {\n");
		fprintf(fp, "	phongshading on\n");
		fprintf(fp, "        colour 0.284, 0.309, 0.184\n");
		fprintf(fp, "        material 0.0, 0.25, 0.5, 6.0\n");
		fprintf(fp, "        backfacing on\n");
		fprintf(fp, "	scale(0.0015, 0.0015, 0.0015)\n");
		fprintf(fp, "        translate(%f, -4.5, %f)\n", 3.5 * cos(-yr), 3.5 * sin(-yr));

		fprintf(fp, "        offfile bishop20.geo\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "\n");
		fprintf(fp, "geometry {\n");
		fprintf(fp, "	phongshading on\n");
		fprintf(fp, "        colour 0.284, 0.309, 0.184\n");
		fprintf(fp, "        material 0.0, 0.25, 0.5, 6.0\n");
		fprintf(fp, "        backfacing on\n");
		fprintf(fp, "	scale(0.002, 0.002, 0.002)\n");
		fprintf(fp, "        rotate(%f, y)\n", yd);
		fprintf(fp, "        translate(0.0, -4.5, 0.0)\n");

		fprintf(fp, "        offfile knight20.geo\n");
		fprintf(fp, "}\n");
		fclose(fp);
	}
}
