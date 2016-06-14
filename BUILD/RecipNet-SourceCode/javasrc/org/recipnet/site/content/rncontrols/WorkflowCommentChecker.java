/*
 * Reciprocal Net project
 * 
 * CommentChecker.java
 * 
 * 24-Jun-2005: midurbin wrote the first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;

/**
 * <p>
 * A custom tag that suppresses its body if the 'comments' on the most
 * immediatly enclosing {@code SampleHistoryContext} are null. Furthermore if it
 * finds itself nested within a {@code SampleActionIterator} and its body is NOT
 * suppressed, it invokes
 * {@link SampleActionIterator#notifyFieldDisplayed() notifyFieldDisplayed()} on
 * that {@code SampleActionIterator}.
 * </p><p>
 * This tag assumes that its body and a nested field to display the comments
 * <strong>will</strong> be evaluated if this tag does not suppress its body.
 * In order to meet this expectation there must not be any other "checker" tags
 * nested within this tag.
 * </p>
 */
public class WorkflowCommentChecker extends HtmlPageElement {

    /**
     * A reference to the most immediately surrounding
     * {@code SuppressionContext}; not only is this used to propagate
     * suppression information but to determine if the comments will ultimately
     * be displayed for the purposes of notifying the
     * {@code SampleActionIterator}.
     */
    private SuppressionContext suppressionContext;

    /**
     * A refernce to the most immediately surrounding
     * {@code SampleActionIterator} if one surrounds this tag. If present, and
     * this tag does not suppress its body, notification of the displayed field
     * is made.
     */
    private SampleActionIterator sampleActionIterator;

    /**
     * The {@code SampleHistoryContext} from which the 'comments' field is
     * checked. If 'comments' is null this tag will suppress its body.
     */
    private SampleHistoryContext sampleHistoryContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
        this.sampleActionIterator = null;
        this.sampleHistoryContext = null;
    }

    /** Implements {@code SuppressionContext}. */
    public boolean isTagsBodySuppressedThisPhase() {
        if ((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase()) {
            return true;
        }
        return ((this.sampleHistoryContext == null)
                || ((this.sampleHistoryContext.getSampleHistoryInfo() == null)
                        ? true
                        : (this.sampleHistoryContext.getSampleHistoryInfo().comments
                                == null)));
    }

    /**
     * {@inheritDoc}; this version looks up various surronding tags.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleHistoryContext}
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
        this.suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);

        this.sampleActionIterator
                = findRealAncestorWithClass(this, SampleActionIterator.class);

        this.sampleHistoryContext
                = findRealAncestorWithClass(this, SampleHistoryContext.class);
        if (this.sampleHistoryContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version notifies the containing
     * {@code SampleActionIterator} if it exists and this tag's body isn't
     * currently suppressed.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if ((this.sampleActionIterator != null)
                && !isTagsBodySuppressedThisPhase()) {
            this.sampleActionIterator.notifyFieldDisplayed();
        }

        return super.onFetchingPhaseAfterBody();
    }

    /**
     * {@inheritDoc}; this version evaluates its body to a buffer (instead of
     * the JspWriter) if it is to be suppressed.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);

        return this.isTagsBodySuppressedThisPhase() ? EVAL_BODY_BUFFERED
                : EVAL_BODY_INCLUDE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        WorkflowCommentChecker dc = (WorkflowCommentChecker) super.generateCopy(
                newId, map);

        dc.sampleHistoryContext
                = (SampleHistoryContext) map.get(this.sampleHistoryContext);
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        dc.sampleActionIterator
                = (SampleActionIterator) map.get(this.sampleActionIterator);

        return dc;
    }
}
