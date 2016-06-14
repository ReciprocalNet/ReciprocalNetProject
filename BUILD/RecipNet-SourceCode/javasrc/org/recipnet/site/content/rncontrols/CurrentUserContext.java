/*
 * Reciprocal Net project
 * 
 * AuthorizationChecker.java
 * 
 * 29-Jun-2005: midurbin wrote first draft
 * 21-Apr-2006: jobollin updated this class to accommodate changes to UserInfo
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.db.UserInfo;

/**
 * This tag simply provides a {@code UserContext} exposing the currently
 * logged-in user if one exists. The {@code UserInfo} provided by this context
 * is a clone of the one stored in the session and any changes made to it by
 * nested tags will have no effect either on the database nor the session.
 */
public class CurrentUserContext extends HtmlPageElement implements UserContext {

    /**
     * A clone of the {@code UserInfo} stored in the session's 'userInfo'
     * attribute, or null if there is no currently logged-in user.
     */
    private UserInfo user;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.user = null;
    }

    /**
     * {@inheritDoc}. This version gets the {@code UserInfo} associated with the
     * session; this is the one that will be provided by {@code getUserInfo()}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.user = (UserInfo) pageContext.getSession().getAttribute("userInfo");
        if (this.user != null) {
            this.user = user.clone();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    public UserInfo getUserInfo() {
        return this.user;
    }
}
