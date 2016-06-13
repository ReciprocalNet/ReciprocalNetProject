/*
 * Reciprocal Net project
 * 
 * SampleIdSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code SearchConstraint} to limit search results to those samples with
 * sampleId's that are equal to the given sampleId.
 */
public class SampleIdSC extends SearchConstraint {

    /** The sampleId of the sample that may be included in this search. */
    private final int sampleId;

    /** A constructor that fully initializes this {@code SampleIdSC}. */
    public SampleIdSC(int sampleId) {
        this.sampleId = sampleId;
    }

    /** Gets the sampleId that was provided to the constructor. */
    public int getSampleId() {
        return this.sampleId;
    }

    /**
     * {@inheritDoc}; this version generates a {@code String} that may be used
     * as a portion of the SQL WHERE clause to require that a sample have the
     * given id value.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters,
            @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(sampleId));
        return tableTracker.getTableAlias("samples", this) + ".id = ?";
    }

    /** Equality is based on class and sampleId. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof SampleIdSC) {
            SampleIdSC sisc = (SampleIdSC) obj;
            return ((this.getClass() == sisc.getClass())
                    && (this.sampleId == sisc.sampleId));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass()) + String.valueOf(this.sampleId)).hashCode();
    }
}
