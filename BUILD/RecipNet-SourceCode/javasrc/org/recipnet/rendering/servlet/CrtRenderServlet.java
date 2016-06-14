/*
 * Reciprocal Net project
 * rendering software
 * 
 * CrtRenderServlet.java
 *
 * 18-Aug-2004: eisiorho wrote first draft
 * 03-Sep-2004: ekoperda fixed bug #1379 by updating import list
 * 23-Nov-2005: jobollin updated handleRequest() to accommodate ScnFile changes;
 *              applied type parameters where needed; removed unused local
 *              variables; and removed unused imports
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.messaging.JAXMServlet;
import javax.xml.messaging.ReqRespListener;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.ScnFile;
import org.recipnet.common.molecule.Atom;
import org.recipnet.rendering.dispatcher.AbstractJobDispatcher;
import org.recipnet.rendering.dispatcher.ArtJob;
import org.recipnet.rendering.dispatcher.ComputeNode;
import org.recipnet.rendering.dispatcher.RequestAuthenticator;
import org.recipnet.rendering.dispatcher.SimpleJobDispatcher;
import org.recipnet.rendering.util.CrtFileToScnFileConverter;
import org.recipnet.rendering.util.PixImageConverter;

/**
 * <p>
 * CrtRenderServlet has one method, <b>doPost</b>, which receives a
 * SOAPMessage, makes a <b>CrtRenderRequest</b> object from the SOAPMessage,
 * create an <b>RequestAuthenticator</b> object to authenticate the incoming
 * request, converts the .crt file into a .scn file, create an <b>ArtJob</b>
 * and submits the job into the job queue, receives the job results and encodes
 * a SOAPMessage, and sends it back to the client.
 * </p><p>
 * There will be logging that will document when certain events occur in the
 * servlet, such as the conversion of the .crt to the .scn, the completion of a
 * render job, and any error that may occur during the handling of incoming
 * requests.
 * </p>
 */
public class CrtRenderServlet extends JAXMServlet implements ReqRespListener {

    /**
     * Used to declare what type of service that has been requested for
     * {@code RequestAuthenticator}
     */
    public final static String REQUEST = RequestAuthenticator.CRT_REQUEST;

    /**
     * Creates an instance of {@code RequestAuthenticator} for client
     * authorization. This variable is initialized in {@code init()} and not
     * thereafter modified.
     */
    private RequestAuthenticator reqAuth;

    /**
     * Creates an instance of {@code PixImageConverter} for conversion of .crt
     * file to .jpg, .pcx, .pix, or .ppm; this variable is initialized in
     * {@code init()} and not thereafter modified.
     */
    private PixImageConverter pixImageConverter;

    /**
     * Contains a set of nodes that are used to for distributed rendering. This
     * variable is initialized in {@code init()} and not thereafter modified.
     */
    private Set<ComputeNode> availableNodes;

    /**
     * Creates an instance of {@code AbstractJobDispatcher} to help distribute
     * render jobs that are sent. This variable is initialized in {@code init()}
     * and not thereafter modified.
     */
    private AbstractJobDispatcher dispatcher;

    /**
     * Holds the location of the art program. This variable is initialized in
     * {@code init()} and not thereafter modified.
     */
    private String art;

