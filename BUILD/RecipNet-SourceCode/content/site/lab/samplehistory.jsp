<%--
  - Reciprocal Net project
  -
  - lab/samplehistory.jsp
  -
  - 11-Jul-2002: leqian wrote first draft
  - 07-Aug-2002: ekoperda accommodated bug #288 by changing all references
  -              to fullSample.status to fullSample.mostRecentStatus.
  - 12-Aug-2002: leqian fixed bug #328, add pageTitle parameter
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 19-Feb-2003: midurbin multi-moded page and added special UI handling for
  -              a transition from public to non-public sample
  - 23-Apr-2003: adharurk removed clientIp from the parameter list of 
  -              PageHelper.revertToPreviousVersion
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 15-Jul-2003: dfeng changed the "View" link from sample.jsp to 
  -              showsample.jsp
  - 31-Jul-2003: midurbin fixed bug #997
  - 08-Aug-2003: midurbin added option to revert repository files
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 05-Jul-2005: midurbin rewrote using custom tags, factoring the alternate
  -              modes to revert.jsp and considerrevert.jsp
  - 13-Jul-2005: midurbin added new required parameters to samplePage and
  -              redirectToLogin
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 23-Sep-2005: midurbin added ShowsampleLink tags where applicable
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@page import="org.recipnet.site.shared.db.SampleInfo"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:samplePage title="Sample History" requireSampleHistoryId="false"
        ignoreSampleHistoryId="true" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <rn:authorizationChecker canEditSample="true" invert="true"
      suppressRenderingOnly="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_SEE_LAB_SUMMARY%>"/>
  </rn:authorizationChecker>
  <br />
  <table>
    <tr>
      <th class="twoColumnLeft">Lab:</th>
      <td class="twoColumnRight">
        <rn:labField />
      </td>
    </tr>
    <tr>
      <th class="twoColumnLeft">Provider:</th>
      <td class="twoColumnRight">
        <rn:providerField />
      </td>
    </tr>
    <tr>
      <th class="twoColumnLeft">
        <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
      </th>
      <td class="twoColumnRight">
        <rn:sampleField displayValueOnly="true"
            fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
      </td>
    </tr>
    <tr>
      <th class="twoColumnLeft">
        <rn:sampleFieldLabel fieldCode="<%=SampleInfo.STATUS%>" />:
      </th>
      <td class="twoColumnRight">
        <rn:sampleField fieldCode="<%=SampleInfo.STATUS%>" />
      </td> 
    </tr>
  </table>
  <br /> 
  <table>
    <tr>
      <th class="headerCell">Date</th>
      <th class="headerCell">User</th>
      <th class="headerCell">Action</th>
      <th class="headerCell">New status</th>
      <th class="headerCell">Comments</th>
      <th class="headerCell">&nbsp;</th>
      <th class="headerCell">&nbsp;</th>
    </tr>
    <rn:sampleHistoryIterator id="hit">
      <ctl:parityChecker includeOnlyOnLast="true">
        <tr class="lastRow">
      </ctl:parityChecker>
      <ctl:parityChecker includeOnlyOnLast="true" invert="true">
        <tr class="${rn:testParity(hit.iterationCountSinceThisPhaseBegan,
                                   'evenRow', 'oddRow')}">
      </ctl:parityChecker>
        <td class="bodyCell">
          <rn:sampleHistoryField
              fieldCode="<%=SampleHistoryField.FieldCode.ACTION_DATE%>" />
        </td>
        <td class="bodyCell">
          <rn:sampleHistoryField fieldCode="<%=
         SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION%>" />
        </td>
        <td class="bodyCell">
          <rn:sampleHistoryField fieldCode="<%=
              SampleHistoryField.FieldCode.ACTION_PERFORMED%>" />
        </td>
        <td class="bodyCell">
          <rn:sampleHistoryField
              fieldCode="<%=SampleHistoryField.FieldCode.NEW_STATUS%>" />
        </td>
        <td class="bodyCell">
          <rn:sampleHistoryField fieldCode="<%=
              SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS%>" />
          &nbsp;
        </td> 
        <td class="bodyCell">
          <rn:showsampleLink sampleIsKnownToBeLocal="true"
              basicPageUrl="/showsamplebasic.jsp"
              detailedPageUrl="/showsampledetailed.jsp">
            <rn:sampleParam name="sampleId" />
            <rn:sampleHistoryParam name="sampleHistoryId" />
            View
          </rn:showsampleLink>
        </td>
        <ctl:parityChecker includeOnlyOnLast="true">
          <td class="bodyCell">Most recent version</td>
        </ctl:parityChecker>
        <ctl:parityChecker includeOnlyOnLast="true" invert="true">
          <td class="bodyCell">
            <rn:sampleChecker includeIfUnpublishedVersionOfPublicSample="true">
              <ctl:a href="/lab/considerrevert.jsp">
                <rn:sampleParam name="sampleId" />
                <rn:sampleHistoryParam name="sampleHistoryId"
                        useMostRecentHistoryId="true" />
                <rn:sampleHistoryParam name="targetSampleHistoryId" />
                Revert to this version
              </ctl:a>
            </rn:sampleChecker>
            <rn:sampleChecker includeIfUnpublishedVersionOfPublicSample="true"
                invert="true">
              <ctl:a href="/lab/revert.jsp">
                <rn:sampleParam name="sampleId" />
                <rn:sampleHistoryParam name="sampleHistoryId"
                        useMostRecentHistoryId="true" />
                <rn:sampleHistoryParam name="targetSampleHistoryId" />
                Revert to this version
              </ctl:a>
            </rn:sampleChecker>
          </td>
        </ctl:parityChecker>
      </tr>
    </rn:sampleHistoryIterator>
  </table>
  <table width="100%">
    <tr>
      <td align="right">
        <rn:showsampleLink sampleIsKnownToBeLocal="true"
            basicPageUrl="/showsamplebasic.jsp"
            detailedPageUrl="/showsampledetailed.jsp">
          <rn:sampleParam name="sampleId" />
          <rn:sampleHistoryParam name="sampleHistoryId" />
          View this sample...
        </rn:showsampleLink>
        <br />
        <rn:link href="/lab/sample.jsp">Edit this sample...</rn:link>
      </td>
    </tr>
  </table>
  <ctl:styleBlock>
    .navLinksRight { text-align: right; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
    .evenRow { background-color: #D6D6D6; }
    .oddRow { background-color: #F0F0F0; }
    .bodyCell { padding: 3px; }
    .lastRow { background-color: #8888A0; color: #FFFFFF;
        text-align: left;}
    th.headerCell { background-color: #909090; color: #FFFFFF;
        text-align: left; padding: 5px; }
  </ctl:styleBlock>
</rn:samplePage>
