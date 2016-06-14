/*
 * Reciprocal Net project
 * 
 * SearchButton.java
 * 
 * 19-Aug-2004: cwestnea wrote first draft
 * 27-Jul-2005: midurbin updated onClick() to invoke triggerSearch() instead of
 *              doSearch()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;

/**
 * This tag very simply triggers a search on the search page. All of the
 * validation and actual searching is handled by {@code SearchPage}. For this
 * reason, this button must be nested within a {@code SearchPage}.
 */
public class SearchButton extends ButtonHtmlControl {

    /**
     * {@inheritDoc}; this version triggers a search by the host
     * {@code SearchPage} by invoking its {@code triggerSearch()} method
     * 
     * @throws IllegalStateException if the surrounding page is not a search
     *         page
     */
    @Override
    protected void onClick(
            @SuppressWarnings("unused") PageContext pageContext) {
        try {
            ((SearchPage) getPage()).triggerSearch();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }
    }
}
