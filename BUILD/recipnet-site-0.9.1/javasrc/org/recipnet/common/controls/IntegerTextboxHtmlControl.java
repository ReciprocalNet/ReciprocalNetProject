/*
 * Reciprocal Net project
 * 
 * IntegerTextboxHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 04-Aug-2004: cwestnea fixed bug #1308 in convertBodyContentToValue()
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and modified class to 
 *              conform to new ErrorSupplier inheritance rules
 * 26-Jul-2005: midurbin moved the validation checking code out of parseValue()
 *              and into isParsedValueValid()
 * 01-Mar-2006: jobollin removed minValueSet and maxValueSet fields, relying
 *              instead on sensible default values for minValue and maxValue;
 *              reformatted the source
 */

package org.recipnet.common.controls;

/**
 * An {@code IntegerTextboxHtmlControl} maintains an {@code Integer} value that
 * may be modified in a textbox.
 */
public class IntegerTextboxHtmlControl extends TextboxHtmlControl {

    /**
     * An error flag that may be reported when the value cannot be parsed as an
     * Integer. This, along with flags defined on the superclass, is used in the
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

    /**
     * An inclusive minimum value may be optionally set as an attribute.
     * Initialized by {@code reset()} and altered by calls to its 'setter'
     * method, {@code setMinValue()}. This is a 'transient' variable in that it
     * may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private int minValue;

    /**
     * An inclusive maximum value may be optionally set as an attribute.
     * Initialized by {@code reset()} and altered by calls to its 'setter'
     * method, {@code setMinValue()}. This is a 'transient' variable in that it
     * may change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private int maxValue;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }

    /**
     * @return the minimum allowed value for this textbox, value is meaningless
     *         if {@code minValue} has not been set
     */
    public int getMinValue() {
        return this.minValue;
    }

    /** @param minValue the minimum allowed value for this textbox */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maximum allowed value for this textbox, value is meaningless
     *         if {@code maxValue} has not been set
     */
    public int getMaxValue() {
        return this.maxValue;
    }

    /** @param maxValue the maximum allowed value for this textbox */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * {@inheritDoc}; this version converts the body content {@code String}
     * into an {@code Integer}.
     * 
     * @param bodyContent a {@code String} representing the evaluated output of
     *        the body of this tag
     *        
     * @return the parsed value, or null to indicate that the body content may
     *         not be used for a value.
     * 
     * @throws NumberFormatException if the body content is nonempty and
     *         nonblank but cannot be interpreted as an {@code Integer}
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        if ((bodyContent == null) || (bodyContent.trim().length() == 0)) {
            return null;
        } else {
            return Integer.valueOf(bodyContent.trim());
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation parses
     * {@code rawValue} into a {@code Integer}. If the 'rawValue' cannot be
     * converted into a {@code Integer}, 'failedValidation' is set to true adn
     * the {@code VALUE_IS_NOT_A_NUMBER} error flag is set.
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control.
     * @return the parsed value, or null if there was a parsing error.
     */
    @Override
    protected Object parseValue(String rawValue) {
        String val = (String) super.parseValue(rawValue);
        if (val == null) {
            return val;
        }
        try {
            return Integer.valueOf(rawValue);
        } catch (NumberFormatException ex) {
            super.setUnparseable(true);
            super.setErrorFlag(IntegerTextboxHtmlControl.VALUE_IS_NOT_A_NUMBER);
            return null;
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation checks whether
     * the value falls within the range specified by the 'maxValue' and
     * 'minValue' properties on top of the superclass' validation checks.
     */
    @Override
    protected boolean isParsedValueValid(Object parsedValue) {
        if (!super.isParsedValueValid(parsedValue)) {
            return false;
        } else if (parsedValue != null) {
            Integer x = (Integer) parsedValue;

            if (x.intValue() < this.minValue) {
                super.setErrorFlag(IntegerTextboxHtmlControl.VALUE_IS_TOO_LOW);
                return false;
            }
            if (x.intValue() > this.maxValue) {
                super.setErrorFlag(IntegerTextboxHtmlControl.VALUE_IS_TOO_HIGH);
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
     * converts the {@code valueToDisplay} which is an {@code Integer} into a
     * {@code String}.
     * 
     * @param valueToDisplay an {@code Object} to be displayed
     * @return a {@code String} representing the value of the parameter.
     */
    @Override
    protected String convertValue(Object valueToDisplay) {
        if (valueToDisplay == null) {
            return "";
        }
        return ((Integer) valueToDisplay).toString();
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code IntegerTextboxHtmlControl}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        IntegerTextboxHtmlControl src = (IntegerTextboxHtmlControl) source;
        this.minValue = src.minValue;
        this.maxValue = src.maxValue;
    }
}
