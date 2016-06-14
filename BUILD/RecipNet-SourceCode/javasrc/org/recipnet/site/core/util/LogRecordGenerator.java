/*
 * Reciprocal Net project
 * 
 * LogRecordGenerator.java
 *
 * 15-Apr-2003: midurbin wrote first draft
 * 12-May-2003: midurbin fixed bug #901 in ismPullServiced()
 * 23-May-2003: ekoperda added processTermination()
 * 27-May-2003: ekoperda added lockExceptionOnRevoke()
 * 29-May-2003: ekoperda added sampleLockPeriodicReleaseException(),
 *              repositoryAgentStartupException(), repositoryScanException(),
 *              repositoryScanFinished(), secondaryDirectoryDeletionException()
 *              and primaryDirectoryCommitException(), 
 * 10-Jun-2003: ekoperda changed spec of ismPushed() and ismPushFailed() to
 *              accommodate new class IsmPushAgent
 * 08-Jul-2003: ekoperda added lockSchedulerTimeout() and 
 *              lockAcquisitionTimeout()
 * 10-Jul-2003: midurbin added statisticsAgentFailedToCreate(),
 *              siteStatsGenerationException(), labStatsGenerationException()
 *              statsDbWriteBehind(), statsDbWriteBehindException()
 * 28-Jul-2003: nsanghvi daemonStartupSignalException()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 07-May-2004: cwestnea added rmiRegistryStartupException()
 * 01-Aug-2005: ekoperda renamed ismReceiveSummary() to ismExchangeSummary(),
 *              changed its spec, changed the specs of ismReceiveException() 
 *              and ismPushed(), and removed ismExceptionWhileAcceptingPush(),
 *              ismPullServiced() and ismPullServiceException()
 * 12-May-2006: jobollin reformatted the source and updated docs
 * 06-Jan-2008: ekoperda added topologySiteShunned()
 * 12-Jun-2008: ekoperda added sampleIdsExhaustedAtStartup()
 * 26-Nov-2008: ekoperda added ismDeactivationDelayed, siteDeactivated() and 
 *              repositoryHoldingsInvalidated(), and changed ismExchangeSummary
 * 02-Jan-2009: ekoperda added labTransferArrived(), labTransferDeparted() and
 *              labTransferAgentFailedToCreate()
 */

package org.recipnet.site.core.util;

import java.io.File;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DeadlockDetectedException;
import org.recipnet.site.core.MessageDecodingException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourcesExhaustedException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.agent.IsmPushAgent;
import org.recipnet.site.core.agent.ReceivedMessageAgent;
import org.recipnet.site.core.lock.AbstractLock;
import org.recipnet.site.core.msg.CoreMessage;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.shared.db.SampleIdBlock;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * LogRecordGenerator contains every message that core would write to the 
 * system log.  These messages are formatted and encapuslated by various 
 * function calls that all return {@code LogRecord} objects.   
 */
public abstract class LogRecordGenerator {



