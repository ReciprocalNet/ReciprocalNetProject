/*
 * Reciprocal Net Project
 *
 * FormHtmlElement.java
 *
 * 16-May-2005: midurbin wrote first draft using SelfFormHtmlElement's code
 * 19-Jan-2006: jobollin updated copyTransientPropertiesFrom to use setter
 *              methods; reformatted the source
 * 13-Mar-2006: jobollin made the form method be output in lower case for
 *              conformance with XHTML (it is case-insensitive in HTML 4)
 * 23-Jun-2006: jobollin implemented additional escaping of output HTML
 *              attribute values
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * <p>
 * A tag handler class for a phase-recognizing custom action that outputs an
 * HTML form tag. It sets the form's accept-charset attribute to "UTF-8" to
 * demand that user agents encode characters in UTF-8 (which elsewhere we
 * <em>assume</em> they do, but which HTML does not require in the absence of
 * such an attribute). This tag handler supports the following properties:
 * </p>
 * <dl>
 * <dt>action</dt>
 * <dd>the value to specify for the "action" attribute of the &lt;form&gt; tag
 * output by this handler, or {@code null} (the default) to indicate that the
 * current URI should be used as the action. <i>This tag is currently used in to
 * TLD entries: the "selfForm" and "form" tags. In the case where it's used as a
 * "selfForm" this 'action' attribute is NOT exposed causing the value to
 * default to null, indicating that the action will be the current page
 * URL.</i></dd>
 * <dt>method</dt>
 * <dd>the value to specify for the "method" attribute of the &lt;form&gt; tag
 * output by this handler. Defaults to "POST" if not explicitly specified, and
 * should not be null.</dd>
 * <dt>accept</dt>
 * <dd>the value to specify for the "accept" attribute of the &lt;form&gt; tag
 * output by this handler, or {@code null} (the default) to indicate that no
 * accept attribute should be specified. This HTML attribute is used to specify
 * the content types that the server is prepared to accept, and may be used by
 * user agents to filter out non-conforming files from a file selection
 * control</dd>
 * <dt>enctype</dt>
 * <dd>the value to specify for the "enctype" attribute of the &lt;form&gt; tag
 * output by this handler, or {@code null} (the default) to indicate that no
 * enctype attribute should be specified. This HTML attribute specifies the
 * content type that should be used to submit the form to the server; HTML
 * specifies that the default for this attribute is
 * "application/x-www-form-urlencoded", and that type "multipart/form-data"
 * should be used with &lt;input&gt; elements of type file</dd>
 * <dt>pageForm</dt>
 * <dd>a boolean property that determines whether or not this tag handler is to
 * output a "page form", which draws extra form content from the containing
 * {@code HtmlPage} if that object provides any and outputs it directly after
 * the form start tag. Such additional content might include input elements of
 * type "hidden", for instance. It is legal to have more than one page form in
 * the same page. Defaults to {@code true}</dd>
 * </dl>
 * <p>
 * Other form attributes defined by HTML are not supported by this version of
 * this handler.
 * </p>
 * <p>
 * This tag handler exhibits non-default behavior only during the rendering
 * phase.
 * </p>
 */
public class FormHtmlElement extends HtmlPageElement {

    /**
     * The value of this tag handler's "action" property; may be {@code null}
     * (the default). This URL is considered to be relative to the context path.
     */
    private String action;

    /**
     * The value of this tag handler's "accept" property; may be {@code null}
     * (the default)
     */
    private String accept;

    /**
     * The value of this tag handler's "enctype" property; may be {@code null}
     * (the default)
     */
    private String enctype;

    /**
     * The value of this tag handler's "method" property; may not be
     * {@code null}; defaults to "POST"
     */
    private String method;

