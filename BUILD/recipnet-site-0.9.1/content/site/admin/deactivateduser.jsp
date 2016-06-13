<%--
  - Reciprocal Net project
  - deactivateduser.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              edituser.jsp
  - 17-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 18-May-2005: midurbin updated userPage properties to reflect spec changes
  - 13-Jul-2005: midurbin added new required parameters to userPage
  - 28-Apr-2006: jobollin fixed bug #1787 by updating the <rn:userPage>
  -              attributes
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserActionButton" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:userPage title="View Deactivated User" userIdParamName="userId"
        labIdParamName="labId" providerIdParamName="providerId"
        completionUrlParamName="labOrProviderPage"
        cancellationUrlParamName="labOrProviderPage"
        loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <ctl:selfForm>
    <table class="adminFormTable">
      <tr>
        <th class="twoColumnLeft">User ID:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.ID%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">User Name:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.USER_NAME%>"
                  displayAsLabel="true" />
        </td>
      </tr>
      <rn:userChecker includeIfLabUser="true">
        <tr>
          <th class="twoColumnLeft">Lab Name (ID):</th>
          <td class="twoColumnRight">
            <rn:labField /> (<rn:labField fieldCode="<%=LabField.ID%>" />)
          </td>
        </tr>
      </rn:userChecker>
      <rn:userChecker includeIfProviderUser="true">
        <tr>
          <th class="twoColumnLeft">Provider Name (ID):</th>
          <td class="twoColumnRight">
            <rn:providerField /> (<rn:providerField fieldCode="<%=
                    ProviderField.ID%>" />)
          </td>
        </tr>
      </rn:userChecker>
      <tr>
        <th class="twoColumnLeft">Full Name:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.FULL_NAME%>"
                  displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Creation Date:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.CREATION_DATE%>" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Inactive Date:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.INACTIVE_DATE%>" />
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  <br />
  <center>
    <rn:userChecker includeIfProviderUser="true">
        <rn:link href="/admin/editprovider.jsp"
                contextType="<%=ProviderContext.class%>">
          Back to Manage Provider (<rn:providerField />)
        </rn:link>
    </rn:userChecker>
    <rn:userChecker includeIfLabUser="true">
        <rn:link href="/admin/editlab.jsp"
                contextType="<%=LabContext.class%>">
          Back to Manage Lab (<rn:labField />)
        </rn:link>
    </rn:userChecker>
  </center>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
  </ctl:styleBlock>
</rn:userPage>
