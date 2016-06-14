/*
 * Convert NFF files to art (Version 2 art that is) input files
 * usage: nff2art [-g [xgrid ygrid zgrid]] [-s x.x] < infile > outfile
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/*
 * The current colour
 */
float	R, G, B;
float	aR, aG, aB;
float	bR, bG, bB;

/*
 * Material properties
 */
float	Kd, Ks, Ksexp, Ir;
float	Refl = 0.0, Trans;

/*
 * A global  scale factor
 */
float	s = 1.0;

char	buf[128];

main(argc, argv)
	int	argc;
	char	**argv;
{
	int	grid, c, gx, gy, gz;
	float	x, y, z;

	grid = 0;

	for (c = 1; c < argc; c++)
		if (argv[c][0] == '-' && argv[c][1] == 'g') {
			printf("subdivision grid");
			grid = 1;
			if (argc >= 5) {
				gx = atof(argv[++c]);
				gy = atof(argv[++c]);
				gz = atof(argv[++c]);
				printf(" %d, %d, %d {\n", gx, gy, gz);
			} else {
				printf(" 20, 20, 20 {\n");
			}
		} else if (argv[c][0] == '-' && argv[c][1] == 's') {
			s = atof(argv[++c]);
		}

	printf("screensize 0,0\n");
	printf("maxhitlevel 4\n");

	while ((c = getchar()) != EOF) {
		switch (c) {
		case '\t':
			break;

		case 'v':
			view();
			break;

		case 'l':	/* Lights */
			read_three("%g %g %g\n", &x, &y, &z);
			x *= s;
			y *= s;
			z *= s;

			printf("light {\n");
			printf("\tcolor 0.5, 0.5, 0.5\n");
			printf("\tlocation (%f, %f, %f)\n", x, y, z);
			printf("}\n\n");
			break;

		case 'b':	/* Background color */
			read_three("%g %g %g\n", &bR, &bG, &bB);
			printf("background %g, %g, %g\n\n", bR, bG, bB);

			break;

		case 'f':	/* Fill color and shading stuff */
			if (scanf("%g %g %g %g %g %g %g %g\n", 
				&R, &G, &B, 
				&Kd, &Ks, &Ksexp,
				&Trans, &Ir
			) != 8)
				thunk("what happened");

			/*
			 * Give us some ambient...
			 */
			aR = 0.05 * R;
			aG = 0.05 * G;
			aB = 0.05 * B;

			/*
			 * Set Refl to min(Ks, 1 - T) as Craig Kolb does
			 * in his nff2rayshade awk script.
			 */
			Refl = Ks;
			if (1.0 - Trans < Ks)
				Refl = 1.0 - Trans;

			printf("material %f, %f, %f, %f\n", Ir, Kd, Ks, Ksexp);
			printf("reflectance %f\n", Refl);
			printf("transparency %f\n", Trans);
			printf("color %f, %f, %f\n", R, G, B);
			printf("ambient %f, %f, %f\n\n", aR, aG, aB);
			break;
		case 'c':	/* Cylinder or Cone */
			cone();
			break;

		case 's':	/* Sphere */
			sphere();
			break;

		case 'p':	/* Polygon or polygonal patch */
			c = getchar();
			if (c == 'p')
				polypatch();
			else
				poly();

			break;

		case '#':	/* Comment */
			printf("/* ");
			while ((c = getchar()) != '\n' && c != EOF)
				putchar(c);

			printf(" */\n");

				if (c == EOF)
					exit(0);

			break;

		default:
			sprintf(buf, "Unknown  key character '%c'.", c);
			thunk(buf);
		}
	}

	if (grid)
		printf("}\n");

	
	exit(0);
}

/*
 * view
 *
 *	Read in and write the viewing parameters
 */
view()
{
	float	Fx, Fy, Fz;
	float	Ax, Ay, Az;
	float	Ux, Uy, Uz;
	float	angle;
	float	hither, yon;
	int	xres, yres;

	read_three("\nfrom %g %g %g", &Fx, &Fy, &Fz);
	read_three("\nat %g %g %g", &Ax, &Ay, &Az);
	read_three("\nup %g %g %g", &Ux, &Uy, &Uz);

	if (scanf("\nangle %g", &angle) != 1)
		thunk("wanted a number for fov");

	if (scanf("\nhither %g", &hither) != 1)
		thunk("wanted a number for hither");

	if (scanf("\nresolution %d %d\n", &xres, &yres) != 2)
		thunk("wanted two numbers for resolution");

	printf("up (%g, %g, %g)\n", Ux, Uy, Uz);

	Fx *= s;
	Fy *= s;
	Fz *= s;

	Ax *= s;
	Ay *= s;
	Az *= s;

	printf("lookat (%f, %f, %f, %f, %f, %f, 0.0)\n",
		Fx, Fy, Fz, Ax, Ay, Az);

	printf("fieldofview %g\n\n", angle);

}

