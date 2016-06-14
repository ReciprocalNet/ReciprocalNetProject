/*
 * Reciprocal Net project
 * 
 * SampleHistoryParam.java
 * 
 * 05-Jul-2005: midurbin wrote first draft
 * 23-Sep-2005: midurbin updated onFetchingPhaseAfterBody() to prevent a
 *              NullPointerException from being thrown when the SampleContext
 *              and SampleHistoryContext provide null values
 * 30-Mar-2006: jobollin fixed bug #1771 by overriding
 *              copyTransientPropertiesFrom()
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LinkParam;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * This is a control that adds a parameter of the specified name, with the value
 * of the sampleHistoryId provided by either a surrounding
 * {@code SampleHistoryContext} or {@code SampleContext} (with that order of
 * preference). This tag must be nested within the {@code LinkHtmlElement}
 * representing the link whose URL will have this parameter added.
 */
public class SampleHistoryParam extends LinkParam {

    /**
     * The most immediate {@code SampleContext} in which this tag is nested.
     * This reference is acquired by {@code onRegistrationPhaseBeforeBody()} and
     * used by {@code onFetchingPhaseAfterBody()} to set the superclass' 'value'
     * property.
     */
    private SampleContext sampleContext;

    /**
     * The most immediate {@code SampleHistoryContext} in which this tag is
     * nested. This reference is acquired by
     * {@code onRegistrationPhaseBeforeBody()} and used by
     * {@code onFetchingPhaseAfterBody()} to set the superclass' 'value'
     * property.
     */
    private SampleHistoryContext sampleHistoryContext;

    /**
     * An optional property that if set indicates that this parameter should
     * have a value equal to the sample's most recent history id rather than the
     * history id available from the {@code SampleHistoryContext}.
     */
    private boolean useMostRecentHistoryId;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.sampleHistoryContext = null;
        this.useMostRecentHistoryId = false;
    }

    /** Sets the 'useMostRecentHistoryId' property. */
    public void setUseMostRecentHistoryId(boolean use) {
        this.useMostRecentHistoryId = use;
    }

    /** Gets the 'useMostRecentHistoryId' property. */
    public boolean isUseMostRecentHistoryId() {
        return this.useMostRecentHistoryId;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the
     * {@code SampleHistoryContext}, or, failing that, the
     * {@code SampleContext} in which this tag is nested or throws an exception
     * after delegating back to the superclass implementation.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext} or {@code SampleHistoryContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleHistoryContext = findRealAncestorWithClass(this,
                SampleHistoryContext.class);
        if (this.sampleHistoryContext == null) {
            this.sampleContext = findRealAncestorWithClass(this,
                    SampleContext.class);
            if (this.sampleContext == null) {
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets the 'value' attributes on the
     * superclass before delegating back to its implementation.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        SampleInfo si = (this.sampleHistoryContext != null
                ? this.sampleHistoryContext.getSampleHistoryInfo().sample
                : this.sampleContext.getSampleInfo());

        if (si != null) {
            this.setValue(String.valueOf((this.useMostRecentHistoryId
                    ? si.mostRecentHistoryId
                    : si.historyId)));
        }

        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleHistoryParam dc
                = (SampleHistoryParam) super.generateCopy(newId, map);

        dc.sampleHistoryContext
                = (SampleHistoryContext) map.get(this.sampleHistoryContext);
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
