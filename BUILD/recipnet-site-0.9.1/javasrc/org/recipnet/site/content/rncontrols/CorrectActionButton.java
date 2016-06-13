/*
 * Reciprocal Net project
 * 
 * CorrectActionButton.java
 * 
 * 24-Jun-2005: midurbin wrote first draft
 * 09-Jun-2006: jobollin reformatted the source and made this class emit a
 *              display:hidden control instead of nothing when hidden 
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * An extension of {@code ContextPreservingButton} to link to a workflow action
 * page in correction mode. This button overrides the 'visible' property of it's
 * grandparent class in order to cause itself to be invisible in the event that
 * the action provided by the {@code SampleActionIterator} in which this tag
 * must be nested is not correctable.
 */
public class CorrectActionButton extends ContextPreservingButton {

    /**
     * A reference to the {@code SampleActionIterator} surrounding this tag.
     * Determined by {@code onRegistrationPhaseBeforeBody()}, this reference is
     * used to determine the action code, whether it is correctable, and the
     * uncorrectable fields to pass.
     */
    private SampleActionIterator sampleActionIterator;

    /** A required property; indicates a page to correct an action. */
    private String submittedCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String preliminaryDataCollectedCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String structureRefinedCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String releasedToPublicCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String declaredIncompleteCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String declaredNonScsCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String failedToCollectCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String withdrawnByProviderCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String citationCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String suspendedCorrectionPageUrl;