    /**
     * Initalizes many of the member variables in {@code this} class.
     * 
     * @param servletConfig a {@code ServetConfig} object used by a servlet
     *        container to pass information to a servlet during initialization.
     * @throws ServletException if there is a general error which occurred
     *         during the execution of this method
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        ServletContext context = servletConfig.getServletContext();
        reqAuth = RequestAuthenticator.getRequestAuthenticator(context);
        art = context.getInitParameter("art");
        String cjpeg = context.getInitParameter("cjpeg");
        String vort2pcx = context.getInitParameter("vort2pcx");
        String vort2ppm = context.getInitParameter("vort2ppm");
        String renderDir = context.getInitParameter("renderDir");

        // Create our set of available nodes.
        availableNodes = new TreeSet<ComputeNode>();
        availableNodes.add(new ComputeNode("node1", "node1"));

        // Instantiate the dispatcher.
        dispatcher = new SimpleJobDispatcher(availableNodes,
                new File(renderDir), false, 0, 5, 5);
        // Instantiate an image converter.
        pixImageConverter = new PixImageConverter(vort2ppm, vort2pcx, cjpeg);
    }

    /**
     * Handles rendering requests that communicates through HTTP Post.
     * 
     * @param req a {@code HttpServletRequest} contains the request made to
     *        {@code this} servlet
     * @param res a {@code HttpServletResponse} contains the response for the
     *        client from {@code this}
     * @throws IOException
     * @throws ServletException if there is a general error which occurred
     *         during the execution of this method
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        // Verify that we received XML data in the post
        String contentType = req.getHeader("Content-Type");
        if ((contentType == null) || !contentType.equals("text/xml")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Expected MIME Content-Type:text/xml but got '"
                            + contentType + "'");
            return;
        }
        SOAPMessage msg = null;
        CrtRenderRequest crr = new CrtRenderRequest();

        try {
            ServletInputStream sis = req.getInputStream();
            byte[] data = new byte[req.getContentLength()];
            int total = 0;
            while (total < req.getContentLength()) {
                total += sis.read(data, total, req.getContentLength() - total);
            }
            String inme = new String(data);
            crr.decode(inme);
            // Write the response back to the remote site
            res.setHeader("Content-Type", "text/xml");
            res.setStatus(HttpServletResponse.SC_OK);
            
            /*
             * XXX: This is very broken.  The most obvious symptom of brokenness
             * is that the 'msg' variable is necessarilly null at this point,
             * but the underlying problem is that nothing is done with the
             * request that would contribute to producing a meaningful response
             */
            
            msg.writeTo(res.getOutputStream());
            res.flushBuffer();
        } catch (SOAPException soape) {
            throw new ServletException(soape);
        }
    }

    /**
     * Handles SOAP requests for image processing.
     * 
     * @param message the request for image rendering as a {@code SOAPMessage}
     * @return the rendered image as a {@code SOAPMessage}
     */
    public SOAPMessage onMessage(SOAPMessage message) {
        try {
            CrtRenderRequest crr = new CrtRenderRequest();
            crr.decode(message);
            return handleRequest(crr);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe.getMessage());
        } catch (SOAPException se) {
            throw new NumberFormatException(se.getMessage());
        }
    }

    /**
     * @param crr contains the reqeust for image rendering as a
     *        {@code CrtRenderRequest}
     * @return the rendered image as a {@code SOAPMessage}
     * @throws SOAPException if an error occurs while handling SOAP elements
     * @throws IOException
     */
    public SOAPMessage handleRequest(CrtRenderRequest crr)
            throws SOAPException, IOException {
        try {
            CrtRenderResponse response = new CrtRenderResponse();
            String commandArgs[] = new String[1];
            commandArgs[0] = "-n";

            // Checks to see if it's an authenticate requested CrtRenderRequest
            if (!reqAuth.authenticateCredentials(crr.username, crr.password)) {
                return null;
            } else if (!reqAuth.checkAuthorizationForRequestType(crr.username,
                    REQUEST)) {
                return null;
            } else if (!reqAuth.checkAuthorizationForRequestedPriority(
                    crr.username, crr.requestedPriority)) {
                return null;
            }
            Reader r = new InputStreamReader(new ByteArrayInputStream(
                    crr.crtFileData), "UTF-8");
            CrtFile<Atom> crtFile = CrtFile.readFrom(r);
            r.close();
            ScnFile scnFile = new CrtFileToScnFileConverter().convert(crtFile,
                    crr);
            StringWriter writer = new StringWriter();
            scnFile.writeTo(writer);

            ArtJob renderJob = new ArtJob(ArtJob.RENDER_WITH_ART, art,
                    commandArgs, crr.requestedImageFormat, crr.imageSizeX,
                    crr.imageSizeY, crr.jpegQuality,
                    writer.getBuffer().toString().getBytes("UTF-8"),
                    pixImageConverter, crr.requestedPriority);

            if (!reqAuth.checkAuthorizationForJobCost(crr.username, renderJob)) {
                return null;
            }
            dispatcher.doJob(renderJob);
            response.pictureFileData = renderJob.getFinalFileData();
            response.renderTime = renderJob.getPerfTimer().elapsed();
            response.username = crr.username;
            response.imageType = crr.requestedImageFormat;
            return response.encode();
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }
}
