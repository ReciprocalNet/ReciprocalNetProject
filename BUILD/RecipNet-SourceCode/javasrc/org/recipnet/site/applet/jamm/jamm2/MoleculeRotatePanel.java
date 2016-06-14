/*
 * Reciprocal Net Project
 *
 * MoleculeRotatePanel.java
 *
 * 20-Nov-2002: jobollin changed the import statment for Rotate3DModel
 * 25-Feb-2003: jobollin reformatted the source as part of task #749
 * 26-Feb-2003: jobollin moved the body of the initialize method into the
 *              three-arg constructor
 * 26-Feb-2003: jobollin removed the connect() method and placed the
 *              functionality into setModel() and modelChanged()
 * 27-Feb-2003: jobollin improved support for selectable hydrogen atom display
 *              with a new getConnections() method
 * 27-Feb-2003: jobollin rrevised and expanded the javadoc comments as part of
 *              task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 * 25-May-2006: jobollin updated this class to reduce the default ball size in
 *              ball & stick mode; reformatted the source; updated docs
 * 03-Nov-2006: jobollin removed debugging statements
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.recipnet.site.applet.jamm.MessageEvent;
import org.recipnet.site.applet.jamm.ModelEvent;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * Extends Enhanced3DPanel by providing features relevant for rendering and
 * examining molecules. Added features include ball & stick and space-filled
 * rendering modes, support for optional display/hiding of hydrogen atoms, and
 * distance and angle calculations.
 */
class MoleculeRotatePanel extends Enhanced3DPanel {

    /** A code for line drawing mode */
    final static int LINE_MODE = 0;

    /** A code for ball &amp; stick drawing mode */
    final static int BALL_STICK_MODE = 1;

    /** A code for space-filled drawing mode */
    final static int SPACE_MODE = 2;

    /** The maximum legal value for the drawing mode code */
    final static int MAX_DRAW_MODE = SPACE_MODE;

    /** A code for atom selection mode */
    final static int ATOM_MODE = 0;

    /** A code for distance selection mode */
    final static int DISTANCE_MODE = 1;

    /** A code for angle selection mode */
    final static int ANGLE_MODE = 2;

    /** The maximum valid selection mode constant */
    final static int MAX_INPUT_MODE = ANGLE_MODE;

    /** Text labels for the rendering modes */
    final static String[] modes = { "line", "ball", "space" };
    
    /**
     * A scale factor (further) modifying the ball size rendered in
     * ball & stick mode, both on-screen and in server-rendered images
     */ 
    private final static double BALL_SCALE_FACTOR = 0.65;

    /** a local copy of the connections array, suitable for sorting */
    private int[][] con = null;

    /** an alternative local connections array */
    private int[][] alternateCon = null;

    /**
     * The {@code Atom} used for all green atoms in red / green stereo mode
     */
    protected Atom greenAtom = null;

    /**
     * The {@code Atom} used for all red atoms in red / green stereo mode
     */
    protected Atom redAtom = null;

    /** A format for displaying numbers to two decimal places */
    protected DecimalFormat hundredthsFormat;

    /** A format for displaying numbers to three decimal places */
    protected DecimalFormat thousandthsFormat;

    /**
     * an array containing the {@code Atom}s to use for each relevant ball
     * type, indexed by ball type
     */
    protected Atom[] balls;

    /** an array containing all the known ball radii, indexed by ball type */
    protected float[] radii;

    /** The indices of the currently selected atoms */
    protected int[] selectedAtoms;

    /** Flags whether or not hydrogen display is enabled */
    protected boolean hydro;

    /** the scale factor for balls in ball &amp; and stick mode */
    protected double ballSizeBall;

    /** the scale factor for balls in space-filled mode */
    protected double ballSizeSpace;

    /** the current drawing mode */
    protected int drawMode;

    /** the number of atoms currently selected */
    protected int numSelected;

    /** the current output (selection) mode */
    protected int outputMode;

    /**
     * preserves the previously chosen state of the label display option when
     * not in line drawing mode
     */
    private boolean savedLabelState;

    /** The number of connections not including hydrogen atoms */
    private int numNonHCon;

    /** indicates whether or not this object has been initialized */
    private boolean initialized = false;

    /**
     * Creates a new {@code MoleculeRotatePanel}
     */
    public MoleculeRotatePanel() {
        this(null, null, null);
    }

    /**
     * Creates a new {@code MoleculeRotatePanel} with the specified model and
     * color table
     * 
     * @param model the {@code Rotate3DModel} to display on this panel
     * @param tab a {@code Color[]} containing the colors with which to display
     *        the model
     */
    public MoleculeRotatePanel(Rotate3DModel model, Color[] tab) {
        this(model, tab, null);
    }

