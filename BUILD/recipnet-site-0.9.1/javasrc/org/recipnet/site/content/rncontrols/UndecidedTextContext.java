/*
 * Reciprocal Net project
 * UndecidedTextContext.java
 *
 * 15-Oct-2004: midurbin wrote first draft
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 28-Oct-2005: jobollin updated onProcessingPhaseAfterBody() to account for
 *              SampleInfo's change to using appropriately-typed Lists for
 *              attributes and annotations (instead of raw Lists)
 * 17-Mar-2006: jobollin formatted the source and removed unused imports 
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A {@code SampleTextContext} implementation that is designed to work with
 * {@code TextTypeSelector} to allow the creation of new annotations/ attributes
 * on a {@code SampleInfo}. This tag gets a text type from the
 * {@code TextTypeSelector} supplied as the required 'textTypeSelector' property
 * and creates a new {@code SampleTextInfo} of the given type and provides it to
 * a a nested {@code SampleField} or other {@code SampleTextContext}-recognizing
 * tag.
 */
public class UndecidedTextContext extends HtmlPageElement implements
        SampleTextContext {

    /**
     * The {@code SampleTextInfo} provided by this context. This object is
     * created by {@code onFetchingPhaseBeforeBody()} of the type determined by
     * a call to {@code textTypeSelector.getTextType()} and provided through the
     * {@code SampleTextContext} implementation method
     * {@code getSampleTextInfo()}. If modified, it is added to the
     * {@code SampleInfo} of the most immedite {@code SampleContext} by
     * {@code onProcessingPhaseAfterBody()}.
     */
    private SampleTextInfo sampleTextInfo;

    /**
     * A reference to the most immediate {@code SampleContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * A required property used to determine the text type of the attribute or
     * annotation provided by this {@code SampleTextContext} implementation.
     */
    private TextTypeSelector textTypeSelector;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleTextInfo = null;
        this.sampleContext = null;
        this.textTypeSelector = null;
    }

    /**
     * @param selector a {@code TextTypeSelector} that will be used to determine
     *        the text type of the attribute/annotation provided by this
     *        {@code SampleTextContext}
     */
    public void setTextTypeSelector(TextTypeSelector selector) {
        this.textTypeSelector = selector;
    }

    /**
     * @return a {@code TextTypeSelector} that will be used to determine the
     *         text type of the attribute/annotation provided by this
     *         {@code SampleTextContext}
     */
    public TextTypeSelector getTextTypeSelector() {
        return this.textTypeSelector;
    }

    /**
     * {@inheritDoc}; this version determines the most immediate
     * {@code SampleContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version creates an annotation/attribute that will be
     * returned by {@code getSampleTextInfo()}.
     * 
     * @throws IllegalStateException if {@code textTypeSelector} returns an
     *         unknown text type
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        // Resolve the SampleTextInfo represented by this context, if any
        if (this.sampleContext.getSampleInfo() == null) {
            this.sampleTextInfo = null;
        } else {
            int textType = this.textTypeSelector.getTextType();

            if (SampleTextBL.isAttribute(textType)) {
                this.sampleTextInfo = new SampleAttributeInfo(textType, null);
            } else if (SampleTextBL.isAnnotation(textType)) {
                this.sampleTextInfo = new SampleAnnotationInfo(textType, null);
            } else if (textType == SampleTextInfo.INVALID_TYPE) {
                /*
                 * In some cases TextTypeSelector will return INVALID_TYPE
                 * indicating that no type has been selected, for example during
                 * an HTTP GET. In these cases, this context should be the null
                 * context.
                 */
                this.sampleTextInfo = null;
            } else {
                // the supplied text type was not valid
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version stores the {@code sampleTextInfo} in the
     * {@code SampleInfo} provided by the {@code SampleContext}, provided a
     * value was entered for it.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if ((this.sampleTextInfo != null) && (this.sampleTextInfo.value != null)) {
            SampleInfo si = this.sampleContext.getSampleInfo();

            assert si != null;

            if (SampleTextBL.isAnnotation(this.sampleTextInfo.type)) {
                si.annotationInfo.add(
                        (SampleAnnotationInfo) this.sampleTextInfo);
            } else if (SampleTextBL.isAttribute(this.sampleTextInfo.type)) {
                si.attributeInfo.add((SampleAttributeInfo) this.sampleTextInfo);
            }
        }

        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * Implements {@code SampleTextContext}.
     * 
     * @return a {@code SampleTextInfo} of the type provided by
     *         {@code TextTypeSelector}
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public SampleTextInfo getSampleTextInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }

        return this.sampleTextInfo;
    }

    /**
     * This method returns {@code SampleTextInfo.INVALID_TYPE} until the
     * {@code FETCHING_PHASE} after which point it returns the text type of the
     * {@code SampleTextInfo} object returned by {@code getSampleTextInfo()},
     * or if there is no {@code SampleTextInfo} at that point then it continues
     * to return {@code SampleTextInfo.INVALID_TYPE}.
     * 
     * @return the text type code for the {@code SampleTextInfo} provided by
     *         this context
     */
    public int getTextType() {
        return ((this.sampleTextInfo == null) ? SampleTextInfo.INVALID_TYPE
                : this.sampleTextInfo.type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UndecidedTextContext dc
                = (UndecidedTextContext) super.generateCopy(newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);

        return dc;
    }
}
