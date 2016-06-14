#include <stdio.h>
#include <math.h>
#include "art.h"

/*
 * mcpy4
 *
 *	copies 4 by 4 matrix m2 into m1
 */
void
mcpy4(m1, m2)
	float	*m1, *m2;
{
	float	*fin;

	fin = m1 + 16;
	while (m1 != fin)
		*m1++ = *m2++;
}

/*
 * mident4
 *
 *	set m to a 4 by 4 identity matrix
 */
void
mident4(m)
	float	*m;
{
	float	*fin, *m1;

	m1 = m;
	fin = m + 16;
	while (m != fin)
		*m++ = 0;

	m1[0] = 1.0;
	m1[5] = 1.0;
	m1[10] = 1.0;
	m1[15] = 1.0;
}

/*
 * transpose
 *
 *	transpose the 4x4 matrix m2 and put it in m1
 */
transpose(m1, m2)
	matrix	m1, m2;
{
	int	i, j;

	for (i = 0; i != 4; i++)
		for (j = 0; j != 4; j++)
			m1[i][j] = m2[j][i];
}

/*
 * mmult4
 *
 *	mulitiplies four by four matrix m3 by m2, leaving result in
 * m1.
 *
 */
void
mmult4(m1, m2, m3)
	float	m1[4][4], m2[4][4], m3[4][4];
{
	m1[0][0] = m2[0][0] * m3[0][0] + m2[0][1] * m3[1][0] + m2[0][2] * m3[2][0] + m2[0][3] * m3[3][0];
	m1[0][1] = m2[0][0] * m3[0][1] + m2[0][1] * m3[1][1] + m2[0][2] * m3[2][1] + m2[0][3] * m3[3][1];
	m1[0][2] = m2[0][0] * m3[0][2] + m2[0][1] * m3[1][2] + m2[0][2] * m3[2][2] + m2[0][3] * m3[3][2];
	m1[0][3] = m2[0][0] * m3[0][3] + m2[0][1] * m3[1][3] + m2[0][2] * m3[2][3] + m2[0][3] * m3[3][3];
	m1[1][0] = m2[1][0] * m3[0][0] + m2[1][1] * m3[1][0] + m2[1][2] * m3[2][0] + m2[1][3] * m3[3][0];
	m1[1][1] = m2[1][0] * m3[0][1] + m2[1][1] * m3[1][1] + m2[1][2] * m3[2][1] + m2[1][3] * m3[3][1];
	m1[1][2] = m2[1][0] * m3[0][2] + m2[1][1] * m3[1][2] + m2[1][2] * m3[2][2] + m2[1][3] * m3[3][2];
	m1[1][3] = m2[1][0] * m3[0][3] + m2[1][1] * m3[1][3] + m2[1][2] * m3[2][3] + m2[1][3] * m3[3][3];
	m1[2][0] = m2[2][0] * m3[0][0] + m2[2][1] * m3[1][0] + m2[2][2] * m3[2][0] + m2[2][3] * m3[3][0];
	m1[2][1] = m2[2][0] * m3[0][1] + m2[2][1] * m3[1][1] + m2[2][2] * m3[2][1] + m2[2][3] * m3[3][1];
	m1[2][2] = m2[2][0] * m3[0][2] + m2[2][1] * m3[1][2] + m2[2][2] * m3[2][2] + m2[2][3] * m3[3][2];
	m1[2][3] = m2[2][0] * m3[0][3] + m2[2][1] * m3[1][3] + m2[2][2] * m3[2][3] + m2[2][3] * m3[3][3];
	m1[3][0] = m2[3][0] * m3[0][0] + m2[3][1] * m3[1][0] + m2[3][2] * m3[2][0] + m2[3][3] * m3[3][0];
	m1[3][1] = m2[3][0] * m3[0][1] + m2[3][1] * m3[1][1] + m2[3][2] * m3[2][1] + m2[3][3] * m3[3][1];
	m1[3][2] = m2[3][0] * m3[0][2] + m2[3][1] * m3[1][2] + m2[3][2] * m3[2][2] + m2[3][3] * m3[3][2];
	m1[3][3] = m2[3][0] * m3[0][3] + m2[3][1] * m3[1][3] + m2[3][2] * m3[2][3] + m2[3][3] * m3[3][3];
}

