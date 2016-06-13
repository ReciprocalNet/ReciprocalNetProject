/*
 * Reciprocal Net project
 * 
 * RepositoryManager.java
 *
 * 17-Jun-2002: leqian wrote first draft
 * 28-Jun-2002: ekoperda added getHostedSamples() function
 * 01-Jul-2002: ekoperda added db access code
 * 12-Jul-2002: ekoperda added configuration properties support, bootstrap
 *              mode support, and site grant file support
 * 12-Aug-2002: ekoperda fixed bug #335
 * 13-Aug-2002: ekoperda fixed bug #345 in createDataDirectory()
 * 27-Aug-2002: ekoperda added scanDirectories(), scanDirsAndSubdirs(),
 *              scanSingleDirectory(), and isDirectoryRegistered().  Also
 *              added support for periodic tasks.
 * 04-Sep-2002: ekoperda added general logging support throughout
 * 27-Sep-2002: ekoperda changed import statements to reflect class
 *              rearrangements
 * 08-Oct-2002: ekoperda changed processCoreMessage() to pass a ProcessedIsmCM
 *              back to Site Manager on success
 * 15-Oct-2002: ekoperda added requestHoldingsDump() method and added code to
 *              pass SampleHoldingCM's to Sample Manager
 * 31-Oct-2002: ekoperda changed method passCoreMessage() to be public
 * 07-Nov-2002: ekoperda added functions createDataFile() and readDataFile()
 * 30-Jan-2003: ekoperda added support for receiving RepositoryHoldingISM's by 
 *              adding eventRepositoryHolding(), writeHoldingRecord(), 2nd 
 *              version of dbDeleteHolding(); modifying dbAddReplaceHolding(), 
 *              processCoreMessage()
 * 14-Feb-2003: adharurk modified isDirectoryValid(), added exception handling
 * 17-Feb-2003: ekoperda added eventSampleStatusHint() and modified
 *              createDataDirectory() to generate RepositoryHoldingISM's
 * 21-Feb-2003: ekoperda added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 14-Mar-2003: ekoperda fixed bug #778 in eventRepositoryHolding()
 * 24-Mar-2003: jobollin fixed bug #816 in createDataDirectory()
 * 02-Apr-2003: nsanghvi added validation to createDataDirectory()
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 29-May-2003: ekoperda added file-versioning support throughout
 * 03-Jun-2003: ekoperda fixed bug #926 in dbDeleteFiles()
 * 09-Jun-2003: ekoperda fixed bug #930 in beginWritingDataFile() and
 *              notifyFileUploadFinished()
 * 20-Jun-2003: midurbin fixed bug #940 in start()
 * 02-Jul-2003: ekoperda fixed bug #949 in notifyFileUploadFinished()
 * 03-Jul-2003: ekoperda fixed bug #963 in beginWritingDataFile() and 
 *              notifyFileUploadFinished(); added dbDeactivateFiles()
 * 10-Jul-2003: midurbin added getLabDirectorySize() and modified start() to 
 *              accomodate new PrimaryDirectoryAgent options
 * 17-Jul-2003: midurbin added isFilenameValid() and modified 
 *              createDataDirectory(), beginWritingDataFile() and
 *              renameDataFile() to call it
 * 31-Jul-2003: midurbin fixed bug #982 in beginReadingDataFile()
 * 04-Aug-2003: midurbin fixed bug #1010: renamed dbDeactivateFiles() to
 *              dbDeactivateFile()
 * 08-Aug-2003: midurbin added revertSampleToVersionIncludingFiles()
 * 12-Aug-2003: midurbin fixed bug #1014 in start()
 * 14-Aug-2003: midurbin/ekoperda fixed bug #1020 in 
 *              registerExistingDirectories()
 * 28-Aug-2003: ekoperda fixed bug #1038 in eventRepositoryHolding()
 * 07-Jan-2004: ekoperda changed package references due to source tree
 *              reorganization
 * 18-Feb-2004: cwestnea fixed bug #1030 in beginReadingDataFile() and 
 *              beginWritingDataFile()
 * 08-Apr-2004: midurbin fixed bug #1130 in createDataDirectory()
 * 07-May-2004: cwestnea modified RMI connection string in start()
 * 03-Jun-2004: cwestnea removed isFilenameValid() and replaced calls to it in
 *              createDataDirectory(), beginWritingDataFile(), and 
 *              renameDataFile() with the validation package
 * 07-Jun-2004: midurbin changed package references due to source tree
 *              reorganization
 * 14-Jun-2004: ekoperda modified registerExistingDirectories() to compensate
 *              for loss of special-purpose functions in SampleManager
 * 21-Jun-2004: ekoperda removed references to obsolete SampleLock class 
 *              throughout
 * 21-Jun-2004: ekoperda modified registerExistingDirectories() to compensate
 *              for loss of special-purpose functions in SampleManager
 * 12-Jul-2004: ekoperda added closeDataFiles(), also renamed
 *              notifyFileUploadFinished() to notifyFileUploadsFinished() and
 *              changed that function to support multiple files at once
 * 30-Jul-2004: cwestnea altered constructor's creation of
 *              SecondaryDirectoryAgent and modified processCoreMessage() to 
 *              handle UnusedSecondaryDirectoryHintCM
 * 18-Aug-2004: cwestnea made changes throughout to use SampleWorkflowBL
 * 05-Jan-2005: midurbin added code to set originalSampleHistoryId before all
 *              calls to dbAddFile()
 * 05-Jan-2005: midurbin updated revertSampleToVersionIncludingFiles() to
 *              accomodate SampleManager.revertSampleToVersion() spec change
 * 11-Jan-2005: jobollin moved Validator to org.recipnet.common
 * 12-Jan-2005: ekoperda fixed bug #1497 in processCoreMessage() and 
 *              passCoreMessage()
 * 11-Feb-2005: ekoperda clarified javadoc on closeDataFiles()
 * 01-Mar-2005: ekoperda modified getRepositoryFiles() to return per-file
 *              historical information
 * 04-Aug-2005: midurbin replaced removeDataFile(), eradicateDataFile() with
 *              removeDataFiles(), eradicateDataFiles() respectively
 * 17-Oct-2005: midurbin added getAllRepositoryFileInfosForSample(), updated
 *              dbFetchFilesForSample()
 * 21-Oct-2005: midurbin added  modifyDataFileDescription() and updated various
 *              method specifications to include file descriptions
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 * 11-Apr-2006: jobollin updated with some generics; revised uses of
 *              ObjectCache; removed unnecessary RemoteException declarations;
 *              reformatted the code; removed unused imports
 * 11-May-2006: jobollin added multiple finally{} blocks to ensure that all
 *              locks are released in the scope of the method that acquires
 *              them, and similarly, that all JDBC Statements are closed by the
 *              method that opens them
 * 19-Jun-2006: updated the signature of method getLabDirectorySize() for
 *              consistency with changes to PrimaryDirectoryAgent 
 * 07-Jan-2008: ekoperda rearranged locking in doPrimaryDirectoryScanTask() and
 *              scanSingleDirectory()
 * 30-Nov-2008: ekoperda added support for InvalidateHoldingsCM
 * 02-Jan-2009: ekoperda improved handling of SampleStatusCM's, including new
 *              functions elevate- and decreaseLocalHoldingLevel() and a new
 *              version of createDataDirectory()
 */

package org.recipnet.site.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.recipnet.common.ObjectCache;
import org.recipnet.common.Validator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.agent.PrimaryDirectoryAgent;
import org.recipnet.site.core.agent.SecondaryDirectoryAgent;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.LockAgent;
import org.recipnet.site.core.lock.OutOfBandReadTicket;
import org.recipnet.site.core.lock.RepositoryTicket;
import org.recipnet.site.core.lock.UnisonClosureTicket;
import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.InvalidateHoldingsCM;
import org.recipnet.site.core.msg.LocalLabsChangedCM;
import org.recipnet.site.core.msg.PingCM;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.RepositoryHoldingISM;
import org.recipnet.site.core.msg.SampleHoldingCM;
import org.recipnet.site.core.msg.SampleStatusHintCM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.msg.UnusedSecondaryDirectoryHintCM;
import org.recipnet.site.core.util.CoreMessageQueue;
import org.recipnet.site.core.util.CoreProcessWrapper;
import org.recipnet.site.core.util.CoreScheduledTask;
import org.recipnet.site.core.util.CvsInvoker;
import org.recipnet.site.core.util.EventSignal;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.MutexLock;
import org.recipnet.site.core.util.PrimaryDirectory;
import org.recipnet.site.core.util.RepositoryHoldingComparator;
import org.recipnet.site.core.util.RepositoryLocks;
import org.recipnet.site.core.util.SecondaryDirectory;
import org.recipnet.site.core.util.SnapshotStatement;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.validation.ComplexFilenameValidator;
import org.recipnet.site.shared.validation.ContainerStringValidator;
import org.recipnet.site.shared.validation.FilenameValidator;

/**
 * Implementation for the Repository Manager core object. There should be only
 * one of these objects ever instantiated at a single time. We'll create our own
 * worker thread so that this object is "always running".
 */
