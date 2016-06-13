/*
 * Reciprocal Net project
 * 
 * CreateDataDirectoryButton.java
 * 
 * 09-Jun-2005: midurbin wrote first draft
 * 17-Jan-2006: jobollin updated docs to reflect ErrorMessageElement's name
 *              change; removed unused imports
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * This special purpose button tag is used to trigger the creaton of a data
 * directory for the sample provided by the {@code EditSamplePage} tag in
 * which this tag must be nested.  This button should only be visible when the
 * sample does not already have a data directory.  The easiest way to ensure
 * this is to put it into an {@code ErrorChecker} or other tag that
 * is specially configured to be suppressed if a data directory exists.
 */
public class CreateDataDirectoryButton extends ButtonHtmlControl {

    /**
     * A reference to the enclosing {@code EditSamplePage} tag, set during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private EditSamplePage editSamplePage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.editSamplePage = null;
    }

    /**
     * {@inheritDoc}. This version gets a reference to the
     * {@code EditSamplePage}.
     * 
     * @throws IllegalStateException if this tag is not nested within an
     *     {@code EditSamplePage}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext); 
        this.editSamplePage
                = this.findRealAncestorWithClass(this, EditSamplePage.class);
        if (this.editSamplePage == null) {
            throw new IllegalStateException();
        }
        return rc;
    }

    /**
     * {@inheritDoc}.  This version invokes
     * {@link EditSamplePage#triggerCreateDirectory}.
     */
    @Override
    protected void onClick(@SuppressWarnings("unused") PageContext pageContext) {
        this.editSamplePage.triggerCreateDirectory();
    }
}
