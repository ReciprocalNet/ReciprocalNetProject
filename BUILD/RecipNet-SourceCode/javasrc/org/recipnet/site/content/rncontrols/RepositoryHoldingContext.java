/*
 * Reciprocal Net project
 * 
 * RepositoryHoldingContext.java
 * 
 * 20-Oct-2004: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.RepositoryHoldingInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code RepositoryHoldingInfo} appropriate for some implementation-defined
 * purpose.
 */
public interface RepositoryHoldingContext {

    /**
     * Provides the repository holding information appropriate for this context.
     * When the {@code RepositoryHoldingContext} implementation is a
     * phase-recognizing custom tag, its repository holding information is
     * typically not avilable until the {@code FETCHING_PHASE}.
     * 
     * @return a {@code RepositoryHoldingInfo} object for this representing the
     *         repository holding information for this context, or {@code null}
     *         if none is relevant or (yet) available. Typically, this object is
     *         <em>live</em>, in that changes to it will be visible to this
     *         context and to other objects that retrieve this context's info.
     */
    public RepositoryHoldingInfo getRepositoryHoldingInfo();
}
