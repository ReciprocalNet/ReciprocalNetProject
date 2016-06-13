/*
 * Reciprocal Net project
 * LabTransferInitiateISM.java
 *
 * 01-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent from the Reciprocal Net coordinator
 * to all sites whenever it needs to initiate a transfer of lab record from one
 * site to another.
 *
 * Other lab transfer machinery obtains a labTransferId value from this ISM.
 * That value is carried implicitly in the sourceSeqNum field declared by the
 * superclass.
 *
 * Lab transfers are in involved, multi-step process, and 
 * this is merely the first step.  See LabTransferAgent for more details.
 */
public class LabTransferInitiateISM extends InterSiteMessage {
    /** Identifies the lab record being transferred. */
    public int labId;

    /** Identifies the site at which the lab previously resided. */
    public int previousHomeSiteId;

    /** Identifies the site to which the lab is being transffered. */
    public int newHomeSiteId;

    /** Default constructor */
    public LabTransferInitiateISM() {
	super();
	this.labId = LabInfo.INVALID_LAB_ID;
	this.previousHomeSiteId = SiteInfo.INVALID_SITE_ID;
	this.newHomeSiteId = SiteInfo.INVALID_SITE_ID;
    }

    /**
     * Convenience constructor that completely fills this ISM's fields.  This
     * constructor is useful only to the Reciprocal Net Coordinator, because he
     * is the only entity who may transmit this message.
     */
    public LabTransferInitiateISM(int labId, int previousHomeSiteId, 
            int newHomeSiteId) {
	super();
	this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.labId = labId;
	this.previousHomeSiteId = previousHomeSiteId;
	this.newHomeSiteId = newHomeSiteId;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	if (this.labId == LabInfo.INVALID_LAB_ID
                || this.previousHomeSiteId == SiteInfo.INVALID_SITE_ID 
	        || this.newHomeSiteId == SiteInfo.INVALID_SITE_ID) {
	    throw new IllegalStateException();
	}

	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "labId",
		Integer.toString(this.labId));
	DomUtil.createTextEl(realParent, "previousHomeSiteId", 
	        Integer.toString(this.previousHomeSiteId));
	DomUtil.createTextEl(realParent, "newHomeSiteId", 
	        Integer.toString(this.newHomeSiteId));
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.labId = DomUtil.getTextForElAsInt(realParent, "labId");
	this.previousHomeSiteId = DomUtil.getTextForElAsInt(realParent,
                "previousHomeSiteId");
	this.newHomeSiteId = DomUtil.getTextForElAsInt(realParent,
                "newHomeSiteId");
	return realParent;
    }	
}
