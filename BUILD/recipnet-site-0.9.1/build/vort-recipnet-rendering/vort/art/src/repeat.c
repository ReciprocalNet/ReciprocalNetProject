#include <math.h>
#include <stdio.h>
#include "art.h"
#include "objs.h"
#include "macro.h"
#include "gram.h"

extern hlist	*fhlist;

extern object	*objectinit(), *csginit();
extern object	**oblistsp;

extern attr	*astackp;
extern mats	*mstackp;

/*
 * dorepeat
 *
 *	execute the body of a repeat loop.
 */
dorepeat(expr, stmts)
	expression	*expr;
	details		*stmts;
{
	int		i, count;
	details		*d, *nxtd, *head;
	details		*dl, *shead, *stail, *newstmnt;
	expression	*nexpr;
	object		*obj, *ohead;

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

	calctransforms(mstackp);

	mstackp = mstackp->nxt;
	mstackp->d = mstackp->lst->d;
	mident4(mstackp->d.vm);

	/*
	 * get list back in right order.
	 */
	head = (details *)NULL;
	for (d = stmts; d != (details *)NULL; d = nxtd) {
		nxtd = d->nxt;
		d->nxt = head;
		head = d;
	}

	count = eval_iexpr(expr);
	for (i = 0; i != count; i++) {
		for (d = head; d != (details *)NULL; d = d->nxt) {
			switch (d->type) {
			case SCALE:
				scale(d->u.v.x, d->u.v.y, d->u.v.z);
				break;
			case TRANSLATE:
				translate(d->u.v.x, d->u.v.y, d->u.v.z);
				break;
			case ROTATE:
				rotate(d->u.rot.ang, d->u.rot.axis);
				break;
			case TRANSFORM:
				transform(*d->u.trans.m);
				free(d->u.trans.m);
				break;
			case OBJECT:
				if ((ohead = objectinit(d->u.obj.sym, (details *)NULL)) != (object *)NULL) {
					for (obj = ohead; obj->nxt != (object *)NULL; obj = obj->nxt)
						;

					obj->nxt = *oblistsp;
					*oblistsp = ohead;
				}
				break;
			case REPEAT:
				shead = stail = (details *)NULL;

				for (dl = d->u.rpt.stmt; dl != (details *)NULL; dl = dl->nxt) {
					newstmnt = (details *)smalloc(sizeof(details));                                    
					copydetails(newstmnt, dl);

					if (stail == (details *)NULL)
						shead = stail = newstmnt;
					else {
						stail->nxt = newstmnt;
						stail = newstmnt;
					}

					stail->nxt = (details *)NULL;
				}

				nexpr = (expression *)smalloc(sizeof(expression));
				*nexpr = *d->u.rpt.expr;

				dorepeat(nexpr, shead);
				break;
			default:
				fatal("art: bad type in repeat loop.\n");
			}
		}
	}

	for (d = head; d != (details *)NULL; d = nxtd) {
		nxtd = d->nxt;
		free(d);
	}

	astackp = astackp->lst;
	mstackp = mstackp->lst;
}

