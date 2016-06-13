<%--
  - Reciprocal Net project
  - releasetopublic.jsp
  -
  - 26-Jul-2002: eisiorho wrote first draft
  - 07-Aug-2002: eisiorho fixed bug #325
  - 14-Aug-2002: eisiorho fixed bug #344
  - 29-Aug-2002: leqian added localtracking support
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 26-Mar-2003: jrhanna added special handling for INELIGIBLE_FOR_RELEASE
  -              annotations and missing holding records
  - 27-Mar-2003: midurbin fixed bug #825
  - 01-Apr-2003: dfeng fixed bug #843
  - 23-Apr-2003: adharurk changed parameters of
  -              PageHelper.releaseSampleToPublic()
  - 01-May-2003: midurbin added multi-value support to copyright and
  -              laymans explanation, added correctability support, and
  -              included new jspf files.
  - 09-May-2003: midurbin fixed bug #900
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 26-Aug-2004: midurbin rewrote page using custom tags
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 14-Jan-2005: jobollin add <ctl:selfForm> tags
  - 10-Mar-2005: midurbin added preferred name input field
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 10-Mar-2006: jobollin updated this page to use the new styles from the
  -              shared stylesheet and to verify correct behavior of the
  -              various tweaked SampleField varieties
  - 08-Dec-2015: yuma added preserveParam1
  --%>
<%@ page session="true" isThreadSafe="true"
        info="Release Sample to Public"
        import="org.recipnet.site.content.rncontrols.SampleField"
        import="org.recipnet.site.content.rncontrols.WapPage"
        import="org.recipnet.site.shared.bl.SampleTextBL"
        import="org.recipnet.site.shared.bl.SampleWorkflowBL"
        import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Release Sample to Public"
        workflowActionCode="<%=SampleWorkflowBL.RELEASED_TO_PUBLIC%>"
        workflowActionCodeCorrected="<%=
                SampleWorkflowBL.RELEASED_TO_PUBLIC_CORRECTED%>"
        editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <rn:sampleChecker includeIfNotHeldLocally="true">
    <ctl:redirect target="/lab/nolocalholdings.jsp"
            preserveParam="sampleId" preserveParam1="sampleHistoryId" />
  </rn:sampleChecker>
  <rn:sampleChecker includeIfIneligibleForRelease="true">
    <ctl:redirect target="/lab/ineligibleforrelease.jsp"
            preserveParam="sampleId" preserveParam1="sampleHistoryId" />
  </rn:sampleChecker>
  <div class="pageBody">
    <p class="pageInstructions">
      Enter any additional information about the sample that is appropriate for
      public dissemination.  All such items are optional, but you can provide an
      explanation for the non-technical audience, a preferred name, and a
      copyright notice if you choose.  Click the "Save" button to record the
      data and release the sample.
      <ctl:errorMessage
              errorFilter="<%=WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR%>">
        <span class="errorMessage"><br/>
          You must address the flagged validation errors before the data
          will be accepted.
        </span>
      </ctl:errorMessage>
    </p>
    <ctl:selfForm>
      <table class="bodyTable">
        <tr>
          <th colspan="2" class="leadSectionHead">General Information</th>
        </tr>
        <tr>
          <th style="width: 12em;">
            <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
          </th>
          <td>
            <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                    displayAsLabel="true" />
          </td>
        </tr>
        <tr>
          <th style="width: 12em;">Originating Lab:</th>
          <td><rn:labField /></td>
        </tr>
        <tr>
          <th style="width: 12em;">Originating Provider:</th>
          <td><rn:providerField /></td>
        </tr>
        <tr>
          <th colspan="2" class="sectionHead">Additional Information</th>
        </tr>
        <tr>
          <th colspan="2" style="text-align: left;">
              <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.LAYMANS_EXPLANATION%>" />:
            <div class="fieldNotes">
              Describe aspects of the sample that might be interesting to a
              general, non-technical audience.  Use appropriately
              non-technical language.
            </div>
          </th>
        </tr>
        <tr>
          <td colspan="2">
            <rn:sampleField
                    fieldCode="<%=SampleTextBL.LAYMANS_EXPLANATION%>" />
          </td>
        </tr>
        <tr>
          <th colspan="2" style="text-align: left;">
              <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.PREFERRED_NAME%>" />:
            <div class="fieldNotes">
              The 'preferred' sample name will be displayed most
              prominently on the basic view of the sample, and will be the
              first name displayed for this sample in search results.
              If no name is selected here then each time a 'preferred name'
              is needed one will be be chosen automatically, giving
              preference to common names over trade names over IUPAC
              names.  Specify a name here only if the automatic selection
              behavior is inappropriate.
            </div>
          </th>
        </tr>
        <tr>
          <td colspan="2">
            <rn:sampleField id="preferredName"
                    fieldCode="<%=SampleTextBL.PREFERRED_NAME%>" />
            <ctl:errorMessage errorFilter="<%=SampleField.NO_SAMPLE_NAMES%>"
                    errorSupplier="${preferredName}">
              <strong>
                There are currently no names from which to choose a preferred
                one.
              </strong>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th colspan="2" style="text-align: left; padding-top: 0.5em;">
              <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.COPYRIGHT_NOTICE%>" />:
            <div class="fieldNotes">
              Enter the text of any copyright notice that should be
              recorded with this sample's information.
            </div>
          </th>
        </tr>
        <tr>
          <td colspan="2">
            <rn:sampleField fieldCode="<%=SampleTextBL.COPYRIGHT_NOTICE%>"
                    getDefaultCopyrightNoticeFromLabContext="true" />
          </td>
        </tr>
        <rn:ltaIterator>
          <tr>
            <th><rn:sampleFieldLabel />:</th>
            <td><rn:sampleField /><rn:sampleFieldUnits /></td>
          </tr>
        </rn:ltaIterator>
        <tr>
          <th colspan="2" class="sectionHead">Comments</th>
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
    </ctl:selfForm>
  </div>
  <ctl:styleBlock>
    .fieldNotes { color: #505050; font-style: italic; white-space: normal; width: 50em; }
  </ctl:styleBlock>
</rn:wapPage>
