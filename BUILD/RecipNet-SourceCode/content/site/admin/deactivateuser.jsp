<%--
  - Reciprocal Net project
  - deactivate.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              edituser.jsp
  - 17-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 18-May-2005: midurbin updated userPage properties to reflect spec changes
  - 13-Jul-2005: midurbin added new required parameters to userPage
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:userPage title="Deactivate User" userIdParamName="userId"
        labIdParamName="labId" providerIdParamName="providerId"
        cancellationUrlParamName="editUserPage"
        completionUrlParamName="labOrProviderPage" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <br />
  <ctl:selfForm>
    <center>
      <h2>
        <p class="errorMessage" >
          You are about to deactivate the username
          "<font color="blue"><rn:userField
                  fieldCode="<%=UserField.USER_NAME%>" /></font>", for
          <font color="blue"><rn:userField /></font>. This user will not be
          able to log in again.  Are you sure you want to do this?
        </p>
      </h2>
      <rn:userActionButton userFunction="<%=UserPage.UserFunction.CANCEL%>"
              label="Cancel" />
      <rn:userActionButton
              userFunction="<%=UserPage.UserFunction.DEACTIVATE_USER%>"
              label="Yes, Deactivate" />
    </center>
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold;}
  </ctl:styleBlock>
</rn:userPage>
