<%--
  - Reciprocal Net project
  - submit.jsp
  -
  - 26-Jul-2002: eisiorho wrote first draft
  - 06-Aug-2002: eisiorho fixed bugs #248, #259
  - 07-Aug-2002: eisiorho fixed bug #289
  - 09-Aug-2002: eisiorho fixed bug #326
  - 29-Aug-2002: leqian added localtracking support
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 25-Oct-2002: adharurk replaced the call to the function
  -              getAllProviderInfo() with one to getAllActiveProviderInfo()
  - 12-Nov-2002: eisiorho fixed bug #597
  - 25-Feb-2003: eisiorho fixed bug #754, by improving support for blank form
  -              fields
  - 20-Mar-2003: ajooloor corrected UI behavior for the mychoice textbox
  - 23-Apr-2003: adharurk changed parameters of PageHelper.submitNewSample()
  - 26-Jun-2003: dfeng replace include file common.inc with common.jspf
  - 28-Jul-2003: jrhanna added multi-value and correctiblity support
  - 31-Jul-2003: dfeng added support for PROVIDER_REFERENCE_NUMBER textfield
  - 05-Aug-2003: jrhanna fixed bug #1011
  - 25-Aug-2003: midurbin fixed bug #1029
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 29-Mar-2004: midurbin fixed bug #1059
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 30-Jun-2005: midurbin rewrote using custom tags
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 27-Sep-2005: midurbin added option to use a Lab-assigned localLabId
  - 06-Oct-2005: midurbin used the new PROVIDER_ID_FIELD field code
  - 02-Nov-2005: midurbin added an error message for the unlikely case where
  -              a user has posted an invalid provider id
  - 08-Nov-2005: midurbin added formula validation and overruling
  - 10-Mar-2006: jobollin updated to use standard styles from the stylesheet
  -              and to verify that the new SampleField characteristics work
  -              acceptably on this page.
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.AutoLocalLabIdField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ValidationOverrideButton"
 %>
