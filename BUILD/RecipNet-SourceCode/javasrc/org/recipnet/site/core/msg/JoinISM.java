/*
 * Reciprocal Net project
 * JoinISM.java
 *
 * 01-Jan-2009: ekoperda wrote first draft
 * 20-Mar-2009: ekoperda enhanced argument checking in a constructor
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
 * Inter-site message that is sent by site A to join its ISM stream with the
 * ISM stream of site B.  Some other receiving site, call it site C, cannot
 * process this ISM generated by site A until it has processed some particular
 * public ISM generated by site B.  Site B is identified by its site id
 * (joinedSiteId) and its public ISM is identified by sequence number
 * (joinedSiteSeqNum).
 *
 * Note that site A must not be the Coordinator.  In order for new sites to be
 * bootstrapped, the Coordinator cannot join its ISM stream to any other site's
 * stream by generating one of these JoinISM's.  The Coordinator's ISM stream
 * must remain unfettered and self-contained.
 */
public class JoinISM extends InterSiteMessage {
    /** Identifies the site whose ISM stream is to be joined. */
    public int joinedSiteId;

    /**
     * The sequence number within the ISM stream of the target site that is
     * to be joined.  This is presumed to identify a public ISM.
     */
    public long joinedSiteSeqNum;

    /** Default constructor */
    public JoinISM() {
	super();
	this.joinedSiteId = SiteInfo.INVALID_SITE_ID;
	this.joinedSiteSeqNum = InterSiteMessage.INVALID_SEQ_NUM;
    }

    /** Convenience constructor that completely fills this ISM's fields. */
    public JoinISM(int sourceSiteId, int joinedSiteId, long joinedSiteSeqNum) {
	super();
	if (sourceSiteId == SiteInfo.INVALID_SITE_ID
	        || joinedSiteId == SiteInfo.INVALID_SITE_ID
	        || joinedSiteSeqNum == InterSiteMessage.INVALID_SEQ_NUM) {
	    throw new IllegalArgumentException();
	}
	this.sourceSiteId = sourceSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.joinedSiteId = joinedSiteId;
	this.joinedSiteSeqNum = joinedSiteSeqNum;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	if (this.joinedSiteId == SiteInfo.INVALID_SITE_ID
                || this.joinedSiteSeqNum  == InterSiteMessage.INVALID_SEQ_NUM){
	    throw new IllegalStateException();
	}

	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "joinedSiteId",
		Integer.toString(this.joinedSiteId));
	DomUtil.createTextEl(realParent, "joinedSiteSeqNum", 
	        Long.toString(this.joinedSiteSeqNum));
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.joinedSiteId = DomUtil.getTextForElAsInt(realParent,
	        "joinedSiteId");
	this.joinedSiteSeqNum = DomUtil.getTextForElAsLong(realParent,
                "joinedSiteSeqNum");
	return realParent;
    }	
}
