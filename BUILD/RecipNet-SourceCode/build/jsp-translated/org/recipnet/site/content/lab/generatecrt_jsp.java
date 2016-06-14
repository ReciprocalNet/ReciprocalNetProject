package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.common.controls.ErrorChecker;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.CifFileContext;
import org.recipnet.site.shared.validation.FilenameValidator;
import org.recipnet.site.shared.validation.ContainerStringValidator;

public final class generatecrt_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

static private org.apache.jasper.runtime.ProtectedFunctionMapper _jspx_fnmap_0;

static {
  _jspx_fnmap_0= org.apache.jasper.runtime.ProtectedFunctionMapper.getMapForFunction("rn:replaceTail", org.recipnet.site.content.rncontrols.ElFunctions.class, "replaceTail", new Class[] {java.lang.String.class, java.lang.String.class, java.lang.String.class});
}

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_generateCrtWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_crtFileName_crtFileKey_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_hiddenString_initialValue_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifFile_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButton_option_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_listbox_multiple_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_option_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_listbox_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_ifValueIsTrue_control;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_generateCrt_keyControl_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_validator_validator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_label_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_checkTrackedFile_requireValid_fileKey;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammModel_modelUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_jammApplet_width_height_border_applet_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_sortFilesByName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_requiredExtension;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_generateCrtWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_crtFileName_crtFileKey_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_hiddenString_initialValue_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifFile_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButton_option_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_listbox_multiple_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_option_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_listbox_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_ifValueIsTrue_control = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_generateCrt_keyControl_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_validator_validator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_label_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_checkTrackedFile_requireValid_fileKey = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammModel_modelUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_jammApplet_width_height_border_applet_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_sortFilesByName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_requiredExtension = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_generateCrtWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_crtFileName_crtFileKey_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.release();
    _jspx_tagPool_ctl_selfForm_id.release();
    _jspx_tagPool_ctl_hiddenString_initialValue_id_nobody.release();
    _jspx_tagPool_rn_cifFile_id.release();
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_ctl_button_label_id_nobody.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.release();
    _jspx_tagPool_ctl_radioButton_option_id.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_ctl_listbox_multiple_id.release();
    _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols.release();
    _jspx_tagPool_ctl_option_label_nobody.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue.release();
    _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.release();
    _jspx_tagPool_ctl_listbox_id.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_ifValueIsTrue_control.release();
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.release();
    _jspx_tagPool_rn_generateCrt_keyControl_id_nobody.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.release();
    _jspx_tagPool_ctl_validator_validator_nobody.release();
    _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns.release();
    _jspx_tagPool_ctl_phaseEvent_onPhases.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_label_editable_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
    _jspx_tagPool_rn_checkTrackedFile_requireValid_fileKey.release();
    _jspx_tagPool_rn_jammModel_modelUrl.release();
    _jspx_tagPool_rn_jammApplet_width_height_border_applet_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier.release();
    _jspx_tagPool_rn_fileIterator_sortFilesByName.release();
    _jspx_tagPool_rn_fileChecker_requiredExtension.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n");
      //  rn:generateCrtWapPage
      org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage _jspx_th_rn_generateCrtWapPage_0 = (org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage) _jspx_tagPool_rn_generateCrtWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_crtFileName_crtFileKey_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage.class);
      _jspx_th_rn_generateCrtWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_generateCrtWapPage_0.setParent(null);
      _jspx_th_rn_generateCrtWapPage_0.setTitle("Generate a CRT File");
      _jspx_th_rn_generateCrtWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_generateCrtWapPage_0.setEditSamplePageHref("/lab/generatefiles.jsp");
      _jspx_th_rn_generateCrtWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_generateCrtWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      _jspx_th_rn_generateCrtWapPage_0.setFileNameParamName("cifName");
      _jspx_th_rn_generateCrtWapPage_0.setCrtFileName((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtName']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      _jspx_th_rn_generateCrtWapPage_0.setCrtFileKey((java.lang.Long) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtKey']}", java.lang.Long.class, (PageContext)_jspx_page_context, null, false));
      int _jspx_eval_rn_generateCrtWapPage_0 = _jspx_th_rn_generateCrtWapPage_0.doStartTag();
      if (_jspx_eval_rn_generateCrtWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage crtPage = null;
        if (_jspx_eval_rn_generateCrtWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_generateCrtWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_generateCrtWapPage_0.doInitBody();
        }
        crtPage = (org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage) _jspx_page_context.findAttribute("crtPage");
        do {
          out.write("\n  <div class=\"bodyDiv\">\n    ");
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generateCrtWapPage_0);
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
              out.write("\n      ");
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
              out.write("\n      ");
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
          out.write(' ');
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_generateCrtWapPage_0, _jspx_page_context))
            return;
          out.write("\n    ");
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generateCrtWapPage_0);
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
              out.write("\n      ");
              out.write("\n      ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement pageForm = null;
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm_id.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_selfForm_0.setId("pageForm");
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                pageForm = (org.recipnet.common.controls.FormHtmlElement) _jspx_page_context.findAttribute("pageForm");
                do {
                  out.write("\n        ");
                  //  ctl:hiddenString
                  org.recipnet.common.controls.HiddenStringHtmlControl crtKey = null;
                  org.recipnet.common.controls.HiddenStringHtmlControl _jspx_th_ctl_hiddenString_0 = (org.recipnet.common.controls.HiddenStringHtmlControl) _jspx_tagPool_ctl_hiddenString_initialValue_id_nobody.get(org.recipnet.common.controls.HiddenStringHtmlControl.class);
                  _jspx_th_ctl_hiddenString_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_hiddenString_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_hiddenString_0.setId("crtKey");
                  _jspx_th_ctl_hiddenString_0.setInitialValue(new String(""));
                  int _jspx_eval_ctl_hiddenString_0 = _jspx_th_ctl_hiddenString_0.doStartTag();
                  if (_jspx_th_ctl_hiddenString_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  crtKey = (org.recipnet.common.controls.HiddenStringHtmlControl) _jspx_page_context.findAttribute("crtKey");
                  _jspx_tagPool_ctl_hiddenString_initialValue_id_nobody.reuse(_jspx_th_ctl_hiddenString_0);
                  out.write("\n        <table class=\"formTable\" border=\"0\" style=\"margin: 0;\">\n          <tr>\n            <td style=\"vertical-align: top;\">\n              <div style=\"margin-bottom: 1em\">Use the controls below to describe\n              the model view for which a CRT should be generated, then click the\n              \"Compute Model\" button to preview the result. When you are\n              satisfied, assign an appropriate file name (and optionally a\n              description), and click the \"Save CRT File\" button to save the CRT\n              to the repository, overwriting any existing file of the same name.\n              Click the \"Cancel\" button to return to the Generate Files page\n              without saving a CRT file to the repository.</div>\n              ");
                  //  rn:cifFile
                  org.recipnet.site.content.rncontrols.CifFileContext cif = null;
                  org.recipnet.site.content.rncontrols.CifFileContext _jspx_th_rn_cifFile_0 = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_tagPool_rn_cifFile_id.get(org.recipnet.site.content.rncontrols.CifFileContext.class);
                  _jspx_th_rn_cifFile_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_cifFile_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_cifFile_0.setId("cif");
                  int _jspx_eval_rn_cifFile_0 = _jspx_th_rn_cifFile_0.doStartTag();
                  if (_jspx_eval_rn_cifFile_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_cifFile_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_cifFile_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_cifFile_0.doInitBody();
                    }
                    cif = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_page_context.findAttribute("cif");
                    do {
                      out.write("\n                <div style=\"text-align: right; vertical-align: top;\">\n                  <div style=\"margin: 0; float: left;\"><span\n                      class=\"fieldLabel\">CIF name:</span>\n                    ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField cifName = null;
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifFile_0);
                      _jspx_th_rn_fileField_0.setId("cifName");
                      _jspx_th_rn_fileField_0.setFieldCode( FileField.FILENAME );
                      int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                      if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      cifName = (org.recipnet.site.content.rncontrols.FileField) _jspx_page_context.findAttribute("cifName");
                      _jspx_tagPool_rn_fileField_id_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                      out.write("\n                    ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifFile_0);
                      _jspx_th_ctl_errorMessage_0.setErrorFilter( CifFileContext.CIF_HAS_ERRORS );
                      int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_0.doInitBody();
                        }
                        do {
                          out.write("\n                      <span class=\"errorNotice\">[contains errors]</span>\n                    ");
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
                      out.write("\n                  </div>\n                  ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifFile_0);
                      _jspx_th_ctl_errorMessage_1.setErrorFilter(
                      CifFileContext.UNPARSEABLE_CIF 
                      | CifFileContext.EMPTY_CIF
                      | CifFileContext.NO_FILE_SPECIFIED );
                      int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_1.doInitBody();
                        }
                        do {
                          out.write("\n                    <span class=\"errorMessage\">CIF is invalid or has no data\n                      blocks</span>\n                  ");
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
                      out.write("\n                  ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifFile_0);
                      _jspx_th_ctl_errorChecker_0.setInvert(true);
                      _jspx_th_ctl_errorChecker_0.setErrorFilter(
                      CifFileContext.UNPARSEABLE_CIF 
                      | CifFileContext.EMPTY_CIF
                      | CifFileContext.NO_FILE_SPECIFIED );
                      int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                    ");
                          //  ctl:button
                          org.recipnet.common.controls.ButtonHtmlControl generateCrtButton = null;
                          org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_id_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                          _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
                          _jspx_th_ctl_button_0.setId("generateCrtButton");
                          _jspx_th_ctl_button_0.setLabel("Compute Model");
                          int _jspx_eval_ctl_button_0 = _jspx_th_ctl_button_0.doStartTag();
                          if (_jspx_th_ctl_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          generateCrtButton = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("generateCrtButton");
                          _jspx_tagPool_ctl_button_label_id_nobody.reuse(_jspx_th_ctl_button_0);
                          out.write("\n                  ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_invert_errorFilter.reuse(_jspx_th_ctl_errorChecker_0);
                      out.write("\n                </div>\n                ");
                      //  ctl:radioButtonGroup
                      org.recipnet.common.controls.RadioButtonGroupHtmlControl modelType = null;
                      org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                      _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifFile_0);
                      _jspx_th_ctl_radioButtonGroup_0.setId("modelType");
                      _jspx_th_ctl_radioButtonGroup_0.setInitialValue(new String("simple"));
                      int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
                      if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                        }
                        modelType = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("modelType");
                        do {
                          out.write("\n                  <div id=\"simpleDiv\" class=\"optionDiv\">");
                          //  ctl:radioButton
                          org.recipnet.common.controls.RadioButtonHtmlControl simpleRadio = null;
                          org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_0 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                          _jspx_th_ctl_radioButton_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_radioButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_radioButton_0.setId("simpleRadio");
                          _jspx_th_ctl_radioButton_0.setOption("simple");
                          int _jspx_eval_ctl_radioButton_0 = _jspx_th_ctl_radioButton_0.doStartTag();
                          if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButton_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButton_0.doInitBody();
                            }
                            simpleRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("simpleRadio");
                            do {
                              out.write("\n                    ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_ctl_radioButton_0, _jspx_page_context))
                              return;
                              out.write("\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButton_0.doAfterBody();
                              simpleRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("simpleRadio");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_radioButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          simpleRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("simpleRadio");
                          _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_0);
                          out.write(" <dfn>Simple model</dfn>. Generate a\n                    model including all atoms from the CIF but not accounting\n                    for any crystallographic symmetry.\n                  </div>\n                  <div id=\"grownDiv\" class=\"optionDiv\">");
                          //  ctl:radioButton
                          org.recipnet.common.controls.RadioButtonHtmlControl grownRadio = null;
                          org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_1 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                          _jspx_th_ctl_radioButton_1.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_radioButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_radioButton_1.setId("grownRadio");
                          _jspx_th_ctl_radioButton_1.setOption("grown");
                          int _jspx_eval_ctl_radioButton_1 = _jspx_th_ctl_radioButton_1.doStartTag();
                          if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButton_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButton_1.doInitBody();
                            }
                            grownRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("grownRadio");
                            do {
                              out.write("\n                    ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_ctl_radioButton_1, _jspx_page_context))
                              return;
                              out.write("\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButton_1.doAfterBody();
                              grownRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("grownRadio");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_radioButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          grownRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("grownRadio");
                          _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_1);
                          out.write(" <dfn>Grown model</dfn>. Generate a model\n                    including the specified atom(s) and all other atoms in the\n                    same chemical moiety(-ies), including symmetry copies as\n                    necessary; truncate extended structures at one unit cell\n                    length in each dimension.<br />\n                    <div class=\"innerDiv\" style=\"margin-right: 0\"><span\n                      style=\"vertical-align: 250%; font-weight:bold;\"> Ensure these atoms are\n                      included:</span>\n                      ");
                          //  ctl:listbox
                          org.recipnet.common.controls.ListboxHtmlControl grownModelSeeds = null;
                          org.recipnet.common.controls.ListboxHtmlControl _jspx_th_ctl_listbox_0 = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_tagPool_ctl_listbox_multiple_id.get(org.recipnet.common.controls.ListboxHtmlControl.class);
                          _jspx_th_ctl_listbox_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_listbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_listbox_0.setId("grownModelSeeds");
                          _jspx_th_ctl_listbox_0.setMultiple(true);
                          int _jspx_eval_ctl_listbox_0 = _jspx_th_ctl_listbox_0.doStartTag();
                          if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_listbox_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_listbox_0.doInitBody();
                            }
                            grownModelSeeds = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("grownModelSeeds");
                            do {
                              out.write("\n                        ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_2(_jspx_th_ctl_listbox_0, _jspx_page_context))
                              return;
                              out.write("\n                        ");
                              //  rn:atomSites
                              org.recipnet.site.content.rncontrols.CifAtomSiteIterator seedSites = null;
                              org.recipnet.site.content.rncontrols.CifAtomSiteIterator _jspx_th_rn_atomSites_0 = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols.get(org.recipnet.site.content.rncontrols.CifAtomSiteIterator.class);
                              _jspx_th_rn_atomSites_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_atomSites_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
                              _jspx_th_rn_atomSites_0.setId("seedSites");
                              _jspx_th_rn_atomSites_0.setExcludedTypeSymbols("H");
                              int _jspx_eval_rn_atomSites_0 = _jspx_th_rn_atomSites_0.doStartTag();
                              if (_jspx_eval_rn_atomSites_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_rn_atomSites_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_atomSites_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_atomSites_0.doInitBody();
                              }
                              seedSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("seedSites");
                              do {
                              out.write("\n                          ");
                              if (_jspx_meth_ctl_option_0(_jspx_th_rn_atomSites_0, _jspx_page_context))
                              return;
                              out.write("\n                        ");
                              int evalDoAfterBody = _jspx_th_rn_atomSites_0.doAfterBody();
                              seedSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("seedSites");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_rn_atomSites_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_rn_atomSites_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              seedSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("seedSites");
                              _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols.reuse(_jspx_th_rn_atomSites_0);
                              out.write("\n                      ");
                              int evalDoAfterBody = _jspx_th_ctl_listbox_0.doAfterBody();
                              grownModelSeeds = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("grownModelSeeds");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_listbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_listbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          grownModelSeeds = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("grownModelSeeds");
                          _jspx_tagPool_ctl_listbox_multiple_id.reuse(_jspx_th_ctl_listbox_0);
                          out.write("\n                    </div>\n                  </div>\n                  <div id=\"packedDiv\" class=\"optionDiv\">");
                          //  ctl:radioButton
                          org.recipnet.common.controls.RadioButtonHtmlControl packedRadio = null;
                          org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_2 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                          _jspx_th_ctl_radioButton_2.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_radioButton_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_radioButton_2.setId("packedRadio");
                          _jspx_th_ctl_radioButton_2.setOption("packed");
                          int _jspx_eval_ctl_radioButton_2 = _jspx_th_ctl_radioButton_2.doStartTag();
                          if (_jspx_eval_ctl_radioButton_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_radioButton_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButton_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButton_2.doInitBody();
                            }
                            packedRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("packedRadio");
                            do {
                              out.write("\n                    ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_3(_jspx_th_ctl_radioButton_2, _jspx_page_context))
                              return;
                              out.write("\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButton_2.doAfterBody();
                              packedRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("packedRadio");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_radioButton_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_radioButton_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          packedRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("packedRadio");
                          _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_2);
                          out.write(" <dfn>Packing model</dfn>. Generate a\n                    model representing the contents of a triclinic box of the\n                    specified relative dimensions, centered at the specified\n                    position.<br />\n                    ");
                          //  ctl:radioButtonGroup
                          org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_1 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                          _jspx_th_ctl_radioButtonGroup_1.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_radioButtonGroup_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_radioButtonGroup_1.setInitialValue(new String("coordinates"));
                          int _jspx_eval_ctl_radioButtonGroup_1 = _jspx_th_ctl_radioButtonGroup_1.doStartTag();
                          if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButtonGroup_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButtonGroup_1.doInitBody();
                            }
                            do {
                              out.write("\n                      <table>\n                        <tr>\n                          <th>Packing box dimensions:</th>\n                          <td>");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl asize = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_0 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_0.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_0.setId("asize");
                              _jspx_th_ctl_doubleTextbox_0.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_0.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_0 = _jspx_th_ctl_doubleTextbox_0.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_0.doInitBody();
                              }
                              asize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("asize");
                              do {
                              out.write('1');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_0.doAfterBody();
                              asize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("asize");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              asize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("asize");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_0);
                              out.write("\n                          ");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl bsize = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_1 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_1.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_1.setId("bsize");
                              _jspx_th_ctl_doubleTextbox_1.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_1.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_1 = _jspx_th_ctl_doubleTextbox_1.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_1.doInitBody();
                              }
                              bsize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bsize");
                              do {
                              out.write('1');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_1.doAfterBody();
                              bsize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bsize");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              bsize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bsize");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_1);
                              out.write("\n                          ");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl csize = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_2 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_2.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_2.setId("csize");
                              _jspx_th_ctl_doubleTextbox_2.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_2.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_2 = _jspx_th_ctl_doubleTextbox_2.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_2.doInitBody();
                              }
                              csize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("csize");
                              do {
                              out.write('1');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_2.doAfterBody();
                              csize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("csize");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              csize = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("csize");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_2);
                              out.write("\n                          </td>\n                        </tr>\n                        <tr>\n                          <th>");
                              //  ctl:radioButton
                              org.recipnet.common.controls.RadioButtonHtmlControl coordsRadio = null;
                              org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_3 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                              _jspx_th_ctl_radioButton_3.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_radioButton_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_radioButton_3.setId("coordsRadio");
                              _jspx_th_ctl_radioButton_3.setOption("coordinates");
                              int _jspx_eval_ctl_radioButton_3 = _jspx_th_ctl_radioButton_3.doStartTag();
                              if (_jspx_eval_ctl_radioButton_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_radioButton_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButton_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButton_3.doInitBody();
                              }
                              coordsRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("coordsRadio");
                              do {
                              out.write("\n                            ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_4(_jspx_th_ctl_radioButton_3, _jspx_page_context))
                              return;
                              out.write("\n                            ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButton_3.doAfterBody();
                              coordsRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("coordsRadio");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_radioButton_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_radioButton_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              coordsRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("coordsRadio");
                              _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_3);
                              out.write(" Box center at coordinates:</th>\n                          <td>\n                           ");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl acenter = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_3 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_3.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_3.setId("acenter");
                              _jspx_th_ctl_doubleTextbox_3.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_3.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_3 = _jspx_th_ctl_doubleTextbox_3.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_3.doInitBody();
                              }
                              acenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("acenter");
                              do {
                              out.write('0');
                              out.write('.');
                              out.write('5');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_3.doAfterBody();
                              acenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("acenter");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              acenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("acenter");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_3);
                              out.write("\n                           ");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl bcenter = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_4 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_4.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_4.setId("bcenter");
                              _jspx_th_ctl_doubleTextbox_4.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_4.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_4 = _jspx_th_ctl_doubleTextbox_4.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_4.doInitBody();
                              }
                              bcenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bcenter");
                              do {
                              out.write('0');
                              out.write('.');
                              out.write('5');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_4.doAfterBody();
                              bcenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bcenter");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              bcenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("bcenter");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_4);
                              out.write("\n                            ");
                              //  ctl:doubleTextbox
                              org.recipnet.common.controls.DoubleTextboxHtmlControl ccenter = null;
                              org.recipnet.common.controls.DoubleTextboxHtmlControl _jspx_th_ctl_doubleTextbox_5 = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.get(org.recipnet.common.controls.DoubleTextboxHtmlControl.class);
                              _jspx_th_ctl_doubleTextbox_5.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_doubleTextbox_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_doubleTextbox_5.setId("ccenter");
                              _jspx_th_ctl_doubleTextbox_5.setMinValue(0.0);
                              _jspx_th_ctl_doubleTextbox_5.setMaxValue(7.0);
                              int _jspx_eval_ctl_doubleTextbox_5 = _jspx_th_ctl_doubleTextbox_5.doStartTag();
                              if (_jspx_eval_ctl_doubleTextbox_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_doubleTextbox_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_doubleTextbox_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_doubleTextbox_5.doInitBody();
                              }
                              ccenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("ccenter");
                              do {
                              out.write('0');
                              out.write('.');
                              out.write('5');
                              int evalDoAfterBody = _jspx_th_ctl_doubleTextbox_5.doAfterBody();
                              ccenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("ccenter");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_doubleTextbox_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_doubleTextbox_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              ccenter = (org.recipnet.common.controls.DoubleTextboxHtmlControl) _jspx_page_context.findAttribute("ccenter");
                              _jspx_tagPool_ctl_doubleTextbox_minValue_maxValue_id.reuse(_jspx_th_ctl_doubleTextbox_5);
                              out.write("\n                          </td>\n                        </tr>\n                        <tr>\n                          <th>");
                              //  ctl:radioButton
                              org.recipnet.common.controls.RadioButtonHtmlControl atomRadio = null;
                              org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_4 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                              _jspx_th_ctl_radioButton_4.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_radioButton_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_radioButton_4.setId("atomRadio");
                              _jspx_th_ctl_radioButton_4.setOption("atom");
                              int _jspx_eval_ctl_radioButton_4 = _jspx_th_ctl_radioButton_4.doStartTag();
                              if (_jspx_eval_ctl_radioButton_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_radioButton_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_radioButton_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_radioButton_4.doInitBody();
                              }
                              atomRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("atomRadio");
                              do {
                              out.write("\n                            ");
                              if (_jspx_meth_ctl_extraHtmlAttribute_5(_jspx_th_ctl_radioButton_4, _jspx_page_context))
                              return;
                              out.write("\n                          ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButton_4.doAfterBody();
                              atomRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("atomRadio");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_radioButton_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_radioButton_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              atomRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("atomRadio");
                              _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_4);
                              out.write(" Box center at atom:</th>\n                          <td>");
                              //  ctl:listbox
                              org.recipnet.common.controls.ListboxHtmlControl packedModelCenter = null;
                              org.recipnet.common.controls.ListboxHtmlControl _jspx_th_ctl_listbox_1 = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_tagPool_ctl_listbox_id.get(org.recipnet.common.controls.ListboxHtmlControl.class);
                              _jspx_th_ctl_listbox_1.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_listbox_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_1);
                              _jspx_th_ctl_listbox_1.setId("packedModelCenter");
                              int _jspx_eval_ctl_listbox_1 = _jspx_th_ctl_listbox_1.doStartTag();
                              if (_jspx_eval_ctl_listbox_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_listbox_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_listbox_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_listbox_1.doInitBody();
                              }
                              packedModelCenter = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("packedModelCenter");
                              do {
                              out.write("\n                            ");
                              //  rn:atomSites
                              org.recipnet.site.content.rncontrols.CifAtomSiteIterator centerSites = null;
                              org.recipnet.site.content.rncontrols.CifAtomSiteIterator _jspx_th_rn_atomSites_1 = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols.get(org.recipnet.site.content.rncontrols.CifAtomSiteIterator.class);
                              _jspx_th_rn_atomSites_1.setPageContext(_jspx_page_context);
                              _jspx_th_rn_atomSites_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_1);
                              _jspx_th_rn_atomSites_1.setId("centerSites");
                              _jspx_th_rn_atomSites_1.setExcludedTypeSymbols("H");
                              int _jspx_eval_rn_atomSites_1 = _jspx_th_rn_atomSites_1.doStartTag();
                              if (_jspx_eval_rn_atomSites_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_rn_atomSites_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_atomSites_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_atomSites_1.doInitBody();
                              }
                              centerSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("centerSites");
                              do {
                              out.write("\n                              ");
                              if (_jspx_meth_ctl_option_1(_jspx_th_rn_atomSites_1, _jspx_page_context))
                              return;
                              out.write("\n                            ");
                              int evalDoAfterBody = _jspx_th_rn_atomSites_1.doAfterBody();
                              centerSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("centerSites");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_rn_atomSites_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_rn_atomSites_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              centerSites = (org.recipnet.site.content.rncontrols.CifAtomSiteIterator) _jspx_page_context.findAttribute("centerSites");
                              _jspx_tagPool_rn_atomSites_id_excludedTypeSymbols.reuse(_jspx_th_rn_atomSites_1);
                              out.write("\n                          ");
                              int evalDoAfterBody = _jspx_th_ctl_listbox_1.doAfterBody();
                              packedModelCenter = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("packedModelCenter");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_listbox_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_listbox_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              packedModelCenter = (org.recipnet.common.controls.ListboxHtmlControl) _jspx_page_context.findAttribute("packedModelCenter");
                              _jspx_tagPool_ctl_listbox_id.reuse(_jspx_th_ctl_listbox_1);
                              out.write("</td>\n                        </tr>\n                      </table>\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_radioButtonGroup_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_radioButtonGroup_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_radioButtonGroup_initialValue.reuse(_jspx_th_ctl_radioButtonGroup_1);
                          out.write("\n                    ");
                          //  ctl:errorChecker
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorChecker_1.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_errorChecker_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorChecker_1.setErrorFilter( ErrorChecker.ANY_ERROR );
                          _jspx_th_ctl_errorChecker_1.setInvert(true);
                          int _jspx_eval_ctl_errorChecker_1 = _jspx_th_ctl_errorChecker_1.doStartTag();
                          if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorChecker_1.doInitBody();
                            }
                            do {
                              out.write("\n                      ");
                              //  ctl:ifValueIsTrue
                              org.recipnet.common.controls.BooleanValueChecker _jspx_th_ctl_ifValueIsTrue_0 = (org.recipnet.common.controls.BooleanValueChecker) _jspx_tagPool_ctl_ifValueIsTrue_control.get(org.recipnet.common.controls.BooleanValueChecker.class);
                              _jspx_th_ctl_ifValueIsTrue_0.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_ifValueIsTrue_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_1);
                              _jspx_th_ctl_ifValueIsTrue_0.setControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${generateCrtButton}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
                              int _jspx_eval_ctl_ifValueIsTrue_0 = _jspx_th_ctl_ifValueIsTrue_0.doStartTag();
                              if (_jspx_eval_ctl_ifValueIsTrue_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_ifValueIsTrue_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_ifValueIsTrue_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_ifValueIsTrue_0.doInitBody();
                              }
                              do {
                              out.write("\n                        ");
                              if (_jspx_meth_ctl_ifValueIsTrue_1(_jspx_th_ctl_ifValueIsTrue_0, _jspx_page_context))
                              return;
                              out.write("\n                        ");
                              if (_jspx_meth_ctl_ifValueIsTrue_2(_jspx_th_ctl_ifValueIsTrue_0, _jspx_page_context))
                              return;
                              out.write("\n                        ");
                              //  rn:generateCrt
                              org.recipnet.site.content.rncontrols.GenerateCrtElement generator = null;
                              org.recipnet.site.content.rncontrols.GenerateCrtElement _jspx_th_rn_generateCrt_0 = (org.recipnet.site.content.rncontrols.GenerateCrtElement) _jspx_tagPool_rn_generateCrt_keyControl_id_nobody.get(org.recipnet.site.content.rncontrols.GenerateCrtElement.class);
                              _jspx_th_rn_generateCrt_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_generateCrt_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_0);
                              _jspx_th_rn_generateCrt_0.setId("generator");
                              _jspx_th_rn_generateCrt_0.setKeyControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${crtKey}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
                              int _jspx_eval_rn_generateCrt_0 = _jspx_th_rn_generateCrt_0.doStartTag();
                              if (_jspx_th_rn_generateCrt_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              generator = (org.recipnet.site.content.rncontrols.GenerateCrtElement) _jspx_page_context.findAttribute("generator");
                              _jspx_tagPool_rn_generateCrt_keyControl_id_nobody.reuse(_jspx_th_rn_generateCrt_0);
                              out.write("\n                      ");
                              int evalDoAfterBody = _jspx_th_ctl_ifValueIsTrue_0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_ifValueIsTrue_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_ifValueIsTrue_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_ctl_ifValueIsTrue_control.reuse(_jspx_th_ctl_ifValueIsTrue_0);
                              out.write("\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_errorChecker_1.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_1);
                          out.write("\n                    ");
                          //  ctl:errorChecker
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorChecker_2.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_errorChecker_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorChecker_2.setErrorFilter( ErrorChecker.ANY_ERROR );
                          int _jspx_eval_ctl_errorChecker_2 = _jspx_th_ctl_errorChecker_2.doStartTag();
                          if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorChecker_2.doInitBody();
                            }
                            do {
                              out.write("\n                      ");
                              if (_jspx_meth_ctl_phaseEvent_4(_jspx_th_ctl_errorChecker_2, _jspx_page_context))
                              return;
                              out.write("\n                    ");
                              int evalDoAfterBody = _jspx_th_ctl_errorChecker_2.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_2);
                          out.write("\n                  </div>\n                  <div style=\"margin-top: 1em; margin-bottom: 1em;\">\n                    <hr />\n                  </div>\n                  <table>\n                    <tr>\n                      <th style=\"padding-top: 0.2em; vertical-align: top;\">CRT\n                        name:</th>\n                      <td>");
                          //  ctl:textbox
                          org.recipnet.common.controls.TextboxHtmlControl crtName = null;
                          org.recipnet.common.controls.TextboxHtmlControl _jspx_th_ctl_textbox_0 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.get(org.recipnet.common.controls.TextboxHtmlControl.class);
                          _jspx_th_ctl_textbox_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_textbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_textbox_0.setId("crtName");
                          _jspx_th_ctl_textbox_0.setSize(22);
                          _jspx_th_ctl_textbox_0.setRequired(true);
                          _jspx_th_ctl_textbox_0.setShouldConvertBlankToNull(false);
                          _jspx_th_ctl_textbox_0.setFailedValidationHtml("<br/><span class='errorMessage'>The requested file name is not valid.</span>");
                          int _jspx_eval_ctl_textbox_0 = _jspx_th_ctl_textbox_0.doStartTag();
                          if (_jspx_eval_ctl_textbox_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_textbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_textbox_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_textbox_0.doInitBody();
                            }
                            crtName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("crtName");
                            do {
                              out.write("\n                        ");
                              //  ctl:validator
                              org.recipnet.common.controls.ValidatorHtmlElement _jspx_th_ctl_validator_0 = (org.recipnet.common.controls.ValidatorHtmlElement) _jspx_tagPool_ctl_validator_validator_nobody.get(org.recipnet.common.controls.ValidatorHtmlElement.class);
                              _jspx_th_ctl_validator_0.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_validator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_textbox_0);
                              _jspx_th_ctl_validator_0.setValidator(new FilenameValidator());
                              int _jspx_eval_ctl_validator_0 = _jspx_th_ctl_validator_0.doStartTag();
                              if (_jspx_th_ctl_validator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_ctl_validator_validator_nobody.reuse(_jspx_th_ctl_validator_0);
                              out.write("\n                        ");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:replaceTail(param['cifName'], \".cif\", \"\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                              out.write(".crt\n                        ");
                              int evalDoAfterBody = _jspx_th_ctl_textbox_0.doAfterBody();
                              crtName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("crtName");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_textbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_textbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          crtName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("crtName");
                          _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.reuse(_jspx_th_ctl_textbox_0);
                          out.write("\n                      </td>\n                    </tr>\n                    <tr>\n                      <th style=\"padding-top: 0.2em; vertical-align: top;\">CRT\n                        description:</th>\n                      <td>\n                        ");
                          //  ctl:textarea
                          org.recipnet.common.controls.TextareaHtmlControl description = null;
                          org.recipnet.common.controls.TextareaHtmlControl _jspx_th_ctl_textarea_0 = (org.recipnet.common.controls.TextareaHtmlControl) _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns.get(org.recipnet.common.controls.TextareaHtmlControl.class);
                          _jspx_th_ctl_textarea_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_textarea_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                          _jspx_th_ctl_textarea_0.setId("description");
                          _jspx_th_ctl_textarea_0.setColumns(32);
                          _jspx_th_ctl_textarea_0.setRows(2);
                          _jspx_th_ctl_textarea_0.setShouldConvertBlankToNull(true);
                          _jspx_th_ctl_textarea_0.setFailedValidationHtml("<br/><span class='errorMessage'>The file description contains impermissible characters.</span>");
                          int _jspx_eval_ctl_textarea_0 = _jspx_th_ctl_textarea_0.doStartTag();
                          if (_jspx_eval_ctl_textarea_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_textarea_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_textarea_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_textarea_0.doInitBody();
                            }
                            description = (org.recipnet.common.controls.TextareaHtmlControl) _jspx_page_context.findAttribute("description");
                            do {
                              out.write("\n                          ");
                              //  ctl:validator
                              org.recipnet.common.controls.ValidatorHtmlElement _jspx_th_ctl_validator_1 = (org.recipnet.common.controls.ValidatorHtmlElement) _jspx_tagPool_ctl_validator_validator_nobody.get(org.recipnet.common.controls.ValidatorHtmlElement.class);
                              _jspx_th_ctl_validator_1.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_validator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_textarea_0);
                              _jspx_th_ctl_validator_1.setValidator(new ContainerStringValidator());
                              int _jspx_eval_ctl_validator_1 = _jspx_th_ctl_validator_1.doStartTag();
                              if (_jspx_th_ctl_validator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_ctl_validator_validator_nobody.reuse(_jspx_th_ctl_validator_1);
                              out.write("\n                        ");
                              int evalDoAfterBody = _jspx_th_ctl_textarea_0.doAfterBody();
                              description = (org.recipnet.common.controls.TextareaHtmlControl) _jspx_page_context.findAttribute("description");
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_textarea_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_textarea_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          description = (org.recipnet.common.controls.TextareaHtmlControl) _jspx_page_context.findAttribute("description");
                          _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns.reuse(_jspx_th_ctl_textarea_0);
                          out.write("\n                        ");
                          if (_jspx_meth_ctl_phaseEvent_5(_jspx_th_ctl_radioButtonGroup_0, _jspx_page_context))
                            return;
                          out.write("\n                      </td>\n                    </tr>\n                    <tr>\n                      <th style=\"padding-top: 0.2em; vertical-align: top;\">Comments:</th>\n                      <td>");
                          if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_radioButtonGroup_0, _jspx_page_context))
                            return;
                          out.write("</td>\n                    </tr>\n                  </table>\n                ");
                          int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_0.doAfterBody();
                          modelType = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("modelType");
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_radioButtonGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      modelType = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("modelType");
                      _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.reuse(_jspx_th_ctl_radioButtonGroup_0);
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_rn_cifFile_0.doAfterBody();
                      cif = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_page_context.findAttribute("cif");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_cifFile_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_cifFile_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cif = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_page_context.findAttribute("cif");
                  _jspx_tagPool_rn_cifFile_id.reuse(_jspx_th_rn_cifFile_0);
                  out.write("\n              <div class=\"formDiv\" style=\"text-align: center;\">\n                ");
                  if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n                ");
                  if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n              </div>\n            </td>\n            <td style=\"vertical-align: top;\">\n              <div id=\"sidebar\" style=\"width: 300px; margin: 0.5em 0 0 1em;\"><span\n                class=\"fieldLabel\">Computed model:</span>\n                <div id=\"jammDiv\"\n                  style=\"width: 300px; height: 300px; background: silver; border: 1px solid #32357D;\">\n                  ");
                  if (_jspx_meth_rn_checkTrackedFile_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n                  ");
                  if (_jspx_meth_ctl_errorMessage_2(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n                </div>\n                <div style=\"font-style: italic; margin-bottom: 1em; \">(Toggle\n                  label display with the 'L' key; you may need to click in the\n                  window first.)</div>\n                <span class=\"fieldLabel\">CRT files already in the repository:</span><br />\n                ");
                  //  rn:fileIterator
                  org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                  _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_fileIterator_0.setSortFilesByName(true);
                  int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
                  if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_fileIterator_0.doInitBody();
                    }
                    do {
                      out.write("\n                  ");
                      //  rn:fileChecker
                      org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_requiredExtension.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                      _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                      _jspx_th_rn_fileChecker_0.setRequiredExtension(".crt");
                      int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
                      if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                    <div class=\"fileName\">");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                          _jspx_th_rn_fileField_1.setFieldCode( FileField.FILENAME );
                          int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                          if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                          out.write("</div>\n                    <div class=\"fileDescription\">");
                          //  rn:fileField
                          org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                          _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                          _jspx_th_rn_fileField_2.setFieldCode( FileField.DESCRIPTION);
                          int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                          if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                          out.write("</div>\n                  ");
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
                      out.write("\n                ");
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
                  out.write("\n              </div>\n            </td>\n          </tr>\n        </table>\n      ");
                  int evalDoAfterBody = _jspx_th_ctl_selfForm_0.doAfterBody();
                  pageForm = (org.recipnet.common.controls.FormHtmlElement) _jspx_page_context.findAttribute("pageForm");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_selfForm_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              pageForm = (org.recipnet.common.controls.FormHtmlElement) _jspx_page_context.findAttribute("pageForm");
              _jspx_tagPool_ctl_selfForm_id.reuse(_jspx_th_ctl_selfForm_0);
              out.write("\n    ");
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
          out.write("\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_generateCrtWapPage_0, _jspx_page_context))
            return;
          out.write("\n  <script type=\"text/javascript\">\n  <!-- // begin hiding javascript from ancient browsers\n    var pageForm = document.getElementById('pageForm')\n    var mainOptions = new Array(\"simple\", \"grown\", \"packed\")\n    var buttonChildren = new Array()\n    var buttonPeers = new Array()\n\n    buttonChildren[\"simpleRadio\"] = new Array()\n    buttonChildren[\"grownRadio\"] = new Array(\"grownModelSeeds\")\n    buttonChildren[\"packedRadio\"]\n      = new Array(\"asize\", \"bsize\", \"csize\", \"coordsRadio\", \"atomRadio\")\n    buttonChildren[\"coordsRadio\"] = new Array(\"acenter\", \"bcenter\", \"ccenter\")\n    buttonChildren[\"atomRadio\"] = new Array(\"packedModelCenter\")\n\n    buttonPeers[\"simpleRadio\"] = new Array(\"grownRadio\", \"packedRadio\")\n    buttonPeers[\"grownRadio\"] = new Array(\"simpleRadio\", \"packedRadio\")\n    buttonPeers[\"packedRadio\"] = new Array(\"simpleRadio\", \"grownRadio\")\n    buttonPeers[\"coordsRadio\"] = new Array(\"atomRadio\")\n    buttonPeers[\"atomRadio\"] = new Array(\"coordsRadio\")\n    \n    function selectMainOption(option) {\n      for (var index = 0; index < mainOptions.length; index++) {\n");
          out.write("        var div = document.getElementById(mainOptions[index] + \"Div\")\n        \n        if (option == mainOptions[index]) {\n          div.style.background=\"white\"\n        } else {\n          div.style.background=\"silver\"\n        }\n      }\n      selectOption(option)\n    }\n    \n    function selectOption(option) {\n      var radioId = option + \"Radio\"\n      var peers = buttonPeers[radioId]\n\n      enableChildControls(radioId, true, false)\n      for (var index = 0; index < peers.length; index++) {\n        enableChildControls(peers[index], false, false)\n      }\n    }\n\n    function enableChildControls(radioId, state, unconditional) {\n      var childControls = buttonChildren[radioId]\n\n      for (var index = 0; index < childControls.length; index++) {\n        var childId = childControls[index]\n        var child = document.getElementById(childId)\n        \n        child.disabled = !state\n        if (buttonChildren[childId]) {\n          if (child.checked || !state || unconditional) {\n            enableChildControls(childId, state, unconditional)\n");
          out.write("          }\n        }\n      }\n    }\n\n    function enableAllControls() {\n      for (var index = 0; index < mainOptions.length; index++) {\n        var radioId = mainOptions[index] + \"Radio\"\n        var button = document.getElementById(radioId)\n\n        button.disabled = false\n        enableChildControls(radioId, true, true)  \n      }\n      \n      return true\n    }\n    \n    pageForm.onsubmit = enableAllControls\n    \n    for (var index = 0; index < mainOptions.length; index++) {\n      var button = document.getElementById(mainOptions[index] + \"Radio\")\n\n      if (button.checked) {\n        button.click()\n        break\n      }\n    }\n\n    // stop hiding javascript -->\n  </script>\n");
          int evalDoAfterBody = _jspx_th_rn_generateCrtWapPage_0.doAfterBody();
          crtPage = (org.recipnet.site.content.rncontrols.GenerateCrtFileWapPage) _jspx_page_context.findAttribute("crtPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_generateCrtWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_generateCrtWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_generateCrtWapPage_title_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_crtFileName_crtFileKey_authorizationFailedReasonParamName.reuse(_jspx_th_rn_generateCrtWapPage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_generateCrtWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generateCrtWapPage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n      <p class=\"errorMessage\">This sample has been retracted by its lab and no\n      files may be generated for it.</p>\n    ");
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

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButton_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButton_0);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("selectMainOption('simple')");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButton_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_1 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButton_1);
    _jspx_th_ctl_extraHtmlAttribute_1.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("selectMainOption('grown')");
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_listbox_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_2 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_listbox_0);
    _jspx_th_ctl_extraHtmlAttribute_2.setName("size");
    _jspx_th_ctl_extraHtmlAttribute_2.setValue("4");
    int _jspx_eval_ctl_extraHtmlAttribute_2 = _jspx_th_ctl_extraHtmlAttribute_2.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_2);
    return false;
  }

  private boolean _jspx_meth_ctl_option_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_atomSites_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_0 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option_label_nobody.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_atomSites_0);
    _jspx_th_ctl_option_0.setLabel((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${seedSites.siteRecord.label}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_option_0 = _jspx_th_ctl_option_0.doStartTag();
    if (_jspx_th_ctl_option_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option_label_nobody.reuse(_jspx_th_ctl_option_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButton_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_3 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButton_2);
    _jspx_th_ctl_extraHtmlAttribute_3.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_3.setValue("selectMainOption('packed')");
    int _jspx_eval_ctl_extraHtmlAttribute_3 = _jspx_th_ctl_extraHtmlAttribute_3.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_3);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButton_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_4 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButton_3);
    _jspx_th_ctl_extraHtmlAttribute_4.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_4.setValue("selectOption('coords')");
    int _jspx_eval_ctl_extraHtmlAttribute_4 = _jspx_th_ctl_extraHtmlAttribute_4.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_4);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButton_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_5 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButton_4);
    _jspx_th_ctl_extraHtmlAttribute_5.setName("onClick");
    _jspx_th_ctl_extraHtmlAttribute_5.setValue("selectOption('atom')");
    int _jspx_eval_ctl_extraHtmlAttribute_5 = _jspx_th_ctl_extraHtmlAttribute_5.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_5);
    return false;
  }

  private boolean _jspx_meth_ctl_option_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_atomSites_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:option
    org.recipnet.common.controls.ListboxOption _jspx_th_ctl_option_1 = (org.recipnet.common.controls.ListboxOption) _jspx_tagPool_ctl_option_label_nobody.get(org.recipnet.common.controls.ListboxOption.class);
    _jspx_th_ctl_option_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_option_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_atomSites_1);
    _jspx_th_ctl_option_1.setLabel((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${centerSites.siteRecord.label}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_option_1 = _jspx_th_ctl_option_1.doStartTag();
    if (_jspx_th_ctl_option_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_option_label_nobody.reuse(_jspx_th_ctl_option_1);
    return false;
  }

  private boolean _jspx_meth_ctl_ifValueIsTrue_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:ifValueIsTrue
    org.recipnet.common.controls.BooleanValueChecker _jspx_th_ctl_ifValueIsTrue_1 = (org.recipnet.common.controls.BooleanValueChecker) _jspx_tagPool_ctl_ifValueIsTrue_control.get(org.recipnet.common.controls.BooleanValueChecker.class);
    _jspx_th_ctl_ifValueIsTrue_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_ifValueIsTrue_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_0);
    _jspx_th_ctl_ifValueIsTrue_1.setControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${grownRadio}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_ifValueIsTrue_1 = _jspx_th_ctl_ifValueIsTrue_1.doStartTag();
    if (_jspx_eval_ctl_ifValueIsTrue_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_ifValueIsTrue_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_ifValueIsTrue_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_ifValueIsTrue_1.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_ctl_phaseEvent_0(_jspx_th_ctl_ifValueIsTrue_1, _jspx_page_context))
          return true;
        out.write("\n                        ");
        int evalDoAfterBody = _jspx_th_ctl_ifValueIsTrue_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_ifValueIsTrue_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_ifValueIsTrue_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_ifValueIsTrue_control.reuse(_jspx_th_ctl_ifValueIsTrue_1);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_0 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_1);
    _jspx_th_ctl_phaseEvent_0.setOnPhases("fetching");
    _jspx_th_ctl_phaseEvent_0.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_0 = _jspx_th_ctl_phaseEvent_0.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_0.doInitBody();
      }
      do {
        out.write("\n                            ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("generator"), "seedSites", "${grownModelSeeds.valueAsStringCollection}", _jspx_page_context, null);
        out.write("\n                          ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_0);
    return false;
  }

  private boolean _jspx_meth_ctl_ifValueIsTrue_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:ifValueIsTrue
    org.recipnet.common.controls.BooleanValueChecker _jspx_th_ctl_ifValueIsTrue_2 = (org.recipnet.common.controls.BooleanValueChecker) _jspx_tagPool_ctl_ifValueIsTrue_control.get(org.recipnet.common.controls.BooleanValueChecker.class);
    _jspx_th_ctl_ifValueIsTrue_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_ifValueIsTrue_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_0);
    _jspx_th_ctl_ifValueIsTrue_2.setControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${packedRadio}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_ifValueIsTrue_2 = _jspx_th_ctl_ifValueIsTrue_2.doStartTag();
    if (_jspx_eval_ctl_ifValueIsTrue_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_ifValueIsTrue_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_ifValueIsTrue_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_ifValueIsTrue_2.doInitBody();
      }
      do {
        out.write("\n                          ");
        if (_jspx_meth_ctl_phaseEvent_1(_jspx_th_ctl_ifValueIsTrue_2, _jspx_page_context))
          return true;
        out.write("\n                          ");
        if (_jspx_meth_ctl_ifValueIsTrue_3(_jspx_th_ctl_ifValueIsTrue_2, _jspx_page_context))
          return true;
        out.write("\n                          ");
        if (_jspx_meth_ctl_ifValueIsTrue_4(_jspx_th_ctl_ifValueIsTrue_2, _jspx_page_context))
          return true;
        out.write("\n                        ");
        int evalDoAfterBody = _jspx_th_ctl_ifValueIsTrue_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_ifValueIsTrue_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_ifValueIsTrue_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_ifValueIsTrue_control.reuse(_jspx_th_ctl_ifValueIsTrue_2);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_1 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_2);
    _jspx_th_ctl_phaseEvent_1.setOnPhases("fetching");
    _jspx_th_ctl_phaseEvent_1.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_1 = _jspx_th_ctl_phaseEvent_1.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_1.doInitBody();
      }
      do {
        out.write("\n                            ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("generator"), "boxSize", "${asize.value},${bsize.value},${csize.value}", _jspx_page_context, null);
        out.write("\n                          ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_1);
    return false;
  }

  private boolean _jspx_meth_ctl_ifValueIsTrue_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:ifValueIsTrue
    org.recipnet.common.controls.BooleanValueChecker _jspx_th_ctl_ifValueIsTrue_3 = (org.recipnet.common.controls.BooleanValueChecker) _jspx_tagPool_ctl_ifValueIsTrue_control.get(org.recipnet.common.controls.BooleanValueChecker.class);
    _jspx_th_ctl_ifValueIsTrue_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_ifValueIsTrue_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_2);
    _jspx_th_ctl_ifValueIsTrue_3.setControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${coordsRadio}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_ifValueIsTrue_3 = _jspx_th_ctl_ifValueIsTrue_3.doStartTag();
    if (_jspx_eval_ctl_ifValueIsTrue_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_ifValueIsTrue_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_ifValueIsTrue_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_ifValueIsTrue_3.doInitBody();
      }
      do {
        out.write("\n                            ");
        if (_jspx_meth_ctl_phaseEvent_2(_jspx_th_ctl_ifValueIsTrue_3, _jspx_page_context))
          return true;
        out.write("\n                          ");
        int evalDoAfterBody = _jspx_th_ctl_ifValueIsTrue_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_ifValueIsTrue_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_ifValueIsTrue_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_ifValueIsTrue_control.reuse(_jspx_th_ctl_ifValueIsTrue_3);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_2 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_3);
    _jspx_th_ctl_phaseEvent_2.setOnPhases("fetching");
    _jspx_th_ctl_phaseEvent_2.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_2 = _jspx_th_ctl_phaseEvent_2.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_2.doInitBody();
      }
      do {
        out.write("\n                              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("generator"), "boxCoordinates", "${acenter.value},${bcenter.value},${ccenter.value}", _jspx_page_context, null);
        out.write("\n                            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_2);
    return false;
  }

  private boolean _jspx_meth_ctl_ifValueIsTrue_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:ifValueIsTrue
    org.recipnet.common.controls.BooleanValueChecker _jspx_th_ctl_ifValueIsTrue_4 = (org.recipnet.common.controls.BooleanValueChecker) _jspx_tagPool_ctl_ifValueIsTrue_control.get(org.recipnet.common.controls.BooleanValueChecker.class);
    _jspx_th_ctl_ifValueIsTrue_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_ifValueIsTrue_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_2);
    _jspx_th_ctl_ifValueIsTrue_4.setControl((org.recipnet.common.controls.HtmlControl) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${atomRadio}", org.recipnet.common.controls.HtmlControl.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_ifValueIsTrue_4 = _jspx_th_ctl_ifValueIsTrue_4.doStartTag();
    if (_jspx_eval_ctl_ifValueIsTrue_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_ifValueIsTrue_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_ifValueIsTrue_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_ifValueIsTrue_4.doInitBody();
      }
      do {
        out.write("\n                            ");
        if (_jspx_meth_ctl_phaseEvent_3(_jspx_th_ctl_ifValueIsTrue_4, _jspx_page_context))
          return true;
        out.write("\n                          ");
        int evalDoAfterBody = _jspx_th_ctl_ifValueIsTrue_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_ifValueIsTrue_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_ifValueIsTrue_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_ifValueIsTrue_control.reuse(_jspx_th_ctl_ifValueIsTrue_4);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_ifValueIsTrue_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_3 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_ifValueIsTrue_4);
    _jspx_th_ctl_phaseEvent_3.setOnPhases("fetching");
    _jspx_th_ctl_phaseEvent_3.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_3 = _jspx_th_ctl_phaseEvent_3.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_3.doInitBody();
      }
      do {
        out.write("\n                              ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("generator"), "boxCenter", "${packedModelCenter.value}", _jspx_page_context, null);
        out.write("\n                            ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_3);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_4(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_4 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
    _jspx_th_ctl_phaseEvent_4.setOnPhases("fetching");
    _jspx_th_ctl_phaseEvent_4.setSkipIfSuppressed(true);
    int _jspx_eval_ctl_phaseEvent_4 = _jspx_th_ctl_phaseEvent_4.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_4.doInitBody();
      }
      do {
        out.write("\n                        ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("crtKey"), "value", "${''}", _jspx_page_context, null);
        out.write("\n                      ");
        int evalDoAfterBody = _jspx_th_ctl_phaseEvent_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_phaseEvent_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_phaseEvent_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_phaseEvent_skipIfSuppressed_onPhases.reuse(_jspx_th_ctl_phaseEvent_4);
    return false;
  }

  private boolean _jspx_meth_ctl_phaseEvent_5(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButtonGroup_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_5 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
    _jspx_th_ctl_phaseEvent_5.setOnPhases("fetching");
    int _jspx_eval_ctl_phaseEvent_5 = _jspx_th_ctl_phaseEvent_5.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_5.doInitBody();
      }
      do {
        out.write("\n                          ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("crtPage"), "fileDescription", "${description.value}", _jspx_page_context, null);
        out.write("\n                        ");
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

  private boolean _jspx_meth_rn_wapComments_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButtonGroup_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapComments
    org.recipnet.site.content.rncontrols.WapComments _jspx_th_rn_wapComments_0 = (org.recipnet.site.content.rncontrols.WapComments) _jspx_tagPool_rn_wapComments_nobody.get(org.recipnet.site.content.rncontrols.WapComments.class);
    _jspx_th_rn_wapComments_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapComments_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
    int _jspx_eval_rn_wapComments_0 = _jspx_th_rn_wapComments_0.doStartTag();
    if (_jspx_th_rn_wapComments_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapComments_nobody.reuse(_jspx_th_rn_wapComments_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapSaveButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapSaveButton
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_label_editable_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_wapSaveButton_0.setLabel("Save CRT file");
    _jspx_th_rn_wapSaveButton_0.setEditable(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${not empty crtKey.valueAsString}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_label_editable_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_0 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_nobody.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapCancelButton_0 = _jspx_th_rn_wapCancelButton_0.doStartTag();
    if (_jspx_th_rn_wapCancelButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_nobody.reuse(_jspx_th_rn_wapCancelButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_checkTrackedFile_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:checkTrackedFile
    org.recipnet.site.content.rncontrols.TrackedFileChecker _jspx_th_rn_checkTrackedFile_0 = (org.recipnet.site.content.rncontrols.TrackedFileChecker) _jspx_tagPool_rn_checkTrackedFile_requireValid_fileKey.get(org.recipnet.site.content.rncontrols.TrackedFileChecker.class);
    _jspx_th_rn_checkTrackedFile_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_checkTrackedFile_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_checkTrackedFile_0.setFileKey((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${crtKey.valueAsString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_rn_checkTrackedFile_0.setRequireValid(true);
    int _jspx_eval_rn_checkTrackedFile_0 = _jspx_th_rn_checkTrackedFile_0.doStartTag();
    if (_jspx_eval_rn_checkTrackedFile_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_checkTrackedFile_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_checkTrackedFile_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_checkTrackedFile_0.doInitBody();
      }
      do {
        out.write("\n                    ");
        if (_jspx_meth_rn_jammModel_0(_jspx_th_rn_checkTrackedFile_0, _jspx_page_context))
          return true;
        out.write("\n                  ");
        int evalDoAfterBody = _jspx_th_rn_checkTrackedFile_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_checkTrackedFile_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_checkTrackedFile_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_checkTrackedFile_requireValid_fileKey.reuse(_jspx_th_rn_checkTrackedFile_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammModel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_checkTrackedFile_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammModel
    org.recipnet.site.content.rncontrols.JammModelElement _jspx_th_rn_jammModel_0 = (org.recipnet.site.content.rncontrols.JammModelElement) _jspx_tagPool_rn_jammModel_modelUrl.get(org.recipnet.site.content.rncontrols.JammModelElement.class);
    _jspx_th_rn_jammModel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammModel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_checkTrackedFile_0);
    _jspx_th_rn_jammModel_0.setModelUrl((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${htmlPage.contextPath}/servlet/fileretrieve?key=${crtKey.valueAsString}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_rn_jammModel_0 = _jspx_th_rn_jammModel_0.doStartTag();
    if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_jammModel_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_jammModel_0.doInitBody();
      }
      do {
        out.write("\n                      ");
        if (_jspx_meth_rn_jammApplet_0(_jspx_th_rn_jammModel_0, _jspx_page_context))
          return true;
        out.write("\n                    ");
        int evalDoAfterBody = _jspx_th_rn_jammModel_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_jammModel_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_jammModel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammModel_modelUrl.reuse(_jspx_th_rn_jammModel_0);
    return false;
  }

  private boolean _jspx_meth_rn_jammApplet_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_jammModel_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:jammApplet
    org.recipnet.site.content.rncontrols.JammAppletTag _jspx_th_rn_jammApplet_0 = (org.recipnet.site.content.rncontrols.JammAppletTag) _jspx_tagPool_rn_jammApplet_width_height_border_applet_nobody.get(org.recipnet.site.content.rncontrols.JammAppletTag.class);
    _jspx_th_rn_jammApplet_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_jammApplet_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_jammModel_0);
    _jspx_th_rn_jammApplet_0.setWidth("300");
    _jspx_th_rn_jammApplet_0.setHeight("300");
    _jspx_th_rn_jammApplet_0.setApplet("minijamm");
    _jspx_th_rn_jammApplet_0.setDynamicAttribute(null, "border", new String("false"));
    int _jspx_eval_rn_jammApplet_0 = _jspx_th_rn_jammApplet_0.doStartTag();
    if (_jspx_th_rn_jammApplet_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_jammApplet_width_height_border_applet_nobody.reuse(_jspx_th_rn_jammApplet_0);
    return false;
  }

  private boolean _jspx_meth_ctl_errorMessage_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:errorMessage
    org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
    _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${generator}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_errorMessage_2.doInitBody();
      }
      do {
        out.write("\n                    <span class=\"errorMessage\">CRT generation failed -- check your CIF</span>\n                  ");
        int evalDoAfterBody = _jspx_th_ctl_errorMessage_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_errorMessage_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_errorMessage_errorSupplier.reuse(_jspx_th_ctl_errorMessage_2);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_generateCrtWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generateCrtWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessage { color: red; text-align: center; }\n    div.bodyDiv { font-family: Arial, Helvetica, Verdana, sans-serif;\n        margin: 1em }\n    div.bodyDiv div { margin: .5em 0 .5em 0em; }\n    div.formDiv, div.optionDiv { padding: .5em .5em .5em 2.5em; }\n    div.optionDiv { border: 1px solid #32357D; text-indent: -2em; }\n    div.innerDiv { margin-top: 1em; text-indent: 0 }\n    span.fieldLabel, div.bodyDiv th { font-style: normal; font-weight: bold; }\n    div.bodyDiv div.fileName { margin: 0.25em 0 0 0; }\n    div.bodyDiv div.fileDescription { font-style: italic; margin: 0 0 0 1em;\n        color: gray; }\n    div.bodyDiv th { text-align: left; }\n    div.bodyDiv tr { margin-top: 0.5em; margin-bottom: 0.5em; }\n  ");
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
