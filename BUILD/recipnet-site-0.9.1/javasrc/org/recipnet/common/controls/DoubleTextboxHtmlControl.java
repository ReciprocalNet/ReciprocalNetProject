/*
 * Reciprocal Net project
 * 
 * DoubleTextboxHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 04-Aug-2004: cwestnea fixed bug #1308 in convertBodyContentToValue()
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and modified class to 
 *              conform to new ErrorSupplier inheritance rules
 * 02-Nov-2004: midurbin added generateCopy() to fix bug #1439
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 26-Jul-2005: midurbin moved the validation checking code out of parseValue()
 *              and into isParsedValueValid()
 * 16-Sep-2005: midurbin fixed bug #1337 in convertValue() 
 * 01-Mar-2006: jobollin removed minValueSet and maxValueSet fields, relying
 *              instead on sensible default values for minValue and maxValue;
 *              reformatted the source
 * 12-Jun-2006: jobollin removed an unnecessary cast from convertValue()
 */

package org.recipnet.common.controls;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * An {@code DoubleTextboxHtmlControl} maintains a {@code Double} value that may
 * be modified in a textbox. The special value {@code Double.NaN} is considered
 * to be equivalent to {@code null}, indicating that no value has been set for
 * this control.
 */
public class DoubleTextboxHtmlControl extends TextboxHtmlControl {

    /**
     * An error flag that may be reported when the value cannot be parsed as a
     * Double. This, along with flags defined on the superclass, is used in the
     * implementation of {@code ErrorSupplier}.
     */
    public static final int VALUE_IS_NOT_A_NUMBER
            = TextboxHtmlControl.getHighestErrorFlag() << 1;

    /**
     * An error flag that may be reported when {@code maxValue} has been set and
     * the value of this control exceeds it. This, along with flags defined on
     * the superclass, is used in the implementation of {@code ErrorSupplier}.
     */
    public static final int VALUE_IS_TOO_HIGH
            = TextboxHtmlControl.getHighestErrorFlag() << 2;

