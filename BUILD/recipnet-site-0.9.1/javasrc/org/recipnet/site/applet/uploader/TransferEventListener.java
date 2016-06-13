/*
 * Reciprocal Net project
 * @(#)TransferEventListener.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 */

package org.recipnet.site.applet.uploader;
import java.util.EventListener;

/**
 * This interface describes the manner in which the <code>TransferThread</code>
 * communicates progress information to its registered listeners.
 */
public interface TransferEventListener extends EventListener {
    public void receiveTransferProgressEvent(TransferProgressEvent event);
    public void receiveTransferFailedEvent(TransferFailedEvent event);
    public void receiveTransferThreadIdleEvent(TransferThreadIdleEvent event);
}
