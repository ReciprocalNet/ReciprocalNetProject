/*
 * Reciprocal Net project
 * 
 * LabPage.java
 * 
 * 07-Dec-2004: mdurbin wrote first draft
 * 17-Jan-2005: jobollin modified doBeforeBody() to use addFormContent() to
 *              output hidden <input> elements
 * 27-Jan-2005: midurbin added onReevaluation()
 * 24-Feb-2005: midurbin added optimistic locking support
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 13-Jul-2005: midurbin removed redirectToLogin(), updated doBeforeBody() and
 *              checkAuthorization() to provide reason codes to the login page
 * 27-Jul-2005: midurbin updated doBeforeBody() and doAfterBody() to reflect
 *              name and spec changes; updated performLabAction() to throw
 *              EvaluationAbortedException
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
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
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.logevent.LabActionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * An extension of {@code RecipnetPage} but overrides some of the context
 * implementations. This {@code LabPage} provides the {@code LabContext} for an
 * arbitrary lab as indicated by parameters on the request object.
 * </p>
 * <p>
 * This class recognizes and requires one request parameter:
 * <ul>
 * <li> A lab id parameter, refers to the lab that is to be described by this
 * page </li>
 * </ul>
 * </p>
 * <p>
 * This class's implementation of the {@code LabContext} differs from the
 * superclass's. While the 'lab' for the superclass's implementation is the lab
 * to which the currently logged in user belongs, the 'lab' for this class is
 * based on the request parameter. The {@code ProviderContext} implementation of
 * the superclass is unchanged.
 * </p>
 * <p>
 * The timestamp of the {@code LabInfo} that is provided through the
 * {@code LabContext} is included as a parameter when the data is posted to
 * ensure that changes aren't made to the {@code LabInfo} object that weren't
 * displayed on this {@code LabPage}.
 * </p>
 */
public class LabPage extends RecipnetPage implements SiteContext {

