/*
 * Reciprocal Net project
 * 
 * QuickSearchByLabParam.java
 * 
 * 24-Sep-2004: midurbin wrote first draft
 * 22-Jun-2005: midurbin renamed this class and made it more general-purpose
 * 30-Mar-2006: jobollin fixed bug #1771 by overriding
 *              copyTransientPropertiesFrom()
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LinkParam;

/**
 * This is a control that adds a parameter of the specified name, with the value
 * of the labId for the lab described by the {@code LabContext} in which this
 * tag is nested. This parameter should be named to be recognized by the page to
 * which the link points. This tag must be nested within the
 * {@code LinkHtmlElement} representing the link whose URL will have this
 * parameter added.
 */
public class LabParam extends LinkParam {

    /**
     * The most immediate {@code LabContext} in which this tag is nested. This
     * reference is acquired by {@code onRegistrationPhaseBeforeBody()} and used
     * by {@code onFetchingPhaseAfterBody()} to set the superclass' 'value'
     * attribute.
     */
    private LabContext labContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.labContext = null;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets a
     * reference to the {@code LabContext} in which this tag is nested or throws
     * an exception after delegating back to the superclass' implementation.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code LabContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation sets the
     * 'value' attributes on the superclass before delegating back to its
     * implementation.
     * 
     * @throws NullPointerException if the {@code LabContext} returns null
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        setValue(String.valueOf(this.labContext.getLabInfo().id));
        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        LabParam dc = (LabParam) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);

        return dc;
    }

    /**
     * {@inheritDoc}. This version preserves the current 'value' (which is
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
