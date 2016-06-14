/*
 * Reciprocal Net project
 * 
 * ProviderInfo.java
 *
 * 03-Jun-2002: hclin wrote skeleton
 * 21-Jun-2002: ekoperda added db access code
 * 25-Jun-2002: ekoperda added serialization code
 * 08-Aug-2002: jobollin added toString() method
 * 09-Aug-2002: ekoperda fixed serialVersionUID to compensate for toString()
 *              method
 * 25-Sep-2002: ekoperda added XML serialization code
 * 25-Oct-2002: adharurk implemented the Comparable Interface
 * 15-Nov-2002: eisiorho added clonable interface
 * 21-Feb-2003: ekoperda added 1-param constructor and implemented
 *              ContainerObject interface
 * 19-Mar-2003: ekoperda fixed bug 788 in compareTo()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 11-May-2006: jobollin made clone() a little more friendly; reformatted the
 *              source; removed unused imports
 * 31-May-2006: jobollin performed minor cleanup
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
 * ProviderInfo is a container class that maps very cleanly onto the database
 * table named 'providers'. It contains basic information about a particular
 * provider in Reciprocal Net.
 */
public class ProviderInfo implements Cloneable, Comparable<ProviderInfo>,
        ContainerObject, DomTreeParticipant, Serializable {
    /** Default value for an invalid provider id. */
    public static final int INVALID_PROVIDER_ID = -1;

    /** A centrally-assigned, globally unique identifier for the provider. */
    public int id;

    /** labId identifies the lab this provider account is associated with. */
    public int labId;

    /**
     * 1 if the sample provider is actively cooperating with the lad, 0 if not.
     */
    public boolean isActive;

    /** Descriptive name for the sample provider. */
    public String name;

    /** Name of the person who heads this research group. */
    public String headContact;

    /**
     * User provided text comments about this provider. May be null.
     */
    public String comments;

    /**
     * A default constructor is necessary for this class to be passed by RMI.
     */
    public ProviderInfo() {
        id = INVALID_PROVIDER_ID;
        labId = LabInfo.INVALID_LAB_ID;
        isActive = false;
        name = null;
        headContact = null;
        comments = null;
    }

    /**
     * One-param constructor that sets the id field as specified and sets the
     * other fields to their default (invalid) values.
     */
    public ProviderInfo(int id) {
        this();
        this.id = id;
    }

    /**
     * returns a string representation of this object. This implementation
     * returns the {@code name} field unless that is {@code null},
     * in which case it returns an empty string.
     * 
     * @return a {@code String} representation of this object
     */
    @Override
    public String toString() {
        return (name == null) ? "" : name;
    }

    /**
     * from interface Comparable; sorts these objects in alphabetical order by
     * {@code name}.
     */
    public int compareTo(ProviderInfo p) {
        if (this.name == null) {
            return (p.name == null) ? 0 : -1;
        } else if (p.name == null) {
            return 1;
        } else {
            return (this.name.compareTo(p.name));
        }
    }

    /*
     * TODO: create a new class called CoreProviderInfo that extends
     * ProviderInfo. Then, take everything that appears below this line and move
     * it to that class. Only Site Manager needs access to the variables and
     * methods found below.
     */

    /** Unique serial number used for optimistic locking */
    public long ts;

    /**
     * Constructor used to create this object from the current record in a db
     * resultset
     */
    public ProviderInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        labId = rs.getInt("lab_id");
        isActive = rs.getBoolean("active");
        name = rs.getString("name");
        headContact = rs.getString("headContact");
        comments = rs.getString("comments");
        ts = rs.getLong("ts");
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("id", id);
        rs.updateInt("lab_id", labId);
        rs.updateBoolean("active", isActive);
        if (name != null) {
            rs.updateString("name", name);
        } else {
            rs.updateNull("name");
        }
        if (headContact != null) {
            rs.updateString("headContact", headContact);
        } else {
            rs.updateNull("headContact");
        }
        if (comments != null) {
            rs.updateString("comments", comments);
        } else {
            rs.updateNull("comments");
        }
        rs.updateLong("ts", ts);
    }

    /**
     * Store this object in the specified portion of a DOM tree. From interface
     * DomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        if (this.id == INVALID_PROVIDER_ID) {
            throw new IllegalStateException();
        }

        Element provEl = DomUtil.createEl(doc, base, "provider");
        
        DomUtil.addAttrToEl(provEl, "id", Integer.toString(this.id));
        DomUtil.createTextEl(provEl, "labId", Integer.toString(this.labId));
        DomUtil.createTextEl(provEl, "active", Boolean.toString(this.isActive));
        if (this.name != null) {
            DomUtil.createTextEl(provEl, "name", this.name);
        }
        if (this.headContact != null) {
            DomUtil.createTextEl(provEl, "headContact", this.headContact);
        }
        if (this.comments != null) {
            DomUtil.createTextEl(provEl, "comments", this.comments);
        }

        return base;
    }

    /**
     * Replace the member variables of this object with those obtained from the
     * specified portion of a DOM tree. From interface DomTreeParticipant.
     */
    public Node extractFromDom(@SuppressWarnings("unused") Document doc,
            Node base) throws SAXException {
        DomUtil.assertNodeName(base, "provider");
        DomUtil.assertAttributeCount(base, 1);
        Element baseEl = (Element) base;
        
        this.id = DomUtil.getAttrForElAsInt(baseEl, "id");

        // Get the other member variables
        this.labId = DomUtil.getTextForElAsInt(baseEl, "labId");
        this.isActive = DomUtil.getTextForElAsBoolean(baseEl, "active");
        this.name = DomUtil.getTextForEl(baseEl, "name", false);
        this.headContact = DomUtil.getTextForEl(baseEl, "headContact", false);
        this.comments = DomUtil.getTextForEl(baseEl, "comments", false);
        
        return base;
    }

    /**
     * <p>
     * Returns a new {@code ProviderInfo} distinct from this one but having
     * equal members.
     * </p><p>
     * <strong>NOTE:</strong> Because this class does not override
     * {@code Object.equals()}, instances and their clones will be inequal
     * despite (initially) having the same values for all fields.
     * </p>
     * 
     * @return a clone of this {@code ProviderInfo}
     */
    @Override
    public ProviderInfo clone() {
        try {
            return (ProviderInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Can't happen because ProviderInfo is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }
}
