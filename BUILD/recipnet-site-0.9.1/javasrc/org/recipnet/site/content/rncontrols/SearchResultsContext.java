/*
 * Reciprocal Net project
 * 
 * SearchResultsContext.java
 * 
 * 02-Nov-2004: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;

import org.recipnet.site.shared.db.SampleInfo;

/**
 * An interface defining a contract by which objects may provide a collection of
 * {@code SampleInfo} representing, in principle, some portion of the results of
 * a search
 */
public interface SearchResultsContext {

    /**
     * Provides the collection of sample information appropriate for this
     * context. and the specified starting index and count. When the
     * {@code SearchResultsContext} implementation is a phase-recognizing custom
     * tag, its results information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @param startingIndex the one-based index of the first result to include
     * @param maxResults the maximum number of results to return
     * @return a {@code Collection} of {@code SampleInfo} objects representing
     *         the search results; will have exactly {@code maxResults} elements
     *         unless there are fewer overall hits starting at the specified
     *         index, in which case all the remaining hits are returned
     */
    public Collection<SampleInfo> getSearchResults(int startingIndex,
            int maxResults);
}
