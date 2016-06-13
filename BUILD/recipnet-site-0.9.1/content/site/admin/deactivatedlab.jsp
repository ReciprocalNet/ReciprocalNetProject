<%--
  - Reciprocal Net project
  - deactivatedlab.jsp
  -
  - 07-Dec-2004: midurbin wrote first draft using custom tags, factoring some
  -              code from editlab.jsp
  - 17-Jan-2005: jobollin andded <ctl:selfForm> tags
  - 21-Jun-2005: midurbin changed UserIterator error flag name to reflect
  -              changes made in HtmlPageIterator
  - 13-Jul-2005: midurbin added new required parameters to labPage
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SiteField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserIterator" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:labPage title="View Deactivated Lab" labIdParamName="labId"
        manageLabsPageUrl="/admin/managelabs.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason" >
  <br />
  <ctl:selfForm>
    <table class="adminFormTable" border="0" cellspacing="0">
      <tr>
        <th class="twoColumnLeft">Lab ID:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.ID%>" displayAsLabel="true" />
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
          <rn:labField displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Short Name:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.SHORT_NAME%>"
                  displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Home URL:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.HOME_URL%>"
                  displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Lab Directory Name:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.DIRECTORY_NAME%>"
                  displayAsLabel="true"/>
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Default Copyright Notice:</th>
        <td class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.DEFAULT_COPYRIGHT_NOTICE%>"
                  displayAsLabel="true" />
        </td>
      </tr>
      <tr>
         <th class="twoColumnLeft">Is Active:</th>
        <td class="twoColumnRight">No.</td>
      </tr>
    </table>
    <br />
    <p class="headerFont1">
      Deactivated user accounts associated with this lab:
    </p>
    <rn:userIterator id="inactiveUsers" restrictToActiveUsers="false"
            restrictToInactiveUsers="true" restrictToLabUsers="true"
            sortByUsername="true"><%-- prevent leading spaces
      --%><ctl:parityChecker includeOnlyOnFirst="true"
              invert="true">,</ctl:parityChecker>
      &nbsp;&nbsp;
      <rn:userField fieldCode="<%=UserField.USER_NAME%>"
              displayAsLabel="true" />
      <font class="ancillaryText">
        (<rn:userField displayAsLabel="true"/>)
      </font><%-- 
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
    <a href="managelabs.jsp">Back to Lab Management</a>
  </center>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .ancillaryText { font-size: x-small; color: #444444; }
    .headerFont1  { font-size:medium; font-weight: bold; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
  </ctl:styleBlock>
</rn:labPage>
