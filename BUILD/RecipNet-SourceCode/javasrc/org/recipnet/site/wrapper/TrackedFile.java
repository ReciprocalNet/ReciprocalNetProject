/*
 * Reciprocal Net Project
 *
 * TrackedFile.java
 *
 * 21-Oct-2002: jobollin wrote first draft
 * 09-Jan-2003: jobollin removed unused imports
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper
 * 23-May-2006: jobollin made some member variables private and removed the
 *              nullary constructor 
 */

package org.recipnet.site.wrapper;

import java.io.File;
import java.io.Serializable;

/**
 * a class encapsulating a File object with its MIME type; used by the
 * FileTracker class
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 * @since 0.5.2
 */
public class TrackedFile implements Serializable {

    /**
     * a {@code File} representing the file underlying this {@code TrackedFile}
     */
    private final File file;

    /**
     * The type information associated with this {@code TrackedFile}; opaque to
     * this {@code TrackedFile} itself
     */
    private final String type;

    /**
     * A flag indicating whether this file underlying this {@code TrackedFile}
     * should be deleted when this {@code TrackedFile} is invalidated
     */
    private final boolean deleteOnInvalidation;

    /**
     * The length, in bytes, most recently determined for the file underlying
     * this {@code TrackedFile}; updated by {@link #updateFileLength()}
     */
    private long length;

    /**
     * The validity flag for this {@code TrackedFile}; intialized to
     * {@code true}, and set to {@code false} by {@link #invalidate()}
     */
    private boolean valid;

    /**
     * The last access timestamp for this {@code TrackedFile}; set to the
     * current system time when an instance is initialized, and subsequently
     * updated via {@link #setTimestamp(long)}
     */
    private long accessTimestamp;

    /**
     * creates a TrackedFile wrapping the specified File, associated with the
     * specified type, and having the specified delete-on-invalidation flag
     * 
     * @param f the File to be wrapped by this TrackedFile
     * @param t a String designating the type of this TrackedFile
     * @param delete a flag indicating whether the underlying File should be
     *        deleted when this TrackedFile's invalidate method is invoked
     */
    TrackedFile(File f, String t, boolean delete) {
        file = f;
        type = t;
        deleteOnInvalidation = delete;
        valid = true;
        updateFileLength();
        setTimestamp(System.currentTimeMillis());
    }

    /**
     * Returns the {@code File} object associated with this {@code TrackedFile}
     * 
     * @return the {@code File} object
     */
    public File getFile() {
        return file;
    }

    /**
     * Computes the difference between the specified time and this
     * {@code TrackedFile}'s last access timestamp, without updating it
     * 
     * @param base the timestamp to compare with this {@code TrackedFile}'s
     *        last access timestamp, as provided (for instance) by
     *        {@code System.currentTimeMillis()}
     * @return the number of milliseconds between the specified comparison
     *         timestamp and this {@code TrackedFile}'s last access timestamp,
     *         positive if the comparison timestamp is more recent than the
     *         access timestamp, negative if the reverse
     */
    synchronized public long getRelativeAge(long base) {
        return (base - accessTimestamp);
    }

    /**
     * returns the type information associated with which this
     * {@code TrackedFile}
     * 
     * @return the content type information as a {@code String}
     */
    public String getType() {
        return type;
    }

    /**
     * returns the length of the file, as most recently determined. (The
     * filesystem is not consulted.)
     * 
     * @return the (cached) length in bytes of the file
     */
    synchronized public long getLength() {
        return length;
    }

    /**
     * returns the space that will be freed if this TrackedFile is invalidated;
     * will be 0L if this TrackedFile is not set to delete on invalidation
     * 
     * @return the number of bytes that would be freed on the filesystem by
     *         invalidating this {@code TrackedFile}
     */
    synchronized public long getRecoverableLength() {
        return (deleteOnInvalidation ? getLength() : 0L);
    }

    /**
     * Determines whether this {@code TrackedFile} is valid, which is the case
     * from the time it is initialized until it is explicitly invalidated
     * 
     * @return {@code true} if this {@code TrackedFile} is valid, {@code false}
     *         if not
     */
    synchronized public boolean isValid() {
        return valid;
    }

    /**
     * Invalidates this {@code TrackedFile}, causing it to be flagged invalid
     * and, if so configured, deleting the underlying file. Does nothing if this
     * {@code TrackedFile} is already invalid
     */
    synchronized void invalidate() {
        if (valid && deleteOnInvalidation) {
            file.delete();
        }
        valid = false;
    }

    /**
     * Updates this {@code TrackedFile}'s last access timestamp
     * 
     * @param ts the timestamp to set on this {@code TrackedFile}, as provided
     *        (for instance) by {@code System.currentTimeMillis()}
     */
    synchronized public void setTimestamp(long ts) {
        accessTimestamp = ts;
    }

    /**
     * Causes this {@code TrackedFile} to (re)determine the length of its
     * underlying file; it remembers this value until the next time this method
     * is invoked
     */
    synchronized void updateFileLength() {
        length = file.length();
    }
}
