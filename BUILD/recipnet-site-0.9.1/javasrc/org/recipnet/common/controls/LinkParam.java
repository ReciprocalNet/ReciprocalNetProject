/*
 * Reciprocal Net Project
 *
 * LinkParam.java
 *
 * 24-Sep-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 23-Sep-2005: midurbin updated onFetchingPhaseAfterBody() to only add the
 *              parameter if its name and value were both non-null
 * 16-Mar-2006: jobollin performed a complete rewrite
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * A custom tag handler that allows JSP authors to specify query parameters to
 * HTML link elements. Users provide the parameter name and value; the innermost
 * surrounding {@code LinkHtmlElement} is updated during the
 * {@code FETCHING_PHASE} and again during the {@code PROCESSING_PHASE} via its
 * {@link LinkHtmlElement#addParameter(String, String)} method if both name and
 * value are non-{@code null} and the name is nonempty. Users do not need to
 * (and shouldn't) escape the parameter name or value because
 * {@code LinkHtmlElement} will handle that automatically.
 * 
 * @author jobollin
 * @version 1.0
 */
public class LinkParam extends HtmlPageElement {

    /**
     * A transient tag property specifying the name of the query parameter to
     * add
     */
    private String name;

    /**
     * A transient tag property specifying the value of the query parameter to
     * add
     */
    private String value;

    /**
     * The innermost {@code LinkHtmlElement} surrounding this tag
     */
    private LinkHtmlElement linkElement;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        name = null;
        value = null;
        linkElement = null;
    }

    /**
     * Returns the value of this tag handler's {@code name} property
     * 
     * @return the {@code name} {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of this tag handler's {@code name} property
     * 
     * @param name the {@code String} to set as the {@code name}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of this tag handler's {@code value} property
     * 
     * @return the {@code value} {@code String}
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of this tag handler's {@code value} property
     * 
     * @param value the {@code String} to set as the {@code value}
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}. This version finds the innermost surrounding
     * {@code LinkHtmlElement}, and updates it with this tag's name and value
     * if they are non-{@code null} and the name is non-empty
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        linkElement = findRealAncestorWithClass(this, LinkHtmlElement.class);
        if (linkElement == null) {
            throw new JspException("Parameter without a link");
        }

        return rc;
    }

    /**
     * {@inheritDoc}. This version updates the innermost surrounding
     * {@code LinkHtmlElement} with this tag's name and value if they are
     * non-{@code null} and the name is non-empty
     * 
     * @see HtmlPageElement#onFetchingPhaseAfterBody()
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        addLinkParameter();

        return super.onFetchingPhaseAfterBody();
    }

    /**
     * {@inheritDoc}. This version updates the innermost surrounding
     * {@code LinkHtmlElement} with this tag's name and value if they are
     * non-{@code null} and the name is non-empty
     * 
     * @see HtmlPageElement#onProcessingPhaseAfterBody(PageContext)
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        addLinkParameter();

        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * Uses the {@code name} and {@code value} properties of this tag to set a
     * query parameter on the surrounding {@code LinkHtmlElement}, provided
     * that the name is neither {@code null} nor {@code empty} and the value is
     * not {@code null}
     */
    private void addLinkParameter() {
        String paramName = getName();
        String paramValue = getValue();

        if ((paramName != null) && (paramName.length() > 0)
                && (paramValue != null)) {
            linkElement.addParameter(paramName, paramValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        LinkParam copy = (LinkParam)
                super.generateCopy(newId, origToCopyMap);

        copy.linkElement
                = (LinkHtmlElement) origToCopyMap.get(this.linkElement);

        return copy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        LinkParam sourceElement = (LinkParam) source;

        super.copyTransientPropertiesFrom(source);
        setName(sourceElement.getName());
        setValue(sourceElement.getValue());
    }
}
