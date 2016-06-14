/*
 * Reciprocal Net Project
 *
 * RadioButtonGroupHtmlControl.java
 *
 * 01-Feb-2006: jobollin wrote first draft
 * 12-Jun-2006: jobollin suppressed an unused argument warning
 */

package org.recipnet.common.controls;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A tag handler for a radio button group.  Radio button groups do not have an
 * HTML representation independent of radio button input elements, thus this tag
 * produces no HTML directly.  It does maintain the current value of the group,
 * however, which allows the member radio buttons to determine whether or not
 * they are selected (i.e. to determine their value).
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class RadioButtonGroupHtmlControl extends HtmlControl {
    
    /**
     * {@inheritDoc}.  This version simply returns the raw value
     * 
     * @see HtmlControl#parseValue(String)
     */
    @Override
    protected Object parseValue(String rawValue) {
        return rawValue;
    }

    /**
     * {@inheritDoc}.  This version sets the initial value on this control early
     * for the benefit of nested radio buttons.  The same value will be set
     * again with the same priority at the end of the registration phase, but
     * that's harmless.
     * 
     * @see HtmlControl#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        setValue(getInitialValue(), DEFAULT_VALUE_PRIORITY, LOWEST_PRIORITY);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version causes this tag's body to be
     * <em>un</em>buffered during the {@code RENDERING_PHASE}, unlike most
     * {@code HtmlControl}s'.
     * 
     * @see HtmlControl#onRenderingPhaseBeforeBody(JspWriter)
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out)
            throws IOException, JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        
        return ((rc == EVAL_BODY_BUFFERED) ? EVAL_BODY_INCLUDE : rc);
    }

    /**
     * Determines whether the specified radio button control's 'option' property
     * matches this button group's value; an affirmative response indicates that
     * the radio button is the selected one of this group -- provided that it is
     * a member of this group in the first place
     * 
     * @param  button the {@code RadioButtonHtmlControl} to test for being the
     *         selected member of this group
     *         
     * @return {@code true} if the specified button's 'option' property is
     *         correct for the selected radio button in this group.
     */
    public boolean isSelectedOption(RadioButtonHtmlControl button) {
        return (getValue() != null) && getValue().equals(button.getOption());
    }
    
    /**
     * {@inheritDoc}.  This version returns an empty string
     * 
     * @see HtmlControl#generateHtmlToDisplayAndPersistValue(boolean, boolean,
     *      Object, String)
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused") boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            @SuppressWarnings("unused") Object value,
            @SuppressWarnings("unused") String rawValue) {
        return "";
    }

    /**
     * {@inheritDoc}.  This version returns an empty string
     * 
     * @see HtmlControl#generateHtmlToDisplayForLabel(Object)
     */
    @Override
    protected String generateHtmlToDisplayForLabel(
            @SuppressWarnings("unused") Object val) {
        return "";
    }

    /**
     * {@inheritDoc}.  This version returns an empty string
     * 
     * @see HtmlControl#generateHtmlToInvisiblyPersistValue(Object)
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(
            @SuppressWarnings("unused") Object value) {
        return "";
    }

    /**
     * {@inheritDoc}.  This version returns an empty string
     * 
     * @see HtmlControl#generateHtmlUsingFormatter(Object)
     */
    @Override
    protected String generateHtmlUsingFormatter(
            @SuppressWarnings("unused") Object value) {
        return "";
    }
    
}
