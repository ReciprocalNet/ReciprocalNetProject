<%--
  - Reciprocal Net project
  - /lab/removefiles.jsp
  -
  - 04-Aug-2005: midurbin wrote first draft
  - 21-Oct-2005: midurbin updated references to files.jsp
  - 28-Oct-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@ page import="org.recipnet.site.content.rncontrols.RemoveFilesWapPage"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:removeFilesWapPage title="Remove Files" filenameParamName="filename"
        editSamplePageHref="/lab/managefiles.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <center>
    <ctl:selfForm>
      <table class="table" cellspacing="0" width="400">
        <tr>
          <th colspan="2" class="tableHeader">
            Remove selected files
            </th>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="2" align="left">
              <p>
                The following files have been selected for removal.
              </p>
              Please verify that you wish to remove these files, enter comments
              in the field below and press 'Remove'.
            </td>
          </tr>
          <tr>
            <td class="tableLabels">filename</td>
            <td class="tableLabels">size</td>
          </tr>
          <rn:filenameIterator id="fileIt">
            <tr class="${rn:testParity(fileIt.iterationCountSinceThisPhaseBegan,
                                       'evenRow', 'oddRow')}">
              <td>
                <strong><rn:fileField
                        fieldCode="<%=FileField.FILENAME%>" /></strong>
              </td>
              <td>
                <rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" />
              </td>
            </tr>
          </rn:filenameIterator>
          <tr>
            <td class="tableLabels" colspan="2">
              There are
              <strong>
                <ctl:iterationCount htmlPageIterator="<%=fileIt%>" />
              </strong> files, totalling
              <strong>
                <rn:cumulativeByteCount fileIterator="<%=fileIt%>" />
              </strong> bytes.
            </td>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="2">
              Comments: <br />
              <rn:wapComments />
            </td>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="2">
              <rn:wapCancelButton /><rn:wapSaveButton label="Remove" />
            </td>
          </tr>
        </table>
      </center>
    </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage { color: red; }
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
    tr.evenRow { padding: 0.25em; background: #D0D0D0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
  </ctl:styleBlock>
</rn:removeFilesWapPage>
