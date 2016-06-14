/*
 * Reciprocal Net project
 * @(#)ReplayRequestISM.java
 *
 * 02-Oct-2002: ekoperda wrote first draft
 * 31-Oct-2002: ekoperda added field 'lastPrivateSeqNum'
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 15-Dec-2005: ekoperda rearranged message contents and class spec to describe
 *              only a single requested site
 */

package org.recipnet.site.core.msg;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A link-local ISM that allows one site (identified by 
 * the <code>sourceSiteId</code> field) to request from a second site
 * (identified by the <code>destSiteId</code> field) that it replay ISM's that
 * were originated by a third site previously (identified by the 
 * <code>requestedSiteId</code> field).  If 
 * <code>destSiteId==requestedSiteId</code>, then the destination site has an
 * implicit obligation to replay all requested ISM's; otherwise, the
 * destination site is under no obligation but is encouraged to provide 
 * best-effort service. <p>
 *
 * The specific ISM's requested to be replayed are identified by the
 * <code>excludePublicSequenceNumbersUpTo</code> and 
 * <code>excludePrivateSeqNumsUpTo</code> fields.  Any public ISM's
 * originated by <code>requestedSiteId</code> with a sequence number greater
 * than <code>excludePublicSeqNumsUpTo</code> are requested, as are any private
 * ISM's originated by <code>requestedSiteId</code> with a sequence number
 * greater than <code>excludePrivateSeqNumsUpTo</code>.
 */
public class ReplayRequestISM extends InterSiteMessage {
    /**
     * Identifies the site that originated the ISM's that are being requested
     * for replay.
     */
    public int requestedSiteId;

    /**
     * Describes the sequence numbers of public ISM's that should be excluded
     * from the replay.  Any public ISM (originated by 
     * <code>requestedSiteId</code>) should not be replayed if its sequence
     * number is less than or equal to the value in this field.  The special
     * value <code>INVALID_SEQ_NUM</code> in this field indicates that no
     * public ISM's are excluded.
     */
    public long excludePublicSeqNumsUpTo;

    /**
     * Describes the sequence numbers of private ISM's that should be excluded
     * from the replay.  Any public ISM (originated by 
     * <code>requestedSiteId</code>) should not be replayed if its sequence
     * number is less than or equal to the value in this field.  The special
     * value <code>INVALID_SEQ_NUM</code> in this field indicates that no
     * private ISM's are excluded.
     */
    public long excludePrivateSeqNumsUpTo;

    /**
     * A number that indicates the maximum number of ISM's that should be
     * replayed as a result of the present exchange.  Requesting sites should
     * set this value considering network performance, desired latency, and
     * available buffers.  The replaying site is free to impose a lower limit
     * and replay fewer ISM's than this value would indicate, even if a greater
     * number of ISM's are available.  <p>
     *
     * In the event that the requesting site bundles several
     * <code>ReplayRequestISM</code>'s together and sends them to one site as
     * part of a single exchange with that site, the limit described by this
     * field applies to all ISM's that may be replayed by that site as part of
     * the exchange, not just those ISM's that may be replayed in response to 
     * this particular request.  For this reason, requesting sites preparing a
     * bundle of <code>ReplayRequestISM</code>'s for transmission in a single
     * exchange should set this value identically in each, and a failure of a
     * requesting site to do so may result in unpredictable behavior on the
     * part of the replaying site.
     */
    public long maxIsmsToReplay;

    /** No-arg constructor */
    public ReplayRequestISM() {
	super();
        this.requestedSiteId = SiteInfo.INVALID_SITE_ID;
	this.excludePublicSeqNumsUpTo = INVALID_SEQ_NUM;
	this.excludePrivateSeqNumsUpTo = INVALID_SEQ_NUM;
        this.maxIsmsToReplay = Long.MAX_VALUE;
    }

    /** Convenience constructor that fully fills this ISM's members. */
    public ReplayRequestISM(int sourceSiteId, int destSiteId, 
            int requestedSiteId, long excludePublicSeqNumsUpTo, 
            long excludePrivateSeqNumsUpTo, long maxIsmsToReplay) {
	super();
	this.sourceSiteId = sourceSiteId;
	this.destSiteId = destSiteId;
	this.linkLocal = true;
        this.requestedSiteId = requestedSiteId;
        this.excludePublicSeqNumsUpTo = excludePublicSeqNumsUpTo;
        this.excludePrivateSeqNumsUpTo = excludePrivateSeqNumsUpTo;
        this.maxIsmsToReplay = maxIsmsToReplay;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
        DomUtil.createTextEl(realParent, "requestedSiteId", 
                this.requestedSiteId == RECIPROCAL_NET_COORDINATOR
		? "coordinator" 
                : Integer.toString(this.requestedSiteId));
        if (this.excludePublicSeqNumsUpTo 
                != InterSiteMessage.INVALID_SEQ_NUM) {
            DomUtil.createTextEl(realParent, "excludePublicSeqNumsUpTo", 
                    Long.toString(this.excludePublicSeqNumsUpTo));
        }
        if (this.excludePrivateSeqNumsUpTo 
                != InterSiteMessage.INVALID_SEQ_NUM) {
            DomUtil.createTextEl(realParent, "excludePrivateSeqNumsUpTo", 
                    Long.toString(this.excludePrivateSeqNumsUpTo));
        }
        DomUtil.createTextEl(realParent, "maxIsmsToReplay", 
                Long.toString(this.maxIsmsToReplay));
        return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
        String requestedSiteIdAsString 
                = DomUtil.getTextForEl(realParent, "requestedSiteId", true);
        this.requestedSiteId = requestedSiteIdAsString.equals("coordinator")
                ? RECIPROCAL_NET_COORDINATOR
                : Integer.parseInt(requestedSiteIdAsString);
	this.excludePublicSeqNumsUpTo = DomUtil.getTextForElAsLong(realParent, 
                "excludePublicSeqNumsUpTo", INVALID_SEQ_NUM);
	this.excludePrivateSeqNumsUpTo = DomUtil.getTextForElAsLong(realParent,
                "excludePrivateSeqNumsUpTo", INVALID_SEQ_NUM);
        this.maxIsmsToReplay 
                = DomUtil.getTextForElAsLong(realParent, "maxIsmsToReplay");
        return realParent;
    }
}

