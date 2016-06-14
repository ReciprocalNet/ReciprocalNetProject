/*
 * Reciprocal Net Project
 *
 * Rotate3DPanel.java
 *
 * 20-Nov-2002: jobollin changed thew import statements for Rotate3DModel,
 *              Matrix3D, ModelEvent, and ModelListener
 * 19-Dec-2002: jobollin made processKeyEvent() consume events that it
 *              intercepts as part of task #445
 * 26-Feb-2003: jobollin factored in pointOK() from Enhanced3DPanel and
 *              DemoRotatePanel
 * 26-Feb-2003: jobollin made all imports explicit
 * 26-Feb-2003: jobollin moved the body of the initialize() method into the
 *              two-arg constructor
 * 27-Feb-2003: jobollin removed the connect() method and bound the model
 *              more tightly
 * 03-Mar-2003: jobollin reformatted the source and wrote javadoc comments
 *              as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import org.recipnet.common.Matrix3D;
import org.recipnet.site.applet.jamm.Blender;
import org.recipnet.site.applet.jamm.ModelEvent;
import org.recipnet.site.applet.jamm.ModelListener;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * A specialized <code>JPanel</code> for displaying and manipulating a
 * three-dimensional wireframe model.  <code>Rotate3DPanel</code> uses a
 * separate data model class, <code>Rotate3DModel</code> to encapsulate points
 * and connections among those points.  Each point has an associated label and
 * type; <code>Rotate3DPanel</code> uses the point types as indices into an
 * array of <code>Color</code>s.
 */
