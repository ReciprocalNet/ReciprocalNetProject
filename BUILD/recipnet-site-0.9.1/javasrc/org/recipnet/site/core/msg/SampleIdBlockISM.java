/*
 * Reciprocal Net project
 * @(#)SampleIdBlockISM.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 31-Oct-2002: ekoperda added TRANSFER_REJECT function code a 3-param version
 *              of the constructor
 * 07-Nov-2002: ekoperda added expiresAfter field, supporting code, and a new
 *              version of the constructor for the PROPOSED function
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 14-May-2008: ekoperda enabled TRANSFER_REQUEST messages to expire,
 *              rearranged constructors, and added hasExpired()
 */

package org.recipnet.site.core.msg;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SampleIdBlock;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Inter-site message that deals with the dynamic allocation of sample id
 * blocks.  Exact meaning of the message depends upon the func value.  See
 * comments below for more information.
 */
public class SampleIdBlockISM extends InterSiteMessage {
    public static final int INVALID_FUNC = 0;
    public static final int PROPOSAL = 100;
    public static final int PROPOSAL_APPROVED = 150;
    public static final int PROPOSAL_DISAPPROVED = 160;
    public static final int CLAIM = 200;
    public static final int TRANSFER_INITIATE = 300;
    public static final int TRANSFER_REJECT = 320;
    public static final int TRANSFER_COMPLETE = 350;
    public static final int TRANSFER_REQUEST = 400;
    public static final int TRANSFER_REQUEST_DENIED = 450;

    /** 
     * prefix of the sample id block.  Values in the range 1 to 2097152 are 
     * valid.  Should be SampleIdBlock.INVALID_SAMPLE_ID_BLOCK_ID if this
     * message's func is TRANSFER_REQUEST or TRANSFER_REQUEST_DENIED. 
     */
    public int blockId;

    /**
     * The function being expressed by this message.  This is more convenient
     * than using several distinct message classes to exchange information
     * about sample id blocks.
     *
     * Valid values are:
     *    PROPOSAL:                 the site transmitting this message is 
     *                              announcing to ALL_SITES that it intends to
     *				    claim the specified block for itself, 
     *				    unless it discovers that the block has
     *				    already been claimed by another site.  All
     *				    sites receiving this message should reply
     *				    (relatively quickly) with either a 
     *				    PROPOSAL_APPROVED message or a
     *				    PROPOSAL_DISAPPROVED message.
     *    PROPOSAL_ARPROVED:        sent in reply to a previous PROPOSAL 
     *                              message to indicate that the sender is not
     *                              aware of any existing claims to the 
     *                              proposed sample id block.  (private msg)
     *    PROPOSAL_DISAPPROVED:     sent in reply to a previous PROPOSAL 
     *                              message to indicate that the sender is
     *                              aware of an existing claim (or proposal)
     *                              for that block by another site.  
     *                              otherSiteId should be set to the id of the
     *                              site that made the earlier claim (or
     *                              proposal).  (private msg)
     *    CLAIM:                    the site transmitting this message is
     *                              announcing to ALL_SITES that it has claimed
     *                              the specified block for itself.  A message
     *                              of this type is not valid and may be 
     *                              ignored if a previous PROPOSAL message from
     *                              the same site was not transmitted, or if
     *                              the local site responded to the earlier
     *                              PROPOSAL message with a
     *                              PROPOSAL_DISAPPROVED.  (Hence, a proposing
     *                              site may not issue a claim unless 
     *                              unanimous approval has been received from
     *                              at least 51% of Reciprocal Net sites
     *                              and a timeout period has expired.)  The
     *                              exception to this rule is the
     *                              RECIPROCAL_NET_COORDINATOR, who may issue
     *                              a CLAIM at any time, without prior
     *                              PROPOSAL's.
     *    TRANSFER_INITIATE:        announces to all sites that the sender
     *                              is in the process of transferring the
     *                              specified block to another site, specified
     *                              by otherSiteId.  The transfer is considered
     *                              incomplete until the receiving site
     *                              announces a corresponding
     *                              TRANSFER_COMPLETE message to all sites. The
     *                              receiving site also has the option to send
     *                              a TRANSFER_REJECT announcement
     *    TRANSFER_REJECT:          announces to all sites that the sender has
     *                              rejected a transferred block from another
     *                              site, specified by otherSiteId.
     *    TRANSFER_COMPLETE:        announces to all sites that the sender has
     *                              received a transferred block from another
     *                              site, specified by otherSiteId.
     *    TRANSFER_REQUEST:         private message sent from one site to 
     *                              another requesting that an unused block be
     *                              transferred to the sender.  This request
     *                              should be made only in cases of emergency
     *                              (where an immediate shortage of sample id's
     *                              exists); the usual method for a site to
     *                              obtain a new block is to issue a PROPOSAL
     *                              and later a CLAIM.  The receiving site
     *                              should reply (relatively quickly) with 
     *                              either a TRANSFER_INITIATE announcement or
     *                              a TRANSFER_REQUEST_DENIED, at its
     *                              discretion.
     *    TRANSFER_REQUEST_DENIED:  private message sent in reply to a prior
     *                              TRANSFER_REQUEST to indicate the sending
     *                              site chooses to not transfer any blocks.
     */
    public int func;

    /**
     * A site id that has signifigance only when func is PROPOSAL_DISAPPROVED,
     * TRANSFER_INITIATE, TRANSFER_COMPLETE, and TRANSFER_REJECT.  For other 
     * func codes, this value should be SiteInfo.INVALID_SITE_ID.
     */
    public int otherSiteId;

