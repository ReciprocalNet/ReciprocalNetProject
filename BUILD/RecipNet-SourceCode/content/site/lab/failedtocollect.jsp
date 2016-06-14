<%--
  - Reciprocal Net project
  - failedtocollect.jsp
  -
  - 06-Feb-2003: midurbin wrote first draft
  - 23-Apr-2003: adharurk changed parameters of PageHelper.failedToCollect()
  - 05-Jun-2003: adharurk added correctability support
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 19-Aug-2004: cwestnea rewrote using custom tags
  - 24-Aug-2004: midurbin made WapPage's editSamplePageHref relative to the
  -              context path
  - 14-Jan-2005: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 13-Mar-2006: jobollin updated this page to use the revised styles
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Failed to Collect"
    workflowActionCode="<%= SampleWorkflowBL.FAILED_TO_COLLECT %>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.FAILED_TO_COLLECT_CORRECTED %>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Enter any comments about why it was not possible to collect
      sufficient data on this sample, then click the "Save" button.
      <ctl:errorMessage
        errorFilter="<%= WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>"><br/>
        <span class="errorMessage"
              style="font-weight: normal; font-style: italic;">
          You must address the flagged validation errors before the data
          will be accepted.
        </span>
      </ctl:errorMessage>
    </p>
    <ctl:selfForm>
      <table class="bodyTable">
        <tr>
          <th class="leadSectionHead" colspan="4">General Information</th>
        </tr>
        <tr>
          <th>Laboratory:</th>
          <td colspan="3">
            <rn:labField fieldCode="<%= LabField.NAME %>"
                displayAsLabel="true" />
          </td>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
          </th>
          <td colspan="3">
            <rn:sampleField fieldCode="<%= SampleInfo.LOCAL_LAB_ID %>"
                displayAsLabel="true" />
          </td>
        </tr>
        <tr>
          <th class="multiboxLabel">
            <rn:sampleFieldLabel
                fieldCode="<%=SampleTextBL.CRYSTALLOGRAPHER_NAME%>" />:
          </th>
          <td colspan="3">
            <rn:sampleField
                fieldCode="<%= SampleTextBL.CRYSTALLOGRAPHER_NAME %>">
              <ctl:extraHtmlAttribute name="tabindex" value="1"/>
            </rn:sampleField>
          </td>
        </tr>
        <tr>
          <th class="sectionHead" style="padding-top: 0;" colspan="2">
            Comments
          </th>
        </tr>
        <tr>
          <td align="center" colspan="2">
            <rn:wapComments />
          </td>
        </tr>
        <rn:ltaIterator id="ltas">
          <ctl:errorChecker errorFilter="<%= HtmlPageIterator.NO_ITERATIONS %>"
            invert="true">
            ${ltas.currentIterationFirst ?
              '<tr>
                <th class="sectionHead"
                  colspan="2">Additional Sample Information</th>
              </tr>' : ''}
          </ctl:errorChecker>
          <tr>
            <th>
              <rn:sampleFieldLabel />:
            </th>
            <td>
              <rn:sampleField /><rn:sampleFieldUnits />
            </td>
          </tr>
        </rn:ltaIterator>
        <tr>
          <td class="formButtons" colspan="2">
            <rn:wapSaveButton />
            <rn:wapCancelButton />
          </td>
        </tr>
      </table>
    </ctl:selfForm>
  </div>
</rn:wapPage>
