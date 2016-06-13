/*
 * Reciprocal Net project
 * 
 * UserPage.java
 * 
 * 30-Nov-2004: mdurbin wrote first draft
 * 17-Jan-2005: jobollin modified doBeforeBody() to use addFormContent to
 *              output hidden <input> elements
 * 27-Jan-2005: midurbin added onReevaluation()
 * 24-Feb-2005: midurbin added optimistic locking support
 * 26-Apr-2005: midurbin updated calls to UserActionLogEvent constructor to
 *              reflect specification change and set the preferences for a 
 *              newly created user
 * 18-May-2005: midurbin replaced user functions with an enum, generalized some
 *              functionality and pushed most of the redirection target
 *              determination code to the JSPs using 'cancellationUrlParamName'
 *              and 'completionUrlParamName' properties.
 * 10-Jun-2005: midurbin update performUserAction() to use UserBL
 * 29-Jun-2005: midurbin added code to update the currently logged-in user if
 *              it was modified by a completed action
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 13-Jul-2005: midurbin updated doBeforeBody() and checkAuthorization() to
 *              provide reason codes to the login page; removed
 *              sendRedirectToLogin()
 * 26-Jul-2005: midurbin fixed bug #1634 in doBeforeBody()
 * 27-Jul-2005: midurbin updated doBeforeBody() and doAfterBody() to reflect
 *              name and spec changes; updated performUserAction() to throw
 *              EvaluationAbortedException
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
 *              HtmlPage
 * 06-Oct-2005: midurbin updated various calls to AuthorizationCheckerBL to
 *              accomodate method specification changes
 * 21-Apr-2006: jobollin fixed bug #1775 in doBeforePageBody() and made
 *              doAfterPageBody() protected
 * 10-Jan-2008: ekoperda fixed bug #1855 in performUserAction()
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.DuplicateDataException;
import org.recipnet.site.core.InvalidModificationException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.UserActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that extends the {@code RecipnetPage} to allow an
 * arbitrary {@code UserInfo} to be modified.  The
 * {@link UserPage.UserFunction UserFunction} enumeration describes the various
 * actions that may be taken on the {@code UserInfo}.  A {@link
 * UserActionButton UserActionButton} is required to trigger a particular
 * action, and more than one action may be triggered on a single JSP's
 * {@code UserPage} tag. <p>
 *
 * Any action performed is considered to be performed by the currently
 * logged-in user, and checks are made with {@link
 * org.recipnet.site.shared.bl.AuthorizationCheckerBL AuthorizationCheckerBL}
 * to ensure that the user is authorized to perform such an action. <p>
 *
 * The user on which the action is being performed is determined in one of
 * three ways.
 * <ol>
 *  <li>
 *    The {@code userIdParamName} property is specified and the
 *    corresponding request parameter indicates a valid user id.  In this case
 *    this tag overrides the superclass behavior to expose the
 *    {@code UserContext} for the indicated user (as opposed to the
 *    currently logged-in user).  Likewise, the {@code LabContext} and
 *    {@code ProviderContext} reflect the lab or provider of the specified
 *    user.  
 *  </li>
 *  <li>
 *    Either the {@code labIdParamName} or
 *    {@code providerIdParamName} is specified and their corresponding
 *    request parmeter represents a valid lab or provider.  In this case this
 *    tag overrides the superclass behavior to expose a new lab or provider
 *    user for the given lab or provider.
 *  </li>
 *  <li>
 *    If neither of the above three properties are set, or none of their
 *    corresponding request parameters represent valid users, labs or
 *    providers, the user exposed by this tag is the currently logged-in user.
 *    This behavior is identical to the superclass' behavior.  In such a case,
 *    it will represent a user modifying his/her own user information.  
 *  </li>
 * </ol>
 * <br />
 *
 * The timestamp of the {@code UserInfo} that is provided through the
 * {@code UserContext} is included as a parameter when the data is posted
 * to ensure that changes aren't made to the {@code UserInfo} object that
 * weren't displayed on this {@code UserPage}.
 */
public class UserPage extends RecipnetPage {

