/**
 * Reciprocal Net project 
 *
 * ClientAccessLogEvent.java
 *
 * 11-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/** 
 * A subclass of LogEvent used to record the first access of each client IP
 * to each webapp session.  The severity Level.INFO is assumed.
 */
public class ClientAccessLogEvent extends LogEvent {
        
    /** 
     * Constructs a new ClientAccessLogEvent with the specified parameters
     *
     * @param  jspSessionId the session id string of the webapp session for
     *         which a logout is being logged
     * @param  contextName the name of the ServletContext with which the
     *         specified session id is associated
     * @param  clientIP the IP number of the client, as a String
     */
    public ClientAccessLogEvent(String jspSessionId, String contextName,
            String clientIP) {
        super.createLogRecord(Level.INFO, 
                "webapp session {0} on {1}: accessed by client at {2}",
                new Object[] {jspSessionId, contextName, clientIP},
                null);
    }
}
 
