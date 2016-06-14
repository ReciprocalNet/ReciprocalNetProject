/*
 * Reciprocal Net Project
 *
 * @(#) JaMMer.java
 *
 * By John C. Bollinger, Indiana University Molecular Structure Center
 *
 * 20-Nov-2002: jobollin changed the import statement for Rotate3DModel
 * 09-Jan-2003: jobollin seperated the file comment from the class-level
 *              Javadoc comment, cleaned up unused imports, made all imports
 *              explicit, cleaned up the class-level Javadoc comment
 * 19-Feb-2003: jobollin changed references to table.dat in the class-level
 *              javadocs to refer instead to element_data.txt as part of task
 *              #682
 * 03-Mar-2003: jobollin reformatted the source and extended and revised the
 *              javadoc comments as part of task #749
 * 03-Jul-2003: jobollin adjusted the renderer to scale the image according
 *              to the display size of the applet
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.applet.jamm.TableContainer;

/**
 * <h1>Java Molecular Modeler / renderer (JaMMer) version 1.2.
 * <p>
 * <h2>Overview</h2>
 * <p>
 * JaMMer is an applet for displaying and manipulating a three-dimensional
 * molecular model inside a browser, with a view toward submitting it to a
 * back-end, high-quality rendering engine.  JaMMer provides for rotating and
 * scaling the model, and can present wireframe, ball & stick, and
 * pseudo-space-filled views.  Rendering is of either a ball & stick or a
 * space filled representation.  Depth cueing is used in all modes to improve
 * the viewer's 3-D perception, and size cueing is applied as well in ball &
 * stick and space-filled modes.
 * </p><p>
 * This applet is written with the use of the Swing implementation of JDK
 * 1.2.2.  Currently it requires Sun's Java Plugin to run in some browsers, as
 * the VMs of MSIE and of NS Communicator 4.x (and lower) do not support the
 * latest Swing classes.  Netscape 6 and higher, Mozilla 1.0 and higher, and
 * several other browsers ship with Java 2 support built in.
 * </p>
 * <h2>Architecture</h2>
 * <p>
 * The JaMMer class is a subclass of JApplet.  It constructs the components and
 * builds and manages the UI.  Swing components are used throughout. The
 * actual molecule rendering is performed by a DemoRotatePanel instance;
 * DemoRotatePanel is a custom descendant of JPanel.  (In between JPanel and
 * DemoRotatePanel is Rotate3DPanel, another custom component that is a little
 * more general.)  DemoRotatePanels (and Rotate3DPanels) handle various types
 * of mouse input directly, but they depend on an outside class (JaMM in this
 * case) to manipulate their rendering controls.
 * </p>
 * <h2>Parameters
 * <p>
 * JaMMer must read a model file and a table file to get the information with
 * which to initialize the DemoRotatePanel.  The "model" and "table"
 * parameters, if present, specify the files to use for this purpose.  If they
 * are missing then the defaults are "model.obj" and "element_data.txt". JaMM
 * uses these along with its getDocumentBase() method to construct URLs from
 * which to read the data; this operation is potentially viewer-dependant.
 * </p><p>
 * JaMMer also recognizes a "color" parameter.  If specified, this parameter is
 * interpreted as an integer to be used as an argument to a Color constructor;
 * the color constructed this way is set as the initial background color of
 * the DemoRotatePanel.  If this parameter is not specified then the
 * DemoRotatePanel initially retains its default background color.
 * </p>
 * <h2>Controls</h2>
 * <p>
 * The GUI is pretty much self-explanatory.  All of the controls are hooks into
 * the DemoRotatePanel's options and programmatic controls.  The
 * DemoRotatePanel itself bears a little explanation, however:
 * <ul>
 *   <li>left drag to rotate the molecule</li>
 *   <li>right drag or alt- left drag to scale the molecule</li>
 * </ul>
 * </p>
 * <h2>Credits</h2>
 * <p>
 * Some supporting and inner classes are derived from materials provided by
 * other authors, including copyrighted code licensed from Sun Microsystems
 * and material posted on Sun's JDC site.  The JaMMer 1.0 applet is derived
 * from JaMM 2.1 by John C. Bollinger and John N. Huffman.
 * </p>
 *
 * @author John C. Bollinger, Indiana University Molecular Structure Center
 */
