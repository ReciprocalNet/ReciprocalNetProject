<%--
 - Reciprocal Net project
 - @(#)lab/confirmrevert.jsp
 -
 - 05-Jul-2005: midurbin wrote the first draft
 - 13-Jul-2005: midurbin added new required parameters to revertWapPage
--%>
<%@page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@page import="org.recipnet.site.shared.db.SampleInfo"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:revertWapPage title="Revert sample"
    editSamplePageHref="/lab/samplehistory.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <center>
    <p class="errorMessage">
      Warning:&nbsp; You are about to revert this publicly visible sample to
      a version that is not public.&nbsp;&nbsp;This action is strongly
      discouraged because it may disrupt digital library harvesters or
      invalidate third-party hyperlinks.&nbsp;&nbsp; A preferred approach is
      to <rn:link href="/lab/retract.jsp">retract</rn:link> the sample
      instead.
    </p>
    <table>
      <tr>
        <td>
          <ctl:selfForm>
            <rn:wapCancelButton />
          </ctl:selfForm>
        </td>
        <td>
          <ctl:form method="GET" action="/lab/revert.jsp">
            <ctl:button label="Continue"/>
          </ctl:form>
        </td>
        <td>
          <ctl:selfForm>
            <rn:buttonLink label="Retract" target="/lab/retract.jsp" />
          </ctl:selfForm>
        </td>
      <tr>
    </table>
  </center>
  <ctl:styleBlock>
    .navLinksRight { text-align: right; }
    .errorMessage  { color: red; font-weight: bold;}
  </ctl:styleBlock>
</rn:revertWapPage>
