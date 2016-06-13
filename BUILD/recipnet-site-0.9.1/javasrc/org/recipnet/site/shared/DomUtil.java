/*
 * Reciprocal Net project
 * 
 * DomUtil.java
 *
 * 25-Sep-2002: ekoperda wrote first draft
 * 30-Sep-2002: ekoperda fixed bug #502 in getTextForElAsBoolean()
 * 08-Oct-2002: ekoperda added function getTextForElAsLong(), 2 versions, 
 *              new version of xmlToDomTree(), and createSoapDocument()
 * 18-Nov-2002: nisheth added functions getTextForElAsDouble()(2 versions),
 *              getAttrForElAsInt()(2nd version), domTreeToObject(), and
 *              xmlToObject()
 * 21-Feb-2003: ekoperda added exception support throughout
 * 13-May-2003: midurbin fixed bug #817 in getTextForElAsBinary(),
 *              createTextElWithBinaryData(), xmlToDomTree()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.shared; changed package references to match
 *              source tree reorganization
 * 14-Jun-2004: ekoperda added objectToDomTreeUsingResources(),
 *              objectToXmlUsingResources(), and addTextToEl()
 * 19-Jan-2006: ekoperda added 3-arg version of getAttrForElAsLong() and also
 *              getAttrForElAsBoolean()
 * 26-Apr-2006: jobollin accommodated revisions to BASE64 streams; formatted the
 *              source; removed unused imports
 * 15-Jun-2006: jobollin added a warning-suppression annotation
 * 10-Jan-2008: ekoperda removed deprecated function getTextForElAsBinary()
 */

package org.recipnet.site.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.recipnet.common.Base64InputStream;
import org.recipnet.common.Base64OutputStream;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.UnexpectedExceptionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/**
 * A collection of static convenience functions that are useful when inserting
 * objects into a Document Object Model (DOM) tree, or extracting them from one.
 * Classes that implement the DomTreeParticipant interface tend to find these
 * functions handy.
 */
public class DomUtil {
    
    private final static DocumentBuilder documentBuilder;

    private final static Transformer transformer;

    /* Static initialization code */
    static {
        DocumentBuilder db;
        Transformer tr;
        
        try {
            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();
            
            factory.setCoalescing(true);
            factory.setIgnoringComments(true);
            db = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            // We'll throw an IllegalStateException later
            db = null;
        }
        documentBuilder = db;
        
        try {
            tr = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            // We'll throw an IllegalStateException later
            tr = null;
        }
        transformer = tr;
    }

    /**
     * Returns the requested Element object from the DOM tree. The element is
     * specified by the combination of its parent element and its tag name. May
     * return null if {@code throwException} is false and the named element does
     * not exist. If {@code throwException} is false and there exists more than
     * one element with the specified name, the first such element is returned.
     * Useful when extracting an object from a DOM tree.
     * 
     * @throws SAXParseException only if {@code throwException} is true: if the
     *         specified element was not found.
     * @throws SAXNotSupportedException only if {@code throwException} is true:
     *         if more than one of the specified element was found.
     */
    public static Element findSingleElement(Element parent, String elementName,
            boolean throwException) throws SAXException {
        NodeList nl = parent.getElementsByTagName(elementName);

        if (nl.getLength() == 0) {
            if (throwException) {
                throw new SAXParseException("<" + elementName + "> element"
                        + " expected", null);
            } else {
                return null;
            }
        } else if (nl.getLength() > 1) {
            if (throwException) {
                throw new SAXNotSupportedException("Multiple <" + elementName
                        + "> elements not supported");
            }
            
            // No error, just take the first element whose name matches
        }
        
        return (Element) nl.item(0);
    }

