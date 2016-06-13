/*
 * Reciprocal Net project
 * 
 * PrimaryDirectoryAgent.java
 *
 * 29-May-2003: ekoperda wrote first draft
 * 03-Jul-2003: ekoperda fixed bug #963 in promoteTempFile()
 * 10-Jul-2003: midurbin added getLabDirectorySize() and member variable
 *              labDirSizeScriptFilespec.
 * 08-Aug-2003: midurbin added revertFiles()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package 
 *              to org.recipnet.site.core.agent; change package references due
 *              to source tree reorganization
 * 05-Jan-2005: midurbin updated revertFiles() to preserve
 *              originalSampleHistoryId for files (re)created in a reversion
 * 21-Oct-2005: midurbin added support for file descriptions
 * 26-May-2006: jobollin reformatted the source and implemented generics
 * 16-Jun-2006: jobollin added enhanced-for loops in some places
 * 19-Jun-2006: jobollin updated getLabDirectorySize() to use internal Java
 *              mechanisms instead of an external script; updated
 *              scanDirectory() to detect missing and inaccessible directories
 *              and handle them more appropriately
 * 06-Nov-2006: jobollin modified source comments
 * 07-Jan-2008: ekoperda added filename trim()-ing throughout to fix bug #1870
 */

package org.recipnet.site.core.agent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ProcessAbnormalExitException;
import org.recipnet.site.core.ProcessIncompleteException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.util.CoreProcessWrapper;
import org.recipnet.site.core.util.CvsInvoker;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.PrimaryDirectory;
import org.recipnet.site.core.util.SecondaryDirectory;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;

/**
 * <p>
 * An agent owned by RepositoryManager. A "primary directory" is a directory on
 * the filesystem within the repository area which contains zero or more data
 * files associated with the current version of a particular sample (for which
 * the local site is authoritative). The association between a
 * locally-authoritative sample and its primary directory is established by a
 * holding record (i.e. a {@code RepositoryHoldingInfo} object). Individual
 * primary directories manged by this object are identified by
 * {@code PrimaryDirectory} container objects.
 * </p><p>
 * The data files contained within a primary directory are always considered to
 * be the most current set of data files available for the associated sample.
 * It is assumed that users are free to "muck with" the files inside primary
 * directories at will, via means external to the application. Such changes
 * must be detected and echoed in the local site's database and CVS repository
 * for consistency's sake. To this end, the caller must periodically scan the
 * contents of each registered primary directory and update database state as
 * appropriate; the {@code scanDirectory()} method assists with this.
 * </p><p>
 * Sample data files are versioned with the help of the CVS program. As new
 * files are added to a primary directory, existing files are overwritten, and
 * old files are removed, CVS is notified of the change and stores a detailed
 * record of the change in the CVS repository. {@code SecondaryDirectoryAgent}
 * may access the same CVS repository subsequently as it recreates the set of
 * data files associated with a particular sample/version.
 * </p><p>
 * Note that in every case when changes are made to the files in a primary
 * directory, or prior changes have been detected, the caller is responsible
 * for persisting appropriate knowledge in the local site's database. Typically
 * this knowledge is exchange in the form of one or several
 * {@code RepositoryFileInfo} objects.
 * </p>
 * <p>
 * By convention, file names stored in the database and the associated 
 * container classes are trim()-ed of any leading and trailing spaces.  Care is
 * taken whenever converting from database-style file names to filesystem-style
 * filenames to ensure that the proper sort of string comparison occurs.
 */
public class PrimaryDirectoryAgent {

    /** Config option set at construction time */
    private final String repositoryBaseUrl;

    /** Config option set at construction time */
    private final File repositoryBaseDirectory;

    /** Config option set at construction time */
    private final String createDirScriptFilespec;

    private final String uploadedFilePrefix;

    /** Reference to a CvsInvoker object provided at construction time */
    private final CvsInvoker cvsInvoker;

    /** Reference to a SiteManager object provided at construction time */
    private final SiteManager siteManager;
    
    /** The number of bytes in one kilobyte */
    private final static long KB = 1024L;

