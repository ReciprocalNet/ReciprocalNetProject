/*
 * Reciprocal Net project
 * @(#)SampleDeactivationISM.java
 *
 * 29-Jan-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SampleInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is broadcast from a sample's authoritative site to
 * all sites whenever a previously-replicated sample is no longer visible to 
 * the public or eligible for replication.  Only the id of the sample to be
 * deleted is transmitted.
 */
public class SampleDeactivationISM extends InterSiteMessage {    
    public int oldSampleId;

    /** Default constructor */
    public SampleDeactivationISM() {
	super();
	oldSampleId = SampleInfo.INVALID_SAMPLE_ID;
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).
     */
    public SampleDeactivationISM(int localSiteId, int oldSampleId) {
	super();
	this.sourceSiteId = localSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSampleManager = true;
	this.oldSampleId = oldSampleId;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "sampleId", 
                Integer.toString(this.oldSampleId));
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.oldSampleId = DomUtil.getTextForElAsInt(realParent, "sampleId");
	return realParent;
    }	
}
