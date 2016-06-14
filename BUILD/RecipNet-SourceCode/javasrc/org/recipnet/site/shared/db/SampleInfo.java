/*
 * Reciprocal Net project
 * 
 * SampleInfo.java
 *
 * 29-May-2002: leqian wrote skeleton
 * 10-Jun-2002: ekoperda added a field and did some reformatting
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 07-Aug-2002: ekoperda added mostRecentStatus field
 * 09-Aug-2002: ekoperda added Comparable interface
 * 30-Aug-2002: ekoperda removed all references to atomInfo and the db table
 *              sampleAtoms
 * 30-Aug-2002: ekoperda added stub for parseEmpiricalFormula function
 * 03-Sep-2002: jobollin added parseEmpiricalFormula implementation and
 *              calculateDensity, calculateVolume, and calculateFormulaWeight
 *              methods as part of task #301
 * 12-Sep-2002: jobollin fixed buggy parseEmpiricalFormula(String) method
 *              and added commentary describing the formula validity rules
 * 24-Sep-2002: eisiorho added comments to calculateDensity()
 * 05-Nov-2002: ekoperda added fields firstActionDate, lastActionDate,
 *              and releaseActionDate
 * 07-Nov-2002: nisheth added XML serialization code
 * 15-Jan-2003: ekoperda modified contructor-from-db and dbStore() to
 *              accommodate the three new db fields for date summary.  Also
 *              added isPublic() function.
 * 27-Jan-2003: adharurk added status codes to accommodate new workflow
 * 29-Jan-2003: ekoperda added function cloneAndSanitize()
 * 17-Feb-2003: ekoperda modified insertIntoDom() and extractFromDom() to make
 *              historyId an optional field in the native-XML format
 * 21-Feb-2003: ekoperda added 2-param constructor and implemented
 *              ContainerObject interface
 * 13-Mar-2003: ekoperda modified insertIntoDom() and extractFromDom() to
 *              include first/last/release action dates in XML representation
 * 23-May-2003: ekoperda added isMostRecentVersion()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 01-Mar-2004: cwestnea modified calculateFormulaWeight() and
 *              parseEmpiricalFormula() to accommodate renamed class ElementsBL
 * 07-May-2004: cwestnea added parseEmpiricalFormula(int) that parses different
 *              types of empirical formulas and modified
 *              parseEmpiricalFormula()
 * 03-May-2004: midurbin added field codes and the methods: getField(),
 *              setField(), extractValue(), replaceValue() and
 *              isSampleField()
 * 07-Jun-2004: midurbin changed values of field code constants so that they
 *              could be more sensibly ordered in ResourceBundle property files
 * 14-Jun-2004: ekoperda implemented ExtendedDomTreeParticipant by adding
 *              insertIntoDomUsingResources() and updating insertIntoDom()
 * 18-Aug-2004: cwestnea moved status constants to SampleWorkflowBL and made
 *              changes throughout to use them
 * 05-Oct-2004: midurbin fixed bug #1397 in updateValue()
 * 05-Nov-2004: eisiorho moved calculateVolume() to reflect new class
 *              SampleMathBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 12-Jul-2005: ekoperda removed cloneAndSanitize(), moving logic to BL layer
 * 06-Oct-2005: midurbin replaced 'providerId' field with
 *              'mostRecentProviderId'
 * 26-Oct-2005: jobollin changed the order of elements in the DOM
 *              representation by updating insertIntoDom() and
 *              insertIntoDomUsingResources()
 * 27-Oct-2005: jobollin updated clone() to make use of the new "foreach"
 *              construct, to declare that it returns SampleInfo, and to not
 *              declare that it throws CloneNotSupportedException; while working
 *              on this class, also corrected type safety problems in clone(),
 *              extractValue(), and updateValue()
 * 03-Nov-2005: jobollin added unnecessary typecasts in extractValue() and
 *              updateValue() to work around a bug in javac <= 1.5.0_05
 * 09-Nov-2005: jobollin removed formula parsing methods in favor of those
 *              on ChemicalFormulaBL; added getFirstAttributeOfType(); removed
 *              unused imports; moved all constructors before all other methods
 *              (except for SampleInfo(ResultSet), left where it is because it
 *              belongs to Core rather than to the world)
 * 31-May-2006: jobollin reformatted the source and added a hashCode() method
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.ExtendedDomTreeParticipant;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SampleInfo is a container class that maps very cleanly onto the database
 * table named 'samples'.  It contains all sample info respective to date.
 */