    /**
     * The only constructor.
     * 
     * @param repositoryBaseUrl the URL fragment under which the local site's
     *        repository is accessible to web browsers. The should match the web
     *        server's configuration; an appropriate value is probably stored in
     *        the 'repositoryUrl' field of the db table 'sites'. For instance:
     *        'http://www.server.edu/recipnet/data/'. Should end with a trailing
     *        slash.
     * @param repositoryBaseDirectory the filesystem directory under which the
     *        local site's repository is accessible to local processes. For
     *        instance: '/var/recipnet/data/'. Should end with a trailing slash.
     * @param createDirScriptFilespec the filesystem location of the program
     *        that this class should employ to create new primary directories.
     *        For instance: '/usr/bin/recipnet-createreposdir'. This program is
     *        invoked on the command line.
     * @param labDirSizeScriptFilespec the filesystem location of the program
     *        that this class should employ to determine the size of a
     *        particular lab directory.
     * @param uploadedFilePrefix a string that should be prepended to the name
     *        of each temporary file created during this object's operation.
     * @param cvsInvoker reference to an already-initialized CvsInvoker object
     *        that this object should use. (The same CvsInvoker object should be
     *        used by {@code SecondaryDirectoryAgent} also.)
     * @param siteManager reference to an already-initialized SiteManager object
     *        that this object should use. Only one method,
     *        {@code SiteManager.recordLogRecord()}, is called.
     */
    public PrimaryDirectoryAgent(String repositoryBaseUrl,
            File repositoryBaseDirectory, String createDirScriptFilespec,
            String labDirSizeScriptFilespec, String uploadedFilePrefix,
            CvsInvoker cvsInvoker, SiteManager siteManager) {
        /*
         * Note: parameter labDirSizeScriptFilespec is no longer used, and will
         * be removed in some future version
         */
        
        this.repositoryBaseUrl = repositoryBaseUrl;
        this.repositoryBaseDirectory = repositoryBaseDirectory;
        this.createDirScriptFilespec = createDirScriptFilespec;
        this.uploadedFilePrefix = uploadedFilePrefix;
        this.cvsInvoker = cvsInvoker;
        this.siteManager = siteManager;
    }

