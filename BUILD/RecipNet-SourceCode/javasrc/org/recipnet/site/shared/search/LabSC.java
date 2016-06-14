/*
 * Reciprocal Net project
 * 
 * LabSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code SearchConstraint} to limit search results to those samples that
 * belong to a given lab.
 */
public class LabSC extends SearchConstraint {

    /** An {@code Integer} representation of the id for the lab. */
    private final int labId;

    /**
     * A constructor that fully initializes a {@code LabSC}.
     */
    public LabSC(int labId) {
        this.labId = labId;
    }

    /** @return the labId that was provided to the constructor */
    public int getLabId() {
        return labId;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation returns a
     * where clause fragment comparing the sample table's "lab_id" to the
     * parameter ({@code labId}) added to the {@code parameters}
     * {@code Collection}.
     * 
     * @param tableTracker needed to get an alias for the 'samples' table
     * @param parameters a {@code Collection} to which the labId value is added
     * @return a {@code String} equating the sample's labId with the labId
     *         assigned to this {@code LabSC}
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(labId));
        return tableTracker.getTableAlias("samples", this) + ".lab_id = ?";
    }

    /** Equality is based on class and labId. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof LabSC) {
            return ((this.getClass() == obj.getClass())
                    && (this.labId == ((LabSC) obj).labId));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + String.valueOf(this.labId)).hashCode();
    }
}
