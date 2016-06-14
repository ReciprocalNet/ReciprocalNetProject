/*
 * Reciprocal Net project
 * 
 * SearchResultsPage.java
 *
 * 02-Nov-2004: midurbin wrote first draft
 * 17-Jan-2005: jobollin modified doBeforeBody() to output hidden <input>
 *              elements via addFormContent()
 * 05-Jul-2005: midurbin replaced calls to addFormContent() with calls to
 *              addFormField()
 * 30-Jun-2005: ekoperda added an ErrorSupplier implementation and graceful
 *              handling for ResourceNotFoundExceptions thrown by core
 * 27-Jul-2005: midurbin updated doBeforeBody() to reflect name and spec
 *              changes
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
 *              HtmlPage
 * 15-Aug-2005: midurbin added getSearchParams()
 * 13-Jun-2006: jobollin added type parameters and reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.PaginationContext;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * <p>
 * This tag implements {@code SearchResultsContext} and
 * {@code PaginationContext} in a way that would allow for paging through search
 * results.
 * </p><p>
 * The searchId of a search that has been performed is supplied through the
 * request parameter with the name provided to this tag as the
 * 'searchIdParamName' property and paging information is supplied though the
 * request parameters with the names provided by the 'pageSizeParamName' and
 * 'pageNumberParamName' properties. During the {@code FETCHING_PHASE} this tag
 * fetches the {@code SampleInfo} object for the search results that fall within
 * the range provided by this {@code PaginationContext} are stored. It is
 * expected that nested tags will only request that exact range of search
 * results. If the specified search id is not available from core, as might be
 * the case if the search expired, this tag sets an error flag. Callers are
 * encouraged to detect this error flag and notify users of the condition.
 * </p><p>
 * The current implementation is meant to completely handling paging as well as
 * managing the search results and the method {@code getSearchResults()} only
 * allows parameters consistent with the pageing values returned by the
 * {@code PaginationContext} methods.
 * </p>
 */
