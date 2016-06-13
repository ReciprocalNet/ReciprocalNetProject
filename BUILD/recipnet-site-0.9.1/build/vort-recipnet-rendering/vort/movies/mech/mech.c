#include <stdio.h>
#include <math.h>

/*
 * Makes 36 frames of a simple four-bar mechanism.
 *
 */

float	W = 0.02, R1 = 0.06, R2 = 0.02;

float	r, l, d, c, l2;
float	x1, y11, x0, y00;
float	epsi();

#define	RAD2DEG	57.29577951308
#define	DEG2RAD	0.017453292519
#define	MY_PI	3.141592653589

FILE	*fp;


main()
{
	float	theta, gamma, bonk;
        char    buf[40];
        int     i;

	r = 0.3;
	l = 0.7555;
	d = 0.8;
	c = 0.4;
	l2 = 1.0;

        for (i = 1; i <= 36; i++) {
                sprintf(buf, "mech%02d.scn", i);
                if ((fp = fopen(buf, "w")) == NULL) {
                        fprintf(stderr, "Couldn't open '%s' for writing\n", buf);
                        exit(1);
                }

		x1 = y11 = x0 = y00 = 0.0;

		fprintf(fp, "lookat (0.5, 0.5, 0.8, 0.4, 0.0, 0.0, 0.0)\n");
		fprintf(fp, "raysperpixel 4\n");
		fprintf(fp, "background 0.3, 0.0, 0.3\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (-1.5, 0.3, 0.0)\n");
		fprintf(fp, "\tcolour 0.8, 0.8, 0.8\n");
		fprintf(fp, "}\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (-0.1, 0.3, -0.7)\n");
		fprintf(fp, "\tcolour 0.5, 0.5, 0.5\n");
		fprintf(fp, "}\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (0.5, 0.5, 0.8)\n");
		fprintf(fp, "\tcolour 0.9, 0.9, 0.9\n");
		fprintf(fp, "}\n");
		fprintf(fp, "light {\n");
		fprintf(fp, "\tlocation (2.2, 1.0, 0.0)\n");
		fprintf(fp, "\tcolour 0.8, 0.8, 0.8\n");
		fprintf(fp, "}\n");
	
		fprintf(fp, "polygon {\n");
		fprintf(fp, "\tcolour 0.3, 0.5, 0.7\n");
		fprintf(fp, "\tmaterial 0.0, 0.4, 0.6, 6\n");
		fprintf(fp, "\treflectance 0.7\n");
		fprintf(fp, "\tvertex (-0.5, -0.5, 0.5)\n");
		fprintf(fp, "\tvertex (-0.5, -0.5, -0.5)\n");
		fprintf(fp, "\tvertex (1.0, -0.5, -0.5)\n");
		fprintf(fp, "\tvertex (1.0, -0.5, 0.5)\n");
		fprintf(fp, "}\n");
		fprintf(fp, "csg {\n");
		theta = 10.0 * (i - 1) * DEG2RAD;
		make_r_bar(theta);
		bonk = epsi(theta);
		make_c_bar(bonk);
		make_l_bar();
	
		fprintf(fp, "\tb1 + b2 + b3 + c1 + c2 + c3 + c4 + c5 + c6 - c7 + pin1 + pin2 + pin3 + pin4 + pin5\n");
		fprintf(fp, "}\n");
		fclose(fp);
	}
}

float
epsi(theta)
	float	theta;
{
	float	s = 1.0;
	float	a, b, e, w, p, q;
	float	cosine;
	
	a = d * d + c * c  + r * r - l * l - 2.0 * r * d * cos(theta);
	b = 2.0 * c * d - 2.0 * c * r * cos(theta);
	e = -2.0 * c * r * sin(theta);
	w = a * a - e * e;
	p = b * b + e * e;
	q = 2.0 * a * b;
	if (theta > MY_PI)
		s = -1.0;

	cosine = (-q + s * sqrt(fabs(q * q - 4.0 * w * p))) / (2.0 * p);
	if (cosine > 1.0)
		cosine = 1.0;
	if (cosine < -1.0)
		cosine = -1.0;
	return (acos(cosine));
}

