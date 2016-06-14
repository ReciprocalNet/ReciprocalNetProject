<%--
  - Reciprocal Net project
  - nolocalholdings.jsp
  -
  - 26-Aug-2004: midurbin wrote the first draft based on releasetopublic.jsp
  - 20-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to samplePage
  --%>
<%@ page session="true" isThreadSafe="true"
        import="org.recipnet.site.shared.bl.SampleWorkflowBL"
        import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:samplePage title="Release Sample to Public" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <p class="errorMessage">
    This sample has no data directory and thus cannot be released to the public
    at this time.
    <br />
    Please cancel this operation and create a data directory before returning
    to this page.
  </p>
  <ctl:selfForm>
    <rn:buttonLink target="/lab/sample.jsp" label="Cancel" />
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold; }
  </ctl:styleBlock>
</rn:samplePage>
