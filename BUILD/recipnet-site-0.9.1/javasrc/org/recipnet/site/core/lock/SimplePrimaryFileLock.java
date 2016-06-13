/*
 * Reciprocal Net project
 * @(#)SimplePrimaryFileLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 */

package org.recipnet.site.core.lock;

/**
 * A subclass of <code>AbstractLock</code> that authorizes the lock user to
 * read/write/remove a particular file from a particular primary repository
 * directory.  This kind of lock is disrupted by other 
 * <code>PrimaryFileLock</code>'s for the same sample and file that would write
 * to the file or remove the file. <p>
 *
 * This class is nothing more than a straightforward implementation of an
 * <code>AbstractLock</code> with a <code>PrimaryFileLock</code> interface. <p>
 *
 * This class is thread-safe.
 */
public class SimplePrimaryFileLock extends AbstractLock 
        implements PrimaryFileLock {
    /** Set at construction time. */
    private int sampleId;

    /** Set at construction time. */
    private String fileName;

    /** Set at construction time. */
    private boolean isReading;

    /** Set at construction time. */
    private boolean isWriting;

    /** Set at construction time. */
    private boolean isRemoving;

    /**
     * Constructor.
     * @param sampleId identifies the primary repository directory in which a
     *     file will be read/written/removed.
     * @param fileName identifies the specified file within the primary 
     *     repository directory that will be read/written/removed.
     * @param isReading should be true if the lock user intends to read the
     *     file's contents, false otherwise.
     * @param isWriting should be true if the lock user intends to write (or
     *     append, or overwrite) the file's contents, false otherwise.
     * @param isRemoving should be true if the lock user intends to remove the
     *     file from the filesystem, false otherwise.
     * @param userId the <code>userId</code> property of the 
     *     <code>AbstractLock</code> superclass is set to this value for
     *     convenience.
     */
    public SimplePrimaryFileLock(int sampleId, String fileName, 
            boolean isReading, boolean isWriting, boolean isRemoving, 
            int userId) {
	super();
	this.sampleId = sampleId;
	this.fileName = fileName;
	this.isReading = isReading;
	this.isWriting = isWriting;
	this.isRemoving = isRemoving;
	super.setUserId(userId);
    }

    /**
     * Overrides function on <code>AbstractLock</code>.  The current 
     * implementation returns true if <code>otherLock</code> is a
     * <code>PrimaryFileLock</code>, which references the same sample id
     * and filename as this lock, and that would write or remove the file.
     * Otherwise delegates to <code>AbstractLock</code>.
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
	if (otherLock instanceof PrimaryFileLock) {
	    PrimaryFileLock otherPrimaryFileLock = (PrimaryFileLock) otherLock;
	    if ((this.sampleId == otherPrimaryFileLock.getSampleId())
		    && this.fileName.equals(otherPrimaryFileLock.getFileName())
                    && (otherPrimaryFileLock.isWritingFile()
                        || otherPrimaryFileLock.isRemovingFile())) {
		return true;
	    }
	}
	return super.wouldBeDisruptedBy(otherLock);
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public int getSampleId() {
	return sampleId;
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public String getFileName() {
	return fileName;
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public boolean isReadingFile() {
	return isReading;
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public boolean isWritingFile() {
	return isWriting;
    }

    /** From interface <code>PrimaryFileLock</code>. */
    public boolean isRemovingFile() {
	return isRemoving;
    }

    /**
     * Overrides function on <code>AbstractLock</code> -- for debugging use 
     * only.
     */
    @Override
    public String toString() {
	return super.toString()
                + " sampleId=" + this.sampleId
                + " file='" + this.fileName + "'"
                + " read=" + this.isReading
                + " write=" + this.isWriting
  	        + " remove=" + this.isRemoving;
    }
}
