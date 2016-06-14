/*
 * Reciprocal Net project
 * @(#)PrimaryFileLock.java
 *
 * 27-May-2003: ekoperda wrote first draft
 */
package org.recipnet.site.core.lock;

/**
 * An interface implemented by subclasses of <code>AbstractLock</code> that 
 * access primary repository directories.  A lock that implements this 
 * interface authorizes access to a particular file in a primary repository 
 * directory, or possibly to the directory itself.  This interface simplifies
 * lock-conflict-checking for primary repository directories.
 */
public interface PrimaryFileLock {
    /**
     * @return the sample id that uniquely identifies the primary repository
     *     directory referenced by this lock.
     */
    public int getSampleId();

    /**
     * @return the name of the file within the primary repository directory
     *     referenced by this lock, or null if the directory itself (and every
     *     file within it) are referenced.
     */
    public String getFileName();

    /**
     * @return true if the file(s) referenced by this lock are being read,
     *     false otherwise.
     */
    public boolean isReadingFile();

    /**
     * @return true if the file(s) referenced by this lock are being written,
     *     false otherwise.
     */
    public boolean isWritingFile();

    /**
     * @return true if the file(s) referenced by this lock are being removed,
     *     false otherwise.
     */
    public boolean isRemovingFile();
}
