/*
 * Reciprocal Net project
 * @(#)PrimaryFileReadLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 */
package org.recipnet.site.core.lock;

/**
 * A subclass of <code>AbstractLock</code> that authorizes the lock user to
 * read any file within a particular primary repository directory.  At the
 * lock user's option, this kind of lock may or may not be disrupted by writes
 * to and removes from files within the primary repository directory. <p>
 *
 * This class is thread-safe.
 */
public class PrimaryDirectoryReadLock extends AbstractLock 
        implements PrimaryFileLock {
    /** Set at construction time. */
    private int sampleId;

    /** Set at construction time. */
    private boolean coexistsWithWriting;

    /** Set at construction time. */
    private boolean coexistsWithRemoving;

    /**
     * Constructor.
     * @param sampleId identifies the primary repository directory from which
     *     files will be read.
     * @param coexistsWithWriting if true, this lock would not be disturbed by
     *     concurrent writes to files within the same primary repository
     *     directory; if false, this lock would be disturbed.
     * @param coexistsWithRemoving if true, this lock would not be disturbed by
     *     concurrent removals of files within the same primary repository
     *     directory; if false, this lock would be disturbed.
     */
    public PrimaryDirectoryReadLock(int sampleId, boolean coexistsWithWriting, 
            boolean coexistsWithRemoving) {
	super();
	this.sampleId = sampleId;
	this.coexistsWithWriting = coexistsWithWriting;
	this.coexistsWithRemoving = coexistsWithRemoving;
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public int getSampleId() {
	return sampleId;
    }

    /** From interface <code>PrimaryFileLock</code>.  Always returns null. */
    public String getFileName() {
	return null;
    }

    /** From interface <code>PrimaryFileLock</code>.  Always returns true. */
    public boolean isReadingFile() {
	return true;
    }

    /** From interface <code>PrimaryFileLock</code>.  Always returns false. */
    public boolean isWritingFile() {
	return false;
    }

    /** From interface <code>PrimaryFileLock</code>.  Always returns false. */
    public boolean isRemovingFile() {
	return false;
    }

    /**
     * Overrides function on <code>AbstractLock</code>.  Current implementation
     * returns true if a) <code>otherLock</code> is a 
     * <code>PrimaryFileLock</code>, b) <code>otherLock</code> references the
     * same sample id as this lock, and c) <code>otherLock</code> is performing
     * a write and this lock can't coexist with writes, or 
     * <code>otherLock</code> is performing a removal and this lock can't 
     * coexist with removals.  Otherwise delegates to 
     * <code>AbstractLock</code>.
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
	if (otherLock instanceof PrimaryFileLock) {
	    PrimaryFileLock otherPrimaryFileLock = (PrimaryFileLock) otherLock;
	    if ((this.sampleId == otherPrimaryFileLock.getSampleId())
		&& ((otherPrimaryFileLock.isWritingFile() 
		         && !coexistsWithWriting)
		    || (otherPrimaryFileLock.isRemovingFile() 
			 && !coexistsWithRemoving))) {
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
                + " coexistsWithWriting=" + this.coexistsWithWriting
                + " coexistsWithRemoving=" + this.coexistsWithRemoving;
    }
}