    /**
     * Creates a new {@code MoleculeRotatePanel} with the specified model, color
     * table, and radius table
     * 
     * @param model the {@code Rotate3DModel} to display on this panel
     * @param tab a {@code Color[]} containing the colors with which to display
     *        the model
     * @param rad a {@code float[]} containing the radii for elements contained
     *        in the model
     */
    public MoleculeRotatePanel(Rotate3DModel model, Color[] tab, float[] rad) {
        super(model, tab);
        if (rad == null) {
            defaultRadii();
        } else {
            setRadii(rad);
        }
        drawMode = LINE_MODE;
        outputMode = ATOM_MODE;
        hydro = false;
        savedLabelState = labels;
        hundredthsFormat = new DecimalFormat("##0.00");
        thousandthsFormat = new DecimalFormat("##0.000");
        ballSizeBall = 1d;
        ballSizeSpace = 1d;
        con = mod.getConnections();
        initialized = true;
        tableUpdated();
        hsort();
    }

    /**
     * Sets the background color for this applet
     * 
     * @param bg a {@code Color} to use for the panel background
     */
    public void setBackground(Color bg) {
        super.setBackground(bg);
        reinitBalls();
    }

    /**
     * Sets the ball size for ball &amp; stick mode
     * 
     * @param bs a {@code double} containing the new ball size
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
     * @return the ball size as a {@code double}
     */
    public double getBallSizeBall() {
        return ballSizeBall;
    }

    /**
     * Sets the ball size for space-filled mode
     * 
     * @param bs a {@code double} containing the new ball size
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
     * @return the ball size as a {@code double}
     */
    public double getBallSizeSpace() {
        return ballSizeSpace;
    }

    protected int[][] getConnections() {
        return (alternateCon == null) ? con : alternateCon;
    }

