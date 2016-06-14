/*
 * Reciprocal Net project
 * 
 * RequestAttributeChecker.java
 *
 * 15-Aug-2005: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;


/**
 * A {@code SuppressionContext} implementation that suppresses its body based on
 * the presence or absence of the request attribute indicated by the required
 * 'attributeName' property. The value and type of the attribute are ignored.
 */
public class RequestAttributeChecker extends HtmlPageElement implements
        SuppressionContext {

    /**
     * The most immediate {@code SuppressionContext} implementation; determined
     * during {@code onRegistrationPhaseBeforeBody()} and used to ensure that
     * this tag propagates suppression indicators from its ancestry along with
     * its own suppression indicators.
     */
    private SuppressionContext suppressionContext;

    /**
     * A required property indicating the name of an attribute on the
     * {@code ServletRequest} object. (typically attributes are attached before
     * forwarding)
     */
    private String attributeName;

    /**
     * A required property that when set to true indicates that this tag's body
     * should be included (rather than suppressed when the attribute is
     * present). When false, the body is suppressed when the attribute is
     * present.
     */
    private boolean includeIfAttributeIsPresent;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
        this.includeIfAttributeIsPresent = true;
    }

    /** Setter for the 'attributeName' property. */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /** Getter for the 'attributeName' property. */
    public String getAttributeName() {
        return this.attributeName;
    }

    /** Setter for the 'includeIfAttributeIsPresent' property. */
    public void setIncludeIfAttributeIsPresent(boolean include) {
        this.includeIfAttributeIsPresent = include;
    }

    /** Getter for the 'includeIfAttributeIsPresent' property. */
    public boolean getIncludeIfAttributeIsPresent() {
        return this.includeIfAttributeIsPresent;
    }

    /** Implements {@code SuppressionContext}. */
    public boolean isTagsBodySuppressedThisPhase() {
        return (((this.suppressionContext != null)
                    && this.suppressionContext.isTagsBodySuppressedThisPhase())
                || ((this.pageContext.getRequest().getAttribute(
                        this.attributeName) == null)
                    == this.includeIfAttributeIsPresent));
    }

    /**
     * {@inheritDoc}; this version finds the innermost surrounding
     * {@code SuppressionContext} if one exists.
     * 
     * @throws IllegalStateException if this tag is not nested within the
     *         required context
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        /*
         * get the SuppressionContext if one exists if one exists, a reference
         * is needed so that its isTagsBodySuppressedThisPhase() return value
         * may be propagated
         */
        this.suppressionContext = findRealAncestorWithClass(this,
                SuppressionContext.class);

        return rc;
    }

    /**
     * {@inheritDoc}; this version simply evaluates the body to the buffer
     * (instead of the JspWriter) when the body is suppressed.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        return (isTagsBodySuppressedThisPhase() ? EVAL_BODY_BUFFERED
                : EVAL_BODY_INCLUDE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        RequestAttributeChecker dc
                = (RequestAttributeChecker) super.generateCopy(newId, map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        
        return dc;
    }
}
