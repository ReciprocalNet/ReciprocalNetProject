/*
 * Reciprocal Net project
 * 
 * SampleManager.java
 *
 * 17-Jun-2002: leqian wrote first draft
 * 25-Jun-2002: ekoperda added db access code
 * 12-Jul-2002: ekoperda added configuration properties support and bootstrap
 *              mode support
 * 18-Jul-2002: ekoperda fixed bug #181
 * 23-Jul-2002: ekoperda coded revertSampleToVersion() method
 * 25-Jul-2002: ekoperda fixed bug #200
 * 26-Jul-2002: ekoperda fixed bug #203
 * 06-Aug-2002: ekoperda fixed bug #271
 * 07-Aug-2002: ekoperda fixed bug #277 in putSampleInfo()
 * 07-Aug-2002: ekoperda fixed bug #288 by adding support for
 *              the mostRecentStatus field in the SampleInfo container
 * 08-Aug-2002: ekoperda added performance-monitoring code
 * 09-Aug-2002: ekoperda wrote dbFetchMultipleSamples() and modified
 *              getSearchResults()
 * 15-Aug-2002: ekoperda modified locking mechanisms to support the updated
 *              SampleLockAgent class.
 * 16-Aug-2002: ekoperda added a second version of getSearchResults()
 * 19-Aug-2002: ekoperda made a minor change to the cache addition policy in
 *              getSearchResults()
 * 26-Aug-2002: ekoperda added lookupSampleId() function.
 * 27-Aug-2002: ekoperda added periodic task support
 * 30-Aug-2002: ekoperda removed all references to the old sampleAtoms table,
 *              which has been deleted.
 * 30-Aug-2002: ekoperda added support for atom searching, plus other general
 *              search-optimization features
 * 04-Sep-2002: jobollin added a ParseException catch clause to
 *              updateSearchAtoms()
 * 04-Sep-2002: ekoperda added general logging support throughout
 * 06-Sep-2002: ekoperda fixed bug #422
 * 11-Sep-2002: ekoperda added rebuildSearchAtoms() method and rearranged
 *              the sample id block management code to properly support a
 *              rebuild operation.
 * 18-Sep-2002: ekoperda added rebuildSampleData() method
 * 27-Sep-2002: ekoperda changed import statements to reflect class
 *              rearrangements
 * 08-Oct-2002: ekoperda changed processCoreMessage() to pass a ProcessedIsmCM
 *              back to Site Manager on success
 * 15-Oct-2002: ekoperda added support for the new search index table
 *              'searchLocalHoldings'
 * 18-Oct-2002: ekoperda restructured the locking code throughout in order to
 *              fix bug #550; changed return type of verifySampleNumbers(),
 *              renamed getAllOwnedSamples() to getAllAuthoritativeSamples(),
 *              added second version of putSampleInfo() that takes action date
 *              as a parameter
 * 21-Oct-2002: ekoperda added function getEarliestAuthoritativeHistoryDate()
 * 25-Oct-2002: ekoperda moved all sample id-tracking logic to the new class
 *              SampleIdAgent and made method passCoreMessage() public
 * 01-Nov-2002: ekoperda fixed bug #569 in getMultipleSampleInfo()
 * 04-Nov-2002: ekoperda changed start() function so that the sampleIdAgent is
 *              initialized even in bootstrap mode; added generateInitialIsms()
 * 05-Nov-2002: ekoperda modified getSampleInfo(), dbFetchSample(),
 *              and dbFetchMultipleSamplesWithOr() to support SampleInfo's 3
 *              new fields
 * 07-Nov-2002: ekoperda fixed bug #589 in getSampleInfo() - locking error
 * 16-Dec-2002: ekoperda fixed bug #658 by inserting extra error-checking into
 *              getSearchResults()
 * 17-Dec-2002: ekoperda modified start() to support multiple db connections
 * 18-Dec-2002: ekoperda fixed bug #668 in getMultipleSampleInfo()
 * 19-Dec-2002: ekoperda fixed bug #670 in getSearchResults()
 * 15-Jan-2003: ekoperda modified putSampleInfo(), dbFetchSample(), and
 *              dbFetchMultipleSamplesWithOr() to accommodate direct storage of
 *              the three date summary fields on each SampleInfo
 * 04-Feb-2003: ekoperda removed getAllAuthoritativeSamples(),
 *              getAllHostedSamples(), and getAllKnownSamples() - no longer
 *              useful; modified rebuildSampleData() to run a search instead
 * 14-Feb-2003: adharurk made lookupSampleId() RMI-accessible
 * 18-Feb-2003: ekoperda fixed bug #726 in revertSampleToVersion() and
 *              putSampleInfo()
 * 18-Feb-2003: ekoperda modified putSampleInfo() to include logic for
 *              generating replication ISM's; wrote publicizeExistingSamples()
 *              as a migration task
 * 19-Feb-2003: ekoperda fixed bug #736 in putSampleInfo() by adding validation
 *              logic
 * 20-Feb-2003: ekoperda added getSearchParams()
 * 21-Feb-2003: ekoperda added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 13-Mar-2003: ekoperda fixed bug 774 in eventSampleActivation(),
 *              eventSampleUpdate(), and eventSampleDeactivation()
 * 20-Mar-2003: ekoperda fixed bug 777 in lookupSampleId(), dbDoSearch(), and
 *              dbScanForLocalLabId()
 * 21-Mar-2003: yli revised getEarliestAuthoritativeHistoryDate() which does
 *              not use sampleHistory table any more
 * 24-Mar-2003: ekoperda fixed bug 814 by making putSampleInfo() reject illegal
 *              localLabId's on new samples
 * 27-Mar-2003: ekoperda fixed bug 821 by making putSampleInfo() reject new
 *              samples whose lab/provider is inactive
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator
 * 22-Apr-2003: ekoperda removed clientIp field from argument list of
 *              putSampleInfo(), revertSampleToVersion(), and dbWriteSample()
 * 23-May-2003: ekoperda added support throughout for LockAgent
 * 08-Jul-2003: ekoperda modified start() to pass new config directives to
 *              LockAgent
 * 17-Jul-2003: midurbin modified putSampleInfo to use new function
 *              RepositoryManager.isFilenameValid() to validate LocalLabId
 * 18-Jul-2003: ekoperda modified start() to utilize new config directive
 *              DbUrlForBootstrap
 * 08-Aug-2003: midurbin added a second version of revertSampleToVersion() that
 *              accepts a preexisting lock as a parameter
 * 14-Aug-2003: midurbin/ekoperda fixed bug #1020 by adding new versions of
 *              getSampleInfo() and putSampleInfo()
 * 07-Jan-2004: ekoperda changed package references due to source tree
 *              reorganization
 * 08-Apr-2004: midurbin fixed bug #1130 in putSampleInfo()
 * 29-Apr-2004: midurbin fixed bug #1197 in putSampleInfo()
 * 07-May-2004: cwestnea added updateSearchAtomsByType() and modified
 *              updateSearchAtoms() to update different empirical formulas
 * 18-Mar-2004: cwestnea modified RMI connection string in start()
 * 27-May-2004: ajooloor and ekoperda added
 *              dbFetchMultipleSamplesWithTempTable(),
 *              dbFetchMultipleSamplesHelperSample(),
 *              dbFetchMultipleSamplesHelperSampleHistory(),
 *              dbFetchMultipleSamplesHelperGeneral(), and modified
 *              getMultipleSampleInfo() and dbFetchMultipleSamplesWithOr() to
 *              improve bulk-fetching performance
 * 03-Jun-2004: cwestnea modified putSampleInfo() and added new member
 *              variables to use the validation package
 * 07-Jun-2004: midurbin changed package references due to source tree
 *              reorganization
 * 21-Jun-2004: ekoperda improved performance of one generateWhereClause() and
 *              removed another obsolete version of the function
 * 21-Jun-2004: ekoperda modified all references to SampleLock's to employ new
 *              locking subsystem instead
 * 21-Jun-2004: ekoperda removed obsolete versions of getSampleInfo() and
 *              putSampleInfo() that were specific to GenericExclusiveLocks
 * 08-Aug-2004: cwestnea made changes throughout to use SampleWorkflowBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 05-Jan-2005: midurbin modified dbWriteSampleVersion() to add support for
 *              the new field 'orignialHistoryId' on versioned data
 * 05-Jan-2005: midurbin changed spec of revertSampleToVersion()
 * 05-Jan-2005: midurbin modified spec for putSampleInfo() and dbWriteSample()
 *              to include revertedToSampleVersion in the sampleHistory table
 * 11-Jan-2005: jobollin moved Validator to org.recipnet.common
 * 12-Jan-2005: ekoperda fixed bug #1497 in processCoreMessage() and
 *              passCoreMessage()
 * 25-Feb-2005: midurbin modified dbDoSearch() and executeSearch() so that the
 *              localLabs collection will be made available to the SearchParams
 *              and refactored code from SearchParams to dbDoSearch()
 * 11-May-2005: ekoperda added updateSearchUnitCells() and
 *              rebuildSearchUnitCells() and code to invoke them
 * 12-Jul-2005: ekoperda added updateSearchSpaceGroups() and 
 *              rebuildSearchSpaceGroups() and code to invoke them
 * 27-Sep-2005: midurbin added getNextUnusedLocalLabId()
 * 06-Oct-2005: midurbin added support for versioning a sample's providerId
 * 28-Oct-2005: jobollin removed unnecessary CloneNotSupportedException catches
 *              resulting from SampleInfo.clone() no longer throwing this
 *              exception; reformatted the affected methods to remove tabs;
 *              removed unused imports
 * 09-Nov-2005: jobollin updated updateSearchAtomsByType() to use the new
 *              ChemicalFormulaBL method for parsing formulae instead of the
 *              (deprecated and removed) methods on SampleInfo, and to treat
 *              atom counts recorded in the searchAtoms table as type
 *              BigDecimal
 * 23-Mar-2006: jobollin fixed bug #1769 in updateSearchAtomsByType(), and
 *              reformatted the source; fixed type safety warnings
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 * 10-Apr-2006: copied documentation comments around so that each method has
 *              its own
 * 11-Apr-2006: jobollin switched to initializing object caches via
 *              ObjectCache.newInstance(); removed unnecessary declarations of
 *              RemoteException
 * 11-May-2006: jobollin added multiple finally{} blocks to ensure that all
 *              locks are released in the scope of the method that acquires
 *              them, and similarly, that all JDBC Statements are closed by the
 *              method that opens them
 * 29-May-2006: jobollin updated dbDoSearch() for consistency with
 *              modifications to the SearchConstraint API
 * 30-May-2006: jobollin modified docs 
 * 14-May-2008: ekoperda fixed bug #1891 in dbEraseSample()
 * 04-Jul-2008: ekoperda added runPeriodicSampleIdBlockCheck() and 
 *              countUnusedSampleIds() to enhance new-site synchronization
 * 31-Dec-2008: ekoperda relaxed ISM validation in eventSampleActivation()
 * 02-Jan-2009: ekoperda added readvertiseSample()
 */

package org.recipnet.site.core;

import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.recipnet.common.Element;
import org.recipnet.common.ObjectCache;
import org.recipnet.common.PerfTimer;
import org.recipnet.common.Validator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.agent.SampleIdAgent;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.lock.LockAgent;
import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.LocalLabsChangedCM;
import org.recipnet.site.core.msg.PingCM;
import org.recipnet.site.core.msg.PreexecuteSearchCM;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.SampleActivationISM;
import org.recipnet.site.core.msg.SampleDeactivationISM;
import org.recipnet.site.core.msg.SampleHoldingCM;
import org.recipnet.site.core.msg.SampleIdBlockHintCM;
import org.recipnet.site.core.msg.SampleIdBlockISM;
import org.recipnet.site.core.msg.SampleStatusHintCM;
import org.recipnet.site.core.msg.SampleUpdateISM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.util.CachedSearchResults;
import org.recipnet.site.core.util.CoreMessageQueue;
import org.recipnet.site.core.util.CoreScheduledTask;
import org.recipnet.site.core.util.EventSignal;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.MutexLock;
import org.recipnet.site.core.util.SampleLocks;
import org.recipnet.site.shared.SampleStats;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.bl.ChemicalFormulaBL;
import org.recipnet.site.shared.bl.SampleMathBL;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.SearchUnitCellsInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.search.RequireAuthoritativeSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintExtraInfo;
import org.recipnet.site.shared.search.SearchTableTracker;
import org.recipnet.site.shared.validation.ContainerStringValidator;
import org.recipnet.site.shared.validation.FilenameValidator;

/**
 * Implementation for the Sample Manager core object. There should be only one
 * of these objects ever instantiated at a single time. We'll create our own
 * worker thread so that this object is "always running".
 */
