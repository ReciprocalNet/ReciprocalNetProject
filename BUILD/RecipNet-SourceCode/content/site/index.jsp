<%--
  - Reciprocal Net project
  - index.jsp
  -
  - 22-Jul-2002: hclin wrote first draft
  - 11-Sep-2002: jobollin performed task #427: remove log in box from
  -              index.jsp
  - 15-Nov-2002: ekoperda added some explanatory text to the body of the page
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 02-Apr-2004: cwestnea made changes to text per task #1171
  - 16-Jun-2004: cwestnea rewrote to use custom tags
  - 26-Apr-2006: jobollin updated to use the new styles
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Site Information">
  <div class="pageBody">
    <table width="100%" cellspacing="4">
      <tr valign="top" width="500">
        <td style="padding: 20px"> 
          <div class="title">What is Reciprocal Net?</div>
          <p>
            The Reciprocal Net Site Network is a distributed database for 
            crystallographic information, supported by the 
            <a href="http://www.nsdl.org/">National Science Digital 
            Library</a>, and is run by participating crystallography labs 
            across the world. Each entry in the database generally 
            describes a single crystal structure that was synthesized or 
            isolated by a research chemist and was analyzed by means of 
            X-ray crystallography.
          </p>
        </td>
        <%-- These styles must be inline to work correctly --%>
        <td rowspan="2" style="padding: 15px;">
          <img src="images/molecule-world.png" />
        </td>
      </tr> 
      <tr> 
        <td style="padding: 20px"> 
          <div class="title">How to use this site</div>
          <p>
            You are visiting one of the many sites in the 
            <a href="http://www.reciprocalnet.org/master/sitelist.html"
            >Reciprocal Net Site Network</a>.  This site is operated by the 
            <a href="/">laboratory</a> noted in the upper-right corner of 
            this page.  Please use the menu bar above to navigate to 
            samples in this site's database. Select samples are available 
            to the general public without authentication. Authorized users,
            please <a href="login.jsp">log in</a> first. You may jump to 
            another site's database or visit the master server at 
            <a href="http://www.reciprocalnet.org">ReciprocalNet.org</a> 
            for more information and 
            <a href="http://www.reciprocalnet.org/master/userguide.html"
            >general help.</a> 
          </p>
        </td>
      </tr>
    </table>
  </div>
  <ctl:styleBlock>
    div.pageBody { text-align: left; background-color: #f3f3f3; }
    div.pageBody td {
      margin: 0;
      vertical-align: top;
      background-color: #e7ecf3;
      font-size: small;
    }
    div.title {
      width: 55%;
      color: #0033CC;
      font-size: 115%;
      font-weight: bold;
      border-bottom: 0.1em solid black;
    }
  </ctl:styleBlock>
</rn:page>

