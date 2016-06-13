/*
 * Reciprocal Net project
 * 
 * LabelHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 17-May-2004: midurbin modified parseValue() and generateHtmlToDisplay() so
 *              that, like other HtmlControls the current value would be
 *              preserved over an HTTP roundtrip.
 * 08-Apr-2005: midurbin renamed generateHtmlToDisplay() to
 *              generateHtmlToDisplayAndPersistValue()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * This is a custom tag for a label control. The value of a label is a
 * {@code String}. A label is useful because it escapes its text for safe
 * display on an HTML page.
 */
public class LabelHtmlControl extends HtmlControl {

    /**
     * {@inheritDoc}; this version simply returns the body content.
     * 
     * @param bodyContent a {@code String} representing the evaluated output of
     *        the body of this tag
     * @return the evaluated body contents
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        /*
         * the body content needs no modification to be used as the value for
         * this control
         */
        return bodyContent;
    }

    /**
     * {@inheritDoc}; this version returns the {@code rawValue}
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control. In this case it will
     *        always be null and is ignored.
     * @return the parsed value, or null if there was a parsing or validation
     *         error. In this case it will always be simply the value of this
     *         control.
     */
    @Override
    protected Object parseValue(String rawValue) {
        return rawValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused") boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            Object value,
            @SuppressWarnings("unused") String rawValue) {
        String valueToDisplay = (String) value;
        
        if (valueToDisplay == null) {
            return "";
        }
        return "<span " + getExtraHtmlAttributesAsString() + ">"
                + HtmlControl.escapeNestedValue(valueToDisplay) + "</span>"
                + "<input type=\"hidden\" name=\"" + getId() + "\" id=\""
                + getId() + "\" value=\""
                + HtmlControl.escapeAttributeValue(valueToDisplay) + "\" />";
    }
}
