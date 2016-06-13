package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.content.rncontrols.WapPage;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class failedtocollect_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorChecker_invert_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldUnits_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapCancelButton_nobody;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_ltaIterator_id.release();
    _jspx_tagPool_ctl_errorChecker_invert_errorFilter.release();
    _jspx_tagPool_rn_sampleFieldLabel_nobody.release();
    _jspx_tagPool_rn_sampleField_nobody.release();
    _jspx_tagPool_rn_sampleFieldUnits_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n");
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Failed to Collect");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode( SampleWorkflowBL.FAILED_TO_COLLECT );
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(
      SampleWorkflowBL.FAILED_TO_COLLECT_CORRECTED );
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
          out.write("\n  <div class=\"pageBody\">\n    <p class=\"pageInstructions\">\n      Enter any comments about why it was not possible to collect\n      sufficient data on this sample, then click the \"Save\" button.\n      ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter( WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("<br/>\n        <span class=\"errorMessage\"\n              style=\"font-weight: normal; font-style: italic;\">\n          You must address the flagged validation errors before the data\n          will be accepted.\n        </span>\n      ");
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
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n      <table class=\"bodyTable\">\n        <tr>\n          <th class=\"leadSectionHead\" colspan=\"4\">General Information</th>\n        </tr>\n        <tr>\n          <th>Laboratory:</th>\n          <td colspan=\"3\">\n            ");
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
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th>\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write(":\n          </th>\n          <td colspan=\"3\">\n            ");
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
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th class=\"multiboxLabel\">\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleTextBL.CRYSTALLOGRAPHER_NAME);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write(":\n          </th>\n          <td colspan=\"3\">\n            ");
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
                  out.write("\n              ");
                  if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_sampleField_1, _jspx_page_context))
                    return;
                  out.write("\n            ");
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
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th class=\"sectionHead\" style=\"padding-top: 0;\" colspan=\"2\">\n            Comments\n          </th>\n        </tr>\n        <tr>\n          <td align=\"center\" colspan=\"2\">\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        ");
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
                      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${ltas.currentIterationFirst ?\n              '<tr>\n                <th class=\"sectionHead\"\n                  colspan=\"2\">Additional Sample Information</th>\n              </tr>' : ''}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
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
                  out.write("\n          <tr>\n            <th>\n              ");
                  if (_jspx_meth_rn_sampleFieldLabel_2(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
                    return;
                  out.write(":\n            </th>\n            <td>\n              ");
                  if (_jspx_meth_rn_sampleField_2(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
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
              out.write("\n        <tr>\n          <td class=\"formButtons\" colspan=\"2\">\n            ");
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
          out.write("\n  </div>\n");
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

  private boolean _jspx_meth_rn_sampleFieldLabel_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleFieldLabel
    org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
    _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
    if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleFieldLabel_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
    return false;
  }

  private boolean _jspx_meth_rn_sampleField_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
    if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_2);
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
