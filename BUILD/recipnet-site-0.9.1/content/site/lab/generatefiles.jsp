<%--
  - Reciprocal Net project
  -
  - lab/generatefiles.jsp
  -
  - 12-Jan-2006: jobollin wrote first draft
  --%>
<%@ page
  import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage"%>
<%@ page import="org.recipnet.common.controls.HtmlPageCounter"%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl"%>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn"%>
<rn:samplePage title="Generate Files" loginPageUrl="/login.jsp"
  ignoreSampleHistoryId="true" currentPageReinvocationUrlParamName="origUrl"
  authorizationFailedReasonParamName="authorizationFailedReason">
  <rn:authorizationChecker canEditSample="true" invert="true"
    suppressRenderingOnly="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
      authorizationReasonParamName="authorizationFailedReason"
      authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE%>" />
  </rn:authorizationChecker>
  <rn:sampleChecker includeIfRetracted="true">
    <p class="errorMessage" style="text-align: center">This sample has been
    retracted by its lab and cannot have file actions performed on it.</p>
  </rn:sampleChecker>
  <rn:sampleChecker includeIfRetracted="true" invert="true">
  <div class="bodyDiv">
    <div class="sideBar">
      <span class="fieldLabel">Files present in the repository:</span>
      <rn:fileIterator sortFilesByName="true">
        <div class="fileName"><rn:fileField fieldCode="<%= FileField.FILENAME %>"
          /></div>
        <div class="fileDescription"><rn:fileField fieldCode="<%= FileField.DESCRIPTION%>"
          /></div>
      </rn:fileIterator>
    </div>
    <ctl:selfForm>
      <table class="actionTable" cellspacing="0">
        <tr>
          <th colspan="2" class="tableTitle">Available CRT file generation
           actions:</th>
        </tr>
        <tr>
          <th class="columnLabel" colspan="2">Use these actions to create CRT
            files in this sample's file repository.  You will be directed to a
            form on which you can name, describe, preview, and save the CRT
            model.</th>
        </tr>
        <ctl:counter id="cifRowCounter" />
        <rn:fileIterator id="cifFileIt" sortFilesByName="true"
          requestUnavailableFiles="false">
          <rn:fileChecker requiredExtension=".cif">
            <ctl:increment counter="${cifRowCounter}" />
            <ctl:phaseEvent onPhases="registration">
              <jsp:useBean id="cifName"
                class="org.recipnet.site.wrapper.StringBean" scope="page">
                <jsp:setProperty name="cifName" property="string" value="" />
              </jsp:useBean>
            </ctl:phaseEvent>
            <ctl:phaseEvent onPhases="processing,rendering">
              <jsp:setProperty name="cifName" property="string"
                value="${cifFileIt.sampleDataFile.name}" />
            </ctl:phaseEvent>
            <tr class="${rn:testParity(cifRowCounter.count, 'evenRow', 'oddRow')}">
              <td>Generate a CRT file from
                <span style="font-weight: bold;"><rn:fileField
                fieldCode="<%=FileField.FILENAME%>" /></span></td>
              <td class="actionButton"><rn:buttonLink
                label="Generate CRT file" target="/lab/generatecrt.jsp"
                cifName="${cifName}" /></td>
            </tr>
          </rn:fileChecker>
        </rn:fileIterator>
        <ctl:errorMessage errorSupplier="${cifRowCounter}"
          errorFilter="<%=HtmlPageCounter.NEVER_INCREMENTED%>">
          <tr class="oddRow">
            <td class="nofiles" colspan="2">none currently available</td>
          </tr>
        </ctl:errorMessage>
        <tr>
          <th colspan="2" class="tableTitle">Available PDB file generation
           actions:</th>
        </tr>
        <tr>
          <th class="columnLabel" colspan="2">Use these actions to create PDB
            files in this sample's file repository corresponding to CRT files
            in the repository.  You will be directed to a form on which you can
            name, describe, and save the PDB file.</th>
        </tr>
        <ctl:counter id="pdbRowCounter" />
        <rn:fileIterator id="crtIt1" sortFilesByName="true"
          requestUnavailableFiles="false">
          <rn:fileChecker requiredExtension=".crt">
            <ctl:increment counter="${pdbRowCounter}" />
            <ctl:phaseEvent onPhases="registration">
              <jsp:useBean id="crtName"
                class="org.recipnet.site.wrapper.StringBean" scope="page">
                <jsp:setProperty name="crtName" property="string" value="" />
              </jsp:useBean>
            </ctl:phaseEvent>
            <ctl:phaseEvent onPhases="processing,rendering">
              <jsp:setProperty name="crtName" property="string"
                value="${crtIt1.sampleDataFile.name}" />
            </ctl:phaseEvent>
            <tr class="${rn:testParity(pdbRowCounter.count, 'evenRow', 'oddRow')}">
              <td>Generate a PDB file from
                <span style="font-weight: bold;"><rn:fileField
                fieldCode="<%=FileField.FILENAME%>" /></span></td>
              <td class="actionButton"><rn:buttonLink label="Generate PDB file"
                target="/lab/generatepdb.jsp" crtFile="${crtName}" /></td>
            </tr>
          </rn:fileChecker>
        </rn:fileIterator>
        <ctl:errorMessage errorSupplier="${pdbRowCounter}"
          errorFilter="<%=HtmlPageCounter.NEVER_INCREMENTED%>">
          <tr class="oddRow">
            <td class="nofiles" colspan="2">none currently available</td>
          </tr>
        </ctl:errorMessage>
        <tr>
          <th colspan="2" class="tableTitle">Available ORTEP instruction
           generation actions:</th>
        </tr>
        <tr>
          <th class="columnLabel" colspan="2">Use these actions to create and
          download an ORTEP instruction file containing the default instructions
          used by JaMM to create ORTEP illustrations for the model described by
          the indicated CRT file.  You can tune the ORTEP details by modifying
          the instructions and uploading them to the repository.  There is no
          need to generate or upload ORTEP instructions if do not intend to
          customize them.</th>
        </tr>
        <ctl:counter id="ortRowCounter" />
        <ctl:phaseEvent onPhases="registration">
          <jsp:useBean id="crtName2"
            class="org.recipnet.site.wrapper.StringBean" scope="page">
            <jsp:setProperty name="crtName2" property="string" value="" />
          </jsp:useBean>
          <jsp:useBean id="sdtName"
            class="org.recipnet.site.wrapper.StringBean" scope="page">
            <jsp:setProperty name="sdtName" property="string" value="" />
          </jsp:useBean>
        </ctl:phaseEvent>
        <rn:fileIterator id="crtFileIt2" sortFilesByName="true"
          requestUnavailableFiles="false">
          <rn:fileChecker requiredExtension=".crt">
            <ctl:phaseEvent onPhases="fetching,processing,rendering">
              <jsp:setProperty name="crtName2" property="string"
                value="${crtFileIt2.sampleDataFile.name}" />
              <jsp:setProperty name="sdtName" property="string"
                value="${rn:replaceTail(crtName2, '.crt', '.sdt')}" />
            </ctl:phaseEvent>
            <rn:multiFilenameChecker requiredFilenameGlob="${sdtName}">
              <ctl:increment counter="${ortRowCounter}" />
              <tr
                class="${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}">
                <td>Generate ORTEP instructions from
                  <span style="font-weight: bold;"><rn:fileField
                  fieldCode="<%=FileField.FILENAME%>" /></span> and
                  <span style="font-weight: bold;">${sdtName}</span></td>
                <td class="actionButton"><rn:buttonLink
                  label="Generate ORTEP instructions"
                  target="/servlet/generateort" crtName="${crtName2}"
                  sdtName="${sdtName}" /></td>
              </tr>
            </rn:multiFilenameChecker>
            <rn:multiFilenameChecker requiredFilenameGlob="*.cif"
              forbiddenFilenameGlob="${sdtName}">
              <rn:cifChooser id="cifChooser" />
              <ctl:increment counter="${ortRowCounter}" />
              <tr
                class="${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}">
                <td>Generate ORTEP instructions from
                  <span style="font-weight: bold;"><rn:fileField
                  fieldCode="<%=FileField.FILENAME%>" /></span> and
                  <span style="font-weight: bold;">${cifChooser.cifName}</span></td>
                <td class="actionButton"><rn:buttonLink
                  label="Generate ORTEP instructions"
                  target="/servlet/generateort" crtName="${crtName2}"
                  cifName="${cifChooser.cifName}" /></td>
              </tr>
            </rn:multiFilenameChecker>
          </rn:fileChecker>
        </rn:fileIterator>
        <ctl:errorMessage errorSupplier="${ortRowCounter}"
          errorFilter="<%=HtmlPageCounter.NEVER_INCREMENTED%>">
          <tr class="oddRow">
            <td class="nofiles" colspan="2">none currently available</td>
          </tr>
        </ctl:errorMessage>
      </table>
    </ctl:selfForm>
  </div>
  <p class="navlinks"><rn:link href="/lab/sample.jsp">Back to
   "Edit Sample"...</rn:link><br />
  <rn:link href="/jamm.jsp">JaMM visualization...</rn:link><br />
  <rn:link href="/lab/uploadfilesform.jsp">Upload files
   (form-based)...</rn:link><br />
  <rn:link href="/lab/uploadfilesapplet.jsp">Upload files
   (Drag-and-Drop)...</rn:link><br />
  <rn:link href="/lab/managefiles.jsp">Manage files...</rn:link></p>
  </rn:sampleChecker>
  <ctl:styleBlock>
    div.bodyDiv { font-family: Arial, Helvetica, Verdana; margin: 1em 1em 0 1em}
    div.sideBar { float: right; width: 300px; margin-left: 1em; }
    table.actionTable { border: 3px solid #32357D; 
        margin-left: auto; margin-right: auto; max-width: 60em; }
    table.actionTable th, table.actionTable td { padding: 0.25em }
    tr.oddRow { background: #F0F0F0; color: #000050; }
    tr.evenRow { background: #D0D0D0; color: #000050; }
    th.tableTitle { background: #32357D; color: #FFFFFF;
        font-weight: bold; text-align: left; }
    th.columnLabel { background: #656BFA; color: #FFFFFF;
        font-style: italic; font-weight: normal; text-align: left; }
    td.actionButton { text-align: center; width: 1px; }
    td.nofiles { color: #707070; font-style: italic; text-align: center; }
    div.fileName { margin: 0.25em 0 0 0; }
    div.fileDescription { font-style: italic; margin: 0 0 0 1em; color: gray; }
    span.fieldLabel { font-style: normal; font-weight: bold; }
    p.errorMessage { color: red; }
    p.navlinks { text-align: right; margin-top: 0.5em; }
  </ctl:styleBlock>
</rn:samplePage>
