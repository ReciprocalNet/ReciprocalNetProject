/*
 * Reciprocal Net project
 * 
 * DeleteSampleTextButton.java
 * 
 * 30-Sep-2004: midurbin wrote first draft
 * 27-Jan-2005: midurbin refactored code to make class extend from
 *              WapSaveButton and use the 'saveToPersistedOp' property
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 11-Nov-2005: midurbin fixed bug #1694 in onClick()
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A custom tag that when clicked removes a given sample annotation or attribute
 * and then inovkes {@code performWorkflowAction()} on the {@code WapPage} to
 * save the changes. This tag therefore must be nested within a {@code WapPage}
 * and a {@code SampleTextContext} implementation.
 */
public class DeleteSampleTextButton extends WapSaveButton {

    /**
     * A reference to the most immediate {@code SampleTextContext} that encloses
     * this tag. This variable is set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleTextContext sampleTextContext;

    /**
     * An optional attribute that defaults to {@code true}. When true it
     * indicates that this button should not be displayed if the page is not in
     * "correction mode" (as determined by a call to
     * {@code WapPage.isCorrectionMode()}). When false, this button will be
     * displayed as normal, regardless of whether this page is in "correction
     * mode" or not.
     */
    private boolean suppressIfNotCorrectionMode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleTextContext = null;
        this.suppressIfNotCorrectionMode = true;
        this.setLabel("Delete");
    }

    /**
     * @param suppress indicates whether this button should be suppressed when
     *        this page is not in 'correction mode'.
     */
    public void setSuppressIfNotCorrectionMode(boolean suppress) {
        this.suppressIfNotCorrectionMode = suppress;
    }

    /**
     * @return a boolean that indicates whether this button be suppressed when
     *         this page is not in 'correction mode'.
     */
    public boolean getSuppressIfNotCorrectionMode() {
        return this.suppressIfNotCorrectionMode;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost
     * {@code SampleTextContext} and sets the {@code visible} attribute on the
     * superclass.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleTextContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get SampleTextContext
        this.sampleTextContext = findRealAncestorWithClass(this,
                SampleTextContext.class);
        if (this.sampleTextContext == null) {
            throw new IllegalStateException();
        }

        if (this.suppressIfNotCorrectionMode
                && !this.getWapPage().isInCorrectionMode()) {
            /*
             * the JSP author has indicated that this control should be
             * suppressed if the page is not in correction mode, and the page is
             * currently not in correction mode
             */
            setVisible(false);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets the value of the {@code SampleTextInfo}
     * provided by the {@code SampleTextContext} to {@code null} (unless the
     * surrounding {@code WapPage} has had any validation errors reported to it)
     * to indicate that it should be removed, then delegates to the superclass
     */
    @Override
    protected void onClick(PageContext pageContext) throws JspException {
        SampleTextInfo sampleTextInfo
                = this.sampleTextContext.getSampleTextInfo();
        
        if ((sampleTextInfo != null) && this.getWapPage().areAllFieldsValid()) {
            /*
             * This action is only performed if all field are valid, because
             * otherwise we'd update the SampleInfo even when the rest of this
             * operation (done by the superclass) does not occur. This can cause
             * an exception when the page is not reevaluated.
             */
            sampleTextInfo.value = null;
        }
        
        super.onClick(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        DeleteSampleTextButton src = (DeleteSampleTextButton) source;
        
        /*
         * this method specifically omits transient properties of the superclass
         * because they are not exposed as properties by this implementation,
         * and in the case of 'visible' may be used internally
         */
        this.setLabel(src.getLabel());
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        DeleteSampleTextButton dc
                = (DeleteSampleTextButton) super.generateCopy(newId, map);
        
        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);
        
        return dc;
    }
}
