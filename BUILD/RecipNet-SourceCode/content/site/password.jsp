<%--
  - Reciprocal Net project
  - password.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              edituser.jsp
  - 17-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 18-May-2005: midurbin updated userPage properties to reflect spec changes
  -              and moved from /admin/ to /
  - 13-Jul-2005: midurbin added new required parameters to UserPage
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:userPage title="Change User Password" userIdParamName="userId"
        labIdParamName="labId" providerIdParamName="providerId"
        completionUrlParamName="editUserPage"
        cancellationUrlParamName="editUserPage" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
 <br />
 <ctl:selfForm>
   <table class="adminFormTable">
    <tr>
      <th class="twoColumnLeft">Full Name:</th>
      <td class="twoColumnRight">
        <rn:userField fieldCode="<%=UserField.FULL_NAME%>"
                displayAsLabel="true" />
      </td>
    </tr>
    <tr>
      <th class="twoColumnLeft">User Name:</th>
      <td class="twoColumnRight">
        <rn:userField fieldCode="<%=UserField.USER_NAME%>"
                displayAsLabel="true" />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <hr size="1">
      </td>
    </tr>
    <tr>
      <th colspan="2">
        Reset Password:<br />
        <font class="comment">
          Passwords are limited to 8 characters maximum.<br />&nbsp;
        </font>
      </th> 
    </tr>
    <tr>
      <td class="twoColumnLeft"> New password: </td>
      <td class="twoColumnRight">
        <ctl:textbox id="password1" maskInput="true" maxLength="8" size="8"
                required="true"></ctl:textbox>
      </td>
    </tr>
    <tr>
      <td class="twoColumnLeft"> Confirm new password: </td>
      <td class="twoColumnRight">
        <rn:userField id="password2" fieldCode="<%=UserField.PASSWORD%>"
            redundantPassword="<%=password1%>" editable="true" />
      </td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <ctl:errorMessage errorSupplier="<%=password2%>"
            errorFilter="<%=UserField.PASSWORD_DOES_NOT_MATCH%>">
      <tr>
        <td class="oneColumn" colspan="2">
          <div class="unmatchedPasswords">
            The passwords entered were invalid or did not match.<br />
            Please carefully retype the new password.
          </div>
        </td>
      </tr>
    </ctl:errorMessage>
    <tr>
      <td class="oneColumn" colspan="2">
        <rn:userActionButton
                userFunction="<%=UserPage.UserFunction.CHANGE_USER_PASSWORD%>"
                label="Change Password" />
        <rn:userActionButton userFunction="<%=UserPage.UserFunction.CANCEL%>"
                label="Cancel" />
      </td>
    </table>
  </ctl:selfForm>
  <ctl:styleBlock>
    .unmatchedPasswords  { color: red; font-weight: bold; font-size: x-small; }
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in; }
    .twoColumnLeft   { text-align: right; vertical-align: top; padding: 0.01in;
            border-width: 0; width: 45%; }
    .twoColumnRight  { text-align: left; vertical-align: top;  padding: 0.01in;
            border-width: 0; width: 55%; }
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
    .comment { font-family: sans-serif; font-size: x-small; color: #9E9E9E;
            text-align: center; }
  </ctl:styleBlock>
</rn:userPage>
