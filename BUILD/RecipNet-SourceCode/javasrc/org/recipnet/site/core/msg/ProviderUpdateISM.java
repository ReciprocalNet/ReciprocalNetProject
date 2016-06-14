/*
 * Reciprocal Net project
 * @(#)ProviderUpdateISM.java
 *
 * 15-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.db.ProviderInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent to all sites from one site to indicate that 
 * a provider belonging to one of the labs hosted there has changed.  The
 * message may also be sent by the Coordinator.  The updated Provider record is
 * specified by updatedProvider.  Provider deactivation may be accomplished
 * with this message by setting updatedProvider.active=false.
 */
public class ProviderUpdateISM extends InterSiteMessage {    
    public ProviderInfo updatedProvider;

    /** Default constructor */
    public ProviderUpdateISM() {
	super();
	updatedProvider = new ProviderInfo();
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  This constructor is useful only to the Reciprocal
     * Net Coordinator, or the site that hosts the provider.
     */
    public ProviderUpdateISM(ProviderInfo updatedProvider, int sourceSiteId) {
	super();
	this.sourceSiteId = sourceSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.updatedProvider = updatedProvider;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	updatedProvider.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Node realParent = super.extractFromDom(doc, base);
	updatedProvider.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

