/*
 * Reciprocal Net project
 * 
 * SpecificSampleVersion.java
 * 
 * 09-Dec-2004: midurbin wrote first draft
 * 06-Oct-2005: midurbin updated references to a sample's provider
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that provides a {@code SampleContext} to any nested tags for a
 * version of a sample described by request parameters. Along with the
 * {@code SampleContext} implementation there is a {@code LabContext} and
 * {@code ProviderContext} implemention for the lab and provider associated with
 * the sample.
 */
public class SpecificSampleVersion extends HtmlPageElement implements
        LabContext, ProviderContext, SampleContext {

    /**
     * A required parameter that indicates the name of the request parameter
     * that contains the sample id for the sample that is to be represented by
     * this tag.
     */
    private String sampleIdParamName;

    /**
     * A required parameter that indicates the name of the request parameter
     * that contains the sample history id for the sample that is to be
     * represented by this tag.
     */
    private String sampleHistoryIdParamName;

    /**
     * The sample id; parsed from the request parameter, 'sampleIdParamName'
     * during {@code onRegistrationPhaseBeforeBody()} and used to determine
     * which sample to fetch during {@code onFetchingPhaseBeforeBody()}.
     */
    private int sampleId;

    /**
     * The sample history id; parsed from the request parameter,
     * 'sampleIdParamName' during {@code onRegistrationPhaseBeforeBody()} and
     * used to determine which sample to fetch during
     * {@code onFetchingPhaseBeforeBody()}.
     */
    private int sampleHistoryId;

    /**
     * The {@code SampleInfo} object that was fetched by
     * {@code onFetchingPhaseBeforeBody()} and will be returned by
     * {@code getSampleInfo()}.
     */
    private SampleInfo sampleInfo;

    /**
     * The {@code LabInfo} for the lab from which the sample originates. This
     * object is fetched by {@code onFetchingPhaseBeforeBody()} and returned by
     * {@code getLabInfo()}.
     */
    private LabInfo labInfo;

    /**
     * The {@code ProviderInfo} for the provider by whom this sample was
     * submitted. This object is fetched by {@code onFetchingPhaseBeforeBody()}
     * and returned by {@code getProviderInfo()}.
     */
    private ProviderInfo providerInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        this.sampleIdParamName = null;
        this.sampleHistoryIdParamName = null;
        this.sampleId = SampleInfo.INVALID_SAMPLE_ID;
        this.sampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
    }

    /**
     * @param name the name of a request parameter that should contain the
     *        sample id for the sample that will be the subject of this tag's
     *        {@code SampleContext} implementation.
     */
    public void setSampleIdParamName(String name) {
        this.sampleIdParamName = name;
    }

    /**
     * @return the name of a request parameter that should contain the sample id
     *         for the sample that will be the subject of this tag's
     *         {@code SampleContext} implementation.
     */
    public String getSampleIdParamName() {
        return this.sampleIdParamName;
    }

    /**
     * @param name the name of a request parameter that should contain the
     *        sample history id for the sample that will be the subject of this
     *        tag's {@code SampleContext} implementation.
     */
    public void setSampleHistoryIdParamName(String name) {
        this.sampleHistoryIdParamName = name;
    }

    /**
     * @return the name of a request parameter that should contain the sample
     *         history id for the sample that will be the subject of this tag's
     *         {@code SampleContext} implementation.
     */
    public String getSampleHistoryIdParamName() {
        return this.sampleHistoryIdParamName;
    }

    /**
     * Implements {@code SampleContext}. This method should not be called
     * before the {@code FETCHING_PHASE}.
     * 
     * @return the {@code SampleInfo} for the sample described by this
     *         {@code SpecificSampleVersion}.
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public SampleInfo getSampleInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.sampleInfo;
    }

    /**
     * Implements {@code LabContext}. This method should not be called before
     * the {@code FETCHING_PHASE}.
     * 
     * @return the {@code LabInfo} for the lab from which the sample described
     *         by this {@code SpecificSampleVersion} originates.
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public LabInfo getLabInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.labInfo;
    }

    /**
     * Implements {@code ProviderContext}. This method should not be called
     * before the {@code FETCHING_PHASE}.
     * 
     * @return the {@code ProviderInfo} for the provider by whom the sample
     *         described by this {@code SpecificSampleVersion} was submitted.
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public ProviderInfo getProviderInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.providerInfo;
    }

    /**
     * {@inheritDoc}; this version retrieves the sample id and sample history
     * id that were provided (in the request being serviced) to define this
     * {@code SpecificSampleVersion}'s sample.
     * 
     * @throws JspException with a root cause of a {@code NumberFormatException}
     *         if the sample history id or sample id were missing or improperly
     *         formatted.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.sampleId
                    = Integer.parseInt(pageContext.getRequest().getParameter(
                            this.sampleIdParamName));
            this.sampleHistoryId
                    = Integer.parseInt(pageContext.getRequest().getParameter(
                            this.sampleHistoryIdParamName));
        } catch (NumberFormatException nfe) {
            throw new JspException(nfe);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version fetches the appropriate {@code SampleInfo},
     * {@code LabInfo} and {@code ProviderInfo} objects from core.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());
        try {
            // get the SampleInfo
            this.sampleInfo = RequestCache.getSampleInfo(
                    this.pageContext.getRequest(), this.sampleId,
                    this.sampleHistoryId);
            if (this.sampleInfo == null) {
                this.sampleInfo = cc.getSampleManager().getSampleInfo(
                        this.sampleId, this.sampleHistoryId);
                RequestCache.putSampleInfo(this.pageContext.getRequest(),
                        this.sampleInfo);
            }

            // get the LabInfo for the sample's lab
            this.labInfo = RequestCache.getLabInfo(
                    this.pageContext.getRequest(), this.sampleInfo.labId);
            if (this.labInfo == null) {
                this.labInfo = cc.getSiteManager().getLabInfo(
                        this.sampleInfo.labId);
                RequestCache.putLabInfo(this.pageContext.getRequest(),
                        this.labInfo);
            }

            // get the ProviderInfo for the sample's provider
            this.providerInfo = RequestCache.getProviderInfo(
                    this.pageContext.getRequest(),
                    this.sampleInfo.dataInfo.providerId);
            if (this.providerInfo == null) {
                this.providerInfo = cc.getSiteManager().getProviderInfo(
                        this.sampleInfo.dataInfo.providerId);
                RequestCache.putProviderInfo(this.pageContext.getRequest(),
                        this.providerInfo);
            }
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }
        return rc;
    }
}
