<%--
  - collectpreliminarydata.jsp
  -
  - 17-Jul-2002: jobollin wrote first draft
  - 06-Aug-2002: jobollin fixed bug 252
  - 06-Aug-2002: jobollin fixed bugs 263 and 266
  - 22-Aug-2002: jobollin fixed bugs 262 and 280
  - 29-Aug-2002: leqian added localtracking support
  - 10-Sep-2002: eisiorho added appropriate measurement unit for "scientific
  -              data"
  - 18-Sep-2002: ekoperda fixed bug #462 (incorrect symbol for Angstroms)
  - 14-Nov-2002: adharurk added combo box for Crystallographer name.
  - 05-Feb-2003: dfeng renamed gatherdata.jsp to
  -              collectpreliminarydata.jsp, also made supporting changes
  - 31-Mar-2003: midurbin fixed bug #840
  - 02-Apr-2003: midurbin fixed bug #839
  - 23-Apr-2003: adharurk changed parameters of 
  -              PageHelper.collectPreliminaryData()
  - 05-Jun-2003: adharurk added correctability support
  - 20-Jun-2003: dfeng added Multi-value support for CRYSTALLOGRAPHER_NAME
  - 01-Jul-2003: dfeng added support to detect html tags within forms
  - 29-Jul-2003: dfeng added validation rules for the cell angle fields and
  -              the cell length fields. 
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 08-Dec-2004: midurbin completed cwestnea's rewrite using custom tags
  - 19-Jan-2004: midurbin fixed bug #1503
  - 24-Jan-2005: jobollin added <ctl:selfForm> tags
  - 24-Feb-2005: midurbin fixed bug #1535 in javascript method getElement()
  - 12-Apr-2005: midurbin added sample field for RAW_DATA_URL annotation
  - 13-Jul-2005: midurbin added new required parameters to wapPage
  - 05-Aug-2005: midurbin added SampleFieldUnits tags
  - 12-Aug-2005: midurbin added SampleFieldLabel tags where applicable
  - 21-Feb-2006: jobollin added support for synchronization with CIF
  - 28-Apr-2006: jobollin added tolerant handling of invalid CIFs
  - 09-Jan-2008: ekoperda rearranged tags to fix bug #1857 during CIF sync
  --%>