    /**
     * The expiration date of this message; at present, its value is
     * significant only if func is PROPOSAL or TRANSFER_REQUEST.  In both cases
     * it indicates that sites that receive this ISM after the specified date
     * need not reply with an approval or disapproval.  In the PROPOSAL case,
     * however, receiving sites still are expected to update their own state
     * tables.  The field is optional and may be null.
     */
    public Date expiresAfter;

    /**
     * Default constructor; useful only when Class.forName() is invoked during
     * ISM decoding.
     */
    protected SampleIdBlockISM() {
	super();
	this.sourceSiteId = SiteInfo.INVALID_SITE_ID;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSampleManager = true;
	this.blockId = SampleIdBlock.INVALID_SAMPLE_ID_BLOCK_ID;
	this.func = INVALID_FUNC;
	this.otherSiteId = SiteInfo.INVALID_SITE_ID;
	this.expiresAfter = null;
    }

    /**
     * Constructor invoked by the various static factory methods.
     * and also by Class.forName during ISM decoding.
     */
    private SampleIdBlockISM(int localSiteId) {
	this();
	this.sourceSiteId = localSiteId;
    }

    /**
     * @return true if this message has an expiration date attached and the
     *     date is earlier than the system's current time.  Returns false
     *  othersise.
     */
    public boolean hasExpired() {
	return this.expiresAfter != null
	        && new Date().compareTo(this.expiresAfter) > 0;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	if (this.blockId != SampleIdBlock.INVALID_SAMPLE_ID_BLOCK_ID) {
	    DomUtil.createTextEl(realParent, "blockId", 
                    Integer.toString(this.blockId));
	}
	if (this.func != INVALID_FUNC) {
            DomUtil.createTextEl(realParent, "func", 
                    Integer.toString(this.func));
	}
	if (this.otherSiteId != SiteInfo.INVALID_SITE_ID) {
	    DomUtil.createTextEl(realParent, "otherSiteId",
		    Integer.toString(this.otherSiteId));
	}
	if (this.expiresAfter != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    DomUtil.createTextEl(realParent, "expiresAfter", 
                    sdf.format(this.expiresAfter));
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

	this.blockId = DomUtil.getTextForElAsInt(realParent, "blockId",
                SampleIdBlock.INVALID_SAMPLE_ID_BLOCK_ID);
	this.func = DomUtil.getTextForElAsInt(realParent, "func",
	        INVALID_FUNC);
        this.otherSiteId = DomUtil.getTextForElAsInt(realParent, "otherSiteId",
	        SiteInfo.INVALID_SITE_ID);
	try {
	    String expiresAfterStr = DomUtil.getTextForEl(realParent, 
                    "expiresAfter", false);
	    if (expiresAfterStr != null) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		this.expiresAfter = sdf.parse(expiresAfterStr);
	    } else {
		this.expiresAfter = null;
	    }
	} catch (ParseException ex) {
	    throw new SAXParseException("Invalid expiresAfter date format", 
                    null, ex);
	}

	return realParent;
    }

    public static SampleIdBlockISM newClaim(int localSiteId, int blockId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.blockId = blockId;
	msg.func = CLAIM;
	return msg;
    }

    public static SampleIdBlockISM newTransferInitiate(int localSiteId, 
            int blockId, int targetSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.blockId = blockId;
	msg.func = TRANSFER_INITIATE;
	msg.otherSiteId = targetSiteId;
	return msg;
    }

    public static SampleIdBlockISM newTransferComplete(int localSiteId, 
            int blockId, int targetSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.blockId = blockId;
	msg.func = TRANSFER_COMPLETE;
	msg.otherSiteId = targetSiteId;
	return msg;
    }

    public static SampleIdBlockISM newTransferReject(int localSiteId, 
            int blockId, int targetSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.blockId = blockId;
	msg.func = TRANSFER_REJECT;
	msg.otherSiteId = targetSiteId;
	return msg;
    }

    public static SampleIdBlockISM newProposalApproved(int localSiteId, 
            int blockId, int destSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.destSiteId = destSiteId;
	msg.blockId = blockId;
	msg.func = PROPOSAL_APPROVED;
	return msg;
    }

    public static SampleIdBlockISM newProposalDisapproved(int localSiteId, 
            int blockId, int destSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.destSiteId = destSiteId;
	msg.blockId = blockId;
	msg.func = PROPOSAL_DISAPPROVED;
	return msg;
    }

    /**
     * @param proposalPeriod ISM will expire this many milliseconds from now.
     */
    public static SampleIdBlockISM newProposal(int localSiteId, int blockId, 
            long proposalPeriod) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.blockId = blockId;
	msg.func = PROPOSAL;
        msg.expiresAfter = 
                new Date(msg.sourceDate.getTime() + proposalPeriod);
	return msg;
    }

    /**
     * @param validityPeriod ISM will expire this many milliseconds from now
     */
    public static SampleIdBlockISM newTransferRequest(int localSiteId, 
            int destSiteId, long validityPeriod) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.destSiteId = destSiteId;
	msg.func = TRANSFER_REQUEST;
        msg.expiresAfter = 
                new Date(msg.sourceDate.getTime() + validityPeriod);
	return msg;
    }

    public static SampleIdBlockISM newTransferRequestDenied(int localSiteId, 
            int destSiteId) {
	SampleIdBlockISM msg = new SampleIdBlockISM(localSiteId);
	msg.destSiteId = destSiteId;
	msg.func = TRANSFER_REQUEST_DENIED;
	return msg;
    }
}

