/*
 * Reciprocal Net project
 * 
 * HtmlRedirect.java
 *
 * 19-Feb-2004: midurbin wrote first draft
 * 27-Feb-2004: midurbin modified this class to allow forwarding during the
 *              processing phase as well.
 * 10-Mar-2004: cwestnea fixed bug #1169 in setTarget()
 * 16-Jul-2004: cwestnea fixed bug #1293 in onRegistrationPhaseAfterBody()
 * 04-Aug-2004: cwestnea added support for both relative and absolute targets
 * 26-Aug-2004: midurbin added improved suppression
 * 26-Aug-2004: midurbin added onParsingPhaseAfterBody(),
 *              onFetchingPhaseAfterBody() and onRenderingPhaseAfterBody() to
 *              allow for possible redirection at the completion of any phase
 * 26-Aug-2004: midurbin added preserveParam attribute and generateTargetUrl()
 * 02-Nov-2004: midurbin added generateCopy() to fix bug #1439
 * 25-Feb-2004: midurbin added addRequestParam()
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 19-Jan-2006: jobollin updated copyTransientPropertiesFrom() to use setter
 *              methods; reformatted the source
 * 08-Dec-2015: yuma added preserveParam1 attribute and setPreserveParam1() 
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a Tag that conditionally redirects the browser if the indicated
 * condition is met. At the end of each phase if the condition is true, the
 * browswer is redirected to the provided target URL. The URL can be either
 * absolute or relative. A relative URL will be processed by appending the
 * target to the context path. This means that a proper relative target will
 * look like: target="/page.jsp"
 */
public class HtmlRedirect extends HtmlPageElement {
    /**
     * An optional attribute that is checked at the end of each phase to
     * determine whether or not the browser should be redirected to the
     * {@code target}. Initialized by {@code reset()} and modified by its
     * 'setter' method, {@code setCondition}. This is a 'transient' variable in
     * that its value may change from phase to phase and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean condition;

    /**
     * A required attribute that indicates the URL of the page to which the
     * browser may be redirected. Initialized by {@code reset()} and modified by
     * its 'setter' method, {@code setTarget()}. This is a 'transient' variable
     * in that its value may change form phase to phase and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private String target;

    /**
     * Optional attribute, defaults to false; indicates whether or not the URL
     * to redirect to is an absolute URL or not. Relative URLs are processed by
     * appending the target to the context path. Initialized by {@code reset()}
     * and modified by its 'setter' method, {@code setAbsoluteTarget()}.
     */
    private boolean absoluteTarget;

    /**
     * A {@code Map} of parameter names to parameter values for parameters that
     * will be included on the URL query line for the page to which this tag
     * will redirect. This map is populated by calls to
     * {@code setPreserveParam()} and {@code addParameter()} and is used to
     * generate the redirect URL in {@code generateTarget()}.
     */
    private Map<String, String> targetRequestParamMap;

