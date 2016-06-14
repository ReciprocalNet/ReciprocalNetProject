/*
 * Reciprocal Net project
 * 
 * SampleFieldRecord.java
 *
 * 10-Jun-2005: midurbin wrote first draft
 * 06-Oct-2005: midurbin updated assertValueAppropriateForType() to refelect
 *              new SampleInfo/SampleDataInfo provider fields
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * <p>
 * Contains information including field type and value for any field within a
 * {@code SampleInfo}, {@code SampleDataInfo} or {@code SampleTextInfo}.
 * </p><p>
 * This class is implemented so that whenever both a 'fieldCode' and 'value' are
 * entered, a check is performed to ensure that the type of object that is
 * stored as the 'value' is appropriate for the given 'fieldCode'. The rules for
 * this check are as follows:
 * </p>
 * <ul>
 * <li>SampleDataInfo fields with int values must be Integer objects.</li>
 * <li>SampleDataInfo fields with double values must be Double objects.</li>
 * <li>SampleDataInfo fields with String values must be String objects.</li>
 * <li>SampleInfo fields with int values must be Integer objects.</li>
 * <li>SampleInfo fields with String values must be String objects.</li>
 * <li>SampleAnnotationInfo fields contain the entire SampleAnnotationInfo
 * object</li>
 * <li>SampleAttributeInfo fields contain the entire SampleAttributeInfo
 * object</li>
 * </ul>
 * <p>
 * Note: This mapping is meant to be identical to {@link
 * org.recipnet.site.shared.db.SampleInfo#extractValue(int)
 * SampleInfo.extractValue()} except in the case of annotations and attributes.
 * </p>
 */
public class SampleFieldRecord {

    /** Indicates the field within the {@code SampleInfo}. */
    private final int fieldCode;

    /** Indicates the field value. */
    private Object value;

    /**
     * Creates a fully initialized {@code SampleFieldRecord}.
     * 
     * @throws IllegalStateException if the 'value' is not of the appropriate
     *         type for the 'fieldCode'.
     */
    public SampleFieldRecord(int fieldCode, Object value) {
        if (!SampleTextBL.isAttribute(fieldCode)
                && !SampleTextBL.isAnnotation(fieldCode)
                && !SampleDataInfo.isDataField(fieldCode)
                && !SampleInfo.isSampleField(fieldCode)
                && (fieldCode != SampleTextBL.INVALID_TYPE)) {
            // invalid field code
            throw new IllegalArgumentException("Unsupported field type");
        } else if (!isAcceptableValueForFieldType(value, fieldCode)) {
            throw new IllegalArgumentException(
                    "value type is incompatible with field code");
        } else {
            this.fieldCode = fieldCode;
            this.value = value;
        }
    }

    /** Gets the 'fieldCode'. */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * Sets the 'value'.
     * 
     * @throws IllegalStateException if the 'value' is not of the appropriate
     *         type for the 'fieldCode'.
     */
    public void setValue(Object value) {
        if (!isAcceptableValueForFieldType(value, getFieldCode())) {
            throw new IllegalArgumentException();
        } else {
            this.value = value;
        }
    }

    /** Gets the 'value'. */
    public Object getValue() {
        return value;
    }

    /**
     * An internal helper function called whenever the 'value' or 'fieldCode'
     * change. If both are set, but the type of 'value' is inappropriate for the
     * 'fieldCode' an exception is thrown.
     * 
     * @throws IllegalStateException if the 'value' is not the appropriate
     *         object type for the 'fieldCode'.
     */
    private boolean isAcceptableValueForFieldType(Object o, int code) {
        return ((code == SampleTextBL.INVALID_TYPE) || (o == null)
                || getValueTypeForField(code).isInstance(o));
    }

    public static Class<?> getValueTypeForField(int fieldCode) {
        if (fieldCode == SampleTextBL.INVALID_TYPE) {
            return null;
        } else if (SampleTextBL.isAnnotation(fieldCode)) {
            return SampleAnnotationInfo.class;
        } else if (SampleTextBL.isAttribute(fieldCode)) {
            return SampleAttributeInfo.class;
        } else {
            switch (fieldCode) {
                case SampleInfo.ID:
                case SampleInfo.LAB_ID:
                case SampleInfo.MOST_RECENT_PROVIDER_ID:
                case SampleInfo.STATUS:
                case SampleInfo.MOST_RECENT_STATUS:
                case SampleInfo.HISTORY_ID:
                case SampleInfo.MOST_RECENT_HISTORY_ID:
                case SampleDataInfo.Z_FIELD:
                case SampleDataInfo.PROVIDER_ID_FIELD:
                    return Integer.class;
                case SampleDataInfo.A_FIELD:
                case SampleDataInfo.B_FIELD:
                case SampleDataInfo.C_FIELD:
                case SampleDataInfo.ALPHA_FIELD:
                case SampleDataInfo.BETA_FIELD:
                case SampleDataInfo.GAMMA_FIELD:
                case SampleDataInfo.DCALC_FIELD:
                case SampleDataInfo.T_FIELD:
                case SampleDataInfo.V_FIELD:
                case SampleDataInfo.RF_FIELD:
                case SampleDataInfo.RWF_FIELD:
                case SampleDataInfo.RF2_FIELD:
                case SampleDataInfo.RWF2_FIELD:
                case SampleDataInfo.GOOF_FIELD:
                case SampleDataInfo.FORMULAWEIGHT_FIELD:
                    return Double.class;
                case SampleInfo.LOCAL_LAB_ID:
                case SampleDataInfo.SPGP_FIELD:
                case SampleDataInfo.COLOR_FIELD:
                case SampleDataInfo.SUMMARY_FIELD:
                    return String.class;
                default:
                    throw new IllegalArgumentException(
                            "unsupported field code");
            }
        }
    }
}
