/*
 * Reciprocal Net project
 * 
 * CitationSelector.java
 *
 * 30-Sep-2004: midurbin wrote first draft
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 13-Jan-2005: midubin modified onFetchingPhaseBeforeBody() and
 *              onProcessingPhaseAfterBody() to handle the
 *              originalSampleHistoryId field in the annotation
 * 13-Jun-2006: jobollin reformatted the source 
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A custom tag that provides a {@code CITATION_OF_A_PUBLICATION} annotation
 * through a {@code SampleTextContext} implementation. This tag must be nested
 * with in a {@code WapPage} tag to determine whether this page is in
 * 'correction mode'. If so, this {@code SampleTextContext} provides the
 * citation that was created during the correction history id provided by the
 * {@code WapPage} method {@code getCorrectionHistoryId()}. Otherwise it
 * provides a new citation for addition to the sample provided by the
 * {@code SampleContext} implementation of the {@code WapPage}.
 */
public class CitationSelector extends HtmlPageElement implements
        SampleTextContext {

    /**
     * A reference to the {@code WapPage} tag in which this tag is nested. Set
     * by {@code onRegistrationPhaseBeforeBody()} and used to determine whether
     * this page is in 'correction mode' and to get the {@code SampleInfo} to
     * which a citation will be added or corrected.
     */
    private WapPage wapPage;

    /**
     * A reference to the citation that is provided by this
     * {@code SampleTextContext} implementation. This value is initialized to
     * null, and set by {@code onFetchingPhaseBeforeBody()}.
     */
    private SampleAnnotationInfo citation;

    /**
     * A reference to the existing citation that will be modified or deleted.
     * This value is initialized to null, and set by
     * {@code onFetchingPhaseBeforeBody()} for later comparison by
     * {@code onProcessingPhaseAfterBody()}.
     */
    private SampleAnnotationInfo existingCitation;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.wapPage = null;
        this.citation = null;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the {@code WapPage}
     * in which this tag is nested.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code WapPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get WapPage
        try {
            this.wapPage = (WapPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets {@code citation} to either an existing
     * citation on the {@code SampleInfo} provided by the {@code WapPage} or to
     * a new one.
     * 
     * @throws IllegalArgumentException if the correction history id returned by
     *         the method {@code WapPage.getCorrectionHistoryId()} indicates a
     *         sample history id at which no citation was created
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        int correctionHistoryId = this.wapPage.getCorrectionHistoryId();

        if (correctionHistoryId
                == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
            // not correction mode, use a new citation
            this.citation = new SampleAnnotationInfo(
                    SampleTextBL.CITATION_OF_A_PUBLICATION, null);
        } else {
            // correction mode, determine which citation should be corrected
            SampleInfo sampleInfo = this.wapPage.getSampleInfo();

            for (SampleAnnotationInfo annotation : sampleInfo.annotationInfo) {
                if ((annotation.type == SampleTextBL.CITATION_OF_A_PUBLICATION)
                        && (annotation.originalSampleHistoryId
                                == correctionHistoryId)) {
                    this.existingCitation = annotation;
                    this.citation = new SampleAnnotationInfo(annotation.type,
                            annotation.value);
                    break;
                }
            }
            if (this.citation == null) {
                // there was no citation created at the given historyId
                throw new IllegalStateException();
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version removes the citation if its value is
     * {@code null} or an empty string, and adds it if it is a new citation.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        SampleInfo sampleInfo = this.wapPage.getSampleInfo();
        
        if (this.citation.value == null) {
            // remove the old citation if it existed
            if (this.existingCitation != null) {
                sampleInfo.annotationInfo.remove(this.existingCitation);
            }
        } else if (this.existingCitation == null) {
            // add the citation if it is new
            sampleInfo.annotationInfo.add(this.citation);
        } else if (!this.existingCitation.value.equals(this.citation.value)) {
            // replace the existing citation that was modified
            sampleInfo.annotationInfo.remove(this.existingCitation);
            sampleInfo.annotationInfo.add(this.citation);
        } else {
            /*
             * the citation's value was unchanged so no modification to the
             * annotation on the sample needs to be made
             */
        }
        
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * Implements {@code SampleTextContext}; the current implementation returns
     * a {@code CITATION_OF_A_PUBLICATION} annotation or thows an exception if
     * none is available at this time. This method should not be called before
     * the {@code FETCHING_PHASE}.
     * 
     * @return a {@code SampleTextInfo} that represents a citation
     * @throws IllegalStateException if no citation is avialable
     */
    public SampleTextInfo getSampleTextInfo() {
        if (this.citation == null) {
            throw new IllegalStateException();
        }
        return this.citation;
    }

    /**
     * Implements {@code SampleTextContext}; the current implementation always
     * returns {@code SampleTextInfo.CITATION_OF_A_PUBLICATION}.
     */
    public int getTextType() {
        return SampleTextBL.CITATION_OF_A_PUBLICATION;
    }
}
