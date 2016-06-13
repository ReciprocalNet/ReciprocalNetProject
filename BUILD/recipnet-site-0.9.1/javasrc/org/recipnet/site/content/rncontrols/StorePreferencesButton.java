/*
 * Reciprocal Net project
 * 
 * StorePreferenceButton.java
 * 
 * 24-Jun-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * This special purpose button tag is used to trigger the storage of updated
 * user preference information to the current session by the
 * {@link EditSamplePage} tag (in which this tag must be nested).
 */
public class StorePreferencesButton extends ButtonHtmlControl {

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
     * {@inheritDoc}; this version looks up the containing
     * {@code EditSamplePage}.
     * 
     * @throws IllegalStateException if this tag is not nested within an
     *         {@code EditSamplePage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.editSamplePage = (EditSamplePage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link EditSamplePage#triggerStorePreferences
     * EditSamplePage.triggerStorePreferences()}.
     */
    @Override
    protected void onClick(
            @SuppressWarnings("unused") PageContext pageContext) {
        this.editSamplePage.triggerStorePreferences();
    }
}