    /**
     * An ErrorSupplier error flag that may be set if an attempt to create a
     * new user fails because another user already exists with the given
     * username.
     */
    public static final int DUPLICATE_USERNAME
            = HtmlPage.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return DUPLICATE_USERNAME;
    }

    /**
     * The user id parsed from the request parameter specified by
     * {@code userIdParamName}, the user id of the currently logged-in
     * user if no user, provider or lab-identifying parameters were set or
     * {@code UserInfo.INVALID_USER_ID} if a new user is to be created.
     */
    private int userId;

    /**
     * The lab id parsed from the request parameter specified by
     * {@code labIdParamName} or {@code LabInfo.INVALID_LAB_ID} if
     * none was provided.
     */
    private int labId;

    /**
     * The provider id parsed from the request parameter specified by
     * {@code providerIdParamName} or
     * {@code ProviderInfo.INVALID_PROVIDER_ID} if none was provided.
     */
    private int providerId;

    /**
     * The user described by this {@code UserContext} implementation.
     * This object is fetched during the {@code FETCHING_PHASE} to be
     * either the user indicted by {@code userId}, a new lab user for
     * the lab defined by {@code labId}, a new provider user for the
     * provider defined by {@code providerId} or the currently logged-in
     * user.  This is the value returned by {@code getUserInfo()}.
     */
    private UserInfo userInfo;

    /**
     * The {@code LabInfo} object for the lab with which this page's user
     * is affiliated, or null if this user is not a lab user.  This object is
     * fetched from core during the {@code FETCHING_PHASE} and returned by
     * {@code getLabInfo()}.
     */
    private LabInfo labInfo;

    /**
     * The {@code ProviderInfo} object for the provider with which this
     * page's user is affiliated, or null if this user is not a provider user.
     * This object is fetched from core during the {@code FETCHING_PHASE}
     * and returned by {@code getProviderInfo()}.
     */
    private ProviderInfo providerInfo;

    /**
     * An optional property that indicates the name of the request parameter
     * that contains the user id for the user on which this page operates.  If
     * this property is not set, this page will either expose a new lab or
     * provider user if a lab id or provider id are specified, or the currently
     * logged-in user for self-modification.
     */
    private String userIdParamName;

    /**
     * An optional property that indicates the name of the request parameter
     * that contains the lab id.  This parameter is only used if the
     * 'userIdParamName' parameter is unset, or set to
     * {@code INVALID_USER_ID}, in which case this lab id describes the
     * lab to which a new user will be created.
     */
    private String labIdParamName;

    /**
     * An optional property that indicates the name of the request parameter
     * that contains the provider id.  This parameter is only used if the
     * 'userIdParamName' and 'labIdParamName' are unset or both set to invalid
     * values in which case this provider id describes the provider to which a
     * new user will be created.
     */
    private String providerIdParamName;

    /**
     * A required property that indicates a request parameter that is expected
     * to contain the URL (relative to the context path) of the page to which
     * the browser should be redirected upon a successful non-cancellation
     * user function.  All query-line parameters needed by that page must be
     * included in the value of the parameter named by this property.
     */
    private String completionUrlParamName;

    /**
     * A required property that indicates a request parameter that is expected
     * to contain the URL (relative to the context path) of the page to which
     * the browser should be redirected upon a successful cancellation user
     * function.  All query-line parameters needed by this page must be
     * included in the value of the parameter named by this property.
     */
    private String cancellationUrlParamName;

    /**
     * An optional property that defaults to null.  When this page is requested
     * to perform the {@code CONSIDER_DEACTIVATION} function, it redirects
     * the browser to a URL for a page that explains the significance of
     * deactivation and ask for definitive confirmation that that is what
     * the user wishes to do.  The URL (relative to the context path) of the
     * page that provides that UI  must be provided as this property in the
     * event that such an action is possible from this page.  This value will
     * not be escaped before use, so if any escaping is needed, it is left up
     * to the JSP author.
     */
    private String confirmDeactivationPageUrl;

    /**
     * An optional property that indicates the 'completionUrlParamName' for
     * the {@code UserPage} tag used on the JSP indicated by
     * 'confirmDeactivationPageUrl'.  This should be specified whenever
     * 'confirmDeactivationPageUrl' is specified.  When this tag redirects the
     * browser to the JSP specified by 'confirmDeactivationPageUrl' the current
     * 'completionUrlParamName's request parameter value is escaped and
     * included as a request parameter to that page with this name.  In order
     * for that page to correctly return after completion, its
     * 'cancellationUrlParamName' must be the same as this
     * 'confirmDeactivationCancellationPageUrlParamName'.
     */
    private String confirmDeactivationCompletionPageUrlParamName;

    /**
     * An optional property that indicates the 'cancellationUrlParamName' for
     * the {@code UserPage} tag used on the JSP indicated by
     * 'confirmDeactivationPageUrl'.  This should be specified whenever
     * 'confirmDeactivationPageUrl' is specified.  When this tag redirects the
     * browser to the JSP specified by 'confirmDeactivationPageUrl' the current
     * url and query (relative to the context path) is escaped and included 
     * as a request parameter to that page with this name.  In order for that
     * page to correctly return to this page upon cancellation, its
     * 'cancellationUrlParamName' must be the same as this
     * 'confirmDeactivationCancellationPageUrlParamName'.
     */
    private String confirmDeactivationCancellationPageUrlParamName;

    /**
     * The {@code ts} field of the {@code UserInfo}.  Included by
     * this page as a hidden field in some {@code selfForm} tags and
     * set during the parsing phase or the fetching phase.  Initialized to zero
     * by {@code reset()} to indicate that it is unset.
     */
    private long timestamp;

    /**
     * An internal variable that stores the {@code UserFunction} that will
     * be triggered by a call to {@code triggerUserAction()}.
     */
    private UserFunction userFunction;

    /**
     * An internal boolean that indicates whether a function has been triggered
     * or not.  If a function execution has been triggered, the method
     * {@code performUserAction()} will be invoked at the end of the
     * {@code PROCESSING_PHASE}.
     */
    private boolean triggerAction;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.confirmDeactivationPageUrl = null;
        this.confirmDeactivationCompletionPageUrlParamName = null;
        this.confirmDeactivationCancellationPageUrlParamName = null;
        this.completionUrlParamName = null;
        this.cancellationUrlParamName = null;
        this.userIdParamName = null;
        this.providerIdParamName = null;
        this.labIdParamName = null;
        this.userId = UserInfo.INVALID_USER_ID;
        this.providerId = ProviderInfo.INVALID_PROVIDER_ID;
        this.labId = LabInfo.INVALID_LAB_ID;
        this.timestamp = 0;
        this.userFunction = null;
    }

    /**
     * @param name the name of a request parameter that contains the URL to
     *     which the browser should be redirected upon completion of a user
     *     action.  The URL passed as the parameter must be relative to the
     *     context path.
     */
    public void setCompletionUrlParamName(String name) {
        this.completionUrlParamName = name;
    }

    /**
     * @return the name of a request parameter that contains the URL to which
     *     the browser should be redirected upon completion of a user action
     *     when the 'labPageUrl' or 'providerPageUrl' are unspecified.
     */
    public String getCompletionUrlParamName() {
        return this.completionUrlParamName;
    }

    /**
     * @param name the name of a request parameter that contains the URL to
     *     which the browser should be redirected upon cancellation of a user
     *     action.  The URL passed as the parameter must be relative to the
     *     context path.
     */
    public void setCancellationUrlParamName(String name) {
        this.cancellationUrlParamName = name;
    }

    /**
     * @return the name of a request parameter that contains the URL to which
     *     the browser should be redirected upon cancellation of a user action.
     */
    public String getCancellationUrlParamName() {
        return this.cancellationUrlParamName;
    }

    /**
     * @param url the URL (relative to the context path) of the page where 
     *     a warning message is displayed and the user may confirm that he/she
     *     does, in fact, wish to deactivate the user
     */
    public void setConfirmDeactivationPageUrl(String url) {
        this.confirmDeactivationPageUrl = url;
    }

    /**
     * @return the URL (relative to the context path) of the page where a
     *     warning message is displayed and the user may confirm that he/she
     *     does, in fact, wish to deactivate the user
     */
    public String getConfirmDeactivationPageUrl() {
        return this.confirmDeactivationPageUrl;
    }

    /**
     * @param name the name of the 'completionUrlParamName' for the
     *     {@code UserPage} tag on the page indicated by
     *     'confirmDeactivationPageUrl'.
     */
    public void setConfirmDeactivationCompletionPageUrlParamName(String name) {
        this.confirmDeactivationCompletionPageUrlParamName = name;
    }

    /**
     * @return the name of the 'completionUrlParamName' for the
     *     {@code UserPage} tag on the page indicated by
     *     'confirmDeactivationPageUrl'.
     */
    public String getConfirmDeactivationCompletionPageUrlParamName() {
        return this.confirmDeactivationCompletionPageUrlParamName;
    }

    /**
     * @return the name of the 'cancellationUrlParamName' for the
     *     {@code UserPage} tag on the page indicated by
     *     'confirmDeactivationPageUrl'.
     */
    public String getConfirmDeactivationCancellationPageUrlParamName() {
        return this.confirmDeactivationCancellationPageUrlParamName;
    }

    /**
     * @param name the name of the 'cancellationUrlParamName' for the
     *     {@code UserPage} tag on the page indicated by
     *     'confirmDeactivationPageUrl'.
     */
    public void setConfirmDeactivationCancellationPageUrlParamName(
            String name) {
        this.confirmDeactivationCancellationPageUrlParamName = name;
    }


    /** @param name the name of the request parameter containing the user id */
    public void setUserIdParamName(String name) {
        this.userIdParamName = name;
    }

    /** @return the name of the request parameter containing the user id */
    public String getUserIdParamName() {
        return this.userIdParamName;
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
     * {@inheritDoc}. this version delegates back to the superclass'
     * implementation before performing various phase-based operations. During
     * the {@code REGISTRATION_PHASE} this tag ensures that the request
     * parameters are valid. During the {@code FETCHING_PHASE} the container
     * objects are fetched and the currently logged in user's authorization to
     * access the objects is checked. During the {@code RENDERING_PHASE} the
     * parameters used to maintain this page's state are output into hidden
     * fields.
     * 
     * @throws JspException wrapping an IOException or other exception that is
     *         thrown by one of the methods invoked by this method
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                try {
                    if (this.userIdParamName != null) {
                        // parse UserId if present
                        String userIdStr
                                = this.pageContext.getRequest().getParameter(
                                        this.userIdParamName);
                        if (userIdStr == null) {
                            this.userId = UserInfo.INVALID_USER_ID;
                        } else {
                            this.userId = Integer.parseInt(userIdStr);
                            addFormField(this.userIdParamName, userIdStr);
                        }
                    }
                    if (this.labIdParamName != null) {
                        // parse LabId if present
                        String labIdStr
                                = this.pageContext.getRequest().getParameter(
                                        this.labIdParamName);
                        if (labIdStr == null) {
                            this.labId = LabInfo.INVALID_LAB_ID;
                        } else {
                            this.labId = Integer.parseInt(labIdStr);
                            addFormField(this.labIdParamName, labIdStr);
                        }
                    }
                    if (this.providerIdParamName != null) {
                        // parse ProviderId if present
                        String providerIdStr
                                = this.pageContext.getRequest().getParameter(
                                        this.providerIdParamName);
                        if (providerIdStr == null) {
                            this.providerId = ProviderInfo.INVALID_PROVIDER_ID;
                        } else {
                            this.providerId = Integer.parseInt(providerIdStr);
                            addFormField(this.providerIdParamName,
                                              providerIdStr);
                        }
                    }
                    if ((providerId == ProviderInfo.INVALID_PROVIDER_ID)
                            && (labId == LabInfo.INVALID_LAB_ID)
                            && (userId == UserInfo.INVALID_USER_ID)) {
                        // no provider, lab or user was specified as a url
                        // query parameter, this page must be intended to 
                        // expose the currently logged-in user
                        if (this.pageContext.getSession().getAttribute(
                                "userInfo") != null){
                            /*
                             * use the currently logged-in user
                             *
                             * Note: we just store the userId, and later fetch
                             * the most current version of the UserInfo.  This
                             * is because the UserInfo stored in the session 
                             * may be out of date and we want to avoid an
                             * optimistic locking exception.
                             */
                            this.userId = ((UserInfo)
                                    this.pageContext.getSession().getAttribute(
                                            "userInfo")).id;
                        } else {
                            // no currently-logged in user
                            try {
                                sendRedirectToLogin(
                           AuthorizationReasonMessage.AUTHENTICATION_REQUIRED);
                                abort();
                                return;
                            } catch (IOException ex) {
                                throw new JspException(ex);
                            }
                        }
                    }
                    if (this.completionUrlParamName != null) {
                        addFormField(this.completionUrlParamName,
                                this.pageContext.getRequest().getParameter(
                                        this.completionUrlParamName));
                    }
                    if (this.cancellationUrlParamName != null) {
                        addFormField(this.cancellationUrlParamName,
                                this.pageContext.getRequest().getParameter(
                                        this.cancellationUrlParamName));
                    }
                } catch (NumberFormatException nfe) {
                    try {
                        ((HttpServletResponse)
                                super.pageContext.getResponse()).sendError(
                                       HttpServletResponse.SC_BAD_REQUEST);
                        abort();
                        return;
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.PARSING_PHASE:
                // parse the timestamp if an existing user is being edited
                if (this.userId != UserInfo.INVALID_USER_ID) {
                    try {
                        String timestampStr
                                = this.pageContext.getRequest().getParameter(
                                        "timestamp");
                        this.timestamp = Long.parseLong(timestampStr);
                    } catch (NumberFormatException nfe) {
                        try {
                            ((HttpServletResponse)
                                    super.pageContext.getResponse()).sendError(
                                           HttpServletResponse.SC_BAD_REQUEST);
                            abort();
                            return;  // Never reached: abort() always throws
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
                    
                    if (this.userId != UserInfo.INVALID_USER_ID) {
                        UserInfo loggedInUser;
                        
                        // fetch the existing user
                        this.userInfo = RequestCache.getUserInfo(
                                this.pageContext.getRequest(), this.userId);
                        if (this.userInfo == null) {
                            this.userInfo = siteManager.getUserInfo(this.userId);
                            RequestCache.putUserInfo(
                                    this.pageContext.getRequest(),
                                    this.userInfo);
                        }

                        // set the timestamp if it wasn't already parsed and
                        // add it to the form content
                        if (this.timestamp == 0L) {
                            this.timestamp = this.userInfo.ts;
                        }
                        addFormField("timestamp",
                                String.valueOf(this.timestamp));

                        /*
                         * FIXME: The next bit seems rather a lot of bother just
                         * to make an assertion; leaving it in for the moment,
                         * however 
                         */
                        loggedInUser = getLoggedInUser();
                        if ((this.userInfo != null) && (loggedInUser != null)
                                && (this.userInfo.id == loggedInUser.id)) {
                            assert this.userInfo.preferences.equals(
                                    this.pageContext.getSession().getAttribute(
                                            "preferences"));
                        }
                        
                        if (this.userInfo.providerId
                                != ProviderInfo.INVALID_PROVIDER_ID) {
                            // if it's a provider user, fetch its provider
                            this.providerInfo = RequestCache.getProviderInfo(
                                    this.pageContext.getRequest(),
                                    this.providerId);
                            if (this.providerInfo == null) {
                                this.providerInfo
                                    = siteManager.getProviderInfo(
                                            this.userInfo.providerId);
                                RequestCache.putProviderInfo(
                                        this.pageContext.getRequest(),
                                        this.providerInfo);
                            }
                        }
                        
                        if (this.userInfo.labId != LabInfo.INVALID_LAB_ID) {
                            // if it's a lab user, fetch its lab
                            this.labInfo = RequestCache.getLabInfo(
                                    this.pageContext.getRequest(),
                                    this.userInfo.labId);
                            if (this.labInfo == null) {
                                this.labInfo = siteManager.getLabInfo(
                                        this.userInfo.labId);
                                RequestCache.putLabInfo(
                                        this.pageContext.getRequest(),
                                        this.labInfo);
                            }
                        }
                    } else {
                        // create a new user

                        this.userInfo = siteManager.getEmptyUserInfo();

                        if (this.labId != LabInfo.INVALID_LAB_ID) {
                            // the user will be a lab user
                            this.labInfo = RequestCache.getLabInfo(
                                    this.pageContext.getRequest(), this.labId);
                            if (this.labInfo == null) {
                                this.labInfo = siteManager.getLabInfo(this.labId);
                                RequestCache.putLabInfo(
                                        this.pageContext.getRequest(),
                                        this.labInfo);
                            }
                            this.userInfo.labId = this.labInfo.id;
                        } else if (this.providerId
                                != ProviderInfo.INVALID_PROVIDER_ID) {
                            // the user will be a provider user
                            this.providerInfo = RequestCache.getProviderInfo(
                                    this.pageContext.getRequest(),
                                    this.providerId);
                            if (this.providerInfo == null) {
                                this.providerInfo = siteManager.getProviderInfo(
                                        this.providerId);
                                RequestCache.putProviderInfo(
                                        this.pageContext.getRequest(),
                                        this.providerInfo);
                            }
                            this.userInfo.providerId = this.providerInfo.id;
                        } else {
                            
                            /*
                             * This case should have been caught during
                             * REGISTRATION_PHASE
                             */
                            assert false: "New user requested without lab or "
                                    + "provider";
                        }
                        
                        this.userInfo.isActive = true;
                        this.userInfo.creationDate = new Date();
                    }
                    
                    int reason = checkAuthorization();
                    if (reason
                            != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
                        sendRedirectToLogin(reason);
                        abort();
                        return;
                    }
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (IOException ex) {
                    throw new JspException(ex);
                } catch (ResourceNotFoundException ex) {
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
     * {@inheritDoc}. This version determines whether the user function has
     * been triggered or cancelled and at the end of the
     * {@code PROCESSING_PHASE} acts accordingly.
     * 
     * @throws JspException wraps any exception that may be thrown while
     *         performing the user function
     */
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        switch (getPhase()) {
            case HtmlPage.PROCESSING_PHASE:
                if (this.triggerAction) {
                    performUserAction();
                }
                break;
        }
        super.doAfterPageBody();
    }

    /**
     * Implements {@code UserContext}.  This method may not be called
     * before the {@code FETCHING_PHASE}.  If a valid user id was supplied
     * via a request parameter to this page, this method will return that
     * user's {@code UserInfo}.  Otherwise, a new {@code UserInfo} 
     * associated with either the lab or the provider supplied to this page
     * will be returned.  Unlike the superclass' implementation, this method is
     * NOT meant to return the {@code UserInfo} for the currently logged
     * in user (though may, if the subject of this user page happens to be the
     * currently logged in user).
     * @return the {@code UserInfo} for the user that is the subject of 
     *     this page
     * @throws IllegalStateException if this method is invoked before the
     *     {@code FETCHING_PHASE}
     */
    @Override
    public final UserInfo getUserInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.userInfo;
    }
    
    /**
     * Returns user information for the currently logged-in user.  This is the
     * same information returned by the superclass's version of
     * {@link RecipnetPage#getUserInfo() getUserInfo()}.
     * 
     * @return a {@code UserInfo} describing the currently logged-in user, or
     *         {@code null} if no user is currently logged in
     */
    final public UserInfo getLoggedInUser() {
        return super.getUserInfo();
    }

    /**
     * Implements {@code ProviderContext}.  This method may not be called
     * before the {@code FETCHING_PHASE}.  The {@code ProviderInfo}
     * returned by this method is the provider with with the user returned by
     * {@code getUserInfo()} is associated or null if it is a lab user.
     * Unlike the superclass, the user whose provider is returned by this
     * method is NOT the currently logged in user, but instead the user
     * specified by request parameters.
     * @return the {@code ProviderInfo} for the provider with which the
     *     user that is the subject of this page is associated or null if it is
     *     a lab user.
     * @throws IllegalStateException if this method is invoked before the
     *     {@code FETCHING_PHASE}
     */
    @Override
    final public ProviderInfo getProviderInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.providerInfo;
    }

    /**
     * Implements {@code LabContext}.  This method may not be called
     * before the {@code FETCHING_PHASE}.  The {@code LabInfo}
     * returned by this method is the lab with with the user returned by
     * {@code getUserInfo()} is associated or null if it is a lab user.
     * Unlike the superclass, the user whose lab is returned by this method is
     * NOT the currently logged in user, but instead the user specified by
     * request parameters.
     * @return the {@code LabInfo} for the lab with which the user that is
     *     the subject of this page is associated or null if it is a provider
     *     user.
     * @throws IllegalStateException if this method is invoked before the
     *     {@code FETCHING_PHASE}
     */
    @Override
    final public LabInfo getLabInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.labInfo;
    }

    /**
     * {@inheritDoc}. This version is invoked after the container objects are
     * fetched to check whether the currently logged in user may edit the user
     * that is the subject of this page, or add the user if it is a new user.
     * This is an early and incomplete authorization check, in that before a
     * particular function is completed a specific check is performed for that
     * function. This method simply makes sure that at least one function
     * defined by this page <em>could</em> be performed on the given user by
     * the currently logged in user.
     * 
     * @throws IllegalStateException if all the container objects are null
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();
        
        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (this.userId != UserInfo.INVALID_USER_ID) {
            return (AuthorizationCheckerBL.canEditSomethingAboutUser(
                    getLoggedInUser(), getUserInfo(), getProviderInfo())
                    ? auth : AuthorizationReasonMessage.CANNOT_EDIT_USER);
        } else {
            if (this.labId != LabInfo.INVALID_LAB_ID) {
                return (AuthorizationCheckerBL.canCreateLabUser(
                        getLoggedInUser(), getLabInfo())
                        ? auth
                        : AuthorizationReasonMessage.CANNOT_CREATE_LAB_USER);
            } else if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                return (AuthorizationCheckerBL.canCreateProviderUser(
                        getLoggedInUser(), getProviderInfo())
                     ? auth
                     : AuthorizationReasonMessage.CANNOT_CREATE_PROVIDER_USER);
            } else {
                // neither a user, lab or provider was specified
                throw new IllegalStateException();
            }
        }
    }

    /**
     * Triggers the performance of the indicated function for this
     * {@code UserPage} on the current user by the currently logged in user at
     * the end of this tag's {@code PROCESSING_PHASE}. This method checks the
     * validition of the selected function by making a call to
     * {@code isFunctionValid()} that in turn delegates to
     * {@code AuthorizationCheckerBL} to determine the validity of the triggered
     * function.
     * 
     * @param userFunction a {@code UserFunction} representing the type of
     *        function to be performed by this page
     * @throws IllegalArgumentException if the provided 'userFunction' is
     *         incompatible with other parsed request parameters (i.e., the
     *         {@code EDIT_EXISTING_USER} function is specified but no valid
     *         user id was provided to this page).
     * @throws IllegalStateException if invoked other than during the
     *         {@code PROCESSING_PHASE}, or if a function has already been
     *         triggered
     */
    public void triggerUserAction(UserFunction userFunction) {
        if ((getPhase() != HtmlPage.PROCESSING_PHASE)
                || (this.userFunction != null)) {
            throw new IllegalStateException();
        } else  if (!isFunctionValid(userFunction)) {
            throw new IllegalArgumentException();
        } else {
            this.userFunction = userFunction;
            this.triggerAction = true;
        }
    }

    /**
     * Checks whether the given user function is valid given the parameters
     * passed to this page and the authorization of the currently logged in user
     * to perform such functions. This method must not be called before the
     * {@code FETCHING_PHASE}. This method is called by this class when a
     * function is triggered and does not need to be explicitly called by
     * subclasses, but it may be useful for classes that cooperate with this
     * one.
     * 
     * @param userFunction a {@code UserFunction} representing the type of
     *        function proposed to be performed by this page; should not be
     *        {@code null}
     * @throws IllegalStateException if this method is invoked before the
     *         {@code FETCHING_PHASE}
     * @return true if the function may be performed by the currently logged in
     *         user, or false if it may not
     */
    public boolean isFunctionValid(UserFunction userFunction) {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        } else {
            return userFunction.isValidForPage(this);
        }
    }

    /**
     * Performs the user function for this {@code UserPage}. This method is
     * invoked by {@code doAfterPageBody()} during the {@code PROCESSING_PHASE}
     * if {@code triggerUserAction()} has been called.
     * 
     * @throws IllegalStateException if this method is called at any phase other
     *         than the {@code PROCESSING_PHASE} or if the requested function is
     *         not valid
     * @throws JspException if any other exception is encountered while
     *         performing the user function.
     * @throws EvaluationAbortedException upon successfully performing the
     *         requested action
     */
    protected void performUserAction() throws JspException,
            EvaluationAbortedException {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        if (!areAllFieldsValid() && (this.userFunction != UserFunction.CANCEL)) {
            return;
        }
        
        CoreConnector cc
                = CoreConnector.extract(super.pageContext.getServletContext());

        /*
         * Ensure that the timestamp from the first roundtrip is used.
         * If the timestamp is different than that of the version in the
         * database, core will throw an OptimisticLockingException
         * TODO: consider handling the locking error in this class rather than
         *       letting it be thrown to the error page
         */
        if (this.timestamp != 0) {
            this.userInfo.ts = this.timestamp;
        }

        try {
            switch (this.userFunction) {
                case DEACTIVATE_USER:
		    // No need to update prefs on the UserInfo since we're
		    // deactivating the account anyway.

                    // deactivate the user
                    UserBL.deactivateUser(this.userInfo);

                    // write updated user
                    cc.getSiteManager().writeUpdatedUserInfo(this.userInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(new UserActionLogEvent(
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            UserActionLogEvent.USER_DEACTIVATED,
                            this.userInfo, getLoggedInUser().username));

                    redirectUponActionCompletion();
                    
                    break;
                case ADD_USER:
                    // set preferences on new user
                    this.userInfo.preferences = UserBL.parsePreferenceString(
                            this.pageContext.getServletContext().getInitParameter(
                                        "newUserInitialPreferences"));

                    // store new user
                    this.userInfo.id = cc.getSiteManager().writeUpdatedUserInfo(
                            this.userInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(new UserActionLogEvent(
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            UserActionLogEvent.USER_CREATED, this.userInfo,
                            getLoggedInUser().username));

                    redirectUponActionCompletion();
                    
                    break;
                case EDIT_EXISTING_USER:
		    // Change preferences on the UserInfo to match those of the
		    // session if the logged-on user is the one being modified.
		    if (this.userInfo.id == getLoggedInUser().id) {
			this.userInfo.preferences =  (UserPreferences)
                                this.pageContext.getSession().getAttribute(
                                "preferences");
		    }

                    // write updated user
                    cc.getSiteManager().writeUpdatedUserInfo(this.userInfo);

                    // update the currently logged in user if modified
                    if (this.userInfo.id == getLoggedInUser().id) {
                        this.updateCurrentlyLoggedInUser(this.userInfo);
                    }

                    // record log event
                    cc.getSiteManager().recordLogEvent(new UserActionLogEvent(
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            UserActionLogEvent.USER_MODIFIED,
                            this.userInfo, getLoggedInUser().username));

                    redirectUponActionCompletion();
                    
                    break;
                case CHANGE_PREFERENCES:
		    // Don't want to extract preferences from the session
		    // because nested tags may have deliberately changed the
		    // prefs on our UserInfo.

                    // write the updated user
                    cc.getSiteManager().writeUpdatedUserInfo(this.userInfo);

                    // update the currently logged in user if modified
                    if (this.userInfo.id == getLoggedInUser().id) {
                        this.updateCurrentlyLoggedInUser(this.userInfo);
                    }

                    // record log event
                    cc.getSiteManager().recordLogEvent(new UserActionLogEvent(
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            UserActionLogEvent.PREFS_EXPLICITLY_MODIFIED,
                            this.userInfo, getLoggedInUser().username));

                    redirectUponActionCompletion();
                    
                    break;
                case CANCEL:
                    redirectUponActionCancellation();
                    
                    break;
                case CHANGE_USER_PASSWORD:
		    // Change preferences on the UserInfo to match those of the
		    // session if the logged-on user is the one being modified.
		    if (this.userInfo.id == getLoggedInUser().id) {
			this.userInfo.preferences =  (UserPreferences)
                                this.pageContext.getSession().getAttribute(
                                "preferences");
		    }

                    // write updated user
                    cc.getSiteManager().writeUpdatedUserInfo(this.userInfo);

                    // update the currently logged in user if modified
                    if (this.userInfo.id == getLoggedInUser().id) {
                        this.updateCurrentlyLoggedInUser(this.userInfo);
                    }

                    // record log event
                    cc.getSiteManager().recordLogEvent(new UserActionLogEvent(
                            this.pageContext.getSession().getId(),
                            this.pageContext.getRequest().getServerName(),
                            UserActionLogEvent.USER_PASSWORD_CHANGED,
                            this.userInfo, getLoggedInUser().username));
                    // redirect
                    redirectUponActionCompletion();
                    
                    break;
                case CONSIDER_DEACTIVATION:
                    String servletPath = ((HttpServletRequest)
                            this.pageContext.getRequest()).getServletPath();
                    /*
                     * Generate the URL (without the query part) of the confirm
                     * deactivation page.
                     */
                    String url = getContextPath()
                            + this.confirmDeactivationPageUrl;

                    /*
                     * Geneate the query parameter that will indicate the URL
                     * to which the confirm deactivation page should redirect
                     * when deactivation is complete.
                     *
                     * This should be the same page to which THIS page should
                     * redirect upon completion because once the user is
                     * deactivated, an edit user page is no longer useful.
                     */
                    String completionParam
                          = this.confirmDeactivationCompletionPageUrlParamName
                          + "=" + URLEncoder.encode(
                              this.pageContext.getRequest().getParameter(
                                      this.completionUrlParamName), "UTF-8");

                    /*
                     * Generate the query parameter that will indicate the URL
                     * to which the confirm deactivation page should redirect
                     * when deactivation is cancelled.
                     *
                     * This should be the current page including all of the
                     * URL parameters that were present in the initial HTTP
                     * get.  Unfortunaately we must reconstruct the query from
                     * that URL by appending the userId, completionUrlParam and
                     * cancellationUrlParam.
                     */
                    String cancellationParam
                         = this.confirmDeactivationCancellationPageUrlParamName
                            + "=" + URLEncoder.encode(servletPath + "?userId="
                            + this.userId + "&"
                            + this.completionUrlParamName + "="
                            + URLEncoder.encode(
                                this.pageContext.getRequest().getParameter(
                                    this.completionUrlParamName), "UTF-8")
                            + "&" + this.cancellationUrlParamName + "="
                            + URLEncoder.encode(
                                this.pageContext.getRequest().getParameter(
                                    this.cancellationUrlParamName), "UTF-8"),
                                    "UTF-8");

                    /*
                     * Redirect to the "confirm deactivation" page providing
                     * the userId, cancellationUrlParam and completionUrlParam.
                     */
                    ((HttpServletResponse) pageContext.getResponse()
                            ).sendRedirect(url + "?" + this.userIdParamName
                                    + "=" + this.userId + "&" + completionParam
                                    + "&" + cancellationParam);
                    break;
                default:
                    // Can't happen -- all the possible values are specified
                    assert false;
            }
            
            abort();
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (DuplicateDataException ex) {
            this.setErrorFlag(DUPLICATE_USERNAME);
        } catch (InvalidDataException ex) {
            // would most likely be the result of a programming error
            throw new JspException(ex);
        } catch (InvalidModificationException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * A helper method that redirects the browser upon completion of a user
     * action.  The target page is that which is specified in the request
     * parameter indicated by 'completionUrlParamName'.
     * @throws IOException if an error occurs while redirecting
     */
    protected void redirectUponActionCompletion() throws IOException {
        ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                this.getContextPath() 
                + this.pageContext.getRequest().getParameter(
                        this.completionUrlParamName));
    }

    /**
     * A helper method that redirects the browser upon cancellation of a user
     * action.  The target page is that which is specified in the request
     * parameter indicated by 'cancellationUrlParamName'.
     * @throws IOException if an error occurs while redirecting
     */
    protected void redirectUponActionCancellation() throws IOException {
        ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                this.getContextPath() 
                + this.pageContext.getRequest().getParameter(
                        this.cancellationUrlParamName));
    }

    /**
     * Overrides {@code HtmlPage}; the current implementation unsets
     * 'triggerAction' and 'userFunction' then delegates back to the
     * superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerAction = false;
        this.userFunction = null;
        super.onReevaluation();
    }

    /** An enumeration of user functions. */
    public static enum UserFunction {
        
        /** A UserFunction representing adding a new user */
        ADD_USER {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId != UserInfo.INVALID_USER_ID) {
                    // a new user may not be added if a user id was specified
                    return false;
                } else {
                    return (page.labId == LabInfo.INVALID_LAB_ID
                            ? AuthorizationCheckerBL.canCreateProviderUser(
                                    page.getLoggedInUser(),
                                    page.getProviderInfo())
                            : AuthorizationCheckerBL.canCreateLabUser(
                                    page.getLoggedInUser(),
                                    page.getLabInfo()));
                }
            }
        },
        
        /** A UserFunction representing editing an existing user */
        EDIT_EXISTING_USER {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId == UserInfo.INVALID_USER_ID) {
                    // to edit a user, a user id must have been determined
                    return false;
                } else {
                    return AuthorizationCheckerBL.canEditUser(
                            page.getLoggedInUser(), page.getUserInfo(),
                            page.getProviderInfo());
                }
            }
        },
        
        /** A UserFunction representing changing a user's password */
        CHANGE_USER_PASSWORD {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId == UserInfo.INVALID_USER_ID) {
                    // to modify a user's password, a user id must have been
                    // determined
                    return false;
                } else {
                    return AuthorizationCheckerBL.canChangeUserPassword(
                            page.getLoggedInUser(), page.getUserInfo(),
                            page.getProviderInfo());
                }
            }
        },
        
        /**
         * A UserFunction representing considering deactivating a user without
         * actually deactivating him
         */
        CONSIDER_DEACTIVATION {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId == UserInfo.INVALID_USER_ID) {
                    // to deactivate a user, a user id must have been determined
                    return false;
                } else {
                    return AuthorizationCheckerBL.canDeactivateUser(
                            page.getLoggedInUser(), page.getUserInfo(),
                            page.getProviderInfo());
                }
            }
        },
        
        /** A UserFunction representing deactivating a user */
        DEACTIVATE_USER {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId == UserInfo.INVALID_USER_ID) {
                    // to deactivate a user, a user id must have been determined
                    return false;
                } else {
                    return AuthorizationCheckerBL.canDeactivateUser(
                            page.getLoggedInUser(), page.getUserInfo(),
                            page.getProviderInfo());
                }
            }
        },
        
        /** A UserFunction representing changing a user's preferences */
        CHANGE_PREFERENCES {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(UserPage page) {
                if (page.userId == UserInfo.INVALID_USER_ID) {
                    // to modify a user's preferences, a user id must have been
                    // determined
                    return false;
                } else {
                    return AuthorizationCheckerBL.canChangeUserPreferences(
                            page.getLoggedInUser(), page.getUserInfo(),
                            page.getProviderInfo());
                }
            }
        },
        
        /**
         * A UserFunction representing cancelling (possibly persisted) changes
         */
        CANCEL {
            
            /**
             * {@inheritDoc}
             */
            @Override
            boolean isValidForPage(@SuppressWarnings("unused") UserPage page) {
                // Cancellation is always valid
                return true;
            }
        };
        
        /**
         * Checks whether this user function is valid for the specified
         * {@code UserPage} in its current state, based on the parameters passed
         * to it and the authorization of the currently logged in user to
         * perform such functions. This method must not be invoked before its
         * argument has been processed in its {@code FETCHING_PHASE}.
         * 
         * @param page the {@code UserPage} which proposes to perform a user
         *        function of the type described by this {@code UserFunction}
         * @return true if the page may perform a function of this type on
         *         behalf of the currently logged in user, or false if it may
         *         not
         */
        abstract boolean isValidForPage(UserPage page);
    }
}
