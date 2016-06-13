package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SampleField;
import org.recipnet.site.content.rncontrols.WapPage;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;

public final class releasetopublic_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  public String getServletInfo() {
    return "Release Sample to Public";
  }

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfNotHeldLocally;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleChecker_includeIfIneligibleForRelease;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_id_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_getDefaultCopyrightNoticeFromLabContext_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_ltaIterator;
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
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfNotHeldLocally = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleChecker_includeIfIneligibleForRelease = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_getDefaultCopyrightNoticeFromLabContext_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_ltaIterator = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldUnits_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapCancelButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_sampleChecker_includeIfNotHeldLocally.release();
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.release();
    _jspx_tagPool_rn_sampleChecker_includeIfIneligibleForRelease.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_sampleField_getDefaultCopyrightNoticeFromLabContext_fieldCode_nobody.release();
    _jspx_tagPool_rn_ltaIterator.release();
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
      //  rn:wapPage
      org.recipnet.site.content.rncontrols.WapPage _jspx_th_rn_wapPage_0 = (org.recipnet.site.content.rncontrols.WapPage) _jspx_tagPool_rn_wapPage_workflowActionCodeCorrected_workflowActionCode_title_loginPageUrl_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.WapPage.class);
      _jspx_th_rn_wapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_wapPage_0.setParent(null);
      _jspx_th_rn_wapPage_0.setTitle("Release Sample to Public");
      _jspx_th_rn_wapPage_0.setWorkflowActionCode(SampleWorkflowBL.RELEASED_TO_PUBLIC);
      _jspx_th_rn_wapPage_0.setWorkflowActionCodeCorrected(
                SampleWorkflowBL.RELEASED_TO_PUBLIC_CORRECTED);
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
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_sampleChecker_0(_jspx_th_rn_wapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_sampleChecker_1(_jspx_th_rn_wapPage_0, _jspx_page_context))
            return;
          out.write("\n  <div class=\"pageBody\">\n    <p class=\"pageInstructions\">\n      Enter any additional information about the sample that is appropriate for\n      public dissemination.  All such items are optional, but you can provide an\n      explanation for the non-technical audience, a preferred name, and a\n      copyright notice if you choose.  Click the \"Save\" button to record the\n      data and release the sample.\n      ");
          //  ctl:errorMessage
          org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
          _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
          _jspx_th_ctl_errorMessage_0.setErrorFilter(WapPage.NESTED_TAG_REPORTED_VALIDATION_ERROR);
          int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
          if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_errorMessage_0.doInitBody();
            }
            do {
              out.write("\n        <span class=\"errorMessage\"><br/>\n          You must address the flagged validation errors before the data\n          will be accepted.\n        </span>\n      ");
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
              out.write("\n      <table class=\"bodyTable\">\n        <tr>\n          <th colspan=\"2\" class=\"leadSectionHead\">General Information</th>\n        </tr>\n        <tr>\n          <th style=\"width: 12em;\">\n            ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
              out.write(":\n          </th>\n          <td>\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              _jspx_th_rn_sampleField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
              if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_sampleField_0);
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th style=\"width: 12em;\">Originating Lab:</th>\n          <td>");
              if (_jspx_meth_rn_labField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n        </tr>\n        <tr>\n          <th style=\"width: 12em;\">Originating Provider:</th>\n          <td>");
              if (_jspx_meth_rn_providerField_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("</td>\n        </tr>\n        <tr>\n          <th colspan=\"2\" class=\"sectionHead\">Additional Information</th>\n        </tr>\n        <tr>\n          <th colspan=\"2\" style=\"text-align: left;\">\n              ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleTextBL.LAYMANS_EXPLANATION);
              int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
              out.write(":\n            <div class=\"fieldNotes\">\n              Describe aspects of the sample that might be interesting to a\n              general, non-technical audience.  Use appropriately\n              non-technical language.\n            </div>\n          </th>\n        </tr>\n        <tr>\n          <td colspan=\"2\">\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_1.setFieldCode(SampleTextBL.LAYMANS_EXPLANATION);
              int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
              if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_1);
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\" style=\"text-align: left;\">\n              ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_2.setFieldCode(SampleTextBL.PREFERRED_NAME);
              int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
              out.write(":\n            <div class=\"fieldNotes\">\n              The 'preferred' sample name will be displayed most\n              prominently on the basic view of the sample, and will be the\n              first name displayed for this sample in search results.\n              If no name is selected here then each time a 'preferred name'\n              is needed one will be be chosen automatically, giving\n              preference to common names over trade names over IUPAC\n              names.  Specify a name here only if the automatic selection\n              behavior is inappropriate.\n            </div>\n          </th>\n        </tr>\n        <tr>\n          <td colspan=\"2\">\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField preferredName = null;
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_2.setId("preferredName");
              _jspx_th_rn_sampleField_2.setFieldCode(SampleTextBL.PREFERRED_NAME);
              int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
              if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              preferredName = (org.recipnet.site.content.rncontrols.SampleField) _jspx_page_context.findAttribute("preferredName");
              _jspx_tagPool_rn_sampleField_id_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_2);
              out.write("\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(SampleField.NO_SAMPLE_NAMES);
              _jspx_th_ctl_errorMessage_1.setErrorSupplier((org.recipnet.common.controls.ErrorSupplier) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${preferredName}", org.recipnet.common.controls.ErrorSupplier.class, (PageContext)_jspx_page_context, null, false));
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n              <strong>\n                There are currently no names from which to choose a preferred\n                one.\n              </strong>\n            ");
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
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <th colspan=\"2\" style=\"text-align: left; padding-top: 0.5em;\">\n              ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleFieldLabel_3.setFieldCode(SampleTextBL.COPYRIGHT_NOTICE);
              int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
              out.write(":\n            <div class=\"fieldNotes\">\n              Enter the text of any copyright notice that should be\n              recorded with this sample's information.\n            </div>\n          </th>\n        </tr>\n        <tr>\n          <td colspan=\"2\">\n            ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_getDefaultCopyrightNoticeFromLabContext_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_sampleField_3.setFieldCode(SampleTextBL.COPYRIGHT_NOTICE);
              _jspx_th_rn_sampleField_3.setGetDefaultCopyrightNoticeFromLabContext(true);
              int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
              if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_getDefaultCopyrightNoticeFromLabContext_fieldCode_nobody.reuse(_jspx_th_rn_sampleField_3);
              out.write("\n          </td>\n        </tr>\n        ");
              if (_jspx_meth_rn_ltaIterator_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        <tr>\n          <th colspan=\"2\" class=\"sectionHead\">Comments</th>\n        </tr>\n        <tr>\n          <td colspan=\"2\" style=\"text-align: center;\">\n            ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          </td>\n        </tr>\n        <tr>\n          <td colspan=\"2\" class=\"formButtons\">\n            ");
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

  private boolean _jspx_meth_rn_sampleChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_0 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfNotHeldLocally.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
    _jspx_th_rn_sampleChecker_0.setIncludeIfNotHeldLocally(true);
    int _jspx_eval_rn_sampleChecker_0 = _jspx_th_rn_sampleChecker_0.doStartTag();
    if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_0.doInitBody();
      }
      do {
        out.write("\n    ");
        if (_jspx_meth_ctl_redirect_0(_jspx_th_rn_sampleChecker_0, _jspx_page_context))
          return true;
        out.write('\n');
        out.write(' ');
        out.write(' ');
        int evalDoAfterBody = _jspx_th_rn_sampleChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_sampleChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_sampleChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfNotHeldLocally.reuse(_jspx_th_rn_sampleChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_0);
    _jspx_th_ctl_redirect_0.setTarget("/lab/nolocalholdings.jsp");
    _jspx_th_ctl_redirect_0.setPreserveParam("sampleId");
    _jspx_th_ctl_redirect_0.setPreserveParam1("sampleHistoryId");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleChecker_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_wapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleChecker
    org.recipnet.site.content.rncontrols.SampleChecker _jspx_th_rn_sampleChecker_1 = (org.recipnet.site.content.rncontrols.SampleChecker) _jspx_tagPool_rn_sampleChecker_includeIfIneligibleForRelease.get(org.recipnet.site.content.rncontrols.SampleChecker.class);
    _jspx_th_rn_sampleChecker_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_wapPage_0);
    _jspx_th_rn_sampleChecker_1.setIncludeIfIneligibleForRelease(true);
    int _jspx_eval_rn_sampleChecker_1 = _jspx_th_rn_sampleChecker_1.doStartTag();
    if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_sampleChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_sampleChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_sampleChecker_1.doInitBody();
      }
      do {
        out.write("\n    ");
        if (_jspx_meth_ctl_redirect_1(_jspx_th_rn_sampleChecker_1, _jspx_page_context))
          return true;
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
      return true;
    _jspx_tagPool_rn_sampleChecker_includeIfIneligibleForRelease.reuse(_jspx_th_rn_sampleChecker_1);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_sampleChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_1 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleChecker_1);
    _jspx_th_ctl_redirect_1.setTarget("/lab/ineligibleforrelease.jsp");
    _jspx_th_ctl_redirect_1.setPreserveParam("sampleId");
    _jspx_th_ctl_redirect_1.setPreserveParam1("sampleHistoryId");
    int _jspx_eval_ctl_redirect_1 = _jspx_th_ctl_redirect_1.doStartTag();
    if (_jspx_th_ctl_redirect_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.reuse(_jspx_th_ctl_redirect_1);
    return false;
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
        out.write("\n          <tr>\n            <th>");
        if (_jspx_meth_rn_sampleFieldLabel_4(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write(":</th>\n            <td>");
        if (_jspx_meth_rn_sampleField_4(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        if (_jspx_meth_rn_sampleFieldUnits_0(_jspx_th_rn_ltaIterator_0, _jspx_page_context))
          return true;
        out.write("</td>\n          </tr>\n        ");
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

  private boolean _jspx_meth_rn_sampleField_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_ltaIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleField
    org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_4 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
    _jspx_th_rn_sampleField_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_ltaIterator_0);
    int _jspx_eval_rn_sampleField_4 = _jspx_th_rn_sampleField_4.doStartTag();
    if (_jspx_th_rn_sampleField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleField_nobody.reuse(_jspx_th_rn_sampleField_4);
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
        out.write("\n    .fieldNotes { color: #505050; font-style: italic; white-space: normal; width: 50em; }\n  ");
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
