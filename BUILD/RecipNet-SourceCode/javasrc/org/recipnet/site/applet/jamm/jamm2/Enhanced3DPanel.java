/*
 * Reciprocal Net Project
 *
 * Enhanced3DPanel.java
 *
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DPanel and
 *              Matrix3D
 * 25-Feb-2003: jobollin reformatted the source as part of task #749
 * 26-Feb-2003: jobollin revised and extended the javadoc comments as part of
 *              task #749
 * 26-Feb-2003: jobollin incorporated the initialize method into the two-arg
 *              constructor
 * 26-Feb-2003: jobollin factored pointOK() out into Rotate3DPanel
 * 26-Feb-2003: jobollin removed unused rotateEnabled field
 * 26-Feb-2003: jobollin replaced the label field with direct invocations
 *              of mod.getPointLabels(), and removed the then unused connect()
 *              method
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package 
 *              references to match source tree reorganization
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import org.recipnet.common.Matrix3D;
import org.recipnet.site.applet.jamm.Blender;
import org.recipnet.site.applet.jamm.MessageEvent;
import org.recipnet.site.applet.jamm.MessageListener;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * An improved <code>Rotate3DPanel<code> supporting stereographics, display of
 * vertex labels and rotation angles, and reporting of messages to external
 * listeners.
 */
