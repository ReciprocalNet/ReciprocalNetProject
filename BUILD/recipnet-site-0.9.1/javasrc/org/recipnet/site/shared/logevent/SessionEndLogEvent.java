/*
 * Reciprocal Net project
 *  
 * SessionEndLogEvent.java
 *
 * 14-Apr-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 11-Jan-2005: jobollin changed the server name to a servlet context name and
 *              removed the "wasExplicitlyClosed" attribute
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * A subclass of LogEvent used to record the termination of a webapp session.
 * The severity Level.INFO is assumed.
 */
public class SessionEndLogEvent extends LogEvent {

    /** current JSP session id */
    public final String jspSessionId;

    /** the name of the ServletContext on which the event occurred */
    public final String contextName;

    /**
     * The only constructor.
     * 
     * @param jspSessionId JSP session id (a string) that had been assigned to
     *        the now-defunct webapp session.
     * @param contextName the name of the servlet context on which the event
     *        occurred.
     */
    public SessionEndLogEvent(String jspSessionId, String contextName) {
        super.createLogRecord(Level.INFO, "webapp session {0} closed on {1}",
                new Object[] { jspSessionId, contextName }, null);
        this.jspSessionId = jspSessionId;
        this.contextName = contextName;
    }
}
