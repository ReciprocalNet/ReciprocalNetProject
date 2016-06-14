<%--
   - Reciprocal Net project
   - /lab/uploadfilesapplet.jsp
   -
   - 16-Jun-2005: ekoperda wrote first draft
   - 13-Jul-2005: midurbin added new required parameters to uploaderPage
   - 04-Aug-2005: midurbin altered page to return to /lab/files.jsp on
   -              cancellation or completion
   - 21-Oct-2005: midurbin added navigation links to other pages, renamed file
   - 30-Mar-2006: jobollin updated page to use the new styles
   - 10-Jan-2008: ekoperda updated the top-of-page instructions
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UploaderPage" %>
<rn:uploaderPage
    title="Upload Files: Drag-and-Drop"
    workflowActionCode="<%=SampleWorkflowBL.MULTIPLE_FILES_ADDED_OR_REPLACED%>"
    editSamplePageHref="/lab/managefiles.jsp"
    heldFilesPageHref="/lab/uploadedfileconflicts.jsp"
    loginPageUrl="/login.jsp" currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <ctl:selfForm>
    <p class="pageInstructions">
      Drag and drop file icons from your desktop computer's file manager to the
      rectangle below.  Click the Save button when you're done.  You will be
      prompted again if any existing sample data files would be overwritten.
    </p>
    <p class="pageInstructions">
      If you happen to see a security warning, please click the 'Yes' button or
      the 'Always' button.  Your browser will need to trust Reciprocal Net's 
      signed applet for this feature to work.  If you are using Microsoft
      Internet Explorer, you may need to click the applet to activate it.
    </p>
    <p style="font-weight: bold;">
      Sample data files already attached to this sample:
    </p>
    <ul style="text-align: left; margin-left: auto; margin-right: auto; max-width: 50em;">
      <rn:fileIterator id="existingSampleDataFiles">
        <li>
          <rn:fileField />, 
          <rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" /> bytes,
          uploaded by 
          <rn:sampleHistoryField fieldCode="<%=
            SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION%>"
                requireHistory="false"
                noHistoryText="unknown"
            />
          at
          <rn:sampleHistoryField 
              fieldCode="<%=SampleHistoryField.FieldCode.ACTION_DATE%>" 
              requireHistory="false"
              noHistoryText="unknown"
              />
        </li>
      </rn:fileIterator>
      <ctl:errorMessage errorSupplier="${existingSampleDataFiles}" 
          errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
        <li>
          <i>(none)</i>
        </li>
      </ctl:errorMessage>
    </ul>

    <p style="font-weight: bold;">
      Sample data files uploaded but not yet saved:
    </p>
    <div style="margin-left: 1em;">
      <rn:uploaderApplet archiveHref="/applets/uploader.jar"
          supportServletHref="/servlet/uploadersupport" 
          height="150" width="300" />
    </div>

    <rn:wapSaveButton />
    <rn:wapCancelButton />
  </ctl:selfForm>
    <p class="navLinks">
      <rn:link href="/lab/sample.jsp">Back to "Edit Sample"...</rn:link>
      <br />
      <rn:link href="/lab/managefiles.jsp">Manage files...</rn:link>
      <br />
      <rn:link href="/lab/uploadfilesform.jsp">
        Upload files (form-based)...
      </rn:link>
    </p>
  </div>
  <ctl:styleBlock>
    .navLinks { text-align: right; }
  </ctl:styleBlock>
</rn:uploaderPage>
