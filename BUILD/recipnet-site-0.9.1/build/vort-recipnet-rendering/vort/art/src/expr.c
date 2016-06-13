#include <stdio.h>
#include <math.h>
#include "art.h"

float		eval_fexpr();

static	symbol	*vars = (symbol *)NULL;

/*
 * doassignment
 *
 *	process the expression string in s.
 */
doassignment(s)
	char	*s;
{
	char	*eq, *p;
	symbol	*var;

	for (eq = s; *eq != 0 && *eq != '='; eq++)
		;

	if (eq == 0)
		usage("bad assignment in -D.\n");

	for (p = s; *p != 0 && *p != '.'; p++)
		;

	*eq = 0;
	var = insertsym(&vars, s);

	if (*p == '.') {
		var->type = EXP_FLOAT;
		sscanf(eq + 1, "%f", &var->u.f);
	} else {
		var->type = EXP_INT;
		sscanf(eq + 1, "%i", &var->u.i);
	}
}

/*
 * defvar
 *
 *	define a variable
 */
defvar(s, e)
	char		*s;
	expression	*e;
{
	symbol		*var;

	var = insertsym(&vars, s);

	var->type = e->type;

	switch (e->type) {
	case EXP_FLOAT:
		var->u.f = e->u.f;
		break;
	case EXP_INT:
		var->u.i = e->u.i;
		break;
	default:
		fatal("art: bad expression in defvar.\n");
	}

	free(e);
}

/*
 * get_varexpr
 *
 *	create an expression node for a variable.
 */
expression *
get_varexpr(s)
	char	*s;
{
	expression	*e;
	symbol		*sym;
	char		buf[BUFSIZ];

	e = (expression *)smalloc(sizeof(expression));

	if ((sym = findsym(vars, s)) == (symbol *)NULL) {
		sprintf(buf, "art: variable %s not defined.\n", s);
		fatal(buf);
	}

	e->type = sym->type;

	switch (e->type) {
	case EXP_FLOAT:
		e->u.f = sym->u.f;
		break;
	case EXP_INT:
		e->u.i = sym->u.i;
		break;
	default:
		fatal("art: bad variable in get_varexpr.\n");
	}

	return(e);
}

/*
 * get_ival
 *
 *	retrieve a symbol value returning an int.
 */
int
get_ival(s)
	char	*s;
{
	int	i;
	symbol	*sym;
	char	buf[BUFSIZ];

	sym = findsym(vars, s);

	if (sym == (symbol *)NULL) {
		sprintf(buf, "art: symbol %s not defined.\n", s);
		fatal(buf);
	}

	if (sym->type == EXP_INT)
		i = sym->u.i;
	else if (sym->type == EXP_FLOAT)
		i = sym->u.f;

	return(i);
}

/*
 * get_fval
 *
 *	retrieve a symbol value returning a float.
 */
float
get_fval(s)
	char	*s;
{
	float	f;
	symbol	*sym;
	char	buf[BUFSIZ];

	sym = findsym(vars, s);

	if (sym == (symbol *)NULL) {
		sprintf(buf, "art: symbol %s not defined.\n", s);
		fatal(buf);
	}

	if (sym->type == EXP_INT)
		f = sym->u.i;
	else if (sym->type == EXP_FLOAT)
		f = sym->u.f;

	return(f);
}

/*
 * get_type
 *
 *	work the return type for an expression
 */
int
get_type(type, left, right)
	int		type;
	expression	*left, *right;
{
	if (right == (expression *)NULL)
		return(left->type);

	if (left->type == EXP_FLOAT && right->type == EXP_FLOAT)
		return(EXP_FLOAT);

	if (left->type == EXP_INT && right->type == EXP_INT)
		return(EXP_INT);

	if (left->type == EXP_INT && right->type == EXP_FLOAT)
		return(EXP_FLOAT);

	if (left->type == EXP_FLOAT && right->type == EXP_INT)
		return(EXP_FLOAT);

	return(type);
}

/*
 * eval_fexpr
 *
 *	evaluate a floating point expression
 */
float 
eval_fexpr(e)
	expression	*e;
{
	float	f;

	switch (e->type) {
	case EXP_INT:
		f = e->u.i;
		free(e);
		break;
	case EXP_FLOAT:
		f = e->u.f;
		free(e);
		break;
	default:
		fatal("art: bad type in eval_fexpr.\n");
	}

	return(f);
}

/*
 * eval_iexpr
 *
 *	evaluate an integer expression
 */
int
eval_iexpr(e)
	expression	*e;
{
	int	i;

	switch (e->type) {
	case EXP_INT:
		i = e->u.i;
		free(e);
		break;
	case EXP_FLOAT:
		i = e->u.f;
		free(e);
		break;
	default:
		fatal("art: bad type in eval_iexpr.\n");
	}

	return(i);
}

/*
 * get_expr
 *
 *	get an expression node, compressing it if possible
 */
expression *
get_expr(type, left, right)
	int		type;
	expression	*left, *right;
{
	int		ntype;
	expression	*expr;

	ntype = get_type(type, left, right);

	expr = (expression *)smalloc(sizeof(expression));

	switch (type) {
	case EXP_UMINUS:
		switch (ntype) {
		case EXP_FLOAT:
			expr->type = EXP_FLOAT;
			expr->u.f = -eval_fexpr(left);
			break;
		case EXP_INT:
			expr->type = EXP_INT;
			expr->u.i = -eval_iexpr(left);
			break;
		default:
			fatal("art: bad subexpression type in get_expr.\n");
		}
		break;
	case EXP_ADD:
		switch (ntype) {
		case EXP_FLOAT:
			expr->type = EXP_FLOAT;
			expr->u.f = eval_fexpr(left) + eval_fexpr(right);
			break;
		case EXP_INT:
			expr->type = EXP_INT;
			expr->u.i = eval_iexpr(left) + eval_iexpr(right);
			break;
		default:
			fatal("art: bad subexpression type in get_expr.\n");
		}
		break;
	case EXP_SUB:
		switch (ntype) {
		case EXP_FLOAT:
			expr->type = EXP_FLOAT;
			expr->u.f = eval_fexpr(left) - eval_fexpr(right);
			break;
		case EXP_INT:
			expr->type = EXP_INT;
			expr->u.i = eval_iexpr(left) - eval_iexpr(right);
			break;
		default:
			fatal("art: bad subexpression type in get_expr.\n");
		}
		break;
	case EXP_DIV:
		switch (ntype) {
		case EXP_FLOAT:
			expr->type = EXP_FLOAT;
			expr->u.f = eval_fexpr(left) / eval_fexpr(right);
			break;
		case EXP_INT:
			expr->type = EXP_INT;
			expr->u.i = eval_iexpr(left) / eval_iexpr(right);
			break;
		default:
			fatal("art: bad subexpression type in get_expr.\n");
		}
		break;
	case EXP_MUL:
		switch (ntype) {
		case EXP_FLOAT:
			expr->type = EXP_FLOAT;
			expr->u.f = eval_fexpr(left) * eval_fexpr(right);
			break;
		case EXP_INT:
			expr->type = EXP_INT;
			expr->u.i = eval_iexpr(left) * eval_iexpr(right);
			break;
		default:
			fatal("art: bad subexpression type in get_expr.\n");
		}
		break;
	default:
		fatal("art: bad expression type in get_expr.\n");
	}

	return(expr);
}
