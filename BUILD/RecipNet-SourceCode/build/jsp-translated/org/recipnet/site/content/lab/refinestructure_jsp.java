package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.CifFileContext;
import org.recipnet.site.content.rncontrols.ValidationOverrideButton;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleDataInfo;

public final class refinestructure_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_filenames;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifChooser_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_file_fileName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifFile_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_fieldCode;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_label_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_filenames = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifChooser_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_file_fileName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifFile_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_fieldCode = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_label_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_filenames.release();
    _jspx_tagPool_rn_cifChooser_id_nobody.release();
    _jspx_tagPool_rn_file_fileName.release();
    _jspx_tagPool_rn_cifFile_id_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_nobody.release();
    _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif.release();
    _jspx_tagPool_rn_sampleField_id_fieldCode.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.release();
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_button_label_editable_nobody.release();
    _jspx_tagPool_rn_ltaIterator_id.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n");
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Record Refinement Results");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode( SampleWorkflowBL.STRUCTURE_REFINED );
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(
      SampleWorkflowBL.STRUCTURE_REFINED_CORRECTED );
      _jspx_th_rn_wapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_wapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_wapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_wapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_wapPage_0 = _jspx_th_rn_wapPage_0.doStartTag();
      if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_wapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_wapPage_0.doInitBody();
        }
        do {
          out.write("\n  <div class=\"pageBody\">\n  <p class=\"pageInstructions\">\n    Enter the structure refinement data on the form below and click\n    the \"Save\" button to record it.\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${htmlPage}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
          _jspx_th_ctl_errorMessage_0.setErrorFilter(
      HtmlPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("<br/>\n      <span class=\"errorMessage\"\n            style=\"font-weight: normal; font-style: italic;\">\n        You must address the flagged validation errors before the data\n        will be accepted.\n      </span>\n    ");
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
          out.write("\n  </p>\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    ");
              //  rn:filenames
              org.recipnet.site.content.rncontrols.SimpleMultiFilenameContext _jspx_th_rn_filenames_0 = (org.recipnet.site.content.rncontrols.SimpleMultiFilenameContext) _jspx_tagPool_rn_filenames.get(org.recipnet.site.content.rncontrols.SimpleMultiFilenameContext.class);
              _jspx_th_rn_filenames_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_filenames_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              int _jspx_eval_rn_filenames_0 = _jspx_th_rn_filenames_0.doStartTag();
              if (_jspx_eval_rn_filenames_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_filenames_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_filenames_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_filenames_0.doInitBody();
                }
                do {
                  out.write("\n      ");
                  //  rn:cifChooser
                  org.recipnet.site.content.rncontrols.PreferredCifChooser cifChooser = null;
                  org.recipnet.site.content.rncontrols.PreferredCifChooser _jspx_th_rn_cifChooser_0 = (org.recipnet.site.content.rncontrols.PreferredCifChooser) _jspx_tagPool_rn_cifChooser_id_nobody.get(org.recipnet.site.content.rncontrols.PreferredCifChooser.class);
                  _jspx_th_rn_cifChooser_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_cifChooser_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenames_0);
                  _jspx_th_rn_cifChooser_0.setId("cifChooser");
                  int _jspx_eval_rn_cifChooser_0 = _jspx_th_rn_cifChooser_0.doStartTag();
                  if (_jspx_th_rn_cifChooser_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cifChooser = (org.recipnet.site.content.rncontrols.PreferredCifChooser) _jspx_page_context.findAttribute("cifChooser");
                  _jspx_tagPool_rn_cifChooser_id_nobody.reuse(_jspx_th_rn_cifChooser_0);
                  out.write("\n      ");
                  //  rn:file
                  org.recipnet.site.content.rncontrols.SimpleFileContext _jspx_th_rn_file_0 = (org.recipnet.site.content.rncontrols.SimpleFileContext) _jspx_tagPool_rn_file_fileName.get(org.recipnet.site.content.rncontrols.SimpleFileContext.class);
                  _jspx_th_rn_file_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_file_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_filenames_0);
                  _jspx_th_rn_file_0.setFileName((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                  int _jspx_eval_rn_file_0 = _jspx_th_rn_file_0.doStartTag();
                  if (_jspx_eval_rn_file_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_file_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_file_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_file_0.doInitBody();
                    }
                    do {
                      out.write("\n        ");
                      //  rn:cifFile
                      org.recipnet.site.content.rncontrols.CifFileContext cif = null;
                      org.recipnet.site.content.rncontrols.CifFileContext _jspx_th_rn_cifFile_0 = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_tagPool_rn_cifFile_id_nobody.get(org.recipnet.site.content.rncontrols.CifFileContext.class);
                      _jspx_th_rn_cifFile_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_cifFile_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_file_0);
                      _jspx_th_rn_cifFile_0.setId("cif");
                      int _jspx_eval_rn_cifFile_0 = _jspx_th_rn_cifFile_0.doStartTag();
                      if (_jspx_th_rn_cifFile_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      cif = (org.recipnet.site.content.rncontrols.CifFileContext) _jspx_page_context.findAttribute("cif");
                      _jspx_tagPool_rn_cifFile_id_nobody.reuse(_jspx_th_rn_cifFile_0);
                      out.write("\n      ");
                      int evalDoAfterBody = _jspx_th_rn_file_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_file_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_file_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_file_fileName.reuse(_jspx_th_rn_file_0);
                  out.write("\n    ");
                  int evalDoAfterBody = _jspx_th_rn_filenames_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_filenames_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_filenames_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_filenames.reuse(_jspx_th_rn_filenames_0);
              out.write("\n    <table class=\"bodyTable\">\n      <tr>\n        <th class=\"leadSectionHead\" colspan=\"8\">General Information</th>\n      </tr>\n      <tr>\n        <th colspan=\"2\">\n          ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write(":\n        </th>\n        <td colspan=\"6\">\n          ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
              _jspx_th_rn_sampleField_0.setFieldCode(
              SampleInfo.LOCAL_LAB_ID );
              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th colspan=\"2\">Originating Lab:</th>\n        <td colspan=\"6\">");
              if (_jspx_meth_rn_labField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n      </tr>\n      <tr>\n        <th colspan=\"2\">Originating Provider:</th>\n        <td colspan=\"6\">\n          ");
              if (_jspx_meth_rn_providerField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th style=\"vertical-align: top;\" colspan=\"2\">\n          ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleDataInfo.SUMMARY_FIELD);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write(":\n        </th>\n        <td colspan=\"6\">\n          ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_1.setFieldCode(SampleDataInfo.SUMMARY_FIELD);
              int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
              if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_1);
              out.write("\n        </td>\n      </tr>\n      ");
              //  rn:cifSampleFilter
              org.recipnet.site.content.rncontrols.CifSampleContextFilter _jspx_th_rn_cifSampleFilter_0 = (org.recipnet.site.content.rncontrols.CifSampleContextFilter) _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif.get(org.recipnet.site.content.rncontrols.CifSampleContextFilter.class);
              _jspx_th_rn_cifSampleFilter_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_cifSampleFilter_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_cifSampleFilter_0.setCif((org.recipnet.common.files.CifFile) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif.cifFile}", org.recipnet.common.files.CifFile.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_rn_cifSampleFilter_0.setFixShelxCifs(true);
              _jspx_th_rn_cifSampleFilter_0.setEnabled(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${loadFromCif.valueAsBoolean\n          || loadFromBuggyCif.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
              int _jspx_eval_rn_cifSampleFilter_0 = _jspx_th_rn_cifSampleFilter_0.doStartTag();
              if (_jspx_eval_rn_cifSampleFilter_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_cifSampleFilter_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_cifSampleFilter_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_cifSampleFilter_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th colspan=\"8\" class=\"sectionHead\" style=\"padding-top: 0.5em\">\n            Crystal Data\n          </th>\n        </tr>\n        <tr>\n          <th colspan=\"2\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_2.setFieldCode(SampleDataInfo.A_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
                  out.write(":\n          </th>\n          <td colspan=\"2\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellA = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_2.setFieldCode( SampleDataInfo.A_FIELD );
                  _jspx_th_rn_sampleField_2.setId("cellA");
                  int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
                  if (_jspx_eval_rn_sampleField_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_2.doInitBody();
                    }
                    cellA = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellA");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_sampleField_2, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_rn_sampleField_2, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_2.doAfterBody();
                      cellA = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellA");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellA = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellA");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_2);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_0.setFieldCode(SampleDataInfo.A_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
                  out.write("\n          </td>\n          <th class=\"nonLeadColumn\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_3.setFieldCode(SampleDataInfo.ALPHA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
                  out.write(":\n          </th>\n          <td colspan=\"3\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellAlpha = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_3.setFieldCode( SampleDataInfo.ALPHA_FIELD );
                  _jspx_th_rn_sampleField_3.setId("cellAlpha");
                  int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
                  if (_jspx_eval_rn_sampleField_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_3.doInitBody();
                    }
                    cellAlpha = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellAlpha");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_2(_jspx_th_rn_sampleField_3, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_3.doAfterBody();
                      cellAlpha = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellAlpha");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellAlpha = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellAlpha");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_3);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_1 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_1.setFieldCode(SampleDataInfo.ALPHA_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_1 = _jspx_th_rn_sampleFieldUnits_1.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_1);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_4 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_4.setFieldCode(SampleDataInfo.B_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_4 = _jspx_th_rn_sampleFieldLabel_4.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_4);
                  out.write(":\n          </th>\n          <td colspan=\"2\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellB = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_4.setFieldCode( SampleDataInfo.B_FIELD );
                  _jspx_th_rn_sampleField_4.setId("cellB");
                  int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
                  if (_jspx_eval_rn_sampleField_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_4.doInitBody();
                    }
                    cellB = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellB");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_3(_jspx_th_rn_sampleField_4, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_4(_jspx_th_rn_sampleField_4, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_4.doAfterBody();
                      cellB = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellB");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellB = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellB");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_4);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_2 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_2.setFieldCode(SampleDataInfo.B_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_2 = _jspx_th_rn_sampleFieldUnits_2.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_2);
                  out.write("\n          </td>\n          <th class=\"nonLeadColumn\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_5 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_5.setFieldCode(SampleDataInfo.BETA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_5 = _jspx_th_rn_sampleFieldLabel_5.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_5);
                  out.write(":\n          </th>\n          <td colspan=\"3\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellBeta = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_5.setFieldCode( SampleDataInfo.BETA_FIELD );
                  _jspx_th_rn_sampleField_5.setId("cellBeta");
                  int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
                  if (_jspx_eval_rn_sampleField_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_5.doInitBody();
                    }
                    cellBeta = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellBeta");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_5(_jspx_th_rn_sampleField_5, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_5.doAfterBody();
                      cellBeta = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellBeta");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellBeta = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellBeta");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_5);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_3 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_3.setFieldCode(SampleDataInfo.BETA_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_3 = _jspx_th_rn_sampleFieldUnits_3.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_3);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_6 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_6.setFieldCode(SampleDataInfo.C_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_6 = _jspx_th_rn_sampleFieldLabel_6.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_6);
                  out.write(":\n          </th>\n          <td colspan=\"2\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellC = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_6.setFieldCode( SampleDataInfo.C_FIELD );
                  _jspx_th_rn_sampleField_6.setId("cellC");
                  int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
                  if (_jspx_eval_rn_sampleField_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_6.doInitBody();
                    }
                    cellC = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellC");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_6(_jspx_th_rn_sampleField_6, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_7(_jspx_th_rn_sampleField_6, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_6.doAfterBody();
                      cellC = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellC");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellC = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellC");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_6);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_4 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_4.setFieldCode(SampleDataInfo.C_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_4 = _jspx_th_rn_sampleFieldUnits_4.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_4);
                  out.write("\n          </td>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_7 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_7.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_7.setFieldCode(SampleDataInfo.GAMMA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_7 = _jspx_th_rn_sampleFieldLabel_7.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_7);
                  out.write(":\n          </th>\n          <td colspan=\"3\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField cellGamma = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_7 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_7.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_7.setFieldCode( SampleDataInfo.GAMMA_FIELD );
                  _jspx_th_rn_sampleField_7.setId("cellGamma");
                  int _jspx_eval_rn_sampleField_7 = _jspx_th_rn_sampleField_7.doStartTag();
                  if (_jspx_eval_rn_sampleField_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_7.doInitBody();
                    }
                    cellGamma = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellGamma");
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_8(_jspx_th_rn_sampleField_7, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_7.doAfterBody();
                      cellGamma = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellGamma");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  cellGamma = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("cellGamma");
                  _jspx_tagPool_rn_sampleField_id_fieldCode.reuse(_jspx_th_rn_sampleField_7);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_5 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_5.setFieldCode(SampleDataInfo.GAMMA_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_5 = _jspx_th_rn_sampleFieldUnits_5.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_5);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_8 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_8.setFieldCode(SampleDataInfo.SPGP_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_8 = _jspx_th_rn_sampleFieldLabel_8.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_8);
                  out.write(":\n          </th>\n          <td colspan=\"2\">\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField spaceGroup = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_8 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_8.setId("spaceGroup");
                  _jspx_th_rn_sampleField_8.setFieldCode(SampleDataInfo.SPGP_FIELD);
                  int _jspx_eval_rn_sampleField_8 = _jspx_th_rn_sampleField_8.doStartTag();
                  if (_jspx_th_rn_sampleField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  spaceGroup = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("spaceGroup");
                  _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_8);
                  out.write("\n          </td>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_9 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_9.setFieldCode(SampleDataInfo.Z_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_9 = _jspx_th_rn_sampleFieldLabel_9.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_9);
                  out.write(":\n          </th>\n          <td colspan=\"3\">\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_9 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_9.setFieldCode(SampleDataInfo.Z_FIELD);
                  int _jspx_eval_rn_sampleField_9 = _jspx_th_rn_sampleField_9.doStartTag();
                  if (_jspx_th_rn_sampleField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_9);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"2\"></td>\n          <td colspan=\"6\">\n            ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${spaceGroup}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n              <div class=\"errorNotice\" style=\"max-width: 40em;\">\n                The entered space group symbol is invalid: either it\n                does not represent a valid space group or its format is\n                incorrect or unsupported.  (Hermann-Mauguin symbols in\n                CIF format (preferred), SHELX format, and some variations\n                are supported.)\n              </div>\n              <div class=\"notice\" style=\"max-width: 40em;\">\n                Correct the symbol and click \"Save\" to resubmit, or click\n                \"Override\" to record the invalid symbol.\n              </div>\n              <div class=\"notice\" style=\"max-width: 40em;\">\n                WARNING: invalid space group symbols are not searchable.\n              </div>\n            ");
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
                  out.write("\n            ");
                  //  rn:overrideValidationButton
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton overrideButton = null;
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_0 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                  _jspx_th_rn_overrideValidationButton_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_overrideValidationButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_overrideValidationButton_0.setSampleField((org.recipnet.site.content.rncontrols.SampleField) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${spaceGroup}", org.recipnet.site.content.rncontrols.SampleField.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_overrideValidationButton_0.setLabel("Override");
                  _jspx_th_rn_overrideValidationButton_0.setId("overrideButton");
                  int _jspx_eval_rn_overrideValidationButton_0 = _jspx_th_rn_overrideValidationButton_0.doStartTag();
                  if (_jspx_th_rn_overrideValidationButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  overrideButton = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("overrideButton");
                  _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_0);
                  out.write("\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${overrideButton}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorChecker_0.setErrorFilter(
                      ValidationOverrideButton.VALIDATION_OVERRIDDEN);
                  int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n              <span class=\"notice\">(validation overridden)</span>\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_0);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"8\" class=\"sectionHead\">\n            Agreement Statistics\n          </th>\n        </tr>\n        <tr>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_10 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_10.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_10.setFieldCode(SampleDataInfo.RF_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_10 = _jspx_th_rn_sampleFieldLabel_10.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_10);
                  out.write(":\n          </th>\n          <td>\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_10 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_10.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_10.setFieldCode(SampleDataInfo.RF_FIELD);
                  int _jspx_eval_rn_sampleField_10 = _jspx_th_rn_sampleField_10.doStartTag();
                  if (_jspx_th_rn_sampleField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_10);
                  out.write("\n          </td>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_11 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_11.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_11.setFieldCode(SampleDataInfo.RF2_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_11 = _jspx_th_rn_sampleFieldLabel_11.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_11);
                  out.write(":\n          </th>\n          <td>\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_11 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_11.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_11.setFieldCode(SampleDataInfo.RF2_FIELD);
                  int _jspx_eval_rn_sampleField_11 = _jspx_th_rn_sampleField_11.doStartTag();
                  if (_jspx_th_rn_sampleField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_11);
                  out.write("\n          </td>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_12 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_12.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_12.setFieldCode(SampleDataInfo.RWF_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_12 = _jspx_th_rn_sampleFieldLabel_12.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_12);
                  out.write(":\n          </th>\n          <td>\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_12 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_12.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_12.setFieldCode(SampleDataInfo.RWF_FIELD);
                  int _jspx_eval_rn_sampleField_12 = _jspx_th_rn_sampleField_12.doStartTag();
                  if (_jspx_th_rn_sampleField_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_12);
                  out.write("\n          </td>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_13 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_13.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_13.setFieldCode(SampleDataInfo.RWF2_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_13 = _jspx_th_rn_sampleFieldLabel_13.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_13);
                  out.write(":\n          </th>\n          <td>\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_13 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_13.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_13.setFieldCode(SampleDataInfo.RWF2_FIELD);
                  int _jspx_eval_rn_sampleField_13 = _jspx_th_rn_sampleField_13.doStartTag();
                  if (_jspx_th_rn_sampleField_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_13);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"3\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_14 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_14.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_14.setFieldCode(SampleDataInfo.GOOF_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_14 = _jspx_th_rn_sampleFieldLabel_14.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_14);
                  out.write(":\n          </th>\n          <td colspan=\"5\">\n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_14 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_14.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_14.setFieldCode(SampleDataInfo.GOOF_FIELD);
                  int _jspx_eval_rn_sampleField_14 = _jspx_th_rn_sampleField_14.doStartTag();
                  if (_jspx_th_rn_sampleField_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_14);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"8\" class=\"sectionHead\">Names and Formulae</th>\n        </tr>\n        <tr>\n          <td colspan=\"4\" style=\"vertical-align: top;\">\n            <table cellspacing=\"0\">\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_15 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_15.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_15.setFieldCode(SampleTextBL.EMPIRICAL_FORMULA);
                  int _jspx_eval_rn_sampleFieldLabel_15 = _jspx_th_rn_sampleFieldLabel_15.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_15);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField empForm = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_15 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_15.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_15.setId("empForm");
                  _jspx_th_rn_sampleField_15.setFieldCode(
                      SampleTextBL.EMPIRICAL_FORMULA );
                  int _jspx_eval_rn_sampleField_15 = _jspx_th_rn_sampleField_15.doStartTag();
                  if (_jspx_th_rn_sampleField_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  empForm = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("empForm");
                  _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_15);
                  out.write("\n                  ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empForm}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_2.setErrorFilter(
                          SampleField.VALIDATOR_REJECTED_VALUE);
                  int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_2.doInitBody();
                    }
                    do {
                      out.write("\n                    <div class=\"errorNotice\" style=\"max-width: 20em;\">\n                      The entered empirical formula is invalid.\n                    </div>\n                    <div class=\"notice\" style=\"max-width: 20em;\">\n                      Correct the formula and click \"Save\" to resubmit, or\n                      click \"Override\" to record the invalid formula.\n                    </div>\n                    <div class=\"notice\" style=\"max-width: 20em;\">\n                      WARNING: invalid formulae may not be searchable.\n                    </div>\n                  ");
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
                  out.write("\n                  ");
                  //  rn:overrideValidationButton
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton overrideEFButton = null;
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_1 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                  _jspx_th_rn_overrideValidationButton_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_overrideValidationButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_overrideValidationButton_1.setSampleField((org.recipnet.site.content.rncontrols.SampleField) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empForm}", org.recipnet.site.content.rncontrols.SampleField.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_overrideValidationButton_1.setLabel("Override");
                  _jspx_th_rn_overrideValidationButton_1.setId("overrideEFButton");
                  int _jspx_eval_rn_overrideValidationButton_1 = _jspx_th_rn_overrideValidationButton_1.doStartTag();
                  if (_jspx_th_rn_overrideValidationButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  overrideEFButton = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("overrideEFButton");
                  _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_1);
                  out.write("\n                  ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorMessage_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${overrideEFButton}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_3.setErrorFilter(
                      ValidationOverrideButton.VALIDATION_OVERRIDDEN);
                  int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_3.doInitBody();
                    }
                    do {
                      out.write("\n                    <span class=\"notice\">(validation overridden)</span>\n                  ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_3);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_16 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_16.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_16.setFieldCode(SampleTextBL.MOIETY_FORMULA);
                  int _jspx_eval_rn_sampleFieldLabel_16 = _jspx_th_rn_sampleFieldLabel_16.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_16);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField moiety = null;
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_16 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_16.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_16.setId("moiety");
                  _jspx_th_rn_sampleField_16.setFieldCode(
                      SampleTextBL.MOIETY_FORMULA );
                  int _jspx_eval_rn_sampleField_16 = _jspx_th_rn_sampleField_16.doStartTag();
                  if (_jspx_th_rn_sampleField_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  moiety = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("moiety");
                  _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_16);
                  out.write("\n                  ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorMessage_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${moiety}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_4.setErrorFilter(
                      SampleField.VALIDATOR_REJECTED_VALUE);
                  int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_4.doInitBody();
                    }
                    do {
                      out.write("\n                    <div class=\"errorNotice\" style=\"max-width: 20em;\">\n                      The entered moiety formula is invalid.\n                    </div>\n                    <div class=\"notice\" style=\"max-width: 20em;\">\n                      Correct the formula and click \"Save\" to resubmit, or\n                      click \"Override\" to record the invalid formula.\n                    </div>\n                    <div class=\"notice\" style=\"max-width: 20em;\">\n                      WARNING: invalid formulae may not be searchable.\n                    </div>\n                  ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_4);
                  out.write("\n                  ");
                  //  rn:overrideValidationButton
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton overrideMoietyButton = null;
                  org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_2 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                  _jspx_th_rn_overrideValidationButton_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_overrideValidationButton_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_overrideValidationButton_2.setSampleField((org.recipnet.site.content.rncontrols.SampleField) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${moiety}", org.recipnet.site.content.rncontrols.SampleField.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_rn_overrideValidationButton_2.setLabel("Override");
                  _jspx_th_rn_overrideValidationButton_2.setId("overrideMoietyButton");
                  int _jspx_eval_rn_overrideValidationButton_2 = _jspx_th_rn_overrideValidationButton_2.doStartTag();
                  if (_jspx_th_rn_overrideValidationButton_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  overrideMoietyButton = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("overrideMoietyButton");
                  _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_2);
                  out.write("\n                  ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorMessage_5.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${overrideMoietyButton}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorMessage_5.setErrorFilter(
                       ValidationOverrideButton.VALIDATION_OVERRIDDEN);
                  int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_5.doInitBody();
                    }
                    do {
                      out.write("\n                    <span class=\"notice\">(validation overridden)</span>\n                  ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_5.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_5);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_17 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_17.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_17.setFieldCode(SampleTextBL.STRUCTURAL_FORMULA);
                  int _jspx_eval_rn_sampleFieldLabel_17 = _jspx_th_rn_sampleFieldLabel_17.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_17);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_17 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_17.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_17.setFieldCode(
                      SampleTextBL.STRUCTURAL_FORMULA );
                  int _jspx_eval_rn_sampleField_17 = _jspx_th_rn_sampleField_17.doStartTag();
                  if (_jspx_th_rn_sampleField_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_17);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_18 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_18.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_18.setFieldCode(SampleTextBL.SMILES_FORMULA);
                  int _jspx_eval_rn_sampleFieldLabel_18 = _jspx_th_rn_sampleFieldLabel_18.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_18);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_18 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_18.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_18.setFieldCode(
                      SampleTextBL.SMILES_FORMULA );
                  int _jspx_eval_rn_sampleField_18 = _jspx_th_rn_sampleField_18.doStartTag();
                  if (_jspx_th_rn_sampleField_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_18);
                  out.write("\n                </td>\n              </tr>\n            </table>\n          </td>\n          <td colspan=\"4\" style=\"vertical-align: top\">\n            <table cellspacing=\"0\">\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_19 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_19.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_19.setFieldCode(SampleTextBL.TRADE_NAME);
                  int _jspx_eval_rn_sampleFieldLabel_19 = _jspx_th_rn_sampleFieldLabel_19.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_19);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_19 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_19.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_19.setFieldCode( SampleTextBL.TRADE_NAME );
                  int _jspx_eval_rn_sampleField_19 = _jspx_th_rn_sampleField_19.doStartTag();
                  if (_jspx_th_rn_sampleField_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_19);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_20 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_20.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_20.setFieldCode(SampleTextBL.COMMON_NAME);
                  int _jspx_eval_rn_sampleFieldLabel_20 = _jspx_th_rn_sampleFieldLabel_20.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_20);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_20 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_20.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_20.setFieldCode( SampleTextBL.COMMON_NAME );
                  int _jspx_eval_rn_sampleField_20 = _jspx_th_rn_sampleField_20.doStartTag();
                  if (_jspx_th_rn_sampleField_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_20);
                  out.write("\n                </td>\n              </tr>\n              <tr>\n                <th class=\"multiboxLabel\">\n                  ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_21 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_21.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_21.setFieldCode(SampleTextBL.IUPAC_NAME);
                  int _jspx_eval_rn_sampleFieldLabel_21 = _jspx_th_rn_sampleFieldLabel_21.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_21);
                  out.write(":\n                </th>\n                <td>\n                  ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_21 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_21.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_21.setFieldCode( SampleTextBL.IUPAC_NAME );
                  int _jspx_eval_rn_sampleField_21 = _jspx_th_rn_sampleField_21.doStartTag();
                  if (_jspx_th_rn_sampleField_21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_21);
                  out.write("\n                </td>\n              </tr>\n            </table>\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"8\" class=\"actionButton\">\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorChecker_1.setInvert(true);
                  int _jspx_eval_ctl_errorChecker_1 = _jspx_th_ctl_errorChecker_1.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_1.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:button
                      org.recipnet.common.controls.ButtonHtmlControl loadFromCif = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_1);
                      _jspx_th_ctl_button_0.setId("loadFromCif");
                      _jspx_th_ctl_button_0.setSuppressInsteadOfSkip(true);
                      _jspx_th_ctl_button_0.setLabel((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("Load crystal data from ${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      int _jspx_eval_ctl_button_0 = _jspx_th_ctl_button_0.doStartTag();
                      if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_button_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_button_0.doInitBody();
                        }
                        loadFromCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromCif");
                        do {
                          out.write("\n                ");
                          if (_jspx_meth_ctl_extraHtmlAttribute_9(_jspx_th_ctl_button_0, _jspx_page_context))
                            return;
                          out.write("\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_button_0.doAfterBody();
                          loadFromCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromCif");
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      loadFromCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromCif");
                      _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.reuse(_jspx_th_ctl_button_0);
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.reuse(_jspx_th_ctl_errorChecker_1);
                  out.write("\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorChecker_2.setErrorFilter(CifFileContext.CIF_HAS_ERRORS );
                  int _jspx_eval_ctl_errorChecker_2 = _jspx_th_ctl_errorChecker_2.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_2.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:button
                      org.recipnet.common.controls.ButtonHtmlControl loadFromBuggyCif = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_1 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
                      _jspx_th_ctl_button_1.setId("loadFromBuggyCif");
                      _jspx_th_ctl_button_1.setSuppressInsteadOfSkip(true);
                      _jspx_th_ctl_button_1.setLabel((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("Load crystal data from ${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      int _jspx_eval_ctl_button_1 = _jspx_th_ctl_button_1.doStartTag();
                      if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_button_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_button_1.doInitBody();
                        }
                        loadFromBuggyCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromBuggyCif");
                        do {
                          out.write("\n                ");
                          if (_jspx_meth_ctl_extraHtmlAttribute_10(_jspx_th_ctl_button_1, _jspx_page_context))
                            return;
                          out.write("\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_button_1.doAfterBody();
                          loadFromBuggyCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromBuggyCif");
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_button_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      loadFromBuggyCif = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("loadFromBuggyCif");
                      _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.reuse(_jspx_th_ctl_button_1);
                      out.write("<br/>\n              <span class=\"errorNotice\">Warning: CIF '");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("'\n                  contains errors</span>\n            ");
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
                  out.write("\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  int _jspx_eval_ctl_errorChecker_3 = _jspx_th_ctl_errorChecker_3.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_3.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_4.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
                      _jspx_th_ctl_errorChecker_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_4.setInvert(true);
                      _jspx_th_ctl_errorChecker_4.setErrorFilter(CifFileContext.CIF_HAS_ERRORS );
                      int _jspx_eval_ctl_errorChecker_4 = _jspx_th_ctl_errorChecker_4.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_4.doInitBody();
                        }
                        do {
                          out.write("\n                ");
                          if (_jspx_meth_ctl_button_2(_jspx_th_ctl_errorChecker_4, _jspx_page_context))
                            return;
                          out.write("<br/>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_4.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_4);
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
                      _jspx_th_ctl_errorChecker_5.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_5.setErrorFilter(CifFileContext.UNPARSEABLE_CIF );
                      int _jspx_eval_ctl_errorChecker_5 = _jspx_th_ctl_errorChecker_5.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_5.doInitBody();
                        }
                        do {
                          out.write("\n                <span class=\"errorNotice\">\n                  Warning: CIF '");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          out.write("' could not be parsed\n                </span>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_5.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_5);
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_6.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
                      _jspx_th_ctl_errorChecker_6.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_6.setErrorFilter(CifFileContext.EMPTY_CIF );
                      int _jspx_eval_ctl_errorChecker_6 = _jspx_th_ctl_errorChecker_6.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_6.doInitBody();
                        }
                        do {
                          out.write("\n                <span class=\"errorNotice\">\n                  Warning: CIF '");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          out.write("' contains no data\n                  blocks\n                </span>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_6.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_6);
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_3.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_errorSupplier.reuse(_jspx_th_ctl_errorChecker_3);
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_cifSampleFilter_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_cifSampleFilter_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_cifSampleFilter_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif.reuse(_jspx_th_rn_cifSampleFilter_0);
              out.write("\n      ");
              //  rn:ltaIterator
              org.recipnet.site.content.rncontrols.LtaIterator ltas = null;
              org.recipnet.site.content.rncontrols.LtaIterator _jspx_th_rn_ltaIterator_0 = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_tagPool_rn_ltaIterator_id.get(org.recipnet.site.content.rncontrols.LtaIterator.class);
              _jspx_th_rn_ltaIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_ltaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_ltaIterator_0.setId("ltas");
              int _jspx_eval_rn_ltaIterator_0 = _jspx_th_rn_ltaIterator_0.doStartTag();
              if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_ltaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_ltaIterator_0.doInitBody();
                }
                ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
                do {
                  out.write("\n        ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_7.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
                  _jspx_th_ctl_errorChecker_7.setErrorFilter( HtmlPageIterator.NO_ITERATIONS );
                  _jspx_th_ctl_errorChecker_7.setInvert(true);
                  int _jspx_eval_ctl_errorChecker_7 = _jspx_th_ctl_errorChecker_7.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_7.doInitBody();
                    }
                    do {
                      out.write("\n          ");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltas.currentIterationFirst ?\n            '<tr>\n              <th class=\"sectionHead\" colspan=\"8\">Additional Information</th>\n            </tr>' : ''}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\n        ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_invert_errorFilter.reuse(_jspx_th_ctl_errorChecker_7);
                  out.write("\n        <tr>\n          <th colspan=\"2\">\n            ");
                  if (_jspx_meth_rn_sampleFieldLabel_22(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  out.write(":\n          </th>\n          <td colspan=\"6\">\n            ");
                  if (_jspx_meth_rn_sampleField_22(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  if (_jspx_meth_rn_sampleFieldUnits_6(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_ltaIterator_0.doAfterBody();
                  ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_ltaIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              ltas = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_page_context.findAttribute("ltas");
              _jspx_tagPool_rn_ltaIterator_id.reuse(_jspx_th_rn_ltaIterator_0);
              out.write("\n      <tr>\n        <th class=\"sectionHead\" colspan=\"8\">Refinement Comments</th>\n      </tr>\n      <tr>\n        <td colspan=\"8\" style=\"text-align: center\">\n          ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"8\" class=\"formButtons\">\n          ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write(' ');
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n    </table>\n  ");
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
          out.write("\n  <script type=\"text/javascript\">\n    <!-- // hide from ancient browsers\n    \n    // A function that sets the values of cellAlpha, cellBeta and cellGamma to\n    // 90 if none of them were previously set and at least one of cellA, cellB\n    // and cellC has been set.\n    function setVals() {\n        if (!(getElement(\"cellA\").value == \"\"\n                && getElement(\"cellB\").value == \"\"\n                && getElement(\"cellC\").value == \"\")) {\n            var cellAlpha = getElement(\"cellAlpha\");\n            var cellBeta = getElement(\"cellBeta\");\n            var cellGamma = getElement(\"cellGamma\");\n            if (cellAlpha.value == \"\" && cellBeta.value == \"\"\n                    && cellGamma.value == \"\") {\n                cellAlpha.value = \"90.00\";\n                cellBeta.value = \"90.00\";\n                cellGamma.value = \"90.00\";\n            }\n        }\n    }\n\n    // A function that finds the first element that starts with the provided\n    // name.  This is useful because the HTML form element for an\n    // HtmlPageElement is guaranteed to have a name that starts with the id\n");
          out.write("    // value assigned to that element.\n    function getElement(name) {\n        var index = 0;\n        while (index < document.forms[1].elements.length) {\n            if (document.forms[1].elements[index].name.indexOf(name) == 0) {\n                return document.forms[1].elements[index];\n            } else {\n                index ++;\n            }\n        }\n    } \n\n    // stop hiding -->\n  </script>\n  </div>\n");
          int evalDoAfterBody = _jspx_th_rn_wapPage_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_wapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_wapPage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_2);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_1 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_2);
    _jspx_th_ctl_extraHtmlAttribute_1.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_2 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_3);
    _jspx_th_ctl_extraHtmlAttribute_2.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_2.setValue("3");
    int _jspx_eval_ctl_extraHtmlAttribute_2 = _jspx_th_ctl_extraHtmlAttribute_2.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_2);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_3 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_4);
    _jspx_th_ctl_extraHtmlAttribute_3.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_3.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_3 = _jspx_th_ctl_extraHtmlAttribute_3.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_3);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_4 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_4.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_4);
    _jspx_th_ctl_extraHtmlAttribute_4.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_4.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_4 = _jspx_th_ctl_extraHtmlAttribute_4.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_4);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_5 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_5);
    _jspx_th_ctl_extraHtmlAttribute_5.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_5.setValue("3");
    int _jspx_eval_ctl_extraHtmlAttribute_5 = _jspx_th_ctl_extraHtmlAttribute_5.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_5);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_6 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_6);
    _jspx_th_ctl_extraHtmlAttribute_6.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_6.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_6 = _jspx_th_ctl_extraHtmlAttribute_6.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_6);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_7 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_7.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_6);
    _jspx_th_ctl_extraHtmlAttribute_7.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_7.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_7 = _jspx_th_ctl_extraHtmlAttribute_7.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_7);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_8 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_8.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_7);
    _jspx_th_ctl_extraHtmlAttribute_8.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_8.setValue("3");
    int _jspx_eval_ctl_extraHtmlAttribute_8 = _jspx_th_ctl_extraHtmlAttribute_8.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_8);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_9(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_9 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_9.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_0);
    _jspx_th_ctl_extraHtmlAttribute_9.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_9.setValue("5");
    int _jspx_eval_ctl_extraHtmlAttribute_9 = _jspx_th_ctl_extraHtmlAttribute_9.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_9);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_10(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_10 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_10.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_1);
    _jspx_th_ctl_extraHtmlAttribute_10.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_10.setValue("5");
    int _jspx_eval_ctl_extraHtmlAttribute_10 = _jspx_th_ctl_extraHtmlAttribute_10.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_10);
    return false;
  }

  private boolean _jspx_meth_ctl_button_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_2 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_editable_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_4);
    _jspx_th_ctl_button_2.setEditable(false);
    _jspx_th_ctl_button_2.setLabel("Load crystal data from CIF");
    int _jspx_eval_ctl_button_2 = _jspx_th_ctl_button_2.doStartTag();
    if (_jspx_th_ctl_button_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_label_editable_nobody.reuse(_jspx_th_ctl_button_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_22(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_22 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_22.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_22 = _jspx_th_rn_sampleFieldLabel_22.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_22);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_22(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_22 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_22.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_22 = _jspx_th_rn_sampleField_22.doStartTag();
    if (_jspx_th_rn_sampleField_22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_22);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_6 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_6 = _jspx_th_rn_sampleFieldUnits_6.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_6);
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
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
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
}
