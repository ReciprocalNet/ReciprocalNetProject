/*
 * Reciprocal Net project
 * @(#)ProcessedIsmCM.java
 *
 * 03-Oct-2002: ekoperda wrote first draft
 * 31-Oct-2002: ekoperda wrote second draft for better interoperability with
 *              the new ReceivedMessageAgent class
 */

package org.recipnet.site.core.msg;

/**
 * A core message that a core module sends to Site Manager when it has finished
 * processing an ISM, or failed trying to do so.  Site Manager in turn forwards
 * the message to MessagingAgent.processProcessedIsmCM for handling.
 */
public class ProcessedIsmCM extends CoreMessage {
    public boolean succeeded;
    public boolean shouldCheckIfCurrent;
    public boolean shouldUpdateState;
    public boolean shouldRevertState;
    public boolean shouldUpdateDb;
    public boolean shouldLogMessage;
    public boolean shouldClearFile;

    public InterSiteMessage ism;
    public String message;
    public Exception exception;

    private ProcessedIsmCM(boolean succeeded, boolean shouldCheckIfCurrent, 
            boolean shouldUpdateState, boolean shouldRevertState, 
            boolean shouldUpdateDb, boolean shouldLogMessage, 
            boolean shouldClearFile, InterSiteMessage ism, String message, 
            Exception exception) {
	super();
	this.succeeded = succeeded;
	this.shouldCheckIfCurrent = shouldCheckIfCurrent;
	this.shouldUpdateState = shouldUpdateState;
	this.shouldRevertState = shouldRevertState;
	this.shouldUpdateDb = shouldUpdateDb;
	this.shouldLogMessage = shouldLogMessage;
	this.shouldClearFile = shouldClearFile;
	this.ism = ism;
	this.message = message;
	this.exception = exception;
    }

    public static ProcessedIsmCM success(InterSiteMessage ism) {
	return new ProcessedIsmCM(true, true, true, false, true, true, true, 
                ism, null, null);
    }

    public static ProcessedIsmCM success(InterSiteMessage ism, 
            String message) {
	return new ProcessedIsmCM(true, true, true, false, true, true, true, 
                ism, message, null);
    }

    public static ProcessedIsmCM failure(InterSiteMessage ism, 
            String message) {
	return new ProcessedIsmCM(false, true, false, true, false, true, false,
                ism, message, null);
    }

    public static ProcessedIsmCM failure(InterSiteMessage ism, 
            Exception exception) {
	return new ProcessedIsmCM(false, true, false, true, false, true, false,
                ism, null, exception);
    }

    public static ProcessedIsmCM ignoreFailure(InterSiteMessage ism, 
            String message) {
	return new ProcessedIsmCM(false, true, true, false, true, true, true, 
                ism, message, null);
    }

    public static ProcessedIsmCM clearOldFile(InterSiteMessage ism) {
	return new ProcessedIsmCM(false, false, false, false, false, false, 
                true, ism, null, null);
    }

    public static ProcessedIsmCM fromLocal(InterSiteMessage ism) {
	return new ProcessedIsmCM(true, false, false, false, true, false, 
                false, ism, null, null);
    }
}

