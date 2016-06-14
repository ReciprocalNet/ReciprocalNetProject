/*
 * Reciprocal Net Project
 *  
 * ExceptionLogEvent.java 
 * 
 * 30-Apr-2003: jrhanna wrote first draft 
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record exceptions. The log record created
 * contains general information about the exception and possibly a stack trace,
 * depending on the log record formatter utilized by lower-level code. This log
 * record has severity {@code Level.WARNING}.
 */
public class ExceptionLogEvent extends LogEvent {

    /**
     * The only constructor
     * 
     * @param jspSessionId JSP session id (a string) assigned to the webapp
     *        session.
     * @param serverName DNS name of the computer hosting the servlet container
     *        on which the event occurred.
     * @param urlFragment identifies the jsp file that failed to catch the
     *        exception.
     * @param exception The exception itself.
     */
    public ExceptionLogEvent(String jspSessionId, String serverName,
            String urlFragment, Throwable exception) {
        super();
        super.createLogRecord(Level.WARNING, "webapp session {0} on {1}:"
                + " exception encountered on {2} .", new Object[] {
                jspSessionId, serverName, urlFragment }, exception);
    }
}
