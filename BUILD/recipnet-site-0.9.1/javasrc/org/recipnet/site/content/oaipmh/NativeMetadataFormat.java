/*
 * Reciprocal Net Project
 *
 * NativeMetadataFormat.java
 *
 * 13-Oct-2005: jobollin wrote first draft, with reference to OaiPmhResponder
 * 19-Oct-2005: jobollin removed use of a ResourceBundle to localize the
 *              response -- only the markup was ever localized, so it was
 *              pointless; moreover, the oai_dc format never did localize
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import org.recipnet.common.MultiTypeCache;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.LanguageHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An {@code MetadataFormat} implementation representing Reciprocal Net
 * native metadata in XML
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class NativeMetadataFormat extends BaseMetadataFormat {

    /** The namespace URI for the oai_dc schema */
    private final static URI NAMESPACE_URI =
            URI.create("http://www.reciprocalnet.org/master/");
    
    /** The URL for this format's XML schema */
    private final static URL SCHEMA_URL;
    
    /**
     * Initialize the schema URL, eating the the MalformedURLException that
     * we know can't really happen anyway.
     */
    static {
        URL tempUrl;
        
        try {
            tempUrl = new URL(
                    "http://www.reciprocalnet.org/master/native_xml.xsd");
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
     * The {@code LanguageHelper} this metadata format will use to localize
     * metadata
     */
    private final LanguageHelper languageHelper;
    
    /**
     * Initializes a {@code NativeMetadataFormat} with default properties
     * 
     * @param  languageHelper a {@code LanguageHelper} with which to
     *         obtain metadata description text messages
     */
    public NativeMetadataFormat(LanguageHelper languageHelper) {
        super("native_xml", SCHEMA_URL, NAMESPACE_URI);
        this.languageHelper = languageHelper;
    }

    /**
     * {@inheritDoc}.  This version writes Dublin Core metadata in XML format.
     * 
     * @see MetadataFormat#writeSampleMetadata(XmlWriter, SampleInfo, MultiTypeCache)
     */
    public void writeSampleMetadata(XmlWriter writer, SampleInfo sample,
            @SuppressWarnings("unused") MultiTypeCache<Integer> cache)
            throws IOException, OperationFailedException {
        
        // Use the DomUtil to generate an XML document.
        Enumeration<Locale> locales = Collections.enumeration(
                Collections.singleton(new Locale("en", "US")));
        Document sampleDoc = DomUtil.objectToDomTreeUsingResources(sample,
                languageHelper.getSiteStringBundle(locales, false));
        NodeList nodes = sampleDoc.getElementsByTagName("sample");
        Node sampleElement;
        
        // The tree should contain a single <sample> element.
        if (nodes.getLength() < 1) {
            throw new IllegalArgumentException("No <sample> present");
        } else if (nodes.getLength() > 1) {
            throw new IllegalArgumentException("Multiple <sample>s present");
        } else {
            sampleElement = nodes.item(0);
        }
        
        /*
         * The <sample> element produced by DomUtil needs to be patched up a
         * bit, so we can't just send it to writeElement()
         */
        writer.openElement("rn:sample");
        writer.addAttribute("xmlns", "");
        writer.addAttribute("xmlns:rn", getNamespaceUri().toString());
        writer.addAttribute("xsi:schemaLocation",
                getNamespaceUri().toString() + ' ' + getSchemaUrl());
        writeAttributes(sampleElement, writer);
        writeChildren(sampleElement, writer);
        writer.closeElement();
    }

    /**
     * Writes the attributes of the specified DOM element {@code Node} to the
     * provided {@code XmlWriter}; this is sensible when a corresponding element
     * has been opened on the writer but not yet had any content added to it
     * 
     * @param  element the {@code Node} from which the attributes should be
     *         drawn
     * @param  writer the {@code XmlWriter} to which the attributes should be
     *         written
     */
    private void writeAttributes(Node element, XmlWriter writer) {
        NamedNodeMap attributes = element.getAttributes(); 
        
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attribute = (Attr) attributes.item(i);
            
            writer.addAttribute(attribute.getName(), attribute.getValue());
        }
    }

    /**
     * Recursively writes the element, text, and cdata section children of the
     * specified element {@code Node} to the specified {@code XmlWriter}; this
     * is sensible when a corresponding element is open on the writer and is
     * the current node
     * 
     * @param  element the {@code Node} from which the children should be
     *         written
     * @param  writer the {@code XmlWriter} to which the data should be written
     */
    private void writeChildren(Node element, XmlWriter writer) {
        NodeList children = element.getChildNodes();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    writeElement(node, writer);
                    break;
                case Node.TEXT_NODE:
                    writer.addText(node.getNodeValue());
                    break;
                case Node.CDATA_SECTION_NODE:
                    writer.addLiteralText("<![CDATA[");
                    writer.addLiteralText(node.getNodeValue());
                    writer.addLiteralText("]]>");
                    break;
            }
        }
    }

    /**
     * Writes the specified element {@code Node} and all its descendants to the
     * specified {@code XmlWriter}; this is sensible at any time, so long as
     * the writer is open and has not closed its topmost element. 
     * 
     * @param  element the element {@code Node} to write
     * @param  writer the {@code XmlWriter} to which the data should be
     *         written
     */
    private void writeElement(Node element, XmlWriter writer) {
        NodeList children = element.getChildNodes();
        boolean characterChildren = false;
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            
            if ((child.getNodeType() == Node.CDATA_SECTION_NODE)
                    || (child.getNodeType() == Node.TEXT_NODE)) {
                characterChildren = true;
                break;
            }
        }
        
        if (characterChildren) {
            writer.openElement(element.getNodeName(), -1);
        } else {
            writer.openElement(element.getNodeName());
        }
        writeAttributes(element, writer);
        writeChildren(element, writer);
        writer.closeElement();
    }
}

