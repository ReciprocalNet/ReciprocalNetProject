/*
 * Reciprocal Net project
 * @(#)TransferFailedEvent.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 * 11-Nov-2005: midurbin added the INVALID_FILENAME reason
 */
package org.recipnet.site.applet.uploader;
import java.util.EventObject;

/**
 * An event message that is generated whenever the transfer of a specific file
 * failed for some reason.  Reason codes are available to identify the specific
 * reason, and additional information about the error may be encapsulated
 * within the message.
 */
public class TransferFailedEvent extends EventObject {
    public static enum Reason {
        /** 
         * Indicates that the remote HTTP server returned an HTTP error code 
         */
        SERVER_ERROR,

        /**
         * Indicates that the user cancelled the transfer of this file by
         * clicking on the Cancel button.
         */
        USER_CANCELLED,

        /**
         * Indicates that this file was not transferred because it has a name
         * identical to one that was transferred earlier in the session.
         */
        DUPLICATE_FILENAME,

        /** 
         * Indicates that this file was not transferred because it has an
         * invalid name.
         */
        INVALID_FILENAME,

        /**
         * Indicates that this file was not transferred because some sort of
         * Java exception was encountered.
         */
        JAVA_EXCEPTION
    }

    /**
     * Identifies the specific reason the transfer failed.  Specific meanings
     * of reason codes are documented elsewhere.
     */
    public Reason reason;

    /**
     * The name of the file whose transfer failed.  This value is available for
     * all reasons.
     */
    public String filename;

    /**
     * The numeric response code returned by the server, as defined by the
     * HTTP spec.  This value is meaningless and should be ignored unless
     * <code>reason</code> is<code>SERVER_ERROR</code>.
     */
    public int httpResponseCode;

    /**
     * Any extra texy that may have been present on the response line
     * returned by the server.  This value is meaningless and should be 
     * ignored unless <code>reason</code> is <code>SERVER_ERROR</code>.
     */
    public String httpResponseMessage;

    /**
     * Contains the body of the HTTP response that was returned by the server.
     * Depending upon the server's implementation, this may include a detailed
     * description of the problem or a stack trace.  This value is meaningless
     * and should be ignored unless <code>reason</code> is 
     * <code>SERVER_ERROR</code>.
     */
    public String httpResponseData;

    /**
     * A reference to the specific Java exception that was encountered.  This
     * value is meaningless unless <code>reason</code> is
     * <code>JAVA_EXCEPTION</code>.
     */
    public Throwable exception;

    /**
     * Constructor.  Simply sets the identically-named member variables.
     */
    public TransferFailedEvent(Object source, Reason reason, String filename, 
            int httpResponseCode, String httpResponseMessage, 
            String httpResponseData, Throwable exception) {
	super(source);
        this.reason = reason;
	this.filename = filename;
	this.httpResponseCode = httpResponseCode;
	this.httpResponseMessage = httpResponseMessage;
	this.httpResponseData = httpResponseData;
        this.exception = exception;
    }
}
