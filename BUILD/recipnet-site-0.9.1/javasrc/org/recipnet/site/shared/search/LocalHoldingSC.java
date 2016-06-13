/*
 * Reciprocal Net project
 * 
 * LocalHoldingSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

import org.recipnet.site.shared.db.RepositoryHoldingInfo;

/**
 * A {@code SearchConstraint} that limits the search results to samples
 * that are hosted at the local site.
 */
public class LocalHoldingSC extends SearchConstraint {

    /** A constructor that fully initializes a {@code LocalHoldingSC}. */
    public LocalHoldingSC() {
        // does nothing
    }

    /**
     * Generates a {@code String} that may be used as a portion of the SQL WHERE
     * clause to require that a sample be held at the local site.
     * 
     * @param tableTracker used to get the table alas
     * @param parameters the sequence of parameters that corresponds to the
     *        sequence of where clause fragments generated; one Integer
     *        parameter is added by this method.
     * @param scei not used by this implementation
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters,
            @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(RepositoryHoldingInfo.BASIC_DATA));
        
        return tableTracker.getTableAlias("searchLocalHoldings", this)
                + ".repositoryHoldings_replicaLevel >= ?";
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
