/*
 * Reciprocal Net project
 *  
 * SampleActionLogEvent.java
 *
 * 23-Apr-2003: adharurk wrote first draft.
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record a workflow action on a sample.
 */
public class SampleActionLogEvent extends LogEvent {

    /**
     * The only constructor. The sample's associated lab name is not required
     * because this would pose an undue performance burden on callers
     * 
     * @param sampleId id of the sample on which the action was performed.
     * @param localLabId local lab id of the sample.on which the action was
     *        performed.
     * @param JSPSessionId JSP session id (a string) associated with the user
     *        session.
     * @param serverName DNS name of the computer hosting the servlet container
     *        on which the event occurred.
     * @param userName user-name of the user who performed the action on the
     *        sample.
     * @param actionName textual description of the kind of action that was
     *        performed on the sample.
     */
    public SampleActionLogEvent(int sampleId, String localLabId,
            String JSPSessionId, String serverName, String userName,
            String actionName) {
        super();
        super.createLogRecord(Level.INFO,
                "webapp session {0} on {1}: {2} {3} sample id {4} ({5}) ",
                new Object[] { JSPSessionId, serverName, userName, actionName,
                        new Integer(sampleId), localLabId }, null);
    }
}
