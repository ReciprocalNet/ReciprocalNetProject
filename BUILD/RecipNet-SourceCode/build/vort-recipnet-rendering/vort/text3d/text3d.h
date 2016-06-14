

#ifndef FONTLIB
#ifdef PC
extern char	*malloc();
#define FONTLIB "c:\\lib\\hershey\\"
#else
#define FONTLIB "/usr/local/lib/hershey/"
#endif
#endif

#ifndef M_PI
#define M_PI    3.14159265358979323846
#endif

#ifndef D2R
#define D2R (M_PI / 180.0)
#endif

typedef struct {
	float	r, g, b;
} colour;

typedef struct {
	float	ri;
	float	kd;
	float	ks;
	int	ksexp;
} matl;

typedef struct {
	float	x, y, z;
} vector;

typedef struct {
	float	ang;
	int	ax;
} rota;

typedef struct {
	float	radius;
	colour	col;
	int	col_set;
	colour	amb;
	int	amb_set;
	float	refl;
	int	ref_set;
	float	trans;
	int	tran_set;
	matl	mat;
	int	mat_set;
	float	textr;
	float	textcos;
	float	textsin;
	vector	scal;
	int	scal_set;
	vector	tran;
	int	trans_set;
	rota	rot;
	int	rot_set;
	int	fixedwidth;
	int	centered;
	void	(*drawfun)();
} attribs;

#define SQR(x)  ((x) * (x))
#define ABS(x)  ((x) >= 0 ? (x) : -(x))
#define MAX(a, b)       ((a) < (b) ? (b) : (a))


