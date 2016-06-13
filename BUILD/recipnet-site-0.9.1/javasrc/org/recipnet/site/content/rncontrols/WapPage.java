/*
 * Reciprocal Net project
 * 
 * WapPage.java
 *
 * 02-Jul-2004: midurbin wrote first draft
 * 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 24-Aug-2004: midurbin changed the spec for editSamplePageHref
 * 24-Sep-2004: midurbin added doAfterBody(), triggerWorkflowAction(), and
 *              triggerCancellation(); made performWorkflowAction() and
 *              cancelWorkflowAction() protected
 * 30-Sep-2004: midurbin added isCorrectionMode() and getCorrectionHistoryId()
 * 05-Oct-2004: midurbin added storeSample() and redirectToSamplePage() to move
 *              code out of performWorkflowAction()
 * 17-Jan-2005: jobollin updated doBeforeBody() to use addFormContent for
 *              outputting hidden <input> elements
 * 27-Jan-2005: midurbin added onReevaluation() and getComments()
 * 30-Jun-2005: midurbin updated storeSample() to invoke updateSample() on
 *              SamplePage and altered performWorkflowAction() to check for
 *              duplicate local lab ids
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 07-Jul-2005: midurbin updated performWorkflowAction() to return a boolean
 * 13-Jul-2005: midurbin updated checkAuthorization() to return an int
 * 26-Jul-2005: midurbin added triggerPageReevaluation()
 * 26-Jul-2005: midurbin added storeArbitrarySampleToCore()
 * 27-Jul-2005: midurbin updated doAfterBody() to catch thrown
 *              EvaluationAbortedException objects
 * 27-Jul-2005: midurbin updated doAfterBody(), doBeforeBody() to reflect name
 *              and spec changes and added EvaluationAbortedException to the
 *              list of checked exceptions where needed
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
 *              HtmlPage
 * 16-Aug-2005: midurbin removed import statement for WorkflowHelper
 * 06-Oct-2005: midurbin updated performWorkflowAction() to pass provider users
 *              to alterSampleForWorkflowAction() when needed
 * 02-Nov-2005: midurbin updated performWorkflowAction() to consult
 *              AuthorizationCheckerBL.canSetSamplesProvider()
 * 20-Jan-2006: jobollin updated docs, reformatted code
 * 06-Feb-2006: ekoperda fixed bug #1726 in storeArbitrarySampleToCore()
 * 17-Feb-2006: jobollin made onReevaluation() drop any existing FullSampleInfo
 *              from the request cache
 * 14-Jun-2006: jobollin updated class docs
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DuplicateDataException;
import org.recipnet.site.core.InvalidModificationException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.SampleActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.RequestCache;
import org.recipnet.site.wrapper.WrongSampleStateException;

/**
 * <p>
 * This is a custom tag, to be used in place of {@code SamplePage} for pages
 * that are intended to perform a workflow action on a sample.
 * </p>
 * <p>
 * This page accepts and propagates the following request parameters:
 * <dl>
 * <dt>sampleId</dt>
 * <dd>handled by the superclass, this indicates the current sample and is
 * required in all cases except when the workflow action is {@code SUBMITTED}.
 * (the JSP author should set the 'requireSampleId' property to false)</dd>
 * <dt>sampleHistoryId</dt>
 * <dd>handled by the superclass, this parameter is required in all cases
 * except when the workflow action is {@code SUBMITED}. (the JSP author should
 * set the 'requireSampleHistoryId' property to false)</dd>
 * <dt>correctionHistoryId</dt>
 * <dd>propagated by this class, but used only as an indicator that this page
 * is performing a correction action, in which case the action performed will be
 * that indicated by the 'workflowActionCodeCorrected' property. Other tags that
 * require knowledge about whether this page is in "correction mode" may use
 * methods such as {@code isInCorrectionMode()} and
 * {@code  getCorrectionHistoryId()} uncorrectableFields: propagated by this
 * class, but not used directly. Other classes, such as SampleField, are
 * affected by this parameter.</dd>
 * </dl>
 * </p>
 * <p>
 * The comments associated with the workflow action performed by this
 * {@code WapPage} can be provided at any time by a call to
 * {@code setComments()} before the action is actually performed. The method
 * {@code getComments()} may be used to get any existing or default comments,
 * but for this class will return {@code null} until
 * {@link #setComments(String)} has been invoked.
 * </p>
 * <p>
 * To actually perform the workflow action, the method
 * {@link #triggerWorkflowAction()} must be invoked before or during the
 * {@code PROCESSING_PHASE}; the assigned action will then be performed at the
 * end of this tag's {@code PROCESSING_PHASE}.
 * </p>
 */
