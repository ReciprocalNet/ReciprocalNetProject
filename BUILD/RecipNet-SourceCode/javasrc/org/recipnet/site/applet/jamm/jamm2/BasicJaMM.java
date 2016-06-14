/*
 * Reciprocal Net Project
 *
 * @(#)BasicJaMM.java
 *
 * Copyright (c) 1998, 2000, 2003 The Trustees of Indiana University
 *
 * 19-Sep-2002: jobollin modified getData to handle URLs on other hosts
 * 28-Oct-2002: jobollin added support for the sampleId parameter
 * 20-Nov-2002: jobollin added an import for Rotate3DModel
 * 17-Dec-2002: jobollin added support for loading table.dat from the applet 
 *              jar as part of task #509
 * 18-Dec-2002: jobollin added code to install a custom KeyEventDispatcher
 *              when running in a JVM that supports them (part of task #445)
 * 09-Jan-2003: jobollin removed unused imports
 * 10-Jan-2003: jobollin reformatted the source according to code conventions,
 *              made all imports explicit, and did a bit of rearchitecting to
 *              remove some unnecessary instance variables
 * 13-Jan-2003: jobollin rewrote the Javadocs
 * 13-Jan-2003: jobollin modified initInstance to accept a TableContainer
 *              object instead of the two constituent arrays
 * 19-Feb-2003: jobollin changed the default element data file name from
 *              table.dat to element_data.txt as part of task #682
 * 25-Feb-2003: jobollin reformatted the source and revised the javadocs as
 *              part of task #749
 * 13-Mar-2003: jobollin removed impoort statement for InvokerThread as part
 *              of task #743
 * 11-Jul-2003: jrhanna added sampleHistoryId parameter
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.applet.jamm.TableContainer;
import org.recipnet.site.applet.jamm.WebFiles;

/**
 * The superclass for all Java 2 JaMM implementations
 *
 * @author John C. Bollinger
 * @version 1.0
 */
