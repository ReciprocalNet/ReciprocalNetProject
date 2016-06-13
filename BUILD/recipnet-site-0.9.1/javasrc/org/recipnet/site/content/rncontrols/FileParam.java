/*
 * Reciprocal Net project
 * 
 * FileParam.java
 * 
 * 04-Aug-2005: midurbin wrote first draft
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
 * This is a control that adds a parameter of the specified name, with the
 * value of the filename for the file described by the {@code FileContext}
 * in which this tag is nested.  This parameter should be named to be
 * recognized by the page to which the link points.  This tag must be nested
 * within the {@code LinkHtmlElement} representing the link whose URL wil
 * have this parameter added.  
 */
public class FileParam extends LinkParam {
    
    /**
     * The most immediate {@code FileContext} in which this tag is nested.
     * This reference is acquired by
     * {@code onRegistrationPhaseBeforeBody()} and used by
     * {@code onFetchingPhaseAfterBody()} to set the superclass' 'value'
     * attribute.
     */
    private FileContext fileContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileContext = null;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the {@code FileContext}
     * in which this tag is nested or throws an exception after delegating back
     * to the superclass' implementation.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code FileContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version sets the 'value' attributes on the
     * superclass before delegating back to its implementation.
     * 
     * @throws NullPointerException if the {@code FileContext} returns null
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        this.setValue(String.valueOf(
                this.fileContext.getSampleDataFile().getName()));
        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileParam dc = (FileParam) super.generateCopy(newId, map);
        
        dc.fileContext = (FileContext) map.get(this.fileContext);
        
        return dc;
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
