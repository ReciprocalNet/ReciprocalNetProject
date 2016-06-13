/*
 * Reciprocal Net project
 * 
 * ImageHtmlElement.java
 * 
 * 20-Oct-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 15-Aug-2005: midurbin updated onRenderingPhaseAfterBody() to reflect changes
 *              in ImageHtmlElement
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.ImageHtmlElement;
import org.recipnet.site.shared.db.SampleHistoryInfo;

/**
 * <p>
 * A custom tag that outputs HTML to display the image icon associated with the
 * action associated with the {@code SampleHistoryInfo} provided by the
 * {@code SampleHistoryContext} that most tightly encloses this tag.
 * </p><p>
 * The current implementation expects that image files will have paths of the
 * following format: [context path]/images/action[action code].gif where
 * [context path] is the context path and [action code] is the action code as
 * defined on {@code SampleWorkflowBl}.
 * </p><p>
 * The {@code SampleHistoryContext} in which this tag must be nested must return
 * a non-null {@code SampleHistoryInfo} from a call to
 * {@code getSampleHistoryInfo()} during the {@code RENDERING_PHASE}.
 * </p>
 */
public class ActionIcon extends ImageHtmlElement {

    /**
     * The most immediate {@code SampleHistoryContext} that encloses this tag.
     * The method {@code onRegistrationPhaseBeforeBody()} sets this reference
     * and {@code onFetchingPhaseBeforeBody()} uses it to set superclass' 'src'
     * parameter.
     */
    private SampleHistoryContext sampleHistoryContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleHistoryContext = null;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation finds the
     * {@code SampleHistoryContext} that encloses this tag.
     * 
     * @throws IllegalStateException if no {@code SampleHistoryContext} is found
     *         to enclose this tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find the SampleHistoryContext
        this.sampleHistoryContext
                = findRealAncestorWithClass(this, SampleHistoryContext.class);
        if (this.sampleHistoryContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation sets the
     * 'src' and 'alt' property on the superclass according to the action
     * associated with the {@code SampleHistoryContext}.
     * 
     * @throws IllegalStateException if the {@code SampleHistoryContext} is a
     *         null context.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        SampleHistoryInfo shi = this.sampleHistoryContext.getSampleHistoryInfo();
        
        if (shi == null) {
            throw new IllegalStateException();
        }

        setSrc("/images/action" + shi.action + ".gif");
        setAlt(String.valueOf(shi.action));
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ActionIcon dc = (ActionIcon) super.generateCopy(newId, map);
        
        dc.sampleHistoryContext
                = (SampleHistoryContext) map.get(this.sampleHistoryContext);
        
        return dc;
    }
}
