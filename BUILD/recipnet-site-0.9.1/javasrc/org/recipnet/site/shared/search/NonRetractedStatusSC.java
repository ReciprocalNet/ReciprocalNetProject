/*
 * Reciprocal Net project
 * 
 * NonRetractedStatusSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples that have
 * not be retracted.
 */
public class NonRetractedStatusSC extends SearchConstraintGroup {

    /**
     * A constructor that fully initializes a {@code NonRetractedStatusSC}. The
     * current implementation dictates that this {@code SearchConstraintGroup}
     * subclass is a 'NOR' group that contains individual
     * {@code SearchConstraint} objects for each retracted status.
     */
    public NonRetractedStatusSC() {
        super(NOR);

        for (Integer sc : SampleWorkflowBL.getAllRetractedStatusCodes()) {
            addChild(new StatusSC(sc.intValue()));
        }
    }

    /** All instances of this class are equal. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else {
            return getClass() == obj.getClass();
        }
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }
}
