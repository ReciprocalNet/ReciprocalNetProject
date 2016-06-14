/*
 * Reciprocal Net Project
 * 
 * @(#)TableContainer.java
 * 
 * xx-xxx-1998: jobollin wrote first draft
 * 24-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;
import java.awt.Color;

/**
 * Encapsulates a table of element colors and one of the corresponding atomic
 * radii
 */
public class TableContainer {

    /** The element colors, indexed by atomic number */
    public Color[] colorTab;

    /** The atomic radii, indexed by atomic number */
    public float[] radii;

    /**
     * Constructs a new <code>TableContainer</code>
     */
    public TableContainer() {
        colorTab = null;
        radii = null;
    }

    /**
     * Constructs a new <code>TableContainer</code> with tables of the specified
     * length
     *
     * @param  len the length for the tables
     */
    public TableContainer(int len) {
        colorTab = new Color[len];
        radii = new float[len];
    }
}
