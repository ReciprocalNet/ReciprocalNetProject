<%--
  - Reciprocal Net project
  - editprovider.jsp
  -
  - 22-Jul-2002: hclin wrote first draft
  - 09-Aug-2002: hclin fixed bug #295
  - 14-Aug-2002: hclin fixed bug #354
  - 21-Aug-2002: eisiorho removed TODO, task #373
  - 13-Sep-2002: jobollin corrected title capitalization per task 332
  - 17-Oct-2002: eisiorho changed UI of action deleting a Provider Site.
  - 06-Dec-2002: eisiorho fixed bug #636
  - 06-Dec-2002: eisiorho added code to check for empty text boxes on form
  -              post
  - 21-Jan-2003: adharurk improved the way in which active and deactivated
  -              users for a specific provider are displayed task #586
  - 14-Mar-2003: ajooloor removed spaces in textarea
  - 18-Mar-2003: yli removed extra spaces in "Provider Head Contact" box
  - 20-Mar-2003: dfeng fixed bug #799 that deleted providerInfo fields
  -              when a deactivation is cancelled. Fixed link to edituser.jsp
  -              by reformating jsp. Corrected the behavior in provider
  -              deactivation
  - 03-Apr-2003: dfeng fixed bug #851
  - 17-Apr-2003: eisiorho added hyperlink to manageproviders.jsp
  - 22-Apr-2003: dfeng altered call to PageHelper.writeUpdatedProviderInfo()
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 01-Jul-2003: dfeng added support to detect html tags within forms
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 30-Nov-2004: midurbin rewrote using custom tags, factoring out code for
  -              the different page modes to addprovider.jsp,
  -              deactivateprovider.jsp and deactivatedprovider.jsp
  - 17-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 18-May-2005: midurbin updated link parameters to reflect UserPage spec
  -              changes
  - 21-Jun-2005: midurbin changed UserIterator error flag name to reflect
  -              changes made in HtmlPageIterator
  - 13-Jul-2005: midurbin added new required parameters to providerPage
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserIterator" %>

<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:providerPage title="Edit Provider" labIdParamName="labId"
        providerIdParamName="providerId"
        manageProvidersPageUrl="/admin/manageproviders.jsp"
        confirmDeactivationPageUrl="/admin/deactivateprovider.jsp"
        loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason" >
  <br />
  <ctl:selfForm>
    <table class="adminFormTable">
      <tr>
        <th class="twoColumnLeft">Provider ID:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.ID%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Name (ID):</th>
        <td  class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.NAME%>" displayAsLabel="true" />
          (<rn:labField fieldCode="<%=LabField.ID%>" />)
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Provider Name:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.NAME%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Provider Head Contact:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.HEAD_CONTACT%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Comments:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.COMMENTS%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <ctl:errorMessage
          errorFilter="<%= ProviderPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>">
        <tr>
          <td colspan="2" class="oneColumn">
            <div class="errorMessage">
              * Missing or invalid entry.&nbsp;&nbsp;
              Please check these entries and
              resubmit.
            </div>
          </td>
        </tr>
      </ctl:errorMessage>
      <tr>
        <th class="twoColumnLeft">Submit your changes: </th>
        <td class="twoColumnRight">
          <rn:providerActionButton
                  providerFunction="<%=ProviderPage.EDIT_EXISTING_PROVIDER%>"
                  label="Submit Changes" />
          <rn:providerActionButton label="Cancel" providerFunction="<%=
                  ProviderPage.CANCEL_PROVIDER_FUNCTION%>" />
        </td>
      </tr>
      <tr>
        <td colspan="2"> <hr /> </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Deactivate this provider:</th>
        <td class="twoColumnRight">
          <rn:providerActionButton providerFunction="<%=
                  ProviderPage.CONSIDER_DEACTIVATION%>" label="Deactivate" />
        </td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Active user accounts associated with this provider:
    </p>
    <table class="adminFormTable" border="0" cellspacing="0">
      <tr class="headerColor">
        <th class="threeColumnLeft">User full name</th>
        <th class="threeColumnMiddle">Username</th>
        <th>&nbsp;</th>
      </tr>
      <rn:userIterator id="activeUsers" restrictToActiveUsers="true"
             restrictToProviderUsers="true" sortByUsername="true">
        <tr class="${rn:testParity(activeUsers.iterationCountSinceThisPhaseBegan,
                                   'rowColortrue', 'rowColorfalse')}">
          <td class="threeColumnLeft">
            <rn:userField />
          </td>
          <td class="threeColumnMiddle">
            <rn:userField fieldCode="<%=UserField.USER_NAME%>" />
          </td>
          <td class="threeColumnLeft">
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
            There are no active users associated with this provider.
          </td>
        </tr>
      </ctl:errorMessage>
      <tr class="headerColor">
        <td colspan="3" class="threeColumnLeft">
          <rn:link href="/admin/adduser.jsp" contextType="<%=
                  ProviderContext.class%>">
            <ctl:currentPageLinkParam name="labOrProviderPage" />
            Add a new user...
          </rn:link>
        </td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Deactivated user accounts associated with this provider:
    </p>
    <rn:userIterator id="inactiveUsers" restrictToActiveUsers="false"
            restrictToInactiveUsers="true" restrictToProviderUsers="true"
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
      There are no deactivated users associated with this provider.
    </ctl:errorMessage>
  </ctl:selfForm>
  <br />
  <br />
  <center>
    <rn:link href="/admin/manageproviders.jsp" contextType="<%=
            LabContext.class%>">Back to Provider Management</rn:link>
  </center>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .ancillaryText { font-size: x-small; color: #444444; }
    .headerFont1  { font-size:medium; font-weight: bold; }
    .rowColorfalse { background-color: #E6E6E6 }
    .rowColortrue  { background-color: #FFFFFF }
    .headerColor   { background-color: #CCCCCC }
    .noUsers { text-align: center; padding: 1.5em; border-width: 0; }
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
    .threeColumnLeft   { text-align: left; padding: 0.05in; border-width: 0; }
    .threeColumnMiddle { text-align: left;  padding: 0.05in; border-width: 0;}
    .threeColumnRight  { text-align: left;  padding: 0.05in; border-width: 0; }
    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }
  </ctl:styleBlock>
</rn:providerPage>
