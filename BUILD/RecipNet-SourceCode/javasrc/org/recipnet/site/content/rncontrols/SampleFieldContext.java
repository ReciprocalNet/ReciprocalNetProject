/*
 * Reciprocal Net project
 * 
 * SampleFieldContext.java
 *
 * 10-Jun-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.SampleFieldRecord;

/**
 * An interface extending SampleTextContext and defining a contract by which
 * objects may additionally provide a {@code SampleFieldRecord} appropriate for
 * some implementation-defined purpose.
 */
public interface SampleFieldContext extends SampleTextContext {

    /**
     * Provides the sample field information appropriate for this context. When
     * the {@code SampleFieldContext} implementation is a phase-recognizing
     * custom tag, its sample information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code SampleFieldRecord} object representing the sample field
     *         information for this context, or {@code null} if none is relevant
     *         or (yet) available. Typically, this object is <em>live</em>,
     *         in that changes to it will be visible to this context and to
     *         other objects that retrieve this context's info.
     */
    public SampleFieldRecord getSampleField();
}
