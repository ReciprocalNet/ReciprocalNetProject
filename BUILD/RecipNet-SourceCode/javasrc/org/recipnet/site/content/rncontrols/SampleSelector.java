/*
 * Reciprocal Net project
 * 
 * SampleSelector.java
 * 
 * 05-Oct-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 26-Jul-2005: midurbin added comments to validation code in
 *              onFetchingPhaseAfterBody() to argue the case for not
 *              overrideing isParsedValueValid()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A tag that allows the user to input a local lab id to identify a sample from
 * the most immediate {@code LabContext}'s lab. If no such sample exists, an
 * error code is generated, otherwise the sample's id can be retrieved by a call
 * to {@code SampleSelector.getSampleId()}. This tag is designed to be used
 * beside an {@code UndecidedSampleContext} which may provide a
 * {@code SampleContext} representing the sample selected by this
 * {@code SampleSelector}.
 */
public class SampleSelector extends TextboxHtmlControl {

    /** Possible error flags, for {@code ErrorSupplier} implementation */
    public static final int INVALID_LOCALLABID
            = TextboxHtmlControl.getHighestErrorFlag() << 1;

    /** Allows sublcass to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return INVALID_LOCALLABID;
    }

    /**
     * The {@code LabContext} for the lab from which this {@code SampleSelector}
     * will identify a sample. Set by {@code onRegistrationPhaseBeforeBody()} to
     * the most immediate {@code LabContext} in which this tag is nested.
     */
    private LabContext labContext;

    /**
     * The id of the sample for the sample number (that was entered into this
     * control) and the lab (that was defined by the {@code LabContext}). This
     * value defaults to {@code INVALID_SAMPLE_ID} and is set to the value
     * obtained from {@code SampleManager.lookupSampleId()}. This value may be
     * retrieved after the {@code FETCHING_PHASE} by a call to
     * {@code getSampleId()}.
     */
    private int sampleId;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.labContext = null;
        this.sampleId = SampleInfo.INVALID_SAMPLE_ID;
    }

    /**
     * This method should not be called before the {@code FETCHING_PHASE}.
     * 
     * @return the id of the Sample described by the {@code LabContext}'s Lab
     *         and the entered {@code localLabId}.
     */
    public int getSampleId() {
        return this.sampleId;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost containing
     * {@code LabContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code LabContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find the LabContext
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            // Can't find the required context from ancestry
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the sampleId of the sample bearing the
     * local lab id entered into this control in the context of the lab
     * indicated by the {@code LabContext}.
     * 
     * @throws JspException with the root cause of any exception thrown by the
     *         call to core
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        int labId = this.labContext.getLabInfo().id;
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        if (getValue() != null) {
            if (getProhibited()) {
                /*
                 * a validation error will be reported; return early to avoid an
                 * extra call to core.
                 */
                return super.onFetchingPhaseAfterBody();
            }
            try {
                this.sampleId = cc.getSampleManager().lookupSampleId(labId,
                        getValueAsString());
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            }
            /*
             * This shouldn't go into 'isParsedValueValid()' because although it
             * *is* a validation check, it pertains to a value that is derived
             * from the parsed value. Furthermore, the code that derives this
             * value should not be moved to 'isParsedValueValid()' because it is
             * a critical operation performed during this phase and not just a
             * validation check. The code should not be duplicated between the
             * two methods even though it would preserve some architectural
             * integrity because it would be inefficient.
             */
            if (this.sampleId == SampleInfo.INVALID_SAMPLE_ID) {
                setErrorFlag(INVALID_LOCALLABID);
                setFailedValidation(true);
            }
        }
        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleSelector dc = (SampleSelector) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);

        return dc;
    }

}
