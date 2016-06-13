<%--
 - Reciprocal Net project
 - @(#)lab/confirmrevert.jsp
 -
 - 05-Jul-2005: midurbin wrote the first draft
 - 13-Jul-2005: midurbin added new required parameters to revertWapPage
 - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
 - 23-Sep-2005: midurbin added ShowsampleLink tags where applicable
--%>
<%@page import="org.recipnet.site.content.rncontrols.SampleHistoryField"%>
<%@page import="org.recipnet.site.shared.db.SampleInfo"%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:revertWapPage title="Revert sample"
    includeDataFilesParamName="includeFiles"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <p class="pageInstructions">
    Please enter any comments and press 'Confirm' to revert to an
    older version, or 'Cancel' to return to view the Sample History.
  </p>
  <table align="center" width="80%" class="searchTable">
    <tr>
      <td align="center">
        <strong>Current sample version:</strong>
        <br />
        <table class="searchTable">
          <tr>
            <td align="right">
              <strong>
                <rn:sampleFieldLabel
                        fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
              </strong>
            </td>
            <td align="left">
              <rn:sampleField displayValueOnly="true"
                  fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
            </td>
          </tr>
          <tr>
            <td align="right"><strong>Lab:</strong></td>
            <td align="left">
              <rn:labField />
            </td>
          </tr>
          <tr>
            <td align="right"><strong>Provider:</strong></td>
            <td align="left">
              <rn:providerField />
            </td>
          </tr>
          <tr>
            <td align="right">
              <strong>
                <rn:sampleFieldLabel fieldCode="<%=SampleInfo.STATUS%>" />:
              </strong>
            </td>
            <td align="left">
              <rn:sampleField displayValueOnly="true"
                  fieldCode="<%=SampleInfo.STATUS%>" />
            </td>
          </tr>
          <tr>
            <td align="right"><strong>Last action date:</strong></td>
            <td align="left">
              <rn:sampleHistoryTranslator translateFromSampleContext="true">
                <rn:sampleHistoryField fieldCode="<%=
                        SampleHistoryField.FieldCode.ACTION_DATE%>" />
              </rn:sampleHistoryTranslator>
            </td>
          </tr>
          <tr>
            <td align="center" colspan="2">
              <rn:showsampleLink openInWindow="old"
                      basicPageUrl="/showsamplebasic.jsp"
                      detailedPageUrl="/showsampledetailed.jsp"
                      sampleIsKnownToBeLocal="true">
                <rn:sampleParam name="sampleId" />
                <rn:sampleHistoryParam name="sampleHistoryId" />
                View this version...
              </rn:showsampleLink>
            </td>
          </tr>
        </table>
      </td>
      <td align="center">
        <i>reverting to</i> <br />
        <img src="../images/revert-arrow.gif">
      </td>
      <td align="center">
        <strong>Previous sample version:</strong>
        <br />
        <rn:specificSampleVersion sampleIdParamName="sampleId"
            sampleHistoryIdParamName="targetSampleHistoryId">
          <table class="searchTable">
            <tr>
              <td align="right">
                <strong>
                  <rn:sampleFieldLabel
                          fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
                </strong>
              </td>
              <td align="left">
                <rn:sampleField displayValueOnly="true"
                    fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />
              </td>
            </tr>
            <tr>
              <td align="right"><strong>Lab:</strong></td>
              <td align="left">
                <rn:labField />
              </td>
            </tr>
            <tr>
              <td align="right"><strong>Provider:</strong></td>
              <td align="left">
                <rn:providerField />
              </td>
            </tr>
            <tr>
              <td align="right">
                <strong>
                  <rn:sampleFieldLabel fieldCode="<%=SampleInfo.STATUS%>" />:
                </strong>
              </td>
              <td align="left">
                <rn:sampleField displayValueOnly="true"
                    fieldCode="<%=SampleInfo.STATUS%>" />
              </td>
            </tr>
            <tr>
              <td align="right"><strong>Last action date:</strong></td>
              <td align="left">
                <rn:sampleHistoryTranslator translateFromSampleContext="true">
                  <rn:sampleHistoryField fieldCode="<%=
                          SampleHistoryField.FieldCode.ACTION_DATE%>" />
                </rn:sampleHistoryTranslator>
              </td>
            </tr>
            <tr>
              <td align="center" colspan="2">
              <rn:showsampleLink openInWindow="new"
                      basicPageUrl="/showsamplebasic.jsp"
                      detailedPageUrl="/showsampledetailed.jsp"
                      sampleIsKnownToBeLocal="true">
                <rn:sampleParam name="sampleId" />
                <rn:sampleHistoryParam name="sampleHistoryId" />
                View this version...
              </rn:showsampleLink>
              </td>
            </tr>
          </table>
        </rn:specificSampleVersion>
      </td>
    </tr>
    <tr>
      <td align="center" colspan="3">
        <ctl:selfForm>
          <br />
          <p>
            <table>
              <tr>
                <td align="right" valign="center">
                  <ctl:checkbox id="includeFiles"
                      initialValue="<%=Boolean.TRUE%>" />
                </td>
                <td align="left" valign="center">
                  - Revert data files&nbsp;
                </td>
              </tr>
            </table>
            <table width="50%">
              <tr>
                <td>
                  <font class="light">
                    If you choose to revert data files as well as sample
                    data, the repository files will be replaced by those
                    from the version to which you are reverting.  If you
                    choose not to include data files in this reversion,
                    the files will remain unchanged.
                  </font>
                </td>
              </tr>
            </table>
          </p>
          <br />
          Comments: (optional)
          <br />
          <rn:wapComments />
          <table>
            <tr>
              <td>
                <rn:wapSaveButton />
              </td>
              <td>
                <rn:buttonLink target="/lab/samplehistory.jsp"
                    label="Cancel"/>
              </td>
            </tr>
          </table>
        </ctl:selfForm>
      </td>
    </tr>
  </table>
  <ctl:styleBlock>
    .navLinksRight { text-align: right; }
    .pageInstructions { font-weight: bold; font-style: normal;
        font-size: medium; text-decoration: none; }
    .searchTable  { border-width: thin; border-style: solid;
        border-color: #CCCCCC; }
    .searchTableHeader { text-align: left; background-color: #CCCCCC; }
    .searchTableSubHeader { background-color: #EBEBEB; }
    .searchTableColumnWidth { width: 25%; }
    font.light { font-family: Arial, Helvetica, Verdana; font-style: narrow; 
        color: #A0A0A0; }
  </ctl:styleBlock>
</rn:revertWapPage>
