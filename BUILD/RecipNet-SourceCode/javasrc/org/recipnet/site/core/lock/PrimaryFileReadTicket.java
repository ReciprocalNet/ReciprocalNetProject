/*
 * Reciprocal Net project
 * 
 * PrimaryFileReadTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 26-May-2006: jobollin reformatted the source and removed unused imports 
 */

package org.recipnet.site.core.lock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.ResourceNotFoundException;

/**
 * <p>
 * A subclass of {@code RepositoryTicket} that enables ticket users to read the
 * contents of a particular file in a particular sample's primary repository
 * directory. This kind of lock is disrupted by other {@code PrimaryFileLock}'s
 * for the same sample and file if the other lock would write to the file or
 * remove the file.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class PrimaryFileReadTicket extends RepositoryTicket implements
        PrimaryFileLock {
    
    /** Set at construction time. */
    private int sampleId;

    /** Set at construction time. */
    private File file;

    /** Set at construction time. */
    private String fileName;

    /**
     * An open file stream from which data is read. Set by {@code open()} and
     * accessed by {@code notifyLockRevoked()} and {@code read()}.
     */
    private InputStream fileInputStream;

    /**
     * Constructor.
     * 
     * @param userId the {@code userId} property of the {@code AbstractLock}
     *        superclass is set to this value for convenience.
     * @param timeUntilExpiration the approximate number of milliseconds after
     *        ticket granting that the ticket should expire. This value is used
     *        to invoke {@code AbstractLock.setExpiration()}.
     * @param sampleId identifies the sample in whose primary repository
     *        directory the file to be read resides.
     * @param file identifies the file on the filesystem (presumably within the
     *        specified sample's primary repository directory) that is to be
     *        read. The name of the file is implied by this reference.
     */
    public PrimaryFileReadTicket(int userId, long timeUntilExpiration,
            int sampleId, File file) {
        super(userId, timeUntilExpiration);
        this.sampleId = sampleId;
        this.file = file;
        this.fileName = file.getName();
        this.fileInputStream = null;
    }

    /** From interface {@code PrimaryFileLock}. */
    public int getSampleId() {
        return this.sampleId;
    }

    /** From interface {@code PriamryFileLock}. */
    public String getFileName() {
        return this.fileName;
    }

    /** From interface {@code PrimaryFileLock}. Always returns true. */
    public boolean isReadingFile() {
        return true;
    }

    /** From interface {@code PrimaryFileLock}. Always returns false. */
    public boolean isWritingFile() {
        return false;
    }

    /** From interface {@code PrimaryFileLock}. Always returns false. */
    public boolean isRemovingFile() {
        return false;
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * delegates to {@code RepositoryTicket} and then opens for reading the file
     * specified at construction time, and finally renews the ticket.
     * 
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the {@code file} specified at
     *         construction time was not accessible for reading.
     * @throws ResourceNotFoundException if the {@code file} specified at
     *         construction time could not be found.
     */
    @Override
    public synchronized void open() throws OperationFailedException {
        super.open();

        if (!this.file.isFile() || !this.file.exists()) {
            throw new ResourceNotFoundException(this.file);
        }
        if (!this.file.canRead()) {
            throw new ResourceNotAccessibleException(this.file);
        }
        try {
            this.fileInputStream = new FileInputStream(this.file);
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
        
        renew();
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * simply closes the open file if it hasn't been closed already.
     * 
     * @throws OperationFailedException on low-level error.
     */
    @Override
    protected synchronized void notifyLockRevoked()
            throws OperationFailedException {
        if (this.fileInputStream != null) {
            try {
                this.fileInputStream.close();
            } catch (IOException ex) {
                throw new OperationFailedException(ex);
            } finally {
                this.fileInputStream = null;
            }
        }
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * reads from the file and renews the ticket.
     * 
     * @throws IllegalStateException if there is no open file, possibly because
     *         an exception was thrown from {@code open()} previously.
     * @throws OperationFailedException on low-level error.
     */
    @Override
    public synchronized int read(byte[] buffer, int offset, int length)
            throws OperationFailedException {
        if (this.fileInputStream == null) {
            throw new IllegalStateException();
        }
        renew();
        try {
            return this.fileInputStream.read(buffer, offset, length);
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Overrides method on {@code AbstractLock}. The current implementation
     * returns true if {@code otherLock} is a {@code PrimaryFileLock} that
     * references the same sample id and file name as this lock, and the other
     * lock would write to or remove the file. Otherwise delegates to
     * {@code AbstractLock}.
     */
    @Override
    protected synchronized boolean wouldBeDisruptedBy(AbstractLock otherLock) {
        if (otherLock instanceof PrimaryFileLock) {
            PrimaryFileLock otherPrimaryFileLock = (PrimaryFileLock) otherLock;
            if ((this.sampleId == otherPrimaryFileLock.getSampleId())
                    && this.fileName.equals(otherPrimaryFileLock.getFileName())
                    && (otherPrimaryFileLock.isWritingFile() || otherPrimaryFileLock.isRemovingFile())) {
                return true;
            }
        }
        return super.wouldBeDisruptedBy(otherLock);
    }

    /**
     * Overrides function on {@code AbstractLock} -- for debugging use only.
     */
    @Override
    public synchronized String toString() {
        return super.toString() + " sampleId=" + this.sampleId + " file='"
                + this.fileName + "'";
    }
}
