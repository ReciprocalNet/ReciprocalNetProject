#include <stdio.h>
/*
 * places chess pieces on a chess board.
 *
 * 	input is "board piece row,col piece row,col .....
 * 	(Note the commas)
 */

extern	char	*index();


main(argc, argv)
	int	argc;	
	char	**argv;
{

	int	i, row, col;
	char	piece[20];
	float	x, y, z, scale = 0.0012;

	if (argc < 2) {
		fprintf(stderr, "%s piece row,col piece row,col ....\n", argv[0]);
		exit(1);
	}
	
	printf("lookat(0.0, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0)\n");
	printf("background 0.2,0.2,0.3\n");
	printf("\n");
	printf("light {\n");
	printf("	colour 1.0, 1.0, 1.0\n");
	printf("	location (0.0, 1.0, 7.0)\n");
	printf("}\n");
	printf("light {\n");
	printf("	colour 1.0, 1.0, 1.0\n");
	printf("	location (-5.0, 5.0, 6.0)\n");
	printf("}\n");
	printf("\n");

	printf("polygon {\n");
	printf("	material 0.0, 0.65, 0.35, 20.0\n");
	printf("	reflectance 0.65\n");
	printf("	tile tile.pix size 0.25, 0.25\n");
	printf("	vertex(4.0, -4.0, 4.0)\n");
	printf("	vertex(-4.0, -4.0, 4.0)\n");
	printf("	vertex(-4.0, -4.0, -4.0)\n");
	printf("	vertex(4.0, -4.0, -4.0)\n");
	printf("}\n");

	printf("polygon {\n");
	printf("	material 0.0, 0.0, 0.3, 10.0\n");
	printf("	colour 1.0, 1.0, 1.0\n");
	printf("	reflectance 0.999\n");
	printf("	vertex(-7.0, -4.0, -7.0)\n");
	printf("	vertex(-7.0, 10.0, -7.0)\n");
	printf("	vertex(7.0, 10.0, -7.0)\n");
	printf("	vertex(7.0, -4.0, -7.0)\n");
	printf("}\n");

	printf("polygon {\n");
	printf("	material 0.0, 0.0, 0.3, 10.0\n");
	printf("	colour 1.0, 1.0, 1.0\n");
	printf("	reflectance 0.999\n");
	printf("	vertex(7.0, -4.0, -7.0)\n");
	printf("	vertex(7.0, 10.0, -7.0)\n");
	printf("	vertex(7.0, 10.0, 7.0)\n");
	printf("	vertex(7.0, -4.0, 7.0)\n");
	printf("}\n");

	printf("polygon {\n");
	printf("	material 0.0, 0.0, 0.3, 10.0\n");
	printf("	colour 1.0, 1.0, 1.0\n");
	printf("	reflectance 0.999\n");
	printf("	vertex(-7.0, -4.0, -7.0)\n");
	printf("	vertex(-7.0, 10.0, -7.0)\n");
	printf("	vertex(-7.0, 10.0, 7.0)\n");
	printf("	vertex(-7.0, -4.0, 7.0)\n");
	printf("}\n");

	z = -4.0;
	i = 1;
	while (i < argc - 1) {
		strcpy(piece, argv[i]);
		sscanf(argv[i + 1], "%d,%d", &row, &col);
		i += 2;
		fprintf(stderr, "%s %d,%d\n", piece, row, col);
		x = -3.5 + (float)(row - 1);
		y = -3.5 + (float)(col - 1);
		printf("geometry {\n");
		printf("	phongshading on\n");
		printf("        colour 0.25, 0.459, 0.2\n");
		printf("        material 0.0, 0.5, 0.35, 5.0\n");
		printf("        backfacing on\n");
		printf("	scale(%.4f, %.4f, %.4f)\n", scale, scale, scale);
		printf("	translate(%f, %f, %f)\n", x, z, y);
		printf("        geofile %s.geo\n", piece);
		printf("}\n");
		printf("\n");
	}

	exit(0);
}