    /** A required property; indicates a page to correct an action. */
    private String resumedCorrectionPageUrl;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleActionIterator = null;
        /*
         * required properties need not be listed here because they WILL be set
         * to a value by their setters before any phase methods are invoked
         */
    }

    /** Sets the 'submittedCorrectionPageUrl' property. */
    public void setSubmittedCorrectionPageUrl(String url) {
        this.submittedCorrectionPageUrl = url;
    }

    /** Gets the 'submittedCorrectionPageUrl' property. */
    public String getSubmittedCorrectionPageUrl() {
        return this.submittedCorrectionPageUrl;
    }

    /** Sets the 'preliminaryDataCollectedCorrectionPageUrl' property. */
    public void setPreliminaryDataCollectedCorrectionPageUrl(String url) {
        this.preliminaryDataCollectedCorrectionPageUrl = url;
    }

    /** Gets the 'preliminaryDataCollectedCorrectionPageUrl' property. */
    public String getPreliminaryDataCollectedCorrectionPageUrl() {
        return this.preliminaryDataCollectedCorrectionPageUrl;
    }

    /** Sets the 'structureRefinedCorrectionPageUrl' property. */
    public void setStructureRefinedCorrectionPageUrl(String url) {
        this.structureRefinedCorrectionPageUrl = url;
    }

    /** Gets the 'structureRefinedCorrectionPageUrl' property. */
    public String getStructureRefinedCorrectionPageUrl() {
        return this.structureRefinedCorrectionPageUrl;
    }

    /** Sets the 'releasedToPublicCorrectionPageUrl' property. */
    public void setReleasedToPublicCorrectionPageUrl(String url) {
        this.releasedToPublicCorrectionPageUrl = url;
    }

    /** Gets the 'releasedToPublicCorrectionPageUrl' property. */
    public String getReleasedToPublicCorrectionPageUrl() {
        return this.releasedToPublicCorrectionPageUrl;
    }

    /** Sets the 'declaredIncompleteCorrectionPageUrl' property. */
    public void setDeclaredIncompleteCorrectionPageUrl(String url) {
        this.declaredIncompleteCorrectionPageUrl = url;
    }

    /** Gets the 'declaredIncompleteCorrectionPageUrl' property. */
    public String getDeclaredIncompleteCorrectionPageUrl() {
        return this.declaredIncompleteCorrectionPageUrl;
    }

    /** Sets the 'declaredNonScsCorrectionPageUrl' property. */
    public void setDeclaredNonScsCorrectionPageUrl(String url) {
        this.declaredNonScsCorrectionPageUrl = url;
    }

    /** Gets the 'declaredNonScsCorrectionPageUrl' property. */
    public String getDeclaredNonScsCorrectionPageUrl() {
        return this.declaredNonScsCorrectionPageUrl;
    }

    /** Sets the 'failedToCollectCorrectionPageUrl' property. */
    public void setFailedToCollectCorrectionPageUrl(String url) {
        this.failedToCollectCorrectionPageUrl = url;
    }

    /** Gets the 'failedToCollectCorrectionPageUrl' property. */
    public String getFailedToCollectCorrectionPageUrl() {
        return this.failedToCollectCorrectionPageUrl;
    }

    /** Sets the 'withdrawnByProviderCorrectionPageUrl' property. */
    public void setWithdrawnByProviderCorrectionPageUrl(String url) {
        this.withdrawnByProviderCorrectionPageUrl = url;
    }

    /** Gets the 'withdrawnByProviderCorrectionPageUrl' property. */
    public String getWithdrawnByProviderCorrectionPageUrl() {
        return this.withdrawnByProviderCorrectionPageUrl;
    }

    /** Sets the 'citationCorrectionPageUrl' property. */
    public void setCitationCorrectionPageUrl(String url) {
        this.citationCorrectionPageUrl = url;
    }

    /** Gets the 'citationCorrectionPageUrl' property. */
    public String getCitationCorrectionPageUrl() {
        return this.citationCorrectionPageUrl;
    }

    /** Sets the 'suspendedCorrectionPageUrl' property. */
    public void setSuspendedCorrectionPageUrl(String url) {
        this.suspendedCorrectionPageUrl = url;
    }

    /** Gets the 'suspendedCorrectionPageUrl' property. */
    public String getSuspendedCorrectionPageUrl() {
        return this.suspendedCorrectionPageUrl;
    }

    /** Sets the 'resumedCorrectionPageUrl' property. */
    public void setResumedCorrectionPageUrl(String url) {
        this.resumedCorrectionPageUrl = url;
    }

    /** Gets the 'resumedCorrectionPageUrl' property. */
    public String getResumedCorrectionPageUrl() {
        return this.resumedCorrectionPageUrl;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation simply
     * gets a reference to the surrounding {@code SampleActionIterator}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleActionIterator}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(
            @SuppressWarnings("hiding") PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleActionIterator
                = findRealAncestorWithClass(this, SampleActionIterator.class);
        if (this.sampleActionIterator == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation sets the
     * 'visible' and 'target' properties of the superclass based on the action
     * provided by the {@code SampleActionIterator} and its correctability.
     * 
     * @throws IllegalStateException if any action is reportd to be correctable
     *         but no property exists on this class to specify the correction
     *         page url.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (!this.sampleActionIterator.isCurrentActionCorrectable()) {
            this.setVisible(false);
            return rc;
        }

        /*
         * All actions that could be correctable according to SampleWorkflowBL
         * are listed here
         */
        switch (this.sampleActionIterator.getSampleHistoryInfo().action) {
            case SampleWorkflowBL.SUBMITTED:
                this.setTarget(this.submittedCorrectionPageUrl);
                break;
            case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED:
                this.setTarget(this.preliminaryDataCollectedCorrectionPageUrl);
                break;
            case SampleWorkflowBL.STRUCTURE_REFINED:
                this.setTarget(this.structureRefinedCorrectionPageUrl);
                break;
            case SampleWorkflowBL.RELEASED_TO_PUBLIC:
                this.setTarget(this.releasedToPublicCorrectionPageUrl);
                break;
            case SampleWorkflowBL.DECLARED_INCOMPLETE:
                this.setTarget(this.declaredIncompleteCorrectionPageUrl);
                break;
            case SampleWorkflowBL.DECLARED_NON_SCS:
                this.setTarget(this.declaredNonScsCorrectionPageUrl);
                break;
            case SampleWorkflowBL.FAILED_TO_COLLECT:
                this.setTarget(this.failedToCollectCorrectionPageUrl);
                break;
            case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER:
                this.setTarget(this.withdrawnByProviderCorrectionPageUrl);
                break;
            case SampleWorkflowBL.CITATION_ADDED:
            case SampleWorkflowBL.CITATION_ADDED_CORRECTED:
                this.setTarget(this.citationCorrectionPageUrl);
                break;
            case SampleWorkflowBL.SUSPENDED:
            case SampleWorkflowBL.SUSPENDED_CORRECTED:
                this.setTarget(this.suspendedCorrectionPageUrl);
                break;
            case SampleWorkflowBL.RESUMED:
            case SampleWorkflowBL.RESUMED_CORRECTED:
                this.setTarget(this.resumedCorrectionPageUrl);
                break;
            default:
                throw new IllegalStateException();
        }
        this.addParameter(
                "correctionHistoryId",
                String.valueOf(this.sampleActionIterator.getSampleHistoryInfo().id));
        this.addParameter(
                "uncorrectableFields",
                String.valueOf(this.sampleActionIterator.getCurrentActionUncorrectableFields()));

        return rc;
    }

    /**
     * {@inheritDoc}; this version inserts a place-holder into the HTML that
     * takes up the same amount of space as this button would if it were
     * dsiplayed normally
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        return super.generateHtmlToInvisiblyPersistValue(value)
                + "<button type=\"button\" name=\"placeholder\""
                + " value=\"\" disabled=\"true\" style=\"visibility: hidden;\">"
                + getLabel() + "</button>";
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        CorrectActionButton dc = (CorrectActionButton) super.generateCopy(
                newId, map);

        dc.sampleActionIterator
                = (SampleActionIterator) map.get(this.sampleActionIterator);

        return dc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation delegates
     * back to the superclass but ensures that the 'visible' property is not
     * modified, but instead stays as whatever value was set by this class.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        boolean overrideVisibility = this.getVisible();

        super.copyTransientPropertiesFrom(source);

        this.setVisible(overrideVisibility);
    }
}
