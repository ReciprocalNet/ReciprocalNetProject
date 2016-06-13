/*
 * Reciprocal Net project
 * 
 * PreferredFormulaChecker.java
 *
 * 26-Apr-2004: midurbin wrote first draft
 * 13-Jun-2006: jobollin made this class extend AbstractChecker and reformatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A tag that evaluates its body when the sample provided by the surrounding
 * {@code SampleContext} has a 'preferred formula' (as determined by a call to
 * {@link org.recipnet.site.shared.bl.SampleTextBL#getSamplePreferredFormula
 * SampleTextBL.getSamplePreferredFormula() }). If no 'preferred formula' is
 * available on the sample, this tag suppresses its body. Besides this typical
 * 'checker' functionality, this tag implements {@code SampleTextContext} to
 * provide the {@code SampleTextInfo} that was determined to be the preferred
 * formula. Nested tags such as {@code SampleField} may then be used to expose
 * this preferred formula which may be of any number of text types.
 */
public class PreferredFormulaChecker extends AbstractChecker implements
        SampleTextContext, SuppressionContext {

    /**
     * A reference to the {@code SampleContext} in which this tag is nested.
     * This is set by {@code onRegistrationPhaseBeforeBody()} and provides the
     * sample from which the preferred formula is extracted.
     */
    private SampleContext sampleContext;

    /**
     * A {@code SampleTextInfo} from the {@code SampleInfo} supplied by the
     * {@code SampleContext} that represents the preferred formula or null if no
     * formulae exist for the sample or if the sample doesn't exist.
     */
    private SampleTextInfo preferredFormula;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.preferredFormula = null;
    }

    /**
     * {@inheritDoc}; this version delegates to the superclass's version, then
     * looks up a reference to the {@code SampleContext}
     * 
     * @throws JspException if no {@code SampleContext} surrounds this tag
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
     * {@inheritDoc}; this version gets the
     * {@code SampleInfo} from the {@code SampleContext}, determines the
     * preferred formula, and determines whether this tags body should continue
     * to be suppressed
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo si = this.sampleContext.getSampleInfo();
        
        if (si != null) {
            this.preferredFormula = SampleTextBL.getSamplePreferredFormula(si);
            inclusionConditionMet = (this.preferredFormula != null);
        } // else this tag does nothing, and its body remains suppressed
        
        return rc;
    }

    /**
     * Implements {@code SampleTextContext}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     * @return the 'preferred' formula, or null if none exist or if nested in a
     *         null SampleTextContext
     */
    public SampleTextInfo getSampleTextInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.preferredFormula;
    }

    /**
     * Implements {@code SampleTextContext}.
     * 
     * @return the text type of the {@code SampleTextInfo} returned by
     *         {@code getSampleTextInfo()} or {@code SampleTextBL.INVALID_TYPE}
     *         if null is returned
     */
    public int getTextType() {
        if (this.preferredFormula == null) {
            return SampleTextBL.INVALID_TYPE;
        }
        return this.preferredFormula.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        PreferredFormulaChecker dc
                = (PreferredFormulaChecker) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        
        return dc;
    }
}
