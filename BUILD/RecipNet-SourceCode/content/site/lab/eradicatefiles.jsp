<%--
  - Reciprocal Net project
  - /lab/removefiles.jsp
  -
  - 04-Aug-2005: midurbin wrote first draft
  - 21-Oct-2005: midurbin updated link to managefiles.jsp
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@ page import="org.recipnet.site.content.rncontrols.RemoveFilesWapPage"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:eradicateFilesWapPage title="Eradicate File" filenameParamName="filename"
        editSamplePageHref="/lab/managefiles.jsp"
        loginPageUrl="/login.jsp" currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <center>
    <ctl:selfForm>
      <table class="warningTable" cellspacing="0" width="400">
        <tr>
          <th colspan="2" class="warningTableHeader">
            Eradicate selected files
          </th>
        </tr>
        <tr>
          <td class="warningTableInstructions" colspan="2" align="left">
            <p>
              The following files have been selected for eradication.
            </p>
            If you are certain that you wish to eradicate these files enter
            comments in the field below and press the 'Eradicate' button.
          </td>
        </tr>
        <tr>
          <td class="warningTableLabels">filename</td>
          <td class="warningTableLabels">size</td>
        </tr>
        <rn:filenameIterator id="fileIt">
          <tr class="${rn:testParity(fileIt.iterationCountSinceThisPhaseBegan,
                                     'evenRow', 'oddRow')}">
            <td>
              <strong>
                <rn:fileField fieldCode="<%=FileField.FILENAME%>" />
              </strong>
            </td>
            <td>
              <rn:fileField fieldCode="<%=FileField.FILE_SIZE%>" />
            </td>
          </tr>
        </rn:filenameIterator>
        <tr>
          <td class="warningTableLabels" colspan="2">
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
          <td class="warningTableInstructions" colspan="2">
            Comments: <br />
            <rn:wapComments />
          </td>
        </tr>
        <tr>
          <td class="warningTableInstructions" colspan="2">
            <font class="errorMessage">
              When a file is eradicated every version of it is
              <strong>permanently removed</strong> from the repository and
              the action cannot be undone.  This defeats Reciprocal Net's
              sample versioning mechanism and it is strongly recommended that
              you Remove the files instead.
              <strong>
                Eradicating these files will free
                <rn:cumulativeByteCount fileIterator="<%=fileIt%>"
                    useAggregateSize="true" /> bytes in the repository.
              </strong>
            </font>
          </td>
        </tr>
        <tr>
          <td class="warningTableInstructions" colspan="2">
            <rn:wapCancelButton /><rn:wapSaveButton label="Eradicate" />
          </td>
        </tr>
      </table>
    </ctl:selfForm>
  </center>
  <ctl:styleBlock>
    .errorMessage { color: red; }
    table.warningTable { border: 3px solid #FF0000;}
    th.warningTableHeader { padding: 0.25em; background: #FF0000; 
        font-family: Arial, Helvetica, Verdana; font-weight: bold; 
        font-style: normal; color: #FFFFFF; text-align: left }
    td.warningTableLabels { padding: 0.25em; background: #606060; 
        font-family: Arial, Helvetica, Verdana; font-style: italic; 
        color: #FFFFFF; text-align: center; }
    tr.oddRow { background: #F0F0F0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #500000; }
    tr.evenRow { padding: 0.25em; background: #D0D0D0; text-align: center;
        font-family: Arial, Helvetica, Verdana; color: #500000; }
    td.warningTableInstructions { padding: 0.25em; background: #000000;
        font-family: Arial, Helvetica, Verdana; color: #FFF0F0; }
  </ctl:styleBlock>
</rn:eradicateFilesWapPage>
