/*
 * Reciprocal Net project
 * 
 * SampleAnnotationInfo.java
 *
 * 03-Jun-2002: leqian wrote first draft
 * 18-Jun-2002: leqian wrote second draft
 * 26-Jun-2002: ekoperda added db access code
 * 08-Jul-2002: ekoperda added 2-param constructor
 * 12-Sep-2002: ekoperda added additional LEVEL constants
 * 17-Sep-2002: ekoperda fixed bug #435 in 2-param constructor
 * 10-Nov-2002: nisheth added XML serialization code
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 14-Jun-2004: ekoperda implemented ExtendedDomTreeParticipant by adding
 *              insertIntoDomUsingResources() and updating insertIntoDom()
 * 27-Sep-2004: midurbin fixed bug #1398 in equals()
 * 27-Oct-2005: jobollin added a hashCode() method consistent with equals(),
 *              added a clone() method overriding the superclass' and making use
 *              of a covariant return type, and reformatted the code to remove
 *              tab characters
 * 31-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.shared.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.ExtendedDomTreeParticipant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * SampleAnnotationInfo is a container class that maps very cleanly onto the
 * database table named 'sampleAnnotations'.
 */
public class SampleAnnotationInfo extends SampleTextInfo implements
        ExtendedDomTreeParticipant {
    
    /**
     * This constant is used for representing the null value in table for level
     * property. Refer to comments about level for more info.
     */
    public static final int ALL_LEVELS = -1;

    public static final int UNKNOWN_LEVEL = 0;

    public static final int ELEMENTARY_LEVEL = 1;

    public static final int MIDDLE_LEVEL = 2;

    public static final int HIGH_LEVEL = 4;

    public static final int UNDERGRADUATE_LEVEL = 8;

    public static final int GRADUATE_LEVEL = 16;

    public static final int GENERIC_COMMONMOLECULES_LEVEL = 8192;

    public static final int RESEARCHER_LEVEL = 65536;

    /**
     * This constant is used for representing the null value in the
     * referenceSample field and is used in referenceSample property. Refer to
     * the comment of referenceSample property for more details.
     */
    public static final int INVALID_REFERENCE_SAMPLE = -1;

    /**
     * The exact function of this particular field should be determined by the
     * education people. Basically, though, it will allow certain annotations to
     * be customized for multiple grade levels.If the value of this field does
     * not match the current visitor's grade level value, then the annotation is
     * not displayed. If this field is null in the table, its value should be
     * ALL_LEVELS, the constant defined in this class. In this case, the
     * annotation is displayed regardless of audience. This property should be
     * ALL_LEVELS for most annotations.
     */
    public int level;

    /**
     * The id of a row in the samples table. For attribute types superseded by
     * another sample, supersedes another sample, and duplicate structure of
     * another sample this number is the sample number of the sample being
     * superseded, superseding, or duplicating this sample. The first two
     * attribute types should be used when a sample's data must be changed. In
     * this case, the first sample should be withdrawn and have its superseded
     * by annotation set, and the second sample should be created, with
     * mostly-identical metadata to the first, and have its supersedes
     * annotation set. In the case of one structure whose data has been updated
     * multiple times, the associated sample records will comprise a
     * doubly-linked list in this fashion. When a particular structure
     * represented by one lab's sample is found to duplicate another lab's
     * sample, the duplicate structure annotation shall be set for each. In any
     * case, if this field is not null then the user interface will display a
     * hyperlink to the referenced sample number; the contents of the value
     * field will be displayed to the user next to the link. This field should
     * be null for most annotations. In this case, the value is
     * INVALID_REFERENCE_SAMPLE defined above.
     */
    public int referenceSample;

    /** Create an empty object */
    public SampleAnnotationInfo() {
        level = ALL_LEVELS;
        referenceSample = INVALID_REFERENCE_SAMPLE;
    }

    /**
     * Create an object with the specified type and value. (Use this when adding
     * an annotation to an existing SampleInfo object.)
     */
    public SampleAnnotationInfo(int type, String value) {
        this();
        this.type = type;
        this.value = value;
    }

    /*
     * TODO: create a new class called CoreSampleAnnotationInfo that extends
     * SampleAnnotationInfo. Then, take everything that appears below this line
     * and move it to that class. Only Site Manager needs access to the
     * variables and methods found below.
     */

    /**
     * Constructor to create this object from the current record in a db
     * resultset.
     */
    public SampleAnnotationInfo(ResultSet rs) throws SQLException {
        super(rs);
        level = rs.getInt("level");
        if (rs.wasNull()) {
            level = ALL_LEVELS;
        }
        referenceSample = rs.getInt("referenceSample");
        if (rs.wasNull()) {
            referenceSample = INVALID_REFERENCE_SAMPLE;
        }
    }

    /**
     * Determines whether the specified object is equal to this one, which is
     * true if and only if it is also a {@code SampleAttributeInfo} and has the
     * same type, level, and reference sample, and an equal value.
     * 
     * @param x the {@code Object} to compare with this one
     * @return {@code true} if the specified object is equal to this one,
     *         otherwise {@code false}
     */
    @Override
    public boolean equals(Object x) {
        if (x == this) {
            return true;
        } else if (x instanceof SampleAnnotationInfo) {
            SampleAnnotationInfo y = (SampleAnnotationInfo) x;
            
            return ((this.type == y.type)
                    && SampleInfo.compareReferences(this.value, y.value)
                    && (this.level == y.level)
                    && (this.referenceSample == y.referenceSample));
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
        return (value.hashCode() ^ (level << 16) ^ (type << 8)
                ^ referenceSample);
    }

    @Override
    public SampleAnnotationInfo clone() {
        return (SampleAnnotationInfo) super.clone();
    }

    /** Store this object in the current row of the provided db resultset */
    @Override
    public void dbStore(ResultSet rs) throws SQLException {
        super.dbStore(rs);
        if (level != ALL_LEVELS) {
            rs.updateInt("level", level);
        } else {
            rs.updateNull("level");
        }
        if (referenceSample != INVALID_REFERENCE_SAMPLE) {
            rs.updateInt("referenceSample", referenceSample);
        } else {
            rs.updateNull("referenceSample");
        }
    }

    /**
     * Store this object in the specified portion of a DOM tree. From interface
     * ExtendedDomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        return insertIntoDomUsingResources(doc, base, null);
    }

    /**
     * Store this object in the specified portion of a DOM tree using a
     * caller-specified resource bundle. From interface
     * ExtendedDomTreeParticipant.
     */
    public Node insertIntoDomUsingResources(
            @SuppressWarnings("unused") Document doc, Node base,
            ResourceBundle resources) {
        Element annotEl = DomUtil.createTextEl(base, "annotation", this.value);
        
        DomUtil.addAttrToEl(annotEl, "type", Integer.toString(this.type));
        if (resources != null) {
            // Store a textual description of the type.
            DomUtil.addAttrToEl(annotEl, "description", resources
                    .getString("field" + this.type));
        }
        if (level != ALL_LEVELS) {
            DomUtil.addAttrToEl(annotEl, "level", Integer.toString(this.level));
        }
        if (referenceSample != INVALID_REFERENCE_SAMPLE) {
            DomUtil.addAttrToEl(annotEl, "referenceSample", Integer
                    .toString(this.referenceSample));
        }
        return annotEl;
    }

    /**
     * Replace the member variables of this object with those obtained from the
     * specified portion of a DOM tree. From interface
     * ExtendedDomTreeParticipant.
     */
    public Node extractFromDom(
            @SuppressWarnings("unused") Document doc, Node base)
            throws SAXException {
        DomUtil.assertNodeName(base, "annotation");
        Element annotationEl = (Element) base;
        
        this.type = DomUtil.getAttrForElAsInt(annotationEl, "type");
        this.value = DomUtil.getTextForEl(annotationEl, false);
        this.level = DomUtil.getAttrForElAsInt(annotationEl, "level",
                ALL_LEVELS);
        this.referenceSample = DomUtil.getAttrForElAsInt(annotationEl,
                "referenceSample", INVALID_REFERENCE_SAMPLE);
        
        return annotationEl;
    }
}
