package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.ErrorChecker;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.CifFileContext;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;

public final class collectpreliminarydata_jsp extends org.apache.jasper.runtime.HttpJspBase
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
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_fieldCode;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_label_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_suppressInsteadOfSkip;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_suppressInsteadOfSkip;

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
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_fieldCode = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_label_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_suppressInsteadOfSkip = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_suppressInsteadOfSkip = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_filenames.release();
    _jspx_tagPool_rn_cifChooser_id_nobody.release();
    _jspx_tagPool_rn_file_fileName.release();
    _jspx_tagPool_rn_cifFile_id_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_rn_cifSampleFilter_fixShelxCifs_enabled_cif.release();
    _jspx_tagPool_rn_sampleField_id_fieldCode.release();
    _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.release();
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_button_label_editable_nobody.release();
    _jspx_tagPool_rn_ltaIterator.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_wapComments.release();
    _jspx_tagPool_rn_wapSaveButton_suppressInsteadOfSkip.release();
    _jspx_tagPool_rn_wapCancelButton_suppressInsteadOfSkip.release();
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
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Collect Preliminary Data");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode( SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED );
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(
      SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED_CORRECTED );
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
          out.write("\n  <div class=\"pageBody\">\n  <p class=\"pageInstructions\">\n    Enter the preliminary crystal data on the form below and click\n    the \"Save\" button to record it.\n    ");
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
              out.write("<br/>\n      <span class=\"errorMessage\"\n            style=\"font-weight: normal; font-style: italic;\">\n        You must address the flagged validation errors before the data\n        will be accepted.\n      </span> \n    ");
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
              out.write("\n    <table class=\"bodyTable\">\n      <tr>\n        <th class=\"leadSectionHead\" colspan=\"4\">General Information</th>\n      </tr>\n      <tr>\n        <th>Laboratory:</th>\n        <td colspan=\"3\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_0.setFieldCode( LabField.NAME );
              _jspx_th_rn_labField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
              if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_labField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th>\n          ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write(":\n        </th>\n        <td colspan=\"3\">\n          ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_0.setFieldCode( SampleInfo.LOCAL_LAB_ID );
              _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"multiboxLabel\">\n          ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleTextBL.CRYSTALLOGRAPHER_NAME);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write(":\n        </th>\n        <td colspan=\"3\">\n          ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_1.setFieldCode( SampleTextBL.CRYSTALLOGRAPHER_NAME );
              int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
              if (_jspx_eval_rn_sampleField_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleField_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleField_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleField_1.doInitBody();
                }
                do {
                  out.write("  \n            ");
                  if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_sampleField_1, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleField_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleField_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode.reuse(_jspx_th_rn_sampleField_1);
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
                  out.write("\n        <tr>\n          <th colspan=\"4\" class=\"sectionHead\"\n              style=\"padding-top: 0.0em;\">Crystal Data</th>\n        </tr>\n        <tr>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_2.setFieldCode(SampleDataInfo.A_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
                  out.write(":\n          </th>\n          <td class=\"minimumWidth\"> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_rn_sampleField_2, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_2(_jspx_th_rn_sampleField_2, _jspx_page_context))
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
                  out.write("\n          </td>\n          <th class=\"minimumWidth\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_3.setFieldCode(SampleDataInfo.ALPHA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
                  out.write(":\n          </th>\n          <td> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_3(_jspx_th_rn_sampleField_3, _jspx_page_context))
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
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_4 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_4.setFieldCode(SampleDataInfo.B_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_4 = _jspx_th_rn_sampleFieldLabel_4.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_4);
                  out.write(":\n          </th>\n          <td class=\"minimumWidth\"> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_4(_jspx_th_rn_sampleField_4, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_5(_jspx_th_rn_sampleField_4, _jspx_page_context))
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
                  out.write("\n          </td>\n          <th class=\"minimumWidth\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_5 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_5.setFieldCode(SampleDataInfo.BETA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_5 = _jspx_th_rn_sampleFieldLabel_5.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_5);
                  out.write(":\n          </th>\n          <td> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_6(_jspx_th_rn_sampleField_5, _jspx_page_context))
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
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_6 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_6.setFieldCode(SampleDataInfo.C_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_6 = _jspx_th_rn_sampleFieldLabel_6.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_6);
                  out.write(":\n          </th>\n          <td class=\"minimumWidth\"> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_7(_jspx_th_rn_sampleField_6, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_8(_jspx_th_rn_sampleField_6, _jspx_page_context))
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
                  out.write("\n          </td>\n          <th class=\"minimumWidth\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_7 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_7.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_7.setFieldCode(SampleDataInfo.GAMMA_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_7 = _jspx_th_rn_sampleFieldLabel_7.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_7);
                  out.write(":\n          </th>\n          <td> \n            ");
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
                      if (_jspx_meth_ctl_extraHtmlAttribute_9(_jspx_th_rn_sampleField_7, _jspx_page_context))
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
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_8 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_8.setFieldCode(SampleDataInfo.T_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_8 = _jspx_th_rn_sampleFieldLabel_8.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_8);
                  out.write(":\n          </th>\n          <td class=\"minimumWidth\"> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_8 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_8.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_8.setFieldCode( SampleDataInfo.T_FIELD );
                  int _jspx_eval_rn_sampleField_8 = _jspx_th_rn_sampleField_8.doStartTag();
                  if (_jspx_eval_rn_sampleField_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_8.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_10(_jspx_th_rn_sampleField_8, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_8.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode.reuse(_jspx_th_rn_sampleField_8);
                  //  rn:sampleFieldUnits
                  org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_6 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
                  _jspx_th_rn_sampleFieldUnits_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldUnits_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldUnits_6.setFieldCode(SampleDataInfo.T_FIELD);
                  int _jspx_eval_rn_sampleFieldUnits_6 = _jspx_th_rn_sampleFieldUnits_6.doStartTag();
                  if (_jspx_th_rn_sampleFieldUnits_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldUnits_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldUnits_6);
                  out.write("\n          </td>\n          <th class=\"minimumWidth\">\n            ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_9 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleFieldLabel_9.setFieldCode(SampleDataInfo.COLOR_FIELD);
                  int _jspx_eval_rn_sampleFieldLabel_9 = _jspx_th_rn_sampleFieldLabel_9.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_9);
                  out.write(":\n          </th>\n          <td> \n            ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_9 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_9.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_rn_sampleField_9.setFieldCode( SampleDataInfo.COLOR_FIELD );
                  int _jspx_eval_rn_sampleField_9 = _jspx_th_rn_sampleField_9.doStartTag();
                  if (_jspx_eval_rn_sampleField_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleField_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleField_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleField_9.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_11(_jspx_th_rn_sampleField_9, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_rn_sampleField_9.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleField_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleField_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode.reuse(_jspx_th_rn_sampleField_9);
                  out.write("\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"4\" class=\"actionButton\">\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorChecker_0.setInvert(true);
                  int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:button
                      org.recipnet.common.controls.ButtonHtmlControl loadFromCif = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
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
                          if (_jspx_meth_ctl_extraHtmlAttribute_12(_jspx_th_ctl_button_0, _jspx_page_context))
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
                  out.write("\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  _jspx_th_ctl_errorChecker_1.setErrorFilter(CifFileContext.CIF_HAS_ERRORS );
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
                      org.recipnet.common.controls.ButtonHtmlControl loadFromBuggyCif = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_1 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label_id.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_1);
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
                          if (_jspx_meth_ctl_extraHtmlAttribute_13(_jspx_th_ctl_button_1, _jspx_page_context))
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
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_1);
                  out.write("\n            ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_cifSampleFilter_0);
                  _jspx_th_ctl_errorChecker_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                  int _jspx_eval_ctl_errorChecker_2 = _jspx_th_ctl_errorChecker_2.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_2.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_3.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
                      _jspx_th_ctl_errorChecker_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_3.setInvert(true);
                      _jspx_th_ctl_errorChecker_3.setErrorFilter(CifFileContext.CIF_HAS_ERRORS );
                      int _jspx_eval_ctl_errorChecker_3 = _jspx_th_ctl_errorChecker_3.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_3.doInitBody();
                        }
                        do {
                          out.write("\n                ");
                          if (_jspx_meth_ctl_button_2(_jspx_th_ctl_errorChecker_3, _jspx_page_context))
                            return;
                          out.write("<br/>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_3.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_invert_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_3);
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_4.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
                      _jspx_th_ctl_errorChecker_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_4.setErrorFilter(CifFileContext.UNPARSEABLE_CIF );
                      int _jspx_eval_ctl_errorChecker_4 = _jspx_th_ctl_errorChecker_4.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_4.doInitBody();
                        }
                        do {
                          out.write("\n                <span class=\"errorNotice\">Warning: CIF '");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          out.write("'\n                    could not be parsed</span>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorChecker_4.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorChecker_4);
                      out.write("\n              ");
                      //  ctl:errorChecker
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorChecker_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_2);
                      _jspx_th_ctl_errorChecker_5.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cif}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorChecker_5.setErrorFilter(CifFileContext.EMPTY_CIF );
                      int _jspx_eval_ctl_errorChecker_5 = _jspx_th_ctl_errorChecker_5.doStartTag();
                      if (_jspx_eval_ctl_errorChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorChecker_5.doInitBody();
                        }
                        do {
                          out.write("\n                <span class=\"errorNotice\">Warning: CIF '");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cifChooser.cifName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                          out.write("'\n                    contains no data blocks</span>\n              ");
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
                      out.write("\n            ");
                      int evalDoAfterBody = _jspx_th_ctl_errorChecker_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorChecker_errorSupplier.reuse(_jspx_th_ctl_errorChecker_2);
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
              out.write("\n      <tr><th class=\"sectionHead\" colspan=\"4\">Additional Information</th></tr>\n      <tr>\n        <th>\n          ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_10 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_10.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_10.setFieldCode(SampleTextBL.RAW_DATA_URL);
              int _jspx_eval_rn_sampleFieldLabel_10 = _jspx_th_rn_sampleFieldLabel_10.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_10);
              out.write(":\n        </th>\n        <td colspan=\"3\"> \n          ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_10 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_10.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_10.setFieldCode( SampleTextBL.RAW_DATA_URL );
              int _jspx_eval_rn_sampleField_10 = _jspx_th_rn_sampleField_10.doStartTag();
              if (_jspx_eval_rn_sampleField_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleField_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleField_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleField_10.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_ctl_extraHtmlAttribute_14(_jspx_th_rn_sampleField_10, _jspx_page_context))
                    return;
                  out.write("\n          ");
                  int evalDoAfterBody = _jspx_th_rn_sampleField_10.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleField_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleField_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode.reuse(_jspx_th_rn_sampleField_10);
              out.write("\n        </td>\n      </tr>\n      ");
              org.recipnet.site.wrapper.StringBean ltaClass = null;
              synchronized (_jspx_page_context) {
                ltaClass = (org.recipnet.site.wrapper.StringBean) _jspx_page_context.getAttribute("ltaClass", PageContext.PAGE_SCOPE);
                if (ltaClass == null){
                  ltaClass = new org.recipnet.site.wrapper.StringBean();
                  _jspx_page_context.setAttribute("ltaClass", ltaClass, PageContext.PAGE_SCOPE);
                }
              }
              out.write("\n      ");
              org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("ltaClass"), "string", "class='subsectionHead'", null, null, false);
              out.write("\n      ");
              if (_jspx_meth_rn_ltaIterator_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n      <tr>\n        <th class=\"sectionHead\" colspan=\"4\">Data Collection Comments</th>\n      </tr>\n      <tr>\n        <td colspan=\"4\" style=\"text-align: center;\">\n          ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"4\" class=\"formButtons\">\n          ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          ");
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

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_1);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("1");
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
    _jspx_th_ctl_extraHtmlAttribute_1.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_2 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_2);
    _jspx_th_ctl_extraHtmlAttribute_2.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_2.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_2 = _jspx_th_ctl_extraHtmlAttribute_2.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_2);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_3 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_3);
    _jspx_th_ctl_extraHtmlAttribute_3.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_3.setValue("3");
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
    _jspx_th_ctl_extraHtmlAttribute_4.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_4.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_4 = _jspx_th_ctl_extraHtmlAttribute_4.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_4);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_5 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_5.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_4);
    _jspx_th_ctl_extraHtmlAttribute_5.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_5.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_5 = _jspx_th_ctl_extraHtmlAttribute_5.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_5);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_6 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_6.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_5);
    _jspx_th_ctl_extraHtmlAttribute_6.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_6.setValue("3");
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
    _jspx_th_ctl_extraHtmlAttribute_7.setName("onChange");
    _jspx_th_ctl_extraHtmlAttribute_7.setValue("setVals()");
    int _jspx_eval_ctl_extraHtmlAttribute_7 = _jspx_th_ctl_extraHtmlAttribute_7.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_7);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_8(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_8 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_8.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_6);
    _jspx_th_ctl_extraHtmlAttribute_8.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_8.setValue("2");
    int _jspx_eval_ctl_extraHtmlAttribute_8 = _jspx_th_ctl_extraHtmlAttribute_8.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_8);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_9(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_9 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_9.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_7);
    _jspx_th_ctl_extraHtmlAttribute_9.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_9.setValue("3");
    int _jspx_eval_ctl_extraHtmlAttribute_9 = _jspx_th_ctl_extraHtmlAttribute_9.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_9);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_10(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_10 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_10.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_8);
    _jspx_th_ctl_extraHtmlAttribute_10.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_10.setValue("4");
    int _jspx_eval_ctl_extraHtmlAttribute_10 = _jspx_th_ctl_extraHtmlAttribute_10.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_10);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_11 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_11.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_9);
    _jspx_th_ctl_extraHtmlAttribute_11.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_11.setValue("4");
    int _jspx_eval_ctl_extraHtmlAttribute_11 = _jspx_th_ctl_extraHtmlAttribute_11.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_11);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_12(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_12 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_12.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_0);
    _jspx_th_ctl_extraHtmlAttribute_12.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_12.setValue("5");
    int _jspx_eval_ctl_extraHtmlAttribute_12 = _jspx_th_ctl_extraHtmlAttribute_12.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_12);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_13(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_13 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_13.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_1);
    _jspx_th_ctl_extraHtmlAttribute_13.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_13.setValue("5");
    int _jspx_eval_ctl_extraHtmlAttribute_13 = _jspx_th_ctl_extraHtmlAttribute_13.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_13);
    return false;
  }

  private boolean _jspx_meth_ctl_button_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_errorChecker_3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_2 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_editable_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_3);
    _jspx_th_ctl_button_2.setEditable(false);
    _jspx_th_ctl_button_2.setLabel("Load crystal data from CIF");
    int _jspx_eval_ctl_button_2 = _jspx_th_ctl_button_2.doStartTag();
    if (_jspx_th_ctl_button_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_label_editable_nobody.reuse(_jspx_th_ctl_button_2);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_14(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_10, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_14 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_14.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_10);
    _jspx_th_ctl_extraHtmlAttribute_14.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_14.setValue("50");
    int _jspx_eval_ctl_extraHtmlAttribute_14 = _jspx_th_ctl_extraHtmlAttribute_14.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_14);
    return false;
  }

  private boolean _jspx_meth_rn_ltaIterator_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    HttpServletRequest request = (HttpServletRequest)_jspx_page_context.getRequest();
    //  rn:ltaIterator
    org.recipnet.site.content.rncontrols.LtaIterator _jspx_th_rn_ltaIterator_0 = (org.recipnet.site.content.rncontrols.LtaIterator) _jspx_tagPool_rn_ltaIterator.get(org.recipnet.site.content.rncontrols.LtaIterator.class);
    _jspx_th_rn_ltaIterator_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_ltaIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_ltaIterator_0 = _jspx_th_rn_ltaIterator_0.doStartTag();
    if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_ltaIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_ltaIterator_0.doInitBody();
      }
      do {
        out.write("\n        <tr>\n          <th ");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltaClass.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        out.write(">\n            ");
        if (_jspx_meth_rn_sampleFieldLabel_11(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write(":\n          </th>\n          <td colspan=\"3\" ");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltaClass.string}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        out.write(">\n            ");
        if (_jspx_meth_rn_sampleField_11(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        if (_jspx_meth_rn_sampleFieldUnits_7(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write("\n          </td>\n        </tr> \n        ");
        org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("ltaClass"), "string", "", null, null, false);
        out.write("\n      ");
        int evalDoAfterBody = _jspx_th_rn_ltaIterator_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_ltaIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_ltaIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_ltaIterator.reuse(_jspx_th_rn_ltaIterator_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_11 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_11.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_11 = _jspx_th_rn_sampleFieldLabel_11.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_11);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_11(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_11 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_11.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_11 = _jspx_th_rn_sampleField_11.doStartTag();
    if (_jspx_eval_rn_sampleField_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleField_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleField_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleField_11.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_extraHtmlAttribute_15(_jspx_th_rn_sampleField_11, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_sampleField_11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleField_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleField_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField.reuse(_jspx_th_rn_sampleField_11);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_15(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleField_11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_15 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_15.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleField_11);
    _jspx_th_ctl_extraHtmlAttribute_15.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_15.setValue("100");
    int _jspx_eval_ctl_extraHtmlAttribute_15 = _jspx_th_ctl_extraHtmlAttribute_15.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_15);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_7 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_7.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_7 = _jspx_th_rn_sampleFieldUnits_7.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_7);
    return false;
  }

  private boolean _jspx_meth_rn_wapComments_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapComments
    org.recipnet.site.content.rncontrols.WapComments _jspx_th_rn_wapComments_0 = (org.recipnet.site.content.rncontrols.WapComments) _jspx_tagPool_rn_wapComments.get(org.recipnet.site.content.rncontrols.WapComments.class);
    _jspx_th_rn_wapComments_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapComments_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_wapComments_0 = _jspx_th_rn_wapComments_0.doStartTag();
    if (_jspx_eval_rn_wapComments_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_wapComments_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_wapComments_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_wapComments_0.doInitBody();
      }
      do {
        if (_jspx_meth_ctl_extraHtmlAttribute_16(_jspx_th_rn_wapComments_0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_rn_wapComments_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_wapComments_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_wapComments_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapComments.reuse(_jspx_th_rn_wapComments_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_16(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapComments_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_16 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_16.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapComments_0);
    _jspx_th_ctl_extraHtmlAttribute_16.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_16.setValue("200");
    int _jspx_eval_ctl_extraHtmlAttribute_16 = _jspx_th_ctl_extraHtmlAttribute_16.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_16);
    return false;
  }

  private boolean _jspx_meth_rn_wapSaveButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapSaveButton
    org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_suppressInsteadOfSkip.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
    _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_wapSaveButton_0.setSuppressInsteadOfSkip(true);
    int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
    if (_jspx_eval_rn_wapSaveButton_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_wapSaveButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_wapSaveButton_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_wapSaveButton_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_ctl_extraHtmlAttribute_17(_jspx_th_rn_wapSaveButton_0, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_rn_wapSaveButton_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_wapSaveButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapSaveButton_suppressInsteadOfSkip.reuse(_jspx_th_rn_wapSaveButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_17(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapSaveButton_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_17 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_17.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapSaveButton_0);
    _jspx_th_ctl_extraHtmlAttribute_17.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_17.setValue("32767");
    int _jspx_eval_ctl_extraHtmlAttribute_17 = _jspx_th_ctl_extraHtmlAttribute_17.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_17);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_0 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_suppressInsteadOfSkip.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_wapCancelButton_0.setSuppressInsteadOfSkip(true);
    int _jspx_eval_rn_wapCancelButton_0 = _jspx_th_rn_wapCancelButton_0.doStartTag();
    if (_jspx_eval_rn_wapCancelButton_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_wapCancelButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_wapCancelButton_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_wapCancelButton_0.doInitBody();
      }
      do {
        out.write("\n            ");
        if (_jspx_meth_ctl_extraHtmlAttribute_18(_jspx_th_rn_wapCancelButton_0, _jspx_page_context))
          return true;
        out.write("\n          ");
        int evalDoAfterBody = _jspx_th_rn_wapCancelButton_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_wapCancelButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_wapCancelButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_suppressInsteadOfSkip.reuse(_jspx_th_rn_wapCancelButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_18(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapCancelButton_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_18 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_18.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapCancelButton_0);
    _jspx_th_ctl_extraHtmlAttribute_18.setName("tabindex");
    _jspx_th_ctl_extraHtmlAttribute_18.setValue("32767");
    int _jspx_eval_ctl_extraHtmlAttribute_18 = _jspx_th_ctl_extraHtmlAttribute_18.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_18);
    return false;
  }
}
