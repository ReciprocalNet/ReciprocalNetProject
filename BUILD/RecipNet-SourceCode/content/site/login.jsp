<%--
   - login.jsp
   -
   - 22-Jul-2002: hclin wrote first draft
   - 06-Aug-2002: hclin fixed bug #246
   - 09-Aug-2002: hclin fixed bug #318
   - 03-Sep-2002: eisiorho added new feature, so that if the user was forwarded
   -                from a previous page because of an error, the user upon
   -                correct Authentication will be redirected back to the page
   -                they desired, instead of being forwarded back as before.
   - 20-Nov-2002: ekoperda fixed bug #609
   - 21-Nov-2002: ekoperda fixed bug #611
   - 13-Dec-2002: adharurk fixed bug #634
   - 11-Mar-2003: jobollin fixed bug #771
   - 28-Mar-2003: eisiorho fixed bug #835, fixed bad formatting
   - 28-Mar-2003: jrhanna added special handling for users already logged in
   - 31-Mar-2003: midurbin fixed bug #842
   - 08-May-2003: midurbin changed default redirection to lab/summary.jsp for
   -              lab users.
   - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
   - 02-Jul-2004: cwestnea rewrote to use tags
   - 04-Aug-2004: cwestnea modified redirect to use relative path
   - 08-Sep-2004: midurbin added support for AuthenticationController to allow
   -              older pages to recognize the logged in user
   - 29-Sep-2004: eisiorho fixed bug #1383
   - 16-Nov-2004: midurbin replaced the ExtraBodyAttribute tag with an
   -              ExtraHtmlAttribute tag
   - 12-Jan-2004: jobollin fixed bug #1499
   - 14-Jan-2005: jobollin inserted <ctl:selfForm> tags, configured the
   -              page onLoad attribute to be specified only when the form
   -              is included, and changed the default focus to be on forms[1]
   -              instead of forms[0].
   - 13-Jul-2005: midurbin added authorizationReasonMessage
   - 26-Jul-2005: midurbin removed the code that was added for task #1380
   - 08-Dec-2015: yuma added preserveParam1 for ctl:redirect
  --%>
<%@ page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Site Login">
  <rn:authorizationChecker>
    <ctl:redirect target="/alreadyloggedin.jsp" preserveParam="origUrl"
        preserveParam1="authorizationFailedReason"/>
  </rn:authorizationChecker>
  <rn:authorizationChecker invert="true">
    <ctl:extraHtmlAttribute name="onLoad"
        value="document.forms[1].username.focus()" />
    <center>
      <table>
        <tr>
          <td align="left">
            <br />
            <div class="errorMessage">
              <rn:authorizationReasonMessage id="authError"
                  authorizationReasonParamName="authorizationFailedReason" />
            </div>
            <ctl:errorMessage errorSupplier="<%=authError%>" errorFilter="<%=
                AuthorizationReasonMessage.AUTHORIZATION_REASON_DISPLAYED%>">
              <br />
              <br />
              If you are an authorized user, please log in below.
            </ctl:errorMessage>
          </td>
        </tr>
      </table>
      <br />
    </center>
    <ctl:selfForm>
      <center>
        <br/>
        <table class="loginBox">
          <tr><th>User Name:</th></tr>
          <tr><td><ctl:textbox id="username" /></td></tr>
          <tr><th>Password:</th></tr>
          <tr>
            <td>
              <ctl:textbox maskInput="true" id="password" maxLength="8" />
            </td>
          </tr>
          <tr>
            <td>
              <ctl:hiddenString id="origUrl" initialValueFrom="origUrl" />
              <rn:loginButton
                  id="login"
                  username="<%= username.getValueAsString() %>"
                  password="<%= password.getValueAsString() %>"
                  targetPage="/index.jsp"
                  targetPageIfCanSeeLabSummary="/lab/summary.jsp"
                  targetPageParamName="origUrl" />
            </td>
          </tr>
        </table>
      </center>
      <ctl:errorMessage errorSupplier="<%= login %>">
        <center>
          <span class="errorMessage">
            Incorrect username or password.
          </span>
        </center>
      </ctl:errorMessage>
    </ctl:selfForm>
  </rn:authorizationChecker>

  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold;}
    .loginBox     { border-style: solid; border-width: thin;
        border-color: #CCCCCC; text-align: left; }
  </ctl:styleBlock>
</rn:page>
