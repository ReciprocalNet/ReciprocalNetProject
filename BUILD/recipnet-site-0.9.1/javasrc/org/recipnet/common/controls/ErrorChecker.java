/*
 * Reciprocal Net project
 * 
 * ErrorChecker.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 24-Jun-2004: midurbin removed suppressHtmlFormatting attribute and changed
 *              implementaton to guarantee evaluation up until the final phase
 * 25-Jun-2004: cwestnea removed NO_ERROR_REPORTED constant
 * 23-Aug-2004: midurbin added invertFilter attribute
 * 26-Aug-2004: midurbin added SuppressionContext implementation
 * 15-Oct-2004: midurbin fixed bug #1289 in onRegistrationPhaseBeforeBody()
 *              and copyTransientPropertiesFrom()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 17-Jan-2006: jobollin renamed this class from ErrorMessageElement to
 *              ErrorChecker and made it extend AbstractChecker; updated
 *              docs; deprecated the invertFilter getter and setter (which
 *              now are just aliases for the superclass's getInvert() and
 *              setInvert())
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * This is a tag handler for a checker tag that suppresses evaluation of its
 * body during the {@code RENDERING_PHASE} unless the error code supplied by
 * the provided {@code ErrorSupplier} implementor is consistent with the
 * optional filter (and checker inversion status).  
 */
public class ErrorChecker extends AbstractChecker
        implements SuppressionContext {
    /**
     * This is a possible error filter indicating that ANY error should
     * trigger the evaluation of the body of this object.
     */
    public static final int ANY_ERROR = 0xFFFFFFFF;

    /**
     * This is an optional transient attribute, indicating the control whose
     * errors will trigger evaluation of this control's body.  If no control is
     * provided, the closes ancestor that implements {@code ErrorSupplier}
     * will be used.  This variable is initialized by {@code reset()} and
     * may be altered by calls to its setter method, {@code setControl()}.
     */
    private ErrorSupplier errorSupplier;

    /**
     * An internal variable that indicates that 'errorSupplier' was set by 
     * a call to {@code setErrorSupplier()} and not detected automatically
     * during the {@code REGISTRATION_PHASE}.
     */
    private boolean errorSupplierWasProvided;

    /** 
     * This is an optional attribute that that is the logical OR of all error
     * codes that should be detected by this {@code ErrorChecker}.
     * By default, any error will trigger display of the body of this tag. This
     * variable is initialized by {@code reset()} and may be altered by
     * calls to its setter method, {@code setErrorFilter()}.  It is 
     * 'transient' (meaning it may change from phase to phase) and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private int errorFilter;

    /**
     * Indicates whether the {@code errorFilter} indicates error codes
     * that if present should cause the output of the body evaluation during
     * the rendering phase to be suppressed or not.  If
     * {@code invertFilter} is false, (the default) the presence of the
     * error codes indicated by {@code errorFilter} will cause the ouput
     * of the body evaluation during the {@code RENDERING_PHASE} to be
     * rendered, otherwise it will indicate that it should be suppressed.
     */
    // private boolean invertFilter;

    /**
     * The most immediate {@code SuppressionContext} implementation;
     * determined during {@code onRegistrationPhaseBeforeBody()} and used
     * to ensure that this tag propagates suppression indicators from its
     * ancestry along with its own suppression indicators.
     */
    // private SuppressionContext suppressionContext;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.errorFilter = ErrorChecker.ANY_ERROR;
        this.errorSupplier = null;
        this.errorSupplierWasProvided = false;
    }

    /**
     * @param errorSupplier the {@code HtmlControl} implementing
     * {@code ErrorSupplier} whose error codes will be detected by this
     * {@code ErrorChecker}.
     * 
     * @throws IllegalArgumentException if {@code errorSupplier} is {@code null}
     *         and the phase is {@code FETCHING_PHASE} or later
     */
    public void setErrorSupplier(ErrorSupplier errorSupplier) {
        if ((errorSupplier == null)
                && (getPage().getPhase() != HtmlPage.REGISTRATION_PHASE)
                && (getPage().getPhase() != HtmlPage.PARSING_PHASE)) {
            throw new IllegalArgumentException();
        }
        this.errorSupplierWasProvided = true;
        this.errorSupplier = errorSupplier;
    }

    /**
     * @return the {@code HtmlControl} implementing
     * {@code ErrorSupplier} whose error codes will be detected by this
     * {@code ErrorChecker}.
     */
    public ErrorSupplier getErrorSupplier() {
        return this.errorSupplier;
    }

    /**
     * @param errorFilter the logical OR of all the error codes that should
     * trigger evaluation of this tag's body.
     */
    public void setErrorFilter(int errorFilter) {
        this.errorFilter = errorFilter;
    }

    /**
     * @return the logical OR of all the error codes that should
     * trigger evaluation of this tag's body.
     */
    public int getErrorFilter() {
        return this.errorFilter;
    }

    /**
     * @param invert if true, indicates that the body should be rendered if the
     *     error code does NOT match the error filter, otherwise the body will
     *     be rendered if the error code does match the error filter.
     * 
     * @deprecated this method is a hold-over from before this class was
     *             converted to extend AbstractChecker
     */
    @Deprecated
    public void setInvertFilter(boolean invert) {
        setInvert(invert);
    }

    /**
     * @return a boolean that, if true, indicates that the body should be
     *     rendered if the error code does NOT match the error filter,
     *     otherwise the body will be rendered if the error code does match the
     *     error filter.
     * 
     * @deprecated this method is a hold-over from before this class was
     *             converted to extend AbstractChecker
     */
    @Deprecated
    public boolean getInvertFilter() {
        return getInvert();
    }

    /**
     * {@inheritDoc}.  This version obtains an {@code ErrorSupplier} from among
     * this tag's ancestors if none was explicitly set.
     * 
     * @throws  IllegalStateException if no {@code ErrorSupplier} was
     *          specified and none can be found enclosing this tag
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.errorSupplier == null) {
            // no ErrorSupplier has been set, find one from among the tags that 
            // enclose this one or throw an exception if none can be found
            this.errorSupplier = this.findRealAncestorWithClass(
                    this, ErrorSupplier.class);
            if (this.errorSupplier == null) {
                throw new IllegalStateException("No error supplier");
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version determines whether or not the body
     * suppression condition has been satisfied, then delegates to the
     * superclass's implementation.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        inclusionConditionMet
                = ((this.errorSupplier.getErrorCode() & this.errorFilter) != 0);
        
        return super.onRenderingPhaseBeforeBody(out);
    }

    /**
     * {@inheritDoc}.  This version updates the error supplier from which this
     * tag obtains error codes, provided that the supplier was explicitly
     * specified as a (transient) tag attribute.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        
        ErrorChecker src = (ErrorChecker) source;
        
        if (this.errorSupplierWasProvided) {
            setErrorSupplier(src.errorSupplier);
        }
    }

    /**
     * {@inheritDoc}.  This version updates the internal {@code ErrorSupplier}
     * reference, provided that it was obtained implicitly from the tag
     * containment hierarchy.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ErrorChecker dc = (ErrorChecker) super.generateCopy(newId, map);

        if (!this.errorSupplierWasProvided) {
            dc.errorSupplier = (ErrorSupplier) map.get(this.errorSupplier);
        }
        
        return dc;
    }
}
