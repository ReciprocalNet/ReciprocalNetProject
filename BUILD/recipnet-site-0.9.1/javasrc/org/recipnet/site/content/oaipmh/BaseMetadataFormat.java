/*
 * Reciprocal Net Project
 *
 * BaseMetadataFormat.java
 *
 * 18-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.content.oaipmh;

import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * An abstract base implementation of {@code MetadataFormat} intended to make
 * it easier to implement concrete formats
 * 
 * @author jobollin
 * @version 0.9.0
 */
public abstract class BaseMetadataFormat implements MetadataFormat {

    /**
     * The metadata prefix with which to identify this metadata format
     */    
    private final String prefix;

    /**
     * The URL for this metadata format's XML schema
     */
    private final URL url;

    /**
     * The namespace URI identifying this metadata format
     */
    private final URI uri;
    
    /**
     * Initializes a {@code BaseMetadataFormat} with the specified metadata
     * prefix, schemal URL, and namespace URI
     * 
     * @param  prefix the metadata prefix to use to specify this format
     * @param  url a {@code URL} from which the XML schema for this format can
     *         be obtained
     * @param  uri the namespace {@code URI} for this format
     */
    public BaseMetadataFormat(String prefix, URL url, URI uri) {
        this.prefix = prefix;
        this.url = url;
        this.uri = uri;
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetadataFormat#getPrefix()
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetadataFormat#getSchemaUrl()
     */
    public URL getSchemaUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     * 
     * @see MetadataFormat#getNamespaceUri()
     */
    public URI getNamespaceUri() {
        return uri;
    }

    /**
     * Finds and returns the preferred name for the specified sample for use in
     * a title for the element, such as in a <dc:title> element in Dublin Core
     * metadata.  SampleTextBL's methods for selecting explictly preferred
     * names, implicitly preferred names, and preferred formulae are
     * used as required, with the Lab code / local lab id as a final fallback.
     * 
     * @param  sample a {@code SampleInfo} representing the sample for which a
     *         name is desired
     * @param  lab a {@code LabInfo} representing the lab that originate the
     *         sample
     *         
     * @return a non-{@code null} string representation of the best available
     *         name for the specified sample 
     */
    protected String getPreferredName(SampleInfo sample, LabInfo lab) {
        SampleTextInfo nameInfo =
                SampleTextBL.getExplicitlyPreferredName(sample);
    
        if (nameInfo == null) {
            Collection<? extends SampleTextInfo> names =
                    SampleTextBL.getSampleNames(sample, true);
            
            nameInfo = (names.isEmpty()
                    ? SampleTextBL.getSamplePreferredFormula(sample)
                    : names.iterator().next());
        }
        
        /*
         * Fall back on the Lab name / local ID; use the lab's full name because
         * the abbrevriated (short) name is unlikely to be meaningful to most
         * consumers of metadata containing the resulting name
         */
        return (nameInfo == null)
                ? (lab.name + " sample " + sample.localLabId)
                : nameInfo.value;
    }
}
