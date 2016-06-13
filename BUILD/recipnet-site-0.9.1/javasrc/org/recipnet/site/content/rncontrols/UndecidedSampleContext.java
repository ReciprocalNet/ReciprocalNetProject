/*
 * Reciprocal Net project
 * 
 * UndecidedSampleContext.java
 *
 * 05-Oct-2004: midurbin wrote first draft
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A tag that supplies its body a {@code SampleContext} and
 * {@code SampleTextContext} for a Sample that may be dependent upon the value
 * of another control and therefore may be undefined in some cases. Specifically
 * this control may determine the sample that will be provided by its
 * {@code SampleContext} implementation from a {@code SampleSelector} control or
 * from a surrounding {@code SampleContext} if no {@code SampleSelector} is
 * indicated. The sample provided by this {@code SampleContext} and
 * {@code SampleTextContext} is referred to as the 'subject sample' because it
 * it the sample being acted on/exposed by this control.
 * </p><p>
 * This tag also has the ability to assign a 'referenceSample' to an annotation
 * that it provides through the {@code SampleTextContext} interface. The
 * annotation type must be specified as the 'annotationType' property of this
 * tag and always represents a new annotation. If a reference is to be added a
 * 'reference sample' must be specified either by providing the
 * 'referenceSampleContext' property or the 'referenceSampleSelector' property.
 * This {@code SampleSelector} or {@code SampleContext} should most likely be a
 * peer to this {@code UndecidedSampleContext} and should be supplied using the
 * scripting variable reference.
 * </p><p>
 * There is special handling such that if the 'registerWithRetractWapPage'
 * parameter is set to true, this tag will report the {@code SampleInfo} object
 * for the 'subject sample' to the {@code RetractWapPage} in which this tag is
 * nested during the {@code FETCHING_PHASE}. In other cases this tag does not
 * need to be nested within a {@code RetractWapPage}.
 * </p>
 */
