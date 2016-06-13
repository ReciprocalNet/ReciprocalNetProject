/*
 * Reciprocal Net project
 * 
 * SamplePage.java
 *
 * 16-Jun-2004: midurbin wrote first draft
 * 02-Jul-2004: midurbin made all member variables private
 * 19-Aug-2004: cwestnea modified comments to reflect RecipnetPage's
 *              implementation of LabContext and ProviderContext
 * 29-Sep-2004: eisiorho fixed bug #1383
 * 30-Sep-2004: eisiorho fixed bug #1382, fixed redirecting in doBeforeBody()
 * 17-Jan-2005: jobollin updated doBeforeBody() to use addFormContent() to
 *              output hidden <input> elements; removed doAfterBody() which
 *              no longer did anything but delegate to the superclass
 * 10-Jun-2005: midurbin added minimal support for the case when subclasses 
 *              wish to allow new samples to be submitted by provider users
 * 24-Jun-2005: midurbin added 'ignoreSampleHistoryId' property
 * 30-Jun-2005: midurbin added updateSampleInfo() and fixed doBeforeBody() to 
 *              fetch the LabInfo for the lab of a new sample as specified by
 *              the class-level javadocs
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 13-Jul-2005: midurbin updated doBeforeBody() and checkAuthorization() to
 *              provided reason codes to the login page
 * 27-Jul-2005: midurbin updated doBeforeBody() to reflect name and spec
 *              changes
 * 06-Oct-2005: midurbin updated references to a sample's provider
 * 11-Jan-2006: jobollin removed unused imports
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * This tag implements {@code SampleContext} to expose a sample for viewing by
 * the currently logged-in user and was written to be extended to support
 * editing and submission of samples as well.
 * </p><p>
 * When the "sampleId" and "sampleHistoryId" URL parameters are specified to the
 * JSP containing this tag, this tag fully implements the {@code SampleContext},
 * {@code LabContext} and {@code ProviderContext} in a way different than the
 * superclass. The sample referred to by the {@code SampleContext} is that
 * sample defined by the provided "sampleId" and possibly also the
 * "sampleHistoryId". (the "sampleHistoryId" may be omitted if the
 * 'requireSampleId' property is set to false, in which case the most recent
 * version of the sample will be used; the "sampleHistoryId" may also be ignored
 * if 'ignoreSampleHistoryId' is set) The lab and provider that are made
 * available represent the lab and provider to which the sample belongs. In such
 * a case, the currently logged-in user must have the authorization to view the
 * given sample as determined by {@link
 * org.recipnet.site.shared.bl.AuthorizationCheckerBL#canSeeSample
 * AuthorizationCheckerBL.canSeeSample()} or the browser will be redirected to
 * the page indicated in the 'loginPageURL' property.
 * </p><p>
 * When the "sampleId" and "sampleHistoryId" URL parameters are not specified to
 * the JSP containing this tag (which is only valid if the superclass'
 * properties 'requireSampleId' and 'requireSampleHistoryId' are set to false)
 * the {@code SampleContext} provides a new sample that is to be submitted by
 * the currently logged-in user. This sample has it's originating lab set to the
 * lab to which the current user (or current user's provider) belongs, but all
 * other fields must be set by nested tags or subclasses of this tag if this
 * sample is to be submitted. This class' implementation of the
 * {@code LabContext} differs from the superclass in that it will represent the
 * lab from which the sample (or the sample's provider) originate, even if the
 * current user is a provider user. The {@code ProviderContext} implementation
 * simply returns null in this case because it cannot be determined to which
 * provider the new sample will belong. This tag ensures the the currently
 * logged-in user may submit samples by consulting {@link
 * org.recipnet.site.shared.bl.AuthorizationCheckerBL#canSubmitSamples(
 * UserInfo) AuthorizationCheckerBL.canSubmitSamples()}. This functionality is
 * the minimum support requred for this class in order for subclasses to be able
 * to support the submission of new samples.
 */
