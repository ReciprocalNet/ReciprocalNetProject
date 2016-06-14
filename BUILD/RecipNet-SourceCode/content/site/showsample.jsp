<%--
  - Reciprocal Net project
  - @(#)showsample.jsp
  -
  - 20-Jun-2002: hclin wrote first draft
  - 09-Jul-2002: leqian wrote second draft
  - 08-Aug-2002: leqian fixed bug #314
  - 14-Aug-2002: leqian did a minor UI change (task #347)
  - 12-Sep-2002: ekoperda did a major UI redesign to better integrate with the
  -              portal's common molecules section
  - 18-Sep-2002: ekoperda added measurement units and the formula weight field
  -              to this page's researcher mode UI
  - 20-Sep-2002: ekoperda removed references to JaMMed, added an informative
  -              title for researcher mode, restricted visibility of
  -              localtracking attributes, and rearranged UI
  - 26-Sep-2002: ekoperda fixed bug #482 that prevented all annotations from
  -              being displayed
  - 17-Oct-2002: ekoperda fixed bug #534 that worked around a bug in order-of-
  -              operations logic introduced by Sun's j2sdk1.4.1
  - 08-Nov-2002: adharurk changed logic in basic view to invoke
  -              pageHelper.getPreferredChemicalFormula(); added special
  -              handling for attributes of type EMPIRICAL_FORMULA_xxx
  - 20-Nov-2002: jobollin updated JaMM parameters
  - 17-Dec-2002: jobollin fixed malformed HTML output for JaMM 1 appleti
  -              parameters
  - 17-Dec-2002: jobollin removed applet parameters specifying table.dat
  -              as part of task #509
  - 19-Dec-2002: eisiorho added logic so that if there is no available .crt
  -              file, no applet will try to load.
  - 19-Mar-2003: ajooloor changed decimal precision for alpha, beta, gamma,
  -              and volume fields
  - 20-Mar-2003: eisiorho implemented jumpSiteMode
  - 26-Mar-2003: yli added special handling for retracted samples
  - 02-Apr-2003: midurbin fixed bug #849
  - 02-Apr-2003: ekoperda fixed bug #854
  - 21-Apr-2003: adharurk added call to reportSampleView() in PageHelper
  - 22-Apr-2003: dfeng modified naming-logic to utilize
  -              PageHelper.getSamplePreferredName(), etc.
  - 30-May-2003: midurbin changed repository file support to utilize FileHelper
  - 05-Jun-2003: ajooloor added placeholder
  - 10-Jun-2003: midurbin changed repository file support to accomodate
  -              FileHelper changes
  - 17-Jun-2003: midurbin included labId in pageHelper.reportSampleView() call
  - 03-Jul-2003: ajooloor added support for prior versions of samples'
                 data files
  - 17-Jul-2003: dfeng added link to sampleversions.jsp
  - 01-Aug-2003: midurbin fixed bug #1006
  - 11-Aug-2003: ajooloor fixed bug #1010
  - 12-Aug-2003: midurbin fixed bug #1015
  - 22-Aug-2003: jobollin fixed bug #1027
  - 07-Jan-2004: ekoperda changed package references due to source tree
  -              reorganization
  - 29-Mar-2004: midurbin fixed bug #1036
  - 01-Apr-2004: midurbin modified the UI in jumpSiteMode
  - 02-Apr-2004: midurbin added special formatting for IUPAC names
  - 29-Apr-2004: ajooloor fixed bug #1198
  - 08-Aug-2004: cwestnea modified to use SampleWorkflowBL
  - 14-Dec-2004: eisiorho changed texttype references to use new class
  -              SampleTextBL
  - 23-Mar-2005: midurbin altered scriptlets to use new preferred name/formula
  -              determination methods
  - 04-May-2005: midurbin rewrote page to showsamplebasic.jsp,
  -              showsampledetailed.jsp and showsamplejumpsite.jsp; this page
  -              now simply redirects the browser to the preferred mode.
  - 13-Jul-2005: midurbin added new required parameters to ShowSamplePage
  --%>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:showSamplePage basicPageUrl="/showsamplebasic.jsp"
    detailedPageUrl="/showsampledetailed.jsp"
    jumpSitePageUrl="/showsamplejumpsite.jsp" loginPageUrl="/login.jsp"
    currentPageReinvocationUrlParamName="origUrl"
    authorizationFailedReasonParamName="authorizationFailedReason">


  <%--
    This page will ALWAYS result in a redirection.  When possible, pages should
    link to the page to which this page would redirect the user to prevent
    extra HTTP roundtrips.
    --%>


</rn:showSamplePage>
