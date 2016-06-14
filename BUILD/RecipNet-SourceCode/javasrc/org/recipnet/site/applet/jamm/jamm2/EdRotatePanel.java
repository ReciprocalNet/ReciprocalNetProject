/*
 * Reciprocal Net Project
 *
 * @(#) DemoRotatePanel.java
 *
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DModel
 * 25-Feb-2003: jobollin reformatted the source as part of task #749
 * 26-Feb-2003: jobollin extended and revised javadoc comments as part of
 *              task #749
 * 26-Feb-2003: jobollin incorporated the initialize() body into the three-arg
 *              constructor
 * 29-May-2003: jobollin fixed bug #919 by rearranging code in setModel
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * A subclass of <code>DemoRotatePanel</code> providing additional features
 * relevant for use in educational tools and materials.  Specific added
 * features are toggled hydrogen display and home orientation setting.
 */
class EdRotatePanel
        extends DemoRotatePanel {

    /** Flags whether or not to display hydrogen atoms (type 1 vertices) */
    protected boolean hydro;

    /** The number of connections not involving type 1 vertices */
    private int numNonHCon;

    /** The coordinates for the vertices in the home orientation */
    protected float[][] vert;

    /**
     * Constructs a new <code>EdRotatePanel</code>
     */
    public EdRotatePanel() {
        this(null, null, null);
    }

    /**
     * Constructs a new <code>EdRotatePanel</code> with the specified model
     * and color table
     *
     * @param model the <code>Rotate3DModel</code> to present in this panel
     * @param tab the <code>Color[]</code> color table to use
     */
    public EdRotatePanel(Rotate3DModel model, Color[] tab) {
        this(model, tab, null);
    }

    /**
     * Constructs a new <code>EdRotatePanel</code> with the specified model,
     * color table, and ball radius table
     *
     * @param model the <code>Rotate3DModel</code> to present in this panel
     * @param tab the <code>Color[]</code> color table to use
     * @param rad the <code>float[]</code> ball radius table to use
     */
    public EdRotatePanel(Rotate3DModel model, Color[] tab, float[] rad) {
        super(model, tab, rad);
        hydro = false;
    }

    /**
     * Sets the value of the hydrogen display flag
     *
     * @param h the new <code>boolean</code> value
     */
    public void setHydro(boolean h) {
        if (hydro != h) {
            hydro = h;
            repaint();
        }
    }

    /**
     * Toggles the current value of the hydrogen display flag
     *
     * @return the new <code>boolean</code> value of the flag
     */
    public boolean toggleHydro() {
        setHydro(!hydro);
        repaint();
        return hydro;
    }

    /**
     * Gets the current value of the hydrogen display flag
     * 
     * @return the current value of the flag
     */
    public boolean getHydro() {
        return hydro;
    }

    /**
     * Assigns a new model to this panel
     *
     * @param m the new <code>Rotate3DModel</code> to display
     */
    public void setModel(Rotate3DModel m) {
        super.setModel(m);
        if (m != null) {
            int[][] con = getConnections();
            HydrogenToEndComparator c
                = new HydrogenToEndComparator(mod.getPointTypes());

            Arrays.sort(con, c);
            numNonHCon = c.numNonHCon(con);
        }
    }

    protected float[][] getVertices() {
        return vert;
    }

    /**
     * Paints those connections that will be displayed on this panel
     *
     * @param g the <code>Graphics</code> with which to perform the painting
     */
    protected void paintConnections(Graphics g) {
        if (drawMode != SPACE_MODE) {
            if (hydro) {
                super.paintConnections(g);
            } else {
                paintConnections(g, numNonHCon);
            }
        }
    }

    /**
     * Determines whether the specified point ought to be painted
     *
     * @param i the index of the point in the point array
     *
     * @return <code>false</code> if the point is of type 1 and
     *         <code>hydro</code> is false; otherwise <code>true</code>
     */
    protected boolean pointOK(int i) {
        return (((mod.getPointTypes())[i] > 1) || hydro);
    }

    /**
     * Resets the view to the home orientation
     */
    void homeOrientation() {
        synchronized (mod) {
            int nPoints = mod.getNumPoints();
            float[][] temp = new float[nPoints][3];
            
            aMat.transform(mod.getPoints(), temp, nPoints);
            vert = temp;
        }
        reset();
    }
}
