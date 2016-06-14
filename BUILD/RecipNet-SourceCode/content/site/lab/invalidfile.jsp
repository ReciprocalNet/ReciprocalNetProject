<%--
  - Reciprocal Net project
  - /lab/invalidfile.jsp
  -
  - 11-Nov-2005: midurbin wrote first draft
  - 08-Jan-2008: ekoperda added handling for 'duplicateFilename' attribute
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Upload Failed">
  <center>
    <p>
      <table class="table" cellspacing="0">
        <tr>
          <th colspan="2" class="tableHeader">
            The file upload operation failed:
          </th>
        </tr>
        <tr>
          <td class="invalidFiles" align="left">
            <div class="reason">
              <ctl:requestAttributeChecker attributeName="invalidDescription"
                includeIfAttributeIsPresent="true">
                The description
                "<span class="quote"><ctl:displayRequestAttributeValue
                        attributeName="invalidDescription" /></span>" is
                not valid or contains illegal characters. 
              </ctl:requestAttributeChecker>
              <ctl:requestAttributeChecker attributeName="invalidFilename"
                  includeIfAttributeIsPresent="true">
                The filename
                "<span class="quote"><ctl:displayRequestAttributeValue
                        attributeName="invalidFilename" /></span>" is invalid
                or contains invalid characters. 
              </ctl:requestAttributeChecker>
              <ctl:requestAttributeChecker attributeName="duplicateFilename"
                  includeIfAttributeIsPresent="true">
                The file named
                "<span class="quote"><ctl:displayRequestAttributeValue
                        attributeName="duplicateFilename" /></span>"
                cannot be uploaded more than once per turn.<br />
                Check for duplicate file names. 
              </ctl:requestAttributeChecker>           
            </div>
          </td>
        </tr>
      </table>
    </p>
  </center>
  <p align="right">
    <ctl:a href="/lab/uploadfilesform.jsp">
      <ctl:requestAttributeParam name="sampleId" attributeName="sampleId" />
      Upload Files (form-based)...
    </ctl:a>
  </p>
  <ctl:styleBlock>
    table.table { border: 3px solid #32357D; }
    th.tableHeader { padding: 0.25em; background: #32357D; 
        font-family: Arial, Helvetica, Verdana; font-weight: bold; 
        font-style: normal; color: #FFFFFF; text-align: left }

    .errorMessage { color: red; font-weight: bold; }
    .reason { margin-left: 2em; }
    .quote { font-weight: bold; }
    td.invalidFiles { padding: 1em; background: #CADBFC; font-family: Arial,
        Helvetica, Verdana; color: #FF0000; }
  </ctl:styleBlock>
</rn:page>
