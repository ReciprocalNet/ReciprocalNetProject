/*
 * Reciprocal Net project
 * 
 * StatisticsAgent.java
 *
 * 10-Jul-2003: midurbin wrote first draft 
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; also changed package
 *              references to match source tree reorganization
 * 08-Aug-2004: cwestnea modified processSiteStatisticsRequestISM() to use
 *              SampleWorkflowBL
 * 11-Apr-2006: jobollin removed inaccessible catch blocks and organized imports
 */

package org.recipnet.site.core.agent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.IsmProcessingException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.VersionUpdater;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.msg.SiteStatisticsISM;
import org.recipnet.site.core.msg.SiteStatisticsRequestISM;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.LabStatisticsInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.db.SiteStatisticsInfo;
import org.recipnet.site.shared.logevent.LogEvent;
import org.recipnet.site.shared.logevent.LoginLogEvent;
import org.recipnet.site.shared.logevent.OaiPmhLogEvent;
import org.recipnet.site.shared.logevent.SampleViewLogEvent;
import org.recipnet.site.shared.logevent.SessionBeginLogEvent;
import org.recipnet.site.shared.logevent.SessionEndLogEvent;

/**
 * Statistics agent maintains site statistics in memory and in its own DB
 * tables. This class is thread-safe.
 */
public class StatisticsAgent {

    /*
     * TODO: Locking is done by sychronization of all member functions, if this
     * hurts performance too much, more granular locking ought to be adopted.
     */

    /**
     * The database connection provided by {@code SiteManager} during the
     * constructor. Synchronize on this.
     */
    private Connection conn;

    /**
     * A reference to the {@code SiteManager} object to which this {@code 
     * StatisticsAgent}
     * object belongs.
     */
    private SiteManager siteManager;

    /**
     * A reference to the {@code RepositoryManager}. Calls are made to
     * {@code RepositoryManager} to retrieve statistics about the
     * repository.
     */
    private RepositoryManager repositoryManager;

    /**
     * Contains various statistics counters that are initialized from the
     * database.
     */
    private SiteStatisticsInfo siteStatisticsInfo;

    /**
     * A {@code Collection} of {@code LabStatisticsInfo} objects.
     * There is one for each lab, containing counts of the number of served http
     * requests for pages that can view samples and for those that can edit
     * samples. These counters are initialized at startup and updated by
     * {@code LogEvent} notifications. From construction time, this can
     * be treated as an exhaustive list of labs for the purpose of statistics
     * reporting.
     */
    private Collection<LabStatisticsInfo> labStatsInfos;

    /**
     * <b>startTime</b> contains a number of milliseconds that represented the
     * current time at the last time that <b>uptime</b> was updated or when
     * this {@code StatisticsAgent} was initialized. It is used to track
     * uptime via calls to the function {@code getUptimeSinceLastCall()
     * }.
     * This value may ONLY be modified during the constructor or the
     * synchronized function {@code getUptimeSinceLastCall()}.
     */
    private long startTime;

    /**
     * A {@code Collection} of currently active unauthenticated sessions.
     * Sessions are represented by {@code String} objects containing
     * their unique JSP session Id. When a {@code SessionBeginLogEvent}
     * is recieved, the session id is added. {@code SessionEndLogEvent}s
     * and {@code LoginLogEvent}'s remove sessions from this
     * {@code Collection} and update the corresponding counters.
     */
    private Collection<String> activeUnauthenticatedSessions;

    /**
     * Initializes a {@code StatisticsAgent} for {@code SiteManager}.
     * At this time values from the database are read into the container
     * objects.
     * <p>
     * There may only be one instance of {@code StatisticsAgent} and its
     * instantiation must coincide with a recipnetd startup.
     * 
     * @param conn a reference to SiteManager's database connection
     * @param siteManager a reference to the {@code SiteManager} object
     *        that owns this {@code StatisticsAgent}.
     * @param repositoryManager a reference to the Repository Manager.
     * @throws OperationFailedException if the StatisticsAgent object cannot be
     *         initialized.
     */
    public StatisticsAgent(Connection conn, SiteManager siteManager,
            RepositoryManager repositoryManager)
            throws OperationFailedException {
        this.conn = conn;
        this.siteManager = siteManager;
        this.repositoryManager = repositoryManager;
        try {
            synchronized (this.conn) {
                Statement cmd = this.conn.createStatement();
                ResultSet rs =
                        cmd.executeQuery("SELECT * FROM statisticsSite;");
                rs.first();
                this.siteStatisticsInfo = new SiteStatisticsInfo(rs);
                this.siteStatisticsInfo.countRecipnetdStartups++;

                rs = cmd.executeQuery("SELECT * FROM statisticsLabs;");
                this.labStatsInfos = new HashSet<LabStatisticsInfo>();
                while (rs.next()) {
                    LabStatisticsInfo ls = new LabStatisticsInfo(rs);
                    this.labStatsInfos.add(ls);
                }
                cmd.close();
            }
        } catch (SQLException ex) {
            throw new OperationFailedException(ex);
        }

        this.startTime = System.currentTimeMillis();
        this.activeUnauthenticatedSessions = new HashSet<String>();
    }

