/*
 * Reciprocal Net project
 * 
 * RepositoryHoldingInfo.java
 *
 * 03-Jun-2002: leqian wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 01-Jul-2002: ekoperda renamed class from 'SampleRepositoryInfo'
 *              to 'RepositoryHoldingInfo' and added db access code
 * 12-Aug-2002: ekoperda fixed bug #333
 * 21-Feb-2003: ekoperda added two new constructors: a 4-param and a 5-param
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RepositoryHoldingInfo is a container class that maps onto the database table
 * named 'repositoryHoldings'.
 */
public class RepositoryHoldingInfo implements Serializable {

    public static final int INVALID_REPOSITORY_HOLDING_ID = -1;

    /**
     * Constants used for replicaLevel property.
     */
    public static final int NO_DATA = 0;

    public static final int BASIC_DATA = 100;

    public static final int FULL_DATA = 200;

    /**
     * SampleId is the Id of a row in the samples table. The Sample is stored in
     * this site's repository.
     */
    public int sampleId;

    /**
     * Id of a row in the sites table. Data about this sample is stored in this
     * site's repository.
     */
    public int siteId;

    /**
     * Describes the level of data in the repository. This is especially
     * relevant in a replicated environment, where mirrors might not include
     * every data file that is present on the originating lab's home site.
     * Possible values are defined above as constants.
     */
    public int replicaLevel;

    /**
     * An extension of the site's respositoryUrl string that describes where in
     * the site's repository a particular sample's data directory can be found.
     * To generate a complete URL to a sample's data directory, you take the
     * repositoryUrl from the sites table, append the urlPath from the
     * repositoryContents table, and then append the id string from the samples
     * table. In a default Reciprocal Net installation, the urlPath value will
     * always be null. This field is provided so that sites may opt to organize
     * their data directories into more complex hierarchies, if desired. Values
     * in this field should not begin or end with a slash.
     */
    public String urlPath;

    /** A default constructor is necessary for RMI to work */
    public RepositoryHoldingInfo() {
        this(INVALID_REPOSITORY_HOLDING_ID, SampleInfo.INVALID_SAMPLE_ID,
                SiteInfo.INVALID_SITE_ID, NO_DATA, null);
    }

    /**
     * Constructor that sets the {@code id} field to the value specified.
     */
    public RepositoryHoldingInfo(int id) {
        this(id, SampleInfo.INVALID_SAMPLE_ID, SiteInfo.INVALID_SITE_ID,
                NO_DATA, null);
    }

    /**
     * Constructor that sets the {@code sampleId} and {@code siteId} fields to
     * the values specified.
     */
    public RepositoryHoldingInfo(int sampleId, int siteId) {
        this(INVALID_REPOSITORY_HOLDING_ID, sampleId, siteId, NO_DATA, null);
    }

    /**
     * Constructor that almost completely fills the new object's members, but
     * sets the {@code id} field to its default value.
     */
    public RepositoryHoldingInfo(int sampleId, int siteId, int replicaLevel,
            String urlPath) {
        this(INVALID_REPOSITORY_HOLDING_ID, sampleId, siteId, replicaLevel,
                urlPath);
    }

    /**
     * Constructor that completely fills the new object's members.
     */
    public RepositoryHoldingInfo(int id, int sampleId, int siteId,
            int replicaLevel, String urlPath) {
        this.id = id;
        this.sampleId = sampleId;
        this.siteId = siteId;
        this.replicaLevel = replicaLevel;
        this.urlPath = urlPath;
    }

    /** database primary key */
    public int id;

    /**
     * Constructor to create this object from the current record in a db
     * resultset.
     */
    public RepositoryHoldingInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        sampleId = rs.getInt("sample_id");
        siteId = rs.getInt("site_id");
        replicaLevel = rs.getInt("replicaLevel");
        urlPath = rs.getString("urlPath");
    }

    public void dbStore(ResultSet rs) throws SQLException {
        if (id != INVALID_REPOSITORY_HOLDING_ID) {
            rs.updateInt("id", id);
        }
        rs.updateInt("sample_id", sampleId);
        rs.updateInt("site_id", siteId);
        rs.updateInt("replicaLevel", replicaLevel);
        if (urlPath != null) {
            rs.updateString("urlPath", urlPath);
        } else {
            rs.updateNull("urlPath");
        }
    }
}