    /**
     * Creates a primary directory for the specified sample at the specified
     * location within the repository. If the directory already existed on the
     * filesystem then a record of each preexisting file is returned.
     * 
     * @param primDir identifies the primary directory to be created.
     * @return a {@code Collection} of zero or more incomplete
     *         {@code RepositoryFileInfo} objects that describe the set of files
     *         present in the directory at the time this function was invoked. A
     *         typical caller would persist this knowledge in the database for
     *         future use by {@code SecondaryDirectoryAgent}.
     * @throws InvalidDataException with a reason code of
     *         {@code ILLEGAL_EXTENSIONPATH} if the value
     *         {@code primDir.holdingExtensionPath} would cause the primary
     *         directory to be created in an illegal filesystem location, one
     *         not beneath the associated lab's base directory.
     * @throws OperationFailedException on low-level error.
     * @throws ProcessAbnormalExitException if CVS terminated abnormally.
     * @throws ProcessIncompleteException if CVS was interrupted unexpectedly.
     */
    public Collection<RepositoryFileInfo> createDirectory(
            PrimaryDirectory primDir) throws InvalidDataException,
            OperationFailedException, ProcessAbnormalExitException,
            ProcessIncompleteException {
        try {
            /*
             * Ensure that the filesystem directory about to be created lies
             * underneath the lab's base directory.
             */
            /*
             * TODO: The approach below seems to work, but is not guaranteed to
             * do, for the canonical form of a path name may be different if
             * the file it represents exists than it is if the file doesn't
             * exist.  (See File.getCanonicalPath() docs.)
             */
            if (!primDir.getFile(this.repositoryBaseDirectory
                    ).getCanonicalPath().startsWith(
                    primDir.getFileForLabDirectory(this.repositoryBaseDirectory
                            ).getCanonicalPath())) {
                // TODO: create a log message here
                throw new InvalidDataException(primDir,
                        InvalidDataException.ILLEGAL_EXTENSIONPATH);
            }

            /*
             * Create the directory in the filesystem if it doesn't already
             * exist. We call out to the shell script 'recipnet-createreposdir'
             * and let it do all the dirty work for us.
             */
            CoreProcessWrapper proc = new CoreProcessWrapper(new String[] {
                    createDirScriptFilespec,
                    primDir.getFile(this.repositoryBaseDirectory).getPath() });
            siteManager.recordLogRecord(proc.getLogRecord());
            proc.waitFor(true);

            // Tell CVS about the new primary directory.
            this.cvsInvoker.registerDirectory(primDir.getCvsTreeName(),
                    primDir.getFile(this.repositoryBaseDirectory));

            /*
             * Register with CVS all existing files inside the new primary
             * directory.
             */
            File existingFiles[]
                    = primDir.getFile(this.repositoryBaseDirectory).listFiles();
            
            if (existingFiles == null) {
                throw new OperationFailedException(
                        "Could not list files for laboratory "
                        + primDir.labDirectoryName + ", holdings "
                        + primDir.holdingExtensionPath);
            }
            
            Collection<RepositoryFileInfo> filesForCaller
                    = new ArrayList<RepositoryFileInfo>();

            for (File existingFile : existingFiles) {
                if (existingFile.isFile() && !existingFile.isHidden()) {
                    filesForCaller.add(new RepositoryFileInfo(primDir.sampleId,
			    existingFile.getName().trim(),
                            this.cvsInvoker.commitAddedFile(existingFile),
                            existingFile.length(), null));
                }
            }
            
            return filesForCaller;
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Detects the files currently present within a specifed primary directory.
     * 
     * @param primDir identifies the primary directory to be queried.
     * @param holdingRecordAvailable should be true if the caller was able to
     *        locate a holding record for the sample and supplied it as part of
     *        {@code primDir}. If false, a
     *        {@code RepositoryDirectoryNotFoundException} is thrown.
     * @param sampleHistoryId for information only; this value is included in
     *        the {@code sampleHistoryId} field of the returned
     *        {@code RepositoryFiles} object.
     * @return a {@code RepositoryFiles} object that describes the set of data
     *         files currently available to be served from the primary
     *         directory. Each file described has an availability of
     *         {@code FILE_IN_PRIMARY_DIRECTORY} and a valid URL.
     * @throws RepositoryDirectoryNotFoundException if
     *         {@code holdingRecordAvailable} is false, or no primary repository
     *         directory existing on the filesystem in the expected location. In
     *         either case, the exception object contains a suggested filesystem
     *         location for presentation to the user.
     */
    public RepositoryFiles getRepositoryFiles(PrimaryDirectory primDir,
            boolean holdingRecordAvailable, int sampleHistoryId)
            throws RepositoryDirectoryNotFoundException {
        /*
         * Detect whether the specified primary directory (still) exists on the
         * filesystem.
         */
        File dir = primDir.getFile(this.repositoryBaseDirectory);
        
        if (!dir.canRead() || !dir.isDirectory()) {
            /*
             * The filesystem directory does not exist. Suggest a nice location
             * to the caller.
             */
            throw new RepositoryDirectoryNotFoundException(new RepositoryFiles(
                    primDir.sampleId, sampleHistoryId), holdingRecordAvailable,
                    false, primDir.getFileForLabDirectory(
                            this.repositoryBaseDirectory).getPath(), null,
                    primDir.sampleLocalLabId);
        }
        if (!holdingRecordAvailable) {
            /*
             * The directory does exist, but the caller was unable to find a
             * holding record, so return a special error code to the caller.
             */
            throw new RepositoryDirectoryNotFoundException(new RepositoryFiles(
                    primDir.sampleId, sampleHistoryId), holdingRecordAvailable,
                    true, primDir.getFileForLabDirectory(
                            this.repositoryBaseDirectory).getPath(), null,
                    primDir.sampleLocalLabId);
        }

        return new RepositoryFiles(primDir.sampleId, sampleHistoryId,
                primDir.getUrl(this.repositoryBaseUrl), dir.listFiles());
    }

    /**
     * Returns a reference to the specified file within the specified primary
     * repository directory. This is a low-cost call: no check is made to ensure
     * the requested primary directory or file exists or is accessible.
     * 
     * @param primaryDirectoryInfo identifies the primary directory in which the
     *        specified file resides.
     * @param fileName identifies the file within the primary directory for
     *        which a reference should be returned.
     * @return a {@code File} object that identifies the location of the
     *         specified file, within the specified primary repository
     *         directory.
     */
    public File getFile(PrimaryDirectory primaryDirectoryInfo, String fileName) {
        return new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                fileName);
    }

    /**
     * Obtains a reference to a uniquely-named file within the specified primary
     * repository directory that may be used as a temporary file during a write
     * operation. The temporary file's name begins with
     * {@code uploadedFilePrefix} value that was specified at construction time.
     * 
     * @param primaryDirectoryInfo identifies the primary directory in which the
     *        temporary file should be created.
     * @return a {@code File} object that identifies the location of a unique
     *         temporary file that the caller may create and write to.
     * @throws OperationFailedException on low-level error.
     */
    public File createTempFile(PrimaryDirectory primaryDirectoryInfo)
            throws OperationFailedException {
        try {
            return File.createTempFile(this.uploadedFilePrefix, null,
                    primaryDirectoryInfo.getFile(this.repositoryBaseDirectory));
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * For an existing temporary file within a primary repository directory,
     * "promotes" that file to sample data file status by registering it with
     * the CVS repository and returning information that the caller should
     * perist in the database. If the target file (identified by
     * {@code fileName}) already exists, it is overwritten.
     * 
     * @param primaryDirectoryInfo identifies the primary directory in which the
     *        existing temporary file resides.
     * @param fileName the desired name for the sample data file to be created
     *        as a result of promotion.
     * @param tempFile the existing temporary file to be promoted, presumably
     *        lying within the specified primary repository directory. This
     *        reference should have been obtained from a prior call to
     *        {@code createTempFile()} for the same primary repository
     *        directory.
     * @param description a [@code String} containing the description for the
     *        newly-promoted repository file; used only in initializing the
     *        {@code RepositoryFileInfo} to be returned
     * @return an incomplete {@code RepositoryFileInfo} object that describes
     *         the newly-created sample data file. A typical caller would
     *         persist this knowledge in the database for future use by
     *         {@code SecondaryDirectoryAgent}.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if file promotion failed due to a
     *         permissions problem.
     */
    public RepositoryFileInfo promoteTempFile(
            PrimaryDirectory primaryDirectoryInfo, String fileName,
            File tempFile, String description) throws OperationFailedException,
            ResourceNotAccessibleException {
        File realFile = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                fileName);
        boolean realFileExisted = realFile.exists();

        // Manipulate files on the filesystem.
        if (realFileExisted) {
            if (!realFile.delete()) {
                // Couldn't delete the existing real file for some reason.
                throw new ResourceNotAccessibleException(realFile);
            }
        }
        if (!tempFile.renameTo(realFile)) {
            // Couldn't move the file for some reason.
            throw new ResourceNotAccessibleException(realFile);
        }

        // Inform CVS about the new/updated file.
        try {
            String cvsRevision = realFileExisted
                    ? this.cvsInvoker.commitModifiedFile(realFile)
                    : this.cvsInvoker.commitAddedFile(realFile);
                    
            if (cvsRevision == null) {
                /*
                 * The file wasn't committed, probably because its contents
                 * haven't changed. Obtain the existing CVS revision number for
                 * the file.
                 */
                cvsRevision = this.cvsInvoker.getFileRevision(realFile);
            }

            return new RepositoryFileInfo(primaryDirectoryInfo.sampleId,
                    fileName, cvsRevision, realFile.length(), description);
        } catch (ProcessAbnormalExitException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Eradicates an existing sample data file from its primary repository
     * directory by removing the file from the filesystem, and removing all
     * traces of the file from the CVS repository. Because such an operation
     * defeats the file-versioning mechanism, its use should be restricted to
     * special cases. Callers are encouraged to call
     * {@code getFileAggregateSize()} and obtain a two-step, informed
     * confirmation from the user before invoking this method.
     * 
     * @param primaryDirectoryInfo identifies the primary repository directory
     *        within which the file to be eradicated lies.
     * @param fileName identifies the file within the specified primary
     *        repository directory that should be eradicated.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if a permissions error was
     *         encountered while deleting files.
     */
    public void eradicateFile(PrimaryDirectory primaryDirectoryInfo,
            String fileName) throws OperationFailedException,
            ResourceNotAccessibleException {
        File fileToRemove = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                fileName);
        
        if (!fileToRemove.exists()) {
            throw new ResourceNotFoundException(fileToRemove);
        } else {
            this.cvsInvoker.eradicateFile(primaryDirectoryInfo.getCvsTreeName(),
                    primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                    fileName);
        }
    }

    /**
     * Calculates the total number of bytes presently used to store all versions
     * of a specified sample data file. The total includes storage within the
     * CVS repository and storage of the most recent version within the primary
     * repository directory. This number is for informational purposes only, and
     * often is invoked in preparation for a call to {@code eradicateFile()}.
     * 
     * @param primaryDirectoryInfo identifies the primary repository directory
     *        within which the file whose size should be calculated resides.
     * @param fileName identifies the file within the specified primary
     *        repository directory whose size should be calculated.
     * @return the total number of bytes
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotFoundException with a nested {@code File} object if
     *         the specified file could not be found within the specified
     *         primary repository directory.
     */
    public long getFileAggregateSize(PrimaryDirectory primaryDirectoryInfo,
            String fileName) throws OperationFailedException,
            ResourceNotFoundException {
        File file = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                fileName);
        
        if (!file.exists()) {
            throw new ResourceNotFoundException(file);
        } else {
            return this.cvsInvoker.getVersionedFileSize(
                    primaryDirectoryInfo.getCvsTreeName(), fileName)
                    + file.length();
        }
    }

    /**
     * Removes an existing sample data file from its primary repository
     * directory and records the removal in the CVS repository. A typical caller
     * would subsequently alter the corresponding row in the 'repositoryFiles'
     * database table to record the removal.
     * 
     * @param primaryDirectoryInfo identifies the primary repository directory
     *        within which the file to be removed lies.
     * @param fileName identifies the file within the specified primary
     *        repository directory that should be removed.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the file deletion failed,
     *         presumably due to a permissions problem.
     * @throws ResourceNotFoundException if the specified file could not be
     *         found on the filesystem.
     */
    public void removeFile(PrimaryDirectory primaryDirectoryInfo,
            String fileName) throws OperationFailedException,
            ResourceNotAccessibleException, ResourceNotFoundException {
        File fileToRemove = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                fileName);
        
        if (!fileToRemove.exists()) {
            throw new ResourceNotFoundException(fileToRemove);
        } else if (!fileToRemove.delete()) {
            throw new ResourceNotAccessibleException(fileToRemove);
        } else {
            this.cvsInvoker.commitRemovedFile(fileToRemove);
        }
    }

    /**
     * Renames an existing sample data file within a primary repository
     * directory. The caller is responsible for recording in the database the
     * removal of the old file and the addition of the new file.
     * 
     * @param primaryDirectoryInfo identifies the primary directory in which the
     *        existing file to be renamed resides.
     * @param oldFileInfo identifies the existing file within the primary
     *        repository directory that is to be renamed.
     * @param newFileName the desired name for the sample data file.
     * @return an incomplete {@code RepositoryFileInfo} object that describes
     *         the newly-renamed sample data file, as though the file had just
     *         been added to the primary repository directory. A typical caller
     *         would persist this knowledge in the database for future use by
     *         {@code SecondaryDirectoryAgent}.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if the rename operation failed due
     *         to a permissions problem.
     * @throws ResourceNotFoundException with a nested {@code File} object if
     *         the file to be renamed (identified by {@code oldFileName} could
     *         not be found on the filesystem.
     */
    public RepositoryFileInfo renameFile(PrimaryDirectory primaryDirectoryInfo,
            RepositoryFileInfo oldFileInfo, String newFileName)
            throws OperationFailedException, ResourceNotAccessibleException,
            ResourceNotFoundException {
        File oldFile = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                oldFileInfo.fileName);
        File newFile = new File(
                primaryDirectoryInfo.getFile(this.repositoryBaseDirectory),
                newFileName);
        
        if (!oldFile.exists()) {
            throw new ResourceNotFoundException(oldFile);
        } else if (newFile.exists()) {
            throw new ResourceNotAccessibleException(newFile);
        } else if (!oldFile.renameTo(newFile)) {
            throw new ResourceNotAccessibleException(oldFile);
        } else {
            this.cvsInvoker.commitRemovedFile(oldFile);
            return new RepositoryFileInfo(primaryDirectoryInfo.sampleId,
                    newFileName, this.cvsInvoker.commitAddedFile(newFile),
                    newFile.length(), oldFileInfo.description);
        }
    }

