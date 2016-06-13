/*
 * Reciprocal Net project
 * 
 * HtmlPageStyleBlock.java
 * 
 * 16-Jun-2004: cwestnea wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * During the registration phase, this tag finds the {@code HtmlPage} tag in
 * which it is enclosed and puts this tag's body into the {@code HtmlPage}'s
 * style block. The body is only evaluated during registration phase, so custom
 * tags should not be used, and scriptlets should not refer to objects which are
 * not available during the registration phase.
 */
public class HtmlPageStyleBlock extends HtmlPageElement {

    /**
     * {@inheritDoc}; this version always returns {@code EVAL_BODY_BUFFERED} so
     * that the body of the tag made be used in
     * {@link #onRegistrationPhaseAfterBody(PageContext)}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);
        return EVAL_BODY_BUFFERED;
    }

    /**
     * {@inheritDoc}; this version adds the body of this tag to the parent
     * {@code HtmlPage}'s style block via its{@code #addStyleBlock(String)}
     * method
     * 
     * @throws IllegalStateException if this tag is not enclosed by a
     *         {@code HtmlPage} tag.
     */
    @Override
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        HtmlPage page = (HtmlPage) TagSupport.findAncestorWithClass(
                this, HtmlPage.class);

        if (page == null) {
            throw new IllegalStateException();
        }
        page.addToStyleBlock(getBodyContent().getString());

        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}; this version always returns {@code SKIP_BODY}
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        super.onParsingPhaseBeforeBody(request);
        return SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version always returns {@code SKIP_BODY}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();
        return SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version always returns {@code SKIP_BODY}
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onProcessingPhaseBeforeBody(pageContext);
        return SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version always returns {@code SKIP_BODY}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        return SKIP_BODY;
    }
}
