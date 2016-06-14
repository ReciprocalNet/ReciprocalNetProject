/*
 * Reciprocal Net project
 * 
 * FormulaAtomSC.java
 *
 * 14-Mar-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.Collection;

/**
 * A {@code SearchConstraint} representing an AND group of {@code AtomCountSC}.
 * This group is useful only in that is provides a well defined group whose
 * children can always be expected to be {@code AtomCountSC} objects.
 */
public class FormulaAtomSC extends SearchConstraintGroup {

    /** Constructs a fully initialized {@code FormulaAtomSC}. */
    public FormulaAtomSC(Collection<AtomCountSC> atomCountSCs) {
        super(AND, atomCountSCs);
    }
}
