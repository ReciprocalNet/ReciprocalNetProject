/*
 * Reciprocal Net project
 * 
 * ButtonHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 10-Mar-2004: cwestnea fixed bug #1168 in onProcessingPhaseBeforeBody()
 * 17-May-2004: midurbin added generateHtmlToDisplayForLabel() to add support
 *              for HtmlControl's 'displayAsLabel' attribute
 * 24-Jun-2004: cwestnea modified generateHtmlToDisplay() to reflect name 
 *              change of HtmlPage.isBrowserNetscape4x()
 * 02-Jul-2003: midurbin altered the spec of onClick() to throw a JspException
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 08-Apr-2005: midurbin added generateHtmlToInvisiblyPersistValue(), modified
 *              generateHtmlToDisplayForLabel() to meet new specification
 * 07-Jul-2005: midurbin added 'suppressInsteadOfSkip' property
 * 19-Aug-2005: midurbin fixed a typo in generateHtmlToInvisiblyPersistValue()
 * 19-Jan-2006: jobollin gave this tag some tag-specific error codes
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * This tag represents an HTML form's submit button input. If this button was
 * clicked its value will be set to {@code Boolean.TRUE}, the overridable
 * {@code onClick()} method will be invoked and it will evaluate its body
 * normally during the {@code PROCESSING_PHASE} (it is suppressed or skipped
 * during all other phases). This tag must be nested within a self-posting form
 * in order to behave properly.
 */