    /**
     * The value of this tag handler's "pageForm" property; defaults to
     * {@code true}
     */
    private boolean pageForm;
    
    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.action = null;
        this.accept = null;
        this.enctype = null;
        this.pageForm = true;
        this.method = "post";
    }

    /**
     * Obtains the value of this tag handler's "action" property
     * 
     * @return the value of this tag handler's "action" property
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of this tag handler's "action" property; a {@code null}
     * value specifies that no action attribute should be included in the HTML
     * output of this tag handler
     * 
     * @param actionValue the new value for the action property
     */
    public void setAction(String actionValue) {
        this.action = actionValue;
    }

    /**
     * Obtains the value of this tag handler's "accept" property
     * 
     * @return the value of this tag handler's "accept" property
     */
    public String getAccept() {
        return accept;
    }

    /**
     * Sets the value of this tag handler's "accept" property; a {@code null}
     * value specifies that no accept attribute should be included in the HTML
     * output of this tag handler
     * 
     * @param acceptValue the new value for the accept property
     */
    public void setAccept(String acceptValue) {
        accept = acceptValue;
    }

    /**
     * Obtains the value of this tag handler's "enctype" property
     * 
     * @return the value of this tag handler's "enctype" property
     */
    public String getEnctype() {
        return enctype;
    }

    /**
     * Sets the value of this tag handler's "enctype" property; a {@code null}
     * value specifies that no enctype attribute should be included in the HTML
     * output of this tag handler
     * 
     * @param enctypeValue the new value for the enctype property
     */
    public void setEnctype(String enctypeValue) {
        enctype = enctypeValue;
    }

    /**
     * Obtains the value of this tag handler's "method" property
     * 
     * @return the value of this tag handler's "method" property
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of this tag handler's "method" property; a method
     * attribute is always included in the HTML output of this tag handler
     * 
     * @param methodValue the new value for the method property
     */
    public void setMethod(String methodValue) {
        method = methodValue;
    }

    /**
     * Obtains the value of this tag handler's "pageForm" property
     * 
     * @return the value of this tag handler's "pageForm" property
     */
    public boolean isPageForm() {
        return pageForm;
    }

    /**
     * Sets the value of this tag handler's "pageForm" property;
     * 
     * @param pageFormValue the new value for the pageForm property
     */
    public void setPageForm(boolean pageFormValue) {
        pageForm = pageFormValue;
    }

    /**
     * Overrides the superclass' method to copy tag properties defined by this
     * class from the specified proxy element to this real element
     * 
     * @param source the {@code HtmlPageElement} proxy element from which to
     *        copy the required properties; should be an instance of
     *        SelfFormHtmlElement
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);

        FormHtmlElement e = (FormHtmlElement) source;

        setAction(e.getAction());
        setAccept(e.getAccept());
        setEnctype(e.getEnctype());
        setPageForm(e.isPageForm());
        setMethod(e.getMethod());
    }

    /**
     * Overrides the superclass method to provide the rendering-phase behavior
     * of this tag handler before the body evaluation, which is to output the
     * appropriately configured &lt;form&gt; start tag
     * 
     * @param out the {@code JspWriter} to which to write the generated HTML
     * @throws IOException if one is generated while writing to {@code out}
     * @throws JspException if any other unresolvable problem is encountered
     *         while processing the corresponding tag
     * @return the value returned by the superclass, typically
     *         {@code Tag.EVAL_BODY_INCLUDE}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        HttpServletRequest request
                = (HttpServletRequest) pageContext.getRequest();

        out.print("<form id=\"");
        out.print(getId());
        out.print("\" method=\"");
        out.print(method.toLowerCase());
        out.print("\" action=\"");
        out.print(HtmlControl.escapeAttributeValue(this.action == null
                ? request.getRequestURL().toString()
                : (request.getContextPath() + this.action)));
        out.print("\" accept-charset=\"");
        out.print("UTF-8");
        if (enctype != null) {
            out.print("\" enctype=\"");
            out.print(HtmlControl.escapeAttributeValue(enctype));
        }
        if (accept != null) {
            out.print("\" accept=\"");
            out.print(HtmlControl.escapeAttributeValue(accept));
        }
        out.print("\">");

        if (isPageForm()) {
            out.print(getPage().getFormContent());
        }

        return rc;
    }

    /**
     * Overrides the superclass method to provide the rendering-phase behavior
     * of this tag handler after the body evaluation, which is to output the
     * appropriately configured &lt;/form&gt; end tag
     * 
     * @param out the {@code JspWriter} to which to write the generated HTML
     * @throws IOException if one is generated while writing to {@code out}
     * @throws JspException if any other unresolvable problem is encountered
     *         while processing the corresponding tag
     * @return the value returned by the superclass, typically
     *         {@code Tag.EVAL_PAGE}
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.print("</form>");
        return super.onRenderingPhaseAfterBody(out);
        
    }
}
