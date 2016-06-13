/*
 * Reciprocal Net project
 * 
 * FinishedStatusSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples for which
 * processing has been completed.
 */
public class FinishedStatusSC extends SearchConstraintGroup {

    /**
     * The constructor that fully initializes a {@code FinishedStatusSC}. The
     * current implmentation dictates that this {@code SearchConstraintGroup}
     * subclass is an 'OR' group that contains individual
     * {@code StatusSearchConstraint} objects for each status where processing
     * has finished.
     */
    public FinishedStatusSC() {
        super(OR);
        for (Integer sc : SampleWorkflowBL.getAllFinishedStatusCodes()) {
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
            return this.getClass() == obj.getClass();
        }
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }
}
