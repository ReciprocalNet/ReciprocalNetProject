/*
 * Reciprocal Net project
 * @(#)RepositoryHoldingISM.java
 *
 * 29-Jan-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is broadcast from a site to all sites whenever data
 * files for a particular sample begin to be hosted on the site.  This message
 * may be repeated, with a different <code>replicaLevel</code>, as needed.  A
 * <code>replicaLevel</code> value of <code>NO_DATA</code> indicates that data 
 * files are no longer being hosted.  This message only makes sense after the
 * sample's metadata has been publicly announced via a SampleActivationISM and
 * before its metadata has been unannounced via a SampleDeactivationISM. <p>
 *
 * Note: at authoritative sites, it will be common when a sample is "released
 *       to public" for two ISM's to be generated.  The correct order is first
 *       the <code>SampleActivationISM</code>, then the 
 *       <code>RepositoryHoldingISM</code> (with a <code>replicaLevel</code> 
 *       value higher than <code>FULL_DATA</code>).  Similarly, if the same 
 *       sample were later "un-released to public", another two ISM's would be 
 *       generated: first the <code>RepositoryHoldingISM</code> (with a value
 *       of <code>NO_DATA</code>), then the SampleDeactivationISM.
 */
public class RepositoryHoldingISM extends InterSiteMessage {    
    /** Identifies the sample whose data files are being advertised. */
    public int sampleId;

    /** 
     * The level of completeness of the data stored at the transmitting site.
     * Valid values are defined in RepositoryHoldingInfo.
     */
    public int replicaLevel;

    /** Default constructor */
    public RepositoryHoldingISM() {
	super();
	sampleId = SampleInfo.INVALID_SAMPLE_ID;
	replicaLevel = RepositoryHoldingInfo.NO_DATA;
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).
     */
    public RepositoryHoldingISM(int localSiteId, int sampleId, 
            int replicaLevel) {
	super();
	this.sourceSiteId = localSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToRepositoryManager = true;
	this.sampleId = sampleId;
	this.replicaLevel = replicaLevel;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "sampleId", 
                Integer.toString(this.sampleId));
	DomUtil.createTextEl(realParent, "replicaLevel", 
                Integer.toString(this.replicaLevel));
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.sampleId = DomUtil.getTextForElAsInt(realParent, "sampleId");
	this.replicaLevel = 
                DomUtil.getTextForElAsInt(realParent, "replicaLevel");
	return realParent;
    }	
}
