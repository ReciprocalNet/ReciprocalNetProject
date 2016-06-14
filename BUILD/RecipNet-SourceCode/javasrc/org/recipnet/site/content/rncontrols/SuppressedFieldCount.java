/*
 * Reciprocal Net project
 * 
 * SuppressedFieldCount.java
 * 
 * 24-Jun-2005: midurbin wrote the first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.recipnet.common.controls.HtmlPageElement;

/**
 * A simple class that displays the number of fields suppressed by a
 * {@code SampleActionFieldIterator} that is provided to this tag as a required
 * attribute.
 */
public class SuppressedFieldCount extends HtmlPageElement {

    /**
     * A required property indicating the {@code SampleActionFieldIterator}
     * whose suppressed field count is to be displayed by this tag.
     */
    private SampleActionFieldIterator fieldIterator;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldIterator = null;
    }

    /** Sets the 'fieldIterator' property. */
    public void setFieldIterator(SampleActionFieldIterator it) {
        this.fieldIterator = it;
    }

    /** Gets the 'fieldIterator' property. */
    public SampleActionFieldIterator getFieldIterator() {
        return this.fieldIterator;
    }

    /**
     * {@inheritDoc}; this version outputs the number of fields the
     * {@code SampleActionFieldIterator} suppressed.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.print(this.fieldIterator.getSuppressedFieldCount());

        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}; this version implements transiency for the
     * 'fieldIterator' attribute
     */
    @Override
    public void copyTransientPropertiesFrom(HtmlPageElement source) {
        // the fieldIterator isn't strictly transient
        this.fieldIterator = ((SuppressedFieldCount) source).fieldIterator;
    }
}
