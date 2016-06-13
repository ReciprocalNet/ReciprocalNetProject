/*
 * Reciprocal Net project
 * 
 * IsmExchangeListener.java
 *
 * 15-Dec-2005: ekoperda wrote first draft, borrowing from IsmPullListner and
 *              IsmPushListener
 * 24-May-2006: jobollin formatted the source
 * 02-Jul-2008: ekoperda modified doPost() to work-around a bug in Tomcat
 */

package org.recipnet.site.content.servlet;

import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.rmi.RemoteException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.wrapper.CoreConnector;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * <p>
 * Servlet that services ISM "pull" and "push" requests from other sites in the
 * Site Network. It is expected that the {@code IsmExchanger} class will be
 * invoking this servlet from the client side. The communications protocol is
 * SOAP over HTTP. No part of of JAXM, the Java API for XML Message, is used in
 * this code because that API is too inflexible and its current implementation
 * from Sun (in year 2002) is too buggy.
 * </p><p>
 * The sequence of communication goes something like this:
 * <ol>
 * <li>The remote site initiates an HTTP POST operation to this servlet,
 * passing inter-site messages to this servlet as a SOAP request.</li>
 * <li>This servlet parses the SOAP request and passes the ISMs down to core
 * for processing.</li>
 * <li>Core returns zero or more response ISMs back to this servlet.</li>
 * <li>This servlet constructs a SOAP response containing the response ISMs
 * and transmits it to the remote site as the HTTP response.</li>
 * </ol>
 * </p>
 */
public class IsmExchangeListener extends HttpServlet {

    /** A reference to the web application's {@code CoreConnector}. */
    private CoreConnector coreConnector;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.coreConnector = CoreConnector.extract(config.getServletContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            // Verify that the post contains XML in UTF-8.
            String characterEncoding = request.getCharacterEncoding();
            
            if ((characterEncoding == null)
                    || !characterEncoding.trim().equalsIgnoreCase("UTF-8")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Expected character encoding UTF-8 but got '"
                                + characterEncoding + "'");
                return;
            }
            String contentType = request.getContentType();
            if ((contentType == null)
                    || (!contentType.trim().equals("text/xml")
                            && !contentType.trim().equals(
                            "text/xml; charset=UTF-8"))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Expected MIME Content-Type:text/xml but got '"
                                + contentType + "'");
                return;
            }

	    /*
	     * Read the posted data into a string.
	     *
	     * BUG ALERT!  As of Tomcat 5.5.26-3, there seems to be a bug in
	     * the Reader that is obtained from request.getReader().  For some
	     * reason that Reader seems unable to read more than 32 KB from the
	     * client -- it reports end-of-stream prematurely.  So, to work
	     * around this bug, we invoke request.getInputStream() and then
	     * construct for ourselves a Reader than actually works.
	     */
	    String encoding = request.getCharacterEncoding();
            InputStream is = request.getInputStream();
	    Reader reader;
	    if (encoding != null && Charset.isSupported(encoding)) {
		reader = new BufferedReader(
                        new InputStreamReader(is, encoding));
	    } else {
		reader = new BufferedReader(new InputStreamReader(is));
	    }
	    String docAsXml = null;
            try {
                docAsXml = SoapUtil.readEntireInputStream(reader);
            } finally {
                reader.close();
            }

            /*
             * Parse the XML document into a DOM tree, then validate the SOAP
             * headers contained within it.
             */
            Document doc = DomUtil.xmlToDomTree(docAsXml);
            SoapUtil.decodeSoapDocument(doc, "IsmExchange", "recipnet",
                    "http://www.reciprocalnet.org/master/");

            /*
             * Now discard the DOM tree and return to the XML string. It should
             * contain zero or more <message> elements. We will extract those
	     * as strings.
             */
            String incoming[] = SoapUtil.extractFragmentsFromXmlDocument(
                    docAsXml, "message");

            // Pass these ISM's down to core and receive the replies.
            String outgoingRaw[] = this.coreConnector.getSiteManager().exchangeInterSiteMessages(
                    incoming, request.getRemoteAddr());

            /*
             * Convert the outgoing ISM's obtained from SiteManager to a format
             * suitable for transmission to the remote site.
             */
            String outgoing[] = new String[outgoingRaw.length];
            
            for (int i = 0; i < outgoing.length; i++) {
                outgoing[i] = SoapUtil.dropXmlDocumentHeader(outgoingRaw[i],
                        "message");
            }

            // Write the response to the client.
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Writer writer = response.getWriter();
            SoapUtil.writeSoapDocument(writer, outgoing, "IsmExchange",
                    "recipnet", "http://www.reciprocalnet.org/master/");
            writer.flush();
            writer.close();
        } catch (SAXException ex) {
            // The request XML was not parsable.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    ex.toString());
        } catch (RemoteException ex) {
            this.coreConnector.reportRemoteException(ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Core modules not accessible: " + ex.toString());
        } catch (OperationFailedException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Operation failed: " + ex.toString());
        }
    }
}
