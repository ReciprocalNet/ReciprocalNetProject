/*
 * Reciprocal Net project
 * @(#)SampleHoldingCM.java
 *
 * 15-Oct-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 */

package org.recipnet.site.core.msg;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A core message that Repository Manager sends to Sample Manager whenever the
 * "holding level" for a sample changes.  Sample Manager uses this informaiton
 * to maintain its own (non-authoritative) database table about which samples
 * are held locally and which are not in order to improve search performance.
 */
public class SampleHoldingCM extends CoreMessage {
    public int sampleId;

    /** 
     * Possible values are RepositoryHoldingInfo.NO_DATA, 
     * RepositoryHoldingInfo.BASIC_DATA, and RepositoryHoldingInfo.FULL_DATA. 
     */
    public int replicaLevel;

    public SampleHoldingCM() {
	this(SampleInfo.INVALID_SAMPLE_ID, RepositoryHoldingInfo.NO_DATA);
    }

    public SampleHoldingCM(int sampleId, int replicaLevel) {
	super();
	this.sampleId = sampleId;
	this.replicaLevel = replicaLevel;
    }
}

