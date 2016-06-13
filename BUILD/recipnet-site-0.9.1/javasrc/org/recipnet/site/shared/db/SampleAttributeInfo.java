/*
 * Reciprocal Net project
 * 
 * SampleAttributeInfo.java
 *
 * 30-May-2002: leqian wrote first draft
 * 18-Jun-2002: leqian wrote second draft
 * 26-Jun-2002: ekoperda added db access code
 * 08-Jul-2002: ekoperda added 2-param constructor
 * 10-Nov-2002: nisheth added XML serialization code
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 14-Jun-2004: ekoperda implemented ExtendedDomTreeParticipant by adding
 *              insertIntoDomUsingResources() and updating insertIntoDom()
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 27-Oct-2005: jobollin added a clone() method overriding the superclass' and
 *              making use of a covariant return type
 * 27-Oct-2005: jobollin pulled equals() down from the superclass and
 *              implemented a consistent hashCode() method
 * 31-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.shared.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.ExtendedDomTreeParticipant;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * SampleAttributeInfo is a container class that maps very cleanly onto the
 * database table named 'sampleAttribute'.
 */
public class SampleAttributeInfo extends SampleTextInfo implements
        ExtendedDomTreeParticipant {

    /** Create an emtpy object */
    public SampleAttributeInfo() {
        // Nothing to do
    }

    /**
     * Create an object with the specified type and value. (Use this when adding
     * an attribute to an existing SampleInfo object.)
     */
    public SampleAttributeInfo(int type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Create an object based upon the current row in the provided resultset
     */
    public SampleAttributeInfo(ResultSet rs) throws SQLException {
        super(rs);
    }

    /**
     * Store this object in the specified portion of a DOM tree. The function
     * has no effect if the current attribute is a localtracking attribute
     * because localtracking attributes are not to be replicated via XML. From
     * interface ExtendedDomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        return insertIntoDomUsingResources(doc, base, null);
    }

    /**
     * Store this object in the specified portion of a DOM tree using a
     * caller-specified resource bundle. The function has no effect if the
     * current attribute is a localtracking attribute because localtracking
     * attributes are not to be replicated via XML. From interface
     * ExtendedDomTreeParticipant.
     */
    public Node insertIntoDomUsingResources(
            @SuppressWarnings("unused") Document doc, Node base,
            ResourceBundle resources) {
        if (type < SampleTextBL.LOCAL_TRACKING_BASE) {
            Element attributeEl = DomUtil.createTextEl(base, "attribute",
                    this.value);
            DomUtil.addAttrToEl(attributeEl, "type",
                    Integer.toString(this.type));
            if (resources != null) {
                // Store a textual description of the type.
                DomUtil.addAttrToEl(attributeEl, "description",
                        resources.getString("field" + this.type));
            }
            return attributeEl;
        }
        return base;
    }

    /**
     * Replace the member variables of this object with those obtained from the
     * specified portion of a DOM tree. From interface
     * ExtendedDomTreeParticipant.
     */
    public Node extractFromDom(@SuppressWarnings("unused") Document doc,
            Node base) throws SAXException {
        Element attributeEl = (Element) base;

        DomUtil.assertNodeName(base, "attribute");
        this.type = DomUtil.getAttrForElAsInt(attributeEl, "type");
        this.value = DomUtil.getTextForEl(attributeEl, true);

        return attributeEl;
    }

    @Override
    public SampleAttributeInfo clone() {
        return (SampleAttributeInfo) super.clone();
    }

    /**
     * Determines whether the specified object is equal to this one, which is
     * true if and only if it is also a {@code SampleAttributeInfo} and has the
     * same type and an equal value.
     * 
     * @param x the {@code Object} to compare with this one
     * @return {@code true} if the specified object is equal to this one,
     *         otherwise {@code false}
     */
    @Override
    public boolean equals(Object x) {
        if (this == x) {
            return true;
        } else if (x instanceof SampleTextInfo) {
            SampleTextInfo y = (SampleTextInfo) x;

            return ((this.type == y.type) && SampleInfo.compareReferences(
                    this.value, y.value));
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this object that is consistent with its
     * {@link #equals(Object)} method -- that is, any
     * {@code SampleAttributeInfo} that is equal to this one will have the same
     * hash code. This code does, therefore, depend on the internal state of
     * this object, and thus it will change if the object is modified. That
     * makes instances potentially unsafe for use in {@code HashSet}s and as
     * keys to {@code HashMap}s.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (value.hashCode() ^ type);
    }
}
