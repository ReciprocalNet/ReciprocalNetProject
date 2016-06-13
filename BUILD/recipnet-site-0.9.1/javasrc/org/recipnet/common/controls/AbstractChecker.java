/*
 * Reciprocal Net project
 * 
 * AbstractChecker.java
 * 
 * 23-Sep-2005: midurbin wrote first draft
 * 28-Mar-2006: jobollin reformatted the code
 * 12-Jun-2006: jobollin performed additional reformatting
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * An abstract base class for 'checker' tags. A 'checker' tag is one that
 * chooses to display or suppress its body based on property settings, data
 * values or other factors. This base class provides a
 * {@code SuppressionContext} implementation and basic support for suppressing
 * the body of this tag.
 * </p><p>
 * The simplest subclasses need only set the 'inclusionConditionMet' property to
 * get the desired 'checker' behavior. (Note: subclasses must still override
 * methods like {@link AbstractChecker#generateCopy(String, Map) generateCopy()}
 * to create deep copies of complex new member variables or to re-resolve any
 * references to other tags)
 * </p>
 */
public abstract class AbstractChecker extends HtmlPageElement implements
        SuppressionContext {

    /**
     * A refernece to the most immediate {@code SuppressionContext}
     * implementation in which this tag is nested if one exists. This reference
     * may be set by {@code onRegistrationPhaseBeforeBody()} and if present,
     * it's suppression indication will be echoed by this tag.
     */
    private SuppressionContext suppressionContext;

    /**
     * An internal variable that indicates whether the conditions defined by the
     * inclusion properties have been met. Typically, it is unknown until the
     * {@code FETCHING_PHASE} and this variable is false, suppressing action or
     * output from nested tags until the point when it can be determined that
     * the conditions for inclusion have been met.
     */
    protected boolean inclusionConditionMet;

    /**
     * An optional property that defaults to false, but when set to true causes
     * the body of this tag to be included by default and suppressed if the
     * inclusion conditions are met. This is the opposite of the normal behavior
     * where the body is suppressed by default and included if the inclusion
     * conditions are met.
     */
    private boolean invert;

    /**
     * {@inheritDoc}. Among other things, this version initializes the
     * 'inclusionConditionMet' field to {@code false}
     */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
        this.invert = false;
        this.inclusionConditionMet = false;
    }

    /**
     * Setter for the 'invert' property.
     * 
     * @param invert indicates whether the inclusion conditions should be
     *        interprited as suppression conditions (meaning that when they are
     *        true they would trigger suppression rather than inclusion)
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * Getter for the 'invert' property.
     * 
     * @return a boolean that indicates whether the inclusion conditions should
     *         be interprited as suppression conditions (meaning that when they
     *         are true they would trigger suppression rather than inclusion)
     */
    public boolean getInvert() {
        return this.invert;
    }

    /**
     * Implements {@code SuppressionContext}. This implementation determines
     * whether the body is to be suppressed by consulting the surrounding
     * {@code SuppressionContext} if one exits, then by checking
     * 'inclusionConditionMet' and 'invert'.
     */
    public boolean isTagsBodySuppressedThisPhase() {
        return ((this.suppressionContext != null)
                    && this.suppressionContext.isTagsBodySuppressedThisPhase())
                || (inclusionConditionMet == invert);
    }

    /**
     * {@inheritDoc}; this version gets the most immediate
     * {@code SuppressionContext} if one exists.
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
     * {@inheritDoc}; this version either buffers (and implicitly discards) or
     * displays the output of the body's evaluation based on the value returned
     * by {@link #isTagsBodySuppressedThisPhase()}.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        return (isTagsBodySuppressedThisPhase() ? EVAL_BODY_BUFFERED : rc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AbstractChecker dc = (AbstractChecker) super.generateCopy(newId, map);

        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);

        return dc;
    }
}