public class ButtonHtmlControl extends HtmlControl
        implements SuppressionContext {

    /**
     * An error flag set during the processing phase by a button that registers
     * a click despite not being enabled (i.e. editable)
     */
    public static final int CLICKED_WHEN_DISABLED
            = HtmlControl.getHighestErrorFlag() << 1;

    /**
     * An error flag set during the processing phase by a button that registers
     * a click despite being suppressed by a surroundign suppression context
     */
    public static final int CLICKED_WHEN_SUPPRESSED
            = HtmlControl.getHighestErrorFlag() << 2;

    /**
     * A reference to the most immediate {@code SuppressionContext}
     * implementation in which this tag is nested if one exists. This reference
     * may be set by {@code onRegistrationPhaseBeforeBody()} and if present,
     * it's suppression indication will be echoed by this tag.
     */
    private SuppressionContext suppressionContext;

    /**
     * An optional attribute representing the text to be drawn on the button. It
     * is initialized by {@code reset()} and may be altered by its 'setter'
     * method, {@code setLabel()}. It is a 'transient' variable in that it may
     * change from phase to phase and may be copied by a call to
     * {@code copyTransientPropertiesFrom()}.
     */
    private String label;

    /**
     * An optional property that defaults to false and indicates whether the
     * body of this tag should be suppressed instead of skipped for the phases
     * during which it is not evaluated normally. If other phase-based custom
     * tags are nested within this tag this property must be set to true so that
     * those tags may have every phase evaluated.
     */
    private boolean suppressInsteadOfSkip;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
        this.label = "Submit";
        this.suppressInsteadOfSkip = false;
    }

    /** @return the label for this button */
    public String getLabel() {
        return this.label;
    }

    /** @param label the {@code String} that is this button's label */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return a boolean indicating whether this tag will suppress its body or
     *         skip it altogether when it is not to be evaluated normally.
     */
    public boolean getSuppressInsteadOfSkip() {
        return this.suppressInsteadOfSkip;
    }

    /**
     * @param suppress if true, indicates that this tag will suppress its body
     *        rather than skip it when it is not to be evaluated normally.
     */
    public void setSuppressInsteadOfSkip(boolean suppress) {
        this.suppressInsteadOfSkip = suppress;
    }

    /**
     * Implements {@code SuppressionContext}.
     * 
     * @return false during every phase except the {@code PROCESSING_PHASE} and
     *         then only true if this button was clicked
     */
    public boolean isTagsBodySuppressedThisPhase() {
        return ((getPage().getPhase() != HtmlPage.PROCESSING_PHASE)
                || !getValueAsBoolean() 
                || ((this.suppressionContext != null)
                   && this.suppressionContext.isTagsBodySuppressedThisPhase()));
    }

    /**
     * {@inheritDoc}.  This version simply
     * suppresses the evaluation of the body during this phase and gets a
     * reference to the surrounding {@code SuppressionContext} if one exists.
     * 
     * @param pageContext unused in this implementation
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);
        
        // get the SuppressionContext if one exists
        this.suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);
        
        return (this.suppressInsteadOfSkip ? EVAL_BODY_BUFFERED : SKIP_BODY);
    }

    /**
     * {@inheritDoc}. This version simply suppresses the evaluation of the body
     * during this phase.
     * 
     * @param request unused in this implementation
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        super.onParsingPhaseBeforeBody(request);
        
        return (this.suppressInsteadOfSkip ? EVAL_BODY_BUFFERED : SKIP_BODY);
    }

    /**
     * {@inheritDoc}. This version simply suppresses the evaluation of the body
     * during this phase.
     * 
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();
        
        return (this.suppressInsteadOfSkip ? EVAL_BODY_BUFFERED : SKIP_BODY);
    }

    /**
     * {@inheritDoc}. This version suppresses the evaluation of the body unless
     * the button was clicked.
     * 
     * @param pageContext the current {@code PageContext} is passed on to child
     *        implementations of {@code onClick()}.
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onProcessingPhaseBeforeBody(pageContext);
        
        if (getValueAsBoolean()) {
            onClick(pageContext);
        }
        
        return (isTagsBodySuppressedThisPhase()
                ? (this.suppressInsteadOfSkip
                        ? EVAL_BODY_BUFFERED
                        : SKIP_BODY)
                : EVAL_BODY_INCLUDE);
    }

    /**
     * {@inheritDoc}. This version simply suppresses the evaluation of the body
     * during this phase.
     * 
     * @param out unused in this implementation
     * @throws IOException if an error occurs while writing to the JspWriter
     * @throws JspException if any other exceptions are encountered during this
     *         method.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        
        return (this.suppressInsteadOfSkip ? EVAL_BODY_BUFFERED : SKIP_BODY);
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation parses
     * {@code rawValue} to determine whether the button was clicked (indicated
     * by a non-null {@code rawValue}).
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters that
     *        corresponds to the value of this control.
     * @return the parsed value, or null if there was a parsing or validation
     *         error.
     */
    @Override
    protected Object parseValue(String rawValue) {
        return Boolean.valueOf(rawValue != null);
    }

    /**
     * Overrides {@code HtmlControl} to generate HTML for this control.
     * 
     * @param editable indicates whether the button should be clickable. Because
     *        in many browsers a button is always clickable, alternate HTML is
     *        used to replace the button control when it is not editable.
     * @param failedValidation is not useful for this control.
     * @param value the value of the control.
     * @param rawValue the value from the POST parameters; not useful for this
     *        element.
     * @return a {@code String} of HTML code for a textbox or plain text,
     *         depending on the editability of the field.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            @SuppressWarnings("unused") Object value,
            @SuppressWarnings("unused") String rawValue) {
        if (getPage().isBrowserNetscape4x() && !editable) {
            // Netscape 4.8 does not support the 'disabled' attribute for
            // button input tags. Therefore, the equivalent disabled button
            // for this browser is simply not to display the button.
            return "";
        } else {
            return "<input name=\"" + getId()
                    + "\" type=\"submit\""
                    + " id=\"" + getId()
                    + "\" value=\"" + getLabel()
                    + "\"" + (editable ? "" : " disabled=\"disabled\"")
                    + getExtraHtmlAttributesAsString() + " />";
        }
    }

    /**
     * {@inheritDoc}. This version returns the value of the 'label' property.
     * 
     * @param value the current value of this button.
     */
    @Override
    protected String generateHtmlToDisplayForLabel(
            @SuppressWarnings("unused") Object value) {
        return getLabel();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The current implementation overrides the subclass and outputs a hidden
     * form field if the 'value' is {@code TRUE} and otherwise nothing.
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        if (Boolean.TRUE.equals(value)) {
            // value is a non-null Boolean representing boolean value <<true>>
            return "<input type=\"hidden\" name=\"" + getId()
                    + "\" id=\"" + getId()
                    + "\" value=\"" + getLabel() + "\" />";
        } else {
            return "";
        }
    }

    /**
     * A template method intended for use by subclasses to insert appropriate
     * behavior for responding to a button click. This version sets an error
     * flag if this button is suppressed when it is invoked or if it is not
     * editable. Subclasses that want the same error behavior should delegate to
     * this method; those that do not, should not.  This method is invoked
     * during the processing phase, before the tag body, if this control's value
     * is TRUE at that point.
     * 
     * @param pageContext the current {@code PageContext}
     * @throws JspException if an exception is encountered during the button
     *         processing.
     */
    protected void onClick(@SuppressWarnings("unused") PageContext pageContext)
            throws JspException {
        if (!getEditable()) {
            setErrorFlag(CLICKED_WHEN_DISABLED);
        }
        if ((suppressionContext != null)
                && suppressionContext.isTagsBodySuppressedThisPhase()) {
            setErrorFlag(CLICKED_WHEN_SUPPRESSED);
        }
    }

    /**
     * Overrides method on {@code HtmlPageElement} to copy all transient fields
     * from {@code source} if it is an {@code HtmlButtonControl} to this object.
     * 
     * @param source an {@code HtmlPageElement} or subclass whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        
        ButtonHtmlControl src = (ButtonHtmlControl) source;
        
        this.label = src.label;
    }

    /**
     * {@inheritDoc}. This version delegates to the superclass then updates any
     * references to 'owned' controls or referenced ancestor tags using the
     * 'map' parameter that was populated by the superclass' implementation as
     * well as the caller, then makes a deep copy of any complex modifiable
     * member variables before returning the deep copy.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ButtonHtmlControl dc = (ButtonHtmlControl) super.generateCopy(newId,
                map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        
        return dc;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return CLICKED_WHEN_SUPPRESSED;
    }
}
