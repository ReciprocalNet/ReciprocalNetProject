/*
 * Reciprocal Net Project
 *
 * miniJaMM.java
 *
 * 20-Nov-2002: jobollin changed the import statments for Rotate3DModel and
 *              Matrix3D
 * 13-Dec-2002: jobollin made all import statements explicit
 * 17-Dec-2002: jobollin added support for loading table.dat from the applet
 *              jar as part of task #509
 * 09-Jan-2003: jobollin cleaned up unused imports
 * 19-Feb-2003: jobollin changed the default element data file name from
 *              table.dat to element_data.txt as part of task #682
 * 20-Feb-2003: jobollin removed references to Java 2 classes as part of task
 *              #682
 * 20-Feb-2003: jobollin worked around a bug in Netscape 4's JRE -- it's
 *              java.lang.ClassLoader.getResource(String) method has a null
 *              implementation (part of task #682)
 * 21-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #742
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.minijamm
 *              package to org.recipnet.site.applet.jamm.minijamm; changed
 *              package references to match source tree reorganization
 * 24-Jan-2006: jobollin added support for a "showLabels" parameter that
 *              controls the initial state of label display; changed a few
 *              method and field names; made fields private
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.minijamm;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.recipnet.site.applet.jamm.Rotate3DView;
import org.recipnet.site.applet.jamm.WebFiles;

/**
 * A lightweight, Java 1.1 compatible JaMM implementation that presents a color
 * line drawing molecule view with optional hydrogen atoms, optional atom
 * labels, and optional rotation angles
 */
public class miniJaMM extends Applet {

    /** A <code>Background</code> for this applet */
    private Background back;

    /** A <code>Border</code> for this applet */
    private Border border;

    /** The background color for this applet */
    private Color backgroundColor;

    /** The color for all text displayed by this applet */
    private Color textColor;

    /** The font with which all text will be diplayed by this applet */
    private Font chemfont;

    /** A graphics context used for drawing the offscreen view */
    private Graphics offscreen;

    /** An image containing the onscreen view */
    private Image screen;

    /** The view of the model */
    private Rotate3DView view;

    /** Any error message produced during initialization */
    private String message = null;

    /** flags whether or not rotation showAngles should be displayed */
    private boolean showAngles = false;

    /** flags whether or not to display the help text */
    private boolean help = false;

    /** flags whether or not to use a <code>Background</code> */
    private boolean isbackground = false;

    /** flags whether or not to use a border */
    private boolean isborder = false;

    /** The most recent previously recorded x coordinate of the mouse pointer */
    private int prevx;

    /** The most recent previously recorded y coordinate of the mouse pointer */
    private int prevy;

