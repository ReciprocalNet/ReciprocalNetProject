/*
 * Reciprocal Net project
 * 
 * SiteStatistics.java
 *
 * 10-Jul-2003: midurbin wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * SiteStatistics is a container class that contains site statistics information
 * that must persist through recipnetd shutdowns that cannot be efficiently
 * derived from other data. The data maps cleanly onto the database table named
 * 'statisticsSite'.
 */
public class SiteStatisticsInfo {

    /** The time when all of the counters were reset */
    public Date lastReset;

    /** The number of times recipnetd started up since last reset */
    public int countRecipnetdStartups;

    /** The number of times recipnetd shut down since last reset */
    public int countRecipnetdShutdowns;

    /** The number of seconds recipnetd has been running since last reset */
    public int uptime;

    /** The number of authenticated webapp sessions since last reset */
    public int authWebappSessions;

    /** The number of unauthenticated webapp sessions since last reset */
    public int unauthWebappSessions;

    /**
     * The number of OAI-PMH queries answered by local site since last reset
     */
    public int countOaiPmhQueries;

    /**
     * The number of samples returned for OAI-PMH queries to local site since
     * last reset
     */
    public int countOaiPmhQuerySamples;

    /** default constructor */
    public SiteStatisticsInfo() {
        reset();
    }

    /**
     * This is the main constructor since this class is intended to be used
     * entirely to read from and write to the database table 'statistics-site'.
     */
    public SiteStatisticsInfo(ResultSet rs) throws SQLException {
        lastReset = rs.getTimestamp("lastReset");
        countRecipnetdStartups = rs.getInt("recipnetdStartups");
        countRecipnetdShutdowns = rs.getInt("recipnetdShutdowns");
        uptime = rs.getInt("uptime");
        authWebappSessions = rs.getInt("webappSessionsAuth");
        unauthWebappSessions = rs.getInt("webappSessionsUnauth");
        countOaiPmhQueries = rs.getInt("oaiPmhQueries");
        countOaiPmhQuerySamples = rs.getInt("oaiPmhQuerySamples");
    }

    /** Resets all the counters. */
    public void reset() {
        lastReset = new Date();
        countRecipnetdStartups = 0;
        countRecipnetdShutdowns = 0;
        uptime = 0;
        authWebappSessions = 0;
        unauthWebappSessions = 0;
        countOaiPmhQueries = 0;
        countOaiPmhQuerySamples = 0;
    }

    /**
     * Updates a {@code ResultSet} to contain the most recent version of the
     * data.
     */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateTimestamp("lastReset", new Timestamp(lastReset.getTime()));
        rs.updateInt("recipnetdStartups", countRecipnetdStartups);
        rs.updateInt("recipnetdShutdowns", countRecipnetdShutdowns);
        rs.updateInt("uptime", uptime);
        rs.updateInt("webappSessionsAuth", authWebappSessions);
        rs.updateInt("webappSessionsUnauth", unauthWebappSessions);
        rs.updateInt("oaiPmhQueries", countOaiPmhQueries);
        rs.updateInt("oaiPmhQuerySamples", countOaiPmhQuerySamples);
    }
}
