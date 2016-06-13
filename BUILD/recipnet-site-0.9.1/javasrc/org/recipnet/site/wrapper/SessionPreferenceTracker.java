/*
 * Reciprocal Net project
 * @(#)SessionPreferenceTracker.java
 * 
 * 26-Apr-2005: mdurbin wrote first draft
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 */

package org.recipnet.site.wrapper;

import java.rmi.RemoteException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.ExceptionLogEvent;
import org.recipnet.site.shared.logevent.UserActionLogEvent;

/**
 * An <code>HttpSessionListener</code> that sets the default preferences at the
 * beginning of an unauthenticated session and stores modified user preferences
 * to the database at the end of the session.  This class uses the session
 * attribute 'preferences' to store the preferences for this session, and
 * expects the session attribute 'userInfo' to contain the
 * <code>UserInfo</code> for the currently logged-in user, if one exists.  If
 * one does exist, this class expects that the 'preference' attribute point to
 * the <code>UserPreferences</code> object referenced within the
 * <code>UserInfo</code>.
 */
public class SessionPreferenceTracker implements HttpSessionListener {

    /**
     * Invoked when the session is created; this method initializes the session
     * attribute 'preferences' to contain a default
     * <code>UserPreferences</code> object.  The specific preference settings
     * may be customized in the context parameter 'defaultUserPreferences'.
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        UserPreferences defaultPreferences
                = UserBL.parsePreferenceString(
                        session.getServletContext().getInitParameter(
                                "unauthenticatedPreferences"));
        session.setAttribute("preferences", defaultPreferences);
    }

    /**
     * Invokes whent he session ends; this method stores any modified
     * preferences to the database if they are associated with a particular
     * user and are different than the current values.  If any exceptions are
     * thrown while writing to the database, they are silently ignored and the
     * preference changes are lost.
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        UserInfo user = (UserInfo) session.getAttribute("userInfo");
        if (user == null) {
            // nothing to do, session preferences die with session 
            return;
        }
        UserPreferences preferences = (UserPreferences) session.getAttribute(
                "preferences");

        // during login the 'preferences' attribute should be reference to the
        // preferences member variable within the UserInfo stored in the 'user'
        // attribute
        assert preferences == user.preferences;

        CoreConnector cc = CoreConnector.extract(session.getServletContext());
        try {
            UserInfo currentUser = cc.getSiteManager().getUserInfo(user.id);
            if (currentUser.preferences != preferences) {
                // the preferences are different than those saved,
                // update the current user version to reflect the preferences
                // from this session and write it back to the database
                currentUser.preferences = preferences;
                cc.getSiteManager().writeUpdatedUserInfo(currentUser);
                
                // record log event
                cc.getSiteManager().recordLogEvent(
                        new UserActionLogEvent(session.getId(),
                        session.getServletContext().getServletContextName(),
                        UserActionLogEvent.PREFS_IMPLICITLY_MODIFIED,
                        currentUser, null));
            }
        } catch (RemoteException ex) {
            // can't communicate with core, report it to CoreConnector and
            // fail silenty because updating preferences isn't a critical
            // feature
            cc.reportRemoteException(ex);
        } catch (Exception ex) {
            try {
                cc.getSiteManager().recordLogEvent(new ExceptionLogEvent(
                        session.getId(),
                        session.getServletContext().getServletContextName(),
                        "session destroyed", ex));
            } catch (Exception ex2) {
                // some failure occured while trying to preserve the
                // preferences set during this session and then another
                // occurred while trying to report the first -- since this
                // isn't a critical feature, simply ignore the failure
            }
        }
    }
}
