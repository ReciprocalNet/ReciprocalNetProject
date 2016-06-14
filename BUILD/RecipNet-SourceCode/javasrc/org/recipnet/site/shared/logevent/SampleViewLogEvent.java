/*
 * Reciprocal Net project
 *  
 * SampleViewLogEvent.java
 *
 * 21-Apr-2003: adharurk wrote first draft.
 * 17-Jun-2003: midurbin added labId field
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record a sample view, by visiting sample.jsp or
 * showsample.jsp.
 */
public class SampleViewLogEvent extends LogEvent {

    /** sample's id */
    public int sampleId;

    /** sample's lab */
    public int labId;

    /** current JSP session id */
    public String JSPSessionId;

    /**
     * true: if the sample is being edited (on lab/sample.jsp) false: if the
     * sample is being viewed (on showsample.jsp)
     */
    public boolean viewOrEdit;

    /**
     * The only constructor
     * 
     * @param sampleId id of the sample being viewed on sample.jsp or
     *        showsample.jsp
     * @param localLabId local lab id of the sample being viewed on sample.jsp
     *        or showsample.jsp
     * @param shortLabName name of the lab to which sample belongs
     * @param labId id of the lab to which the sample belongs
     * @param JSPSessionId JSP session id (a string) associated with the user
     *        session with which the attempt was/is associated.
     * @param serverName DNS name of the computer hosting the servlet container
     *        on which the event occurred.
     * @param viewOrEdit true if the sample is being edited by the user (such as
     *        a pageview on lab/sample.jsp), false if the sample is just being
     *        viewed (such as a pageview on showsample.jsp).
     */
    public SampleViewLogEvent(int sampleId, String shortLabName, int labId,
            String localLabId, String JSPSessionId, String serverName,
            boolean viewOrEdit) {
        super();
        this.sampleId = sampleId;
        this.labId = labId;
        this.JSPSessionId = JSPSessionId;
        this.viewOrEdit = viewOrEdit;

        if (viewOrEdit) {
            super.createLogRecord(Level.FINE,
                    "webapp session {0} on {1}: sample {2} ({3} {4}) edited",
                    new Object[] { JSPSessionId, serverName,
                            new Integer(sampleId), shortLabName, localLabId },
                    null);
        } else {
            super.createLogRecord(Level.FINE,
                    "webapp session {0} on {1}: sample {2} ({3} {4}) viewed",
                    new Object[] { JSPSessionId, serverName,
                            new Integer(sampleId), shortLabName, localLabId },
                    null);
        }

    }
}
