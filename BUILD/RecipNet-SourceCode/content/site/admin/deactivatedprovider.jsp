<%--
  - Reciprocal Net project
  - deactivatedprovider.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              editprovider.jsp
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 21-Jun-2005: midurbin changed UserIterator error flag name to reflect
  -              changes made in HtmlPageIterator
  - 13-Jul-2005: midurbin added new required parameters to providerPage
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserIterator" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:providerPage title="View Deactivated Provider" labIdParamName="labId"
        providerIdParamName="providerId"
        manageProvidersPageUrl="/admin/manageproviders.jsp"
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
          <rn:providerField fieldCode="<%=ProviderField.NAME%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Provider Head Contact:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.HEAD_CONTACT%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Comments:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.COMMENTS%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Is Active?</th>
        <td class="twoColumnRight">No.</td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Deactivated user accounts associated with this provider:
    </p>
    <rn:userIterator id="inactiveUsers" restrictToActiveUsers="false"
            restrictToInactiveUsers="true" restrictToProviderUsers="true"
            sortByUsername="true"><%-- prevent leading spaces
      --%><ctl:parityChecker includeOnlyOnFirst="true"
              invert="true">,</ctl:parityChecker>
      &nbsp;&nbsp;
      <rn:userField fieldCode="<%=UserField.USER_NAME%>" />
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
    <rn:link href="/admin/manageproviders.jsp"
            contextType="<%=LabContext.class%>">
      Back to Provider Management
    </rn:link>
  </center>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .ancillaryText { font-size: x-small; color: #444444; }
    .headerFont1  { font-size:medium; font-weight: bold; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
  </ctl:styleBlock>
</rn:providerPage>
