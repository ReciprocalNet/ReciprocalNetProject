/*
 * Reciprocal Net project
 *  
 * LocalTrackingConfig.java
 *
 * 11-Jun-2002: hclin wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 22-Aug-2002: ekoperda wrote first draft
 * 29-Aug-2002: ekoperda added IGNORE_VISIBILITY constant
 * 14-Nov-2002: eisiorho added visibility constant VISIBLE_ON_LOCALTRACKING
 * 07-Feb-2003: midurbin added visibility constants VISIBLE_ON_SUSPEND,
 *              VISIBLE_ON_RESUME
 * 13-Feb-2003: midurbin added visibility constant VISIBLE_ON_FAILEDTOCOLLECT
 * 13-Feb-2003: midurbin added visibility constant VISIBLE_ON_DECLARENONSCS
 * 18-Feb-2003: dfeng renamed constant VISIBLE_ON_GATHERDATA to
 *              VISIBLE_ON_COLLECTPRELIMINARYDATA
 * 20-Feb-2003: yli added visibility constant VISIBLE_ON_DECLAREINCOMPLETE
 * 21-Feb-2003: yli renamed constant VISIBLE_ON_SOLVE to 
 *              VISIBLE_ON_REFINESTRUCTURE
 * 06-Mar-2003: ekoperda changed comments on addField()
 * 10-Mar-2003: adharurk added visibility constant VISIBLE_ON_RETRACT
 * 11-Mar-2003: ajooloor added visibility constant VISIBLE_ON_WITHDRAW
 * 25-Apr-2003: ekoperda added getTextTypesForAction()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared
 * 08-Aug-2004: cwestnea modified getTextTypesForAction() to use 
 *              SampleWorkflowBL
 * 29-Sep-2004: midurbin renamed the constant VISIBLE_ON_LOCALTRACKING to
 *              VISIBLE_ON_CUSTOMFIELDS
 * 25-May-2006: jobollin reformatted the source and added type arguments
 * 15-Jun-2006: jobollin added a warning-suppression annotation, updated docs
 * 11-Jan-2008: ekoperda added three new visibility flags named
 *              VISIBLE_ON_SHOWSAMPLE_TO_...
 */

package org.recipnet.site.shared;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;

/**
 * Contains information about which attributes are in use storing data for
 * locally-defined fields. Normally there is exactly one of these objects per
 * local lab (at a site).
 */
public class LocalTrackingConfig implements Serializable, Cloneable {
    
    /**
     * A field visibility <em>value</em> (not a flag) indicating that a field
     * is not visible on any workflow action page (though it might be visible
     * elsewhere, such as on sample.jsp)
     */
    public static final int INVALID_VISIBILITY = 0;

    /**
     * A field visibility <em>value</em> (not a flag) signaling that a field is
     * visible on all workflow action pages
     */
    public static final int IGNORE_VISIBILITY = 0xffff;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the search page (whatever that
     * means)
     */
    public static final int VISIBLE_ON_SEARCH = 0x1;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the submit page
     */
    public static final int VISIBLE_ON_SUBMIT = 0x1 << 1;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the collect preliminary data page
     */
    public static final int VISIBLE_ON_COLLECTPRELIMINARYDATA = 0x1 << 2;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the refine structure page
     */
    public static final int VISIBLE_ON_REFINESTRUCTURE = 0x1 << 3;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the release to public page
     */
    public static final int VISIBLE_ON_RELEASE_TO_PUBLIC = 0x1 << 4;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the edit custom fields page
     */
    public static final int VISIBLE_ON_CUSTOMFIELDS = 0x1 << 5;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the suspend page
     */
    public static final int VISIBLE_ON_SUSPEND = 0x1 << 6;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the resume page
     */
    public static final int VISIBLE_ON_RESUME = 0x1 << 7;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the declare incomplete page
     */
    public static final int VISIBLE_ON_DECLAREINCOMPLETE = 0x1 << 8;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the failed to collect page
     */
    public static final int VISIBLE_ON_FAILEDTOCOLLECT = 0x1 << 9;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the declare non-SCS page
     */
    public static final int VISIBLE_ON_DECLARENONSCS = 0x1 << 10;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the retract page
     */
    public static final int VISIBLE_ON_RETRACT = 0x1 << 11;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the withdraw page
     */
    public static final int VISIBLE_ON_WITHDRAW = 0x1 << 12;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the ShowSample page to
     * authenticated users who are associated with a lab.
     */
    public static final int VISIBLE_ON_SHOWSAMPLE_TO_LAB_USERS = 0x1 << 13;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the ShowSample page to
     * authenticated users who are associated with a provider.
     */
    public static final int VISIBLE_ON_SHOWSAMPLE_TO_PROVIDER_USERS 
            = 0x1 << 14;

