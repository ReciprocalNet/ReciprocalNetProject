package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.JammModelElement;
import org.recipnet.site.content.rncontrols.SampleTextIterator;
import org.recipnet.site.content.rncontrols.ShowSamplePage;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class showsamplebasic_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showSamplePage_usePreferredNameAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammModel_repositoryFile_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_stringIterator_strings_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleNameIterator_id_excludeAllButPreferredName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_preferredFormulaChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleNameIterator_id_excludePreferredName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_restrictByTextType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_restrictByTextType_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_showSamplePage_usePreferredNameAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammModel_repositoryFile_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_stringIterator_strings_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleNameIterator_id_excludeAllButPreferredName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_preferredFormulaChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleNameIterator_id_excludePreferredName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_showSamplePage_usePreferredNameAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_jammModel_repositoryFile_id.release();
    _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.release();
    _jspx_tagPool_ctl_stringIterator_strings_id.release();
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.release();
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.release();
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.release();
    _jspx_tagPool_rn_sampleNameIterator_id_excludeAllButPreferredName.release();
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.release();
    _jspx_tagPool_rn_preferredFormulaChecker.release();
    _jspx_tagPool_rn_sampleNameIterator_id_excludePreferredName.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.release();
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.release();
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleTextIterator_restrictByTextType_id.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n");
      //  rn:showSamplePage
      org.recipnet.site.content.rncontrols.ShowSamplePage _jspx_th_rn_showSamplePage_0 = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_tagPool_rn_showSamplePage_usePreferredNameAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ShowSamplePage.class);
      _jspx_th_rn_showSamplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_showSamplePage_0.setParent(null);
      _jspx_th_rn_showSamplePage_0.setSetPreferenceTo(UserBL.ShowsampleViewPref.BASIC);
      _jspx_th_rn_showSamplePage_0.setUsePreferredNameAsTitlePrefix(true);
      _jspx_th_rn_showSamplePage_0.setLabAndNumberSeparator(" sample ");
      _jspx_th_rn_showSamplePage_0.setTitleSuffix(" - Reciprocal Net Common Molecule");
      _jspx_th_rn_showSamplePage_0.setOverrideReinvocationServletPath("/showsample.jsp");
      _jspx_th_rn_showSamplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_showSamplePage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_showSamplePage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_showSamplePage_0 = _jspx_th_rn_showSamplePage_0.doStartTag();
      if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.ShowSamplePage htmlPage = null;
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_showSamplePage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_showSamplePage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <div class=\"pageBody\">\n  ");
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n  <table cellspacing=\"0\" cellpadding=\"0\"\n         style=\"width: 100%; margin-bottom: 0.5em;\">\n  <tr>\n  <td rowspan=\"2\" style=\"width: 400px;\">\n    ");
          //  rn:jammModel
          org.recipnet.site.content.rncontrols.JammModelElement jammModel = null;
          org.recipnet.site.content.rncontrols.JammModelElement _jspx_th_rn_jammModel_0 = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_tagPool_rn_jammModel_repositoryFile_id.get(org.recipnet.site.content.rncontrols.JammModelElement.class);
          _jspx_th_rn_jammModel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_jammModel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_jammModel_0.setId("jammModel");
          _jspx_th_rn_jammModel_0.setRepositoryFile((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtFile']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
          int _jspx_eval_rn_jammModel_0 = _jspx_th_rn_jammModel_0.doStartTag();
          if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_jammModel_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_jammModel_0.doInitBody();
            }
            jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
            do {
              out.write("\n      ");
              //  rn:jammApplet
              org.recipnet.site.content.rncontrols.JammAppletTag jamm = null;
              org.recipnet.site.content.rncontrols.JammAppletTag _jspx_th_rn_jammApplet_0 = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.get(org.recipnet.site.content.rncontrols.JammAppletTag.class);
              _jspx_th_rn_jammApplet_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_jammApplet_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_rn_jammApplet_0.setId("jamm");
              _jspx_th_rn_jammApplet_0.setWidth("400");
              _jspx_th_rn_jammApplet_0.setHeight("400");
              _jspx_th_rn_jammApplet_0.setAppletParam("jamm");
              int _jspx_eval_rn_jammApplet_0 = _jspx_th_rn_jammApplet_0.doStartTag();
              if (_jspx_th_rn_jammApplet_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              jamm = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_page_context.findAttribute("jamm");
              _jspx_tagPool_rn_jammApplet_width_id_height_appletParam_nobody.reuse(_jspx_th_rn_jammApplet_0);
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jammModel}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_0.setErrorFilter(JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n        &nbsp;<img src=\"images/nocrtfile.gif\" alt=\"no CRT file available\"/>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_0);
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
              _jspx_th_ctl_errorMessage_1.setInvertFilter(true);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(JammModelElement.NO_CRT_FILE_AVAILABLE
                  | JammModelElement.REPOSITORY_DIRECTORY_NOT_FOUND);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n        <table cellspacing=\"0\">\n          <tr>\n            <td style=\"vertical-align: top\">\n              <img src=\"images/tip.gif\" alt=\"TIP &gt;\"/>\n            </td>\n            <td colspan=\"3\" class=\"showSampleTipText\">\n                Click and drag your mouse inside the applet above\n                to rotate the molecule in 3-D.\n                <a href=\"help/applets.html\" style=\"padding-left: 1em;\"\n                    target=\"_blank\">Applet instructions...</a>\n            </td>\n          </tr>\n        </table>\n        <p class=\"showSampleTipText\">\n          Switch to another visualization applet:\n        </p>\n        <table cellspacing=\"0\">\n          ");
                  //  ctl:stringIterator
                  org.recipnet.common.controls.ExplicitStringIterator appletChoices = null;
                  org.recipnet.common.controls.ExplicitStringIterator _jspx_th_ctl_stringIterator_0 = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_tagPool_ctl_stringIterator_strings_id.get(org.recipnet.common.controls.ExplicitStringIterator.class);
                  _jspx_th_ctl_stringIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_stringIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                  _jspx_th_ctl_stringIterator_0.setId("appletChoices");
                  _jspx_th_ctl_stringIterator_0.setStrings("miniJaMM,JaMM1,JaMM2");
                  int _jspx_eval_ctl_stringIterator_0 = _jspx_th_ctl_stringIterator_0.doStartTag();
                  if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_stringIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_stringIterator_0.doInitBody();
                    }
                    appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                    do {
                      out.write("\n          <tr>\n            <td>\n              ");
                      if (_jspx_meth_rn_jammChecker_0(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_rn_jammChecker_1(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n            <td>\n              ");
                      if (_jspx_meth_rn_jammAppletPreservingLink_0(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n            <td style=\"padding-left: 2em\">\n              ");
                      if (_jspx_meth_rn_jammChecker_2(_jspx_th_ctl_stringIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n          </tr>\n          ");
                      int evalDoAfterBody = _jspx_th_ctl_stringIterator_0.doAfterBody();
                      appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_stringIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_stringIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  appletChoices = (org.recipnet.common.controls.ExplicitStringIterator) _jspx_page_context.findAttribute("appletChoices");
                  _jspx_tagPool_ctl_stringIterator_strings_id.reuse(_jspx_th_ctl_stringIterator_0);
                  out.write("\n        </table>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_invertFilter_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
              out.write("\n    ");
              int evalDoAfterBody = _jspx_th_rn_jammModel_0.doAfterBody();
              jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_jammModel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          jammModel = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_page_context.findAttribute("jammModel");
          _jspx_tagPool_rn_jammModel_repositoryFile_id.reuse(_jspx_th_rn_jammModel_0);
          out.write("\n  </td>\n  <td class=\"dataTable\">\n    <h1>\n      ");
          //  rn:sampleNameIterator
          org.recipnet.site.content.rncontrols.SampleNameIterator preferredName = null;
          org.recipnet.site.content.rncontrols.SampleNameIterator _jspx_th_rn_sampleNameIterator_0 = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_tagPool_rn_sampleNameIterator_id_excludeAllButPreferredName.get(org.recipnet.site.content.rncontrols.SampleNameIterator.class);
          _jspx_th_rn_sampleNameIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleNameIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleNameIterator_0.setExcludeAllButPreferredName(true);
          _jspx_th_rn_sampleNameIterator_0.setId("preferredName");
          int _jspx_eval_rn_sampleNameIterator_0 = _jspx_th_rn_sampleNameIterator_0.doStartTag();
          if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleNameIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleNameIterator_0.doInitBody();
            }
            preferredName = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("preferredName");
            do {
              out.write("\n        ");
              if (_jspx_meth_rn_sampleField_0(_jspx_th_rn_sampleNameIterator_0, _jspx_page_context))
                return;
              out.write("\n      ");
              int evalDoAfterBody = _jspx_th_rn_sampleNameIterator_0.doAfterBody();
              preferredName = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("preferredName");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleNameIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleNameIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          preferredName = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("preferredName");
          _jspx_tagPool_rn_sampleNameIterator_id_excludeAllButPreferredName.reuse(_jspx_th_rn_sampleNameIterator_0);
          out.write("\n      ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${preferredName}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_2.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
          int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
          if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_2.doInitBody();
            }
            do {
              out.write("\n        ");
              if (_jspx_meth_rn_labField_0(_jspx_th_ctl_errorMessage_2, _jspx_page_context))
                return;
              out.write("\n        #");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
              _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.ID);
              _jspx_th_rn_sampleField_1.setDisplayAsLabel(true);
              int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
              if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_1);
              out.write("\n      ");
              int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_2);
          out.write("\n    </h1>\n    ");
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleChecker_1.setIncludeIfValueIsPresent(SampleTextBL.SHORT_DESCRIPTION);
          int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
          if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleChecker_1.doInitBody();
            }
            do {
              out.write("\n      <h2>");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_rn_sampleField_2.setDisplayAsLabel(true);
              _jspx_th_rn_sampleField_2.setFieldCode(SampleTextBL.SHORT_DESCRIPTION);
              int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
              if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_2);
              out.write("</h2>\n    ");
              int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleChecker_includeIfValueIsPresent.reuse(_jspx_th_rn_sampleChecker_1);
          out.write("\n    ");
          if (_jspx_meth_rn_preferredFormulaChecker_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n\n    ");
          //  rn:sampleNameIterator
          org.recipnet.site.content.rncontrols.SampleNameIterator otherNames = null;
          org.recipnet.site.content.rncontrols.SampleNameIterator _jspx_th_rn_sampleNameIterator_1 = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_tagPool_rn_sampleNameIterator_id_excludePreferredName.get(org.recipnet.site.content.rncontrols.SampleNameIterator.class);
          _jspx_th_rn_sampleNameIterator_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleNameIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleNameIterator_1.setExcludePreferredName(true);
          _jspx_th_rn_sampleNameIterator_1.setId("otherNames");
          int _jspx_eval_rn_sampleNameIterator_1 = _jspx_th_rn_sampleNameIterator_1.doStartTag();
          if (_jspx_eval_rn_sampleNameIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleNameIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleNameIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleNameIterator_1.doInitBody();
            }
            otherNames = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("otherNames");
            do {
              if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_sampleNameIterator_1, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_parityChecker_1(_jspx_th_rn_sampleNameIterator_1, _jspx_page_context))
                return;
              out.write("\n      ");
              if (_jspx_meth_rn_sampleField_4(_jspx_th_rn_sampleNameIterator_1, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_parityChecker_2(_jspx_th_rn_sampleNameIterator_1, _jspx_page_context))
                return;
              int evalDoAfterBody = _jspx_th_rn_sampleNameIterator_1.doAfterBody();
              otherNames = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("otherNames");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleNameIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleNameIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          otherNames = (org.recipnet.site.content.rncontrols.SampleNameIterator) _jspx_page_context.findAttribute("otherNames");
          _jspx_tagPool_rn_sampleNameIterator_id_excludePreferredName.reuse(_jspx_th_rn_sampleNameIterator_1);
          out.write("\n\n    ");
          //  rn:sampleTextIterator
          org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_0 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
          _jspx_th_rn_sampleTextIterator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleTextIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleTextIterator_0.setRestrictByTextType(SampleTextBL.LAYMANS_EXPLANATION);
          int _jspx_eval_rn_sampleTextIterator_0 = _jspx_th_rn_sampleTextIterator_0.doStartTag();
          if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleTextIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleTextIterator_0.doInitBody();
            }
            do {
              out.write("\n      <div class=\"dataItem\">\n        <span class=\"itemLabel\">");
              if (_jspx_meth_rn_sampleFieldLabel_0(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                return;
              out.write(":</span>\n        ");
              if (_jspx_meth_rn_sampleField_5(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                return;
              out.write("\n      </div>\n    ");
              int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleTextIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleTextIterator_restrictByTextType.reuse(_jspx_th_rn_sampleTextIterator_0);
          out.write("\n\n    ");
          //  rn:sampleTextIterator
          org.recipnet.site.content.rncontrols.SampleTextIterator keywords = null;
          org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_1 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_restrictByTextType_id.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
          _jspx_th_rn_sampleTextIterator_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleTextIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
          _jspx_th_rn_sampleTextIterator_1.setRestrictByTextType(SampleTextBL.KEYWORD);
          _jspx_th_rn_sampleTextIterator_1.setId("keywords");
          int _jspx_eval_rn_sampleTextIterator_1 = _jspx_th_rn_sampleTextIterator_1.doStartTag();
          if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleTextIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleTextIterator_1.doInitBody();
            }
            keywords = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("keywords");
            do {
              if (_jspx_meth_ctl_parityChecker_3(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_parityChecker_4(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                return;
              out.write("\n    ");
              if (_jspx_meth_rn_sampleField_6(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                return;
              if (_jspx_meth_ctl_parityChecker_5(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                return;
              int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_1.doAfterBody();
              keywords = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("keywords");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleTextIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          keywords = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("keywords");
          _jspx_tagPool_rn_sampleTextIterator_restrictByTextType_id.reuse(_jspx_th_rn_sampleTextIterator_1);
          out.write("\n  </td>\n  </tr>\n  <tr>\n  <td class=\"navLinks\">\n      ");
          if (_jspx_meth_rn_link_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("<br />\n      ");
          if (_jspx_meth_rn_link_1(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("<br />\n      ");
          if (_jspx_meth_rn_link_2(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write("\n  </td>\n  </tr>\n  </table>\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_showSamplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_showSamplePage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.ShowSamplePage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_showSamplePage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_showSamplePage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_showSamplePage_usePreferredNameAsTitlePrefix_titleSuffix_setPreferenceTo_overrideReinvocationServletPath_loginPageUrl_labAndNumberSeparator_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_showSamplePage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <p style=\"text-align: center; text-color: #F00000;\">\n      This sample has been retracted by its lab, possibly because it is\n      partially or wholly incorrect.  Please examine this\n      sample's comments for more details.\n    </p>\n  ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.reuse(_jspx_th_rn_sampleChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_0 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_0.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_0.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_0 = _jspx_th_rn_jammChecker_0.doStartTag();
    if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_0.doInitBody();
      }
      do {
        out.write("\n                <img src=\"images/pointer.gif\" alt=\"&gt;\"/>\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_1 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_1.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_1.setInvert(true);
    _jspx_th_rn_jammChecker_1.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_1 = _jspx_th_rn_jammChecker_1.doStartTag();
    if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_1.doInitBody();
      }
      do {
        out.write("\n                <img src=\"images/pointer-blank.gif\" alt=\"-\"/>\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_invert_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_0 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammAppletPreservingLink_0.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setSwitchApplet((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_0.setDisableWhenLinkMatchesAppletTag(true);
    int _jspx_eval_rn_jammAppletPreservingLink_0 = _jspx_th_rn_jammAppletPreservingLink_0.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_0.doInitBody();
      }
      do {
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_switchApplet_jammAppletTag_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_stringIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammChecker
    org.recipnet.site.content.rncontrols.JammChecker _jspx_th_rn_jammChecker_2 = (org.recipnet.site.content.rncontrols.JammChecker) _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.get(org.recipnet.site.content.rncontrols.JammChecker.class);
    _jspx_th_rn_jammChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_stringIterator_0);
    _jspx_th_rn_jammChecker_2.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammChecker_2.setIncludeOnlyIfCurrentAppletIs((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${appletChoices.currentString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammChecker_2 = _jspx_th_rn_jammChecker_2.doStartTag();
    if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammChecker_2.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_jammAppletPreservingLink_1(_jspx_th_rn_jammChecker_2, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_jammChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammChecker_jammAppletTag_includeOnlyIfCurrentAppletIs.reuse(_jspx_th_rn_jammChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_jammAppletPreservingLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_jammChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammAppletPreservingLink
    org.recipnet.site.content.rncontrols.JammAppletPreservingLink _jspx_th_rn_jammAppletPreservingLink_1 = (org.recipnet.site.content.rncontrols.JammAppletPreservingLink) _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.get(org.recipnet.site.content.rncontrols.JammAppletPreservingLink.class);
    _jspx_th_rn_jammAppletPreservingLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammAppletPreservingLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammChecker_2);
    _jspx_th_rn_jammAppletPreservingLink_1.setHref("/jamm.jsp");
    _jspx_th_rn_jammAppletPreservingLink_1.setJammAppletTag((org.recipnet.site.content.rncontrols.JammAppletTag) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${jamm}", org.recipnet.site.content.rncontrols.JammAppletTag.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_jammAppletPreservingLink_1.setOpenInWindow("true");
    _jspx_th_rn_jammAppletPreservingLink_1.setDisableWhenLinkMatchesAppletTag(false);
    int _jspx_eval_rn_jammAppletPreservingLink_1 = _jspx_th_rn_jammAppletPreservingLink_1.doStartTag();
    if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammAppletPreservingLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammAppletPreservingLink_1.doInitBody();
      }
      do {
        out.write("open in new window...");
        int evalDoAfterBody = _jspx_th_rn_jammAppletPreservingLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammAppletPreservingLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammAppletPreservingLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammAppletPreservingLink_openInWindow_jammAppletTag_href_disableWhenLinkMatchesAppletTag.reuse(_jspx_th_rn_jammAppletPreservingLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_0);
    _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
    if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorMessage_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_2);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_preferredFormulaChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:preferredFormulaChecker
    org.recipnet.site.content.rncontrols.PreferredFormulaChecker _jspx_th_rn_preferredFormulaChecker_0 = (org.recipnet.site.content.rncontrols.PreferredFormulaChecker) _jspx_tagPool_rn_preferredFormulaChecker.get(org.recipnet.site.content.rncontrols.PreferredFormulaChecker.class);
    _jspx_th_rn_preferredFormulaChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_preferredFormulaChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_rn_preferredFormulaChecker_0 = _jspx_th_rn_preferredFormulaChecker_0.doStartTag();
    if (_jspx_eval_rn_preferredFormulaChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_preferredFormulaChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_preferredFormulaChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_preferredFormulaChecker_0.doInitBody();
      }
      do {
        out.write("\n      <div class=\"dataItem\">\n        <span class=\"itemLabel\">Chemical Formula:</span>\n        ");
        if (_jspx_meth_rn_sampleField_3(_jspx_th_rn_preferredFormulaChecker_0, _jspx_page_context))
          return true;
        out.write("\n      </div>\n    ");
        int evalDoAfterBody = _jspx_th_rn_preferredFormulaChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_preferredFormulaChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_preferredFormulaChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_preferredFormulaChecker.reuse(_jspx_th_rn_preferredFormulaChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_preferredFormulaChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_preferredFormulaChecker_0);
    _jspx_th_rn_sampleField_3.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
    if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_3);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_1);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write("\n      <div class=\"dataItem\">\n      <span class=\"itemLabel\">Other names:</span>\n      ");
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

  private boolean _jspx_meth_ctl_parityChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_1 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_1);
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
        out.write(",\n      ");
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

  private boolean _jspx_meth_rn_sampleField_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_1);
    _jspx_th_rn_sampleField_4.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
    if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_4);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleNameIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_2 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleNameIterator_1);
    _jspx_th_ctl_parityChecker_2.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_2 = _jspx_th_ctl_parityChecker_2.doStartTag();
    if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_2.doInitBody();
      }
      do {
        out.write("\n      </div>\n      ");
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

  private boolean _jspx_meth_rn_sampleFieldLabel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    _jspx_th_rn_sampleField_5.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
    if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_5);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_3 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    _jspx_th_ctl_parityChecker_3.setIncludeOnlyOnFirst(true);
    int _jspx_eval_ctl_parityChecker_3 = _jspx_th_ctl_parityChecker_3.doStartTag();
    if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_3.doInitBody();
      }
      do {
        out.write("\n      <div class=\"dataItem\">\n        <span class=\"itemLabel\">Keywords:</span>\n    ");
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

  private boolean _jspx_meth_ctl_parityChecker_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_4 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
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
        out.write(",\n    ");
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

  private boolean _jspx_meth_rn_sampleField_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    _jspx_th_rn_sampleField_6.setDisplayAsLabel(true);
    int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
    if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_6);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_5 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    _jspx_th_ctl_parityChecker_5.setIncludeOnlyOnLast(true);
    int _jspx_eval_ctl_parityChecker_5 = _jspx_th_ctl_parityChecker_5.doStartTag();
    if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_5.doInitBody();
      }
      do {
        out.write("\n      </div>\n    ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_includeOnlyOnLast.reuse(_jspx_th_ctl_parityChecker_5);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_0.setHref("/showsampledetailed.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Crystallographic\n        details...");
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

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_1.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("More visualization options...");
        int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_1);
    return false;
  }

  private boolean _jspx_meth_rn_link_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_link_2.setHref("/sampleversions.jsp");
    int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_2.doInitBody();
      }
      do {
        out.write("See other versions...");
        int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_2);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
    _jspx_th_rn_authorizationChecker_0.setCanEditSample(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("<br />\n        ");
        if (_jspx_meth_rn_link_3(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("\n      ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canEditSample.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_rn_link_3.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_3.doInitBody();
      }
      do {
        out.write("Edit this sample...");
        int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_3);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showSamplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showSamplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    body               { background-color: #FFFFCC; }\n    div.pageBody       { text-align: left; }\n    td.dataTable       { vertical-align: top; }\n    td.dataTable h1,\n    td.dataTable h2,\n    td.dataTable div   { margin-left: 1.5em; }\n    h1 + div, h2 + div { margin-top: 1.5em; }\n    td.dataTable h1,\n    td.dataTable h2    { text-align: center; margin-bottom: 0.25em; }\n    td.dataTable h1    { font-size: 130%; font-weight: bold; color: #660033; }\n    td.dataTable div   { font-family: Times, serif; font-size: medium; \n                         margin-bottom: 0.5em; }\n    td.dataTable span.itemLabel { color: #660033; font-weight: bold;}\n    td.navLinks        { text-align: right; vertical-align: bottom;\n                         font-family: inherit; font-size: inherit; \n                         padding-top: 1em; }\n    .showSampleTipText { font-size: small; }\n  ");
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
