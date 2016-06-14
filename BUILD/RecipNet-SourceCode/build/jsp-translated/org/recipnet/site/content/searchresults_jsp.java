package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.PaginationField;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.SampleTextIterator;
import org.recipnet.site.content.rncontrols.SearchResultsContext;
import org.recipnet.site.content.rncontrols.SearchResultsIterator;
import org.recipnet.site.content.rncontrols.SearchResultsPage;
import org.recipnet.site.content.rncontrols.WapPage;

public final class searchresults_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchResultsPage_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationChecker_requireElementCountNoMoreThan;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_repeatSearchParam_searchPagePath_searchIdParamName_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToSingleSearchResult_jumpSitePageUrl_editSamplePageUrl_detailedPageUrl_basicPageUrl_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchResultsIterator_usePaginationContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_searchResultIndex_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_nameAndFormulaIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleNameIterator_excludePreferredName_excludeIUPACNames;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_matchedFormulaIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_disabled_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_paginationChecker_requireElementCountNoLessThan;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm_pageForm_method;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_hiddenInt_initialValueFrom_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_searchResultsPage_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationChecker_requireElementCountNoMoreThan = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_repeatSearchParam_searchPagePath_searchIdParamName_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToSingleSearchResult_jumpSitePageUrl_editSamplePageUrl_detailedPageUrl_basicPageUrl_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchResultsIterator_usePaginationContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_searchResultIndex_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_nameAndFormulaIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleNameIterator_excludePreferredName_excludeIUPACNames = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_matchedFormulaIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_disabled_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_paginationChecker_requireElementCountNoLessThan = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm_pageForm_method = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_hiddenInt_initialValueFrom_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_searchResultsPage_title.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_redirect_target_nobody.release();
    _jspx_tagPool_ctl_paginationField_nobody.release();
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.release();
    _jspx_tagPool_ctl_paginationField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_paginationChecker_requireElementCountNoMoreThan.release();
    _jspx_tagPool_rn_authorizationChecker_invert.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_rn_repeatSearchParam_searchPagePath_searchIdParamName_name_nobody.release();
    _jspx_tagPool_rn_redirectToSingleSearchResult_jumpSitePageUrl_editSamplePageUrl_detailedPageUrl_basicPageUrl_nobody.release();
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.release();
    _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.release();
    _jspx_tagPool_rn_searchResultIndex_nobody.release();
    _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_nameAndFormulaIterator_id.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.release();
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_rn_sampleNameIterator_excludePreferredName_excludeIUPACNames.release();
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_matchedFormulaIterator.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.release();
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_disabled_nobody.release();
    _jspx_tagPool_ctl_paginationChecker_requireElementCountNoLessThan.release();
    _jspx_tagPool_ctl_selfForm_pageForm_method.release();
    _jspx_tagPool_ctl_hiddenInt_initialValueFrom_id_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n\n");
      //  rn:searchResultsPage
      org.recipnet.site.content.rncontrols.SearchResultsPage _jspx_th_rn_searchResultsPage_0 = (org.recipnet.site.content.rncontrols.SearchResultsPage) _jspx_tagPool_rn_searchResultsPage_title.get(org.recipnet.site.content.rncontrols.SearchResultsPage.class);
      _jspx_th_rn_searchResultsPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_searchResultsPage_0.setParent(null);
      _jspx_th_rn_searchResultsPage_0.setTitle("Search Results");
      int _jspx_eval_rn_searchResultsPage_0 = _jspx_th_rn_searchResultsPage_0.doStartTag();
      if (_jspx_eval_rn_searchResultsPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.SearchResultsPage htmlPage = null;
        if (_jspx_eval_rn_searchResultsPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_searchResultsPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_searchResultsPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.SearchResultsPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(
      SearchResultsPage.UNKNOWN_SEARCH_ID);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("\n    ");
              if (_jspx_meth_ctl_redirect_0(_jspx_th_ctl_errorMessage_0, _jspx_page_context))
                return;
              out.write('\n');
              out.write(' ');
              out.write(' ');
              int evalDoAfterBody = _jspx_th_ctl_errorMessage_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_errorMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_0);
          out.write("\n  <p>\n    Found <font color=\"red\"><b>");
          if (_jspx_meth_ctl_paginationField_0(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("</b></font> samples.\n    ");
          //  ctl:paginationChecker
          org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_0 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
          _jspx_th_ctl_paginationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_paginationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
          _jspx_th_ctl_paginationChecker_0.setRequirePageCountNoLessThan(1);
          int _jspx_eval_ctl_paginationChecker_0 = _jspx_th_ctl_paginationChecker_0.doStartTag();
          if (_jspx_eval_ctl_paginationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_paginationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_paginationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_paginationChecker_0.doInitBody();
            }
            do {
              out.write("\n      &nbsp;&nbsp;Page ");
              //  ctl:paginationField
              org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_1 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
              _jspx_th_ctl_paginationField_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_paginationField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_0);
              _jspx_th_ctl_paginationField_1.setFieldCode(
          PaginationField.CURRENT_PAGE_NUMBER);
              int _jspx_eval_ctl_paginationField_1 = _jspx_th_ctl_paginationField_1.doStartTag();
              if (_jspx_th_ctl_paginationField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_1);
              out.write(" of\n      ");
              //  ctl:paginationField
              org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_2 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
              _jspx_th_ctl_paginationField_2.setPageContext(_jspx_page_context);
              _jspx_th_ctl_paginationField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_0);
              _jspx_th_ctl_paginationField_2.setFieldCode(PaginationField.TOTAL_PAGE_COUNT);
              int _jspx_eval_ctl_paginationField_2 = _jspx_th_ctl_paginationField_2.doStartTag();
              if (_jspx_th_ctl_paginationField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_2);
              out.write(".\n    ");
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
          out.write("\n  </p>\n  ");
          if (_jspx_meth_ctl_paginationChecker_1(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_redirectToSingleSearchResult_0(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("\n\n  ");
          if (_jspx_meth_ctl_pageLink_0(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write(" &nbsp;  &nbsp;  &nbsp;  &nbsp;  &nbsp;\n\n  ");
          if (_jspx_meth_ctl_pageLink_1(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("\n\n  <table cellspacing=\"15\" border=\"0\">\n    ");
          //  rn:searchResultsIterator
          org.recipnet.site.content.rncontrols.SearchResultsIterator _jspx_th_rn_searchResultsIterator_0 = (org.recipnet.site.content.rncontrols.SearchResultsIterator) _jspx_tagPool_rn_searchResultsIterator_usePaginationContext.get(org.recipnet.site.content.rncontrols.SearchResultsIterator.class);
          _jspx_th_rn_searchResultsIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_searchResultsIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
          _jspx_th_rn_searchResultsIterator_0.setUsePaginationContext(true);
          int _jspx_eval_rn_searchResultsIterator_0 = _jspx_th_rn_searchResultsIterator_0.doStartTag();
          if (_jspx_eval_rn_searchResultsIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_searchResultsIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_searchResultsIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_searchResultsIterator_0.doInitBody();
            }
            do {
              out.write("\n      <tr>\n        <td align=\"right\" valign=\"top\">\n          ");
              if (_jspx_meth_rn_searchResultIndex_0(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                return;
              out.write(".\n        </td>\n        <td align=\"left\" valign=\"top\" width=\"350\">\n          ");
              //  rn:showsampleLink
              org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
              _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
              _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
              _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
              _jspx_th_rn_showsampleLink_0.setJumpSitePageUrl("/showsamplejumpsite.jsp");
              int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
              if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_showsampleLink_0.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  if (_jspx_meth_rn_sampleHistoryParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
                    return;
                  out.write("\n            ");
                  //  rn:labField
                  org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
                  _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
                  _jspx_th_rn_labField_0.setFieldCode(LabField.SHORT_NAME);
                  int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
                  if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_0);
                  out.write("\n            #");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
                  _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
                  int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
                  if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_showsampleLink_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_showsampleLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_showsampleLink_jumpSitePageUrl_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_0);
              out.write("\n          ");
              //  rn:nameAndFormulaIterator
              org.recipnet.site.content.rncontrols.NameAndFormulaIterator nameAndFormula = null;
              org.recipnet.site.content.rncontrols.NameAndFormulaIterator _jspx_th_rn_nameAndFormulaIterator_0 = (org.recipnet.site.content.rncontrols.NameAndFormulaIterator) _jspx_tagPool_rn_nameAndFormulaIterator_id.get(org.recipnet.site.content.rncontrols.NameAndFormulaIterator.class);
              _jspx_th_rn_nameAndFormulaIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_nameAndFormulaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
              _jspx_th_rn_nameAndFormulaIterator_0.setId("nameAndFormula");
              int _jspx_eval_rn_nameAndFormulaIterator_0 = _jspx_th_rn_nameAndFormulaIterator_0.doStartTag();
              if (_jspx_eval_rn_nameAndFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_nameAndFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_nameAndFormulaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_nameAndFormulaIterator_0.doInitBody();
                }
                nameAndFormula = (org.recipnet.site.content.rncontrols.NameAndFormulaIterator) _jspx_page_context.findAttribute("nameAndFormula");
                do {
                  if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_nameAndFormulaIterator_0, _jspx_page_context))
                    return;
                  if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_nameAndFormulaIterator_0, _jspx_page_context))
                    return;
                  if (_jspx_meth_rn_sampleField_1(_jspx_th_rn_nameAndFormulaIterator_0, _jspx_page_context))
                    return;
                  if (_jspx_meth_ctl_parityChecker_2(_jspx_th_rn_nameAndFormulaIterator_0, _jspx_page_context))
                    return;
                  int evalDoAfterBody = _jspx_th_rn_nameAndFormulaIterator_0.doAfterBody();
                  nameAndFormula = (org.recipnet.site.content.rncontrols.NameAndFormulaIterator) _jspx_page_context.findAttribute("nameAndFormula");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_nameAndFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_nameAndFormulaIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              nameAndFormula = (org.recipnet.site.content.rncontrols.NameAndFormulaIterator) _jspx_page_context.findAttribute("nameAndFormula");
              _jspx_tagPool_rn_nameAndFormulaIterator_id.reuse(_jspx_th_rn_nameAndFormulaIterator_0);
              out.write("\n          [");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
              _jspx_th_rn_sampleField_2.setFieldCode(SampleInfo.STATUS);
              _jspx_th_rn_sampleField_2.setDisplayValueOnly(true);
              int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
              if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_2);
              out.write("]\n          ");
              if (_jspx_meth_rn_sampleNameIterator_0(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                return;
              out.write("\n          ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
              _jspx_th_rn_sampleChecker_0.setIncludeIfValueIsPresent(
              SampleTextBL.STRUCTURAL_FORMULA);
              int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
              if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_0.doInitBody();
                }
                do {
                  out.write("\n            <br />\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
                  _jspx_th_rn_sampleFieldLabel_0.setFieldCode(
                SampleTextBL.STRUCTURAL_FORMULA);
                  int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
                  out.write(":\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
                  _jspx_th_rn_sampleField_4.setFieldCode(
                SampleTextBL.STRUCTURAL_FORMULA);
                  _jspx_th_rn_sampleField_4.setDisplayAsLabel(true);
                  _jspx_th_rn_sampleField_4.setFormatFieldForSearchResults(true);
                  _jspx_th_rn_sampleField_4.setStyleClassForSearchMatch("match");
                  int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
                  if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_4);
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.reuse(_jspx_th_rn_sampleChecker_0);
              out.write("\n          ");
              if (_jspx_meth_rn_matchedFormulaIterator_0(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                return;
              out.write("\n          <br />Provider: ");
              if (_jspx_meth_rn_providerField_0(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                return;
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(
              SearchResultsIterator.IS_MORE_RECENT_THAN_SEARCH);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n          <span class=\"warningMessage\">\n            <br />\n            This sample has been updated recently and might not match the\n            search criteria any longer.\n          </span>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
              out.write("\n        </td>\n        <td>\n          ");
              if (_jspx_meth_rn_authorizationChecker_1(_jspx_th_rn_searchResultsIterator_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n    ");
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
          out.write("\n  </table>\n\n  ");
          if (_jspx_meth_ctl_pageLink_2(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_pageLink_3(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_pageLink_4(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_pageLink_5(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_paginationChecker_2(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_pageLink_7(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_pageLink_8(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_pageLink_9(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("&nbsp;&nbsp;\n  ");
          if (_jspx_meth_ctl_pageLink_10(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write("\n  <br />\n  <br />\n  ");
          //  ctl:paginationChecker
          org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_3 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requireElementCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
          _jspx_th_ctl_paginationChecker_3.setPageContext(_jspx_page_context);
          _jspx_th_ctl_paginationChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
          _jspx_th_ctl_paginationChecker_3.setRequireElementCountNoLessThan(6);
          int _jspx_eval_ctl_paginationChecker_3 = _jspx_th_ctl_paginationChecker_3.doStartTag();
          if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_paginationChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_paginationChecker_3.doInitBody();
            }
            do {
              out.write("\n    ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm_pageForm_method.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_3);
              _jspx_th_ctl_selfForm_0.setMethod("GET");
              _jspx_th_ctl_selfForm_0.setPageForm(false);
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                do {
                  out.write("\n      Results per page: ");
                  //  ctl:paginationField
                  org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_3 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_fieldCode_nobody.get(org.recipnet.common.controls.PaginationField.class);
                  _jspx_th_ctl_paginationField_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_paginationField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_paginationField_3.setFieldCode(PaginationField.RESIZE_PAGE_SELECTOR);
                  int _jspx_eval_ctl_paginationField_3 = _jspx_th_ctl_paginationField_3.doStartTag();
                  if (_jspx_th_ctl_paginationField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_paginationField_fieldCode_nobody.reuse(_jspx_th_ctl_paginationField_3);
                  out.write("\n      ");
                  //  ctl:hiddenInt
                  org.recipnet.common.controls.HiddenIntHtmlControl searchId = null;
                  org.recipnet.common.controls.HiddenIntHtmlControl _jspx_th_ctl_hiddenInt_0 = (org.recipnet.common.controls.HiddenIntHtmlControl) _jspx_tagPool_ctl_hiddenInt_initialValueFrom_id_nobody.get(org.recipnet.common.controls.HiddenIntHtmlControl.class);
                  _jspx_th_ctl_hiddenInt_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_hiddenInt_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_hiddenInt_0.setId("searchId");
                  _jspx_th_ctl_hiddenInt_0.setInitialValueFrom("searchId");
                  int _jspx_eval_ctl_hiddenInt_0 = _jspx_th_ctl_hiddenInt_0.doStartTag();
                  if (_jspx_th_ctl_hiddenInt_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  searchId = (org.recipnet.common.controls.HiddenIntHtmlControl) _jspx_page_context.findAttribute("searchId");
                  _jspx_tagPool_ctl_hiddenInt_initialValueFrom_id_nobody.reuse(_jspx_th_ctl_hiddenInt_0);
                  out.write("\n      <input type=\"submit\" value=\"Resize\" />\n    ");
                  int evalDoAfterBody = _jspx_th_ctl_selfForm_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_selfForm_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_selfForm_pageForm_method.reuse(_jspx_th_ctl_selfForm_0);
              out.write('\n');
              out.write(' ');
              out.write(' ');
              int evalDoAfterBody = _jspx_th_ctl_paginationChecker_3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_paginationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_paginationChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_paginationChecker_requireElementCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_3);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_searchResultsPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_searchResultsPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.SearchResultsPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_searchResultsPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_searchResultsPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_searchResultsPage_title.reuse(_jspx_th_rn_searchResultsPage_0);
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

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_0);
    _jspx_th_ctl_redirect_0.setTarget("/searchexpired.jsp");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_ctl_paginationField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:paginationField
    org.recipnet.common.controls.PaginationField _jspx_th_ctl_paginationField_0 = (org.recipnet.common.controls.PaginationField) _jspx_tagPool_ctl_paginationField_nobody.get(org.recipnet.common.controls.PaginationField.class);
    _jspx_th_ctl_paginationField_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_paginationField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    int _jspx_eval_ctl_paginationField_0 = _jspx_th_ctl_paginationField_0.doStartTag();
    if (_jspx_th_ctl_paginationField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_paginationField_nobody.reuse(_jspx_th_ctl_paginationField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_paginationChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:paginationChecker
    org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_1 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requireElementCountNoMoreThan.get(org.recipnet.common.controls.PaginationChecker.class);
    _jspx_th_ctl_paginationChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_paginationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_paginationChecker_1.setRequireElementCountNoMoreThan(0);
    int _jspx_eval_ctl_paginationChecker_1 = _jspx_th_ctl_paginationChecker_1.doStartTag();
    if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_paginationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_paginationChecker_1.doInitBody();
      }
      do {
        out.write("\n    ");
        if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_ctl_paginationChecker_1, _jspx_page_context))
          return true;
        out.write('\n');
        out.write(' ');
        out.write(' ');
        int evalDoAfterBody = _jspx_th_ctl_paginationChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_paginationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_paginationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_paginationChecker_requireElementCountNoMoreThan.reuse(_jspx_th_ctl_paginationChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_paginationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_1);
    _jspx_th_rn_authorizationChecker_0.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n      Additional results may become visible after you\n      ");
        if (_jspx_meth_ctl_a_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write(".\n    ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_invert.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_a_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:a
    org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
    _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_ctl_a_0.setHref("/login.jsp");
    int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_a_0.doInitBody();
      }
      do {
        out.write("log in");
        if (_jspx_meth_rn_repeatSearchParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_ctl_a_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_a_href.reuse(_jspx_th_ctl_a_0);
    return false;
  }

  private boolean _jspx_meth_rn_repeatSearchParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:repeatSearchParam
    org.recipnet.site.content.rncontrols.RepeatSearchParam _jspx_th_rn_repeatSearchParam_0 = (org.recipnet.site.content.rncontrols.RepeatSearchParam) _jspx_tagPool_rn_repeatSearchParam_searchPagePath_searchIdParamName_name_nobody.get(org.recipnet.site.content.rncontrols.RepeatSearchParam.class);
    _jspx_th_rn_repeatSearchParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_repeatSearchParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_repeatSearchParam_0.setName("origUrl");
    _jspx_th_rn_repeatSearchParam_0.setSearchIdParamName("searchId");
    _jspx_th_rn_repeatSearchParam_0.setSearchPagePath("/search.jsp");
    int _jspx_eval_rn_repeatSearchParam_0 = _jspx_th_rn_repeatSearchParam_0.doStartTag();
    if (_jspx_th_rn_repeatSearchParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_repeatSearchParam_searchPagePath_searchIdParamName_name_nobody.reuse(_jspx_th_rn_repeatSearchParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_redirectToSingleSearchResult_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:redirectToSingleSearchResult
    org.recipnet.site.content.rncontrols.RedirectToSingleSearchResult _jspx_th_rn_redirectToSingleSearchResult_0 = (org.recipnet.site.content.rncontrols.RedirectToSingleSearchResult) _jspx_tagPool_rn_redirectToSingleSearchResult_jumpSitePageUrl_editSamplePageUrl_detailedPageUrl_basicPageUrl_nobody.get(org.recipnet.site.content.rncontrols.RedirectToSingleSearchResult.class);
    _jspx_th_rn_redirectToSingleSearchResult_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_redirectToSingleSearchResult_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_rn_redirectToSingleSearchResult_0.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_redirectToSingleSearchResult_0.setDetailedPageUrl("/showsampledetailed.jsp");
    _jspx_th_rn_redirectToSingleSearchResult_0.setJumpSitePageUrl("/showsamplejumpsite.jsp");
    _jspx_th_rn_redirectToSingleSearchResult_0.setEditSamplePageUrl("/lab/sample.jsp");
    int _jspx_eval_rn_redirectToSingleSearchResult_0 = _jspx_th_rn_redirectToSingleSearchResult_0.doStartTag();
    if (_jspx_th_rn_redirectToSingleSearchResult_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_redirectToSingleSearchResult_jumpSitePageUrl_editSamplePageUrl_detailedPageUrl_basicPageUrl_nobody.reuse(_jspx_th_rn_redirectToSingleSearchResult_0);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_0 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_0.setPageOffset(-1);
    _jspx_th_ctl_pageLink_0.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_0 = _jspx_th_ctl_pageLink_0.doStartTag();
    if (_jspx_eval_ctl_pageLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_pageLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_pageLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_pageLink_0.doInitBody();
      }
      do {
        out.write("\n    &lt;&lt;&lt; Previous page\n  ");
        int evalDoAfterBody = _jspx_th_ctl_pageLink_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_pageLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_pageLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.reuse(_jspx_th_ctl_pageLink_0);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_1 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_1.setPageOffset(1);
    _jspx_th_ctl_pageLink_1.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_1 = _jspx_th_ctl_pageLink_1.doStartTag();
    if (_jspx_eval_ctl_pageLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_pageLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_pageLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_pageLink_1.doInitBody();
      }
      do {
        out.write("\n    Next page &gt;&gt;&gt;\n  ");
        int evalDoAfterBody = _jspx_th_ctl_pageLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_pageLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_pageLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.reuse(_jspx_th_ctl_pageLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_searchResultIndex_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:searchResultIndex
    org.recipnet.site.content.rncontrols.SearchResultIndex _jspx_th_rn_searchResultIndex_0 = (org.recipnet.site.content.rncontrols.SearchResultIndex) _jspx_tagPool_rn_searchResultIndex_nobody.get(org.recipnet.site.content.rncontrols.SearchResultIndex.class);
    _jspx_th_rn_searchResultIndex_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_searchResultIndex_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    int _jspx_eval_rn_searchResultIndex_0 = _jspx_th_rn_searchResultIndex_0.doStartTag();
    if (_jspx_th_rn_searchResultIndex_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_searchResultIndex_nobody.reuse(_jspx_th_rn_searchResultIndex_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_0 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleParam_0.setName("sampleId");
    int _jspx_eval_rn_sampleParam_0 = _jspx_th_rn_sampleParam_0.doStartTag();
    if (_jspx_th_rn_sampleParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_0 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleHistoryParam_0.setName("sampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_0 = _jspx_th_rn_sampleHistoryParam_0.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_nameAndFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_nameAndFormulaIterator_0);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write('(');
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_nameAndFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_nameAndFormulaIterator_0);
    _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_1.setInvert(true);
    int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
    if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_1.doInitBody();
      }
      do {
        out.write(",\n            ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_nameAndFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_nameAndFormulaIterator_0);
    _jspx_th_rn_sampleField_1.setDisplayValueOnly(true);
    _jspx_th_rn_sampleField_1.setFormatFieldForSearchResults(true);
    _jspx_th_rn_sampleField_1.setStyleClassForSearchMatch("match");
    int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
    if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_1);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_nameAndFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_2 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_nameAndFormulaIterator_0);
    _jspx_th_ctl_parityChecker_2.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_2 = _jspx_th_ctl_parityChecker_2.doStartTag();
    if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_2.doInitBody();
      }
      do {
        out.write(')');
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleNameIterator_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleNameIterator
    org.recipnet.site.content.rncontrols.SampleNameIterator _jspx_th_rn_sampleNameIterator_0 = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_tagPool_rn_sampleNameIterator_excludePreferredName_excludeIUPACNames.get(org.recipnet.site.content.rncontrols.SampleNameIterator.class);
    _jspx_th_rn_sampleNameIterator_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleNameIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    _jspx_th_rn_sampleNameIterator_0.setExcludePreferredName(true);
    _jspx_th_rn_sampleNameIterator_0.setExcludeIUPACNames(true);
    int _jspx_eval_rn_sampleNameIterator_0 = _jspx_th_rn_sampleNameIterator_0.doStartTag();
    if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleNameIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleNameIterator_0.doInitBody();
      }
      do {
        if (_jspx_meth_ctl_parityChecker_3(_jspx_th_rn_sampleNameIterator_0, _jspx_page_context))
          return true;
        if (_jspx_meth_ctl_parityChecker_4(_jspx_th_rn_sampleNameIterator_0, _jspx_page_context))
          return true;
        if (_jspx_meth_rn_sampleField_3(_jspx_th_rn_sampleNameIterator_0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_rn_sampleNameIterator_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleNameIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleNameIterator_excludePreferredName_excludeIUPACNames.reuse(_jspx_th_rn_sampleNameIterator_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_3 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_0);
    _jspx_th_ctl_parityChecker_3.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_3 = _jspx_th_ctl_parityChecker_3.doStartTag();
    if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_3.doInitBody();
      }
      do {
        out.write("\n              <br />Other names: ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_3);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_4 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_0);
    _jspx_th_ctl_parityChecker_4.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_4.setInvert(true);
    int _jspx_eval_ctl_parityChecker_4 = _jspx_th_ctl_parityChecker_4.doStartTag();
    if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_4.doInitBody();
      }
      do {
        out.write(";\n            ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_4);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_0);
    _jspx_th_rn_sampleField_3.setDisplayValueOnly(true);
    _jspx_th_rn_sampleField_3.setFormatFieldForSearchResults(true);
    _jspx_th_rn_sampleField_3.setStyleClassForSearchMatch("match");
    int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
    if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_3);
    return false;
  }

  private boolean _jspx_meth_rn_matchedFormulaIterator_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:matchedFormulaIterator
    org.recipnet.site.content.rncontrols.MatchedFormulaIterator _jspx_th_rn_matchedFormulaIterator_0 = (org.recipnet.site.content.rncontrols.MatchedFormulaIterator) _jspx_tagPool_rn_matchedFormulaIterator.get(org.recipnet.site.content.rncontrols.MatchedFormulaIterator.class);
    _jspx_th_rn_matchedFormulaIterator_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_matchedFormulaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    int _jspx_eval_rn_matchedFormulaIterator_0 = _jspx_th_rn_matchedFormulaIterator_0.doStartTag();
    if (_jspx_eval_rn_matchedFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_matchedFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_matchedFormulaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_matchedFormulaIterator_0.doInitBody();
      }
      do {
        out.write("\n            <br />\n            ");
        if (_jspx_meth_rn_sampleFieldLabel_1(_jspx_th_rn_matchedFormulaIterator_0, _jspx_page_context))
          return true;
        out.write(":\n            ");
        if (_jspx_meth_rn_sampleField_5(_jspx_th_rn_matchedFormulaIterator_0, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_rn_matchedFormulaIterator_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_matchedFormulaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_matchedFormulaIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_matchedFormulaIterator.reuse(_jspx_th_rn_matchedFormulaIterator_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_matchedFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_matchedFormulaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_matchedFormulaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_matchedFormulaIterator_0);
    _jspx_th_rn_sampleField_5.setDisplayValueOnly(true);
    _jspx_th_rn_sampleField_5.setFormatFieldForSearchResults(true);
    _jspx_th_rn_sampleField_5.setStyleClassForSearchMatch("match");
    int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
    if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_styleClassForSearchMatch_formatFieldForSearchResults_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_5);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsIterator_0);
    _jspx_th_rn_authorizationChecker_1.setCanEditSample(true);
    _jspx_th_rn_authorizationChecker_1.setSuppressRenderingOnly(true);
    int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_1.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_rn_link_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.reuse(_jspx_th_rn_authorizationChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Edit...");
        int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_0);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_2 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_2.setPageOffset(-1);
    _jspx_th_ctl_pageLink_2.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_2 = _jspx_th_ctl_pageLink_2.doStartTag();
    if (_jspx_eval_ctl_pageLink_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_pageLink_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_pageLink_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_pageLink_2.doInitBody();
      }
      do {
        out.write("\n    &lt;&lt;&lt; Previous page&nbsp;&nbsp;\n  ");
        int evalDoAfterBody = _jspx_th_ctl_pageLink_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_pageLink_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_pageLink_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.reuse(_jspx_th_ctl_pageLink_2);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_3 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_3.setPageOffset(-3);
    _jspx_th_ctl_pageLink_3.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_3.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_3 = _jspx_th_ctl_pageLink_3.doStartTag();
    if (_jspx_th_ctl_pageLink_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_3);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_4 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_4.setPageOffset(-2);
    _jspx_th_ctl_pageLink_4.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_4.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_4 = _jspx_th_ctl_pageLink_4.doStartTag();
    if (_jspx_th_ctl_pageLink_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_4);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_5 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_5.setPageOffset(-1);
    _jspx_th_ctl_pageLink_5.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_5.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_5 = _jspx_th_ctl_pageLink_5.doStartTag();
    if (_jspx_th_ctl_pageLink_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_5);
    return false;
  }

  private boolean _jspx_meth_ctl_paginationChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:paginationChecker
    org.recipnet.common.controls.PaginationChecker _jspx_th_ctl_paginationChecker_2 = (org.recipnet.common.controls.PaginationChecker) _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.get(org.recipnet.common.controls.PaginationChecker.class);
    _jspx_th_ctl_paginationChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_paginationChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_paginationChecker_2.setRequirePageCountNoLessThan(2);
    int _jspx_eval_ctl_paginationChecker_2 = _jspx_th_ctl_paginationChecker_2.doStartTag();
    if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_paginationChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_paginationChecker_2.doInitBody();
      }
      do {
        out.write("\n    <font color=\"red\">\n      ");
        if (_jspx_meth_ctl_pageLink_6(_jspx_th_ctl_paginationChecker_2, _jspx_page_context))
          return true;
        out.write("&nbsp;&nbsp;\n    </font>\n  ");
        int evalDoAfterBody = _jspx_th_ctl_paginationChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_paginationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_paginationChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_paginationChecker_requirePageCountNoLessThan.reuse(_jspx_th_ctl_paginationChecker_2);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_paginationChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_6 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_disabled_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_paginationChecker_2);
    _jspx_th_ctl_pageLink_6.setPageOffset(0);
    _jspx_th_ctl_pageLink_6.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_6.setDisabled(true);
    _jspx_th_ctl_pageLink_6.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_6 = _jspx_th_ctl_pageLink_6.doStartTag();
    if (_jspx_th_ctl_pageLink_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_disabled_nobody.reuse(_jspx_th_ctl_pageLink_6);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_7 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_7.setPageOffset(1);
    _jspx_th_ctl_pageLink_7.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_7.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_7 = _jspx_th_ctl_pageLink_7.doStartTag();
    if (_jspx_th_ctl_pageLink_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_7);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_8 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_8.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_8.setPageOffset(2);
    _jspx_th_ctl_pageLink_8.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_8.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_8 = _jspx_th_ctl_pageLink_8.doStartTag();
    if (_jspx_th_ctl_pageLink_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_8);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_9(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_9 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_9.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_9.setPageOffset(3);
    _jspx_th_ctl_pageLink_9.setIncludePageNumber(true);
    _jspx_th_ctl_pageLink_9.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_9 = _jspx_th_ctl_pageLink_9.doStartTag();
    if (_jspx_th_ctl_pageLink_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset_includePageNumber_nobody.reuse(_jspx_th_ctl_pageLink_9);
    return false;
  }

  private boolean _jspx_meth_ctl_pageLink_10(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:pageLink
    org.recipnet.common.controls.PaginationLink _jspx_th_ctl_pageLink_10 = (org.recipnet.common.controls.PaginationLink) _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.get(org.recipnet.common.controls.PaginationLink.class);
    _jspx_th_ctl_pageLink_10.setPageContext(_jspx_page_context);
    _jspx_th_ctl_pageLink_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    _jspx_th_ctl_pageLink_10.setPageOffset(1);
    _jspx_th_ctl_pageLink_10.setPreserveParam("searchId");
    int _jspx_eval_ctl_pageLink_10 = _jspx_th_ctl_pageLink_10.doStartTag();
    if (_jspx_eval_ctl_pageLink_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_pageLink_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_pageLink_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_pageLink_10.doInitBody();
      }
      do {
        out.write("\n    Next page &gt;&gt;&gt;\n  ");
        int evalDoAfterBody = _jspx_th_ctl_pageLink_10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_pageLink_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_pageLink_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_pageLink_preserveParam_pageOffset.reuse(_jspx_th_ctl_pageLink_10);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_searchResultsPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_searchResultsPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n      .warningMessage { font-style: italic; color: #4A766E; }\n      .match { font-weight: bold; color: #200050; }\n  ");
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
