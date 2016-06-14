/*
 * Reciprocal Net project
 * 
 * ProviderPage.java
 * 
 * 30-Nov-2004: mdurbin wrote first draft
 * 17-Jan-2005: jobollin updated doBeforeBody() to use addFormContent to output
 *              hidden <input> elements
 * 27-Jan-2005: midurbin added onReevaluation()
 * 24-Feb-2005: midurbin added optimistic locking support
 * 26-Apr-2005: midurbin added UserActionLogEvent for users deactivated when
 *              their provider is deactived
 * 10-Jun-2005: midurbin update performProviderAction() to use UserBL and
 *              ProviderBL
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 13-Jul-2005: midurbin removed redirectToLogin(), updated doBeforeBody() and
 *              checkAuthorization() to provide reason codes to the login page
 * 26-Jul-2005: midurbin fixed bug #1634 in doBeforeBody()
 * 27-Jul-2005: midurbin updated doBeforeBody() and doAfterBody() to reflect
 *              name and spec changes; updated performProviderAction to throw
 *              EvaluationAbortedException
 * 11-Aug-2005: midurbin factored ErrorSupplier implementing code out to
 *              HtmlPage
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.DuplicateDataException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.ProviderBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.ProviderActionLogEvent;
import org.recipnet.site.shared.logevent.UserActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * An extension of {@code RecipnetPage} but overrides some of the context
 * implementations. This {@code ProviderPage} provides the
 * {@code ProviderContext} for an arbitrary provider as indicated by parameters
 * on the request object.
 * </p><p>
 * This class recognizes two request parameters:
 * <ul>
 * <li>A provider id parameter, if provided, refers to the provider that will
 * be the subject of this provider page.</li>
 * <li>A lab id parameter, if provided when no provider id parameter is
 * provided, refers to the lab for which a new provider may be created by this
 * page.</li>
 * </ul>
 * Exactly one parameter is expected. If none are provided this page will throw
 * exceptions from various methods. If more than one are provided, only the
 * first (in the order of priority listed above) will be considered.
 * </p><p>
 * This class' implementation of the {@code ProviderContext} and
 * {@code LabContext} differ from the superclass'. While the 'provider' for the
 * superclass' implementation is the provider to which the currently logged in
 * user belongs, the 'provider' for this class is based on the request
 * parameters. Likewise, the 'lab' provided by this {@code LabContext} is not
 * the lab with which the currently logged in user is associated, but instead
 * the lab from which the specified provider originates (or to which a new
 * provider will be added).
 * </p><p>
 * The timestamp of the {@code ProviderInfo} that is provided through the
 * {@code ProviderContext} is included as a parameter when the data is posted to
 * ensure that changes aren't made to the {@code ProviderInfo} object that
 * weren't displayed on this {@code UserPage}.
 * </p>
 */
public class ProviderPage extends RecipnetPage {

    /**
     * A provider function code representing no function
     */
    public static final int NO_PROVIDER_FUNCTION = 0;

    /**
     * A provider function code representing adding a new provider
     */
    public static final int ADD_NEW_PROVIDER = 1;

    /**
     * A provider function code representingediting an existing provider
     */
    public static final int EDIT_EXISTING_PROVIDER = 2;

    /**
     * A provider function code representing initiating the provider
     * deactivation procedure
     */
    public static final int CONSIDER_DEACTIVATION = 3;

    /**
     * A provider function code representing confirming provider deactivation
     */
    public static final int DEACTIVATE_PROVIDER = 4;

    /**
     * A provider function code representing cancelling provider deactivation
     */
    public static final int CANCEL_DEACTIVATION = 5;

    /**
     * A provider function code representing cancelling any provider function
     */
    public static final int CANCEL_PROVIDER_FUNCTION = 6;

    /**
     * The provider id parsed from the request parameter specified by
     * {@code providerIdParamName} or {@code ProviderInfo.INVALID_PROVIDER_ID}
     * if none was provided.
     */
    private int providerId;

    /**
     * The lab id parsed from the request parameter specified by
     * {@code labIdParamName} or {@code LabInfo.INVALID_LAB_ID} if none was
     * provided.
     */
    private int labId;