public class JaMMer
        extends BasicJaMM {

    /** The default rendered image size */
    final static int DEFAULT_RENDER_SIZE = 500;

    /** The maximium permitted rendered image size */
    final static int RENDER_MAX = 1000;

    /** a ball size control for ball &amp; stick mode */
    protected LabelledDecimalPanel bsBallSize;

    /** a ball size control for space filled mode */
    protected LabelledDecimalPanel spaceBallSize;

    /** the name of the model to load */
    protected String modName = null;

    /** a heading for the output HTML file */
    protected String outputHeading;

    /** A <code>String</code> representation of the rendering size to use */
    protected String renderSize;

    /** The blue component of the current background color */
    protected int bblue;

    /** The green component of the current background color */
    protected int bgreen;

    /** The red component of the current background color */
    protected int bred;

    /** The currently selected viewing mode */
    protected int selectedMode = DemoRotatePanel.SPACE_MODE;

    /** The background color dialog box */
    private JDialog backgd;

    /** A string containing the URL of the image rendering service */
    private String imageScript;

    /**
     * Provides brief information about JaMMer.
     *
     * @return a <code>String</code> containing brief information about JaMMer
     */
    public String getAppletInfo() {
        return new String(
            "JaMMer version 1.2\nby John C. Bollinger\n" + 
            "copyright (c) 1999, 2002, the Trustees of Indiana University");
    }

    /**
     * {@inheritDoc}
     */
    public void initInstance(JPanel content) {
        super.initInstance(content);
        JLabel jl;

        int appletHeight = getHeight();

        imageScript = getParameter("render");

        /* outer border */
        content.setBorder(new BevelBorder(BevelBorder.RAISED));

        /* initialize the sidebar */
        JPanel sidebar = new JPanel();
        sidebar.setAlignmentX(0.0f);

        /* Headings */
        Font f = content.getFont();
        f = f.deriveFont(Font.BOLD, f.getSize2D() * 1.25f);
        jl = new JLabel("Indiana University");
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jl.setFont(f);
        sidebar.add(jl);
        jl = new JLabel("Molecular Structure Center");
        jl.setFont(f);
        sidebar.add(jl);
        jl = new JLabel("High-Speed Rendering Tool");
        jl.setFont(f);
        sidebar.add(jl);

        /* construct the controls, but do not add them to the sidebar yet */
        JPanel controls = buildControls();
        int sbWidth = controls.getPreferredSize().width;

        /* The instruction text */
        String text =
            new String(
                "Left-click and drag in the preview window (left) to rotate the molecule;"
                + " right-click and drag to scale it."
                + "  Select rendering style and ball size with the controls below,"
                + " and click the 'Render It!' button to get your ray-traced image.");
        JTextArea ta = new JTextArea(text);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setBackground(sidebar.getBackground());
        ta.setBorder(BorderFactory.createEmptyBorder(20, 5, 10, 5));

        /* calculate text area size by breaking up the text by words and
           counting the number of lines required for them in the current font */
        FontMetrics taFM = ta.getFontMetrics(ta.getFont());
        Insets ins = ta.getInsets();
        int taNRow = 1;
        int charOff = 0;
        int lineOff = 0;
        int tBegin = 0;
        int tEnd;
        final int lineMax = sbWidth - ins.left - ins.right;
        final int tlen = text.length();
        while (charOff < tlen) {
            charOff = text.indexOf(' ', charOff);
            if (charOff < 0) {
                charOff = tlen;
            }
            tEnd = charOff - 1;
            for (; (charOff < tlen) && (text.charAt(charOff) == ' ');
                    charOff++) {
                // does nothing
            }
            if (taFM.stringWidth(text.substring(lineOff, charOff - 1)) > lineMax) {
                taNRow++;
                if (tBegin == lineOff) {
                    lineOff = charOff;
                } else if ((tEnd >= lineOff)
                        && (taFM.stringWidth(text.substring(lineOff, tEnd)) <= lineMax)) {
                    lineOff = charOff;
                } else {
                    lineOff = tBegin;
                }
            }
            tBegin = charOff;
        }

        ta.setRows(taNRow);
        ta.setPreferredSize(new Dimension(sbWidth,
                (taNRow * taFM.getHeight()) + ins.top + ins.bottom));
        ta.setMaximumSize(ta.getPreferredSize());
        sidebar.add(ta);

        /* now put in the controls */
        sidebar.add(controls);

        /* put on an image at the bottom */
        String imageURLString = getParameter("imageurl");
        if (imageURLString != null) {
            System.out.println("Opening image URL:");
            try {
                URL imageURL = new URL(getDocumentBase(), imageURLString);
                System.out.println("   " + imageURL.toString());
                ImageIcon i = new ImageIcon(imageURL);
                System.out.println("Image loaded; pasting it on a label.\n");
                System.out.println("Resizing the image");
                if (i.getIconWidth() > (sbWidth - 10)) {
                    i.setImage(i.getImage().getScaledInstance(sbWidth - 10, -1,
                            Image.SCALE_SMOOTH));
                }
                JLabel l = new JLabel(i);
                l.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                sidebar.add(l);
            } catch (MalformedURLException e) {
                System.out.println("Bad image URL; continuing...");
            }
        }

        /* add the sidebar to the Applet's content pane */
        sidebar.setPreferredSize(new Dimension(sbWidth, appletHeight));
        content.add(sidebar, BorderLayout.EAST);

        /* initialize the rendering size */
        renderSize = Integer.toString(DEFAULT_RENDER_SIZE);
        try {
            String renderSizeString = getParameter("rsize");
            if (renderSizeString != null) {
                int rs = Integer.decode(renderSizeString).intValue();
                if ((rs > 0) && (rs <= RENDER_MAX)) {
                    renderSize = renderSizeString;
                }
            }
        } catch (NumberFormatException e) {
            // does nothign
        }
        outputHeading = getParameter("heading");
    }

    /**
     * Constructs the background color selection dialog box
     */
    protected void buildBackgroundDialog() {
        JPanel p;
        JPanel p2;
        JButton but;
        SliderField sf;
        Color c = pan.getBackground();

        bred = c.getRed();
        bgreen = c.getGreen();
        bblue = c.getBlue();

        backgd = new JDialog();
        backgd.setTitle("Background Color");

        p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p = new JPanel(new GridLayout(4, 1));
        sf = new SliderField(255, 0, 255, new JLabel("red"));
        sf.getModel().addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    bred = ((BoundedRangeModel) (e.getSource())).getValue();
                    pan.setBackground(new Color(bred, bgreen, bblue));
                }
            });
        sf.setValue(bred);
        p.add(sf);
        sf = new SliderField(255, 0, 255, new JLabel("green"));
        sf.getModel().addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    bgreen = ((BoundedRangeModel) (e.getSource())).getValue();
                    pan.setBackground(new Color(bred, bgreen, bblue));
                }
            });
        sf.setValue(bgreen);
        p.add(sf);
        sf = new SliderField(255, 0, 255, new JLabel("blue"));
        sf.getModel().addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    bblue = ((BoundedRangeModel) (e.getSource())).getValue();
                    pan.setBackground(new Color(bred, bgreen, bblue));
                }
            });
        sf.setValue(bblue);
        p.add(sf);
        p2.add(p);
        but = new JButton("Close");
        but.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    backgd.hide();
                }
            });
        p2.add(but);

        backgd.getContentPane().add(p2);
        backgd.pack();
    }

    /**
     * Constructs the user control panel for the applet, including registering
     * event handlers to manipulate various components appropriately
     *
     * @return a <code>JPanel</code> containing the controls
     */
    protected JPanel buildControls() {
        JRadioButton RB;
        JButton B;
        JPanel controls;
        JPanel sub1;
        final ButtonGroup bGroup = new ButtonGroup();
        final JCheckBox lineModeCheckBox = new JCheckBox();
        GridBagLayout gridb = new GridBagLayout();
        GridBagConstraints constr = new GridBagConstraints();
        DecimalField DF;
        DecimalFormat dFormat = new DecimalFormat("0.00");

        /* set up the controls */
        controls = new JPanel(gridb);

        /* Drawing mode control */
        lineModeCheckBox.setText("line-mode preview");
        lineModeCheckBox.setSelected(true);
        lineModeCheckBox.addItemListener(
            new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        ((DemoRotatePanel) pan).setDrawMode(DemoRotatePanel.LINE_MODE);
                    } else {
                        ((DemoRotatePanel) pan).setDrawMode(selectedMode);
                    }
                }
            } );
        constr.gridwidth = GridBagConstraints.REMAINDER;
        constr.insets.left = 5;
        constr.anchor = GridBagConstraints.WEST;
        gridb.setConstraints(lineModeCheckBox, constr);
        controls.add(lineModeCheckBox);

        /* Rendering Mode Controls */

        /* Space Filled Mode Button */
        RB = new JRadioButton("Space Filled", true);
        RB.setActionCommand("space");
        RB.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    selectedMode = DemoRotatePanel.SPACE_MODE;
                    if (spaceBallSize != null) {
                        spaceBallSize.setEnabled(true);
                    }
                    if (bsBallSize != null) {
                        bsBallSize.setEnabled(false);
                    }
                    if (!lineModeCheckBox.isSelected()) {
                        ((DemoRotatePanel) pan).setDrawMode(selectedMode);
                    }
                }
            } );
        bGroup.add(RB);
        constr.gridwidth = GridBagConstraints.RELATIVE;
        constr.weightx = 1.0;
        gridb.setConstraints(RB, constr);
        controls.add(RB);

        /* Space Filled Mode Ball Size */
        DF = new DecimalField(1.0, 4, dFormat);
        DF.addFocusListener(
            new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    // does nothing
                }

                public void focusLost(FocusEvent fe) {
                    ((DemoRotatePanel) pan).setBallSizeSpace(spaceBallSize.getField()
                            .getValue());
                    spaceBallSize.getField().setValue(((DemoRotatePanel) pan)
                            .getBallSizeSpace());
                }
            } );
        DF.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ((DemoRotatePanel) pan).setBallSizeSpace(spaceBallSize.getField()
                            .getValue());
                    spaceBallSize.getField().setValue(((DemoRotatePanel) pan)
                            .getBallSizeSpace());
                }
            } );
        spaceBallSize = new LabelledDecimalPanel(DF, new JLabel("ball size"));
        spaceBallSize.setEnabled(true);
        constr.gridwidth = GridBagConstraints.REMAINDER;
        constr.weightx = 0.0;
        constr.insets.left = 0;
        constr.anchor = GridBagConstraints.EAST;
        gridb.setConstraints(spaceBallSize, constr);
        controls.add(spaceBallSize);

        /* Ball & Stick Mode Button */
        RB = new JRadioButton("Ball and Stick", false);
        RB.setActionCommand("ball");
        RB.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    selectedMode = DemoRotatePanel.BALL_STICK_MODE;
                    if (spaceBallSize != null) {
                        spaceBallSize.setEnabled(false);
                    }
                    if (bsBallSize != null) {
                        bsBallSize.setEnabled(true);
                    }
                    if (!lineModeCheckBox.isSelected()) {
                        ((DemoRotatePanel) pan).setDrawMode(selectedMode);
                    }
                }
            } );
        bGroup.add(RB);
        constr.gridwidth = GridBagConstraints.RELATIVE;
        constr.weightx = 1.0;
        constr.insets.left = 5;
        constr.anchor = GridBagConstraints.WEST;
        gridb.setConstraints(RB, constr);
        controls.add(RB);

        /* Ball & Stick Mode Ball Size */
        DF = new DecimalField(1.0, 4, dFormat);
        DF.addFocusListener(
            new FocusListener() {
                public void focusGained(FocusEvent fe) {
                    // does nothing
                }

                public void focusLost(FocusEvent fe) {
                    ((DemoRotatePanel) pan).setBallSizeBall(bsBallSize.getField()
                            .getValue());
                    bsBallSize.getField().setValue(((DemoRotatePanel) pan)
                            .getBallSizeBall());
                }
            } );
        DF.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ((DemoRotatePanel) pan).setBallSizeBall(bsBallSize.getField()
                            .getValue());
                    bsBallSize.getField().setValue(((DemoRotatePanel) pan)
                            .getBallSizeBall());
                }
            } );
        bsBallSize = new LabelledDecimalPanel(DF, new JLabel("ball size"));
        bsBallSize.setEnabled(false);
        constr.gridwidth = GridBagConstraints.REMAINDER;
        constr.weightx = 0.0;
        constr.insets.left = 0;
        constr.anchor = GridBagConstraints.EAST;
        gridb.setConstraints(bsBallSize, constr);
        controls.add(bsBallSize);

        /* utility buttons */
        sub1 = new JPanel(new GridLayout(1, 2));

        B = new JButton("Background");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 2), B.getBorder()));
        B.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (backgd == null) {
                        buildBackgroundDialog();
                    }
                    backgd.show();
                }
            } );
        sub1.add(B);
        B = new JButton("Reset");
        B.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 2, 0, 0), B.getBorder()));
        B.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    pan.reset();
                }
            } );
        sub1.add(B);
        constr.anchor = GridBagConstraints.CENTER;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.insets.left = 5;
        constr.insets.right = 5;
        constr.insets.bottom = 2;
        gridb.setConstraints(sub1, constr);
        controls.add(sub1);

        /* Render Button */
        B = new JButton("Render It!");
        B.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Color bgColor = pan.getBackground();
                    String renderType = bGroup.getSelection().getActionCommand();
                    double ballSize;
                    if (renderType.equals("ball")) {
                        ballSize = ((DemoRotatePanel) pan).getBallSizeBall();
                    } else {
                        ballSize = ((DemoRotatePanel) pan).getBallSizeSpace();
                    }
                    requestImage(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(),
                        renderType, renderSize, ballSize * 0.8);
                }
            } );
        constr.insets.top = 2;
        constr.insets.bottom = 0;
        gridb.setConstraints(B, constr);
        controls.add(B);

        return controls;
    }

    protected Rotate3DPanel createMainPanel(Rotate3DModel mod, TableContainer tc) {
        int appletHeight = getHeight();
        DemoRotatePanel drp = new DemoRotatePanel(mod, tc.colorTab, tc.radii);
        drp.setBorder(new BevelBorder(BevelBorder.LOWERED));
        drp.setPreferredSize(new Dimension(appletHeight, appletHeight));
        return drp;
    }

    /**
     * Reports errors that occur during execution. May be
     * overridden by subclasses that want to use alternative output
     * facilities.  (Status window, popup box, ...)
     *
     * @param  type a <code>String</code> describing the type of error
     * @param  message a <code>String</code> containing a detail message
     */
    protected void reportError(String type, String message) {
        JOptionPane.showMessageDialog(null, type + ": " + message, "Oops",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Requests a ray-traced image to be generated in the current molecule
     * orientation with the specified background color, ball scale, 
     * and image type, and with the configured image size
     *
     * @param  br the red component of the background color
     * @param  bg the green component of the background color
     * @param  bb the blue component of the background color
     * @param  type a <code>String</code> describing the rendering type
     * @param  size a <code>String</code> indicating the image size
     * @param  ssize a <code>double</code> indicating the ball scale
     */
    protected void requestImage(int br, int bg, int bb, String type,
        String size, double ssize) {
        double[] ang = pan.calculateAngles();
        String hstring =
            (outputHeading == null) ? "" : ("&heading=" + outputHeading);
        String data =
            "size=" + size + "&drawtype=" + type + "&name=" + modName + "&x="
            + ang[0] + "&y=" + ang[1] + "&z=" + ang[2] + "&r=" + (br / 255.0)
            + "&g=" + (bg / 255.0) + "&b=" + (bb / 255.0) + "&distance="
            + (550.0 * pan.getScale()
              / Math.max(pan.getWidth(), pan.getHeight()))
            + "&bsize=" + ssize + hstring;
        RunCGI(imageScript, data);
    }
}
