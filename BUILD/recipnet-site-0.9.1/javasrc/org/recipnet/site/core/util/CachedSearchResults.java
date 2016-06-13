/*
 * Reciprocal Net project
 * 
 * CachedSearchResults.java
 *
 * 17-Jun-2002: ekoperda wrote first draft
 * 28-Jun-2002: ekoperda added sampleHistoryList variable
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 18-Oct-2002: ekoperda removed the getSearchItems() function
 * 11-May-2006: jobollin reformatted the source and converted to generics
 */

package org.recipnet.site.core.util;

import java.lang.System;
import java.util.List;
import java.util.ArrayList;

/**
 * CachedSearchResults is an internal container class used by Sample Manager. It
 * is used in that module's "second map" to associate a search number (obtained
 * when the caller "stores" a SearchParams object) with a list of all the sample
 * numbers that match the specified search criteria. These CachedSearchResults
 * objects held in a cache that is maintained by Sample Manager in order to
 * prevent the same search from having to be executed repeatedly.
 */
public class CachedSearchResults {

    /**
     * the id number for this search, as returned by storeSearchParams() when
     * the search was first submitted.
     */
    public int searchId;

    /**
     * A {@code List} of {@code Integer}s that specifies which Reciprocal Net
     * sample numbers match the specified search criteria.
     */
    public List<Integer> sampleIdList;

    /**
     * A {@code List} of {@code Integer}s that corresponds directly with the
     * {@code sampleIdList}. This list contains the sampleHistory numbers for
     * the matching samples, current as of the time the search was run. This is
     * used by Sample Manager to detect when a sample returned by an earlier
     * search has since been updated and might no longer match the user's search
     * criteria.
     */
    public List<Integer> sampleHistoryList;

    /**
     * the time at which the db query was executed and this class was populated,
     * obtained from System.currentTimeMillis(). Sample Manager uses this value
     * for cache maintenance.
     */
    public long execution_timestamp;

    /**
     * Initializes a new {@code CachedSearchResults}, setting the execution
     * timestamp to the current time
     */
    public CachedSearchResults() {
        sampleIdList = new ArrayList<Integer>();
        sampleHistoryList = new ArrayList<Integer>();
        execution_timestamp = System.currentTimeMillis();
    }
}
