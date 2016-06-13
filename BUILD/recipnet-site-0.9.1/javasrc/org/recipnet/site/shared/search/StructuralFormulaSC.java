/*
 * Reciprocal Net project
 * 
 * StructuralFormulaSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code STRUCTUAL_FORMULA} attribute whose value contains the given text.
 */
public class StructuralFormulaSC extends AttributeSC {

    /**
     * A constructor that fully initializes a {@code StructuralFormulaSC}.
     * 
     * @param formulaFragment a {@code String} that must match some part of a
     *        {@code STRUCTURAL_FORMULA} attribute on a sample for it to be
     *        included in the search results
     */
    public StructuralFormulaSC(String formulaFragment) {
        super(SampleTextBL.STRUCTURAL_FORMULA, formulaFragment, MATCHES_PART);
    }
}
