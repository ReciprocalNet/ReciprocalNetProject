/*
 * Reciprocal Net project
 * 
 * SiteStatisticsRequestISM.java
 *
 * 10-Jul-2003: midurbin wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.core.msg;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An Inter-site message sent by the coordinator requesting that site statistics
 * be sent to a specified site.
 */
public class SiteStatisticsRequestISM extends InterSiteMessage {

    /**
     * The {@code siteId} of the site where the {@code SiteStatisticsISM }
     * should be sent. (usually the portal)
     */
    public int collectionSiteId;

    /**
     * Indicates whether statistics counters should be reset after reporting
     * there current values.
     */
    public boolean resetCounters;

    /**
     * The expiration date of this message, indicating that sites that receive
     * this ISM after the specified date need not reply, nor reset thier
     * staistics counters. The field is optional and may be null.
     */
    public Date expirationDate;

    /** Default constructor */
    public SiteStatisticsRequestISM() {
        this.collectionSiteId = SiteInfo.INVALID_SITE_ID;
        this.resetCounters = false;
        this.expirationDate = null;
    }

    /**
     * Creates a new message and completely fills is members (for convenience).
     * This constructor is useful only to the Reciprocal Net Coordinator,
     * because he is the only entity who may transmit this mesage.
     */
    public SiteStatisticsRequestISM(int destSiteId, int collectionSiteId,
            boolean reset, Date expirationDate) {
        super.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
        super.destSiteId = destSiteId;
        super.deliverToSiteManager = true;
        this.collectionSiteId = collectionSiteId;
        this.resetCounters = reset;
        this.expirationDate = expirationDate;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
        Node realParent = super.insertIntoDom(doc, base);
        SimpleDateFormat sdf = new SimpleDateFormat(
                InterSiteMessage.DATE_FORMAT);
        
        DomUtil.createTextEl(realParent, "collectionSiteId",
                Integer.toString(this.collectionSiteId));
        if (this.resetCounters) {
            DomUtil.createEl(doc, realParent, "resetCounters");
        }
        if (this.expirationDate != null) {
            DomUtil.createTextEl(realParent, "expirationDate",
                    sdf.format(this.expirationDate));
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
        SimpleDateFormat sdf = new SimpleDateFormat(
                InterSiteMessage.DATE_FORMAT);
        
        this.collectionSiteId = DomUtil.getTextForElAsInt(realParent,
                "collectionSiteId");
        this.resetCounters = DomUtil.isElPresent(realParent, "resetCounters");
        if (DomUtil.isElPresent(realParent, "expirationDate")) {
            try {
                this.expirationDate = sdf.parse(DomUtil.getTextForEl(
                        realParent, "expirationDate", true));
            } catch (ParseException ex) {
                throw new SAXParseException("invalid date format", null, ex);
            }
        }
        
        return realParent;
    }
}
