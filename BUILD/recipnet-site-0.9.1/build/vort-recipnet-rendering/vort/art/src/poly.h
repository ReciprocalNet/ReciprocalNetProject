
#define	MAX_ORDER	12	
#define	ALG_TOLERANCE	0.001

#define	MAXERROR	1.0e-11
#define	RELERROR	1.0e-10
#define	MAXIT		800
#define	MAXP2		32

#define	MAXT	1e10

/*
 * a coefficient smaller than FUDGE is considered to be zero (0.0).
 *		- this is only done if WORRY_ABOUT_REPEATED_ROOTS
 *		is defined
 *
#define	WORRY_ABOUT_REPEATED_ROOTS
 */
#define	FUDGE		1.0e-12

typedef struct sp {
	int	ord;
	double	coef[MAX_ORDER];
} spoly;

