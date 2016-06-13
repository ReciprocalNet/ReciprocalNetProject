/*
 * Reciprocal Net project
 * 
 * SampleParam.java
 * 
 * 05-Jul-2005: midurbin wrote first draft
 * 23-Sep-2005: midurbin updated onFetchingPhaseAfterBody() to prevent a
 *              NullPointerException from being thrown when the SampleContext
 *              provides null
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
 * of the sampleId for the sample described by the {@code SampleContext} in
 * which this tag is nested. This parameter should be named to be recognized by
 * the page to which the link points. This tag must be nested within the
 * {@code LinkHtmlElement} representing the link whose URL will have this
 * parameter added.
 */
public class SampleParam extends LinkParam {

    /**
     * The most immediate {@code SampleContext} in which this tag is nested.
     * This reference is acquired by {@code onRegistrationPhaseBeforeBody()} and
     * used by {@code onFetchingPhaseAfterBody()} to set the superclass' 'value'
     * attribute.
     */
    private SampleContext sampleContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the
     * {@code SampleContext} in which this tag is nested or throws an exception
     * after delegating back to the superclass' implementation.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleContext = findRealAncestorWithClass(this,
                SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets the 'value' attributes on the
     * superclass before delegating back to its implementation.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if (this.sampleContext.getSampleInfo() != null) {
            this.setValue(
                    String.valueOf(this.sampleContext.getSampleInfo().id));
        }
        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleParam dc = (SampleParam) super.generateCopy(newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);

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
