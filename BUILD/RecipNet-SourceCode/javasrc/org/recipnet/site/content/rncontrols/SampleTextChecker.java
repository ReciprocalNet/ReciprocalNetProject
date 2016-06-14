/*
 * Reciprocal Net project
 * 
 * SampleTextChecker.java
 *
 * 23-Sep-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A 'checker' tag that evaluates its body based on characteristics of the
 * {@code SampleTextInfo} recieved from a surrounding {@code SampleTextContext}.
 */
public class SampleTextChecker extends AbstractChecker {

    /** A reference to the surrounding {@code SampleTextContext}. */
    private SampleTextContext sampleTextContext;

    /**
     * An optional property that indicates what criteria in the
     * {@code SampleTextInfo} suppression vs. inclusion should be based upon.
     */
    private boolean includeOnlyForAnnotationsWithReferences;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleTextContext = null;
        this.includeOnlyForAnnotationsWithReferences = false;
    }

    /** Setter for the 'includeOnlyForAnnotationsWithReferences' property. */
    public void setIncludeOnlyForAnnotationsWithReferences(boolean include) {
        this.includeOnlyForAnnotationsWithReferences = include;
    }

    /** Getter for the 'includeOnlyForAnnotationsWithReferences' property. */
    public boolean getIncludeOnlyForAnnotationsWithReferences() {
        return this.includeOnlyForAnnotationsWithReferences;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost surrounding
     * {@code SampleTextContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleTextContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleTextContext
                = findRealAncestorWithClass(this, SampleTextContext.class);
        if (this.sampleTextContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the {@code SampleTextInfo} from the
     * {@code SampleTextContext} and determines whether to suppress or include
     * the body of this tag.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleTextInfo sti = this.sampleTextContext.getSampleTextInfo();

        if (this.includeOnlyForAnnotationsWithReferences) {
            super.inclusionConditionMet = ((sti instanceof SampleAnnotationInfo)
                    && (((SampleAnnotationInfo) sti).referenceSample
                            != SampleInfo.INVALID_SAMPLE_ID));
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleTextChecker dc
                = (SampleTextChecker) super.generateCopy(newId, map);

        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);

        return dc;
    }
}
