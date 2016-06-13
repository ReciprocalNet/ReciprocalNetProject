/*
 * Reciprocal Net Project
 *
 * DemoRotatePanel.java
 *
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DModel
 * 25-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 26-Feb-2003: jobollin incorporated the initialize method into the three-arg
 *              constructor
 * 26-Feb-2003: jobollin factored pointOK() out into Rotate3DPanel
 * 26-Feb-2003: jobollin removed the connect() method and put support for
 *              reinitballs() into the new modelChanged(ModelEvent) method
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import org.recipnet.site.applet.jamm.ModelEvent;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * A <code>Rotate3DPanel</code> subclass that provides features relevant for
 * rendering and examining molecules, in particular ball&amp;stick
 * and space-filled rendering modes
 */
class DemoRotatePanel
        extends Rotate3DPanel {

    /** represents line drawing mode */
    static final int LINE_MODE = 0;

    /** represents ball &amp; stick drawing mode */
    static final int BALL_STICK_MODE = 1;

    /** represents space-filled drawing mode */
    static final int SPACE_MODE = 2;

    /** the maximum integer value that is sensical for <code>drawMode</code> */
    static final int MAX_DRAW_MODE = SPACE_MODE;

    /**
     * an array containing the <code>Atom</code>s to use for each relevant
     * ball type, indexed by ball type
     */
    protected Atom[] balls;

    /** an array containing all the known ball radii, indexed by ball type */
    protected float[] radii;

    /** the scale factor for balls in ball &amp; and stick mode */
    protected double ballSizeBall;

    /** the scale factor for balls in space-filled mode */
    protected double ballSizeSpace;

    /** the current drawing mode */
    protected int drawMode;
    
    /** indicates whether or not this object has been initialized */
    private boolean initialized = false;

    /**
     * Constructs a new <code>DemoRotatePanel</code>
     */
    public DemoRotatePanel() {
        this(null, null, null);
    }

    /**
     * Constructs a new <code>DemoRotatePanel</code>with the specified model
     * and color table
     * 
     * @param  model the <code>Rotate3DModel<code> to diplay on this panel
     * @param  tab a <code>Color[]</code> containing the vertex and ball
     *         color to use for each vertex type
     */
    public DemoRotatePanel(Rotate3DModel model, Color[] tab) {
        this(model, tab, null);
    }

    /**
     * Constructs a new <code>DemoRotatePanel</code> with the specified model
     * color table, and radius table
     *
     * @param  model the <code>Rotate3DModel<code> to diplay on this panel
     * @param  tab a <code>Color[]</code> containing the vertex and ball
     *         color to use for each vertex type
     * @param  rad a <code>float[]</code> containing the ball radii for each
     *         vertex type
     */
    public DemoRotatePanel(Rotate3DModel model, Color[] tab, float[] rad) {
        super(model, tab);
        if (rad == null) {
            defaultRadii();
        } else {
            setRadii(rad);
        }
        drawMode = LINE_MODE;
        ballSizeBall = 1d;
        ballSizeSpace = 1d;
        initialized = true;
        tableUpdated();
    }

    /**
     * Sets the background color for this panel
     *
     * @param bg the new background color
     */
    public void setBackground(Color bg) {
        super.setBackground(bg);
        reinitBalls();
    }

    /**
     * Sets the ball size for ball &amp; stick mode
     *
     * @param bs a <code>double</code> containing the new ball size
     */
    public void setBallSizeBall(double bs) {
        if ((bs > 0) && (bs != ballSizeBall)) {
            ballSizeBall = bs;
            repaint();
        }
    }

    /**
     * Gets the ball size used in ball &amp; stick mode
     *
     * @return the ball size as a <code>double</code>
     */
    public double getBallSizeBall() {
        return ballSizeBall;
    }

    /**
     * Sets the ball size for space-filled mode
     *
     * @param bs a <code>double</code> containing the new ball size
     */
    public void setBallSizeSpace(double bs) {
        if ((bs > 0) && (bs != ballSizeSpace)) {
            ballSizeSpace = bs;
            repaint();
        }
    }

    /**
     * Gets the ball size used in space-filled mode
     *
     * @return the ball size as a <code>double</code>
     */
    public double getBallSizeSpace() {
        return ballSizeSpace;
    }

    /**
     * Sets the drawing mode to <code>mode</code>, provided that
     * <code>mode</code> is a valid drawing mode for this panel
     *
     * @param mode the new drawing mode code
     */
    public void setDrawMode(int mode) {
        if ((drawMode != mode) && (mode >= 0) && (mode <= MAX_DRAW_MODE)) {
            if ((mode != LINE_MODE) && (tvert != null)) {
                zSort();
            }
            drawMode = mode;
            repaint();
        }
    }

    /**
     * Gets the current drawing mode
     *
     * @return the drawing mode
     */
    public int getDrawMode() {
        return drawMode;
    }

    public void setModel(Rotate3DModel m) {
        super.setModel(m);
        balls = null;
        reinitBalls();
    }
    
    /**
     * Returns the ball radius table
     *
     * @return the radius table as a <code>float[]</code>
     */
    public float[] getRadii() {
        return radii;
    }

    /**
     * Paints the body of this component with the use of the specified graphics
     * context
     *
     * @param g the <code>Graphics</code>
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (drawMode != LINE_MODE) {
            paintBalls(g);
        }
    }

    /**
     * Sets a new ball radius table
     *
     * @param rad a <code>float[]</code> containing the new radii, indexed by
     *        ball type
     */
    protected void setRadii(float[] rad) {
        if (rad != null) {
            radii = rad;
            repaint();
        }
    }

    public void modelChanged(ModelEvent modelEvent) {
        super.modelChanged(modelEvent);
        if ((modelEvent.getId() & ModelEvent.VERTICES_MASK) != 0) {
            reinitBalls();
        }
    }

    /**
     * Creates a radius table that contains a default radius for ever ball
     * type
     */
    protected void defaultRadii() {
        int l = colorTab.length;
        if (l < 0) {
            radii = null;
        } else {
            radii = new float[l];
            for (int i = 0; i < l; i++) {
                radii[i] = 1.0f;
            }
        }
    }

    /**
     * Paints the atom balls on the image.  In space-filling mode the ball
     * scale is set so as to cause significant overlap; otherwise the ball
     * scale is set so as to leave plenty of each stick visible.  This method
     * is measurably slower in red/green stereo mode than in mono or L/R
     * stereo mode because in R/G mode two ball images must be rendered for
     * each atom whereas in any other mode only one is rendered (but may be
     * drawn twice).
     * 
     * @param  g the <code>Graphics</code> with which to draw the balls
     */
    protected void paintBalls(Graphics g) {
        int[] type = mod.getPointTypes();
        
        if (drawMode == SPACE_MODE) {
            Atom.setBallScale(scale * 1.6f * (float) ballSizeSpace);
        } else {
            Atom.setBallScale((scale * (float) ballSizeBall) / 1.8f);
        }
        int j;
        int n;
        int zLevel;
        int offset;
        int nvert = mod.getNumPoints();
        Image I;
        for (int i = 0; i < nvert; i++) {
            j = zMap[i];
            if (pointOK(j)) {
                n = type[j];
                zLevel = (int) (16 * cueLevel(tvert[j][2]));
                I = balls[n].render(zLevel, tvert[j][2], radii[n]);
                offset = I.getWidth(null) >> 1;
                g.drawImage(I, tvert[j][0] - offset, tvert[j][1] - offset, this);
            }
        }
    }

    /**
     * Paints the connections on the panel, except in space-filled mode
     *
     * @param  g the <code>Graphics</code> with which to draw the balls
     */
    protected void paintConnections(Graphics g) {
        if (drawMode != SPACE_MODE) {
            super.paintConnections(g);
        }
    }

    /**
     * Initializes any missing, required balls, and synchronizes exisiting
     * balls with the current background color
     */
    protected void reinitBalls() {
        if (!initialized || (mod == null) || (colorTab == null)) {
            return;
        }

        int[] type = mod.getPointTypes();
        
        if (balls == null) {
            balls = new Atom[colorTab.length];
        } else if (balls.length < colorTab.length) {
            Atom[] ta = new Atom[colorTab.length];
            System.arraycopy(balls, 0, ta, 0, balls.length);
            balls = ta;
        }
        if (mod == null) {
            return;
        }
        Color bg = getBackground();
        for (int i = 0; i < mod.getNumPoints(); i++) {
            int t = type[i];
            if (balls[t] == null) {
                try {
                    balls[t] = new Atom(colorTab[t], bg);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    balls[t] = new Atom(colorTab[0], bg);
                }
            } else {
                balls[t].setBackground(bg);
            }
        }
    }

    /**
     * Performs necessary modifications when the color table has been updated;
     * in particular, this version reinitializes the ball images
     */
    protected void tableUpdated() {
        balls = null;
        reinitBalls();
    }


    /**
     * Transforms the first <code>np</code> points in the model; this version
     * also invokes a depth sort of the coordinates unless the panel is in
     * line drawing mode.
     *
     * @param np the number of points to transform
     */
    protected void transform(int np) {
        super.transform(np);
        if (drawMode != LINE_MODE) {
            zSort();
        }
    }
}