    /**
     * A refernce to the most immediate {@code SuppressionContext} if one
     * exists. This reference is set by {@code onRegistrationPhaseBeforeBody()}
     * and consulted before redirecting the browser.
     */
    private SuppressionContext suppressionContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.condition = true;
        this.target = null;
        this.absoluteTarget = false;
        this.targetRequestParamMap = null;
        this.suppressionContext = null;
    }

    /** @param condition the condition upon which redirection should occur. */
    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    /** @return the condition upon which redirection should occur. */
    public boolean getCondition() {
        return this.condition;
    }

    /** @param target the URL to which the browser should be redirected */
    public void setTarget(String target) {
        this.target = target;
    }

    /** @return the URL to which the browser should be redirected */
    public String getTarget() {
        return this.target;
    }

    /** @param absoluteTarget whether the target is relative or absolute */
    public void setAbsoluteTarget(boolean absoluteTarget) {
        this.absoluteTarget = absoluteTarget;
    }

    /** @return whether the target is absolute or not */
    public boolean getAbsoluteTarget() {
        return this.absoluteTarget;
    }

    /**
     * Adds an entry to the {@code targetRequestParamMap} that contains the
     * given param name and the value of the request parameter for this page
     * with that name.
     * 
     * @param param the name of an existing URL parameter
     */
    public void setPreserveParam(String param) {
        addRequestParam(param, this.pageContext.getRequest().getParameter(
                param));
    }
    public void setPreserveParam1(String param) {
        addRequestParam(param, this.pageContext.getRequest().getParameter(
                param));
    }


    /**
     * Adds a parameter/value pair to the {@code targetRequestParamMap} that
     * will be included in the query of the URL in the redirect.
     * 
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public void addRequestParam(String name, String value) {
        if (this.targetRequestParamMap == null) {
            this.targetRequestParamMap = new HashMap<String, String>();
        }
        this.targetRequestParamMap.put(name, value);
    }

    /**
     * {@inheritDoc}.  This version finds the
     * most immediate {@code SuppressionContext} if one exits.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        // get the SuppressionContext if one exists
        // if one exists, this tag must use it to determine whether or not to
        // suppress a possible redirect action
        this.suppressionContext = this.findRealAncestorWithClass(
                this, SuppressionContext.class);
        return rc;
    }

    /**
     * {@inheritDoc}; this version redirects the browser to {@code target} if
     * {@code condition} is {@code true} before delegating back to the
     * superclass.
     * 
     * @throws JspException wrapping an IOException if one is thrown while
     *         redirecting the browser
     */
    @Override
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.condition && !((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase())) {
            // issue a redirect to the client
            try {
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        generateTargetUrl());
                return SKIP_PAGE;
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}; this version redirects the browser to {@code target} if
     * {@code condition} is {@code true} before delegating back to the
     * superclass.
     * 
     * @throws JspException wrapping an IOException if one is thrown while
     *         redirecting the browser
     */
    @Override
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {
        if (this.condition && !((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase())) {
            // issue a redirect to the client
            try {
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        generateTargetUrl());
                return SKIP_PAGE;
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onParsingPhaseAfterBody(request);
    }

    /**
     * {@inheritDoc}; this version redirects the browser to {@code target} if
     * {@code condition} is {@code true} before delegating back to the
     * superclass.
     * 
     * @throws JspException wrapping an IOException if one is thrown while
     *         redirecting the browser
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if (this.condition && !((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase())) {
            // issue a redirect to the client
            try {
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        generateTargetUrl());
                return SKIP_PAGE;
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onFetchingPhaseAfterBody();
    }

    /**
     * {@inheritDoc}; this version redirects the browser to {@code target} if
     * {@code condition} is {@code true} before delegating back to the
     * superclass.
     * 
     * @throws JspException wrapping an IOException if one is thrown while
     *         redirecting the browser
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.condition && !((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase())) {
            // issue a redirect to the client
            try {
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        generateTargetUrl());
                return SKIP_PAGE;
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}; this version redirects the browser to {@code target} if
     * {@code condition} is {@code true} before delegating back to the
     * superclass.
     * 
     * @throws IOException if an error is encountered while redirecting the
     *         browser
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        if (this.condition && !((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase())) {
            // issue a redirect to the client
            ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                    generateTargetUrl());
            return SKIP_PAGE;
        }
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        HtmlRedirect src = (HtmlRedirect) source;
        setCondition(src.condition);
        setTarget(src.target);
    }

    /**
     * A helper method to generate the complete target URL based on this tag's
     * properties.
     * 
     * @return an absolute URL with all the non-null parameters indicated by
     *         calls to {@code setPreserveParam()} and
     *         {@code addRequestParam()}.
     */
    protected String generateTargetUrl() {
        StringBuilder actualTarget = new StringBuilder();
        
        if (!this.absoluteTarget) {
            actualTarget.append(getPage().getContextPath());
        }
        actualTarget.append(this.target);
        if (this.targetRequestParamMap != null) {
            // attach any preserved parameters
            boolean first = true;
            
            for (Entry<String, String> entry
                    : targetRequestParamMap.entrySet()) {
                String nextParam = entry.getKey();
                String nextValue = entry.getValue();
                if (nextValue != null) {
                    try {
                        actualTarget.append((first ? "?" : "&") + nextParam
                                + "=" + URLEncoder.encode(nextValue, "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        // can't happen because "UTF-8" is always supported
                        assert false;
                    }
                    first = false;
                }
            }
        }
        
        return actualTarget.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HtmlRedirect dc = (HtmlRedirect) super.generateCopy(newId, map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        if (this.targetRequestParamMap != null) {
            dc.targetRequestParamMap = new HashMap<String, String>(
                    this.targetRequestParamMap);
        }
        
        return dc;
    }
}
