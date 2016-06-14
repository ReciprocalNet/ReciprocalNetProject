<%--
  - Reciprocal Net project
  -
  - withdraw.jsp
  -
  - 11-Mar-2003: ajooloor wrote first draft
  - 19-Mar-2003: nsanghvi fixed bug #791 
  - 31-Mar-2003: adharurk fixed bug #831
  - 23-Apr-2003: adharurk changed parameters of PageHelper.withdrawSample()
  - 05-Jun-2003: adharurk added correctability support
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 19-Aug-2004: cwestnea rewrote using custom tags
  - 24-Aug-2004: midurbin made WapPage's editSamplePageHref relative to the
  -              context path
  - 14-Jan-2004: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 13-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Withdraw Sample"
    workflowActionCode="<%= SampleWorkflowBL.WITHDRAWN_BY_PROVIDER %>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.WITHDRAWN_BY_PROVIDER_CORRECTED%>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Enter any comments about why the sample is being withdrawn from the
      Reciprocal Net, then click the "Save" button to effect the sample
      withdrawl.
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
          <th class="leadSectionHead" colspan="2">Comments</th>
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
