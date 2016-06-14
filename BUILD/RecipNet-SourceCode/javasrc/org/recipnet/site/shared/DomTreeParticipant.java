/*
 * Reciprocal Net project
 * @(#)DomTreeParticipant.java
 * 
 * 25-Sep-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda moved interface from org.recipnet.site.misc package to
 *              org.recipnet.site.shared
 */

package org.recipnet.site.shared;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * General-purpose interface; implementing classes have the ability to
 * serialize themselves to and from a Document Object Model (DOM) tree.  This
 * might be particularly useful in applications where the DOM representation
 * will then be translated to XML and transmitted across a network.
 *
 * In a class hierarchy where the base class implements this interface,
 * subclass implementations of this interface's two functions should begin with
 * a call to the corresponding function on the super class.  This provides a
 * mechanism for subclasses to nest subclass-specific serialization data inside
 * the parent class's serialized representation.
 */
public interface DomTreeParticipant {
    /**
     * Causes this object to "serialize" itself to the specified DOM Document.
     * The object may insert additional elements below the specified base node,
     * may insert a text node directly below, or may do nothing at all to the
     * DOM tree. To create a Document that contains nothing but a
     * a representation of this object, set base=doc. <p>
     *
     * In the implementation of this function for subclasses where the parent
     * class also implements the DomTreeParticipant interface, the first call
     * should be to super.insertIntoDom(doc, base).  The subclass
     * implementation should then take the Node returned by this call as the
     * base to be used by it, and ignore the base that was specified by the
     * caller. <p>
     *
     * This function may return base, if it has no special provision for nested
     * subclass data, or it may return a deeper node in the DOM tree under
     * which subclasses may append an elements (or a text node) that they like.
     */
    public Node insertIntoDom(Document doc, Node base);

    /**
     * Causes this object to "unserialize" itself from the specified DOM
     * Document.  base should be the corresponding Node that was passed as the
     * base to insertIntoDom() previously.  The function may throw an
     * SAXException (or subclass) if required/expected Nodes in the DOM tree
     * were not present, or if unexpected, unknown, or invalid attributes or
     * data values were encountered. <p>
     *
     * In the implementation of this function for subclasses where the parent
     * class also implements the DomTreeParticipant interface, the first call
     * should be to super.extractFromDom(doc, base).  The subclass
     * implementation should then take the Node returned by this call as the
     * base to be used by it, and ignore the base that was specified by the
     * caller. <p>
     *
     * This function may return base, if it has no special provision for nested
     * subclass data, or it may return a deeper node in the DOM tree under
     * which subclasses should extract any elements (or a text node) that they
     * might have stored there during a previous call to insertIntoDom().
     */
    public Node extractFromDom(Document doc, Node base) throws SAXException;
}
