<%--
   - Reciprocal Net project
   -
   - lab/managefiles.jsp
   -
   - 10-Jun-2003: midurbin wrote first draft
   - 21-Jul-2003: ajooloor fixed bug #968
   - 22-Jul-2003: ekoperda fixed bug #988 related to upload processing
   - 11-Aug-2003: ajooloor fixed bug #1010
   - 07-Jan-2004: ekoperda changed package references due to source tree
   -              reorganization
   - 31-Mar-2004: midurbin clarified wording for the "allow overwrite" option
   - 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
   - 20-Apr-2005: ekoperda added hyperlink to new page uploader.jsp
   - 04-Aug-2005: midurbin rewrote using custom tags
   - 21-Oct-2005: midurbin added file description support; changed jsp name
   -              to managefiles.jsp from files.jsp; factored out file upload
   -              code
   - 27-Oct-2005: midurbin added a column for file modification date
   - 17-Feb-2006: jobollin made this page disable file actions for files
   -              without history
   - 08-Mar-2006: jobollin notes that an updated version of this page was
   -               inadvertently committed with Task #1095, even though the
   -              changes made in that version had nothing to do with that task
   - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
   --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField" %>
<%@ page
    import="org.recipnet.site.content.rncontrols.SelectFilesForActionPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:selectFilesForActionPage title="Manage Files"
        loginPageUrl="/login.jsp" ignoreSampleHistoryId="true"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <rn:authorizationChecker canEditSample="true" invert="true"
      suppressRenderingOnly="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE%>"/>
  </rn:authorizationChecker>
  <rn:sampleChecker includeIfRetracted="true">
    <center>
      <font color="red">
        This sample has been retracted by its lab and cannot have file actions
        performed on it.
      </font>
    </center>
    <br />
  </rn:sampleChecker>
  <rn:sampleChecker includeIfRetracted="true" invert="true">
    <ctl:extraHtmlAttribute name="onLoad"
        value="enableDisableButton('Button', 'selectedFiles')" />
    <table width="100%">
      <tr>
        <td valign="top" align="center">
          <ctl:selfForm>
            <table class="table" cellspacing="0">
              <tr>
                <th colspan="9" class="tableHeader">
                  Repository files:
                </th>
              </tr>
              <tr>
                <td class="tableLabels">selected</td>
                <td class="tableLabels">filename</td>
                <td class="tableLabels">size</td>
                <td class="tableLabels">most recent modification date</td>
                <td class="tableLabels" width="200" align="left">
                  description
                </td>
                <td class="tableLabels">&nbsp;</td>
                <td class="tableLabels">&nbsp;</td>
                <td class="tableLabels">&nbsp;</td>
                <td class="tableLabels">&nbsp;</td>
              </tr>
              <rn:fileIterator id="fileIt">
                <tr class="${rn:testParity(fileIt.iterationCountSinceThisPhaseBegan,
                                           'evenRow', 'oddRow')}">
                  <td class="common">
                    <ctl:errorChecker errorSupplier="${actionDateField}"
                      invert="true">
                      <rn:fileSelectionCheckbox id="selectedFiles">
                        <ctl:extraHtmlAttribute name="onClick"
                                value="enableDisableButton('Button',
                                        'selectedFiles')" />
                      </rn:fileSelectionCheckbox>
                    </ctl:errorChecker>
                    <ctl:errorChecker errorSupplier="${actionDateField}">
                      &nbsp;
                    </ctl:errorChecker>
                  </td>
                  <td class="common" align="left">
                    <rn:fileField fieldCode="<%=FileField.LINKED_FILENAME%>" />
                  </td>
                  <td class="common" align="right">
                    <rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" />
                    &nbsp;&nbsp;
                  </td>
                  <td class="common" width="200" align="center">
                    <rn:sampleHistoryTranslator
                        translateFromFileContext="true">
                      <rn:sampleHistoryField id="actionDateField" fieldCode="<%=
                          SampleHistoryField.FieldCode.ACTION_DATE%>" 
                          requireHistory="false"
                          noHistoryText="unknown"/>
                    </rn:sampleHistoryTranslator>
                  </td>
                  <td class="common" width="200" align="left">
                    <rn:fileField fieldCode="<%=FileField.DESCRIPTION%>" />
                  </td>
                  <ctl:errorChecker errorSupplier="${actionDateField}"
                    invert="true">
                    <td class="common">
                        <rn:link href="/lab/removefiles.jsp">
                          <rn:fileParam name="filename" />remove
                        </rn:link>
                    </td>
                    <td class="common">
                      <rn:link href="/lab/renamefile.jsp">
                        <rn:fileParam name="selectedFile" />rename
                      </rn:link>
                    </td>
                    <td class="common">
                      <rn:link href="/lab/eradicatefiles.jsp">
                        <rn:fileParam name="filename" />eradicate
                      </rn:link>
                    </td>
                    <td class="common">
                      <rn:fileChecker includeOnlyIfFileHasDescription="true">
                        <rn:link href="/lab/modifyfiledescription.jsp">
                          <rn:fileParam name="selectedFile" />
                          edit description
                        </rn:link>
                      </rn:fileChecker>
                      <rn:fileChecker includeOnlyIfFileHasDescription="true"
                          invert="true">
                        <rn:link href="/lab/modifyfiledescription.jsp">
                          <rn:fileParam name="selectedFile" />add description
                        </rn:link>
                      </rn:fileChecker>
                    </td>
                  </ctl:errorChecker>
                  <ctl:errorChecker errorSupplier="${actionDateField}">
                    <td class="common">
                        <rn:link href="/lab/removefiles.jsp" visible="false">
                          <rn:fileParam name="filename" />remove
                        </rn:link>
                    </td>
                    <td class="common">
                      <rn:link href="/lab/renamefile.jsp" visible="false">
                        <rn:fileParam name="selectedFile" />rename
                      </rn:link>
                    </td>
                    <td class="common">
                      <rn:link href="/lab/eradicatefiles.jsp" visible="false">
                        <rn:fileParam name="filename" />eradicate
                      </rn:link>
                    </td>
                    <td class="common">
                      <rn:fileChecker includeOnlyIfFileHasDescription="true">
                        <rn:link href="/lab/modifyfiledescription.jsp"
                          visible="false">
                          <rn:fileParam name="selectedFile" />
                          edit description
                        </rn:link>
                      </rn:fileChecker>
                      <rn:fileChecker includeOnlyIfFileHasDescription="true"
                          invert="true">
                        <rn:link href="/lab/modifyfiledescription.jsp"
                          visible="false">
                          <rn:fileParam name="selectedFile" />add description
                        </rn:link>
                      </rn:fileChecker>
                    </td>
                  </ctl:errorChecker>
                </tr>
              </rn:fileIterator>
              <ctl:errorMessage errorSupplier="${fileIt}"
                  errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
                <tr class="oddRow">
                  <td class="nofiles" colspan="9">
                    There are currently no files in the repository.
                  </td>
                </tr>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="${fileIt}"
                  errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>"
                  invertFilter="true">
                <tr>
                  <td class="tableInstructions" colspan="9">
                    <ctl:errorMessage errorFilter="<%=
                            SelectFilesForActionPage.NO_FILES_SELECTED%>">
                     <p class="errorMessage">
                       You must select at least one file by clicking the
                        checkbox to the left of its name before clicking any of
                        the buttons below.
                      </p>
                    </ctl:errorMessage>
                    <rn:initActionOnFilesButton id="removeButton"
                        label="Remove selected files..."
                        targetExtendedOpWapPageUrl="/lab/removefiles.jsp" />
                    <rn:initActionOnFilesButton id="eradicateButton"
                        label="Eradicate selected files..."
                        targetExtendedOpWapPageUrl="/lab/eradicatefiles.jsp"
                            />
                  </td>
                </tr>
              </ctl:errorMessage>
            </table>
          </ctl:selfForm>
        </td>
      </tr>
    </table>
    <p align="right">
      <rn:link href="/lab/sample.jsp">Back to "Edit Sample"...</rn:link>
      <br />
      <rn:link href="/jamm.jsp">JaMM visualization...</rn:link>
      <br />
      <rn:link href="/lab/uploadfilesform.jsp">
        Upload files (form-based)...
      </rn:link>
      <br />
      <rn:link href="/lab/uploadfilesapplet.jsp">
        Upload files (Drag-and-Drop)...
      </rn:link>
    </p>
  </rn:sampleChecker>
  <ctl:styleBlock>
    p.errorMessage { color: red; }
    table.table { border: 3px solid #32357D; }
    th.tableHeader { padding: 0.25em; background: #32357D; 
        font-family: Arial, Helvetica, Verdana; font-weight: bold; 
        font-style: normal; color: #FFFFFF; text-align: left }
    td.tableLabels { padding: 0.25em; background: #656BFA; 
        font-family: Arial, Helvetica, Verdana; font-style: italic; 
        color: #FFFFFF; text-align: center; }
    td.tableInstructions { padding: 0.25em; background: #CADBFC;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
    td.common { padding: 0.25em; vertical-align: top; }
    td.nofiles { padding: 0.25em; color: #707070; font-style: italic;
        text-align: center; }
    tr.oddRow { background: #F0F0F0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
    tr.evenRow { background: #D0D0D0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
    font.normal { font-family: Arial, Helvetica, Verdana; font-style: normal; }
    font.description { font-family: Arial, Helvetica, Verdana;
        font-style: italic; font-size: small; color: #505050; }
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
        function setRemoteName(remoteNameBoxId, localNameBoxId) {
            if (manuallyUpdatedFieldNames.indexOf(remoteNameBoxId)!=-1) {
              // the box shouldn't be updated
              return;
            } else {
              var localPath = getElementById(localNameBoxId).value;
              var lastSlash = localPath.length;
                for (var j = localPath.length; j >= 0; j -- ) {
                  if (localPath.charAt(j) == '/'
                      || localPath.charAt(j) == '\\') {
                    lastSlash = j + 1;
                    j = 0;
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
</rn:selectFilesForActionPage>
