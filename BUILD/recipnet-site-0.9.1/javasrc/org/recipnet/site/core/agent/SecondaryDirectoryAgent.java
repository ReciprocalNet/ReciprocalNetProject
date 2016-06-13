/*
 * Reciprocal Net project
 * 
 * SecondaryDirectoryAgent.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; also changed package
 *              references to match source code reorganization
 * 18-Aug-2004: cwestnea added directoryCreationLock, deleteUnusedDirectory(),
 *              and modified file throughout to use them.
 * 12-Jan-2005: ekoperda modified notifyLockRevoked() to account for spec
 *              change on RepositoryManager.passCoreMessage()
 * 01-Mar-2005: ekoperda modified getRepositoryFiles() to return per-file
 *              historical information
 * 02-May-2005: ekoperda adjusted getRepositoryFiles() to match changes in
 *              RepositoryFiles class
 * 11-May-2006: jobollin reformatted the source; added generics; removed unused
 *              imports
 * 20-Jun-2006: jobollin updated docs, modified exception handling to
 *              accommodate changes to PrimaryDirectoryAgent
 */

package org.recipnet.site.core.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.LockAgent;
import org.recipnet.site.core.lock.OutOfBandReadTicket;
import org.recipnet.site.core.lock.SecondaryDirectoryDummyTicket;
import org.recipnet.site.core.lock.SecondaryFileReadTicket;
import org.recipnet.site.core.msg.UnusedSecondaryDirectoryHintCM;
import org.recipnet.site.core.util.CvsInvoker;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.SecondaryDirectory;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * <p>
 * An agent owned by RepositoryManager. A "secondary directory" is a temporary
 * directory on the filesystem, within the repository area, which contains zero
 * or more data files associated with a particular sample/version. Secondary
 * directories are created on an as-needed basis and destroyed when they're no
 * longer needed. Recall that data files associated with samples are
 * <i>versioned</i> by {@code PrimaryDirectoryAgent} with the help of
 * the CVS program. {@code SecondaryDirectoryAgent} taps in to the same
 * CVS repository used by {@code PrimaryDirectoryAgent} to populate
 * secondary directories: CVS provides a facility for extracting a particular
 * version of a particular data file (associated with a particular version of a
 * particular sample) to a secondary directory on demand.
 * </p><p>
 * A particular secondary directory managed by this object is identified by a
 * {@code SecondaryDirectory} container object. The secondary directory
 * area within the local site's repository must be initialized, only once per
 * filesystem. {@code createArea()} should be invoked at installation
 * time to effect this.
 * </p><p>
 * It is recommended that callers invoke {@code purgeAll()} at shutdown
 * time in order to keep the secondary directory area clean. This object is a
 * {@code LockListener}, in the {@code LockAgent} sense, and
 * receives a notification every time a core lock is granted or revoked. In this
 * way the active locks which reference files within secondary directories are
 * tracked, and when the active lock count reaches zero the secondary directory
 * becomes eligible to be purged.
 * </p><p>
 */
public class SecondaryDirectoryAgent implements LockAgent.LockListener {

    // TODO: add lots of logging throughout.

    /** Config option set at construction time */
    private final String repositoryBaseUrl;

    /** Config option set at construction time */
    private final File repositoryBaseDirectory;

    /** Config option set at construction time */
    private final long gracePeriod;

    /** Config option set at construction time */
    private final long fileSizeLimit;

    /** Config option set at construction time */
    private final long maxTotalSizeUsed;

    /** Reference to a CvsInvoker object provided at construction time. */
    private final CvsInvoker cvsInvoker;

    /**
     * Reference to a RepositoryTicketAgent object provided at construction
     * time.
     */
    private final LockAgent lockAgent;

    /** Reference to SiteManager object provided at construction time. */
    private final SiteManager siteManager;

    /** Reference to RepositoryManager object provided at construction time. */
    private final RepositoryManager repositoryManager;