/*
 * sphere
 *
 *	Read and write a sphere
 */
sphere()
{
	float	x, y, z, r;

	read_four("%g %g %g %g\n", &x, &y, &z, &r);

	x *= s;
	y *= s;
	z *= s;
	r *= s;
	
	printf("sphere {\n");
	printf("\tcenter (%f, %f, %f)\n", x, y, z);
	printf("\tradius  %f\n", r);

	printf("}\n");
}

/*
 * cone
 *
 *	Read in and write a cone or a cylinder
 *
 */
cone()
{
	float	apex_x, apex_y, apex_z, apex_r;
	float	base_x, base_y, base_z, base_r;

	read_four("%g %g %g %g\n", &base_x, &base_y, &base_z, &base_r);
	read_four("%g %g %g %g\n", &apex_x, &apex_y, &apex_z, &apex_r);

	base_x *= s;
	base_y *= s;
	base_z *= s;
	base_r *= s;

	apex_x *= s;
	apex_y *= s;
	apex_z *= s;
	apex_r *= s;

	if (fabs(apex_r - base_r) < 1.0e-10) {
		printf("cylinder {\n");
		printf("\tcenter (%f, %f, %f)\n", base_x, base_y, base_z);
		printf("\tcenter (%f, %f, %f)\n", apex_x, apex_y, apex_z);
		printf("\tradius %f\n", base_r);
	} else {
		printf("cone {\n");
		printf("\tcenter (%f, %f, %f)\n", base_x, base_y, base_z);
		printf("\tradius %f\n", base_r);
		printf("\tcenter (%f, %f, %f)\n", apex_x, apex_y, apex_z);
		printf("\tradius %f\n", apex_r);
	}

	printf("}\n");
}

/*
 * poly
 *
 * 	Read in and write a polygon.
 */
poly()
{
	float	x, y, z;
	int	i, nv;

	if (scanf("%d\n", &nv) != 1)
		thunk("wanted an integer for number of verticies");

	if (nv <= 0)
		thunk("p: silly value for number of verticies");
	
	printf("polygon {\n");

	for (i = 0; i < nv; i++) {
		read_three("%g %g %g\n", &x, &y, &z);
		x *= s;
		y *= s;
		z *= s;
		printf("\tvertex (%f, %f, %f)\n", x, y, z);
	}

	printf("}\n");
}

/*
 * polypatch
 *
 *	Read in and write a "poly patch" thingo (ie a polygon with
 *	vertex normals)
 */
polypatch()
{
	float	x, y, z, nx, ny, nz;
	int	i, nv;

	if (scanf("%d\n", &nv) != 1)
		thunk("wanted an integer for number of verticies");

	if (nv <= 0)
		thunk("p: silly value for number of verticies");
	
	printf("polygon {\n");

	for (i = 0; i < nv; i++) {
		read_six("%g %g %g %g %g %g\n", &x, &y, &z, &nx, &ny, &nz);
		x *= s;
		y *= s;
		z *= s;
		printf("\tvertex (%f, %f, %f), (%f, %f, %f)\n", x, y, z, nx, ny, nz);
	}

	printf("}\n");
}

/*
 * thunk
 *
 *	Go thunk! (die)
 */
thunk(s)
	char	*s;
{
	fprintf(stderr, "%s\n", s);
	exit(1);
}

/*
 * We seem to be reading lots of sets of 3 or 4 or 6 numbers....
 */
read_three(f, a, b, c)
	char	*f;
	float	*a, *b, *c;
{
	int	n;

	if ((n = scanf(f, a, b, c)) != 3) {
		sprintf(buf, "expected to read 3 numbers with '%s' format but got %d\n", f, n);
		thunk(buf);
	}

}

read_four(f, a, b, c, d)
	char	*f;
	float	*a, *b, *c, *d;
{
	int	n;

	if ((n = scanf(f, a, b, c, d)) != 4) {
		sprintf(buf, "expected to read 4 numbers with '%s' format but got %d\n", f, n);
		thunk(buf);
	}

}

read_six(f, a, b, c, d, e, g)
	char	*f;
	float	*a, *b, *c, *d, *e, *g;
{
	int	n;

	if ((n = scanf(f, a, b, c, d, e, g)) != 6) {
		sprintf(buf, "expected to read 6 numbers with '%s' format but got %d\n", f, n);
		thunk(buf);
	}

}