    /**
     * Performs one-time initialization tasks to prepare this applet to run
     */
    public void init() {
        String labelFlag;
        String borderParam;
        
        chemfont = new java.awt.Font("Courier", 0, 12);

        URLConnection.setDefaultAllowUserInteraction(true);
        if ((getParameter("image") != null)) {
            isbackground = true;
            back = new Background(this);
        }
        backgroundColor = getColor("background");
        if (backgroundColor == null) {
            backgroundColor = getBackground();
        }

        textColor = getColor("textcolor");
        if (textColor == null) {
            textColor = Color.black;
        }

        try {
            screen = createImage(getSize().width, getSize().height);
            offscreen = screen.getGraphics();
        } catch (Exception e) {
            // ignore it (an empty window will be displayed)
        }

        borderParam = getParameter("border");
        
        if ((borderParam != null) && !(borderParam.equalsIgnoreCase("false")
                || borderParam.equalsIgnoreCase("no")
                || borderParam.equals("0"))) {
            isborder = true;
            border = new Border(offscreen, getSize().width, getSize().height);
        }

        String mdname = getParameter("model");
        if (mdname == null) {
            mdname = "model.crt";
        }

        view = new Rotate3DView();
        try {
            String tabName = getParameter("table");
            InputStream tabStream;

            view.setModel(WebFiles.getData(new URL(getDocumentBase(), mdname),
                    System.out));
            tabStream = (tabName == null)
                    ? getClass().getClassLoader().getResourceAsStream(
                            "element_data.txt")
                    : new URL(getDocumentBase(), tabName).openStream();
            if (tabStream == null) {
                message = "Atomic parameter table not found";
                System.out.println(message);
            } else {
                view.setTable(WebFiles.getTable(tabStream, System.out));
            }
        } catch (Exception e) {
            message = e.getMessage();
            System.out.println(message);
        }
        view.setWidth(getSize().width);
        view.setHeight(getSize().height);
        try {
            float initScale = Float.valueOf(getParameter("scale")).floatValue();
            
            view.setInitialScale(initScale);
        } catch (Exception e) {
            // ignore the problem and use the default scale factor
        }
        labelFlag = getParameter("showLabels");
        if ("true".equalsIgnoreCase(labelFlag)
                || "yes".equalsIgnoreCase(labelFlag)
                || "on".equalsIgnoreCase(labelFlag)
                || "1".equals(labelFlag)) {
            view.toggleLabels();
        }
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    /**
     * Draws the display of the model using double buffering
     * 
     * @param  g the <code>Graphics</code> with which to paint the applet
     */
    public void paint(Graphics g) {
        g.setFont(chemfont);
        offscreen.setFont(chemfont);

        if ((view != null) && (offscreen != null)) {
            int winW = getSize().width;
            int winH = getSize().height;

            offscreen.setColor(backgroundColor);
            offscreen.fillRect(0, 0, winW, winH);
            if (isbackground) {
                back.drawBackground(offscreen, this);
            }

            view.paint(offscreen, backgroundColor, textColor);

            if (isborder) {
                border.draw();
            }

            if (help) {
                paintHelp(offscreen);
            }

            if (showAngles) {
                paintAngles(offscreen, winH);
            }
        } else if (message != null) {
            g.drawString("Error in model:", 3, 20);
            g.drawString(message, 10, 40);
        }

        // double buffer draw to graphics context
        g.drawImage(screen, 0, 0, this);
    }

    /**
     * Prints the help text in the specified color with the use of the provided
     * graphics context
     * 
     * @param g the graphics context with which to draw the help text
     */
    public void paintHelp(Graphics g) {
        g.setColor(textColor);
        g.drawString("?   This help message", 20, 30);
        g.drawString("a   Molecule Rotation Angles", 20, 45);
        g.drawString("l   Atom labels", 20, 60);
        g.drawString("h   Hydrogen labels", 20, 75);
        g.drawString("r   Reset rotation and size", 20, 90);
        g.drawString("Left Mouse Button - rotate", 20, 115);
        g.drawString("Right Mouse Button - resize", 20, 130);
    }

    /**
     * updates the applet display
     * 
     * @param g the <code>Graphics</code> on which to draw
     */
    public void update(Graphics g) {
        if (offscreen == null) {
            g.clearRect(0, 0, getSize().width, getSize().height);
        }
        paint(g);
    }

    /**
     * Returns a <code>Color</code> corresponding to the integer color value
     * in the named parameter.
     * 
     * @param name the name of the parameter from which to obtain the color
     *        value
     * @return the <code>Color</code> corresponding to parameter
     *         <code>name</code>, or <code>null</code> if that parameter
     *         doesn't exist or cannot be interpreted as a color value
     */
    protected Color getColor(String name) {
        String value = getParameter(name);
        int intvalue;
        try {
            intvalue = Integer.parseInt(value, 16);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Color(intvalue);
    }

    /**
     * Provides support for the keyboard controls
     * 
     * @param e a <code>KeyEvent</code> corresponding to some keyboard gesture
     */
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_R:
                    view.reset();
                    repaint();
                    break;
                case KeyEvent.VK_L:
                    view.toggleLabels();
                    repaint();
                    break;
                case KeyEvent.VK_A:
                    showAngles = !showAngles;
                    repaint();
                    break;
                case KeyEvent.VK_H:
                    view.toggleHydro();
                    repaint();
                    break;
                case KeyEvent.VK_SLASH:
                    if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                        help = !help;
                        repaint();
                    }
            }
        }
    }

    /**
     * Processes mouse presses over the applet area and the mouse pointer
     * entering or leaving the applet area
     * 
     * @param e a <code>MouseEvent</code> corresponding to the mouse gesture
     */
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                prevx = e.getX();
                prevy = e.getY();
                break;
            case MouseEvent.MOUSE_ENTERED:
                showStatus("miniJaMM v. 1.2 by J. N. Huffman and J. C. "
                        + "Bollinger, Indiana University");
                break;
            case MouseEvent.MOUSE_EXITED:
                showStatus("");
        }
    }

    /**
     * Handles mouse drags over the applet area
     * 
     * @param e a <code>MouseEvent</code> corresponding to the mouse gesture
     *        that ocurred
     */
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        if (e.getID() != MouseEvent.MOUSE_DRAGGED) {
            return;
        }
        int x = e.getX();
        int y = e.getY();
        long mods = e.getModifiers();
        if ((mods & (InputEvent.META_MASK | InputEvent.ALT_MASK)) != 0) {
            view.setScale(view.getScale() + ((prevy - y) / 2));
            repaint();
        } else if ((mods & InputEvent.CTRL_MASK) != 0) {
            float ztheta = (((y - prevy) * 360.0f) / getSize().width)
                    + (((x - prevx) * 360.0f) / getSize().height);
            view.zRot(ztheta);
            repaint();
        } else {
            float xtheta = ((prevy - y) * 360.0f) / getSize().width;
            float ytheta = ((x - prevx) * 360.0f) / getSize().height;
            view.xRot(xtheta);
            view.yRot(ytheta);
            repaint();
        }
        prevx = x;
        prevy = y;
    }

    /**
     * Calculates the current rotation angles and displays them in the specified
     * color at the specified height with the use of the provided graphics
     * context
     * 
     * @param g the <code>Graphics</code> to use to display the angles
     * @param y the height at which to display the angles
     */
    private void paintAngles(Graphics g, int y) {
        double[] angles = view.getAngles();

        g.setColor(textColor);
        g.drawString("X: " + (Math.round(angles[0] * 100.) / 100.), 10, y - 10);
        g.drawString("Y: " + (Math.round(angles[1] * 100.) / 100.), 110, y - 10);
        g.drawString("Z: " + (Math.round(angles[2] * 100.) / 100.), 210, y - 10);
    }
}
