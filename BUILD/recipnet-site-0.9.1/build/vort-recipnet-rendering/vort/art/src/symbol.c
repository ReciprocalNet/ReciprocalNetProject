#include <stdio.h>
#include <math.h>
#include "art.h"

/*
 * findsym
 *
 *	retrieve a symbol from a symbol tree
 */
symbol *
findsym(tree, s)
	symbol	*tree;
	char	*s;
{
	int	val;

	if (tree != (symbol *)NULL) {
		val = strcmp(tree->name, s);
		if (val == 0)
			return(tree);
		if (val < 0)
			return(findsym(tree->left, s));
		else
			return(findsym(tree->right, s));
	}

	return((symbol *)NULL);
}

/*
 * insertsym
 *
 *	insert a symbol into a symbol tree.
 */
symbol *
insertsym(tree, s)
	symbol		**tree;
	char		*s;
{
	int		val;
	symbol		*sym;

	if (*tree != (symbol *)NULL) {
		val = strcmp((*tree)->name, s);
		if (val == 0)
			return(*tree);
		if (val < 0)
			return(insertsym(&(*tree)->left, s));
		else
			return(insertsym(&(*tree)->right, s));
	}

	*tree = sym = (symbol *)smalloc(sizeof(symbol));
	sym->name = s;
	sym->left = (symbol *)NULL;
	sym->right = (symbol *)NULL;

	return(sym);
}
