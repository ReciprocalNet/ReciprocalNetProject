/*
 * Reciprocal Net project
 * @(#)SecondaryDirectoryDummyTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.lock;
import org.recipnet.site.core.util.SecondaryDirectory;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A special-purpose subclass of <code>RepositoryTicket</code> that doesn't
 * enable ticket users to do anything.  Its sole purpose is to be associated
 * with a newly-created secondary directory to temporarily prevent that
 * directory from being purged because no tickets were associated with it.  
 * This kind of ticket normally is assigned a relatively short expiration 
 * interval, just enough to serve as a "grace period" until more substantive
 * tickets (like <code>SecondaryFileReadTicket</code>'s) can be associated with
 * the secondary directory.
 *
 * This class is thread-safe.
 */
public class SecondaryDirectoryDummyTicket extends RepositoryTicket {
    /** Set at construction time. */
    private SecondaryDirectory secondaryDirectoryInfo;

    /**
     * Constructor.
     * @param timeUntilExpiration the approximate number of milliseconds after
     *     ticket granting that the ticket should expire.  This value is used
     *     to invoke <code>AbstractLock.setExpiration()</code>.
     * @param secondaryDirectoryInfo identifies the newly-created secondary
     *     repository directory.
     */
    public SecondaryDirectoryDummyTicket(long timeUntilExpiration, 
            SecondaryDirectory secondaryDirectoryInfo) {
	super(UserInfo.INVALID_USER_ID, timeUntilExpiration);
	this.secondaryDirectoryInfo = secondaryDirectoryInfo;
    }

    /**
     * @return the <code>SecondaryDirectoryInfo</code> object that was supplied
     *     to this ticket at construction time.
     */
    public SecondaryDirectory getSecondaryDirectoryInfo() {
	return this.secondaryDirectoryInfo;
    }

    /**
     * Overrides function on <code>AbstractLock</code> -- for debugging use 
     * only.
     */
    @Override
    public String toString() {
	return super.toString()
                + " sampleId=" + this.secondaryDirectoryInfo.sampleId
                + " sampleHistoryId=" 
                + this.secondaryDirectoryInfo.sampleHistoryId;
    }
}
