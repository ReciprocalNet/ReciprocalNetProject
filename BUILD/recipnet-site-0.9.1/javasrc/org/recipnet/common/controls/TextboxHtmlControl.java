/*
 * Reciprocal Net project
 * 
 * TextboxHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 17-May-2004: midurbin added generateHtmlToDisplayForLabel() to add support
 *              for HtmlControl's 'displayAsLabel' attribute
 * 24-Jun-2004: cwestnea modified generateHtmlToDisplay() to reflect name 
 *              change of HtmlPage.isBrowserNetscape4x()
 * 25-Jun-2004: cwestnea changed the style of referring to error constants
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 24-Sep-2004: midurbin moved error supplier implementation and 'required'
 *              validation check to HtmlControl
 * 16-Nov-2004: midurbin fixed bug #1463 in generateHtmlToDisplay()
 * 08-Apr-2005: midurbin renamed generateHtmlToDisplay() to
 *              generateHtmlToDisplayAndPersistValue() and updated
 *              generateHtmlToDisplayForLabel() to conform to new specification
 * 26-Jul-2005: midurbin moved the validation checking code out of parseValue()
 *              and into isParsedValueValid()
 * 16-Sep-2005: midurbin fixed bug #1337 by overriding
 *              generateHtmlToInvisiblyPersistValue()
 * 19-Jan-2006: jobollin changed the value of NO_LIMIT to Integer.MAX_VALUE
 * 23-Jan-2006: jobollin fixed bug #1725; moved considerable functionality to
 *              new superclass AbstractTextHtmlControl
 */

package org.recipnet.common.controls;

/**
 * A {@code TextboxHtmlControl} maintains a {@code String} value that may be
 * modified in a textbox. Constraints for the display of the textbox and the
 * value may be provided as attributes to the tag.
 */
public class TextboxHtmlControl extends AbstractTextHtmlControl {

    /**
     * This is an optional attribute that is initialized by {@code reset()} and
     * may be altered by its 'setter' method, {@code setSize()}. It is a
     * 'transient' variable in that it may change from phase to phase and may be
     * copied by a call to {@code copyTransientPropertiesFrom()}.
     */
    private int size;

    /**
     * This is an optional attribute indicating whether characters should be
     * masked while being input (as in a password), that is initialized by
     * {@code reset()} and may be set by its 'setter' method,
     * {@code setMaskInput()}. Parsed values are not sent back to the browser
     * by {@code generateHtmlToDisplayAndPersistValue()}, but instead an empty
     * field is displayed.
     */
    private boolean maskInput;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        super.setFailedValidationHtml("<font color=\"RED\">*</font>");
        this.size = 16;
        this.maskInput = false;
    }

    /** @return the size of the textbox */
    public int getSize() {
        return this.size;
    }

    /** @param size the size of the textbox */
    public void setSize(int size) {
        this.size = size;
    }

    /** @return a boolean indicating whether input will be masked */
    public boolean getMaskInput() {
        return this.maskInput;
    }

    /** @param maskInput indicates whether input should be masked */
    public void setMaskInput(boolean maskInput) {
        this.maskInput = maskInput;
    }

    /**
     * This is a dummy implementation of a convenience function to convert the
     * {@code value} object into {@code String} for display. Child classes may
     * overload it to deal with their respective object types.
     * 
     * @param valueToDisplay an {@code Object} to be displayed
     * 
     * @return a {@code String} representing the value of the parameter.
     */
    protected String convertValue(Object valueToDisplay) {
        return ((valueToDisplay == null) ? "" : valueToDisplay.toString());
    }

    /**
     * Overrides {@code HtmlControl} to generate HTML for this control.
     * 
     * @param editable indicates whether the field should be editable. If the
     *        field is not editable the value is displayed and a hidden field is
     *        used.
     * @param failedValidation indicates that the field has an invalid value.
     * @param value the value of the control.
     * @param rawValue the value from the POST parameters, this will differ from
     *        the value in cases of validation failures to preserve the validity
     *        of the {@code value}.
     *        
     * @return a {@code String} of HTML code for a textbox or plain text,
     *         depending on the editability of the field.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            boolean failedValidation, Object value, String rawValue) {
        String valueToDisplay = (this.maskInput ? null
                : (failedValidation ? rawValue : convertValue(value)));
        
        if (valueToDisplay == null) {
            valueToDisplay = "";
        }
        if (isShouldTrim()) {
            valueToDisplay = valueToDisplay.trim();
        }
        if (getPage().isBrowserNetscape4x() && !editable) {
            // Netscape 4.8 does not support the 'disabled' attribute for
            // button input tags. Therefore, the equivalent disabled button
            // for this browser is simply not to display the button.
            return "<input type=\"hidden\" name=\""
                    + this.id
                    + "\" value=\""
                    + HtmlControl.escapeAttributeValue(valueToDisplay)
                    + "\" />"
                    + (this.maskInput ? "<i>(hidden)</i>"
                            : HtmlControl.escapeNestedValue(valueToDisplay));
        } else {
            return ("<input name=\""
                    + getId()
                    + "\""
                    + " id=\""
                    + getId()
                    + "\""
                    + " type=\""
                    + (maskInput ? "password" : "text")
                    + "\""
                    + " size=\""
                    + this.size
                    + "\""
                    + ((getMaxLength() != NO_LIMIT) ? (" maxlength=\""
                            + getMaxLength() + "\" ") : (""))
                    + (editable ? "" : " readonly=\"readonly\"") + " value=\""
                    + HtmlControl.escapeAttributeValue(valueToDisplay) + "\""
                    + getExtraHtmlAttributesAsString() + " />");
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation returns an
     * escaped textual representation of {@code value}.
     * 
     * @param value the current value of this textbox
     */
    @Override
    protected String generateHtmlToDisplayForLabel(Object value) {
        String valueToDisplay
                = (this.maskInput ? "" : convertValue(value));
        
        if (valueToDisplay == null) {
            valueToDisplay = "";
        }
        if (isShouldTrim()) {
            valueToDisplay = valueToDisplay.trim();
        }
        return "<span "
                + getExtraHtmlAttributesAsString()
                + ">"
                // FIXME: remove <i> tags; use CSS instead:
                + (this.maskInput ? "<i>(hidden)</i>"
                        : HtmlControl.escapeNestedValue(valueToDisplay))
                + "</span>";
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation returns a
     * hidden field containing an escaped textual representation of the
     * {@code Value}.
     * 
     * @param value the current value of the textbox
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        String valueToDisplay = convertValue(value);
        
        if (valueToDisplay == null) {
            valueToDisplay = "";
        }
        if (isShouldTrim()) {
            valueToDisplay = valueToDisplay.trim();
        }
        return "<input type=\"hidden\" id=\"" + getId() + "\" name=\""
                + getId() + "\" value=\""
                + escapeAttributeValue(valueToDisplay) + "\" />";
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code TextboxHtmlControl}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        TextboxHtmlControl src = (TextboxHtmlControl) source;
        this.size = src.size;
    }
}
