/*
 * Reciprocal Net project
 * 
 * WapSaveButton.java
 *
 * 02-Jul-2004: midurbin wrote first draft
 * 24-Sep-2004: midurbin modified onClick() to accomodate changes in WapPage
 * 27-Jan-2005: midurbin added protected method getWapPage() and
 *              'saveToPersistedOp' property
 * 26-Jul-2005: midurbin added 'reevaluatePage' property
 * 19-Jan-2006: jobollin made this class provide ButtonHtmlControl's new,
 *              extended error behavior; reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * This special purpose button tag is used to save the changes done within a
 * {@code WapPage} tag. The label for this button is "Save". When clicked, it
 * invokes either {@code WapPage.performWorkflowAction()} or
 * {@code ExtendedOperationWapPage.queueSampleChanges()} depending on whether
 * the property 'saveToPersistedOp' is false or not. One of these should be
 * present after all fields within a {@code WapPage} tag. If 'saveToPersistedOp'
 * is set to true, this tag must be nested in a
 * {@code ExtendedOperationWapPage}.  Furthermore, if it is known that the
 * implementation of {@code performWorkflowAction()} or
 * {@code queueSampleChanges()} does not redirect the browser yet the page
 * should must be reevaluated in order to correctly display an updated user
 * interface, the optional property 'reevaluatePage' may be set to true.
 */
public class WapSaveButton extends ButtonHtmlControl {
    /**
     * A reference to the enclosing {@code WapPage} tag, set during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private WapPage wapPage;

    /**
     * An optional property that defaults to false and indicates whether the
     * {@code SampleInfo} on which the {@code WapPage} is performing an action
     * should be stored to a {@code PersistedOperation} (true) via a call to
     * {@code ExtendedOperationWapPage.queueSampleChanges()} or whether the
     * workflow action should be triggered by a call to
     * {@code WapPage.triggerWorkflowAction()} (false) when this button is
     * clicked. When this property is set to true, this tag must be nested
     * within a {@code ExtendedOperationWapPage}.
     */
    private boolean saveToPersistedOp;

    /**
     * An optional property that defaults to false and indicates whether the
     * page should be reevaluted after the other button processing has been
     * completed. This is useful in pages where sample changes are made but the
     * same page is displayed again, reflecting the updated sample. This
     * property will have no effect if the other methods invoked on the
     * {@code WapPage} result in a redirect.
     */
    private boolean reevaluatePage;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        super.setLabel("Save");
        this.wapPage = null;
        this.saveToPersistedOp = false;
        this.reevaluatePage = false;
    }

    /**
     * @return the {@code WapPage} tag in which this tag is nested if called
     *         after the start of the {@code REGISTRATION_PHASE}, otherwise
     *         null
     */
    protected WapPage getWapPage() {
        return this.wapPage;
    }

    /**
     * @param persist a boolean indicating whether clicking this button should
     *        invoke {@code ExtendedOperationWapPage.queueSampleChanges()} or
     *        {@code WapPage.triggerWorkflowAction()}.
     */
    public void setSaveToPersistedOp(boolean persist) {
        this.saveToPersistedOp = persist;
    }

    /**
     * @return a boolean indicating whether clicking this button should invoke
     *         {@code ExtendedOperationWapPage.queueSampleChanges()} or
     *         {@code WapPage.triggerWorkflowAction()}.
     */
    public boolean getSaveToPersistedOp() {
        return this.saveToPersistedOp;
    }

    /**
     * Setter for the 'reevaluatePage' property.
     * 
     * @param reevaluate a boolean indicating whether
     *        {@code WapPage.triggerPageReevaluation()} should be called when
     *        this button is clicked.
     */
    public void setReevaluatePage(boolean reevaluate) {
        this.reevaluatePage = reevaluate;
    }

    /**
     * Getter for the 'reevaluatePage' property.
     * 
     * @return a boolean indicating whether
     *         {@code WapPage.triggerPageReevaluation()} should be called when
     *         this button is clicked.
     */
    public boolean getReevaluatePage() {
        return this.reevaluatePage;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation sets
     * {@code wapPage}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code WapPage} tag or if 'saveToPersistedOp' is true and the
     *         {@code WapPage} is not also an {@code ExtendedOperationWapPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the WapPage tag
        this.wapPage = this.findRealAncestorWithClass(this, WapPage.class);
        if (this.wapPage == null) {
            throw new IllegalStateException();
        }

        // ensure that wapPage is an instance of ExtendedOperationWapPage if
        // needed
        if (this.saveToPersistedOp
                && !(this.wapPage instanceof ExtendedOperationWapPage)) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code ButtonHtmlControl}; the current implementation invokes
     * {@code WapPage.triggerWorkflowAction()} or
     * {@code ExtendedOperationWapPage.queueSampleChanges()} depending on
     * 'saveToPersistedOp'.  It delegates to the superclass to obtain the error
     * behavior provided there.
     * 
     * @see ButtonHtmlControl#onClick(PageContext)
     */
    @Override
    protected void onClick(PageContext pageContext) throws JspException {
        super.onClick(pageContext);

        if (this.saveToPersistedOp) {
            
            // during the REGISTRATION_PHASE we ensured that the WapPage was
            // an ExtendedOperationWapPage given that saveToPersistedOp was
            // true
            assert this.wapPage instanceof ExtendedOperationWapPage;
            
            ((ExtendedOperationWapPage) this.wapPage).queueSampleChanges();
        } else {
            this.wapPage.triggerWorkflowAction();
        }
        if (this.reevaluatePage) {
            this.wapPage.triggerPageReevaluation();
        }
    }
}
