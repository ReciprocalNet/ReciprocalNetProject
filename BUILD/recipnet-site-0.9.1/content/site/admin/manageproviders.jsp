<%--
  - Reciprocal Net project
  - manageproviders.jsp
  -
  - 22-Jul-2002: hclin wrote first draft
  - 09-Aug-2002: hclin fixed bug #295
  - 21-Aug-2002: eisiorho removed TODO, task #373
  - 13-Sep-2002: jobollin corrected title capitalization per task 332
  - 21-Jan-2003: adharurk improved the way in which Active and Deactivated
  -              providers are displayed, task #586
  - 17-Apr-2003: eisiorho added hyperlink to managelabs.jsp
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 05-Jan-2005: midurbin finished eisiorho's rewrite using custom tags
  - 17-Jan-2005: jobollin inserted <ctl:selfForm> tags
  - 21-Jun-2005: midurbin changed ProviderIterator error flag name to reflect
  -              changes made in HtmlPageIterator
  - 13-Jul-2005: midurbin added new required parameters to labPage
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabContext" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:labPage labIdParamName="labId" title="Manage Providers"
    loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <ctl:selfForm>
    <rn:labChecker requireActive="true">
      <p class="headerFont1">
        Active providers associated with <rn:labField />:
      </p>
      <table class="adminFormTable" border="0" cellspacing="0">
        <rn:providerIterator id="pit" restrictToActiveProviders="true">
          <tr class="${rn:testParity(pit.iterationCountSinceThisPhaseBegan,
                                     'rowColorEven', 'rowColorOdd')}">
            <th class="threeColumnLeft">
              <rn:providerField fieldCode="<%=ProviderField.NAME%>" />
            </th>
            <td class="twoColumnRight">
              <rn:link contextType="<%=ProviderContext.class%>"
                  href="/admin/editprovider.jsp">Edit this Provider</rn:link>
            </td>
          </tr>
        </rn:providerIterator>
        <tr>
          <td colspan="2" class="headerColor">
            <rn:link contextType="<%= LabContext.class %>"
                href="/admin/addprovider.jsp">Add a new provider...</rn:link>
          </td>
        </tr>
      </table>
      <br />
      <br />
    </rn:labChecker>
    <p class="headerFont1">
      Deactivated providers associated with <rn:labField />.
    </p>
    <rn:providerIterator id="inactiveProvs" restrictToInactiveProviders="true"
       ><%-- prevent leading spaces
      --%><ctl:parityChecker includeOnlyOnFirst="true"
              invert="true">,</ctl:parityChecker>
      &nbsp;&nbsp;
      <rn:link href="/admin/deactivatedprovider.jsp" contextType="<%=
              ProviderContext.class%>">
      <rn:providerField fieldCode="<%=ProviderField.NAME%>" /></rn:link>
      <font class="ancillaryText">(<rn:userField />)</font><%--
            --- prevent trailing spaces ---
    --%></rn:providerIterator>
    <ctl:errorMessage errorSupplier="<%=inactiveProvs%>"
            errorFilter="<%=ProviderIterator.NO_ITERATIONS%>">
      There are no deactivated providers associated with this lab.
    </ctl:errorMessage>
  </ctl:selfForm>
  <br />
  <br />
  <center>
    <ctl:a href="/admin/managelabs.jsp">Back to Lab Management</ctl:a>
  </center>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
                      border-color: #CCCCCC; padding: 0.01in;}
    .headerFont1  { font-size:medium; font-weight: bold; }
    .rowColorEven { background-color: #E6E6E6 }
    .rowColorOdd { background-color: #FFFFFF }
    .headerColor   { background-color: #CCCCCC }
    .threeColumnLeft   { text-align: left; padding: 0.05in; border-width: 0; }
    .threeColumnRight  { text-align: left; padding: 0.05in; border-width: 0; }
    .twoColumnRight  { text-align: left; padding: 0.01in; border-width: 0;}
  </ctl:styleBlock>
</rn:labPage>
