/*
 * Reciprocal Net project
 * @(#)LabActivationISM.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.db.LabInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent from the Reciprocal Net coordinator
 * to all sites whenever a new lab joins the network.  It announces the new
 * lab's existence by encapsulating a LabInfo object.  The LabInfo object
 * specifies the lab's id, name, short name, home url, default copyright
 * notice, and home site id.  (The lab's active value is always true with this
 * sort of message.)
 */
public class LabActivationISM extends InterSiteMessage {    
    public LabInfo newLab;

    public LabActivationISM() {
	super();
	newLab = new LabInfo();
    }

    public LabActivationISM(LabInfo newLab) {
	super();
	this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.newLab = newLab;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	newLab.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Node realParent = super.extractFromDom(doc, base);
	newLab.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

