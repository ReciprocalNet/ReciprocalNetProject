/*
 * Reciprocal Net Project
 *
 * @(#)JaMMed.java
 *
 * By John C. Bollinger, Indiana University Molecular Structure Center
 *
 * Copyright (c) 2000, 2003 The Trustees of Indiana University
 *
 * 16-Aug-2002: jobollin Fixed bug #385
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DModel
 * 19-Dec-2002: jobollin made processKeyEvent() consume events that it
 *              intercepts as part of task #445
 * 10-Jan-2003: jobollin reformatted the source according code conventions and
 *              seperated the file comment from the class-level Javadoc comment
 * 19-Feb-2003: jobollin changed references in the class-level javadocs to
 *              table.dat to refer instead to element_data.txt as part of task
 *              #682
 * 03-Mar-2003: jobollin reformatted the source and updated and revised the
 *              javadoc comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.applet.jamm.TableContainer;

/**
 * <h1>Java Molecular Modeler / Educational (JaMMed) version 1.3.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * JaMMed is an applet for displaying and manipulating a three-dimensional
 * molecular model inside a browser, with a view toward use in the context of
 * a web-based lesson.  JaMMed provides for rotating and scaling the model,
 * and can present wireframe, ball & stick, and pseudo-space-filled views.
 * Depth cueing is used in all modes to improve the viewer's 3-D perception,
 * and size cueing is applied as well in ball & stick and space-filled modes.
 * </p><p>
 * This applet is written with the use of the Swing implementation of JDK 1.3.
 * Currently it requires Sun's Java Plugin to run, as the VMs of MSIE 5 (and
 * lower) and of NS Communicator 4.x (and lower) do not support the latest
 * Swing classes.  Communicator 6 is reputed to support Swing, but JaMMed has
 * not been tested against Communicator 6.
 * </p>
 * <h2>Architecture</h2>
 * <p>
 * The JaMMed class is a subclass of JApplet.  It constructs the components and
 * builds and manages the UI.  Swing components are used throughout.
 * </p><p>
 * The actual molecule rendering is performed by a MoleculeRotatePanel
 * instance; MoleculeRotatePanel is a custom descendant of JPanel.  (In
 * between JPanel and MoleculeRotatePanel is Rotate3DPanel, another custom
 * component that is a little more general.)  MoleculeRotatePanels (and
 * Rotate3DPanels) handle various types of mouse input directly, but they
 * depend on an outside class (JaMMed in this case) to manipulate their
 * rendering controls.
 * </p>
 * <h2>Parameters</h2>
 * <p>
 * JaMMed must read a model file and a table file to get the information with
 * which to initialize the MoleculeRotatePanel.  The "model" and "table"
 * parameters, if present, specify the files to use for this purpose.  If they
 * are missing then the defaults are "model.obj" and "element_data.txt".
 * JaMMed uses these along with its getDocumentBase() method to construct URLs
 * from which to read the data; this operation is potentially
 * viewer-dependant.
 * </p><p>
 * JaMMed also recognizes a "color" parameter.  If specified, this parameter is
 * interpreted as an integer to be used as an argument to a Color constructor;
 * the color constructed this way is set as the initial background color of
 * the MoleculeRotatePanel.  If this parameter is not specified then the
 * MoleculeRotatePanel initially retains its default background color.
 * </p><p>
 * Other parameters allow the initial display options to be customized.  The
 * "drawmode" parameter can be set to "space" or "ball" for initial space-
 * filled or ball & stick rendering styles, respectively; otherwise the
 * default is to start with line mode.  The initial molecule orientation can
 * be controlled with the "xrot", "yrot", and "zrot" parameters, each of which
 * takes a floating-point value.  Appropriate values for xrot, yrot, and zrot
 * can be obtained from any of the JaMM versions that displays angles.
 * </p>
 * <h2>Controls</h2>
 * <ul>
 *   <li>left drag to rotate the molecule</li>
 *   <li>right drag or alt- left drag to scale the molecule</li>
 *   <li>shift drag to translate the molecule</li>
 *   <li>'r' to reset to the initial view</li>
 *   <li>'b' for ball & stick mode</li>
 *   <li>'l' for line mode</li>
 *   <li>'s' for space-filling mode</li>
 * </ul>
 * <h2>Credits</h2>
 * <p>
 * Some supporting and inner classes are derived from materials provided by
 * other authors, including copyrighted code licensed from Sun Microsystems
 * and material posted on Sun's JDC site.  The JaMMed 1.0 applet was derived
 * from JaMMer 1.0 by John C. Bollinger and John N. Huffman.
 * </p>
 *
 * @author John C. Bollinger
 * @version 1.3
 */
