#include <stdio.h>
#include <math.h>

FILE	*fp;

main()
{
	float	a;
	int	i;
	char	buf[20];

        for (i = 1; i <= 36; i++) {
                sprintf(buf, "superq%02d.scn", i);
                if ((fp = fopen(buf, "w")) == NULL) {
                        fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
                        exit(1);
                }

                a = 10.0 * (i - 1);

		fprintf(fp, "\n");
		fprintf(fp, "lookat(0.0, 7.0, 9.0, 0.0, 6.0, 0.0, 0.0)\n");
		fprintf(fp, "background 0.2, 0.2, 0.3\n");
		fprintf(fp, "\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	location (0.0, 6.0, 7.0)\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	location (6.0, 6.0, 10.0)\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "superquadric {\n");
		fprintf(fp, "	rotate (%f, x)\n", a);
		fprintf(fp, "	rotate (%f, y)\n", a);
		fprintf(fp, "	translate (-3.7, 4.0, 0.0)\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	ambient 0.1, 0.1, 0.1\n");
		fprintf(fp, "	material 0.0, 0.75, 0.25, 5.0\n");
		fprintf(fp, "	vertex (3.0, 4.0, 3.0)\n");
		fprintf(fp, "	vertex (-3.0, -4.0, -3.0)\n");
		fprintf(fp, "	order 4.0\n");
		fprintf(fp, "	texture marble {\n");
		fprintf(fp, "		blendcolour 0.2, 0.2, 0.2\n");
		fprintf(fp, "		scalefactors 2.0, 30.0, 1.0\n");
		fprintf(fp, "	}\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "superquadric {\n");
		fprintf(fp, "	rotate (%f, x)\n", a);
		fprintf(fp, "	rotate (%f, y)\n", a);
		fprintf(fp, "	translate (3.7, 4.0, 0.0)\n");
		fprintf(fp, "	colour 0.815, 0.5, 0.278\n");
		fprintf(fp, "	ambient 0.1, 0.1, 0.1\n");
		fprintf(fp, "	material 0.0, 0.75, 0.25, 5.0\n");
		fprintf(fp, "	vertex (3.0, 4.0, 3.0)\n");
		fprintf(fp, "	vertex (-3.0, -4.0, -3.0)\n");
		fprintf(fp, "	order 4.0\n");
		fprintf(fp, "        texture wood {\n");
		fprintf(fp, "		blendcolor 0.4, 0.25, 0.12\n");
		fprintf(fp, "		scalefactors 40.0, 20.0, 2.0\n");
		fprintf(fp, "	}\n");
		fprintf(fp, "}\n");
		fprintf(fp, "\n");
		fprintf(fp, "polygon {\n");
		fprintf(fp, "	colour 1.0, 1.0, 1.0\n");
		fprintf(fp, "	material 0.0, 0.75, 0.25, 5.0\n");
		fprintf(fp, "	reflectance 0.5\n");
		fprintf(fp, "	vertex (-12.0, -3.0, 12.0)\n");
		fprintf(fp, "	vertex (-12.0, -3.0, -12.0)\n");
		fprintf(fp, "	vertex (12.0, -3.0, -12.0)\n");
		fprintf(fp, "	vertex (12.0, -3.0, 12.0)\n");
		fprintf(fp, "}\n");
		fclose(fp);
	}
}
