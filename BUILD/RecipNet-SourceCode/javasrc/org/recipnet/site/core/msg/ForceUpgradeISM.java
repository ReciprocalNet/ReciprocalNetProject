/*
 * Reciprocal Net project
 * @(#)ForceUpgradeISM.java
 *
 * 28-Nov-2008: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is sent from the Reciprocal Net coordinator
 * to all sites whenever it needs to force sites to upgrade to a new release of
 * the Reciprocal Net Site Software.  Sites should fail to process this message
 * if their local site software is older than the version specified in this
 * ISM.  This will cause the ISM queue to stall and no further Coordinator
 * ISM's in the stream to be processed.
 *
 * Once the local site software is upgraded to a compliant version, this
 * message should be "processed" simply by flagging it as processed.  (No
 * further database updates are required.)  Thus the Coordinator's ISM stream
 * is un-stalled and the local site is free to process any additional ISM's
 * coming later in the stream.
 *
 * This functionality is useful when some downstream ISM's from the Coordinator
 * need to be protected; that is, when downstream messages might be
 * misinterpreted by sites running outdated software.
 *
 * Note that the version comparison performed is ASCII-stringwise only.  This
 * class contains no logic to parse complex version numbers or expand numerals.
 */
public class ForceUpgradeISM extends InterSiteMessage {    
    /** Identifies the minimum desired version number. */
    public String version;

    /** Default constructor */
    public ForceUpgradeISM() {
	super();
	version = null;
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  This constructor is useful only to the Reciprocal
     * Net Coordinator, because he is the only entity who may transmit this
     * message.
     */
    public ForceUpgradeISM(String version) {
	super();
	this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSiteManager = true;
	this.version = version;
    }

    /**
     * Returns true if and only if versionToCheck equals this ISM's version or
     * if versionToCheck is higher than this ISM's version.  This comparison is
     * ASCII-stringwise only; there is no logic for parsing complex version
     * number schemes nor to expand numerals.
     */
    public boolean checkVersion(String versionToCheck) {
	return this.version.compareTo(versionToCheck) <= 0;
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	DomUtil.createTextEl(realParent, "version", this.version);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Element realParent = (Element) super.extractFromDom(doc, base);
	this.version = DomUtil.getTextForEl(realParent, "version", true);
	return realParent;
    }	
}