    /**
     * Updates {@code StatisticsAgent}'s database tables to match the
     * the in-memory counters.
     */
    public synchronized void periodicWriteBehind() {
        try {
            synchronized (this.conn) {
                // update uptime
                this.siteStatisticsInfo.uptime += getUptimeSinceLastCall();

                Statement cmd =
                        this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                                ResultSet.CONCUR_UPDATABLE);
                try {
                    ResultSet rs =
                            cmd.executeQuery("SELECT * FROM statisticsSite;");
                    rs.next();
                    // store all counters
                    this.siteStatisticsInfo.dbStore(rs);
                    rs.updateRow();
                } finally {
                    cmd.close();
                }

                cmd =
                        this.conn.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);
                try {
                    for (LabStatisticsInfo ls : this.labStatsInfos) {
                        ResultSet rs =
                                cmd.executeQuery("SELECT * FROM statisticsLabs"
                                        + " WHERE lab_id=" + ls.labId + ";");
                        if (!rs.first()) {
                            rs =
                                    cmd.executeQuery("SELECT * FROM"
                                            + " statisticsLabs;");
                            rs.moveToInsertRow();
                            ls.dbStore(rs);
                            rs.insertRow();
                        } else {
                            ls.dbStore(rs);
                            rs.updateRow();
                        }
                    }
                } finally {
                    cmd.close();
                }
            }
            this.siteManager.recordLogRecord(LogRecordGenerator
                    .statsDbWriteBehind());
        } catch (Exception ex) {
            // periodic tasks may not throw exceptions so logging is the best
            // we can do.
            this.siteManager.recordLogRecord(LogRecordGenerator
                    .statsDbWriteBehindException(ex));
        }
    }

    /**
     * Processes a {@code SiteStatisticsRequestISM}. A
     * {@code ProcessedIsmCm} is always sent to the provided
     * {@code SiteManager} reference and if successfully processed, a
     * {@code SendIsmCM} containing the {@code SiteStatisticsISM}
     * is also passed.
     * 
     * @param requestMsg the message that requsted the statistics.
     * @throws IsmProcessingException if the {@code SourceSiteId} is not
     *         the coordinator or the {@code targetSiteId} is not a known
     *         site.
     * @throws OperationFailedException if an exception is encountered while
     *         processing the message.
     * @throws UnexpectedExceptionException if this class is used in an
     *         unintended way and normally impossible exceptions are caught
     */
    public synchronized void processSiteStatisticsRequestISM(
            SiteStatisticsRequestISM requestMsg) throws IsmProcessingException,
            OperationFailedException {

        if (requestMsg.sourceSiteId != InterSiteMessage.RECIPROCAL_NET_COORDINATOR) {
            throw new IsmProcessingException(requestMsg,
                    IsmProcessingException.SENDER_NOT_AUTHORIZED);
        }

        try {
            siteManager.getSiteInfo(requestMsg.collectionSiteId);
        } catch (ResourceNotFoundException ex) {
            throw new IsmProcessingException(requestMsg,
                    IsmProcessingException.RESOURCE_NOT_ADVERTISED);
        }

        // if the request has expired, we are done.
        if ((requestMsg.expirationDate != null)
                && (new Date().compareTo(requestMsg.expirationDate) > 0)) {
            siteManager.passCoreMessage(ProcessedIsmCM.success(requestMsg,
                    "A SiteStastisticsISM was processed, but ignored because"
                            + " its expiration date had been reached."));
            return;
        }
        SiteStatisticsISM msg = null;
        // update uptime
        this.siteStatisticsInfo.uptime += getUptimeSinceLastCall();

        // Generate an ISM with all the up-to-date in-memory counters
        // The requesting message (from the coordinator) must specify a
        // collection site to recieve statistics information.(the
        // validity of this site id will be asserted at send time)
        msg =
                new SiteStatisticsISM(this.siteManager.localSiteId,
                        requestMsg.collectionSiteId, this.siteStatisticsInfo,
                        this.labStatsInfos);

        if (msg.labSpecificStatistics != null) {
            for (SiteStatisticsISM.PerLabStats currentLabStats
                    : msg.labSpecificStatistics) {
                try {
                    LabInfo currentLabInfo =
                            (this.siteManager.getLabInfo(currentLabStats.labId));

                    // determine and set repository size (if hosted locally)
                    if (currentLabInfo.homeSiteId == this.siteManager.localSiteId) {
                        currentLabStats.repositorySize =
                                this.repositoryManager
                                        .getLabDirectorySize(currentLabInfo);
                    }

                    // Set public sample count:
                    synchronized (conn) {
                        Statement cmd = this.conn.createStatement();
                        ResultSet rs =
                                cmd
                                        .executeQuery("SELECT Count(*) FROM"
                                                + " samples WHERE lab_id="
                                                + currentLabInfo.id
                                                + " AND (status="
                                                + SampleWorkflowBL.COMPLETE_PUBLIC_STATUS
                                                + " OR status="
                                                + SampleWorkflowBL.RETRACTED_PUBLIC_STATUS
                                                + " OR status="
                                                + SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS
                                                + " OR status="
                                                + SampleWorkflowBL.NON_SCS_PUBLIC_STATUS
                                                + ");");
                        rs.next();
                        currentLabStats.countPublicSamples = rs.getInt(1);
                    }
                } catch (Exception ex) {
                    this.siteManager.recordLogRecord(LogRecordGenerator
                            .labStatsGenerationException(currentLabStats.labId,
                                    ex));
                    throw new OperationFailedException(ex);
                }
            }
        }
        synchronized (conn) {
            // get recipnet version
            try {
                msg.version = VersionUpdater.getCurrentVersion(conn);
            } catch (SQLException ex) {
                this.siteManager.recordLogRecord(LogRecordGenerator
                        .siteStatsGenerationException(ex));
                throw new OperationFailedException(ex);
            }
        }

        // generate a sequence number table
        msg.ismPublicSeqNumMap = new HashMap<Integer, Long>();
        msg.ismPrivateSeqNumMap = new HashMap<Integer, Long>();
        try {
            SiteInfo sites[] = siteManager.getAllSiteInfo();
            for (SiteInfo element : sites) {
                msg.ismPublicSeqNumMap.put(Integer.valueOf(element.id), Long
                        .valueOf(element.publicSeqNum));
                msg.ismPrivateSeqNumMap.put(Integer.valueOf(element.id), Long
                        .valueOf(element.privateSeqNum));
            }
        } catch (OperationFailedException ex) {
            this.siteManager.recordLogRecord(LogRecordGenerator
                    .siteStatsGenerationException(ex));
            throw ex;
        }

        // send messages
        siteManager.passCoreMessage(new SendIsmCM(msg));
        siteManager.passCoreMessage(ProcessedIsmCM.success(requestMsg));

        // Reset counters
        if (requestMsg.resetCounters) {
            this.siteStatisticsInfo.reset();
            for (LabStatisticsInfo labStats : this.labStatsInfos) {
                labStats.reset();
            }
        }
    }

    /**
     * Extracts statistical information from the log event and updates counters
     * in {@code siteStatisticsInfo}.
     * 
     * @param event a {@code LogEvent} subclass that may contain relevent
     *        information.
     */
    public synchronized void notifyLogEvent(LogEvent event) {
        if (event instanceof SessionBeginLogEvent) {
            this.activeUnauthenticatedSessions
                    .add(((SessionBeginLogEvent) event).jspSessionId);
        } else if (event instanceof SessionEndLogEvent) {
            if (activeUnauthenticatedSessions
                    .remove(((SessionEndLogEvent) event).jspSessionId)) {
                this.siteStatisticsInfo.unauthWebappSessions++;
            }
        } else if (event instanceof LoginLogEvent) {
            if (((LoginLogEvent) event).authenticationResult) {
                this.activeUnauthenticatedSessions
                        .remove(((LoginLogEvent) event).jspSessionId);
                this.siteStatisticsInfo.authWebappSessions++;
            }
        } else if (event instanceof OaiPmhLogEvent) {
            this.siteStatisticsInfo.countOaiPmhQueries++;
            this.siteStatisticsInfo.countOaiPmhQuerySamples +=
                    ((OaiPmhLogEvent) event).countSamplesReturned;
        } else if (event instanceof SampleViewLogEvent) {
            SampleViewLogEvent svle = (SampleViewLogEvent) event;
            boolean foundAndUpdated = false;
            for (LabStatisticsInfo current : this.labStatsInfos) {
                // Search for lab whose sample was viewed/edited
                if (current.labId == svle.labId) {
                    // update it's counter and return
                    foundAndUpdated = true;
                    if (svle.viewOrEdit) {
                        current.editSamplePageViews++;
                    } else {
                        current.showSamplePageViews++;
                    }
                    return;
                }
            }
            if (!foundAndUpdated) {
                // Add a new lab to the list and update its counters
                LabStatisticsInfo newLab = new LabStatisticsInfo();
                newLab.labId = svle.labId;
                if (svle.viewOrEdit) {
                    newLab.editSamplePageViews++;
                } else {
                    newLab.showSamplePageViews++;
                }
                this.labStatsInfos.add(newLab);
            }
        }
    }

    /**
     * Must be called when recipnetd shuts down. All
     * {@code siteStatisticsInfo} are written to the database.
     */
    public synchronized void notifyCoreShutdown() {
        this.siteStatisticsInfo.countRecipnetdShutdowns++;
        periodicWriteBehind();
    }

    /**
     * Returns the number of seconds that have elapsed since the last call to
     * this function, or since construction of this {@code StatisticsAgent}
     * object.
     */
    private synchronized int getUptimeSinceLastCall() {
        long now = System.currentTimeMillis();
        int secondsElapsed = (int) ((now - this.startTime + 500) / 1000);
        this.startTime = now;
        return secondsElapsed;
    }
}
