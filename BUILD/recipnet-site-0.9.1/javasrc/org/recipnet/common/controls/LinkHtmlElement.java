/*
 * Reciprocal Net project
 * 
 * LinkHtmlElement.java
 * 
 * 18-Jun-2004: cwestnea wrote first draft
 * 09-Aug-2004: cwestnea added visibled and disabled attributes
 * 30-Aug-2004: midurbin added openInWindow attribute
 * 30-Sep-2004: jobollin fixed bug #1405 by removing dependency on
 *              org.recipnet.site.UnexpectedExceptionException
 * 02-Nov-2004: midurbin added generateCopy() to fix bug #1439
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 26-Apr-2005: midurbin added 'hrefIsAbsolute' property, support for
 *              linking to absolute URLs and the method getWholeHrefForATag()
 * 28-Apr-2005: midurbin fixed bug #1582 in setValue()
 * 27-Sep-2005: midurbin added a SuppressionContext implementation for when
 *              'visible' is false
 * 19-Jan-2006: jobollin updated copyTransientPropertiesFrom() to use setter
 *              methods; reformatted the code; removed unused imports; made the
 *              uri parameter map default to empty instead of null
 * 13-Mar-2006: jobollin made this tag escape the contents of the attributes it
 *              outputs
 * 16-Mar-2006: jobollin made the tag implement DynamicAttributes to provide a
 *              mechanism for adding attributes to the output tag, and removed
 *              the awful setName() / setValue() mechanism for adding query
 *              parameters (in favor of the new ParameterHtmlElement tag)
 * 12-Jun-2006: jobollin suppressed an unused argument warning
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

/**
 * This tag corresponds to an &lt;a&gt; tag in HTML. All hrefs are assumed to 
 * be relative to the context path of the servlet unles the property
 * 'hrefIsAbsolute' is set to true. There are some other helpful
 * features as well. This tag produces output similar to this: 
 * &lt;a href="(/context){href}?{param1}={value1}&amp;{param2}={value2}..." 
 * style="{style}" class="{styleClass}" &gt;
 */
