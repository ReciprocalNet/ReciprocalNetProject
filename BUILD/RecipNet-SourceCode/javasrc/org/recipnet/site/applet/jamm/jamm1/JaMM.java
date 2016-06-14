/*
 * Reciprocal Net Project
 *
 * JaMM.java
 *
 * xx-xxx-1996: jnhuffma original programmer
 * 04-May-2000: jobollin did last update
 * 05-Nov-2002: jobollin implemented interfaces to the new server-side support
 * 20-Nov-2002: jobollin changed the import statements for Matrix3D,
 *              BadAtomException, BadBondException, and FileFormatException
 * 12-Dec-2002: jobollin added the getImageResource method and modified the
 *              image loading code to fix bug #631 and address the JaMM 1 part
 *              of task #509
 * 13-Dec-2002: jobollin made all imports explicit
 * 09-Jan-2003: jobollin cleaned up unused variables
 * 24-Feb-2003: jobollin reformatted the source and somewhat extended the
 *              javadoc comments as part of task #749
 * 24-Feb-2003: jobollin removed the WarningBox inner class and replaced it
 *              with a method on TraceBox that creates a suitable Dialog
 * 24-Feb-2003: jobollin removed the unused field "save"
 * 24-Feb-2003: jobollin removed multiple extraneous UI component references
 *              from the TraceBox inner class
 * 01-Jul-2003: midurbin fixed bug #957 in paint()
 * 11-Jul-2003: jrhanna added sampleHistoryId parameter
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm1 package 
 *              to org.recipnet.site.applet.jamm.jamm1; changed package 
 *              references to match source tree reorganization
 * 06-Jan-2005: jobollin added implementation comments regarding the use of
 *              URLEncoder.encode(String), which was deprecated in favor of
 *              a newly added method in Java 1.4
 * 06-Jan-2005: jobollin removed usage of the deprecated StreamTokenizer
 *              constructor, and in so doing, realigned the exception
 *              handling for the getData() method.
 * 05-Jun-2006: jobollin reformatted the source
 * 23-Jun-2006: jobollin replaced deprecated URLEncoder
 */

package org.recipnet.site.applet.jamm.jamm1;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.recipnet.common.Matrix3D;
import org.recipnet.common.Utf8UrlEncoder;
import org.recipnet.site.applet.jamm.AWTRatioSliderField;
import org.recipnet.site.applet.jamm.AWTSliderField;
import org.recipnet.site.applet.jamm.BadAtomException;
import org.recipnet.site.applet.jamm.BadBondException;
import org.recipnet.site.wrapper.FileFormatException;

/**
 * <strong>Ja</strong>va <strong>M</strong>olecular <strong>M</strong>odeler,
 * a highly functional Java 1.1 applet for interactive display of molecular
 * images; allows the user to rotate the molecule in wireframe, space filled,
 * and ball and stick representations, and provides interfaces to quality image
 * generation via external programs
 */
public class JaMM extends Applet {

    /** the <code>MenuItem</code> for requesting a rendered image */
    protected MenuItem imageItem;

    /** the <code>MenuItem</code> for requesting an ORTEP */
    protected MenuItem ortepItem;

    /** the X coordinate of the last mouse keypress */
    protected int mouseXDown = -1;

    /** the Y coordinate of the last mouse keypress */
    protected int mouseYDown = -1;

    /** The Reciprocal Net sample id of the current sample */
    protected long sampleId;

    /** The Reciprocal Net historyId of the current sample */
    protected long sampleHistoryId;

    /** The <code>CheckboxMenuItem</code> selecting red/green stereo mode */
    protected CheckboxMenuItem RG;

    /** The <code>CheckboxMenuItem</code> selecting angle measurement mode */
    protected CheckboxMenuItem anglemode;

    /**
     * The <code>CheckboxMenuItem</code> controlling orientation angle display
     */
    protected CheckboxMenuItem angles;

    /**
     * The <code>CheckboxMenuItem</code> selecting distance measurement mode
     */
    protected CheckboxMenuItem bondmode;

    /**
     * The <code>CheckboxMenuItem</code> selecting rotation mode
     */
    protected CheckboxMenuItem drawmode;

    /**
     * The <code>CheckboxMenuItem</code> controlling hydrogen atom display
     */
    protected CheckboxMenuItem hydro;

    /**
     * The <code>CheckboxMenuItem</code> controlling label display
     */
    protected CheckboxMenuItem labels;

    /**
     * The <code>CheckboxMenuItem</code> selecting stereographic mode
     */
    protected CheckboxMenuItem stereo;

    /** the font to use for messages */
    protected Font chemfont;

    /** the frame that holds the menu bar */
    protected Frame menuFrame;

    /**
     * a <code>Frame</code> to display a message when the ray tracer is in use
     */
    private Frame warningBox;

    /** a graphics context for rendering to the offscreen buffer */
    protected Graphics offscreen;

    /**
     * an <code>Image</code> with which to display the buffered molecule view
     */
    protected Image screen;

    /** The coordinate tranformation matrix for the current view */
    private Matrix3D amat = new Matrix3D();

