/*
 * Reciprocal Net project
 * @(#)SampleStatusHintCM.java
 *
 * 17-Feb-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 01-Jan-2009: ekoperda added new fields localSiteNewlyAuthoritative and 
 *              localSiteNewlyNonAuthoritative
 */

package org.recipnet.site.core.msg;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A core message that Sample Manager sends to Repository Manager as a "hint"
 * whenever a sample for which the local site is authoritative changes from
 * being public to non-public or vice versa, or whenever the local site becomes
 * newly authoritative or non-authoritative for a particular sample.  
 * Repository Manager may use these messages as triggers to generate
 * RepositoryHoldingISM's for the specified sample (as appropriate), but is
 * under no obligation to do so.  If the <code>ismToSend</code> field is
 * populated then Repository Manager must send a <code>SendIsmCM</code> that
 * contains the ISM to Site Manager, but only once all of Repository Manager's
 * own ISM's (if any) have been sent to Site Manager.
 */
public class SampleStatusHintCM extends CoreMessage {
    public static enum Trigger {
	NONE,
	SAMPLE_NEWLY_PUBLIC,
	SAMPLE_NEWLY_NONPUBLIC,
	LOCAL_SITE_NEWLY_AUTHORITATIVE,
	LOCAL_SITE_NEWLY_NONAUTHORITATIVE
    }

    /** id of the sample whose status or context just changed */
    public int sampleId;

    /** describes the status or context change that just occurred */
    public Trigger trigger;

    /**
     * If set, the ISM that the message recipient must pass to Site Manager for
     * transmission.  May be null.
     */
    public InterSiteMessage ismToSend;

    /** default constructor */
    public SampleStatusHintCM() {
	this.sampleId = SampleInfo.INVALID_SAMPLE_ID;
	this.trigger = Trigger.NONE;
	this.ismToSend = null;
    }

    /** constructor that completely fills this object's member variables */
    public SampleStatusHintCM(int sampleId, Trigger trigger, 
            InterSiteMessage ismToSend) {
	this.sampleId = sampleId;
	this.trigger = trigger;
	this.ismToSend = ismToSend;
    }
}
