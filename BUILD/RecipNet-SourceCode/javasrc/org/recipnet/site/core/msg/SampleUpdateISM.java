/*
 * Reciprocal Net project
 * @(#)SampleUpdateISM.java
 *
 * 29-Jan-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 12-Jul-2005: ekoperda modified constructor to match SampleInfo spec change
 */

package org.recipnet.site.core.msg;
import java.util.Date;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Inter-site message that is broadcast from one site to all sites whenever an
 * existing sample record needs to be updated.
 */
public class SampleUpdateISM extends InterSiteMessage {    
    public SampleInfo updatedSample;

    /** Default constructor */
    public SampleUpdateISM() {
	super();
	updatedSample = new SampleInfo();
    }

    /** 
     * Creates a new message and completely fills its members (for 
     * convenience).  The specified <code>newSample</code> object is cloned
     * and sanitized in order to make it suitable for replication.
     */
    public SampleUpdateISM(int localSiteId, SampleInfo updatedSample) {
	super();
	this.sourceSiteId = localSiteId;
	this.sourceDate = new Date();
	this.destSiteId = InterSiteMessage.ALL_SITES;
	this.deliverToSampleManager = true;
	this.updatedSample 
                = SampleTextBL.sanitizeSampleForExport(updatedSample);
    }

    /**
     * From interface DomTreeParticipant, overrides function on
     * InterSiteMessage.
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
	Node realParent = super.insertIntoDom(doc, base);
	updatedSample.insertIntoDom(doc, realParent);
	return realParent;
    }

    /**
     * From interface DomTreeParticipant, overrides function on 
     * InterSiteMessage.
     */
    @Override
    public Node extractFromDom(Document doc, Node base) throws SAXException {
	Node realParent = super.extractFromDom(doc, base);
	updatedSample.extractFromDom(doc, realParent.getFirstChild());
	return realParent;
    }
}

