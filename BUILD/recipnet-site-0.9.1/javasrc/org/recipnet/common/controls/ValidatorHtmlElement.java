/*
 * Reciprocal Net Project
 *
 * ValidatorHtmlElement.java
 *
 * 18-Jan-2006: jobollin wrote first draft
 */
package org.recipnet.common.controls;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.Validator;

/**
 * A phase-recognizing tag that defines provides a {@code Validator} to the
 * other tags in an {@code HtmlPage}.  If nested within an {@code HtmlControl},
 * this tag sets that control's validator during the {@code REGISTRATION_PHASE}.
 * If this tag is assigned an ID, then its {@code Validator} is thereby
 * available wherever the ID is defined.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class ValidatorHtmlElement extends HtmlPageElement {
    
    /**
     * The {@code Validator} maintained by this tag.  This is a required tag
     * attribute and must not be {@code null}
     */
    private Validator validator;

    /**
     * Obtains the validator maintained by this tag; subclasses could usefully
     * override this method to provide a specific {@code Validator}
     * implementation or even a particular, fixed, {@code Validator} instance
     * 
     * @return the {@code Validator}
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * Sets the validator to be maintained by this tag
     * 
     * @param  validator the {@code Validator}
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * {@inheritDoc}.  This version looks for a surrounding {@code HtmlControl},
     * and sets its validator to the one returned by {@link #getValidator()} if
     * one is found
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        HtmlControl hostControl
                = findRealAncestorWithClass(this, HtmlControl.class);
        
        if (hostControl != null) {
            hostControl.setValidator(getValidator());
        } else {
            throw new IllegalStateException("No control to validate");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version resets this tag's validator
     * 
     * @see org.recipnet.common.controls.HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        validator = null;
    }

}
