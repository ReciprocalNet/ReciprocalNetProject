/*
 * Reciprocal Net project
 * 
 * OaiPmhResponder.java
 *
 * 14-Oct-2002: nisheth wrote first draft.
 * 20-Jan-2003: ekoperda reorganized code, added support for resumption tokens,
 *              and integrated class into source tree
 * 27-Jan-2003: adharurk renamed sample status constants (new workflow)
 * 04-Mar-2003: adharurk handled exceptions coming from core; tweaked by
 *              ekoperda
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 05-Jun-2003: ajooloor added logging support
 * 27-Jun-2003: jobollin fixed several bugs: misspelled "noRecordsMatch",
 *              unsupported yyyy-MM-dd date format, native metadata format
 *              well-formed but not valid, wrong response when no verb present,
 *              no check for from date after until date, from and until dates
 *              exclusive instead of inclusive.  Also performed some general
 *              cleanup.
 * 01-Sep-2003: ekoperda fixed bug #1039 in writeSamples()
 * 07-Jan-2004: ekoperda changed package references due to source tree
 *              reorganization
 * 14-Apr-2004: cwestnea modified writeListRecordsOrListIdentifiersTag(),
 *              writeSamples(), writeRecordTag(), generateResumptionToken(),
 *              extractStartIndexFromResumptionToken() and added
 *              extractMetadataPrefixFromResumptionToken() to fix bug #1189
 * 01-Jun-2004: cwestnea made changes throughout to use CoreConnector
 * 14-Jun-2004: ekoperda added language-specific text to samples' XML by
 *              modifying init(), generateResponse(), writeRecordTag(),
 *              convertSampleToNativeXml(), and RequestContext
 * 06-Aug-2004: cwestnea fixed bug #1295 by added escapeElString() and
 *              escapeAttrString()
 * 08-Aug-2004: cwestnea modified writeHeaderTag() to use SampleWorkflowBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 22-Feb-2005: ekoperda modified writeRequestTag() for compatability on JDK1.5
 * 25-Feb-2005: midurbin updated populateSearchParams() to accomodate changes
 *              in SearchParams
 * 16-Aug-2005: midurbin removed reference to AuthorizationController and
 *              HtmlHelper
 * 05-Oct-2005: jobollin removed unused imports
 * 05-Oct-2005: jobollin replaced int error codes with an enum
 * 05-Oct-2005: jobollin modified several methods to take a PrintWriter instead
 *              of a whole RequestInfo
 * 06-Oct-2005: midurbin updated references to a sample's provider
 * 06-Oct-2005: jobollin moved many methods into helper classes; those methods
 *              that produce responses specific to particular OAI requests
 *              in particular
 * 06-Oct-2005: jobollin made all response output go through an XmlWriter
 * 07-Oct-2005: jobollin moved OAI date handling into a seperate utility class
 * 07-Oct-2005: jobollin made the native XML namespace URI consistent across all
 *              responses
 * 18-Oct-2005: jobollin made metadata flavors into their own classes
 * 18-Oct-2005: jobollin replaced method generateResponse() with method
 *              handleRequest(); the new method accomplishes the same goal but
 *              has an altogether different internal structure
 * 18-Oct-2005: jobollin changed RequestInfo into PmhRequest, reducing its scope
 *              in the process
 * 19-Oct-2005: jobollin modified writeRecordTag() to not pass a ResourceBundle
 *              to metadata implementations
 * 19-Oct-2005: jobollin modified handleRequest() to no longer make any pretense
 *              of localizing the response.  It had depended heavily on the
 *              metadata handlers in this regard, and neither of the existing
 *              ones ever fully localized their output anyway (nor is there a
 *              viable way to make them do so).
 * 24-Oct-2005: jobollin modified writeHeaderTag() to use SampleWorkflowBL in
 *              determining whether or not a sample has been retracted
 * 25-Oct-2005: jobollin moved set manipulation functions to new class
 *              SetContext in wrapper
 * 25-Oct-2005: jobollin moved getSampleId(), writeHeaderTag(),
 *              writeRecordTag(), and related fields to new class ItemUtility
 * 25-Oct-2005: jobollin moved all class nested classes out into their own
 *              files
 */

