<%--
   - Reciprocal Net project
   - search.jsp
   -
   - 30-Jul-2002: rlauer wrote first draft
   - 01-Aug-2002: hclin modified first draft
   - 06-Aug-2002: ekoperda added "browse mode" feature
   - 06-Aug-2002: hclin fixed bug #268
   - 07-Aug-2002: jobollin multiple changes:
   -              modifiPageHelper.storeSearchParamsed internals to avoid
   -              multiple invocations of request.getParameter() and
   -              concomittant revalidation; corrected pervasive problems with
   -              comparing double values to NaN via == and/or != ; made
   -              selected lab and limit to provider options "sticky" across
   -              validation errors; reformatted the search form; corrected
   -              problems that led to invalid range values being stored in the
   -              SearchParams container
   - 12-Aug-2002: leqian fixed bug #337
   - 22-Aug-2002: leqian added range check for minZ and maxZ, task #338
   - 26-Aug-2002: leqian removed authController.setIdValue call, task #370
   - 04-Sep-2002: jobollin removed extraneous carriage return characters from
   -              the source
   - 06-Sep-2002: jobollin implemented a constraint by sample provider
   -              option for laboratory staff users
   - 24-Sep-2002: eisiorho added better pagination, task #444
   - 15-Oct-2002: ekoperda added support for the requireLocalHolding field on
   -              SearchParams
   - 17-Oct-2002: eisiorho added ordering of search results
   - 31-Oct-2002: eisiorho changed option menu, from 'All' to 500 samples
   -              maximum shown per page
   - 13-Dec-2002: adharurk added ordering of search results for browse mode
   - 19-Feb-2003: adharurk added support for "quickSearchByLab' as a query line
   -              parameter
   - 27-Feb-2003: nsanghvi added support for "repeatSearchId" as a query line 
   -              parameter
   - 06-Mar-2003: adharurk added fields 'requireTerminal' and 
   -              'requireNonRetracted' to the search constraints
   - 06-Mar-2003: yli added support for the sampleProviderName field
   - 02-Apr-2003: jrhanna fixed bug #848
   - 08-May-2003: midurbin added support for 'quickSummarySearchByLab',
   -              'restrictToStatus' and 'daysToInclude' as query line 
   -              parameters
   - 27-Jun-2003: midurbin added search by keyword support.
   - 27-Jun-2003: ajooloor altered quick searches to be local
   - 07-Jan-2004: ekoperda changed package references due to source tree
   -              reorganization
   - 19-Aug-2004: cwestnea rewrote using tags
   - 24-Aug-2004: midurbin specified a searchResultsPage for the SearchPage
   - 14-Jan-2005: jobollin inserted <ctl:selfForm> elements
   - 25-Feb-2005: midurbin updated page to reflect new SearchParmFields specs
   - 14-Mar-2005: midurbin added search-by-atom support
   - 11-May-2005: ekoperda added a 'Unit Cell' section to the UI
   - 12-Jul-2005: ekoperda exposed enhanced space group symbol searching to UI
   - 18-Aug-2005: midurbin fixed formatting bug #1552
  --%>
<%@ page import="org.recipnet.site.content.rncontrols.ProviderField" %>
<%@ page import="org.recipnet.site.content.rncontrols.SearchPage" %>
<%@ page import="org.recipnet.site.content.rncontrols.SearchParamsField" %>
<%@ page import="org.recipnet.site.content.rncontrols.UnitCellSearchField" %>
<%@ page import=
    "org.recipnet.site.content.rncontrols.UnitCellSearchFieldGroup"%>
