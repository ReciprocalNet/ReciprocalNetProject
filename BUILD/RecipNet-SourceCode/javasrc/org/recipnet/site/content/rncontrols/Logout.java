/*
 * Reciprocal Net project
 * 
 * Logout.java
 * 
 * 13-Jul-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.AuthenticationLogger;

/**
 * A custom tag that causes the current session to be invalidated and records a
 * {@code LogoutLogEvent} during any phase in which it is evaluated.  This
 * tag recognizes the {@code SuppressionContext} and will not invalidate
 * the session as long as it is suppressed.  This tag should be immediately
 * followed by an {@code HtmlRedirect} tag to prevent disrupting other
 * tags on the page that are dependent upon the current session.
 */
public class Logout extends HtmlPageElement {

    /**
     * The most immediate {@code SuppressionContext} implementation;
     * determined by {@code doBeforeBody()} during the
     * {@code REGISTRATION_PHASE} and used to prevent this tag from
     * invalidating the session as long as it is suppressed.
     */
    private SuppressionContext suppressionContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.suppressionContext = null;
    }

    /**
     * {@inheritDoc}; this version
     * invokes {@link #logout()} unless it's supprssed, as determined by
     * consulting the {@code SuppressionContext} that's found during the
     * {@code REGISTRATION_PHASE}.
     */
    @Override
    public int doStartTag() throws JspException {
        int rc = super.doStartTag();
        Logout realElement = (Logout) getRealElement();
        
        if (getPage().getPhase() == HtmlPage.REGISTRATION_PHASE) {
            realElement.suppressionContext
                    = findRealAncestorWithClass(this,
                            SuppressionContext.class);
        }
        if ((realElement.suppressionContext == null)
                || !realElement.suppressionContext.
                        isTagsBodySuppressedThisPhase()) {
            realElement.logout();
        }
        
        return rc;
    }

    /**
     * If there is a currently logged-in user, this method invalidates the
     * session and reports a {@code LogoutLogEvent}.
     * @throws JspException wrapping an IOException if one occurs
     */
    protected void logout() throws JspException {
        UserInfo currentUser
                = (UserInfo) pageContext.getSession().getAttribute("userInfo");
        
        if (currentUser != null) {
            try {
                // information for logging
                AuthenticationLogger logger =
                        AuthenticationLogger.getAuthenticationLogger(
                                pageContext.getServletContext());
                HttpSession session = pageContext.getSession();

                // log successful logout event.
                logger.logLogoutEvent(session, currentUser);
                pageContext.getSession().invalidate();
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        Logout dc = (Logout) super.generateCopy(newId, map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        
        return dc;
    }
    
}
