/*
 * Reciprocal Net project
 * 
 * InitActionOnFilesButton.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 15-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * A button that must be nested within a {@link SelectFilesForActionPage
 * SelectFilesForActionPage} to trigger creation of a {@code MultiFileOperation}
 * whose id is passed to the URL indicated by the required
 * 'targetExtendedOpWapPageUrl' property.
 */
public class InitActionOnFilesButton extends ButtonHtmlControl {

    /**
     * A reference to the enclosing {@code SelectFilesForActionPage} tag, set
     * during {@code onRegistrationPhaseBeforeBody()}.
     */
    private SelectFilesForActionPage selectFilesForActionPage;

    /**
     * The URL (relative to the servlet context root) to which the surrounding
     * {@code SelectFilesForActionPage} should redirect the client when this
     * button is clicked. The page must be written to handle the included
     * parameters.
     */
    private String targetExtendedOpWapPageUrl;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.targetExtendedOpWapPageUrl = null;
    }

    /** Sets the 'targetExtendedOpWapPageUrl' property. */
    public void setTargetExtendedOpWapPageUrl(String url) {
        this.targetExtendedOpWapPageUrl = url;
    }

    /** Gets the 'targetExtendedOpWapPageUrl' property. */
    public String getTargetExtendedOpWapPageUrl() {
        return this.targetExtendedOpWapPageUrl;
    }

    /**
     * {@inheritDoc}; this version ensures that this tag is nested in a
     * {@code SelectFilesForActionPage} tag.
     * 
     * @throws IllegalStateException if the {@code HtmlPage} evaluating this tag
     *         is not a {@code SelectFilesForActionPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.selectFilesForActionPage = (SelectFilesForActionPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link SelectFilesForActionPage#triggerRedirectToTarget(String)} with the
     * 'targetExtenedOPWapPageUrl' property value as the target.
     */
    @Override
    protected void onClick(
            @SuppressWarnings("unused") PageContext pageContext) {
        this.selectFilesForActionPage.triggerRedirectToTarget(
                this.targetExtendedOpWapPageUrl);
    }
}