    /**
     * An ErrorSupplier error flag that may be set if an attempt to perform a
     * function fails because the lab directory name changed without a specified
     * confirmation condition being met.
     */
    public static final int DIRECTORY_NAME_CHANGED_WITHOUT_CONFIRMATION
            = HtmlPage.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return DIRECTORY_NAME_CHANGED_WITHOUT_CONFIRMATION;
    }

    /**
     * Possible functions that may be performed by this {@code LabPage}.
     */
    public static final int NO_LAB_FUNCTION = 0;

    public static final int EDIT_EXISTING_LAB = 1;

    public static final int CANCEL_LAB_FUNCTION = 2;

    /**
     * The lab id parsed from the request parameter specified by
     * {@code labIdParamName} or {@code LabInfo.INVALID_LAB_ID} if none was
     * provided.
     */
    private int labId;

    /**
     * The {@code LabInfo} object for this page's lab. This object is fetched
     * from core during the {@code FETCHING_PHASE} and returned by
     * {@code getLabInfo()}.
     */
    private LabInfo labInfo;

    /**
     * The {@code SiteInfo} object for the site that is the home site for this
     * page's lab. This object is fetched from core during the
     * {@code FETCHING_PHASE} and returned by {@code getSiteInfo()}.
     */
    private SiteInfo siteInfo;

    /**
     * A required property that indicates the name of the request parameter that
     * contains the lab id.
     */
    private String labIdParamName;

    /**
     * A property that indicates the URL (relative to the context path) of the
     * page where the user may select a lab to edit. When a function on a lab is
     * completed, the browser will be redirected back to this page. Therefore,
     * if such a function is to be performed on this page, this value should not
     * be left as null, the default value. This value will not be escaped before
     * use, so if any escaping is needed, it is left up to the JSP author.
     */
    private String manageLabsPageUrl;

    /**
     * An optional property that defaults to null, but when set, indicates the
     * name of a request parameter that must be non-null for any action that
     * would change the name of the lab's directory. A checkbox or otherwise
     * appropriate control may be given this name and serve as the confirmation
     * for the change in a lab's directory name. When a page is unconcerned
     * about the possibility of the lab's directory changing, it may leave omit
     * this property.
     */
    private String labDirNameChangeConfirmationParamName;

    /**
     * An internal variable that indicates whether the change in a lab's
     * directory name has been confirmed or not. This is set during the
     * {@code PARSING_PHASE} and will be true if a non-null parameter with the
     * name 'labDirNameChangeConfirmationParamName' was part of the request.
     */
    private boolean allowDirNameChange;

    /**
     * An internal variable that stores the value of the lab directory name that
     * was fetched (before any user changes may be performed) when a
     * 'labDirNameChangeConfirmationParamName' has been specified. When set,
     * this is used by {@code performLabAction()} to determine if the lab
     * directory name changed in cases where confirmation is required for such
     * changes.
     */
    private String fetchedLabDirName;

    /**
     * The {@code ts} field of the {@code LabInfo}. Included by this page as a
     * hidden field in some {@code selfForm} tags and set during the parsing
     * phase or the fetching phase. Initialized to zero by {@code reset()} to
     * indicate that it is unset.
     */
    private long timestamp;

    /**
     * An internal variable that stores the function code for the function
     * triggered by a call to {@code triggerLabAction()}. This should be one of
     * the function codes defined by this class.
     */
    private int labFunction;

    /**
     * An internal boolean that indicates whether the performance of a function
     * has been triggered or not. If it has been triggered, the method
     * {@code performLabAction()} will be invoked at the end of the
     * {@code PROCESSING_PHASE}.
     */
    private boolean triggerAction;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.manageLabsPageUrl = null;
        this.labIdParamName = null;
        this.labDirNameChangeConfirmationParamName = null;
        this.fetchedLabDirName = null;
        this.timestamp = 0;
        this.labFunction = NO_LAB_FUNCTION;
    }

    /**
     * @param url the URL (relative to the context path) of the page that lists
     *        all the labs and links to pages where they may be edited
     */
    public void setManageLabsPageUrl(String url) {
        this.manageLabsPageUrl = url;
    }

    /**
     * @return a URL (relative to the context path) of the page that lists all
     *         the labs and links to pages where they may be edited
     */
    public String getManageLabsPageUrl() {
        return this.manageLabsPageUrl;
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
     * @param name the name of a request parameter that must be non-null for an
     *        action to be allowed to change the name of the lab's directory
     */
    public void setLabDirNameChangeConfirmationParamName(String name) {
        this.labDirNameChangeConfirmationParamName = name;
    }

    /**
     * @return the name of a request parameter that must be non-null for an
     *         action to be allowed to change the name of the lab's directory
     */
    public String getLabDirNameChangeConfirmationParamName() {
        return this.labDirNameChangeConfirmationParamName;
    }

    /**
     * {@inheritDoc}; this version delegates back to the superclass's
     * implementation before performing various phase-based operations. During
     * the {@code REGISTRATION_PHASE} this tag parses the request parameter. The
     * {@code LabInfo} object for the lab is fetched during the
     * {@code FETCHING_PHASE}, provided that the logged-in user is authorized
     * to see it. During the {@code RENDERING_PHASE} the labId parameter is
     * output as a hidden form field to maintain it between roundtrips.
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
                    // parse LabId if present
                    String labIdStr = this.pageContext.getRequest().getParameter(
                            this.labIdParamName);
                    if (labIdStr == null) {
                        this.labId = LabInfo.INVALID_LAB_ID;
                    } else {
                        this.labId = Integer.parseInt(labIdStr);
                        addFormField(this.labIdParamName, labIdStr);
                    }

                    if (labId == LabInfo.INVALID_LAB_ID) {
                        // for this page to be valid, a lab must be specified
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
                this.allowDirNameChange
                        = ((this.labDirNameChangeConfirmationParamName != null)
                                && (this.pageContext.getRequest().getParameter(
                                        this.labDirNameChangeConfirmationParamName)
                                        != null));

                // parse the timestamp
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
                break;
            case HtmlPage.FETCHING_PHASE:
                CoreConnector cc = CoreConnector.extract(
                        super.pageContext.getServletContext());
                
                try {
                    SiteManagerRemote siteManager = cc.getSiteManager();
                    
                    /*
                     * during the registration phase we ensured that a lab was
                     * specified
                     */
                    assert this.labId != LabInfo.INVALID_LAB_ID;

                    // fetch the lab
                    this.labInfo = RequestCache.getLabInfo(
                            this.pageContext.getRequest(), this.labId);
                    if (this.labInfo == null) {
                        this.labInfo = siteManager.getLabInfo(this.labId);
                        RequestCache.putLabInfo(this.pageContext.getRequest(),
                                this.labInfo);
                    }

                    /*
                     * set the timestamp if it wasn't already parsed and add it
                     * to the form content
                     */
                    if (this.timestamp == 0) {
                        this.timestamp = this.labInfo.ts;
                    }
                    addFormField("timestamp", String.valueOf(this.timestamp));

                    // fetch the lab's site (which is always the home site)
                    this.siteInfo = RequestCache.getSiteInfo(
                            this.pageContext.getRequest(),
                            this.labInfo.homeSiteId);
                    if (this.siteInfo == null) {
                        this.siteInfo = siteManager.getSiteInfo(
                                this.labInfo.homeSiteId);
                        RequestCache.putLocalSiteInfo(
                                this.pageContext.getRequest(), this.siteInfo);
                    }

                    if (this.labDirNameChangeConfirmationParamName != null) {
                        /*
                         * store a copy of the lab directory name so that we can
                         * determine if it changes
                         */
                        this.fetchedLabDirName = this.labInfo.directoryName;
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
                    performLabAction();
                }
                break;
        }
        super.doAfterPageBody();
    }

    /**
     * Implements {@code LabContext}. This method may not be called before the
     * {@code FETCHING_PHASE}. The {@code LabInfo} returned by this this method
     * is the lab that is the subject of this page, originates. Unlike the
     * superclass, this is NOT the lab to which the currently logged in user
     * belongs.
     * 
     * @return the {@code LabInfo} for the lab that is the subject of this page
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
     * Implements {@code SiteContext}. This method may not be called before the
     * {@code FETCHING_PHASE}. The {@code SiteInfo} returned by this this
     * method is the home site for the lab that is the subject of this page.
     * 
     * @return the {@code SiteInfo} for the home site for the lab that is the
     *         subject of this page
     * @throws IllegalStateException if this method is invoked before the
     *         {@code FETCHING_PHASE}
     */
    public SiteInfo getSiteInfo() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.siteInfo;
    }

    /**
     * {@inheritDoc}; this version determines whether the currently logged in
     * user may edit the lab that is the subject of this page.
     * 
     * @throws IllegalStateException if no lab has been fetched
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();

        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (this.labInfo != null) {
            return (AuthorizationCheckerBL.canAdministerLab(getUserInfo(),
                    super.getLabInfo(), this.labInfo) ? auth
                    : AuthorizationReasonMessage.CANNOT_EDIT_LAB);
        } else {
            // no lab was fetched
            throw new IllegalStateException();
        }
    }

    /**
     * Redirects the browser to the supplied page. The parameter 'urlAndQuery'
     * will be appended to the context path to form the complete URL
     * 
     * @throws IOException if an error is encountered while redirecting
     */
    protected void sendRedirect(String urlAndQuery) throws IOException {
        ((HttpServletResponse) this.pageContext.getResponse()).sendRedirect(
                ((HttpServletRequest) this.pageContext.getRequest()).getContextPath()
                + urlAndQuery);
    }

    /**
     * Triggers the performance of the indicated lab function for this
     * {@code LabPage} on the current lab by the currently logged in user at the
     * end of this tag's {@code PROCESSING_PHASE}. At this point the validity
     * of the selected function is verified.
     * 
     * @param labFunction the function to be performed by this page; must be one
     *        of those defined by this class
     * @throws IllegalArgumentException if the provided lab function is not one
     *         of those defined by this class
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it is too late to perform the lab
     *         action associated with the function or if another function has
     *         been triggered.
     */
    public void triggerLabAction(int labFunction) {
        if ((getPhase() == HtmlPage.RENDERING_PHASE)
                || (this.labFunction != LabPage.NO_LAB_FUNCTION)) {
            throw new IllegalStateException();
        }

        if (!isFunctionValid(labFunction)) {
            throw new IllegalArgumentException();
        }

        this.labFunction = labFunction;
        this.triggerAction = true;
    }

    /**
     * Performs the lab function for this {@code LabPage}. This method is
     * invoked by {@code doAfterPageBody()} during the {@code PROCESSING_PHASE}
     * if {@code triggerLabAction()} has been called.
     * 
     * @throws IllegalStateException if this method is called at any phase other
     *         than the {@code PROCESSING_PHASE} or if the requested function is
     *         not valid.
     * @throws JspException if any other exception is encountered while
     *         performing the lab function
     */
    public void performLabAction() throws JspException,
            EvaluationAbortedException {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        if (!areAllFieldsValid() && (this.labFunction != CANCEL_LAB_FUNCTION)) {
            return;
        }

        CoreConnector cc
                = CoreConnector.extract(super.pageContext.getServletContext());
        
        /*
         * Ensure that the timestamp from the first roundtrip is used. If the
         * timestamp is different than that of the version in the database, core
         * will throw an OptimisticLockingException
         *
         * TODO: consider handling the locking error in this class rather than
         * letting it be thrown to the error page
         */
        if (this.timestamp != 0) {
            this.labInfo.ts = this.timestamp;
        }

        try {
            switch (labFunction) {
                case NO_LAB_FUNCTION:
                    // do nothing
                    return;
                case EDIT_EXISTING_LAB:
                    // check to see if we need confirmation for a lab directory
                    // name change
                    if (!this.allowDirNameChange
                            && !((this.fetchedLabDirName == null)
                                    && (this.labInfo.directoryName == null))
                            && ((this.fetchedLabDirName == null)
                                    || !this.fetchedLabDirName.equals(
                                            this.labInfo.directoryName))
                            && ((this.labInfo.directoryName == null)
                                    || !this.labInfo.directoryName.equals(
                                            this.fetchedLabDirName))) {
                        setErrorFlag(DIRECTORY_NAME_CHANGED_WITHOUT_CONFIRMATION);
                        return;
                    }

                    cc.getSiteManager().writeUpdatedLabInfo(this.labInfo);

                    // record log event
                    cc.getSiteManager().recordLogEvent(
                            new LabActionLogEvent(
                                    this.labInfo,
                                    this.pageContext.getSession().getId(),
                                    this.pageContext.getRequest().getServerName(),
                                    this.getUserInfo().username));

                // fall through to redirect to the manage labs page
                case CANCEL_LAB_FUNCTION:
                    // redirect to the manage labs page
                    ((HttpServletResponse) this.pageContext.getResponse()
                            ).sendRedirect(((HttpServletRequest)
                                    this.pageContext.getRequest()).getContextPath()
                                    + this.manageLabsPageUrl);
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
     * Checks whether the given lab function is valid with respect to the
     * parameters passed to this page and the authorization of the currently
     * logged in user to perform such functions. This method must not be invoked
     * before the {@code FETCHING_PHASE}. This method is called by this class
     * when a function is triggered and does not need to be explicitly called
     * for this to appropriately validate functions, though it may be useful to
     * other classes, such as {@code LabActionButton}.
     * 
     * @param labFunction the function to be performed by this page; must be one
     *        of those defined by this class
     * @throws IllegalArgumentException if the provided 'labFunction' is not one
     *         of those defined by this class
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     * @return true if the function may be performed by the currently logged in
     *         user, or false if it may not
     */
    public boolean isFunctionValid(int labFunction) {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        switch (labFunction) {
            case NO_LAB_FUNCTION:
                // always a valid action
                return true;
            case EDIT_EXISTING_LAB:
                // to edit a lab, a lab id must have been provided
                if (this.labId == LabInfo.INVALID_LAB_ID) {
                    return false;
                } else {
                    return AuthorizationCheckerBL.canAdministerLab(
                            this.getUserInfo(), super.getLabInfo(),
                            this.labInfo);
                }
            case CANCEL_LAB_FUNCTION:
                // always a valid action
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * {@inheritDoc}; this version unsets 'triggerAction' and 'labFunction'
     * then delegates back to the superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerAction = false;
        this.labFunction = NO_LAB_FUNCTION;
        super.onReevaluation();
    }
}
