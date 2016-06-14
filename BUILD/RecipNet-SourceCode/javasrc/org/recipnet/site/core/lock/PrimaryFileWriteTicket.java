/*
 * Reciprocal Net project
 * 
 * PrimaryFileWriteTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 29-Mar-2004: midurbin fixed bug #1041 in wouldBeDisruptedBy()
 * 12-Jul-2004: ekoperda implemented UnisonClosureTicket by adding 
 *              supportsClosureInUnisonWith(), beforeUnisonClosure(), 
 *              closeInUnisonWith(), and afterUnisonClosure(), and modifying
 *              close()
 * 11-Feb-2005: ekoperda modified supportsClosureInUnisonWith() to make its
 *              check a little more thorough
 * 21-Oct-2005: midurbin added support for file descriptions
 */

package org.recipnet.site.core.lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.util.PrimaryDirectory;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * <p>
 * A subclass of {@code RepositoryTicket} that enables ticket users to write to
 * create the contents of a particular file in a particular sample's primary
 * repository directory. This kind of lock is disrupted by other
 * {@code PrimaryFileLock}'s for the same sample and file if the other lock
 * would write to the file or remove the file.
 * </p><p>
 * Because this kind of ticket actually alters the data files associated with a
 * sample, upon normal closure of the ticket a sample workflow action is
 * performed. (Every change to repository files must be accompanied by a
 * workflow action.) This is the reason this class's constructor takes so many
 * arguments. As bytes are written to the ticket, they are forwarded to the
 * filesystem and stored in a temporary file within the appropriate primary
 * repository directory. At ticket close time, assuming the sample workflow
 * action finished, the temporary file is "promoted" to the real file with
 * expected filename. If the ticket is revoked without being closed gracefully,
 * the temporary file is deleted.
 * </p><p>
 * This kind of ticket supports "unison closures" as described by the
 * {@code UnisonClosureTicket} interface. The batch of tickets to be closed in
 * unison must contain only {@code PrimaryFileWriteTicket}'s or its subclasses,
 * and each ticket must be associated with the same sample id as the others. Any
 * {@code PrimaryFileWriteTicket} within the batch may be selected as the master
 * ticket during the unison closure.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class PrimaryFileWriteTicket extends RepositoryTicket implements
        PrimaryFileLock, UnisonClosureTicket {
    
    /** Set at construction time. */
    private PrimaryDirectory primaryDirectoryInfo;

    /** Set at construction time. */
    private String fileName;

    /** Set at construction time. */
    private File tempFile;

    /** Set at construction time. */
    private SampleInfo sample;

    /** Set at construction time. */
    private String comments;

    /** Set at construction time. */
    private String description;

    /** Set at construction time. */
    private int actionCode;

    /** Set at construction time. */
    private RepositoryManager repositoryManager;

    /**
     * An open file stream to which data is written. Set by {@code open()} and
     * accessed by {@code notifyLockRevoked()}, {@code close()}, and
     * {@code write()}.
     */
    private OutputStream tempFileOutputStream;

    /**
     * Constructor.
     * 
     * @param userId the {@code userId} property of the {@code AbstractLock}
     *        superclass is set to this value for convenience.
     * @param timeUntilExpiration the approximate number of milliseconds after
     *        ticket granting that the ticket should expire. This value is used
     *        to invoke {@code AbstractLock.setExpiration()}.
     * @param primaryDirectoryInfo identifies the primary repository directory
     *        in which the file to be created/overwritten resides.
     * @param fileName identifies the file within the primary repository
     *        directory that is to be created/overwritten.
     * @param tempFile identifies the file on the filesystem within the primary
     *        repository directory that should be used as a temporary file for
     *        the duration of the upload.
     * @param sample a possibly updated {@code SampleInfo}, as would be passed
     *        to {@code SampleManager.putSampleInfo()}.
     * @param comments workflow comments to be associated with this file write,
     *        or null, as would be passed to
     *        {@code SampleManager.putSampleInfo()}.
     * @param description description to be associated with the file or null
     * @param actionCode workflow action code to be associated with this file
     *        write, as would be passed to {@code SampleManager.putSampleInfo()}.
     * @param repositoryManager a reference to {@code RepositoryManager}. At
     *        close time, {@code notifyFileUploadFinished()} on this object is
     *        invoked.
     */
    public PrimaryFileWriteTicket(int userId, long timeUntilExpiration,
            PrimaryDirectory primaryDirectoryInfo, String fileName,
            File tempFile, SampleInfo sample, String comments,
            String description, int actionCode,
            RepositoryManager repositoryManager) {
        super(userId, timeUntilExpiration);
        this.primaryDirectoryInfo = primaryDirectoryInfo;
        this.fileName = fileName;
        this.tempFile = tempFile;
        this.sample = sample;
        this.comments = comments;
        this.description = description;
        this.actionCode = actionCode;
        this.repositoryManager = repositoryManager;
        this.tempFileOutputStream = null;
    }

    /** {@inheritDoc} */
    public int getSampleId() {
        return this.sample.id;
    }

    /** {@inheritDoc} */
    public String getFileName() {
        return this.fileName;
    }

    /** {@inheritDoc}.  This version always returns {@code false}.  */
    public boolean isReadingFile() {
        return false;
    }

    /** {@inheritDoc}.  This version always returns {@code true}.  */
    public boolean isWritingFile() {
        return true;
    }

    /** {@inheritDoc}.  This version always returns {@code false}.  */
    public boolean isRemovingFile() {
        return false;
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * closes the output file stream (if it hasn't been closed already) and
     * deletes the temporary file (if it hasn't been promoted already).
     * 
     * @throws OperationFailedException on low-level error.
     */
    @Override
    public synchronized void notifyLockRevoked()
            throws OperationFailedException {
        if (this.tempFileOutputStream != null) {
            try {
                this.tempFileOutputStream.close();
            } catch (IOException ex) {
                throw new OperationFailedException(ex);
            } finally {
                this.tempFileOutputStream = null;
            }
        }
        if ((this.tempFile != null) && this.tempFile.exists()) {
            this.tempFile.delete();
        }
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * delegates to {@code RepositoryTicket} and then opens for writing the
     * temporary file specified at construction time, and finally renews the
     * ticket.
     * 
     * @throws OperationFailedException on low-level error.
     */
    @Override
    public synchronized void open() throws OperationFailedException {
        super.open();
        try {
            this.tempFileOutputStream = new FileOutputStream(this.tempFile);
        } catch (FileNotFoundException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Overrides method on {@code RepositoryTicket}. The current implementation
     * closes the temporary file, invokes
     * {@code RepositoryManager.notifyFileUploadFinished()} (which presumably
     * performs the sample workflow action and promotes the temporary file), and
     * finally delegates to {@code RepositoryTicket}. This method never throws
     * DeadlockDetectedException.
     * 
     * @throws IllegalStateException if the temporary file was not open for
     *         writing, perhaps because an exception was thrown earlier.
     * @throws OperationFailedException on low-level error.
     */
    @Override
    public synchronized void close() throws OperationFailedException {
        // This function we're calling is intended for use in a unison closure,
        // but it happens to do exactly what we need.
        beforeUnisonClosure();

        // Report back to RepositoryManager that we're done; let it handle all
        // the filesystem and database updates that need to happen.
        this.repositoryManager.notifyFileUploadsFinished(
                this.primaryDirectoryInfo, this.sample, super.getUserId(),
                comments, this.actionCode, new String[] { this.fileName },
                new File[] { this.tempFile },
                new String[] { this.description });

        // This function we're calling is intended for use during a unison
        // closure, but it happens to do exactly what we need.
        afterUnisonClosure();
    }

    /**
     * Implements {@code UnisonClosureTicket}; the current implementation
     * returns true exactly when a) every {@code otherTicket} is an instance of
     * this class or one of its subclasses, and b) every {@code otherTicket} is
     * associated with the same sample id as this one.
     */
    public boolean supportsClosureInUnisonWith(
            UnisonClosureTicket otherTickets[]) {
        for (int i = 0; i < otherTickets.length; i++) {
            if (!(otherTickets[i] instanceof PrimaryFileWriteTicket)) {
                return false;
            }
            if (getSampleId()
                    != ((PrimaryFileWriteTicket) otherTickets[i]).getSampleId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implements {@code UnisonClosureTicket}; the current implementation
     * simply closes the temporary file opened in a previous call to
     * {@code open()}.
     * 
     * @throws IllegalStateException if no temporary file has been opened
     *         previously.
     * @throws OperationFailedException with a nested {@code IOException} on a
     *         failure within Java's I/O subsystem.
     */
    public void beforeUnisonClosure() throws OperationFailedException {
        if (this.tempFileOutputStream != null) {
            try {
                renew();
                this.tempFileOutputStream.close();
            } catch (IOException ex) {
                throw new OperationFailedException(ex);
            } finally {
                this.tempFileOutputStream = null;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Implements {@code UnisonClosureTicket}; acts as a master ticket to close
     * an entire batch of tickets in unison. The current implementation
     * aggregates information from the ticket batch and then makes a single call
     * to {@code RepositoryManager.notifyFileUploadsFinished()}, where most of
     * the post-upload processing takes place.
     * 
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotFoundException if there was a permissions problem
     *         while attempting to manipulate one of the temporary files
     *         associated with any of the tickets in the batch.
     */
    public void closeInUnisonWith(UnisonClosureTicket otherTickets[])
            throws OperationFailedException {
        String fileNames[] = new String[otherTickets.length + 1];
        File tempFiles[] = new File[otherTickets.length + 1];
        String descriptions[] = new String[otherTickets.length + 1];

        fileNames[0] = this.fileName;
        tempFiles[0] = this.tempFile;
        descriptions[0] = this.description;
        for (int i = 0; i < otherTickets.length; i++) {
            PrimaryFileWriteTicket otherTicket
                    = (PrimaryFileWriteTicket) otherTickets[i];
            
            fileNames[i + 1] = otherTicket.fileName;
            tempFiles[i + 1] = otherTicket.tempFile;
            descriptions[i + 1] = otherTicket.description;
        }

        // Report back to RepositoryManager that we're done; let it handle all
        // the filesystem and database updates that need to happen.
        this.repositoryManager.notifyFileUploadsFinished(
                this.primaryDirectoryInfo, this.sample, getUserId(),
                this.comments, this.actionCode, fileNames, tempFiles,
                descriptions);
    }

    /**
     * Implements {@code UnisonClosureTicket}; the current implementation
     * simply invokes {@code close()} on the superclass for completeness.
     * 
     * @throws OperationFailedException with a nested
     *         {@code DeadlockDetectedException} on core deadlock.
     */
    public void afterUnisonClosure() throws OperationFailedException {
        try {
            // This next call to the superclass results in this lock being
            // released. We postpone releasing the lock until here, after
            // the call to RepositoryManager.notifyFileUploadsFinished(), in
            // order to maintain thread-safety.
            super.close();
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * {@inheritDoc}.  This version writes the specified bytes to the temporary
     * file and renews the ticket.
     * 
     * @throws IllegalStateException if there is no temporary file open for
     *         writing, possibly because an exception was thrown earlier.
     * @throws OperationFailedException on low-level error.
     */
    @Override
    public synchronized void write(byte[] buffer)
            throws OperationFailedException {
        if (this.tempFileOutputStream == null) {
            throw new IllegalStateException();
        }
        try {
            this.tempFileOutputStream.write(buffer);
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
        renew();
    }

    /**
     * Overrides method on {@code AbstractLock}. The current implementation
     * returns true if {@code other} is a {@code PreliminaryFileLock} that
     * references the same sample id and file name as this lock, and the other
     * lock would write to or remove the file. Otherwise delegates to
     * {@code AbstractLock}. This method is NOT synchronized as it may be
     * invoked as a condition for releasing another lock (LockAgent's
     * schedulerLock) that is needed by {@code PrimaryFileWriteTicket.close()}.
     * Were this method synchronized with {@code close()} deadlock may occur.
     */
    @Override
    protected boolean wouldBeDisruptedBy(AbstractLock otherLock) {
        if (otherLock instanceof PrimaryFileLock) {
            PrimaryFileLock otherPrimaryFileLock = (PrimaryFileLock) otherLock;
            if ((this.sample.id == otherPrimaryFileLock.getSampleId())
                    && this.fileName.equals(otherPrimaryFileLock.getFileName())
                    && (otherPrimaryFileLock.isWritingFile()
                            || otherPrimaryFileLock.isRemovingFile())) {
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
        return super.toString() + " sampleId=" + this.sample.id + " file='"
                + this.fileName + "'" + " tempfile='" + this.tempFile.getName()
                + "'" + " action=" + this.actionCode;
    }
}
