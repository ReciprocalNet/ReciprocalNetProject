/*
 * Reciprocal Net project
 * 
 * HtmlPagePhaseEvent.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 27-Feb-2004: midurbin wrote second draft
 * 05-Aug-2004: midurbin added fetching phase support
 * 13-Jan-2006: jobollin added support for multiple phases; removed
 *              transience of the (renamed) onPhases property; reformatted the
 *              source; updated docs
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;

/**
 * This is a Tag whose body is evaluated only during the specified phase.
 */
public class HtmlPagePhaseEvent extends HtmlPageElement {

    /**
     * A map recording the phases on which this tag should evaluate its body,
     * along with string names for those phases, as determined by the required
     * {@code onPhases} property.
     */
    private Map<Integer, String> includeOnPhases;
    
    /**
     * An optional, non-transient attribute indicating whether this tag should
     * skip evaluation of its body when it itself is suppressed, regardless of
     * phase.  Defaults to false. 
     */
    private boolean skipIfSuppressed;
    
    private SuppressionContext suppressionContext;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.includeOnPhases = new LinkedHashMap<Integer, String>();
        this.skipIfSuppressed = false;
        this.suppressionContext = null;
    }

    /**
     * Retrieves the value of the {@code onPhases} property, a comma-delimited
     * list of the names of the phases on which this tag will evaluate its body
     * 
     * @return a {@code String} containing a comma-delimited list of the names
     *         of the phases on which this tag will evaluate its body, all
     *         lowercase and with no internal whitespace; though not
     *         necessarilly equal in the {@code String} sense to the most recent
     *         argument to {@link #setOnPhases(String)}, it is equivalent with
     *         respect to the semantics of this tag property
     */
    public String getOnPhases() {
        StringBuilder sb = new StringBuilder();
        
        for (String phaseName : includeOnPhases.values()) {
            sb.append(',').append(phaseName);
        }
        sb.deleteCharAt(0);
        
        return sb.toString();
    }

    /**
     * Sets the {@code onPhases} property, which determines the phases on which
     * this tag should evaluate its body
     * 
     * @param  phases a {@code String} containing a comma- or
     *         semicolon-delimited list of the case insensitive names of the
     *         phases during which the body of this tag should be evaluated. The
     *         valid phase names are "registration", "parsing", "fetching",
     *         "processing" and "rendering".
     *
     * @throws IllegalArgumentException if the provided {@code phases}
     *         contains an unrecognized phase name 
     */
    public void setOnPhases(String phases) {
        includeOnPhases.clear();
        
        for (String phase : phases.split(" *[,;] *", -1)) {
            if (phase.equalsIgnoreCase("registration")) {
                includeOnPhases.put(HtmlPage.REGISTRATION_PHASE,
                        "registration");
            } else if (phase.equalsIgnoreCase("parsing")) {
                includeOnPhases.put(HtmlPage.PARSING_PHASE,
                        "parsing");
            } else if (phase.equalsIgnoreCase("fetching")) {
                includeOnPhases.put(HtmlPage.FETCHING_PHASE,
                        "fetching");
            } else if (phase.equalsIgnoreCase("processing")) {
                includeOnPhases.put(HtmlPage.PROCESSING_PHASE,
                        "processing");
            } else if (phase.equalsIgnoreCase("rendering")) {
                includeOnPhases.put(HtmlPage.RENDERING_PHASE,
                        "rendering");
            } else {
                throw new IllegalArgumentException("'" + phase
                        + "' is not a valid phase name");
            }
        }
    }

    /**
     * Determines whether this tag is configured to always skip evaluating its
     * body when suppressed, regardless of phase
     * 
     * @return {@code true} if this tag is configured to always skip evaluating
     *         its body when it is suppressed, {@code false} if not
     */
    public boolean isSkipIfSuppressed() {
        return skipIfSuppressed;
    }

    /**
     * Configures whether this tag should always skip evaluating its body when
     * suppressed, regardless of phase
     * 
     * @param  skipIfSuppressed {@code true} if this tag should always skip
     *         evaluating its body when it is suppressed, {@code false} if it
     *         should ignore suppression in determining whether to evaluate its
     *         body
     */
    public void setSkipIfSuppressed(boolean skipIfSuppressed) {
        this.skipIfSuppressed = skipIfSuppressed;
    }

    /**
     * {@inheritDoc}.  This version delegates back to the superclass, then
     * determines whether to evaluate the body based on the configured
     * collection of phases on which it is configured to do so
     * 
     * @param  pageContext the current {@code PageContext}, potentially
     *         needed by the superclass.
     *        
     * @return {@code EVAL_BODY_INCLUDE} if the body ought to be
     *         evaluated during this phase, otherwise {@code SKIP_BODY}
     *         
     * @throws JspException if an exception is encountered during this method
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);
        
        suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);
        
        return ((includeOnPhases.containsKey(HtmlPage.REGISTRATION_PHASE)
                    && !(skipIfSuppressed 
                            && (suppressionContext != null)
                            && suppressionContext.isTagsBodySuppressedThisPhase()))
                ? EVAL_BODY_INCLUDE
                : SKIP_BODY);
    }

    /**
     * {@inheritDoc}.  This version delegates back to the superclass, then
     * determines whether to evaluate the body based on the configured
     * collection of phases on which it is configured to do so
     * 
     * @param request the current {@code ServletRequest}, potentially
     *        needed by the superclass.
     *        
     * @return {@code EVAL_BODY_INCLUDE} if the body ought to be
     *         evaluated during this phase, otherwise {@code SKIP_BODY}
     *         
     * @throws JspException if an exception is encountered during this method
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        super.onParsingPhaseBeforeBody(request);

        return ((includeOnPhases.containsKey(HtmlPage.PARSING_PHASE)
                && !(skipIfSuppressed 
                        && (suppressionContext != null)
                        && suppressionContext.isTagsBodySuppressedThisPhase()))
            ? EVAL_BODY_INCLUDE
            : SKIP_BODY);
    }

    /**
     * {@inheritDoc}.  This version delegates back to the superclass, then
     * determines whether to evaluate the body based on the configured
     * collection of phases on which it is configured to do so
     * 
     * @return {@code EVAL_BODY_INCLUDE} if the body ought to be
     *         evaluated during this phase, otherwise {@code SKIP_BODY}
     *         
     * @throws JspException if an exception is encountered during this method
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();

        return ((includeOnPhases.containsKey(HtmlPage.FETCHING_PHASE)
                && !(skipIfSuppressed 
                        && (suppressionContext != null)
                        && suppressionContext.isTagsBodySuppressedThisPhase()))
            ? EVAL_BODY_INCLUDE
            : SKIP_BODY);
    }

    /**
     * {@inheritDoc}.  This version delegates back to the superclass, then
     * determines whether to evaluate the body based on the configured
     * collection of phases on which it is configured to do so
     * 
     * @param  pageContext the current {@code PageContext}, potentially
     *         needed by the superclass.
     *        
     * @return {@code EVAL_BODY_INCLUDE} if the body ought to be
     *         evaluated during this phase, otherwise {@code SKIP_BODY}
     *         
     * @throws JspException if an exception is encountered during this method
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onProcessingPhaseBeforeBody(pageContext);

        return ((includeOnPhases.containsKey(HtmlPage.PROCESSING_PHASE)
                && !(skipIfSuppressed 
                        && (suppressionContext != null)
                        && suppressionContext.isTagsBodySuppressedThisPhase()))
            ? EVAL_BODY_INCLUDE
            : SKIP_BODY);
    }

    /**
     * {@inheritDoc}.  This version delegates back to the superclass, then
     * determines whether to evaluate the body based on the configured
     * collection of phases on which it is configured to do so
     * 
     * @param  out a {@code JspWriter}, used by most elements to output
     *         HTML for display.
     *        
     * @return {@code EVAL_BODY_INCLUDE} if the body ought to be
     *         evaluated during this phase, otherwise {@code SKIP_BODY}
     *         
     * @throws IOException if an error occurs while writing to the JspWriter.
     * @throws JspException if an exception is encountered during this method
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);

        return ((includeOnPhases.containsKey(HtmlPage.RENDERING_PHASE)
                && !(skipIfSuppressed 
                        && (suppressionContext != null)
                        && suppressionContext.isTagsBodySuppressedThisPhase()))
            ? EVAL_BODY_INCLUDE
            : SKIP_BODY);
    }

    /**
     * {@inheritDoc}.  This version correctly copies the internal collection of
     * phases on which this tag should evaluate its body and the suppression
     * context reference
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        HtmlPagePhaseEvent copy =
                (HtmlPagePhaseEvent) super.generateCopy(newId, origToCopyMap);
        
        copy.includeOnPhases
                = new LinkedHashMap<Integer, String>(this.includeOnPhases);
        copy.suppressionContext = (SuppressionContext) origToCopyMap.get(
                this.suppressionContext);
        
        return copy;
    }
}
