/*
 * Reciprocal Net project
 * 
 * SampleDataRangeSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

/**
 * A {@code SearchConstraint} to limit the search results to those samples that
 * have {@code SampleDataInfo} numeric data values that fall within the given
 * range.
 */
public class SampleDataRangeSC extends SearchConstraintGroup {

    /** The value that is required for the data field. */
    private final double value;

    /**
     * A percentage that can be used to construct the range of acceptable
     * values. This is done by adding this percentage of the 'value' to the
     * 'value' to determine the upperbound and subtracting the same percent to
     * determine the lowerbound.
     */
    private final double marginOfError;

    /**
     * A {@code SampleDataInfo} field code that indicates a field that is a
     * double.
     */
    private final int fieldCode;

    /**
     * A constructor that fully initializes a {@code SampleDataRangeSC}.
     */
    public SampleDataRangeSC(int fieldCode, double value, double marginOfError) {
        super(AND);
        this.value = value;
        this.marginOfError = marginOfError;
        this.fieldCode = fieldCode;
        addChild(new NumericSampleDataSC(this.fieldCode,
                (this.value * (1 - (this.marginOfError / 100))),
                NumberComparisonSC.GREATER_THAN_OR_EQUAL_TO));
        addChild(new NumericSampleDataSC(this.fieldCode,
                (this.value * (1 + (this.marginOfError / 100))),
                NumberComparisonSC.LESS_THAN_OR_EQUAL_TO));
    }

    /** Gets the field code that was initialized by the constructor. */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * Gets the 'value' that was initialized by a call to {@code setValue()}.
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Gets the 'marginOfError' that was initialized by a call to
     * {@code setMarginOfError()}.
     */
    public double getMarginOfError() {
        return this.marginOfError;
    }

    /** Equality is based on class, fieldCode, value and marginOfError. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof SampleDataRangeSC) {
            SampleDataRangeSC sdrsc = (SampleDataRangeSC) obj;

            return ((this.getClass() == sdrsc.getClass())
                    && (this.fieldCode == sdrsc.fieldCode)
                    && (Double.compare(this.value, sdrsc.value) == 0)
                    && (Double.compare(this.marginOfError, sdrsc.marginOfError)
                            == 0));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass()) + String.valueOf(this.fieldCode)
                + String.valueOf(this.value) + String.valueOf(this.marginOfError)).hashCode();
    }
}
