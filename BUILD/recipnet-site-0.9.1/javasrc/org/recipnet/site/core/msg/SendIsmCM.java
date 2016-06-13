/*
 * Reciprocal Net project
 * 
 * SendIsmCM.java
 *
 * 27-Sep-2002: ekoperda wrote first draft
 * 21-Apr-2006: jobollin added type parameters and reformatted the source
 */

package org.recipnet.site.core.msg;

/**
 * A core message that a core module sends to Site Manager when it wants to
 * transmit an ISM. This CoreMessage-type encapsulates the ISM that is to be
 * signed and then transmitted. Even SiteManager uses this sort of CoreMessage
 * to queue its ISM's for asynchronous transmission.
 */
public class SendIsmCM extends CoreMessage implements Comparable<SendIsmCM> {
    public InterSiteMessage ismToTransmit;

    public SendIsmCM() {
        this(null);
    }

    public SendIsmCM(InterSiteMessage ismToTransmit) {
        super();
        this.ismToTransmit = ismToTransmit;
    }

    /**
     * From interface Comparable; the natural ordering of these CM's is by
     * destSiteId and then SourceSeqNum of the encapsulated ISM.  Users should
     * note that this ordering is not consistent with equals().
     */
    public int compareTo(SendIsmCM x) {
        if (this.ismToTransmit.destSiteId < x.ismToTransmit.destSiteId) {
            return -1;
        } else if (this.ismToTransmit.destSiteId > x.ismToTransmit.destSiteId) {
            return 1;
        } else {
            if (this.ismToTransmit.sourceSeqNum < x.ismToTransmit.sourceSeqNum) {
                return -1;
            } if (this.ismToTransmit.sourceSeqNum > x.ismToTransmit.sourceSeqNum) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
