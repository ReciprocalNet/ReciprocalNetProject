/*
 * Reciprocal Net project
 * 
 * ProviderContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.LabInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code LabInfo} appropriate for some implementation-defined purpose.
 */
public interface LabContext {

    /**
     * Provides the laboratory information appropriate for this context. When
     * the {@code LabContext} implementation is a phase-recognizing custom tag,
     * its laboratory information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code LabInfo} object representing the laboratory information
     *         for this context, or {@code null} if none is relevant or (yet)
     *         available. Typically, this object is <em>live</em>, in that
     *         changes to it will be visible to this context and to other
     *         objects that retrieve this context's info.
     */
    public LabInfo getLabInfo();

}
