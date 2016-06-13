#include <stdio.h>
#include <math.h>

/*
 * 	Produces 36 frames of a crankshaft "bit" rotating around.
 */

FILE	*fp;

main()
{
	float	r = 0.8, g = 0.8, b = 0.8;
	float	kd = 0.4, ks = 0.6, kse = 10.0;
	float	t = 0.8, a = 1.8, a2 = 5.0, a3 = 1.0, a4 = 3.5;
	float	rb = 0.3;
	float	xr, yr;

	char	buf[40];
	int	i;

	for (i = 1; i <= 36; i++) {
		sprintf(buf, "crank%02d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
                        fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
                        exit(1);
                }

                xr = yr = 10.0 * (i - 1);

		fprintf(fp, "lookat(0.0, 0.0, 16.0, 0.0, 0.0, 0.0, 0.0)\n\n");
		fprintf(fp, "raysperpixel 4\n");
		fprintf(fp, "background 0.2, 0.2, 0.3\n\n");

		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (11.0, 11.0, 11.0)\n");
		fprintf(fp, "\tcolour 1.0, 1.0, 1.0\n");
		fprintf(fp, "}\n\n");

		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (-11.0, 11.0, 11.0)\n");
		fprintf(fp, "\tcolour 1.0, 1.0, 1.0\n");
		fprintf(fp, "}\n\n");

		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (1.0, 1.0, 16.0)\n");
		fprintf(fp, "\tcolour 1.0, 1.0, 1.0\n");
		fprintf(fp, "}\n\n");

		fprintf(fp, "polygon {\n");
		fprintf(fp, "\tvertex (-30.0, -30.0, -15.0)\n");
		fprintf(fp, "\tvertex (-30.0, 30.0, -15.0)\n");
		fprintf(fp, "\tvertex (30.0, 30.0, -15.0)\n");
		fprintf(fp, "\tvertex (30.0, -30.0, -15.0)\n");
		fprintf(fp, "\tcolour 0.2, 0.2, 0.3\n");
		fprintf(fp, "\tmaterial 0.0, 1.0, 0.0, 1\n");
		fprintf(fp, "\ttexture marble {\n");
		fprintf(fp, "\t\tblendcolor 0.0, 0.0, 0.0\n");
		fprintf(fp, "\t\tscalefactors 0.07, 6.2, 1.0\n");
		fprintf(fp, "\t}\n");
		fprintf(fp, "}\n\n");

		fprintf(fp, "csg {\n");
		fprintf(fp, "\trotate (%f, x)\n", xr);
		fprintf(fp, "\trotate (%f, y)\n\n", yr);

		fprintf(fp, "\tcylinder c1 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", 0.9999 * t);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", -t * 0.9999);
		fprintf(fp, "\t\tradius 9.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c2 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", t);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t);
		fprintf(fp, "\t\tradius 3.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c3 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (4.0, -3.0, %f)\n", t * 1.2);
		fprintf(fp, "\t\tcenter (4.0, -3.0, %f)\n", -t * 1.2);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c4 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (-4.0, -3.0, %f)\n", t * 1.2);
		fprintf(fp, "\t\tcenter (-4.0, -3.0, %f)\n", -t * 1.2);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c5 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (6.245, -5.0, %f)\n", t);
		fprintf(fp, "\t\tcenter (6.245, -5.0, %f)\n", -t);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c6 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (-6.245, -5.0, %f)\n", t);
		fprintf(fp, "\t\tcenter (-6.245, -5.0, %f)\n", -t);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c7 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + a);
		fprintf(fp, "\t\tradius 2.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c8 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		/*fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");*/
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + a + a2);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + a + a2 + a3);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c9 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + rb);
		fprintf(fp, "\t\tradius %f \n", 2.0 + rb);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c10 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t - a4);
		fprintf(fp, "\t\tradius %f \n", 2.0);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcylinder c11 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t - rb);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t);
		fprintf(fp, "\t\tradius %f \n", 2.0 + rb);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\ttorus t1 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + rb);
		fprintf(fp, "\t\tradii %f, %f \n", 2.0 + rb, rb);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\ttorus t2 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 6.0, %f)\n", -t - rb);
		fprintf(fp, "\t\tradii %f, %f \n", 2.0 + rb, rb);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b1 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (3.0, -4.0, %f)\n", t);
		fprintf(fp, "\t\tvertex (9.0, 7.0, %f)\n", -t);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b2 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (-3.0, -4.0, %f)\n", t);
		fprintf(fp, "\t\tvertex (-9.0, 7.0, %f)\n", -t);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b3 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (6.245, -5.625, %f)\n", t * 1.1);
		fprintf(fp, "\t\tvertex (9.1, -3.9, %f)\n", -t * 1.1);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b4 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (-6.245, -5.625, %f)\n", t * 1.1);
		fprintf(fp, "\t\tvertex (-9.1, -3.9, %f)\n", -t * 1.1);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b5 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (-8.0, 6.0, %f)\n", t + 0.01);
		fprintf(fp, "\t\tvertex (8.0, 10.0, %f)\n", -t - 0.01);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b6 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (3.0, -4.0, %f)\n", t + 0.01);
		fprintf(fp, "\t\tvertex (4.1, -2.9, %f)\n", -t - 0.01);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tbox b7 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tvertex (-3.0, -4.0, %f)\n", t + 0.01);
		fprintf(fp, "\t\tvertex (-4.1, -2.9, %f)\n", -t - 0.01);
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcone cone1 {\n");
		fprintf(fp, "\t\tmaterial 0.0, %f, %f, %.1f\n", kd, ks, kse);
		fprintf(fp, "\t\tcolour %f, %f, %f\n", r, g, b);
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + a + a2);
		fprintf(fp, "\t\tradius 1.0\n");
		fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", t + a);
		fprintf(fp, "\t\tradius 2.0\n");
		fprintf(fp, "\t}\n\n");

		fprintf(fp, "\tcone1 + c1 - b1 - b2 + (b6 - c3) + (b7 - c4) - b3 - b4 - b5\n");
		fprintf(fp, "\t+ c5 + c6 + c2 + c7 + c8 + (c9 - t1) + c10 + (c11 - t2)\n");
		fprintf(fp, "}\n");
		fclose(fp);
	}
}
