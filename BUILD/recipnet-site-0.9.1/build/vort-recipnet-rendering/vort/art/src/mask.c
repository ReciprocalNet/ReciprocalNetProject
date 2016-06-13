#include <stdio.h>
#include "art.h"

uvvec	points2x2[4] = { 
			{ -0.25, 0.25 },
			{ 0.25, -0.25 },
			{ -0.25, -0.25 },
			{ 0.25, 0.25 },
		};

uvvec	points3x3[9] = { 
			{ 0.0, 0.0 },
			{ -2 / 6.0, 2 / 6.0 },
			{ -2 / 6.0, -2 / 6.0 },
			{ -2 / 6.0, 0.0 },
			{ 2 / 6.0, 2 / 6.0 },
			{ 0.0, 2 / 6.0 },
			{ 2 / 6.0, -2 / 6.0 },
			{ 0.0, -2 / 6.0 },
			{ 0.0, 2 / 6.0 }
		};

uvvec	points4x4[16] = { 
			{ -3 / 8.0, 3 / 8.0 },
			{ -3 / 8.0, 1 / 8.0 },
			{ -3 / 8.0, -1 / 8.0 },
			{ -3 / 8.0, -3 / 8.0 },
			{ -1 / 8.0, 3 / 8.0 },
			{ -1 / 8.0, 1 / 8.0 },
			{ -1 / 8.0, -1 / 8.0 },
			{ -1 / 8.0, -3 / 8.0 },
			{ 1 / 8.0, 3 / 8.0 },
			{ 1 / 8.0, 1 / 8.0 },
			{ 1 / 8.0, -1 / 8.0 },
			{ 1 / 8.0, -3 / 8.0 },
			{ 3 / 8.0, 3 / 8.0 },
			{ 3 / 8.0, 1 / 8.0 },
			{ 3 / 8.0, -1 / 8.0 },
			{ 3 / 8.0, -3 / 8.0 }
		};

uvvec	circle5Points[5] = {
			{ 0.0, 0.0 },
			{ 0.0, 0.5 },
			{ 0.0, -0.5 },
			{ 0.5, 0.0 },
			{ -0.5, 0.0 }
		};

uvvec	circle7Points[7] = {
			{ 0.0, 0.0 },
			{ 0.000000, 0.666667 },
			{ 0.000000, -0.666667 },
			{ 0.577350, 0.333333 },
			{ -0.577350, 0.333333 },
			{ 0.577350, -0.333333 },
			{ -0.577350, -0.333333 }
		};

uvvec	circle13Points[13] = {
			{ 0.0, 0.0 },
			{ 0.333333, 0.577350 },		/* outer */
			{ 0.666667, 0.000000 },
			{ 0.333333, -0.577350 },
			{ -0.333333, -0.577350 },
			{ -0.666667, 0.000000 },
			{ -0.333333, 0.577350 },
			{ 0.000000, 0.333333 },		/* inner */
			{ 0.577350, 0.166667 },
			{ 0.577350, -0.166667 },
			{ 0.000000, -0.333333 },
			{ -0.577350, -0.166667 },
			{ -0.577350, 0.166667 }
		};

uvvec	circle19Points[19] = {
			{ 0.0, 0.0 },
			{ 0.750, 0.433 },	/* outer */
			{ 0.0, 0.866 },
			{ -0.750, 0.433 },
			{ -0.750, -0.433 },
			{ 0.0, -0.866 },
			{ 0.750, -0.433 },
			{ 0.75, 0.0 },
			{ 0.375, 0.650 },	/* inner */
			{ -0.375, 0.650 },
			{ -0.750, 0.000 },
			{ -0.375, -0.650 },
			{ 0.375, -0.650 },
			{ 0.375, 0.216 },
			{ 0.000, 0.433 },
			{ -0.375, 0.217 },
			{ -0.375, -0.216 },
			{ 0.000, -0.433 },
			{ 0.375, -0.217 }
		};

sampleMask	sample2x2 = { 4, 0.25, points2x2 };
sampleMask	sample3x3 = { 9, 2 / 6.0, points3x3 };
sampleMask	sample4x4 = { 16, .0125, points4x4 };

sampleMask	circle5 = { 5, 0.25, circle5Points };
sampleMask	circle7 = { 7, 0.14, circle7Points };
sampleMask	circle13 = { 13, 0.090, circle13Points };
sampleMask	circle19 = { 19, 0.075, circle19Points };

sampleMask *
getSquareSampleMask(nsamples)
	int	nsamples;
{
	if (nsamples == 1)
		return (sampleMask *)NULL;

	if (nsamples > 9)
		return &sample4x4;

	if (nsamples > 4)
		return &sample3x3;

	return &sample2x2;
}

sampleMask *
getCircularSampleMask(nsamples)
	int	nsamples;
{
	if (nsamples == 1)
		return (sampleMask *)NULL;

	if (nsamples >= 19)
		return &circle19;

	if (nsamples >= 13)
		return &circle13;

	if (nsamples >= 7)
		return &circle7;

	return &circle5;
}
