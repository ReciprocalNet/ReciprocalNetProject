<%--
  - Reciprocal Net project
  -
  - changeacl.jsp
  -
  - 28-Jul-2002: eisiorho wrote first draft
  - 14-Aug-2002: eisiorho fixed bug #359
  - 19-Aug-2002: eisiorho changed changeacl.jsp to reflect change in
  -              PageHelper.java, task #362
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 14-Mar-2003: eisiorho fixed bug #776, fixed formatting
  - 18-Mar-2003: nsanghvi removed extraneous text from UI
  - 23-Apr-2003: adharurk changed parameters of 
  -              PageHelper.endCommitAccessLevelChange()
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 16-Nov-2004: midurbin rewrote using custom tags
  - 14-Jan-2005: jobollin added <ctl:selfForm> tags
  - 10-Jun-2005: midurbin added code to disallow modification of one's own
  -              access level
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 01-Feb-2006: jobollin inserted radio button groups to support changes to
  -              sampleAccessSelector
  - 13-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.db.SampleAccessInfo" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Change Access Level"
    workflowActionCode="<%= SampleWorkflowBL.CHANGED_ACL %>"
    workflowActionCodeCorrected="<%=SampleWorkflowBL.CHANGED_ACL%>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Every active user account on this site is listed below. To change
      the access level of any user or users to this particular sample, select
      the radio button corresponding to the desired access level(s), then
      click the "Save" button to record the change(s).
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
      <table class="bodyTable" cellspacing="0">
        <thead class="bordered">
        <tr>
          <th class="leadSectionHead" style="text-align: center"
            colspan="2">User Information</th>
          <th class="leadSectionHead" style="text-align: center"
            colspan="3">Access Level</th>
        </tr>
        <tr>
          <th class="textSubhead">User Name</th>
          <th class="textSubhead">Affiliation</th>
          <th class="radioSubhead">Read/Write</th>
          <th class="radioSubhead">Read Only</th>
          <th class="radioSubhead">No Access</th>
        </tr>
        </thead>
        <tbody class="bordered">
        <rn:userIterator sortByFullName="true">
          <tr class="${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}">
            <td style="padding-right: 1em;"><rn:userField /></td>
            <td>
              <rn:userChecker includeIfLabUser="true">
                <rn:labField />
              </rn:userChecker>
              <rn:userChecker includeIfProviderUser="true">
                <rn:providerField />
              </rn:userChecker>
            </td>
            <ctl:radioButtonGroup>
            <td class="radio">
              <rn:sampleAccessSelector
                      accessLevel="<%=SampleAccessInfo.READ_WRITE%>" />
            </td>
            <rn:userChecker includeIfCurrentlyLoggedInUser="true" invert="true">
              <td class="radio">
                <rn:sampleAccessSelector
                        accessLevel="<%=SampleAccessInfo.READ_ONLY%>" />
              </td>
              <td class="radio">
                <rn:sampleAccessSelector
                        accessLevel="<%=SampleAccessInfo.INVALID_ACCESS%>" />
              </td>
            </rn:userChecker>
            <rn:userChecker includeIfCurrentlyLoggedInUser="true">
              <td colspan="2" style="text-align: center;">
                <span class="note">
                  (you may not reduce your own access level)
                </span>
              </td>
            </rn:userChecker>
            </ctl:radioButtonGroup>
          </tr>
        </rn:userIterator>
        </tbody>
        <tbody>
        <tr>
          <th class="sectionHead" colspan="5">Comments</th>
        </tr>
        <%--
          - On a typical workflow action page, LTA's would be included here, but
          - because changes to the access control list for a sample are not
          - versioned and LTA's are, it is not appropriate for this page.
         --%>
        <tr>
          <td colspan="5" style="text-align: center;">
            <rn:wapComments />
          </td>
        </tr>
        <tr>
          <td class="formButtons" colspan="5">
            <rn:wapSaveButton />
            <rn:wapCancelButton />
          </td>
        </tr>
        </tbody>
      </table>
    </ctl:selfForm>
  </div>
  <ctl:styleBlock>
    table.bodyTable th.textSubhead {
        text-align: left; padding-right: 1em; }
    table.bodyTable th.radioSubhead {
        text-align: center; padding: 0 1em 0 1em; }
    table.bodyTable td.radio { text-align: center; }
    table.bodyTable td { white-space: nowrap; }
    thead.bordered { background: #32357D; color: #FFFFFF;
         border-left: 3px solid #32357D;
         border-right: 3px solid #32357D; border-top: 3px solid #32357D; }
    tbody.bordered { border-left: 3px solid #32357D;
         border-right: 3px solid #32357D; border-bottom: 3px solid #32357D; }
    .note { color: #505050; font-style: italic; font-size: x-small; }
  </ctl:styleBlock>
</rn:wapPage>
