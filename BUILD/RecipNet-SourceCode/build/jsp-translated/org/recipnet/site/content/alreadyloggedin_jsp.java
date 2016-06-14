package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.recipnet.site.content.rncontrols.AuthorizationReasonMessage;
import org.recipnet.site.content.rncontrols.UserField;

public final class alreadyloggedin_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(2);
    _jspx_dependants.add("/WEB-INF/controls.tld");
    _jspx_dependants.add("/WEB-INF/rncontrols.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_page_title;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_selfForm;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_rn_logout_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_ctl_styleBlock;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_rn_page_title = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_selfForm = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_rn_logout_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_ctl_styleBlock = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_rn_page_title.release();
    _jspx_tagPool_ctl_selfForm.release();
    _jspx_tagPool_rn_authorizationChecker_invert.release();
    _jspx_tagPool_ctl_redirect_target_nobody.release();
    _jspx_tagPool_rn_authorizationChecker.release();
    _jspx_tagPool_rn_authorizationReasonMessage_id_authorizationReasonParamName_nobody.release();
    _jspx_tagPool_ctl_errorMessage_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.release();
    _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.release();
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.release();
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.release();
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.release();
    _jspx_tagPool_rn_logout_nobody.release();
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.release();
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

      out.write("\n\n\n\n\n\n");
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
          //  ctl:selfForm
          org.recipnet.common.controls.FormHtmlElement _jspx_th_ctl_selfForm_0 = (org.recipnet.common.controls.FormHtmlElement) _jspx_tagPool_ctl_selfForm.get(org.recipnet.common.controls.FormHtmlElement.class);
          _jspx_th_ctl_selfForm_0.setPageContext(_jspx_page_context);
          _jspx_th_ctl_selfForm_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_page_0);
          int _jspx_eval_ctl_selfForm_0 = _jspx_th_ctl_selfForm_0.doStartTag();
          if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_ctl_selfForm_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_ctl_selfForm_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_ctl_selfForm_0.doInitBody();
            }
            do {
              out.write("\n    ");
              if (_jspx_meth_rn_authorizationChecker_0(_jspx_th_ctl_selfForm_0, _jspx_page_context))
                return;
              out.write("\n    ");
              //  rn:authorizationChecker
              org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_1 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
              _jspx_th_rn_authorizationChecker_1.setPageContext(_jspx_page_context);
              _jspx_th_rn_authorizationChecker_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
              int _jspx_eval_rn_authorizationChecker_1 = _jspx_th_rn_authorizationChecker_1.doStartTag();
              if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_rn_authorizationChecker_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_rn_authorizationChecker_1.doInitBody();
                }
                do {
                  out.write("\n      <center>\n        <table width=\"50%\">\n          <tr>\n            <td align=\"left\">\n              <br />\n              <div class=\"errorMessage\">\n                ");
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
                  out.write("\n              </div>\n              ");
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
                      out.write("\n                <br />\n                <p>\n                  You are currently logged in as\n                  <strong>\n                    ");
                      //  rn:userField
                      org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_0 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                      _jspx_th_rn_userField_0.setPageContext(_jspx_page_context);
                      _jspx_th_rn_userField_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_0);
                      _jspx_th_rn_userField_0.setFieldCode( UserField.USER_NAME );
                      _jspx_th_rn_userField_0.setDisplayAsLabel(true);
                      int _jspx_eval_rn_userField_0 = _jspx_th_rn_userField_0.doStartTag();
                      if (_jspx_th_rn_userField_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_userField_0);
                      out.write("</strong>; click logout if you\n                 wish to attempt this action using a different user account.\n                </p>\n              ");
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
                  out.write("\n              ");
                  //  ctl:errorMessage
                  org.recipnet.common.controls.ErrorChecker _jspx_th_ctl_errorMessage_1 = (org.recipnet.common.controls.ErrorChecker) _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.get(org.recipnet.common.controls.ErrorChecker.class);
                  _jspx_th_ctl_errorMessage_1.setPageContext(_jspx_page_context);
                  _jspx_th_ctl_errorMessage_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
                  _jspx_th_ctl_errorMessage_1.setErrorSupplier(authError);
                  _jspx_th_ctl_errorMessage_1.setErrorFilter(
                  AuthorizationReasonMessage.AUTHORIZATION_REASON_DISPLAYED);
                  _jspx_th_ctl_errorMessage_1.setInvertFilter(true);
                  int _jspx_eval_ctl_errorMessage_1 = _jspx_th_ctl_errorMessage_1.doStartTag();
                  if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_ctl_errorMessage_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_ctl_errorMessage_1.doInitBody();
                    }
                    do {
                      out.write("\n                <p>\n                  You are already logged in as\n                  <strong>");
                      //  rn:userField
                      org.recipnet.site.content.rncontrols.UserField _jspx_th_rn_userField_1 = (org.recipnet.site.content.rncontrols.UserField) _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.get(org.recipnet.site.content.rncontrols.UserField.class);
                      _jspx_th_rn_userField_1.setPageContext(_jspx_page_context);
                      _jspx_th_rn_userField_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_errorMessage_1);
                      _jspx_th_rn_userField_1.setFieldCode( UserField.USER_NAME );
                      _jspx_th_rn_userField_1.setDisplayAsLabel(true);
                      int _jspx_eval_rn_userField_1 = _jspx_th_rn_userField_1.doStartTag();
                      if (_jspx_th_rn_userField_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                        return;
                      _jspx_tagPool_rn_userField_fieldCode_displayAsLabel_nobody.reuse(_jspx_th_rn_userField_1);
                      out.write("</strong>!\n                </p>\n                <p>\n                  To log in as a different user, click 'logout'.\n                </p>\n              ");
                      int evalDoAfterBody = _jspx_th_ctl_errorMessage_1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_ctl_errorMessage_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                      out = _jspx_page_context.popBody();
                  }
                  if (_jspx_th_ctl_errorMessage_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                    return;
                  _jspx_tagPool_ctl_errorMessage_invertFilter_errorSupplier_errorFilter.reuse(_jspx_th_ctl_errorMessage_1);
                  out.write("\n              <p>\n                Otherwise you may use the navigation links above to perform\n                an action that you are authorized to perform or click cancel to\n                ");
                  if (_jspx_meth_rn_authorizationChecker_2(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n                ");
                  if (_jspx_meth_rn_authorizationChecker_3(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n              </p>\n              ");
                  if (_jspx_meth_ctl_button_0(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n              ");
                  if (_jspx_meth_rn_authorizationChecker_4(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n              ");
                  if (_jspx_meth_rn_authorizationChecker_5(_jspx_th_rn_authorizationChecker_1, _jspx_page_context))
                    return;
                  out.write("\n            </td>\n          </tr>\n        </table>\n      </center>\n    ");
                  int evalDoAfterBody = _jspx_th_rn_authorizationChecker_1.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_rn_authorizationChecker_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_rn_authorizationChecker_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_rn_authorizationChecker.reuse(_jspx_th_rn_authorizationChecker_1);
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
          out.write('\n');
          out.write(' ');
          out.write(' ');
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

  private boolean _jspx_meth_rn_authorizationChecker_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_selfForm_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_0 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_selfForm_0);
    _jspx_th_rn_authorizationChecker_0.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_0 = _jspx_th_rn_authorizationChecker_0.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_0.doInitBody();
      }
      do {
        out.write("\n      ");
        if (_jspx_meth_ctl_redirect_0(_jspx_th_rn_authorizationChecker_0, _jspx_page_context))
          return true;
        out.write("\n    ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_invert.reuse(_jspx_th_rn_authorizationChecker_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_0 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_0);
    _jspx_th_ctl_redirect_0.setTarget("/login.jsp");
    int _jspx_eval_ctl_redirect_0 = _jspx_th_ctl_redirect_0.doStartTag();
    if (_jspx_th_ctl_redirect_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_0);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_2 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_2.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_authorizationChecker_2.setCanSeeLabSummary(true);
    int _jspx_eval_rn_authorizationChecker_2 = _jspx_th_rn_authorizationChecker_2.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_2.doInitBody();
      }
      do {
        out.write("\n                  go to the lab summary page.\n                ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_2);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_3(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_3 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_3.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_authorizationChecker_3.setCanSeeLabSummary(true);
    _jspx_th_rn_authorizationChecker_3.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_3 = _jspx_th_rn_authorizationChecker_3.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_3.doInitBody();
      }
      do {
        out.write("\n                  go to the site info page.\n                ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_3);
    return false;
  }

  private boolean _jspx_meth_ctl_button_0(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_0 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_0.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_ctl_button_0.setSuppressInsteadOfSkip(true);
    _jspx_th_ctl_button_0.setLabel("Logout");
    int _jspx_eval_ctl_button_0 = _jspx_th_ctl_button_0.doStartTag();
    if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_button_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_button_0.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_rn_logout_0(_jspx_th_ctl_button_0, _jspx_page_context))
          return true;
        out.write("\n                ");
        if (_jspx_meth_ctl_redirect_1(_jspx_th_ctl_button_0, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_ctl_button_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_button_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_button_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.reuse(_jspx_th_ctl_button_0);
    return false;
  }

  private boolean _jspx_meth_rn_logout_0(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:logout
    org.recipnet.site.content.rncontrols.Logout _jspx_th_rn_logout_0 = (org.recipnet.site.content.rncontrols.Logout) _jspx_tagPool_rn_logout_nobody.get(org.recipnet.site.content.rncontrols.Logout.class);
    _jspx_th_rn_logout_0.setPageContext(_jspx_page_context);
    _jspx_th_rn_logout_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_0);
    int _jspx_eval_rn_logout_0 = _jspx_th_rn_logout_0.doStartTag();
    if (_jspx_th_rn_logout_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_logout_nobody.reuse(_jspx_th_rn_logout_0);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_1(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_1 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_0);
    _jspx_th_ctl_redirect_1.setTarget("/login.jsp");
    _jspx_th_ctl_redirect_1.setPreserveParam("origUrl");
    _jspx_th_ctl_redirect_1.setPreserveParam1("authorizationFailedReason");
    int _jspx_eval_ctl_redirect_1 = _jspx_th_ctl_redirect_1.doStartTag();
    if (_jspx_th_ctl_redirect_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_preserveParam1_preserveParam_nobody.reuse(_jspx_th_ctl_redirect_1);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_4(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_4 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_4.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_authorizationChecker_4.setCanSeeLabSummary(true);
    int _jspx_eval_rn_authorizationChecker_4 = _jspx_th_rn_authorizationChecker_4.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_4.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_ctl_button_1(_jspx_th_rn_authorizationChecker_4, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_4);
    return false;
  }

  private boolean _jspx_meth_ctl_button_1(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_1 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_1.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_4);
    _jspx_th_ctl_button_1.setLabel("Cancel");
    _jspx_th_ctl_button_1.setSuppressInsteadOfSkip(true);
    int _jspx_eval_ctl_button_1 = _jspx_th_ctl_button_1.doStartTag();
    if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_button_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_button_1.doInitBody();
      }
      do {
        out.write("\n                  ");
        if (_jspx_meth_ctl_redirect_2(_jspx_th_ctl_button_1, _jspx_page_context))
          return true;
        out.write("\n                ");
        int evalDoAfterBody = _jspx_th_ctl_button_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_button_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_button_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.reuse(_jspx_th_ctl_button_1);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_2(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_2 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_1);
    _jspx_th_ctl_redirect_2.setTarget("/lab/summary.jsp");
    int _jspx_eval_ctl_redirect_2 = _jspx_th_ctl_redirect_2.doStartTag();
    if (_jspx_th_ctl_redirect_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_2);
    return false;
  }

  private boolean _jspx_meth_rn_authorizationChecker_5(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  rn:authorizationChecker
    org.recipnet.site.content.rncontrols.AuthorizationChecker _jspx_th_rn_authorizationChecker_5 = (org.recipnet.site.content.rncontrols.AuthorizationChecker) _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.get(org.recipnet.site.content.rncontrols.AuthorizationChecker.class);
    _jspx_th_rn_authorizationChecker_5.setPageContext(_jspx_page_context);
    _jspx_th_rn_authorizationChecker_5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_1);
    _jspx_th_rn_authorizationChecker_5.setCanSeeLabSummary(true);
    _jspx_th_rn_authorizationChecker_5.setInvert(true);
    int _jspx_eval_rn_authorizationChecker_5 = _jspx_th_rn_authorizationChecker_5.doStartTag();
    if (_jspx_eval_rn_authorizationChecker_5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_rn_authorizationChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_rn_authorizationChecker_5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_rn_authorizationChecker_5.doInitBody();
      }
      do {
        out.write("\n                ");
        if (_jspx_meth_ctl_button_2(_jspx_th_rn_authorizationChecker_5, _jspx_page_context))
          return true;
        out.write("\n              ");
        int evalDoAfterBody = _jspx_th_rn_authorizationChecker_5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_rn_authorizationChecker_5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_rn_authorizationChecker_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_rn_authorizationChecker_invert_canSeeLabSummary.reuse(_jspx_th_rn_authorizationChecker_5);
    return false;
  }

  private boolean _jspx_meth_ctl_button_2(javax.servlet.jsp.tagext.JspTag _jspx_th_rn_authorizationChecker_5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:button
    org.recipnet.common.controls.ButtonHtmlControl _jspx_th_ctl_button_2 = (org.recipnet.common.controls.ButtonHtmlControl) _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.get(org.recipnet.common.controls.ButtonHtmlControl.class);
    _jspx_th_ctl_button_2.setPageContext(_jspx_page_context);
    _jspx_th_ctl_button_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_rn_authorizationChecker_5);
    _jspx_th_ctl_button_2.setLabel("Cancel");
    _jspx_th_ctl_button_2.setSuppressInsteadOfSkip(true);
    int _jspx_eval_ctl_button_2 = _jspx_th_ctl_button_2.doStartTag();
    if (_jspx_eval_ctl_button_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_ctl_button_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_ctl_button_2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_ctl_button_2.doInitBody();
      }
      do {
        out.write("\n                  ");
        if (_jspx_meth_ctl_redirect_3(_jspx_th_ctl_button_2, _jspx_page_context))
          return true;
        out.write("\n                ");
        int evalDoAfterBody = _jspx_th_ctl_button_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_ctl_button_2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_ctl_button_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_button_suppressInsteadOfSkip_label.reuse(_jspx_th_ctl_button_2);
    return false;
  }

  private boolean _jspx_meth_ctl_redirect_3(javax.servlet.jsp.tagext.JspTag _jspx_th_ctl_button_2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ctl:redirect
    org.recipnet.common.controls.HtmlRedirect _jspx_th_ctl_redirect_3 = (org.recipnet.common.controls.HtmlRedirect) _jspx_tagPool_ctl_redirect_target_nobody.get(org.recipnet.common.controls.HtmlRedirect.class);
    _jspx_th_ctl_redirect_3.setPageContext(_jspx_page_context);
    _jspx_th_ctl_redirect_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_ctl_button_2);
    _jspx_th_ctl_redirect_3.setTarget("/index.jsp");
    int _jspx_eval_ctl_redirect_3 = _jspx_th_ctl_redirect_3.doStartTag();
    if (_jspx_th_ctl_redirect_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_ctl_redirect_target_nobody.reuse(_jspx_th_ctl_redirect_3);
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
        out.write("\n    .errorMessage  { color: red; font-weight: bold;}\n  ");
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