/*
 * minv4
 *
 *	find the inverse of the 4 by 4 matrix b using gausian elimination
 * and return it in a.
 */
minv4(a, b)
	matrix	a, b;
{
	float	val, val2;
	int	i, j, k, ind;
	matrix	tmp;

	mident4((float *) a);

	mcpy4(tmp, (float *) b);

	for (i = 0; i != 4; i++) {

		val = tmp[i][i];		/* find pivot */
		ind = i;
		for (j = i + 1; j != 4; j++) {
			if (fabs(tmp[j][i]) > fabs(val)) {
				ind = j;
				val = tmp[j][i];
			}
		}

		if (ind != i) {			/* swap columns */
			for (j = 0; j != 4; j++) {
				val2 = a[i][j];
				a[i][j] = a[ind][j];
				a[ind][j] = val2;
				val2 = tmp[i][j];
				tmp[i][j] = tmp[ind][j];
				tmp[ind][j] = val2;
			}
		}

		if (val == 0.0) {
			fatal("art: singular matrix in minv4.\n");
		}

		for (j = 0; j != 4; j++) {
			tmp[i][j] /= val;
			a[i][j] /= val;
		}

		for (j = 0; j != 4; j++) {	/* eliminate column */
			if (j == i)
				continue;
			val = tmp[j][i];
			for (k = 0; k != 4; k++) {
				tmp[j][k] -= tmp[i][k] * val;
				a[j][k] -= a[i][k] * val;
			}
		}
	}
}

/*
 * minv3x3
 *
 *	calculate the inverse of a 3x3 matrix b and put it in a.
 */
void
minv3x3(a, b)
	mat3x3	a, b;
{
	float	det;

	/*
	 * compute the adjoint
	 */
	a[0][0] = b[1][1] * b[2][2] - b[1][2] * b[2][1];
	a[0][1] = b[0][2] * b[2][1] - b[0][1] * b[2][2];
	a[0][2] = b[0][1] * b[1][2] - b[0][2] * b[1][1];

	a[1][0] = b[1][2] * b[2][0] - b[1][0] * b[2][2];
	a[1][1] = b[0][0] * b[2][2] - b[0][2] * b[2][0];
	a[1][2] = b[0][2] * b[1][0] - b[0][0] * b[1][2];

	a[2][0] = b[1][0] * b[2][1] - b[1][1] * b[2][0];
	a[2][1] = b[0][1] * b[2][0] - b[0][0] * b[2][1];
	a[2][2] = b[0][0] * b[1][1] - b[0][1] * b[1][0];

	/*
	 * compute the determinate
	 */
	det = a[0][0] * b[0][0] + a[1][0] * b[0][1] + a[2][0] * b[0][2];

	if (det == 0.0)
		fatal("art: singular matrix in minv4.\n");

	/*
	 * divide through by the determinate
	 */
	a[0][0] /= det;
	a[0][1] /= det;
	a[0][2] /= det;

	a[1][0] /= det;
	a[1][1] /= det;
	a[1][2] /= det;

	a[2][0] /= det;
	a[2][1] /= det;
	a[2][2] /= det;
}


/*
 * smult4
 *
 *	multiply a 4 by 4 matrix by the scalar s
 *
 */
smult4(m, s)
	float	*m;
	float	s;
{
	float	*fin;

	fin = m + 16;
	while (m != fin)
		*m++ *= s;
}

/*
 * printmatrix
 *
 *	print out a 4 by 4 matrix.
 */
printmatrix(m)
	matrix	m;
{
	printf("matrix %x\n", m);

	printf("%e %e %e %e\n", m[0][0], m[0][1], m[0][2], m[0][3]);
	printf("%e %e %e %e\n", m[1][0], m[1][1], m[1][2], m[1][3]);
	printf("%e %e %e %e\n", m[2][0], m[2][1], m[2][2], m[2][3]);
	printf("%e %e %e %e\n", m[3][0], m[3][1], m[3][2], m[3][3]);
}
