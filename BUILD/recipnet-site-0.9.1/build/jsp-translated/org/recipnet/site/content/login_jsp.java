package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textbox_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_textbox_maxLength_maskInput_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_hiddenString_initialValueFrom_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_loginButton_username_targetPageParamName_targetPageIfCanSeeLabSummary_targetPage_password_id_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textbox_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_textbox_maxLength_maskInput_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_hiddenString_initialValueFrom_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_loginButton_username_targetPageParamName_targetPageIfCanSeeLabSummary_targetPage_password_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_rn_authorizationChecker.release();
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.release();
    _jspx_tagPool_rn_authorizationChecker_invert.release();
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.release();
    _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_ctl_textbox_id_nobody.release();
    _jspx_tagPool_ctl_textbox_maxLength_maskInput_id_nobody.release();
    _jspx_tagPool_ctl_hiddenString_initialValueFrom_id_nobody.release();
    _jspx_tagPool_rn_loginButton_username_targetPageParamName_targetPageIfCanSeeLabSummary_targetPage_password_id_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier.release();
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
      //  rn:page
      org.recipnet.site.content.rncontrols.RecipnetPage _jspx_th_rn_page_0 = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_tagPool_rn_page_title.get(org.recipnet.site.content.rncontrols.RecipnetPage.class);
      _jspx_th_rn_page_0.setPageContext(_jspx_page_context);
      _jspx_th_rn_page_0.setParent(null);
      _jspx_th_rn_page_0.setTitle("Site Login");
      int _jspx_eval_rn_page_0 = _jspx_th_rn_page_0.doStartTag();
      if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        org.recipnet.site.content.rncontrols.RecipnetPage htmlPage = null;
        if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_rn_page_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_rn_page_0.doInitBody();
        }
        htmlPage = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_page_context.findAttribute("htmlPage");
        do {
          out.write('\n');
          out.write(' ');
          out.write(' ');
          if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write('\n');
          out.write(' ');
          out.write(' ');
          //  rn:authorizationChecker
          org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
          _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
          _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          _jspx_th_rn_authorizationChecker_1.setInvert(true);
          int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
          if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_rn_authorizationChecker_1.doInitBody();
            }
            do {
              out.write("\n    ");
              if (_jspx_meth_ctl_extraHtmlAttribute_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                return;
              out.write("\n    <center>\n      <table>\n        <tr>\n          <td align=\"left\">\n            <br />\n            <div class=\"errorMessage\">\n              ");
              //  rn:authorizationReasonMessage
              org.recipnet.site.content.rncontrols.AuthorizationReasonMessage authError = null;
              org.recipnet.site.content.rncontrols.AuthorizationReasonMessage _jspx_th_rn_authorizationReasonMessage_0 = (org.recipnet.site.content.rncontrols.AuthorizationReasonMessage) _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody.get(org.recipnet.site.content.rncontrols.AuthorizationReasonMessage.class);
              _jspx_th_rn_authorizationReasonMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationReasonMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_rn_authorizationReasonMessage_0.setId("authError");
              _jspx_th_rn_authorizationReasonMessage_0.setAuthorizationReasonParamName("authorizationFailedReason");
              int _jspx_eval_rn_authorizationReasonMessage_0 = _jspx_th_rn_authorizationReasonMessage_0.doStartTag();
              if (_jspx_th_rn_authorizationReasonMessage_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              authError = (org.recipnet.site.content.rncontrols.AuthorizationReasonMessage) _jspx_page_context.findAttribute("authError");
              _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody.reuse(_jspx_th_rn_authorizationReasonMessage_0);
              out.write("\n            </div>\n            ");
              //  ctl:errorMessage
              org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_0 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
              _jspx_th_ctl_errorMessage_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_errorMessage_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              _jspx_th_ctl_errorMessage_0.setErrorSupplier(authError);
              _jspx_th_ctl_errorMessage_0.setErrorFilter(
                AuthorizationReasonMessage.AUTHORIZATION_REASON_DISPLAYED);
              int _jspx_eval_ctl_errorMessage_0 = _jspx_th_ctl_errorMessage_0.doStartTag();
              if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_errorMessage_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_errorMessage_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_errorMessage_0.doInitBody();
                }
                do {
                  out.write("\n              <br />\n              <br />\n              If you are an authorized user, please log in below.\n            ");
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
              out.write("\n          </td>\n        </tr>\n      </table>\n      <br />\n    </center>\n    ");
              //  ctl:selfForm
              org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
              _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
              _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
              int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
              if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_ctl_selfForm_0.doInitBody();
                }
                do {
                  out.write("\n      <center>\n        <br/>\n        <table class=\"loginBox\">\n          <tr><th>User Name:</th></tr>\n          <tr><td>");
                  //  ctl:textbox
                  org.recipnet.common.controls.TextboxHtmlControl username = null;
                  org.recipnet.common.controls.TextboxHtmlControl _jspx_th_ctl_textbox_0 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_tagPool_ctl_textbox_id_nobody.get(org.recipnet.common.controls.TextboxHtmlControl.class);
                  _jspx_th_ctl_textbox_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_textbox_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_textbox_0.setId("username");
                  int _jspx_eval_ctl_textbox_0 = _jspx_th_ctl_textbox_0.doStartTag();
                  if (_jspx_th_ctl_textbox_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  username = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("username");
                  _jspx_tagPool_ctl_textbox_id_nobody.reuse(_jspx_th_ctl_textbox_0);
                  out.write("</td></tr>\n          <tr><th>Password:</th></tr>\n          <tr>\n            <td>\n              ");
                  //  ctl:textbox
                  org.recipnet.common.controls.TextboxHtmlControl password = null;
                  org.recipnet.common.controls.TextboxHtmlControl _jspx_th_ctl_textbox_1 = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_tagPool_ctl_textbox_maxLength_maskInput_id_nobody.get(org.recipnet.common.controls.TextboxHtmlControl.class);
                  _jspx_th_ctl_textbox_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_textbox_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_textbox_1.setMaskInput(true);
                  _jspx_th_ctl_textbox_1.setId("password");
                  _jspx_th_ctl_textbox_1.setMaxLength(8);
                  int _jspx_eval_ctl_textbox_1 = _jspx_th_ctl_textbox_1.doStartTag();
                  if (_jspx_th_ctl_textbox_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  password = (org.recipnet.common.controls.TextboxHtmlControl) _jspx_page_context.findAttribute("password");
                  _jspx_tagPool_ctl_textbox_maxLength_maskInput_id_nobody.reuse(_jspx_th_ctl_textbox_1);
                  out.write("\n            </td>\n          </tr>\n          <tr>\n            <td>\n              ");
                  //  ctl:hiddenString
                  org.recipnet.common.controls.HiddenStringHtmlControl origUrl = null;
                  org.recipnet.common.controls.HiddenStringHtmlControl _jspx_th_ctl_hiddenString_0 = (org.recipnet.common.controls.HiddenStringHtmlControl) _jspx_tagPool_ctl_hiddenString_initialValueFrom_id_nobody.get(org.recipnet.common.controls.HiddenStringHtmlControl.class);
                  _jspx_th_ctl_hiddenString_0.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_hiddenString_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_hiddenString_0.setId("origUrl");
                  _jspx_th_ctl_hiddenString_0.setInitialValueFrom("origUrl");
                  int _jspx_eval_ctl_hiddenString_0 = _jspx_th_ctl_hiddenString_0.doStartTag();
                  if (_jspx_th_ctl_hiddenString_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  origUrl = (org.recipnet.common.controls.HiddenStringHtmlControl) _jspx_page_context.findAttribute("origUrl");
                  _jspx_tagPool_ctl_hiddenString_initialValueFrom_id_nobody.reuse(_jspx_th_ctl_hiddenString_0);
                  out.write("\n              ");
                  //  rn:loginButton
                  org.recipnet.site.content.rncontrols.LoginButton login = null;
                  org.recipnet.site.content.rncontrols.LoginButton _jspx_th_rn_loginButton_0 = (org.recipnet.site.content.rncontrols.LoginButton) _jspx_tagPool_rn_loginButton_username_targetPageParamName_targetPageIfCanSeeLabSummary_targetPage_password_id_nobody.get(org.recipnet.site.content.rncontrols.LoginButton.class);
                  _jspx_th_rn_loginButton_0.setPageContext(_jspx_page_context);
                  _jspx_th_rn_loginButton_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_rn_loginButton_0.setId("login");
                  _jspx_th_rn_loginButton_0.setUsername( username.getValueAsString() );
                  _jspx_th_rn_loginButton_0.setPassword( password.getValueAsString() );
                  _jspx_th_rn_loginButton_0.setTargetPage("/index.jsp");
                  _jspx_th_rn_loginButton_0.setTargetPageIfCanSeeLabSummary("/lab/summary.jsp");
                  _jspx_th_rn_loginButton_0.setTargetPageParamName("origUrl");
                  int _jspx_eval_rn_loginButton_0 = _jspx_th_rn_loginButton_0.doStartTag();
                  if (_jspx_th_rn_loginButton_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  login = (org.recipnet.site.content.rncontrols.LoginButton) _jspx_page_context.findAttribute("login");
                  _jspx_tagPool_rn_loginButton_username_targetPageParamName_targetPageIfCanSeeLabSummary_targetPage_password_id_nobody.reuse(_jspx_th_rn_loginButton_0);
                  out.write("\n            </td>\n          </tr>\n        </table>\n      </center>\n      ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_errorSupplier.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier( login );
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n        <center>\n          <span class=\"errorMessage\">\n            Incorrect username or password.\n          </span>\n        </center>\n      ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_errorSupplier.reuse(_jspx_th_ctl_errorMessage_1);
                  out.write("\n    ");
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
              int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
              out = _jspx_page_context.popBody();
          }
          if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_rn_authorizationChecker_invert.reuse(_jspx_th_rn_authorizationChecker_1);
          out.write("\n\n  ");
          if (_jspx_meth_ctl_styleBlock_0(_jspx_th_rn_page_0, _jspx_page_context))
            return;
          out.write('\n');
          int evalDoAfterBody = _jspx_th_rn_page_0.doAfterBody();
          htmlPage = (org.recipnet.site.content.rncontrols.RecipnetPage) _jspx_page_context.findAttribute("htmlPage");
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_rn_page_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
          out = _jspx_page_context.popBody();
      }
      if (_jspx_th_rn_page_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_rn_page_title.reuse(_jspx_th_rn_page_0);
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

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n    ");
        if (_jspx_meth_ctl_redirect_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write('\n');
        out.write(' ');
        out.write(' ');
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_ctl_redirect_0.setTarget("/alreadyloggedin.jsp");
    _jspx_th_ctl_redirect_0.setPreserveParam("origUrl");
    _jspx_th_ctl_redirect_0.setPreserveParam1("authorizationFailedReason");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_ctl_extraHtmlAttribute_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:extraHtmlAttribute
    org.recipnet.common.controls.ExtraHtmlAttribute _jspx_th_ctl_extraHtmlAttribute_0 = (org.recipnet.common.controls.ExtraHtmlAttribute) _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.get(org.recipnet.common.controls.ExtraHtmlAttribute.class);
    _jspx_th_ctl_extraHtmlAttribute_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_extraHtmlAttribute_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_extraHtmlAttribute_0.setName("onLoad");
    _jspx_th_ctl_extraHtmlAttribute_0.setValue("document.forms[1].username.focus()");
    int _jspx_eval_ctl_extraHtmlAttribute_0 = _jspx_th_ctl_extraHtmlAttribute_0.doStartTag();
    if (_jspx_th_ctl_extraHtmlAttribute_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_extraHtmlAttribute_value_name_nobody.reuse(_jspx_th_ctl_extraHtmlAttribute_0);
    return false;
  }

  private boolean _jspx_meth_ctl_styleBlock_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_page_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:styleBlock
    org.recipnet.common.controls.HtmlPageStyleBlock _jspx_th_ctl_styleBlock_0 = (org.recipnet.common.controls.HtmlPageStyleBlock) _jspx_tagPool_ctl_styleBlock.get(org.recipnet.common.controls.HtmlPageStyleBlock.class);
    _jspx_th_ctl_styleBlock_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_styleBlock_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
    int _jspx_eval_ctl_styleBlock_0 = _jspx_th_ctl_styleBlock_0.doStartTag();
    if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_styleBlock_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_styleBlock_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_styleBlock_0.doInitBody();
      }
      do {
        out.write("\n    .errorMessage  { color: red; font-weight: bold;}\n    .loginBox     { border-style: solid; border-width: thin;\n        border-color: #CCCCCC; text-align: left; }\n  ");
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