public class SampleInfo implements Cloneable, Comparable<SampleInfo>,
        ContainerObject, ExtendedDomTreeParticipant, Serializable {

    /**
     * Field code constants for fields stored within this container.  Constant
     * values must not conflict with attributes/annotation code, sample data
     * codes or with the localtracking range.
     */
    public static final int ID = 1;
    public static final int LAB_ID = 2;
    public static final int MOST_RECENT_PROVIDER_ID = 3;
    public static final int LOCAL_LAB_ID = 4;
    public static final int STATUS = 5;
    public static final int MOST_RECENT_STATUS = 6;
    public static final int HISTORY_ID = 7;
    public static final int MOST_RECENT_HISTORY_ID = 8;

    /**
     * This constant is used for id field to indicate
     * that this is a new SampleInfo and thus
     * has not been assigned an id yet.
     * SampleManager will recognize this and generate an
     * id before writing into the database.
     */
    public static final int INVALID_SAMPLE_ID=-1;

    /** the date format used in the XML representation of a sample */
    public static final String DATE_FORMAT = "yyyy-MM-dd kk:mm:ss Z";

   /** Sample's unique id (across all Recipnet Net) */
    public int id;

    /** Id of the sample's <i>originating lab</i> */
    public int labId;

    /**
     * Id of the sample's <i>originating provider</i> for the current
     * "version" of the sample. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}. 
     */
    public int mostRecentProviderId;

    /** A unique name/number for this sample assigned by the originating lab */
    public String localLabId;

    /**
     * The workflow state code for this sample, at the current "version".
     * The possible values are defined in {@code SampleWorkflowBL}. They
     * are: PENDING_STATUS, REFINEMENT_PENDING_STATUS, SUSPENDED_STATUS,
     * COMPLETE_STATUS, COMPLETE_PUBLIC_STATUS, INCOMPLETE_STATUS,
     * INCOMPLETE_PUBLIC_STATUS, NON_SCS_STATUS, NON_SCS_PUBLIC_STATUS,
     * RETRACTED_STATUS, RETRACTED_PUBLIC_STATUS, WITHDRAWN_STATUS,
     * NOGO_STATUS.
     */
    public int status;

    /**
     * The most recent status value available for the sample.  This might
     * differ from the status field above if, for example, the caller
     * deliberately requested a past version of this object (that wasn't the
     * most current). The possible values are defined in
     * {@code SampleWorkflowBL}. They are: PENDING_STATUS,
     * REFINEMENT_PENDING_STATUS, SUSPENDED_STATUS, COMPLETE_STATUS,
     * COMPLETE_PUBLIC_STATUS, INCOMPLETE_STATUS, INCOMPLETE_PUBLIC_STATUS,
     * NON_SCS_STATUS, NON_SCS_PUBLIC_STATUS, RETRACTED_STATUS,
     * RETRACTED_PUBLIC_STATUS, WITHDRAWN_STATUS, NOGO_STATUS.<p />
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public int mostRecentStatus;

    /**
     * Stores the history "version" of this sample.  Version numbers are
     * guaranteed to increase with time. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}. */
    public int historyId;

    /**
     * The most recent version number available
     * for the sample.  This might differ from the historyId above if,
     * for example, the caller deliberately requested a past version of
     * this object (that wasn't the most current). <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public int mostRecentHistoryId;

    /** Sample data information, from table "sampleData" */
    public SampleDataInfo dataInfo;

    /**
     * This is a list of SampleAttibuteInfo objects which stores sample
     * attibute infomation from "sampleAttibutes" table
     */
    public List<SampleAttributeInfo> attributeInfo;

    /**
     * This is a list of SampleAnnotationInfo objects which stores sample
     * annotation infomations from "sampleAttibutes" table
     */
    public List<SampleAnnotationInfo> annotationInfo;

    /**
     * This is a list of SampleAccessInfo objects which stores access control
     * level infomations from "sampleAcls" table
     */
    public List<SampleAccessInfo> accessInfo;

    /**
     * This value is significant only if this SampleInfo object was obtained
     * via a call to SampleManager.getSearchResults().  In this case, if the
     * variable below is true, this means that the current sample has been
     * updated since the time the user's search was executed.  This sample
     * may no longer match the user's original query parameters (although
     * a non-match is not guaranteed) -- the only way to resolve the issue
     * would be to execute the user's search again.  A message to this effect
     * should be displayed to the user. <p>
     *
     * If the variable below is false then no special action need be taken. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public boolean isMoreRecentThanSearch;

    /**
     * The date of this sample's first workflow action in its workflow history.
     * Workflow actions are sorted by historyId, not date, so there is no
     * guarantee that this date is minimal.  This value may be null on a
     * newly-created sample.  Normally this value is interpreted to be the
     * "creation date" of the sample. <p>
     *
     * IMPORTANT: this field is for the user's information only.  Program logic
     * must never depend on this value. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public Date firstActionDate;

    /**
     * The date of this sample's last (most recent) workflow action in its
     * workflow history.  Workflow actions are sorted by historyId, not date,
     * so there is no guarantee that this date is maximal.  This value may be
     * null on a newly-created sample.  Normally this value is interpreted to
     * be the "last modification date" of the sample.
     *
     * IMPORTANT: this field is for the user's information only.  Program logic
     * must never depend on this value. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public Date lastActionDate;

    /**
     * The date of this sample's most recent workflow action that had action
     * code {@code RELEASED_TO_PUBLIC}.  May be null if no such action has
     * been performed on this sample (yet).  Normally this value is interpreted
     * to be the "release date" of the sample.
     *
     * IMPORTANT: this field is for the user's information only.  Program logic
     * must never depend on this value. <p>
     *
     * The value of this field is ignored during a call to
     * {@code SampleManager.putSampleInfo()}.
     */
    public Date releaseActionDate;

    /** create an empty object */
    public SampleInfo() {
        id = INVALID_SAMPLE_ID;
        labId = LabInfo.INVALID_LAB_ID;
        mostRecentProviderId = ProviderInfo.INVALID_PROVIDER_ID;
        localLabId = null;
        status = SampleWorkflowBL.INVALID_STATUS;
        mostRecentStatus = SampleWorkflowBL.INVALID_STATUS;
        historyId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        mostRecentHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        dataInfo = new SampleDataInfo();
        attributeInfo = new ArrayList<SampleAttributeInfo>();
        annotationInfo = new ArrayList<SampleAnnotationInfo>();
        accessInfo = new ArrayList<SampleAccessInfo>();
        isMoreRecentThanSearch = false;
        firstActionDate = null;
        lastActionDate = null;
        releaseActionDate = null;
    }

    /**
     * Convenience constructor that creates a near-useless SampleInfo objects
     * that can be used as a search key when finding other SampleInfo objects.
     */
    public SampleInfo(int id) {
        this();
        this.id = id;
    }

    /**
     * Convenience constructor that creates a near-useless SampleInfo object
     * that can be used as a search key when finding other SampleInfo objects.
     */
    public SampleInfo(int id, int historyId) {
        this();
        this.id = id;
        this.historyId = historyId;
    }

    /**
     * May be used to check whether a fieldCode references a field within
     * {@code SampleInfo}.
     * @param fieldCode an integer known to reference a field within a
     *     {@code SampleInfo} object or one of its members.
     * @return true if the provided fieldCode references a valid field within
     *     {@code SampleInfo} (as opposed to {@code SampleDataInfo},
     *     {@code SampleTextInfo}, etc)
     */
    public static boolean isSampleField(int fieldCode) {
        switch (fieldCode) {
          case SampleInfo.ID:
          case SampleInfo.LAB_ID:
          case SampleInfo.MOST_RECENT_PROVIDER_ID:
          case SampleInfo.LOCAL_LAB_ID:
          case SampleInfo.STATUS:
          case SampleInfo.MOST_RECENT_STATUS:
          case SampleInfo.HISTORY_ID:
          case SampleInfo.MOST_RECENT_HISTORY_ID:
            return true;
          default:
            return false;
        }
    }

    /**
     * Returns the value of the the field referenced by {@code fieldCode}.
     * This may be a field on {@code SampleInfo},
     * {@code SampleDataInfo} or any number of matching attributes or
     * annotations.  In the case of attributes/annotations the returned object
     * will always be an array of {@code String}s (unless none exist, in
     * which case, null will be returned)  In other cases, the returned object
     * will always be the Object that is associated with the particular field.
     * This method delegates to one of various {@code getField()} methods.
     * @param fieldCode indicates which field's value is to be returned
     * @return an {@code Object} representation of the indicated field
     * @throws IllegalArgumentException if the provided fieldCode is unknown
     */
    public Object extractValue(int fieldCode) {
        if (SampleInfo.isSampleField(fieldCode)) {
            return getField(fieldCode);
        } else if (SampleDataInfo.isDataField(fieldCode)) {
            return this.dataInfo.getField(fieldCode);
        } else if (SampleTextBL.isAnnotation(fieldCode)
                || SampleTextBL.isAttribute(fieldCode)) {
            List<String> valueList = new ArrayList<String>();
            List<? extends SampleTextInfo> textInfoList;
            
            if (SampleTextBL.isAnnotation(fieldCode)) {
                textInfoList = this.annotationInfo;
            } else {
                textInfoList = this.attributeInfo;
            }
            
            for (SampleTextInfo text : textInfoList) {
                if (text.type == fieldCode) {
                    valueList.add(text.value);
                }
            }
            
            return ((valueList.size() == 0)
                    ? null
                    : valueList.toArray(new String[valueList.size()]));
        } else {
            // invalid fieldCode
            throw new IllegalArgumentException();
        }
    }

    /**
     * Replaces the current value of the field(s) indicated by
     * {@code fieldCode} with the provided object.  This object must be
     * of the same type returned by {@code extractValue()}.  If the
     * {@code fieldCode} is an annotation or attribute, {@code value}
     * must be of type {@code String[]} and is considered to be a
     * comprehensive list of attributes/annotations of that type.  This means
     * that existing annotations/attributes that are in the 'value' array will
     * deleted and any new ones will be added.  For all single-valued fields,
     * the value will simply be replaced.
     * @param fieldCode indicates which fields value is to be replaced
     * @param value An Object whose value will be used to replace the given
     *     field's value.  This value should be the complex data type that
     *     corresponds to the 'fieldCode' parameter.  (Integer, Double, String
     *     and String[])  In the case of a String[] passed to replace all
     *     annotations/attributes of the given type, null may be provided
     *     which will result in the removal of all annotations/attributes of
     *     that type.
     * @throws IllegalArgumentException if the provided fieldCode is unknown
     */
    public void updateValue(int fieldCode, Object value) {
        if (SampleInfo.isSampleField(fieldCode)) {
            setField(fieldCode, value);
        } else if (SampleDataInfo.isDataField(fieldCode)) {
            this.dataInfo.setField(fieldCode, value);
        } else if (SampleTextBL.isAnnotation(fieldCode)
                || SampleTextBL.isAttribute(fieldCode)) {
            
            /*
             * 'value' is a String[] representing all attributes/annotations of
             * the type 'fieldCode', or null if all values have been removed
             */

            Collection<String> newValues = ((value == null)
                    ? Collections.<String>emptyList()
                    : new ArrayList<String>(Arrays.asList((String[]) value)));
            
            /*
             * Iterate through all the current attribute/annotations on the
             * provided sample and remove those that don't belong any more.
             */
            
            Iterator<? extends SampleTextInfo> it;
            boolean isAnnotation = SampleTextBL.isAnnotation(fieldCode);
            
            if (isAnnotation) {
                it = this.annotationInfo.iterator();
            } else {
                it = this.attributeInfo.iterator();
            }
            
            while (it.hasNext()) {
                SampleTextInfo text = it.next();
                
                if (text.type == fieldCode) {
                    if (newValues.contains(text.value)) {
                        /*
                         * Remove value from our list of values to be added
                         * because it is already present.
                         */
                        newValues.remove(text.value);
                    } else {
                        /*
                         * Remove unkown value from annotation/attribute list
                         * because it is no longer valid.
                         */
                        it.remove();
                    }
                }
            }

            /*
             * Iterate through the remaining values to be added and add them to
             * the appropriate annotation or attribute list, as appropriate
             */
            for (String valueToAdd : newValues) {
                if ((valueToAdd != null) && !valueToAdd.equals("")) {
                    if (isAnnotation) {
                        this.annotationInfo.add(
                                new SampleAnnotationInfo(fieldCode,
                                        valueToAdd));
                    } else {
                        this.attributeInfo.add(
                                new SampleAttributeInfo(fieldCode,
                                        valueToAdd));
                    }
                }
            }
        } else {
            // invalid fieldCode
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the value of the indicated field.  The object type of the value
     * will depend on the type associated with the field but will be the same
     * as expected by {@code setField()}.
     * @param fieldCode indicates which field's value is to be returned
     * @return an {@code Object} representation of the value of the field
     * indicated by {@code fieldCode}.
     */
    public Object getField(int fieldCode) {
        switch (fieldCode) {
            case SampleInfo.ID:
                return Integer.valueOf(this.id);
            case SampleInfo.LAB_ID:
                return Integer.valueOf(this.labId);
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
                return Integer.valueOf(this.mostRecentProviderId);
            case SampleInfo.LOCAL_LAB_ID:
                return this.localLabId;
            case SampleInfo.STATUS:
                return Integer.valueOf(this.status);
            case SampleInfo.MOST_RECENT_STATUS:
                return Integer.valueOf(this.mostRecentStatus);
            case SampleInfo.HISTORY_ID:
                return Integer.valueOf(this.historyId);
            case SampleInfo.MOST_RECENT_HISTORY_ID:
                return Integer.valueOf(this.mostRecentHistoryId);
            default:
                // invalid fieldCode
                throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the value of the indicated field.  The indicated value must be the
     * same type of object as would be returned by {@code getField()} for
     * the same fieldCode.
     * @param fieldCode indicates the field whose value is to be replaced
     * @throws IllegalArgumentException if the provided fieldCode is invalid
     *     (this may be verified by a call to {@code isDataField()})
     */
    public void setField(int fieldCode, Object value) {
        switch (fieldCode) {
            case SampleInfo.ID:
                this.id = ((Integer) value).intValue();
                break;
            case SampleInfo.LAB_ID:
                this.labId = ((Integer) value).intValue();
                break;
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
                this.mostRecentProviderId = ((Integer) value).intValue();
                break;
            case SampleInfo.LOCAL_LAB_ID:
                this.localLabId = (String) value;
                break;
            case SampleInfo.STATUS:
                this.status = ((Integer) value).intValue();
                break;
            case SampleInfo.MOST_RECENT_STATUS:
                this.mostRecentStatus = ((Integer) value).intValue();
                break;
            case SampleInfo.HISTORY_ID:
                this.historyId = ((Integer) value).intValue();
                break;
            case SampleInfo.MOST_RECENT_HISTORY_ID:
                this.mostRecentHistoryId = ((Integer) value).intValue();
                break;
            default:
                // invalid fieldCode
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object x) {
        if (this == x) {
            return true;
        } else if (x instanceof SampleInfo) {
            SampleInfo y = (SampleInfo) x;

            return (this.id == y.id)
                    && (this.labId == y.labId)
                    && (this.mostRecentProviderId == y.mostRecentProviderId)
                    && compareReferences(this.localLabId, y.localLabId)
                    && (this.status == y.status)
                    && (this.mostRecentStatus == y.mostRecentStatus)
                    && (this.historyId == y.historyId)
                    && (this.mostRecentHistoryId == y.mostRecentHistoryId)
                    && this.dataInfo.equals(y.dataInfo)
                    && this.attributeInfo.equals(y.attributeInfo)
                    && this.annotationInfo.equals(y.annotationInfo)
                    && this.accessInfo.equals(y.accessInfo)
                    && (this.isMoreRecentThanSearch == y.isMoreRecentThanSearch)
                    && compareReferences(this.firstActionDate,
                            y.firstActionDate)
                    && compareReferences(this.lastActionDate, y.lastActionDate)
                    && compareReferences(this.releaseActionDate,
                            y.releaseActionDate);
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this {@code SampleInfo} that is consistent with
     * its {@link #equals(Object)} method.  This hash code is based on some of
     * the values of this object's fields, but is inexpensive to compute.  In
     * order to minimize computation cost, the hashing method chosen may not
     * distinguish between very similar {@code SampleInfo}s.  Because this hash
     * code depends on field values, modification of an instance may change its
     * hash code.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (id ^ (mostRecentHistoryId << 16));
    }
    
    /**
     * Compares two references for equality according to these rules:
     * <ol>
     * <li>if both references are {@code null} then they compare as equal</li>
     * <li>if reference {@code o1} is non-{@code null} then the references
     * compare equal if and only if {@code o1.equals(o2)}</li>
     * <li>otherwise, the two references do not compare as equal</li>
     * </ol>
     * 
     * @param o1 the first reference to compare
     * @param o2 the second reference to compare
     * @return {@code true} if the two references compare as equal,
     *         {@code false} if not
     */
    public static boolean compareReferences(Object o1, Object o2) {
        return ((o1 == null) ? (o2 == null) : o1.equals(o2));
    }

    public int compareTo(SampleInfo y) {
        if (this.id < y.id) {
            return -1;
        } else if (this.id == y.id) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Creates and returns a {@code SampleInfo} that is distict from this one
     * but equal to it according to equals().  The clone can be freely modified
     * without affect on the original (and vise versa).
     * 
     * @return the cloned {@code SampleInfo}
     */
    @Override
    public SampleInfo clone() {
        SampleInfo x;
        
        try {
            x = (SampleInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            
            // Cannot happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }

        if (this.localLabId != null) {
            x.localLabId = new String(this.localLabId);
        }
        if (this.firstActionDate != null) {
            x.firstActionDate = new Date(this.firstActionDate.getTime());
        }
        if (this.lastActionDate != null) {
            x.lastActionDate = new Date(this.lastActionDate.getTime());
        }
        if (this.releaseActionDate != null) {
            x.releaseActionDate = new Date(this.releaseActionDate.getTime());
        }

        x.dataInfo = this.dataInfo.clone();

        x.attributeInfo = new ArrayList<SampleAttributeInfo>(
                this.attributeInfo.size());
        for (SampleAttributeInfo info : this.attributeInfo) {
             x.attributeInfo.add(info.clone());
        }

        x.annotationInfo = new ArrayList<SampleAnnotationInfo>(
                this.annotationInfo.size());
        for (SampleAnnotationInfo info : this.annotationInfo) {
             x.annotationInfo.add(info.clone());
        }

        x.accessInfo = new ArrayList<SampleAccessInfo>(
                this.accessInfo.size());
        for (SampleAccessInfo info : this.accessInfo) {
             x.accessInfo.add(info.clone());
        }

        return x;
    }

    /**
     * Returns true if this sample is read-visible to the public (everyone),
     * false if not.  The decision is based solely on the current
     * {@code status} value.
     */
    public boolean isPublic() {
        return SampleWorkflowBL.isPublicStatusCode(this.status);
    }

    /**
     * @return true if, at the time this sample was fetched from the database
     *     this version of the sample was the most current version of the
     *     samle available, false otherwise.
     */
    public boolean isMostRecentVersion() {
        return historyId == mostRecentHistoryId;
    }

    /**
     * Returns the first attribute in this {@code SampleInfo}'s attribute list
     * that has the specified type
     * 
     * @param  type the attribute type requested
     * 
     * @return the first {@code SampleAttributeInfo} in this
     *         {@code SampleInfo}'s list that has the specified type, or
     *         {@code null} if there isn't any
     */
    public SampleAttributeInfo getFirstAttributeOfType(int type) {
        for (SampleAttributeInfo attr : attributeInfo) {
            if (attr.type == type) {
                return attr;
            }
        }
        
        return null;
    }
    
    /**
     * Store this object (and subobjects) in the specified portion of a DOM
     * tree.  From interface ExtendedDomTreeParticipant.
     */
    public Node insertIntoDom(Document doc, Node base) {
        return insertIntoDomUsingResources(doc, base, null);
    }

    /**
     * Store this object (and subobjects) in the specified portion of a DOM
     * tree.  From interface ExtendedDomTreeParticipant.
     */
    public Node insertIntoDomUsingResources(Document doc, Node base,
            ResourceBundle resources) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        /*
         * Store data from this SampleInfo proper.
         */
        Element sampleEl = DomUtil.createEl(doc, base, "sample");
        DomUtil.addAttrToEl(sampleEl, "id", Integer.toString(this.id));
        DomUtil.createTextEl(sampleEl, "localLabId", this.localLabId);
        DomUtil.createTextEl(sampleEl, "labId",Integer.toString(this.labId));
        DomUtil.createTextEl(sampleEl, "mostRecentProviderId",
                Integer.toString(this.mostRecentProviderId));
        if (historyId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
            DomUtil.createTextEl(sampleEl, "version",
                    Integer.toString(this.historyId));
        }
        Element statusEl = DomUtil.createEl(doc, sampleEl, "status");
        DomUtil.addAttrToEl(statusEl, "code", Integer.toString(this.status));
        if (resources != null) {
            // Store a textual explanation for the sample's status.
            String statusStr = resources.getString("status" + this.status);
            DomUtil.addTextToEl(statusEl, statusStr);
        }

        // Include the child SampleDataInfo's data
        dataInfo.insertIntoDom(doc, sampleEl);

        // Include all the child SampleAttributeInfo's data
        for (SampleAttributeInfo attr : attributeInfo) {
            attr.insertIntoDomUsingResources(doc, sampleEl, resources);
        }

        // Include all the child SampleAnnotationInfo's data
        for (SampleAnnotationInfo anno : annotationInfo) {
            anno.insertIntoDomUsingResources(doc,sampleEl, resources);
        }

        DomUtil.createTextEl(sampleEl, "firstActionDate",
                sdf.format(this.firstActionDate));
        DomUtil.createTextEl(sampleEl, "lastActionDate",
                 sdf.format(this.lastActionDate));
        if (releaseActionDate != null) {
            DomUtil.createTextEl(sampleEl, "releaseActionDate",
                    sdf.format(this.releaseActionDate));
        }
        
        return sampleEl;
    }

    /**
     * Replace the member variables of this object (and subobjects) with those
     * obtained from the specified portion of a DOM tree.  From interface
     * ExtendedDomTreeParticipant.
     */
    public Node extractFromDom(Document doc, Node base) throws SAXException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        // Get the root <sample> element and its one attribute
        DomUtil.assertNodeName(base, "sample");
        DomUtil.assertAttributeCount(base, 1);
        Element sampleEl = (Element) base;
        id = DomUtil.getAttrForElAsInt(sampleEl, "id");
        localLabId = DomUtil.getTextForEl(sampleEl, "localLabId", false);
        labId = DomUtil.getTextForElAsInt(sampleEl, "labId");
        if (DomUtil.isElPresent(sampleEl, "mostRecentProviderId")) {
            mostRecentProviderId = DomUtil.getTextForElAsInt(sampleEl,
                    "mostRecentProviderId");
        } else {
            // this case if for parsing of pre-0.9.0 ISM where the providerId
            // wasn't a versioned field
            mostRecentProviderId
                    = DomUtil.getTextForElAsInt(sampleEl, "providerId");
            dataInfo.providerId = mostRecentProviderId;
        }
        historyId = DomUtil.getTextForElAsInt(sampleEl, "version",
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
        Element statusEl =
                DomUtil.findSingleElement(sampleEl, "status", false);
        status = DomUtil.getAttrForElAsInt(statusEl, "code");
        try {
            firstActionDate = sdf.parse(DomUtil.getTextForEl(sampleEl,
                    "firstActionDate", true));
            lastActionDate = sdf.parse(DomUtil.getTextForEl(sampleEl,
                    "lastActionDate", true));
            String releaseActionDateStr = DomUtil.getTextForEl(sampleEl,
                    "releaseActionDate", false);
            releaseActionDate = releaseActionDateStr != null
                    ? sdf.parse(releaseActionDateStr) : null;
        } catch (ParseException ex) {
            throw new SAXException("Invalid date format", ex);
        }

        // The base <sample> object contains the data for a SampleDataInfo
        dataInfo.extractFromDom(doc,sampleEl);

        // Extract all the <attribute> elements into SampleAttributeInfo's
        NodeList attributeList = doc.getElementsByTagName("attribute");
        int listLength = attributeList.getLength();
        attributeInfo.clear();
        for(int i = 0; i < listLength; i++){
            SampleAttributeInfo attr = new SampleAttributeInfo();
            Node node = attributeList.item(i);
            attr.extractFromDom(doc, node);
            attributeInfo.add(attr);
        }

        // Extract all the <annotation> elements into SampleAnnotationInfo's
        NodeList annotationList = doc.getElementsByTagName("annotation");
        listLength = annotationList.getLength();
        annotationInfo.clear();
        for(int i = 0; i < listLength; i++){
            SampleAnnotationInfo anno = new SampleAnnotationInfo();
            Node node = annotationList.item(i);
            anno.extractFromDom(doc, node);
            annotationInfo.add(anno);
        }

        return sampleEl;
    }

    /*
     * TODO: create a new class called CoreSampleInfo that extends this one;
     * move everything below this comment to the new class
     */
    
    /**
     * Creates a new object that matches the current row in the
     * caller-provided database recordset.  The new instance will have no
     * associated sample data information, sample access information,
     * attributes, or annotations; these are not available from the samples
     * table, and have to be added separately
     * 
     * @param  rs the {@code ResultSet} from which to load data
     */
    public SampleInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        labId = rs.getInt("lab_id");
        mostRecentProviderId = rs.getInt("current_provider_id");
        localLabId = rs.getString("localLabId");
        mostRecentStatus = rs.getInt("status");
        mostRecentHistoryId = rs.getInt("current_sampleHistory_id");
        firstActionDate = rs.getTimestamp("firstActionDate");
        lastActionDate = rs.getTimestamp("lastActionDate");
        releaseActionDate = rs.getTimestamp("releaseActionDate");

        dataInfo = new SampleDataInfo();
        attributeInfo = new ArrayList<SampleAttributeInfo>();
        annotationInfo = new ArrayList<SampleAnnotationInfo>();
        accessInfo = new ArrayList<SampleAccessInfo>();

        isMoreRecentThanSearch = false;
    }

    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("id", id);
        rs.updateInt("lab_id", labId);
        rs.updateInt("current_provider_id", mostRecentProviderId);
        rs.updateString("localLabId", localLabId);
        rs.updateInt("status", mostRecentStatus);
        rs.updateInt("current_sampleHistory_id", mostRecentHistoryId);
        if (firstActionDate != null) {
            rs.updateTimestamp("firstActionDate",
                    new Timestamp(firstActionDate.getTime()));
        } else {
            rs.updateNull("firstActionDate");
        }
        if (lastActionDate != null) {
            rs.updateTimestamp("lastActionDate",
                    new Timestamp(lastActionDate.getTime()));
        } else {
            rs.updateNull("lastActionDate");
        }
        if (releaseActionDate != null) {
            rs.updateTimestamp("releaseActionDate",
                    new Timestamp(releaseActionDate.getTime()));
        } else {
            rs.updateNull("releaseActionDate");
        }
    }
}
