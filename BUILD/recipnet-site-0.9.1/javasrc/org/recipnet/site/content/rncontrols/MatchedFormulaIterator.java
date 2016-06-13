/*
 * Reciprocal Net project
 * 
 * MatchedFormulaIterator.java
 *
 * 19-Aug-2005: midurbin wrote the first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.search.SearchMatches;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * An {@code AbstractSampleTextIterator} subclass that iterates over the various
 * attributes that represent alternate empirical formulae that also matched the
 * search criteria indicated by the search fetched by the
 * {@code SearchResultsPage} in which this tag must be nested.
 * </p><p>
 * This {@code SampleTextContext} will only provide attributes of the following
 * types: {@code SampleTextBL.EMPIRICAL_FORMULA_DERIVED},
 * {@code SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT} and
 * {@code SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION}.
 * </p>
 */
public class MatchedFormulaIterator extends AbstractSampleTextIterator {

    /** The {@code SearchResultsPage} in which this tag is nested. */
    private SearchResultsPage searchResultsPage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.searchResultsPage = null;
    }

    /**
     * {@inheritDoc}; this version verifies that this tag resides in a
     * {@link SearchResultsPage}
     * 
     * @throws IllegalStateException if the {@code HtmlPage} returned by
     *         {@code getPage()} is not an instance of a
     *         {@code SearchResultsPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.searchResultsPage = (SearchResultsPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version returns a collection of attributes that
     * specifically matched the search performed for this tag's surrounding
     * {@code SearchResultPage} that represent alternative empirical formulae.
     */
    @Override
    public Collection<SampleTextInfo> getFilteredTextInfoCollection(
            SampleInfo sampleInfo) throws JspException {
        Collection<SampleTextInfo> textInfos = new ArrayList<SampleTextInfo>();

        if (sampleInfo == null) {
            /*
             * no SampleInfo was provided, therefore no SampleTextInfos will be
             * provided and this iterator will not evaluate its body.
             */
            return textInfos;
        }
        
        SearchParams sp = this.searchResultsPage.getSearchParams();
        SearchMatches sm = RequestCache.getSearchMatches(
                pageContext.getRequest(), sp, sampleInfo);

        if (sm == null) {
            sm = new SearchMatches(sp, sampleInfo);
            RequestCache.putSearchMatches(pageContext.getRequest(), sm, sp,
                    sampleInfo);
        }
        for (SampleAttributeInfo attr : sampleInfo.attributeInfo) {
            if (((attr.type == SampleTextBL.EMPIRICAL_FORMULA_DERIVED)
                    || (attr.type == SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT)
                    || (attr.type == SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION))
                    && !sm.getMatchesForField(attr).isEmpty()) {
                textInfos.add(attr);
            }
        }

        return textInfos;
    }
}
