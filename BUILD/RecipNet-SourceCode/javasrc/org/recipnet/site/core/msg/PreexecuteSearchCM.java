/*
 * Reciprocal Net project
 * @(#)PreexecuteSearchCM.java
 *
 * 17-Jan-2003: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;

/**
 * A core message that Sample Manager sends to itself to signal its worker 
 * thread that a search is pending and ought to be executed.  Normally this CM
 * would be generated as new searches were created (stored).  Because timely 
 * delivery of this CM cannot be guaranteed, any code that might act upon the
 * results of the search must be able to handle the case where the search has
 * not executed yet.
 */
public class PreexecuteSearchCM extends CoreMessage {
    /**
     * Id of the search that is to be pre-executed.  Search id's are assigned
     * by SampleManager.storeSearchParams() and are keys into two maps:
     * SampleManager.storedSearchParams and SampleManager.cachedSearchParams .
     */
    public int searchId;

    public PreexecuteSearchCM(int searchId) {
	this.searchId = searchId;
    }
}