public class SearchResultsPage extends RecipnetPage implements
        PaginationContext, SearchResultsContext {
    
    /**
     * ErrorSupplier error flags for use in cases where throwing an exception
     * would be too exteme.
     */
    public static final int UNKNOWN_SEARCH_ID
            = HtmlPage.getHighestErrorFlag() << 1;

    /** Allows subclasses to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return UNKNOWN_SEARCH_ID;
    }

    /**
     * A possible value for various int member variables that indicates that the
     * value has not been usefully set yet.
     */
    public static final int UNDEFINED = -1;

    /**
     * The id of the search whose results are represented by this
     * {@code SearchResultsContext} implementation. Before the value is parsed
     * or if an exception is thrown while parsing this value may be set to
     * {@code UNDEFINEED}.
     */
    private int parsedSearchId;

    /**
     * The page number that will be provided by this {@code PaginationContext}
     * implementation. Parsed from the requrest parameters by
     * {@code doBeforePageBody()}.
     */
    private int parsedPageNumber;

    /**
     * The page size that will be provided by this {@code PaginationContext}
     * implementation. Parsed from the requrest parameters by
     * {@code doBeforePageBody()}.
     */
    private int parsedPageSize;

    /**
     * The total number of elements that will be provided by this
     * {@code PaginationContext} implementation. This value is determined during
     * the {@code FETCHING_PHASE} by {@code doBeforePageBody()}.
     */
    private int fetchedElementCount;

    /**
     * A {@code List} of {@code SampleInfo} objects representing the
     * search results that fall within the range indicated by this
     * {@code PaginationContext}. It is expected that a nested
     * {@code SearchResultsContext}-recognizing tags will request only those
     * search results that fall into the paging scheme laid out by this tag's
     * {@code PaginationContext} implementation. This {@code Collection} is
     * generated during the {@code FETCHING_PHASE} and is returned by
     * {@code getSearchResults()} if the parameters match.
     */
    private List<SampleInfo> cachedSearchResults;

    /**
     * An optional property that defaults to "searchId" and indicates the name
     * of the request parameter that contains the search Id. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String searchIdParamName;

    /**
     * An optional property that defaults to "page" and indicates the name of
     * the request parameter that contains the page number. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String pageNumberParamName;

    /**
     * An optional property that defaults to "pageSize" and indicates the name
     * of the request parameter that contains the page size. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String pageSizeParamName;

    /**
     * The {@code SearchParams} object that was used to perform the search whose
     * results are provided by this page. This variable caches a reference to
     * the {@code SearchParams} fetched during the {@code FETCHING_PHASE}.
     */
    private SearchParams searchParams;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.parsedSearchId = UNDEFINED;
        this.parsedPageNumber = UNDEFINED;
        this.parsedPageSize = UNDEFINED;
        this.fetchedElementCount = UNDEFINED;
        this.cachedSearchResults = null;
        this.searchIdParamName = "searchId";
        this.pageNumberParamName = "page";
        this.pageSizeParamName = "pageSize";
        this.searchParams = null;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        search Id
     */
    public void setSearchIdParamName(String name) {
        this.searchIdParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         search Id
     */
    public String getSearchIdParamName() {
        return this.searchIdParamName;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        page number
     */
    public void setPageNumberParamName(String name) {
        this.pageNumberParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         page number
     */
    public String getPageNumberParamName() {
        return this.pageNumberParamName;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        page size
     */
    public void setPageSizeParamName(String name) {
        this.pageSizeParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         page size
     */
    public String getPageSizeParamName() {
        return this.pageSizeParamName;
    }

    /**
     * {@inheritDoc}; this version gets any URL parameters during the
     * {@code REGISTRATION_PHASE} and fetches the search results during the
     * {@code FETCHING_PHASE}, storing the values in various member variables.
     * 
     * @throws JspException wrapping any exceptions thrown by calls to core
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        
        switch (getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                try {
                    /*
                     * we parse URL parameters during this phase because these
                     * parameters exist for both an HTTP GET and POST and should
                     * be accessible as soon as possible
                     */

                    String paramStr = this.pageContext.getRequest().getParameter(
                            this.searchIdParamName);
                    this.parsedSearchId = Integer.parseInt(paramStr);
                    this.addFormField(this.searchIdParamName, paramStr);
                    paramStr = this.pageContext.getRequest().getParameter(
                            this.pageSizeParamName);
                    if (paramStr == null) {
                        // no page size specified, default to 10
                        this.parsedPageSize = 10;
                    } else {
                        this.parsedPageSize = Integer.parseInt(paramStr);
                    }

                    paramStr = this.pageContext.getRequest().getParameter(
                            this.pageNumberParamName);
                    if (paramStr == null) {
                        // no page number specified, default to 1
                        this.parsedPageNumber = 1;
                    } else {
                        this.parsedPageNumber = Integer.parseInt(paramStr);
                    }
                } catch (NumberFormatException nfe) {
                    try {
                        ((HttpServletResponse) super.pageContext.getResponse()
                                ).sendError(HttpServletResponse.SC_BAD_REQUEST);
                        abort();
                        return;
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    }
                }
                break;
            case HtmlPage.PARSING_PHASE:
                // this tag does nothing during the parsing phase
                break;
            case HtmlPage.FETCHING_PHASE:
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());
                int startIndex = this.parsedPageSize
                        * (this.parsedPageNumber - 1);
                
                try {
                    SampleInfo[] fullResults
                            = cc.getSampleManager().getSearchResults(
                                    this.parsedSearchId, startIndex,
                                    this.parsedPageSize);
                    int endIndex = Math.min(fullResults.length, startIndex
                            + this.parsedPageSize);

                    this.fetchedElementCount = fullResults.length;
                    this.cachedSearchResults = new ArrayList<SampleInfo>(
                            Arrays.asList(fullResults).subList(startIndex,
                                    endIndex));
                    this.searchParams = cc.getSampleManager().getSearchParams(
                            this.parsedSearchId);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (ResourceNotFoundException ex) {
                    // Trap this exception and notify the user gracefully
                    setErrorFlag(UNKNOWN_SEARCH_ID);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }

                break;
            case HtmlPage.PROCESSING_PHASE:
                // this tag does nothing during the processing phase
                break;
            case HtmlPage.RENDERING_PHASE:
                // this tag does nothing during the rendering phase
                break;
        }
    }

    /**
     * Implements {@code PagintationContext}.
     */
    public int getPageNumber() {
        return this.parsedPageNumber;
    }

    /**
     * Implements {@code PagintationContext}.
     */
    public int getPageSize() {
        return this.parsedPageSize;
    }

    /**
     * Implements {@code PagintationContext}. This method must not be called
     * before the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public int getElementCount() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.fetchedElementCount;
    }

    /**
     * Implements {@code PaginationContext}. This method must not be called
     * before the {@code FETCHING_PHASE}.
     */
    public int getPageCount() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return (this.fetchedElementCount / this.parsedPageSize)
                + (this.fetchedElementCount % this.parsedPageSize == 0 ? 0 : 1);
    }

    /**
     * Implements {@code SearchResultsContext}. This method must not be called
     * before the {@code FETCHING_PHASE}. In the current implementation this
     * method may only be called with parameters consistent with those returned
     * by this class' {@code PaginationContext} methods. Specifically
     * 'startingIndex' must be equal to
     * {@code parsedPageSize * (parsedPageNumber - 1)} and 'maxResults' must be
     * equal to {@code parsedPageSize}
     * 
     * @return a {@code List} of search results starting with the
     *         'startingIndex' and including the next 'maxResults' results (or
     *         all of the remaining results if there are fewer than
     *         'maxResults')
     * @param startingIndex the index (starting from 1) of the first search
     *        result being requested. For this implementation, this value must
     *        be consistent with the {@code getPageNumber()} and
     *        {@code getPageSize()}.
     * @param maxResults the requested number of search results to be returned,
     *        fewer may be returned if fewer exist starting with the given
     *        starting index
     */
    public List<SampleInfo> getSearchResults(int startingIndex, int maxResults) {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }

        if ((startingIndex - 1 != (this.parsedPageSize
                * (this.parsedPageNumber - 1)))
                || (maxResults != this.parsedPageSize)) {
            throw new IllegalArgumentException();
        }

        return this.cachedSearchResults;
    }

    public SearchParams getSearchParams() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.searchParams;
    }
}
