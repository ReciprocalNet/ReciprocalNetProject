/*
 * Reciprocal Net project
 * 
 * WapCancelButton.java
 *
 * 02-Jul-2004: midurbin wrote first draft
 * 24-Sep-2004: midurbin modified onClick() to accomodate changes in WapPage
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * This special purpose button tag is used to cancel the changes done within a
 * {@code WapPage} tag. The label for this button is "Cancel". When clicked, it
 * invokes {@link WapPage#cancelWorkflowAction()}. By convention, one of these
 * should be present after all fields within a {@code WapPage} tag.
 */
public class WapCancelButton extends ButtonHtmlControl {

    /**
     * A reference to the enclosing {@code WapPage} tag, set during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private WapPage wapPage;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        setLabel("Cancel");
        this.wapPage = null;
    }

    /**
     * {@inheritDoc}; this version sets the {@code wapPage}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code WapPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the WapPage tag
        try {
            this.wapPage = (WapPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link WapPage#triggerCancellation()}.
     */
    @Override
    protected void onClick(
            @SuppressWarnings("unused") PageContext pageContext) {
        this.wapPage.triggerCancellation();
    }
}
