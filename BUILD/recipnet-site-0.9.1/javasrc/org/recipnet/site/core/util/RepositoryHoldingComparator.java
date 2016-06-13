/*
 * Reciprocal Net project
 * 
 * RepositoryHoldingComparator.java
 *
 * 02-Jul-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 11-Apr-2006: jobollin added appropriate type parameters and reformatted
 */

package org.recipnet.site.core.util;

import java.util.Comparator;

import org.recipnet.site.shared.db.RepositoryHoldingInfo;

/**
 * This is a comparator that is able to determine whether one
 * RepositoryHoldingInfo is "more desirable" than another. Such a decision needs
 * to be made, for instance, when a user has requested data for a particular
 * sample number and that data is stored at multiple sites -- RepositoryManager
 * needs to return an ordered list of sites where that data can be found. The
 * criteria for choosing the best RepositoryHoldingInfo are as follows:
 * <ol>
 * <li>the local site is chosen first (if data are available here)</li>
 * <li>sites with FULL_DATA are chosen next, in arbitrary order</li>
 * <li>sites with BASIC_DATA are chosen last, in arbitrary order</li>
 * </ol>
 */
public class RepositoryHoldingComparator implements
        Comparator<RepositoryHoldingInfo> {
    /*
     * TODO: This comparator's decisions could be made more intelligent if it
     * had knowledge of loading factors and connectivity to other sites.
     */
    
    private final int localSiteId;

    /**
     * Create a new comparator. localSiteId should be the id number of the local
     * site, as obtained from Site Manager. This information is required so that
     * this class can give appropriate preference to sample data that's hosted
     * at the local site.
     */
    public RepositoryHoldingComparator(int localSiteId) {
        this.localSiteId = localSiteId;
    }

    /**
     * The real meat
     */
    public int compare(RepositoryHoldingInfo h1, RepositoryHoldingInfo h2) {

        if (h1.siteId == h2.siteId) {
            return 0;
        } else if (h1.siteId == localSiteId) {
            return -1;
        } else if (h2.siteId == localSiteId) {
            return 1;
        } else if (h1.replicaLevel == h2.replicaLevel) {
            return 0;
        } else if (h1.replicaLevel == RepositoryHoldingInfo.FULL_DATA) {
            return -1;
        } else {
            return 1;
        }
    }
}