public class SamplePage extends RecipnetPage implements SampleContext {

    /** The current sample */
    private SampleInfo sampleInfo = null;

    /** The current provider (if a valid sample has been fetched) */
    private ProviderInfo providerInfo = null;

    /** The current lab (if a valid sample has been fetched) */
    private LabInfo labInfo = null;

    /**
     * This stores the id of the sample that is the subject of this page. This
     * value is taken first from the URL parameter 'sampleId'.
     */
    private int sampleId;

    /**
     * Indicates whether or not this tag should return an {@code SC_BAD_REQUEST}
     * response if there is no 'sampleId' URL parameter. This method is set by
     * {@code setRequireSampleId()} and defaults to true.
     */
    private boolean requireSampleId;

    /**
     * This stores the history id of the sample that is the subject of this
     * page. It is retrieved from the 'sampleHistoryId', URL parameter or
     * defaults to "INVALID_SAMPLE_HISTORY_ID" which is treated as the most
     * recent version.
     */
    private int sampleHistoryId;

    /**
     * Indicates whether or not this tag should return an {@code SC_BAD_REQUEST}
     * response if there is no 'sampleHistoryId' URL parameter. This method is
     * set by {@code setRequireSampleId()} and defaults to false.
     */
    private boolean requireSampleHistoryId;

    /**
     * Indicates that even when the "sampleHistoryId" request parameter is set
     * it should be ignored and the most recent version of the sample should be
     * used. This property should not be set when 'requireSampleHistoryId' is
     * set.
     */
    private boolean ignoreSampleHistoryId;

    /**
     * Overrides {@code HtmlPage} to initialize all member variables. Subclasses
     * may override this method, but MUST delegate back to their superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.sampleInfo = null;
        this.providerInfo = null;
        this.labInfo = null;
        this.sampleId = SampleInfo.INVALID_SAMPLE_ID;
        this.requireSampleId = true;
        this.sampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        this.requireSampleHistoryId = false;
        this.ignoreSampleHistoryId = false;
    }

    /**
     * Sets the 'requireSampleId' property.
     * 
     * @param requireSampleId indicates whether this Tag should throw an
     *        exception if the URL parameter 'sampleId' is not present.
     */
    public void setRequireSampleId(boolean requireSampleId) {
        this.requireSampleId = requireSampleId;
    }

    /**
     * Gets the 'requireSampleId' property.
     * 
     * @return a boolean indicating whether this tag should throw an exception
     *         if there is no 'sampleId' request parameter.
     */
    public boolean getRequireSampleId() {
        return this.requireSampleId;
    }

    /**
     * Sets the 'requireSampleHistoryId' property.
     * 
     * @param requireSampleHistoryId indicates whether this Tag should throw an
     *        exception if the URL parameter 'sampleHistoryId' is not present.
     * @throws IllegalArgumentException if 'ignoreSampleHistoryId' is already
     *         set to true while setting this to true
     */
    public void setRequireSampleHistoryId(boolean requireSampleHistoryId) {
        if (requireSampleHistoryId && this.ignoreSampleHistoryId) {
            throw new IllegalArgumentException();
        }
        this.requireSampleHistoryId = requireSampleHistoryId;
    }

    /**
     * Gets the 'requireSampleHistoryId' property.
     * 
     * @return a boolean indicating whether this tag should throw an exception
     *         if there is no 'sampleHistoryId' request parameter.
     */
    public boolean getRequireSampleHistoryId() {
        return this.requireSampleHistoryId;
    }

    /**
     * Sets the 'ignoreSampleHistoryId' property.
     * 
     * @param ignore indicates whether this Tag should ignored the
     *        'sampleHistoryId' request parameter even if it is present
     *        
     * @throws IllegalArgumentException if 'requireSampleHistoryId' is already
     *         set to true while setting this to true
     */
    public void setIgnoreSampleHistoryId(boolean ignore) {
        if (ignore && this.requireSampleHistoryId) {
            throw new IllegalArgumentException();
        }
        this.ignoreSampleHistoryId = ignore;
    }

