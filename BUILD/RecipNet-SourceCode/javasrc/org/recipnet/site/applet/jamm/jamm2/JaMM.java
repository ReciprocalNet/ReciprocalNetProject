/*
 * Reciprocal Net Project
 *
 * JaMM.java
 *
 * By John C. Bollinger, Indiana University Molecular Structure Center
 *
 * Copyright (c) 1998, 2002 - 2006 The Trustees of Indiana University
 *
 * 28-Oct-2002: jobollin modified doOrtep, doImage, and doLinedraw to make
 *              them compatible with the new servlet-based support
 * 06-Nov-2002: jobollin updated the source comments to comply with JavaDoc
 *              conventions and updated the JaMM version number to 2.3
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DPanel
 * 22-Nov-2002: jobollin fixed bug #616 by changing the URLEncoder.encode
 *              method invoked by each of the server-side support requests
 * 19-Dec-2002: jobollin made processKeyEvent consume KeyEvents that it
 *              intercepts as part of task #445
 * 09-Jan-2003: jobollin removed unused imports
 * 19-Feb-2003: jobollin changed class javadocs to refer to element_data.txt
 *              instead of table.dat as part of task #682
 * 25-Feb-2003: jobollin reformatted the source and revised the javadoc
 *              comments as part of task #749
 * 13-Mar-2003: jobollin removed the import for InvokerThread as part of
 *              task #743
 * 03-Jul-2003: jobollin modified requestOrtep to scale the image according
 *              to the applet's display size
 * 11-Jul-2003: jrhanna added sampleHistoryId parameter
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 * 06-Jan-2005: jobollin added implementation comments regarding use of
 *              URLEncoder.encode(String), which was deprecated in favor of
 *              a newly added method in Java 1.4
 * 25-May-2006: jobollin updated docs, formatted the source, implemented
 *              generics
 * 23-Jun-2006: jobollin replaced deprecated URLEncoder
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.recipnet.common.Utf8UrlEncoder;
import org.recipnet.site.applet.jamm.MessageEvent;
import org.recipnet.site.applet.jamm.MessageListener;
import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.applet.jamm.TableContainer;

/**
 * <h2>Java Molecular Modeller (JaMM) version 2.3.
 * <h3>Overview</h3>
 * <p>
 * JaMM is an applet for
 * displaying and manipulating a three-dimensional molecular model inside a
 * browser.  JaMM provides for rotating, scaling, and translating the model,
 * and can present wireframe, ball & stick, and pseudo-space-filled views.
 * Depth cueing is used in all modes to improve the viewer's 3-D perception,
 * and left/right and red/green stereo modes produce good stereo results.
 * The user can toggle the displays of hydrogen atoms, labels, and orientation
 * angles.  Element colors and radii are configurable.  JaMM will also provide
 * interatomic distances and angles.
 * </p><p>This applet is written with the use of the Swing GUI toolkit of
 * Java 2, and thus requires a Java 2 JRE to run.  Mozilla and Netscape 6+
 * provide suitable JREs, but older versions of Netscape and all versions
 * of Internet Explorer require Sun's Java Plugin to run.
 * </p>
 * <h3>Architecture</h3>
 * <p>The {@code JaMM} class constructs the components and builds
 * and manages the UI.  Swing components are used throughout. The actual
 * molecule rendering is performed by a {@code MoleculeRotatePanel}
 * instance; {@code MoleculeRotatePanel} is a custom {@code JPanel}.
 * (In between {@code JPanel} and {@code MoleculeRotatePanel} is
 * {@code Rotate3DPanel}, another custom component that is
 * a little more general.)  {@code MoleculeRotatePanel}s (and
 * {@code Rotate3DPanel}s) handle various types of mouse input directly,
 * but they depend on an outside class ({@code JaMM} in this case) to
 * manipulate their rendering controls.  {@code MoleculeRotatePanel}s
 * also throw messages to registered {@code MessageListeners} in
 * response to certain user inputs.
 * </p><p>
 * {@code JaMM} uses a {@code TextOutputPane} to display information
 * for the user. {@code TextOutputPane} is a custom 
 * {@code JScrollPane} which always manages a {@code JTextArea} and
 * provides a {@code PrintWriter} for appending text.  Initially the
 * {@code JTextArea} is sized to fit the {@code TextOutputPane}'s
 * viewport, but it grows if enough text is added.  The
 * {@code TextOutputPane} also scrolls its view automatically when text is
 * added, so that new text added to it is always immediately shown without user
 * intervention. Any message {@code JaMM} receives from its
 * {@code MoleculeRotatePanel} is
 * immediately printed on its {@code TextOutputPane}.
 * </p>
 * <h3>Parameters</h3>
 * <p>{@code JaMM} must read a model file and an element parameter file to
 * get the information with which to initialize the
 * {@code MoleculeRotatePanel}. The "model" and "table" parameters, if
 * present, specify the files to use for his purpose.  If they are missing then
 * the defaults are "model.obj" (relative to the document base) and
 * "element_data.txt" (as a resource available via the class loader). 
 * {@code JaMM} also recognizes a
 * "color" parameter.  If specified, this parameter is interpreted as an
 * integer to be used as an argument to a {@code Color} constructor; the
 * color constructed this way is set as the initial background color of the
 * {@code MoleculeRotatePanel}.  If this parameter is not specified then
 * the {@code MoleculeRotatePanel} initially retains its default
 * background color.
 * </p>
 * <h3>Controls</h3>
 * <p>The GUI is pretty much self-explanatory.  All of the controls are
 * hooks into the {@code MoleculeRotatePanel}'s options and programmatic
 * controls. {@code JaMM} also responds to the + and - keys by brightening or
 * darkening, respectively, the {@code MoleculeRotatePanel}'s background.
 * It displays a help file in response to the F1 or ? key.  The
 * {@code MoleculeRotatePanel} itself bears
 * a little explanation, however:
 * <ul>
 *   <li>left drag to rotate the molecule</li>
 *   <li>right drag or alt- left drag to scale the molecule</li>
 *   <li>shift- drag to translate the molecule</li>
 *   <li>click on atoms to get their current coordinates or to select them for
 *       a distance or angle calculation (depending on the current mode)</li>
 * </ul>
 * (Added March 2002:)
 * <ul>
 *   <li>the 'h' key toggles hydrogen atoms</li>
 *   <li>the 'r' key resets the model</li>
 *   <li>the 'l' key selects line drawing mode
 *   <li>the 'b' key selects ball&amp;stick drawing mode
 *   <li>the 's' key selects space-filled drawing mode
 * </ul>
 * </p>
 * <h3>Credits</h3>
 * <p>Some supporting and inner classes are derived from
 * materials provided by other authors, including copyrighted code licensed
 * from Sun Microsystems and material posted on Sun's JDC web site.  The JaMM
 * 2.3 applet is inspired by and derives some code from JaMM 1.0 and (never
 * released) JaMM 2.0 by John N. Huffman.
 * </p>
 * 
 * @author John C. Bollinger (jobollin@indiana.edu)
 * 
 * @version 2.3
 */
