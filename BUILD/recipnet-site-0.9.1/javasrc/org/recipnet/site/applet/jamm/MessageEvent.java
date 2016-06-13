/*
 * Reciprocal Net Project
 * 
 * @(#)MessageEvent.java
 * 
 * xx-xxx-1998: jobollin wrote first draft
 * 25-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;

/**
 * A minimalist class for an object to use to broadcast arbitrary messages to
 * interested listeners.
 */
public class MessageEvent
        extends java.util.EventObject {

    /** The message carried by this event */
    private String message;

    /**
     * Constructs a new <code>MessageEvent</code> with an empty message and the
     * specified source
     *
     * @param  source the object that created the event
     */
    public MessageEvent(Object source) {
        this(source, "");
    }

    /**
     * Constructs a new <code>MessageEvent</code> with the specified message
     * and source
     *
     * @param  source the object that created the event
     * @param  mes the message to be carried by this event
     */
    public MessageEvent(Object source, String mes) {
        super(source);
        message = new String(mes);
    }

    /**
     * Returns the message carried by this event
     *
     * @return a <code>String</code> containing the message
     */
    public String getMessage() {
        return new String(message);
    }
}
