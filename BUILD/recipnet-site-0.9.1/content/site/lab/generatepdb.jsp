<%--
  - Reciprocal Net project
  -
  - lab/generatepdb.jsp
  -
  - 12-Jan-2006: jobollin wrote first draft
  --%>
<%@ page import="org.recipnet.common.controls.ErrorChecker"%>
<%@ page
  import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage"%>
<%@ page import="org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage"%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.SimpleFileContext"%>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL"%>
<%@ page import="org.recipnet.site.shared.validation.FilenameValidator"%>
<%@ page import="org.recipnet.site.shared.validation.ContainerStringValidator"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl"%>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn"%>
<rn:generatePdbWapPage title="Generate a PDB File" loginPageUrl="/login.jsp"
  editSamplePageHref="/lab/generatefiles.jsp"
  currentPageReinvocationUrlParamName="origUrl"
  authorizationFailedReasonParamName="authorizationFailedReason"
  fileNameParamName="crtFile" pdbFileName="${param['pdbName']}">
  <rn:authorizationChecker canEditSample="true" invert="true"
    suppressRenderingOnly="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
      authorizationReasonParamName="authorizationFailedReason"
      authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE%>" />
  </rn:authorizationChecker>
  <rn:sampleChecker includeIfRetracted="true">
    <p class="errorMessage">This sample has been retracted by its lab and no
    files may be generated for it.</p>
  </rn:sampleChecker>
  <rn:sampleChecker includeIfRetracted="true" invert="true">
    <ctl:errorMessage errorFilter="<%= GeneratePdbFileWapPage.BAD_CRT %>">
      <p class="errorMessage">The specified file, ${param['crtFile']}, cannot
      be parsed as a CRT file.</p>
    </ctl:errorMessage>
    <ctl:errorMessage errorFilter="<%= GeneratePdbFileWapPage.CRT_IO_ERROR %>">
      <p class="errorMessage">The specified file, ${param['crtFile']}, could
      not be read. The problem may be transient, so reloading this page may
      help. If not then please notify the site administrator of the
      problem.</p>
    </ctl:errorMessage>
    <ctl:errorMessage errorFilter="<%= GeneratePdbFileWapPage.PDB_IO_ERROR %>">
      <p class="errorMessage">The specified file, ${pdbPage.pdbFileName}, could
      not be written. The problem may be transient, so clicking the appropriate
      action button again may resolve it. If not then please notify the site
      administrator of the problem.</p>
    </ctl:errorMessage>
    <ctl:errorChecker errorFilter="<%= ErrorChecker.ANY_ERROR
                                       ^ GeneratePdbFileWapPage.PDB_IO_ERROR %>"
      invert="true">
      <ctl:selfForm>
        <table class="formTable" cellspacing="0">
          <tr>
            <td class="pageInstructions" colspan="3">Enter the name of the PDB
            file to create, if different from the suggested name.  Optionally,
            specify a description of the file, enter comments about this file
            generation action, or both.  When ready, click the "Create PDB
            file" button button to create a PDB file reflecting the content of
            the specified CRT file, replacing any pre-existing repository file
            of the same name.  If you do not wish to create a PDB file then
            click the "Cancel" button.</td>
          </tr>
          <tr>
            <th style="white-space: nowrap;">CRT file name:</th>
            <td>
              <rn:fileField id="crtFile" fieldCode="<%= FileField.FILENAME %>"/>
            </td>
            <th style="text-align: left; padding-left: 1em;">PDB files already
              in the repository:</th>
          </tr>
          <tr>
            <th style="white-space: nowrap;">PDB file name:</th>
            <td>
              <ctl:textbox id="pdbName" size="22" required="true"
                shouldConvertBlankToNull="false"
                failedValidationHtml="<br/><span class='errorMessage'>The requested file name is not valid.</span>">
                <ctl:validator validator="<%=new FilenameValidator()%>" />
                ${rn:replaceTail(param['crtFile'], ".crt", "")}.pdb
              </ctl:textbox>
            </td>
            <td rowspan="4" style="vertical-align: top;">
              <ctl:phaseEvent onPhases="registration">
                <jsp:useBean id="pdbFullName"
                  class="org.recipnet.site.wrapper.StringBean" scope="page"/>
              </ctl:phaseEvent>
              <rn:fileIterator id="fileIt" sortFilesByName="true">
                <rn:fileChecker requiredExtension=".pdb">
                  <ctl:phaseEvent onPhases="rendering">
                    <jsp:setProperty name="pdbFullName" property="string"
                      value="${fileIt.sampleDataFile.name}"/>
                  </ctl:phaseEvent>
                  <div id="${pdbFullName.string}" class="fileName"><rn:fileField
                       fieldCode="<%= FileField.FILENAME %>" /></div>
                  <div class="fileDescription"><rn:fileField
                      fieldCode="<%= FileField.DESCRIPTION%>" /></div>
                </rn:fileChecker>
              </rn:fileIterator>
            </td>
          </tr>
          <tr>
            <th style="white-space: nowrap;">File description:</th>
            <td>
              <ctl:textarea id="description" columns="32" rows="2"
                shouldConvertBlankToNull="true"
                failedValidationHtml="<br/><span class='errorMessage'>The file description contains impermissible characters.</span>">
                <ctl:validator
                  validator="<%=new ContainerStringValidator()%>" />
              </ctl:textarea>
              <ctl:phaseEvent onPhases="fetching">
                <jsp:setProperty name="pdbPage" property="fileDescription"
                  value="${description.value}" />
              </ctl:phaseEvent>
            </td>
          </tr>
          <tr>
            <th style="white-space: nowrap;">Comments:</th>
            <td><rn:wapComments/></td>
          </tr>
          <tr>
            <td class="actionButton" colspan="2">
              <rn:wapSaveButton label="Create PDB file" />
              <rn:wapCancelButton />
            </td>
          </tr>
        </table>
      </ctl:selfForm>
      <script type="text/javascript">
        <!-- // hide script from ancient browsers
          var unflaggedColor = "black"
          var flaggedColor = "red"
          var pdbNameElement = document.getElementById("pdbName")
          var flaggedElement

          flagExistingFile("${pdbName.value}")
          if (pdbNameElement) {
            pdbNameElement.onkeyup = checkFileName
            pdbNameElement.onblur = checkFileName
            pdbNameElement.onchange = checkFileName
          }

          function checkFileName(e) {
            if (!e) var e = window.event
            var target = (e.target) ? e.target : e.srcElement

            if (target.value) {
              flagExistingFile(target.value)
            }

            return true
          }

          function flagExistingFile(fileName) {
            if (flaggedElement) {
              flaggedElement.style.color = unflaggedColor
            }
            flaggedElement = document.getElementById(fileName)
            if (flaggedElement) {
              flaggedElement.style.color = flaggedColor
            }
          }

             // stop hiding -->
      </script>
    </ctl:errorChecker>
  </rn:sampleChecker>
  <ctl:styleBlock>
    table.formTable { width: 60em; margin: 1em auto 0 auto;
        font-family: Arial, Helvetica, Verdana, sans-serif; }
    table.formTable td, table.formTable th {
        vertical-align: center; padding: 0.25em; }
    table.formTable th { text-align: left; padding-left: 1em; }
    table.formTable td.pageInstructions { padding-bottom: 1em; }
    table.formTable td.actionButton { text-align: center; padding-top: 1em; }
    td.actionButton input { padding-left: 0.2em; padding-right: 0.2em }
    div.fileName { margin-top: 0.25em; padding-left: 1em;}
    div.fileDescription { font-style: italic; margin: 0 0 0 2em; color: gray; 
        width: 20em;}
    .errorMessage { color: red; text-align: center; }
  </ctl:styleBlock>
</rn:generatePdbWapPage>
