/*
 * Reciprocal Net Project
 * 
 * ListSamplesHandler.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.ParsedDate;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.search.ActionDateSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.validation.OaiPmhMetadataPrefixValidator;
import org.recipnet.site.shared.validation.OaiPmhSetSpecValidator;
import org.recipnet.site.shared.validation.ValidValidator;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * <p>
 * A {@code VerbHandler} implementation supporting those "List*" OAI-PMH
 * verbs for which this repository implementation may employ OAI-PMH flow
 * control; as of this version, that's "ListRecords" and "ListIdentifiers".
 * These verbs' arguments are expected to consist of
 * <strong>either</strong>
 * </p>
 * <ul>
 * <li>a {@code metadataPrefix} (required) and optionally one or more of
 * {@code from}, {@code until}, and {@code set}, <strong>or</strong></li>
 * <li>a {@code resumptionToken}</li>
 * </ul>
 * <p>In the first case, the optional arguments are subject to some additional
 * general OAI-PMH validation rules:
 * </p>
 * <ul>
 * <li>The value of the "setSpec" argument (if present) must conform to the
 *     format specified by OAI-PMH.</li>
 * <li>The values of the "from" and "until" arguments (if present) must
 *     conform to the date format specified by OAI-PMH.</li>
 * <li>If the "from" and "until" arguments are both present then their
 *     values must be expressed to the same granularity.</li>
 * <li>If the "from" and "until" arguments are both present then the time
 *     represented by the "until" value must not precede the one represented by
 *     the "from" value (though they may express the <em>same</em> time,
 *     relative to their granularity).</li>
 * </ul> 
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class ListSamplesHandler extends VerbHandler {

    /**
     * A flag indicating whether this handler is acting as a ListRecords
     * handler (if {@code true}); otherwise it is a ListIdentifiers handler
     */
    private final boolean listRecords;
    
    /**
     * The maximum number of metadata records that should be returned in a
     * single ListIdentifiers or ListRecords request. If more samples are to
     * be returned then the servlet will send this number of samples
     * accompanied by a resumptionToken. A well-behaved client then will
     * initiate a second request to this servlet requesting more records,
     * and so forth. The value 0 is reserved and means that no limit should
     * be enforced.
     */
    private final int maxOaiPmhSamples;
    
    /**
     * An {@code SetContext} for this handler's use
     */
    private final SetContext setContext;
    
    /**
     * An {@code ItemUtility} for use interpreting item identifiers and
     * producing item-level OAI-PMH information
     */
    private final ItemUtility itemUtil;
    
    /**
     * Initializes a {@code ListSamplesHandler} with the specified
     * parameters
     * 
     * @param  listRecords {@code true} for this handler to implement the
     *         full "ListRecords" verb; {@code false} for it to implement
     *         the similar but less verbose "ListIdentifiers" verb
     * @param  maxSamples the maximum number of sample metadata records that
     *         should be returned in a sample response; requests for more
     *         than this number of samples will be split among multiple
     *         responses with use of resumption tokens.  A non-positive
     *         value indicates no limit; small positive values are legal
     *         but not recommended. 
     * @param  connector a {@code CoreConnector} with which to retrieve
     *         information from the Reciprocal Net core
     * @param  setContext an {@code SetContext} for this handler's use in
     *         fulfilling requests
     * @param  itemUtility an {@code ItemUtility} for this handler's use
     *         interpreting item identifiers and producing item-level
     *         OAI-PMH information
     */
    public ListSamplesHandler(boolean listRecords, int maxSamples,
            CoreConnector connector, SetContext setContext,
            ItemUtility itemUtility) {
        super(listRecords ? "ListRecords" : "ListIdentifiers", connector);
        this.listRecords = listRecords;
        this.maxOaiPmhSamples = Math.max(0, maxSamples);
        this.setContext = setContext;
        this.itemUtil = itemUtility;
        supportArgument("metadataPrefix", new OaiPmhMetadataPrefixValidator());
        supportArgument("from", new ValidValidator());  // validated differently
        supportArgument("until", new ValidValidator()); // validated differently
        supportArgument("set", new OaiPmhSetSpecValidator());
        supportArgument("resumptionToken", new ValidValidator());
    }

    /**
     * Validates the well-formedness of the arguments to be handled by this verb
     * handler.
     * 
     * @param  request an {@code PmhRequest} representing the request
     * 
     * @throws PmhException if the arguments are invalid
     * 
     * @see VerbHandler#validateRequest(PmhRequest)
     */
    @Override
    public void validateRequest(PmhRequest request) throws PmhException {
        String resumptionToken = request.getResumptionToken();
        
        if ((resumptionToken == null)
                && (request.getMetadataPrefix() == null)) {
            throw new PmhException(PmhError.BAD_ARGUMENT,
                    "A 'metadataPrefix' or 'resumptionToken' "
                    + "argument is required for this OAI verb.");
        } else if ((resumptionToken != null)
                && (request.getRequest().getParameterMap().size() > 2)) {
            // one parameter is the verb, the others are arguments
            throw new PmhException(PmhError.BAD_ARGUMENT,
                    "'resumptionToken' may not be accompanied by other "
                    + "arguments");
        } else {
            super.validateRequest(request);
                
            /*
             * If both a from and an until date were specified then
             * the two must be compatible:
             */
            ParsedDate fromDate;
            ParsedDate untilDate;
            
            try {
                fromDate = request.getFromDate();
            } catch (ParseException pe) {
                throw new PmhException(PmhError.BAD_ARGUMENT,
                        "malformed date in 'from' argument");
            }
            try {
                untilDate = request.getUntilDate();
            } catch (ParseException pe) {
                throw new PmhException(PmhError.BAD_ARGUMENT,
                "malformed date in 'until' argument");
            }
                
            if ((fromDate != null) && (untilDate != null)) {
                
                /*
                 * Verify that the from and until dates have the same
                 * granularity
                 */
                if (!untilDate.isCompatibleWith(fromDate)) {
                    throw new PmhException(PmhError.BAD_ARGUMENT,
                            "OAI 'from' date '" + request.getFrom()
                            + "' has different granularity than "
                            + "OAI 'until' date '" + request.getUntil()
                            + "'");
                }
                
                /*
                 * make sure the from date is not after the until date,
                 * taking into account that for the search
                 * parameters' purpose the dates have been parsed
                 * into exclusive upper and lower bounds (accounting
                 * for the granularities of the supplied date strings)
                 */
                if ((untilDate.getDate().getTime()
                         - fromDate.getDate().getTime()) < 2000) {
                    throw new PmhException(PmhError.BAD_ARGUMENT,
                            "OAI 'from' date '" + request.getFrom()
                            + "' must not be after "
                            + "OAI 'until' date '" + request.getUntil()
                            + "'");
                }
            }
        }
    }

    /**
     * {@inheritDoc}.  This version handles a List(Records|Identifiers)
     * request by either starting a new search or returning additional
     * records from a previous one
     * 
     * @see VerbHandler#handleRequest(PmhRequest, XmlWriter)
     */
    @Override
    public void handleRequest(PmhRequest request, XmlWriter writer)
            throws InconsistentDbException, OperationFailedException,
            RemoteException, IOException {
        int searchId;
        int startIndex;
        String metadataPrefix;
        String resumptionToken = request.getResumptionToken();
        SampleManagerRemote sampleManager =
                getCoreConnector().getSampleManager();
        
        /*
         *  argument validation assumed to have already been performed via
         *  validateArguments(PmhRequest)
         */

        if (resumptionToken == null) {
            String set = request.getSet();
            SearchParams params;
            
            metadataPrefix = request.getMetadataPrefix();
            
            if ((set != null) && !setContext.isSetValid(set)) {
                
                // The requested set is not supported
                throw new PmhException(PmhError.NO_RECORDS_MATCH,
                        "unknown set '" + set + "' requested");
            } else if ((metadataPrefix != null)
                    && !itemUtil.isSupportingMetadataPrefix(metadataPrefix)) {
                
                // The requested metadata format is not supported.
                throw new PmhException(PmhError.CANNOT_DISSEMINATE_FORMAT,
                        "'" + metadataPrefix
                        + "' not supported by this site.");
            }

            // Create and populate a SearchParams object.
            params = sampleManager.getEmptySearchParams();
            populateSearchParams(params, request);

            // Pass the SearchParams to core and obtain a search id.
            searchId = sampleManager.storeSearchParams(params);
            startIndex = 0;
        } else {
            // This claims to be a continuation of a previous request.
            try {
                SampleResumptionToken token =
                        SampleResumptionToken.fromString(resumptionToken);
                
                searchId = token.getSearchId();
                startIndex = token.getStartIndex();
                metadataPrefix = token.getMetadataPrefix();
                
                if (!itemUtil.isSupportingMetadataPrefix(metadataPrefix)) {
                    throw new PmhException(PmhError.BAD_RESUMPTION_TOKEN, 
                            "invalid resumption token");
                }
            } catch (IllegalArgumentException iae) {
                throw new PmhException(PmhError.BAD_RESUMPTION_TOKEN, 
                        iae.getMessage());
            }
        }

        // Fetch and serve the samples that are to be returned this round trip.
        writeSamples(request, writer, searchId, startIndex, metadataPrefix);
    }

    /**
     * Configures a <code>SearchParams</code> to perform a search for the
     * samples that conform to the {@code set}, {@code from}, and
     * {@code until} criteria expressed in the OAI request
     * 
     * @param  searchParams the {@code SearchParams} to configure 
     * @param  request an {@code PmhRequest} representing the request
     *
     * @throws IllegalStateException if any of the OAI parameters is
     *         specified but not valid and parsable (this should not happen
     *         because the parameters should have already been validated)
     */
    private void populateSearchParams(SearchParams searchParams,
               PmhRequest request) {
        Collection<SearchConstraint> searchConstraints =
                new ArrayList<SearchConstraint>();
        
        // Parse the 'set' parameter specified.
        String set = request.getSet();

        if (set != null) {
            try {
                searchConstraints.add(setContext.getConstraintForSet(set));
            } catch (IllegalArgumentException iae) {
                
                /*
                 * Should never happen; the argument should have previously
                 * been validated
                 */
                throw new IllegalStateException("Invalid OAI 'set' argument");
            }
        }

        // Parse the from date specified.
        try {
            ParsedDate from = request.getFromDate();

            if (from != null) {
                searchConstraints.add(new ActionDateSC(from.getDate(),
                        ActionDateSC.REQUIRE_ACTION_AFTER));
            }
        } catch (ParseException ex) {
            /*
             * Should never happen; the argument should have previously been
             * validated
             */
            throw new IllegalStateException("Invalid OAI 'from' date");
        }

        // Parse the until date specified.
        try {
            ParsedDate until = request.getUntilDate();

            if (until != null) {
                searchConstraints.add(new ActionDateSC(until.getDate(),
                        ActionDateSC.REQUIRE_ACTION_BEFORE));
            }
        } catch (ParseException ex) {
            /*
             * Should never happen; the argument should have previously been
             * validated
             */
            throw new IllegalStateException("Invalid OAI 'until' date");
        }

        searchParams.addToHeadWithAnd(searchConstraints);
        AuthorizationCheckerBL.blessSearchParams(searchParams, null);
    }

    /**
     * Helper function that fetches samples from core (that match caller-
     * specified search criteria) and sends either sample records or sample
     * headers to the client. The caller should provide the searchId issued
     * by SampleManager when it stored its search previously. The current
     * implementation supports partial responses implicitly and will signal
     * the client that it should resume its request later if necessary.
     * Takes the request info object and sets the number of samples returned
     * to the client for the OAI request. Also records "error code" and
     * "error message" if an error is encountered while servicing the
     * request.
     * 
     * @param  request an {@code PmhRequest} representing the request
     * @param  writer an {@code XmlWriter} to which output should be
     *         directed 
     * @param  searchId the search id issued to the caller in a previous call
     *            to SampleManager.storeSearchParams().
     * @param  startIndex 0-based index that indicates where in the search
     *            results set this response should begin. This value would
     *            be nonzero only if a resumption token was issued to the
     *            client by this method previously.
     * @param  metadataPrefix ignored if {@code headersOnly} is {@code true}.
     *            Otherwise, this prefix is passed to writeRecordTag() to
     *            specify the requested format of the sample record.
     *            
     * @throws InconsistentDbException if a database inconsistency was
     *             detected.
     * @throws OperationFailedException if the operation could not be
     *             completed because of a low-level error.
     * @throws RemoteException on RMI error.
     * @throws IOException if one is encountered
     */
    private void writeSamples(PmhRequest request, XmlWriter writer,
            int searchId, int startIndex, String metadataPrefix)
            throws InconsistentDbException, IOException,
            OperationFailedException, RemoteException {
        String blockName = getVerb();
        MultiTypeCache<Integer> cache = new MultiTypeCache<Integer>();
        SampleInfo[] samples;
        int endIndex;

        // Retrieve a block of search results from core.
        try {
            SampleManagerRemote sampleManager =
                    getCoreConnector().getSampleManager();
            if (maxOaiPmhSamples == 0) {
                // There is no limit on samples per request; fetch as many
                // as possible.
                samples = sampleManager.getSearchResults(searchId);
                endIndex = samples.length;
            } else {
                // Fetch a limited number of samples from core.
                samples = sampleManager.getSearchResults(searchId,
                        startIndex, maxOaiPmhSamples);
                endIndex = Math.min(startIndex + maxOaiPmhSamples,
                        samples.length);
            }
            request.setCountSamplesReturned(endIndex - startIndex);
        } catch (ResourceNotFoundException ex) {

            /*
             * A bad or expired search id -- return an error
             * Note: this could happen on an initial search if the server
             * were extremely busy
             */
            throw new PmhException(PmhError.BAD_RESUMPTION_TOKEN, null);
        }
        if (endIndex == startIndex) {

            // Return an OAI error if no samples match the search criteria.
            throw new PmhException(PmhError.NO_RECORDS_MATCH, null);
        }

        // Write XML to the client.

        // Open the appropriate parent XML element
        writer.openElement(blockName);
        for (int i = startIndex; i < endIndex; i++) {
            if (listRecords) {
                itemUtil.writeRecordTag(
                        writer, samples[i], metadataPrefix, cache);
            } else {
                itemUtil.writeHeaderTag(writer, samples[i]);
            }
        }
        if ((startIndex != 0) || ((samples.length > maxOaiPmhSamples)
                && (maxOaiPmhSamples > 0))) {

            /*
             * Include a <resumptionToken> element because we're using
             * multiple round-trips to service this request.
             */
            writer.openElement("resumptionToken", -1);
            writer.addAttribute(
                    "completeListSize", String.valueOf(samples.length));
            writer.addAttribute("cursor", String.valueOf(startIndex));
            if (endIndex < samples.length) {

                /*
                 * More samples remain in this resultset; issue a
                 * resumptionToken to signal the client that he should
                 * continue. Specify this response's ending index as the
                 * next request's starting index.
                 */
                writer.addText(new SampleResumptionToken(
                        searchId, endIndex, metadataPrefix).toString());
            }
            writer.closeElement();
        }

        // Close the parent XML element
        writer.closeElement(blockName);
    }
}