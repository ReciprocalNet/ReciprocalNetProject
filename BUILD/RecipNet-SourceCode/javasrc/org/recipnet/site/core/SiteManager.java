/*
 * Reciprocal Net project
 * 
 * SiteManager.java
 *
 * 06-Jun-2002: ekoperda wrote first draft
 * 18-Jun-2002: hclin wSrote the skeleton for formal methods
 * 19-Jun-2002: ekoperda added db access code
 * 28-Jun-2002: ekoperda added passCoreMessage() and getLocalLabs() methods
 * 12-Jul-2002: ekoperda added configuration properties support, bootstrap
 *              mode support, and site grant file support
 * 26-Jul-2002: ekoperda fixed bugs #201 and #202
 * 08-Aug-2002: ekoperda added getLocalLabs2(), a remote method
 * 23-Aug-2002: ekoperda added getLocalTrackingConfig() and support for
 *              the localtracking feature
 * 26-Aug-2002: ekoperda added lookupLabId() and isLabDirectoryValid()
 *              functions in order to support Repository Manager's scanning
 *              feature.
 * 27-Aug-2002: ekoperda added schedulePeriodicTask() function and general
 *              periodic task support.
 * 03-Sep-2002: ekoperda added recordLogEvent() function and general logging
 *              support.
 * 12-Sep-2002: ekoperda recoded getAllSiteInfo(), getAllLabInfo(),
 *              getAllProviderInfo(), getAllUserInfo(), getUsersForLab(), and
 *              getUsersForProvider() to read from the cache whenever possible
 *              for improved performance.
 * 26-Sep-2002: ekoperda fixed bug #481 in getUsersForProvider()
 * 27-Sep-2002: ekoperda changed import statements to reflect class
 *              rearrangement and reconfigured ISM handling to support the new
 *              XML serialization feature
 * 30-Sep-2002: ekoperda fixed bugs #503 in passCoreMessage() and
 *              EventProviderUpdate()
 * 08-Oct-2002: ekoperda added support for ISM passing between sites throghout
 *              module, and fixed bug #512 in writeUpdatedProviderInfo
 * 18-Oct-2002: ekoperda added getLocalSiteInfo() function
 * 23-Oct-2002: ekoperda modified processInterSiteMessage() and added
 *              eventSiteRest() in order to handle SiteResetISM's
 * 28-Oct-2002: ekoperda modified the constructor to pass newly-expected
 *              configuration parameters to IsmPuller and IsmPusher
 * 29-Oct-2002: ekoperda fixed bug #568 by modifying acceptIsmBundle() to drop
 *              messages that originated at the local site and modifying
 *              doReplayRequestTask() to not attempt to pull messages that
 *              originated at the local site
 * 30-Oct-2002: ekoperda moved all received-ISM tracking code to
 *              core.util.ReceivedMessageAgentadded and added new methods
 *              passCoreMessages() and writeUpdatedSiteInfo()
 * 04-Nov-2002: ekoperda updated code in performBootstrapTasks() to invoke
 *              the new ReceivedMessageAgent class
 * 04-Nov-2002: ekoperda added synchronizeWithSiteNetwork() and
 *              generateInitialIsms()
 * 05-Nov-2002: eisiorho fixed bug #440 in dbPreloadUsers()
 * 08-Nov-2002: ekoperda fixed bug #593 in generateInitialIsms()
 * 12-Nov-2002: ekoperda fixed bug #595 in passIsmBundle() and pullIsmBundle()
 * 15-Nov-2002: eisiorho fixed getSiteInfo(), getProviderInfo(), getUserInfo()
 *              & getLabInfo() to return clones of containers.
 * 10-Dec-2002: eisiorho added fixCorruptProviders(), which is called once
 *              during a version update.
 * 20-Dec-2002: ekoperda fixed bug #671 in processCoreMessage() and bug #672 in
 *              eventSiteUpdate()
 * 04-Feb-2003: ekoperda and eisiorho fixed bug #695 in writeUpdatedUserInfo()
 * 21-Feb-2002: ekoperda added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 18-Mar-2003: midurbin refactored some ISM-code into new TopologyAgent class
 * 20-Mar-2003: ekoperda fixed bug 777 in dbFetchLabByDirectoryName() and
 *              dbFetchUser()
 * 14-Apr-2003: ekoperda modified recordLogEvent() as part of task 872
 * 15-Apr-2003: eisiorho fixed bug #857 in dbPreloadUsers()
 * 15-Apr-2003: midurbin moved logging code to LogRecordGenerator and added
 *              recordLogRecord()
 * 10-Jun-2003: ekoperda moved ISM-pushing code to new class IsmPushAgent
 * 20-Jun-2003: midurbin fixed bug #939 in constructor
 * 10-Jul-2003: midurbin added statistics collection and reporting support
 *              through use of the new statisticsAgent member variable
 * 18-Jul-2003: ekoperda modified start() to utilize new config directive
 *              DbUrlForBootstrap
 * 07-Jan-2004: ekoperda changed package references due to source tree
 *              reorganization
 * 08-Apr-2004: midurbin added code to check the validity of generated ISMs and
 *              and to halt ism generation for this and other ism-generation
 *              exceptions.
 * 07-May-2004: cwestnea modified RMI connection string in start()
 * 03-Jun-2004: cwestnea modified writeUpdatedLabInfo(),
 *              writeUpdatedProviderInfo(), writeUpdatedUserInfo(), and
 *              writeUpdatedSiteInfo() to do data validation
 * 07-Jun-2004: midurbin changed package references due to source tree
 *              reorganization
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 11-Jan-2005: jobollin moved Validator to org.recipnet.common
 * 12-Jan-2005: ekoperda fixed bug #1497 in processCoreMessage(),
 *              passCoreMessage(), performBootstrapTasks(), and eventSendIsm()
 * 13-Jan-2005: ekoperda fixed bug #1496 in writeUpdateSiteInfo(),
 *              writeUpdatedLabInfo(), and writeUpdatedProviderInfo()
 * 10-Jun-2005: midurbin added additional validation check to
 *              writeUpdatedUserInfo()
 * 06-Oct-2005: midurbin removed getProviderForSample()
 * 18-Jan-2006: ekoperda shifted more ISM-related code to ReceivedMessageAgent,
 *              accommodated new class IsmExchanger, and neutered 
 *              synchronizeWithSiteNetwork()         
 * 20-Feb-2006: ekoperda rewrote synchronizeWithSiteNetwork() to utilize the
 *              new SiteNetworkSynchronizer, also refactored and exposed
 *              pullIsmsFromRemoteSite() for core-internal use
 * 20-Mar-2006: jobollin removed unused imports
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 * 11-Apr-2006: jobollin switched to initializing object caches via
 *              ObjectCache.newInstance(); inserted type parameters; removed
 *              unnecessary RemoteException declarations
 * 21-Apr-2006: jobollin updated this class to accommodate changes to UserInfo
 *              and CoreMessageQueue, reformatted the source
 * 11-May-2006: jobollin added multiple finally{} blocks to ensure that all
 *              JDBC Statements are closed by the method that opens them 
 * 30-May-2006: jobollin removed catch blocks rendered unreachable by
 *              updates to LabInfo.clone()
 * 31-May-2006: jobollin performed minor updates for consistency with other
 *              changes in various places 
 * 15-Jun-2006: jobollin made start() initialize the lab IDs of the
 *              LocalTrackingConfig objects it creates 
 * 06-Jan-2008: ekoperda modified eventSiteUpdate() to notify TopologyAgent
 * 04-Jul-2008: ekoperda updated synchronizeWithSiteNetwork() to match the new 
 *              arguments required by SiteNetworkSynchronizer
 * 28-Nov-2008: ekoperda added support for ForceUpgradeISM and corrected
 *              handling of SiteActivationISM and SiteDeactivationISM.
 * 31-Dec-2008: ekoperda relaxed ISM validation in eventProviderActivation()
 *              and improved handling of re-activated site records
 * 01-Jan-2009: ekoperda added markSiteGrantIsmAsProcessed() as part of an
 *              online db migration task
 * 02-Jan-2009: ekoperda added connections to a LabTransferAgent and handling
 *              for its associated ISM's
 * 02-Jan-2009: ekoperda clarified ISM validation in eventLabUpdate()
 */

package org.recipnet.site.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.recipnet.common.ObjectCache;
import org.recipnet.common.Validator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.agent.IsmPushAgent;
import org.recipnet.site.core.agent.LabTransferAgent;
import org.recipnet.site.core.agent.MessageFileAgent;
import org.recipnet.site.core.agent.ReceivedMessageAgent;
import org.recipnet.site.core.agent.StatisticsAgent;
import org.recipnet.site.core.agent.TopologyAgent;
import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.ForceUpgradeISM;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.InvalidateHoldingsCM;
import org.recipnet.site.core.msg.JoinISM;
import org.recipnet.site.core.msg.LabActivationISM;
import org.recipnet.site.core.msg.LabTransferInitiateISM;
import org.recipnet.site.core.msg.LabTransferCompleteISM;
import org.recipnet.site.core.msg.LabUpdateISM;
import org.recipnet.site.core.msg.LocalLabsChangedCM;
import org.recipnet.site.core.msg.PingCM;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.ProviderActivationISM;
import org.recipnet.site.core.msg.ProviderUpdateISM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.msg.SiteActivationISM;
import org.recipnet.site.core.msg.SiteDeactivationISM;
import org.recipnet.site.core.msg.SiteGrantISM;
import org.recipnet.site.core.msg.SiteResetISM;
import org.recipnet.site.core.msg.SiteStatisticsRequestISM;
import org.recipnet.site.core.msg.SiteUpdateISM;
import org.recipnet.site.core.util.CoreMessageQueue;
import org.recipnet.site.core.util.CoreScheduledTask;
import org.recipnet.site.core.util.EventSignal;
import org.recipnet.site.core.util.IsmExchanger;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.core.util.MsgpakUtil;
import org.recipnet.site.core.util.MutexLock;
import org.recipnet.site.core.util.SerialNumber;
import org.recipnet.site.core.util.SiteNetworkSynchronizer;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.LogEvent;
import org.recipnet.site.shared.validation.ContainerStringValidator;

/**
 * Implementation for the Site Manager core object. There should be only one of
 * these objects ever instantiated at a single time. We'll create our own worker
 * thread so that this object is "always running".
 */
