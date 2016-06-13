/*
 * Reciprocal Net project
 * 
 * ProviderActivationISM.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 08-Oct-2002: ekoperda added 2-param constructor
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.core.msg;

import java.util.Date;
import org.recipnet.site.shared.db.ProviderInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * An Inter-site message that is sent from one site to all other sites whenever
 * a new Provider record has been created for a lab that's based there. It
 * announces the new provider's existence by encapsulating a ProviderInfo
 * object. The message may also be sent by the Coordinator.
 */
public class ProviderActivationISM extends InterSiteMessage {
    
    public ProviderInfo newProvider;

    public ProviderActivationISM() {
        newProvider = new ProviderInfo();
    }

    public ProviderActivationISM(ProviderInfo newProvider) {
        this(newProvider, RECIPROCAL_NET_COORDINATOR);
    }

    public ProviderActivationISM(ProviderInfo newProvider, int sourceSiteId) {
        this.sourceSiteId = sourceSiteId;
        this.sourceDate = new Date();
        this.destSiteId = InterSiteMessage.ALL_SITES;
        this.deliverToSiteManager = true;
        this.newProvider = newProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
        Node realParent = super.insertIntoDom(doc, base);
        
        newProvider.insertIntoDom(doc, realParent);
        
        return realParent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
        Node realParent = super.extractFromDom(doc, base);
        
        newProvider.extractFromDom(doc, realParent.getFirstChild());
        
        return realParent;
    }
}
