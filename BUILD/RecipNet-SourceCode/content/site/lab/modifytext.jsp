<%--
  -- Reciprocal Net project
  -- modifytext.jsp
  --
  -- 28-Jul-2002: eisiorho wrote first draft
  -- 29-Aug-2002: leqian added localtracking support
  -- 05-Sep-2002: eisiorho modified the option menu to include new attributes
  -- 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  -- 17-Oct-2002: ekoperda fixed bug #546 that sometimes prevented all
  --              localtracking attributes from appearing in the dropdown box
  --              at the top of the page, also reorganized page code
  -- 12-Nov-2002: eisiorho fixed bug #599
  -- 14-Nov-2002: eisiorho removed supporting code for Ltas for this page.
  -- 25-Feb-2003: eisiorho fixed bug #747 by avoiding references to
  --              localtracking fields
  -- 19-Mar-2003: nsanghvi fixed UI alignment
  -- 24-Mar-2003: midurbin improved validation to catch fields containing
  --              only whitespace
  -- 02-Apr-2003: midurbin fixed bug #839
  -- 23-Apr-2003: adharurk changed parameters of 
  --              PageHelper.modifySampleTextFields(),
  --              PageHelper.deleteTextInfo() and PageHelper.addTextInfo()
  -- 29-May-2003: adharurk added call to WorkflowHelper.getEligibleFieldCodes()
  -- 01-Jul-2003: dfeng added support to detect html tags within forms
  -- 15-Jul-2003: dfeng fixed bug #956 in UI
  -- 07-Jan-2004: ekoperda changed package references due to source tree
  --              reorganization
  -- 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
  -- 28-Jan-2005: midurbin rewrote using custom tags
  -- 13-Jul-2005: midurbin added new required parameters to
  --              extendedOperationWapPage
  -- 26-Jul-2005: midurbin updated SampleFields to reflect property name change
  -- 26-Jul-2005: midurbin added 'reevaluatePage' property to those buttons
  --              where 'saveToPersistedOp' was true
  -- 05-Aug-2005: midurbin added SampleFieldUnits tags
  -- 11-Nov-2005: midurbin added support for field validation and overruling of
  --              field validation
  -- 17-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.common.controls.ValidationSupressor" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ValidationOverrideButton"
 %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleTextInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:extendedOpWapPage title="Modify Text Fields"
        workflowActionCode="<%= SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>"
        workflowActionCodeCorrected="<%=
          SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>"
        editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <ctl:suppressValidation id="validation" enabled="${ !save.valueAsBoolean }">
  <div class="pageBody">
  <p class="pageInstructions">
    Add, delete, or modify metadata fields then click 'Save' to
    save your changes. Fields may be deleted using their 'delete' button
    or by erasing all their text.
  </p><p class="pageInstructions" style="font-style: italic;">
    No changes applied via this page are permanent until you click the 'Save'
    button.
    <ctl:errorMessage
      errorFilter="<%= ValidationSupressor.VALIDATION_ERROR_REPORTED %>"><br/>
      <span class="errorMessage"
            style="font-weight: normal; font-style: italic;">
        You must address the flagged validation errors before the changes will
        be recorded.
      </span>
    </ctl:errorMessage>
  </p>
  <ctl:selfForm>
    <table class="bodyTable" cellspacing="0">
      <tr>
        <th colspan="3" class="tableTitle">Available text modifications</th>
      </tr>
      <tr>
        <th colspan="3" class="columnLabel">Add a new field:</th>
      </tr>
      <rn:sampleUpdateBlocker enabled="${ !save.valueAsBoolean }">
      <ctl:iterator id="newFieldIterator" iterations="1">
      <tr class="${rn:testParity(
                      newFieldIterator.iterationCountSinceThisPhaseBegan,
                      'evenRow', 'oddRow')}">
        <td>
          <rn:textTypeSelector id="sel" includeFieldsForAction="<%=
                  SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>"/>
        </td>
        <rn:undecidedTextContext textTypeSelector="${sel}">
          <td>
            <rn:sampleField id="newText"
                required="${sel.textType != SampleTextInfo.INVALID_TYPE}"
                useTextareaInsteadOfTextboxForUnknownCode="true"
                width="64" height="2"/>
            <rn:sampleFieldUnits />
            <rn:overrideValidationButton sampleField="${newText}"
                label="override" id="saveOverrideNew"
                saveToPersistedOp="true"/>
            <ctl:errorMessage errorSupplier="${saveOverrideNew}"
                errorFilter="<%=
                    ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
              <span class="notice">(validation overridden)</span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="${newText}"
                errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
              <div class="errorNotice">
                The entered value is invalid.
              </div>
              <div class="notice">
                Correct the value and click "add another" to try to add it
                again, click "Save" to attempt to save the corrected value in
                place, or click "override" to override the validation and
                save.
              </div>
            </ctl:errorMessage>
          </td>
          <td>
            <ctl:parityChecker includeOnOnlyIteration="true" invert="true">
              <ctl:deleteIterationButton/>
            </ctl:parityChecker>
          </td>
        </rn:undecidedTextContext>
      </tr>
      <ctl:errorChecker errorSupplier="${newText}" errorFilter="<%=
                    SampleField.REQUIRED_VALUE_IS_MISSING
                    | SampleField.VALUE_ENTERED_FOR_NULL_CONTEXT%>">
        <tr class="${rn:testParity(
                        newFieldIterator.iterationCountSinceThisPhaseBegan,
                        'evenRow', 'oddRow')}">
          <td colspan="3" style="text-align: center;">
            <ctl:errorMessage errorSupplier="${newText}" errorFilter="<%=
                    SampleField.REQUIRED_VALUE_IS_MISSING%>">
              <span class="errorMessage">When you select a field type, you must
                enter a value.</span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="${newText}" errorFilter="<%=
                    SampleField.VALUE_ENTERED_FOR_NULL_CONTEXT%>">
              <span class="errorMessage">You must select a field type to record
                a value.</span>
            </ctl:errorMessage>
          </td>
        </tr>
      </ctl:errorChecker>
      </ctl:iterator>
      </rn:sampleUpdateBlocker>
      <tr class="${rn:testParity(newFieldIterator.iterations,
                                 'evenRow', 'oddRow')}">
        <td colspan="3" style="text-align:center">
          <ctl:addIterationButton iterator="${newFieldIterator}"/>
        </td>
      </tr>
      <tr>
        <th colspan="3" class="columnLabel">Modify attributes of this sample:</th>
      </tr>
      <rn:sampleTextIterator restrictToAttributes="true" id="attributes"
          sortByTextTypeName="true" restrictByWorkflowAction="<%=
             SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>">
        <tr class="${rn:testParity(attributes.iterationCountSinceThisPhaseBegan,
                                   "evenRow", "oddRow")}">
          <th><rn:sampleFieldLabel />:</th>
          <td>
            <rn:sampleField id="attrSampleField" />
            <rn:sampleFieldUnits />
            <rn:overrideValidationButton sampleField="<%=attrSampleField%>"
                label="override" id="saveOverrideAttr" 
                saveToPersistedOp="true" />
            <ctl:errorMessage errorSupplier="<%=saveOverrideAttr%>"
                errorFilter="<%=
                        ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
              <span class="notice">(validation overridden)</span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=attrSampleField%>"
                errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
              <div class="errorNotice">
                The entered value is invalid.
              </div>
              <div class="notice">
                Correct the value and click "Save" to resubmit, or click
                "override" to record the invalid value.
              </div>
            </ctl:errorMessage>
          </td>
          <td>
            <rn:deleteSampleTextInfoButton saveToPersistedOp="true"
                    reevaluatePage="true" label="Delete"
                    suppressIfNotCorrectionMode="false" />
          </td>
        </tr>
      </rn:sampleTextIterator>
      <ctl:errorMessage errorSupplier="<%=attributes%>">
        <tr class="evenRow">
          <td colspan="3" class="notice" style="text-align: center;">
            There are currently no attributes.
          </td>
        </tr>
      </ctl:errorMessage>
      <tr>
        <th colspan="3" class="columnLabel">Modify annotations of this sample:</th>
      </tr>
      <rn:sampleTextIterator restrictToAnnotations="true" id="annotations"
          sortByTextTypeName="true" restrictByWorkflowAction="<%=
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>">
        <tr class="${
            rn:testParity(annotations.iterationCountSinceThisPhaseBegan,
                          "evenRow", "oddRow")}">
          <th><rn:sampleFieldLabel />:</th>
          <td>
            <rn:sampleField id="annoSampleField"
                overrideSpecificValidationUnlessParamNameMatches=
                        ".*(save){1,}?.*(_persist){0}?.*"
                useTextareaInsteadOfTextboxForUnknownCode="true" 
                width="64" height="2"/>
            <rn:sampleFieldUnits />
            <rn:overrideValidationButton sampleField="<%=annoSampleField%>"
                label="override" id="saveOverrideAnno"
                saveToPersistedOp="true" />
            <ctl:errorMessage errorSupplier="<%=saveOverrideAnno%>"
                    errorFilter="<%=
                        ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
              <span class="notice">(validation overridden)</span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=annoSampleField%>"
                    errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
              <div class="errorNotice">
                The entered value is invalid!
              </div>
              <div class="notice">
                Correct the value and click "Save" to resubmit, or click
                "override" to record the invalid value.
              </div>
            </ctl:errorMessage>
          </td>
          <td>
            <rn:deleteSampleTextInfoButton saveToPersistedOp="true"
                    reevaluatePage="true" label="Delete"
                    suppressIfNotCorrectionMode="false" />
          </td>
        </tr>
      </rn:sampleTextIterator>
      <ctl:errorMessage errorSupplier="<%=annotations%>">
        <tr class="evenRow">
          <td colspan="3" class="notice" style="text-align: center;">
            There are currently no annotations.
          </td>
        </tr>
      </ctl:errorMessage>
    </table>
    <div style="margin-top: 1em; margin-bottom: 1em; text-align: center;">
      <rn:wapSaveButton id="save" />
      <rn:wapCancelButton />
    </div>
  </ctl:selfForm>
  <ctl:styleBlock>
    table.bodyTable { border: 3px solid #32357D; }
    table.bodyTable th,
    table.bodyTable td { vertical-align: top; padding: 0.25em; }
    table.bodyTable th.tableTitle { background: #32357D; color: #FFFFFF;
        font-weight: bold; text-align: left; }
    table.bodyTable th.columnLabel { background: #656BFA; color: white;
        font-style: italic; font-weight: normal; text-align: left; }
    tr.oddRow { background: #F0F0F0; color: #000050; }
    tr.evenRow { background: #D0D0D0; color: #000050; }
  </ctl:styleBlock>
  </div>
  </ctl:suppressValidation>
</rn:extendedOpWapPage>

