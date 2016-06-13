/*
 * Reciprocal Net project
 * @(#)TransferProgressEvent.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 */
package org.recipnet.site.applet.uploader;
import java.util.EventObject;

/**
 * A <code>TransferEvent</code> subclass that is generated repeatedly as a file
 * is being transferred to the server.  The information contained within this
 * message is intended to be presented to the user as a running status display.
 * For any particular file transfer, at least two of these messages are
 * generated: one at the start of the transfer and another at the end.  
 * Additional progress messages may be sent in the middle as well, but the
 * interval between these is not guaranteed.
 */
public class TransferProgressEvent extends EventObject {
    /** The name of the file presently being transferred. */
    public String filename;
    
    /** The number of bytes transferred to the server so far. */
    public long bytesTransferred;

    /**
     * The total number of data bytes in the file presently being transferred.
     */
    public long bytesTotal;

    /** 
     * Set to true if the file described by this message has been transferred
     * successfully.  Such a message would be the last progress message sent
     * for the file.
     */
    public boolean transferComplete;

    public TransferProgressEvent(Object source, String filename, 
            long bytesTransferred, long bytesTotal, boolean transferComplete) {
	super(source);
	this.filename = filename;
	this.bytesTransferred = bytesTransferred;
	this.bytesTotal = bytesTotal;
        this.transferComplete = transferComplete;
    }
}
