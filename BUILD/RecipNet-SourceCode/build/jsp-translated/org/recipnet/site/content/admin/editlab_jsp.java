package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.LabContext;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.LabPage;
import org.recipnet.site.content.rncontrols.SiteField;
import org.recipnet.site.content.rncontrols.UserContext;
import org.recipnet.site.content.rncontrols.UserField;
import org.recipnet.site.content.rncontrols.UserIterator;

public final class editlab_jsp extends org.apache.jasper.runtime.HttpJspBase
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

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labPage_title_manageLabsPageUrl_loginPageUrl_labIdParamName_labDirNameChangeConfirmationParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_siteField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_id_fieldCode_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_checkbox_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labActionButton_label_labFunction_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToActiveUsers_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_link_href_contextType;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_currentPageLinkParam_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToInactiveUsers_restrictToActiveUsers_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_labPage_title_manageLabsPageUrl_loginPageUrl_labIdParamName_labDirNameChangeConfirmationParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_siteField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_id_fieldCode_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_checkbox_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labActionButton_label_labFunction_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToActiveUsers_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_link_href_contextType = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToInactiveUsers_restrictToActiveUsers_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_labPage_title_manageLabsPageUrl_loginPageUrl_labIdParamName_labDirNameChangeConfirmationParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_siteField_fieldCode_nobody.release();
    _jspx_tagPool_rn_labField_editable_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_editable_nobody.release();
    _jspx_tagPool_rn_labField_id_fieldCode_editable_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_ctl_checkbox_id_nobody.release();
    _jspx_tagPool_rn_labActionButton_label_labFunction_nobody.release();
    _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToActiveUsers_id.release();
    _jspx_tagPool_rn_userField_nobody.release();
    _jspx_tagPool_rn_userField_fieldCode_nobody.release();
    _jspx_tagPool_rn_link_href_contextType.release();
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.release();
    _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToInactiveUsers_restrictToActiveUsers_id.release();
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.release();
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
      //  rn:labPage
      org.recipnet.site.content.rncontrols.LabPage _jspx_th_rn_labPage_0 = (org.recipnet.site.content.rncontrols.LabPage) _jspx_tagPool_rn_labPage_title_manageLabsPageUrl_loginPageUrl_labIdParamName_labDirNameChangeConfirmationParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.LabPage.class);
      _jspx_th_rn_labPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_labPage_0.setParent(null);
      _jspx_th_rn_labPage_0.setTitle("Edit Lab");
      _jspx_th_rn_labPage_0.setLabIdParamName("labId");
      _jspx_th_rn_labPage_0.setManageLabsPageUrl("/admin/managelabs.jsp");
      _jspx_th_rn_labPage_0.setLabDirNameChangeConfirmationParamName("confirmNameChange");
      _jspx_th_rn_labPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_labPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_labPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_labPage_0 = _jspx_th_rn_labPage_0.doStartTag();
      if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.LabPage htmlPage = null;
        if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_labPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_labPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.LabPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <br />\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table class=\"adminFormTable\" border=\"0\" cellspacing=\"0\">\n      <tr>\n        <th class=\"twoColumnLeft\">Lab ID:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_0.setFieldCode(LabField.ID);
              int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
              if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Home Site (ID):</th>\n        <td  class=\"twoColumnRight\">\n          ");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_0 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_siteField_0.setFieldCode(SiteField.NAME);
              int _jspx_eval_rn_siteField_0 = _jspx_th_rn_siteField_0.doStartTag();
              if (_jspx_th_rn_siteField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_0);
              out.write("\n          (");
              //  rn:siteField
              org.recipnet.site.content.rncontrols.SiteField _jspx_th_rn_siteField_1 = (org.recipnet.site.content.rncontrols.SiteField) _jspx_tagPool_rn_siteField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.SiteField.class);
              _jspx_th_rn_siteField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_siteField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_siteField_1.setFieldCode(SiteField.ID);
              int _jspx_eval_rn_siteField_1 = _jspx_th_rn_siteField_1.doStartTag();
              if (_jspx_th_rn_siteField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_siteField_fieldCode_nobody.reuse(_jspx_th_rn_siteField_1);
              out.write(")\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Lab Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              if (_jspx_meth_rn_labField_1(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Lab Short Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_2 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_2.setFieldCode(LabField.SHORT_NAME);
              _jspx_th_rn_labField_2.setEditable(true);
              int _jspx_eval_rn_labField_2 = _jspx_th_rn_labField_2.doStartTag();
              if (_jspx_th_rn_labField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_editable_nobody.reuse(_jspx_th_rn_labField_2);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Lab Home URL:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_3 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_3.setFieldCode(LabField.HOME_URL);
              _jspx_th_rn_labField_3.setEditable(true);
              int _jspx_eval_rn_labField_3 = _jspx_th_rn_labField_3.doStartTag();
              if (_jspx_th_rn_labField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_editable_nobody.reuse(_jspx_th_rn_labField_3);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Lab Directory Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField labDirName = null;
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_4 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_id_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_4.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_4.setFieldCode(LabField.DIRECTORY_NAME);
              _jspx_th_rn_labField_4.setEditable(true);
              _jspx_th_rn_labField_4.setId("labDirName");
              int _jspx_eval_rn_labField_4 = _jspx_th_rn_labField_4.doStartTag();
              if (_jspx_th_rn_labField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              labDirName = (org.recipnet.site.content.rncontrols.LabField) _jspx_page_context.findAttribute("labDirName");
              _jspx_tagPool_rn_labField_id_fieldCode_editable_nobody.reuse(_jspx_th_rn_labField_4);
              out.write("\n        </td>\n      </tr>\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(LabField.INVALID_LAB_DIRECTORY_NAME);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier(labDirName);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td class=\"twoColumnLeft\">&nbsp;</td>\n          <td class=\"twoColumnRight\">\n            <div class=\"errorMessage\">\n              <strong>The lab directory name is not valid!</strong>\n              <br />\n              (lab directory names may not contain certain special characters\n              and may not be equal to 'temp' or 'cvs')\n            </div>\n          </td>\n        </tr>\n      ");
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
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_1.setErrorFilter(
              LabPage.DIRECTORY_NAME_CHANGED_WITHOUT_CONFIRMATION);
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td class=\"twoColumnLeft\">&nbsp;</td>\n          <td class=\"twoColumnRight\">\n            <div class=\"confirmationBox\">\n              <div class=\"errorMessage\">\n                You are about to change the Directory Name for this lab.&nbsp;\n                &nbsp; This may have repercussions throughout the Reciprocal Net\n                Site Network and should be undertaken only after consultation\n                with Reciprocal Net technical support.&nbsp;&nbsp; Changing this\n                value inappropriately may lead to inaccessibility of data files\n                associated with samples originated by this lab.\n              </div>\n              <br />\n              If you wish to proceed, check the box below and click\n              'Submit Changes', otherwise hit 'Cancel' to return to the manage\n              labs page.\n              <br />\n              <br />\n              ");
                  //  ctl:checkbox
                  org.recipnet.common.controls.CheckboxHtmlControl confirmNameChange = null;
                  org.recipnet.common.controls.CheckboxHtmlControl _jspx_th_ctl_checkbox_0 = (org.recipnet.common.controls.CheckboxHtmlControl) _jspx_tagPool_ctl_checkbox_id_nobody.get(org.recipnet.common.controls.CheckboxHtmlControl.class);
                  _jspx_th_ctl_checkbox_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_checkbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                  _jspx_th_ctl_checkbox_0.setId("confirmNameChange");
                  int _jspx_eval_ctl_checkbox_0 = _jspx_th_ctl_checkbox_0.doStartTag();
                  if (_jspx_th_ctl_checkbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  confirmNameChange = (org.recipnet.common.controls.CheckboxHtmlControl) _jspx_page_context.findAttribute("confirmNameChange");
                  _jspx_tagPool_ctl_checkbox_id_nobody.reuse(_jspx_th_ctl_checkbox_0);
                  out.write("\n              Yes, I understand the consequences and wish to proceed.\n            </div>\n          </td>\n        </tr>\n      ");
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
              out.write("\n      <tr>\n        <th class=\"twoColumnLeft\">Default Copyright Notice:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labField
              org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_5 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
              _jspx_th_rn_labField_5.setPageContext(_jspx_page_context);
              _jspx_th_rn_labField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labField_5.setFieldCode(LabField.DEFAULT_COPYRIGHT_NOTICE);
              _jspx_th_rn_labField_5.setEditable(true);
              int _jspx_eval_rn_labField_5 = _jspx_th_rn_labField_5.doStartTag();
              if (_jspx_th_rn_labField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labField_fieldCode_editable_nobody.reuse(_jspx_th_rn_labField_5);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <td colspan=\"2\">&nbsp;</td>\n      </tr>\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_2 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_2.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_2.setErrorFilter( LabPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
              int _jspx_eval_ctl_errorMessage_2 = _jspx_th_ctl_errorMessage_2.doStartTag();
              if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_2.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td colspan=\"2\" class=\"oneColumn\">\n            <div class=\"errorMessage\">\n              * Missing or invalid entry.&nbsp;&nbsp;\n              Please check these entries and resubmit.\n            </div>\n          </td>\n        </tr>\n      ");
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
              out.write("\n      <tr>\n        <th class=\"twoColumnLeft\">Submit your changes: </th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:labActionButton
              org.recipnet.site.content.rncontrols.LabActionButton _jspx_th_rn_labActionButton_0 = (org.recipnet.site.content.rncontrols.LabActionButton) _jspx_tagPool_rn_labActionButton_label_labFunction_nobody.get(org.recipnet.site.content.rncontrols.LabActionButton.class);
              _jspx_th_rn_labActionButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_labActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labActionButton_0.setLabFunction(LabPage.EDIT_EXISTING_LAB);
              _jspx_th_rn_labActionButton_0.setLabel("Submit Changes");
              int _jspx_eval_rn_labActionButton_0 = _jspx_th_rn_labActionButton_0.doStartTag();
              if (_jspx_th_rn_labActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labActionButton_label_labFunction_nobody.reuse(_jspx_th_rn_labActionButton_0);
              out.write("\n          ");
              //  rn:labActionButton
              org.recipnet.site.content.rncontrols.LabActionButton _jspx_th_rn_labActionButton_1 = (org.recipnet.site.content.rncontrols.LabActionButton) _jspx_tagPool_rn_labActionButton_label_labFunction_nobody.get(org.recipnet.site.content.rncontrols.LabActionButton.class);
              _jspx_th_rn_labActionButton_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_labActionButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_labActionButton_1.setLabel("Cancel");
              _jspx_th_rn_labActionButton_1.setLabFunction(
                  LabPage.CANCEL_LAB_FUNCTION);
              int _jspx_eval_rn_labActionButton_1 = _jspx_th_rn_labActionButton_1.doStartTag();
              if (_jspx_th_rn_labActionButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_labActionButton_label_labFunction_nobody.reuse(_jspx_th_rn_labActionButton_1);
              out.write("\n        </td>\n      </tr>\n    </table>\n    <br />\n    <p class=\"headerFont1\">\n      Active user accounts associated with this lab:\n    </p>\n    <table class=\"adminFormTable\" border=\"0\" cellspacing=\"0\">\n      <tr class=\"headerColor\">\n        <th class=\"threeColumn\">User full name</th>\n        <th class=\"threeColumn\">Username</th>\n        <th>&nbsp;</th>\n      </tr>\n      ");
              //  rn:userIterator
              org.recipnet.site.content.rncontrols.UserIterator activeUsers = null;
              org.recipnet.site.content.rncontrols.UserIterator _jspx_th_rn_userIterator_0 = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToActiveUsers_id.get(org.recipnet.site.content.rncontrols.UserIterator.class);
              _jspx_th_rn_userIterator_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userIterator_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userIterator_0.setId("activeUsers");
              _jspx_th_rn_userIterator_0.setRestrictToActiveUsers(true);
              _jspx_th_rn_userIterator_0.setRestrictToLabUsers(true);
              _jspx_th_rn_userIterator_0.setSortByUsername(true);
              int _jspx_eval_rn_userIterator_0 = _jspx_th_rn_userIterator_0.doStartTag();
              if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userIterator_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userIterator_0.doInitBody();
                }
                activeUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("activeUsers");
                do {
                  out.write("\n        <tr class=\"");
                  out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${rn:testParity(activeUsers.iterationCountSinceThisPhaseBegan,\n                                   'rowColortrue', 'rowColorfalse')}", java.lang.String.class, (PageContext)_jspx_page_context, _jspx_fnmap_0, false));
                  out.write("\">\n          <td class=\"threeColumn\">\n            ");
                  if (_jspx_meth_rn_userField_0(_jspx_th_rn_userIterator_0, _jspx_page_context))
                    return;
                  out.write("\n          </td>\n          <td class=\"threeColumn\">\n            ");
                  //  rn:userField
                  org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_1 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                  _jspx_th_rn_userField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_userField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
                  _jspx_th_rn_userField_1.setFieldCode(UserField.USER_NAME);
                  int _jspx_eval_rn_userField_1 = _jspx_th_rn_userField_1.doStartTag();
                  if (_jspx_th_rn_userField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_1);
                  out.write("\n          </td>\n          <td class=\"threeColumn\">\n            ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_0 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
                  _jspx_th_rn_link_0.setHref("/admin/edituser.jsp");
                  _jspx_th_rn_link_0.setContextType(
                    UserContext.class);
                  int _jspx_eval_rn_link_0 = _jspx_th_rn_link_0.doStartTag();
                  if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_0.doInitBody();
                    }
                    do {
                      out.write("\n              ");
                      if (_jspx_meth_ctl_currentPageLinkParam_0(_jspx_th_rn_link_0, _jspx_page_context))
                        return;
                      out.write("\n              Edit this user...\n            ");
                      int evalDoAfterBody = _jspx_th_rn_link_0.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_0);
                  out.write("\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userIterator_0.doAfterBody();
                  activeUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("activeUsers");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userIterator_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userIterator_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              activeUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("activeUsers");
              _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToActiveUsers_id.reuse(_jspx_th_rn_userIterator_0);
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_3 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_3.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_3.setErrorSupplier(activeUsers);
              _jspx_th_ctl_errorMessage_3.setErrorFilter(UserIterator.NO_ITERATIONS);
              int _jspx_eval_ctl_errorMessage_3 = _jspx_th_ctl_errorMessage_3.doStartTag();
              if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_3.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td colspan=\"3\" class=\"noUsers\">\n            There are no active users associated with this lab.\n          </td>\n        </tr>\n      ");
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
              out.write("\n      <tr class=\"headerColor\">\n        <td colspan=\"3\" class=\"threeColumn\">\n          ");
              //  rn:link
              org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_1 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
              _jspx_th_rn_link_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_link_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_link_1.setHref("/admin/adduser.jsp");
              _jspx_th_rn_link_1.setContextType(
                  LabContext.class);
              int _jspx_eval_rn_link_1 = _jspx_th_rn_link_1.doStartTag();
              if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_link_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_link_1.doInitBody();
                }
                do {
                  out.write("\n            ");
                  if (_jspx_meth_ctl_currentPageLinkParam_1(_jspx_th_rn_link_1, _jspx_page_context))
                    return;
                  out.write("\n            Add a new user...\n          ");
                  int evalDoAfterBody = _jspx_th_rn_link_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_link_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_link_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_1);
              out.write("\n        </td>\n      </tr>\n    </table>\n    <br />\n    <p class=\"headerFont1\">\n      Deactivated user accounts associated with this lab:\n    </p>\n    ");
              //  rn:userIterator
              org.recipnet.site.content.rncontrols.UserIterator inactiveUsers = null;
              org.recipnet.site.content.rncontrols.UserIterator _jspx_th_rn_userIterator_1 = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToInactiveUsers_restrictToActiveUsers_id.get(org.recipnet.site.content.rncontrols.UserIterator.class);
              _jspx_th_rn_userIterator_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userIterator_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userIterator_1.setId("inactiveUsers");
              _jspx_th_rn_userIterator_1.setRestrictToActiveUsers(false);
              _jspx_th_rn_userIterator_1.setRestrictToInactiveUsers(true);
              _jspx_th_rn_userIterator_1.setRestrictToLabUsers(true);
              _jspx_th_rn_userIterator_1.setSortByUsername(true);
              int _jspx_eval_rn_userIterator_1 = _jspx_th_rn_userIterator_1.doStartTag();
              if (_jspx_eval_rn_userIterator_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userIterator_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userIterator_1.doInitBody();
                }
                inactiveUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("inactiveUsers");
                do {
                  if (_jspx_meth_ctl_parityChecker_0(_jspx_th_rn_userIterator_1, _jspx_page_context))
                    return;
                  out.write("\n      &nbsp;&nbsp;\n      ");
                  //  rn:link
                  org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_2 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
                  _jspx_th_rn_link_2.setPageContext(_jspx_page_context);
                  _jspx_th_rn_link_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_1);
                  _jspx_th_rn_link_2.setHref("/admin/deactivateduser.jsp");
                  _jspx_th_rn_link_2.setContextType(
              UserContext.class);
                  int _jspx_eval_rn_link_2 = _jspx_th_rn_link_2.doStartTag();
                  if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_rn_link_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_rn_link_2.doInitBody();
                    }
                    do {
                      out.write("\n        ");
                      if (_jspx_meth_ctl_currentPageLinkParam_2(_jspx_th_rn_link_2, _jspx_page_context))
                        return;
                      out.write("\n        ");
                      //  rn:userField
                      org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_2 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                      _jspx_th_rn_userField_2.setPageContext(_jspx_page_context);
                      _jspx_th_rn_userField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
                      _jspx_th_rn_userField_2.setFieldCode(UserField.USER_NAME);
                      int _jspx_eval_rn_userField_2 = _jspx_th_rn_userField_2.doStartTag();
                      if (_jspx_th_rn_userField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_2);
                      out.write("\n      ");
                      int evalDoAfterBody = _jspx_th_rn_link_2.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_rn_link_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_rn_link_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_2);
                  out.write("\n      <font class=\"ancillaryText\">(");
                  if (_jspx_meth_rn_userField_3(_jspx_th_rn_userIterator_1, _jspx_page_context))
                    return;
                  out.write(")</font>");
                  int evalDoAfterBody = _jspx_th_rn_userIterator_1.doAfterBody();
                  inactiveUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("inactiveUsers");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userIterator_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userIterator_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              inactiveUsers = (org.recipnet.site.content.rncontrols.UserIterator) _jspx_page_context.findAttribute("inactiveUsers");
              _jspx_tagPool_rn_userIterator_sortByUsername_restrictToLabUsers_restrictToInactiveUsers_restrictToActiveUsers_id.reuse(_jspx_th_rn_userIterator_1);
              out.write("\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_4 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_4.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_4.setErrorSupplier(inactiveUsers);
              _jspx_th_ctl_errorMessage_4.setErrorFilter(UserIterator.NO_ITERATIONS);
              int _jspx_eval_ctl_errorMessage_4 = _jspx_th_ctl_errorMessage_4.doStartTag();
              if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_4.doInitBody();
                }
                do {
                  out.write("\n      There are no deactivated users associated with this lab.\n    ");
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
              out.write('\n');
              out.write(' ');
              out.write(' ');
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
          out.write("\n  <br />\n  <br />\n  <center>\n    ");
          //  rn:link
          org.recipnet.site.content.rncontrols.ContextPreservingLink _jspx_th_rn_link_3 = (org.recipnet.site.content.rncontrols.ContextPreservingLink) _jspx_tagPool_rn_link_href_contextType.get(org.recipnet.site.content.rncontrols.ContextPreservingLink.class);
          _jspx_th_rn_link_3.setPageContext(_jspx_page_context);
          _jspx_th_rn_link_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
          _jspx_th_rn_link_3.setHref("/admin/managelabs.jsp");
          _jspx_th_rn_link_3.setContextType(
            LabContext.class);
          int _jspx_eval_rn_link_3 = _jspx_th_rn_link_3.doStartTag();
          if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_link_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_link_3.doInitBody();
            }
            do {
              out.write("Back to Lab Management");
              int evalDoAfterBody = _jspx_th_rn_link_3.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_link_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_link_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_link_href_contextType.reuse(_jspx_th_rn_link_3);
          out.write("\n  </center>\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_labPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_labPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.LabPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_labPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_labPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_labPage_title_manageLabsPageUrl_loginPageUrl_labIdParamName_labDirNameChangeConfirmationParamName_currentPageReinvocationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_labPage_0);
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

  private boolean _jspx_meth_rn_labField_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_editable_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_labField_1.setEditable(true);
    int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
    if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_editable_nobody.reuse(_jspx_th_rn_labField_1);
    return false;
  }

  private boolean _jspx_meth_rn_userField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userField
    org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
    _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_0);
    int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
    if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userField_nobody.reuse(_jspx_th_rn_userField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_currentPageLinkParam_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_0 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_0);
    _jspx_th_ctl_currentPageLinkParam_0.setName("labOrProviderPage");
    int _jspx_eval_ctl_currentPageLinkParam_0 = _jspx_th_ctl_currentPageLinkParam_0.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_0);
    return false;
  }

  private boolean _jspx_meth_ctl_currentPageLinkParam_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_1 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_1);
    _jspx_th_ctl_currentPageLinkParam_1.setName("labOrProviderPage");
    int _jspx_eval_ctl_currentPageLinkParam_1 = _jspx_th_ctl_currentPageLinkParam_1.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_1);
    return false;
  }

  private boolean _jspx_meth_ctl_parityChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:parityChecker
    org.recipnet.common.controls.ParityChecker _jspx_th_ctl_parityChecker_0 = (org.recipnet.common.controls.ParityChecker) _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.get(org.recipnet.common.controls.ParityChecker.class);
    _jspx_th_ctl_parityChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_parityChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_1);
    _jspx_th_ctl_parityChecker_0.setIncludeOnlyOnFirst(true);
    _jspx_th_ctl_parityChecker_0.setInvert(true);
    int _jspx_eval_ctl_parityChecker_0 = _jspx_th_ctl_parityChecker_0.doStartTag();
    if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_parityChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_parityChecker_0.doInitBody();
      }
      do {
        out.write(',');
        int evalDoAfterBody = _jspx_th_ctl_parityChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_parityChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_parityChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_parityChecker_invert_includeOnlyOnFirst.reuse(_jspx_th_ctl_parityChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_currentPageLinkParam_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_link_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:currentPageLinkParam
    org.recipnet.common.controls.CurrentPageLinkParam _jspx_th_ctl_currentPageLinkParam_2 = (org.recipnet.common.controls.CurrentPageLinkParam) _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.get(org.recipnet.common.controls.CurrentPageLinkParam.class);
    _jspx_th_ctl_currentPageLinkParam_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_currentPageLinkParam_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_link_2);
    _jspx_th_ctl_currentPageLinkParam_2.setName("labOrProviderPage");
    int _jspx_eval_ctl_currentPageLinkParam_2 = _jspx_th_ctl_currentPageLinkParam_2.doStartTag();
    if (_jspx_th_ctl_currentPageLinkParam_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_currentPageLinkParam_name_nobody.reuse(_jspx_th_ctl_currentPageLinkParam_2);
    return false;
  }

  private boolean _jspx_meth_rn_userField_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userIterator_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:userField
    org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_3 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
    _jspx_th_rn_userField_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_userField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userIterator_1);
    int _jspx_eval_rn_userField_3 = _jspx_th_rn_userField_3.doStartTag();
    if (_jspx_th_rn_userField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_userField_nobody.reuse(_jspx_th_rn_userField_3);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_labPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_labPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .adminFormTable { border-style: solid; border-width: thin;\n            border-color: #CCCCCC; padding: 0.01in;}\n    .ancillaryText { font-size: x-small; color: #444444; }\n    .confirmationBox { font-size: x-small; width: 300px; border-style: dashed;\n            border-width: thin; border-color: #FF0000; padding: 0.25em; }\n    .headerFont1  { font-size:medium; font-weight: bold; }\n    .rowColorfalse { background-color: #E6E6E6 }\n    .rowColortrue  { background-color: #FFFFFF }\n    .headerColor   { background-color: #CCCCCC }\n    .noUsers { text-align: center; padding: 1.5em; border-width: 0; }\n    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;\n            border-width: 0; }\n    .twoColumnLeft   { text-align: right; padding: 0.01in; border-width: 0;}\n    .twoColumnRight  { text-align: left;  padding: 0.01in;  border-width: 0;}\n    .threeColumn   { text-align: left; padding: 0.05in; border-width: 0; }\n    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }\n  ");
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
