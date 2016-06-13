/*
 * Reciprocal Net project
 *  
 * SessionBeginLogEvent.java
 *
 * 14-Apr-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 11-Jan-2005: jobollin modified this log event to remove the client IP and
 *              to change the server name to a context name
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record the beginning of a new webapp session.
 * The severity Level.INFO is assumed.
 */
public class SessionBeginLogEvent extends LogEvent {

    /** current JSP session id */
    public final String jspSessionId;

    /** the name of the servlet context on which the event occurred */
    public final String contextName;

    /**
     * The only constructor.
     * 
     * @param jspSessionId JSP session id (a string) assigned to the new webapp
     *        session.
     * @param contextName the name of the ServletContext on which the event
     *        occurred.
     */
    public SessionBeginLogEvent(String jspSessionId, String contextName) {
        super.createLogRecord(Level.INFO, "webapp session {0} created on {1}",
                new Object[] { jspSessionId, contextName }, null);
        this.jspSessionId = jspSessionId;
        this.contextName = contextName;
    }
}
