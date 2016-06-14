<%--
  - Reciprocal Net project
  - declarenonscs.jsp
  -
  - 29-Jan-2003: midurbin wrote first draft
  - 10-Feb-2003: midurbin added localtracking support
  - 19-Feb-2003: jobollin edited the page instructions as part of task 717
  - 23-Apr-2003: adharurk changed parameters of PageHelper.declareNonSCS()
  - 05-Jun-2003: adharurk added correctability support
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 19-Aug-2004: cwestnea rewote using custom tags
  - 24-Aug-2004: midurbin made WapPage's editSamplePageHref relative to the
  -              context path
  - 14-Jan-2005: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 13-Mar-2006: jobollin revised this page to use the new styles
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Declare Non-SCS"
    workflowActionCode="<%= SampleWorkflowBL.DECLARED_NON_SCS %>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.DECLARED_NON_SCS_CORRECTED%>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Click "Save" below to declare that this sample is intended for something
      other than single-crystal analysis (<i>i.e.</i> for powder diffraction,
      small-angle scattering sample, <i>etc.</i>).  This is intended for
      classifying samples before any experiment is performed on them.
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