    /**
     * Scans a particular primary repository directory and detects any
     * differences since the last scan. New files, modified files, and removed
     * files all are detected by comparing actual files on the filesystem
     * against records stored in CVS (by previous calls to this class). For
     * each detected difference, CVS is notified of the new/modified/removed
     * file and appropriate entries are added to {@code changesToAdd} and/or
     * {@code changesToDeactivate}. The caller is responsible for recording the
     * scan's results in the database.
     * 
     * @param dirToScan the {@code PrimaryDirectory} object that identifies the
     *        directory in the repository filesystem that should be scanned.
     * @param dirAsExpected the {@code SecondaryDirectory} object that
     *        describes the directory's expected (or previously known) state.
     *        Differences between this expected state and the actual condition
     *        of the repository filesystem are reported to the caller.
     * @param filesToAdd a caller-provided {@code Collection}; should support
     *        additions and be empty. When this method returns, the collection
     *        contains zero or more references to new (and incomplete)
     *        {@code RepositoryFileInfo} objects, one for each new or modified
     *        file detected in the repository filesystem.
     * @param filesToDeactivate a caller-provided {@code Collection}; should
     *        support additions and be empty. When this method returns, the
     *        collection contains zero or more references to
     *        {@code RepositoryFileInfo} objects, where the objects referenced
     *        were passed to the method in the
     *        {@code dirAsExpected.filesAvailable} field. One object reference
     *        is inserted into this collection for each expected file that is
     *        no longer present in (has been removed from) the repository
     *        filesystem.
     * @throws OperationFailedException if the contents of the directory
     *         described by {@code dirToScan} cannot be listed because of some
     *         filesystem-related error (e.g. the directory doesn't exist,
     *         isn't a directory, or is not readable because of
     *         filesystem-level permissions)
     * @throws IllegalArgumentException if the sample referenced by
     *         {@code dirToScan} and {@code dirAsExpected} do not agree.
     */
    public void scanDirectory(PrimaryDirectory dirToScan,
            SecondaryDirectory dirAsExpected,
            Collection<RepositoryFileInfo> filesToAdd,
            Collection<RepositoryFileInfo> filesToDeactivate)
            throws OperationFailedException {
        // Sanity check.
        if (dirToScan.sampleId != dirAsExpected.sampleId) {
            throw new IllegalArgumentException();
        }

        // Detected all newly-added files.
        File actualFiles[]
                = dirToScan.getFile(this.repositoryBaseDirectory).listFiles();
        
        if (actualFiles == null) {
            throw new OperationFailedException(
                    "Could not list files for lab "
                     + dirToScan.labDirectoryName
                    + ", holdings path " + dirToScan.holdingExtensionPath);
        }
        
        for (File actualFile : actualFiles) {
            if (!actualFile.isHidden() && actualFile.isFile()
                    && !dirAsExpected.containsFile(actualFile)) {
                /*
                 * Detected a file that Repository Manager doesn't know about.
                 * Commit the new file to CVS and generate a new
                 * RepositoryFileInfo record to inform the caller about what
                 * happened.
                 */
                try {
                    filesToAdd.add(new RepositoryFileInfo(dirToScan.sampleId,
			    actualFile.getName().trim(),
                            this.cvsInvoker.commitAddedFile(actualFile),
                            actualFile.length(), null));
                } catch (OperationFailedException ex) {
                    this.siteManager.recordLogRecord(
                            LogRecordGenerator.primaryDirectoryCommitException(
                                    actualFile, ex));
                }
            }
        }

        // Detect all modified and newly-removed files.
        for (RepositoryFileInfo expectedFile : dirAsExpected.filesAvailable) {
            File matchingActualFile = null;
            File missingFile = null;
            
            for (File actualFile : actualFiles) {
                if (expectedFile.fileName.equals(
                        actualFile.getName().trim())) {
                    matchingActualFile = actualFile;
                    break;
                }
            }
            
            try {
                if (matchingActualFile == null) {
                    /*
                     * Detected a missing file that CVS used to know about (per
                     * our records). Tell CVS to remove the file and commit the
                     * removal. Instruct the caller to deactivate the old
                     * RepositoryFileInfo record.
                     */
                    missingFile = new File(
                            dirToScan.getFile(this.repositoryBaseDirectory),
                            expectedFile.fileName);
                    this.cvsInvoker.commitRemovedFile(missingFile);
                    filesToDeactivate.add(expectedFile);
                } else if (this.cvsInvoker.checkFileModified(
                        matchingActualFile)) {
                    /*
                     * Detected an existing file that CVS says has been
                     * modified. Commit the new version to CVS. Instruct the
                     * caller to deactivate the old RepositoryChangeInfo record
                     * and create a new one in its place.
                     */
                    filesToAdd.add(new RepositoryFileInfo(
                            dirToScan.sampleId,
                            matchingActualFile.getName().trim(),
                            this.cvsInvoker.commitModifiedFile(
                                    matchingActualFile),
                            matchingActualFile.length(),
                            expectedFile.description));
                    filesToDeactivate.add(expectedFile);
                }
            } catch (OperationFailedException ex) {
                this.siteManager.recordLogRecord(
                        LogRecordGenerator.primaryDirectoryCommitException(
                                ((matchingActualFile != null)
                                        ? matchingActualFile
                                        : missingFile),
                                ex));
            }
        }
    }