public class RepositoryManager extends UnicastRemoteObject implements
        RepositoryManagerRemote, Runnable {
    // References to the other core objects
    SiteManager siteManager;

    SampleManager sampleManager;

    // Worker thread variables
    private final AtomicReference<Thread> workerThread;

    private final EventSignal startupSignal;

    final EventSignal shutdownSignal;

    private final CoreMessageQueue messageQueue;

    // "Ping" objects -- used to see if the worker is running
    private final MutexLock pingMutex;

    private final EventSignal pingSignal;

    // Configuration parameters. These are set during startup.
    private final Properties properties;

    /**
     * a List of the Lab IDs for all the labs currently hosted on the local site
     */
    private final List<Integer> localLabs;

    /**
     * a cache to map sample IDs to {@code Collection}s of
     * {@code RepositoryHoldingInfo} objects
     */
    private final ObjectCache<Collection<RepositoryHoldingInfo>> holdingsCache;

    // Cache to map sampleId's to SecondaryDirectoryInfo objects.
    private final ObjectCache<SecondaryDirectory> secondaryDirsCache;

    // tells us whether we were started in bootstrap mode or not
    private final boolean bootstrapMode;

    /**
     * a String containing the full path to our local repository's base
     * directory.
     */
    private final String baseDirectory;

    // Agents used to control repository directories.
    private PrimaryDirectoryAgent primaryDirectoryAgent;

    private SecondaryDirectoryAgent secondaryDirectoryAgent;

    // Lock agent shared with sample manager.
    private LockAgent lockAgent;

    // Comparator used to rank sites.
    private RepositoryHoldingComparator holdingsComparator;

    // validates strings to ensure they are valid filenames, subdirectories
    // are allowed
    private final Validator extensionPathValidator;

    // validates strings to ensure they are valid filenames, subdirectories are
    // not allowed
    private final Validator dataFilenameValidator;

    // validates strings to ensure they are valid file descriptions
    private final Validator fileDescriptionValidator;

    /***************************************************************************
     * CONSTRUCTOR AND BASIC METHODS (not remotely accessible)
     **************************************************************************/

    /** The one and only constructor */
    public RepositoryManager(Properties p, boolean bootstrapMode)
            throws RemoteException {
        // initialize our simple member variables
        workerThread = new AtomicReference<Thread>(null);
        startupSignal = new EventSignal(false, false);
        shutdownSignal = new EventSignal(false, false);
        messageQueue = new CoreMessageQueue();
        pingMutex = new MutexLock();
        pingSignal = new EventSignal();
        localLabs = new ArrayList<Integer>();
        properties = p;
        this.bootstrapMode = bootstrapMode;
        baseDirectory = properties.getProperty("RepBaseDirectory");
        extensionPathValidator = new ComplexFilenameValidator();
        dataFilenameValidator = new FilenameValidator();
        fileDescriptionValidator = new ContainerStringValidator();

        // create our object caches according to configuration parameters
        holdingsCache = ObjectCache.newInstance(
                properties.getProperty("RepHoldingsCache"));
        secondaryDirsCache = ObjectCache.newInstance(
                properties.getProperty("RepSecondaryDirsCache"));
    }

    /**
     * Called by CoreLoader. This function performs all one-time initialization
     * tasks like RMI binding, connecting to the database, etc. It also spawns
     * the worker thread. This function will return false if one or more of
     * those one-time startup actions failed - this indicates that the other
     * core modules should be terminated.
     */
    public boolean start() {
        
        /*
         * First task: ask Site Manager for a list of labs currently hosted at
         * the local site. We'll be notified via a CoreMessage if this list
         * should change sometime later.
         */
        int labarray[];
        
        try {
            labarray = siteManager.getLocalLabs();
        } catch (OperationFailedException ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.getLocalLabsException(this, ex));
            stop();
            return false;
        }
        
        for (int labId : labarray) {
            localLabs.add(Integer.valueOf(labId));
        }

        /*
         * Initiate some of our miscellaneous member objects.
         */
        holdingsComparator
                = new RepositoryHoldingComparator(siteManager.localSiteId);

        /* share sampleManager's LockAgent */
        lockAgent = sampleManager.lockAgent;

        /*
         * If we were started in bootstrap mode, terminate right here. The
         * startup tasks below this line are not necessary for bootstrap mode.
         */
        if (bootstrapMode) {
            return true;
        }

        /*
         * Next task: ask Site Manager for the URL of the local site's
         * repository. Initialize all the file-management agents.
         */
        try {
            SiteInfo localSiteInfo
                    = siteManager.getSiteInfo(siteManager.localSiteId);
            CvsInvoker cvsInvoker = new CvsInvoker(new File(
                    properties.getProperty("RepCvsCommand")), new File(
                    properties.getProperty("RepCvsDirectory")),
                    siteManager.logger);
            primaryDirectoryAgent = new PrimaryDirectoryAgent(
                    localSiteInfo.repositoryUrl, new File(
                            properties.getProperty("RepBaseDirectory")),
                    properties.getProperty("RepScriptCreateDir"),
                    properties.getProperty("RepScriptLabDirSize"),
                    properties.getProperty("RepUploadedFilePrefix"),
                    cvsInvoker, siteManager);
            secondaryDirectoryAgent = new SecondaryDirectoryAgent(
                    localSiteInfo.repositoryUrl,
                    new File(properties.getProperty("RepBaseDirectory")),
                    Long.parseLong(properties.getProperty(
                            "RepPriorVersionGracePeriod")),
                    Long.parseLong(properties.getProperty(
                            "RepPriorVersionFileSizeLimit")),
                    Long.parseLong(properties.getProperty(
                            "RepPriorVersionTotalSizeLimit")),
                    cvsInvoker, lockAgent, siteManager, this);
        } catch (Exception ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryAgentStartupException(ex));
            stop();
            return false;
        }

        /*
         * Next task: spawn the worker thread and wait for it to start. The
         * thread name and wait time are user-configurable.
         */
        workerThread.set(new Thread(this,
                properties.getProperty("RepWorkerThreadName")));
        workerThread.get().start();
        int startuptime = Integer.parseInt(
                properties.getProperty("RepWorkerThreadStartupTime"));
        if (!startupSignal.receive(startuptime)) {
            // The worker thread failed to start in a timely fashion.
            // Terminate.
            siteManager.recordLogRecord(
                    LogRecordGenerator.threadFailedToStart(this));
            workerThread.getAndSet(null).interrupt();
            stop();
            return false;
        }

        /*
         * Last task: bind ourselves to RMI so that other modules can cannect to
         * us and begin making requests.
         */
        try {
            Naming.bind("//localhost:" + properties.getProperty("GenRmiPort")
                    + "/" + properties.getProperty("RepRmiName"), this);
        } catch (AlreadyBoundException ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.rmiAlreadyBoundException(
                            this, properties.getProperty("RepRmiName"), ex));
            stop();
            return false;
        } catch (ConnectException ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.rmiConnectException(this, ex));
            stop();
            return false;
        } catch (Exception ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.rmiException(this, ex));
            stop();
            return false;
        }

        // Everything has started up successfully.
        return true;
    }

    /**
     * Called by CoreShutdown. This method signals this object's worker threads
     * to terminate asynchronously. The shutdownSignal event signal will be set
     * when this object's shutdown has completed.
     */
    public void stop() {
        
        // Unbind the RMI name so that new calls can't arrive
        try {
            
            /*
             * FIXME: this probably doesn't prevent new calls by existing
             * clients
             */
            Naming.unbind(properties.getProperty("RepRmiName"));
        } catch (Exception ex) {
            // No special action required
        }

        // Signal the worker thread to stop
        Thread temp = workerThread.getAndSet(null);
        
        if (temp != null) {
            temp.interrupt();
        } else {
            // the thread has terminated abornormally previously, so just
            // report that the thread has already terminated.
            shutdownSignal.send();
        }

        // Clean up any temp files that might exist on the filesystem.

        if (secondaryDirectoryAgent != null) {
            secondaryDirectoryAgent.purgeAll();
        }
    }

    /**
     * The primary worker thread. It will not terminate unless the stop() method
     * is called from another thread.
     */
    public void run() {
        try {

            // Initialize variables needed for thread management
            Thread thisThread = Thread.currentThread();
            final EventSignal shuttingDown = new EventSignal();

            // Perform any one-time initialization tasks
            siteManager.schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("RepScannerTask"), new Runnable() {
                        public void run() {
                            doPrimaryDirectoryScanTask(shuttingDown);
                        }
                    }));

            // Tell everyone that we've started successfully
            startupSignal.send();

            // Do our primary thread looper
            while (workerThread.get() == thisThread) {
                CoreMessage msg = messageQueue.receive(0);
                if (msg != null) {
                    processCoreMessage(msg);
                }
            }

            // A shutdown request has been received. Perform shutdown tasks.
            shuttingDown.send(); // command any running scans to abort

            // Tell every other thread that Site Manager's worker thread has
            // terminated gracefully
            shutdownSignal.send();
        } catch (Exception ex) {
            // Generic catch-all for otherwise uncaught exceptions
            siteManager.recordLogRecord(LogRecordGenerator.threadException(
                    this, ex));
        }
    }

    /***************************************************************************
     * REMOTE METHODS
     **************************************************************************/

    /**
     * Call this function to determine whether this core object is still
     * functioning. Sends a message to the worker thread and waits for a reply;
     * returns true only if the worker thread is running. If milliseconds is
     * specified, this is the maximum number of milliseconds that this call
     * should wait for the ping reply before deciding the worker is
     * unresponsive.
     */
    public boolean isAlive(int milliseconds) {

        // Wait for any other outstanding ping operation to complete
        if (!pingMutex.acquire(milliseconds)) {
            return false;
        } else {
            try {

                // Send a message to the worker thread asking him to signal the
                // pingSignal. Wait for his reply.
                pingSignal.reset();
                messageQueue.send(new PingCM());
                
                return pingSignal.receive(milliseconds);
            } finally {
                pingMutex.release();
            }
        }
    }

    /**
     * Allows the client to determine whether the specified sample is hosted at
     * the local site. Returns: RepositoryHoldingInfo.NO_DATA if the sample is
     * not hosted locally (or the sample id is unknown)
     * RepositoryHoldingInfo.BASIC_DATA if at least a "basic" set of data files
     * is present for this sample RepositoryHoldingInfo.FULL_DATA if a "full"
     * set of data files is present for this sample. This value is always
     * returned for samples whose originating lab is based at the local site.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public int getLocalHoldingLevel(int sampleId)
            throws OperationFailedException {
        try {
            AbstractLock lock = RepositoryLocks.getLocalHoldingLevel(sampleId);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
                
                return ((holding == null) ? RepositoryHoldingInfo.NO_DATA
                        : holding.replicaLevel);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Returns an ArrayList containing RepositoryHoldingInfo objects (fetched
     * from the database or cache) for the specified sampleId. These holdings
     * might exist on other sites, depending upon Repository Manager's internal
     * replication scheme. Entries in the returned list are ordered, with the
     * "most preferable" sites listed first. Returns an empty list if no
     * holdings for the specified sample were found (or the sample id is not
     * known).
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public List<RepositoryHoldingInfo> getHoldingsForSample(int sampleId)
            throws OperationFailedException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.getHoldings(sampleId);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // See if the set of holdings for this sample has been cached.
                Collection<RepositoryHoldingInfo> holdings
                        = holdingsCache.get(sampleId);
                if (holdings == null) {
                    // Not in the cache; fetch them from the db.
                    holdings = dbFetchHoldingsForSample(
                            lock.getConnection(), sampleId);
                    holdingsCache.put(sampleId, holdings);
                }

                // Sort the holdings in order of preference.
                ArrayList<RepositoryHoldingInfo> holdingsForCaller
                        = new ArrayList<RepositoryHoldingInfo>(holdings);
                Collections.sort(holdingsForCaller, holdingsComparator);

                return holdingsForCaller;
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Discovers the sample data files that are available for a particular
     * sample/version. If the version requested is not the most recent version
     * of the requested sample, this may involve the creation and population of
     * a secondary repository directory as needed. Throws an exception if data
     * files are not available for the specified sample at the present time.
     * 
     * @return a {@code RepositoryFiles} object that describes the zero
     *         or more sample data files available for the specified
     *         sample/version. The files enumerated by this object may have a
     *         mix of availabilities: {@code FILE_IN_PRIMARY_DIRECTORY},
     *         {@code FILE_IN_SECONDARY_DIRECTORY}, and
     *         {@code FILE_AVAILABLE_UPON_REQUEST}.
     * @param sampleId identifies the sample for which files are being queried.
     * @param sampleHistoryId identifies the version of the specified sample for
     *        which files are being queried.
     * @param shouldIgnoreFileSizeLimit should be false in normal operation. If
     *        true, then every returned file that would have had availability
     *        {@code FILE_AVAILABLE_UPON_REQUEST} otherwise instead is
     *        extracted to a secondary repository directory and assigned
     *        availability {@code FILE_IN_SECONDARY_DIRECTORY}.
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws RepositoryDirectoryNotFoundException if no local holding record
     *         exists for the specified sample, or if no primary repository
     *         directory exists for the specified sample. The exception object
     *         contains a suggested location for the primary repository
     *         directory. Whatever the underlying error condition, the problem
     *         may be remedied by invoking {@code createDataDirectory()}.
     * @throws WrongSiteException if no local holding record exists for the
     *         specified sample and the local site is not authoritative for that
     *         sample. This exception indicates that there is no reason to
     *         assume that data files associated with the specified sample
     *         should be available at the local site.
     */
    public RepositoryFiles getRepositoryFiles(int sampleId,
            int sampleHistoryId, boolean shouldIgnoreFileSizeLimit)
            throws InconsistentDbException, OperationFailedException,
            RepositoryDirectoryNotFoundException, ResourceNotFoundException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.getRepositoryFiles(sampleId,
                    sampleHistoryId);
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                /*
                 * Fetch container objects from the other two core modules.
                 * These calls throw exceptions if the references are invalid.
                 */
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        sampleHistoryId, lock);
                siteManager.getSiteInfo(siteManager.localSiteId);
                LabInfo lab = siteManager.getLabInfo(sample.labId);

                // Fetch our own container objects.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    
                    /*
                     * This sample has no holding record, so we couldn't
                     * possibly return a list of files. See if it's even
                     * possible for us to have a holding record for the
                     * specified sample.
                     */
                    if (!localLabs.contains(new Integer(sample.labId))) {
                        
                        /*
                         * The local site is not authoritative for the requested
                         * sample and there is no local holding record. No files
                         * are available here.
                         */
                        throw new WrongSiteException();
                    }

                    /*
                     * Let PrimaryDirectoryAgent suggest to the caller a
                     * filesystem location for which he ought to create a
                     * holding.
                     */
                    return primaryDirectoryAgent.getRepositoryFiles(
                            new PrimaryDirectory(sample, lab), false,
                            sampleHistoryId);
                }

                if (sample.isMostRecentVersion()) {
                    /*
                     * The most recent version of the sample was specified, so
                     * let PrimaryDirectoryAgent tell the caller about the files
                     * available.
                     */
                    RepositoryFiles rf
                            = primaryDirectoryAgent.getRepositoryFiles(
                                    new PrimaryDirectory(sample, lab, holding),
                                    true, sample.historyId);

                    /*
                     * Supply historical database information to the
                     * RepositoryFiles object because our callers might be
                     * interested in this.
                     */
                    SecondaryDirectory sd = getSecondaryDirectoryInfo(
                            lock.getConnection(), sample.id, sample.historyId);
                    rf.supplyHistoricalInformation(sd.filesAvailable);
                    
                    return rf;
                } else {
                    
                    /*
                     * A prior version of the sample was specified, so let
                     * SecondaryDirectoryAgent tell the caller about the files
                     * available. The proper versions of some files just might
                     * be sitting in the primary directory, so ask
                     * PrimaryDirectoryAgent to check that and pass the results
                     * to SecondaryDirectoryAgent.
                     */
                    return secondaryDirectoryAgent.getRepositoryFiles(
                            getSecondaryDirectoryInfo(lock.getConnection(),
                                    sample.id, sample.historyId),
                            primaryDirectoryAgent.getRepositoryFiles(
                                    new PrimaryDirectory(sample, lab, holding),
                                    true, sample.historyId),
                            shouldIgnoreFileSizeLimit);
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Queries the database and returns all files added or removed for the given
     * sample. This method does not consult the primary directory nor does it
     * extract any secondary directories. This method is meant to supply file
     * information to complement the sample information in a recently fetched
     * {@code FullSampleInfo} object.
     * <p>
     * This method does not make use of caching to improve performance so
     * redundant calls should be avoided.
     * 
     * @param sampleId identifies the sample for which files are being queried.
     * @return an of {@code RepositoryFileInfo} objects containing all
     *         information about files for all versions of the sample. This
     *         method should never return null but may return an empty array if
     *         no files are/were associated with the sample or if there isn't
     *         currently a holding record for the sample.
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws WrongSiteException if no local holding record exists for the
     *         specified sample and the local site is not authoritative for that
     *         sample. This exception indicates that there is no reason to
     *         assume that data files associated with the specified sample
     *         should be available at the local site.
     */
    public RepositoryFileInfo[] getAllRepositoryFileInfosForSample(int sampleId)
            throws InconsistentDbException, OperationFailedException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock
                    = RepositoryLocks.getAllRepositoryFileInfosForSample(
                            sampleId);
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                /*
                 * Fetch container objects from the other two core modules.
                 * These calls throw exceptions if the references are invalid.
                 */
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);

                // Fetch our own container objects.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    /*
                     * This sample has no holding record, so we couldn't
                     * possibly return a list of files. See if it's even
                     * possible for us to have a holding record for the
                     * specified sample.
                     */
                    if (!localLabs.contains(new Integer(sample.labId))) {
                        /*
                         * The local site is not authoritative for the requested
                         * sample and there is no local holding record. No files
                         * are available here.
                         */
                        throw new WrongSiteException();
                    }
                    // there is no holding record and thus no files-- return
                    // an empty array
                    return new RepositoryFileInfo[0];
                }

                Collection<RepositoryFileInfo> files = dbFetchFilesForSample(
                        lock.getConnection(), sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
                return files.toArray(new RepositoryFileInfo[0]);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Convenience version of createDataDirectory() that does not require an
     * existing lock; instead, a new one is obtained.
     */
    public void createDataDirectory(int sampleId, String extensionPath,
	    int userId) throws InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            ResourceNotFoundException, WrongSiteException {
	this.createDataDirectory(sampleId, extensionPath, userId, null);
    }			

    /**
     * Creates the specified data directory in the repository and registers it
     * in the repositoryHoldings database table. This function should only be
     * called after getRepositoryFiles() has thrown a
     * {@code RepositoryDirectoryNotFoundException}.
     * <p>
     * It makes sense to call this method only when the local site is
     * authoritative for the specified sample. If the associated sample is
     * public (as tracked by SampleManager), a broadcast
     * {@code RepositoryHoldingISM} advertising {@code FULL_DATA}
     * is generated.
     * 
     * @param sampleId identifies the sample for which a holding record and
     *        primary repository directory should be created.
     * @param extensionPath the {@code extensionPath} value to be used in
     *        creating the holding record and primary repository directory, or
     *        null if no extension path is desired.
     * @param userId identifies the user on whose behalf this operation is 
     *        being performed (to be associated with a temporary lock, for
     *        informational use only), or {@code INVALID_USER_ID} if this
     *        is not known.
     * @param an existing lock object protecting sufficient privilege for 
     *        creating new directories.  May be null, in which case this method
     *        will obtain (and release) its own new lock.
     * @throws IllegalArgumentException if existingLock does not contain
     *         sufficient privileges.
     * @throws InconsistentDbException if an inconsistency in the database was
     *         detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code NEED_SAMPLE} if {@code dir.sampleId} is not
     *         specified, {@code WRONG_BASEDIRECTORY} if
     *         {@code dir.baseFilespec} differs from the local
     *         repository's base, {@code WRONG_LABDIRECTORY} if
     *         {@code dir.labDirectoryName} does not match the sample's
     *         associated lab's directory name, {@code WRONG_LOCALLABID}
     *         if {@code dir.localLabId} does not match the associated
     *         sample's localLabId, or {@code ILLEGAL_EXTENSIONPATH} if
     *         the data directory requested would not be a filesystem-child (or
     *         child of a child, etc.) of the lab's base directory.
     * @throws InvalidDataException with a reason code of
     *         {@code ILLEGAL_EXTENSIONPATH} if the value
     *         {@code primDir.holdingExtensionPath} would cause the
     *         primary directory to be created in an illegal filesystem
     *         location, one not beneath the associated lab's base directory or
     *         if the extensionPath contains illegal characters.
     * @throws InvalidModificationException with a reason code of
     *         {@code HOLDING_ALREADY_EXISTS} if a local holding record
     *         for the specified sample already exists.
     * @throws OperationFailedException on low-level error.
     * @throws ProcessAbnormalExitException if CVS terminated abnormally.
     * @throws ProcessIncompleteException if CVS was interrupted unexpectedly.
     * @throws WrongSiteException if the local site is not authoritative for 
     *         the specified sample.
     */
    public void createDataDirectory(int sampleId, String extensionPath,
	    int userId, AbstractLock existingLock) 
            throws InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            ResourceNotFoundException, WrongSiteException {
        siteManager.assertIsmGenerationNotHalted();
        try {
            // verify that the extensionPath is valid (the localLabId should
            // have been checked when the sample was generated)
            if ((extensionPath != null)
                    && !extensionPathValidator.isValid(extensionPath)) {
                throw new InvalidDataException(extensionPath,
                        InvalidDataException.ILLEGAL_EXTENSIONPATH);
            }

            // Get a lock.
            AbstractLock lock;
	    if (existingLock == null) {
		lock = RepositoryLocks.createDataDirectory(sampleId, userId);
		lockAgent.registerLock(lock);
		lock.acquire();
	    } else if (!RepositoryLocks.createDataDirectorySubpart(
		    sampleId, userId).isEncompassedBy(existingLock)) {
		throw new IllegalArgumentException();
	    } else {
		lock = existingLock;
	    }

            try {
                // Fetch relevent container objects. These calls throw
                // exceptions if the fetches fail.
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
                LabInfo lab = siteManager.getLabInfo(sample.labId);

                // Make sure the local site is authoritative for the sample.
                if (!localLabs.contains(new Integer(sample.labId))) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }

                // Make sure we don't already have a holding.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
                if (holding != null) {
                    throw new InvalidModificationException(
                           InvalidModificationException.HOLDING_ALREADY_EXISTS,
                            sample);
                }

                // Create a new holding record in the database (and cache).
                holding = updateHoldingRecord(lock.getConnection(), sample.id,
                        siteManager.localSiteId,
                        RepositoryHoldingInfo.FULL_DATA, extensionPath);

                // Create the filesystem directory and register it with CVS.
                Collection<RepositoryFileInfo> filesToAdd
                        = primaryDirectoryAgent.createDirectory(
                                new PrimaryDirectory(sample, lab, holding));
                if (!filesToAdd.isEmpty()) {
                    /*
                     * At least one file already existed in the primary
                     * directory. Update the sample's history log and insert
                     * rows into 'repositoryFiles'.
                     */
                    SampleInfo newSample = sampleManager.putSampleInfo(sample,
                            SampleWorkflowBL.DETECTED_PREEXISTING_FILES,
                            new Date(), userId, null, lock);
                    for (RepositoryFileInfo file : filesToAdd) {
                        file.firstSampleHistoryId = newSample.historyId;
                        file.originalSampleHistoryId = newSample.historyId;
                        dbAddFile(lock.getConnection(), file);
                    }
                }

                // Possibly generate a RepositoryHoldingISM if this is a public
                // sample.
                if (sample.isPublic()) {
                    siteManager.passCoreMessage(new SendIsmCM(
                            new RepositoryHoldingISM(siteManager.localSiteId,
                            sample.id, RepositoryHoldingInfo.FULL_DATA)));
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                if (existingLock == null) {
		    lock.release();
		}
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Retrieves a ticket number that can be used for the next several minutes
     * (configurable within Repository Manager) to access data files for this
     * sample in the repository. The returned ticket number can be verified with
     * verifyTicket().
     * 
     * @throws OperationFailedException on low-level error.
     */
    public int grantNewTicket(RepositoryFiles files, int userId)
            throws OperationFailedException {
        try {
            RepositoryTicket ticket = RepositoryLocks.grantNewTicket(
                    userId,
                    Long.parseLong(
                            properties.getProperty("RepOutOfBandReadTimeout")),
                    files);
            
            lockAgent.registerLock(ticket);
            lockAgent.acquireLock(ticket, true);
            ticket.open();
            return ticket.getId();
            
            // NOTE: the lock is intentionally not released
            
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Verifies a repository-access ticket number that was granted during a
     * previous call to grantNewTicket(). Returns true if the ticket number is
     * valid for the specified file, or false if it is not valid or has expired.
     * 
     * @throws OperationFailedException on low-level error.
     */
    public boolean verifyTicket(int ticketId, String urlFragment)
            throws OperationFailedException {
        try {
            try {
                AbstractLock lock = lockAgent.getLock(ticketId);
                if (!(lock instanceof OutOfBandReadTicket)) {
                    return false;
                }
                OutOfBandReadTicket ticket = (OutOfBandReadTicket) lock;
                return ticket.isUrlFragmentValid(urlFragment);
            } catch (ResourceNotFoundException ex) {
                // Invalid ticket id. Maybe the ticket expired.
                return false;
            } catch (ClassCastException ex) {
                // Ticket wasn't of the kind expected. Maybe the id was bogus.
                return false;
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Verifies a repository-access ticket number that was granted during a
     * previous call to grantNewTicket(). Returns the number of seconds until
     * the ticket expires (if it is valid), or 0 if the ticket is not valid.
     * 
     * @throws OperationFailedException on low-level error.
     */
    public int verifyTicketWithTtl(int ticketId, String urlFragment)
            throws OperationFailedException {
        try {
            try {
                AbstractLock lock = lockAgent.getLock(ticketId);
                
                if (!(lock instanceof OutOfBandReadTicket)) {
                    return 0;
                }
                OutOfBandReadTicket ticket = (OutOfBandReadTicket) lock;
                if (!ticket.isUrlFragmentValid(urlFragment)) {
                    // Ticket is valid, but the urlFragment is not.
                    return 0;
                }
                return (int) (ticket.getTimeToExpire()
                        - System.currentTimeMillis());
            } catch (ResourceNotFoundException ex) {
                // Invalid ticket id. Maybe the ticket expired.
                return 0;
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Renews the repository tickets having the specified IDs by extending the
     * time before they expire without manipulating the underlying files they
     * reference.
     * 
     * @param  ticketIds the IDs of zero or more repository tickets that should
     *         be renewed
     * 
     * @throws ResourceNotFoundException if any of the ticket IDs does not
     *         reference an active RepositoryTicket
     * @throws OperationFailedException if the renewal could not be performed
     *         on any one or more of the specified tickets; in this case some
     *         of the tickets may have been renewed, but some will not have
     *         been; those not renewed may include some that <em>could</em>
     *         have been but weren't
     */
    public void renewTickets(int... ticketIds) throws ResourceNotFoundException,
            OperationFailedException  {
        try {
            for (int ticketId : ticketIds) {
                AbstractLock lock = lockAgent.getLock(ticketId);
                
                if (!(lock instanceof RepositoryTicket)) {
                    throw new ResourceNotFoundException();
                } else {
                    ((RepositoryTicket) lock).touch();
                }
            }
        } catch (IllegalStateException ise) {
            throw new OperationFailedException(ise);
        } catch (DeadlockDetectedException dde) {
            throw new OperationFailedException(dde);
        }
    }

    /**
     * For a sample for which the local site is authoritative and a primary
     * repository directory is available, opens a sample data file within the
     * primary repository directory for writing and returns a ticket number that
     * identifies the open session. Optionally, the method may throw an
     * exception if the target file already exists or does not already exist. A
     * typical caller would subsequently invoke {@code writeToDataFile()}
     * potentially many times and finally {@code closeDataFile()}. When
     * the open ticket is closed gracefully, the specified workflow action is
     * performed on the associated sample and the new data file is bound to the
     * most recent version of the associated sample.
     * 
     * @return a ticket number that identifies the open session. The caller
     *         should retain this value for future use with other file-related
     *         methods.
     * @param sample should be the most recent {@code SampleInfo} version
     *        for the sample with which the new data file should be associated.
     *        This object may have been modified by the caller since having been
     *        fetched, in which case the sample metadata changes will be saved
     *        when the ticket is closed gracefully.
     * @param fileName the name of the file to be created/written, presumably
     *        within the sample's associated primary repository directory.
     * @param allowCreation if false, and the specified file does not already
     *        exist within the specified sample's associated primary repository
     *        directory, a {@code ResourceNotFoundException} is thrown.
     * @param allowOverwrite if false, and the specified file does already exist
     *        within the specified sample's associated primary repository
     *        directory, an {@code OperationNotPermittedException} is
     *        thrown.
     * @param actionCode value used to perform a sample workflow action when the
     *        ticket is closed subsequently, as would be passed to
     *        {@code SampleManager.putSampleInfo()}. The special value
     *        {@code SampleWorkflowBL.SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED}
     *        is permitted if {@code allowCreation} is true and
     *        {@code allowOverwrite} is true -- it instructs this method
     *        to substitute either {@code SampleWorkflowBL.FILE_ADDED} or
     *        {@code SampleWorkflowBL.FILE_REPLACED} in place of the
     *        special value, depending upon whether the target file already
     *        exists or not.
     * @param userId value used to perform a sample workflow action when the
     *        ticket is closed subsequently, as would be passed to
     *        {@code SampleManager.putSampleInfo()}.
     * @param comments value used to perform a sample workflow action when the
     *        ticket is closed subsequently, as would be passed to
     *        {@code SampleManager.putSampleInfo()}.
     * @param description the desired description for the file
     * @throws IllegalArgumentException
     * @throws InvalidDataException with the reason code
     *         {@code ILLEGAL_FILENAME} if the fileName contains invalid
     *         charcters
     * @throws OperationFailedException on low-level error.
     * @throws OperationNotPermittedException if the target file already exists
     *         within the specified sample's associated primary repository
     *         directory and {@code allowOverwrite} is false.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if the operation failed due to a
     *         filesystem permissions error.
     * @throws ResourceNotFoundException if the target file does not already
     *         exist within the specified sample's associated primary repository
     *         directory and {@code allowCreation} is false.
     * @throws WrongSiteException if the local site is not authoritative for the
     *         specified sample.
     */
    public int beginWritingDataFile(SampleInfo sample, String fileName,
            boolean allowCreation, boolean allowOverwrite, int actionCode,
            int userId, String comments, String description)
            throws InvalidDataException, OperationFailedException,
            OperationNotPermittedException,
            RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException {
        try {
            boolean dontReleaseLock = false;

            // Get a lock.
            AbstractLock lock = RepositoryLocks.beforeFileUpload(sample.id,
                    fileName, userId);
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects and do initial
                // validation.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(sample);
                }
                
                LabInfo lab = siteManager.getLabInfo(sample.labId);
                PrimaryDirectory primaryDirectoryInfo
                        = new PrimaryDirectory(sample, lab, holding);
                
                if (!localLabs.contains(sample.labId)) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }
                
                if (!dataFilenameValidator.isValid(fileName)) {
                    // Abort; filename contains illegal characters
                    throw new InvalidDataException(fileName,
                            InvalidDataException.ILLEGAL_FILENAME);
                }
                if ((description != null)
                        && !fileDescriptionValidator.isValid(description)) {
                    // new description contains illegal characters.
                    throw new InvalidDataException(description,
                            InvalidDataException.ILLEGAL_FILE_DESCRIPTION);
                }

                /*
                 * Do some up-front checking in an attempt to detect possible
                 * errors earlier.
                 */
                File destinationFile = primaryDirectoryAgent.getFile(
                        primaryDirectoryInfo, fileName);
                if (destinationFile.exists()) {
                    if (!allowOverwrite) {
                        // Abort; the destination file already exists.
                        throw new OperationNotPermittedException(
                                destinationFile);
                    } else if (!(destinationFile.isFile()
                            && destinationFile.canWrite())) {

                        /*
                         * Abort; we wouldn't have been able to write to the
                         * destination file anyway. (This check wouldn't make
                         * any sense if the destination file didn't already
                         * exist.)
                         */
                        throw new ResourceNotAccessibleException(
                                destinationFile);
                    }
                } else { // File doesn't exist yet
                    if (!allowCreation) {
                        // Abort; the destination file does not already exist.
                        throw new ResourceNotFoundException(destinationFile);
                    } else if (!destinationFile.getParentFile().canWrite()) {
                        /*
                         * Abort; we wouldn't have had permissions to create the
                         * destination file anyway.
                         */
                        throw new ResourceNotAccessibleException(
                                destinationFile);
                    }
                }

                // Special handling for the action code
                // SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED.
                int actionCodeToUse = actionCode;
                if (actionCode
                        == SampleWorkflowBL.SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED) {
                    if (!allowCreation || !allowOverwrite) {
                        throw new IllegalArgumentException();
                    }
                    actionCodeToUse = destinationFile.exists()
                            ? SampleWorkflowBL.FILE_REPLACED
                            : SampleWorkflowBL.FILE_ADDED;
                }

                // Find a place to stick the temp file during the upload.
                File tempFile = primaryDirectoryAgent.createTempFile(
                        primaryDirectoryInfo);

                // Promote our lock to a repository ticket.
                RepositoryTicket ticket = RepositoryLocks.uploadFile(
                        userId,
                        Long.parseLong(properties.getProperty(
                                "RepFileAccessTimeout")),
                        primaryDirectoryInfo, fileName, tempFile, sample,
                        comments, description, actionCodeToUse, this);
                lockAgent.registerLock(ticket);
                lockAgent.promoteLock(lock, ticket);

                ticket.open();
                dontReleaseLock = true;
                return ticket.getId();
            } catch (DeadlockDetectedException ex) {
                // Need special handling for a Deadlock exception because our
                // lock may no longer be active. We wouldn't want to try to
                // release it later in that case.
                lock = lock.getPromotedVersion();
                dontReleaseLock = !lock.isActive();
                throw new OperationFailedException(ex);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                if (!dontReleaseLock) {
                    lock.getPromotedVersion().release();
                }
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * For a sample held locally, opens a sample data file for reading and
     * returns a ticket number that identifies the open session. The caller
     * specifies the sample/version with which the desired file is associated;
     * this in turn indicates the appropriate version of the desired sample data
     * file to be read. The file may be read directly from the sample's
     * associated primary repository directory, if this is the version desired,
     * or may be extracted from the CVS repository to a secondary repository and
     * retained there for the duration of the session.
     * <p>
     * A typical caller subsequently would invoke
     * {@code readFromDataFile()} potentially many times and finally
     * {@code closeDataFile()}.
     * 
     * @return a ticket number that identifies the open session. The caller
     *         should retain this value for future use with other file-related
     *         methods.
     * @param sampleId identifies the sample with which the desired data file is
     *        associated.
     * @param sampleHistoryId identifies the particular version of the specified
     *        sample with which the desired data file/version is associated.
     * @param fileName identifies the file within the specified sample/version's
     *        associated primary or secondary directory that is to be read.
     * @param userId identifies the user on whose behalf this operation is being
     *        performed (to be associated with a temporary lock, for
     *        informational use only), or {@code INVALID_USER_ID} if this
     *        is not known.
     * @throws InconsistentDbException on database inconsistency.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if the operation failed due to a
     *         filesystem permissions error.
     * @throws ResourceNotFoundException if the desired file could not be found
     *         within the specified sample/version's associated primary or
     *         secondary repository directory.
     */
    public int beginReadingDataFile(int sampleId, int sampleHistoryId,
            String fileName, int userId) throws InconsistentDbException,
            OperationFailedException, RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException {
        try {
            boolean dontReleaseLock = false;

            // Get a lock.
            AbstractLock lock = RepositoryLocks.beforeFileDownload(sampleId,
                    sampleHistoryId, fileName, userId);
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects.
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        sampleHistoryId, lock);
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(
                            new SampleInfo(sampleId, sampleHistoryId));
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);

                // Figure out where the data we want to read lives.
                if (sample.isMostRecentVersion()) {
                    // Try to prmote our lock to a ticket on the primary
                    // directory, but don't waste any time blocking.
                    File fileInPrimDir = primaryDirectoryAgent.getFile(
                            new PrimaryDirectory(sample, lab, holding),
                            fileName);
                    RepositoryTicket ticket
                            = RepositoryLocks.fileDownloadPrimary(
                                    userId,
                                    Long.parseLong(properties.getProperty(
                                            "RepFileAccessTimeout")),
                                    sample.id, fileInPrimDir);
                    lockAgent.registerLock(ticket);
                    lockAgent.promoteLock(lock, ticket, false);
                    if (ticket.isActive()) {
                        // Found the file we want in the primary directory.
                        dontReleaseLock = true;
                        try {
                            ticket.open();
                        } catch (OperationFailedException ex) {
                            ticket.release();
                            throw ex;
                        }
                        return ticket.getId();
                    }
                }

                // Fetch database information about the requested file.
                SecondaryDirectory secondaryDirectoryInfo
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), sample.id,
                                sample.historyId);
                RepositoryFileInfo desiredFile
                        = secondaryDirectoryInfo.findFileWithName(fileName);
                if (desiredFile == null) {
                    // According to the db, the requested file doesn't belong
                    // to the specified sample/version.
                    throw new ResourceNotFoundException(fileName);
                }

                // Extract the file to its secondary directory.
                File fileToDownload = secondaryDirectoryAgent.getFile(
                        secondaryDirectoryInfo, desiredFile);

                // Promote our lock to a repository ticket.
                RepositoryTicket ticket2
                        = RepositoryLocks.fileDownloadSecondary(
                                userId,
                                Long.parseLong(properties.getProperty(
                                        "RepFileAccessTimeout")),
                                secondaryDirectoryInfo, fileToDownload);
                lockAgent.registerLock(ticket2);
                lockAgent.promoteLock(lock, ticket2);
                dontReleaseLock = true;
                try {
                    ticket2.open();
                } catch (OperationFailedException ex) {
                    ticket2.release();
                    throw ex;
                }
                return ticket2.getId();
            } catch (DeadlockDetectedException ex) {
                // Need special handling for a Deadlock exception because our
                // lock is no longer active. We wouldn't want to try to
                // release it later.
                dontReleaseLock = true;
                throw new OperationFailedException(ex);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                if (!dontReleaseLock) {
                    lock.release();
                }
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * For an open file session that has been cleared for reading, reads a
     * specified number of bytes from the open file.
     * 
     * @return a {@code byte} array containing data read from the file.
     *         if the size of the array is less than {@code maxBytesToRead},
     *         this indicates that end-of-file has been reached. The array may
     *         have length zero in this case.
     * @param ticketId identifies a previously-opened ticket session.
     * @param maxBytesToRead the maximum number of bytes that should be read
     *        from the file as part of this chunk.
     * @throws OperationFailedException on low-level error.
     * @throws OperationNotPermittedException if the specified ticket is not
     *         authorized to read.
     * @throws ResourceNotFoundException if the specified {@code ticketId}
     *         is not valid.
     */
    public byte[] readFromDataFile(int ticketId, int maxBytesToRead)
            throws OperationFailedException, OperationNotPermittedException,
            ResourceNotFoundException {
        try {
            // Look up the existing lock indicated by the caller.
            AbstractLock lock = lockAgent.getLock(ticketId);
            
            if (!(lock instanceof RepositoryTicket)) {
                throw new ResourceNotFoundException();
            }
            RepositoryTicket ticket = (RepositoryTicket) lock;

            // Fetch bytes from the filesystem in 16K chunks.
            ByteArrayOutputStream baos
                    = new ByteArrayOutputStream(maxBytesToRead);
            int totalBytesRead = 0;
            byte[] buf = new byte[16384];
            int bytesRead;
            do {
                int bytesToRead
                        = Math.min(maxBytesToRead - totalBytesRead, 16384);
                bytesRead = ticket.read(buf, 0, bytesToRead);
                if (bytesRead > 0) {
                    baos.write(buf, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }
            } while ((bytesRead != -1) && (totalBytesRead < maxBytesToRead));

            return baos.toByteArray();
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * For an open file session that has been cleared for writing, writes some
     * bytes to the open file.
     * 
     * @param ticketId identifies a previously-opened ticket session.
     * @param dataToWrite a {@code byte} array whose contents should be
     *        appended to the end of the open file.
     * @throws OperationFailedException on low-level error.
     * @throws OperationNotPermittedException if specified ticket is not
     *         authorized to write.
     * @throws ResourceNotFoundException if the specified {@code ticketId}
     *         is not valid.
     */
    public void writeToDataFile(int ticketId, byte[] dataToWrite)
            throws OperationFailedException, OperationNotPermittedException,
            ResourceNotFoundException {
        try {
            // Look up the preexisting lock indicated by the caller.
            AbstractLock lock = lockAgent.getLock(ticketId);
            if (!(lock instanceof RepositoryTicket)) {
                throw new ResourceNotFoundException();
            }
            RepositoryTicket ticket = (RepositoryTicket) lock;

            // Write bytes to the filesystem.
            ticket.write(dataToWrite);
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Gracefully closes an open file session. For some ticket types, a sample
     * workflow action may be performed as part of this method.
     * 
     * @param ticketId identifies a previously-opened ticket session.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotFoundException if the specified {@code ticketId}
     *         is not valid.
     */
    public void closeDataFile(int ticketId) throws OperationFailedException,
            ResourceNotFoundException {
        try {
            // Look up the preexisting lock indicated by the caller.
            
            AbstractLock lock = lockAgent.getLock(ticketId);
            
            if (!(lock instanceof RepositoryTicket)) {
                throw new ResourceNotFoundException();
            } else {
                // Close any open files (and release the lock)
                ((RepositoryTicket) lock).close();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Gracefully closes several open file sessions at once for tickets that
     * support it. Not all kinds of tickets support closures-in-unison, and
     * additional restrictions on the types of tickets that may be closed
     * simultaneously may be imposed. Consult individual ticket classes for more
     * information about support for unison closures. For some ticket types, a
     * single sample workflow action may be performed as part of this method;
     * for others, none.
     * 
     * @param ticketIds identifies zero or more previously-opened ticket
     *        session.
     * @throws OperationFailedException on low-level error. Note that in this
     *         case the status of the specified tickets is undefined: some may
     *         be open and others may be closed and still others may be stuck
     *         somewhere in the middle.
     * @throws OperationNotPermittedException if any of the specified tickets do
     *         not support unison closures, or if the specified set of tickets
     *         cannot be closed as a group.
     * @throws ResourceNotFoundException if the specified {@code ticketId}
     *         is not valid.
     */
    public void closeDataFiles(int ticketIds[])
            throws OperationFailedException, OperationNotPermittedException,
            ResourceNotFoundException {
        // Exit early in the trivial cases.
        if (ticketIds.length == 0) {
            return;
        } else if (ticketIds.length == 1) {
            closeDataFile(ticketIds[0]);
            return;
        }

        try {
            // Look up ticket IDs and verify lock types.
            
            AbstractLock firstLock = lockAgent.getLock(ticketIds[0]);
            
            if (!(firstLock instanceof RepositoryTicket)) {
                throw new ResourceNotFoundException();
            } else if (!(firstLock instanceof UnisonClosureTicket)) {
                throw new OperationNotPermittedException();
            }
            
            UnisonClosureTicket firstTicket = (UnisonClosureTicket) firstLock;
            UnisonClosureTicket otherTickets[]
                    = new UnisonClosureTicket[ticketIds.length - 1];
            
            for (int i = 0; i < otherTickets.length; i++) {
                AbstractLock lock = lockAgent.getLock(ticketIds[i + 1]);
                
                if (!(lock instanceof RepositoryTicket)) {
                    throw new ResourceNotFoundException();
                } else if (!(lock instanceof UnisonClosureTicket)) {
                    throw new OperationNotPermittedException();
                } else {
                    otherTickets[i] = (UnisonClosureTicket) lock;
                }
            }

            /*
             * Verify with the firstTicket that a unison closure is likely to
             * succeed
             */
            if (!firstTicket.supportsClosureInUnisonWith(otherTickets)) {
                throw new OperationNotPermittedException();
            }

            // Prepare all the tickets for a unison closure.
            firstTicket.beforeUnisonClosure();
            for (int i = 0; i < otherTickets.length; i++) {
                otherTickets[i].beforeUnisonClosure();
            }

            // Tell the first ticket to do the unison closure.
            firstTicket.closeInUnisonWith(otherTickets);

            // Tell all the tickets we just did a unison closure.
            firstTicket.afterUnisonClosure();
            for (int i = 0; i < otherTickets.length; i++) {
                otherTickets[i].afterUnisonClosure();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Abruptly terminates an open file session, removing any temporary
     * resources that might have been in use.
     * 
     * @param ticketId identifies a previously-opened ticket session.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotFoundException if the specified {@code ticketId}
     *         is not valid.
     */
    public void abortDataFile(int ticketId) throws OperationFailedException,
            ResourceNotFoundException {
        try {
            // Look up the preexisting lock indicated by the caller.
            AbstractLock lock = lockAgent.getLock(ticketId);
            
            if (!(lock instanceof RepositoryTicket)) {
                throw new ResourceNotFoundException();
            } else {
                // Close any open files (and release the lock).
                ((RepositoryTicket) lock).abort();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }
    
    /**
     * Eradicates existing sample data files from their primary repository
     * directory by removing the files from the filesystem, and removing all
     * traces of the files from the CVS repository. Because such an operation
     * defeats the file-versioning mechanism, its use should be restricted to
     * special cases. Callers are encouraged to call
     * {@code getDataFileAggregateSize()} and obtain a two-step, informed
     * confirmation from the user before invoking this method. The sample
     * workflow action {@code FILES_ERADICATED} is performed as part of
     * this method.
     * 
     * @param sample should be the most recent {@code SampleInfo} version
     *        for the sample with which the data files to be eradicated are
     *        associated. This object may have been modified by the caller since
     *        having been fetched, in which case the sample metadata changes
     *        will be saved when the sample workflow action is performed.
     * @param fileNames the names of the files to be eradicated, presumably
     *        within the sample's associated primary repository directory.
     * @param userId value used to perform the sample workflow action, as would
     *        be passed to {@code SampleManager.putSampleInfo()}.
     * @param actionCode used to identify the kind of workflow action to be
     *        performed. The value is passed transparently to
     *        {@code SampleManager.putSampleInfo()}.
     * @param comments value used to perform the sample workflow action, as
     *        would be passed to {@code SampleManager.putSampleInfo()}.
     * @throws InconsistentDbException if database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid.
     * @throws InvalidModificationException with a {@code reason} of
     *         {@code CANTCHANGE_LAB}, {@code CANTCHANGE_PROVIDER},
     *         or {@code CANTCHANGE_LOCALLABID} if a prohibited
     *         modification to an existing sample was attempted.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if a permissions error was
     *         encountered while deleting files.
     * @throws ResourceNotFoundException with a nested {@code File}
     *         object if the specified file could not be found within the
     *         specified primary repository directory. Or, an embedded
     *         {@code identifier} of type {@code SampleInfo} if
     *         modification of an unknown sample was attempted, type
     *         {@code LabInfo} if {@code sample.labId} is unknown,
     *         or {@code ProviderInfo} if
     *         {@code sample.providerInfo} is unknown.
     * @throws WrongSiteException if the local site is not authoritative for the
     *         specified sample.
     */
    public void eradicateDataFiles(SampleInfo sample, String[] fileNames,
            int userId, int actionCode, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.eradicateFiles(sample.id,
                    fileNames, userId);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects and do initial
                // validation.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(sample);
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);
                PrimaryDirectory primaryDirectoryInfo = new PrimaryDirectory(
                        sample, lab, holding);
                if (!localLabs.contains(sample.labId)) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }

                // Ensure that each files exist.
                SecondaryDirectory secondaryDirectoryInfo
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), sample.id,
                                sample.historyId);
                for (String fileName : fileNames) {
                    // Find the specific file within the specified
                    // sample/version's SecondaryDirectory object.
                    // We will ensured that the specified version is the most
                    // current by invoking SampleManager.putSampleInfo().
                    if (secondaryDirectoryInfo.findFileWithName(fileName)
                            == null) {
                        throw new ResourceNotFoundException(fileName);
                    }

                }
                // Append an entry to the sample's history log.
                sampleManager.putSampleInfo(
                        sample, actionCode, new Date(), userId, comments, lock);

                // Clear each file from the filesystem and the CVS repository.
                for (String fileName : fileNames) {
                    primaryDirectoryAgent.eradicateFile(
                            primaryDirectoryInfo, fileName);

                    // Delete all applicable rows from the 'repositoryFiles'
                    // table
                    dbDeleteFiles(lock.getConnection(), sample.id, fileName);
                }

                // update the cache.
                invalidateSecondaryDirsCache(sample.id);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Gracefully removes any number of existing sample data files from the most
     * recent version of their associated sample. The indicated sample workflow
     * action is performed as part of this method.
     * 
     * @param sample should be the most recent {@code SampleInfo} version
     *        for the sample with which the data file(s) to be removed is
     *        associated. This object may have been modified by the caller since
     *        having been fetched, in which case the sample metadata changes
     *        will be saved when the sample workflow action is performed.
     * @param fileNames the names of the files to be removed, presumably within
     *        the sample's associated primary repository directory.
     * @param userId value used to perform the sample workflow action, as would
     *        be passed to {@code SampleManager.putSampleInfo()}.
     * @param actionCode used to identify the kind of workflow action to be
     *        performed. The value is passed transparently to
     *        {@code SampleManager.putSampleInfo()}.
     * @param comments value used to perform the sample workflow action, as
     *        would be passed to {@code SampleManager.putSampleInfo()}.
     * @throws InconsistentDbException if database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if a permissions error was
     *         encountered while deleting files.
     * @throws ResourceNotFoundException with a nested {@code File}
     *         object if the specified file could not be found within the
     *         specified primary repository directory. Or, an embedded
     *         {@code identifier} of type {@code SampleInfo} if
     *         modification of an unknown sample was attempted, type
     *         {@code LabInfo} if {@code sample.labId} is unknown,
     *         or {@code ProviderInfo} if
     *         {@code sample.providerInfo} is unknown.
     * @throws WrongSiteException if the local site is not authoritative for the
     *         specified sample.
     */
    public void removeDataFiles(SampleInfo sample, String[] fileNames,
            int userId, int actionCode, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, OperationFailedException,
            OperationNotPermittedException,
            RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.removeFiles(
                    sample.id, fileNames, userId);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects and do initial
                // validation.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(sample);
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);
                PrimaryDirectory primaryDirectoryInfo = new PrimaryDirectory(
                        sample, lab, holding);
                if (!localLabs.contains(new Integer(sample.labId))) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }
                SecondaryDirectory secondaryDirectoryInfo
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), sample.id,
                                sample.historyId);

                for (String fileName : fileNames) {
                    // Find the specific file within the specified
                    // sample/version's SecondaryDirectory object.
                    // We will ensured that the specified version is the most
                    // current by invoking SampleManager.putSampleInfo().
                    if (secondaryDirectoryInfo.findFileWithName(fileName)
                            == null) {
                        throw new ResourceNotFoundException(fileName);
                    }
                }

                // Append an entry to the sample's history log.
                SampleInfo newSample = sampleManager.putSampleInfo(sample,
                        actionCode, new Date(), userId, comments, lock);

                for (String fileName : fileNames) {
                    // Clear the file from the filesystem and the CVS
                    // repository.
                    primaryDirectoryAgent.removeFile(primaryDirectoryInfo,
                            fileName);

                    // Delete all applicable rows from the 'repositoryFiles'
                    // table.
                    dbDeactivateFile(
                            lock.getConnection(),
                            secondaryDirectoryInfo.findFileWithName(fileName).id,
                            newSample.historyId);
                }

                // update the cache
                invalidateSecondaryDirsCache(sample.id);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Renames an existing sample data file on the most recent version of its
     * associated sample. The sample workflow action {@code FILE_RENAMED}
     * is performed as part of this method.
     * 
     * @param sample should be the most recent {@code SampleInfo} version
     *        for the sample with which the data file to be renamed is
     *        associated. This object may have been modified by the caller since
     *        having been fetched, in which case the sample metadata changes
     *        will be saved when the sample workflow action is performed.
     * @param oldFileName the name of the existing file to be renamed.
     * @param newFileName the desired name for the file.
     * @param userId value used to perform the sample workflow action, as would
     *        be passed to {@code SampleManager.putSampleInfo()}.
     * @param comments value used to perform the sample workflow action, as
     *        would be passed to {@code SampleManager.putSampleInfo()}.
     * @throws InconsistentDbException if database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid, or with the reason
     *         {@code ILLEGAL_FILENAME} if the filename contains illegal
     *         characters.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if a permissions error was
     *         encountered while deleting files.
     * @throws ResourceNotFoundException with a nested {@code File}
     *         object if the specified file could not be found within the
     *         specified primary repository directory. Or, an embedded
     *         {@code identifier} of type {@code SampleInfo} if
     *         modification of an unknown sample was attempted, type
     *         {@code LabInfo} if {@code sample.labId} is unknown,
     *         or {@code ProviderInfo} if
     *         {@code sample.providerInfo} is unknown.
     * @throws WrongSiteException if the local site is not authoritative for the
     *         specified sample.
     */
    public void renameDataFile(SampleInfo sample, String oldFileName,
            String newFileName, int userId, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, OperationFailedException,
            OperationNotPermittedException,
            RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.renameFile(
                    sample.id, oldFileName, newFileName, userId);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects and do initial
                // validation.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(sample);
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);
                PrimaryDirectory primaryDirectoryInfo = new PrimaryDirectory(
                        sample, lab, holding);
                if (!localLabs.contains(new Integer(sample.labId))) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }
                SecondaryDirectory secondaryDirectoryInfo
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), sample.id,
                                sample.historyId);
                if (!dataFilenameValidator.isValid(newFileName)) {
                    // new file name contains illegal characters.
                    throw new InvalidDataException(newFileName,
                            InvalidDataException.ILLEGAL_FILENAME);
                }

                // Find the specific file within the specified sample/version's
                // SecondaryDirectory object.
                RepositoryFileInfo oldFileInfo
                        = secondaryDirectoryInfo.findFileWithName(oldFileName);
                if (oldFileInfo == null) {
                    throw new ResourceNotFoundException(oldFileName);
                }

                // Clear the file from the filesystem and the CVS repository.
                RepositoryFileInfo fileInfo
                        = primaryDirectoryAgent.renameFile(
                                primaryDirectoryInfo, oldFileInfo, newFileName);

                // Append an entry to the sample's history log.
                SampleInfo newSample = sampleManager.putSampleInfo(sample,
                        SampleWorkflowBL.FILE_RENAMED, new Date(), userId,
                        comments, lock);

                // Update information in the 'repositoryFiles' table and cache.
                dbDeactivateFile(lock.getConnection(), oldFileInfo.id,
                        newSample.historyId);
                fileInfo.firstSampleHistoryId = newSample.historyId;
                fileInfo.originalSampleHistoryId = newSample.historyId;
                dbAddFile(lock.getConnection(), fileInfo);
                invalidateSecondaryDirsCache(sample.id);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Modifies the description associated with a given data file.
     * 
     * @param sample should be the most recent {@code SampleInfo} version
     *        for the sample with which the data file to be renamed is
     *        associated. This object may have been modified by the caller since
     *        having been fetched, in which case the sample metadata changes
     *        will be saved when the sample workflow action is performed.
     * @param fileName the name of the existing file whose description will be
     *        changed
     * @param description the desired description for the file
     * @param userId value used to perform the sample workflow action, as would
     *        be passed to {@code SampleManager.putSampleInfo()}.
     * @param comments value used to perform the sample workflow action, as
     *        would be passed to {@code SampleManager.putSampleInfo()}.
     * @throws InconsistentDbException if database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid, or with the reason
     *         {@code ILLEGAL_FILENAME} if the filename contains illegal
     *         characters.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotAccessibleException if a permissions error was
     *         encountered while deleting files.
     * @throws ResourceNotFoundException with a nested {@code File}
     *         object if the specified file could not be found within the
     *         specified primary repository directory. Or, an embedded
     *         {@code identifier} of type {@code SampleInfo} if
     *         modification of an unknown sample was attempted, type
     *         {@code LabInfo} if {@code sample.labId} is unknown,
     *         or {@code ProviderInfo} if
     *         {@code sample.providerInfo} is unknown.
     * @throws WrongSiteException if the local site is not authoritative for the
     *         specified sample.
     */
    public void modifyDataFileDescription(SampleInfo sample, String fileName,
            String description, int userId, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, OperationFailedException,
            OperationNotPermittedException,
            RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.modifyFileDescription(
                    sample.id, fileName, userId);
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects and do initial
                // validation.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sample.id);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(sample);
                }
                siteManager.getLabInfo(sample.labId);
                
                if (!localLabs.contains(sample.labId)) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }
                SecondaryDirectory secondaryDirectoryInfo
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), sample.id,
                                sample.historyId);
                if ((description != null)
                        && !fileDescriptionValidator.isValid(description)) {
                    // new description contains illegal characters.
                    throw new InvalidDataException(description,
                            InvalidDataException.ILLEGAL_FILE_DESCRIPTION);
                }

                // Find the specific file within the specified sample/version's
                // SecondaryDirectory object.
                RepositoryFileInfo fileInfo
                        = secondaryDirectoryInfo.findFileWithName(fileName);
                if (fileInfo == null) {
                    throw new ResourceNotFoundException(fileName);
                }

                // Append an entry to the sample's history log.
                SampleInfo newSample = sampleManager.putSampleInfo(sample,
                        SampleWorkflowBL.FILE_DESCRIPTION_MODIFIED, new Date(),
                        userId, comments, lock);

                // Update information in the 'repositoryFiles' table and cache.
                dbDeactivateFile(lock.getConnection(), fileInfo.id,
                        newSample.historyId);
                fileInfo.firstSampleHistoryId = newSample.historyId;
                fileInfo.originalSampleHistoryId = newSample.historyId;
                fileInfo.description = description;
                fileInfo.id = RepositoryFileInfo.INVALID_REPOSITORY_FILE_ID;
                dbAddFile(lock.getConnection(), fileInfo);
                invalidateSecondaryDirsCache(sample.id);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Changes the data and files for the specified sample back to the way they
     * were for a previous version of the sample. No data is actually erased;
     * instead a new workflow action is performed.
     * 
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER} or
     *         {@code BLANK_STRINGS_FORBIDDEN} if the specified
     *         {@code sample} is not valid.
     * @throws InvalidModificationException with a {@code reason} of
     *         {@code CANTCHANGE_LAB}, {@code CANTCHANGE_PROVIDER},
     *         or {@code CANTCHANGE_LOCALLABID} if a prohibited
     *         modification to an existing sample was attempted.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws OptimisticLockingException if another possibly conflicting update
     *         to the same sample has been made recently.
     * @throws ResourceNotFoundException with an embedded
     *         {@code identifier} of type {@code SampleInfo} if
     *         modification of an unknown sample was attempted, type
     *         {@code LabInfo} if {@code sample.labId} is unknown,
     *         or {@code ProviderInfo} if
     *         {@code sample.providerInfo} is unknown.
     * @throws UnexpectedExceptionException
     * @throws WrongSiteException if the sample about to be created or modified
     *         did not originate at a lab currently hosted at the local site.
     */
    public void revertSampleToVersionIncludingFiles(int sampleId,
            int currentHistoryId, int desiredHistoryId, int userId,
            String comments) throws InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        try {
            // Get a lock that will encompass the entire operation
            AbstractLock lock
                    = RepositoryLocks.revertSampleToVersionIncludingFiles(
                            sampleId, userId);
            
            lockAgent.registerLock(lock);
            lock.acquire();
            try {
                // revert metadata
                sampleManager.revertSampleToVersion(sampleId, currentHistoryId,
                        desiredHistoryId, userId, comments, lock, true);

                // retreive updated sample
                SampleInfo newSample = sampleManager.getSampleInfo(sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
                // Fetch remaining relevent container objects.
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), newSample.id);
                if (holding == null) {
                    // no repository, therefore the file reversion can be
                    // treated as complete.
                    return;
                }
                LabInfo lab = siteManager.getLabInfo(newSample.labId);
                if (!localLabs.contains(newSample.labId)) {
                    // Local site is not authoritative for this sample.
                    throw new WrongSiteException();
                }

                // get the PrimaryDirectory object for the indicated sample
                PrimaryDirectory primaryDirectoryInfo
                        = new PrimaryDirectory(newSample, lab, holding);

                /*
                 * get collection of RepositoryFileInfo objects for the version
                 * to which we will revert
                 */
                Collection<RepositoryFileInfo> oldVersionFiles
                        = getSecondaryDirectoryInfo(
                                lock.getConnection(), newSample.id,
                                desiredHistoryId).filesAvailable;

                /*
                 * create empty collections that will contain the files that
                 * will be modified by primaryDirectoryAgent.revertFiles() so
                 * that we can update the DB to reflect the filesystem
                 */
                Collection<RepositoryFileInfo> filesToAdd
                        = new ArrayList<RepositoryFileInfo>();
                Collection<String> filesToDeactivate = new ArrayList<String>();
                primaryDirectoryAgent.revertFiles(oldVersionFiles,
                        primaryDirectoryInfo, filesToAdd, filesToDeactivate);

                /*
                 * Deactivate rows in the 'repositoryFiles' table to reflect
                 * deleted/modified files.
                 */
                for (String filename : filesToDeactivate) {
                    dbDeactivateFile(lock.getConnection(), newSample.id,
                            filename, newSample.historyId);
                }
                invalidateSecondaryDirsCache(newSample.id);

                /*
                 * Add rows to the 'repositoryFiles' table to reflect
                 * added/modified files.
                 */
                for (RepositoryFileInfo rfi : filesToAdd) {
                    rfi.firstSampleHistoryId = newSample.historyId;
                    /*
                     * normally the originalSampleHistoryId would be set here,
                     * but in this case it has already been set by
                     * PrimaryDirectoryAgent.revertFiles()
                     */
                    dbAddFile(lock.getConnection(), rfi);
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                // Generic catch-all to ensure the lock gets released.
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Calculates the total number of bytes presently used to store all versions
     * of a specified sample data file. The total includes storage within the
     * CVS repository and storage of the most recent version within the primary
     * repository directory. This number is for informational purposes only, and
     * often is invoked in preparation for a call to
     * {@code eradicateDataFiles()}.
     * 
     * @param sampleId identifies the sample with which the data file is
     *        associated.
     * @param fileName identifies the file within the specified primary
     *        repository directory whose size should be calculated.
     * @throws InconsistentDbException on database inconsistency.
     * @throws OperationFailedException on low-level error.
     * @throws RepositoryDirectoryNotFoundException if a primary repository
     *         directory is not available for the specified sample at the
     *         present time. No suggestion is included within the exception
     *         object.
     * @throws ResourceNotFoundException with a nested {@code File}
     *         object if the specified file could not be found within the
     *         specified primary repository directory.
     */
    public long getDataFileAggregateSize(int sampleId, String fileName)
            throws InconsistentDbException, OperationFailedException,
            RepositoryDirectoryNotFoundException, ResourceNotFoundException {
        try {
            // Get a lock.
            AbstractLock lock = RepositoryLocks.getDataFileAggregateSize(
                    sampleId, fileName);
            
            lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Fetch all the relevent container objects.
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
                if (holding == null) {
                    throw new RepositoryDirectoryNotFoundException(
                            new SampleInfo(sampleId));
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);

                return primaryDirectoryAgent.getFileAggregateSize(
                        new PrimaryDirectory(sample, lab, holding), fileName);
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }



    /**************************************************************************
     * INTRA-CORE METHODS (not remotely accessible)
     *************************************************************************/



    /**
     * Allows other Core modules to pass a message to this one.
     * 
     * @throws OperationFailedException only if Repository Manager was
     *         initialized in bootstrap mode, and then only if an error occurred
     *         while processing the specified {@code msg}. Such errors
     *         are reported synchronously in bootstrap mode because message
     *         processing happens synchronously in bootstrap mode.
     */
    public void passCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        if (bootstrapMode) {
            // We deliver all messages synchronously if we're in bootstrap mode
            processCoreMessage(msg);
        } else {
            // Deliver messages asynchronously to the worker thread during
            // normal operation.
            messageQueue.send(msg);
        }
    }

    /**
     * A periodic task that scans the contents of each registered primary
     * repository directory, notes any changes, and updates the database to
     * reflect them.
     * <p>
     * Locking Implementation Note: Previous versions of this method acquired a
     * lock and held it for the duration of the repository scan, promoting and
     * demoting it once for each individual primary repository directory.  
     * Although this was an elegant solution, it caused practical problems on
     * sites with many thousands of primary directories.  The locking 
     * infrastructure cannot handle several thousand promotions/demotions on a
     * single lock without encountering OutOfMemoryError's and garbage
     * collection flukes.  Thus, the present implementation uses individual
     * locks for each primary repository directory scanned.  There is no
     * motherlock to unify the whole scan operation.
     *
     * @param cancel an {@code EventSignal} that this method will monitor to
     *        determine whether it needs to terminate prematurely
     */
    public void doPrimaryDirectoryScanTask(EventSignal cancel) {
	ScanResult stats = new ScanResult();
	try {
	    if (cancel.isSet()) {
		return;
	    }

	    // Get a db lock.
	    AbstractLock lock = RepositoryLocks.primaryDirectoryScanBegin();
	    lockAgent.registerLock(lock);
	    lock.acquire();

	    /*
	     * Take a snapshot of all local holding records in the db. We will
	     * iterate over the snapshot instead of over the live table in
	     * order to avoid locking the live table during the (possibly
	     * lengthy) running time of this method.  So there's no need for us
	     * to hold a lock.
	     */
	    Statement cmd = new SnapshotStatement(
                    lock.getConnection().createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
            try {
                ResultSet rs = cmd.executeQuery("SELECT * FROM"
                        + " repositoryHoldings WHERE site_id="
                        + siteManager.localSiteId + ";");
		lock.release();

		/*
		 * Iterate over the holdings snapshot to visit every primary
		 * repository directory.  Yield periodically because this is a
		 * long-running task and we want to be a good citizen.
		 */
		while (rs.next()) {
		    scanSingleDirectory(new RepositoryHoldingInfo(rs), stats);
		    Thread.yield();
		    if (cancel.isSet()) {
                        siteManager.recordLogRecord(
                                LogRecordGenerator.repositoryScanCanceled(
                                stats.getChangedFileCount(),
                                stats.getChangedSampleCount()));
			return;
		    }
		}
	    } finally {
		cmd.close();
	    }

            // Normal completion.
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanFinished(
                    stats.getChangedFileCount(),
                    stats.getChangedSampleCount()));
        } catch (Exception ex) {
            // ensure that unexpected exceptions are logged
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanFailed(
                    stats.getChangedFileCount(), 
                    stats.getChangedSampleCount(), ex));
        }
    }

    private void scanSingleDirectory(RepositoryHoldingInfo holding,
            ScanResult result) throws DeadlockDetectedException, SQLException {
        Collection<RepositoryFileInfo> filesToAdd
                = new ArrayList<RepositoryFileInfo>();
        Collection<RepositoryFileInfo> filesToDeactivate
                = new ArrayList<RepositoryFileInfo>();
        /*
         * Acquire a new lock we'll hold for the duration of this single
         * directory.
         */
        AbstractLock myLock =
                RepositoryLocks.primaryDirectoryScanSingle(holding.sampleId);
        lockAgent.registerLock(myLock);
        myLock.acquire();

        try {
            SampleInfo sample = sampleManager.getSampleInfo(
                    holding.sampleId,
                    SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID,
                    myLock);
            LabInfo lab = siteManager.getLabInfo(sample.labId);
            SecondaryDirectory dirAsExpected = getSecondaryDirectoryInfo(
                    myLock.getConnection(), sample.id, sample.historyId);

            /*
             * Scan the filesystem directory and receive a report back about
	     * any detected modifications.
             */
            primaryDirectoryAgent.scanDirectory(
                    new PrimaryDirectory(sample, lab, holding),
                    dirAsExpected, filesToAdd, filesToDeactivate);

            if (!filesToAdd.isEmpty() || !filesToDeactivate.isEmpty()) {

                /*
                 * At least one modification was detected. Append an entry to
                 * the sample's history log to describe the file changes.
                 */
                SampleInfo newSample = sampleManager.putSampleInfo(
                        sample, SampleWorkflowBL.DETECTED_FILE_CHANGES,
                        new Date(), UserInfo.INVALID_USER_ID, null, myLock);

                /*
                 * Deactivate rows in the 'repositoryFiles' table to reflect
                 * deleted/modified files.
                 */
                for (RepositoryFileInfo file : filesToDeactivate) {
                    dbDeactivateFile(myLock.getConnection(), file.id,
                            newSample.historyId);
                    result.incrementChangedFileCount();
                }

                /*
                 * Add rows to the 'repositoryFiles' table to reflect
                 * added/modified files.
                 */
                for (RepositoryFileInfo file : filesToAdd) {
                    file.firstSampleHistoryId = newSample.historyId;
                    file.originalSampleHistoryId = newSample.historyId;
                    dbAddFile(myLock.getConnection(), file);
                    result.incrementChangedFileCount();
                }

                // Clean up.
                invalidateSecondaryDirsCache(newSample.id);
                result.incrementChangedSampleCount();
            }
        } catch (OperationFailedException ofe) {
            // log it and attempt to continue
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanException(
                            holding.urlPath, ofe));
        } catch (InconsistentDbException ide) {
            // log it and attempt to continue
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanException(
                            holding.urlPath, ide));
        } catch (OperationNotPermittedException onpe) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanException(
                            holding.urlPath, onpe));
        } catch (InvalidDataException ide) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryScanException(
                            holding.urlPath, ide));
        } finally {
	    myLock.release();
	}
    }
    
    /**
     * Called by Sample Manager to request that a complete set of
     * SampleHoldingCM's be generated and sent back to it asynchronously. In so
     * doing, we will have informed Sample Manager about every sample for which
     * data is held locally.
     * 
     * @throws DeadlockDetectedException
     * @throws OperationFailedException
     */
    public void requestHoldingsDump() throws DeadlockDetectedException,
            OperationFailedException {
        /*
         * We always do a direct-from-database query because there's no
         * guarantee that every db row is in the cache.
         */

        // Get a lock.
        AbstractLock lock = RepositoryLocks.holdingsDump();
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            String sql = "SELECT sample_id, replicaLevel"
                    + " FROM repositoryHoldings" + " WHERE site_id="
                    + siteManager.localSiteId + ";";
            Statement cmd = lock.getConnection().createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            
            try {
                ResultSet rs = cmd.executeQuery(sql);
                while (rs.next()) {
                    int sampleId = rs.getInt("sample_id");
                    int replicaLevel = rs.getInt("replicaLevel");
                    
                    sampleManager.passCoreMessage(
                            new SampleHoldingCM(sampleId, replicaLevel));
                }
            } finally {
                cmd.close();
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }
    }

    /**
     * Called by {@code PrimaryFileWriteTicket} upon completion of a file
     * upload operation. One or more files can be committed at once, provided
     * they all belong to the same sample. This method promotes the temporary
     * uploaded files to real sample data files and updates the database.
     * <p>
     * The caller is responsible for acquiring an appropriate filesystem lock
     * before invoking this method and not releasing that lock until this method
     * returns.
     * 
     * @param primaryDirectoryInfo describes the primary repository directory in
     *        which the caller has created temporary files and in which real
     *        sample data files should be created.
     * @param sample a {@code SampleInfo} object, possibly modified by
     *        the caller, for the same sample that
     *        {@code primaryDirectoryInfo} describes. This value is
     *        passed transparently to {@code SampleManager.putSampleInfo()}.
     * @param userId identifies the user who should be credited with performing
     *        the workflow action. This value is passed transparently to
     *        {@code SampleManager.putSampleInfo()}.
     * @param comments human-readable comments attached to the workflow action.
     *        The value is passed transparently to
     *        {@code SampleManager.putSampleInfo()}.
     * @param actionCode used to identify the kind of workflow action to be
     *        performed. The value is passed transparently to
     *        {@code SampleManager.putSampleInfo()}.
     * @param fileNames an array of one or more file names (without any path
     *        components) that this method should use when creating real sample
     *        data files within the specified primary repository directory.
     * @param tempFiles identifies temporary data files currently sitting within
     *        the specified primary repository directory that should be promoted
     *        to real sample data files by this method. There is a one-to-one
     *        correspondence between elements of {@code tempFiles} and
     *        elements of {@code fileNames}.
     * @param descriptions an array of one or more desired descriptions that
     *        this method should use when creating real sample data files within
     *        the specified primary repository directory
     * @throws IllegalArgumentException if the length of the
     *         {@code fileNames} array does not match the length of the
     *         {@code tempFiles} array.
     * @throws OperationFailedException on low-level error.
     * @throws ResourceNotAccessibleException if there was a permissions problem
     *         while attempting to manipulate one of the specified
     *         {@code tempFiles}.
     */
    public void notifyFileUploadsFinished(
            PrimaryDirectory primaryDirectoryInfo, SampleInfo sample,
            int userId, String comments, int actionCode, String fileNames[],
            File tempFiles[], String descriptions[])
            throws OperationFailedException, ResourceNotAccessibleException {
        if (fileNames.length != tempFiles.length) {
            throw new IllegalArgumentException();
        }

        try {
            /*
             * Obtain a lock that will let us do database manipulation. We
             * assume that the caller continues to hold file-write tickets on
             * each of the files he told us about, so we don't bother to lock
             * the filesystem.
             */
            AbstractLock lock = RepositoryLocks.afterFileUpload(sample.id,
                    fileNames, userId);

            lockAgent.registerLock(lock);
            lockAgent.acquireLock(lock, true);
            try {

                // Manipulate the filesystem for each finished file.
                RepositoryFileInfo fileInfos[]
                        = new RepositoryFileInfo[fileNames.length];
                
                for (int i = 0; i < fileNames.length; i++) {
                    // Promote the temp file to a real file and commit it to CVS
                    File realFile = primaryDirectoryAgent.getFile(
                            primaryDirectoryInfo, fileNames[i]);

                    fileInfos[i] = primaryDirectoryAgent.promoteTempFile(
                            primaryDirectoryInfo, fileNames[i], tempFiles[i],
                            descriptions[i]);

                    /*
                     * Fix permissions on the new file. It's necessary to invoke
                     * a shell script to do the dirty work for us.
                     */
                    CoreProcessWrapper proc = new CoreProcessWrapper(
                            new String[] {
                                    properties.getProperty("RepScriptFixFile"),
                                    realFile.getParent() + File.separator,
                                    fileNames[i] });
                    siteManager.recordLogRecord(proc.getLogRecord());
                    proc.waitFor(true);
                }

                /*
                 * Append an entry to the sample's history log to describe the
                 * file upload(s).
                 */
                SampleInfo newSample = sampleManager.putSampleInfo(sample,
                        actionCode, new Date(), userId, comments, lock);

                /*
                 * Add new rows to the 'repositoryFiles' table. Optionally
                 * deactivate any previous row for the same file that might have
                 * existed.
                 */
                for (int i = 0; i < fileNames.length; i++) {
                    fileInfos[i].firstSampleHistoryId = newSample.historyId;
                    fileInfos[i].originalSampleHistoryId = newSample.historyId;
                    dbDeactivateFile(lock.getConnection(), sample.id,
                            fileNames[i], newSample.historyId);
                    dbAddFile(lock.getConnection(), fileInfos[i]);
                    invalidateSecondaryDirsCache(sample.id);
                }
            } finally {
                lock.release();
            }
        } catch (ProcessIncompleteException ex) {
            throw new OperationFailedException(ex);
        } catch (DuplicateDataException ex) {
            throw new UnexpectedExceptionException(ex);
        } catch (InconsistentDbException ex) {
            throw new OperationFailedException(ex);
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } catch (InvalidDataException ex) {
            throw new OperationFailedException(ex);
        } catch (InvalidModificationException ex) {
            throw new OperationFailedException(ex);
        } catch (WrongSiteException ex) {
            throw new OperationFailedException(ex);
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Update task called during upgrade from recipnet-0.6.0 to recipnet-0.6.1.
     * Registers all existing repository directories as primary repository
     * directories. Initializes the CVS repository also and secondary directory
     * area also.
     */
    public void registerExistingDirectories() throws DeadlockDetectedException,
            DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, SQLException, WrongSiteException {
        // Get a lock.
        AbstractLock lock = RepositoryLocks.registerExistingDirectories();

        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            CvsInvoker cvsInvoker = new CvsInvoker(new File(
                    properties.getProperty("RepCvsCommand")), new File(
                    properties.getProperty("RepCvsDirectory")),
                    siteManager.logger);

            primaryDirectoryAgent = new PrimaryDirectoryAgent(
                    "http://dummy",
                    new File(properties.getProperty("RepBaseDirectory")),
                    properties.getProperty("RepScriptCreateDir"),
                    properties.getProperty("RepScriptDirSize"),
                    properties.getProperty("RepUploadedFilePrefix"),
                    cvsInvoker, siteManager);

            // Initialize the CVS repository.
            cvsInvoker.initRepository(true);

            // Initialize the secondary directory area
            SecondaryDirectoryAgent.createArea(new File(
                    properties.getProperty("RepBaseDirectory")));

            /*
             * Iterate through all local holding records and register every
             * existing repository directory as a "primary directory" under the
             * new system.
             */
            Statement cmd = lock.getConnection().createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            try {
                ResultSet rs = cmd.executeQuery(
                        "SELECT * FROM repositoryHoldings WHERE site_id="
                        + siteManager.localSiteId + ";");
                while (rs.next()) {
                    // Fetch the relevant container objects.
                    RepositoryHoldingInfo holding
                            = new RepositoryHoldingInfo(rs);
                    SampleInfo sample = sampleManager.getSampleInfo(
                            holding.sampleId,
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
                    LabInfo lab = siteManager.getLabInfo(sample.labId);

                    // Register the directory.
                    Collection<RepositoryFileInfo> filesToAdd
                            = primaryDirectoryAgent.createDirectory(
                                    new PrimaryDirectory(sample, lab, holding));
                    
                    if (!filesToAdd.isEmpty()) {
                        /*
                         * At least one file already existed in the primary
                         * directory. Update the sample's history log and insert
                         * rows into 'repositoryFiles'.
                         */
                        SampleInfo newSample = sampleManager.putSampleInfo(
                                sample,
                                SampleWorkflowBL.DETECTED_PREEXISTING_FILES,
                                new Date(), UserInfo.INVALID_USER_ID, null,
                                lock);
                        
                        for (RepositoryFileInfo file : filesToAdd) {
                            file.firstSampleHistoryId = newSample.historyId;
                            file.originalSampleHistoryId = newSample.historyId;
                            dbAddFile(lock.getConnection(), file);
                        }
                    }

                    // Send a status update to the user.
                    System.out.print(".");
                }
            } finally {
                cmd.close();
            }
        } finally {
            lock.release();
        }
    }

    /**
     * @param labInfo identifies which Lab's repository directory size is to be
     *        returned.
     * @return the number of KB in the given subdirectory in the repository.
     * @throws OperationFailedException when a low level error occurs.
     */
    public long getLabDirectorySize(LabInfo labInfo)
            throws OperationFailedException {
        return primaryDirectoryAgent.getLabDirectorySize(labInfo.directoryName);
    }



    /**************************************************************************
     * INTERNAL HELPER METHODS (not remotely accessible)
     *************************************************************************/



    /**
     * Accepts a {@code CoreMessage} that is addressed to Repository
     * Manager, processes it, and possibly updates Repository Manager's internal
     * state or database tables. This is a dispatcher method: it delegates to
     * other methods named {@code event...()} depending upon the type of
     * {@code msg}.
     * <p>
     * If {@code msg} is of type {@code InterSiteMessage} then at
     * the completion of message processing, this method generates a
     * {@code ProcessedIsmCM} and inserts it into Site Manager's message
     * queue to record the outcome. Such asynchronous notifications fit Site
     * Manager's model of asynchronous ISM processing.
     * <p>
     * In the case where one of the {@code event...()} methods this
     * method dispatches to throws an exception, the exception is logged in all
     * cases. If Repository Manager is in bootstrap mode, this implies that this
     * method was invoked synchronously by a user-interactive thread, so the
     * exception is re-thrown by this method for eventual display to the user.
     * If Repository Manager is not in bootstrap mode, then the failure is
     * reported to {@code SiteManager} (if {@code msg} is an
     * {@code InterSiteMessage}) and nothing more is done because this
     * method was probably invoked by a worker thread (rather than a user
     * thread), and has no mechanism other than the logfile for communicating
     * with the user.
     * 
     * @param msg the core message to be processed.
     * @throws OperationFailedException only if Repository Manager was
     *         initialized in bootstrap mode, and then only if an error was
     *         encountered while processing {@code msg}. The nested
     *         {@code Exception} will be the one thrown by whatever
     *         {@code event...()} method was dispatched to.
     */
    private void processCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        try {
            if (msg instanceof PingCM) {
                eventPing((PingCM) msg);
            } else if (msg instanceof LocalLabsChangedCM) {
                eventLocalLabsChanged((LocalLabsChangedCM) msg);
            } else if (msg instanceof SampleStatusHintCM) {
                eventSampleStatusHint((SampleStatusHintCM) msg);
            } else if (msg instanceof RepositoryHoldingISM) {
                eventRepositoryHolding((RepositoryHoldingISM) msg);
            } else if (msg instanceof UnusedSecondaryDirectoryHintCM) {
                eventUnusedSecondaryDirectoryHint(
                        (UnusedSecondaryDirectoryHintCM) msg);
	    } else if (msg instanceof InvalidateHoldingsCM) {
		eventInvalidateHoldings((InvalidateHoldingsCM) msg);
            } else {
                siteManager.recordLogRecord(LogRecordGenerator.cmUnknownType(
                        this, msg));
            }
        } catch (Exception ex) {
            // Generic catch-all for any sort of exception. This method is
            // called from a worker thread, not a user thread, so the only
            // means for us to report an exception is to log it.
            siteManager.recordLogRecord(LogRecordGenerator.cmCausedException(
                    this, msg, ex));

            // Report the error. We choose an appropriate notification
            // channel depending on whether Site Manager is in bootstrap mode
            // or not and the type of the message.
            if (bootstrapMode) {
                // Report the error synchronously to calling code via Java's
                // usual exception mechanism.
                throw new OperationFailedException(ex);
            } else if (msg instanceof InterSiteMessage) {
                // Send a special failure message to ReceivedMessageAgent (via
                // our message queue) in order to avoid completely stalling
                // ISM processing.
                siteManager.passCoreMessage(ProcessedIsmCM.failure(
                        (InterSiteMessage) msg, ex));
            } else {
                // Treat the exception as nonfatal by doing nothing more. The
                // exception was logged previously.
            }
        }
    }

    /** Executed when a PingCM is received */
    private void eventPing(@SuppressWarnings("unused") PingCM msg) {
        pingSignal.send();
    }

    /** Executed whenever Site Manager's list of local labs has changed */
    private void eventLocalLabsChanged(
            @SuppressWarnings("unused") LocalLabsChangedCM msg)
            throws DeadlockDetectedException, OperationFailedException {
        AbstractLock lock = RepositoryLocks.modifyLocalLabs();
        
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            localLabs.clear();
            for (int labId : siteManager.getLocalLabs()) {
                localLabs.add(labId);
            }
        } finally {
            lock.release();
        }
    }

    /**
     * Executed when a {@code SampleStatusHintCM} arrives from Sample
     * Manager.
     */
    private void eventSampleStatusHint(SampleStatusHintCM msg)
	    throws InconsistentDbException, InvalidDataException, 
	    InvalidModificationException, OperationFailedException,
            WrongSiteException {
	switch (msg.trigger) {
	    case SAMPLE_NEWLY_PUBLIC:
		// Generate a RepositoryHoldingISM that will advertise our
		// holding level.
		siteManager.passCoreMessage(new SendIsmCM(
                        new RepositoryHoldingISM(siteManager.localSiteId, 
			msg.sampleId, 
                        this.getLocalHoldingLevel(msg.sampleId))));
		break;
	    case SAMPLE_NEWLY_NONPUBLIC:
                // Generate a RepositoryHoldingISM for NO_DATA -- this
		// effectively un-advertises the data directory now that the
		// sample is no longer public.
		siteManager.passCoreMessage(new SendIsmCM(
                        new RepositoryHoldingISM(siteManager.localSiteId, 
                        msg.sampleId, RepositoryHoldingInfo.NO_DATA)));
		break;
	    case LOCAL_SITE_NEWLY_AUTHORITATIVE:
		// Elevate the local holding level to FULL_DATA and announce
		// the change.
		this.elevateLocalHoldingLevel(msg.sampleId, 
			RepositoryHoldingInfo.FULL_DATA);
		break;
	    case LOCAL_SITE_NEWLY_NONAUTHORITATIVE:
		// Decrease the local holding level to NO_DATA and announce
		// the change.
		this.decreaseLocalHoldingLevel(msg.sampleId,
			RepositoryHoldingInfo.NO_DATA);
		break;
	}

        // Relay the hinter's ISM for transmission.
        if (msg.ismToSend != null) {
            siteManager.passCoreMessage(new SendIsmCM(msg.ismToSend));
        }
    }

    /**
     * Executed when a RepositoryHoldingISM arrives from another site and needs
     * to be processed. A corresponding record in
     * {@code repositoryHoldings} is added, modified, or deleted, as
     * specified by the ISM.
     * 
     * @throws DeadlockDetectedException
     * @throws InconsistentDbException
     * @throws OperationFailedException
     * @throws ResourceNotFoundException if the message's specified sample id or
     *         associatd lab is not known.
     * @throws UnexpectedExceptionException
     */
    private void eventRepositoryHolding(RepositoryHoldingISM msg)
            throws DeadlockDetectedException, InconsistentDbException,
            IsmProcessingException, OperationFailedException,
            ResourceNotFoundException {
        // Get a lock.
        AbstractLock lock = RepositoryLocks.modifyHolding(msg.sampleId);
        lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Validate the incoming ISM.
            SampleInfo sample = sampleManager.getSampleInfo(msg.sampleId,
                    SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
            
            siteManager.getLabInfo(sample.labId);

            // Proceed with the update.
            updateHoldingRecord(lock.getConnection(), msg.sampleId,
                    msg.sourceSiteId, msg.replicaLevel, null);
        } finally {
            lock.release();
        }

        siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded holding level change to " + msg.replicaLevel
                        + " for sample " + msg.sampleId + " at site "
                        + msg.sourceSiteId + "."));
    }

    /**
     * Executed when an UnusedSecondaryDirectoryHintCM is received. This
     * implementation calls {@code considerDeletingDirectory} on
     * {@code SecondaryDirectoryAgent}.
     */
    private void eventUnusedSecondaryDirectoryHint(
            UnusedSecondaryDirectoryHintCM msg) {
        secondaryDirectoryAgent.considerDeletingDirectory(msg.directoryKey);
    }

    /**
     * Executed when an InvalidateHoldingsCM is received.  This implementation
     * deletes all the stored/cached holding records associated with some
     * particular remote site.
     */
    private void eventInvalidateHoldings(InvalidateHoldingsCM msg)
	    throws DeadlockDetectedException, OperationFailedException {
	// Get a lock.
	AbstractLock lock = RepositoryLocks.modifyAllHoldings();
	lockAgent.registerLock(lock);
	lock.acquire();

	try {
	    Statement cmd = lock.getConnection().createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
	    String sql = "SELECT * FROM repositoryHoldings WHERE site_id="
                    + msg.siteId + ";";
	    int cHoldings = 0;
	    try {
		ResultSet rs = cmd.executeQuery(sql);
		while (rs.next()) {
		    RepositoryHoldingInfo holding 
                            = new RepositoryHoldingInfo(rs);
		    holdingsCache.invalidate(holding.sampleId);
		    rs.deleteRow();
		    cHoldings ++;
		}
	    } finally {
		cmd.close();
	    }

	    // Log something for debugging.
            siteManager.recordLogRecord(
                    LogRecordGenerator.repositoryHoldingsInvalidated(
		    msg.siteId, cHoldings));
	} catch (SQLException ex) {
	    throw new OperationFailedException(ex);
	} finally {
	    lock.release();
	}
    }

    /**
     * Elevates the local holding level for some sample to a particular new 
     * level.  Particular steps include each of the following, as applicable:
     *    1. create a primary directory
     *    2. writes a holding record
     *    3. updates the holding records cache
     *    4. generates a RepositoryHoldingISM
     *    5. sends a SampleHoldingCM
     */
    private void elevateLocalHoldingLevel(int sampleId, int newLevel) 
	    throws InconsistentDbException, InvalidDataException, 
	    InvalidModificationException, OperationFailedException,
            WrongSiteException {
        try {
            AbstractLock lock 
                    = RepositoryLocks.elevateLocalHoldingLevel(sampleId);
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
		if (holding != null && holding.replicaLevel >= newLevel) {
		    // Nothing to do.  Exit gracefully...
		} else if (holding == null || holding.replicaLevel 
			   == RepositoryHoldingInfo.NO_DATA) {
		    // Create a primary directory using the defaults.
		    this.createDataDirectory(sampleId, null, 
                            UserInfo.INVALID_USER_ID, lock);
		} else {
		    // Record the new holding level.
		    this.updateHoldingRecord(lock.getConnection(), 
                            sampleId, this.siteManager.localSiteId,
                            newLevel, holding.urlPath);

		    // Perhaps announce the new holding level.
		    SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
		    if (sample.isPublic()) {
			siteManager.passCoreMessage(new SendIsmCM(
                                new RepositoryHoldingISM(
                                siteManager.localSiteId, sampleId, newLevel)));
		    }
		}
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Decreases the local holding level for some sample to a particular new
     * level.  Particular steps include ach of the following, as applicable:
     *   1. writes a holding record
     *   2. updates the holding records cache
     *   3. generates a RepositoryHoldingISM
     *   4. sends a SampleHoldingCM
     */
    private void decreaseLocalHoldingLevel(int sampleId, int newLevel)
	    throws InconsistentDbException, OperationFailedException {
        try {
            AbstractLock lock 
                    = RepositoryLocks.decreaseLocalHoldingLevel(sampleId);
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                RepositoryHoldingInfo holding = dbFetchLocalHoldingForSample(
                        lock.getConnection(), sampleId);
		if (holding == null || holding.replicaLevel <= newLevel) {
		    // Nothing to do.  Exit gracefully...
		} else {
		    // Record the new holding level.
		    this.updateHoldingRecord(lock.getConnection(), 
                            sampleId, this.siteManager.localSiteId,
                            newLevel, holding.urlPath);

		    // Perhaps announce the new holding level.
		    SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);
		    if (sample.isPublic()) {
			siteManager.passCoreMessage(new SendIsmCM(
                                new RepositoryHoldingISM(
                                siteManager.localSiteId, sampleId, newLevel)));
		    }
		}
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Helper function that updates Repository's Manager's holding record with
     * respect to the specified sample and site. Creates a new holding record,
     * modifies an existing holding record, or deletes a previous holding
     * record, as necessary. Updates both the database and the cache.
     * 
     * @throws OperationFailedException on database error.
     */
    private RepositoryHoldingInfo updateHoldingRecord(Connection conn,
            int sampleId, int siteId, int replicaLevel, String urlPath)
            throws OperationFailedException {
        RepositoryHoldingInfo holding = new RepositoryHoldingInfo(sampleId,
                siteId, replicaLevel, urlPath);

        // Update the database.
        try {
            if (replicaLevel == RepositoryHoldingInfo.NO_DATA) {
                // Delete the corresponding row in repositoryHoldings, if one
                // exists.
                dbDeleteHolding(conn, sampleId, siteId);
            } else {
                // Add or update a row in repositoryHoldings.
                dbAddReplaceHolding(conn, holding);
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }

        // Update the caches
        holdingsCache.invalidate(sampleId);

        // For a local holding, send a CM to SampleManager to inform it of the
        // change.
        if (siteId == siteManager.localSiteId) {
            sampleManager.passCoreMessage(new SampleHoldingCM(sampleId,
                    replicaLevel));
        }

        return holding;
    }

    /* it interfaces with the cache!! */
    private SecondaryDirectory getSecondaryDirectoryInfo(Connection conn,
            int sampleId, int sampleHistoryId) throws SQLException {
        // Check the cache first.
        SecondaryDirectory dir = secondaryDirsCache.get(
                SecondaryDirectory.getKey(sampleId, sampleHistoryId));
        
        if (dir != null) {
            return dir;
        }

        // Fetch a bunch of RepositoryFiles from the db and put the result
        // into the cache for future use.
        dir = new SecondaryDirectory(sampleId, sampleHistoryId,
                dbFetchFilesForSample(conn, sampleId, sampleHistoryId));
        secondaryDirsCache.put(dir.getKey(), dir);

        return dir;
    }

    private void invalidateSecondaryDirsCache(int sampleId) {
        for (SecondaryDirectory secondaryDirectoryInfo
                : secondaryDirsCache.getAll(SecondaryDirectory.class)) {
            if (secondaryDirectoryInfo.sampleId == sampleId) {
                secondaryDirsCache.invalidate(secondaryDirectoryInfo.getKey());
            }
        }
    }

    /**
     * Returns an array of int's representing Holding ID numbers that describe
     * the specified sample. The caller may present these Holding ID numbers to
     * dbFetchHolding() to obtain the corresponding RepositoryHoldingInfo
     * objects. Returns an array of length 0 if no holdings were found for the
     * specified sample number. This method should be called only after a cache
     * miss has occurred.
     * 
     * @throws SQLException
     */
    private static Collection<RepositoryHoldingInfo> dbFetchHoldingsForSample(
            Connection conn, int sampleId) throws SQLException {
        Collection<RepositoryHoldingInfo> holdings
                = new ArrayList<RepositoryHoldingInfo>();
        String sql = "SELECT * FROM repositoryHoldings WHERE sample_id="
                + sampleId + ";";
        Statement cmd = conn.createStatement();
        
        try {
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next()) {
                holdings.add(new RepositoryHoldingInfo(rs));
            }
        } finally {
            cmd.close();
        }

        return holdings;
    }

    /**
     * @return the holdingId of the newly-inserted row, or if a row already
     *         existed, the existing row's holdingId
     * @throws SQLException
     */
    private static int dbAddReplaceHolding(Connection conn,
            RepositoryHoldingInfo holding) throws SQLException {
        int id;

        // See if a holding record exists already for this site id and
        // sample id
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        
        try {
            String sql = "SELECT * FROM repositoryHoldings WHERE site_id="
                    + holding.siteId + " AND sample_id=" + holding.sampleId;
            ResultSet rs = cmd.executeQuery(sql);
            
            if (rs.next()) {
                // A matching row already exists. Replace its data.
                id = rs.getInt("id");
                holding.id = id;
                holding.dbStore(rs);
                rs.updateRow();
            } else {
                // No matching row exists. Create a new row.
                rs.moveToInsertRow();
                holding.dbStore(rs);
                rs.insertRow();

                // Hack! This is an unsupported way to get the id of the
                // row we just inserted.
                rs.next();
                id = rs.getInt("id");
            }
        } finally {
            cmd.close();
        }

        return id;
    }

    /** @throws SQLException */
    private static void dbDeleteHolding(Connection conn, int holdingId)
            throws SQLException {
        Statement cmd = conn.createStatement();
        
        try {
            String sql = "DELETE FROM repositoryHoldings" + " WHERE id="
                    + holdingId + ";";
            
            cmd.executeUpdate(sql);
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function that deletes the holding record for the specified
     * sample and site, if one exists. Returns the holdingId of the now-deleted
     * row, if one existed, or INVALID_REPOSITORY_HOLDING_ID if not.
     * 
     * @throws SQLException
     */
    private static int dbDeleteHolding(Connection conn, int sampleId, int siteId)
            throws SQLException {
        int holdingId = RepositoryHoldingInfo.INVALID_REPOSITORY_HOLDING_ID;

        // See if a holding record exists for this site id and
        // sample id.
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        
        try {
            String sql = "SELECT * FROM repositoryHoldings WHERE site_id="
                    + siteId + " AND sample_id=" + sampleId;
            ResultSet rs = cmd.executeQuery(sql);
            
            if (rs.next()) {
                // A matching row exists. Record its id and delete it.
                holdingId = rs.getInt("id");
                rs.deleteRow();
            }
        } finally {
            cmd.close();
        }

        return holdingId;
    }

    /**
     * Returns the RepositoryHoldingInfo object representing the holding for the
     * specified sample at the local site. Returns null if no such holding is
     * present in the local site's repository.
     */
    private RepositoryHoldingInfo dbFetchLocalHoldingForSample(Connection conn,
            int sampleId) throws SQLException {

        /*
         * TODO: many RMI-accessible functions call this function directly.
         * Performance might be improved if we wrote a caching wrapper around it
         */
        
        String sql = "SELECT * FROM repositoryHoldings" + " WHERE sample_id="
                + sampleId + " AND site_id=" + siteManager.localSiteId + ";";
        Statement cmd = conn.createStatement();
        
        try {
            ResultSet rs = cmd.executeQuery(sql);

            return (rs.next() ? new RepositoryHoldingInfo(rs) : null);
        } finally {
            cmd.close();
        }
    }

    /**
     * @param conn a connection to the database
     * @param sampleId the id of the sample whose data files are requested
     * @param sampleHistoryId the historyId for the sample-version whose data
     *        files are requested or {@code INVALID_SAMPLE_HISTORY_ID} if
     *        files for all sample-versions are requested
     * @return a {@code Collection} of {@code RepositoryFileInfo}
     *         objects that describes the set of files that existed in the
     *         repository for the specified sample, as of the specified
     *         historyId. The list is empty if no such files existed.
     * @throws SQLException on database error.
     */
    private static Collection<RepositoryFileInfo> dbFetchFilesForSample(
            Connection conn, int sampleId, int sampleHistoryId)
            throws SQLException {
        Collection<RepositoryFileInfo> files
                = new ArrayList<RepositoryFileInfo>();

        String sql = "SELECT * FROM repositoryFiles"
                + " WHERE sample_id = "
                + sampleId
                + (sampleHistoryId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID 
                        ? (" AND first_sampleHistory_id <= "
                                + sampleHistoryId
                                + " AND (last_sampleHistory_id > "
                                + sampleHistoryId
                                + " OR last_sampleHistory_id IS NULL);")
                        : ";");
        Statement cmd = conn.createStatement();
        
        try {
            ResultSet rs = cmd.executeQuery(sql);
            
            while (rs.next()) {
                files.add(new RepositoryFileInfo(rs));
            }
        } finally {
            cmd.close();
        }

        return files;
    }

    /**
     * Deactivates an existing row in the {@code repositoryFiles} table.
     */
    private static void dbDeactivateFile(Connection conn, int fileId,
            int lastSampleHistoryId) throws SQLException {
        Statement cmd = conn.createStatement();
        
        try {
            cmd.executeUpdate("UPDATE repositoryFiles SET last_sampleHistory_id="
                    + lastSampleHistoryId + " WHERE id=" + fileId + ";");
        } finally {
            cmd.close();
        }
    }

    /**
     * Deactivates any existing active rows in the {@code repositoryFiles}
     * table associated with the specified sample and file name.
     */
    private static void dbDeactivateFile(Connection conn, int sampleId,
            String fileName, int lastSampleHistoryId) throws SQLException {
        PreparedStatement cmd = conn.prepareStatement("UPDATE repositoryFiles"
                + " SET last_sampleHistory_id=?"
                + " WHERE sample_id=? AND fileName=?"
                + " AND last_sampleHistory_id is NULL;");
        
        try {
            cmd.setInt(1, lastSampleHistoryId);
            cmd.setInt(2, sampleId);
            cmd.setString(3, fileName);
            cmd.execute();
        } finally {
            cmd.close();
        }
    }

    /**
     * Inserts a new row into the {@code repositoryFiles} table.
     */
    private static int dbAddFile(Connection conn, RepositoryFileInfo file)
            throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        
        try {
            String sql = "SELECT * FROM repositoryFiles WHERE id=0;";
            ResultSet rs = cmd.executeQuery(sql);
            rs.moveToInsertRow();
            
            file.dbStore(rs);
            rs.insertRow();

            // Hack! This is an unsupported way to get the id of the
            // row we just inserted.
            rs.next();
            
            return rs.getInt("id");
        } finally {
            cmd.close();
        }
    }

    /**
     * Deletes all rows in the {@code repositoryFiles} table for a
     * particular sample; particularly useful during an eradication.
     */
    private static void dbDeleteFiles(Connection conn, int sampleId,
            String fileName) throws SQLException {
        PreparedStatement cmd = conn.prepareStatement(
                "DELETE FROM repositoryFiles WHERE sample_id=? AND fileName=?;");
        
        try {
            cmd.setInt(1, sampleId);
            cmd.setString(2, fileName);
            cmd.execute();
        } finally {
            cmd.close();
        }
    }

    /**
     * Maintains statistics about the progress of a repository scan
     *
     * @author jobollin
     * @version 0.9.0
     */
    private static class ScanResult {
        private int changedFileCount = 0;
        private int changedSampleCount = 0;
        
        /**
         * Initializes a new {@code ScanResult}
         */
        public ScanResult() {
            // do nothing
        }

        /**
         * Returns the changedFileCount
         *
         * @return the changedFileCount
         */
        public int getChangedFileCount() {
            return changedFileCount;
        }

        /**
         * Increments the count of changed files
         */
        public void incrementChangedFileCount() {
            changedFileCount++;
        }

        /**
         * Returns the changedSampleCount
         *
         * @return the changedSampleCount
         */
        public int getChangedSampleCount() {
            return changedSampleCount;
        }

        /**
         * Increments the count of changed samples
         */
        public void incrementChangedSampleCount() {
            changedSampleCount++;
        }
    }
}