public class LinkHtmlElement extends HtmlPageElement
        implements SuppressionContext, DynamicAttributes {

    /**
     * A refernece to the most immediate {@code SuppressionContext}
     * implementation in which this tag is nested if one exists.  This
     * reference may be set by {@code onRegistrationPhaseBeforeBody()} and
     * if present, its suppression indication will be echoed by this tag.
     */
    private SuppressionContext suppressionContext;

    /**
     * Required transient attribute containing the URL of the page that this
     * link points to. When a relative path is specified, it must be of the
     * form "/page.jsp" and be relative to the servlet context path.  When an
     * absolute path is specified it must be the complete address and the
     * property 'hrefIsAbsolute' must be set to true.  Initialized in
     * {@code reset()} and used in
     * {@code onRenderingPhaseBeforeBody()}.
     */
    private String href;

    /**
     * An optional property that when set to true indicates that the 'href'
     * property is to be interprited as an absolute URL (without any query
     * parameters; they should be added using the addParameter() method).  This
     * property defaults to false, indicating that 'href' must be relative to
     * the context path.
     */
    private boolean hrefIsAbsolute;

    /**
     * Optional transient attribute, defaults to null; contains the inline 
     * style that this link should use. This text is output to the "style" 
     * attribute on the &lt;a&gt; tag. If this variable is null, no attribute 
     * is output. The cascading quality of CSS means that the class attribute 
     * will provide a basic style which may be overrided by the inline style 
     * attribute. Initialized in {@code reset()} and used in 
     * {@code onRenderingPhaseBeforeBody()}.
     */
    private String style;

    /**
     * Optional transient attribute, defaults to null; contains the style class
     * that this link should use. This text is output to the "class" attribute
     * on the &lt;a&gt; tag. If this variable is null, no attribute is output. 
     * The cascading quality of CSS means that the class attribute will provide
     * a basic style which may be overrided by the inline style attribute.
     * Initialized in {@code reset()} and used in 
     * {@code onRenderingPhaseBeforeBody()}.
     */
    private String styleClass;

    /**
     * Optional attribute, defaults to null; contains the name of the 
     * parameter which should be preserved. What this means is that the 
     * majority of the time, this parameter will take the default value given 
     * to it in the attributes for this tag, like every other parameter. When
     * the requested url is the same as the href, however, this parameter will
     * preserve the value that is parsed from the request parameters.
     * Initialized in {@code reset()}.
     */
    private String preserveParam;

    /**
     * Optional transient attribute, defaults to true; if this is false, the 
     * body will be skipped in {@code onRenderingPhaseBeforeBody()}.
     */
    private boolean visible;

    /**
     * Optional transient attribute, defaults to false; if this is true, 
     * instead of a link, the body will be made bold.
     */
    private boolean disabled;

    /**
     * Optional attribute that when set, indicates the name of the window that
     * will be the 'target' of the HTML &lt;a&gt; tag.  This value defaults to
     * null which indicates that the link should open in the current window.
     */
    private String openInWindow;

    /**
     * A map containing parameter names and their values to be added to the
     * URL. These parameters and values are appended to the URL. Initialized 
     * in {@code reset()} and created and added to in 
     * {@code addParameter}.
     */
    private Map<String, String> uriParameters;

    /**
     * If true, specifies that this element's ID should be rendered as an ID
     * attribute on the corresponding anchor element
     */
    private boolean renderId;
    
    /**
     * A map supporting this tag handler's dynamic attributes.  This handler's
     * dynamic attributes are transient, in that they can change from phase to
     * phase.  They are used during the rendering phase to create extra HTML
     * attributes on the output anchor tag
     */
    private Map<String, String> dynamicAttributes;
    
    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
        this.href = null;
        this.hrefIsAbsolute = false;
        this.style = null;
        this.styleClass = null;
        this.preserveParam = null;
        this.uriParameters = new HashMap<String, String>();
        this.visible = true;
        this.disabled = false;
        this.openInWindow = null;
        this.renderId = false;
        this.dynamicAttributes = new HashMap<String, String>();
    }

    /**
     * @param href the url of the page being linked to
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * @return the url of the page being linked to
     */
    public String getHref() {
        return this.href;
    }

    /**
     * @param isAbsolute a boolean indicating whether the 'href' property is
     *     an absolute address or not
     */
    public void setHrefIsAbsolute(boolean isAbsolute) {
        this.hrefIsAbsolute = isAbsolute;
    }

    /**
     * @return a boolean indicating whether the 'href' property is
     *     an absolute address or not
     */
    public boolean getHrefIsAbsolute() {
        return this.hrefIsAbsolute;
    }

    /**
     * @param style the style of the link to display
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the style of the link to display
     */
    public String getStyle() {
        return this.style;
    }

    /**
     * @param styleClass the style class of the link to display
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * @return the style class of the link to display
     */
    public String getStyleClass() {
        return this.styleClass;
    }

    /**
     * @param preserveParam the name of the parameter that, if the requested 
     *     URL is the same as the href, should get its value from the submitted
     *     values instead of using the default value specified.
     */
    public void setPreserveParam(String preserveParam) {
        this.preserveParam = preserveParam;
    }

    /**
     * @return the name of the parameter that, if the requested URL is the 
     *     same as the href, should get its value from the submitted values 
     *     instead of using the default value specified.
     */
    public String getPreserveParam() {
        return this.preserveParam;
    }

    /**
     * @param disabled whether or not this link is disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return whether or not this link is disabled
     */
    public boolean getDisabled() {
        return this.disabled;
    }

    /**
     * @return whether or not this link is visible
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * @param visible whether or not this link is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @param windowName the name of the window in which this link should open,
     *     or null if it should open in the current window
     */
    public void setOpenInWindow(String windowName) {
        this.openInWindow = windowName;
    }

    /**
     * @return the name of the window in which this link will open, or null if
     *     it will open in the current window
     */
    public String getOpenInWindow() {
        return this.openInWindow;
    }

    /**
     * Determines whether this element will render its ID as an ID attribute on
     * the correspondign anchor tag
     * 
     * @return {@code true} if the ID will be rendered, {@code false} if not
     */
    public boolean isRenderId() {
        return renderId;
    }

    /**
     * Sets whether this tag will render its ID as an ID attribute on the
     * corresponding anchor tag
     * 
     * @param  renderId {@code true} if this element should render its ID,
     *         {@code false} if not
     */
    public void setRenderId(boolean renderId) {
        this.renderId = renderId;
    }

    /**
     * This adds a parameter to the map of parameters. If there is already a 
     * parameter with that name, this method will overwrite its value. This 
     * method is public so subclasses and any other tag may add parameters as
     * necessary.
     * @param name the name of the parameter to add
     * @param value the value of the parameter to add
     */
    public void addParameter(String name, String value) {
        this.uriParameters.put(name, value);
    }

    /**
     * Sets a dynamic attribute on this tag; this version uses the local name
     * and string representation of the value to output an attribute in the
     * anchor tag created by this tag
     * 
     * @see DynamicAttributes#setDynamicAttribute(String, String, Object)
     */
    public void setDynamicAttribute(@SuppressWarnings("unused") String uri,
            String localName, Object value)
            throws JspException {
        assert (localName != null) : "null dynamic attribute name";
        
        if (value != null) {
            dynamicAttributes.put(localName, value.toString());
        } // null values are silently ignored
    }

    /**
     * Implements {@code SuppressionContext}.  The current implementation
     * determines whether the body is to be suppressed based on the 'visible'
     * property's value as well as any surrounding
     * {@code SuppressionContext}.
     */
    public boolean isTagsBodySuppressedThisPhase() {
        if ((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase()) {
            return true;
        }
        
        return !this.visible;
    }

    /**
     * {@inheritDoc}.  This version gets the most immediate
     * {@code SuppressionContext} if one exists.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get the SuppressionContext if one exists
        // if one exists, a reference is needed so that its
        // isTagsBodySuppressedThisPhase() return value may be propagated
        this.suppressionContext = this.findRealAncestorWithClass(
                this, SuppressionContext.class);

        return rc;
    }

    /**
     * {@inheritDoc}.  This version outputs the appropriate 
     * &lt;a&gt; tag to represent this tag.
     * 
     * @param out the {@code JspWriter} to which the HTML for the control
     *     is written
     *     
     * @return the return value of the superclass' implementation is returned
     * 
     * @throws IOException if an error occurs while writing to the JspWriter.
     * @throws JspException if any other exceptions are encountered during this
     *     method.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) 
            throws IOException, JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        
        if (isTagsBodySuppressedThisPhase()) {
            return EVAL_BODY_BUFFERED;
        } else if (this.disabled) {
            out.print("<strong>");
        } else {
            HttpServletRequest request
                    = (HttpServletRequest) super.pageContext.getRequest();
    
            // preserve parameter if appropriate
            if ((this.preserveParam != null) 
                    && request.getServletPath().equals(href)) {
                // preserved the passed in value if this is the right page
                String rawValue = request.getParameter(this.preserveParam);
                
                if (rawValue == null) {
                    rawValue = "";
                }
                addParameter(this.preserveParam, rawValue);
            }
    
            out.print("<a href=\"");
            out.print(HtmlControl.escapeAttributeValue(getWholeHrefForATag()));

            // output parameters
            out.print(HtmlControl.escapeAttributeValue(
                    getParametersAsString()));
            out.print("\" ");

            if (isRenderId()) {
                out.print("id=\"");
                out.print(HtmlControl.escapeAttributeValue(getId()));
                out.print("\" ");
            }
            
            // output style information
            if (this.styleClass != null) {
                out.print("class=\"");
                out.print(HtmlControl.escapeAttributeValue(this.styleClass));
                out.print("\" ");
            }
    
            if (this.style != null) {
                out.print("style=\"");
                out.print(HtmlControl.escapeAttributeValue(this.style));
                out.print("\" ");
            }
    
            // output target window information
            if (this.openInWindow != null) {
                out.print("target=\"");
                out.print(HtmlControl.escapeAttributeValue(this.openInWindow));
                out.print("\" ");
            }

            // output extra attributes
            for (Entry<String, String> entry : dynamicAttributes.entrySet()) {
                out.print(entry.getKey());
                out.print("=\"");
                out.print(HtmlControl.escapeAttributeValue(entry.getValue()));
                out.print("\" ");
            }
            
            out.print(">");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version outputs the &lt;/a&gt; tag.
     * 
     * @param out the {@code JspWriter} to which the HTML for the control
     *     is written
     *     
     * @return the return value of the superclass' implementation is returned
     * 
     * @throws IOException if an error occurs while writing to the JspWriter.
     * @throws JspException if any other exceptions are encountered during this
     *     method.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) 
            throws IOException, JspException {
        int rc = super.onRenderingPhaseAfterBody(out);
        
        if (!isTagsBodySuppressedThisPhase()) {
            out.print(this.disabled ? "</strong>" : "</a>");
        }
        
        return rc;
    }

    /**
     * <p>
     * A helper method that returns a {@code String} representation of the
     * URL that should be used as the 'href' property of the HTML &lt;a&gt; tag
     * that is output by this tag.
     * </p><p>
     * This method may be overridden by subclasses when new properties affect
     * the 'href'.  This class will not invoke this method before the
     * {@code RENDERING_PHASE}.  The base class implementation simply
     * outputs the 'href' property when 'hrefIsAbsolute' is true, otherwise it
     * outputs the context path followed by the 'href' property.
     * </p>
     *
     * @return a {@code String} that may be used in an HTML &lt;a&gt; tag
     */
    protected String getWholeHrefForATag() {
        if (getHrefIsAbsolute()) {
            return getHref();
        } else {
            return ((HttpServletRequest) this.pageContext.getRequest()
                    ).getContextPath() + getHref();
        }
    }

    /**
     * {@inheritDoc}.  This version copies {@code href}, 
     * {@code style}, {@code styleClass}, and on rendering phase,
     * adds the parameters and their values to the parameter map.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);

        LinkHtmlElement src = (LinkHtmlElement) source;

        // make sure not to overwrite with default
        if (src.href != null) {
            setHref(src.href);
        }
        setStyle(src.style);
        setStyleClass(src.styleClass);
        this.uriParameters.putAll(src.uriParameters);
        this.dynamicAttributes.putAll(src.dynamicAttributes);

        // make sure we are not overwriting with the default
        if (!src.getVisible()) {
            setVisible(false);
        }
        if (src.getDisabled()) {
            setDisabled(true);
        }
    }

    /** 
     * Internal helper method that converts the {@code Map} of URI 
     * parameters and their values into a {@code String} of the format: 
     * ?name1=value1&amp;name2=value2 . The values are URL encoded, so the 
     * return value may be directly inserted into the href.
     *
     * @return a formatted {@code String} of attribute/value pairs, or 
     *         the blank string if no parameters are defined.
     *
     * @throws RuntimeException if the Java runtime library doesn't support
     *         UTF-8 (which would be a violation of the Java platform library
     *         spec, so it ought never to happen)
     */
    private String getParametersAsString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (Entry<String, String> entry : uriParameters.entrySet()) {
            if (first) {
                sb.append('?');
                first = false;
            } else {
                sb.append('&');
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sb.append('='); 
                sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch(UnsupportedEncodingException ex) {

                /*
                 * Java requires that all implementations support the UTF-8
                 * character encoding, so this should never happen
                 */
                throw new RuntimeException(ex);
            }
        }
        
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        LinkHtmlElement dc = (LinkHtmlElement) super.generateCopy(newId, map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        dc.uriParameters = new HashMap<String, String>(this.uriParameters);
        dc.dynamicAttributes
                = new HashMap<String, String>(this.dynamicAttributes);
        
        return dc;
    }
}
