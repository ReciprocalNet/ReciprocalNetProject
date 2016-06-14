<%--
  - Reciprocal Net project
  - managelabs.jsp
  -
  - 22-Jul-2002: hclin wrote first draft
  - 09-Aug-2002: hclin fixed bug #295
  - 21-Aug-2002: eisiorho fixed bug #376
  - 13-Sep-2002: jobollin corrected title capitalization per task 332
  - 20-Sep-2002: eisiorho fixed bug #469
  - 17-Apr-2003: eisiorho added hyperlink to /admin/index.jsp
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 06-Dec-2004: eisiorho rewrote using custom tags
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 25-Feb-2005: midurbin added redirectToLogin in case of insufficient access
  - 13-Jul-2005: midurbin added new required parameters to redirectToLogin
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@page
    import="org.recipnet.site.content.rncontrols.AuthorizationReasonMessage" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>

<rn:page title="Manage Labs">
  <rn:authorizationChecker canAdministerLabs="true" invert="true">
    <rn:redirectToLogin target="/login.jsp" returnUrlParamName="origUrl"
        authorizationReasonParamName="authorizationFailedReason"
        authorizationReasonCode="<%=
          AuthorizationReasonMessage.CANNOT_ADMINISTER_LABS%>" />
  </rn:authorizationChecker>
  <rn:authorizationChecker canAdministerLabs="true">
    <p class="headerFont1">Reciprocal Net Labs:</p>
    <ctl:selfForm>
      <table class="adminFormTable" border="0" cellspacing="0">
        <rn:labIterator id="lit">
          <tr class="${rn:testParity(lit.iterationCountSinceThisPhaseBegan,
                                     'rowColortrue', 'rowColorfalse')}">
          <th class="threeColumnLeft">
            <rn:labField fieldCode="<%=LabField.NAME%>" />
          </th>
          <rn:labChecker requireLocal="true" requireActive="true">
              <td class="threeColumnMiddle">
                <rn:link contextType="<%=LabContext.class%>"
                    href="/admin/editlab.jsp">Edit this Lab</rn:link>
              </td>
              <td class="threeColumnRight">
                <rn:link contextType="<%=LabContext.class%>"
                    href="/admin/manageproviders.jsp"
                    >Manage this lab's provider(s)</rn:link>
              </td>
          </rn:labChecker>
            <rn:labChecker requireLocal="true" requireInactive="true">
              <td class="threeColumnMiddle">
                <rn:link contextType="<%=LabContext.class%>"
                    href="/admin/deactivatedlab.jsp"
                    >This lab is deactivated.</rn:link>
              </td>
              <td class="threeColumnRight">
                <rn:link contextType="<%=LabContext.class%>"
                    href="/admin/manageproviders.jsp"
                    >This lab's provider(s) are deactivated.</rn:link>
              </td>
            </rn:labChecker>
          <rn:labChecker requireNonlocal="true">
            <td colspan="2">This lab may not managed from this site.</td>
          </rn:labChecker>
          </tr>
        </rn:labIterator>
      </table>
    </ctl:selfForm>
    <br />&nbsp;<br />
    <center>
      <ctl:a href="/admin/index.jsp">Back to the Administration Tools</ctl:a>
    </center>
  </rn:authorizationChecker>
  <ctl:styleBlock>
    .headerFont1  { font-size:medium; font-weight: bold; }
    .rowColorfalse { background-color: #E6E6E6 }
    .rowColortrue  { background-color: #FFFFFF }
    .threeColumnLeft   { text-align: left; padding: 0.05in; border-width: 0; }
    .threeColumnMiddle { text-align: left;  padding: 0.05in; border-width: 0;}
    .threeColumnRight  { text-align: left;  padding: 0.05in; border-width: 0; }
    .adminFormTable { border-style: solid; border-width: thin;
                      border-color: #CCCCCC; padding: 0.01in;}
  </ctl:styleBlock>
</rn:page>
