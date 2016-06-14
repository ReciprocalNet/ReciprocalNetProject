<%--
  - Reciprocal Net project
  -
  - retract.jsp
  -
  - 26-Jul-2002: eisiorho wrote first draft
  - 13-Sep-2002: jobollin fixed line delimiters (Task 433)
  - 20-Feb-2003: adharurk renamed the file as retract.jsp with functionality
  -              and UI change
  - 10-Mar-2003: adharurk added local tracking support to the page
  - 23-Apr-2003: adharurk changed parameters of PageHelper.retractSample()
  - 26-Jun-2003: dfeng replace include file common.inc with common.jspf
  - 01-Jul-2003: dfeng added support to detect html tags in forms
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
  - 05-Oct-2004: midurbin rewrote page to use custom tags
  - 14-Dec-2004: eisiorho changed textype references to use new class
  -              SampleTextBL
  - 14-Jan-2005: jobollin added <ctl:selfForm> elements
  - 22-Jun-2005: midurbin made changes to reflect quickSearchByLabParam name
  -              change
  - 13-Jul-2005: midurbin added new required parameters to retractWapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 02-Feb-2006: jobollin updated the page to accommodate the changes to
  -              radioButton
  - 13-Mar-2006: jobollin updated the page to use the new styles
  --%>
