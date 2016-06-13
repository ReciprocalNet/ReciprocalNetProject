<%--
  - Reciprocal Net project
  - /lab/removefiles.jsp
  -
  - 04-Aug-2005: midurbin wrote first draft
  - 21-Oct-2005: midurbin updated references to files.jsp
  --%>
<%@ page import="org.recipnet.common.controls.HtmlControl" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:renameFileWapPage title="Rename File" fileNameParamName="selectedFile"
        editSamplePageHref="/lab/managefiles.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <center>
    <ctl:selfForm>
      <p>
        <table class="table" cellspacing="0">
          <tr>
            <th colspan="3" class="tableHeader">
              Rename file:
            </th>
          </tr>
          <tr>
            <td class="tableLabels">current file name</td>
            <td class="tableLabels">&nbsp;</td>
            <td class="tableLabels">new file name</td>
          </tr>
          <tr class="oddRow">
            <td class="padding">
              <strong>
                <rn:fileField fieldCode="<%=FileField.FILENAME%>" />
              </strong>
            </td>
            <td class="padding">
              <img src="../images/rename-arrow.gif" />
            </td>
            <td class="padding">
              <rn:newFileName required="true" id="newName" />
            </td>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="3">
              <ctl:errorMessage errorSupplier="<%=newName%>"
                  errorFilter="<%=HtmlControl.REQUIRED_VALUE_IS_MISSING%>">
                <p class="errorMessage">
                  You must enter a new filename if you wish to rename the file.
                </p>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=newName%>"
                  errorFilter="<%=HtmlControl.VALIDATOR_REJECTED_VALUE%>">
                <p class="errorMessage">
                  You have entered an invalid filename or one that contains
                  invalid characters.
                </p>
              </ctl:errorMessage>
              Comments: <br />
              <rn:wapComments />
            </td>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="3">
              <rn:wapCancelButton /><rn:wapSaveButton label="Rename" />
            </td>
          </tr>
        </table>
      </p>
    </center>
  </ctl:selfForm>
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
    tr.oddRow { padding: 0.25em; background: #F0F0F0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
    td.padding { padding: 0.25em; }
  </ctl:styleBlock>
</rn:renameFileWapPage>
