<%--
  - Reciprocal Net project
  - searchresults.jsp
  -
  - 01-Aug-2002: rlauer wrote first draft
  - 02-Aug-2002: ekoperda touched up first draft
  - 08-Aug-2002: ekoperda fixed bugs #296 and #298 (UI refinements)
  - 21-Aug-2002: eisiorho changed retrieveSearchResults(), retrieves only
  -              partial list of SampleInfos.
  - 27-Aug-2002: ekoperda fixed compilation bug by importing HtmlHelper again
  - 13-Sep-2002: jobollin fixed capitalization of page title (task 332) and
  -              removed extra carriage returns
  - 24-Sep-2002: eisiorho added better pagination, task #444
  - 17-Oct-2002: eisiorho added 'withdrawn' feature to results display
  - 31-Oct-2002: eisiorho changed option menu, from 'All' to 500 samples
  -              maximum shown per page
  - 01-Nov-2002: ekoperda fixed bug #560 that was caused by an incorrect
  -              calling order to PageHelper
  - 12-Nov-2002: eisiorho fixed bug #598
  - 18-Nov-2002: ekoperda fixed bug #602
  - 16-Dec-2002: ekoperda fixed bug #659 by rearranging the order in which
  -              form fields are processed during a GET/POST
  - 18-Dec-2002: eisiorho updated the display to reflect the
  -              isMoreRecentThanSearch flag in SampleInfo
  - 27-Feb-2003: nsanghvi added code to provide link to 'Login' if a search 
  -              returns 0 results for an unauthenticated users.
  - 26-Mar-2003: dfeng exposed sample status, empirical formula, structural
  -              formula, provider name, common names and trade names in UI 
  - 02-Apr-2003: dfeng fixed bug #850
  - 02-Apr-2003: midurbin fixed bug 846
  - 30-May-2003: midurbin added sampleHistoryId to links to showsample.jsp
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 22-Aug-2003: midurbin fixed bug #1024 (now, by edict, all search results
  -              containing only one sample redirect to showsample.jsp, never
  -              to sample.jsp)
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 29-Mar-2004: ajooloor changed redirecting of authenticated users when the
                 search contains only one result.
  - 14-Dec-2004: eisiorho changed texttype references to use new class
  -              SampleTextBL
  - 23-Mar-2005: midurbin altered scriptlets to use new PREFERRED_NAME
  -              determination methods
  - 12-Apr-2005: midurbin rewrote using custom tags 
  - 21-Jun-2005: midurbin replaced funny use of ErrorMessageElement with a
  -              ParityChecker
  - 30-Jun-2005: ekoperda added handling of expired search id's
  - 15-Aug-2005: midurbin added support for indicating matched formulae
  - 18-Aug-2005: midurbin fixed bug #1647
  - 23-Sep-2005: midurbin added ShowsampleLink tags, updated
  -              redirectToSingleSearchResults
  - 06-Oct-2005: midurbin fixed bug #1648
--%>
<%@ page import="org.recipnet.common.controls.PaginationField" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleTextIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.SearchResultsContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.SearchResultsIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.SearchResultsPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>

