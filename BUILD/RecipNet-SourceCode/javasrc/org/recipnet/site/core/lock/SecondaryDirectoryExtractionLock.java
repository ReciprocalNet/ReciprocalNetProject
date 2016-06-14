/*
 * Reciprocal Net project
 * @(#)SecondaryDirectoryExtractionLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.lock;
import org.recipnet.site.shared.db.SampleHistoryInfo;

/**
 * A subclass of <code>AbstractLock</code> that authorizes the lock user to
 * (possibly) create a new secondary repository directory and extract (from the
 * CVS repository) one or more files into it.  This kind of lock is disrupted
 * by other <code>SecondaryDirectoryExtractionLock</code>'s for the same 
 * sample/version, or in some cases by other 
 * <code>SecondaryDirectoryExtractionLock</code>'s for any version of the same
 * sample. <p>
 *
 * This class is thread-safe.
 */
public class SecondaryDirectoryExtractionLock extends AbstractLock {
    /** Set at construction time. */
    private int sampleId;

    /** Set at construction time. */
    private int sampleHistoryId;

    /**
     * Constructor.
     * @param sampleId identifies the particular sample for which a secondary
     *     repository directory will be created and populated.
     * @param sampleHistoryId identifies the particular version of the sample
     *     for which a secondary repository directory will be created and
     *     populated.  If this is not known, this value may be 
     *     <code>INVALID_SAMPLE_HISTORY_ID</code>.  Callers are cautioned that
     *     specifying <code>INVALID_SAMPLE_HISTORY_ID</code> negatively affects
     *     potential concurrency of locks and may decrease performance.
     * @param userId the <code>userId</code> property of the
     *     <code>AbstractLock</code> superclass is set to this value for 
     *     convenience.
     */
    public SecondaryDirectoryExtractionLock(int sampleId, int sampleHistoryId, 
            int userId) {
	super();
	this.sampleId = sampleId;
	this.sampleHistoryId = sampleHistoryId;
	super.setUserId(userId);
    }

    /**
     * Overrides function on <code>AbstractLock</code>.  The current
     * implementation returns true if <code>otherLock</code> is a
     * <code>SecondaryDirectoryExtractionLock</code> which references the same
     * sample id and sample history id as this lock, or merely the same sample
     * id if this lock's <code>sampleHistoryId</code> value is 
     * <code>INVALID_SAMPLE_HISTORY_ID</code>.  Otherwise delegates to
     * <code>AbstractLock</code>.
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
	if (otherLock instanceof SecondaryDirectoryExtractionLock) {
	    SecondaryDirectoryExtractionLock x 
                    = (SecondaryDirectoryExtractionLock) otherLock;
	    if ((this.sampleId == x.sampleId)
		    && ((this.sampleHistoryId == x.sampleHistoryId)
                        || (this.sampleHistoryId 
                             == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID))) {
		return true;
	    }
	}
	return super.wouldBeDisruptedBy(otherLock);
    }

    /**
     * Overrides function on <code>AbstractLock</code> -- for debugging use 
     * only.
     */
    @Override
    public String toString() {
	return super.toString()
                + " sampleId=" + this.sampleId
                + " sampleHistoryId=" + this.sampleHistoryId;
    }
}