    /** A transformation for a modification of the view */
    private Matrix3D tmat = new Matrix3D();

    /** a notification dialog */
    protected MessageBox mess;

    /** the structural model */
    protected Model3D md = null;

    /** the name of the model file */
    private String modName = null;

    /** the ray tracer user interface */
    protected TraceBox box;

    /** flags whether or not to draw the balls */
    protected boolean balls;

    /**
     * Flags whether or not to draw the sticks (sticks drawn when
     * <code>false</code>
     */
    protected boolean space;

    /**
     * A revised scale factor for the model, not yet applied
     * 
     * @see #xfac xfac
     */
    private float newxfac;

    /**
     * A scale factor for the molecule view based on both the molecule size and
     * any user resizing
     */
    private float xfac;

    /**
     * the number of points currently selected; used only in angle measuring
     * mode
     */
    private int aclicknum = 0;

    /** The number of the first vertex in the current angle */
    private int apoint1 = 0;

    /** The number of the second vertex in the current angle */
    private int apoint2 = 0;

    /** The number of the third vertex in the current angle */
    private int apoint3 = 0;

    /**
     * the number of points currently selected; used only in distance measuring
     * mode
     */
    int bclicknum = 0;

    /** The number of the first vertex in the current distance */
    int bpoint1;

    /** The number of the second vertex in the current distance */
    int bpoint2;

    /**
     * a <code>String</code> representation of the URL of the image rendering
     * service
     */
    private String imageScript;

    /**
     * a <code>String</code> representation of the URL of the ORTEP rendering
     * service
     */
    private String ortepScript;

