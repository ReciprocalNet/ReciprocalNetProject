/*
 * Reciprocal Net project
 * 
 * SearchResultIndex.java
 * 
 * 12-Apr-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * A simple tag that, when nested within a {@code SearchResultsIterator},
 * outputs at each iteration the search result index (as obtained by a call to
 * {@link SearchResultsIterator#getCurrentResultIndex getCurrentResultIndex()}).
 */
public class SearchResultIndex extends HtmlPageElement {

    /**
     * {@inheritDoc}; this version looks up the {@code SearchResultsIterator},
     * determines the index of the current search result and writes it to the
     * provided {@code JspWriter}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SearchResultsIterator}
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        SearchResultsIterator searchResultsIterator
                = findRealAncestorWithClass(this, SearchResultsIterator.class);
        
        if (searchResultsIterator == null) {
            throw new IllegalStateException();
        }
        out.print(searchResultsIterator.getCurrentResultIndex());
        
        return super.onRenderingPhaseAfterBody(out);
    }
}
