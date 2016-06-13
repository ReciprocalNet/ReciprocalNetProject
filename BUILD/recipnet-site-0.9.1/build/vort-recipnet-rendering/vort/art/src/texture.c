#include <stdio.h>
#include <math.h>
#include "art.h"
#include "macro.h"
#include "gram.h"

typedef struct {
	char	*name;
	float	(*fun)();
	char	what;
} ourtextures;

float	wood1(),
	wood2(),
	bump(),
	colourblend(),
	granite(),
	marble(),
	wrinkled(),
	fuzz(),
	stucco(),
	spotted(),
	ripple(),
	tile(),
	blocks(),
	bricks(),
	waves();

#define	COLOUR_CHANGE	1
#define NORMAL_CHANGE	2
#define TILE_PATTERN	3

#define WIDTH_SET	1
#define HEIGHT_SET	2
#define LENGTH_SET	2
#define DEPTH_SET	4
#define MIXEMUP		8

ourtextures texturetable[] = {
        {"marble",	marble,		COLOUR_CHANGE},
        {"wood",	wood1,		COLOUR_CHANGE},
        {"wood1",	wood1,		COLOUR_CHANGE},
        {"wood2",	wood2,		COLOUR_CHANGE},
        {"oldwood",	wood2,		COLOUR_CHANGE},
        {"bump",	bump,		NORMAL_CHANGE},
        {"bumpy",	bump,		NORMAL_CHANGE},
        {"colorblend",	colourblend,	COLOUR_CHANGE},
        {"colourblend",	colourblend,	COLOUR_CHANGE},
        {"granite",	granite,	COLOUR_CHANGE},
        {"wrinkled",	wrinkled,	NORMAL_CHANGE},
        {"fuzz",	fuzz,		NORMAL_CHANGE},
        {"fuzzy",	fuzz,		NORMAL_CHANGE},
        {"stucco",	stucco,		NORMAL_CHANGE},
        {"spotted",	spotted,	COLOUR_CHANGE},
        {"blocks",	blocks,		COLOUR_CHANGE},
        {"bricks",	bricks,		COLOUR_CHANGE},
        {"ripple",	ripple,		NORMAL_CHANGE},
        {"ripples",	ripple,		NORMAL_CHANGE},
        {"waves",	waves,		NORMAL_CHANGE},
        {"wavey",	waves,		NORMAL_CHANGE},
        {"tile",	tile,		TILE_PATTERN},
        {"image",	tile, 		TILE_PATTERN}
};

#define NUMTEXT	(sizeof(texturetable) / sizeof(ourtextures))

#define NWAVES	50

#define POW(x, e) {\
	int	_i;\
	float	_x = x;\
\
	for (_i = 1; _i < e; _i++)\
		x *= _x;\
}

static int	noise_inited = 0;
static int	ripples_inited = 0;
static vector	*ripples;
static colour	white = {1.0, 1.0, 1.0};

extern float	noise();
extern float	randtable[], *randp, *erandp;
extern float	sourceradius;
extern attr	*astackp;
extern mats	*mstackp;

extern void    (*tilefun[NUM_OBJS])();


/*
 * Produces a triangular wave....
 */
float
triwave(x)
	float   x;
{
	float	a = 1.0;
	int	n;

	/*
	 * Get X into the range 0 to 4.0
	 * (Using period of 4 to make the slopes 1 or -1)
	 */

	if (x < 0.0) {
		a = -1.0;
		x = -x;
	}

        n = x / 4.0;
        x -= n * 4.0;

	if (x < 1.0) 
		return (a * x);
	else if (x < 3.0)
		return(-x * a + a * 2.0);
	else
		return(a * x - a * 4.0);
}

/*
 * dotexture
 *
 * 	Applies textures to different attributes of an object....
 */
