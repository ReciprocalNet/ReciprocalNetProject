/*
 * Reciprocal Net project
 * 
 * IterationCount.java
 * 
 * 04-Aug-2005: midurbin wrote the first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * A custom tag that outputs the result of {@link
 * HtmlPageIterator#getIterationCountSinceThisPhaseBegan()
 * HtmlPageIterator.getIterationCountSinceThisPhaseBegan()} for a specified
 * {@code HtmlPageIterator}. If this tag is nested within the iterator it will
 * output the index for the current iteration (starting at zero); if it is
 * located after the iterator it will output the iteration count.
 */
public class IterationCount extends HtmlPageElement {

    /**
     * A required property that is set to a reference of a peer or parent
     * {@code HtmlPageIterator} whose iteration count will be output by this
     * tag.
     */
    private HtmlPageIterator htmlPageIterator;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.htmlPageIterator = null;
    }

    /**
     * Sets the 'htmlPageIterator' property.
     * 
     * @param it the {@code HtmlPageIterator} whose iteration count is to be
     *        presented
     */
    public void setHtmlPageIterator(HtmlPageIterator it) {
        this.htmlPageIterator = it;
    }

    /**
     * Gets the 'htmlPageIterator' property.
     * 
     * @return the {@code HtmlPageIterator} whose iteration count is to be
     *         presented
     */
    public HtmlPageIterator getHtmlPageIterator() {
        return this.htmlPageIterator;
    }

    /**
     * {@inheritDoc}; this version writes the number of iterations evaluated
     * this phase by the 'htmlPageIterator'.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.print(htmlPageIterator.getIterationCountSinceThisPhaseBegan());
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /** {@inheritDoc} */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        IterationCount src = (IterationCount) source;
        
        super.copyTransientPropertiesFrom(source);
        setHtmlPageIterator(src.getHtmlPageIterator());
    }
}
