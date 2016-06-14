/*
 * Reciprocal Net project
 * 
 * MoietyFormulaSearchConstraint.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code MOIETY_FORMULA} attribute whose value contains the given text.
 */
public class MoietyFormulaSC extends AttributeSC {

    /**
     * A constructor that fully initializes a {@code MoietyFormulaSC}.
     * 
     * @param formulaFragment a {@code String} that must match some part of a
     *        {@code MOIETY_FORMULA} attribute on a sample for it to be included
     *        in the search results
     */
    public MoietyFormulaSC(String formulaFragment) {
        super(SampleTextBL.MOIETY_FORMULA, formulaFragment, MATCHES_PART);
    }
}
