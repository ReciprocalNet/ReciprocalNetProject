/*
 * Reciprocal Net project
 *
 * RecipnetPage.java
 * 
 * 12-Mar-2004: cwestnea wrote first draft
 * 04-Jun-2004: cwestnea fixed bug #1238 in reset()
 * 19-Aug-2004: cwestnea implemented LabContext and ProviderContext for the 
 *              currently logged in user
 * 16-Nov-2004: midurbin updated reset() to reflect changes in HtmlPage
 * 20-Jan-2005: jobollin completely reworked the mechanism for including
 *              header.jsp and footer.jsp, largely by providing a new method
 *              on HtmlPage, includeResource(), that does the work. reset(),
 *              doBeforeBody(), and doAfterBody() were all modified, and some
 *              private variables and imports were removed.
 * 27-Jan-2005: midurbin moved the invocation of addExtraHtmlAttribute() to
 *              doBeforeBody()
 * 21-Apr-2005: midurbin added support to suppress the default body style when
 *              the superclass' 'styleClass' property is set
 * 29-Jun-2005: midurbin updated reset() to use a clone of the currently logged
 *              in user's UserInfo and added updateCurrentlyLoggedInUser()
 * 05-Jul-2005: midurbin updated doBeforeBody() to set the
 *              'servletPathAndQueryForReinvocation' request attribute before
 *              including the header
 * 13-Jul-2005: midurbin added 'loginPageUrl',
 *              'authorizationFailedReasonParamName' and
 *              'currentPageReinvocationUrlParamName' properties and the
 *              redirectToLogin() and checkAuthorization() methods.
 * 27-Jul-2005: midurbin updated doBeforeBody() and doAfterBody() to reflect
 *              name and spec changes
 * 11-Jan-2006: jobollin removed unused imports; overrode doStartTag() to handle
 *              setting up character encoding and the stylesheet reference;
 *              reformatted the source; removed default style attributes
 * 21-Apr-2006: jobollin made doAfterPageBody() protected
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * This tag describes the most basic {@code HtmlPage} in the site software, and
 * implements a {@code UserContext}. Additionally, it includes
 * {@code /header.jsp} and {@code /footer.jsp} in their appropriate places
 * before and after the body of the tag. The logged in user is the user that is
 * provided and this is obtained by getting the userInfo attribute from the
 * session. The lab and provider contexts of this user are also provided.
 */
