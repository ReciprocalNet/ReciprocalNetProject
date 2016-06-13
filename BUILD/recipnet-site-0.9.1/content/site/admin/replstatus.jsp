<%--
  - Reciprocal Net project
  - replstatus.jsp
  -
  - 19-Mar-2009: ekoperda wrote first draft
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.SiteField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SiteIterator" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>

<rn:page title="Replication Status">
  <p class="pageInstructions">
    This page is intended for debugging purposes only.  The information below
    describes this site's replication status with respect to the rest of the
    Reciprocal Net Site Network.
  </p>
  <p class="headerFont1">Sites table:</p>
  <table class="adminFormTable" border="0" cellspacing="0">
    <tr class='rowColorfalse'>
      <th class="threeColumnLeft">ID</th>
      <th class="threeColumnMiddle">Name</th>
      <th class="threeColumnMiddle">Short Name</th>
      <th class="threeColumnMiddle">Public Seq. Num.</th>
      <th class="threeColumnMiddle">Private Seq. Num.</th>
      <th class="threeColumnMiddle">Final Seq. Num.</th>
      <th class="threeColumnMiddle">Active</th>
      <th class="threeColumnRight">Base URL</th>
    </tr>
    <rn:siteIterator id="sit">
      <tr class="${rn:testParity(sit.iterationCountSinceThisPhaseBegan,
                                     'rowColortrue', 'rowColorfalse')}">
        <td class="threeColumnLeft">
          <rn:siteField fieldCode="<%=SiteField.ID%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.NAME%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.SHORT_NAME%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.PUBLIC_SEQ_NUM%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.PRIVATE_SEQ_NUM%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.FINAL_SEQ_NUM%>" />
        </td>
        <td class="threeColumnMiddle">
          <rn:siteField fieldCode="<%=SiteField.IS_ACTIVE%>" />
        </td>
        <td class="threeColumnRight">
          <rn:siteField fieldCode="<%=SiteField.BASE_URL%>" />
        </td>
      </tr>
    </rn:siteIterator>
  </table>
  <br />&nbsp;<br />
  <center>
    <ctl:a href="/admin/index.jsp">Back to the Administration Tools</ctl:a>
  </center>
  <ctl:styleBlock>
    .headerFont1  { font-size:medium; font-weight: bold; }
    .rowColorfalse { background-color: #E6E6E6 }
    .rowColortrue  { background-color: #FFFFFF }
    .threeColumnLeft   { text-align: left; padding: 0.05in; 
                         border-style: solid; border-width: thin; 
                         border-color: #CCCCCC; }
    .threeColumnMiddle { text-align: left; padding: 0.05in; 
                         border-style: solid; border-width: thin; 
                         border-color: #CCCCCC; }
    .threeColumnRight  { text-align: left; padding: 0.05in; 
                         border-style: solid; border-width: thin; 
                         border-color: #CCCCCC; }
    .adminFormTable { border-style: solid; border-width: thin;
                      border-color: #CCCCCC; padding: 0.01in;}
  </ctl:styleBlock>
</rn:page>