    /**
     * performs one-time initialization tasks; mainly reading the model and
     * preparing the user interface
     */
    public void init() {
        /* read applet parameters */
        String sampleIdStr = getParameter("sampleId");
        Reader reader = null;

        if (sampleIdStr != null) {
            try {
                sampleId = Long.parseLong(sampleIdStr);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
                sampleId = -1L;
            }
        } else {
            sampleId = -1L;
        }

        String historyIdStr = getParameter("sampleHistoryId");
        if (historyIdStr != null) {
            try {
                sampleHistoryId = Long.parseLong(historyIdStr);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
                sampleHistoryId = -1L;
            }
        } else {
            sampleHistoryId = -1L;
        }

        imageScript = getParameter("render");
        ortepScript = getParameter("ortep");
        modName = getParameter("model");
        if (modName == null) {
            modName = "model.obj";
        }

        /* we mustn't be too small */
        resize((getSize().width <= 20) ? 400 : getSize().width,
                (getSize().height <= 20) ? 400 : getSize().height);

        /* prepare other windows */
        menuFrame = new Frame("JaMM V1.0");
        menuFrame.setMenuBar(buildMB());
        menuFrame.pack();
        menuFrame.setSize(300, 100);
        mess = new MessageBox(menuFrame);
        mess.setModal(true);

        setLayout(new BorderLayout());

        /* load model */
        URLConnection.setDefaultAllowUserInteraction(true);
        try {
            float xw;

            reader = new InputStreamReader(new BufferedInputStream(new URL(
                    getDocumentBase(), modName).openStream()), "US-ASCII");
            md = getData(reader);
            md.SetApplet(this);
            md.bball = getImageResource("bball.gif");
            md.blball = getImageResource("blball.gif");
            md.crball = getImageResource("crball.gif");
            md.wball = getImageResource("wball.gif");
            md.yball = getImageResource("yball.gif");
            md.gball = getImageResource("gball.gif");
            md.goldball = getImageResource("goldball.gif");
            md.rball = getImageResource("rball.gif");
            md.cpball = getImageResource("cpball.gif");
            md.findBB();
            xw = Math.max(md.xmax - md.xmin, Math.max(md.ymax - md.ymin,
                    md.zmax - md.zmin));
            xfac = 0.7f * Math.min(getSize().width / xw, getSize().height / xw);
            newxfac = xfac;
        } catch (Exception e) {

            /* attempt to proceed no matter what */

            System.err.println(e.getMessage());

            md = new Model3D();
            ortepItem.setEnabled(false);
            imageItem.setEnabled(false);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // discard it
                }
            }
        }

        /* initialize state variables */
        chemfont = new java.awt.Font("Courier", 0, 12);
        balls = false;
        space = false;

        /* create a space for double-buffering */
        try {
            screen = createImage(getSize().width, getSize().height);
            offscreen = screen.getGraphics();
        } catch (Exception e) {
            // FIXME: what should be done?
        }

        /*
         * enable keyboard and mouse events (these are handled locally, without
         * a handler)
         */
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    /**
     * Draws the current view of the model by use of the supplied graphics
     * context
     * 
     * @param g the <code>Graphics</code> with which to draw the view
     */
    public void paint(Graphics g) {
        g.setFont(chemfont);
        offscreen.setFont(chemfont);
        if (md != null) {
            int winW = getSize().width;
            int winH = getSize().height;

            // This is for stereo view and red-green view modes
            if (stereo.getState() || RG.getState()) {
                if (stereo.getState()) {
                    winW /= 2;
                }
                md.stereomat.unit();
                md.stereomat.translate(-(md.xmin + md.xmax) / 2,
                        -(md.ymin + md.ymax) / 2, -(md.zmin + md.zmax) / 2);
                md.stereomat.mult(amat);
                md.stereomat.yrot(5.0);
                md.stereomat.scale(newxfac, -newxfac, (16 * newxfac) / winW);
                md.stereomat.translate(winW / 2, winH / 2, 8);
            }

            // This is the matrix used for all rotation and scaling
            md.mat.unit();
            md.mat.translate(-(md.xmin + md.xmax) / 2,
                    -(md.ymin + md.ymax) / 2, -(md.zmin + md.zmax) / 2);
            md.mat.mult(amat);
            md.mat.scale(newxfac, -newxfac, (16 * newxfac) / winW);
            if (stereo.getState()) {
                md.mat.translate(((winW * 3) / 2), winH / 2, 8);
            } else {
                md.mat.translate(winW / 2, winH / 2, 8);
            }
            md.transformed = false;

            // This is where the actual drawing begins
            if (offscreen != null) {
                // set the background color to white, if in red-green mode, else
                // to grey
                if (RG.getState()) {
                    offscreen.setColor(Color.white);
                } else {
                    offscreen.setColor(getBackground());
                }
                offscreen.fillRect(0, 0, getSize().width, getSize().height);

                // draw the molecule, using its own paint function
                md.paint(offscreen, labels.getState(), hydro.getState(), balls,
                        stereo.getState(), RG.getState(), space,
                        (newxfac / xfac));

                // Draw the angles, if appropriate
                if (angles.getState()) {
                    showAngles(offscreen);
                }

                // This is used for showing and calculating the bond distance
                if (bclicknum >= 1) {
                    offscreen.setColor(Color.red);
                    offscreen.drawString(md.Comp[bpoint1 / 3],
                            md.tvert[bpoint1], md.tvert[bpoint1 + 1]);
                }
                if (bclicknum >= 2) {
                    offscreen.drawString(md.Comp[bpoint2 / 3],
                            md.tvert[bpoint2], md.tvert[bpoint2 + 1]);
                    offscreen.drawLine(md.tvert[bpoint1],
                            md.tvert[bpoint1 + 1], md.tvert[bpoint2],
                            md.tvert[bpoint2 + 1]);
                }

                // This is used for calculating and displaying the bond angles
                if (aclicknum >= 1) {
                    offscreen.setColor(Color.blue);
                    offscreen.drawString(md.Comp[apoint1 / 3],
                            md.tvert[apoint1], md.tvert[apoint1 + 1]);
                }
                if (aclicknum >= 2) {
                    offscreen.drawString(md.Comp[apoint2 / 3],
                            md.tvert[apoint2], md.tvert[apoint2 + 1]);
                    offscreen.drawLine(md.tvert[apoint1],
                            md.tvert[apoint1 + 1], md.tvert[apoint2],
                            md.tvert[apoint2 + 1]);
                }
                if (aclicknum >= 3) {
                    offscreen.drawString(md.Comp[apoint3 / 3],
                            md.tvert[apoint3], md.tvert[apoint3 + 1]);
                    offscreen.drawLine(md.tvert[apoint2],
                            md.tvert[apoint2 + 1], md.tvert[apoint3],
                            md.tvert[apoint3 + 1]);
                }

                // Draw the applet border
                drawBorder(offscreen);
            }
        }

        // double buffer draw to graphics context
        g.drawImage(screen, 0, 0, this);
    }

    /**
     * Performs tasks necessary each time this applet is started; these include
     * obtaining the keyboard focus and assuring that the menu frame is visible
     */
    public void start() {
        // be sure to open the pull down menu window
        requestFocus();
        menuFrame.setVisible(true);
    }

    /**
     * Performs tasks necessary to stop the applet; this involves hiding any
     * open windows
     */
    public void stop() {
        // Be sure to get rid of any windows that might be showing, when a
        // person
        // leaves this applet.
        menuFrame.setVisible(false);
        mess.setVisible(false);
        if (box != null) {
            box.setVisible(false);
        }
        if (warningBox != null) {
            warningBox.setVisible(false);
        }
    }

    /**
     * Updates the view of the model on the supplied graphics context (normally
     * a context for rendering on the offscreen buffer)
     * 
     * @param g the <code>Graphics</code> with which to draw the view
     */
    public void update(Graphics g) {
        // This is important for double buffering
        if (offscreen == null) {
            g.clearRect(0, 0, getSize().width, getSize().height);
        }
        paint(g);
    }

    /**
     * Creates a new Model3D and loads it with data from the provided stream
     * 
     * @param reader the <code>Reader</code> from which to load the model
     * @return a <code>Model3D</code> representing the model
     * @throws FileFormatException if the data read from the provided Reader do
     *         not constitute a valid CRT file
     * @throws IOException if one occurs while reading the CRT data from the
     *         provided Reader
     */
    protected Model3D getData(Reader reader) throws FileFormatException,
            IOException {
        Model3D mod = null;
        int num = 0;

        /* set up a stream tokenizer on the reader */
        StreamTokenizer st = new StreamTokenizer(reader);

        st.ordinaryChars('\u0021', '\uffff');
        st.wordChars('\u0021', '\uffff');
        st.eolIsSignificant(true);
        st.commentChar('#');
        st.parseNumbers();

        st.nextToken();

        /* Read the model input via the tokenizer */
        scan: while (true) {
            switch (st.ttype) {
                default:
                    break scan;
                case StreamTokenizer.TT_EOL:
                    break;
                case StreamTokenizer.TT_WORD:
                    if (st.sval.compareTo("CARTESIAN") == 0) {
                        mod = new Model3D();
                        while ((st.ttype != StreamTokenizer.TT_EOL)
                                && (st.ttype != StreamTokenizer.TT_EOF)) {
                            st.nextToken();
                        }
                        st.nextToken();
                        double x;
                        double y;
                        double z;
                        String label;
                        do {
                            label = st.sval;

                            x = 0d;
                            y = 0d;
                            z = 0d;
                            if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                                x = st.nval;
                                if (st.nextToken()
                                        == StreamTokenizer.TT_NUMBER) {
                                    y = st.nval;
                                    if (st.nextToken()
                                            == StreamTokenizer.TT_NUMBER) {
                                        z = st.nval;
                                    } else {
                                        throw new BadAtomException(label);
                                    }
                                } else {
                                    throw new BadAtomException(label);
                                }
                                if (st.nextToken()
                                        == StreamTokenizer.TT_NUMBER) {
                                    num = (int) st.nval;
                                } else {
                                    throw new BadAtomException(label);
                                }
                            } else {
                                throw new BadAtomException(label);
                            }
                            mod.addVert(label, (float) x, (float) y, (float) z,
                                    num);
                            while ((st.ttype != StreamTokenizer.TT_EOL)
                                    && (st.ttype != StreamTokenizer.TT_EOF)) {
                                st.nextToken();
                            }
                            if (st.ttype == StreamTokenizer.TT_EOF) {
                                break scan;
                            }
                            st.nextToken();
                        } while (st.sval.compareTo("ENDATOMS") != 0);
                    } else if (st.sval.compareTo("ENDATOMS") == 0) {
                        while ((st.ttype != StreamTokenizer.TT_EOL)
                                && (st.ttype != StreamTokenizer.TT_EOF)) {
                            st.nextToken();
                        }
                        if (st.ttype == StreamTokenizer.TT_EOF) {
                            break scan;
                        }
                        int n;

                        while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                            n = (int) st.nval;
                            if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
                                throw new BadBondException("");
                            }
                            mod.addLine(n - 1, (int) st.nval - 1);
                            st.nextToken();
                        }
                        break scan;
                    } else if (st.sval.compareTo("ENDBONDS") == 0) {
                        break scan;
                    } else {
                        while ((st.nextToken() != StreamTokenizer.TT_EOL)
                                && (st.ttype != StreamTokenizer.TT_EOF)) {
                            // do nothing
                        }
                        st.nextToken();
                    }
            }
        }

        if (mod == null) {
            throw new FileFormatException("Not a valid CRT file");
        } else {
            return mod;
        }
    }

    /**
     * requests an ORTEP image, corresponding to the current model and view,
     * from the configured ORTEP service
     */
    protected final void doOrtep() {
        double[] ang = calcAngles();

        /*
         * NOTE: URLEncoder.encode(String) is deprecated in Java 1.4 and above
         * in favor of a new method, URLEncoder.encode(String, String),
         * introduced at that time. This class must be compatible with Java 1.1,
         * and so we use Utf8UrlEncoder instead of the deprecated method.
         */

        String data = "sampleId=" + sampleId + "&sampleHistoryId="
                + sampleHistoryId + "&name=" + Utf8UrlEncoder.encode(modName)
                + "&function=ortep" + "&x=" + ang[0] + "&y=" + ang[1] + "&z="
                + ang[2] + "&shiftx=0" + "&shifty=0" + "&scale=" + xfac
                + "&stereo=" + getStereoModeString() + "&quality=90"
                + "&hydro=" + String.valueOf(hydro.getState());
        runCGI(ortepScript, data);
    }

    /**
     * requests a ray traced image, corresponding to the current model and view
     * and conforming to the specified options, from the configured ray tracer
     * service
     * 
     * @param r the red component of the background color
     * @param g the green component of the background color
     * @param b the blue component of the background color
     * @param msize the dimension of the image to render
     * @param type a <code>String</code> indicating the type of view to render
     * @param bsize the size with which to render the balls in the model
     */
    protected final void doRayTrace(float r, float g, float b, String msize,
            String type, float bsize) {
        double[] ang = calcAngles();

        /*
         * NOTE: URLEncoder.encode(String) is deprecated in Java 1.4 and above
         * in favor of a new method, URLEncoder.encode(String, String),
         * introduced at that time. This class must be compatible with Java 1.1,
         * and so we use Utf8UrlEncoder instead of the deprecated method.
         */

        String data = "sampleId=" + sampleId + "&sampleHistoryId="
                + sampleHistoryId + "&name=" + Utf8UrlEncoder.encode(modName)
                + "&function=render" + "&size=" + msize + "&drawtype=" + type
                + "&x=" + ang[0] + "&y=" + ang[1] + "&z=" + ang[2] + "&r=" + r
                + "&g=" + g + "&b=" + b + "&distance=" + xfac + "&quality=90"
                + "&shiftx=0" + "&shifty=0" + "&stereo="
                + getStereoModeString() + "&bsize=" + bsize + "&hydro="
                + String.valueOf(hydro.getState());
        runCGI(imageScript, data);
    }

    /**
     * Returns the appropriate code for the current stereo mode for use with the
     * ray tracing service
     * 
     * @return a <code>String</code> containing the code for the current
     *         stereo mode
     */
    protected String getStereoModeString() {
        if (stereo.getState()) {
            return "lr";
        } else if (RG.getState()) {
            return "rg";
        } else {
            return "mono";
        }
    }

    /**
     * constructs the menu bar for this applet
     * 
     * @return the <code>MenuBar</code>
     */
    protected MenuBar buildMB() {
        MenuBar mb = new MenuBar();
        Menu m;
        MenuItem mi;

        /* The Output menu */
        m = new Menu("Output");
        m.add(imageItem = new MenuItem("Create Image..."));
        imageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (box == null) {
                    box = new TraceBox();
                }
                box.setVisible(true);
            }
        });
        if (imageScript == null) {
            imageItem.setEnabled(false);
        }
        m.add(ortepItem = new MenuItem("Create Ortep"));
        ortepItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                doOrtep();
            }
        });
        if (ortepScript == null) {
            ortepItem.setEnabled(false);
        }
        mb.add(m);

        /* The View menu */
        m = new Menu("View");
        m.add(mi = new MenuItem("Line Drawing"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                labels.setEnabled(true);
                space = false;
                balls = false;
                RG.setEnabled(true);
                repaint();
            }
        });
        m.add(mi = new MenuItem("Ball and Stick"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                balls = true;
                space = false;
                labels.setState(false);
                labels.setEnabled(false);
                RG.setState(false);
                RG.setEnabled(false);
                repaint();
            }
        });
        m.add(mi = new MenuItem("Space Filled"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                space = true;
                balls = true;
                RG.setState(false);
                RG.setEnabled(false);
                labels.setState(false);
                labels.setEnabled(false);
                repaint();
            }
        });
        m.addSeparator();
        m.add(stereo = new CheckboxMenuItem("Stereo"));
        stereo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    RG.setState(false);
                    newxfac /= 2.0;
                } else {
                    newxfac *= 2.0;
                }
                repaint();
            }
        });
        m.add(RG = new CheckboxMenuItem("RedGreen"));
        RG.setState(false);
        RG.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    if (stereo.getState()) {
                        stereo.setState(false);
                        newxfac *= 2.0;
                    }
                }
                repaint();
            }
        });
        m.addSeparator();
        m.add(mi = new MenuItem("Reset"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                amat.unit();
                repaint();
            }
        });
        mi = null;
        mb.add(m);

        /* The Display menu */
        m = new Menu("Display");
        angles = new CheckboxMenuItem("Angles");
        angles.setState(true);
        angles.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                repaint();
            }
        });
        m.add(angles);
        m.add(labels = new CheckboxMenuItem("Labels"));
        labels.setState(false);
        labels.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                repaint();
            }
        });
        m.add(hydro = new CheckboxMenuItem("Hydrogens"));
        hydro.setState(false);
        hydro.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                repaint();
            }
        });
        mb.add(m);

        /* The Mode menu */
        m = new Menu("Mode");
        m.add(drawmode = new CheckboxMenuItem("Rotate"));
        drawmode.setState(true);
        drawmode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    bondmode.setState(false);
                    anglemode.setState(false);
                    bclicknum = 0;
                    aclicknum = 0;
                    repaint();
                }
            }
        });
        m.add(bondmode = new CheckboxMenuItem("Bond Distance"));
        bondmode.setState(false);
        bondmode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    drawmode.setState(false);
                    anglemode.setState(false);
                    aclicknum = 0;
                    repaint();
                }
            }
        });
        m.add(anglemode = new CheckboxMenuItem("Angle"));
        anglemode.setState(false);
        anglemode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    drawmode.setState(false);
                    bondmode.setState(false);
                    bclicknum = 0;
                    repaint();
                }
            }
        });
        mb.add(m);
        m = null;

        return mb;
    }

    /**
     * Handles keyboard events for this applet so as to provide keyboard
     * controls
     * 
     * @param e a <code>KeyEvent</code> to handle
     */
    protected void processKeyEvent(KeyEvent e) {
        if (e.getKeyChar() == 'r') {
            amat.unit();
            md.transformed = false;
            md.transform();
            newxfac = xfac;
            repaint();
        }
        super.processKeyEvent(e);
    }

    /**
     * Handles mouse events that occur on this applet and do not involve cursor
     * movements (e.g. clicks), so as to implement the mouse controls
     * 
     * @param e a <code>MouseEvent</code> to handle
     */
    protected void processMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                if (drawmode.getState()) {
                    // do nothing
                } else if (bondmode.getState()) {
                    bclicknum++;
                    if (bclicknum == 1) {
                        bpoint1 = md.findPoint(e.getX(), e.getY());
                    } else if (bclicknum == 2) {
                        bpoint2 = md.findPoint(e.getX(), e.getY());
                    } else {
                        bpoint1 = bpoint2;
                        bpoint2 = md.findPoint(e.getX(), e.getY());
                    }
                    repaint();
                    if (bclicknum > 1) {
                        mess.ShowMessage("Bond Distance",
                                "The distance between "
                                        + md.Comp[bpoint1 / 3]
                                        + " and \n"
                                        + md.Comp[bpoint2 / 3]
                                        + " is "
                                        + (Math.round(md.findDistance(bpoint1,
                                                bpoint2) * 10000d) / 10000f)
                                        + " Angstroms.");
                    }
                } else if (anglemode.getState()) {
                    aclicknum++;
                    if (aclicknum == 1) {
                        apoint1 = md.findPoint(e.getX(), e.getY());
                    } else if (aclicknum == 2) {
                        apoint2 = md.findPoint(e.getX(), e.getY());
                    } else if (aclicknum == 3) {
                        apoint3 = md.findPoint(e.getX(), e.getY());
                    } else {
                        apoint1 = apoint2;
                        apoint2 = apoint3;
                        apoint3 = md.findPoint(e.getX(), e.getY());
                    }
                    repaint();
                    if (aclicknum > 2) {
                        double dist1 = md.findDistance(apoint2, apoint3);
                        double dist2 = md.findDistance(apoint1, apoint2);
                        double dist3 = md.findDistance(apoint1, apoint3);
                        double angle = (180d * Math.acos((((dist1 * dist1)
                                + (dist2 * dist2)) - (dist3 * dist3))
                                / (2 * dist1 * dist2))) / 3.14159265;
                        mess.ShowMessage("Bond Angle", "Angle from atom "
                                + md.Comp[apoint1 / 3] + ", atom "
                                + md.Comp[apoint2 / 3] + ", and \natom "
                                + md.Comp[apoint3 / 3] + " is: "
                                + (Math.round(angle * 1000d) / 1000f)
                                + " Degrees.");
                    }
                }
                break;
            case MouseEvent.MOUSE_PRESSED:
                mouseXDown = e.getX();
                mouseYDown = e.getY();
        }
        super.processMouseEvent(e);
    }

    /**
     * Handles mouse events that occur on this applet and do involve cursor
     * movements (e.g. drags), so as to implement the mouse controls
     * 
     * @param e a <code>MouseEvent</code> to handle
     */
    protected void processMouseMotionEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int m = e.getModifiers();
            if ((m & (InputEvent.ALT_MASK | InputEvent.META_MASK)) != 0) {
                newxfac = newxfac + ((mouseYDown - mouseY) / 2);
            } else if ((m & InputEvent.CTRL_MASK) != 0) {
                tmat.unit();
                float ztheta = (((mouseY - mouseYDown) * 360.0f) / getSize().width)
                        + (((mouseX - mouseXDown) * 360.0f) / getSize().height);
                tmat.zrot(ztheta);
                amat.mult(tmat);
            } else {
                tmat.unit();
                float xtheta = ((mouseYDown - mouseY) * 360.0f)
                        / getSize().width;
                float ytheta = ((mouseX - mouseXDown) * 360.0f)
                        / getSize().height;
                tmat.xrot(xtheta);
                tmat.yrot(ytheta);
                amat.mult(tmat);
            }
            repaint();
            mouseXDown = mouseX;
            mouseYDown = mouseY;
        }
        super.processMouseMotionEvent(e);
    }

    /**
     * Invokes the HTTP service at the location specified by <code>script</code>
     * relative to this applet's document base, with the query string specified
     * by <code>sdata</code>; the results are displayed in a new browser
     * window
     * 
     * @param script a <code>String</code> representation of the URL of the
     *        service to invoke
     * @param sdata a <code>String</code> containing the query parameters to
     *        pass to the service
     */
    protected void runCGI(String script, String sdata) {
        try {
            getAppletContext().showDocument(
                    new URL(getDocumentBase(), script + "?" + sdata), "_blank");
        } catch (MalformedURLException e) {
            mess.ShowMessage("Error", "Script URL mis- or un-configured:\n" + e);
        } finally {
            if (warningBox != null) {
                warningBox.setVisible(false);
            }
        }
    }

    /**
     * calculates the relative angles displayed at the bottom of Applet and
     * provided to external programs
     * 
     * @return a <code>double[]</code> containing the three orientation angles
     */
    double[] calcAngles() {
        double R = 180.0d / 3.14159265;
        double[] angles = new double[3];
        angles[0] = ((int) (R * (Math.atan2(amat.zy, amat.zz)) * 100d)) / 100d;
        double sinx = Math.sin(angles[0] / R);
        double cosy = 0d;
        if (sinx <= .70711) {
            cosy = amat.zz / Math.cos(angles[0] / R);
        } else {
            cosy = amat.zy / sinx;
        }
        angles[1] = ((int) (R * (Math.atan2(-amat.zx, cosy)) * 100d)) / 100d;
        angles[2] = ((int) (R * (Math.atan2(amat.yx, amat.xx)) * 100d)) / 100d;
        return angles;
    }

    /**
     * Loads an image via this applet's {@code ClassLoader}
     * 
     * @param name the name of the image to load, as a {@code String}
     * @return an {@code Image} representing the loaded image
     */
    private Image getImageResource(String name) {
        ClassLoader loader = this.getClass().getClassLoader();
        URL url = loader.getResource(name);
        
        if (url == null) {
            return null;
        } else {
            return getImage(url);
        }
    }

    /**
     * Draws the border around the applet
     * 
     * @param g the <code>Graphics</code> with which to draw the border
     */
    private void drawBorder(Graphics g) {
        Color light = new Color(224, 224, 224);
        Color dark = new Color(128, 128, 128);
        int h = getSize().height;
        int w = getSize().width;

        g.setColor(light);
        g.drawLine(0, h, 0, 0);
        g.drawLine(0, 0, w, 0);
        g.drawLine(w - 6, 5, w - 6, h - 6);
        g.drawLine(w - 6, h - 6, 5, h - 6);
        g.setColor(Color.white);
        g.drawLine(1, h - 1, 1, 1);
        g.drawLine(1, 1, w - 1, 1);
        g.drawLine(w - 5, 4, w - 5, h - 5);
        g.drawLine(w - 5, h - 5, 4, h - 5);
        g.setColor(dark);
        g.drawLine(4, h - 6, 4, 4);
        g.drawLine(4, 4, w - 6, 4);
        g.drawLine(w - 2, 2, w - 2, h - 2);
        g.drawLine(w - 2, h - 2, 2, h - 2);
        g.setColor(Color.black);
        g.drawLine(5, h - 7, 5, 5);
        g.drawLine(5, 5, w - 7, 5);
        g.drawLine(w - 1, 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, 1, h - 1);

    }

    /**
     * Draws the orientation angles with the specified graphics context
     * 
     * @param g the <code>Graphics</code> with which to draw the angles
     */
    private void showAngles(Graphics g) {
        g.setColor(Color.blue);
        double[] angles = calcAngles();
        g.drawString("X: " + angles[0], 10, getSize().height - 10);
        g.drawString("Y: " + angles[1], 110, getSize().height - 10);
        g.drawString("Z: " + angles[2], 210, getSize().height - 10);
    }

    /**
     * A <code>Dialog</code> subclass with an attached, modifiable
     * <code>TextArea</code> in which to display a message
     */
    class MessageBox extends Dialog {

        /** The <code>TextArea</code> in which to display the message */
        protected TextArea message;

        /**
         * Constructs a new <code>MessageBox</code> with the specified owner
         * <code>Frame</code>
         * 
         * @param owner the <code>Frame</code> that owns this
         *        <code>MessageBox</code>, or <code>null</code> if there is
         *        none
         */
        MessageBox(Frame owner) {
            super(owner, null, false);

            Button okButton;

            message = new TextArea(" ", 3, 25, TextArea.SCROLLBARS_NONE);
            message.setEditable(false);
            setLayout(new BorderLayout());
            add("Center", message);
            add("South", okButton = new Button("OK"));
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
            pack();
            setSize(300, 150);
        }

        /**
         * Set the message title and text of this message box and make it
         * visible
         * 
         * @param title a <code>String</code> containing the title for the
         *        message box
         * @param mess a <code>String</code> containing the message text
         */
        public void ShowMessage(String title, String mess) {
            setTitle(title);
            message.replaceRange(mess, 0, message.getText().length());
            setVisible(true);
        }

    }

    /**
     * A user interface to the ray tracing facility
     */
    class TraceBox extends Frame implements AdjustmentListener {

        /** a control for adjusting the ball size */
        private AWTRatioSliderField size;

        /** a control for adjusting the blue component of the background */
        private AWTSliderField blue;

        /** a control for adjusting the green component of the background */
        private AWTSliderField green;

        /** a control for adjusting the red component of the background */
        private AWTSliderField red;

        /** a panel in which to display the selected background color */
        private Panel box;

        /**
         * Constructs a new TraceBox
         */
        public TraceBox() {
            super("Ray Tracer Interface");
            Label lab;
            Panel p;
            final CheckboxGroup drawTypeGroup = new CheckboxGroup();
            final CheckboxGroup imageSizeGroup = new CheckboxGroup();

            setLayout(new BorderLayout(20, 20));

            /* color picker */
            Panel colorPicker = new Panel();
            colorPicker.setLayout(new BorderLayout());
            lab = new Label();
            lab.setFont(new Font("Helvetica", Font.BOLD, 20));
            lab.setText("Background Color");
            colorPicker.add("North", lab);
            p = new Panel();
            p.setLayout(new GridLayout(4, 1));
            p.add(red = new AWTSliderField(new Label("red")));
            p.add(green = new AWTSliderField(new Label("green")));
            p.add(blue = new AWTSliderField(new Label("blue")));
            red.addAdjustmentListener(this);
            red.setBackground(Color.red);
            green.addAdjustmentListener(this);
            green.setBackground(Color.green);
            blue.addAdjustmentListener(this);
            blue.setBackground(Color.blue);
            box = new Panel();
            box.setBackground(getColor());
            p.add(box);
            colorPicker.add("Center", p);

            /* sphere size slider */
            size = new AWTRatioSliderField(new Label("Sphere Size"));

            /* all sliders */
            p = new Panel();
            p.setLayout(new BorderLayout());
            p.add("Center", colorPicker);
            p.add("South", size);
            add("Center", p);

            /* buttons */
            Button b;
            p = new Panel();
            b = new Button("OK");
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    String drawType;
                    String imageSize;

                    setVisible(false);
                    synchronized (JaMM.this) {
                        if (warningBox == null) {
                            createWarningBox();
                        }
                    }
                    warningBox.setVisible(true);
                    switch (drawTypeGroup.getSelectedCheckbox().getLabel().charAt(
                            0)) {
                        case 'R': // "Rods"
                            drawType = "Line";
                            break;
                        case 'S': // "Space-filled"
                            drawType = "Space";
                            break;
                        default:
                            drawType = "Ball";
                    }
                    imageSize = imageSizeGroup.getSelectedCheckbox().getLabel().substring(
                            0, 3);
                    callRayTrace(drawType, imageSize);
                }
            });
            p.add(b);
            b = new Button("Cancel");
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    setVisible(false);
                }
            });
            p.add(b);
            b = null;
            add("South", p);

            /* multiple choice groups */
            Panel p2 = new Panel();
            p2.setLayout(new GridLayout(2, 1, 10, 10));

            Panel modelType = new Panel();
            modelType.setLayout(new BorderLayout());
            lab = new Label();
            lab.setFont(new Font("Helvetica", Font.BOLD, 20));
            lab.setText("Model Type");
            modelType.add("North", lab);
            p = new Panel();
            p.setLayout(new GridLayout(3, 1));
            p.add(new Checkbox("Ball and Stick", drawTypeGroup, true));
            p.add(new Checkbox("Space Filled", drawTypeGroup, false));
            p.add(new Checkbox("Rods", drawTypeGroup, false));
            modelType.add("Center", p);
            p2.add(modelType);
            modelType = null;

            Panel modelSize = new Panel();
            modelSize.setLayout(new BorderLayout());
            lab = new Label();
            lab.setFont(new Font("Helvetica", Font.BOLD, 20));
            lab.setText("Image Size");
            modelSize.add("North", lab);
            p = new Panel();
            p.setLayout(new GridLayout(4, 1));
            p.add(new Checkbox("100x100", imageSizeGroup, false));
            p.add(new Checkbox("300x300", imageSizeGroup, true));
            p.add(new Checkbox("500x500", imageSizeGroup, false));
            p.add(new Checkbox("700x700", imageSizeGroup, false));
            modelSize.add("Center", p);
            p2.add(modelSize);
            modelSize = null;

            /* finish up */
            add("East", p2);
            p2 = null;
            p = null;
            lab = null;
            pack();
            setSize(getPreferredSize());
        }

        /**
         * returns the color corresponding to the current values of the three
         * color component sliders
         * 
         * @return the <code>Color</code> indicated by the controls
         */
        protected Color getColor() {
            return new Color(red.getValue(), green.getValue(), blue.getValue());
        }

        /**
         * Invokes the ray tracer based on the current state of the controls and
         * the specified drawing type and image size
         * 
         * @param drawType a <code>String</code> indicating the drawing type
         *        requested
         * @param imageSize a <code>String</code> indicating the image size
         *        requested
         */
        protected void callRayTrace(String drawType, String imageSize) {
            doRayTrace(red.getValue() / 255.0f, green.getValue() / 255.0f,
                    blue.getValue() / 255.0f, imageSize, drawType,
                    size.getRatioValue());
        }

        /**
         * Creates this JaMM's warning box (but does not make it visible)
         */
        private void createWarningBox() {
            synchronized (JaMM.this) {
                Frame wb = new Frame();
                Label label = new Label();

                label.setFont(new Font("Helvetica", Font.BOLD, 25));
                label.setText("Ray tracing image, Please wait...");
                wb.setBackground(Color.red);
                wb.setForeground(Color.black);
                wb.setLayout(new BorderLayout());
                wb.add("Center", label);
                wb.pack();
                wb.setSize(420, 100);
                warningBox = wb;
            }
        }

        /**
         * Implementation method of the <code>AdjustmentListener</code>
         * interface; invoked when a component to which this object is listening
         * generates an adjustment event
         * 
         * @param e the <code>AdjustmentEvent</code> representing the
         *        adjustment that ocurred
         */
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (box != null) {
                box.setBackground(getColor());
            }
        }
    }
}
