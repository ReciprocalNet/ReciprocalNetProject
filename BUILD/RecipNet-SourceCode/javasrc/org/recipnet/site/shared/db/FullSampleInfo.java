/*
 * Reciprocal Net project
 * 
 * FullSampleInfo.java
 *
 * 29-May-2002: leqian wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 07-Aug-2002: ekoperda fixed bug #288 by changing name of status field to
 *              mostRecentStatus
 * 27-Jan-2003: adharurk changed comments to reflect new status constants 
 * 21-Feb-2003: ekoperda added 1-param convenience constructor
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 08-Aug-2004: cwestnea modified the constructor to use SampleWorkflowBL
 * 06-Oct-2005: midurbin added 'mostRecentProviderId' field
 * 17-Feb-2006: jobollin added type parameters where appropriate
 */

package org.recipnet.site.shared.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * FullSampleInfo is a container class that contains all HistoryInfo objects
 * (will be packed in historyInfos property) which relative to a sample. This
 * container also includes some key properties from SampleInfo container for
 * convience.
 */
public class FullSampleInfo implements Serializable {

    /** Sample's id. */
    public int id;

    /** Lab's id */
    public int labId;

    /** Id for the Provider associated with the current version of the sample */
    public int mostRecentProviderId;

    /** Local lab's name. */
    public String localLabId;

    /**
     * The workflow state code for this sample. The possible values are defined
     * in SampleInfo.
     */
    public int mostRecentStatus;

    /** HistoryID of the current version of the sample. */
    public int mostRecentHistoryId;

    /**
     * A map of SampleHistoryInfo objects which are mapped to sampleHistory
     * table
     */
    public List<SampleHistoryInfo> history;

    /**
     * Create an emtpy object
     */
    public FullSampleInfo() {
        id = SampleInfo.INVALID_SAMPLE_ID;
        labId = LabInfo.INVALID_LAB_ID;
        mostRecentProviderId = ProviderInfo.INVALID_PROVIDER_ID;
        localLabId = null;
        mostRecentStatus = SampleWorkflowBL.INVALID_STATUS;
        mostRecentHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        history = new ArrayList<SampleHistoryInfo>();
    }

    /**
     * Initializes a new {@code FullSampleInfo}, assigning it the specified
     * sample ID
     * 
     * @param  id the sample ID to assign
     */
    public FullSampleInfo(int id) {
        this();
        this.id = id;
    }

    /**
     * Create a new object that matches the current row in the caller-provided
     * database {@code ResultSet}.
     * 
     * @param  rs the {@code ResultSet} from which to populate this
     *         {@code FullSampleInfo}
     *         
     * @throws SQLException if reading fields from the provided
     *         {@code ResultSet} fails
     */
    /*
     * TODO: create a new class called CoreSampleInfo that extends this one;
     * move this function to the new class
     */
    public FullSampleInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        labId = rs.getInt("lab_id");
        mostRecentProviderId = rs.getInt("current_provider_id");
        localLabId = rs.getString("localLabId");
        mostRecentStatus = rs.getInt("status");
        mostRecentHistoryId = rs.getInt("current_sampleHistory_id");
        history = new ArrayList<SampleHistoryInfo>();
    }
}
