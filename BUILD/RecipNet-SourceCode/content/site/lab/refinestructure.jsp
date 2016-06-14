<%--
  - Reciprocal Net project
  - refinestructure.jsp
  -
  - 26-Jul-2002: eisiorho wrote first draft
  - 06-Aug-2002: eisiorho fixed bugs #258, #260
  - 07-Aug-2002: eisiorho fixed bugs #281
  - 20-Aug-2002: eisiorho added fields a, b, c, alpha, beta, gamma.
  - 29-Aug-2002: leqian added localtracking support
  - 29-Aug-2002: leqian fixed bug #402
  - 10-Sep-2002: eisiorho added appropriate measurement unit for "scientific
  -              data"
  - 18-Sep-2002: ekoperda fixed bug #457 (maxlength of textbox for IUPAC name)
  - 18-Sep-2002: ekoperda fixed bug #462 (incorrect symbol for angstroms)
  - 21-Feb-2003: yli renamed action link solve.jsp to refinestructure.jsp
  - 18-Mar-2003: yli changed decimal precision on fields a, b, c, alpha, beta
  -              and gamma
  - 05-Jun-2003: dfeng added multi-value support for tradeName and commonName
  - 10-Jul-2003: dfeng added correctability support
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 19-Aug-2004: cwestnea rewrote using custom tags
  - 24-Aug-2004: midurbin made WapPage's editSamplePageHref relative to the
  -              context path
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 14-Jan-2004: jobollin added <ctl:selfForm> elements
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 19-Aug-2005: midurbin added a button to overrule validation errors for the
  -              Space Group field
  - 11-Nov-2005: midurbin added formula validation and overruling
  - 24-Feb-2006: jobollin added support for synchronization with CIF and
  -              rearranged paged contents
  - 28-Apr-2006: jobollin added tolerant handling of invalid CIFs
  - 09-Jan-2008: ekoperda rearranged tags to fix bug #1857 during CIF sync
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleField" %>
<%@ page import="org.recipnet.site.content.rncontrols.CifFileContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.ValidationOverrideButton"
 %>
