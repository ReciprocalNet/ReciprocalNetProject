/*
 * Reciprocal Net project
 * 
 * SampleContextTranslator.java
 * 
 * 23-Sep-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that translates some context into a {@code SampleContext}.
 * The following translation modes are currently available:
 * <dl>
 * <dt>{@code translateFromAnnotationReferenceSample}</dt>
 * <dd>a mode that provides the {@code SampleInfo} for the sample
 * referenced by the 'referenceSample' on the {@code SampleAnnotationInfo}
 * provided by the {@code SampleTextContext} surrounding this tag if it
 * is in fact an annotation with a valid reference sample. Otherwise the null
 * context is provided.</dd>
 * </dl>
 */
public class SampleContextTranslator extends HtmlPageElement
        implements SampleContext {

    /**
     * A reference to the surrounding {@code SampleTextContext}.  This is
     * resolved by {@code onRegistrationPhaseBeforeBody()} if needed by
     * the selected translation mode.
     */
    private SampleTextContext sampleTextContext;

    /**
     * An optional property that indicates a possible translation mode that is
     * meant to translate the surrounding {@code SampleTextContext}'s 
     * 'referenceSample' (if one exists) into the corresponding
     * {@code SampleInfo}.
     */
    private boolean translateFromAnnotationReferenceSample;

    /**
     * The {@code SampleInfo} provided by this {@code SampleContext}
     * implementation.
     */
    private SampleInfo sampleInfo;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleTextContext = null;
        this.translateFromAnnotationReferenceSample = false;
        this.sampleInfo = null;
    }

    /** Setter for the 'translateFromAnnotationReferenceSample' property. */
    public void setTranslateFromAnnotationReferenceSample(boolean translate) {
        this.translateFromAnnotationReferenceSample = translate;
    }

    /** Getter for the 'translateFromAnnotationReferenceSample' property. */
    public boolean getTranslateFromAnnotationReferenceSample() {
        return this.translateFromAnnotationReferenceSample;
    }

    /**
     * Implements {@code SampleContext}.
     * @throws IllegalStateException if called before the
     *     {@code FETCHING_PHASE}.
     */
    public SampleInfo getSampleInfo() {
        if ((this.getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (this.getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.sampleInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        if (translateFromAnnotationReferenceSample) {
            this.sampleTextContext
                    = findRealAncestorWithClass(this, SampleTextContext.class);
            if (this.sampleTextContext == null) {
                throw new IllegalStateException();
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version prepares the {@code SampleInfo} for this
     * {@code SampleContext} implementation based on the configured translation
     * mode and host context
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (this.translateFromAnnotationReferenceSample) {
            SampleTextInfo sti = this.sampleTextContext.getSampleTextInfo();
            
            if ((sti == null) || !(sti instanceof SampleAnnotationInfo)
                    || (((SampleAnnotationInfo) sti).referenceSample
                            == SampleInfo.INVALID_SAMPLE_ID)) {
                // no valid translation to perform, return early leaving the
                // SampleInfo as null
                return rc;
            }
            
            SampleAnnotationInfo sai = (SampleAnnotationInfo) sti;
            
            this.sampleInfo = RequestCache.getSampleInfo(
                    pageContext.getRequest(), sai.referenceSample,
                    SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
            if (this.sampleInfo == null) {
                CoreConnector cc = CoreConnector.extract(
                        pageContext.getServletContext());
                try {
                    this.sampleInfo = cc.getSampleManager().getSampleInfo(
                            sai.referenceSample);
                    RequestCache.putSampleInfo(pageContext.getRequest(),
                                               this.sampleInfo);
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                }
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleContextTranslator dc
                = (SampleContextTranslator) super.generateCopy(newId, map);
        
        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);
        
        return dc;
    }
}