public class Enhanced3DPanel
        extends Rotate3DPanel {

    /** represents monographic rendering mode */
    static final int MONO_MODE = 0;

    /** represents left / right stereographic rendering mode */
    static final int LR_STEREO_MODE = 1;

    /** represents red / green stereographic rendering mode */
    static final int RG_STEREO_MODE = 2;

    /** The maximum allowed value for a stereo mode constant */
    static final int MAX_STEREO_MODE = RG_STEREO_MODE;

    /** The color with which to draw user-selected angles */
    protected Color angleColor;

    /** The green color for red / green stereo */
    protected Color stereoGreen;

    /** The red color for red / green stereo */
    protected Color stereoRed;

    /** Contains the objects registered as event listeners on this panel */
    private javax.swing.event.EventListenerList elList;

    /** The left-eye transformation matrix for stereo views */
    protected Matrix3D lMat;

    /** The transformed vertex coordinates for the left-eye view */
    protected int[][] stvert;

    /** Flags whether or not to display rotation angles */
    protected boolean angles;

    /** Flags whether or not to display vertex labels */
    protected boolean labels;

    /** The code for the current stereo mode */
    protected int stereoMode;

    /** The color for the left eye image */
    private Color lColor;

    /** The color for the right eye image */
    private Color rColor;

    /**
     * Stores the selected background color when the view is changed to
     * red / green stereo mode, and is used to restore that color when the
     * view is changed to any other mode
     */
    private Color savedColor;

    /**
     * Creates a new <code>Enhanced3DPanel</code>
     */
    public Enhanced3DPanel() {
        super(null, null);
    }

    /**
     * Creates a new <code>Enhanced3DPanel</code> with the specified model and
     * color table
     *
     * @param model the <code>Rotate3DModel</code> to present in this panel
     * @param tab a <code>Color[]</code> containing the colors for all the
     *        vertex types
     */
    public Enhanced3DPanel(Rotate3DModel model, Color[] tab) {
        super(model, tab);
        elList = new javax.swing.event.EventListenerList();
        angles = true;
        labels = false;
        stereoMode = MONO_MODE;
        angleColor = Color.blue;
    }

    /**
     * Sets the color to use for the lines displaying user-defined angles
     *
     * @param c the <code>Color<code>
     */
    public void setAngleColor(Color c) {
        if (c != null) {
            angleColor = c;
            if (angles) {
                repaint();
            }
        }
    }

    /**
     * Gets the current color used to display user-defined angles
     *
     * @return the <code>Color<code>
     */
    public Color getAngleColor() {
        return angleColor;
    }

    /**
     * Sets whether or not to display orientation angles
     *
     * @param a <code>true</code> to display angles, <code>false</code> to
     *        hide them
     */
    public void setAngles(boolean a) {
        if (angles != a) {
            angles = a;
            repaint();
        }
    }

    /**
     * Returns whether or not orientation angles are diplayed by this panel
     *
     * @return <code>true</code> if angles are displayed; <code>false</code>
     *         otherwise
     */
    public boolean getAngles() {
        return angles;
    }

    /**
     * Toggles the display of orientation angles
     *
     * @return the new value of the angles flag
     */
    public boolean toggleAngles() {
        setAngles(!angles);
        repaint();
        return angles;
    }

    /**
     * Sets whether or not to display vertex labels
     *
     * @param l <code>true</code> to display labels, <code>false</code> to
     *        hide them
     */
    public void setLabels(boolean l) {
        if (l != labels) {
            labels = l;
            repaint();
        }
    }

    /**
     * Returns whether or not vertex labels are diplayed by this panel
     *
     * @return <code>true</code> if labels are displayed; <code>false</code>
     *         otherwise
     */
    public boolean getLabels() {
        return labels;
    }

    /**
     * Toggles the display of vertex labels
     *
     * @return the new value of the labels flag
     */
    public boolean toggleLabels() {
        setLabels(!labels);
        repaint();
        return labels;
    }

    /**
     * If <code>mode</code> is a valid stereo mode and different from the
     * current mode, then changes the mode and schedules the panel for
     * repainting.  When switching to or from left/right stereo mode, the model
     * is rescaled appropriately.  When switching between red/green stereo
     * mode and any other mode, the background color is saved or restored as
     * appropiate (red/green stereo is always painted on a white background).
     *
     * @param mode the new stereo mode
     */
    public void setStereoMode(int mode) {
        if ((mode >= 0) && (mode <= MAX_STEREO_MODE) && (mode != stereoMode)) {
            if (stereoMode == RG_STEREO_MODE) {
                setBackground(savedColor);
            } else if (mode == RG_STEREO_MODE) {
                Color newColor = Color.white;
                
                savedColor = getBackground();
                stereoGreen = new Color(0, newColor.getGreen(), 0, 127);
                stereoRed = new Color(newColor.getRed(), 0, 0, 127);
                setBackground(newColor);
            }
            stereoMode = mode;
            rescale();
            transformed = false;
            repaint();
        }
    }

    /**
     * Returns the current stereo mode code
     *
     * @return the stereo mode code
     */
    public int getStereoMode() {
        return stereoMode;
    }

    /**
     * Adds a listener for <code>MessageEvent</code>s to this panel's list
     *
     * @param ml the <code>MessageListener</code> to add
     */
    public void addMessageListener(MessageListener ml) {
        elList.add(MessageListener.class, ml);
    }

    /** 
     * Finds the closest suitable transformed point in the model to the
     * specified 2D coordinates on the panel, provided that there is any
     * within the selection radius.  Points for which <code>pointOK</code>
     * returns <code>false</code> are never selected.
     * <p>
     * Used to associate mouse clicks with points
     * 
     * @param  x the x coodinate of the selection point
     * @param  y the y coordinate of the selection point
     */
    public int findPoint(int x, int y) {
        int point = -1;
        int[][] verts;
        int dx;
        int dy;
        int d2;
        
        /*
         * The initial value of min is the square of the the selection radius
         * A model vertex must be within the selection radius of the click
         * location to be selected.
         */
        int min = 26;
        
        if ((stereoMode == LR_STEREO_MODE) && (x <= (winWidth / 2))) {
            verts = stvert;
        } else {
            verts = tvert;
        }
        for (int i = mod.getNumPoints(); --i >= 0;) {
            if (pointOK(i)) {
                dx = x - verts[i][0];
                dy = y - verts[i][1];
                d2 = (dx * dx) + (dy * dy);
                if (d2 < min) {
                    min = d2;
                    point = i;
                }
            }
        }
        return point;
    }

    /**
     * Displays the model on this panel according to the options selected
     * 
     * @param  g a <code>Graphics</code> to use to paint this panel
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (labels) {
            paintLabels(g);
        }

        if (angles) {
            paintAngles(g, winHeight);
        }
    }

    /**
     * Removes a MessageListener from among those registered to receive
     * <code>MessageEvent</code>s from this panel
     *
     * @param ml the <code>MessageListener</code> to remove
     */
    public void removeMessageListener(MessageListener ml) {
        elList.remove(MessageListener.class, ml);
    }

    /**
     * Sends out a MessageEvent to all listeners registered to receive such
     * events.  A generic MessageEvent is supplied if the argument is null.
     */
    protected void fireMessageEvent(MessageEvent messageEvent) {
        synchronized (elList) {
            // Guaranteed to return a non-null array
            Object[] listeners = elList.getListenerList();

            /*
             *  Process the listeners last to first, notifying those that are
             *  interested in this event
             */
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MessageListener.class) {
                    // Lazily create the event:
                    if (messageEvent == null) {
                        messageEvent = new MessageEvent(this);
                    }
                    ((MessageListener) listeners[i + 1]).messageSent(
                        messageEvent);
                }
            }
        }
    }

    /**
     * Responds to a mouse click near point number <code>p</code>
     *
     * @param p the index of a point near which the click occurred
     */
    protected void handleClick(int p) {
        if (p >= 0) {
            fireMessageEvent(new MessageEvent(this,
                    (mod.getPointLabels())[p] + "  " + tvert[p][0] + "  "
                        + tvert[p][1] + "  " + tvert[p][2]));
        }
    }

    protected int minDimension(int w, int h) {
        return Math.min(((stereoMode == LR_STEREO_MODE) ? (w / 2) : w), h);
    }

    /**
     * Updates the right eye view and left eye view drawing colors suitably
     * for drawing an element with the specified base color index, foreground
     * ratio, and background color
     *
     * @param colorIndex the index in the color table of the base color
     * @param fgRatio the ratio of foreground (base) color to background color
     * @param bg the background color
     */
    protected void mixColors(int colorIndex, float fgRatio, Color bg) {
        if (stereoMode == RG_STEREO_MODE) {
            rColor = stereoGreen;
            lColor = stereoRed;
        } else {
            rColor = lColor = Blender.blend(colorTab[colorIndex], bg, fgRatio);
        }
    }

    /**
     * draws the label for vertex <code>j</code> with the background color
     *
     * @param g the <code>Graphics</code> with which to draw the label
     * @param j the index of the vertex whose label to overpaint
     */
    protected void overpaintLabel(Graphics g, int j) {
        String label = (mod.getPointLabels())[j];
        
        g.setColor(bgColor);
        g.drawString(label, tvert[j][0], tvert[j][1]);
        if (stereoMode != MONO_MODE) {
            g.drawString(label, stvert[j][0], stvert[j][1]);
        }
    }

    /**
     * Calculates and displays the three orientation angles corresponding to the
     * current view
     *
     * @param g the <code>Graphics</code> with which to paint the angles
     * @param yPos the vertical position at which to paint the angles
     */
    protected void paintAngles(Graphics g, int yPos) {
        g.setColor(angleColor);
        double[] ang = calculateAngles();

        g.drawString("X: " + ang[0], 50, yPos - 10);
        g.drawString("Y: " + ang[1], 150, yPos - 10);
        g.drawString("Z: " + ang[2], 250, yPos - 10);
    }

    /**
     * Paints the first <code>n<code> connections, taking the current stereo
     * mode into account
     *
     * @param g the <code>Graphics</code> with which to paint the connections
     * @param n the number of connections to paint
     */
    protected void paintConnections(Graphics g, int n) {
        if (stereoMode == RG_STEREO_MODE) {
            colorTab[0] = stereoGreen;
            paintConnections(g, n, tvert, false);
            colorTab[0] = stereoRed;
            paintConnections(g, n, stvert, false);
        } else {
            paintConnections(g, n, tvert, true);
            if (stereoMode != MONO_MODE) {
                paintConnections(g, n, stvert, true);
            }
        }
    }

    /**
     * Paints the label of point <code>j<code>, taking into account the current
     * stereo mode
     *
     * @param g the <code>Graphics</code> with which to paint the label
     * @param j the index of the point whose label to paint
     */
    protected void paintLabel(Graphics g, int j) {
        String label = (mod.getPointLabels())[j];
        
        mixColors((mod.getPointTypes())[j], cueLevel(tvert[j][2]),
                bgColor);
        g.setColor(rColor);
        g.drawString(label, tvert[j][0], tvert[j][1]);
        if (stereoMode != MONO_MODE) {
            g.setColor(lColor);
            g.drawString(label, stvert[j][0], stvert[j][1]);
        }
    }

    /**
     * Paints the labels for the vertices that are displayed (as determined by
     * the <code>pointOK(int)</code> method); each label is painted by means of
     * the <code>paintLabel(Graphics, int)</code> method.
     *
     * @param g the <code>Graphics</code> with which to paint the labels
     */
    protected void paintLabels(Graphics g) {
        int nvert = mod.getNumPoints();
        for (int j = 0; j < nvert; j++) {
            if (pointOK(j)) {
                paintLabel(g, j);
            }
        }
    }

    /**
     * Adjusts the view transformation to put view at the correct position in
     * the window; this version derives the left-eye orientation if in stereo
     * mode and sets the positions correctly for left / right stereo
     */
    protected void positionView() {
        /* if in a stereo mode then derive the left-eye view from the
           right-eye view; in any case translate the view(s) to put it (them)
           in the correct position in the window */
        if (stereoMode != MONO_MODE) {
            lMat = rMat.copy();
            lMat.yrot(6.0);
            if (stereoMode == LR_STEREO_MODE) {
                lMat.translate((winWidth >> 2), (winHeight >> 1), 0f);
                rMat.translate(((3 * winWidth) >> 2), (winHeight >> 1), 0f);
            } else {
                lMat.translate((winWidth >> 1), (winHeight >> 1), 0f);
                rMat.translate((winWidth >> 1), (winHeight >> 1), 0f);
            }
        } else {
            super.positionView();
        }
    }

    /**
     * Responds to mouse events relevant to this panel; delegates to the
     * superclass first, then does its own handling
     *
     * @param e the <code>MouseEvent</code> to process
     */
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        switch (e.getID()) {
        case MouseEvent.MOUSE_CLICKED:
            handleClick(findPoint(e.getX(), e.getY()));
            break;
        }
    }

    /**
     * Transforms the first <code>np</code> points in the model
     *
     * @param np the number of points to transform
     */
    protected void transform(int np) {
        super.transform(np);
        if (stereoMode != MONO_MODE) {
            float[][] vert = getVertices();
            
            if ((stvert == null) || (stvert.length < vert.length)) {
                stvert = new int[vert.length][3];
            }
            lMat.transform(vert, stvert, np);
        }
    }
}
