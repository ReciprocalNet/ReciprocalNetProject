#include <stdio.h>
#include <math.h>

FILE	*fp;

main()
{
	float	r;
	int	i;
	char	buf[80];

	for (i = 1; i <= 36; i++) {
		sprintf(buf, "cyl%02d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
			exit(1);
		}

		r = 10.0 * (i - 1);

		fprintf(fp, "lookat(0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 0.0)\n");
		fprintf(fp, "raysperpixel 4\n");
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

		fprintf(fp, "cylinder {\n");
		fprintf(fp, "	rotate(%f, x)\n", r);
		fprintf(fp, "	rotate(%f, y)\n", r);
		fprintf(fp, "	rotate(%f, z)\n", r);
		fprintf(fp, "	material 0.0, 1.0, 0.0, 0.0\n");
		fprintf(fp, "	colour 0.7, 0.7, 0.7\n");
		fprintf(fp, "	center (1.0, 3.0, 0.0)\n");
		fprintf(fp, "	center (1.0, -3.0, 0.0)\n");
		fprintf(fp, "	texture marble {\n");
		fprintf(fp, "		scale(0.2, 0.2, 0.2)\n");
		fprintf(fp, "		blendcolour 0.2, 0.2, 0.2\n");
		fprintf(fp, "		scalefactors 5.0, 10.0, 1.0\n");
		fprintf(fp, "	}\n");
		fprintf(fp, "}\n");

		fprintf(fp, "polygon {\n");
		fprintf(fp, "	material 0.0, 0.65, 0.35, 20.0\n");
		fprintf(fp, "	reflectance 0.65\n");
		fprintf(fp, "	tile tile.pix size 0.25, 0.25\n");
		fprintf(fp, "	vertex(7.0, -4.5, 7.0)\n");
		fprintf(fp, "	vertex(-7.0, -4.5, 7.0)\n");
		fprintf(fp, "	vertex(-7.0, -4.5, -7.0)\n");
		fprintf(fp, "	vertex(7.0, -4.5, -7.0)\n");
		fprintf(fp, "}\n");
	}
}
