/*
 * Reciprocal Net Project
 *
 * MultiEventDispatcher.java
 *
 * xx-xxx-2001: jobollin wrote first draft
 * 18-Dec-2002: jobollin modified comments and formatting while working on
 *              task #445 (no functional modifications)
 * 09-Jan-2003: jobollin cleaned up unused imports
 * 27-Feb-2003: jobollin reformatted the source and revised and extended the
 *              javadoc comments as aprt of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 * 03-Nov-2006: jobollin suppressed debugging output
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A class whose instances can function as listeners for various events on
 * multiple objects at the same time, and that invoke a specified method on a
 * specified object when an event is received.  As of this version, the method
 * to be invoked must be declared in the class of the target, not inherited.
 * 
 * <p>
 * This version stores only one method per event source per event type.
 * </p>
 *
 * @author John C. Bollinger
 * @version 0.5.3
 */
public class MultiEventDispatcher
        implements ActionListener, ChangeListener, FocusListener {
    
    /**
     * An array of <code>Class[1]</code> each containing the event type
     * corresponding to the its position in the array, as keyed by the
     * event keys defined in this class
     */
    private static Class[][] eTypes;
    
    /**
     * A <code>Class[]</code> containing the event listener classes as
     * keyed by the event keys defined in this class
     */ 
    private static Class[] listenerClasses;
    
    /** javax.swing.AbstractButton.class */
    private static Class abstractButtonClass;

    /** javax.swing.JTextField.class */
    private static Class jTextFieldClass;

    /** java.awt.Component.class */
    private static Class componentClass;

    /** the key for action events */
    private final static int ACTION_PERFORMED_KEY = 0;

    /** the key for focus gained type focus events */
    private final static int FOCUS_GAINED_KEY = 1;

    /** the key for focus lost type focus events */
    private final static int FOCUS_LOST_KEY = 2;

    /** the key for change events */
    private final static int STATE_CHANGED_KEY = 3;

    /** the number of defined event keys */
    private final static int NUMBER_OF_KEYS = 4;

    /**
     * A <code>String[]</code> containing the names of the event classes
     * as keyed by the event keys defined in this class
     */
    private final static String[] eventClassNames =
    {
        "java.awt.event.ActionEvent", "java.awt.event.FocusEvent",
        "java.awt.event.FocusEvent", "javax.swing.event.ChangeEvent"
    };

    /**
     * A <code>String[]</code> containing the names of the listener classes
     * as keyed by the event keys defined in this class
     */
    private final static String[] listenerClassNames =
    {
        "java.awt.event.ActionListener", "java.awt.event.FocusListener",
        "java.awt.event.FocusListener", "javax.swing.event.ChangeListener"
    };

    static {
        eTypes = new Class[NUMBER_OF_KEYS][1];
        listenerClasses = new Class[NUMBER_OF_KEYS];
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            try {
                eTypes[i][0] = Class.forName(eventClassNames[i]);
                listenerClasses[i] = Class.forName(listenerClassNames[i]);
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }
        try {
            componentClass = Class.forName("java.awt.Component");
            abstractButtonClass = Class.forName("javax.swing.AbstractButton");
            jTextFieldClass = Class.forName("javax.swing.JTextField");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }

    /** a <code>Map</code> from event sources to handler lists */
    private Map eventMap;

    /**
     * constructs a new <code>MultiEventDispatcher</code>
     */
    public MultiEventDispatcher() {
        eventMap = new WeakHashMap();
    }

    /**
     * obtains the event handlers registered for the specified source object as
     * an Object[]
     *
     * @param source the source object whose event handlers are requested
     *
     * @return an Object[] containing two elements per defined key -- the
     *         target Object and the Method to invoke on the target
     */
    protected Object[] getEventsFor(Object source) {
        Object[] events = (Object[]) eventMap.get(source);
        if (events == null) {
            events = new Object[NUMBER_OF_KEYS * 2];
        }
        return events;
    }

    /**
     * Sets the action to take upon receiving the specified event
     * from the specified <code>source</code>; any previous action is replaced
     *
     * @param  source the event source <code>Object</code> for which to set the
     *         action
     * @param  target the <code>Object</code> on which to invoke a method in
     *         response to the specified event from the specified source
     * @param  method a <code>String</code> containing the name of the method
     *         to invoke on <code>target</code> in response to the specified
     *         event from the specified source
     * @param  key the event key for the event that occurred
     *
     * @throws NoSuchMethodException if <code>target</code> does not declare
     *         a method of the specified name having the appropriate signature
     */
    protected void registerEvent(Object source, Object target, String method,
        int key)
            throws NoSuchMethodException {
        Object[] handlers = getEventsFor(source);
        handlers[key * 2] = target;
        handlers[(key * 2) + 1] =
            target.getClass().getDeclaredMethod(method, eTypes[key]);
        eventMap.put(source, handlers);
    }

    /**
     * Resets the action to take on receiving the specified event type from the
     * specified source (to no action)
     *
     * @param  source the event source <code>Object</code> for which to reset
     *         the action
     * @param  key the event key for the event that occurred
     */
    protected void unregisterEvent(Object source, int key) {
        Object[] responses = (Object[]) eventMap.get(source);
        responses[key * 2] = responses[(key * 2) + 1] = null;
    }

    /**
     * Determines whether this object is registered as an event listener of the
     * specified type on the specified event source, by reference to the source
     *
     * @param  source the event source <code>Object</code>
     * @param  listenerClass the <code>Class</code> corresponding to the
     *         listener type of interest
     *
     * @return <code>true</code> if registered, <code>false</code> if not
     */
    protected boolean isRegistered(Object source, Class listenerClass) {
        EventListener[] listeners = null;
        if (componentClass.isAssignableFrom(source.getClass())) {
            listeners = ((Component) source).getListeners(listenerClass);
        }
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] == this) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Performs the actual event dispatch by looking up the event handlers
     * registered on the event source and choosing the one associated with
     * the provided key.
     *
     * @param  e an <code>EventObject</code> representing the event that
     *         ocurred
     * @param  key the key indicating specifically what type of event occurred
     */
    protected void eventOccurred(EventObject e, int key) {
        Object source = e.getSource();

        // System.err.println("Event " + e + " occurred (type " + key + ")");
        if (source != null) {
            Object[] responses = (Object[]) eventMap.get(source);

            if (responses != null) {
                try {
                    ((Method) responses[(key * 2) + 1]).invoke(
                        responses[key * 2], new Object[] { e } );
                } catch (IllegalAccessException iae) {
                    System.err.println(iae);
                } catch (InvocationTargetException ite) {
                    System.err.println(ite);
                } catch (NullPointerException npe) {
                    System.err.println("No handler registered");
                    /*
                     * Do nothing.  This may be an entirely valid case if, for
                     * example, only one listener method is registered on an
                     * event associated with a multi-method Listener.  That case
                     * in particular cannot be distinguished from an error
                     * either by this object or by an object that has registered
                     * event handlers with this object.
                     */
                }
            }
        }
    }

    /**
     * Registers this object as an <code>ActionListener</code> on object
     * <code>source</code>, so that it invokes the method named by
     * <code>method</code> on object <code>target</code> when an
     * <code>ActionEvent</code> is received
     *
     * @param source the object on which to register as an ActionListener;
     *        should be a subclass of javax.swing.AbstractButton or of
     *        javax.swing.JTextField
     * @param target the object on which to invoke a method in response to an
     *        <code>ActionEvent</code> from <code>source</code>
     * @param method a <code>String</code> containing the name of a method on
     *        <code>target</code> that has an argument list of one
     *        ActionEvent; that method will be invoked on <code>target</code>
     *        with the ActionEvent provided by the source when this object is
     *        notified of the event. (Note: for AWT events, this will happen
     *        in the AWT event processing thread.)
     */
    public void registerAction(Object source, Object target, String method) {
        try {
            Class cl = source.getClass();
            if (abstractButtonClass.isAssignableFrom(cl)) {
                registerEvent(source, target, method, ACTION_PERFORMED_KEY);
                if ( !isRegistered(source,
                        listenerClasses[ACTION_PERFORMED_KEY])) {
                    ((AbstractButton) source).addActionListener(this);
                }
            } else if (jTextFieldClass.isAssignableFrom(cl)) {
                registerEvent(source, target, method, ACTION_PERFORMED_KEY);
                if ( !isRegistered(source,
                        listenerClasses[ACTION_PERFORMED_KEY])) {
                    ((JTextField) source).addActionListener(this);
                }
            }
        } catch (NoSuchMethodException nsme) {
            System.out.println("No method " + method + " in " + target);
            unregisterEvent(source, ACTION_PERFORMED_KEY);
        }
    }

    /**
     * Implementation method of the <code>EventListener</code> interface
     *
     * @param  ae the <code>ActionEvent</code> describing the action that
     *         occurred
     */
    public void actionPerformed(ActionEvent ae) {
        eventOccurred(ae, ACTION_PERFORMED_KEY);
    }

    /**
     * Registers this object as an ChangeListener on object <code>source</code>,
     * so that it invokes the method named by <code>method</code> on object
     * <code>target</code> when an <code>ChangeEvent</code> is received;
     * <strong>this version is a do-nothing placeholder</strong>
     *
     * @param source the object on which to register as an ChangeListener
     * @param target the object on which to invoke a method in response to a
     *        <code>ChangeEvent</code> from <code>source</code>
     * @param method a <code>String</code> containing the name of a method on
     *        <code>target</code> that has an argument list of one
     *        ChangeEvent; that method will be invoked on <code>target</code>
     *        with the ChangeEvent provided by the source when this object is
     *        notified of the event. (Note: for AWT events, this will happen
     *        in the AWT event processing thread.)
     */
    public void registerChange(Object source, Object target, String method) {
        /*
         * It is not currently possible to register a ChangeListener with this
         * class.
         */
    }

    /**
     * Implementation method of the <code>ChangeListener</code> interface
     *
     * @param  ce the <code>ChangeEvent</code> describing the action that
     *         occurred
     */
    public void stateChanged(ChangeEvent ce) {
        eventOccurred(ce, STATE_CHANGED_KEY);
    }

    /**
     * Registers this object as a FocusListener on object <code>source</code>,
     * so that it invokes the method named by <code>method</code> on object
     * <code>target</code> when a focus gained <code>FocusEvent</code> is
     * received
     *
     * @param source the object on which to register as an FocusListener;
     *        should be a subclass of java.awt.Component
     * @param target the object on which to invoke a method in response to a
     *        focus gained event from <code>source</code>
     * @param method a <code>String</code> containing the name of a method on
     *        <code>target</code> that has an argument list of one FocusEvent;
     *        that method will be invoked on <code>target</code> with the
     *        FocusEvent provided by the source when this object is notified
     *        of the event. (Note: for AWT events, this will happen in the AWT
     *        event processing thread.)
     */
    public void registerFocusGain(Object source, Object target, String method) {
        try {
            if (componentClass.isAssignableFrom(source.getClass())) {
                registerEvent(source, target, method, FOCUS_GAINED_KEY);
                if (!isRegistered(source, listenerClasses[FOCUS_GAINED_KEY])) {
                    ((Component) source).addFocusListener(this);
                }
            }
        } catch (NoSuchMethodException nsme) {
            System.out.println("No method " + method + " in " + target);
            unregisterEvent(source, FOCUS_GAINED_KEY);
        }
    }

    /**
     * Implementation method of the <code>FocusListener</code> interface
     *
     * @param  fe  the <code>FocusEvent</code> describing the focus gain that
     *         occurred
     */
    public void focusGained(FocusEvent fe) {
        eventOccurred(fe, FOCUS_GAINED_KEY);
    }

    /**
     * Registers this object as a FocusListener on object <code>source</code>,
     * so that it invokes the method named by <code>method</code> on object
     * <code>target</code> when a focus lost <code>FocusEvent</code> is
     * received
     *
     * @param source the object on which to register as a FocusListener; should
     *        be a subclass of java.awt.Component
     * @param target the object on which to invoke a method in response to a
     *        focus lost event from <code>source</code>
     * @param method a <code>String</code> containing the name of a method on
     *        <code>target</code> that has an argument list of one FocusEvent;
     *        that method will be invoked on <code>target</code> with the
     *        FocusEvent provided by the source when this object is notified
     *        of the event. (Note: for AWT events, this will happen in the AWT
     *        event processing thread.)
     */
    public void registerFocusLoss(Object source, Object target, String method) {
        try {
            if (componentClass.isAssignableFrom(source.getClass())) {
                registerEvent(source, target, method, FOCUS_LOST_KEY);
                if (!isRegistered(source, listenerClasses[FOCUS_LOST_KEY])) {
                    ((Component) source).addFocusListener(this);
                }
            }
        } catch (NoSuchMethodException nsme) {
            System.out.println("No method " + method + " in " + target);
            unregisterEvent(source, FOCUS_LOST_KEY);
        }
    }

    /**
     * Implementation method of the <code>FocusListener</code> interface
     *
     * @param  fe  the <code>FocusEvent</code> describing the focus loss that
     *         occurred
     */
    public void focusLost(FocusEvent fe) {
        eventOccurred(fe, FOCUS_LOST_KEY);
    }
}
