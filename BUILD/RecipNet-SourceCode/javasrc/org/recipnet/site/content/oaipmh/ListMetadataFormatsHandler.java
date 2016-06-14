/*
 * Reciprocal Net Project
 * 
 * ListMetadataFormatsHandler.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.rmi.RemoteException;

import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.validation.URIValidator;

/**
 * A VerbHandler that handles the "ListMetadataFormats" verb by verifying
 * that a corresponding record exists in core, and returning the list of
 * formats (the same for all existing records) if it does.  The requests
 * handled by this handler may optionally bear an "identifier" argument
 * (which then must be the correct identifier for an accessible sample).
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ListMetadataFormatsHandler extends VerbHandler {

    /**
     * An {@code ItemUtility} for use interpreting item identifiers and
     * producing item-level OAI-PMH information
     */
    private final ItemUtility itemUtil;
    
    /**
     * Initializes a {@code ListMetadataFormatsHandler} with the specified
     * OAI item utility
     * 
     * @param  itemUtility an {@code ItemUtility} for this handler's use
     *         interpreting item identifiers and producing item-level
     *         OAI-PMH information
     */
    public ListMetadataFormatsHandler(ItemUtility itemUtility) {
        
        /* This handler does not need to (directly) access core */
        
        super("ListMetadataFormats", null);
        this.itemUtil = itemUtility;
        supportArgument("identifier", new URIValidator());
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see VerbHandler#handleRequest(PmhRequest, XmlWriter)
     */
    @Override
    public void handleRequest(PmhRequest request, XmlWriter writer)
            throws OperationFailedException, RemoteException {
        
        /*
         * argument validation assumed to have already been performed via
         * validateArguments(RequestContext)
         */

        String identifier = request.getIdentifier();

        /*
         * If an identifier was specified then it must refer to an existing
         * sample
         */
        if ((identifier != null) && (itemUtil.getSampleId(identifier)
                == SampleInfo.INVALID_SAMPLE_ID)) {
            throw new PmhException(PmhError.ID_DOES_NOT_EXIST, "'"
                    + identifier + "'.");
        } else {
            
            // Generate the response.  The strings hard-coded here conform
            // to the OAI spec.
            writer.openElement("ListMetadataFormats");
            
            for (MetadataFormat format : itemUtil.getMetadataFormats()) {
                writer.openElement("metadataFormat");
                writer.openElement("metadataPrefix", -1);
                writer.addText(format.getPrefix());
                writer.closeElement();
                writer.openElement("schema", -1);
                writer.addText(format.getSchemaUrl().toString());
                writer.closeElement();
                writer.openElement("metadataNamespace", -1);
                writer.addText(format.getNamespaceUri().toString());
                writer.closeElement();
                writer.closeElement();
            }
            
            writer.closeElement("ListMetadataFormats");
        }
    }
}
