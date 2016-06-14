/*
 * Reciprocal Net project
 * 
 * SiteContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.SiteInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code SiteInfo} appropriate for some implementation-defined purpose.
 */
public interface SiteContext {

    /**
     * Provides the site information appropriate for this context. When the
     * {@code SiteContext} implementation is a phase-recognizing custom tag, its
     * site information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code SiteInfo} object representing the site information for
     *         this context, or {@code null} if none is relevant or (yet)
     *         available. Typically, this object is <em>live</em>, in that
     *         changes to it will be visible to this context and to other
     *         objects that retrieve this context's info.
     */
    public SiteInfo getSiteInfo();
}
