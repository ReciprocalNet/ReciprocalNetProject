<%--
  - Reciprocal Net project
  - account.jsp
  -
  - 18-May-2005: midurbin wrote first draft
  - 13-Jul-2005: midurbin added new required parameters to UserPage
  - 19-Aug-2005: midurbin added VALIDATE_SPACE_GROUP preference
  - 28-Oct-2005: midurbin added various new preference options
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.PreferenceField" %>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserContext" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UserPage" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:userPage title="Manage account" completionUrlParamName="origPage"
    cancellationUrlParamName="origPage" loginPageUrl="/login.jsp"
        currentPageReinvocationUrlParamName="origUrl"
        authorizationFailedReasonParamName="authorizationFailedReason">
  <ctl:errorMessage
          errorFilter="<%=HtmlPage.NESTED_TAG_REPORTED_VALIDATION_ERROR%>">
    <p class="errorMessage">
      Validation Error -- please check your entries and resubmit
    </p>
  </ctl:errorMessage>
  <br />
  <ctl:selfForm>
    <table cellspacing="0">
      <tr class="userInfoHeader">
        <td colspan="2" class="threeColumnRight">
          <strong>User Information:</strong>
        </td>
      </tr>
      <tr class="userInfoRow">
        <td class="threeColumnLeft">name:</td>
        <td class="threeColumnRight"><rn:userField /></td>
      </tr>
      <tr class="userInfoRow">
        <td class="threeColumnLeft">username:</td>
        <td class="threeColumnRight">
          <rn:userField fieldCode="<%=UserField.USER_NAME%>" />
        </td>
      </tr>
      <tr class="userInfoRow">
        <td class="threeColumnCenter" colspan="2">
          <ctl:a href="/password.jsp">
            <ctl:currentPageLinkParam name="editUserPage" />
            change your password...
          </ctl:a>
        </td>
      </tr>
    </table>

    <br />
    <table cellspacing="0" width="75%">
      <tr class="headerRow">
        <td colspan="3" class="threeColumnRight">
          <strong>Sample Viewing Preferences:</strong>
        </tr>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">applet:</td>
        <td class="threeColumnCenter">
          <rn:preferenceField
              preferenceType="<%=PreferenceField.PrefType.APPLET%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            The preferred applet will be displayed for samples with CRT files.
            &nbsp;&nbsp;This setting may also be changed on any page where JaMM
            is visible by selecting a different applet..
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft"><nobr>sample view:</nobr></td>
        <td class="threeColumnCenter">
          <rn:preferenceField
              preferenceType="<%=PreferenceField.PrefType.SAMPLE_VIEW%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            This is the page that is used to display a sample that is the
            single result of a search or quick search.&nbsp;&nbsp; This option
            has no effect when you are not authorized to edit a sample, in
            which case the "Sample details" page will be used.
          </span>
        </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft"><nobr>sample details page view:</nobr></td>
        <td class="threeColumnCenter">
          <rn:preferenceField
              preferenceType="<%=PreferenceField.PrefType.SHOWSAMPLE_VIEW%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            The sample details page has multiple view modes.&nbsp;&nbsp; Basic
            mode represents a simplified view of a sample ideal for those
            with little or no chemistry background, while detailed mode
            contains lots of specific crystallographic data.
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>suppress file descriptions:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_FILE_DESCRIPTIONS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, file descriptions will be suppressed on the
            show sample page as well as the edit sample page.
          </span>
        </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>implicitly set preferences:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.ALLOW_IMPLICIT_PREF_CHANGES%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, the sample details page mode preference as well as
            the preferred visualization applet will be updated by switching
            between page modes or applet types.  In other words, the last
            JaMM applet viewed or sample details page mode will be considered
            to be the preferred one.
          </span>
        </td>
      </tr>
    </table>

    <br />
    <table cellspacing="0" width="75%">
      <tr class="headerRow">
        <td colspan="3" class="threeColumnRight">
          <strong>Search Parameter Preferences:</strong>
        </tr>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>restrict searches to non-retracted samples:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SEARCH_NON_RETRACTED%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When this box is checked the search page option to restrict
            searches to those samples which have not be retracted previously
            will be selected by default.
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>restrict searches to finished samples:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField
              preferenceType="<%=PreferenceField.PrefType.SEARCH_FINISHED%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When this box is checked, the search page option to restrict
            searches to those samples for which processing has finished will be
            selected by default.
          </span>
        </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>restrict searches to local samples:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField
              preferenceType="<%=PreferenceField.PrefType.SEARCH_LOCAL%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When this box is checked, the search page option to restrict
            searches to samples with data at the local site will be selected by
            default.
          </span>
        </td>
      </tr>
      <rn:userChecker includeIfProviderUser="true">
        <tr class="evenRow">
          <td class="threeColumnLeft">
            <nobr>restrict searches to samples from my provider:</nobr>
            <br />
            (<rn:providerField />)
          </td>
          <td class="threeColumnCenter">
            <rn:preferenceField preferenceType="<%=
                PreferenceField.PrefType.SEARCH_MY_PROVIDER%>" />
          </td>
          <td class="threeColumnRight">
            <span class="description">
              When this box is checked, the search page option to restrict
              searches to samples originating from my provider will be selected
              by default.
            </span>
          </td>
        </tr>
      </rn:userChecker>
    </table>

    <rn:authorizationChecker canSeeLabSummary="true">
      <br />
      <table cellspacing="0" width="75%">
        <tr class="headerRow">
          <td colspan="3" class="threeColumnRight">
            <strong>Lab Summary Preferences:</strong>
          </tr>
        </tr>
        <tr class="oddRow">
          <td class="threeColumnLeft">Summary Days:</td>
          <td class="threeColumnCenter">
            <rn:preferenceField id="summaryDays"
                preferenceType="<%=PreferenceField.PrefType.SUMMARY_DAYS%>" />
            <ctl:errorMessage errorSupplier="<%=summaryDays%>"
                errorFilter="<%=PreferenceField.VALUE_IS_NOT_A_NUMBER%>">
              <br>
              <span class="error">
                The value must be a number!
              </span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=summaryDays%>"
                errorFilter="<%=PreferenceField.VALUE_IS_TOO_HIGH%>">
              <br />
              <span class="error">
                The value is too high!<br /> (value must be between 0 and 511)
              </span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=summaryDays%>"
                errorFilter="<%=PreferenceField.VALUE_IS_TOO_LOW%>">
              <br />
              <span class="error">
                The value is too low!<br /> (value must be between 0 and 511)
              </span>
            </ctl:errorMessage>
          </td>
          <td class="threeColumnRight">
            <span class="description">
              The Lab Summary page will display samples that have last been
              modified no more than this number of days ago.
            </span>
          </td>
        </tr>
        <tr class="evenRow">
          <td class="threeColumnLeft">Summary Samples:</td>
          <td class="threeColumnRight">
            <rn:preferenceField id="summarySamples" preferenceType="<%=
                PreferenceField.PrefType.SUMMARY_SAMPLES%>" />
            <ctl:errorMessage errorSupplier="<%=summarySamples%>"
                errorFilter="<%=PreferenceField.VALUE_IS_NOT_A_NUMBER%>">
              <br>
              <span class="error">
                The value must be a number!
              </span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=summarySamples%>"
                errorFilter="<%=PreferenceField.VALUE_IS_TOO_HIGH%>">
              <br />
              <span class="error">
                The value is too high!<br /> (value must be between 1 and 255)
              </span>
            </ctl:errorMessage>
            <ctl:errorMessage errorSupplier="<%=summarySamples%>"
                errorFilter="<%=PreferenceField.VALUE_IS_TOO_LOW%>">
              <br />
              <span class="error">
                The value is too low!<br /> (value must be between 1 and 255)
              </span>
            </ctl:errorMessage>
          </td>
          <td class="threeColumnRight">
            <span class="description">
              The Lab Summary page will display no more than this number of
              samples for each finished sample status.
            </span>
          </td>
        </tr>
      </table>
    </rn:authorizationChecker>

    <br />
    <table cellspacing="0" width="75%">
      <tr class="headerRow">
        <td colspan="3" class="threeColumnRight">
          <strong>Action Boxes on the Edit Sample page:</strong>
          <br />
          <i>(only applicable when you are authorized to edit a sample)</i>
        </tr>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft"><nobr>suppress blank fields:</nobr></td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_BLANK_FIELD%>" />
        </td>
          <td class="threeColumnRight">
            <span class="description">
              When checked, data fields for which no data has been entered will
              not be visible.
            </span>
          </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft"><nobr>suppress comments:</nobr></td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_COMMENTS%>" />
        </td>
          <td class="threeColumnRight">
            <span class="description">
              When checked, comments will not be included at the bottom of each
              action box.
            </span>
          </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>suppress empty file boxes:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_EMPTY_FILE_ACTIONS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, empty file action boxes will not be displayed.
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>suppress empty correction boxes:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_EMPTY_CORRECTION_ACTIONS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, boxes for correction actions where no new data was
            added will not be displayed.
          </span>
        </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>suppress other empty boxes:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_EMPTY_OTHER_ACTIONS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, boxes for actions where no new data was added will
            not be displayed (excludes file actions and correction actions).
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>suppress skipped action boxes:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_SKIPPED_ACTIONS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, boxes for actions that were undone by a reversion
            will not be displayed.
          </span>
        </td>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>suppress blank file lists:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_BLANK_FILE_LISTINGS%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, the lists of files added, removed or modified for
            each action box will be suppressed when they contains no files.
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>suppress suppression options:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
              PreferenceField.PrefType.SUPPRESS_SUPPRESSION%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When checked, the edit sample page will not expose these settings
            for modification.  &nbsp;&nbsp;Instead, you will have to return to
            this page to change the settings.
          </span>
        </td>
      </tr>
    </table>

    <br />
    <table cellspacing="0" width="75%">
      <tr class="headerRow">
        <td colspan="3" class="threeColumnRight">
          <strong>File Upload Preferences:</strong>
          <br />
          <i>
            (Only applicable when you are authorized to upload files for a
            sample)
          </i>
        </tr>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>default file overwrite option:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
                  PreferenceField.PrefType.DEFAULT_FILE_OVERWRITE%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When this box is checked, the file upload page will default to
            allowing existing files in the repository to be overwritten. 
            &nbsp;&nbsp;Note: sample data files are versioned, so such
            overwriting can be undone.
          </span>
        </td>
      </tr>
      <tr class="evenRow">
        <td class="threeColumnLeft">
          <nobr>default file upload mechanism:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
                  PreferenceField.PrefType.FILE_UPLOAD_MECHANISM%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            Indicates the preferred mechanism for uploading files.&nbsp;&nbsp;
            "Form-based" indicates an HTML form that allows the user to select
            files to be uploaded and optionally include comments and file
            descriptions.&nbsp;&nbsp; "Drag-and-drop" indicates a java applet
            onto which multiple files may be be dragged and uploaded.
          </span>
        </td>
      </tr>
    </table>

    <br />
    <table cellspacing="0" width="75%">
      <tr class="headerRow">
        <td colspan="3" class="threeColumnRight">
          <strong>Display warnings about:</strong>
        </tr>
      </tr>
      <tr class="oddRow">
        <td class="threeColumnLeft">
          <nobr>non-standard space group symbols:</nobr>
        </td>
        <td class="threeColumnCenter">
          <rn:preferenceField preferenceType="<%=
                  PreferenceField.PrefType.VALIDATE_SPACE_GROUP%>" />
        </td>
        <td class="threeColumnRight">
          <span class="description">
            When this box is checked, space group symbols will be parsed and
            validated when entered.  Unrecognized space group symbols will
            trigger a warning message and must be resolved.  This option is
            recommended because only standard, parsible space group symbols
            will be considered in searches.
          </span>
        </td>
      </tr>
    </table>

    <br />
    <table width="75%">
      <rn>
        <td align="center">
          <rn:userActionButton label="Save"
            userFunction="<%=UserPage.UserFunction.CHANGE_PREFERENCES%>" />
          <rn:userActionButton label="Cancel"
            userFunction="<%=UserPage.UserFunction.CANCEL%>" />
        </td>
      </rn>
    </table>
  </ctl:selfForm>
  <ctl:styleBlock>
    .description { font-size: x-small; color: #505060; }
    .error { font-size: x-small; color: #FF0000; }
    .errorMessage  { color: red; font-weight: bold;}
    .headerRow {background-color: #909090; color: #FFFFFF;
            text-align: left; } 
    .oddRow { background-color: #D6D6D6; }
    .evenRow { background-color: #F0F0F0; }
    .threeColumnLeft { text-align: right; vertical-align: top; padding: 4px; }
    .threeColumnCenter { text-align: left; vertical-align: top; padding: 4px; }
    .threeColumnRight { text-align: left; vertical-align: top; padding: 4px; }
    .userInfoHeader { background-color: #8888A0; color: #FFFFFF;
            text-align: left; }
    .userInfoRow { background-color: #DCDCF6; }
  </ctl:styleBlock>
</rn:userPage>
