/*
 * Reciprocal Net project
 * PublicStatusSC.java
 *
 * 01-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.site.shared.search;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples that
 * have a status code indicating that they are visible to the public.
 */
public class PublicStatusSC extends SearchConstraintGroup {
    /**
     * A constructor that fully initializes a {@code PublicStatusSC}. The
     * current implementation dictates that this {@code SearchConstraintGroup}
     * subclass is an 'OR' group that contains individual
     * {@code SearchConstraint} objects for each public status.
     */
    public PublicStatusSC() {
        super(OR);
        for (Integer sc : SampleWorkflowBL.getAllPublicStatusCodes()) {
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
