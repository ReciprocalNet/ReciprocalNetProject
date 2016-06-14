<%--
  - Reciprocal Net project
  -
  - lab/uploadfilesform.jsp
  -
  - 21-Oct-2005: midurbin wrote first draft, factoring code from files.jsp
  - 30-Mar-2006: jobollin updated this page to use the new styles
  - 19-Mar-2009: ekoperda fixed bug #1922 in JS function setRemoteName()
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@ page import="org.recipnet.site.content.servlet.MultipartUploadAccepter" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:samplePage title="Upload Files"
        loginPageUrl="/login.jsp" ignoreSampleHistoryId="true"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <rn:authorizationChecker canEditSample="true" invert="true"
      suppressRenderingOnly="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE%>"/>
  </rn:authorizationChecker>
  <rn:sampleChecker includeIfRetracted="true">
    <p class=errorMessage">
      This sample has been retracted by its lab and no more files may be
      uploaded for it.
    </p>
  </rn:sampleChecker>
  <rn:sampleChecker includeIfRetracted="true" invert="true">
    <ctl:extraHtmlAttribute name="onLoad"
        value="enableDisableButton('Button', 'selectedFiles')" />
      <ctl:form method="POST" enctype="multipart/form-data"
          pageForm="false" action="/servlet/multipartuploadaccepter">
        <rn:uploadOp
            workflowAction="<%=SampleWorkflowBL.FILE_ADDED%>" />
        <table class="bodyTable" cellspacing="0"> 
          <tr>
            <th class="tableHeader" colspan="3">
              File upload:
            </th>
          </tr>
          <tr>
           <th colspan="3" style="padding-top: 0.25em;">
             Comments: (optional)
           </th>
          </tr>
          <tr>
            <td colspan="3">
              <textarea rows="2" cols="36" name="<%=
                  MultipartUploadAccepter.COMMENTS_PARAM_NAME%>"
                  wrap="virtual"></textarea>
            </td>
          </tr>
          <tr>
            <th colspan="2" class="mainHeading">
              Filename:
            </th>
            <th class="mainHeading">
              Description:
            </th>
          </tr>
          <tr>
            <td colspan="3">
              <hr size="1" color="#32357D" />
            </td>
          </tr>
          <ctl:iterator id="files" iterations="5">
          <tr>
            <th>
              Remote: (optional)
            </th>
            <td>
              <input type="text" size="32"
                  name="<%=MultipartUploadAccepter.FILE_NAME_PREFIX%>${
                      files.iterationCountSinceThisPhaseBegan}"
                  onChange="updatedField('<%=
                      MultipartUploadAccepter.FILE_NAME_PREFIX%>${
                          files.iterationCountSinceThisPhaseBegan}')" />
            </td>
            <td rowspan="2">
              <textarea columns="32" rows="2" name="<%=
                MultipartUploadAccepter.DESCRIPTION_PREFIX%>${
                    files.iterationCountSinceThisPhaseBegan}"></textarea>
            </td>
          </tr>
          <tr>
            <th>
              Local:
            </th>
            <td>
            <%--
               - onChange() works for most browsers, 
               - onBlur() produces similar behavior in others
               - see notes accompanying the javascript below
              --%>
              <input type="file" size="22"
                  name="<%=MultipartUploadAccepter.FILE_PREFIX%>${
                      files.iterationCountSinceThisPhaseBegan}"
                  onChange="setRemoteName('<%=
                      MultipartUploadAccepter.FILE_NAME_PREFIX%>${
                          files.iterationCountSinceThisPhaseBegan
                      }','<%=MultipartUploadAccepter.FILE_PREFIX%>${
                          files.iterationCountSinceThisPhaseBegan}')"
                  onBlur="setRemoteName('<%=
                      MultipartUploadAccepter.FILE_NAME_PREFIX%>${
                          files.iterationCountSinceThisPhaseBegan
                      }','<%=MultipartUploadAccepter.FILE_PREFIX%>${
                          files.iterationCountSinceThisPhaseBegan}')">
            </td>
          </tr>
          <tr>
            <td colspan="3">
              <hr size="1" color="#32357D" />
            </td>
          </tr>
          </ctl:iterator>
          <tr>
            <td colspan="3" class="formButtons" style="padding: 0.25em;">
              <input type="submit" value="Upload now" />
            </td>
          </tr>
        </table>
      </ctl:form>
    <p class="navLinks">
      <rn:link href="/lab/sample.jsp">Back to "Edit Sample"...</rn:link>
      <br />
      <rn:link href="/lab/managefiles.jsp">Manage files...</rn:link>
      <br />
      <rn:link href="/jamm.jsp">JaMM visualization...</rn:link>
      <br />
      <rn:link href="/lab/uploadfilesapplet.jsp">
        File Upload (Drag and Drop)...
      </rn:link>
    </p>
  </rn:sampleChecker>
  </div>
  <ctl:styleBlock>
    table.bodyTable { margin-top: 1em; border: 3px solid #32357D;
        background: #F0F0F0; color: #000050; }
    table.bodyTable th,
    table.bodyTable td { padding: 0 0.25em 0 0.25em; text-align: left;
        font-weight: normal; font-style: normal; }
    table.bodyTable th.tableHeader { background: #32357D; color: #FFFFFF;
        padding: 0.25em; font-weight: bold; }
    table.bodyTable th.mainHeading { padding-top: 0.5em; font-weight: bold; }
    .navLinks { text-align: right; }
  </ctl:styleBlock>
  <script language="JavaScript">
    <!-- // begin hiding javascript from older browsers

        var manuallyUpdatedFieldNames = "";

        function updatedField(fieldName) {
            if (manuallyUpdatedFieldNames.indexOf(fieldName)==-1) {
                manuallyUpdatedFieldNames=manuallyUpdatedFieldNames+fieldName;
            }
        }

        // This function sets the form field with the name given as the
        // remoteNameBoxId parameter to the filename to the filename entered
        // in the localNameBoxId box (with the path removed) if there is no
        // remote filename entered.

        // Netscape 4.8 does not trigger onChange() when the "Browse"
        // button of the FileUpload element is clicked, so it is
        // neccessary to include a call to this function for the onBlur()
        // event as well, to approximate the desired behavior
        // onClick is triggered when the "browse" button of a FileUpload
        // element is clicked for some browsers, though there is variance
        // in whether it is proccessed before or after the field is
        // updated by the file selection dialog box.
	//
	// IE likes to give us a whole file path beginning with C:\, while
	// Firefox 3.0.7 likes to give us just the file name with no slashes or
	// other reference to directories.  The function below accommodates
	// both.
        function setRemoteName(remoteNameBoxId, localNameBoxId) {
            if (manuallyUpdatedFieldNames.indexOf(remoteNameBoxId)!=-1) {
              // the box shouldn't be updated
              return;
            } else {
              var localPath = getElementById(localNameBoxId).value;
              var lastSlash = 0;
                for (var j = 0; j < localPath.length; j ++) {
                  if (localPath.charAt(j) == '/'
                      || localPath.charAt(j) == '\\') {
                    lastSlash = j + 1;
                  }
                }
              getElementById(remoteNameBoxId).value
                  = localPath.substring(lastSlash);
            }
        }

        function getElementById(id) {
          for (var fi = 0; fi < document.forms.length; fi ++) {
            var index = 0;
            while (index < document.forms[fi].elements.length) {
              if (document.forms[fi].elements[index].name == id) {
                return document.forms[fi].elements[index];
              } else {
                  index ++;
              }
            }
          }
        }

        function getElementByIdPart(idPart) {
          for (var fi = 0; fi < document.forms.length; fi ++) {
            var index = 0;
            while (index < document.forms[fi].elements.length) {
              if (document.forms[fi].elements[index].name.indexOf(idPart)==-1) {
                index++;
              } else {
                return document.forms[fi].elements[index];
              }
            }
          }
        }

        // if any checkbox element in the page with the name 'checkboxName'
        // is checked, the button 'buttonName' will be enabled, otherwise it
        // will be disabled
        function enableDisableButton(buttonNamePart, checkboxNamePart) {
          for (var fi = 0; fi < document.forms.length; fi ++) {
            var index = 0;
            while (index < document.forms[fi].elements.length) {
              if (document.forms[fi].elements[index].name.indexOf(
                  checkboxNamePart)!=-1) {
                if (document.forms[fi].elements[index].checked==true) {
                  setDisabled(buttonNamePart, false);
                  return;
                }
              }
              index ++;
            }
          }
          setDisabled(buttonNamePart, true);
        }

        function setDisabled(buttonNamePart, isDisabled) {
          for (var fi = 0; fi < document.forms.length; fi ++) {
            for (var index = 0; index < document.forms[fi].elements.length;
                    index++) {
              if (document.forms[fi].elements[index].name.indexOf(
                      buttonNamePart)==-1) {
              } else {
                document.forms[fi].elements[index].disabled=isDisabled;
              }
            }
          }
        }
        // stop hiding -->
  </script>
</rn:samplePage>
