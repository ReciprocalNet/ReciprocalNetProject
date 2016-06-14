/*
 * Reciprocal Net project
 * 
 * BrowserSpecificElement.java
 * 
 * 24-Jun-2004: cwestnea wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * This tag evaluates its body only when the user's browser is the type
 * specified. There is no default behavior so a browser type must be specified
 * in the attributes. Every phase's body will be skipped if the user is not
 * using the correct browser type. The flags are logically ORed together so any
 * browser that meets any one of the true attributes will be able to see.
 * </p><p>
 * Users should observe that this tag does not <em>suppress</em> its body when
 * the user agent does not meet the configured criteria; rather, it avoids
 * <em>evaluating</em> the body altogether, including any nested custom tags.
 * </p>
 */
public class BrowserSpecificElement extends HtmlPageElement {
    /**
     * Optional attribute, defaults to false; if this is true, the body of this
     * tag will be output if the browser is netscape4x. Attributes are logically
     * ORed so if the user is using any specified browser, the body will be
     * shown.
     */
    private boolean netscape4x;

    /**
     * Optional attribute, defaults to false; if this is true, the body of this
     * tag will be output if the browser is not netscape4x. Attributes are
     * logically ORed so if the user is using any specified browser, the body
     * will be shown.
     */
    private boolean notNetscape4x;

    /**
     * Internal flag which specifies whether or not the body should be
     * processed. Initialized in {@code reset()} and set in
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private boolean shouldShow;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.netscape4x = false;
        this.notNetscape4x = false;
        this.shouldShow = false;
    }

    /**
     * Sets whether the body of this tag should be processed for Netscape 4.x
     * user agents
     * 
     * @param notNetscape4x {@code true} if the body should be evaluated for
     *        Netscape 4.x user agents, {@code false} otherwise
     */
    /** @param netscape4x should the body be evaluated for netscape4x */
    public void setNetscape4x(boolean netscape4x) {
        this.netscape4x = netscape4x;
    }

    /** @return should the body be evaluated for netscape4x */
    public boolean getNetscape4x() {
        return this.netscape4x;
    }

    /**
     * Sets whether the body of this tag should be evaluated for user agents
     * other than Netscape 4.x
     * 
     * @param notNetscape4x {@code true} if the body should be evaluated for
     *        non-Netscape 4.x browsers, {@code false} otherwise
     */
    public void setNotNetscape4x(boolean notNetscape4x) {
        this.notNetscape4x = notNetscape4x;
    }

    /**
     * @return should the body be evaluated for browsers other than netscape4x
     */
    public boolean getNotNetscape4x() {
        return this.notNetscape4x;
    }

    /**
     * {@inheritDoc}; this version decides at this point whether or not the
     * body should be evaluated in this and all subsequent phases
     * 
     * @throws IllegalStateException if none of the attributes are set to true.
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);
        
        if (!this.netscape4x && !this.notNetscape4x) {
            // they mustn't all be off
            throw new IllegalStateException();
        }

        // main logic; check the attributes and the user agent
        if (this.netscape4x) {
            this.shouldShow |= getPage().isBrowserNetscape4x();
        }
        if (this.notNetscape4x) {
            this.shouldShow |= !getPage().isBrowserNetscape4x();
        }

        return (this.shouldShow ? EVAL_BODY_INCLUDE : SKIP_BODY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        super.onParsingPhaseBeforeBody(request);
        return (this.shouldShow ? EVAL_BODY_INCLUDE : SKIP_BODY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();
        return (this.shouldShow ? EVAL_BODY_INCLUDE : SKIP_BODY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onProcessingPhaseBeforeBody(pageContext);
        return (this.shouldShow ? EVAL_BODY_INCLUDE : SKIP_BODY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        return (this.shouldShow ? EVAL_BODY_INCLUDE : SKIP_BODY);
    }
}
