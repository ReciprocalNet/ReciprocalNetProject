/*
 * Reciprocal Net project
 * 
 * SearchPage.java
 *
 * 19-Aug-2004: cwestnea wrote first draft
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 24-Aug-2004: midurbin clarified comments regarding searchResultsPage and
 *              removed default value
 * 27-Jan-2005: midurbin added onReevaluation()
 * 25-Feb-2005: midurbin made changes throughout to accomodate new SearchParams
 *              architecture
 * 14-Mar-2005: midurbin added search-by-atom support
 * 16-May-2005: midurbin fixed bug #1598 in setSortOrder()
 * 27-Jul-2005: midurbin added doAfterPageBody() and triggerSearch(); updated
 *              methods calling abort() to throw EvaluationAbortedException
 * 11-Aug-2005: midurbin factored some ErrorSupplier implementing code out to
 *              HtmlPage
 * 10-Nov-2005: midurbin fixed bug #1671 by trim()ing String parameters in
 *              doBeforePageBody()
 * 14-Jun-2006: jobollin reformatted the source
 * 09-Jan-2008: ekoperda enhanced 'quickSummarySearchByLab' to be invokable
 *              from portal software
 */

package org.recipnet.site.content.rncontrols;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.search.ActionDateSC;
import org.recipnet.site.shared.search.AtomCountSC;
import org.recipnet.site.shared.search.ChemicalNameSC;
import org.recipnet.site.shared.search.FormulaAtomSC;
import org.recipnet.site.shared.search.IdNumberSC;
import org.recipnet.site.shared.search.KeywordSC;
import org.recipnet.site.shared.search.LabSC;
import org.recipnet.site.shared.search.MaxZSC;
import org.recipnet.site.shared.search.MinZSC;
import org.recipnet.site.shared.search.NonRetractedStatusSC;
import org.recipnet.site.shared.search.RequireAuthoritativeSC;
import org.recipnet.site.shared.search.SampleDataRangeSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintGroup;
import org.recipnet.site.shared.search.StatusSC;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * This page recognizes several special request parameters:
 * <dl>
 *   <dt>repeatSearchId</dt>
 *   <dd>
 *     requests repetition of a previous search with a newly authenticated
 *     user
 *   </dd>
 *
 *   <dt>refineSearchId</dt>
 *   <dd>allows a user to refine a previously executed search</dd>
 *
 *   <dt>quickSearch</dt>
 *   <dd>requests a search by local lab id (only)</dd>
 *
 *   <dt>quickSearchByLab</dt>
 *   <dd>requests a search by the lab id (only)</dd>
 *
 *   <dt>quickSummarySearchByLab or advancedSearchByLab</dt>
 *   <dd>
 *     requests a search by the lab id, but is more customizable, since it also
 *     supports several add-on request parameters.  The two mode names are
 *     equivalent; it is sufficient to specify only one of them on the query
 *     line.
 *   </dd>
 *
 *   <dt>daysToInclude</dt>
 *   <dd>
 *     An optional companion to the quickSummarySearchByLab mode.  If present,
 *     this argument specifies that the search should return only samples that
 *     have been modified during within the specified number of days (prior to
 *     the current system date)
 *   </dd>
 *
 *   <dt>restrictToStatus</dt>
 *   <dd>
 *     An optional companion to the quickSummarySearchByLab mode.  If present,
 *     this argument specifies that the search should return only samples
 *     currently having the specified status code
 *   </dd>
 *
 *   <dt>restrictToNonRetracted</dt>
 *   <dd>
 *     An optional companion to the quickSummarySearchByLab mode.  If present,
 *     the value must be "1", which specifies that the search should return
 *     only samples that do not have a retracted status code.
 *   </dd>
 *
 *   <dt>chemName</dt>
 *   <dd>
 *     An optional companion to the quickSummarySearchByLab mode.  If present,
 *     this argument specifies that the search should return only samples
 *     that have a "name" matching the specified name.
 *   </dd>
 *
 *   <dt>browse</dt>
 *   <dd>requests a search that returns all the records</dd>
 * </dl>
 *
 * <p>
 * The location of the results page relative to the servlet context path, must
 * be provided. This page is invoked when a search is performed and is sent two
 * parameters: 'searchId', which is given by {@code SampleManager} when the
 * search is stored, and 'pageSize' which gotten from the request object using
 * {@code pageSizeParam.}
 * </p><p>
 * The number of samples to return per page is not specified in a
 * {@code SearchParams} object, but rather it is passed on to the search
 * results page for processing there. This value is retrieved from the optional
 * request parameter, specified in {@code pageSizeParam} and defaults to 10. 
 * The value is retrieved during {@code doSearch()} and is then forwarded on as
 * a request line parameter.
 * </p><p>
 * This class offers various methods that indirectly expose the
 * {@code SearchParams} object.
 * <ul>
 * <li>{@link #getSearchConstraintGroup()} exposes (read only) all of the
 * {@code SearchConstraint} objects added to the main AND group by previous
 * full evaluations of a {@code SearchPage}. This allows previously executed
 * searches to be used to populate search fields nested within this page.</li>
 * <li>{@link #getSortOrder()} exposes the sort order of a previous search or
 * the default order</li>
 * <li>{@link #addSearchConstraint(SearchConstraint)} allows
 * {@code SearchConstraint} objects to be added to the empty
 * {@code SearchParams} during the {@code PROCESSING_PHASE}. Even in the even
 * that a previously executed search was used to pre-populated nested fields,
 * every {@code SearchConstraint} that is to apply to the search must be passed
 * as an argument in a call to this method.</li>
 * <li>{@link #setRange(Double, Double, int)} allows the two-part addition of
 * {@code SampleDataRangeSC} objects. This means that two separate controls may
 * be used for the center and the margin of error for these
 * {@code SearchConstraint}s.</li>
 * <li>{@link #addKeyword(String)} and {@link #setKeywordOperator(int)} allow
 * any number of keywords to be supplied independant of their grouping
 * operator.</li>
 * <li> {@link #setSortOrder(int)} allows the sort order to be set. </li>
 * <li>{@link #addAtomCountSearch(String, String, Integer, Double)} allows the
 * multi-part addition of {@code AtomCountSC} children of a
 * {@code FormulaAtomSC} to be added.</li>
 * <li>{@link #setConsiderEFAtoms(boolean)},
 * {@link #setConsiderEFDAtoms(boolean)},
 * {@link #setConsiderEFLSAtoms(boolean)}, and
 * {@link #setConsiderEFSIAtoms(boolean)} allow the domain for any atom
 * searches to be set.</li>
 * </ul>
 * </p>
 */
public class SearchPage extends RecipnetPage {
    /** Error supplied if the min z field is greater than the max z field */
    public static final int MINZ_GREATER_THAN_MAXZ
            = RecipnetPage.getHighestErrorFlag() << 1;

    /**
     * Error supplied if an atom count and operator are given when no atom type
     * has been entered.
     */
    public static final int ATOM_TYPE_MISSING
            = RecipnetPage.getHighestErrorFlag() << 2;

    /**
     * Error supplied if an atom type and operator are given when no atom count
     * has been entered.
     */
    public static final int ATOM_COUNT_MISSING
            = RecipnetPage.getHighestErrorFlag() << 3;

    /**
     * Error supplied if an atom type and count are given when no operator has
     * been entered.
     */
    public static final int ATOM_OPERATOR_MISSING
            = RecipnetPage.getHighestErrorFlag() << 4;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return ATOM_OPERATOR_MISSING;
    }

    /**
     * The {@code SearchParams} object that corresponds to the optional
     * 'repeatSearchId' parameter. This is fetched during the
     * {@code FetchingPhase} and is indirectly exposed to nested tags through
     * the methods: {@code getSearchConstraintGroup()} and
     * {@code getSortOrder()}.
     */
    private SearchParams oldSearchParams;

    /**
     * A new {@code SearchParams} object that is initialized during the
     * {@code FETCHING_PHASE} by a call to
     * {@code SampleManager.getEmptySearchParams()} and may be indirectly
     * populated by nested tags during the {@code PROCESSING_PHASE} through
     * calls to {@code addSearchConstraint()}, {@code addKeyword()},
     * {@code setKeywordOperator()}, {@code setRange()} and
     * {@code setSortOrder()}.
     */
    private SearchParams newSearchParams;

    /**
     * A {@code Collection} of {@code SearchConstraint} objects that is
     * populated through calls to {@code addSearchConstraint()} and used by
     * {@code doSearch()} to populate the {@code newSearchParams}. This
     * intermediate cache of {@code SearchConstraint} objects improves
     * efficiency.
     */
    private Collection<SearchConstraint> searchConstraints;

    /**
     * A {@code Collection} of {@code String} objects that are the keywords
     * that will comprise a single {@code KeywordSC}. This object is immutable
     * once created, so keywords are cached until {@code doSearch()} is called
     * at which point if any exist a new {@code KeywordSC} is added to the
     * {@code searchConstraints} and in turn to the {@code SearchParams}
     * object.
     */
    private Collection<String> keywords;

    /**
     * Another piece of information needed to construct a {@code KeywordSC}
     * object. This is set by calls to {@code setKeywordOperator()} and used by
     * {@code doSearch()} when the {@code KeywordSC} is created.
     */
    private int keywordOperator;

    /**
     * Used by nested {@code SearchParamsField} objects that represent
     * {@code KEYWORD}s. This index is initialized to zero and returned and
     * incremented by calls to {@code getKeywordIndex()}. If used
     * appropriately, nested tags may use this to assign the numerous keywords
     * on a {@code oldSearchParams} to individual controls when being
     * populated.
     */
    private int keywordIndex;

    /**
     * Used by {@code getAtomCountIndex()} during the {@code FETCHING_PHASE} to
     * assign indices to keys. For each unique key passed to the method, the
     * next sequential index must be returned. This a list of provided keys,
     * whose corresponding index values represent the index to be returned by
     * the method.
     */
    private List<String> atomCountList;

    /**
     * A mapping of arbitrary keys ({@code String} objects) to
     * {@code SearchPage.AtomSearchRecord} objects. The records are populated
     * and inserted into the map by calls to {@code addAtomCountSearch()}. If a
     * record is fully populated when {@code doSearch()} is invoked, it will be
     * used to generate an {@code AtomCountSC} that will be the child of a
     * {@code FormulaAtomSC}. If the record has had an 'atomSymbol' or an
     * 'atomCount' entered but is not fully complete an error flag will be set.
     */
    private Map<String, AtomSearchRecord> atomSearchMap;

    /**
     * Used with all of the {@code AtomSearchRecord}s in the
     * {@code atomSearchMap} to generate {@code AtomCountSC}s.
     */
    private boolean considerEFAtoms;

    private boolean considerEFDAtoms;

    private boolean considerEFLSAtoms;

    private boolean considerEFSIAtoms;

    /**
     * A mapping of {@code SampleDataInfo} field codes ({@code Integer}s) to
     * {@code SearchPage.RangeRecord} objects for the {@code SampleDataRangeSC}
     * object that will represent some search criteria on the
     * {@code SampleDataInfo} field. This {@code Map} is populated by calls to
     * {@code setRange()}, and once a {@code RangeRecord} has both its fields
     * set, a new {@code SampleDataRangeSC} is added to the
     * {@code searchConstraints} {@code Collection} for inclusion in the
     * {@code SearchParams}.
     */
    private Map<Integer, RangeRecord> rangeMap;

    /**
     * Private variables to facillitate validation. When a {@code MinZSC} or
     * {@code MaxZSC} is passed to {@code addSearchConstraint()}, these values
     * are set or compared. They default to -1.
     */
    private int minz;

    private int maxz;

    /**
     * Required attribute; specifies the relative path of the page that will
     * display the results of this search to the user. This path must be of the
     * form "/page.jsp" and is relative to the servlet context.
     */
    private String searchResultsPage;

    /**
     * Optional attribute, defaults to "pageSize"; specifies the request
     * parameter to get the page size from. Retrieved from the request in
     * {@code doSearch()}.
     */
    private String pageSizeParam;

    /**
     * An internal boolean that indicates whether a search has been triggered 
     * or not. If a search execution has been triggered, the method
     * {@code doSearch()} will be invoked at the end of the
     * {@code PROCESSING_PHASE}.
     */
    private boolean triggerSearch;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.searchResultsPage = null;
        this.pageSizeParam = "pageSize";
        this.oldSearchParams = null;
        this.newSearchParams = null;
        this.searchConstraints = new ArrayList<SearchConstraint>();
        this.keywords = new ArrayList<String>();
        this.keywordOperator = SearchConstraintGroup.OR;
        this.keywordIndex = 0;
        this.atomSearchMap = new HashMap<String, AtomSearchRecord>();
        this.atomCountList = new ArrayList<String>();
        this.considerEFAtoms = false;
        this.considerEFDAtoms = false;
        this.considerEFLSAtoms = false;
        this.considerEFSIAtoms = false;
        this.rangeMap = new HashMap<Integer, RangeRecord>();
        this.minz = -1;
        this.maxz = -1;
        this.triggerSearch = false;
    }

    /** @param searchResultsPage the page to redirect to */
    public void setSearchResultsPage(String searchResultsPage) {
        this.searchResultsPage = searchResultsPage;
    }

    /** @return the page to redirect to */
    public String getSearchResultsPage() {
        return this.searchResultsPage;
    }

    /** @param pageSizeParam the parameter to get the page size from */
    public void setPageSizeParam(String pageSizeParam) {
        this.pageSizeParam = pageSizeParam;
    }

    /** @return the parameter to get the page size from */
    public String getPageSizeParam() {
        return this.pageSizeParam;
    }

    /**
     * Gets the {@code SearchConstraintGroup} that contains all of the
     * {@code SearchConstraint} objects from the previous evaluation of
     * {@code SearchPage} that resulted in the search with the id provided by
     * the 'repeatSearchId' parameter. This may be invoked by nested tags
     * during the {@code FETCHING_PHASE} to expose the various
     * {@code SearchConstraint} objects. There is no guarantee that any
     * particular constraints will be present or absent or in any order, so the
     * extraction logic of nested tags must be sufficiently robust. See the
     * class level documentation for expected behavior of this class.
     * 
     * @return a {@code SearchConstraintGroup} if one with the AND operator
     *         happened to be the head of the {@code SearchParams} object with
     *         the searchId provided as the 'repeatSearchId' parameter or null
     *         otherwise
     */
    public SearchConstraintGroup getSearchConstraintGroup() {
        if (this.oldSearchParams != null) {
            SearchConstraint sc = oldSearchParams.getHead();
            
            if ((sc != null)
                    && (sc.getClass() == SearchConstraintGroup.class)
                    && (((SearchConstraintGroup) sc).getOperator()
                            == SearchConstraintGroup.AND)) {
                return (SearchConstraintGroup) sc;
            }
        }
        
        return null;
    }

    /**
     * Adds (or retains) a {@code SearchConstraint}. This method must be called
     * for every {@code SearchConstraint} that is to be present on the
     * {@code SearchParams}, even those that existed on a search that was
     * provided by a {@code repeatSearchId} parameter.
     * 
     * @param sc a {@code SearchConstraint} to refine this search. Because
     *        {@code SearchConstraint} objects are immutable, this may be
     *        reused from a previous search.
     * @throws IllegalStateException if called during any phase other than the
     *         {@code PROCESSING_PHASE}.
     */
    public void addSearchConstraint(SearchConstraint sc) {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        if (sc.getClass() == MinZSC.class) {
            this.minz = ((MinZSC) sc).getValue().intValue();
        } else if (sc.getClass() == MaxZSC.class) {
            this.maxz = ((MaxZSC) sc).getValue().intValue();
        }
        // add code to queue search constraint addition
        this.searchConstraints.add(sc);
    }

    /**
     * Gets the {@code SearchOrder} for the search.
     */
    public int getSortOrder() {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        /*
         * at the start of the fetching phase the sort order for the new
         * SearchParams was initialized to the value from the old one if it
         * existed
         */
        return this.newSearchParams.getSortOrder();
    }

    /**
     * Sets the {@code SearchOrder} for the search.
     * 
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public void setSortOrder(int order) {
        if ((getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        this.newSearchParams.setSortOrder(order);
    }

    /**
     * Stores some of the data needed to construct a {@code SampleDataRangeSC}.
     * When sufficient data has been included, a new {@code SampleDataRangeSC}
     * is added to the {@code SearchParams} object. When excess or unexpected
     * data is provided, this method throws an exception. When insufficient
     * data is provided, it may be ignored.
     * 
     * @param center a {@code Double} representing the center of the range of
     *        acceptable values for the given 'sampleDataInfoFiledCode' or null
     *        when this call isn't meant to supply this information. When this
     *        value is null, another call to this method is required for the
     *        {@code SampleDataRangeSC} to be created.
     * @param margin a {@code Double} representing the percent variance allowed
     *        for acceptable values for the given 'sampleDataInfoFiledCode' or
     *        null when this call isn't meant to supply this information. When
     *        this value is null, another call to this method is required for
     *        the {@code SampleDataRangeSC} to be created.
     * @param sampleDataInfoFieldCode one of the field codes defined on
     *        {@code SampleDataInfo} to represent the field whose value must
     *        fall into the given range.
     * @throws IllegalArgumentException if a value is provided when the same
     *         value for the same field code has already been provided
     * @throws IllegalStateException if this method is called during any phase
     *         other than the {@code PROCESSING_PHASE}
     */
    public void setRange(Double center, Double margin,
            int sampleDataInfoFieldCode) {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        RangeRecord rrec = this.rangeMap.get(sampleDataInfoFieldCode);
        
        if (rrec == null) {
            rrec = new RangeRecord();
            this.rangeMap.put(sampleDataInfoFieldCode, rrec);
        }
        if (center != null) {
            if (rrec.center != null) {
                throw new IllegalArgumentException();
            }
            rrec.center = center;
        }
        if (margin != null) {
            if (rrec.margin != null) {
                throw new IllegalArgumentException();
            }
            rrec.margin = margin;
        }
        if ((rrec.margin != null) && (rrec.center != null)) {
            this.searchConstraints.add(new SampleDataRangeSC(
                    sampleDataInfoFieldCode, rrec.center.doubleValue(),
                    rrec.margin.doubleValue()));
        }
    }

    /**
     * If invoked exactly once during the {@code FETCHING_PHASE} by each nested
     * field that represents a single keyword, this method returns the index of
     * the keywords on the existing search that should correspond to the 
     * calling field.
     * 
     * @return an integer index, starting at zero, that increments after each
     *         invocation
     * @throws IllegalStateException if called during any phase other than the
     *         {@code FETCHING_PHASE}.
     */
    public int getKeywordIndex() {
        if (getPhase() != HtmlPage.FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        return this.keywordIndex++;
    }

    /**
     * This method may be called any number of times before {@code doSearch()}
     * is invoked when all the keywords supplied by the calls will be used to
     * construct a {@code KeywordSC} that will be used in the new
     * {@code SearchParams} object.
     * 
     * @param keyword a keyword {@code String}
     * @throws IllegalStateException if this method is called during any phase
     *         other than the {@code PROCESSING_PHASE}
     */
    public void addKeyword(String keyword) {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        this.keywords.add(keyword);
    }

    /**
     * This method may be called to set the operator for the {@code KeywordSC}
     * that will be made if any keywords are given by calls to
     * {@code addKeyword()}. This must be one of the operators defined on
     * {@code keywordSC}.
     * 
     * @param operator an operator defined on {@code KeywordSC}
     * @throws IllegalStateException if this method is called during any phase
     *         other than the {@code PROCESSING_PHASE}
     */
    public void setKeywordOperator(int operator) {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        this.keywordOperator = operator;
    }

    /**
     * @param consider will be used as the 'considerEF' field in all
     *        {@code AtomCountSC}
     */
    public void setConsiderEFAtoms(boolean consider) {
        this.considerEFAtoms = consider;
    }

    /**
     * @param consider will be used as the 'considerEFD' field in all
     *        {@code AtomCountSC}
     */
    public void setConsiderEFDAtoms(boolean consider) {
        this.considerEFDAtoms = consider;
    }

    /**
     * @param consider will be used as the 'considerEFLS' field in all
     *        {@code AtomCountSC}
     */
    public void setConsiderEFLSAtoms(boolean consider) {
        this.considerEFLSAtoms = consider;
    }

    /**
     * @param consider will be used as the 'considerEFSI' field in all
     *        {@code AtomCountSC}
     */
    public void setConsiderEFSIAtoms(boolean consider) {
        this.considerEFSIAtoms = consider;
    }

    /**
     * Gets the index for the {@code AtomCountSC} that should be associated
     * with the provided key. Every call with the same key during the same page
     * evaluation will return the same index, and indexes will be provided,
     * starting at zero and increasing for each new key.
     * 
     * @return an integer index, starting at zero, that increments for each key
     * @throws IllegalStateException if called during any phase other than the
     *         {@code FETCHING_PHASE}.
     */
    public int getAtomCountIndex(String key) {
        if (getPhase() != HtmlPage.FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        int index = this.atomCountList.indexOf(key);
        
        if (index < 0) {
            this.atomCountList.add(key);
            index = this.atomCountList.size() - 1;
        }
        
        return index;
    }

    /**
     * Supplies some information about the atom count search with the arbitrary
     * index. This method populates the {@code atomSearchMap} that is later
     * used to generate a {@code FormulaAtomSC} and its children.
     * 
     * @param key a value used to group multiple calls. Each call with the same
     *        'key' will operate on the same record in the
     *        {@code atomSearchMap}.
     * @param atomSymbol the two digit elemental symbol or null if this call is
     *        not meant to provide this piece of data.
     * @param numericOperatorCode an operator code as defined by
     *        {@code NumberComparisonSC} or null if this call is not meant to
     *        provide this piece of data.
     * @param atomCount a double representing the number of atoms or null if
     *        this call is not meant to provide this piece of data.
     * @throws IllegalStateException if called on any phase other than the
     *         {@code PROCESSING_PHASE}
     * @throws IllegalArgumentException if a piece of data is being supplied
     *         for
     *         and index that has already been supplied a value for the given
     *         field (ie, two calls with the same index where the 'atomSymbol'
     *         is non-null).
     */
    public void addAtomCountSearch(String key, String atomSymbol,
            Integer numericOperatorCode, Double atomCount) {
        if (getPhase() != HtmlPage.PROCESSING_PHASE) {
            throw new IllegalStateException();
        }
        AtomSearchRecord rec = this.atomSearchMap.get(key);
        if (rec == null) {
            rec = new AtomSearchRecord();
            this.atomSearchMap.put(key, rec);
        }
        if (atomSymbol != null) {
            if (rec.atomSymbol != null) {
                throw new IllegalArgumentException();
            }
            rec.atomSymbol = atomSymbol;
        }
        if (numericOperatorCode != null) {
            if (rec.numericOperatorCode != null) {
                throw new IllegalArgumentException();
            }
            rec.numericOperatorCode = numericOperatorCode;
        }
        if (atomCount != null) {
            if (rec.atomCount != null) {
                throw new IllegalArgumentException();
            }
            rec.atomCount = atomCount;
        }
    }

    /**
     * Overrides {@code HtmlPage}; retrieves {@code CoreConnector} and
     * {@code SampleManager} and handles special modes.
     * 
     * @throws JspException if there is an error getting search params
     */
    @Override
    public void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();

        if (getPhase() == HtmlPage.FETCHING_PHASE) {
            CoreConnector coreConnector
                    = CoreConnector.extract(pageContext.getServletContext());
            SampleManagerRemote sampleManager;

            try {
                sampleManager = coreConnector.getSampleManager();
                this.newSearchParams = sampleManager.getEmptySearchParams();
                if (this.oldSearchParams != null) {
                    this.newSearchParams.setSortOrder(
                            this.oldSearchParams.getSortOrder());
                }
            } catch (RemoteException ex) {
                coreConnector.reportRemoteException(ex);
                throw new JspException(ex);
            }

            // Handle a repeat search
            ServletRequest request = this.pageContext.getRequest();
            String rsi = request.getParameter("repeatSearchId");

            if (rsi != null) {
                int repeatSearchId = Integer.parseInt(rsi);

                try {
                    this.oldSearchParams
                            = sampleManager.getSearchParams(repeatSearchId);
                    this.newSearchParams = this.oldSearchParams;
                    doSearch();
                    return;
                } catch (RemoteException ex) {
                    coreConnector.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
            }

            // Allow a search to be refined
            rsi = request.getParameter("refineSearchId");
            if (rsi != null) {
                int refineSearchId = Integer.parseInt(rsi);

                try {
                    this.oldSearchParams
                            = sampleManager.getSearchParams(refineSearchId);
                    this.newSearchParams.setSortOrder(
                            this.oldSearchParams.getSortOrder());
                } catch (RemoteException ex) {
                    coreConnector.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
            }

            // Handle a quick search by local lab id
            String param = request.getParameter("quickSearch");
            if ((param != null) && !param.equals("sample # (local search)")) {
                this.searchConstraints.add(new IdNumberSC(param.trim()));
                this.searchConstraints.add(new RequireAuthoritativeSC());
                doSearch();
                return;
            }

            // Handle a quick search by a lab id
            param = request.getParameter("quickSearchByLab");
            if (param != null) {
                this.searchConstraints.add(new LabSC(Integer.parseInt(param)));
                this.setSortOrder(SearchParams.SORTBY_LOCALLABID);
                doSearch();
                return;
            }

            // Handle a more customizable search by lab
            param = request.getParameter("quickSummarySearchByLab");
	    if (param == null) {
		param = request.getParameter("advancedSearchByLab");
	    }
            if (param != null) {
                this.searchConstraints.add(new LabSC(Integer.parseInt(param)));

		// Handle the optional argument 'daysToInclude'
                String daysToInclude = request.getParameter("daysToInclude");
                if ((daysToInclude != null) && !daysToInclude.equals("")) {
                    Calendar cal = Calendar.getInstance();

                    cal.add(Calendar.DAY_OF_YEAR,
                            -Integer.parseInt(daysToInclude));
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    this.searchConstraints.add(new ActionDateSC(cal.getTime(),
                            ActionDateSC.REQUIRE_ACTION_AFTER));
                }

		// Handle the optional argument 'restrictToStatus'
                String restrictToStatus
                        = request.getParameter("restrictToStatus");
                if (restrictToStatus != null 
                        && !restrictToStatus.equals("")) {
                    this.searchConstraints.add(new StatusSC(
                            Integer.parseInt(restrictToStatus)));
                }
		
		// Handle the optional argument 'restrictToNonRetracted'
		if (request.getParameter("restrictToNonRetracted") != null) {
		    this.searchConstraints.add(new NonRetractedStatusSC());
		}

		// Handle the optional argument 'chemName'
		String chemName = request.getParameter("chemName");
		if (chemName != null) {
		    this.searchConstraints.add(new ChemicalNameSC(chemName));
		}

                setSortOrder(SearchParams.SORTBY_LOCALLABID);
                doSearch();
                return;
            }

            // Handle a browse request
            if (request.getParameter("browse") != null) {
                setSortOrder(SearchParams.SORTBY_LOCALLABID);
                doSearch();
                return;
            }
        }
    }

    /**
     * {@inheritDoc}; this version determines whether the search has been
     * triggered and at the end of the {@code PROCESSING_PHASE} acts
     * accordingly.
     */
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        switch (getPhase()) {
            case HtmlPage.PROCESSING_PHASE:
                if (this.triggerSearch) {
                    this.doSearch();
                }
                break;
        }
        super.doAfterPageBody();
    }

    /**
     * Triggers the execution of the search defined by the nested
     * {@code SearchParamField} tags by the currently logged-in user at the end
     * of this tag's {@code PROCESSING_PHASE}.
     * 
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because it is too late to perform the
     *         search
     */
    public void triggerSearch() {
        if (getPhase() == HtmlPage.RENDERING_PHASE) {
            throw new IllegalStateException();
        }
        this.triggerSearch = true;
    }

    /**
     * Helper method to perform a search. Stores the search parameters in
     * {@code SampleManager} and redirects to {@code searchResultsPage}. Two
     * parameters are passed along as well, the search id and the page size.
     * 
     * @throws JspException if there is an error redirecting or storing the
     *         search
     */
    protected void doSearch() throws JspException, EvaluationAbortedException {
        CoreConnector coreConnector
                = CoreConnector.extract(this.pageContext.getServletContext());

        try {
            if ((this.minz != -1) && (this.maxz != -1) 
                    && (this.minz > this.maxz)) {
                this.setErrorFlag(MINZ_GREATER_THAN_MAXZ);
                return;
            }

            /*
             * create any SearchConstraints that required an arbitrary amount
	     * of fields
             */
            this.searchConstraints.add(new KeywordSC(this.keywordOperator,
                    this.keywords));

            // creates the children for the FormulaAtomSC
            Collection<AtomCountSC> atomCountSCs 
                    = new ArrayList<AtomCountSC>();
            for (Map.Entry<String, AtomSearchRecord> entry
                    : this.atomSearchMap.entrySet()) {
                AtomSearchRecord rec = entry.getValue();

                if (rec.atomSymbol == null || rec.numericOperatorCode == null
                        || rec.atomCount == null) {
                    // at least one field has been left incomplete
                    if ((rec.atomSymbol == null) && (rec.atomCount == null)) {
                        /*
                         * Either the operator or no fields have been supplied.
                         * As a convenience to nested controls these cases are
                         * ignored.
                         */
                    } else {
                        if (rec.atomSymbol == null) {
                            setErrorFlag(ATOM_TYPE_MISSING);
                        }
                        if (rec.atomCount == null) {
                            setErrorFlag(ATOM_COUNT_MISSING);
                        }
                        if (rec.numericOperatorCode == null) {
                            setErrorFlag(ATOM_OPERATOR_MISSING);
                        }
                    }
                } else {
                    atomCountSCs.add(new AtomCountSC(rec.atomSymbol,
                            rec.atomCount.doubleValue(),
                            rec.numericOperatorCode.intValue(),
                            this.considerEFAtoms, this.considerEFDAtoms,
                            this.considerEFLSAtoms, this.considerEFSIAtoms));
                }
            }

            if (getErrorCode() != ErrorSupplier.NO_ERROR_REPORTED) {
                return;
            }

            /*
             * create the FormulaAtomSC if needed and add it to the
             * searchConstraints that will be included in the search
             */
            if (atomCountSCs.size() > 0) {
                this.searchConstraints.add(new FormulaAtomSC(atomCountSCs));
            }

            // add all keywords to the new SearchParams
            this.newSearchParams.addToHeadWithAnd(this.searchConstraints);

            /*
             * ensure that the search only includes samples that the current
             * user may view
             */
            if (this.oldSearchParams == this.newSearchParams) {
                AuthorizationCheckerBL.reblessSearchParams(
                        this.newSearchParams, this.getUserInfo());
            } else {
                AuthorizationCheckerBL.blessSearchParams(this.newSearchParams,
                        this.getUserInfo());
            }

            SampleManagerRemote sampleManager
                    = coreConnector.getSampleManager();
            int searchId
                    = sampleManager.storeSearchParams(this.newSearchParams);
            String pageSize = this.pageContext.getRequest().getParameter(
                    this.pageSizeParam);

            if (pageSize == null) {
                pageSize = "10";
            }
            ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                    getContextPath()
                    + this.searchResultsPage
                    + "?searchId="
                    + searchId
                    + "&pageSize=" + pageSize);
            abort();
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * {@inheritDoc}; this version unsets 'triggerSearch' then delegates back
     * to the superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerSearch = false;
        super.onReevaluation();
    }

    /**
     * A class simply to contain information needed to create a
     * {@code SampleDataRangeSC}.
     */
    private static class RangeRecord {
        public Double center;

        public Double margin;
    }

    /**
     * A class simply to contain most of the information needed to create an
     * {@code AtomCountSC}.
     */
    private static class AtomSearchRecord {
        public String atomSymbol;

        public Integer numericOperatorCode;

        public Double atomCount;
    }
}
