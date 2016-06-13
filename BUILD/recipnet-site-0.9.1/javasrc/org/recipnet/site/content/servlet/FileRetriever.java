/*
 * Reciprocal Net Project
 * 
 * FileRetriever.java
 *
 * 10-Oct-2002: jobollin began initial coding
 * 07-Jan-2004: ekoperda changed package references due to source tree 
 *              reorganization
 * 24-May-2006: jobollin updated docs
 */

package org.recipnet.site.content.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.site.wrapper.FileTracker;
import org.recipnet.site.wrapper.TrackedFile;

/**
 * a servlet to retreive previously stored typed data
 */
public class FileRetriever extends HttpServlet {

    /**
     * a private reference to this context's {@code FileTracker}
     */
    private FileTracker fileTracker;

    /**
     * initialize this servlet instance
     */
    @Override
    public void init() {
        fileTracker = FileTracker.getFileTracker(
                getServletConfig().getServletContext());
    }

    /**
     * processes HTTP GET requests. This version just delegates to the generic
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)} method.
     * 
     * @param req an {@code HttpServletRequest} representing the client request
     * @param resp an {@code HttpServletResponse} with which to return a
     *        response to the client
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        processRequest(req, resp);
    }

    /**
     * processes HTTP POST requests. This version just delegates to the generic
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)} method.
     * 
     * @param req an {@code HttpServletRequest} representing the client request
     * @param resp an {@code HttpServletResponse} with which to return a
     *        response to the client
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        processRequest(req, resp);
    }

    /**
     * processes HTTP GET and POST requests. Passes the request off to an
     * appropriate method based on the request parameters, or returns an error
     * response if it is unable to do so.
     * 
     * @param req an {@code HttpServletRequest} representing the client request
     * @param resp an {@code HttpServletResponse} with which to return a
     *        response to the client
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        TrackedFile tf;
        FileChannel inputChannel;
        WritableByteChannel outputChannel;
        long key;
        long flen;

        try {
            key = Long.parseLong(req.getParameter("key"));
        } catch (NullPointerException npe) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "no key specified");
            return;
        } catch (NumberFormatException nfe) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "malformed key");
            return;
        }

        // retrieve and process the file information
        tf = fileTracker.getTrackedFile(key);
        if ((tf == null) || !tf.getFile().canRead()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "The key or corresponding file was not found");
            return;
        } else {
            String type = tf.getType();
            if (type != null) {
                resp.setContentType(type);
            }
        }
        flen = tf.getLength();
        resp.setContentLength((int) flen);

        // transfer the file to the reponse body
        inputChannel = (new FileInputStream(tf.getFile())).getChannel();
        outputChannel = Channels.newChannel(resp.getOutputStream());
        for (int pos = 0; pos < flen;) {
            pos += inputChannel.transferTo(pos, flen - pos, outputChannel);
        }

        // clean up
        inputChannel.close();
        outputChannel.close();
    }
}