public class BasicJaMM
        extends JApplet {

    /** the name by wHich to obtain the model data from the host server */
    protected String modName = null;

    /** a <code>PrintWriter</code> to which user messages may be printed */
    protected PrintWriter outWriter;

    /** the <code>Rotate3DPanel</code> presenting the main view */
    protected Rotate3DPanel pan;

    /** the Reciprocal Net sample ID for the current model */
    protected long sampleId;
 
    /** The Reciprocal Net historyId of the current sample */
    protected long sampleHistoryId;  

    /**
     * provides brief information about this applet
     *
     * @return a brief applet identification string
     */
    public String getAppletInfo() {
        return new String(
            "BasicJaMM version 1.0\nby John C. Bollinger,\n"
                + "Copyright (c) 2002, Indiana University");
    }

    /**
     * initializes the modName instance variable and attempts to read the model
     *
     * @return a <code>Rotate3DModel</code> containing the model data, or
     *         <code>null</code> if the specified model name cannot be used to
     *         generate a valid URL
     */
    public Rotate3DModel getData() {
        modName = getParameter("model");
        if (modName == null) {
            modName = "model.obj";
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Rotate3DModel m =
                WebFiles.getData(new URL(getDocumentBase(), modName),
                    new PrintStream(baos));
            if (baos.size() > 0) {
                outWriter.write(baos.toString());
            }
            return m;
        } catch (MalformedURLException mue) {
            return null;
        }
    }

    /**
     * reads and returns the color / radius table
     *
     * @return the color / radius table as a <code>TableContainer</code>, or
     *         <code>null</code> if the specified table name is invalid or no
     *         table file is found
     */
    public TableContainer getTable() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String tabName = getParameter("table");
            URL tabURL =
                (tabName == null)
                ? getClass().getClassLoader().getResource("element_data.txt")
                : new URL(getDocumentBase(), tabName);
            TableContainer tc =
                ((tabURL == null) ? null
                : WebFiles.getTable(tabURL, new PrintStream(baos)));

            if (baos.size() > 0) {
                outWriter.write(baos.toString());
            }

            return tc;
        } catch (MalformedURLException mue) {
            return null;
        }
    }

    /**
     * public void init () The applet's initialization method.  The model,
     * tables, and UI are initialized in parallel by use of threads, then the
     * display component (a Rotate3DPanel) is constructed from the results and
     * attached to the UI.
     */
    public void init() {
        StringWriter outBuffer = new StringWriter();
        outWriter = new PrintWriter(outBuffer);
        Rotate3DModel mod;
        TableContainer tc;

        try {
            URLConnection.setDefaultAllowUserInteraction(true);

            /* load the model */
            InvokerThread modelThread =
                new InvokerThread(this, "getData", new Object[0]);
            modelThread.start();

            /* load the color/radius table */
            InvokerThread tableThread =
                new InvokerThread(this, "getTable", new Object[0]);
            tableThread.start();

            JPanel content = new JPanel(new BorderLayout());
            initInstance(content);

            String sampleIdStr = getParameter("sampleId");
            if (sampleIdStr != null) {
                try {
                    sampleId = Long.parseLong(sampleIdStr);
                } catch (NumberFormatException nfe) {
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
                    sampleHistoryId = -1L;
                }
            } else {
                sampleHistoryId = -1L;
            }

            /* wait for the other two threads' completion */
            try {
                modelThread.join();
                if (modelThread.threwException()) {
                    modelThread.getException().printStackTrace(outWriter);
                    mod = null;
                } else {
                    mod = (Rotate3DModel) modelThread.getResult();
                }
            } catch (InterruptedException e) {
                mod = null;
            }
            try {
                tableThread.join();
                if (tableThread.threwException()) {
                    tableThread.getException().printStackTrace(outWriter);
                    tc = null;
                } else {
                    tc = (TableContainer) tableThread.getResult();
                }
            } catch (InterruptedException e) {
                tc = null;
            }

            /* report any problems with loading model or table */
            if (mod == null) {
                outWriter.println("\nCould not load model.\n");
            }
            if (tc == null) {
                outWriter.println(
                    "\nCould not load atomic parameter table; using defaults.\n");
                tc = new TableContainer();
            }

            /* construct and initialize the display */
            pan = createMainPanel(mod, tc);
            String panelColor = getParameter("color");
            if (panelColor != null) {
                int i = Integer.decode(panelColor).intValue();
                pan.setBackground(new Color(i));
            }
            content.add(pan, BorderLayout.CENTER);
            setContentPane(content);

            /*
             * attempt to install a KeyEventDispatcher; this is only necessary
             * for Java 1.4 and above, and will fail more-or-less silently on
             * 1.2 and 1.3 series JVMs:
             */
            try {
                Class jkedClass = Class.forName(
                        "org.recipnet.site.applet.jamm.jamm2.JaMMKeyEventDispatcher");
                Class[] paramClasses =
                    new Class[] {
                        Class.forName("org.recipnet.site.applet.jamm.jamm2.BasicJaMM"),
                        Class.forName("org.recipnet.site.applet.jamm.jamm2.Rotate3DPanel")
                    };
                Object[] params = new Object[] {this, pan};

                /* no reference to this object need be retained here */
                jkedClass.getConstructor(paramClasses).newInstance(params);
            } catch (NoClassDefFoundError ncdfe) {
                /* This should be thrown by pre-1.4 JVMs, and can be ignored in
                 * that case.  If it were thrown by any other JVM then it would
                 * probably mean that keyboard controls would not work in JaMM.
                 * No special recovery action is required, but it is logged to
                 * System.err
                 */
                System.err.println("Could not load JaMMKeyEventDispatcher ("
                    + ncdfe.getMessage() + "); this must be a pre-1.4 JVM.");
            }
        } catch (Exception t) {
            t.printStackTrace(outWriter);
            outWriter.flush();
            getContentPane().add(new JScrollPane(
                    new JTextArea(t.toString() + ": " + t.getMessage() + "\n"
                        + outBuffer.toString()),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
            return;
        }
        String s = outBuffer.toString();
        if (s.length() > 0) {
            reportError("", s);
        }
    }

    /**
     * Performs the actions necessary to start the applet
     */
    public void start() {
        requestFocus();
        repaint();
    }

    /**
     * Constructs a <code>URL</code> with specified query parameters and
     * displays the corresponding resource in an appropriate context; uses
     * {@link #showDocument(java.net.URL) showDocument(java.net.URL)} to
     * display the resource
     *
     * @param script a <code>String</code> representing a URL; interpreted
     *        relative to this applet's document base if relative
     * @param sdata parameters to the resource in the form of a query string
     *        (less the initial '?')
     */
    protected void RunCGI(String script, String sdata) {
        try {
            showDocument(new URL(getDocumentBase(), script + "?" + sdata));
        } catch (MalformedURLException e) {
            reportError("Error", "Script URL mis- or un-configured.");
        }
    }

    /**
     * Creates, initializes, and returns the correct kind of
     * <code>Rotate3DPanel</code> for this applet; runs after the model and
     * parameter tables have been loaded (or have failed to load), and after
     * <code>initInstance</code>.
     *
     * @param mod the <code>Rotate3DModel</code> to present
     * @param tc a <code>TableContainer</code> encapsulating the color and
     *        radius tables with which to construct the
     *        <code>Rotate3DPanel</code>
     *
     * @return a suitable <code>Rotate3DPanel</code> instance configured for
     *         this applet
     */
    protected Rotate3DPanel createMainPanel(Rotate3DModel mod,
            TableContainer tc) {
        return new Rotate3DPanel(mod, tc.colorTab);
    }

    /**
     * Performs initialization tasks appropriate for this applet.  This method
     * is invoked from init() and runs in parallel with the model and
     * parameter table retrievals.  This version does nothing, but subclasses
     * may override it to perform initialization tasks.
     *
     * @param content not used in this version
     */
    protected void initInstance(JPanel content) {
    }

    /**
     * Outputs error messages to an appropriate resource.  This version writes
     * messages to System.out, but subclasses may override it to send messages
     * to other facilities (status window, popup box, <em>etc</em>.).
     *
     * @param type a <code>String</code> describing the error type
     * @param message a <code>String</code> containing the error message
     */
    protected void reportError(String type, String message) {
        System.out.println(type + ": " + message);
    }

    /**
     * Displays the resource specified by the provided URL in a suitable
     * context. This version simply wraps an invocation of
     * getAppletContext().showDocument(u).
     *
     * @param u a <code>URL</code> indicating the resource to show
     */
    protected void showDocument(URL u) {
        getAppletContext().showDocument(u);
    }
}
