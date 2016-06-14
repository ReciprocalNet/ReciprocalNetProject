package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.ErrorChecker;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage;
import org.recipnet.site.content.rncontrols.FileField;
import org.recipnet.site.content.rncontrols.SimpleFileContext;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.validation.FilenameValidator;
import org.recipnet.site.shared.validation.ContainerStringValidator;

public final class generatepdb_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_generatePdbWapPage_title_pdbFileName_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_validator_validator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_phaseEvent_onPhases;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileIterator_sortFilesByName_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileChecker_requiredExtension;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_fileField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_generatePdbWapPage_title_pdbFileName_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_validator_validator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_phaseEvent_onPhases = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileIterator_sortFilesByName_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileChecker_requiredExtension = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_fileField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_generatePdbWapPage_title_pdbFileName_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.release();
    _jspx_tagPool_rn_redirectToLogin_target_returnUrlParamName_authorizationReasonParamName_authorizationReasonCode_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfRetracted.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_fileField_id_fieldCode_nobody.release();
    _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.release();
    _jspx_tagPool_ctl_validator_validator_nobody.release();
    _jspx_tagPool_ctl_phaseEvent_onPhases.release();
    _jspx_tagPool_rn_fileIterator_sortFilesByName_id.release();
    _jspx_tagPool_rn_fileChecker_requiredExtension.release();
    _jspx_tagPool_rn_fileField_fieldCode_nobody.release();
    _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_label_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n");
      //  rn:generatePdbWapPage
      org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage _jspx_th_rn_generatePdbWapPage_0 = (org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage) _jspx_tagPool_rn_generatePdbWapPage_title_pdbFileName_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage.class);
      _jspx_th_rn_generatePdbWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_generatePdbWapPage_0.setParent(null);
      _jspx_th_rn_generatePdbWapPage_0.setTitle("Generate a PDB File");
      _jspx_th_rn_generatePdbWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_generatePdbWapPage_0.setEditSamplePageHref("/lab/generatefiles.jsp");
      _jspx_th_rn_generatePdbWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_generatePdbWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      _jspx_th_rn_generatePdbWapPage_0.setFileNameParamName("crtFile");
      _jspx_th_rn_generatePdbWapPage_0.setPdbFileName((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['pdbName']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      int _jspx_eval_rn_generatePdbWapPage_0 = _jspx_th_rn_generatePdbWapPage_0.doStartTag();
      if (_jspx_eval_rn_generatePdbWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage pdbPage = null;
        if (_jspx_eval_rn_generatePdbWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_generatePdbWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_generatePdbWapPage_0.doInitBody();
        }
        pdbPage = (org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage) _jspx_page_context.findAttribute("pdbPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canEditSample.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generatePdbWapPage_0);
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
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_generatePdbWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:sampleChecker
          org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
          _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generatePdbWapPage_0);
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
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_errorMessage_0.setErrorFilter( GeneratePdbFileWapPage.BAD_CRT );
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n      <p class=\"errorMessage\">The specified file, ");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtFile']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write(", cannot\n      be parsed as a CRT file.</p>\n    ");
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
              out.write("\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_errorMessage_1.setErrorFilter( GeneratePdbFileWapPage.CRT_IO_ERROR );
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n      <p class=\"errorMessage\">The specified file, ");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param['crtFile']}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write(", could\n      not be read. The problem may be transient, so reloading this page may\n      help. If not then please notify the site administrator of the\n      problem.</p>\n    ");
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
              out.write("\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_errorMessage_2.setErrorFilter( GeneratePdbFileWapPage.PDB_IO_ERROR );
              int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_2.doInitBody();
                }
                do {
                  out.write("\n      <p class=\"errorMessage\">The specified file, ");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pdbPage.pdbFileName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write(", could\n      not be written. The problem may be transient, so clicking the appropriate\n      action button again may resolve it. If not then please notify the site\n      administrator of the problem.</p>\n    ");
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
              out.write("\n    ");
              //  ctl:errorChecker
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
              _jspx_th_ctl_errorChecker_0.setErrorFilter( ErrorChecker.ANY_ERROR
                                       ^ GeneratePdbFileWapPage.PDB_IO_ERROR );
              _jspx_th_ctl_errorChecker_0.setInvert(true);
              int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
              if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorChecker_0.doInitBody();
                }
                do {
                  out.write("\n      ");
                  //  ctl:selfForm
                  org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
                  _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
                  int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
                  if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_selfForm_0.doInitBody();
                    }
                    do {
                      out.write("\n        <table class=\"formTable\" cellspacing=\"0\">\n          <tr>\n            <td class=\"pageInstructions\" colspan=\"3\">Enter the name of the PDB\n            file to create, if different from the suggested name.  Optionally,\n            specify a description of the file, enter comments about this file\n            generation action, or both.  When ready, click the \"Create PDB\n            file\" button button to create a PDB file reflecting the content of\n            the specified CRT file, replacing any pre-existing repository file\n            of the same name.  If you do not wish to create a PDB file then\n            click the \"Cancel\" button.</td>\n          </tr>\n          <tr>\n            <th style=\"white-space: nowrap;\">CRT file name:</th>\n            <td>\n              ");
                      //  rn:fileField
                      org.recipnet.site.content.rncontrols.FileField crtFile = null;
                      org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_0 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                      _jspx_th_rn_fileField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                      _jspx_th_rn_fileField_0.setId("crtFile");
                      _jspx_th_rn_fileField_0.setFieldCode( FileField.FILENAME );
                      int _jspx_eval_rn_fileField_0 = _jspx_th_rn_fileField_0.doStartTag();
                      if (_jspx_th_rn_fileField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      crtFile = (org.recipnet.site.content.rncontrols.FileField) _jspx_page_context.findAttribute("crtFile");
                      _jspx_tagPool_rn_fileField_id_fieldCode_nobody.reuse(_jspx_th_rn_fileField_0);
                      out.write("\n            </td>\n            <th style=\"text-align: left; padding-left: 1em;\">PDB files already\n              in the repository:</th>\n          </tr>\n          <tr>\n            <th style=\"white-space: nowrap;\">PDB file name:</th>\n            <td>\n              ");
                      //  ctl:textbox
                      org.recipnet.common.controls.TextboxHtmlControl pdbName = null;
                      org.recipnet.common.controls.TextboxHtmlControl _jspx_th_ctl_textbox_0 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.get(org.recipnet.common.controls.TextboxHtmlControl.class);
                      _jspx_th_ctl_textbox_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_textbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                      _jspx_th_ctl_textbox_0.setId("pdbName");
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
                        pdbName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("pdbName");
                        do {
                          out.write("\n                ");
                          //  ctl:validator
                          org.recipnet.common.controls.ValidatorHtmlElement _jspx_th_ctl_validator_0 = (org.recipnet.common.controls.ValidatorHtmlElement) _jspx_tagPool_ctl_validator_validator_nobody.get(org.recipnet.common.controls.ValidatorHtmlElement.class);
                          _jspx_th_ctl_validator_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_validator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_textbox_0);
                          _jspx_th_ctl_validator_0.setValidator(new FilenameValidator());
                          int _jspx_eval_ctl_validator_0 = _jspx_th_ctl_validator_0.doStartTag();
                          if (_jspx_th_ctl_validator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_validator_validator_nobody.reuse(_jspx_th_ctl_validator_0);
                          out.write("\n                ");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:replaceTail(param['crtFile'], \".crt\", \"\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                          out.write(".pdb\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_textbox_0.doAfterBody();
                          pdbName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("pdbName");
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_textbox_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_textbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      pdbName = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("pdbName");
                      _jspx_tagPool_ctl_textbox_size_shouldConvertBlankToNull_required_id_failedValidationHtml.reuse(_jspx_th_ctl_textbox_0);
                      out.write("\n            </td>\n            <td rowspan=\"4\" style=\"vertical-align: top;\">\n              ");
                      if (_jspx_meth_ctl_phaseEvent_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      //  rn:fileIterator
                      org.recipnet.site.content.rncontrols.FileIterator fileIt = null;
                      org.recipnet.site.content.rncontrols.FileIterator _jspx_th_rn_fileIterator_0 = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_tagPool_rn_fileIterator_sortFilesByName_id.get(org.recipnet.site.content.rncontrols.FileIterator.class);
                      _jspx_th_rn_fileIterator_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_fileIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                      _jspx_th_rn_fileIterator_0.setId("fileIt");
                      _jspx_th_rn_fileIterator_0.setSortFilesByName(true);
                      int _jspx_eval_rn_fileIterator_0 = _jspx_th_rn_fileIterator_0.doStartTag();
                      if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_fileIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_fileIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_fileIterator_0.doInitBody();
                        }
                        fileIt = (org.recipnet.site.content.rncontrols.FileIterator) _jspx_page_context.findAttribute("fileIt");
                        do {
                          out.write("\n                ");
                          //  rn:fileChecker
                          org.recipnet.site.content.rncontrols.FileChecker _jspx_th_rn_fileChecker_0 = (org.recipnet.site.content.rncontrols.FileChecker) _jspx_tagPool_rn_fileChecker_requiredExtension.get(org.recipnet.site.content.rncontrols.FileChecker.class);
                          _jspx_th_rn_fileChecker_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_fileChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileIterator_0);
                          _jspx_th_rn_fileChecker_0.setRequiredExtension(".pdb");
                          int _jspx_eval_rn_fileChecker_0 = _jspx_th_rn_fileChecker_0.doStartTag();
                          if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_fileChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_fileChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_fileChecker_0.doInitBody();
                            }
                            do {
                              out.write("\n                  ");
                              if (_jspx_meth_ctl_phaseEvent_1(_jspx_th_rn_fileChecker_0, _jspx_page_context))
                              return;
                              out.write("\n                  <div id=\"");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pdbFullName.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                              out.write("\" class=\"fileName\">");
                              //  rn:fileField
                              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_1 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                              _jspx_th_rn_fileField_1.setPageContext(_jspx_page_context);
                              _jspx_th_rn_fileField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                              _jspx_th_rn_fileField_1.setFieldCode( FileField.FILENAME );
                              int _jspx_eval_rn_fileField_1 = _jspx_th_rn_fileField_1.doStartTag();
                              if (_jspx_th_rn_fileField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_1);
                              out.write("</div>\n                  <div class=\"fileDescription\">");
                              //  rn:fileField
                              org.recipnet.site.content.rncontrols.FileField _jspx_th_rn_fileField_2 = (org.recipnet.site.content.rncontrols.FileField) _jspx_tagPool_rn_fileField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.FileField.class);
                              _jspx_th_rn_fileField_2.setPageContext(_jspx_page_context);
                              _jspx_th_rn_fileField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_fileChecker_0);
                              _jspx_th_rn_fileField_2.setFieldCode( FileField.DESCRIPTION);
                              int _jspx_eval_rn_fileField_2 = _jspx_th_rn_fileField_2.doStartTag();
                              if (_jspx_th_rn_fileField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_rn_fileField_fieldCode_nobody.reuse(_jspx_th_rn_fileField_2);
                              out.write("</div>\n                ");
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
                          out.write("\n              ");
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
                      _jspx_tagPool_rn_fileIterator_sortFilesByName_id.reuse(_jspx_th_rn_fileIterator_0);
                      out.write("\n            </td>\n          </tr>\n          <tr>\n            <th style=\"white-space: nowrap;\">File description:</th>\n            <td>\n              ");
                      //  ctl:textarea
                      org.recipnet.common.controls.TextareaHtmlControl description = null;
                      org.recipnet.common.controls.TextareaHtmlControl _jspx_th_ctl_textarea_0 = (org.recipnet.common.controls.TextareaHtmlControl) _jspx_tagPool_ctl_textarea_shouldConvertBlankToNull_rows_id_failedValidationHtml_columns.get(org.recipnet.common.controls.TextareaHtmlControl.class);
                      _jspx_th_ctl_textarea_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_textarea_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
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
                          out.write("\n                ");
                          //  ctl:validator
                          org.recipnet.common.controls.ValidatorHtmlElement _jspx_th_ctl_validator_1 = (org.recipnet.common.controls.ValidatorHtmlElement) _jspx_tagPool_ctl_validator_validator_nobody.get(org.recipnet.common.controls.ValidatorHtmlElement.class);
                          _jspx_th_ctl_validator_1.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_validator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_textarea_0);
                          _jspx_th_ctl_validator_1.setValidator(new ContainerStringValidator());
                          int _jspx_eval_ctl_validator_1 = _jspx_th_ctl_validator_1.doStartTag();
                          if (_jspx_th_ctl_validator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_validator_validator_nobody.reuse(_jspx_th_ctl_validator_1);
                          out.write("\n              ");
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
                      out.write("\n              ");
                      if (_jspx_meth_ctl_phaseEvent_2(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n          </tr>\n          <tr>\n            <th style=\"white-space: nowrap;\">Comments:</th>\n            <td>");
                      if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                        return;
                      out.write("</td>\n          </tr>\n          <tr>\n            <td class=\"actionButton\" colspan=\"2\">\n              ");
                      if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                        return;
                      out.write("\n            </td>\n          </tr>\n        </table>\n      ");
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
                  out.write("\n      <script type=\"text/javascript\">\n        <!-- // hide script from ancient browsers\n          var unflaggedColor = \"black\"\n          var flaggedColor = \"red\"\n          var pdbNameElement = document.getElementById(\"pdbName\")\n          var flaggedElement\n\n          flagExistingFile(\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pdbName.value}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  out.write("\")\n          if (pdbNameElement) {\n            pdbNameElement.onkeyup = checkFileName\n            pdbNameElement.onblur = checkFileName\n            pdbNameElement.onchange = checkFileName\n          }\n\n          function checkFileName(e) {\n            if (!e) var e = window.event\n            var target = (e.target) ? e.target : e.srcElement\n\n            if (target.value) {\n              flagExistingFile(target.value)\n            }\n\n            return true\n          }\n\n          function flagExistingFile(fileName) {\n            if (flaggedElement) {\n              flaggedElement.style.color = unflaggedColor\n            }\n            flaggedElement = document.getElementById(fileName)\n            if (flaggedElement) {\n              flaggedElement.style.color = flaggedColor\n            }\n          }\n\n             // stop hiding -->\n      </script>\n    ");
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
              out.write('\n');
              out.write(' ');
              out.write(' ');
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
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_generatePdbWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_generatePdbWapPage_0.doAfterBody();
          pdbPage = (org.recipnet.site.content.rncontrols.GeneratePdbFileWapPage) _jspx_page_context.findAttribute("pdbPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_generatePdbWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_generatePdbWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_generatePdbWapPage_title_pdbFileName_loginPageUrl_fileNameParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_generatePdbWapPage_0);
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_generatePdbWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfRetracted.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generatePdbWapPage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfRetracted(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    <p class=\"errorMessage\">This sample has been retracted by its lab and no\n    files may be generated for it.</p>\n  ");
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

  private boolean _jspx_meth_ctl_phaseEvent_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_0 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_phaseEvent_0.setOnPhases("registration");
    int _jspx_eval_ctl_phaseEvent_0 = _jspx_th_ctl_phaseEvent_0.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_0.doInitBody();
      }
      do {
        out.write("\n                ");
        org.recipnet.site.wrapper.StringBean pdbFullName = null;
        synchronized (_jspx_page_context) {
          pdbFullName = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("pdbFullName", PageContext.PAGE_SCOPE);
          if (pdbFullName == null){
            pdbFullName = new org.recipnet.site.wrapper.StringBean();
            _jspx_page_context.setAttribute("pdbFullName", pdbFullName, PageContext.PAGE_SCOPE);
          }
        }
        out.write("\n              ");
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
    _jspx_th_ctl_phaseEvent_1.setOnPhases("rendering");
    int _jspx_eval_ctl_phaseEvent_1 = _jspx_th_ctl_phaseEvent_1.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_1.doInitBody();
      }
      do {
        out.write("\n                    ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("pdbFullName"), "string", "${fileIt.sampleDataFile.name}", _jspx_page_context, null);
        out.write("\n                  ");
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

  private boolean _jspx_meth_ctl_phaseEvent_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  ctl:phaseEvent
    org.recipnet.common.controls.HtmlPagePhaseEvent _jspx_th_ctl_phaseEvent_2 = (org.recipnet.common.controls.HtmlPagePhaseEvent) _jspx_tagPool_ctl_phaseEvent_onPhases.get(org.recipnet.common.controls.HtmlPagePhaseEvent.class);
    _jspx_th_ctl_phaseEvent_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_phaseEvent_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_phaseEvent_2.setOnPhases("fetching");
    int _jspx_eval_ctl_phaseEvent_2 = _jspx_th_ctl_phaseEvent_2.doStartTag();
    if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_phaseEvent_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_phaseEvent_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_phaseEvent_2.doInitBody();
      }
      do {
        out.write("\n                ");
        org.apache.jasper.runtime.JspRuntimeLibrary.handleSetPropertyExpression(_jspx_page_context.findAttribute("pdbPage"), "fileDescription", "${description.value}", _jspx_page_context, null);
        out.write("\n              ");
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

  private boolean _jspx_meth_rn_wapComments_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapComments
    org.recipnet.site.content.rncontrols.WapComments _jspx_th_rn_wapComments_0 = (org.recipnet.site.content.rncontrols.WapComments) _jspx_tagPool_rn_wapComments_nobody.get(org.recipnet.site.content.rncontrols.WapComments.class);
    _jspx_th_rn_wapComments_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapComments_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
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
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_label_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_wapSaveButton_0.setLabel("Create PDB file");
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_label_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_generatePdbWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_generatePdbWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    table.formTable { width: 60em; margin: 1em auto 0 auto;\n        font-family: Arial, Helvetica, Verdana, sans-serif; }\n    table.formTable td, table.formTable th {\n        vertical-align: center; padding: 0.25em; }\n    table.formTable th { text-align: left; padding-left: 1em; }\n    table.formTable td.pageInstructions { padding-bottom: 1em; }\n    table.formTable td.actionButton { text-align: center; padding-top: 1em; }\n    td.actionButton input { padding-left: 0.2em; padding-right: 0.2em }\n    div.fileName { margin-top: 0.25em; padding-left: 1em;}\n    div.fileDescription { font-style: italic; margin: 0 0 0 2em; color: gray; \n        width: 20em;}\n    .errorMessage { color: red; text-align: center; }\n  ");
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
