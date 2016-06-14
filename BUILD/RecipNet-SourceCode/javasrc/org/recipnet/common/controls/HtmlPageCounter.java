/*
 * Reciprocal Net Project
 *
 * HtmlPageCounter.java
 *
 * 16-Jan-2006: jobollin wrote first draft
 * 12-Jun-2006: jobollin removed unused imports
 */

package org.recipnet.common.controls;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * A phase-recognizing tag handler class that establishes an autoincrementing
 * counter for use in the body of an HtmlPage.  Typically, an instance would be
 * exposed and referenced as a scripting variable.
 * </p><p>
 * This tag implements {@code ErrorSupplier}, but in an atypical way: it starts
 * each phase with an error code set ({@code NEVER_READ}), and later clears this
 * code when appropriate.  This unusual approach is required to support the
 * instended usage mode, wherein the tag has an empty body and serves to
 * establish a counter to be referenced later in the JSP body.
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class HtmlPageCounter extends HtmlPageElement implements ErrorSupplier {
    
    /**
     * An error flag that indicates that this tag handler's counter was never
     * incremented
     */
    public static final int NEVER_INCREMENTED = 1 << 0;

    /**
     * The current count maintained by this counter
     */
    private int count;

    /**
     * The current error code set on this {@code ErrorSupplier}
     */
    private int errorCode;
    
    /**
     * Returns the current count maintained by this counter
     * 
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments this counter's count by one
     */
    public void incrementCount() {
        ++count;
        clearErrorFlag(NEVER_INCREMENTED);
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc =  super.onRegistrationPhaseBeforeBody(pageContext);
        
        count = 0;
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#onParsingPhaseBeforeBody(ServletRequest)
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        count = 0;
        return super.onParsingPhaseBeforeBody(request);
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        count = 0;
        return super.onFetchingPhaseBeforeBody();
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#onProcessingPhaseBeforeBody(PageContext)
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        count = 0;
        return super.onProcessingPhaseBeforeBody(pageContext);
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#onRenderingPhaseBeforeBody(JspWriter)
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out)
            throws IOException, JspException {
        count = 0;
        return super.onRenderingPhaseBeforeBody(out);
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        errorCode |= errorFlag;
    }
    
    /**
     * Clears an error flag set on this error supplier.  This method is peculiar
     * to this error supplier -- it is not a standard behavior of the
     * {@code ErrorSupplier} class
     * 
     * @param  errorFlag the error flag to clear from this error supplier
     */
    public void clearErrorFlag(int errorFlag) {
        errorCode &= ~errorFlag;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own
     * error codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return NEVER_INCREMENTED;
    }

    /**
     * {@inheritDoc}.  This version resets this counter's count to zero and
     * delegates to the superclass.
     * 
     * @see HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        count = 0;
        
        // ErrorSupplier implementation
        errorCode = NEVER_INCREMENTED;
    }
}
