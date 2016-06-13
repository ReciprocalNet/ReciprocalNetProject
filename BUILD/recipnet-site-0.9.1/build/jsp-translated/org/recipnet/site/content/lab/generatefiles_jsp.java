package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.common.controls.HtmlPageCounter;
import org.recipnet.site.content.rncontrols.FileField;

public final class generatefiles_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_0;
static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_1;

static {
  _jspx_fnmap_0= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("rn:testParity", org.recipnet.site.content.rncontrols.ElFunctions.class, "testParity", new Class[] {int.class, java.lang.String.class, java.lang.String.class});
  _jspx_fnmap_1= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("rn:replaceTail", org.recipnet.site.content.rncontrols.ElFunctions.class, "replaceTail", new Class[] {java.lang.String.class, java.lang.String.class, java.lang.String.class});
}

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_sortFilesByName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_counter_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_requiredExtension;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_increment_counter_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_cifName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_crtFile_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_sdtName_label_crtName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob_forbiddenFilenameGlob;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifChooser_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_crtName_cifName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_sortFilesByName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_counter_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_requiredExtension = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_increment_counter_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_cifName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_crtFile_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_sdtName_label_crtName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob_forbiddenFilenameGlob = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifChooser_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_crtName_cifName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.release();
    _jspx_tagPool_rn_fileIterator_sortFilesByName.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_counter_id_nobody.release();
    _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.release();
    _jspx_tagPool_rn_fileChecker_requiredExtension.release();
    _jspx_tagPool_ctl_increment_counter_nobody.release();
    _jspx_tagPool_ctl_phaseEvent_onPhases.release();
    _jspx_tagPool_rn_buttonLink_target_label_cifName_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_buttonLink_target_label_crtFile_nobody.release();
    _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob.release();
    _jspx_tagPool_rn_buttonLink_target_sdtName_label_crtName_nobody.release();
    _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob_forbiddenFilenameGlob.release();
    _jspx_tagPool_rn_cifChooser_id_nobody.release();
    _jspx_tagPool_rn_buttonLink_target_label_crtName_cifName_nobody.release();
    _jspx_tagPool_rn_link_href.release();
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

      out.write("\n\n\n\n\n\n");
      //  rn:samplePage
      org.recipnet.site.content.rncontrols.SamplePage _jspx_th_rn_samplePage_0 = (org.recipnet.site.content.rncontrols.SamplePage) _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.SamplePage.class);
      _jspx_th_rn_samplePage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_samplePage_0.setParent(null);
      _jspx_th_rn_samplePage_0.setTitle("Generate Files");
      _jspx_th_rn_samplePage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_samplePage_0.setIgnoreSampleHistoryId(true);
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
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
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
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
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
              out.write("\n  <div class=\"bodyDiv\">\n    <div class=\"sideBar\">\n      <span class=\"fieldLabel\">Files present in the repository:</span>\n      ");
              //  rn:fileIterator
              org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName.get(org.recipnet.site.content.rncontrols.FileIterator.class);
              _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_rn_fileIterator_0.setSortFilesByName(true);
              int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
              if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_fileIterator_0.doInitBody();
                }
                do {
                  out.write("\n        <div class=\"fileName\">");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                  _jspx_th_rn_fileField_0.setFieldCode( FileField.FILENAME );
                  int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                  if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                  out.write("</div>\n        <div class=\"fileDescription\">");
                  //  rn:fileField
                  org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                  _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                  _jspx_th_rn_fileField_1.setFieldCode( FileField.DESCRIPTION);
                  int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                  if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                  out.write("</div>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_fileIterator_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_fileIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_fileIterator_sortFilesByName.reuse(_jspx_th_rn_fileIterator_0);
              out.write("\n    </div>\n    ");
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
                  out.write("\n      <table class=\"actionTable\" cellspacing=\"0\">\n        <tr>\n          <th colspan=\"2\" class=\"tableTitle\">Available CRT file generation\n           actions:</th>\n        </tr>\n        <tr>\n          <th class=\"columnLabel\" colspan=\"2\">Use these actions to create CRT\n            files in this sample's file repository.  You will be directed to a\n            form on which you can name, describe, preview, and save the CRT\n            model.</th>\n        </tr>\n        ");
                  //  ctl:counter
                  org.recipnet.common.controls.HtmlPageCounter cifRowCounter = null;
                  org.recipnet.common.controls.HtmlPageCounter _jspx_th_ctl_counter_0 = (org.recipnet.common.controls.HtmlPageCounter) _jspx_tagPool_ctl_counter_id_nobody.get(org.recipnet.common.controls.HtmlPageCounter.class);
                  _jspx_th_ctl_counter_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_counter_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_counter_0.setId("cifRowCounter");
                  int _jspx_eval_ctl_counter_0 = _jspx_th_ctl_counter_0.doStartTag();
                  if (_jspx_th_ctl_counter_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cifRowCounter = (org.recipnet.common.controls.HtmlPageCounter) _jspx_page_context.findAttribute("cifRowCounter");
                  _jspx_tagPool_ctl_counter_id_nobody.reuse(_jspx_th_ctl_counter_0);
                  out.write("\n        ");
                  //  rn:fileIterator
                  org.recipnet.site.content.rncontrols.FileIterator cifFileIt = null;
                  org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_1 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                  _jspx_th_rn_fileIterator_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_fileIterator_1.setId("cifFileIt");
                  _jspx_th_rn_fileIterator_1.setSortFilesByName(true);
                  _jspx_th_rn_fileIterator_1.setRequestUnavailableFiles(false);
                  int _jspx_eval_rn_fileIterator_1 = _jspx_th_rn_fileIterator_1.doStartTag();
                  if (_jspx_eval_rn_fileIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_fileIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_fileIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_fileIterator_1.doInitBody();
                    }
                    cifFileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("cifFileIt");
                    do {
                      out.write("\n          ");
                      //  rn:fileChecker
                      org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_requiredExtension.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                      _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_1);
                      _jspx_th_rn_fileChecker_0.setRequiredExtension(".cif");
                      int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
                      if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n            ");
                          if (_jspx_meth_ctl_increment_0(_jspx_th_rn_fileChecker_0, _jspx_page_context))
                            return;
                          out.write("\n            ");
                          if (_jspx_meth_ctl_phaseEvent_0(_jspx_th_rn_fileChecker_0, _jspx_page_context))
                            return;
                          out.write("\n            ");
                          if (_jspx_meth_ctl_phaseEvent_1(_jspx_th_rn_fileChecker_0, _jspx_page_context))
                            return;
                          out.write("\n            <tr class=\"");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(cifRowCounter.count, 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                          out.write("\">\n              <td>Generate a CRT file from\n                <span style=\"font-weight: bold;\">");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                          _jspx_th_rn_fileField_2.setFieldCode(FileField.FILENAME);
                          int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                          if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                          out.write("</span></td>\n              <td class=\"actionButton\">");
                          if (_jspx_meth_rn_buttonLink_0(_jspx_th_rn_fileChecker_0, _jspx_page_context))
                            return;
                          out.write("</td>\n            </tr>\n          ");
                          int evalDoAfterBody = _jspx_th_rn_fileChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_fileChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileChecker_requiredExtension.reuse(_jspx_th_rn_fileChecker_0);
                      out.write("\n        ");
                      int evalDoAfterBody = _jspx_th_rn_fileIterator_1.doAfterBody();
                      cifFileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("cifFileIt");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_fileIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_fileIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cifFileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("cifFileIt");
                  _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.reuse(_jspx_th_rn_fileIterator_1);
                  out.write("\n        ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifRowCounter}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_0.setErrorFilter(HtmlPageCounter.NEVER_INCREMENTED);
                  int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_0.doInitBody();
                    }
                    do {
                      out.write("\n          <tr class=\"oddRow\">\n            <td class=\"nofiles\" colspan=\"2\">none currently available</td>\n          </tr>\n        ");
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
                  out.write("\n        <tr>\n          <th colspan=\"2\" class=\"tableTitle\">Available PDB file generation\n           actions:</th>\n        </tr>\n        <tr>\n          <th class=\"columnLabel\" colspan=\"2\">Use these actions to create PDB\n            files in this sample's file repository corresponding to CRT files\n            in the repository.  You will be directed to a form on which you can\n            name, describe, and save the PDB file.</th>\n        </tr>\n        ");
                  //  ctl:counter
                  org.recipnet.common.controls.HtmlPageCounter pdbRowCounter = null;
                  org.recipnet.common.controls.HtmlPageCounter _jspx_th_ctl_counter_1 = (org.recipnet.common.controls.HtmlPageCounter) _jspx_tagPool_ctl_counter_id_nobody.get(org.recipnet.common.controls.HtmlPageCounter.class);
                  _jspx_th_ctl_counter_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_counter_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_counter_1.setId("pdbRowCounter");
                  int _jspx_eval_ctl_counter_1 = _jspx_th_ctl_counter_1.doStartTag();
                  if (_jspx_th_ctl_counter_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  pdbRowCounter = (org.recipnet.common.controls.HtmlPageCounter) _jspx_page_context.findAttribute("pdbRowCounter");
                  _jspx_tagPool_ctl_counter_id_nobody.reuse(_jspx_th_ctl_counter_1);
                  out.write("\n        ");
                  //  rn:fileIterator
                  org.recipnet.site.content.rncontrols.FileIterator crtIt1 = null;
                  org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_2 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                  _jspx_th_rn_fileIterator_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileIterator_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_fileIterator_2.setId("crtIt1");
                  _jspx_th_rn_fileIterator_2.setSortFilesByName(true);
                  _jspx_th_rn_fileIterator_2.setRequestUnavailableFiles(false);
                  int _jspx_eval_rn_fileIterator_2 = _jspx_th_rn_fileIterator_2.doStartTag();
                  if (_jspx_eval_rn_fileIterator_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_fileIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_fileIterator_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_fileIterator_2.doInitBody();
                    }
                    crtIt1 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtIt1");
                    do {
                      out.write("\n          ");
                      //  rn:fileChecker
                      org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_1 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_requiredExtension.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                      _jspx_th_rn_fileChecker_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_2);
                      _jspx_th_rn_fileChecker_1.setRequiredExtension(".crt");
                      int _jspx_eval_rn_fileChecker_1 = _jspx_th_rn_fileChecker_1.doStartTag();
                      if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileChecker_1.doInitBody();
                        }
                        do {
                          out.write("\n            ");
                          if (_jspx_meth_ctl_increment_1(_jspx_th_rn_fileChecker_1, _jspx_page_context))
                            return;
                          out.write("\n            ");
                          if (_jspx_meth_ctl_phaseEvent_2(_jspx_th_rn_fileChecker_1, _jspx_page_context))
                            return;
                          out.write("\n            ");
                          if (_jspx_meth_ctl_phaseEvent_3(_jspx_th_rn_fileChecker_1, _jspx_page_context))
                            return;
                          out.write("\n            <tr class=\"");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(pdbRowCounter.count, 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                          out.write("\">\n              <td>Generate a PDB file from\n                <span style=\"font-weight: bold;\">");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_3 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_3.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
                          _jspx_th_rn_fileField_3.setFieldCode(FileField.FILENAME);
                          int _jspx_eval_rn_fileField_3 = _jspx_th_rn_fileField_3.doStartTag();
                          if (_jspx_th_rn_fileField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_3);
                          out.write("</span></td>\n              <td class=\"actionButton\">");
                          if (_jspx_meth_rn_buttonLink_1(_jspx_th_rn_fileChecker_1, _jspx_page_context))
                            return;
                          out.write("</td>\n            </tr>\n          ");
                          int evalDoAfterBody = _jspx_th_rn_fileChecker_1.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_fileChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_fileChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileChecker_requiredExtension.reuse(_jspx_th_rn_fileChecker_1);
                      out.write("\n        ");
                      int evalDoAfterBody = _jspx_th_rn_fileIterator_2.doAfterBody();
                      crtIt1 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtIt1");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_fileIterator_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_fileIterator_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  crtIt1 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtIt1");
                  _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.reuse(_jspx_th_rn_fileIterator_2);
                  out.write("\n        ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pdbRowCounter}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(HtmlPageCounter.NEVER_INCREMENTED);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n          <tr class=\"oddRow\">\n            <td class=\"nofiles\" colspan=\"2\">none currently available</td>\n          </tr>\n        ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
                  out.write("\n        <tr>\n          <th colspan=\"2\" class=\"tableTitle\">Available ORTEP instruction\n           generation actions:</th>\n        </tr>\n        <tr>\n          <th class=\"columnLabel\" colspan=\"2\">Use these actions to create and\n          download an ORTEP instruction file containing the default instructions\n          used by JaMM to create ORTEP illustrations for the model described by\n          the indicated CRT file.  You can tune the ORTEP details by modifying\n          the instructions and uploading them to the repository.  There is no\n          need to generate or upload ORTEP instructions if do not intend to\n          customize them.</th>\n        </tr>\n        ");
                  //  ctl:counter
                  org.recipnet.common.controls.HtmlPageCounter ortRowCounter = null;
                  org.recipnet.common.controls.HtmlPageCounter _jspx_th_ctl_counter_2 = (org.recipnet.common.controls.HtmlPageCounter) _jspx_tagPool_ctl_counter_id_nobody.get(org.recipnet.common.controls.HtmlPageCounter.class);
                  _jspx_th_ctl_counter_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_counter_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_counter_2.setId("ortRowCounter");
                  int _jspx_eval_ctl_counter_2 = _jspx_th_ctl_counter_2.doStartTag();
                  if (_jspx_th_ctl_counter_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  ortRowCounter = (org.recipnet.common.controls.HtmlPageCounter) _jspx_page_context.findAttribute("ortRowCounter");
                  _jspx_tagPool_ctl_counter_id_nobody.reuse(_jspx_th_ctl_counter_2);
                  out.write("\n        ");
                  if (_jspx_meth_ctl_phaseEvent_4(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n        ");
                  //  rn:fileIterator
                  org.recipnet.site.content.rncontrols.FileIterator crtFileIt2 = null;
                  org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_3 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                  _jspx_th_rn_fileIterator_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileIterator_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_fileIterator_3.setId("crtFileIt2");
                  _jspx_th_rn_fileIterator_3.setSortFilesByName(true);
                  _jspx_th_rn_fileIterator_3.setRequestUnavailableFiles(false);
                  int _jspx_eval_rn_fileIterator_3 = _jspx_th_rn_fileIterator_3.doStartTag();
                  if (_jspx_eval_rn_fileIterator_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_fileIterator_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_fileIterator_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_fileIterator_3.doInitBody();
                    }
                    crtFileIt2 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtFileIt2");
                    do {
                      out.write("\n          ");
                      //  rn:fileChecker
                      org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_2 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_requiredExtension.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                      _jspx_th_rn_fileChecker_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_3);
                      _jspx_th_rn_fileChecker_2.setRequiredExtension(".crt");
                      int _jspx_eval_rn_fileChecker_2 = _jspx_th_rn_fileChecker_2.doStartTag();
                      if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileChecker_2.doInitBody();
                        }
                        do {
                          out.write("\n            ");
                          if (_jspx_meth_ctl_phaseEvent_5(_jspx_th_rn_fileChecker_2, _jspx_page_context))
                            return;
                          out.write("\n            ");
                          //  rn:multiFilenameChecker
                          org.recipnet.site.content.rncontrols.MultiFilenameChecker _jspx_th_rn_multiFilenameChecker_0 = (org.recipnet.site.content.rncontrols.MultiFilenameChecker) _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob.get(org.recipnet.site.content.rncontrols.MultiFilenameChecker.class);
                          _jspx_th_rn_multiFilenameChecker_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_multiFilenameChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_2);
                          _jspx_th_rn_multiFilenameChecker_0.setRequiredFilenameGlob((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sdtName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          int _jspx_eval_rn_multiFilenameChecker_0 = _jspx_th_rn_multiFilenameChecker_0.doStartTag();
                          if (_jspx_eval_rn_multiFilenameChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_multiFilenameChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_multiFilenameChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_multiFilenameChecker_0.doInitBody();
                            }
                            do {
                              out.write("\n              ");
                              if (_jspx_meth_ctl_increment_2(_jspx_th_rn_multiFilenameChecker_0, _jspx_page_context))
                              return;
                              out.write("\n              <tr\n                class=\"");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                              out.write("\">\n                <td>Generate ORTEP instructions from\n                  <span style=\"font-weight: bold;\">");
                              //  rn:fileField
                              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_4 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                              _jspx_th_rn_fileField_4.setPageContext(_jspx_page_context);
                              _jspx_th_rn_fileField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_0);
                              _jspx_th_rn_fileField_4.setFieldCode(FileField.FILENAME);
                              int _jspx_eval_rn_fileField_4 = _jspx_th_rn_fileField_4.doStartTag();
                              if (_jspx_th_rn_fileField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_4);
                              out.write("</span> and\n                  <span style=\"font-weight: bold;\">");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sdtName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                              out.write("</span></td>\n                <td class=\"actionButton\">");
                              if (_jspx_meth_rn_buttonLink_2(_jspx_th_rn_multiFilenameChecker_0, _jspx_page_context))
                              return;
                              out.write("</td>\n              </tr>\n            ");
                              int evalDoAfterBody = _jspx_th_rn_multiFilenameChecker_0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_multiFilenameChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_multiFilenameChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob.reuse(_jspx_th_rn_multiFilenameChecker_0);
                          out.write("\n            ");
                          //  rn:multiFilenameChecker
                          org.recipnet.site.content.rncontrols.MultiFilenameChecker _jspx_th_rn_multiFilenameChecker_1 = (org.recipnet.site.content.rncontrols.MultiFilenameChecker) _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob_forbiddenFilenameGlob.get(org.recipnet.site.content.rncontrols.MultiFilenameChecker.class);
                          _jspx_th_rn_multiFilenameChecker_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_multiFilenameChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_2);
                          _jspx_th_rn_multiFilenameChecker_1.setRequiredFilenameGlob("*.cif");
                          _jspx_th_rn_multiFilenameChecker_1.setForbiddenFilenameGlob((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sdtName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          int _jspx_eval_rn_multiFilenameChecker_1 = _jspx_th_rn_multiFilenameChecker_1.doStartTag();
                          if (_jspx_eval_rn_multiFilenameChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_multiFilenameChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_multiFilenameChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_multiFilenameChecker_1.doInitBody();
                            }
                            do {
                              out.write("\n              ");
                              //  rn:cifChooser
                              org.recipnet.site.content.rncontrols.PreferredCifChooser cifChooser = null;
                              org.recipnet.site.content.rncontrols.PreferredCifChooser _jspx_th_rn_cifChooser_0 = (org.recipnet.site.content.rncontrols.PreferredCifChooser) _jspx_tagPool_rn_cifChooser_id_nobody.get(org.recipnet.site.content.rncontrols.PreferredCifChooser.class);
                              _jspx_th_rn_cifChooser_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_cifChooser_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_1);
                              _jspx_th_rn_cifChooser_0.setId("cifChooser");
                              int _jspx_eval_rn_cifChooser_0 = _jspx_th_rn_cifChooser_0.doStartTag();
                              if (_jspx_th_rn_cifChooser_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              cifChooser = (org.recipnet.site.content.rncontrols.PreferredCifChooser) _jspx_page_context.findAttribute("cifChooser");
                              _jspx_tagPool_rn_cifChooser_id_nobody.reuse(_jspx_th_rn_cifChooser_0);
                              out.write("\n              ");
                              if (_jspx_meth_ctl_increment_3(_jspx_th_rn_multiFilenameChecker_1, _jspx_page_context))
                              return;
                              out.write("\n              <tr\n                class=\"");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(ortRowCounter.count, 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                              out.write("\">\n                <td>Generate ORTEP instructions from\n                  <span style=\"font-weight: bold;\">");
                              //  rn:fileField
                              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_5 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                              _jspx_th_rn_fileField_5.setPageContext(_jspx_page_context);
                              _jspx_th_rn_fileField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_1);
                              _jspx_th_rn_fileField_5.setFieldCode(FileField.FILENAME);
                              int _jspx_eval_rn_fileField_5 = _jspx_th_rn_fileField_5.doStartTag();
                              if (_jspx_th_rn_fileField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_5);
                              out.write("</span> and\n                  <span style=\"font-weight: bold;\">");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                              out.write("</span></td>\n                <td class=\"actionButton\">");
                              if (_jspx_meth_rn_buttonLink_3(_jspx_th_rn_multiFilenameChecker_1, _jspx_page_context))
                              return;
                              out.write("</td>\n              </tr>\n            ");
                              int evalDoAfterBody = _jspx_th_rn_multiFilenameChecker_1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_multiFilenameChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_multiFilenameChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_multiFilenameChecker_requiredFilenameGlob_forbiddenFilenameGlob.reuse(_jspx_th_rn_multiFilenameChecker_1);
                          out.write("\n          ");
                          int evalDoAfterBody = _jspx_th_rn_fileChecker_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_fileChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_fileChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_fileChecker_requiredExtension.reuse(_jspx_th_rn_fileChecker_2);
                      out.write("\n        ");
                      int evalDoAfterBody = _jspx_th_rn_fileIterator_3.doAfterBody();
                      crtFileIt2 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtFileIt2");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_fileIterator_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_fileIterator_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  crtFileIt2 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("crtFileIt2");
                  _jspx_tagPool_rn_fileIterator_sortFilesByName_requestUnavailableFiles_id.reuse(_jspx_th_rn_fileIterator_3);
                  out.write("\n        ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ortRowCounter}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_2.setErrorFilter(HtmlPageCounter.NEVER_INCREMENTED);
                  int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_2.doInitBody();
                    }
                    do {
                      out.write("\n          <tr class=\"oddRow\">\n            <td class=\"nofiles\" colspan=\"2\">none currently available</td>\n          </tr>\n        ");
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
                  out.write("\n      </table>\n    ");
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
              out.write("\n  </div>\n  <p class=\"navlinks\">");
              if (_jspx_meth_rn_link_0(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("<br />\n  ");
              if (_jspx_meth_rn_link_1(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("<br />\n  ");
              if (_jspx_meth_rn_link_2(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("<br />\n  ");
              if (_jspx_meth_rn_link_3(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("<br />\n  ");
              if (_jspx_meth_rn_link_4(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
                return;
              out.write("</p>\n  ");
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_samplePage_0, _jspx_page_context))
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
      _jspx_tagPool_rn_samplePage_title_loginPageUrl_ignoreSampleHistoryId_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_samplePage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <p class=\"errorMessage\" style=\"text-align: center\">This sample has been\n    retracted by its lab and cannot have file actions performed on it.</p>\n  ");
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

  private boolean _jspx_meth_ctl_increment_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:increment
    org.recipnet.common.controls.HtmlPageCounterIncrementor _jspx_th_ctl_increment_0 = (org.recipnet.common.controls.HtmlPageCounterIncrementor) _jspx_tagPool_ctl_increment_counter_nobody.get(org.recipnet.common.controls.HtmlPageCounterIncrementor.class);
    _jspx_th_ctl_increment_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_increment_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
    _jspx_th_ctl_increment_0.setCounter((org.recipnet.common.controls.HtmlPageCounter) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifRowCounter}", org.recipnet.common.controls.HtmlPageCounter.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_increment_0 = _jspx_th_ctl_increment_0.doStartTag();
    if (_jspx_th_ctl_increment_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_increment_counter_nobody.reuse(_jspx_th_ctl_increment_0);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_0 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
    _jspx_th_ctl_phaseEvent_0.setOnPhases("registration");
    int _jspx_eval_ctl_phaseEvent_0 = _jspx_th_ctl_phaseEvent_0.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_0.doInitBody();
      }
      do {
        out.write("\n              ");
        org.recipnet.site.wrapper.StringBean cifName = null;
        synchronized (_jspx_page_context) {
          cifName = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("cifName", PageContext.PAGE_SCOPE);
          if (cifName == null){
            cifName = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("cifName", cifName, PageContext.PAGE_SCOPE);
            out.write("\n                ");
            org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("cifName"), "string", "", null, null, false);
            out.write("\n              ");
          }
        }
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_0);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_1 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
    _jspx_th_ctl_phaseEvent_1.setOnPhases("processing,rendering");
    int _jspx_eval_ctl_phaseEvent_1 = _jspx_th_ctl_phaseEvent_1.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_1.doInitBody();
      }
      do {
        out.write("\n              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("cifName"), "string", "${cifFileIt.sampleDataFile.name}", _jspx_page_context, null);
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_1);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_0 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_cifName_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
    _jspx_th_rn_buttonLink_0.setLabel("Generate CRT file");
    _jspx_th_rn_buttonLink_0.setTarget("/lab/generatecrt.jsp");
    _jspx_th_rn_buttonLink_0.setDynamicAttribute(null, "cifName", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_buttonLink_0 = _jspx_th_rn_buttonLink_0.doStartTag();
    if (_jspx_th_rn_buttonLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_cifName_nobody.reuse(_jspx_th_rn_buttonLink_0);
    return false;
  }

  private boolean _jspx_meth_ctl_increment_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:increment
    org.recipnet.common.controls.HtmlPageCounterIncrementor _jspx_th_ctl_increment_1 = (org.recipnet.common.controls.HtmlPageCounterIncrementor) _jspx_tagPool_ctl_increment_counter_nobody.get(org.recipnet.common.controls.HtmlPageCounterIncrementor.class);
    _jspx_th_ctl_increment_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_increment_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
    _jspx_th_ctl_increment_1.setCounter((org.recipnet.common.controls.HtmlPageCounter) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pdbRowCounter}", org.recipnet.common.controls.HtmlPageCounter.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_increment_1 = _jspx_th_ctl_increment_1.doStartTag();
    if (_jspx_th_ctl_increment_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_increment_counter_nobody.reuse(_jspx_th_ctl_increment_1);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_2 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
    _jspx_th_ctl_phaseEvent_2.setOnPhases("registration");
    int _jspx_eval_ctl_phaseEvent_2 = _jspx_th_ctl_phaseEvent_2.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_2.doInitBody();
      }
      do {
        out.write("\n              ");
        org.recipnet.site.wrapper.StringBean crtName = null;
        synchronized (_jspx_page_context) {
          crtName = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("crtName", PageContext.PAGE_SCOPE);
          if (crtName == null){
            crtName = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("crtName", crtName, PageContext.PAGE_SCOPE);
            out.write("\n                ");
            org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("crtName"), "string", "", null, null, false);
            out.write("\n              ");
          }
        }
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_2);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_3 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
    _jspx_th_ctl_phaseEvent_3.setOnPhases("processing,rendering");
    int _jspx_eval_ctl_phaseEvent_3 = _jspx_th_ctl_phaseEvent_3.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_3.doInitBody();
      }
      do {
        out.write("\n              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("crtName"), "string", "${crtIt1.sampleDataFile.name}", _jspx_page_context, null);
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_3);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_1 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_crtFile_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_1);
    _jspx_th_rn_buttonLink_1.setLabel("Generate PDB file");
    _jspx_th_rn_buttonLink_1.setTarget("/lab/generatepdb.jsp");
    _jspx_th_rn_buttonLink_1.setDynamicAttribute(null, "crtFile", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${crtName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_buttonLink_1 = _jspx_th_rn_buttonLink_1.doStartTag();
    if (_jspx_th_rn_buttonLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_crtFile_nobody.reuse(_jspx_th_rn_buttonLink_1);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_4 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_phaseEvent_4.setOnPhases("registration");
    int _jspx_eval_ctl_phaseEvent_4 = _jspx_th_ctl_phaseEvent_4.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_4.doInitBody();
      }
      do {
        out.write("\n          ");
        org.recipnet.site.wrapper.StringBean crtName2 = null;
        synchronized (_jspx_page_context) {
          crtName2 = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("crtName2", PageContext.PAGE_SCOPE);
          if (crtName2 == null){
            crtName2 = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("crtName2", crtName2, PageContext.PAGE_SCOPE);
            out.write("\n            ");
            org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("crtName2"), "string", "", null, null, false);
            out.write("\n          ");
          }
        }
        out.write("\n          ");
        org.recipnet.site.wrapper.StringBean sdtName = null;
        synchronized (_jspx_page_context) {
          sdtName = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("sdtName", PageContext.PAGE_SCOPE);
          if (sdtName == null){
            sdtName = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("sdtName", sdtName, PageContext.PAGE_SCOPE);
            out.write("\n            ");
            org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("sdtName"), "string", "", null, null, false);
            out.write("\n          ");
          }
        }
        out.write("\n        ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_4);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_fileChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_5 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_2);
    _jspx_th_ctl_phaseEvent_5.setOnPhases("fetching,processing,rendering");
    int _jspx_eval_ctl_phaseEvent_5 = _jspx_th_ctl_phaseEvent_5.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_5.doInitBody();
      }
      do {
        out.write("\n              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("crtName2"), "string", "${crtFileIt2.sampleDataFile.name}", _jspx_page_context, null);
        out.write("\n              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("sdtName"), "string", "${rn:replaceTail(crtName2, '.crt', '.sdt')}", _jspx_page_context, _jspx_fnmap_1);
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_onPhases.reuse(_jspx_th_ctl_phaseEvent_5);
    return false;
  }

  private boolean _jspx_meth_ctl_increment_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_multiFilenameChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:increment
    org.recipnet.common.controls.HtmlPageCounterIncrementor _jspx_th_ctl_increment_2 = (org.recipnet.common.controls.HtmlPageCounterIncrementor) _jspx_tagPool_ctl_increment_counter_nobody.get(org.recipnet.common.controls.HtmlPageCounterIncrementor.class);
    _jspx_th_ctl_increment_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_increment_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_0);
    _jspx_th_ctl_increment_2.setCounter((org.recipnet.common.controls.HtmlPageCounter) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ortRowCounter}", org.recipnet.common.controls.HtmlPageCounter.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_increment_2 = _jspx_th_ctl_increment_2.doStartTag();
    if (_jspx_th_ctl_increment_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_increment_counter_nobody.reuse(_jspx_th_ctl_increment_2);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_multiFilenameChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_2 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_sdtName_label_crtName_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_0);
    _jspx_th_rn_buttonLink_2.setLabel("Generate ORTEP instructions");
    _jspx_th_rn_buttonLink_2.setTarget("/servlet/generateort");
    _jspx_th_rn_buttonLink_2.setDynamicAttribute(null, "crtName", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${crtName2}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_buttonLink_2.setDynamicAttribute(null, "sdtName", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sdtName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_buttonLink_2 = _jspx_th_rn_buttonLink_2.doStartTag();
    if (_jspx_th_rn_buttonLink_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_sdtName_label_crtName_nobody.reuse(_jspx_th_rn_buttonLink_2);
    return false;
  }

  private boolean _jspx_meth_ctl_increment_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_multiFilenameChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:increment
    org.recipnet.common.controls.HtmlPageCounterIncrementor _jspx_th_ctl_increment_3 = (org.recipnet.common.controls.HtmlPageCounterIncrementor) _jspx_tagPool_ctl_increment_counter_nobody.get(org.recipnet.common.controls.HtmlPageCounterIncrementor.class);
    _jspx_th_ctl_increment_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_increment_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_1);
    _jspx_th_ctl_increment_3.setCounter((org.recipnet.common.controls.HtmlPageCounter) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ortRowCounter}", org.recipnet.common.controls.HtmlPageCounter.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_increment_3 = _jspx_th_ctl_increment_3.doStartTag();
    if (_jspx_th_ctl_increment_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_increment_counter_nobody.reuse(_jspx_th_ctl_increment_3);
    return false;
  }

  private boolean _jspx_meth_rn_buttonLink_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_multiFilenameChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_3 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_crtName_cifName_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_multiFilenameChecker_1);
    _jspx_th_rn_buttonLink_3.setLabel("Generate ORTEP instructions");
    _jspx_th_rn_buttonLink_3.setTarget("/servlet/generateort");
    _jspx_th_rn_buttonLink_3.setDynamicAttribute(null, "crtName", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${crtName2}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_buttonLink_3.setDynamicAttribute(null, "cifName", (java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_buttonLink_3 = _jspx_th_rn_buttonLink_3.doStartTag();
    if (_jspx_th_rn_buttonLink_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_crtName_cifName_nobody.reuse(_jspx_th_rn_buttonLink_3);
    return false;
  }

  private boolean _jspx_meth_rn_link_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_0.setHref("/lab/sample.jsp");
    int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_0.doInitBody();
      }
      do {
        out.write("Back to\n   \"Edit Sample\"...");
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

  private boolean _jspx_meth_rn_link_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_1.setHref("/jamm.jsp");
    int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
    if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_1.doInitBody();
      }
      do {
        out.write("JaMM visualization...");
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

  private boolean _jspx_meth_rn_link_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_2.setHref("/lab/uploadfilesform.jsp");
    int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_2.doInitBody();
      }
      do {
        out.write("Upload files\n   (form-based)...");
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

  private boolean _jspx_meth_rn_link_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_3.setHref("/lab/uploadfilesapplet.jsp");
    int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
    if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_3.doInitBody();
      }
      do {
        out.write("Upload files\n   (Drag-and-Drop)...");
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

  private boolean _jspx_meth_rn_link_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:link
    org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_4 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
    _jspx_th_rn_link_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_link_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_rn_link_4.setHref("/lab/managefiles.jsp");
    int _jspx_eval_rn_link_4 = _jspx_th_rn_link_4.doStartTag();
    if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_link_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_link_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_link_4.doInitBody();
      }
      do {
        out.write("Manage files...");
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_samplePage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_samplePage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    div.bodyDiv { font-family: Arial, Helvetica, Verdana; margin: 1em 1em 0 1em}\n    div.sideBar { float: right; width: 300px; margin-left: 1em; }\n    table.actionTable { border: 3px solid #32357D; \n        margin-left: auto; margin-right: auto; max-width: 60em; }\n    table.actionTable th, table.actionTable td { padding: 0.25em }\n    tr.oddRow { background: #F0F0F0; color: #000050; }\n    tr.evenRow { background: #D0D0D0; color: #000050; }\n    th.tableTitle { background: #32357D; color: #FFFFFF;\n        font-weight: bold; text-align: left; }\n    th.columnLabel { background: #656BFA; color: #FFFFFF;\n        font-style: italic; font-weight: normal; text-align: left; }\n    td.actionButton { text-align: center; width: 1px; }\n    td.nofiles { color: #707070; font-style: italic; text-align: center; }\n    div.fileName { margin: 0.25em 0 0 0; }\n    div.fileDescription { font-style: italic; margin: 0 0 0 1em; color: gray; }\n    span.fieldLabel { font-style: normal; font-weight: bold; }\n    p.errorMessage { color: red; }\n");
        out.write("    p.navlinks { text-align: right; margin-top: 0.5em; }\n  ");
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
