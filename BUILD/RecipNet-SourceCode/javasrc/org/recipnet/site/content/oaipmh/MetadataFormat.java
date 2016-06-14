/*
 * Reciprocal Net Project
 *
 * MetadataFormat.java
 *
 * 10-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * An interface describing pluggable metadata format implementations.
 * Implementation classes can describe their characteristics and format sample
 * data into an appropriate form for inclusion in an OAI-PMH response
 * 
 * @author jobollin
 * @version 0.9.0
 */
public interface MetadataFormat {
    
    /**
     * Returns the metadata prefix used by this metadata format
     *  
     * @return the metadata prefix for this format
     */
    String getPrefix();
    
    /**
     * Returns the URL of the XML metadata schema used by this metadata format
     *  
     * @return the metadata schema URL for this format
     */
    URL getSchemaUrl();
    
    /**
     * Returns the URI of the XML namespace used by this metadata format
     *  
     * @return the metadata schema namespace URI for this format
     */
    URI getNamespaceUri();
    
    /**
     * Produces a metadata record corresponding to the specified sample with use
     * of the provided writer.  The supplied {@code Map} can be used to cache
     * objects for use by the current or a future invocation of this method.
     * 
     * @param  writer the {@code XmlWriter} rto which to direct the output
     * @param  sample the {@code SampleInfo} from which to derive metadata
     * @param  cache a {@code MultiTypeCache<Integer>} in which this method may
     *         choose to cache objects for the current or a future invocation of
     *         this method.  Implementations should not assume the presence of
     *         particular objects, but can make use of the fact that this same
     *         map may be passed to multiple invocations of this method to
     *         reduce the overall number of expensive computations and lookups
     *         that need to be performed
     * @throws IOException if one is encountered
     * @throws OperationFailedException if the operation could not be completed
     *         because of a low-level error.
     */
    void writeSampleMetadata(XmlWriter writer, SampleInfo sample,
            MultiTypeCache<Integer> cache)
            throws IOException, OperationFailedException;
}