    /**
     * Sets the drawing mode to {@code mode}, provided that {@code mode} is a
     * valid drawing mode for this panel
     * 
     * @param mode the new drawing mode code
     */
    public void setDrawMode(int mode) {
        if ((mode >= 0) && (mode <= MAX_DRAW_MODE)) {
            if (mode == LINE_MODE) {
                labels = savedLabelState;
            } else {
                transformed = false;
                if (drawMode == LINE_MODE) {
                    savedLabelState = labels;
                    labels = false;
                }
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

    /**
     * Returns the text label corresponding to the current drawing mode
     * 
     * @return a {@code String} containing the label
     */
    public String getDrawModeString() {
        return modes[drawMode];
    }

    /**
     * Sets the hydrogen atom display mode
     * 
     * @param h {@code true} to display hydrogen atoms, {@code false} to hide
     *        them
     */
    public void setHydro(boolean h) {
        if (hydro != h) {
            hydro = h;
            repaint();
        }
    }

    /**
     * Returns the current hydrogen display mode
     * 
     * @return {@code true} if hydrogen atoms are displayed, {@code false} if
     *         not
     */
    public boolean getHydro() {
        return hydro;
    }

    /**
     * Toggles the hydrogen display mode
     * 
     * @return the new mode
     */
    public boolean toggleHydro() {
        setHydro(!hydro);
        repaint();
        return hydro;
    }

    /**
     * {@inheritDoc}
     */
    public void setModel(Rotate3DModel m) {
        super.setModel(m);
        if (m != null) {
            hsort();
            selectedAtoms = new int[m.getNumPoints()];
            clearSelection();
            if (initialized) {
                balls = null;
                reinitBalls();
                con = m.getConnections();
            }
        }
    }

    /**
     * Sorts the local connections array so that connections including hydrogen
     * atoms come at the end
     */
    protected void hsort() {
        int[][] connections = getConnections();

        if (connections != null) {
            HydrogenToEndComparator c
                    = new HydrogenToEndComparator(mod.getPointTypes());

            Arrays.sort(connections, c);
            numNonHCon = c.numNonHCon(connections);
        } else {
            numNonHCon = 0;
        }
    }

    /**
     * Returns the code for the current output (selection) mode
     * 
     * @return the code for the current mode
     */
    public int getOuputMode() {
        return outputMode;
    }

    /**
     * Sets the output (selection) mode
     * 
     * @param mode the code for the mode to set
     */
    public void setOutputMode(int mode) {
        if ((mode >= 0) && (mode <= MAX_INPUT_MODE)) {
            clearSelection();
            outputMode = mode;
            repaint();
        }
    }

    /**
     * Returns the atom radius table
     * 
     * @return the radius table as a {@code float[]}
     */
    public float[] getRadii() {
        return radii;
    }

    /**
     * Sets a new atom radius table
     * 
     * @param rad a {@code float[]} containing the new radii, indexed by atomic
     *        number
     */
    protected void setRadii(float[] rad) {
        if (rad != null) {
            radii = rad;
            repaint();
        }
    }

    /**
     * Returns the ball size scale factor in use in the current drawing mode
     * 
     * @return a {@code double} containing the ball size scale factor
     */
    public double getRelativeBallSize() {
        double rval;
        switch (drawMode) {
            case BALL_STICK_MODE:
                rval = ballSizeBall * BALL_SCALE_FACTOR;
                break;
            case SPACE_MODE:
                rval = ballSizeSpace;
                break;
            default:
                rval = 0d;
        }
        return rval;
    }

    /**
     * {@inheritDoc}
     */
    public void setStereoMode(int mode) {
        super.setStereoMode(mode);
        if (stereoMode == RG_STEREO_MODE) {
            Color bg = getBackground();
            redAtom = new Atom(stereoRed, bg);
            greenAtom = new Atom(stereoGreen, bg);
            repaint();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (drawMode != LINE_MODE) {
            paintBalls(g);
        }
        if (numSelected > 0) {
            paintSelection(g);
        }
    }

    /**
     * Resets the orientation and scale, but not the stereo option; also clears
     * the current selection, if any
     */
    public void reset() {
        clearSelection();
        super.reset();
    }

    /**
     * Deselects any currently selected atoms
     */
    protected void clearSelection() {
        numSelected = 0;
    }

    /**
     * Creates a default atomic radius table
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

    protected void handleClick(int p) {
        if (p < 0) {
            clearSelection();
            repaint();
        } else {
            if (numSelected > outputMode) {
                clearSelection();
            }
            selectAtom(p);
            repaint();
            switch (outputMode) {
                case ATOM_MODE:
                    reportAtom();
                    break;
                case DISTANCE_MODE:
                    if (numSelected == 2) {
                        reportDistance();
                    }
                    break;
                case ANGLE_MODE:
                    if (numSelected == 3) {
                        reportAngle();
                    }
                    break;
            }
        }
    }

    /**
     * Paints the atom balls on the image. In space-filling mode the ball scale
     * is set so as to cause significant overlap; otherwise the ball scale is
     * set so as to leave plenty of each stick visible. This method is
     * measurably slower in red/green stereo mode than in mono or L/R stereo
     * mode because in R/G mode two ball images must be rendered for each atom
     * whereas in any other mode only one is rendered (but may be drawn twice).
     * 
     * @param g the {@code Graphics} with which to draw the balls
     */
    protected void paintBalls(Graphics g) {
        int[] type = mod.getPointTypes();

        if (drawMode == SPACE_MODE) {
            Atom.setBallScale((float) (scale * 1.7 * ballSizeSpace));
        } else {
            Atom.setBallScale(
                    (float) (scale * ballSizeBall * (BALL_SCALE_FACTOR / 2)));
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
                if (stereoMode == RG_STEREO_MODE) {
                    I = greenAtom.render(zLevel, tvert[j][2], radii[n]);
                    offset = I.getWidth(null) >> 1;
                    g.drawImage(I, tvert[j][0] - offset, tvert[j][1] - offset,
                            this);
                    I = redAtom.render(zLevel, tvert[j][2], radii[n]);
                    g.drawImage(I, stvert[j][0] - offset,
                            stvert[j][1] - offset, this);
                } else {
                    I = balls[n].render(zLevel, tvert[j][2], radii[n]);
                    offset = I.getWidth(null) >> 1;
                    g.drawImage(I, tvert[j][0] - offset, tvert[j][1] - offset,
                            this);
                    if (stereoMode == LR_STEREO_MODE) {
                        g.drawImage(I, stvert[j][0] - offset, stvert[j][1]
                                - offset, this);
                    }
                }
            }
        }
    }

    /**
     * Paints all the model's bonds, subject to any options in effect
     * 
     * @param g the {@code Graphics} with which to paint the connections
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
     * Paints the current selection, if any
     * 
     * @param g the {@code Graphics} with which to paint
     */
    protected void paintSelection(Graphics g) {
        Font f1 = g.getFont();
        Font f2 = f1.deriveFont(Font.BOLD);

        for (int i = 0; i < numSelected; i++) {
            overpaintLabel(g, selectedAtoms[i]);
            g.setFont(f2);
            paintLabel(g, selectedAtoms[i]);
            g.setFont(f1);
        }
        if (numSelected > 1) {
            int numConnections = numSelected - 1;
            int[][] tCon = new int[numConnections][2];
            Graphics2D g2d = (Graphics2D) g.create();
            float[] dash = { 6f };

            g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 10f, dash, 9f));
            for (int i = 0; i < numConnections; i++) {
                tCon[i][0] = selectedAtoms[i];
                tCon[i][1] = selectedAtoms[i + 1];
            }
            alternateCon = tCon;
            paintConnections(g2d, numConnections);
            alternateCon = null;
        }
    }

    /**
     * Determines whether the specified atom is "ok" to display. This version
     * returns {@code true} unless the specified atom is a hydrogen and hydrogen
     * display is turned off
     * 
     * @param i the point index
     * @return {@code true} if the atom is "ok" for display; {@code false}
     *         otherwise
     */
    protected boolean pointOK(int i) {
        return (((mod.getPointTypes())[i] > 1) || hydro);
    }

    /**
     * Initializes any missing, required balls, and synchronizes exisiting balls
     * with the current background color
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
     * Calculates the angle described by the current selection (if any) and
     * fires a {@code MessageEvent} containing a description of the angle and
     * its measure
     */
    protected void reportAngle() {
        if (numSelected < 3) {
            return;
        }
        String[] pLabels = mod.getPointLabels();
        float[][] vert = getVertices();
        double ca = 0d;
        double l1 = 0d;
        double l2 = 0d;
        float d1;
        float d2;

        for (int i = 0, j1 = selectedAtoms[0], j2 = selectedAtoms[1],
                j3 = selectedAtoms[2]; i < 3; i++) {
            d1 = (vert[j1][i] - vert[j2][i]);
            d2 = (vert[j3][i] - vert[j2][i]);
            l1 += (d1 * d1);
            l2 += (d2 * d2);
            ca += (d1 * d2);
        }
        ca /= Math.sqrt(l1 * l2);
        double a = Math.toDegrees(Math.acos(ca));
        fireMessageEvent(new MessageEvent(this, "Angle  "
                + pLabels[selectedAtoms[0]] + " -- " + pLabels[selectedAtoms[1]]
                + " -- " + pLabels[selectedAtoms[2]] + " :  "
                + hundredthsFormat.format(a) + " degrees"));
    }

    /**
     * Fires a {@code MessageEvent} containing a description of the (first)
     * currently selected atom, if any
     */
    protected void reportAtom() {
        if (numSelected < 1) {
            return;
        }
        if (hundredthsFormat == null) {
            System.out.println("Format null!");
        }
        fireMessageEvent(new MessageEvent(this, "Atom  "
                + (mod.getPointLabels())[selectedAtoms[0]]
                + ", 3D view coordinates:  "
                + hundredthsFormat.format((double) tvert[selectedAtoms[0]][0])
                + "  "
                + hundredthsFormat.format((double) tvert[selectedAtoms[0]][1])
                + "  "
                + hundredthsFormat.format((double) tvert[selectedAtoms[0]][2])));
    }

    /**
     * Calculates the distance described by the current selection (if any) and
     * fires a {@code MessageEvent} containing a description of the distance and
     * its measure
     */
    protected void reportDistance() {
        if (numSelected < 2) {
            return;
        }
        String[] pLabels = mod.getPointLabels();
        float[][] vert = getVertices();
        double d = 0d;
        float t;

        for (int i = 0, j1 = selectedAtoms[0], j2 = selectedAtoms[1]; i < 3; i++) {
            t = vert[j1][i] - vert[j2][i];
            d += (t * t);
        }
        d = Math.sqrt(d);
        fireMessageEvent(new MessageEvent(this, "Distance  "
                + pLabels[selectedAtoms[0]] + " -- " + pLabels[selectedAtoms[1]]
                + " :  " + thousandthsFormat.format(d) + " Angstroms"));
    }

    /**
     * Adds the specified atom to the list of selected atoms
     * 
     * @param i DOCUMENT ME!
     */
    protected void selectAtom(int i) {
        for (int j = 0; j < numSelected; j++) {
            if (selectedAtoms[j] == i) {
                return;
            }
        }
        selectedAtoms[numSelected++] = i;
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
     * Transforms the first {@code np} points in the model; this version also
     * invokes a depth sort of the coordinates unless the panel is in line
     * drawing mode.
     * 
     * @param np the number of points to transform
     */
    protected void transform(int np) {
        super.transform(np);
        if (drawMode != LINE_MODE) {
            zSort();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void modelChanged(ModelEvent modelEvent) {
        super.modelChanged(modelEvent);
        if ((modelEvent.getId() & ModelEvent.VERTICES_MASK) != 0) {
            reinitBalls();
        }
        if ((modelEvent.getId() & ModelEvent.CONNECTIONS_MASK) != 0) {
            con = mod.getConnections();
            hsort();
        }
    }
}
