/*
 * Reciprocal Net Project
 *
 * @(#) ModelEvent.java
 *
 * 20-Nov-2002: jobollin moved this class to org.recipnet.site.misc from *.jamm
 * 21-Feb-2003: jobollin reformatted the source and added javadoc comments
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;

/**
 * A class representing an event that occured in a <code>Rotate3DModel</code>.
 *
 * @author John Bollinger
 * @version 0.6.0
 */
public class ModelEvent
        extends java.util.EventObject {

    /**
     * represents an event comprising addition of one or more connections to
     * the model
     */
    public final static long CONNECTIONS_MASK = 1;

    /**
     * represents an event comprising rederivation of the computed parameters
     * of the model (centroid and radius)
     */
    public final static long DERIVED_MASK = 2;

    /**
     * represents an event comprising addition of one or more vertices to
     * the model
     */
    public final static long VERTICES_MASK = 4;

    /**
     * a combination of the various bitmasks defining the nature of the
     * event
     */
    private long eventId;

    /**
     * Constructs a new <code>ModelEvent</code>
     *
     * @param  source the <code>Object</code> on which this event occured
     */
    ModelEvent(Object source) {
        super(source);
        eventId = 0L;
    }

    /**
     * Constructs a new <code>ModelEvent</code> with the specified event id
     *
     * @param  source the <code>Object</code> on which this event occured
     * @param  id the event id to assign to this event
     */
    ModelEvent(Object source, long id) {
        super(source);
        eventId = id;
    }

    /**
     * Returns the bitfield event id for this object, a combination the various
     * bitmasks that define the nature of this event
     *
     * @return the event id
     */
    public long getId() {
        return eventId;
    }
}
