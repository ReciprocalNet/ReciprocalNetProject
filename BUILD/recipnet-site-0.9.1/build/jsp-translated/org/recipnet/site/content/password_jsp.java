package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.LabField;
import org.recipnet.site.content.rncontrols.ProviderField;
import org.recipnet.site.content.rncontrols.UserContext;
import org.recipnet.site.content.rncontrols.UserField;
import org.recipnet.site.content.rncontrols.UserPage;

public final class password_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textbox_size_required_maxLength_maskInput_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_redundantPassword_id_fieldCode_editable_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userActionButton_userFunction_label_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textbox_size_required_maxLength_maskInput_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_redundantPassword_id_fieldCode_editable_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userActionButton_userFunction_label_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_ctl_textbox_size_required_maxLength_maskInput_id_nobody.release();
    _jspx_tagPool_rn_userField_redundantPassword_id_fieldCode_editable_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
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

      out.write("\n\n\n\n\n\n\n\n");
      //  rn:userPage
      org.recipnet.site.content.rncontrols.UserPage _jspx_th_rn_userPage_0 = (org.recipnet.site.content.rncontrols.UserPage) _jspx_tagPool_rn_userPage_userIdParamName_title_providerIdParamName_loginPageUrl_labIdParamName_currentPageReinvocationUrlParamName_completionUrlParamName_cancellationUrlParamName_authorizationFailedReasonParamName.get(org.recipnet.site.content.rncontrols.UserPage.class);
      _jspx_th_rn_userPage_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_userPage_0.setParent(null);
      _jspx_th_rn_userPage_0.setTitle("Change User Password");
      _jspx_th_rn_userPage_0.setUserIdParamName("userId");
      _jspx_th_rn_userPage_0.setLabIdParamName("labId");
      _jspx_th_rn_userPage_0.setProviderIdParamName("providerId");
      _jspx_th_rn_userPage_0.setCompletionUrlParamName("editUserPage");
      _jspx_th_rn_userPage_0.setCancellationUrlParamName("editUserPage");
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
          out.write("\n <br />\n ");
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
              out.write("\n   <table class=\"adminFormTable\">\n    <tr>\n      <th class=\"twoColumnLeft\">Full Name:</th>\n      <td class=\"twoColumnRight\">\n        ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_0.setFieldCode(UserField.FULL_NAME);
              _jspx_th_rn_userField_0.setDisplayAsLabel(true);
              int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
              if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_userField_0);
              out.write("\n      </td>\n    </tr>\n    <tr>\n      <th class=\"twoColumnLeft\">User Name:</th>\n      <td class=\"twoColumnRight\">\n        ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_1 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_1.setFieldCode(UserField.USER_NAME);
              _jspx_th_rn_userField_1.setDisplayAsLabel(true);
              int _jspx_eval_rn_userField_1 = _jspx_th_rn_userField_1.doStartTag();
              if (_jspx_th_rn_userField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_userField_1);
              out.write("\n      </td>\n    </tr>\n    <tr>\n      <td colspan=\"2\">\n        <hr size=\"1\">\n      </td>\n    </tr>\n    <tr>\n      <th colspan=\"2\">\n        Reset Password:<br />\n        <font class=\"comment\">\n          Passwords are limited to 8 characters maximum.<br />&nbsp;\n        </font>\n      </th> \n    </tr>\n    <tr>\n      <td class=\"twoColumnLeft\"> New password: </td>\n      <td class=\"twoColumnRight\">\n        ");
              //  ctl:textbox
              org.recipnet.common.controls.TextboxHtmlControl password1 = null;
              org.recipnet.common.controls.TextboxHtmlControl _jspx_th_ctl_textbox_0 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_tagPool_ctl_textbox_size_required_maxLength_maskInput_id_nobody.get(org.recipnet.common.controls.TextboxHtmlControl.class);
              _jspx_th_ctl_textbox_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_textbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_textbox_0.setId("password1");
              _jspx_th_ctl_textbox_0.setMaskInput(true);
              _jspx_th_ctl_textbox_0.setMaxLength(8);
              _jspx_th_ctl_textbox_0.setSize(8);
              _jspx_th_ctl_textbox_0.setRequired(true);
              int _jspx_eval_ctl_textbox_0 = _jspx_th_ctl_textbox_0.doStartTag();
              if (_jspx_th_ctl_textbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              password1 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("password1");
              _jspx_tagPool_ctl_textbox_size_required_maxLength_maskInput_id_nobody.reuse(_jspx_th_ctl_textbox_0);
              out.write("\n      </td>\n    </tr>\n    <tr>\n      <td class=\"twoColumnLeft\"> Confirm new password: </td>\n      <td class=\"twoColumnRight\">\n        ");
              //  rn:userField
              org.recipnet.site.content.rncontrols.UserField password2 = null;
              org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_2 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_redundantPassword_id_fieldCode_editable_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
              _jspx_th_rn_userField_2.setPageContext(_jspx_page_context);
              _jspx_th_rn_userField_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userField_2.setId("password2");
              _jspx_th_rn_userField_2.setFieldCode(UserField.PASSWORD);
              _jspx_th_rn_userField_2.setRedundantPassword(password1);
              _jspx_th_rn_userField_2.setEditable(true);
              int _jspx_eval_rn_userField_2 = _jspx_th_rn_userField_2.doStartTag();
              if (_jspx_th_rn_userField_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              password2 = (org.recipnet.site.content.rncontrols.UserField) _jspx_page_context.findAttribute("password2");
              _jspx_tagPool_rn_userField_redundantPassword_id_fieldCode_editable_nobody.reuse(_jspx_th_rn_userField_2);
              out.write("\n      </td>\n    </tr>\n    <tr>\n      <td colspan=\"2\">&nbsp;</td>\n    </tr>\n    ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier(password2);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(UserField.PASSWORD_DOES_NOT_MATCH);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n      <tr>\n        <td class=\"oneColumn\" colspan=\"2\">\n          <div class=\"unmatchedPasswords\">\n            The passwords entered were invalid or did not match.<br />\n            Please carefully retype the new password.\n          </div>\n        </td>\n      </tr>\n    ");
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
              out.write("\n    <tr>\n      <td class=\"oneColumn\" colspan=\"2\">\n        ");
              //  rn:userActionButton
              org.recipnet.site.content.rncontrols.UserActionButton _jspx_th_rn_userActionButton_0 = (org.recipnet.site.content.rncontrols.UserActionButton) _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.get(org.recipnet.site.content.rncontrols.UserActionButton.class);
              _jspx_th_rn_userActionButton_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_userActionButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              _jspx_th_rn_userActionButton_0.setUserFunction(UserPage.UserFunction.CHANGE_USER_PASSWORD);
              _jspx_th_rn_userActionButton_0.setLabel("Change Password");
              int _jspx_eval_rn_userActionButton_0 = _jspx_th_rn_userActionButton_0.doStartTag();
              if (_jspx_th_rn_userActionButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_userActionButton_userFunction_label_nobody.reuse(_jspx_th_rn_userActionButton_0);
              out.write("\n        ");
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
              out.write("\n      </td>\n    </table>\n  ");
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
        out.write("\n    .unmatchedPasswords  { color: red; font-weight: bold; font-size: x-small; }\n    .adminFormTable { border-style: solid; border-width: thin;\n            border-color: #CCCCCC; padding: 0.01in; }\n    .twoColumnLeft   { text-align: right; vertical-align: top; padding: 0.01in;\n            border-width: 0; width: 45%; }\n    .twoColumnRight  { text-align: left; vertical-align: top;  padding: 0.01in;\n            border-width: 0; width: 55%; }\n    .oneColumn { text-align: center; vertical-align: center; padding: 0.01in;\n            border-width: 0; }\n    .comment { font-family: sans-serif; font-size: x-small; color: #9E9E9E;\n            text-align: center; }\n  ");
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
