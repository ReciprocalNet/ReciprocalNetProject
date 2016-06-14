<%--
   - Reciprocal Net project
   - jamm.jsp
   -
   - 30-Jul-2002: hclin wrote first draft
   - 07-Aug-2002: hclin fixed bug #293
   - 13-Sep-2002: jobollin corrected title capitalization per task 332
   - 29-Oct-2002: jobollin adjusted applet parameters as part of task 510
   - 05-Nov-2002: jobollin adjusted applet parameters further and performed
   -              minor general modifications
   - 20-Nov-2002: jobollin modified JammSupport URLs
   - 17-Dec-2002: jobollin removed applet parameters specifying table.dat as
   -              part of task #509
   - 31-Mar-2003: yli added special handling for zero crt files
   - 30-May-2003: midurbin modified page to utilize FileHelper and to 
   -              support versioning.
   - 06-Jun-2003: midurbin modified page to accomodate changes to FileHelper
   - 11-Jul-2003: jrhanna removed excess references to userId from jamm 
   -              invocations 
   - 22-Aug-2003: jobollin fixed bug #1027
   - 07-Jan-2004: ekoperda changed package references due to source tree
   -              reorganization
   - 22-Apr-2004: midurbin fixed bug #1195
   - 18-Aug-2004: cwestnea rewrote using custom tags
   - 12-Apr-2005: midurbin added errorMessage tags to suppress jamm applet
   -              options and links when jamm is unavailable, and removed
   -              redundant error messages
   - 13-Jul-2005: midurbin added new required parameters to SamplePage
   - 23-Sep-2005: midurbin added ShowsampleLink
   - 21-Oct-2005: midurbin added file descriptions
   - 07-Feb-2006: jobollin updated to accommodate JammAppletTag revisions and
   -              the new JammModelElement
   - 20-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField" %>
<%@ page import="org.recipnet.site.content.rncontrols.JammModelElement" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:samplePage title="JaMM Viewer" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <table style="width: 100%;">
    <tr>
      <td style="width: 600px;"> 
        <rn:jammModel id="jammModel" repositoryFile="${param['crtFile']}">
          <rn:jammApplet id="jamm" width="600" height="500" appletParam="jamm"/>
          <ctl:errorMessage 
              errorFilter="<%= JammModelElement.NO_CRT_FILE_AVAILABLE %>">
            <p class="errorMessage">
              No .crt file for this sample is currently available.
            </p>
          </ctl:errorMessage>
          <ctl:errorMessage 
              errorFilter="<%= JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND %>">
            <p class="errorMessage">
              The repository directory for this sample was not found.
            </p>
          </ctl:errorMessage>
        </rn:jammModel>
      </td>
      <td style="padding-left: 1em;">
        <rn:fileIterator id="fileIt" filterByExtension="crt">
          <ctl:parityChecker includeOnlyOnFirst="true">
          <table>
          </ctl:parityChecker>
            <tr>
              <td>
                <rn:jammAppletPreservingLink 
                    jammAppletTag="${jamm}" 
                    switchCrtFileUsingFileContext="${fileIt}" 
                    useCrtFileAsLinkText="true" 
                    disableWhenLinkMatchesAppletTag="true" />
              </td>
              <rn:fileChecker includeOnlyIfFileHasDescription="true">
                <td>-</td>
                <td>
                  <rn:fileField fieldCode="<%=FileField.DESCRIPTION%>" />
                </td>
              </rn:fileChecker>
            </tr>
          <ctl:parityChecker includeOnlyOnLast="true">
          </table>
          </ctl:parityChecker>
        </rn:fileIterator>
        <ctl:errorMessage errorSupplier="${jammModel}" invertFilter="true"
            errorFilter="<%=(JammModelElement.NO_CRT_FILE_AVAILABLE
                    | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND)%>">
          <div style="margin-top: 2.5em;">
            <rn:showsampleLink basicPageUrl="/showsamplebasic.jsp"
                   detailedPageUrl="/showsampledetailed.jsp"
                   sampleIsKnownToBeLocal="true">
              <rn:sampleParam name="sampleId" />
              <rn:sampleHistoryParam name="sampleHistoryId" />
              Sample Information...
            </rn:showsampleLink><br />
            <rn:authorizationChecker canEditSample="true" 
                suppressRenderingOnly="true">
              <rn:link href="/lab/sample.jsp">Edit Sample...</rn:link>
            </rn:authorizationChecker>
          </div>
        </ctl:errorMessage>
      </td>
    </tr>
  </table>
  <ctl:errorMessage errorSupplier="${jammModel}" invertFilter="true"
      errorFilter="<%=(JammModelElement.NO_CRT_FILE_AVAILABLE
              | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND)%>">
    <hr />
    <rn:jammAppletPreservingLink 
        jammAppletTag="${jamm}" 
        switchApplet="JaMM1" 
        disableWhenLinkMatchesAppletTag="true"
      >JaMM1</rn:jammAppletPreservingLink>
    - works with any Java-capable browser<br />
    <rn:jammAppletPreservingLink 
        jammAppletTag="${jamm}" 
        switchApplet="JaMM2" 
        disableWhenLinkMatchesAppletTag="true"
      >JaMM2</rn:jammAppletPreservingLink>
    - requires Sun Java plug-in 1.2 or higher (1.4 recommended)<br />
    <rn:jammAppletPreservingLink 
        jammAppletTag="${jamm}" 
        switchApplet="miniJaMM" 
        disableWhenLinkMatchesAppletTag="true"
      >miniJaMM</rn:jammAppletPreservingLink>
    - works with any Java-capable browser<br />
  </ctl:errorMessage>
  </div>
  <ctl:styleBlock>
    div.pageBody { text-align: left; }
    .indent { margin-left: 1em; }
  </ctl:styleBlock>
</rn:samplePage>