    /**
     * A lock that is held while a directory is being created in order to prevent the directory from being deleted prematurely. A thread must never attempt to get the  {@code  directoryCreationLock}  monitor while it holds the  {@code  directories}  monitor because the chance of deadlock is too great. This lock is used in four places: {@code  clearSomeSpace()} ,  {@code  getRepositoryFiles()} , {@code  getFile()} , and  {@code  deleteUnusedDirectory()} . The use in  {@code  clearSomeSpace()}  is somewhat rendundant since it is only called from  {@code  extractFile()}  and that is only called from  {@code  getRepositoryFiles()}  and {@code  getFile()} . The other methods are never called internally so there is no direct chance someone attempting to get this lock while holding the directories lock. Additionally all calls to external methods that are made while holding the directories lock never call any method on this class, so there is no chance of a directoryCreationLock getting method being called from an external method by a thread that has gotten the directories lock.
     */
    private final Object directoryCreationLock;

    /**
     * Associates keys (unspecified type) with {@code DirectoryRecord}
     * objects; one object for each registered secondary directory. Methods
     * should synchronize on this to prevent concurrent modifications during
     * both reads and updates. A thread must never hold this lock when
     * attempting to get the {@code directoryCreationLock}. Anytime a
     * directory record is to be used, this lock must be held.
     */
    private final Map<Object, DirectoryRecord> directories;

    /**
     * Associates {@code RepositoryTicket} objects with
     * {@code DirectoryRecord} objects, one mapping for each active
     * ticket on a secondary directory. This map exists for efficiency's sake
     * only; it duplicates the information present in the {@code tickets}
     * field of each {@code RepositoryDirectory} object stored in
     * {@code directories}. Synchronize on {@code directories} to
     * prevent concurrent modifications.
     */
    private final Map<AbstractLock, DirectoryRecord> ticketsToDirectories;

    /**
     * Counter that tracks the total number of bytes currently consumed by all
     * files in all registered secondary directories. Synchronize on
     * {@code directories} to prevent concurrent modifications.
     */
    private long totalSpaceUsed;

    /**
     * The only constructor. Purges all files/directories that lie within the
     * secondary directory area on the filesystem in order to begin with a clean
     * slate.
     * 
     * @param repositoryBaseUrl the URL fragment under which the local site's
     *        repository is accessible to web browsers. The should match the web
     *        server's configuration; an appropriate value is probably stored in
     *        the 'repositoryUrl' field of the db table 'sites'. For instance:
     *        'http://www.server.edu/recipnet/data/'. Should end with a trailing
     *        slash.
     * @param repositoryBaseDirectory the filesystem directory under which the
     *        local site's repository is accessible to local processes. For
     *        instance: '/var/recipnet/data/'.
     * @param gracePeriod the number of milliseconds after its creation that a
     *        secondary directory becomes eligible for deletion if no tickets
     *        have been bound to it.
     * @param fileSizeLimit the maximum size, in bytes, of any single file that
     *        is extracted from CVS into a temporary directory. Files larger
     *        than this limit are not extracted (unless the caller opts to
     *        ignore limits). Set this to 0 to disable limits.
     * @param maxTotalSizeUsed the maximum number of filesystem storage space in
     *        bytes that should be consumed by secondary directories. This class
     *        may delete secondary directories prematurely in order to remain
     *        below this limit. Set this to 0 to disable limits.
     * @param cvsInvoker reference to an already-initialized CvsInvoker object
     *        that this object should use. (The same CvsInvoker object should be
     *        used by {@code PrimaryDirectoryAgent} also.)
     * @param lockAgent reference to an already-initialized LockAgent that this
     *        object should use.
     * @param siteManager reference to the {@code SiteManager}; used
     *        solely for logging.
     * @param repositoryManager a reference to the {@code RepositoryManager}
     */
    public SecondaryDirectoryAgent(String repositoryBaseUrl,
            File repositoryBaseDirectory, long gracePeriod, long fileSizeLimit,
            long maxTotalSizeUsed, CvsInvoker cvsInvoker, LockAgent lockAgent,
            SiteManager siteManager, RepositoryManager repositoryManager) {
        this.repositoryBaseUrl = repositoryBaseUrl;
        this.repositoryBaseDirectory = repositoryBaseDirectory;
        this.gracePeriod = gracePeriod;
        this.fileSizeLimit = fileSizeLimit;
        this.maxTotalSizeUsed = maxTotalSizeUsed;
        this.cvsInvoker = cvsInvoker;
        this.lockAgent = lockAgent;
        this.siteManager = siteManager;
        this.repositoryManager = repositoryManager;

        this.directories = new HashMap<Object, DirectoryRecord>();
        this.ticketsToDirectories
                = new HashMap<AbstractLock, DirectoryRecord>();
        this.totalSpaceUsed = 0;
        this.directoryCreationLock = new Object();

        lockAgent.registerListener(this);
        purgeAll();
    }

