/*
 * Reciprocal Net Project
 * 
 * JammSupport.java
 *
 * 13-Nov-2002: jobollin wrote first draft
 * 21-Nov-2002: ekoperda fixed bug #614 in doOrtep()
 * 22-Nov-2002: jobollin fixed minor Javadoc bug
 * 09-Jan-2003: jobollin removed unused imports
 * 19-Feb-2003: jobollin renamed WEB-INF/table.dat to WEB-INF/element_data.txt
 *              as part of task #682
 * 04-Mar-2003: adharurk handled exceptions coming from core; tweaked by 
 *              ekoperda
 * 06-Mar-2003: ekoperda reorganized exceptions in doOrtep()
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 20-Mar-2003: jobollin fixed bug #629 by catching FileFormatExceptions thrown
 *              from LinePostcriptMaker, OrtepInputMaker, and SceneMaker, and
 *              delivering an HTTP 500 error and logging the exception instead
 *              of throwing it; the *Maker methods were modified to
 *              throw FileFormatException instead of returning null values,
 *              and to catch all recognized RuntimeExceptions that might be
 *              internally thrown (in general they are wrapped in
 *              FileFormatExceptions and rethrown).
 * 20-Mar-2003: jobollin fixed bag #630 by providing more information in the
 *              error message
 * 21-Mar-2003: jobollin added additional logging for exceptional conditions
 *              (not all of which generate any other record)
 * 08-May-2003: adharurk fixed bug #889 in doLineDraw() and doOrtep(): added
 *              member variable tempDir
 * 30-May-2003: midurbin added support for versioning (using sampleHistoryId) 
 *              and changed calls to compensate for core/wrapper level changes.
 * 20-Jun-2003: ajooloor added exception handling to the calls to FileTracker
 *              methods
 * 11-Jul-2003: jrhanna fixed bug #969 in doOrtep()
 * 11-Jul-2003: jrhanna added an instance of AuthenticationController 
 *              in order to provide a userId from the current HttpSession
 * 28-Jul-2003: jrhanna added logging support
 * 01-Aug-2003: midurbin fixed bug #982 in doOrtep() and processRequest()
 * 12-Aug-2003: midurbin fixed bug #990 by separating piped processes and 
 *              adding watchdog timers to terminate those that are stalled
 * 22-Aug-2003: jobollin fixed bug #1027 by allowing JammSupport to accept
 *              INVALID_SAMPLE_HISTORY_ID as a sample history id; concommittant
 *              changes were applied to JSP pages providing JaMM.
 * 07-Jan-2004: ekoperda changed package references due to source tree 
 *              reorganization
 * 01-Jun-2004: cwestnea made changes throughout to use CoreConnector
 * 08-Aug-2004: cwestnea modified doOrtep() to use SampleWorkflowBL
 * 15-Aug-2005: midurbin added the "suppressNavigationLinks" attribute to the
 *              forwarded request
 * 16-Aug-2005: midurbin removed references to AuthenticationController
 * 21-Oct-2005: midurbin updated doOrtep() to accomodate specification changes
 *              to RepositoryManager.beginWritingDataFiles()
 * 05-Dec-2005: jobollin updated doRender() to accommodate changes to
 *              SceneMaker; updated processRequest(), doLinedraw(), doOrtep(),
 *              and doRender() to make use of the new CrtFile; made external
 *              processes launched by doLineDraw(), doOrtep(), and doRender() be
 *              launched directly instead of via the system shell; split up
 *              doOrtep() into four methods to both reduce method complexity and
 *              more easily accommodate addition of a new way for generating
 *              ORTEP input; accommodated API changes to the ImageParameters
 *              class; removed unused imports 
 */

package org.recipnet.site.content.servlet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.common.ProcessWrapper;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.OrtFile;
import org.recipnet.common.files.ScnFile;
import org.recipnet.common.files.SdtFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.CifParseException;
import org.recipnet.common.files.cif.CifParser;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.ResourceNotAccessibleException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.logevent.JammSupportLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.FileFormatException;
import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.ImageParameters;
import org.recipnet.site.wrapper.LinePostscriptMaker;
import org.recipnet.site.wrapper.OrtepInputMaker;
import org.recipnet.site.wrapper.RepositoryFileInputStream;
import org.recipnet.site.wrapper.SceneMaker;

