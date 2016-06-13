/*
 * Reciprocal Net project
 * @(#)SiteActivationISM.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 07-Jan-2004: ekoperda change package references to match source tree
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
 * to all sites whenever a new site joins the network.  It announces the new
 * site's existing by encapsulating a SiteInfo object.  The SiteInfo object
 * specifies the new site's id, name, short name, base URL, repository URL,
 * and public encryption key.  The corresponding message for site deactivation
 * is SiteDeactivationISM.
 */
public class SiteActivationISM extends InterSiteMessage {    
    public SiteInfo newSite;

    /** Default constructor */
    public SiteActivationISM() {
	super();
	newSite = new SiteInfo();
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  This constructor is useful only to the Reciprocal
     * Net Coordinator, because he is the only entity who may transmit this
     * message.
     */
    public SiteActivationISM(SiteInfo newSite) {
	super();
	this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.newSite = newSite;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	newSite.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Node realParent = super.extractFromDom(doc, base);
	newSite.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

