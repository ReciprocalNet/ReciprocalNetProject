/*
 * Reciprocal Net Project
 *
 * ValidationSupressor.java
 *
 * 29-Mar-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * A {@code ValidationContext} implementation that by default traps validation
 * errors and does <em>not</em> forward them to any surrounding context
 * (contrary to the normally-expected behavior of validation contexts).  This
 * trapping behavior can be controlled via a tag attribute.  No errors are in
 * any case forwarded until the {@code FETCHING_PHASE}, at which point the
 * cumulative validation state is reported if this tag's error trapping is not
 * enabled.  After that, error forwarding occurs normally whenever this tag is
 * not enabled.
 * </p><p>
 * This tag implements {@code ErrorSupplier} by providing an error flag that is
 * raised if any nested tag reports a validation error (whether or not that
 * error is forwarded to any surrounding context).
 * </p>
 * 
 * @author jobollin
 * @version 1.0
 */
public class ValidationSupressor extends HtmlPageElement
        implements ValidationContext, ErrorSupplier {
    
    /**
     * An {@code ErrorSupplier} error code indicating that a validation error
     * was reported to this context
     */
    public final static int VALIDATION_ERROR_REPORTED = 1 << 0;
    
    /**
     * The innermost surrounding {@code ValidationContext}, if any
     */
    private ValidationContext hostContext;
    
    /**
     * A latch recording whether any validation error has been reported to this
     * context
     */
    private boolean validationErrorReported;
    
    /**
     * A latch that is enabled before this tag starts processing its body in the
     * {@code FETCHING_PHASE} if it has a containing context to which it could
     * report errors; after that point, this tag will consider forwarding
     * validation errors to the surrounding context as they are received, based
     * on whether or not this tag is enabled.
     */
    private boolean considerForwarding;

    /**
     * A transient tag attribute that determines whether this tag's
     * error-trapping behavior is enabled; defaults to {@code true}
     */
    private boolean enabled;
    
    private int errorCode;
    
    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        hostContext = null;
        enabled = true;
        considerForwarding = false;
        validationErrorReported = false;
        errorCode = 0;
    }

    /**
     * Determines whether this tag's error-trapping behavior is enabled
     * 
     * @return {@code true} if the trapping behavior is enabled (the default),
     *         or {@code false} if it is disabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this tag's error-trapping behavior is enabled (by default
     * it is enabled)
     * 
     * @param  enabled {@code true} if error trapping should be enabled,
     *         {@code false} if not
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * {@inheritDoc}.  This version looks up the innermost surrounding
     * {@code ValidationContext}, if any; it is not necessary that any such
     * context exist.
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        hostContext = findRealAncestorWithClass(this, ValidationContext.class);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version determines whether this tag should ever
     * consider forwarding validation errors, and if so, determines whether to
     * forward any errors already reported to it.
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseAfterBody();

        considerForwarding = (hostContext != null);
        if (!areAllFieldsValid() && shouldForwardError()) {
            hostContext.reportValidationError();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ValidationContext#reportValidationError()
     */
    public void reportValidationError() {
        validationErrorReported = true;
        setErrorFlag(VALIDATION_ERROR_REPORTED);
        
        if (shouldForwardError()) {
            hostContext.reportValidationError();
        }
    }

    /**
     * {@inheritDoc}.  Whether or not this context forwards validation errors,
     * it always keeps track of whether any have been reported to it, as any
     * validation context should.
     * 
     * @see ValidationContext#areAllFieldsValid()
     */
    public boolean areAllFieldsValid() {
        return !validationErrorReported;
    }

    /**
     * A helper method that determines whether the surrounding validation
     * context should be notified of validation errors occurring within this
     * validation context.  The result depends not only on whether this context
     * is configured to trap errors, but also on whether a surrounding context
     * exists at all.
     * 
     * @return {@code true} if this tag should forward validation errors
     *         reported to it; {@code false} if it should not
     */
    private boolean shouldForwardError() {
        return (considerForwarding && !enabled);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     *
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
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
        return VALIDATION_ERROR_REPORTED;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(java.lang.String, java.util.Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        ValidationSupressor copy = (ValidationSupressor)
                super.generateCopy(newId, origToCopyMap);
        
        copy.hostContext
                = (ValidationContext) origToCopyMap.get(this.hostContext);
        
        return copy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        
        this.enabled = ((ValidationSupressor) source).enabled;
    }

}
