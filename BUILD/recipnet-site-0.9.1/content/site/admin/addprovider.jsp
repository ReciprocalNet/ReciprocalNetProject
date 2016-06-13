<%--
  - Reciprocal Net project
  - addprovider.jsp
  -
  - 30-Nov-2004: midurbin wrote first draft using custom tags to mimic part of
  -              editprovider.jsp
  - 17-Jan-2005: jobollin added <ctl:selfForm> tags
  - 13-Jul-2005: midurbin added new required parameters to providerPage

--%>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:providerPage title="Add Provider" labIdParamName="labId"
        providerIdParamName="providerId"
        manageProvidersPageUrl="/admin/manageproviders.jsp"
        loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason" >
  <br />
  <ctl:selfForm>
    <table class="adminFormTable">
      <tr>
        <th class="twoColumnLeft">Lab Name (ID):</th>
        <td  class="twoColumnRight">
          <rn:labField fieldCode="<%=LabField.NAME%>" displayAsLabel="true" />
          (<rn:labField fieldCode="<%=LabField.ID%>" />)
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Provider Name:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.NAME%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Provider Head Contact:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.HEAD_CONTACT%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <th class="twoColumnLeft">Comments:</th>
        <td class="twoColumnRight">
          <rn:providerField fieldCode="<%=ProviderField.COMMENTS%>"
                  editable="true" />
        </td>
      </tr>
      <tr>
        <td colspan="2">&nbsp;</td>
      </tr>
      <ctl:errorMessage
          errorFilter="<%= ProviderPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>">
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
        <td colspan="2" class="oneColumn">
          <rn:providerActionButton
                  providerFunction="<%=ProviderPage.ADD_NEW_PROVIDER%>"
                  label="Add New Provider" />
          <rn:providerActionButton label="Cancel" providerFunction="<%=
                  ProviderPage.CANCEL_PROVIDER_FUNCTION%>" />
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  <ctl:styleBlock>
    .adminFormTable { border-style: solid; border-width: thin;
            border-color: #CCCCCC; padding: 0.01in;}
    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;
            border-width: 0; }
    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}
    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}
    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }
  </ctl:styleBlock>
</rn:providerPage>