make_r_bar(theta)
	float	theta;
{
	float	a, b;

	fprintf(fp, "\tcylinder c1 {\n");
	fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", -W);
	fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");
	fprintf(fp, "\tcylinder pin1 {\n");
	fprintf(fp, "\t\tcolour 1.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", W);
	fprintf(fp, "\t\tcenter (0.0, 0.0, %f)\n", 3 * W);
	fprintf(fp, "\t\tradius %f\n", R2);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tbox b1 {\n");
	fprintf(fp, "\t\tcolour 0.5, 0.4, 1.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\treflectance 0.65\n");
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", 0.0, R1, -W);
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", r, -R1, W);
	fprintf(fp, "\t\trotate(%f, z)\n", theta * RAD2DEG);
	fprintf(fp, "\t}\n");


	fprintf(fp, "\tcylinder c2 {\n");
	fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, -W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");
	fprintf(fp, "\tcylinder pin2 {\n");
	fprintf(fp, "\t\tcolour 1.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, -6 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11,  6 * W);
	fprintf(fp, "\t\tradius %f\n", R2);
	fprintf(fp, "\t}\n");

	x0 = r * cos(theta);
	y00 = r * sin(theta);
}

make_l_bar()
{
	float	gamma;
	float	a, b;
	float	cosg;

	a = x1 - x0;

	cosg = (a == 0.0 ? 0.0 : a / l);

	if (cosg > 1.0)
		cosg = 1.0;
	if (cosg < -1.0)
		cosg = -1.0;

	gamma = acos(cosg);

	fprintf(fp, "\tcylinder c3 {\n");
	fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x0, y00, W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x0, y00, 3 * W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tbox b2 {\n");
	fprintf(fp, "\t\tcolour 1.0, 0.4, 0.4\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\treflectance 0.65\n");
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", 0.0, R1, W);
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", l, -R1, 3 * W);
	fprintf(fp, "\t\trotate(%f, z)\n", gamma * RAD2DEG);
	fprintf(fp, "\t\ttranslate(%f, %f, 0.0)\n", x0, y00);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tcylinder c4 {\n");
	fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, 3 * W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");
	fprintf(fp, "\tcylinder pin3 {\n");
	fprintf(fp, "\t\tcolour 1.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, -3 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, 4 * W);
	fprintf(fp, "\t\tradius %f\n", R2);
	fprintf(fp, "\t}\n");
	x0 = x0 + l * cos(gamma);
	y00 = y00 + l * sin(gamma);
}

make_c_bar(epsi)
	float	epsi;
{
	float	a, b;

	fprintf(fp, "\tcylinder c5 {\n");
	fprintf(fp, "\t\tcolour 0.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d, 0.0, -W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d, 0.0, W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");
	fprintf(fp, "\tcylinder pin4 {\n");
	fprintf(fp, "\t\tcolour 1.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d, 0.0, -3 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d, 0.0, 3 * W);
	fprintf(fp, "\t\tradius %f\n", R2);
	fprintf(fp, "\t}\n");


	x1 = l2 * cos(epsi);
	y11 = l2 * sin(epsi);

	fprintf(fp, "\tbox b3 {\n");
	fprintf(fp, "\t\tcolour 0.5, 0.4, 1.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\treflectance 0.65\n");
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", 0.0, R1, -W);
	fprintf(fp, "\t\tvertex (%f, %f, %f)\n", l2, -R1, W);
	fprintf(fp, "\t\trotate(%f, z)\n", epsi * RAD2DEG);
	fprintf(fp, "\t\ttranslate(%f, %f, 0.0)\n", d, 0.0);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tcylinder c6 {\n");
	fprintf(fp, "\t\tcolour 1.0, 0.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d + x1, y11, -4 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d + x1, y11, 4 * W);
	fprintf(fp, "\t\tradius %f\n", 2.5 * R1);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tcylinder c7 {\n");
	fprintf(fp, "\t\tcolour 1.0, 0.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d + x1, y11, -5.1 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", d + x1, y11, 5.1 * W);
	fprintf(fp, "\t\tradius %f\n", R1);
	fprintf(fp, "\t}\n");


	x1 = d + c * cos(epsi);
	y11 = c * sin(epsi);
	fprintf(fp, "\tcylinder pin5 {\n");
	fprintf(fp, "\t\tcolour 1.0, 1.0, 0.0\n");
	fprintf(fp, "\t\tmaterial 0.0, 0.4, 0.6, 6\n");
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, -2 * W);
	fprintf(fp, "\t\tcenter (%f, %f, %f)\n", x1, y11, 3 * W);
	fprintf(fp, "\t\tradius %f\n", R2);
	fprintf(fp, "\t}\n");
}
