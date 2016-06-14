<%--
   - alreadyloggedin.jsp
   -
   - 02-Jul-2004: cwestnea wrote first draft
   - 04-Aug-2004: cwestnea modified redirect to use relative path
   - 14-Jan-2005: jobollin inserted <ctl:selfForm> elements
   - 13-Jul-2005: midurbin added authorizationReasonMessage
   - 08-Dec-2015: yuma added preserveParam1
  --%>
<%@ page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage"%>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>

<rn:page title="Site Login">
  <ctl:selfForm>
    <rn:authorizationChecker invert="true">
      <ctl:redirect target="/login.jsp" />
    </rn:authorizationChecker>
    <rn:authorizationChecker>
      <center>
        <table width="50%">
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
                <p>
                  You are currently logged in as
                  <strong>
                    <rn:userField fieldCode="<%= UserField.USER_NAME %>" 
                        displayAsLabel="true" /></strong>; click logout if you
                 wish to attempt this action using a different user account.
                </p>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=authError%>" errorFilter="<%=
                  AuthorizationReasonMessage.AUTHORIZATION_REASON_DISPLAYED%>"
                  invertFilter="true">
                <p>
                  You are already logged in as
                  <strong><rn:userField fieldCode="<%= UserField.USER_NAME %>" 
                          displayAsLabel="true" /></strong>!
                </p>
                <p>
                  To log in as a different user, click 'logout'.
                </p>
              </ctl:errorMessage>
              <p>
                Otherwise you may use the navigation links above to perform
                an action that you are authorized to perform or click cancel to
                <rn:authorizationChecker canSeeLabSummary="true">
                  go to the lab summary page.
                </rn:authorizationChecker>
                <rn:authorizationChecker canSeeLabSummary="true"
                    invert="true">
                  go to the site info page.
                </rn:authorizationChecker>
              </p>
              <ctl:button suppressInsteadOfSkip="true" label="Logout">
                <rn:logout />
                <ctl:redirect target="/login.jsp" preserveParam="origUrl"
                    preserveParam1="authorizationFailedReason" />
              </ctl:button>
              <rn:authorizationChecker canSeeLabSummary="true">
                <ctl:button label="Cancel" suppressInsteadOfSkip="true">
                  <ctl:redirect target="/lab/summary.jsp" />
                </ctl:button>
              </rn:authorizationChecker>
              <rn:authorizationChecker canSeeLabSummary="true" invert="true">
                <ctl:button label="Cancel" suppressInsteadOfSkip="true">
                  <ctl:redirect target="/index.jsp" />
                </ctl:button>
              </rn:authorizationChecker>
            </td>
          </tr>
        </table>
      </center>
    </rn:authorizationChecker>
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold;}
  </ctl:styleBlock>
</rn:page>