public class RecipnetPage extends HtmlPage implements UserContext, LabContext,
        ProviderContext {
    /**
     * The {@code UserInfo} of the logged in user, null if no user is logged in.
     * This is a clone of the session attribute, "userInfo".
     */
    private UserInfo user;

    /**
     * The {@code LabInfo} of the logged in user, null if no user is logged in,
     * or it has not yet been requested. Retrieved from core in
     * {@code doBeforePageBody()} during the fetching phase.
     */
    private LabInfo userLab;

    /**
     * The {@code ProviderInfo} of the logged in user, null if no user is logged
     * in, or it has not yet been requested. Retrieved from core in
     * {@code doBeforePageBody()} during the fetching phase.
     */
    private ProviderInfo userProvider;

    /**
     * An optional property that indicates that page to which the browser should
     * be redirected in the event that the currently logged-in user has
     * insufficient authorization to view the current page. The URL should be
     * relative to the context path and begin with a slash '/' character. This
     * property is required for subclasses that invoke
     * {@code redirectToLogin()}.
     */
    private String loginPageUrl;

    /**
     * An optional property that indicates the URL parameter that will contain
     * the code for the authorization failure reason that resulted in the
     * redirection to the 'loginPageUrl'. Be sure that the page indicated by
     * 'loginPageUrl' is set to handle this particular parameter. This property
     * is required for subclasses that invoke {@code redirectToLogin()}.
     */
    private String authorizationFailedReasonParamName;

    /**
     * An optional property that indicates the URL parameter that will contain
     * the current page reinvocation URL. Be sure that the page indicated by
     * 'loginPageUrl' is set to handle this particular parameter. This property
     * is required for subclasses that invoke {@code redirectToLogin()}.
     */
    private String currentPageReinvocationUrlParamName;

    /**
     * {@inheritDoc}. This version sets up character encoding for the request
     * and response objects then delegates to the superclass's version
     * 
     * @see HtmlPage#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request
                = (HttpServletRequest) pageContext.getRequest();
        String stylesheetName
                = pageContext.getServletContext().getInitParameter(
                        "stylesheetName");

        // The response should specify the charset, we use UTF-8 by default
        // Additionally Java can't figure out that the request's character
        // encoding is UTF-8 on it's own, so we have to tell it
        pageContext.getResponse().setContentType("text/html; charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // UTF-8 should always be supported
            throw new UnexpectedExceptionException(ex);
        }

        if (stylesheetName != null) {
            setStylesheetUrl(request.getContextPath() + stylesheetName);
        }

        return super.doStartTag();
    }

    /** Overrides {@code HtmlPage}; initializes all member variables. */
    @Override
    protected void reset() {
        super.reset();
        this.user = (UserInfo) super.pageContext.getSession().getAttribute(
                "userInfo");
        if (this.user != null) {
            this.user = this.user.clone();
        }
        this.userLab = null;
        this.userProvider = null;
        this.loginPageUrl = null;
        this.authorizationFailedReasonParamName = null;
        this.currentPageReinvocationUrlParamName = null;
    }

    /**
     * Setter for the 'loginPageUrl' property.
     * 
     * @param url a url, relative to the context path, starting with a '/'
     *        character indicating a page to which insufficiently authorized
     *        clients should be redirected
     */
    public void setLoginPageUrl(String url) {
        this.loginPageUrl = url;
    }

    /** Getter for the 'loginPageUrl' property. */
    public String getLoginPageUrl() {
        return this.loginPageUrl;
    }

    /**
     * Setter for the 'authorizationFailedReasonParamName' property.
     * 
     * @param paramName the name of a URL parameter expected by the page
     *        indicated by the 'loginPageUrl' to contain a reason code for the
     *        authorization failure
     */
    public void setAuthorizationFailedReasonParamName(String paramName) {
        this.authorizationFailedReasonParamName = paramName;
    }

    /** Getter for the 'authorizationFailedReasonParamName' property. */
    public String getAuthorizationFailedReasonParamName() {
        return this.authorizationFailedReasonParamName;
    }

    /**
     * Setter for the 'currentPageReinvocationUrlParamName' property.
     * 
     * @param paramName the name of a URL parameter expected by the page
     *        indicated by the 'loginPageUrl' to contain a reinvocation URL for
     *        the current page
     */
    public void setCurrentPageReinvocationUrlParamName(String paramName) {
        this.currentPageReinvocationUrlParamName = paramName;
    }

    /** Getter for the 'currentPageReinvocationUrlParamName' property. */
    public String getCurrentPageReinvocationUrlParamName() {
        return this.currentPageReinvocationUrlParamName;
    }

    /**
     * Implements {@code UserContext}; returns the {@code UserInfo} for the
     * currently logged-on user, or null if there is no currently logged-on
     * user.
     */
    public UserInfo getUserInfo() {
        return this.user;
    }

    /**
     * A method to associate a newer version of the currently logged-in user
     * with the session. This method should be called by subclasses that invoke
     * {@code SiteManager.writeUpdatedUserInfo()} so that the {@code UserInfo}
     * in the session is the most recent known version.
     * 
     * @throws IllegalArgumentException if the supplied {@code UserInfo} does
     *         not represent the same user as the currently logged-in user.
     */
    protected void updateCurrentlyLoggedInUser(UserInfo user) {
        if ((this.user == null) || (this.user.id != user.id)) {
            throw new IllegalArgumentException();
        }
        this.pageContext.getSession().setAttribute("userInfo", user);
        this.pageContext.getSession().setAttribute("preferences",
                user.preferences);
    }

    /**
     * Implements {@code LabContext}; returns the {@code LabInfo} for the
     * currently logged-on user, or null if there is no currently logged-on
     * user.
     * 
     * @throws IllegalStateException if called before fetching phase
     */
    public LabInfo getLabInfo() {
        if (this.getPhase() < FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        if (this.user == null) {
            return null;
        }
        return this.userLab;
    }

    /**
     * Implements {@code ProviderContext}; returns the {@code ProviderInfo} for
     * the currently logged-on user, or null if there is no currently logged-on
     * user.
     * 
     * @throws IllegalStateException if called before fetching phase
     */
    public ProviderInfo getProviderInfo() {
        if (this.getPhase() < FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        if (this.user == null) {
            return null;
        }
        return this.userProvider;
    }

    /**
     * Overrides {@code HtmlPage}; delegates to superclass then (rendering
     * phase only) includes {@code /header.jsp} by use of
     * HtmlPage.includeResource()
     */
    @Override
    protected void doBeforePageBody()
            throws JspException, EvaluationAbortedException {
        super.doBeforePageBody();
        
        if ((this.getPhase() == FETCHING_PHASE) && (this.user != null)) {
            CoreConnector coreConnector = CoreConnector.extract(
                    this.pageContext.getServletContext());

            // get the user's provider
            if (user.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                this.userProvider = RequestCache.getProviderInfo(
                        this.pageContext.getRequest(), user.providerId);
                if (this.userProvider == null) {
                    try {
                        this.userProvider
                               = coreConnector.getSiteManager().getProviderInfo(
                                       user.providerId);
                    } catch (RemoteException ex) {
                        coreConnector.reportRemoteException(ex);
                        throw new JspException(ex);
                    } catch (OperationFailedException ex) {
                        throw new JspException(ex);
                    }
                    RequestCache.putProviderInfo(this.pageContext.getRequest(),
                            this.userProvider);
                }
            }

            // get the user's lab
            if (user.labId != LabInfo.INVALID_LAB_ID) {
                this.userLab = RequestCache.getLabInfo(
                        this.pageContext.getRequest(), user.labId);
                if (this.userLab == null) {
                    try {
                        this.userLab
                                = coreConnector.getSiteManager().getLabInfo(
                                        user.labId);
                    } catch (RemoteException ex) {
                        coreConnector.reportRemoteException(ex);
                        throw new JspException(ex);
                    } catch (OperationFailedException ex) {
                        throw new JspException(ex);
                    }
                    RequestCache.putLabInfo(this.pageContext.getRequest(),
                            this.userLab);
                }
            }
        } else if (this.getPhase() == RENDERING_PHASE) {
            ServletRequest request = pageContext.getRequest();
            
            request.setAttribute("pageTitle", getTitle());
            // add the 'servletPathAndQueryForReinvocation' to the request so
            // tags in the header may use it
            request.setAttribute(
                    "servletPathAndQueryForReinvocation",
                    this.getServletPathAndQueryForReinvocation());
            includeResource("/header.jsp");
        }
    }

    /**
     * Overrides {@code HtmlPage}; during the rendering phase includes
     * {@code /footer.jsp} by use of HtmlPage.includeResource(), then (in every
     * phase) delegates to the superclass
     */
    @Override
    protected void doAfterPageBody()
            throws JspException, EvaluationAbortedException {
        if (super.getPhase() == HtmlPage.RENDERING_PHASE) {
            includeResource("/footer.jsp");
        }
        super.doAfterPageBody();
    }

    /**
     * A helper method that determines if the current user (whether
     * authenticated or anonymous) is authorized to view this page. In order to
     * guarantee that authorization requirements are cumulative, subclasses must
     * delegate back to the superclass function. To improve efficiency, subclass
     * authors should avoid duplicating check performed by the superclass'
     * implementation.
     * <p>
     * {@code RecipnetPage}'s implementation always returns
     * {@link AuthorizationReasonMessage#USER_IS_AUTHORIZED
     * AuthorizationReasonMessage.USER_IS_AUTHORIZED}. By convention, subclasses
     * should never return {@code USER_IS_AUTHORIZED} but instead return the
     * value returned by the superclass once all of its authorization
     * requirements have been checked. In other words, subclasses may return
     * failures in addition to those returned by the superclass but must never
     * return success when the superclass would return failure.
     * <p>
     * As a general guidline, subclasses should return early, without performing
     * any of their checks if the superclass indicates a failure.
     * 
     * @return an integer code (from {@code AuthorizationReasonMessage})
     *         indicating whether the user is authorized to view this page
     */
    protected int checkAuthorization() {
        return AuthorizationReasonMessage.USER_IS_AUTHORIZED;
    }

    /**
     * A helper method that will simply redirect the browser to the page
     * indicated by the 'loginPageUrl' property. The specified 'reason' will be
     * included as a request parameter with the name indicated by the
     * 'authorizationFailedReasonParamName' property. Furthermore a parameter
     * with the name given by the 'currentPageReinvocationUrlParamName' property
     * will be included and have a URLEncoded version of the String returned by
     * the {@code getServletPathForReinvocation()} method as its value.
     * 
     * @param reason a reason code as defined on {@link
     *        AuthorizationReasonMessage AuthorizationReasonMethod}
     * @throws IOException if the redirect fails
     * @throws IllegalStateException if 'loginPageUrl',
     *         'authorizationFailedReasonParamName' or
     *         'currentPageReinvocationUrlParamName' is null
     */
    protected void sendRedirectToLogin(int reason) throws IOException {
        if ((this.loginPageUrl == null)
                || (this.authorizationFailedReasonParamName == null)
                || (this.currentPageReinvocationUrlParamName == null)) {
            throw new IllegalStateException();
        }
        try {
            ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                    ((HttpServletRequest)
                            pageContext.getRequest()).getContextPath()
                    + this.loginPageUrl
                    + "?"
                    + this.currentPageReinvocationUrlParamName
                    + "="
                    + URLEncoder.encode(
                            this.getServletPathAndQueryForReinvocation(),
                            "UTF-8")
                    + "&"
                    + this.authorizationFailedReasonParamName
                    + "="
                    + String.valueOf(reason));
        } catch (UnsupportedEncodingException ex) {
            // can't hapen because UTF-8 is always supported
            throw new UnexpectedExceptionException(ex);
        }
    }
}
