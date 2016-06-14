/*
 * Reciprocal Net project
 * 
 * RequireAuthoritativeSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 16-May-2005: midurbin fixed bug #1527 in getWhereClauseFragment()
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code SearchConstraint} to limit search results to those samples which
 * belong to labs hosted at the local site. For the sake of efficiency, this
 * {@code SearchConstraint} does not need to be provided with the local labs at
 * construction time, but instead gets them from the
 * {@code SearchConstraintExtraInfo} when {@code getWhereClauseFragment()} is
 * invoked.
 */
public class RequireAuthoritativeSC extends SearchConstraint {

    /**
     * A constructor that fully initializes a {@code RequireAuthoritativeSC}.
     */
    public RequireAuthoritativeSC() {
        // nothing to set
    }

    /**
     * {@inheritDoc}; this version returns an SQL where clause frament
     * requiring that a sample belong to a local lab to be included. This method
     * gets a collection of the local labs for a site from the
     * {@code SearchConstraintExtraInfo} passed to this method.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, SearchConstraintExtraInfo scei) {
        String sampleTable = tableTracker.getTableAlias("samples", this);
        StringBuilder sb = new StringBuilder();

        for (Object id : scei.getLocalLabIds()) {
            /*
             * add each local lab's ID to the parameters collection and append
             * some SQL to ensure that compares the lab with the "lab_id" column
             * on the "samples" table
             */
            parameters.add(id);
            if (sb.length() != 0) {
                sb.append(" OR ");
            }
            sb.append(sampleTable + ".lab_id = ?");
        }

        if (scei.getLocalLabIds().size() > 1) {
            return "(" + sb + ")";
        } else {
            return sb.toString();
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
