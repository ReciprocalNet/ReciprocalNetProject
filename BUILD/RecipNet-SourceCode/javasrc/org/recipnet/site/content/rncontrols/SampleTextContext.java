/*
 * Reciprocal Net project
 * 
 * SampleTextContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 30-Aug-2004: midurbin added getTextType()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * An interface defining a contract by which objects may provide a
 * {@code SampleTextInfo} and text type pair, appropriate for some
 * implementation-defined purpose.
 */
public interface SampleTextContext {

    /**
     * Provides the sample text information appropriate for this context. When
     * the {@code SampleTextContext} implementation is a phase-recognizing
     * custom tag, its sample information is typically not avilable until the
     * {@code FETCHING_PHASE}.
     * 
     * @return a {@code SampleTextInfo} object representing the sample text
     *         information for this context, or {@code null} if none is relevant
     *         or (yet) available. Typically, this object is <em>live</em>,
     *         in that changes to it will be visible to this context and to
     *         other objects that retrieve this context's info.
     */
    public SampleTextInfo getSampleTextInfo();

    /**
     * Provides the text type code of the {@code SampleTextInfo} that will be
     * made available by {@link #getSampleTextInfo()}, or
     * {@code SampleTextInfo.INVALID_TEXT_TYPE} if that information has yet to
     * be determined. In the latter case, the code returned may be different at
     * some later time when the text type has been determined, but if ever an
     * instance returns any other code then it will not subsequently return a
     * different one. Whenever {@code getSampleTextInfo()} returns a non-{@code null}
     * value, this method will return the corresponding text type code.
     * 
     * @return the text type code of the {@code SampleTextInfo} that will be
     *         returned by a call to {@code getSampleTextInfo()} or
     *         {@code SampleTextInfo.INVALID_TYPE} if the text type is not known
     *         yet.
     */
    public int getTextType();
}
