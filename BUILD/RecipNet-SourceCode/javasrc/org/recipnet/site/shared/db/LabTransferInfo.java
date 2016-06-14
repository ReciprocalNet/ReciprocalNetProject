/*
 * Reciprocal Net project
 * LabTransferInfo.java
 *
 * 01-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.site.shared.db;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * LabTransferInfo is a database container class that represents the contents
 * of a single row in the table named 'labTransfers'.
 */
public class LabTransferInfo {
    /**
     * Primary key; equivalently called the labTransferId.
     */
    public long id;

    /**
     * Foreign key that identifies the lab to which this object pertains.
     */
    public int labId;

    /**
     * Foreign key that identifies the site that formerly hosted the lab.
     */
    public int previousHomeSiteId;

    /**
     * Foreign key that identifies the site that will host the lab at the
     * completion of the transfer operation.
     */
    public int newHomeSiteId;
    
    /**
     * False if the transfer has been initiated/announced but is not yet 
     * complete; true if the transfer is complete.
     */
    public boolean complete;

    /** default constructor */
    public LabTransferInfo() {
	this.id = InterSiteMessage.INVALID_SEQ_NUM;
	this.labId = LabInfo.INVALID_LAB_ID;
	this.previousHomeSiteId = SiteInfo.INVALID_SITE_ID;
	this.newHomeSiteId = SiteInfo.INVALID_SITE_ID;
	this.complete = false;
    }

    /**
     * This is the main constructor since this class is intended to be used
     * entirely to read from and write to the database table 'labTransfers'.
     */
    public LabTransferInfo(ResultSet rs) throws SQLException {
	this.id = rs.getLong("id");
	this.labId = rs.getInt("lab_id");
	this.previousHomeSiteId = rs.getInt("previous_homeSite_id");
	this.newHomeSiteId = rs.getInt("new_homeSite_id");
	this.complete = rs.getBoolean("complete");
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
	rs.updateLong("id", this.id);
	rs.updateInt("lab_id", this.labId);
	rs.updateInt("previous_homeSite_id", this.previousHomeSiteId);
	rs.updateInt("new_homeSite_id", this.newHomeSiteId);
	rs.updateBoolean("complete", this.complete);
    }
}