<%@ page import="org.recipnet.common.controls.HtmlPage" %>
<%@ page import="org.recipnet.common.controls.ErrorChecker" %>
<%@ page import="org.recipnet.site.content.rncontrols.LabField" %>
<%@ page import="org.recipnet.site.content.rncontrols.CifFileContext" %>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ page import="org.recipnet.site.shared.bl.SampleWorkflowBL" %>
<%@ page import="org.recipnet.site.shared.db.SampleDataInfo" %>
<%@ page import="org.recipnet.site.shared.db.SampleInfo" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:wapPage title="Collect Preliminary Data"
    workflowActionCode="<%= SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED %>"
    workflowActionCodeCorrected="<%=
      SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED_CORRECTED %>"
    editSamplePageHref="/lab/sample.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">
  <div class="pageBody">
  <p class="pageInstructions">
    Enter the preliminary crystal data on the form below and click
    the "Save" button to record it.
    <ctl:errorMessage errorSupplier="${htmlPage}" errorFilter="<%=
      HtmlPage.NESTED_TAG_REPORTED_VALIDATION_ERROR %>"><br/>
      <span class="errorMessage"
            style="font-weight: normal; font-style: italic;">
        You must address the flagged validation errors before the data
        will be accepted.
      </span> 
    </ctl:errorMessage>
  </p>
  <ctl:selfForm>
    <rn:filenames>
      <rn:cifChooser id="cifChooser"/>
      <rn:file fileName="${cifChooser.cifName}">
        <rn:cifFile id="cif"/>
      </rn:file>
    </rn:filenames>
    <table class="bodyTable">
      <tr>
        <th class="leadSectionHead" colspan="4">General Information</th>
      </tr>
      <tr>
        <th>Laboratory:</th>
        <td colspan="3">
          <rn:labField fieldCode="<%= LabField.NAME %>" 
              displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th>
          <rn:sampleFieldLabel fieldCode="<%=SampleInfo.LOCAL_LAB_ID%>" />:
        </th>
        <td colspan="3">
          <rn:sampleField fieldCode="<%= SampleInfo.LOCAL_LAB_ID %>" 
              displayAsLabel="true" />
        </td>
      </tr>
      <tr>
        <th class="multiboxLabel">
          <rn:sampleFieldLabel
              fieldCode="<%=SampleTextBL.CRYSTALLOGRAPHER_NAME%>" />:
        </th>
        <td colspan="3">
          <rn:sampleField 
              fieldCode="<%= SampleTextBL.CRYSTALLOGRAPHER_NAME %>">  
            <ctl:extraHtmlAttribute name="tabindex" value="1"/>
          </rn:sampleField>
        </td>
      </tr>
      <rn:cifSampleFilter cif="${cif.cifFile}" fixShelxCifs="true"
          enabled="${loadFromCif.valueAsBoolean
          || loadFromBuggyCif.valueAsBoolean}">
        <tr>
          <th colspan="4" class="sectionHead"
              style="padding-top: 0.0em;">Crystal Data</th>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.A_FIELD%>" />:
          </th>
          <td class="minimumWidth"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.A_FIELD %>"
                id="cellA">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.A_FIELD%>" />
          </td>
          <th class="minimumWidth">
            <rn:sampleFieldLabel
                    fieldCode="<%=SampleDataInfo.ALPHA_FIELD%>" />:
          </th>
          <td> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.ALPHA_FIELD %>" 
                id="cellAlpha">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.ALPHA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.B_FIELD%>" />:
          </th>
          <td class="minimumWidth"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.B_FIELD %>"
                id="cellB">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.B_FIELD%>" />
          </td>
          <th class="minimumWidth">
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.BETA_FIELD%>" />:
          </th>
          <td> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.BETA_FIELD %>" 
                id="cellBeta">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.BETA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.C_FIELD%>" />:
          </th>
          <td class="minimumWidth"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.C_FIELD %>"
                id="cellC">
              <ctl:extraHtmlAttribute name="onChange" value="setVals()" />
              <ctl:extraHtmlAttribute name="tabindex" value="2"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.C_FIELD%>" />
          </td>
          <th class="minimumWidth">
            <rn:sampleFieldLabel
                    fieldCode="<%=SampleDataInfo.GAMMA_FIELD%>" />:
          </th>
          <td> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.GAMMA_FIELD %>" 
                id="cellGamma">
              <ctl:extraHtmlAttribute name="tabindex" value="3"/>
            </rn:sampleField><rn:sampleFieldUnits
                fieldCode="<%=SampleDataInfo.GAMMA_FIELD%>" />
          </td>
        </tr>
        <tr>
          <th>
            <rn:sampleFieldLabel fieldCode="<%=SampleDataInfo.T_FIELD%>" />:
          </th>
          <td class="minimumWidth"> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.T_FIELD %>">
              <ctl:extraHtmlAttribute name="tabindex" value="4"/>
            </rn:sampleField><rn:sampleFieldUnits
                    fieldCode="<%=SampleDataInfo.T_FIELD%>" />
          </td>
          <th class="minimumWidth">
            <rn:sampleFieldLabel
                    fieldCode="<%=SampleDataInfo.COLOR_FIELD%>" />:
          </th>
          <td> 
            <rn:sampleField fieldCode="<%= SampleDataInfo.COLOR_FIELD %>">
              <ctl:extraHtmlAttribute name="tabindex" value="4"/>
            </rn:sampleField>
          </td>
        </tr>
        <tr>
          <td colspan="4" class="actionButton">
            <ctl:errorChecker errorSupplier="${cif}" invert="true">
              <ctl:button id="loadFromCif" suppressInsteadOfSkip="true"
                label="Load crystal data from ${cifChooser.cifName}">
                <ctl:extraHtmlAttribute name="tabindex" value="5"/>
              </ctl:button>
            </ctl:errorChecker>
            <ctl:errorChecker errorSupplier="${cif}"
                errorFilter="<%=CifFileContext.CIF_HAS_ERRORS %>">
              <ctl:button id="loadFromBuggyCif" suppressInsteadOfSkip="true"
                label="Load crystal data from ${cifChooser.cifName}">
                <ctl:extraHtmlAttribute name="tabindex" value="5"/>
              </ctl:button><br/>
              <span class="errorNotice">Warning: CIF '${cifChooser.cifName}'
                  contains errors</span>
            </ctl:errorChecker>
            <ctl:errorChecker errorSupplier="${cif}">
              <ctl:errorChecker errorSupplier="${cif}" invert="true"
                  errorFilter="<%=CifFileContext.CIF_HAS_ERRORS %>">
                <ctl:button editable="false"
                    label="Load crystal data from CIF"/><br/>
              </ctl:errorChecker>
              <ctl:errorChecker errorSupplier="${cif}"
                  errorFilter="<%=CifFileContext.UNPARSEABLE_CIF %>">
                <span class="errorNotice">Warning: CIF '${cifChooser.cifName}'
                    could not be parsed</span>
              </ctl:errorChecker>
              <ctl:errorChecker errorSupplier="${cif}"
                  errorFilter="<%=CifFileContext.EMPTY_CIF %>">
                <span class="errorNotice">Warning: CIF '${cifChooser.cifName}'
                    contains no data blocks</span>
              </ctl:errorChecker>
            </ctl:errorChecker>
          </td>
        </tr>
      </rn:cifSampleFilter>
      <tr><th class="sectionHead" colspan="4">Additional Information</th></tr>
      <tr>
        <th>
          <rn:sampleFieldLabel
                  fieldCode="<%=SampleTextBL.RAW_DATA_URL%>" />:
        </th>
        <td colspan="3"> 
          <rn:sampleField fieldCode="<%= SampleTextBL.RAW_DATA_URL %>">
            <ctl:extraHtmlAttribute name="tabindex" value="50"/>
          </rn:sampleField>
        </td>
      </tr>
      <jsp:useBean id="ltaClass"
          class="org.recipnet.site.wrapper.StringBean" scope="page"/>
      <jsp:setProperty name="ltaClass" property="string"
          value="class='subsectionHead'"/>
      <rn:ltaIterator>
        <tr>
          <th ${ltaClass.string}>
            <rn:sampleFieldLabel />:
          </th>
          <td colspan="3" ${ltaClass.string}>
            <rn:sampleField>
              <ctl:extraHtmlAttribute name="tabindex" value="100"/>
            </rn:sampleField><rn:sampleFieldUnits />
          </td>
        </tr> 
        <jsp:setProperty name="ltaClass" property="string" value=""/>
      </rn:ltaIterator>
      <tr>
        <th class="sectionHead" colspan="4">Data Collection Comments</th>
      </tr>
      <tr>
        <td colspan="4" style="text-align: center;">
          <rn:wapComments><ctl:extraHtmlAttribute name="tabindex" value="200"
            /></rn:wapComments>
        </td>
      </tr>
      <tr>
        <td colspan="4" class="formButtons">
          <rn:wapSaveButton suppressInsteadOfSkip="true">
            <ctl:extraHtmlAttribute name="tabindex" value="32767"/>
          </rn:wapSaveButton>
          <rn:wapCancelButton suppressInsteadOfSkip="true">
            <ctl:extraHtmlAttribute name="tabindex" value="32767"/>
          </rn:wapCancelButton>
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  <script type="text/javascript">
    <!-- // hide from ancient browsers
    
    // A function that sets the values of cellAlpha, cellBeta and cellGamma to
    // 90 if none of them were previously set and at least one of cellA, cellB
    // and cellC has been set.
    function setVals() {
        if (!(getElement("cellA").value == ""
                && getElement("cellB").value == ""
                && getElement("cellC").value == "")) {
            var cellAlpha = getElement("cellAlpha");
            var cellBeta = getElement("cellBeta");
            var cellGamma = getElement("cellGamma");
            if (cellAlpha.value == "" && cellBeta.value == ""
                    && cellGamma.value == "") {
                cellAlpha.value = "90.00";
                cellBeta.value = "90.00";
                cellGamma.value = "90.00";
            }
        }
    }

    // A function that finds the first element that starts with the provided
    // name.  This is useful because the HTML form element for an
    // HtmlPageElement is guaranteed to have a name that starts with the id
    // value assigned to that element.
    function getElement(name) {
        var index = 0;
        while (index < document.forms[1].elements.length) {
            if (document.forms[1].elements[index].name.indexOf(name) == 0) {
                return document.forms[1].elements[index];
            } else {
                index ++;
            }
        }
    } 

    // stop hiding -->
  </script>
  </div>
</rn:wapPage>
