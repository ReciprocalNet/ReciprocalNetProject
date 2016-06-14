/**
 * Reciprocal Net project 
 *
 * LogoutLogEvent.java
 *
 * 11-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/** 
 * A subclass of LogEvent used to record explicit web app logouts (as
 * opposed to container-initiated session closure).  The severity Level.INFO
 * is assumed.
 */
public class LogoutLogEvent extends LogEvent {
        
    /** 
     * Constructs a new LogoutLogEvent with the specified parameters
     *
     * @param  jspSessionId the session id string of the webapp session for
     *         which a logout is being logged
     * @param  contextName the name of the ServletContext with which the
     *         specified session id is associated
     * @param  userName the username of the user who logged out
     * @param  userId the user id of the user who logged out
     */
    public LogoutLogEvent(String jspSessionId, String contextName,
            String userName, int userId) {
        super.createLogRecord(Level.INFO, 
                "webapp session {0} on {1}: user {2}({3}) logged out",
                new Object[] {jspSessionId, contextName, userName,
                              new Integer(userId)},
                null);
    }
}
 
