/*
 * Reciprocal Net project
 * 
 * ValidationOverrideButton.java
 *
 * 19-Aug-2005: midurbin wrote first draft
 * 11-Nov-2005: midurbin included the 'sampleField' property in those copied by
 *              copyTransientPropertiesFrom so that it would function properly
 *              within an HtmlPageIterator
 * 17-Jan-2006: jobollin updated docs to reflect ErrorMessageElement's name
 *              change
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.bl.UserBL;

/**
 * <p>
 * A special-purpose extension of {@code WapSaveButton} that when clicked
 * attempts to complete the current workflow action while directing a given
 * {@code SampleField} not to use the {@code Validator} that it would normally
 * use on its 'owned' control.
 * </p><p>
 * This button is invisible in the event that the given {@code SampleField} does
 * not have the {@code VALIDATOR_REJECTED_VALUE} error flag set.
 * </p><p>
 * Furthermore, this tag checks the current session's user preferences and if
 * the {@code VALIDATE_SPACE_GROUP} preference is set to false and the
 * {@code SampleField}'s field code is {@code SampleDataInfo.SPGP_FIELD}, this
 * button triggers the {@code SampleField} not to use the validator and is
 * invisible.
 * </p><p>
 * Text associated with this button should be within an {@code ErrorChecker} tag
 * that only evaluates its body when the 'sampleField' is reporting the
 * {@code VALIDATOR_REJECTED_VALUE} error flag, but this tag should always be
 * evaluated because even when it's invisible, it persists its value through a
 * hidden field. Avoid the common mistake of putting this tag within a
 * {@code ErrorChecker} tag.
 * </p>
 */
public class ValidationOverrideButton extends WapSaveButton {

    /**
     * An {@code ErrorSupplier} error flag that indicates that this tag
     * silently overruled validation.
     */
    public static final int VALIDATION_OVERRIDDEN
            = HtmlControl.getHighestErrorFlag() << 1;

    /**
     * A reference to a {@code SampleField} whose {@code Validator}
     * validation is considered to be optional and may be overruled.
     */
    private SampleField sampleField;

    /**
     * An internal variable that keeps track of whether the button had been
     * clicked on previous roundtrips to overrule validation.
     */
    private boolean persistOverruleRequest;

    

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleField = null;
        this.persistOverruleRequest = false;
    }

    /**
     * Sets the 'sampleField' attribute.
     *
     * @param  sampleField the {@code SampleField} affected by this
     *         {@code ValidationOverrideButton}
     */
    public void setSampleField(SampleField sampleField) {
        this.sampleField = sampleField;
    }

    /**
     * Gets the 'sampleField' attribute.
     * 
     * @return the {@code SampleField} affected by this
     *         {@code ValidationOverrideButton}
     */
    public SampleField getSampleField() {
        return this.sampleField;
    }

    /**
     * {@inheritDoc}.  This version determines whether this control should
     * reflect an override decision made on a previous HTTP exchange and
     * persisted to the current one.  This test really is more suited to the
     * {@code PARSING_PHASE}, but it needs to be performed during page
     * reevaluations, when that phase is not evaluated, yet the result needs to
     * be available during that phase when it <em>is</em> evaluated.
     *  
     * @see HtmlControl#onRegistrationPhaseAfterBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        this.persistOverruleRequest = "true".equalsIgnoreCase(
                pageContext.getRequest().getParameter(getId() + "_persist"));
        
        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}. This version determines whether the 'sampleField' should
     * have its validator removed or not, and triggers the removal if needed.
     */
    @Override
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {

        /*
         * The underlying ButtonHtmlControl has already parsed its value,
         * the supplied SampleField's 'owned' control has not yet validated its
         * value against the Validator supplied by the SampleField.  If this
         * button was clicked, or the currently logged-in user's preferences
         * indicates that this optional validation should always be overruled, 
         * trigger the removal of the validator on the SampleField.
         */
        UserPreferences prefs
                = (UserPreferences) pageContext.getSession().getAttribute(
                        "preferences");

        if (this.persistOverruleRequest
                || getValueAsBoolean()
                || (!UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.VALIDATE_SPACE_GROUP, prefs)
                        && (this.sampleField.getFieldCode()
                                == SampleDataInfo.SPGP_FIELD))) {
            this.sampleField.overrideSpecificValidation();
        }
        
        return super.onParsingPhaseAfterBody(request);
    }

    /**
     * {@inheritDoc}. This version makes this button invisible if the
     * 'sampleField' is not invalid due to a validator failure.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws JspException,
            IOException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        
        if ((this.sampleField.getErrorCode()
                & SampleField.VALIDATOR_REJECTED_VALUE) == 0) {
            setVisible(false);
            
            /*
             * This flag could conceivably have been set as early as the
             * FETCHING_PHASE, but it is set here to be certain that the
             * override actually worked
             */
            if (this.persistOverruleRequest || getValueAsBoolean()) {
                setErrorFlag(VALIDATION_OVERRIDDEN);
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.ButtonHtmlControl#generateHtmlToInvisiblyPersistValue(Object)
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        boolean valueToPersist = (((value != null) && value.equals(true))
                || this.persistOverruleRequest);
        StringBuilder sb = new StringBuilder();
        
        sb.append("<input type=\"hidden\" name=\"");
        sb.append(getId());
        sb.append("_persist\" id=\"");
        sb.append(getId());
        sb.append("_persist\" value=\"");
        sb.append(valueToPersist ? "true" : "false");
        sb.append("\" />");
        
        return sb.toString();
    }

    /**
     * {@inheritDoc}. This version ensures that 'visible' is not exposed as a
     * transient property for this subclass.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        ValidationOverrideButton src = (ValidationOverrideButton) source;
        boolean preserveVisible = getVisible();
        
        super.copyTransientPropertiesFrom(source);
        this.setVisible(preserveVisible);
        this.setSampleField(src.getSampleField());
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    public static int getHighestErrorFlag() {
        return VALIDATION_OVERRIDDEN;
    }
}
