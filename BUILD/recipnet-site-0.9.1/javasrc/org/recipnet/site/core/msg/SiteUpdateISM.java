/*
 * Reciprocal Net project
 * @(#)SiteUpdateISM.java
 *
 * 15-Jul-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda added support for serialization to XML and also moved
 *              the class into the core.msg package, from the container package
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent from the Reciprocal Net coordinator
 * to all sites whenever an existing site record needs to be updated.  Site
 * deactivation should be accomplished with the SiteDeactivationISM message
 * instead.
 */
public class SiteUpdateISM extends InterSiteMessage {    
    public SiteInfo updatedSite;

    /** Default constructor */
    public SiteUpdateISM() {
	super();
	updatedSite = new SiteInfo();
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  This constructor is useful only to the Reciprocal
     * Net Coordinator, because he is the only entity who may transmit this
     * message.
     */
    public SiteUpdateISM(SiteInfo updatedSite) {
	super();
	this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.updatedSite = updatedSite;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	updatedSite.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
        Node realParent = super.extractFromDom(doc, base);
	updatedSite.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

