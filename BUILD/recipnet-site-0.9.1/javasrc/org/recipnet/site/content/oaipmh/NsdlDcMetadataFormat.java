/*
 * Reciprocal Net Project
 *
 * NsdlDcMetadataFormat.java
 *
 * 19-Oct-2005: jobollin wrote first draft based on OaiDcMetadataFormat
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * An {@code MetadataFormat} implementation representing NSDL's flavor of
 * qualified Dublin Core metadata in XML.  The actual metadata provided by this
 * format is generally the same as that provided by the oai_dc format, but it
 * is presented via refined elements from the qualified dublin core element
 * set where possible, and tagged with much more type and encoding information.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class NsdlDcMetadataFormat extends BaseMetadataFormat {

    /** The namespace URI for the oai_dc schema */
    private final static URI NAMESPACE_URI =
            URI.create("http://ns.nsdl.org/nsdl_dc_v1.02/");
    
    /** The URL for this format's XML schema */
    private final static URL SCHEMA_URL;

    /**
     * The version string of the nsdl_dc schema version against which this
     * metadata format validates
     */
    private final static String SCHEMA_VERSION = "1.02.010";
    
    /**
     * Initialize the schema URL, eating the the MalformedURLException that
     * we know can't really happen anyway.
     */
    static {
        URL tempUrl;
        
        try {
            tempUrl = new URL(
                    "http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd");
        } catch (MalformedURLException mue) {
            
            /*
             * This should never happen because the URL String in the above
             * constructor is well-formed
             */
            tempUrl = null;
        }
        SCHEMA_URL = tempUrl;
    }

    /**
     * The {@code DateUtility} this metadata format will use to format dates
     */
    private final DateUtility dateUtility;
    
    /**
     * The {@code CoreConnector} this metadata format will use to interact with
     * the Reciprocal Net core
     */
    private final CoreConnector coreConnector;
    
    /**
     * The {@code LanguageHelper} this metadata format will use to localize
     * metadata
     */
    private final LanguageHelper languageHelper;
    
    /**
     * Initializes a {@code OaiDcMetadataFormat} with the specified parameters
     * 
     * @param  dateUtil an {@code DateUtility} with which to format dates
     * @param  connector a {@code CoreConnector} with which to access the
     *         Reciprocal Net core modules
     * @param  languageHelper a {@code LanguageHelper} with which to
     *         internationalize messages
     */
    public NsdlDcMetadataFormat(DateUtility dateUtil, CoreConnector connector,
            LanguageHelper languageHelper) {
        super("nsdl_dc", SCHEMA_URL, NAMESPACE_URI);
        dateUtility = dateUtil;
        coreConnector = connector;
        this.languageHelper = languageHelper;
    }

    /**
     * {@inheritDoc}.  This version writes Qualified Dublin Core metadata in the
     * NSDL's defined format.
     * 
     * @see MetadataFormat#writeSampleMetadata(XmlWriter, SampleInfo, MultiTypeCache)
     */
    @SuppressWarnings("unchecked")
    public void writeSampleMetadata(XmlWriter writer, SampleInfo sample,
            MultiTypeCache<Integer> cache)
            throws IOException, OperationFailedException {
        
        /*
         * The Locale by which to get field name strings out of language helper.
         * A particular Locale is specified that corresponds to the metadata
         * language specified within the metadata.  
         */
        Collection<Locale> locales =
                Collections.singleton(new Locale("en", "US"));
        boolean hasExplanation = false;
        SiteManagerRemote siteManager = coreConnector.getSiteManager();

        /*
         * Look up the LabInfo, ProviderInfo, and SiteInfo records associated
         * with this sample. Retrieve these from the caller-provided cache if
         * possible. If they must be fetched from the core, store them in the
         * cache for use next time.
         */
        LabInfo lab = cache.get(sample.labId, LabInfo.class);
        if (lab == null) {
            lab = siteManager.getLabInfo(sample.labId);
            cache.put(lab.id, LabInfo.class, lab);
        }
        ProviderInfo provider =
                cache.get(sample.dataInfo.providerId, ProviderInfo.class);
        if (provider == null) {
            provider = siteManager.getProviderInfo(sample.dataInfo.providerId);
            cache.put(provider.id, ProviderInfo.class, provider);
        }
        SiteInfo homeSite = cache.get(lab.homeSiteId, SiteInfo.class);
        if (homeSite == null) {
            homeSite = siteManager.getSiteInfo(lab.homeSiteId);
            cache.put(homeSite.id, SiteInfo.class, homeSite);
        }

        // Start writing
        writer.openElement("nsdl_dc:nsdl_dc");
        writer.addAttribute("xmlns:nsdl_dc", getNamespaceUri().toString());
        writer.addAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        writer.addAttribute("xmlns:dct", "http://purl.org/dc/terms/");
        writer.addAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        writer.addAttribute("xsi:schemaLocation",
                getNamespaceUri().toString() + ' ' + getSchemaUrl());
        writer.addAttribute("schemaVersion", SCHEMA_VERSION);
        
        // Add a title bearing the best available name of the compound
        writer.openElement("dc:title", -1);
        writer.addText("Molecular Structure of ");
        writer.addText(getPreferredName(sample, lab));
        writer.closeElement();
        
        // Add any layman's explanation(s) as a <dc:description>
        for (Iterator it = sample.annotationInfo.iterator(); it.hasNext(); ) {
            SampleAnnotationInfo anno = (SampleAnnotationInfo) it.next();

            if (anno.type == SampleTextBL.LAYMANS_EXPLANATION) {
                writer.openElement("dc:description", -1);
                writer.addText(anno.value);
                writer.closeElement();
                
                hasExplanation = true;
            }
        }
        
        /*
         * Add resource types with use of the DCMI controlled type vocabulary.
         * We have to fudge a bit on the resource types because we can't
         * determine what files are available for samples without any local
         * holdings.  We assume that everything has a CIF and CRT, and label
         * items "Dataset" on the basis of the former and "Interactive Resource"
         * and "Image" on the basis of the latter.
         * 
         * TODO: Test for the presence of the CIF and CRT files and use the
         * results to tag this item with some or all of these types 
         */
        writer.openElement("dc:type", -1);
        writer.addAttribute("xsi:type", "dct:DCMIType");
        writer.addText("Dataset");
        writer.closeElement();
        writer.openElement("dc:type", -1);
        writer.addAttribute("xsi:type", "dct:DCMIType");
        writer.addText("InteractiveResource");
        writer.closeElement();
        writer.openElement("dc:type", -1);
        writer.addAttribute("xsi:type", "dct:DCMIType");
        writer.addText("Image");
        writer.closeElement();
        
        /*
         * We furthermore assign type "Text" to anything bearing a layman's
         * explanation.
         */
        if (hasExplanation) {
            writer.openElement("dc:type", -1);
            writer.addAttribute("xsi:type", "dct:DCMIType");
            writer.addText("Text");
            writer.closeElement();
        }
        
        /*
         * Add the various timestamps available, using appropriate element
         * refinements and specifying the format appropriately
         */
        writer.openElement("dct:created", -1);
        writer.addAttribute("xsi:type", "dct:ISO8601");
        writer.addText(dateUtility.formatDate(sample.firstActionDate));
        writer.closeElement();
        writer.openElement("dct:issued", -1);
        writer.addAttribute("xsi:type", "dct:ISO8601");
        writer.addText(dateUtility.formatDate(sample.releaseActionDate));
        writer.closeElement();
        writer.openElement("dct:modified", -1);
        writer.addAttribute("xsi:type", "dct:ISO8601");
        writer.addText(dateUtility.formatDate(sample.lastActionDate));
        writer.closeElement();

        // Add a standard subject using one of NSDL's controlled vocabularies 
        writer.openElement("dc:subject", -1);
        writer.addAttribute("xsi:type", "nsdl_dc:GEM");
        writer.addText("Chemistry");
        writer.closeElement();
        
        /*
         * Turn KEYWORD attributes into <dc:subject> elements.
         * 
         * TODO: if we ever adopt a controlled vocabulary for keywords, we
         * should tag these subject elements with a corresponding type
         */
        for (Iterator it = sample.attributeInfo.iterator(); it.hasNext(); ) {
            SampleAttributeInfo attr = (SampleAttributeInfo) it.next();

            if (attr.type == SampleTextBL.KEYWORD) {
                writer.openElement("dc:subject", -1);
                writer.addText(attr.value);
                writer.closeElement();
            }
        }

        // Specify the lab and provider as <dc:contributor>s.
        writer.openElement("dc:contributor", -1);
        writer.addText(lab.name);
        writer.closeElement();
        writer.openElement("dc:contributor", -1);
        writer.addText(provider.name);
        writer.closeElement();

        // Turn CRYSTALLOGRAPHER_NAME and SAMPLE_PROVIDER_NAME attributes into
        // <db:contributor> elements.
        for (Iterator it = sample.attributeInfo.iterator(); it.hasNext(); ) {
            SampleAttributeInfo attr = (SampleAttributeInfo) it.next();

            if ((attr.type == SampleTextBL.CRYSTALLOGRAPHER_NAME)
                    || (attr.type == SampleTextBL.SAMPLE_PROVIDER_NAME)){
                writer.openElement("dc:contributor", -1);
                writer.addText(attr.value);
                writer.closeElement();
            }
        }

        // Specify the sample id as a <dc:identifier>
        writer.openElement("dc:identifier", -1);
        writer.addText("Reciprocal Net sample id ");
        writer.addText(String.valueOf(sample.id));
        writer.closeElement();
        
        // Specify the (Lab, Local lab id) pair as a <dc:identifier> 
        writer.openElement("dc:identifier", -1);
        writer.addText(lab.name);
        writer.addText(" Sample ");
        writer.addText(sample.localLabId);
        writer.closeElement();
        
        /*
         * Specify the sample's showsamplebasic URL as a <dc:identifier> of
         * suitable declared type
         */
        writer.openElement("dc:identifier", -1);
        writer.addAttribute("xsi:type", "dct:URI");
        writer.addText(homeSite.baseUrl);
        writer.addText("showsample.jsp?sampleId=");
        writer.addText(String.valueOf(sample.id));
        writer.closeElement();

        /*
         * Specify English as the <dc:language>, with an appropriate type
         * qualifier
         */
        writer.openElement("dc:language", -1);
        writer.addAttribute("xsi:type", "dct:RFC3066");
        writer.addText("en");
        writer.closeElement();
        
        /*
         * Turn CSD_REFCODE, ICSD_COLLECTION_CODE, PDB_ENTRY_NUMBER,
         * and CAS_REGISTRY_NUMBER attributes into <dc:relation> elements.
         */
        for (Iterator it = sample.attributeInfo.iterator(); it.hasNext(); ) {
            SampleAttributeInfo attr = (SampleAttributeInfo) it.next();

            if ((attr.type == SampleTextBL.CSD_REFCODE)
                    || (attr.type == SampleTextBL.ICSD_COLLECTION_CODE)
                    || (attr.type == SampleTextBL.PDB_ENTRY_NUMBER)
                    || (attr.type == SampleTextBL.CAS_REGISTRY_NUMBER)) {
                writer.openElement("dc:relation", -1);
                writer.addText(this.languageHelper.getFieldString(attr.type,
                        Collections.enumeration(locales), false));
                writer.addText(":");
                writer.addText(attr.value);
                writer.closeElement();
            }
        }

        /*
         * Turn CITATION_OF_A_PUBLICATION annotations into <dct:isReferencedBy>
         * elements.
         */
        for (Iterator it = sample.annotationInfo.iterator(); it.hasNext(); ) {
            SampleAnnotationInfo anno = (SampleAnnotationInfo) it.next();

            if (anno.type == SampleTextBL.CITATION_OF_A_PUBLICATION) {
                writer.openElement("dct:isReferencedBy", -1);
                writer.addText(anno.value);
                writer.closeElement();
            }
        }

        // Finish the sample record.
        writer.closeElement("nsdl_dc:nsdl_dc");
    }
}
