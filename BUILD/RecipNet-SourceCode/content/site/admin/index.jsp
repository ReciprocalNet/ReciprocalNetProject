<%--
  - Reciprocal Net project
  - /admin/index.jsp
  -
  - 22-Jul-2002: jobollin wrote first draft
  - 22-Jul-2002: hclin modified first draft
  - 21-Nov-2002: ekoperda fixed bug #610 by disabling all siteadmin-only links
  - 20-Mar-2003: ajooloor fixed spacing among site/lab options
  - 26-Jun-2003: dfeng replace include file common.inc with common.jspf
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 20-Sep-2004: eisiorho rewrote using custom tags
  - 25-Feb-2005: midurbin added redirectToLogin in case of insufficient access
  - 13-Jul-2005: midurbin added new required parameters to redirectToLogin
  - 19-Mar-2009: ekoperda added hyperlink to replstatus.jsp
  --%>
<%@page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Administrator Index"> 
  <rn:authorizationChecker canAdministerLabs="true" invert="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
          AuthorizationReasonMessage.CANNOT_ADMINISTER_LABS%>" />
  </rn:authorizationChecker>
  <rn:authorizationChecker canAdministerLabs="true">
    <ctl:a href="/admin/managelabs.jsp">Manage Labs</ctl:a><br />
    <ctl:a disabled="true"
        href="/admin/siteconfig.jsp">Configure Site</ctl:a><br />
    <ctl:a disabled="true"
        href="/admin/replconfig.jsp">Configure Replication</ctl:a><br />
    <ctl:a disabled="true"
        href="/admin/localtracking.jsp">Configure Local Tracking</ctl:a><br />
    <ctl:a href="/admin/replstatus.jsp">Replication Status</ctl:a><br />
    <!--  Remove this table when we know what content to put here. --!>
    <table width="100%" height="400">
      <tr><td></td></tr>
    </table>
    <!--   End   -->
  </rn:authorizationChecker>
</rn:page>
