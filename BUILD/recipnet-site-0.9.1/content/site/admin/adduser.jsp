<%--
  - Reciprocal Net project
  - adduser.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              edituser.jsp
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 18-May-2005: midurbin updated userPage properties to reflect spec changes
  - 10-Jun-2005: midurbin exposed "submitting provider" global access flag for
  -              provider users
  - 13-Jul-2005: midurbin added new required parameters to userPage
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:userPage title="Add User" userIdParamName="userId" labIdParamName="labId"
        providerIdParamName="providerId"
        completionUrlParamName="labOrProviderPage"
        cancellationUrlParamName="labOrProviderPage" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <ctl:selfForm>
    <table class="adminFormTable">
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
          <rn:userField fieldCode="<%=UserField.FULL_NAME%>" editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">User Name:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.USER_NAME%>" editable="true" />
          <ctl:errorMessage errorFilter="<%=UserPage.DUPLICATE_USERNAME%>">
            <div class="errorMessage">
              There already exists another user with this username!
              <br />  Please select a new username.
            </div>
          </ctl:errorMessage>
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Password:</th>
        <td class="twoColumnRight">
          <rn:userField fieldCode="<%=UserField.PASSWORD%>" editable="true"/>
        </td>
      </tr>
      <rn:userChecker includeIfLabUser="true">
        <tr>
          <th class="twoColumnLeft">Global Access Level:</th>
          <td class="twoColumnRight">
            <rn:userField fieldCode="<%=UserField.SCIENTIFIC_USER_TOGGLE%>" />
              Scientific User
            <rn:userField fieldCode="<%=UserField.LAB_ADMIN_TOGGLE%>" />
              Lab Administrator
            <rn:userField fieldCode="<%=UserField.SITE_ADMIN_TOGGLE%>" />
              Site Administrator
          </td>
        </tr>
      </rn:userChecker>
      <rn:userChecker includeIfProviderUser="true">
        <tr>
          <th class="twoColumnLeft">May Submit Samples:</th>
          <td class="twoColumnRight">
            <rn:userField
                fieldCode="<%=UserField.SUBMITTING_PROVIDER_TOGGLE%>" />
            <span class="note">
              (when this box is checked, this provider user will
              <br /> be allowed to submit new samples from his/her provider)
            </span>
          </td>
        </tr>
      </rn:userChecker>
      <ctl:errorMessage
          errorFilter="<%= UserPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>">
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
        <td class="oneColumn" colspan="2">
          <rn:userActionButton label="Add User"
                  userFunction="<%=UserPage.UserFunction.ADD_USER%>" />
          <rn:userActionButton userFunction="<%=UserPage.UserFunction.CANCEL%>"
                  label="Cancel" />
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  <br />
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in; }
    .twoColumnLeft   { text-align: right; vertical-align: top; padding: 0.01in;
            border-width: 0; }
    .twoColumnRight  { text-align: left; vertical-align: top;  padding: 0.01in;
            border-width: 0; }
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
    .note { color: #909090; font-style: italic; font-size: x-small; }
  </ctl:styleBlock>
</rn:userPage>
