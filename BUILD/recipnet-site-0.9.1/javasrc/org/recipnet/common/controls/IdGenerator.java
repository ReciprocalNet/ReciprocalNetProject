/*
 * Reciprocal Net project
 * 
 * IdGenerator.java
 * 
 * 22-Jun-2004: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * {@code HtmlPageElement} maintains 'real' elements for the 'proxy' elements
 * that exist during each evaluation of its body. 'Proxy' elements are grouped
 * with their 'real' counterpart by having matching ID values. For this purpose,
 * a {@code IdGenerator} implementation can be consulted to provide a unique ID
 * for these fields given various contraints. For instance, the simplest
 * implementation could simply return predictably incrementing numbers but would
 * require that nested fields (that don't have id values) maintain their count
 * and order between phases.
 */
public interface IdGenerator {

    /**
     * Systematically generates ID values such that a nested
     * {@code HtmlPageElement} will recieve the same ID for each phase (and thus
     * be associated with the same 'real' element).
     * 
     * @param prefix an {@code String} that may be used as part of the entire
     *        id. This value may be null.
     * @return a {@code String()} that should be used as an ID for a nested
     *         {@code HtmlPageElement}. (note, exactly ONE call to this method
     *         should be made for each {@code HtmlPageElement})
     */
    public String getNextId(String prefix);

}
