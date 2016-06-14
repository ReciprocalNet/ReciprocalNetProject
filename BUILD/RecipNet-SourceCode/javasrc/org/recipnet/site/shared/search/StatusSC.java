/*
 * Reciprocal Net project
 * 
 * StatusSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code SearchConstraint} that limits the search to samples that have a
 * particular status.
 */
public class StatusSC extends SearchConstraint {

    /** A status code from {@code SampleWorkflowBL}. */
    private int status;

    /**
     * A constructor that fully initializes this {@code StatusSC}.
     */
    public StatusSC(int status) {
        this.status = status;
    }

    /**
     * Generates a {@code String} that may be used as a portion of the SQL WHERE
     * clause to require that a sample have a particular status.
     * 
     * @param tableTracker used to get the table alas
     * @param parameters the sequence of parameters that corresponds to the
     *        sequence of where clause fragments generated; one Integer
     *        parameter is added by this method.
     * @param scei not used in this implementation
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(this.status));
        return tableTracker.getTableAlias("samples", this) + ".status = ?";
    }

    /** Equality is based on class and status. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof StatusSC) {
            return ((this.getClass() == obj.getClass())
                    && (this.status == ((StatusSC) obj).status));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + String.valueOf(this.status)).hashCode();
    }
}
