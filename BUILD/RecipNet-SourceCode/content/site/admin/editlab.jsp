<%--
  - Reciprocal Net project
  - editlab.jsp
  -
  - 22-Jul-2002: hclin wrote first draft
  - 09-Aug-2002: hclin fixed bug #295
  - 14-Aug-2002: hclin fixed bug #355
  - 21-Aug-2002: eisiorho removed TODO, task #373
  - 13-Sep-2002: jobollin corrected title capitalization per task 332
  - 17-Oct-2002: eisiorho changed UI of action deleting a Lab.
  - 15-Nov-2002: eisiorho fixed bug #600
  - 21-Jan-2002: adharurk improved the way in which active and deactive users
  -              are displayed, task #586
  - 14-Mar-2003: ajooloor removed spaces from textarea
  - 28-Mar-2003: ajooloor fixed bug #836
  - 17-Apr-2003: eisiorho added hyperlink to managelabs.jsp
  - 23-Apr-2003: ajooloor altered call to PageHelper.writeUpdatedLabInfo()
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 01-Jul-2003: dfeng added support to detect html tags in forms
  - 28-Jul-2003: dfeng added warning message to changing lab directory name
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 07-Dec-2004: midurbin rewrote using custom tags, factoring some code to
  -              deactivatedlab.jsp
  - 14-Dec-2004: midurbin added an invalid lab directory name error message
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 18-May-2005: midurbin updated link parameters to reflect UserPage spec
  -              changes
  - 21-Jun-2005: midurbin changed UserIterator error flag name to reflect
  -              changes made in HtmlPageIterator
  - 13-Jul-2005: midurbin added new required parameters to labPage
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.SiteField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserIterator" %>

