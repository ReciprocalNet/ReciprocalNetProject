/*
 * Reciprocal Net project
 * 
 * TextualSampleDataSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

import org.recipnet.site.shared.db.SampleDataInfo;

/**
 * A {@code SearchConstraint} that limits the search results to those samples
 * with a 'sampleData' values that match the given value. This
 * {@code SearchConstraint} only handles those fields within {@code SampleInfo}
 * that have textual ({@code String}) types. For numeric ({@code double},
 * {@code int}) fields, use {@code NumericSampleDataSC}.
 */
public class TextualSampleDataSC extends TextComparisonSC {

    /**
     * The desired value for the given field in the {@code SampleDataInfo}.
     */
    private String value;

    /**
     * The operator that relates the provided 'value' with values stored in the
     * database. This must be set to one of the constant operator codes defined
     * on {@code TextComparisonSC}.
     */
    private int operator;

    /**
     * Indicates the field within the {@code SampleDataInfo} that is being
     * compared to the given 'value'.
     */
    private int fieldCode;

    /**
     * A constructor that fully initializes a {@code TextualSampleDataSC}.
     * 
     * @param fieldCode the code (as defined by {@code SampleDataInfo})
     *        representing the particular {@code SampleDataInfo} field that will
     *        be compared
     * @param value a double value for comparison
     * @param operator one of the operator codes defined on
     *        {@code TextComparisonSC}
     */
    public TextualSampleDataSC(int fieldCode, String value, int operator) {
        this.fieldCode = fieldCode;
        this.value = value;
        this.operator = operator;
    }

    /** Gets the 'value' that was supplied to the constructor. */
    public String getValue() {
        return value;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation generates
     * a {@code String} that may be used as a portion of the SQL WHERE clause to
     * require that the sample data field indicated by 'fieldCode' has the given
     * 'value'.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        parameters.add(convertRawValueToExpression(this.value, this.operator));
        
        return tableTracker.getTableAlias("sampleData", this) + "."
                + fieldCodeToColumnName(fieldCode) + " "
                + convertOperatorToString(this.operator) + " ?";
    }

    /**
     * Converts a given 'fieldCode' to the name of the database column that
     * contains that field's value.
     * 
     * @throws IllegalStateException if the provided 'fieldCode' does not
     *         represent a textual field on {@code SampleDataInfo}.
     */
    private static String fieldCodeToColumnName(int fieldCode) {
        switch (fieldCode) {
            case SampleDataInfo.COLOR_FIELD:
                return "color";
            case SampleDataInfo.SPGP_FIELD:
                return "spgp";
            case SampleDataInfo.SUMMARY_FIELD:
                return "summary";
            default:
                throw new IllegalArgumentException();
        }
    }

    /** Equality is based on class, operation, fieldCode and value. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof TextualSampleDataSC) {
            TextualSampleDataSC tsdsc = (TextualSampleDataSC) obj;
            return ((this.getClass() == tsdsc.getClass())
                    && (this.operator == tsdsc.operator)
                    && (this.fieldCode == tsdsc.fieldCode)
                    && (((this.value == null) && (tsdsc.value == null))
                            || ((this.value != null)
                                    && this.value.equals(tsdsc.value))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass()) + String.valueOf(this.operator)
                + String.valueOf(this.fieldCode) + this.value).hashCode();
    }
}