public class WapPage extends SamplePage {

    /**
     * An ErrorSupplier error flag indicating that a sample was being submitted
     * with the same localLabId as another sample from the same lab.
     */
    public static final int DUPLICATE_LOCAL_LAB_ID
            = HtmlPage.getHighestErrorFlag() << 1;

    /**
     * An ErrorSupplier error flag indicating that the sample's providerId has
     * been set to a value that is invalid for the user performing the action.
     */
    public static final int INVALID_PROVIDER_ID
            = HtmlPage.getHighestErrorFlag() << 2;

    /**
     * Defines the type of WapPage this tag encloses. This is a required
     * property that must be equal to an action code defined on
     * {@code SampleWorkflowBL} that corresponds to a workflow action page. This
     * will never be a correction action, but instead, the presence of the
     * parameter 'correctionHistoryId' will indicate that this page is actually
     * meant to perform a correction action, and that correction action code
     * should be supplied as {@code workflowActionCodeCorrected}.
     */
    private int workflowActionCode;

    /**
     * Defines the action that will be performed by this page if it is in
     * 'correction' mode. This is indicated by the presence of a request
     * parameter named 'correctionHistoryId'. This is a required property that
     * must be equal to an action code defined on {@code SampleWorkflowBL}.
     */
    private int workflowActionCodeCorrected;

    /**
     * A {@code String} representation of the relative path of the page to which
     * this tag should redirect upon successful completion of its workflow
     * action. This path must be of the form "/page.jsp" and is relative to the
     * servlet context. The sample id of the current sample will be provided as
     * a parameter to the request for this page. This is a required property.
     */
    private String editSamplePageHref;

    /**
     * This variable stores the comments for this workflow action. Initialized
     * to null by {@code reset()} and set by a call to {@code setComments()}
     * this value is used during the method {@code performWorkflowAction()}.
     */
    private String comments;

    /**
     * An internal variable that indicates whether the workflow action should be
     * performed at the end of the {@code PROCESSING_PHASE} or not. Set by
     * {@code triggerWorkflowAction()}.
     */
    private boolean triggerWorkflowAction;

    /**
     * An internal variable that indicates whether the workflow action should be
     * canceled and the browser should be redirected to the edit sample page at
     * the end of the {@code PROCESSING_PHASE}. Set by
     * {@code cancelWorkflowAction()}.
     */
    private boolean cancelWorkflowAction;

    /**
     * An internal variable that indicates whether the page should be
     * reevaluated after the {@code PROCESSING_PHASE}. If this is set to true,
     * and there are no validation errors reported {@code reevaluatePage()} will
     * be invoked at the end of the {@code PROCESSING_PHASE} after any other
     * processing or any subclass' processing. Set by
     * {@code triggerPageReevaluation()}.
     */
    private boolean triggerPageReevaluation;