    /**
     * A field visibility bit flag that, as part of a visibility value,
     * indicates that a field is visible on the ShowSample page to
     * unauthenticated users (but not to authenticated users).
     */
    public static final int VISIBLE_ON_SHOWSAMPLE_TO_UNAUTHENTICATED_USERS 
            = 0x1 << 15;

    /** The lab for which this set of attributes is in use. */
    public int labId;

    /**
     * a List of LocalTrackingConfig.Field objects that describes the local
     * tracking fields that are in use by this lab. They are in user-specified
     * order. If the list is empty then no locally-specified fields are in use
     * by this lab.
     */
    public SortedSet<Field> fields;

    /** Default constructor */
    public LocalTrackingConfig() {
        labId = LabInfo.INVALID_LAB_ID;
        fields = new TreeSet<Field>();
    }

    /**
     * Creates a copy of this {@code LocalTrackingConfig} having the same lab
     * ID and equal fields
     */
    @Override
    public LocalTrackingConfig clone() {
        LocalTrackingConfig x;
        
        try {
            x = (LocalTrackingConfig) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Can't happen because this class is Cloneable
            
            throw new UnexpectedExceptionException(cnse);
        }
        
        // The fields are immutable, therefore they don't need to be cloned
        x.fields = new TreeSet<Field>(this.fields);
        
        return x;
    }
    
    /**
     * Determines whether the specified text type (as obtained from the type
     * field of a SampleTextInfo object, for instance) corresponds to a
     * locally-defined field recognized by this configuration object.
     * 
     * @param textType the text type code to be checked
     * 
     * @return {@code true} if the specified text type code is one for which
     *         this object contains local tracking field information, otherwise
     *         {@code false}
     */
    public boolean isTextTypeValid(int textType) {
        return fields.contains(new Field(textType));
    }

    /**
     * Provides the field name for the specified text type
     * 
     * @param textType the text type code for which a field name is requested
     * @return the field name as a {@code String}, or {@code null} if no field
     *         bearing the specified code is known to this
     *         {@code LocalTrackingConfig}
     */
    public String getFieldName(int textType) {
        for (Field f : this.fields) {
            if (f.textType == textType) {
                return f.name;
            }
        }
        
        return null;
    }

    /**
     * Adds a Field to those managed by this {@code LocalTrackingConfig},
     * replacing any previous field definition for the same text type. The
     * Field definitions collectively dictate which text types are recognized
     * by this config object, and what their parameters are
     * 
     * @param textType the text type code for the new field
     * @param params a parameter {@code String} defining the field parameters,
     *        as interpreted by {@link Field#Field(int, String)}
     */
    public void addField(int textType, String params) {
        fields.add(new Field(textType, params));
    }

    /**
     * Obtains a {@code Set} of {@code Integer}s representing all the local
     * tracking text types that should be "visible" (i.e. eligible to be
     * edited) as part of the specified workflow action.
     * 
     * @param actionCode the code for the action about which local tracking
     *        field visibility information is requested; should generally be on
     *        of the workflow action code constants defined by
     *        {@link SampleWorkflowBL}
     * @return a {@code Set} of the text type codes (as {@code Integer}s) for
     *         the local tracking fields that should be visible as part of the
     *         specified action; will be an empty set if the action code is
     *         unrecognized or if no local tracking fields should be visible
     *         during the corresponding action
     */
    public Set<Integer> getTextTypesForAction(int actionCode) {
        int visibilityRequirement;
        
        switch (actionCode) {
            case SampleWorkflowBL.SUBMITTED:
                visibilityRequirement = VISIBLE_ON_SUBMIT;
                break;
            case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED:
                visibilityRequirement = VISIBLE_ON_COLLECTPRELIMINARYDATA;
                break;
            case SampleWorkflowBL.STRUCTURE_REFINED:
                visibilityRequirement = VISIBLE_ON_REFINESTRUCTURE;
                break;
            case SampleWorkflowBL.RELEASED_TO_PUBLIC:
                visibilityRequirement = VISIBLE_ON_RELEASE_TO_PUBLIC;
                break;
            case SampleWorkflowBL.MODIFIED_LTAS:
                visibilityRequirement = VISIBLE_ON_CUSTOMFIELDS;
                break;
            case SampleWorkflowBL.SUSPENDED:
                visibilityRequirement = VISIBLE_ON_SUSPEND;
                break;
            case SampleWorkflowBL.RESUMED:
                visibilityRequirement = VISIBLE_ON_RESUME;
                break;
            case SampleWorkflowBL.DECLARED_INCOMPLETE:
                visibilityRequirement = VISIBLE_ON_DECLAREINCOMPLETE;
                break;
            case SampleWorkflowBL.FAILED_TO_COLLECT:
                visibilityRequirement = VISIBLE_ON_FAILEDTOCOLLECT;
                break;
            case SampleWorkflowBL.DECLARED_NON_SCS:
                visibilityRequirement = VISIBLE_ON_DECLARENONSCS;
                break;
            case SampleWorkflowBL.RETRACTED:
                visibilityRequirement = VISIBLE_ON_RETRACT;
                break;
            case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER:
                visibilityRequirement = VISIBLE_ON_WITHDRAW;
                break;
            default:
                // invalid/unsupported action code specified; the set we return
                // should be empty (should not contain any field codes).
                return Collections.emptySet();
        }
        
        Set<Integer> textTypes = new HashSet<Integer>();
        
        for (Field field : fields) {
            if ((field.visibility & visibilityRequirement) != 0) {
                textTypes.add(Integer.valueOf(field.textType));
            }
        }
        
        return textTypes;
    }

