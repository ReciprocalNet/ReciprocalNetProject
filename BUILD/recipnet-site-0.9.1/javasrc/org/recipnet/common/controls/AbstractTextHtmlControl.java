/*
 * Reciprocal Net Project
 *
 * AbstractTextHtmlControl.java
 *
 * 23-Jan-2006: jobollin wrote first draft
 */
package org.recipnet.common.controls;

/**
 * An abstract class representing text-based HTML controls.  For all such
 * controls, this class provides for configuring a maximum value length and
 * flags governing whether the control's value should be trimmed of leading and
 * trailing whitespace and whether empty values should be internally converted
 * to {@code null}s.
 * 
 * @author jobollin
 * @version 1.0
 */
public abstract class AbstractTextHtmlControl extends HtmlControl {

    /**
     * An error flag that may be reported when the parsed value is invalid
     * because it contains more than {@code maxLength} characters. This is used
     * in the implementation of {@code ErrorSupplier}.
     */
    public static final int VALUE_IS_TOO_LONG
            = HtmlControl.getHighestErrorFlag() << 1;
    
    /**
     * a static code that may be used to indicate that no upper limit should be
     * enforced as a size constraint.
     */
    public static final int NO_LIMIT = Integer.MAX_VALUE;
    
    /**
     * This is an optional attribute that is initialized by {@code reset()} and
     * may be altered by its 'setter' method, {@code setRows()}. It is a
     * 'transient' variable in that it may change from phase to phase and may be
     * copied by a call to {@code copyTransientPropertiesFrom()}.
     */
    private int maxLength;
    
    /**
     * This is an optional attribute indicating whether submitted values will
     * have leading and trailing whitespace trimmed, that is initialized by
     * {@code reset()} and may be altered by its 'setter' method,
     * {@code setShouldTrim()}. It is a 'transient' variable in that it may
     * change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean shouldTrim;
    
    /**
     * This is an optional attribute that is initialized by  {@code  reset()}  and may be altered by its 'setter' method,  {@code  setRows()} . It is a 'transient' variable in that it may change from phase to phase and may be copied by a call to  {@code  copyTransientPropertiesFrom()} .
     */
    private boolean shouldConvertBlankToNull;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.maxLength = NO_LIMIT;
        this.shouldTrim = true;
        this.shouldConvertBlankToNull = true;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return VALUE_IS_TOO_LONG;
    }

    /** @return the maximum number of characters that may be entered */
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * @param maxLength the maximum number of characters that may be entered
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return whether or not leading and trailing whitespace should be trimmed
     */
    public boolean isShouldTrim() {
        return this.shouldTrim;
    }

    /**
     * @param shouldTrim whether or not leading and trailing whitespace should
     *        be trimmed
     */
    public void setShouldTrim(boolean shouldTrim) {
        this.shouldTrim = shouldTrim;
    }

    /** @return whether or not blank input will be stored as null */
    public boolean isShouldConvertBlankToNull() {
        return this.shouldConvertBlankToNull;
    }

    /**
     * @param shouldConvertBlankToNull whether or not blank input will be stored
     *        as null.
     */
    public void setShouldConvertBlankToNull(boolean shouldConvertBlankToNull) {
        this.shouldConvertBlankToNull = shouldConvertBlankToNull;
    }

    /**
     * Adjusts the specified value string to produce an object appropriate for
     * a value of this control.  This version applies the processing directed
     * by this control's {@code shouldConvertBlankToNull} and
     * {@code shouldTrim} properties
     * 
     * @param  valueString a non-{@code null} {@code String} to be mangled
     *         appropriately for use as the value of this control
     * 
     * @return the mangled value {@code Object}
     */
    protected Object mangleValueString(String valueString) {
        String tempValue = (this.shouldTrim ? valueString.trim() : valueString);
        
        return ((this.shouldConvertBlankToNull && (tempValue.length() == 0))
                ? null : tempValue);
    }

    /**
     * {@inheritDoc}.  This version trims the body content as directed by this
     * control's {@code shouldTrim} property, and returns {@code null} if the
     * value is empty.
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        return mangleValueString(bodyContent);
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation parses
     * {@code rawValue}, potentially trimming whitespace or converting a blank
     * value to null.
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control.
     *        
     * @return the parsed value {@code Object}
     */
    @Override
    protected Object parseValue(String rawValue) {
        // handle a null value to avoid NPEs
        return ((rawValue == null) ? null : mangleValueString(rawValue));
    }

    /**
     * {@inheritDoc}.  This version checks the length of the parsed value
     * against the 'maxLength' property in addition to the superclass's
     * validation checks.
     */
    @Override
    protected boolean isParsedValueValid(Object parsedValue) {
        boolean isSuperValid = super.isParsedValueValid(parsedValue);
        
        /*
         * The parsed value might not be a String for some specialized
         * subclasses; in such cases (or if the parsed value is null) the
         * maxLength check is irrelevant
         */ 
        if ((parsedValue instanceof String)
                && (((String) parsedValue).length() > this.maxLength)) {
            setErrorFlag(VALUE_IS_TOO_LONG);
            return false;
        } else {
            return isSuperValid;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlControl#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        AbstractTextHtmlControl src = (AbstractTextHtmlControl) source;
        
        this.maxLength = src.maxLength;
        this.shouldTrim = src.shouldTrim;
        this.shouldConvertBlankToNull = src.shouldConvertBlankToNull;
    }

}