    /**
     * An internal variable that keeps track of whether this page is in
     * 'correction mode' or not and the history id of the action being
     * corrected. This variable is initialized to null, indicating that it is
     * not yet known whether this page is in 'correction mode'. Other possible
     * values include {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} which
     * indicates that this page is not in correciton mode, or any valid sample
     * history id. The method {@code getCorrectionHistoryId()} is repsponsible
     * for setting this variable based on the presense and value of the URL
     * parameter 'correctionHistoryId'.
     */
    private Integer correctionHistoryId;

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return INVALID_PROVIDER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        setRequireSampleId(true);
        setRequireSampleHistoryId(true);
        this.workflowActionCode = SampleWorkflowBL.INVALID_ACTION;
        this.workflowActionCodeCorrected = SampleWorkflowBL.INVALID_ACTION;
        this.editSamplePageHref = null;
        this.comments = null;
        this.triggerWorkflowAction = false;
        this.cancelWorkflowAction = false;
        this.triggerPageReevaluation = false;
        this.correctionHistoryId = null;
    }

    /**
     * The attribute setter method for {@code workflowActionCode}.
     * 
     * @param code a constant code, defined on {@code SampleWorkflowBL} that
     *        describes the action performed on a single workflow action page.
     * @throws IllegalArgumentException if the provided code is invalid
     */
    public void setWorkflowActionCode(int code) {
        if (!SampleWorkflowBL.isValidActionCode(code)) {
            throw new IllegalArgumentException();
        }
        this.workflowActionCode = code;
    }

    /**
     * The attribute getter method for {@code workflowActionCode}.
     * 
     * @return the workflow action code assigned to this {@code WapPage}.
     */
    public int getWorkflowActionCode() {
        return this.workflowActionCode;
    }

    /**
     * The attribute setter method for {@code workflowActionCodeCorrected}.
     * 
     * @param code a constant code, defined on {@code SampleWorkflowBL} that
     *        describes the action performed on this workflow action page when
     *        it is in correction mode.
     * @throws IllegalArgumentException if the provided code is invalid
     */
    public void setWorkflowActionCodeCorrected(int code) {
        if (!SampleWorkflowBL.isValidActionCode(code)) {
            throw new IllegalArgumentException();
        }
        this.workflowActionCodeCorrected = code;
    }

    /**
     * The attribute getter method for {@code workflowActionCodeCorrected}.
     * 
     * @return the workflow action code assigned to this {@code WapPage} when it
     *         is in correction mode.
     */
    public int getWorkflowActionCodeCorrected() {
        return this.workflowActionCodeCorrected;
    }

    /**
     * @param href a {@code String} indicating the location of the edit sample
     *        page relative to the servlet context path.
     */
    public void setEditSamplePageHref(String href) {
        this.editSamplePageHref = href;
    }

    /**
     * @return a {@code String} indicating the location of the edit sample page
     *         relative to the servlet context path.
     */
    public String getEditSamplePageHref() {
        return this.editSamplePageHref;
    }

    /**
     * A useful method to allow nested tags to determine whether the page is in
     * "correction mode" and if so, the sample history id at which the action
     * being corrected was performed. If the page is not in correction mode,
     * {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} is returned.
     * 
     * @return the sample history id when the action being corrected was
     *         performed or if the page is not in correction mode,
     *         {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID}
     */
    public int getCorrectionHistoryId() {
        if (this.correctionHistoryId == null) {
            String unparsedHistoryId
                    = this.pageContext.getRequest().getParameter(
                            "correctionHistoryId");
            
            this.correctionHistoryId = ((unparsedHistoryId == null)
                    ? Integer.valueOf(
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                    : Integer.valueOf(unparsedHistoryId));
        }
        
        return this.correctionHistoryId.intValue();
    }

    /**
     * A useful method to determine whether the page is in "correction mode".
     * 
     * @return a boolean indicating whether the page is in "correction mode".
     */
    public boolean isInCorrectionMode() {
        return (getCorrectionHistoryId()
                != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
    }

    /**
     * @return the comments that have been set by a call to
     *         {@code getComments()} or null if no such call has been made.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * @param comments the workflow action comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();

        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                HttpServletRequest request
                        = (HttpServletRequest) super.pageContext.getRequest();
                String uncorrectableFields
                        = request.getParameter("uncorrectableFields");
                String correctionHistId
                        = request.getParameter("correctionHistoryId");

                if (uncorrectableFields != null) {
                    addFormField("uncorrectableFields", uncorrectableFields);
                }
                if (correctionHistId != null) {
                    addFormField("correctionHistoryId", correctionHistId);
                }
                
                break;
        }
    }

    /**
     * Overrides {@code HtmlPage}; the current implementation determines
     * whether the workflow action has been triggered or cancelled and at the
     * end of the {@code PROCESSING_PHASE} acts accordingly.
     * 
     * @throws JspException wraps any exception that may be thrown while
     *         performing the workflow action
     * @throws EvaluationAbortedException during the {@code PROCESSING_PHASE}
     *         on successful performance <em>or</em> cancellation of the WAP
     *         represented by this page
     */
    // FIXME: This EvaluationAbortedException business is a bad approach
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        switch (getPhase()) {
            case HtmlPage.PROCESSING_PHASE:
                if (this.triggerWorkflowAction) {
                    performWorkflowAction();
                } else if (this.cancelWorkflowAction) {
                    cancelWorkflowAction();
                }
                if (this.triggerPageReevaluation && areAllFieldsValid()) {
                    reevaluatePage();
                }
                break;
        }
        
        super.doAfterPageBody();
    }

    /**
     * {@inheritDoc}.  This version also verifies that the user may edit the
     * sample.
     * 
     * @see AuthorizationCheckerBL#canEditSample(UserInfo, SampleInfo)
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();
        
        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (!AuthorizationCheckerBL.canEditSample(
                getUserInfo(), getSampleInfo())) {
            return AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE;
        } else {
            return auth;
        }
    }

    /**
     * Ensures that no changes to the {@code SampleInfo} object associated with
     * this workflow action will be stored to the database. This method must be
     * called before the end of the {@code PROCESSING_PHASE}.
     * 
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it it is too late to cancel the
     *         action or if {@link #triggerWorkflowAction()} has been called.
     */
    public void triggerCancellation() {
        if ((getPhase() == HtmlPage.RENDERING_PHASE)
                || this.triggerWorkflowAction) {
            throw new IllegalStateException();
        }
        this.cancelWorkflowAction = true;
    }

    /**
     * Triggers the reevaluation of this page at the end of the
     * {@code PROCESSING_PHASE} after any other processing.
     * 
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it is too late to reevaluate the
     *         page
     */
    public void triggerPageReevaluation() {
        if (getPhase() == HtmlPage.RENDERING_PHASE) {
            throw new IllegalStateException();
        }
        this.triggerPageReevaluation = true;
    }

    /**
     * Performs any clean-up operations needed to cancel the workflow action and
     * redirects the user to the edit sample page. This method should is invoked
     * by {@link #doAfterPageBody()} during the {@code PROCESSING_PHASE} if
     * {@link #triggerCancellation()} has been called. The current
     * implementation performs no clean-up, but subclasses may override this
     * method to contain their own clean-up code but should delegate back to the
     * superclass to perform the redirect.
     * 
     * @throws JspException if an exception is encountered while redirecting the
     *         browser.
     * @throws EvaluationAbortedException on successful completion
     */
    // FIXME: This EvaluationAbortedException business is a bad approach
    protected void cancelWorkflowAction() throws JspException,
            EvaluationAbortedException {
        // currently there is no clean-up work to do, so simply redirect the
        // browser.
        try {
            redirectToEditSamplePage();
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        abort();
    }

    /**
     * Triggers the performance of the workflow action for this {@code WapPage}
     * on the current sample by the current user at the end of this tag's
     * {@code PROCESSING_PHASE}.
     * 
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it is too late to perform the
     *         workflow action or if {@link #triggerCancellation()} has been
     *         called.
     */
    public void triggerWorkflowAction() {
        if ((getPhase() == HtmlPage.RENDERING_PHASE)
                || this.cancelWorkflowAction) {
            throw new IllegalStateException();
        }
        this.triggerWorkflowAction = true;
    }

    /**
     * Performs the workflow for this {@code WapPage}. This method is invoked
     * by {@link #doAfterPageBody()} during the {@code PROCESSING_PHASE} if
     * {@link #triggerWorkflowAction()} has been called. This method may be
     * overrideen by subclasses to include special handling for different types
     * of workflow actions. It is not neccessary or appropriate to delegate back
     * to this class, as all workflow action proceedures should be written into
     * the subclass.
     * 
     * @return true if the action was performed, false if an error was
     *         encountered
     * @throws IllegalStateException if this method is called at any phase other
     *         than the {@code PROCESSING_PHASE}.
     * @throws JspException if any other exception is encountered while
     *         performing the workflow action.
     * @throws EvaluationAbortedException on successful completion
     */
    // FIXME: This EvaluationAbortedException business is a bad approach
    protected boolean performWorkflowAction() throws JspException,
            EvaluationAbortedException {
        if (!areAllFieldsValid()) {
            return false;
        }
        CoreConnector cc
                = CoreConnector.extract(pageContext.getServletContext());
        
        try {
            int actionCode = (super.pageContext.getRequest().getParameter(
                    "correctionHistoryId") != null
                    ? this.workflowActionCodeCorrected
                    : this.workflowActionCode);
            SampleInfo sample = getSampleInfo();

            if (!SampleWorkflowBL.isActionValid(sample.status, actionCode)) {
                throw new WrongSampleStateException(sample.status, actionCode);
            }

            ProviderInfo samplesProvider = RequestCache.getProviderInfo(
                    pageContext.getRequest(),
                    sample.dataInfo.providerId);
            if (samplesProvider == null) {
                samplesProvider = cc.getSiteManager().getProviderInfo(
                        sample.dataInfo.providerId);
                RequestCache.putProviderInfo(pageContext.getRequest(),
                        samplesProvider);
            }
            if (!AuthorizationCheckerBL.canSetSamplesProvider(
                    getUserInfo(), sample, samplesProvider)) {
                setErrorFlag(INVALID_PROVIDER_ID);
                return false;
            }

            // determine whether provider users are needed to update sample
            Map<Integer, UserInfo> providerUserMap;
            if (SampleWorkflowBL.doesActionRequireProviderUsers(
                    sample, actionCode, getProviderInfo())) {
                providerUserMap = new HashMap<Integer, UserInfo>();
                for (UserInfo user : cc.getSiteManager().getUsersForProvider(
                        sample.dataInfo.providerId)) {
                    providerUserMap.put(user.id, user);
                }
            } else {
                providerUserMap = null;
            }
            
            // alter the sample
            SampleWorkflowBL.alterSampleForWorkflowAction(
                    sample, actionCode, providerUserMap);

            storeSample();
            redirectToEditSamplePage();
            
            // ensure that processing halts
            abort();
            return true;
        } catch (DuplicateDataException ex) {
            if ((ex.getDuplicateField()
                    & DuplicateDataException.LAB_AND_LOCALLABID) != 0) {
                // the lab and localLabId is identical to an existing sample,
                // supply an error code so the mistake may be corrected by the
                // user
                setErrorFlag(DUPLICATE_LOCAL_LAB_ID);
                return false;
            }
            // some other duplicate date exception occurred that isn't
            // gracefully handled-- throw it
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (InvalidDataException ex) {
            throw new JspException(ex);
        } catch (InvalidModificationException ex) {
            throw new JspException(ex);
        } catch (WrongSampleStateException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * Stores the page's sample to core using the action code specified to this
     * page, the currently logged-in user and the comments provided to this tag.
     * This method invokes {@link SamplePage#updateSampleInfo(SampleInfo)} so
     * that the current {@code SampleInfo} is updated in accordance with the
     * completion of the workflow action. To store an arbitrary sample with
     * arbitrary comments, users, etc; use the static method
     * {@link #storeArbitrarySampleToCore(PageContext, SampleInfo, UserInfo,
     * int, String)}
     * 
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was attempted but
     *         another sample already exists with the same combination of
     *         {@code labId} and {@code localLabId}
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN}, {@code ILLEGAL_LOCALLABID},
     *         {@code ILLEGAL_SPGP}, {@code ILLEGAL_COLOR},
     *         {@code ILLEGAL_SUMMARY}, {@code ILLEGAL_COMMENTS},
     *         {@code ILLEGAL_ATTRIBUTE}, {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid.
     * @throws InvalidModificationException with a {@code reason} of
     *         {@code CANTCHANGE_LAB}, {@code CANTCHANGE_PROVIDER}, or
     *         {@code CANTCHANGE_LOCALLABID} if a prohibited modification to an
     *         existing sample was attempted.
     * @throws IOException if there is an error loading a the resource bundle
     *         containing the action code
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws RemoteException on RMI error.
     * @throws ResourceNotFoundException with an embedded {@code identifier} of
     *         type {@code SampleInfo} if modification of an unknown sample was
     *         attempted, type {@code LabInfo} if {@code sample.labId} is
     *         unknown, or {@code ProviderInfo} if {@code sample.providerInfo}
     *         is unknown.
     * @throws WrongSiteException if the sample about to be created or modified
     *         did not originate at a lab currently hosted at the local site.
     */
    protected void storeSample() throws DuplicateDataException,
            InconsistentDbException, InvalidDataException,
            InvalidModificationException, IOException,
            OperationFailedException, RemoteException,
            ResourceNotFoundException, WrongSiteException {
        this.updateSampleInfo(storeArbitrarySampleToCore(
                this.pageContext,
                getSampleInfo(),
                getUserInfo(),
                ((this.pageContext.getRequest().getParameter(
                        "correctionHistoryId") != null)
                        ? this.workflowActionCodeCorrected
                        : this.workflowActionCode),
                getComments()));
    }

    /**
     * <p>
     * Redirects the browser to the page specified in the 'editSamplePageHref'
     * property for this {@code WapPage}. This method is called after an action
     * is successfully completed or when an action is cancelled.
     * </p><p>
     * Processing should be halted after this method is called via a call to
     * {@link HtmlPage#abort()} or by returning {@code SKIP_PAGE} from
     * {@link #doAfterPageBody()}.
     * </p>
     * 
     * @throws IOException if an error is encountered while redirecting to the
     *         edit sample page.
     */
    protected void redirectToEditSamplePage() throws IOException {
        // redirect to the edit sample page
        ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                getContextPath()
                + this.editSamplePageHref
                + "?sampleId="
                + getSampleInfo().id);
    }

    /**
     * {@inheritDoc}. This version unsets 'triggerWorkflowAction',
     * 'triggerPageReevaluation', and 'comments' then delegates back to the
     * superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerWorkflowAction = false;
        this.triggerPageReevaluation = false;
        this.comments = null;
        
        /*
         * Any cached FullSampleInfo is now obsolete because a new workflow
         * action has been performed 
         */
        RequestCache.removeFullSampleInfo(
                pageContext.getRequest(), getSampleInfo().id);
        
        super.onReevaluation();
    }

    /**
     * Invokes the method
     * {@link SampleManager#putSampleInfo(SampleInfo, int, int, String)} to
     * store the given sample and records a {@code SampleActionLogEvent} for the
     * action. Does not affect page state.
     * 
     * @param pageContext the current PageContext from which to get information
     *        about the session as well as to get ServletContext attributes
     * @param sampleInfo the sample to be stored
     * @param userInfo the user to perform the action
     * @param workflowActionCode the action to be performed
     * @param comments the comments associated with the action
     * 
     * @return the {@code SampleInfo} returned by
     *         {@code SampleManager.putSampleInfo()}
     *         
     * @throws DuplicateDataException with a {@code reason} of
     *         {@code LAB_AND_LOCALLABID} if sample creation was attempted but
     *         another sample already exists with the same combination of
     *         {@code labId} and {@code localLabId}
     * @throws InconsistentDbException if a database inconsistency was detected.
     * @throws InvalidDataException with a {@code reason} of
     *         {@code MISMATCHED_LAB_AND_PROVIDER},
     *         {@code BLANK_STRINGS_FORBIDDEN}, {@code ILLEGAL_LOCALLABID},
     *         {@code ILLEGAL_SPGP}, {@code ILLEGAL_COLOR},
     *         {@code ILLEGAL_SUMMARY}, {@code ILLEGAL_COMMENTS},
     *         {@code ILLEGAL_ATTRIBUTE}, {@code ILLEGAL_ANNOTATION},
     *         {@code INACTIVE_ASSOCIATED_LAB}, or
     *         {@code INACTIVE_ASSOCIATED_PROVIDER}, if the specified
     *         {@code sample} is not valid.
     * @throws InvalidModificationException with a {@code reason} of
     *         {@code CANTCHANGE_LAB}, {@code CANTCHANGE_PROVIDER}, or
     *         {@code CANTCHANGE_LOCALLABID} if a prohibited modification to an
     *         existing sample was attempted.
     * @throws IOException if there is an error loading a the resource bundle
     *         containing the action code
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     * @throws RemoteException on RMI error.
     * @throws ResourceNotFoundException with an embedded {@code identifier} of
     *         type {@code SampleInfo} if modification of an unknown sample was
     *         attempted, type {@code LabInfo} if {@code sample.labId} is
     *         unknown, or {@code ProviderInfo} if {@code sample.providerInfo}
     *         is unknown.
     * @throws WrongSiteException if the sample about to be created or modified
     *         did not originate at a lab currently hosted at the local site.
     */
    protected static SampleInfo storeArbitrarySampleToCore(
            PageContext pageContext, SampleInfo sampleInfo, UserInfo userInfo,
            int workflowActionCode, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException, IOException,
            OperationFailedException, RemoteException,
            ResourceNotFoundException, WrongSiteException {

        // store the container object
        CoreConnector coreConnector
                = CoreConnector.extract(pageContext.getServletContext());
        SampleManagerRemote sampleManager = null;
        SiteManagerRemote siteManager = null;

        sampleManager = coreConnector.getSampleManager();
        siteManager = coreConnector.getSiteManager();

        SampleInfo sampleFromCore = sampleManager.putSampleInfo(sampleInfo,
                workflowActionCode, userInfo.id, comments);

        // record log event
        siteManager.recordLogEvent(new SampleActionLogEvent(sampleFromCore.id,
                sampleFromCore.localLabId, pageContext.getSession().getId(),
                pageContext.getRequest().getServerName(), userInfo.fullName,
                LanguageHelper.extract(
                        pageContext.getServletContext()).getActionString(
                                workflowActionCode,
                                LanguageHelper.getDefaultLocales(),
                                false)));
        
        return sampleFromCore;
    }
}
