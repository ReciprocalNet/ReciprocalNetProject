/*
 * Reciprocal Net Project
 *
 * OaiDcMetadataFormat.java
 *
 * 13-Oct-2005: jobollin wrote first draft, with reference to OaiPmhResponder
 * 19-Oct-2005: jobollin fixed the namespace URI (previously the URI for
 *              the OAI-PMH namespace had been specified)
 * 19-Oct-2005: jobollin added a <dc:identifier> specifying the sample's
 *              showsample basic view URL at its home site
 * 19-Oct-2005: jobollin improved the content of the <dc:title> element per
 *              NSDL's request / recommendation    
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
 * An {@code MetadataFormat} implementation representing (unqualified) Dublin
 * Core metadata in XML
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class OaiDcMetadataFormat extends BaseMetadataFormat {
    
    /** The namespace URI for the oai_dc schema */
    private final static URI NAMESPACE_URI =
            URI.create("http://www.openarchives.org/OAI/2.0/oai_dc/");
    
    /** The URL for this format's XML schema */
    private final static URL SCHEMA_URL;
    
    /**
     * Initialize the schema URL, eating the the MalformedURLException that
     * we know can't really happen anyway.
     */
    static {
        URL tempUrl;
        
        try {
            tempUrl = new URL("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
        } catch (MalformedURLException mue) {
            
            /*
             * This should never happen because the URL String in the above
             * constructor is valid
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
     *         obtain metadata description text messages
     */
    public OaiDcMetadataFormat(DateUtility dateUtil, CoreConnector connector,
            LanguageHelper languageHelper) {
        super("oai_dc", SCHEMA_URL, NAMESPACE_URI);
        dateUtility = dateUtil;
        coreConnector = connector;
        this.languageHelper = languageHelper;
    }

    /**
     * {@inheritDoc}.  This version writes Dublin Core metadata in XML format.
     * 
     * @see MetadataFormat#writeSampleMetadata(XmlWriter, SampleInfo, MultiTypeCache)
     */
    @SuppressWarnings("unchecked")
    public void writeSampleMetadata(XmlWriter writer, SampleInfo sample,
            MultiTypeCache<Integer> cache)
            throws IOException, OperationFailedException {
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
        
        /*
         * The en-US field descriptions are always used in this metadata format
         */
        Collection<Locale> locales =
                Collections.singleton(new Locale("en", "US"));
        boolean hasExplanation = false;

        // Start writing
        writer.openElement("oai_dc:dc");
        writer.addAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        writer.addAttribute("xmlns:oai_dc", getNamespaceUri().toString());
        writer.addAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        writer.addAttribute("xsi:schemaLocation",
                getNamespaceUri().toString() + ' ' + getSchemaUrl());
        
        writer.openElement("dc:title", -1);
        writer.addText("Molecular Structure of ");
        writer.addText(getPreferredName(sample, lab));
        writer.closeElement();
        
        // Add Layman's explanation(s) as a <dc:description>
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
         * We have to fudge a bit on the resource types because we can't
         * determine what files are available for samples without any local
         * holdings.  We assume that everything has a CIF and CRT, and label
         * items "Dataset" on the basis of the former and "Interactive Resource"
         * on the basis of the latter.
         */
        writer.openElement("dc:type", -1);
        writer.addText("Dataset");
        writer.closeElement();
        writer.openElement("dc:type", -1);
        writer.addText("Interactive Resource");
        writer.closeElement();
        
        /*
         * We furthermore assign type "Text" to anything bearing a layman's
         * explanation.
         */
        if (hasExplanation) {
            writer.openElement("dc:type", -1);
            writer.addText("Text");
            writer.closeElement();
        }
        
        writer.openElement("dc:date", -1);
        writer.addText(dateUtility.formatDate(sample.firstActionDate));
        writer.closeElement();
        writer.openElement("dc:date", -1);
        writer.addText(dateUtility.formatDate(sample.lastActionDate));
        writer.closeElement();

        // Turn KEYWORD attributes into <dc:subject> elements.
        for (Iterator it = sample.attributeInfo.iterator(); it.hasNext(); ) {
            SampleAttributeInfo attr = (SampleAttributeInfo) it.next();

            if (attr.type == SampleTextBL.KEYWORD) {
                writer.openElement("dc:subject", -1);
                writer.addText(attr.value);
                writer.closeElement();
            }
        }

        /*
         * Specify the lab and provider as <dc:contributor>s.
         * use the lab's full name because the abbrevriated (short) name is
         * unlikely to be meaningful to most consumers of this metadata
         */
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
        writer.addText("Reciprocal Net Sample id ");
        writer.addText(String.valueOf(sample.id));
        writer.closeElement();
        
        // Specify the (Lab, Local lab id) pair as a <dc:identifier> 
        writer.openElement("dc:identifier", -1);
        writer.addText(lab.name);
        writer.addText(" Sample ");
        writer.addText(sample.localLabId);
        writer.closeElement();
        
        /*
         * Specify the show sample URL on the sample's home site as a
         * <dc:identifier>
         */
        writer.openElement("dc:identifier", -1);
        writer.addText(homeSite.baseUrl);
        writer.addText("showsample.jsp?sampleId=");
        writer.addText(String.valueOf(sample.id));
        writer.closeElement();

        /*
         * No URL on the portal is specified unless (above) the portal is the
         * sample's home site.  This is intentional. 
         */
        
        // Specify English as the <dc:language>
        writer.openElement("dc:language", -1);
        writer.addText("en");
        writer.closeElement();
        
        // Turn CSD_REFCODE, ICSD_COLLECTION_CODE, PDB_ENTRY_NUMBER,
        // and CAS_REGISTRY_NUMBER attributes into <dc:relation> elements.
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

        // Turn CITATION_OF_A_PUBLICATION annotations into <dc:relation>
        // elements.
        for (Iterator it = sample.annotationInfo.iterator(); it.hasNext(); ) {
            SampleAnnotationInfo anno = (SampleAnnotationInfo) it.next();

            if (anno.type == SampleTextBL.CITATION_OF_A_PUBLICATION) {
                writer.openElement("dc:relation", -1);
                writer.addText(this.languageHelper.getFieldString(anno.type,
                        Collections.enumeration(locales), false));
                writer.addText(":");
                writer.addText(anno.value);
                writer.closeElement();
            }
        }

        // Write the final part of the sample record.
        writer.closeElement("oai_dc:dc");
    }
}