    /**
     * Returns true if the requested element is present in a DOM tree, false
     * otherwise. The element is specified by the combination of its parent
     * element and its tag name. Does not attempt to validate the content or
     * attributes of the element. Will not throw an exception. Useful when
     * extracting an object from a DOM tree.
     */
    public static boolean isElPresent(Element parent, String elementName) {
        try {
            return findSingleElement(parent, elementName, false) != null;
        } catch (SAXException ex) {
            // Can't happen because we passed 'false' to findSingleElement()
            return false;
        }
    }

    /**
     * Returns the value of the text-only element specified explicitely. May
     * return null if {@code throwException} is false and the named element does
     * not contain plain text or its value is not specified. Useful when
     * extracting an object from a DOM tree.
     * 
     * @throws SAXParseException only if {@code throwException} is true: if the
     *         specified element contained no data.
     * @throws SAXNotSupportedException only if {@code throwException} is true:
     *         if the specified element contains nested elements instead of
     *         text.
     */
    public static String getTextForEl(Element el, boolean throwException)
            throws SAXException {
        Node child = el.getFirstChild();
        
        if (child == null) {
            if (throwException) {
                throw new SAXParseException("Element <" + el.getNodeName()
                        + "> has no content", null);
            } else {
                return null;
            }
        } else if (child.getNodeType() != Node.TEXT_NODE) {
            
            /*
             * FIXME: What about CDATA sections?  We may not need to worry about
             * them because the parser is configured to coalesce them with text
             * and because none of the methods of this class actually produce
             * any, but a document produced by hand might contain them.
             */
            /*
             * FIXME: What about comments?  Similar to CDATA sections, we
             * probably only need to worry about them for hand-crafted documents
             */
            /*
             * FIXME: What about entity references?  As above, we probably only
             * need to worry about them in hand-crafted documents
             */
            /*
             * FIXME: What about processing instructions?  We don't (currently)
             * insert them, but a hand-crafted document might include them.  The
             * parser will pass on any that it sees in its input.
             */
            if (throwException) {
                throw new SAXNotSupportedException("Only text content is "
                        + " expected for element <" + el.getNodeName() + ">");
            } else {
                return null;
            }
        }
        
        String value = ((Text) child).getWholeText();
        
        if ((value == null) || (value.length() == 0)) {
            if (throwException) {
                throw new SAXParseException("Element <"
                        + el.getNodeName() + "> has empty content", null);
            } else {
                return null;
            }
        } else {
            return value;
        }
    }

    /**
     * Returns the value of the text-only element that's specified by its parent
     * element and its name. May return null if {@code throwException} is false
     * and the named element does not exist, does not contain plain text, or its
     * value is not specified. If {@code throwException} is false and there
     * exist more than one element with the specified name, the first such
     * element is evaluated. Useful when extracting an object from a DOM tree.
     * 
     * @throws SAXParseException only if {@code throwException} is true: if the
     *         specified element was not found or if it contained no data.
     * @throws SAXNotSupportedException only if {@code throwException} is true:
     *         if more than one of the specified element was found or if the
     *         specified element contains nested elements instead of text.
     */
    public static String getTextForEl(Element parent, String elementName,
            boolean throwException) throws SAXException {
        Element el;
        
        try {
            el = findSingleElement(parent, elementName, true);
        } catch (SAXException se) {
            if (!throwException) {
                return null;
            } else {
                throw se;
            }
        }

        return getTextForEl(el, throwException);
    }

