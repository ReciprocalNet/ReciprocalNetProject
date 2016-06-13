/*
 * Reciprocal Net project
 * 
 * LabInfo.java
 *
 * 03-Jun-2002: hclin wrote skeleton
 * 19-Jun-2002: ekoperda added db access code
 * 25-Jun-2002: ekoperda added serialization code
 * 01-Jul-2002: ekoperda added default constructor
 * 09-Jul-2002: ekoperda added directoryName field
 * 16-Jul-2002: ekoperda added 6-param constructor for convenience
 * 08-Aug-2002: jobollin added toString() method
 * 25-Sep-2002: ekoperda added XML serialization code
 * 15-Nov-2002: eisiorho added clonable interface
 * 21-Feb-2003: ekoperda added 1-param constructor and implemented 
 *              ContainerObject interface
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.DomTreeParticipant;
import org.recipnet.site.shared.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * LabInfo is a container class that maps very cleanly onto the database table
 * named 'labs'. It contains basic information about a particular lab in
 * Reciprocal Net.
 */
public class LabInfo implements Cloneable, ContainerObject, DomTreeParticipant,
        Serializable {

    /** Default value for an invalid lab id. */
    public static final int INVALID_LAB_ID = -1;

    /** A centrally-assigned, globally unique identifier for the laboratory. */
    public int id;

    /** True if the lab is active; else false. */
    public boolean isActive;

    /** The full name of the laboratory. */
    public String name;

    /**
     * A possibly abbreviated name for the lab, suitable for informal
     * references.
     */
    public String shortName;

    /**
     * An abbreviated name for the lab that's suitable for constructing
     * directory names in the repository. Should not begin or end with a '/'
     * (the directory separator).
     */
    public String directoryName;

    /** URL to the lab's home page; maybe be null. */
    public String homeUrl;

    /**
     * Every sample generates by this lab will be given this copyright notice by
     * default, though the user may change it on a per-sample basis. May be
     * null.
     */
    public String defaultCopyrightNotice;

    /**
     * id of a row in the sites table that identifies this lab's home site. The
     * home site is where all samples that the lab generates are stored.
     */
    public int homeSiteId;

    /**
     * A default constructor is necessary for this container to be passed by
     * RMI.
     */
    public LabInfo() {
        this(INVALID_LAB_ID, null, null, null, null, SiteInfo.INVALID_SITE_ID);
        isActive = false;
    }

    /**
     * One-param constructor that sets tje od field as specified and sets the
     * other fields to their default (invalid) values.
     */
    public LabInfo(int id) {
        this();
        this.id = id;
    }

    /** Construct an object representing a brand-new lab */
    public LabInfo(int id, String name, String shortName, String directoryName,
            String homeUrl, int homeSiteId) {
        this.id = id;
        this.isActive = true;
        this.name = name;
        this.shortName = shortName;
        this.directoryName = directoryName;
        this.homeUrl = homeUrl;
        this.defaultCopyrightNotice = null;
        this.homeSiteId = homeSiteId;
        ts = 0;
    }

    /**
     * returns a string representation of this object. This implementation
     * returns the object's {@code name} field unless that is {@code null}, in
     * which case it returns an empty string.
     * 
     * @return a {@code String} representation of this object
     */
    @Override
    public String toString() {
        return (name == null) ? "" : name;
    }

    /**
     * Unique serial number used for optimistic locking
     */
    /*
     * TODO: create a new class called CoreLabInfo that extends LabInfo; move
     * the variable below into CoreLabInfo.
     */
    public long ts;

    /**
     * Constructor to create this object from the current record in a database
     * Resultset. This constructor should only ever be called by SiteManager.
     */
    /*
     * TODO: create a new class called CoreLabInfo that extends LabInfo; move
     * the method below into CoreLabInfo.
     */
    public LabInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        isActive = rs.getBoolean("active");
        name = rs.getString("name");
        shortName = rs.getString("shortName");
        directoryName = rs.getString("directoryName");
        homeUrl = rs.getString("homeUrl");
        defaultCopyrightNotice = rs.getString("defaultCopyrightNotice");
        homeSiteId = rs.getInt("homeSite_id");
        ts = rs.getLong("ts");
    }

    /**
     * Store the contents of this object in the current row of the provided
     * databases Resultset. This method should only ever be called by
     * SiteManager.
     */
    /*
     * TODO: create a new class called CoreLabInfo that extends LabInfo; move
     * the method below into CoreLabInfo.
     */

    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("id", id);
        rs.updateBoolean("active", isActive);
        if (name != null) {
            rs.updateString("name", name);
        } else {
            rs.updateNull("name");
        }
        if (shortName != null) {
            rs.updateString("shortName", shortName);
        } else {
            rs.updateNull("shortName");
        }
        if (directoryName != null) {
            rs.updateString("directoryName", directoryName);
        } else {
            rs.updateNull("directoryName");
        }
        if (homeUrl != null) {
            rs.updateString("homeUrl", homeUrl);
        } else {
            rs.updateNull("homeUrl");
        }
        if (defaultCopyrightNotice != null) {
            rs.updateString("defaultCopyrightNotice", defaultCopyrightNotice);
        } else {
            rs.updateNull("defaultCopyrightNotice");
        }
        rs.updateInt("homeSite_id", homeSiteId);
        rs.updateLong("ts", ts);
    }

    /**
     * Store this object in the specified portion of a DOM tree. From interface
     * DomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        if (this.id == INVALID_LAB_ID) {
            throw new IllegalStateException();
        }

        Element labEl = DomUtil.createEl(doc, base, "lab");
        DomUtil.addAttrToEl(labEl, "id", String.valueOf(this.id));

        DomUtil.createTextEl(labEl, "active", String.valueOf(this.isActive));
        if (this.name != null) {
            DomUtil.createTextEl(labEl, "name", this.name);
        }
        if (this.shortName != null) {
            DomUtil.createTextEl(labEl, "shortName", this.shortName);
        }
        if (this.directoryName != null) {
            DomUtil.createTextEl(labEl, "directoryName", this.directoryName);
        }
        if (this.homeUrl != null) {
            DomUtil.createTextEl(labEl, "homeUrl", this.homeUrl);
        }
        if (this.defaultCopyrightNotice != null) {
            DomUtil.createTextEl(labEl, "defaultCopyrightNotice",
                    this.defaultCopyrightNotice);
        }
        DomUtil.createTextEl(labEl, "homeSiteId",
                Integer.toString(this.homeSiteId));

        return base;
    }

    /**
     * Replace the member variables of this object with those obtained from the
     * specified portion of a DOM tree. From interface
     * {@code DomTreeParticipant}.
     */
    public Node extractFromDom(@SuppressWarnings("unused") Document doc,
            Node base) throws SAXException {
        DomUtil.assertNodeName(base, "lab");
        DomUtil.assertAttributeCount(base, 1);
        Element baseEl = (Element) base;
        
        this.id = DomUtil.getAttrForElAsInt(baseEl, "id");

        // Get the other member variables
        this.isActive = DomUtil.getTextForElAsBoolean(baseEl, "active");
        this.name = DomUtil.getTextForEl(baseEl, "name", false);
        this.shortName = DomUtil.getTextForEl(baseEl, "shortName", false);
        this.directoryName = DomUtil.getTextForEl(baseEl, "directoryName",
                false);
        this.homeUrl = DomUtil.getTextForEl(baseEl, "homeUrl", false);
        this.defaultCopyrightNotice = DomUtil.getTextForEl(baseEl,
                "defaultCopyrightNotice", false);
        this.homeSiteId = DomUtil.getTextForElAsInt(baseEl, "homeSiteId");
        
        return base;
    }

    /**
     * <p>
     * Creates a new {@code LabInfo} different from this one but equivalent to
     * it
     * </p><p>
     * <strong>NOTE:</strong> Because this class does not override
     * {@code Object.equals()}, instances and their clones will be inequal
     * despite (initially) having the same values for all fields.
     * </p>
     * 
     * @return the clone
     */
    @Override
    public LabInfo clone() {
        try {
            return (LabInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Can't happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }
}
