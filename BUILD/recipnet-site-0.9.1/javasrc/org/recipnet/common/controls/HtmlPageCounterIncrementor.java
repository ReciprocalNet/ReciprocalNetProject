/*
 * Reciprocal Net Project
 *
 * HtmlPageCounterIncrementor.java
 *
 * 16-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A custom tag handler that works with an {@link HtmlPageCounter} by
 * incrementing it, provided that this tag is not within a suppression context
 * that is currently suppressing its body
 * 
 * @author jobollin
 * @version 1.0
 */
public class HtmlPageCounterIncrementor extends HtmlPageElement {

    /**
     * The counter that this action will increment, exposed as a required tag
     * attribute
     */
    private HtmlPageCounter counter;

    /**
     * The innermost surrounding {@code SuppressionContext}, if any
     */
    private SuppressionContext suppressionContext;

    /**
     * Gets the "counter" property assigned to this counter incrementor
     * 
     * @return the {@code HtmlPageCounter} that is incremented by this
     *         incrementor
     */
    public HtmlPageCounter getCounter() {
        return counter;
    }

    /**
     * Sets the "counter" property assigned to this counter incrementor; this is
     * the counter that is to be incremented
     * 
     * @param counter the {@code HtmlPageCounter} to be incremented by this
     *        incrementor
     */
    public void setCounter(HtmlPageCounter counter) {
        if (counter == null) {
            throw new NullPointerException("Null counter");
        }
        this.counter = counter;
    }

    /**
     * {@inheritDoc}. This version increments the counter, provided that this
     * tag is not within the suppressed body of a suppression context.
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);
        conditionallyIncrementCounter();

        return rc;
    }

    /**
     * {@inheritDoc}. This version increments the counter, provided that this
     * tag is not within the suppressed body of a suppression context.
     * 
     * @see HtmlPageElement#onParsingPhaseBeforeBody(ServletRequest)
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        int rc = super.onParsingPhaseBeforeBody(request);

        conditionallyIncrementCounter();

        return rc;
    }

    /**
     * {@inheritDoc}. This version increments the counter, provided that this
     * tag is not within the suppressed body of a suppression context.
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        conditionallyIncrementCounter();

        return rc;
    }

    /**
     * {@inheritDoc}. This version increments the counter, provided that this
     * tag is not within the suppressed body of a suppression context.
     * 
     * @see HtmlPageElement#onProcessingPhaseBeforeBody(PageContext)
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        conditionallyIncrementCounter();

        return rc;
    }

    /**
     * {@inheritDoc}. This version increments the counter, provided that this
     * tag is not within the suppressed body of a suppression context.
     * 
     * @see HtmlPageElement#onRenderingPhaseBeforeBody(JspWriter)
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        conditionallyIncrementCounter();

        return rc;
    }

    /**
     * Increments the counter referenced by this tag, provided that the tag is
     * not suppressed
     */
    private void conditionallyIncrementCounter() {
        if ((suppressionContext == null)
                || !suppressionContext.isTagsBodySuppressedThisPhase()) {
            counter.incrementCount();
        }
    }

    /**
     * {@inheritDoc}. This version updates the reference to the containing
     * suppression context, if any.
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map map) {
        HtmlPageCounterIncrementor copy
                = (HtmlPageCounterIncrementor) super.generateCopy(newId, map);

        copy.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);

        return copy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();

        counter = null;
        suppressionContext = null;
    }
}