public class Rotate3DPanel
        extends javax.swing.JPanel
        implements ModelListener {

    /** The default drawing color */
    protected final static Color DEFAULT_COLOR = Color.black;

    /** The default number of positions for the color table */
    protected final static int DEFAULT_NUM_COLOR = 100;

    /** A cached reference to the current background color */
    protected Color bgColor;

    /** the cumulative rotation matrix */
    protected Matrix3D aMat;

    /**
     * the transformation matrix for the current view; composed at need from
     * <code>aMat</code> and translation and scaling operations
     */
    protected Matrix3D rMat;

    /** The model displayed on this panel */
    protected Rotate3DModel mod;

    /** The table of colors by vertex type */
    protected Color[] colorTab;

    /** the coordinates of the transformed vertices of the model */
    protected int[][] tvert;

    /** A depth map of the vertices, from farthest away to closest */
    protected int[] zMap;

    /**
     * A boolean flag set to indicate that rotation around the X axis is
     * locked
     */
    protected boolean lockX;

    /**
     * A boolean flag set to indicate that rotation around the Y axis is
     * locked
     */
    protected boolean lockY;

    /**
     * A flag to indicate whether the currently cached transformed vertex
     * coordinates correspond to the latest view parameters
     */
    protected boolean transformed;

    /** The current scale factor */
    protected float scale;

    /**
     * The diameter of a sphere tightly enclosing the transformed coordinates
     */
    protected float scaledDiameter;

    /** The user specified shift in the X dimension */
    protected float shiftX;

    /** The user specified shift in the Y dimension */
    protected float shiftY;

    /** The X coordinate of the last mouse keypress */
    protected int mouseXDown = -1;

    /** The Y coordinate of the last mouse keypress */
    protected int mouseYDown = -1;

    /** cached value of the window height */
    protected int winHeight;

    /** cached value of the window width */
    protected int winWidth;

    /**
     * Constructs a new <code>Rotate3DPanel</code>
     */
    public Rotate3DPanel() {
        this(null, null);
    }

    /**
     * Constructs a new <code>Rotate3DPanel</code> with the specified model and
     * color table
     *
     * @param model the <code>Rotate3DModel</code> to display on this panel
     * @param tab the color table to use for displaying the model
     */
    public Rotate3DPanel(Rotate3DModel model, Color[] tab) {
        super(true);
        aMat = new Matrix3D();
        transformed = false;
        scale = 1f;
        shiftX = shiftY = 0f;
        lockX = lockY = false;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK
                | AWTEvent.FOCUS_EVENT_MASK);
        setTable(tab);
        setModel(model);
    }

    /**
     * Obtains the current connection array; subclasses can override this method
     * to alter the connections that are displayed
     */
    protected int[][] getConnections() {
        return mod.getConnections();
    }

    /**
     * Sets the model to be displayed; a new, empty model is used if
     * <code>m</code> is <code>null</code>
     *
     * @param m the <code>Rotate3DModel</code> to display on this panel
     */
    public void setModel(Rotate3DModel m) {
        if (m == null) {
            mod = new Rotate3DModel();
        } else {
            mod = m;
            rescale();
            repaint();
        }
    }

    /**
     * Gets the model currently being displayed by this panel
     *
     * @return the <code>Rotate3DModel</code> currently being displayed
     */
    public Rotate3DModel getModel() {
        return mod;
    }

    /**
     * Gets the current scale factor
     *
     * @return the <code>float</code> scale factor
     */
    public float getScale() {
        return scale;
    }

    /**
     * Gets the current user X translation
     *
     * @return the X translation
     */
    public float getShiftX() {
        return shiftX;
    }

    /**
     * Gets the current user Y translation
     *
     * @return the Y translation
     */
    public float getShiftY() {
        return shiftY;
    }

    /**
     * Gets the vertex coordinates; subclasses can override this method to
     * modify which vertices are drawn or for other reasons
     * 
     * @return the vertex coordinates as a <code>float[n][3]</code>
     */
    protected float[][] getVertices() {
        return mod.getPoints();
    }

    /**
     * Sets the color table values according to those in the argument. If the
     * argument is <code>null</code> then a non-<code>null</code> table is left
     * alone, and a <code>null</code> one is initialized to an array of default
     * length containing the default color at every position. If the argument is
     * non-<code>null</code>, then all non-<code>null</code> elements are copied
     * to the corresponding position in the table, which is extended, if
     * necessary, to accommodate them.
     *
     * @param t a <code>Color[]</code> containing the colors to use for various
     *        vertex types
     */
    public void setTable(Color[] t) {
        /* if t is null then just initialize colorTab as necessary */
        if (t == null) {
            if (colorTab != null) {
                return;
            }
            colorTab = new Color[DEFAULT_NUM_COLOR];
            Arrays.fill(colorTab, DEFAULT_COLOR);
        } else {
            /* initialize or extend colorTab to accommodate all elements of t */
            if (colorTab == null) {
                colorTab = new Color[t.length];
            } else if (colorTab.length < t.length) {
                Color[] tt = new Color[t.length];
                System.arraycopy(colorTab, 0, tt, 0, colorTab.length);
                colorTab = tt;
            }
            /* copy non-null elements of t and initialize colorTab elements
               where necessary */
            for (int i = 0; i < t.length; i++) {
                if (t[i] != null) {
                    colorTab[i] = t[i];
                } else if (colorTab[i] == null) {
                    colorTab[i] = DEFAULT_COLOR;
                }
            }
            /* initialize any remaining null elements of colorTab */
            for (int i = t.length; i < colorTab.length; i++) {
                if (colorTab[i] == null) {
                    colorTab[i] = DEFAULT_COLOR;
                }
            }
        }
        tableUpdated();
        repaint();
    }

    /**
     * Gets the current color table
     *
     * @return a <code>Color[]</code> containing the current colors for each
     *         vertex type
     */
    public Color[] getTable() {
        return colorTab;
    }

    /* instance methods */
    public double[] calculateAngles() {
        double[] angles = new double[3];
        double x = Math.atan2(aMat.zy, aMat.zz);
        double sinx = Math.sin(x);
        double cosy =
            (sinx <= .70711) ? (aMat.zz / Math.cos(x)) : (aMat.zy / sinx);
        angles[0] = Math.toDegrees(x);
        angles[1] = Math.toDegrees(Math.atan2(-(aMat.zx), cosy));
        angles[2] = Math.toDegrees(Math.atan2(aMat.yx, aMat.xx));
        for (int i = 0; i < 3; i++) {
            angles[i] = Math.rint(angles[i] * 100d) / 100d;
        }
        return angles;
    }

    /**
     * Implementation method of the <code>ModelListener</code> interface;
     * triggers appropriate actions when the current model is modified
     *
     * @param modelEvent a <code>ModelEvent</code> representing the event
     *        that occurred
     */
    public void modelChanged(ModelEvent modelEvent) {
        if (mod.equals(modelEvent.getSource())
                && ((modelEvent.getId()
                & (ModelEvent.CONNECTIONS_MASK | ModelEvent.VERTICES_MASK)) != 0)) {
            rescale();
            repaint();
        }
    }

    /**
     * Displays the model in the component's window according to the options
     * selected; in doing so it performs these steps:
     * <ul>
     *   <li>constructs a transformation that translates the model centroid
     *       to the coordinate origin, performs user-specified rotation,
     *       scaling, and translation</li>
     *   <li>repositions the view with <code>positionView()</code></li>
     *   <li>transforms the vertices with <code>transform()</code></li>
     *   <li>draws the model with paintConnections()</code></li>
     * </ul>
     * 
     * @param g the <code>Graphics</code> with which to do the painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if ((mod == null) || (mod.getNumPoints() <= 1)) {
            return;
        }

        /* Prepare parameters */
        bgColor = getBackground(); // Cache the current background color
        scaledDiameter = 2.0f * mod.getRadius() * scale;

        /* initialize the view matrix */
        if (rMat == null) {
            rMat = new Matrix3D();
        } else {
            rMat.unit();
        }

        /* put the molecular centroid at the rotation origin */
        rMat.translate(-mod.getCenterX(), -mod.getCenterY(), -mod.getCenterZ());

        /*
         * apply the rotation, translation, and scale, accounting for the
         * display's inverted Y axis
         */
        rMat.mult(aMat);
        rMat.translate(shiftX, -shiftY, 0f);
        rMat.scale(scale, -scale, scale);

        positionView();
        transform();
        paintConnections(g);
    }

    /**
     * Rescales the display contents
     */
    protected void rescale() {
        winWidth = getWidth();
        winHeight = getHeight();
        rescale(winWidth, winHeight);
    }

    /**
     * Rescales the display contents according to the specified dimensions
     *
     * @param w the width to rescale to
     * @param h the height to rescale to
     */
    protected void rescale(int w, int h) {
        float r = mod.getRadius();
        if (r > 0.1) {
            scale = 0.40f * (minDimension(w, h) / r);
        }
    }

    /**
     * Resets the orientation and scale, but not the stereo option.
     */
    public void reset() {
        aMat.unit();
        rescale();
        shiftX = shiftY = 0f;
        transformed = false;
        repaint();
    }

    /**
     * Changes the size and position of this component
     *
     * @param x the new X coordinate
     * @param y the new Y coordinate
     * @param w the new width
     * @param h the new height
     */
    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
        rescale(winWidth = w, winHeight = h);
    }

    /**
     * Calculates the correct foreground color : background color ratio for
     * a vertex with the specified Z coordinate (for depth cueing)
     *
     * @param  z the Z coordinate as a <code>float</code>
     *
     * @return the foreground color : background color ratio as a
     *         <code>float</code>
     */
    protected float cueLevel(float z) {
        return (0.0199999f + (0.98f * ((z / scaledDiameter) + 0.5f)));
    }

    /**
     * Returns the minimum dimension of the available drawing area corresponding
     * to the panel display dimensions provided.  This may differ from the
     * minimum of the two arguments; for instance, if only part of the available
     * area is to be used.  The result may be used, among other things, to
     * calculate scale factors.
     *
     * @param w the width in pixels
     * @param h the height in pixels
     *
     * @return the minimum dimension of the available drawing area
     */
    protected int minDimension(int w, int h) {
        return Math.min(w, h);
    }

    /**
     * Displays a connection between points <code>i1</code> and <code>i1</code>
     * of the supplied coordinate array; the line is colored according to the
     * color table for the corresponding vertex types if <code>multicolor</code>
     * is <code>true</code>, or according to the default color otherwise
     *
     * @param g the <code>Graphics</code> with which to draw the connection
     * @param i1 the index of the first point
     * @param i2 the index of the second point
     * @param points a <code>float[][]</code> containing all the point
     *        coordinates
     * @param multicolor <code>true</code> to use the color table colors,
     *        <code>false</code> to use <code>g</code>'s current color
     */
    protected void paintConnection(Graphics g, int i1, int i2, int[][] points,
        boolean multicolor) {
        int[] type = mod.getPointTypes();
        int midx;
        int midy;
        float z1;
        float z2;

        /* segment depths */
        z1 = ((3 * points[i1][2]) + points[i2][2]) / 4f;
        z2 = (points[i1][2] + (3 * points[i2][2])) / 4f;

        /* connection midpoint */
        midx = (points[i1][0] + points[i2][0]) / 2;
        midy = (points[i1][1] + points[i2][1]) / 2;

        /* first half */
        if (multicolor) {
            g.setColor(Blender.blend(colorTab[type[i1]], bgColor, cueLevel(z1)));
        } else {
            g.setColor(Blender.blend(colorTab[0], bgColor, cueLevel(z1)));
        }
        g.drawLine(points[i1][0], points[i1][1], midx, midy);

        /* second half */
        if (multicolor) {
            g.setColor(Blender.blend(colorTab[type[i2]], bgColor, cueLevel(z2)));
        }
        g.drawLine(points[i2][0], points[i2][1], midx, midy);
    }

    /**
     * Paints all the model's connections
     *
     * @param g the <code>Graphics</code> with which to paint the connections
     */
    protected void paintConnections(Graphics g) {
        paintConnections(g, mod.getNumConnections());
    }

    /**
     * Paints the first <code>n</code> of the model's connections
     *
     * @param g the <code>Graphics</code> with which to paint the connections
     * @param n the number of connections to paint
     */
    protected void paintConnections(Graphics g, int n) {
        paintConnections(g, n, tvert, true);
    }

    /**
     * Paints the first <code>n</code> of the model's connections, based on the
     * provided coordinates for the model vertices
     *
     * @param g the <code>Graphics</code> with which to paint the connections
     * @param n the number of connections to paint
     * @param points the coordinates of the model vertices to use
     * @param multicolor <code>true</code> to color the connections according to
     *        the colors specified for the endpoints' types; <code>false</code>
     *        to use <code>g</code>'s current drawing color
     */
    protected void paintConnections(Graphics g, int n, int[][] points,
        boolean multicolor) {
        int[][] con = getConnections();
        
        for (int i = 0; i < n; i++) {
            paintConnection(g, con[i][0], con[i][1], points, multicolor);
        }
    }

    /**
     * Adjusts the view transformation to put view at the correct position in
     * the window; this version just translates the coordinate origin to the
     * center of the viewing area
     */
    protected void positionView() {
        rMat.translate((winWidth >> 1), (winHeight >> 1), 0f);
    }

    /**
     * Applies special handling for focus events that occur on this component
     *
     * @param e the <code>FocusEvent</code> representing the event that occurred
     */
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        if (e.getID() == FocusEvent.FOCUS_LOST) {
            lockX = lockY = false;
        }
    }

    /**
     * Applies special handling for keyboard events that occur on this component
     *
     * @param e the <code>KeyEvent</code> representing the event that occurred
     */
    protected void processKeyEvent(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_RELEASED) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_X:
                lockY = false;
                break;
            case KeyEvent.VK_Y:
                lockX = false;
                break;
            default:
                super.processKeyEvent(e);
                return;
            }
            e.consume();
        } else if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                scale = -scale;
                transformed = false;
                repaint();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                aMat.yrot(-6.0);
                transformed = false;
                repaint();
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                aMat.yrot(6.0);
                transformed = false;
                repaint();
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                aMat.xrot(6.0);
                transformed = false;
                repaint();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                aMat.xrot(-6.0);
                transformed = false;
                repaint();
                break;
            default:
                if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_X:
                        lockY = true;
                        break;
                    case KeyEvent.VK_Y:
                        lockX = true;
                        break;
                    default:
                        super.processKeyEvent(e);
                        return;
                    }
                } else {
                    super.processKeyEvent(e);
                    return;
                }
            }
            e.consume();
        } else {
            super.processKeyEvent(e);
        }
    }

    /**
     * Applies special handling for mouse events that occur on this component
     * and don't involve mouse movement
     *
     * @param e the <code>MouseEvent</code> representing the event that occurred
     */
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            requestFocus();
            mouseXDown = e.getX();
            mouseYDown = e.getY();
        }
    }

    /**
     * Applies special handling for mouse events that occur on this component
     * and do involve mouse movement
     *
     * @param e the <code>MouseEvent</code> representing the event that occurred
     */
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int m = e.getModifiers();
            /* scale */
            if ((m
                    & (InputEvent.BUTTON2_MASK | InputEvent.BUTTON3_MASK
                    | InputEvent.ALT_MASK)) != 0) {
                int mdxc = mouseXDown - (winWidth / 2);
                int mdyc = mouseYDown - (winHeight / 2);
                int mxc = mouseX - (winWidth / 2);
                int myc = mouseY - (winHeight / 2);
                double mr = Math.max(1d, Math.sqrt((mxc * mxc) + (myc * myc)));
                double mdr =
                    Math.max(1d, Math.sqrt((mdxc * mdxc) + (mdyc * mdyc)));
                scale *= (mr / mdr);
                /* translate */
            } else if ((m & InputEvent.SHIFT_MASK) != 0) {
                shiftX += ((mouseX - mouseXDown) / scale);
                shiftY += ((mouseY - mouseYDown) / scale);
                /* rotate around Z */
            } else if (((m & InputEvent.CTRL_MASK) != 0) && !lockX && !lockY) {
                int mouseXC = mouseX - (winHeight / 2);
                int mouseYC = mouseY - (winWidth / 2);
                int mouseXDownC = mouseXDown - (winHeight / 2);
                int mouseYDownC = mouseYDown - (winWidth / 2);
                double mouseR =
                        Math.sqrt(((mouseXC * mouseXC) + (mouseYC * mouseYC)));
                double mouseRDown = Math.sqrt(((mouseXDownC * mouseXDownC)
                        + (mouseYDownC * mouseYDownC)));
                double cosTheta =
                    ((mouseXC * mouseXDownC) + (mouseYC * mouseYDownC))
                    / (mouseR * mouseRDown);
                float angle = (float) Math.toDegrees(Math.acos(cosTheta));
                aMat.zrot(((mouseYC * mouseXDownC) < (mouseXC * mouseYDownC))
                    ? angle : (-angle));
                /* normal rotate */
            } else {
                if (!lockX) {
                    float xTheta = ((mouseYDown - mouseY) * 360.0f) / winWidth;
                    aMat.xrot(xTheta);
                }
                if (!lockY) {
                    float yTheta = ((mouseX - mouseXDown) * 360.0f) / winHeight;
                    aMat.yrot(yTheta);
                }
            }
            transformed = false;
            repaint();
            mouseXDown = mouseX;
            mouseYDown = mouseY;
        }
    }

    /**
     * Performs appropriate action in response to the model's color table being
     * updated.  This version does nothing, but subclasses may override it to
     * insert their own handling for this situation.
     */
    protected void tableUpdated() {
        /* A hook for this class' descendants */
    }

    /**
     * Checks whether a transformation is required, and if so, invokes it via
     * <code>transform(int)</code>
     */
    protected void transform() {
        int np = mod.getNumPoints();
        if (transformed || (np <= 0)) {
            return;
        }
        transform(np);
        transformed = true;
    }

    /**
     * Transforms the first <code>np</code> points according to the current
     * transformation matrix; will replace the <code>tvert</code> array if it
     * is <code>null</code> or too short
     *
     * @param np the number of points to transform
     */
    protected void transform(int np) {
        float[][] vert = getVertices();
        
        if ((tvert == null) || (tvert.length < vert.length)) {
            tvert = new int[vert.length][3];
        }
        rMat.transform(vert, tvert, np);
    }

    /**
     * Sorts the transformed coordinates into ascending order by z coordinate.
     * This can be used to display the points in depth order.
     */
    protected void zSort() {
        int i;
        int nvert = mod.getNumPoints();
        if ((zMap == null) || (zMap.length < nvert)) {
            zMap = new int[nvert];
            for (i = 0; i < nvert; i++) {
                zMap[i] = i;
            }
        }
        int j;
        int k;
        int mx;
        int mn;
        for (i = 0, j = nvert - 1; i < j; i++, j--) {
            mn = j;
            mx = j;
            for (k = i; k < j; k++) {
                if (tvert[zMap[k]][2] < tvert[zMap[mn]][2]) {
                    mn = k;
                } else if (tvert[zMap[k]][2] > tvert[zMap[mx]][2]) {
                    mx = k;
                }
            }
            if (mn != i) {
                k = zMap[i];
                zMap[i] = zMap[mn];
                zMap[mn] = k;
            }
            if (mx != j) {
                if (mx == i) {
                    if (mn == j) {
                        continue;
                    } else {
                        mx = mn;
                    }
                }
                k = zMap[j];
                zMap[j] = zMap[mx];
                zMap[mx] = k;
            }
        }
    }

    /**
     * Rotates the current view around the x (horizontal, in-plane) axis
     *
     * @param theta the rotation angle in degrees
     */
    public void xrot(float theta) {
        aMat.xrot(theta);
        transformed = false;
        repaint();
    }

    /**
     * Rotates the current view around the y (vertical, in-plane) axis
     *
     * @param theta the rotation angle in degrees
     */
    public void yrot(float theta) {
        aMat.yrot(theta);
        transformed = false;
        repaint();
    }

    /**
     * Rotates the current view around the z (perpendicular) axis
     *
     * @param theta the rotation angle in degrees
     */
    public void zrot(float theta) {
        aMat.zrot(theta);
        transformed = false;
        repaint();
    }

    /**
     * Determines whether the specified vertex is "ok".  Subclasses may assign
     * their own interpretation of "ok" by overriding this method, and may
     * choose any significance they wish to the attribute.  This version
     * always returns <code>true</code>.
     *
     * @param i the point index
     *
     * @return <code>true</code> if the point is "ok"; <code>false</code>
     *         otherwise
     */
    protected boolean pointOK(int i) {
        return true;
    }

}
