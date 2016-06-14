/*
 * Reciprocal Net project 
 * 
 * LabActionLogEvent.java
 *
 * 23-Apr-2003: ajooloor wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

import org.recipnet.site.shared.db.LabInfo;

/**
 * Subclass of LogEvent used to record changes in Lab information The severity
 * Level.INFO is assumed.
 */
public class LabActionLogEvent extends LogEvent {

    /**
     * Initializes a new {@code LabActionLogEvent} with the specified parameters
     * 
     * @param lab the labinfo record of the lab whose record has been modified
     * @param jspSessionId JSP session id (a string) associated with the user
     *        session in which the record is modified
     * @param userName user name editting edituser.jsp.
     * @param serverName DNS name of the computer hosting the servlet container
     *        on which the event occurred.
     */
    public LabActionLogEvent(LabInfo lab, String jspSessionId,
            String serverName, String userName) {
        super();
        super.createLogRecord(Level.INFO,
                "webapp session {0} on {1}: lab {2} (id {3}) modified by {4}",
                new Object[] { jspSessionId, serverName, lab.name,
                        new Integer(lab.id), userName }, null);
    }
}
