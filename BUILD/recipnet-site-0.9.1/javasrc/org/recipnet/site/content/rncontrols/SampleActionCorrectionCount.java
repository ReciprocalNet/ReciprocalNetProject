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
 * A simple class that displays the number of times the action described by the
 * current iteration of the surrounding {@code SampleActionIterator} was
 * corrected.
 */
public class SampleActionCorrectionCount extends HtmlPageElement {

    /**
     * {@inheritDoc}; this version outputs the correction count from the
     * surrounding {@code SampleActionIterator}.
     * 
     * @throws NullPointerException if this tag is not nested in a
     *         {@code SampleActionIterator}
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.println(findRealAncestorWithClass(this,
                SampleActionIterator.class).getCurrentActionCorrectionCount());
        
        return super.onRenderingPhaseAfterBody(out);
    }
}
