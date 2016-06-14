/*
 * Reciprocal Net project
 * 
 * SampleHistoryContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.SampleHistoryInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code SampleHistoryInfo} appropriate for some implementation-defined
 * purpose.
 */
public interface SampleHistoryContext {

    /**
     * Provides the sample history information appropriate for this context.
     * When the {@code SampleHistoryContext} implementation is a
     * phase-recognizing custom tag, its sample history information is typically
     * not avilable until the {@code FETCHING_PHASE}.
     * 
     * @return a {@code SampleHistoryInfo} object representing the sample
     *         history information for this context, or {@code null} if none is
     *         relevant or (yet) available. Typically, this object is
     *         <em>live</em>, in that changes to it will be visible to this
     *         context and to other objects that retrieve this context's info.
     */
    public SampleHistoryInfo getSampleHistoryInfo();
}