<%@ page import="org.recipnet.site.shared.bl.SampleTextBL" %>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:searchPage title="Site Search" searchResultsPage="/searchresults.jsp">
  <div style="margin-top: 1em;">
  <ctl:errorMessage>
    <span class="errorMessage">
      <center>
        Validation error: please check your input and try again.
      </center>
    </span>
  </ctl:errorMessage>
  <ctl:selfForm>
    <table class="searchTable" align="center">
      <tr>
        <th colspan="4" class="sectionHeader">Sample Identification</th>
      </tr>
      <tr>
        <th>
          Sample number:
        </th>
        <td>
          <rn:searchParamsField tabIndex="1" id="localSampleId"
              fieldCode="<%= SearchParamsField.LOCAL_SAMPLE_ID %>" />
        </td>
        <th>
          Laboratory:
        </th>
        <td>
          <rn:searchParamsField tabIndex="2" id="lab"
              fieldCode="<%= SearchParamsField.LAB_SEARCH %>" />
        </td>
      </tr> 
      <rn:authorizationChecker canSeeProviderListForLab="true">
        <tr>
          <th>Submitted by group:</th>
          <td>
            <rn:searchParamsField tabIndex="3" id="provider"
                fieldCode="<%= SearchParamsField.PROVIDER %>"/>
          </td>
          <th>
            Submitted by individual:
          </th>
          <td>
            <rn:searchParamsField tabIndex="3" id="sampleProviderName"
                fieldCode="<%= SearchParamsField.SAMPLE_PROVIDER_NAME %>" />
          </td>
        </tr> 
      </rn:authorizationChecker>
      <rn:authorizationChecker canFilterResultsByOwnProvider="true">
        <tr>
          <th>
            Submitted by group:
          </th>
          <td>
            <rn:searchParamsField tabIndex="4" id="useUserProvider"
                fieldCode="<%= SearchParamsField.USE_USER_PROVIDER %>" />
            my group (<rn:providerField fieldCode="<%=ProviderField.NAME%>" />)
          </td>
          <th>
            Submitted by individual:
          </th>
          <td>
            <rn:searchParamsField tabIndex="4" id="sampleProviderName"
                fieldCode="<%= SearchParamsField.SAMPLE_PROVIDER_NAME %>" />
          </td>
        </tr>
      </rn:authorizationChecker>
      <tr>
        <th>Crystallographer:</th>
        <td colspan="3">
          <rn:searchParamsField tabIndex="5" id="crystallographer"
              fieldCode="<%= SearchParamsField.CRYSTALLOGRAPHER %>" />
        </td>
      </tr>
      <tr>
        <th valign="top">
          Constrain search to:
        </th>
        <td colspan="3">
          <rn:searchParamsField tabIndex="6" id="requireLocalHolding"
              fieldCode="<%= SearchParamsField.REQUIRE_LOCAL_HOLDING %>" />
          samples with data at the local site
          <br />
          <rn:searchParamsField tabIndex="6" id="requireTerminal"
              fieldCode="<%= SearchParamsField.REQUIRE_TERMINAL_STATUS %>" />
          samples for which processing has finished
          <br />
          <rn:searchParamsField tabIndex="6" id="requireNonRetracted"
              fieldCode="<%= SearchParamsField.REQUIRE_NON_RETRACTED %>" />
          samples which have not been retracted previously
        </td>
      </tr>  
      <tr>
        <th colspan="4" class="sectionHeader">Names and Formulae</th>
      </tr>
      <tr>
        <th>Compound Name: </th>
        <td>
          <rn:searchParamsField tabIndex="9" id="chemName"
              fieldCode="<%= SearchParamsField.CHEM_NAME %>" />
  
        </td>
        <th>Empirical Formula: </th>
        <td>
          <rn:searchParamsField tabIndex="10" id="empirFormula"
              fieldCode="<%= SearchParamsField.EMPIR_FORMULA %>" />
        </td>
      </tr>
      <tr>
        <th>Structural Formula: </th>
        <td>
          <rn:searchParamsField tabIndex="11" id="structFormula"
              fieldCode="<%= SearchParamsField.STRUCT_FORMULA %>" />
        </td>
        <th>Moiety Formula: </th>
        <td>
          <rn:searchParamsField tabIndex="12" id="moietyFormula"
              fieldCode="<%= SearchParamsField.MOIETY_FORMULA %>" />
        </td>
      </tr>
      <tr>
        <th colspan="4" class="sectionHeader">
          Unit Cell
        </th>
      </tr>
      <rn:unitCellSearchFieldGroup id="ucSearch">
        <tr>
          <th>a:</th>
          <td>
            <rn:unitCellSearchField tabIndex="14"
                fieldCode="<%= UnitCellSearchField.FieldCode.A %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.A_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
          <td colspan="2" rowspan="7" valign="top">
            <ctl:radioButtonGroup initialValue="<%=
                UnitCellSearchField.FieldCode.MATCH_UNIT_CELLS_AS_ENTERED.toString()
                %>">
            <div style="margin-bottom: 0.5em;">
            <rn:unitCellSearchField tabIndex="21" id="enteredCell"
               fieldCode="<%= 
               UnitCellSearchField.FieldCode.MATCH_UNIT_CELLS_AS_ENTERED %>" />
              match samples' unit cell parameters as they were entered
            </div>
            <div style="margin-top: 0.5em;">
              <rn:unitCellSearchField tabIndex="21" id="reducedCell"
                  fieldCode="<%= 
                  UnitCellSearchField.FieldCode.MATCH_REDUCED_CELLS %>" />
              match samples' reduced cells; the specified cell (left) is
              </ctl:radioButtonGroup>
              <div style="margin-top: 0.5em; margin-left: 3em;">
                <ctl:radioButtonGroup initialValue="<%=
                    UnitCellSearchField.FieldCode.P_CENTERING.toString() %>">
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.P_CENTERING %>" />
                primitive<br />
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.A_CENTERING %>" />
                A centered<br/>
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.B_CENTERING %>" />
                B centered<br/>
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.C_CENTERING %>" />
                C centered<br/>
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.F_CENTERING %>" />
                F centered<br/>
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.I_CENTERING %>" />
                I centered<br/>
	        <rn:unitCellSearchField tabIndex="22" fieldCode="<%=
                    UnitCellSearchField.FieldCode.R_CENTERING %>" />
                R centered (obverse or reverse)
                </ctl:radioButtonGroup>
              </div>
            </div>
          </td>
        </tr>
        <tr>
          <th>b:</th>
          <td>
            <rn:unitCellSearchField tabIndex="15"
                fieldCode="<%= UnitCellSearchField.FieldCode.B %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.B_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>c:</th>
          <td>
            <rn:unitCellSearchField tabIndex="16"
                fieldCode="<%= UnitCellSearchField.FieldCode.C %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.C_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>&alpha; (alpha):</th>
          <td>
            <rn:unitCellSearchField tabIndex="17"
                fieldCode="<%= UnitCellSearchField.FieldCode.ALPHA %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.ALPHA_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>&beta; (beta):</th>
          <td>
            <rn:unitCellSearchField tabIndex="18"
                fieldCode="<%= UnitCellSearchField.FieldCode.BETA %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.BETA_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>&gamma; (gamma):</th>
          <td>
            <rn:unitCellSearchField tabIndex="19"
                fieldCode="<%= UnitCellSearchField.FieldCode.GAMMA %>" />
	    <ctl:errorMessage
                errorFilter="<%=UnitCellSearchFieldGroup.GAMMA_MISSING%>">
              <span class="errorMessageSmall">
                * required for reduced cell search
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
        <tr>
          <th>error tolerance:</th>
          <td>
	    &nbsp;&#177;&nbsp;
            <rn:unitCellSearchField tabIndex="20"
                fieldCode="<%=
                UnitCellSearchField.FieldCode.PERCENT_ERROR_TOLERANCE %>" />%
	    <ctl:errorMessage errorFilter="<%=UnitCellSearchFieldGroup.
                PERCENT_ERROR_TOLERANCE_MISSING%>">
              <span class="errorMessageSmall">
                * required
              </span>
            </ctl:errorMessage>
          </td>
        </tr>
      </rn:unitCellSearchFieldGroup>
      <tr>
        <th colspan="4" class="sectionHeader">
          Other Crystallographic Data
        </th>
      </tr>
      <tr>
        <th>Space Group:</th>
        <td colspan="3">
          <rn:searchParamsField tabIndex="23" id="spaceGroup"
              fieldCode="<%= SearchParamsField.SPACE_GROUP %>" />
          <span class="supplementalInstructions">
            (All settings of the specified group will be matched, with
            enantiomorphic distinctions ignored.)
          </span>
          <ctl:errorMessage errorSupplier="<%=spaceGroup%>"
              errorFilter="<%=SearchParamsField.VALIDATOR_REJECTED_VALUE%>">
            <span class="errorMessage">
              <br />
              The specified text is not a valid space group symbol.
          </ctl:errorMessage>
        </td>
      </tr>
      <tr>
        <th>Minimum Z:</th>
        <td>
          <rn:searchParamsField tabIndex="24" id="minZ"
              fieldCode="<%= SearchParamsField.MIN_Z %>" />
        </td>
        <th>Maximum Z:</th>
        <td>
          <rn:searchParamsField tabIndex="25" id="maxZ"
              fieldCode="<%= SearchParamsField.MAX_Z %>" />
          <ctl:errorMessage
              errorFilter="<%=SearchPage.MINZ_GREATER_THAN_MAXZ%>">
            <span class="errorMessageSmall">
              * cannot be less than Minimum Z.
            </span>
          </ctl:errorMessage>
        </td>
      </tr>
      <tr>
        <th>Volume:</th>
        <td>
          <rn:searchParamsField tabIndex="26" id="volume"
              fieldCode="<%= SearchParamsField.VOLUME %>" />&nbsp;&#177;&nbsp;
          <rn:searchParamsField tabIndex="27" id="rangeVolume"
              fieldCode="<%= SearchParamsField.RANGE_VOLUME %>" />%
        </td>
        <th>Density:</th>
        <td>
          <rn:searchParamsField tabIndex="28" id="density"
              fieldCode="<%= SearchParamsField.DENSITY %>" />&nbsp;&#177;&nbsp;
          <rn:searchParamsField tabIndex="29" id="rangeDensity"
              fieldCode="<%= SearchParamsField.RANGE_DENSITY %>" />%
        </td>
      </tr>
      <tr>
        <th>Temperature:</th>
        <td>
          <rn:searchParamsField tabIndex="30" id="temp"
              fieldCode="<%= SearchParamsField.TEMP %>" />&nbsp;&#177;&nbsp;
          <rn:searchParamsField tabIndex="31" id="rangeTemp"
              fieldCode="<%= SearchParamsField.RANGE_TEMP %>" />%
        </td>
        <td colspan="2"></td>
      </tr>
      <tr>
        <th colspan="4" class="sectionHeader">Atom Counts:</th>
      </tr>
      <tr>
        <td colspan="2">
          <ctl:errorMessage errorFilter="<%=SearchPage.ATOM_TYPE_MISSING%>">
            <p class="errorMessage">
              You must select an atom type whenever an atom count is supplied.
            </p>
          </ctl:errorMessage>
          <ctl:errorMessage errorFilter="<%=SearchPage.ATOM_COUNT_MISSING%>">
            <p class="errorMessage">
              You must select a positive atom count whenever an atom type is
              supplied.
            </p>
          </ctl:errorMessage>
          <ctl:errorMessage
                  errorFilter="<%=SearchPage.ATOM_OPERATOR_MISSING%>">
            <p class="errorMessage">
              You must select an operator whenever you select an atom type or
              count.
            </p>
          </ctl:errorMessage>
          <ctl:iterator id="countGroup" iterations="3">
          <div style="margin: 0.25em 0 0.25em 1.5em; white-space: nowrap;">
          <rn:searchParamsField tabIndex="32"
              groupKey="${countGroup.iterationCountSinceThisPhaseBegan + 1}"
              fieldCode="<%=SearchParamsField.SEARCH_ATOM_TYPE%>" />
          is present and occurs
          <rn:searchParamsField tabIndex="32"
              groupKey="${countGroup.iterationCountSinceThisPhaseBegan + 1}"
              fieldCode="<%=SearchParamsField.SEARCH_ATOM_OPERATOR%>" />
          <rn:searchParamsField tabIndex="32"
              groupKey="${countGroup.iterationCountSinceThisPhaseBegan + 1}"
              fieldCode="<%=SearchParamsField.SEARCH_ATOM_COUNT%>" />
          times.
          </div>
          </ctl:iterator>
        </td>
        <td colspan="2" style="vertical-align: top; padding-left: 1em;">
          <rn:searchParamsField tabIndex="41"
                  fieldCode="<%= SearchParamsField.SEARCH_ATOMS_FROM_EF%>" />
          Search <rn:sampleFieldLabel
                  fieldCode="<%=SampleTextBL.EMPIRICAL_FORMULA%>" />
          <br />
          <rn:searchParamsField tabIndex="42"
                  fieldCode="<%= SearchParamsField.SEARCH_ATOMS_FROM_EFD%>" />
          Search <rn:sampleFieldLabel
                  fieldCode="<%=SampleTextBL.EMPIRICAL_FORMULA_DERIVED%>" />
          <br />
          <rn:searchParamsField tabIndex="43"
                  fieldCode="<%= SearchParamsField.SEARCH_ATOMS_FROM_EFLS%>" />
          Search <rn:sampleFieldLabel fieldCode="<%=
                  SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT%>" />
          <br />
          <rn:searchParamsField tabIndex="44"
                  fieldCode="<%= SearchParamsField.SEARCH_ATOMS_FROM_EFSI%>" />
          Search <rn:sampleFieldLabel fieldCode="<%=
                  SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION%>" />
        </td>
      </tr>
      <tr>
        <th colspan="4" class="sectionHeader">Keywords</th>
      </tr>
      <tr>
        <th>Match: </th>
        <td colspan="3">
          <span style="padding-right: 1em;">
            <ctl:radioButtonGroup id="match" initialValue="any">
            <rn:searchParamsField tabIndex="45" id="matchAny"
                fieldCode="<%= SearchParamsField.MATCH_ANY_KEYWORD %>" />
            <strong>Any</strong> of these terms
          </span>
          <span style="padding-right: 1em;">
            <rn:searchParamsField tabIndex="46" id="matchAll"
                fieldCode="<%= SearchParamsField.MATCH_ALL_KEYWORD %>" />
            <strong>All</strong> of these terms
          </span>
          </ctl:radioButtonGroup>
        </td>
      </tr>
      <tr>
        <th>Terms:</th>
        <td colspan="3">
          <span style="padding-right: 1em;">
            <rn:searchParamsField tabIndex="47" id="keyword1"
                fieldCode="<%= SearchParamsField.KEYWORD %>" />
          </span>
          <span style="padding-right: 1em;">
            <rn:searchParamsField tabIndex="48" id="keyword2"
                fieldCode="<%= SearchParamsField.KEYWORD %>" />
          </span>
          <span style="padding-right: 1em;">
            <rn:searchParamsField tabIndex="49" id="keyword3"
                fieldCode="<%= SearchParamsField.KEYWORD %>" />
          </span>
        </td>
      </tr>
      <tr>
        <th colspan="4" class="sectionHeader">
          Ordering of Search Results
        </th>
      </tr>
      <tr>
        <th>Sort results by: </th>
        <td>
          <rn:searchParamsField tabIndex="50" id="sortOrder"
              fieldCode="<%= SearchParamsField.SORT_ORDER %>" />
        </td>
        <th>Results per page: </th>
        <td colspan="3">
          <ctl:listbox id="pageSize" initialValue="10">
            <ctl:extraHtmlAttribute name="tabindex" value="60"/>
            <ctl:option>5</ctl:option>
            <ctl:option>10</ctl:option>
            <ctl:option>25</ctl:option>
            <ctl:option>50</ctl:option>
            <ctl:option>100</ctl:option>
            <ctl:option>500</ctl:option>
          </ctl:listbox>
        </td>
      </tr>
      <tr>
        <td colspan="4"
            style="text-align: center; background-color: inherit; padding-top: 1em">
          <rn:searchButton id="searchButton" label="Search"/>
          <ctl:extraHtmlAttribute name="tabindex" value="1000"
              attributeAccepter="${searchButton}"/>
        </td>
      </tr>
    </table>
  </ctl:selfForm>
  </div>
  <ctl:styleBlock>
    .errorMessageSmall  { color: #F00000; font-size: x-small; }
    .supplementalInstructions { color: gray; font-style: italic; }
    table.searchTable  { border: thin solid #CCCCCC; }
    table.searchTable th,
    table.searchTable td { background-color: #EBEBEB }
    table.searchTable th { text-align: right; white-space: nowrap;}
    table.searchTable td { text-align: left; }
    table.searchTable th.sectionHeader { text-align: left;
        padding: 0.5em 0 0.1em 0.25em; background-color: #CCCCCC; }
  </ctl:styleBlock>
</rn:searchPage>
