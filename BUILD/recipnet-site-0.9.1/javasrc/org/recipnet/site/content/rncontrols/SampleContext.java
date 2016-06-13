/*
 * Reciprocal Net project
 * 
 * SampleContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.SampleInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code SampleInfo} appropriate for some implementation-defined purpose.
 */
public interface SampleContext {

    /**
     * Provides the sample information appropriate for this context. When the
     * {@code SampleContext} implementation is a phase-recognizing custom tag,
     * its sample information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code SampleInfo} object representing the sample information
     *         for this context, or {@code null} if none is relevant or (yet)
     *         available. Typically, this object is <em>live</em>, in that
     *         changes to it will be visible to this context and to other
     *         objects that retrieve this context's info.
     */
    public SampleInfo getSampleInfo();
}