public class SampleManager extends UnicastRemoteObject implements
        SampleManagerRemote, Runnable {
    // References to the other core objects
    SiteManager siteManager;

    RepositoryManager repositoryManager;

    // Worker thread variables
    private final AtomicReference<Thread> workerThread;

    private final EventSignal startupSignal;

    final EventSignal shutdownSignal;

    private final CoreMessageQueue messageQueue;

    // "Ping" objects -- used to see if the worker is running
    private final MutexLock pingMutex;

    private final EventSignal pingSignal;

    // Caches; used to reduce the number of database queries
    // stores SearchParams objects by search id:
    private final ObjectCache<SearchParams> storedSearchParams;

    // stores CachedSearchResults objects by search id:
    private final ObjectCache<CachedSearchResults> cachedSearchResults;

    // stores SampleInfo objects by sample id:
    private final ObjectCache<SampleInfo> cachedSamples;

    // a random number generator used for assigning id's to new db records
    private final Random randomIdGenerator;

    // an array of Lab Id's that includes every lab currently hosted on the
    // local site
    private final List<Integer> localLabs;

    // used to synchronize database reading and writing between threads;
    // shared with RepositoryManager.
    LockAgent lockAgent;

    // stores our configuration properties
    private final Properties properties;

    // tells us whether we were created in bootstrap mode
    private final boolean bootstrapMode;

    // stores statistical counters that can be queried
    private final SampleStats stats;

    // Used to manage the sample id block negotation protocol
    private SampleIdAgent idAgent;

    // Validates localLabIds as a filename since they will be used to create
    // a directory
    private final Validator localLabIdValidator;

    // Validates data strings as container strings
    private final Validator sampleStringFieldValidator;



    /**************************************************************************
     *
     * CONSTRUCTOR AND BASIC METHODS (not remotely accessible)
     *
     *************************************************************************/



    /** The one and only constructor */
    public SampleManager(Properties p, boolean bootstrapMode)
            throws RemoteException {
        // Initiate our simple member variables
        workerThread = new AtomicReference<Thread>(null);
        startupSignal = new EventSignal(false, false);
        shutdownSignal = new EventSignal(false, false);
        messageQueue = new CoreMessageQueue();
        pingMutex = new MutexLock();
        pingSignal = new EventSignal();
        randomIdGenerator = new SecureRandom();
        localLabs = new ArrayList<Integer>();
        properties = p;
        this.bootstrapMode = bootstrapMode;
        stats = new SampleStats();
        localLabIdValidator = new FilenameValidator();
        sampleStringFieldValidator = new ContainerStringValidator();

        // create our object caches according to configuration properties
        storedSearchParams = ObjectCache.newInstance(
                properties.getProperty("SamSearchParamsCache"));
        cachedSearchResults = ObjectCache.newInstance(
                properties.getProperty("SamSearchResultsCache"));
        cachedSamples = ObjectCache.newInstance(
                properties.getProperty("SamSampleCache"));
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
         * First task: create our db locking manager. It will manage our db
         * connection pool for us. The locking manager also will be shared by
         * RepositoryManager.
         */
        this.lockAgent = new LockAgent(this.siteManager,
                properties.getProperty("SamLockLogStackTraces").equals("true"),
                Integer.parseInt(properties.getProperty("SamLockTimeout")));

        /*
         * Next task: establish potentially many database connections and hand
         * them all over to LockAgent for safekeeping. The driver class name,
         * database URI, username, and password are all user-configurable. The
         * number of parallel connections to create is also user-configurable.
         */
        try {
            Class.forName(properties.getProperty("DbDriverClassName"));
        } catch (Exception ex) {
            siteManager.recordLogRecord(LogRecordGenerator.dbDriverFailedToLoad(
                    this, properties.getProperty("DbDriverClassName"), ex));
            stop();
            return false;
        }
        int connectionPoolSize = Integer.parseInt(
                properties.getProperty("SamDbConnectionCount"));
        String dbUrl = this.bootstrapMode
                ? properties.getProperty("DbUrlForBootstrap")
                : properties.getProperty("DbUrl");
        try {
            for (int i = 0; i < connectionPoolSize; i++) {
                Connection conn = DriverManager.getConnection(dbUrl,
                        properties.getProperty("SamDbUsername"),
                        properties.getProperty("SamDbPassword"));
                lockAgent.addConnection(conn);
            }
        } catch (SQLException ex) {
            siteManager.recordLogRecord(LogRecordGenerator.dbFailedToConnect(
                    this, dbUrl, properties.getProperty("SamDbUsername"), ex));
            stop();
            return false;
        }

        /*
         * Next task: ask Site Manager for a list of labs currently hosted at
         * the local site. We'll be notified via a CoreMessage if this list
         * should change once we're up and running.
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
        for (int i = 0; i < labarray.length; i++) {
            localLabs.add(Integer.valueOf(labarray[i]));
        }

        /*
         * Next task: build the list of sample id's that can be assigned to
         * newly-created samples.
         */
        try {
            idAgent = new SampleIdAgent(siteManager, this, lockAgent,
                    properties);
            idAgent.init(bootstrapMode);
        } catch (ResourcesExhaustedException ex) {
	    if ((ex.getReason() 
		    & ResourcesExhaustedException.SAMPLE_IDS_EXHAUSTED) != 0) {
		siteManager.recordLogRecord(
		        LogRecordGenerator.sampleIdsExhaustedAtStartup(ex));
	    } else {
		siteManager.recordLogRecord(
                        LogRecordGenerator.sampleIdListBuildException(ex));
	    }
	    stop();
	    return false;
	} catch (Exception ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.sampleIdListBuildException(ex));
            stop();
            return false;
        }

        /*
         * If we were started in bootstrap mode, terminate right here. The
         * startup tasks below this line are not necessary for bootstrap mode.
         */
        if (bootstrapMode) {
            return true;
        }

        /*
         * Next task: spawn the worker thread and wait for it to start. The
         * thread name and maximum wait time are user-configurable.
         */
        workerThread.set(new Thread(this,
                properties.getProperty("SamWorkerThreadName")));
        workerThread.get().start();
        int startuptime = Integer.parseInt(
                properties.getProperty("SamWorkerThreadStartupTime"));
        if (startupSignal.receive(startuptime) == false) {
            // The worker thread failed to start in a timely fashion.
            // Terminate.
            siteManager.recordLogRecord(
                    LogRecordGenerator.threadFailedToStart(this));
            workerThread.getAndSet(null).interrupt();
            stop();
            return false;
        }

        /*
         * Final task: bind ourselves to RMI so that other modules can connect
         * to us and make requests.
         */
        try {
            Naming.bind("//localhost:" + properties.getProperty("GenRmiPort")
                    + "/" + properties.getProperty("SamRmiName"), this);
        } catch (AlreadyBoundException ex) {
            siteManager.recordLogRecord(
                    LogRecordGenerator.rmiAlreadyBoundException(
                            this, properties.getProperty("SamRmiName"), ex));
            stop();
            return false;
        } catch (ConnectException ex) {
            siteManager.recordLogRecord(LogRecordGenerator.rmiConnectException(
                    this, ex));
            stop();
            return false;
        } catch (Exception ex) {
            siteManager.recordLogRecord(LogRecordGenerator.rmiException(this,
                    ex));
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
        // Unbind the RMI name so that new remote calls can't arrive
        try {
            // TODO: this probably doesn't have the effect described above
            Naming.unbind(properties.getProperty("SamRmiName"));
        } catch (Exception ex) {
            // If this operation fails, it fails. We don't need
            // to take any special action.
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
    }

    /**
     * The primary worker thread. It will not terminate unless the
     * {@code stop()} method is called from another thread.
     */
    public void run() {
        try {

            // Initialize variables needed for thread management
            Thread thisThread = Thread.currentThread();

            // Perform any one-time initialization tasks
            siteManager.schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SamRandomizerTask"),
                    new Runnable() {
                        public void run() {
                            randomIdGenerator.nextInt();
                        }
                    }));
            siteManager.schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SamIdMaintenanceTask"),
                    new Runnable() {
                        public void run() {
                            try {
                                idAgent.periodicCheck();
                            } catch (Exception ex) {
                                // This is a just-in-case failsafe mechanism to
                                // ensure that any exceptions here at least
                                // get reported somehow
                                siteManager.recordLogRecord(
                                        LogRecordGenerator.sampleIdPeriodicCheckException(ex));
                            }
                        }
                    }));
            siteManager.schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SamLockPurgeTask"), new Runnable() {
                        public void run() {
                            try {
                                lockAgent.releaseExpiredLocks();
                            } catch (Exception ex) {
                                // This is a just-in-case failsafe mechanism to
                                // ensure that any exceptions here at least
                                // get reported somehow.
                                siteManager.recordLogRecord(
                                        LogRecordGenerator.sampleLockPeriodicReleaseException(ex));
                            }
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
            if (this.lockAgent != null) {
                this.lockAgent.releaseAllLocks();
            }

            // Tell every other thread that Site Manager's worker thread has
            // terminated gracefully
            shutdownSignal.send();
        } catch (Exception ex) {
            // Generic catch-all for otherwise uncaught exceptions
            siteManager.recordLogRecord(LogRecordGenerator.threadException(
                    this, ex));
        }
    }



    /**************************************************************************
     *
     * REMOTE METHODS
     *
     *************************************************************************/



    /**
     * Determines whether this core object (in particular, its worker thread) is
     * still functioning.
     * 
     * @param milliseconds the maximum number of milliseconds to wait for a
     *        reply, or zero for no limit; because only one ping request can be
     *        in progress at a time, it is possible that the calling thread will
     *        be blocked for up to twice this number of milliseconds.
     * @return {@code true} if the worker thread is confirmed to be running,
     *         {@code false} if it is unresponsive (which does not necessarilly
     *         mean it isn't progressing)
     */
    public boolean isAlive(int milliseconds) {

        if (!workerThread.get().isAlive()
                /* Wait for our turn */
                || !pingMutex.acquire(milliseconds)) {
            return false;
        } else {
            try {
                
                /*
                 * Send a message to the worker thread asking him to signal the
                 * pingSignal and await his reply.
                 */
                pingSignal.reset();
                messageQueue.send(new PingCM());
                
                return pingSignal.receive(milliseconds);
            } finally {
                // Release the mutex lock upon return or exception
                pingMutex.release();
            }
        }
    }

    /**
     * Fetches the specified sample object from the database (or the cache), or
     * an empty {@code SampleInfo} object.
     * 
     * @return an empty {@code SampleInfo} object suitable for caller population
     *         and a subsequent call to {@code putSampleInfo()} for
     *         sample creation
     */
    public SampleInfo getSampleInfo() {
        return new SampleInfo();
    }

    /**
     * Fetches the specified sample object from the database (or the cache), or
     * an empty {@code SampleInfo} object.
     * 
     * @param sampleId if specified, identifies the sample that should be
     *        fetched.
     * @return the most recent version of the specified sample
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because a low-level error occurred.
     * @throws ResourceNotFoundException with an {@code identifier} of
     *         type {@code SampleInfo} if no matching sample could be found.
     */
    public SampleInfo getSampleInfo(int sampleId)
            throws InconsistentDbException, OperationFailedException,
            ResourceNotFoundException {
        return getSampleInfo(sampleId,
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, null);
    }

    /**
     * Fetches the specified sample object from the database (or the cache), or
     * an empty {@code SampleInfo} object.
     * 
     * @return the specified version of the specified sample
     * @param sampleId the ID of the sample that should be returned
     * @param historyId the version of the specified sample that should be
     *        returned. The value
     *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} is
     *        reserved and is interpreted as a request for the most recent
     *        version.
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because a low-level error occurred.
     * @throws ResourceNotFoundException with an {@code identifier} of
     *         type {@code SampleInfo} if no matching sample could be found.
     */
    public SampleInfo getSampleInfo(int sampleId, int historyId)
            throws InconsistentDbException, OperationFailedException,
            ResourceNotFoundException {
        return getSampleInfo(sampleId, historyId, null);
    }

    /**
     * Fetches the specified sample object from the database (or the cache), or
     * an empty {@code SampleInfo} object.
     * 
     * @return the specified version of the specified sample
     * @param sampleId identifies the sample that should be returned
     * @param historyId identifies the version of the specified sample that
     *        should be returned. The value
     *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} is
     *        reserved and is interpreted as a request for the most recent
     *        version.
     * @param existingLock an existing lock to use for the operation, or
     *        {@code null} if a new lock should be obtained; if non-null then
     *        must be cleared for {@code requestSampleRead()}
     * @throws IllegalArgumentException if {@code existingLock} is not
     *         {@code null} but lacks sufficient authorization to perform the
     *         necessary tasks.
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because a low-level error occurred.
     * @throws ResourceNotFoundException with an {@code identifier} of
     *         type {@code SampleInfo} if no matching sample could be found
     */
    public SampleInfo getSampleInfo(int sampleId, int historyId,
            AbstractLock existingLock) throws InconsistentDbException,
            OperationFailedException, ResourceNotFoundException {
        AbstractLock lock;
        SampleInfo sample;

        PerfTimer perfTimer = new PerfTimer("");
        boolean shouldWriteToCache
                = (historyId == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);

        try {
            // If an pre-existing lock was provided by the caller, verify that
            // it has been cleared for the requisite operations. If no
            // pre-existing lock was supplied, create our own new one.
            if (existingLock != null) {
                if (!SampleLocks.managerGetSampleInfo_verifyExistingLock(
                        existingLock, sampleId, false)) {
                    // Provided lock is insufficient.
                    throw new IllegalArgumentException();
                }
                lock = existingLock;
            } else {
                lock = SampleLocks.managerGetSampleInfo(sampleId, false);
                this.lockAgent.registerLock(lock);
                lock.acquire();
            }

            try {
                // First see if we can read the sample from the cache
                sample = cachedSamples.get(sampleId);
                if ((sample != null) && ((historyId
                        == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                        || (historyId == sample.historyId))) {
                    // We can fulfill the caller's request from the cache
                    stats.record(perfTimer, "getSampleInfo() from cache");
                    
                    // The lock is released in a finally {} block below
                    return sample.clone();
                }

                // The cache lookup failed; promote our lock to a db-type lock
                // (if we own the lock).
                if (existingLock == null) {
                    AbstractLock oldLock = lock;
                    
                    lock = SampleLocks.managerGetSampleInfo(sampleId, true);
                    this.lockAgent.registerLock(lock);
                    lock.promoteFrom(oldLock);
                }

                // Do the database read
                try {
                    sample = dbFetchSample(lock.getConnection(), sampleId,
                            historyId, false);
                    if ((sample != null) && shouldWriteToCache) {
                        cachedSamples.put(sample.id, sample);
                    }
                } catch (SQLException ex) {
                    // database error
                    throw new OperationFailedException(ex);
                }
            } finally {
                if (existingLock == null) {
                    lock.release();
                }
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }

        // All done
        if (sample != null) {
            stats.record(perfTimer, "getSampleInfo() from db");

            return sample.clone();
        } else {
            throw new ResourceNotFoundException(
                    new SampleInfo(sampleId, historyId));
        }
    }

    /**
     * Return a FullSampleInfo object by given the sampleId;
     * 
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because a low-level error occurred.
     * @throws ResourceNotFoundException with an {@code identifier} of
     *         type {@code FullSampleInfo} if no matching sample could be
     *         found.
     * @throws UnexpectedExceptionException
     */
    public FullSampleInfo getFullSampleInfo(int sampleId)
            throws InconsistentDbException, OperationFailedException,
            ResourceNotFoundException {
        /*
         * We always do a whole database query to build a FullSampleInfo object
         * i.e. we don't try to cache these. They are requested relatively
         * infrequently in any case.
         */
        try {
            // Obtain a database lock
            AbstractLock lock = SampleLocks.managerGetFullSampleInfo(sampleId);
            
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                Connection conn = lock.getConnection();

                // First, grab data from the samples table. Fill the basic
                // members of the FullSampleInfo.
                Statement cmd = conn.createStatement();
                FullSampleInfo fsample;
                
                try {
                    String sql = "SELECT * FROM samples WHERE id=" + sampleId + ";";
                    ResultSet rs = cmd.executeQuery(sql);

                    if (rs.next()) {
                        fsample = new FullSampleInfo(rs);
                    } else {
                        // sample id not known
                        throw new ResourceNotFoundException(
                                new FullSampleInfo(sampleId));
                    }
                    rs.close();

                    // Next, grab data from the sampleHistory table. Create a
                    // bunch of SampleHistoryInfo objects and fill them with
                    // basic
                    // information.
                    sql = "SELECT * FROM sampleHistory WHERE sample_id="
                            + sampleId + " ORDER BY id;";
                    rs = cmd.executeQuery(sql);
                    while (rs.next()) {
                        fsample.history.add(new SampleHistoryInfo(rs));
                    }
                } finally {
                    cmd.close();
                }

                // Finally, iterate through every history object we just
                // grabbed and fetch a whole SampleInfo object for that
                // particular historyId.
                for (SampleHistoryInfo history : fsample.history) {
                    history.sample
                            = dbFetchSample(conn, sampleId, history.id, true);
                    if (history.sample == null) {
                        // No matching sample, even though we got the sample id
                        // and sample history id from the database a minute ago
                        throw new InconsistentDbException();
                    }

                    // Copy some information to this SampleInfo from the
                    // parent FullSample object. We do this for efficiency --
                    // we instructed dbFetchSample() above to skip one db
                    // query because we'd already retrieved the same data when
                    // we created the FullSampleInfo object earlier.
                    history.sample.id = sampleId;
                    history.sample.labId = fsample.labId;
                    history.sample.mostRecentProviderId
                            = fsample.mostRecentProviderId;
                    history.sample.localLabId = fsample.localLabId;
                    history.sample.status = history.newStatus;
                    // we set this to the status this sample
                    // version *had* at the time it was created,
                    // rather than the status the sample *has*
                    // now.
                    history.sample.mostRecentHistoryId
                            = fsample.mostRecentHistoryId;
                    history.sample.mostRecentStatus
                            = fsample.mostRecentStatus;
                }

                // All done, return
                return fsample;
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
     * Returns the id of the sample whose labId and localLabId are as specified.
     * If no such sample exists, returns INVALID_SAMPLE_ID. This function
     * requires a round-trip to the database. TODO: figure out some way for this
     * function to use a cache so that a round-trip to the database is not
     * required for every call. Can the existing samples cache be re-used?
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public int lookupSampleId(int labId, String localLabId)
            throws OperationFailedException {
        int sampleId = SampleInfo.INVALID_SAMPLE_ID;

        try {
            AbstractLock lock = SampleLocks.managerLookupSampleId();
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                // Use a PreparedStatement instead of a normal statement so
                // that the caller's localLabId string will be escaped.
                PreparedStatement cmd = lock.getConnection().prepareStatement(
                        "SELECT id FROM samples"
                                + " WHERE lab_id=? AND localLabId=?");
                
                try {
                    cmd.setInt(1, labId);
                    cmd.setString(2, localLabId);

                    ResultSet rs = cmd.executeQuery();
                    while (rs.next()) {
                        sampleId = rs.getInt("id");
                    }
                } finally {
                    cmd.close();
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }

            return sampleId;
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Returns true if the specified sample number exists in the local database,
     * false otherwise
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public boolean verifySampleNumber(int sampleId)
            throws OperationFailedException {
        try {
            // Obtain a cache-only lock
            AbstractLock lock
                    = SampleLocks.managerVerifySampleNumber(sampleId, false);
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Return early if the requested sample is in the cache
                if (cachedSamples.get(sampleId) != null) {
                    // lock is released in a finally {} block below
                    return true;
                }

                // Promote our lock to a database-type lock
                AbstractLock oldLock = lock;
                lock = SampleLocks.managerVerifySampleNumber(sampleId, true);
                this.lockAgent.registerLock(lock);
                lock.promoteFrom(oldLock);

                // return true if a matching row is found in the 'samples' table
                try {
                    Connection conn = lock.getConnection();
                    Statement cmd = conn.createStatement();
    
                    try {
                        String sql = "SELECT id FROM samples WHERE id="
                                + sampleId + ";";
    
                        return cmd.executeQuery(sql).next();
                    } finally {
                        cmd.close();
                    }
                } catch (SQLException ex) {
                    throw new OperationFailedException(ex);
                } 
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Verifies several sample numbers at once. sampleIds should contain items
     * of type Integer that represent the sample numbers to be checked. Returns
     * another set that contains only those sample numbers that are valid.
     * 
     * @param  sampleIds a {@code Set} containing the {@code Integer} IDs to
     *         verify
     *         
     * @return a {@code Set} of those {@code Integer}s that are valid
     *         sample IDs
     *         
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public Set<Integer> verifySampleNumbers(Set<Integer> sampleIds)
            throws OperationFailedException {
        return verifySampleNumbers(sampleIds, null);
    }

    /**
     * Verifies several sample numbers at once. sampleIds should contain items
     * of type Integer that represent the sample numbers to be checked. Returns
     * another set that contains only those sample numbers that are valid.
     * 
     * @param  sampleIds a {@code Set} containing the {@code Integer} IDs to
     *         verify
     * @param  existingLock an existing, active lock that provides sufficient
     *         resource access to read the sample cache and to select from the
     *         samples table in the DB; this lock will be used instead of a
     *         new one being obtained
     *         
     * @return a {@code Set} of those {@code Integer}s that are valid
     *         sample IDs
     *         
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public Set<Integer> verifySampleNumbers(Set<Integer> sampleIds,
            AbstractLock existingLock) throws OperationFailedException {
        Set<Integer> valid = new HashSet<Integer>(
                1 + (4 * sampleIds.size() + 2) / 3);
        Set<Integer> unknown = new HashSet<Integer>(sampleIds);
        AbstractLock lock;

        try {

            /*
             * If an pre-existing lock was provided by the caller, verify that
             * it has been cleared for the requisite operations. If no
             * pre-existing lock was supplied, create our own new one.
             */
            if (existingLock != null) {
                if (!SampleLocks.managerVerifySampleNumbers_verifyExistingLock(
                        existingLock, sampleIds, false)) {
                    throw new IllegalArgumentException();
                }
                lock = existingLock;
            } else {
                lock = SampleLocks.managerVerifySampleNumbers(sampleIds, false);
                this.lockAgent.registerLock(lock);
                lock.acquire();
            }

            try {
                // verify as many samples as we can from the cache
                for (Iterator<Integer> it = unknown.iterator(); it.hasNext();) {
                    Integer key = it.next();

                    if (cachedSamples.get(key) != null) {
                        it.remove();
                        valid.add(key);
                    }
                }

                // We can terminate early if the list of unknowns is empty.
                if (unknown.isEmpty()) {
                    // lock is released in a finally {} block below
                    return valid;
                }

                // The next step is to do a big database lookup. Build the SQL
                // query string.
                StringBuilder sql = new StringBuilder(
                        "SELECT id FROM samples WHERE");
                boolean addOr = false;

                for (Integer id : unknown) {
                    if (!addOr) {
                        addOr = true;
                    } else {
                        sql.append(" OR");
                    }
                    sql.append(" id=").append(id);
                }
                sql.append(';');

                // If we created our own lock previously, promote it from a
                // cache-only lock to one that can read from the database.
                if (existingLock == null) {
                    AbstractLock oldLock = lock;
                    lock = SampleLocks.managerVerifySampleNumbers(
                            unknown, true);
                    this.lockAgent.registerLock(lock);
                    lock.promoteFrom(oldLock);
                }

                // Do the db query and parse the resultset.
                try {
                    Statement cmd = lock.getConnection().createStatement();
                    
                    try {
                        ResultSet rs = cmd.executeQuery(sql.toString());

                        while (rs.next()) {
                            valid.add(rs.getInt("id"));
                        }
                    } finally {
                        cmd.close();
                    }
                    
                    return valid;
                } catch (SQLException ex) {
                    throw new OperationFailedException(ex);
                }
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
     * <p>
     * Writes a {@code SampleInfo} object and returns an updated
     * SampleInfo object, for a sample where the local site is authoritative.
     * The {@code SampleInfo} object should have been obtained via a
     * prior call to one of the getSampleInfo() methods, or another equivalent
     * method on Sample Manager. In the specified {@code SampleInfo}
     * object, the {@code id} and {@code historyId} fields should
     * not have been modified, the {@code labId},
     * {@code providerId}, and {@code localLabId} should not have
     * been modified unless this is a brand-new sample about to be written for
     * the first time, the {@code status} field may be changed to a valid
     * status code at will, the children of the {@code dataInfo}
     * reference may be changed at will, and the items within the
     * {@code attributeInfo}, {@code annotationInfo}, and
     * {@code accessInfo} collections may be added or removed at will
     * (but not modified in-place). The {@code mostRecentStatus},
     * {@code mostRecentHistoryId} and {@code mostRecentProviderId}
     * fields are ignored.
     * </p><p>
     * For a brand-new sample about to be written for the first time, the
     * specified {@code id} value should be INVALID_SAMPLE_ID and the
     * specified {@code historyId} value should be
     * INVALID_SAMPLE_HISTORY_ID. These are the same values assigned to a new
     * {@code SampleInfo} object by the nullary getSampleInfo() call.
     * After this function is called, the new sample's unique id can be
     * retrieved from the {@code id} field in the returned SampleInfo
     * object.
     * </p><p>
     * Note that any duplicate atom records, attribute records, annotation
     * records, or access records are silently stripped out before the sample is
     * written to the database.
     * </p><p>
     * If no action date is specified, the current date is assumed to be the
     * action date. This is correct behavior for any calling module that
     * interfaces with a real live user.
     * </p><p>
     * It makes sense to call this method only when the local site is
     * authoritative for the specified sample - any other use may throw an
     * exception. If the sample's status should change from being non-public to
     * public, or change from public to non-public, or items within an
     * already-public sample change, an appropriate broadcast
     * SampleActivationISM/SampleUpdateISM/SampleDeactivationISM is generated.
     * </p><p>
     * If the {@code originalSampleHistoryId} for any
     * {@code SampleAnnotationInfo}, {@code sampleAttributeInfo}
     * or {@code SampleDataInfo} contained in the {@code SampleInfo}
     * passed to this method is unset or unchanged from the current version when
     * the corresponding data value has changed it will be overwritten with the
     * sample history id associated with this call. Otherwise the
     * {@code originalSampleHistoryId} value to which it had been set
     * will remain.
     * </p>
     * 
     * @param sample a SampleInfo containing the information to record
     * @param actionCode the workflow action code representing the action that
     *        resulted in the sample update
     * @param userId the ID of the user responsible for this update
     * @param comments sample history comments to record in the specified
     *        sample's records for this update
     *        
     * @return a SampleInfo reflecting the state of the specified sample after
     *         the update is performed
     * 
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was
     *         attempted but another sample already exists with the same
     *         combination of {@code labId} and {@code localLabId}
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID}, {@code ILLEGAL_SPGP},
     *         {@code ILLEGAL_COLOR}, {@code ILLEGAL_SUMMARY},
     *         {@code ILLEGAL_COMMENTS}, {@code ILLEGAL_ATTRIBUTE},
     *         {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
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
    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode,
            int userId, String comments) throws DuplicateDataException,
            InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        return putSampleInfo(sample, actionCode, new Date(), userId, comments,
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, null);
    }

    /**
     * <p>
     * Writes a {@code SampleInfo} object and returns an updated
     * SampleInfo object, for a sample where the local site is authoritative.
     * The {@code SampleInfo} object should have been obtained via a
     * prior call to one of the getSampleInfo() methods, or another equivalent
     * method on Sample Manager. In the specified {@code SampleInfo}
     * object, the {@code id} and {@code historyId} fields should
     * not have been modified, the {@code labId},
     * {@code providerId}, and {@code localLabId} should not have
     * been modified unless this is a brand-new sample about to be written for
     * the first time, the {@code status} field may be changed to a valid
     * status code at will, the children of the {@code dataInfo}
     * reference may be changed at will, and the items within the
     * {@code attributeInfo}, {@code annotationInfo}, and
     * {@code accessInfo} collections may be added or removed at will
     * (but not modified in-place). The {@code mostRecentStatus},
     * {@code mostRecentHistoryId} and {@code mostRecentProviderId}
     * fields are ignored.
     * </p><p>
     * For a brand-new sample about to be written for the first time, the
     * specified {@code id} value should be INVALID_SAMPLE_ID and the
     * specified {@code historyId} value should be
     * INVALID_SAMPLE_HISTORY_ID. These are the same values assigned to a new
     * {@code SampleInfo} object by the nullary getSampleInfo() call.
     * After this function is called, the new sample's unique id can be
     * retrieved from the {@code id} field in the returned SampleInfo
     * object.
     * </p><p>
     * Note that any duplicate atom records, attribute records, annotation
     * records, or access records are silently stripped out before the sample is
     * written to the database.
     * </p><p>
     * If no action date is specified, the current date is assumed to be the
     * action date. This is correct behavior for any calling module that
     * interfaces with a real live user.
     * </p><p>
     * It makes sense to call this method only when the local site is
     * authoritative for the specified sample - any other use may throw an
     * exception. If the sample's status should change from being non-public to
     * public, or change from public to non-public, or items within an
     * already-public sample change, an appropriate broadcast
     * SampleActivationISM/SampleUpdateISM/SampleDeactivationISM is generated.
     * </p><p>
     * If the {@code originalSampleHistoryId} for any
     * {@code SampleAnnotationInfo}, {@code sampleAttributeInfo}
     * or {@code SampleDataInfo} contained in the {@code SampleInfo}
     * passed to this method is unset or unchanged from the current version when
     * the corresponding data value has changed it will be overwritten with the
     * sample history id associated with this call. Otherwise the
     * {@code originalSampleHistoryId} value to which it had been set
     * will remain.
     * </p>
     * 
     * @param sample a SampleInfo containing the information to record
     * @param actionCode the workflow action code representing the action that
     *        resulted in the sample update
     * @param actionDate 
     * @param userId the ID of the user responsible for this update
     * @param comments sample history comments to record in the specified
     *        sample's records for this update
     *        
     * @return a SampleInfo reflecting the state of the specified sample after
     *         the update is performed
     * 
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was
     *         attempted but another sample already exists with the same
     *         combination of {@code labId} and {@code localLabId}
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID}, {@code ILLEGAL_SPGP},
     *         {@code ILLEGAL_COLOR}, {@code ILLEGAL_SUMMARY},
     *         {@code ILLEGAL_COMMENTS}, {@code ILLEGAL_ATTRIBUTE},
     *         {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
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
    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode,
            Date actionDate, int userId, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        return putSampleInfo(sample, actionCode, actionDate, userId, comments,
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, null);
    }

    /**
     * <p>
     * Writes a {@code SampleInfo} object and returns an updated
     * SampleInfo object, for a sample where the local site is authoritative.
     * The {@code SampleInfo} object should have been obtained via a
     * prior call to one of the getSampleInfo() methods, or another equivalent
     * method on Sample Manager. In the specified {@code SampleInfo}
     * object, the {@code id} and {@code historyId} fields should
     * not have been modified, the {@code labId},
     * {@code providerId}, and {@code localLabId} should not have
     * been modified unless this is a brand-new sample about to be written for
     * the first time, the {@code status} field may be changed to a valid
     * status code at will, the children of the {@code dataInfo}
     * reference may be changed at will, and the items within the
     * {@code attributeInfo}, {@code annotationInfo}, and
     * {@code accessInfo} collections may be added or removed at will
     * (but not modified in-place). The {@code mostRecentStatus},
     * {@code mostRecentHistoryId} and {@code mostRecentProviderId}
     * fields are ignored.
     * </p><p>
     * For a brand-new sample about to be written for the first time, the
     * specified {@code id} value should be INVALID_SAMPLE_ID and the
     * specified {@code historyId} value should be
     * INVALID_SAMPLE_HISTORY_ID. These are the same values assigned to a new
     * {@code SampleInfo} object by the nullary getSampleInfo() call.
     * After this function is called, the new sample's unique id can be
     * retrieved from the {@code id} field in the returned SampleInfo
     * object.
     * </p><p>
     * Note that any duplicate atom records, attribute records, annotation
     * records, or access records are silently stripped out before the sample is
     * written to the database.
     * </p><p>
     * If no action date is specified, the current date is assumed to be the
     * action date. This is correct behavior for any calling module that
     * interfaces with a real live user.
     * </p><p>
     * It makes sense to call this method only when the local site is
     * authoritative for the specified sample - any other use may throw an
     * exception. If the sample's status should change from being non-public to
     * public, or change from public to non-public, or items within an
     * already-public sample change, an appropriate broadcast
     * SampleActivationISM/SampleUpdateISM/SampleDeactivationISM is generated.
     * </p><p>
     * If the {@code originalSampleHistoryId} for any
     * {@code SampleAnnotationInfo}, {@code sampleAttributeInfo}
     * or {@code SampleDataInfo} contained in the {@code SampleInfo}
     * passed to this method is unset or unchanged from the current version when
     * the corresponding data value has changed it will be overwritten with the
     * sample history id associated with this call. Otherwise the
     * {@code originalSampleHistoryId} value to which it had been set
     * will remain.
     * </p>
     * 
     * @param sample a SampleInfo containing the information to record
     * @param actionCode the workflow action code representing the action that
     *        resulted in the sample update
     * @param actionDate the action date to record in this sample's record
     * @param userId the ID of the user responsible for this update
     * @param comments sample history comments to record in the specified
     *        sample's records for this update
     * @param existingLock an existing lock object protecting sufficient
     *        privilege for inserting into or updating the sample data tables
     *        
     * @return a SampleInfo reflecting the state of the specified sample after
     *         the update is performed
     * 
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was
     *         attempted but another sample already exists with the same
     *         combination of {@code labId} and {@code localLabId}
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID}, {@code ILLEGAL_SPGP},
     *         {@code ILLEGAL_COLOR}, {@code ILLEGAL_SUMMARY},
     *         {@code ILLEGAL_COMMENTS}, {@code ILLEGAL_ATTRIBUTE},
     *         {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
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
    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode,
            Date actionDate, int userId, String comments,
            AbstractLock existingLock) throws DuplicateDataException,
            InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            OptimisticLockingException, ResourceNotFoundException,
            WrongSiteException {
        return putSampleInfo(sample, actionCode, actionDate, userId, comments,
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, existingLock);
    }

    /**
     * <p>
     * Writes a {@code SampleInfo} object and returns an updated
     * SampleInfo object, for a sample where the local site is authoritative.
     * The {@code SampleInfo} object should have been obtained via a
     * prior call to one of the getSampleInfo() methods, or another equivalent
     * method on Sample Manager. In the specified {@code SampleInfo}
     * object, the {@code id} and {@code historyId} fields should
     * not have been modified, the {@code labId},
     * {@code providerId}, and {@code localLabId} should not have
     * been modified unless this is a brand-new sample about to be written for
     * the first time, the {@code status} field may be changed to a valid
     * status code at will, the children of the {@code dataInfo}
     * reference may be changed at will, and the items within the
     * {@code attributeInfo}, {@code annotationInfo}, and
     * {@code accessInfo} collections may be added or removed at will
     * (but not modified in-place). The {@code mostRecentStatus},
     * {@code mostRecentHistoryId} and {@code mostRecentProviderId}
     * fields are ignored.
     * </p><p>
     * For a brand-new sample about to be written for the first time, the
     * specified {@code id} value should be INVALID_SAMPLE_ID and the
     * specified {@code historyId} value should be
     * INVALID_SAMPLE_HISTORY_ID. These are the same values assigned to a new
     * {@code SampleInfo} object by the nullary getSampleInfo() call.
     * After this function is called, the new sample's unique id can be
     * retrieved from the {@code id} field in the returned SampleInfo
     * object.
     * </p><p>
     * Note that any duplicate atom records, attribute records, annotation
     * records, or access records are silently stripped out before the sample is
     * written to the database.
     * </p><p>
     * If no action date is specified, the current date is assumed to be the
     * action date. This is correct behavior for any calling module that
     * interfaces with a real live user.
     * </p><p>
     * It makes sense to call this method only when the local site is
     * authoritative for the specified sample - any other use may throw an
     * exception. If the sample's status should change from being non-public to
     * public, or change from public to non-public, or items within an
     * already-public sample change, an appropriate broadcast
     * SampleActivationISM/SampleUpdateISM/SampleDeactivationISM is generated.
     * </p><p>
     * If the {@code originalSampleHistoryId} for any
     * {@code SampleAnnotationInfo}, {@code sampleAttributeInfo}
     * or {@code SampleDataInfo} contained in the {@code SampleInfo}
     * passed to this method is unset or unchanged from the current version when
     * the corresponding data value has changed it will be overwritten with the
     * sample history id associated with this call. Otherwise the
     * {@code originalSampleHistoryId} value to which it had been set
     * will remain.
     * </p>
     * 
     * @param sample a SampleInfo containing the information to record
     * @param actionCode the workflow action code representing the action that
     *        resulted in the sample update
     * @param actionDate the action date to record in this sample's record
     * @param userId the ID of the user responsible for this update
     * @param comments sample history comments to record in the specified
     *        sample's records for this update
     * @param revertedToHistoryId the sample history ID to which this change
     *        represents a reversion
     * @param existingLock an existing lock object protecting sufficient
     *        privilege for inserting into or updating the sample data tables
     *        
     * @return a SampleInfo reflecting the state of the specified sample after
     *         the update is performed
     * 
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was
     *         attempted but another sample already exists with the same
     *         combination of {@code labId} and {@code localLabId}
     * @throws IllegalArgumentException if {@code existingLock} was
     *         specified and lacks sufficient authorization to perform the
     *         necessary operations.
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN},
     *         {@code ILLEGAL_LOCALLABID}, {@code ILLEGAL_SPGP},
     *         {@code ILLEGAL_COLOR}, {@code ILLEGAL_SUMMARY},
     *         {@code ILLEGAL_COMMENTS}, {@code ILLEGAL_ATTRIBUTE},
     *         {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
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
    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode,
            Date actionDate, int userId, String comments,
            int revertedToHistoryId, AbstractLock existingLock)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        /*
         * This is a big function and could use lots more work.
         * TODO: 1. comprehensive rollback support, so that an exception thrown
         * in the midst of this function doesn't cause an inconsistent database
         * state.
         * 2. more comprehensive validation on each of the sample's sub-objects.
         * 3. reduce the number of db round-trips to increase efficiency
         */

        try {
            // Obtain the first lock that encapsulates every operation we might
            // need to perform later.
            AbstractLock lock;
            
            if (existingLock == null) {
                lock = SampleLocks.managerPutSampleInfo(
                        sample.id == SampleInfo.INVALID_SAMPLE_ID, sample.id);
                this.lockAgent.registerLock(lock);
                lock.acquire();
            } else {
                if (!SampleLocks.managerPutSampleInfo_verifyExistingLock(
                        existingLock,
                        sample.id == SampleInfo.INVALID_SAMPLE_ID, sample.id)) {
                    throw new IllegalArgumentException();
                }
                lock = existingLock;
            }

            try {
                // Basic validation.
                if (!localLabs.contains(Integer.valueOf(sample.labId))) {
                    throw new WrongSiteException(new LabInfo(sample.labId));
                }
                LabInfo lab = siteManager.getLabInfo(sample.labId);
                ProviderInfo provider = siteManager.getProviderInfo(
                        sample.dataInfo.providerId);
                if (provider.labId != lab.id) {
                    throw new InvalidDataException(sample,
                            InvalidDataException.MISMATCHED_LAB_AND_PROVIDER);
                }
                if ((sample.dataInfo.spgp != null)
                        && !sampleStringFieldValidator.isValid(
                                sample.dataInfo.spgp)) {
                    throw new InvalidDataException(sample,
                            InvalidDataException.ILLEGAL_SPGP);
                }
                if ((sample.dataInfo.color != null)
                        && !sampleStringFieldValidator.isValid(
                                sample.dataInfo.color)) {
                    throw new InvalidDataException(sample,
                            InvalidDataException.ILLEGAL_COLOR);
                }
                if ((sample.dataInfo.summary != null)
                        && !sampleStringFieldValidator.isValid(
                                sample.dataInfo.summary)) {
                    throw new InvalidDataException(sample,
                            InvalidDataException.ILLEGAL_SUMMARY);
                }
                if ((comments != null)
                        && !sampleStringFieldValidator.isValid(comments)) {
                    throw new InvalidDataException(sample,
                            InvalidDataException.ILLEGAL_COMMENTS);
                }
                for (SampleAttributeInfo attr : sample.attributeInfo) {
                    if (!sampleStringFieldValidator.isValid(attr.value)) {
                        throw new InvalidDataException(sample,
                                InvalidDataException.ILLEGAL_ATTRIBUTE);
                    }
                }
                for (SampleAnnotationInfo ann : sample.annotationInfo) {
                    if (!sampleStringFieldValidator.isValid(ann.value)) {
                        throw new InvalidDataException(sample,
                                InvalidDataException.ILLEGAL_ANNOTATION);
                    }
                }

                // If sample already exists, compare it to its previous version
                // to detect illegal modifications.
                SampleInfo oldsample;
                
                if (sample.id != SampleInfo.INVALID_SAMPLE_ID) {
                    
                    // throws an exception if the sample ID is invalid:
                    oldsample = getSampleInfo(sample.id,
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);

                    if (oldsample.labId != sample.labId) {
                        throw new InvalidModificationException(
                                InvalidModificationException.CANTCHANGE_LAB,
                                sample);
                    }
                    if (!oldsample.localLabId.equals(sample.localLabId)) {
                        throw new InvalidModificationException(
                                InvalidModificationException.CANTCHANGE_LOCALLABID,
                                sample);
                    }
                    if (oldsample.historyId != sample.historyId) {
                        throw new OptimisticLockingException(sample);
                    }
                    
                    // Populate the sample's date summary fields
                    sample.firstActionDate = oldsample.firstActionDate;
                    sample.lastActionDate = actionDate;
                    sample.releaseActionDate
                            = ((sample.isPublic() && !oldsample.isPublic())
                                    ? actionDate
                                    : oldsample.releaseActionDate);
                } else {
                    oldsample = null;
                    if (sample.localLabId != null) {
                        // This is a new sample; ensure that the localLabId is
                        // valid. This is required because the localLabId will
                        // be mapped to a directory on the filesystem later.
                        if (!localLabIdValidator.isValid(sample.localLabId)) {
                            throw new InvalidDataException(sample,
                                    InvalidDataException.ILLEGAL_LOCALLABID);
                        }

                        // Ensure the localLabId hasn't been used already
                        // within the lab.
                        boolean rc = dbScanForLocalLabId(lock.getConnection(),
                                sample.labId, sample.localLabId);
                        if (rc) {
                            throw new DuplicateDataException(sample,
                                    DuplicateDataException.LAB_AND_LOCALLABID);
                        }

                        // Ensure the associated lab and provider are still
                        // active.
                        if (!lab.isActive) {
                            throw new InvalidDataException(
                                    sample,
                                    InvalidDataException.INACTIVE_ASSOCIATED_LAB);
                        }
                        if (!provider.isActive) {
                            throw new InvalidDataException(
                                    sample,
                                    InvalidDataException.INACTIVE_ASSOCIATED_PROVIDER);
                        }
                    }

                    // Generate a new unique sample id.
                    sample.id = idAgent.getNewSampleId(lock);
                    lock = lock.getPromotedVersion();
                    
                    // Populate the sample's date summary fields and possibly
                    // the localLabId field.
                    sample.firstActionDate = actionDate;
                    sample.lastActionDate = actionDate;
                    sample.releaseActionDate
                            = (sample.isPublic() ? actionDate : null);

                    // localLabId must have a value; set it to the sample id if
                    // none specified
                    if (sample.localLabId == null) {
                        sample.localLabId = Integer.toString(sample.id);
                    }
                }

                // Proceed with the update.
                dbWriteSample(lock.getConnection(), sample,
                        (oldsample != null), actionCode, userId, comments,
                        actionDate, revertedToHistoryId);
                if (oldsample != null) {
                    dbDeactivateSampleVersion(
                            lock.getConnection(), oldsample, sample);
                }
                dbWriteSampleVersion(lock.getConnection(), oldsample, sample);

                // Invalidate the cache.
                cachedSamples.invalidate(sample.id);

                // Possibly generate an ISM to announce the change if this is a
                // public sample.
                if (((oldsample == null) || !oldsample.isPublic())
                        && sample.isPublic()) {
                    /*
                     * Broadcast a SampleActivationISM to all sites. The
                     * sample's contents are sanitized before transmission. Also
                     * notify RepositoryManager that it may need to generate an
                     * ISM of its own.
                     */
                    siteManager.passCoreMessage(new SendIsmCM(
                            new SampleActivationISM(siteManager.localSiteId,
                                    sample)));
                    repositoryManager.passCoreMessage(new SampleStatusHintCM(
                            sample.id, 
                            SampleStatusHintCM.Trigger.SAMPLE_NEWLY_PUBLIC, 
                            null));
                } else if (((oldsample != null) && oldsample.isPublic())
                        && sample.isPublic()) {
                    /*
                     * Broadcast a SampleUpdateISM to all sites. The sample's
                     * contents are sanitized before transmission. No need to
                     * notify RepositoryManager in this case.
                     */
                    siteManager.passCoreMessage(new SendIsmCM(
                            new SampleUpdateISM(siteManager.localSiteId, 
                            sample)));
                } else if (((oldsample != null) && oldsample.isPublic())
                        && !sample.isPublic()) {
                    /*
                     * Generate a sample deactivation ISM and notify Repository
                     * Manager that it may need to generate an ISM of its own.
                     * Pass our ISM to Repository Manager, rather than
                     * transmitting it immediately, because if RepositoryManager
                     * decides to generate a RepositoryHoldingISM, its ISM must
                     * be sent before our SampleDeactivationISM. We'll trust
                     * RepositoryManager to transmit our ISM in a timely
                     * fashion.
                     */
                    repositoryManager.passCoreMessage(new SampleStatusHintCM(
                            sample.id, 
			    SampleStatusHintCM.Trigger.SAMPLE_NEWLY_NONPUBLIC,
			    new SampleDeactivationISM(
                                    siteManager.localSiteId, sample.id)));
                }

                // Read the newly-written sample back from the database and
                // return it (unless we just created a new sample, in which
                // case drop through...)
                if (oldsample != null) {
                    return getSampleInfo(sample.id, sample.historyId, lock);
                }
            } catch (SQLException ex) {
                // Database error.
                throw new OperationFailedException(ex);
            } finally {
                if (existingLock == null) {
                    lock.release();
                }
            }

            // Return the sample we just created to the caller.
            return getSampleInfo(sample.id, sample.historyId);
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Changes data for the specified sample back to the data that was
     * associated with a particular (previous) version of the sample. No data is
     * actually erased; instead a new workflow action is performed.
     * 
     * @param sampleId the id of the sample to be reverted
     * @param currentHistoryId the current history id of the sample; used for
     *        optimistic locking
     * @param desiredHistoryId the history id of the sample version to which the
     *        sample will be reverted
     * @param userId the id of the user that is to perform the revert action
     * @param comments the comments associated with this workflow action
     * @throws IllegalArgumentException if {@code existingLock} is not
     *         sufficient.
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
    public void revertSampleToVersion(int sampleId, int currentHistoryId,
            int desiredHistoryId, int userId, String comments)
            throws InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        revertSampleToVersion(sampleId, currentHistoryId, desiredHistoryId,
                userId, comments, null, false);
    }

    /**
     * Changes data for the specified sample back to the data that was
     * associated with a particular (previous) version of the sample. No data is
     * actually erased; instead a new workflow action is performed.
     * 
     * @param sampleId the id of the sample to be reverted
     * @param currentHistoryId the current history id of the sample; used for
     *        optimistic locking
     * @param desiredHistoryId the history id of the sample version to which the
     *        sample will be reverted
     * @param userId the id of the user that is to perform the revert action
     * @param comments the comments associated with this workflow action
     * @param existingLock an existing lock that must be adequite for this
     *        action
     * @param filesIncluded a boolean that indicates that this invocation is
     *        part of a revert action where files are being reverted as well
     * @throws IllegalArgumentException if {@code existingLock} is not
     *         sufficient.
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
    public void revertSampleToVersion(int sampleId, int currentHistoryId,
            int desiredHistoryId, int userId, String comments,
            AbstractLock existingLock, boolean filesIncluded)
            throws InconsistentDbException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        try {
            AbstractLock lock;
            
            if (existingLock != null) {
                if (!SampleLocks.managerRevertSampleToVersion_verifyExistingLock(
                        existingLock, sampleId)) {
                    throw new IllegalArgumentException();
                }
                lock = existingLock;
            } else {
                lock = SampleLocks.managerRevertSampleToVersion(sampleId);
                this.lockAgent.registerLock(lock);
                lock.acquire();
            }

            try {
                /*
                 * Retrieve the most current version of the specified sample
                 * and check for obvious errors; throws an exception if sampleId
                 * is invalid
                 */
                SampleInfo sample = getSampleInfo(sampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID, lock);

                if (!localLabs.contains(sample.labId)) {
                    throw new WrongSiteException(new LabInfo(sample.labId));
                }

                if (sample.mostRecentHistoryId != currentHistoryId) {
                    throw new OptimisticLockingException(
                            new SampleInfo(sampleId, currentHistoryId));
                }

                /*
                 * Retrieve the desired previous version of the specified
                 * sample; throws an exception if desiredHistoryId doesn't
                 * belong to the specified sample
                 */
                SampleInfo oldSample
                        = getSampleInfo(sampleId, desiredHistoryId, lock);

                // Prepare the old sampleInfo object to be re-written:
                oldSample.historyId = sample.historyId;
                oldSample.mostRecentHistoryId = sample.mostRecentHistoryId;
                // the old sample's originalSampleHistoryId remains unchanged

                // Write the new workflow action and retain sampleHistoryId
                putSampleInfo(
                        oldSample,
                        (filesIncluded
                                ? SampleWorkflowBL.REVERTED_INCLUDING_FILES
                                : SampleWorkflowBL.REVERTED_WITHOUT_FILES),
                        new Date(), userId, comments, desiredHistoryId, lock);
            } catch (DuplicateDataException ex) {
                // Can't happen from putSampleInfo() because we modified an
                // existing sample.
                throw new UnexpectedExceptionException(ex);
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
     * Return a new empty SearchParams
     */
    public SearchParams getEmptySearchParams() {
        return new SearchParams();
    }

    /**
     * Search samples with a given SearchParams. A "SearchId" will be returned.
     * PageHelper has to use getSearchResults method to fetch the search result.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-leve error.
     */
    public int storeSearchParams(SearchParams searchParams)
            throws OperationFailedException {
        int searchId = 0;

        try {
            // Get the requisite lock.
            AbstractLock lock = SampleLocks.managerStoreSearchParams();
            
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                
                // Generate a unique 31-bit search id.
                for (searchId = randomIdGenerator.nextInt(0x7FFFFFFF);
                        storedSearchParams.get(searchId) != null;
                        searchId = randomIdGenerator.nextInt(0x7FFFFFFF)) {
                    // Do nothing more; the point is to find a usable searchId
                }
    
                // Store the object in our cache
                storedSearchParams.put(searchId, searchParams);
            } finally {
                // Always release the lock, no matter what
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }

        // Signal the worker thread to begin pre-executing the search, if
        // the feature is enabled
        if (properties.getProperty("SamPreexecuteSearches").equals("true")) {
            passCoreMessage(new PreexecuteSearchCM(searchId));
        }

        return searchId;
    }

    /**
     * Retrieves a previously-stored SearchParams object. The caller would have
     * obtained the {@code searchId} value via a previous call to
     * {@code storeSearchParams}.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-leve error.
     * @throws ResourceNotFoundException with no embedded
     *         {@code identifier} if the specified {@code searchId}
     *         is not known.
     * @throws UnexpectedExceptionException
     */
    public SearchParams getSearchParams(int searchId)
            throws OperationFailedException,
            ResourceNotFoundException {
        try {
            AbstractLock lock = SampleLocks.managerGetSearchParams(searchId);
            SearchParams params;
            
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                params = storedSearchParams.get(searchId);
            } finally {
                // Always release the lock, no matter what
                lock.release();
            }

            if (params == null) {
                throw new ResourceNotFoundException();
            } else {
                return params.clone();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Return a complete list of {@code SampleInfo}s that match the specified
     * searchId.
     * 
     * @param searchId the search ID of the previously-performed search for
     *        which results are requested
     * @return a {@code SampleInfo[]} containing {@code SampleInfo} objects
     *         corresponding to all the search hits 
     * 
     * @throws IllegalArgumentException if {@code startIndex} is
     *         specified, but is greater than the number of samples returned by
     *         the search.
     * @throws InconsistentDbException if inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException with no embedded
     *         {@code identifier} if the specified {@code searchId}
     *         is not known.
     */
    public SampleInfo[] getSearchResults(int searchId)
            throws InconsistentDbException, OperationFailedException,
            ResourceNotFoundException {
        return getSearchResults(searchId, 0, 0);
    }

    /**
     * Return a partial list of SampleInfo's that match the specified searchId.
     * The returned array has most of its elements set to null, except at most
     * {@code maxSamples} elements beginning with array index
     * {@code startIndex}. Calling this version of {@code getSearchResults()}
     * may yield improved performance over the other version for RMI-connected
     * clients because of the reduced number of SampleInfo objects that have to
     * be marshalled and unmarshalled.
     * 
     * @param searchId the search ID of the previously-performed search for
     *        which results are requested
     * @param startIndex the index into the results of the first hit that should
     *        be returned
     * @param maxSamples the maximum number of real samples to return, or 0 to
     *        return all hits
     * @return a {@code SampleInfo[]} having length equal to the total number of
     *         hits, and having non-null elements corresponding to those hits
     *         at the indices specified via {@code startIndex} and
     *         {@code maxHits} 
     * 
     * @throws IllegalArgumentException if {@code startIndex} is
     *         specified, but is greater than the number of samples returned by
     *         the search.
     * @throws InconsistentDbException if inconsistency was detected in the
     *         database.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException with no embedded
     *         {@code identifier} if the specified {@code searchId}
     *         is not known.
     */
    public SampleInfo[] getSearchResults(int searchId, int startIndex,
            int maxSamples) throws InconsistentDbException,
            OperationFailedException, ResourceNotFoundException {
        try {
            
            /*
             * Obtain our first lock that will let us find out if the search
             * has been executed recently and its results are in the cache.
             * This lock acquisition will block until the search has finished
             * if another thread is in the process of executing the search
             * already.
             */
            AbstractLock lock = SampleLocks.managerGetSearchResults(searchId);
            
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Decide whether the search has been executed already or not.
                CachedSearchResults results = cachedSearchResults.get(searchId);
                if (results == null) {
                    // The search needs to be executed. Pass our lock so that
                    // it can be promoted.
                    results = executeSearch(searchId, lock, true);
                }

                // Filter the list of samples to be returned
                if ((startIndex == 0) && (maxSamples == 0)) {
                    // Fetch all samples
                    return getMultipleSampleInfo(results.sampleIdList,
                            results.sampleHistoryList, lock, true, true);
                } else {
                    // Early exit if there are no samples to return
                    if (results.sampleIdList.isEmpty()) {
                        return new SampleInfo[0];
                    }

                    // Detect the error condition where the caller-specified
                    // startIndex is higher than the number of samples
                    // available.
                    if ((startIndex < 0)
                            || (startIndex >= results.sampleIdList.size())) {
                        throw new IllegalArgumentException();
                    }

                    // Fetch some subset of samples. Our lock will be promoted
                    // along the way.
                    int endIndex = startIndex + maxSamples;
                    if (endIndex > results.sampleIdList.size()) {
                        endIndex = results.sampleIdList.size();
                    }
                    SampleInfo[] samplesSubset = getMultipleSampleInfo(
                            results.sampleIdList.subList(startIndex, endIndex),
                            results.sampleHistoryList.subList(startIndex,
                                    endIndex), lock, true, true);
                    SampleInfo[] samples
                            = new SampleInfo[results.sampleIdList.size()];
                    for (int i = 0; i < samplesSubset.length; i++) {
                        samples[i + startIndex] = samplesSubset[i];
                    }
                    return samples;
                }
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Returns the SampleStats container that represents the current status of
     * Sample Manager's performance counters.
     */
    public SampleStats getStats() {
        return stats.clone();
    }

    /**
     * Resets the SampleStats container and all the counters within it.
     */
    public void resetStats() {
        stats.reset();
    }

    /**
     * Returns the earliest action date of any row in the 'samples' table where
     * the local site is authoritative for the corresponding sample; this
     * corresponds with the earliest date that any workflow step was performed
     * on a locally-owned sample. Returns the current date if there are no
     * samples for which the local site is authoritative. This information is
     * used by OaiPmhResponder to handle OAI requests.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public Date getEarliestAuthoritativeHistoryDate()
            throws OperationFailedException {
        /*
         * FIXME: this implementation always obtains a global read lock
         * and fetches data directly from the database; perhaps the result of
         * that operation could be cached somehow for greater efficiency
         */
        // Obtain the right kind of lock
        AbstractLock lock
                = SampleLocks.managerGetEarliestAuthoritativeHistoryDate();
        
        // Build the SQL query that will return the correct answer
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT MIN(s.firstActionDate) AS d FROM samples s");
        sql.append("  WHERE (");
        boolean firstElement = true;
        for (Integer labId : localLabs) {
            if (!firstElement) {
                sql.append(" OR ");
            }
            firstElement = false;
            sql.append("lab_id=");
            sql.append(labId);
        }
        sql.append(");");

        try {
            this.lockAgent.registerLock(lock);
            lock.acquire();

            // Execute the SQL query and extract the date from the resultset
            try {
                Statement cmd = lock.getConnection().createStatement();
                try {
                    ResultSet rs = cmd.executeQuery(sql.toString());
                    
                    return (rs.next() ? rs.getTimestamp("d") : new Date());
                } finally {
                    cmd.close();
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
     * Gets the next auto-generated local lab id for the given lab if
     * auto-generation has been enabled.
     * 
     * @param labId indicates the lab whose auto-id generation rules are being
     *        consulted
     * @return the next avaialable automatically generated LocalLabId for the
     *         given lab or null if auto-generation is not enabled for the lab
     *         or all of the automatic id values have already been used
     * @throws ResourcesExhaustedException with the
     *         {@code SAMPLE_AUT_LOCAL_LAB_IDS_EXHAUSTED} reason code if
     *         auto-numbering is enabled but all of the possible sample numbers
     *         are taken.
     * @throws OperationFailedException if any exceptions are thrown while
     *         accessing the database
     */
    public String getNextUnusedLocalLabId(int labId)
            throws OperationFailedException {
        String prefix = properties.getProperty("SamLocalLabIdPrefix" + labId);
        if (prefix == null) {
            return null;
        }
        int digits = Integer.parseInt(
                properties.getProperty("SamLocalLabIdAutoDigits" + labId));

        String sql = "SELECT localLabId" + " FROM samples"
                + " WHERE samples.lab_id=" + labId
                + " AND samples.localLabId REGEXP " + "'^" + prefix
                + "[[:digit:]]{" + digits + "}$'" + " ORDER BY localLabId;";

        // Create a DecimalFormat to format an integer with zeros padding the
        // width the the preferred size
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            pattern.append("0");
        }
        DecimalFormat formatter = new DecimalFormat(pattern.toString());

        try {
            AbstractLock lock = SampleLocks.managerGetNextUnusedLocalLabId();
            
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                StringBuilder consideredLocalLabId = new StringBuilder(prefix);
                Statement cmd = lock.getConnection().createStatement();

                try {
                    ResultSet rs = cmd.executeQuery(sql);
                    
                    for (int consideredNumber = 1; rs.next();
                            consideredNumber++) {
                        consideredLocalLabId.setLength(prefix.length());
                        consideredLocalLabId.append(
                                formatter.format(consideredNumber));
                        if (!consideredLocalLabId.toString().equals(
                                rs.getString(1))) {
                            // The id has not been used; stop searching
                            break;
                        }
                    }
                } finally {
                    cmd.close();
                }
                if (consideredLocalLabId.length() > (digits + prefix.length())) {
                    // out of ids!
                    throw new ResourcesExhaustedException(
                            ResourcesExhaustedException.SAMPLE_AUTO_LOCAL_LAB_IDS_EXHAUSTED);
                }
                return consideredLocalLabId.toString();
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
     *
     * INTRA-CORE METHODS (not remotely accessible)
     *
     *************************************************************************/



    /**
     * Allows other Core modules to pass a message to this one.
     * 
     * @throws OperationFailedException only if Sample Manager was initialized
     *         in bootstrap mode, and then only if an error occurred while
     *         processing the specified {@code msg}. Such errors are
     *         reported synchronously in bootstrap mode because message
     *         processing happens synchronously in bootstrap mode.
     */
    public void passCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        if (bootstrapMode) {
            // We process all messages synchronously when we're in bootstrap
            // mode.
            processCoreMessage(msg);
        } else {
            // Place the message in the queue for future pickup by the
            // worker thread.
            messageQueue.send(msg);
        }
    }

    /**
     * Called by CoreLoader during a 'dbupdate' operation to rebuild the
     * contents of the searchAtoms table based upon existing sample metadata
     * records. This might be necessary due to a recent db schema change for
     * example.
     * 
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws InconsistentDbException if an inconsistency in the database was
     *         detected.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void rebuildSearchAtoms() throws InconsistentDbException,
            OperationFailedException {
        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("not in bootstrap mode");
        }

        try {
            AbstractLock lock = SampleLocks.managerRebuildSearchAtoms();
            
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // clear all existing rows from the searchAtoms table
                Statement cmd = lock.getConnection().createStatement();
                cmd.executeUpdate("DELETE FROM searchAtoms;");

                /*
                 * Run a database query to obtain all known sample IDs, then
                 * invoke updateSearchAtoms() for each one.
                 */
                String sql = "SELECT id, current_sampleHistory_id"
                        + " FROM samples;";
                cmd = lock.getConnection().createStatement();
                try {
                    ResultSet rs = cmd.executeQuery(sql);
                    while (rs.next()) {
                        int sampleId = rs.getInt("id");
                        int sampleHistoryId
                                = rs.getInt("current_sampleHistory_id");
                        SampleInfo sample = dbFetchSample(lock.getConnection(),
                                sampleId, sampleHistoryId, false);
                        
                        updateSearchAtoms(lock.getConnection(), sample);
                    }
                } finally {
                    cmd.close();
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
     * Called by CoreLoader during a 'dbupdate' operation to rebuild the
     * contents of the searchLocalHoldings table based upon existing sample
     * metadata records. This might be necessary due to a recent db schema
     * change for example.
     * 
     * @throws DeadlockDetectedException
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void rebuildSearchLocalHoldings() throws DeadlockDetectedException,
            OperationFailedException {
        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("not in bootstrap mode");
        }

        try {
            // First, clear the searchLocalHoldings table
            AbstractLock lock = SampleLocks.managerRebuildSearchLocalHoldings();
            
            this.lockAgent.registerLock(lock);
            lock.acquire();
            try {
                // clear all existing rows from the table
                Statement cmd = lock.getConnection().createStatement();
                
                try {
                    cmd.executeUpdate("DELETE FROM searchLocalHoldings;");
                } finally {
                    cmd.close();
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            } finally {
                lock.release();
            }
        } catch (DeadlockDetectedException ex) {
            throw new OperationFailedException(ex);
        }

        /*
         * Instruct Repository Manager to send us a SampleHoldingCM for each
         * sample.  We're in bootstrap mode, so these CMs will be
         * delivered and processed synchronously (before we regain control
         * here) and thus there is no concurrency risk. There would be a
         * concurrency risk if it were possible that another thread might try to
         * read from searchLocalHoldings during this time, however.
         */
        repositoryManager.requestHoldingsDump();
    }

    /**
     * Called by CoreLoader during a 'dbupdate' operation to regenerate auto-
     * computed values that are stored in the sampleData table. The three
     * auto-computed fields are v, dcalc, and formulaWeight. For each sample
     * where at least one of the three auto-computed fields' values will change,
     * the version number is incremented and the DB_REBUILT workflow action is
     * performed. Auto-computed values are only auto-computed for samples that
     * are owned by the local site - for which the labId represents a "local
     * lab". Known samples that are not owned by the local site are not updated
     * by this method.
     * 
     * @return the number of owned samples whose auto-computed fields were
     *         updated
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws InconsistentDbException if an inconsistency was detected in the
     *         database.
     * @throws InvalidDataException might happen as a sample is updated.
     * @throws InvalidModificationException might happen as a sample is updated
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws UnexpectedExceptionException
     */
    public int rebuildSampleData() throws InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException {
        SampleInfo[] samples;
        int countSamplesUpdated = 0;

        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("not in bootstrap mode");
        }

        // Get a list of all samples owned by the local site.
        
        /*
         * TODO: Perhaps we should process samples in chunks, rather than
         * requesting that all of them be fetched at once.
         */
        try {
            SearchParams search = getEmptySearchParams();
            
            search.addToHeadWithAnd(new RequireAuthoritativeSC());
            samples = getSearchResults(storeSearchParams(search));
        } catch (ResourceNotFoundException ex) {
            // Can't happen because we fetched the search by an id we just
            // obtained.
            throw new UnexpectedExceptionException(ex);
        }

        /*
         * TODO: there is a slight concurrency risk here since the call to
         * getAllOwnedSamples() above obtained a global read lock on our
         * behalf and then released it, and then below we make many calls
         * to putSampleInfo() that each acquire a single-sample write
         * lock on our behalf. Possibly recode all this so that
         * a single db lock is held for the duration of this method.
         * As long as this function is called only from bootstrap mode,
         * though, there is no risk.
         */

        // Iterate through every owned sample.
        for (int i = 0; i < samples.length; i++) {
            SampleInfo sample = samples[i];

            // Determine whether its auto-generated values are correct or
            // if they need to be updated.
            double oldV = sample.dataInfo.v;
            double oldDcalc = sample.dataInfo.dcalc;
            double oldFormulaWeight = sample.dataInfo.formulaWeight;
            SampleMathBL.calculateVolume(sample.dataInfo);
            SampleMathBL.calculateFormulaWeight(sample);
            SampleMathBL.calculateDensity(sample.dataInfo);
            if ((Double.compare(oldV, sample.dataInfo.v) != 0)
                    || (Double.compare(oldDcalc, sample.dataInfo.dcalc) != 0)
                    || (Double.compare(oldFormulaWeight,
                            sample.dataInfo.formulaWeight) != 0)) {
                try {
                    putSampleInfo(sample, SampleWorkflowBL.DB_REBUILT,
                            UserInfo.INVALID_USER_ID, null);
                    countSamplesUpdated++;
                } catch (DuplicateDataException ex) {
                    // Can't happen because we modified an existing sample.
                    throw new UnexpectedExceptionException(ex);
                } catch (WrongSiteException ex) {
                    // Can't happen because we filtered by
                    // locally-authoritative samples.
                    throw new UnexpectedExceptionException(ex);
                }
            }
        }

        return countSamplesUpdated;
    }

    /**
     * Called by CoreLoader during 'dbupdate' and 'dbrebuild' operations to
     * rebuild the contents of the searchUnitCells table based upon existing
     * sample metadata records. This might be necessary due to a recent db
     * schema change for example.
     * 
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws InconsistentDbException if an inconsistency in the database was
     *         detected.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void rebuildSearchUnitCells() throws InconsistentDbException,
            OperationFailedException {
        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("not in bootstrap mode");
        }

        try {
            AbstractLock lock = SampleLocks.managerRebuildSearchUnitCells();
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Run a database query to clear all existing rows from
                // searchAtoms
                Statement cmd = lock.getConnection().createStatement();
                
                try {
                    cmd.executeUpdate("DELETE FROM searchUnitCells;");
                } finally {
                    cmd.close();
                }

                // Run a database query to obtain all known sample IDs, then
                // iterate over the IDs  and invoke updateSearchUnitCells() once
                // for each one.
                String sql = "SELECT id, current_sampleHistory_id"
                        + " FROM samples;";
                cmd = lock.getConnection().createStatement();
                try {
                    ResultSet rs = cmd.executeQuery(sql);
                    while (rs.next()) {
                        int sampleId = rs.getInt("id");
                        int sampleHistoryId
                                = rs.getInt("current_sampleHistory_id");
                        SampleInfo sample = dbFetchSample(lock.getConnection(),
                                sampleId, sampleHistoryId, false);
                        
                        updateSearchUnitCells(lock.getConnection(), sample);
                    }
                } finally {
                    cmd.close();
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
     * Called by CoreLoader during 'dbupdate' and 'dbrebuild' operations to
     * rebuild the contents of the searchSpaceGroups table based upon existing
     * sample metadata records. This might be necessary due to a recent db
     * schema change for example.
     * 
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws InconsistentDbException if an inconsistency in the database was
     *         detected.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void rebuildSearchSpaceGroups() throws InconsistentDbException,
            OperationFailedException {
        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("not in bootstrap mode");
        }

        try {
            AbstractLock lock = SampleLocks.managerRebuildSearchSpaceGroups();
            this.lockAgent.registerLock(lock);
            lock.acquire();

            try {
                // Run a database query to clear all existing rows from
                // searchAtoms
                Statement cmd = lock.getConnection().createStatement();
                cmd.executeUpdate("DELETE FROM searchSpaceGroups;");

                // Run a database query to obtain all known sample id's. Keep
                // the ResultSet open. Iterate through each sample id that
                // exists and call updateSearchSpaceGroups() for each one.
                String sql = "SELECT id, current_sampleHistory_id"
                        + " FROM samples;";
                cmd = lock.getConnection().createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery(sql);
                    
                    while (rs.next()) {
                        int sampleId = rs.getInt("id");
                        int sampleHistoryId
                                = rs.getInt("current_sampleHistory_id");
                        SampleInfo sample = dbFetchSample(lock.getConnection(),
                                sampleId, sampleHistoryId, false);
                        
                        updateSearchSpaceGroups(lock.getConnection(), sample);
                    }
                } finally {
                    cmd.close();
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
     * Causes SampleIdAgent's periodic self-checks to be run.  In turn, some
     * sample id block proposals may be generated or some emergency loans may
     * be requested.  There is a possibility of a deadlock risk here, depending
     * on which thread invokes the function.  Therefore, this function is
     * accessible only during bootstrap mode, when there is only one thread and
     * absolutely no deadlock risk.
     * @throws IllegalStateException if the core modules are not in bootstrap
     *     mode.
     * @throws DeadlockDetecteException
     * @throws OperationFailedException
     */
    public void runPeriodicSampleIdBlockCheck() 
            throws DeadlockDetectedException, OperationFailedException {
	if (!this.bootstrapMode) {
	    throw new IllegalStateException();
	}
	this.idAgent.periodicCheck();
    }

    /**
     * Simple pass-thru to SampleIdAgent.countUnusedSampleIds().
     * @return the number of ID's available.
     * @throws DeadlockDetectedException
     * @throws OperationFailedException
     */
    public int countUnusedSampleIds() throws DeadlockDetectedException, 
            OperationFailedException {
	return this.idAgent.countUnusedSampleIds();
    }

    /**
     * Causes the local site to re-announce a particular sample to the rest of
     * the Site Network.  This is sensical only for a sample that has a public
     * status and for which the local site is authoritative.  This method
     * generates a SampleUpdateISM.  It may be useful if the local site's 
     * metadata has changed in an unusual way or if there is some uncertainty
     * in the Site Network regarding this sample.

     * @param sampleId identifies the sample to be re-advertised.
     * @throws IllegalArgumentException if the specified sample is not public.
     * @throws ResourceNotFoundException if the sampleId is not known.
     * @throws WrongArgumentException if the local site is not authoritative
     *      for the specified sample.
     */
    public void readvertiseSample(int sampleId) 
	    throws InconsistentDbException, OperationFailedException, 
            ResourceNotFoundException, WrongSiteException {
	SampleInfo sample = this.getSampleInfo(sampleId);
	LabInfo lab = this.siteManager.getLabInfo(sample.labId);
	if (lab.homeSiteId != this.siteManager.localSiteId) {
	    throw new WrongSiteException();
	}
	if (!sample.isPublic()) {
	    throw new IllegalArgumentException();
	}
	this.siteManager.passCoreMessage(new SendIsmCM(new SampleUpdateISM(
	        this.siteManager.localSiteId, sample)));
    }



    /**************************************************************************
     *
     * INTERNAL HELPER METHODS (not remotely accessible)
     *
     *************************************************************************/



    /**
     * Accepts a {@code CoreMessage} that is addressed to Sample Manager,
     * processes it, and possibly updates Sample Manager's internal state or
     * database tables. This is a dispatcher method: it delegates to other
     * methods named {@code event...()} depending upon the type of
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
     * cases. If Sample Manager is in bootstrap mode, this implies that this
     * method was invoked synchronously by a user-interactive thread, so the
     * exception is re-thrown by this method for eventual display to the user.
     * If Sample Manager is not in bootstrap mode, then the failure is reported
     * to {@code SiteManager} (if {@code msg} is an
     * {@code InterSiteMessage}) and nothing more is done because this
     * method was probably invoked by a worker thread (rather than a user
     * thread), and has no mechanism other than the logfile for communicating
     * with the user.
     * 
     * @param msg the core message to be processed.
     * @throws OperationFailedException only if Sample Manager was initialized
     *         in bootstrap mode, and then only if an error was encountered
     *         while processing {@code msg}. The nested
     *         {@code Exception} will be the one thrown by whatever
     *         {@code event...()} method was dispatched to.
     */
    private void processCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        try {
            // Decode the message and branch accordingly. If an inter-site
            // message gets passed to us here, we can assume that the message
            // was addressed to Sample Manager at the local site and its
            // authenticity has already been verified.
            if (msg instanceof PingCM) {
                eventPing((PingCM) msg);
            } else if (msg instanceof LocalLabsChangedCM) {
                eventLocalLabsChanged((LocalLabsChangedCM) msg);
            } else if (msg instanceof SampleHoldingCM) {
                eventSampleHolding((SampleHoldingCM) msg);
            } else if (msg instanceof PreexecuteSearchCM) {
                eventPreexecuteSearch((PreexecuteSearchCM) msg);
            } else if (msg instanceof SampleIdBlockISM) {
                idAgent.processSampleIdBlockIsm((SampleIdBlockISM) msg);
            } else if (msg instanceof SampleIdBlockHintCM) {
                idAgent.processSampleIdBlockHintCm((SampleIdBlockHintCM) msg);
            } else if (msg instanceof SampleActivationISM) {
                eventSampleActivation((SampleActivationISM) msg);
            } else if (msg instanceof SampleUpdateISM) {
                eventSampleUpdate((SampleUpdateISM) msg);
            } else if (msg instanceof SampleDeactivationISM) {
                eventSampleDeactivation((SampleDeactivationISM) msg);
            } else {
                siteManager.recordLogRecord(
                        LogRecordGenerator.cmUnknownType(this, msg));
            }
        } catch (Exception ex) {
            // Generic catch-all for any sort of exception. This method is
            // called from a worker thread, not a user thread, so the only
            // means for us to report an exception is to log it.
            siteManager.recordLogRecord(
                    LogRecordGenerator.cmCausedException(this, msg, ex));

            // Report the error. We choose an appropriate notification
            // channel depending on whether Site Manager is in bootstrap mode
            // or not and the type of the message.
            if (this.bootstrapMode) {
                // Report the error synchronously to calling code via Java's
                // usual exception mechanism.
                throw new OperationFailedException(ex);
            } else if (msg instanceof InterSiteMessage) {
                // Send a special failure message to ReceivedMessageAgent (via
                // our message queue) in order to avoid completely stalling
                // ISM processing.
                siteManager.passCoreMessage(
                        ProcessedIsmCM.failure((InterSiteMessage) msg, ex));
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

    /**
     * Executed whenever Site Manager's list of local labs has changed
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventLocalLabsChanged(
            @SuppressWarnings("unused") LocalLabsChangedCM msg)
            throws DeadlockDetectedException, OperationFailedException {
        
        /*
         * This is not a deadlock risk because we process core messages
         * asynchronously. i.e. Site Manager will have finished sending
         * this message some time before this method gets called.
         */
        int i;
        int labIds[];
        AbstractLock lock = SampleLocks.managerEventLocalLabsChanged();
        
        this.lockAgent.registerLock(lock);
        lock.acquire();
        try {
            localLabs.clear();
            labIds = siteManager.getLocalLabs();
            for (i = 0; i < labIds.length; i++) {
                localLabs.add(Integer.valueOf(labIds[i]));
            }
        } finally {
            lock.release();
        }
    }

    /**
     * Executed whenever Repository Manager notifies us of a change in a
     * sample's holding level.
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventSampleHolding(SampleHoldingCM msg)
            throws DeadlockDetectedException, OperationFailedException {
        AbstractLock lock = SampleLocks.managerEventSampleHolding();
        this.lockAgent.registerLock(lock);
        lock.acquire();
        try {
            dbAddUpdateDeleteLocalHolding(lock.getConnection(), msg.sampleId,
                    msg.replicaLevel);
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }
    }

    /**
     * Executed in response to a trigger sent during a user's previous call to
     * storeSearchParams(). We will pre-execute the search and attempt to have
     * search results available (in the cache) before the user invokes
     * getSearchResults().
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the search id contained within the
     *         message is unknown.
     */
    private void eventPreexecuteSearch(PreexecuteSearchCM msg)
            throws DeadlockDetectedException, OperationFailedException,
            ResourceNotFoundException {
        // Let executeSearch() acquire and release its own db lock; tell it not
        // to block if another thread is already executing the search.
        executeSearch(msg.searchId, null, false);
    }

    /**
     * Executed when a SampleActivationISM arrives from another site and needs
     * to be processed. The enclosed sample metadata is added to the local
     * site's database.
     * 
     * There is graceful handling for the case where a sample record for the
     * specified sample id already exists.  In such a case, the existing 
     * sample record merely is updated.  This behavior was introduced in the
     * 0.9.1 codebase and is necessary to enable recovery from some
     * inconsistent state in the Site Network.  See bug #1910 for more
     * information.
     *
     * @throws DeadlockDetectedException on deadlock.
     * @throws InconsistentDbException possibly if the sample could not be
     *         written.
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if
     *         the ISM did not originate from the Coordinator or the embedded
     *         sample's associated lab's home site, or
     *         {@code RESOURCE_ALREADY_ADVERTISED} if the message's
     *         sample id is already known.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the message's sample's associated
     *         lab or provider is not known.
     * @throws UnexpectedExceptionException
     */
    private void eventSampleActivation(SampleActivationISM msg)
            throws DeadlockDetectedException, InconsistentDbException,
            IsmProcessingException, OperationFailedException {
        // Validate the incoming ISM.
        LabInfo lab = siteManager.getLabInfo(msg.newSample.labId);
        if ((msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR)
                && (msg.sourceSiteId != lab.homeSiteId)) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        // Obtain a lock.
        AbstractLock lock
                = SampleLocks.managerEventSampleActivation(msg.newSample.id);
        this.lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Proceed with the update.
            dbEraseSample(lock.getConnection(), msg.newSample.id);
            dbWriteSample(lock.getConnection(), msg.newSample, false,
                    SampleWorkflowBL.REPLICATED_FROM_ELSEWHERE,
                    UserInfo.INVALID_USER_ID, msg.getSuggestedFileName(),
                    new Date(), SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
            // not necessary to call dbDeactivateSampleVersion() here
            // because this sample was previously unknown.
            dbWriteSampleVersion(lock.getConnection(), null, msg.newSample);
            cachedSamples.invalidate(msg.newSample.id);
            // Release the lock.
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }

        // Send a success message and return.
        siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded sample activation for " + msg.newSample.id
                        + " from lab " + msg.newSample.labId));
    }

    /**
     * Executed when a SampleUpdateISM arrives from another site and needs to be
     * processed. The enclosed sample metadata updates an existing copy of the
     * sample in the local site's database. In practice, this amounts to
     * deleting the old metadata and then adding the updated metadata.
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws InconsistentDbException possibly if the sample could not be
     *         written.
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if
     *         the ISM did not originate from the Coordinator or the embedded
     *         sample's associated lab's home site, or
     *         {@code RESOURCE_NOT_ADVERTISED} if the message's sample id
     *         is not already known.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the message sample's associated lab
     *         or provider is not known.
     * @throws UnexpectedExceptionException
     */
    private void eventSampleUpdate(SampleUpdateISM msg)
            throws DeadlockDetectedException, InconsistentDbException,
            IsmProcessingException, OperationFailedException,
            ResourceNotFoundException {
        // Validate the incoming ISM.
        LabInfo lab;
        try {
            getSampleInfo(msg.updatedSample.id); // ignore the result
        } catch (ResourceNotFoundException ex) {
            // FIXME: shouldn't use exceptions for flow control
            
            // This is an update for a sample we don't know about yet.
            throw new IsmProcessingException(msg,
                    IsmProcessingException.RESOURCE_NOT_ADVERTISED);
        }
        lab = siteManager.getLabInfo(msg.updatedSample.labId);
        if ((msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR)
                && (msg.sourceSiteId != lab.homeSiteId)) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        // Obtain a lock.
        AbstractLock lock
                = SampleLocks.managerEventSampleUpdate(msg.updatedSample.id);
        this.lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Proceed with the update. First erase any record of the
            // sample, then write as new record.
            dbEraseSample(lock.getConnection(), msg.updatedSample.id);
            dbWriteSample(lock.getConnection(), msg.updatedSample, false,
                    SampleWorkflowBL.REPLICATED_FROM_ELSEWHERE,
                    UserInfo.INVALID_USER_ID, msg.getSuggestedFileName(),
                    new Date(), SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
            // not necessary to call dbDeactivateSampleVersion() here
            // because this sample was just erased.
            dbWriteSampleVersion(lock.getConnection(), null, msg.updatedSample);
            cachedSamples.invalidate(msg.updatedSample.id);

            // Release the lock.
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }

        // Send a success message and return.
        siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded sample update for " + msg.updatedSample.id
                        + " from lab " + msg.updatedSample.labId));
    }

    /**
     * Executed when a SampleDeactivationISM arrives from another site and needs
     * to be processed. The sample identified by the ISM is removed from the
     * local site's database (presumably because the sample's authoritative site
     * has declared that the same is no longer public.)
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws InconsistentDbException possibly if the sample could not be
     *         written.
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if
     *         the ISM did not originate from the Coordinator or the embedded
     *         sample's associated lab's home site, or
     *         {@code RESOURCE_NOT_ADVERTISED} if the message's sample id
     *         is not already known.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the message sample's associated lab
     *         or provider is not known.
     * @throws UnexpectedExceptionException
     */
    private void eventSampleDeactivation(SampleDeactivationISM msg)
            throws DeadlockDetectedException, InconsistentDbException,
            IsmProcessingException, OperationFailedException,
            ResourceNotFoundException {
        // Validate the incoming ISM.
        SampleInfo sample;
        LabInfo lab;
        
        try {
            sample = getSampleInfo(msg.oldSampleId);
        } catch (ResourceNotFoundException ex) {
            // This is a deactivation for a sample we don't already know
            // about.
            throw new IsmProcessingException(msg,
                    IsmProcessingException.RESOURCE_NOT_ADVERTISED);
        }
        lab = siteManager.getLabInfo(sample.labId);
        if (((msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR)
                && (msg.sourceSiteId != lab.homeSiteId))
                || ((msg.sourceSiteId == InterSiteMessage.RECIPROCAL_NET_COORDINATOR)
                        && (lab.homeSiteId == siteManager.localSiteId))){
            /*
             * The ISM is in error because it was not issued by the site hosting
             * the lab that originated the sample, and either it also was not
             * issued by the Coordinator, or it pertains to a sample originated
             * by a lab currently hosted at *this* site.  (Even the Coordinator
             * cannot force us to deactivate our own sample.  If it attempts to
             * do so then it's broken or compromised.) 
             */
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }
        
        // Obtain a lock.
        AbstractLock lock
                = SampleLocks.managerEventSampleDeactivation(msg.oldSampleId);
        this.lockAgent.registerLock(lock);
        lock.acquire();

        try {
            // Proceed with the update.
            dbEraseSample(lock.getConnection(), msg.oldSampleId);
            cachedSamples.invalidate(msg.oldSampleId);

            // Release the lock.
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            lock.release();
        }

        // Send a success message and return.
        siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded sample deactivation for " + msg.oldSampleId
                        + " from lab " + sample.labId));
    }

    /**
     * Causes the specified search (defined by a SearchParams object from the
     * {@code storedSearchParams} map) to be executed and the resulting
     * CachedSearchResults object to be stored in the
     * {@code cachedSearchResults} map. The same CachedSearchResults
     * object is also returned for convenience The caller's existing lock will
     * be promoted if necessary to allow the search to execute. If
     * {@code existingLock} is null then a new lock is acquired and
     * released.
     * <p>
     * If another thread happens to be executing the search already (but results
     * are not available yet), the {@code shouldBlock} parameter controls
     * whether this method will block until the other thread finishes the search
     * (and returns a CachedSearchResults), or returns null immediately (having
     * taken no action). Note that if {@code shouldBlock} is
     * {@code false} then there is no guarantee that results are
     * available yet in the {@code cachedSearchResults} map when the
     * method returns.
     * <p>
     * No action is taken, but a CachedSearchResults is still returned, if
     * search results already are available for the specified search id.
     * 
     * @throws DeadlockDetectedException on deadlock.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified search id is not
     *         known.
     * @throws UnexpectedExceptionException
     */
    private CachedSearchResults executeSearch(int searchId,
            AbstractLock existingLock, boolean shouldBlock)
            throws DeadlockDetectedException, OperationFailedException,
            ResourceNotFoundException {
        AbstractLock lock;

        // Acquire a new lock or promote the caller's existing lock,
        // as appropriate.
        if (existingLock != null) {
            lock = SampleLocks.managerExecuteSearch(searchId, existingLock);
            this.lockAgent.registerLock(lock);
            /*
             * No need to bother testing to see if the existingLock had
             * sufficient privileges, it's just quicker and simpler to acquire
             * the privileges we need directly.
             */
            lock.promoteFrom(existingLock);
        } else {
            lock = SampleLocks.managerExecuteSearch(searchId, null);
            this.lockAgent.registerLock(lock);
            lock.acquire(shouldBlock);
            if (!lock.isActive()) {
                /*
                 * Another thread is already executing this search and we aren't
                 * allowed to block. Return gracefully.
                 */
                return null;
            }
        }

        try {
            // Exit early if search results are already available
            CachedSearchResults results = cachedSearchResults.get(searchId);
            
            if (results != null) {
                return results;
            }

            SearchParams params = storedSearchParams.get(searchId);
            if (params == null) {
                // unknown search id
                throw new ResourceNotFoundException();
            }

            /*
             * TODO: Demote the lock here because we don't need
             * local-labs-iterate access any more. Possible performance to be
             * gained.
             */

            /*
             * Execute the search and obtain the CachedSearchResults object
             * Store the resulting object in the cache for easy access next
             * time.
             */
            results = dbDoSearch(
                    lock.getConnection(), searchId, params, stats, localLabs);
            cachedSearchResults.put(searchId, results);
            
            return results;
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        } finally {
            if (existingLock == null) {
                lock.release();
            }
        }
    }

    /**
     * Internal function that writes portions of a new sample, or the first
     * version of a new sample, to the database. Inserts new rows into the
     * samples and sampleHistory table as necessary. The caller is responsible
     * for updating the sampleData, sampleAttributes, sampleAnnotations,
     * sampleAcls, and all of the search tables. The {@code sample.id}
     * field must be set before this function is called, even if a new sample is
     * being created. The method populates the fields
     * {@code sample.historyId} and
     * {@code sample.mostRecentHistoryId} with the id of the history row
     * just inserted.
     * 
     * @param conn a valid database connection. The caller must have acquired
     *        the appropriate kind of lock previously.
     * @param sample the SampleInfo object to be written to the database (or
     *        created). The {@code sample.id} must have been set to a
     *        unique id by the caller if a new sample is being created.
     * @param alreadyExists if false, a new row is inserted into the samples
     *        table. If true, the existing row in the samples table is updated.
     * @param actionCode integer value written to the {@code actionCode}
     *        field in the database.
     * @param userId integer value written to the {@code userId} field in
     *        the database. May be INVALID_USER_ID.
     * @param comments String value written to the {@code comments} field
     *        in the database. May be null.
     * @param revertedToSampleHistoryId integer value written to the
     *        {@code revertedToSampleHistoryId} field in the database to
     *        represent the sample version to which this is a reversion, if it
     *        is a reversion. May be INVALID_SAMPLE_HISTORY_ID in the case that
     *        this is not a reversion
     * @throws SQLException on database error.
     */
    private static void dbWriteSample(Connection conn, SampleInfo sample,
            boolean alreadyExists, int actionCode, int userId, String comments,
            Date actionDate, int revertedToSampleHistoryId)
            throws ResourceNotFoundException, SQLException {
        if (!alreadyExists) {
            // Insert a row into the samples table if one doesn't already exist
            dbAddSampleEntry(conn, sample);
        }

        // Write a row to the history table to obtain the new history id.
        SampleHistoryInfo history = new SampleHistoryInfo();
        history.sampleId = sample.id;
        history.action = actionCode;
        history.newStatus = sample.status;
        history.userId = userId;
        history.comments = comments;
        history.revertedToSampleHistoryId = revertedToSampleHistoryId;
        history.date = actionDate;
        history.id = dbAddHistoryEntry(conn, history);

        // Update the row in the samples table
        sample.historyId = history.id;
        sample.mostRecentHistoryId = history.id;
        sample.mostRecentStatus = sample.status;
        sample.mostRecentProviderId = sample.dataInfo.providerId;
        dbUpdateSampleEntry(conn, sample);
    }

    /**
     * <p>
     * Internal function that writes portions of a new version of an existing
     * sample, or the first version of a new sample, to the database. Inserts
     * new rows in the sampleData, sampleAttributes, sampleAnnotations,
     * sampleAcls and all of the "search" tables, as necessary. The caller is
     * responsible for updating the samples and sampleHistory tables.
     * Additionally, if the sample being written is a new version of an existing
     * sample, the caller is responsible for deactivating the appropriate rows
     * in sampleData, sampleAttributes, and sampleAnnotations.
     * </p><p>
     * For new or modified annotations or attributes on a sample, if
     * {@code SampleTextInfo.originalSampleHistoryId} is set to
     * {@code INVALID_SAMPLE_HISTORY_ID} it will be overwritten with the
     * history id for the new sample version. For modifications to of the fields
     * within a {@code SampleDataInfo} object where the
     * {@code originalSampleHistoryId} is unset or remains unchanged from the
     * previous version, it will be overwritten with the history id for the new
     * sample version. In all cases, if the {@code originalSampleHistoryId} was
     * updated prior to this call, the updated value will be stored unchanged.
     * </p>
     * 
     * @param conn a valid database connection. The caller must have acquired
     *        the appropriate kind of lock previously.
     * @param oldSample the immediate previous version of the sample to be
     *        written, or null if a new sample is to be written. Values inside
     *        this {@code SampleInfo} object are used to determine which
     *        data/attributes/annotations are new to this version and must be
     *        recorded in the database.
     * @param newSample the new version (or first version) of the sample to be
     *        written. The {@code newSample.id} and {@code newSample.historyId}
     *        must have been set by the caller previously, even if this is a new
     *        sample.
     * @throws SQLException on database error.
     */
    private static void dbWriteSampleVersion(Connection conn,
            SampleInfo oldSample, SampleInfo newSample) throws SQLException {
        // Insert a new row in the sampleData table if necessary.
        // dbDeactivateSampleVersion() hopefully would have deactivated the
        // old row already.
        if ((oldSample == null)
                || !newSample.dataInfo.equals(oldSample.dataInfo)) {
            newSample.dataInfo.firstSampleHistoryId = newSample.historyId;
            newSample.dataInfo.lastSampleHistoryId
                    = SampleHistoryInfo.STILL_ACTIVE;
            if ((newSample.dataInfo.originalSampleHistoryId
                    == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                    || (oldSample == null)
                    || (newSample.dataInfo.originalSampleHistoryId
                            == oldSample.dataInfo.originalSampleHistoryId)) {
                // the sample is different from the previous version but the
                // originalSampleHistoryId was the same or unset, indicating a
                // user initiated change to the data-- reset the
                // originalSampleHistoryId to the historyId of this action
                newSample.dataInfo.originalSampleHistoryId
                        = newSample.historyId;
            } else {
                // the originalSampleHistoryId is set to something different
                // than that of the previous version (probably because it was
                // and old version to which we are reverting) and that value
                // should be respected and retained by this method
            }
            newSample.dataInfo.sampleId = newSample.id;
            dbAddDataEntry(conn, newSample.dataInfo);
        }

        // Insert rows into sampleAttributes as necessary.
        List<SampleAttributeInfo> toBeAdded
                = new ArrayList<SampleAttributeInfo>(newSample.attributeInfo);
        if (oldSample != null) {
            toBeAdded.removeAll(oldSample.attributeInfo);
        }
        for (SampleAttributeInfo attribute : toBeAdded) {
            attribute.id = SampleTextInfo.INVALID_SAMPLE_TEXT_ID;
            attribute.firstSampleHistoryId = newSample.historyId;
            attribute.lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
            if (attribute.originalSampleHistoryId
                    == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                attribute.originalSampleHistoryId = newSample.historyId;
            }
            attribute.sampleId = newSample.id;
            dbAddAttributeEntry(conn, attribute);
            // previous line assigns the id of its choice
        }

        // Insert rows into sampleAnnotations as necessary.
        List<SampleAnnotationInfo> toBeAdded2
                = new ArrayList<SampleAnnotationInfo>(newSample.annotationInfo);
        if (oldSample != null) {
            toBeAdded2.removeAll(oldSample.annotationInfo);
        }
        for (SampleAnnotationInfo annotation : toBeAdded2) {
            annotation.id = SampleTextInfo.INVALID_SAMPLE_TEXT_ID;
            annotation.firstSampleHistoryId = newSample.historyId;
            annotation.lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
            if (annotation.originalSampleHistoryId 
                    == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                annotation.originalSampleHistoryId = newSample.historyId;
            }
            annotation.sampleId = newSample.id;
            dbAddAnnotationEntry(conn, annotation);
            // previous line assigns the id of its choice
        }

        // Insert rows into sampleAcls as necessary.
        List<SampleAccessInfo> toBeAdded3
                = new ArrayList<SampleAccessInfo>(newSample.accessInfo);
        if (oldSample != null) {
            toBeAdded3.removeAll(oldSample.accessInfo);
        }
        for (SampleAccessInfo access : toBeAdded3) {
            access.id = SampleAccessInfo.INVALID_SAMPLE_ACCESS_ID;
            access.sampleId = newSample.id;
            dbAddAccessEntry(conn, access);
            // previous line assigns the id of its choice
        }

        // Update whatever needs to be updated in the various index tables.
        updateSearchAtoms(conn, newSample);
        updateSearchUnitCells(conn, newSample);
        updateSearchSpaceGroups(conn, newSample);
    }

    /**
     * Internal function that deactivates portions of an existing sample,
     * usually performed just before new sample metadata is written. Deactivates
     * rows in sampleData, sampleAttributes, and sampleAnnotations, and deletes
     * from sampleAcls, as necessary. Data entries/ attributes/annotations/acl
     * entries that are present on {@code oldSample} but not on
     * {@code newSample} are deactivated/deleted.
     * 
     * @param conn a valid database connection. The caller must have acquired
     *        the appropriate kind of lock previously.
     * @param oldSample the version of the sample that reflects what's presently
     *        stored in the database. May not be null.
     * @param newSample a caller-modified version of the sample whose lack of
     *        particular data entries/attributes/annotations/acl entries
     *        indicates which db rows should be deactivated/deleted. The
     *        {@code newSample.id} and {@code newSample.historyId}
     *        must have been set by the caller previously.
     * @throws SQLException on database error.
     */
    private static void dbDeactivateSampleVersion(Connection conn,
            SampleInfo oldSample, SampleInfo newSample)
            throws SQLException {
        // Deactivate the row in the sampleData table if necessary.
        if (!newSample.dataInfo.equals(oldSample.dataInfo)) {
            dbDeactivateDataEntry(conn,
                    oldSample.dataInfo.firstSampleHistoryId,
                    newSample.historyId);
        }

        // Deactivate rows in sampleAttributes as necessary.
        List<SampleAttributeInfo> toBeDeleted
                = new ArrayList<SampleAttributeInfo>(oldSample.attributeInfo);
        
        toBeDeleted.removeAll(newSample.attributeInfo);
        for (SampleAttributeInfo attribute : toBeDeleted) {
            dbDeactivateAttributeEntry(conn, attribute.id, newSample.historyId);
        }

        // Deactivate rows in SampleAnnotations as necessary.
        List<SampleAnnotationInfo> toBeDeleted2
                = new ArrayList<SampleAnnotationInfo>(oldSample.annotationInfo);
        toBeDeleted2.removeAll(newSample.annotationInfo);
        for (SampleAnnotationInfo annotation : toBeDeleted2) {
            dbDeactivateAnnotationEntry(conn, annotation.id,
                    newSample.historyId);
        }

        // Delete rows in sampleAcls as necessary.
        List<SampleAccessInfo> toBeDeleted3
                = new ArrayList<SampleAccessInfo>(oldSample.accessInfo);
        toBeDeleted3.removeAll(newSample.accessInfo);
        for (SampleAccessInfo access : toBeDeleted3) {
            dbDeleteAccessEntry(conn, access.id);
        }
    }

    /**
     * Internal function that deletes all rows that pertain to a sample
     * (identified by {@code sampleId}, including those in samples,
     * sampleHistory, sampleData, sampleAttributes, sampleAnnotations,
     * sampleAcls, and the search tables. This operation is sensical only for
     * remotely-owned samples that are replicated here, but not for
     * locally-owned samples.
     * 
     * @throws SQLException
     */
    private static void dbEraseSample(Connection conn, int sampleId)
            throws SQLException {
        Statement cmd = conn.createStatement();

        try {
            cmd.executeUpdate("DELETE FROM samples WHERE id="
		    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM sampleHistory WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM sampleData WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM sampleAttributes WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM sampleAnnotations WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM sampleAcls WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM searchAtoms WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM searchLocalHoldings"
		    + " WHERE sample_id=" + sampleId + ";");
            cmd.executeUpdate("DELETE FROM searchSpaceGroups WHERE sample_id="
                    + sampleId + ";");
            cmd.executeUpdate("DELETE FROM searchUnitCells WHERE sample_id="
                    + sampleId + ";");
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function used to fetch and populate a single SampleInfo object
     * from the database. This method does not interface with the cache -- it
     * should be called incase of a cache miss. Set
     * sampleHistoryId=SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID to grab the
     * most recent version of the sample available. Returns null if the sample
     * could not be found. We assume the caller has already obtained the
     * appropriate database lock.
     * 
     * @throws InconsistentDbException
     * @throws SQLException
     */
    private static SampleInfo dbFetchSample(Connection conn, int sampleId,
            int sampleHistoryId, boolean gettingFullSampleInfo)
            throws InconsistentDbException, SQLException {
        SampleInfo sample;
        SampleAttributeInfo attribute;
        SampleAnnotationInfo annotation;
        SampleAccessInfo access;
        String sql;
        ResultSet rs;

        Statement cmd = conn.createStatement();

        try {
            // We can bypass the read of the table samples if we're in the
            // middle of fetching a whole FullSampleInfo object. These
            // values inside sample will be filled in by the caller later.
            if (gettingFullSampleInfo == false) {
                sql = "SELECT * FROM samples WHERE id=" + sampleId + ";";
                rs = cmd.executeQuery(sql);

                // If the specified sample could not be found, return null.
                // Otherwise, create a new SampleInfo and fill in its basic
                // members.
                if (!rs.next()) {
                    return null;
                }
                sample = new SampleInfo(rs);
                rs.close();

                // Populate the historyId and status fields of the sample. If
                // a version of the sample other than the most recent was
                // requested, this will require a db round-trip.
                if ((sampleHistoryId == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                        || (sampleHistoryId == sample.mostRecentHistoryId)) {
                    // Apparently the most recent version of the sample was
                    // just fetched.
                    sample.historyId = sample.mostRecentHistoryId;
                    sample.status = sample.mostRecentStatus;
                } else {
                    sample.historyId = sampleHistoryId;

                    // Query the sampleHistory table to find the right status
                    // value.
                    sql = "SELECT newStatus FROM sampleHistory" + " WHERE id="
                            + sampleHistoryId + ";";
                    rs = cmd.executeQuery(sql);
                    rs.next(); // If there is no history row, the exception
                    // is caught and rethrown below...
                    sample.status = rs.getInt("newStatus");
                    rs.close();
                }
            } else {
                sample = new SampleInfo();
                sample.id = sampleId;
                sample.historyId = sampleHistoryId;
                // We expect that our caller, who apparently is building a
                // FullSampleInfo object, will fill in most of the fields
                // in sample.
            }

            // Grab the appropriate row from table sampleData. Throw an
            // exception if no row exists.
            if (sampleHistoryId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                sql = "SELECT * FROM sampleData WHERE sample_id=" + sampleId
                        + " AND first_sampleHistory_id <= " + sampleHistoryId
                        + " AND (last_sampleHistory_id > " + sampleHistoryId
                        + "      OR last_sampleHistory_id IS NULL)" + ";";
            } else {
                sql = "SELECT * FROM sampleData WHERE sample_id=" + sampleId
                        + " AND last_sampleHistory_id IS NULL" + ";";
            }
            rs = cmd.executeQuery(sql);
            if (!rs.next()) {
                // Couldn't find sampleData row for this version of this sample
                throw new InconsistentDbException();
            }
            sample.dataInfo = new SampleDataInfo(rs);
            rs.close();

            // Grab potentially many rows from table sampleAttributes.
            if (sampleHistoryId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                sql = "SELECT * FROM sampleAttributes WHERE sample_id="
                        + sampleId + " AND first_sampleHistory_id <= "
                        + sampleHistoryId + " AND (last_sampleHistory_id > "
                        + sampleHistoryId
                        + " OR last_sampleHistory_id IS NULL)" + ";";
            } else {
                sql = "SELECT * FROM sampleAttributes WHERE sample_id="
                        + sampleId + " AND last_sampleHistory_id IS NULL" + ";";
            }
            rs = cmd.executeQuery(sql);
            while (rs.next()) {
                attribute = new SampleAttributeInfo(rs);
                sample.attributeInfo.add(attribute);
            }
            rs.close();

            // Grab potentially many rows from table sampleAnnotations.
            if (sampleHistoryId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                sql = "SELECT * FROM sampleAnnotations WHERE sample_id="
                        + sampleId + " AND first_sampleHistory_id <= "
                        + sampleHistoryId + " AND (last_sampleHistory_id > "
                        + sampleHistoryId
                        + " OR last_sampleHistory_id IS NULL)" + ";";
            } else {
                sql = "SELECT * FROM sampleAnnotations WHERE sample_id="
                        + sampleId + " AND last_sampleHistory_id IS NULL" + ";";
            }
            rs = cmd.executeQuery(sql);
            while (rs.next()) {
                annotation = new SampleAnnotationInfo(rs);
                sample.annotationInfo.add(annotation);
            }
            rs.close();

            // Grab potentially many rows from table sampleAcls.
            sql = "SELECT * FROM sampleAcls WHERE sample_id=" + sampleId + ";";
            rs = cmd.executeQuery(sql);
            while (rs.next()) {
                access = new SampleAccessInfo(rs);
                sample.accessInfo.add(access);
            }
            rs.close();

        } finally {
            cmd.close();
        }
        return sample;
    }

    /**
     * Internal function used to fetch and populate a CachedSearchResults object
     * by executing the specified search against the database. This method does
     * not interface with the cache -- it should be called in case of a cache
     * miss. We assume the caller has already obtained the appropriate database
     * lock.
     * 
     * @throws SQLException
     */
    private static CachedSearchResults dbDoSearch(Connection conn,
            int searchId, SearchParams search, SampleStats stats,
            Collection<Integer> localLabs) throws SQLException {
        CachedSearchResults results = new CachedSearchResults();

        PerfTimer perfTimer = new PerfTimer("dbDoSearch()");

        SearchConstraint head = search.getHead();

        // create a collection of parameter to be populated by SearchConstraints
        List<Object> parameters = new ArrayList<Object>();

        /*
         * Create a SearchTableTracker that will assign table aliases and
         * generate a FROM clause
         */
        SearchTableTracker tableTracker = new SearchTableTracker(head);

        /*
         * Create a SearchConstraintExtra info to provide information to the
         * SearchConstraints
         */
        SearchConstraintExtraInfo scei = new SearchConstraintExtraInfo(
                localLabs);

        /*
         * Generate the WHERE clause and populate the parameters collection.
         * This has the side effect of preparing the supplied tableTracker with
         * the information it needs to generate the FROM clause
         */
        String whereClause
                = head.getWhereClauseFragment(tableTracker, parameters, scei);

        // get the FROM clause from tableTracker
        String fromClause = tableTracker.getSqlFromClause();

        // create the ORDER BY clause
        String sortClause = null;
        
        switch (search.getSortOrder()) {
            case SearchParams.DEFAULT_SORT_ORDER:
                sortClause = "";
                break;
            case SearchParams.SORTBY_LOCALLABID:
                sortClause = " ORDER BY s.lab_id, s.localLabId";
                break;
            case SearchParams.SORTBY_LOCALLABID_REV:
                sortClause = " ORDER BY s.lab_id DESC, s.localLabId DESC";
                break;
            case SearchParams.SORTBY_LABID_LOCALLABID:
                sortClause = " ORDER BY s.localLabId";
                break;
            case SearchParams.SORTBY_LABID_LOCALLABID_REV:
                sortClause = " ORDER BY s.localLabId DESC";
                break;
            case SearchParams.SORTBY_CURRENT_SAMPLEHISTORY_ID:
                sortClause = " ORDER BY s.current_sampleHistory_id";
                break;
            case SearchParams.SORTBY_CURRENT_SAMPLEHISTORY_ID_REV:
                sortClause = " ORDER BY s.current_sampleHistory_id DESC";
                break;
            default:
                throw new IllegalStateException();
        }

        /*
         * generate the complete SQL query such that the results will be in two
         * columns, sampleId and currentSampleHistoryId
         */
        String sql = null;
        if (whereClause.length() > 0) {
            sql = "SELECT DISTINCT s.id, s.current_sampleHistory_id" + " FROM "
                    + fromClause + " WHERE " + whereClause + sortClause + ";";
        } else {
            sql = "SELECT DISTINCT s.id, s.current_sampleHistory_id" + " FROM "
                    + fromClause + sortClause;
        }

        /*
         * create a PreparedStatement that combines the SQL query with the
         * parameters
         */
        PreparedStatement cmd = conn.prepareStatement(sql);
        
        try {
            ListIterator<?> it = parameters.listIterator();
            
            while (it.hasNext()) {
                cmd.setObject(it.nextIndex() + 1, it.next());
            }

            ResultSet rs = cmd.executeQuery();
            stats.record(perfTimer);

            // Build our results object
            while (rs.next()) {
                results.sampleIdList.add(Integer.valueOf(rs.getInt("id")));
                results.sampleHistoryList.add(
                        Integer.valueOf(rs.getInt("current_sampleHistory_id")));
            }
        } finally {
            cmd.close();
        }

        results.searchId = searchId;
        return results;
    }

    /**
     * Internal function that inserts a row into the 'sampleHistory' table.
     * 
     * @return the id of the newly-inserted row
     * @throws SQLException on database error
     */
    private static int dbAddHistoryEntry(Connection conn,
            SampleHistoryInfo history) throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery("SELECT * FROM sampleHistory"
                    + " WHERE id=0;");
            rs.moveToInsertRow();
            history.dbStore(rs);
            rs.insertRow();

            // TODO: these next two lines are a horrible hack, but they seem to
            // work. recode this is the future to a more portable implementation.
            rs.next();
            return rs.getInt("id");
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function that updates an existing row in the 'samples' table.
     * 
     * @throws ResourceNotFoundException if no existing row has the
     *         {@code sample.id} specified
     * @throws SQLException on database error
     */
    private static void dbUpdateSampleEntry(Connection conn,
            SampleInfo sample)
            throws ResourceNotFoundException, SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery("SELECT * FROM samples WHERE id="
                    + sample.id + ";");
            if (!rs.next()) {
                throw new ResourceNotFoundException(sample);
            }
            sample.dbStore(rs);
            rs.updateRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that inserts a new row in the 'samples' table. The
     * specified {@code sample.id} value is used as the primary key.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddSampleEntry(Connection conn, SampleInfo sample)
            throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        
        try {
            ResultSet rs
                    = cmd.executeQuery("SELECT * FROM samples WHERE id=0;");
            
            rs.moveToInsertRow();
            sample.dbStore(rs);
            rs.insertRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that updates an existing row in the 'sampleData' table
     * and "deactivates" it by setting its 'last_sampleHistory_id' field to the
     * value specified.
     * 
     * @throws SQLException on database error.
     */
    private static void dbDeactivateDataEntry(Connection conn, int id,
            int lastSampleHistoryId) throws SQLException {
        Statement cmd = conn.createStatement();

        try {
            String sql = "UPDATE sampleData" + " SET last_sampleHistory_id="
                    + lastSampleHistoryId + " WHERE first_sampleHistory_id="
                    + id + ";";
            cmd.executeUpdate(sql);
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that inserts a new row into the 'sampleData' table. The
     * specified {@code data.first_sampleHistory_id} is used as the
     * primary key.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddDataEntry(Connection conn, SampleDataInfo data)
            throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery("SELECT * FROM sampleData"
                    + " WHERE first_sampleHistory_id=0;");
            rs.moveToInsertRow();
            data.dbStore(rs);
            rs.insertRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that updates an existing row in the 'sampleAttributes'
     * table and "deactivates" it by setting its 'last_sampleHistory_id' field
     * to the value specified.
     * 
     * @throws SQLException on database error.
     */
    private static void dbDeactivateAttributeEntry(Connection conn, int id,
            int lastSampleHistoryId) throws SQLException {
        Statement cmd = conn.createStatement();

        try {
            String sql = "UPDATE sampleAttributes SET last_sampleHistory_id="
                    + lastSampleHistoryId + " WHERE id=" + id + ";";
            cmd.executeUpdate(sql);
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that inserts a new row into the 'sampleAttributes' table.
     * The primary key is auto-assigned by the database engine.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddAttributeEntry(Connection conn,
            SampleAttributeInfo attribute) throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery(
                    "SELECT * FROM sampleAttributes WHERE id=0;");
            rs.moveToInsertRow();
            attribute.dbStore(rs);
            rs.insertRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that updates an existing row in the 'sampleAnnotations'
     * table and "deactivates" it by setting its 'last_sampleHistory_id' field
     * to the value specified.
     * 
     * @throws SQLException on database error
     */
    private static void dbDeactivateAnnotationEntry(Connection conn, int id,
            int lastSampleHistoryId) throws SQLException {
        Statement cmd = conn.createStatement();
        
        try {
            String sql = "UPDATE sampleAnnotations SET last_sampleHistory_id="
                    + lastSampleHistoryId + " WHERE id=" + id + ";";
            
            cmd.executeUpdate(sql);
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that inserts a new row into the 'sampleAnnotations'
     * table. The primary key is auto-assigned by the database engine.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddAnnotationEntry(Connection conn,
            SampleAnnotationInfo annotation) throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery(
                    "SELECT * FROM sampleAnnotations WHERE id=0;");
            
            rs.moveToInsertRow();
            annotation.dbStore(rs);
            rs.insertRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that deletes the specified row from the 'sampleAcls'
     * table.
     * 
     * @throws SQLException on database error.
     */
    private static void dbDeleteAccessEntry(Connection conn, int id)
            throws SQLException {
        Statement cmd = conn.createStatement();

        try {
            String sql = "DELETE FROM sampleAcls" + " WHERE id=" + id + ";";
            
            cmd.executeUpdate(sql);
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that inserts a new row into the 'sampleAcls' table. The
     * primary key is auto-generated by the database engine.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddAccessEntry(Connection conn,
            SampleAccessInfo access) throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery(
                    "SELECT * FROM sampleAcls WHERE id=0;");
            
            rs.moveToInsertRow();
            access.dbStore(rs);
            rs.insertRow();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function that returns true if a sample exists that has the
     * specified labId and localLabId values, or false if one does not. The
     * caller must hold a global read lock before using this function.
     * 
     * @throws SQLException on database error.
     */
    private static boolean dbScanForLocalLabId(Connection conn, int labId,
            String localLabId) throws SQLException {

        // Use a PreparedStatement instead of a normal statement so
        // that the caller's localLabId string will be escaped.
        PreparedStatement cmd = conn.prepareStatement(
                "SELECT id FROM samples WHERE lab_id=? AND localLabId=?;");

        try {
            cmd.setInt(1, labId);
            cmd.setString(2, localLabId);

            return cmd.executeQuery().next();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal method that makes changes to the 'searchLocalHoldings' table to
     * record that the specified sample is stored at the specified replicaLevel.
     * Will insert, update, or delete database rows as necessary.
     * 
     * @throws SQLException on database error.
     */
    private static void dbAddUpdateDeleteLocalHolding(Connection conn,
            int sampleId, int replicaLevel) throws SQLException {
        Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);

        try {
            String sql = "SELECT * FROM searchLocalHoldings WHERE sample_id="
                    + sampleId + ";";
            ResultSet rs = cmd.executeQuery(sql);
            
            if (rs.next()) {
                if (replicaLevel >= RepositoryHoldingInfo.BASIC_DATA) {
                    // update the existing row
                    rs.updateInt("repositoryHoldings_replicaLevel",
                            replicaLevel);
                    rs.updateRow();
                } else {
                    // delete the existing row
                    rs.deleteRow();
                }
            } else {
                if (replicaLevel >= RepositoryHoldingInfo.BASIC_DATA) {
                    // insert a new row
                    rs.moveToInsertRow();
                    rs.updateInt("sample_id", sampleId);
                    rs.updateInt("repositoryHoldings_replicaLevel",
                            replicaLevel);
                    rs.insertRow();
                } else {
                    // no database row is needed in this case; do nothing
                }
            }
        } finally {
            cmd.close();
        }
    }

    /**
     * Helper function for updateSearchAtoms that does the actual updating given
     * a empirical formula type to do the update for.
     * 
     * @throws SQLException on database error.
     */
    private static void updateSearchAtomsByType(Connection conn,
            SampleInfo sample, int type) throws SQLException {
        List<Integer> searchAtomsIdsToDelete = new ArrayList<Integer>();
        Map<Element, BigDecimal> desiredSearchAtoms;
        SampleAttributeInfo formulaAttribute
                = sample.getFirstAttributeOfType(type);

        if (formulaAttribute != null) {
            // Parse the sample's empirical formula
            try {
                desiredSearchAtoms = ChemicalFormulaBL.getAtomCounts(
                        ChemicalFormulaBL.parseFormula(formulaAttribute.value),
                        false);
            } catch (InvalidDataException ide) {
                /*
                 * The sample has an invalid formula, so no rows should be
                 * present in the searchAtoms table
                 */
                desiredSearchAtoms
                        = Collections.<Element, BigDecimal> emptyMap();
            } catch (IllegalArgumentException iae) {
                /*
                 * The atoms cannot be counted because the formula contains one
                 * or more abbreviations (or is invalid), so no rows should be
                 * present in the searchAtoms table
                 */
                desiredSearchAtoms
                        = Collections.<Element, BigDecimal> emptyMap();
            }
        } else {
            /*
             * The sample has no formula of the specified type, so no rows
             * should be present in the searchAtoms table
             */
            desiredSearchAtoms = Collections.<Element, BigDecimal> emptyMap();
        }

        // Read all the existing rows in searchAtoms associated with this
        // sample.
        String sql = "SELECT * FROM searchAtoms WHERE sample_id=? AND type=?;";
        PreparedStatement ps = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        try {
            ps.setInt(1, sample.id);
            ps.setInt(2, type);

            ResultSet rs = ps.executeQuery();

            // Delete any old rows that are no longer applicable, modify those
            // whose value has changed.
            while (rs.next()) {
                String existingElementSymbol = rs.getString("element");
                Element existingElement
                        = Element.forSymbol(existingElementSymbol);
                BigDecimal existingCount = rs.getBigDecimal("count");
                BigDecimal desiredCount
                        = desiredSearchAtoms.get(existingElement);

                if (desiredCount == null) {
                    /*
                     * The row should be deleted from searchAtoms because it is
                     * no longer desired or doesn't refer to a valid element. We
                     * can't delete it from the ResultSet now, however, because
                     * JDBC does not fully specify the effect of the change on
                     * the ResultSet. In testing it was found that the driver
                     * tested appeared to delete the row entirely from the
                     * result set, making the erstwhile next row the current
                     * row. Other drivers apparently replace the current row
                     * with a blank placeholder row.
                     */
                    searchAtomsIdsToDelete.add(rs.getInt("id"));
                } else if (!desiredCount.equals(existingCount)) {
                    // The row's count in searchAtoms should be updated.
                    rs.updateBigDecimal("count", desiredCount);
                    rs.updateRow();
                }
                desiredSearchAtoms.remove(existingElement);
            }

            // Add any new rows to searchAtoms that didn't exist before and
            // should now.
            for (Map.Entry<Element, BigDecimal> e
                    : desiredSearchAtoms.entrySet()) {
                rs.moveToInsertRow();
                rs.updateInt("sample_id", sample.id);
                rs.updateString("element", e.getKey().getSymbol());
                rs.updateInt("type", type);
                rs.updateBigDecimal("count", e.getValue());
                rs.insertRow();
            }
            rs.close();
        } finally {
            ps.close();
        }

        Statement cmd = conn.createStatement();

        try {
            /*
             * Generate and execute another SQL query to remove any excess rows
             * from searchAtoms flagged earlier for deletion.
             */
            if (!searchAtomsIdsToDelete.isEmpty()) {
                StringBuilder sb = new StringBuilder(
                        "DELETE FROM searchAtoms WHERE id=");

                for (Integer i : searchAtomsIdsToDelete) {
                    sb.append(i).append(" OR id=");
                }
                sb.setLength(sb.length() - " OR id=".length());
                sb.append(';');
                cmd.executeUpdate(sb.toString());
            }
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function that should be called immediately after a sample record
     * is changed. It updates the searchAtoms table to reflect the newest state
     * of this sample's metadata. The specified sample may be a newly-created
     * sample. The caller should hold a database write lock for the searchAtoms
     * table.
     * 
     * @throws SQLException on database error.
     */
    private static void updateSearchAtoms(Connection conn, SampleInfo sample)
            throws SQLException {
        updateSearchAtomsByType(conn, sample, SampleTextBL.EMPIRICAL_FORMULA);
        updateSearchAtomsByType(conn, sample,
                SampleTextBL.EMPIRICAL_FORMULA_DERIVED);
        updateSearchAtomsByType(conn, sample,
                SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION);
        updateSearchAtomsByType(conn, sample,
                SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT);
    }

    /**
     * Internal function that should be called immediately following a change to
     * a sample record. It updates the {@code searchUnitCells} database
     * table to reflect the newest state of this sample's metadata. The
     * specified sample may be a newly-created sample.
     * 
     * @param conn the db connection to use. It is normally accompanied by a
     *        lock the caller obtained previously that authorizes writes to the
     *        db table {@code searchUnitCells}.
     * @param sample the newly-modified sample record whose search index data is
     *        to be modified.
     * @throws SQLException on database error.
     */
    private static void updateSearchUnitCells(Connection conn, SampleInfo sample)
            throws SQLException {
        // Do some mathematical calculations on the sample now in order to make
        // searching faster later.
        SearchUnitCellsInfo suci =
                SampleMathBL.generateSearchUnitCellsInfo(sample);

        // Access the existing database row for this sample if one exists.
        String sql =
                "SELECT * FROM searchUnitCells WHERE sample_id=" + sample.id;
        Statement cmd =
                conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery(sql);

            // Add, modify, or delete the database row as appropriate.
            if (rs.next()) {
                if (suci == null) {
                    // Delete the existing db row since no RUC can be computed
                    // anymore.
                    rs.deleteRow();
                } else {
                    // Update the existing db row since the RUC may have
                    // changed.
                    suci.dbStore(rs);
                    rs.updateRow();
                }
            } else {
                if (suci != null) {
                    // Insert a db row since the RUC can now be computed.
                    rs.moveToInsertRow();
                    suci.dbStore(rs);
                    rs.insertRow();
                } // Otherwise, nothing to do
            }

            // Clean up.
            rs.close();
        } finally {
            cmd.close();
        }
    }

    /**
     * Internal function that should be called immediately following a change to
     * a sample record. It updates the {@code searchSpaceGroups} database
     * table to reflect the newest state of this sample's metadata. The
     * specified sample may be a newly-created sample.
     * 
     * @param conn the db connection to use. It is normally accompanied by a
     *        lock the caller obtained previously that authorizes writes to the
     *        db table {@code searchSpaceGroups}.
     * @param sample the newly-modified sample record whose search index data is
     *        to be modified.
     * @throws SQLException on database error.
     */
    private static void updateSearchSpaceGroups(Connection conn,
            SampleInfo sample) throws SQLException {
        // Do some mathematical calculations on the sample now in order to make
        // searching faster later.
        String canonicalSymbol = null;

        if (sample.dataInfo.spgp != null) {
            try {
                canonicalSymbol = SpaceGroupSymbolBL.createCanonicalSymbol(
                        SpaceGroupSymbolBL.createFormattedSymbol(
                                sample.dataInfo.spgp));
            } catch (InvalidDataException ex) {
                // The sample's space group is not valid. We cannot index it.
                // Drop through...
            }
        }

        // Access the existing database row for this sample if one exists.
        String sql = "SELECT * FROM searchSpaceGroups WHERE sample_id="
                + sample.id;
        Statement cmd = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        try {
            ResultSet rs = cmd.executeQuery(sql);

            // Add, modify, or delete the database row as appropriate.
            boolean rowExists = rs.next();
            
            if (!rowExists && (canonicalSymbol == null)) {
                // Nothing to do in this case.
            } else if (rowExists && (canonicalSymbol == null)) {
                // Delete the existing db row since no canonical symbol can be
                // determined anymore.
                rs.deleteRow();
            } else if (!rowExists && (canonicalSymbol != null)) {
                // Insert a db row since the canonical symbol now can be
                // determined
                rs.moveToInsertRow();
                rs.updateInt("sample_id", sample.id);
                rs.updateString("canonicalsymbol", canonicalSymbol);
                rs.insertRow();
            } else {
                // Update the existing db row since the canonical symbol may
                // have
                // changed.
                rs.updateString("canonicalsymbol", canonicalSymbol);
                rs.updateRow();
            }

            // Clean up.
            rs.close();
        } finally {
            cmd.close();
        }
    }

    /**
     * Fetches potentially many SampleInfo's into a single array. The
     * SampleInfo's are read from the cache where possible, then the cache
     * misses are fetched from the database via an efficient batch algorithm.
     * DB-fetched samples are put into the cache, up to the limit of cache
     * capacity.
     * 
     * @param sampleHistoryIds may be null, in which case the most recent
     *        version of every sample available is fetched.
     * @param existingLock may be null, in which case a new lock will be
     *        acquired and released during this function's lifetime.
     *        Alternately, the lock may have been acquired by the caller, and in
     *        this case the lock will be promoted for the appropriate operations
     *        if necessary.
     * @param preserveOrder if true, then the order of samples in the returned
     *        array matches the order of sample ids in {@code sampleIds},
     *        as specified by the caller. If false, then the order of samples
     *        returned is not defined.
     * @param alwaysGetMostCurrent if true, the most recent version of every
     *        sample is always returned. If the version returned is more recent
     *        than the sampleHistoryId specified for that sample (assuming the
     *        sampleHistoryId was not INVALID_HISTORY_ID), the
     *        {@code isMoreRecentThanSearch} flag on that sample is set.
     *        The function behaves normally when this value is false.
     * @throws DeadlockDetectedException
     * @throws InconsistentDbException
     * @throws OperationFailedException
     * @throws UnexpectedExceptionException
     */
    private SampleInfo[] getMultipleSampleInfo(int sampleIds[],
            int sampleHistoryIds[], AbstractLock existingLock,
            boolean preserveOrder, boolean alwaysGetMostCurrent)
            throws DeadlockDetectedException, InconsistentDbException,
            OperationFailedException {
        int i;
        PerfTimer perfTimer = new PerfTimer("getMultipleSampleInfo()");

        // Acquire/promote our lock as appropriate.
        AbstractLock lock = SampleLocks.managerGetMultipleSampleInfo(sampleIds,
                false, existingLock);
        
        this.lockAgent.registerLock(lock);
        if (existingLock != null) {
            lock.promoteFrom(existingLock);
        } else {
            lock.acquire();
        }

        try {
            SampleInfo samples[] = new SampleInfo[sampleIds.length];
            
            if (sampleHistoryIds == null) {
                sampleHistoryIds = new int[sampleIds.length];
                Arrays.fill(sampleHistoryIds,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
            }

            // Iterate through the requested sampleId's and read as many of
            // them as possible from the cache. Flag those samples that are
            // more recent than requested (if alwaysGetMostCurrent is true).
            int countCacheMisses = 0;
            for (i = 0; i < sampleIds.length; i++) {
                SampleInfo sample = cachedSamples.get(sampleIds[i]);
                
                if ((sample != null)
                        && (alwaysGetMostCurrent
                                || (sampleHistoryIds[i]
                                        == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                                || (sample.historyId == sampleHistoryIds[i]))) {
                    samples[i] = sample.clone();
                    samples[i].isMoreRecentThanSearch = alwaysGetMostCurrent
                            && (sampleHistoryIds[i]
                                    != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                            && (sample.historyId != sampleHistoryIds[i]);
                } else {
                    samples[i] = null;
                    countCacheMisses++;
                }
            }

            // Early exit if every sample was fetched from the cache
            if (countCacheMisses == 0) {
                return samples;
            }

            // Build two arrays that contain every sampleId/sampleHistoryId
            // that will need to be fetched from the database. Also construct
            // a map from sample id to array position for use later.
            int arrayPos = 0;
            int sampleIdsFromDb[] = new int[countCacheMisses];
            int sampleHistoryIdsFromDb[] = new int[countCacheMisses];
            Map<Integer, Integer> sampleIdToArrayPos = (preserveOrder
                    ? new HashMap<Integer, Integer>(countCacheMisses) : null);

            for (i = 0; i < samples.length; i++) {
                if (samples[i] == null) {
                    sampleIdsFromDb[arrayPos] = sampleIds[i];
                    sampleHistoryIdsFromDb[arrayPos] = sampleHistoryIds[i];
                    if (preserveOrder) {
                        sampleIdToArrayPos.put(sampleIds[i], i);
                    }
                    arrayPos++;
                }
            }

            // Promote our lock to allow the remaining samples to be read from
            // the database.
            AbstractLock oldLock = lock;
            lock = SampleLocks.managerGetMultipleSampleInfo(sampleIdsFromDb,
                    true, existingLock);
            this.lockAgent.registerLock(lock);
            lock.promoteFrom(oldLock);

            // Do the database fetch. Select the best (fastest) algorithm for
            // the fetch based on a configured threshold sample count.
            SampleInfo samplesFromDb[];
            try {
                int threshold = Integer.parseInt(
                        properties.getProperty("SamBulkFetchThreshold"));
                if (sampleIdsFromDb.length < threshold) {
                    // This algorithm is faster for small numbers of samples.
                    samplesFromDb = dbFetchMultipleSamplesWithOr(
                            lock.getConnection(), stats, sampleIdsFromDb,
                            sampleHistoryIdsFromDb, alwaysGetMostCurrent);
                } else {
                    // This algorithm is faster for large numbers of samples.
                    samplesFromDb = dbFetchMultipleSamplesWithTempTable(
                            lock.getConnection(), stats, sampleIdsFromDb,
                            sampleHistoryIdsFromDb, alwaysGetMostCurrent);
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }

            // Take the samples from the database and copy them to our output
            // array. Also put them into the sample cache at the same time.
            int samplesIntoCache = 0;
            int currentArrayPos = 0;
            
            for (SampleInfo sampleFromDb : samplesFromDb) {
                
                if (preserveOrder) {
                    arrayPos = sampleIdToArrayPos.get(sampleFromDb.id);
                    samples[arrayPos] = sampleFromDb.clone();
                } else {
                    while (samples[currentArrayPos] != null) {
                        currentArrayPos++;
                    }
                    samples[currentArrayPos] = sampleFromDb.clone();
                }
                if (samplesIntoCache < cachedSamples.getMaxSize()) {
                    // Add as many samples to the cache as the cache will hold,
                    // but trying to add samples beyond that just reduces
                    // the cache's efficiency.
                    cachedSamples.put(sampleFromDb.id, sampleFromDb);
                    samplesIntoCache++;
                }
            }

            // Log the operation and return to the caller.
            stats.record(perfTimer);
            return samples;
        } finally {
            if (existingLock == null) {
                lock.release();
            }
        }
    }

    /**
     * Similar to the function above, but translates the supplied collections
     * for {@code sampleIds} and {@code sampleHistoryIds} to
     * arrays prior to invoking it. Both collections should contain Integer
     * objects.
     * 
     * @throws DeadlockDetectedException
     * @throws InconsistentDbException
     * @throws OperationFailedException
     */
    private SampleInfo[] getMultipleSampleInfo(Collection<Integer> sampleIds,
            Collection<Integer> sampleHistoryIds, AbstractLock existingLock,
            boolean preserveOrder, boolean alwaysGetMostCurrent)
            throws DeadlockDetectedException, InconsistentDbException,
            OperationFailedException {
        int sampleIdsArray[] = new int[sampleIds.size()];
        int sampleHistoryIdsArray[] = null;
        int i = 0;
        
        for (Integer id : sampleIds) {
            sampleIdsArray[i++] = id.intValue();
        }

        if (sampleHistoryIds != null) {
            sampleHistoryIdsArray = new int[sampleHistoryIds.size()];
            i = 0;
            for (Integer histId : sampleHistoryIds) {
                sampleHistoryIdsArray[i++] = histId.intValue();
            }
        }

        return getMultipleSampleInfo(sampleIdsArray, sampleHistoryIdsArray,
                existingLock, preserveOrder, alwaysGetMostCurrent);
    }

    /**
     * Internal function used to fetch and populate multiple SampleInfo objects
     * from the database. This method does not interface with the cache, but
     * should be called in case of a cache miss. The caller must already have
     * obtained a lock that permits full read access to sample metadata tables
     * for the samples specified by {@code origSampleId}.
     * <p>
     * The current implementation fetches samples from the database using a big
     * WHERE clause that enumerates the sampleId's and sampleHistoryId's to be
     * fetched. It is efficient for only small numbers of samples because as
     * query size increases, MySQL's response time tends to grow quadratically.
     * 
     * @return an array of {@code SampleInfo} objects fetched from the
     *         database. The number of samples in the array is the same as the
     *         number of sample id's specified in {@code origSampleId};
     *         however, the returned samples are sorted in order of sample id.
     * @param conn a database {@code Connection} to use.
     * @param stats a {@code SampleStats} object to which performance
     *        data from this method will be logged.
     * @param origSampleId an array of sample id's describing which samples out
     *        to be fetched. This array need not be sorted.
     * @param origSampleHistoryId an array of sample history id's, corresponding
     *        to {@code origSampleId}, that describes which version of
     *        each sample is to be fetched. The special value
     *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID}
     *        indicates that the most recent version of the sample available
     *        should be fetched.
     * @param getMostCurrent if true, then the most recent version of each
     *        sample is retured regardless of the corresponding value specified
     *        in {@code origSampleHistoryId}. Then, if the fetched
     *        sample's version number differes from the one specified by the
     *        caller (and the caller specified some version other than
     *        {@code INVALID_SAMPLE_HISTORY_ID}, the flag
     *        {@code isMoreRecentThanSearch} inside the corresponding
     *        {@code SampleInfo} in the returned array is set to
     *        {@code true}. If this argument is false, the function
     *        behaves normally and the {@code isMoreRecentThanSearch}
     *        flag on {@code SampleInfo}'s in the returned array is
     *        never set.
     * @throws IllegalArgumentException if the lengths of
     *         {@code origSampleId} and {@code origSampleHistoryId}
     *         are not equal.
     * @throws InconsistentDbException if an inconsistency within the samples'
     *         metadata is detected.
     * @throws ResourceNotFoundException with a nested {@code SampleInfo}
     *         object if a sample identified by a pair of {@code sampleIds}
     *         and {@code sampleHistoryIds} element values could not be
     *         found in the database.
     * @throws SQLException on database error.
     */
    private static SampleInfo[] dbFetchMultipleSamplesWithOr(Connection conn,
            SampleStats stats, int[] origSampleId, int origSampleHistoryId[],
            boolean getMostCurrent) throws InconsistentDbException,
            ResourceNotFoundException, SQLException {
        PerfTimer perfTimer = new PerfTimer("dbFetchMultipleSamplesWithOr()");

        // Basic validation
        if (origSampleId.length != origSampleHistoryId.length) {
            // The arrays don't have equal size
            throw new IllegalArgumentException();
        }
        if (origSampleId.length == 0) {
            return new SampleInfo[0];
        }

        // Build a sorted table of sampleId's and sampleHistoryId's.
        int sampleId[] = new int[origSampleId.length];
        int sampleHistoryId[] = new int[origSampleId.length];
        System.arraycopy(origSampleId, 0, sampleId, 0, origSampleId.length);
        Arrays.sort(sampleId);
        for (int i = 0; i < origSampleId.length; i++) {
            int pos = Arrays.binarySearch(sampleId, origSampleId[i]);

            assert (pos >= 0);
            sampleHistoryId[pos] = origSampleHistoryId[i];
        }

        // Fetch rows from the 'samples' table and use them to create some
        // SampleInfo objects.
        Statement cmd = conn.createStatement();

        try {
            SampleInfo samples[] = dbFetchMultipleSamplesHelperSamples(
                    sampleId, sampleHistoryId, getMostCurrent, perfTimer, cmd,
                    "SELECT * FROM samples WHERE "
                            + generateWhereClause("id", sampleId)
                            + " ORDER BY id;");

            // Build a list of absolute sampleHistoryId's to be fetched,
            // resolving
            // any indefinite history id's to definite ones at this time. Also
            // discover simultaneously whether we should expect to fetch
            // non-current (i.e. old) versions of any sample.
            boolean fetchingOldVersionOfAnySample = false;
            int sampleHistoryIdToFetch[] = new int[origSampleId.length];
            for (int i = 0; i < sampleHistoryId.length; i++) {
                if (getMostCurrent || (sampleHistoryId[i]
                        == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)) {
                    sampleHistoryIdToFetch[i] = samples[i].mostRecentHistoryId;
                } else {
                    sampleHistoryIdToFetch[i] = sampleHistoryId[i];
                    if (sampleHistoryIdToFetch[i]
                            != samples[i].mostRecentHistoryId) {
                        fetchingOldVersionOfAnySample = true;
                    }
                }
            }

            /*
             * Fetch rows from the 'sampleHistory' table and use them to update
             * history-related fields in our SampleInfo's. Possibly skip this
             * step if there are no non-current (i.e. old) versions of samples
             * being fetched.
             */
            if (fetchingOldVersionOfAnySample) {
                dbFetchMultipleSamplesHelperSampleHistory(samples, perfTimer,
                        cmd, "SELECT id, sample_id, newStatus, date"
                                + " FROM sampleHistory WHERE "
                                + generateWhereClause("id",
                                        sampleHistoryIdToFetch)
                                + " ORDER BY sample_id;");
            }

            /*
             * Fetch rows from the other four sample metadata tables. Use the
             * rows returned to append various records to our SampleInfos.
             */
            dbFetchMultipleSamplesHelperGeneral("sampleAcls", samples,
                    perfTimer, cmd, "SELECT * FROM sampleAcls WHERE "
                            + generateWhereClause("sample_id", sampleId)
                            + " ORDER BY sample_id;");
            String bigWhereClause = getMostCurrent
                    ? (" WHERE last_sampleHistory_id IS NULL AND ("
                            + generateWhereClause("sample_id", sampleId) + ")")
                    : (" WHERE " + generateWhereClause("sample_id", sampleId,
                            "first_sampleHistory_id", "last_sampleHistory_id",
                            sampleHistoryId));
            dbFetchMultipleSamplesHelperGeneral("sampleData", samples,
                    perfTimer, cmd, "SELECT * from sampleData "
                            + bigWhereClause + " ORDER BY sample_id;");
            dbFetchMultipleSamplesHelperGeneral("sampleAttributes", samples,
                    perfTimer, cmd, "SELECT * from sampleAttributes "
                            + bigWhereClause + " ORDER BY sample_id;");
            dbFetchMultipleSamplesHelperGeneral("sampleAnnotations", samples,
                    perfTimer, cmd, "SELECT * from sampleAnnotations "
                            + bigWhereClause + " ORDER BY sample_id;");

            return samples;
        } finally {
            // Clean up.
            cmd.close();
            stats.record(perfTimer);
        }
    }

    /**
     * <p>
     * Internal function used to fetch and populate multiple SampleInfo objects
     * from the database. This method does not interface with the cache, but
     * should be called in case of a cache miss. The caller must already have
     * obtained a lock that permits full read access to sample metadata tables
     * for the samples specified by {@code origSampleId}.
     * </p><p>
     * The current implementation creates a temporary database table called
     * 'tmp' that holds the sampleId and sampleHistoryId values of samples to be
     * fetched. It is efficient only for large numbers of samples due to the
     * overhead of creating the temporary table.
     * </p>
     * 
     * @return an array of {@code SampleInfo} objects fetched from the
     *         database. The number of samples in the array is the same as the
     *         number of sample id's specified in {@code origSampleId};
     *         however, the returned samples are sorted in order of sample id.
     * @param conn a database {@code Connection} to use.
     * @param stats a {@code SampleStats} object to which performance
     *        data from this method will be logged.
     * @param origSampleId an array of sample id's describing which samples out
     *        to be fetched. This array need not be sorted.
     * @param origSampleHistoryId an array of sample history id's, corresponding
     *        to {@code origSampleId}, that describes which version of
     *        each sample is to be fetched. The special value
     *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID}
     *        indicates that the most recent version of the sample available
     *        should be fetched.
     * @param getMostCurrent if true, then the most recent version of each
     *        sample is retured regardless of the corresponding value specified
     *        in {@code origSampleHistoryId}. Then, if the fetched
     *        sample's version number differes from the one specified by the
     *        caller (and the caller specified some version other than
     *        {@code INVALID_SAMPLE_HISTORY_ID}, the flag
     *        {@code isMoreRecentThanSearch} inside the corresponding
     *        {@code SampleInfo} in the returned array is set to
     *        {@code true}. If this argument is false, the function
     *        behaves normally and the {@code isMoreRecentThanSearch}
     *        flag on {@code SampleInfo}'s in the returned array is
     *        never set.
     * @throws IllegalArgumentException if the lengths of
     *         {@code origSampleId} and {@code origSampleHistoryId}
     *         are not equal.
     * @throws InconsistentDbException if an inconsistency within the samples'
     *         metadata is detected.
     * @throws ResourceNotFoundException with a nested {@code SampleInfo}
     *         object if a sample identified by a pair of {@code sampleIds}
     *         and {@code sampleHistoryIds} element values could not be
     *         found in the database.
     * @throws SQLException on database error.
     */
    private static SampleInfo[] dbFetchMultipleSamplesWithTempTable(
            Connection conn, SampleStats stats, int[] origSampleId,
            int origSampleHistoryId[], boolean getMostCurrent)
            throws InconsistentDbException, ResourceNotFoundException,
            SQLException {
        PerfTimer perfTimer = new PerfTimer(
                "dbFetchMultipleSamplesWithTempTable");

        // Sanity check.
        if (origSampleId.length != origSampleHistoryId.length) {
            // The arrays don't have equal size
            throw new IllegalArgumentException();
        }

        // Return early if there's nothing to fetch.
        if (origSampleId.length == 0) {
            return new SampleInfo[0];
        }

        // Build a sorted table of sampleIds and sampleHistoryIds.
        int sampleId[] = new int[origSampleId.length];
        int sampleHistoryId[] = new int[origSampleId.length];
        
        System.arraycopy(origSampleId, 0, sampleId, 0, origSampleId.length);
        Arrays.sort(sampleId);
        for (int i = 0; i < origSampleId.length; i++) {
            int pos = Arrays.binarySearch(sampleId, origSampleId[i]);
            
            sampleHistoryId[pos] = origSampleHistoryId[i];
        }

        // Create a temporary table that has the sampleIds we want to fetch
        // and the most recent sampleHistoryId's available for those samples.
        Statement cmd = conn.createStatement();

        try {
            String sql = "CREATE TEMPORARY TABLE tmp"
                    + " (PRIMARY KEY(fetchSampleId))"
                    + " SELECT id AS fetchSampleId,"
                    + "   current_sampleHistory_id AS fetchSampleHistoryId"
                    + " FROM samples" + " WHERE "
                    + generateWhereClause("id", sampleId) + " ORDER BY id;";
            perfTimer.newChild("create temporary table");
            cmd.execute(sql);
            perfTimer.stopChild();

            /*
             * Possibly alter some sampleHistoryId values in the temporary
             * table, depending on exactly which sample versions the caller
             * asked us to fetch.
             */
            boolean fetchingOldVersionOfAnySample = false;
            if (!getMostCurrent) {
                Statement cmd2 = conn.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                try {
                    String sql2 = "SELECT * FROM tmp;";
                    perfTimer.newChild("fixup temporary table");
                    ResultSet rs2 = cmd2.executeQuery(sql2);
                    perfTimer.stopChild();
                    int arrayPos = 0;

                    while (rs2.next()) {
                        if (sampleId[arrayPos] != rs2.getInt("fetchSampleId")) {
                            // Expected to be on a different sample.
                            throw new InconsistentDbException();
                        }
                        if ((sampleHistoryId[arrayPos]
                                != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                                && (rs2.getInt("fetchSampleHistoryId")
                                        != sampleHistoryId[arrayPos])) {
                            /*
                             * Caller specifically asked for an older version of
                             * this sample; not the most current. Update the
                             * temporary table to indicate the requested
                             * version.
                             */
                            rs2.updateInt("fetchSampleHistoryId",
                                    sampleHistoryId[arrayPos]);
                            rs2.updateRow();
                            fetchingOldVersionOfAnySample = true;
                        }
                        arrayPos++;
                    }
                    rs2.close();
                } finally {
                    cmd2.close();
                }
                
                /*
                 * The temporary table now describes precisely the
                 * sample/versions we intend to fetch rows and create SampleInfo
                 * objects for.
                 */
            }

            // Fetch rows from the 'samples' table and use them to construct
            // SampleInfo objects.
            SampleInfo samples[] = dbFetchMultipleSamplesHelperSamples(
                    sampleId,
                    sampleHistoryId,
                    getMostCurrent,
                    perfTimer,
                    cmd,
                    "SELECT samples.* FROM tmp"
                            + " INNER JOIN samples ON tmp.fetchSampleId=samples.id;");

            // Fetch rows from the 'sampleHistory' table and use them to
            // overwrite
            // values on our SampleInfo objects. We can skip this step if no
            // old version of any sample is being fetched.
            if (fetchingOldVersionOfAnySample) {
                dbFetchMultipleSamplesHelperSampleHistory(
                        samples,
                        perfTimer,
                        cmd,
                        "SELECT id, sample_id, newStatus"
                                + " FROM tmp INNER JOIN sampleHistory"
                                + "   ON tmp.fetchSampleHistoryId=sampleHistory.id"
                                + " ORDER BY sample_id;");
            }

            // Fetch rows from the other four sample metadata tables. Use the
            // rows
            // returned to append various records to our SampleInfo's.
            dbFetchMultipleSamplesHelperGeneral("sampleAcls", samples,
                    perfTimer, cmd, "SELECT sampleAcls.*"
                            + " FROM tmp INNER JOIN sampleAcls"
                            + "   ON tmp.fetchSampleId=sampleAcls.sample_id"
                            + " ORDER BY sample_id;");
            dbFetchMultipleSamplesHelperGeneral(
                    "sampleData",
                    samples,
                    perfTimer,
                    cmd,
                    "SELECT sampleData.*"
                            + " FROM tmp"
                            + " INNER JOIN sampleData"
                            + "   ON fetchSampleId=sample_id"
                            + "       AND fetchSampleHistoryId >= first_sampleHistory_id"
                            + "       AND (fetchSampleHistoryId < last_sampleHistory_id"
                            + "            OR last_sampleHistory_id IS NULL)"
                            + " ORDER BY sample_id;");
            dbFetchMultipleSamplesHelperGeneral(
                    "sampleAttributes",
                    samples,
                    perfTimer,
                    cmd,
                    "SELECT sampleAttributes.*"
                            + " FROM tmp"
                            + " INNER JOIN sampleAttributes"
                            + "   ON fetchSampleId=sample_id"
                            + "       AND fetchSampleHistoryId >= first_sampleHistory_id"
                            + "       AND (fetchSampleHistoryId < last_sampleHistory_id"
                            + "            OR last_sampleHistory_id IS NULL)"
                            + " ORDER BY sample_id;");
            dbFetchMultipleSamplesHelperGeneral(
                    "sampleAnnotations",
                    samples,
                    perfTimer,
                    cmd,
                    "SELECT sampleAnnotations.*"
                            + " FROM tmp"
                            + " INNER JOIN sampleAnnotations"
                            + "   ON fetchSampleId=sample_id"
                            + "       AND fetchSampleHistoryId >= first_sampleHistory_id"
                            + "       AND (fetchSampleHistoryId < last_sampleHistory_id"
                            + "            OR last_sampleHistory_id IS NULL)"
                            + " ORDER BY sample_id;");

            // Delete the temporary table
            perfTimer.newChild("drop table");
            cmd.execute("DROP TABLE tmp;");
            perfTimer.stopChild();

            return samples;
        } finally {
            // Clean up.
            cmd.close();
            stats.record(perfTimer);
        }
    }

    /**
     * Helper function useful when doing a bulk-fetch of samples. Executes a
     * caller-specified database query on the 'samples' table, transforms the
     * matching rows into {@code SampleInfo} objects, and returns them.
     * The caller specifies the sample id's that he expects the query to fetch;
     * an exception is thrown if any of them could not be. The caller also
     * specifies the sample history id's of the samples he intends to fetch
     * subsequently; this knowledge can optionally be used to set the
     * {@code isMoreRecentThanSearch} flag on returned
     * {@code SampleInfo}'s.
     * 
     * @return an array of {@code SampleInfo} objects representing data
     *         fetched from the database. Note that these
     *         {@code SampleInfo}'s are "incomplete" in the sense that
     *         only the 'samples' table has been fetched, but several other
     *         database tables contain sample-related metadata also. Callers are
     *         expected to invoke other "helper" functions that will modify the
     *         {@code SampleInfo}'s returned by this method in-place in
     *         order to achieve a "complete" SampleInfo. The
     *         {@code isMoreRecentThanSearch} field on returned
     *         {@code SampleInfo's} may be set to true for some samples
     *         if {@code setIsMoreRecentThanSearch} is true. The
     *         {@code historyId} and {@code status} fields on
     *         returned {@code SampleInfo's} are set also, but their
     *         values are "correct" only if the caller intended to fetch the
     *         most recent version of that particular sample. If the caller
     *         intended to fetch a non-current (i.e. old) version of the sample,
     *         he should overwrite these two fields with values read from the
     *         'sampleHistory' table.
     * @param sampleIds an array containing the id's of samples expected to be
     *        returned by the database query, in ascending numeric order.
     * @param sampleHistoryIds ignored unless
     *        {@code setIsMoreRecentThanSearch} is true, the elements of
     *        this array correspond with {@code sampleIds} and identify
     *        the expected most recent version of each sample. The special value
     *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID}
     *        indicates that no version in particular is expected and that the
     *        caller plans simply to fetch the most recent version of the sample
     *        available.
     * @param setIsMoreRecentThanSearch if true, the
     *        {@code isMoreRecentThanSearch} flag on
     *        {@code SampleInfo}'s in the returned array may be set if
     *        the expected sample history id for a fetched sample (as specified
     *        by the corresponding element of {@code sampleHistoryIds})
     *        differs from the most recent history id for that sample.
     * @param perfTimer a {@code PerfTimer} object used by the caller to
     *        track the entire bulk-fetch operation. On this timer object a
     *        child timer object will be created and then stopped, marking the
     *        period this method was blocked waiting for the database engine to
     *        execute its query.
     * @param cmd a {@code Statement} object already bound to an
     *        appropriate database connection. This object may be persisted
     *        across multiple calls to this method.
     * @param sql a SQL query in string form that this method will execute. The
     *        query must return all the columns of the 'samples' table. The rows
     *        returned by the query must be in order of ascending sample id. The
     *        rows returned by the query must be filtered in such a way as to
     *        exclude information about any sample that is not represented in
     *        {@code samples}.
     * @throws ResourceNotFoundException with a nested {@code SampleInfo}
     *         object if a sample identified by a pair of {@code sampleIds}
     *         and {@code sampleHistoryIds} element values could not be
     *         found in the database.
     * @throws SQLException if an error occurred while executing the database
     *         query.
     */
    private static SampleInfo[] dbFetchMultipleSamplesHelperSamples(
            int sampleIds[], int sampleHistoryIds[],
            boolean setIsMoreRecentThanSearch, PerfTimer perfTimer,
            Statement cmd, String sql) throws ResourceNotFoundException,
            SQLException {
        // Execute the caller-specified db query.
        perfTimer.newChild("samples query");
        ResultSet rs = cmd.executeQuery(sql);
        perfTimer.stopChild();

        // Iterate throw the resulting rows, creating SampleInfo objects as we
        // go.
        int arrayPos = 0;
        SampleInfo samples[] = new SampleInfo[sampleIds.length];
        
        while (rs.next()) {
            SampleInfo sample = new SampleInfo(rs);
            if (sample.id != sampleIds[arrayPos]) {
                // Expected to be on a different sample. This probably means
                // the sample we wanted isn't in the database.
                throw new ResourceNotFoundException(new SampleInfo(
                        sampleIds[arrayPos], sampleHistoryIds[arrayPos]));
            }
            if (setIsMoreRecentThanSearch
                    && (sampleHistoryIds[arrayPos] != sample.mostRecentHistoryId)
                    && (sampleHistoryIds[arrayPos]
                            != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)) {
                // The criteria are met: set a flag on this SampleInfo as a
                // convenience to the caller.
                sample.isMoreRecentThanSearch = true;
            }

            // Set the sample's historyId and status values assuming that we're
            // meaning to fetch the most recent version of the sample. These
            // values might be overwritten by the caller later if that's not
            // the case.
            sample.historyId = sample.mostRecentHistoryId;
            sample.status = sample.mostRecentStatus;

            samples[arrayPos] = sample;
            arrayPos++;
        }

        // Clean up and return.
        rs.close();
        
        return samples;
    }

    /**
     * Helper function useful when doing a bulk-fetch of samples. Executes a
     * caller-specified database query of the 'sampleHistory' table and uses the
     * resulting table to alter the {@code status} and
     * {@code historyId} fields in the caller-supplied
     * {@code SampleInfo} objects appropriately. Calling this method is
     * generally only necessary if non-current (i.e. old) versions of samples
     * are being fetched, because the {@code SampleInfo} objects returned
     * by {@code dbFetchMultipleSamplesHelperSamples()} would already
     * contain correct values otherwise.
     * 
     * @param samples an array of incomplete {@code SampleInfo} objects.
     *        As rows are read from the database, the {@code status} and
     *        {@code historyId} fields of these {@code SampleInfo}'s
     *        are overwritten. Samples within the array must be ordered by their
     *        {@code id} field.
     * @param perfTimer a {@code PerfTimer} object used by the caller to
     *        track the entire bulk-fetch operation. On this timer object a
     *        child timer object will be created and then stopped, marking the
     *        period this method was blocked waiting for the database engine to
     *        execute its query.
     * @param cmd a {@code Statement} object already bound to an
     *        appropriate database connection. This object may be persisted
     *        across multiple calls to this method.
     * @param sql a SQL query in string form that this method will execute. The
     *        query must return all the columns of the 'sampleHistory' table.
     *        The rows returned by the query must be in order of ascending
     *        sample id. The rows returned by the query must be filtered in such
     *        a way as to exclude information about any sample that is not
     *        represented in {@code samples}.
     * @throws InconsistentDbException if an inconsistency within the samples'
     *         metadata is detected.
     * @throws SQLException if an error occurred while executing the database
     *         query.
     */
    private static void dbFetchMultipleSamplesHelperSampleHistory(
            SampleInfo samples[], PerfTimer perfTimer, Statement cmd,
            String sql) throws InconsistentDbException, SQLException {
        // Execute the caller-specific db query.
        perfTimer.newChild("sampleHistory query");
        ResultSet rs = cmd.executeQuery(sql);
        perfTimer.stopChild();

        for (int arrayPos = 0; rs.next(); arrayPos++) {
            if (samples[arrayPos].id != rs.getInt("sample_id")) {
                // Expected one sample, but fetched another.
                throw new InconsistentDbException();
            }

            // Overwrite any status or historyId that may have been set
            // previously; these correct values refer to the requested (old)
            // version of the sample.
            samples[arrayPos].status = rs.getInt("newStatus");
            samples[arrayPos].historyId = rs.getInt("id");
        }
        rs.close();
    }

    /**
     * Helper function useful when doing a bulk-fetch of samples. Executes a
     * caller-specified database query of a single table, transforms the
     * matching rows into container objects of an appropriate type, and attaches
     * those container objects to the SampleInfo's provided by the caller.
     * 
     * @param tableName the name of the database table to be read. Valid values
     *        are {@code sampleAcls}, {@code sampleData},
     *        {@code sampleAnnotations}, and
     *        {@code sampleAttributes}.
     * @param samples an array of incomplete {@code SampleInfo} objects.
     *        As rows are read from the database, appropriate records will be
     *        added to the {@code SampleInfo}'s. Samples within the
     *        array must be ordered by their {@code id} field.
     * @param perfTimer a {@code PerfTimer} object used by the caller to
     *        track the entire bulk-fetch operation. On this timer object a
     *        child timer object will be created and then stopped, marking the
     *        period this method was blocked waiting for the database engine to
     *        execute its query.
     * @param cmd a {@code Statement} object already bound to an
     *        appropriate database connection. This object may be persisted
     *        across multiple calls to this method.
     * @param sql a SQL query in string form that this method will execute. The
     *        query must return all the columns of the table named by
     *        {@code tableName}. The rows returned by the query must be
     *        in order of ascending sample id. The rows returned by the query
     *        must be filtered in such a way as to exclude information about any
     *        sample that is not represented in {@code samples}.
     * @throws IllegalArgumentException if {@code tableName} is not
     *         recognized.
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws SQLException if an error occurred while executing the database
     *         query.
     */
    private static void dbFetchMultipleSamplesHelperGeneral(String tableName,
            SampleInfo samples[], PerfTimer perfTimer, Statement cmd, String sql)
            throws InconsistentDbException, SQLException {
        // Execute the caller-provided SQL query.
        perfTimer.newChild(tableName + " query");
        ResultSet rs = cmd.executeQuery(sql);
        perfTimer.stopChild();

        // Iterate through the results, matching each SampleInfo to zero or
        // more database rows.
        int arrayPos = 0;
        while (rs.next()) {
            int thisRowSampleId = rs.getInt("sample_id");

            if (tableName.equals("sampleData")) {
                // Every SampleInfo should match exactly one row.
                if (thisRowSampleId != samples[arrayPos].id) {
                    throw new InconsistentDbException();
                }
                samples[arrayPos].dataInfo = new SampleDataInfo(rs);
                arrayPos++;
            } else if (tableName.equals("sampleAcls")) {
                // Every SampleInfo can match zero or more rows.
                while (thisRowSampleId != samples[arrayPos].id) {
                    arrayPos++;
                }
                samples[arrayPos].accessInfo.add(new SampleAccessInfo(rs));
            } else if (tableName.equals("sampleAttributes")) {
                // Every SampleInfo can match zero or more rows.
                while (thisRowSampleId != samples[arrayPos].id) {
                    arrayPos++;
                }
                samples[arrayPos].attributeInfo.add(
                        new SampleAttributeInfo(rs));
            } else if (tableName.equals("sampleAnnotations")) {
                // Every SampleInfo can match zero or more rows.
                while (thisRowSampleId != samples[arrayPos].id) {
                    arrayPos++;
                }
                samples[arrayPos].annotationInfo.add(
                        new SampleAnnotationInfo(rs));
            } else {
                // Unrecognized table name.
                throw new IllegalArgumentException();
            }
        }
        rs.close();
    }

    /**
     * Returns a SQL "WHERE" clause fragment of the form: fieldName IN
     * (intValues[0], intValues[1], ...) for each {@code intValue}
     * specified. This syntax is equivalent to: fieldName=intValues[0] OR
     * fieldName=intValues[1] ... but more compact. The WHERE clause fragment
     * returned by this method should be encapsulated in parentheses if the
     * caller plans to combine it with other SQL fragments to form a larger
     * WHERE clause.
     */
    private static String generateWhereClause(String fieldName, int intValues[]) {
        StringBuilder sql = new StringBuilder();
        boolean firstElement = true;
        
        sql.append(fieldName);
        sql.append(" ");
        sql.append("IN (");
        for (int i = 0; i < intValues.length; i++) {
            if (!firstElement) {
                sql.append(", ");
            }
            firstElement = false;
            sql.append(intValues[i]);
        }
        sql.append(")");
        
        return sql.toString();
    }

    /**
     * Like the first version of generateWhereClause() but more complicated. The
     * two arrays mst have equal size; each pair of array entries results in one
     * SQL fragment being generated. If intValues2[0] has the special value -1
     * then the fragment looks like: (fieldName1=intValues1[0] AND fieldName3 IS
     * NULL) . If intValues2[0] is not -1 then the fragment looks like:
     * (fieldName1=intValues1[0] AND fieldName2 <= intValues2[0] AND (fieldName3 >
     * intValues2[0] OR fieldName3 IS NULL)) . The fragments are linked by OR's.
     */
    private static String generateWhereClause(String fieldName1,
            int intValues1[], String fieldName2, String fieldName3,
            int intValues2[]) {
        StringBuilder sql = new StringBuilder();
        boolean firstElement = true;
        
        for (int i = 0; i < intValues1.length; i++) {
            if (!firstElement) {
                sql.append(" OR ");
            }
            firstElement = false;

            if (intValues2[i] == -1) {
                sql.append("(");
                sql.append(fieldName1);
                sql.append("=");
                sql.append(intValues1[i]);
                sql.append(" AND ");
                sql.append(fieldName3);
                sql.append(" IS NULL)");
            } else {
                sql.append("(");
                sql.append(fieldName1);
                sql.append("=");
                sql.append(intValues1[i]);
                sql.append(" AND ");
                sql.append(fieldName2);
                sql.append(" <= ");
                sql.append(intValues2[i]);
                sql.append(" AND (");
                sql.append(fieldName3);
                sql.append(" > ");
                sql.append(intValues2[i]);
                sql.append(" OR ");
                sql.append(fieldName3);
                sql.append(" IS NULL))");
            }
        }
        
        return sql.toString();
    }
}
