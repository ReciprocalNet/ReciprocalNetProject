/*
 * Reciprocal Net project
 * 
 * RetractWapPage.java
 *
 * 05-Oct-2004: midurbin wrote first draft
 * 06-Jun-2005: midurbin updated performWorkflowAction() to use new
 *              SUPERSEDED_ANOTHER_SAMPLE action code
 * 07-Jul-2005: midurbin updated performWorkflowAction() to return a boolean
 * 26-Jul-2005: midurbin replaced call to storeSample() for the superseding 
 *              sample with a call to storeArbitrarySampleToCore() to fix bug
 *              #1638
 * 27-Jul-2005: updated performWorkflowAction() to throw
 *              EvaluationAbortedException
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * <p>
 * An extention of {@code WapPage} for use only with the "retract" workflow
 * action. The overridden version of {@code performWorkflowAction()} is similar
 * to the superclass except that if a {@code supersedingSample} is identified,
 * that sample is also stored to the database. This tag updates the status of
 * the sample to reflect the completion of a "retract" workflow action.
 * </p><p>
 * This tag is designed to be used with an {@link UndecidedSampleContext} that
 * invokes the method {@link #setSupersedingSample(SampleInfo)} but such an
 * interaction is not required.
 * </p>
 */
public class RetractWapPage extends WapPage {

    /** Possible error flags, for {@code ErrorSupplier} implementation */
    public static final int CANNOT_SUPERSEDE_WITH_RETRACTED_SAMPLE
            = WapPage.getHighestErrorFlag() << 1;

    /** Allows sublcass to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return CANNOT_SUPERSEDE_WITH_RETRACTED_SAMPLE;
    }

    /**
     * Stores a reference to a {@code SampleInfo} object for the superseding
     * sample. (if one has been set) If this is null, then no sample will will
     * be marked as superseding this one. This variable is set by a call to
     * {@code setSupsersedingSample()}. This is not exposed as a property of
     * the {@code RetractWapPage} tag but should be set by another tag via the
     * method {@code setSupersedingSample()}.
     */
    private SampleInfo supersedingSample;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.supersedingSample = null;
        setWorkflowActionCode(SampleWorkflowBL.RETRACTED);
        setWorkflowActionCodeCorrected(SampleWorkflowBL.RETRACTED);
    }

    /**
     * This method may be called to indicate a sample that will be treated as
     * superseding the sample defined by this {@code RetractWapPage}.
     * {@code RetractWapPage}. If this method is not called, or is called with
     * a null value, then no sample will supersede the current sample. If a
     * valid sample is provided, it will be stored to the database in a manner
     * consistent with the workflow action for a superseding sample. A log
     * message will be generated as well.
     * 
     * @param sample The {@code SampleInfo} for the sample that will supersede
     *        this sample. This sample must be eligible for such a workflow
     *        action to be performed on it.
     */
    public void setSupersedingSample(SampleInfo sample) {
        this.supersedingSample = sample;
    }

    /**
     * {@inheritDoc}; this version performs the workflow action on the sample,
     * and performs another action on the {@code supersedingSample} if one was
     * specified.
     * 
     * @throws JspException wrapping any exceptions thrown by core during the
     *         processing of this workflow action or wrapping a
     *         {@code WrongSampleStateException} if the action is not valid for
     *         a sample's status.
     * @throws IllegalStateException if this method is called during any phase
     *         other than the {@code PROCESSING_PHASE}.
     */
    @Override
    public boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        if (!areAllFieldsValid()) {
            setErrorFlag(NESTED_TAG_REPORTED_VALIDATION_ERROR);
            return false;
        }

        int actionCode = ((super.pageContext.getRequest().getParameter(
                "correctionHistoryId") != null)
                ? getWorkflowActionCodeCorrected()
                : getWorkflowActionCode());

        if (!SampleWorkflowBL.isActionValid(getSampleInfo().status, actionCode)) {
            throw new JspException(new WrongSampleStateException(
                    getSampleInfo().status, actionCode));
        }

        if ((this.supersedingSample != null)
                && !SampleWorkflowBL.isActionValid(
                        this.supersedingSample.status,
                        SampleWorkflowBL.SUPERSEDED_ANOTHER_SAMPLE)) {
            setErrorFlag(CANNOT_SUPERSEDE_WITH_RETRACTED_SAMPLE);
            return false;
        }

        // update the sample to reflect action
        SampleWorkflowBL.alterSampleForWorkflowAction(getSampleInfo(),
                actionCode);

        try {
            storeSample();

            // store the superseding sample if one has been identified
            if (this.supersedingSample != null) {
                SampleWorkflowBL.alterSampleForWorkflowAction(
                        this.supersedingSample,
                        SampleWorkflowBL.SUPERSEDED_ANOTHER_SAMPLE);
                storeArbitrarySampleToCore(this.pageContext,
                        this.supersedingSample, getUserInfo(),
                        SampleWorkflowBL.SUPERSEDED_ANOTHER_SAMPLE,
                        getComments());
            }

            redirectToEditSamplePage();

            // ensure that processing halts
            abort();
            return true;
        } catch (RemoteException ex) {
            CoreConnector.extract(this.pageContext.getServletContext()
                    ).reportRemoteException(ex);
            throw new JspException(ex);
        } catch (RecipnetException ex) {
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }
}