/**
 * <p>
 * a servlet to provide server-side support for JaMM. This version specifically
 * supports JaMM's various image creation options.
 * </p><p>
 * {@code JammSupport} interacts with the Reciprocal Net core components via
 * RMI, and obtains the necessary parameters for doing so from the application
 * context in the same manner that the Reciprocal Net wrapper classes do.
 * {@code JammSupport} also uses the following initialization parameters from
 * its servlet configuration:
 * </p>
 * <dl>
 * <dt>childPath</dt>
 * <dd>the executable path to be provided to child processes</dd>
 * <dt>darterServer</dt>
 * <dd>The DNS name or IP address of a server that should be relied upon for
 * darter (ray-tracing) services</dd>
 * </dl>
 * <p>
 * {@code JammSupport} obtains and uses the {@link FileTracker} associated with
 * its servlet context; it will not function correctly if no file tracker is
 * available.
 * </p><p>
 * {@code JammSupport}'s operation is directed by the request parameters it
 * receives in either the query string or in the message body (or both). If
 * multiple parameters of the same name are provided then the first one will be
 * used. The parameters supported are
 * </p>
 * <ul>
 * <li>For all requests:
 * <dl>
 * <dt>function</dt>
 * <dd>identifies the support function requested. Valid values are
 * <dl>
 * <dt>linedraw</dt>
 * <dd>prepare a labelled line drawing</dd>
 * <dt>render</dt>
 * <dd>prepare a ray-traced image</dd>
 * <dt>ortep</dt>
 * <dd>prepare an ORTEP</dd>
 * </dl>
 * </dd>
 * <dt>sampleId</dt>
 * <dd>the Reciprocal Net sample id of the sample for which to perform the
 * requested support task</dd>
 * <dt>name</dt>
 * <dd>the URL of the CRT file defining the view of the sample for which to
 * perform the task; it should contain a valid repository ticket number </dd>
 * <dt>x</dt>
 * <dd>the angle of rotation about the x axis, in degrees, of the orientation
 * requested; default 0.0</dd>
 * <dt>y</dt>
 * <dd>the angle of rotation about the y axis, in degrees, of the orientation
 * requested; default 0.0</dd>
 * <dt>z</dt>
 * <dd>the angle of rotation about the z axis, in degrees, of the orientation
 * requested; default 0.0</dd>
 * <dt>quality</dt>
 * <dd>the quality factor for generating a JPEG file from a pixel map; valid
 * values are 0 - 100; useful values are probably about 50-95; the default is
 * 70.</dd>
 * </dl>
 * </li>
 * <li>For line drawing requests ({@code function} is "linedraw")
 * <dl>
 * <dt>hydro</dt>
 * <dd>a boolean parameters indicating whether or not to include hydrogen atoms
 * in the prepared image. Values "1", "yes", and "true" (case insensitive) are
 * affirmative, any other -- no value at all -- is negative. Support for this
 * parameter may be added to other drawing modes in a future version.</dd>
 * <dt>scale</dt>
 * <dd>a floating point scaling parameter for the image; should not be zero or
 * negative, but reasonable values will depend on the model; default 1.0</dd>
 * </dl>
 * </li>
 * <li>For rendering requests ({@code function} is "render")
 * <dl>
 * <dt>drawType</dt>
 * <dd>the first letter of this parameter indicates the type of ray-traced
 * image requested -- 'l' for "line" mode (rods only); 'b' for ball and stick
 * mode; and 's' for space-filling mode. There is no default, so if this
 * parameter is not provided for a rendering request then no image will be
 * generated.</dd>
 * <dt>r</dt>
 * <dd>the red component of the background color as a floating-point value
 * between zero and one; default 1.0</dd>
 * <dt>g</dt>
 * <dd>the green component of the background color as a floating-point value
 * between zero and one; default 1.0</dd>
 * <dt>b</dt>
 * <dd>the blue component of the background color as a floating-point value
 * between zero and one; default 1.0</dd>
 * <dt>bsize</dt>
 * <dd>a scale factor for the ball size; should be a floating-point number
 * greater than zero; default 1.0</dd>
 * <dt>rsize</dt>
 * <dd>a scale factor for the rod radius; should be a floating-point number
 * greater than zero; default 1.0</dd>
 * <dt>distance</dt>
 * <dd>This deceptively-named parameter specifies an overall model
 * scale factor, though its reciprocal may indeed be used to determine the
 * distance from the virtual camera to the center of the model.  Values greater
 * than some model-dependent threshhold will result in part or all of the model
 * being outside the field of view.  It should be a floating point number;
 * default 1.</dd>
 * <dt>size</dt>
 * <dd> the size, in pixels, of the image to create; should be an integer number
 * greater than zero. Very large values may be rejected by the darter server;
 * the exact cutoff depends on the darter server's configuration. Default 100
 * </dd>
 * </dl>
 * </li>
 * </ul>
 * <p>
 * These parameters are stored in an {@link ImageParameters} instance and
 * provided in that way to {@code JammSupport}'s own supporting classes.
 * </p>
 * 
 * @see FileTracker
 * @see ImageParameters
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class JammSupport extends HttpServlet {

    /**
     * the name / IP number of a Darter server that will render images for us
     */
    private String darterServer;

    /**
     * the ServletContext in which this servlet is running
     */
    private ServletContext context;

    /**
     * a reference to this application's FileTracker
     */
    private FileTracker fileTracker;

    /**
     * the environment provided to child processes
     */
    private String[] childEnv;

    /**
     * temporary directory used by pnm programs to create temporary files
     */
    private File tempDir;

    private long externalProcessTimeout;

    // Reference to core connector for this servlet
    private CoreConnector coreConnector;

    /**
     * initializes this servlet instance
     */
    @Override
    public void init() {
        ServletConfig config = getServletConfig();
        context = config.getServletContext();
        String childPath = config.getInitParameter("childPath");
        externalProcessTimeout = Long.parseLong(
                context.getInitParameter("externalProcessTimeout"));
        this.coreConnector = CoreConnector.extract(context);

        if (childPath == null) {
            try {
                SiteManagerRemote siteManager
                        = this.coreConnector.getSiteManager();
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.NO_CHILD_PATH));
            } catch (RemoteException re) {
                // only report error to core - logging failures are not
                // critical to JammSupport
                this.coreConnector.reportRemoteException(re);
            }
            childEnv = new String[0];
        } else {
            childEnv = new String[] { childPath };
        }
        String tempDirPath = context.getInitParameter("tempDir");
        tempDir = ((tempDirPath == null) || (tempDirPath.trim().length() == 0))
                ? (File) context.getAttribute("javax.servlet.context.tempdir")
                : new File(tempDirPath);
        darterServer = context.getInitParameter("darterServer");
        fileTracker = FileTracker.getFileTracker(context);
        if (fileTracker == null) {
            try {
                SiteManagerRemote siteManager
                        = this.coreConnector.getSiteManager();
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.NO_FILE_TRACKER));
            } catch (RemoteException re) {
                // only report error to core - logging failures are not
                // critical to JammSupport
                this.coreConnector.reportRemoteException(re);
            }
        }
    }

    /**
     * Processes HTTP GET requests. This version just delegates to the generic
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)} method.
     * 
     * @param  req an {@code HttpServletRequest} representing the client request
     * @param  resp an {@code HttpServletResponse} with which to return a
     *         response to the client
     *        
     * @throws IOException if an I/O error occurs
     * @throws ServletException if sample and/or its repository information
     *         could not be retrieved
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        processRequest(req, resp);
    }

    /**
     * Processes HTTP POST requests. This version just delegates to the generic
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)} method.
     * 
     * @param  req an {@code HttpServletRequest} representing the client request
     * @param  resp an {@code HttpServletResponse} with which to return a
     *         response to the client
     *         
     * @throws IOException if an I/O error occurs
     * @throws ServletException if sample and/or its repository information
     *         could not be retrieved
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        processRequest(req, resp);
    }

    /**
     * processes HTTP GET and POST requests. Passes the request off to an
     * appropriate method based on the request parameters, or returns an error
     * response if it is unable to do so.
     * 
     * @param  req an {@code HttpServletRequest} representing the client request
     * @param  resp an {@code HttpServletResponse} with which to return a
     *         response to the client
     *         
     * @throws IOException if an I/O error occurs
     * @throws ServletException if sample and/or its repository information
     *         could not be retrieved
     */
    private void processRequest(HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException, ServletException {
        
        // the parsed parameters and applicable defaults
        ImageParameters parameters = new ImageParameters(
                new URL(req.getRequestURL().toString()), req.getParameterMap());

        // get core objects
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();

        if (parameters.getFunction() == null) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.URL_ERROR, parameters.getCrtName(),
                    null, req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "No function specified");
        } else if (parameters.getSampleId() == SampleInfo.INVALID_SAMPLE_ID) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.URL_ERROR, parameters.getCrtName(),
                    parameters.getFunction(), req.getQueryString(),
                    SampleInfo.INVALID_SAMPLE_ID,
                    SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID));
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid or no sample id specified");
        } else {
            try {
                SampleManagerRemote sampleManager
                        = this.coreConnector.getSampleManager();
                RepositoryManagerRemote repositoryManager
                        = this.coreConnector.getRepositoryManager();
                SampleInfo sampleInfo = sampleManager.getSampleInfo(
                        parameters.getSampleId(),
                        parameters.getSampleHistoryId());
                LabInfo labInfo = siteManager.getLabInfo(sampleInfo.labId);
                CrtFile<? extends Atom> crt;
                int t = repositoryManager.beginReadingDataFile(
                        parameters.getSampleId(),
                        parameters.getSampleHistoryId(),
                        parameters.getCrtName(), parameters.getUserId());
                Reader crtReader;
                
                try {
                    crtReader = new InputStreamReader(
                            new RepositoryFileInputStream(coreConnector, t),
                            "UTF-8");
                } catch (IOException ioe) {
                    
                    // Exception while creating the Reader
                    try {
                        repositoryManager.abortDataFile(t);
                    } catch (RecipnetException re) {
                        // do nothing
                    } catch (RemoteException re) {
                        // do nothing
                    }
                    
                    throw ioe;
                }
                try {
                    crt = CrtFile.readFrom(crtReader);
                } catch (ParseException pe) {
                    siteManager.recordLogEvent(new JammSupportLogEvent(
                            JammSupportLogEvent.CRT_ERROR,
                            parameters.getCrtName(), parameters.getFunction(),
                            req.getQueryString(), parameters.getSampleId(),
                            parameters.getSampleHistoryId(), pe));
                    resp.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Could not generate image -- "
                                    + "likely cause: malformed .crt file");
                    return;
                } catch (IOException ioe) {
                    siteManager.recordLogEvent(new JammSupportLogEvent(
                            JammSupportLogEvent.CRT_ERROR,
                            parameters.getCrtName(), parameters.getFunction(),
                            req.getQueryString(), parameters.getSampleId(),
                            parameters.getSampleHistoryId(), ioe));
                    resp.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Could not generate image -- "
                                    + "likely cause: server malfunction");
                    return;
                } finally {
                    try {
                        crtReader.close();
                    } catch (IOException ioe) {
                        // do nothing
                    }
                }
                req.setAttribute("suppressNavigationLinks", Boolean.TRUE);
                req.setAttribute("labShortName", labInfo.shortName);
                req.setAttribute("sampleLocalLabId", sampleInfo.localLabId);
                if (parameters.getFunction().equals("linedraw")) {
                    // create and deliver a line drawing view
                    doLinedraw(req, resp, parameters, crt);
                } else if (parameters.getFunction().equals("render")) {
                    // create and deliver a rendered image view
                    doRender(req, resp, parameters, crt);
                } else if (parameters.getFunction().equals("ortep")) {
                    // create and deliver an ORTEP view
                    doOrtep(req, resp, parameters, crt);
                } else {
                    siteManager.recordLogEvent(new JammSupportLogEvent(
                            JammSupportLogEvent.URL_ERROR,
                            parameters.getCrtName(), parameters.getFunction(),
                            req.getQueryString(), parameters.getSampleId(),
                            SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID));
                    resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                            "Requested function not known");
                }
            } catch (UnsupportedEncodingException uee) {

                /*
                 * Only UTF-8 is used explicitly, and all Java runtimes are
                 * required to support it, so UnsupportedEncodingExceptions
                 * should not happen
                 */
                throw new UnexpectedExceptionException(uee);
            } catch (RemoteException ex) {
                this.coreConnector.reportRemoteException(ex);
                throw new ServletException(ex);
            } catch (RecipnetException re) {
                throw new ServletException(re);
            }
        }
    }

    /**
     * Handles a request for a line drawing
     * 
     * @param req the {@code HttpServletRequest} object representing the client
     *        request
     * @param resp the {@code HttpServletResponse} object with which to provide
     *        the response
     * @param parameters a {@code ImageParameters} object containing parsed
     *        request parameters and other data relevant to handling the request
     * @param crt a {@code CrtFile} containing the model to draw
     * 
     * @throws IOException if an I/O error occurs
     * @throws RemoteException if {@code SiteManager} cannot be contacted, or if
     *         communication with it fails mid-stream with this exception
     * @throws ServletException if forwarding the request via the specified
     *         {@code RequestDispatcher} fails with this exception
     */
    private void doLinedraw(HttpServletRequest req, HttpServletResponse resp,
            ImageParameters parameters, CrtFile<? extends Atom> crt)
            throws IOException, RemoteException, ServletException {
        StringBuilder postScript;
        StringBuilder bboxBuffer = new StringBuilder();
        RequestDispatcher rd;
        File epsFile;
        File jpegFile;
        BufferedReader reader;
        Writer writer;
        ProcessWrapper cjpegProcess;
        ProcessWrapper pnmmarginProcess;
        ProcessWrapper pnmcropProcess;
        ProcessWrapper pnmdepthProcess;
        ProcessWrapper gsProcess;
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();

        rd = context.getRequestDispatcher("/jammline.jsp");
        if (rd == null) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.REQUEST_DISPATCHER_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Cannot forward to jammline");
            return;
        }

        postScript = new StringBuilder(LinePostscriptMaker.makeLinePostscript(
                parameters, crt.getModel()));

        // Let ghostscript calculate the bounding box
        gsProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "gs", "-sDEVICE=bbox", "-r100", "-q", "-dBATCH",
                        "-dNOPAUSE", "-dSAFER", "-" }, childEnv), false, true,
                null, false, null);
        gsProcess.setWatchdogTimer(externalProcessTimeout);

        writer = new OutputStreamWriter(new BufferedOutputStream(
                gsProcess.getOutputStream()));
        try {
            writer.append(postScript);
        } finally {
            writer.close();
        }

        // the desired output arrives from GhostScript's stderr stream
        reader = new BufferedReader(new InputStreamReader(
                gsProcess.getErrorStream()));
        try {
            for (String line = reader.readLine(); line != null;
                    line = reader.readLine()) {
                bboxBuffer.append(line).append('\n');
            }
        } finally {
            reader.close();
        }

        try {
            int result = gsProcess.waitFor(externalProcessTimeout);

            if (result == 0) {
                // GhostScript completed successfully; insert the result
                postScript.insert(LinePostscriptMaker.BB_OFFSET, bboxBuffer);
            } else {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.GS_CALCULATION,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), result));
            }
        } catch (InterruptedException ie) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    (gsProcess.wasAborted() ? JammSupportLogEvent.GS_TIMEOUT
                            : JammSupportLogEvent.GS_INTERUPTED),
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ie));
        }

        /*
         * If GhostScript didn't succeed at calculating the bounding box then
         * proceed without one. The result will still be valid PostScript, but
         * not valid Encapsulated PostScript.
         */

        // Write the EPS file to the file system
        epsFile = fileTracker.createTempFile("lines", ".eps");
        writer = new BufferedWriter(new FileWriter(epsFile));
        try {
            writer.append(postScript);
        } finally {
            writer.close();
        }

        // Create the JPEG file on the filesystem
        jpegFile = fileTracker.createTempFile("lines", ".jpeg");
        FileOutputStream fos = new FileOutputStream(jpegFile);

        /*
         * Provide temporary directory specified by configuration file or
         * servlet container to pnm utility programs so that they can create and
         * modify temporary files as required in that temporary directory
         */

        cjpegProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "cjpeg", "-quality",
                        String.valueOf((int) parameters.getQualityFactor()) },
                childEnv, tempDir), false, true, fos, true, System.err);
        cjpegProcess.setWatchdogTimer(externalProcessTimeout);

        pnmmarginProcess = new ProcessWrapper(
                Runtime.getRuntime().exec(
                        new String[] { "pnmmargin", "-white", "20" }, childEnv,
                        tempDir), false, true, cjpegProcess.getOutputStream(),
                true, System.err);
        pnmmarginProcess.setWatchdogTimer(externalProcessTimeout);

        pnmcropProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "pnmcrop" }, childEnv, tempDir), false, true,
                pnmmarginProcess.getOutputStream(), true, System.err);
        pnmcropProcess.setWatchdogTimer(externalProcessTimeout);

        pnmdepthProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "pnmdepth", "255" }, childEnv, tempDir), false,
                true, pnmcropProcess.getOutputStream(), true, System.err);
        pnmdepthProcess.setWatchdogTimer(externalProcessTimeout);

        gsProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "gs", "-sDEVICE=pgmraw", "-r100", "-q",
                        "-dBATCH", "-dNOPAUSE", "-dSAFER", "-sOutputFile=-",
                        epsFile.getAbsolutePath() }, childEnv, tempDir), true,
                true, pnmdepthProcess.getOutputStream(), true, System.err);
        gsProcess.setWatchdogTimer(externalProcessTimeout);

        try {
            int result = gsProcess.waitFor(externalProcessTimeout);
            pnmdepthProcess.waitFor(externalProcessTimeout);
            pnmcropProcess.waitFor(externalProcessTimeout);
            pnmmarginProcess.waitFor(externalProcessTimeout);
            cjpegProcess.waitFor(externalProcessTimeout);

            if (result != 0) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.GS_RETURN_CODE,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), result));
            }
        } catch (InterruptedException ie) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    (gsProcess.wasAborted() ? JammSupportLogEvent.GS_TIMEOUT
                            : JammSupportLogEvent.GS_INTERUPTED),
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ie));
        }

        /*
         * proceed whether or not GhostScript completed successfully. If it
         * didn't then the user can at least get the EPS file.
         */
        siteManager.recordLogEvent(new JammSupportLogEvent(
                parameters.getCrtName(), parameters.getFunction(),
                req.getQueryString(), parameters.getSampleId(),
                parameters.getSampleHistoryId()));
        req.setAttribute("epsKey", new Long(fileTracker.trackFile(epsFile,
                "application/postscript", true)));
        req.setAttribute("jpegKey", new Long(fileTracker.trackFile(jpegFile,
                "image/jpeg", true)));
        rd.forward(req, resp);
    }

    /**
     * Handles a request for an ORTEP diagram. Chooses the most appropriate
     * source of atomic parameters (preferring an SDT for backward
     * compatability, but using a CIF if no SDT is available), loads custom
     * ORTEP instructions if available in the repository, and delegates to the
     * appropriate one of
     * {@link #doOrtepFromSdt(HttpServletRequest, HttpServletResponse,
     * RequestDispatcher, ImageParameters, OrtFile, CrtFile, String)}
     * and {@link #doOrtepFromCif(HttpServletRequest, HttpServletResponse,
     * RequestDispatcher, ImageParameters, OrtFile, CrtFile, String)}.
     * 
     * @param req the {@code HttpServletRequest} object representing the client
     *        request
     * @param resp the {@code HttpServletResponse} object with which to provide
     *        the response
     * @param parameters a {@code ImageParameters} object containing parsed
     *        request parameters and other data relevant to handling the request
     * @param crt a {@code CrtFile} containing the model to draw
     * 
     * @throws IOException if an I/O error occurs
     * @throws RecipnetException if RepositoryManager throws it during
     *         repository access
     * @throws ServletException if jammortep.jsp throws this exception while
     *         processing a forwarded request
     * @throws RemoteException if could not connect to SiteManager
     */
    private void doOrtep(HttpServletRequest req, HttpServletResponse resp,
            ImageParameters parameters, CrtFile<? extends Atom> crt)
            throws RecipnetException, IOException, RemoteException,
            ServletException {
        RequestDispatcher rd = context.getRequestDispatcher("/jammortep.jsp");
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        RepositoryManagerRemote repositoryManager
                = this.coreConnector.getRepositoryManager();
        String modelName = (parameters.getCrtName().endsWith(".crt")
                ? parameters.getCrtName().substring(
                        0, parameters.getCrtName().length() - 4)
                : parameters.getCrtName());
        String ortName = modelName + ".ort";
        String sdtName = modelName + ".sdt";
        OrtFile ort;
        RepositoryFiles files;

        if (rd == null) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.REQUEST_DISPATCHER_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Cannot forward to jammortep");
            return;
        }

        files = repositoryManager.getRepositoryFiles(parameters.getSampleId(),
                parameters.getSampleHistoryId(), false);

        // Read ORT file from the repository, if available
        if (files.getRecordWithName(ortName) != null) {
            try {
                final int t = repositoryManager.beginReadingDataFile(
                        parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ortName,
                        parameters.getUserId());
                Reader ortReader = new InputStreamReader(
                        new RepositoryFileInputStream(coreConnector, t),
                        "UTF-8");

                try {
                    ort = OrtFile.readFrom(ortReader);
                } finally {
                    ortReader.close();
                }
            } catch (ParseException pe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_STUB_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), pe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP -- ORT file malformed");
                return;
            } catch (ResourceNotAccessibleException rnae) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_STUB_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), rnae));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP -- ORT file not accessible");
                return;
            } catch (IOException ioe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_STUB_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ioe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP -- I/O error while reading "
                                + "stub");
                return;
            } catch (ResourceNotFoundException ex) {

                /*
                 * This should not happen because the RepositoryFiles object
                 * contained a record for the requested file
                 */
                throw new UnexpectedExceptionException(ex);
            }
        } else {

            // No ORT file available; work without one
            ort = null;
        }

        if (files.getRecordWithName(sdtName) != null) {
            doOrtepFromSdt(req, resp, rd, parameters, ort, crt, sdtName);
        } else {
            String cifName = findCif(
                    (String) req.getAttribute("sampleLocalLabId"), modelName,
                    files);

            if (cifName != null) {
                doOrtepFromCif(req, resp, rd, parameters, ort, crt, cifName);
            } else {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_NO_ATOMIC_PARAMETERS,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId()));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP -- No suitable atomic "
                                + "parameter file is available");
            }
        }
    }

    /**
     * A helper method for
     * {@link #doOrtep(HttpServletRequest, HttpServletResponse, ImageParameters,
     * CrtFile)} that handles the details specific to producing an ORTEP image
     * based on an SDT, orientation, and optional pre-built ORTEP instructions
     * (<i>i.e.</i> the classic way). The main job of this method is to get the
     * appropriate ORTEP-III input file into a temporary file; the
     * {@link #generateOrtepImage(HttpServletRequest, HttpServletResponse,
     * RequestDispatcher, ImageParameters, File)} method takes it from there.
     * 
     * @param req an {@code HttpServletRequest} representing the request in
     *        response to which the ORTEP diagram is being prepared
     * @param resp the {@code HttpServletResponse} with which the response
     *        should be delivered
     * @param rd a {@code RequestDispatcher} configured to dispatch the request
     *        to the appropriate JSP for display once the image is ready
     * @param parameters an {@code ImageParameters} containing details of the
     *        requested image
     * @param ort an {@code OrtFile} containing the pre-built ORTEP instructions
     *        for the requested model, or {@code null} if there are none
     * @param crt a {@code CrtFile} representing the model to be drawn; atoms in
     *        this file's model should have appropriate site codes if they refer
     *        to different symmetry sites than the ones listed in the CIF
     * @param sdtName the expected name of the SDT file to render
     * 
     * @throws RecipnetException if RepositoryManager throws it during
     *         repository access
     * @throws IOException if an I/O error occurs
     * @throws RemoteException if SiteManager or RepositoryManager cannot be
     *         contacted, or if communication with them fails with this
     *         exception within the scope of this method
     * @throws ServletException if {@code generateOrtepImage()} throws this
     *         exception
     */
    private void doOrtepFromSdt(HttpServletRequest req,
            HttpServletResponse resp, RequestDispatcher rd,
            ImageParameters parameters, OrtFile ort,
            CrtFile<? extends Atom> crt, String sdtName)
            throws RecipnetException, IOException, RemoteException,
            ServletException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        RepositoryManagerRemote repositoryManager
                = this.coreConnector.getRepositoryManager();
        OrtepInputMaker inputMaker;
        SdtFile<FractionalAtom> sdt;
        File ortinFile;
        Writer ortinWriter;

        try {
            int t = repositoryManager.beginReadingDataFile(
                    parameters.getSampleId(), parameters.getSampleHistoryId(),
                    sdtName, parameters.getUserId());
            Reader sdtReader = new InputStreamReader(
                    new RepositoryFileInputStream(coreConnector, t), "UTF-8");

            try {
                sdt = SdtFile.readFrom(sdtReader);
            } catch (ParseException pe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_INPUT_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), pe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP input file -- "
                                + "likely cause: malformed .sdt file");
                return;
            } catch (IOException ioe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_INPUT_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ioe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP input file -- "
                                + "likely cause: server malfunction");
                return;
            } finally {
                try {
                    sdtReader.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        } catch (ResourceException ex) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.ORTEP_SDT_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ex));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not generate ORTEP -- SDT file not available");
            return;
        }

        inputMaker = OrtepInputMaker.forSDT(sdt);
        if (ort == null) {
            ort = inputMaker.createOrtepInstructions(crt.getModel());
        }
        ortinFile = fileTracker.createTempFile("ort", ".ort");
        ortinWriter = new BufferedWriter(new FileWriter(ortinFile));
        try {
            inputMaker.appendOrtepInput(ortinWriter, ort, parameters);
        } finally {
            ortinWriter.close();
        }

        generateOrtepImage(req, resp, rd, parameters, ortinFile);
    }

    /**
     * Attempts to find among the available repository file names the one that
     * is most likely to correspond to the CIF file for a particular sample or
     * model. File content is not examined, only file names.
     * 
     * @param localLabId the local lab ID {@code String} for the sample in
     *        question; the first choice for a CIF name is this string with
     *        ".cif" appended
     * @param modelName the base name of a file identifying a particular
     *        molecular model for this sample (e.g. a CRT file name less its
     *        ".crt" extension); the second choice for a CIF name is this string
     *        with ".cif" appended
     * @param allFiles a {@code RepositoryFiles} object describing all the files
     *        available for the sample and version of interest, used to test for
     *        the existence of the first two file name options, and to search
     *        for an alternative if neither of the first two is present
     * @return the CIF name as a {@code String} if a likely one is found,
     *         otherwise {@code null}
     */
    private String findCif(String localLabId, String modelName,
            RepositoryFiles allFiles) {
        String trialName;

        // try <localLabId>.cif first
        trialName = localLabId + ".cif";
        if (allFiles.getRecordWithName(trialName) != null) {
            return trialName;
        }

        // try <modelName>.cif next
        trialName = modelName + ".cif";
        if (allFiles.getRecordWithName(trialName) != null) {
            return trialName;
        }

        // Look for any file name ending with ".cif", ignoring case
        for (RepositoryFiles.Record record : allFiles.getRecords()) {
            if (record.getName().toLowerCase().endsWith(".cif")) {
                return record.getName();
            }
        }

        return null;
    }

    /**
     * A helper method for
     * {@link #doOrtep(HttpServletRequest, HttpServletResponse, ImageParameters,
     * CrtFile)} that handles the details specific to producing an ORTEP image
     * based on a CIF, CRT, orientation, and optional pre-built ORTEP
     * instructions; this is the new, preferred way of doing it. The main job of
     * this method is to get the appropriate ORTEP-III input file into a
     * temporary file; the {@link #generateOrtepImage(HttpServletRequest,
     * HttpServletResponse, RequestDispatcher, ImageParameters, File)} method
     * takes it from there.
     * 
     * @param req an {@code HttpServletRequest} representing the request in
     *        response to which the ORTEP diagram is being prepared
     * @param resp the {@code HttpServletResponse} with which the response
     *        should be delivered
     * @param rd a {@code RequestDispatcher} configured to dispatch the request
     *        to the appropriate JSP for display once the image is ready
     * @param parameters an {@code ImageParameters} containing details of the
     *        requested image
     * @param ort an {@code OrtFile} containing the pre-built ORTEP instructions
     *        for the requested model, or {@code null} if there are none
     * @param crt a {@code CrtFile} representing the model to be drawn; atoms in
     *        this file's model should have appropriate site codes if they refer
     *        to different symmetry sites than the ones listed in the CIF
     * @param cifName the expected name of the CIF file from which atomic
     *        parameters should be drawn
     * @throws RecipnetException if RepositoryManager throws it during
     *         repository access
     * @throws IOException if an I/O error occurs
     * @throws RemoteException if SiteManager or RepositoryManager cannot be
     *         contacted, or if communication with them fails within the scope
     *         of this method
     * @throws ServletException if {@code generateOrtepImage()} throws this
     *         exception
     */
    private void doOrtepFromCif(HttpServletRequest req,
            HttpServletResponse resp, RequestDispatcher rd,
            ImageParameters parameters, OrtFile ort,
            CrtFile<? extends Atom> crt, String cifName)
            throws RecipnetException, IOException, RemoteException,
            ServletException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        RepositoryManagerRemote repositoryManager
                = this.coreConnector.getRepositoryManager();
        OrtepInputMaker inputMaker;
        CifParser parser = new CifParser();
        CifFile cif;
        Iterator<DataBlock> blocks;
        File ortinFile;
        Writer ortinWriter;

        try {
            int t = repositoryManager.beginReadingDataFile(
                    parameters.getSampleId(), parameters.getSampleHistoryId(),
                    cifName, parameters.getUserId());
            InputStream cifStream = new RepositoryFileInputStream(
                    coreConnector, t);

            try {
                cif = parser.parseCif(cifStream);
            } catch (CifParseException cpe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_INPUT_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), cpe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP input file -- "
                                + "likely cause: malformed .cif file");
                return;
            } catch (IOException ioe) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_INPUT_ERROR,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ioe));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP input file -- "
                                + "likely cause: server malfunction");
                return;
            } finally {
                try {
                    cifStream.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        } catch (ResourceException ex) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.ORTEP_CIF_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ex));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not generate ORTEP -- CIF file not available");
            return;
        }

        blocks = cif.blockIterator();
        if (!blocks.hasNext()) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.ORTEP_EMPTY_CIF,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not generate ORTEP -- "
                            + "CIF file contained no data blocks");
            return;
        }
        try {
            inputMaker = OrtepInputMaker.forCIF(blocks.next());
        } catch (IllegalArgumentException iae) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.ORTEP_INADEQUATE_CIF,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not generate ORTEP -- "
                            + "CIF file contains insufficient information in "
                            + "its first data block");
            return;
        }
        if (ort == null) {
            ort = inputMaker.createOrtepInstructions(crt.getModel());
        }
        ortinFile = fileTracker.createTempFile("ort", ".ort");
        ortinWriter = new BufferedWriter(new FileWriter(ortinFile));
        try {
            inputMaker.appendOrtepInput(ortinWriter, ort, parameters);
        } finally {
            ortinWriter.close();
        }

        generateOrtepImage(req, resp, rd, parameters, ortinFile);
    }

    /**
     * Runs the application's configured ORTEP-III engine on the specified input
     * to create an EPS file, generates a JPEG rendition of the EPS image,
     * tracks both images via FileTracker, adds them and the input file as
     * request attributes with well-known names, and forwards the request via
     * the specified RequestDispatcher for display.
     * 
     * @param req an {@code HttpServletRequest} representing the request in
     *        response to which the ORTEP diagram is being prepared
     * @param resp the {@code HttpServletResponse} with which the response
     *        should be delivered
     * @param rd a {@code RequestDispatcher} configured to dispatch the request
     *        to the appropriate JSP for display once the image is ready
     * @param parameters an {@code ImageParameters} containing details of the
     *        requested image
     * @param ortinFile a {@code File} referencing a temporary ORTEP-III input
     *        file that ORTEP should be directed to render; this file will be
     *        handed off to FileTracker to track and eventually delete
     *        
     * @throws IOException if an I/O error occurs
     * @throws RemoteException if SiteManager cannot be contacted, or if
     *         communication with it fails within the scope of this method
     * @throws ServletException if forwarding the request via the specified
     *         {@code RequestDispatcher} fails with this exception
     */
    private void generateOrtepImage(HttpServletRequest req,
            HttpServletResponse resp, RequestDispatcher rd,
            ImageParameters parameters, File ortinFile) throws IOException,
            RemoteException, ServletException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        File epsFile = fileTracker.createTempFile("ort", ".eps");
        FileOutputStream fos = new FileOutputStream(epsFile);
        ProcessWrapper ortProcess;
        ProcessWrapper cjpegProcess;
        ProcessWrapper pnmmarginProcess;
        ProcessWrapper pnmcropProcess;
        ProcessWrapper pnmscaleProcess;
        ProcessWrapper gsProcess;
        File jpegFile;

        ortProcess = new ProcessWrapper(
                Runtime.getRuntime().exec(
                        new String[] { "recipnet-ortep3",
                                ortinFile.getAbsolutePath() }, childEnv), true,
                true, fos, true, System.err);
        try {
            int result = ortProcess.waitFor(externalProcessTimeout);
            if (result != 0) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_FAIL_CODE,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), result));
                resp.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate an ORTEP image -- "
                                + "ORTEP failed with code "
                                + result
                                + ".\nThe most likely cause is a malformed CIF"
                                + " or SDT."
                                );
                return;
            }
        } catch (InterruptedException ie) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.ORTEP_EXCEPTION,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ie));
            if (ortProcess.wasAborted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP image -- process timed out");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate ORTEP image -- ORTEP was"
                                + " interrupted prior to completion");
            }
            return;
        }

        jpegFile = fileTracker.createTempFile("ort", ".jpeg");
        fos = new FileOutputStream(jpegFile);

        /*
         * Provide temporary directory specified by configuration file or
         * servlet container to pnm utility programs so that they can create and
         * modify temporary files as required in that temporary directory
         */

        cjpegProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "cjpeg", "-quality",
                        String.valueOf((int) parameters.getQualityFactor()) },
                childEnv, tempDir), false, true, fos, true, System.err);
        cjpegProcess.setWatchdogTimer(externalProcessTimeout);

        pnmmarginProcess = new ProcessWrapper(
                Runtime.getRuntime().exec(
                        new String[] { "pnmmargin", "-white", "9" }, childEnv,
                        tempDir), false, true, cjpegProcess.getOutputStream(),
                true, System.err);
        pnmmarginProcess.setWatchdogTimer(externalProcessTimeout);

        pnmscaleProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "pnmscale", "0.50" }, childEnv, tempDir), false,
                true, pnmmarginProcess.getOutputStream(), true, System.err);
        pnmscaleProcess.setWatchdogTimer(externalProcessTimeout);

        pnmcropProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "pnmcrop" }, childEnv, tempDir), false, true,
                pnmscaleProcess.getOutputStream(), true, System.err);
        pnmcropProcess.setWatchdogTimer(externalProcessTimeout);

        gsProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "gs", "-sDEVICE=pgmraw", "-r100", "-q",
                        "-dBATCH", "-dNOPAUSE", "-dSAFER", "-sOutputFile=-",
                        epsFile.getAbsolutePath() }, childEnv, tempDir), false,
                true, pnmcropProcess.getOutputStream(), true, System.err);
        gsProcess.setWatchdogTimer(externalProcessTimeout);

        try {
            int result = gsProcess.waitFor(externalProcessTimeout);
            pnmcropProcess.waitFor(externalProcessTimeout);
            pnmscaleProcess.waitFor(externalProcessTimeout);
            pnmmarginProcess.waitFor(externalProcessTimeout);
            cjpegProcess.waitFor(externalProcessTimeout);

            if (result != 0) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.ORTEP_FAIL_CODE,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), result));
            }
            // Continue regardless of the return code
        } catch (InterruptedException ie) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    (gsProcess.wasAborted() ? JammSupportLogEvent.GS_ORT_TIMEOUT
                            : JammSupportLogEvent.GS_ORT_INTERUPTED),
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ie));
            // otherwise ignore the problem. The image will not be available.
        }
        siteManager.recordLogEvent(new JammSupportLogEvent(
                parameters.getCrtName(), parameters.getFunction(),
                req.getQueryString(), parameters.getSampleId(),
                parameters.getSampleHistoryId()));

        req.setAttribute("ortinKey", new Long(fileTracker.trackFile(ortinFile,
                "text/plain", true)));
        req.setAttribute("epsKey", new Long(fileTracker.trackFile(epsFile,
                "application/postscript", true)));
        req.setAttribute("jpegKey", new Long(fileTracker.trackFile(jpegFile,
                "image/jpeg", true)));
        rd.forward(req, resp);
    }

    /**
     * handles a request for a rendered image
     * 
     * @param req the {@code HttpServletRequest} object representing the client
     *        request
     * @param resp the {@code HttpServletResponse} object with which to provide
     *        the response
     * @param parameters an {@code ImageParameters} object containing parsed
     *        request parameters and other data relevant to handling the request
     * @param crt a {@code CrtFile} containing the model to be rendered
     * @throws IOException if an I/O error occurs
     * @throws RemoteException if {@code SiteManager} cannot be contacted, or if
     *         communication with it fails mid-stream with this exception
     * @throws ServletException if forwarding the request via the specified
     *         {@code RequestDispatcher} fails with this exception
     */
    private void doRender(HttpServletRequest req, HttpServletResponse resp,
            ImageParameters parameters, CrtFile<? extends Atom> crt)
            throws IOException, RemoteException, ServletException {
        SiteManagerRemote siteManager = this.coreConnector.getSiteManager();
        RequestDispatcher rd;
        Reader tableReader;
        ScnFile scene;
        Writer out;
        File jpegFile;
        ProcessWrapper darterProcess;
        ProcessWrapper vort2ppmProcess;
        ProcessWrapper cjpegProcess;

        rd = context.getRequestDispatcher("/jammrender.jsp");
        if (rd == null) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.REQUEST_DISPATCHER_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId()));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Cannot forward to jammrender");
            return;
        }

        tableReader = new BufferedReader(new InputStreamReader(
                context.getResourceAsStream("/WEB-INF/element_data.txt")));
        try {
            scene = SceneMaker.makeScene(parameters, tableReader,
                    crt.getModel());
        } catch (FileFormatException ffe) {
            siteManager.recordLogEvent(new JammSupportLogEvent(
                    JammSupportLogEvent.SCENE_MAKER_ERROR,
                    parameters.getCrtName(), parameters.getFunction(),
                    req.getQueryString(), parameters.getSampleId(),
                    parameters.getSampleHistoryId(), ffe));
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not generate scene description -- "
                            + "likely cause: malformed CRT file");
            return;
        } finally {
            tableReader.close();
        }

        jpegFile = fileTracker.createTempFile("image", ".jpeg");
        FileOutputStream fos = new FileOutputStream(jpegFile);

        cjpegProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "cjpeg", "-quality",
                        String.valueOf((int) parameters.getQualityFactor()) },
                childEnv), false, true, fos, true, System.err);
        cjpegProcess.setWatchdogTimer(externalProcessTimeout);

        vort2ppmProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                new String[] { "recipnet-vort2ppm" }, childEnv), false, true,
                cjpegProcess.getOutputStream(), true, System.err);
        vort2ppmProcess.setWatchdogTimer(externalProcessTimeout);

        darterProcess = new ProcessWrapper(
                Runtime.getRuntime().exec(
                        new String[] { "recipnet-renderclient",
                                String.valueOf(parameters.getImageSize()),
                                String.valueOf(parameters.getImageSize()),
                                darterServer }, childEnv), false, true,
                vort2ppmProcess.getOutputStream(), true, System.err);
        darterProcess.setWatchdogTimer(externalProcessTimeout);

        /*
         * Platform default character encoding used here because it is the most
         * likely encoding to be expected by the external processes:
         */
        out = new OutputStreamWriter(new BufferedOutputStream(
                darterProcess.getOutputStream()));
        try {
            try {
                scene.writeTo(out);
            } finally {
                out.close();
            }

            int result = darterProcess.waitFor(externalProcessTimeout);
            vort2ppmProcess.waitFor(externalProcessTimeout);
            cjpegProcess.waitFor(externalProcessTimeout);
            if (result != 0) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.RENDER_EXIT_CODE,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), result));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate image -- "
                                + "recipnet-renderclient failed with code "
                                + result);
                return;
            }
        } catch (Exception ex) {
            if (darterProcess.wasAborted()) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.RENDER_TIMEOUT,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ex));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate image -- "
                                + "recipnet-renderclient timed out");
            } else if (ex instanceof InterruptedException) {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.RENDER_INTERUPTED,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ex));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate image -- "
                                + "recipnet-renderclient was interrupted");
            } else {
                siteManager.recordLogEvent(new JammSupportLogEvent(
                        JammSupportLogEvent.RENDER_EXCEPTION,
                        parameters.getCrtName(), parameters.getFunction(),
                        req.getQueryString(), parameters.getSampleId(),
                        parameters.getSampleHistoryId(), ex));
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not generate image -- exception encountered by"
                                + " recipnet-renderclient");
            }
            return;
        }

        siteManager.recordLogEvent(new JammSupportLogEvent(
                parameters.getCrtName(), parameters.getFunction(),
                req.getQueryString(), parameters.getSampleId(),
                parameters.getSampleHistoryId()));
        req.setAttribute("jpegKey", new Long(fileTracker.trackFile(jpegFile,
                "image/jpeg", true)));

        // jammrender.jsp will display the view
        rd.forward(req, resp);
    }
}
