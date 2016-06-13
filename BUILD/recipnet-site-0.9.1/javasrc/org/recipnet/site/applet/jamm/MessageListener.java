/*
 * Reciprocal Net Project
 * 
 * @(#)MessageListener.java
 * 
 * xx-xxx-1998: jobollin wrote first draft
 * 25-Feb-2003: jobollin reformatted the source and added javadoc comments as
 *              part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;

/**
 * An interface for use by classes that want to be able to receive
 * <code>MessageEvent</code>s
 */
public interface MessageListener
        extends java.util.EventListener {

    /**
     * Recieves the specified <code>MessageEvent</code>
     *
     * @param  e the <code>MessageEvent</code> to receive
     */
    public void messageSent(MessageEvent e);
}
