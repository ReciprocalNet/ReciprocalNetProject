/*
 * Reciprocal Net project
 * 
 * SoapUtil.java
 *
 * 07-Oct-2002: ekoperda wrote first draft
 * 13-May-2003: midurbin fixed bug #817 in readEntireInputStream()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.shared
 * 14-Dec-2005: ekoperda replaced encodeSoapDocument() with
 *              writeSoapDocument(), dropHeaderOnMessageDocument() with
 *              dropXmlDocumentHeader(), getMessageStringArray() with
 *              extractFragmentsFromXmlDocument(), and removed 
 *              countMessageElements()
 * 01-Jun-2006: jobollin reformatted the docs
 */

package org.recipnet.site.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Contains various utility functions useful when dealing with SOAP messages.
 * The Simple Object Access Protocol is an XML-based specification for web
 * services.
 */
public class SoapUtil {
    /**
     * Writes a complete SOAP envelope and body to a caller-supplied character
     * sink. The caller supplies the body as an array of strings; each is echoed
     * sequentially without any translation or delimiters. See also the
     * {@code decodeSoapDocument()} function on this class, which is this
     * function's counterpart.
     * 
     * @param w the {@code Writer} to which this method should send characters.
     *        This writer is assumed to implement UTF-8 character encoding.
     * @param documentBodies an array of zero or more strings, each of which is
     *        assumed to contain XML fragments. This method echoes these strings
     *        to the writer verbatim, one after the next. These document bodies
     *        must not contain XML document headers.
     * @param localName the SOAP "function" to be invoked.
     * @param prefix the XML element name prefix to use in the SOAP header.
     * @param namespaceUri the XML namespace to be referenced in the SOAP
     *        header.
     * @throws IOException if an error was encountered while sending characters
     *         to the {@code Writer}.
     */
    public static void writeSoapDocument(Writer w, String documentBodies[],
            String localName, String prefix, String namespaceUri)
            throws IOException {
        w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        w.write("<soap-env:Envelope xmlns:soap-env=\"");
        w.write("http://schemas.xmlsoap.org/soap/envelope/\">");
        w.write("<soap-env:Header/>");
        w.write("<soap-env:Body>");
        w.write("<");
        w.write(prefix);
        w.write(":");
        w.write(localName);
        w.write(" xmlns:");
        w.write(prefix);
        w.write("=\"");
        w.write(namespaceUri);
        w.write("\">");
        for (String body : documentBodies) {
            w.write(body);
        }
        w.write("</");
        w.write(prefix);
        w.write(":");
        w.write(localName);
        w.write(">");
        w.write("</soap-env:Body>");
        w.write("</soap-env:Envelope>");
    }

    /**
     * Takes a SOAP document in DOM form, performs some validation checks on the
     * SOAP document, and returns the "base element" of the SOAP document
     * underneath which message-specific elements probably are stored. An
     * exception is thrown if the SOAP base element does not have the prefix and
     * namespace URI specified, but the local name of that element is not
     * checked (most callers will use this parameter to store a function name or
     * something similar).
     * 
     * @throws SAXException on a parse error
     */
    public static Element decodeSoapDocument(Document doc, String localName,
            String prefix, String namespaceUri) throws SAXException {
        Element envelopeEl = doc.getDocumentElement();
        DomUtil.assertNodeName(envelopeEl, "soap-env:Envelope");
        DomUtil.assertAttributeCount(envelopeEl, 1);
        String xmlns = DomUtil.getAttrForEl(envelopeEl, "xmlns:soap-env", true);
        
        if (!xmlns.equals("http://schemas.xmlsoap.org/soap/envelope/")) {
            throw new SAXNotSupportedException("Unexpected URI for"
                    + " soap-env namespace");
        }

        Element bodyEl
                = DomUtil.findSingleElement(envelopeEl, "soap-env:Body", true);
        
        DomUtil.assertNodeName(bodyEl, "soap-env:Body");
        DomUtil.assertAttributeCount(bodyEl, 0);

        Element baseEl = DomUtil.findSingleElement(bodyEl, prefix + ":"
                + localName, true);
        
        DomUtil.assertAttributeCount(baseEl, 1);
        if (!DomUtil.getAttrForEl(baseEl, "xmlns:" + prefix, true).equals(
                namespaceUri)) {
            throw new SAXNotSupportedException("Expected the namespace URI on"
                    + " the SOAP base element to be '" + namespaceUri + "'");
        }

        return baseEl;
    }

