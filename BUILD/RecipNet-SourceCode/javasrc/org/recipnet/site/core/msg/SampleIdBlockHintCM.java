/*
 * Reciprocal Net project
 * @(#)SampleIdBlockHintCM.java
 *
 * 24-Oct-2002: ekoperda wrote first draft
 */

package org.recipnet.site.core.msg;

/**
 * A core message that Sample Manager sends to itself as a "hint" that it might
 * need to take action about a dwindling number of sample id blocks.  Such a
 * message is necessary because Sample Manager consumes sample id's in webapp
 * threads (via calls to SampleManager.putSampleInfo()) but obtains new sample
 * id's from code in worker threads that communicates with other sites.
 */
public class SampleIdBlockHintCM extends CoreMessage {
    /** 
     * There aren't any members of this class because no
     * additional data is required for this message.
     */
}