    /* ************************************************************************
    *                                                                         *
    *  GENERAL FUNCTIONS: can be used by any of the three core components     *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when a core module attemps to open a connection to
     * the database engine and receives a SQLException in reply.
     * 
     * @param component identifies the core module from which the connection
     *        attempt originated.
     * @param dbUrl JDBC-style database URL to which the connection was
     *        attempted.
     * @param dbUsername user account name supplied to the database engine.
     * @param ex the SQLException encountered.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord dbFailedToConnect(Object component, String dbUrl,
            String dbUsername, SQLException ex) {
        return generateSingleRecord(Level.SEVERE, "{0} could not connect to"
                + " the specified database: {1}, using account {2}.",
                new Object[] { componentName(component), dbUrl, dbUsername },
                ex);
    }

    /**
     * Generates a LogRecord when a core module attemps to load a database
     * driver and an Exception is thrown.
     * 
     * @param component identifies the core module in which the driver is being
     *        loaded.
     * @param driverName The name of the DB driver Class.
     * @param ex the Exception encountered.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord dbDriverFailedToLoad(Object component,
            String driverName, Exception ex) {
        return generateSingleRecord(Level.SEVERE, "{0} could not load the"
                + " MySQL database driver named {1}.  Check to make sure the"
                + " driver is installed properly.", new Object[] {
                componentName(component), driverName }, ex);
    }

    /**
     * Generates a LogRecord when a core module's worker thread takes an
     * excessive amount of time to start.
     * 
     * @param component identifies the core module whos worker thread failed to
     *        start.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord threadFailedToStart(Object component) {
        return generateSingleRecord(Level.SEVERE, "{0}s worker thread failed"
                + " to start in a timely fashion.",
                new Object[] { componentName(component) }, null);
    }

    /**
     * Generates a LogRecord when a core module's worker thread is interrupted.
     * 
     * @param component identifies the core module whos worker thread was
     *        interrupted.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord threadInterrupted(Object component) {
        return generateSingleRecord(Level.FINE, "{0}s worker thread was"
                + " interrupted while waiting for core message.",
                new Object[] { componentName(component) }, null);
    }

    /**
     * Generates a LogRecord when a core module's worker thread thows an
     * unhandled Exception.
     * 
     * @param component identifies the core module whos worker thread threw the
     *        Exception
     * @param ex the Exception encountered.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord threadException(Object component, Throwable ex) {
        return generateSingleRecord(Level.SEVERE, "{0}s worker thread threw"
                + " an exception.  recipnetd has failed and should be"
                + " restarted.", new Object[] { componentName(component) }, ex);
    }

    /**
     * Generates a LogRecord when a core module attempts to bind itself to RMI
     * and an AlredyBoundException is thrown.
     * 
     * @param component identifies the core module that that is attempting to
     *        bind to RMI.
     * @param name the name that is being registered with RMI
     * @param ex the ALreadyBoundException encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord rmiAlreadyBoundException(Object component,
            String name, AlreadyBoundException ex) {
        return generateSingleRecord(Level.SEVERE, "{0} could not initialize"
                + " itself for RMI because the name {1} is already"
                + " bound. Check to see if another instance of recipnetd"
                + " is already running on this machine.  If not, you"
                + " should restart the RMI registry service.", new Object[] {
                componentName(component), name }, ex);
    }

    /**
     * Generates a LogRecord when a core module attempts to bind itself to RMI
     * and a ConnectException is thrown.
     * 
     * @param component identifies the core module that that is attempting to
     *        bind to RMI.
     * @param ex EonnectException encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord rmiConnectException(Object component,
            ConnectException ex) {
        return generateSingleRecord(Level.SEVERE, "{0} could not initialize"
                + " itself for RMI. Please ensure that the RMI registry"
                + " service is running.",
                new Object[] { componentName(component) }, ex);
    }

    /**
     * Generates a LogRecord when a core module attempts to bind itself to RMI
     * and an Exception is thrown. Use rmiConnectException or
     * rmiAlreadyBoundException instead when appropriate.
     * 
     * @param component identifies the core module that that is attempting to
     *        bind to RMI.
     * @param ex Exception encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord rmiException(Object component, Exception ex) {
        return generateSingleRecord(Level.SEVERE, "{0} could not initialize"
                + " itself for RMI.",
                new Object[] { componentName(component) }, ex);
    }

    /**
     * Generates a LogRecord when a core module receives a CoreMessage of an
     * unknown type.
     * 
     * @param component identifies the core module that received the unknown
     *        message type.
     * @param msg the CoreMessage that was receieved.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord cmUnknownType(Object component, CoreMessage msg) {
        return generateSingleRecord(Level.WARNING, "Message of unknown type"
                + " received by {0}: {1}.", new Object[] {
                componentName(component), msg.getClass().getName() }, null);
    }

    /**
     * Generates a LogRecord when an exception is thrown within a core module
     * during the processing of a CoreMessage.
     * 
     * @param component identifies the core component that threw the exception
     * @param msg the CoreMessage whose processing caused the exception
     * @param ex the Exception that was thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord cmCausedException(Object component,
            CoreMessage msg, Exception ex) {
        return generateSingleRecord(Level.WARNING, "Exception encountered"
                + " while processing message received by {0}.  Message class:"
                + " {1}.", new Object[] { componentName(component),
                msg.getClass().getName() }, ex);
    }

    /**
     * Generates a LogRecord when a call to getLocalLabs() thows an Exception.
     * 
     * @param component identifies the core module that made the call to
     *        getLocalLabs()
     * @param ex the Exception encountered.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord getLocalLabsException(Object component, 
            Exception ex) {
        return generateSingleRecord(Level.SEVERE, "{0} was unable to fetch"
                +" local labs list", new Object[] { componentName(component) },
                ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to SITEMANAGER                                                *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates LogRecord when Site Manager fails to open the Site Grant file
     * for reading.
     * 
     * @param siteGrantFile the unreadable File
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord siteGrantFileUnreadable(File siteGrantFile) {
        return generateSingleRecord(Level.SEVERE, "Site Manager could not read"
                + " from the site grant file {0}.",
                new Object[] { siteGrantFile.getName() }, null);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to read data from the
     * Site Grant file and an Exception is thrown.
     * 
     * @param siteGrantFile the site grant File
     * @param ex the Exception thown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord siteGrantFileException(File siteGrantFile,
            Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Site Manager encountered"
                + " an error while reading the site grant file - {0}.",
                new Object[] { siteGrantFile.getName() }, ex);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to preload containers
     * and an Exception is thrown.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord dbPreloadContainersException(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Site Manager could not"
                + " preload containers.", null, ex);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to instatiate
     * TopologyAgent and an Exception is thrown.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord topologyAgentFailedToCreate(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Site Manager could not"
                + " create TopologyAgent.", null, ex);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to instatiate
     * StatisticsAgent and an Exception is thrown.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord statisticsAgentFailedToCreate(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Site Manager could not"
                + " create StatisticsAgent.", null, ex);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to instatiate
     * LabTransferAgent and an Exception is thrown.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord labTransferAgentFailedToCreate(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Site Manager could not"
                + " create LabTransferAgent.", null, ex);
    }

    /**
     * Generates a LogRecord when Site Manager attempts to initialize the
     * digital signature algorithm and an Exception is thrown.
     * 
     * @param algorithm identifies the algorithm
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord algorithmInvalid(String algorithm, Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Digital signature algorithm"
                + " {0} is not available locally.", new Object[] { algorithm },
                ex);
    }

    /**
     * Generates an array of LogRecords when Site Manager attemps to initialize
     * the message file directories and an Exception is thrown.
     * 
     * @param ex the Exception thrown. If ex is an instance of
     *        MessageDecodingException a second LogEvent is returned including
     *        the first bad message.
     * @return a {@code LogRecord[]} containing elements corresponding to the
     *         arguments
     */
    public static LogRecord[] messageFileDirectoriesFailedToInit(Exception ex) {
        LogRecord lr1 = generateSingleRecord(Level.SEVERE, "Site Manager was"
                + " unable to initialize the  message file directories.", null,
                ex);
        
        if (ex instanceof MessageDecodingException) {
            return new LogRecord[] {
                    lr1,
                    generateSingleRecord(
                            Level.FINE,
                            "The problematic ISM contents are {0}",
                            new Object[] { ((MessageDecodingException) ex).getBadMessage() },
                                    ex) };
        } else {
            return new LogRecord[] { lr1 };
        }
    }