    /**
     * Determines the size of all of the contents of the repository directory
     * for a given lab.
     * 
     * @param labPath path name of for the lab whose directory size is to be
     *        returned, relative to the configured repository base directory
     * @return the aggregate size of the contents of the specified directory and
     *         its descendants, in kilobytes (units of 1024 bytes), as a
     *         {@code long}.  Fractions of a kilobyte are rounded up.
     * @throws OperationFailedException if the directory size computation fails
     */
    public long getLabDirectorySize(String labPath)
            throws OperationFailedException {
        return (computeDirSize(
                new File(repositoryBaseDirectory, labPath)) + (KB - 1)) / KB;
    }

    /**
     * Computes the combined size of all files in the directory subtree rooted
     * at the location designated by the specified {@code File}
     * 
     * @param directory a {@code File} representing the directory whose
     *        aggregate size is requested. It is advisable that this
     *        {@code File} be absolute; if not, then it is resolved to an
     *        absolute path name as described in the documentation for the
     *        {@code File} class.
     * @return the aggregate size of the contents of the specified directory and
     *         its descendants, in bytes, as a {@code long}
     * @throws OperationFailedException if the directory size computation fails
     */
    private long computeDirSize(File directory)
            throws OperationFailedException {
        long totalSize = 0;
        
        try {
            File[] fileList;

            if (!directory.isDirectory()) {
                throw new OperationFailedException("Not a directory: "
                        + directory.getAbsolutePath());
            }

            fileList = directory.listFiles();

            if (fileList == null) {
                throw new OperationFailedException(
                        "Could not list contents of directory "
                                + directory.getAbsolutePath());
            }

            for (File file : fileList) {
                if (file.isFile()) {
                    totalSize += file.length();
                } else if (file.isDirectory()) {
                    totalSize += computeDirSize(file);
                }
                // if neither, then ignore the file
            }

        } catch (SecurityException se) {
            throw new OperationFailedException(
                    "Could not compute directory size", se);
        }
        
        return totalSize;
    }
            
