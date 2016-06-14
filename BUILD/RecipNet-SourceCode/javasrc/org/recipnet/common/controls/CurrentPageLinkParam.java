/*
 * Reciprocal Net project
 * 
 * CurrentPageLinkParam.java
 * 
 * 18-May-2004: midurbin wrote first draft
 * 05-Jun-2005: midurbin updated onRegistrationPhaseBeforeBody() to recognize
 *              the 'servletPathAndQueryForReinvocation' request attribute set
 *              by RecipnetPage
 * 30-Mar-2006: jobollin fixed bug #1771 by overriding
 *              copyTransientPropertiesFrom()
 */

package org.recipnet.common.controls;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * This is a control that when nested in a {@code LinkHtmlElement} adds a URL
 * parameter to the link that has a value equal to the current page's
 * reinvocation URL (relative to the context path). The reinvocation URL is
 * determined by checking for the request attribute
 * 'servletPathAndQueryForReinvocation', or by invoking
 * {@code HtmlPage.getServletPathAndQueryForReinvocation()} if the attribute
 * does not exist. The name of the parameter added may be specified by the
 * 'name' property, but the value is automatically determined. In the event that
 * the current request contains a query parameter with the name specified for
 * this parameter, it may be used in place of the current URL.
 */
public class CurrentPageLinkParam extends LinkParam {

    /**
     * An optional property that if set indicates a regular expression that if
     * present in the generated 'value' property will be replaced by the
     * 'replacement' property value. If this property is set, 'replacement' must
     * also be set.
     */
    private String regEx;

    /**
     * An optional property that must be set if and only if the 'regEx' is also
     * set and indicates a {@code String} that will replace every instance of
     * the 'regEx' in the URL generated for the 'value' property.
     */
    private String replacement;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.regEx = null;
        this.replacement = null;
    }

    /** @param regEx the regular expression to be replaced in the 'value' */
    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    /** @return a regular expression to be replaced in the 'value' */
    public String getRegEx() {
        return this.regEx;
    }

    /** @param replacement text to replace the 'regex' in the 'value' */
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    /** @return text to replace the 'regex' in the 'value' */
    public String getReplacement() {
        return this.replacement;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation sets the
     * 'value' property to the current request parameter with the name indicated
     * by the 'name' property, or the value of {@link
     * HtmlPage#getServletPathAndQueryForReinvocation()
     * HtmlPage.getServletPathAndQueryForReinvocation()} if the parameter is
     * unavailable. If 'regEx' and 'replacement' are set, the computed value for
     * the 'value' attribute is updated by replacing all instances of the
     * 'regEx' with the 'replacement' string.
     * 
     * @throws IllegalStateException if the 'regEx' property is set, but
     *         'replacement' is not set
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        String existingParamValue
                = this.pageContext.getRequest().getParameter(getName());
        
        if (existingParamValue == null) {
            existingParamValue
                    = (String) super.pageContext.getRequest().getAttribute(
                            "servletPathAndQueryForReinvocation");
        }
        String newParamValue = ((existingParamValue == null)
                ? getPage().getServletPathAndQueryForReinvocation()
                : existingParamValue);
        
        if (this.regEx != null) {
            if (this.replacement == null) {
                throw new IllegalStateException();
            }
            newParamValue = newParamValue.replaceAll(this.regEx,
                    this.replacement);
        }
        setValue(newParamValue);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version preserves the current 'value' (which is
     * computed, not set as a tag attribute on the proxy element).
     * 
     * @see LinkParam#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        String savedValue = getValue();
        
        super.copyTransientPropertiesFrom(source);
        setValue(savedValue);
    }
}
