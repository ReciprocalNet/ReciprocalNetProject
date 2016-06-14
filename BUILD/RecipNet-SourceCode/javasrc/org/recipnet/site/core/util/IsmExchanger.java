/*
 * Reciprocal Net project
 * 
 * IsmExchanger.java
 *
 * 01-Aug-2005: ekoperda wrote first draft, borrowing heavily from IsmPuller
 * 12-May-2006: jobollin formatted the source and updated docs
 * 25-Jan-2009: ekoperda fixed bug #1919 in exchange()
 */

package org.recipnet.site.core.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.SoapUtil;
import org.xml.sax.SAXException;

/**
 * <p>
 * A core utility class that connects to a specified remote site in the Site
 * Network and exchanges {@code InterSiteMessage}s with it. Caller-supplied
 * ISMs are transmitted to the remote site and response ISM's received from the
 * remote site are returned to the caller. Such an exchange could be
 * characterized as either a "push" or a "pull", depending upon the nature of
 * the particular ISMs exchanged.
 * </p><p>
 * This class is designed to interface with the {@code IsmExchangeListener}
 * servlet, and that servlet is assumed to be running at the remote site. The
 * communications protocol is SOAP over HTTP. No part of JAXM, the Java API for
 * XML message, is used in this code because its API is too inflexible and its
 * current implementation from Sun (as of year 2002) is too buggy.
 * </p>
 */
public class IsmExchanger {

    /*
     * TODO: this class sets two system properties at construction time in
     * order to control the behavior of Sun's implementation of Java's
     * URLConnection class. This is both a portability issue and a
     * thread-safety issue. The bottom line is that whichever instance of this
     * class has been created most recently will control the timeout behavior
     * across all instances in the VM. Using this mechanism is necessary,
     * however, in order to prevent an uncooperative remote site from stalling
     * Site Manager's worker thread.  Consider altering this class's behavior
     * if/when Sun gives us better control over URLConnection's behavior.
     */

    private String lastServerErrorMessage = null;

    /**
     * A constant that controls the maximum number of bytes transferred to the
     * remote site in a single chunk. Changing this value may affect ISM
     * transfer performance.
     */
    private static final int HTTP_CHUNK_SIZE = 32768;

    /**
     * <p>
     * Initializes a new {@code IsmExchanger} with the specified arguments
     * </p><p>
     * <strong>WARNING:</stong> see the thread-safety and portability notice at
     * the top of this class.
     * </p>
     * 
     * @param connectTimeout the number of milliseconds that {@code exchange()}
     *        should wait for a connection to the remote HTTP server before
     *        failing.
     * @param readTimeout the number of milliseconds pullMessages() should wait
     *        for data to arrive once a connection to the remote HTTP server
     *        has been established before failing.
     */
    public IsmExchanger(int connectTimeout, int readTimeout) {
        /*
         * TODO: Sun's current implementation (year 2002) appears to wait twice
         * the specified amounts of time, although this behavior is not
         * documented.
         */
        System.setProperty("sun.net.client.defaultConnectTimeout",
                String.valueOf(connectTimeout));
        System.setProperty("sun.net.client.defaultReadTimeout",
                String.valueOf(readTimeout));
    }

    /** Simple getter */
    public String getLastServerErrorMessage() {
        return lastServerErrorMessage;
    }

    /**
     * Exchanges potentially many ISMs with the specified remote site.
     * 
     * @return an array of zero or more strings that represent XML encodings of
     *         {@code InterSiteMessage} objects that were received from the
     *         remote site during the exchange.
     * @param messagesAsXml an array of signed XML representations of
     *        {@code InterSiteMessage} objects. These are transmitted to the
     *        remote site. Each of these strings should be of the form returned
     *        by {@code ReplayRequestISM.toXmlAddSignature()} and then
     *        {@code SoapUtil.dropHeaderOnMessageDocument()}.
     * @param destSiteBaseUrl is the "base URL" of the destination site, as
     *        contained in the {@code baseUrl} field of the {@code SiteInfo}
     *        class. Normally this would be of the form
     *        {@code http://www.univ.edu/recipnet/} .
     * @param shouldDisconnect should be set to true if the caller knows that
     *        further ISM exchanges to the remote server are unlikely in the
     *        near future. This flag may prevent HTTP keep-alives from being
     *        employed behind the scenes. If the caller has any doubt about the
     *        liklihood of further ISM exchanges, this argument should be
     *        false.
     * @throws OperationFailedException if messages could not be exchanged with
     *         the remote site. The nested exception might be a
     *         {@code java.net.SocketTimeoutException} (if the socket timed
     *         out), a {@code java.io.IOException} (if another kind of I/O
     *         exception occurred), an {@code org.xml.sax.SAXException} (if the
     *         XML received from the remote site could not be parsed), or empty
     *         (if the remote server returned an HTTP error code, in which the
     *         exception's {@code message} field was populated).
     */
    public String[] exchange(String messagesAsXml[], String destSiteBaseUrl,
            boolean shouldDisconnect) throws OperationFailedException {
        lastServerErrorMessage = null;

        try {
            // Set up an HTTP connection with the specified destination URL.
            URL destination = new URL(new URL(destSiteBaseUrl),
                    "servlet/ismexchange");
            HttpURLConnection conn
                    = (HttpURLConnection) destination.openConnection();
            String docAsXml;
            
            try {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "text/xml; charset=UTF-8");
                conn.setChunkedStreamingMode(HTTP_CHUNK_SIZE);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                /*
                 * Send a SOAP request to the server, including the caller's
                 * ISMs.
                 */
                OutputStreamWriter osw = new OutputStreamWriter(
                        new BufferedOutputStream(conn.getOutputStream()),
                        "UTF-8");
                try {
                    SoapUtil.writeSoapDocument(osw, messagesAsXml,
                            "IsmExchange", "recipnet",
                            "http://www.reciprocalnet.org/master/");
                    osw.flush();
                } finally {
                    osw.close();
                }

                // Check for errors; throw an exception if the operation failed
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		    InputStream errorStream = conn.getErrorStream();
		    if (errorStream != null) {
			lastServerErrorMessage 
			        = SoapUtil.readEntireInputStream(errorStream);
		    } else {
			lastServerErrorMessage = null;
		    }
                    throw new OperationFailedException(conn.getResponseCode()
                            + " " + conn.getResponseMessage());
                }

                // Read a complete response back from the remote site.
                InputStream is = conn.getInputStream();
                try {
                    docAsXml = SoapUtil.readEntireInputStream(is);
                } finally {
                    is.close();
                }

            } finally {
                if (shouldDisconnect) {
                    conn.disconnect();
                }
            }
            
            /*
             * Parse the XML document into a DOM tree and validate the headers
             * contained within it, then discard the result and return instead
             * the String-representations of the messages
             */
            SoapUtil.decodeSoapDocument(DomUtil.xmlToDomTree(docAsXml),
                    "IsmExchange", "recipnet",
                    "http://www.reciprocalnet.org/master/");

            return SoapUtil.extractFragmentsFromXmlDocument(docAsXml, 
                    "message");
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        } catch (SAXException ex) {
            throw new OperationFailedException(ex);
        }
    }
}
