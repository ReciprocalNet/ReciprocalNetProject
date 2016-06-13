/*
 * Reciprocal Net project
 * 
 * LabStatisticsInfo.java
 *
 * 10-Jul-2003: midurbin wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LabStatisticsInfo is a container class that maps very cleanly only the
 * database table named 'statisticsLabs'. It contains the lab specific
 * statistical information that must persist through recipnetd shutdowns that
 * cannot be efficiently derived from other data.
 */
public class LabStatisticsInfo {

    /** id of the lab for which this class contains statistics */
    public int labId;

    /**
     * The number of times pages displaying information on a particular sample
     * has been viewed for samples belonging to the lab specified by {@code 
     * labId} since the counter was reset.
     */
    public int showSamplePageViews;

    /**
     * The number of times a page where sample data can be edited has been
     * viewed for samples belonging to the lab specified by {@code labId} since
     * the counter was reset.
     */
    public int editSamplePageViews;

    /**
     * Default constructor.
     */
    public LabStatisticsInfo() {
        this.labId = LabInfo.INVALID_LAB_ID;
        reset();
    }

    /**
     * This is the main constructor since this class is intended to be used
     * entirely to read from and write to the database table 'statistics-lab'.
     */
    public LabStatisticsInfo(ResultSet rs) throws SQLException {
        this.labId = rs.getInt("lab_id");
        this.showSamplePageViews = rs.getInt("showSampleViews");
        this.editSamplePageViews = rs.getInt("editsampleViews");
    }

    /** Sets all counters back to zero. */
    public final void reset() {
        this.showSamplePageViews = 0;
        this.editSamplePageViews = 0;
    }

    /**
     * Updates a {@code ResultSet} to contain the most recent version of the
     * data.
     */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("lab_id", labId);
        rs.updateInt("showSampleViews", showSamplePageViews);
        rs.updateInt("editSampleViews", editSamplePageViews);
    }
}
