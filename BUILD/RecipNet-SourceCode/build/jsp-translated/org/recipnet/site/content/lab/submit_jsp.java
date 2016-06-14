package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AutoLocalLabIdField;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.ValidationOverrideButton;
import org.recipnet.site.content.rncontrols.WapPage;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

public final class submit_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_requireSampleId_requireSampleHistoryId_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfNewSample;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButton_option_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_required_prohibited_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfAutoNumberingIsConfigured;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_autoLocalLabIdField_selected_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfNumbersAreExhausted;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButton_option_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_autoLocalLabIdChecker_invert_includeOnlyIfAutoNumberingIsConfigured;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_label_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_condition_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_requireSampleId_requireSampleHistoryId_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfNewSample = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButton_option_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_required_prohibited_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfAutoNumberingIsConfigured = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_autoLocalLabIdField_selected_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfNumbersAreExhausted = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButton_option_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_autoLocalLabIdChecker_invert_includeOnlyIfAutoNumberingIsConfigured = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_label_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_condition_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_requireSampleId_requireSampleHistoryId_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfNewSample.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.release();
    _jspx_tagPool_ctl_radioButton_option_id_nobody.release();
    _jspx_tagPool_rn_sampleField_required_prohibited_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfAutoNumberingIsConfigured.release();
    _jspx_tagPool_rn_autoLocalLabIdField_selected_id_nobody.release();
    _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfNumbersAreExhausted.release();
    _jspx_tagPool_ctl_radioButton_option_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_autoLocalLabIdChecker_invert_includeOnlyIfAutoNumberingIsConfigured.release();
    _jspx_tagPool_rn_sampleField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.release();
    _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.release();
    _jspx_tagPool_rn_ltaIterator.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
    _jspx_tagPool_rn_wapCancelButton_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canSeeLabSummary.release();
    _jspx_tagPool_ctl_button_label_id_nobody.release();
    _jspx_tagPool_ctl_redirect_target_condition_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canSeeLabSummary.release();
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

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n");
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_requireSampleId_requireSampleHistoryId_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Submit");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode(SampleWorkflowBL.SUBMITTED);
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(SampleWorkflowBL.SUBMITTED_CORRECTED);
      _jspx_th_rn_wapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_wapPage_0.setRequireSampleId(false);
      _jspx_th_rn_wapPage_0.setRequireSampleHistoryId(false);
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
          out.write("\n  <div class=\"pageBody\">\n    <p class=\"pageInstructions\">\n      Enter the basic crystal data on the form below and click the\n      \"Save\" button to record it.\n    ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(
        WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("\n      <br/><span class=\"errorMessage\"\n        style=\"font-size: normal; font-style: italic\">You must address the\n        flagged validation errors before the data will be accepted.\n      </span>\n    ");
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
          _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_1.setErrorFilter(WapPage.DUPLICATE_LOCAL_LAB_ID);
          int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
          if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_1.doInitBody();
            }
            do {
              out.write("\n      <br/><span class=\"errorMessage\"\n        style=\"font-size: normal; font-style: italic\">The selected unique ID is\n        already in use; please choose a different one.\n      </span>\n    ");
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
          _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_2.setErrorFilter(WapPage.INVALID_PROVIDER_ID);
          int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
          if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_2.doInitBody();
            }
            do {
              out.write("\n      <br/><span class=\"errorMessage\"\n        style=\"font-size: normal; font-style: italic\">You may not submit samples\n        for the specified provider.  Specify a different one to continue, or\n        contact the Reciprocal Net site administrator.\n      </span>\n    ");
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
          out.write("\n    </p>\n    ");
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
              out.write("\n      <table class=\"bodyTable\">\n        <tr>\n          <th class=\"leadSectionHead\" colspan=\"2\">General Information</th>\n        </tr>\n        <tr>\n          <th >Originating lab:</th>\n          <td>");
              if (_jspx_meth_rn_labField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n        </tr>\n        ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_0.setIncludeIfNewSample(true);
              _jspx_th_rn_sampleChecker_0.setInvert(true);
              int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
              if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_0.doInitBody();
                }
                do {
                  out.write("\n          <tr>\n            <th>\n              ");
                  //  rn:sampleFieldLabel
                  org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
                  _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
                  _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
                  if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
                  out.write(":\n            </th>\n            <td>\n                ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
                  _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
                  int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
                  if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
                  out.write("\n            </td>\n          </tr>\n        ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample.reuse(_jspx_th_rn_sampleChecker_0);
              out.write("\n        ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfNewSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_1.setIncludeIfNewSample(true);
              int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
              if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_1.doInitBody();
                }
                do {
                  out.write("\n          ");
                  //  ctl:radioButtonGroup
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl sampleNumberSource = null;
                  org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
                  _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
                  _jspx_th_ctl_radioButtonGroup_0.setId("sampleNumberSource");
                  _jspx_th_ctl_radioButtonGroup_0.setInitialValue(new String("myChoice"));
                  int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
                  if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                    }
                    sampleNumberSource = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("sampleNumberSource");
                    do {
                      out.write("\n          <tr>\n            <th style=\"vertical-align: top;\">Unique local ID:</th>\n            <td style=\"white-space: nowrap;\">\n              ");
                      //  ctl:radioButton
                      org.recipnet.common.controls.RadioButtonHtmlControl myChoiceRadio = null;
                      org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_0 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id_nobody.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                      _jspx_th_ctl_radioButton_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_radioButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_ctl_radioButton_0.setOption("myChoice");
                      _jspx_th_ctl_radioButton_0.setId("myChoiceRadio");
                      int _jspx_eval_ctl_radioButton_0 = _jspx_th_ctl_radioButton_0.doStartTag();
                      if (_jspx_th_ctl_radioButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      myChoiceRadio = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("myChoiceRadio");
                      _jspx_tagPool_ctl_radioButton_option_id_nobody.reuse(_jspx_th_ctl_radioButton_0);
                      out.write("\n              My choice:\n              ");
                      //  rn:sampleField
                      org.recipnet.site.content.rncontrols.SampleField localLabId = null;
                      org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_required_prohibited_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                      _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                      _jspx_th_rn_sampleField_1.setId("localLabId");
                      _jspx_th_rn_sampleField_1.setRequired(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${myChoiceRadio.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      _jspx_th_rn_sampleField_1.setProhibited(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!myChoiceRadio.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
                      if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      localLabId = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("localLabId");
                      _jspx_tagPool_rn_sampleField_required_prohibited_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_1);
                      out.write("\n              ");
                      //  rn:autoLocalLabIdChecker
                      org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker _jspx_th_rn_autoLocalLabIdChecker_0 = (org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker) _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfAutoNumberingIsConfigured.get(org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker.class);
                      _jspx_th_rn_autoLocalLabIdChecker_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_autoLocalLabIdChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_autoLocalLabIdChecker_0.setIncludeOnlyIfAutoNumberingIsConfigured(true);
                      int _jspx_eval_rn_autoLocalLabIdChecker_0 = _jspx_th_rn_autoLocalLabIdChecker_0.doStartTag();
                      if (_jspx_eval_rn_autoLocalLabIdChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_autoLocalLabIdChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_autoLocalLabIdChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_autoLocalLabIdChecker_0.doInitBody();
                        }
                        do {
                          out.write("\n                <br />\n                ");
                          //  ctl:radioButton
                          org.recipnet.common.controls.RadioButtonHtmlControl labAssign = null;
                          org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_1 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id_nobody.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                          _jspx_th_ctl_radioButton_1.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_radioButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
                          _jspx_th_ctl_radioButton_1.setOption("labAssign");
                          _jspx_th_ctl_radioButton_1.setId("labAssign");
                          int _jspx_eval_ctl_radioButton_1 = _jspx_th_ctl_radioButton_1.doStartTag();
                          if (_jspx_th_ctl_radioButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          labAssign = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("labAssign");
                          _jspx_tagPool_ctl_radioButton_option_id_nobody.reuse(_jspx_th_ctl_radioButton_1);
                          out.write("\n                Next available sample number:&nbsp;&nbsp; \n                <span class=\"darkText\">\n                  ");
                          //  rn:autoLocalLabIdField
                          org.recipnet.site.content.rncontrols.AutoLocalLabIdField labAutoId = null;
                          org.recipnet.site.content.rncontrols.AutoLocalLabIdField _jspx_th_rn_autoLocalLabIdField_0 = (org.recipnet.site.content.rncontrols.AutoLocalLabIdField) _jspx_tagPool_rn_autoLocalLabIdField_selected_id_nobody.get(org.recipnet.site.content.rncontrols.AutoLocalLabIdField.class);
                          _jspx_th_rn_autoLocalLabIdField_0.setPageContext(_jspx_page_context);
                          _jspx_th_rn_autoLocalLabIdField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
                          _jspx_th_rn_autoLocalLabIdField_0.setId("labAutoId");
                          _jspx_th_rn_autoLocalLabIdField_0.setSelected(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${labAssign.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                          int _jspx_eval_rn_autoLocalLabIdField_0 = _jspx_th_rn_autoLocalLabIdField_0.doStartTag();
                          if (_jspx_th_rn_autoLocalLabIdField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          labAutoId = (org.recipnet.site.content.rncontrols.AutoLocalLabIdField) _jspx_page_context.findAttribute("labAutoId");
                          _jspx_tagPool_rn_autoLocalLabIdField_selected_id_nobody.reuse(_jspx_th_rn_autoLocalLabIdField_0);
                          out.write("\n                </span>\n                ");
                          if (_jspx_meth_rn_autoLocalLabIdChecker_1(_jspx_th_rn_autoLocalLabIdChecker_0, _jspx_page_context))
                            return;
                          out.write("\n                <br />\n                ");
                          if (_jspx_meth_ctl_radioButton_2(_jspx_th_rn_autoLocalLabIdChecker_0, _jspx_page_context))
                            return;
                          out.write("\n                Default numeric identifier\n                ");
                          //  ctl:errorMessage
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
                          _jspx_th_ctl_errorMessage_3.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${labAutoId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorMessage_3.setErrorFilter(
                        AutoLocalLabIdField.SELECTED_WITH_NULL_VALUE);
                          int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                          if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_3.doInitBody();
                            }
                            do {
                              out.write("\n                  <div style=\"max-width: 30em;\">\n                    <div class=\"errorMessageSmall\">\n                      The available numbers are exhausted.\n                    </div>\n                    <div class=\"errorMessageExtraSmall\">\n                      You must not select \"Next available sample number\"\n                      because no more numbers remain.  Please select\n                      another option and click \"Save\" to continue.\n                    </div>\n                  </div>\n                ");
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
                          out.write("\n                ");
                          //  ctl:errorMessage
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
                          _jspx_th_ctl_errorMessage_4.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${localLabId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorMessage_4.setInvertFilter(true);
                          _jspx_th_ctl_errorMessage_4.setErrorFilter(SampleField.REQUIRED_VALUE_IS_MISSING);
                          int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                          if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_4.doInitBody();
                            }
                            do {
                              out.write("\n                  ");
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_4);
                              _jspx_th_ctl_errorMessage_5.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${labAutoId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                              _jspx_th_ctl_errorMessage_5.setErrorFilter(
                        AutoLocalLabIdField.PARSED_VALUE_IS_NOT_MOST_RECENT);
                              int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_5.doInitBody();
                              }
                              do {
                              out.write("\n                    <div style=\"max-width: 30em;\">\n                      <div class=\"errorMessageSmall\">\n                        The previously suggested sample ID has been taken.\n                      </div>\n                      <div class=\"errorMessageExtraSmall\">\n                        A new sample number is displayed above.  Click the\n                        \"Save\" button to continue with the revised suggestion.\n                      </div>\n                    </div>\n                  ");
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
                              out.write("\n                ");
                              int evalDoAfterBody = _jspx_th_ctl_errorMessage_4.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorMessage_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_4);
                          out.write("\n                ");
                          //  ctl:errorMessage
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
                          _jspx_th_ctl_errorMessage_6.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${labAutoId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                          _jspx_th_ctl_errorMessage_6.setInvertFilter(true);
                          _jspx_th_ctl_errorMessage_6.setErrorFilter(
                        AutoLocalLabIdField.PARSED_VALUE_IS_NOT_MOST_RECENT);
                          int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                          if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_6.doInitBody();
                            }
                            do {
                              out.write("\n                  ");
                              //  ctl:errorMessage
                              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                              _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
                              _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_6);
                              _jspx_th_ctl_errorMessage_7.setErrorFilter(WapPage.DUPLICATE_LOCAL_LAB_ID);
                              int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
                              if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                              if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_7.doInitBody();
                              }
                              do {
                              out.write("\n                    <div style=\"max-width: 30em\">\n                      <div class=\"errorMessageSmall\">\n                        A sample with this local ID already exists for this lab.\n                      </div>\n                      <div class=\"errorMessageExtraSmall\">\n                        Select a new ID and click \"Save\" to continue.\n                      </div>\n                    </div>\n                  ");
                              int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                              } while (true);
                              if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                              }
                              if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                              return;
                              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_7);
                              out.write("\n                ");
                              int evalDoAfterBody = _jspx_th_ctl_errorMessage_6.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorMessage_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_6);
                          out.write("\n              ");
                          int evalDoAfterBody = _jspx_th_rn_autoLocalLabIdChecker_0.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_autoLocalLabIdChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_autoLocalLabIdChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfAutoNumberingIsConfigured.reuse(_jspx_th_rn_autoLocalLabIdChecker_0);
                      out.write("\n              ");
                      //  rn:autoLocalLabIdChecker
                      org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker _jspx_th_rn_autoLocalLabIdChecker_2 = (org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker) _jspx_tagPool_rn_autoLocalLabIdChecker_invert_includeOnlyIfAutoNumberingIsConfigured.get(org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker.class);
                      _jspx_th_rn_autoLocalLabIdChecker_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_autoLocalLabIdChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_rn_autoLocalLabIdChecker_2.setInvert(true);
                      _jspx_th_rn_autoLocalLabIdChecker_2.setIncludeOnlyIfAutoNumberingIsConfigured(true);
                      int _jspx_eval_rn_autoLocalLabIdChecker_2 = _jspx_th_rn_autoLocalLabIdChecker_2.doStartTag();
                      if (_jspx_eval_rn_autoLocalLabIdChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_rn_autoLocalLabIdChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_rn_autoLocalLabIdChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_rn_autoLocalLabIdChecker_2.doInitBody();
                        }
                        do {
                          out.write("\n                <br />\n                ");
                          if (_jspx_meth_ctl_radioButton_3(_jspx_th_rn_autoLocalLabIdChecker_2, _jspx_page_context))
                            return;
                          out.write("\n                Default numeric identifier\n                ");
                          //  ctl:errorMessage
                          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_8 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                          _jspx_th_ctl_errorMessage_8.setPageContext(_jspx_page_context);
                          _jspx_th_ctl_errorMessage_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_2);
                          _jspx_th_ctl_errorMessage_8.setErrorFilter(WapPage.DUPLICATE_LOCAL_LAB_ID);
                          int _jspx_eval_ctl_errorMessage_8 = _jspx_th_ctl_errorMessage_8.doStartTag();
                          if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                            if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                              out = _jspx_page_context.pushBody();
                              _jspx_th_ctl_errorMessage_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                              _jspx_th_ctl_errorMessage_8.doInitBody();
                            }
                            do {
                              out.write("\n                  <div style=\"max-width: 30em;\">\n                    <div class=\"errorMessageSmall\">\n                      A sample with this local ID already exists for this lab.\n                    </div>\n                    <div class=\"errorMessageExtraSmall\">\n                      This problem is probably transient.  Click the \"Save\"\n                      button again to continue.\n                    </div>\n                  </div>\n                ");
                              int evalDoAfterBody = _jspx_th_ctl_errorMessage_8.doAfterBody();
                              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                              break;
                            } while (true);
                            if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                              out = _jspx_page_context.popBody();
                          }
                          if (_jspx_th_ctl_errorMessage_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                            return;
                          _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_8);
                          out.write("\n              ");
                          int evalDoAfterBody = _jspx_th_rn_autoLocalLabIdChecker_2.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_rn_autoLocalLabIdChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_rn_autoLocalLabIdChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_autoLocalLabIdChecker_invert_includeOnlyIfAutoNumberingIsConfigured.reuse(_jspx_th_rn_autoLocalLabIdChecker_2);
                      out.write("\n              ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_9 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_9.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_ctl_errorMessage_9.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${localLabId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorMessage_9.setErrorFilter(SampleField.REQUIRED_VALUE_IS_MISSING);
                      int _jspx_eval_ctl_errorMessage_9 = _jspx_th_ctl_errorMessage_9.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_9.doInitBody();
                        }
                        do {
                          out.write("\n                <div style=\"max-width: 30em;\">\n                  <div class=\"errorMessageSmall\">\n                    No local sample ID entered.\n                  </div>\n                  <div class=\"errorMessageExtraSmall\">\n                    Either enter a unique local sample ID or specify that\n                    one should be generated automatically.\n                  </div>\n                </div>\n              ");
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
                      out.write("\n              ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_10 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_10.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_ctl_errorMessage_10.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${localLabId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorMessage_10.setErrorFilter(SampleField.PROHIBITED_VALUE_IS_PRESENT);
                      int _jspx_eval_ctl_errorMessage_10 = _jspx_th_ctl_errorMessage_10.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_10.doInitBody();
                        }
                        do {
                          out.write("\n                <div style=\"max-width: 30em;\">\n                  <div class=\"errorMessageSmall\">\n                    You have both entered a local sample ID and requested\n                    automatic local sample ID assignment.\n                  </div>\n                  <div class=\"errorMessageExtraSmall\">\n                    If you wish to use the entered ID, select\n                    <span style=\"white-space: nowrap;\">\"My Choice\"</span>,\n                    otherwise clear the sample text field associated with that\n                    option.  Click the \"Save\" button again to continue.\n                  </div>\n                </div>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_10.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_10);
                      out.write("\n              ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_11 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_11.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                      _jspx_th_ctl_errorMessage_11.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${localLabId}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
                      _jspx_th_ctl_errorMessage_11.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
                      int _jspx_eval_ctl_errorMessage_11 = _jspx_th_ctl_errorMessage_11.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_11.doInitBody();
                        }
                        do {
                          out.write("\n                <div style=\"max-width: 30em;\">\n                  <div class=\"errorMessageSmall\">\n                    The requested local sample ID is not valid.\n                  </div>\n                  <div class=\"errorMessageExtraSmall\">\n                    IDs may contain only upper- and lowercase ASCII letters,\n                    ASCII decimal digits, hyphens, underscores, and periods.\n                    They may not be blank.\n                  </div>\n                </div>\n              ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_11.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_11);
                      out.write("\n            </td>\n          </tr>\n          ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_0.doAfterBody();
                      sampleNumberSource = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("sampleNumberSource");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButtonGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  sampleNumberSource = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_page_context.findAttribute("sampleNumberSource");
                  _jspx_tagPool_ctl_radioButtonGroup_initialValue_id.reuse(_jspx_th_ctl_radioButtonGroup_0);
                  out.write("\n        ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfNewSample.reuse(_jspx_th_rn_sampleChecker_1);
              out.write("\n        <tr>\n          <th class=\"sectionHead\" colspan=\"2\">Sample Provider Information</th>\n        </tr>\n        <tr>\n          <th>\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleDataInfo.PROVIDER_ID_FIELD);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write(":\n          </th>\n          <td>\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_2.setFieldCode(SampleDataInfo.PROVIDER_ID_FIELD);
              int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
              if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_2);
              out.write("\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_12 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_12.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_12.setErrorFilter(WapPage.INVALID_PROVIDER_ID);
              int _jspx_eval_ctl_errorMessage_12 = _jspx_th_ctl_errorMessage_12.doStartTag();
              if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_12.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_12.doInitBody();
                }
                do {
                  out.write("\n              <div class=\"errorMessageSmall\" style=\"max-width: 30em;\">\n                You may not submit samples to that provider!\n              </div>\n            ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_12.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_12 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_12);
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_2.setFieldCode(SampleTextBL.PROVIDER_REFERENCE_NUMBER);
              int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
              out.write(":\n          </th>\n          <td>\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_3.setFieldCode(SampleTextBL.PROVIDER_REFERENCE_NUMBER);
              int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
              if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_3);
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th class=\"multiboxLabel\">\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_3.setFieldCode(SampleTextBL.SAMPLE_PROVIDER_NAME);
              int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
              out.write(":\n          </th>\n          <td>\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_4.setFieldCode(SampleTextBL.SAMPLE_PROVIDER_NAME);
              int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
              if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_4);
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\" class=\"sectionHead\" style=\"padding-top: 0;\">Sample\n            Information</th>\n        </tr>\n        <tr>\n          <th>\n            Anticipated empirical formula:\n          </th>\n          <td>\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField formula = null;
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_5.setId("formula");
              _jspx_th_rn_sampleField_5.setFieldCode(SampleTextBL.EMPIRICAL_FORMULA);
              int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
              if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              formula = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("formula");
              _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_5);
              out.write("\n\n            ");
              //  rn:overrideValidationButton
              org.recipnet.site.content.rncontrols.ValidationOverrideButton overrideButton = null;
              org.recipnet.site.content.rncontrols.ValidationOverrideButton _jspx_th_rn_overrideValidationButton_0 = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.get(org.recipnet.site.content.rncontrols.ValidationOverrideButton.class);
              _jspx_th_rn_overrideValidationButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_overrideValidationButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_overrideValidationButton_0.setSampleField((org.recipnet.site.content.rncontrols.SampleField) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${formula}", org.recipnet.site.content.rncontrols.SampleField.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_rn_overrideValidationButton_0.setLabel("Override");
              _jspx_th_rn_overrideValidationButton_0.setId("overrideButton");
              int _jspx_eval_rn_overrideValidationButton_0 = _jspx_th_rn_overrideValidationButton_0.doStartTag();
              if (_jspx_th_rn_overrideValidationButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              overrideButton = (org.recipnet.site.content.rncontrols.ValidationOverrideButton) _jspx_page_context.findAttribute("overrideButton");
              _jspx_tagPool_rn_overrideValidationButton_sampleField_label_id_nobody.reuse(_jspx_th_rn_overrideValidationButton_0);
              out.write("\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_13 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_13.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_13.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${overrideButton}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_13.setErrorFilter(
                        ValidationOverrideButton.VALIDATION_OVERRIDDEN);
              int _jspx_eval_ctl_errorMessage_13 = _jspx_th_ctl_errorMessage_13.doStartTag();
              if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_13.doInitBody();
                }
                do {
                  out.write("\n              <span class=\"notice\">(validation overridden)</span>\n            ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_13.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_13);
              out.write("\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_14 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_14.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_14.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${formula}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              _jspx_th_ctl_errorMessage_14.setErrorFilter(SampleField.VALIDATOR_REJECTED_VALUE);
              int _jspx_eval_ctl_errorMessage_14 = _jspx_th_ctl_errorMessage_14.doStartTag();
              if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_14.doInitBody();
                }
                do {
                  out.write("\n              <div class=\"errorNotice\">\n                The entered empirical formula is invalid.\n              </div>\n              <div class=\"notice\">\n                Correct the formula and click \"Save\" to resubmit, or click\n                \"Override\" to record the invalid formula.\n              </div>\n              <div class=\"notice\">\n                WARNING: invalid formulae are not searchable.\n              </div>\n            ");
                  int evalDoAfterBody = _jspx_th_ctl_errorMessage_14.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_errorMessage_14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_errorMessage_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_14);
              out.write("\n          </td>\n        </tr>\n        ");
              if (_jspx_meth_rn_ltaIterator_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        <tr>\n          <th class=\"sectionHead\" colspan=\"2\">Comments</th>\n        </tr>\n        <tr>\n          <td colspan=\"2\" style=\"text-align: center;\">\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <td class=\"formButtons\" colspan=\"2\">\n            ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            ");
              if (_jspx_meth_rn_sampleChecker_2(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            ");
              //  rn:sampleChecker
              org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_3 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfNewSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
              _jspx_th_rn_sampleChecker_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleChecker_3.setIncludeIfNewSample(true);
              int _jspx_eval_rn_sampleChecker_3 = _jspx_th_rn_sampleChecker_3.doStartTag();
              if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleChecker_3.doInitBody();
                }
                do {
                  out.write("\n              ");
                  //  rn:authorizationChecker
                  org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
                  _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_3);
                  _jspx_th_rn_authorizationChecker_0.setCanSeeLabSummary(true);
                  _jspx_th_rn_authorizationChecker_0.setSuppressRenderingOnly(true);
                  int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
                  if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_authorizationChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  ctl:button
                      org.recipnet.common.controls.ButtonHtmlControl cancelToSummary = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_id_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
                      _jspx_th_ctl_button_0.setLabel("Cancel");
                      _jspx_th_ctl_button_0.setId("cancelToSummary");
                      int _jspx_eval_ctl_button_0 = _jspx_th_ctl_button_0.doStartTag();
                      if (_jspx_th_ctl_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      cancelToSummary = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("cancelToSummary");
                      _jspx_tagPool_ctl_button_label_id_nobody.reuse(_jspx_th_ctl_button_0);
                      out.write("\n                ");
                      if (_jspx_meth_ctl_redirect_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_0);
                  out.write("\n              ");
                  //  rn:authorizationChecker
                  org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
                  _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_3);
                  _jspx_th_rn_authorizationChecker_1.setCanSeeLabSummary(true);
                  _jspx_th_rn_authorizationChecker_1.setInvert(true);
                  _jspx_th_rn_authorizationChecker_1.setSuppressRenderingOnly(true);
                  int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
                  if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_authorizationChecker_1.doInitBody();
                    }
                    do {
                      out.write("\n                ");
                      //  ctl:button
                      org.recipnet.common.controls.ButtonHtmlControl cancelToIndex = null;
                      org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_1 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_label_id_nobody.get(org.recipnet.common.controls.ButtonHtmlControl.class);
                      _jspx_th_ctl_button_1.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_button_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                      _jspx_th_ctl_button_1.setLabel("Cancel");
                      _jspx_th_ctl_button_1.setId("cancelToIndex");
                      int _jspx_eval_ctl_button_1 = _jspx_th_ctl_button_1.doStartTag();
                      if (_jspx_th_ctl_button_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      cancelToIndex = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_page_context.findAttribute("cancelToIndex");
                      _jspx_tagPool_ctl_button_label_id_nobody.reuse(_jspx_th_ctl_button_1);
                      out.write("\n                ");
                      if (_jspx_meth_ctl_redirect_1(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_authorizationChecker_suppressRenderingOnly_invert_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_1);
                  out.write("\n            ");
                  int evalDoAfterBody = _jspx_th_rn_sampleChecker_3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleChecker_includeIfNewSample.reuse(_jspx_th_rn_sampleChecker_3);
              out.write("\n          </td>\n        </tr>\n      </table>\n    ");
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
          out.write("\n  </div>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_wapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_wapPage_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_wapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_wapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_requireSampleId_requireSampleHistoryId_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_wapPage_0);
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

  private boolean _jspx_meth_rn_autoLocalLabIdChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_autoLocalLabIdChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:autoLocalLabIdChecker
    org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker _jspx_th_rn_autoLocalLabIdChecker_1 = (org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker) _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfNumbersAreExhausted.get(org.recipnet.site.content.rncontrols.AutoLocalLabIdChecker.class);
    _jspx_th_rn_autoLocalLabIdChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_autoLocalLabIdChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
    _jspx_th_rn_autoLocalLabIdChecker_1.setIncludeOnlyIfNumbersAreExhausted(true);
    int _jspx_eval_rn_autoLocalLabIdChecker_1 = _jspx_th_rn_autoLocalLabIdChecker_1.doStartTag();
    if (_jspx_eval_rn_autoLocalLabIdChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_autoLocalLabIdChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_autoLocalLabIdChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_autoLocalLabIdChecker_1.doInitBody();
      }
      do {
        out.write("\n                  <span class=\"errorMessage\">(numbers exhausted)</span>\n                ");
        int evalDoAfterBody = _jspx_th_rn_autoLocalLabIdChecker_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_autoLocalLabIdChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_autoLocalLabIdChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_autoLocalLabIdChecker_includeOnlyIfNumbersAreExhausted.reuse(_jspx_th_rn_autoLocalLabIdChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_radioButton_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_autoLocalLabIdChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:radioButton
    org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_2 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_nobody.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
    _jspx_th_ctl_radioButton_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_radioButton_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_0);
    _jspx_th_ctl_radioButton_2.setOption("autoAssign");
    int _jspx_eval_ctl_radioButton_2 = _jspx_th_ctl_radioButton_2.doStartTag();
    if (_jspx_th_ctl_radioButton_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_radioButton_option_nobody.reuse(_jspx_th_ctl_radioButton_2);
    return false;
  }

  private boolean _jspx_meth_ctl_radioButton_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_autoLocalLabIdChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:radioButton
    org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_3 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_nobody.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
    _jspx_th_ctl_radioButton_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_radioButton_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_autoLocalLabIdChecker_2);
    _jspx_th_ctl_radioButton_3.setOption("autoAssign");
    int _jspx_eval_ctl_radioButton_3 = _jspx_th_ctl_radioButton_3.doStartTag();
    if (_jspx_th_ctl_radioButton_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_radioButton_option_nobody.reuse(_jspx_th_ctl_radioButton_3);
    return false;
  }

  private boolean _jspx_meth_rn_ltaIterator_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
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
        out.write("\n          <tr>\n            <th>\n              ");
        if (_jspx_meth_rn_sampleFieldLabel_4(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write(":\n            </th>\n            <td>\n              ");
        if (_jspx_meth_rn_sampleField_6(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write("\n            </td>\n          </tr>\n        ");
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

  private boolean _jspx_meth_rn_sampleFieldLabel_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_4 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_4 = _jspx_th_rn_sampleFieldLabel_4.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_4);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_6(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
    if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_6);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldUnits_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldUnits
    org.recipnet.site.content.rncontrols.SampleFieldUnits _jspx_th_rn_sampleFieldUnits_0 = (org.recipnet.site.content.rncontrols.SampleFieldUnits) _jspx_tagPool_rn_sampleFieldUnits_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldUnits.class);
    _jspx_th_rn_sampleFieldUnits_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldUnits_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldUnits_0 = _jspx_th_rn_sampleFieldUnits_0.doStartTag();
    if (_jspx_th_rn_sampleFieldUnits_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldUnits_nobody.reuse(_jspx_th_rn_sampleFieldUnits_0);
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

  private boolean _jspx_meth_rn_sampleChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_2 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_sampleChecker_2.setIncludeIfNewSample(true);
    _jspx_th_rn_sampleChecker_2.setInvert(true);
    int _jspx_eval_rn_sampleChecker_2 = _jspx_th_rn_sampleChecker_2.doStartTag();
    if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_2.doInitBody();
      }
      do {
        out.write("\n              ");
        if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_rn_sampleChecker_2, _jspx_page_context))
          return true;
        out.write("\n            ");
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_invert_includeIfNewSample.reuse(_jspx_th_rn_sampleChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_wapCancelButton_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:wapCancelButton
    org.recipnet.site.content.rncontrols.WapCancelButton _jspx_th_rn_wapCancelButton_0 = (org.recipnet.site.content.rncontrols.WapCancelButton) _jspx_tagPool_rn_wapCancelButton_nobody.get(org.recipnet.site.content.rncontrols.WapCancelButton.class);
    _jspx_th_rn_wapCancelButton_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_wapCancelButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_2);
    int _jspx_eval_rn_wapCancelButton_0 = _jspx_th_rn_wapCancelButton_0.doStartTag();
    if (_jspx_th_rn_wapCancelButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_wapCancelButton_nobody.reuse(_jspx_th_rn_wapCancelButton_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_condition_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_ctl_redirect_0.setTarget("/lab/summary.jsp");
    _jspx_th_ctl_redirect_0.setCondition(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cancelToSummary.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_condition_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_1 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_condition_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_redirect_1.setTarget("/index.jsp");
    _jspx_th_ctl_redirect_1.setCondition(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${cancelToIndex.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_ctl_redirect_1 = _jspx_th_ctl_redirect_1.doStartTag();
    if (_jspx_th_ctl_redirect_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_condition_nobody.reuse(_jspx_th_ctl_redirect_1);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessageSmall { color: #F00000; font-size: small; margin-left: 4em; }\n    .errorMessageExtraSmall { font-family: sans-serif; font-size: x-small;\n        color: #909090; text-align: left; margin-left: 4em; }\n    .briefComment { font-size: x-small; color: #9E9E9E; text-align: left; }\n    .darkText { font-weight: bold; color: #000050; }\n  ");
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