    /**
     * Generates a LogRecord when Site Manager finds a local tracking field for
     * a lab that is not hosted at the local site in the config file.
     * 
     * @param labId identifies the lab specified by the local tracking field
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord configInvalidLocalLab(int labId) {
        return generateSingleRecord(Level.SEVERE, "Site Manager was unable to"
                + " configure localtracking fields because lab id {0}"
                + " is not hosted at the local site.",
                new Object[] { Integer.valueOf(labId) }, null);
    }

    /**
     * Generates a LogRecord when Site Manager encounters and Exception while
     * parsing the config file.
     * 
     * @param directive indentifies the directive within the config file
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord configParseException(String directive, Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Parse error on config file"
                + " for directive {0}.", new Object[] { directive }, ex);
    }

    /**
     * Generates a LogRecord when Site Manager encounters an Exception while
     * initializing msgs-held directory.
     * 
     * @param ex the Exception encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord msgsHeldDirFailedToInit(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "ReceivedMessageAgent failed"
                + " to initialize msgs-held directory.", null, ex);
    }

    /**
     * Generates a pair of LogRecord's when an Exception is thrown while writing
     * a sent-ism to file. The first LogRecord includes the filename of the ISM
     * while the second (lower Level) LogRecord includes its Classtype.
     * 
     * @param msg the InterSiteMessage that caused the Exception
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord[] ismGenerationException(InterSiteMessage msg,
            Exception ex) {
        return new LogRecord[] {
                generateSingleRecord(Level.WARNING, "Error while writing"
                        + " sent-ism to file {0}.",
                        new Object[] { msg.getSuggestedFileName() }, ex),
                generateSingleRecord(Level.FINE, "Problematic sent-ism had"
                        + " class {0}.",
                        new Object[] { msg.getClass().getName() }, null) };
    }

    /**
     * Generates an array of LogRecords when an OperationFailedException is
     * thrown during the call from Site Manager to ismPuller.pullMessages().
     * 
     * @param targetSiteId the id of the site from which Site Manager is
     *        attempting to pull ISM's.
     * @param serverErrorMessage the server error message (if any)
     * @param ex the OperationFailedException that was encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord[] ismPullException(int targetSiteId,
            String serverErrorMessage, Exception ex) {
        LogRecord lr = generateSingleRecord(Level.WARNING,
                "Periodic ISM pull from" + " site id {0} failed.",
                new Object[] { Integer.valueOf(targetSiteId) }, ex);
        
        if (serverErrorMessage == null) {
            return new LogRecord[] { lr };
        } else {
            return new LogRecord[] {
                    lr,
                    generateSingleRecord(Level.FINE,
                            "ISM-pull resulted in server error {0}",
                            new Object[] { serverErrorMessage }, null) };
        }
    }

    /**
     * Generates a LogRecord when an unhandled Exception is thrown during the
     * during the body of SiteManager.doReplayRequestTask().
     * 
     * @param ex the Exception that was encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismReplayRequestException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Exception occurred while"
                + " doing periodic ISM pull", null, ex);
    }

    /**
     * Generates a LogRecord when an unhandled Exception is thrown by
     * receivedMsgAgent.periodicCheck() during calls to
     * PageHelper.doRedeliverHeldMsgsTask()
     * 
     * @param ex the Exception that was encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismRedeliveryException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Exception occurred while"
                + " doing periodic held-message redelivery.", null, ex);
    }

    /**
     * Generates LogRecord when Site Manager fails to open the Site Grant file
     * for reading.
     * 
     * @param siteGrantFile the unreadable File
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord siteDeactivated() {
        return generateSingleRecord(Level.SEVERE, "This site has been remotely"
                + " deactivated by the Reciprocal Net Coordinator.  Most user"
                + " operations are disabled.  Please cease all use, stop the"
                + " recipnetd daemon, and uninstall the Reciprocal Net site"
		+ " software.", null, null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to SAMPLEMANAGER                                              *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when an Exception is encountered while Sample
     * Manager tries to build a list of sample Id's.
     * 
     * @param ex the exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdListBuildException(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Sample Manager could not"
                + " build its list of new sample id's.", null, ex);
    }

    /**
     * Generates a LogRecord to report if an exception occurs while Sample
     * Manager calls idAgent.periodicCheck()
     * 
     * @param ex the Exception that occured
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdPeriodicCheckException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Exception occurred"
                + " while doing periodic sample id check", null, ex);
    }

    /**
     * Generates a LogRecord to report if an exception occurs while Sample
     * Manager calls idAgent.periodicCheck()
     * 
     * @param ex the Exception that occured
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleLockPeriodicReleaseException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Exception occurred"
	        + " while doing periodic lock releases", null, ex);
    }

    /**
     * Generates a LogRecord when an ResourcesExhaustedException is encountered
     * while building a list of sample id's during Sample Manager startup.
     * 
     * @param ex the exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdsExhaustedAtStartup(
            ResourcesExhaustedException ex) {
	return generateSingleRecord(Level.SEVERE, "This Reciprocal Net server"
                + " cannot start up because the local site does not possess"
                + " any sample id blocks.  Try running the sync utility in"
                + " order to synchronize this site with the rest of the"
                + " Reciprocal Net Site Network.  If the problem persists,"
                + " please contact Reciprocal Net technical support.", 
                null, ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to REPOSITORYMANAGER                                          *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when an OperationFailedException occurs during a
     * periodic scan of repository directories.
     * 
     * @param ex the Exception that occured
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord periodicDirectoryScanException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Failure during periodic"
                + " scan of repository directories.", null, ex);
    }

    /**
     * Generates a LogRecord when a file is found that doesn't fall under the
     * expected root directory during a scan.
     * 
     * @param baseDirectory identifies the repository base directory
     * @param scannedDirectory the File that is found to be in error
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryDirectoryParseError(String baseDirectory,
            File scannedDirectory) {
        return generateSingleRecord(Level.WARNING, "Filename parse error"
                + " during repository scan: directory {1} does not fall"
                + " under the expected repository root directory {0}",
                new Object[] { baseDirectory, scannedDirectory.getPath() },
                null);
    }

    /**
     * Generates a LogRecord when Repository Manager encounters an exception
     * while attempting to initialize of its agents.
     * 
     * @param ex the Exception that occurred.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryAgentStartupException(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "Exception occurred while"
                + " RepositoryManager was initializing its agents.", null, ex);
    }

    /**
     * Generates a LogRecord when Repository Manager encounters an exception
     * while doing a periodic filesystem scan.
     * 
     * @param ex the Exception that occurred.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryScanException(String urlPath,
            Exception ex) {
        return generateSingleRecord(Level.WARNING, "Periodic repository"
                + " filesystem scan encountered an exception while processing"
                + " directory {0}.",
                new Object[] { urlPath }, ex);
    }

    /**
     * Generates a LogRecord when Repository Manager cancels a periodic scan of
     * its primary directories before it finishes normally
     * 
     * @param countChangedFiles the number of changed files that were detected
     *        during the scan before it was canceled.
     * @param countChangedSamples the number of samples for which at least one
     *        changed file was detected during the scan before it was canceled.
     *        
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryScanCanceled(int countChangedFiles, 
            int countChangedSamples) {
        return generateSingleRecord(Level.INFO, "Periodic repository primary"
                + " directory scan canceled.  Detected {0} changes on {1}"
                + " samples before cancellation.",
                new Object[] {
                        Integer.valueOf(countChangedFiles),
                        Integer.valueOf(countChangedSamples) },
                null);
    }

    /**
     * Generates a LogRecord when Repository Manager fails to complete a
     * periodic scan of its primary directories because of an exception
     * 
     * @param countChangedFiles the number of changed files that were detected
     *        during the scan.
     * @param countChangedSamples the number of samples for which at least one
     *        changed file was detected during the scan.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryScanFailed(int countChangedFiles, 
            int countChangedSamples, Exception ex) {
        return generateSingleRecord(Level.WARNING, "Periodic repository primary"
                + " directory scan failed.  Detected {0} changes on {1}"
                + " samples before the failure.",
                new Object[] {
                        Integer.valueOf(countChangedFiles),
                        Integer.valueOf(countChangedSamples) },
                ex);
    }

    /**
     * Generates a LogRecord when Repository Manager finishes a periodic scan of
     * its primary directories.
     * 
     * @param countChangedFiles the number of changed files that were detected
     *        during the scan.
     * @param countChangedSamples the number of samples for which at least one
     *        changed file was detected during the scan.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord repositoryScanFinished(int countChangedFiles, 
            int countChangedSamples) {
        return generateSingleRecord(Level.INFO, "Periodic repository primary"
                + " directory scan completed.  Detected {0} changes on {1}"
                + " samples.", new Object[] { Integer.valueOf(countChangedFiles),
                Integer.valueOf(countChangedSamples) }, null);
    }

    /**
     * Generates a LogRecord when Repository Manager invalidates a number of
     * holding records associated with some particular site id.
     */
    public static LogRecord repositoryHoldingsInvalidated(int siteId,
            int countHoldingsInvalidated) {
	return generateSingleRecord(Level.INFO, "Invalidated {1} repository"
	        + " holding records formerly associated with site id {0}",
		new Object[] { Integer.valueOf(siteId), 
		Integer.valueOf(countHoldingsInvalidated) }, null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to SAMPLEIDAGENT                                              *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when SampleIdAgent initiates a proposal for a
     * SampleIdBlock.
     * 
     * @param blockId the id of the block requested
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdBlockInitProposal(int blockId) {
        return generateSingleRecord(Level.INFO, "Initiated proposal for sample"
                + " id  block {0}.", new Object[] { Integer.valueOf(blockId) },
                null);
    }

    /**
     * Generates a LogRecord when SampleIdAgent initiates a SampleIdBlock
     * emergency transfer request.
     * 
     * @param count the number of blocks requestd
     * @param destSiteId the site from which the blocks are requested
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdBlockEmergencyTransferInit(int count,
            int destSiteId) {
        return generateSingleRecord(Level.INFO, "Initiated emergency transfer"
                + " request for {0} sample id blocks from site id" + " {1}.",
                new Object[] { Integer.valueOf(count), Integer.valueOf(destSiteId) },
                null);
    }

    /**
     * Generates a LogRecord when SampleIdAgent is granted sufficient approval,
     * and claims a SampleIdBlock.
     * 
     * @param blockId the id of the block claimed
     * @param proposalPeriod the period of time the proposal was outstanding
     * @param approvalCount the number of sites that approved the local site's
     *        claim to the SampleIdBlock
     * @param perfectApprovalCount the amount of sites that could approve or
     *        disapprove of the claim
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdBlockClaim(int blockId,
            long proposalPeriod, int approvalCount, int perfectApprovalCount) {
        return generateSingleRecord(Level.INFO, "Sample id block {0} was"
                + " claimed by the local site after a"
                + " proposal period of {1} milliseconds and an"
                + " approval of {2} out of {3}.", new Object[] {
                Integer.valueOf(blockId), Long.valueOf(proposalPeriod),
                Integer.valueOf(approvalCount),
                Integer.valueOf(perfectApprovalCount) }, null);
    }

    /**
     * Generates a LogRecord when SampleIdAgent is not granted sufficient
     * approval and cannot claim a SampleIdBlock.
     * 
     * @param blockId the id of the block claimed
     * @param proposalPeriod the period of time the proposal was outstanding
     * @param approvalCount the number of sites that approved the local site's
     *        claim to the SampleIdBlock
     * @param perfectApprovalCount the amount of sites that could approve or
     *        disapprove of the claim
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdBlockClaimFailure(int blockId,
            long proposalPeriod, int approvalCount, int perfectApprovalCount) {
        return generateSingleRecord(Level.INFO, "Sample id block"
                + " {0} could not be claimed by the local site"
                + " after a proposal period of {1} milliseconds"
                + " and an approval of {2} out of {3}.", new Object[] {
                Integer.valueOf(blockId), Long.valueOf(proposalPeriod),
                Integer.valueOf(approvalCount),
                Integer.valueOf(perfectApprovalCount) }, null);
    }

    /**
     * Generate a LogRecord when a SampleIdBlock is purged due to its proposal
     * period expiring.
     * 
     * @param block the SampleIdBlock that was proposed and is now being purged.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord sampleIdBlockPurged(SampleIdBlock block) {
        return generateSingleRecord(Level.WARNING, "Sample id block"
                + " {0} proposed by site id {1} on {2}"
                + " was purged because the proposal period expired.",
                new Object[] { Integer.valueOf(block.id),
                        Integer.valueOf(block.siteId), block.proposalDate },
                null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to RECEIVEDMESSAGEAGENT                                       *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when ISM processing is complete. If the message was
     * successfully processed then it is logged at Level.INFO. Otherwise it is
     * logged at Level.WARNING.
     * 
     * @param msg the ProcessedIsmCM for the processed ISM.
     * @param time the number of milliseconds that elapsed during processing
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismProcessed(ProcessedIsmCM msg, long time) {
        if (msg.succeeded) {
            return generateSingleRecord(Level.INFO, "ISM {0} was accepted and"
                    + " processed in {1} ms.  {2}", new Object[] {
                    msg.ism.getSuggestedFileName(), Long.valueOf(time),
                    (msg.message == null ? "" : msg.message) }, null);
            /*
             * It's okay to ignore the shouldClearFile flag in this case --
             * higher-level logic should have ensured that this flag is always
             * true when message processing succeeded.
             */
        } else  if (!msg.succeeded && msg.shouldClearFile) {
            return generateSingleRecord(Level.WARNING, "ISM {0} was"
                    + " accepted and could not be processed.  {1}",
                    new Object[] { msg.ism.getSuggestedFileName(),
                            (msg.message == null ? "" : msg.message) }, null);
        } else if (!msg.succeeded && !msg.shouldClearFile) {
            return generateSingleRecord(Level.WARNING, "ISM {0} was"
                    + " accepted and could not be processed.  It is"
                    + " being held for automatic redelivery.  {1}",
                    new Object[] { msg.ism.getSuggestedFileName(),
                            (msg.message == null ? "" : msg.message) }, null);
        } else {
            return null;
        }
    }

