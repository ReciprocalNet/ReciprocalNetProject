package org.recipnet.site.content.lab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.SampleHistoryField;
import org.recipnet.site.shared.db.SampleInfo;

public final class revert_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_includeDataFilesParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_sampleHistoryParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_specificSampleVersion_sampleIdParamName_sampleHistoryIdParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_checkbox_initialValue_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapComments_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_wapSaveButton_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_buttonLink_target_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_includeDataFilesParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_specificSampleVersion_sampleIdParamName_sampleHistoryIdParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_checkbox_initialValue_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapComments_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_wapSaveButton_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_buttonLink_target_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_includeDataFilesParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.release();
    _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext.release();
    _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.release();
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl.release();
    _jspx_tagPool_rn_sampleParam_name_nobody.release();
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.release();
    _jspx_tagPool_rn_specificSampleVersion_sampleIdParamName_sampleHistoryIdParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_checkbox_initialValue_id_nobody.release();
    _jspx_tagPool_rn_wapComments_nobody.release();
    _jspx_tagPool_rn_wapSaveButton_nobody.release();
    _jspx_tagPool_rn_buttonLink_target_label_nobody.release();
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

      out.write("\n\n\n\n\n");
      //  rn:revertWapPage
      org.recipnet.site.content.rncontrols.RevertWapPage _jspx_th_rn_revertWapPage_0 = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_includeDataFilesParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.RevertWapPage.class);
      _jspx_th_rn_revertWapPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_revertWapPage_0.setParent(null);
      _jspx_th_rn_revertWapPage_0.setTitle("Revert sample");
      _jspx_th_rn_revertWapPage_0.setIncludeDataFilesParamName("includeFiles");
      _jspx_th_rn_revertWapPage_0.setEditSamplePageHref("/lab/sample.jsp");
      _jspx_th_rn_revertWapPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_revertWapPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_revertWapPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_revertWapPage_0 = _jspx_th_rn_revertWapPage_0.doStartTag();
      if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RevertWapPage htmlPage = null;
        if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_revertWapPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_revertWapPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <p class=\"pageInstructions\">\n    Please enter any comments and press 'Confirm' to revert to an\n    older version, or 'Cancel' to return to view the Sample History.\n  </p>\n  <table align=\"center\" width=\"80%\" class=\"searchTable\">\n    <tr>\n      <td align=\"center\">\n        <strong>Current sample version:</strong>\n        <br />\n        <table class=\"searchTable\">\n          <tr>\n            <td align=\"right\">\n              <strong>\n                ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_0 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_sampleFieldLabel_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleFieldLabel_0 = _jspx_th_rn_sampleFieldLabel_0.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_0);
          out.write(":\n              </strong>\n            </td>\n            <td align=\"left\">\n              ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_0 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_sampleField_0.setDisplayValueOnly(true);
          _jspx_th_rn_sampleField_0.setFieldCode(SampleInfo.LOCAL_LAB_ID);
          int _jspx_eval_rn_sampleField_0 = _jspx_th_rn_sampleField_0.doStartTag();
          if (_jspx_th_rn_sampleField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_0);
          out.write("\n            </td>\n          </tr>\n          <tr>\n            <td align=\"right\"><strong>Lab:</strong></td>\n            <td align=\"left\">\n              ");
          if (_jspx_meth_rn_labField_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n            </td>\n          </tr>\n          <tr>\n            <td align=\"right\"><strong>Provider:</strong></td>\n            <td align=\"left\">\n              ");
          if (_jspx_meth_rn_providerField_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n            </td>\n          </tr>\n          <tr>\n            <td align=\"right\">\n              <strong>\n                ");
          //  rn:sampleFieldLabel
          org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_1 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
          _jspx_th_rn_sampleFieldLabel_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleFieldLabel_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_sampleFieldLabel_1.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleFieldLabel_1 = _jspx_th_rn_sampleFieldLabel_1.doStartTag();
          if (_jspx_th_rn_sampleFieldLabel_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_1);
          out.write(":\n              </strong>\n            </td>\n            <td align=\"left\">\n              ");
          //  rn:sampleField
          org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_1 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
          _jspx_th_rn_sampleField_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_sampleField_1.setDisplayValueOnly(true);
          _jspx_th_rn_sampleField_1.setFieldCode(SampleInfo.STATUS);
          int _jspx_eval_rn_sampleField_1 = _jspx_th_rn_sampleField_1.doStartTag();
          if (_jspx_th_rn_sampleField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_1);
          out.write("\n            </td>\n          </tr>\n          <tr>\n            <td align=\"right\"><strong>Last action date:</strong></td>\n            <td align=\"left\">\n              ");
          //  rn:sampleHistoryTranslator
          org.recipnet.site.content.rncontrols.SampleHistoryTranslator _jspx_th_rn_sampleHistoryTranslator_0 = (org.recipnet.site.content.rncontrols.SampleHistoryTranslator) _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext.get(org.recipnet.site.content.rncontrols.SampleHistoryTranslator.class);
          _jspx_th_rn_sampleHistoryTranslator_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_sampleHistoryTranslator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_sampleHistoryTranslator_0.setTranslateFromSampleContext(true);
          int _jspx_eval_rn_sampleHistoryTranslator_0 = _jspx_th_rn_sampleHistoryTranslator_0.doStartTag();
          if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_sampleHistoryTranslator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_sampleHistoryTranslator_0.doInitBody();
            }
            do {
              out.write("\n                ");
              //  rn:sampleHistoryField
              org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_0 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
              _jspx_th_rn_sampleHistoryField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryTranslator_0);
              _jspx_th_rn_sampleHistoryField_0.setFieldCode(
                        SampleHistoryField.FieldCode.ACTION_DATE);
              int _jspx_eval_rn_sampleHistoryField_0 = _jspx_th_rn_sampleHistoryField_0.doStartTag();
              if (_jspx_th_rn_sampleHistoryField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_0);
              out.write("\n              ");
              int evalDoAfterBody = _jspx_th_rn_sampleHistoryTranslator_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_sampleHistoryTranslator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_sampleHistoryTranslator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext.reuse(_jspx_th_rn_sampleHistoryTranslator_0);
          out.write("\n            </td>\n          </tr>\n          <tr>\n            <td align=\"center\" colspan=\"2\">\n              ");
          if (_jspx_meth_rn_showsampleLink_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write("\n            </td>\n          </tr>\n        </table>\n      </td>\n      <td align=\"center\">\n        <i>reverting to</i> <br />\n        <img src=\"../images/revert-arrow.gif\">\n      </td>\n      <td align=\"center\">\n        <strong>Previous sample version:</strong>\n        <br />\n        ");
          //  rn:specificSampleVersion
          org.recipnet.site.content.rncontrols.SpecificSampleVersion _jspx_th_rn_specificSampleVersion_0 = (org.recipnet.site.content.rncontrols.SpecificSampleVersion) _jspx_tagPool_rn_specificSampleVersion_sampleIdParamName_sampleHistoryIdParamName.get(org.recipnet.site.content.rncontrols.SpecificSampleVersion.class);
          _jspx_th_rn_specificSampleVersion_0.setPageContext(_jspx_page_context);
          _jspx_th_rn_specificSampleVersion_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          _jspx_th_rn_specificSampleVersion_0.setSampleIdParamName("sampleId");
          _jspx_th_rn_specificSampleVersion_0.setSampleHistoryIdParamName("targetSampleHistoryId");
          int _jspx_eval_rn_specificSampleVersion_0 = _jspx_th_rn_specificSampleVersion_0.doStartTag();
          if (_jspx_eval_rn_specificSampleVersion_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_specificSampleVersion_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_specificSampleVersion_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_specificSampleVersion_0.doInitBody();
            }
            do {
              out.write("\n          <table class=\"searchTable\">\n            <tr>\n              <td align=\"right\">\n                <strong>\n                  ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_2 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
              _jspx_th_rn_sampleFieldLabel_2.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleFieldLabel_2 = _jspx_th_rn_sampleFieldLabel_2.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_2);
              out.write(":\n                </strong>\n              </td>\n              <td align=\"left\">\n                ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_2 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
              _jspx_th_rn_sampleField_2.setDisplayValueOnly(true);
              _jspx_th_rn_sampleField_2.setFieldCode(SampleInfo.LOCAL_LAB_ID);
              int _jspx_eval_rn_sampleField_2 = _jspx_th_rn_sampleField_2.doStartTag();
              if (_jspx_th_rn_sampleField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_2);
              out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"right\"><strong>Lab:</strong></td>\n              <td align=\"left\">\n                ");
              if (_jspx_meth_rn_labField_1(_jspx_th_rn_specificSampleVersion_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"right\"><strong>Provider:</strong></td>\n              <td align=\"left\">\n                ");
              if (_jspx_meth_rn_providerField_1(_jspx_th_rn_specificSampleVersion_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"right\">\n                <strong>\n                  ");
              //  rn:sampleFieldLabel
              org.recipnet.site.content.rncontrols.SampleFieldLabel _jspx_th_rn_sampleFieldLabel_3 = (org.recipnet.site.content.rncontrols.SampleFieldLabel) _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleFieldLabel.class);
              _jspx_th_rn_sampleFieldLabel_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleFieldLabel_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
              _jspx_th_rn_sampleFieldLabel_3.setFieldCode(SampleInfo.STATUS);
              int _jspx_eval_rn_sampleFieldLabel_3 = _jspx_th_rn_sampleFieldLabel_3.doStartTag();
              if (_jspx_th_rn_sampleFieldLabel_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleFieldLabel_fieldCode_nobody.reuse(_jspx_th_rn_sampleFieldLabel_3);
              out.write(":\n                </strong>\n              </td>\n              <td align=\"left\">\n                ");
              //  rn:sampleField
              org.recipnet.site.content.rncontrols.SampleField _jspx_th_rn_sampleField_3 = (org.recipnet.site.content.rncontrols.SampleField) _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.get(org.recipnet.site.content.rncontrols.SampleField.class);
              _jspx_th_rn_sampleField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
              _jspx_th_rn_sampleField_3.setDisplayValueOnly(true);
              _jspx_th_rn_sampleField_3.setFieldCode(SampleInfo.STATUS);
              int _jspx_eval_rn_sampleField_3 = _jspx_th_rn_sampleField_3.doStartTag();
              if (_jspx_th_rn_sampleField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleField_fieldCode_displayValueOnly_nobody.reuse(_jspx_th_rn_sampleField_3);
              out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"right\"><strong>Last action date:</strong></td>\n              <td align=\"left\">\n                ");
              //  rn:sampleHistoryTranslator
              org.recipnet.site.content.rncontrols.SampleHistoryTranslator _jspx_th_rn_sampleHistoryTranslator_1 = (org.recipnet.site.content.rncontrols.SampleHistoryTranslator) _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext.get(org.recipnet.site.content.rncontrols.SampleHistoryTranslator.class);
              _jspx_th_rn_sampleHistoryTranslator_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_sampleHistoryTranslator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
              _jspx_th_rn_sampleHistoryTranslator_1.setTranslateFromSampleContext(true);
              int _jspx_eval_rn_sampleHistoryTranslator_1 = _jspx_th_rn_sampleHistoryTranslator_1.doStartTag();
              if (_jspx_eval_rn_sampleHistoryTranslator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_sampleHistoryTranslator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_sampleHistoryTranslator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_sampleHistoryTranslator_1.doInitBody();
                }
                do {
                  out.write("\n                  ");
                  //  rn:sampleHistoryField
                  org.recipnet.site.content.rncontrols.SampleHistoryField _jspx_th_rn_sampleHistoryField_1 = (org.recipnet.site.content.rncontrols.SampleHistoryField) _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryField.class);
                  _jspx_th_rn_sampleHistoryField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_sampleHistoryField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_sampleHistoryTranslator_1);
                  _jspx_th_rn_sampleHistoryField_1.setFieldCode(
                          SampleHistoryField.FieldCode.ACTION_DATE);
                  int _jspx_eval_rn_sampleHistoryField_1 = _jspx_th_rn_sampleHistoryField_1.doStartTag();
                  if (_jspx_th_rn_sampleHistoryField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_sampleHistoryField_fieldCode_nobody.reuse(_jspx_th_rn_sampleHistoryField_1);
                  out.write("\n                ");
                  int evalDoAfterBody = _jspx_th_rn_sampleHistoryTranslator_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_sampleHistoryTranslator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_sampleHistoryTranslator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_sampleHistoryTranslator_translateFromSampleContext.reuse(_jspx_th_rn_sampleHistoryTranslator_1);
              out.write("\n              </td>\n            </tr>\n            <tr>\n              <td align=\"center\" colspan=\"2\">\n              ");
              if (_jspx_meth_rn_showsampleLink_1(_jspx_th_rn_specificSampleVersion_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n            </tr>\n          </table>\n        ");
              int evalDoAfterBody = _jspx_th_rn_specificSampleVersion_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_specificSampleVersion_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_specificSampleVersion_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_specificSampleVersion_sampleIdParamName_sampleHistoryIdParamName.reuse(_jspx_th_rn_specificSampleVersion_0);
          out.write("\n      </td>\n    </tr>\n    <tr>\n      <td align=\"center\" colspan=\"3\">\n        ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n          <br />\n          <p>\n            <table>\n              <tr>\n                <td align=\"right\" valign=\"center\">\n                  ");
              //  ctl:checkbox
              org.recipnet.common.controls.CheckboxHtmlControl includeFiles = null;
              org.recipnet.common.controls.CheckboxHtmlControl _jspx_th_ctl_checkbox_0 = (org.recipnet.common.controls.CheckboxHtmlControl) _jspx_tagPool_ctl_checkbox_initialValue_id_nobody.get(org.recipnet.common.controls.CheckboxHtmlControl.class);
              _jspx_th_ctl_checkbox_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_checkbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_checkbox_0.setId("includeFiles");
              _jspx_th_ctl_checkbox_0.setInitialValue(Boolean.TRUE);
              int _jspx_eval_ctl_checkbox_0 = _jspx_th_ctl_checkbox_0.doStartTag();
              if (_jspx_th_ctl_checkbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              includeFiles = (org.recipnet.common.controls.CheckboxHtmlControl) _jspx_page_context.findAttribute("includeFiles");
              _jspx_tagPool_ctl_checkbox_initialValue_id_nobody.reuse(_jspx_th_ctl_checkbox_0);
              out.write("\n                </td>\n                <td align=\"left\" valign=\"center\">\n                  - Revert data files&nbsp;\n                </td>\n              </tr>\n            </table>\n            <table width=\"50%\">\n              <tr>\n                <td>\n                  <font class=\"light\">\n                    If you choose to revert data files as well as sample\n                    data, the repository files will be replaced by those\n                    from the version to which you are reverting.  If you\n                    choose not to include data files in this reversion,\n                    the files will remain unchanged.\n                  </font>\n                </td>\n              </tr>\n            </table>\n          </p>\n          <br />\n          Comments: (optional)\n          <br />\n          ");
              if (_jspx_meth_rn_wapComments_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n          <table>\n            <tr>\n              <td>\n                ");
              if (_jspx_meth_rn_wapSaveButton_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n              <td>\n                ");
              if (_jspx_meth_rn_buttonLink_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n              </td>\n            </tr>\n          </table>\n        ");
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
          out.write("\n      </td>\n    </tr>\n  </table>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_revertWapPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_revertWapPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.RevertWapPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_revertWapPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_revertWapPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_revertWapPage_title_loginPageUrl_includeDataFilesParamName_editSamplePageHref_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_revertWapPage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_rn_showsampleLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_0 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    _jspx_th_rn_showsampleLink_0.setOpenInWindow("old");
    _jspx_th_rn_showsampleLink_0.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_0.setDetailedPageUrl("/showsampledetailed.jsp");
    _jspx_th_rn_showsampleLink_0.setSampleIsKnownToBeLocal(true);
    int _jspx_eval_rn_showsampleLink_0 = _jspx_th_rn_showsampleLink_0.doStartTag();
    if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_0.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_sampleParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_0(_jspx_th_rn_showsampleLink_0, _jspx_page_context))
          return true;
        out.write("\n                View this version...\n              ");
        int evalDoAfterBody = _jspx_th_rn_showsampleLink_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_showsampleLink_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_showsampleLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_0 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleParam_0.setName("sampleId");
    int _jspx_eval_rn_sampleParam_0 = _jspx_th_rn_sampleParam_0.doStartTag();
    if (_jspx_th_rn_sampleParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_0 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_0);
    _jspx_th_rn_sampleHistoryParam_0.setName("sampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_0 = _jspx_th_rn_sampleHistoryParam_0.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_0);
    return false;
  }

  private boolean _jspx_meth_rn_labField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_specificSampleVersion_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
    int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
    if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_1);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_specificSampleVersion_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_1 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
    int _jspx_eval_rn_providerField_1 = _jspx_th_rn_providerField_1.doStartTag();
    if (_jspx_th_rn_providerField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_1);
    return false;
  }

  private boolean _jspx_meth_rn_showsampleLink_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_specificSampleVersion_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:showsampleLink
    org.recipnet.site.content.rncontrols.ShowsampleLink _jspx_th_rn_showsampleLink_1 = (org.recipnet.site.content.rncontrols.ShowsampleLink) _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl.get(org.recipnet.site.content.rncontrols.ShowsampleLink.class);
    _jspx_th_rn_showsampleLink_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_showsampleLink_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_specificSampleVersion_0);
    _jspx_th_rn_showsampleLink_1.setOpenInWindow("new");
    _jspx_th_rn_showsampleLink_1.setBasicPageUrl("/showsamplebasic.jsp");
    _jspx_th_rn_showsampleLink_1.setDetailedPageUrl("/showsampledetailed.jsp");
    _jspx_th_rn_showsampleLink_1.setSampleIsKnownToBeLocal(true);
    int _jspx_eval_rn_showsampleLink_1 = _jspx_th_rn_showsampleLink_1.doStartTag();
    if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_showsampleLink_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_showsampleLink_1.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_sampleParam_1(_jspx_th_rn_showsampleLink_1, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_rn_sampleHistoryParam_1(_jspx_th_rn_showsampleLink_1, _jspx_page_context))
          return true;
        out.write("\n                View this version...\n              ");
        int evalDoAfterBody = _jspx_th_rn_showsampleLink_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_showsampleLink_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_showsampleLink_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_showsampleLink_sampleIsKnownToBeLocal_openInWindow_detailedPageUrl_basicPageUrl.reuse(_jspx_th_rn_showsampleLink_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleParam
    org.recipnet.site.content.rncontrols.SampleParam _jspx_th_rn_sampleParam_1 = (org.recipnet.site.content.rncontrols.SampleParam) _jspx_tagPool_rn_sampleParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleParam.class);
    _jspx_th_rn_sampleParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
    _jspx_th_rn_sampleParam_1.setName("sampleId");
    int _jspx_eval_rn_sampleParam_1 = _jspx_th_rn_sampleParam_1.doStartTag();
    if (_jspx_th_rn_sampleParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleParam_name_nobody.reuse(_jspx_th_rn_sampleParam_1);
    return false;
  }

  private boolean _jspx_meth_rn_sampleHistoryParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_showsampleLink_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:sampleHistoryParam
    org.recipnet.site.content.rncontrols.SampleHistoryParam _jspx_th_rn_sampleHistoryParam_1 = (org.recipnet.site.content.rncontrols.SampleHistoryParam) _jspx_tagPool_rn_sampleHistoryParam_name_nobody.get(org.recipnet.site.content.rncontrols.SampleHistoryParam.class);
    _jspx_th_rn_sampleHistoryParam_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_sampleHistoryParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_showsampleLink_1);
    _jspx_th_rn_sampleHistoryParam_1.setName("sampleHistoryId");
    int _jspx_eval_rn_sampleHistoryParam_1 = _jspx_th_rn_sampleHistoryParam_1.doStartTag();
    if (_jspx_th_rn_sampleHistoryParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_sampleHistoryParam_name_nobody.reuse(_jspx_th_rn_sampleHistoryParam_1);
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

  private boolean _jspx_meth_rn_buttonLink_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:buttonLink
    org.recipnet.site.content.rncontrols.ContextPreservingButton _jspx_th_rn_buttonLink_0 = (org.recipnet.site.content.rncontrols.ContextPreservingButton) _jspx_tagPool_rn_buttonLink_target_label_nobody.get(org.recipnet.site.content.rncontrols.ContextPreservingButton.class);
    _jspx_th_rn_buttonLink_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_buttonLink_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_buttonLink_0.setTarget("/lab/samplehistory.jsp");
    _jspx_th_rn_buttonLink_0.setLabel("Cancel");
    int _jspx_eval_rn_buttonLink_0 = _jspx_th_rn_buttonLink_0.doStartTag();
    if (_jspx_th_rn_buttonLink_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_buttonLink_target_label_nobody.reuse(_jspx_th_rn_buttonLink_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_revertWapPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_revertWapPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .navLinksRight { text-align: right; }\n    .pageInstructions { font-weight: bold; font-style: normal;\n        font-size: medium; text-decoration: none; }\n    .searchTable  { border-width: thin; border-style: solid;\n        border-color: #CCCCCC; }\n    .searchTableHeader { text-align: left; background-color: #CCCCCC; }\n    .searchTableSubHeader { background-color: #EBEBEB; }\n    .searchTableColumnWidth { width: 25%; }\n    font.light { font-family: Arial, Helvetica, Verdana; font-style: narrow; \n        color: #A0A0A0; }\n  ");
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
