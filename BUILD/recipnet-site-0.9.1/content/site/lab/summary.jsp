<%--
   - Reciprocal Net Project
   - @(#)lab/summary.jsp
   - 
   - 05-May-2003: midurbin wrote first draft
   - 22-Jul-2003: ajooloor fixed bug #954
   - 22-Jul-2003: ajooloor added comments and fixed bug #970
   - 21-Aug-2003: ekoperda fixed bug #1022 in listAllSamplesByStatus()
   - 07-Jan-2004: ekoperda changed package references due to source tree
   -              reorganization
   - 13-Apr-2004: midurbin added option to display samples modified for last
   -              one day
   - 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
   - 22-Jun-2005: midurbin rewrote using custom tags
   - 29-Jun-2005: midurbin fixed bug #1623
   - 13-Jul-2005: midurbin added new required parameters to redirectToLogin
   - 09-Jan-2008: ekoperda fixed bug #1881 by matching LinkHtmlElement changes
  --%>
<%@page import="org.recipnet.common.controls.PaginationField" %>
<%@page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@page import="org.recipnet.site.content.rncontrols.SampleHistoryField" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Summary">
  <rn:authorizationChecker canSeeLabSummary="true" invert="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
          AuthorizationReasonMessage.CANNOT_SEE_LAB_SUMMARY%>" />
  </rn:authorizationChecker>
  <rn:authorizationChecker canSeeLabSummary="true">
    <table width="100%">
      <tr>
        <td>
          <font class="normal" size="4">Recently modified samples:</font>
          <ul>
            <rn:summarySearch setSummaryDaysPrefParamName="daysToShow"
                maxSearchResultsToInclude="50">
              <rn:searchResultsIterator usePaginationContext="true">
                <li>
                  <rn:link href="/lab/sample.jsp">
                    <rn:sampleField displayValueOnly="true"
                        fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                    </rn:link> modified
                    <rn:relativeDayField
                        displayRelativeDayWhenSampleWasModified="true" />
                </li>
              </rn:searchResultsIterator>
              <ctl:paginationChecker requirePageCountNoLessThan="2">
                <br />
                <font class="light">(50 most recent shown; 
                  <ctl:a href="/search.jsp">
                    <rn:labParam name="quickSummarySearchByLab" />list all
                    <ctl:paginationField fieldCode="<%=
                        PaginationField.TOTAL_ELEMENT_COUNT%>" /></ctl:a>)
                </font>
              </ctl:paginationChecker>
            </rn:summarySearch>
          </ul>
          <font class="label">
            Samples modified <rn:relativeDayField
                displaySampleDaysPreference="true" /> are listed.
          </font>
          <br />
          <font class="light">List actions for last:
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="1" />
              1
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="3" />
              3
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="7" />
              7
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="14" />
              14
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="30" />
              30
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="180" />
              180
            </ctl:a>
             -
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="daysToShow" value="365" />
              365
            </ctl:a>
            Days
            <br />
          </font> 
        </td>
        <td valign="top" align="right">
          <font class="normal" size="4">
            <rn:labField />
          </font>
          <br />
        </td>
      </tr>
    </table>
    <br />
    <table cellspacing="10">
      <tr>
        <td align="left" colspan="4">
          <hr align="left" width="300" color="#4A6695" />
          <font class="normal" size="4">In-progress samples:</font>
        </td>
      </tr>
      <tr>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Pending status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    setSummarySamplesPrefParamName="oldSampleCount"
                    statusCode="<%=SampleWorkflowBL.PENDING_STATUS%>"
                    ignorePreferredResultLimit="true">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <%--
                       - Ignore oldSampleCount and display up to
                       - 'maxSamplesToInclude' instead.  This is a *feature*
                       - because most lab users will want to see all their
                       - in-progress samples at once.
                      --%>
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.PENDING_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.PENDING_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </rn:summarySearch>
              </td>
            </tr>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Refinement pending status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch statusCode="<%=
                        SampleWorkflowBL.REFINEMENT_PENDING_STATUS%>"
                    ignorePreferredResultLimit="true">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <%--
                       - Ignore oldSampleCount and display up to
                       - 'maxSamplesToInclude' instead.  This is a *feature*
                       - because most lab users will want to see all their
                       - in-progress samples at once.
                      --%>
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.REFINEMENT_PENDING_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.REFINEMENT_PENDING_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </rn:summarySearch>
              </td>
            </tr>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBoxStopped">
            <tr>
              <th class="summaryHeaderStopped">
                <font class="dark">
                  Suspended status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.SUSPENDED_STATUS%>"
                    ignorePreferredResultLimit="true">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <%--
                       - Ignore oldSampleCount and display up to
                       - 'maxSamplesToInclude' instead.  This is a *feature*
                       - because most lab users will want to see all their
                       - in-progress samples at once.
                      --%>
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.SUSPENDED_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.SUSPENDED_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td align="left" colspan="4">
          <hr align="left" width="300" color="#4A6695" />
          <font class="normal" size="4">Finished samples:</font>
        </td>
      </tr>
      <tr>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Complete status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.COMPLETE_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.COMPLETE_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.COMPLETE_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Incomplete status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.INCOMPLETE_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                          fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Non-single-crystal sample status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.NON_SCS_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                    </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NON_SCS_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NON_SCS_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Withdrawn status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.WITHDRAWN_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.WITHDRAWN_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.WITHDRAWN_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  "No go" status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.NOGO_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
              </td>
            </tr>
            <tr>
              <td align="right">
                <font class="light">
                  <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NOGO_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NOGO_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBoxStopped">
            <tr>
              <th class="summaryHeaderStopped">
                <font class="dark">
                  Retracted status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.RETRACTED_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                            fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                             value="<%=String.valueOf(
                             SampleWorkflowBL.RETRACTED_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                             PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.RETRACTED_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td align="left" colspan="4">
          <hr align="left" width="300" color="#4A6695" />
          <font class="normal" size="4">Finished samples:</font>
        </td>
      </tr>
      <tr>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Complete, visible to public status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.COMPLETE_PUBLIC_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                           fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.COMPLETE_PUBLIC_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.COMPLETE_PUBLIC_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Incomplete, visible to public status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch statusCode="<%=
                    SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                    </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                          fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBox">
            <tr>
              <th class="summaryHeader">
                <font class="dark">
                  Non-single-crystal sample, visible to public status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.NON_SCS_PUBLIC_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                  </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                          fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NON_SCS_PUBLIC_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.NON_SCS_PUBLIC_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
        <td valign="top" width="250">
          <table class="summaryBoxStopped">
            <tr>
              <th class="summaryHeaderStopped">
                <font class="dark">
                  Retracted, visible to public status
                </font>
              </th>
            </tr>
            <tr>
              <td class="summaryBody">
                <rn:summarySearch
                    statusCode="<%=SampleWorkflowBL.RETRACTED_PUBLIC_STATUS%>">
                  <rn:searchResultsIterator usePaginationContext="true">
                    <rn:link href="/lab/sample.jsp">
                      <rn:sampleField displayValueOnly="true"
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                      </rn:link>&nbsp;&nbsp;
                    </rn:searchResultsIterator>
                </td>
              </tr>
              <tr>
                <td align="right">
                  <font class="light">
                    <ctl:paginationChecker requirePageCountNoLessThan="2">
                      (<ctl:paginationField
                        fieldCode="<%=PaginationField.PAGE_SIZE%>" />
                      most recent shown;
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.RETRACTED_PUBLIC_STATUS)%>" />
                        see all
                        <ctl:paginationField fieldCode="<%=
                            PaginationField.TOTAL_ELEMENT_COUNT%>"
                      /></ctl:a>)
                    </ctl:paginationChecker>
                    <ctl:paginationChecker requirePageCountNoMoreThan="1">
                      <ctl:a href="/search.jsp">
                        <rn:labParam name="quickSummarySearchByLab" />
                        <ctl:linkParam name="restrictToStatus"
                            value="<%=String.valueOf(
                            SampleWorkflowBL.RETRACTED_PUBLIC_STATUS)%>" />
                        (more detailed listing)
                      </ctl:a>
                    </ctl:paginationChecker>
                  </font>
                </td>
              </tr>
            </rn:summarySearch>
          </table>
        </td>
      </tr>
      <tr>
        <td align="left" colspan="4">
          <hr  align="left" width="300" color="#4A6695" />
          <font class="light">Limit samples per finished or public status to:
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="oldSampleCount" value="5" />
              5
            </ctl:a>
            - 
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="oldSampleCount" value="10" />
              10
            </ctl:a>
            - 
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="oldSampleCount" value="15" />
              15
            </ctl:a>
            - 
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="oldSampleCount" value="20" />
              20
            </ctl:a>
            - 
            <ctl:a href="/lab/summary.jsp">
              <ctl:linkParam name="oldSampleCount" value="25" />
              25
            </ctl:a>
          </font>
        </td>
      </tr>
    </table>
  </rn:authorizationChecker>
  <ctl:styleBlock>
    font.dark { font-family: Arial, Helvetica, Verdana; font-weight: bold; 
            color: #000050; }
    font.light { font-family: Arial, Helvetica, Verdana; font-style: narrow; 
            color: #A0A0A0; }
    font.normal { font-family: Arial, Helvetica, Verdana; font-style: normal; }
    table.summaryBox { width: 100%; border: 3px solid #DAE8F1; }
    table.summaryBoxStopped { width: 100%; border: 3px solid #F1E8DA; }
    th.summaryHeader { background-color: #DAE8F1; padding: 5px; } 
    th.summaryHeaderStopped { background-color: #F1E8DA; padding: 5px; } 
    td.summaryBody { height: 100px; text-align: center; }
  </ctl:styleBlock>
</rn:page>