<%@ page session="true" isThreadSafe="true"
        info="Retract Sample"
        import="org.recipnet.site.content.rncontrols.RetractWapPage"
        import="org.recipnet.common.controls.HtmlPageIterator"
        import="org.recipnet.site.content.rncontrols.SampleField"
        import="org.recipnet.site.content.rncontrols.SampleSelector"
        import="org.recipnet.site.shared.bl.SampleTextBL"
        import="org.recipnet.site.shared.bl.SampleWorkflowBL"
        import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:retractWapPage title="Retract"
        workflowActionCode="<%=SampleWorkflowBL.RETRACTED%>"
        workflowActionCodeCorrected="<%=SampleWorkflowBL.RETRACTED%>"
        editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
    <p class="pageInstructions">
        You are about to retract this sample from Reciprocal Net. You may
        optionally indicate another sample from the same lab
        (<rn:labField />) that supersedes the one you are about to retract
        (<rn:sampleField displayAsLabel="true"
                fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />).
      <ctl:errorMessage errorFilter="<%=
          RetractWapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR%>"><br />
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
          <th class="leadSectionHead" colspan="2">Sample to Retract</th>
        </tr>
        <tr>
          <th style="white-space: nowrap;">Laboratory:</th>
          <td><rn:labField /></td>
        </tr>
        <tr>
          <th style="white-space: nowrap;">Local sample ID:</th>
          <td><rn:sampleField displayAsLabel="true"
                  fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" /></td>
        </tr>
        <tr>
          <th class="sectionHead" colspan="2">Superseding Sample</th>
        </tr>
        <ctl:radioButtonGroup initialValue="false">
        <tr>
          <td colspan="2">
            <div id="option1" class="optionDiv">
              <ctl:radioButton id="noSampleSupersedes" option="false">
                <ctl:extraHtmlAttribute name="onClick"
                    value="selectMainOption('noSampleSupersedes')" />
              </ctl:radioButton> No sample supersedes sample
              <rn:sampleField displayAsLabel="true"
                  fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"/>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <div id="option2" class="optionDiv">
              <div style="margin-bottom: 0.5em;">
                <ctl:radioButton id="sampleSupersedes" option="true">
                  <ctl:extraHtmlAttribute name="onClick"
                        value="selectMainOption('sampleSupersedes')" />
                </ctl:radioButton>
                This sample supersedes sample
                <rn:sampleField displayAsLabel="true"
                    fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
              </div>
              <div style="padding-left: 2em;">
              <span style="font-weight: bold; white-space: nowrap;"><rn:labField
                /> Local Sample ID:</span>
              <rn:sampleSelector id="sampleSelector"
                required="${sampleSupersedes.valueAsBoolean}"
                prohibited="${noSampleSupersedes.valueAsBoolean}" />
              <ctl:a id="browseSamples" href="/search.jsp" renderId="true"
                openInWindow="search" onClick="isBrowseEnabled()">browse all...
                <rn:labParam name="quickSearchByLab"/></ctl:a>
              <ctl:errorMessage errorSupplier="<%=sampleSelector%>"
                errorFilter="<%= SampleSelector.PROHIBITED_VALUE_IS_PRESENT%>">
                <br/><span class="errorMessage">You may not specify a
                superseding sample without indicating that you in fact wish to
                supersede the existing sample.</span>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=sampleSelector%>"
                errorFilter="<%= SampleSelector.REQUIRED_VALUE_IS_MISSING%>">
                <br/><span class="errorMessage">You must specify a superseding 
                    sample when indicating that you wish to supersede the
                    existing sample.</span>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=sampleSelector%>"
                errorFilter="<%= SampleSelector.INVALID_LOCALLABID%>">
                <span class="errorMessage">Unknown sample</span><br/>
              </ctl:errorMessage>
              <ctl:errorMessage errorFilter="<%=
                      RetractWapPage.CANNOT_SUPERSEDE_WITH_RETRACTED_SAMPLE%>">
                <br/><span class="errorMessage">You may not supersede a
                  retracted sample with another retracted sample.</span>
              </ctl:errorMessage>
              <table>
                <tr>
                  <th>Note for the superseding sample (specified
                    above):</th>
                  <th style="padding-left: 2em;">Note for the superseded sample
                    (<rn:labField /> sample
                    <rn:sampleField displayAsLabel="true"
                      fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />):</th>
                </tr>
                <tr>
                  <rn:undecidedSampleContext
                    subjectSampleSelector="<%=sampleSelector%>"
                    annotationType="<%=
                        SampleTextBL.SUPERSEDES_ANOTHER_SAMPLE%>"
                    registerWithRetractWapPage="true">
                    <td><rn:sampleField id="supersedingNotes"
                      required="${sampleSupersedes.valueAsBoolean}"
                      prohibited="${noSampleSupersedes.valueAsBoolean}"
                      ownedElementId="supersedingNotesArea" />
                      <ctl:errorMessage errorSupplier="<%=supersedingNotes%>"
                        errorFilter="<%=
                            SampleField.PROHIBITED_VALUE_IS_PRESENT%>">
                        <div class="errorMessage">
                          These notes may not be specified unless you indicate
                          that you wish to supersede the sample with another
                          sample.
                        </div>
                      </ctl:errorMessage>
                      <ctl:errorMessage errorSupplier="<%=supersedingNotes%>"
                           errorFilter="<%=
                               SampleField.REQUIRED_VALUE_IS_MISSING%>">
                        <div class="errorMessage">
                          These notes must be specified if you wish to supersede
                          the sample.
                        </div>
                      </ctl:errorMessage>
                    </td>
                  </rn:undecidedSampleContext>  
                  <rn:undecidedSampleContext
                    annotationType="<%=
                        SampleTextBL.SUPERSEDED_BY_ANOTHER_SAMPLE%>"
                    referenceSampleSelector="<%=sampleSelector%>">
                    <td style="padding-left: 2em">
                      <rn:sampleField id="supersededNotes"
                        prohibited="${noSampleSupersedes.valueAsBoolean}"
                        required="${sampleSupersedes.valueAsBoolean}"
                        ownedElementId="supersededNotesArea"/>
                      <ctl:errorMessage errorSupplier="<%=supersededNotes%>"
                        errorFilter="<%=
                            SampleField.REQUIRED_VALUE_IS_MISSING%>">
                        <div class="errorMessage">
                          These notes must be specified if you wish to supersede
                          the sample.
                        </div>
                      </ctl:errorMessage>
                      <ctl:errorMessage errorSupplier="<%=supersededNotes%>"
                        errorFilter="<%=
                            SampleField.PROHIBITED_VALUE_IS_PRESENT%>">
                        <div class="errorMessage">
                          These notes may not be specified unless you indicate
                          that you wish to supersede the sample with another
                          sample.
                        </div>
                      </ctl:errorMessage>
                    </td>
                  </rn:undecidedSampleContext>  
                </tr>
              </table>
              </div>
            </div>
          </td>
        </tr>
        </ctl:radioButtonGroup>
        <rn:ltaIterator id="ltas">
          <ctl:errorChecker errorFilter="<%= HtmlPageIterator.NO_ITERATIONS %>"
            invert="true">
            ${ltas.currentIterationFirst ?
              '<tr>
                <th class="sectionHead"
                  colspan="3">Additional Sample Information</th>
              </tr>' : ''}
          </ctl:errorChecker>
          <tr>
            <th style="white-space: nowrap; width:1px; max-width: 1px;">
              <rn:sampleFieldLabel />:
            </th>
            <td colspan="2">
              <rn:sampleField /><rn:sampleFieldUnits />
            </td>
          </tr>
        </rn:ltaIterator>
        <tr>
          <th colspan="3" class="sectionHead">Comments</th>
        </tr>
        <tr>
          <td style="text-align: center;" colspan="3">
            <rn:wapComments />
          </td>
        </tr>
        <tr>
          <td class="formButtons" colspan="3">
            <rn:wapSaveButton />
            <rn:wapCancelButton />
          </td>
        </tr>
      </table>
    </ctl:selfForm>
  </div>

  <script type="text/Javascript">
  var mainOptions = new Array("sampleSupersedes", "noSampleSupersedes")
  var optionChildren = new Array()
  var optionPeers = new Array()
  var optionRegions = new Array()
  var mainOption;

  optionPeers["noSampleSupersedes"] = new Array("sampleSupersedes")
  optionPeers["sampleSupersedes"] = new Array("noSampleSupersedes")
  optionRegions["noSampleSupersedes"] = "option1"
  optionRegions["sampleSupersedes"] = "option2"
  optionChildren["noSampleSupersedes"] = new Array()
  optionChildren["sampleSupersedes"] = new Array(
      document.getElementById("sampleSelector"),
      document.getElementById("browseSamples"),
      document.getElementById("supersedingNotesArea"),
      document.getElementById("supersededNotesArea"))

  selectMainOption("noSampleSupersedes")

  function selectMainOption(option) {
    for (var index = 0; index < mainOptions.length; index++) {
      var optionName = mainOptions[index]
      var div = document.getElementById(optionRegions[optionName])

      if (optionName == option) {
        mainOption = option
        div.style.background="white"
        disableChildren(optionName, false)
      } else {
        div.style.background="silver"
        disableChildren(optionName, true)
      }
    }
  }

  function disableChildren(optionName, disabled) {
    var children = optionChildren[optionName]

    for (var childIndex = 0; childIndex < children.length; childIndex++) {
      children[childIndex].disabled = disabled
    }
  }

  function isBrowseEnabled() {
    return (mainOption == "sampleSupersedes")
  }

  </script>
  <ctl:styleBlock>
    div.optionDiv { border: 1px solid #32357D; padding: 0.25em; }
  </ctl:styleBlock>
</rn:retractWapPage>
