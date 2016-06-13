/*
 * Reciprocal Net project
 * 
 * SampleTextInfo.java
 *
 * 18-Jun-2002: leqian wrote first draft
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 22-Aug-2002: ekoperda added support for local tracking fields
 * 23-Aug-2002: ekoperda added compareTo() operator
 * 05-Sep-2002: eisiorho added constants CSD_REFCODE, ICSD_COLLECTION_CODE,
 *              PDB_ENTRY_NUMBER, TEXT_CONTRIBUTOR, SHORT_DESCRIPTION
 * 17-Oct-2002: ekoperda and eisiorho added function getAllTextTypes() and
 *              constants CAS_REGISTRY_NUMBER and DETERMINATION_PROCEDURE.
 * 04-Nov-2002: ekoperda removed the text type PUBLICATION_REFERENCE
 * 07-Nov-2002: adharurk added three new texttypes,
 *              EMPIRICAL_FORMULA_DERIVED, EMPIRICAL_FORMULA_SINGLE_ION and
 *              EMPIRICAL_FORMULA_LESS_SOLVENT
 * 23-Jan-2003: adharurk added three new texttypes, INELIGIBLE_FOR_RELEASE,
 *              INCOMPLETENESS_EXPLANATION, DATA_QUALITY
 * 25-Feb-2003: eisiorho fixed bug #753 in getAllTextTypes()
 * 17-Jun-2003: dfeng added new annotation type TWIN_EXPLANATION
 * 31-Jul-2003: dfeng added new annotation type PROVIDER_REFERENCE_NUMBER
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 05-Jan-2005: eisiorho added field 'originalSampleHistoryId'
 * 27-Oct-2005: jobollin updated clone() to catch CloneNotSupportedExceptions
 *              (which shouldn't ever happen anyway), and pushed equals() down
 *              to SampleAttributeInfo because SampleAnnotationInfo cannot
 *              otherwise correctly override equals().  Made this class abstract
 *              at the same time so as to ensure that it is not directly
 *              instantiated.
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.recipnet.site.UnexpectedExceptionException;

/**
 * SampleTextInfo is the parent class for SampleAttributeInfo
 * and SampleAnnotationInfo.
 */
public abstract class SampleTextInfo implements Serializable, Cloneable,
        Comparable<SampleTextInfo> {
    public static final int INVALID_SAMPLE_TEXT_ID = -1;

    /**
     * This constant is used when it is not a valid
     * SampleAnnotationInfo or SampleAttributeInfo
     * class.
     */
    public static final int INVALID_TYPE=0;

    /**
     *  A numeric code indicating what sort of annotation
     *  or attribute
     *  this row contains. Possible values were defined
     *  in SampleAnnotationInfo and SampleAttributeInfo
     *  classes.
     */
    public int type;

    /** The textual value associated with this sample */
    public String value;

    /** Create an emtpy object */
    public SampleTextInfo() {
        id = INVALID_SAMPLE_TEXT_ID;
        firstSampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
        originalSampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        sampleId = SampleInfo.INVALID_SAMPLE_ID;
        type = INVALID_TYPE;
        value = null;
    }

    @Override
    public SampleTextInfo clone() {
        try {
            return (SampleTextInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            
            // Cannot happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }

    /**
     * sorts objects in order of type, then by value.  This is "almost"
     * consistent with the equals() operator, except in the case where a
     * SampleAttributeInfo is being compared to a SampleAnnotationInfo object.
     */
    public int compareTo(SampleTextInfo x) {
        if (this.type != x.type) {
            return this.type - x.type;
        }
        
        return this.value.compareTo(x.value);
    }

    /*
     * TODO: create a new class called CoreSampleTextInfo that extends
     *       SampleTextInfo.  Then, take everything that appears below this
     *       line and move it to that class.  Only Sample Manager needs access
     *       to the variables and methods found below.
     */
    public int id;
    public int firstSampleHistoryId;
    public int lastSampleHistoryId;
    public int originalSampleHistoryId;
    public int sampleId;

    /**
     * Constructor to create this object from the current record in a db
     * resultset
     */
    public SampleTextInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        firstSampleHistoryId = rs.getInt("first_sampleHistory_id");
        lastSampleHistoryId = rs.getInt("last_sampleHistory_id");
        originalSampleHistoryId = rs.getInt("original_sampleHistory_id");
        if (rs.wasNull()) {
            lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
        }
        sampleId = rs.getInt("sample_id");
        type = rs.getInt("type");
        value = rs.getString("value");
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        if (id != INVALID_SAMPLE_TEXT_ID) {
            rs.updateInt("id", id);
        } else {
            rs.updateNull("id");
        }
        rs.updateInt("first_sampleHistory_id", firstSampleHistoryId);
        if (lastSampleHistoryId != SampleHistoryInfo.STILL_ACTIVE) {
            rs.updateInt("last_sampleHistory_id", lastSampleHistoryId);
        } else {
            rs.updateNull("last_sampleHistory_id");
        }
        rs.updateInt("original_sampleHistory_id", originalSampleHistoryId);
        rs.updateInt("sample_id", sampleId);
        rs.updateInt("type", type);
        if (value != null) {
            rs.updateString("value", value);
        } else {
            rs.updateNull("value");
        }
    }
}
