/*
 * Reciprocal Net project
 * 
 * SearchResultsIterator.java
 * 
 * 10-Aug-2004: cwestnea wrote first draft
 * 02-Nov-2004: midurbin factored code out to the PaginationContext and
 *              SearchResultsContext and added an ErrorSupplier implementation
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 21-Jun-2005: midurbin moved the ErrorSupplier implementation to the
 *              superclass
 * 06-Oct-2005: midurbin updated references to a sample's provider
 * 14-Jun-2006: jobollin reformatted the source
 * 09-Jan-2008: ekoperda fixed bug #1878 in onFetchingPhaseBeforeBody()
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.common.controls.PaginationContext;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * This tag iterates through a set of {@code SampleInfo} objects representing
 * the results of the search request defined by the {@code SearchResultsConext}
 * that most immediately surrounds this tag. This tag may optionally recognize a
 * {@code PaginationContext} that will provide paging information.
 * </p><p>
 * This tag provides {@code SampleContexts} for the search results and the
 * corresponding {@code LabContexts} and {@code ProviderContexts} for the
 * samples.
 * </p>
 */
public class SearchResultsIterator extends HtmlPageIterator implements
        LabContext, ProviderContext, SampleContext {

    /**
     * An error flag to indicate that the sample currently provided by this
     * tag's {@code SampleContext} has been changes since the search was
     * performed.
     */
    public static final int IS_MORE_RECENT_THAN_SEARCH
            = HtmlPageIterator.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return IS_MORE_RECENT_THAN_SEARCH;
    }

    /**
     * The most immediate {@code SearchResultsContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()} and used by
     * {@code onFetchingPhaseBeforeBody()} to get the search results.
     */
    private SearchResultsContext searchResultsContext;

    /**
     * The most immeidate {@code PaginationContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()} if the 'usePaginationContext'
     * property has been set to true. If set, this context is used to override
     * the value of 'firstResultIndex' and 'finalResultIndex'.
     */
    private PaginationContext paginationContext;

    /**
     * An optional property that indicates the starting point in the search
     * results that this {@code SearchResultsIterator} will cover. If unset,
     * value defaults to 1. This is an <i>inclusive</i> lower bound.
     */
    private int firstResultIndex;

    /**
     * An optional property that indicates the ending point in the search
     * results that this {@code SearchResultsIterator} will cover. If unset,
     * value defaults to 10. This is an <i>inclusive</i> upper bound.
     */
    private int finalResultIndex;

    /**
     * An optional property that when set to true indicates that this tag will
     * determine 'firestResultIndex' and 'finalResultIndex' from the
     * {@code PaginationContext} that surrounds this tag. This value defaults to
     * false, and must not be set to true unless this tag is nested within a
     * {@code PaginationContext} implementing tag.
     */
    private boolean usePaginationContext;

    /**
     * A collection containing all the samples that this iterator should iterate
     * over. Initialized in {@code reset()} and retrieved from the database in
     * {@code onFetchingPhase()}.
     */
    private Collection<SampleInfo> sampleCollection;

    /**
     * An iterator which is used to iterate over {@code sampleCollection}. It
     * is set in {@code beforeIteration()}.
     */
    private Iterator<SampleInfo> sampleIterator;

    /**
     * The current sample we are providing info for. Retrieved from
     * {@code sampleCollection} in {@code onIterationBeforeBody()}.
     */
    private SampleInfo sampleInfo;

    /**
     * The lab of the sample we are providing info for. Retrieved using the
     * {@code sampleInfo} in {@code onIterationBeforeBody()}.
     */
    private LabInfo labInfo;

    /**
     * The provider of the sample we are providing info for. Retrieved using the
     * {@code sampleInfo} in {@code onIterationBeforeBody()}.
     */
    private ProviderInfo providerInfo;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.searchResultsContext = null;
        this.paginationContext = null;
        this.firstResultIndex = 1;
        this.finalResultIndex = 10;
        this.sampleCollection = null;
        this.sampleIterator = null;
        this.sampleInfo = null;
        this.labInfo = null;
        this.providerInfo = null;
    }

    /**
     * Implements {@code SampleContext}; returns the current {@code SampleInfo}
     * object.
     */
    public SampleInfo getSampleInfo() {
        return this.sampleInfo;
    }

    /**
     * Implements {@code SampleContext}; returns the lab of the current
     * {@code SampleInfo} object.
     */
    public LabInfo getLabInfo() {
        return this.labInfo;
    }

    /**
     * Implements {@code SampleContext}; returns the provider of the current
     * {@code SampleInfo} object.
     */
    public ProviderInfo getProviderInfo() {
        return this.providerInfo;
    }

    /**
     * @return the index at which this iterator will start
     */
    public int getFirstResultIndex() {
        return this.firstResultIndex;
    }

    /**
     * @param firstResultIndex the index at which this iterator should start
     */
    public void setFirstResultIndex(int firstResultIndex) {
        this.firstResultIndex = firstResultIndex;
    }

    /**
     * @return the index at which this iterator will stop
     */
    public int getFinalResultIndex() {
        return this.finalResultIndex;
    }

    /**
     * @param finalResultIndex the index at which this iterator should stop
     */
    public void setFinalResultIndex(int finalResultIndex) {
        this.finalResultIndex = finalResultIndex;
    }

    /**
     * @param use indicates that the {@code PaginationContext} should be used to
     *        determine the range of results over which to iterate. If set to
     *        true, this tag must be nested within a {@code PaginationContext}.
     */
    public void setUsePaginationContext(boolean use) {
        this.usePaginationContext = use;
    }

    /**
     * @return a boolean that indicates whether the {@code PaginationContext}
     *         should be used to determine the range of results over which to
     *         iterate
     */
    public boolean getUsePaginationContext() {
        return this.usePaginationContext;
    }

    /**
     * @return the position of the current sample in the search results list,
     *         starting from 'firstResultIndex'.
     */
    public int getCurrentResultIndex() {
        return this.getIterationCountSinceThisPhaseBegan()
                + this.firstResultIndex;
    }

    /**
     * {@inheritDoc}; this version prevents the IS_MORE_RECENT_THAN_SEARCH flag
     * from being set generally because it is specific to an individual
     * iteration.
     * 
     * @throws IllegalArgumentException if an error flag that is
     *         iteration-specific is supplied
     */
    @Override
    public void setErrorFlag(int errorFlag) {
        if ((errorFlag & IS_MORE_RECENT_THAN_SEARCH) != 0) {
            /*
             * the specified error flag relates to a specific iteration and
             * should not be set with this method because this method will
             * affect ALL iterations
             */
            /*
             * Note: if there were any way to unset error flags, this would be
             * implemented differently
             */
            throw new IllegalArgumentException();
        }
        super.setErrorFlag(errorFlag);
    }

    /**
     * {@inheritDoc}; this version to allows iteration-specific (not general)
     * error flags to be returned.
     */
    @Override
    public int getErrorCode() {
        int extraFlags = ((getPage().getPhase() != HtmlPage.REGISTRATION_PHASE)
                && (getPage().getPhase() != HtmlPage.PARSING_PHASE)
                && getSampleInfo().isMoreRecentThanSearch)
                ? IS_MORE_RECENT_THAN_SEARCH
                : 0x0;
        
        /*
         * the IS_MORE_RECENT_THAN_SEARCH flag corresponds to the iteration
         * during which this method is called, not the whole
         * SearchResultsIterator
         */
        
        return super.getErrorCode() | extraFlags;
    }

    /**
     * {@inheritDoc}; this version looks up the appropriate
     * {@code SearchResultsContext} and {@code PaginationContext} if needed.
     * 
     * @throws IllegalStateException if a needed context is missing
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get SearchResultsContext
        this.searchResultsContext = findRealAncestorWithClass(this,
                SearchResultsContext.class);
        if (this.searchResultsContext == null) {
            throw new IllegalStateException();
        }

        if (this.usePaginationContext) {
            this.paginationContext = findRealAncestorWithClass(this,
                    PaginationContext.class);
            if (this.paginationContext == null) {
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version retrieves the search results from the
     * {@code SearchResultsContext} and fetches and caches any needed container
     * objects.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (this.usePaginationContext) {
            assert (this.paginationContext != null);
            this.firstResultIndex = (this.paginationContext.getPageSize()
                    * (this.paginationContext.getPageNumber() - 1)) + 1;
            this.finalResultIndex = this.firstResultIndex
                    + this.paginationContext.getPageSize() - 1;
        }
        this.sampleCollection = this.searchResultsContext.getSearchResults(
                this.firstResultIndex, this.finalResultIndex
                        - this.firstResultIndex + 1);

	if (this.sampleCollection != null) {
	    for (SampleInfo sample : this.sampleCollection) {
		/*
		 * the current sample should NOT be cached because it has been
		 * marked specific to this search
		 */

		CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());
		ServletRequest request = this.pageContext.getRequest();
		
		try {
		    // get the lab for the sample and make sure it is cached
		    LabInfo lab 
                            = RequestCache.getLabInfo(request, sample.labId);

		    if (lab == null) {
			lab = cc.getSiteManager().getLabInfo(sample.labId);
			RequestCache.putLabInfo(request, lab);
		    }

		    // get the provider for the sample and make sure it is
		    // cached
		    ProviderInfo provider = RequestCache.getProviderInfo(
                            request, sample.dataInfo.providerId);

		    if (provider == null) {
			provider = cc.getSiteManager().getProviderInfo(
		                sample.dataInfo.providerId);
			RequestCache.putProviderInfo(request, provider);
		    }
		} catch (RemoteException ex) {
		    cc.reportRemoteException(ex);
		    throw new JspException(ex);
		} catch (OperationFailedException ex) {
		    throw new JspException(ex);
		}
	    }
	}

        return rc;
    }

    /**
     * {@inheritDoc}; this implementation gets the {@code sampleIterator} if
     * there are samples to be iterated over.
     */
    @Override
    protected void beforeIteration() {
        if (this.sampleCollection != null) {
            this.sampleIterator = this.sampleCollection.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version populates the data objects if there are
     * iterations remaining.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        int index = getIterationCountSinceThisPhaseBegan();

        if (this.sampleIterator == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                    && (getPostedIterationCount() > index)) {
                /*
                 * evaluate the body for the benefit of nested elements, even
                 * though those nested elements may not yet get the
                 * SampleContext. This ensures that the SampleFields can parse
                 * values that might have been posted for this request
                 */
                return true;
            }
            return false;
        }
        if (this.sampleIterator.hasNext()) {
            this.sampleInfo = this.sampleIterator.next();
            if (this.sampleInfo == null) {
                return false;
            }
            this.labInfo = RequestCache.getLabInfo(
                    this.pageContext.getRequest(), this.sampleInfo.labId);
            this.providerInfo = RequestCache.getProviderInfo(
                    this.pageContext.getRequest(),
                    this.sampleInfo.dataInfo.providerId);
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SearchResultsIterator dc = (SearchResultsIterator) super.generateCopy(
                newId, map);

        dc.paginationContext
                = (PaginationContext) map.get(this.paginationContext);
        dc.searchResultsContext
                = (SearchResultsContext) map.get(this.searchResultsContext);

        return dc;
    }
}
