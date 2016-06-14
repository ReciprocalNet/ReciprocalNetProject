/*
 * Reciprocal Net project
 * 
 * KeywordSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.Collection;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraintGroup} that limits search results to those samples
 * that contain the given keywords. If the operator is set to 'AND', all of the
 * keywords must be present, if it is set to 'OR' then any one of the keywords
 * must be present.
 */
public class KeywordSC extends SearchConstraintGroup {

    /**
     * A constructor that fully initializes a {@code KeywordSC}.
     * 
     * @param operator one of the operators defined on
     *        {@code SearchConstraintGroup}
     * @param keywords a {@code Collection} of keywords ({@code String}
     *        objects) that must be present on a sample for it to be included in
     *        the search results
     */
    public KeywordSC(int operator, Collection<String> keywords) {
        super(operator);

        for (String keyword : keywords) {
            addChild(new AttributeSC(SampleTextBL.KEYWORD, keyword,
                    TextComparisonSC.MATCHES_BEGINNING));
        }
    }
}
