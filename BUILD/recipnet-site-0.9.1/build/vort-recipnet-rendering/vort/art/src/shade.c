/*
 * the shading routines and their utilities
 */
#include <stdio.h>
#include <math.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern object	*oblist;
extern light	*lights;
extern hlist	*fhlist;
extern int	maxhitlevel;

extern colour	backcol;
extern colour	ambient;

extern double	power();

extern hlist	*trace();

extern float	randtable[], *randp, *erandp;

extern float    fogfactor, rfactor;
extern colour	hazecolour;

extern float    falloff, ri;

extern short	raynumber;

extern hlist	*(*intersects[])();

extern int	selfshadowing[];
extern int	checkbbox[];

extern void	(*normal[])();

extern shadedata	*sstackp, *sstack;

/*
 * trace a ray to a light source, processing any transparent surfaces
 * hit along the way.
 */
int
tracelight(obj, l, lr, col, cosang, hitlevel)
	object	*obj;
	light	*l;
	ray	*lr;
	pixel	*col;
	float	cosang;
	int	hitlevel;
{
	object	*o;
	vector	nalt;
	ray	tmp;
	surface	sp;
	hlist	*hit, *p, *lp, *end;
	float	fact, t, peturb, lastt;
	colour	base;

	if (l->shadows) {
		lr->raynumber = raynumber++;
		lr->type = SHADOW_RAY;

		if (!selfshadowing[obj->type]) {
			obj->lastray.raynumber = lr->raynumber;
			obj->lastray.t = 0.0;
		}

		o = l->lasthits[hitlevel];

		if (o != (object *)NULL) {
			o->lastray.raynumber = lr->raynumber;
			o->lastray.t = 0.0;

			if ((checkbbox[o->type] && missedbbox(lr, &o->bb)) ||
			 (hit = intersects[o->type](lr, o, &end)) == (hlist *)NULL)
				hit = trace(lr, oblist);
			else if (hit->t >= lr->maxt) {
				for (lp = hit; lp != (hlist *)NULL; lp = p) {
					p = lp->nxt;
					release(lp);
				}

				hit = trace(lr, oblist);
			}
		} else
			hit = trace(lr, oblist);
	} else
		hit = (hlist *)NULL;

	base.r = l->c.r;
	base.g = l->c.g;
	base.b = l->c.b;

	o = (object *)NULL;

	while (hit != (hlist *)NULL) {
		sp = *hit->obj->s;

		o = hit->obj;
		lastt = hit->t;

		if (o->txtlist != (texture *)NULL) {
			nalt.x = nalt.y = nalt.z = 0.0;
			tmp.org.x = lr->org.x + lr->dir.x * hit->t;
			tmp.org.y = lr->org.y + lr->dir.y * hit->t;
			tmp.org.z = lr->org.z + lr->dir.z * hit->t;
			dotexture(o, &tmp.org, &nalt, hit->type, &sp);
		}

		if (sp.trans.r == 0.0 && sp.trans.g == 0.0 && sp.trans.b == 0.0)
			break;

		if (hit->t < lr->maxt && o != (object *)NULL) {
			if (hit->obj == o)
				fact = 1.0 / (1.0 + o->s->falloff * (lastt - hit->t));
			else
				fact = 1.0;

			base.r *= sp.trans.r * fact;
			base.g *= sp.trans.g * fact;
			base.b *= sp.trans.b * fact;
		}


		if ((p = hit->nxt) == (hlist *)NULL) {
			lr->org.x += lr->dir.x * hit->t;
			lr->org.y += lr->dir.y * hit->t;
			lr->org.z += lr->dir.z * hit->t;

			lr->raynumber = raynumber++;
			lr->maxt -= hit->t;

			if (obj->type == POLYGON || obj->type == TRIANGLE
			   || obj->type == RING) {
				obj->lastray.raynumber = lr->raynumber;
				obj->lastray.t = 0.0;
			}

			release(hit);

			hit = trace(lr, oblist);
		} else {
			release(hit);

			hit = p;
		}
	}

	if (hit != (hlist *)NULL) {

		t = hit->t;

		if (t >= lr->maxt || hit->obj->incsg)
			l->lasthits[hitlevel] = (object *)NULL;
		else
			l->lasthits[hitlevel] = hit->obj;

		for (lp = hit; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	} else {
		l->lasthits[hitlevel] = (object *)NULL;
		t = lr->maxt;
	}

	if (l->type == DISTANT)
		fact = 1.0;
	else if (l->type == DIRECTIONAL) {
		if (l->beamdist != 0.0)
			fact = pow(cosang, l->beamdist);
		else
			fact = 1.0;
		if (l->cosin != 2.0)
			fact *= linsmooth(l->cosedge, l->cosin, cosang);
		fact /= (1.0 + falloff * t);
	} else
		fact = 1.0 / (1.0 + falloff * t);

	col->r += base.r * fact;
	col->g += base.g * fact;
	col->b += base.b * fact;

				/* maybe the light is closer */
	return(t >= lr->maxt);
}

/*
 * checklight
 *
 *	is the light l visible to us from loc? TRUE if
 * it is, FALSE if it isn't. lr is returned as the ray
 * hitting l from loc, and col is the intensity value of
 * the light.
 */
int
checklight(obj, l, loc, lr, col, n, prod, hitlevel)
	object	*obj;
	light	*l;
	vector	*loc;
	ray	*lr;
	pixel	*col;
	vector	*n;
	float	*prod;
	int	hitlevel;
{
	object		*o;
	vector		dir;
	ray		tmp;
	float		dist, t, peturb, lastt, cosang;
	int		count, hit;
	float		u, v, deltaU, deltaV;
	sampleMask	*mask;
	float		theta;
	float		circleRadius, circleDistance, fact;

	if (l->type == DISTANT) {
		lr->dir = l->dir;
		lr->org = *loc;

		if ((*prod = dprod(*n, lr->dir)) < 0.0)
			return(FALSE);

		dist = HUGE_DIST;
	} else {
		vsub(lr->dir, l->org, *loc);

		dist = sqrt(dprod(lr->dir, lr->dir));

		lr->dir.x /= dist;
		lr->dir.y /= dist;
		lr->dir.z /= dist;

		dir = lr->dir;

		if ((*prod = dprod(*n, dir)) < 0.0)
			return(FALSE);

		if (l->type == DIRECTIONAL && (cosang = dprod(dir, l->dir)) < l->cosedge) {
			return(FALSE);
		}

		lr->org = *loc;
	}

	lr->maxt = dist;

	col->r = col->g = col->b = 0.0;

	if (l->rays > 1 && l->rad != 0.0 && dist != 0.0) {

                theta = asin(l->rad / dist);
                circleRadius = l->rad * cos(theta);
                circleDistance = dist - l->rad * (l->rad / dist);

                fact = circleRadius / circleDistance;
                lr->maxt = circleDistance;

		mask = getCircularSampleMask(l->rays);

		hit = FALSE;

		for (count = 0; count != l->rays; count++)
		{
			tmp = *lr;

			deltaU = mask->magnitude * (1.0 - 2 * randnum());
			deltaV = mask->magnitude * (1.0 - 2 * randnum());

			u = fact * (mask->points[count].u + deltaU);
			v = fact * (mask->points[count].v + deltaV);

			generatePerturbationVector(tmp.dir, u, v, &dir);

			vadd(tmp.dir, tmp.dir, dir);

			normalise(tmp.dir);

			if (tracelight(obj, l, &tmp, col, cosang, hitlevel))
			{
				hit = TRUE;
			}
		}

		return hit;
	}

	return tracelight(obj, l, lr, col, cosang, hitlevel);
}

/*
 * shade
 *
 *	returns the shading info for an object with reflection and/or
 * transparency.
 */
void
shade(pix, i, hit, hitlevel)
	pixel		*pix;
	register ray	*i;
	hlist		*hit;
	int		hitlevel;
{
	hlist		*nxthit, *p, *lp;
	light		*l;
	pixel		objcol, othercol;
	ray		lr, ir;
	vector		n, ns, mid, cv, sv, objn, nalt;
	texture		*txt;
	geometry	*gm;
	polygon		*poly;
	vector		*vcolp, vcol, rvcol;
	surface		sp;
	transdata	*td;
	facet		*f;
	register object	*o;
	register int	leaving;
	float		len, prod;
	colour		dif, spec;
	register float	fact, ksfact, kdfact, ndoti, lensvsqu;

	ir.dir = i->dir;
	smult(ir.dir, hit->t);
	vadd(ir.org, i->org, ir.dir);

	o = hit->obj;
	sp = *o->s;

	switch (hit->obj->type) {
	case GEOMETRY:
		if (o->obj.geo->colours != (vector *)NULL) {
			gm = o->obj.geo;
			vcolp = &gm->colours[gm->colourtable[gm->faces[hit->type].index]];
			sp.c.r = vcolp->x;
			sp.c.g = vcolp->y;
			sp.c.b = vcolp->z;
		} else if (o->obj.geo->vcolours != (vector *)NULL) {
			interpcol(o, hit->type, &ir.org, &rvcol);
			sp.c.r = rvcol.x;
			sp.c.g = rvcol.y;
			sp.c.b = rvcol.z;
		}

		if (o->obj.geo->transp != (float *)NULL) {
			interptrans(o, hit->type, &ir.org, &rvcol);
			sp.trans.r = rvcol.x;
			sp.trans.g = rvcol.y;
			sp.trans.b = rvcol.z;
		}
		break;
	case HFIELD:
		if (o->obj.hfield->colours != (float *)NULL) {
			getcol(o, hit->type, &ir.org, &rvcol);
			sp.c.r = rvcol.x;
			sp.c.g = rvcol.y;
			sp.c.b = rvcol.z;
		}
		break;
	case POLYGON:
	case TRIANGLE:
		if (o->obj.ply->colours != (vector *)NULL) {
			poly = o->obj.ply;
			vcol.x = sp.c.r;
			vcol.y = sp.c.g;
			vcol.z = sp.c.b;
			interp(poly, &vcol, poly->colours, &ir.org, &rvcol);
			sp.c.r = rvcol.x;
			sp.c.g = rvcol.y;
			sp.c.b = rvcol.z;
		} 
		break;
	}

	/*
	 * calculate the normal and object colour if necessary
	 */
	normal[o->type](&objn, &ir.org, o, hit->type);

	td = o->td;

	if (o->txtlist != (texture *)NULL)
	{
		nalt.x = nalt.y = nalt.z = 0.0;

		dotexture(o, &ir.org, &nalt, hit->type, &sp);

		if (nalt.x != 0.0 && nalt.y != 0.0 && nalt.z != 0.0)
		{
			/*
			 * preserve the magnitude of the perturbation...
			 */
			len = sqrt(dprod(objn, objn));

			smult(nalt, len);

			vadd(objn, objn, nalt);
		}
	}

	v3x3tmult(n, objn, td->mat);

	normalise(n);

	if ((ndoti = dprod(n, i->dir)) > 0.0) { /* normal facing away from us */
		smult(n, -1.0);
		ndoti = -ndoti;
		leaving = TRUE;
	} else
		leaving = FALSE;

	kdfact = sp.kd;
	ksfact = sp.ks;
	objcol.r = sp.c.r;
	objcol.g = sp.c.g;
	objcol.b = sp.c.b;

	dif.r = dif.g = dif.b = 0.0;
	spec.r = spec.g = spec.b = 0.0;

	for (l = lights; l != (light *)NULL; l = l->nxt) {
		lr.orgobj = o;
		lr.orgtype = hit->type;
		if (checklight(o, l, &ir.org, &lr, &othercol, &n, &prod, hitlevel)) {
			fact = prod * kdfact;
			dif.r += fact * othercol.r;
			dif.g += fact * othercol.g;
			dif.b += fact * othercol.b;
			if (ksfact != 0.0) {
				ns = n;
				smult(ns, -2 * prod);
				vadd(mid, lr.dir, ns);
				normalise(mid);
				fact = dprod(i->dir, mid);
				if (fact > 0.0) {
					fact = power(fact, sp.ksexp) * ksfact;
					spec.r += fact * othercol.r;
					spec.g += fact * othercol.g;
					spec.b += fact * othercol.b;
				}
			}
		}
	}

	pix->r = (sp.a.r + dif.r) * objcol.r + spec.r;
	pix->g = (sp.a.g + dif.g) * objcol.g + spec.g;
	pix->b = (sp.a.b + dif.b) * objcol.b + spec.b;

	if (sp.trans.r != 0.0 || sp.trans.g != 0.0 || sp.trans.b != 0.0) {
		pix->r *= (1.0 - sp.trans.r);
		pix->g *= (1.0 - sp.trans.g);
		pix->b *= (1.0 - sp.trans.b);
	}

	/*
	 * refraction  - Roy Hall's method.
	 */
	if (sp.ri != 0.0 && sp.ri != 1.0 && hitlevel < maxhitlevel &&
	   (sp.trans.r != 0.0 || sp.trans.g != 0.0 || sp.trans.b != 0.0)) {

		cv.x = ndoti * n.x;
		cv.y = ndoti * n.y;
		cv.z = ndoti * n.z;

		if (o->incsg == SUBTRACTED)
			leaving = !leaving;

		if (leaving) {
					/* 
					 * just in case ray starts in an object
					 */
			if (sstackp != sstack)
				ir.ri = (sstackp - 1)->ri;
			else
				ir.ri = ri;
		} else
			ir.ri = sp.ri;

		fact = i->ri / ir.ri;
		 
		sv.x = fact * (i->dir.x - cv.x);
		sv.y = fact * (i->dir.y - cv.y);
		sv.z = fact * (i->dir.z - cv.z);

		/*
		 * greater than or equal 1.0 means internal reflection
		 */
		if ((lensvsqu = dprod(sv, sv)) < 1.0) {

			fact = sqrt(1.0 - lensvsqu);

			ir.dir.x = sv.x - (n.x * fact);
			ir.dir.y = sv.y - (n.y * fact);
			ir.dir.z = sv.z - (n.z * fact);

			ir.raynumber = raynumber++;
			ir.type = TRANSPARENCY_RAY;
			ir.orgobj = o;
			ir.orgtype = hit->type;

			if (o->type == POLYGON || o->type == TRIANGLE
			   || o->type == RING) {
				o->lastray.raynumber = ir.raynumber;
				o->lastray.t = 0.0;
			}

			nxthit = trace(&ir, oblist);

			if (nxthit != (hlist *)NULL) {

				sstackp++;
				sstackp->ri = sp.ri;
				sstackp->falloff = sp.falloff;

				shade(&objcol, &ir, nxthit, hitlevel + 1);

				sstackp--;

				pix->r += sp.trans.r * objcol.r;
				pix->g += sp.trans.g * objcol.g;
				pix->b += sp.trans.b * objcol.b;
			} else {
				pix->r += sp.trans.r * backcol.r;
				pix->g += sp.trans.g * backcol.g;
				pix->b += sp.trans.b * backcol.b;
			}

			for (lp = nxthit; lp != (hlist *)NULL; lp = p) {
				p = lp->nxt;
				release(lp);
			}
		}
	}

	if (hitlevel < maxhitlevel &&
		(sp.refl.r != 0.0 || sp.refl.g != 0.0 || sp.refl.b != 0.0)) {

		smult(n, 2 * ndoti);
		vsub(ir.dir, i->dir, n);

		ir.raynumber = raynumber++;
		ir.ri = i->ri;
		ir.type = REFLECTION_RAY;
		ir.orgobj = o;
		ir.orgtype = hit->type;

		if (!selfshadowing[o->type]) {
			o->lastray.raynumber = ir.raynumber;
			o->lastray.t = 0.0;
		}

		nxthit = trace(&ir, oblist);

		if (nxthit != (hlist *)NULL) {
			shade(&objcol, &ir, nxthit, hitlevel + 1);
			pix->r += sp.refl.r * objcol.r;
			pix->g += sp.refl.g * objcol.g;
			pix->b += sp.refl.b * objcol.b;
		} else {
			pix->r += sp.refl.r * backcol.r;
			pix->g += sp.refl.g * backcol.g;
			pix->b += sp.refl.b * backcol.b;
		}

		for (lp = nxthit; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	}

	if (sp.ri == 1.0 && hitlevel < maxhitlevel &&
	   (sp.trans.r != 0.0 || sp.trans.g != 0.0 || sp.trans.b != 0.0)) {

		ir.dir = i->dir;

		ir.raynumber = raynumber++;
		ir.ri = sp.ri;
		ir.type = TRANSPARENCY_RAY;
		ir.orgobj = o;
		ir.orgtype = hit->type;

		if (o->type == POLYGON || o->type == TRIANGLE
		   || o->type == RING) {
			o->lastray.raynumber = ir.raynumber;
			o->lastray.t = 0.0;
		}

		nxthit = trace(&ir, oblist);

		if (nxthit != (hlist *)NULL) {
			shade(&objcol, &ir, nxthit, hitlevel + 1);
			pix->r += sp.trans.r * objcol.r;
			pix->g += sp.trans.g * objcol.g;
			pix->b += sp.trans.b * objcol.b;
		} else {
			pix->r += sp.trans.r * backcol.r;
			pix->g += sp.trans.g * backcol.g;
			pix->b += sp.trans.b * backcol.b;
		}

		for (lp = nxthit; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}
	}

	if (sp.alpha != 1.0) {

		fact = sp.alpha / (1.0 + sstackp->falloff * hit->t);

		pix->r = fact * pix->r;
		pix->g = fact * pix->g;
		pix->b = fact * pix->b;

		ir.dir = i->dir;

		ir.raynumber = raynumber++;
		ir.ri = sp.ri;

		ir.type = TRANSPARENCY_RAY;
		ir.orgobj = o;
		ir.orgtype = hit->type;

		if (o->type == POLYGON || o->type == TRIANGLE
		   || o->type == RING) {
			o->lastray.raynumber = ir.raynumber;
			o->lastray.t = 0.0;
		}

		nxthit = trace(&ir, oblist);

		if (nxthit != (hlist *)NULL) {

			shade(&objcol, &ir, nxthit, hitlevel);

			fact = (1.0 + sstackp->falloff * nxthit->t) / (1.0 + sstackp->falloff * (hit->t + nxthit->t));

			objcol.r = (1.0 - sp.alpha) * fact * objcol.r;
			objcol.g = (1.0 - sp.alpha) * fact * objcol.g;
			objcol.b = (1.0 - sp.alpha) * fact * objcol.b;
		} else {
			objcol.r = (1.0 - sp.alpha) * backcol.r;
			objcol.g = (1.0 - sp.alpha) * backcol.g;
			objcol.b = (1.0 - sp.alpha) * backcol.b;
		}

		for (lp = nxthit; lp != (hlist *)NULL; lp = p) {
			p = lp->nxt;
			release(lp);
		}

		pix->r += objcol.r;
		pix->g += objcol.g;
		pix->b += objcol.b;
	} else {

		/*
		 *	Beer's law for fog and haze....
		 */
		if (fogfactor != 0.0) {
			fact = 1.0 - (1.0 - rfactor) * exp((double)(-fogfactor * hit->t));
			mix(*pix, hazecolour, fact);
		}

		fact = 1.0 / (1.0 + sstackp->falloff * hit->t);

		pix->r *= fact;
		pix->g *= fact;
		pix->b *= fact;
	}

	pix->r = clamp(pix->r);
	pix->g = clamp(pix->g);
	pix->b = clamp(pix->b);
}
