<%-- Reciprocal Net project
   - header.jsp
   -
   - 22-Jul-2002: hclin wrote first draft
   - 05-Aug-2002: ekoperda redesigned UI slightly
   - 06-Aug-2002: ekoperda added quicksearch and browse features
   - 16-Sep-2002: jobollin shrank the menu bar (Task #430)
   - 03-Oct-2002: jobollin implemented the reduced ReciprocalNet Logo
   -              (Task #489)
   - 07-Oct-2002: adharurk added a feature that passes origURL to the
   -              login.jsp when user clicks on 'Log in' link
   - 10-Oct-2002: adharurk fixed bug no.532 that threw an exception when user
   -              directly goes to 'login.jsp' before visiting any
   -              intermediate page
   - 18-Nov-2002: ekoperda fixed bug 606; added a bunch of JavaScript to do the
   -              rollover
   - 10-Dec-2002: jobollin fixed bug 637
   - 11-Dec-2002: jobollin fixed bug 646
   - 23-Jan-2003: adharurk added link to site "reciprocalnet.org"
   - 21-Mar-2003: eisiorho fixed bug #797 so 'origURL' is based on
   -              request.getUrl()
   - 31-Mar-2003: adharurk fixed bug #832
   - 08-May-2003: midurbin added summary menu item for lab users
   - 27-Jun-2003: ajooloor changed text in quick search box
   - 31-Mar-2004: cwestnea changed the header image per task #1174
   - 02-Apr-2004: cwestnea switched username and logout link per task #1057
   - 02-Apr-2004: cwestnea modified navbar per task #1173
   - 02-Apr-2004: cwestnea fixed bug #1001
   - 02-Apr-2004: cwestnea fixed bug #1177
   - 24-Jun-2004: cwestnea rewrote using custom tags
   - 16-Jul-2004: cwestnea fixed bug #1300
   - 04-Aug-2004: cwestnea modified redirect to use relative path
   - 30-Sep-2004: eisiorho fixed bug #1382, changed value for 'origUrl'
   - 16-Nov-2004: midurbin replaced 'extraAttribute' with 'extraHtmlAttribute'
   -              to reflect changes in the TLD
   - 21-Jan-2005: jobollin gave the quicksearch feature its own HTML form
   - 16-May-2005: midurbin fixed bug #1527 using new form tag
   - 18-May-2005: midurbin added "Preferences" link
   - 29-Jun-2005: midurbin added CurrentUserContext around entire body to
   -              make up for the fact that AuthorizationChecker no longer
   -              supplied the UserContext
   - 05-Jul-2005: midurbin updated the "Preferences" and "Log in" link
   -              parameters because regular expressions were no longer needed
   -              to ensure proper handling
   - 15-Aug-2005: midurbin added RequestAttributeChecker tags to create a 
   -              minimal version of the header
   - 18-Jan-2006: jobollin moved style block to the external stylesheet and
   -              generally tweaked styles for simpler code and greater
   -              cross-browser consistency
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<ctl:page suppressGeneratedHtml="true">
  <rn:currentUserContext>
    <table class="header" border="0" cellspacing="0" width="100%">
      <tr style="background: black">
        <td><%--
        -- Ensure that no spaces are in the HTML as Netscape 4.x uses them as
        -- an excuse to make the height of the table cell larger than it needs
        -- to be.
          --%><ctl:requestAttributeChecker
                  attributeName="suppressNavigationLinks"
                  includeIfAttributeIsPresent="false"><%--
            --%><a href="http://www.reciprocalnet.org"><%--
          --%></ctl:requestAttributeChecker><%--
          --%><ctl:img alt="Reciprocal Net" border="0" src="/images/banner.jpg"/><%--
          --%><ctl:requestAttributeChecker
                  attributeName="suppressNavigationLinks"
                  includeIfAttributeIsPresent="false"><%--
            --%></a><%--
          --%></ctl:requestAttributeChecker><%--
        --%></td>
        <td align="right"><%--
          --%><ctl:requestAttributeChecker
                  attributeName="suppressNavigationLinks"
                  includeIfAttributeIsPresent="false"><%--
            --%><a href="/"><%--
          --%></ctl:requestAttributeChecker><%--
          --%><ctl:img alt="Site sponsor" border="2" width="256"
            src="/images/sitesponsor.gif" /><%--
          --%><ctl:requestAttributeChecker
                  attributeName="suppressNavigationLinks"
                  includeIfAttributeIsPresent="false"><%--
            --%></a><%--
          --%></ctl:requestAttributeChecker><%--
        --%></td>
      </tr>
      <ctl:requestAttributeChecker attributeName="suppressNavigationLinks"
              includeIfAttributeIsPresent="false">
        <tr style="background: #969696">
          <td colspan="2">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  &nbsp;&nbsp;&nbsp;<ctl:a styleClass="navItem" href="/index.jsp">Site Info</ctl:a>
                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                  <ctl:a styleClass="navItem" href="/search.jsp">Search</ctl:a>
                  <rn:authorizationChecker canSeeLabSummary="true">
                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                    <ctl:a styleClass="navItem" href="/lab/summary.jsp">Lab
                      Summary</ctl:a>
                  </rn:authorizationChecker>
                  <rn:authorizationChecker canSubmitSamples="true">
                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                    <ctl:a styleClass="navItem" href="/lab/submit.jsp">Submit
                      Sample</ctl:a>
                  </rn:authorizationChecker>
                  <rn:authorizationChecker canAdministerLabs="true">
                  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
                    <ctl:a styleClass="navItem" href="/admin/index.jsp">Admin
                      Tools</ctl:a>
                  </rn:authorizationChecker>
                </td>
                <td align="right">
                  <ctl:form method="GET" action="/search.jsp" pageForm="false">
                    <input name="quickSearch" type="text" size="22"
                           value="sample # (local search)"
                           onFocus="value=''" />
                    <input name="quickSearchSubmit" type="submit" value="Go" />
                  </ctl:form>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr style="background: #C8C8C8; font-style: bold">
          <td>
            <ctl:displayRequestAttributeValue attributeName="pageTitle" />
          </td>
          <td align="right">
            <rn:authorizationChecker invert="true">
              <ctl:a href="/login.jsp">
                <ctl:currentPageLinkParam name="origUrl" />
                  Log in</ctl:a>
            </rn:authorizationChecker>
            <rn:authorizationChecker>
              <ctl:a href="/logout.jsp">Log out</ctl:a> &nbsp;
              <ctl:a href="/account.jsp">
                <ctl:currentPageLinkParam name="origPage" />
                  Preferences
                </ctl:a>&nbsp;
              <rn:userField fieldCode="<%= UserField.FULL_NAME %>"
                  id="HEADER_USER_FULLNAME" displayAsLabel="true"/>
            </rn:authorizationChecker>
          </td>
        </tr>
      </ctl:requestAttributeChecker>
    </table>
  </rn:currentUserContext>
</ctl:page>