package org.recipnet.site.content.servlet;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.common.XmlWriter;
import org.recipnet.common.XmlWriter.XmlVersion;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.content.oaipmh.DateUtility;
import org.recipnet.site.content.oaipmh.GetRecordHandler;
import org.recipnet.site.content.oaipmh.IdentifyHandler;
import org.recipnet.site.content.oaipmh.ItemUtility;
import org.recipnet.site.content.oaipmh.ListMetadataFormatsHandler;
import org.recipnet.site.content.oaipmh.ListSamplesHandler;
import org.recipnet.site.content.oaipmh.ListSetsHandler;
import org.recipnet.site.content.oaipmh.MetadataFormat;
import org.recipnet.site.content.oaipmh.NativeMetadataFormat;
import org.recipnet.site.content.oaipmh.OaiDcMetadataFormat;
import org.recipnet.site.content.oaipmh.PmhError;
import org.recipnet.site.content.oaipmh.PmhException;
import org.recipnet.site.content.oaipmh.PmhRequest;
import org.recipnet.site.content.oaipmh.SetContext;
import org.recipnet.site.content.oaipmh.VerbHandler;
import org.recipnet.site.shared.logevent.LogEvent;
import org.recipnet.site.shared.logevent.OaiPmhLogEvent;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A servlet implementing the "repository" end of the HTTP binding of the
 * <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html">Open
 * Archives Initiative - Protocol for Metadata Harvesting (OAI-PMH)
 * version 2.0</a>.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class OaiPmhResponder extends HttpServlet {
    
    /**
     * A default SerialVersionUID, because this class inherits Serializable
     * from HttpServlet
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The content type required (by OAI-PMH) of POSTed requests
     */
    private static final String POST_CONTENT_TYPE =
            "application/x-www-form-urlencoded";

    /**
     * A utility object for parsing and formatting OAI-PMH protocol date strings
     */
    private final DateUtility dateUtility = new DateUtility();
    
    /**
     * The host web application's {@code CoreConnector}.  This variable is
     * initialized in the {@code init()} method.
     */
    private CoreConnector coreConnector;
    
    /**
     * A map from verb strings to the corresponding verb handler objects, used
     * in dispatching requests 
     */
    private final Map<String, VerbHandler> handlerMap =
            new HashMap<String, VerbHandler>();
    
    /**
     * Initializes this servlet instance by invoking {@code super.init()},
     * reading startup parameters from the servlet context, and binding to the
     * core modules via RMI.
     * 
     * @param  config a {@code ServletConfig} describing this servlet's
     *         configuration and context
     *          
     * @throws ServletException if servlet initialization fails 
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        
        // Bind to our web application's LanguageHelper instance.
        LanguageHelper languageHelper = LanguageHelper.extract(context);
        
        Map<String, MetadataFormat> metadataMap =
                new HashMap<String, MetadataFormat>();
        MetadataFormat format;
        VerbHandler handler;
        int maxOaiPmhSamples;
        SetContext setContext;
        ItemUtility itemUtil;

        super.init(config);
        
        // Bind to our web application's CoreConnector instance.
        coreConnector = CoreConnector.extract(context);
        
        // Obtain the configured maximum number of OAI-PMH item metadata records
        // to return in one response
        try {
            maxOaiPmhSamples = Integer.parseInt(
                    context.getInitParameter("maxOaiPmhSamples"));
        } catch (NullPointerException npe) {
            maxOaiPmhSamples = 0;
        } catch (NumberFormatException nfe) {
            maxOaiPmhSamples = 0;
        }
        
        // Install metadata formats
        format = new NativeMetadataFormat(languageHelper);
        metadataMap.put(format.getPrefix(), format);
        
        format = new OaiDcMetadataFormat(dateUtility, coreConnector,
                languageHelper);
        metadataMap.put(format.getPrefix(), format);
        
        // Install verb handlers
        setContext = new SetContext(coreConnector);
        itemUtil = new ItemUtility(
                metadataMap, coreConnector, dateUtility, setContext);
        
        handler = new GetRecordHandler(coreConnector, itemUtil);
        handlerMap.put(handler.getVerb(), handler);     // GetRecord
        
        handler = new IdentifyHandler(
                context.getInitParameter("adminEmail"), dateUtility,
                coreConnector);
        handlerMap.put(handler.getVerb(), handler);     // Identify
        
        handler = new ListSamplesHandler(
                false, maxOaiPmhSamples, coreConnector, setContext, itemUtil);
        handlerMap.put(handler.getVerb(), handler);     // ListIdentifiers
        
        handler = new ListMetadataFormatsHandler(itemUtil);
        handlerMap.put(handler.getVerb(), handler);     // ListMetadataFormats
        
        handler = new ListSamplesHandler(
                true, maxOaiPmhSamples, coreConnector, setContext, itemUtil);
        handlerMap.put(handler.getVerb(), handler);     // ListRecords
        
        handler = new ListSetsHandler(coreConnector, setContext);
        handlerMap.put(handler.getVerb(), handler);     // ListSets
    }

    /**
     * Handles an HTTP GET request by passing it on to
     * {@link #handleRequest(HttpServletRequest, HttpServletResponse)} 
     * 
     * @param  request the {@code HttpServletRequest} representing the request 
     * @param  response the {@code HttpServletResponse} representing the
     *         response
     * 
     * @throws IOException if an I/O error prevents the completion of request
     *         processing and response delivery
     * 
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    /**
     * Handles an HTTP POST request by first verifying that its content type is
     * correct (application/x-www-form-urlencoded), and then passing it on to
     * {@link #handleRequest(HttpServletRequest, HttpServletResponse)} 
     * 
     * @param  request the {@code HttpServletRequest} representing the request 
     * @param  response the {@code HttpServletResponse} representing the
     *         response
     * 
     * @throws IOException if an I/O error prevents the completion of request
     *         processing and response delivery
     * 
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (POST_CONTENT_TYPE.equals(request.getContentType())) {
            handleRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "POSTed harvesting requests must have type "
                    + POST_CONTENT_TYPE);
        }
    }

    /**
     * <p>
     * Handles OAI-PMH harvesting requests delivered via either GET or POST
     * requests by parsing the request parameters and delivering a corresponding
     * OAI-PMH response.  Most of the details are delegated to an appropriate
     * {@link VerbHandler}, selected based on the OAI-PMH verb specified by
     * the request.
     * </p><p>
     * Responds with an HTTP Error 500 if a {@link RemoteException},
     * {@link OperationFailedException}, or {@link InconsistentDbException} is
     * encountered.
     * </p>
     * 
     * @param  request the {@code HttpServletRequest} representing the request 
     * @param  response the {@code HttpServletResponse} representing the
     *         response
     * 
     * @throws IOException if an I/O error prevents the completion of request
     *         processing and response delivery 
     */
    /*
     * TODO: consider returning Error 503 if the server gets busy in order to
     *       reduce the impact of some kinds of DoS attacks.  [Don't neglect to
     *       consider how the servlet would determine whether the server is
     *       busy.]
     */
    @SuppressWarnings("unchecked")
    private void handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PmhRequest oaiRequest = new PmhRequest(request, dateUtility);
        XmlWriter writer;
        LogEvent logEvent;

        /*
         * set up
         */

        response.setContentType("text/xml; charset=UTF-8");

        // Open an XmlWriter on the response's output stream
        writer = new XmlWriter(response.getOutputStream(), XmlVersion.XML_1_0,
                "UTF-8");
        writer.setDefaultIndent(3);

        /*
         * write common response part
         */
        
        writer.openElement("OAI-PMH");
        writer.addAttribute("xmlns", "http://www.openarchives.org/OAI/2.0/");
        writer.addAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        writer.addAttribute("xsi:schemaLocation",
                "http://www.openarchives.org/OAI/2.0/ "
                + "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");

        writer.openElement("responseDate", -1);
        writer.addText(dateUtility.formatDate(new Date()));
        writer.closeElement("responseDate");

        writer.openElement("request", -1);

        /*
         * Note: whether or not the request parameters are echoed depends on
         * their collective validity.  None are echoed in the event of a BadVerb
         * or BadArgument OAI error; otherwise all are echoed (even if some
         * other OAI error is ultimately indicated). 
         */
        
        try {
            try {
                VerbHandler handler;

                handler = handlerMap.get(oaiRequest.getVerb());
                if (handler == null) {
                    throw new PmhException(PmhError.BAD_VERB, "verb '"
                            + oaiRequest.getVerb() + "' is not supported.");
                }

                // Check request well-formedness
                handler.validateRequest(oaiRequest);
                
                /*
                 * The verb is known, and its arguments conform to the spec;
                 * echo them as attributes of the <request> element.
                 * 
                 * Note:  This bit performs an unchecked cast, but the docs of
                 * the ServletRequest class guarantee that it is safe:
                 * 
                 * TODO: remove the cast (and the SuppressWarnings annotation on
                 * this method) when some future version of the Servlet API
                 * returns an appropriately-typed Map instead of a raw one
                 */
                for (Map.Entry<String, String[]> entry :
                        ((Map<String, String[]>) request.getParameterMap()).entrySet()) {
                    writer.addAttribute(entry.getKey(), entry.getValue()[0]);
                }
                
                // Write the request URL to the <request> element body
                writer.addText(request.getRequestURL().toString());
                writer.closeElement("request");

                /*
                 * Handle the request via the appropriate handler.  The handler
                 * will either write the appropriate XML element to the writer,
                 * or it will throw an PmhException indicating a protocol error
                 * (without having written anything).
                 */
                try {
                    handler.handleRequest(oaiRequest, writer);
                    logEvent = createSuccessEvent(oaiRequest);
                } catch (PmhException oe) {
                    writeErrorTag(writer, oe.getErrorType(), oe.getMessage());
                    logEvent = createOaiErrorEvent(oaiRequest, oe);
                }
            } catch (PmhException oe) {

                /*
                 * Invalid argument(s) (vis-a-vis OAI-PMH) or missing, multiple,
                 * or unknown verb.  These are the cases for which the request
                 * parameters are not echoed as attributes of the <request>
                 * element, but the request URL still needs to be written to the
                 * element body.
                 */ 
                writer.addText(request.getRequestURL().toString());
                writer.closeElement("request");
                writeErrorTag(writer, oe.getErrorType(), oe.getMessage());
                logEvent = createOaiErrorEvent(oaiRequest, oe);
            }

            /*
             * finish up
             */
            
            writer.closeAllElements(); // Should affect just the OAI-PMH element
            writer.flush();
            
            // Not responsible for closing the stream
            
        } catch (RemoteException re) {
            
            /*
             * Error encountered while attempting to communicate with the
             * Reciprocal Net core 
             */
            coreConnector.reportRemoteException(re);
            try {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioe) {
                // drop it
            }
            logEvent = createInternalErrorEvent(oaiRequest, re);
        } catch (RecipnetException re) {
            
            /*
             * Internal error reported by the Reciprocal Net core 
             */
            try {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioe) {
                // drop it
            }
            logEvent = createInternalErrorEvent(oaiRequest, re);
        }
        
        /*
         * log the activity 
         */
        try {
            coreConnector.getSiteManager().recordLogEvent(logEvent);
        } catch (RemoteException re) {
            // drop it
        } catch (IllegalStateException ise) {
            // drop it
        }
    }
    
    /**
     * Creates a log event appropriate for reporting an OAI-PMH request the
     * resulted in a successful OAI response being issued
     * 
     * @param  oaiRequest an {@code InternalOaiRequest} representing the request that
     *         resulted in an OAI error
     * 
     * @return a {@code LogEvent} appropriate for logging the request and its
     *         result
     */
    private LogEvent createSuccessEvent(PmhRequest oaiRequest) {
        HttpServletRequest request = oaiRequest.getRequest();
        String verb;
        
        try {
            verb = oaiRequest.getVerb();
        } catch (PmhException oe) {
            verb = null;
        }
        
        return new OaiPmhLogEvent(request.getRemoteAddr(),
                oaiRequest.getServerName(), verb,
                oaiRequest.getCountSamplesReturned());
    }

    /**
     * Creates a log event appropriate for reporting an OAI-PMH request the
     * resulted in an OAI-level error response being issued
     * 
     * @param  oaiRequest an {@code InternalOaiRequest} representing the request
     *         that resulted in an OAI error
     * @param  exception an {@code PmhException} containing the pertinent error
     *         type and detail message
     * 
     * @return a {@code LogEvent} appropriate for logging the request and its
     *         result
     */
    private LogEvent createOaiErrorEvent(PmhRequest oaiRequest,
            PmhException exception) {
        HttpServletRequest request = oaiRequest.getRequest();
        String verb;
        
        try {
            verb = oaiRequest.getVerb();
        } catch (PmhException oe) {
            verb = null;
        }
        
        return new OaiPmhLogEvent(request.getRemoteAddr(),
                oaiRequest.getServerName(), verb,
                oaiRequest.getIdentifier(), oaiRequest.getSet(),
                oaiRequest.getMetadataPrefix(), oaiRequest.getFrom(),
                oaiRequest.getUntil(), oaiRequest.getResumptionToken(),
                exception.getErrorType().getErrorCode(),
                exception.getMessage());
    }

    /**
     * Creates a log event appropriate for reporting an OAI-PMH request the
     * resulted in an HTTP error response being issued
     * 
     * @param  oaiRequest an {@code InternalOaiRequest} representing the request
     *         that resulted in an OAI error
     * @param  exception an {@code PmhException} containing the pertinent error
     *         type and detail message
     * 
     * @return a {@code LogEvent} appropriate for logging the request and its
     *         result
     */
    private LogEvent createInternalErrorEvent(PmhRequest oaiRequest,
            Exception exception) {
        HttpServletRequest request = oaiRequest.getRequest();
        String verb;
        
        try {
            verb = oaiRequest.getVerb();
        } catch (PmhException oe) {
            verb = null;
        }
        
        return new OaiPmhLogEvent(request.getRemoteAddr(),
                oaiRequest.getServerName(), verb,
                oaiRequest.getIdentifier(), oaiRequest.getSet(),
                oaiRequest.getMetadataPrefix(), oaiRequest.getFrom(),
                oaiRequest.getUntil(), oaiRequest.getResumptionToken(),
                exception.getMessage(), exception);
    }

    /**
     * Writes an error tag into the response describing an OAI protocol error
     * encountered while attempting to service a request.
     *
     * @param  writer an {@code XmlWriter} to which to write the error
     *         information
     * @param  errorType an {@code PmhError} representing the error that
     *         was detected
     * @param  errorDetail a text description of the error that occurred,
     *         providing more information to a human consumer of the response;
     *         may be {@code null}
     */
    private void writeErrorTag(XmlWriter writer, PmhError errorType,
            String errorDetail) {
        String text = errorType.getErrorDescription()
                + ((errorDetail == null) ? "" : (": " + errorDetail));

        writer.openElement("error", -1);
        writer.addAttribute("code", errorType.getErrorCode());
        writer.addText(text);
        writer.closeElement();
    }
}
