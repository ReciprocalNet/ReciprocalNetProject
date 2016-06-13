/*
 * Reciprocal Net project
 * 
 * RedirectToLogin.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 05-Jul-2005: midurbin changed call to HtmlPage.getServletPathAndQuery() to
 *              reflect method name change
 * 13-Jul-2005: midurbin added 'authorizationReasonParamName' and
 *              'authorizationReasonCode' properties
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.common.controls.HtmlRedirect;

/**
 * <p>
 * This is an extension of {@code RedirectHtmlControl} that redirects the
 * browser to the login page, appending the current page's reinvocation ULR as a
 * parameter with the name provided by the property 'returnUrlParamName'.
 * </p><p>
 * Unlike {@code HtmlRedirect} this tag really only has one purpose and should
 * be nested within an {@code AuthorizationChecker} tag for pages where a
 * certain level of authorization is required, but no {@code HtmlPage} subclass
 * handles the checking. The target page may have a {@code LoginButton} tag
 * whose 'targetPageParamName' is set to the same value as this tag's
 * 'returnUrlParamName'.
 * </p><p>
 * The TLD for this tag should only expose four properties (all required):
 * <ul>
 * <li><strong>target</strong>: the URL of the login page (relative to the
 * context path)</li>
 * <li><strong>returnUrlParamName</strong>: the name of the parameter that
 * will be part of the reqest of the target. (ie, "origUrl") The value of the
 * included query-line parameter will be the path and query used to get the page
 * that includes this tag (relative to the context path).</li>
 * <li><strong>authorizationReasonParamName</strong>: the name of the
 * parameter that will convey the reason for redirection to login to the target
 * page.</li>
 * <li><strong>authorizationReasonCode</strong>: the code (defined on
 * {@code AuthorizationReasonMessage}) that identifies the reason for
 * redirection. This typically corresponds with the "checker" tag that surrounds
 * this tag.</li>
 * </ul>
 */
public class RedirectToLogin extends HtmlRedirect {

    /**
     * A required property indicating the name of the parameter that will be
     * passed to the target page with a value equal to the URL of the page
     * containing this tag.
     */
    private String returnUrlParamName;

    /**
     * A required property that indicates the URL parameter that will contain
     * the code for the authorization failure reason. The page that is the
     * 'target' of this {@code HtmlRedirect} tag should recognize this
     * particular parameter name. tag.
     */
    private String authorizationReasonParamName;

    /**
     * A required property that must be set to one of the codes defined on
     * {@link AuthorizationReasonMessage AuthorizationReasonMessage} that
     * indicates why authorization is required for the current page. In normal
     * use, this should correspond with the {@code *Checker} tag that surrounds
     * this tag.
     */
    private String authorizationReasonCodeStr;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.returnUrlParamName = null;
        this.authorizationReasonParamName = null;
        this.authorizationReasonCodeStr = null;
    }

    /**
     * Sets the 'returnUrlParamName' property.
     * 
     * @param paramName the name of the request parameter that will contain the
     *        URL for the page from which the user was redirected
     */
    public void setReturnUrlParamName(String paramName) {
        addRequestParam(paramName,
                getPage().getServletPathAndQueryForReinvocation());
        this.returnUrlParamName = paramName;
    }

    /**
     * Gets the 'returnUrlParamName' property.
     * 
     * @return the name of the request parameter that will contain the URL for
     *         the page from which the user was redirected
     */
    public String getReturnUrlParamName() {
        return this.returnUrlParamName;
    }

    /**
     * Setter for the 'authorizationReasonParamName' property.
     * 
     * @param paramName the name of a URL parameter expected by the 'target'
     *        page to contain a reason code for the authorization failure
     */
    public void setAuthorizationReasonParamName(String paramName) {
        this.authorizationReasonParamName = paramName;
        if ((this.authorizationReasonParamName != null)
                && (this.authorizationReasonCodeStr != null)) {
            addRequestParam(paramName, this.authorizationReasonCodeStr);
        }
    }

    /** Getter for the 'authorizationReasonParamName' property. */
    public String getAuthorizationReasonParamName() {
        return this.authorizationReasonParamName;
    }

    /**
     * Setter for the 'authorizationReasonCode' property.
     * 
     * @param reason one of the reason codes on
     *        {@code AuthorizationReasonMessage}
     */
    public void setAuthorizationReasonCode(int reason) {
        this.authorizationReasonCodeStr = String.valueOf(reason);
        if ((this.authorizationReasonParamName != null)
                && (this.authorizationReasonCodeStr != null)) {
            addRequestParam(this.authorizationReasonParamName,
                    this.authorizationReasonCodeStr);
        }
    }

    /** Getter for 'authorizationReasonCode'. */
    public int getAuthorizationReasonCode() {
        return Integer.parseInt(this.authorizationReasonCodeStr);
    }

}
