<%--
  - Reciprocal Net project
  -
  - lab/generatecrt.jsp
  -
  - 24-Jan-2006: jobollin wrote first draft
  - 28-Apr-2006: jobollin added tolerant handling of invalid CIFs
  --%>
<%@ page
  import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage"%>
<%@ page import="org.recipnet.common.controls.ErrorChecker"%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.CifFileContext"%>
<%@ page import="org.recipnet.site.shared.validation.FilenameValidator"%>
<%@ page import="org.recipnet.site.shared.validation.ContainerStringValidator"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl"%>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn"%>
<rn:generateCrtWapPage title="Generate a CRT File" loginPageUrl="/login.jsp"
  editSamplePageHref="/lab/generatefiles.jsp"
  currentPageReinvocationUrlParamName="origUrl"
  authorizationFailedReasonParamName="authorizationFailedReason"
  fileNameParamName="cifName" crtFileName="${param['crtName']}"
  crtFileKey="${param['crtKey']}">
  <div class="bodyDiv">
    <rn:authorizationChecker canEditSample="true"
      invert="true" suppressRenderingOnly="true">
      <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE%>" />
      </rn:authorizationChecker> <rn:sampleChecker includeIfRetracted="true">
      <p class="errorMessage">This sample has been retracted by its lab and no
      files may be generated for it.</p>
    </rn:sampleChecker>
    <rn:sampleChecker includeIfRetracted="true" invert="true">
      <%-- normal page body begins here --%>
      <ctl:selfForm id="pageForm">
        <ctl:hiddenString id="crtKey" initialValue="" />
        <table class="formTable" border="0" style="margin: 0;">
          <tr>
            <td style="vertical-align: top;">
              <div style="margin-bottom: 1em">Use the controls below to describe
              the model view for which a CRT should be generated, then click the
              "Compute Model" button to preview the result. When you are
              satisfied, assign an appropriate file name (and optionally a
              description), and click the "Save CRT File" button to save the CRT
              to the repository, overwriting any existing file of the same name.
              Click the "Cancel" button to return to the Generate Files page
              without saving a CRT file to the repository.</div>
              <rn:cifFile id="cif">
                <div style="text-align: right; vertical-align: top;">
                  <div style="margin: 0; float: left;"><span
                      class="fieldLabel">CIF name:</span>
                    <rn:fileField id="cifName"
                        fieldCode="<%= FileField.FILENAME %>" />
                    <ctl:errorMessage
                        errorFilter="<%= CifFileContext.CIF_HAS_ERRORS %>">
                      <span class="errorNotice">[contains errors]</span>
                    </ctl:errorMessage>
                  </div>
                  <ctl:errorMessage errorFilter="<%=
                      CifFileContext.UNPARSEABLE_CIF 
                      | CifFileContext.EMPTY_CIF
                      | CifFileContext.NO_FILE_SPECIFIED %>">
                    <span class="errorMessage">CIF is invalid or has no data
                      blocks</span>
                  </ctl:errorMessage>
                  <ctl:errorChecker invert="true" errorFilter="<%=
                      CifFileContext.UNPARSEABLE_CIF 
                      | CifFileContext.EMPTY_CIF
                      | CifFileContext.NO_FILE_SPECIFIED %>">
                    <ctl:button id="generateCrtButton" label="Compute Model"/>
                  </ctl:errorChecker>
                </div>
                <ctl:radioButtonGroup id="modelType" initialValue="simple">
                  <div id="simpleDiv" class="optionDiv"><ctl:radioButton
                    id="simpleRadio" option="simple">
                    <ctl:extraHtmlAttribute name="onClick"
                      value="selectMainOption('simple')" />
                    </ctl:radioButton> <dfn>Simple model</dfn>. Generate a
                    model including all atoms from the CIF but not accounting
                    for any crystallographic symmetry.
                  </div>
                  <div id="grownDiv" class="optionDiv"><ctl:radioButton
                    id="grownRadio" option="grown">
                    <ctl:extraHtmlAttribute name="onClick"
                      value="selectMainOption('grown')" />
                    </ctl:radioButton> <dfn>Grown model</dfn>. Generate a model
                    including the specified atom(s) and all other atoms in the
                    same chemical moiety(-ies), including symmetry copies as
                    necessary; truncate extended structures at one unit cell
                    length in each dimension.<br />
                    <div class="innerDiv" style="margin-right: 0"><span
                      style="vertical-align: 250%; font-weight:bold;"> Ensure these atoms are
                      included:</span>
                      <ctl:listbox id="grownModelSeeds" multiple="true">
                        <ctl:extraHtmlAttribute name="size" value="4" />
                        <rn:atomSites id="seedSites" excludedTypeSymbols="H">
                          <ctl:option label="${seedSites.siteRecord.label}" />
                        </rn:atomSites>
                      </ctl:listbox>
                    </div>
                  </div>
                  <div id="packedDiv" class="optionDiv"><ctl:radioButton
                    id="packedRadio" option="packed">
                    <ctl:extraHtmlAttribute name="onClick"
                      value="selectMainOption('packed')" />
                    </ctl:radioButton> <dfn>Packing model</dfn>. Generate a
                    model representing the contents of a triclinic box of the
                    specified relative dimensions, centered at the specified
                    position.<br />
                    <ctl:radioButtonGroup initialValue="coordinates">
                      <table>
                        <tr>
                          <th>Packing box dimensions:</th>
                          <td><ctl:doubleTextbox id="asize" minValue="0"
                            maxValue="7">1</ctl:doubleTextbox>
                          <ctl:doubleTextbox id="bsize" minValue="0"
                            maxValue="7">1</ctl:doubleTextbox>
                          <ctl:doubleTextbox id="csize" minValue="0"
                            maxValue="7">1</ctl:doubleTextbox>
                          </td>
                        </tr>
                        <tr>
                          <th><ctl:radioButton id="coordsRadio"
                            option="coordinates">
                            <ctl:extraHtmlAttribute name="onClick"
                              value="selectOption('coords')" />
                            </ctl:radioButton> Box center at coordinates:</th>
                          <td>
                           <ctl:doubleTextbox id="acenter" minValue="0"
                             maxValue="7">0.5</ctl:doubleTextbox>
                           <ctl:doubleTextbox id="bcenter" minValue="0"
                             maxValue="7">0.5</ctl:doubleTextbox>
                            <ctl:doubleTextbox id="ccenter" minValue="0"
                             maxValue="7">0.5</ctl:doubleTextbox>
                          </td>
                        </tr>
                        <tr>
                          <th><ctl:radioButton id="atomRadio" option="atom">
                            <ctl:extraHtmlAttribute name="onClick"
                              value="selectOption('atom')" />
                          </ctl:radioButton> Box center at atom:</th>
                          <td><ctl:listbox id="packedModelCenter">
                            <rn:atomSites id="centerSites"
                              excludedTypeSymbols="H">
                              <ctl:option
                                label="${centerSites.siteRecord.label}" />
                            </rn:atomSites>
                          </ctl:listbox></td>
                        </tr>
                      </table>
                    </ctl:radioButtonGroup>
                    <ctl:errorChecker errorSupplier="${cif}"
                      errorFilter="<%= ErrorChecker.ANY_ERROR %>" invert="true">
                      <ctl:ifValueIsTrue control="${generateCrtButton}">
                        <ctl:ifValueIsTrue control="${grownRadio}">
                          <ctl:phaseEvent onPhases="fetching"
                            skipIfSuppressed="true">
                            <jsp:setProperty name="generator"
                              property="seedSites"
                              value="${grownModelSeeds.valueAsStringCollection}"/>
                          </ctl:phaseEvent>
                        </ctl:ifValueIsTrue>
                        <ctl:ifValueIsTrue control="${packedRadio}">
                          <ctl:phaseEvent onPhases="fetching"
                            skipIfSuppressed="true">
                            <jsp:setProperty name="generator"
                              property="boxSize"
                              value="${asize.value},${bsize.value},${csize.value}"/>
                          </ctl:phaseEvent>
                          <ctl:ifValueIsTrue control="${coordsRadio}">
                            <ctl:phaseEvent onPhases="fetching"
                              skipIfSuppressed="true">
                              <jsp:setProperty name="generator"
                                property="boxCoordinates"
                                value="${acenter.value},${bcenter.value},${ccenter.value}"/>
                            </ctl:phaseEvent>
                          </ctl:ifValueIsTrue>
                          <ctl:ifValueIsTrue control="${atomRadio}">
                            <ctl:phaseEvent onPhases="fetching"
                              skipIfSuppressed="true">
                              <jsp:setProperty name="generator"
                                property="boxCenter"
                                value="${packedModelCenter.value}" />
                            </ctl:phaseEvent>
                          </ctl:ifValueIsTrue>
                        </ctl:ifValueIsTrue>
                        <rn:generateCrt id="generator" keyControl="${crtKey}"/>
                      </ctl:ifValueIsTrue>
                    </ctl:errorChecker>
                    <ctl:errorChecker errorSupplier="${cif}"
                      errorFilter="<%= ErrorChecker.ANY_ERROR %>">
                      <ctl:phaseEvent
                        onPhases="fetching" skipIfSuppressed="true">
                        <jsp:setProperty
                          name="crtKey" property="value" value="${''}" />
                      </ctl:phaseEvent>
                    </ctl:errorChecker>
                  </div>
                  <div style="margin-top: 1em; margin-bottom: 1em;">
                    <hr />
                  </div>
                  <table>
                    <tr>
                      <th style="padding-top: 0.2em; vertical-align: top;">CRT
                        name:</th>
                      <td><ctl:textbox id="crtName" size="22" required="true"
                        shouldConvertBlankToNull="false"
                        failedValidationHtml="<br/><span class='errorMessage'>The requested file name is not valid.</span>">
                        <ctl:validator validator="<%=new FilenameValidator()%>" />
                        ${rn:replaceTail(param['cifName'], ".cif", "")}.crt
                        </ctl:textbox>
                      </td>
                    </tr>
                    <tr>
                      <th style="padding-top: 0.2em; vertical-align: top;">CRT
                        description:</th>
                      <td>
                        <ctl:textarea id="description" columns="32" rows="2"
                          shouldConvertBlankToNull="true"
                          failedValidationHtml="<br/><span class='errorMessage'>The file description contains impermissible characters.</span>">
                          <ctl:validator
                            validator="<%=new ContainerStringValidator()%>" />
                        </ctl:textarea>
                        <ctl:phaseEvent onPhases="fetching">
                          <jsp:setProperty name="crtPage"
                            property="fileDescription"
                            value="${description.value}" />
                        </ctl:phaseEvent>
                      </td>
                    </tr>
                    <tr>
                      <th style="padding-top: 0.2em; vertical-align: top;">Comments:</th>
                      <td><rn:wapComments /></td>
                    </tr>
                  </table>
                </ctl:radioButtonGroup>
              </rn:cifFile>
              <div class="formDiv" style="text-align: center;">
                <rn:wapSaveButton label="Save CRT file"
                  editable="${not empty crtKey.valueAsString}" />
                <rn:wapCancelButton />
              </div>
            </td>
            <td style="vertical-align: top;">
              <div id="sidebar" style="width: 300px; margin: 0.5em 0 0 1em;"><span
                class="fieldLabel">Computed model:</span>
                <div id="jammDiv"
                  style="width: 300px; height: 300px; background: silver; border: 1px solid #32357D;">
                  <rn:checkTrackedFile fileKey="${crtKey.valueAsString}"
                    requireValid="true">
                    <rn:jammModel
                      modelUrl="${htmlPage.contextPath}/servlet/fileretrieve?key=${crtKey.valueAsString}">
                      <rn:jammApplet width="300" height="300" applet="minijamm"
                        border="false"/>
                    </rn:jammModel>
                  </rn:checkTrackedFile>
                  <ctl:errorMessage errorSupplier="${generator}">
                    <span class="errorMessage">CRT generation failed -- check your CIF</span>
                  </ctl:errorMessage>
                </div>
                <div style="font-style: italic; margin-bottom: 1em; ">(Toggle
                  label display with the 'L' key; you may need to click in the
                  window first.)</div>
                <span class="fieldLabel">CRT files already in the repository:</span><br />
                <rn:fileIterator sortFilesByName="true">
                  <rn:fileChecker requiredExtension=".crt">
                    <div class="fileName"><rn:fileField fieldCode="<%= FileField.FILENAME %>"
                      /></div>
                    <div class="fileDescription"><rn:fileField fieldCode="<%= FileField.DESCRIPTION%>"
                      /></div>
                  </rn:fileChecker>
                </rn:fileIterator>
              </div>
            </td>
          </tr>
        </table>
      </ctl:selfForm>
    </rn:sampleChecker>
  </div>
  <ctl:styleBlock>
    .errorMessage { color: red; text-align: center; }
    div.bodyDiv { font-family: Arial, Helvetica, Verdana, sans-serif;
        margin: 1em }
    div.bodyDiv div { margin: .5em 0 .5em 0em; }
    div.formDiv, div.optionDiv { padding: .5em .5em .5em 2.5em; }
    div.optionDiv { border: 1px solid #32357D; text-indent: -2em; }
    div.innerDiv { margin-top: 1em; text-indent: 0 }
    span.fieldLabel, div.bodyDiv th { font-style: normal; font-weight: bold; }
    div.bodyDiv div.fileName { margin: 0.25em 0 0 0; }
    div.bodyDiv div.fileDescription { font-style: italic; margin: 0 0 0 1em;
        color: gray; }
    div.bodyDiv th { text-align: left; }
    div.bodyDiv tr { margin-top: 0.5em; margin-bottom: 0.5em; }
  </ctl:styleBlock>
  <script type="text/javascript">
  <!-- // begin hiding javascript from ancient browsers
    var pageForm = document.getElementById('pageForm')
    var mainOptions = new Array("simple", "grown", "packed")
    var buttonChildren = new Array()
    var buttonPeers = new Array()

    buttonChildren["simpleRadio"] = new Array()
    buttonChildren["grownRadio"] = new Array("grownModelSeeds")
    buttonChildren["packedRadio"]
      = new Array("asize", "bsize", "csize", "coordsRadio", "atomRadio")
    buttonChildren["coordsRadio"] = new Array("acenter", "bcenter", "ccenter")
    buttonChildren["atomRadio"] = new Array("packedModelCenter")

    buttonPeers["simpleRadio"] = new Array("grownRadio", "packedRadio")
    buttonPeers["grownRadio"] = new Array("simpleRadio", "packedRadio")
    buttonPeers["packedRadio"] = new Array("simpleRadio", "grownRadio")
    buttonPeers["coordsRadio"] = new Array("atomRadio")
    buttonPeers["atomRadio"] = new Array("coordsRadio")
    
    function selectMainOption(option) {
      for (var index = 0; index < mainOptions.length; index++) {
        var div = document.getElementById(mainOptions[index] + "Div")
        
        if (option == mainOptions[index]) {
          div.style.background="white"
        } else {
          div.style.background="silver"
        }
      }
      selectOption(option)
    }
    
    function selectOption(option) {
      var radioId = option + "Radio"
      var peers = buttonPeers[radioId]

      enableChildControls(radioId, true, false)
      for (var index = 0; index < peers.length; index++) {
        enableChildControls(peers[index], false, false)
      }
    }

    function enableChildControls(radioId, state, unconditional) {
      var childControls = buttonChildren[radioId]

      for (var index = 0; index < childControls.length; index++) {
        var childId = childControls[index]
        var child = document.getElementById(childId)
        
        child.disabled = !state
        if (buttonChildren[childId]) {
          if (child.checked || !state || unconditional) {
            enableChildControls(childId, state, unconditional)
          }
        }
      }
    }

    function enableAllControls() {
      for (var index = 0; index < mainOptions.length; index++) {
        var radioId = mainOptions[index] + "Radio"
        var button = document.getElementById(radioId)

        button.disabled = false
        enableChildControls(radioId, true, true)  
      }
      
      return true
    }
    
    pageForm.onsubmit = enableAllControls
    
    for (var index = 0; index < mainOptions.length; index++) {
      var button = document.getElementById(mainOptions[index] + "Radio")

      if (button.checked) {
        button.click()
        break
      }
    }

    // stop hiding javascript -->
  </script>
</rn:generateCrtWapPage>
