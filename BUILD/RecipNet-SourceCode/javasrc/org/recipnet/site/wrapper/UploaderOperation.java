/*
 * Reciprocal Net Project
 * 
 * UploaderOperation.java
 *
 * 08-Jul-2005: ekoperda wrote first draft
 * 04-Aug-2005: midurbin added new version of acceptUpload() to work with
 *              MultipartMimeFormParser; added getCompleteFilenames(),
 *              updateFileForTerminatedTransfer()
 * 21-Oct-2005: midurbin added support for file descriptions
 * 16-May-2006: jobollin reformatted the source; updated for consistency with
 *              the revised RepositoryFileTransfer API
 */

package org.recipnet.site.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.recipnet.common.MultipartMimeFormParser;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.RepositoryFileTransfer;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * A subclass of {@code PersistedOperation} that represents a bulk upload of
 * repository data files for one particular sample from a client of the web
 * application. The uploaded files are saved as sample data files by performing
 * a caller-specified workflow action. Support is available for resolving any
 * filename conflicts that may arise by selective caching of file data at the
 * wrapper layer.
 * </p><p>
 * An {@code UploaderOperation} passes through four distinct phases in its
 * lifetime.
 * <ol>
 * <li>{@code INITIAL}. Starts at construction time and lasts until
 * {@code startReceivingUploads()} is invoked. Not much of anything useful can
 * be done during this phase.</li>
 * <li>{@code RECEIVING_UPLOADS}. Starts with invocation of
 * {@code startReceivingUploads()} and lasts until
 * {@code startManagingHeldFiles()} is invoked. During this time it is expected
 * that the web application will receive zero or more files uploaded from the
 * client side. The caller should convey each of these transfer requests to this
 * class by invoking {@code acceptUpload()}</li>
 * <li>{@code MANAGING_HELD_FILES}. Starts when
 * {@code startManagingHeldFiles()} is invoked and lasts until
 * {@code commitFiles()} is invoked. During this time it is expected that the
 * web application will call {@code getHeldFiles()} to obtain a list of all the
 * conflicts, present this information to the user, and based on his input call
 * either {@code abandonHeldFile()} or
 * {@code transferHeldFileToRepositoryManager()} for each held file</li>
 * <li>{@code COMPLETE}. Starts when {@code commitFiles()} is invoked. This is
 * a terminal state. Not much of anything useful can be done during this
 * phase</li>
 * <li>{@code ABORTED}. This is an error handling phase. It begins when
 * {@code unregister()} is invoked, but only if {@code COMPLETE} phase has not
 * yet been achieved. This is a terminal state; not much of anything useful can
 * be done during this phase</li>
 * </ol>
 * Note that these phases are not at all related to the phase-based evaluation
 * scheme defined by {@code HtmlPage} and implemented in the custom tags
 * library.
 * </p><p>
 * The {@code unregister()} method (as defined by {@code PersistedOperation})
 * cleans up any temporary resources this class may have acquired during its
 * operation. Callers should not invoke {@code unregister()} directly but
 * instead invoke {@code OperationPersister.closeOperation()} once their use for
 * an instance of this class has passed. Generally this class would have reached
 * {@code COMPLETE} phase or {@code ABORTED} phase by this time. However, care
 * has been taken to ensure that {@code unregister()} may be invoked at any
 * point in an instance's lifetime; the method will cause any in-progress file
 * transfers to be aborted.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class UploaderOperation extends WorkflowActionPersistedOperation {

    private enum Phase {
        /**
         * The initial phase in an {@code UploaderOperation}'s life cycle
         */
        INITIAL,
        
        /**
         * The phase in an {@code UploaderOperation}'s life cycle during which
         * receives uploaded files from a client
         */
        RECEIVING_UPLOADS,
        
        /**
         * The phase in an {@code UploaderOperation}'s life cycle during which
         * it determines, possibly in conjunction with the client, how the
         * uploaded files should be disposed
         */
        MANAGING_HELD_FILES,
        
        /**
         * The phase in an {@code UploaderOperation}'s life cycle after all
         * uploaded files have been satisfactorilly disposed
         */
        COMPLETE,
        
        /**
         * The phase in an {@code UploaderOperation}'s life cycle after the
         * overall upload operation has been aborted prior to normal completion
         */
        ABORTED
    }

    /** The buffer size for multipart/form-data uploads. */
    static final int BUFFER_SIZE = 65536;

    /**
     * Part of the state table: tracks the current phase in the
     * {@code UploaderOperation}'s lifetime. Method implementations must
     * synchronize on the {@code UploaderOperation} instance in order to read or
     * modify this value.
     */
    private Phase phase;

    /**
     * Part of the state table: a collection of {@code FileRecord} objects
     * that tracks state information about files being transferred. Initialized
     * by the constructor. Records are appended by {@code acceptUpload()}.
     * Several other methods manipulate the content of the {@code FileRecord}
     * objects contained within this collection. Method implementations must
     * synchronize on this {@code UploaderOperation} instance in order to access
     * this list or any records within it.
     */
    private Collection<FileRecord> files;

    /**
     * A reference to the web application's {@code CoreConnector} object; set by
     * the constructor.
     */
    private CoreConnector cc;

    /**
     * A reference to the web application's {@code FileTracker} object; set by
     * the constructor.
     */
    private FileTracker fileTracker;

    /**
     * Identifies the workflow action to be executed by this
     * {@code UploaderOperation}. Set by the constructor. Consult
     * {@code SampleManager.putSampleInfo()} for more information about the
     * particulars of this value.
     */
    private int workflowActionCode;

    /**
     * Configuration option set at construction time. If false, then any attempt
     * by the caller to upload a file whose name does not match any existing
     * sample data file associated with the sample will fail initially and the
     * file will be "held" for confirmation. If true, then such an upload would
     * succeed initially, without explicit confirmation.
     */
    private boolean alwaysAllowFileCreation;

    /**
     * Configuration option set at construction time. If false, then any attempt
     * by the caller to upload a file whose name matches that of an existing
     * sample data file associated with the sample will fail initially and the
     * file will be "held" for confirmation. If true, then such an upload would
     * succeed initially, without explicit confirmation.
     */
    private boolean alwaysAllowFileOverwrites;

    /**
     * Configuration option set at construction time. When this is true it
     * indicates that upon completion of the operation an upload confirmation
     * page should be displayed.
     */
    private boolean displayConfirmationPage;

    /**
     * Constructor.
     * 
     * @param cc a reference to the web application's {@code CoreConnector}
     *        object. This reference is retained by this operation and may be
     *        used at any point in this operation's lifetime.
     * @param fileTracker a reference to the web application's
     *        {@code FileTracker} object. This reference is retained by this
     *        operation and may be used at any point in this operation's
     *        lifetime.
     * @param expirationInterval the number of milliseconds past an access that
     *        this operation should expire if not accessed again. The special
     *        value {@code PersistedOperation.NO_TIME} indicates that the
     *        operation should not expire automatically.
     * @param sample the sample to be committed to the database as part of the
     *        workflow action that will be executed by this operation later.
     *        Equivalent to the {@code sample} argument to
     *        {@code SampleManager.putSampleInfo()}.
     * @param user identifies the user who should be credited with performing
     *        the workflow action that will be executed by this operation later.
     *        Equivalent to the {@code user} argument to
     *        {@code SampleManager.putSampleInfo()}.
     * @param workflowActionCode identifies the workflow action that will be
     *        executed by this operation later. Equivalent to the
     *        {@code actionCode} argument to
     *        {@code SampleManager.putSampleInfo()}.
     * @param workflowComments comment string associated with the workflow
     *        action that will be executed by this operation later. Equivalent
     *        to the {@code comments} argument to
     *        {@code SampleManager.putSampleInfo()}.
     * @param alwaysAllowFileCreation a configuration option that controls when
     *        uploaded files are "held" for further confirmation. If
     *        {@code false}, then any attempt by the caller to upload a file
     *        whose name does not match any existing sample data file associated
     *        with the sample will fail initially and the file will be held. If
     *        {@code true}, the such an upload would succeed initially, without
     *        explicit confirmation.
     * @param alwaysAllowFileOverwrites a configuration option that controls
     *        when uploaded files are "held" for further confirmation. If
     *        {@code false}, then any attempt by the caller to upload a file
     *        whose name matches than of an existing sample data file associated
     *        with the sample will fail initially and the file will be "held"
     *        for confirmation. If true, then such an upload would succeed
     *        initially, without explicit confirmation.
     * @param displayConfirmationPage a configuration option that indicates that
     *        an upload confirmation page should be displayed once the files are
     *        committed.
     */
    public UploaderOperation(CoreConnector cc, FileTracker fileTracker,
            long expirationInterval, SampleInfo sample, UserInfo user,
            int workflowActionCode, String workflowComments,
            boolean alwaysAllowFileCreation, boolean alwaysAllowFileOverwrites,
            boolean displayConfirmationPage) {
        super(expirationInterval, sample, workflowComments, user.id);

        this.phase = Phase.INITIAL;
        this.files = new ArrayList<FileRecord>();
        this.cc = cc;
        this.fileTracker = fileTracker;
        this.workflowActionCode = workflowActionCode;
        this.alwaysAllowFileCreation = alwaysAllowFileCreation;
        this.alwaysAllowFileOverwrites = alwaysAllowFileOverwrites;
        this.displayConfirmationPage = displayConfirmationPage;
    }

    /**
     * A method to determine whether a confirmation page should be displayed
     * upon completion of this operation.
     * 
     * @return {@code true} if the confirmation page should be displayed,
     *         {@code false} if not
     */
    public boolean getDisplayConfirmationPage() {
        return this.displayConfirmationPage;
    }

    /**
     * Overrides {@code PersistedOperation} and intended to be invoked by the
     * {@code OperationPersister} only. The current implementation aborts any
     * file transfers that are presently in-progress and cleans up temporary
     * files, etc. The method may be invoked in any phase of this
     * {@code UploaderOperation}'s lifetime; it will transition the operation's
     * phase to {@code ABORTED} unless the operation has already reached
     * {@code COMPLETED}.
     * 
     * @throws RemoteException on RMI error. In this case the exception
     *         would have been reported to {@code CoreConnector} already.
     */
    @Override
    protected synchronized void unregister(FileTracker tracker)
            throws IOException {
        assert (tracker == this.fileTracker) : "Wrong FileTracker";
        
        super.unregister(this.fileTracker);
        
        /*
         * The call to unregister() above has the effect of telling FileTracker
         * to forget about all of our temporary files that it had been tracking.
         * That invalidated our TrackedFile objects, and thus any temporary
         * files this operation may have had were deleted.
         */

        /*
         * Abort any file transfer that might still be in progress or that is
         * being held somewhere.
         */
        for (FileRecord rec : this.files) {
            rec.abortFileIfNotComplete();
        }

        // Update our state.
        if (this.phase != Phase.COMPLETE) {
            this.phase = Phase.ABORTED;
        }
    }

    /**
     * Transitions this {@code UploaderOperation} to {@code RECEIVING_UPLOADS}
     * PHASE and enables it to receive file transfers.
     * 
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code INITIAL}.
     */
    public synchronized void startReceivingUploads() {
        assertPhase(Phase.INITIAL);
        this.phase = Phase.RECEIVING_UPLOADS;
    }

    /**
     * Transitions this {@code UploaderOperation} to {@code MANAGING_HELD_FILES}
     * phase and enables the manipulation of any previously-uploaded files that
     * may have been held. The method aborts any file uploads that may be
     * in-progress by other threads at the time it is invoked. If the current
     * phase is already {@code MANAGING_HELD_FILES} phase at the time this
     * method is invoked, the method has no effect.
     * 
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code RECEIVING_UPLOADS} phase.
     * @throws IOException if there was a problem aborting any of the tickets.
     * @throws RemoteException on RMI error.
     */
    public synchronized void startManagingHeldFiles() throws IOException,
            RemoteException {
        if (this.phase == Phase.MANAGING_HELD_FILES) {
            // We're already in the mode the caller wants. Exit early.
            return;
        }
        assertPhase(Phase.RECEIVING_UPLOADS);

        // Abort any file transfer that might still be in progress.
        for (FileRecord rec : this.files) {
            if (rec.isWritingInProgress()) {
                rec.abortFileIfNotComplete();
            }
        }

        // Update our state.
        this.phase = Phase.MANAGING_HELD_FILES;
        touchUploadedFiles();
        notifyAccess();
    }

    /**
     * Transitions this {@code UploaderOperation} to {@code COMPLETE} by
     * executing the workflow action that was specified at construction time.
     * This will have the effect of saving zero or more uploaded files (that are
     * not still held) as sample data files in the repository. The method aborts
     * any files that are still "held" or that are presently being copied to
     * core by other threads.
     * 
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code MANAGING_HELD_FILES}.
     * @throws IOException if an I/O-related problem was encountered.
     * @throws RemoteException on RMI error.
     * @throws UnexpectedExceptionException on unexpected error.
     */
    public synchronized void commitFiles() throws IOException,
            RemoteException {
        assertPhase(Phase.MANAGING_HELD_FILES);

        /*
         * Abort any file transfer that is being held in a temp file and any
         * transfer that is being copied to RepositoryManager from a temp file.
         * Simultaneously, build a table of all the files presently held in
         * RepositoryManager.
         */
        
        Collection<FileRecord> filesToCommit = new ArrayList<FileRecord>();
        Collection<RepositoryFileOutputStream> outputStreams
                = new ArrayList<RepositoryFileOutputStream>();

        for (FileRecord rec : this.files) {
            if (rec.status != FileRecord.Status.HELD_IN_REPOSITORY_MANAGER) {
                // Abort this incomplete file.
                rec.abortFileIfNotComplete();
            } else {
                // Prepare to commit this file.
                filesToCommit.add(rec);
                outputStreams.add(rec.repositoryFileOutputStream);
            }
        }

        // Tell RepositoryManager to close a bunch of tickets at once.
        RepositoryFileOutputStream.closeInUnison(outputStreams);

        // Update the state on some file records.
        for (FileRecord rec : filesToCommit) {
            rec.status = FileRecord.Status.COMPLETE;
        }

        // Update our state.
        this.phase = Phase.COMPLETE;
        notifyAccess();
    }

    /**
     * During {@code RECEIVING_UPLOADS} phase, receives a file from some remote
     * host. Depending on the file's name, the value of
     * {@code alwaysAllowFileCreation} specified at construction time, and the
     * value of {@code alwaysAllowFileOverwrites} specified at construction
     * time, the file's data may be written directly to core or the file may be
     * "held" in a temporary file at the wrapper layer. The caller would be able
     * to detect such a held-file condition later, during
     * {@code MANAGING_HELD_FILES} phase.
     * <p>
     * This method is potentially very long-running, depending chiefly upon the
     * performance characteristics of the {@code InputStream} contained within
     * {@code transfer}. It is possible that a long-running upload may be
     * aborted if another thread were to invoke {@code startManagingHeldFiles()}
     * before this method had finished its work. In such a case this method
     * would exit prematurely, but the exit would be graceful and the aborted
     * condition would not be detectable by the caller.
     * 
     * @param transfer a {@code RepositoryFileTransfer} object that is in its
     *        "download mode", initialized to receive a file uploaded from some
     *        remote source.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code RECEIVING_UPLOADS}.
     * @throws InvalidDataException with the reason code
     *         {@code ILLEGAL_FILENAME} if {@code transfer.getFileName()}
     *         contains characters that are not valid for file names.
     * @throws IOException if some I/O-related problem was encountered. This may
     *         be an {@code InterruptedIOException} if another thread aborted
     *         this upload as it was in-progress.
     * @throws OperationFailedException on low-level error in core.
     * @throws RemoteException on RMI error. In this case the exception would
     *         have been reported to {@code CoreConnector} already.
     */
    public void acceptUpload(RepositoryFileTransfer transfer)
            throws InvalidDataException, IOException, OperationFailedException,
            RemoteException {
        notifyAccess();

        // Do some state checks and initialize state tables to receive a file.
        FileRecord rec;
        OutputStream outputStream;
        AtomicBoolean abortSignal;
        
        synchronized (this) {
            assertPhase(Phase.RECEIVING_UPLOADS);
            touchUploadedFiles();

            // Create a record to track the file transfer
            rec = new FileRecord(transfer.getFileName());
            this.files.add(rec);

            // Decide where we want to dump file data being transferred to us.
            try {
                // Attempt to send the file to RepositoryManager directly.
                rec.ticketId
                        = this.cc.getRepositoryManager().beginWritingDataFile(
                                getSampleInfo(), transfer.getFileName(),
                                this.alwaysAllowFileCreation,
                                this.alwaysAllowFileOverwrites,
                                this.workflowActionCode, getUserId(),
                                getComments(), null);
                rec.status = FileRecord.Status.WRITING_TO_REPOSITORY_MANAGER;
                rec.repositoryFileOutputStream
                        = new RepositoryFileOutputStream(this.cc, rec.ticketId);
                outputStream = rec.repositoryFileOutputStream;
            } catch (OperationNotPermittedException ex) {
                
                /*
                 * RepositoryManager refused to write the file because a file of
                 * the same name already exists in the primary repository
                 * directory and this.alwaysAllowFileOverwrites was false. So
                 * we're forced to write the file's data to a temporary file for
                 * now.
                 */
                rec.tempFile = this.fileTracker.createTempFile(
                        "uploadoperation", ".tmp");
                rec.trackedFileId = this.fileTracker.trackFile(rec.tempFile,
                        "application/octet-stream", true);
                notifyFileTracked(rec.trackedFileId);
                rec.status = FileRecord.Status.WRITING_TO_TEMP_FILE;
                rec.tempFileOutputStream = new FileOutputStream(rec.tempFile);
                outputStream = rec.tempFileOutputStream;
            } catch (ResourceNotFoundException ex) {
                
                /*
                 * RepositoryManager refused to write the file because no file
                 * of the specified name already exists in the primary
                 * repository directory and this.alwaysAllowFileCreation was
                 * false. So we're forced to write the file's data to a
                 * temporary file for now.
                 */
                rec.tempFile = this.fileTracker.createTempFile(
                        "uploadoperation", ".tmp");
                rec.trackedFileId = this.fileTracker.trackFile(rec.tempFile,
                        "application/octet-stream", true);
                notifyFileTracked(rec.trackedFileId);
                rec.status = FileRecord.Status.WRITING_TO_TEMP_FILE;
                rec.tempFileOutputStream = new FileOutputStream(rec.tempFile);
                outputStream = rec.tempFileOutputStream;
            } catch (RemoteException ex) {
                this.cc.reportRemoteException(ex);
                throw ex;
            }

            // Cache some values from the FileRecord object.
            abortSignal = rec.abortSignal;
        }

        /*
         * Receive file data and copy it to whatever OutputStream was selected
         * above. This section of code is deliberately not synchronized because
         * it may be long-running and we don't want to block other threads.
         */
        try {
            transfer.doTransfer(outputStream,
                    new TransferRegulator(abortSignal));
        } catch (IOException ex) {
            // Some I/O related problem caused the transfer to fail.
            notifyAccess();
            updateFileForTerminatedTransfer(rec, ex); // will throw
        }
        
        notifyAccess();
        touchUploadedFiles();
        updateFileForTerminatedTransfer(rec, null);
    }

    /**
     * During {@code RECEIVING_UPLOADS} phase, receives a file from a HTTP post
     * parsed with the MultipartMimeFormParser.
     * 
     * @param parser A {@code MultipartMimeFormParser} that has advanced through
     *        the POSTED stream to the beginning of file data for a file being
     *        uploaded. At the end of this method this
     *        {@code MultipartMimeFormParser}'s stream will have been advanced
     *        to the end of the file data, and the following field which is
     *        expected to contain a description of the file will have been read
     * @param filename the name that the file will be saved as; may or may not
     *        be the current file name.
     * @param description a file description for the uploaded file
     * @throws IllegalArgumentException if the provided 'filename' has already
     *         been used in a previous call to this function
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code RECEIVING_UPLOADS}.
     * @throws InvalidDataException with the reason code
     *         {@code ILLEGAL_FILENAME} if the 'filename' parameter is an
     *         invalid filename or contains characters that are not valid for
     *         file names.
     * @throws IOException if some I/O-related problem was encountered. This may
     *         be an {@code InterruptedIOException} if another thread aborted
     *         this upload as it was in-progress.
     * @throws OperationFailedException on low-level error in core.
     * @throws RemoteException on RMI error. In this case the exception would
     *         have been reported to {@code CoreConnector} already.
     */
    public void acceptUpload(MultipartMimeFormParser parser, String filename,
            String description) throws IOException, InvalidDataException,
            OperationFailedException, RemoteException {
        notifyAccess();

        // Do some state checks and initialize state tables to receive a file.
        FileRecord rec;
        OutputStream outputStream;
        
        synchronized (this) {
            assertPhase(Phase.RECEIVING_UPLOADS);
            touchUploadedFiles();

            /*
             * Ensure that a file with the same name isn't already part of this
             * operation
             */
            for (FileRecord record : this.files) {
                if (record.name.equals(filename)) {
                    throw new IllegalArgumentException();
                }
            }

            // Create a record to track the file transfer
            rec = new FileRecord(filename);
            rec.description = description;
            this.files.add(rec);
            
            // Decide where we want to dump file data being transferred to us.
            try {
                // Attempt to send the file to RepositoryManager directly.
                rec.ticketId
                        = this.cc.getRepositoryManager().beginWritingDataFile(
                                getSampleInfo(), filename,
                                this.alwaysAllowFileCreation,
                                this.alwaysAllowFileOverwrites,
                                this.workflowActionCode, getUserId(),
                                getComments(), description);
                rec.status = FileRecord.Status.WRITING_TO_REPOSITORY_MANAGER;
                rec.repositoryFileOutputStream
                        = new RepositoryFileOutputStream(this.cc, rec.ticketId);
            } catch (OperationNotPermittedException ex) {
                /*
                 * RepositoryManager refused to write the file because a file of
                 * the same name already exists in the primary repository
                 * directory and this.alwaysAllowFileOverwrites was false. So
                 * we're forced to write the file's data to a temporary file for
                 * now.
                 */
                rec.tempFile = this.fileTracker.createTempFile(
                        "uploadoperation", ".tmp");
                rec.trackedFileId = this.fileTracker.trackFile(rec.tempFile,
                        "application/binary", true);
                notifyFileTracked(rec.trackedFileId);
                rec.status = FileRecord.Status.WRITING_TO_TEMP_FILE;
                rec.tempFileOutputStream = new FileOutputStream(rec.tempFile);
            } catch (ResourceNotFoundException ex) {
                /*
                 * RepositoryManager refused to write the file because no file
                 * of the specified name already exists in the primary
                 * repository directory and this.alwaysAllowFileCreation was
                 * false. So we're forced to write the file's data to a
                 * temporary file for now.
                 */
                rec.tempFile = this.fileTracker.createTempFile(
                        "uploadoperation", ".tmp");
                rec.trackedFileId = this.fileTracker.trackFile(rec.tempFile,
                        "application/binary", true);
                notifyFileTracked(rec.trackedFileId);
                rec.status = FileRecord.Status.WRITING_TO_TEMP_FILE;
                rec.tempFileOutputStream = new FileOutputStream(rec.tempFile);
            } catch (RemoteException ex) {
                this.cc.reportRemoteException(ex);
                throw ex;
            }

            // Cache some values from the FileRecord object.
            outputStream = rec.repositoryFileOutputStream != null
                    ? rec.repositoryFileOutputStream
                    : rec.tempFileOutputStream;
        }

        /*
         * Receive file data and copy it to whatever OutputStream was selected
         * above. This section of code is deliberately not synchronized because
         * it may be long-running and we don't want to block other threads.
         */
        try {
            byte buffer[];
            
            for (;;) {
                buffer = parser.readCurrentFieldValueAsBytes(BUFFER_SIZE);
                notifyAccess();
                if (buffer == null) {
                    break;
                }
                outputStream.write(buffer);
            }
        } catch (IOException ex) {
            // Some I/O related problem caused the transfer to fail.
            updateFileForTerminatedTransfer(rec, ex); // will throw
        }
        notifyAccess();
        touchUploadedFiles();
        updateFileForTerminatedTransfer(rec, null);
    }

    /**
     * <p>
     * During {@code MANAGING_HELD_FILES} phase, allows the caller to discover
     * which uploaded files, if any, were held rather than being written
     * directly to core when {@code acceptUpload()} was invoked previously. For
     * each entry returned, the caller should consult the user and then invoke
     * either {@code abandonHeldFile()} or
     * {@code transferHeldFileToRepositoryManager()} as appropriate.
     * </p><p>
     * Any uploaded files this function might return records for have been
     * registered with the {@code FileTracker} and are potentially
     * web-accessible via the {@code FileRetriever} servlet. The value returned
     * from {@code SampleDataField.getUrl()} on the returned records is
     * constructed thusly:
     * {@code fileRetrieveServletHref/filename/keyParamName=key} , where
     * {@code fileRetrieveServletHref} and {@code keyParamName} are specified by
     * the caller.
     * </p>
     * 
     * @return a {@code Collection} of zero or more {@code SampleDataFile}'s
     *         that describes the set of held files.
     * @param fileRetrieveServletHref a string used in constructing the URL
     *        contained within returned file records.
     * @param keyParamName a string used in constructing the URL contained
     *        within the returned file records.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code MANAGING_HELD_FILES}.
     */
    public synchronized Collection<? extends SampleDataFile> getHeldFiles(
            String fileRetrieveServletHref, String keyParamName) {
        assertPhase(Phase.MANAGING_HELD_FILES);

        Collection<HeldFileRecord> heldFiles = new ArrayList<HeldFileRecord>();

        for (FileRecord rec : this.files) {
            if (rec.status == FileRecord.Status.HELD_IN_TEMP_FILE) {
                heldFiles.add(new HeldFileRecord(rec, getSampleInfo().id,
                        fileRetrieveServletHref + "/" + rec.name + "?"
                                + keyParamName + "="
                                + Long.toString(rec.trackedFileId)));
            }
        }
        
        return heldFiles;
    }

    /**
     * During {@code MANAGING_HELD_FILES}, allows the caller to discover if at
     * least one uploaded file was held rather than being directly written to
     * core.
     * 
     * @return true if at least one file was held, or false otherwise.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code MANAGING_HELD_FILES}.
     */
    public synchronized boolean isAnyFileHeld() {
        assertPhase(Phase.MANAGING_HELD_FILES);
        
        for (FileRecord rec : this.files) {
            if (rec.status == FileRecord.Status.HELD_IN_TEMP_FILE) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * During {@code MANAGING_HELD_FILES} phase, allows the caller to specify
     * that a particular "held" file (as returned by a call to
     * {@code getHeldFiles()} previously) should be abandoned and aborted,
     * rather than sent to core.
     * 
     * @param heldFile a {@code SampleDataFile} object of the sort that was
     *        returned by a previous call to {@code getHeldFiles()}.
     * @throws ClassCastException if {@code file} was not returned by a previous
     *         call to {@code getHeldFiles()}.
     * @throws IllegalArgumentException if the specified file is not (has never
     *         been) associated with this operation.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code MANAGING_HELD_FILES}, or if the specified file is no
     *         longer being held at the wrapper layer.
     * @throws IOException if there was a problem aborting any of the tickets.
     * @throws RemoteException on RMI error.
     */
    public synchronized void abandonHeldFile(SampleDataFile heldFile)
            throws IOException, RemoteException {
        assertPhase(Phase.MANAGING_HELD_FILES);

        // Obtain a matching FileRecord.
        FileRecord rec = ((HeldFileRecord) heldFile).getFileRecord();

        if (rec.status != FileRecord.Status.HELD_IN_TEMP_FILE) {
            // Perhaps the caller previously abandoned this same file.
            throw new IllegalStateException();
        }
        if (!this.files.contains(rec)) {
            // Perhaps the caller got this record from some other
            // UploaderOperation;
            throw new IllegalArgumentException();
        }

        // Flag the file record as aborted.
        rec.abortFileIfNotComplete();
    }

    /**
     * <p>
     * During {@code MANAGING_HELD_FILES}, allows the caller to specify that a
     * particular "held" file (as returned by a call to {@code getHeldFiles()}
     * previously) should be written to core rather than abandoned. (The file
     * would not be committed, however, until the caller later invoked
     * {@code commitFiles()}). The success of such an operation would depend
     * upon the value of {@code allowFileCreation}, the value of
     * {@code allowFileOverwrite}, and the possible existance of a sample data
     * file with the same name.
     * </p><p>
     * This method is potentially long-running, depending upon the performance
     * characteristics of the link to core and the length of the file's data. It
     * is possible that a long-running transfer might be aborted if another
     * thread were to invoke {@code commitFiles()} before this method had
     * finished its work. In such a case this method would exit prematurely, but
     * the exit would be graceful and the aborted condition would not be
     * detectable immediately by the caller.
     * </p><p>
     * The configuration parameters {@code alwaysAllowFileCreation} and
     * {@code alwaysAllowFileOverwrites} specified at construction time have no
     * effect on this method.
     * </p>
     * 
     * @param heldFile a {@code SampleDataFile} object of the sort that was
     *        returned by a previous call to {@code getHeldFiles()}.
     * @param allowFileCreation if {@code true}, and no sample data file with
     *        name {@code name} already exists, then the transfer succeeds, a
     *        new sample data file is created, and no exception is thrown.
     * @param allowFileOverwrite if {@code true}, and a sample data file whose
     *        name is {@code name} already exists, then the transfer succeeds,
     *        the existing sample data file is overwritten, and no exception is
     *        thrown.
     * @throws ClassCastException if {@code file} was not returned by a previous
     *         call to {@code getHeldFiles()}.
     * @throws IllegalArgumentException if {@code name} does not identify a held
     *         file, or if {@code heldFile} is not associated (has never been
     *         associated) with this operation.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code MANAGING_HELD_FILES}, or if the specified file is no
     *         longer being held at the wrapper layer.
     * @throws InvalidDataException with the reason code
     *         {@code ILLEGAL_FILENAME} if {@code name} contains characters that
     *         are not valid when naming a sample data file.
     * @throws IOException if some I/O-related problem was encountered.
     * @throws OperationFailedException on low-level error in core.
     * @throws OperationNotPermittedException if the specified file already
     *         exists within RepositoryManager's primary directory and
     *         {@code allowFileOverwrite} is {@code false}.
     * @throws RemoteException on RMI error. In this case the exception would
     *         have been reported to {@code CoreConnector} already.
     * @throws ResourceNotFoundException if the specified file does not already
     *         exist within RepositoryManager's primary directory and
     *         {@code allowFileCreation} is {@code false}.
     */
    public void keepHeldFile(SampleDataFile heldFile,
            boolean allowFileCreation, boolean allowFileOverwrite)
            throws InvalidDataException, IOException, OperationFailedException,
            OperationNotPermittedException, RemoteException,
            ResourceNotFoundException {
        notifyAccess();

        // Do some checks and initialize state tables to transfer a file.
        RepositoryFileOutputStream outputStream;
        FileRecord rec;
        synchronized (this) {
            assertPhase(Phase.MANAGING_HELD_FILES);
            touchUploadedFiles();

            // Obtain a matching FileRecord.
            rec = ((HeldFileRecord) heldFile).getFileRecord();
            
            if (rec.status != FileRecord.Status.HELD_IN_TEMP_FILE) {
                // Perhaps the caller previously abandoned this same file.
                throw new IllegalStateException();
            }
            if (!this.files.contains(rec)) {
                
                /*
                 * Perhaps the caller got this record from some other
                 * UploaderOperation.
                 */
                throw new IllegalArgumentException();
            }

            // Attempt to open a RepositoryManager ticket.
            try {
                rec.ticketId
                        = this.cc.getRepositoryManager().beginWritingDataFile(
                                getSampleInfo(), rec.name, allowFileCreation,
                                allowFileOverwrite, this.workflowActionCode,
                                getUserId(), getComments(), rec.description);
            } catch (RemoteException ex) {
                this.cc.reportRemoteException(ex);
                throw ex;
            }
            outputStream
                    = new RepositoryFileOutputStream(this.cc, rec.ticketId);
            rec.repositoryFileOutputStream = outputStream;

            // Update our state table to indicate the transfer is in progress.
            rec.status = FileRecord.Status.COPYING_TO_REPOSITORY_MANAGER;
        }

        /*
         * Transfer file data from the temporary file to RepositoryManager.
         * This section of code is deliberately not synchronized because it may
         * be long-running and we don't want to block other threads.
         */
        IOException ioExceptionReceived = null;
        try {
            InputStream in = new FileInputStream(rec.tempFile);
            
            outputStream.copyFromStream(in);
            in.close();
        } catch (IOException ex) {
            ioExceptionReceived = ex;
        }
        notifyAccess();

        // Update state tables now that the transfer is terminated.
        synchronized (this) {
            FileRecord.Status s = rec.status;

            if (s == FileRecord.Status.COPYING_TO_REPOSITORY_MANAGER) {
                if (ioExceptionReceived == null) {
                    /*
                     * The file was successfully copied to RepositoryManager. We
                     * need to leave repositoryFileOutputStream open for now
                     * because commitFiles() will close it later.
                     */
                    rec.status = FileRecord.Status.HELD_IN_REPOSITORY_MANAGER;
                } else {
                    /*
                     * The file did not finish copying due to an IOException.
                     * Set a failure status and abort the ticket.
                     */
                    // TODO: log the IOException somehow
                    rec.status = FileRecord.Status.ABORTED_IN_REPOSITORY_MANAGER;
                    rec.repositoryFileOutputStream.abort();
                    rec.repositoryFileOutputStream = null;
                }
            } else if (s == FileRecord.Status.ABORTED_IN_REPOSITORY_MANAGER) {
                /*
                 * Another thread told us to abort the transfer as the copy
                 * operation was in progress. We need to clean up by aborting
                 * the ticket.
                 */
                rec.repositoryFileOutputStream.abort();
                rec.repositoryFileOutputStream = null;
            }
            
            touchUploadedFiles();
        }
    }

    /**
     * During {@code COMPLETE} phase, allows the caller to discover which
     * uploaded files, if any, were written to core by this operation.
     * 
     * @return a {@code Collection} of zero or more filenames that describes the
     *         set of complete files.
     * @throws IllegalStateException if the current phase is anything other than
     *         {@code COMPLETE}.
     */
    public synchronized Collection<String> getCompleteFilenames() {
        assertPhase(Phase.COMPLETE);
        Collection<String> completeFilenames = new ArrayList<String>();
        
        for (FileRecord rec : this.files) {
            if (rec.status == FileRecord.Status.COMPLETE) {
                completeFilenames.add(rec.name);
            }
        }
        
        return completeFilenames;
    }

    /**
     * Internal helper function that throws an exception if the current phase
     * differs from the phase expected by the caller.
     * 
     * @param expectedPhase see comments on {@code phase} for a complete list of
     *        valid values.
     * @throws IllegalStateException if {@code expectedPhase} differs from this
     *         {@code UploaderOperation}'s current phase.
     */
    private synchronized void assertPhase(Phase expectedPhase) {
        if (this.phase != expectedPhase) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Causes those temporary files currently held in the repository to be
     * "touched" without being modified, so that the Repository Manager will put
     * off discarding them
     * 
     * @throws IOException on error; in particular, this exception will be
     *         thrown if the Repository Manager has already discarded any of the
     *         files
     */
    public synchronized void touchUploadedFiles() throws IOException {
        List<RepositoryFileOutputStream> streamsToTouch
                = new ArrayList<RepositoryFileOutputStream>(files.size());
        
        for (FileRecord rec : files) {
            if (rec.status == FileRecord.Status.HELD_IN_REPOSITORY_MANAGER) {
                assert rec.repositoryFileOutputStream != null;
                streamsToTouch.add(rec.repositoryFileOutputStream);
            } else if (rec.status == FileRecord.Status.HELD_IN_TEMP_FILE) {
                // generates a FileTracker file access:
                fileTracker.getTrackedFile(rec.trackedFileId);
            }
        }
        
        if (!streamsToTouch.isEmpty()) {
            RepositoryFileOutputStream.touchStreams(streamsToTouch);
        }
    }

    /**
     * Updates a {@code FileRecord}s state to reflect the termination of data
     * transfer either because the file was completed or because an exception
     * was encountered.
     * 
     * @param rec the file for which data transfer has been completed
     * @param ioExceptionReceived the IOException that resulted in the
     *        termination of the file transfer or null if the transfer ended
     *        naturally.
     * @throws IOException if the transfer was ended by a supplied IOException
     *         or because the upload was aborted
     */
    private synchronized void updateFileForTerminatedTransfer(FileRecord rec,
            IOException ioExceptionReceived) throws IOException {
        switch (rec.status) {
            case WRITING_TO_REPOSITORY_MANAGER:
                if (ioExceptionReceived == null) {
                    /*
                     * The file was successfully written to RepositoryManager.
                     * We need to leave repositoryFileOutputStream open for now
                     * because commitFiles() will close it later.
                     */
                    rec.status = FileRecord.Status.HELD_IN_REPOSITORY_MANAGER;
                } else {
                    /*
                     * The file did not finish writing due to an IOException.
                     * Set a failure status and abort the ticket.
                     */
                    rec.status = FileRecord.Status.ABORTED_IN_REPOSITORY_MANAGER;
                    rec.repositoryFileOutputStream.abort();
                    rec.repositoryFileOutputStream = null;
                    throw ioExceptionReceived;
                }
                break;
            case ABORTED_IN_REPOSITORY_MANAGER:
                /*
                 * Another thread told us to abort the transfer as the write
                 * operation was in progress. We need to clean up by aborting
                 * the ticket.
                 */
                rec.repositoryFileOutputStream.abort();
                rec.repositoryFileOutputStream = null;
                throw new InterruptedIOException();
            case WRITING_TO_TEMP_FILE:
                if (ioExceptionReceived == null) {
                    /*
                     * The file was successfully written to the temporary area.
                     * We will close the file so it can be manipulated later.
                     * FileTracker will eventually delete the file once this
                     * operation is unregistered.
                     */
                    rec.status = FileRecord.Status.HELD_IN_TEMP_FILE;
                    rec.tempFileOutputStream.close();
                    rec.tempFileOutputStream = null;
                } else {
                    /*
                     * The file did not finish writing due to an IOException.
                     * Set a failure status and close the file. FileTracker will
                     * eventually delete the file once this operation is
                     * unregistered.
                     */
                    rec.status = FileRecord.Status.ABORTED_TEMP_FILE;
                    rec.tempFileOutputStream.close();
                    rec.tempFileOutputStream = null;
                    throw ioExceptionReceived;
                }
                break;
            case ABORTED_TEMP_FILE:
                /*
                 * Another thread told us to abort the transfer as the write
                 * operation was in progress. We need to clean up by closing the
                 * temporary file. FileTracker will eventually delete the file
                 * once this operation is unregistered.
                 */
                rec.tempFileOutputStream.close();
                rec.tempFileOutputStream = null;
                throw new InterruptedIOException();
            default:
                /*
                 * The invoker is responsible for not calling this method in
                 * these cases; if he is irresponsible, then we don't
                 * necessarilly need to do anything
                 */
                assert false;
        }
    }

    /**
     * Internal record-keeping class used to track state on a per-file basis.
     */
    private static class FileRecord {
        
        /**
         * An enumeration of the various statuses that may be assigned to files
         * managed by an {@code UploaderOperation}
         */
        public enum Status {
            /** Initial state; nothing's happened yet. */
            CREATED,

            /**
             * The file was not eligible to be sent to Repository Manager
             * directly for some reason, so the file's data is being written to
             * a temporary file at the wrapper layer instead. This status
             * indicates that bytes are being transferred from the client to the
             * server. In a typical workflow, this status is preceded by CREATED
             * and folowed by HELD_IN_TEMP_FILE.
             */
            WRITING_TO_TEMP_FILE {
                @Override
                public boolean isWritingFile() {
                    return true;
                }
            },

            /**
             * The file's data is being received from the client and streamed
             * directly to Repository Manager. In a typical workflow, this
             * status is preceded by CREATED and followed by
             * HELD_IN_REPOSITORY_MANAGER.
             */
            WRITING_TO_REPOSITORY_MANAGER {
                @Override
                public boolean isWritingFile() {
                    return true;
                }
            },

            /**
             * File data has been received from the client and is being stored
             * temporarily at the wrapper layer. This indicates that the
             * transfer of bytes from client to server has finished. In a
             * typical workflow, this status is preceded by WRITING_TO_TEMP_FILE
             * and followed by COPYING_TO_REPOSITORY_MANAGER.
             */
            HELD_IN_TEMP_FILE,

            /**
             * File data has been received from the client and is being stored
             * in Repository Manager. (Although the sample data file won't be
             * visible to other users until the operation completes.) This
             * indicates that the transfer of bytes from client to server has
             * finished. In a typical workflow, this status is preceded by
             * WRITING_TO_REPOSITORY_MANAGER and followed by COMPLETE.
             */
            HELD_IN_REPOSITORY_MANAGER,

            /**
             * File data that previously was received from the client and stored
             * in a temporary file at the wrapper layer for some reason is now
             * being copied to Repository Manager. In a typical workflow, this
             * status is preceded by HELD_IN_TEMP_FILE and followed by
             * HELD_IN_REPOSITORY_MANAGER.
             */
            COPYING_TO_REPOSITORY_MANAGER {
                @Override
                public boolean isWritingFile() {
                    return true;
                }
            },

            /**
             * Usually the terminal status code, this indicates that a file that
             * was written to Repository Manager has been committed as a real
             * sample data file. In a typical workflow, this status is preceded
             * by HELD_IN_REPOSITORY_MANAGER.
             */
            COMPLETE,

            /**
             * An abnormal status code that indicates the file transfer has been
             * aborted. It also indicates that there may be resources associated
             * with a temporary file at the wrapper layer that need to be
             * cleaned up.
             */
            ABORTED_TEMP_FILE,

            /**
             * An abnormal status code that indicates the file transfer has been
             * aborted. It also indicates that there may be resources associated
             * with a temporary file at the wrapper layer or in Repository
             * Manager that need to be cleaned up.
             */
            ABORTED_IN_REPOSITORY_MANAGER;
            
            /**
             * Determines whether this status is one in which the operation
             * involves writing or copying a file
             * 
             * @return {@code true} if this status involves writing or copying a
             *         file, {@code false} if not
             */
            public boolean isWritingFile() {
                return false;
            }
        }

        /** The status of the file record; see possible status codes above. */
        public Status status;

        /** The name of the file, absent any path elements */
        public String name;

        /** The description of the file. */
        public String description;

        /**
         * A {@code RepositoryManager} ticket id, of the sort issued by
         * {@code RepositoryManager.beginWritingDataFile()}. The field contains
         * sensical values only when the record's status is
         * WRITING_TO_REPOSITORY_MANAGER or COPYING_TO_REPOSITORY_MANAGER or
         * HELD_IN_REPOSITORY_MANAGER. The field is 0 if no ticket has been
         * opened yet.
         */
        public int ticketId;

        /**
         * A reference to any temporary file that may be held at the wrapper
         * layer. The field contains sensical values only when the record's
         * status is WRITING_TO_TEMP_FILE or HELD_IN_TEMP_FILE or
         * COPYING_TO_REPOSITORY_MANAGER. The field is null if there is no
         * associated temp file at the wrapper layer associated with this file
         * record.
         */
        public File tempFile;

        /**
         * An id obtained from the {@code FileTracker} that identifies the same
         * file referenced by {@code tempFile}. It is necessary for us to hang
         * on to this id so that we can make sure that {@code FileTracker}
         * deletes our temp file later, once we're done with it. The field is 0
         * if no temp file has been created yet.
         */
        public long trackedFileId;

        /**
         * An output stream that dumps data to the temporary file held at the
         * wrapper layer. This field contains sensical values only when the file
         * record's status is WRITING_TO_TEMP_FILE. The field is null if there
         * is no temp file or if the temp file is not presently open for
         * writing.
         */
        public FileOutputStream tempFileOutputStream;

        /**
         * An output stream that dumps data to RepositoryManager as the new
         * sample data file is created in the primary repository directory. The
         * field contains sensical values only when the file record's status is
         * WRITING_TO_REPOSITORY_MANAGER or COPYING_TO_REPOSITORY_MANAGER. The
         * field is null if there is no file in Repository Manager or if the
         * file in Repository Manager is not presently open for writing.
         */
        public RepositoryFileOutputStream repositoryFileOutputStream;

        /**
         * A signal that should be set to true when the user has desired t abort
         * the transfer of this file for some reason. Threads that perform
         * long-running operations associated with the file record such as
         * copying or downloading should monitor this signals and terminate
         * their operations early if the signal should become set.
         */
        public AtomicBoolean abortSignal;

        /**
         * Constructs that sets the {@code name} field to a caller-specified
         * value and sets all other fields to default values.
         * 
         * @param  name the name of the file represented by this record
         */
        public FileRecord(String name) {
            this.status = Status.CREATED;
            this.name = name;
            this.description = null;
            this.ticketId = 0;
            this.tempFile = null;
            this.trackedFileId = 0;
            this.tempFileOutputStream = null;
            this.repositoryFileOutputStream = null;
            this.abortSignal = new AtomicBoolean();
        }

        /**
         * Method that "aborts" a specified file transfer. The precise meaning
         * of "abort" varies according to this record's status. It removes any
         * temporary files that may exist, aborts any {@code RepositoryManager}
         * tickets that may exist, and updates the status value of {@code rec}.
         * This method has no effect if the file already has reached a terminal
         * state.
         * 
         * @throws IOException if there was a problem aborting any of the
         *         tickets.
         * @throws RemoteException on RMI error. In this case the exception
         *         would have been reported to {@code CoreConnector} already.
         */
        public void abortFileIfNotComplete() throws IOException {
            switch (this.status) {
                case CREATED:
                    // Nothing to do.
                    break;
                case WRITING_TO_TEMP_FILE:
                    /*
                     * Another thread is presently writing this file. Signal him
                     * to stop. The writing thread is responsible for closing
                     * the output stream that he presently has open.
                     */
                    this.abortSignal.set(true);
                    /*
                     * Mark the temp file as aborted. Because the temp file is
                     * registered with FileTracker, it will be deleted by
                     * FileTracker as this operation is unregistered.
                     */
                    this.status = FileRecord.Status.ABORTED_TEMP_FILE;
                    break;
                case HELD_IN_TEMP_FILE:
                    /*
                     * There is no thread presently writing this file, but set
                     * the abort flag anyway for good measure.
                     */
                    this.abortSignal.set(true);

                    /*
                     * Mark the temp file as aborted. Because the temp file is
                     * registered with FileTracker and will be deleted by
                     * FileTracker as this operation is unregistered.
                     */
                    this.status = FileRecord.Status.ABORTED_TEMP_FILE;
                    break;
                case WRITING_TO_REPOSITORY_MANAGER:
                case COPYING_TO_REPOSITORY_MANAGER:
                    /*
                     * Another thread is presently writing this file. Signal him
                     * to stop. The writing thread is responsible for aborting
                     * the output stream that he presently has open.
                     */
                    this.abortSignal.set(true);

                    // Mark the temp file as aborted.
                    this.status
                            = FileRecord.Status.ABORTED_IN_REPOSITORY_MANAGER;
                    break;
                case HELD_IN_REPOSITORY_MANAGER:
                    /*
                     * Tell RepositoryManager to abort the temp file that was
                     * written earlier.
                     */
                    this.repositoryFileOutputStream.abort();
                    /*
                     * There is no thread presently writing this file, but set
                     * the abort flag anyway for good measure.
                     */
                    this.abortSignal.set(true);

                    // Mark the file as aborted.
                    this.status
                            = FileRecord.Status.ABORTED_IN_REPOSITORY_MANAGER;
                    break;
                case COMPLETE:
                case ABORTED_TEMP_FILE:
                case ABORTED_IN_REPOSITORY_MANAGER:

                    /*
                     * The file has already reached a terminal state, so there's
                     * no cleanup we need to do. Exit gracefully.
                     */
                    break;
                default:
                    /*
                     * Can't happen because all FileRecord states are accounted
                     * for above.
                     */
                    assert false;
            }
        }

        /**
         * Returns a string representation of this object; intended for
         * debugging purposes only
         * 
         * @return a {@code String} representation of this {@code FileRecord}
         */
        @Override
        public String toString() {
            return this.name + "(" + this.status + ")";
        }

        /**
         * Determines, based on the status assigned to this record, whether the
         * file described is currently being written or copied
         * 
         * @return {@code true} if the file is being written, {@code false} if
         *         not
         */
        public boolean isWritingInProgress() {
            return this.status.isWritingFile();
        }
    }

    /**
     * A recordkeeping class that that is exposed to callers through its
     * {@code SampleDataFile} interface. This class acts as a file handle of
     * sorts, that callers can use when invoking {@code abandonHeldFile()} or
     * {@code transferHeldFileToRepositoryManager()}, for instance. The
     * {@code FileRecord} class is not reused for this class's purpose because
     * this class maintains a URL that is based, in part, upon caller-specified
     * strings. Instead, this class contains a reference back to a
     * {@code FileRecord} class that is useful for {@code UploaderOperation}'s
     * internal manipulations.
     */
    private static class HeldFileRecord implements SampleDataFile {
        private FileRecord fileRecord;

        private int sampleId;

        private String url;

        protected HeldFileRecord(FileRecord fileRecord, int sampleId,
                    String url) {
            if ((fileRecord.status != FileRecord.Status.HELD_IN_TEMP_FILE)
                    || (fileRecord.tempFile == null)
                    || (fileRecord.tempFileOutputStream != null)) {
                throw new IllegalArgumentException();
            }
            this.fileRecord = fileRecord;
            this.sampleId = sampleId;
            this.url = url;
        }

        /**
         * {@inheritDoc}
         */
        public int getSampleId() {
            return this.sampleId;
        }

        /**
         * {@inheritDoc}
         */
        public int getSampleHistoryId() {
            return SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        }

        /**
         * {@inheritDoc}
         */
        public int getOriginalSampleHistoryId() {
            return SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        }

        /**
         * {@inheritDoc}
         */
        public String getName() {
            return this.fileRecord.name;
        }

        /**
         * {@inheritDoc}
         */
        public String getDescription() {
            return this.fileRecord.description;
        }

        /**
         * {@inheritDoc}
         */
        public long getSize() {
            return this.fileRecord.tempFile.length();
        }

        /**
         * {@inheritDoc}  This version returns the configured URL string.
         */
        public String getUrl() {
            return this.url;
        }

        /**
         * {@inheritDoc}  This version always returns {@code true}.
         */
        public boolean isProvisional() {
            return true;
        }

        /**
         * {@inheritDoc}.  This version always returns {@code false}.
         */
        public boolean isSettled() {
            return false;
        }

        /**
         * Returns the {@code FileRecord} configured on this
         * {@code HeldFileRecord}
         * 
         * @return the {@code FileRecord} configured on this
         *         {@code HeldFileRecord}
         */
        protected FileRecord getFileRecord() {
            return this.fileRecord;
        }
    }

    /**
     * An internal class that is used by {@code acceptUpload} when it interfaces
     * with the {@code RepositoryFileTransfer} object. It enables the
     * {@code UploaderOperation} to prematurely abort a transfer-in-progress if
     * the need should arise, and keeps this operation active during lengthy
     * file transfers.
     */
    private class TransferRegulator implements
            RepositoryFileTransfer.Regulator {
        private AtomicBoolean cancelSignal;

        /**
         * Initializes a new {@code TransferRegulator} with the specified
         * cancelation signal
         * 
         * @param cancelSignal an {@code AtomicBoolean} that this
         *        {@code Regulator} tests periodically to determine whether the
         *        file transfer it is regulating should be cancelled (which is
         *        the case if it holds the value {@code true})
         */
        public TransferRegulator(AtomicBoolean cancelSignal) {
            this.cancelSignal = cancelSignal;
        }

        /**
         * {@inheritDoc}.  This version cancels the transfer after this
         * {@code Regulator} has been instructed to do so
         * 
         * @return {@code false} if this {@code TransferRegulator}'s
         *         cancellation signal is set to {@code true}
         */
        public boolean shouldRepositoryFileTransferContinue(
                @SuppressWarnings("unused") String filename,
                @SuppressWarnings("unused") long bytesTransferred,
                @SuppressWarnings("unused") long bytesTotal) {
            notifyAccess();
            return !this.cancelSignal.get();
        }
    }
}
