/*
 * Reciprocal Net project
 * 
 * SiteInfo.java
 *
 * 29-May-2002: ekoperda wrote skeleton
 * 21-Jun-2002: ekoperda added db access code
 * 25-Jun-2002: ekoperda added serialization code
 * 15-Jul-2002: ekoperda added publicKey field and supporting code
 * 25-Sep-2002: ekoperda added XML serialization code, plus new fields
 *              publicSeqNum and privateSeqNum
 * 15-Nov-2002: eisiorho added clonable interface.
 * 21-Feb-2003: ekoperda added 1-param constructor and implemented 
 *              ContainerObject interface
 * 18-Mar-2003: midurbin added equals()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 20-Feb-2006: ekoperda made class Comparable by site id
 * 31-May-2006: jobollin reformatted the source; fixed the signature of the
 *              equals() method
 * 10-Jan-2008: ekoperda altered extractFromDom() to match changes in DomUtil
 * 26-Nov-2008: ekoperda added fields isActive and finalSeqNum
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.DomTreeParticipant;
import org.recipnet.site.shared.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * SiteInfo is a container class that maps very cleanly onto the database table
 * named 'sites'. It contains basic information about a particular site in
 * Reciprocal Net.
 */
public class SiteInfo implements Cloneable, Comparable<SiteInfo>,
        ContainerObject, DomTreeParticipant, Serializable {

    /** Default value for an invalid site id. */
    public static final int INVALID_SITE_ID = -1;

    /**
     * This must match the constant specified in InterSiteMessage. We cannot
     * use the constant from InterSiteMessage because our build process's
     * dependency checking needs to go the other way.
     */
    public static final long INVALID_SEQ_NUM = -1;

    /** A centrally-assigned, globally unique identifier for the site. */
    public int id;

    /**
     * The full name of the site or the group that hosts the site. This text
     * should be pretty, like 'Indiana University Molecular Structure Center'.
     */
    public String name;

    /**
     * A possibly abbreviated name for the site, suitable for informal
     * references.
     */
    public String shortName;

    /**
     * URL of the web root of the recipnet installation on this site. Normally
     * this will be http://servername.com/recipnet/ . Note that this URL does
     * not include a page name. Must end with a trailing '/'.
     */
    public String baseUrl;

    /**
     * URL base of the HTTP-accessible data file repository at this site.
     * Normally this will be http://servername.com/recipnet/data/ . Data files
     * for samples can be found in directories under this one. Must end with a
     * trailing '/'.
     */
    public String repositoryUrl;

    /** For verification of digital signatures on ISM's and such. */
    public PublicKey publicKey;

    /** default constructor */
    public SiteInfo() {
        this(INVALID_SITE_ID, null, null, null, null, null);
    }

    /**
     * One-param constructor that sets the id field as specified and sets the
     * other fields to their default (invalid) values.
     */
    public SiteInfo(int id) {
        this(id, null, null, null, null, null);
    }

    /** Creates an object from a few parameters; leaves the URL's null */
    public SiteInfo(int id, String name, String shortName, 
            PublicKey publicKey) {
        this(id, name, shortName, null, null, publicKey);
    }

    /** Creates a complete object from parameters */
    public SiteInfo(int id, String name, String shortName, String baseUrl,
            String repositoryUrl, PublicKey publicKey) {
        this.id = id;
	this.isActive = true;
        this.name = name;
        this.shortName = shortName;
        this.baseUrl = baseUrl;
        this.repositoryUrl = repositoryUrl;
        this.publicKey = publicKey;
        this.ts = 0;
        this.publicSeqNum = INVALID_SEQ_NUM;
        this.privateSeqNum = INVALID_SEQ_NUM;
	this.finalSeqNum = INVALID_SEQ_NUM;
    }

    /**
     * Store this object in the specified portion of a DOM tree. From interface
     * DomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        if (this.id == INVALID_SITE_ID) {
            throw new IllegalStateException();
        }

        Element siteEl = DomUtil.createEl(doc, base, "site");
        DomUtil.addAttrToEl(siteEl, "id", Integer.toString(this.id));

        if (this.name != null) {
            DomUtil.createTextEl(siteEl, "name", this.name);
        }

        if (this.shortName != null) {
            DomUtil.createTextEl(siteEl, "shortName", this.shortName);
        }

        if (this.baseUrl != null) {
            DomUtil.createTextEl(siteEl, "baseUrl", this.baseUrl);
        }

        if (this.repositoryUrl != null) {
            DomUtil.createTextEl(siteEl, "repositoryUrl", this.repositoryUrl);
        }
        Element publicKeyEl = DomUtil.createTextElWithBinaryData(siteEl,
                "publicKey", this.publicKey.getEncoded());
        DomUtil.addAttrToEl(publicKeyEl, "algorithm",
                this.publicKey.getAlgorithm());
        DomUtil.addAttrToEl(publicKeyEl, "format", this.publicKey.getFormat());

        return base;
    }

    /**
     * Replace the member variables of this object with those obtained from the
     * specified portion of a DOM tree. From interface DomTreeParticipant.
     */
    public Node extractFromDom(@SuppressWarnings("unused") Document doc,
            Node base) throws SAXException {
        try {
            // Get the root <site> element and its one attribute
            DomUtil.assertNodeName(base, "site");
            DomUtil.assertAttributeCount(base, 1);
            Element baseEl = (Element) base;
            this.id = DomUtil.getAttrForElAsInt(baseEl, "id");

            // Get the other member variables
            this.name = DomUtil.getTextForEl(baseEl, "name", false);
            this.shortName = DomUtil.getTextForEl(baseEl, "shortName", false);
            this.baseUrl = DomUtil.getTextForEl(baseEl, "baseUrl", false);
            this.repositoryUrl = DomUtil.getTextForEl(baseEl, "repositoryUrl",
                    false);

            // Get the <publicKey> element and its three attributes
            Element keyEl 
                    = DomUtil.findSingleElement(baseEl, "publicKey", true);
            String keyAlgorithm 
                    = DomUtil.getAttrForEl(keyEl, "algorithm", true);
            if (!keyAlgorithm.equals("DSA")) {
                throw new SAXNotSupportedException("Site key algorithm '"
                        + keyAlgorithm + "' not supported");
            }
            String keyFormat = DomUtil.getAttrForEl(keyEl, "format", true);
            if (!keyFormat.equals("X.509")) {
                throw new SAXNotSupportedException("Site key format '"
                        + keyFormat + "' not supported");
            }
            byte[] rawKey = DomUtil.getTextForElAsBytes(keyEl);
            X509EncodedKeySpec specKey = new X509EncodedKeySpec(rawKey);
            KeyFactory factory = KeyFactory.getInstance("DSA");
            this.publicKey = factory.generatePublic(specKey);
        } catch (NoSuchAlgorithmException ex) {
            throw new SAXException("Site key algorithm not available locally",
                    ex);
        } catch (InvalidKeySpecException ex) {
            throw new SAXException("Invalid site key", ex);
        }

        return base;
    }

    /**
     * Creates a new {@code SiteInfo} equal to this one but stinct from it
     * 
     * @return the clone
     */
    @Override
    public SiteInfo clone() {
        try {
            return (SiteInfo) super.clone();
        } catch (CloneNotSupportedException ex) {
            // Can't happen because this class is cloneable.
            throw new UnexpectedExceptionException(ex);
        }
    }

    /**
     * Determines whether this {@code SiteInfo} is equal to the specified
     * object, which is the case if the object is also a {@code SiteInfo} and
     * has the same ID.
     * 
     * @param o the {@code Object} to compare to this one
     * @return {@code true} if the specified object is equal to this one,
     *         {@code false} if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof SiteInfo) {
            return (this.id == ((SiteInfo) o).id);
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this object consistent with its
     * {@link #equals(Object)} method
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return this.id;
    }

    /** Implements Comparable. Consistent with {@link #equals(Object)}. */
    public int compareTo(SiteInfo x) {
        return this.id - x.id;
    }

    /*
     * TODO: create a new class called CoreSiteInfo that extends SiteInfo.
     * Then, take everything that appears below this line and move it to that
     * class.  Only Site Manager needs access to the variables and methods
     * found below.
     */

    /** Unique serial number used for optimistic locking */
    public long ts;

    /**
     * The sequence number of the most recent public ISM originated by this
     * site that has been received and accepted by the local site.
     */
    public long publicSeqNum;

    /**
     * The sequence number of the most recent private ISM originated by this
     * site that has been received and accepted by the local site.
     */
    public long privateSeqNum;

    /**
     * The sequence number of the final ISM originated by this site.  Any ISM's
     * received with a higher sequence number are to be dropped.  This field
     * makes sense in either of the following cases:
     *    * When associated with a deactivated site (i.e. isActive==false).
     *      In this case, either finalSeqNum==publicSeqNum or
     *      finalSeqNum==privateSeqNum.
     *    * When associated with a site that is preparing to be deactivated.
     *      At this moment, isActive==true, finalSeqNum&gt;publicSeqNum, and
     *      finalSeqNum&gt;privateSeqNum.
     * Set this field to INVALID_SEQUENCE_NUMBER in other cases, or for
     * deactivated sites where the final sequence number is not known.
     */
    public long finalSeqNum;

    /**
     * True if the site is active; else false.  Note that unlike some other 
     * container objects, here on SiteInfo the isActive field behaves like a
     * local summary.  The local site sets this field in the course of its own
     * processing, and this field is persisted in the database, but this
     * field's value is not directly propagated between sites (via ISM's or
     * other means.) 
     */
    public boolean isActive;

    /**
     * Constructor to create this object from the current record in a db
     * resultset
     */
    public SiteInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
	isActive = rs.getBoolean("active");
        name = rs.getString("name");
        shortName = rs.getString("shortName");
        baseUrl = rs.getString("baseUrl");
        repositoryUrl = rs.getString("repositoryUrl");
        publicKey = null;

        try {
            byte[] encodedPublicKey = rs.getBytes("publicKey");
            X509EncodedKeySpec specPublicKey = new X509EncodedKeySpec(
                    encodedPublicKey);
            KeyFactory factory = KeyFactory.getInstance("DSA");
            publicKey = factory.generatePublic(specPublicKey);
        } catch (NoSuchAlgorithmException ex) {
            publicKey = null;
        } catch (InvalidKeySpecException ex) {
            publicKey = null;
        }

        ts = rs.getLong("ts");
        publicSeqNum = rs.getLong("publicSeqNum");
        if (rs.wasNull()) {
            publicSeqNum = INVALID_SEQ_NUM;
        }
        privateSeqNum = rs.getLong("privateSeqNum");
        if (rs.wasNull()) {
            privateSeqNum = INVALID_SEQ_NUM;
        }
	finalSeqNum = rs.getLong("finalSeqNum");
	if (rs.wasNull()) {
	    finalSeqNum = INVALID_SEQ_NUM;
	}
    }

    /** Store this object in the current row of the provided db resultset */
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
        if (baseUrl != null) {
            rs.updateString("baseUrl", baseUrl);
        } else {
            rs.updateNull("baseUrl");
        }
        if (repositoryUrl != null) {
            rs.updateString("repositoryUrl", repositoryUrl);
        } else {
            rs.updateNull("repositoryUrl");
        }

        byte[] encodedPublicKey = publicKey.getEncoded();
        rs.updateBytes("publicKey", encodedPublicKey);

        rs.updateLong("ts", ts);
        if (publicSeqNum != INVALID_SEQ_NUM) {
            rs.updateLong("publicSeqNum", publicSeqNum);
        } else {
            rs.updateNull("publicSeqNum");
        }
        if (privateSeqNum != INVALID_SEQ_NUM) {
            rs.updateLong("privateSeqNum", privateSeqNum);
        } else {
            rs.updateNull("privateSeqNum");
        }
	if (finalSeqNum != INVALID_SEQ_NUM) {
	    rs.updateLong("finalSeqNum", finalSeqNum);
	} else {
	    rs.updateNull("finalSeqNum");
	}
    }
}
