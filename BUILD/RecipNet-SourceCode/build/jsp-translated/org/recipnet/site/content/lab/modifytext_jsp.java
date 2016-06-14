package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.ValidationSupressor;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.ValidationOverrideButton;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleTextInfo;

public final class modifytext_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_extendedOpWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_suppressValidation_id_enabled;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleUpdateBlocker_enabled;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_iterator_iterations_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_textTypeSelector_includeFieldsForAction_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_undecidedTextContext_textTypeSelector;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_required_id_height_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnOnlyIteration;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_deleteIterationButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_addIterationButton_iterator_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAttributes_restrictByWorkflowAction_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAnnotations_restrictByWorkflowAction_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_overrideSpecificValidationUnlessParamNameMatches_id_height_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_extendedOpWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_suppressValidation_id_enabled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleUpdateBlocker_enabled = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_iterator_iterations_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_textTypeSelector_includeFieldsForAction_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_undecidedTextContext_textTypeSelector = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_required_id_height_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnOnlyIteration = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_deleteIterationButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_addIterationButton_iterator_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAttributes_restrictByWorkflowAction_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAnnotations_restrictByWorkflowAction_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_overrideSpecificValidationUnlessParamNameMatches_id_height_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_extendedOpWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_suppressValidation_id_enabled.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_sampleUpdateBlocker_enabled.release();
    _jspx_tagPool_ctl_iterator_iterations_id.release();
    _jspx_tagPool_rn_textTypeSelector_includeFieldsForAction_id_nobody.release();
    _jspx_tagPool_rn_undecidedTextContext_textTypeSelector.release();
    _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_required_id_height_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnOnlyIteration.release();
    _jspx_tagPool_ctl_deleteIterationButton_nobody.release();
    _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_addIterationButton_iterator_nobody.release();
    _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAttributes_restrictByWorkflowAction_id.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_id_nobody.release();
    _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier.release();
    _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAnnotations_restrictByWorkflowAction_id.release();
    _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_overrideSpecificValidationUnlessParamNameMatches_id_height_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_id_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n");
      //  rn:extendedOpWapPage
      org.recipnet.site.content.rncontrols.ExtendedOperationWapPage _jspx_th_rn_extendedOpWapPage_0 = (org.recipnet.site.content.rncontrols.ExtendedOperationWapPage) _jspx_tagPool_rn_extendedOpWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.ExtendedOperationWapPage.class);
      _jspx_th_rn_extendedOpWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_extendedOpWapPage_0.setParent(null);
      _jspx_th_rn_extendedOpWapPage_0.setTitle("Modify Text Fields");
      _jspx_th_rn_extendedOpWapPage_0.setWorkflowActionCode( SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
      _jspx_th_rn_extendedOpWapPage_0.setWorkflowActionCodeCorrected(
          SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
      _jspx_th_rn_extendedOpWapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_extendedOpWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_extendedOpWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_extendedOpWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_extendedOpWapPage_0 = _jspx_th_rn_extendedOpWapPage_0.doStartTag();
      if (_jspx_eval_rn_extendedOpWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.ExtendedOperationWapPage wapPage = null;
        if (_jspx_eval_rn_extendedOpWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_extendedOpWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_extendedOpWapPage_0.doInitBody();
        }
        wapPage = (org.recipnet.site.content.rncontrols.ExtendedOperationWapPage) _jspx_page_context.findAttribute("wapPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  ctl:suppressValidation
          org.recipnet.common.controls.ValidationSupressor validation = null;
          org.recipnet.common.controls.ValidationSupressor _jspx_th_ctl_suppressValidation_0 = (org.recipnet.common.controls.ValidationSupressor) _jspx_tagPool_ctl_suppressValidation_id_enabled.get(org.recipnet.common.controls.ValidationSupressor.class);
          _jspx_th_ctl_suppressValidation_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_suppressValidation_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_extendedOpWapPage_0);
          _jspx_th_ctl_suppressValidation_0.setId("validation");
          _jspx_th_ctl_suppressValidation_0.setEnabled(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ !save.valueAsBoolean }", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
          int _jspx_eval_ctl_suppressValidation_0 = _jspx_th_ctl_suppressValidation_0.doStartTag();
          if (_jspx_eval_ctl_suppressValidation_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_suppressValidation_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_suppressValidation_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_suppressValidation_0.doInitBody();
            }
            validation = (org.recipnet.common.controls.ValidationSupressor) _jspx_page_context.findAttribute("validation");
            do {
              out.write("\n  <div class=\"pageBody\">\n  <p class=\"pageInstructions\">\n    Add, delete, or modify metadata fields then click 'Save' to\n    save your changes. Fields may be deleted using their 'delete' button\n    or by erasing all their text.\n  </p><p class=\"pageInstructions\" style=\"font-style: italic;\">\n    No changes applied via this page are permanent until you click the 'Save'\n    button.\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_suppressValidation_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter( ValidationSupressor.VALIDATION_ERROR_REPORTED );
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("<br/>\n      <span class=\"errorMessage\"\n            style=\"font-weight: normal; font-style: italic;\">\n        You must address the flagged validation errors before the changes will\n        be recorded.\n      </span>\n    ");
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
              out.write("\n  </p>\n  ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_suppressValidation_0);
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                do {
                  out.write("\n    <table class=\"bodyTable\" cellspacing=\"0\">\n      <tr>\n        <th colspan=\"3\" class=\"tableTitle\">Available text modifications</th>\n      </tr>\n      <tr>\n        <th colspan=\"3\" class=\"columnLabel\">Add a new field:</th>\n      </tr>\n      ");
                  //  rn:sampleUpdateBlocker
                  org.recipnet.site.content.rncontrols.SampleUpdateBlocker _jspx_th_rn_sampleUpdateBlocker_0 = (org.recipnet.site.content.rncontrols.SampleUpdateBlocker) _jspx_tagPool_rn_sampleUpdateBlocker_enabled.get(org.recipnet.site.content.rncontrols.SampleUpdateBlocker.class);
                  _jspx_th_rn_sampleUpdateBlocker_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleUpdateBlocker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_sampleUpdateBlocker_0.setEnabled(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ !save.valueAsBoolean }", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                  int _jspx_eval_rn_sampleUpdateBlocker_0 = _jspx_th_rn_sampleUpdateBlocker_0.doStartTag();
                  if (_jspx_eval_rn_sampleUpdateBlocker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleUpdateBlocker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleUpdateBlocker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleUpdateBlocker_0.doInitBody();
                    }
                    do {
                      out.write("\n      ");
                      //  ctl:iterator
                      org.recipnet.common.controls.SimpleIterator newFieldIterator = null;
                      org.recipnet.common.controls.SimpleIterator _jspx_th_ctl_iterator_0 = (org.recipnet.common.controls.SimpleIterator) _jspx_tagPool_ctl_iterator_iterations_id.get(org.recipnet.common.controls.SimpleIterator.class);
                      _jspx_th_ctl_iterator_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_iterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleUpdateBlocker_0);
                      _jspx_th_ctl_iterator_0.setId("newFieldIterator");
                      _jspx_th_ctl_iterator_0.setIterations(1);
                      int _jspx_eval_ctl_iterator_0 = _jspx_th_ctl_iterator_0.doStartTag();
                      if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_iterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_iterator_0.doInitBody();
                        }
                        newFieldIterator = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("newFieldIterator");
                        do {
                          out.write("\n      <tr class=\"");
                          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(\n                      newFieldIterator.iterationCountSinceThisPhaseBegan,\n                      'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                          out.write("\">\n        <td>\n          ");
                          //  rn:textTypeSelector
                          org.recipnet.site.content.rncontrols.TextTypeSelector sel = null;
                          org.recipnet.site.content.rncontrols.TextTypeSelector _jspx_th_rn_textTypeSelector_0 = (org.recipnet.site.content.rncontrols.TextTypeSelector) _jspx_tagPool_rn_textTypeSelector_includeFieldsForAction_id_nobody.get(org.recipnet.site.content.rncontrols.TextTypeSelector.class);
                          _jspx_th_rn_textTypeSelector_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_textTypeSelector_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                          _jspx_th_rn_textTypeSelector_0.setId("sel");
                          _jspx_th_rn_textTypeSelector_0.setIncludeFieldsForAction(
                  SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
                          int _jspx_eval_rn_textTypeSelector_0 = _jspx_th_rn_textTypeSelector_0.doStartTag();
                          if (_jspx_th_rn_textTypeSelector_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          sel = (org.recipnet.site.content.rncontrols.TextTypeSelector) _jspx_page_context.findAttribute("sel");
                          _jspx_tagPool_rn_textTypeSelector_includeFieldsForAction_id_nobody.reuse(_jspx_th_rn_textTypeSelector_0);
                          out.write("\n        </td>\n        ");
                          //  rn:undecidedTextContext
                          org.recipnet.site.content.rncontrols.UndecidedTextContext _jspx_th_rn_undecidedTextContext_0 = (org.recipnet.site.content.rncontrols.UndecidedTextContext) _jspx_tagPool_rn_undecidedTextContext_textTypeSelector.get(org.recipnet.site.content.rncontrols.UndecidedTextContext.class);
                          _jspx_th_rn_undecidedTextContext_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_undecidedTextContext_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                          _jspx_th_rn_undecidedTextContext_0.setTextTypeSelector((org.recipnet.site.content.rncontrols.TextTypeSelector) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sel}", org.recipnet.site.content.rncontrols.TextTypeSelector.class, (PageContext)_jspx_page_context, null, false));
                          int _jspx_eval_rn_undecidedTextContext_0 = _jspx_th_rn_undecidedTextContext_0.doStartTag();
                          if (_jspx_eval_rn_undecidedTextContext_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_rn_undecidedTextContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_rn_undecidedTextContext_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_rn_undecidedTextContext_0.doInitBody();
                            }
                            do {
                              out.write("\n          <td>\n            ");
                              //  rn:sampleField
                              org.recipnet.site.content.rncontrols.SampleField newText = null;
                              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_required_id_height_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
                              _jspx_th_rn_sampleField_0.setId("newText");
                              _jspx_th_rn_sampleField_0.setRequired(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sel.textType != SampleTextInfo.INVALID_TYPE}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                              _jspx_th_rn_sampleField_0.setUseTextareaInsteadOfTextboxForUnknownCode(true);
                              _jspx_th_rn_sampleField_0.setWidth(64);
                              _jspx_th_rn_sampleField_0.setHeight(2);
                              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
                              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              newText = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("newText");
                              _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_required_id_height_nobody.reuse(_jspx_th_rn_sampleField_0);
                              out.write("\n            ");
                              if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_undecidedTextContext_0, _jspx_page_context))
                              return;
                              out.write("\n            ");
                              //  rn:overrideValidationButton
                              org.recipnet.site.content.rncontrols.ValidationOverrideButton saveOverrideNew = null;
                              org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_0 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                              _jspx_th_rn_overrideValidationButton_0.setPageContext(_jspx_page_context);
                              _jspx_th_rn_overrideValidationButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
                              _jspx_th_rn_overrideValidationButton_0.setSampleField((org.recipnet.site.content.rncontrols.SampleField) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newText}", org.recipnet.site.content.rncontrols.SampleField.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_rn_overrideValidationButton_0.setLabel("override");
                              _jspx_th_rn_overrideValidationButton_0.setId("saveOverrideNew");
                              _jspx_th_rn_overrideValidationButton_0.setSaveToPersistedOp(true);
                              int _jspx_eval_rn_overrideValidationButton_0 = _jspx_th_rn_overrideValidationButton_0.doStartTag();
                              if (_jspx_th_rn_overrideValidationButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              saveOverrideNew = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("saveOverrideNew");
                              _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_0);
                              out.write("\n            ");
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
                              _jspx_th_ctl_errorMessage_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${saveOverrideNew}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_ctl_errorMessage_1.setErrorFilter(
                    ValidationOverrideButton.VALIDATION_OVERRIDDEN);
                              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_1.doInitBody();
                              }
                              do {
                              out.write("\n              <span class=\"notice\">(validation overridden)</span>\n            ");
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
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
                              _jspx_th_ctl_errorMessage_2.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newText}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_ctl_errorMessage_2.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
                              int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_2.doInitBody();
                              }
                              do {
                              out.write("\n              <div class=\"errorNotice\">\n                The entered value is invalid.\n              </div>\n              <div class=\"notice\">\n                Correct the value and click \"add another\" to try to add it\n                again, click \"Save\" to attempt to save the corrected value in\n                place, or click \"override\" to override the validation and\n                save.\n              </div>\n            ");
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
                              out.write("\n          </td>\n          <td>\n            ");
                              if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_undecidedTextContext_0, _jspx_page_context))
                              return;
                              out.write("\n          </td>\n        ");
                              int evalDoAfterBody = _jspx_th_rn_undecidedTextContext_0.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_rn_undecidedTextContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_rn_undecidedTextContext_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_rn_undecidedTextContext_textTypeSelector.reuse(_jspx_th_rn_undecidedTextContext_0);
                          out.write("\n      </tr>\n      ");
                          //  ctl:errorChecker
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_iterator_0);
                          _jspx_th_ctl_errorChecker_0.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newText}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorChecker_0.setErrorFilter(
                    SampleField.REQUIRED_VALUE_IS_MISSING
                    | SampleField.VALUE_ENTERED_FOR_NULL_CONTEXT);
                          int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                          if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorChecker_0.doInitBody();
                            }
                            do {
                              out.write("\n        <tr class=\"");
                              out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(\n                        newFieldIterator.iterationCountSinceThisPhaseBegan,\n                        'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                              out.write("\">\n          <td colspan=\"3\" style=\"text-align: center;\">\n            ");
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
                              _jspx_th_ctl_errorMessage_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newText}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_ctl_errorMessage_3.setErrorFilter(
                    SampleField.REQUIRED_VALUE_IS_MISSING);
                              int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_3.doInitBody();
                              }
                              do {
                              out.write("\n              <span class=\"errorMessage\">When you select a field type, you must\n                enter a value.</span>\n            ");
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
                              out.write("\n            ");
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorChecker_0);
                              _jspx_th_ctl_errorMessage_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newText}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_ctl_errorMessage_4.setErrorFilter(
                    SampleField.VALUE_ENTERED_FOR_NULL_CONTEXT);
                              int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_4.doInitBody();
                              }
                              do {
                              out.write("\n              <span class=\"errorMessage\">You must select a field type to record\n                a value.</span>\n            ");
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
                              out.write("\n          </td>\n        </tr>\n      ");
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
                          out.write("\n      ");
                          int evalDoAfterBody = _jspx_th_ctl_iterator_0.doAfterBody();
                          newFieldIterator = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("newFieldIterator");
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_iterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_iterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      newFieldIterator = (org.recipnet.common.controls.SimpleIterator) _jspx_page_context.findAttribute("newFieldIterator");
                      _jspx_tagPool_ctl_iterator_iterations_id.reuse(_jspx_th_ctl_iterator_0);
                      out.write("\n      ");
                      int evalDoAfterBody = _jspx_th_rn_sampleUpdateBlocker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleUpdateBlocker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleUpdateBlocker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleUpdateBlocker_enabled.reuse(_jspx_th_rn_sampleUpdateBlocker_0);
                  out.write("\n      <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(newFieldIterator.iterations,\n                                 'evenRow', 'oddRow')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                  out.write("\">\n        <td colspan=\"3\" style=\"text-align:center\">\n          ");
                  if (_jspx_meth_ctl_addIterationButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n        </td>\n      </tr>\n      <tr>\n        <th colspan=\"3\" class=\"columnLabel\">Modify attributes of this sample:</th>\n      </tr>\n      ");
                  //  rn:sampleTextIterator
                  org.recipnet.site.content.rncontrols.SampleTextIterator attributes = null;
                  org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_0 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAttributes_restrictByWorkflowAction_id.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
                  _jspx_th_rn_sampleTextIterator_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleTextIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_sampleTextIterator_0.setRestrictToAttributes(true);
                  _jspx_th_rn_sampleTextIterator_0.setId("attributes");
                  _jspx_th_rn_sampleTextIterator_0.setSortByTextTypeName(true);
                  _jspx_th_rn_sampleTextIterator_0.setRestrictByWorkflowAction(
             SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
                  int _jspx_eval_rn_sampleTextIterator_0 = _jspx_th_rn_sampleTextIterator_0.doStartTag();
                  if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleTextIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleTextIterator_0.doInitBody();
                    }
                    attributes = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("attributes");
                    do {
                      out.write("\n        <tr class=\"");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(attributes.iterationCountSinceThisPhaseBegan,\n                                   \"evenRow\", \"oddRow\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                      out.write("\">\n          <th>");
                      if (_jspx_meth_rn_sampleFieldLabel_0(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                        return;
                      out.write(":</th>\n          <td>\n            ");
                      //  rn:sampleField
                      org.recipnet.site.content.rncontrols.SampleField attrSampleField = null;
                      org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                      _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                      _jspx_th_rn_sampleField_1.setId("attrSampleField");
                      int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
                      if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      attrSampleField = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("attrSampleField");
                      _jspx_tagPool_rn_sampleField_id_nobody.reuse(_jspx_th_rn_sampleField_1);
                      out.write("\n            ");
                      if (_jspx_meth_rn_sampleFieldUnits_1(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      //  rn:overrideValidationButton
                      org.recipnet.site.content.rncontrols.ValidationOverrideButton saveOverrideAttr = null;
                      org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_1 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                      _jspx_th_rn_overrideValidationButton_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_overrideValidationButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                      _jspx_th_rn_overrideValidationButton_1.setSampleField(attrSampleField);
                      _jspx_th_rn_overrideValidationButton_1.setLabel("override");
                      _jspx_th_rn_overrideValidationButton_1.setId("saveOverrideAttr");
                      _jspx_th_rn_overrideValidationButton_1.setSaveToPersistedOp(true);
                      int _jspx_eval_rn_overrideValidationButton_1 = _jspx_th_rn_overrideValidationButton_1.doStartTag();
                      if (_jspx_th_rn_overrideValidationButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      saveOverrideAttr = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("saveOverrideAttr");
                      _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_1);
                      out.write("\n            ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                      _jspx_th_ctl_errorMessage_5.setErrorSupplier(saveOverrideAttr);
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
                          out.write("\n              <span class=\"notice\">(validation overridden)</span>\n            ");
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
                      out.write("\n            ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
                      _jspx_th_ctl_errorMessage_6.setErrorSupplier(attrSampleField);
                      _jspx_th_ctl_errorMessage_6.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
                      int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_6.doInitBody();
                        }
                        do {
                          out.write("\n              <div class=\"errorNotice\">\n                The entered value is invalid.\n              </div>\n              <div class=\"notice\">\n                Correct the value and click \"Save\" to resubmit, or click\n                \"override\" to record the invalid value.\n              </div>\n            ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_6.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_6);
                      out.write("\n          </td>\n          <td>\n            ");
                      if (_jspx_meth_rn_deleteSampleTextInfoButton_0(_jspx_th_rn_sampleTextIterator_0, _jspx_page_context))
                        return;
                      out.write("\n          </td>\n        </tr>\n      ");
                      int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_0.doAfterBody();
                      attributes = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("attributes");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleTextIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleTextIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  attributes = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("attributes");
                  _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAttributes_restrictByWorkflowAction_id.reuse(_jspx_th_rn_sampleTextIterator_0);
                  out.write("\n      ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_7.setErrorSupplier(attributes);
                  int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_7.doInitBody();
                    }
                    do {
                      out.write("\n        <tr class=\"evenRow\">\n          <td colspan=\"3\" class=\"notice\" style=\"text-align: center;\">\n            There are currently no attributes.\n          </td>\n        </tr>\n      ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier.reuse(_jspx_th_ctl_errorMessage_7);
                  out.write("\n      <tr>\n        <th colspan=\"3\" class=\"columnLabel\">Modify annotations of this sample:</th>\n      </tr>\n      ");
                  //  rn:sampleTextIterator
                  org.recipnet.site.content.rncontrols.SampleTextIterator annotations = null;
                  org.recipnet.site.content.rncontrols.SampleTextIterator _jspx_th_rn_sampleTextIterator_1 = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAnnotations_restrictByWorkflowAction_id.get(org.recipnet.site.content.rncontrols.SampleTextIterator.class);
                  _jspx_th_rn_sampleTextIterator_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleTextIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_sampleTextIterator_1.setRestrictToAnnotations(true);
                  _jspx_th_rn_sampleTextIterator_1.setId("annotations");
                  _jspx_th_rn_sampleTextIterator_1.setSortByTextTypeName(true);
                  _jspx_th_rn_sampleTextIterator_1.setRestrictByWorkflowAction(
              SampleWorkflowBL.MODIFIED_TEXT_FIELDS);
                  int _jspx_eval_rn_sampleTextIterator_1 = _jspx_th_rn_sampleTextIterator_1.doStartTag();
                  if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_sampleTextIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_sampleTextIterator_1.doInitBody();
                    }
                    annotations = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("annotations");
                    do {
                      out.write("\n        <tr class=\"");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${\n            rn:testParity(annotations.iterationCountSinceThisPhaseBegan,\n                          \"evenRow\", \"oddRow\")}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                      out.write("\">\n          <th>");
                      if (_jspx_meth_rn_sampleFieldLabel_1(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                        return;
                      out.write(":</th>\n          <td>\n            ");
                      //  rn:sampleField
                      org.recipnet.site.content.rncontrols.SampleField annoSampleField = null;
                      org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_overrideSpecificValidationUnlessParamNameMatches_id_height_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                      _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
                      _jspx_th_rn_sampleField_2.setId("annoSampleField");
                      _jspx_th_rn_sampleField_2.setOverrideSpecificValidationUnlessParamNameMatches(".*(save){1,}?.*(_persist){0}?.*");
                      _jspx_th_rn_sampleField_2.setUseTextareaInsteadOfTextboxForUnknownCode(true);
                      _jspx_th_rn_sampleField_2.setWidth(64);
                      _jspx_th_rn_sampleField_2.setHeight(2);
                      int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
                      if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      annoSampleField = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("annoSampleField");
                      _jspx_tagPool_rn_sampleField_width_useTextareaInsteadOfTextboxForUnknownCode_overrideSpecificValidationUnlessParamNameMatches_id_height_nobody.reuse(_jspx_th_rn_sampleField_2);
                      out.write("\n            ");
                      if (_jspx_meth_rn_sampleFieldUnits_2(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                        return;
                      out.write("\n            ");
                      //  rn:overrideValidationButton
                      org.recipnet.site.content.rncontrols.ValidationOverrideButton saveOverrideAnno = null;
                      org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_2 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
                      _jspx_th_rn_overrideValidationButton_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_overrideValidationButton_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
                      _jspx_th_rn_overrideValidationButton_2.setSampleField(annoSampleField);
                      _jspx_th_rn_overrideValidationButton_2.setLabel("override");
                      _jspx_th_rn_overrideValidationButton_2.setId("saveOverrideAnno");
                      _jspx_th_rn_overrideValidationButton_2.setSaveToPersistedOp(true);
                      int _jspx_eval_rn_overrideValidationButton_2 = _jspx_th_rn_overrideValidationButton_2.doStartTag();
                      if (_jspx_th_rn_overrideValidationButton_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      saveOverrideAnno = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("saveOverrideAnno");
                      _jspx_tagPool_rn_overrideValidationButton_saveToPersistedOp_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_2);
                      out.write("\n            ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_8 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_8.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
                      _jspx_th_ctl_errorMessage_8.setErrorSupplier(saveOverrideAnno);
                      _jspx_th_ctl_errorMessage_8.setErrorFilter(
                        ValidationOverrideButton.VALIDATION_OVERRIDDEN);
                      int _jspx_eval_ctl_errorMessage_8 = _jspx_th_ctl_errorMessage_8.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_8.doInitBody();
                        }
                        do {
                          out.write("\n              <span class=\"notice\">(validation overridden)</span>\n            ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_8.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_8);
                      out.write("\n            ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_9 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_9.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
                      _jspx_th_ctl_errorMessage_9.setErrorSupplier(annoSampleField);
                      _jspx_th_ctl_errorMessage_9.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
                      int _jspx_eval_ctl_errorMessage_9 = _jspx_th_ctl_errorMessage_9.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_9.doInitBody();
                        }
                        do {
                          out.write("\n              <div class=\"errorNotice\">\n                The entered value is invalid!\n              </div>\n              <div class=\"notice\">\n                Correct the value and click \"Save\" to resubmit, or click\n                \"override\" to record the invalid value.\n              </div>\n            ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_9.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_9);
                      out.write("\n          </td>\n          <td>\n            ");
                      if (_jspx_meth_rn_deleteSampleTextInfoButton_1(_jspx_th_rn_sampleTextIterator_1, _jspx_page_context))
                        return;
                      out.write("\n          </td>\n        </tr>\n      ");
                      int evalDoAfterBody = _jspx_th_rn_sampleTextIterator_1.doAfterBody();
                      annotations = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("annotations");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_sampleTextIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_sampleTextIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  annotations = (org.recipnet.site.content.rncontrols.SampleTextIterator) _jspx_page_context.findAttribute("annotations");
                  _jspx_tagPool_rn_sampleTextIterator_sortByTextTypeName_restrictToAnnotations_restrictByWorkflowAction_id.reuse(_jspx_th_rn_sampleTextIterator_1);
                  out.write("\n      ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_10 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_10.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_10.setErrorSupplier(annotations);
                  int _jspx_eval_ctl_errorMessage_10 = _jspx_th_ctl_errorMessage_10.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_10.doInitBody();
                    }
                    do {
                      out.write("\n        <tr class=\"evenRow\">\n          <td colspan=\"3\" class=\"notice\" style=\"text-align: center;\">\n            There are currently no annotations.\n          </td>\n        </tr>\n      ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_10.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier.reuse(_jspx_th_ctl_errorMessage_10);
                  out.write("\n    </table>\n    <div style=\"margin-top: 1em; margin-bottom: 1em; text-align: center;\">\n      ");
                  //  rn:wapSaveButton
                  org.recipnet.site.content.rncontrols.WapSaveButton save = null;
                  org.recipnet.site.content.rncontrols.WapSaveButton _jspx_th_rn_wapSaveButton_0 = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_tagPool_rn_wapSaveButton_id_nobody.get(org.recipnet.site.content.rncontrols.WapSaveButton.class);
                  _jspx_th_rn_wapSaveButton_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_wapSaveButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_wapSaveButton_0.setId("save");
                  int _jspx_eval_rn_wapSaveButton_0 = _jspx_th_rn_wapSaveButton_0.doStartTag();
                  if (_jspx_th_rn_wapSaveButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  save = (org.recipnet.site.content.rncontrols.WapSaveButton) _jspx_page_context.findAttribute("save");
                  _jspx_tagPool_rn_wapSaveButton_id_nobody.reuse(_jspx_th_rn_wapSaveButton_0);
                  out.write("\n      ");
                  if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                    return;
                  out.write("\n    </div>\n  ");
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
              out.write('\n');
              out.write(' ');
              out.write(' ');
              if (_jspx_meth_ctl_styleBlock_0(_jspx_th_ctl_suppressValidation_0, _jspx_page_context))
                return;
              out.write("\n  </div>\n  ");
              int evalDoAfterBody = _jspx_th_ctl_suppressValidation_0.doAfterBody();
              validation = (org.recipnet.common.controls.ValidationSupressor) _jspx_page_context.findAttribute("validation");
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_ctl_suppressValidation_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_ctl_suppressValidation_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          validation = (org.recipnet.common.controls.ValidationSupressor) _jspx_page_context.findAttribute("validation");
          _jspx_tagPool_ctl_suppressValidation_id_enabled.reuse(_jspx_th_ctl_suppressValidation_0);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_extendedOpWapPage_0.doAfterBody();
          wapPage = (org.recipnet.site.content.rncontrols.ExtendedOperationWapPage) _jspx_page_context.findAttribute("wapPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_extendedOpWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_extendedOpWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_extendedOpWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_extendedOpWapPage_0);
      out.write('\n');
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

  private boolean _jspx_meth_rn_sampleFieldUnits_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_undecidedTextContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
    int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_undecidedTextContext_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnOnlyIteration.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedTextContext_0);
    _jspx_th_ctl_parityChecker_0.setIncludeOnOnlyIteration(true);
    _jspx_th_ctl_parityChecker_0.setInvert(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_ctl_deleteIterationButton_0(_jspx_th_ctl_parityChecker_0, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnOnlyIteration.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_deleteIterationButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_parityChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:deleteIterationButton
    org.recipnet.common.controls.DeleteIterationButton _jspx_th_ctl_deleteIterationButton_0 = (org.recipnet.common.controls.DeleteIterationButton) _jspx_tagPool_ctl_deleteIterationButton_nobody.get(org.recipnet.common.controls.DeleteIterationButton.class);
    _jspx_th_ctl_deleteIterationButton_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_deleteIterationButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_parityChecker_0);
    int _jspx_eval_ctl_deleteIterationButton_0 = _jspx_th_ctl_deleteIterationButton_0.doStartTag();
    if (_jspx_th_ctl_deleteIterationButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_deleteIterationButton_nobody.reuse(_jspx_th_ctl_deleteIterationButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_addIterationButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:addIterationButton
    org.recipnet.common.controls.AddIterationButton _jspx_th_ctl_addIterationButton_0 = (org.recipnet.common.controls.AddIterationButton) _jspx_tagPool_ctl_addIterationButton_iterator_nobody.get(org.recipnet.common.controls.AddIterationButton.class);
    _jspx_th_ctl_addIterationButton_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_addIterationButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_ctl_addIterationButton_0.setIterator((org.recipnet.common.controls.SimpleIterator) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${newFieldIterator}", org.recipnet.common.controls.SimpleIterator.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_ctl_addIterationButton_0 = _jspx_th_ctl_addIterationButton_0.doStartTag();
    if (_jspx_th_ctl_addIterationButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_addIterationButton_iterator_nobody.reuse(_jspx_th_ctl_addIterationButton_0);
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

  private boolean _jspx_meth_rn_sampleFieldUnits_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_1 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_1 = _jspx_th_rn_sampleFieldUnits_1.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_1);
    return false;
  }

  private boolean _jspx_meth_rn_deleteSampleTextInfoButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:deleteSampleTextInfoButton
    org.recipnet.site.content.rncontrols.DeleteSampleTextButton _jspx_th_rn_deleteSampleTextInfoButton_0 = (org.recipnet.site.content.rncontrols.DeleteSampleTextButton) _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody.get(org.recipnet.site.content.rncontrols.DeleteSampleTextButton.class);
    _jspx_th_rn_deleteSampleTextInfoButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_deleteSampleTextInfoButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_0);
    _jspx_th_rn_deleteSampleTextInfoButton_0.setSaveToPersistedOp(true);
    _jspx_th_rn_deleteSampleTextInfoButton_0.setReevaluatePage(true);
    _jspx_th_rn_deleteSampleTextInfoButton_0.setLabel("Delete");
    _jspx_th_rn_deleteSampleTextInfoButton_0.setSuppressIfNotCorrectionMode(false);
    int _jspx_eval_rn_deleteSampleTextInfoButton_0 = _jspx_th_rn_deleteSampleTextInfoButton_0.doStartTag();
    if (_jspx_th_rn_deleteSampleTextInfoButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody.reuse(_jspx_th_rn_deleteSampleTextInfoButton_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_2 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    int _jspx_eval_rn_sampleFieldUnits_2 = _jspx_th_rn_sampleFieldUnits_2.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_2);
    return false;
  }

  private boolean _jspx_meth_rn_deleteSampleTextInfoButton_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleTextIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:deleteSampleTextInfoButton
    org.recipnet.site.content.rncontrols.DeleteSampleTextButton _jspx_th_rn_deleteSampleTextInfoButton_1 = (org.recipnet.site.content.rncontrols.DeleteSampleTextButton) _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody.get(org.recipnet.site.content.rncontrols.DeleteSampleTextButton.class);
    _jspx_th_rn_deleteSampleTextInfoButton_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_deleteSampleTextInfoButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleTextIterator_1);
    _jspx_th_rn_deleteSampleTextInfoButton_1.setSaveToPersistedOp(true);
    _jspx_th_rn_deleteSampleTextInfoButton_1.setReevaluatePage(true);
    _jspx_th_rn_deleteSampleTextInfoButton_1.setLabel("Delete");
    _jspx_th_rn_deleteSampleTextInfoButton_1.setSuppressIfNotCorrectionMode(false);
    int _jspx_eval_rn_deleteSampleTextInfoButton_1 = _jspx_th_rn_deleteSampleTextInfoButton_1.doStartTag();
    if (_jspx_th_rn_deleteSampleTextInfoButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_deleteSampleTextInfoButton_suppressIfNotCorrectionMode_saveToPersistedOp_reevaluatePage_label_nobody.reuse(_jspx_th_rn_deleteSampleTextInfoButton_1);
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_suppressValidation_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_suppressValidation_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    table.bodyTable { border: 3px solid #32357D; }\n    table.bodyTable th,\n    table.bodyTable td { vertical-align: top; padding: 0.25em; }\n    table.bodyTable th.tableTitle { background: #32357D; color: #FFFFFF;\n        font-weight: bold; text-align: left; }\n    table.bodyTable th.columnLabel { background: #656BFA; color: white;\n        font-style: italic; font-weight: normal; text-align: left; }\n    tr.oddRow { background: #F0F0F0; color: #000050; }\n    tr.evenRow { background: #D0D0D0; color: #000050; }\n  ");
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
