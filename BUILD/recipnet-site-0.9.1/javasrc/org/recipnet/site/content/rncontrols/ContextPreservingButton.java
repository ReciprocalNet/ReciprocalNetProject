/*
 * Reciprocal Net project
 * 
 * ContextPreservingButton.java
 * 
 * 25-Aug-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 24-Jun-2005: midurbin added support for subclasses to add URI parameters
 * 06-Oct-2005: midurbin fixed bug #1655 in onProcessingPhaseAfterBody()
 * 10-Nov-2005: midurbin fixed a bug in generateCopy() so that a deep copy of
 *              the uriParameters would be made
 * 13-Jan-2006: jobollin made this class implement DynamicAttributes; removed
 *              unused imports; updated docs
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.recipnet.common.controls.ButtonHtmlControl;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * This tag creates a button that when clicked simply redirects the browser to
 * the page indicated by the 'target' attribute.  The address is assumed to be
 * relative to the context path of the servlet and should be of the form
 * "/page.jsp".  If this tag is nested within a {@code SampleContext} it
 * will add the parameters 'sampleId' and 'sampleHistoryId' to the URL so
 * that the {@code SampleContext} may be preserved.  This tag supports "dynamic
 * attributes" in such a way that adding attributes to the corresponding custom
 * action that are not otherwise mapped by the TLD results in those attributes
 * being used to add request parameters to the URL to which this button
 * redirects.
 */
public class ContextPreservingButton extends ButtonHtmlControl
        implements DynamicAttributes {
    
    // TODO: add on to this class to support other contexts as needed

    /**
     * Required attribute containing the relative path of the page to which
     * this button will redirect the browser when clicked.  This path must be
     * of the form "/page.jsp" and is relative to the servlet context.
     * Initialized in {@link #reset()} and used in
     * {@link #onProcessingPhaseAfterBody(PageContext)}.
     */
    private String target;

    /**
     * The most immediate {@code SampleContext}; determined by
     * {@link #onRegistrationPhaseBeforeBody(PageContext)} and used if this
     * button is clicked to determine the "sampleId" and "sampleHistoryId" that
     * will be provided as URL parameters to the page indicated by the
     * {@code target} attribute. If no {@code SampleContext} is found, this
     * value will be {@code null} and no parameters will be added to the URL.
     */
    private SampleContext sampleContext;

    /**
     * A map containing parameter names and their values to be added to the
     * URL. These parameters and values are appended to the URL. Initialized 
     * in {@link #reset()} and created and added to in  {@code addParameter}.
     */
    private Map<String, String> uriParameters;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.target = null;
        this.sampleContext = null;
        this.uriParameters = new HashMap<String, String>();
    }

    /**
     * Sets the target to which this button redirects the user
     * 
     * @param  target a {@code String} containing the path (relative to the
     *         servlet context) of the page to which the browser should be
     *         redirected if this button is clicked
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Gets the target to which this button redirects the user
     * 
     * @return a {@code String} containing the path (relative to the servlet
     *         context) to which the browser will be redirected if this button
     *         is clicked
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * Adds parameters that will be appended to the URL to which this button
     * redirects.
     * 
     * @param  name the name of the parameter (if another parameter with this
     *         name exists, it will be silently replaced)
     * @param  value the value of the parameter
     */
    protected void addParameter(String name, String value) {
        this.uriParameters.put(name, value);
    }

    /**
     * {@inheritDoc}.  This implementation finds and records the innermost
     * surrounding {@code SampleContext}, if one exists.
     * 
     * @throws IllegalStateException if no {@code SampleContext} is found
     * @throws JspException if the superclass encounters and exception
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        // get sampleContext if one exists
        this.sampleContext
                = this.findRealAncestorWithClass(this, SampleContext.class);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This implementation checks whether this button was
     * clicked; if so, it redirects the browser to the configured {@code target}
     * before returning {@code SKIP_PAGE}
     * 
     * @throws JspException wrapping any {@code IOException} thrown while
     *         redirecting the request
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.getValueAsBoolean()) {
            StringBuffer targetFullUrl = new StringBuffer();
            targetFullUrl.append(this.getPage().getContextPath()
                    + this.target);
            if ((this.sampleContext != null)
                    && (this.sampleContext.getSampleInfo() != null)) {
                this.addParameter("sampleId",
                        String.valueOf(this.sampleContext.getSampleInfo().id));
                this.addParameter("sampleHistoryId", String.valueOf(
                        this.sampleContext.getSampleInfo().historyId));
            } else {
                // there is no SampleContext or it has no sample, add nothing 
                // to the URL
            }

            try {
                boolean first = true;
                for (Entry<String, String> entry
                        : this.uriParameters.entrySet()) {
                    targetFullUrl.append((first ? "?" : "&"));
                    targetFullUrl.append(entry.getKey());
                    targetFullUrl.append("=");
                    targetFullUrl.append(
                            URLEncoder.encode(entry.getValue(), "UTF-8")); 
                    first = false;
                }

                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        targetFullUrl.toString());
                return SKIP_PAGE;
            } catch (UnsupportedEncodingException ex) {
                // can't happen because UTF-8 is always a valid encoding
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}. This implementation delegates to the superclass then
     * updates any references to 'owned' controls or referenced ancestor tags
     * using the 'map' parameter that was populated by the superclass'
     * implementation as well as the caller, then makes a deep copy of any
     * complex modifiable member variables before returning the deep copy.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ContextPreservingButton dc
                = (ContextPreservingButton) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.uriParameters = new HashMap<String, String>(this.uriParameters);
        
        return dc;
    }

    /**
     * {@inheritDoc}.  Dynamic attributes set on this tag may be transient, so
     * the whole parameter map is handled as a transient property
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        this.uriParameters.putAll(
                ((ContextPreservingButton) source).uriParameters);
    }

    /**
     * {@inheritDoc}.  The local name of the specified attribute is used as
     * the name of a request parameter that this button should include in the
     * redirection it produces; the string value of the {@code value} argument
     * is used as the value, except that {@code null} is mapped to an empty
     * string.
     * 
     * @see DynamicAttributes#setDynamicAttribute(String, String, Object)
     */
    public void setDynamicAttribute(@SuppressWarnings("unused") String uri,
            String localName, Object value) {
        addParameter(localName, ((value == null) ? "" : value.toString()));
    }
}
