/*
 * Reciprocal Net project
 * 
 * EmpiricalFormulaSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code EMPIRICAL_FORMULA} attribute whose value contains the given text.
 */
public class EmpiricalFormulaSC extends AttributeSC {

    /**
     * A constructor that fully initializes an {@code EmpiricalFormulaSC}.
     * 
     * @param formulaFragment a {@code String} that must match some part of an
     *        {@code EMPIRICAL_FORMULA} attribute on a sample for it to be
     *        included in the search results
     */
    public EmpiricalFormulaSC(String formulaFragment) {
        super(SampleTextBL.EMPIRICAL_FORMULA, formulaFragment, MATCHES_PART);
    }
}