    /**
     * The {@code ProviderInfo} object for the provider that is the subject of
     * this page. This object is fetched from core during the
     * {@code FETCHING_PHASE} and returned by {@code getProviderInfo()}.
     */
    private ProviderInfo providerInfo;

    /**
     * The {@code LabInfo} object for the lab with which this page's provider is
     * affiliated. This object is fetched from core during the
     * {@code FETCHING_PHASE} and returned by {@code getLabInfo()}.
     */
    private LabInfo labInfo;

    /**
     * A required property that indicates the name of the request parameter that
     * contains the provider id.
     */
    private String providerIdParamName;

    /**
     * A required property that indicates the name of the request parameter that
     * contains the lab id. This parameter is only used if the
     * 'providerIdParmName' parameter is unset, or set to
     * {@code INVALID_PROVIDER_ID}, in which case this lab id describes the lab
     * to which a new provider will be created.
     */
    private String labIdParamName;

    /**
     * An optional parameter that defaults to null. When this page is requested
     * to perform the {@code CONSIDER_DEACTIVATION} function, it redirects the
     * browser to a URL for a page that explains the significance of
     * deactivation and ask for definitive confirmation that that is what the
     * user wishes to do. Therefore, if such a function is to be performed on
     * this page, this value should not be null. That URL must be provided as
     * this property and should be relative to the context path. This value will
     * not be escaped before use, so if any escaping is needed, it is left up to
     * the JSP author.
     */
    private String confirmDeactivationPageUrl;

    /**
     * A parameter that indicates the URL (relative to the context path) of the
     * page where the user may manage the providers for a given lab. When a
     * function on a provider is completed, the browser will be redirected back
     * to this page. Therefore, if such a function is to be performed on this
     * page, this value should not be left as null, the default value. This
     * value will not be escaped before use, so if any escaping is needed, it is
     * left up to the JSP author.
     */
    private String manageProvidersPageUrl;

    /**
     * An optional parameter that indicates the URL (relative to the
     * contextPath) of the page where the user may manage a given provider. When
     * a user cancels an attempt to deactivate a provider, the browser is
     * redirected to this page where other changes may be made. Therefore, if
     * such an action is to be performed on this page, this value should not be
     * left as null, the default value. This value will not be escaped before
     * use, so if any escaping is needed, it is left up to the JSP author.
     */
    private String providerPageUrl;

    /**
     * The {@code ts} field of the {@code ProviderInfo}. Included by this page
     * as a hidden field in some {@code selfForm} tags and set during the
     * parsing phase or the fetching phase. Initialized to zero by
     * {@code reset()} to indicate that it is unset.
     */
    private long timestamp;

    /**
     * An internal variable that stores the function code for the function
     * triggered by a call to {@code triggerUserAction()}. This should be one
     * of the function codes defined by this class.
     */
    private int providerFunction;