<%@ page import="org.recipnet.site.content.rncontrols.WapPage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleDataInfo" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ page import="org.recipnet.site.shared.db.SampleTextInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Submit" workflowActionCode="<%=SampleWorkflowBL.SUBMITTED%>"
       workflowActionCodeCorrected="<%=SampleWorkflowBL.SUBMITTED_CORRECTED%>"
       editSamplePageHref="/lab/sample.jsp" requireSampleId="false"
       requireSampleHistoryId="false" loginPageUrl="/login.jsp"
       currentPageReinvocationUrlParamName="origUrl"
       authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
      Enter the basic crystal data on the form below and click the
      "Save" button to record it.
    <ctl:errorMessage errorFilter="<%=
        WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR%>">
      <br/><span class="errorMessage"
        style="font-size: normal; font-style: italic">You must address the
        flagged validation errors before the data will be accepted.
      </span>
    </ctl:errorMessage>
    <ctl:errorMessage errorFilter="<%=WapPage.DUPLICATE_LOCAL_LAB_ID%>">
      <br/><span class="errorMessage"
        style="font-size: normal; font-style: italic">The selected unique ID is
        already in use; please choose a different one.
      </span>
    </ctl:errorMessage>
    <ctl:errorMessage errorFilter="<%=WapPage.INVALID_PROVIDER_ID%>">
      <br/><span class="errorMessage"
        style="font-size: normal; font-style: italic">You may not submit samples
        for the specified provider.  Specify a different one to continue, or
        contact the Reciprocal Net site administrator.
      </span>
    </ctl:errorMessage>
    </p>
    <ctl:selfForm>
      <table class="bodyTable">
        <tr>
          <th class="leadSectionHead" colspan="2">General Information</th>
        </tr>
        <tr>
          <th >Originating lab:</th>
          <td><rn:labField /></td>
        </tr>
        <rn:sampleChecker includeIfNewSample="true" invert="true">
          <tr>
            <th>
              <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
            </th>
            <td>
                <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                    displayValueOnly="true"/>
            </td>
          </tr>
        </rn:sampleChecker>
        <rn:sampleChecker includeIfNewSample="true">
          <ctl:radioButtonGroup id="sampleNumberSource" initialValue="myChoice">
          <tr>
            <th style="vertical-align: top;">Unique local ID:</th>
            <td style="white-space: nowrap;">
              <ctl:radioButton option="myChoice" id="myChoiceRadio" />
              My choice:
              <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                  id="localLabId"
                  required="${myChoiceRadio.valueAsBoolean}"
                  prohibited="${!myChoiceRadio.valueAsBoolean}" />
              <rn:autoLocalLabIdChecker
                      includeOnlyIfAutoNumberingIsConfigured="true">
                <br />
                <ctl:radioButton option="labAssign" id="labAssign" />
                Next available sample number:&nbsp;&nbsp; 
                <span class="darkText">
                  <rn:autoLocalLabIdField id="labAutoId"
                        selected="${labAssign.valueAsBoolean}" />
                </span>
                <rn:autoLocalLabIdChecker
                        includeOnlyIfNumbersAreExhausted="true">
                  <span class="errorMessage">(numbers exhausted)</span>
                </rn:autoLocalLabIdChecker>
                <br />
                <ctl:radioButton option="autoAssign" />
                Default numeric identifier
                <ctl:errorMessage errorSupplier="${labAutoId}"
                    errorFilter="<%=
                        AutoLocalLabIdField.SELECTED_WITH_NULL_VALUE%>">
                  <div style="max-width: 30em;">
                    <div class="errorMessageSmall">
                      The available numbers are exhausted.
                    </div>
                    <div class="errorMessageExtraSmall">
                      You must not select "Next available sample number"
                      because no more numbers remain.  Please select
                      another option and click "Save" to continue.
                    </div>
                  </div>
                </ctl:errorMessage>
                <ctl:errorMessage errorSupplier="${localLabId}"
                    invertFilter="true"
                    errorFilter="<%=SampleField.REQUIRED_VALUE_IS_MISSING%>">
                  <ctl:errorMessage errorSupplier="${labAutoId}"
                      errorFilter="<%=
                        AutoLocalLabIdField.PARSED_VALUE_IS_NOT_MOST_RECENT%>">
                    <div style="max-width: 30em;">
                      <div class="errorMessageSmall">
                        The previously suggested sample ID has been taken.
                      </div>
                      <div class="errorMessageExtraSmall">
                        A new sample number is displayed above.  Click the
                        "Save" button to continue with the revised suggestion.
                      </div>
                    </div>
                  </ctl:errorMessage>
                </ctl:errorMessage>
                <ctl:errorMessage errorSupplier="${labAutoId}"
                    invertFilter="true" errorFilter="<%=
                        AutoLocalLabIdField.PARSED_VALUE_IS_NOT_MOST_RECENT%>">
                  <ctl:errorMessage
                      errorFilter="<%=WapPage.DUPLICATE_LOCAL_LAB_ID%>" >
                    <div style="max-width: 30em">
                      <div class="errorMessageSmall">
                        A sample with this local ID already exists for this lab.
                      </div>
                      <div class="errorMessageExtraSmall">
                        Select a new ID and click "Save" to continue.
                      </div>
                    </div>
                  </ctl:errorMessage>
                </ctl:errorMessage>
              </rn:autoLocalLabIdChecker>
              <rn:autoLocalLabIdChecker invert="true"
                      includeOnlyIfAutoNumberingIsConfigured="true">
                <br />
                <ctl:radioButton option="autoAssign" />
                Default numeric identifier
                <ctl:errorMessage
                    errorFilter="<%=WapPage.DUPLICATE_LOCAL_LAB_ID%>" >
                  <div style="max-width: 30em;">
                    <div class="errorMessageSmall">
                      A sample with this local ID already exists for this lab.
                    </div>
                    <div class="errorMessageExtraSmall">
                      This problem is probably transient.  Click the "Save"
                      button again to continue.
                    </div>
                  </div>
                </ctl:errorMessage>
              </rn:autoLocalLabIdChecker>
              <ctl:errorMessage errorSupplier="${localLabId}"
                  errorFilter="<%=SampleField.REQUIRED_VALUE_IS_MISSING%>">
                <div style="max-width: 30em;">
                  <div class="errorMessageSmall">
                    No local sample ID entered.
                  </div>
                  <div class="errorMessageExtraSmall">
                    Either enter a unique local sample ID or specify that
                    one should be generated automatically.
                  </div>
                </div>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="${localLabId}"
                  errorFilter="<%=SampleField.PROHIBITED_VALUE_IS_PRESENT%>">
                <div style="max-width: 30em;">
                  <div class="errorMessageSmall">
                    You have both entered a local sample ID and requested
                    automatic local sample ID assignment.
                  </div>
                  <div class="errorMessageExtraSmall">
                    If you wish to use the entered ID, select
                    <span style="white-space: nowrap;">"My Choice"</span>,
                    otherwise clear the sample text field associated with that
                    option.  Click the "Save" button again to continue.
                  </div>
                </div>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="${localLabId}"
                  errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
                <div style="max-width: 30em;">
                  <div class="errorMessageSmall">
                    The requested local sample ID is not valid.
                  </div>
                  <div class="errorMessageExtraSmall">
                    IDs may contain only upper- and lowercase ASCII letters,
                    ASCII decimal digits, hyphens, underscores, and periods.
                    They may not be blank.
                  </div>
                </div>
              </ctl:errorMessage>
            </td>
          </tr>
          </ctl:radioButtonGroup>
        </rn:sampleChecker>
        <tr>
          <th class="sectionHead" colspan="2">Sample Provider Information</th>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel
                    fieldCode="<%=SampleDataInfo.PROVIDER_ID_FIELD%>" />:
          </th>
          <td>
            <rn:sampleField
                    fieldCode="<%=SampleDataInfo.PROVIDER_ID_FIELD%>" />
            <ctl:errorMessage errorFilter="<%=WapPage.INVALID_PROVIDER_ID%>" >
              <div class="errorMessageSmall" style="max-width: 30em;">
                You may not submit samples to that provider!
              </div>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel
                fieldCode="<%=SampleTextBL.PROVIDER_REFERENCE_NUMBER%>" />:
          </th>
          <td>
            <rn:sampleField
                fieldCode="<%=SampleTextBL.PROVIDER_REFERENCE_NUMBER%>" />
          </td>
        </tr>
        <tr>
          <th class="multiboxLabel">
            <rn:sampleFieldLabel
                fieldCode="<%=SampleTextBL.SAMPLE_PROVIDER_NAME%>" />:
          </th>
          <td>
            <rn:sampleField
                fieldCode="<%=SampleTextBL.SAMPLE_PROVIDER_NAME%>" />
          </td>
        </tr>
        <tr>
          <th colspan="2" class="sectionHead" style="padding-top: 0;">Sample
            Information</th>
        </tr>
        <tr>
          <th>
            Anticipated empirical formula:
          </th>
          <td>
            <rn:sampleField id="formula"
                fieldCode="<%=SampleTextBL.EMPIRICAL_FORMULA%>" />

            <rn:overrideValidationButton sampleField="${formula}"
                label="Override" id="overrideButton" />
            <ctl:errorMessage errorSupplier="${overrideButton}"
                    errorFilter="<%=
                        ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
              <span class="notice">(validation overridden)</span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="${formula}"
                    errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
              <div class="errorNotice">
                The entered empirical formula is invalid.
              </div>
              <div class="notice">
                Correct the formula and click "Save" to resubmit, or click
                "Override" to record the invalid formula.
              </div>
              <div class="notice">
                WARNING: invalid formulae are not searchable.
              </div>
            </ctl:errorMessage>
          </td>
        </tr>
        <rn:ltaIterator>
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
          <td class="formButtons" colspan="2">
            <rn:wapSaveButton />
            <rn:sampleChecker includeIfNewSample="true" invert="true">
              <rn:wapCancelButton />
            </rn:sampleChecker>
            <rn:sampleChecker includeIfNewSample="true">
              <rn:authorizationChecker canSeeLabSummary="true"
                      suppressRenderingOnly="true">
                <ctl:button label="Cancel" id="cancelToSummary" />
                <ctl:redirect target="/lab/summary.jsp"
                     condition="${cancelToSummary.valueAsBoolean}" />
              </rn:authorizationChecker>
              <rn:authorizationChecker canSeeLabSummary="true" invert="true"
                      suppressRenderingOnly="true">
                <ctl:button label="Cancel" id="cancelToIndex" />
                <ctl:redirect target="/index.jsp"
                    condition="${cancelToIndex.valueAsBoolean}" />
              </rn:authorizationChecker>
            </rn:sampleChecker>
          </td>
        </tr>
      </table>
    </ctl:selfForm>
  </div>
  <ctl:styleBlock>
    .errorMessageSmall { color: #F00000; font-size: small; margin-left: 4em; }
    .errorMessageExtraSmall { font-family: sans-serif; font-size: x-small;
        color: #909090; text-align: left; margin-left: 4em; }
    .briefComment { font-size: x-small; color: #9E9E9E; text-align: left; }
    .darkText { font-weight: bold; color: #000050; }
  </ctl:styleBlock>
</rn:wapPage>
