<%--
   - Reciprocal Net project
   - upload.jsp
   -
   - 16-June-2005: ekoperda wrote first draft
   - 13-Jul-2005: midurbin added new required parameters to uploaderPage
   - 04-Aug-2005: midurbin altered page to return to /lab/files.jsp on
   -              cancellation or completion; set new property
   -              'uploadConfirmationPageHref' on UploaderPage
   - 21-Oct-2005: midurbin included the file description
   - 01-Feb-2006: jobollin added a radio button control to support changes to
   -              heldFileOption
   - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileField" %>
<%@ page import="org.recipnet.site.content.rncontrols.HeldFileOption" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UploaderPage" %>
<rn:uploaderPage
    title="Resolve conflicts in uploaded files"
    workflowActionCode="<%=SampleWorkflowBL.MULTIPLE_FILES_ADDED_OR_REPLACED%>"
    editSamplePageHref="/lab/managefiles.jsp"
    heldFilesPageHref="/lab/uploadedfileconflicts.jsp"
    uploadConfirmationPageHref="/lab/uploadcompleted.jsp"
    loginPageUrl="/login.jsp" currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <ctl:selfForm>
    <p class="pageInstructions">
      At least one of the files you just uploaded has the same name as an
      existing data file.  For each file listed below, select which version(s)
      you wish to keep.
    </p>

    <table class="fileTable" border="0" cellspacing="0">
      <tr>
        <th class="headerFont">File name:</th>
        <th>&nbsp;</th>
        <th class="headerFont">
          Existing file:<br />(abandon uploaded file)
        </th>
        <th>&nbsp;</th>
        <th class="headerFont">
          Uploaded file:<br />(replace existing file)
        </th>
      </tr>    

      <ctl:phaseEvent onPhases="registration">
        <jsp:useBean id="rowClass" class="org.recipnet.site.wrapper.StringBean"
            scope="page"/>
      </ctl:phaseEvent>
      <rn:heldFileIterator id="hfit"
          fileRetrieveServletHref="/servlet/fileretrieve" keyParamName="key">
        <jsp:setProperty name="rowClass" property="string"
            value="${rn:testParity(hfit.iterationCountSinceThisPhaseBegan,
                                   'rowColorEven', 'rowColorOdd')}"/>
        <tr class="${rowClass.string}">
           <td colspan="6" class="spacerCell">&nbsp;</td>
        </tr>
        <tr class="${rowClass.string}">
        <td class="fileNameCell">
  	  <rn:fileField fieldCode="<%=FileField.FILENAME%>" />
        </td>
        <td width="5">&nbsp;</td>
        <ctl:radioButtonGroup>
        <td class="fileDetailCell">
          <rn:fileContextTranslator translateProvisionalToSettled="true">
            <rn:fileField 
                fieldCode="<%=FileField.LINKED_FILENAME%>" 
                label="existing file" />
            (<rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" />)
            <br />
            last modified:
            <rn:sampleHistoryField 
                fieldCode="<%=SampleHistoryField.FieldCode.ACTION_DATE%>"
                requireHistory="false"
                noHistoryText="unknown"
                /><br />
            by:
            <rn:sampleHistoryField fieldCode="<%=
                   SampleHistoryField.FieldCode.
                   USER_FULLNAME_THAT_PERFORMED_ACTION%>"
                requireHistory="false"
                noHistoryText="unknown"
                   />
            <rn:fileChecker includeOnlyIfFileHasDescription="true">
              <br />
              description:
                <rn:fileField fieldCode="<%=FileField.DESCRIPTION%>" />
            </rn:fileChecker>
          </rn:fileContextTranslator>
          <center>
            <rn:heldFileOption 
                fileToKeep="<%=HeldFileOption.FileToKeep.EXISTING%>" />
            keep this file
          </center>
        </td>
        <td width="20">&nbsp;</td>
        <td class="fileDetailCell">
          <rn:fileField fieldCode="<%=FileField.LINKED_FILENAME%>" 
              label="uploaded file" />
          (<rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" />)
          <rn:fileChecker includeOnlyIfFileHasDescription="true">
            <br />
            description:
              <rn:fileField fieldCode="<%=FileField.DESCRIPTION%>" />
          </rn:fileChecker>
          <center>
            <rn:heldFileOption
                fileToKeep="<%=HeldFileOption.FileToKeep.UPLOADED%>" />
            keep this file
          </center>
        </td>
        </ctl:radioButtonGroup>
        <td width="10">&nbsp;</td>
        </tr>
        <tr class="${rowClass.string}">
          <td colspan="6" class="spacerCell">&nbsp;</td>
        </tr>        
      </rn:heldFileIterator>
    </table>
    &nbsp;<br />
    <rn:wapSaveButton />
    <rn:wapCancelButton />
  </ctl:selfForm>
  <ctl:styleBlock>
    .headerFont { font-size: medium; font-weight: bold; }
    .fileNameCell { font-weight: bold; padding: 0.1in }
    .fileDetailCell { border-style: solid; border-width: thin; 
            border-color: #CCCCCC; padding: 0.1in; }
    .spacerCell { font-size: 1%; padding: 0.03in }
    .rowColorEven { background-color: #E6E6E6 }
    .rowColorOdd  { background-color: #FFFFFF }
    .fileTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in; }
    .pageInstructions { font-weight: bold; font-style: normal; font-size:
            medium; text-decoration: none; }
  </ctl:styleBlock>
</rn:uploaderPage>
