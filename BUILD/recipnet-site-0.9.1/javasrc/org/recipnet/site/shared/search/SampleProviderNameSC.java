/*
 * Reciprocal Net project
 * 
 * SampleProviderNameSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code SAMPLE_PROVIDER_NAME} attribute whose value contains the given text.
 */
public class SampleProviderNameSC extends AttributeSC {

    /**
     * A constructor that fully initializes a {@code SampleProviderNameSC}.
     * 
     * @param nameFragment a {@code String} that must match some part of a
     *        {@code SAMPLE_PROVIDER_NAME} attribute on a sample for it to be
     *        included in the search results
     */
    public SampleProviderNameSC(String nameFragment) {
        super(SampleTextBL.SAMPLE_PROVIDER_NAME, nameFragment, MATCHES_PART);
    }
}
