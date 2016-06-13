<%--
  - Reciprocal Net project
  - /lab/uploadcompleted.jsp
  -
  - 04-Aug-2005: midurbin wrote first draft
  - 10-Nov-2005: midurbin updated references to files.jsp
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator"%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:uploadConfirmationPage title="Upload Completed" secondsUntilRedirect="5"
        redirectTargetPageUrl="/lab/managefiles.jsp">
  <center>
    <p>
      <table class="table" cellspacing="0">
        <tr>
          <th colspan="2" class="tableHeader">
            The upload operation has completed:
          </th>
        </tr>
        <rn:filenameIterator id="fileIt">
          <tr>
            <td class="tableInstructions" align="left" colspan="2">
              <strong><rn:fileField
                      fieldCode="<%=FileField.FILENAME%>" /></strong> -
              <rn:fileField fieldCode="<%=FileField.FILE_SIZE_EXACT%>" />
            </td>
          </tr>
          <ctl:parityChecker includeOnlyOnLast="true">
            <rn:sampleHistoryTranslator translateFromFileContext="true">
              <rn:workflowCommentChecker>
                <tr>
                  <td class="tableInstructions" align="left">
                    <font class="label">Comments</font>:
                    <strong>
                      <rn:sampleHistoryField fieldCode="<%=
                    SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS%>" />
                      &nbsp;       
                    </strong>
                  </td>
                </tr>
              </rn:workflowCommentChecker>
            </rn:sampleHistoryTranslator>
          </ctl:parityChecker>
        </rn:filenameIterator>
        <ctl:errorMessage errorSupplier="<%=fileIt%>"
                errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
          <tr>
            <td class="noFiles" align="center" colspan="2">
              <strong>No files were uploaded!</strong>
            </td>
        </ctl:errorMessage>
      </table>
    </p>
    <p align="right">
      <rn:link href="/lab/managefiles.jsp">Back to "manage files"...</rn:link>
    </p>
  </center>
  <ctl:styleBlock>
    table.table { border: 3px solid #32357D; }
    td.noFiles { padding: 1em; background: #CADBFC; font-family: Arial,
        Helvetica, Verdana; color: #FF0000; }
    th.tableHeader { padding: 0.25em; background: #32357D; 
        font-family: Arial, Helvetica, Verdana; font-weight: bold; 
        font-style: normal; color: #FFFFFF; text-align: left }
    td.tableLabels { padding: 0.25em; background: #656BFA; 
        font-family: Arial, Helvetica, Verdana; font-style: italic; 
        color: #FFFFFF; text-align: center; }
    td.tableInstructions { padding: 0.25em; background: #CADBFC;
        font-family: Arial, Helvetica, Verdana; color: #000050; }
  </ctl:styleBlock>
</rn:uploadConfirmationPage>
