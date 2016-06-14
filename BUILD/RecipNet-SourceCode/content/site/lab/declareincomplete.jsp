<%--
  - Reciprocal Net project
  -
  - declareincomplete.jsp
  -
  - 20-Feb-2003: yli wrote first draft
  - 28-Mar-2003: eisiorho fixed bug #837
  - 31-Mar-2003: midurbin fixed bug #840
  - 31-Mar-2003: adharurk fixed bug #828
  - 23-Apr-2003: adharurk changed parameters of
  -              PageHelper.declareSampleIncomplete()
  - 05-Jun-2003: adharurk added correctability support
  - 12-Jun-2003: midurbin completed adharurk's multivalue support addition
  - 10-Jul-2003: ajooloor fixed bug #966
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 30-Aug-2004: midurbin rewrote using custom tags
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 14-Jan-2005: jobollin added <ctl:selfForm> elements
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 13-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page session="true" isThreadSafe="true"
        import="org.recipnet.site.shared.bl.SampleTextBL"
        import="org.recipnet.site.shared.bl.SampleWorkflowBL"
        import="org.recipnet.common.controls.HtmlPageIterator"
        import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Declare Incomplete"
        workflowActionCode="<%=SampleWorkflowBL.DECLARED_INCOMPLETE%>"
        workflowActionCodeCorrected="<%=
                SampleWorkflowBL.DECLARED_INCOMPLETE_CORRECTED%>"
        editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Explain why sample analysis will not be completed, then click the "Save"
      button to record the changes.
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
          <th class="leadSectionHead" colspan="2">
            <rn:sampleFieldLabel fieldCode="<%=
                    SampleTextBL.INCOMPLETENESS_EXPLANATION%>" />:
          </th>
        </tr>
        <tr>
          <td style="text-align: center;" colspan="2">
            <rn:sampleField fieldCode="<%=
                    SampleTextBL.INCOMPLETENESS_EXPLANATION%>" />
          </td>
        </tr>
        <rn:ltaIterator id="ltas">
          <ctl:errorChecker errorFilter="<%= HtmlPageIterator.NO_ITERATIONS %>"
            invert="true">
            ${ltas.currentIterationFirst ?
              '<tr>
                <th class="sectionHead" colspan="2">Additional Information</th>
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
          <th class="sectionHead" colspan="2">
            Comments:
          </th>
        </tr>
        <tr>
          <td style="text-align: center;" colspan="2">
            <rn:wapComments />
          </td>
        </tr>
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
