<%--
  - Reciprocal Net project
  - @(#)sampeversions.jsp
  - 
  - 11-Jun-2003: dfeng wrote first draft
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 14-Jun-2005: midurbin rewrote using custom tags
  - 21-Jun-2005: midurbin replaced funny use of ErrorMessageElement with a
  -              ParityChecker
  - 13-Jul-2005: midurbin added new required parameters to SamplePage
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField" %>
<%@ page
    import="org.recipnet.site.content.rncontrols.DailySampleSummaryField" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<rn:samplePage title="Sample Versions" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <table cellspacing="10">
    <tr>
      <td>
        <strong>
          <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
        </strong>
        <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
            displayAsLabel="true" />
      </td>
    </tr>
    <tr>
      <td>
        <strong>Lab:</strong><rn:labField />
      </td> 
    </tr>
    <tr>
      <td>
        <strong>Provider:</strong><rn:providerField />
      </td>
    </tr>
  </table>
  <br />
  <table>
    <rn:sampleDailyVersionIterator>
      <ctl:parityChecker includeOnlyOnLast="true">
        <tr>
          <td>
            <table width="100%" style="border: 3px solid #DAE8F1">
              <tr>
                <th align="center" bgcolor="#DAE8F1" height="30">
                  <rn:sampleHistoryField fieldCode="<%=
                      SampleHistoryField.FieldCode.ACTION_DATE_ONLY%>" />
                  <br />
                  (most recent version)
                </th>
              </tr>
      </ctl:parityChecker>
      <ctl:parityChecker includeOnlyOnLast="true" invert="true">
        <tr>
          <td>
            <table width="100%" style="border: 3px solid #F1E8DA">
              <tr>
                <th align="center" bgcolor="#F1E8DA" height="30">
                  <rn:sampleHistoryField fieldCode="<%=
                      SampleHistoryField.FieldCode.ACTION_DATE_ONLY%>" />
                </th>
              </tr>
      </ctl:parityChecker>
            <tr> 
              <td>
                <strong>Status:</strong>
                  <rn:sampleHistoryField fieldCode="<%=
                      SampleHistoryField.FieldCode.NEW_STATUS%>" />
                  <rn:dailySampleSummaryIterator>
                    <br />Action
                    <i>
                      <rn:dailySampleSummaryField fieldCode="<%=
                          DailySampleSummaryField.FieldCode.ACTION%>" />
                    </i> performed
                    <rn:dailySampleSummaryField
                    fieldCode="<%=DailySampleSummaryField.FieldCode.COUNT%>" />
                    time<rn:dailySampleSummaryField fieldCode="<%=
                           DailySampleSummaryField.FieldCode.S_IF_PLURAL%>" />.
                  </rn:dailySampleSummaryIterator>
                <br />
                <rn:link href="/showsample.jsp">See this version...</rn:link>
              </td>   
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
    </rn:sampleDailyVersionIterator> 
  </table>
  <br />
  <rn:authorizationChecker invert="true">
    Additional versions of the sample may become visible after you
    <ctl:a href="/login.jsp">
      log in<ctl:currentPageLinkParam name="origUrl" />
    </ctl:a>.
  </rn:authorizationChecker>
</rn:samplePage>
