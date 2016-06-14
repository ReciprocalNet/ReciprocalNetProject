/*
 * Reciprocal Net project
 * 
 * FileContext.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 02-May-2005: ekoperda altered spec to expose a SampleDataFile object
 *              rather than a portion of a RepositoryFiles object
 * 12-Jan-2006: jobollin reformatted the docs
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.site.shared.SampleDataFile;

/**
 * This context is designed to be implemented by JSP custom tags that know how
 * to expose information about a particular sample data file to other JSP
 * custom tags that may be nested within them.  The choice of which particular
 * sample data file is exposed here is defined by specific implementations.
 */
public interface FileContext {
    
    /**
     * Provides the {@code SampleDataFile} represented by this context, any time
     * during or after the {@code FETCHING_PHASE}; may return {@code null} if
     * the context is nonfunctional for some reason
     * 
     * @return a reference to the {@code SampleDataFile} object for this
     *         {@code FileContext}.
     *     
     * @throws IllegalStateException if the current phase is earlier than
     *         {@code FETCHING_PHASE}.
     */
    public SampleDataFile getSampleDataFile();  
}
