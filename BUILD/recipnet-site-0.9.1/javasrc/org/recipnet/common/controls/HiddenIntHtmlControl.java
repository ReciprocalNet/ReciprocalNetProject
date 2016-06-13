/*
 * Reciprocal Net project
 * 
 * HiddenIntHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 08-Apr-2005: midurbin  modified generateHtmlToDisplay() to delegate to
 *              generateHtmlToInvisiblyPersistValue() and fixed bug #1559 in
 *              parseValue()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * This Tag is used to maintain a {@code Integer} value without exposing it to
 * the user.
 */
public class HiddenIntHtmlControl extends HtmlControl {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object convertBodyContentToValue(String bodyContent) {
        return Integer.valueOf(bodyContent);
    }

    /**
     * {@inheritDoc}; this version parses {@code rawValue} into an
     * {@code Integer}
     */
    @Override
    protected Object parseValue(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        return new Integer(rawValue);
    }

    /**
     * {@inheritDoc}; this version simply delegates to
     * {@link #generateHtmlToInvisiblyPersistValue(Object)} because the value is
     * meant to be hidden.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused")
            boolean editable, @SuppressWarnings("unused")
            boolean failedValidation, Object value, @SuppressWarnings("unused")
            String rawValue) {
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