    /**
     * <p>
     * Replaces the contents of a specified primary directory with those that
     * belong to a particular previous revision of the sample. The caller is
     * expected to specify the files and desired "target" revision of those
     * files that are to populate the primary directory, possibly by querying an
     * existing {@code SecondaryDirectory} object.
     * </p><p>
     * This method makes changes to the primary directory filesystem and CVS
     * repository that the caller is reponsible for recording into the database;
     * upon return, {@code filesToDeactivate} and {@code filesToAdd} will convey
     * the neccessary details to the caller. The {@code RepositoryFileInfo}
     * objects in {@code filesToAdd} after this call is made will have had their
     * {@code originalSampleHistoryId} updated to reflect that of the target
     * file.
     * </p>
     * 
     * @param targetFiles a collection of {@code RepositoryFileInfo} objects
     *        representing files from the version to which the repository is
     *        being reverted. The caller can obtain these from a
     *        {@code SecondaryDirectory} object.
     * @param primaryDirectoryInfo the primary directory being modified
     * @param filesToAdd a caller-provided {@code Collection}; should support
     *        additions and be empty. When this method returns, the collection
     *        contains zero or more references to new (and incomplete)
     *        {@code RepositoryFileInfo} objects, one for each target file that
     *        was created or modified as part of the reversion.
     * @param filesToDeactivate a caller-provided {@code Collection}; should
     *        support additions and be empty. When this method returns, the
     *        collection contains zero or more references to {@code String}
     *        objects, representing filenames of files that existed in the
     *        primary directory but have since been deleted because they were
     *        not enumerated by the caller in {@code targetFiles}.
     * @throws OperationFailedException if there is a low-level error
     */
    public void revertFiles(Collection<RepositoryFileInfo> targetFiles,
            PrimaryDirectory primaryDirectoryInfo,
            Collection<RepositoryFileInfo> filesToAdd,
            Collection<String> filesToDeactivate)
            throws OperationFailedException {
        /*
         * retreive the existing file listing before we make changes based on
         * the target file listing
         */
        File existingFiles[] = primaryDirectoryInfo.getFile(
                this.repositoryBaseDirectory).listFiles();
        
        if (existingFiles == null) {
            throw new OperationFailedException(
                    "Could not list files for directory "
                    + this.repositoryBaseDirectory.getAbsolutePath());
        }

        /*
         * a set of Strings that are filenames representing files that don't
         * need to be removed or replaced
         */
        Collection<String> unchangedFiles = new HashSet<String>();

        // Iterate through old filenames and add/update those files
        for (RepositoryFileInfo rfi : targetFiles) {
            if (rfi.lastSampleHistoryId == SampleHistoryInfo.STILL_ACTIVE) {
                /*
                 * file is unchanged and needs no modification, but add it to a
                 * list of unchanged files so that when we iterate through the
                 * existing files we can be sure not to delete it.
                 */
                unchangedFiles.add(rfi.fileName);
            } else {
                /*
                 * file needs to be added/updated in the current working
                 * directory
                 */
                File file = getFile(primaryDirectoryInfo, rfi.fileName);
                
                if (file.exists()) {
                    /*
                     * Signal to the caller that there is a new version of this
                     * file present that needs to be deactivated
                     */
                    filesToDeactivate.add(rfi.fileName);
                }
                cvsInvoker.replaceFileWithRevision(
                        primaryDirectoryInfo.getFile(
                                this.repositoryBaseDirectory),
                        rfi.fileName, rfi.cvsRevision);
                /*
                 * The file doesn't need to be CVS 'added' even if there isn't a
                 * copy of it present in the filesystem because replace with
                 * version is always treated as a modification.
                 */
                String cvsRevision = cvsInvoker.commitModifiedFile(file);
                if (cvsRevision == null) {
                    /*
                     * The file wasn't committed, probably because its contents
                     * of the revert-to version were identical to the current
                     * version. (this can happen with multiple reversions)
                     * Obtain the existing CVS revision number for the file.
                     */
                    cvsRevision = this.cvsInvoker.getFileRevision(file);
                }
                RepositoryFileInfo rfiToAdd = new RepositoryFileInfo(
                        rfi.sampleId, rfi.fileName, cvsRevision, file.length(),
                        rfi.description);

                /*
                 * set the originalSampleHistoryId to that of the version of the
                 * file to which we are reverting
                 */
                rfiToAdd.originalSampleHistoryId = rfi.originalSampleHistoryId;

                filesToAdd.add(rfiToAdd);
            }
        }

        /*
         * delete any files that were not included in the list of files from the
         * target version and signal to the caller to deactivate the database
         * record
         */
        for (File existingFile : existingFiles) {
            String f = existingFile.getName().trim();
            
            if (!existingFile.isHidden() && existingFile.isFile()
                    && !filesToDeactivate.contains(f)
		    && !unchangedFiles.contains(f)) {
                filesToDeactivate.add(f);
                removeFile(primaryDirectoryInfo, f);
            }
        }
    }
}
