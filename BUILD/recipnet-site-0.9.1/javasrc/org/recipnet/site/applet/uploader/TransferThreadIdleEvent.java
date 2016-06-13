/*
 * Reciprocal Net project
 * @(#)TransferThreadIdleEvent.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 */
package org.recipnet.site.applet.uploader;
import java.util.EventObject;

/**
 * An event message that is generated whenever the transfer thread is about to
 * go idle and an indefinite amount of time is expected to pass before the next
 * message is sent.  This message may be generated spuriously, but the transfer
 * thread will not pause without having sent one of these messages.
 */
public class TransferThreadIdleEvent extends EventObject {
    public TransferThreadIdleEvent(Object source) {
	super(source);
    }
}
