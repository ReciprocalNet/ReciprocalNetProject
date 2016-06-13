
/*
 * macros for processing vectors
 */

/*
 * dprod
 *
 *	returns the dot product of a and b.
 */
#define	dprod(a, b)	((a).x * (b).x + (a).y * (b).y + (a).z * (b).z)

/*
 * xprod
 *
 *	returns the vector cross product of b and c in a
 */
#define xprod(a, b, c) { \
	(a).x = (b).y * (c).z - (b).z * (c).y; \
	(a).y = (b).z * (c).x - (b).x * (c).z; \
	(a).z = (b).x * (c).y - (b).y * (c).x; \
}

/*
 * normalise
 *
 *	normalise vector a
 */
#define	normalise(a) {\
	register float	_norm;\
\
	_norm = sqrt((double)dprod((a), (a)));\
	(a).x /= _norm;\
	(a).y /= _norm;\
	(a).z /= _norm;\
}

/*
 * reverse
 *
 *	reverse the vector a
 */
#define reverse(a) {\
	(a).x = -(a).x;\
	(a).y = -(a).y;\
	(a).z = -(a).z;\
}

/*
 * vsub
 *
 *	assign the subtraction of c from b to a
 */
#define vsub(a, b, c) {\
	(a).x = (b).x - (c).x;\
	(a).y = (b).y - (c).y;\
	(a).z = (b).z - (c).z;\
}

/*
 * vadd
 *
 *	assign the addition of b and c into a
 */
#define vadd(a, b, c) {\
	(a).x = (b).x + (c).x;\
	(a).y = (b).y + (c).y;\
	(a).z = (b).z + (c).z;\
}

/*
 * smult
 *
 *	scalar multiply vector a by b
 */
#define	smult(a, b) {\
	(a).x *= (b);\
	(a).y *= (b);\
	(a).z *= (b);\
}

/*
 * vmmult
 *
 *	multiply a vector b by matrix m, putiing the result in a
 *
 */
#define	vmmult(a, b, m) {\
	(a).x = (b).x * m[0][0] + (b).y * m[1][0] + (b).z * m[2][0] + m[3][0]; \
	(a).y = (b).x * m[0][1] + (b).y * m[1][1] + (b).z * m[2][1] + m[3][1]; \
	(a).z = (b).x * m[0][2] + (b).y * m[1][2] + (b).z * m[2][2] + m[3][2]; \
}

/*
 * cp3x3
 *
 *	copy a 3 x 3 matrix out of b into a
 */
#define cp3x3(a, b) { \
	a[0][0] = b[0][0]; a[0][1] = b[0][1]; a[0][2] = b[0][2]; \
	a[1][0] = b[1][0]; a[1][1] = b[1][1]; a[1][2] = b[1][2]; \
	a[2][0] = b[2][0]; a[2][1] = b[2][1]; a[2][2] = b[2][2]; \
}

/*
 * cp2x3
 *
 *	copy a 3 x 3 matrix out of b into a
 */
#define cp2x3(a, b) { \
	a[0][0] = b[0][0]; a[0][1] = b[0][1]; a[0][2] = b[0][2]; \
	a[1][0] = b[1][0]; a[1][1] = b[1][1]; a[1][2] = b[1][2]; \
}

/*
 * v3x3mult
 *
 *	multiply a vector b by matrix m, puting the result in a
 *
 */
#define	v3x3mult(a, b, m) {\
	(a).x = (b).x * m[0][0] + (b).y * m[1][0] + (b).z * m[2][0]; \
	(a).y = (b).x * m[0][1] + (b).y * m[1][1] + (b).z * m[2][1]; \
	(a).z = (b).x * m[0][2] + (b).y * m[1][2] + (b).z * m[2][2]; \
}

/*
 * v3x3tmult
 *
 *	multiply a vector b by the transpose of matrix m, puting the
 * result in a
 *
 */
