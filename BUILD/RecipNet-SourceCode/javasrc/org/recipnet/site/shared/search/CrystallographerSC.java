/*
 * Reciprocal Net project
 * 
 * CrystallographerSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples with a
 * {@code CRYSTALLOGRAPHER_NAME} attribute whose value contains the given text.
 */
public class CrystallographerSC extends AttributeSC {

    /**
     * A constructor that fully initializes a {@code CrystallographerSC}.
     * 
     * @param nameFragment a {@code String} that must match some part of a
     *        {@code CRYSTALLOGRAPHER_NAME} attribute on a sample for it to be
     *        included in the search results
     */
    public CrystallographerSC(String nameFragment) {
        super(SampleTextBL.CRYSTALLOGRAPHER_NAME, nameFragment, MATCHES_PART);
    }
}
