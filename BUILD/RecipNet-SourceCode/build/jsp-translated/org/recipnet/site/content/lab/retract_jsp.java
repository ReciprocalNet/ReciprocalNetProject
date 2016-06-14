package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.RetractWapPage;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.SampleSelector;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class retract_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  public String getServletInfo() {
    return "Retract Sample";
  }

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_retractWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButtonGroup_initialValue;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_radioButton_option_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleSelector_required_prohibited_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_a_renderId_openInWindow_onClick_id_href;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_undecidedSampleContext_subjectSampleSelector_registerWithRetractWapPage_annotationType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_undecidedSampleContext_referenceSampleSelector_annotationType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_retractWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButtonGroup_initialValue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_radioButton_option_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleSelector_required_prohibited_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_a_renderId_openInWindow_onClick_id_href = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_undecidedSampleContext_subjectSampleSelector_registerWithRetractWapPage_annotationType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_undecidedSampleContext_referenceSampleSelector_annotationType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_retractWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_radioButtonGroup_initialValue.release();
    _jspx_tagPool_ctl_radioButton_option_id.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_rn_sampleSelector_required_prohibited_id_nobody.release();
    _jspx_tagPool_ctl_a_renderId_openInWindow_onClick_id_href.release();
    _jspx_tagPool_rn_labParam_name_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_undecidedSampleContext_subjectSampleSelector_registerWithRetractWapPage_annotationType.release();
    _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody.release();
    _jspx_tagPool_rn_undecidedSampleContext_referenceSampleSelector_annotationType.release();
    _jspx_tagPool_rn_ltaIterator_id.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
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

      out.write("\n\n\n\n");
      //  rn:retractWapPage
      org.recipnet.site.content.rncontrols.RetractWapPage _jspx_th_rn_retractWapPage_0 = (org.recipnet.site.content.rncontrols.RetractWapPage) _jspx_tagPool_rn_retractWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.RetractWapPage.class);
      _jspx_th_rn_retractWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_retractWapPage_0.setParent(null);
      _jspx_th_rn_retractWapPage_0.setTitle("Retract");
      _jspx_th_rn_retractWapPage_0.setWorkflowActionCode(SampleWorkflowBL.RETRACTED);
      _jspx_th_rn_retractWapPage_0.setWorkflowActionCodeCorrected(SampleWorkflowBL.RETRACTED);
      _jspx_th_rn_retractWapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_retractWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_retractWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_retractWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_retractWapPage_0 = _jspx_th_rn_retractWapPage_0.doStartTag();
      if (_jspx_eval_rn_retractWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RetractWapPage retractPage = null;
        if (_jspx_eval_rn_retractWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_retractWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_retractWapPage_0.doInitBody();
        }
        retractPage = (org.recipnet.site.content.rncontrols.RetractWapPage) _jspx_page_context.findAttribute("retractPage");
        do {
          out.write("\n  <div class=\"pageBody\">\n    <p class=\"pageInstructions\">\n        You are about to retract this sample from Reciprocal Net. You may\n        optionally indicate another sample from the same lab\n        (");
          if (_jspx_meth_rn_labField_0(_jspx_th_rn_retractWapPage_0, _jspx_page_context))
            return;
          out.write(") that supersedes the one you are about to retract\n        (");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_retractWapPage_0);
          _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
          _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
          if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
          out.write(").\n      ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_retractWapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(
          RetractWapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("<br />\n          <span class=\"errorMessage\"\n                style=\"font-weight: normal; font-style: italic;\">\n            You must address the flagged validation errors before the data\n            will be accepted.\n          </span>\n      ");
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
          out.write("\n    </p>\n    ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_retractWapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n      <table class=\"bodyTable\">\n        <tr>\n          <th class=\"leadSectionHead\" colspan=\"2\">Sample to Retract</th>\n        </tr>\n        <tr>\n          <th style=\"white-space: nowrap;\">Laboratory:</th>\n          <td>");
              if (_jspx_meth_rn_labField_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n        </tr>\n        <tr>\n          <th style=\"white-space: nowrap;\">Local sample ID:</th>\n          <td>");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_1.setDisplayAsLabel(true);
              _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
              if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_1);
              out.write("</td>\n        </tr>\n        <tr>\n          <th class=\"sectionHead\" colspan=\"2\">Superseding Sample</th>\n        </tr>\n        ");
              //  ctl:radioButtonGroup
              org.recipnet.common.controls.RadioButtonGroupHtmlControl _jspx_th_ctl_radioButtonGroup_0 = (org.recipnet.common.controls.RadioButtonGroupHtmlControl) _jspx_tagPool_ctl_radioButtonGroup_initialValue.get(org.recipnet.common.controls.RadioButtonGroupHtmlControl.class);
              _jspx_th_ctl_radioButtonGroup_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_radioButtonGroup_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_radioButtonGroup_0.setInitialValue(new String("false"));
              int _jspx_eval_ctl_radioButtonGroup_0 = _jspx_th_ctl_radioButtonGroup_0.doStartTag();
              if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_radioButtonGroup_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_radioButtonGroup_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td colspan=\"2\">\n            <div id=\"option1\" class=\"optionDiv\">\n              ");
                  //  ctl:radioButton
                  org.recipnet.common.controls.RadioButtonHtmlControl noSampleSupersedes = null;
                  org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_0 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                  _jspx_th_ctl_radioButton_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_radioButton_0.setId("noSampleSupersedes");
                  _jspx_th_ctl_radioButton_0.setOption("false");
                  int _jspx_eval_ctl_radioButton_0 = _jspx_th_ctl_radioButton_0.doStartTag();
                  if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButton_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButton_0.doInitBody();
                    }
                    noSampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("noSampleSupersedes");
                    do {
                      out.write("\n                ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_ctl_radioButton_0, _jspx_page_context))
                        return;
                      out.write("\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButton_0.doAfterBody();
                      noSampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("noSampleSupersedes");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButton_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  noSampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("noSampleSupersedes");
                  _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_0);
                  out.write(" No sample supersedes sample\n              ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_sampleField_2.setDisplayAsLabel(true);
                  _jspx_th_rn_sampleField_2.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
                  if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_2);
                  out.write("\n            </div>\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"2\">\n            <div id=\"option2\" class=\"optionDiv\">\n              <div style=\"margin-bottom: 0.5em;\">\n                ");
                  //  ctl:radioButton
                  org.recipnet.common.controls.RadioButtonHtmlControl sampleSupersedes = null;
                  org.recipnet.common.controls.RadioButtonHtmlControl _jspx_th_ctl_radioButton_1 = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_tagPool_ctl_radioButton_option_id.get(org.recipnet.common.controls.RadioButtonHtmlControl.class);
                  _jspx_th_ctl_radioButton_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_radioButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_radioButton_1.setId("sampleSupersedes");
                  _jspx_th_ctl_radioButton_1.setOption("true");
                  int _jspx_eval_ctl_radioButton_1 = _jspx_th_ctl_radioButton_1.doStartTag();
                  if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_radioButton_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_radioButton_1.doInitBody();
                    }
                    sampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("sampleSupersedes");
                    do {
                      out.write("\n                  ");
                      if (_jspx_meth_ctl_extraHtmlAttribute_1(_jspx_th_ctl_radioButton_1, _jspx_page_context))
                        return;
                      out.write("\n                ");
                      int evalDoAfterBody = _jspx_th_ctl_radioButton_1.doAfterBody();
                      sampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("sampleSupersedes");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_radioButton_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_radioButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  sampleSupersedes = (org.recipnet.common.controls.RadioButtonHtmlControl) _jspx_page_context.findAttribute("sampleSupersedes");
                  _jspx_tagPool_ctl_radioButton_option_id.reuse(_jspx_th_ctl_radioButton_1);
                  out.write("\n                This sample supersedes sample\n                ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_sampleField_3.setDisplayAsLabel(true);
                  _jspx_th_rn_sampleField_3.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
                  if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_3);
                  out.write(":\n              </div>\n              <div style=\"padding-left: 2em;\">\n              <span style=\"font-weight: bold; white-space: nowrap;\">");
                  if (_jspx_meth_rn_labField_2(_jspx_th_ctl_radioButtonGroup_0, _jspx_page_context))
                    return;
                  out.write(" Local Sample ID:</span>\n              ");
                  //  rn:sampleSelector
                  org.recipnet.site.content.rncontrols.SampleSelector sampleSelector = null;
                  org.recipnet.site.content.rncontrols.SampleSelector _jspx_th_rn_sampleSelector_0 = (org.recipnet.site.content.rncontrols.SampleSelector) _jspx_tagPool_rn_sampleSelector_required_prohibited_id_nobody.get(org.recipnet.site.content.rncontrols.SampleSelector.class);
                  _jspx_th_rn_sampleSelector_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleSelector_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_sampleSelector_0.setId("sampleSelector");
                  _jspx_th_rn_sampleSelector_0.setRequired(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                  _jspx_th_rn_sampleSelector_0.setProhibited(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${noSampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                  int _jspx_eval_rn_sampleSelector_0 = _jspx_th_rn_sampleSelector_0.doStartTag();
                  if (_jspx_th_rn_sampleSelector_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  sampleSelector = (org.recipnet.site.content.rncontrols.SampleSelector) _jspx_page_context.findAttribute("sampleSelector");
                  _jspx_tagPool_rn_sampleSelector_required_prohibited_id_nobody.reuse(_jspx_th_rn_sampleSelector_0);
                  out.write("\n              ");
                  //  ctl:a
                  org.recipnet.common.controls.LinkHtmlElement browseSamples = null;
                  org.recipnet.common.controls.LinkHtmlElement _jspx_th_ctl_a_0 = (org.recipnet.common.controls.LinkHtmlElement) _jspx_tagPool_ctl_a_renderId_openInWindow_onClick_id_href.get(org.recipnet.common.controls.LinkHtmlElement.class);
                  _jspx_th_ctl_a_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_a_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_a_0.setId("browseSamples");
                  _jspx_th_ctl_a_0.setHref("/search.jsp");
                  _jspx_th_ctl_a_0.setRenderId(true);
                  _jspx_th_ctl_a_0.setOpenInWindow("search");
                  _jspx_th_ctl_a_0.setDynamicAttribute(null, "onClick", new String("isBrowseEnabled()"));
                  int _jspx_eval_ctl_a_0 = _jspx_th_ctl_a_0.doStartTag();
                  if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_a_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_a_0.doInitBody();
                    }
                    browseSamples = (org.recipnet.common.controls.LinkHtmlElement) _jspx_page_context.findAttribute("browseSamples");
                    do {
                      out.write("browse all...\n                ");
                      if (_jspx_meth_rn_labParam_0(_jspx_th_ctl_a_0, _jspx_page_context))
                        return;
                      int evalDoAfterBody = _jspx_th_ctl_a_0.doAfterBody();
                      browseSamples = (org.recipnet.common.controls.LinkHtmlElement) _jspx_page_context.findAttribute("browseSamples");
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_a_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_a_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  browseSamples = (org.recipnet.common.controls.LinkHtmlElement) _jspx_page_context.findAttribute("browseSamples");
                  _jspx_tagPool_ctl_a_renderId_openInWindow_onClick_id_href.reuse(_jspx_th_ctl_a_0);
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier(sampleSelector);
                  _jspx_th_ctl_errorMessage_1.setErrorFilter( SampleSelector.PROHIBITED_VALUE_IS_PRESENT);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n                <br/><span class=\"errorMessage\">You may not specify a\n                superseding sample without indicating that you in fact wish to\n                supersede the existing sample.</span>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_errorMessage_2.setErrorSupplier(sampleSelector);
                  _jspx_th_ctl_errorMessage_2.setErrorFilter( SampleSelector.REQUIRED_VALUE_IS_MISSING);
                  int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_2.doInitBody();
                    }
                    do {
                      out.write("\n                <br/><span class=\"errorMessage\">You must specify a superseding \n                    sample when indicating that you wish to supersede the\n                    existing sample.</span>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_errorMessage_3.setErrorSupplier(sampleSelector);
                  _jspx_th_ctl_errorMessage_3.setErrorFilter( SampleSelector.INVALID_LOCALLABID);
                  int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_3.doInitBody();
                    }
                    do {
                      out.write("\n                <span class=\"errorMessage\">Unknown sample</span><br/>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_ctl_errorMessage_4.setErrorFilter(
                      RetractWapPage.CANNOT_SUPERSEDE_WITH_RETRACTED_SAMPLE);
                  int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_4.doInitBody();
                    }
                    do {
                      out.write("\n                <br/><span class=\"errorMessage\">You may not supersede a\n                  retracted sample with another retracted sample.</span>\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_4.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorFilter.reuse(_jspx_th_ctl_errorMessage_4);
                  out.write("\n              <table>\n                <tr>\n                  <th>Note for the superseding sample (specified\n                    above):</th>\n                  <th style=\"padding-left: 2em;\">Note for the superseded sample\n                    (");
                  if (_jspx_meth_rn_labField_3(_jspx_th_ctl_radioButtonGroup_0, _jspx_page_context))
                    return;
                  out.write(" sample\n                    ");
                  //  rn:sampleField
                  org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                  _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_sampleField_4.setDisplayAsLabel(true);
                  _jspx_th_rn_sampleField_4.setFieldCode(SampleInfo.LOCAL_LAB_ID);
                  int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
                  if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_4);
                  out.write("):</th>\n                </tr>\n                <tr>\n                  ");
                  //  rn:undecidedSampleContext
                  org.recipnet.site.content.rncontrols.UndecidedSampleContext _jspx_th_rn_undecidedSampleContext_0 = (org.recipnet.site.content.rncontrols.UndecidedSampleContext) _jspx_tagPool_rn_undecidedSampleContext_subjectSampleSelector_registerWithRetractWapPage_annotationType.get(org.recipnet.site.content.rncontrols.UndecidedSampleContext.class);
                  _jspx_th_rn_undecidedSampleContext_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_undecidedSampleContext_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_undecidedSampleContext_0.setSubjectSampleSelector(sampleSelector);
                  _jspx_th_rn_undecidedSampleContext_0.setAnnotationType(
                        SampleTextBL.SUPERSEDES_ANOTHER_SAMPLE);
                  _jspx_th_rn_undecidedSampleContext_0.setRegisterWithRetractWapPage(true);
                  int _jspx_eval_rn_undecidedSampleContext_0 = _jspx_th_rn_undecidedSampleContext_0.doStartTag();
                  if (_jspx_eval_rn_undecidedSampleContext_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_undecidedSampleContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_undecidedSampleContext_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_undecidedSampleContext_0.doInitBody();
                    }
                    do {
                      out.write("\n                    <td>");
                      //  rn:sampleField
                      org.recipnet.site.content.rncontrols.SampleField supersedingNotes = null;
                      org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_5 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                      _jspx_th_rn_sampleField_5.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_0);
                      _jspx_th_rn_sampleField_5.setId("supersedingNotes");
                      _jspx_th_rn_sampleField_5.setRequired(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      _jspx_th_rn_sampleField_5.setProhibited(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${noSampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      _jspx_th_rn_sampleField_5.setOwnedElementId("supersedingNotesArea");
                      int _jspx_eval_rn_sampleField_5 = _jspx_th_rn_sampleField_5.doStartTag();
                      if (_jspx_th_rn_sampleField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      supersedingNotes = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("supersedingNotes");
                      _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody.reuse(_jspx_th_rn_sampleField_5);
                      out.write("\n                      ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_5 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_5.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_0);
                      _jspx_th_ctl_errorMessage_5.setErrorSupplier(supersedingNotes);
                      _jspx_th_ctl_errorMessage_5.setErrorFilter(
                            SampleField.PROHIBITED_VALUE_IS_PRESENT);
                      int _jspx_eval_ctl_errorMessage_5 = _jspx_th_ctl_errorMessage_5.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_5.doInitBody();
                        }
                        do {
                          out.write("\n                        <div class=\"errorMessage\">\n                          These notes may not be specified unless you indicate\n                          that you wish to supersede the sample with another\n                          sample.\n                        </div>\n                      ");
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
                      out.write("\n                      ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_6 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_6.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_0);
                      _jspx_th_ctl_errorMessage_6.setErrorSupplier(supersedingNotes);
                      _jspx_th_ctl_errorMessage_6.setErrorFilter(
                               SampleField.REQUIRED_VALUE_IS_MISSING);
                      int _jspx_eval_ctl_errorMessage_6 = _jspx_th_ctl_errorMessage_6.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_6.doInitBody();
                        }
                        do {
                          out.write("\n                        <div class=\"errorMessage\">\n                          These notes must be specified if you wish to supersede\n                          the sample.\n                        </div>\n                      ");
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
                      out.write("\n                    </td>\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_undecidedSampleContext_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_undecidedSampleContext_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_undecidedSampleContext_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_undecidedSampleContext_subjectSampleSelector_registerWithRetractWapPage_annotationType.reuse(_jspx_th_rn_undecidedSampleContext_0);
                  out.write("  \n                  ");
                  //  rn:undecidedSampleContext
                  org.recipnet.site.content.rncontrols.UndecidedSampleContext _jspx_th_rn_undecidedSampleContext_1 = (org.recipnet.site.content.rncontrols.UndecidedSampleContext) _jspx_tagPool_rn_undecidedSampleContext_referenceSampleSelector_annotationType.get(org.recipnet.site.content.rncontrols.UndecidedSampleContext.class);
                  _jspx_th_rn_undecidedSampleContext_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_undecidedSampleContext_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
                  _jspx_th_rn_undecidedSampleContext_1.setAnnotationType(
                        SampleTextBL.SUPERSEDED_BY_ANOTHER_SAMPLE);
                  _jspx_th_rn_undecidedSampleContext_1.setReferenceSampleSelector(sampleSelector);
                  int _jspx_eval_rn_undecidedSampleContext_1 = _jspx_th_rn_undecidedSampleContext_1.doStartTag();
                  if (_jspx_eval_rn_undecidedSampleContext_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_undecidedSampleContext_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_undecidedSampleContext_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_undecidedSampleContext_1.doInitBody();
                    }
                    do {
                      out.write("\n                    <td style=\"padding-left: 2em\">\n                      ");
                      //  rn:sampleField
                      org.recipnet.site.content.rncontrols.SampleField supersededNotes = null;
                      org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_6 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
                      _jspx_th_rn_sampleField_6.setPageContext(_jspx_page_context);
                      _jspx_th_rn_sampleField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_1);
                      _jspx_th_rn_sampleField_6.setId("supersededNotes");
                      _jspx_th_rn_sampleField_6.setProhibited(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${noSampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      _jspx_th_rn_sampleField_6.setRequired(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sampleSupersedes.valueAsBoolean}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
                      _jspx_th_rn_sampleField_6.setOwnedElementId("supersededNotesArea");
                      int _jspx_eval_rn_sampleField_6 = _jspx_th_rn_sampleField_6.doStartTag();
                      if (_jspx_th_rn_sampleField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      supersededNotes = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("supersededNotes");
                      _jspx_tagPool_rn_sampleField_required_prohibited_ownedElementId_id_nobody.reuse(_jspx_th_rn_sampleField_6);
                      out.write("\n                      ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_7 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_7.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_1);
                      _jspx_th_ctl_errorMessage_7.setErrorSupplier(supersededNotes);
                      _jspx_th_ctl_errorMessage_7.setErrorFilter(
                            SampleField.REQUIRED_VALUE_IS_MISSING);
                      int _jspx_eval_ctl_errorMessage_7 = _jspx_th_ctl_errorMessage_7.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_7.doInitBody();
                        }
                        do {
                          out.write("\n                        <div class=\"errorMessage\">\n                          These notes must be specified if you wish to supersede\n                          the sample.\n                        </div>\n                      ");
                          int evalDoAfterBody = _jspx_th_ctl_errorMessage_7.doAfterBody();
                          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                            break;
                        } while (true);
                        if (_jspx_eval_ctl_errorMessage_7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                          out = _jspx_page_context.popBody();
                      }
                      if (_jspx_th_ctl_errorMessage_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_7);
                      out.write("\n                      ");
                      //  ctl:errorMessage
                      org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_8 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                      _jspx_th_ctl_errorMessage_8.setPageContext(_jspx_page_context);
                      _jspx_th_ctl_errorMessage_8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_undecidedSampleContext_1);
                      _jspx_th_ctl_errorMessage_8.setErrorSupplier(supersededNotes);
                      _jspx_th_ctl_errorMessage_8.setErrorFilter(
                            SampleField.PROHIBITED_VALUE_IS_PRESENT);
                      int _jspx_eval_ctl_errorMessage_8 = _jspx_th_ctl_errorMessage_8.doStartTag();
                      if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        if (_jspx_eval_ctl_errorMessage_8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                          out = _jspx_page_context.pushBody();
                          _jspx_th_ctl_errorMessage_8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                          _jspx_th_ctl_errorMessage_8.doInitBody();
                        }
                        do {
                          out.write("\n                        <div class=\"errorMessage\">\n                          These notes may not be specified unless you indicate\n                          that you wish to supersede the sample with another\n                          sample.\n                        </div>\n                      ");
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
                      out.write("\n                    </td>\n                  ");
                      int evalDoAfterBody = _jspx_th_rn_undecidedSampleContext_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_undecidedSampleContext_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_undecidedSampleContext_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_undecidedSampleContext_referenceSampleSelector_annotationType.reuse(_jspx_th_rn_undecidedSampleContext_1);
                  out.write("  \n                </tr>\n              </table>\n              </div>\n            </div>\n          </td>\n        </tr>\n        ");
                  int evalDoAfterBody = _jspx_th_ctl_radioButtonGroup_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_ctl_radioButtonGroup_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_ctl_radioButtonGroup_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_ctl_radioButtonGroup_initialValue.reuse(_jspx_th_ctl_radioButtonGroup_0);
              out.write("\n        ");
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
                  out.write("\n          ");
                  //  ctl:errorChecker
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorChecker_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorChecker_invert_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorChecker_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
                  _jspx_th_ctl_errorChecker_0.setErrorFilter( HtmlPageIterator.NO_ITERATIONS );
                  _jspx_th_ctl_errorChecker_0.setInvert(true);
                  int _jspx_eval_ctl_errorChecker_0 = _jspx_th_ctl_errorChecker_0.doStartTag();
                  if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorChecker_0.doInitBody();
                    }
                    do {
                      out.write("\n            ");
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltas.currentIterationFirst ?\n              '<tr>\n                <th class=\"sectionHead\"\n                  colspan=\"3\">Additional Sample Information</th>\n              </tr>' : ''}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
                      out.write("\n          ");
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
                  out.write("\n          <tr>\n            <th style=\"white-space: nowrap; width:1px; max-width: 1px;\">\n              ");
                  if (_jspx_meth_rn_sampleFieldLabel_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  out.write(":\n            </th>\n            <td colspan=\"2\">\n              ");
                  if (_jspx_meth_rn_sampleField_7(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  out.write("\n            </td>\n          </tr>\n        ");
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
              out.write("\n        <tr>\n          <th colspan=\"3\" class=\"sectionHead\">Comments</th>\n        </tr>\n        <tr>\n          <td style=\"text-align: center;\" colspan=\"3\">\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <td class=\"formButtons\" colspan=\"3\">\n            ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n            ");
              if (_jspx_meth_rn_wapCancelButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
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
          out.write("\n  </div>\n\n  <script type=\"text/Javascript\">\n  var mainOptions = new Array(\"sampleSupersedes\", \"noSampleSupersedes\")\n  var optionChildren = new Array()\n  var optionPeers = new Array()\n  var optionRegions = new Array()\n  var mainOption;\n\n  optionPeers[\"noSampleSupersedes\"] = new Array(\"sampleSupersedes\")\n  optionPeers[\"sampleSupersedes\"] = new Array(\"noSampleSupersedes\")\n  optionRegions[\"noSampleSupersedes\"] = \"option1\"\n  optionRegions[\"sampleSupersedes\"] = \"option2\"\n  optionChildren[\"noSampleSupersedes\"] = new Array()\n  optionChildren[\"sampleSupersedes\"] = new Array(\n      document.getElementById(\"sampleSelector\"),\n      document.getElementById(\"browseSamples\"),\n      document.getElementById(\"supersedingNotesArea\"),\n      document.getElementById(\"supersededNotesArea\"))\n\n  selectMainOption(\"noSampleSupersedes\")\n\n  function selectMainOption(option) {\n    for (var index = 0; index < mainOptions.length; index++) {\n      var optionName = mainOptions[index]\n      var div = document.getElementById(optionRegions[optionName])\n");
          out.write("\n      if (optionName == option) {\n        mainOption = option\n        div.style.background=\"white\"\n        disableChildren(optionName, false)\n      } else {\n        div.style.background=\"silver\"\n        disableChildren(optionName, true)\n      }\n    }\n  }\n\n  function disableChildren(optionName, disabled) {\n    var children = optionChildren[optionName]\n\n    for (var childIndex = 0; childIndex < children.length; childIndex++) {\n      children[childIndex].disabled = disabled\n    }\n  }\n\n  function isBrowseEnabled() {\n    return (mainOption == \"sampleSupersedes\")\n  }\n\n  </script>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_retractWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_retractWapPage_0.doAfterBody();
          retractPage = (org.recipnet.site.content.rncontrols.RetractWapPage) _jspx_page_context.findAttribute("retractPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_retractWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_retractWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_retractWapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_retractWapPage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_retractWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_retractWapPage_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
    if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_1);
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
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("selectMainOption('noSampleSupersedes')");
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
    _jspx_th_ctl_extraHtmlAttribute_1.setValue("selectMainOption('sampleSupersedes')");
    int _jspx_eval_ctl_extraHtmlAttribute_1 = _jspx_th_ctl_extraHtmlAttribute_1.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_1);
    return false;
  }

  private boolean _jspx_meth_rn_labField_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButtonGroup_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_2 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
    int _jspx_eval_rn_labField_2 = _jspx_th_rn_labField_2.doStartTag();
    if (_jspx_th_rn_labField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_2);
    return false;
  }

  private boolean _jspx_meth_rn_labParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_a_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labParam
    org.recipnet.site.content.rncontrols.LabParam _jspx_th_rn_labParam_0 = (org.recipnet.site.content.rncontrols.LabParam) _jspx_tagPool_rn_labParam_name_nobody.get(org.recipnet.site.content.rncontrols.LabParam.class);
    _jspx_th_rn_labParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_a_0);
    _jspx_th_rn_labParam_0.setName("quickSearchByLab");
    int _jspx_eval_rn_labParam_0 = _jspx_th_rn_labParam_0.doStartTag();
    if (_jspx_th_rn_labParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labParam_name_nobody.reuse(_jspx_th_rn_labParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_radioButtonGroup_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_3 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_radioButtonGroup_0);
    int _jspx_eval_rn_labField_3 = _jspx_th_rn_labField_3.doStartTag();
    if (_jspx_th_rn_labField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_3);
    return false;
  }

  private boolean _jspx_meth_rn_sampleFieldLabel_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_7(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_7 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_7.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_7 = _jspx_th_rn_sampleField_7.doStartTag();
    if (_jspx_th_rn_sampleField_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_7);
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

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_retractWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_retractWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    div.optionDiv { border: 1px solid #32357D; padding: 0.25em; }\n  ");
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
