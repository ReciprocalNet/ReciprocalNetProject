<%--
  - Reciprocal Net project
  - customfields.jsp
  -
  - 07-Nov-2002: eisiorho wrote first draft
  - 20-Nov-2002: ekoperda fixed bug #607
  - 14-Feb-2003: midurbin added include to footer.jsp and adjusted 
  -              localtracking visibility constants.
  - 23-Apr-2003: adharurk changed parameters of PageHelper.modifySampleLta()
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 29-Sep-2004: midurbin renamed the visibility constant
  -              VISIBLE_ON_LOCAL_TRACKING to VISIBLE_ON_CUSTOMFIELDS
  - 29-Sep-2004: midurbin rewrote using custom tags and renamed file
  - 14-Jan-2005: jobollin added <ctl:selfForm> elements
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 10-Mar-2006: jobollin moved the function of nocustomtags.jsp into this
  -              page
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Modify Custom Fields"
    workflowActionCode="<%= SampleWorkflowBL.MODIFIED_LTAS%>"
    workflowActionCodeCorrected="<%=SampleWorkflowBL.MODIFIED_LTAS%>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <ctl:selfForm>
    <rn:sampleChecker includeIfNoVisibleLtas="true">
      <p class="errorMessage">
        No custom fields have been configured for this sample's
        originating lab.  Local system administrators define custom
        fields on a per-lab by editing the Reciprocal Net Site Software's
        configuration file.  More information about this feature is
        available in the User Guide.
      </p>
      <rn:wapCancelButton/>
    </rn:sampleChecker>
    <rn:sampleChecker includeIfNoVisibleLtas="true" invert="true">
      <p class="pageInstructions">
        Modify the custom fields below and click the &quot;Submit&quot; button to
        record the changes.
        <ctl:errorMessage
          errorFilter="<%= WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>">
          <span class="errorMessage"
                style="font-weight: normal; font-style: italic;">
            You must address the flagged validation errors before the data
            will be accepted.
          </span>
        </ctl:errorMessage>
      </p>
      <table class="bodyTable">
        <rn:ltaIterator id="ltas">
          ${ltas.currentIterationFirst
            ? '<tr><th class="leadSectionHead" colspan="2">Custom Fields</th></tr>'
            : ''}
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
          <th class="sectionHead" colspan="2">Comments</th>
        </tr>
        <tr>
          <td colspan="2" style="text-align: center;">
            <rn:wapComments />
          </td>
        </tr>
        <tr>
          <td colspan="2" class="formButtons">
            <rn:wapSaveButton />
            <rn:wapCancelButton />
          </td>
        </tr>
      </table>
    </rn:sampleChecker>
  </ctl:selfForm>
  </div>
</rn:wapPage>

