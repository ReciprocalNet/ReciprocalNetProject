<%--
  - Reciprocal Net project
  - /lab/modifyfiledescription.jsp
  -
  - 21-Oct-2005: midurbin wrote first draft
  --%>
<%@ page import="org.recipnet.common.controls.HtmlControl" %>
<%@ page import="org.recipnet.common.controls.TextareaHtmlControl" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileField"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:modifyFileDescriptionWapPage title="Modify File Description"
        fileNameParamName="selectedFile"
        editSamplePageHref="/lab/managefiles.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <center>
    <ctl:selfForm>
      <p>
        <table class="table" cellspacing="0">
          <tr>
            <th class="tableHeader">
              Modify description:
            </th>
          </tr>
          <tr>
            <td class="tableInstructions">
              You may update the description for
              "<rn:fileField fieldCode="<%=FileField.FILENAME%>"/>" and click
              "Save" to save the changes.
            </th>
          </tr>
          <tr>
            <td class="tableLabels">description </td>
          </tr>
          <tr class="oddRow">
            <td class="padding">
              <strong>
                <rn:newFileDescription rows="3" columns="32" id="description"/>
              </strong>
            </td>
          </tr>
          <tr>
            <td class="tableInstructions">
              <ctl:errorMessage errorSupplier="<%=description%>"
                  errorFilter="<%=TextareaHtmlControl.VALUE_IS_TOO_LONG%>">
                <p class="errorMessage">
                  The description you entered it too long, please use fewer
                  than 200 characters.
                </p>
              </ctl:errorMessage>
              Comments: <br />
              <rn:wapComments />
            </td>
          </tr>
          <tr>
            <td class="tableInstructions" colspan="3">
              <rn:wapCancelButton /><rn:wapSaveButton label="Save" />
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
</rn:modifyFileDescriptionWapPage>