    /**
     * An internal boolean that indicates whether the performance of a function
     * has been triggered or not. If it has been triggered, the method
     * {@code performProviderAction()} will be invoked at the end of the
     * {@code PROCESSING_PHASE}.
     */
    private boolean triggerAction;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.confirmDeactivationPageUrl = null;
        this.manageProvidersPageUrl = null;
        this.providerPageUrl = null;
        this.providerIdParamName = "providerId";
        this.labIdParamName = "labId";
        this.timestamp = 0;
        this.providerFunction = NO_PROVIDER_FUNCTION;
    }

    /**
     * @param url the URL (relative to the context path) of the page where a
     *        lab's providers are listed for management
     */
    public void setManageProvidersPageUrl(String url) {
        this.manageProvidersPageUrl = url;
    }

    /**
     * @return the URL (relative to the context path) of the page where a lab's
     *         providers are listed for management
     */
    public String getManageProvidersPageUrl() {
        return this.manageProvidersPageUrl;
    }

    /**
     * @param url the URL (relative to the context path) of the page where a
     *        provider's fields may be edited
     */
    public void setProviderPageUrl(String url) {
        this.providerPageUrl = url;
    }

    /**
     * @return the URL (relative to the context path) of the page where a
     *         provider's fields may be edited
     */
    public String getProviderPageUrl() {
        return this.providerPageUrl;
    }

    /**
     * @param url the URL (relative to the context path) of the page where a
     *        warning message is displayed and the user may confirm that he/she
     *        does, in fact, wish to deactivate the provider
     */
    public void setConfirmDeactivationPageUrl(String url) {
        this.confirmDeactivationPageUrl = url;
    }

    /**
     * @return the URL (relative to the context path) of the page where a
     *         warning message is displayed and the user may confirm that he/she
     *         does, in fact, wish to deactivate the provider
     */
    public String getConfirmDeactivationPageUrl() {
        return this.confirmDeactivationPageUrl;
    }

    /**
     * @param name the name of the request parameter containing the provider id
     */
    public void setProviderIdParamName(String name) {
        this.providerIdParamName = name;
    }

    /** @return the name of the request parameter containing the provider id */
    public String getProviderIdParamName() {
        return this.providerIdParamName;
    }

    /** @param name the name of the request parameter containing the lab id */
    public void setLabIdParamName(String name) {
        this.labIdParamName = name;
    }

    /** @return the name of the request parameter containing the lab id */
    public String getLabIdParamName() {
        return this.labIdParamName;
    }

    /**
     * {@inheritDoc}; this version delegates back to the superclass'
     * implementation before performing various phase-based operations. During
     * the {@code REGISTRATION_PHASE} this tag parses the request parameters.
     * The corresponding container objects are fetched during the
     * {@code FETCHING_PHASE} after a quick check of the currently logged in
     * user's authorization. During the {@code RENDERING_PHASE} the parameters
     * used to maintain this page's state are output into hidden fields.
     * 
     * @throws JspException wrapping an IOException or other exception that is
     *         throw by one of the methods invoked by this method
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                try {
                    // parse ProviderId if present
                    String providerIdStr
                            = this.pageContext.getRequest().getParameter(
                                    this.providerIdParamName);
                    if (providerIdStr == null) {
                        this.providerId = ProviderInfo.INVALID_PROVIDER_ID;
                    } else {
                        this.providerId = Integer.parseInt(providerIdStr);
                        addFormField(this.providerIdParamName, providerIdStr);
                    }

                    // parse LabId if present
                    String labIdStr = this.pageContext.getRequest().getParameter(
                            this.labIdParamName);
                    if (labIdStr == null) {
                        this.labId = LabInfo.INVALID_LAB_ID;
                    } else {
                        this.labId = Integer.parseInt(labIdStr);
                        addFormField(this.labIdParamName, labIdStr);
                    }

                    if ((providerId == ProviderInfo.INVALID_PROVIDER_ID)
                            && (labId == LabInfo.INVALID_LAB_ID)) {
                        /*
                         * for this page to be valid, either a provider or a lab
                         * must be specified.
                         */
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException nfe) {
                    try {
                        ((HttpServletResponse) super.pageContext.getResponse()
                                ).sendError(HttpServletResponse.SC_BAD_REQUEST);
                        abort();
                        return;
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.PARSING_PHASE:
                // parse the timestamp if an existing provider is being edited
                if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                    try {
                        String timestampStr
                                = this.pageContext.getRequest().getParameter(
                                        "timestamp");
                        this.timestamp = Long.parseLong(timestampStr);
                    } catch (NumberFormatException nfe) {
                        try {
                            ((HttpServletResponse) super.pageContext.getResponse()
                                    ).sendError(HttpServletResponse.SC_BAD_REQUEST);
                            abort();
                            return;
                        } catch (IOException ex) {
                            throw new JspException(ex);
                        }
                    }
                }
                break;
            case HtmlPage.FETCHING_PHASE:
                CoreConnector cc = CoreConnector.extract(
                        super.pageContext.getServletContext());
                try {
                    SiteManagerRemote siteManager = cc.getSiteManager();
                    if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                        // fetch the existing provider
                        this.providerInfo = RequestCache.getProviderInfo(
                                this.pageContext.getRequest(), this.providerId);
                        if (this.providerInfo == null) {
                            this.providerInfo = siteManager.getProviderInfo(
                                    this.providerId);
                            RequestCache.putProviderInfo(
                                    this.pageContext.getRequest(),
                                    this.providerInfo);
                        }

                        /* set the timestamp if it wasn't already parsed and
                         add it to the form content*/
                        if (this.timestamp == 0) {
                            this.timestamp = this.providerInfo.ts;
                        }
                        addFormField("timestamp",
                                String.valueOf(this.timestamp));

                        // fetch its originating lab as well
                        this.labInfo = RequestCache.getLabInfo(
                                this.pageContext.getRequest(),
                                this.providerInfo.labId);
                        if (this.labInfo == null) {
                            this.labInfo = siteManager.getLabInfo(
                                    this.providerInfo.labId);
                        }
                    } else if (this.labId != LabInfo.INVALID_LAB_ID) {
                        // fetch the lab, and create a new provider for it
                        this.labInfo = RequestCache.getLabInfo(
                                this.pageContext.getRequest(), this.labId);
                        if (this.labInfo == null) {
                            this.labInfo = siteManager.getLabInfo(this.labId);
                        }
                        this.providerInfo = siteManager.getEmptyProviderInfo();
                        this.providerInfo.isActive = true;
                        this.providerInfo.labId = this.labId;
                    }
                    int reason = checkAuthorization();
                    if (reason != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
                        sendRedirectToLogin(reason);
                        abort();
                        return;
                    }
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (IOException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
                break;
            case HtmlPage.PROCESSING_PHASE:
                // nothing to be done
                break;
            case HtmlPage.RENDERING_PHASE:
                // nothing to be done
                break;
        }
    }

    /**
     * {@inheritDoc}; this version determines whether the performance of a
     * function has been triggered or cancelled, and at the end of the
     * {@code PROCESSING_PHASE} acts accordingly.
     * 
     * @throws JspException wraps any exception that may be thrown while
     *         performing the provider function
     */
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        switch (getPhase()) {
            case HtmlPage.PROCESSING_PHASE:
                if (this.triggerAction) {
                    performProviderAction();
                }
                break;
        }
        super.doAfterPageBody();
    }

    /**
     * Implements {@code ProviderContext}. This method may not be called before
     * the {@code FETCHING_PHASE}. The {@code ProviderInfo} returned by this
     * method is the provider that is the subject of this page as specified by
     * the request parameters. Unlike the superclass, this is NOT the provider
     * to which the currently logged in user belongs.
     * 
     * @return the {@code ProviderInfo} for the provider that is the subject of
     *         this {@code ProviderPage}
     * @throws IllegalStateException if this method is invoked before the
     *         {@code FETCHING_PHASE}
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
     * Implements {@code LabContext}. This method may not be called before the
     * {@code FETCHING_PHASE}. The {@code LabInfo} returned by this this method
     * is the lab from which the provider that is the subject of this page,
     * originates. Unlike the superclass, this is NOT the lab to which the
     * currently logged in user belongs.
     * 
     * @return the {@code LabInfo} for the lab from which this page's provider
     *         originates
     * @throws IllegalStateException if this method is invoked before the
     *         {@code FETCHING_PHASE}
     */
    @Override
    public LabInfo getLabInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.labInfo;
    }

    /**
     * {@inheritDoc}; this version determines whether the currently logged in
     * user may edit the provider that is the subject of this page, or add the
     * provider if it is a new provider.
     * 
     * @throws IllegalStateException if all the container objects are null
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();

        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
            return (AuthorizationCheckerBL.canEditProvider(this.getUserInfo(),
                    super.getLabInfo(), this.labInfo) ? auth
                    : AuthorizationReasonMessage.CANNOT_EDIT_PROVIDER);
        } else if (this.labId != LabInfo.INVALID_LAB_ID) {
            return (AuthorizationCheckerBL.canAddProviderForLab(
                    this.getUserInfo(), super.getLabInfo(), this.labInfo) ? auth
                    : AuthorizationReasonMessage.CANNOT_CREATE_PROVIDER_FOR_LAB);
        } else {
            // neither a lab nor provider was specified
            throw new IllegalStateException();
        }
    }

    /**
     * Redirects the browser to the supplied page.
     * 
     * @param urlAndQuery a {@code String} to be appended to the context path to
     *        form the complete target address.
     * @throws IOException if an error is encountered while redirecting
     */
    protected void sendRedirect(String urlAndQuery) throws IOException {
        ((HttpServletResponse) this.pageContext.getResponse()).sendRedirect(
                ((HttpServletRequest) this.pageContext.getRequest()).getContextPath()
                + urlAndQuery);
    }

    /**
     * Triggers the performance of the indicated provider function for this
     * {@code ProviderPage} on the current provider by the currently logged in
     * user at the end of this tag's {@code PROCESSING_PHASE}. At this point
     * the validity of the selected function is verified.
     * 
     * @param providerFunction the function to be performed by this page; must
     *        be one of those defined by this class
     * @throws IllegalArgumentException if the provided provider function is not
     *         one of those defined by this class, or is incompatible with other
     *         parsed request parameter (ie, the {@code EDIT_PROVIDER} provider
     *         fuction was specified but no provider id was included in the
     *         request parameters)
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it is too late to perform the
     *         provider action associated with the function or if another
     *         function has been triggered.
     */
    public void triggerProviderAction(int providerFunction) {
        if ((getPhase() == HtmlPage.RENDERING_PHASE)
                || (this.providerFunction != ProviderPage.NO_PROVIDER_FUNCTION)) {
            throw new IllegalStateException();
        }

        if (!isFunctionValid(providerFunction)) {
            throw new IllegalArgumentException();
        }

        this.providerFunction = providerFunction;
        this.triggerAction = true;

    }

    /**
     * Performs the provider function for this {@code ProviderPage}. This
     * method is invoked by {@code doAfterPageBody()} during the
     * {@code PROCESSING_PHASE} if {@code triggerProviderAction()} has been
     * called.
     * 
     * @throws IllegalStateException if this method is called at any phase other
     *         than the {@code PROCESSING_PHASE} or if the requested function is
     *         not valid.
     * @throws JspException if any other exception is encountered while
     *         performing the provider function
     * @throws EvaluationAbortedException if page evaluation is aborted as a
     *         result of performing the configured action; this is in fact the
     *         normal success behavior in this version
     */
    public void performProviderAction() throws JspException,
            EvaluationAbortedException {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        if (!areAllFieldsValid()
                && (this.providerFunction != CANCEL_PROVIDER_FUNCTION)) {
            return;
        }
        
        CoreConnector cc
                = CoreConnector.extract(super.pageContext.getServletContext());

        /*
         * Ensure that the timestamp from the first roundtrip is used. If the
         * timestamp is different than that of the version in the database, core
         * will throw an OptimisticLockingException TODO: consider handling the
         * locking error in this class rather than letting it be thrown to the
         * error page
         */
        if (this.timestamp != 0) {
            this.providerInfo.ts = this.timestamp;
        }

        try {
            switch (providerFunction) {
                case NO_PROVIDER_FUNCTION:
                    // do nothing
                    return;
                case ADD_NEW_PROVIDER:
                    // store the new provider
                    this.providerInfo.id
                            = cc.getSiteManager().writeUpdatedProviderInfo(
                                    this.providerInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(
                            new ProviderActionLogEvent(
                                    this.pageContext.getSession().getId(),
                                    this.pageContext.getRequest().getServerName(),
                                    this.getUserInfo().username,
                                    this.providerInfo.id,
                                    this.providerInfo.name, false));

                    // redirect to the manage providers page
                    sendRedirect(this.manageProvidersPageUrl + "?labId="
                            + this.labInfo.id);
                    abort();
                    return;
                case EDIT_EXISTING_PROVIDER:
                    // store the updated provider
                    cc.getSiteManager().writeUpdatedProviderInfo(
                            this.providerInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(
                            new ProviderActionLogEvent(
                                    this.pageContext.getSession().getId(),
                                    this.pageContext.getRequest().getServerName(),
                                    this.getUserInfo().username,
                                    this.providerInfo.id,
                                    this.providerInfo.name, true));

                    // redirect to the manage providers page
                    sendRedirect(this.manageProvidersPageUrl + "?labId="
                            + this.labInfo.id);
                    abort();
                    return;
                case CONSIDER_DEACTIVATION:
                    sendRedirect(this.confirmDeactivationPageUrl
                            + "?providerId=" + this.providerId);
                    abort();
                    return;
                case DEACTIVATE_PROVIDER:
                    ProviderBL.deactivateProvider(this.providerInfo);
                    SiteManagerRemote siteManager = cc.getSiteManager();
                    
                    for (UserInfo user : siteManager.getUsersForProvider(
                            this.providerInfo.id)) {
                        // Deactivate each currently active user for this
                        // provider.
                        UserBL.deactivateUser(user);
                        try {
                            // write updated user
                            siteManager.writeUpdatedUserInfo(user);

                            // record log event
                            siteManager.recordLogEvent(new UserActionLogEvent(
                                    this.pageContext.getSession().getId(),
                                    this.pageContext.getRequest().getServerName(),
                                    UserActionLogEvent.USER_DEACTIVATED_WITH_PROVIDER,
                                    user, this.getUserInfo().username));
                        } catch (DuplicateDataException ex) {
                            // Can't happen because we're modifying an existing
                            // user
                            throw new UnexpectedExceptionException(ex);
                        }
                    }
                    // store the updated (deactivated) provider
                    siteManager.writeUpdatedProviderInfo(this.providerInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(
                            new ProviderActionLogEvent(
                                    this.pageContext.getSession().getId(),
                                    this.pageContext.getRequest().getServerName(),
                                    this.getUserInfo().username,
                                    this.providerInfo.id,
                                    this.providerInfo.name, true));

                    // redirect to the manage providers page
                    sendRedirect(this.manageProvidersPageUrl + "?labId="
                            + this.labInfo.id);
                    abort();
                    return;
                case CANCEL_DEACTIVATION:
                    // redirect to the edit provider page
                    sendRedirect(this.providerPageUrl + "?providerId="
                            + this.providerInfo.id);
                    abort();
                    return;
                case CANCEL_PROVIDER_FUNCTION:
                    // redirect to the manage providers page
                    sendRedirect(this.manageProvidersPageUrl + "?labId="
                            + this.labInfo.id);
                    abort();
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (InvalidDataException ex) {
            // would most likely be the result of a programming error
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (OperationNotPermittedException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * Checks whether the given provider function is valid given the parameters
     * passed to this page and the authorization of the currently logged in user
     * to perform such functions. This method must not be called before the
     * {@code FETCHING_PHASE}. This method is called by this class when a
     * function is triggered and does not need to be explicitly called for this
     * to appropriately validate functions, though it may be useful for other
     * classes like {@code ProviderActionButton}.
     * 
     * @param providerFunction the function to be performed by this page; must
     *        be one of those defined by this class
     * @throws IllegalArgumentException if the provided 'providerFunction' is
     *         not one of those defined by this class
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     * @return true if the function may be performed by the currently logged in
     *         user, or false if it may not
     */
    public boolean isFunctionValid(int providerFunction) {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        switch (providerFunction) {
            case NO_PROVIDER_FUNCTION:
                // always a valid action
                return true;
            case ADD_NEW_PROVIDER:
                // a new provider may not be added if a provider id was
                // specified
                if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                    return false;
                }
                return AuthorizationCheckerBL.canAddProviderForLab(
                        this.getUserInfo(), super.getLabInfo(), this.labInfo);
            case EDIT_EXISTING_PROVIDER:
                // to edit a provider, a provider id must have been provided
                if (this.providerId == ProviderInfo.INVALID_PROVIDER_ID) {
                    return false;
                }
                return AuthorizationCheckerBL.canEditProvider(
                        this.getUserInfo(), super.getLabInfo(), this.labInfo);
            case CONSIDER_DEACTIVATION:
            case DEACTIVATE_PROVIDER:
                return AuthorizationCheckerBL.canEditProvider(
                        this.getUserInfo(), super.getLabInfo(), this.labInfo);
            case CANCEL_DEACTIVATION:
            case CANCEL_PROVIDER_FUNCTION:
                // always a valid action
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * {@inheritDoc}; this version unsets 'triggerAction' and
     * 'providerFunction' then delegates back to the superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerAction = false;
        this.providerFunction = NO_PROVIDER_FUNCTION;
        super.onReevaluation();
    }
}