public class JaMM
        extends BasicJaMM
        implements MessageListener, ChangeListener {

    /**
     * a {@code ButtonGroup} from which the applet can obtain the current
     * setting of the requested image size
     */
    protected ButtonGroup sizeGroup;

    /** the help dialog box */
    protected JDialog helpBox = null;

    /** the rendering options dialog box */
    protected JDialog renderOptions = null;

    /**
     * a {@code Map} that assists with communication between the rendering
     * options dialog and the applet's repainting methods
     */
    private Map changerMap;

    /** an object to handle dispatching callbacks to the appropriate method */
    protected MultiEventDispatcher dispatcher;

    /**
     * a {@code PrintWriter} with which the applet may deliver user
     * messages
     */
    protected PrintWriter out;

    /**
     * the name of the server-side resource that provides ray-tracing services
     */
    protected String imageScript;

    /**
     * the name of the server-side resource that provides line-drawing services
     */
    protected String lineScript;

    /**
     * the name of the server-side resource that provides ORTEP-drawing
     * services
     */
    protected String ortepScript;

    /**
     * indicates whether a rendering job should use the applet's current
     * background or a white background
     */
    protected boolean backgroundColorFlag;

    /** the ball mode radio button */
    private AbstractButton ballButton;

    /** the hydrogen display checkbox */
    private AbstractButton hydrogenButton;

    /** the image rendering button */
    private AbstractButton imageBut;

    /** the label display checkbox */
    private AbstractButton labelCheckBox;

    /** the line mode radio button */
    private AbstractButton lineButton;

    /** the line drawing button */
    private AbstractButton lineDrawBut;

    /** the ORTEP button */
    private AbstractButton ortepBut;

    /** the space-filled mode radio button */
    private AbstractButton spaceButton;

    /**
     * provides brief information about JaMM
     *
     * @return a {@code String} containing brief information about this
     *         applet
     */
    public String getAppletInfo() {
        return "JaMM version 2.3\n" + "by John C. Bollinger\n"
        + "copyright (c) 1999 - 2006 The Trustees of Indiana University";
    }

    /**
     * constructs and displays the help dialog
     */
    public void buildHelpBox() {
        String helpName = getParameter("help");
        if (helpName == null) {
            helpName = new String("JaMM.help");
        }
        buildHelpBox(helpName);
        helpBox.setVisible(true);
    }

    /**
     * Constructs the help dialog
     *
     * @param  helpFile a {@code String} representation of the URL of the
     *         help file, relative to this applet's document base
     */
    protected void buildHelpBox(String helpFile) {
        helpBox = new JDialog();
        JPanel content = new JPanel(new BorderLayout());
        helpBox.setModal(false);
        helpBox.setTitle("JaMM Help");
        JButton b = new JButton("Close");
        dispatcher.registerAction(b, this, "closeHelp");
        Box centeringBox = Box.createHorizontalBox();
        centeringBox.add(Box.createHorizontalGlue());
        centeringBox.add(b);
        centeringBox.add(Box.createHorizontalGlue());
        content.add(centeringBox, BorderLayout.SOUTH);
        JTextArea t = new JTextArea();
        t.setLineWrap(true);
        t.setWrapStyleWord(true);
        try {
            StreamTokenizer st =
                new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(
                            new URL(getDocumentBase(), helpFile).openStream())));
            st.resetSyntax();
            st.wordChars(0x0001, 0xffff);
            st.lowerCaseMode(false);
            int tType;
            try {
                while ((tType = st.nextToken()) != StreamTokenizer.TT_EOF) {
                    if (tType == StreamTokenizer.TT_WORD) {
                        t.append(st.sval);
                    } else if (tType == StreamTokenizer.TT_EOL) {
                        t.append("\n");
                    }
                }
            } catch (IOException ioe) {
                // do nothing
            }
        } catch (MalformedURLException ue) {
            t.append("Oops!  " + ue.getMessage()
                + "\nProbable cause: server misconfiguration\n");
        } catch (IOException ie) {
            t.append("Oops!  " + ie.getMessage()
                + "\nProbable cause: server down or inaccessible\n");
        }
        JScrollPane s =
            new JScrollPane(t, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        s.setPreferredSize(new Dimension(400, 600));
        content.add(s, BorderLayout.CENTER);
        helpBox.setContentPane(content);
        helpBox.pack();
    }

    /**
     * closes the help box if it is open
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void closeHelp(ActionEvent e) {
        helpBox.setVisible(false);
    }

    /**
     * Builds the rendering options dialog
     */
    protected void buildRenderOptions() {
        renderOptions = new JDialog();
        renderOptions.setTitle("Rendering Options");
        renderOptions.setModal(false);
        renderOptions.getContentPane().setLayout(new FlowLayout());

        if (changerMap == null) {
            changerMap = new WeakHashMap();
        }
        JPanel p;
        SliderField sf;
        BoundedRangeModel brm;

        renderOptions.getContentPane().setLayout(new BorderLayout());
        Box bigBox = new Box(BoxLayout.X_AXIS);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new CompoundBorder(new TitledBorder("background color"),
                new EmptyBorder(0, 5, 0, 5)));
        Color background = pan.getBackground();
        sf = new SliderField(255, 0, background.getRed(), new JLabel("red"));
        brm = sf.getModel();
        changerMap.put(brm, new Integer(16));
        brm.addChangeListener(this);
        p.add(sf);
        sf = new SliderField(255, 0, background.getGreen(), new JLabel("green"));
        brm = sf.getModel();
        changerMap.put(brm, new Integer(8));
        brm.addChangeListener(this);
        p.add(sf);
        sf = new SliderField(255, 0, background.getBlue(), new JLabel("blue"));
        brm = sf.getModel();
        changerMap.put(brm, new Integer(0));
        brm.addChangeListener(this);
        p.add(sf);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton rb =
            new JRadioButton("render on selected background", true);
        dispatcher.registerAction(rb, this, "doToggleBackgroundFlag");
        bg.add(rb);
        p.add(rb);
        rb = new JRadioButton("render on white background", false);
        dispatcher.registerAction(rb, this, "doToggleBackgroundFlag");
        p.add(rb);
        bg.add(rb);
        bigBox.add(p);

        JPanel p2 = new JPanel(new BorderLayout());

        p = new JPanel();
        p.setBorder(new TitledBorder("ball size"));
        DecimalField ballSize =
                new DecimalField(1.0d, 5, new DecimalFormat("#0.0#;\000#0.0"));
        dispatcher.registerAction(ballSize, this, "updateBallSize");
        dispatcher.registerFocusLoss(ballSize, this, "updateBallSize");
        p.add(ballSize);
        p2.add(p, BorderLayout.NORTH);

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder("image size"));
        sizeGroup = new ButtonGroup();
        rb = new JRadioButton("100 x 100", false);
        rb.setActionCommand("100");
        sizeGroup.add(rb);
        p.add(rb);
        rb = new JRadioButton("300 x 300", true);
        rb.setActionCommand("300");
        sizeGroup.add(rb);
        p.add(rb);
        rb = new JRadioButton("500 x 500", false);
        rb.setActionCommand("500");
        sizeGroup.add(rb);
        p.add(rb);
        rb = new JRadioButton("700 x 700", false);
        rb.setActionCommand("700");
        sizeGroup.add(rb);
        p.add(rb);
        rb = new JRadioButton("1000 x 1000", false);
        rb.setActionCommand("1000");
        sizeGroup.add(rb);
        p.add(rb);
        p2.add(p, BorderLayout.CENTER);
        bigBox.add(p2);
        renderOptions.getContentPane().add(bigBox, BorderLayout.CENTER);

        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalGlue());
        JButton B = new JButton("Close");
        dispatcher.registerAction(B, this, "doCloseRenderingOptions");
        box.add(B);
        box.add(Box.createHorizontalGlue());
        renderOptions.getContentPane().add(box, BorderLayout.SOUTH);
        renderOptions.pack();
    }

    /**
     * Constructs the UI for the applet, including registering event handlers
     * to manipulate various components appropriately
     *
     * @param content the topmost container of the UI (not necessarilly of the
     *        applet)
     * @param appletWidth the overall width on which to base the various
     *        components' preferred widths
     * @param appletHeight the overall height on which to base the various
     *        components' preferred heights
     */
    protected void buildUI(JPanel content, int appletWidth, int appletHeight) {
        ButtonGroup BG;
        JButton B;
        JCheckBox CB;
        JPanel controls;
        JPanel sub;
        GridBagLayout gbl;
        GridBagConstraints constraints;
        JRadioButton RB;
        TextOutputPane OP;

        /* outer border */
        content.setBorder(new BevelBorder(BevelBorder.RAISED));

        /* set up all the controls */
        gbl = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0d;
        constraints.weighty = 1.0d;
        controls = new JPanel(gbl);

        /* Drawing mode controls */
        sub = new JPanel(new GridLayout(3, 1));
        sub.setBorder(new TitledBorder("draw mode"));
        BG = new ButtonGroup();
        lineButton = RB = new JRadioButton("Line Drawing", true);
        dispatcher.registerAction(RB, this, "doLines");
        BG.add(RB);
        sub.add(RB);
        ballButton = RB = new JRadioButton("Ball and Stick", false);
        dispatcher.registerAction(RB, this, "doBalls");
        BG.add(RB);
        sub.add(RB);
        spaceButton = RB = new JRadioButton("Space Filled", false);
        dispatcher.registerAction(RB, this, "doSpace");
        BG.add(RB);
        sub.add(RB);
        gbl.setConstraints(sub, constraints);
        controls.add(sub);
        
        /* stereo mode controls */
        sub = new JPanel(new GridLayout(3, 1));
        sub.setBorder(new TitledBorder("stereo mode"));
        BG = new ButtonGroup();
        RB = new JRadioButton("Mono", true);
        dispatcher.registerAction(RB, this, "doMono");
        BG.add(RB);
        sub.add(RB);
        RB = new JRadioButton("Left/Right Stereo", false);
        dispatcher.registerAction(RB, this, "doLRStereo");
        BG.add(RB);
        sub.add(RB);
        RB = new JRadioButton("Red/Green Stereo", false);
        dispatcher.registerAction(RB, this, "doRGStereo");
        BG.add(RB);
        sub.add(RB);
        gbl.setConstraints(sub, constraints);
        controls.add(sub);
        
        /* input mode controls */
        sub = new JPanel(new GridLayout(3, 1));
        sub.setBorder(new TitledBorder("output mode"));
        BG = new ButtonGroup();
        RB = new JRadioButton("Atom", true);
        dispatcher.registerAction(RB, this, "doAtom");
        BG.add(RB);
        sub.add(RB);
        RB = new JRadioButton("Distance", false);
        dispatcher.registerAction(RB, this, "doDistance");
        BG.add(RB);
        sub.add(RB);
        RB = new JRadioButton("Angle", false);
        dispatcher.registerAction(RB, this, "doAngle");
        BG.add(RB);
        sub.add(RB);
        gbl.setConstraints(sub, constraints);
        controls.add(sub);
        
        /* option boxes */
        sub = new JPanel(new GridLayout(3, 1));
        sub.setBorder(new TitledBorder("options"));
        CB = new JCheckBox("Rotation Angles", true);
        dispatcher.registerAction(CB, this, "doAngles");
        sub.add(CB);
        CB = new JCheckBox("Labels", false);
        dispatcher.registerAction(CB, this, "doLabels");
        sub.add(CB);
        labelCheckBox = CB;
        hydrogenButton = CB = new JCheckBox("Hydrogen Atoms", false);
        dispatcher.registerAction(CB, this, "doHydrogen");
        sub.add(CB);
        gbl.setConstraints(sub, constraints);
        controls.add(sub);
        
        /* action buttons */
        sub = new JPanel(new GridLayout(4, 1));
        sub.setBorder(new EmptyBorder(3, 3, 3, 3));
        Insets ins = new Insets(2, 2, 2, 2);
        lineDrawBut = B = new JButton("Line Drawing");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1), B.getBorder()));
        B.setMargin(ins);
        dispatcher.registerAction(B, this, "doLineDraw");
        if (lineScript == null) {
            B.setEnabled(false);
        }
        sub.add(B);
        B = new JButton("ORTEP Diagram");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1), B.getBorder()));
        B.setMargin(ins);
        dispatcher.registerAction(B, this, "doOrtep");
        if (ortepScript == null) {
            B.setEnabled(false);
        }
        ortepBut = B;
        sub.add(B);
        B = new JButton("Rendered Image");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1), B.getBorder()));
        B.setMargin(ins);
        dispatcher.registerAction(B, this, "doImage");
        if (imageScript == null) {
            B.setEnabled(false);
        }
        imageBut = B;
        sub.add(B);
        B = new JButton("Rendering Options");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1), B.getBorder()));
        B.setMargin(ins);
        dispatcher.registerAction(B, this, "doRenderingOptions");
        sub.add(B);
        constraints.gridheight = GridBagConstraints.REMAINDER;
        gbl.setConstraints(sub, constraints);
        controls.add(sub);
        controls.setPreferredSize(new Dimension((int) (appletWidth * .22),
                (int) (appletHeight * .78)));
        content.add(controls, BorderLayout.EAST);

        /* Prepare an output area for the applet's use */
        OP = new TextOutputPane("JaMM v2.3 by J. C. Bollinger\n"
                + "Copyright (C) 1999, 2002, Indiana University\n\n"
                + "? for help\n\n");
        OP.setPreferredSize(new Dimension(appletWidth,
                (int) (appletHeight * .22)));
        OP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1), OP.getBorder()));
        content.add(OP, BorderLayout.SOUTH);
        out = OP.out();
    }

    protected Rotate3DPanel createMainPanel(Rotate3DModel mod,
            TableContainer tc) {
        MoleculeRotatePanel mrp =
            new MoleculeRotatePanel(mod, tc.colorTab, tc.radii);

        mrp.setBorder(new BevelBorder(BevelBorder.LOWERED));
        mrp.setPreferredSize(new Dimension((int) (getWidth() * .78),
                (int) (getHeight() * .78)));
        mrp.addMessageListener(this);
        // mrp.setBallSizeBall(0.5d);
        if (mod == null) {
            ortepBut.setEnabled(false);
            imageBut.setEnabled(false);
        }
        return mrp;
    }

    /**
     * sets the display to angle selection mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doAngle(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setOutputMode(MoleculeRotatePanel.ANGLE_MODE);
    }

    /**
     * toggles the display panel's display of the current rotation angles
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doAngles(ActionEvent e) {
        ((MoleculeRotatePanel) pan).toggleAngles();
    }

    /**
     * sets the viewing panel to atom mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doAtom(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setOutputMode(MoleculeRotatePanel.ATOM_MODE);
    }

    /**
     * sets the viewing panel to ball & stick mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doBalls(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setDrawMode(
                MoleculeRotatePanel.BALL_STICK_MODE);
        if (labelCheckBox != null) {
            labelCheckBox.setEnabled(false);
        }
    }

    /**
     * closes the renderingOptions dialog if it is open
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doCloseRenderingOptions(ActionEvent e) {
        renderOptions.setVisible(false);
    }

    /**
     * sets the viewing panel to distance selection mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doDistance(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setOutputMode(
                MoleculeRotatePanel.DISTANCE_MODE);
    }

    /**
     * sets the viewing panel to display hydrogen atoms
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doHydrogen(ActionEvent e) {
        ((MoleculeRotatePanel) pan).toggleHydro();
    }

    /**
     * submits an asynchronous image generation request
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doImage(ActionEvent e) {
        try {
            Thread T = new InvokerThread(this, "requestImage", null);
            T.start();
        } catch (NoSuchMethodException nsme) {
            out.println("Internal error -- unknown method requested.");
        } catch (Exception ex) {
            ex.printStackTrace(out);
        }
    }

    /**
     * toggles the viewing panel's display of atom labels
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doLabels(ActionEvent e) {
        ((MoleculeRotatePanel) pan).toggleLabels();
    }

    /**
     * submits an asynchronous line drawing generation request
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doLineDraw(ActionEvent e) {
        try {
            Thread T = new InvokerThread(this, "requestLineDrawing", null);
            T.start();
        } catch (NoSuchMethodException nsme) {
            out.println("Internal error -- unknown method requested.");
        } catch (Exception ex) {
            ex.printStackTrace(out);
        }
    }

    /**
     * sets the viewing panel to line mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doLines(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setDrawMode(MoleculeRotatePanel.LINE_MODE);
        if (labelCheckBox != null) {
            labelCheckBox.setEnabled(true);
        }
    }

    /**
     * sets the viewing panel left/right stereo mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doLRStereo(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setStereoMode(
                Enhanced3DPanel.LR_STEREO_MODE);
    }

    /**
     * sets the viewing panel to monographic mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doMono(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setStereoMode(
                Enhanced3DPanel.MONO_MODE);
    }

    /**
     * submits an asynchronous ORTEP drawing request
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doOrtep(ActionEvent e) {
        try {
            Thread T = new InvokerThread(this, "requestOrtep", null);
            T.start();
        } catch (NoSuchMethodException nsme) {
            out.println("Internal error -- unknown method requested.");
        } catch (Exception ex) {
            ex.printStackTrace(out);
        }
    }

    /**
     * displays the rendering options panel if it is not already displayed
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doRenderingOptions(ActionEvent e) {
        renderOptions.setVisible(true);
    }

    /**
     * sets the viewing panel to red/green stereo mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doRGStereo(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setStereoMode(
                Enhanced3DPanel.RG_STEREO_MODE);
    }

    /**
     * sets the viewing panel to space-filled mode
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doSpace(ActionEvent e) {
        ((MoleculeRotatePanel) pan).setDrawMode(MoleculeRotatePanel.SPACE_MODE);
        if (labelCheckBox != null) {
            labelCheckBox.setEnabled(false);
        }
    }

    /**
     * toggles the background color flag
     *
     * @param  e the {@code ActionEvent} that triggered this action
     */
    public void doToggleBackgroundFlag(ActionEvent e) {
        backgroundColorFlag = !backgroundColorFlag;
    }

    /**
     * Obtains the current stereographic mode from the display panel and
     * returns a corresponding {@code String}
     *
     * @return "mono" for monographic mode, "lr" for left / right stereo mode,
     *         or "rg" for red / green stereo mode; an empty string for any
     *         unrecognized mode
     */
    protected String getStereoModeString() {
        if (pan != null) {
            switch (((Enhanced3DPanel) pan).getStereoMode()) {
            case Enhanced3DPanel.MONO_MODE:
                return "mono";
            case Enhanced3DPanel.LR_STEREO_MODE:
                return "lr";
            case Enhanced3DPanel.RG_STEREO_MODE:
                return "rg";
            }
        }
        return "";
    }

    /**
     * initializes the model, tables, and UI, then builds a
     * {@code MoleculeRotatePanel} from the results and attaches it to
     * the UI
     *
     * @param  content the {@code JPanel} that will be set as this
     *         {@code JApplet}'s content pane
     */
    public void initInstance(JPanel content) {
        dispatcher = new MultiEventDispatcher();
        lineScript = getParameter("line");
        ortepScript = getParameter("ortep");
        imageScript = getParameter("render");
        buildUI(content, getWidth(), getHeight());
        backgroundColorFlag = true;
        enableEvents(java.awt.AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * Receives a message from a broadcast source on which this {@code JaMM} is
     * registered
     * 
     * @param  e a {@code MessageEvent} describing the message received
     */
    public void messageSent(MessageEvent e) {
        out.println(e.getMessage());
    }

    /**
     * Processes keyboard events that occur on this applet, or hands off
     * to the superclass any that it chooses not to process itself
     *
     * @param  e the {@code KeyEvent} to process
     */
    protected void processKeyEvent(KeyEvent e) {
        if (e.getID() != KeyEvent.KEY_PRESSED) {
            super.processKeyEvent(e);
            return;
        }
        switch (e.getKeyCode()) {
        case KeyEvent.VK_B:
            ballButton.doClick();
            break;
        case KeyEvent.VK_H:
            hydrogenButton.doClick();
            break;
        case KeyEvent.VK_L:
            lineButton.doClick();
            break;
        case KeyEvent.VK_R:
            pan.reset();
            break;
        case KeyEvent.VK_S:
            spaceButton.doClick();
            break;
        case KeyEvent.VK_EQUALS:
            if (!e.isShiftDown()) {
                break;
            }
        /* fall through iff shift key was down (i.e. '+' character) */
        case KeyEvent.VK_ADD:
        case KeyEvent.VK_PLUS:
            out.println("Brightening the background...");
            updatePanelColor(pan.getBackground().brighter());
            break;
        case KeyEvent.VK_SUBTRACT:
        case KeyEvent.VK_MINUS:
            out.println("Darkening the background...");
            updatePanelColor(pan.getBackground().darker());
            break;
        case KeyEvent.VK_SLASH:
            if (!e.isShiftDown()) {
                break;
            }
        /* fall through iff shift key was down (i.e. '?' character) */
        case KeyEvent.VK_F1:
            if (helpBox == null) {
                out.println("One moment, please ...\n\n");
                try {
                    Thread T = new InvokerThread(this, "buildHelpBox", null);
                    T.start();
                } catch (NoSuchMethodException nsme) {
                    out.println("Internal error -- unknown method requested.");
                } catch (Exception ex) {
                    ex.printStackTrace(out);
                }
            } else {
                helpBox.setVisible(true);
            }
            break;
        default:
            super.processKeyEvent(e);
            return;
        }
        e.consume();
    }

    protected void reportError(String type, String message) {
        if (out == null) {
            super.reportError(type, message);
        } else {
            out.println(type + ": " + message);
        }
    }

    /**
     * Requests a ray-traced image to be generated and displayed by the server,
     * based on the current display state and rendering options
     */
    public void requestImage() {
        Color bc = backgroundColorFlag ? pan.getBackground() : Color.white;
        float[] colors = bc.getColorComponents(null);
        double[] ang = pan.calculateAngles();
        int panSize = Math.max(pan.getWidth(), pan.getHeight());
        String size = sizeGroup.getSelection().getActionCommand();
        String drawType = ((MoleculeRotatePanel) pan).getDrawModeString();

        // try {
        String data =
            "sampleId=" + sampleId
            + "&sampleHistoryId=" + sampleHistoryId 

            /*
             * NOTE: URLEncoder.encode(String) is deprecated in Java 1.4 and
             * above in favor of a new method, URLEncoder.encode(String,
             * String), introduced at that time. This class must be compatible
             * with Java 1.2, and so we use Utf8UrlEncoder instead of the
             * deprecated method.
             */

            + "&name=" + Utf8UrlEncoder.encode(modName)
            + "&function=render"
            + "&size=" + size
            + "&drawtype=" + drawType
            + "&x=" + ang[0]
            + "&y=" + ang[1]
            + "&z=" + ang[2]
            + "&r=" + colors[0]
            + "&g=" + colors[1]
            + "&b=" + colors[2]
            + "&distance=" + (550.0 * pan.getScale() / panSize)
            + "&quality=90"
            + "&shiftx=" + pan.getShiftX()
            + "&shifty=" + pan.getShiftY()
            + "&stereo=" + getStereoModeString()
            + "&bsize=" + ((MoleculeRotatePanel) pan).getRelativeBallSize()
            + "&hydro="
            + String.valueOf(((MoleculeRotatePanel) pan).getHydro());
        out.println("\nRay-traced image requested for " + modName
            + ", with angles " + ang[0] + "  " + ang[1] + "  " + ang[2]);
        out.println("Please wait (this may take some time).");
        out.println("Your image will appear in a separate browser window.\n");
        RunCGI(imageScript, data);
        /*
         * When the Java 1.4 version of the URL encoding is activated, it will
         * require this catch block:
         * } catch (UnsupportedEncodingException uee) {
         *    out.print("Your browser's Java implementation is broken ");
         *    out.println("(no UTF-8 support)");
         *    out.println("Cannot generate a rendering request.");
         * }
         */
    }

    /**
     * Requests a line drawing to be generated and displayed by the server,
     * based on the current display state
     */
    public void requestLineDrawing() {
        double[] ang = pan.calculateAngles();

        //try {
        String data =
            "sampleId=" + sampleId + "&sampleHistoryId=" + sampleHistoryId 

            /*
             * NOTE: URLEncoder.encode(String) is deprecated in Java 1.4 and
             * above in favor of a new method, URLEncoder.encode(String,
             * String), introduced at that time. This class must be compatible
             * with Java 1.2, and so we use Utf8UrlEncoder instead of the
             * deprecated method.
             */

            + "&name=" + Utf8UrlEncoder.encode(modName)
            + "&function=linedraw" + "&x=" + ang[0] + "&y=" + ang[1] + "&z="
            + ang[2] + "&shiftx=" + pan.getShiftX() + "&shifty="
            + pan.getShiftY() + "&scale=" + pan.getScale() + "&stereo="
            + getStereoModeString() + "&quality=90" + "&hydro="
            + String.valueOf(((MoleculeRotatePanel) pan).getHydro());
        out.println("\nLine drawing requested for " + modName
            + ", with angles " + ang[0] + "  " + ang[1] + "  " + ang[2]);
        out.println(
            "Please wait -- your drawing will appear in a separate browser window.\n");
        RunCGI(lineScript, data);
        /*
         * When the Java 1.4 version of the URL encoding is activated, it will
         * require this catch block:
         * } catch (UnsupportedEncodingException uee) {
         *    out.print("Your browser's Java implementation is broken ");
         *    out.println("(no UTF-8 support)");
         *    out.println("Cannot generate a rendering request.");
         * }
         */
    }

    /**
     * Requests an ORTEP image to be generated and displayed by the server,
     * based on the current display state
     */
    public void requestOrtep() {
        double[] ang = pan.calculateAngles();

        //try {
        String data =
            "sampleId=" + sampleId + "&sampleHistoryId=" + sampleHistoryId  

            /*
             * NOTE: URLEncoder.encode(String) is deprecated in Java 1.4 and
             * above in favor of a new method, URLEncoder.encode(String,
             * String), introduced at that time. This class must be compatible
             * with Java 1.2, and so we use Utf8UrlEncoder instead of the
             * deprecated method.
             */

            + "&name=" + Utf8UrlEncoder.encode(modName)
            + "&function=ortep" + "&x=" + ang[0] + "&y=" + ang[1] + "&z="
            + ang[2] + "&shiftx=" + pan.getShiftX() + "&shifty="
            + pan.getShiftY() + "&scale=" + pan.getScale() + "&stereo="
            + getStereoModeString() + "&quality=90" + "&hydro="
            + String.valueOf(((MoleculeRotatePanel) pan).getHydro());
        out.println("\nORTEP image requested for " + modName + ", with angles "
            + ang[0] + "  " + ang[1] + "  " + ang[2]);
        out.println(
            "Please wait -- your image will appear in a separate browser window.\n");
        RunCGI(ortepScript, data);
        /*
         * When the Java 1.4 version of the URL encoding is activated, it will
         * require this catch block:
         * } catch (UnsupportedEncodingException uee) {
         *    out.print("Your browser's Java implementation is broken ");
         *    out.println("(no UTF-8 support)");
         *    out.println("Cannot generate a rendering request.");
         * }
         */
    }

    /**
     * a utility method that attempts to load the specified resource in a new
     * window
     *
     * @param u the {@code URL} specifying the resource to display
     */
    protected void showDocument(URL u) {
        getAppletContext().showDocument(u, "_blank");
    }

    /**
     * Performs the actions required each time this applet is started 
     */
    public void start() {
        super.start();
        if ((renderOptions == null) && (pan != null)) {
            buildRenderOptions();
        }
    }

    /**
     * notifies the applet that a change has occurred in a component that it is
     * monitoring.  In this version, the applet monitors only some
     * SliderFields for controlling the background color.
     *
     * @param  e the {@code ChangeEvent} that ocurred
     */
    public void stateChanged(ChangeEvent e) {
        BoundedRangeModel brm = (BoundedRangeModel) e.getSource();
        int shift = ((Integer) changerMap.get(brm)).intValue();
        int mask = 0xffffffff ^ (0xff << shift);
        int val = brm.getValue() << shift;
        int newRGB = (pan.getBackground().getRGB() & mask) | val;
        pan.setBackground(new Color(newRGB));
    }

    /**
     * updates the display's ball size with the value contained in the supplied
     * {@code DecimalField}; used by the other two updateBallSize methods
     * to perform the actual update
     *
     * @param df a {@code DecimalField} containing the new ball size
     */
    protected void updateBallSize(DecimalField df) {
        double d = df.getValue();
        ((MoleculeRotatePanel) pan).setBallSizeBall(d);
        ((MoleculeRotatePanel) pan).setBallSizeSpace(d);
    }

    /**
     * a call-back method that updates the display's ball size in response to
     * an {@code ActionEvent} on a {@code DecimalField}
     *
     * @param ae the {@code ActionEvent} that triggered the update
     */
    void updateBallSize(ActionEvent ae) {
        updateBallSize((DecimalField) ae.getSource());
    }

    /**
     * a call-back method that updates the display's ball size in response to a
     * {@code FocusEvent} on a {@code DecimalField}
     *
     * @param fe the {@code FocusEvent} that triggered the update
     */
    void updateBallSize(FocusEvent fe) {
        updateBallSize((DecimalField) fe.getSource());
    }

    /**
     * changes the background color of the display panel to the specified
     * {@code Color}
     *
     * @param c the new {@code Color}
     */
    protected void updatePanelColor(Color c) {
        int rgb = c.getRGB();
        int mask = 0xff;
        
        for (Iterator it = changerMap.entrySet().iterator();
                it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            
            int shift = ((Integer) entry.getValue()).intValue();
            BoundedRangeModel brm = (BoundedRangeModel) entry.getKey();
            
            brm.setValue((rgb >> shift) & mask);
        }
    }

}
