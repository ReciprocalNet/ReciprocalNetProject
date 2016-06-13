/*
 * Reciprocal Net project
 * 
 * CheckboxHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 17-May-2004: midurbin added generateHtmlToDisplayForLabel() to add support
 *              for HtmlControl's 'displayAsLabel' attribute
 * 25-Jun-2004: cwestnea changed some style in referring to error constants
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 24-Sep-2004: midurbin moved error supplier implementation and 'required'
 *              validation check to HtmlControl
 * 08-Apr-2004: midurbin added generateHtmlToInvisiblyPersistValue(), factoring
 *              some code out of generateHtmlToDisplayForLabel()
 * 17-Feb-2006: jobollin added an appropriate implementation of
 *              isValueSubmitted()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * This class is for the {@code CheckboxHtmlControl} phase-recognizing custom
 * tag. If the {@code value} is true, then it is checked.
 */
public class CheckboxHtmlControl extends HtmlControl {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        super.setFailedValidationHtml("<font color=\"red\">*</font>");
    }

    /**
     * {@inheritDoc}; this version parses the raw value as false if it is
     * {@code null}, or true if it isn't
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control.
     * @return a {@code Boolean} representing the value {@code true} if this
     *         checkbox control is checked, or {@code false} if it isn't
     */
    @Override
    protected Object parseValue(String rawValue) {
        return Boolean.valueOf(rawValue != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            @SuppressWarnings("unused")
            boolean failedValidation, Object value, @SuppressWarnings("unused")
            String rawValue) {
        if (editable) {
            return "<input type=\"checkbox\" name=\""
                    + super.getId()
                    + "\" id=\""
                    + super.getId()
                    + "\""
                    + super.getExtraHtmlAttributesAsString()
                    + (super.getValue() == Boolean.TRUE ? " checked=\"true\""
                            : "") + " />";
        } else {
            return generateHtmlToDisplayForLabel(value)
                    + generateHtmlToInvisiblyPersistValue(value);
        }
    }

    /**
     * {@inheritDoc}; the current implementation returns an '*' if the value is
     * a {@code Boolean} representing the value {@code true} (indicating that
     * this check box is selected) or an empty string otherwise
     */
    @Override
    protected String generateHtmlToDisplayForLabel(Object value) {
        return (Boolean.TRUE.equals(value) ? "*" : "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        if ((value != null) && value.equals(true)) {
            return "<intput type=\"hidden\" name=\"" + getId()
                    + "\" id=\"" + getId() + "\" value=\"true\" />";
        } else {
            return "";
        }
    }
}
