/*
 * Reciprocal Net project
 * @(#)ExtendedDomTreeParticipant.java
 * 
 * 14-Jun-2004: ekoperda wrote first draft
 */
package org.recipnet.site.shared;
import java.util.ResourceBundle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Defines special-purpose extensions to the general-purpose
 * <code>DomTreeParticipant</code> interface.  Implementing classes are able to
 * serialize themselves to and from a Document Object Model (DOM) tree in
 * unusual or adaptable ways.  Not all methods defined by this interface will
 * make sense in the context of every implementing class, so implementing 
 * classes are free simply to throw an
 * <code>UnsupportedOperationException</code> from nonsensical methods.
 */
public interface ExtendedDomTreeParticipant extends DomTreeParticipant {
    /**
     * Similar to <code>insertIntoDom()</code> as defined on
     * <code>DomTreeParticipant</code>, except this function takes an
     * additional argument called <code>resources</code>.  Implementations may
     * use the caller-supplied resources to affect the DOM tree that they 
     * generate.
     * @return a <code>Node</code> within the DOM tree beneath which subclasses
     *     may insert elements of their own, or <code>base</code> if an
     *     implementation has no special provision for nested subclass data.
     * @param doc a <code>Document</code> into which this object should be
     *     stored.
     * @param base a <code>Node</code> within <code>doc</code> beneath which 
     *     an implementation may insert its elements.  This may be equal to
     *     <code>doc</code>.
     * @param resources a <code>ResourceBundle</code> that will control the
     *     generated DOM elements in an implementation-specific manner.  May be
     *     null.
     * @throws UnsupportedOperationException if a particular implementation
     *     does not support this method.
     */
    public Node insertIntoDomUsingResources(Document doc, Node base, 
            ResourceBundle resources);
}
