<%--
  - Reciprocal Net project
  - deactivateprovider.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              editprovider.jsp
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to providerPage
  --%>

<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:providerPage title="Deactivate Provider" labIdParamName="labId"
        providerIdParamName="providerId"
        manageProvidersPageUrl="/admin/manageproviders.jsp"
        providerPageUrl="/admin/editprovider.jsp" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason" >
  <br />
  <ctl:selfForm>
    <center>
      <h2>
        <p class="errorMessage">
          You are about to deactivate provider <font color="blue">
          <rn:providerField fieldCode="<%=ProviderField.NAME%>" /></font>, and
          all user accounts associated. This will prevent new samples from being
          submitted under this provider, but will not affect existing samples.
          This action cannot be undone. Are you sure you want to procede?
        </p>
      </h2>
      <rn:providerActionButton label="Cancel" providerFunction="<%=
              ProviderPage.CANCEL_DEACTIVATION%>" />
      <rn:providerActionButton label="Yes, Deactivate"
              providerFunction="<%=ProviderPage.DEACTIVATE_PROVIDER%>"/>
    </center>
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold;}
  </ctl:styleBlock>
</rn:providerPage>
