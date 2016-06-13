/*
 * Reciprocal Net project
 * 
 * LoginButton.java
 * 
 * 07-Jun-2004: cwestnea wrote first draft
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 11-Jan-2005: jobollin modified onClick() to use the new AuthenticationLogger
 * 26-Apr-2005: midurbin modified onClick() to update user preferences session
 *              attribute
 * 18-Aug-2005: midurbin updated onClick() to initialize the user's preferences
 *              if they haven't already been initialized
 * 16-Sep-2005: midurbin replaced a call to UserInfo.checkPassword() with a
 *              call to UserBL.checkPassword()
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.AuthenticationLogger;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * Extends {@code ButtonHtmlControl} to authenticate and log in the user. After
 * the user is authenticated, their {@code UserInfo} object is saved in the
 * session as the attribute {@code userInfo}. By default, the button's label is
 * set to be "Login" however this may be overridden by the label attribute.
 */
public class LoginButton extends ButtonHtmlControl implements ErrorSupplier {

    /** Error flag indicating that the user was could not be authenticated. */
    public static final int AUTHENTICATION_FAILURE = 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return AUTHENTICATION_FAILURE;
    }

    /**
     * Required attribute; the username of the user attempting to login.
     */
    private String username;

    /**
     * Required attribute; the password of the user attempting to login.
     */
    private String password;

    /**
     * Required attribute; if the user cannot be redirected to
     * {@code targetPageParamName} or {@code targetPageIfCanSeeLabSummary} then
     * the user is redirected to the page specified in this attribute. This page
     * is relative to the servlet context.
     */
    private String targetPage;

    /**
     * Optional attribute; if the user cannot be redirected to
     * {@code targetPageParamName} then, if this attribute is set and the user
     * is authorized to see the lab summary, the user is redirected to the page
     * specified in this attribute. This page is relative to the servlet
     * context.
     */
    private String targetPageIfCanSeeLabSummary;

    /**
     * Optional attribute; if this attribute is set and the parameter it points
     * to is valid, the user is redirected to the page specified in the
     * parameter. This page is relative to the servlet context.
     */
    private String targetPageParamName;

    /**
     * Keeps track of the specific errors detected while parsing the input for
     * this control. Error code will be the logical OR of one or more of the
     * errors codes defined by this class or its subclass or may be the reserved
     * value of zero, indicating that no error has occurred. {@code errorCode}
     * is initialized by {@code reset()} and modified by {@code onClick()} and
     * {@code setErrorFlag()}. This field is used in the implementation of
     * {@code ErrorSupplier}.
     */
    private int errorCode;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        setLabel("Login");
        this.username = null;
        this.password = null;
        this.targetPage = null;
        this.targetPageIfCanSeeLabSummary = null;
        this.targetPageParamName = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /** @return the username */
    public String getUsername() {
        return this.username;
    }

    /** @param username the username of the user */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the password */
    public String getPassword() {
        return this.password;
    }

    /** @param password the password of the user */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return the page of last resort */
    public String getTargetPage() {
        return targetPage;
    }

    /** @param targetPage the page of last resort */
    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }

    /**
     * @return the page to go if the user can see lab summary and the page param
     *         is bad
     */
    public String getTargetPageIfCanSeeLabSummary() {
        return targetPageIfCanSeeLabSummary;
    }

    /**
     * @param targetPageIfCanSeeLabSummary the page to go if the user can see
     *        lab summary and the page param is bad
     */
    public void setTargetPageIfCanSeeLabSummary(
            String targetPageIfCanSeeLabSummary) {
        this.targetPageIfCanSeeLabSummary = targetPageIfCanSeeLabSummary;
    }

    /** @return the name of the parameter to get the preferred target page */
    public String getTargetPageParamName() {
        return targetPageParamName;
    }

    /**
     * @param targetPageParamName the name of the parameter to get the preferred
     *        target page
     */
    public void setTargetPageParamName(String targetPageParamName) {
        this.targetPageParamName = targetPageParamName;
    }

    /**
     * Implements {@code ErrorSupplier}; possible error codes are:
     * {@code NO_ERROR_REPORTED} and {@code AUTHENTICATION_FAILURE}
     * 
     * @return whether or not an error has occured while logging on. Valid after
     *         the processing phase.
     */
    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    @Override
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * {@inheritDoc}; this version performs the login when the user clicks the
     * button. The user's {@code UserInfo} is stored in the session as
     * "userInfo" and his preferences object as "preferences" The
     * redirectionPage is then set, and is determined by first using the
     * {@code origURL} parameter if it is not null, and then using
     * {@code index.jsp} for a less priviledged user, or {@code lab/summary.jsp}
     * for a more priviledged user.
     */
    @Override
    protected void onClick(PageContext pageContext) throws JspException {
        if ((this.username == null) || (this.password == null)) {
            setErrorFlag(AUTHENTICATION_FAILURE);
            return;
        }

        try {
            // information for logging
            AuthenticationLogger logger
                    = AuthenticationLogger.getAuthenticationLogger(
                            pageContext.getServletContext());
            CoreConnector cc
                    = CoreConnector.extract(pageContext.getServletContext());
            HttpSession session = pageContext.getSession();
            UserInfo user;

            try {
                user = cc.getSiteManager().getUserInfo(this.username);
            } catch (RemoteException re) {
                cc.reportRemoteException(re);
                throw new JspException(re);
            } catch (ResourceNotFoundException ex) {

                // Username not known -- log an event for invalid user.
                logger.logLoginEvent(session, this.username,
                        UserInfo.INVALID_USER_ID, false);

                setErrorFlag(AUTHENTICATION_FAILURE);
                return;
            }

            if (!UserBL.checkPassword(user, this.password) || !user.isActive) {

                /*
                 * Supplied password does not match user's. log unsuccessful
                 * login event.
                 */
                logger.logLoginEvent(session, this.username, user.id, false);
                setErrorFlag(AUTHENTICATION_FAILURE);
            } else {

                // log successful login event.
                logger.logLoginEvent(session, this.username, user.id, true);

                if (!UserBL.havePreferencesBeenInitialized(user.preferences)) {
                    /*
                     * initialize the preferences according to the servlet
                     * initialization parameter
                     */
                    user.preferences = UserBL.parsePreferenceString(
                            pageContext.getSession().getServletContext().getInitParameter(
                                    "newUserInitialPreferences"));
                }

                // attach the user and preferences to the session
                pageContext.getSession().setAttribute("userInfo", user);
                pageContext.getSession().setAttribute("preferences",
                        user.preferences);

                // where should we redirect the user
                String origUrl = pageContext.getRequest().getParameter(
                        this.targetPageParamName);
                String redirectionPage;

                if ((origUrl != null) && !origUrl.equals("")) {
                    redirectionPage = origUrl;
                } else if (AuthorizationCheckerBL.canSeeLabSummary(user)) {
                    redirectionPage = this.targetPageIfCanSeeLabSummary;
                } else {
                    redirectionPage = this.targetPage;
                }
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                        getPage().getContextPath() + redirectionPage);
            }
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (IllegalArgumentException iae) {
            throw new JspException(iae);
        }
    }

    /**
     * {@inheritDoc}; this version will skip the rest of the page if we have
     * redirected.
     * 
     * @return {@code SKIP_PAGE} if no error occured while logging in, the
     *         superclass' return value otherwise.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        // if we are redirecting, we need to return SKIP_PAGE
        return ((getErrorCode() == NO_ERROR_REPORTED) && getValueAsBoolean())
                ? SKIP_PAGE
                : super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}; this version copies the username and password between
     * phases.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        LoginButton src = (LoginButton) source;
        this.username = src.username;
        this.password = src.password;
    }
}
