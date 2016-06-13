/*
 * Reciprocal Net project
 * 
 * AuthorizationReasonMessage.java
 *
 * 13-Jul-2005: midurbin wrote the first draft
 * 15-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A custom tag that outputs a localized message explaining why the currently
 * logged-in user (or an unauthenticated request) did not have appropriate
 * authorization to view a particular JSP. This tag defines the codes for the
 * possible reasons
 */
public class AuthorizationReasonMessage extends HtmlPageElement implements
        ErrorSupplier {

    /**
     * An error flag indicating that a reason code has been supplied and an
     * error message has been displayed. This flag will be only be set during
     * the {@code RENDERING_PHASE} if it is set at all.
     */
    public static final int AUTHORIZATION_REASON_DISPLAYED = 1 << 0;

    /**
     * A reason code that indicates that there was not an authorization failure.
     * This is not a valid code to be passed to this tag and serves as a base
     * value indicating that redirection is not neccessary.
     */
    public static final int USER_IS_AUTHORIZED = 0;

    /**
     * A reason code indicating that the current user is not authorized to
     * submit samples
     */
    public static final int CANNOT_SUBMIT_SAMPLES = 1;

    /**
     * A reason code indicating that the current user is not authorized to see
     * the requested sample
     */
    public static final int CANNOT_SEE_SAMPLE = 2;

    /**
     * A reason code indicating that the current user is not authenticated, but
     * needs to be in order to perform the requested action
     */
    public static final int AUTHENTICATION_REQUIRED = 3;

    /**
     * A reason code indicating that the current user is not authorized to edit
     * the specified user
     */
    public static final int CANNOT_EDIT_USER = 4;

    /**
     * A reason code indicating that the current user is not authorized to
     * create a laboratory uder
     */
    public static final int CANNOT_CREATE_LAB_USER = 5;

    /**
     * A reason code indicating that the current user is not authorized to
     * create a provider user
     */
    public static final int CANNOT_CREATE_PROVIDER_USER = 6;

    /**
     * A reason code indicating that the current user is not authorized to edit
     * the specified provider
     */
    public static final int CANNOT_EDIT_PROVIDER = 7;

    /**
     * A reason code indicating that the current user is not authorized to
     * create providers for the specified laboratory
     */
    public static final int CANNOT_CREATE_PROVIDER_FOR_LAB = 8;

    /**
     * A reason code indicating that the current user is not authorized to edit
     * the specified laboratory
     */
    public static final int CANNOT_EDIT_LAB = 9;

    /**
     * A reason code indicating that the current user is not authorized to edit
     * the specified sample
     */
    public static final int CANNOT_EDIT_SAMPLE = 10;

    /**
     * A reason code indicating that the current user is not authorized to
     * change his own preferences
     */
    public static final int CANNOT_CHANGE_PREFERENCES = 11;

    /**
     * A reason code indicating that the current user is not authorized to see
     * the laboratory summary
     */
    public static final int CANNOT_SEE_LAB_SUMMARY = 12;

    /**
     * A reason code indicating that the current user is not authorized to
     * administer labs for this site
     */
    public static final int CANNOT_ADMINISTER_LABS = 13;

    /**
     * A required property that indicates the URL parameter that will contain
     * the code for the authorization failure reason. The tag that initiates the
     * redirect to this page should be configured to provide one of the reason
     * codes defined by this class as the value to the parameter with this name.
     */
    private String authorizationReasonParamName;

    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.authorizationReasonParamName = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * Setter for the 'authorizationReasonParamName' property.
     * 
     * @param paramName the name of a URL parameter expected to contain a reason
     *        code for the authorization failure
     */
    public void setAuthorizationReasonParamName(String paramName) {
        this.authorizationReasonParamName = paramName;
    }

    /**
     * Getter for the 'authorizationReasonParamName' property.
     * 
     * @return the name of the authorization reason request parameter
     */
    public String getAuthorizationReasonParamName() {
        return this.authorizationReasonParamName;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         reported by this element
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return AUTHORIZATION_REASON_DISPLAYED;
    }

    /**
     * {@inheritDoc}; this version outputs the localized string associated with
     * the reason code if one was provided through the
     * 'authorizationReasonParamName' parameter.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        String reasonStr = this.pageContext.getRequest().getParameter(
                this.authorizationReasonParamName);

        if (reasonStr != null) {
            LanguageHelper lh = LanguageHelper.extract(
                    this.pageContext.getServletContext());

            try {
                out.print(lh.getAuthorizationReasonString(
                                Integer.parseInt(reasonStr),
                                this.pageContext.getRequest().getLocales(),
                                true));
                setErrorFlag(AUTHORIZATION_REASON_DISPLAYED);
            } catch (ResourceNotFoundException ex) {
                throw new JspException(ex);
            }
        }

        return super.onRenderingPhaseAfterBody(out);
    }
}
