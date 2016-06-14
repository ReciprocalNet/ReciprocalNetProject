package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.content.rncontrols.SelectFilesForActionPage;

public final class managefiles_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_0;

static {
  _jspx_fnmap_0= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("rn:testParity", org.recipnet.site.content.rncontrols.ElFunctions.class, "testParity", new Class[] {int.class, java.lang.String.class, java.lang.String.class});
}

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_selectFilesForActionPage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileSelectionCheckbox_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_visible_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_selectFilesForActionPage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileSelectionCheckbox_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_visible_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_selectFilesForActionPage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_fileIterator_id.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.release();
    _jspx_tagPool_rn_fileSelectionCheckbox_id.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.release();
    _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_link_href.release();
    _jspx_tagPool_rn_fileParam_name_nobody.release();
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription.release();
    _jspx_tagPool_rn_link_visible_href.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody.release();
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
      //  rn:selectFilesForActionPage
      org.recipnet.site.content.rncontrols.SelectFilesForActionPage _jspx_th_rn_selectFilesForActionPage_0 = (org.recipnet.site.content.rncontrols.SelectFilesForActionPage) _jspx_tagPool_rn_selectFilesForActionPage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SelectFilesForActionPage.class);
      _jspx_th_rn_selectFilesForActionPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_selectFilesForActionPage_0.setParent(null);
      _jspx_th_rn_selectFilesForActionPage_0.setTitle("Manage Files");
      _jspx_th_rn_selectFilesForActionPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_selectFilesForActionPage_0.setIgnoreSampleHistoryId(true);
      _jspx_th_rn_selectFilesForActionPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_selectFilesForActionPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_selectFilesForActionPage_0 = _jspx_th_rn_selectFilesForActionPage_0.doStartTag();
      if (_jspx_eval_rn_selectFilesForActionPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.SelectFilesForActionPage htmlPage = null;
        if (_jspx_eval_rn_selectFilesForActionPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_selectFilesForActionPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_selectFilesForActionPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.SelectFilesForActionPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <br />\n  ");
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_selectFilesForActionPage_0);
          _jspx_th_rn_authorizationChecker_0.setCanEditSample(true);
          _jspx_th_rn_authorizationChecker_0.setInvert(true);
          _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
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
            AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE);
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
          _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.reuse(_jspx_th_rn_authorizationChecker_0);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_selectFilesForActionPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_selectFilesForActionPage_0);
          _jspx_th_rn_sampleChecker_1.setIncludeIfRetracted(true);
          _jspx_th_rn_sampleChecker_1.setInvert(true);
          int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
          if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleChecker_1.doInitBody();
            }
            do {
              out.write("\n    ");
              if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n    <table width=\"100%\">\n      <tr>\n        <td valign=\"top\" align=\"center\">\n          ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                do {
                  out.write("\n            <table class=\"table\" cellspacing=\"0\">\n              <tr>\n                <th colspan=\"9\" class=\"tableHeader\">\n                  Repository files:\n                </th>\n              </tr>\n              <tr>\n                <td class=\"tableLabels\">selected</td>\n                <td class=\"tableLabels\">filename</td>\n                <td class=\"tableLabels\">size</td>\n                <td class=\"tableLabels\">most recent modification date</td>\n                <td class=\"tableLabels\" width=\"200\" align=\"left\">\n                  description\n                </td>\n                <td class=\"tableLabels\">&nbsp;</td>\n                <td class=\"tableLabels\">&nbsp;</td>\n                <td class=\"tableLabels\">&nbsp;</td>\n                <td class=\"tableLabels\">&nbsp;</td>\n              </tr>\n              ");
                  //  rn:fileIterator
                  org.recipnet.site.content.rncontrols.FileIterator fileIt = null;
                  org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                  _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_fileIterator_0.setId("fileIt");
                  int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
                  if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_fileIterator_0.doInitBody();
                    }
                    fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
                    do {
                      out.write("\n                <tr class=\"");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(fileIt.iterationCountSinceThisPhaseBegan,\n                                           'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                      out.write("\">\n                  <td class=\"common\">\n                    ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_ctl_errorChecker_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionDateField}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_0.setInvert(true);
                      int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:fileSelectionCheckbox
                          org.recipnet.site.content.rncontrols.FileSelectionCheckbox selectedFiles = null;
                          org.recipnet.site.content.rncontrols.FileSelectionCheckbox _jspx_th_rn_fileSelectionCheckbox_0 = (org.recipnet.site.content.rncontrols.FileSelectionCheckbox) _jspx_tagPool_rn_fileSelectionCheckbox_id.get(org.recipnet.site.content.rncontrols.FileSelectionCheckbox.class);
                          _jspx_th_rn_fileSelectionCheckbox_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileSelectionCheckbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
                          _jspx_th_rn_fileSelectionCheckbox_0.setId("selectedFiles");
                          int _jspx_eval_rn_fileSelectionCheckbox_0 = _jspx_th_rn_fileSelectionCheckbox_0.doStartTag();
                          if (_jspx_eval_rn_fileSelectionCheckbox_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_fileSelectionCheckbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_fileSelectionCheckbox_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_fileSelectionCheckbox_0.doInitBody();
                            }
                            selectedFiles = (org.recipnet.site.content.rncontrols.FileSelectionCheckbox) _jspx_page_context.findAttribute("selectedFiles");
                            do {
                              out.write("\n                        ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_rn_fileSelectionCheckbox_0, _jspx_page_context))
                              return;
                              out.write("\n                      ");
                              int evalDoAfterBody = _jspx_th_rn_fileSelectionCheckbox_0.doAfterBody();
                              selectedFiles = (org.recipnet.site.content.rncontrols.FileSelectionCheckbox) _jspx_page_context.findAttribute("selectedFiles");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_fileSelectionCheckbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_fileSelectionCheckbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          selectedFiles = (org.recipnet.site.content.rncontrols.FileSelectionCheckbox) _jspx_page_context.findAttribute("selectedFiles");
                          _jspx_tagPool_rn_fileSelectionCheckbox_id.reuse(_jspx_th_rn_fileSelectionCheckbox_0);
                          out.write("\n                    ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.reuse(_jspx_th_ctl_errorChecker_0);
                      out.write("\n                    ");
                      if (_jspx_meth_ctl_errorChecker_1(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                  </td>\n                  <td class=\"common\" align=\"left\">\n                    ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_rn_fileField_0.setFieldCode(FileField.LINKED_FILENAME);
                      int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                      if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                      out.write("\n                  </td>\n                  <td class=\"common\" align=\"right\">\n                    ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_rn_fileField_1.setFieldCode(FileField.FILE_SIZE);
                      int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                      if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                      out.write("\n                    &nbsp;&nbsp;\n                  </td>\n                  <td class=\"common\" width=\"200\" align=\"center\">\n                    ");
                      //  rn:sampleHistoryTranslator
                      org.recipnet.site.content.rncontrols.SampleHistoryTranslator _jspx_th_rn_sampleHistoryTranslator_0 = (org.recipnet.site.content.rncontrols.SampleHistoryTranslator) _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.get(org.recipnet.site.content.rncontrols.SampleHistoryTranslator.class);
                      _jspx_th_rn_sampleHistoryTranslator_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleHistoryTranslator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_rn_sampleHistoryTranslator_0.setTranslateFromFileContext(true);
                      int _jspx_eval_rn_sampleHistoryTranslator_0 = _jspx_th_rn_sampleHistoryTranslator_0.doStartTag();
                      if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_sampleHistoryTranslator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_sampleHistoryTranslator_0.doInitBody();
                        }
                        do {
                          out.write("\n                      ");
                          //  rn:sampleHistoryField
                          org.recipnet.site.content.rncontrols.SampleHistoryField actionDateField = null;
                          org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                          _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryTranslator_0);
                          _jspx_th_rn_sampleHistoryField_0.setId("actionDateField");
                          _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                          SampleHistoryField.FieldCode.ACTION_DATE);
                          _jspx_th_rn_sampleHistoryField_0.setRequireHistory(false);
                          _jspx_th_rn_sampleHistoryField_0.setNoHistoryText("unknown");
                          int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
                          if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          actionDateField = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_page_context.findAttribute("actionDateField");
                          _jspx_tagPool_rn_sampleHistoryField_requireHistory_noHistoryText_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
                          out.write("\n                    ");
                          int evalDoAfterBody = _jspx_th_rn_sampleHistoryTranslator_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_sampleHistoryTranslator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_sampleHistoryTranslator_translateFromFileContext.reuse(_jspx_th_rn_sampleHistoryTranslator_0);
                      out.write("\n                  </td>\n                  <td class=\"common\" width=\"200\" align=\"left\">\n                    ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_rn_fileField_2.setFieldCode(FileField.DESCRIPTION);
                      int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                      if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                      out.write("\n                  </td>\n                  ");
                      if (_jspx_meth_ctl_errorChecker_2(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                  ");
                      if (_jspx_meth_ctl_errorChecker_3(_jspx_th_rn_fileIterator_0, _jspx_page_context))
                        return;
                      out.write("\n                </tr>\n              ");
                      int evalDoAfterBody = _jspx_th_rn_fileIterator_0.doAfterBody();
                      fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_fileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
                  _jspx_tagPool_rn_fileIterator_id.reuse(_jspx_th_rn_fileIterator_0);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_0.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
                  int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_0.doInitBody();
                    }
                    do {
                      out.write("\n                <tr class=\"oddRow\">\n                  <td class=\"nofiles\" colspan=\"9\">\n                    There are currently no files in the repository.\n                  </td>\n                </tr>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileIt}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(HtmlPageIterator.NO_ITERATIONS);
                  _jspx_th_ctl_errorMessage_1.setInvertFilter(true);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n                <tr>\n                  <td class=\"tableInstructions\" colspan=\"9\">\n                    ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                      _jspx_th_ctl_errorMessage_2.setErrorFilter(
                            SelectFilesForActionPage.NO_FILES_SELECTED);
                      int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_2.doInitBody();
                        }
                        do {
                          out.write("\n                     <p class=\"errorMessage\">\n                       You must select at least one file by clicking the\n                        checkbox to the left of its name before clicking any of\n                        the buttons below.\n                      </p>\n                    ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_2);
                      out.write("\n                    ");
                      //  rn:initActionOnFilesButton
                      org.recipnet.site.content.rncontrols.InitActionOnFilesButton removeButton = null;
                      org.recipnet.site.content.rncontrols.InitActionOnFilesButton _jspx_th_rn_initActionOnFilesButton_0 = (org.recipnet.site.content.rncontrols.InitActionOnFilesButton) _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody.get(org.recipnet.site.content.rncontrols.InitActionOnFilesButton.class);
                      _jspx_th_rn_initActionOnFilesButton_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_initActionOnFilesButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                      _jspx_th_rn_initActionOnFilesButton_0.setId("removeButton");
                      _jspx_th_rn_initActionOnFilesButton_0.setLabel("Remove selected files...");
                      _jspx_th_rn_initActionOnFilesButton_0.setTargetExtendedOpWapPageUrl("/lab/removefiles.jsp");
                      int _jspx_eval_rn_initActionOnFilesButton_0 = _jspx_th_rn_initActionOnFilesButton_0.doStartTag();
                      if (_jspx_th_rn_initActionOnFilesButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      removeButton = (org.recipnet.site.content.rncontrols.InitActionOnFilesButton) _jspx_page_context.findAttribute("removeButton");
                      _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody.reuse(_jspx_th_rn_initActionOnFilesButton_0);
                      out.write("\n                    ");
                      //  rn:initActionOnFilesButton
                      org.recipnet.site.content.rncontrols.InitActionOnFilesButton eradicateButton = null;
                      org.recipnet.site.content.rncontrols.InitActionOnFilesButton _jspx_th_rn_initActionOnFilesButton_1 = (org.recipnet.site.content.rncontrols.InitActionOnFilesButton) _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody.get(org.recipnet.site.content.rncontrols.InitActionOnFilesButton.class);
                      _jspx_th_rn_initActionOnFilesButton_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_initActionOnFilesButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                      _jspx_th_rn_initActionOnFilesButton_1.setId("eradicateButton");
                      _jspx_th_rn_initActionOnFilesButton_1.setLabel("Eradicate selected files...");
                      _jspx_th_rn_initActionOnFilesButton_1.setTargetExtendedOpWapPageUrl("/lab/eradicatefiles.jsp");
                      int _jspx_eval_rn_initActionOnFilesButton_1 = _jspx_th_rn_initActionOnFilesButton_1.doStartTag();
                      if (_jspx_th_rn_initActionOnFilesButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      eradicateButton = (org.recipnet.site.content.rncontrols.InitActionOnFilesButton) _jspx_page_context.findAttribute("eradicateButton");
                      _jspx_tagPool_rn_initActionOnFilesButton_targetExtendedOpWapPageUrl_label_id_nobody.reuse(_jspx_th_rn_initActionOnFilesButton_1);
                      out.write("\n                  </td>\n                </tr>\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
                  out.write("\n            </table>\n          ");
                  int evalDoAfterBody = _jspx_th_ctl_selfForm_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_selfForm_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_selfForm.reuse(_jspx_th_ctl_selfForm_0);
              out.write("\n        </td>\n      </tr>\n    </table>\n    <p align=\"right\">\n      ");
              if (_jspx_meth_rn_link_10(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_11(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_12(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n      <br />\n      ");
              if (_jspx_meth_rn_link_13(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("\n    </p>\n  ");
              int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.reuse(_jspx_th_rn_sampleChecker_1);
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_selectFilesForActionPage_0, _jspx_page_context))
            return;
          out.write("\n  <script language=\"JavaScript\">\n    <!-- // begin hiding javascript from older browsers\n\n        var manuallyUpdatedFieldNames = \"\";\n\n        function updatedField(fieldName) {\n            if (manuallyUpdatedFieldNames.indexOf(fieldName)==-1) {\n                manuallyUpdatedFieldNames=manuallyUpdatedFieldNames+fieldName;\n            }\n        }\n\n        // This function sets the form field with the name given as the\n        // remoteNameBoxId parameter to the filename to the filename entered\n        // in the localNameBoxId box (with the path removed) if there is no\n        // remote filename entered.\n\n        // Netscape 4.8 does not trigger onChange() when the \"Browse\"\n        // button of the FileUpload element is clicked, so it is\n        // neccessary to include a call to this function for the onBlur()\n        // event as well, to approximate the desired behavior\n        // onClick is triggered when the \"browse\" button of a FileUpload\n        // element is clicked for some browsers, though there is variance\n");
          out.write("        // in whether it is proccessed before or after the field is\n        // updated by the file selection dialog box.\n        function setRemoteName(remoteNameBoxId, localNameBoxId) {\n            if (manuallyUpdatedFieldNames.indexOf(remoteNameBoxId)!=-1) {\n              // the box shouldn't be updated\n              return;\n            } else {\n              var localPath = getElementById(localNameBoxId).value;\n              var lastSlash = localPath.length;\n                for (var j = localPath.length; j >= 0; j -- ) {\n                  if (localPath.charAt(j) == '/'\n                      || localPath.charAt(j) == '\\\\') {\n                    lastSlash = j + 1;\n                    j = 0;\n                  }\n                }\n              getElementById(remoteNameBoxId).value\n                  = localPath.substring(lastSlash);\n            }\n        }\n\n        function getElementById(id) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n");
          out.write("              if (document.forms[fi].elements[index].name == id) {\n                return document.forms[fi].elements[index];\n              } else {\n                  index ++;\n              }\n            }\n          }\n        }\n\n        function getElementByIdPart(idPart) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n              if (document.forms[fi].elements[index].name.indexOf(idPart)==-1) {\n                index++;\n              } else {\n                return document.forms[fi].elements[index];\n              }\n            }\n          }\n        }\n\n        // if any checkbox element in the page with the name 'checkboxName'\n        // is checked, the button 'buttonName' will be enabled, otherwise it\n        // will be disabled\n        function enableDisableButton(buttonNamePart, checkboxNamePart) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            var index = 0;\n            while (index < document.forms[fi].elements.length) {\n");
          out.write("              if (document.forms[fi].elements[index].name.indexOf(\n                  checkboxNamePart)!=-1) {\n                if (document.forms[fi].elements[index].checked==true) {\n                  setDisabled(buttonNamePart, false);\n                  return;\n                }\n              }\n              index ++;\n            }\n          }\n          setDisabled(buttonNamePart, true);\n        }\n\n        function setDisabled(buttonNamePart, isDisabled) {\n          for (var fi = 0; fi < document.forms.length; fi ++) {\n            for (var index = 0; index < document.forms[fi].elements.length;\n                    index++) {\n              if (document.forms[fi].elements[index].name.indexOf(\n                      buttonNamePart)==-1) {\n              } else {\n                document.forms[fi].elements[index].disabled=isDisabled;\n              }\n            }\n          }\n        }\n        // stop hiding -->\n  </script>\n");
          int evalDoAfterBody = _jspx_th_rn_selectFilesForActionPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.SelectFilesForActionPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_selectFilesForActionPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_selectFilesForActionPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_selectFilesForActionPage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_selectFilesForActionPage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_selectFilesForActionPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_selectFilesForActionPage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <center>\n      <font color=\"red\">\n        This sample has been retracted by its lab and cannot have file actions\n        performed on it.\n      </font>\n    </center>\n    <br />\n  ");
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

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("onLoad");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("enableDisableButton('Button', 'selectedFiles')");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileSelectionCheckbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_1 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileSelectionCheckbox_0);
    _jspx_th_ctl_extraHtmlAttribute_1.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("enableDisableButton('Button',\n                                        'selectedFiles')");
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_ctl_errorChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:errorChecker
    org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
    _jspx_th_ctl_errorChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_errorChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_errorChecker_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionDateField}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_errorChecker_1 = _jspx_th_ctl_errorChecker_1.doStartTag();
    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_errorChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_errorChecker_1.doInitBody();
      }
      do {
        out.write("\n                      &nbsp;\n                    ");
        int evalDoAfterBody = _jspx_th_ctl_errorChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_errorChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_errorChecker_errorSupplier.reuse(_jspx_th_ctl_errorChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_errorChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:errorChecker
    org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
    _jspx_th_ctl_errorChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_errorChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_errorChecker_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionDateField}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_ctl_errorChecker_2.setInvert(true);
    int _jspx_eval_ctl_errorChecker_2 = _jspx_th_ctl_errorChecker_2.doStartTag();
    if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_errorChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_errorChecker_2.doInitBody();
      }
      do {
        out.write("\n                    <td class=\"common\">\n                        ");
        if (_jspx_meth_rn_link_0(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_link_1(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_link_2(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_fileChecker_0(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
          return true;
        out.write("\n                      ");
        if (_jspx_meth_rn_fileChecker_1(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_errorChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_errorChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.reuse(_jspx_th_ctl_errorChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_rn_link_0.setHref("/lab/removefiles.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_0(_jspx_th_rn_link_0, _jspx_page_context))
          return true;
        out.write("remove\n                        ");
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

  private boolean _jspx_meth_rn_fileParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_0 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_0);
    _jspx_th_rn_fileParam_0.setName("filename");
    int _jspx_eval_rn_fileParam_0 = _jspx_th_rn_fileParam_0.doStartTag();
    if (_jspx_th_rn_fileParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_rn_link_1.setHref("/lab/renamefile.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_fileParam_1(_jspx_th_rn_link_1, _jspx_page_context))
          return true;
        out.write("rename\n                      ");
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

  private boolean _jspx_meth_rn_fileParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_1 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_1);
    _jspx_th_rn_fileParam_1.setName("selectedFile");
    int _jspx_eval_rn_fileParam_1 = _jspx_th_rn_fileParam_1.doStartTag();
    if (_jspx_th_rn_fileParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_link_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_rn_link_2.setHref("/lab/eradicatefiles.jsp");
    int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_2.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_fileParam_2(_jspx_th_rn_link_2, _jspx_page_context))
          return true;
        out.write("eradicate\n                      ");
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

  private boolean _jspx_meth_rn_fileParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_2 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
    _jspx_th_rn_fileParam_2.setName("filename");
    int _jspx_eval_rn_fileParam_2 = _jspx_th_rn_fileParam_2.doStartTag();
    if (_jspx_th_rn_fileParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_2);
    return false;
  }

  private boolean _jspx_meth_rn_fileChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileChecker
    org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
    _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_rn_fileChecker_0.setIncludeOnlyIfFileHasDescription(true);
    int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
    if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_fileChecker_0.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_link_3(_jspx_th_rn_fileChecker_0, _jspx_page_context))
          return true;
        out.write("\n                      ");
        int evalDoAfterBody = _jspx_th_rn_fileChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_fileChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_0);
    return false;
  }

  private boolean _jspx_meth_rn_link_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
    _jspx_th_rn_link_3.setHref("/lab/modifyfiledescription.jsp");
    int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_3.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_3(_jspx_th_rn_link_3, _jspx_page_context))
          return true;
        out.write("\n                          edit description\n                        ");
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

  private boolean _jspx_meth_rn_fileParam_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_3 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_3);
    _jspx_th_rn_fileParam_3.setName("selectedFile");
    int _jspx_eval_rn_fileParam_3 = _jspx_th_rn_fileParam_3.doStartTag();
    if (_jspx_th_rn_fileParam_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_3);
    return false;
  }

  private boolean _jspx_meth_rn_fileChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileChecker
    org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_1 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
    _jspx_th_rn_fileChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_rn_fileChecker_1.setIncludeOnlyIfFileHasDescription(true);
    _jspx_th_rn_fileChecker_1.setInvert(true);
    int _jspx_eval_rn_fileChecker_1 = _jspx_th_rn_fileChecker_1.doStartTag();
    if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_fileChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_fileChecker_1.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_link_4(_jspx_th_rn_fileChecker_1, _jspx_page_context))
          return true;
        out.write("\n                      ");
        int evalDoAfterBody = _jspx_th_rn_fileChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_fileChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_1);
    return false;
  }

  private boolean _jspx_meth_rn_link_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_4 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
    _jspx_th_rn_link_4.setHref("/lab/modifyfiledescription.jsp");
    int _jspx_eval_rn_link_4 = _jspx_th_rn_link_4.doStartTag();
    if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_4.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_4(_jspx_th_rn_link_4, _jspx_page_context))
          return true;
        out.write("add description\n                        ");
        int evalDoAfterBody = _jspx_th_rn_link_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_4);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_4 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_4);
    _jspx_th_rn_fileParam_4.setName("selectedFile");
    int _jspx_eval_rn_fileParam_4 = _jspx_th_rn_fileParam_4.doStartTag();
    if (_jspx_th_rn_fileParam_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_4);
    return false;
  }

  private boolean _jspx_meth_ctl_errorChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:errorChecker
    org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
    _jspx_th_ctl_errorChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_errorChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
    _jspx_th_ctl_errorChecker_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${actionDateField}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_errorChecker_3 = _jspx_th_ctl_errorChecker_3.doStartTag();
    if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_errorChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_errorChecker_3.doInitBody();
      }
      do {
        out.write("\n                    <td class=\"common\">\n                        ");
        if (_jspx_meth_rn_link_5(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_link_6(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_link_7(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                    <td class=\"common\">\n                      ");
        if (_jspx_meth_rn_fileChecker_2(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
          return true;
        out.write("\n                      ");
        if (_jspx_meth_rn_fileChecker_3(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
          return true;
        out.write("\n                    </td>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_errorChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_errorChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_errorChecker_errorSupplier.reuse(_jspx_th_ctl_errorChecker_3);
    return false;
  }

  private boolean _jspx_meth_rn_link_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_5 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_visible_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_rn_link_5.setHref("/lab/removefiles.jsp");
    _jspx_th_rn_link_5.setVisible(false);
    int _jspx_eval_rn_link_5 = _jspx_th_rn_link_5.doStartTag();
    if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_5.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_5(_jspx_th_rn_link_5, _jspx_page_context))
          return true;
        out.write("remove\n                        ");
        int evalDoAfterBody = _jspx_th_rn_link_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_visible_href.reuse(_jspx_th_rn_link_5);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_5 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_5);
    _jspx_th_rn_fileParam_5.setName("filename");
    int _jspx_eval_rn_fileParam_5 = _jspx_th_rn_fileParam_5.doStartTag();
    if (_jspx_th_rn_fileParam_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_5);
    return false;
  }

  private boolean _jspx_meth_rn_link_6(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_6 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_visible_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_rn_link_6.setHref("/lab/renamefile.jsp");
    _jspx_th_rn_link_6.setVisible(false);
    int _jspx_eval_rn_link_6 = _jspx_th_rn_link_6.doStartTag();
    if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_6.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_fileParam_6(_jspx_th_rn_link_6, _jspx_page_context))
          return true;
        out.write("rename\n                      ");
        int evalDoAfterBody = _jspx_th_rn_link_6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_visible_href.reuse(_jspx_th_rn_link_6);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_6 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_6);
    _jspx_th_rn_fileParam_6.setName("selectedFile");
    int _jspx_eval_rn_fileParam_6 = _jspx_th_rn_fileParam_6.doStartTag();
    if (_jspx_th_rn_fileParam_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_6);
    return false;
  }

  private boolean _jspx_meth_rn_link_7(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_7 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_visible_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_7.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_rn_link_7.setHref("/lab/eradicatefiles.jsp");
    _jspx_th_rn_link_7.setVisible(false);
    int _jspx_eval_rn_link_7 = _jspx_th_rn_link_7.doStartTag();
    if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_7.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_fileParam_7(_jspx_th_rn_link_7, _jspx_page_context))
          return true;
        out.write("eradicate\n                      ");
        int evalDoAfterBody = _jspx_th_rn_link_7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_visible_href.reuse(_jspx_th_rn_link_7);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_7 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_7.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_7);
    _jspx_th_rn_fileParam_7.setName("filename");
    int _jspx_eval_rn_fileParam_7 = _jspx_th_rn_fileParam_7.doStartTag();
    if (_jspx_th_rn_fileParam_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_7);
    return false;
  }

  private boolean _jspx_meth_rn_fileChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileChecker
    org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_2 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
    _jspx_th_rn_fileChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_rn_fileChecker_2.setIncludeOnlyIfFileHasDescription(true);
    int _jspx_eval_rn_fileChecker_2 = _jspx_th_rn_fileChecker_2.doStartTag();
    if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_fileChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_fileChecker_2.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_link_8(_jspx_th_rn_fileChecker_2, _jspx_page_context))
          return true;
        out.write("\n                      ");
        int evalDoAfterBody = _jspx_th_rn_fileChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_fileChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileChecker_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_link_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_8 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_visible_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_8.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_2);
    _jspx_th_rn_link_8.setHref("/lab/modifyfiledescription.jsp");
    _jspx_th_rn_link_8.setVisible(false);
    int _jspx_eval_rn_link_8 = _jspx_th_rn_link_8.doStartTag();
    if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_8.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_8(_jspx_th_rn_link_8, _jspx_page_context))
          return true;
        out.write("\n                          edit description\n                        ");
        int evalDoAfterBody = _jspx_th_rn_link_8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_visible_href.reuse(_jspx_th_rn_link_8);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_8 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_8.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_8);
    _jspx_th_rn_fileParam_8.setName("selectedFile");
    int _jspx_eval_rn_fileParam_8 = _jspx_th_rn_fileParam_8.doStartTag();
    if (_jspx_th_rn_fileParam_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_8);
    return false;
  }

  private boolean _jspx_meth_rn_fileChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileChecker
    org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_3 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription.get(org.recipnet.site.content.rncontrols.FileChecker.class);
    _jspx_th_rn_fileChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_rn_fileChecker_3.setIncludeOnlyIfFileHasDescription(true);
    _jspx_th_rn_fileChecker_3.setInvert(true);
    int _jspx_eval_rn_fileChecker_3 = _jspx_th_rn_fileChecker_3.doStartTag();
    if (_jspx_eval_rn_fileChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_fileChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_fileChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_fileChecker_3.doInitBody();
      }
      do {
        out.write("\n                        ");
        if (_jspx_meth_rn_link_9(_jspx_th_rn_fileChecker_3, _jspx_page_context))
          return true;
        out.write("\n                      ");
        int evalDoAfterBody = _jspx_th_rn_fileChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_fileChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_fileChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileChecker_invert_includeOnlyIfFileHasDescription.reuse(_jspx_th_rn_fileChecker_3);
    return false;
  }

  private boolean _jspx_meth_rn_link_9(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_9 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_visible_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_9.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_3);
    _jspx_th_rn_link_9.setHref("/lab/modifyfiledescription.jsp");
    _jspx_th_rn_link_9.setVisible(false);
    int _jspx_eval_rn_link_9 = _jspx_th_rn_link_9.doStartTag();
    if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_9.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_rn_fileParam_9(_jspx_th_rn_link_9, _jspx_page_context))
          return true;
        out.write("add description\n                        ");
        int evalDoAfterBody = _jspx_th_rn_link_9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_visible_href.reuse(_jspx_th_rn_link_9);
    return false;
  }

  private boolean _jspx_meth_rn_fileParam_9(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:fileParam
    org.recipnet.site.content.rncontrols.FileParam _jspx_th_rn_fileParam_9 = (org.recipnet.site.content.rncontrols.FileParam) _jspx_tagPool_rn_fileParam_name_nobody.get(org.recipnet.site.content.rncontrols.FileParam.class);
    _jspx_th_rn_fileParam_9.setPageContext(_jspx_page_context);
    _jspx_th_rn_fileParam_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_9);
    _jspx_th_rn_fileParam_9.setName("selectedFile");
    int _jspx_eval_rn_fileParam_9 = _jspx_th_rn_fileParam_9.doStartTag();
    if (_jspx_th_rn_fileParam_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_fileParam_name_nobody.reuse(_jspx_th_rn_fileParam_9);
    return false;
  }

  private boolean _jspx_meth_rn_link_10(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_10 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_10.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_10.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_10 = _jspx_th_rn_link_10.doStartTag();
    if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_10.doInitBody();
      }
      do {
        out.write("Back to \"Edit Sample\"...");
        int evalDoAfterBody = _jspx_th_rn_link_10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_10);
    return false;
  }

  private boolean _jspx_meth_rn_link_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_11 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_11.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_11.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_11 = _jspx_th_rn_link_11.doStartTag();
    if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_11.doInitBody();
      }
      do {
        out.write("JaMM visualization...");
        int evalDoAfterBody = _jspx_th_rn_link_11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_11);
    return false;
  }

  private boolean _jspx_meth_rn_link_12(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_12 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_12.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_12.setHref("/lab/uploadfilesform.jsp");
    int _jspx_eval_rn_link_12 = _jspx_th_rn_link_12.doStartTag();
    if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_12.doInitBody();
      }
      do {
        out.write("\n        Upload files (form-based)...\n      ");
        int evalDoAfterBody = _jspx_th_rn_link_12.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_12);
    return false;
  }

  private boolean _jspx_meth_rn_link_13(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_13 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_13.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_13.setHref("/lab/uploadfilesapplet.jsp");
    int _jspx_eval_rn_link_13 = _jspx_th_rn_link_13.doStartTag();
    if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_13.doInitBody();
      }
      do {
        out.write("\n        Upload files (Drag-and-Drop)...\n      ");
        int evalDoAfterBody = _jspx_th_rn_link_13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_link_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_link_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_link_href.reuse(_jspx_th_rn_link_13);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_selectFilesForActionPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_selectFilesForActionPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    p.errorMessage { color: red; }\n    table.table { border: 3px solid #32357D; }\n    th.tableHeader { padding: 0.25em; background: #32357D; \n        font-family: Arial, Helvetica, Verdana; font-weight: bold; \n        font-style: normal; color: #FFFFFF; text-align: left }\n    td.tableLabels { padding: 0.25em; background: #656BFA; \n        font-family: Arial, Helvetica, Verdana; font-style: italic; \n        color: #FFFFFF; text-align: center; }\n    td.tableInstructions { padding: 0.25em; background: #CADBFC;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n    td.common { padding: 0.25em; vertical-align: top; }\n    td.nofiles { padding: 0.25em; color: #707070; font-style: italic;\n        text-align: center; }\n    tr.oddRow { background: #F0F0F0; text-align: center;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n    tr.evenRow { background: #D0D0D0; text-align: center;\n        font-family: Arial, Helvetica, Verdana; color: #000050; }\n    font.normal { font-family: Arial, Helvetica, Verdana; font-style: normal; }\n");
        out.write("    font.description { font-family: Arial, Helvetica, Verdana;\n        font-style: italic; font-size: small; color: #505050; }\n  ");
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
