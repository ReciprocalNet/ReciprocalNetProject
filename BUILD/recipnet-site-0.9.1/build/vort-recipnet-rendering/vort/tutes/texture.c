#include <stdio.h>

FILE	*fp;

main()
{
	char	buf[10];
	float	xs, ts, ps;
	int	i = 0;

	for (xs = 0.0; xs <= 20.0; xs += 2.0) {
		sprintf(buf, "t%02d.scn", i);
		fprintf(stderr, "Creating file: %s\n", buf);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "can't open file\n");
			exit(1);
		}
		i++;

		writethem(xs, 1.0, 1.0);
		fclose(fp);
	}
	for (ts = 0.0; ts <= 10.0; ts += 0.5) {
		fprintf(stderr, "Creating file: %s\n", buf);
		sprintf(buf, "t%d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "can't open file\n");
			exit(1);
		}
		i++;

		writethem(10.0, ts, 1.0);
		fclose(fp);
	}

	for (ps = 0.0; ps <= 5.0; ps += 0.25) {
		fprintf(stderr, "Creating file: %s\n", buf);
		sprintf(buf, "t%d.scn", i);
		if ((fp = fopen(buf, "w")) == NULL) {
			fprintf(stderr, "can't open file\n");
			exit(1);
		}
		i++;

		writethem(10.0, 7.0, ps);
		fclose(fp);
	}
}

writethem(xs, ts, ps)
	float	xs, ts, ps;
{
	fprintf(fp, "title \"s=%.2f t=%.2f p=%.2f\"\n", xs, ts, ps);
	fprintf(fp, "lookat(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0)\n");
	fprintf(fp, "\n");
	fprintf(fp, "background 0.0, 0.2, 0.2\n");
	fprintf(fp, "\n");
	fprintf(fp, "\n");
	fprintf(fp, "light {\n");
	fprintf(fp, "\tradius 0.1\n");
	fprintf(fp, "\tcolour 1.0, 1.0, 1.0\n");
	fprintf(fp, "\tlocation (4.0, 4.0, 4.1)\n");
	fprintf(fp, "}\n");
	fprintf(fp, "\n");
	fprintf(fp, "\n");
	fprintf(fp, "polygon {\n");
	fprintf(fp, "\tmaterial 0.0, 0.7, 0.3, 10\n");
	fprintf(fp, "\tcolour 1.0, 1.0, 1.0\n");
	fprintf(fp, "\tambient 0.5, 0.5, 0.5\n");
	fprintf(fp, "\ttexture marble {\n");
	fprintf(fp, "\t\tblendcolour 0.0, 0.0, 0.0\n");
	fprintf(fp, "\t\tscalefactors %f, %f, %f\n", xs, ts, ps);
	fprintf(fp, "\t}\n");

	fprintf(fp, "\tvertex(-2.0, -2.0, -2.0)\n");
	fprintf(fp, "\tvertex(-2.0, 2.0, -2.0)\n");
	fprintf(fp, "\tvertex(0.0, 2.0, -2.0)\n");
	fprintf(fp, "\tvertex(0.0, -2.0, -2.0)\n");
	fprintf(fp, "}\n");
	fprintf(fp, "\n");
	fprintf(fp, "polygon {\n");
	fprintf(fp, "\tmaterial 0.0, 0.7, 0.3, 10\n");
	fprintf(fp, "\tcolour 0.815, 0.5, 0.278\n");
	fprintf(fp, "\tambient 0.5, 0.5, 0.5\n");
	fprintf(fp, "\ttexture wood {\n");
	fprintf(fp, "\t\tblendcolour 0.4, 0.25, 0.12\n");
	fprintf(fp, "\t\tscalefactors %f, %f, %f\n", xs, ts, ps);
	fprintf(fp, "\t}\n");
	fprintf(fp, "\tvertex(0.0, -2.0, -2.0)\n");
	fprintf(fp, "\tvertex(0.0, 2.0, -2.0)\n");
	fprintf(fp, "\tvertex(2.0, 2.0, -2.0)\n");
	fprintf(fp, "\tvertex(2.0, -2.0, -2.0)\n");
	fprintf(fp, "}\n");
}
