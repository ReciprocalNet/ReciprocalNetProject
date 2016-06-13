/*
 * Reciprocal Net project
 * 
 * ProviderActionLogEvent.java
 *
 * 22-Apr-2003: dfeng wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record an action performed on provider. The
 * severity Level.INFO is assumed.
 */
public class ProviderActionLogEvent extends LogEvent {
    /**
     * @param jspSessionId JSP session id (a string) associated with the new
     *        webapp session
     * @param serverName server Name of the webapp server
     * @param userName user name editting editprovider.jsp.
     * @param providerId the unique provider id of the provider that is being
     *        modified or created
     * @param providerName the provider name of the provider that is being
     *        editted.
     * @param isModifyAction a boolean value which is true if the action is
     *        modifying existing provider info, false if the action is creating
     *        a new provider
     */
    public ProviderActionLogEvent(String jspSessionId, String serverName,
            String userName, int providerId, String providerName,
            boolean isModifyAction) {
        super();
        // Check to see what kind of action is performed by the user
        // isModifyAction is true if the action is modifying or deactivating
        // the provider, false if it is the creation of a new provider
        if (isModifyAction) {
            super.createLogRecord(Level.INFO,
                    "webapp session {0} on {1}: provider {2}"
                            + " (id {3}) modified by {4}", new Object[] {
                            jspSessionId, serverName, providerName,
                            new Integer(providerId), userName }, null);
        } else {
            super.createLogRecord(Level.INFO,
                    "webapp session {0} on {1}: provider {2}"
                            + " (id {3}) created by {4}", new Object[] {
                            jspSessionId, serverName, providerName,
                            new Integer(providerId), userName }, null);
        }
    }
}
