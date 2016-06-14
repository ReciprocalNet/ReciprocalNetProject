/*
 * Reciprocal Net project
 * 
 * MultiFilenameContext.java
 * 
 * 04-Aug-2005: midurbin wrote first draft
 * 11-Jan-2006: jobollin reformatted this header comment
 */
package org.recipnet.site.content.rncontrols;

import java.util.Collection;

/**
 * This context is designed to be implemented by JSP custom tags that pertain
 * to multiple files.  Implementing tags should clearly define the
 * significance of the filenames.  This includes: the sample to which they
 * belong, the purpose for their selection as well as any other relevent
 * characteristic.
 */
public interface MultiFilenameContext {
    
    /**
     * A method that provides data to context-recognizing controls.  This
     * method should only be called during or after the
     * <code>FETCHING_PHASE</code>.
     * @return a collection of filenames
     * @throws IllegalStateException if the current phase is earlier than
     *     <code>FETCHING_PHASE</code>.
     */
    public Collection<String> getFilenames();  
}
