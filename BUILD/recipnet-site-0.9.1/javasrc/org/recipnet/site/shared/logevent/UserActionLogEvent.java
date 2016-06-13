/*
 * Reciprocal Net project
 *  
 * UserActionLogEvent.java
 *
 * 22-Apr-2003: ajooloor wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent; also changed package
 *              references to match source tree reorganization
 * 26-Apr-2005: midurbin added reason codes and corresponding messages
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;
import org.recipnet.site.shared.db.UserInfo;

/**
 * Subclass of LogEvent used to record changes in user account information. The
 * severity Level.INFO is assumed.
 */
public class UserActionLogEvent extends LogEvent {

    /** A reason code indicating the creation of a new user. */
    public static final int USER_CREATED = 0;

    /** A reason code indicating the modification of an existing user. */
    public static final int USER_MODIFIED = 1;

    /** A reason code indicating the change of a user's password. */
    public static final int USER_PASSWORD_CHANGED = 2;

    /** A reason code indicating the deactivation of an existing user */
    public static final int USER_DEACTIVATED = 3;

    /**
     * A reason code indicating the deactivation of an existing user because the
     * provider to which the user belonged was deactivated.
     */
    public static final int USER_DEACTIVATED_WITH_PROVIDER = 4;

    /**
     * A reason code indicating that a user's preferences that had changed
     * during a session and are being saved now that the session has ended.
     */
    public static final int PREFS_IMPLICITLY_MODIFIED = 5;

    /**
     * A reason code indicating that a user's preferences are being saved as a
     * response to a form submission on a JSP that allows preference setting.
     */
    public static final int PREFS_EXPLICITLY_MODIFIED = 6;

    /**
     * The only constructor.
     * 
     * @param jspSessionId JSP session id (a string) associated with the user
     *        session in which the record is modified
     * @param serverName DNS name of the computer hosting the servlet container
     *        on which the event occurred.
     * @param reasonCode one of the reason codes defined by this class to
     *        indicate which user action occurred
     * @param user the userinfo record of the user whose record has been
     *        created/modified
     * @param userName user name editting edituser.jsp.
     * @throws IllegalArgumentException if 'actionCode' is not one of the values
     *         defined by this class
     */
    public UserActionLogEvent(String jspSessionId, String serverName,
            int reasonCode, UserInfo user, String userName) {
        super();
        switch (reasonCode) {
            case USER_CREATED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) created by {4}", new Object[] {
                        jspSessionId, serverName, user.username,
                        new Integer(user.id), userName }, null);
                break;
            case USER_MODIFIED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) modified by {4}", new Object[] {
                        jspSessionId, serverName, user.username,
                        new Integer(user.id), userName }, null);
                break;
            case USER_PASSWORD_CHANGED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) had his password changed by {4}",
                        new Object[] { jspSessionId, serverName, user.username,
                                new Integer(user.id), userName }, null);
                break;
            case USER_DEACTIVATED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) had been deactivated by {4}",
                        new Object[] { jspSessionId, serverName, user.username,
                                new Integer(user.id), userName }, null);
                break;
            case USER_DEACTIVATED_WITH_PROVIDER:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) had been deactivated as a result"
                        + " of his/her provider being deactivated by {4}",
                        new Object[] { jspSessionId, serverName, user.username,
                                new Integer(user.id), userName }, null);
                break;
            case PREFS_IMPLICITLY_MODIFIED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) was updated to reflect"
                        + " preference changes made during the session",
                        new Object[] { jspSessionId, serverName, user.username,
                                new Integer(user.id) }, null);
                break;
            case PREFS_EXPLICITLY_MODIFIED:
                super.createLogRecord(Level.INFO, "webapp session {0} on {1}:"
                        + " user {2} (id {3}) had preferences updated by {4}",
                        new Object[] { jspSessionId, serverName, user.username,
                                new Integer(user.id), userName }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
