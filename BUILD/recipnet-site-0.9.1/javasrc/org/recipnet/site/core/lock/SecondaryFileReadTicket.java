/*
 * Reciprocal Net project
 * 
 * SecondaryFileReadTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 10-Jun-2003: ekoperda fixed bug #932 in read()
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 30-May-2006: jobollin removed unused imports and reformatted the source
 */

package org.recipnet.site.core.lock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.util.SecondaryDirectory;

/**
 * <p>
 * A subclass of {@code RepositoryTicket} that enables ticket users to read the
 * contents of a particular file in a particular sample/version's secondary
 * repository directory. This kind of lock is not disrupted by any other locks
 * beyond those detected by {@code AbstractLock}.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class SecondaryFileReadTicket extends RepositoryTicket {
    
    /** Set at construction time. */
    private SecondaryDirectory secondaryDirectoryInfo;

    /** Set at construction time. */
    private File file;

    /**
     * An open file stream from which data is read. Set by {@code open()} and
     * accessed by {@code notifyLockRevoked()} and {@code read()}.
     */
    private InputStream fileInputStream;

    /**
     * Initializes a new {@code SecondaryFileReadTicket} with the specified
     * parameters
     * 
     * @param userId the user ID granted read access via this ticket
     * @param timeUntilExpiration the approximate number of milliseconds after
     *        ticket granting that the ticket should expire. This value is used
     *        to invoke {@code AbstractLock.setExpiration()}.
     * @param secondaryDirectoryInfo identifies the secondary directory for a
     *        particular sample/version in which the file to be read resides.
     * @param file identifies the existing file, presumably within a a
     *        sample/version's secondary repository directory, that is to be
     *        read.
     */
    public SecondaryFileReadTicket(int userId, long timeUntilExpiration,
            SecondaryDirectory secondaryDirectoryInfo, File file) {
        super(userId, timeUntilExpiration);
        this.secondaryDirectoryInfo = secondaryDirectoryInfo;
        this.file = file;
        this.fileInputStream = null;
    }

    /**
     * @return the {@code SecondaryDirectoryInfo} object that was supplied to
     *         this ticket at construction time.
     */
    public SecondaryDirectory getSecondaryDirectoryInfo() {
        return this.secondaryDirectoryInfo;
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
     * Overrides function on {@code AbstractLock} -- for debugging use only.
     */
    @Override
    public synchronized String toString() {
        return super.toString() + " sampleId="
                + this.secondaryDirectoryInfo.sampleId + " sampleHistoryId="
                + this.secondaryDirectoryInfo.sampleHistoryId + " file='"
                + this.file.getName() + "'";
    }
}
