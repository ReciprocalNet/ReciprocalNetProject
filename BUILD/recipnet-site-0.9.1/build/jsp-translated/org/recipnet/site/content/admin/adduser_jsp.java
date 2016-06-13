package org.recipnet.site.content.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.LabContext;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.ProviderContext;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.UserField;
import org.recipnet.site.content.rncontrols.UserPage;

public final class adduser_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfLabUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_labField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userChecker_includeIfProviderUser;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_providerField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userActionButton_userFunction_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfLabUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_labField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userChecker_includeIfProviderUser = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_providerField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userActionButton_userFunction_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_userChecker_includeIfLabUser.release();
    _jspx_tagPool_rn_labField_nobody.release();
    _jspx_tagPool_rn_labField_fieldCode_nobody.release();
    _jspx_tagPool_rn_userChecker_includeIfProviderUser.release();
    _jspx_tagPool_rn_providerField_nobody.release();
    _jspx_tagPool_rn_providerField_fieldCode_nobody.release();
    _jspx_tagPool_rn_userField_fieldCode_editable_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorFilter.release();
    _jspx_tagPool_rn_userField_fieldCode_nobody.release();
    _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.release();
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

      out.write("\n\n\n\n\n\n\n\n\n");
      //  rn:userPage
      org.recipnet.site.content.rncontrols.UserPage _jspx_th_rn_userPage_0 = (org.recipnet.site.content.rncontrols.UserPage) _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.UserPage.class);
      _jspx_th_rn_userPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_userPage_0.setParent(null);
      _jspx_th_rn_userPage_0.setTitle("Add User");
      _jspx_th_rn_userPage_0.setUserIdParamName("userId");
      _jspx_th_rn_userPage_0.setLabIdParamName("labId");
      _jspx_th_rn_userPage_0.setProviderIdParamName("providerId");
      _jspx_th_rn_userPage_0.setCompletionUrlParamName("labOrProviderPage");
      _jspx_th_rn_userPage_0.setCancellationUrlParamName("labOrProviderPage");
      _jspx_th_rn_userPage_0.setLoginPageUrl("/login.jsp");
      _jspx_th_rn_userPage_0.setCurrentPageReinvocationUrlParamName("origUrl");
      _jspx_th_rn_userPage_0.setAuthorizationFailedReasonParamName("authorizationFailedReason");
      int _jspx_eval_rn_userPage_0 = _jspx_th_rn_userPage_0.doStartTag();
      if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.UserPage htmlPage = null;
        if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_userPage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_userPage_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.UserPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write("\n  <br />\n  ");
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userPage_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    <table class=\"adminFormTable\">\n      ");
              //  rn:userChecker
              org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_0 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfLabUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
              _jspx_th_rn_userChecker_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userChecker_0.setIncludeIfLabUser(true);
              int _jspx_eval_rn_userChecker_0 = _jspx_th_rn_userChecker_0.doStartTag();
              if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userChecker_0.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th class=\"twoColumnLeft\">Lab Name (ID):</th>\n          <td class=\"twoColumnRight\">\n            ");
                  if (_jspx_meth_rn_labField_0(_jspx_th_rn_userChecker_0, _jspx_page_context))
                    return;
                  out.write(' ');
                  out.write('(');
                  //  rn:labField
                  org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_1 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
                  _jspx_th_rn_labField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_labField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_0);
                  _jspx_th_rn_labField_1.setFieldCode(LabField.ID);
                  int _jspx_eval_rn_labField_1 = _jspx_th_rn_labField_1.doStartTag();
                  if (_jspx_th_rn_labField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_labField_fieldCode_nobody.reuse(_jspx_th_rn_labField_1);
                  out.write(")\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userChecker_0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userChecker_includeIfLabUser.reuse(_jspx_th_rn_userChecker_0);
              out.write("\n      ");
              //  rn:userChecker
              org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_1 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfProviderUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
              _jspx_th_rn_userChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userChecker_1.setIncludeIfProviderUser(true);
              int _jspx_eval_rn_userChecker_1 = _jspx_th_rn_userChecker_1.doStartTag();
              if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userChecker_1.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th class=\"twoColumnLeft\">Provider Name (ID):</th>\n          <td class=\"twoColumnRight\">\n            ");
                  if (_jspx_meth_rn_providerField_0(_jspx_th_rn_userChecker_1, _jspx_page_context))
                    return;
                  out.write(' ');
                  out.write('(');
                  //  rn:providerField
                  org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_1 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
                  _jspx_th_rn_providerField_1.setPageContext(_jspx_page_context);
                  _jspx_th_rn_providerField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_1);
                  _jspx_th_rn_providerField_1.setFieldCode(
                    ProviderField.ID);
                  int _jspx_eval_rn_providerField_1 = _jspx_th_rn_providerField_1.doStartTag();
                  if (_jspx_th_rn_providerField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_providerField_fieldCode_nobody.reuse(_jspx_th_rn_providerField_1);
                  out.write(")\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userChecker_includeIfProviderUser.reuse(_jspx_th_rn_userChecker_1);
              out.write("\n      <tr>\n        <th class=\"twoColumnLeft\">Full Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_0.setFieldCode(UserField.FULL_NAME);
              _jspx_th_rn_userField_0.setEditable(true);
              int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
              if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_editable_nobody.reuse(_jspx_th_rn_userField_0);
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">User Name:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_1 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_1.setFieldCode(UserField.USER_NAME);
              _jspx_th_rn_userField_1.setEditable(true);
              int _jspx_eval_rn_userField_1 = _jspx_th_rn_userField_1.doStartTag();
              if (_jspx_th_rn_userField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_editable_nobody.reuse(_jspx_th_rn_userField_1);
              out.write("\n          ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(UserPage.DUPLICATE_USERNAME);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n            <div class=\"errorMessage\">\n              There already exists another user with this username!\n              <br />  Please select a new username.\n            </div>\n          ");
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
              out.write("\n        </td>\n      </tr>\n      <tr>\n        <th class=\"twoColumnLeft\">Password:</th>\n        <td class=\"twoColumnRight\">\n          ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_2 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_2.setFieldCode(UserField.PASSWORD);
              _jspx_th_rn_userField_2.setEditable(true);
              int _jspx_eval_rn_userField_2 = _jspx_th_rn_userField_2.doStartTag();
              if (_jspx_th_rn_userField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_editable_nobody.reuse(_jspx_th_rn_userField_2);
              out.write("\n        </td>\n      </tr>\n      ");
              //  rn:userChecker
              org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_2 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfLabUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
              _jspx_th_rn_userChecker_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_userChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userChecker_2.setIncludeIfLabUser(true);
              int _jspx_eval_rn_userChecker_2 = _jspx_th_rn_userChecker_2.doStartTag();
              if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userChecker_2.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th class=\"twoColumnLeft\">Global Access Level:</th>\n          <td class=\"twoColumnRight\">\n            ");
                  //  rn:userField
                  org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_3 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                  _jspx_th_rn_userField_3.setPageContext(_jspx_page_context);
                  _jspx_th_rn_userField_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_2);
                  _jspx_th_rn_userField_3.setFieldCode(UserField.SCIENTIFIC_USER_TOGGLE);
                  int _jspx_eval_rn_userField_3 = _jspx_th_rn_userField_3.doStartTag();
                  if (_jspx_th_rn_userField_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_3);
                  out.write("\n              Scientific User\n            ");
                  //  rn:userField
                  org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_4 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                  _jspx_th_rn_userField_4.setPageContext(_jspx_page_context);
                  _jspx_th_rn_userField_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_2);
                  _jspx_th_rn_userField_4.setFieldCode(UserField.LAB_ADMIN_TOGGLE);
                  int _jspx_eval_rn_userField_4 = _jspx_th_rn_userField_4.doStartTag();
                  if (_jspx_th_rn_userField_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_4);
                  out.write("\n              Lab Administrator\n            ");
                  //  rn:userField
                  org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_5 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                  _jspx_th_rn_userField_5.setPageContext(_jspx_page_context);
                  _jspx_th_rn_userField_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_2);
                  _jspx_th_rn_userField_5.setFieldCode(UserField.SITE_ADMIN_TOGGLE);
                  int _jspx_eval_rn_userField_5 = _jspx_th_rn_userField_5.doStartTag();
                  if (_jspx_th_rn_userField_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_5);
                  out.write("\n              Site Administrator\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userChecker_2.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userChecker_includeIfLabUser.reuse(_jspx_th_rn_userChecker_2);
              out.write("\n      ");
              //  rn:userChecker
              org.recipnet.site.content.rncontrols.UserChecker _jspx_th_rn_userChecker_3 = (org.recipnet.site.content.rncontrols.UserChecker) _jspx_tagPool_rn_userChecker_includeIfProviderUser.get(org.recipnet.site.content.rncontrols.UserChecker.class);
              _jspx_th_rn_userChecker_3.setPageContext(_jspx_page_context);
              _jspx_th_rn_userChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userChecker_3.setIncludeIfProviderUser(true);
              int _jspx_eval_rn_userChecker_3 = _jspx_th_rn_userChecker_3.doStartTag();
              if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_userChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_userChecker_3.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <th class=\"twoColumnLeft\">May Submit Samples:</th>\n          <td class=\"twoColumnRight\">\n            ");
                  //  rn:userField
                  org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_6 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                  _jspx_th_rn_userField_6.setPageContext(_jspx_page_context);
                  _jspx_th_rn_userField_6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_3);
                  _jspx_th_rn_userField_6.setFieldCode(UserField.SUBMITTING_PROVIDER_TOGGLE);
                  int _jspx_eval_rn_userField_6 = _jspx_th_rn_userField_6.doStartTag();
                  if (_jspx_th_rn_userField_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_rn_userField_fieldCode_nobody.reuse(_jspx_th_rn_userField_6);
                  out.write("\n            <span class=\"note\">\n              (when this box is checked, this provider user will\n              <br /> be allowed to submit new samples from his/her provider)\n            </span>\n          </td>\n        </tr>\n      ");
                  int evalDoAfterBody = _jspx_th_rn_userChecker_3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_userChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_userChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userChecker_includeIfProviderUser.reuse(_jspx_th_rn_userChecker_3);
              out.write("\n      ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_1.setErrorFilter( UserPage.NESTED_TAG_REPORTED_VALIDATION_ERROR );
              int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
              if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_1.doInitBody();
                }
                do {
                  out.write("\n        <tr>\n          <td colspan=\"2\" class=\"oneColumn\">\n            <div class=\"errorMessage\">\n              * Missing or invalid entry.&nbsp;&nbsp;\n              Please check these entries and\n              resubmit.\n            </div>\n          </td>\n        </tr>\n      ");
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
              out.write("\n      <tr>\n        <td class=\"oneColumn\" colspan=\"2\">\n          ");
              //  rn:userActionButton
              org.recipnet.site.content.rncontrols.UserActionButton _jspx_th_rn_userActionButton_0 = (org.recipnet.site.content.rncontrols.UserActionButton) _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.get(org.recipnet.site.content.rncontrols.UserActionButton.class);
              _jspx_th_rn_userActionButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userActionButton_0.setLabel("Add User");
              _jspx_th_rn_userActionButton_0.setUserFunction(UserPage.UserFunction.ADD_USER);
              int _jspx_eval_rn_userActionButton_0 = _jspx_th_rn_userActionButton_0.doStartTag();
              if (_jspx_th_rn_userActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.reuse(_jspx_th_rn_userActionButton_0);
              out.write("\n          ");
              //  rn:userActionButton
              org.recipnet.site.content.rncontrols.UserActionButton _jspx_th_rn_userActionButton_1 = (org.recipnet.site.content.rncontrols.UserActionButton) _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.get(org.recipnet.site.content.rncontrols.UserActionButton.class);
              _jspx_th_rn_userActionButton_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userActionButton_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userActionButton_1.setUserFunction(UserPage.UserFunction.CANCEL);
              _jspx_th_rn_userActionButton_1.setLabel("Cancel");
              int _jspx_eval_rn_userActionButton_1 = _jspx_th_rn_userActionButton_1.doStartTag();
              if (_jspx_th_rn_userActionButton_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.reuse(_jspx_th_rn_userActionButton_1);
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
          out.write("\n  <br />\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_userPage_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_userPage_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.UserPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_userPage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_userPage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.reuse(_jspx_th_rn_userPage_0);
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

  private boolean _jspx_meth_rn_labField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:labField
    org.recipnet.site.content.rncontrols.LabField _jspx_th_rn_labField_0 = (org.recipnet.site.content.rncontrols.LabField) _jspx_tagPool_rn_labField_nobody.get(org.recipnet.site.content.rncontrols.LabField.class);
    _jspx_th_rn_labField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_labField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_0);
    int _jspx_eval_rn_labField_0 = _jspx_th_rn_labField_0.doStartTag();
    if (_jspx_th_rn_labField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_labField_nobody.reuse(_jspx_th_rn_labField_0);
    return false;
  }

  private boolean _jspx_meth_rn_providerField_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:providerField
    org.recipnet.site.content.rncontrols.ProviderField _jspx_th_rn_providerField_0 = (org.recipnet.site.content.rncontrols.ProviderField) _jspx_tagPool_rn_providerField_nobody.get(org.recipnet.site.content.rncontrols.ProviderField.class);
    _jspx_th_rn_providerField_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_providerField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userChecker_1);
    int _jspx_eval_rn_providerField_0 = _jspx_th_rn_providerField_0.doStartTag();
    if (_jspx_th_rn_providerField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_providerField_nobody.reuse(_jspx_th_rn_providerField_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_userPage_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_userPage_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessage  { color: red; font-weight: normal; font-size: x-small; }\n    .adminFormTable { border-style: solid; border-width: thin;\n            border-color: #CCCCCC; padding: 0.01in; }\n    .twoColumnLeft   { text-align: right; vertical-align: top; padding: 0.01in;\n            border-width: 0; }\n    .twoColumnRight  { text-align: left; vertical-align: top;  padding: 0.01in;\n            border-width: 0; }\n    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;\n            border-width: 0; }\n    .note { color: #909090; font-style: italic; font-size: x-small; }\n  ");
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
