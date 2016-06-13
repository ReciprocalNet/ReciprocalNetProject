/*
 * Reciprocal Net Project
 * 
 * UploaderSupport.java
 *
 * 15-Jun-2005: ekoperda wrote first draft
 * 17-May-2006: jobollin converted to the new RepositoryFileTransfer API;
 *              reformatted the source
 * 27-Dec-2007: ekoperda fixed bug #1836 in run()
 */

package org.recipnet.site.content.servlet;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.RepositoryFileTransfer;
import org.recipnet.site.wrapper.OperationPersister;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * <p>
 * A servlet intended specifically to support the 'uploader' applet that's part
 * of the Reciprocal Net site software. It receives an uploaded file from the
 * applet and passes the data along to an {@code UploaderOperation} object,
 * which then does the all the dirty work of processing the file, etc. The only
 * supported HTTP method verb is {@code POST}. Posted data should consist of
 * the output from a {@code RepositoryFileTransfer} object, with no extraneous
 * bytes whatsoever. No data ever is returned to the client by this servlet;
 * clients can evaluate the success of their request by examining the HTTP
 * response code that they receive.
 * </p><p>
 * Consult the {@code RepositoryFileTransfer} class for more information about
 * the applet-to-servlet interface. Consult the {@code UploaderOperation} class
 * for more information about how uploaded files are processed.
 * </p>
 */
@SuppressWarnings("serial")
public class UploaderSupport extends HttpServlet {
    /**
     * A reference to the {@code OperationPersister} object that lives in
     * application scope. Set by {@code init()}.
     */
    private OperationPersister operationPersister;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.operationPersister = OperationPersister.extract(
                getServletConfig().getServletContext());
    }

    /**
     * {@inheritDoc}; this version never sends an entity back to the client; it
     * simply responds with an HTTP response code appropriate to the result of
     * the POST. Possible HTTP response codes are
     * <ul>
     * <li>{@code SC_NO_CONTENT} ({@code 204}) if all transfers were
     * processed,</li>
     * <li>{@code SC_FORBIDDEN} ({@code 403}) if one of the transfer requests
     * contained an invalid or unrecognized operation id,</li>
     * <li>{@code SC_SERVICE_UNAVAILABLE} ({@code 503}) if the core modules
     * could not be reached, and</li>
     * <li>{@code SC_INTERNAL_SERVER_ERROR} ({@code 500}) if any other
     * exception was encountered.</li>
     * </ul>
     */
    @Override
    public void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException {
        try {               
	    InputStream is = request.getInputStream();

	    /*
       	     * Read a transfer object from posted data. The next line will
             * throw an exception if there is no data to be read.
             */
            RepositoryFileTransfer transfer
                    = RepositoryFileTransfer.forDownload(is);

            /*
             * Look up the UploaderOperation object referenced by the
             * transfer request.
             */
            UploaderOperation op = (UploaderOperation)
                    this.operationPersister.getOperation(
                    transfer.getOperationId());

            /*
             * Pass the transfer request on to the UploaderOperation and let
             * it do the dirty work. The UploaderOperation may read some
             * bytes from our InputStream before it returns.
             */
            op.acceptUpload(transfer);

	    // Clean up.
	    is.close();	    
	    response.sendError(HttpServletResponse.SC_NO_CONTENT);
        } catch (EOFException ex) {
            // We encountered the end of the request unexpectedly.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ResourceNotFoundException ex) {
            /*
             * The operationId specified in the client's transfer request is
             * not recognized.
             */
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (ClassCastException ex) {
            /*
             * The operationId specified in the client's transfer request is
             * recognized, but the referenced operation does not support
             * file uploads.
             */
	    response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (RemoteException ex) {
            // Apparently the core modules are down right now.
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } catch (InterruptedIOException ex) {
            /*
             * The transfer was cancelled in-progress due to a new command
             * received from the client side.
             */
            response.sendError(HttpServletResponse.SC_CONFLICT);
        } catch (Throwable ex) {
            /*
             * TODO: this needs to be logged somehow!
             */
            ex.printStackTrace(System.out);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
