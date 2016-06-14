/*
 * Reciprocal Net project
 * @(#)SiteResetISM.java
 *
 * 22-Oct-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
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
 * ISM sent by the Coordinator that directs the destination site(s) to reset
 * the sequence number counters they maintain about a specified third site.  
 * This capability is introduced without any specific purpose in mind; that is,
 * there currently are no known circumstances in which the Coordinator would
 * need to transmit one of these messages.  Instead, the sequence-number-
 * reset capability is being reserved just in case a need arises for it in the
 * future. <p>
 *
 * The message may be sent by the Coordinator either as a public broadcast to
 * all sites or a private transmission to one site. <p>
 *
 * NOTE: for a variety of reasons, it is unlikely to be useful for the 
 *       Coordinator to transmit this message as a broadcast to ALL_SITES.  One
 *       reason is that if this message is broadcast, it will then also be
 *       interpreted by new sites activated long after whatever transient
 *       sequence-number error necessesitated this message has been corrected.
 *       Consequences from such after-the-fact processing may be difficult to
 *       predict.  Therefore, it is recommended that the Coordinator always
 *       send this sort of message as a private transmission.
 */
public class SiteResetISM extends InterSiteMessage { 
    public static final long DONT_RESET = -2;

    /**
     * The id of the site for which sequence numbers are to be reset.  That is,
     * the desination site that receives this message should alter its records
     * about this other site in its sequence number table.  This must be the
     * id of a single site; there is no provision for updating sequence numbers
     * about several sites.
     */
    public int otherSiteId;

    /**
     * The correct setting for the destination site's 'publicSeqNum' counter.
     * INVALID_SEQUENCE_NUMBER is a valid counter value and thus is a valid 
     * value for this field.  If this field is <code>DONT_RESET</code>,
     * the destination site should ignore this field and not update its
     * 'publicSeqNum' counter.
     */
    public long publicSeqNum;

    /**
     * The correct setting for the destination site's 'privateSeqNum' counter.
     * INVALID_SEQUENCE_NUMBER is a valid counter value and thus is a valid 
     * value for this field.  If this field is <code>DONT_RESET</code>,
     * the destination site should ignore this field and not update its
     * 'privateSeqNum' counter.
     */
    public long privateSeqNum;

    /** Default constructor */
    public SiteResetISM() {
	super();
	this.otherSiteId = SiteInfo.INVALID_SITE_ID;
	this.publicSeqNum = DONT_RESET;
	this.privateSeqNum = DONT_RESET;
    }

    /** Convenience constructor that fully fills this ISM's members. */
    public SiteResetISM(int sourceSiteId, int destSiteId, int otherSiteId, 
            long publicSeqNum, long privateSeqNum) {
	super();
	super.sourceSiteId = sourceSiteId;
	super.sourceDate = new Date();
	super.sourceSeqNum = INVALID_SEQ_NUM;
	super.sourcePrevSeqNum = INVALID_SEQ_NUM;
	super.destSiteId = destSiteId;
	super.deliverToSiteManager = true;
	this.otherSiteId = otherSiteId;
	this.publicSeqNum = publicSeqNum;
	this.privateSeqNum = privateSeqNum;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);

	if (this.otherSiteId == SiteInfo.INVALID_SITE_ID) {
	    throw new IllegalStateException();
	}
	DomUtil.createTextEl(realParent, "otherSiteId", 
                Integer.toString(this.otherSiteId));

	if (this.publicSeqNum != DONT_RESET) {
	    DomUtil.createTextEl(realParent, "publicSeqNum", 
	            Long.toString(this.publicSeqNum));
	}

	if (this.privateSeqNum != DONT_RESET) {
  	    DomUtil.createTextEl(realParent, "privateSeqNum", 
                    Long.toString(this.privateSeqNum));
	}

	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);

	this.otherSiteId = DomUtil.getTextForElAsInt(realParent, 
                "otherSiteId");
	this.publicSeqNum = DomUtil.getTextForElAsLong(realParent, 
                "publicSeqNum", DONT_RESET);
	this.privateSeqNum = DomUtil.getTextForElAsLong(realParent, 
                "privateSeqNum", DONT_RESET);

	return realParent;
    }	
}