    /**
     * Gets the 'ignoreSampleHistoryId' property.
     * 
     * @return a boolean that indicates whether this Tag should ignored the
     *         'sampleHistoryId' request parameter even if it is present
     */
    public boolean getIgnoreSampleHistoryId() {
        return this.ignoreSampleHistoryId;
    }

    /**
     * {@inheritDoc}.  During the {@code REGISTRATION_PHASE} the
     * parameters 'sampleId' and 'sampleHistoryId' are parsed.  During the
     * {@code FETCHING_PHASE}, if 'sampleId' and 'sampleHistoryId' appear valid
     * then an attempt is made to fetch the matching sample version. A call is
     * then made to {@code checkAuthorization()} to ensure that the user may
     * access the given sample. If the user is not authorized, he/she is
     * redirected to the login page and processing of this page is aborted.
     * During the {@code RENDERING_PHASE} HTML for a &lt;form&gt; is rendered,
     * including hidden fields that will serve to persist 'sampleId' and
     * 'sampleHistoryId'. Subclasses should delegate back to this method before
     * performing their own phase-based actions to ensure proper ordering.
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        
        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                // Attepmt to determine the sample version about which this
                // SamplePage pertains
                try {
                    String sampleIdStr
                            = super.pageContext.getRequest().getParameter(
                                "sampleId");
                    if ((sampleIdStr == null) && this.requireSampleId) {
                        // sampleId is required, but missing
                        throw new NumberFormatException();
                    } else if (sampleIdStr != null) {
                        this.sampleId = Integer.parseInt(sampleIdStr);
                        addFormField("sampleId", sampleIdStr);
                    } else {
                        // the 'sampleId' parameter is missing but not required
                        // so use the default value that was set by reset()
                    }
                    if (!this.ignoreSampleHistoryId) {
                        String sampleHistoryIdStr
                                = super.pageContext.getRequest().getParameter(
                                    "sampleHistoryId");
                        if ((sampleHistoryIdStr == null)
                                && this.requireSampleHistoryId) {
                            // sampleHistoryId is required, but missing
                            throw new NumberFormatException();
                        } else if (sampleHistoryIdStr != null) {
                            this.sampleHistoryId
                                    = Integer.parseInt(sampleHistoryIdStr);
                            addFormField("sampleHistoryId", sampleHistoryIdStr);
                        } else {
                            // the 'sampleHistoryId' parameter is missing but
                            // not required so use the default value that was
                            // set by reset()
                        }
                    }
                } catch (NumberFormatException nfe) {
                    try {
                        ((HttpServletResponse) super.pageContext.getResponse())
                                .sendError(HttpServletResponse.SC_BAD_REQUEST);
                        abort();
                        return;
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.PARSING_PHASE:
                // this tag does nothing during the parsing phase
                break;
            case HtmlPage.FETCHING_PHASE:
                // check the user's authorization then attempt to fetch the
                // needed container objects
                CoreConnector coreConnector = CoreConnector.extract(
                        super.pageContext.getServletContext());
                try {
                    SiteManagerRemote siteManager
                            = coreConnector.getSiteManager();
                    SampleManagerRemote sampleManager
                            = coreConnector.getSampleManager();
                    if (this.sampleId == SampleInfo.INVALID_SAMPLE_ID) {
                        if (getUserInfo() != null) {
                            // fetch a new SampleInfo
                            this.sampleInfo = sampleManager.getSampleInfo();
                            
                            // assign it's lab to that to which the user, or
                            // the user's provider belongs
                            if (AuthorizationCheckerBL.isLabUser(
                                    getUserInfo())) {
                                this.sampleInfo.labId = getUserInfo().labId;
                            } else if (AuthorizationCheckerBL.isProviderUser(
                                    getUserInfo())) {
                                
                                // the current user is a provider user, use the
                                // lab to which the user's provider belongs
                                this.sampleInfo.labId
                                        = super.getProviderInfo().labId;
                            }
                            if (!AuthorizationCheckerBL.canEditSample(
                                    getUserInfo(), this.sampleInfo)) {
                                // the user can't edit the sample he/she is
                                // creating; this should be corrected now
                                AuthorizationCheckerBL.setAccessLevel(
                                        SampleAccessInfo.READ_WRITE,
                                        getUserInfo(), this.sampleInfo);
                            }
                        }
                    } else if (this.sampleHistoryId
                            == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                        /*
                         * fetch the most recent version; must always come from
                         * SampleManager, not cache
                         */
                        this.sampleInfo
                                = sampleManager.getSampleInfo(this.sampleId);
                        RequestCache.putSampleInfo(
                               super.pageContext.getRequest(), this.sampleInfo);
                    } else {
                        // fetch indicated version
                        this.sampleInfo = RequestCache.getSampleInfo(
                                super.pageContext.getRequest(), this.sampleId,
                                this.sampleHistoryId);
                        if (this.sampleInfo == null) {
                            // cache miss
                            this.sampleInfo = sampleManager.getSampleInfo(
                                    this.sampleId, this.sampleHistoryId);
                            RequestCache.putSampleInfo(
                                    super.pageContext.getRequest(),
                                    this.sampleInfo);
                        }
                    }
                    int reason = this.checkAuthorization();
                    if (reason
                            != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
                        /*
                         * either the user has requested an existing sample that
                         * he/she cannot view, or a user is attempting to access
                         * a new sample when he/she is not authorized to do so.
                         * Either way, redirect to the login page and abort
                         * processing.
                         */
                        sendRedirectToLogin(reason);
                        abort();
                        return;
                    }
                    if (this.sampleInfo != null) {
                        if (this.sampleInfo.dataInfo.providerId
                                != ProviderInfo.INVALID_PROVIDER_ID) {
                            this.providerInfo = RequestCache.getProviderInfo(
                                    super.pageContext.getRequest(),
                                    this.sampleInfo.dataInfo.providerId);
                            if (this.providerInfo == null) {
                                // cache miss
                                this.providerInfo = siteManager.getProviderInfo(
                                        this.sampleInfo.dataInfo.providerId);
                                RequestCache.putProviderInfo(
                                        super.pageContext.getRequest(),
                                        this.providerInfo);
                            }
                        }
                        if (this.sampleInfo.labId != LabInfo.INVALID_LAB_ID) {
                            this.labInfo = RequestCache.getLabInfo(
                                    super.pageContext.getRequest(),
                                    this.sampleInfo.labId);
                            if (this.labInfo == null) {
                                // cache miss
                                this.labInfo = siteManager.getLabInfo(
                                        this.sampleInfo.labId);
                                RequestCache.putLabInfo(
                                        super.pageContext.getRequest(),
                                        this.labInfo);
                            }
                        }
                    }
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (IOException ex) {
                    throw new JspException(ex);
                }
                break;
            case HtmlPage.PROCESSING_PHASE:
                // this tag does nothing during the processing phase
                break;
            case HtmlPage.RENDERING_PHASE:
                // this tag does nothing during the rendering phase
                break;
        }
    }

    /**
     * {@inheritDoc}.  This version is invoked by {@link #doBeforePageBody()}
     * after the {@code SampleInfo} object has been fetched during the
     * {@code FETCHING_PHASE} to check whether the the currently logged in user,
     * as determined by {@code RecipnetPage}, is authorized to access the
     * sample in the way that is characterized by this Tag. The current
     * implementation checks whether the user may view ({@code
     *  AuthorizationCheckerBL.canViewSample()})
     * or submit ({@code AuthorizationCheckerBL.canSubmitSamples()}) the given
     * sample based on whether the {@code sampleId} referred to a valid sample
     * or was {@code SampleInfo.INVALID_SAMPLE_ID} respectively.
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();
        
        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (this.sampleId == SampleInfo.INVALID_SAMPLE_ID) {
            // no sampleId specified indicating that a new sample is to be used
            return (AuthorizationCheckerBL.canSubmitSamples(getUserInfo())
                    ? auth : AuthorizationReasonMessage.CANNOT_SUBMIT_SAMPLES);
        } else {
            // user is attempting to access an existing sample (or null)
            return (AuthorizationCheckerBL.canSeeSample(
                    getUserInfo(),this.sampleInfo)
                    ? auth : AuthorizationReasonMessage.CANNOT_SEE_SAMPLE);
        }
    }

    /**
     * This method is required to implement the {@code SampleContext} interface,
     * and allows nested {@code SampleFields} to get their values from the
     * database. By convention this method should not be called before the
     * {@code FETCHING_PHASE}.
     * 
     * @return the {@code SampleInfo} object for the sample about which this
     *         {@code SamplePage} pertains.
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public SampleInfo getSampleInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.sampleInfo;
    }

    /**
     * A method to update the internal {@code SampleInfo} object to reflect
     * changes made by calls to {@link
     * org.recipnet.site.core.SampleManager#putSampleInfo(SampleInfo, int, int,
     * String) SampleManager.putSampleInfo()}. This method should be invoked by
     * subclasses when they store the value to core, especially in the case of a
     * newly submitted sample where the sampleId is assigned by that call.
     * 
     * @param updatedSampleInfo the {@code SampleInfo} returned by a call to
     *        {@code SampleManager.putSampleInfo()}.
     * @throws IllegalStateException if there is current no sample or the
     *         current sample is not new.
     */
    protected void updateSampleInfo(SampleInfo updatedSampleInfo) {
        if ((this.sampleInfo == null)
                || ((this.sampleInfo.id != SampleInfo.INVALID_SAMPLE_ID) 
                        && (this.sampleInfo.id != updatedSampleInfo.id))) {
            throw new IllegalStateException();
        }
        this.sampleInfo = updatedSampleInfo;
        this.sampleHistoryId = updatedSampleInfo.historyId;
        RequestCache.putSampleInfo(pageContext.getRequest(), updatedSampleInfo);
    }

    /**
     * This method overrides {@code RecipnetPage} to provide the
     * {@code ProviderInfo} object asociated with the sample, and allows nested
     * {@code ProviderFields} to get their values from the database. By
     * convention, this method should not be called before the
     * {@code FETCHING_PHASE}.
     * 
     * @return the {@code ProviderInfo} object for the provider associated with
     *         the sample about which this {@code SamplePage} pertains,
     *         
     * @throws IllegalStateException() if no {@code ProviderInfo} is avaialable,
     *         either because it is not yet the {@code FETCHING_PHASE}, some
     *         other error condition prevented the {@code ProviderInfo} object
     *         from being fetched or the {@code SampleInfo} object was
     *         unavailable or did not specify a valid provider.
     */
    @Override
    public ProviderInfo getProviderInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.providerInfo;
    }

    /**
     * This method overrides {@code RecipnetPage} to provide the {@code LabInfo}
     * object asociated with the sample, and allows nested {@code LabFields} to
     * get their values from the database. By convention, this method should not
     * be called before the {@code FETCHING_PHASE}.
     * 
     * @return the {@code LabInfo} object for the lab associated with the sample
     *         to which this {@code SamplePage} pertains.
     *         
     * @throws IllegalStateException() if no {@code LabInfo} is avaialable,
     *         either because it is not yet the {@code FETCHING_PHASE}, some
     *         other error condition prevented the {@code LabInfo} object from
     *         being fetched or the {@code SampleInfo} object was unavailable or
     *         did not specify a valid lab.
     */
    @Override
    public LabInfo getLabInfo() {
        if (this.labInfo == null) {
            return super.getLabInfo();
        }
        return this.labInfo;
    }
}
