package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.content.rncontrols.DailySampleSummaryField;
import org.recipnet.site.shared.db.SampleInfo;

public final class sampleversions_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
    _jspx_dependants.add("/WEB-INF/controls.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleDailyVersionIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_dailySampleSummaryIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_currentPageLinkParam_name_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleDailyVersionIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_dailySampleSummaryIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_sampleDailyVersionIterator.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.release();
    _jspx_tagPool_rn_dailySampleSummaryIterator.release();
    _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_authorizationChecker_invert.release();
    _jspx_tagPool_ctl_a_href.release();
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.release();
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

      out.write("\n\n\n\n\n\n");
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("Sample Versions");
      _jspx_th_rn_samplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_samplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_samplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_samplePage_0 = _jspx_th_rn_samplePage_0.doStartTag();
      if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.SamplePage samplePage = null;
        if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_samplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_samplePage_0.doInitBody();
        }
        samplePage = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_page_context.findAttribute("samplePage");
        do {
          out.write("\n  <table cellspacing=\"10\">\n    <tr>\n      <td>\n        <strong>\n          ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
          out.write(":\n        </strong>\n        ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
          int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
          if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
          out.write("\n      </td>\n    </tr>\n    <tr>\n      <td>\n        <strong>Lab:</strong>");
          if (_jspx_meth_rn_labField_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n      </td> \n    </tr>\n    <tr>\n      <td>\n        <strong>Provider:</strong>");
          if (_jspx_meth_rn_providerField_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write("\n      </td>\n    </tr>\n  </table>\n  <br />\n  <table>\n    ");
          //  rn:sampleDailyVersionIterator
          org.recipnet.site.content.rncontrols.SampleDailyVersionIterator _jspx_th_rn_sampleDailyVersionIterator_0 = (org.recipnet.site.content.rncontrols.SampleDailyVersionIterator) _jspx_tagPool_rn_sampleDailyVersionIterator.get(org.recipnet.site.content.rncontrols.SampleDailyVersionIterator.class);
          _jspx_th_rn_sampleDailyVersionIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleDailyVersionIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
          int _jspx_eval_rn_sampleDailyVersionIterator_0 = _jspx_th_rn_sampleDailyVersionIterator_0.doStartTag();
          if (_jspx_eval_rn_sampleDailyVersionIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleDailyVersionIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleDailyVersionIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleDailyVersionIterator_0.doInitBody();
            }
            do {
              out.write("\n      ");
              //  ctl:parityChecker
              org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
              _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleDailyVersionIterator_0);
              _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnLast(true);
              int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
              if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_parityChecker_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td>\n            <table width=\"100%\" style=\"border: 3px solid #DAE8F1\">\n              <tr>\n                <th align=\"center\" bgcolor=\"#DAE8F1\" height=\"30\">\n                  ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_0);
                  _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                      SampleHistoryField.FieldCode.ACTION_DATE_ONLY);
                  int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                  out.write("\n                  <br />\n                  (most recent version)\n                </th>\n              </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_0);
              out.write("\n      ");
              //  ctl:parityChecker
              org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
              _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleDailyVersionIterator_0);
              _jspx_th_ctl_parityChecker_1.setIncludeOnlyOnLast(true);
              _jspx_th_ctl_parityChecker_1.setInvert(true);
              int _jspx_eval_ctl_parityChecker_1 = _jspx_th_ctl_parityChecker_1.doStartTag();
              if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_parityChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_parityChecker_1.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td>\n            <table width=\"100%\" style=\"border: 3px solid #F1E8DA\">\n              <tr>\n                <th align=\"center\" bgcolor=\"#F1E8DA\" height=\"30\">\n                  ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_1);
                  _jspx_th_rn_sampleHistoryField_1.setFieldCode(
                      SampleHistoryField.FieldCode.ACTION_DATE_ONLY);
                  int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
                  out.write("\n                </th>\n              </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_parityChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_parityChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_parityChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_1);
              out.write("\n            <tr> \n              <td>\n                <strong>Status:</strong>\n                  ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_2 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleDailyVersionIterator_0);
              _jspx_th_rn_sampleHistoryField_2.setFieldCode(
                      SampleHistoryField.FieldCode.NEW_STATUS);
              int _jspx_eval_rn_sampleHistoryField_2 = _jspx_th_rn_sampleHistoryField_2.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_2);
              out.write("\n                  ");
              //  rn:dailySampleSummaryIterator
              org.recipnet.site.content.rncontrols.DailySampleSummaryIterator _jspx_th_rn_dailySampleSummaryIterator_0 = (org.recipnet.site.content.rncontrols.DailySampleSummaryIterator) _jspx_tagPool_rn_dailySampleSummaryIterator.get(org.recipnet.site.content.rncontrols.DailySampleSummaryIterator.class);
              _jspx_th_rn_dailySampleSummaryIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_dailySampleSummaryIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleDailyVersionIterator_0);
              int _jspx_eval_rn_dailySampleSummaryIterator_0 = _jspx_th_rn_dailySampleSummaryIterator_0.doStartTag();
              if (_jspx_eval_rn_dailySampleSummaryIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_dailySampleSummaryIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_dailySampleSummaryIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_dailySampleSummaryIterator_0.doInitBody();
                }
                do {
                  out.write("\n                    <br />Action\n                    <i>\n                      ");
                  //  rn:dailySampleSummaryField
                  org.recipnet.site.content.rncontrols.DailySampleSummaryField _jspx_th_rn_dailySampleSummaryField_0 = (org.recipnet.site.content.rncontrols.DailySampleSummaryField) _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.DailySampleSummaryField.class);
                  _jspx_th_rn_dailySampleSummaryField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_dailySampleSummaryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_dailySampleSummaryIterator_0);
                  _jspx_th_rn_dailySampleSummaryField_0.setFieldCode(
                          DailySampleSummaryField.FieldCode.ACTION);
                  int _jspx_eval_rn_dailySampleSummaryField_0 = _jspx_th_rn_dailySampleSummaryField_0.doStartTag();
                  if (_jspx_th_rn_dailySampleSummaryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.reuse(_jspx_th_rn_dailySampleSummaryField_0);
                  out.write("\n                    </i> performed\n                    ");
                  //  rn:dailySampleSummaryField
                  org.recipnet.site.content.rncontrols.DailySampleSummaryField _jspx_th_rn_dailySampleSummaryField_1 = (org.recipnet.site.content.rncontrols.DailySampleSummaryField) _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.DailySampleSummaryField.class);
                  _jspx_th_rn_dailySampleSummaryField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_dailySampleSummaryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_dailySampleSummaryIterator_0);
                  _jspx_th_rn_dailySampleSummaryField_1.setFieldCode(DailySampleSummaryField.FieldCode.COUNT);
                  int _jspx_eval_rn_dailySampleSummaryField_1 = _jspx_th_rn_dailySampleSummaryField_1.doStartTag();
                  if (_jspx_th_rn_dailySampleSummaryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.reuse(_jspx_th_rn_dailySampleSummaryField_1);
                  out.write("\n                    time");
                  //  rn:dailySampleSummaryField
                  org.recipnet.site.content.rncontrols.DailySampleSummaryField _jspx_th_rn_dailySampleSummaryField_2 = (org.recipnet.site.content.rncontrols.DailySampleSummaryField) _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.DailySampleSummaryField.class);
                  _jspx_th_rn_dailySampleSummaryField_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_dailySampleSummaryField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_dailySampleSummaryIterator_0);
                  _jspx_th_rn_dailySampleSummaryField_2.setFieldCode(
                           DailySampleSummaryField.FieldCode.S_IF_PLURAL);
                  int _jspx_eval_rn_dailySampleSummaryField_2 = _jspx_th_rn_dailySampleSummaryField_2.doStartTag();
                  if (_jspx_th_rn_dailySampleSummaryField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_dailySampleSummaryField_fieldCode_nobody.reuse(_jspx_th_rn_dailySampleSummaryField_2);
                  out.write(".\n                  ");
                  int evalDoAfterBody = _jspx_th_rn_dailySampleSummaryIterator_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_dailySampleSummaryIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_dailySampleSummaryIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_dailySampleSummaryIterator.reuse(_jspx_th_rn_dailySampleSummaryIterator_0);
              out.write("\n                <br />\n                ");
              if (_jspx_meth_rn_link_0(_jspx_th_rn_sampleDailyVersionIterator_0, _jspx_page_context))
                return;
              out.write("\n              </td>   \n            </tr>\n          </table>\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"2\">&nbsp;</td>\n      </tr>\n    ");
              int evalDoAfterBody = _jspx_th_rn_sampleDailyVersionIterator_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleDailyVersionIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleDailyVersionIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleDailyVersionIterator.reuse(_jspx_th_rn_sampleDailyVersionIterator_0);
          out.write(" \n  </table>\n  <br />\n  ");
          if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_samplePage_0.doAfterBody();
          samplePage = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_page_context.findAttribute("samplePage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_samplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_samplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_samplePage_title_loginPageUrl_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_samplePage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleDailyVersionIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleDailyVersionIterator_0);
    _jspx_th_rn_link_0.setHref("/showsample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("See this version...");
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

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    _jspx_th_rn_authorizationChecker_0.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n    Additional versions of the sample may become visible after you\n    ");
        if (_jspx_meth_ctl_a_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write(".\n  ");
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
        out.write("\n      log in");
        if (_jspx_meth_ctl_currentPageLinkParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
          return true;
        out.write("\n    ");
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

  private boolean _jspx_meth_ctl_currentPageLinkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_0 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_ctl_currentPageLinkParam_0.setName("origUrl");
    int _jspx_eval_ctl_currentPageLinkParam_0 = _jspx_th_ctl_currentPageLinkParam_0.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_0);
    return false;
  }
}
