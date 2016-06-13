/*
 * Reciprocal Net Project
 *
 * SampleUpdateBlocker.java
 *
 * 23-Mar-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A special-purpose SampleContext implementation that stands between a
 * surrounding SampleContext and other tags; when enabled, this context prevents
 * the surrounding context from being updated by tags inside this context.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SampleUpdateBlocker extends HtmlPageElement
        implements SampleContext {

    /**
     * An optional, transient property specifying whether this context should
     * block updates to the surrounding sample context; defaults to
     * {@code false}
     */
    private boolean enabled;
    
    /**
     * The innermost surrounding {@code SampleContext}
     */
    private SampleContext hostContext;
    
    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        enabled = false;
        hostContext = null;
    }

    /**
     * Returns the value of this tag handler's 'enabled' property; when enabled,
     * this context prevents tags nested within it from updating the surrounding
     * sample context by passing a clone of that context's {@code SampleInfo}
     * in place of the original
     * 
     * @return {@code true} if this context is enabled to block sample context
     *         updates, {@code false} if not
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets this tag handler's 'enabled' property; when enabled,
     * this context prevents tags nested within it from updating the surrounding
     * sample context by passing a clone of that context's {@code SampleInfo}
     * in place of the original
     * 
     * @param  enabled {@code true} to enable this context's update blocking
     *         behavior, {@code false} to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * {@inheritDoc}.  This version obtains a reference to the innermost
     * containing {@code SampleContext}; it is an error if no such context
     * exists
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        hostContext = findRealAncestorWithClass(this, SampleContext.class);
        if (hostContext == null) {
            throw new IllegalStateException("Not host sample context");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see SampleContext#getSampleInfo()
     */
    public SampleInfo getSampleInfo() {
        SampleInfo info = hostContext.getSampleInfo();
        
        return (enabled && (info != null)) ? info.clone() : info;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        SampleUpdateBlocker sub = (SampleUpdateBlocker) source;
        
        super.copyTransientPropertiesFrom(source);
        this.setEnabled(sub.isEnabled());
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(java.lang.String, java.util.Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        SampleUpdateBlocker copy
               = (SampleUpdateBlocker) super.generateCopy(newId, origToCopyMap);
        
        copy.hostContext = (SampleContext) origToCopyMap.get(this.hostContext);
        
        return copy;
    }

}