    /**
     * Performs one-time initialization of the secondary directory area within
     * the repository filesystem.
     * 
     * @param repositoryBaseDirectory the filesystem directory under which the
     *        local site's repository is accessible to local processes. For
     *        instance: '/var/recipnet/data/'.
     */
    public static void createArea(File repositoryBaseDirectory) {
        File secondaryDirectoryArea
                = SecondaryDirectory.getFileForWholeArea(
                        repositoryBaseDirectory);
        secondaryDirectoryArea.mkdir();
    }

    /**
     * <p>
     * The main entry point for this class: creates a secondary directory
     * (within the repository filesystem) and populates it with some or all
     * files available for a particular version of a particular sample by
     * extracting them from CVS. The caller should have queried the database
     * previously and obtained a {@code SecondaryDirectory} object that
     * describes the files available for extraction from CVS.
     * </p><p>
     * Not all files available for a sample/version are necessarily extracted
     * from CVS and placed in the secondary directory. Files available from the
     * sample/version's associated primary directory (as indicated by
     * {@code filesFromPrimaryDirectory}) are not extracted. Files
     * larger than a configurable limit are not extracted, unless the caller
     * specifies that limits should be ignored. Other secondary directories may
     * be deleted prematurely in order to keep the total space consumed by
     * secondary directories beneath the configured limit.
     * </p>
     * 
     * @return a {@code RepositoryFile} object (suitable for transmission
     *         to a client) that describes all files available for download for
     *         a particular version of a particular sample. Availability is
     *         indicated for each file, whether
     *         {@code FILE_IN_PRIMARY_DIRECTORY},
     *         {@code FILE_IN_SECONDARY_DIRECTORY}, or
     *         {@code FILE_AVAILABLE_UPON_REQUEST}.
     * @param secondaryDirectoryInfo identifies the sample/version for which
     *        files are being queried and describes the files that are available
     *        for extraction from CVS.
     *        {@code RepositoryManager.getSecondaryDirectoryInfo()} knows
     *        how to consult the database and return such information.
     * @param filesFromPrimaryDirectory the result from a prior call to
     *        {@code PrimaryDirectoryAgent.getRepositoryFiles()}. This
     *        is a convenience to callers who prefer to offer files for download
     *        from primary directories whenever possible. For any file/version
     *        that belongs to the specified sample/version that this object
     *        indicates would be available from the specified sample's
     *        associated primary directory, the returned availability is
     *        {@code FILE_IN_PRIMARY_DIRECTORY}. May be null if this
     *        feature is not desired, in which case all files will be served
     *        from the secondary directory.
     * @param shouldIgnoreFileSizeLimit if false (the default), files are not
     *        extracted from CVS to a secondary directory if they are larger
     *        than {@code fileSizeLimit} bytes (as configured at
     *        construction time). Such functionality might be useful in limiting
     *        DoS-attack potentional from unauthenticated users. If true, no
     *        individual file size limit is enforced.
     * @throws DeadlockDetectedException if deadlock is detected.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the secondary directory could
     *         not be created on the filesystem due to a permissions error of
     *         some sort.
     */
    public RepositoryFiles getRepositoryFiles(
            SecondaryDirectory secondaryDirectoryInfo,
            RepositoryFiles filesFromPrimaryDirectory,
            boolean shouldIgnoreFileSizeLimit)
            throws DeadlockDetectedException, OperationFailedException,
            ResourceNotAccessibleException {
        RepositoryFiles filesToReturn = new RepositoryFiles(
                secondaryDirectoryInfo.sampleId,
                secondaryDirectoryInfo.sampleHistoryId);

        /*
         * Create a new temporary filesystem directory (and DirectoryRecord) if
         * no secondary directory already exists for this sample/version.
         */
        DirectoryRecord record;
        synchronized (this.directoryCreationLock) {
            // synchronized to prevent the directory from being deleted while
            // we get the new directory
            record = createNewDirectory(secondaryDirectoryInfo);
            SecondaryDirectoryDummyTicket ticket
                    = new SecondaryDirectoryDummyTicket(
                            this.gracePeriod, secondaryDirectoryInfo);
            this.lockAgent.registerLock(ticket);
            this.lockAgent.acquireLock(ticket, true);

            /*
             * The code segment above just registered a "dummy" ticket on the
             * soon-to-be-populated secondary directory. This is necessary to
             * support callers who first call
             * RepositoryManager.getRepositoryFiles() and then later call
             * RepositoryManager.grantNewTicket() to obtain an
             * OutOfBandReadTicket. OutOfBandReadTickets are unique in that
             * they reference secondary directories created previously rather
             * than creating secondary directories themselves. Binding a dummy
             * ticket to the secondary directory at creation time establishes a
             * "grace period" during which the caller may obtain an
             * OutOfBandReadTicket without fear of the secondary directory being
             * purged in the meantime.  The dummy ticket is intentionally not
             * released; instead it will auto-expire in a fairly short time.
             */

            File existingFiles[] = record.directory.listFiles();

            // Iterate through each file that the SecondaryDirectory object
            // says belongs to this version of this sample.
            for (RepositoryFileInfo desiredFile
                    : secondaryDirectoryInfo.filesAvailable) {

                // Skip ahead if desiredFile can be served to the user from the
                // primary directory.
                if (filesFromPrimaryDirectory != null) {
                    RepositoryFiles.Record rec
                            = filesFromPrimaryDirectory.getRecordWithName(
                                    desiredFile.fileName);
                    if ((desiredFile.lastSampleHistoryId
                            == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                            && (rec != null)) {
                        filesToReturn.addFile(
                                new RepositoryFiles.Record(rec, desiredFile));
                        continue;
                    }
                }

                // Skip ahead if desiredFile already exists in the secondary
                // directory.
                File matchingExistingFile = null;
                for (int i = 0; i < existingFiles.length; i++) {
                    if (desiredFile.fileName.equals(existingFiles[i].getName())) {
                        matchingExistingFile = existingFiles[i];
                        break;
                    }
                }
                if (matchingExistingFile != null) {
                    filesToReturn.addFile(new RepositoryFiles.Record(
                            RepositoryFiles.Availability.SECONDARY_DIRECTORY,
                            matchingExistingFile,
                            secondaryDirectoryInfo.getUrl(this.repositoryBaseUrl),
                            desiredFile, filesToReturn));
                    continue;
                }

                // Skip ahead if the current availableFile is larger than the
                // limit (configurable), unless the shouldIgnoreFileSizeLimit
                // flag is set.
                if (!shouldIgnoreFileSizeLimit && (this.fileSizeLimit != 0)
                        && (desiredFile.fileBytes > this.fileSizeLimit)) {
                    filesToReturn.addFile(new RepositoryFiles.Record(
                            RepositoryFiles.Availability.ON_REQUEST,
                            desiredFile, null, filesToReturn));
                    continue;
                }

                // Extract desiredFile from CVS into the secondary directory.
                extractFile(desiredFile,
                        secondaryDirectoryInfo.getCvsTreeName(), record);
                filesToReturn.addFile(new RepositoryFiles.Record(
                        RepositoryFiles.Availability.SECONDARY_DIRECTORY,
                        desiredFile,
                        secondaryDirectoryInfo.getUrl(this.repositoryBaseUrl),
                        filesToReturn));
            }
            return filesToReturn;
        }
    }

    /**
     * Creates a secondary directory (within the repository filesystem) and
     * populates it with the single file/version specified by extracting it from
     * CVS. The caller should have queried the database previously and obtained
     * a {@code SecondaryDirectory} object that describes the files
     * available for extraction from CVS.
     * <p>
     * A typical caller would use the return value from this function to acquire
     * a lock (or ticket) before reading the file's contents.
     * <p>
     * Other secondary directories may be deleted prematurely in order to keep
     * the total space consumed by secondary directories beneath the configured
     * limit.
     * 
     * @return a {@code File} object that identifies the location of the
     *         requested file, after extraction from CVS.
     * @param secondaryDirectoryInfo identifies the sample/version for which a
     *        file is being requested and describes the files that are available
     *        for extraction from CVS.
     *        {@code RepositoryManager.getSecondaryDirectoryInfo()} knows
     *        how to consult the database and return such information.
     * @param repositoryFile a reference to one of the
     *        {@code RepositoryFileInfo} objects contained within
     *        {@code secondaryDirectoryInfo.files}. This reference the
     *        single file that should be extracted to the specified secondary
     *        directory.
     * @throws DeadlockDetectedException if deadlock is detected.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the secondary directory could
     *         not be created on the filesystem due to a permissions error of
     *         some sort.
     */
    public File getFile(SecondaryDirectory secondaryDirectoryInfo,
            RepositoryFileInfo repositoryFile)
            throws DeadlockDetectedException, OperationFailedException,
            ResourceNotAccessibleException {
        // Create a secondary directory if one doesn't exist already. Put a
        // dummy ticket on it.
        synchronized (this.directoryCreationLock) {
            DirectoryRecord record = createNewDirectory(secondaryDirectoryInfo);
            SecondaryDirectoryDummyTicket ticket
                    = new SecondaryDirectoryDummyTicket(
                            this.gracePeriod, secondaryDirectoryInfo);
            this.lockAgent.registerLock(ticket);
            this.lockAgent.acquireLock(ticket, true);
            // We will bind the ticket to the appropriate DirectoryRecord
            // later, when the lock agent calls notifyLockGranted().

            // Exit earliy if the desired file already exists within the
            // secondary directory.
            File extractedFile = new File(
                    secondaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                    repositoryFile.fileName);
            if (extractedFile.isFile() && extractedFile.canRead()) {
                return extractedFile;
            }

            // Extract the desired file from CVS into the secondary directory.
            extractFile(repositoryFile, secondaryDirectoryInfo.getCvsTreeName(),
                    record);

            return extractedFile;
        }
    }

    /**
     * Clears all files and directories in the filesystem within the secondary
     * directory area. This method is invoked by the constructor. For good
     * measure, the caller should invoke this method at shutdown time also.
     */
    public void purgeAll() {
        File files[] = SecondaryDirectory.getFileForWholeArea(
                repositoryBaseDirectory).listFiles();

        if (files != null) {
            for (File file : files) {
                try {
                    CvsInvoker.deleteFileRecursively(file);
                } catch (OperationFailedException ex) {
                    this.siteManager.recordLogRecord(
                            LogRecordGenerator.secondaryDirectoryDeletionException(
                                    file, ex));
                }
            }
        }
    }

    /**
     * From interface {@code LockAgent.LockListener}. Called by
     * {@code RepositoryTicketAgent} whenever a new ticket is registered
     * (opened). The current implementation uses this knowledge to track
     * activity on secondary directories and purge those that are no longer in
     * use.
     */
    public void notifyLockGranted(AbstractLock lock) {
        int sampleId = SampleInfo.INVALID_SAMPLE_ID;
        int sampleHistoryId = 0;

        if (lock instanceof SecondaryFileReadTicket) {
            SecondaryFileReadTicket ticket = (SecondaryFileReadTicket) lock;
            
            sampleId = ticket.getSecondaryDirectoryInfo().sampleId;
            sampleHistoryId = ticket.getSecondaryDirectoryInfo().sampleHistoryId;
        } else if (lock instanceof SecondaryDirectoryDummyTicket) {
            SecondaryDirectoryDummyTicket ticket
                    = (SecondaryDirectoryDummyTicket) lock;
            
            sampleId = ticket.getSecondaryDirectoryInfo().sampleId;
            sampleHistoryId = ticket.getSecondaryDirectoryInfo().sampleHistoryId;
        } else if (lock instanceof OutOfBandReadTicket) {
            OutOfBandReadTicket ticket = (OutOfBandReadTicket) lock;
            
            sampleId = ticket.getSampleId();
            sampleHistoryId = ticket.getSampleHistoryId();
        }

        // If the lock is a recognized type, update our state tables.
        if (sampleId != SampleInfo.INVALID_SAMPLE_ID) {
            synchronized (this.directories) {
                DirectoryRecord rec = this.directories.get(
                        DirectoryRecord.getKey(sampleId, sampleHistoryId));
                
                if (rec != null) {
                    rec.tickets.add(lock);
                    this.ticketsToDirectories.put(lock, rec);
                }
            }
        }
    }

    /**
     * From interface {@code LockAgent.LockListener}. Called by
     * {@code RepositoryTicketAgent} whenever a previously-issued is
     * closed or aborted. The current implementation uses this knowledge to
     * track activity on secondary directories and purge those that no longer
     * are in use.
     */
    public void notifyLockRevoked(AbstractLock lock) {
        if (!((lock instanceof SecondaryFileReadTicket)
                || (lock instanceof SecondaryDirectoryDummyTicket)
                || (lock instanceof OutOfBandReadTicket))) {
            return;
        }

        synchronized (this.directories) {
            DirectoryRecord rec = this.ticketsToDirectories.get(lock);
            if (rec == null) {
                // We never bothered to track this ticket while it was alive,
                // so just ignore its death.
                return;
            }

            this.ticketsToDirectories.remove(lock);
            rec.tickets.remove(lock);
            if (rec.tickets.isEmpty()) {
                try {
                    this.repositoryManager.passCoreMessage(
                            new UnusedSecondaryDirectoryHintCM(rec.getKey()));
                } catch (OperationFailedException ex) {
                    /*
                     * RepositoryManager was unable to accept our message for
                     * some reason. This could happen only in bootstrap mode.
                     * Silently drop the error because this CM was noncritical
                     * and we can't throw an exception from this method anyway.
                     */
                }
            }
        }
    }

    /**
     * Deletes a given directory if it is not being used.
     * 
     * @param directoryKey the key used to get the directory record from the map
     *        containing it
     */
    public void considerDeletingDirectory(Object directoryKey) {
        // synchronized to prevent concurrent execution with the critical
        // sections of getRepositoryFiles() and getFile()
        synchronized (this.directoryCreationLock) {
            synchronized (this.directories) {
                DirectoryRecord rec = this.directories.get(directoryKey);
                if (rec == null) {
                    // the directory was most likely already deleted by a
                    // previous hint or by clearSomeSpace(), fail silently
                    return;
                }
                if (rec.tickets.isEmpty()) {
                    // No more tickets are bound to this directory. Kill it and
                    // free up some disk space.
                    try {
                        deleteOldDirectory(rec, true);
                    } catch (OperationFailedException ex) {
                        this.siteManager.recordLogRecord(
                                LogRecordGenerator.secondaryDirectoryDeletionException(
                                    rec.directory, ex));
                    }
                }
            }
        }
    }

    /**
     * Internal function that creates a new (empty) secondary directory and
     * registers it with the {@code directories} structure. If a directory for
     * the same sample/version already exists, returns a reference to the
     * existing record instead.
     * 
     * @param dirInfo identifies the sample/version for which the directory is
     *        being created.
     * @return a {@code DirectoryRecord} representing the new secondary
     *         directory
     * @throws ResourceNotAccessibleException if the secondary directory could
     *         not be created on the filesystem due to a permissions error of
     *         some sort.
     */
    private DirectoryRecord createNewDirectory(SecondaryDirectory dirInfo)
            throws ResourceNotAccessibleException {
        synchronized (this.directoryCreationLock) {
            // we should already hold the directory creation lock before
            // entering this method
            synchronized (this.directories) {
                DirectoryRecord dirRecord = directories.get(
                        DirectoryRecord.getKey(
                                dirInfo.sampleId, dirInfo.sampleHistoryId));
                
                if (dirRecord == null) {
                    File dir = dirInfo.getFile(this.repositoryBaseDirectory);

                    if (!dir.mkdirs()) {
                        throw new ResourceNotAccessibleException(dir);
                    }

                    dirRecord = new DirectoryRecord(dirInfo.sampleId,
                            dirInfo.sampleHistoryId, dir);
                    this.directories.put(dirRecord.getKey(), dirRecord);
                }
                
                return dirRecord;
            }
        }
    }

    /**
     * Internal function that deletes an existing secondary directory and all
     * files within it. Also optionally delete the corresponding directory
     * record from the {@code directories} structure.
     * 
     * @param dirRecord identifies the secondary directory to be deleted.
     * @param deleteFromDirectoriesMap if true (the default), the specified
     *        {@code DirectoryRecord} is removed from the
     *        {@code directories} map. If false, the caller is
     *        responsible for removing the record himself.
     * @throws OperationFailedException if the secondary directory could
     *         not be deleted from the filesystem due to a permissions or other
     *         I/O error
     */
    private void deleteOldDirectory(DirectoryRecord dirRecord,
            boolean deleteFromDirectoriesMap)
            throws OperationFailedException {
        synchronized (this.directories) {
            if (deleteFromDirectoriesMap) {
                this.directories.remove(dirRecord.getKey());
            }
            this.totalSpaceUsed -= dirRecord.bytesUsed;
            CvsInvoker.deleteFileRecursively(dirRecord.directory);
        }
    }

    /**
     * Internal function that extracts a specified file from CVS to an existing
     * secondary directory and updates all state variables.
     * 
     * @param fileToExtract identifies the particular file/version that should
     *        be extracted.
     * @param cvsTreeName the CVS tree name for the requested sample/version, as
     *        returned by {@code SecondaryDirectory.getCvsTreeName()}.
     * @param destinationDirectory identifies the existing secondary directory
     *        to which the file should be extracted.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the secondary directory could
     *         not be created on the filesystem due to a permissions error of
     *         some sort.
     */
    private void extractFile(RepositoryFileInfo fileToExtract,
            String cvsTreeName, DirectoryRecord destinationDirectory)
            throws OperationFailedException, ResourceNotAccessibleException {
        // Remove old secondary directories as needed to remain within the
        // configured filesystem space limit.
        if (((this.totalSpaceUsed + fileToExtract.fileBytes)
                > this.maxTotalSizeUsed) && (this.maxTotalSizeUsed != 0)) {
            clearSomeSpace(this.totalSpaceUsed + fileToExtract.fileBytes
                    - this.maxTotalSizeUsed);
        }
        cvsInvoker.exportFile(cvsTreeName, fileToExtract.fileName,
                fileToExtract.cvsRevision, destinationDirectory.directory);
        synchronized (this.directories) {
            destinationDirectory.notifyFileExtracted(fileToExtract);
            this.totalSpaceUsed += fileToExtract.fileBytes;
        }
    }

    /**
     * Helper function that deletes zero or more existing secondary directories
     * (and all files within) until (at least) a specified number of bytes have
     * been cleared from the filesystem. Secondary directories are deleted as a
     * whole; no secondary directories have some files deleted and others left
     * intact. The current implementation selects secondary directories for
     * deletion via a simple least-recently-used scheme.
     * 
     * @param spaceRequested the number of bytes in the filesystem that should
     *        be deleted.
     */
    private void clearSomeSpace(long spaceRequested) {
        // we don't want to delete any directories while they are being made
        synchronized (this.directoryCreationLock) {
            List<DirectoryRecord> directoriesToRemove;
            
            synchronized (this.directories) {
                
                /*
                 * Generate a list of repository directories, in order of most
                 * desirable to be deleted. The current implementation selects
                 * unused directories to be first, then orders by last
                 * extraction time, oldest first.
                 */
                directoriesToRemove = new ArrayList<DirectoryRecord>(
                        this.directories.values());
                Collections.sort(directoriesToRemove,
                        new Comparator<DirectoryRecord>() {
                    public int compare(DirectoryRecord x, DirectoryRecord y) {
                        if (x.tickets.isEmpty() == y.tickets.isEmpty()) {
                            return (int) (x.lastExtractionTime
                                    - y.lastExtractionTime);
                        } else {
                            return x.tickets.isEmpty() ? -1 : 1;
                        }
                    }
                });

                /*
                 * Iterate through the list and start removing directories until
                 * the requested amount of filesystem space has been reclaimed,
                 * or our directories have been exhausted.
                 */
                long targetTotalSpaceUsed
                        = this.totalSpaceUsed - spaceRequested;
                
                for (DirectoryRecord dirRecord : directoriesToRemove) {
                    if (this.totalSpaceUsed <= targetTotalSpaceUsed) {
                        break;
                    }
                    try {
                        deleteOldDirectory(dirRecord, true);
                    } catch (OperationFailedException ex) {
                        this.siteManager.recordLogRecord(
                                LogRecordGenerator.secondaryDirectoryDeletionException(
                                        dirRecord.directory, ex));
                    }
                }
            }
        }
    }

    /**
     * A nested class that keeps track of secondary directories -- exactly one
     * record for each sample/version that has a secondary directory on the
     * filesystem.
     */
    private static class DirectoryRecord {

        /**
         * A {@code File} representing the filesystem directory tracked by this
         * {@code DirectoryRecord}
         */
        public final File directory;

        /**
         * The ID of the sample to which the directory and its contents are
         * attributed
         */
        public final int sampleId;

        /**
         * The sample history ID of the version of the sample to which the
         * directory and its contents are attributed
         */
        public final int sampleHistoryId;

        /**
         * A collection of locks on this directory and its contents
         */
        public final Collection<AbstractLock> tickets;

        /**
         * The time that the directory represented by this
         * {@code DirectoryRecord} was created, as milliseconds since the epoch
         */
        public final long creationTime;

        /**
         * The time of the last extraction of a file into the directory
         * represented by this {@code DirectoryRecord}, as milliseconds since
         * the epoch
         */
        public long lastExtractionTime;

        /**
         * The aggregate size of all the files currently present in the
         * directory represented by this {@code DirectoryRecord}
         */
        public long bytesUsed;

        /**
         * An object recommended for use as a {@code Map} key to this
         * {@code DirectoryRecord}
         */
        private final Object key;

        /**
         * Initializes a {@code DirectoryRecord} with the specified parameters
         * 
         * @param sampleId The ID of the sample to which the directory and its
         *        contents are attributed
         * @param sampleHistoryId The sample history ID of the version of the
         *        sample to which the directory and its contents are attributed
         * @param directory a {@code File} representing the filesystem directory
         *        tracked by this {@code DirectoryRecord}
         */
        public DirectoryRecord(int sampleId, int sampleHistoryId,
                File directory) {
            this.directory = directory;
            this.sampleId = sampleId;
            this.sampleHistoryId = sampleHistoryId;
            this.tickets = new ArrayList<AbstractLock>();
            this.creationTime = System.currentTimeMillis();
            this.lastExtractionTime = 0;
            this.bytesUsed = 0;
            this.key = getKey(this.sampleId, this.sampleHistoryId);
        }

        /**
         * Notifies this {@code DirectoryRecord} that a file has been extracted
         * into the directory it represents, so that this record can update its
         * internal statistics
         * 
         * @param file a {@code RepositoryFileInfo} representing the file that
         *        was extracted
         */
        public void notifyFileExtracted(RepositoryFileInfo file) {
            this.bytesUsed += file.fileBytes;
            this.lastExtractionTime = System.currentTimeMillis();
        }

        /**
         * Returns an object recommended for use as a {@code Map} key to this
         * {@code DirectoryRecord}. All objects returned by this method by a
         * particular {@code DirectoryRecord} instance will be {@code equal},
         * and will be equal to the object obtained by passing this record's
         * sample and sample history IDs to the static {@link #getKey(int, int)}
         * method.
         * 
         * @return a {@code Object} recommended for use as a {@code Map} key to
         *         this {@code DirectoryRecord}
         */
        public Object getKey() {
            return key;
        }

        /**
         * Chooses an appropriate key for a {@code DirectoryRecord} associated
         * with the specified sample and sample history IDs, based only on those
         * IDs.
         * 
         * @param sampleId the sample ID to use in choosing a key
         * @param sampleHistoryId the sample history ID to use in choosing a key
         * 
         * @return the chosen key {@code Object} 
         */
        public static Object getKey(int sampleId, int sampleHistoryId) {
            return String.valueOf(sampleId) + "/"
                    + String.valueOf(sampleHistoryId);
        }
    }
}