    /**
     * Like getTextForElement() above, except parses the element's text as an
     * int and always throws an exception on error. Useful when extracting an
     * object from a DOM tree.
     * 
     * @throws SAXParseException if the specified element was not found, or if
     *         it contained no data, or if the data was not parsable as an int.
     * @throws SAXNotSupportedException if more than one of the specified
     *         element was found or if the specified element contains nested
     *         elements instead of text.
     */
    public static int getTextForElAsInt(Element parent, String elementName)
            throws SAXException {
        String value = getTextForEl(parent, elementName, true);
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Like getTextForElementAsInt() above, except returns a default integer
     * value instead of throwing an exception if the field could not be read.
     * Useful when extracting an object from a DOM tree.
     */
    public static int getTextForElAsInt(Element parent, String elementName,
            int defaultValue) throws SAXException {
        String value = getTextForEl(parent, elementName, false);
        
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    /**
     * Like getTextForElement() above, except parses the element's text as an
     * int and always throws an exception on error. Useful when extracting an
     * object from a DOM tree.
     * 
     * @throws SAXParseException if the specified element was not found, or if
     *         it contained no data, or if the data was not parsable as an int.
     * @throws SAXNotSupportedException if more than one of the specified
     *         element was found or if the specified element contains nested
     *         elements instead of text.
     */
    public static long getTextForElAsLong(Element parent, String elementName)
            throws SAXException {
        String value = getTextForEl(parent, elementName, true);
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Like getTextForElementAsLong() above, except returns a default long value
     * instead of throwing an exception if the field could not be read. Useful
     * when extracting an object from a DOM tree.
     */
    public static long getTextForElAsLong(Element parent, String elementName,
            long defaultValue) throws SAXException {
        String value = getTextForEl(parent, elementName, false);
        
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    /**
     * Like getTextForElement() above, except parses the element's text as a
     * boolean and always throws an exception on error. Useful when extracting
     * an object from a DOM tree.
     * 
     * @throws SAXParseException if the specified element was not found, or if
     *         it contained no data, or if the data was not parsable as an int.
     * @throws SAXNotSupportedException if more than one of the specified
     *         element was found or if the specified element contains nested
     *         elements instead of text.
     */
    public static boolean getTextForElAsBoolean(Element parent,
            String elementName) throws SAXException {
        String value = getTextForEl(parent, elementName, true);
        
        try {
            return Boolean.valueOf(value).booleanValue();
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Base64-decodes binary data stored as text beneath the specified text
     * element. (The text element object might have been obtained via a call to
     * {@link #findSingleElement(Element, String, boolean)} previously).
     * Verifies that the 'encoding' attribute of the specified element is set to
     * 'BASE64', the only supported encoding.
     * 
     * @param el the {@code Element} containing the requested data as
     *        BASE64-encoded text content
     *        
     * @return a {@code byte[]} containing the decoded binary data
     * 
     * @throws SAXNotSupportedException if the specified element contains nested
     *         elements instead of text, or if the 'encoding' attribute is not
     *         'BASE64'.
     * @throws SAXException if the specified element contains no data, or if an
     *         error occurs while performing Base64-decoding.
     *         
     * @see #createTextElWithBinaryData(Node, String, byte[])
     */
    public static byte[] getTextForElAsBytes(Element el) throws SAXException {
        String encoding = getAttrForEl(el, "encoding", true);
        
        // Verify that this element's 'encoding' attribute is set as expected
        if (!encoding.equals("BASE64")) {
            throw new SAXNotSupportedException("Expected encoding 'BASE64'"
                    + " for binary data on element <" + el.getTagName() + ">");
        }

        // Get the text data from this element
        String strValue = getTextForEl(el, true);

        // Base64-decode the text data
        try {
            Base64InputStream b64is
                    = new Base64InputStream(new StringReader(strValue));
            byte[] buffer = new byte[3 * strValue.length() / 4];
            int byteCount = 0;

            for (int nRead = b64is.read(buffer, 0, buffer.length); (nRead > 0);
                    nRead = b64is.read(
                            buffer, byteCount, buffer.length - byteCount)) {
                byteCount += nRead;
            }
            b64is.close();
            
            if (byteCount == buffer.length) {
                return buffer;
            } else {
                byte[] data = new byte[byteCount];
                
                System.arraycopy(buffer, 0, data, 0, byteCount);
                
                return data;
            }
        } catch (IOException ex) {
            // This probably can't happen -- what could go wrong?
            throw new SAXException("I/O error while BASE64-decoding binary"
                    + " data from XML", ex);
        }
    }
    
    /**
     * Like getTextForElement() above, except parses the element's text as an
     * double and always throws an exception on error. Useful when extracting an
     * object from a DOM tree.
     * 
     * @throws SAXParseException if the specified element was not found, or if
     *         it contained no data, or if the data was not parsable as an int.
     * @throws SAXNotSupportedException if more than one of the specified
     *         element was found or if the specified element contains nested
     *         elements instead of text.
     */
    public static double getTextForElAsDouble(Element parent,
            String elementName) throws SAXException {
        String value = getTextForEl(parent, elementName, true);
        
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Like getTextForElAsDouble() above, except returns a default double value
     * instead of throwing an exception if the field could not be read. Useful
     * when extracting an object from a DOM tree.
     */
    public static double getTextForElAsDouble(Element parent,
            String elementName, double defaultValue) throws SAXException {
        String value = getTextForEl(parent, elementName, false);
        
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    /**
     * Returns the value of the attribute on the specified element that has the
     * specified name as a {@code String} (its native type). May return null if
     * {@code throwException} is false and the named attribute does not exist or
     * its value is not specified or its value is empty. Useful when extracting
     * an object from a DOM tree.
     * 
     * @throws SAXParseException if {@code throwException} is true and the named
     *         attribute does not exist, or its value is not specified, or its
     *         value is empty.
     */
    public static String getAttrForEl(Element el, String attrName,
            boolean throwException) throws SAXException {
        String value = el.getAttribute(attrName);
        
        if ((value == null) || (value.length() == 0)) {
            if (throwException) {
                throw new SAXParseException("Attribute '" + attrName
                        + "' expected on <" + el.getNodeName() + "> element",
                        null);
            } else {
                return null;
            }
        } else {
            return value;
        }
    }

    /**
     * Returns the value of the attribute on the specified element that has the
     * specified name as a {@code int}. Useful when extracting an object from a
     * DOM tree.
     * 
     * @throws SAXParseException if the named attribute does not exist, or if
     *         the value of the attribute could not be parsed as a long.
     */
    public static int getAttrForElAsInt(Element el, String attrName)
            throws SAXException {
        String value = getAttrForEl(el, attrName, true);
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Like getAttrForElAsInt() above, except returns a default int value
     * instead of throwing an exception if the field could not be read. Useful
     * when extracting an object from a DOM tree.
     */
    public static int getAttrForElAsInt(Element el, String attrName,
            int defaultValue) throws SAXException {
        String value = getAttrForEl(el, attrName, false);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    /**
     * Returns the value of the attribute on the specified element that has the
     * specified name as a {@code long}. Useful when extracting an object from
     * a DOM tree.
     * 
     * @throws SAXParseException if the named attribute does not exist, or if
     *         the value of the attribute could not be parsed as a long.
     */
    public static long getAttrForElAsLong(Element el, String attrName)
            throws SAXException {
        String value = getAttrForEl(el, attrName, true);
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new SAXParseException("Invalid number format on '" + value
                    + "'", null, ex);
        }
    }

    /**
     * Like getAttrForElAsLong() above, except returns a default long value
     * instead of throwing an exception if the field could not be read. Useful
     * when extracting an object from a DOM tree.
     */
    public static long getAttrForElAsLong(Element el, String attrName,
            long defaultValue) throws SAXException {
        String value = getAttrForEl(el, attrName, false);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }

    /**
     * Returns the value of the attribute on the specified element that has the
     * specified name as a {@code boolean}, evaluated according to the criteria
     * described for {@link Boolean#parseBoolean(String)}. Useful when
     * extracting an object from a DOM tree. In the event the specified
     * attribute is not present, a specified default value is returned.
     */
    public static boolean getAttrForElAsBoolean(Element el, String attrName,
            boolean defaultValue) throws SAXException {
        String value = getAttrForEl(el, attrName, false);
        
        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    /**
     * Throws a SAXParseException if the name of the specified node (the tag of
     * the specified element) is not equal to the name specified. Useful when
     * extracting an object from a DOM tree.
     */
    public static void assertNodeName(Node node, String nodeName)
            throws SAXParseException {
        String actualNodeName = node.getNodeName();
        
        if (!actualNodeName.equals(nodeName)) {
            throw new SAXParseException("<" + nodeName
                    + "> element expected, found <" + actualNodeName
                    + "> instead", null);
        }
    }

    /**
     * Throws a SAXNotRecognizedException if the number of attributes on the
     * specified node (element) is not equal to the specified number. Useful
     * when extracting an object from a DOM tree.
     */
    public static void assertAttributeCount(Node node, int count)
            throws SAXNotRecognizedException {
        int actualCount = node.getAttributes().getLength();
        
        if (actualCount != count) {
            throw new SAXNotRecognizedException("Expected to find " + count
                    + " attributes on element <" + node.getNodeName()
                    + ", found " + actualCount + " instead");
        }
    }

    /**
     * Creates an element in a DOM tree, including a root-level element. The
     * element to create is specified by the combination of its parent node and
     * the element name that is to be assigned. Useful when building a
     * representation of an object in a DOM tree.
     */
    public static Element createEl(Document doc, Node parent, String name) {
        Element el = doc.createElement(name);
        
        parent.appendChild(el);
        
        return el;
    }

    /**
     * Creates an Element in a DOM tree, having the specified name, the
     * specified character content and the specified parent Node. This method
     * cannot create a root-level element in a DOM tree (document). Useful when
     * building a representation of an object in a DOM tree.
     */
    public static Element createTextEl(Node parent, String name, String value) {
        Document doc = parent.getOwnerDocument();
        Element el = doc.createElement(name);
        
        parent.appendChild(el);
        el.appendChild(doc.createTextNode(value));
        
        return el;
    }

    /**
     * Adds a textual attribute to an existing element in a DOM tree. The
     * attribute will have the name and value specified. Useful when building a
     * representation of an object in a DOM tree.
     */
    public static void addAttrToEl(Element el, String name, String value) {
        Document doc = el.getOwnerDocument();
        Attr a = doc.createAttribute(name);
        
        a.setValue(value);
        el.setAttributeNode(a);
    }

    /**
     * Adds a textual value to an existing element in a DOM tree. Useful when
     * building a representation of an object in a DOM tree.
     */
    public static void addTextToEl(Element el, String value) {
        Document doc = el.getOwnerDocument();
        
        el.appendChild(doc.createTextNode(value));
    }

    /**
     * Similar to createTextEl() above, except can store binary data by first
     * converting it to text. The binary data is encoding as text using the
     * BASE64 encoding scheme. The newly-created element is given an attribute
     * named 'encoding' that is set to 'BASE64'.
     * 
     * @throws UnexpectedExceptionException if the in-memory byte stream failed
     *         for some reason.
     */
    public static Element createTextElWithBinaryData(Node parent, String name,
            byte[] value) {
        String strValue;

        // Base-64 encode the data
        try {
            StringWriter sw = new StringWriter((value.length * 14) / 10);
            Base64OutputStream b64os = new Base64OutputStream(sw, false);
            b64os.write(value);
            b64os.close();
            strValue = sw.toString();
        } catch (IOException ex) {
            // Can't think of any reason this would happen; why would an
            // in-memory byte stream fail?
            throw new UnexpectedExceptionException(ex);
        }

        // Create a text element to hold the data
        Element el = createTextEl(parent, name, strValue);

        // Add an attribute to the element that describes this data encoding
        addAttrToEl(el, "encoding", "BASE64");

        return el;
    }

    /**
     * Converts the specified Object to a DOM tree (Document) by way of its
     * DomTreeParticipant interface. May throw an IllegalStateException if the
     * translation engines shared by all threads using this class failed to
     * initialize for some reason.
     */
    public static Document objectToDomTree(DomTreeParticipant obj) {
        Document doc;

        // Quick sanity check
        if (documentBuilder == null) {
            throw new IllegalStateException("DocumentBuilder failed to"
                    + " initialize");
        }

        // Create and build a DOM tree to represent this object and its
        // subclasses.
        synchronized (documentBuilder) {
            doc = documentBuilder.newDocument();
        }
        obj.insertIntoDom(doc, doc);

        return doc;
    }

    /**
     * Converts the specified Object to a DOM tree (Document) by way of its
     * DomTreeParticipant or ExtendedDomTreeParticipant interface.
     * Caller-supplied {@code resources} are passed to {@code obj} for use
     * during conversion if {@code obj} supports it; otherwise {@code resources}
     * is ignored.
     * 
     * @throws IllegalStateException if the translation engines shared by all
     *         threads using this class failed to initialize for some reason.
     */
    public static Document objectToDomTreeUsingResources(
            DomTreeParticipant obj, ResourceBundle resources) {
        if (documentBuilder == null) {
            throw new IllegalStateException("DocumentBuilder failed to"
                    + " initialize");
        }

        // Create and build a DOM tree to represent this object and its
        // subclasses.
        Document doc;
        
        synchronized (documentBuilder) {
            doc = documentBuilder.newDocument();
        }
        
        if (obj instanceof ExtendedDomTreeParticipant) {
            // Pass the object a ResourceBundle and see whether it works.
            ExtendedDomTreeParticipant xObj = (ExtendedDomTreeParticipant) obj;
            
            try {
                xObj.insertIntoDomUsingResources(doc, doc, resources);
                
                return doc;
            } catch (UnsupportedOperationException ex) {
                // Just drop through...
            }
        }
        
        obj.insertIntoDom(doc, doc);
        
        return doc;
    }

    /**
     * Uses the DOM-serialized version of an object contained in the specified
     * Document to populate the object's member values. The caller-specified
     * object should implement the {@code DomTreeParticipant} interface.
     * 
     * @throws SAXException
     */
    public static void domTreeToObject(Document doc, DomTreeParticipant obj)
            throws SAXException {
        Element base = doc.getDocumentElement();
        
        obj.extractFromDom(doc, base);
    }

    /**
     * Converts the specified DOM tree (Document) to an XML string.
     * 
     * @throws IllegalStateException if the translation engines shared by all
     *         threads using this class failed to initialize for some reason.
     * @throws OperationFailedException if the transformation engine failed to
     *         execute the translation from DOM tree to XML.
     */
    public static String domTreeToXml(Document doc)
            throws OperationFailedException {
        if (transformer == null) {
            throw new IllegalStateException("Transformer failed to"
                    + " initialize");
        }

        // Transform the DOM tree into XML
        StringWriter sw = new StringWriter(1024);
        
        synchronized (transformer) {
            try {
                transformer.transform(new DOMSource(doc), new StreamResult(sw));
            } catch (TransformerException ex) {
                throw new OperationFailedException(ex);
            }
        }

        return sw.toString();
    }

    /**
     * Converts (parses) the specified XML string into a DOM tree (Document).
     * 
     * @throws IllegalStateException if the translation engines shared by all
     *         threads using this class failed to initialize for some reason.
     * @throws SAXException if the XML string could not be parsed into a DOM
     *         document, or if an I/O error of some sort occurred.
     */
    public static Document xmlToDomTree(String xml) throws SAXException {

        // Quick sanity check
        if (documentBuilder == null) {
            throw new IllegalStateException("DocumentBuilder failed to"
                    + " initialize");
        }

        synchronized (documentBuilder) {
            try {
                return documentBuilder.parse(
                        new InputSource(new StringReader(xml)));
            } catch (IOException ex) {
                throw new SAXException("I/O error while parsing XML data", ex);
            }
        }
    }

    public static Document xmlToDomTree(InputStream is) throws SAXException {

        // Quick sanity check
        if (documentBuilder == null) {
            throw new IllegalStateException("DocumentBuilder failed to"
                    + " initialize");
        }

        synchronized (documentBuilder) {
            try {
                return documentBuilder.parse(is);
            } catch (IOException ex) {
                throw new SAXException("I/O error while parsing XML data", ex);
            }
        }
    }

    /**
     * Converts the specified Object to an XML string by using the Object's
     * DomTreeParticipant interface. This is a convenience function that simply
     * invokes objectToDomTree() and then domTreeToXml(), with no provision for
     * the caller to manipulate the DOM tree prior to XML serialization.
     * 
     * @throws IllegalStateException if the translation engines shared by all
     *         threads using this class failed to initialize for some reason.
     * @throws OperationFailedException if the transformation engine failed to
     *         execute the translation from DOM tree to XML.
     */
    public static String objectToXml(DomTreeParticipant obj)
            throws OperationFailedException {
        return domTreeToXml(objectToDomTree(obj));
    }

    /**
     * Converts the specified Object to an XML string by using the Object's
     * DomTreeParticipant interface. This is a convenience function that simply
     * invokes objectToDomTreeUsingResources() and then domTreeToXml(), with no
     * provision for the caller to manipulate the DOM tree prior to XML
     * serialization.
     * 
     * @throws IllegalStateException if the translation engines shared by all
     *         threads using this class failed to initialize for some reason.
     * @throws OperationFailedException if the transformation engine failed to
     *         execute the translation from DOM tree to XML.
     */
    public static String objectToXmlUsingResources(DomTreeParticipant obj,
            ResourceBundle resources) throws OperationFailedException {
        return domTreeToXml(objectToDomTreeUsingResources(obj, resources));
    }

    /**
     * Takes an object and replaces its member values with those taken from an
     * XML-serialized version of another object with the same type. The
     * caller-provided object should implement the {@code DomTreeParticipant}
     * interface. This is a convenience function that simply invokes
     * xmlToDomTree() and then domTreeToObject(), with no provision for the
     * caller to manipulate the DOM tree prior after XML parsing.
     * 
     * @throws SAXException
     */
    public static void xmlToObject(String xml, DomTreeParticipant obj)
            throws SAXException {
        domTreeToObject(xmlToDomTree(xml), obj);
    }

    /**
     * Convenience function that returns a new, empty DOM Document object.
     * 
     * @throws IllegalStateException
     */
    public static Document createDocument() {
        if (documentBuilder == null) {
            throw new IllegalStateException();
        }
        synchronized (documentBuilder) {
            return documentBuilder.newDocument();
        }
    }

    /**
     * Convenience function that returns a new DOM Document object that already
     * has a few of the Elements required for use with the SOAP protocol. The
     * element returned by this function is the single "base" element of the
     * SOAP body; user elements should be inserted into the DOM tree as children
     * of the returned element. localName, prefix, and uri are all standard SOAP
     * terminology. Briefly, localName is like the function name to be called,
     * prefix is a one-word alias for your XML namespace (unique within the
     * document), and uri is a string that uniquely identifies your namespace,
     * probably by referencing some directory on your project's web server.
     * 
     * @throws IllegalStateException
     */
    public static Element createSoapDocument(String localName, String prefix,
            String uri) {
        Document doc = createDocument();

        // Create the SOAP envelope element
        Element envelopeEl = createEl(doc, doc, "soap-env:Envelope");
        addAttrToEl(envelopeEl, "xmlns:soap-env",
                "http://schemas.xmlsoap.org/soap/envelope/");

        // Create the SOAP header element. It can be empty.
        @SuppressWarnings("unused") Element headerEl
                = createEl(doc, envelopeEl, "soap-env:Header");

        // Create the envelope body element
        Element bodyEl = createEl(doc, envelopeEl, "soap-env:Body");

        // Create the base element for the caller's message
        Element baseEl = createEl(doc, bodyEl, prefix + ":" + localName);
        addAttrToEl(baseEl, "xmlns:" + prefix, uri);

        return baseEl;
    }
}