public class UndecidedSampleContext extends HtmlPageElement implements
        SampleContext, SampleTextContext {

    /**
     * The {@code SampleInfo} that represents this sample context and will
     * contain the {@code SampleAnnotationInfo} provided by the
     * {@code SampleTextContext}. This is the 'subject sample'.
     */
    private SampleInfo sampleInfo;

    /**
     * The {@code SampleTextContext} provides this annotation which may have its
     * referenceSample set. The type of annotation is provided by the
     * {@code annotationType} property to this tag.
     */
    private SampleAnnotationInfo sampleAnnotationInfo;

    /**
     * An optional attribute that when present indicates a
     * {@code SampleSelector} from which the referenceSample for the annotation
     * provided by this {@code AnnotationContext}.
     */
    private SampleSelector referenceSampleSelector;

    /**
     * An optional attribute that when set, in the absence of
     * {@code referenceSampleSelector} refers to a {@code SampleContext} whose
     * sample should be the referenceSample for the annotation provided by this
     * {@code SampleTextContext}. If both this and
     * {@code referenceSampleSelector} are unset during
     * {@code onRegistrationPhaseBeforeBody()} this will be set to the most
     * immediate {@code SampleContext} in which this tag is nested.
     */
    private SampleContext referenceSampleContext;

    /**
     * An optional attribute that when present, indicates a
     * {@code SampleSelector} from which a sampleId can be determined. That
     * sampleId is retrived and used during {@code onFetchingPhaseBeforeBody()}
     * to set {@code SampleInfo}.
     */
    private SampleSelector subjectSampleSelector;

    /**
     * A reference to a {@code SampleContext} that describes the 'subject
     * sample'. If, during {@code onRegistrationPhaseBeforeBody()},
     * {@code SampleSelector} is not set, this variable is set to reference the
     * most immediate {@code SampleContext} in which this tag is nested.
     */
    private SampleContext subjectSampleContext;

    /**
     * If this {@code UndecidedSampleContext} defines a superseding sample for a
     * 'retract' workflow action, this optional parameter should be set to true.
     * It defaults to false, but when true, a call is made to
     * {@code RetractWapPage.setSupersedingSample()} during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private boolean registerWithRetractWapPage;

    /**
     * A required attribute; used to indicate the text type of the
     * {@code SampleAnnotationInfo} that will be provided by the
     * {@code SampleTextContext}.
     */
    private int annotationType;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleInfo = null;
        this.sampleAnnotationInfo = null;
        this.referenceSampleContext = null;
        this.referenceSampleSelector = null;
        this.subjectSampleContext = null;
        this.subjectSampleSelector = null;
        this.annotationType = SampleTextInfo.INVALID_TYPE;
        this.registerWithRetractWapPage = false;
    }

    /** Sets the annotation type. */
    public void setAnnotationType(int type) {
        if (!SampleTextBL.isAnnotation(type)) {
            throw new IllegalStateException();
        }
        this.annotationType = type;
    }

    /** @param sc a {@code SampleContext} for the reference sample */
    public void setReferenceSampleContext(SampleContext sc) {
        this.referenceSampleContext = sc;
    }

    /** @param ss a {@code SampleSelector} for the reference sample */
    public void setReferenceSampleSelector(SampleSelector ss) {
        this.referenceSampleSelector = ss;
    }

    /** @param ss a {@code SampleSelector} for the subject sample */
    public void setSubjectSampleSelector(SampleSelector ss) {
        this.subjectSampleSelector = ss;
    }

    /** @param sc a {@code SampleContext} for the subject sample */
    public void setSubjectSampleContext(SampleContext sc) {
        this.subjectSampleContext = sc;
    }

    /**
     * @param reg indicates whether this tag should register itself as the
     *        superseding sample with the {@code RetractWapPage}.
     */
    public void setRegisterWithRetractWapPage(boolean reg) {
        this.registerWithRetractWapPage = reg;
    }

    /**
     * {@inheritDoc}; this version determines the most immediate
     * {@code SampleContext} so as to set the {@code referenceSampleContext} or
     * {@code subjectSampleContext} as needed.
     * 
     * @throws IllegalStateException if {@code registerWithRetractWapPage} is
     *         true and this tag is not nested within a {@code RetractWapPage}
     *         tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find the referenceSampleContext
        if ((this.referenceSampleSelector == null)
                && (this.referenceSampleContext == null)) {
            this.referenceSampleContext = findRealAncestorWithClass(this,
                    SampleContext.class);
        }

        // find the subjectSampleContext
        if ((this.subjectSampleSelector == null)
                && (this.subjectSampleContext == null)) {
            this.subjectSampleContext = findRealAncestorWithClass(this,
                    SampleContext.class);
        }

        // ensure that this tag's HtmlPage is a RetractWapPage, if needed
        if (this.registerWithRetractWapPage) {
            if (!(getPage() instanceof RetractWapPage)) {
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines the sample and reference sample,
     * and fetches any needed objects. A new {@code SampleAnnotation} is created
     * and its {@code referenceSample} is set.
     * 
     * @throws JspException in the event that an exception is thrown by any of
     *         the calls to core, a {@code JspException} will be thrown with the
     *         original exception included as the root cause.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        try {
            // determine the referenceSample
            int referenceSampleId
                    = SampleAnnotationInfo.INVALID_REFERENCE_SAMPLE;

            if (this.referenceSampleSelector != null) {
                referenceSampleId = this.referenceSampleSelector.getSampleId();
            } else if (this.referenceSampleContext != null) {
                referenceSampleId
                        = this.referenceSampleContext.getSampleInfo().id;
            } else {
                // there is no reference sample
            }

            // get the subject sample
            if ((this.subjectSampleSelector != null)
                    && (this.subjectSampleSelector.getSampleId()
                            != SampleInfo.INVALID_SAMPLE_ID)) {
                int subjectSampleId = this.subjectSampleSelector.getSampleId();

                this.sampleInfo = RequestCache.getSampleInfo(
                        super.pageContext.getRequest(), subjectSampleId,
                        SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
                if (this.sampleInfo == null) {
                    // cache miss
                    this.sampleInfo = cc.getSampleManager().getSampleInfo(
                            subjectSampleId);
                    RequestCache.putSampleInfo(super.pageContext.getRequest(),
                            sampleInfo);
                }
            } else if (this.subjectSampleContext != null) {
                this.sampleInfo = this.subjectSampleContext.getSampleInfo();
            } else {
                // no sample is defined for this context
                return rc;
            }
            
            /*
             * a SampleAnnotationInfo is created but not yet inserted into the
             * sample. If its value is set by a nested SampleField it will be
             * added to the sample's annotationInfo list by
             * onProcessingPhaseAfterBody()
             */
            this.sampleAnnotationInfo = new SampleAnnotationInfo(
                    this.annotationType, null);
            this.sampleAnnotationInfo.referenceSample = referenceSampleId;
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }

        // report the subject sample to the RetractWapPage if neccessary
        if (this.registerWithRetractWapPage) {
            ((RetractWapPage) getPage()).setSupersedingSample(this.sampleInfo);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version adds {@code sampleAnnotationInfo} to the
     * sample if it has been set by any nested tags.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if ((this.sampleAnnotationInfo != null)
                && (this.sampleAnnotationInfo.value != null)) {
            assert this.sampleInfo != null;
            this.sampleInfo.annotationInfo.add(this.sampleAnnotationInfo);
        }

        return super.onProcessingPhaseBeforeBody(pageContext);
    }

    /**
     * Implements {@code SampleContext}. In the event that the 'subject sample'
     * has not been specified, or has been specified but does not yet have a
     * value, this method will return null. In other cases, this method will
     * return null during the {@code REGISTRATION_PHASE} and
     * {@code PARSING_PHASE} but will return a valid {@code SampleInfo} for all
     * other phases.
     * 
     * @return the {@code SampleInfo} for the sample described by this context
     *         (the 'subject sample').
     */
    public SampleInfo getSampleInfo() {
        return this.sampleInfo;
    }

    /**
     * Implements {@code SampleTextContext}. This method may return null and
     * will do so in the same cases where {@code getSampleInfo()} will return
     * null.
     * 
     * @return a {@code SampleAnnotationInfo} of the type indicated by the
     *         required 'annotationType' attribute.
     */
    public SampleTextInfo getSampleTextInfo() {
        return this.sampleAnnotationInfo;
    }

    /**
     * Implements {@code SampleTextContext}. This value is equal to the text
     * type provided to {@code setAnnotationType()} and is available during
     * every phase.
     */
    public int getTextType() {
        return this.annotationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UndecidedSampleContext dc
                = (UndecidedSampleContext) super.generateCopy(newId, map);
        
        dc.subjectSampleContext
                = (SampleContext) map.get(this.subjectSampleContext);
        
        return dc;
    }
}