<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:labPage title="Edit Lab" labIdParamName="labId"
        manageLabsPageUrl="/admin/managelabs.jsp"
        labDirNameChangeConfirmationParamName="confirmNameChange"
        loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason" >
  <br />
  <ctl:selfForm>
    <table class="adminFormTable" border="0" cellspacing="0">
      <tr>
        <th class="twoColumnLeft">Lab ID:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.ID%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Home Site (ID):</th>
        <td  class="twoColumnRight">
          <rn:siteField fieldCode="<%=SiteField.NAME%>" />
          (<rn:siteField fieldCode="<%=SiteField.ID%>" />)
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Name:</th>
        <td class="twoColumnRight">
          <rn:labField editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Short Name:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.SHORT_NAME%>" editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Home URL:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.HOME_URL%>" editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Directory Name:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.DIRECTORY_NAME%>"
                  editable="true" id="labDirName" />
        </td>
      </tr>
      <ctl:errorMessage errorFilter="<%=LabField.INVALID_LAB_DIRECTORY_NAME%>"
              errorSupplier="<%=labDirName%>" >
        <tr>
          <td class="twoColumnLeft">&nbsp;</td>
          <td class="twoColumnRight">
            <div class="errorMessage">
              <strong>The lab directory name is not valid!</strong>
              <br />
              (lab directory names may not contain certain special characters
              and may not be equal to 'temp' or 'cvs')
            </div>
          </td>
        </tr>
      </ctl:errorMessage>
      <ctl:errorMessage errorFilter="<%=
              LabPage.DIRECTORY_NAME_CHANGED_WITHOUT_CONFIRMATION%>">
        <tr>
          <td class="twoColumnLeft">&nbsp;</td>
          <td class="twoColumnRight">
            <div class="confirmationBox">
              <div class="errorMessage">
                You are about to change the Directory Name for this lab.&nbsp;
                &nbsp; This may have repercussions throughout the Reciprocal Net
                Site Network and should be undertaken only after consultation
                with Reciprocal Net technical support.&nbsp;&nbsp; Changing this
                value inappropriately may lead to inaccessibility of data files
                associated with samples originated by this lab.
              </div>
              <br />
              If you wish to proceed, check the box below and click
              'Submit Changes', otherwise hit 'Cancel' to return to the manage
              labs page.
              <br />
              <br />
              <ctl:checkbox id="confirmNameChange" />
              Yes, I understand the consequences and wish to proceed.
            </div>
          </td>
        </tr>
      </ctl:errorMessage>
      <tr>
        <th class="twoColumnLeft">Default Copyright Notice:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.DEFAULT_COPYRIGHT_NOTICE%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <ctl:errorMessage
          errorFilter="<%= LabPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>">
        <tr>
          <td colspan="2" class="oneColumn">
            <div class="errorMessage">
              * Missing or invalid entry.&nbsp;&nbsp;
              Please check these entries and resubmit.
            </div>
          </td>
        </tr>
      </ctl:errorMessage>
      <tr>
        <th class="twoColumnLeft">Submit your changes: </th>
        <td class="twoColumnRight">
          <rn:labActionButton labFunction="<%=LabPage.EDIT_EXISTING_LAB%>"
                  label="Submit Changes" />
          <rn:labActionButton label="Cancel" labFunction="<%=
                  LabPage.CANCEL_LAB_FUNCTION%>" />
        </td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Active user accounts associated with this lab:
    </p>
    <table class="adminFormTable" border="0" cellspacing="0">
      <tr class="headerColor">
        <th class="threeColumn">User full name</th>
        <th class="threeColumn">Username</th>
        <th>&nbsp;</th>
      </tr>
      <rn:userIterator id="activeUsers" restrictToActiveUsers="true"
             restrictToLabUsers="true" sortByUsername="true">
        <tr class="${rn:testParity(activeUsers.iterationCountSinceThisPhaseBegan,
                                   'rowColortrue', 'rowColorfalse')}">
          <td class="threeColumn">
            <rn:userField />
          </td>
          <td class="threeColumn">
            <rn:userField fieldCode="<%=UserField.USER_NAME%>" />
          </td>
          <td class="threeColumn">
            <rn:link href="/admin/edituser.jsp" contextType="<%=
                    UserContext.class%>">
              <ctl:currentPageLinkParam name="labOrProviderPage" />
              Edit this user...
            </rn:link>
          </td>
        </tr>
      </rn:userIterator>
      <ctl:errorMessage errorSupplier="<%=activeUsers%>"
               errorFilter="<%=UserIterator.NO_ITERATIONS%>">
        <tr>
          <td colspan="3" class="noUsers">
            There are no active users associated with this lab.
          </td>
        </tr>
      </ctl:errorMessage>
      <tr class="headerColor">
        <td colspan="3" class="threeColumn">
          <rn:link href="/admin/adduser.jsp" contextType="<%=
                  LabContext.class%>">
            <ctl:currentPageLinkParam name="labOrProviderPage" />
            Add a new user...
          </rn:link>
        </td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Deactivated user accounts associated with this lab:
    </p>
    <rn:userIterator id="inactiveUsers" restrictToActiveUsers="false"
            restrictToInactiveUsers="true" restrictToLabUsers="true"
            sortByUsername="true"><%-- prevent leading spaces
      --%><ctl:parityChecker
              includeOnlyOnFirst="true" invert="true">,</ctl:parityChecker>
      &nbsp;&nbsp;
      <rn:link href="/admin/deactivateduser.jsp" contextType="<%=
              UserContext.class%>">
        <ctl:currentPageLinkParam name="labOrProviderPage" />
        <rn:userField fieldCode="<%=UserField.USER_NAME%>" />
      </rn:link>
      <font class="ancillaryText">(<rn:userField />)</font><%-- 
            --- prevent trailing spaces ---
    --%></rn:userIterator>
    <ctl:errorMessage errorSupplier="<%=inactiveUsers%>"
            errorFilter="<%=UserIterator.NO_ITERATIONS%>">
      There are no deactivated users associated with this lab.
    </ctl:errorMessage>
  </ctl:selfForm>
  <br />
  <br />
  <center>
    <rn:link href="/admin/managelabs.jsp" contextType="<%=
            LabContext.class%>">Back to Lab Management</rn:link>
  </center>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .ancillaryText { font-size: x-small; color: #444444; }
    .confirmationBox { font-size: x-small; width: 300px; border-style: dashed;
            border-width: thin; border-color: #FF0000; padding: 0.25em; }
    .headerFont1  { font-size:medium; font-weight: bold; }
    .rowColorfalse { background-color: #E6E6E6 }
    .rowColortrue  { background-color: #FFFFFF }
    .headerColor   { background-color: #CCCCCC }
    .noUsers { text-align: center; padding: 1.5em; border-width: 0; }
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
    .threeColumn   { text-align: left; padding: 0.05in; border-width: 0; }
    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }
  </ctl:styleBlock>
</rn:labPage>
