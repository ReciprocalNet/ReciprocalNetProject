/*
 * Reciprocal Net Project
 *
 * JaMMKeyEventDispatcher.java
 *
 * 18-Dec-2002: jobollin wrote first draft
 * 27-Feb-2003: jobollin extended the javadoc comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

/**
 * a <code>KeyEventDispatcher</code> implementation for applets descending from
 * <code>BasicJaMM</code>.  This plugs in to the new focus management
 * subsystem implemented in Java 1.4, and provides for the applet's main view
 * panel and the applet itself to receive keyboard events when they are not
 * focused.
 */
class JaMMKeyEventDispatcher implements KeyEventDispatcher {

    /**
     * The <code>BasicJaMM</code> instance to which this dispatcher should
     * attempt to dispatch key events
     */
    private BasicJaMM jamm;

    /**
     * The <code>KeyboardFocusManager</code> in place when this dispatcher
     * was constructed
     */
    private KeyboardFocusManager manager;

    /**
     * The <code>Rotate3DPanel</code> instance to which this dispatcher should
     * attempt to dispatch key events
     */
    private Rotate3DPanel panel;

    /**
     * constructs a new JaMMKeyEventDispatcher
     *
     * @param _jamm the <code>BasicJaMM</code> to which this
     *        <code>KeyEventDispatcher</code> may attempt to dispatch
     *        <code>KeyEvents</code>
     * @param _panel the <code>Rotate3DPanel</code> to which this
     *        <code>KeyEventDispatcher</code> will attempt to dispatch
     *        <code>KeyEvents</code>
     */
    public JaMMKeyEventDispatcher(BasicJaMM _jamm, Rotate3DPanel _panel) {
        if (_jamm == null) {
            throw new IllegalArgumentException(
                "The _jamm parameter may not be null");
        }
        if (_panel == null) {
            throw new IllegalArgumentException(
                "The _panel parameter may not be null");
        }
        jamm = _jamm;
        panel = _panel;
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }

    /**
     * implementation method of <code>KeyEventDispatcher</code>.  This version
     * first dispatches <code>e</code> to the registered
     * <code>Rotate3DPanel</code>, and if it is not consumed then dispatches
     * it to the registered <code>BasicJaMM</code>.
     *
     * @param e the <code>KeyEvent</code> to dispatch
     *
     * @return <code>true</code> if <code>e</code> is consumed,
     *         <code>false</code> otherwise
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        manager.redispatchEvent(panel, e);
        if (e.isConsumed()) {
            return true;
        } else {
            manager.redispatchEvent(jamm, e);
            return e.isConsumed();
        }
    }

    /**
     * causes this <code>KeyEventDispatcher</code> to deregister itself from
     * the <code>KeyboardFocusManager</code> and thus stop dispatching events.
     * This operation cannot be reversed; a new
     * <code>JaMMKeyEventDispatcher</code> must be instantiated instead.
     */
    void stopDispatching() {
        manager.removeKeyEventDispatcher(this);
        manager = null;
        jamm = null;
        panel = null;
    }
}