    /**
     * Given a whole XML document, this function strips any document header
     * information and returns the "body" portion of the document. The body
     * portion of the document is identified by the name of the document root
     * element. If the specified {@code xmlDoc} does not contain any header,
     * this function's return value may be equal to {@code xmlDoc}. The current
     * implementation performs a simple string search for the named
     * {@code headElement} and conditionally lops of the leading portion of the
     * {@code xmlDoc}.
     * 
     * @return the "body" portion of the XML document, with any header
     *         information removed. This may be null if {@code xmlDoc} was null.
     * @param xmlDoc an XML document, or fragment thereof.
     * @param headElementName the name of the XML document's root element, for
     *        instance "message".
     * @throws IllegalArgumentException if {@code headElementName} does not
     *         appear within {@code xmlDoc}.
     */
    public static String dropXmlDocumentHeader(String xmlDoc,
            String headElementName) {
        Pattern pattern = Pattern.compile("<\\s*?" + headElementName + "[>\\s]");
        Matcher matcher = pattern.matcher(xmlDoc);
        
        if (!matcher.find()) {
            throw new IllegalArgumentException("No <" + headElementName
                    + "> element found in XML document");
        }
        
        return xmlDoc.substring(matcher.start());
    }

    /**
     * Parses a caller-supplied XML document and extracts all of the XML
     * fragments nested within that document that have the specified name. For
     * example, if {@code elementName} is "message", then each XML fragment
     * beginning with "&lt;message" and ending with "&lt;/message&gt;" is
     * extracted. This method should not be used on XML documents that contain
     * CDATA sections or that contain elements with name {@code elementName}
     * nested within one another.
     * 
     * @return an array of zero or more XML fragments as strings.
     * @param xmlDoc the XML document or document fragment to be parsed.
     * @param elementName the name of the XML element that delimits each
     *        fragment to be parsed. For instance "message".
     */
    public static String[] extractFragmentsFromXmlDocument(String xmlDoc,
            String elementName) {
        Collection<String> matchedFragments = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<\\s*?" + elementName
                + "[>\\s].+?<\\s*?/" + elementName + "\\s*?>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xmlDoc);
        
        while (matcher.find()) {
            matchedFragments.add(matcher.group());
        }
        
        return matchedFragments.toArray(new String[matchedFragments.size()]);
    }

    /**
     * Returns a String containing the complete contents of the specified input
     * stream. The specified input stream is read to its end; this function may
     * block. The caller is responsible for closing the input stream after this
     * function returns. The character set {@code UTF-8} is assumed. In the
     * current implementation, the chunk size is four kilobytes.
     */
    public static String readEntireInputStream(InputStream is)
            throws IOException {
        return readEntireInputStream(new InputStreamReader(is, "UTF-8"));
    }

    /**
     * Variant of the other version of {@code readEntireInputStream()} that
     * works with a {@code Reader} object instead of an input stream. No
     * character set is assumed by this method.
     */
    public static String readEntireInputStream(Reader reader)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        char buffer[] = new char[4096];
        int charsRead;
        
        do {
            charsRead = reader.read(buffer, 0, buffer.length);
            if (charsRead > 0) {
                sb.append(buffer, 0, charsRead);
            }
        } while (charsRead >= 0);

        return sb.toString();
    }
}
