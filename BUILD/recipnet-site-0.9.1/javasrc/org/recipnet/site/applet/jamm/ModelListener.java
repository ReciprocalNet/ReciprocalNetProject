/*
 * Reciprocal Net Project
 *
 * @(#) ModelListener.java
 *
 * 20-Nov-2002: jobollin moved this class to org.recipnet.site.misc from *.jamm
 * 21-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;

/**
 * an interface for notifying interested classes of changes to a particular
 * Rotate3DModel instance
 */
public interface ModelListener
        extends java.util.EventListener {

    /**
     * Invoked to notify the listening object that the model it is listening to
     * for <code>ModelEvent</code>s has generated an event
     *
     * @param  e the <code>ModelEvent</code> that was generated
     */
    public void modelChanged(ModelEvent e);
}
