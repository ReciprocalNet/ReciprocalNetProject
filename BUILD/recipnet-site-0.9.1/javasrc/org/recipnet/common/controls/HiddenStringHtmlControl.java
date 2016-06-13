/*
 * Reciprocal Net project
 * 
 * HiddenStringHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 08-Apr-2005: midurbin renamed generateHtmlToDisplay() to
 *              generateHtmlToDisplayAndPersistValue()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * This Tag is used to maintain a {@code String} value without exposing it to
 * the user.
 */
public class HiddenStringHtmlControl extends HtmlControl {

    /**
     * {@inheritDoc}; this version simply returns the body content.
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        /*
         * the body content needs no modification to be used as the value for
         * this control.
         */
        return bodyContent;
    }

    /**
     * {@inheritDoc}; this version just returns the {@code rawValue}
     */
    @Override
    protected Object parseValue(String rawValue) {
        return rawValue;
    }

    /**
     * {@inheritDoc}; this version simply delegates to
     * {@code #generateHtmlToInvisiblyPersistValue(Object)} because the value is
     * meant to be hidden.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused") boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            Object value, @SuppressWarnings("unused") String rawValue) {
        return generateHtmlToInvisiblyPersistValue(value);
    }

    /**
     * {@inheritDoc}; this version returns an empty string because this
     * control's value is hidden.
     */
    @Override
    protected String generateHtmlToDisplayForLabel(
            @SuppressWarnings("unused") Object value) {
        return "";
    }
}
