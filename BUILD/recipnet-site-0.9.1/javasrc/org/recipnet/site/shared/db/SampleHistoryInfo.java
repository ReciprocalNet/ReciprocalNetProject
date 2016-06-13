/*
 * Reciprocal Net project
 * 
 * SampleHistoryInfo.java
 *
 * 30-May-2002: leqian wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 19-Jul-2002: jobollin adjusted workflow action constants to agree with
 *              the design documents
 * 12-Aug-2002: ekoperda fixed bug #290
 * 18-Sep-2002: ekoperda added workflow action constants IMPORTED and 
 *              DB_REBUILD
 * 14-Nov-2002: eisiorho added new workflow action MODIFIED_LTAS
 * 27-Jan-2003: adharurk added action constants to accommodate new workflow
 * 07-Feb-2003: ekoperda added REPLICATED_FROM_ELSEWHERE action code
 * 25-Apr-2003: ekoperda added action codes SUBMITTED_CORRECT,
 *              PRELIMINARY_DATA_COLLECTED_CORRECTED, 
 *              STRUCTURE_REFINED_CORRECTED, RELEASED_TO_PUBLIC_CORRECTED,
 *              DECLARED_INCOMPLETE_CORRECTED, DECLARED_NON_SCS_CORRECTED,
 *              FAILED_TO_COLLECT_CORRECTED, and 
 *              WITHDRAWN_BY_PROVIDER_CORRECTED
 * 25-Apr-2003: ekoperda removed clientIp field and supporting code
 * 23-May-2003: ekoperda added action codes DETECTED_PREEXISTING_FILES,
 *              DETECTED_FILE_CHANGES, FILE_ADDED, FILE_ADDED_IMPLICITLY,
 *              FILE_RENAMED, FILE_REPLACED, FILE_REMOVED, FILE_ERADICATED
 * 05-Jun-2003: midubin added action codes CITATION_ADDED_CORRECTED,
 *              SUSPENDED_CORRECTED, RESUMED_CORRECTED
 * 24-Jun-2003: midurbin changed RAW_EDITED action code to
 *              DEPRECATED_RAW_EDITED.
 * 03-Jul-2003: ekoperda added action code 
 *              SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 18-Aug-2004: cwestnea moved action codes to SampleWorkflowBL and made 
 *              changes throughout to use it
 * 05-Jan-2005: midurbin added revertedToSampleHistoryId and supporting code
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * SampleInfo is a container class that maps very cleanly onto the database
 * table named 'sampleHistory'. It contains all sample historic infomation for a
 * certain sample.
 */
public class SampleHistoryInfo implements Serializable {
    
    public static final int INVALID_SAMPLE_HISTORY_ID = -1;

    public static final int STILL_ACTIVE = -1;

    /** This history id */
    public int id;

    /** Sample's id in "sample" table */
    public int sampleId;

    /**
     * Action's ID. A number that identifies the kind of workflow action that
     * was taken. Must be one of the constants defined on
     * {@code SampleWorkflowBL}.
     */
    public int action;

    /**
     * The new status of the sample after this action was taken. Must be one of
     * the constants defined on {@code SampleWorkflowBL}.
     */
    public int newStatus;

    /** Date */
    public Date date;

    /**
     * user's id. If this value is null in the table, it should have value
     * INVALID_USER_ID.
     */
    public int userId;

    /**
     * User provided comments for future reference. This text might explain why
     * a particular workflow action was necessary or appropriate, for example,
     * and will be visible during any future sample audit.
     */
    public String comments;

    /**
     * If this {@code SampleHistoryInfo} represents a revert action, this will
     * indicate the sample history id whose sample version was the target of
     * this reversion. Otherwise this value will be null in the table and have
     * the value {@code INVALID_SAMPLE_HISTORY_ID}.
     */
    public int revertedToSampleHistoryId;

    /** The SampleInfo object connected to the sample */
    public SampleInfo sample;

    /** Create an empty object */
    public SampleHistoryInfo() {
        id = INVALID_SAMPLE_HISTORY_ID;
        sampleId = SampleInfo.INVALID_SAMPLE_ID;
        action = SampleWorkflowBL.INVALID_ACTION;
        newStatus = SampleWorkflowBL.INVALID_STATUS;
        date = null;
        userId = UserInfo.INVALID_USER_ID;
        comments = null;
        revertedToSampleHistoryId = INVALID_SAMPLE_HISTORY_ID;
    }

    /*
     * TODO: create a new class called CoreSampleHistoryInfo that extends
     * SampleHistoryInfo. Then, take everything that appears below this line and
     * move it to that class. Only Sample Manager needs access to the variables
     * and methods found below.
     */

    /**
     * Constructor to create this object from the current record in a db
     * resultset
     */
    public SampleHistoryInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        sampleId = rs.getInt("sample_id");
        action = rs.getInt("action");
        newStatus = rs.getInt("newStatus");
        date = rs.getTimestamp("date");
        userId = rs.getInt("user_id");
        if (rs.wasNull()) {
            userId = UserInfo.INVALID_USER_ID;
        }
        comments = rs.getString("comments");
        revertedToSampleHistoryId = rs.getInt("revertedToSampleHistoryId");
        if (rs.wasNull()) {
            revertedToSampleHistoryId = INVALID_SAMPLE_HISTORY_ID;
        }
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        if (id != INVALID_SAMPLE_HISTORY_ID) {
            rs.updateInt("id", id);
        } else {
            rs.updateNull("id");
        }
        rs.updateInt("sample_id", sampleId);
        rs.updateInt("action", action);
        rs.updateInt("newStatus", newStatus);
        rs.updateTimestamp("date", new Timestamp(date.getTime()));
        if (userId != UserInfo.INVALID_USER_ID) {
            rs.updateInt("user_id", userId);
        } else {
            rs.updateNull("user_id");
        }
        if (comments != null) {
            rs.updateString("comments", comments);
        } else {
            rs.updateNull("comments");
        }
        if (revertedToSampleHistoryId != INVALID_SAMPLE_HISTORY_ID) {
            rs.updateInt("revertedToSampleHistoryId",
                    revertedToSampleHistoryId);
        } else {
            rs.updateNull("revertedToSampleHistoryId");
        }
    }
}