<%@ page import="org.recipnet.common.controls.HtmlPage" %>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ page import="org.recipnet.site.shared.db.SampleDataInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Record Refinement Results"
    workflowActionCode="<%= SampleWorkflowBL.STRUCTURE_REFINED %>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.STRUCTURE_REFINED_CORRECTED %>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <p class="pageInstructions">
    Enter the structure refinement data on the form below and click
    the "Save" button to record it.
    <ctl:errorMessage errorSupplier="${htmlPage}" errorFilter="<%=
      HtmlPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>"><br/>
      <span class="errorMessage"
            style="font-weight: normal; font-style: italic;">
        You must address the flagged validation errors before the data
        will be accepted.
      </span>
    </ctl:errorMessage>
  </p>
  <ctl:selfForm>
    <rn:filenames>
      <rn:cifChooser id="cifChooser"/>
      <rn:file fileName="${cifChooser.cifName}">
        <rn:cifFile id="cif"/>
      </rn:file>
    </rn:filenames>
    <table class="bodyTable">
      <tr>
        <th class="leadSectionHead" colspan="8">General Information</th>
      </tr>
      <tr>
        <th colspan="2">
          <rn:sampleFieldLabel
                  fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
        </th>
        <td colspan="6">
          <rn:sampleField displayAsLabel="true" fieldCode="<%=
              SampleInfo.LOCAL_LAB_ID %>" />
        </td>
      </tr>
      <tr>
        <th colspan="2">Originating Lab:</th>
        <td colspan="6"><rn:labField /></td>
      </tr>
      <tr>
        <th colspan="2">Originating Provider:</th>
        <td colspan="6">
          <rn:providerField />
        </td>
      </tr>
      <tr>
        <th style="vertical-align: top;" colspan="2">
          <rn:sampleFieldLabel
                  fieldCode="<%=SampleDataInfo.SUMMARY_FIELD%>" />:
        </th>
        <td colspan="6">
          <rn:sampleField
                  fieldCode="<%=SampleDataInfo.SUMMARY_FIELD%>" />
        </td>
      </tr>
      <rn:cifSampleFilter cif="${cif.cifFile}" fixShelxCifs="true"
          enabled="${loadFromCif.valueAsBoolean
          || loadFromBuggyCif.valueAsBoolean}">
        <tr>
          <th colspan="8" class="sectionHead" style="padding-top: 0.5em">
            Crystal Data
          </th>
        </tr>
        <tr>
          <th colspan="2">
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.A_FIELD%>" />:
          </th>
          <td colspan="2"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.A_FIELD %>"
                id="cellA">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.A_FIELD%>" />
          </td>
          <th class="nonLeadColumn">
            <rn:sampleFieldLabel
                    fieldCode="<%=SampleDataInfo.ALPHA_FIELD%>" />:
          </th>
          <td colspan="3"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.ALPHA_FIELD %>" 
                id="cellAlpha">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.ALPHA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th colspan="2">
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.B_FIELD%>" />:
          </th>
          <td colspan="2"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.B_FIELD %>"
                id="cellB">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.B_FIELD%>" />
          </td>
          <th class="nonLeadColumn">
            <rn:sampleFieldLabel 
                fieldCode="<%=SampleDataInfo.BETA_FIELD%>" />:
          </th>
          <td colspan="3"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.BETA_FIELD %>" 
                id="cellBeta">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.BETA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th colspan="2">
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.C_FIELD%>" />:
          </th>
          <td colspan="2"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.C_FIELD %>"
                id="cellC">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.C_FIELD%>" />
          </td>
          <th>
            <rn:sampleFieldLabel 
                    fieldCode="<%=SampleDataInfo.GAMMA_FIELD%>" />:
          </th>
          <td colspan="3"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.GAMMA_FIELD %>" 
                id="cellGamma">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.GAMMA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th colspan="2">
            <rn:sampleFieldLabel
                fieldCode="<%=SampleDataInfo.SPGP_FIELD%>" />:
          </th>
          <td colspan="2">
            <rn:sampleField id="spaceGroup"
                fieldCode="<%=SampleDataInfo.SPGP_FIELD%>" />
          </td>
          <th>
            <rn:sampleFieldLabel
                fieldCode="<%=SampleDataInfo.Z_FIELD%>" />:
          </th>
          <td colspan="3">
            <rn:sampleField
                fieldCode="<%=SampleDataInfo.Z_FIELD%>" />
          </td>
        </tr>
        <tr>
          <td colspan="2"></td>
          <td colspan="6">
            <ctl:errorMessage errorSupplier="${spaceGroup}"
                errorFilter="<%=SampleField.VALIDATOR_REJECTED_VALUE%>">
              <div class="errorNotice" style="max-width: 40em;">
                The entered space group symbol is invalid: either it
                does not represent a valid space group or its format is
                incorrect or unsupported.  (Hermann-Mauguin symbols in
                CIF format (preferred), SHELX format, and some variations
                are supported.)
              </div>
              <div class="notice" style="max-width: 40em;">
                Correct the symbol and click "Save" to resubmit, or click
                "Override" to record the invalid symbol.
              </div>
              <div class="notice" style="max-width: 40em;">
                WARNING: invalid space group symbols are not searchable.
              </div>
            </ctl:errorMessage>
            <rn:overrideValidationButton sampleField="${spaceGroup}"
                label="Override" id="overrideButton" />
            <ctl:errorChecker errorSupplier="${overrideButton}"
                    errorFilter="<%=
                      ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
              <span class="notice">(validation overridden)</span>
            </ctl:errorChecker>
          </td>
        </tr>
        <tr>
          <th colspan="8" class="sectionHead">
            Agreement Statistics
          </th>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.RF_FIELD%>" />:
          </th>
          <td>
            <rn:sampleField fieldCode="<%=SampleDataInfo.RF_FIELD%>" />
          </td>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.RF2_FIELD%>"/>:
          </th>
          <td>
            <rn:sampleField fieldCode="<%=SampleDataInfo.RF2_FIELD%>" />
          </td>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.RWF_FIELD%>"/>:
          </th>
          <td>
            <rn:sampleField fieldCode="<%=SampleDataInfo.RWF_FIELD%>" />
          </td>
          <th>
            <rn:sampleFieldLabel 
                fieldCode="<%=SampleDataInfo.RWF2_FIELD%>" />:
          </th>
          <td>
            <rn:sampleField fieldCode="<%=SampleDataInfo.RWF2_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th colspan="3">
            <rn:sampleFieldLabel
                fieldCode="<%=SampleDataInfo.GOOF_FIELD%>" />:
          </th>
          <td colspan="5">
            <rn:sampleField
                fieldCode="<%=SampleDataInfo.GOOF_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th colspan="8" class="sectionHead">Names and Formulae</th>
        </tr>
        <tr>
          <td colspan="4" style="vertical-align: top;">
            <table cellspacing="0">
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.EMPIRICAL_FORMULA%>" />:
                </th>
                <td>
                  <rn:sampleField id="empForm" fieldCode="<%=
                      SampleTextBL.EMPIRICAL_FORMULA %>" />
                  <ctl:errorMessage errorSupplier="${empForm}"
                          errorFilter="<%=
                          SampleField.VALIDATOR_REJECTED_VALUE%>">
                    <div class="errorNotice" style="max-width: 20em;">
                      The entered empirical formula is invalid.
                    </div>
                    <div class="notice" style="max-width: 20em;">
                      Correct the formula and click "Save" to resubmit, or
                      click "Override" to record the invalid formula.
                    </div>
                    <div class="notice" style="max-width: 20em;">
                      WARNING: invalid formulae may not be searchable.
                    </div>
                  </ctl:errorMessage>
                  <rn:overrideValidationButton sampleField="${empForm}"
                      label="Override" id="overrideEFButton" />
                  <ctl:errorMessage errorSupplier="${overrideEFButton}"
                      errorFilter="<%=
                      ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
                    <span class="notice">(validation overridden)</span>
                  </ctl:errorMessage>
                </td>
              </tr>
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                          fieldCode="<%=SampleTextBL.MOIETY_FORMULA%>" />:
                </th>
                <td>
                  <rn:sampleField id="moiety" fieldCode="<%=
                      SampleTextBL.MOIETY_FORMULA %>" />
                  <ctl:errorMessage errorSupplier="${moiety}"
                      errorFilter="<%=
                      SampleField.VALIDATOR_REJECTED_VALUE%>">
                    <div class="errorNotice" style="max-width: 20em;">
                      The entered moiety formula is invalid.
                    </div>
                    <div class="notice" style="max-width: 20em;">
                      Correct the formula and click "Save" to resubmit, or
                      click "Override" to record the invalid formula.
                    </div>
                    <div class="notice" style="max-width: 20em;">
                      WARNING: invalid formulae may not be searchable.
                    </div>
                  </ctl:errorMessage>
                  <rn:overrideValidationButton sampleField="${moiety}"
                      label="Override" id="overrideMoietyButton" />
                  <ctl:errorMessage errorSupplier="${overrideMoietyButton}"
                       errorFilter="<%=
                       ValidationOverrideButton.VALIDATION_OVERRIDDEN%>" >
                    <span class="notice">(validation overridden)</span>
                  </ctl:errorMessage>
                </td>
              </tr>
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.STRUCTURAL_FORMULA%>" />:
                </th>
                <td>
                  <rn:sampleField fieldCode="<%=
                      SampleTextBL.STRUCTURAL_FORMULA %>" />
                </td>
              </tr>
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.SMILES_FORMULA%>" />:
                </th>
                <td>
                  <rn:sampleField fieldCode="<%=
                      SampleTextBL.SMILES_FORMULA %>" />
                </td>
              </tr>
            </table>
          </td>
          <td colspan="4" style="vertical-align: top">
            <table cellspacing="0">
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.TRADE_NAME%>" />:
                </th>
                <td>
                  <rn:sampleField 
                      fieldCode="<%= SampleTextBL.TRADE_NAME %>" />
                </td>
              </tr>
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.COMMON_NAME%>" />:
                </th>
                <td>
                  <rn:sampleField 
                      fieldCode="<%= SampleTextBL.COMMON_NAME %>" />
                </td>
              </tr>
              <tr>
                <th class="multiboxLabel">
                  <rn:sampleFieldLabel
                      fieldCode="<%=SampleTextBL.IUPAC_NAME%>" />:
                </th>
                <td>
                  <rn:sampleField 
                      fieldCode="<%= SampleTextBL.IUPAC_NAME %>" />
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td colspan="8" class="actionButton">
            <ctl:errorChecker errorSupplier="${cif}" invert="true">
              <ctl:button id="loadFromCif" suppressInsteadOfSkip="true"
                  label="Load crystal data from ${cifChooser.cifName}">
                <ctl:extraHtmlAttribute name="tabindex" value="5"/>
              </ctl:button>
            </ctl:errorChecker>
            <ctl:errorChecker errorSupplier="${cif}"
                errorFilter="<%=CifFileContext.CIF_HAS_ERRORS %>">
              <ctl:button id="loadFromBuggyCif" suppressInsteadOfSkip="true"
                  label="Load crystal data from ${cifChooser.cifName}">
                <ctl:extraHtmlAttribute name="tabindex" value="5"/>
              </ctl:button><br/>
              <span class="errorNotice">Warning: CIF '${cifChooser.cifName}'
                  contains errors</span>
            </ctl:errorChecker>
            <ctl:errorChecker errorSupplier="${cif}">
              <ctl:errorChecker errorSupplier="${cif}" invert="true"
                  errorFilter="<%=CifFileContext.CIF_HAS_ERRORS %>">
                <ctl:button editable="false"
                    label="Load crystal data from CIF"/><br/>
              </ctl:errorChecker>
              <ctl:errorChecker errorSupplier="${cif}"
                  errorFilter="<%=CifFileContext.UNPARSEABLE_CIF %>">
                <span class="errorNotice">
                  Warning: CIF '${cifChooser.cifName}' could not be parsed
                </span>
              </ctl:errorChecker>
              <ctl:errorChecker errorSupplier="${cif}"
                  errorFilter="<%=CifFileContext.EMPTY_CIF %>">
                <span class="errorNotice">
                  Warning: CIF '${cifChooser.cifName}' contains no data
                  blocks
                </span>
              </ctl:errorChecker>
            </ctl:errorChecker>
          </td>
        </tr>
      </rn:cifSampleFilter>
      <rn:ltaIterator id="ltas">
        <ctl:errorChecker errorFilter="<%= HtmlPageIterator.NO_ITERATIONS %>"
            invert="true">
          ${ltas.currentIterationFirst ?
            '<tr>
              <th class="sectionHead" colspan="8">Additional Information</th>
            </tr>' : ''}
        </ctl:errorChecker>
        <tr>
          <th colspan="2">
            <rn:sampleFieldLabel />:
          </th>
          <td colspan="6">
            <rn:sampleField /><rn:sampleFieldUnits />
          </td>
        </tr>
      </rn:ltaIterator>
      <tr>
        <th class="sectionHead" colspan="8">Refinement Comments</th>
      </tr>
      <tr>
        <td colspan="8" style="text-align: center">
          <rn:wapComments />
        </td>
      </tr>
      <tr>
        <td colspan="8" class="formButtons">
          <rn:wapSaveButton /> <rn:wapCancelButton />
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  <script type="text/javascript">
    <!-- // hide from ancient browsers
    
    // A function that sets the values of cellAlpha, cellBeta and cellGamma to
    // 90 if none of them were previously set and at least one of cellA, cellB
    // and cellC has been set.
    function setVals() {
        if (!(getElement("cellA").value == ""
                && getElement("cellB").value == ""
                && getElement("cellC").value == "")) {
            var cellAlpha = getElement("cellAlpha");
            var cellBeta = getElement("cellBeta");
            var cellGamma = getElement("cellGamma");
            if (cellAlpha.value == "" && cellBeta.value == ""
                    && cellGamma.value == "") {
                cellAlpha.value = "90.00";
                cellBeta.value = "90.00";
                cellGamma.value = "90.00";
            }
        }
    }

    // A function that finds the first element that starts with the provided
    // name.  This is useful because the HTML form element for an
    // HtmlPageElement is guaranteed to have a name that starts with the id
    // value assigned to that element.
    function getElement(name) {
        var index = 0;
        while (index < document.forms[1].elements.length) {
            if (document.forms[1].elements[index].name.indexOf(name) == 0) {
                return document.forms[1].elements[index];
            } else {
                index ++;
            }
        }
    } 

    // stop hiding -->
  </script>
  </div>
</rn:wapPage>