    /**
     * An error flag that may be reported when {@code minValue} has been set and
     * the value of this control is below it. This, along with flags defined on
     * the superclass, is used in the implementation of {@code ErrorSupplier}.
     */
    public static final int VALUE_IS_TOO_LOW
            = TextboxHtmlControl.getHighestErrorFlag() << 3;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return VALUE_IS_TOO_LOW;
    }

    /** Used to format the output of value. */
    private DecimalFormat decimalFormat;

    /**
     * An inclusive minimum value may be optionally set as an attribute.
     * Initialized by {@code reset()} and altered by calls to its 'setter'
     * method, {@code setMinValue()}. This is a 'transient' variable in that it
     * may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private double minValue;

    /**
     * An inclusive maximum value may be optionally set as an attribute.
     * Initialized by {@code reset()} and altered by calls to its 'setter'
     * method, {@code setMinValue()}. This is a 'transient' variable in that it
     * may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private double maxValue;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.minValue = -Double.MAX_VALUE;
        this.maxValue = Double.MAX_VALUE;
        this.decimalFormat = new DecimalFormat();
        this.decimalFormat.setMinimumFractionDigits(3);
        this.decimalFormat.setMaximumFractionDigits(5);
        this.decimalFormat.setGroupingUsed(true);
    }

    /**
     * @return the minimum allowed value for this textbox, value is meaningless
     *         if {@code minValue} has not been set.
     */
    public double getMinValue() {
        return this.minValue;
    }

    /** @param minValue the minimum allowed value for this textbox */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maximum allowed value for this textbox, value is meaningless
     *         if {@code maxValue} has not been set
     */
    public double getMaxValue() {
        return this.maxValue;
    }

    /** @param maxValue the maximum allowed value for this textbox */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minimum number of digits to be displayed to the right of the
     *         decimal point.
     */
    public int getMinFractionalDigits() {
        return this.decimalFormat.getMinimumFractionDigits();
    }

    /**
     * Sets the minimum number of digits to display to the right of the
     * decimal point. This is an optional attribute that is initialized by
     * {@code reset()} and may be set by its 'setter' method,
     * {@code setMinFractionalDigits()}.  It is 'transient' in that
     * it may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     * 
     * @param minFractionalDigits the minimum number of digits to be displayed
     *        to the right of the decimal point.
     */
    public void setMinFractionalDigits(int minFractionalDigits) {
        this.decimalFormat.setMinimumFractionDigits(minFractionalDigits);
    }

    /**
     * @return the maximum number of digits to be displayed to the right of the
     *         decimal point.
     */
    public int getMaxFractionalDigits() {
        return this.decimalFormat.getMaximumFractionDigits();
    }

    /**
     * Sets the maximum number of digits to display to the right of the
     * decimal point. This is an optional attribute that is initialized by
     * {@code reset()} and may be set by this method. It is 'transient' in that
     * it may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     * 
     * @param maxFractionalDigits the maximum number of digits to be displayed
     *        to the right of the decimal point.
     */
    public void setMaxFractionalDigits(int maxFractionalDigits) {
        this.decimalFormat.setMaximumFractionDigits(maxFractionalDigits);
    }

    /**
     * Determines whether this field will format its value with grouping
     * symbols (i.e. separating groups of three or four digits to the left of
     * the decimal point)
     * 
     * @return {@code true} if this field is configured to use grouping,
     *         {@code false} if not
     */
    public boolean isGroupingUsed() {
        return this.decimalFormat.isGroupingUsed();
    }

    /**
     * Sets whether this field will format its value with grouping symbols.
     * This is an optional attribute that is initialized in reset() and may
     * thereafter by modified by this method. It is 'transient' in that
     * it may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     * 
     * @param used {@code true} if this field should format its value with
     *        grouping symbols, {@code false} if not
     */
    public void setGroupingUsed(boolean used) {
        this.decimalFormat.setGroupingUsed(used);
    }
    
    /**
     * {@inheritDoc}; this version converts the body content {@code String}
     * into a {@code Double}.
     * 
     * @param  bodyContent a {@code String} representing the evaluated output of
     *         the body of this tag
     * @return the parsed value, or null to indicate that the body content may
     *         not be used for a value.
     *         
     * @throws NumberFormatException if the body content cannot be interpreted
     *         as a {@code Double}
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        if ((bodyContent == null) || (bodyContent.trim().length() == 0)) {
            return null;
        } else {
            return Double.valueOf(bodyContent.trim());
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation parses
     * {@code rawValue} into a {@code Double}. If the 'rawValue' cannot be
     * converted into a {@code Double} 'failedValidation' is set to true and the
     * {@code VALUE_IS_NOT_A_NUMBER} error flag is set.
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control.
     * @return the parsed value, or null if the value could not be parsed as a
     *         Double
     */
    @Override
    protected Object parseValue(String rawValue) {
        String val = (String) super.parseValue(rawValue);
        
        if (val == null) {
            return null;
        } else {
            try {
                return Double.valueOf(rawValue);
            } catch (NumberFormatException ex) {
                super.setUnparseable(true);
                super.setErrorFlag(
                        DoubleTextboxHtmlControl.VALUE_IS_NOT_A_NUMBER);
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}.  In addition to the superclass's validation rules, this
     * version requires any non-{@code null} value to be in the (inclusive)
     * range defined by the configured minimum and maximum values
     */
    @Override
    protected boolean isParsedValueValid(Object parsedValue) {
        if (!super.isParsedValueValid(parsedValue)) {
            return false;
        } else if (parsedValue != null) {
            Double x = (Double) parsedValue;
            
            if (x.doubleValue() < this.minValue) {
                super.setErrorFlag(DoubleTextboxHtmlControl.VALUE_IS_TOO_LOW);
                return false;
            }
            if (x.doubleValue() > this.maxValue) {
                super.setErrorFlag(DoubleTextboxHtmlControl.VALUE_IS_TOO_HIGH);
                return false;
            }
        }
        
        /*
         * We can't validate further, so we return the result of the
         * superclass's validation, which necessarily was successful if we got
         * to this point.
         */
        return true;
    }

    /**
     * Overrides {@code TextboxHtmlControl}; the current implementation
     * converts the {@code valueToDisplay} which is an {@code Double} into a
     * {@code String}.
     * 
     * @param valueToDisplay an {@code Object} to be displayed; expected to be
     *        of type {@code Double}
     * @return a {@code String} representing the value of the parameter.
     */
    @Override
    protected String convertValue(Object valueToDisplay) {
        if ((valueToDisplay == null) || ((Double) valueToDisplay).isNaN()) {
            return "";
        }
        return this.decimalFormat.format(valueToDisplay);
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code DoubleTextboxHtmlControl}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        if (source instanceof DoubleTextboxHtmlControl) {
            DoubleTextboxHtmlControl src = (DoubleTextboxHtmlControl) source;

            this.minValue = src.minValue;
            this.maxValue = src.maxValue;
            this.decimalFormat = src.decimalFormat;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        DoubleTextboxHtmlControl dc = (DoubleTextboxHtmlControl) super.generateCopy(
                newId, map);

        if (this.decimalFormat != null) {
            dc.decimalFormat = (DecimalFormat) this.decimalFormat.clone();
        }

        return dc;
    }
}
