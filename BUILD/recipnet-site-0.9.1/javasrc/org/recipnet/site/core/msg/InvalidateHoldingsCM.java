/*
 * Reciprocal Net project
 * @(#)InvalidateHoldingsCM.java
 *
 * 30-Nov-2008: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * A core message that Site Manager sends to Repository Manager whenever all
 * recorded repository holdings associated with a specified site need to be
 * deleted.  This may occur in response to some remote site being deactivated,
 * for example.
 */
public class InvalidateHoldingsCM extends CoreMessage {
    /** identifies the remote site. */
    public int siteId;

    public InvalidateHoldingsCM() {
	this(SiteInfo.INVALID_SITE_ID);
    }

    public InvalidateHoldingsCM(int siteId) {
	super();
	this.siteId = siteId;
    }
}

