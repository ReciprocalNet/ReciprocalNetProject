/*
 * Reciprocal Net Project
 *
 * PmhRequest.java
 *
 * 18-Oct-2005: jobollin wrote first draft, based (vaguely) on the class that
 *              once was OaiPmhResponder.RequestInfo
 */

package org.recipnet.site.content.oaipmh;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.recipnet.common.ParsedDate;


/**
 * A class representing an OAI-PMH protocol (version 2.0) request, as embedded
 * in an HTTP request according to the description in the protocol specification
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class PmhRequest {

    /**
     * The {@code HttpServletRequest} on which this {@code PmhRequest} is based
     */
    private final HttpServletRequest request;

    /**
     * An {@code DateUtility} to use for parsing dates
     */
    private final DateUtility dateUtility;

    /**
     * The 'from' date specified for this request, if any
     */
    private ParsedDate fromDate;

    /**
     * The 'until' date specified for this request, if any
     */
    private ParsedDate untilDate;

    /**
     * The number of samples that were returned to the OAI-PMH client for a
     * successful request,
     */
    private int countSamplesReturned;

    /**
     * Initializes an {@code PmhRequest} based on the specified request,
     * assigning the specified {@code DateUtility} for its use
     * 
     * @param  request the {@code HttpServletRequest} representing the
     *         HTTP GET or POST request in which this OAI-PMH request is
     *         embedded
     * @param  dateUtility an {@code DateUtility} for use in parsing dates
     *         associated with this request
     */
    public PmhRequest(HttpServletRequest request, DateUtility dateUtility) {
        this.request = request;
        this.dateUtility = dateUtility;
    }

    /**
     * Returns the OAI verb associated with this request.  Unlike the other
     * OAI parameter accessors, this one verifies that exactly one value is
     * present for the parameter and throws an PmhException if that is not
     * the case.
     * 
     * @return the verb {@code String}
     * 
     * @throws PmhException if the OAI-PMH verb is unspecified or multiply
     *         specified; the associated error type will be
     *         {@link PmhError#BAD_VERB}
     */
    public String getVerb() throws PmhException {
        String[] verbs = request.getParameterValues("verb");

        if (verbs == null) {
            throw new PmhException(PmhError.BAD_VERB, "No verb specified");
        } else if (verbs.length != 1) {
            throw new PmhException(PmhError.BAD_VERB,
                    "multiple verbs specified");
        }
        
        return verbs[0];
    }

    /**
     * Returns the OAI identifier specified as an argument to this request
     *  
     * @return the identifier {@code String}, or {@code null} if none was
     *         specified
     */
    public String getIdentifier() {
        return request.getParameter("identifier");
    }

    /**
     * Returns the OAI metadata prefix specified as an argument to this request
     *  
     * @return the metadata prefix {@code String}, or {@code null} if none was
     *         specified
     */
    public String getMetadataPrefix() {
        return request.getParameter("metadataPrefix");
    }

    /**
     * Returns the OAI resumption token specified as an argument to this request
     *  
     * @return the resumption token {@code String}, or {@code null} if none was
     *         specified
     */
    public String getResumptionToken() {
        return request.getParameter("resumptionToken");
    }

    /**
     * Returns the OAI set specified as an argument to this request
     *  
     * @return the set {@code String}, or {@code null} if none was specified
     */
    public String getSet() {
        return request.getParameter("set");
    }

    /**
     * Returns the OAI from date specified as an argument to this request
     *  
     * @return the from date {@code String}, or {@code null} if none was
     *         specified
     */
    public String getFrom() {
        return request.getParameter("from");
    }

    /**
     * Returns the OAI 'from' date associated with this request, if any, as a
     * {@code ParsedDate}
     * 
     * @return the 'from' date as a {@code ParsedDate}, or {@code null} if there
     *         is no 'from' date
     * @throws ParseException if the 'from' parameter is present but cannot
     *             be parsed as a date in any of the supported formats
     */
    public ParsedDate getFromDate() throws ParseException {
        if (fromDate == null) {
            String from = getFrom();

            if (from != null) {
                fromDate = dateUtility.parseDate(from, false);
            }
        }
        return fromDate;
    }

    /**
     * Returns the OAI until date specified as an argument to this request
     *  
     * @return the until date {@code String}, or {@code null} if none was
     *         specified
     */
    public String getUntil() {
        return request.getParameter("until");
    }

    /**
     * Returns the OAI 'until' date associated with this request, if any, as a
     * {@code ParsedDate}
     * 
     * @return the 'until' date as a {@code ParsedDate}, or {@code null} if
     *         there is no 'until' date
     * @throws ParseException if the 'until' parameter is present but cannot
     *             be parsed as a date in any of the supported formats
     */
    public ParsedDate getUntilDate() throws ParseException {
        if (untilDate == null) {
            String until = getUntil();

            if (until != null) {
                untilDate = dateUtility.parseDate(until, true);
            }
        }
        return untilDate;
    }

    /**
     * Returns the HTTP request object on which this {@code PmhRequest} is
     * based
     * 
     * @return the {@code HttpServletRequest} on which this object is based
     */
    public HttpServletRequest getRequest() {
        return request;
    }
    
    /**
     * Returns the name of the server to which this request was directed, as
     * specified by the client
     * 
     * @return the server name as a {@code String}
     */
    public String getServerName() {
        return request.getServerName();
    }

    /**
     * Returns the full URL with which this request was made, as specified by
     * the client
     * 
     * @return the URL as a {@code String}
     */
    public String getRequestUrl() {
        return request.getRequestURL().toString();
    }

    /**
     * Stores the number of samples returned to the OAI-PMH client for a
     * successful request
     * 
     * @param countSamplesReturned the number of samples
     */
    public void setCountSamplesReturned(int countSamplesReturned) {
        this.countSamplesReturned = countSamplesReturned;
    }

    /**
     * Returns the number of samples returned in response to this request, as
     * previously indicated by invocation of
     * {@link #setCountSamplesReturned(int)}
     * 
     * @return the number of samples 
     */
    public int getCountSamplesReturned() {
        return countSamplesReturned;
    }
}
