/*
 * Reciprocal Net project
 * @(#)LabUpdateISM.java
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
import org.recipnet.site.shared.db.LabInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent from the Reciprocal Net coordinator
 * to all sites whenever an existing lab's record needs to change.  The message
 * may also be sent by the site that currently hosts the lab.  Lab deactivation
 * may be accomplished with this message by setting updateLab.active=false.
 */
public class LabUpdateISM extends InterSiteMessage {    
    public LabInfo updatedLab;

    /** Default constructor */
    public LabUpdateISM() {
	super();
	updatedLab = new LabInfo();
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  This constructor is useful only to the Reciprocal
     * Net Coordinator, or the site that previously hosted the lab.
     */
    public LabUpdateISM(LabInfo updatedLab, int sourceSiteId) {
	super();
	this.sourceSiteId = sourceSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.updatedLab = updatedLab;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	updatedLab.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Node realParent = super.extractFromDom(doc, base);
	updatedLab.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

