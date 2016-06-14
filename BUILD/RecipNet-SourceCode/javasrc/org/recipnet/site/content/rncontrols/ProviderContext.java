/*
 * Reciprocal Net project
 * 
 * ProviderContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.ProviderInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code ProviderInfo} appropriate for some implementation-defined purpose.
 */
public interface ProviderContext {

    /**
     * Provides the provider information appropriate for this context. When the
     * {@code ProviderContext} implementation is a phase-recognizing custom tag,
     * its provider information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code ProviderInfo} object representing the provider
     *         information for this context, or {@code null} if none is relevant
     *         or (yet) available. Typically, this object is <em>live</em>,
     *         in that changes to it will be visible to this context and to
     *         other objects that retrieve this context's info.
     */
    public ProviderInfo getProviderInfo();
}