    /**
     * Generates a LogRecord when recipnetd stalls while processing ISMs.
     * 
     * @param filename identifies the ISM for which processing was stalled
     * @param time the number of milliseconds elapsed during processing (should
     *        represent the internal timeout)
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismProcessingStalled(String filename, long time) {
        return generateSingleRecord(Level.SEVERE,
                "recipnetd has stalled and must be restarted.  ISM {0}"
                        + " has been in processing for {1} ms, a very long"
                        + " time.",
                new Object[] { filename, Long.valueOf(time) }, null);
    }

    /**
     * Generates a LogRecord when an exception is encountered while receiveing
     * an ISM.
     * 
     * @param sourceSiteId the id of the site that purportedly sent the message
     * @param ex the Exception encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismReceiveException(int sourceSiteId, Exception ex) {
        return generateSingleRecord(Level.FINE,
                "Error while receiving ISM from site id {0}", new Object[] {
                        sourceSiteId, null }, ex);
    }

    /**
     * Generates a LogRecord that summarizes the handling of an ISM exchange
     * that was initiated by a remote site. See {@code ReceivedMessageAgent} for
     * a thorough description of the various statistical counter arguments to
     * this function.
     * 
     * @param remoteSite a string that identifies the remote site that initiated
     *        the ISM exchange. For instance "129.79.85.43" or "site 11,563".
     * @param received the total number of messages received
     * @param unknownSender the number of messages received from an unknown site
     * @param badSignature the number of messages whose signatures were invalid
     * @param parseError the number of messages that could not be parsed
     * @param tooOld the number of messages that had already been processed
     * @param localOrigin the number of messages originating from the local site
     * @param notAddressedToLocal the number of messages not addressed to this
     *        site
     * @param fromDeactivatedSite the number of messages purportedly sent by
     *        deactivated sites, or almost-deactivated sites with sequence
     *        numbers too high
     * @param miscError the number of messages dropped on account of other
     *        errors
     * @param acceptedAndQueued the number of messages queued for normal
     *        processing
     * @param linkLocal the number of messages diverted for link-local
     *        processing
     * @param unrecognizedType the number of messages of unrecognized type
     * @param linkLocalAccepted the number of messages accepted for link-local
     *        processing
     * @param linkLocalResponses the number of link-local response messages
     *        sent
     * @param messagesReplayed the number of ISMs replayed
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismExchangeSummary(String remoteSite, int received,
            int unknownSender, int badSignature, int parseError, int tooOld,
	    int localOrigin, int notAddressedToLocal, int fromDeactivatedSite,
	    int miscError, int acceptedAndQueued, int linkLocal, 
            int unrecognizedType, int linkLocalAccepted, 
            int linkLocalResponses, int messagesReplayed) {
        StringBuilder sb = new StringBuilder("{1} ISMs received from {0}.");
        Level level = Level.INFO;
        
        if (unknownSender > 0) {
            sb.append(" {2} originated from an unknown site and were");
            sb.append(" dropped.");
            level = Level.WARNING;
        }
        if (badSignature > 0) {
            sb.append(" {3} had invalid digital signatures and were dropped.");
            level = Level.WARNING;
        }
        if (parseError > 0) {
            sb.append(" {4} could not be parsed and were dropped.");
            level = Level.WARNING;
        }
        if (tooOld > 0) {
            sb.append(" {5} duplicates of messages already processed were"
                    + " dropped.");
        }
        if (localOrigin > 0) {
            sb.append(" {6} originated from the local site and were dropped.");
            level = Level.WARNING;
        }
        if (notAddressedToLocal > 0) {
            sb.append(" {7} were not addressed to the local site and were"
                    + " dropped.");
            level = Level.WARNING;
        }
        if (fromDeactivatedSite > 0) {
            sb.append(" {8} originated from a deactivated or"
                    + " almost-deactivated site and were dropped.");
            level = Level.WARNING;
        }
        if (miscError > 0) {
            sb.append(" {9} encountered miscellaneous errors and were"
                    + " dropped.");
            level = Level.WARNING;
        }
        if (acceptedAndQueued > 0) {
            sb.append(" {10} were accepted and queued for processing.");
        }
        if (linkLocal > 0) {
            sb.append(" {11} were diverted for link-local processing.");
        }
        if (unrecognizedType > 0) {
            sb.append(" {12} had an unrecognized type and were dropped.");
            level = Level.WARNING;
        }
        if (linkLocalAccepted > 0) {
            sb.append(" {13} link-local messages were accepted.");
        }
        if (linkLocalResponses > 0) {
            sb.append(" {14} responses were sent.");
        }
        if (messagesReplayed > 0) {
            sb.append(" {15} ISMs were replayed.");
        }
        return generateSingleRecord(level, sb.toString(), new Object[] {
                remoteSite, // 0
                received, // 1
                unknownSender, // 2
                badSignature, // 3
                parseError, // 4
                tooOld, // 5
                localOrigin, // 6
                notAddressedToLocal, // 7
		fromDeactivatedSite, // 8
                miscError, // 9
                acceptedAndQueued, // 10
                linkLocal, // 11
                unrecognizedType, // 12
                linkLocalAccepted, // 13
                linkLocalResponses, // 14
                messagesReplayed, // 15
                null }, null);
    }

    /**
     * Generates a LogRecord when ReceivedMessageAgent finally fully
     * deactivates a site record following some final ISM processing.  This log
     * message should always be preceded by a "partial" deactivation message
     * emitted by SiteManager.
     * 
     * @param site a site record just recently deactivated.
     * @return a {@code LogRecord} corresponding to the arguments.
     */
    public static LogRecord ismSiteDeactivatedDelayed(SiteInfo site) {
        return generateSingleRecord(Level.INFO,
                "Recorded full deactivation of site id {0} following final"
		+ " ISM processing",
		new Object[] { site.id, null }, null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to CORELOADER                                                 *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when an Exception is thrown while starting up the
     * core modules.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord daemonFailedToStart(Exception ex) {
        return generateSingleRecord(Level.SEVERE, "daemon did not start.",
                null, ex);
    }

    /**
     * Generates a LogRecord when one of the core components fails to start for
     * CoreLoader.
     * 
     * @param component identifies the component that failed to start
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord componentFailedToStart(Object component) {
        return generateSingleRecord(Level.SEVERE, "daemon did not start -"
                + " {0} error.", new Object[] { componentName(component) },
                null);
    }

    /**
     * Generates a LogRecord when CoreLoader starts.
     * 
     * @return an appropriate {@code LogRecord}
     */
    public static LogRecord daemonStarted() {
        return generateSingleRecord(Level.INFO, "daemon started.", null, null);
    }

    /**
     * Generates a LogRecord when an Exception is thrown while invoking an
     * external process to signal successful startup.
     * 
     * @param commandLine identifies the external program that the caller
     *        attempted to invoke.
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord daemonStartupSignalException(String commandLine,
            Exception ex) {
        return generateSingleRecord(Level.SEVERE, "error invoking external"
                + " process {0} to signal successful startup",
                new Object[] { commandLine }, ex);
    }

    /**
     * Generates a LogRecord when a RemoteException is thrown while trying to
     * create a new RMI registry.
     * 
     * @param ex the RemoteException thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord rmiRegistryStartupException(RemoteException ex) {
        return generateSingleRecord(Level.SEVERE, "error staring rmiregistry."
                + " Check to make sure that no other instances of recipnetd or"
                + " rmiregistry are running", null, ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to CORESHUTDOWN                                               *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord at the beginning of a core shutdown.
     * 
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord daemonShuttingDown() {
        return generateSingleRecord(Level.INFO, "daemon shutting down.", null,
                null);
    }

    /**
     * Generates a LogRecord to at the completion of a core shutdown.
     * 
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord daemonStopped() {
        return generateSingleRecord(Level.INFO, "daemon stopped.", null, null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to COREPROCESSWRAPPER                                         *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord during a call to CoreProcessWrapper.getLogRecord();
     * describes the activity of an external process, including whether it
     * terminated normally or abnormally.
     * 
     * @param args strings array used the invoke the external process, as passed
     *        to Runtime.exec().
     * @param exitCode the exit code of the external process.
     * @param runningTime the approximate number of milliseconds the process ran
     *        before it terminated.
     * @param outputText captured text that the process wrote to stdout, or null
     *        if captured text is not available.
     * @param errorText captured text that the process wrote to stderr, or null
     *        if captured text is not available.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord processTermination(String args[], int exitCode,
            long runningTime, String outputText, String errorText) {
        StringBuilder commandLine = new StringBuilder();
        
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                commandLine.append(args[i]);
                commandLine.append(" ");
            }
        }
        return generateSingleRecord(Level.FINEST, "External process {0}"
                + " spawned; exited with code {1} after {2} ms.  Output text:"
                + " {3}.  Error text: {4}.", new Object[] { commandLine,
                Integer.valueOf(exitCode), Long.valueOf(runningTime),
                outputText, errorText }, null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to LOCKAGENT                                                  *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when LockAgent encounters an exception while
     * attempting to revoke a previously-granted lock. Presumably this exception
     * would have been thrown by the lock object itself.
     * 
     * @param lock the lock object that threw the exception.
     * @param ex the Exception that occurred.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord lockExceptionOnRevoke(AbstractLock lock,
            OperationFailedException ex) {
        return generateSingleRecord(Level.WARNING, "Error encountered while"
                + " revoking lock {0} of type {1}.", new Object[] {
                Integer.valueOf(lock.getId()), lock.getClass().getName() }, ex);
    }

    /**
     * Generates a LogRecord when LockAgent fails to acquire its scheduler lock
     * within a reasonable period. LockAgent typically throws a
     * {@code DeadlockDetectedException} to its caller in this case.
     * 
     * @param ex the DeadlockDetectedException that will be thrown by the
     *        caller.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord lockSchedulerTimeout(DeadlockDetectedException ex) {
        return generateSingleRecord(Level.WARNING, "Locking subsystem detected"
                + " deadlock while waiting for the scheduler -- the most"
                + " likely cause is an internal programming error.", null, ex);
    }

    /**
     * Generates two LogRecord's when LockAgent fails to acquire a
     * caller-specified lock within a reasonable period. LockAgent typically
     * throws a {@code DeadlockDetectedException} to its caller in this case.
     * 
     * @param ex the {@code DeadlockDetectedException} that will be thrown by
     *        the caller.
     * @param desiredLock the lock that LockAgent's caller attempted to acquire.
     * @param lockToIgnore the lock that LockAgent's caller said could be
     *        ignored, or null.
     * @param activeLocks a {@code Collection} of {@code AbstractLock} objects
     *        representing the set of currently active locks, according to
     *        {@code LockAgent}.
     * @param pendingLocks a {@code Collection} of {@code AbstractLock} objects
     *        representing the set of currently pending locks, according to
     *        {@code LockAgent}.
     * @param freeConnections a {@code Collection} of {@code Connection} objects
     *        representing the free pool of connections that {@code LockAgent}
     *        maintains. The current implementation does nothing more than count
     *        the number of items in this {@code Collection}.
     * @param busyConnections a {@code Collection} of {@code Connection} objects
     *        representing the set of connections currently on loan to active
     *        locks by {@code LockAgent}. The current implementation does
     *        nothing more than count the number of items in this
     *        {@code Collection}.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord[] lockAcquisitionTimeout(
            DeadlockDetectedException ex, AbstractLock desiredLock,
            AbstractLock lockToIgnore,
            Collection<? extends AbstractLock> activeLocks,
            Collection<? extends AbstractLock> pendingLocks,
            Collection<? extends Connection> freeConnections,
            Collection<? extends Connection> busyConnections) {
        StringBuilder activeLocksTable = new StringBuilder();
        
        for (AbstractLock lock : activeLocks) {
            activeLocksTable.append("[");
            activeLocksTable.append(lock.toString());
            activeLocksTable.append("]");
        }

        StringBuilder pendingLocksTable = new StringBuilder();
        for (AbstractLock lock : pendingLocks) {
            pendingLocksTable.append("[");
            pendingLocksTable.append(lock.toString());
            pendingLocksTable.append("]");
        }

        return new LogRecord[] {
                generateSingleRecord(
                        Level.WARNING,
                        "Locking subsystem detected deadlock while waiting to"
                                + " acquire lock [{0}]. {1} other locks were"
                                + " active and {2} other locks were pending."
                                + " The most likely cause is an internal"
                                + " programming error.",
                        new Object[] { desiredLock.toString(),
                                Integer.valueOf(activeLocks.size()),
                                Integer.valueOf(pendingLocks.size() - 1) }, ex),
                generateSingleRecord(
                        Level.FINE,
                        "Deadlock debugging info: desired=[{0}] ignore=[{1}]"
                                + " freeDbConn={2} busyDbConn={3} active=[{4}]"
                                + " pending=[{5}].",
                        new Object[] {
                                desiredLock.toString(),
                                ((lockToIgnore != null) 
                                        ? String.valueOf(lockToIgnore.getId())
                                        : ""),
                                Integer.valueOf(freeConnections.size()),
                                Integer.valueOf(busyConnections.size()),
                                activeLocksTable.toString(),
                                pendingLocksTable.toString() }, null) };
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to PRIMARYDIRECTORYAGENT                                      *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when PrimaryDirectoryAgent encounters an exception
     * while committing to CVS a file sitting inside a particular sample's
     * primary repository directory.
     * 
     * @param file the file on the filesystem for which the error was
     *        encountered.
     * @param ex the Exception that occurred.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord primaryDirectoryCommitException(File file,
            Exception ex) {
        return generateSingleRecord(Level.WARNING, "Unable to commit file {0}"
                + " to CVS from primary directory.",
                new Object[] { file.getPath() }, ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to SECONDARYDIRECTORYAGENT                                    *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when SecondaryDirectoryAgent encounters an
     * exception while deleting a (temporary) secondary repository directory
     * that is no longer needed.
     * 
     * @param secondaryDirectory identifies the directory on the filesystem for
     *        which deletion was attempted.
     * @param ex the Exception that occurred.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord secondaryDirectoryDeletionException(
            File secondaryDirectory, Exception ex) {
        return generateSingleRecord(Level.WARNING, "Unable to delete temporary"
                + " secondary repository directory {0} after use.",
                new Object[] { secondaryDirectory.getPath() }, ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to ISMPUSHAGENT                                               *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when an ISM's are successfully pushed to another
     * site.
     * 
     * @param isms a {@code Collection} of one or more {@code InterSiteMessage}
     *        objects describing the set of ISM's that was just pushed.
     * @param site the SiteInfo object for the site to which the message was
     *        pushed.
     * @param countRepliesReceived a count of the number of ISMs the remote site
     *        sent to the local site as part of the exchange that the local site
     *        chose to discard.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord ismPushed(Collection<InterSiteMessage> isms,
            SiteInfo site, int countRepliesReceived) {
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;
        
        for (InterSiteMessage ism : isms) {
            if (!isFirst) {
                buf.append(", ");
            }
            isFirst = false;
            buf.append(ism.getSuggestedFileName());
        }
        
        return generateSingleRecord(
                Level.INFO,
                countRepliesReceived > 0
                        ? "ISM(s) {0} pushed to {1}.  {2} replies were discarded."
                        : "ISM(s) {0} pushed to {1}.",
                new Object[] {
                        buf.toString(), site.baseUrl, countRepliesReceived },
                null);
    }

    /**
     * Generates an array of LogRecords when Site Manager is unable to push an
     * ISM's to another site. The first LogRecord explains that the push failed.
     * The second record reports the server error returned.
     * 
     * @param isms a {@code Collection} of one or more {@code InterSiteMessage}
     *        objects describing the set of ISM's for which a Push operation was
     *        unsuccessful.
     * @param site the SiteInfo container for the site being pushed to
     * @param serverErrorMessage the server error message (if any)
     * @param ex the Exception reported
     * @return a {@code LogRecord[]} containing describing the push failure
     */
    public static LogRecord[] ismPushFailed(
            Collection<? extends InterSiteMessage> isms, SiteInfo site,
            String serverErrorMessage, Exception ex) {
        StringBuffer buf = new StringBuffer();
        boolean isFirst = true;
        
        for (InterSiteMessage ism : isms) {
            if (!isFirst) {
                buf.append(", ");
            }
            isFirst = false;
            buf.append(ism.getSuggestedFileName());
        }

        LogRecord lr = generateSingleRecord(Level.WARNING,
                "Network error prevented"
                        + " ISM {0} from being pushed to site id {1}.",
                new Object[] { buf.toString(), Integer.valueOf(site.id) }, ex);
        if (serverErrorMessage == null) {
            return new LogRecord[] { lr };
        } else {
            return new LogRecord[] {
                    lr,
                    generateSingleRecord(Level.FINE,
                            "ISM-push resulted in server error {0}",
                            new Object[] { serverErrorMessage }, null) };
        }
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to STATISTICSAGENT                                            *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when StatisticsAgent encounters an exception while
     * compiling lab-specific statistics for a SiteStatisticsISM.
     * 
     * @param labId identifies the lab whose statistics were being retrieved
     *        when the excption was encountered
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord labStatsGenerationException(int labId, Exception ex) {
        return generateSingleRecord(Level.WARNING, "An exception was"
                + " encountered while compiling lab-specific statistics in"
                + " response to a SiteStatisticsRequestISM, for lab id {0}.",
                new Object[] { Integer.valueOf(labId) }, ex);
    }

    /**
     * Generates a LogRecord when StatisticsAgent encounters an exception while
     * compiling site-specific statistics for a SiteStatisticsISM.
     * 
     * @param ex the Exception thrown
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord siteStatsGenerationException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "An exception was"
                + " encountered while compiling site-specific statistics"
                + " in response to a SiteStatisticsRequestISM.", null, ex);
    }

    /**
     * Generates LogRecord when statistics information is written to the DB.
     * 
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord statsDbWriteBehind() {
        return generateSingleRecord(Level.INFO, "Site Manager wrote"
                + " statistics information to the database.", null, null);
    }

    /**
     * Generates LogRecord when an exception is throw preventing statistics
     * information from being written to the DB.
     * 
     * @param ex the {@code Exception} encountered
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord statsDbWriteBehindException(Exception ex) {
        return generateSingleRecord(Level.WARNING, "Site Manager encountered"
                + " an exception while writing statistics information to the"
                + " database.", null, ex);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to TOPOLOGYAGENT                                              *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when TopologyAgent decides to shun a site for not
     * participating in ISM pushes.
     * 
     * @param site identifies the remote site.
     * @param shunnedDuration number of milliseconds site will be shunned.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord topologySiteShunned(int siteId, 
	    long shunnedDuration) {
        return generateSingleRecord(Level.INFO, "Shunning site id {0} for the"
	        + " next {1} minutes due to failed ISM pushes.",
		new Object[] { siteId, shunnedDuration / 60000 },
		null);
    }



    /* ************************************************************************
    *                                                                         *
    *  Specific to LABTRANSFERAGENT                                           *
    *                                                                         *
    **************************************************************************/



    /**
     * Generates a LogRecord when LabTransferAgent dismisses a local lab.
     * 
     * @param labId identifies the lab that was dismissed.
     * @param previousHomeSiteId identifies the lab's previous home site.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord labTransferArrived(int labId, 
            int previousHomeSiteId) {
        return generateSingleRecord(Level.INFO, "Greeted local lab with id"
                + " {0}.  The lab was transferred from site id {1}.",
		new Object[] { labId, previousHomeSiteId }, null);
    }

    /**
     * Generates a LogRecord when LabTransferAgent dismisses a local lab.
     * 
     * @param labId identifies the lab that was dismissed.
     * @param newHomeSiteId identifies the lab's new home site.
     * @return a {@code LogRecord} corresponding to the arguments
     */
    public static LogRecord labTransferDeparted(int labId, int newHomeSiteId) {
        return generateSingleRecord(Level.INFO, "Dismissed local lab with id"
                + " {0}.  The lab was transferred to site id {1}.",
		new Object[] { labId, newHomeSiteId }, null);
    }



    /* ************************************************************************
    *                                                                         *
    *                        PRIVATE HELPER FUNCTIONS                         *
    *                                                                         *
    **************************************************************************/



    /**
     * This static function generates a quick and easy log record that can be
     * used for structured logging.
     *
     * @param level the Level of the LogRecord: SEVERE, WARNING, INFO, etc...
     * @param msg a message that can optionally contain MessageFormat formatted
     *     parameter insertion.  Note: Inclusion of the first argument 
     *     denoted by the substring "{0" will automatically signal formatting.
     * @param args an array of objects to be used as optional arguments
     * @param thrown the {@code Throwable} object that instigated this
     *     LogEvent.
     * @return a {@code LogRecord} corresponding to the arguments 
     */
    private static LogRecord generateSingleRecord(Level level, String msg,
            Object args[], Throwable thrown) {
        LogRecord lr = new LogRecord(level, msg);
        
        if (args != null) {
            lr.setParameters(args);
        }
        if (thrown != null) {
            lr.setThrown(thrown);
        }
        
        return lr;
    }

    /**
     * A private helper function to display the name of a passed core 
     * component.
     * 
     * @param component valid Object types include SiteManager, SampleManager
     *     and RepositoryManager.
     * @return a human-readable String name for the specified component object
     */
    private static String componentName(Object component) {
        if (component instanceof SiteManager) {
            return new String("Site Manager");
        } else if (component instanceof SampleManager) {
            return new String("Sample Manager");
        } else if (component instanceof RepositoryManager) {
            return new String("Repository Manager");
        } else if (component instanceof IsmPushAgent) {
            return new String("ISM Push Agent");
        } else if (component instanceof ReceivedMessageAgent) {
            return new String("Received Message Agent");
        } else {
            return new String("Unspecified Core Component");
        }
    }
}
