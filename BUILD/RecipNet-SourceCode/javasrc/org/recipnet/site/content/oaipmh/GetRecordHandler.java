/*
 * Reciprocal Net Project
 * 
 * GetRecordHandler.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.rmi.RemoteException;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.validation.OaiPmhMetadataPrefixValidator;
import org.recipnet.site.shared.validation.URIValidator;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A VerbHandler that handles the "GetRecord" verb by fetching a
 * corresponding record from core and writing a corresponding metadata record
 * (or by generating an appropriate error response if that isn't possible).  The
 * requests handled by this handler should have valid "identifier" and
 * "metadataPrefix" arguments, and no others.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class GetRecordHandler extends VerbHandler {

    /**
     * An {@code ItemUtility} for use interpreting item identifiers and
     * producing item-level OAI-PMH information
     */
    private final ItemUtility itemUtil;
    
    /**
     * Initializes a {@code GetRecordHandler} with default parameters
     * 
     * @param  connector a {@code CoreConnector} with which to retrieve
     *         information from the Reciprocal Net core
     * @param  itemUtility an {@code ItemUtility} for this handler's use
     *         interpreting item identifiers and producing item-level
     *         OAI-PMH information
     */
    public GetRecordHandler(CoreConnector connector,
            ItemUtility itemUtility) {
        super("GetRecord", connector);
        this.itemUtil = itemUtility;
        supportArgument("identifier", new URIValidator());
        supportArgument("metadataPrefix", new OaiPmhMetadataPrefixValidator());
    }
    
    /**
     * Validates the well-formedness of the arguments to this verb handler.
     * This implementation requires the "identifier" argument and the
     * "metadataPrefix" argument to be present, and no others. 
     * 
     * @param  request an {@code PmhRequest} representing the request
     * 
     * @throws PmhException if the request's arguments are invalid
     * 
     * @see VerbHandler#validateRequest(PmhRequest)
     */
    @Override
    public void validateRequest(PmhRequest request) throws PmhException {
        if (request.getIdentifier() == null) {
            throw new PmhException(PmhError.BAD_ARGUMENT,
                    "'identifier' argument required for this OAI verb.");
        } else if (request.getMetadataPrefix() == null) {
            throw new PmhException(PmhError.BAD_ARGUMENT,
                    "'metadataPrefix' argument required for this OAI verb.");
        } else {
            super.validateRequest(request);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see VerbHandler#handleRequest(PmhRequest, XmlWriter)
     */
    @Override
    public void handleRequest(PmhRequest request, XmlWriter writer)
            throws RemoteException, IOException, OperationFailedException,
            InconsistentDbException {
        String identifier = request.getIdentifier();
        String prefix = request.getMetadataPrefix();
        int sampleId;
        SampleInfo sample;
        
        /*
         * Most argument validation assumed to have already been performed
         * via validateArguments(RequestContext)
         */

        // Verify that the metadata format requested is in fact supported
        if (!itemUtil.isSupportingMetadataPrefix(prefix)) {
            throw new PmhException(PmhError.CANNOT_DISSEMINATE_FORMAT,
                    "'" + request.getMetadataPrefix()
                    + "' not supported by this site.");
        }
        
        // Extract the sample id from the OAI identifier string and make
        // sure it refers to an existing sample
        sampleId = itemUtil.getSampleId(identifier);
        if (sampleId == SampleInfo.INVALID_SAMPLE_ID) {
            throw new PmhException(PmhError.ID_DOES_NOT_EXIST,
                    "'" + identifier + "'.");
        }
        
        // Fetch the requested sample from core.
        try {
            SampleManagerRemote sampleManager =
                    getCoreConnector().getSampleManager();
            
            sample = sampleManager.getSampleInfo(sampleId);
        } catch (ResourceNotFoundException ex) {
            
            // id not known (should not happen -- we already checked the ID)
            throw new PmhException(PmhError.ID_DOES_NOT_EXIST,
                    "'" + identifier + "'.");
        }

        // Verify that unauthenticated users may access this sample.
        if (!AuthorizationCheckerBL.canSeeSample(null, sample)) {
            
            // return exactly the same error code as if the sample didn't exist
            throw new PmhException(PmhError.ID_DOES_NOT_EXIST,
                    "'" + identifier + "'.");
        }

        // Generate the response.
        // The strings hard-coded here conform to the OAI-PMH spec.
        writer.openElement("GetRecord");
        itemUtil.writeRecordTag(
                writer, sample, prefix, new MultiTypeCache<Integer>());
        writer.closeElement("GetRecord");
        request.setCountSamplesReturned(1);
    }
}
