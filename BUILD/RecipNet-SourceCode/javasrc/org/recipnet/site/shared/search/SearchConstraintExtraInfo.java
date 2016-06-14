/*
 * Reciprocal Net project
 * 
 * SearchConstraintExtraInfo.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that contains any extra information needed by any
 * {@code SearchConstraint} objects that is not easily available at construction
 * time. A {@code SearchConstraintExtraInfo} object is made available to
 * {@code SearchConstraint} objects as they are translated to SQL.
 */
public class SearchConstraintExtraInfo {

    /**
     * A {@code Collection} of {@code Integer} representations of each local
     * lab's ID. This data is used by
     * {@code RequireAuthoritativeSearchConstraint}.
     */
    private final Set<Integer> localLabIds;

    /**
     * Initializes a new {@code SearchConstraintExtraInfo} for use as a
     * parameter to {@code SearchConstraint.getWhereClauseFragment()}.
     * 
     * @param localLabIds a {@code Collection} containing the ID
     *        ({@code Integer}) for each lab hosted at the local site; no
     *        reference to this collection is retained beyond the completion of
     *        this constructor
     */
    public SearchConstraintExtraInfo(Collection<Integer> localLabIds) {
        this.localLabIds = Collections.unmodifiableSet(
                new HashSet<Integer>(localLabIds));
    }

    /**
     * Exposes an unmodifiable {@code Collection} of the local labs IDs provided
     * to this {@code SearchConstraintExtraInfo} at construction time.
     */
    public Collection<Integer> getLocalLabIds() {
        return this.localLabIds;
    }
}