void
dotexture(o, l, n, type, surf)
	object	*o;
	vector	*l, *n;
	int	type;
	surface	*surf;
{
	texture		*tl;
	float		val;
	colour		col;

	for (tl = o->txtlist; tl != (texture *)NULL; tl = tl->nxt) {
		col = surf->c;
		val = tl->txtfun(o, tl, l, n, &col, type);
		switch (tl->modulate) {
		case AMBIENT:
			surf->a.r = col.r;
			surf->a.g = col.g;
			surf->a.b = col.b;
			break;
		case TRANSPARENCY:
			surf->trans.r = col.r;
			surf->trans.g = col.g;
			surf->trans.b = col.b;
			break;
		case REFLECTANCE:
			surf->refl.r = col.r;
			surf->refl.g = col.g;
			surf->refl.b = col.b;
			break;
		case RI:
			surf->ri = 1.0 + val;
			break;
		case COLOUR:
			surf->c.r = col.r;
			surf->c.g = col.g;
			surf->c.b = col.b;
			break;
		default:
			;
		}
	}
}

/*
 * octaves
 *
 *	Accumulate a 3D noise function over octaves octaves, scaling
 *	each by 1 / f
 */
float
turbulance(pos, octaves)
	vector	*pos;
	int	octaves;
{
	real	t, f;
	vector	p;
	int	i;

	f = 1.0;
	t = 0.0;

	p.x = pos->x  + 123.456;
	p.y = pos->y;
	p.z = pos->z;
		
	for (i = 0; i < octaves; i++) {
		t += fabs(noise(&p)) / f;
		p.x /= 0.527849474673;
		p.y /= 0.527849474673;
		p.z /= 0.527849474673;
		f /= 0.527849474673;
	}

	return(t - 0.3);
}

/*
 * tile
 *
 * 	Does a tiled texture
 */