<rn:searchResultsPage title="Search Results">
  <ctl:errorMessage errorFilter="<%=
      SearchResultsPage.UNKNOWN_SEARCH_ID%>">
    <ctl:redirect target="/searchexpired.jsp" />
  </ctl:errorMessage>
  <p>
    Found <font color="red"><b><ctl:paginationField /></b></font> samples.
    <ctl:paginationChecker requirePageCountNoLessThan="1">
      &nbsp;&nbsp;Page <ctl:paginationField fieldCode="<%=
          PaginationField.CURRENT_PAGE_NUMBER%>" /> of
      <ctl:paginationField
          fieldCode="<%=PaginationField.TOTAL_PAGE_COUNT%>" />.
    </ctl:paginationChecker>
  </p>
  <ctl:paginationChecker requireElementCountNoMoreThan="0">
    <rn:authorizationChecker invert="true">
      Additional results may become visible after you
      <ctl:a href="/login.jsp">log in<rn:repeatSearchParam name="origUrl"
          searchIdParamName="searchId"
          searchPagePath="/search.jsp" /></ctl:a>.
    </rn:authorizationChecker>
  </ctl:paginationChecker>
  <rn:redirectToSingleSearchResult basicPageUrl="/showsamplebasic.jsp"
      detailedPageUrl="/showsampledetailed.jsp"
      jumpSitePageUrl="/showsamplejumpsite.jsp" 
      editSamplePageUrl="/lab/sample.jsp" />

  <ctl:pageLink pageOffset="-1" preserveParam="searchId">
    &lt;&lt;&lt; Previous page
  </ctl:pageLink> &nbsp;  &nbsp;  &nbsp;  &nbsp;  &nbsp;

  <ctl:pageLink pageOffset="1" preserveParam="searchId">
    Next page &gt;&gt;&gt;
  </ctl:pageLink>

  <table cellspacing="15" border="0">
    <rn:searchResultsIterator usePaginationContext="true">
      <tr>
        <td align="right" valign="top">
          <rn:searchResultIndex />.
        </td>
        <td align="left" valign="top" width="350">
          <rn:showsampleLink basicPageUrl="/showsamplebasic.jsp"
              detailedPageUrl="/showsampledetailed.jsp"
              jumpSitePageUrl="/showsamplejumpsite.jsp">
            <rn:sampleParam name="sampleId" />
            <rn:sampleHistoryParam name="sampleHistoryId" />
            <rn:labField fieldCode="<%=LabField.SHORT_NAME%>" />
            #<rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                displayValueOnly="true" />
          </rn:showsampleLink>
          <rn:nameAndFormulaIterator id="nameAndFormula"><%--
                    -- prevent spaces in HTML output --
            --%><ctl:parityChecker includeOnlyOnFirst="true">(<%--
            --%></ctl:parityChecker><%--
            --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
            </ctl:parityChecker><%--
            --%><rn:sampleField displayValueOnly="true"
                    formatFieldForSearchResults="true"
                    styleClassForSearchMatch="match" /><%--
            --%><ctl:parityChecker includeOnlyOnLast="true">)<%--
            --%></ctl:parityChecker><%--
          --%></rn:nameAndFormulaIterator>
          [<rn:sampleField fieldCode="<%=SampleInfo.STATUS%>"
                  displayValueOnly="true"/>]
          <rn:sampleNameIterator excludePreferredName="true"
              excludeIUPACNames="true"><%--
                    -- prevent spaces in HTML output --
            --%><ctl:parityChecker includeOnlyOnFirst="true">
              <br />Other names: <%--
            --%></ctl:parityChecker><%--
            --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">;
            </ctl:parityChecker><%--
            --%><rn:sampleField displayValueOnly="true"
                    formatFieldForSearchResults="true"
                    styleClassForSearchMatch="match" /><%--
          --%></rn:sampleNameIterator>
          <rn:sampleChecker includeIfValueIsPresent="<%=
              SampleTextBL.STRUCTURAL_FORMULA%>">
            <br />
            <rn:sampleFieldLabel fieldCode="<%=
                SampleTextBL.STRUCTURAL_FORMULA%>" />:
            <rn:sampleField fieldCode="<%=
                SampleTextBL.STRUCTURAL_FORMULA%>" displayAsLabel="true"
                formatFieldForSearchResults="true"
                styleClassForSearchMatch="match" />
          </rn:sampleChecker>
          <rn:matchedFormulaIterator>
            <br />
            <rn:sampleFieldLabel />:
            <rn:sampleField displayValueOnly="true"
                formatFieldForSearchResults="true"
                styleClassForSearchMatch="match" />
          </rn:matchedFormulaIterator>
          <br />Provider: <rn:providerField />
          <ctl:errorMessage errorFilter="<%=
              SearchResultsIterator.IS_MORE_RECENT_THAN_SEARCH%>">
          <span class="warningMessage">
            <br />
            This sample has been updated recently and might not match the
            search criteria any longer.
          </span>
          </ctl:errorMessage>
        </td>
        <td>
          <rn:authorizationChecker canEditSample="true"
              suppressRenderingOnly="true">
            <rn:link href="/lab/sample.jsp">Edit...</rn:link>
          </rn:authorizationChecker>
        </td>
      </tr>
    </rn:searchResultsIterator>
  </table>

  <ctl:pageLink pageOffset="-1" preserveParam="searchId">
    &lt;&lt;&lt; Previous page&nbsp;&nbsp;
  </ctl:pageLink>
  <ctl:pageLink pageOffset="-3" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:pageLink pageOffset="-2" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:pageLink pageOffset="-1" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:paginationChecker requirePageCountNoLessThan="2">
    <font color="red">
      <ctl:pageLink pageOffset="0" includePageNumber="true" disabled="true"
          preserveParam="searchId" />&nbsp;&nbsp;
    </font>
  </ctl:paginationChecker>
  <ctl:pageLink pageOffset="1" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:pageLink pageOffset="2" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:pageLink pageOffset="3" includePageNumber="true"
      preserveParam="searchId" />&nbsp;&nbsp;
  <ctl:pageLink pageOffset="1" preserveParam="searchId">
    Next page &gt;&gt;&gt;
  </ctl:pageLink>
  <br />
  <br />
  <ctl:paginationChecker requireElementCountNoLessThan="6">
    <ctl:selfForm method="GET" pageForm="false">
      Results per page: <ctl:paginationField
          fieldCode="<%=PaginationField.RESIZE_PAGE_SELECTOR%>" />
      <ctl:hiddenInt id="searchId" initialValueFrom="searchId" />
      <input type="submit" value="Resize" />
    </ctl:selfForm>
  </ctl:paginationChecker>
  <ctl:styleBlock>
      .warningMessage { font-style: italic; color: #4A766E; }
      .match { font-weight: bold; color: #200050; }
  </ctl:styleBlock>
</rn:searchResultsPage>