    /**
     * Nested class that stores information about a single locally-defined
     * field.  Instances are immutable.
     */
    public static class Field implements Serializable, Cloneable,
            Comparable<Field> {
        
        /**
         * The value in SampleAttributeInfo.type that will identify a sample
         * attribute whose locally-defined field is described by this object.
         */
        public final int textType;

        /** The human-readable name of this field */
        public final String name;

        /**
         * A combination of bit flags that describes the circumstances under
         * which this locally-defined field should be visible to the user.
         * The individual bit flags are documented as constants defined on
         * LocalTrackingConfig.
         */
        public final int visibility;

        /**
         * Initializes a new {@code Field} with the specified text type, no
         * name, and no visibility.  Such a {@code Field} can be useful for 
         * looking up other fields bearing the same text type, because this
         * class's equality criteria and natural order depend only on the text
         * type
         * 
         * @param  textType the text type code for this {@code Field}
         */
        public Field(int textType) {
            this.textType = textType;
            name = null;
            visibility = INVALID_VISIBILITY;
        }

        /**
         * Initializes a new {@code Field} of the specified text type based on
         * the specified configuration string.
         * 
         * @param  textType the text type code for this {@code Field}
         * @param  params a parameter string formatted as
         *         labId,"field name",visibility .
         * @throws NumberFormatException if one of the numeric tokens in the
         *         parameter string is malformed
         * @throws NoSuchElementException if the parameter string contains too
         *         few parameters
         */
        public Field(int textType, String params)
                throws NoSuchElementException, NumberFormatException {
            StringTokenizer t = new StringTokenizer(params, ",");
            @SuppressWarnings("unused") int labId
                    = Integer.parseInt(t.nextToken());
            String temp = t.nextToken();
            
            // remove the leading and trailing quote marks from the name
            name = temp.substring(1, temp.length() - 1);

            visibility = Integer.parseInt(t.nextToken());
            this.textType = textType;
        }

        /**
         * Parses and returns the 'labId' portion of a Field configuration
         * string
         * 
         * @param params the {@code Field} parameter {@code String}
         * @return the parsed ID
         * @throws NumberFormatException if the lab ID is malformed
         */
        public static int parseLabId(String params)
                throws NumberFormatException {
            StringTokenizer t = new StringTokenizer(params, ",");
            return Integer.parseInt(t.nextToken());
        }

        /** returns true if both objects have the same textType */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof Field) {
                return this.textType == ((Field) o).textType;
            } else {
                return false;
            }
        }
        
        /**
         * Returns a hash code for this {@code Field}; this method is
         * consistent with {@link #equals(Object)}
         * 
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return textType;
        }

        /**
         * Compares this {@code Field} with the specified one to determine
         * which comes first in this class's natural order
         * 
         * @param x this {@code Field} to compare to this one
         * @return an integer less than, equal to, or greater than zero
         *         corresponding to whether this field comes before, at the
         *         same place, or after the specified one
         */
        public int compareTo(Field x) {
            if (this.textType < x.textType) {
                return -1;
            } else if (this.textType > x.textType) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
