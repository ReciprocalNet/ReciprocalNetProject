<%--
  - Reciprocal Net project
  - ineligibleforrelease.jsp
  -
  - 26-Aug-2004: midurbin wrote the first draft based on releasetopublic.jsp
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 20-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 13-Jun-2005: midurbin replace SampleHistoryField field code's with the new
  -              enumeration
  - 13-Jul-2005: midurbin added new required parameters to samplePage
  --%>
<%@ page session="true" isThreadSafe="true"
        import="org.recipnet.site.shared.bl.SampleTextBL"
        import="org.recipnet.site.shared.bl.SampleWorkflowBL"
        import="org.recipnet.site.content.rncontrols.SampleHistoryField"
        import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:samplePage title="Release Sample to Public"  loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <p class="errorMessage">
    <font size="+1">
      This sample has been flagged ineligible for release for the following
      reason(s):
    </font>
  </p>
  <br />
  <ctl:selfForm>
    <ul>
      <rn:sampleTextIterator
            restrictByTextType="<%=SampleTextBL.INELIGIBLE_FOR_RELEASE%>">
        <li>
          <rn:sampleField displayAsLabel="true" />
          <br />
          <span class="ancillaryText">
            <rn:sampleHistoryField fieldCode="<%=
                    SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION%>" />
            on <rn:sampleHistoryField fieldCode="<%=
                    SampleHistoryField.FieldCode.ACTION_DATE%>" />
          </span>
        </li>
        <br />
        <br />
      </rn:sampleTextIterator>
    </ul>
    This sample cannot be released to the public until the errors above are
    corrected.
    <blockquote>
      <table>
        <tr>
          <td>
            <rn:buttonLink target="/lab/sample.jsp" label="Cancel" />
          </td>
          <td>
            <rn:buttonLink target="/lab/modifytext.jsp"
                    label="Modify text ..." />
          </td>
        </tr>
      </table>
    </blockquote>
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold; }
    .ancillaryText { font-size: x-small; color: #444444; }
  </ctl:styleBlock>
</rn:samplePage>
