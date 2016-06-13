/*
 * Reciprocal Net project
 * 
 * SmilesFormulaSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code SMILES_FORMULA} annotation whose value contains the given text.
 */
public class SmilesFormulaSC extends AnnotationSC {

    /**
     * A constructor that fully initializes a {@code SmilesFormulaSC}.
     * 
     * @param formulaFragment a {@code String} that must match some part of a
     *        {@code SMILES_FORMULA} attribute on a sample for it to be included
     *        in the search results
     */
    public SmilesFormulaSC(String formulaFragment) {
        super(SampleTextBL.SMILES_FORMULA, formulaFragment, MATCHES_PART);
    }
}
