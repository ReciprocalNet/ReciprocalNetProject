/*
 * Reciprocal Net project
 * LabTransferCompleteISM.java
 *
 * 01-Jan-2009: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent by one site to all other sites whenever it
 * receives and accepts a transfer of a lab record from some third site.  Lab
 * transfers are an involved, multi-step process, and this is merely one step.
 * See LabTransferAgent for more details.
 */
public class LabTransferCompleteISM extends InterSiteMessage {
    /**
     * Identifies a lab transfer, as initiated by a previous
     * LabTransferInitiateISM. 
     */
    public long labTransferId;

    /** Default constructor */
    public LabTransferCompleteISM() {
	super();
	this.labTransferId = InterSiteMessage.INVALID_SEQ_NUM;
    }

    /** Convenience constructor that completely fills this ISM's fields. */
    public LabTransferCompleteISM(int sourceSiteId, long labTransferId) {
	super();
	this.sourceSiteId = sourceSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.labTransferId = labTransferId;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	if (this.labTransferId == InterSiteMessage.INVALID_SEQ_NUM) {
	    throw new IllegalStateException();
	}

	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "labTransferId", 
	        Long.toString(this.labTransferId));
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.labTransferId = DomUtil.getTextForElAsLong(realParent,
                "labTransferId");
	return realParent;
    }	
}
