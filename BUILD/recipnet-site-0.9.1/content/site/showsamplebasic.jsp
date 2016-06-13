<%--
  - Reciprocal Net project
  -
  - showsamplebasic.jsp
  -
  - 04-May-2005: midurbin wrote first draft
  - 06-Jum-2005: midurbin fixed bug #1607
  - 10-Jun-2005: midurbin updated references to UserPreferencesBL to account
  -              for name change
  - 21-Jun-2005: midurbin replaced funny use of ErrorMessageElement with a
  -              ParityChecker
  - 05-Jul-2005: midurbin added 'overrideReinvocationServletPath' property to
  -              the showSamplePage tag
  - 13-Jul-2005: midurbin added new required parameters to ShowSamplePage
  - 07-Feb-2006: jobollin revised to accommodate changes to JammAppletTag and
  -              the new JammModelElement
  - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
  - 29-Mar-2006: jobollin updated this page to use the new styles
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.JammModelElement" %>
<%@ page import="org.recipnet.site.content.rncontrols.SampleTextIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.ShowSamplePage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.bl.UserBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:showSamplePage
    setPreferenceTo="<%=UserBL.ShowsampleViewPref.BASIC%>"
    usePreferredNameAsTitlePrefix="true" labAndNumberSeparator=" sample "
    titleSuffix=" - Reciprocal Net Common Molecule"
    overrideReinvocationServletPath="/showsample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <rn:sampleChecker includeIfRetracted="true">
    <p style="text-align: center; text-color: #F00000;">
      This sample has been retracted by its lab, possibly because it is
      partially or wholly incorrect.  Please examine this
      sample's comments for more details.
    </p>
  </rn:sampleChecker>
  <table cellspacing="0" cellpadding="0"
         style="width: 100%; margin-bottom: 0.5em;">
  <tr>
  <td rowspan="2" style="width: 400px;">
    <rn:jammModel id="jammModel" repositoryFile="${param['crtFile']}">
      <rn:jammApplet id="jamm" width="400" height="400" appletParam="jamm"/>
      <ctl:errorMessage errorSupplier="${jammModel}"
          errorFilter="<%=JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND%>">
        &nbsp;<img src="images/nocrtfile.gif" alt="no CRT file available"/>
      </ctl:errorMessage>
      <ctl:errorMessage invertFilter="true"
          errorFilter="<%=JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND%>">
        <table cellspacing="0">
          <tr>
            <td style="vertical-align: top">
              <img src="images/tip.gif" alt="TIP &gt;"/>
            </td>
            <td colspan="3" class="showSampleTipText">
                Click and drag your mouse inside the applet above
                to rotate the molecule in 3-D.
                <a href="help/applets.html" style="padding-left: 1em;"
                    target="_blank">Applet instructions...</a>
            </td>
          </tr>
        </table>
        <p class="showSampleTipText">
          Switch to another visualization applet:
        </p>
        <table cellspacing="0">
          <ctl:stringIterator id="appletChoices" strings="miniJaMM,JaMM1,JaMM2">
          <tr>
            <td>
              <rn:jammChecker jammAppletTag="${jamm}"
                  includeOnlyIfCurrentAppletIs="${appletChoices.currentString}">
                <img src="images/pointer.gif" alt="&gt;"/>
              </rn:jammChecker>
              <rn:jammChecker jammAppletTag="${jamm}" invert="true"
                  includeOnlyIfCurrentAppletIs="${appletChoices.currentString}">
                <img src="images/pointer-blank.gif" alt="-"/>
              </rn:jammChecker>
            </td>
            <td>
              <rn:jammAppletPreservingLink
                  jammAppletTag="${jamm}"
                  switchApplet="${appletChoices.currentString}"
                  disableWhenLinkMatchesAppletTag="true"
                  >${appletChoices.currentString}</rn:jammAppletPreservingLink>
            </td>
            <td style="padding-left: 2em">
              <rn:jammChecker jammAppletTag="${jamm}"
                  includeOnlyIfCurrentAppletIs="${appletChoices.currentString}">
                <rn:jammAppletPreservingLink href="/jamm.jsp"
                    jammAppletTag="${jamm}" openInWindow="true"
                    disableWhenLinkMatchesAppletTag="false"
                    >open in new window...</rn:jammAppletPreservingLink>
              </rn:jammChecker>
            </td>
          </tr>
          </ctl:stringIterator>
        </table>
      </ctl:errorMessage>
    </rn:jammModel>
  </td>
  <td class="dataTable">
    <h1>
      <rn:sampleNameIterator excludeAllButPreferredName="true"
                  id="preferredName">
        <rn:sampleField displayAsLabel="true" />
      </rn:sampleNameIterator>
      <ctl:errorMessage errorSupplier="${preferredName}"
          errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
        <rn:labField />
        #<rn:sampleField fieldCode="<%=SampleInfo.ID%>" displayAsLabel="true" />
      </ctl:errorMessage>
    </h1>
    <rn:sampleChecker
        includeIfValueIsPresent="<%=SampleTextBL.SHORT_DESCRIPTION%>">
      <h2><rn:sampleField displayAsLabel="true"
                    fieldCode="<%=SampleTextBL.SHORT_DESCRIPTION%>" /></h2>
    </rn:sampleChecker>
    <rn:preferredFormulaChecker>
      <div class="dataItem">
        <span class="itemLabel">Chemical Formula:</span>
        <rn:sampleField displayAsLabel="true" />
      </div>
    </rn:preferredFormulaChecker>

    <rn:sampleNameIterator excludePreferredName="true"
        id="otherNames"><%--
      --%><ctl:parityChecker includeOnlyOnFirst="true">
      <div class="dataItem">
      <span class="itemLabel">Other names:</span>
      </ctl:parityChecker><%--
      --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
      </ctl:parityChecker>
      <rn:sampleField displayAsLabel="true" /><%--
      --%><ctl:parityChecker includeOnlyOnLast="true">
      </div>
      </ctl:parityChecker><%--
    --%></rn:sampleNameIterator>

    <rn:sampleTextIterator
        restrictByTextType="<%=SampleTextBL.LAYMANS_EXPLANATION%>">
      <div class="dataItem">
        <span class="itemLabel"><rn:sampleFieldLabel />:</span>
        <rn:sampleField displayAsLabel="true" />
      </div>
    </rn:sampleTextIterator>

    <rn:sampleTextIterator restrictByTextType="<%=SampleTextBL.KEYWORD%>"
        id="keywords"><%--
    --%><ctl:parityChecker includeOnlyOnFirst="true">
      <div class="dataItem">
        <span class="itemLabel">Keywords:</span>
    </ctl:parityChecker><%--
    --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
    </ctl:parityChecker>
    <rn:sampleField displayAsLabel="true" /><%--
    --%><ctl:parityChecker includeOnlyOnLast="true">
      </div>
    </ctl:parityChecker><%--
    --%></rn:sampleTextIterator>
  </td>
  </tr>
  <tr>
  <td class="navLinks">
      <rn:link href="/showsampledetailed.jsp">Crystallographic
        details...</rn:link><br />
      <rn:link href="/jamm.jsp">More visualization options...</rn:link><br />
      <rn:link href="/sampleversions.jsp">See other versions...</rn:link><%--
      --%><rn:authorizationChecker suppressRenderingOnly="true"
          canEditSample="true"><br />
        <rn:link href="/lab/sample.jsp">Edit this sample...</rn:link>
      </rn:authorizationChecker>
  </td>
  </tr>
  </table>
  </div>
  <ctl:styleBlock>
    body               { background-color: #FFFFCC; }
    div.pageBody       { text-align: left; }
    td.dataTable       { vertical-align: top; }
    td.dataTable h1,
    td.dataTable h2,
    td.dataTable div   { margin-left: 1.5em; }
    h1 + div, h2 + div { margin-top: 1.5em; }
    td.dataTable h1,
    td.dataTable h2    { text-align: center; margin-bottom: 0.25em; }
    td.dataTable h1    { font-size: 130%; font-weight: bold; color: #660033; }
    td.dataTable div   { font-family: Times, serif; font-size: medium; 
                         margin-bottom: 0.5em; }
    td.dataTable span.itemLabel { color: #660033; font-weight: bold;}
    td.navLinks        { text-align: right; vertical-align: bottom;
                         font-family: inherit; font-size: inherit; 
                         padding-top: 1em; }
    .showSampleTipText { font-size: small; }
  </ctl:styleBlock>
</rn:showSamplePage>