public class JaMMed extends BasicJaMM {

    /** The initial state of the hydrogen display flag */
    protected boolean initHydro;

    /** The initial drawing mode */
    protected int initMode;

    /**
     * {@inheritDoc}
     *
     * @see BasicJaMM#getAppletInfo()
     */
    public String getAppletInfo() {
        return new String(
            "JaMMed version 1.3\nby John C. Bollinger,\n"
            + "copyright (c) 2001, 2002, 2003 "
            + "the Trustees of Indiana University");
    }

    /**
     * {@inheritDoc}
     *
     * @see BasicJaMM#initInstance(JPanel)
     */
    public void initInstance(JPanel content) {
        super.initInstance(content);

        /* activate keyboard controls */
        enableEvents(AWTEvent.KEY_EVENT_MASK);

        /* set the initial draw mode */
        String parm = getParameter("drawmode");
        if (parm == null) {
            initMode = DemoRotatePanel.LINE_MODE;
        } else {
            parm = parm.toLowerCase();
            if (parm.equals("ball")) {
                initMode = DemoRotatePanel.BALL_STICK_MODE;
            } else if (parm.equals("space")) {
                initMode = DemoRotatePanel.SPACE_MODE;
            } else {
                initMode = DemoRotatePanel.LINE_MODE;
            }
        }

        /* set the initial hydrogen mode */
        parm = getParameter("hydrogen");
        if ((parm != null)
                && (parm.equalsIgnoreCase("true")
                || parm.equalsIgnoreCase("on") || parm.equalsIgnoreCase("yes"))) {
            initHydro = true;
        } else {
            initHydro = false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see BasicJaMM#createMainPanel(Rotate3DModel, TableContainer)
     */
    protected Rotate3DPanel createMainPanel(Rotate3DModel mod, TableContainer tc) {
        EdRotatePanel erp = new EdRotatePanel(mod, tc.colorTab, tc.radii);
        int appletHeight = getHeight();
        String parm;

        parm = getParameter("xrot");
        if (parm != null) {
            try {
                erp.xrot(-Float.parseFloat(parm));
            } catch (NumberFormatException e) {
                // nothing to do
            }
             // ignore bad values
        }
        parm = getParameter("yrot");
        if (parm != null) {
            try {
                erp.yrot(-Float.parseFloat(parm));
            } catch (NumberFormatException e) {
                // nothing to do
            }
             // ignore bad values
        }
        parm = getParameter("zrot");
        if (parm != null) {
            try {
                erp.zrot(-Float.parseFloat(parm));
            } catch (NumberFormatException e) {
                // nothing to do
            }
             // ignore bad values
        }
        parm = getParameter("border");
        if ((parm == null) || parm.toLowerCase().equals("true")) {
            erp.setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        erp.setPreferredSize(new Dimension(appletHeight, appletHeight));
        erp.setDrawMode(initMode);
        erp.setHydro(initHydro);
        erp.homeOrientation();

        return erp;
    }

    /**
     * Processes keyboard events that occur on this applet, or hands off
     * to the superclass any that it chooses not to process itself
     *
     * @param  e the <code>KeyEvent</code> to process
     */
    protected void processKeyEvent(KeyEvent e) {
        EdRotatePanel erp = (EdRotatePanel) pan;
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_B:
                erp.setDrawMode(DemoRotatePanel.BALL_STICK_MODE);
                break;
            case KeyEvent.VK_H:
                erp.toggleHydro();
                break;
            case KeyEvent.VK_L:
                erp.setDrawMode(DemoRotatePanel.LINE_MODE);
                break;
            case KeyEvent.VK_R:
                erp.reset();
                erp.setDrawMode(initMode);
                erp.setHydro(initHydro);
                break;
            case KeyEvent.VK_S:
                erp.setDrawMode(DemoRotatePanel.SPACE_MODE);
                break;
            default:
                super.processKeyEvent(e);
                return;
            }
            e.consume();
        } else {
            super.processKeyEvent(e);
        }
    }
}