#define	v3x3tmult(a, b, m) {\
	(a).x = (b).x * m[0][0] + (b).y * m[0][1] + (b).z * m[0][2]; \
	(a).y = (b).x * m[1][0] + (b).y * m[1][1] + (b).z * m[1][2]; \
	(a).z = (b).x * m[2][0] + (b).y * m[2][1] + (b).z * m[2][2]; \
}

/*
 * macros from moving from ray to object space and visa versa
 */

/*
 * transray
 *
 *	calculate ray a which is ray b transformed into
 * the object space of o.
 */
#define transray(o, a, b) {\
        transdata       *_td;\
\
	_td = o->td; \
\
	v3x3mult((a).org, (b).org, _td->mat);\
	v3x3mult((a).dir, (b).dir, _td->mat);\
\
	(a).org.x += _td->trans.x;\
	(a).org.y += _td->trans.y;\
	(a).org.z += _td->trans.z;\
}

/*
 * toobject
 *
 *	transform the point in world space b into object space 
 * and save in a.
 */
#define	toobject(o, a, b) { \
	transdata	*_td;\
\
	_td = o->td; \
\
	v3x3mult((a), (b), _td->mat); \
\
	(a).x += _td->trans.x; \
	(a).y += _td->trans.y; \
	(a).z += _td->trans.z; \
}

/*
 * totexture
 *
 *	transform the point in world space b into texture space 
 * and save in a.
 */
#define	totexture(txt, a, b) { \
	transdata	*_td; \
\
	_td = txt->td; \
\
	v3x3mult((a), (b), _td->mat); \
\
	(a).x += _td->trans.x; \
	(a).y += _td->trans.y; \
	(a).z += _td->trans.z; \
\
	(a).x *= _td->nscales.x; \
	(a).y *= _td->nscales.y; \
	(a).z *= _td->nscales.z; \
}

/*
 * toworld
 *
 *	transform the point in object space b into world space 
 * and save in a.
 */
#define	toworld(o, a, b) { \
\
	if (o->mat != (mat3x3 *)NULL) { \
		v3x3tmult((a), (b), (*o->mat)); \
	} else \
		a = b; \
\
	(a).x -= o->trans.x; \
	(a).y -= o->trans.y; \
	(a).z -= o->trans.z; \
}

/*
 * macros to handle allocation of hitlist entries
 */
#define release(p)	{\
	p->nxt = fhlist;\
	fhlist = p;\
}

#define	fetch(p)	{\
	if (fhlist != (hlist *)NULL) {\
		p = fhlist;\
		fhlist = fhlist->nxt;\
	} else\
		p = (hlist *)smalloc(sizeof(hlist));\
}

/*
 * macro for accessing random number table
 */
#define	randnum()	((randp == erandp) ? *(randp = randtable) : *randp++)


/*
 * miscellaneous macros
 */
#define	sqr(x)	((x) * (x))

/*
 * set a or b depending on whether c is a minimum or maximum value
 */
#define	minmax(a, b, c)	{\
	if ((a) > (c)) \
		(a) = (c); \
	else if ((b) < (c)) \
		(b) = (c); \
}

/*
 * clamp a value to be between 0.0 and 1.0
 */
#define clamp(a)        ((a) < 0.0 ? 0.0 : ((a) < 1.0 ? (a) : 1.0))

/*
 * linearly blend two colors
 */
#define mix(col1, col2, fact) { \
	(col1).r = fact * (col2).r + (1.0 - fact) * (col1).r;\
	(col1).g = fact * (col2).g + (1.0 - fact) * (col1).g;\
	(col1).b = fact * (col2).b + (1.0 - fact) * (col1).b;\
}

/*
 * provide a linear ramp for c according to a and b
 */
#define linsmooth(a, b, c)	(((c) < (a)) ? 0.0 : ((c) > (b)) ? 1.0 : ((c) - (a)) / ((b) - (a)))
