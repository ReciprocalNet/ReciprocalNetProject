<%--
  - Reciprocal Net project
  -
  - showsampledetailed.jsp
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
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 23-Sep-2005: midurbin replaced jumpToSample with ShowsampleLink
  - 21-Oct-2005: midurbin added file descriptions
  - 28-Oct-2005: midurbin added support to suppress file descriptions based on
  -              the user's preferences
  - 29-Mar-2006: jobollin updated this page to use the new styles
  - 29-Dec-2007: ekoperda added display of Status field
  - 11-Jan-2008: ekoperda added display of LTA's
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.FileField" %>
<%@ page import="org.recipnet.site.content.rncontrols.FileIterator" %>
<%@ page import="org.recipnet.site.content.rncontrols.JammModelElement" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ShowSamplePage" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.bl.UserBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleDataInfo" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:showSamplePage useLabAndNumberAsTitlePrefix="true"
    setPreferenceTo="<%=UserBL.ShowsampleViewPref.DETAILED%>"
    labAndNumberSeparator=" sample " titleSuffix=" - Reciprocal Net"
    overrideReinvocationServletPath="/showsample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason" >
  <div class="pageBody">
  <rn:sampleChecker includeIfRetracted="true">
    <p style="text-align: center; text-color: #F00000;">
      This sample has been retracted by its lab, possibly because it is
      partially or wholly incorrect.  Please examine this
      sample's comments for more details.
    </p>
  </rn:sampleChecker>
  <table style="width: 100%; margin-bottom: 0.5em;">
  <tr>
  <td rowspan="2" style="width: 400px; vertical-align: top;">
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
        <p style="font-size: small;">
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
  <td>
    <table class="dataTable" cellSpacing="0">
      <ctl:intIterator id="fieldCodes"
                       ints="<%=new int[] {SampleTextBL.EMPIRICAL_FORMULA,
                                SampleDataInfo.A_FIELD,
                                SampleDataInfo.B_FIELD,
                                SampleDataInfo.C_FIELD,
                                SampleDataInfo.ALPHA_FIELD,
                                SampleDataInfo.BETA_FIELD,
                                SampleDataInfo.GAMMA_FIELD,
                                SampleDataInfo.V_FIELD,
                                SampleDataInfo.SPGP_FIELD,
                                SampleDataInfo.DCALC_FIELD,
                                SampleDataInfo.COLOR_FIELD,
                                SampleDataInfo.Z_FIELD,
                                SampleDataInfo.T_FIELD,
                                SampleDataInfo.FORMULAWEIGHT_FIELD,
                                SampleDataInfo.RF_FIELD,
                                SampleDataInfo.RWF_FIELD,
                                SampleDataInfo.RF2_FIELD,
                                SampleDataInfo.RWF2_FIELD,
                                SampleDataInfo.SUMMARY_FIELD}%>">
      <rn:sampleChecker
          includeIfValueIsPresent="${fieldCodes.currentInt}">
        <tr>
          <th><rn:sampleFieldLabel fieldCode="${fieldCodes.currentInt}" />:</th>
          <td>
            <rn:sampleField displayAsLabel="true"
                fieldCode="${fieldCodes.currentInt}" />
            <span class="dataTableUnits"><rn:sampleFieldUnits
                  fieldCode="${fieldCodes.currentInt}" /></span>
          </td>
        </tr>
      </rn:sampleChecker>
      </ctl:intIterator>

      <%-- every attribute that is visible except the empirical formula,
           which was already displayed--%>
      <rn:sampleTextIterator restrictToAttributes="true"
          restrictToTextTypesOtherThan="<%=
              SampleTextBL.EMPIRICAL_FORMULA%>">
        <rn:authorizationChecker canSeeSampleText="true"
            requireAuthentication="false" suppressRenderingOnly="true">
          <tr>
            <th><rn:sampleFieldLabel />:</th>
            <td>
              <rn:sampleField displayAsLabel="true" />
              <span class="dataTableUnits"><rn:sampleFieldUnits /></span>
            </td>
          </tr>
        </rn:authorizationChecker>
      </rn:sampleTextIterator>

      <rn:sampleTextIterator restrictToAnnotations="true">
        <rn:authorizationChecker canSeeSampleText="true"
            requireAuthentication="false" suppressRenderingOnly="true">
          <tr>
            <th><rn:sampleFieldLabel />:</th>
            <td>
              <rn:sampleField displayAsLabel="true" />
              <span class="dataTableUnits"><rn:sampleFieldUnits /></span>
              <rn:sampleTextChecker
                   includeOnlyForAnnotationsWithReferences="true">
                <rn:sampleContextTranslator
                    translateFromAnnotationReferenceSample="true">
                  (jump to <rn:showsampleLink
                      detailedPageUrl="/showsampledetailed.jsp"
                      basicPageUrl="/showsamplebasic.jsp"
                      jumpSitePageUrl="/showsamplejumpsite.jsp">
                    <rn:sampleParam name="sampleId" />
                    <rn:labField fieldCode="<%=LabField.SHORT_NAME%>" />
                    sample 
                    <rn:sampleField displayValueOnly="true"
                        fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
                  /></rn:showsampleLink>)
                </rn:sampleContextTranslator>
              </rn:sampleTextChecker>
            </td>
          </tr>
        </rn:authorizationChecker>
      </rn:sampleTextIterator>

      <tr>
        <th>Lab name:</th>
        <td><rn:labField /></td>
      </tr>
      <tr>
        <th>Sample provider:</th>
        <td><rn:providerField /></td>
      </tr>
      <rn:ltaIterator id="ltas" considerShowSampleVisibility="true" 
          skipBlankValues="true">
        <tr>
          <th>
            <rn:sampleFieldLabel />:
          </th>
          <td>
            <rn:sampleField displayValueOnly="true" /><rn:sampleFieldUnits />
          </td>
        </tr>
      </rn:ltaIterator>
      <tr>
        <th>
          <rn:sampleFieldLabel fieldCode="<%=SampleInfo.STATUS%>" />:
        </th>
        <td>
          <rn:sampleField fieldCode="<%=SampleInfo.STATUS%>" />
        </td>
      </tr>
    </table>
  </td>
  </tr>

  <tr>
    <td class="navLinks">
      <rn:link href="/showsamplebasic.jsp">Basic view...</rn:link>
      <br />
      <rn:link href="/jamm.jsp">More visualization options...</rn:link>
      <br />
      <rn:link href="/sampleversions.jsp">See other versions...</rn:link>
      <br />
      <rn:authorizationChecker suppressRenderingOnly="true"
          canEditSample="true">
        <rn:link href="/lab/sample.jsp">Edit this sample...</rn:link>
        <br />
      </rn:authorizationChecker>
    </td>
  </tr>
  </table>
  <table class="fileTable">
    <rn:fileIterator id="fileIt"
        requestUnavailableFilesParamName="makeFilesAvailable">
      <ctl:parityChecker includeOnlyOnFirst="true">
        <tr>
          <th colspan="5">Repository Files:</th>
      </ctl:parityChecker>
      <ctl:parityChecker includeOnlyOnMultiplesOf="5">
        </tr>
        <tr>
      </ctl:parityChecker>
        <td>
          <rn:fileField id="file" fieldCode="<%=FileField.LINKED_FILENAME%>" />
          <ctl:errorMessage errorSupplier="<%=file%>"
              errorFilter="<%=FileField.FILE_AVAILABLE_UPON_REQUEST%>">
            <img src="images/star.gif" alt="*">
          </ctl:errorMessage>
          <ctl:errorMessage errorSupplier="<%=file%>"
              errorFilter="<%=FileField.FILE_UNAVAILABLE%>">
            <img src="images/star.gif" alt="*">
          </ctl:errorMessage>
          <rn:fileChecker includeOnlyIfFileHasDescription="true">
            <rn:preferenceChecker includeIfBooleanPrefIsTrue="<%=
                UserBL.Pref.SUPPRESS_DESCRIPTIONS%>" invert="true">
              <br />
              <font class="description">
                <rn:fileField fieldCode="<%=FileField.DESCRIPTION%>" />
              </font>
            </rn:preferenceChecker>
          </rn:fileChecker>
        </td>
        <ctl:parityChecker includeOnlyOnLast="true">
          </tr>
        </ctl:parityChecker>
    </rn:fileIterator>
    <ctl:errorMessage errorSupplier="${fileIt}"
        errorFilter="<%=FileIterator.SOME_FILES_AVAILABLE_UPON_REQUEST%>">
      <tr>
        <td colspan="5" style="padding-top: 0.5em;">
            <img name="star" src="images/star.gif" alt="*"/>
            These large files are available upon request.
            <rn:link href="/showsampledetailed.jsp">
              Make them available...
              <ctl:linkParam name="makeFilesAvailable" value="true" />
            </rn:link>
        </td>
      </tr>
    </ctl:errorMessage>
    <ctl:errorMessage errorSupplier="${fileIt}"
        errorFilter="<%=FileIterator.SOME_FILES_UNAVAILABLE%>">
      <tr>
        <td colspan="5" style="padding-top: 0.5em;">
            <img name="star" src="images/star.gif" alt="*"/>
            You lack authorization to download these large files.
        </td>
      </tr>
    </ctl:errorMessage>
    <ctl:errorMessage errorSupplier="${fileIt}"
            errorFilter="<%=(FileIterator.DIRECTORY_BUT_NO_HOLDINGS
            | FileIterator.HOLDINGS_BUT_NO_DIRECTORY
            | FileIterator.NO_DIRECTORY_NO_HOLDINGS)%>">
      <tr>
        <td class="errorMessage">
            Repository is not available.
        </td>
      </tr>
    </ctl:errorMessage>
    <ctl:errorMessage errorSupplier="${fileIt}"
        errorFilter="<%=FileIterator.NO_FILES_IN_REPOSITORY%>">
      <tr>
        <td class="errorMessage">
          No files for <rn:sampleField fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>"
            displayAsLabel="true" /> in repository!
        </td>
      </tr>
    </ctl:errorMessage>
  </table>
  </div>
  <ctl:styleBlock>
    div.pageBody { margin-top: 0.75em; text-align: inherit; }
    table.dataTable { margin-left: 1.5em }
    table.dataTable th { vertical-align: baseline; text-align: right;
        font-style: narrow; font-weight: normal; color: gray;
        white-space: nowrap; padding: 0;}
    table.dataTable td { vertical-align: baseline; text-align: left;
        color: #000040; padding: 0 0 0 0.25em;}
    .dataTableUnits { color: gray; }
    table.fileTable { clear: both; margin-top: 0.5em; }
    table.fileTable th,
    table.fileTable td { vertical-align: top; text-align: left; padding: 0.1em;}
    table.fileTable th { background-color: #f4f4ff; }
    table.fileTable td { width: 20%; background-color: #f4f4f4; }
    .description { font: italic small Times, serif; color: gray; }
    td.navLinks { padding-top: 1em; text-align: right; }
  </ctl:styleBlock>
</rn:showSamplePage>
