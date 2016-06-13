<%--
  - Reciprocal Net project
  -
  - addcitation.jsp
  -
  - 26-Jul-2002: eisiorho wrote first draft
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 04-Nov-2002: ekoperda got rid of all code for the REFERENCE_ADDED action
  - 06-Mar-2003: nsanghvi fixed bug 750 to make 'Citation' a required field
  - 23-Apr-2003: adharurk changed parameters of PageHelper.addCitation()
  - 05-Jun-2003: adharurk added correctability support
  - 19-Jun-2003: dfeng fixed bug 938 to make adding citation behave correctly
  - 16-Jul-2003: eisiorho fixed bug #967 in POST logic
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 30-Sep-2004: midurbin rewrote the page using custom tags
  - 14-Jan-2005: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 13-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Add Citation"
    workflowActionCode="<%=SampleWorkflowBL.CITATION_ADDED%>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.CITATION_ADDED_CORRECTED%>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Enter the comments, citations, and publication reference and click the
      "Save" button to record them.
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
        <rn:citationSelector>
          <tr>
            <th class="leadSectionHead"><rn:sampleFieldLabel /> (required):</th>
          </tr>
          <tr>
            <td style="text-align: center;">
              <rn:sampleField required="true" />
            </td>
          </tr>
          <tr>
            <th class="sectionHead" colspan="2">Comments</th>
          </tr>
          <tr>
            <td style="text-align: center;" colspan="2">
              <rn:wapComments />
            </td>
          </tr>
          <tr>
            <td class="formButtons" colspan="2">
              <rn:wapSaveButton />
              <rn:deleteSampleTextInfoButton label="Remove this citation" />
              <rn:wapCancelButton />
            </td>
          </tr>
        </rn:citationSelector>
      </table>
    </ctl:selfForm>
  </div>
</rn:wapPage>
