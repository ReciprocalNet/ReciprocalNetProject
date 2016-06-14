/*
 * Reciprocal Net project
 *  
 * OaiPmhLogEvent.java
 *
 * 05-Jun-2003: ajooloor wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 26-Oct-2005: jobollin removed references to webapp sessions --
 *              OaiPmhResponder doesn't use them and never will
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/** 
 * Subclass of LogEvent used to record OAI harvester requests.  The severity 
 * Level.WARNING is assumed for failures and Level.INFO is assumed for
 * successes.
 */
public class OaiPmhLogEvent extends LogEvent {

    /**
     * The number of samples that were returned to the OAI-PMH client for a
     * successful request, or zero for an unsuccessful one. 
     */
    public int countSamplesReturned = 0;

    /** 
     * The constructor to be called when an error is encountered while
     * servicing an OAI-PMH request
     *
     * @param ipAddress the IP address of the client.
     * @param serverName DNS name of the computer hosting the servlet container
     *     on which the event occurred.
     * @param oaiVerb the 'verb' element from the OAI request.
     * @param identifier the 'identifier' element from the OAI request.
     * @param set the 'set' element of the OAI request.
     * @param metadataPrefix the 'metadataPrefix' element from the OAI request.
     * @param from the 'from' element from the OAI request.
     * @param until the 'until' element from the OAI request.
     * @param resumptionToken the 'resumptionToken' element from the OAI
     *     request.
     * @param errorCode the "error code" returned to the client, as defined by
     *     the OAI-PMH spec.
     * @param errorText  the free-form "error message" returned to the client. 
     */
    public OaiPmhLogEvent(String ipAddress, String serverName, 
            String oaiVerb, String identifier, String set,
            String metadataPrefix, String from, String until, 
            String resumptionToken, String errorCode,String errorText) {
        super.createLogRecord(Level.WARNING,"webapp on {0}:"
                + " error encountered while servicing request type {1} "
		+ " for client at ip address{2} error code {3} error text{4}"
                + " OAI elements identifier {5} set {6} metadataPrefix {7}"
		+ " from {8} until {9} resumptionToken {10} ", 
                new Object[] {serverName, oaiVerb,ipAddress,
		errorCode, errorText, identifier, set, metadataPrefix, from,
		until, resumptionToken}, null);
    }

    /** 
     * The constructor to be called when an internal server error is 
     * encountered while servicing an OAI-PMH request
     *
     * @param ipAddress the IP address of the client.
     * @param serverName DNS name of the computer hosting the servlet container
     *     on which the event occurred.
     * @param oaiVerb the 'verb' element from the OAI request.
     * @param identifier the 'identifier' element from the OAI request.
     * @param set the 'set' element of the OAI request.
     * @param metadataPrefix the 'metadataPrefix' element from the OAI request.
     * @param from the 'from' element from the OAI request.
     * @param until the 'until' element from the OAI request.
     * @param resumptionToken the 'resumptionToken' element from the OAI
     *     request.
     * @param errorText the type of error encountered while servicing the 
     *     OAI request. 
     * @param exception the {@code Exception} that triggered this log event
     */
    public OaiPmhLogEvent(String ipAddress, String serverName, 
            String oaiVerb, String identifier, String set,
            String metadataPrefix, String from, String until, 
            String resumptionToken, String errorText, Throwable exception) {
        super.createLogRecord(Level.WARNING,"webapp on {0}:"
                + " error encountered while servicing request type {1} "
		+ " for client at ip address{2} error text{3} OAI elements"
	        + " identifier {4} set {5} metadataPrefix {6} from {7} "
	        + " until {8} resumptionToken {9} ", 
                new Object[] {serverName, oaiVerb,ipAddress,
		errorText,  identifier, set, metadataPrefix, from, until, 
		resumptionToken}, exception);
    }

    /** 
     * The constructor to be called when a OAI-PMH request is successfully 
     * serviced 
     *
     * @param ipAddress the IP address of the client.
     * @param serverName DNS name of the computer hosting the servlet container
     *     on which the event occurred.
     * @param oaiVerb the 'verb' element from the OAI request.
     * @param countSamplesReturned the number of samples returned for a 
     *     successful request.
     */
    public OaiPmhLogEvent(String ipAddress, String serverName, 
            String oaiVerb, int countSamplesReturned) {
        super.createLogRecord(Level.INFO," webapp session {0} on {1}: samples "
                + "returned for request type {1} from client with "
                + "ip address {2} at server {3}", new Object[] { 
                new Integer(countSamplesReturned), oaiVerb, ipAddress,
                serverName }, null);
        this.countSamplesReturned = countSamplesReturned;
    }
}
