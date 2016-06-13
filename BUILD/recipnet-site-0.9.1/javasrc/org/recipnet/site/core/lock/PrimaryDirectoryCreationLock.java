/*
 * Reciprocal Net project
 * @(#)PrimaryDirectoryCreationLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 */

package org.recipnet.site.core.lock;

/**
 * A subclass of <code>AbstractLock</code> that authorizes the lock user to
 * create a primary repository directory (and presumably an associated holding 
 * record) for a particlar sample that doesn't already have one.  This kind of
 * lock is disrupted by other <code>PrimaryDirectoryCreationLock</code>'s with 
 * the same sample id, and also with any <code>PrimaryDirectoryLock</code> with
 * the same sample id. This lock may also be used to grant exclusive access to
 * a primary repository directory.<p>
 * 
 * This class is thread-safe.
 */
public class PrimaryDirectoryCreationLock extends AbstractLock {
    /** Set at construction time. */
    private int sampleId;

    /**
     * Constructor.
     * @param sampleId identifies the primary repository directory that will be
     *     created.
     * @param userId the <code>userId</code> property of the
     *     <code>AbstractLock</code> superclass is set to this value for
     *     convenience.
     */
    public PrimaryDirectoryCreationLock(int sampleId, int userId) {
	super();
	this.sampleId = sampleId;
	super.setUserId(userId);
    }

    /**
     * Overrides function on <code>AbstractLock</code>.  Current implementation
     * returns true if either a) <code>otherLock</code> is a 
     * <code>PrimaryDirectoryCreationLock</code> and references the same sample
     * id as this lock, or b) <code>otherLock</code> is a 
     * <code>PrimaryFileLock</code> and references the same sample id as this
     * lock.  Otherwise delegates to <code>AbstractLock</code>.
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
	if (otherLock instanceof PrimaryDirectoryCreationLock) {
	    PrimaryDirectoryCreationLock x 
                    = (PrimaryDirectoryCreationLock) otherLock;
	    if (this.sampleId == x.sampleId) {
		return true;
	    }
	} else if (otherLock instanceof PrimaryFileLock) {
	    PrimaryFileLock x = (PrimaryFileLock) otherLock;
	    if (this.sampleId == x.getSampleId()) {
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
	        + " sampleId=" + this.sampleId;
    }
}
