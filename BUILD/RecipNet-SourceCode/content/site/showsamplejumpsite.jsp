<%--
  - Reciprocal Net project
  - @(#)showsamplejumpsite.jsp
  -
  - 04-May-2005: midurbin wrote first draft
  - 13-Jul-2005: midurbin added new required parameters to ShowSamplePage
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<rn:showSamplePage useLabAndNumberAsTitlePrefix="true"
    labAndNumberSeparator=" sample " titleSuffix=" - Reciprocal Net"
    loginPageUrl="/login.jsp" currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <table width="100%" cellspacing="5" cellpadding="10">
    <tr>
      <td bgcolor="#ebebeb" width="65%" valign="top">
        <p class="jumpSiteInstructions">
          <center>
            <div class="jumpSiteHeader">
              The requested sample is hosted at another site...
            </div>
            <img src="images/longarrow.gif" />
          </center>
        </p>
        <p class="jumpSiteInstructions">
          The Reciprocal Net sample you requested is not available on
          this site.&nbsp;&nbsp;The sample is available on other
          Reciprocal Net sites, however.&nbsp;&nbsp;Please access it at
          one of the partner sites listed to the right.
        </p>
        <p class="jumpSiteInstructions">
          The Reciprocal Net Site Network is a distributed database;
          samples are distributed across the Internet for accuracy,
          efficiency, and high availability.&nbsp;&nbsp;See
          <a class="jumpSiteLink"
              href="http://www.reciprocalnet.org/master/sitelist.html">
            Reciprocalnet.org
          </a> for more information about Reciprocal Net's distributed
          features.
        </p>
      </td>
      <td bgcolor="#e7ecf3" width="35%" align="center">
        <rn:holdingsIterator>
          <p class="jumpSiteInstructions">
            <rn:link href="/showsample.jsp" considerSiteContext="true">
              <rn:siteSponsorImage />
            </rn:link>
            <br />
            <rn:labField fieldCode="<%=LabField.SHORT_NAME%>" /> sample
            <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                displayAsLabel="true" /> hosted at 
            <rn:link href="/showsample.jsp" considerSiteContext="true">
              <nobr><rn:siteField /></nobr>
            </rn:link> <rn:holdingLevel />
          </p>
          <br />
        </rn:holdingsIterator>
      </td>
    </tr>
  </table>
  <ctl:styleBlock>
    a.jumpSiteLink:visited    { font-weight: normal; font-style: normal;
        font-size: 16px; text-decoration: none; color: #0033CC; }
    a.jumpSiteLink:hover      { font-weight: normal; font-style: normal;
        font-size: 16px; text-decoration: underline; color: #0033CC; }
    a.jumpSiteLink:link       { font-weight: normal; font-style: normal;
        font-size: 16px; text-decoration: none; color: #0033CC; }

    .jumpSiteInstructions {
        font-family: Arial, Verdana, Helvetica, sans-serif;
        font-size: 16px;
        color: #000000;
        font-weight: normal;
        margin: 20px;
    }

    .jumpSiteHeader {
        font-family: Arial, Verdana, Helvetica, sans-serif;
        font-size: 16px;
        color: #0033CC;
        font-weight: bold;
        margin: 10px;
    }
  </ctl:styleBlock>

</rn:showSamplePage>
