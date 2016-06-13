<%--
 - Reciprocal Net project
 - lab/sample.jsp
 -
 - 11-Jul-2002: leqian wrote first draft
 - 12-Aug-2002: leqian and ekoperda fixed bug #283 by allowing the user to
 -              create a repository directory if one does not already exist.
 - 14-Aug-2002: leqian changed UI #347
 - 19-Aug-2002: eisiorho added empirical/Structural formula, task #284
 - 28-Aug-2002: leqian added displayOneItem functions and added displaying
 -              local tracking ability. Task #391, #401
 - 10-Sep-2002: eisiorho added appropriate measurement unit for "scientific
 -              data"
 - 18-Sep-2002: ekoperda fixed bug #462 (correct symbol for Angstroms)
 - 19-Sep-2002: eisiorho fixed bug #443 (render correctly in Netscape 4.7)
 - 15-Oct-2002: eisiorho updated workflow links to be visible only if
 -              PageHelper.isActionValid() returns true.
 - 07-Nov-2002: eisiorho added localtracking.jsp as a link
 - 19-Dec-2002: eisiorho added logic so that if there is no available .crt
 -              file, no applet will try to load.
 - 27-Jan-2003: adharurk renamed sample status and action constants
 -              (new workflow)
 - 30-Jan-2003: midurbin added action link to declarenonscs.jsp
 - 03-Feb-2003: midurbin changed stall.jsp link to suspend.jsp
 - 14-Feb-2003: adharurk changed withdraw.jsp link to retract,jsp
 - 06-Feb-2003: midurbin added action link to failedtocollect.jsp
 - 20-Feb-2003: yli added action link to declareincomplete.jsp
 - 21-Feb-2003: yli renamed action link solve.jsp to refinestructure.jsp
 - 19-Feb-2003: dfeng renamed link gatherdata.jsp to
 -              collectpreliminarydata.jsp
 - 11-Mar-2003: ajooloor added action link to withdraw.jsp
 - 19-Mar-2003: ajooloor changed decimal precision for alpha, beta, gamma,
 -              and volume fields
 - 26-Mar-2003: dfeng exposed sample status in UI
 - 28-Mar-2003: jobollin reorganized the action menu (task #824)
 - 21-Apr-2003: adharurk added a call to reportSampleView() in PageHelper.
 - 28-Apr-2003: midurbin rewrote page to utilize WorkflowHelper (complete UI
 -              redesign)
 - 01-May-2003: midurbin fixed bug #892
 - 30-May-2003: midurbin changed repository file support to utilize FileHelper.
 - 02-Jun-2003: ajooloor added calls to HtmlHelper.prettyFileSize()
 - 05-Jun-2003: ajooloor changed call from PageHelper.isActionValid() to
 -              WorkflowHelper.isActionValid()
 - 05-Jun-2003: adharurk added "action" as query line parameter to suspend.jsp
 - 10-Jun-2003: midurbin updated the link to "files.jsp" and moved
 -              RepositoryDirectoryNotFoundException handling to this file.
 - 13-Jun-2003: midurbin added action/display filters
 -              WorkflowHelper.isActionValid()
 - 17-Jun-2003: midurbin included labId in pageHelper.reportSampleView() call
 - 24-Jun-2003: midurbin removed action link to 'rawedit.jsp'.
 - 01-Jul-2003: midurbin fixed bug #948
 - 31-Jul-2003: midurbin fixed bug #997
 - 01-Aug-2003: eisiorho fixed bug #975, added function formatReferenceLink()
 - 12-Aug-2003: ajooloor fixed bug #1010
 - 07-Jan-2004: ekoperda changed package references due to source tree
 -              reorganization
 - 02-Apr-2004: midurbin added special formatting for IUPAC names
 - 05-Apr-2004: cwestnea made changes to ui per task #1055
 - 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
 - 29-Sep-2004: midurbin replaced link to localtracking.jsp with one to
 -              customfields.jsp to reflect the name change
 - 14-Dec-2004: eisiorho changed textype references to use new class
 -              SampleTextBL
 - 25-Jun-2005: midurbin rewrote using custom tags
 - 05-Jul-2005: midurbin updated link to samplehistory.jsp to eliminate the
 -              'mode' parameter
 - 13-Jul-2005: midurbin added new required parameters to editSamplePage
 - 05-Aug-2005: midurbin added SampleFieldUnits tags
 - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
 - 23-Sep-2005: midurbin added ShowsampleLink
 - 17-Oct-2005: midurbin added names of added/removed files
 - 21-Oct-2005: midurbin added box styles for actions 10800 and 10900, added
 -              file descriptions and created separate button links for file
 -              management and file upload
 - 27-Oct-2005: midurbin added names of modified files
 - 28-Oct-2005: midurbin added support to suppress file descriptions based on
 -              the user's preferences; exposed new suppression preferences
 - 10-Nov-2005: midurbin added a message to action boxes that were skipped by 
 -              reversion
 - 18-Jan-2006: jobollin added "Generate Files" button
 - 16-Mar-2006: jobollin removed uses of <ctl:link>'s param and value
 -              attributes
 - 28-Mar-2006: jobollin updated this page to accommodate ParityChecker mods
 - 03-Apr-2006: jobollin reintroduced the inactive version of the "Withdraw"
 -              control
 - 27-Apr-2006: jobollin fixed bug #1783
 - 03-Jan-2008: ekoperda prettified UI to fix bug #1851
 --%>
<%@ page import="org.recipnet.common.controls.HtmlPageIterator"
  import="org.recipnet.site.content.rncontrols.DataDirectoryField"
  import="org.recipnet.site.content.rncontrols.EditSamplePage"
  import="org.recipnet.site.content.rncontrols.FileField"
  import="org.recipnet.site.content.rncontrols.FileIterator"
  import="org.recipnet.site.content.rncontrols.LabField"
  import="org.recipnet.site.content.rncontrols.PreferenceField"
  import="org.recipnet.site.content.rncontrols.PreferenceField.PrefType"
  import="org.recipnet.site.content.rncontrols.SampleActionFieldIterator"
  import="org.recipnet.site.content.rncontrols.SampleActionFileIterator"
  import="org.recipnet.site.content.rncontrols.SampleActionIterator"
  import="org.recipnet.site.content.rncontrols.SampleField"
  import="org.recipnet.site.content.rncontrols.SampleHistoryField"
  import="org.recipnet.site.shared.bl.SampleTextBL"
  import="org.recipnet.site.shared.bl.SampleWorkflowBL"
  import="org.recipnet.site.shared.bl.UserBL"
  import="org.recipnet.site.shared.db.SampleInfo"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl"%>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn"%>
<rn:editSamplePage title="Edit Sample" ignoreSampleHistoryId="true"
  unsetSuppressionPreferenceParamName="unsetSuppression"
  loginPageUrl="/login.jsp" currentPageReinvocationUrlParamName="origUrl"
  authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <ctl:selfForm>
    <table cellspacing="0" border="0" cellpadding="0">
      <tr>
        <td style="text-align: left; vertical-align: top; width: 300px;">
        <ctl:errorMessage errorFilter="<%=
              EditSamplePage.NESTED_TAG_REPORTED_VALIDATION_ERROR%>">
          <div class="errorBox">
            <div class="errorBoxTitle">
              <ctl:img src="/images/error.gif" styleClass="icon"/>
              Error
            </div>
            <div class="errorBoxBody">
              <div>
                One or more errors were encountered while processing this
                page. Please correct the indicated input boxes and
                resubmit the form.
              </div>
              <div style="text-align: right">
                (<a href="#error">jump to error on this page</a>)
              </div>
            </div>
          </div>
        </ctl:errorMessage>
        <div class="infoBox">
          <div class="infoBoxTitle">
            <ctl:img src="/images/basicinfo.gif" styleClass="icon"/>
            Basic Information
          </div>
          <div class="infoBoxBody">
            <table cellspacing="0">
              <tr>
                <th><rn:sampleFieldLabel
                       fieldCode="<%= SampleInfo.LOCAL_LAB_ID %>" />:</th>
                <td><rn:sampleField
                  displayValueOnly="true"
                  fieldCode="<%= SampleInfo.LOCAL_LAB_ID %>" /></td>
              </tr>
              <tr>
                <th>Lab name:</th>
                <td><rn:labField /></td>
              </tr>
              <tr>
                <th>Provided by group:</th>
                <td><rn:providerField/></td>
              </tr>
            </table>
          </div>
        </div>
        <div class="infoBox">
          <div class="infoBoxTitle">
            <ctl:img src="/images/otherviews.gif" styleClass="icon"/>
            Other views
          </div>
          <div class="infoBoxBody">
            <ul>
              <li>
                <rn:link href="/lab/samplehistory.jsp">Sample History</rn:link>
              </li>
              <li>
                <rn:showsampleLink basicPageUrl="/showsamplebasic.jsp"
                    detailedPageUrl="/showsampledetailed.jsp"
                    sampleIsKnownToBeLocal="true">
                  <rn:sampleParam name="sampleId" />
                  Sample detail
                </rn:showsampleLink>
              </li>
              <li>
                <rn:link href="/jamm.jsp">JaMM Visualization</rn:link>
              </li>
            </ul>
          </div>
        </div>
        <div class="infoBox">
          <div class="infoBoxTitle">
            <ctl:img src="/images/status.gif" styleClass="icon"/>
            Status
          </div>
          <div class="infoBoxBody">
            <ul>
              <rn:statusDisplay
                unselectedStatusPatternHtml=
                    "<li class='otherStatus'>[statusName]</li>"
                selectedStatusPatternHtml=
                    "<li class='selectedStatus'>[statusName]</li>"/>
            </ul>
          </div>
        </div>
        <div class="infoBox">
          <div class="infoBoxTitle">
            <ctl:img src="/images/managefiles.gif" styleClass="icon"/>
            Data files
          </div>
          <div class="infoBoxBody">
            <rn:fileIterator id="files">
              <ctl:parityChecker includeOnlyOnFirst="true">
                <ul>
              </ctl:parityChecker>
                <li><rn:fileField
                        fieldCode="<%= FileField.LINKED_FILENAME%>" />
                    (<rn:fileField fieldCode="<%= FileField.FILE_SIZE%>" />)
                  <rn:fileChecker includeOnlyIfFileHasDescription="true">
                    <rn:preferenceChecker
                        includeIfBooleanPrefIsTrue="<%=
                                           UserBL.Pref.SUPPRESS_DESCRIPTIONS%>"
                        invert="true">
                      <div class="fileDescription">
                        <rn:fileField fieldCode="<%= FileField.DESCRIPTION%>" />
                      </div>
                    </rn:preferenceChecker>
                  </rn:fileChecker>
                </li>
                <ctl:parityChecker includeOnlyOnLast="true">
                  </ul>
                </ctl:parityChecker>
              </rn:fileIterator>
              <ctl:errorMessage errorSupplier="<%=files%>"
                  errorFilter="<%= FileIterator.NO_FILES_IN_REPOSITORY%>">
                There are no files in the repository.
              </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=files%>" errorFilter="<%=
                    FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                    | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                    | FileIterator.HOLDINGS_BUT_NO_DIRECTORY%>">
              <div class="errorMessage"> The file repository is not configured
                (correctly) for sample
                <rn:sampleField fieldCode="<%= SampleInfo.LOCAL_LAB_ID%>"
                    displayValueOnly="true" />:
              </div>
              <ctl:errorMessage errorSupplier="<%=files%>" errorFilter="<%=
                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS%>">
                <div class="errorMessage" style="margin-left: 1em;">
                  Data directory found but no holding record.<br />
                </div>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=files%>"
                    errorFilter="<%= FileIterator.HOLDINGS_BUT_NO_DIRECTORY%>">
                <div class="errorMessage">
                  Data directory missing.
                </div>
              </ctl:errorMessage>
              <ctl:errorMessage errorSupplier="<%=files%>" errorFilter="<%=
                  FileIterator.NO_DIRECTORY_NO_HOLDINGS%>">
                Data directory missing and no holding record.
                <div> You should create a
                  directory in the repository to store this sample's
                  data files. Please specify the directory to be created
                  below. You may leave the text box blank if you wish to
                  have the data directory created in the default
                  location.
                </div>
                <div style="margin-top: 1em;">
                  <rn:dataDirectoryField directoryPart="<%=
                      DataDirectoryField.FieldCode.FIRST_PART%>"
                      fileIterator="<%=files%>" />/ <br />
                    &nbsp;&nbsp;
                    <rn:dataDirectoryField id="extpath" directoryPart="<%=
                        DataDirectoryField.FieldCode.EXTENSION_PATH%>"
                        fileIterator="<%=files%>" />/
                      <rn:dataDirectoryField directoryPart="<%=
                          DataDirectoryField.FieldCode.LAST_PART%>"
                          fileIterator="<%=files%>" /> <br />
                        <center><rn:createDataDirectoryButton label="Create"/>
                        </center>
                </div>
                <ctl:errorMessage errorSupplier="<%=extpath%>">
                  <div class="errorMessage">
                    <a name="error" />The extention path is invalid either
                    because it contains an invalid character or because it is
                    set to a reserved word. Please select a different extension
                    path and press "Create" again.
                  </div>
                </ctl:errorMessage>
              </ctl:errorMessage>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=files%>"
                  invertFilter="true"
                  errorFilter="<%=
                                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                                  | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                             | FileIterator.HOLDINGS_BUT_NO_DIRECTORY%>">
              <div style="text-align: right; margin: 0.25em 0 0.25em 0;">
                <rn:uploadFilesButton
                      formBasedPageUrl="/lab/uploadfilesform.jsp"
                      dragAndDropPageUrl="/lab/uploadfilesapplet.jsp"
                      label="Upload files..." />
              </div>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=files%>"
                  errorFilter="<%=
                                  FileIterator.DIRECTORY_BUT_NO_HOLDINGS
                                  | FileIterator.NO_DIRECTORY_NO_HOLDINGS
                              | FileIterator.HOLDINGS_BUT_NO_DIRECTORY
                              | FileIterator.NO_FILES_IN_REPOSITORY%>"
                  invertFilter="true">
              <div style="text-align: right; margin: 0.25em 0 0.25em 0;">
                <rn:buttonLink target="/lab/generatefiles.jsp"
                    label="Generate files..." />
              </div>
              <div style="text-align: right; margin: 0.25em 0 0.25em 0;">
                <rn:buttonLink target="/lab/managefiles.jsp"
                    label="Manage files..." />
              </div>
            </ctl:errorMessage>
          </div>
        </div>
        </td>
        <td class="actionColumn">
          <rn:preferenceChecker invert="true" includeIfBooleanPrefIsTrue="<%=
                  UserBL.Pref.SUPPRESS_SUPPRESSION%>">
          <table class="preferenceTable" cellspacing="0" cellpadding="0"
              style="text-align: left;">
            <ctl:browserSpecific notNetscape4x="true">
              <tr>
                <td rowspan="3" bgcolor="#FFFFFF" valign="top" align="right">
                <ctl:img src="/images/grayarch.gif" styleClass="grayArcImage" />                </td>
                <td colspan="3">&nbsp;</td>
              </tr>
            </ctl:browserSpecific>
            <tr>
              <th style="font-style:normal; color: black; vertical-align: top; padding-left: 0.5em">Suppress:</th>
              <td><ctl:intIterator id="prefs" ints="<%= new int[] {
                    PrefType.SUPPRESS_BLANK_FIELD.ordinal(),
                    PrefType.SUPPRESS_COMMENTS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_FILE_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_CORRECTION_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_EMPTY_OTHER_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_SKIPPED_ACTIONS.ordinal(),
                    PrefType.SUPPRESS_BLANK_FILE_LISTINGS.ordinal(),
                    PrefType.SUPPRESS_SUPPRESSION.ordinal()
                  } %>">
                  <span class="preferenceItem"><rn:preferenceField
                      id="prefField" preferenceType="<%=
                      PrefType.values()[prefs.getCurrentInt()] %>" />
                    ${ prefField.preferenceType.defaultLabel }</span>
                </ctl:intIterator>
              </td>
              <td><rn:storePreferencesButton label="Apply" /></td>
            </tr>
            <ctl:browserSpecific notNetscape4x="true">
              <tr>
                <td colspan="3"><ctl:img src="/images/graycurve.gif"
                    styleClass="grayCurveImage" /></td>
              </tr>
            </ctl:browserSpecific>
          </table>
        </rn:preferenceChecker>
        <ctl:phaseEvent onPhases="registration">
          <jsp:useBean id="closeBody"
              class="org.recipnet.site.wrapper.StringBean"/>
        </ctl:phaseEvent>
        <rn:sampleActionIterator id="actionIt">
          <jsp:setProperty name="closeBody" property="string" value=""/>
          <div class="actionBox<rn:sampleHistoryField fieldCode="<%=
                      SampleHistoryField.FieldCode.ACTION_CODE%>" />">
            <div class="actionBox">
              <ctl:errorChecker errorFilter="<%=
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE
                    | SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE
              %>">
              <div class="twoLineActionBoxTitle">
              </ctl:errorChecker>
              <ctl:errorChecker invert="true" errorFilter="<%=
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE
                    | SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE
              %>">
              <div class="actionBoxTitle">
              </ctl:errorChecker>
                <rn:actionIcon styleClass="icon"/>
                <span class="bright"><rn:sampleHistoryField fieldCode="<%=
                    SampleHistoryField.FieldCode.ACTION_PERFORMED%>" /></span>
                <rn:sampleHistoryField fieldCode="<%=
                    SampleHistoryField.FieldCode.ACTION_DATE%>" /> by
                <span class="bright"><rn:sampleHistoryField
                    fieldCode="<%= SampleHistoryField.FieldCode.USER_FULLNAME_THAT_PERFORMED_ACTION%>" />
                </span>
                <ctl:errorMessage errorFilter="<%=
                    SampleActionIterator.ACTION_WAS_CORRECTED_ONCE%>"><br/>
                  1 revision made subsequently
                </ctl:errorMessage>
                <ctl:errorMessage errorFilter="<%=
                    SampleActionIterator.ACTION_WAS_CORRECTED_MORE_THAN_ONCE%>"><br/>
                  <rn:correctionCount /> revisions made subsequently
                </ctl:errorMessage>
              </div>
              <rn:sampleActionFieldIterator id="fieldIt">
                <ctl:parityChecker includeOnlyOnFirst="true">
                  <div class="actionBoxBody">
                    <table>
                    <tr>
                    <td style="width: 100%;">
                    <table cellspacing="0">
                  <ctl:phaseEvent onPhases="fetching,rendering"
                      skipIfSuppressed="true">
                    <jsp:setProperty name="closeBody" property="string"
                        value="</div>"/>
                  </ctl:phaseEvent>
                </ctl:parityChecker>
                  <tr>
                    <th style="width: 35%"><rn:sampleFieldLabel />:</th>
                    <td style="width: 65%">
                      <rn:sampleChecker
                          includeIfSampleFieldContextProvidesValue="true">
                        <rn:sampleField displayValueOnly="true" />
                        <rn:sampleFieldUnits />
                        <span class="light"><rn:sampleTextChecker
                            includeOnlyForAnnotationsWithReferences="true">
                          <rn:sampleContextTranslator
                              translateFromAnnotationReferenceSample="true">
                            (jump to
                            <rn:showsampleLink
                                detailedPageUrl="/showsampledetailed.jsp"
                                basicPageUrl="/showsamplebasic.jsp"
                                jumpSitePageUrl="/showsamplejumpsite.jsp">
                              <rn:sampleParam name="sampleId" />
                              <rn:labField fieldCode="<%=
                                  LabField.SHORT_NAME%>" /> sample
                              <rn:sampleField displayValueOnly="true"
                                  fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
                            </rn:showsampleLink>)
                          </rn:sampleContextTranslator>
                        </rn:sampleTextChecker></span>
                      </rn:sampleChecker>
                      <rn:sampleChecker invert="true"
                          includeIfSampleFieldContextProvidesValue="true">
                        <span class="light">(no value)</span>
                      </rn:sampleChecker>
                    </td>
                  </tr>
                  <ctl:parityChecker includeOnlyOnLast="true">
                    <ctl:errorChecker errorSupplier="${fieldIt}" invert="true"
                        errorFilter="<%=
                      SampleActionFieldIterator.SOME_FIELDS_WERE_SUPPRESSED%>">
                      </table>
                      </td>
                    </ctl:errorChecker>
                  </ctl:parityChecker>
                </rn:sampleActionFieldIterator>
                <ctl:errorMessage errorSupplier="${fieldIt}" errorFilter="<%=
                    SampleActionFieldIterator.SOME_FIELDS_WERE_SUPPRESSED%>">
                  <ctl:errorChecker errorSupplier="${fieldIt}" errorFilter="<%=
                    HtmlPageIterator.NO_ITERATIONS%>">
                    <div class="actionBoxBody">
                      <table>
                      <tr>
                      <td style="width: 100%;">
                      <table>
                    <ctl:phaseEvent onPhases="fetching,rendering"
                        skipIfSuppressed="true">
                      <jsp:setProperty name="closeBody" property="string"
                          value="</div>"/>
                    </ctl:phaseEvent>
                  </ctl:errorChecker>
                  <tr>
                    <td colspan="2" class="suppressionCount">
                      (<rn:suppressedFieldCount fieldIterator="${fieldIt}"/>
                      blank fields suppressed)
                    </td>
                  </tr>
                </table>
                </td>
                </ctl:errorMessage>
                <ctl:if conditionMet="${empty closeBody.string}" invert="true">
                <td style="vertical-align: bottom;">
                <rn:correctActionButton label="Revise..."
     submittedCorrectionPageUrl="/lab/submit.jsp"
     preliminaryDataCollectedCorrectionPageUrl="/lab/collectpreliminarydata.jsp"
     structureRefinedCorrectionPageUrl="/lab/refinestructure.jsp"
     releasedToPublicCorrectionPageUrl="/lab/releasetopublic.jsp"
     declaredIncompleteCorrectionPageUrl="/lab/declareincomplete.jsp"
     declaredNonScsCorrectionPageUrl="/lab/declarenonscs.jsp"
     failedToCollectCorrectionPageUrl="/lab/failedtocollect.jsp"
     withdrawnByProviderCorrectionPageUrl="/lab/withdraw.jsp"
     citationCorrectionPageUrl="/lab/addcitation.jsp"
     suspendedCorrectionPageUrl="/lab/suspend.jsp"
     resumedCorrectionPageUrl="/lab/resume.jsp" />
                </td>
                </tr>
                </table>
              ${closeBody.string}
              </ctl:if>
              <rn:sampleActionFileIterator id="addedFiles"
                  mode="<%= SampleActionFileIterator.Mode.ADDED_FILES%>"><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true">
                  <div>
                    Files added:
                </ctl:parityChecker><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
                    <ctl:parityChecker includeOnlyOnLast="true">
                      and
                    </ctl:parityChecker>
                  </ctl:parityChecker>
                  <span class="fileName"><rn:fileField /></span><%--
                  --%><ctl:parityChecker includeOnlyOnLast="true">
                    </div>
                  </ctl:parityChecker><%--
              --%></rn:sampleActionFileIterator>
              <ctl:errorMessage errorSupplier="${addedFiles}"
                errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
                <rn:preferenceChecker
                  includeIfBooleanPrefIsTrue="<%=
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS%>"
                  invert="true">
                  <div>
                    Files added:
                    <span class="fileName">(none)</span>
                  </div>
                </rn:preferenceChecker>
              </ctl:errorMessage>
              <rn:sampleActionFileIterator id="removedFiles"
                  mode="<%= SampleActionFileIterator.Mode.REMOVED_FILES%>"><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true">
                  <div>
                    Files removed:
                </ctl:parityChecker><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
                    <ctl:parityChecker includeOnlyOnLast="true">
                      and
                    </ctl:parityChecker>
                  </ctl:parityChecker>
                  <span class="fileName"><rn:fileField /></span><%--
                  --%><ctl:parityChecker includeOnlyOnLast="true">
                    </div>
                  </ctl:parityChecker><%--
              --%></rn:sampleActionFileIterator>
              <ctl:errorMessage errorSupplier="${removedFiles}"
                errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
                <rn:preferenceChecker
                  includeIfBooleanPrefIsTrue="<%=
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS%>"
                  invert="true">
                  <div>
                    Files removed:
                    <span class="fileName">(none)</span>
                  </div>
                </rn:preferenceChecker>
              </ctl:errorMessage>
              <rn:sampleActionFileIterator id="modifiedFiles"
                  mode="<%= SampleActionFileIterator.Mode.MODIFIED_FILES%>"><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true">
                  <div>
                    Files modified:
                </ctl:parityChecker><%--
                --%><ctl:parityChecker includeOnlyOnFirst="true" invert="true">,
                    <ctl:parityChecker includeOnlyOnLast="true">
                      and
                    </ctl:parityChecker>
                  </ctl:parityChecker>
                  <span class="fileName"><rn:fileField /></span><%--
                  --%><ctl:parityChecker includeOnlyOnLast="true">
                    </div>
                  </ctl:parityChecker><%--
              --%></rn:sampleActionFileIterator>
              <ctl:errorMessage errorSupplier="${modifiedFiles}"
                errorFilter="<%=HtmlPageIterator.NO_ITERATIONS%>">
                <rn:preferenceChecker
                  includeIfBooleanPrefIsTrue="<%=
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS%>"
                  invert="true">
                  <div>
                    Files modified:
                    <span class="fileName">(none)</span>
                  </div>
                </rn:preferenceChecker>
              </ctl:errorMessage>
              <rn:preferenceChecker invert="true" includeIfBooleanPrefIsTrue=
                  "<%=UserBL.Pref.SUPPRESS_COMMENT%>">
                <rn:workflowCommentChecker>
                  <div>
                    Comments:<span class="comments">
                    <rn:sampleHistoryField fieldCode="<%=
                      SampleHistoryField.FieldCode.WORKFLOW_ACTION_COMMENTS%>"/>
                    </span>
                  </div>
                </rn:workflowCommentChecker>
              </rn:preferenceChecker>
              <ctl:errorMessage errorFilter="<%=
                SampleActionIterator.CURRENT_ACTION_WAS_SKIPPED_BY_REVERSION%>">
                <div>
                  <span class="skippedByReversionMessage"> The effect of this
                    action was undone by a subsequent "Reverted" action.</span>
                </div>
              </ctl:errorMessage>
            </div>
          </div>
        </rn:sampleActionIterator>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.ONE_FILE_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_FILE_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_CORRECTION_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_OTHER_ACTION_WAS_SUPPRESSED
                | SampleActionIterator.SOME_OTHER_ACTIONS_WERE_SUPPRESSED
                | SampleActionIterator.ONE_SKIPPED_ACTION_WAS_SUPPRESSED 
                | SampleActionIterator.SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED%>">
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.ONE_FILE_ACTION_WAS_SUPPRESSED%>">
          <div>
            <span class="dark">1</span>
            empty file action box was suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_EMPTY_FILE.toString()%>"/>
              see this action box...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.SOME_FILE_ACTIONS_WERE_SUPPRESSED%>">
          <div>
            <span class="dark"><rn:suppressedActionCount
                actionIterator="${actionIt}"
                includeFileActionsInCount="true" /></span>
            empty file action boxes were suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_EMPTY_FILE.toString()%>"/>
              see these action boxes...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.ONE_CORRECTION_ACTION_WAS_SUPPRESSED%>">
          <div>
            <span class="dark">1</span>
            empty correction action box was suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                value="<%= UserBL.Pref.SUPPRESS_EMPTY_CORRECTION.toString()%>"/>
              see this action box...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED%>">
          <div>
            <span class="dark"><rn:suppressedActionCount
                actionIterator="${actionIt}"
                includeCorrectionActionsInCount="true" /></span>
            empty correction action boxes were suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                value="<%= UserBL.Pref.SUPPRESS_EMPTY_CORRECTION.toString()%>"/>
              see these action boxes...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.ONE_OTHER_ACTION_WAS_SUPPRESSED%>">
          <div>
            <span class="dark">1</span>
            other empty action box was suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_EMPTY_OTHER.toString()%>"/>
              see this action box...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.SOME_OTHER_ACTIONS_WERE_SUPPRESSED%>">
          <div>
            <span class="dark"><rn:suppressedActionCount
                actionIterator="${actionIt}"
                includeOtherActionsInCount="true" /></span>
            other empty action boxes were suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_EMPTY_OTHER.toString()%>"/>
              see these action boxes...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.ONE_SKIPPED_ACTION_WAS_SUPPRESSED%>">
          <div>
            <span class="dark">1</span>
            action that was skipped by a reversion was suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_SKIPPED.toString()%>"/>
              see this action box...
            </rn:link>
          </div>
        </ctl:errorMessage>
        <ctl:errorMessage errorSupplier="${actionIt}" errorFilter="<%=
            SampleActionIterator.SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED%>">
          <div>
            <span class="dark"><rn:suppressedActionCount
                actionIterator="${actionIt}"
                includeSkippedActionsInCount="true" /></span>
            actions that were skipped by a reversion were suppressed.
            <rn:link href="/lab/sample.jsp" style="padding-left: 1em;">
              <ctl:linkParam name="unsetSuppression"
                  value="<%= UserBL.Pref.SUPPRESS_SKIPPED.toString()%>"/>
              see these action boxes...
            </rn:link>
          </div>
        </ctl:errorMessage>
        </td>
      </tr>
    </table>
    <table id="workflowTable">
      <tr>
        <th colspan="5">Standard Workflow:</th>
        <th colspan="3" class="leftPadded">Other Options:</th>
      </tr>
      <tr>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED%>">
            <rn:link href="/lab/collectpreliminarydata.jsp"
                styleClass="menuItem">
              Collect preliminary data
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED%>">
            Collect preliminary data
          </rn:sampleChecker></td>
        <td rowspan="4">
          <ctl:img src="/images/flow-arrow.gif" alt="-->" />
        </td>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.STRUCTURE_REFINED%>">
            <rn:link href="/lab/refinestructure.jsp"
                styleClass="menuItem">
              Refine structure
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.STRUCTURE_REFINED%>">
            Refine structure
          </rn:sampleChecker>
        </td>
        <td rowspan="4">
          <ctl:img src="/images/flow-arrow.gif" alt="-->" />
        </td>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.RELEASED_TO_PUBLIC%>">
            <rn:link href="/lab/releasetopublic.jsp"
                styleClass="menuItem">
              Release to Public
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.RELEASED_TO_PUBLIC%>">
            Release to Public
          </rn:sampleChecker>
        </td>
        <td class="leftPadded">
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>">
            <rn:link href="/lab/modifytext.jsp" styleClass="menuItem">
              Modify text
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS%>">
            Modify text
          </rn:sampleChecker>
        </td>
        <td class="leftPadded">
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.CITATION_ADDED%>">
            <rn:link href="/lab/addcitation.jsp" styleClass="menuItem">
              Add Citation
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.CITATION_ADDED%>">
            Add Citation
          </rn:sampleChecker>
        </td>
      </tr>
      <tr>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.FAILED_TO_COLLECT%>">
            <rn:link href="/lab/failedtocollect.jsp"
                styleClass="menuItem">
              Failed to collect
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.FAILED_TO_COLLECT%>">
            Failed to collect
          </rn:sampleChecker>
        </td>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.DECLARED_INCOMPLETE%>">
            <rn:link href="/lab/declareincomplete.jsp"
                styleClass="menuItem">
              Declare Incomplete
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.DECLARED_INCOMPLETE%>">
            Declare Incomplete
          </rn:sampleChecker>
        </td>
        <td>&nbsp;</td>
        <td class="leftPadded">
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.MODIFIED_LTAS%>">
            <rn:link href="/lab/customfields.jsp" styleClass="menuItem">
              Edit Custom fields
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.MODIFIED_LTAS%>">
            Edit Custom fields
          </rn:sampleChecker>
        </td>
        <td class="leftPadded">
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.RETRACTED%>">
            <rn:link href="/lab/retract.jsp" styleClass="menuItem">
              Retract
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.RETRACTED%>">
            Retract
          </rn:sampleChecker>
        </td>
      </tr>
      <tr>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.DECLARED_NON_SCS%>">
            <rn:link href="/lab/declarenonscs.jsp" styleClass="menuItem">
              Declare non-SCS
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.DECLARED_NON_SCS%>">
            Declare non-SCS
          </rn:sampleChecker>
        </td>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.SUSPENDED%>">
            <rn:link href="/lab/suspend.jsp" styleClass="menuItem">
              Suspend
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.RESUMED%>">
            <rn:link href="/lab/resume.jsp" styleClass="menuItem">
              Resume
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.SUSPENDED%>">
            <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
                SampleWorkflowBL.RESUMED%>">
              Suspend
            </rn:sampleChecker>
          </rn:sampleChecker>
        </td>
        <td>&nbsp;</td>
        <td colspan="2" class="leftPadded">
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.CHANGED_ACL%>">
            <rn:link href="/lab/changeacl.jsp" styleClass="menuItem">
              Grant / Revoke Access
            </rn:link>
          </rn:sampleChecker>
        </td>
      </tr>
      <tr>
        <td>
          <rn:sampleChecker includeIfActionIsValid="<%=
              SampleWorkflowBL.WITHDRAWN_BY_PROVIDER%>">
            <rn:link href="/lab/withdraw.jsp" styleClass="menuItem">
              Withdraw
            </rn:link>
          </rn:sampleChecker>
          <rn:sampleChecker invert="true" includeIfActionIsValid="<%=
              SampleWorkflowBL.WITHDRAWN_BY_PROVIDER%>">
            Withdraw
          </rn:sampleChecker>
        </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>
  </ctl:selfForm>
  </div>
  <ctl:styleBlock>
      div.actionBox100,
      div.actionBox110 { background-color: #68396F; }
      div.actionBox200,
      div.actionBox210 { background-color: #2D532D; }
      div.actionBox300,
      div.actionBox310 { background-color: #794647; }
      div.actionBox400,
      div.actionBox410 { background-color: #787547; }
      div.actionBox500,
      div.actionBox550 { background-color: #666666; }
      div.actionBox600,
      div.actionBox610 { background-color: #107060; }
      div.actionBox800,
      div.actionBox850,
      div.actionBox900,
      div.actionBox905,
      div.actionBox910,
      div.actionBox915,
      div.actionBox1000,
      div.actionBox1100,
      div.actionBox1210,
      div.actionBox1220,
      div.actionBox1300,
      div.actionBox1400 { background-color: #666666; }
      div.actionBox1500,
      div.actionBox1510 { background-color: #84603C; }
      div.actionBox1600,
      div.actionBox1610 { background-color: #666666; }
      div.actionBox1700,
      div.actionBox1710 { background-color: #84603C; }
      div.actionBox1800,
      div.actionBox1810,
      div.actionBox10000,
      div.actionBox10100,
      div.actionBox10200,
      div.actionBox10300,
      div.actionBox10400,
      div.actionBox10500,
      div.actionBox10600,
      div.actionBox10700,
      div.actionBox10800,
      div.actionBox10900,
      div.actionBox100000 { background-color: #666666; }
      form { margin: 0; border: 0; padding: 0; }
      div.pageBody table { width: 100%; }
      td.actionColumn { vertical-align: top; text-align: right; }
      td.actionColumn > div { margin-left: 1em; }
      div.errorBox,
      div.actionBox,
      div.infoBox { padding: 0.25em 0.25em 0.25em 1.25em; margin: 1em 0 1em 0; }
      div.errorBox { background-color: #CC0000; }
      div.infoBox { background-color: #2E385C; }
      div.actionBox { text-align: left; color: #CCCCCC; }
      span.bright,
      div.errorBoxTitle,
      div.infoBoxTitle { font-weight: bold; color: white; }
      div.errorBoxTitle,
      div.actionBoxTitle,
      div.twoLineActionBoxTitle,
      div.infoBoxTitle { font-size: small; margin-bottom: 0.25em;
                         white-space: nowrap; }
      div.actionBoxTitle,
      div.errorBoxTitle,
      div.infoBoxTitle { line-height: 32px; }
      div.twoLineActionBoxTitle { line-height: 16px; }
      div.infoBoxBody,
      div.errorBoxBody,
      div.actionBoxBody { background-color: #F3F3F3; padding: 0.25em;
                          clear: both; }
      div.infoBoxBody th,
      div.infoBoxBody td,
      div.actionBoxBody th,
      div.actionBoxBody td { vertical-align: baseline; }
      span.dark,
      li.currentStatus,
      div.actionBox td,
      div.infoBox td { font-weight: bold;  color: #000050; }
      div.actionBox td,
      div.infoBox td { text-align: left; }
      div.infoBox ul { margin: 0; }
      div.actionBox td.suppressionCount { text-align: center;
          font-weight: normal; }
      div.actionBox td.suppressionCount,
      div.actionBox th,
      div.infoBox th { font-style: narrow; color: #606060; }
      div.actionBox th,
      div.infoBox th { font-weight: normal; text-align: right;
                       white-space: nowrap; }
      table.preferenceTable { color: #505050; background-color: #C8C8C8;
          margin-top: 0; font-size: small; font-style: italic; }
      img.icon { height: 32px; float: left; margin-right: 0.2em; }
      span.light,
      li.otherStatus { font-style: narrow; color: #A0A0A0; }
      span.comments { font-style: normal; color: #DDDDDD; }
      span.fileName { font-weight: bold; color: white; }
      span.skippedByReversionMessage { color: #F0F0F0; font-weight: bold;
          font-style: italic; }
      div.fileDescription { margin-left: 1em; font: italic small Times, serif;
          color: #707070; }
      img.grayArcImage { vertical-align: top; text-align: right; }
      img.grayCurveImage { vertical-align: bottom; }
      span.preferenceItem { white-space: nowrap; padding-right: 1em; }
      table#workflowTable { white-space: nowrap; text-align: center; width: 1px;
          margin: 1em auto 0 auto; border: 2px solid #DAE8F1; }
      #workflowTable th { background: #DAE8F1; text-align: left; }
      th.leftPadded,
      td.leftPadded { padding-left: 1em; }
  </ctl:styleBlock>
</rn:editSamplePage>
