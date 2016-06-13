/*
 * Reciprocal Net project
 * @(#)ReplayResponseISM.java
 *
 * 28-Oct-2005: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A link-local ISM that is sent in response to a 
 * <code>ReplayRequestISM</code> (another link-local ISM) from the same
 * exchange.  This message summarizes the replay operation that took place or
 * is taking place.  The relative ordering of this message in relation to
 * replayed ISM's in the same exchange response is not defined.
 */
public class ReplayResponseISM extends InterSiteMessage {
    /**
     * Identifies the site that originated the ISM's that are being replayed.
     * Replaying sites should set this field to be equal to 
     * <code>ReplayRequestISM.requestedSiteId</code>.
     */
    public int requestedSiteId;

    /**
     * The sequence number up to which public ISM's were excluded from being
     * replayed.  Replaying sites should set this field to be equal to
     * <code>ReplayRequestISM.excludePublicSeqNumsUpTo</code>.
     */
    public long requestedExcludePublicSeqNumsUpTo;

    /**
     * The sequence number up to which private ISM's were excluded from being
     * replayed.  Replaying sites should set this field to be equal to
     * <code>ReplayRequestISM.excludePrivateSeqNumsUpTo</code>.
     */
    public long requestedExcludePrivateSeqNumsUpTo;

    /**
     * The number of ISM's known to the replaying site that match the criteria
     * specified by the requester in the <code>ReplayRequestISM</code>.
     */
    public long countMatchingIsms;

    /**
     * The number of ISM's actually being replayed by the replaying site.  This
     * is never greater than <code>countMatchingIsms</code>, and may be less if
     * replay-side limits or request-side limits were encountered.
     */
    public long countReplayedIsms;

    /** No-arg constructor */
    public ReplayResponseISM() {
        super();
        this.requestedSiteId = SiteInfo.INVALID_SITE_ID;
        this.requestedExcludePublicSeqNumsUpTo = INVALID_SEQ_NUM;
        this.requestedExcludePrivateSeqNumsUpTo = INVALID_SEQ_NUM;
        this.countMatchingIsms = 0;
        this.countReplayedIsms = 0;
    }

    /** Convenience constructor that fully fills this ISM's members. */
    public ReplayResponseISM(int sourceSiteId, int destSiteId, 
            int requestedSiteId, long requestedExcludePublicSeqNumsUpTo, 
            long requestedExcludePrivateSeqNumsUpTo, long countMatchingIsms, 
            long countReplayedIsms) {
        super();
        super.sourceSiteId = sourceSiteId;
        super.destSiteId = destSiteId;
        super.linkLocal = true;
        this.requestedSiteId = requestedSiteId;
        this.requestedExcludePublicSeqNumsUpTo 
                = requestedExcludePublicSeqNumsUpTo;
        this.requestedExcludePrivateSeqNumsUpTo 
                = requestedExcludePrivateSeqNumsUpTo;
        this.countMatchingIsms = countMatchingIsms;
        this.countReplayedIsms = countReplayedIsms;
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
        if (this.requestedExcludePublicSeqNumsUpTo 
                != InterSiteMessage.INVALID_SEQ_NUM) {
            DomUtil.createTextEl(realParent, 
                    "requestedExcludePublicSeqNumsUpTo", 
                    Long.toString(this.requestedExcludePublicSeqNumsUpTo));
        }
        if (this.requestedExcludePrivateSeqNumsUpTo 
                != InterSiteMessage.INVALID_SEQ_NUM) {
            DomUtil.createTextEl(realParent, 
                    "requestedExcludePrivateSeqNumsUpTo", 
                    Long.toString(this.requestedExcludePrivateSeqNumsUpTo));
        }
        DomUtil.createTextEl(realParent, "countMatchingIsms", 
                Long.toString(this.countMatchingIsms));
        DomUtil.createTextEl(realParent, "countReplayedIsms",
                Long.toString(this.countReplayedIsms));
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
	this.requestedExcludePublicSeqNumsUpTo 
                = DomUtil.getTextForElAsLong(realParent,
                "requestedExcludePublicSeqNumsUpTo", INVALID_SEQ_NUM);
	this.requestedExcludePrivateSeqNumsUpTo 
                = DomUtil.getTextForElAsLong(realParent, 
                "requestedExcludePrivateSeqNumsUpTo", INVALID_SEQ_NUM);
        this.countMatchingIsms
                = DomUtil.getTextForElAsLong(realParent, "countMatchingIsms");
        this.countReplayedIsms
                = DomUtil.getTextForElAsLong(realParent, "countReplayedIsms");
        return realParent;
    }
}
