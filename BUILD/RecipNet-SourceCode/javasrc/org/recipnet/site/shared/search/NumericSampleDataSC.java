/*
 * Reciprocal Net project
 * 
 * NumericSampleDataSC.java
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
 * that have numeric ({@code double}, {@code int}) types. For textual
 * ({@code String}) fields, use {@link TextualSampleDataSC}.
 */
public class NumericSampleDataSC extends NumberComparisonSC {

    /**
     * The desired value for the given field in the {@code SampleDataInfo}.
     */
    private final Number value;

    /**
     * The operator that relates the provided 'value' with values stored in the
     * database. This must be set to one of the constant operator codes defined
     * on {@code NumberComparisonSC}.
     */
    private final int operator;

    /**
     * Indicates the field within the {@code SampleDataInfo} that is being
     * compared to the given 'value'.
     */
    private final int fieldCode;

    /**
     * The name of the column in the database that corresponds to the
     * {@code fieldCode}. This is set when the fieldCode is validated during
     * the constructor and used in {@code getWhereClauseFragment()}.
     */
    private final String columnName;

    /**
     * A constructor that fully initializes a {@code NumericSampleDataSC} to
     * represent a field with a value of type {@code double}.
     * 
     * @param fieldCode the code (as defined by {@code SampleDataInfo})
     *        representing the particular {@code SampleDataInfo} field that will
     *        be compared
     * @param value a double value for comparison
     * @param operator one of the operator codes defined on
     *        {@code NumberComparisonSC}
     */
    public NumericSampleDataSC(int fieldCode, double value, int operator) {
        this.fieldCode = fieldCode;
        this.columnName = fieldCodeToColumnName(fieldCode);
        this.value = Double.valueOf(value);
        this.operator = operator;
    }

    public Number getValue() {
        return this.value;
    }

    /**
     * A constructor that fully initializes a {@code NumericSampleDataSC} to
     * represent a field with a value of type {@code int}.
     * 
     * @param fieldCode the code (as defined by {@code SampleDataInfo})
     *        representing the particular {@code SampleDataInfo} field that will
     *        be compared
     * @param value an int value for comparison
     * @param operator one of the operator codes defined on
     *        {@code NumberComparisonSC}
     */
    public NumericSampleDataSC(int fieldCode, int value, int operator) {
        this.fieldCode = fieldCode;
        this.columnName = fieldCodeToColumnName(fieldCode);
        this.value = new Integer(value);
        this.operator = operator;
    }

    /**
     * {@inheritDoc}; this version generates a {@code String} that may be used
     * as a portion of the SQL WHERE clause to require that the sample data
     * field indicated by 'fieldCode' has the given 'value'.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        parameters.add(this.value);
        return tableTracker.getTableAlias("sampleData", this) + "."
                + columnName + " " + convertOperatorToString(this.operator)
                + " ?";
    }

    /**
     * Converts a given 'fieldCode' to the name of the database column that
     * contains that field's value.
     * 
     * @throws IllegalStateException if the provided 'fieldCode' does not
     *         represent a numeric field on {@code SampleDataInfo}.
     */
    private static String fieldCodeToColumnName(int fieldCode) {
        switch (fieldCode) {
            case SampleDataInfo.A_FIELD:
                return "a";
            case SampleDataInfo.B_FIELD:
                return "b";
            case SampleDataInfo.C_FIELD:
                return "c";
            case SampleDataInfo.ALPHA_FIELD:
                return "alpha";
            case SampleDataInfo.BETA_FIELD:
                return "beta";
            case SampleDataInfo.GAMMA_FIELD:
                return "gamma";
            case SampleDataInfo.DCALC_FIELD:
                return "dcalc";
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                return "formulaWeight";
            case SampleDataInfo.GOOF_FIELD:
                return "goof";
            case SampleDataInfo.RF_FIELD:
                return "rf";
            case SampleDataInfo.RF2_FIELD:
                return "rf2";
            case SampleDataInfo.RWF_FIELD:
                return "rwf";
            case SampleDataInfo.RWF2_FIELD:
                return "rwf2";
            case SampleDataInfo.T_FIELD:
                return "t";
            case SampleDataInfo.V_FIELD:
                return "v";
            case SampleDataInfo.Z_FIELD:
                return "z";
            default:
                throw new IllegalArgumentException();
        }
    }

    /** Equality is based on class, operation, fieldCode and value. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof NumericSampleDataSC) {
            NumericSampleDataSC nsdsc = (NumericSampleDataSC) obj;

            return ((this.getClass() == nsdsc.getClass())
                    && (this.operator == nsdsc.operator)
                    && (this.fieldCode == nsdsc.fieldCode)
                    && (((this.value == null) && (nsdsc.value == null))
                            || ((this.value != null)
                                    && this.value.equals(nsdsc.value))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + String.valueOf(this.operator)
                + String.valueOf(this.fieldCode)
                + this.value).hashCode();
    }
}