float
tile(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	int	type;
	colour	*pcol;
{

	colour	ncol;

	if (tilefun[o->type] != NULL) {
		ncol = *pcol;
		tilefun[o->type](o, txt, l, n, &ncol, type);
		mix(*pcol, ncol, txt->u.t->blend);
	}

	return((pcol->r * 0.3 + pcol->g * 0.59 + pcol->b * 0.11));

}

/*
 * bricks
 *
 * 	Does bricks texture.
 */
float
bricks(o, txt, l, n, col, type)
	object		*o;
	texture		*txt;
	vector		*l, *n;
	colour		*col;
	int		type;
{
	float	r, x, y, z, g, d;
	vector	loc;
	

	totexture(txt, loc, *l);

	/* Get a random number between 0 and mixval */
	r = txt->u.b->mixval * randnum();

	/*
	 * Check how close we are to a "gap"
	 */

	/* BONK */
	g = 1.0 -  txt->u.b->gapwidth / 2.0;

	d = 2 * loc.y / txt->u.b->blockheight - 1.0;
	if (txt->u.b->flags & HEIGHT_SET) {
		y = triwave(d);
		if (fabs(y) >= g) {
			*col = txt->u.b->gapcolour;
			/* Stuff with the normal as well */
			return(1.0);
		}
		if ((txt->u.b->flags & MIXEMUP) && y > r)
			mix(*col, txt->u.b->blendcolour, r);
	}

	y = triwave(d + 1.0);

	if (txt->u.b->flags & WIDTH_SET) {
		z = 2 * loc.x / txt->u.b->blockwidth - 1.0;
		if (y < 0.0)
			z -= 1.0;

		x = triwave(z);
		if (fabs(x) > g) {
			*col = txt->u.b->gapcolour;
			return(1.0);
		}
		if ((txt->u.b->flags & MIXEMUP) && x > r)
			mix(*col, txt->u.b->blendcolour, r);
	}

	if (txt->u.b->flags & DEPTH_SET) {
		z = 2 * loc.z / txt->u.b->blockdepth - 1.0;
		if (y < 0.0)
			z -= 1.0;

		z = triwave(z);
		if (z > g) {
			*col = txt->u.b->gapcolour;
			return(1.0);
		}
		
		if ((txt->u.b->flags & MIXEMUP) && z > r)
			mix(*col, txt->u.b->blendcolour, r);
	}

	return(1.0);
}

/*
 * blocks
 *
 * 	Does blocks texture.
 */
float
blocks(o, txt, l, n, col, type)
	object		*o;
	texture		*txt;
	vector		*l, *n;
	colour		*col;
	int		type;
{
	float	x, y, z, g;
	vector	loc;
	

	totexture(txt, loc, *l);

	*col = txt->u.b->blendcolour;

	/*
	 * Check how close we are to a "gap"
	 */

	/* BLARK */
	g = 1.0 -  txt->u.b->gapwidth / 2.0;

	if (txt->u.b->flags & WIDTH_SET) {
		x = fabs(triwave(2 * loc.x / txt->u.b->blockwidth - 1.0));
		if (x > g) {
			*col = txt->u.b->gapcolour;
			return(1.0);
		}
	}

	if (txt->u.b->flags & HEIGHT_SET) {
		y = fabs(triwave(2 * loc.y / txt->u.b->blockheight - 1.0));
		if (y > g) {
			*col = txt->u.b->gapcolour;
			return(1.0);
		}
	}

	if (txt->u.b->flags & DEPTH_SET) {
		z = fabs(triwave(2 * loc.z / txt->u.b->blockdepth - 1.0));
		if (z > g) {
			*col = txt->u.b->gapcolour;
			return(1.0);
		}
	}

	return(1.0);
}


/*
 * corona
 *
 * 	Does corona texture.
 */
float
corona(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	int	type;
	colour	*pcol;
{
	int	e;
	float	x;
	vector	loc;
	proctxt	*pt;
}

/*
 * marble
 *
 * 	Does marble texture.
 */
float
marble(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	int	type;
	colour	*pcol;
{
	int	e;
	float	x;
	vector	loc;
	proctxt	*pt;

	totexture(txt, loc, *l);

	pt = txt->u.p;

	e = (int)pt->var[2];

	x = pt->var[0] * loc.x + pt->var[1] * turbulance(&loc, pt->octaves);

	x = 0.5 * (1.0 + sin(x));

	POW(x, e);

	texturec(pt, pcol, x);

	return(x);

}

/*
 * granite
 *
 * 	Does granite texture.
 */
float
granite(o, txt, l, n, pcol, type)
	object	*o;
	texture	*txt;
	vector	*l, *n;
	colour	*pcol;
	int	type;
{
	float	x;
	vector	loc;
	proctxt	*pt;
	
	totexture(txt, loc, *l);

	pt = txt->u.p;

	x = pt->var[0] * turbulance(&loc, pt->octaves);

	texturec(pt, pcol, x);

	return(x);

}


/*
 * wood1
 *
 * 	Does wood texture.
 */
float
wood1(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	int	type;
	colour   *pcol;
{
	float	r;
	vector	loc;
	proctxt	*pt;
	
	totexture(txt, loc, *l);

	pt = txt->u.p;

	r = pt->var[0] * sqrt(loc.x * loc.x + loc.y * loc.y);
	r += pt->var[1] * noise(&loc);
	r = 0.5 * (1.0 + pt->var[2] * sin(r * 2 * M_PI));

	texturec(pt, pcol, r);

	return(r);
}


/*
 * wood2
 *
 * 	Does oldwood texture.
 */
float
wood2(o, tx, l, n, pcol, type)
	object	*o;
	texture	*tx;
	vector	*l, *n;
	colour	*pcol;
	int	type;
{
	int	e;
	float	x;
	vector	loc;
	proctxt	*pt;
	
	totexture(tx, loc, *l);

	pt = tx->u.p;

	e = (int)pt->var[2];

	x = sqrt(loc.x * loc.x + loc.y * loc.y);
	x *= pt->var[0];
	x += pt->var[1] * turbulance(&loc, pt->octaves);

	x = 0.5 * (1.0 + sin(x));

	POW(x, e);

	texturec(pt, pcol, x);

	return(x);
}

/*
 * bump
 *
 * 	Does bump texture.
 */
float
bump(o, tx, l, n, pcol, type)
	object	*o;
	texture	*tx;
	vector	*l, *n;
	colour	*pcol;
	int	type;
{
	vector	loc, amp;
	proctxt	*pt;
	
	totexture(tx, loc, *l);

	Vnoise(&loc, &amp);

	pt = tx->u.p;

	n->x += amp.x * pt->var[0];
	n->y += amp.y * pt->var[1];
	n->z += amp.z * pt->var[2];

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * wrinkled
 *
 * 	Does a wrinkled texture.
 */
float
wrinkled(o, tx, l, n, pcol, type)
	object  *o;
	texture *tx;
	vector  *l, *n;
	colour   *pcol;
	int	type;
{
	int	i;
	float	f = 1.0;
	proctxt	*pt;

	vector	amp, loc;
	
	totexture(tx, loc, *l);

	pt = tx->u.p;

	f = 1.0;
	for (i = 0; i < pt->octaves; i++) {
		loc.x *= f;
		loc.y *= f;
		loc.z *= f;
		Vnoise(&loc, &amp);
		n->x += amp.x * pt->var[0];
		n->y += amp.y * pt->var[1];
		n->z += amp.z * pt->var[2];
		f *= 2.0;
	}

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * stucco
 *
 *      Does a stucco texture.
 */
float
stucco(o, txt, l, n, pcol, type)
        object  *o;
        texture *txt;
        vector  *l, *n;
        pixel   *pcol;
	int	type;
{
        vector  amp, loc;
        float   val;

        totexture(txt, loc, *l);

 
        val = noise(&loc);
 
        if (val > txt->u.p->var[0]) {
                Vnoise(&loc, &amp);
                val = (val - txt->u.p->var[0]) / (1.0 - txt->u.p->var[0]);
                val *= txt->u.p->var[1];
                n->x += val * amp.x;
                n->y += val * amp.y;
                n->z += val * amp.z;
        }

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * spotted
 *
 *      Does a spotted texture.
 */
float
spotted(o, txt, l, n, pcol, type)
        object  *o;
        texture *txt;
        vector  *l, *n;
        colour   *pcol;
        int     type;
{
        vector  loc;
        float   val;

        totexture(txt, loc, *l);

        val = noise(&loc);
 
        if (val > txt->u.p->var[0]) {
                val = (val - txt->u.p->var[0]) / (1.0 - txt->u.p->var[0]);
                val *= txt->u.p->var[1];
        } else
		val = 1.0;

	texturec(txt->u.p, pcol, val);
	return(val);


}

/*
 * colourblend
 * 
 *      Does a color blending along the y axis
 */
float
colourblend(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	colour   *pcol;
	int     type;
{
	vector loc;
	float val;
	proctxt *pt;

	totexture(txt, loc, *l);

	pt = txt->u.p;

	val = loc.y - pt->var[0];
	val /= pt->var[1];

	if (pt->var[2] != 0.0)
		val += pt->var[2] * turbulance(&loc, pt->octaves);

	texturec(pt, pcol, val);

	return(0.5 + 0.5 * sin(val));
}


/*
 * rnd
 *
 *	handle some machines funny ideas about what rand should
 * return.
 */
int
rnd()
{
        int     i;
        float   a;

        a = (float)(rand() & 0x7FFF) / 32768.0;

        i = (erandp - randtable - 1) * a;
        return(i);
}

/*
 * fuzz
 *
 *      Does a fuzzy texture.
 */
float
fuzz(o, txt, l, n, pcol, type)
        object  *o;
        texture *txt;
        vector  *l, *n;
	int	type;
        colour   *pcol;
{
	vector	bullshit;
	float	val;

        /*
         * Just add bullshit to the normal
         */

	randp = &randtable[rnd()];

	bullshit.x = (-1.0 + 2 * randnum()) * txt->u.p->var[0];
	bullshit.y = (-1.0 + 2 * randnum()) * txt->u.p->var[1];
	bullshit.z = (-1.0 + 2 * randnum()) * txt->u.p->var[2];

	vadd(*n, *n, bullshit);

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * ripple
 *
 * 	Does a rippley texture.
 */
float
ripple(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	colour   *pcol;
	int     type;
{
	vector	loc, tmp;
	int	i;
	float	mag, val;
	proctxt	*pt;
	
        totexture(txt, loc, *l);

	pt = txt->u.p;

	for (i = 0; i < pt->octaves; i++) {
		vsub(tmp, loc, ripples[i]);
		mag = dprod(tmp, tmp);
		if (mag == 0.0)
			mag = 1.0;

		mag = sqrt(mag);

		val = mag * pt->var[1];

		val = sin(val * 2 * M_PI) * pt->var[0] / mag / pt->octaves;

		smult(tmp, val);

		n->x += tmp.x;
		n->y += tmp.y;
		n->z += tmp.z;
	}

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * waves
 *
 * 	Does a wavey texture.
 */
float
waves(o, txt, l, n, pcol, type)
	object  *o;
	texture *txt;
	vector  *l, *n;
	int     type;
	colour   *pcol;
{
	vector	amp, loc, tmp;
	float	mag, damp, val;
	wlist	*w;
	
        totexture(txt, loc, *l);

	for (w = txt->u.w; w != (wlist *)NULL; w = w->nxt) {

		vsub(tmp, loc, w->center);

		mag = dprod(tmp, tmp);
		if (mag == 0.0)
			mag = 1.0;

		mag = sqrt(mag);

		val =  mag * w->len;	/* Really 2 * PI / w->len */

		damp = 1.0;
		if (w->damp != 0.0)
			damp = exp((double)(-w->damp * mag));

		val = val + w->phase;	/* Really 2 * M_PI * w->phase */

		val = w->amp * damp * sin(val) / mag;

		smult(tmp, val);

		n->x += tmp.x;
		n->y += tmp.y;
		n->z += tmp.z;
	}

	pcol->r = n->x;
	pcol->g = n->y;
	pcol->b = n->z;

	return((n->x + n->y + n->z) / 3.0);
}

/*
 * init_ripples
 *
 *	initialise some ripples.
 */
init_ripples(nwaves)
	int	nwaves;
{
	vector	tmp;
	int	i;

	if (ripples_inited)
		return;

	ripples = (vector *)scalloc(sizeof(vector), (unsigned long)nwaves);

	for (i = 0; i < nwaves; i++) {
		tmp.x = i * 100.1234;
		tmp.y = i * 89.3456;
		tmp.z = i * 103.7654;
		Vnoise(&tmp, &ripples[i]);
		smult(ripples[i], sourceradius);
	}

	ripples_inited = 1;
}

/*
 * waveinit
 *
 *	initialize a list of wave sources
 */
wlist *
waveinit(d)
	details	*d;
{
	wlist	*w;
	details	*wd;

	w = (wlist *)smalloc(sizeof(wlist));

	w->amp = w->len = 1.0;
	w->phase = w->damp = 0.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case CENTER:
			w->center.x = d->u.v.x;
			w->center.y = d->u.v.y;
			w->center.z = d->u.v.z;
			break;
		case AMPLITUDE:
			w->amp = d->u.f;
			break;
		case WAVELENGTH:
			if (d->u.f != 0.0)
				w->len = 2 * M_PI / d->u.f;
			else
				w->len = 2 * M_PI;
			break;
		case PHASE:
			w->phase = 2 * M_PI * d->u.f;
			break;
		case DAMPING:
			w->damp = d->u.f;
			break;
		default:
			warning("art: illegal field in wave source ignored (line %d of %s).\n");
		}

		wd = d;
		d = d->nxt;
		free(wd);
	}

	return(w);
}

/*
 * texturec
 *
 *	compute the color for a textured surface.
 */
texturec(pt, pcol, x)
	proctxt	*pt;
	colour	*pcol;
	float	x;
{
	int	i, j;
	colour	col1, col2;

	if (pt->map == (char *)NULL) {
		/*
		 * It's a variation of the base color.
		 */
		x = clamp(x);
		mix(*pcol, pt->blendcolour, x * pt->blend);
	} else {
		i = fabs(x) * pt->pixw;
		if (i < 0)
			i = 0;
		if (i > pt->pixw)
			i = pt->pixw - 1;

		j = i + 1;
		i *= 3;
		j *= 3;

/* does EMS stuff need to go here ??? */

		col1.r = (unsigned char)pt->map[i] / 255.0;
		col1.g = (unsigned char)pt->map[i + 1] / 255.0;
		col1.b = (unsigned char)pt->map[i + 2] / 255.0;

		col2.r = (unsigned char)pt->map[j] / 255.0;
		col2.g = (unsigned char)pt->map[j + 1] / 255.0;
		col2.b = (unsigned char)pt->map[j + 2] / 255.0;

		col1.r += x * (col2.r - col1.r) / pt->pixw;
		col1.g += x * (col2.g - col1.g) / pt->pixw;
		col1.b += x * (col2.b - col1.b) / pt->pixw;

		mix(*pcol, col1, pt->blend);
	}
}

/*
 * textureinit
 *
 *	initialise the function pointers and fields for a texture pattern. 
 */
texture *
textureinit(name, d, field)
	char	*name;
	details	*d;
	int	field;
{
	texture		*tl;
	char		buf[128];
	details		*ld;
	int		i, found = FALSE;
	wlist		*wl = (wlist *)NULL;
	char		*map;
	unsigned short	pixw, pixh;
	tiletxt		*tilep;
	proctxt		*procp;
	transdata	*txttd;
	int		transformed;
	float		scal, turb, squeez;
	int		scalset, turbset, squeezset, have_block;

	have_block = 0;

	if (!noise_inited && strcmp("tile", name) != 0) {
		init_noise();
		noise_inited = 1;
	}

	tl = (texture *)smalloc(sizeof(texture));
	tl->nxt = (texture *)NULL;

	for (i = 0; i < NUMTEXT; i++) {
		if (strcmp(name, texturetable[i].name) == 0) {
			found = TRUE;
			tl->txtfun = texturetable[i].fun;
			break;
		}
	}

	if (!found) {
		sprintf(buf, "art: unknown procedural texture %s.\n", name);
		fatal(buf);
	}

	tl->modulate = field;
	if (texturetable[i].what == NORMAL_CHANGE && field == COLOUR)
		tl->modulate = -1;

	if (tl->txtfun == tile) {
		tl->u.t = tilep = (tiletxt *)smalloc(sizeof(tiletxt));
		tilep->blend = 1.0;
		tilep->scalew = 1.0;
		tilep->scaleh = 1.0;
	} else if (tl->txtfun == blocks || tl->txtfun == bricks) {
		tl->u.b = (blocktxt *)smalloc(sizeof(blocktxt));
		tl->u.b->flags = 0;
		have_block = 1;
	} else {
		tl->u.p = procp = (proctxt *)smalloc(sizeof(proctxt));
		procp->blend = 1.0;
		procp->map = (char *)NULL;
		procp->octaves = 6;
		procp->var[0] = 1.0;
		procp->var[1] = procp->var[2] = 0.0;
	}

	scal = 1.0;
	turb = squeez = 0.0;
	scalset = turbset = squeezset = FALSE;

	transformed = FALSE;

	if (astackp->nxt == (attr *)NULL) {
		astackp->nxt = (attr *)smalloc(sizeof(attr));
		astackp->nxt->lst = astackp;
		astackp->nxt->nxt = (attr *)NULL;
	}

	astackp = astackp->nxt;
	astackp->d = astackp->lst->d;

	if (mstackp->nxt == (mats *)NULL) {
		mstackp->nxt = (mats *)smalloc(sizeof(mats));
		mstackp->nxt->lst = mstackp;
		mstackp->nxt->nxt = (mats *)NULL;
	}

	mstackp = mstackp->nxt;
	mstackp->d = mstackp->lst->d;
	mident4(mstackp->d.vm);

	mstackp->d.nscales.x = mstackp->d.nscales.y = mstackp->d.nscales.z = 1.0;

	while (d != (details *)NULL) {
		switch (d->type) {
		case MAP:
			if (tl->txtfun == tile)
				warning("art: map in tile texture ignored.\n");
			else
				readascmap(procp, d->u.s);
			break;
		case MAPVALUES:
			procp->map = d->u.cm->m;
			procp->pixw = d->u.cm->n;
			procp->pixh = 1;
			break;
		case RANGE:
			if (tl->txtfun == tile)
				warning("art: range in tile texture ignored.\n");
			else
				procp->octaves = d->u.f;
			break;
		case SOURCE:
                        tl->u.w = d->u.w;
                        tl->u.w->nxt = wl;
                        wl = tl->u.w;
                        break;
		case BLEND:
			if (tl->txtfun == tile)
				tilep->blend = d->u.f;
			else
				procp->blend = d->u.f;
			break;
		case BLENDCOLOR:
			if (tl->txtfun == tile)
				warning("art: blendcolour in tile texture ignored.\n");
			else if (have_block) {
				tl->u.b->blendcolour.r = d->u.v.x;
				tl->u.b->blendcolour.g = d->u.v.y;
				tl->u.b->blendcolour.b = d->u.v.z;
			} else {
				procp->blendcolour.r = d->u.v.x;
				procp->blendcolour.g = d->u.v.y;
				procp->blendcolour.b = d->u.v.z;
			}
			break;
                case TURBULENCE:
			if (tl->txtfun == tile)
				warning("art: turbulence in tile texture ignored.\n");
			else {
				turb = d->u.f;
				turbset = TRUE;
			}
                        break;
                case SQUEEZE:
			if (tl->txtfun == tile)
				warning("art: squeeze in tile texture ignored.\n");
			else {
				squeez = d->u.f;
				squeezset = TRUE;
			}
                        break;
		case SCALEFACTORS:
			if (tl->txtfun == tile)
				warning("art: scalefactors in tile texture ignored.\n");
			else {
				scal = d->u.v.x;
				turb = d->u.v.y;
				squeez = d->u.v.z;
				scalset = turbset = squeezset = TRUE;
			}
			break;
                case SCALEFACTOR:
			if (have_block) {
				tl->u.b->mixval = d->u.f;
				tl->u.b->flags |= MIXEMUP;
				break;
			}
			if (tl->txtfun == tile)
				warning("art: scalefactor in tile texture ignored.\n");
			else {
				scal = d->u.f;
				scalset = TRUE;
			}
                        break;
		case VORTFILE:
			tileinit(d->u.s, &map, &pixw, &pixh);

			if (tl->txtfun == tile) {
				tilep->map = map;
				tilep->pixw = pixw;
				tilep->pixh = pixh;
				tilep->scalew *= pixw;
				tilep->scaleh *= pixh;
			} else {
				procp->map = map;
				procp->pixw = pixw;
				procp->pixh = pixh;
			}

			break;
		case SIZE:
			if (tl->txtfun == tile) {
				tilep->scalew /= d->u.v.x;
				tilep->scaleh /= d->u.v.y;

			} else
				warning("art: size in procedural texture ignored.\n");
			break;
		case GAPSIZE:
			if (have_block)
				tl->u.b->gapwidth = d->u.f;
			break;
		case GAPCOLOUR:
			if (have_block) {
				tl->u.b->gapcolour.r = d->u.v.x;
				tl->u.b->gapcolour.g = d->u.v.y;
				tl->u.b->gapcolour.b = d->u.v.z;
			}
			break;
		case BLOCKSIZE:
			if (have_block) {
				if (fabs(d->u.v.x) > 1.0e-6) {
					tl->u.b->blockwidth = d->u.v.x;
					tl->u.b->flags |= WIDTH_SET;
				}
				if (fabs(d->u.v.y) > 1.0e-6) {
					tl->u.b->blockheight = d->u.v.y;
					tl->u.b->flags |= HEIGHT_SET;
				}
				if (fabs(d->u.v.z) > 1.0e-6) {
					tl->u.b->blockdepth = d->u.v.z;
					tl->u.b->flags |= DEPTH_SET;
				}
			}
			break;
		case ROTATE:
			transformed = TRUE;
			rotate(d->u.rot.ang, d->u.rot.axis);
			break;
		case TRANSLATE:
			transformed = TRUE;
			translate(d->u.v.x, d->u.v.y, d->u.v.z);
			break;
		case SCALE:
			transformed = TRUE;
			scale(d->u.v.x, d->u.v.y, d->u.v.z);
			break;
		case TRANSFORM:
			transformed = TRUE;
			transform(*d->u.trans.m);
			free(d->u.trans.m);
			break;
		default:
			warning("art: illegal field in procedural texture ignored.\n");
		}
		ld = d;
		d = d->nxt;
		free(ld);
	}

	if (transformed) {
		txttd = tl->td = (transdata *)smalloc(sizeof(transdata));

		cp3x3(txttd->mat, mstackp->d.vm);

		txttd->trans.x = mstackp->d.vm[3][0];
		txttd->trans.y = mstackp->d.vm[3][1];
		txttd->trans.z = mstackp->d.vm[3][2];
		txttd->nscales = mstackp->d.nscales;
	} else
		tl->td = (transdata *)NULL;


	if (tl->txtfun == ripple) {
		init_ripples(NWAVES);
	}

	if (tl->txtfun != tile && !have_block) {
		procp->var[0] = procp->var[1] = procp->var[2] = 1.0;

		if (scalset)
			procp->var[0] = procp->var[1] = procp->var[2] = scal;

		if (turbset)
			procp->var[1] = turb;

		if (squeezset)
			procp->var[2] = squeez;

	}

	astackp = astackp->lst;
	mstackp = mstackp->lst;

	return(tl);
}

