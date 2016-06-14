/*
 * Reciprocal Net project
 * 
 * UserChecker.java
 * 
 * 15-Nov-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 10-Jun-2005: midurbin added 'includeIfCurrentlyLoggedInUser' property
 * 14-Jun-2006: jobollin made this tag extend AbstractChecker and reformatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A custom tag that must be nested within a {@code UserContext} implementation
 * and suppresses or includes its body based on characteristics of the user
 * provided by that {@code UserContext}. This tag will always suppress until
 * the {@code FETCHING_PHASE}, at which point it will continue suppressing its
 * body unless the specified inclusion condition is met.
 */
public class UserChecker extends AbstractChecker {

    /**
     * A reference to the most immediately surrounding {@code UserContext}.
     * This reference is determined by {@code onRegistrationPhaseBeforeBody()}
     * and used by {@code onFetchingPhaseBeforeBody()}.
     */
    private UserContext userContext;

    /**
     * An optional property that defaults to false and indicates whether or not
     * the body of this tag should be included (rather than suppressed) if the
     * user provided by the {@code UserContext} is a lab user. This may not be
     * set to true if any other 'include' properties are already set to true.
     */
    private boolean includeIfLabUser;

    /**
     * An optional property that defaults to false and indicates whether or not
     * the body of this tag should be included (rather than suppressed) if the
     * user provided by the {@code UserContext} is a provider user. This may not
     * be set to true if any other 'include' properties are already set to true.
     */
    private boolean includeIfProviderUser;

    /**
     * An optional property that defaults to false and indicates whether or not
     * the body of this tag should be included (rather than suppressed) if the
     * user provided by the {@code UserContext} is a provider user. This may not
     * be set to true if any other 'include' properties are already set to true.
     */
    private boolean includeIfCurrentlyLoggedInUser;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.userContext = null;
        this.includeIfLabUser = false;
        this.includeIfProviderUser = false;
        this.includeIfCurrentlyLoggedInUser = false;
        inclusionConditionMet = false;
    }

    /**
     * @param include indicates whether the body should be included (rather than
     *        suppressed) if the user is a lab user
     */
    public void setIncludeIfLabUser(boolean include) {
        this.includeIfLabUser = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included
     *         (rather than suppressed) if the user is a lab user
     */
    public boolean getIncludeIfLabUser() {
        return this.includeIfLabUser;
    }

    /**
     * @param include indicates whether the body should be included (rather than
     *        suppressed) if the user is a provider user.
     */
    public void setIncludeIfProviderUser(boolean include) {
        this.includeIfProviderUser = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included
     *         (rather than suppressed) if the user is a provider user
     */
    public boolean getIncludeIfProviderUser() {
        return this.includeIfProviderUser;
    }

    /**
     * @param include indicates whether the body should be included (rather than
     *        suppressed) if the user is the currently logged-in user
     */
    public void setIncludeIfCurrentlyLoggedInUser(boolean include) {
        this.includeIfCurrentlyLoggedInUser = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included
     *         (rather than suppressed) if the user is the currently logged-in
     *         user
     */
    public boolean getIncludeIfCurrentlyLoggedInUser() {
        return this.includeIfCurrentlyLoggedInUser;
    }

    /**
     * {@inheritDoc}; this version delegates to the superclass then finds the
     * {@code UserContext} that encloses this tag
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code UserContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find UserContext
        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        if (this.userContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether the body of this tag
     * should be included or suppressed based on the inclusion properties set by
     * the JSP author and the characteristics of the {@code UserInfo} provided
     * by the {@code UserContext}.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (includeIfLabUser && AuthorizationCheckerBL.isLabUser(
                this.userContext.getUserInfo())) {
            inclusionConditionMet = true;
        } else if (includeIfProviderUser
                && AuthorizationCheckerBL.isProviderUser(
                        this.userContext.getUserInfo())) {
            inclusionConditionMet = true;
        } else if (includeIfCurrentlyLoggedInUser) {
            UserInfo currentUser
                    = (UserInfo) this.pageContext.getSession().getAttribute(
                            "userInfo");
            UserInfo contextUser = this.userContext.getUserInfo();

            inclusionConditionMet
                    = ((contextUser != null) && (currentUser != null))
                            && (currentUser.id == contextUser.id);
        } else {

            // This should already be the case; we merely reaffirm it:
            inclusionConditionMet = false;
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UserChecker dc = (UserChecker) super.generateCopy(newId, map);

        dc.userContext = (UserContext) map.get(this.userContext);

        return dc;
    }
}
