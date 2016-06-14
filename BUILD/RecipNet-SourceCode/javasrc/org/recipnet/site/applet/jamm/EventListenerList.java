/*
 * Reciprocal Net Project
 *
 * @(#) EventListenerList.java
 *
 * 20-Nov-2002: jobollin moved this class to org.recipnet.site.misc from *.jamm
 * 21-Feb-2003: jobollin reformatted the source and improved the javadocs as
 *              part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;
import java.util.EventListener;

/**
 * This class is a clone of javax.swing.event.EventListnerList for use with
 * non-Swing-enabled VMs. Because the add() method must sometimes replace one
 * listenerList array with a longer one, users should understand that the
 * array returned by getListeners() may cease at any time to be the one used
 * internally. Also, for the same reason, all public methods that access the
 * internal array are synchronized.
 */
public class EventListenerList {

    /**
     * An array of Listeners and event types that serves as the core data store
     * for this class
     */
    protected Object[] listenerList;

    /** the number of entries used in <code>listenerList</code> */
    protected int nEntries;

    /**
     * Constructs a new EventListenerList
     */
    public EventListenerList() {
        nEntries = 0;
        listenerList = new Object[8];
    }

    /**
     * Returns the number of listeners currently stored in this list
     *
     * @return the number of listeners currently in this list
     */
    public int getListenerCount() {
        return nEntries / 2;
    }

    /**
     * Returns the number of listeners currently in this list that are
     * registered for events of class <code>eventClass</code>
     *
     * @param  eventClass the <code>Class</code> of the event for which a count
     *         of listeners is requested
     *
     * @return the number of listeners for events of class
     *         <code>eventClass</code> in this list
     */
    synchronized public int getListenerCount(Class eventClass) {
        int count = 0;
        for (int i = firstIndexOfClass(eventClass);
                (i < nEntries) && (listenerList[i] == eventClass); i += 2) {
            count++;
        }
        return count;
    }

    /**
     * Returns the array of listeners and event classes that backs this
     * <code>EventListenerList</code>
     *
     * @return the <code>Object[]</code> that backs this
     *         <code>EventListenerList</code> 
     */
    synchronized public Object[] getListenerList() {
        return listenerList;
    }

    /**
     * Returns an array of all the listeners in this list that are registered
     * for events of class <code>eventClass</code>
     *
     * @param  eventClass the <code>Class</code> of the events for which the
     *         listeners are requested
     *
     * @return an <code>EventListener[]</code> containing the listeners for
     *         <code>eventClass</code>
     */
    synchronized public EventListener[] getListeners(Class eventClass) {
        int start = firstIndexOfClass(eventClass);
        int stop;
        for (stop = start;
             (stop < nEntries) && (listenerList[stop] == eventClass);
             stop += 2) {
            // do nothing
        }
        start += 1;
        stop += 1;
        EventListener[] el = new EventListener[(stop - start) >> 1];
        for (int i = start; i < stop; i += 2) {
            el[i >> 1] = (EventListener) listenerList[i];
        }
        return el;
    }

    /**
     * Adds <code>l</code> as a listener for class <code>eventClass</code>
     *
     * @param  eventClass the <code>Class</code> of the events for which
     *         <code>l</code> is to listen
     * @param  l the event listener to add
     */
    synchronized public void add(Class eventClass, EventListener l) {
        Object[] temp;
        int inx = firstIndexOfClass(eventClass);
        if (nEntries >= listenerList.length) {
            temp = new Object[nEntries * 2];
            System.arraycopy(listenerList, 0, temp, 0, inx);
        } else {
            temp = listenerList;
        }
        System.arraycopy(listenerList, inx, temp, inx + 2, nEntries - inx);
        temp[inx] = eventClass;
        temp[++inx] = l;
        listenerList = temp;
        nEntries += 2;
    }

    /**
     * Removes listener <code>l</code> from this list as a listener for events
     * of class <code>eventClass</code>
     *
     * @param  eventClass the <code>Class<code> of the events for which
     *         <code>l</code> should be removed as a listener
     * @param  l the event listener that should be removed
     */
    synchronized public void remove(Class eventClass, EventListener l) {
        for (int i = firstIndexOfClass(eventClass);
                (i < nEntries) && (listenerList[i] == eventClass); i += 2) {
            if (listenerList[i + 1] == l) {
                System.arraycopy(listenerList, i + 2, listenerList, i,
                    nEntries - i);
                listenerList[--nEntries] = null;
                listenerList[--nEntries] = null;
            }
        }
    }

    /**
     * Finds and returns the index of the first entry in the list for class
     * <code>eventClass</code>
     *
     * @param  eventClass the <code>Class<code> of the event to find
     *
     * @return the index of the first appearance <code>eventClass</code> in the
     *         list, or <code>nEntries</code> if <code>eventClass</code> does
     *         not appear
     */
    private int firstIndexOfClass(Class eventClass) {
        int i;
        for (i = 0; i < nEntries; i += 2) {
            if (((Class) listenerList[i]) == eventClass) {
                break;
            }
        }
        return i;
    }
}
