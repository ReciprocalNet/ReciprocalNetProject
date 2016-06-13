/*
 * Reciprocal Net Project
 * 
 * ItemUtility.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A utility class that handles several common details of interpreting
 * item-level aspects of OAI-PMH requests and writing item-level information
 * into OAI-PMH responses 
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ItemUtility {

    /**
     * OAI-PMH defines the format of a metadata record's "identifier" string
     * to be 'oai:domainname:identifier'. This IDENTIFIER_PREFIX is combined
     * with a sample id to create the required OAI identifier string.
     */
    private static final String IDENTIFIER_PREFIX = "oai:reciprocalnet.org:";

    /**
     * A map from metadata format prefixes to the corresponding metadata format
     * handler objects, used in fulfilling requests
     */
    private final Map<String, MetadataFormat> metadataMap;
    
    /**
     * A {@code CoreConnector} by which this utility can communicate with
     * the Reciprocal Net core
     */
    private final CoreConnector coreConnector;
    
    /**
     * A utility object for parsing and formatting OAI-PMH protocol date
     * strings
     */
    private final DateUtility dateUtility;
    
    /**
     * An {@code SetContext} for use interpreting and creating OAI-PMH
     * set specifications for OAI items handled by this item utility
     */
    private final SetContext setContext;

    /**
     * Initializes a {@code ItemUtility} with the specified set context
     * 
     * @param  metadataMap a {@code Map<String, MetadataFormat>} mapping
     *         the metadata prefixes to be used by this utility to the
     *         metadata format objects implementing the associated metadata
     *         formatting behavior
     * @param  coreConnector a {@code CoreConnector} by which this utility
     *         can communicate with the Reciprocal Net core
     * @param  dateUtility an {@code DateUtility} for use formatting
     *         dates 
     * @param  setContext an {@code SetContext} for use by this item
     *         utility interpreting and creating OAI-PMH set specifications
     *         for OAI items
     */
    public ItemUtility(Map<String, MetadataFormat> metadataMap,
            CoreConnector coreConnector, DateUtility dateUtility,
            SetContext setContext) {
        this.metadataMap = Collections.unmodifiableMap(
                new HashMap<String, MetadataFormat>(metadataMap));
        this.coreConnector = coreConnector;
        this.dateUtility = dateUtility;
        this.setContext = setContext;
    }

    /**
     * Writes OAI "record" information for a single sample; used in
     * formulating GetRecord responses. Output produced on the writer
     * depends on the metadata format requested by the caller.
     * 
     * @param  writer an {@code XmlWriter} with which to prepare the response
     * @param  sample the {@code SampleInfo} for which a metadata record is
     *            requested
     * @param  metadataPrefix the metadata prefix identifying the requested
     *         metadata format
     * @param  cache a {@code MultiTypeCache<Integer>} used for look-aside
     *            caching of {@code LabInfo}, {@code ProviderInfo} and
     *            other objects across multiple invocations of this method
     *            (and potentially others); effective use of this cache can
     *            greatly improve performance.
     * 
     * @throws IllegalArgumentException if the specified metadata prefix is not
     *         supported
     * @throws OperationFailedException if the operation could not be
     *         completed because of a low-level error.
     * @throws RemoteException in case of RMI error.
     * @throws IOException if one is encountered while writing the record
     *         data
     */
    public void writeRecordTag(XmlWriter writer, SampleInfo sample,
            String metadataPrefix, MultiTypeCache<Integer> cache)
            throws OperationFailedException, RemoteException, IOException {
        MetadataFormat format = metadataMap.get(metadataPrefix);

        if (format == null) {
            
            /*
             * The client is responsible for ensuring that this does not
             * happen
             */
            throw new IllegalArgumentException(
                    "The request specifies an unsupported metadata format");
        }
        
        writer.openElement("record");
        writeHeaderTag(writer, sample);
        writer.openElement("metadata");
        format.writeSampleMetadata(writer, sample, cache);
        writer.closeElement("metadata");
        writer.closeElement("record");
    }

    /**
     * Writes OAI "header" information for a single sample; used in
     * formulating some responses.
     * 
     * @param writer an {@code XmlWriter} with which to write the output
     * @param sampleInfo the {@code SampleInfo} from which the header should
     *            be built
     */
    public void writeHeaderTag(XmlWriter writer, SampleInfo sampleInfo) {
        writer.openElement("header");
        if (SampleWorkflowBL.isRetractedStatusCode(sampleInfo.status)) {
            // Special handling if the sample's status indicates it's been
            // "deleted", in the OAI sense.
            writer.addAttribute("status", "deleted");
        }

        writer.openElement("identifier", -1);
        writer.addText(IDENTIFIER_PREFIX);
        writer.addText(String.valueOf(sampleInfo.id));
        writer.closeElement();

        writer.openElement("datestamp", -1);
        writer.addText(dateUtility.formatDate(sampleInfo.lastActionDate));
        writer.closeElement();

        for (String setSpec : setContext.getSetSpecsForSample(sampleInfo)) {
            writer.openElement("setSpec", -1);
            writer.addText(setSpec);
            writer.closeElement();
        }

        writer.closeElement();
    }

    /**
     * Extracts a sample ID from the specified OAI identifier (if possible)
     * and consults {@code SampleManger} to verify that it refers to an
     * existing, visible sample. Returns the ID if it's OK, otherwise
     * {@code SampleInfo.INVALID_SAMPLE_ID}
     * 
     * @param oaiIdentifier the OAI identifier to interpret
     * @return the Reciprocal Net sample ID corresponding to the specified
     *         identifier if there is one and it is visible, otherwise
     *         {@code SampleInfo.INVALID_SAMPLE_ID}
     * @throws OperationFailedException if the operation could not be
     *             completed because of a low-level error.
     * @throws RemoteException on RMI error.
     */
    public int getSampleId(String oaiIdentifier)
            throws OperationFailedException, RemoteException {
        int sampleId;

        try {
            // Extract the numeric sample id from the OAI string identifier.
            sampleId = Integer.parseInt(
                    oaiIdentifier.substring(IDENTIFIER_PREFIX.length()));
        } catch (IndexOutOfBoundsException ioobe) {
            // the identifier is not valid
            return SampleInfo.INVALID_SAMPLE_ID;
        } catch (NumberFormatException ru) {
            // the identifier is not parsable
            return SampleInfo.INVALID_SAMPLE_ID;
        }

        // Verify the id with Sample Manager.
        return (coreConnector.getSampleManager().verifySampleNumber(
                sampleId) ? sampleId : SampleInfo.INVALID_SAMPLE_ID);
    }
    
    /**
     * Determines whether this item utility supports the specified OAI-PMH
     * metadata prefix
     * 
     * @param  prefix the metadata prefix to which the inquiry pertains
     * 
     * @return {@code true} if the prefix is supported, {@code false} if not
     */
    public boolean isSupportingMetadataPrefix(String prefix) {
        return metadataMap.containsKey(prefix);
    }
    
    /**
     * Returns a collection of the metadata format objects supported by this
     * item utility
     * 
     * @return a {@code Collection<MetadataFormat>} of the metadata
     *         format objects supported by this utility.  Users should not
     *         attempt to modify this collection, but may iterate over it
     *         without concern for thread safety or concurrent modification
     */
    public Collection<MetadataFormat> getMetadataFormats() {
        return metadataMap.values();
    }
}