public class SiteManager extends UnicastRemoteObject implements
        SiteManagerRemote, Runnable {
    
    // References to the other core objects
    
    SampleManager sampleManager;

    RepositoryManager repositoryManager;

    // Worker thread variables
    
    private final AtomicReference<Thread> workerThread;

    private final EventSignal startupSignal;

    final EventSignal shutdownSignal;

    private final CoreMessageQueue messageQueue;

    // "Ping" objects -- used to see if the worker is running
    
    private final MutexLock pingMutex;

    private final EventSignal pingSignal;

    // The database connection. Synchronize on this.
    private Connection conn;

    // Caches; used to reduce the number of database lookups
    
    private final ObjectCache<SiteInfo> siteCache;

    private final ObjectCache<LabInfo> labCache;

    private final ObjectCache<ProviderInfo> providerCache;

    private final ObjectCache<UserInfo> userCache;

    // a guaranteed-unique counter that is used during some database operations
    private final SerialNumber serialNumber;

    // random number generator used for assigning id's to new db records.
    // Synchronize on this.
    private final Random randomIdGenerator;

    // the properties object from which we can obtain configuration parameters
    private final Properties properties;

    // The local site's id. We share this freely with the other core modules.
    public int localSiteId;

    private PrivateKey localPrivateKey;

    // tells us whether we were started in "bootstrap mode" or not.
    private final boolean bootstrapMode;

    // tells us whether an error has cause ISM generation to be halted
    private boolean ismGenerationHalted;

    // keeps track of our msgs-sent, msgs-recv, and msgs-held directories.
    // synchronize on this.
    private MessageFileAgent messageFileAgent;

    // maps Integers representing lab IDs to LocalTrackingConfig objects.
    // Synchronize on this.
    private final Map<Integer, LocalTrackingConfig> localTrackingConfigMap;

    // Set of CoreScheduledTask objects representing currently executing tasks
    private final Set<CoreScheduledTask> currentTasks;

    // Manages the periodic tasks
    private final Timer scheduler;

    // Used to log things to syslog
    final Logger logger;

    // Used to sign outgoing ISM's and verify signatures on incoming ones;
    // synchronize on this
    private Signature sigEngine;

    // Used to pull ISM's from other sites. Synchronize on this.
    private final IsmExchanger ismExchanger;

    // Used to make decisions about whether and how to Push/Pull ISM's.
    private TopologyAgent topologyAgent;

    // Keeps track of all received/pending messages
    private ReceivedMessageAgent receivedMessageAgent;

    // Used to push ISM's to other sites. Null if pushing has been disabled.
    private IsmPushAgent ismPushAgent;

    // Maintains in-memory counters and db backup of site/lab statistics
    private StatisticsAgent statisticsAgent;

    // Keeps track of pending and complete lab transfer operations.
    private LabTransferAgent labTransferAgent;

    // validates data strings to make sure they are valid
    
    private final Validator labStringFieldValidator;

    private final Validator userStringFieldValidator;

    private final Validator siteStringFieldValidator;

    private final Validator providerStringFieldValidator;



    /**************************************************************************
     * CONSTRUCTOR AND BASIC METHODS (not remotely accessible)
     *************************************************************************/



    /** The one and only constructor */
    public SiteManager(Properties p, boolean bootstrapMode)
            throws RemoteException {
        // Populate our simple member variables
        workerThread = new AtomicReference<Thread>(null);
        startupSignal = new EventSignal(false, false);
        shutdownSignal = new EventSignal(false, false);
        messageQueue = new CoreMessageQueue();
        pingMutex = new MutexLock();
        pingSignal = new EventSignal();
        serialNumber = new SerialNumber();
        randomIdGenerator = new SecureRandom();
        properties = p;
        localSiteId = SiteInfo.INVALID_SITE_ID;
        this.bootstrapMode = bootstrapMode;
        this.ismGenerationHalted = false;
        localTrackingConfigMap = new HashMap<Integer, LocalTrackingConfig>();
        currentTasks = new HashSet<CoreScheduledTask>();
        scheduler = new Timer();
        sigEngine = null; // gets instantiated during start()
        ismExchanger = new IsmExchanger(Integer.parseInt(
                properties.getProperty("SitHttpConnectTimeout")),
                Integer.parseInt(properties.getProperty("SitHttpReadTimeout")));
        ismPushAgent = null; // gets instantiated during start()
        topologyAgent = null; // gets instantiated during start()
        statisticsAgent = null; // gets instantiated during start()
	labTransferAgent = null; // gets instantiated during start()
        labStringFieldValidator = new ContainerStringValidator();
        userStringFieldValidator = new ContainerStringValidator();
        siteStringFieldValidator = new ContainerStringValidator();
        providerStringFieldValidator = new ContainerStringValidator();

        // Initialize the Java logging subsystem
        logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.parse(properties.getProperty("LogLevel")));

        // create our object caches according to configuration properties
        siteCache = ObjectCache.newInstance(
                properties.getProperty("SitSiteCache"));
        labCache = ObjectCache.newInstance(
                properties.getProperty("SitLabCache"));
        providerCache = ObjectCache.newInstance(
                properties.getProperty("SitProviderCache"));
        userCache = ObjectCache.newInstance(
                properties.getProperty("SitUserCache"));
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
         * First task: read our "site grant" file. This is a binary
         * configuration file supplied to us by the Reciprocal Net coordinator.
         * It contains, among other things, our site id and private encryption
         * key.
         */
        File siteGrantFile = new File(properties.getProperty("SitGrantFile"));
        
        if (!siteGrantFile.canRead() || !siteGrantFile.isFile()) {
            recordLogRecord(
                    LogRecordGenerator.siteGrantFileUnreadable(siteGrantFile));
            stop();
            return false;
        }
        try {
            SiteGrantISM siteGrant
                    = MsgpakUtil.readAndDecodeSiteGrant(siteGrantFile);
            
            localSiteId = siteGrant.destSiteId;
            localPrivateKey = siteGrant.privateKey;
        } catch (Exception ex) {
            recordLogRecord(LogRecordGenerator.siteGrantFileException(
                    siteGrantFile, ex));
            stop();
            return false;
        }

        /*
         * Next task: establish a connection to the database. The driver class
         * name, database URI, username, and password are all user-
         * configurable.
         */
        try {
            Class.forName(properties.getProperty("DbDriverClassName"));
        } catch (Exception ex) {
            recordLogRecord(LogRecordGenerator.dbDriverFailedToLoad(
                    this, properties.getProperty("DbDriverClassName"), ex));
            stop();
            return false;
        }
        String dbUrl = this.bootstrapMode
                ? properties.getProperty("DbUrlForBootstrap")
                : properties.getProperty("DbUrl");
        try {
            conn = DriverManager.getConnection(dbUrl,
                    properties.getProperty("SitDbUsername"),
                    properties.getProperty("SitDbPassword"));
        } catch (SQLException ex) {
            recordLogRecord(LogRecordGenerator.dbFailedToConnect(this, dbUrl,
                    properties.getProperty("SitDbUsername"), ex));
            stop();
            return false;
        }

        /*
         * Next task: preload all the containers that we manage.
         */
        try {
            dbPreloadSites();
            dbPreloadLabs();
            dbPreloadProviders();
            dbPreloadUsers();
        } catch (OperationFailedException ex) {
            recordLogRecord(
                    LogRecordGenerator.dbPreloadContainersException(ex));
            stop();
            return false;
        }

        /*
         * Next Task: create new TopologyAgent containing information about
         * current active sites and local site Id.
         */
        try {
            topologyAgent = new TopologyAgent(getAllActiveSiteInfo(), 
                    localSiteId, Long.parseLong(properties.getProperty(
	 	    "SitShunnedDuration")), this);
        } catch (OperationFailedException ex) {
            recordLogRecord(LogRecordGenerator.topologyAgentFailedToCreate(ex));
            stop();
            return false;
        }

        /*
         * Next Task: create new StatisticsAgent
         */
        try {
            statisticsAgent
                    = new StatisticsAgent(conn, this, repositoryManager);
        } catch (OperationFailedException ex) {
            recordLogRecord(
                    LogRecordGenerator.statisticsAgentFailedToCreate(ex));
            stop();
            return false;
        }

        /*
         * Next Task: create new LabTransferAgent
         */
        try {
            this.labTransferAgent = new LabTransferAgent(this.conn, this, 
                    this.sampleManager, this.repositoryManager);
        } catch (Exception ex) {
            recordLogRecord(
                    LogRecordGenerator.labTransferAgentFailedToCreate(ex));
            stop();
            return false;
        }

        /*
         * Next task: create and initialize our engine for generating and
         * verifying digital signatures.
         */
        // FIXME: make the algorithm configurable in recipnetd.conf
        String algorithm = "SHA1withDSA";
        try {
            sigEngine = Signature.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            recordLogRecord(LogRecordGenerator.algorithmInvalid(algorithm, ex));
            stop();
            return false;
        }

        /* Next task: check to see whether ISM generation has been halted */
        this.ismGenerationHalted
                = new File(properties.getProperty("IsmSuicideNote")).exists();

        /*
         * Next task: create MessageFileDirectory objects for our msgs-sent/ and
         * msgs-recv/ directory. Scan the msgs-sent/ directory and keep track of
         * sequence numbers.
         */
        try {
            messageFileAgent = new MessageFileAgent(this.localSiteId,
                    this.topologyAgent, this.conn,
                    new File(properties.getProperty("SitMsgsSentDir")),
                    new File(properties.getProperty("SitMsgsRecvDir")),
                    new File(properties.getProperty("SitMsgsHeldDir")));
        } catch (Exception ex) {
            recordLogRecord(
                    LogRecordGenerator.messageFileDirectoriesFailedToInit(ex));
            stop();
            return false;
        }

        /*
         * Next task: read configuration info for the localtracking feature
	 * from recipnetd.conf.
         */
	try {
	    this.loadLocalTrackingConfig();
	} catch (OperationFailedException ex) {
            recordLogRecord(LogRecordGenerator.getLocalLabsException(this,
                    ex));
            stop();
            return false;
        }

        /*
         * Next task: initialize the subsystem that manages received ISM's
         */
        receivedMessageAgent = new ReceivedMessageAgent(
                this,
                this.sampleManager,
                this.repositoryManager,
                this.messageFileAgent,
                this.topologyAgent,
                Boolean.parseBoolean(
                        properties.getProperty("SitMsgsAlwaysDump")),
                Long.parseLong(
                        properties.getProperty("SitMsgsHoldTime")),
                Long.parseLong(
                        properties.getProperty("SitIsmProcessingTimeout")),
                Integer.parseInt(
                        properties.getProperty("SitReceivedIsmReplayLimit")),
                this.bootstrapMode);
        try {
            receivedMessageAgent.initializeSites();
            if (!bootstrapMode) {
                receivedMessageAgent.readHeldMessages();
            }
        } catch (OperationFailedException ex) {
            recordLogRecord(LogRecordGenerator.msgsHeldDirFailedToInit(ex));
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
         * FIXME: there should be code here to verify that the state of the
         * message file directories is consistent with the sequence numbers in
         * the 'sites' table. Perhaps also to verify that the other two core
         * modules tables are in-sync with the ISM's they claim to have
         * processed.
         */

        /*
         * Next task: create an IsmPushAgent object if ISM pushing has been
         * enabled.
         */
        if (Boolean.parseBoolean(properties.getProperty("SitMsgsPush"))) {
            ismPushAgent = new IsmPushAgent(
                    Long.parseLong(properties.getProperty("SitMsgsPushPredelay")),
                    this,
                    this.topologyAgent,
                    new IsmExchanger(
                            Integer.parseInt(properties.getProperty(
                                    "SitHttpConnectTimeout")),
                            Integer.parseInt(properties.getProperty(
                                    "SitHttpReadTimeout"))));
            ismPushAgent.start();
        }

        /*
         * Next task: spawn the worker thread and wait for it to start. The
         * thread name and wait time are user-configurable.
         */
        workerThread.set(new Thread(this,
                properties.getProperty("SitWorkerThreadName")));
        workerThread.get().start();
        int startupTime = Integer.parseInt(
                properties.getProperty("SitWorkerThreadShutdownTime"));
        if (!startupSignal.receive(startupTime)) {
            // The worker thread failed to start in a timely fashion.
            // Terminate.
            workerThread.getAndSet(null).interrupt();
            recordLogRecord(LogRecordGenerator.threadFailedToStart(this));
            stop();
            return false;
        }

        /*
         * Final task: bind ourselves to RMI so that other components can
         * connect to us and begin making requests.
         */
        try {
            Naming.bind("//localhost:" + properties.getProperty("GenRmiPort")
                    + "/" + properties.getProperty("SitRmiName"), this);
        } catch (AlreadyBoundException ex) {
            recordLogRecord(LogRecordGenerator.rmiAlreadyBoundException(this,
                    properties.getProperty("SitRmiName"), ex));
            stop();
            return false;
        } catch (ConnectException ex) {
            recordLogRecord(LogRecordGenerator.rmiConnectException(this, ex));
            stop();
            return false;
        } catch (Exception ex) {
            recordLogRecord(LogRecordGenerator.rmiException(this, ex));
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
        // Notify StatisticsAgent of the impending shutdown.
        if (statisticsAgent != null) {
            statisticsAgent.notifyCoreShutdown();
        }

        // Unbind the RMI name so that new calls can't arrive
        try {
            // FIXME: This probably doesn't have the effect described above
            Naming.unbind(properties.getProperty("SitRmiName"));
        } catch (Exception ex) {
            // If this operation fails, it fails. We don't need
            // to take any special action.
        }

        // Stop the IsmPushAgent thread. (Wait for it to terminate.)
        if (ismPushAgent != null) {
            ismPushAgent.stop();
        }

        // Signal the worker thread to stop
        Thread temp = workerThread.getAndSet(null);
        
        if (temp != null) {
            temp.interrupt();
        } else {
            /*
             * the thread has terminated abornormally previously (or was never
             * started), so just report that the thread has already terminated.
             */
            shutdownSignal.send();
        }
    }

    /**
     * The work for the primary worker thread.  Will not terminate normally
     * unless the {@link #stop()} method is invoked by another thread.
     */
    public void run() {
        try {

            // Initialize variables needed for thread management
            Thread thisThread = Thread.currentThread();

            // Perform any one-time initialization tasks
            schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SitRandomizerTask"),
                    new Runnable() {
                        public void run() {
                            randomIdGenerator.nextInt();
                        }
                    }));
            schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SitReplayRequestTask"),
                    new Runnable() {
                        public void run() {
                            doReplayRequestTask();
                        }
                    }));
            schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SitRedeliverHeldMsgsTask"),
                    new Runnable() {
                        public void run() {
                            doRedeliverHeldMsgsTask();
                        }
                    }));
            schedulePeriodicTask(new CoreScheduledTask(
                    properties.getProperty("SitStatsCommitTask"),
                    new Runnable() {
                        public void run() {
                            statisticsAgent.periodicWriteBehind();
                        }
                    }));

            // Tell everyone that we've started successfully
            startupSignal.send();

            // Do our primary thread looper
            while (workerThread.get() == thisThread) {
                CoreMessage msg = messageQueue.receive(0);
                
                if (msg == null) {
                    recordLogRecord(LogRecordGenerator.threadInterrupted(this));
                } else {
                    processCoreMessage(msg);
                }
            }

            // A shutdown request has been received. Perform shutdown tasks.

            // Tell every other thread that Site Manager's worker thread has
            // terminated gracefully
            shutdownSignal.send();
        } catch (Exception ex) {
            // Generic catch-all for otherwise uncaught exceptions
            recordLogRecord(LogRecordGenerator.threadException(this, ex));
        }
    }



    /**************************************************************************
     * REMOTE METHODS
     *************************************************************************/



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
        if (!workerThread.get().isAlive() || !pingMutex.acquire(milliseconds)) {
            return false;
        }

        try {
            /*
             * Ask the worker thread to send the pingSignal and await his reply
             */
            pingSignal.reset();
            messageQueue.send(new PingCM());
            
            return pingSignal.receive(milliseconds);
        } finally {
            // Release the mutex lock and return
            pingMutex.release();
        }
    }

    /**
     * @return the {@code SiteInfo} container that corresponds with the
     *         specified {@code siteId}.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified site id is invalid.
     */
    public SiteInfo getSiteInfo(int siteId) throws OperationFailedException,
            ResourceNotFoundException {
        // Try to find the specified site in the cache
        SiteInfo site = siteCache.get(siteId);
        if (site != null) {
            return site.clone();
        }

        // Try to find the specified site in the database
        site = dbFetchSite(siteId);
        if (site != null) {
            siteCache.put(site.id, site);
            return site.clone();
        }

        // Site not found; must be an invalid number
        throw new ResourceNotFoundException(new SiteInfo(siteId));
    }

    /**
     * @return the {@code SiteInfo} container that represents the local site.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public SiteInfo getLocalSiteInfo() throws OperationFailedException,
            ResourceNotFoundException {
        return getSiteInfo(this.localSiteId);
    }

    /**
     * @return the LabInfo container that corresponds with the specified
     *         {@code labId}.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified lab id is invalid.
     */
    public LabInfo getLabInfo(int labId) throws OperationFailedException,
            ResourceNotFoundException {
        LabInfo lab;
        
        // Try to find the specified lab in the cache
        lab = labCache.get(labId);
        if (lab != null) {
            return lab.clone();
        }

        // Try to find the specified lab in the database
        lab = dbFetchLab(labId);
        if (lab != null) {
            labCache.put(lab.id, lab);
            return lab.clone();
        }

        // Lab not found; must be an invalid number
        throw new ResourceNotFoundException(new LabInfo(labId));
    }

    /**
     * @return the ProviderInfo container that corresponds with the specified
     *         {@code providerId}.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified provider id is
     *         invalid.
     */
    public ProviderInfo getProviderInfo(int providerId)
            throws OperationFailedException, ResourceNotFoundException {
        ProviderInfo provider;
        
        // Try to find the specified provider in the cache
        provider = providerCache.get(providerId);
        if (provider != null) {
            return provider.clone();
        }

        // Try to find the specified provider in the database
        provider = dbFetchProvider(providerId);
        if (provider != null) {
            providerCache.put(provider.id, provider);
            return provider.clone();
        }

        // Provider not found; must be an invalid number
        throw new ResourceNotFoundException(new ProviderInfo(providerId));
    }

    /**
     * @return the UserInfo container that corresponds with the specified
     *         {@code userId}.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified user id is invalid.
     */
    public UserInfo getUserInfo(int userId) throws OperationFailedException,
            ResourceNotFoundException {
        UserInfo user;

        // Try first to find the specified user in the cache
        user = userCache.get(userId);
        if (user != null) {
            return user.clone();
        }

        // Try next to find the specified user in the database
        user = dbFetchUser(userId);
        if (user != null) {

            /*
             * Only active UserInfo objects may have their usernames registered
             * in the object cache.
             */
            if (user.isActive == true) {
                userCache.put(user.id, user.username, user);
            } else {
                userCache.put(user.id, null, user);
            }

            return user.clone();
        }

        // User not found; must be an invalid number
        throw new ResourceNotFoundException(new UserInfo(userId));
    }

    /**
     * @return the UserInfo container that corresponds with the specified
     *         {@code username}. Inactive users are invisible to this function
     *         and are not fetched. Particularly useful when authenticating user
     *         logon requests.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified username is invalid
     *         (or used to be valid, but no longer is active)
     */
    public UserInfo getUserInfo(String username)
            throws OperationFailedException, ResourceNotFoundException {
        UserInfo user;

        // Try first to find the specified user in the cache
        user = userCache.get(username);
        if (user != null) {
            return user.clone();
        }

        // Try next to find the specified user in the database
        user = dbFetchUser(username);
        if (user != null) {
            userCache.put(user.id, user.username, user);
            return user.clone();
        }

        // User not found; must be an invalid number
        throw new ResourceNotFoundException(new UserInfo(username));
    }

    /**
     * @return the LabInfo container for the lab that originated the specified
     *         sample, identified by its {@code sampleId}.
     * @throws InconsistentDbException if the specified sample could not be
     *         fetched (via SampleManager) because of a database inconsistency.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the sample id is not valid, or the
     *         sample's originating lab id (as recorded) is not valid.
     */
    public LabInfo getLabForSample(int sampleId)
            throws InconsistentDbException, OperationFailedException,
            ResourceNotFoundException {
        SampleInfo s = sampleManager.getSampleInfo(sampleId);
        
        return getLabInfo(s.labId);
    }

    /**
     * Fetches an array of {@code UserInfo} containers that represents the set
     * of all users (both active and inactive) associated with the specified
     * lab. Valid only for labs currently hosted at the local site since user
     * records are inherently local. User records associated with providers are
     * not returned. The current implementation avoids a database query by
     * consulting the users cache, assuming the cache preload was successful.
     * 
     * @param labId identifies the lab for which users should be fetched
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the specified lab id is invalid
     * @throws WrongSiteException if the specified lab is not hosted at the
     *         local site
     */
    public UserInfo[] getUsersForLab(int labId)
            throws OperationFailedException, ResourceNotFoundException,
            WrongSiteException {
        UserInfo[] users;
        
        /*
         * Retrieve a LabInfo to verify that the specified lab ID is valid; this
         * will throw an exception if the ID is invalid
         */
        LabInfo lab = getLabInfo(labId);
        
        // lab id is invalid
        if (lab.homeSiteId != this.localSiteId) {
            throw new WrongSiteException(new LabInfo(labId));
        }

        /*
         * If it is safe to do so then answer the query from cached data alone.
         * The cache was preloaded when this module first started.
         */
        if (!userCache.hasEverLostItem()) {
            List<UserInfo> usersToReturn = new ArrayList<UserInfo>();

            /*
             * We need to filter the set of all users, accepting only
             * those whose lab id matches the one specified by the caller.
             */
            for (UserInfo user : userCache.getAll(UserInfo.class)) {
                if (user.labId == labId) {
                    usersToReturn.add(user.clone());
                }
            }
            
            users = usersToReturn.toArray(new UserInfo[usersToReturn.size()]);
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    
                    try {
                        ResultSet rs = cmd.executeQuery("SELECT * FROM users"
                                + " WHERE lab_id=" + labId + ";");

                        // Determine the number of user and allocate the array
                        rs.last();
                        users = new UserInfo[rs.getRow()];
                        rs.beforeFirst();

                        // Create one new UserInfo for every row in the
                        // recordset;
                        // put it in our array
                        for (int i = 0; i < users.length; i++) {
                            rs.next();
                            users[i] = new UserInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }

        return users;
    }

    /**
     * Fetches an array of {@code UserInfo} containers that represents the set
     * of all users (both active and inactive) associated with the specified
     * provider. Valid only for providers that belong to labs currently hosted
     * at the local site since user records are inherently local. User records
     * associated with labs are not returned. The current implementation avoids
     * a database query by consulting the users cache, assuming the cache
     * preload was successful.
     * 
     * @param providerId identifies the provider for which users should be
     *        fetched
     * @throws OperationFailedException if a database error occurs
     * @throws ResourceNotFoundException if the specified provider id is invalid
     * @throws WrongSiteException if the specified provider's parent lab is not
     *         hosted at the local site
     */
    public UserInfo[] getUsersForProvider(int providerId)
            throws OperationFailedException, ResourceNotFoundException,
            WrongSiteException {
        UserInfo[] users;

        /*
         * Retrieve a ProviderInfo and verify that the specified provider is
         * valid; this will throw an exception if the provider id is invalid
         */
        ProviderInfo provider = getProviderInfo(providerId); 

        /*
         * Verify that the provider's associated lab is valid; this will throw
         * an exception if the lab ID is invalid
         */
        LabInfo lab = getLabInfo(provider.labId);
        
        if (lab.homeSiteId != this.localSiteId) {
            throw new WrongSiteException(lab);
        }
        
        /*
         * If it is safe to do so then answer the query from cached data alone.
         * The cache was preloaded when this module first started.
         */
        if (!userCache.hasEverLostItem()) {
            List<UserInfo> usersToReturn = new ArrayList<UserInfo>();

            /*
             * We need to filter the set of all users and return only
             * those whose provider id matches the one specified by the caller.
             */
            for (UserInfo user : userCache.getAll(UserInfo.class)) {
                if (user.providerId == providerId) {
                    usersToReturn.add(user.clone());
                }
            }

            users = usersToReturn.toArray(new UserInfo[usersToReturn.size()]);
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    
                    try {
                        ResultSet rs = cmd.executeQuery("SELECT * FROM users"
                                + " WHERE provider_id=" + providerId + ";");

                        // Determine the number of user and allocate the array
                        rs.last();
                        users = new UserInfo[rs.getRow()];
                        rs.beforeFirst();

                        /*
                         * Create one new UserInfo in our array for every row
                         * in the ResultSet
                         */
                        for (int i = 0; i < users.length; i++) {
                            rs.next();
                            users[i] = new UserInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        
        return users;
    }

    /**
     * Fetches an array of {@code SiteInfo} containers that represents the set
     * of all known sites. The current implementation avoids a database query by
     * consulting the sites cache, assuming the cache preload was successful.
     * 
     * @throws OperationFailedException if a database error occurs
     */
    public SiteInfo[] getAllSiteInfo() throws OperationFailedException {
        SiteInfo[] sites;

        /*
         * If it is safe to do so then answer the request from cache alone.  The
         * cache was preloaded when this module first started.
         */
        if (!siteCache.hasEverLostItem()) {
            sites = siteCache.getAll(SiteInfo.class);
            for (int i = 0; i < sites.length; i++) {
                sites[i] = sites[i].clone();
            }
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    
                    try {
                        ResultSet rs = cmd.executeQuery("SELECT * FROM sites;");

                        // Determine the number of sites and allocate the array
                        rs.last();
                        sites = new SiteInfo[rs.getRow()];
                        rs.beforeFirst();

                        /*
                         * Create one new SiteInfo in our array for every row in
                         * the sites table
                         */
                        for (int i = 0; i < sites.length; i++) {
                            rs.next();
                            sites[i] = new SiteInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        
        return sites;
    }

    /**
     * Variant of getAllSiteInfo() that returns potentially fewer records.
     * Those sites with field isActive==false are excluded from the returned
     * array.
     */
    public SiteInfo[] getAllActiveSiteInfo() throws OperationFailedException {
	ArrayList<SiteInfo> sites 
                = new ArrayList(Arrays.asList(this.getAllSiteInfo()));
	Iterator<SiteInfo> it = sites.iterator();
	while (it.hasNext()) {
	    SiteInfo site = it.next();
	    if (!site.isActive) {
		it.remove();
	    }
	}
	return sites.toArray(new SiteInfo[sites.size()]);
    }

    /**
     * Fetches an array of {@code LabInfo} containers that represents the set of
     * all known labs. The current implementation avoids a database query by
     * consulting the labs cache, assuming the cache preload was successful.
     * 
     * @throws OperationFailedException if a database error occurs
     */
    public LabInfo[] getAllLabInfo() throws OperationFailedException {
        LabInfo[] labs;

        /*
         * If it is safe to do so then answer the request from cache alone.  The
         * cache was preloaded when this module first started.
         */
        if (!labCache.hasEverLostItem()) {
            labs = labCache.getAll(LabInfo.class);
            for (int i = 0; i < labs.length; i++) {
                labs[i] = labs[i].clone();
            }
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    
                    try {
                        ResultSet rs = cmd.executeQuery("SELECT * FROM labs;");

                        // Determine the number of labs and allocate the array
                        rs.last();
                        labs = new LabInfo[rs.getRow()];
                        rs.beforeFirst();

                        /*
                         * Create one new LabInfo in our array for every row in
                         * the labs table
                         */
                        for (int i = 0; i < labs.length; i++) {
                            rs.next();
                            labs[i] = new LabInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        
        return labs;
    }

    /**
     * Fetches an array of {@code ProviderInfo} containers that represents the
     * set of all providers that belong to the specified parent lab. The current
     * implementation avoids a database query by consulting the providers cache,
     * assuming the cache preload was successful.
     * 
     * @param labId identifies the lab whose associated providers should be
     *        fetched
     * @throws OperationFailedException if a database error occurs
     * @throws ResourceNotFoundException if the specified lab id is invalid
     */
    public ProviderInfo[] getAllProviderInfo(int labId)
            throws OperationFailedException, ResourceNotFoundException {
        ProviderInfo[] providers;

        // Verify the lab id and throw an exception it's invalid.
        getLabInfo(labId);

        /*
         * If it is safe to do so then answer the request from cache alone.  The
         * cache was preloaded when this module first started.
         */
        if (!providerCache.hasEverLostItem()) {
            List<ProviderInfo> providersToReturn= new ArrayList<ProviderInfo>();
            
            /*
             * We need to filter the providers to select only those whose lab id
             * matches the one specified by the caller.
             */
            for (ProviderInfo provider
                    : providerCache.getAll(ProviderInfo.class)) {
                if (provider.labId == labId) {
                    providersToReturn.add(provider.clone());
                }
            }

            providers = providersToReturn.toArray(
                    new ProviderInfo[providersToReturn.size()]);
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    
                    try {
                        ResultSet rs = cmd.executeQuery(
                                "SELECT * FROM providers WHERE lab_id=" + labId
                                + ";");

                        // Determine the number of providers and allocate the
                        // array
                        rs.last();
                        providers = new ProviderInfo[rs.getRow()];
                        rs.beforeFirst();

                        // Create one new ProviderInfo for every row in the
                        // providers table; put it in our array
                        for (int i = 0; i < providers.length; i++) {
                            rs.next();
                            providers[i] = new ProviderInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        
        return providers;
    }

    /**
     * Fetches an array of {@code UserInfo} containers that represents the set
     * of all local users, both active and inactive. The current implementation
     * avoids a database query by consulting the users cache, assuming the cache
     * preload was successful.
     * 
     * @throws OperationFailedException if a database error occurs
     */
    public UserInfo[] getAllUserInfo() throws OperationFailedException {
        UserInfo[] users;

        /*
         * If it is safe to do so then answer the request from cache alone.  The
         * cache was preloaded when this module first started.
         */
        if (!userCache.hasEverLostItem()) {
            users = userCache.getAll(UserInfo.class);
            for (int i = 0; i < users.length; i++) {
                users[i] = users[i].clone();
            }
        } else {

            // Proceed with a database read instead.
            try {
                synchronized (conn) {
                    Statement cmd = conn.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);

                    try {
                        ResultSet rs = cmd.executeQuery("SELECT * FROM users;");

                        // Determine the number of user and allocate the array
                        rs.last();
                        users = new UserInfo[rs.getRow()];
                        rs.beforeFirst();

                        // Create one new UserInfo for every row in the
                        // recordset; put it in our array
                        for (int i = 0; i < users.length; i++) {
                            rs.next();
                            users[i] = new UserInfo(rs);
                        }
                    } finally {
                        cmd.close();
                    }
                }
            } catch (SQLException ex) {
                throw new OperationFailedException(ex);
            }
        }
        
        return users;
    }

    /**
     * Gets an empty ProviderInfo container that the caller can populate and
     * then pass to {@code writeUpdatedProviderInfo()}.
     */
    public ProviderInfo getEmptyProviderInfo() {
        return new ProviderInfo();
    }

    /**
     * Gets an empty UserInfo container that the caller can populate and then
     * pass to {@code writeUpdatedUserInfo()}.
     */
    public UserInfo getEmptyUserInfo() {
        return new UserInfo();
    }

    /**
     * Writes an updated lab record to the database (and the cache). The
     * {@code LabInfo} object might have been obtained by a previous call to
     * {@code getLabInfo()} (for instance), and should have been altered by the
     * caller since.
     * 
     * @throws InvalidDataException if any of the data strings have characters
     *         that are invalid for a container object. The possible reason
     *         codes are {@code ILLEGAL_NAME}, {@code ILLEGAL_SHORT_NAME},
     *         {@code ILLEGAL_DIRECTORY_NAME}, {@code ILLEGAL_URL}, and
     *         {@code ILLEGAL_COPYRIGHT_NOTICE}
     * @throws InvalidModificationException if an existing lab record is to be
     *         updated, but the caller made a prohibited modification to the
     *         {@code LabInfo} object. The field {@code homeSiteId} must not be
     *         modified. Additionally, no modifications may be made to a lab
     *         record that is already inactive.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws OptimisticLockingException if the update could not be completed
     *         because a possibly conflicting update to the same lab record was
     *         made recently.
     * @throws ResourceNotFoundException if the id specified in the lab record
     *         does not match an already-existing lab record (i.e. updates to
     *         unknown labs are not supported).
     * @throws WrongSiteException if the lab attempting to be updated is not
     *         currently hosted at the local site.
     */
    public void writeUpdatedLabInfo(LabInfo newLabInfo)
            throws InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        assertIsmGenerationNotHalted();

        // Do a quick sanity check on the user-provided container.
        if (newLabInfo.homeSiteId != this.localSiteId) {
            throw new WrongSiteException(newLabInfo);
        }

        // Retrieve the "old" version of the LabInfo container and compare
        // it to the new one. Changes to certain fields are prohibited.  This
        // throws an exception if the specified lab doesn't exist
        LabInfo oldLabInfo = this.getLabInfo(newLabInfo.id);       
        if (oldLabInfo.homeSiteId != newLabInfo.homeSiteId) {
            throw new InvalidModificationException(
                    InvalidModificationException.CANTCHANGE_SITE, newLabInfo);
        }
        if (oldLabInfo.isActive == false) {
            throw new InvalidModificationException(
                    InvalidModificationException.INACTIVE, newLabInfo);
        }

	// Continue with further validation and maybe perform the update.
	this.writeUpdatedLabInfo2(newLabInfo);

        // Transmit an update message to all other sites
        passCoreMessage(
                new SendIsmCM(new LabUpdateISM(newLabInfo, localSiteId)));
    }

    /**
     * Writes an updated provider record to the database (and the cache). The
     * {@code ProviderInfo} object might have been obtained by a previous call
     * to {@code getProviderInfo()} or {@code getEmptyProviderInfo()} (for
     * instance), and should have been altered by the caller since.
     * 
     * @return the id of the provider record written. This id is auto-assigned
     *         if a new provider record was created.
     * @throws InvalidDataException if any of the data strings have characters
     *         that are invalid for a container object. The possible reason
     *         codes are {@code ILLEGAL_NAME}, {@code ILLEGAL_CONTACT}, and
     *         {@code ILLEGAL_COMMENTS}
     * @throws InvalidModificationException if an existing provider record is to
     *         be updated, but the caller made a prohibited modification to the
     *         {@code ProviderInfo} object. The field {@code labId} must not be
     *         modified. Additionally, no modifications may be made to a
     *         provider record that is already inactive.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws OptimisticLockingException if the update could not be completed
     *         because a possibly conflicting update to the same provider record
     *         was made recently.
     * @throws ResourceNotFoundException if the new provider's id field
     *         indicates that it was derived from an existing provider record,
     *         but that existing provider record could not be found.
     * @throws WrongSiteException if the parent lab for the provider to be
     *         written is not currently hosted at the local site.
     */
    public int writeUpdatedProviderInfo(ProviderInfo newProviderInfo)
            throws InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        assertIsmGenerationNotHalted();
        
        // Retrieve the "old" version of the ProviderInfo container and compare
        // it to the new one. Changes to certain fields are prohibited.
        if (newProviderInfo.id != ProviderInfo.INVALID_PROVIDER_ID) {
            
            // throws an exception if the provider id doesn't already exist:
            ProviderInfo oldProviderInfo = getProviderInfo(newProviderInfo.id);
            
            if (oldProviderInfo.labId != newProviderInfo.labId) {
                throw new InvalidModificationException(
                        InvalidModificationException.CANTCHANGE_LAB,
                        newProviderInfo);
            }
            if (oldProviderInfo.isActive == false) {
                throw new InvalidModificationException(
                        InvalidModificationException.INACTIVE, newProviderInfo);
            }
            if (oldProviderInfo.ts != newProviderInfo.ts) {
                throw new OptimisticLockingException(newProviderInfo);
            }
        }

        /*
         * Retrieve the associated lab and make sure it's based at this site
         */
        LabInfo lab = getLabInfo(newProviderInfo.labId);
        
        // previous line this throws an exception if the lab id isn't valid
        if (lab.homeSiteId != this.localSiteId) {
            throw new WrongSiteException(newProviderInfo);
        }

        // make sure there are no illegal characters
        if ((newProviderInfo.name != null)
                && !providerStringFieldValidator.isValid(
                        newProviderInfo.name)) {
            throw new InvalidDataException(newProviderInfo,
                    InvalidDataException.ILLEGAL_NAME);
        }
        if ((newProviderInfo.headContact != null)
                && !providerStringFieldValidator.isValid(
                        newProviderInfo.headContact)) {
            throw new InvalidDataException(newProviderInfo,
                    InvalidDataException.ILLEGAL_CONTACT);
        }
        if ((newProviderInfo.comments != null)
                && !providerStringFieldValidator.isValid(
                        newProviderInfo.comments)) {
            throw new InvalidDataException(newProviderInfo,
                    InvalidDataException.ILLEGAL_COMMENTS);
        }

        // The update is permissible. Proceed.

        // Update our local database and transmit an update message to all the
        // other sites.
        newProviderInfo.ts = serialNumber.get();
        if (newProviderInfo.id == ProviderInfo.INVALID_PROVIDER_ID) {
            /*
             * We do not need to serialize access to the following two lines of
             * code to guard against potentially duplicate provider id's - no
             * special handling is required. This is because
             * generateProviderId() returns cryptographically random numbers and
             * serializes access to the random number pool. Therefore it is
             * computationally infeasible that the same number would be returned
             * twice. Furthermore, any attempt to serialize the two lines below
             * would be inherently deadlock-prone because we're also serializing
             * access to the database connection.
             */
            newProviderInfo.id = generateProviderId();
            dbAddProvider(newProviderInfo);
            providerCache.put(newProviderInfo.id, newProviderInfo);
            passCoreMessage(new SendIsmCM(new ProviderActivationISM(
                    newProviderInfo, this.localSiteId)));
        } else {
            dbUpdateProvider(newProviderInfo);
            providerCache.put(newProviderInfo.id, newProviderInfo);
            passCoreMessage(new SendIsmCM(new ProviderUpdateISM(
                    newProviderInfo, this.localSiteId)));
        }

        return newProviderInfo.id;
    }

    /**
     * Writes an updated UserInfo. The UserInfo object should have been obtained
     * by a previous call to getEmptyUserInfo(), getUserInfo(),
     * getAllUserInfo(), getUsersForProvider(), or getUsersForLab(). This method
     * will create a new user record if
     * {@code newUserInfo.id == UserInfo.INVALID_USER_ID}. Returns the id of
     * the user record.
     * <p>
     * The {@code newUser.id} field is changed to be the user's id, if a new
     * user is being created, a {@code newUser.ts} value is assigned, the
     * {@code newUser.username} is changed to all-lowercase, the
     * {@code newUser.creationDate} is truncated to second precision, and the
     * {@code newUser.inactiveDate} is truncated to second precision.
     * 
     * @return the id of the user record written. This id is auto-assigned if a
     *         new user record was created.
     * @throws DuplicateDataException if a new user record is to be created, but
     *         {@code newUser.username} is already is use by another active user
     *         account on the local site.
     * @throws InvalidDataException with {@code reason} {@code ILLEGAL_FULLNAME},
     *         {@code ILLEGAL_NAME}, {@code HAS_LAB_AND_PROVIDER},
     *         {@code NEED_LAB_OR_PROVIDER}, {@code NEED_INACTIVE_DATE} or
     *         {@code INVALID_ACCESS_FOR_USER_TYPE} if the specified
     *         {@code newUser} object was not valid.
     * @throws InvalidModificationException if an existing user record is to be
     *         updated, but the caller made a prohibited modification to the
     *         {@code UserInfo} object. The fields {@code labId},
     *         {@code providerId}, {@code creationDate}, {@code userName},
     *         and {@code inactiveDate} (unless the user record is to be
     *         deactivated) must not be modified. Additionally, no modifications
     *         may be made to a user record that is already inactive.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws OptimisticLockingException if the update could not be completed
     *         because a possibly conflicting update to the same user record was
     *         made recently.
     * @throws ResourceNotFoundException if {@code newUser.id} indicates that an
     *         existing user record is to be altered, but the existing user
     *         record could not be found. Also might occur if the user's
     *         associated lab or provider record could not be found.
     * @throws WrongSiteException if the user's parent lab (or the user's parent
     *         provider's parent lab) is not currently hosted at the local site.
     */
    public int writeUpdatedUserInfo(UserInfo newUser)
            throws DuplicateDataException, InvalidDataException,
            InvalidModificationException, OperationFailedException,
            OptimisticLockingException, ResourceNotFoundException,
            WrongSiteException {
        LabInfo lab;
        ProviderInfo provider;
        UserInfo oldUser;

        // Quick sanity check - either providerId or labId should be set, but
        // not both
        if ((newUser.providerId == ProviderInfo.INVALID_PROVIDER_ID)
                && (newUser.labId == LabInfo.INVALID_LAB_ID)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.NEED_LAB_OR_PROVIDER);
        }
        if ((newUser.providerId != ProviderInfo.INVALID_PROVIDER_ID)
                && (newUser.labId != LabInfo.INVALID_LAB_ID)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.HAS_LAB_AND_PROVIDER);
        }

        // ensure that the access level is appropriate for the user type
        if ((newUser.providerId == ProviderInfo.INVALID_PROVIDER_ID)
                && ((newUser.globalAccessLevel
                        & UserInfo.SUBMITTING_PROVIDER_ACCESS) != 0)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.INVALID_ACCESS_FOR_USER_TYPE);
        }

        // make sure there are no illegal characters
        if (!userStringFieldValidator.isValid(newUser.fullName)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.ILLEGAL_FULLNAME);
        }
        if (!userStringFieldValidator.isValid(newUser.username)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.ILLEGAL_NAME);
        }

        // Retrieve the "old" version of the UserInfo container and compare
        // it to the new one. Changes to certain fields are prohibited.
        if (newUser.id != UserInfo.INVALID_USER_ID) {
            // throws an exception if the user id doesn't already exist:
            oldUser = getUserInfo(newUser.id);

            if (oldUser.labId != newUser.labId) {
                throw new InvalidModificationException(
                        InvalidModificationException.CANTCHANGE_LAB, newUser);
            }
            if (oldUser.providerId != newUser.providerId) {
                throw new InvalidModificationException(
                        InvalidModificationException.CANTCHANGE_PROVIDER,
                        newUser);
            }
            if (!oldUser.creationDate.equals(newUser.creationDate)) {
                throw new InvalidModificationException(
                        InvalidModificationException.CANTCHANGE_CREATIONDATE,
                        newUser);
            }
            if (oldUser.isActive == false) {
                throw new InvalidModificationException(
                        InvalidModificationException.INACTIVE, newUser);
            }
            if (!oldUser.username.equals(newUser.username)) {
                throw new InvalidModificationException(
                        InvalidModificationException.CANTCHANGE_USERNAME,
                        newUser);
            }
            if (oldUser.ts != newUser.ts) {
                throw new OptimisticLockingException(newUser);
            }
        }

        // Retrieve the associated lab or provider and make sure it's based at
        // the local site
        if (newUser.labId != LabInfo.INVALID_LAB_ID) {
            lab = getLabInfo(newUser.labId); // throws an exception if
            // lab id is invalid
            if (lab.homeSiteId != this.localSiteId) {
                throw new WrongSiteException(newUser);
            }
        }
        if (newUser.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
            provider = getProviderInfo(newUser.providerId);
            // previous line throws an exception if provider id is invalid
            lab = getLabInfo(provider.labId);
            // previous line throws an exception if lab id is invalid
            if (lab.homeSiteId != this.localSiteId) {
                throw new WrongSiteException(newUser);
            }
        }

        /*
         * Check for name conflicts - the username must be unique on this site
         * (ignoring inactive users). Ignore a previous user record with the
         * same id because this will be overwritten.
         */
        oldUser = null;
        try {
            oldUser = getUserInfo(newUser.username);
        } catch (ResourceNotFoundException ex) {
            // we can ignore any exception that might be generated; this
            // would simply indicate that a duplicate name was not found.
        }
        if ((oldUser != null) && (oldUser.id != newUser.id)) {
            throw new DuplicateDataException(newUser,
                    DuplicateDataException.USERNAME);
        }

        // Do other general validation.
        if (!newUser.isActive && (newUser.inactiveDate == null)) {
            throw new InvalidDataException(newUser,
                    InvalidDataException.NEED_INACTIVE_DATE);
        }

        // The update is permissible. Proceed.

        /*
         * Truncate the creation and user dates to seconds precision. This is
         * necessary to correct bugs related to cache maintenance, since a Date
         * object in memory normally would have millisecond precision but a Date
         * object read from our database (a 'datetime' column) has only second
         * precision.
         */
        /*
         * TODO: consider using a more portable scheme here -- what if the db
         * engine supports a different date precision?
         */
        if (newUser.creationDate != null) {
            newUser.creationDate.setTime(
                    (newUser.creationDate.getTime() / 1000) * 1000);
        }
        if (newUser.inactiveDate != null) {
            newUser.inactiveDate.setTime(
                    (newUser.inactiveDate.getTime() / 1000) * 1000);
        }

        // Update our local database
        
        newUser.ts = serialNumber.get();
        
        // by convention, usernames are all-lowercase
        newUser.username = newUser.username.toLowerCase();
        if (newUser.id == UserInfo.INVALID_USER_ID) {
            /*
             * We do not need to serialize access to the following two lines of
             * code to guard against potentially duplicate user id's - no
             * special handling is required. This is because generateUserId()
             * returns cryptographically random numbers and serializes access to
             * the random number pool. Therefore it is computationally
             * infeasible that the same number would be returned twice.
             * Furthermore, any attempt to serialize the two lines below would
             * be inherently deadlock-prone because we're also serializing
             * access to the database connection.
             */
            newUser.id = generateUserId();
            dbAddUser(newUser);
        } else {
            dbUpdateUser(newUser);
        }
        
        // Only active UserInfo may have their usernames registered in
        // the object cache
        if (newUser.isActive == true) {
            userCache.put(newUser.id, newUser.username, newUser);
        } else {
            userCache.put(newUser.id, null, newUser);
        }
        
        return newUser.id;
    }

    /**
     * @return the {@code LocalTrackingConfig} object associated with the
     *         specified lab.
     * @param labId identifies the lab for which the LTC should be fetched
     * @throws WrongSiteException if the specified lab is not currently hosted
     *         at the local site.
     */
    public LocalTrackingConfig getLocalTrackingConfig(int labId)
            throws WrongSiteException {
	synchronized (this.localTrackingConfigMap) {
	    LocalTrackingConfig ltc = localTrackingConfigMap.get(labId);       
	    if (ltc == null) {
		throw new WrongSiteException(new LabInfo(labId));
	    } else {
		return ltc.clone();
	    }
	}
    }

    /**
     * Used by the wrapper modules to write something to syslog. The specified
     * event should be a subclass of LogEvent created specifically for that
     * event type. Core modules should call RecordLogRecord() instead.
     */
    public void recordLogEvent(LogEvent ev) {
        /*
         * TODO: change the implementation here so that different kinds of log
         * events are directed to severity levels appropriately. Also (possibly)
         * maintain counters about the different kinds of wrapper-level events
         * that have happened.
         */
        statisticsAgent.notifyLogEvent(ev);
        for (LogRecord record : ev.getLogRecords()) {
            recordLogRecord(record);
        }
    }

    /**
     * Used by the {@code IsmListener} servlet to pass potentially many ISM's
     * received from a remote site to the core and to receive potentially many
     * replies from the core. Both ways, the messages are passed as
     * {@code String}'s containing XML fragments, where each fragment begins
     * with {@code &lt;message&gt;} and ends with {@code &lt;/message&gt;}. The
     * current implementation is no more than a stub function as all
     * functionality is delegated to {@code ReceivedMessageAgent}.
     * 
     * @return an array of zero or more {@code String}'s, each of which is an
     *         XML document that contains a signed representation of an
     *         {@code InterSiteMessage} addressed to the remote site. These are
     *         responses to the incoming ISM's passed to this function as
     *         {@code messageStrings}. The caller is expected to transmit these
     *         ISM's back to the remote site as part of an exchange.
     * @param messageStrings an array of zero or more {@code String}'s each of
     *        which is an XML representation of an {@code InterSiteMessage} that
     *        was transmitted to the local site from the remote site as part of
     *        an exchange.
     * @param ipAddr a string that identifies the numeric IP address of the
     *        remote site, for logging purposes.
     * @throws OperationFailedException on low-level error. Note that to the
     *         greatest extent possible, exceptions specific to one particular
     *         message are not propagated to the caller but caught by this
     *         method and logged.
     */
    public String[] exchangeInterSiteMessages(String messageStrings[],
            String ipAddr) throws OperationFailedException {
        return this.receivedMessageAgent.exchangeInterSiteMessages(
                messageStrings, ipAddr, true, true);
    }

    /**
     * Converts the specified link-local ISM object to a string, applying a
     * digital signature along the way. This function is deliberately restricted
     * to link-local ISM's for security reasons. (The proper way to create a
     * non-link-local ISM is to send a {@code SendIsmCM} core message to site
     * manager.)
     * 
     * @return a signed XML representation of {@code ism} suitable for
     *         transmission.
     * @param ism the {@code InterSiteMessage} object to be converted to XML.
     *        This function does not set any fields within this object; the
     *        caller is responsible for setting all fields appropriately.
     * @throws IllegalArgumentException if {@code ism} is not a suitable
     *         link-local ISM. Specifically, {@code ism.sourceSiteId} must equal
     *         the local site's id, {@code ism.sourceSeqNum} and
     *         {@code ism.sourcePrevSeqNum} must equal
     *         {@code InterSiteMessage.INVALID_SEQ_NUM}, and
     *         {@code ism.linkLocal} must be set in order to avoid this
     *         exception.
     * @throws OperationFailedException if the local site's private key is
     *         invalid or if the signature could not be added for some other
     *         low-level reason.
     */
    public String signLinkLocalInterSiteMessage(InterSiteMessage ism)
            throws OperationFailedException {
        if (!ism.linkLocal || (ism.sourceSiteId != this.localSiteId)
                || (ism.sourceSeqNum != InterSiteMessage.INVALID_SEQ_NUM)
                || (ism.sourcePrevSeqNum != InterSiteMessage.INVALID_SEQ_NUM)) {
            throw new IllegalArgumentException();
        }
        try {
            synchronized (this.sigEngine) {
                this.sigEngine.initSign(this.localPrivateKey);
                return ism.toXmlAddSignature(this.sigEngine);
            }
        } catch (InvalidKeyException ex) {
            throw new OperationFailedException(ex);
        } catch (SignatureException ex) {
            throw new OperationFailedException(ex);
        }
    }



    /**************************************************************************
     * INTRA-CORE METHODS (not remotely-accessible)
     *************************************************************************/



    /**
     * Allows other Core modules to pass a message to this one.
     * 
     * @throws OperationFailedException only if Site Manager was initialized in
     *         bootstrap mode, and then only if an error occurred while
     *         processing the specified {@code msg}. Such errors are reported
     *         synchronously in bootstrap mode because message processing
     *         happens synchronously in bootstrap mode.
     */
    public void passCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        if (!bootstrapMode) {
            messageQueue.send(msg);
        } else {
            processCoreMessage(msg);
        }
    }

    /**
     * Allows other Core modules to pass several messages to this core module at
     * once. The Collection should contain zero or more {@code CoreMessage}
     * objects.
     * 
     * @throws OperationFailedException only if Site Manager was initialized in
     *         bootstrap mode, and then only if an error occurred while
     *         processing any of the specified {@code msgs}. Such errors are
     *         reported synchronously in bootstrap mode because message
     *         processing happens synchronously.
     */
    public void passCoreMessages(Collection<? extends CoreMessage> msgs)
            throws OperationFailedException {
        if (!bootstrapMode) {
            messageQueue.sendSeveral(msgs);
        } else {
            for (CoreMessage msg : msgs) {
                processCoreMessage(msg);
            }
        }
    }

    /**
     * Schedule the specified periodic task to be run according to the timing
     * parameters specified in the CoreScheduledTask. When the task is triggered
     * this will result in a call from Site Manager to whichever core module
     * first scheduled the task.
     */
    void schedulePeriodicTask(CoreScheduledTask cst) {
        cst.setScheduleInfo(scheduler, currentTasks);
        scheduler.schedule(cst, cst.getInitialDelay());
    }

    /**
     * Returns an array of labId's that lists all active labs currently hosted
     * at the local site. It is conceivable that this return value might vary
     * during Site Manager's lifetime, though we have yet to implement such a
     * mechanism.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         due to a low-level error.
     */
    public int[] getLocalLabs() throws OperationFailedException {
        List<Integer> ids = new ArrayList<Integer>();
        int locallabs[];
        
        for (LabInfo lab : getAllLabInfo()) {
            if (lab.homeSiteId == this.localSiteId) {
                ids.add(lab.id);
            }
        }
        
        // allocate our return array and fill it with lab id's
        locallabs = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            locallabs[i] = ids.get(i).intValue();
        }

        return locallabs;
    }

    /**
     * Called by VersionUpdater during a bootstrap operation. This method
     * iterates through every message included in this site's site grant file
     * and causes it to be processed by the usual mechanisms.
     * 
     * @throws IllegalStateException if bootstrap mode was not enabled at
     *         construction time.
     * @throws IsmProcessingException as the {@code InterSiteMessage}s
     *         contained within the site grant file are processed, if any of
     *         them are invalid.
     * @throws OperationFailedException on low-level error. In each case an
     *         error message that identifies the section of code is sent to
     *         stderr just before this exception is thrown.
     * @throws OperationNotPermittedException if any data already exists in the
     *         local database. An informational error message is sent to stderr
     *         just before this exception is thrown.
     */
    void performBootstrapTasks() throws IsmProcessingException,
            OperationFailedException, OperationNotPermittedException {
        InterSiteMessage bundle[];
        int i;

        // quick sanity check
        if (!bootstrapMode) {
            throw new IllegalStateException("Bootstrap mode not enabled");
        }

        // second sanity check
        if (siteCache.size() != 0) {
            // It is desirable to write to stderr here since the user is
            // running us as a console app.
            System.err.println("You cannot run in bootstrap mode while there"
                    + " is data in the local database.  Please"
                    + " re-initialize the database before"
                    + " attempting to run bootstrap mode again.");
            throw new OperationNotPermittedException();
        }

        // prepare the received message agent by telling it about the existence
        // of the Coordinator.
        receivedMessageAgent.notifySiteActivation(new SiteInfo(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR));

        // read all the messages from the Coordinator that are embedded in
        // our site grant file
        File siteGrantFile = new File(properties.getProperty("SitGrantFile"));
        try {
            bundle = MsgpakUtil.readAndDecodeAllMessages(siteGrantFile);
        } catch (Exception ex) {
            // It is desirable to write to stderr here since the user is
            // running us as a console app.
            System.err.println("Error while reading the site grant file:"
                    + ex.toString());
            throw new OperationFailedException(ex);
        }

        // process the messages one by one
        // It is desirable to write to stdout here since the user is running
        // us as a console app.
        System.out.print("    processing " + bundle.length + " messages... ");
        for (i = 0; i < bundle.length; i++) {
            try {
                receivedMessageAgent.acceptBootstrapIsm(bundle[i]);
            } catch (OperationFailedException ex) {
                // It is desirable to write to stderr here since the user is
                // running us as a console app.
                System.err.println("Error while processing the site grant"
                        + " file: " + ex.toString());
                throw ex;
            }
        }
    }

    /**
     * Returns true if the specified lab directory name is associated with a
     * known lab; false otherwise.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         due to a lower-level error.
     */
    boolean isLabDirectoryValid(String labDirectoryName)
            throws OperationFailedException {
        return lookupLabId(labDirectoryName) != LabInfo.INVALID_LAB_ID;
    }

    /**
     * Returns the id of the lab whose data directories are stored under the
     * specified lab directory name. If the specified lab directory name is not
     * used by a known lab, returns INVALID_LAB_ID.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         due to a lower-level error.
     */
    public int lookupLabId(String labDirectoryName)
            throws OperationFailedException {
        synchronized (conn) {
            if (!labCache.hasEverLostItem()) {
                // We can answer the caller authoritatively with data from the
                // cache.

                for (LabInfo lab : labCache.getAll(LabInfo.class)) {
                    if (lab.directoryName.equals(labDirectoryName)) {
                        return lab.id;
                    }
                }
                
                return LabInfo.INVALID_LAB_ID;
            } else {
                // We must go to the database for an authoritative answer.
                LabInfo lab = dbFetchLabByDirectoryName(labDirectoryName);
                
                return (lab == null) ?  LabInfo.INVALID_LAB_ID : lab.id;
            }
        }
    }

    /**
     * Core-internal method that connects to a specified remote site and asks it
     * to replay inter-site messages eligible for delivery to the local site.
     * This method initiates only a single ISM exchange to the remote site;
     * thus, callers may wish to invoke this method repeatedly if there are many
     * ISM's to be pulled. The pulled messages are returned to the caller as raw
     * XML representations. The caller is responsible for decoding them,
     * validating them, and processing them appropriately.
     * 
     * @return an array of zero or more strings, where each is the XML
     *         representation of an ISM that was pulled from the remote site. In
     *         the case of an error on the remote side, this return value is
     *         null and this method writes an appropriate log message.
     * @param targetSiteId identifies the remote site to which the pull
     *        request(s) are sent.
     * @param siteIdsToAskFor zero or more site id's that identify the sites
     *        whose originated ISM's should be replayed.
     * @param messageLimit the maximum number of replayed ISM's to be pulled
     *        from the remote site during this exchange. In the event that the
     *        number of available messages exceeds this argument's value, the
     *        caller should invoke this function again to download the next
     *        batch.
     * @param rethrowException if this argument is true, then in the case of an
     *        error on the remote side this method throws an
     *        {@code OperationFailedException} instead of returning null.
     */
    public String[] pullIsmsFromRemoteSite(int targetSiteId,
            Collection<Integer> siteIdsToAskFor, int messageLimit,
            boolean rethrowException) throws InvalidKeyException,
            OperationFailedException, SignatureException {
        try {
            String[] messageStrings;
            
            // Prepare the pull requests (a bunch of XML strings).
            Collection<InterSiteMessage> requestIsms
                    = this.receivedMessageAgent.generatePullRequests(
                                targetSiteId, siteIdsToAskFor, messageLimit);
            String requestsAsXml[] = new String[requestIsms.size()];
            
            synchronized (this.sigEngine) {
                this.sigEngine.initSign(this.localPrivateKey);
                int i = 0;
                for (InterSiteMessage requestIsm : requestIsms) {
                    String xmlDoc = requestIsm.toXmlAddSignature(this.sigEngine);
                    requestsAsXml[i++] = SoapUtil.dropXmlDocumentHeader(xmlDoc,
                            "message");
                }
            }

            // Communicate with the remote site via SOAP.
            synchronized (this.ismExchanger) {
                messageStrings = this.ismExchanger.exchange(requestsAsXml,
                        getSiteInfo(targetSiteId).baseUrl, true);
                this.topologyAgent.notifyPull(targetSiteId, true,
                        messageStrings.length);
            }
            
            return messageStrings;
        } catch (OperationFailedException ex) {
            this.recordLogRecord(LogRecordGenerator.ismPullException(
                    targetSiteId,
                    this.ismExchanger.getLastServerErrorMessage(), ex));
            this.topologyAgent.notifyPull(targetSiteId, false, 0);
            if (rethrowException) {
                throw ex;
            } else {
                return null;
            }
        }
    }

    /**
     * Called by VersionUpdater during an update from 0.5.1 to 0.5.2. The
     * sent-messages directory is cleared, then all "missing" ISM's are
     * generated and possibly pushed across the network. Missing ISM's are
     * defined as those that would have been generated during an operation in
     * the past if the site had supported generating ISM's at that time. This
     * method should not be invoked for new installations (0.5.2 and higher)
     * since new installations have the logic necessary to generate the
     * appropriate ISM's as updates are made.
     * <p>
     * The initial ISM's fall into three categories: 1. A LabUpdateISM for each
     * lab hosted at the local site. The message is generated regardless of
     * whether the associated LabInfo record has been modified since site
     * initialization time because there is no good means to detect if the
     * LabInfo has not been modified. 2. A ProviderActivationISM for each
     * provider associated with a lab hosted at the local site.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void generateInitialIsms() throws OperationFailedException {
        // Clear the sent-messages directory. Also delete the received-
        // and held- messages directory since it won't hurt anything.
        messageFileAgent.deleteAll();

        // Generate a LabUpdateISM for each lab hosted at the local site.
        // Then generate potentially many ProviderActivationISM for each
        // provider within the lab.
        for (LabInfo currentLab : getAllLabInfo()) {
            if (currentLab.homeSiteId == localSiteId) {
                passCoreMessage(new SendIsmCM(
                        new LabUpdateISM(currentLab, localSiteId)));

                for (ProviderInfo currentProvider
                        : getAllProviderInfo(currentLab.id)) {
                    passCoreMessage(new SendIsmCM(new ProviderActivationISM(
                            currentProvider, localSiteId)));
                }
            }
        }
    }

    /**
     * Called by VersionUpdater during an init or update operation (while the
     * core is in bootstrap mode), this method initiates a push and then a pull
     * to every known site. Verbose status updates are written to the console.
     * 
     * @throws DeadlockDetectedException on core deadlock
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    public void synchronizeWithSiteNetwork() throws DeadlockDetectedException,
	    OperationFailedException {
        SiteNetworkSynchronizer synchronizer = new SiteNetworkSynchronizer(
		this, this.sampleManager, this.receivedMessageAgent, 
                this.ismExchanger, this.messageFileAgent, System.out);
        synchronizer.synchronize();
    }

    /**
     * Called only once, during the version update from recipnet-0.5.2 to
     * recipnet-0.5.3. Fixes all ProviderInfo records where the headContact or
     * comments field was an empty string (changes them to null); issues a
     * ProviderUpdateISM as appropriate.
     * 
     * @throws InvalidDataException
     * @throws InvalidModificationException
     * @throws OperationFailedException
     * @throws WrongSiteException
     */
    public void fixCorruptProviders() throws InvalidDataException,
            InvalidModificationException, OperationFailedException,
            WrongSiteException {
        for (int labId : getLocalLabs()) {
            for (ProviderInfo provider : getAllProviderInfo(labId)) {
                boolean update = false;
                
                if ("".equals(provider.headContact)) {
                    provider.headContact = null;
                    update = true;
                }
                if ("".equals(provider.comments)) {
                    provider.comments = null;
                    update = true;
                }
                if (update) {
                    writeUpdatedProviderInfo(provider);
                }
            }
        }
    }

    /**
     * One of the database rebuild operations that is invoked by VersionUpdater
     * during an upgrade to release 0.9.0 and every time the database is
     * "rebuilt", this method clears and repopulates the index of stored ISM
     * files that is maintained by {@code MessageFileAgent}.
     * 
     * @throws IllegalStateException if the core modules were not initialized
     *         for bootstrap mode.
     * @throws OperationFailedException with a nested {@code SQLException} if a
     *         low-level error was encountered while accessing the database.
     */
    public void rebuildIsmIndex() throws  OperationFailedException {
        if (!this.bootstrapMode) {
            throw new IllegalStateException();
        }
        this.messageFileAgent.rebuildIsmIndex();
    }

    /**
     * Used by core modules to write arbitrary LogRecords to the logging sub-
     * system.
     */
    public void recordLogRecord(LogRecord lr) {
        logger.log(lr);
    }

    public void recordLogRecord(LogRecord lr[]) {
        for (LogRecord rec : lr) {
            recordLogRecord(rec);
        }
    }

    /**
     * A helper function that will throw an exception if ISM generation has been
     * halted. This method should be called before any DB operation whose change
     * might result in an ISM generation.
     */
    public void assertIsmGenerationNotHalted() throws OperationFailedException {
        if (this.ismGenerationHalted) {
            throw new OperationFailedException(
                    new IsmGenerationHaltedException());
        }
    }

    /**
     * Fully-deactivates some site, either local or remote.  Generally this
     * method is useful following receipt of a SiteDeactivationISM; however,
     * in some cases site record deactivation may need to be delayed.  This
     * method encapsulates common code needed by both 
     * SiteManager.eventSiteDeactivate() and by ReceivedMessageAgent.
     *
     * This method may be invoked only from Site Manager's worker thread, i.e.
     * the same thread in which ISM processing occurs.
     */
    public void performFullSiteDeactivation(int siteId, long finalSeqNum)
            throws OperationFailedException {
	if (siteId == this.localSiteId) {
	    // Halt local ISM generation if we are deactivating the local site.
	    this.ismGenerationHalted = true;
	    this.recordLogRecord(LogRecordGenerator.siteDeactivated());
	    try {
		File errorFile = new File(
                        properties.getProperty("IsmSuicideNote"));
		if (!errorFile.exists()) {
		    errorFile.createNewFile();
		}
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                        new BufferedOutputStream(new FileOutputStream(
                        errorFile, true)), "UTF-8"), true);
	        RuntimeException ex = new RuntimeException("Local site" 
	                + " deactivated by Reciprocal Net Coordinator.");
		ex.printStackTrace(writer);
		writer.close();
	    } catch (IOException uee) {
		/*
		 * We can only hope that this doesn't happen.  If it does
		 * happen then we probably lose some or all of the
		 * information about why we got here.
		 */
	    } catch (RuntimeException re) {
		/*
		 * We can only hope that this doesn't happen.  If it does
		 * happen then we probably lose some or all of the
		 * information about why we got here.
		 */
	    }
	}

	// Update the site record.
	SiteInfo site = getSiteInfo(siteId);
	site.isActive = false;
	site.finalSeqNum = finalSeqNum;
	dbUpdateSite(site);
	siteCache.invalidate(site.id);

	// Notify other core modules and agents of the news.
	if (siteId != this.localSiteId) {
	    repositoryManager.passCoreMessage(
                    new InvalidateHoldingsCM(siteId));;
	}
	receivedMessageAgent.notifyFullSiteDeactivation(siteId);
	topologyAgent.notifySiteDeactivation(siteId);
    }

    /**
     * Called only once, during the version update from recipnet-0.9.0 to
     * recipnet-0.9.1.  Checks the local ISM sequence number table to ensure
     * that it shows our SiteGrantISM received by the Coordinator at bootstrap
     * time as having been processed.  (A bug in earlier Coordinator software
     * caused some errant SiteGrantISM's to be transmitted.)  If not, the local
     * sequence number table is updated.
     */
    public void markSiteGrantIsmAsProcessed() throws InvalidDataException, 
            OperationFailedException, ResourceNotFoundException {
	// Read this site's SiteGrantISM from the site grant file.
        File siteGrantFile = new File(properties.getProperty("SitGrantFile"));
	SiteGrantISM siteGrant
                    = MsgpakUtil.readAndDecodeSiteGrant(siteGrantFile);

	// Compare the sequence number on the ISM to the local sequence number
	// table.
	SiteInfo site = this.getSiteInfo(
                InterSiteMessage.RECIPROCAL_NET_COORDINATOR);
	if (site.privateSeqNum == InterSiteMessage.INVALID_SEQ_NUM
	        || site.privateSeqNum < siteGrant.sourceSeqNum) {
	    // The local sequence number table is out of date.  Update it.
	    site.privateSeqNum = siteGrant.sourceSeqNum;
	    this.writeUpdatedSiteInfo(site);
	}
    }

    /**
     * Similar in functionality to the original writeUpdatedLabInfo() method,
     * except this version is intended for core-internal use.  It is not
     * remotely accessible.  Validation is looser and no LabUpdateISM's are
     * generated.
     */
    public void writeUpdatedLabInfo2(LabInfo newLabInfo)
            throws InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            ResourceNotFoundException, WrongSiteException {
        // make sure there are no illegal characters
        if ((newLabInfo.name != null)
                && !labStringFieldValidator.isValid(newLabInfo.name)) {
            throw new InvalidDataException(newLabInfo,
                    InvalidDataException.ILLEGAL_NAME);
        }
        if ((newLabInfo.shortName != null)
                && !labStringFieldValidator.isValid(newLabInfo.shortName)) {
            throw new InvalidDataException(newLabInfo,
                    InvalidDataException.ILLEGAL_SHORT_NAME);
        }
        if ((newLabInfo.directoryName != null)
                && !labStringFieldValidator.isValid(newLabInfo.directoryName)) {
            throw new InvalidDataException(newLabInfo,
                    InvalidDataException.ILLEGAL_DIRECTORY_NAME);
        }
        if ((newLabInfo.homeUrl != null)
                && !labStringFieldValidator.isValid(newLabInfo.homeUrl)) {
            throw new InvalidDataException(newLabInfo,
                    InvalidDataException.ILLEGAL_URL);
        }
        if ((newLabInfo.defaultCopyrightNotice != null)
                && !labStringFieldValidator.isValid(
                        newLabInfo.defaultCopyrightNotice)) {
            throw new InvalidDataException(newLabInfo,
                    InvalidDataException.ILLEGAL_COPYRIGHT_NOTICE);
        }

	// check for proper locking.
	LabInfo oldLabInfo = this.getLabInfo(newLabInfo.id);
        if (oldLabInfo.ts != newLabInfo.ts) {
            throw new OptimisticLockingException(newLabInfo);
        }

        // The update is permissible. Proceed.
        newLabInfo.ts = serialNumber.get();
        dbUpdateLab(newLabInfo);
        labCache.put(newLabInfo.id, newLabInfo);
    }



    /**************************************************************************
     * INTERNAL HELPER METHODS (not remotely accessible)
     *************************************************************************/



    /**
     * Accepts a {@code CoreMessage} that is addressed to Site Manager,
     * processes it, and possibly updates Site Manager's internal state or
     * database tables. This is a dispatcher method: it delegates to other
     * methods named {@code event...()} depending upon the type of {@code msg}.
     * <p>
     * If {@code msg} is of type {@code InterSiteMessage} then at the completion
     * of message processing, this method generates a {@code ProcessedIsmCM} and
     * inserts it into Site Manager's message queue to record the outcome. Such
     * asynchronous notifications fit Site Manager's model of asynchronous ISM
     * processing.
     * <p>
     * In the case where one of the {@code event...()} methods this method
     * dispatches to throws an exception, the exception is logged in all cases.
     * If Site Manager is in bootstrap mode, this implies that this method was
     * invoked synchronously by a user-interactive thread, so the exception is
     * re-thrown by this method for eventual display to the user. If Site
     * Manager is not in bootstrap mode, then the failure is reported to
     * {@code ReceivedMessageAgent} (if {@code msg} is an
     * {@code InterSiteMessage}) and nothing more is done because this method
     * was probably invoked by a worker thread (rather than a user thread), and
     * has no mechanism other than the logfile for communicating with the user.
     * 
     * @param msg the core message to be processed.
     * @throws OperationFailedException only if Site Manager was initialized in
     *         bootstrap mode, and then only if an error was encountered while
     *         processing {@code msg}. The nested {@code Exception} will be the
     *         one thrown by whatever {@code event...()} method was dispatched
     *         to.
     */
    private void processCoreMessage(CoreMessage msg)
            throws OperationFailedException {
        try {
            if (msg instanceof PingCM) {
                eventPing((PingCM) msg);
            } else if (msg instanceof SendIsmCM) {
                eventSendIsm((SendIsmCM) msg);
            } else if (msg instanceof ProcessedIsmCM) {
                ProcessedIsmCM cm = (ProcessedIsmCM) msg;

                // ReceivedMessageAgent handles these messages for us directly.
                receivedMessageAgent.processProcessedIsmCM(cm);
            } else if (msg instanceof SiteGrantISM) {
                // Nothing to do for this sort of message. We process Site
                // Grants only at startup time.
                InterSiteMessage ism = (InterSiteMessage) msg;
                passCoreMessage(ProcessedIsmCM.success(ism,
                        "Site grant for site id " + ism.destSiteId
                                + " received; no action taken."));
            } else if (msg instanceof SiteActivationISM) {
                eventSiteActivate((SiteActivationISM) msg);
            } else if (msg instanceof SiteUpdateISM) {
                eventSiteUpdate((SiteUpdateISM) msg);
            } else if (msg instanceof SiteDeactivationISM) {
                eventSiteDeactivate((SiteDeactivationISM) msg);
            } else if (msg instanceof LabActivationISM) {
                eventLabActivate((LabActivationISM) msg);
            } else if (msg instanceof LabUpdateISM) {
                eventLabUpdate((LabUpdateISM) msg);
            } else if (msg instanceof ProviderActivationISM) {
                eventProviderActivate((ProviderActivationISM) msg);
            } else if (msg instanceof ProviderUpdateISM) {
                eventProviderUpdate((ProviderUpdateISM) msg);
            } else if (msg instanceof SiteResetISM) {
                eventSiteReset((SiteResetISM) msg);
            } else if (msg instanceof SiteStatisticsRequestISM) {
                statisticsAgent.processSiteStatisticsRequestISM(
                        (SiteStatisticsRequestISM) msg);
	    } else if (msg instanceof ForceUpgradeISM) {
		eventForceUpgrade((ForceUpgradeISM) msg);
            } else if (msg instanceof JoinISM) {
		eventJoin((JoinISM) msg);
	    } else if (msg instanceof LabTransferInitiateISM) {
		this.labTransferAgent.processLabTransferInitiateISM(
		        (LabTransferInitiateISM) msg);
	    } else if (msg instanceof LabTransferCompleteISM) {
		this.labTransferAgent.processLabTransferCompleteISM(
		        (LabTransferCompleteISM) msg);
	    } else if (msg instanceof LocalLabsChangedCM) {
		eventLocalLabsChanged((LocalLabsChangedCM) msg);
	    } else {
                // Unknown message type
                recordLogRecord(LogRecordGenerator.cmUnknownType(this, msg));
            }
        } catch (Exception ex) {
            // Generic catch-all for any sort of exception. This method is
            // called from a worker thread, not a user thread, so the only
            // means for us to report an exception is to log it.
            recordLogRecord(LogRecordGenerator.cmCausedException(this, msg, ex));

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
                passCoreMessage(ProcessedIsmCM.failure((InterSiteMessage) msg,
                        ex));
            } else {
                // Treat the exception as nonfatal by doing nothing more. The
                // exception was logged previously.
            }
        }
    }

    /**
     * Executed when a PingCM has been received from another one of the core
     * modules, or even from Site Manager itself.
     */
    private void eventPing(@SuppressWarnings("unused") PingCM msg) {
        pingSignal.send();
    }

    /**
     * Executed whenever a SendISMCM is received from one of the core modules
     * (including this one). Transmits the ISM encapsulated by this CM to its
     * intended recipient elsewhere on the Site Network. Also will redeliver the
     * ISM locally if its destination field includes the local site id.
     * 
     * @throws OperationFailedException only if Site Manager was initialized in
     *         bootstrap mode, and then only if the {@code ProcessedIsmCM} this
     *         message generates could not be processed for some reason.
     */
    private void eventSendIsm(SendIsmCM msg) throws OperationFailedException {
        InterSiteMessage ism = msg.ismToTransmit;
        String ismAsXml = null;

        // Sign the outgoing message and write it to file
        synchronized (sigEngine) {
            try {
                assertIsmGenerationNotHalted();
                sigEngine.initSign(localPrivateKey);

                // Compute the sequence numbers, sign the message, serialize it
                // to XML, and write the XML to file.
                synchronized (this.messageFileAgent) {
                    this.messageFileAgent.prepareSequenceNumbersForSentMessage(
                            ism);
                    ismAsXml = this.messageFileAgent.generateSentMessage(ism,
                            this.sigEngine);
                    // Verify that the ISM can be read again
                    this.sigEngine.initVerify(
                            getSiteInfo(this.localSiteId).publicKey);
                    this.messageFileAgent.assertSentMessageIsReadable(
                            this.localSiteId, ism.sourceSeqNum, this.sigEngine,
                            true);
                }
            } catch (Exception ex) {
                this.ismGenerationHalted = true;
                try {
                    File errorFile = new File(
                            properties.getProperty("IsmSuicideNote"));
                    PrintWriter writer;
                    
                    if (!errorFile.exists()) {
                        errorFile.createNewFile();
                    }
                    writer = new PrintWriter(new OutputStreamWriter(
                            new BufferedOutputStream(new FileOutputStream(
                                    errorFile, true)), "UTF-8"), true);
                    ex.printStackTrace(writer);
                    writer.write(ism.toXml());
                    writer.write(ismAsXml);
                    writer.close();
                } catch (IOException uee) {
                    /*
                     * We can only hope that this doesn't happen.  If it does
                     * happen then we probably lose some or all of the
                     * information about what went wrong with the ISM
                     */
                } catch (RuntimeException re) {
                    /*
                     * We can only hope that this doesn't happen.  If it does
                     * happen then we probably lose some or all of the
                     * information about what went wrong with the ISM
                     */
                }
                this.recordLogRecord(
                        LogRecordGenerator.ismGenerationException(ism, ex));
                
                return;
            }
        }

        // Send ourselves a ProcessedIsmCM so that our local state tables
        // will be updated -- we won't actually receive/process this message
        // because we can assume that the message has already been applied to
        // the local database.
        passCoreMessage(ProcessedIsmCM.fromLocal(ism));

        // If ISM pushing is enabled, pass this ISM off to the pushing
        // subsystem.
        if (this.ismPushAgent != null) {
            this.ismPushAgent.notifyNewIsm(ism, ismAsXml);
        }
    }

    /**
     * Executed when a SiteActivationISM has arrived from another site and
     * needs to be processed. This sort of message is valid only if it
     * originated from the Coordinator.
     * 
     * There is graceful handling for the case where a site record for the
     * specified site id already exists.  In such a case, the existing site
     * record merely is updated.  This behavior was introduced in the 0.9.1
     * codebase and is necessary to enable recovery from some inconsistent
     * state in the Site Network caused by a previous bug in 
     * eventSiteDeactivate().  See bug #1596 for more information.
     *
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventSiteActivate(SiteActivationISM msg)
            throws IsmProcessingException, OperationFailedException {
        if (!msg.isFromCoordinator()) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }
	try {
	    // First try to update an existing site record, if any exists.
	    eventSiteUpdate2(msg.newSite, true);
	    passCoreMessage(ProcessedIsmCM.success(msg, "Received activation"
                + " but recorded update of site id " + msg.newSite.id + "."));
	} catch (ResourceNotFoundException ex) {
	    // No site record already exists, so create a new one.
	    msg.newSite.ts = serialNumber.get();
	    dbAddSite(msg.newSite);
	    siteCache.put(msg.newSite.id, msg.newSite);
	    receivedMessageAgent.notifySiteActivation(msg.newSite);
	    topologyAgent.notifySiteActivation(msg.newSite);
	    passCoreMessage(ProcessedIsmCM.success(msg, "Recorded activation"
                    + " of site id " + msg.newSite.id + "."));
	}
    }

    /**
     * Executed when a SiteUpdateISM has arrived from another site and needs to
     * be processed. This sort of message is valid only if it originated from
     * the Coordinator.
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventSiteUpdate(SiteUpdateISM msg)
            throws IsmProcessingException, OperationFailedException {
        if (!msg.isFromCoordinator()) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }
	eventSiteUpdate2(msg.updatedSite, false);
        passCoreMessage(ProcessedIsmCM.success(msg, "Recorded update"
                + " to site id " + msg.updatedSite.id + "."));
    }

    /**
     * A helper function to eventSiteUpdate() that performs most of that
     * function's work.  The same logic is useful to one branch of 
     * eventSiteActivate()'s processing.
     * @throws ResourceNotFoundException if no site record for the specified
     *     site id was found.
     */
    private void eventSiteUpdate2(SiteInfo updatedSite, 
            boolean shouldForceActiveToTrue) throws OperationFailedException {
        // Fetch our existing SiteInfo record and merge it with the one the
        // coordinator sent us. We need to preserve our ISM sequence
        // numbers, database timestamps, and other important local fields.
        SiteInfo oldSite = getSiteInfo(updatedSite.id);
        SiteInfo newSite = updatedSite.clone();
        newSite.ts = serialNumber.get();
	newSite.isActive = shouldForceActiveToTrue ? true : oldSite.isActive;
        newSite.publicSeqNum = oldSite.publicSeqNum;
        newSite.privateSeqNum = oldSite.privateSeqNum;
	newSite.finalSeqNum = oldSite.finalSeqNum;

        // Write the updated site record to the database, then the cache
        dbUpdateSite(newSite);
        siteCache.put(newSite.id, newSite);

        topologyAgent.notifySiteUpdate(newSite);
    }

    /**
     * Executed when a SiteDeactivationISM has arrived from another site and
     * needs to be processed. This sort of message is valid only if it
     * originated from the Coordinator.
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventSiteDeactivate(SiteDeactivationISM msg)
            throws IsmProcessingException, OperationFailedException {
	// Validation.
        if (!msg.isFromCoordinator()) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

	SiteInfo site = getSiteInfo(msg.oldSiteId);
	if (msg.finalSeqNum != InterSiteMessage.INVALID_SEQ_NUM
	        && msg.finalSeqNum > site.publicSeqNum
 	        && msg.finalSeqNum > site.privateSeqNum) {
	    // Begin the site-deactivation process.  We cannot fully deactivate
	    // the site at this moment because there are some unprocessed ISM's
	    // remaining.  Final deactivation will be accomplished later, by
	    // ReceivedMessageAgent.processProcessedIsmCM();
	    site.finalSeqNum = msg.finalSeqNum;
	    dbUpdateSite(site);
	    siteCache.invalidate(site.id);
	    receivedMessageAgent.notifyPartialSiteDeactivation(msg.oldSiteId,
	            msg.finalSeqNum);
            passCoreMessage(ProcessedIsmCM.success(msg, "Recorded partial"
		    + " deactivation of site id " + msg.oldSiteId + "."));
	} else {
	    // Proceed with a full deactivation.  There are no unprocessed
	    // ISM's remaining.
	    this.performFullSiteDeactivation(msg.oldSiteId, msg.finalSeqNum);
            passCoreMessage(ProcessedIsmCM.success(msg, "Recorded full"
		    + " deactivation of site id " + msg.oldSiteId + "."));
	}
    }

    /**
     * Executed when a LabActivationISM has arrived from another site and needs
     * to be processed. This sort of message is valid only if it originated from
     * the Coordinator.
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private void eventLabActivate(LabActivationISM msg)
            throws IsmProcessingException, OperationFailedException {
        if (msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        msg.newLab.ts = serialNumber.get();
        dbAddLab(msg.newLab);
        labCache.put(msg.newLab.id, msg.newLab);

        // If the list of labs hosted at the local site has changed, we
        // need to inform the other two core modules.
        if (msg.newLab.homeSiteId == localSiteId) {
	    this.passCoreMessage(new LocalLabsChangedCM());
            sampleManager.passCoreMessage(new LocalLabsChangedCM());
            repositoryManager.passCoreMessage(new LocalLabsChangedCM());
        }

        passCoreMessage(ProcessedIsmCM.success(msg, "Recorded activation"
                + " of lab id " + msg.newLab.id + "."));
    }

    /**
     * Executed when a LabUpdateISM has arrived from another site and needs to
     * be processed. This sort of message is valid if it originated from the
     * Coordinator, or from the site where the lab is currently registered,
     * (according to our records).
     *
     * The homeSiteId field of lab records is not generally changable via
     * LabUpdateISM's.  There is one exception, however.  For legacy purposes,
     * the Coordinator may set this field via LabUpdateISM's.
     *
     * Therefore, any existing homeSiteId field in the database is preserved.
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator or the lab's present home
     *         site.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws UnexpectedExceptionException
     */
    private void eventLabUpdate(LabUpdateISM msg)
            throws IsmProcessingException, OperationFailedException {
        LabInfo oldLab = getLabInfo(msg.updatedLab.id);
        if (!msg.isFromCoordinator()
	        && !msg.isFrom(oldLab.homeSiteId)) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }
	if (!msg.isFromCoordinator()
	        && msg.updatedLab.homeSiteId != oldLab.homeSiteId) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);	    
	}

        // FIXME: perhaps this function should return false if the specified
        // lab does not already exist.
        msg.updatedLab.ts = serialNumber.get();
        dbUpdateLab(msg.updatedLab);
        labCache.put(msg.updatedLab.id, msg.updatedLab);

        passCoreMessage(ProcessedIsmCM.success(msg, "Recorded update"
                + " to lab id " + msg.updatedLab.id + "."));
    }

    /**
     * Executed when a ProviderActivationISM has arrived from another site and
     * needs to be processed. This sort of message is valid if it originated
     * from the Coordinator or from the site where the provider's associated
     * lab is currently hosted (according to our records).
     *
     * There is graceful handling for the case where a provider record for the
     * specified provider id already exists.  In such a case, the existing 
     * provider record merely is updated.  This behavior was introduced in the
     * 0.9.1 codebase and is necessary to enable recovery from some
     * inconsistent state in the Site Network.  See bug #1910 for more
     * information.
     * 
     * @throws IsmProcessingException with the <msg>msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator or the associated lab's
     *         present home site.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws UnexpectedExceptionException
     */
    private void eventProviderActivate(ProviderActivationISM msg)
            throws IsmProcessingException, OperationFailedException {
        LabInfo owningLab = getLabInfo(msg.newProvider.labId);
        if (msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR
                && msg.sourceSiteId != owningLab.homeSiteId) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

	try {
	    // First try to update an existing provider record, if any exists.
	    ProviderInfo oldProvider = getProviderInfo(msg.newProvider.id);
	    if (oldProvider.labId != msg.newProvider.labId) {
		// One site might be trying to overwrite another site's
		// provider records.  Don't let them succeed.
		throw new IsmProcessingException(msg,
                        IsmProcessingException.SENDER_NOT_AUTHORIZED);
	    }
	    msg.newProvider.ts = serialNumber.get();
	    dbUpdateProvider(msg.newProvider);
	    providerCache.put(msg.newProvider.id, msg.newProvider);
	    passCoreMessage(ProcessedIsmCM.success(msg, "Received activation"
                    + " but recorded update of provider id " 
                    + msg.newProvider.id + "."));
	} catch (ResourceNotFoundException ex) {
	    // No provider record already exists, so create a new one.
	    msg.newProvider.ts = serialNumber.get();
	    dbAddProvider(msg.newProvider);
	    providerCache.put(msg.newProvider.id, msg.newProvider);
	    passCoreMessage(ProcessedIsmCM.success(msg, "Recorded activation"
                    + " of provider id " + msg.newProvider.id + "."));
	}
    }

    /**
     * Executed when a ProviderUpdateISM has arrived from another site and needs
     * to be processed. This sort of message is valid if it originated from the
     * Coordinator or from the site where the provider's associated lab is
     * currently hosted (according to our records).
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator or the associated lab's
     *         present home site.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws UnexpectedExceptionException
     */
    private void eventProviderUpdate(ProviderUpdateISM msg)
            throws IsmProcessingException, OperationFailedException {
        ProviderInfo oldProvider = getProviderInfo(msg.updatedProvider.id);
        LabInfo owningLab = getLabInfo(oldProvider.labId);

        if ((msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR)
                && (msg.sourceSiteId != owningLab.homeSiteId)) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        // TODO: perhaps this function should return false if the specified
        // provider does not already exist.
        msg.updatedProvider.ts = serialNumber.get();
        dbUpdateProvider(msg.updatedProvider);
        providerCache.put(msg.updatedProvider.id, msg.updatedProvider);

        passCoreMessage(ProcessedIsmCM.success(msg, "Recorded update"
                + " to provider id " + msg.updatedProvider.id + "."));
    }

    /**
     * Executed when a SiteResetISM has arrived from another site and needs to
     * be processed. This sort of message is valid only if it originated from
     * the Coordinator.
     * 
     * @throws IsmProcessingException with the {@code msg} embedded and a
     *         {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *         not originate from the Coordinator.
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws ResourceNotFoundException if the site id specified within the ISM
     *         does not represent a known site.
     */
    private void eventSiteReset(SiteResetISM msg)
            throws IsmProcessingException, OperationFailedException,
            ResourceNotFoundException {
        if (msg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR) {
            throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        SiteInfo site = getSiteInfo(msg.otherSiteId);

        if (msg.publicSeqNum != SiteResetISM.DONT_RESET) {
            site.publicSeqNum = msg.publicSeqNum;
        }
        if (msg.privateSeqNum != SiteResetISM.DONT_RESET) {
            site.privateSeqNum = msg.privateSeqNum;
        }

        // Write the updated SiteInfo back to the db
        site.ts = serialNumber.get();
        dbUpdateSite(site);
        siteCache.put(site.id, site);

        receivedMessageAgent.notifySiteReset(site);

        passCoreMessage(ProcessedIsmCM.success(msg, "Recorded seqnum reset"
                + " for site id " + msg.otherSiteId + "."));
    }

    /**
     * Executed when a ForceUpgradeISM has arrived from another site and needs
     * to be processed.  This sort of message is valid only if it originated
     * from the Coordinator.
     *
     * @throws IsmProcessingException with the {@code msg} embedded and a 
     *     {@code reason} of {@code SENDER_NOT_AUTHORIZED} if the ISM did
     *     not originate from the Coordinator.
     */
    private void eventForceUpgrade(ForceUpgradeISM msg) 
	throws IsmProcessingException, OperationFailedException {
	if (!msg.isFromCoordinator()) {
	    throw new IsmProcessingException(msg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
	}

	String localVersion = dbFetchVersion();
	if (msg.checkVersion(localVersion)) {
	    // The local site's software is up-to-date.  Process this ISM.
	    passCoreMessage(ProcessedIsmCM.success(msg, "Remote version check"
	            + " suceeded: " + localVersion + " >= " + msg.version));
	} else {
	    // The local site's software is to old.  Fail to process this ISM.
	    passCoreMessage(ProcessedIsmCM.failure(msg, "Remote version check"
	            + " failed: " + localVersion + " < " + msg.version 
                    + ".  Newer Reciprocal Net software is available.  Please"
                    + " upgrade this site's software so that synchronization"
		    + " can continue."));
	}
    }

    /**
     * Executed when a JoinISM has arrived from another site and needs to be
     * processed.
     */
    private void eventJoin(JoinISM msg) throws IsmProcessingException,
            OperationFailedException {
	try {
	    SiteInfo site = this.getSiteInfo(msg.joinedSiteId);
	    if (site.publicSeqNum >= msg.joinedSiteSeqNum) {
		// The local site has processed the joined ISM already.
		// Process this JoinISM.
		this.passCoreMessage(ProcessedIsmCM.success(msg, "ISM join"
                        + " from site id " + msg.sourceSiteId + " succeeded: " 
                        + site.publicSeqNum + " >= " + msg.joinedSiteSeqNum
		        + " for joined site id " + msg.joinedSiteId));
	    } else {
		// The local site has not processed the joined ISM yet.  Fail
		// to process this JoinISM.
		this.passCoreMessage(ProcessedIsmCM.failure(msg, "ISM join"
                        + " from site id " + msg.sourceSiteId + " failed: " 
                        + site.publicSeqNum + " < " + msg.joinedSiteSeqNum
		        + " for joined site id " + msg.joinedSiteId));
	    }
	} catch (ResourceNotFoundException ex) {
	    // The joinedSiteId is not known.  Fail to process this ISM.
	    this.passCoreMessage(ProcessedIsmCM.failure(msg, "ISM join from"
                    + " site id " + msg.sourceSiteId + " failed: joined site"
		    + " id " + msg.joinedSiteId + " not known"));
	}
    }

    /**
     * Executed when a LocalLabsChangedCM has arrived from either this module
     * or from LabTransferAgent to indicate that the set of labs hosted at the
     * local site has changed.
     */
    private void eventLocalLabsChanged(LocalLabsChangedCM msg)
            throws OperationFailedException {
	this.loadLocalTrackingConfig();
    }

    /**
     * Periodic task invoked every now and then by the worker thread, as
     * controlled by configuration directive SiteReplayRequestTask. Generates
     * and sends ISM "replay requests" or "pulls" to all replication partners of
     * the local site. Decisions about replication topology are delegated to the
     * {@code TopologyAgent}.
     * <p>
     * This is the backup mechanism for the normal "push" way of sending ISM's --
     * ISM's might be received this way if the local site happened to miss an
     * earlier push from the sending site because it was down.
     */
    private void doReplayRequestTask() {
        int configuredReplayLimit = Integer.parseInt(
                this.properties.getProperty("SitSentIsmReplayLimit"));
        
        try {
            // Iterate through each site the Topology Agent thinks we should
            // pull from.
            for (Map.Entry<Integer, Collection<Integer>> entry
                    : this.topologyAgent.suggestSitesToPullFrom().entrySet()) {
                String messageStrings[];
                
                do {
                    messageStrings = pullIsmsFromRemoteSite(
                            entry.getKey(), entry.getValue(),
                            configuredReplayLimit, false);

                    // Take the messages we received, decode and validate them,
                    // and insert them into the message queue for processing.
                    if (messageStrings != null) {
                        this.receivedMessageAgent.exchangeInterSiteMessages(
                                messageStrings, "site id " + entry.getKey()
                                        + " (pull)", false, true);
                    }
                } while ((this.receivedMessageAgent.getHintedAvailableMessageCount(
                        entry.getKey()) > 0) && (messageStrings != null));
            }
        } catch (Exception ex) {
            // This is just a general catch-all, to guarantee that any
            // exceptions get recorded somewhere
            recordLogRecord(LogRecordGenerator.ismReplayRequestException(ex));
        }
    }

    /**
     * Periodic task invoked every now and then by the worker thread, as
     * configured by the configuration directive 'SitRedeliverHeldMsgsTask'.
     * Reads every ISM from the msgs-held directory and passes it to the message
     * queue again - the ISM's themselves will be processed or dropped
     * asynchronously. Also deletes very old held-message files, as controlled
     * by the configuration directive 'SitMsgsHoldTime'.
     */
    private void doRedeliverHeldMsgsTask() {
        try {
            receivedMessageAgent.periodicCheck();
        } catch (Exception ex) {
            // This is just a general catch-all, to guarantee that any
            // exceptions get recorded somewhere
            recordLogRecord(LogRecordGenerator.ismRedeliveryException(ex));
        }
    }

    /**
     * Internal function that generates a globally unique id for a new provider
     * record that belongs to a lab hosted at this site. The generated id
     * incorporates 15 bits of the local site's id number but is random beyond
     * that. (Site id's cannot exceed 32767, so the 15-bit restriction here is
     * not a problem.)
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private int generateProviderId() throws OperationFailedException {
        int id;

        /*
         * We need to serialize access to this id-generation function because
         * otherwise it's conceivable that the same id number might be returned
         * twice.
         */
        synchronized (randomIdGenerator) {
            do {
                // Come up with a 16-bit random number
                int temp = randomIdGenerator.nextInt(0x10000);

                // Commingle the 16-bit random number with the local site's
                // 15-bit site id in some reproducible way
                id = ((this.localSiteId << 16) + temp) ^ 0x55555555;

            // Ensure that the generated id is not already in use
            } while (dbFetchProvider(id) != null);
        }
        
        return id;
    }

    /**
     * Internal utility function that returns true if the specified provider id
     * number belongs to the specified site, according to convention.
     */
    private boolean verifyProviderId(int providerId, int siteId) {
        return (((providerId ^ 0x55555555) >> 16) == siteId);
    }

    /**
     * Internal function that generates a globally unique id for a new user
     * record that belongs to this site. The generated id incorporates 15 bits
     * of the local site's id number but is random beyond that.
     * 
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    private int generateUserId() throws OperationFailedException {
        int id;

        /*
         * We need to serialize access to this id-generation function because
         * otherwise it's conceivable that the same id number might be returned
         * twice.
         */
        synchronized (randomIdGenerator) {
            do {
                // Come up with a 16-bit random number
                int temp = randomIdGenerator.nextInt(0x10000);

                // Commingle the 16-bit random number with the local site's
                // 15-bit site id in some reproducible way
                id = ((this.localSiteId << 16) + temp) ^ 0x55555555;

            // Ensure that the generated id is not already in use
            } while (dbFetchUser(id) != null);
        }
        
        return id;
    }

    /**
     * Internal utility function that returns true if the specified user id
     * number belongs to the specified site, according to convention.
     */
    private boolean verifyUserId(int userId, int siteId) {
        return (((userId ^ 0x55555555) >> 16) == siteId);
    }

    /**
     * @throws InvalidDataException if any of the data strings have characters
     *         that are invalid for a container object. The possible reason
     *         codes are {@code ILLEGAL_NAME}, {@code ILLEGAL_SHORT_NAME},
     *         {@code ILLEGAL_BASE_URL}, and {@code ILLEGAL_REPOSITORY_URL}
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws OptimisticLockingException if the update could not be completed
     *         because a possibly conflicting update to the same site record was
     *         made recently.
     * @throws UnexpectedExceptionException
     */
    public void writeUpdatedSiteInfo(SiteInfo newSite)
            throws InvalidDataException, OperationFailedException,
            OptimisticLockingException {
        SiteInfo oldSite = getSiteInfo(newSite.id);

        // make sure there are no illegal characters
        if ((newSite.name != null)
                && !siteStringFieldValidator.isValid(newSite.name)) {
            throw new InvalidDataException(newSite,
                    InvalidDataException.ILLEGAL_NAME);
        }
        if ((newSite.shortName != null)
                && !siteStringFieldValidator.isValid(newSite.shortName)) {
            throw new InvalidDataException(newSite,
                    InvalidDataException.ILLEGAL_SHORT_NAME);
        }
        if ((newSite.baseUrl != null)
                && !siteStringFieldValidator.isValid(newSite.baseUrl)) {
            throw new InvalidDataException(newSite,
                    InvalidDataException.ILLEGAL_BASE_URL);
        }
        if ((newSite.repositoryUrl != null)
                && !siteStringFieldValidator.isValid(newSite.repositoryUrl)) {
            throw new InvalidDataException(newSite,
                    InvalidDataException.ILLEGAL_REPOSITORY_URL);
        }

        // FIXME: compare the newSite and oldSite objects to see whether this
        // update should be permitted.

        if (oldSite.ts != newSite.ts) {
            throw new OptimisticLockingException(newSite);
        }

        // The update is permissible. Proceed.
        newSite.ts = serialNumber.get();
        dbUpdateSite(newSite);
        siteCache.put(newSite.id, newSite);
    }


    /**
     * Internal method that repopulates the localTrackingConfigMap
     * structure based upon the contents of the configuration file.  This is
     * generally invoked at startup time, and may be re-invoked whenever the
     * set of local labs may change.
     */
    private void loadLocalTrackingConfig() throws OperationFailedException {
	synchronized (this.localTrackingConfigMap) {
	    this.localTrackingConfigMap.clear();
	    for (int labId : this.getLocalLabs()) {
		LocalTrackingConfig ltc = new LocalTrackingConfig();
		ltc.labId = labId;
		this.localTrackingConfigMap.put(labId, ltc);
	    }
	    for (Enumeration<?> e = properties.propertyNames();
                    e.hasMoreElements(); ) {
		String directive = (String) e.nextElement();
		if (directive.startsWith("SitLocalField")) {
		    try {
			int textType = Integer.parseInt(
                                directive.substring(13))
                                + SampleTextBL.LOCAL_TRACKING_BASE;
			String configStr = properties.getProperty(directive);
			int labId = LocalTrackingConfig.Field.parseLabId(
                                configStr);
			LocalTrackingConfig ltc 
                                = localTrackingConfigMap.get(labId);
			if (ltc == null) {
			    recordLogRecord(
                                    LogRecordGenerator.configInvalidLocalLab(
				    labId));
			    throw new OperationFailedException();
			}
			ltc.addField(textType, configStr);
		    } catch (NumberFormatException ex) {
			recordLogRecord(
                                LogRecordGenerator.configParseException(
                                directive, ex));
			throw new OperationFailedException(ex);
		    } catch (NoSuchElementException ex) {
			recordLogRecord(
                                LogRecordGenerator.configParseException(
			        directive, ex));
			throw new OperationFailedException(ex);
		    }
		}
	    }
	}
    }

    /**
     * Internal function used to fetch and populate a single SiteInfo object
     * from the database. This method does not interface with the cache -- it
     * should be called in case of a cache miss.
     * 
     * @throws OperationFailedException on database error.
     */
    private SiteInfo dbFetchSite(int id) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM sites WHERE id=" + id + ";");

                    return (rs.next() ? new SiteInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch and populate a single LabInfo object from
     * the database. This method does not interface with the cache -- it should
     * be called in case of a cache miss.
     * 
     * @throws OperationFailedException on database error.
     */
    private LabInfo dbFetchLab(int id) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM labs WHERE id=" + id + ";");

                    return (rs.next() ? new LabInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch and populate a single LabInfo object from
     * the database. This method does not interface with the cache -- it should
     * be called in case of a cache miss.
     * 
     * @throws OperationFailedException on database error.
     */
    private LabInfo dbFetchLabByDirectoryName(String directoryName)
            throws OperationFailedException {
        try {
            synchronized (conn) {
                // Use a PreparedStatement instead of a normal statement so
                // that the caller's directoryName string will be escaped.
                PreparedStatement cmd = conn.prepareStatement(
                        "SELECT * FROM labs WHERE directoryName=?;");
                
                try {
                    cmd.setString(1, directoryName);
                    ResultSet rs = cmd.executeQuery();
    
                    return (rs.next() ? new LabInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch and populate a single ProviderInfo object
     * from the database. This method does not interface with the cache -- it
     * should be called in case of a cache miss.
     * 
     * @throws OperationFailedException on database error.
     */
    private ProviderInfo dbFetchProvider(int id)
            throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM providers WHERE id=" + id + ";");

                    return (rs.next() ? new ProviderInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch and populate a single UserInfo object
     * from the database. This method does not interface with the cache -- it
     * should be called in case of a cache miss.
     * 
     * @throws OperationFailedException on database error.
     */
    private UserInfo dbFetchUser(int id) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM users WHERE id=" + id + ";");
    
                    return (rs.next() ? new UserInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch and populate a single UserInfo object
     * from the database. This method does not interface with the cache -- it
     * should be called in case of a cache miss. Will not return a UserInfo
     * object if isActive=false. This is to support multiple user records having
     * the same username, so long as only one of those user records is active at
     * a time.
     * 
     * @throws OperationFailedException on database error.
     */
    private UserInfo dbFetchUser(String username)
            throws OperationFailedException {
        try {
            synchronized (conn) {
                // Use a PreparedStatement instead of a normal statement so
                // that the caller's username string will be escaped.
                PreparedStatement cmd = conn.prepareStatement(
                        "SELECT * FROM users WHERE username=? AND active=1;");
                
                try {
                    cmd.setString(1, username);
                    ResultSet rs = cmd.executeQuery();

                    return (rs.next() ? new UserInfo(rs) : null);
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch the contents of the 'version' table in
     * the database.  This is simply a string that identifies the software
     * build presently installed on the local site.
     */
    private String dbFetchVersion() throws OperationFailedException {
	try {
	    synchronized (conn) {
		return VersionUpdater.getCurrentVersion(conn);
	    }
	} catch (SQLException ex) {
	    throw new OperationFailedException(ex);
	}
    }

    /**
     * Internal function used to fetch all SiteInfo objects from the database
     * and preload the cache. So long as the cache size is larger than the
     * number of sites, this will allow us to have a 100% cache hit rate from
     * now on.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbPreloadSites() throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM sites;");

                    // Create one new SiteInfo for every row in the sites table;
                    // store the object in the cache
                    while (rs.next()) {
                        SiteInfo site = new SiteInfo(rs);
                        siteCache.put(site.id, site);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch all LabInfo objects from the database and
     * preload the cache. So long as the cache size is larger than the number of
     * labs, this will allow us to have a 100% cache hit rate from now on.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbPreloadLabs() throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM labs;");

                    // Create one new LabInfo for every row in the labs table;
                    // store the object in the cache
                    while (rs.next()) {
                        LabInfo lab = new LabInfo(rs);
                        labCache.put(lab.id, lab);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch all ProviderInfo objects from the
     * database and preload the cache. So long as the cache size is larger than
     * the number of providers, this will allow us to have a 100% cache hit rate
     * from now on.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbPreloadProviders() throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM providers;");

                    // Create one new ProviderInfo for every row in the
                    // providers table; store the object in the cache
                    while (rs.next()) {
                        ProviderInfo provider = new ProviderInfo(rs);
                        providerCache.put(provider.id, provider);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to fetch all UserInfo objects from the database
     * and preload the cache. So long as the cache size is larger than the
     * number of users, this will allow us to have a 100% cache hit rate from
     * now on.
     * 
     * @throws OperationFailedException ond database error.
     */
    private void dbPreloadUsers() throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement();
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM users;");

                    // Create one new UserInfo for every row in the users table;
                    // store the object in the cache. Index active users by
                    // username and userid and inactive users by userid alone.
                    while (rs.next()) {
                        UserInfo user = new UserInfo(rs);
                        userCache.put(user.id, (user.isActive ? user.username
                                : null), user);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal functio used to add a new site to the database. This method is
     * not aware of the cache; the caller must update it. The site.id field must
     * already be set.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbAddSite(SiteInfo site) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM sites;");
                    
                    rs.moveToInsertRow();
                    site.dbStore(rs);
                    rs.insertRow();
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to add a new lab to the database. This method is
     * not aware of the cache; the caller must update it. The lab.id field must
     * already be set.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbAddLab(LabInfo lab) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM labs;");
                    
                    rs.moveToInsertRow();
                    lab.dbStore(rs);
                    rs.insertRow();
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to add a new provider to the database. This method
     * is not aware of the cache; the caller must update it. The provider.id
     * field must already be set.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbAddProvider(ProviderInfo provider)
            throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM providers;");

                    rs.moveToInsertRow();
                    provider.dbStore(rs);
                    rs.insertRow();
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to add a new user to the database. This method is
     * not aware of the cache; the caller must update it. The user.id field must
     * already be set.
     * 
     * @throws OperationFailedException on database error.
     */
    private void dbAddUser(UserInfo user) throws OperationFailedException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM users;");
                    
                    rs.moveToInsertRow();
                    user.dbStore(rs);
                    rs.insertRow();
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to update an existing site in the database. This
     * method is not aware of the cache; the caller must update it.
     * 
     * @throws OperationFailedException on database error.
     * @throws ResourceNotFoundException if an existing version of the site
     *         record to be updated could not be found.
     */
    private void dbUpdateSite(SiteInfo site) throws OperationFailedException,
            ResourceNotFoundException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM sites WHERE id=" + site.id + ";");
                    
                    if (rs.next()) {
                        site.dbStore(rs);
                        rs.updateRow();
                    } else {
                        throw new ResourceNotFoundException(site);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to update an existing lab in the database. This
     * method is not aware of the cache; the caller must update it.
     * 
     * @throws OperationFailedException on database error.
     * @throws ResourceNotFoundException if an existing version of the lab
     *         record to be updated could not be found.
     */
    private void dbUpdateLab(LabInfo lab) throws OperationFailedException,
            ResourceNotFoundException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM labs WHERE id=" + lab.id + ";");
                    
                    if (rs.next()) {
                        lab.dbStore(rs);
                        rs.updateRow();
                    } else {
                        throw new ResourceNotFoundException(lab);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to update an existing provider in the database.
     * This method is not aware of the cache; the caller must update it.
     * 
     * @throws OperationFailedException on database error.
     * @throws ResourceNotFoundException if an existing version of the provider
     *         record to be updated could not be found.
     */
    private void dbUpdateProvider(ProviderInfo provider)
            throws OperationFailedException, ResourceNotFoundException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery("SELECT * FROM providers"
                            + " WHERE id=" + provider.id + ";");
                    
                    if (rs.next()) {
                        provider.dbStore(rs);
                        rs.updateRow();
                    } else {
                        throw new ResourceNotFoundException(provider);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Internal function used to update an existing user in the database. This
     * method is not aware of the cache; the caller must update it.
     * 
     * @throws OperationFailedException on database error.
     * @throws ResourceNotFoundException if an existing version of the user
     *         record to be updated could not be found.
     */
    private void dbUpdateUser(UserInfo user) throws OperationFailedException,
            ResourceNotFoundException {
        try {
            synchronized (conn) {
                Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                
                try {
                    ResultSet rs = cmd.executeQuery(
                            "SELECT * FROM users WHERE id=" + user.id + ";");
                    
                    if (rs.next()) {
                        user.dbStore(rs);
                        rs.updateRow();
                    } else {
                        throw new ResourceNotFoundException(user);
                    }
                } finally {
                    cmd.close();
                }
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }
    }
}
