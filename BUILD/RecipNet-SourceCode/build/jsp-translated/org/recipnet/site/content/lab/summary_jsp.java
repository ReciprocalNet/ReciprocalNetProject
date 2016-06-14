package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.PaginationField;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.SampleHistoryField;

public final class summary_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_summarySearch_setSummaryDaysPrefParamName_maxSearchResultsToInclude;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchResultsIterator_usePaginationContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_relativeDayField_displayRelativeDayWhenSampleWasModified_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_relativeDayField_displaySampleDaysPreference_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_linkParam_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_summarySearch_statusCode_setSummarySamplesPrefParamName_ignorePreferredResultLimit;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_summarySearch_statusCode;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_summarySearch_setSummaryDaysPrefParamName_maxSearchResultsToInclude = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchResultsIterator_usePaginationContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_relativeDayField_displayRelativeDayWhenSampleWasModified_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_relativeDayField_displaySampleDaysPreference_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_linkParam_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_summarySearch_statusCode_setSummarySamplesPrefParamName_ignorePreferredResultLimit = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_summarySearch_statusCode = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.release();
    _jspx_tagPool_rn_summarySearch_setSummaryDaysPrefParamName_maxSearchResultsToInclude.release();
    _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_relativeDayField_displayRelativeDayWhenSampleWasModified_nobody.release();
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_rn_labParam_name_nobody.release();
    _jspx_tagPool_ctl_paginationField_fieldCode_nobody.release();
    _jspx_tagPool_rn_relativeDayField_displaySampleDaysPreference_nobody.release();
    _jspx_tagPool_ctl_linkParam_value_name_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_summarySearch_statusCode_setSummarySamplesPrefParamName_ignorePreferredResultLimit.release();
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.release();
    _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit.release();
    _jspx_tagPool_rn_summarySearch_statusCode.release();
    _jspx_tagPool_ctl_styleBlock.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\n\n\n\n\n\n\n");
      //  rn:page
      org.recipnet.site.content.rncontrols.RecipnetPage _jspx_th_rn_page_0 = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_tagPool_rn_page_title.get(org.recipnet.site.content.rncontrols.RecipnetPage.class);
      _jspx_th_rn_page_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_page_0.setParent(null);
      _jspx_th_rn_page_0.setTitle("Summary");
      int _jspx_eval_rn_page_0 = _jspx_th_rn_page_0.doStartTag();
      if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RecipnetPage htmlPage = null;
        if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_page_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_page_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_0.setCanSeeLabSummary(true);
          _jspx_th_rn_authorizationChecker_0.setInvert(true);
          int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_0.doInitBody();
            }
            do {
              out.write("\n    ");
              //  rn:redirectToLogin
              org.recipnet.site.content.rncontrols.RedirectToLogin _jspx_th_rn_redirectToLogin_0 = (org.recipnet.site.content.rncontrols.RedirectToLogin) _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.get(org.recipnet.site.content.rncontrols.RedirectToLogin.class);
              _jspx_th_rn_redirectToLogin_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_redirectToLogin_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
              _jspx_th_rn_redirectToLogin_0.setTarget("/login.jsp");
              _jspx_th_rn_redirectToLogin_0.setReturnUrlParamName("origUrl");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonParamName("authorizationFailedReason");
              _jspx_th_rn_redirectToLogin_0.setAuthorizationReasonCode(
          AuthorizationReasonMessage.CANNOT_SEE_LAB_SUMMARY);
              int _jspx_eval_rn_redirectToLogin_0 = _jspx_th_rn_redirectToLogin_0.doStartTag();
              if (_jspx_th_rn_redirectToLogin_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.reuse(_jspx_th_rn_redirectToLogin_0);
              out.write('\n');
              out.write(' ');
              out.write(' ');
              int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_0);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_1.setCanSeeLabSummary(true);
          int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_1.doInitBody();
            }
            do {
              out.write("\n    <table width=\"100%\">\n      <tr>\n        <td>\n          <font class=\"normal\" size=\"4\">Recently modified samples:</font>\n          <ul>\n            ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_0 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_setSummaryDaysPrefParamName_maxSearchResultsToInclude.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_0.setSetSummaryDaysPrefParamName("daysToShow");
              _jspx_th_rn_summarySearch_0.setMaxSearchResultsToInclude(50);
              int _jspx_eval_rn_summarySearch_0 = _jspx_th_rn_summarySearch_0.doStartTag();
              if (_jspx_eval_rn_summarySearch_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_0.doInitBody();
                }
                do {
                  out.write("\n              ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_0 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_0);
                  _jspx_th_rn_searchResultsIterator_0.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_0 = _jspx_th_rn_searchResultsIterator_0.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_0.doInitBody();
                    }
                    do {
                      out.write("\n                <li>\n                  ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
                      _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
                      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_0.doInitBody();
                        }
                        do {
                          out.write("\n                    ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_0);
                          _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
                          if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
                          out.write("\n                    ");
                          int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_0);
                      out.write(" modified\n                    ");
                      if (_jspx_meth_rn_relativeDayField_0(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                </li>\n              ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_0);
                  out.write("\n              ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_0 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_0);
                  _jspx_th_ctl_paginationChecker_0.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_0 = _jspx_th_ctl_paginationChecker_0.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n                <br />\n                <font class=\"light\">(50 most recent shown; \n                  ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_0);
                      _jspx_th_ctl_a_0.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
                      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_0.doInitBody();
                        }
                        do {
                          out.write("\n                    ");
                          if (_jspx_meth_rn_labParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
                            return;
                          out.write("list all\n                    ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_0 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
                          _jspx_th_ctl_paginationField_0.setFieldCode(
                        PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_0 = _jspx_th_ctl_paginationField_0.doStartTag();
                          if (_jspx_th_ctl_paginationField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_0);
                          int evalDoAfterBody = _jspx_th_ctl_a_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_0);
                      out.write(")\n                </font>\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_0);
                  out.write("\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_setSummaryDaysPrefParamName_maxSearchResultsToInclude.reuse(_jspx_th_rn_summarySearch_0);
              out.write("\n          </ul>\n          <font class=\"label\">\n            Samples modified ");
              if (_jspx_meth_rn_relativeDayField_1(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write(" are listed.\n          </font>\n          <br />\n          <font class=\"light\">List actions for last:\n            ");
              if (_jspx_meth_ctl_a_1(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_3(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_4(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_5(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_6(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n             -\n            ");
              if (_jspx_meth_ctl_a_7(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n            Days\n            <br />\n          </font> \n        </td>\n        <td valign=\"top\" align=\"right\">\n          <font class=\"normal\" size=\"4\">\n            ");
              if (_jspx_meth_rn_labField_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n          </font>\n          <br />\n        </td>\n      </tr>\n    </table>\n    <br />\n    <table cellspacing=\"10\">\n      <tr>\n        <td align=\"left\" colspan=\"4\">\n          <hr align=\"left\" width=\"300\" color=\"#4A6695\" />\n          <font class=\"normal\" size=\"4\">In-progress samples:</font>\n        </td>\n      </tr>\n      <tr>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Pending status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_1 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode_setSummarySamplesPrefParamName_ignorePreferredResultLimit.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_1.setSetSummarySamplesPrefParamName("oldSampleCount");
              _jspx_th_rn_summarySearch_1.setStatusCode(SampleWorkflowBL.PENDING_STATUS);
              _jspx_th_rn_summarySearch_1.setIgnorePreferredResultLimit(true);
              int _jspx_eval_rn_summarySearch_1 = _jspx_th_rn_summarySearch_1.doStartTag();
              if (_jspx_eval_rn_summarySearch_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_1.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_1 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_1);
                  _jspx_th_rn_searchResultsIterator_1.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_1 = _jspx_th_rn_searchResultsIterator_1.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_1.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_1);
                      _jspx_th_rn_link_1.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
                      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_1.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_1);
                          _jspx_th_rn_sampleField_1.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
                          if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_1);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_1);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_1);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_1 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_1);
                  _jspx_th_ctl_paginationChecker_1.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_1 = _jspx_th_ctl_paginationChecker_1.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_1.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_1 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_1);
                      _jspx_th_ctl_paginationField_1.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_1 = _jspx_th_ctl_paginationField_1.doStartTag();
                      if (_jspx_th_ctl_paginationField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_1);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_8 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_8.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_1);
                      _jspx_th_ctl_a_8.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_8 = _jspx_th_ctl_a_8.doStartTag();
                      if (_jspx_eval_ctl_a_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_8.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_1(_jspx_th_ctl_a_8, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_7 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_7.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_8);
                          _jspx_th_ctl_linkParam_7.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_7.setValue(String.valueOf(
                            SampleWorkflowBL.PENDING_STATUS));
                          int _jspx_eval_ctl_linkParam_7 = _jspx_th_ctl_linkParam_7.doStartTag();
                          if (_jspx_th_ctl_linkParam_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_7);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_2 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_2.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_8);
                          _jspx_th_ctl_paginationField_2.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_2 = _jspx_th_ctl_paginationField_2.doStartTag();
                          if (_jspx_th_ctl_paginationField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_2);
                          int evalDoAfterBody = _jspx_th_ctl_a_8.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_8);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_1);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_2 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_1);
                  _jspx_th_ctl_paginationChecker_2.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_2 = _jspx_th_ctl_paginationChecker_2.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_2.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_9 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_9.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_2);
                      _jspx_th_ctl_a_9.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_9 = _jspx_th_ctl_a_9.doStartTag();
                      if (_jspx_eval_ctl_a_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_9.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_2(_jspx_th_ctl_a_9, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_8 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_8.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_9);
                          _jspx_th_ctl_linkParam_8.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_8.setValue(String.valueOf(
                            SampleWorkflowBL.PENDING_STATUS));
                          int _jspx_eval_ctl_linkParam_8 = _jspx_th_ctl_linkParam_8.doStartTag();
                          if (_jspx_th_ctl_linkParam_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_8);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_9.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_9);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_2);
                  out.write("\n                  </font>\n                ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode_setSummarySamplesPrefParamName_ignorePreferredResultLimit.reuse(_jspx_th_rn_summarySearch_1);
              out.write("\n              </td>\n            </tr>\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Refinement pending status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_2 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_2.setStatusCode(
                        SampleWorkflowBL.REFINEMENT_PENDING_STATUS);
              _jspx_th_rn_summarySearch_2.setIgnorePreferredResultLimit(true);
              int _jspx_eval_rn_summarySearch_2 = _jspx_th_rn_summarySearch_2.doStartTag();
              if (_jspx_eval_rn_summarySearch_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_2.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_2 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_2);
                  _jspx_th_rn_searchResultsIterator_2.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_2 = _jspx_th_rn_searchResultsIterator_2.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_2.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_2);
                      _jspx_th_rn_link_2.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
                      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_2.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
                          _jspx_th_rn_sampleField_2.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_2.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
                          if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_2);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_2);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_2);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_3 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_2);
                  _jspx_th_ctl_paginationChecker_3.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_3 = _jspx_th_ctl_paginationChecker_3.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_3.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_3 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_3.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_3);
                      _jspx_th_ctl_paginationField_3.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_3 = _jspx_th_ctl_paginationField_3.doStartTag();
                      if (_jspx_th_ctl_paginationField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_3);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_10 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_10.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_3);
                      _jspx_th_ctl_a_10.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_10 = _jspx_th_ctl_a_10.doStartTag();
                      if (_jspx_eval_ctl_a_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_10.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_3(_jspx_th_ctl_a_10, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_9 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_9.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_10);
                          _jspx_th_ctl_linkParam_9.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_9.setValue(String.valueOf(
                            SampleWorkflowBL.REFINEMENT_PENDING_STATUS));
                          int _jspx_eval_ctl_linkParam_9 = _jspx_th_ctl_linkParam_9.doStartTag();
                          if (_jspx_th_ctl_linkParam_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_9);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_4 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_4.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_10);
                          _jspx_th_ctl_paginationField_4.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_4 = _jspx_th_ctl_paginationField_4.doStartTag();
                          if (_jspx_th_ctl_paginationField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_4);
                          int evalDoAfterBody = _jspx_th_ctl_a_10.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_10);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_3);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_4 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_2);
                  _jspx_th_ctl_paginationChecker_4.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_4 = _jspx_th_ctl_paginationChecker_4.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_4.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_11 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_11.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_4);
                      _jspx_th_ctl_a_11.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_11 = _jspx_th_ctl_a_11.doStartTag();
                      if (_jspx_eval_ctl_a_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_11.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_4(_jspx_th_ctl_a_11, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_10 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_10.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_11);
                          _jspx_th_ctl_linkParam_10.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_10.setValue(String.valueOf(
                            SampleWorkflowBL.REFINEMENT_PENDING_STATUS));
                          int _jspx_eval_ctl_linkParam_10 = _jspx_th_ctl_linkParam_10.doStartTag();
                          if (_jspx_th_ctl_linkParam_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_10);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_11.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_11);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_4);
                  out.write("\n                  </font>\n                ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit.reuse(_jspx_th_rn_summarySearch_2);
              out.write("\n              </td>\n            </tr>\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBoxStopped\">\n            <tr>\n              <th class=\"summaryHeaderStopped\">\n                <font class=\"dark\">\n                  Suspended status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_3 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_3.setStatusCode(SampleWorkflowBL.SUSPENDED_STATUS);
              _jspx_th_rn_summarySearch_3.setIgnorePreferredResultLimit(true);
              int _jspx_eval_rn_summarySearch_3 = _jspx_th_rn_summarySearch_3.doStartTag();
              if (_jspx_eval_rn_summarySearch_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_3.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_3 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_3);
                  _jspx_th_rn_searchResultsIterator_3.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_3 = _jspx_th_rn_searchResultsIterator_3.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_3.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_3);
                      _jspx_th_rn_link_3.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
                      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_3.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_3);
                          _jspx_th_rn_sampleField_3.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_3.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
                          if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_3);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_3);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_3);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_5 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_5.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_3);
                  _jspx_th_ctl_paginationChecker_5.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_5 = _jspx_th_ctl_paginationChecker_5.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_5.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_5 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_5);
                      _jspx_th_ctl_paginationField_5.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_5 = _jspx_th_ctl_paginationField_5.doStartTag();
                      if (_jspx_th_ctl_paginationField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_5);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_12 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_12.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_5);
                      _jspx_th_ctl_a_12.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_12 = _jspx_th_ctl_a_12.doStartTag();
                      if (_jspx_eval_ctl_a_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_12.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_5(_jspx_th_ctl_a_12, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_11 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_11.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_12);
                          _jspx_th_ctl_linkParam_11.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_11.setValue(String.valueOf(
                            SampleWorkflowBL.SUSPENDED_STATUS));
                          int _jspx_eval_ctl_linkParam_11 = _jspx_th_ctl_linkParam_11.doStartTag();
                          if (_jspx_th_ctl_linkParam_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_11);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_6 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_6.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_12);
                          _jspx_th_ctl_paginationField_6.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_6 = _jspx_th_ctl_paginationField_6.doStartTag();
                          if (_jspx_th_ctl_paginationField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_6);
                          int evalDoAfterBody = _jspx_th_ctl_a_12.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_12);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_5);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_6 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_6.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_3);
                  _jspx_th_ctl_paginationChecker_6.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_6 = _jspx_th_ctl_paginationChecker_6.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_6.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_13 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_13.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_6);
                      _jspx_th_ctl_a_13.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_13 = _jspx_th_ctl_a_13.doStartTag();
                      if (_jspx_eval_ctl_a_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_13.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_6(_jspx_th_ctl_a_13, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_12 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_12.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_13);
                          _jspx_th_ctl_linkParam_12.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_12.setValue(String.valueOf(
                            SampleWorkflowBL.SUSPENDED_STATUS));
                          int _jspx_eval_ctl_linkParam_12 = _jspx_th_ctl_linkParam_12.doStartTag();
                          if (_jspx_th_ctl_linkParam_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_12);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_13.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_13);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_6);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode_ignorePreferredResultLimit.reuse(_jspx_th_rn_summarySearch_3);
              out.write("\n          </table>\n        </td>\n        <td>&nbsp;</td>\n      </tr>\n      <tr>\n        <td align=\"left\" colspan=\"4\">\n          <hr align=\"left\" width=\"300\" color=\"#4A6695\" />\n          <font class=\"normal\" size=\"4\">Finished samples:</font>\n        </td>\n      </tr>\n      <tr>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Complete status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_4 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_4.setStatusCode(SampleWorkflowBL.COMPLETE_STATUS);
              int _jspx_eval_rn_summarySearch_4 = _jspx_th_rn_summarySearch_4.doStartTag();
              if (_jspx_eval_rn_summarySearch_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_4.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_4 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_4);
                  _jspx_th_rn_searchResultsIterator_4.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_4 = _jspx_th_rn_searchResultsIterator_4.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_4.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_4 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_4.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_4);
                      _jspx_th_rn_link_4.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_4 = _jspx_th_rn_link_4.doStartTag();
                      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_4.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_4);
                          _jspx_th_rn_sampleField_4.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_4.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
                          if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_4);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_4.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_4);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_4);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_7 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_7.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_4);
                  _jspx_th_ctl_paginationChecker_7.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_7 = _jspx_th_ctl_paginationChecker_7.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_7.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_7 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_7.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_7);
                      _jspx_th_ctl_paginationField_7.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_7 = _jspx_th_ctl_paginationField_7.doStartTag();
                      if (_jspx_th_ctl_paginationField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_7);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_14 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_14.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_7);
                      _jspx_th_ctl_a_14.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_14 = _jspx_th_ctl_a_14.doStartTag();
                      if (_jspx_eval_ctl_a_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_14.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_7(_jspx_th_ctl_a_14, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_13 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_13.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_14);
                          _jspx_th_ctl_linkParam_13.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_13.setValue(String.valueOf(
                            SampleWorkflowBL.COMPLETE_STATUS));
                          int _jspx_eval_ctl_linkParam_13 = _jspx_th_ctl_linkParam_13.doStartTag();
                          if (_jspx_th_ctl_linkParam_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_13);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_8 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_8.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_14);
                          _jspx_th_ctl_paginationField_8.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_8 = _jspx_th_ctl_paginationField_8.doStartTag();
                          if (_jspx_th_ctl_paginationField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_8);
                          int evalDoAfterBody = _jspx_th_ctl_a_14.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_14);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_7);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_8 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_8.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_4);
                  _jspx_th_ctl_paginationChecker_8.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_8 = _jspx_th_ctl_paginationChecker_8.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_8.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_15 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_15.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_8);
                      _jspx_th_ctl_a_15.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_15 = _jspx_th_ctl_a_15.doStartTag();
                      if (_jspx_eval_ctl_a_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_15.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_8(_jspx_th_ctl_a_15, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_14 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_14.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_15);
                          _jspx_th_ctl_linkParam_14.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_14.setValue(String.valueOf(
                            SampleWorkflowBL.COMPLETE_STATUS));
                          int _jspx_eval_ctl_linkParam_14 = _jspx_th_ctl_linkParam_14.doStartTag();
                          if (_jspx_th_ctl_linkParam_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_14);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_15.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_15);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_8.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_8);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_4.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_4);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Incomplete status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_5 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_5.setStatusCode(SampleWorkflowBL.INCOMPLETE_STATUS);
              int _jspx_eval_rn_summarySearch_5 = _jspx_th_rn_summarySearch_5.doStartTag();
              if (_jspx_eval_rn_summarySearch_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_5.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_5 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_5);
                  _jspx_th_rn_searchResultsIterator_5.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_5 = _jspx_th_rn_searchResultsIterator_5.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_5.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_5 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_5.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_5);
                      _jspx_th_rn_link_5.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_5 = _jspx_th_rn_link_5.doStartTag();
                      if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_5.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_5);
                          _jspx_th_rn_sampleField_5.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_5.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
                          if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_5);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_5.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_5);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_5);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_9 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_9.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_5);
                  _jspx_th_ctl_paginationChecker_9.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_9 = _jspx_th_ctl_paginationChecker_9.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_9.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_9 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_9.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_9);
                      _jspx_th_ctl_paginationField_9.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_9 = _jspx_th_ctl_paginationField_9.doStartTag();
                      if (_jspx_th_ctl_paginationField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_9);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_16 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_16.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_9);
                      _jspx_th_ctl_a_16.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_16 = _jspx_th_ctl_a_16.doStartTag();
                      if (_jspx_eval_ctl_a_16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_16.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_9(_jspx_th_ctl_a_16, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_15 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_15.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_16);
                          _jspx_th_ctl_linkParam_15.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_15.setValue(String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_STATUS));
                          int _jspx_eval_ctl_linkParam_15 = _jspx_th_ctl_linkParam_15.doStartTag();
                          if (_jspx_th_ctl_linkParam_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_15);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_10 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_10.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_16);
                          _jspx_th_ctl_paginationField_10.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_10 = _jspx_th_ctl_paginationField_10.doStartTag();
                          if (_jspx_th_ctl_paginationField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_10);
                          int evalDoAfterBody = _jspx_th_ctl_a_16.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_16);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_9.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_9);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_10 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_10.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_5);
                  _jspx_th_ctl_paginationChecker_10.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_10 = _jspx_th_ctl_paginationChecker_10.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_10.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_17 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_17.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_10);
                      _jspx_th_ctl_a_17.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_17 = _jspx_th_ctl_a_17.doStartTag();
                      if (_jspx_eval_ctl_a_17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_17.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_10(_jspx_th_ctl_a_17, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_16 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_16.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_17);
                          _jspx_th_ctl_linkParam_16.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_16.setValue(String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_STATUS));
                          int _jspx_eval_ctl_linkParam_16 = _jspx_th_ctl_linkParam_16.doStartTag();
                          if (_jspx_th_ctl_linkParam_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_16);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_17.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_17);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_10.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_10);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_5.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_5);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Non-single-crystal sample status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_6 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_6.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_6.setStatusCode(SampleWorkflowBL.NON_SCS_STATUS);
              int _jspx_eval_rn_summarySearch_6 = _jspx_th_rn_summarySearch_6.doStartTag();
              if (_jspx_eval_rn_summarySearch_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_6.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_6 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_6);
                  _jspx_th_rn_searchResultsIterator_6.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_6 = _jspx_th_rn_searchResultsIterator_6.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_6.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_6 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_6.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_6);
                      _jspx_th_rn_link_6.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_6 = _jspx_th_rn_link_6.doStartTag();
                      if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_6.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_6);
                          _jspx_th_rn_sampleField_6.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_6.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
                          if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_6);
                          out.write("\n                    ");
                          int evalDoAfterBody = _jspx_th_rn_link_6.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_6);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_6.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_6);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_11 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_11.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_6);
                  _jspx_th_ctl_paginationChecker_11.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_11 = _jspx_th_ctl_paginationChecker_11.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_11.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_11 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_11.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_11);
                      _jspx_th_ctl_paginationField_11.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_11 = _jspx_th_ctl_paginationField_11.doStartTag();
                      if (_jspx_th_ctl_paginationField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_11);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_18 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_18.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_11);
                      _jspx_th_ctl_a_18.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_18 = _jspx_th_ctl_a_18.doStartTag();
                      if (_jspx_eval_ctl_a_18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_18.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_11(_jspx_th_ctl_a_18, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_17 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_17.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_18);
                          _jspx_th_ctl_linkParam_17.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_17.setValue(String.valueOf(
                            SampleWorkflowBL.NON_SCS_STATUS));
                          int _jspx_eval_ctl_linkParam_17 = _jspx_th_ctl_linkParam_17.doStartTag();
                          if (_jspx_th_ctl_linkParam_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_17);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_12 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_12.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_18);
                          _jspx_th_ctl_paginationField_12.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_12 = _jspx_th_ctl_paginationField_12.doStartTag();
                          if (_jspx_th_ctl_paginationField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_12);
                          int evalDoAfterBody = _jspx_th_ctl_a_18.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_18);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_11.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_11);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_12 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_12.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_6);
                  _jspx_th_ctl_paginationChecker_12.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_12 = _jspx_th_ctl_paginationChecker_12.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_12.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_19 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_19.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_12);
                      _jspx_th_ctl_a_19.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_19 = _jspx_th_ctl_a_19.doStartTag();
                      if (_jspx_eval_ctl_a_19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_19.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_12(_jspx_th_ctl_a_19, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_18 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_18.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_19);
                          _jspx_th_ctl_linkParam_18.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_18.setValue(String.valueOf(
                            SampleWorkflowBL.NON_SCS_STATUS));
                          int _jspx_eval_ctl_linkParam_18 = _jspx_th_ctl_linkParam_18.doStartTag();
                          if (_jspx_th_ctl_linkParam_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_18);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_19.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_19);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_12.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_12);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_6.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_6);
              out.write("\n          </table>\n        </td>\n        <td>&nbsp;</td>\n      </tr>\n      <tr>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Withdrawn status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_7 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_7.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_7.setStatusCode(SampleWorkflowBL.WITHDRAWN_STATUS);
              int _jspx_eval_rn_summarySearch_7 = _jspx_th_rn_summarySearch_7.doStartTag();
              if (_jspx_eval_rn_summarySearch_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_7.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_7 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_7.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_7);
                  _jspx_th_rn_searchResultsIterator_7.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_7 = _jspx_th_rn_searchResultsIterator_7.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_7.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_7 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_7.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_7);
                      _jspx_th_rn_link_7.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_7 = _jspx_th_rn_link_7.doStartTag();
                      if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_7.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_7 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_7.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_7);
                          _jspx_th_rn_sampleField_7.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_7.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_7 = _jspx_th_rn_sampleField_7.doStartTag();
                          if (_jspx_th_rn_sampleField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_7);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_7.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_7);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_7);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_13 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_13.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_7);
                  _jspx_th_ctl_paginationChecker_13.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_13 = _jspx_th_ctl_paginationChecker_13.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_13.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_13 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_13.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_13);
                      _jspx_th_ctl_paginationField_13.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_13 = _jspx_th_ctl_paginationField_13.doStartTag();
                      if (_jspx_th_ctl_paginationField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_13);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_20 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_20.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_13);
                      _jspx_th_ctl_a_20.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_20 = _jspx_th_ctl_a_20.doStartTag();
                      if (_jspx_eval_ctl_a_20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_20.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_20.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_13(_jspx_th_ctl_a_20, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_19 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_19.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_20);
                          _jspx_th_ctl_linkParam_19.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_19.setValue(String.valueOf(
                            SampleWorkflowBL.WITHDRAWN_STATUS));
                          int _jspx_eval_ctl_linkParam_19 = _jspx_th_ctl_linkParam_19.doStartTag();
                          if (_jspx_th_ctl_linkParam_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_19);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_14 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_14.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_20);
                          _jspx_th_ctl_paginationField_14.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_14 = _jspx_th_ctl_paginationField_14.doStartTag();
                          if (_jspx_th_ctl_paginationField_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_14);
                          int evalDoAfterBody = _jspx_th_ctl_a_20.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_20);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_13.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_13);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_14 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_14.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_7);
                  _jspx_th_ctl_paginationChecker_14.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_14 = _jspx_th_ctl_paginationChecker_14.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_14.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_21 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_21.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_14);
                      _jspx_th_ctl_a_21.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_21 = _jspx_th_ctl_a_21.doStartTag();
                      if (_jspx_eval_ctl_a_21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_21.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_21.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_14(_jspx_th_ctl_a_21, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_20 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_20.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_21);
                          _jspx_th_ctl_linkParam_20.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_20.setValue(String.valueOf(
                            SampleWorkflowBL.WITHDRAWN_STATUS));
                          int _jspx_eval_ctl_linkParam_20 = _jspx_th_ctl_linkParam_20.doStartTag();
                          if (_jspx_th_ctl_linkParam_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_20);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_21.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_21);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_14.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_14);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_7.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_7);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  \"No go\" status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_8 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_8.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_8.setStatusCode(SampleWorkflowBL.NOGO_STATUS);
              int _jspx_eval_rn_summarySearch_8 = _jspx_th_rn_summarySearch_8.doStartTag();
              if (_jspx_eval_rn_summarySearch_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_8.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_8 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_8);
                  _jspx_th_rn_searchResultsIterator_8.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_8 = _jspx_th_rn_searchResultsIterator_8.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_8.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_8 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_8.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_8);
                      _jspx_th_rn_link_8.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_8 = _jspx_th_rn_link_8.doStartTag();
                      if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_8.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_8 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_8.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_8);
                          _jspx_th_rn_sampleField_8.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_8.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_8 = _jspx_th_rn_sampleField_8.doStartTag();
                          if (_jspx_th_rn_sampleField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_8);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_8.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_8);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_8.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_8);
                  out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"right\">\n                <font class=\"light\">\n                  ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_15 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_15.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_8);
                  _jspx_th_ctl_paginationChecker_15.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_15 = _jspx_th_ctl_paginationChecker_15.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_15.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_15 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_15.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_15);
                      _jspx_th_ctl_paginationField_15.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_15 = _jspx_th_ctl_paginationField_15.doStartTag();
                      if (_jspx_th_ctl_paginationField_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_15);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_22 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_22.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_15);
                      _jspx_th_ctl_a_22.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_22 = _jspx_th_ctl_a_22.doStartTag();
                      if (_jspx_eval_ctl_a_22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_22.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_22.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_15(_jspx_th_ctl_a_22, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_21 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_21.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_22);
                          _jspx_th_ctl_linkParam_21.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_21.setValue(String.valueOf(
                            SampleWorkflowBL.NOGO_STATUS));
                          int _jspx_eval_ctl_linkParam_21 = _jspx_th_ctl_linkParam_21.doStartTag();
                          if (_jspx_th_ctl_linkParam_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_21);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_16 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_16.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_22);
                          _jspx_th_ctl_paginationField_16.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_16 = _jspx_th_ctl_paginationField_16.doStartTag();
                          if (_jspx_th_ctl_paginationField_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_16);
                          int evalDoAfterBody = _jspx_th_ctl_a_22.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_22);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_15.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_15);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_16 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_16.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_8);
                  _jspx_th_ctl_paginationChecker_16.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_16 = _jspx_th_ctl_paginationChecker_16.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_16.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_23 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_23.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_16);
                      _jspx_th_ctl_a_23.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_23 = _jspx_th_ctl_a_23.doStartTag();
                      if (_jspx_eval_ctl_a_23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_23.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_23.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_16(_jspx_th_ctl_a_23, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_22 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_22.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_23);
                          _jspx_th_ctl_linkParam_22.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_22.setValue(String.valueOf(
                            SampleWorkflowBL.NOGO_STATUS));
                          int _jspx_eval_ctl_linkParam_22 = _jspx_th_ctl_linkParam_22.doStartTag();
                          if (_jspx_th_ctl_linkParam_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_22);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_23.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_23);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_16.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_16);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_8.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_8);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBoxStopped\">\n            <tr>\n              <th class=\"summaryHeaderStopped\">\n                <font class=\"dark\">\n                  Retracted status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_9 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_9.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_9.setStatusCode(SampleWorkflowBL.RETRACTED_STATUS);
              int _jspx_eval_rn_summarySearch_9 = _jspx_th_rn_summarySearch_9.doStartTag();
              if (_jspx_eval_rn_summarySearch_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_9.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_9 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_9);
                  _jspx_th_rn_searchResultsIterator_9.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_9 = _jspx_th_rn_searchResultsIterator_9.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_9.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_9 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_9.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_9);
                      _jspx_th_rn_link_9.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_9 = _jspx_th_rn_link_9.doStartTag();
                      if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_9.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_9 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_9.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_9);
                          _jspx_th_rn_sampleField_9.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_9.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_9 = _jspx_th_rn_sampleField_9.doStartTag();
                          if (_jspx_th_rn_sampleField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_9);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_9.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_9);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_9.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_9);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_17 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_17.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_9);
                  _jspx_th_ctl_paginationChecker_17.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_17 = _jspx_th_ctl_paginationChecker_17.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_17.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_17 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_17.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_17);
                      _jspx_th_ctl_paginationField_17.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_17 = _jspx_th_ctl_paginationField_17.doStartTag();
                      if (_jspx_th_ctl_paginationField_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_17);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_24 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_24.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_17);
                      _jspx_th_ctl_a_24.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_24 = _jspx_th_ctl_a_24.doStartTag();
                      if (_jspx_eval_ctl_a_24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_24.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_24.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_17(_jspx_th_ctl_a_24, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_23 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_23.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_24);
                          _jspx_th_ctl_linkParam_23.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_23.setValue(String.valueOf(
                             SampleWorkflowBL.RETRACTED_STATUS));
                          int _jspx_eval_ctl_linkParam_23 = _jspx_th_ctl_linkParam_23.doStartTag();
                          if (_jspx_th_ctl_linkParam_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_23);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_18 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_18.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_24);
                          _jspx_th_ctl_paginationField_18.setFieldCode(
                             PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_18 = _jspx_th_ctl_paginationField_18.doStartTag();
                          if (_jspx_th_ctl_paginationField_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_18);
                          int evalDoAfterBody = _jspx_th_ctl_a_24.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_24);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_17.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_17);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_18 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_18.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_9);
                  _jspx_th_ctl_paginationChecker_18.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_18 = _jspx_th_ctl_paginationChecker_18.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_18.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_25 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_25.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_18);
                      _jspx_th_ctl_a_25.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_25 = _jspx_th_ctl_a_25.doStartTag();
                      if (_jspx_eval_ctl_a_25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_25.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_25.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_18(_jspx_th_ctl_a_25, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_24 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_24.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_25);
                          _jspx_th_ctl_linkParam_24.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_24.setValue(String.valueOf(
                            SampleWorkflowBL.RETRACTED_STATUS));
                          int _jspx_eval_ctl_linkParam_24 = _jspx_th_ctl_linkParam_24.doStartTag();
                          if (_jspx_th_ctl_linkParam_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_24);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_25.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_25);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_18.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_18);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_9.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_9);
              out.write("\n          </table>\n        </td>\n        <td>&nbsp;</td>\n      </tr>\n      <tr>\n        <td align=\"left\" colspan=\"4\">\n          <hr align=\"left\" width=\"300\" color=\"#4A6695\" />\n          <font class=\"normal\" size=\"4\">Finished samples:</font>\n        </td>\n      </tr>\n      <tr>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Complete, visible to public status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_10 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_10.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_10.setStatusCode(SampleWorkflowBL.COMPLETE_PUBLIC_STATUS);
              int _jspx_eval_rn_summarySearch_10 = _jspx_th_rn_summarySearch_10.doStartTag();
              if (_jspx_eval_rn_summarySearch_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_10.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_10 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_10.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_10);
                  _jspx_th_rn_searchResultsIterator_10.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_10 = _jspx_th_rn_searchResultsIterator_10.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_10.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_10 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_10.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_10);
                      _jspx_th_rn_link_10.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_10 = _jspx_th_rn_link_10.doStartTag();
                      if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_10.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_10 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_10.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_10);
                          _jspx_th_rn_sampleField_10.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_10.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_10 = _jspx_th_rn_sampleField_10.doStartTag();
                          if (_jspx_th_rn_sampleField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_10);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_10.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_10);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_10.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_10);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_19 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_19.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_10);
                  _jspx_th_ctl_paginationChecker_19.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_19 = _jspx_th_ctl_paginationChecker_19.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_19.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_19 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_19.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_19);
                      _jspx_th_ctl_paginationField_19.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_19 = _jspx_th_ctl_paginationField_19.doStartTag();
                      if (_jspx_th_ctl_paginationField_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_19);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_26 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_26.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_19);
                      _jspx_th_ctl_a_26.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_26 = _jspx_th_ctl_a_26.doStartTag();
                      if (_jspx_eval_ctl_a_26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_26.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_26.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_19(_jspx_th_ctl_a_26, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_25 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_25.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_26);
                          _jspx_th_ctl_linkParam_25.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_25.setValue(String.valueOf(
                            SampleWorkflowBL.COMPLETE_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_25 = _jspx_th_ctl_linkParam_25.doStartTag();
                          if (_jspx_th_ctl_linkParam_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_25);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_20 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_20.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_26);
                          _jspx_th_ctl_paginationField_20.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_20 = _jspx_th_ctl_paginationField_20.doStartTag();
                          if (_jspx_th_ctl_paginationField_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_20);
                          int evalDoAfterBody = _jspx_th_ctl_a_26.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_26);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_19.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_19);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_20 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_20.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_10);
                  _jspx_th_ctl_paginationChecker_20.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_20 = _jspx_th_ctl_paginationChecker_20.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_20.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_20.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_27 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_27.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_20);
                      _jspx_th_ctl_a_27.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_27 = _jspx_th_ctl_a_27.doStartTag();
                      if (_jspx_eval_ctl_a_27 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_27 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_27.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_27.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_20(_jspx_th_ctl_a_27, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_26 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_26.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_27);
                          _jspx_th_ctl_linkParam_26.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_26.setValue(String.valueOf(
                            SampleWorkflowBL.COMPLETE_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_26 = _jspx_th_ctl_linkParam_26.doStartTag();
                          if (_jspx_th_ctl_linkParam_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_26);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_27.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_27 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_27);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_20.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_20 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_20);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_10);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Incomplete, visible to public status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_11 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_11.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_11.setStatusCode(
                    SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS);
              int _jspx_eval_rn_summarySearch_11 = _jspx_th_rn_summarySearch_11.doStartTag();
              if (_jspx_eval_rn_summarySearch_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_11.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_11 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_11.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_11);
                  _jspx_th_rn_searchResultsIterator_11.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_11 = _jspx_th_rn_searchResultsIterator_11.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_11.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_11 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_11.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_11);
                      _jspx_th_rn_link_11.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_11 = _jspx_th_rn_link_11.doStartTag();
                      if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_11.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_11 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_11.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_11);
                          _jspx_th_rn_sampleField_11.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_11.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_11 = _jspx_th_rn_sampleField_11.doStartTag();
                          if (_jspx_th_rn_sampleField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_11);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_11.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_11);
                      out.write("&nbsp;&nbsp;\n                    ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_11.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_11);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_21 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_21.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_11);
                  _jspx_th_ctl_paginationChecker_21.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_21 = _jspx_th_ctl_paginationChecker_21.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_21.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_21.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_21 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_21.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_21);
                      _jspx_th_ctl_paginationField_21.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_21 = _jspx_th_ctl_paginationField_21.doStartTag();
                      if (_jspx_th_ctl_paginationField_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_21);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_28 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_28.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_21);
                      _jspx_th_ctl_a_28.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_28 = _jspx_th_ctl_a_28.doStartTag();
                      if (_jspx_eval_ctl_a_28 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_28 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_28.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_28.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_21(_jspx_th_ctl_a_28, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_27 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_27.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_28);
                          _jspx_th_ctl_linkParam_27.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_27.setValue(String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_27 = _jspx_th_ctl_linkParam_27.doStartTag();
                          if (_jspx_th_ctl_linkParam_27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_27);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_22 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_22.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_28);
                          _jspx_th_ctl_paginationField_22.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_22 = _jspx_th_ctl_paginationField_22.doStartTag();
                          if (_jspx_th_ctl_paginationField_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_22);
                          int evalDoAfterBody = _jspx_th_ctl_a_28.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_28 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_28);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_21.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_21 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_21);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_22 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_22.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_11);
                  _jspx_th_ctl_paginationChecker_22.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_22 = _jspx_th_ctl_paginationChecker_22.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_22 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_22.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_22.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_29 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_29.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_22);
                      _jspx_th_ctl_a_29.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_29 = _jspx_th_ctl_a_29.doStartTag();
                      if (_jspx_eval_ctl_a_29 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_29 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_29.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_29.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_22(_jspx_th_ctl_a_29, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_28 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_28.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_29);
                          _jspx_th_ctl_linkParam_28.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_28.setValue(String.valueOf(
                            SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_28 = _jspx_th_ctl_linkParam_28.doStartTag();
                          if (_jspx_th_ctl_linkParam_28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_28);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_29.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_29 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_29);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_22.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_22 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_22);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_11.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_11);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBox\">\n            <tr>\n              <th class=\"summaryHeader\">\n                <font class=\"dark\">\n                  Non-single-crystal sample, visible to public status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_12 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_12.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_12.setStatusCode(SampleWorkflowBL.NON_SCS_PUBLIC_STATUS);
              int _jspx_eval_rn_summarySearch_12 = _jspx_th_rn_summarySearch_12.doStartTag();
              if (_jspx_eval_rn_summarySearch_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_12.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_12 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_12.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_12);
                  _jspx_th_rn_searchResultsIterator_12.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_12 = _jspx_th_rn_searchResultsIterator_12.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_12.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_12 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_12.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_12);
                      _jspx_th_rn_link_12.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_12 = _jspx_th_rn_link_12.doStartTag();
                      if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_12.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_12 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_12.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_12);
                          _jspx_th_rn_sampleField_12.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_12.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_12 = _jspx_th_rn_sampleField_12.doStartTag();
                          if (_jspx_th_rn_sampleField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_12);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_12.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_12);
                      out.write("&nbsp;&nbsp;\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_12.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_12);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_23 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_23.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_12);
                  _jspx_th_ctl_paginationChecker_23.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_23 = _jspx_th_ctl_paginationChecker_23.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_23 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_23.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_23.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_23 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_23.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_23);
                      _jspx_th_ctl_paginationField_23.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_23 = _jspx_th_ctl_paginationField_23.doStartTag();
                      if (_jspx_th_ctl_paginationField_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_23);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_30 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_30.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_23);
                      _jspx_th_ctl_a_30.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_30 = _jspx_th_ctl_a_30.doStartTag();
                      if (_jspx_eval_ctl_a_30 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_30 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_30.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_30.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_23(_jspx_th_ctl_a_30, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_29 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_29.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_30);
                          _jspx_th_ctl_linkParam_29.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_29.setValue(String.valueOf(
                            SampleWorkflowBL.NON_SCS_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_29 = _jspx_th_ctl_linkParam_29.doStartTag();
                          if (_jspx_th_ctl_linkParam_29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_29);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_24 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_24.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_30);
                          _jspx_th_ctl_paginationField_24.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_24 = _jspx_th_ctl_paginationField_24.doStartTag();
                          if (_jspx_th_ctl_paginationField_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_24);
                          int evalDoAfterBody = _jspx_th_ctl_a_30.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_30 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_30);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_23.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_23 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_23);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_24 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_24.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_12);
                  _jspx_th_ctl_paginationChecker_24.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_24 = _jspx_th_ctl_paginationChecker_24.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_24 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_24.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_24.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_31 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_31.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_24);
                      _jspx_th_ctl_a_31.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_31 = _jspx_th_ctl_a_31.doStartTag();
                      if (_jspx_eval_ctl_a_31 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_31 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_31.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_31.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_24(_jspx_th_ctl_a_31, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_30 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_30.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_31);
                          _jspx_th_ctl_linkParam_30.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_30.setValue(String.valueOf(
                            SampleWorkflowBL.NON_SCS_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_30 = _jspx_th_ctl_linkParam_30.doStartTag();
                          if (_jspx_th_ctl_linkParam_30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_30);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_31.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_31 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_31);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_24.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_24 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_24);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_12.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_12);
              out.write("\n          </table>\n        </td>\n        <td valign=\"top\" width=\"250\">\n          <table class=\"summaryBoxStopped\">\n            <tr>\n              <th class=\"summaryHeaderStopped\">\n                <font class=\"dark\">\n                  Retracted, visible to public status\n                </font>\n              </th>\n            </tr>\n            <tr>\n              <td class=\"summaryBody\">\n                ");
              //  rn:summarySearch
              org.recipnet.site.content.rncontrols.SummarySearch _jspx_th_rn_summarySearch_13 = (org.recipnet.site.content.rncontrols.SummarySearch) _jspx_tagPool_rn_summarySearch_statusCode.get(org.recipnet.site.content.rncontrols.SummarySearch.class);
              _jspx_th_rn_summarySearch_13.setPageContext(_jspx_page_context);
              _jspx_th_rn_summarySearch_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_summarySearch_13.setStatusCode(SampleWorkflowBL.RETRACTED_PUBLIC_STATUS);
              int _jspx_eval_rn_summarySearch_13 = _jspx_th_rn_summarySearch_13.doStartTag();
              if (_jspx_eval_rn_summarySearch_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_summarySearch_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_summarySearch_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_summarySearch_13.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:searchResultsIterator
                  org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_13 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
                  _jspx_th_rn_searchResultsIterator_13.setPageContext(_jspx_page_context);
                  _jspx_th_rn_searchResultsIterator_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_13);
                  _jspx_th_rn_searchResultsIterator_13.setUsePaginationContext(true);
                  int _jspx_eval_rn_searchResultsIterator_13 = _jspx_th_rn_searchResultsIterator_13.doStartTag();
                  if (_jspx_eval_rn_searchResultsIterator_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_searchResultsIterator_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_searchResultsIterator_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_searchResultsIterator_13.doInitBody();
                    }
                    do {
                      out.write("\n                    ");
                      //  rn:link
                      org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_13 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                      _jspx_th_rn_link_13.setPageContext(_jspx_page_context);
                      _jspx_th_rn_link_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_13);
                      _jspx_th_rn_link_13.setHref("/lab/sample.jsp");
                      int _jspx_eval_rn_link_13 = _jspx_th_rn_link_13.doStartTag();
                      if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_link_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_link_13.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleField
                          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_13 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                          _jspx_th_rn_sampleField_13.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_13);
                          _jspx_th_rn_sampleField_13.setDisplayValueOnly(true);
                          _jspx_th_rn_sampleField_13.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                          int _jspx_eval_rn_sampleField_13 = _jspx_th_rn_sampleField_13.doStartTag();
                          if (_jspx_th_rn_sampleField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_13);
                          out.write("\n                      ");
                          int evalDoAfterBody = _jspx_th_rn_link_13.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_link_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_13);
                      out.write("&nbsp;&nbsp;\n                    ");
                      int evalDoAfterBody = _jspx_th_rn_searchResultsIterator_13.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_searchResultsIterator_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_searchResultsIterator_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.reuse(_jspx_th_rn_searchResultsIterator_13);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <td align=\"right\">\n                  <font class=\"light\">\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_25 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_25.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_13);
                  _jspx_th_ctl_paginationChecker_25.setRequirePageCountNoLessThan(2);
                  int _jspx_eval_ctl_paginationChecker_25 = _jspx_th_ctl_paginationChecker_25.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_25 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_25.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_25.doInitBody();
                    }
                    do {
                      out.write("\n                      (");
                      //  ctl:paginationField
                      org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_25 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                      _jspx_th_ctl_paginationField_25.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_paginationField_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_25);
                      _jspx_th_ctl_paginationField_25.setFieldCode(PaginationField.PAGE_SIZE);
                      int _jspx_eval_ctl_paginationField_25 = _jspx_th_ctl_paginationField_25.doStartTag();
                      if (_jspx_th_ctl_paginationField_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_25);
                      out.write("\n                      most recent shown;\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_32 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_32.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_25);
                      _jspx_th_ctl_a_32.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_32 = _jspx_th_ctl_a_32.doStartTag();
                      if (_jspx_eval_ctl_a_32 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_32 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_32.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_32.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_25(_jspx_th_ctl_a_32, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_31 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_31.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_31.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_32);
                          _jspx_th_ctl_linkParam_31.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_31.setValue(String.valueOf(
                            SampleWorkflowBL.RETRACTED_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_31 = _jspx_th_ctl_linkParam_31.doStartTag();
                          if (_jspx_th_ctl_linkParam_31.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_31);
                          out.write("\n                        see all\n                        ");
                          //  ctl:paginationField
                          org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_26 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                          _jspx_th_ctl_paginationField_26.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_paginationField_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_32);
                          _jspx_th_ctl_paginationField_26.setFieldCode(
                            PaginationField.TOTAL_ELEMENT_COUNT);
                          int _jspx_eval_ctl_paginationField_26 = _jspx_th_ctl_paginationField_26.doStartTag();
                          if (_jspx_th_ctl_paginationField_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_26);
                          int evalDoAfterBody = _jspx_th_ctl_a_32.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_32 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_32);
                      out.write(")\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_25.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_25 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_25);
                  out.write("\n                    ");
                  //  ctl:paginationChecker
                  org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_26 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
                  _jspx_th_ctl_paginationChecker_26.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationChecker_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_summarySearch_13);
                  _jspx_th_ctl_paginationChecker_26.setRequirePageCountNoMoreThan(1);
                  int _jspx_eval_ctl_paginationChecker_26 = _jspx_th_ctl_paginationChecker_26.doStartTag();
                  if (_jspx_eval_ctl_paginationChecker_26 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_paginationChecker_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_paginationChecker_26.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_paginationChecker_26.doInitBody();
                    }
                    do {
                      out.write("\n                      ");
                      //  ctl:a
                      org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_33 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                      _jspx_th_ctl_a_33.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_a_33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_26);
                      _jspx_th_ctl_a_33.setHref("/search.jsp");
                      int _jspx_eval_ctl_a_33 = _jspx_th_ctl_a_33.doStartTag();
                      if (_jspx_eval_ctl_a_33 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_a_33 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_a_33.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_a_33.doInitBody();
                        }
                        do {
                          out.write("\n                        ");
                          if (_jspx_meth_rn_labParam_26(_jspx_th_ctl_a_33, _jspx_page_context))
                            return;
                          out.write("\n                        ");
                          //  ctl:linkParam
                          org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_32 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
                          _jspx_th_ctl_linkParam_32.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_linkParam_32.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_33);
                          _jspx_th_ctl_linkParam_32.setName("restrictToStatus");
                          _jspx_th_ctl_linkParam_32.setValue(String.valueOf(
                            SampleWorkflowBL.RETRACTED_PUBLIC_STATUS));
                          int _jspx_eval_ctl_linkParam_32 = _jspx_th_ctl_linkParam_32.doStartTag();
                          if (_jspx_th_ctl_linkParam_32.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_32);
                          out.write("\n                        (more detailed listing)\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_a_33.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_a_33 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_a_33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_33);
                      out.write("\n                    ");
                      int evalDoAfterBody = _jspx_th_ctl_paginationChecker_26.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_paginationChecker_26 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_paginationChecker_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationChecker_requirePageCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_26);
                  out.write("\n                  </font>\n                </td>\n              </tr>\n            ");
                  int evalDoAfterBody = _jspx_th_rn_summarySearch_13.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_summarySearch_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_summarySearch_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_summarySearch_statusCode.reuse(_jspx_th_rn_summarySearch_13);
              out.write("\n          </table>\n        </td>\n      </tr>\n      <tr>\n        <td align=\"left\" colspan=\"4\">\n          <hr  align=\"left\" width=\"300\" color=\"#4A6695\" />\n          <font class=\"light\">Limit samples per finished or public status to:\n            ");
              if (_jspx_meth_ctl_a_34(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n            - \n            ");
              if (_jspx_meth_ctl_a_35(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n            - \n            ");
              if (_jspx_meth_ctl_a_36(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n            - \n            ");
              if (_jspx_meth_ctl_a_37(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n            - \n            ");
              if (_jspx_meth_ctl_a_38(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n          </font>\n        </td>\n      </tr>\n    </table>\n  ");
              int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_1);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_page_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_page_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_page_title.reuse(_jspx_th_rn_page_0);
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_rn_relativeDayField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:relativeDayField
    org.recipnet.site.content.rncontrols.RelativeDayField _jspx_th_rn_relativeDayField_0 = (org.recipnet.site.content.rncontrols.RelativeDayField) _jspx_tagPool_rn_relativeDayField_displayRelativeDayWhenSampleWasModified_nobody.get(org.recipnet.site.content.rncontrols.RelativeDayField.class);
    _jspx_th_rn_relativeDayField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_relativeDayField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    _jspx_th_rn_relativeDayField_0.setDisplayRelativeDayWhenSampleWasModified(true);
    int _jspx_eval_rn_relativeDayField_0 = _jspx_th_rn_relativeDayField_0.doStartTag();
    if (_jspx_th_rn_relativeDayField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_relativeDayField_displayRelativeDayWhenSampleWasModified_nobody.reuse(_jspx_th_rn_relativeDayField_0);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_0 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_labParam_0.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_0 = _jspx_th_rn_labParam_0.doStartTag();
    if (_jspx_th_rn_labParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_relativeDayField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:relativeDayField
    org.recipnet.site.content.rncontrols.RelativeDayField _jspx_th_rn_relativeDayField_1 = (org.recipnet.site.content.rncontrols.RelativeDayField) _jspx_tagPool_rn_relativeDayField_displaySampleDaysPreference_nobody.get(org.recipnet.site.content.rncontrols.RelativeDayField.class);
    _jspx_th_rn_relativeDayField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_relativeDayField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_relativeDayField_1.setDisplaySampleDaysPreference(true);
    int _jspx_eval_rn_relativeDayField_1 = _jspx_th_rn_relativeDayField_1.doStartTag();
    if (_jspx_th_rn_relativeDayField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_relativeDayField_displaySampleDaysPreference_nobody.reuse(_jspx_th_rn_relativeDayField_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_1 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_1.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_1 = _jspx_th_ctl_a_1.doStartTag();
    if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_1.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_0(_jspx_th_ctl_a_1, _jspx_page_context))
          return true;
        out.write("\n              1\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_1);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_0 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_1);
    _jspx_th_ctl_linkParam_0.setName("daysToShow");
    _jspx_th_ctl_linkParam_0.setValue("1");
    int _jspx_eval_ctl_linkParam_0 = _jspx_th_ctl_linkParam_0.doStartTag();
    if (_jspx_th_ctl_linkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_2 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_2.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_2 = _jspx_th_ctl_a_2.doStartTag();
    if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_2.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_1(_jspx_th_ctl_a_2, _jspx_page_context))
          return true;
        out.write("\n              3\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_2);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_1 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_2);
    _jspx_th_ctl_linkParam_1.setName("daysToShow");
    _jspx_th_ctl_linkParam_1.setValue("3");
    int _jspx_eval_ctl_linkParam_1 = _jspx_th_ctl_linkParam_1.doStartTag();
    if (_jspx_th_ctl_linkParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_1);
    return false;
  }

  private boolean _jspx_meth_ctl_a_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_3 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_3.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_3 = _jspx_th_ctl_a_3.doStartTag();
    if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_3.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_2(_jspx_th_ctl_a_3, _jspx_page_context))
          return true;
        out.write("\n              7\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_3);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_2 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_3);
    _jspx_th_ctl_linkParam_2.setName("daysToShow");
    _jspx_th_ctl_linkParam_2.setValue("7");
    int _jspx_eval_ctl_linkParam_2 = _jspx_th_ctl_linkParam_2.doStartTag();
    if (_jspx_th_ctl_linkParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_2);
    return false;
  }

  private boolean _jspx_meth_ctl_a_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_4 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_4.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_4 = _jspx_th_ctl_a_4.doStartTag();
    if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_4.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_3(_jspx_th_ctl_a_4, _jspx_page_context))
          return true;
        out.write("\n              14\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_4);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_3 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_4);
    _jspx_th_ctl_linkParam_3.setName("daysToShow");
    _jspx_th_ctl_linkParam_3.setValue("14");
    int _jspx_eval_ctl_linkParam_3 = _jspx_th_ctl_linkParam_3.doStartTag();
    if (_jspx_th_ctl_linkParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_3);
    return false;
  }

  private boolean _jspx_meth_ctl_a_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_5 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_5.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_5 = _jspx_th_ctl_a_5.doStartTag();
    if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_5.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_4(_jspx_th_ctl_a_5, _jspx_page_context))
          return true;
        out.write("\n              30\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_5);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_4 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_5);
    _jspx_th_ctl_linkParam_4.setName("daysToShow");
    _jspx_th_ctl_linkParam_4.setValue("30");
    int _jspx_eval_ctl_linkParam_4 = _jspx_th_ctl_linkParam_4.doStartTag();
    if (_jspx_th_ctl_linkParam_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_4);
    return false;
  }

  private boolean _jspx_meth_ctl_a_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_6 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_6.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_6 = _jspx_th_ctl_a_6.doStartTag();
    if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_6.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_5(_jspx_th_ctl_a_6, _jspx_page_context))
          return true;
        out.write("\n              180\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_6);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_5 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_6);
    _jspx_th_ctl_linkParam_5.setName("daysToShow");
    _jspx_th_ctl_linkParam_5.setValue("180");
    int _jspx_eval_ctl_linkParam_5 = _jspx_th_ctl_linkParam_5.doStartTag();
    if (_jspx_th_ctl_linkParam_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_5);
    return false;
  }

  private boolean _jspx_meth_ctl_a_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_7 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_7.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_7 = _jspx_th_ctl_a_7.doStartTag();
    if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_7.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_6(_jspx_th_ctl_a_7, _jspx_page_context))
          return true;
        out.write("\n              365\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_7);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_6 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_7);
    _jspx_th_ctl_linkParam_6.setName("daysToShow");
    _jspx_th_ctl_linkParam_6.setValue("365");
    int _jspx_eval_ctl_linkParam_6 = _jspx_th_ctl_linkParam_6.doStartTag();
    if (_jspx_th_ctl_linkParam_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_6);
    return false;
  }

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_1 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_8);
    _jspx_th_rn_labParam_1.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_1 = _jspx_th_rn_labParam_1.doStartTag();
    if (_jspx_th_rn_labParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_2 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_9);
    _jspx_th_rn_labParam_2.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_2 = _jspx_th_rn_labParam_2.doStartTag();
    if (_jspx_th_rn_labParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_2);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_3 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_10);
    _jspx_th_rn_labParam_3.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_3 = _jspx_th_rn_labParam_3.doStartTag();
    if (_jspx_th_rn_labParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_3);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_4 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_11);
    _jspx_th_rn_labParam_4.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_4 = _jspx_th_rn_labParam_4.doStartTag();
    if (_jspx_th_rn_labParam_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_4);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_5 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_12);
    _jspx_th_rn_labParam_5.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_5 = _jspx_th_rn_labParam_5.doStartTag();
    if (_jspx_th_rn_labParam_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_5);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_6 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_13);
    _jspx_th_rn_labParam_6.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_6 = _jspx_th_rn_labParam_6.doStartTag();
    if (_jspx_th_rn_labParam_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_6);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_7(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_14, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_7 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_7.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_14);
    _jspx_th_rn_labParam_7.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_7 = _jspx_th_rn_labParam_7.doStartTag();
    if (_jspx_th_rn_labParam_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_7);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_8(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_8 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_8.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_15);
    _jspx_th_rn_labParam_8.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_8 = _jspx_th_rn_labParam_8.doStartTag();
    if (_jspx_th_rn_labParam_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_8);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_9(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_9 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_9.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_16);
    _jspx_th_rn_labParam_9.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_9 = _jspx_th_rn_labParam_9.doStartTag();
    if (_jspx_th_rn_labParam_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_9);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_10(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_10 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_10.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_17);
    _jspx_th_rn_labParam_10.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_10 = _jspx_th_rn_labParam_10.doStartTag();
    if (_jspx_th_rn_labParam_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_10);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_11(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_11 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_11.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_18);
    _jspx_th_rn_labParam_11.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_11 = _jspx_th_rn_labParam_11.doStartTag();
    if (_jspx_th_rn_labParam_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_11);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_12(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_19, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_12 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_12.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_19);
    _jspx_th_rn_labParam_12.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_12 = _jspx_th_rn_labParam_12.doStartTag();
    if (_jspx_th_rn_labParam_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_12);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_13(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_13 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_13.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_20);
    _jspx_th_rn_labParam_13.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_13 = _jspx_th_rn_labParam_13.doStartTag();
    if (_jspx_th_rn_labParam_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_13);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_14(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_21, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_14 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_14.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_21);
    _jspx_th_rn_labParam_14.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_14 = _jspx_th_rn_labParam_14.doStartTag();
    if (_jspx_th_rn_labParam_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_14);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_15(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_22, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_15 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_15.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_22);
    _jspx_th_rn_labParam_15.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_15 = _jspx_th_rn_labParam_15.doStartTag();
    if (_jspx_th_rn_labParam_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_15);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_16(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_23, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_16 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_16.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_23);
    _jspx_th_rn_labParam_16.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_16 = _jspx_th_rn_labParam_16.doStartTag();
    if (_jspx_th_rn_labParam_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_16);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_17(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_24, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_17 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_17.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_24);
    _jspx_th_rn_labParam_17.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_17 = _jspx_th_rn_labParam_17.doStartTag();
    if (_jspx_th_rn_labParam_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_17);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_18(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_25, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_18 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_18.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_25);
    _jspx_th_rn_labParam_18.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_18 = _jspx_th_rn_labParam_18.doStartTag();
    if (_jspx_th_rn_labParam_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_18);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_19(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_26, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_19 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_19.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_26);
    _jspx_th_rn_labParam_19.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_19 = _jspx_th_rn_labParam_19.doStartTag();
    if (_jspx_th_rn_labParam_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_19);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_20(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_27, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_20 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_20.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_27);
    _jspx_th_rn_labParam_20.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_20 = _jspx_th_rn_labParam_20.doStartTag();
    if (_jspx_th_rn_labParam_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_20);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_21(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_28, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_21 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_21.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_28);
    _jspx_th_rn_labParam_21.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_21 = _jspx_th_rn_labParam_21.doStartTag();
    if (_jspx_th_rn_labParam_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_21);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_22(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_29, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_22 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_22.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_29);
    _jspx_th_rn_labParam_22.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_22 = _jspx_th_rn_labParam_22.doStartTag();
    if (_jspx_th_rn_labParam_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_22);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_23(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_30, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_23 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_23.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_30);
    _jspx_th_rn_labParam_23.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_23 = _jspx_th_rn_labParam_23.doStartTag();
    if (_jspx_th_rn_labParam_23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_23);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_24(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_31, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_24 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_24.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_31);
    _jspx_th_rn_labParam_24.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_24 = _jspx_th_rn_labParam_24.doStartTag();
    if (_jspx_th_rn_labParam_24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_24);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_25(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_32, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_25 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_25.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_32);
    _jspx_th_rn_labParam_25.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_25 = _jspx_th_rn_labParam_25.doStartTag();
    if (_jspx_th_rn_labParam_25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_25);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_26(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_33, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_26 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_26.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_33);
    _jspx_th_rn_labParam_26.setName("quickSummarySearchByLab");
    int _jspx_eval_rn_labParam_26 = _jspx_th_rn_labParam_26.doStartTag();
    if (_jspx_th_rn_labParam_26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_26);
    return false;
  }

  private boolean _jspx_meth_ctl_a_34(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_34 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_34.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_34.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_34 = _jspx_th_ctl_a_34.doStartTag();
    if (_jspx_eval_ctl_a_34 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_34 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_34.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_34.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_33(_jspx_th_ctl_a_34, _jspx_page_context))
          return true;
        out.write("\n              5\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_34.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_34 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_34);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_33(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_34, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_33 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_33.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_33.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_34);
    _jspx_th_ctl_linkParam_33.setName("oldSampleCount");
    _jspx_th_ctl_linkParam_33.setValue("5");
    int _jspx_eval_ctl_linkParam_33 = _jspx_th_ctl_linkParam_33.doStartTag();
    if (_jspx_th_ctl_linkParam_33.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_33);
    return false;
  }

  private boolean _jspx_meth_ctl_a_35(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_35 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_35.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_35.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_35 = _jspx_th_ctl_a_35.doStartTag();
    if (_jspx_eval_ctl_a_35 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_35 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_35.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_35.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_34(_jspx_th_ctl_a_35, _jspx_page_context))
          return true;
        out.write("\n              10\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_35.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_35 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_35);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_34(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_35, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_34 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_34.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_34.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_35);
    _jspx_th_ctl_linkParam_34.setName("oldSampleCount");
    _jspx_th_ctl_linkParam_34.setValue("10");
    int _jspx_eval_ctl_linkParam_34 = _jspx_th_ctl_linkParam_34.doStartTag();
    if (_jspx_th_ctl_linkParam_34.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_34);
    return false;
  }

  private boolean _jspx_meth_ctl_a_36(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_36 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_36.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_36.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_36 = _jspx_th_ctl_a_36.doStartTag();
    if (_jspx_eval_ctl_a_36 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_36 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_36.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_36.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_35(_jspx_th_ctl_a_36, _jspx_page_context))
          return true;
        out.write("\n              15\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_36.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_36 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_36);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_35(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_36, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_35 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_35.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_35.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_36);
    _jspx_th_ctl_linkParam_35.setName("oldSampleCount");
    _jspx_th_ctl_linkParam_35.setValue("15");
    int _jspx_eval_ctl_linkParam_35 = _jspx_th_ctl_linkParam_35.doStartTag();
    if (_jspx_th_ctl_linkParam_35.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_35);
    return false;
  }

  private boolean _jspx_meth_ctl_a_37(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_37 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_37.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_37.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_37 = _jspx_th_ctl_a_37.doStartTag();
    if (_jspx_eval_ctl_a_37 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_37 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_37.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_37.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_36(_jspx_th_ctl_a_37, _jspx_page_context))
          return true;
        out.write("\n              20\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_37.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_37 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_37);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_36(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_37, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_36 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_36.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_36.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_37);
    _jspx_th_ctl_linkParam_36.setName("oldSampleCount");
    _jspx_th_ctl_linkParam_36.setValue("20");
    int _jspx_eval_ctl_linkParam_36 = _jspx_th_ctl_linkParam_36.doStartTag();
    if (_jspx_th_ctl_linkParam_36.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_36);
    return false;
  }

  private boolean _jspx_meth_ctl_a_38(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_38 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_38.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_38.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_a_38.setHref("/lab/summary.jsp");
    int _jspx_eval_ctl_a_38 = _jspx_th_ctl_a_38.doStartTag();
    if (_jspx_eval_ctl_a_38 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_38 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_38.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_38.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_linkParam_37(_jspx_th_ctl_a_38, _jspx_page_context))
          return true;
        out.write("\n              25\n            ");
        int evalDoAfterBody = _jspx_th_ctl_a_38.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_38 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_38.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_38);
    return false;
  }

  private boolean _jspx_meth_ctl_linkParam_37(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_38, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:linkParam
    org.recipnet.common.controls.LinkParam _jspx_th_ctl_linkParam_37 = (org.recipnet.common.controls.LinkParam) _jspx_tagPool_ctl_linkParam_value_name_nobody.get(org.recipnet.common.controls.LinkParam.class);
    _jspx_th_ctl_linkParam_37.setPageContext(_jspx_page_context);
    _jspx_th_ctl_linkParam_37.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_38);
    _jspx_th_ctl_linkParam_37.setName("oldSampleCount");
    _jspx_th_ctl_linkParam_37.setValue("25");
    int _jspx_eval_ctl_linkParam_37 = _jspx_th_ctl_linkParam_37.doStartTag();
    if (_jspx_th_ctl_linkParam_37.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_linkParam_value_name_nobody.reuse(_jspx_th_ctl_linkParam_37);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    font.dark { font-family: Arial, Helvetica, Verdana; font-weight: bold; \n            color: #000050; }\n    font.light { font-family: Arial, Helvetica, Verdana; font-style: narrow; \n            color: #A0A0A0; }\n    font.normal { font-family: Arial, Helvetica, Verdana; font-style: normal; }\n    table.summaryBox { width: 100%; border: 3px solid #DAE8F1; }\n    table.summaryBoxStopped { width: 100%; border: 3px solid #F1E8DA; }\n    th.summaryHeader { background-color: #DAE8F1; padding: 5px; } \n    th.summaryHeaderStopped { background-color: #F1E8DA; padding: 5px; } \n    td.summaryBody { height: 100px; text-align: center; }\n  ");
        int evalDoAfterBody = _jspx_th_ctl_styleBlock_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_styleBlock_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_styleBlock.reuse(_jspx_th_ctl_styleBlock_0);
    return false;
  }
}
